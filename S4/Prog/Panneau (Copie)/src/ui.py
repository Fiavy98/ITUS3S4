from __future__ import annotations

import os
from typing import List, Optional

from PyQt5.QtWidgets import (
    QApplication,
    QComboBox,
    QFormLayout,
    QFrame,
    QGridLayout,
    QGroupBox,
    QHBoxLayout,
    QHeaderView,
    QLabel,
    QLineEdit,
    QMainWindow,
    QMessageBox,
    QPushButton,
    QTableWidget,
    QTableWidgetItem,
    QVBoxLayout,
    QWidget,
    QTabWidget,
    QSplitter,
    QScrollArea,
    QProgressBar,
    QTextEdit,
)

from .calculator import run_simulation
from .db import DatabaseClient, summarize_db_exception
from .models import DeviceUsage, SimulationConfig, SimulationResult, TimePeriod
from .time_utils import minute_to_hhmm, parse_hhmm


class MainWindow(QMainWindow):
    def __init__(self) -> None:
        super().__init__()
        self.setWindowTitle("Gestion Energie Solaire - Panneau Solaire")
        self.resize(1400, 900)
        self._apply_style()

        self.last_result: Optional[SimulationResult] = None

        central = QWidget()
        self.setCentralWidget(central)
        root = QVBoxLayout(central)
        root.setContentsMargins(16, 14, 16, 14)
        root.setSpacing(10)

        # ── Titre ──────────────────────────────────────────────────────────
        title = QLabel("ETU 4373")
        title.setObjectName("titleLabel")
        subtitle = QLabel("")
        subtitle.setObjectName("subtitleLabel")
        root.addWidget(title)
        root.addWidget(subtitle)

        # ── Zone principale : 3 colonnes côte à côte ───────────────────────
        #   [Config + Coefficients]  |  [Appareils]  |  [Résultats]
        columns = QHBoxLayout()
        columns.setSpacing(14)

        # Colonne gauche : Périodes + Coefficients
        left_col = QVBoxLayout()
        left_col.setSpacing(10)
        left_col.addWidget(self._build_periods_group())
        left_col.addWidget(self._build_coeff_group())
        left_col.addStretch(1)
        left_widget = QWidget()
        left_widget.setLayout(left_col)
        left_widget.setMaximumWidth(320)

        # Colonne centre : Appareils (tableau grand)
        center_widget = self._build_devices_group()

        # Colonne droite : Résultats
        right_col = QVBoxLayout()
        right_col.setSpacing(10)
        right_col.addWidget(self._build_results_group())
        right_col.addStretch(1)
        right_widget = QWidget()
        right_widget.setLayout(right_col)
        right_widget.setMinimumWidth(280)
        right_widget.setMaximumWidth(340)

        columns.addWidget(left_widget, 0)
        columns.addWidget(center_widget, 1)
        columns.addWidget(right_widget, 0)

        root.addLayout(columns, 1)

        # ── Barre d'actions en bas ─────────────────────────────────────────
        actions_layout = QHBoxLayout()

        self.calculate_btn = QPushButton("🚀 Calculer")
        self.calculate_btn.setObjectName("primaryButton")
        self.calculate_btn.clicked.connect(self.on_calculate)
        actions_layout.addWidget(self.calculate_btn)

        self.save_btn = QPushButton("💾 Sauvegarder")
        self.save_btn.clicked.connect(self.on_save_simulation)
        actions_layout.addWidget(self.save_btn)

        self.clear_btn = QPushButton("🗑️ Tout Effacer")
        self.clear_btn.clicked.connect(self.on_clear_all)
        actions_layout.addWidget(self.clear_btn)

        actions_layout.addStretch(1)

        self.save_status_label = QLabel("Base de données: Non connectée")
        self.save_status_label.setObjectName("dbStatusLabel")
        actions_layout.addWidget(self.save_status_label)

        root.addLayout(actions_layout)

    def _apply_style(self) -> None:
        self.setStyleSheet(
            """
            QMainWindow {
                background: #edf2f8;
            }
            QFrame#columnFrame {
                background: #f8fbff;
                border: 1px solid #d5e2f0;
                border-radius: 12px;
            }
            QLabel#titleLabel {
                color: #0f243d;
                font-size: 25px;
                font-weight: 700;
            }
            QLabel#subtitleLabel {
                color: #4a6078;
                font-size: 13px;
                margin-bottom: 4px;
            }
            QGroupBox {
                background: #ffffff;
                border: 1px solid #d1dfef;
                border-radius: 10px;
                margin-top: 8px;
                font-size: 13px;
                font-weight: 600;
                color: #21334c;
                padding-top: 8px;
            }
            QGroupBox::title {
                subcontrol-origin: margin;
                left: 10px;
                padding: 0 6px;
            }
            QLineEdit, QComboBox {
                background: #fbfdff;
                border: 1px solid #c9d8ea;
                border-radius: 7px;
                padding: 7px 8px;
                font-size: 13px;
            }
            QLineEdit:focus, QComboBox:focus {
                border: 1px solid #3f8cff;
                background: #ffffff;
            }
            QPushButton {
                background: #eef4fb;
                border: 1px solid #cad7e7;
                border-radius: 8px;
                color: #1f3552;
                font-weight: 600;
                padding: 8px 12px;
            }
            QPushButton:hover {
                background: #e3eefb;
            }
            QPushButton#primaryButton {
                background: #0f77d9;
                color: white;
                border: none;
                padding: 9px 14px;
            }
            QPushButton#primaryButton:hover {
                background: #0b67bc;
            }
            QTableWidget {
                background: #ffffff;
                border: 1px solid #d7e2ef;
                border-radius: 8px;
                gridline-color: #e6edf5;
                alternate-background-color: #f8fbff;
                font-size: 13px;
            }
            QHeaderView::section {
                background: #ecf3fb;
                color: #233a59;
                padding: 7px;
                border: none;
                border-bottom: 1px solid #d5e3f2;
                font-weight: 700;
            }
            QFrame#resultCard {
                background: #f7fbff;
                border: 1px solid #d4e3f5;
                border-radius: 8px;
            }
            QLabel.resultTitle {
                color: #3e5e82;
                font-size: 12px;
            }
            QLabel.resultValue {
                color: #12253e;
                font-size: 20px;
                font-weight: 700;
            }
            """
        )

    def _build_periods_group(self) -> QGroupBox:
        group = QGroupBox("Periodes")
        layout = QGridLayout(group)
        layout.setHorizontalSpacing(12)
        layout.setVerticalSpacing(10)

        layout.addWidget(QLabel("Journee debut"), 0, 0)
        self.day_start = QLineEdit("06:00")
        layout.addWidget(self.day_start, 0, 1)

        layout.addWidget(QLabel("Journee fin"), 0, 2)
        self.day_end = QLineEdit("17:00")
        layout.addWidget(self.day_end, 0, 3)

        layout.addWidget(QLabel("Soiree debut"), 1, 0)
        self.evening_start = QLineEdit("17:00")
        layout.addWidget(self.evening_start, 1, 1)

        layout.addWidget(QLabel("Soiree fin"), 1, 2)
        self.evening_end = QLineEdit("19:00")
        layout.addWidget(self.evening_end, 1, 3)

        self.night_start = QLineEdit("19:00")
        self.night_end = QLineEdit("06:00")
        self.night_start.setVisible(False)
        self.night_end.setVisible(False)

        hint = QLabel("Heure HH:MM (24h), ex: 18:30")
        hint.setObjectName("subtitleLabel")
        layout.addWidget(hint, 2, 0, 1, 4)

        return group

    def _build_coeff_group(self) -> QGroupBox:
        group = QGroupBox("Coefficients")
        layout = QFormLayout(group)
        layout.setSpacing(10)

        self.n_reduction = QLineEdit("50")
        self.m_efficiency = QLineEdit("40")
        self.reference_wh = QLineEdit("100")
        self.ordinary_price = QLineEdit("1000")
        self.holiday_price = QLineEdit("2000")

        layout.addRow("Reduction soiree %", self.n_reduction)
        layout.addRow("Puissance panneau P1 %", self.m_efficiency)
        layout.addRow("Chiffre reference (Wh)", self.reference_wh)
        layout.addRow("Prix energie jour ordinaire", self.ordinary_price)
        layout.addRow("Prix energie jour ferie", self.holiday_price)

        return group

    def _build_devices_group(self) -> QGroupBox:
        group = QGroupBox("Materiaux")
        layout = QVBoxLayout(group)
        layout.setSpacing(10)

        load_row = QHBoxLayout()
        load_row.addWidget(QLabel("ID simulation"))
        self.simulation_id_input = QLineEdit("")
        self.simulation_id_input.setPlaceholderText("Ex: 24")
        load_row.addWidget(self.simulation_id_input)

        self.load_from_db_btn = QPushButton("Charger appareils depuis base")
        self.load_from_db_btn.clicked.connect(self.on_load_devices_from_db)
        load_row.addWidget(self.load_from_db_btn)
        load_row.addStretch(1)
        layout.addLayout(load_row)

        self.devices_table = QTableWidget(0, 5)
        self.devices_table.setHorizontalHeaderLabels(
            ["Nom", "Consommation", "Unite", "Debut (HH:MM)", "Fin (HH:MM)"]
        )
        self.devices_table.setAlternatingRowColors(True)
        self.devices_table.verticalHeader().setVisible(False)
        header = self.devices_table.horizontalHeader()
        header.setSectionResizeMode(0, QHeaderView.Stretch)
        header.setSectionResizeMode(1, QHeaderView.ResizeToContents)
        header.setSectionResizeMode(2, QHeaderView.ResizeToContents)
        header.setSectionResizeMode(3, QHeaderView.ResizeToContents)
        header.setSectionResizeMode(4, QHeaderView.ResizeToContents)
        self.devices_table.setMinimumHeight(420)
        layout.addWidget(self.devices_table)

        row_actions = QHBoxLayout()
        add_btn = QPushButton("Ajouter appareil")
        add_btn.clicked.connect(self.add_device_row)
        row_actions.addWidget(add_btn)

        remove_btn = QPushButton("Supprimer ligne")
        remove_btn.clicked.connect(self.remove_device_row)
        row_actions.addWidget(remove_btn)

        sample_btn = QPushButton("Exemple")
        sample_btn.clicked.connect(self.fill_sample)
        row_actions.addWidget(sample_btn)

        clear_btn = QPushButton("Vider")
        clear_btn.clicked.connect(self.clear_devices)
        row_actions.addWidget(clear_btn)

        row_actions.addStretch(1)
        layout.addLayout(row_actions)

        return group

    def _build_results_group(self) -> QGroupBox:
        group = QGroupBox("Resultats")
        layout = QVBoxLayout(group)
        layout.setSpacing(8)

        cards_layout = QGridLayout()
        cards_layout.setSpacing(9)

        self.res_panel_theoretical = self._build_result_card(cards_layout, 0, 0, "Puissance theorique panneau (W)")
        self.res_panel_practical = self._build_result_card(cards_layout, 1, 0, "Puissance pratique P1 (W)")
        self.res_available_energy = self._build_result_card(cards_layout, 0, 1, "Energie disponible cycle (Wh)")
        self.res_ordinary_revenue = self._build_result_card(cards_layout, 1, 1, "Gain jour ordinaire")
        self.res_holiday_revenue = self._build_result_card(cards_layout, 2, 1, "Gain jour ferie")
        self.res_day_peak = self._build_result_card(cards_layout, 2, 0, "Pic consommation journee (W)")
        self.res_evening_peak = self._build_result_card(cards_layout, 3, 0, "Pic consommation soiree (W)")

        layout.addLayout(cards_layout)

        return group

    def _build_result_card(self, layout: QGridLayout, row: int, col: int, title: str) -> QLabel:
        card = QFrame()
        card.setObjectName("resultCard")
        card_layout = QVBoxLayout(card)
        card_layout.setContentsMargins(10, 8, 10, 8)
        card_layout.setSpacing(3)

        title_label = QLabel(title)
        title_label.setProperty("class", "resultTitle")
        value_label = QLabel("-")
        value_label.setProperty("class", "resultValue")

        card_layout.addWidget(title_label)
        card_layout.addWidget(value_label)
        layout.addWidget(card, row, col)
        return value_label

    def add_device_row(self) -> None:
        row = self.devices_table.rowCount()
        self.devices_table.insertRow(row)

        self.devices_table.setItem(row, 0, QTableWidgetItem(""))
        self.devices_table.setItem(row, 1, QTableWidgetItem("0"))
        unit_box = QComboBox()
        unit_box.addItems(["W", "kW"])
        self.devices_table.setCellWidget(row, 2, unit_box)
        self.devices_table.setItem(row, 3, QTableWidgetItem("18:00"))
        self.devices_table.setItem(row, 4, QTableWidgetItem("20:00"))

    def remove_device_row(self) -> None:
        row = self.devices_table.currentRow()
        if row >= 0:
            self.devices_table.removeRow(row)

    def _db_client(self) -> DatabaseClient:
        server = os.getenv("PANNEAU_DB_SERVER", "localhost")
        database = os.getenv("PANNEAU_DB_NAME", "PANNEAUDB")
        port = int(os.getenv("PANNEAU_DB_PORT", "3306"))
        username = os.getenv("PANNEAU_DB_USER", "root")
        password = os.getenv("PANNEAU_DB_PASSWORD", "")

        return DatabaseClient(
            server=server,
            database=database,
            username=username,
            password=password,
            port=port,
        )

    def on_load_devices_from_db(self) -> None:
        simulation_id_text = self.simulation_id_input.text().strip()
        if not simulation_id_text:
            QMessageBox.warning(self, "Chargement", "Saisis un ID de simulation.")
            return

        try:
            simulation_id = int(simulation_id_text)
            if simulation_id <= 0:
                raise ValueError()
        except ValueError:
            QMessageBox.warning(self, "Chargement", "ID simulation invalide.")
            return

        try:
            client = self._db_client()
            devices = client.load_devices_for_simulation(simulation_id)
            if not devices:
                QMessageBox.information(self, "Chargement", "Aucun appareil trouve pour cet ID.")
                return

            self.clear_devices()
            for device in devices:
                self.add_device_row()
                row = self.devices_table.rowCount() - 1
                self.devices_table.item(row, 0).setText(device.name)
                self.devices_table.item(row, 1).setText(f"{device.power_w:.2f}".rstrip("0").rstrip("."))
                widget = self.devices_table.cellWidget(row, 2)
                if isinstance(widget, QComboBox):
                    widget.setCurrentText("W")
                self.devices_table.item(row, 3).setText(minute_to_hhmm(device.start_minute))
                self.devices_table.item(row, 4).setText(minute_to_hhmm(device.end_minute))

            self.save_status_label.setText(
                f"Chargement SQL: {len(devices)} appareil(s) importes depuis simulation #{simulation_id}"
            )
        except Exception as exc:
            QMessageBox.critical(self, "Chargement", f"Erreur SQL: {summarize_db_exception(exc)}")

    def clear_devices(self) -> None:
        self.devices_table.setRowCount(0)

    def fill_sample(self) -> None:
        self.devices_table.setRowCount(0)

        rows = [
            ("Radio", "10", "W", "06:00", "10:00"),
            ("Tele", "100", "W", "08:00", "12:00"),
            ("Ampoule", "50", "W", "20:00", "23:00"),
        ]

        for name, value, unit, start, end in rows:
            self.add_device_row()
            r = self.devices_table.rowCount() - 1
            self.devices_table.item(r, 0).setText(name)
            self.devices_table.item(r, 1).setText(value)
            casted = self.devices_table.cellWidget(r, 2)
            if isinstance(casted, QComboBox):
                casted.setCurrentText(unit)
            self.devices_table.item(r, 3).setText(start)
            self.devices_table.item(r, 4).setText(end)

    def _parse_config(self) -> SimulationConfig:
        day = TimePeriod("journee", parse_hhmm(self.day_start.text()), parse_hhmm(self.day_end.text()))
        evening = TimePeriod(
            "soiree", parse_hhmm(self.evening_start.text()), parse_hhmm(self.evening_end.text())
        )
        night = TimePeriod("nuit", parse_hhmm(self.night_start.text()), parse_hhmm(self.night_end.text()))

        return SimulationConfig(
            day=day,
            evening=evening,
            night=night,
            evening_reduction_pct=float(self.n_reduction.text()),
            panel_efficiency_pct=float(self.m_efficiency.text()),
            battery_margin_pct=0.0,
            reference_wh=float(self.reference_wh.text()),
            ordinary_price_per_reference=float(self.ordinary_price.text()),
            holiday_price_per_reference=float(self.holiday_price.text()),
            panel_p1_capacity_pct=float(self.m_efficiency.text()),
            panel_p2_capacity_pct=30.0,
        )

    def _parse_devices(self) -> List[DeviceUsage]:
        devices: List[DeviceUsage] = []
        for row in range(self.devices_table.rowCount()):
            name_item = self.devices_table.item(row, 0)
            power_item = self.devices_table.item(row, 1)
            start_item = self.devices_table.item(row, 3)
            end_item = self.devices_table.item(row, 4)

            if name_item is None or power_item is None or start_item is None or end_item is None:
                continue

            name = name_item.text().strip()
            if not name:
                raise ValueError(f"Nom appareil manquant a la ligne {row + 1}")

            power_value = float(power_item.text().strip())
            unit_widget = self.devices_table.cellWidget(row, 2)
            unit = "W"
            if isinstance(unit_widget, QComboBox):
                unit = unit_widget.currentText()

            power_w = power_value * 1000.0 if unit.lower() == "kw" else power_value

            start_min = parse_hhmm(start_item.text())
            end_min = parse_hhmm(end_item.text())

            devices.append(
                DeviceUsage(
                    name=name,
                    power_w=power_w,
                    start_minute=start_min,
                    end_minute=end_min,
                )
            )

        if not devices:
            raise ValueError("Ajoute au moins un appareil avant de calculer.")

        return devices

    def on_calculate(self) -> None:
        try:
            config = self._parse_config()
            devices = self._parse_devices()
            result = run_simulation(config, devices)

            self.last_result = result
            self._render_results(result)
            self._auto_save_to_sql(config, result, devices)
        except Exception as exc:
            QMessageBox.critical(self, "Erreur calcul", str(exc))

    def _render_results(self, result: SimulationResult) -> None:
        self.res_panel_theoretical.setText(f"{result.panel_theoretical_w:.2f}")
        self.res_panel_practical.setText(f"{result.panel_practical_w:.2f}")
        self.res_available_energy.setText(f"{result.available_energy_wh:.2f}")
        self.res_ordinary_revenue.setText(f"{result.ordinary_revenue:.2f} Ar")
        self.res_holiday_revenue.setText(f"{result.holiday_revenue:.2f} Ar")
        self.res_day_peak.setText(f"{result.day_peak_load_w:.2f}")
        self.res_evening_peak.setText(f"{result.evening_peak_load_w:.2f}")

    def _auto_save_to_sql(
        self,
        config: SimulationConfig,
        result: SimulationResult,
        devices: List[DeviceUsage],
    ) -> None:
        auto_save = os.getenv("PANNEAU_DB_AUTO_SAVE", "1").strip().lower()
        if auto_save in {"0", "false", "no", "off"}:
            self.save_status_label.setText("Stockage SQL: desactive (PANNEAU_DB_AUTO_SAVE)")
            return

        try:
            client = self._db_client()
            simulation_id = client.save_simulation(config, result, devices)
            server = os.getenv("PANNEAU_DB_SERVER", "localhost")
            database = os.getenv("PANNEAU_DB_NAME", "PanneauDB")
            self.save_status_label.setText(
                f"Stockage SQL: OK (simulation #{simulation_id} sur {server}/{database})"
            )
        except Exception as exc:
            self.save_status_label.setText(
                f"Stockage SQL: echec ({summarize_db_exception(exc)})"
            )

    def on_save_simulation(self) -> None:
        if self.last_result is None:
            QMessageBox.information(self, "Sauvegarder", "Lance d'abord une simulation.")
            return
        try:
            config = self._parse_config()
            devices = self._parse_devices()
            self._auto_save_to_sql(config, self.last_result, devices)
        except Exception as exc:
            QMessageBox.critical(self, "Erreur", str(exc))

    def on_clear_all(self) -> None:
        self.clear_devices()
        for lbl in [
            self.res_panel_theoretical, self.res_panel_practical,
            self.res_available_energy, self.res_day_peak,
            self.res_evening_peak, self.res_ordinary_revenue,
            self.res_holiday_revenue,
        ]:
            lbl.setText("-")
        self.save_status_label.setText("Formulaire reinitialise.")


def run_app() -> None:
    app = QApplication([])
    window = MainWindow()
    window.show()
    app.exec_()