import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { IncubationService, Incubation } from '../../services/lot/incubation.service';
import { LotService, Lot } from '../../services/lot/lot.service';

// Interface dédiée pour le formulaire d'éclosion
interface HatchModel {
  nb_oeufs_hatched: number;
  nb_oeufs_failed: number;
  pct_lahy: number;
  pct_vavy: number;
  nom: string;
  date_entree: string;
}

@Component({
  selector: 'app-incubation',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './incubation.component.html'
})
export class IncubationComponent implements OnInit {
  lots: Lot[] = [];
  incubations: Incubation[] = [];
  loading = false;
  error = '';

  createModel: Partial<Incubation> = {
    lot_source: 0,
    nb_oeufs: 0,
    date_debut: new Date().toISOString().substring(0, 10)
  };

  // Utilisation de HatchModel au lieu de Partial<Incubation>
  hatchModel: HatchModel = {
    nb_oeufs_hatched: 0,
    nb_oeufs_failed: 0,
    pct_lahy: 50,
    pct_vavy: 50,
    nom: '',
    date_entree: new Date().toISOString().substring(0, 10)
  };

  selectedIncubation?: Incubation;

  constructor(
    private incubationService: IncubationService,
    private lotService: LotService
  ) {}

  ngOnInit(): void {
    this.loadLots();
    this.loadIncubations();
  }

  loadLots() {
    this.lotService.getLots().subscribe({
      next: (data) => {
        this.lots = data;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  loadIncubations() {
    this.loading = true;
    this.error = '';
    this.incubationService.getIncubations().subscribe({
      next: data => {
        this.incubations = data;
        this.loading = false;
      },
      error: err => {
        console.error(err);
        this.error = 'Erreur chargement incubations';
        this.loading = false;
      }
    });
  }

  createIncubation() {
    this.error = '';
    this.incubationService.createIncubation(this.createModel).subscribe({
      next: () => {
        this.loadIncubations();
        this.createModel = {
          lot_source: 0,
          nb_oeufs: 0,
          date_debut: new Date().toISOString().substring(0, 10)
        };
      },
      error: err => {
        console.error(err);
        this.error = 'Erreur création incubation';
      }
    });
  }

  selectToHatch(inc: Incubation) {
    this.selectedIncubation = inc;
    this.hatchModel = {
      nb_oeufs_hatched: inc.nb_oeufs_hatched || 0,
      nb_oeufs_failed: inc.nb_oeufs_failed || 0,
      pct_lahy: inc.pct_lahy || 50,
      pct_vavy: inc.pct_vavy || 50,
      nom: '',
      date_entree: new Date().toISOString().substring(0, 10)
    };
  }

  hatch() {
    if (!this.selectedIncubation) return;
    this.error = '';
    this.incubationService.hatchIncubation(this.selectedIncubation.id, this.hatchModel)
      .subscribe({
        next: () => {
          this.loadIncubations();
          this.selectedIncubation = undefined;
        },
        error: err => {
          console.error(err);
          this.error = "Erreur lors de l'éclosion";
        }
      });
  }
}