# Système de Génération Automatique des Écritures Comptables

## Vue d'ensemble

Ce système génère automatiquement des écritures comptables (écritures mère et écritures fils) pour chaque mouvement de stock (Entrée, Sortie, Ajustement). Les écritures sont créées dans une transaction PostgreSQL, garantissant la cohérence des données.

---

## Architecture

```
Mouvement Stock (ENTREE, SORTIE, AJUSTEMENT)
       ↓
StockService.enregistrerEntree()
StockService.enregistrerSortie()
       ↓
EcritureService.generaliserEcriture()
       ↓
┌─────────────────────────────────┐
│   EcritureMere (Entête)         │
│  - date                         │
│  - libelle                      │
│  - journal ("Stock")            │
│  - mouvement_id (FK)            │
└─────────────────────────────────┘
       ↓
┌─────────────────────────────────┐
│  EcritureFils (Détails)         │
│  - ecriture_mere_id (FK)        │
│  - numero_compte                │
│  - libelle                      │
│  - debit                        │
│  - credit                       │
│  - mouvement_id (FK)            │
└─────────────────────────────────┘
```

---

## Structure Base de Données

### Table `ecriture_mere`
Entête des écritures comptables

```sql
CREATE TABLE ecriture_mere (
    id              SERIAL PRIMARY KEY,
    date_ecriture   DATE NOT NULL,
    libelle         VARCHAR(255) NOT NULL,
    journal         VARCHAR(50) NOT NULL DEFAULT 'Stock',
    mouvement_id    INT REFERENCES mouvement(id) ON DELETE CASCADE,
    created_at      TIMESTAMP DEFAULT NOW()
);
```

**Champs:**
- `id`: Identifiant unique
- `date_ecriture`: Date de l'écriture comptable
- `libelle`: Description de l'écriture (ex: "ENTREE - 10 unités - ART001 - 2026-06-03")
- `journal`: Journal comptable ("Stock")
- `mouvement_id`: Référence au mouvement de stock qui a déclenché l'écriture
- `created_at`: Timestamp de création

### Table `ecriture_fils`
Détails des écritures comptables

```sql
CREATE TABLE ecriture_fils (
    id                  SERIAL PRIMARY KEY,
    ecriture_mere_id    INT NOT NULL REFERENCES ecriture_mere(id) ON DELETE CASCADE,
    numero_compte       VARCHAR(10) NOT NULL,
    libelle             VARCHAR(255) NOT NULL,
    debit               DECIMAL(15,4) DEFAULT 0,
    credit              DECIMAL(15,4) DEFAULT 0,
    mouvement_id        INT REFERENCES mouvement(id) ON DELETE CASCADE,
    created_at          TIMESTAMP DEFAULT NOW()
);
```

**Champs:**
- `id`: Identifiant unique
- `ecriture_mere_id`: Référence à l'écriture mère
- `numero_compte`: Numéro du compte comptable (ex: "3100" pour stock)
- `libelle`: Description de la ligne (ex: "Entrée ART001 - 10 unités")
- `debit`: Montant au débit
- `credit`: Montant au crédit
- `mouvement_id`: Référence au mouvement de stock source

---

## Implémentation

### 1. Entités

#### EcritureMere.java
- `dateEcriture`: Date de l'écriture
- `libelle`: Description
- `journal`: Journal (toujours "Stock")
- `mouvementId`: Référence au mouvement source
- `ecritureFils`: List des détails
- Méthodes utilitaires:
  - `getTotalDebit()`: Somme des débits
  - `getTotalCredit()`: Somme des crédits
  - `isEquilibree()`: Vérifie que débit = crédit

#### EcritureFils.java
- `ecritureMere`: Référence à l'entête
- `numeroCompte`: Numéro de compte (3100, 6031, 4011, etc.)
- `libelle`: Description du mouvement
- `debit`: Montant débité
- `credit`: Montant crédité
- `mouvementId`: Référence au mouvement stock

### 2. Repositories

