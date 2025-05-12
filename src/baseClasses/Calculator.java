package baseClasses;

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
        double delta_lat = Math.toRadians(stopB.getStopLat() - stopA.getStopLat());
        double delta_lon = Math.toRadians(stopB.getStopLon() - stopA.getStopLon());

        double latA = Math.toRadians(stopA.getStopLat());
        double latB = Math.toRadians(stopB.getStopLat());

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

    /**
     * @brief Converts a time from double format to String format "HH:MM:SS".
     * @param seconds The time in seconds to convert.
     * @return Converted time in String "HH:MM:SS" format.
     */
    public static String timeToString(double seconds) {
        int totalSeconds = (int) Math.round(seconds);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int secs = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
