import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.Trip;
import baseClasses.Connexion;
import baseClasses.Calculator;

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
     * @brief Implementation of the CSA algorithm to find the best path between
     *        start
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
        } else if (time == null) {
            System.err.println("Invalid time given: " + time);
            return;
        } else if (Calculator.timeToInt(time) < 0) {
            System.err.println("Invalid time given: " + time);
            return;
        } else if (start == destination) {
            System.err.println("Start and destination are the same: " + start);
            return;
        }

        // for every connexion sorted by decreasing departure time
        Map<String, Integer> shortestPath = new HashMap<>();
        for (String stopId : stopMap.keySet()) {
            shortestPath.put(stopId, Integer.MAX_VALUE); // biggest value for each stop
        }
        shortestPath.put(starting_stop.getStopId(), Calculator.timeToInt(time));
        // connexions.sort((c1, c2) ->
        // c1.getDepartureTime().compareTo(c2.getDepartureTime()));

        Map<String, Connexion> previousConnection = new HashMap<>();
        for (Connexion connexion : connexions) {
            String departureStopId = connexion.getFromId();
            String arrivalStopId = connexion.getToId();

            int departureTime = Calculator.timeToInt(connexion.getDepartureTime());
            int arrivalTime = Calculator.timeToInt(connexion.getArrivalTime());
            if (shortestPath.get(departureStopId) <= departureTime) {
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
            path.add(0, currentConnexion);
            currentStop = currentConnexion.getFromId();
            if (currentStop.equals(starting_stop.getStopId())) {
                break;
            }
        }
        if (!currentStop.equals(starting_stop.getStopId())) {
            System.out.println("No path found from " + start + " to " + destination);
            return;
        }

        // Print the path
        if (path.isEmpty()) {
            System.out.println("No path found from " + start + " to " + destination);
        } else {
            System.out.println("Path from " + start + " to " + destination + ":");
            for (Connexion connexion : path) {
                Route route = routeMap.get(tripMap.get(connexion.getTripId()).getRouteId());
                System.out.println("From " + stopMap.get(connexion.getFromId()).getStopName() +
                        " to " + stopMap.get(connexion.getToId()).getStopName() +
                        " departing at " + connexion.getDepartureTime() +
                        " and arriving at " + connexion.getArrivalTime() + " with " + route.getRouteType() + ", line "
                        + route.getRouteShortName());
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