package racesim.model;

public class Simulation {
    private static final double DEFAULT_TRACK_LENGTH_M = 400.0;
    private static final double NITRO_BOOST_JERK_KMH_PER_SEC2 = 4.0;
    private static final double NITRO_BOOST_MAX_RATIO = 0.75;

    private final Voiture voiture;
    private double trackLengthM;
    private double vitesseMS;
    private double positionM;
    private boolean accelerating;
    private boolean nitroActive;
    private final double nitroCapacityK;
    private double nitroEnergyK;
    private double nitroPercent;
    private double currentNitroBoostKmHPerSec;
    private boolean finished;

    public Simulation(Voiture voiture, double trackLengthM) {
        this.voiture = voiture;
        this.vitesseMS = 0.0;
        this.positionM = 0.0;
        this.accelerating = false;
        this.nitroActive = false;
        this.nitroCapacityK = voiture.getNitroCapacityK();
        this.nitroEnergyK = nitroCapacityK;
        this.nitroPercent = nitroCapacityK > 0.0 ? 100.0 : 0.0;
        this.currentNitroBoostKmHPerSec = 0.0;
        this.finished = false;
        this.trackLengthM = normalizeTrackLength(trackLengthM);
    }

    public void update(double deltaSeconds) {
        if (finished || deltaSeconds <= 0.0) {
            return;
        }

        if (accelerating) {
            double accelKmHPerSec = voiture.getAccelerationKmHPerSec();
            if (nitroActive && nitroEnergyK > 0.0) {
                double maxNitroBonus = voiture.getNitroCapacityK() * NITRO_BOOST_MAX_RATIO;
                currentNitroBoostKmHPerSec = Math.min(maxNitroBonus,
                        currentNitroBoostKmHPerSec + NITRO_BOOST_JERK_KMH_PER_SEC2 * deltaSeconds);
                accelKmHPerSec += currentNitroBoostKmHPerSec;

                double consumptionKgPerSec = voiture.getNitroConsumptionKPerMinute() / 60.0;
                nitroEnergyK = Math.max(0.0, nitroEnergyK - consumptionKgPerSec * deltaSeconds);
                nitroPercent = nitroCapacityK > 0.0 ? (nitroEnergyK / nitroCapacityK) * 100.0 : 0.0;
                if (nitroEnergyK <= 0.0) {
                    stopNitro();
                }
            }
            double accelMS2 = accelKmHPerSec * 1000.0 / 3600.0;
            vitesseMS += accelMS2 * deltaSeconds;
        }

        double maxMS = voiture.getVitesseMaxKmH() / 3.6;
        if (vitesseMS > maxMS) {
            vitesseMS = maxMS;
        } else if (vitesseMS < 0.0) {
            vitesseMS = 0.0;
        }

        positionM += vitesseMS * deltaSeconds;
        if (positionM >= trackLengthM) {
            positionM = trackLengthM;
            finished = true;
            stopNitro();
        }
    }

    public void reset() {
        vitesseMS = 0.0;
        positionM = 0.0;
        accelerating = false;
        nitroActive = false;
        nitroEnergyK = nitroCapacityK;
        nitroPercent = nitroCapacityK > 0.0 ? 100.0 : 0.0;
        currentNitroBoostKmHPerSec = 0.0;
        finished = false;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public void startNitro() {
        if (finished || nitroCapacityK <= 0.0) {
            return;
        }
        nitroActive = true;
    }

    public void stopNitro() {
        nitroActive = false;
        currentNitroBoostKmHPerSec = 0.0;
        nitroEnergyK = nitroCapacityK;
        nitroPercent = nitroCapacityK > 0.0 ? 100.0 : 0.0;
    }

    public boolean isNitroActive() {
        return nitroActive;
    }

    public double getNitroPercent() {
        return nitroPercent;
    }

    public boolean isAccelerating() {
        return accelerating;
    }

    public boolean isFinished() {
        return finished;
    }

    public double getPositionM() {
        return positionM;
    }

    public double getTrackLengthM() {
        return trackLengthM;
    }

    public void setTrackLengthM(double trackLengthM) {
        this.trackLengthM = normalizeTrackLength(trackLengthM);
    }

    private static double normalizeTrackLength(double trackLengthM) {
        if (trackLengthM <= 0.0) {
            return DEFAULT_TRACK_LENGTH_M;
        }
        return trackLengthM;
    }

    public double getVitesseMS() {
        return vitesseMS;
    }

    public double getVitesseKmH() {
        return vitesseMS * 3.6;
    }

    public double getVitesseMaxKmH() {
        return voiture.getVitesseMaxKmH();
    }

    public Voiture getVoiture() {
        return voiture;
    }
}
