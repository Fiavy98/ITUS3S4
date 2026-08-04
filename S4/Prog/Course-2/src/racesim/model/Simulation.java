package racesim.model;

public class Simulation {
    private static final double DEFAULT_TRACK_LENGTH_M = 400.0;

    private final Voiture voiture;
    private double trackLengthM;
    private double vitesseMS;
    private double positionM;
    private boolean accelerating;
    private boolean finished;

    public Simulation(Voiture voiture, double trackLengthM) {
        this.voiture = voiture;
        this.vitesseMS = 0.0;
        this.positionM = 0.0;
        this.accelerating = false;
        this.finished = false;
        this.trackLengthM = normalizeTrackLength(trackLengthM);
    }

    public void update(double deltaSeconds) {
        if (finished || deltaSeconds <= 0.0) {
            return;
        }

        if (accelerating) {
            double accelMS2 = voiture.getAccelerationKmHPerSec() * 1000.0 / 3600.0;
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
        }
    }

    public void reset() {
        vitesseMS = 0.0;
        positionM = 0.0;
        accelerating = false;
        finished = false;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
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
