class Route:
    def __init__(self, id=None, rn=None, ville_depart=None, ville_arriver=None, longueur_km=None):
        self._id = id
        self._rn = rn
        self._ville_depart = ville_depart  # objet Ville
        self._ville_arriver = ville_arriver  # objet Ville
        self._longueur_km = longueur_km

    # id
    @property
    def id(self):
        return self._id

    @id.setter
    def id(self, value):
        self._id = value

    # rn
    @property
    def rn(self):
        return self._rn

    @rn.setter
    def rn(self, value):
        self._rn = value

    # ville_depart
    @property
    def ville_depart(self):
        return self._ville_depart

    @ville_depart.setter
    def ville_depart(self, value):
        self._ville_depart = value

    # ville_arriver
    @property
    def ville_arriver(self):
        return self._ville_arriver

    @ville_arriver.setter
    def ville_arriver(self, value):
        self._ville_arriver = value

    # longueur_km
    @property
    def longueur_km(self):
        return self._longueur_km

    @longueur_km.setter
    def longueur_km(self, value):
        self._longueur_km = value

    def __str__(self):
        return (f"Route(id={self.id}, rn='{self.rn}', "
                f"depart={self.ville_depart}, arriver={self.ville_arriver}, "
                f"longueur_km={self.longueur_km})")
