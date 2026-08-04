import { Component, OnInit } from '@angular/core';
import { CommonModule, } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-accueil',
  standalone: true,          
  imports: [CommonModule, RouterModule],   
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.css']
})
export class AccueilComponent implements OnInit {
  titre = 'Akoho';

  constructor() { }

  ngOnInit(): void {
   
  }
}