from __future__ import annotations

from typing import Iterable, List, Tuple


DAY_MINUTES = 24 * 60


def parse_hhmm(value: str) -> int:
    text = value.strip()
    parts = text.split(":")
    if len(parts) != 2:
        raise ValueError(f"Heure invalide: {value}")
    hour = int(parts[0])
    minute = int(parts[1])
    if hour < 0 or hour > 23 or minute < 0 or minute > 59:
        raise ValueError(f"Heure invalide: {value}")

    return hour * 60 + minute
    

def minute_to_hhmm(minute: int) -> str:
    minute = minute % DAY_MINUTES
    hour = minute // 60
    mins = minute % 60
    return f"{hour:02d}:{mins:02d}"


def split_interval(start_minute: int, end_minute: int) -> List[Tuple[int, int]]:
    if start_minute == end_minute:
        return [(0, DAY_MINUTES)]
    if end_minute > start_minute:
        return [(start_minute, end_minute)]
    return [(start_minute, DAY_MINUTES), (0, end_minute)]


def overlap(a_start: int, a_end: int, b_start: int, b_end: int) -> Tuple[int, int] | None:
    start = max(a_start, b_start)
    end = min(a_end, b_end)
    if start >= end:
        return None
    return start, end


def interval_duration_minutes(intervals: Iterable[Tuple[int, int]]) -> int:
    total = 0
    for start, end in intervals:
        total += end - start
    return total
