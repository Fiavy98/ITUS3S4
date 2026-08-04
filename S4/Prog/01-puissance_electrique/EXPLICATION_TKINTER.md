# Explication de l’affichage Tkinter (projet puissance électrique)

Ce document explique **tout ce qui concerne l’interface Tkinter** dans le fichier `src/main.py` :

- les widgets utilisés,
- les paramètres passés à chaque fonction Tkinter/ttk,
- le rôle de chaque méthode liée à l’affichage.

---

## 1) Imports Tkinter

```python
import tkinter as tk
from tkinter import ttk, messagebox
```

- `tkinter as tk` : module Tkinter de base (fenêtre, variables Tk, etc.).
- `ttk` : widgets “themed” (plus modernes visuellement) : `ttk.Frame`, `ttk.Label`, `ttk.Button`, `ttk.Treeview`, etc.
- `messagebox` : boîtes de dialogue (`showerror`, `showwarning`, `askyesno`).

---

## 2) Fenêtre principale

Dans le `main` :

```python
root = tk.Tk()
app = ApplicationPuissance(root)
root.mainloop()
```

### `tk.Tk()`

Crée la fenêtre racine.

### `root.mainloop()`

Lance la boucle événementielle (l’UI reste active et répond aux clics/clavier).

---

## 3) Configuration générale de la fenêtre

Dans `__init__` :

```python
self.root.title("Dimensionnement électrique")
self.root.geometry("860x560")
```

### `title(titre: str)`

- Paramètre : texte du titre de la fenêtre.

### `geometry(chaine: str)`

- Paramètre : format `"largeurxhauteur"`, ici `"860x560"`.

---

## 4) Styles ttk (tableau)

```python
style = ttk.Style(self.root)
style.configure("Treeview", rowheight=28, font=("Arial", 11))
style.configure("Treeview.Heading", font=("Arial", 11, "bold"))
```

### `ttk.Style(master=None)`

- `master` : racine/fenêtre associée.

### `style.configure(style_name, **options)`

- `style_name` : nom du style (`"Treeview"`, `"Treeview.Heading"`).
- `rowheight=28` : hauteur de chaque ligne du tableau.
- `font=("Arial", 11)` : police et taille.
- `font=("Arial", 11, "bold")` : en gras pour l’en-tête.

---

## 5) Structure de layout (containers)

### `ttk.Frame`

Exemple :

```python
container = ttk.Frame(self.root, padding=12)
container.pack(fill="both", expand=True)
```

Paramètres utilisés :

- `master` (positionnel) : parent du widget (`self.root`, `container`, etc.).
- `padding=12` : marge interne.

### `pack(...)`

Paramètres utilisés dans le projet :

- `fill="both"` : étendre horizontalement + verticalement.
- `fill="x"` : étendre horizontalement seulement.
- `expand=True` : le widget prend l’espace disponible.
- `side="left"|"right"` : position relative.
- `anchor="w"` : alignement à gauche (west).
- `pady=(a, b)` : marge verticale (haut, bas).
- `padx=(a, b)` : marge horizontale (gauche, droite).

### `grid(...)`

Utilisé dans le formulaire d’ajout.

Paramètres utilisés :

- `row`, `column` : position ligne/colonne.
- `sticky="w"` : coller à gauche dans la cellule.
- `padx`, `pady` : marges autour du widget.

---

## 6) Widgets de texte

### `ttk.Label`

Exemples :

```python
title = ttk.Label(container, text="Ajout d'utilisation", font=("Arial", 15, "bold"))
self.lbl_panneau = ttk.Label(result_frame, text="Panneau nécessaire : -")
```

Paramètres utilisés :

- `master` : parent.
- `text` : texte affiché.
- `font` : police `(famille, taille, style)`.

### Mise à jour dynamique du texte

```python
self.lbl_panneau.config(text=f"Panneau nécessaire : {panneau:.2f} W")
```

- `config(**options)` : modifie les options d’un widget existant.
- Ici : `text=...`.

---

## 7) Liste déroulante (dropdown)

```python
self.selected_materiel = tk.StringVar(value=self.materiels[0].nom)
self.dropdown = ttk.Combobox(
    form,
    textvariable=self.selected_materiel,
    values=[m.nom for m in self.materiels],
    state="readonly",
    width=25,
)
```

### `tk.StringVar(value=...)`

Variable Tk liée à un widget texte/liste.

Paramètre utilisé :

- `value` : valeur initiale.

### `ttk.Combobox(...)`

