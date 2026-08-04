# Base SQL Server

Ce dossier contient le script SQL pour créer la base `PuissanceElectriqueDB`.

## Ce que fait `db/init/schema.sql`

- crée la base si elle n’existe pas,
- crée les tables `Materiels`, `Configs` et `Utilisations`,
- insère les matériels de départ,
- insère les configurations horaires de départ.

## Utilisation

Comme tu ne veux pas de `docker compose`, ce script est prévu pour être exécuté sur un SQL Server déjà démarré, par exemple ton conteneur local existant.

Connexion attendue :
- server : `localhost,1433`
- user : `sa`
- password : `Az0nye47`

Exemple avec `sqlcmd` :

```bash
sqlcmd -S localhost,1433 -U sa -P "Az0nye47" -i db/init/schema.sql
```

Si tu préfères, tu peux aussi l’exécuter dans SSMS ou Azure Data Studio.

## Tables

### `Materiels`
- `Id` : clé primaire
- `Nom` : nom du matériel
- `Puissance_W` : puissance en watts

### `Configs`
- `Id` : clé primaire
- `HeureDebut` : début de plage horaire
- `HeureFin` : fin de plage horaire
- `Puissance` : coefficient utilisé par ton calcul

### `Utilisations`
- `Id` : clé primaire
- `MaterielId` : lien vers `Materiels`
- `HeureDebut`, `HeureFin` : période d’utilisation
