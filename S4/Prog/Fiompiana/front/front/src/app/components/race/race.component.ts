import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RaceService, Race } from '../../services/race/race.service';

@Component({
  selector: 'app-race',
  standalone: true,
  imports: [CommonModule,RouterModule, FormsModule],
  templateUrl: './race.component.html',
  styleUrls: ['./race.component.css']
})

export class RaceComponent implements OnInit {
  races: Race[] = [];
  loading = true;
  saving = false;
  error = '';

  formModel: Partial<Race> = {};

  constructor(private raceService: RaceService) {}

  ngOnInit(): void {
    this.loading = !this.raceService.hasCache();
    this.raceService.getRaces().subscribe({
      next: (data) => { this.races = data; this.loading = false; },
      error: () => { this.error = 'Impossible de charger les races.'; this.loading = false; }
    });
  }

  refresh() {
    this.loading = true;
    this.raceService.refresh();
  }

  save() {
    if (this.saving) return;
    this.saving = true;
    this.raceService.createRace(this.formModel as Race).subscribe({
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

  trackById(index: number, item: Race) {
    return item.id;
  }
}