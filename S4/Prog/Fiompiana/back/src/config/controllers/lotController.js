const lotModel = require('../models/lotModel');
const raceModel = require('../models/raceModel');

exports.getLots = (req, res) => {
  lotModel.getAllLots((err, results) => {
    if (err) return res.status(500).json(err);
    res.json(results);
  });
};

exports.getVisiteLot = (req, res) => {
  lotModel.getAllVisiteLot((err, results) => {
    if (err) return res.status(500).json(err);
    res.json(results);
  });
};


exports.getSituationLot = (req, res) => {
  const dateChoisie = req.query.date; // on récupère la date depuis Angular
  if (!dateChoisie) return res.status(400).json({ error: 'date obligatoire' });

  lotModel.getSituationLot(dateChoisie, (err, results) => {
    if (err) {
      console.error(err);
      return res.status(500).json(err);
    }
    res.json(results);
  });
};

// CRUD pour lot (create only)
exports.createLot = (req, res) => {
  const lot = req.body;
  lotModel.createLot(lot, (err, result) => {
    if (err) return res.status(500).json(err);
    res.status(201).json({ id: result.insertId, ...lot });
  });
};

// visiteLot (create only) with optional egg destruction that updates stock
exports.createVisiteLot = (req, res) => {
  const v = req.body;
  lotModel.createVisiteLot(v, (err, result) => {
    if (err) return res.status(500).json(err);
    // if destroyed eggs were reported, update stock for the lot
    if (v.simba && v.simba > 0) {
      // compute current available eggs and subtract simba
      lotModel.getTotalOeufLot(v.lot_id, (err2, rows) => {
        if (err2) {
          console.error('Erreur calcul oeufs pour stock', err2);
        } else {
          const current = rows[0]?.total_oeuf || 0;
          const newQty = Math.max(current - v.simba, 0);
          // need race_id for stock record
          lotModel.getLotById(v.lot_id, (err3, lotRows) => {
            if (err3) return console.error(err3);
            if (lotRows.length) {
              const stockRec = {
                lot_id: v.lot_id,
                race_id: lotRows[0].race_id,
                nombre: newQty,
                date: v.date_visite
              };
              lotModel.createStockAtody(stockRec, (err4) => {
                if (err4) console.error('Erreur création stock après simba', err4);
              });
            }
          });
        }
      });
    }
    res.status(201).json({ id: result.insertId, ...v });
  });
};


//Atody atao anaty lot vaovao
exports.createLotFromOeuf = (req, res) => {

  const {
    lot_source_id,
    nb_oeuf_utilise,
    simba = 0,
    nom,
    date_entree,
    nb_lahy,
    nb_vavy,
    percent_lahy,
    percent_vavy
  } = req.body;

  // 1️⃣ récupérer le lot source
  lotModel.getLotById(lot_source_id, (err, lotResult) => {

    if (err) return res.status(500).json(err);

    if (!lotResult.length) {
      return res.status(404).json({
        message: "Lot source introuvable"
      });
    }

    const race_id = lotResult[0].race_id;

    // 2️⃣ récupérer le nombre total d'oeufs disponibles dans le lot source
    //    (getTotalOeufLot tient compte de stockAtody s'il existe)
    lotModel.getTotalOeufLot(lot_source_id, (err, result) => {

      if (err) return res.status(500).json(err);

      const totalOeuf = result[0].total_oeuf || 0;

      // 3️⃣ vérifier si assez d'oeufs
      if (nb_oeuf_utilise > totalOeuf) {
        return res.status(400).json({
          message: "Nombre d'oeufs insuffisant dans ce lot"
        });
      }

      // 4️⃣ calcul oeuf restant après transfert et destructions
      let oeufRestant = totalOeuf - nb_oeuf_utilise - (simba || 0);
      if (oeufRestant < 0) oeufRestant = 0;

      // 5️⃣ créer le nouveau lot à partir des œufs utilisés
      // determine male/female split for the new lot
      let computedLahy = Number(nb_lahy ?? 0);
      let computedVavy = Number(nb_vavy ?? 0);
      // allow percentage-based distribution if provided
      if ((!computedLahy && !computedVavy) && (percent_lahy || percent_vavy)) {
        const pLahy = Number(percent_lahy ?? 0) / 100;
        const pVavy = Number(percent_vavy ?? 0) / 100;
        computedLahy = Math.round(nb_oeuf_utilise * pLahy);
        computedVavy = Math.round(nb_oeuf_utilise * pVavy);
        // adjust rounding to match total
        const diff = nb_oeuf_utilise - (computedLahy + computedVavy);
        if (diff > 0) computedVavy += diff;
        if (diff < 0) computedVavy += diff; // reduce females if over
      }
      // fallback: split evenly
      if (!computedLahy && !computedVavy) {
        computedLahy = Math.floor(nb_oeuf_utilise / 2);
        computedVavy = nb_oeuf_utilise - computedLahy;
      }

      const newLot = {
        nom: nom,
        date_entree: date_entree,
        nb_akoho_initial: nb_oeuf_utilise,
        nb_lahy: computedLahy,
        nb_vavy: computedVavy,
        race_id: race_id, // race héritée du lot source
        age_semaine: 0
      };

      lotModel.createLot(newLot, (err, result) => {

        if (err) return res.status(500).json(err);

// 6️⃣ enregistrer le stock restant (après simba) dans stockAtody
        const stock = {
          lot_id: lot_source_id,
          race_id: race_id,
          nombre: oeufRestant,
          date: date_entree
        };
        lotModel.createStockAtody(stock, (err2) => {
          if (err2) console.error('Erreur enregistrement stockAtody', err2);
          // répondre dans tous les cas
          res.json({
            message: "Nouveau lot créé avec succès",
            lot_cree: {
              id: result.insertId,
              ...newLot
            },
            oeuf_restant_dans_lot_source: oeufRestant
          });
        });

      });

    });

  });

};

