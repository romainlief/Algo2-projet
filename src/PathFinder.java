import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.Trip;
import baseClasses.Walk;
import baseClasses.Connexion;
import baseClasses.Calculator;

public class PathFinder {
    private Map<String, Stop> stopMap;
    private Map<String, Trip> tripMap;
    private Map<String, Route> routeMap;
    private List<Connexion> connexions;
    // private

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
        List<Stop> startingStops = findStopsByName(start);
        if (startingStops.isEmpty()) {
            System.err.println("No stops found with the name: " + start);
            return;
        }
        List<Stop> endStops = findStopsByName(destination);
        if (endStops.isEmpty()) {
            System.err.println("No stops found with the name: " + destination);
            return;
        }
        if (time == null) {
            System.err.println("Invalid time given: " + time);
            return;
        } else if (Calculator.timeToInt(time) < 0) {
            System.err.println("Invalid time given: " + time);
            return;
        } else if (start == destination) {
            System.err.println("Start and destination are the same: " + start);
            return;
        }

        int userStartTime = Calculator.timeToInt(time);

        // for every connexion sorted by decreasing departure time
        Map<String, Integer> shortestPath = new HashMap<>();

        for (String stopId : stopMap.keySet()) {
            shortestPath.put(stopId, Integer.MAX_VALUE); // biggest value for each stop
        }
        for (Stop startingStop : startingStops) {
            shortestPath.put(startingStop.getStopId(), Calculator.timeToInt(time));
        }

        Map<String, Connexion> previousConnection = new HashMap<>();

        int startIndex = findStartIndex(connexions, userStartTime);
        for (int i = startIndex; i < connexions.size(); i++) {
            Connexion connexion = connexions.get(i);
            String departureStopId = connexion.getFromId();
            String arrivalStopId = connexion.getToId();

            int departureTime = Calculator.timeToInt(connexion.getDepartureTime());
            int arrivalTime = Calculator.timeToInt(connexion.getArrivalTime());

            if (shortestPath.get(departureStopId) <= departureTime && shortestPath.get(arrivalStopId) > arrivalTime) {
                shortestPath.put(arrivalStopId, arrivalTime);
                previousConnection.put(arrivalStopId, connexion);

                Stop arrivalStop = stopMap.get(arrivalStopId);
                if (arrivalStop != null && arrivalStop.getWalk() != null) {
                    for (Walk walk : arrivalStop.getWalk()) {
                        Stop walkDest = walk.getDestination();
                        String walkDestId = walkDest.getStopId();
                        int walkDuration = Calculator.timeToInt(walk.getDuration());
                        int walkArrivalTime = arrivalTime + walkDuration; // Utiliser arrivalTime ici

                        // Vérifiez si le temps d'arrivée via la marche est meilleur
                        if (shortestPath.get(walkDestId) > walkArrivalTime) {
                            shortestPath.put(walkDestId, walkArrivalTime);
                            // Ajoutez une référence spéciale pour indiquer qu'il s'agit d'une marche
                            previousConnection.put(walkDestId, connexion); 
                        }
                    }
                }
            }
        }

        Stop bestEndStop = null;
        int minArrivalTime = Integer.MAX_VALUE;
        for (Stop endStop : endStops) {
            int arrivalTime = shortestPath.get(endStop.getStopId());
            if (arrivalTime < minArrivalTime) {
                minArrivalTime = arrivalTime;
                bestEndStop = endStop;
            }
        }
        if (bestEndStop == null) {
            System.out.println("No path found from " + start + " to " + destination);
            return;
        }

        List<Connexion> path = new ArrayList<>();

        Stop currentStop = bestEndStop;

        while (currentStop != null && previousConnection.containsKey(currentStop.getStopId())) {
            Connexion connexion = previousConnection.get(currentStop.getStopId());
            path.add(0, connexion); // Ajouter au début pour reconstruire dans l'ordre
            currentStop = stopMap.get(connexion.getFromId());
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
     * @brief Finds all stops with the given name.
     * @param stopName The name of the stop to find.
     * @return A list of stops with the given name.
     */
    private List<Stop> findStopsByName(String stopName) {
        List<Stop> stops = new ArrayList<>();
        for (Stop stop : stopMap.values()) {
            if (stop.getStopName().equalsIgnoreCase(stopName)) {
                stops.add(stop);
            }
        }
        return stops;
    }

    /**
     * @brief Find the index of the first connexion that departs after the given
     *        time.
     * @param connexions    The list of connexions to search.
     * @param userStartTime The time to search for.
     * @return The index of the first connexion that departs after the given time.
     */
    private int findStartIndex(List<Connexion> connexions, int userStartTime) {
        int left = 0, right = connexions.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (Calculator.timeToInt(connexions.get(mid).getDepartureTime()) >= userStartTime) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}