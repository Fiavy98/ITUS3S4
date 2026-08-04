-- ======================================
-- 1️⃣ Races
-- ======================================
INSERT INTO race (nom) VALUES
('R1'),
('R2');

-- ======================================
-- 2️⃣ Lots
-- ======================================
INSERT INTO lot (nom, date_entree, nb_akoho_initial, nb_lahy, nb_vavy, race_id, age_semaine) VALUES
('Lot 1', '2026-03-01', 100, 50, 50, 1, 0),
('Lot 2', '2026-03-03', 50, 25, 25, 2, 0);

-- ======================================
-- 3️⃣ VariationPoids
-- ======================================
-- Semaines 0 à 2 pour R1
INSERT INTO variationPoids (semaine, race_id, poids_g, sakafo_g) VALUES
(0, 1, 12.00, 12.00),
(1, 1, 15.00, 20.00),
(2, 1, 20.00, 30.00);

-- Semaines 0 à 2 pour R2
INSERT INTO variationPoids (semaine, race_id, poids_g, sakafo_g) VALUES
(0, 2, 10.00, 10.00),
(1, 2, 13.00, 18.00),
(2, 2, 18.00, 25.00);

-- ======================================
-- 4️⃣ PrixRace
-- ======================================
INSERT INTO prixRace (race_id, prixU_sakafo_g, prixV_kg_poulet, prixV_atody, prix_achat_poussin, nbJr_fohy, max_atody_par_poule) VALUES
(1, 200.00, 10000.00, 700.00, 500.00, 21, 4),
(2, 180.00, 9000.00, 600.00, 450.00, 20, 3);

-- ======================================
-- 5️⃣ VisiteLot
-- ======================================
-- Lot 1
INSERT INTO visiteLot (lot_id, date_visite, lahy_maty, vavy_maty, nb_atody, simba) VALUES
(1, '2026-03-02', 0, 1, 5, 0),
(1, '2026-03-05', 1, 1, 10, 0),
(1, '2026-03-08', 0, 1, 8, 0);

-- Lot 2
INSERT INTO visiteLot (lot_id, date_visite, lahy_maty, vavy_maty, nb_atody, simba) VALUES
(2, '2026-03-04', 0, 0, 3, 0),
(2, '2026-03-06', 1, 0, 4, 0);


--============= DATA VIERGE ================================
-- Placeholder templates for new data (commented out to avoid execution errors).
-- INSERT INTO race (nom) VALUES ('');
-- INSERT INTO lot (nom, date_entree, nb_akoho_initial, nb_lahy, nb_vavy, race_id, age_semaine) VALUES ('Lot', '2026-01-01', 0, 0, 0, 1, 0);
-- INSERT INTO variationPoids (semaine, race_id, poids_g, sakafo_g) VALUES (0, 1, 0, 0);
-- INSERT INTO prixRace (race_id, prixU_sakafo_g, prixV_kg_poulet, prixV_atody, prix_achat_poussin, nbJr_fohy, max_atody_par_poule) VALUES (1, 0, 0, 0, 0, 0, 0);
-- INSERT INTO visiteLot (lot_id, date_visite, lahy_maty, vavy_maty, nb_atody, simba) VALUES (1, '2026-01-01', 0, 0, 0, 0);

