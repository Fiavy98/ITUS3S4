# pip install pyodbc
#def get_connection():
#    # ⚠️ Remplacer par vos vraies infos MSSQL
#    import pyodbc
#    return pyodbc.connect(
#        "DRIVER={ODBC Driver 18 for SQL Server};"
#        "SERVER=localhost;"
#        "DATABASE=solar_db;"
#        "UID=sa;"
#        "PWD=StrongPass123!"
#    )

# pip install mysql-connector-python
def get_connection():
    import mysql.connector
    return mysql.connector.connect(
        host="localhost",
        user="root",
        password="",
        database="solar_db"
)
