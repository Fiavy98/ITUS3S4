from services.panneau_service import PanneauService
from repository.destination_repo import DestinationRepository
from models.destination import Destination

class CalculService:
    @staticmethod
    def calculer_et_destiner(materielle):
        panneau = PanneauService.find_compatible_panneau(materielle)
        if panneau:
            destination = Destination(idMaterielle=materielle.id, idPanSolaire=panneau.id)
            DestinationRepository.insert(destination)
            return panneau
        return None