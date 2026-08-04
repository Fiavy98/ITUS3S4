from dataclasses import dataclass
from typing import Optional


@dataclass
class TimePeriod:
    name: str
    start_minute: int
    end_minute: int


@dataclass
class DeviceUsage:
    name: str
    power_w: float
    start_minute: int
    end_minute: int


@dataclass
class PanelTypeInput:
    label: str
    unit_price: float
    practical_unit_w: float
    efficiency_pct: float


@dataclass
class PanelTypePlan:
    label: str
    unit_price: float
    practical_unit_w: float
    efficiency_pct: float
    required_practical_w: float
    quantity: int
    total_price: float


@dataclass
class SimulationConfig:
    day: TimePeriod
    evening: TimePeriod
    night: TimePeriod
    evening_reduction_pct: float
    panel_efficiency_pct: float
    battery_margin_pct: float
    reference_wh: float = 100.0
    ordinary_price_per_reference: float = 0.0
    holiday_price_per_reference: float = 0.0
    panel_p1_capacity_pct: float = 40.0
    panel_p2_capacity_pct: float = 30.0


@dataclass
class SimulationResult:
    panel_theoretical_w: float
    panel_practical_w: float
    panel_p1_w: float
    panel_p2_w: float
    battery_theoretical_wh: float
    battery_practical_wh: float
    charge_base_w: float
    day_peak_load_w: float
    evening_peak_load_w: float
    night_peak_load_w: float
    full_day_peak_load_w: float
    converter_required_w: float
    available_energy_wh: float
    ordinary_revenue: float
    holiday_revenue: float
