class Voiture: 
    def __init__(self,type) : 
        self.type=type
        self.vitesse_max=0
        self.longueur=0
        self.largeur=0

    def aff(self):
        print(f"type : {self.type}\n VM : {self.vitesse_max}")