Paramètres utilisés :

- `master` : parent.
- `textvariable` : variable Tk synchronisée avec la sélection.
- `values` : liste des options.
- `state="readonly"` : interdit la saisie libre, seulement sélection.
- `width=25` : largeur en “caractères” approximatifs.

Récupération de la valeur :

```python
nom_materiel = self.selected_materiel.get()
```

---

## 8) Champs de saisie

```python
self.entry_debut = ttk.Entry(form, width=12)
self.entry_debut.insert(0, "08:00")
```

### `ttk.Entry(...)`

Paramètres utilisés :

- `master` : parent.
- `width=12` : largeur du champ.

### Méthodes utilisées

- `insert(index, texte)` : insère du texte (`index=0` = début).
- `get()` : lit le contenu saisi.

---

## 9) Boutons

```python
btn_add = ttk.Button(form, text="Ajouter", command=self.ajouter_utilisation)
```

### `ttk.Button(...)`

Paramètres utilisés :

- `master` : parent.
- `text` : texte du bouton.
- `command` : fonction appelée au clic (sans parenthèses).

Boutons présents :

- `Ajouter`
- `Calculer panneau/batterie`
- `Supprimer la sélection`
- `Tout supprimer`

---

## 10) Tableau `Treeview`

```python
self.table = ttk.Treeview(
    table_frame,
    columns=("materiel", "puissance", "debut", "fin"),
    show="headings",
    height=10,
)
```

### Paramètres `Treeview`

- `master` : parent.
- `columns=(...)` : identifiants des colonnes de données.
- `show="headings"` : n’affiche que les en-têtes (pas la colonne arbre implicite).
- `height=10` : nombre de lignes visibles.

### En-têtes

```python
self.table.heading("materiel", text="Matériel")
```

- `heading(colonne, text=...)` : titre visible de la colonne.

### Colonnes

```python
self.table.column("puissance", width=120, anchor="center")
```

- `column(colonne, width=...)` : largeur.
- `anchor="center"` : alignement du contenu.

### Insertion de lignes

```python
self.table.insert("", "end", iid=str(index), values=(...))
```

Paramètres :

- parent `""` : niveau racine.
- index `"end"` : ajoute à la fin.
- `iid` : identifiant unique de la ligne (utilisé pour supprimer précisément).
- `values` : tuple de valeurs selon l’ordre des colonnes.

### Lecture et suppression

- `self.table.get_children()` : liste des lignes.
- `self.table.delete(item_id)` : supprime une ligne.
- `self.table.selection()` : lignes sélectionnées.

---

## 11) Scrollbar verticale

```python
scrollbar = ttk.Scrollbar(table_frame, orient="vertical", command=self.table.yview)
self.table.configure(yscrollcommand=scrollbar.set)
```

### `ttk.Scrollbar(...)`

Paramètres utilisés :

- `master` : parent.
- `orient="vertical"` : orientation verticale.
- `command=self.table.yview` : relie le scroll à la vue verticale du tableau.

### `configure(yscrollcommand=...)`

- `yscrollcommand=scrollbar.set` : met à jour visuellement la scrollbar selon la position dans le tableau.

---

## 12) Boîtes de dialogue `messagebox`

Utilisations :

```python
messagebox.showerror("Erreur", "...")
messagebox.showwarning("Attention", "...")
confirmer = messagebox.askyesno("Confirmation", "Supprimer toutes les utilisations ?")
```

- `showerror(titre, message)` : erreur bloquante.
- `showwarning(titre, message)` : avertissement.
- `askyesno(titre, message)` : renvoie `True` (Oui) ou `False` (Non).

---

## 13) Résumé des paramètres Tkinter/ttk utilisés dans ce projet

- Fenêtre : `title`, `geometry`, `mainloop`.
- Layout : `pack(fill, expand, side, anchor, padx, pady)`, `grid(row, column, sticky, padx, pady)`.
- Widgets :
  - `Label(text, font)`
  - `Frame(padding)`
  - `LabelFrame(text, padding)`
  - `Entry(width)` + `insert`, `get`
  - `Button(text, command)`
  - `Combobox(textvariable, values, state, width)`
  - `Treeview(columns, show, height)` + `heading`, `column`, `insert`, `selection`, `get_children`, `delete`
  - `Scrollbar(orient, command)`
- Variables Tk : `StringVar(value)`
- Style ttk : `Style(...).configure(...)`
- Dialogues : `showerror`, `showwarning`, `askyesno`
- MàJ widget : `config(text=...)`, `configure(yscrollcommand=...)`

