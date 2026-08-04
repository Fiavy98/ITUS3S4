
from services.calcul_service import *

def trouver_panneau(materiel, panneaux):
    resultat = []

    e = energie(materiel.watt, materiel.heure)

    for p in panneaux:
        pr = puissance_reelle(p.puissance)
        br = batterie_reelle(p.batterie)

        if pr >= materiel.watt and br >= e:
            resultat.append(p)

    resultat.sort(key=lambda x: x.puissance)
    return resultat[0] if resultat else None
