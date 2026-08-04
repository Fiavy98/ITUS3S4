const db = require('../config/db');

// --- races ---
exports.getAllRaces = (callback) => {
  db.query('SELECT * FROM race', callback);
};

exports.getRaceById = (id, callback) => {
  db.query('SELECT * FROM race WHERE id = ?', [id], callback);
};

exports.createRace = (race, callback) => {
  db.query('INSERT INTO race (nom) VALUES (?)', [race.nom], callback);
};

// removed update/delete operations as only creation and read are supported
exports.getAllVariationPoids = (callback) => {
  const sql = `SELECT vp.*, r.nom AS race_nom
               FROM variationPoids vp
               LEFT JOIN race r ON vp.race_id = r.id`;
  db.query(sql, callback);
};

// retrieve variations for a single race
exports.getVariationByRace = (raceId, callback) => {
  const sql = `SELECT vp.*, r.nom AS race_nom
               FROM variationPoids vp
               LEFT JOIN race r ON vp.race_id = r.id
               WHERE vp.race_id = ?`;
  db.query(sql, [raceId], callback);
};

// calculate weight gain between two weeks for a race
exports.getWeightGain = (raceId, startWeek, endWeek, callback) => {
  // ensure range is valid
  const start = Math.min(startWeek, endWeek);
  const end = Math.max(startWeek, endWeek);

  const sql = `SELECT
    MAX(CASE WHEN semaine = ? THEN poids_g END) AS startWeight,
    MAX(CASE WHEN semaine = ? THEN poids_g END) AS endWeight
    FROM variationPoids
    WHERE race_id = ? AND semaine BETWEEN ? AND ?`;
  db.query(sql, [start, end, raceId, start, end], (err, results) => {
    if (err) return callback(err);
    const row = results[0] || {};
    const startWeight = row.startWeight || 0;
    const endWeight = row.endWeight || 0;
    const gain = endWeight - startWeight;
    callback(null, { startWeight, endWeight, gain });
  });
};

exports.getVariationById = (id, callback) => {
  db.query('SELECT * FROM variationPoids WHERE id = ?', [id], callback);
};

exports.createVariation = (v, callback) => {
  const sql = 'INSERT INTO variationPoids (semaine, race_id, poids_g, sakafo_g) VALUES (?, ?, ?, ?)';
  db.query(sql, [v.semaine, v.race_id, v.poids_g, v.sakafo_g], callback);
};

// --- prixRace ---
exports.getAllPrixRace = (callback) => {
  const sql = `SELECT p.*, r.nom AS race_nom
               FROM prixRace p
               LEFT JOIN race r ON p.race_id = r.id`;
  db.query(sql, callback);
};

exports.getPrixById = (id, callback) => {
  db.query('SELECT * FROM prixRace WHERE id = ?', [id], callback);
};

exports.createPrix = (p, callback) => {
  const sql = 'INSERT INTO prixRace (race_id, prixU_sakafo_g, prixV_kg_poulet, prixV_atody, prix_achat_poussin, nbJr_fohy, max_atody_par_poule) VALUES (?, ?, ?, ?, ?, ?, ?)';
  db.query(sql, [
    p.race_id,
    p.prixU_sakafo_g,
    p.prixV_kg_poulet,
    p.prixV_atody,
    p.prix_achat_poussin,
    p.nbJr_fohy || 0,
    p.max_atody_par_poule || 0
  ], callback);
};

exports.getPrixByRaceId = (raceId, callback) => {
  db.query('SELECT * FROM prixRace WHERE race_id = ?', [raceId], callback);
};
