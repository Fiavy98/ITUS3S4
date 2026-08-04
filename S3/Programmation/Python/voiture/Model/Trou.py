class Trou:
    def __init__(self, id=None, route=None, position_km=None, profondeur=None, largeur=None, etat=None):
        self._id = id
        self._route = route          # objet Route
        self._position_km = position_km
        self._profondeur = profondeur
        self._largeur = largeur
        self._etat = etat

    # id
    @property
    def id(self):
        return self._id

    @id.setter
    def id(self, value):
        self._id = value

    # route
    @property
    def route(self):
        return self._route

    @route.setter
    def route(self, value):
        self._route = value

    # position_km
    @property
    def position_km(self):
        return self._position_km

    @position_km.setter
    def position_km(self, value):
        self._position_km = value

    # profondeur
    @property
    def profondeur(self):
        return self._profondeur

    @profondeur.setter
    def profondeur(self, value):
        self._profondeur = value

    # largeur
    @property
    def largeur(self):
        return self._largeur

    @largeur.setter
    def largeur(self, value):
        self._largeur = value

    # etat
    @property
    def etat(self):
        return self._etat

    @etat.setter
    def etat(self, value):
        self._etat = value

    def __str__(self):
        return (f"Trou(id={self.id}, route={self.route}, position_km={self.position_km}, "
                f"profondeur={self.profondeur}, largeur={self.largeur}, etat='{self.etat}')")
