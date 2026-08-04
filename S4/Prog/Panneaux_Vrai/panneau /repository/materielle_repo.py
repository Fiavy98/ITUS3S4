from config.database import get_connection
from repository.tranche_repo import get_tranche_id

def save_materielle(m):
    id_tranche = get_tranche_id(m.tranche)
    if not id_tranche:
        raise ValueError("Tranche not found")
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("INSERT INTO materielle (nom, heure, idTranche, watt) VALUES (%s, %s, %s, %s)",
                   (m.nom, m.heure, id_tranche, m.watt))
    conn.commit()
    id_materielle = cursor.lastrowid
    cursor.close()
    conn.close()
    return id_materielle
