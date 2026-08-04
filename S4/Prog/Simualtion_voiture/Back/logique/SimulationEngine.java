package Back.logique;

import Back.modele.Voiture;
import Back.modele.Piste;

public class SimulationEngine {
    private final Voiture voiture;
    private final Piste piste;
    private final double theoreticalTimeSec;

    private boolean countdownActive;
    private boolean running;
    private boolean accelerating;
    private boolean finished;
    private int countdownValue;
    private long lastUpdateNanos;
    private double speedKmh;
    private double distanceKm;
    private double realTimeSec;

    public SimulationEngine(Voiture voiture, Piste piste) {
        this.voiture = voiture;
        this.piste = piste;
        this.theoreticalTimeSec = computeTheoreticalTimeSeconds(piste.getLongueur(), voiture);
        resetState();
    }

    private void resetState() {
        countdownActive = false;
        running = false;
        accelerating = false;
        finished = false;
        countdownValue = -5;
        lastUpdateNanos = 0L;
        speedKmh = 0;
        distanceKm = 0;
        realTimeSec = 0;
    }

    public void startCountdown() {
        if (countdownActive || running) {
            return;
        }
        reset();
        countdownActive = true;
        countdownValue = -5;
        // Start physics immediately so the car can move during countdown.
        running = true;
        finished = false;
        lastUpdateNanos = System.nanoTime();
    }

    public void onCountdownTick() {
        if (!countdownActive) {
            return;
        }
        if (countdownValue == 0) {
            countdownActive = false;
            if (!running) {
                beginRace();
            }
        } else {
            countdownValue++;
        }
    }

    public void beginRace() {
        running = true;
        finished = false;
        accelerating = false;
        lastUpdateNanos = System.nanoTime();
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public void onUpdateTick() {
        if (!running) {
            return;
        }

        long now = System.nanoTime();
        double deltaSec = (now - lastUpdateNanos) / 1_000_000_000.0;
        lastUpdateNanos = now;

        if (accelerating) {
            speedKmh = Math.min(voiture.getVitesseMax(), speedKmh + voiture.getAcceleration() * deltaSec);
        }

        distanceKm += (speedKmh * deltaSec) / 3600.0;
        realTimeSec += deltaSec;

        if (distanceKm >= piste.getLongueur()) {
            distanceKm = piste.getLongueur();
            finish();
        }
    }

    public void finish() {
        countdownActive = false;
        running = false;
        finished = true;
        accelerating = false;
    }

    public void stop() {
        if (!running && !countdownActive) {
            return;
        }
        countdownActive = false;
        running = false;
        accelerating = false;
        finished = true;
    }

    public void reset() {
        resetState();
    }

    private double computeTheoreticalTimeSeconds(float distanceKm, Voiture voiture) {
        double distanceMeters = distanceKm * 1000.0;
        double vmaxMps = voiture.getVitesseMax() / 3.6;
        double accelMps2 = voiture.getAcceleration() / 3.6;

        if (distanceMeters <= 0 || vmaxMps <= 0 || accelMps2 <= 0) {
            return 0;
        }

        double timeToVmax = vmaxMps / accelMps2;
        double distanceToVmax = 0.5 * accelMps2 * timeToVmax * timeToVmax;

        if (distanceMeters <= distanceToVmax) {
            return Math.sqrt((2.0 * distanceMeters) / accelMps2);
        }

        return timeToVmax + (distanceMeters - distanceToVmax) / vmaxMps;
    }

    // Getters
    public Voiture getVoiture() {
        return voiture;
    }

    public Piste getPiste() {
        return piste;
    }

    public double getSpeedKmh() {
        return speedKmh;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getRealTimeSec() {
        return realTimeSec;
    }

    public double getTheoreticalTimeSec() {
        return theoreticalTimeSec;
    }

    public boolean isCountdownActive() {
        return countdownActive;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isAccelerating() {
        return accelerating;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getCountdownValue() {
        return countdownValue;
    }
    


    public String getStateText() {
        if (countdownActive) {
            return "Départ dans " + countdownValue;
        } else if (!running && !finished) {
            return "Prêt à lancer";
        } else if (running) {
            return "Course lancée";
        } else if (finished) {
            return "Arrivée";
        }
        return "Arrêt manuel";
    }
}
