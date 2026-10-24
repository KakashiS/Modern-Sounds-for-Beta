package jockeyphilosopher.extended_sounds;

import java.util.ArrayDeque;

public class FurnaceSoundLimiter {

    private static final int MAX_SOUNDS = 10;

    private static final long WINDOW_MS = 2000L;

    private static final double MAX_DISTANCE = 16.0D;

    private static final ArrayDeque<Long> recent = new ArrayDeque<>();

    private static boolean hasListener = false;
    private static double listenerX;
    private static double listenerY;
    private static double listenerZ;

    public static synchronized void setListener(double x, double y, double z) {
        listenerX = x;
        listenerY = y;
        listenerZ = z;
        hasListener = true;
    }

    public static synchronized boolean isAudible(double x, double y, double z) {
        if (!hasListener) return true;
        double dx = x - listenerX;
        double dy = y - listenerY;
        double dz = z - listenerZ;
        return dx * dx + dy * dy + dz * dz <= MAX_DISTANCE * MAX_DISTANCE;
    }

    public static synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        while (!recent.isEmpty() && now - recent.peekFirst() > WINDOW_MS) {
            recent.pollFirst();
        }

        if (recent.size() >= MAX_SOUNDS) {
            return false;
        }

        recent.addLast(now);
        return true;
    }
}