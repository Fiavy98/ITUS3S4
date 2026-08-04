import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LotService, Lot } from '../../services/lot/lot.service';
import { RaceService, Race } from '../../services/race/race.service';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-lot',
  standalone: true,
  imports: [CommonModule,RouterModule, FormsModule],
  templateUrl: './lot.component.html'
})
export class LotComponent implements OnInit {

  lots: Lot[] = [];
  races: Race[] = [];
  loading = true;
  saving = false;
  error = '';

  // form state
  formModel: Partial<Lot> = {};

  constructor(private lotService: LotService, private raceService: RaceService) {}

  ngOnInit(): void {
    // load lots
    this.loading = !this.lotService.hasCache();
    this.lotService.getLots().subscribe({
      next: data => {
        this.lots = data;
        this.loading = false;
      },
      error: err => {
        this.error = 'Erreur chargement lots';
        this.loading = false;
      }
    });
    // load races for dropdown
    this.raceService.getRaces().subscribe({
      next: r => this.races = r,
      error: () => {}
    });
  }

  refresh() {
    this.loading = true;
    this.lotService.refresh();
  }

  save() {
    if (this.saving) return;
    this.saving = true;
    // always create new lot
    this.lotService.createLot(this.formModel as Lot).subscribe({
      next: () => {
        this.refresh();
        this.formModel = {};
        this.saving = false;
      },
      error: err => {
        this.error = 'Échec création';
        this.saving = false;
      }
    });
  }

}