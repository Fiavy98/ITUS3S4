class Voiture : 
    def __init__ (id,self,type,vMin,vMax,long,larg) : 
        self.id=id
        self.type=type
        self.vMax=vMax
        self.vMin=vMin
        self.longueur=long
        self.largeur=larg

#=================get======================
    @property
    def id(self) : 
        return self.id
    
    @property
    def type(self):
        return self.type

    @property
    def vMin(self):
        return self.vMin

    @property
    def vMax(self):
        return self.vMax
    
    @property
    def longueur(self):
        return self.longueur
    
    @property
    def largeur(self):
        return self.largeur

    #=================set======================

    @type.setter
    def type(self, type):
        self.type = type
    
    @vMin.setter
    def vMin(self, vMin):
        self.vMin = vMin
    
    @vMax.setter
    def vMax(self, vMax):
        self.vMax = vMax
    
    @long.setter
    def longueur(self, long):
        self.longueur = long
    
    @larg.setter
    def largeur(self, larg):
        self.largeur = larg


        
        