import { Routes } from '@angular/router';
import { AccueilComponent } from '../components/accueil/accueil.component';

//RACE 
import { RaceComponent } from '../components/race/race.component';
import { VariationRaceComponent } from '../components/race/variationPoids.component';
import { PrixRaceComponent } from '../components/race/prix.component';

//LOT
import { LotComponent } from '../components/Lot/lot.component';
import { VisiteLotComponent } from '../components/Lot/visiteLot.component';
import { SituationLotComponent } from '../components/Lot/situationLot.component';
import { IncubationComponent } from '../components/Lot/incubation.component';

export const appRoutes: Routes = [
  //accueil
  { path: '', component: AccueilComponent },    
  
  //races
  { path: 'races', component: RaceComponent },   
  { path: 'races/variationPoids', component: VariationRaceComponent },
  { path: 'races/prix', component: PrixRaceComponent },

  //lots
  { path: 'lots', component: LotComponent },
  { path: 'lots/visiter', component: VisiteLotComponent },
  { path: 'lots/situation', component: SituationLotComponent },
  { path: 'lots/incubation', component: IncubationComponent },
];
