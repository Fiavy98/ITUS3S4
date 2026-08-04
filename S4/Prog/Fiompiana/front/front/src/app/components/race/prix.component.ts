import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PrixService, PrixRace } from '../../services/race/prix.service';

@Component({
  selector: 'app-prix',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './prix.component.html',
  styleUrls: ['./prix.component.css']
})
export class PrixRaceComponent implements OnInit {

  prix: PrixRace[] = [];
  loading = true;
  saving = false;
  error = '';

  formModel: Partial<PrixRace> = {};

  constructor(private prixService: PrixService) {}

  ngOnInit(): void {
    this.loading = !this.prixService.hasCache();
    this.prixService.getPrix().subscribe({
      next: data => { this.prix = data; this.loading = false; },
      error: () => { this.error = 'Erreur chargement prix'; this.loading = false; }
    });
  }

  refresh() {
    this.loading = true;
    this.prixService.refresh();
  }

  save() {
    if (this.saving) return;
    this.saving = true;
    this.prixService.createPrix(this.formModel as PrixRace).subscribe({
      next: () => { this.refresh(); this.formModel = {}; this.saving = false; },
      error: () => { this.error = 'Échec création'; this.saving = false; }
    });
  }
}