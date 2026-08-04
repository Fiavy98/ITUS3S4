# Configuration de MySQL pour le projet Panneau Solaire

## Checklist de mise en place

### 1. Installation de MySQL

**Linux (Ubuntu/Debian)**
```bash
sudo apt-get update
sudo apt-get install mysql-server mysql-client
sudo mysql_secure_installation  # Configuration initiale
```

**macOS (Homebrew)**
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

**Windows**
- Télécharger depuis: https://dev.mysql.com/downloads/mysql/
- Installer et configurer

### 2. Démarrer le service MySQL

**Linux**
```bash
sudo systemctl start mysql
# Ou
sudo systemctl start mysql@5.7  # Pour les versions spécifiques
```

**macOS**
```bash
brew services start mysql
```

**Windows**
```cmd
net start MySQL80
```

### 3. Créer la base de données

```bash
# Copier le schéma
mysql -u root -p < sql/schema.sql
```

Ou manuellement:
```bash
mysql -u root -p
```

Puis exécuter:
```sql
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
```

### 4. Configurer l'application

**Option 1: Variables d'environnement**
```bash
export PANNEAU_DB_SERVER=localhost
export PANNEAU_DB_PORT=3306
export PANNEAU_DB_NAME=PANNEAUDB
export PANNEAU_DB_USER=root
export PANNEAU_DB_PASSWORD=votre_mot_de_passe
export PANNEAU_DB_AUTO_SAVE=1
```

**Option 2: Fichier .env**
```bash
cp .env.example .env
# Modifier .env avec vos paramètres
```

### 5. Installer les dépendances

```bash
pip install -r requirements.txt
```

### 6. Lancer l'application

```bash
python main.py
```

## Vérifier la connexion

Avant de lancer l'application, vous pouvez tester la connexion:

```bash
python3 << 'TEST_EOF'
import mysql.connector

try:
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="",  # Votre mot de passe
        database="PANNEAUDB"
    )
    print("✓ Connexion réussie!")
    cursor = conn.cursor()
    cursor.execute("SELECT COUNT(*) FROM simulations")
    print(f"  Simulations en base: {cursor.fetchone()[0]}")
    cursor.close()
    conn.close()
except Exception as e:
    print(f"✗ Erreur: {e}")
TEST_EOF
```

## Troubleshooting

### "ERROR 1045 (28000): Access denied for user 'root'"
- Vérifier le mot de passe MySQL
- Réinitialiser le mot de passe si nécessaire: https://dev.mysql.com/doc/refman/8.0/en/resetting-permissions.html

### "ERROR 1049 (42000): Unknown database"
- S'assurer que la base `PANNEAUDB` existe:
  ```bash
  mysql -u root -p -e "SHOW DATABASES;"
  ```

### "Connection refused"
- Vérifier que MySQL est démarré
- Vérifier le port (défaut: 3306)
- Vérifier l'adresse du serveur

## Migration depuis MSSQL

Si vous aviez des données en MSSQL, utilisez un outil comme:
- MySQL Workbench (Migration Wizard)
- Navicat
- Or write a custom Python script

