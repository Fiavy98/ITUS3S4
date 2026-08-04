
def energie(watt, heure):
    return watt * heure

def puissance_reelle(p):
    return p * 0.4

def batterie_reelle(b):
    return b * 1.5

def facteur_tranche(tranche):
    if tranche == "matin":
        return 1.0
    elif tranche == "avant soir":
        return 0.5
    return 0
