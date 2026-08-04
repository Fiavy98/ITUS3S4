from config.db import get_db_connection
from models.materielle import Materielle

class MaterielleRepository:
    @staticmethod
    def insert(materielle):
        conn = get_db_connection()
        with conn.cursor() as cursor:
            sql = "INSERT INTO materielle (nom, heure, idTranche, watt) VALUES (%s, %s, %s, %s)"
            cursor.execute(sql, (materielle.nom, materielle.heure, materielle.idTranche, materielle.watt))
            conn.commit()
            materielle.id = cursor.lastrowid
        conn.close()
        return materielle

    @staticmethod
    def get_all():
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM materielle")
            results = cursor.fetchall()
        conn.close()
        return [Materielle(**row) for row in results]