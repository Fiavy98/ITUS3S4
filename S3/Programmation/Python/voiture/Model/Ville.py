class Ville :
    def __init__(self,id,nom):
        self._id=id
        self._nom=nom
     
    @property
    def id(self):
        return self._id
    
    @id.setter
    def id(self,id):
        self._id=id
    
    @property
    def nom(self):
        return self._nom
    
    @id.setter
    def nom(self,nom):
        self._nom=nom
     
    def __str__(self):
        return f"ville (id={self.id}, nom={self.nom})"