class Type:
    def __init__(self, id=None, nom=""):
        self._id = id
        self._nom = nom

    # Getter et Setter id
    @property
    def id(self):
        return self._id

    @id.setter
    def id(self, value):
        self._id = value

    # Getter et Setter nom
    @property
    def nom(self):
        return self._nom

    @nom.setter
    def nom(self, value):
        self._nom = value

    def __str__(self):
        return f"Type(id={self.id}, nom='{self.nom}')"
