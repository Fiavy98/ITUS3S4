class VoitureRoute:
    def __init__(self,id,voiture,code_route):
        self._id = id
        self._voiture = voiture
        self._code_route = code_route

    # id
    @property
    def id(self):
        return self._id

    @id.setter
    def id(self, value):
        self._id = value

    # voiture
    @property
    def voiture(self):
        return self._voiture

    @voiture.setter
    def voiture(self, value):
        self._voiture = value

    # code_route
    @property
    def code_route(self):
        return self._code_route

    @code_route.setter
    def code_route(self, value):
        self._code_route = value

    def __str__(self):
        return f"VoitureRoute(id={self.id}, voiture={self.voiture}, code_route='{self.code_route}')"
