CREATE DATABASE IF NOT EXISTS PANNEAUDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE PANNEAUDB;

CREATE TABLE simulations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    day_start CHAR(5) NOT NULL,
    day_end CHAR(5) NOT NULL,
    evening_start CHAR(5) NOT NULL,
    evening_end CHAR(5) NOT NULL,
    night_start CHAR(5) NOT NULL,
    night_end CHAR(5) NOT NULL,

    evening_reduction_pct FLOAT NOT NULL,
    panel_efficiency_pct FLOAT NOT NULL,
    battery_margin_pct FLOAT NOT NULL,

    panel_theoretical_w FLOAT NOT NULL,
    panel_practical_w FLOAT NOT NULL,
    battery_theoretical_wh FLOAT NOT NULL,
    battery_practical_wh FLOAT NOT NULL,

    charge_base_w FLOAT NOT NULL,
    day_peak_load_w FLOAT NOT NULL,
    evening_peak_load_w FLOAT NOT NULL
);

CREATE TABLE simulation_devices (
    id INT AUTO_INCREMENT PRIMARY KEY,
    simulation_id INT NOT NULL,
    device_name VARCHAR(120) NOT NULL,
    power_w FLOAT NOT NULL,
    start_time CHAR(5) NOT NULL,
    end_time CHAR(5) NOT NULL,
    CONSTRAINT FK_simulation_devices_simulation
        FOREIGN KEY (simulation_id) REFERENCES simulations(id) ON DELETE CASCADE
);