#### EcritureMereRepository
- `save()`: Insère une écriture mère (standalone connection)
- `saveWithConnection()`: Insère avec une connexion existante (pour transactions)
- `findByMouvementId()`: Recherche l'écriture par ID mouvement
- `findByDate()`: Recherche par date

#### EcritureFilsRepository
- `save()`: Insère une ligne d'écriture
- `saveWithConnection()`: Insère avec connexion existante
- `findByEcritureMereId()`: Récupère toutes les lignes d'une écriture
- `findByMouvementId()`: Récupère les lignes par mouvement

### 3. Service: EcritureService

**Classe centrale**: `com.gestion.stock.metier.service.EcritureService`

#### Compte Comptables Par Défaut
```java
static final String COMPTE_STOCK = "3100";              // Compte de stock
static final String COMPTE_VARIATION_STOCK = "6031";    // Variation de stock (charges)
static final String COMPTE_FOURNISSEUR = "4011";        // Fournisseurs
static final String COMPTE_CLIENT = "4111";             // Clients
static final String COMPTE_TIERS_DIVERS = "4190";       // Tiers divers
```

#### Méthode Principale

```java
public void generaliserEcriture(Mouvement mouvement, Article article, 
                               String sourceCompte, Connection conn)
    throws BusinessException
```

**Paramètres:**
- `mouvement`: Le mouvement de stock à comptabiliser
- `article`: L'article concerné
- `sourceCompte`: Compte optionnel personnalisé
- `conn`: Connexion active (pour transaction)

**Processus:**
1. Crée l'écriture mère (entête)
2. Génère les écritures fils selon le type de mouvement
3. Valide que l'écriture est équilibrée (débit = crédit)

#### Écritures Généreées par Type

##### ENTREE (Stock Entry)
**Débit**: Stock (3100)  
**Crédit**: Fournisseur (4011) ou Tiers

```
Débit:  Stock (3100)              | Montant
Crédit: Fournisseur/Tiers (4011)  | Montant
```

**Exemple**: Entrée de 100 unités à 50€ = 5000€
```
Dr. 3100 (Stock)           5000
    Cr. 4011 (Fournisseur)     5000
```

##### SORTIE (Stock Exit)
**Débit**: Variation de stock (6031) - Charge  
**Crédit**: Stock (3100) - Réduction

```
Débit:  Variation de stock (6031) | Montant
Crédit: Stock (3100)               | Montant
```

**Exemple**: Sortie de 50 unités à 50€ = 2500€
```
Dr. 6031 (Variation de stock) 2500
    Cr. 3100 (Stock)             2500
```

##### AJUSTEMENT (Stock Adjustment)

**Ajustement positif** (augmentation) = Comme une ENTREE
```
Dr. 3100 (Stock)                    | Montant
    Cr. 6031 (Variation de stock)   | Montant
```

**Ajustement négatif** (diminution) = Comme une SORTIE
```
Dr. 6031 (Variation de stock) | Montant
    Cr. 3100 (Stock)          | Montant
```

---

## Intégration dans StockService

### Appels dans StockService

#### En Entrée
```java
public void enregistrerEntree(Article article, LocalDate date, BigDecimal quantite, 
                             BigDecimal prixUnitaire, String sourceRef, 
                             String sourceType, String sourceTiers, String motif)
{
    // ... création mouvement ...
    
    // Generate accounting entries for the entry
    ecritureService.generaliserEcriture(mouvement, article, null, conn);
    
    conn.commit();
}
```

#### En Sortie
```java
public void enregistrerSortie(Article article, LocalDate date, BigDecimal quantite, 
                             BigDecimal puSortie, String sourceRef, 
                             String sourceType, String sourceTiers, String motif)
{
    // ... création mouvement ...
    
    // Generate accounting entries for the exit
    ecritureService.generaliserEcriture(mouvement, article, null, conn);
    
    conn.commit();
}
```

---

## Requêtes SQL Utiles

### Voir Toutes les Écritures Comptables
```sql
SELECT * FROM v_ecriture_complete
ORDER BY date_ecriture DESC, ecriture_mere_id;
```

