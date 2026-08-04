package racesim.util;

public class TimeFormat {
    public static String formatMillis(long ms) {
        boolean negative = ms < 0;
        long abs = Math.abs(ms);
        long minutes = abs / 60000;
        long seconds = (abs / 1000) % 60;
        long millis = abs % 1000;
        String formatted = String.format("%02d:%02d.%03d", minutes, seconds, millis);
        return negative ? "-" + formatted : formatted;
    }
}
