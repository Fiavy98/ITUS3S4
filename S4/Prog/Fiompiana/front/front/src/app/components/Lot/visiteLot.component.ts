import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VisiteLotService, VisiteLot } from '../../services/lot/visiteLot.service';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-visiteLot',
  standalone: true,
  imports: [CommonModule,RouterModule, FormsModule],
  templateUrl: './visiteLot.component.html',
  styleUrls: ['./visiteLot.component.css']
})
export class VisiteLotComponent implements OnInit {

  visites: VisiteLot[] = [];
  loading = true;
  saving = false;
  error = '';

  formModel: Partial<VisiteLot> = {};

  constructor(private visiteService: VisiteLotService) {}

  ngOnInit(): void {
  this.loading = !this.visiteService.hasCache();

  // charger les visites
  this.visiteService.getVisites().subscribe({
    next: data => { 
      this.visites = data; 
      this.loading = false; 
    },
    error: () => { 
      this.error = 'Erreur chargement'; 
      this.loading = false; 
    }
  });

  // charger les lots pour la liste déroulante
  fetch('http://localhost:3000/api/lot')
    .then(res => res.json())
    .then(data => {
      this.lots = data;
    })
    .catch(err => {
      console.error("Erreur chargement lots", err);
    });
}

  refresh() {
    this.loading = true;
    this.visiteService.refresh();
  }

  save() {
    if (this.saving) return;
    this.saving = true;
    this.visiteService.createVisite(this.formModel as VisiteLot).subscribe({
      next: () => { this.refresh(); this.formModel = {}; this.saving = false; },
      error: () => { this.error = 'Échec création'; this.saving = false; }
    });
  }


oeufLot: any = {
  lot_source_id: 0,
  nb_oeuf_utilise: 0,
  simba: 0,
  nom: '',
  date_entree: '',
  percent_lahy: 0,
  percent_vavy: 0
};

  message = '';
  errorOeuf = '';
  oeufLoading = false;

lots: any[] = [];

  createLotFromOeuf() {

  this.message = '';
  this.errorOeuf = '';
  this.oeufLoading = true;

  fetch('http://localhost:3000/api/lot/oeuf', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(this.oeufLot)
  })
  .then(res => res.json())
  .then(data => {
    // backend returns oeuf_restant_dans_lot_source
    const reste = data.oeuf_restant_dans_lot_source;
    this.message = `Nouveau lot créé${reste !== undefined ? ', œufs restants dans le lot source : ' + reste : ''}`;
    console.log(data);
    // clear form
    this.oeufLot = { lot_source_id: 0, nb_oeuf_utilise: 0, nom: '', date_entree: '' };
  })
  .catch(err => {
    this.errorOeuf = "Erreur création lot";
    console.error(err);
  })
  .finally(() => {
    this.oeufLoading = false;
  });

}

}


