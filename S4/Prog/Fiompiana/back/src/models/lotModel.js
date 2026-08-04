const db = require('../config/db');

exports.getAllLots = (callback) => {
  // include race name for display
  const sql = `SELECT l.*, r.nom AS race_nom
               FROM lot l
               LEFT JOIN race r ON l.race_id = r.id`;
  db.query(sql, callback);
};

exports.getLotById = (id, callback) => {
  db.query('SELECT * FROM lot WHERE id = ?', [id], callback);
};

exports.createLot = (lot, callback) => {
  const sql = 'INSERT INTO lot (nom, date_entree, nb_akoho_initial, nb_lahy, nb_vavy, race_id, age_semaine) VALUES (?, ?, ?, ?, ?, ?, ?)';
  db.query(sql, [lot.nom, lot.date_entree, lot.nb_akoho_initial, lot.nb_lahy || 0, lot.nb_vavy || 0, lot.race_id, lot.age_semaine], callback);
};

// update/delete operations removed; only create and read supported

exports.getAllVisiteLot = (callback) => {
  const sql = `SELECT v.*, l.nom AS lot_nom
               FROM visiteLot v
               LEFT JOIN lot l ON v.lot_id = l.id`;
  db.query(sql, callback);
};

exports.getVisiteById = (id, callback) => {
  db.query('SELECT * FROM visiteLot WHERE id = ?', [id], callback);
};

exports.createVisiteLot = (v, callback) => {
  const sql = 'INSERT INTO visiteLot (lot_id, date_visite, lahy_maty, vavy_maty, nb_atody, simba) VALUES (?, ?, ?, ?, ?, ?)';
  db.query(sql, [
    v.lot_id,
    v.date_visite,
    v.lahy_maty || 0,
    v.vavy_maty || 0,
    v.nb_atody,
    v.simba || 0
  ], callback);
};

// update/delete for visiteLot removed as well


exports.getTotalOeufLot = (lot_id, callback) => {
  // calculate available eggs for a lot at the current moment. if there is a
  // stock record (restant après un transfert), use that plus any eggs produced
  // since the stock date; otherwise simply total all visiteLot records.
  const sql = `
    SELECT
      CASE
        WHEN sa.stock_qty IS NOT NULL THEN
          sa.stock_qty + COALESCE(
            (SELECT SUM(nb_atody) FROM visiteLot v2
             WHERE v2.lot_id = ? AND v2.date_visite > sa.stock_date), 0)
        ELSE v.total_oeuf
      END AS total_oeuf
    FROM (
      SELECT SUM(nb_atody) AS total_oeuf
      FROM visiteLot
      WHERE lot_id = ?
    ) v
    LEFT JOIN (
      SELECT s.lot_id, s.nombre AS stock_qty, s.date AS stock_date
      FROM stockAtody s
      JOIN (
        SELECT lot_id, MAX(date) AS maxdate
        FROM stockAtody
        WHERE lot_id = ?
        GROUP BY lot_id
      ) m ON m.lot_id = s.lot_id AND m.maxdate = s.date
    ) sa ON sa.lot_id = ?
  `;
  db.query(sql, [lot_id, lot_id, lot_id, lot_id], callback);
};


// create a new stock record when eggs are transferred
exports.createStockAtody = (stock, callback) => {
  const sql = 'INSERT INTO stockAtody (lot_id, race_id, nombre, date) VALUES (?, ?, ?, ?)';
  db.query(sql, [stock.lot_id, stock.race_id, stock.nombre, stock.date], callback);
};

