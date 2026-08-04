const raceModel = require('../models/raceModel');

exports.getRaces = (req, res) => {
  raceModel.getAllRaces((err, results) => {
    if (err) {
      res.status(500).json(err);
    } else {
      res.json(results);
    }
  });
};

exports.getVariationPoids = (req, res) => {
  const { race_id } = req.query;
  if (race_id) {
    raceModel.getVariationByRace(race_id, (err, results) => {
      if (err) return res.status(500).json(err);
      return res.json(results);
    });
  } else {
    raceModel.getAllVariationPoids((err, results) => {
      if (err) return res.status(500).json(err);
      res.json(results);
    });
  }
};


exports.getPrixRace = (req, res) => {
  raceModel.getAllPrixRace((err, results) => {
    if (err) return res.status(500).json(err);
    res.json(results);
  });
};

// create handlers for race/variation/prix – update/delete removed
exports.createRace = (req, res) => {
  const race = req.body;
  if (!race.nom) return res.status(400).json({ error: 'nom requis' });
  raceModel.createRace(race, (err, result) => {
    if (err) return res.status(500).json(err);
    res.status(201).json({ id: result.insertId, ...race });
  });
};

// variation
exports.createVariation = (req, res) => {
  const v = req.body;
  raceModel.createVariation(v, (err, result) => {
    if (err) return res.status(500).json(err);
    res.status(201).json({ id: result.insertId, ...v });
  });
};

// calculate weight gain for a race between two weeks
exports.getWeightGain = (req, res) => {
  const { race_id, debut, fin } = req.query;
  if (!race_id) {
    return res.status(400).json({ error: 'race_id requis' });
  }

  const rId = parseInt(race_id, 10);
  const start = debut != null ? parseInt(debut, 10) : 0;
  let end = fin != null ? parseInt(fin, 10) : start;

  // On veut récupérer la variation jusqu'à Sfin-1
  end = Math.max(0, end - 1);

  raceModel.getWeightGain(rId, start, end, (err, result) => {
    if (err) return res.status(500).json(err);
    res.json(result);
  });
};

// prixRace
exports.createPrix = (req, res) => {
  const p = req.body;
  raceModel.createPrix(p, (err, result) => {
    if (err) return res.status(500).json(err);
    res.status(201).json({ id: result.insertId, ...p });
  });
};
