from config.db import get_db_connection
from models.panneau import Panneau

class PanneauRepository:
    @staticmethod
    def get_all():
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM pannSolaire")
            results = cursor.fetchall()
        conn.close()
        return [Panneau(**row) for row in results]