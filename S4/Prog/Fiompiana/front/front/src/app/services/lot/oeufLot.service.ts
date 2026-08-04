import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OeufLot {
  lot_source_id: number;
  nb_oeuf_utilise: number;
  simba?: number;
  nom: string;
  date_entree: string;
  race_id: number;
}

@Injectable({
  providedIn: 'root'
})
export class OeufLotService {

  private apiUrl = 'http://localhost:3000/api/lot/oeuf';

  constructor(private http: HttpClient) {}

  createLotFromOeuf(data: OeufLot): Observable<any> {
    return this.http.post(this.apiUrl, data);
  }

}