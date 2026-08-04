CREATE DATABASE jxpoint;

\c jxpoint;

CREATE TABLE joueurs (
  id SERIAL PRIMARY KEY,
  nom TEXT NOT NULL,
  date_creation TIMESTAMPTZ NOT NULL
);

CREATE TABLE parties (
  id SERIAL PRIMARY KEY,
  joueur1_id INT REFERENCES joueurs(id),
  joueur2_id INT REFERENCES joueurs(id),
  date TIMESTAMPTZ NOT NULL,
  gagnant_id INT REFERENCES joueurs(id),
  statut TEXT
);

CREATE TABLE sommets (
  id SERIAL PRIMARY KEY,
  partie_id INT REFERENCES parties(id),
  col INT NOT NULL,
  row INT NOT NULL,
  joueur INT NOT NULL
);