### Vérifier que les Écritures Sont Équilibrées
```sql
SELECT 
    ecriture_mere_id,
    total_debit,
    total_credit,
    CASE WHEN total_debit = total_credit THEN 'OK' ELSE 'KO' END as etat
FROM v_ecriture_summary;
```

### Lier Mouvements et Écritures Comptables
```sql
SELECT * FROM v_ecriture_par_mouvement
ORDER BY mouvement_id DESC;
```

### Rechercher Écritures par Article
```sql
SELECT 
    em.id as ecriture_id,
    em.date_ecriture,
    m.type_mouvement,
    m.quantite,
    m.valeur_mouvement,
    ef.numero_compte,
    ef.libelle,
    ef.debit,
    ef.credit
FROM ecriture_mere em
JOIN mouvement m ON em.mouvement_id = m.id
JOIN article a ON m.article_id = a.id
JOIN ecriture_fils ef ON em.id = ef.ecriture_mere_id
WHERE a.code = 'ART001'
ORDER BY em.date_ecriture DESC;
```

### Résumé par Journal et Date
```sql
SELECT 
    date_ecriture,
    journal,
    nb_ecritures,
    total_debit,
    total_credit,
    difference
FROM v_ecriture_summary
ORDER BY date_ecriture DESC;
```

---

## Transaction et Rollback

Toutes les opérations (mouvement + écritures comptables) se font dans une **transaction unique**:

```java
conn.setAutoCommit(false);
try {
    // 1. Créer/mettre à jour mouvement
    // 2. Générer écritures comptables
    conn.commit();  // Tout ou rien
} catch (Exception e) {
    conn.rollback(); // Annuler tout
}
```

Si l'écriture comptable échoue, le mouvement est aussi annulé.

---

## Validation

### Vérifications Effectuées

1. **Équilibre de l'écriture**: `debit_total == credit_total`
2. **Montants positifs**: Tous les montants en débit/crédit ≥ 0
3. **Intégrité référentielle**: mouvement_id existe dans la table mouvement

### Exceptions

- `BusinessException`: Erreur dans la génération d'écriture
- `SQLException`: Erreur base de données

---

## Exemple Complet: Entrée de Stock

### Données
- Article: ART001
- Quantité: 100 unités
- Prix unitaire: 50€
- Montant total: 5000€
- Fournisseur: Tiers-1

### Opérations Effectuées

**1. Créer Mouvement**
```
INSERT INTO mouvement (article_id, date_mouvement, type_mouvement, quantite, ...)
VALUES (1, '2026-06-03', 'ENTREE', 100, ...)
→ mouvement_id = 42
```

**2. Créer Écriture Mère**
```
INSERT INTO ecriture_mere (date_ecriture, libelle, journal, mouvement_id)
VALUES ('2026-06-03', 'ENTREE - 100 unités - ART001 - 2026-06-03', 'Stock', 42)
→ ecriture_mere_id = 101
```

**3. Créer Écritures Fils**
```
INSERT INTO ecriture_fils (ecriture_mere_id, numero_compte, libelle, debit, credit, mouvement_id)
VALUES 
    (101, '3100', 'Entrée ART001 - 100 unités', 5000, 0, 42),
    (101, '4011', 'Fournisseur/Tiers: Tiers-1', 0, 5000, 42);
```

**4. Validation**
- Total Débit: 5000
- Total Crédit: 5000
- Équilibré: ✓

---

## Points Clés

✅ **Générique**: Fonctionne pour tous les types de mouvements  
✅ **Transactionnel**: Mouvement + Écritures atomiques  
✅ **Traçable**: Chaque écriture liée au mouvement source (mouvement_id)  
✅ **Validé**: Vérification de l'équilibre comptable  
✅ **Flexible**: Comptes configurables, extensible  
✅ **PostgreSQL**: Utilise les features PostgreSQL (RETURNING, CASCADE)  

---

## Prochaines Étapes

Pour aller plus loin:

1. **Rapports comptables**: Générer des rapports de synthèse
2. **Rapprochement**: Vérifier que mouvements = écritures
3. **Corrections**: Écritures d'extourne/correction
4. **Personnalisation**: Mapping article → compte comptable
5. **Audit**: Historique des modifications
