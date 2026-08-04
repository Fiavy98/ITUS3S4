# Migration MSSQL vers MySQL

## Résumé des changements

Ce projet a été migré de **MSSQL Server** vers **MySQL** pour une meilleure portabilité et flexibilité.

### Fichiers modifiés

1. **requirements.txt**
   - Remplacé `pyodbc` par `mysql-connector-python`
   - Python module pour la connexion à MySQL

2. **sql/schema.sql**
   - Conversion de la syntaxe T-SQL (MSSQL) en SQL standard MySQL
   - Changements clés:
     - `INT IDENTITY(1,1)` → `INT AUTO_INCREMENT`
     - `DATETIME2` → `DATETIME`
     - `SYSDATETIME()` → `CURRENT_TIMESTAMP`
     - `NVARCHAR` → `VARCHAR`
     - Suppression des préfixes `dbo.`
     - Suppression des directives `GO`
     - Ajout de `ON DELETE CASCADE` pour les contraintes

3. **src/db.py**
   - Remplacé `pyodbc` par `mysql.connector`
   - Adaptation de la chaîne de connexion pour MySQL
   - Paramètres de requête: `?` → `%s`
   - `OUTPUT INSERTED.id` → `cursor.lastrowid`
   - Gestion des tuples au lieu des objets Row

4. **src/ui.py**
   - Mise à jour de la méthode `_db_client()`
   - Adaptation des variables d'environnement pour MySQL
   - Support du port MySQL (par défaut: 3306)

## Configuration MySQL

### Variables d'environnement requises

```bash
# Serveur MySQL
export PANNEAU_DB_SERVER=localhost

# Nom de la base de données (par défaut: PANNEAUDB)
export PANNEAU_DB_NAME=PANNEAUDB

# Port MySQL (par défaut: 3306)
export PANNEAU_DB_PORT=3306

# Identifiants MySQL
export PANNEAU_DB_USER=root
export PANNEAU_DB_PASSWORD=votre_mot_de_passe
```

### Étapes d'installation

1. **Installer MySQL Server** (si ce n'est pas déjà fait)
   ```bash
   # Ubuntu/Debian
   sudo apt-get install mysql-server

   # macOS (avec Homebrew)
   brew install mysql

   # Windows
   # Télécharger depuis https://dev.mysql.com/downloads/
   ```

2. **Créer la base de données**
   ```bash
   mysql -u root -p < sql/schema.sql
   ```
   Ou interactivement:
   ```bash
   mysql -u root -p
   ```
   Puis copier-coller le contenu de `sql/schema.sql`

3. **Installer les dépendances Python**
   ```bash
   pip install -r requirements.txt
   ```

4. **Configurer les variables d'environnement**
   ```bash
   export PANNEAU_DB_SERVER=localhost
   export PANNEAU_DB_USER=root
   export PANNEAU_DB_PASSWORD=votre_mot_de_passe
   ```

5. **Lancer l'application**
   ```bash
   python main.py
   ```

## Notes importantes

- MySQL est plus portable que MSSQL et ne nécessite pas ODBC Driver
- Les performances sont similaires pour une utilisation locale
- Les données de MSSQL peuvent être migrées avec des scripts dédiés si nécessaire
- Le code est maintenant compatible avec MySQL 5.7+