exports.getSituationLot = (dateChoisie, callback) => {
  // compute situation of each lot at the given date using correlated subqueries
  // the query aggregates mortalities and eggs, and derives the current age
  // to select the proper variation records. Food cost is based on surviving birds.

  const sql = `
SELECT
  l.id AS NumLot,
  l.nom AS NomLot,
  (COALESCE(vlAgg.LahyMaty,0) + COALESCE(vlAgg.VavyMaty,0)) AS Maty,
  (l.nb_akoho_initial - (COALESCE(vlAgg.LahyMaty,0) + COALESCE(vlAgg.VavyMaty,0))) AS NbAkoho,
  (l.nb_lahy - COALESCE(vlAgg.LahyMaty,0)) AS NbLahy,
  (l.nb_vavy - COALESCE(vlAgg.VavyMaty,0)) AS NbVavy,
  ((l.nb_vavy - COALESCE(vlAgg.VavyMaty,0)) * pr.max_atody_par_poule) AS CapaciteMaxAtody,
  (l.nb_akoho_initial * pr.prix_achat_poussin) AS Achat,
  (COALESCE(vpAgg.total_sakafo,0)
     * pr.prixU_sakafo_g
     * (l.nb_akoho_initial - (COALESCE(vlAgg.LahyMaty,0) + COALESCE(vlAgg.VavyMaty,0)))
  ) AS SakafoLany, 
  COALESCE(vpAgg.weight_current,0) AS PoidsMoyen,
  ((l.nb_akoho_initial - (COALESCE(vlAgg.LahyMaty,0) + COALESCE(vlAgg.VavyMaty,0)))
     * COALESCE(vpAgg.weight_current,0)
     * pr.prixV_kg_poulet
  ) AS PrixVente,
  -- calculate eggs available: stock_qty (default 0) + visits to date - visits before stock
  (COALESCE(sa.stock_qty,0) + COALESCE(vlAgg.NbAtody,0) - COALESCE(sa.visits_before_stock,0))
    AS NbAtody,
  (COALESCE(sa.stock_qty,0) + COALESCE(vlAgg.NbAtody,0) - COALESCE(sa.visits_before_stock,0))
    * pr.prixV_atody AS PrixAtody,
  (
    ((l.nb_akoho_initial - (COALESCE(vlAgg.LahyMaty,0) + COALESCE(vlAgg.VavyMaty,0)))
        * COALESCE(vpAgg.weight_current,0)
        * pr.prixV_kg_poulet)
    + (COALESCE(sa.stock_qty,0) + COALESCE(vlAgg.NbAtody,0) - COALESCE(sa.visits_before_stock,0)) * pr.prixV_atody
    - (l.nb_akoho_initial * pr.prix_achat_poussin)
    - (COALESCE(vpAgg.total_sakafo,0)
         * pr.prixU_sakafo_g
         * (l.nb_akoho_initial - (COALESCE(vlAgg.LahyMaty,0) + COALESCE(vlAgg.VavyMaty,0))))
  ) AS Benefice
FROM lot l
JOIN prixRace pr ON pr.race_id = l.race_id
LEFT JOIN (
  SELECT lot_id,
         SUM(lahy_maty) AS LahyMaty,
         SUM(vavy_maty) AS VavyMaty,
         SUM(nb_atody) AS NbAtody
  FROM visiteLot
  WHERE date_visite <= ?
  GROUP BY lot_id
) vlAgg ON vlAgg.lot_id = l.id
LEFT JOIN (
  SELECT l2.id AS lot_id,
         SUM(vp.sakafo_g) AS total_sakafo,
         MAX(vp.poids_g) AS weight_current
  FROM lot l2
  JOIN variationPoids vp
    ON vp.race_id = l2.race_id
   AND vp.semaine <= l2.age_semaine + FLOOR(DATEDIFF(?, l2.date_entree)/7)
  GROUP BY l2.id
) vpAgg ON vpAgg.lot_id = l.id
-- latest stock before date, with amount and date
LEFT JOIN (
  SELECT s.lot_id,
         s.nombre AS stock_qty,
         s.date AS stock_date,
         (SELECT SUM(nb_atody)
          FROM visiteLot v2
          WHERE v2.lot_id = s.lot_id
            AND v2.date_visite <= s.date) AS visits_before_stock
  FROM stockAtody s
  JOIN (
    SELECT lot_id, MAX(date) AS maxdate
    FROM stockAtody
    WHERE date <= ?
    GROUP BY lot_id
  ) m ON m.lot_id = s.lot_id AND m.maxdate = s.date
) sa ON sa.lot_id = l.id
WHERE l.date_entree <= ?
ORDER BY l.nom;
  `;

  db.query(sql, [dateChoisie, dateChoisie, dateChoisie, dateChoisie], callback);
};

// incubation support
exports.getAllIncubations = (callback) => {
  const sql = `
    SELECT i.*, l.nom AS lot_source_nom, r.nom AS race_nom
    FROM incubation i
    JOIN lot l ON l.id = i.lot_source
    JOIN race r ON r.id = i.race_id
    ORDER BY i.date_debut DESC
  `;
  db.query(sql, callback);
};

exports.getIncubationById = (id, callback) => {
  db.query('SELECT * FROM incubation WHERE id = ?', [id], callback);
};

exports.createIncubation = (inc, callback) => {
  const sql = `
    INSERT INTO incubation (
      lot_source, race_id, nb_oeufs,
      date_debut, date_eclosion_prevue, etat,
      nb_oeufs_hatched, nb_oeufs_failed, pct_lahy, pct_vavy
    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `;
  db.query(sql, [
    inc.lot_source,
    inc.race_id,
    inc.nb_oeufs,
    inc.date_debut,
    inc.date_eclosion_prevue,
    inc.etat || 'incubating',
    inc.nb_oeufs_hatched || 0,
    inc.nb_oeufs_failed || 0,
    inc.pct_lahy || 0,
    inc.pct_vavy || 0
  ], callback);
};

exports.updateIncubation = (id, updates, callback) => {
  const fields = [];
  const values = [];
  ['etat', 'nb_oeufs_hatched', 'nb_oeufs_failed', 'pct_lahy', 'pct_vavy'].forEach(key => {
    if (updates[key] !== undefined) {
      fields.push(`${key} = ?`);
      values.push(updates[key]);
    }
  });
  if (!fields.length) return callback(null, { affectedRows: 0 });
  values.push(id);
  const sql = `UPDATE incubation SET ${fields.join(', ')} WHERE id = ?`;
  db.query(sql, values, callback);
};
