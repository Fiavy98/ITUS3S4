import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SituationService, SituationLot } from '../../services/lot/situation.service';

@Component({
  selector: 'app-situation-lot',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './situationLot.component.html'
})
export class SituationLotComponent implements OnInit {
  // default to today so the table is populated immediately
  dateChoisie: string = new Date().toISOString().substring(0, 10);
  situation: SituationLot[] = [];
  loading = false;
  error = '';

  constructor(private situationService: SituationService) {}

  ngOnInit(): void {
    // load data as soon as the component is created
    this.loadSituation();
  }

  loadSituation() {
    if (!this.dateChoisie) return;
    this.loading = true;
    this.error = '';
    this.situationService.getSituationLot(this.dateChoisie).subscribe({
      next: (data) => {
        this.situation = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Erreur chargement';
        this.loading = false;
      }
    });
  }
}