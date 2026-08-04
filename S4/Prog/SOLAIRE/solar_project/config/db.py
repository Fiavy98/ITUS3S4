import pymysql
import os
from pathlib import Path

# Database configuration
DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': '',
    'database': 'solar_db',
    'cursorclass': pymysql.cursors.DictCursor
}

def get_db_connection():
    try:
        return pymysql.connect(**DB_CONFIG)
    except pymysql.err.OperationalError as e:
        raise ConnectionError(f"Cannot connect to MySQL database. Please ensure MySQL is running and configured correctly.\nError: {e}")

def initialize_database():
    """Initialize the database with tables and sample data"""
    # Read SQL file
    sql_file = Path(__file__).parent.parent / 'database.sql'
    if not sql_file.exists():
        raise FileNotFoundError(f"database.sql not found at {sql_file}")
    
    with open(sql_file, 'r') as f:
        sql_commands = f.read()
    
    # Connect to MySQL (without selecting database first)
    try:
        conn = pymysql.connect(
            host=DB_CONFIG['host'],
            user=DB_CONFIG['user'],
            password=DB_CONFIG['password']
        )
    except pymysql.err.OperationalError:
        return False
    
    try:
        with conn.cursor() as cursor:
            # Execute each SQL command
            for command in sql_commands.split(';'):
                if command.strip():
                    cursor.execute(command)
            conn.commit()
        return True
    except Exception as e:
        print(f"Error initializing database: {e}")
        return False
    finally:
        conn.close()