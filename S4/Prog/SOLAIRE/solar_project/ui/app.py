import tkinter as tk
from tkinter import ttk, messagebox
import sys
from repository.tranche_repo import TrancheRepository
from repository.materielle_repo import MaterielleRepository
from repository.destination_repo import DestinationRepository
from repository.panneau_repo import PanneauRepository
from services.calcul_service import CalculService
from models.materielle import Materielle
from config.db import initialize_database

class SolarApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Solar Panel Project")
        self.root.geometry("600x400")
        
        try:
            self.tranches = TrancheRepository.get_all()
            self.tranche_dict = {t.nom: t.id for t in self.tranches}
        except ConnectionError as e:
            messagebox.showerror("Database Error", f"Cannot connect to database:\n{e}\n\nPlease ensure MySQL is running.")
            self.root.quit()
            return
        
        self.notebook = ttk.Notebook(root)
        self.notebook.pack(fill='both', expand=True)
        
        self.create_form_tab()
        self.create_results_tab()
    
    def create_form_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text="Ajouter Matériel")
        
        ttk.Label(frame, text="Nom:").grid(row=0, column=0, padx=10, pady=5)
        self.nom_entry = ttk.Entry(frame)
        self.nom_entry.grid(row=0, column=1, padx=10, pady=5)
        
        ttk.Label(frame, text="Heure d'utilisation:").grid(row=1, column=0, padx=10, pady=5)
        self.heure_entry = ttk.Entry(frame)
        self.heure_entry.grid(row=1, column=1, padx=10, pady=5)
        
        ttk.Label(frame, text="Tranche:").grid(row=2, column=0, padx=10, pady=5)
        self.tranche_combo = ttk.Combobox(frame, values=list(self.tranche_dict.keys()))
        self.tranche_combo.grid(row=2, column=1, padx=10, pady=5)
        
        ttk.Label(frame, text="Watt:").grid(row=3, column=0, padx=10, pady=5)
        self.watt_entry = ttk.Entry(frame)
        self.watt_entry.grid(row=3, column=1, padx=10, pady=5)
        
        ttk.Button(frame, text="Valider", command=self.add_materielle).grid(row=4, column=0, columnspan=2, pady=10)
    
    def create_results_tab(self):
        frame = ttk.Frame(self.notebook)
        self.notebook.add(frame, text="Destinations")
        
        self.results_text = tk.Text(frame, wrap='word')
        self.results_text.pack(fill='both', expand=True)
        
        ttk.Button(frame, text="Actualiser", command=self.load_results).pack(pady=10)
        
        self.load_results()
    
    def add_materielle(self):
        try:
            nom = self.nom_entry.get()
            heure = float(self.heure_entry.get())
            tranche_nom = self.tranche_combo.get()
            watt = float(self.watt_entry.get())
            
            if tranche_nom not in self.tranche_dict:
                messagebox.showerror("Erreur", "Tranche invalide")
                return
            
            idTranche = self.tranche_dict[tranche_nom]
            
            materielle = Materielle(nom=nom, heure=heure, idTranche=idTranche, watt=watt)
            materielle = MaterielleRepository.insert(materielle)
            
            panneau = CalculService.calculer_et_destiner(materielle)
            if panneau:
                messagebox.showinfo("Succès", f"Matériel ajouté et panel assigné: {panneau.nom}")
            else:
                messagebox.showwarning("Avertissement", "Matériel ajouté mais aucun panel compatible trouvé")
            
            # Clear fields
            self.nom_entry.delete(0, tk.END)
            self.heure_entry.delete(0, tk.END)
            self.watt_entry.delete(0, tk.END)
            self.tranche_combo.set('')
            
            self.load_results()
            
        except ValueError:
            messagebox.showerror("Erreur", "Veuillez entrer des valeurs valides")
    
    def load_results(self):
        destinations = DestinationRepository.get_all()
        materielle_dict = {m.id: m for m in MaterielleRepository.get_all()}
        panneau_dict = {p.id: p for p in PanneauRepository.get_all()}
        
        self.results_text.delete(1.0, tk.END)
        for dest in destinations:
            mat = materielle_dict.get(dest.idMaterielle)
            pan = panneau_dict.get(dest.idPanSolaire)
            if mat and pan:
                self.results_text.insert(tk.END, f"Matériel: {mat.nom}\nPanel: {pan.nom} ({pan.puissance} W, {pan.batterie} Wh)\n\n")

if __name__ == "__main__":
    root = tk.Tk()
    app = SolarApp(root)
    root.mainloop()