from config.database import get_connection
from models.panneau import Panneau

def get_panneaux():
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT nom, puissance, batterie FROM pannSolaire")
    panneaux = [Panneau(row[0], row[1], row[2]) for row in cursor.fetchall()]
    cursor.close()
    conn.close()
    return panneaux
