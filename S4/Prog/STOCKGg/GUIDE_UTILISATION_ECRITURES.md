# Guide d'Utilisation: Système Automatique des Écritures Comptables

## Aperçu Rapide

Lors de chaque mouvement de stock (ENTREE, SORTIE, AJUSTEMENT), le système génère automatiquement:
- Une **écriture mère** (entête) avec la date et la description
- Une ou deux **écritures fils** (détails) avec les comptes débités/crédités

Les écritures sont créées automatiquement dans la même transaction que le mouvement.

---

## Installation de la Base de Données

Exécutez le script SQL pour créer les tables:

```bash
psql -U postgres -d gestion_stock -f sql_ecritures.sql
```

Cela crée:
- Table `ecriture_mere`
- Table `ecriture_fils`
- 3 vues pour consulter les écritures

---

## Utilisation dans le Code

### 1. Enregistrer une Entrée de Stock

```java
Article article = new Article();
article.setId(1);

StockService stockService = new StockService();
stockService.enregistrerEntree(
    article,
    LocalDate.of(2026, 6, 3),      // date
    new BigDecimal("100"),          // quantité
    new BigDecimal("50.00"),        // prix unitaire
    "FACTURE-001",                  // référence source
    "Achat",                        // type source
    "Fournisseur ABC",              // tiers
    "Achat initial"                 // motif
);
```

**Résultat comptable:**
```
Dr. 3100 (Stock)              5000.00
    Cr. 4011 (Fournisseur)        5000.00
```

### 2. Enregistrer une Sortie de Stock

```java
stockService.enregistrerSortie(
    article,
    LocalDate.of(2026, 6, 4),      // date
    new BigDecimal("25"),           // quantité
    new BigDecimal("50.00"),        // prix unitaire
    "VENTE-042",                    // référence source
    "Vente",                        // type source
    "Client XYZ",                   // tiers
    "Vente produits"                // motif
);
```

**Résultat comptable:**
```
Dr. 6031 (Variation de stock)  1250.00
    Cr. 3100 (Stock)               1250.00
```

---

## Consulter les Écritures Comptables

### Vue Complète avec Détails

```sql
SELECT 
    em.date_ecriture,
    em.libelle,
    ef.numero_compte,
    ef.libelle as detail,
    ef.debit,
    ef.credit
FROM ecriture_mere em
JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
ORDER BY em.date_ecriture DESC;
```

### Vérifier l'Équilibre

```sql
SELECT 
    ecriture_mere_id,
    total_debit,
    total_credit,
    (total_debit - total_credit) as difference,
    CASE WHEN total_debit = total_credit 
         THEN 'ÉQUILIBRÉE ✓' 
         ELSE 'DÉSÉQUILIBRÉE ✗' 
    END as etat
FROM v_ecriture_summary;
```

### Tracer un Mouvement

```sql
SELECT 
    m.date_mouvement,
    m.type_mouvement,
    m.quantite,
    m.valeur_mouvement,
    em.id as ecriture_mere_id,
    ef.numero_compte,
    ef.debit,
    ef.credit
FROM mouvement m
LEFT JOIN ecriture_mere em ON m.id = em.mouvement_id
LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
WHERE m.id = 42
ORDER BY ef.id;
```

### Consulter par Article

```sql
SELECT 
    a.code,
    m.date_mouvement,
    m.type_mouvement,
    m.quantite,
    em.libelle,
    ef.numero_compte,
    ef.debit,
    ef.credit
FROM article a
JOIN mouvement m ON a.id = m.article_id
LEFT JOIN ecriture_mere em ON m.id = em.mouvement_id
LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
WHERE a.code = 'ART001'
ORDER BY m.date_mouvement DESC;
```

---

## Mapping des Comptes

| Type de Mouvement | Débit | Crédit | Description |
|-------------------|-------|--------|-------------|
| **ENTREE** | 3100 (Stock) | 4011 (Fournisseur) | Augmentation du stock |
| **SORTIE** | 6031 (Variation) | 3100 (Stock) | Réduction du stock |
| **AJUSTEMENT+** | 3100 (Stock) | 6031 (Variation) | Correction d'inventaire |
| **AJUSTEMENT-** | 6031 (Variation) | 3100 (Stock) | Correction d'inventaire |

### Changer les Comptes Par Défaut

Pour personnaliser les comptes, modifiez les constantes dans `EcritureService`:

```java
private static final String COMPTE_STOCK = "3100";              // à modifier
private static final String COMPTE_VARIATION_STOCK = "6031";    // à modifier
private static final String COMPTE_FOURNISSEUR = "4011";        // à modifier
```

---

## Exemples de Situations Réelles

### Scénario 1: Achat et Vente

```java
// Jour 1: Achat 100 unités à 50€
stockService.enregistrerEntree(article, date1, BigDecimal.valueOf(100), 
    BigDecimal.valueOf(50), "BL-001", "Achat", "Fournisseur A", null);

// Jour 3: Vente 30 unités
stockService.enregistrerSortie(article, date2, BigDecimal.valueOf(30), 
    BigDecimal.valueOf(50), "BL-002", "Vente", "Client B", null);

// Jour 5: Vente 20 unités
stockService.enregistrerSortie(article, date3, BigDecimal.valueOf(20), 
    BigDecimal.valueOf(50), "BL-003", "Vente", "Client C", null);
```

