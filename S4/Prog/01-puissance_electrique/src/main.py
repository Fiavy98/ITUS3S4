import tkinter as tk
from tkinter import ttk, messagebox

from materiel import (
    Materiel,
    PeriodeConsommation,
    repartir_par_config,
    calculer_puissance_maximale,
    calculer_necessaire_batterie,
    calculer_necessarie_panneau_solaire,
)
from config import Config
from db_sqlserver import charger_materiels_et_configs_depuis_sqlserver


class ApplicationPuissance:
    def __init__(self, root):
        self.root = root
        self.root.title("Dimensionnement électrique")
        self.root.geometry("860x560")
        self._configurer_styles()

        self.materiels, self.configs = self._charger_depuis_db_ou_defaut()
        self.contrainte = self._calculer_contrainte(self.configs)

        self.utilisations = []

        self._build_ui()

    def _charger_depuis_db_ou_defaut(self):
        materiels_defaut = [
            Materiel("tv", 1),
            Materiel("pc", 50),
            Materiel("clim", 1500),
            Materiel("frigo", 80),
            Materiel("four", 2000),
            Materiel("fer a repasser", 1500),
        ]
        configs_defaut = [
            Config(6, 17, 0.4),
            Config(17, 19, 0.2),
            Config(19, 6, 0),
        ]
        try:
            materiels_db, configs_db = charger_materiels_et_configs_depuis_sqlserver(
                host="localhost",
                port=3306,
                database="PuissanceElectriqueDB",
                user="root",
                password="",
            )

            if not materiels_db or len(configs_db) < 3:
                raise ValueError("Données insuffisantes en base")

            return materiels_db, configs_db
        except Exception:
            return materiels_defaut, configs_defaut

    def _calculer_contrainte(self, configs):
        return [
            configs[0].puissance,
            configs[1].puissance,
            50,
        ]
    
    def _configurer_styles(self):
        self.root.configure(bg="#f4f6f8")
        style = ttk.Style(self.root)
        style.theme_use("clam")

        style.configure("TFrame", background="#f4f6f8")
        style.configure("Card.TFrame", background="#ffffff", relief="flat")
        style.configure("TLabel", background="#f4f6f8", font=("Segoe UI", 10))
        style.configure("Header.TLabel", background="#f4f6f8", font=("Segoe UI", 18, "bold"), foreground="#6a7e8c")
        style.configure("Section.TLabel", background="#ffffff", font=("Segoe UI", 11, "bold"), foreground="#333333")
        style.configure("Result.TLabel", background="#ffffff", font=("Segoe UI", 12, "bold"), foreground="#6a7e8c")
        style.configure("Accent.TButton", font=("Segoe UI", 10, "bold"), foreground="#ffffff", background="#6a7e8c")
        style.map(
            "Accent.TButton",
            background=[("active", "#6a7e8c"), ("disabled", "#a9cce3")],
        )
        style.configure("Secondary.TButton", font=("Segoe UI", 10), foreground="#333333", background="#d6eaf8")
        style.map(
            "Secondary.TButton",
            background=[("active", "#aed6f1"), ("disabled", "#ebf5fb")],
        )
        style.configure("Treeview", rowheight=28, font=("Segoe UI", 10), background="#ffffff", fieldbackground="#ffffff")
        style.configure("Treeview.Heading", font=("Segoe UI", 10, "bold"), background="#e1e8f0")
        style.configure("TLabelframe", background="#f4f6f8")
        style.configure("TLabelframe.Label", font=("Segoe UI", 12, "bold"), foreground="#6a7e8c")

    def _build_ui(self):
        container = ttk.Frame(self.root, padding=16)
        container.pack(fill="both", expand=True)

        header_frame = ttk.Frame(container, style="Card.TFrame", padding=(16, 16, 16, 12))
        header_frame.pack(fill="x", pady=(0, 14))

        title = ttk.Label(header_frame, text="Dimensionnement électrique", style="Header.TLabel")
        title.pack(anchor="w")

        subtitle = ttk.Label(header_frame, text="Planifiez vos consommations et optimisez panneaux et batteries.")
        subtitle.pack(anchor="w", pady=(8, 0))

        form_card = ttk.Frame(container, style="Card.TFrame", padding=16)
        form_card.pack(fill="x", pady=(0, 14))

        ttk.Label(form_card, text="Matériel", style="Section.TLabel").grid(row=0, column=0, sticky="w", padx=(0, 10), pady=(0, 8))
        ttk.Label(form_card, text="Heure début (HH:MM)", style="Section.TLabel").grid(row=0, column=1, sticky="w", padx=(0, 10), pady=(0, 8))
        ttk.Label(form_card, text="Heure fin (HH:MM)", style="Section.TLabel").grid(row=0, column=2, sticky="w", padx=(0, 10), pady=(0, 8))

        self.selected_materiel = tk.StringVar(value=self.materiels[0].nom)
        self.dropdown = ttk.Combobox(
            form_card,
            textvariable=self.selected_materiel,
            values=[m.nom for m in self.materiels],
            state="readonly",
            width=28,
        )
        self.dropdown.grid(row=1, column=0, sticky="w", padx=(0, 10), pady=(0, 8))

        self.entry_debut = ttk.Entry(form_card, width=16)
        self.entry_debut.grid(row=1, column=1, sticky="w", padx=(0, 10), pady=(0, 8))
        self.entry_debut.insert(0, "08:00")

        self.entry_fin = ttk.Entry(form_card, width=16)
        self.entry_fin.grid(row=1, column=2, sticky="w", padx=(0, 10), pady=(0, 8))
        self.entry_fin.insert(0, "10:00")

        btn_add = ttk.Button(form_card, text="Ajouter", command=self.ajouter_utilisation, style="Accent.TButton")
        btn_add.grid(row=1, column=3, sticky="w", padx=(0, 10), pady=(0, 8))

        btn_calc = ttk.Button(form_card, text="Calculer panneau/batterie", command=self.calculer_resultats, style="Secondary.TButton")
        btn_calc.grid(row=1, column=4, sticky="w", pady=(0, 8))

        content_frame = ttk.Frame(container)
        content_frame.pack(fill="both", expand=True)

        table_frame = ttk.LabelFrame(content_frame, text="Utilisations ajoutées", padding=12)
        table_frame.pack(side="left", fill="both", expand=True, padx=(0, 12))

        self.table = ttk.Treeview(
            table_frame,
            columns=("materiel", "puissance", "debut", "fin"),
            show="headings",
            height=12,
        )
        self.table.heading("materiel", text="Matériel")
        self.table.heading("puissance", text="Puissance (W)")
        self.table.heading("debut", text="Début (h)")
        self.table.heading("fin", text="Fin (h)")

        self.table.column("materiel", width=260)
        self.table.column("puissance", width=120, anchor="center")
        self.table.column("debut", width=100, anchor="center")
        self.table.column("fin", width=100, anchor="center")

        scrollbar = ttk.Scrollbar(table_frame, orient="vertical", command=self.table.yview)
        self.table.configure(yscrollcommand=scrollbar.set)

        self.table.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")

        actions_frame = ttk.Frame(content_frame, style="Card.TFrame", padding=12)
        actions_frame.pack(side="right", fill="y")

        btn_supprimer = ttk.Button(actions_frame, text="Supprimer la sélection", command=self.supprimer_selection, style="Accent.TButton")
        btn_supprimer.pack(fill="x", pady=(0, 10))

        btn_tout_supprimer = ttk.Button(actions_frame, text="Tout supprimer", command=self.tout_supprimer, style="Secondary.TButton")
        btn_tout_supprimer.pack(fill="x")

        result_frame = ttk.LabelFrame(actions_frame, text="Résultats", padding=14)
        result_frame.pack(fill="x", pady=(18, 0))

        self.lbl_panneau = ttk.Label(result_frame, text="Panneau nécessaire : -", style="Result.TLabel")
        self.lbl_panneau.pack(anchor="w", pady=(0, 4))

        self.lbl_batterie = ttk.Label(result_frame, text="Batterie nécessaire : -", style="Result.TLabel")
        self.lbl_batterie.pack(anchor="w")

    def _materiel_par_nom(self, nom):
        for materiel in self.materiels:
            if materiel.nom == nom:
                return materiel
        return None

    def _refresh_table(self):
        for row in self.table.get_children():
            self.table.delete(row)

        for index, utilisation in enumerate(self.utilisations):
            self.table.insert(
                "",
                "end",
                iid=str(index),
                values=(
                    utilisation.materiel.nom,
                    utilisation.materiel.puissance,
                    self._format_heure(utilisation.heure_debut),
                    self._format_heure(utilisation.heure_fin),
                ),
            )

    def _parse_heure(self, valeur):
        valeur = valeur.strip()

        if ":" in valeur:
            morceaux = valeur.split(":")
            if len(morceaux) != 2:
                raise ValueError("Format heure invalide")

            heures_str, minutes_str = morceaux
            if not heures_str.isdigit() or not minutes_str.isdigit():
                raise ValueError("Format heure invalide")

            heures = int(heures_str)
            minutes = int(minutes_str)

            if not (0 <= heures <= 24 and 0 <= minutes < 60):
                raise ValueError("Heure hors plage")
            if heures == 24 and minutes != 0:
                raise ValueError("24:00 maximum")

            return heures + minutes / 60

        # compatibilité avec l'ancien format décimal (ex: 17.5)
        return float(valeur)

    def _format_heure(self, heure_decimal):
        total_minutes = int(round(heure_decimal * 60))
        total_minutes %= 24 * 60
        heures = total_minutes // 60
        minutes = total_minutes % 60
        return f"{heures:02d}:{minutes:02d}"

    def ajouter_utilisation(self):
        nom_materiel = self.selected_materiel.get()
        materiel = self._materiel_par_nom(nom_materiel)

        if materiel is None:
            messagebox.showerror("Erreur", "Matériel invalide.")
            return

        try:
            heure_debut = self._parse_heure(self.entry_debut.get())
            heure_fin = self._parse_heure(self.entry_fin.get())
        except ValueError:
            messagebox.showerror("Erreur", "Format heure invalide. Utilise HH:MM (ex: 17:30) ou décimal (ex: 17.5).")
            return

        if not (0 <= heure_debut <= 24 and 0 <= heure_fin <= 24):
            messagebox.showerror("Erreur", "Les heures doivent être entre 0 et 24.")
            return

        if heure_debut == heure_fin:
            messagebox.showerror("Erreur", "L'heure de début et de fin ne peuvent pas être identiques.")
            return

        self.utilisations.append(PeriodeConsommation(materiel, heure_debut, heure_fin))
        self._refresh_table()

    def supprimer_selection(self):
        selection = self.table.selection()
        if not selection:
            messagebox.showwarning("Attention", "Sélectionne au moins une ligne à supprimer.")
            return

        indexes = sorted((int(item_id) for item_id in selection), reverse=True)
        for index in indexes:
            if 0 <= index < len(self.utilisations):
                del self.utilisations[index]        
    def tout_supprimer(self):
        if not self.utilisations:
            return

        confirmer = messagebox.askyesno("Confirmation", "Supprimer toutes les utilisations ?")
        if not confirmer:
            return

        self.utilisations.clear()
        self._refresh_table()
        self.lbl_panneau.config(text="Panneau nécessaire : -")
        self.lbl_batterie.config(text="Batterie nécessaire : -")

    def calculer_resultats(self):
        if not self.utilisations:
            messagebox.showwarning("Attention", "Ajoutez au moins une utilisation.")
            return

        periodes_par_config = repartir_par_config(self.utilisations, self.configs)

        puissance_max_journee = calculer_puissance_maximale(periodes_par_config[0], self.configs[0])
        puissance_max_crepuscule = calculer_puissance_maximale(periodes_par_config[1], self.configs[1])

        batterie = calculer_necessaire_batterie(periodes_par_config[2], self.contrainte[2])
        panneau = calculer_necessarie_panneau_solaire(
            batterie,
            puissance_max_journee,
            puissance_max_crepuscule,
            self.contrainte,
        )

        self.lbl_panneau.config(text=f"Panneau nécessaire : {panneau:.2f} W")
        self.lbl_batterie.config(text=f"Batterie nécessaire : {batterie:.2f} Wh")


if __name__ == "__main__":
    root = tk.Tk()
    app = ApplicationPuissance(root)
    root.mainloop()

