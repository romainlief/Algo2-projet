import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.Trip;
import baseClasses.Connexion;

public class PathFinder {
    private Map<String, Stop> stopMap;
    private Map<String, Trip> tripMap;
    private Map<String, Route> routeMap;
    private List<Connexion> connexions;

    public PathFinder(Map<String, Stop> stopMap, Map<String, Trip> tripMap, Map<String, Route> routeMap,
            List<Connexion> connexions) {
        this.stopMap = stopMap;
        this.tripMap = tripMap;
        this.routeMap = routeMap;
        this.connexions = connexions;
    }

    /**
     * @brief Converts a time string in the format "HH:MM" to an integer
     *        representing the total minutes.
     * @param time The time string to convert.
     * @return The integer representation of the time in minutes.
     */
    private int timeToInt(String time) {
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
     * @brief Implementation of the A* algorithm to find the best path between start
     *        and end positions.
     * @param start       The starting position.
     * @param destination The ending position.
     * @param time        The time at which the journey starts
     */
    public void findPath(String start, String destination, String time) {
        Stop starting_stop = findStop(start);
        Stop end_stop = findStop(destination);

        if (starting_stop == null) {
            System.err.println("Invalid start given in input: " + start);
            return;
        } else if (end_stop == null) {
            System.err.println("Invalid destination given: " + destination);
            return;
        }

        // for every connexion sorted by decreasing departure time
        Map<String, Integer> shortestPath = new HashMap<>();
        for (String stopId : stopMap.keySet()) {
            shortestPath.put(stopId, Integer.MAX_VALUE); // biggest value for each stop
        }
        shortestPath.put(starting_stop.getStopId(), timeToInt(time));
        connexions.sort((c1, c2) -> c1.getDepartureTime().compareTo(c2.getDepartureTime()));

        Map<String, Connexion> previousConnection = new HashMap<>();
        for (Connexion connexion : connexions) {
            String departureStopId = connexion.getFromId();
            String arrivalStopId = connexion.getToId();

            int departureTime = connexion.timeToInt(connexion.getDepartureTime());
            int arrivalTime = connexion.timeToInt(connexion.getArrivalTime());

            if (shortestPath.get(departureStopId) <= departureTime) {
                System.out.println("ici");
                if (shortestPath.get(arrivalStopId) > arrivalTime) {
                    shortestPath.put(arrivalStopId, arrivalTime);
                    previousConnection.put(arrivalStopId, connexion);
                }
            }
        }

        List<Connexion> path = new ArrayList<>();
        String currentStop = end_stop.getStopId();
        while (previousConnection.containsKey(currentStop)) {
            Connexion currentConnexion = previousConnection.get(currentStop);
            path.add(0, currentConnexion); // Add to the beginning of the path
            currentStop = currentConnexion.getFromId();
        }

        // Print the path
        if (path.isEmpty()) {
            System.out.println("No path found from " + start + " to " + destination);
        } else {
            System.out.println("Path from " + start + " to " + destination + ":");
            for (Connexion connexion : path) {
                System.out.println("From " + stopMap.get(connexion.getFromId()).getStopName() +
                        " to " + stopMap.get(connexion.getToId()).getStopName() +
                        " departing at " + connexion.getDepartureTime() +
                        " and arriving at " + connexion.getArrivalTime());
            }
        }

    }

    /**
     * @brief Finds the Stop based on its name in stopMap map.
     * @param stopName The name of the stop to find.
     * @return The Stop.
     */
    private Stop findStop(String stopName) {
        for (Stop stop : stopMap.values()) {
            if (stop.getStopName().equals(stopName)) {
                return stop;
            }
        }
        return null;
    }
}