**Écritures générées:**
```
2026-06-01:
Dr. 3100 (Stock)              5000
    Cr. 4011 (Fournisseur)        5000

2026-06-03:
Dr. 6031 (Variation)          1500
    Cr. 3100 (Stock)             1500

2026-06-05:
Dr. 6031 (Variation)          1000
    Cr. 3100 (Stock)             1000
```

### Scénario 2: Ajustement d'Inventaire

```java
// Correction découverte lors d'un inventaire physique
// Physique: 45 unités au lieu de 50 (perte de 5)
stockService.enregistrerAjustement(article, LocalDate.now(), 
    BigDecimal.valueOf(-5), BigDecimal.valueOf(50),
    null, "Inventaire", null, "Casse découverte");

// Le mouvement génère automatiquement:
// Dr. 6031 (Variation de stock)    250
//     Cr. 3100 (Stock)                250
```

---

## Vérifications et Maintenance

### Audit: Comparer Mouvements et Écritures

```sql
-- Vérifier que tout mouvement a une écriture
SELECT 
    m.id,
    m.type_mouvement,
    m.valeur_mouvement,
    em.id as ecriture_id,
    CASE WHEN em.id IS NULL THEN 'MANQUANTE' ELSE 'OK' END as status
FROM mouvement m
LEFT JOIN ecriture_mere em ON m.id = em.mouvement_id
WHERE m.id NOT IN (SELECT DISTINCT mouvement_id FROM ecriture_mere WHERE mouvement_id IS NOT NULL);
```

### Reconciliation: Valeurs vs Stock

```sql
-- Vérifier cohérence valeur mouvement = débit/crédit
SELECT 
    m.id,
    m.valeur_mouvement,
    COALESCE(SUM(ef.debit), 0) + COALESCE(SUM(ef.credit), 0) as somme_ecriture,
    CASE WHEN m.valeur_mouvement = COALESCE(SUM(ef.debit), 0) + COALESCE(SUM(ef.credit), 0)
         THEN 'OK' ELSE 'INCOHÉRENCE' END as status
FROM mouvement m
LEFT JOIN ecriture_mere em ON m.id = em.mouvement_id
LEFT JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
GROUP BY m.id, m.valeur_mouvement;
```

---

## Gestion des Erreurs

Si une erreur survient lors de la génération d'écriture:
1. Le mouvement est **annulé** (rollback de la transaction)
2. L'exception `BusinessException` est levée
3. Aucune écriture comptable n'est créée

```java
try {
    stockService.enregistrerEntree(article, date, qty, pu, ref, type, tiers, motif);
} catch (BusinessException e) {
    System.err.println("Erreur: " + e.getMessage());
    // Le mouvement a été annulé
}
```

---

## Performance

- Indexes créés sur `date_ecriture`, `journal`, `numero_compte`, `mouvement_id`
- Requêtes optimisées avec EXPLAIN ANALYZE pour les rapports
- Transactions courtes (une seule connexion)
- Pas de N+1 queries (joins optimisés)

---

## Extensions Possibles

### 1. Paramétrage des Comptes par Article

```java
// mapping_compte.csv:
// article_code,type_mouvement,compte_debit,compte_credit
// ART001,ENTREE,3100,4011
// ART002,ENTREE,3101,4012

// Dans EcritureService:
private Map<String, String> accountMapping;
```

### 2. Écritures d'Ajustement Automatiques

```java
// Générer les écritures de bilan mensuels
public void genererEcrituresBilan(LocalDate dateDebut, LocalDate dateFin) {
    // ...
}
```

### 3. Export Comptable

```java
public void exporterComptabilite(LocalDate date, String format) {
    // Exporter au format FEC (Fichier des Écritures Comptables) pour audit
    // Format requis par l'administration fiscale
}
```

---

## Fichiers Principaux

```
com/gestion/stock/
├── metier/
│   ├── entity/
│   │   ├── EcritureMere.java         ← Entête d'écriture
│   │   ├── EcritureFils.java         ← Détails d'écriture
│   │   └── Mouvement.java            ← Mouvement de stock
│   ├── repository/
│   │   ├── EcritureMereRepository.java   ← DAO pour entêtes
│   │   ├── EcritureFilsRepository.java   ← DAO pour détails
│   │   └── MouvementRepository.java      ← DAO pour mouvements
│   └── service/
│       ├── EcritureService.java      ← Service d'écriture (cœur du système)
│       └── StockService.java         ← Service de stock (appelle EcritureService)
└── sql_ecritures.sql                 ← Scripts de création des tables
```

---

## Support et Maintenance

Pour toute question ou correction:
1. Vérifier les tests unitaires
2. Consulter la documentation SQL
3. Tracer les logs d'erreur
4. Vérifier la cohérence des données avec les requêtes d'audit
