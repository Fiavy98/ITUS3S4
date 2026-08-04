import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RaceService, Race } from '../../services/race/race.service';
import { VariationPoidsService, VariationPoids } from '../../services/race/variationPoids.service';

@Component({
  selector: 'app-variationPoids',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './variationPoids.component.html',
  styleUrls: ['./variationPoids.component.css']
})

export class VariationRaceComponent implements OnInit {

  variation: VariationPoids[] = [];
  loading = true;
  saving = false;
  error = '';

  formModel: Partial<VariationPoids> = {};

  races: Race[] = [];
  selectedRaceId?: number;
  debutWeek = 0;
  finWeek = 0;
  weightGainResult: { startWeight: number; endWeight: number; gain: number } | null = null;

  constructor(
    private variationService: VariationPoidsService,
    private raceService: RaceService
  ) {}

  ngOnInit(): void {
    this.loading = !this.variationService.hasCache();
    this.variationService.getVariationPoids().subscribe({
      next: data => { this.variation = data; this.loading = false; },
      error: () => { this.error = 'Erreur chargement'; this.loading = false; }
    });

    this.raceService.getRaces().subscribe({
      next: races => { this.races = races; },
      error: () => { /* ignore */ }
    });
  }

  refresh() {
    this.loading = true;
    this.variationService.refresh();
  }

  loadForRace(raceId?: number) {
    if (!raceId) {
      return;
    }

    this.loading = true;
    this.variationService.getVariationByRace(raceId).subscribe({
      next: data => { this.variation = data; this.loading = false; },
      error: () => { this.error = 'Erreur chargement'; this.loading = false; }
    });
  }

  calcWeightGain() {
    if (!this.selectedRaceId) {
      this.error = 'Veuillez sélectionner une race';
      return;
    }
    this.error = '';
    this.weightGainResult = null;
    this.variationService.getWeightGain(this.selectedRaceId, this.debutWeek, this.finWeek).subscribe({
      next: res => { this.weightGainResult = res; },
      error: () => { this.error = 'Impossible de calculer l’augmentation de poids'; }
    });
  }

  save() {
    if (this.saving) return;
    this.saving = true;
    this.variationService.createVariation(this.formModel as VariationPoids).subscribe({
      next: () => { this.refresh(); this.formModel = {}; this.saving = false; },
      error: () => { this.error = 'Échec création'; this.saving = false; }
    });
  }
}