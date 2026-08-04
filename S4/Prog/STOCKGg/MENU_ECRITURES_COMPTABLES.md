# Menu: Tableau des Écritures Comptables

## Vue d'ensemble

Un nouvel onglet **"Écritures Comptables"** a été ajouté à l'interface principale pour afficher le tableau complet des écritures comptables générées.

---

## Accès

**Emplacement**: Menu principal → Onglet "Écritures Comptables"

**Classes impliquées:**
- `EcritureTablePanel.java` (Nouveau)
- `MainApplication.java` (Modifié)

---

## Fonctionnalités

### 1. 📊 Affichage du Tableau

Le tableau affiche **tous les détails des écritures comptables** :

| Colonne | Description |
|---------|-------------|
| **Date** | Date de l'écriture comptable |
| **Libellé** | Description de l'écriture mère |
| **Journal** | Journal comptable (toujours "Stock") |
| **Compte** | Numéro de compte comptable |
| **Description** | Détail de la ligne d'écriture |
| **Débit** | Montant débité |
| **Crédit** | Montant crédité |
| **Équilibrée** | ✓ si débit = crédit, ✗ sinon |
| **Mouvement ID** | Référence au mouvement source |

### 2. 🔄 Bouton "Rafraichir"

Recharge les données en temps réel depuis la base de données PostgreSQL.

**Utilité**: 
- Voir les nouvelles écritures après un mouvement de stock
- Synchroniser l'affichage avec les changements
- Vérifier l'équilibre comptable en direct

### 3. 📋 Bouton "Exporter"

Exporte le tableau complet au format texte tabulé dans le presse-papiers.

**Format**: Tab-separated values (TSV)
**Utilité**: 
- Coller dans Excel ou Calc
- Générer des rapports
- Archiver les données

---

## Formatage et Couleurs

### Montants (Débit / Crédit)
- Affichés en euros avec 2 décimales
- Alignés à droite
- **Fond vert clair** si non-zéro (pour meilleure lisibilité)
- Blanc si zéro

### État d'Équilibre
- ✓ = Écriture équilibrée (débit = crédit) ✓ OK
- ✗ = Écriture déséquilibrée ✗ ERREUR

---

## Requête SQL Utilisée

```sql
SELECT 
    em.date_ecriture,
    em.libelle as libelle_mere,
    em.journal,
    COALESCE(ef.numero_compte, 'N/A') as numero_compte,
    COALESCE(ef.libelle, '') as libelle_fils,
    COALESCE(ef.debit, 0) as debit,
    COALESCE(ef.credit, 0) as credit,
    CASE WHEN em.id IN (
        SELECT DISTINCT em2.id FROM ecriture_mere em2
        LEFT JOIN ecriture_fils ef2 ON em2.id = ef2.ecriture_mere_id
        GROUP BY em2.id
        HAVING SUM(COALESCE(ef2.debit, 0)) = SUM(COALESCE(ef2.credit, 0))
    ) THEN '✓' ELSE '✗' END as equilibree,
    em.mouvement_id
FROM ecriture_mere em
LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
ORDER BY em.date_ecriture DESC, em.id DESC, ef.id
```

**Caractéristiques:**
- JOIN entre ecriture_mere et ecriture_fils
- Tri par date décroissante (plus récent en premier)
- Vérification automatique de l'équilibre
- Support des écritures sans détail

---

## Exemple d'Utilisation

### Scénario: Entée de Stock

1. **Enregistrement d'une entrée** dans l'onglet "Mouvements"
   - Article: ART001
   - Quantité: 100 unités
   - Prix: 50€
   - Valeur: 5000€

2. **Résultat automatique**: Une écriture est créée

3. **Vérification dans "Écritures Comptables"**
   - Nouvelle ligne apparaît
   - Date: 2026-06-03
   - Libellé: "ENTREE - 100 unités - ART001 - 2026-06-03"
   - Deux lignes d'écriture fils:
     - Dr. 3100 (Stock): 5000.00 €
     - Cr. 4011 (Fournisseur): 5000.00 €
   - État: ✓ Équilibrée

4. **Cliquer "Rafraichir"** pour vérifier les dernières écritures

---

## Cas d'Usage

### 1. Audit Comptable
Vérifier que toutes les écritures sont correctement équilibrées.

```
Cliquer Rafraichir → Chercher ✗ dans la colonne "Équilibrée"
```

### 2. Suivi de Mouvement
Tracer un mouvement de stock jusqu'à son enregistrement comptable.

```
Chercher mouvement_id correspondant
```

### 3. Rapport Périodique
Exporter les écritures pour un audit/rapport.

```
Cliquer Exporter → Coller dans Excel
```

---

## Messages d'Erreur

Si une erreur survient lors du chargement:
- Une ligne "Erreur de chargement" apparaît
- L'exception est affichée dans la console
- Vérifier la connexion PostgreSQL

---

## Architecture Technique

### Classe: EcritureTablePanel

```java
public class EcritureTablePanel extends JPanel {
    - initControls()      // Boutons Rafraichir et Exporter
    - initTable()         // Structure du tableau
    - loadData()          // Requête SQL et remplissage
    - formatNumber()      // Formatage des montants
    - exportData()        // Export TSV
}
```

### Intégration

**MainApplication.java:**
```java
tabbedPane.addTab("Écritures Comptables", new EcritureTablePanel());
```

Cet onglet s'affiche après les onglets existants.

---

## Colonnes et Largeurs

| Colonne | Largeur | Rôle |
|---------|---------|------|
| Date | 100px | Identifier la date |
| Libellé | 250px | Description complète |
| Journal | 80px | Type de journal |
| Compte | 80px | Numéro de compte |
| Description | 200px | Détail de la ligne |
| Débit | 100px | Montant débité |
| Crédit | 100px | Montant crédité |
| Équilibrée | 100px | Statut de l'écriture |
| Mouvement ID | 100px | Traçabilité |

---

## Limitations et Améliorations Futures

### Actuelles
- Affichage de **toutes** les écritures (peut être lent avec beaucoup de données)
- Pas de pagination

### Améliorations Possibles
1. **Pagination**: Afficher 50 lignes par page
2. **Filtrage**: Par date, journal, ou état
3. **Tri**: Cliquer sur en-têtes pour trier
4. **Recherche**: Chercher par mouvement_id ou compte
5. **Statistiques**: Totaux par jour/journal
6. **Export PDF**: Rapport formaté

---

## Tests

Pour tester le nouveau menu:

1. **Démarrer l'application**
   ```bash
   mvn exec:java
   ```

2. **Créer des mouvements** dans l'onglet "Mouvements"

3. **Aller à l'onglet "Écritures Comptables"**

4. **Cliquer "Rafraichir"** pour voir les écritures générées

5. **Vérifier**:
   - ✓ Toutes les écritures apparaissent
   - ✓ État "✓" pour chaque écriture
   - ✓ Les montants correspondent
   - ✓ Export fonctionne

---

## Fichiers Modifiés

- ✨ **EcritureTablePanel.java** (NOUVEAU)
- ✅ **MainApplication.java** (1 ligne ajoutée)

---

**Statut**: ✅ Implémenté et compilé avec succès
