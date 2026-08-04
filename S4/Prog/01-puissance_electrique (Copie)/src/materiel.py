class Materiel:
    """Représente un appareil électrique."""
    def __init__(self, nom, puissance):
        self.nom = nom
        self.puissance = puissance

class PeriodeConsommation:
    """Représente une période de consommation pour un matériel."""
    def __init__(self, materiel, heure_debut, heure_fin):
        self.materiel = materiel
        self.heure_debut = heure_debut
        self.heure_fin = heure_fin

    def __repr__(self):
        return (f"PeriodeConsommation(materiel={self.materiel.nom}, "
                f"debut={self.heure_debut}h, fin={self.heure_fin}h)")

def repartir_utilisation(utilisation, configs):
    """
    Répartit une période d'utilisation sur les différentes plages horaires.
    """
    periodes_reparties = []
    
    if utilisation.heure_fin < utilisation.heure_debut:
        partie1 = PeriodeConsommation(utilisation.materiel, utilisation.heure_debut, 24)
        periodes_reparties.extend(repartir_utilisation(partie1, configs))
        
        partie2 = PeriodeConsommation(utilisation.materiel, 0, utilisation.heure_fin)
        periodes_reparties.extend(repartir_utilisation(partie2, configs))
        return periodes_reparties

    for config in configs:
        debut_plage = config.heureC
        fin_plage = config.heureF
        if fin_plage < debut_plage:
            debut_inter = max(utilisation.heure_debut, debut_plage)
            fin_inter = min(utilisation.heure_fin, 24)
            if debut_inter < fin_inter:
                periodes_reparties.append(PeriodeConsommation(utilisation.materiel, debut_inter, fin_inter))
            
            debut_inter = max(utilisation.heure_debut, 0)
            fin_inter = min(utilisation.heure_fin, fin_plage)
            if debut_inter < fin_inter:
                periodes_reparties.append(PeriodeConsommation(utilisation.materiel, debut_inter, fin_inter))
        else:
            debut_inter = max(utilisation.heure_debut, debut_plage)
            fin_inter = min(utilisation.heure_fin, fin_plage)
            if debut_inter < fin_inter:
                periodes_reparties.append(PeriodeConsommation(utilisation.materiel, debut_inter, fin_inter))
    
    return periodes_reparties

def repartir_par_config(utilisations, configs):
    """
    Répartit une liste d'utilisations et les groupe par configuration.
    
    Returns => retourne un tableau à 2 dimensions de la liste des periodes consommations pour chaque config
    """
    periodes_par_config = [[] for _ in configs]
    
    for utilisation in utilisations:
        periodes_reparties = repartir_utilisation(utilisation, configs)
        
        for periode in periodes_reparties:
            for i, config in enumerate(configs):
                dans_plage = False
                if config.heureC < config.heureF:
                    if config.heureC <= periode.heure_debut < config.heureF:
                        dans_plage = True
                else:
                    if config.heureC <= periode.heure_debut < 24 or 0 <= periode.heure_debut < config.heureF:
                        dans_plage = True
                
                if dans_plage:
                    periodes_par_config[i].append(periode)
                    break 
                   
    return periodes_par_config

def calculer_puissance_maximale(periodes_reparties, config):
    """
    Calcule la puissance maximale atteinte à n'importe quel moment
    au sein d'une plage horaire de configuration donnée.
    """
    points = set()
    for p in periodes_reparties:
        points.add(p.heure_debut)
        points.add(p.heure_fin)

    sorted_points = sorted(list(points))

    max_puissance = 0

    for i in range(len(sorted_points) - 1):
        debut_intervalle = sorted_points[i]
        fin_intervalle = sorted_points[i+1]
        
        if debut_intervalle == fin_intervalle:
            continue

        milieu_intervalle = (debut_intervalle + fin_intervalle) / 2
        dans_plage_config = False
        if config.heureC < config.heureF:
            if config.heureC <= milieu_intervalle < config.heureF:
                dans_plage_config = True
        else: 
            if config.heureC <= milieu_intervalle < 24 or 0 <= milieu_intervalle < config.heureF:
                dans_plage_config = True

        if dans_plage_config:
            puissance_intervalle = 0
            
            for p in periodes_reparties:
                if p.heure_debut <= milieu_intervalle < p.heure_fin:
                    puissance_intervalle += p.materiel.puissance
            
            if puissance_intervalle > max_puissance:
                max_puissance = puissance_intervalle

    return max_puissance

def calculer_necessaire_batterie(periode_consommation_nuit,contrainte):
    """Calculer la puissance totale consommmée afin de déterminer la capacité de la batterie à utiliser

    Returns:
        _double => la capacité de la batterie + le 50% de marge
    """
    total = 0
    for periode in periode_consommation_nuit:
        puissance = periode.materiel.puissance
        temps = 0 
        if periode.heure_fin < periode.heure_debut : 
            temps = 24 - periode.heure_debut + periode.heure_fin
        else:
            temps = periode.heure_fin - periode.heure_debut
        total += puissance * temps
    
    return total*(1+contrainte/100)

def calculer_necessarie_panneau_solaire(batterie,puissanceMaxJournee,puissanceMaxCrepuscule,contrainte):
    """Calculer la puissancce necessaire du panneau solaire afin de subvenir aux besoins des materiaux

        Args:
            Les 3/3 de la batterie(wh)
            Puissance max atteinte en journée
            Puissance max atteinte au crépuscule
            contrainte des 2 config(1 => 40% (chercher 100), 2 => 20% (chercher 100))
        Returns:
            max((puissanceMaxJournee + batterie/13)/contrainte[0],(puissanceMaxCrepuscule+ batterie/13)/contrainte[1])
    """    
    maxPuissance = max((puissanceMaxJournee + batterie/13)/contrainte[0],(puissanceMaxCrepuscule + batterie/13)/contrainte[1])
    print(f"Mat {(puissanceMaxJournee + batterie/13)/contrainte[0]}")
    print(f"Crep {(puissanceMaxCrepuscule+ batterie/13)/contrainte[1]}")
    return maxPuissance