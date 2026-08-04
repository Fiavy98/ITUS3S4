from __future__ import annotations

from collections import defaultdict
from math import ceil
from typing import Iterable, List, Tuple

from .models import DeviceUsage, PanelTypeInput, PanelTypePlan, SimulationConfig, SimulationResult
from .time_utils import overlap, split_interval


def _build_events_for_period(devices: Iterable[DeviceUsage], period_intervals: List[Tuple[int, int]]) -> dict[int, float]:
    events: dict[int, float] = defaultdict(float)

    for device in devices:
        device_intervals = split_interval(device.start_minute, device.end_minute)
        for d_start, d_end in device_intervals:
            for p_start, p_end in period_intervals:
                inter = overlap(d_start, d_end, p_start, p_end)
                if inter is None:
                    continue
                i_start, i_end = inter
                events[i_start] += device.power_w
                events[i_end] -= device.power_w

    return events


def _build_full_day_events(devices: Iterable[DeviceUsage]) -> dict[int, float]:
    events: dict[int, float] = defaultdict(float)

    for device in devices:
        device_intervals = split_interval(device.start_minute, device.end_minute)
        for i_start, i_end in device_intervals:
            events[i_start] += device.power_w
            events[i_end] -= device.power_w

    return events


def _max_load_from_events(events: dict[int, float]) -> float:
    if not events:
        return 0.0

    current = 0.0
    max_value = 0.0
    for minute in sorted(events.keys()):
        current += events[minute]
        if current > max_value:
            max_value = current
    return max_value


def _energy_wh_in_period(devices: Iterable[DeviceUsage], period_intervals: List[Tuple[int, int]]) -> float:
    total_wh = 0.0
    for device in devices:
        device_intervals = split_interval(device.start_minute, device.end_minute)
        for d_start, d_end in device_intervals:
            for p_start, p_end in period_intervals:
                inter = overlap(d_start, d_end, p_start, p_end)
                if inter is None:
                    continue
                i_start, i_end = inter
                duration_hours = (i_end - i_start) / 60.0
                total_wh += device.power_w * duration_hours
    return total_wh


def _events_to_minute_load(events: dict[int, float]) -> List[float]:
    loads = [0.0] * 1440
    current = 0.0
    previous = 0
    for minute in sorted(events.keys()):
        minute = max(0, min(1440, minute))
        for i in range(previous, minute):
            loads[i] = current
        current += events[minute]
        previous = minute
    for i in range(previous, 1440):
        loads[i] = current
    return loads


def _surplus_energy_wh(loads: List[float], intervals: List[Tuple[int, int]], supply_w: float) -> float:
    total_wh = 0.0
    for start, end in intervals:
        for minute in range(start, end):
            surplus = max(0.0, supply_w - loads[minute])
            total_wh += surplus / 60.0
    return total_wh


def run_simulation(config: SimulationConfig, devices: List[DeviceUsage]) -> SimulationResult:
    reduction = config.evening_reduction_pct / 100.0
    evening_factor = 1.0 - reduction

    if evening_factor <= 0:
        raise ValueError("n% (reduction soiree) doit etre strictement inferieur a 100.")
    if config.reference_wh <= 0:
        raise ValueError("Le chiffre de reference en Wh doit etre strictement positif.")

    panel_p1_factor = config.panel_p1_capacity_pct / 100.0
    panel_p2_factor = config.panel_p2_capacity_pct / 100.0
    if panel_p1_factor <= 0:
        raise ValueError("Capacite P1 doit etre strictement positive.")
    if panel_p2_factor <= 0:
        raise ValueError("Capacite P2 doit etre strictement positive.")

    day_intervals = split_interval(config.day.start_minute, config.day.end_minute)
    evening_intervals = split_interval(config.evening.start_minute, config.evening.end_minute)
    day_events = _build_events_for_period(devices, day_intervals)
    evening_events = _build_events_for_period(devices, evening_intervals)
    full_day_events = _build_full_day_events(devices)

    day_peak_load_w = _max_load_from_events(day_events)
    evening_peak_load_w = _max_load_from_events(evening_events)
    full_day_peak_load_w = _max_load_from_events(full_day_events)

    # New 21-04 rule: no battery and no night contribution in sizing.
    battery_theoretical_wh = 0.0
    battery_practical_wh = 0.0
    charge_base_w = 0.0

    day_requirement = day_peak_load_w
    evening_requirement = evening_peak_load_w / evening_factor
    panel_theoretical_w = max(day_requirement, evening_requirement)

    # P1 and P2 share the same theoretical power; each has its own practical sizing.
    panel_p1_w = panel_theoretical_w / panel_p1_factor
    panel_p2_w = panel_theoretical_w / panel_p2_factor

    # Keep legacy field as the P1 practical value (old behavior before Alea extension).
    panel_practical_w = panel_p1_w
    converter_required_w = full_day_peak_load_w * 2.0

    day_loads = _events_to_minute_load(day_events)
    evening_loads = _events_to_minute_load(evening_events)
    available_day_wh = _surplus_energy_wh(day_loads, day_intervals, panel_theoretical_w)
    available_evening_wh = _surplus_energy_wh(evening_loads, evening_intervals, panel_theoretical_w * evening_factor)
    available_energy_wh = available_day_wh + available_evening_wh

    ordinary_revenue = (available_energy_wh / config.reference_wh) * config.ordinary_price_per_reference
    holiday_revenue = (available_energy_wh / config.reference_wh) * config.holiday_price_per_reference

    return SimulationResult(
        panel_theoretical_w=panel_theoretical_w,
        panel_practical_w=panel_practical_w,
        panel_p1_w=panel_p1_w,
        panel_p2_w=panel_p2_w,
        battery_theoretical_wh=battery_theoretical_wh,
        battery_practical_wh=battery_practical_wh,
        charge_base_w=charge_base_w,
        day_peak_load_w=day_peak_load_w,
        evening_peak_load_w=evening_peak_load_w,
        night_peak_load_w=0.0,
        full_day_peak_load_w=full_day_peak_load_w,
        converter_required_w=converter_required_w,
        available_energy_wh=available_energy_wh,
        ordinary_revenue=ordinary_revenue,
        holiday_revenue=holiday_revenue,
    )


def compute_panel_type_plans(panel_theoretical_w: float, panel_types: List[PanelTypeInput]) -> List[PanelTypePlan]:
    plans: List[PanelTypePlan] = []

    for panel in panel_types:
        eff_factor = panel.efficiency_pct / 100.0
        if eff_factor <= 0:
            raise ValueError(f"Rendement invalide pour {panel.label}: doit etre > 0%.")
        if panel.practical_unit_w <= 0:
            raise ValueError(f"Energie unitaire pratique invalide pour {panel.label}: doit etre > 0.")
        if panel.unit_price < 0:
            raise ValueError(f"Prix unitaire invalide pour {panel.label}: doit etre >= 0.")

        required_practical_w = panel_theoretical_w / eff_factor
        quantity = max(1, ceil(required_practical_w / panel.practical_unit_w))
        total_price = quantity * panel.unit_price

        plans.append(
            PanelTypePlan(
                label=panel.label,
                unit_price=panel.unit_price,
                practical_unit_w=panel.practical_unit_w,
                efficiency_pct=panel.efficiency_pct,
                required_practical_w=required_practical_w,
                quantity=quantity,
                total_price=total_price,
            )
        )

    return plans
