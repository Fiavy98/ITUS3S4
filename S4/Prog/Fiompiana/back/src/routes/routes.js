const express = require('express');
const router = express.Router();

const raceController = require('../controllers/raceController');
const lotController = require('../controllers/lotController');

// race
router.get('/race', raceController.getRaces);

// variation poids
router.get('/variationPoids', raceController.getVariationPoids);
// weight gain for a race between two weeks (debut inclusive, fin inclusive)
router.get('/variationPoids/poids', raceController.getWeightGain);

// prix race
router.get('/prixRace', raceController.getPrixRace);

// lot
router.get('/lot', lotController.getLots);

// visite lot
router.get('/visiteLot', lotController.getVisiteLot);

// Route pour situation Lot
router.get('/situationLot', lotController.getSituationLot);

// CRUD routes race
router.post('/race', raceController.createRace);

// CRUD routes variationPoids
router.post('/variationPoids', raceController.createVariation);

// CRUD routes prixRace
router.post('/prixRace', raceController.createPrix);

// CRUD routes lot
router.post('/lot', lotController.createLot);

// CRUD routes visiteLot
router.post('/visiteLot', lotController.createVisiteLot);

//Oeuf atao anaty lot vaovao
router.post('/lot/oeuf', lotController.createLotFromOeuf);

// incubation
router.get('/incubation', lotController.getIncubations);
router.post('/incubation', lotController.createIncubation);
router.post('/incubation/:id/hatch', lotController.hatchIncubation);

module.exports = router;