---

## 14) Remarque importante

Tkinter utilise des **chaînes** pour beaucoup d’options (ex: `"both"`, `"x"`, `"readonly"`, `"center"`). Une faute de frappe dans ces chaînes peut casser l’affichage ou produire un comportement inattendu.

Si tu veux, je peux aussi te faire une version “cheat sheet” ultra courte (1 page) avec seulement : widget, syntaxe minimale, paramètres utiles.

---

## 15) Options exactes utilisées dans ton interface (valeurs réelles)

Voici la liste **exacte** des options que j’ai utilisées dans `src/main.py`.

### Fenêtre racine (`root`)

- `title("Dimensionnement électrique")`
- `geometry("860x560")`

### Style ttk

- `style.configure("Treeview", rowheight=28, font=("Arial", 11))`
- `style.configure("Treeview.Heading", font=("Arial", 11, "bold"))`

### Containers

- `ttk.Frame(self.root, padding=12)`
- `ttk.LabelFrame(container, text="Utilisations ajoutées", padding=8)`
- `ttk.LabelFrame(container, text="Résultats", padding=10)`

### Positionnement (`pack`)

- `pack(fill="both", expand=True)`
- `pack(fill="x", pady=(0, 10))`
- `pack(anchor="w", pady=(0, 10))`
- `pack(side="left", fill="both", expand=True)`
- `pack(side="right", fill="y")`
- `pack(fill="x", pady=(8, 0))`
- `pack(side="left")`
- `pack(side="left", padx=(8, 0))`

### Positionnement (`grid`)

- `grid(row=0, column=0, sticky="w", padx=(0, 8))`
- `grid(row=0, column=1, sticky="w", padx=(0, 8))`
- `grid(row=0, column=2, sticky="w", padx=(0, 8))`
- `grid(row=1, column=0, sticky="w", padx=(0, 8), pady=(4, 0))`
- `grid(row=1, column=1, sticky="w", padx=(0, 8), pady=(4, 0))`
- `grid(row=1, column=2, sticky="w", padx=(0, 8), pady=(4, 0))`
- `grid(row=1, column=3, sticky="w", padx=(4, 0), pady=(4, 0))`
- `grid(row=1, column=4, sticky="w", padx=(8, 0), pady=(4, 0))`

### Labels

- `ttk.Label(..., text="Ajout d'utilisation", font=("Arial", 15, "bold"))`
- `ttk.Label(..., text="Matériel")`
- `ttk.Label(..., text="Heure début (HH:MM)")`
- `ttk.Label(..., text="Heure fin (HH:MM)")`
- `ttk.Label(..., text="Panneau nécessaire : -")`
- `ttk.Label(..., text="Batterie nécessaire : -")`

### Combobox

- `ttk.Combobox(..., textvariable=self.selected_materiel, values=[...], state="readonly", width=25)`
- `tk.StringVar(value=self.materiels[0].nom)`

### Entry

- `ttk.Entry(..., width=12)`
- `insert(0, "08:00")`
- `insert(0, "10:00")`

### Buttons

- `ttk.Button(..., text="Ajouter", command=self.ajouter_utilisation)`
- `ttk.Button(..., text="Calculer panneau/batterie", command=self.calculer_resultats)`
- `ttk.Button(..., text="Supprimer la sélection", command=self.supprimer_selection)`
- `ttk.Button(..., text="Tout supprimer", command=self.tout_supprimer)`

### Treeview

- `ttk.Treeview(..., columns=("materiel", "puissance", "debut", "fin"), show="headings", height=10)`
- `heading("materiel", text="Matériel")`
- `heading("puissance", text="Puissance (W)")`
- `heading("debut", text="Début (h)")`
- `heading("fin", text="Fin (h)")`
- `column("materiel", width=250)`
- `column("puissance", width=120, anchor="center")`
- `column("debut", width=100, anchor="center")`
- `column("fin", width=100, anchor="center")`
- `insert("", "end", iid=str(index), values=(...))`
- `configure(yscrollcommand=scrollbar.set)`

### Scrollbar

- `ttk.Scrollbar(..., orient="vertical", command=self.table.yview)`

### MessageBox

- `messagebox.showerror("Erreur", "...")`
- `messagebox.showwarning("Attention", "...")`
- `messagebox.askyesno("Confirmation", "Supprimer toutes les utilisations ?")`

### Boucle UI

- `root.mainloop()`
