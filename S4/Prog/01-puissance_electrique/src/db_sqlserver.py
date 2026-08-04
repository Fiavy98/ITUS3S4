from materiel import Materiel
from config import Config
import importlib


def charger_materiels_et_configs_depuis_sqlserver(
    host="localhost",
    port=3306,
    database="PuissanceElectriqueDB",
    user="root",
    password="",
):
    """Charge Materiels et Configs depuis une base MySQL.

    Retourne:
        tuple[list[Materiel], list[Config]]
    """
    try:
        mysql = importlib.import_module("mysql.connector")
        connect = mysql.connect
    except ModuleNotFoundError:
        pymysql = importlib.import_module("pymysql")
        connect = pymysql.connect

    connection_params = {
        "host": host,
        "port": port,
        "user": user,
        "password": password,
        "database": database,
    }

    with connect(**connection_params) as conn:
        cursor = conn.cursor()

        cursor.execute("SELECT Nom, Puissance_W FROM Materiels ORDER BY Id")
        materiels = [Materiel(row[0], float(row[1])) for row in cursor.fetchall()]

        cursor.execute("SELECT HeureDebut, HeureFin, Puissance FROM Configs ORDER BY Id")
        configs = [Config(float(row[0]), float(row[1]), float(row[2])) for row in cursor.fetchall()]

    return materiels, configs
