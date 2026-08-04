class RouteVille:
    def __init__(self, id=None, route=None, ville=None, position_km=None):
        self._id = id
        self._route = route      # objet Route
        self._ville = ville      # objet Ville
        self._position_km = position_km

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

    # ville
    @property
    def ville(self):
        return self._ville

    @ville.setter
    def ville(self, value):
        self._ville = value

    # position_km
    @property
    def position_km(self):
        return self._position_km

    @position_km.setter
    def position_km(self, value):
        self._position_km = value

    def __str__(self):
        return (f"RouteVille(id={self.id}, route={self.route}, "
                f"ville={self.ville}, position_km={self.position_km})")
