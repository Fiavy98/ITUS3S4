from config.db import get_db_connection
from models.tranche import Tranche

class TrancheRepository:
    @staticmethod
    def get_all():
        conn = get_db_connection()
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM tranche")
            results = cursor.fetchall()
        conn.close()
        return [Tranche(**row) for row in results]