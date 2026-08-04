package racesim.model;

public class Chrono {
    private final long baseOffsetMs;
    private long startTimeMs;
    private long elapsedMs;
    private boolean running;

    public Chrono(long baseOffsetMs) {
        this.baseOffsetMs = baseOffsetMs;
        this.elapsedMs = baseOffsetMs;
        this.running = false;
    }

    public void start() {
        if (running) {
            return;
        }
        startTimeMs = System.currentTimeMillis();
        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        elapsedMs = getElapsedMs();
        running = false;
    }

    public void reset() {
        elapsedMs = baseOffsetMs;
        running = false;
        startTimeMs = 0L;
    }

    public boolean isRunning() {
        return running;
    }

    public long getElapsedMs() {
        if (!running) {
            return elapsedMs;
        }
        long now = System.currentTimeMillis();
        return elapsedMs + (now - startTimeMs);
    }
}
