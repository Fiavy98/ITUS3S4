from config.database import get_connection
from models.tranche import Tranche

def get_tranches():
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT nom, H_depart, H_arrive FROM tranche")
    tranches = [Tranche(row[0], row[1], row[2]) for row in cursor.fetchall()]
    cursor.close()
    conn.close()
    return tranches

def get_tranche_id(nom):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT id FROM tranche WHERE nom = %s", (nom,))
    row = cursor.fetchone()
    cursor.close()
    conn.close()
    return row[0] if row else None
