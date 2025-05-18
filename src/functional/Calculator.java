package functional;

import objects.Stop;

import java.lang.Math;

public class Calculator {

    /**
     * @brief Calculates the distance to another stop using the haversine distance
     *        formula.
     * 
     * @param other The stop to which the distance is calculated.
     * @return The distance in meters.
     */
    public static double haversine_distance(Stop stopA, Stop stopB) {
        double EARTH_RADIUS = 6371000; // in meters

        // converting values to radians
        double delta_lat = Math.toRadians(stopB.getStopLat() - stopA.getStopLat()); // O(1) -> angdeg / 180.0 * PI
        double delta_lon = Math.toRadians(stopB.getStopLon() - stopA.getStopLon()); // O(1) -> angdeg / 180.0 * PI

        double latA = Math.toRadians(stopA.getStopLat()); // O(1)
        double latB = Math.toRadians(stopB.getStopLat()); // O(1)

        // applying haversine formula
        double a = Math.pow(Math.sin(delta_lat / 2), 2) +
                Math.pow(Math.sin(delta_lon / 2), 2) *
                        Math.cos(latA) *
                        Math.cos(latB);
        double c = 2 * Math.asin(Math.sqrt(a));
        double distance = c * EARTH_RADIUS;

        return distance;
    }

    /**
     * @brief Converts a time string in the format "HH:MM" to an Integer
     *        representing the total seconds.
     * @param time The time string to convert.
     * @return The Integer representation of the time in seconds.
     */
    public static int timeToInt(String time) {
        time = time.replace(";", ":");

        String[] parts = time.split(":");
        if (parts.length < 2 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = (parts.length == 3) ? Integer.parseInt(parts[2]) : 0; // Default to 0 seconds if not provided

        return hours * 3600 + minutes * 60 + seconds;
    }

    public static String intToTime(int seconds) {
        int days = seconds / 86400; 
        int hours = (seconds % 86400) / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        if (days > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        }
    }
}
