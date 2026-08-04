# Solar Panel Project

This project matches solar panels and batteries to electrical devices based on their power requirements and usage patterns.

## Setup

### 1. Install MySQL Server (if not already installed)
```bash
sudo apt-get update
sudo apt-get install mysql-server
```

### 2. Start MySQL Service
```bash
sudo service mysql start
```

### 3. Install Python Dependencies
```bash
# Navigate to the project directory
cd solar_project

# Create and activate virtual environment (recommended)
python3 -m venv venv
source venv/bin/activate

# Install requirements
pip install -r requirements.txt
```

### 4. Initialize the Database
```bash
python3 setup_db.py
```

## Usage

Run the application:
```bash
python3 main.py
```

The GUI has two tabs:
- **Ajouter Matériel**: Add a new device with name, usage hours, time slot, and power consumption.
- **Destinations**: View the assigned solar panels for each device.

## Algorithm

The system calculates required panel power as 2x the device wattage and battery capacity as ~13.33x the device wattage, then assigns the smallest compatible panel.

## Troubleshooting

- **MySQL not running**: Run `sudo service mysql start`
- **Connection refused**: Ensure MySQL is running and listening on localhost:3306
- **Database not initialized**: Run `python3 setup_db.py`
- **Module not found**: Make sure you've activated the virtual environment and installed dependencies