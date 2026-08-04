from repository.panneau_repo import PanneauRepository

class PanneauService:
    @staticmethod
    def find_compatible_panneau(materielle):
        panneaux = PanneauRepository.get_all()
        required_power = materielle.watt * 2
        required_battery = materielle.watt * (1000 / 75)  # approximately 13.333
        
        compatible = [p for p in panneaux if p.puissance >= required_power and p.batterie >= required_battery]
        if compatible:
            # Return the one with smallest power
            return min(compatible, key=lambda p: p.puissance)
        return None