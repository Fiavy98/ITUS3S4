from config.db import get_db_connection
from models.destination import Destination

class DestinationRepository:
    @staticmethod
    def insert(destination):
        conn = get_db_connection()
        with conn.cursor() as cursor:
            sql = "INSERT INTO destination (idMaterielle, idPanSolaire) VALUES (%s, %s)"
            cursor.execute(sql, (destination.idMaterielle, destination.idPanSolaire))
            conn.commit()
            destination.id = cursor.lastrowid
        conn.close()
        return destination

    @staticmethod
    def get_all():
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM destination")
            results = cursor.fetchall()
        conn.close()
        return [Destination(**row) for row in results]