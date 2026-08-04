from materiel import Materiel
from config import Config
import importlib


def charger_materiels_et_configs_depuis_sqlserver(
    server="localhost\\SQLEXPRESS",
    database="PanneauxSolaireDB",

):
    """Charge Materiels et Configs depuis SQL Server.

    Retourne:
        tuple[list[Materiel], list[Config]]
    """
    pyodbc = importlib.import_module("pyodbc")

    connection_string = (
        "DRIVER={ODBC Driver 18 for SQL Server};"
        f"SERVER={server};"
        f"DATABASE={database};"
        "Encrypt=no;"
        "TrustServerCertificate=yes;"
    )

    with pyodbc.connect(connection_string, timeout=5) as conn:
        cursor = conn.cursor()

        cursor.execute("SELECT Nom, Puissance_W FROM dbo.Materiels ORDER BY Id")
        materiels = [Materiel(row[0], float(row[1])) for row in cursor.fetchall()]

        cursor.execute("SELECT HeureDebut, HeureFin, Puissance FROM dbo.Configs ORDER BY Id")
        configs = [Config(float(row[0]), float(row[1]), float(row[2])) for row in cursor.fetchall()]

    return materiels, configs
