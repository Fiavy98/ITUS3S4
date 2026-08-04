from __future__ import annotations

from typing import Iterable, Optional

import mysql.connector
from mysql.connector import Error as MySQLError

from .models import DeviceUsage, SimulationConfig, SimulationResult
from .time_utils import minute_to_hhmm


def summarize_db_exception(exc: Exception) -> str:
    text = str(exc).replace("\n", " ").strip()

    # Common MySQL login/database errors.
    if "1045" in text:
        return "Login MySQL refusé (1045): verifie utilisateur/mot de passe."
    if "1049" in text:
        return "Base introuvable (1049): verifie PANNEAU_DB_NAME."
    if "Connection refused" in text or "2003" in text:
        return "Serveur MySQL introuvable/inaccessible: verifie PANNEAU_DB_SERVER et le port."

    # Keep fallback short for UI readability.
    if len(text) > 180:
        return text[:177] + "..."
    return text


class DatabaseClient:
    def __init__(
        self,
        server: str,
        database: str,
        username: str = "root",
        password: str = "",
        port: int = 3306,
    ) -> None:
        self.server = server
        self.database = database
        self.username = username
        self.password = password
        self.port = port

    def _get_connection(self):
        try:
            return mysql.connector.connect(
                host=self.server,
                user=self.username,
                password=self.password,
                database=self.database,
                port=self.port,
            )
        except MySQLError as err:
            raise err

    def test_connection(self) -> None:
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute("SELECT 1")
            cursor.fetchone()
        finally:
            cursor.close()
            conn.close()

    def save_simulation(
        self,
        config: SimulationConfig,
        result: SimulationResult,
        devices: Iterable[DeviceUsage],
    ) -> int:
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute(
                """
                INSERT INTO simulations (
                    day_start, day_end,
                    evening_start, evening_end,
                    night_start, night_end,
                    evening_reduction_pct, panel_efficiency_pct, battery_margin_pct,
                    panel_theoretical_w, panel_practical_w,
                    battery_theoretical_wh, battery_practical_wh,
                    charge_base_w, day_peak_load_w, evening_peak_load_w
                )
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """,
                (
                    minute_to_hhmm(config.day.start_minute),
                    minute_to_hhmm(config.day.end_minute),
                    minute_to_hhmm(config.evening.start_minute),
                    minute_to_hhmm(config.evening.end_minute),
                    minute_to_hhmm(config.night.start_minute),
                    minute_to_hhmm(config.night.end_minute),
                    config.evening_reduction_pct,
                    config.panel_efficiency_pct,
                    config.battery_margin_pct,
                    result.panel_theoretical_w,
                    result.panel_practical_w,
                    result.battery_theoretical_wh,
                    result.battery_practical_wh,
                    result.charge_base_w,
                    result.day_peak_load_w,
                    result.evening_peak_load_w,
                )
            )
            simulation_id = cursor.lastrowid

            for device in devices:
                cursor.execute(
                    """
                    INSERT INTO simulation_devices (
                        simulation_id, device_name, power_w, start_time, end_time
                    )
                    VALUES (%s, %s, %s, %s, %s)
                    """,
                    (
                        simulation_id,
                        device.name,
                        device.power_w,
                        minute_to_hhmm(device.start_minute),
                        minute_to_hhmm(device.end_minute),
                    )
                )

            conn.commit()
            return simulation_id
        except Exception:
            conn.rollback()
            raise
        finally:
            cursor.close()
            conn.close()

    def load_devices_for_simulation(self, simulation_id: int) -> list[DeviceUsage]:
        conn = self._get_connection()
        try:
            cursor = conn.cursor()
            cursor.execute(
                """
                SELECT device_name, power_w, start_time, end_time
                FROM simulation_devices
                WHERE simulation_id = %s
                ORDER BY id ASC
                """,
                (simulation_id,)
            )
            rows = cursor.fetchall()

            devices: list[DeviceUsage] = []
            for row in rows:
                start_text = str(row[2])
                end_text = str(row[3])
                start_hour, start_minute = start_text.split(":")
                end_hour, end_minute = end_text.split(":")

                devices.append(
                    DeviceUsage(
                        name=str(row[0]),
                        power_w=float(row[1]),
                        start_minute=(int(start_hour) * 60) + int(start_minute),
                        end_minute=(int(end_hour) * 60) + int(end_minute),
                    )
                )

            return devices
        finally:
            cursor.close()
            conn.close()