// incubation
exports.getIncubations = (req, res) => {
  lotModel.getAllIncubations((err, results) => {
    if (err) return res.status(500).json(err);
    res.json(results);
  });
};

exports.createIncubation = (req, res) => {
  const { lot_source_id, nb_oeufs, date_debut } = req.body;
  if (!lot_source_id || !nb_oeufs) {
    return res.status(400).json({ error: 'lot_source_id et nb_oeufs requis' });
  }

  // get the lot to derive race
  lotModel.getLotById(lot_source_id, (err, lotRows) => {
    if (err) return res.status(500).json(err);
    if (!lotRows.length) return res.status(404).json({ message: 'Lot source introuvable' });

    const race_id = lotRows[0].race_id;
    raceModel.getPrixByRaceId(race_id, (err2, prixRows) => {
      if (err2) return res.status(500).json(err2);
      if (!prixRows.length) return res.status(404).json({ message: 'Prix pour la race introuvable' });

      const nbJrFohy = prixRows[0].nbJr_fohy || 0;
      const start = date_debut ? new Date(date_debut) : new Date();
      const eclosion = new Date(start);
      eclosion.setDate(eclosion.getDate() + nbJrFohy);

      const inc = {
        lot_source: lot_source_id,
        race_id: race_id,
        nb_oeufs: nb_oeufs,
        date_debut: start.toISOString().substring(0, 10),
        date_eclosion_prevue: eclosion.toISOString().substring(0, 10),
        etat: 'incubating'
      };

      lotModel.createIncubation(inc, (err3, result) => {
        if (err3) return res.status(500).json(err3);
        res.status(201).json({ id: result.insertId, ...inc });
      });
    });
  });
};

exports.hatchIncubation = (req, res) => {
  const incubationId = req.params.id;
  const { nb_oeufs_hatched, nb_oeufs_failed, pct_lahy, pct_vavy, nom, date_entree } = req.body;

  if (nb_oeufs_hatched == null || nb_oeufs_failed == null) {
    return res.status(400).json({ error: 'nb_oeufs_hatched et nb_oeufs_failed requis' });
  }

  lotModel.getIncubationById(incubationId, (err, incubations) => {
    if (err) return res.status(500).json(err);
    if (!incubations.length) return res.status(404).json({ message: 'Incubation introuvable' });

    const incubation = incubations[0];
    const today = new Date();
    const due = new Date(incubation.date_eclosion_prevue);
    if (today < due) {
      return res.status(400).json({ message: 'La période d incubation n est pas terminée' });
    }

    // compute male/female distribution
    const totalHatched = Number(nb_oeufs_hatched);
    let lahy = 0;
    let vavy = 0;
    if (pct_lahy != null && pct_vavy != null) {
      const pL = Number(pct_lahy) / 100;
      const pV = Number(pct_vavy) / 100;
      lahy = Math.round(totalHatched * pL);
      vavy = Math.round(totalHatched * pV);
      const diff = totalHatched - (lahy + vavy);
      if (diff > 0) vavy += diff;
      if (diff < 0) vavy += diff;
    } else {
      lahy = Math.floor(totalHatched / 2);
      vavy = totalHatched - lahy;
    }

    // create new lot from hatched eggs
    const newLot = {
      nom: nom || `Lot (eclosion #${incubationId})`,
      date_entree: date_entree || new Date().toISOString().substring(0, 10),
      nb_akoho_initial: totalHatched,
      nb_lahy: lahy,
      nb_vavy: vavy,
      race_id: incubation.race_id,
      age_semaine: 0
    };

    lotModel.createLot(newLot, (err2, result) => {
      if (err2) return res.status(500).json(err2);

      lotModel.updateIncubation(incubationId, {
        etat: 'hatched',
        nb_oeufs_hatched: totalHatched,
        nb_oeufs_failed: Number(nb_oeufs_failed),
        pct_lahy: pct_lahy || 0,
        pct_vavy: pct_vavy || 0
      }, (err3) => {
        if (err3) console.error('Erreur mise à jour incubation', err3);
        res.json({
          message: 'Incubation terminée, nouveau lot créé',
          lot_cree: { id: result.insertId, ...newLot }
        });
      });
    });
  });
};
