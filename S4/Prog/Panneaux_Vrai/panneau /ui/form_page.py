
import tkinter as tk
from models.materielle import Materielle
from repository.panneau_repo import get_panneaux
from services.panneau_service import trouver_panneau
from repository.destination_repo import save_destination

def start_app():
    root = tk.Tk()
    root.title("Projet Panneau Solaire")

    def submit():
        m = Materielle(
            entry_nom.get(),
            float(entry_heure.get()),
            entry_tranche.get(),
            float(entry_watt.get())
        )

        panneau = trouver_panneau(m, get_panneaux())

        if panneau:
            save_destination((m.nom, panneau.nom))
            result_label.config(text=f"OK: {panneau.nom}")
        else:
            result_label.config(text="Aucun panneau compatible")

    tk.Label(root, text="Nom").pack()
    entry_nom = tk.Entry(root)
    entry_nom.pack()

    tk.Label(root, text="Heure").pack()
    entry_heure = tk.Entry(root)
    entry_heure.pack()

    tk.Label(root, text="Tranche").pack()
    entry_tranche = tk.Entry(root)
    entry_tranche.pack()

    tk.Label(root, text="Watt").pack()
    entry_watt = tk.Entry(root)
    entry_watt.pack()

    tk.Button(root, text="Valider", command=submit).pack()

    result_label = tk.Label(root, text="")
    result_label.pack()

    root.mainloop()
