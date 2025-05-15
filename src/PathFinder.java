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
        if (!isValidTimeFormat(time)) {
            System.err.println("Invalid time format: " + time);
            return;
        }
        int userStartTime = Calculator.timeToInt(time);
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
        } else if (userStartTime < 0) {
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
        for (Stop startingStop : startingStops) {
            shortestPath.put(startingStop.getStopId(), userStartTime);
        }

        Map<String, Connexion> previousConnection = new HashMap<>();

        int startIndex = BinarySearch.findStartIndex(connexions, userStartTime);
        for (int i = startIndex; i < connexions.size(); i++) {
            Connexion connexion = connexions.get(i);
            String departureStopId = connexion.getFromId();
            String arrivalStopId = connexion.getToId();

            int departureTime = connexion.getDepartureTime();
            int arrivalTime = connexion.getArrivalTime();

            int currentDepartureTime = shortestPath.get(departureStopId);
            int currentArrivalTime = shortestPath.get(arrivalStopId);

            if (currentDepartureTime <= departureTime && currentArrivalTime > arrivalTime) {
                shortestPath.put(arrivalStopId, arrivalTime);
                previousConnection.put(arrivalStopId, connexion);

                Stop arrivalStop = stopMap.get(arrivalStopId);
                if (arrivalStop != null && arrivalStop.getWalk() != null) {
                    for (Walk walk : arrivalStop.getWalk()) {
                        String walkDestId = walk.getDestination().getStopId();
                        int walkArrivalTime = arrivalTime + walk.getDuration();
                        int currentWalkArrivalTime = shortestPath.get(walkDestId);
                        if (currentWalkArrivalTime > walkArrivalTime) {
                            shortestPath.put(walkDestId, walkArrivalTime);
                            previousConnection.put(walkDestId, new Connexion(
                                    null,
                                    arrivalStopId,
                                    walkDestId,
                                    arrivalTime,
                                    walkArrivalTime));
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

        List<Connexion> path = new ArrayList<>();

        Stop currentStop = bestEndStop;

        while (currentStop != null && previousConnection.containsKey(currentStop.getStopId())) {
            Connexion connexion = previousConnection.get(currentStop.getStopId());
            path.add(0, connexion); // Add to the beginning of the path
            currentStop = stopMap.get(connexion.getFromId());
        }

        // Print the path
        if (path.isEmpty() || bestEndStop == null) {
            System.out.println("No path found from " + start + " to " + destination);
        } else {
            System.out.println("Path from " + start + " to " + destination + ":");

            Connexion currentTransportStart = null;
            Connexion lastConnexionOfCurrentTrip = null;

            for (int i = 0; i <= path.size(); i++) {
                Connexion current = (i < path.size()) ? path.get(i) : null;

                if (current != null && current.getTripId() != null) {
                    if (currentTransportStart == null) {
                        // Début d'un nouveau trajet en transport
                        currentTransportStart = current;
                    }
                    // Mettre à jour le dernier arrêt connu de ce trip
                    lastConnexionOfCurrentTrip = current;
                } else {
                    // Si on change de mode (marche ou fin)
                    if (currentTransportStart != null && lastConnexionOfCurrentTrip != null) {
                        printTransport(currentTransportStart, lastConnexionOfCurrentTrip);
                        currentTransportStart = null;
                        lastConnexionOfCurrentTrip = null;
                    }

                    if (current != null && current.getTripId() == null) {
                        // Marchez uniquement si ce n'est pas la dernière connexion
                        String toName = stopMap.get(current.getToId()).getStopName();
                        if (!toName.equalsIgnoreCase(destination)) {
                            printWalk(current);
                        }
                    }
                }
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
        if (stopName == null)
            return stops;

        String targetName = stopName.toLowerCase();
        for (Stop stop : stopMap.values()) {
            String currentName = stop.getStopName();
            if (currentName != null && currentName.toLowerCase().equals(targetName)) {
                stops.add(stop);
            }
        }
        return stops;
    }

    /**
     * @brief Prints the transport information.
     * @param start The starting connexion.
     * @param end   The ending connexion.
     */
    private void printTransport(Connexion start, Connexion end) {
        if (start == null || end == null || start.getTripId() == null)
            return;

        String fromName = stopMap.get(start.getFromId()).getStopName();
        String toName = stopMap.get(end.getToId()).getStopName();
        Route route = routeMap.get(tripMap.get(start.getTripId()).getRouteId());

        String departureTimeStr = Calculator.intToTime(start.getDepartureTime());
        String arrivalTimeStr = Calculator.intToTime(end.getArrivalTime());

        System.out.println("Take " + route.getRouteType() + " " + route.getRouteShortName() +
                " from " + fromName + " (" + departureTimeStr + ")" +
                " to " + toName + " (" + arrivalTimeStr + ")");
    }

    /**
     * @brief Prints the walk information.
     * @param walk The walk connexion to print.
     */
    private void printWalk(Connexion walk) {
        String fromName = stopMap.get(walk.getFromId()).getStopName();
        String toName = stopMap.get(walk.getToId()).getStopName();

        String departureTimeStr = Calculator.intToTime(walk.getDepartureTime());
        String arrivalTimeStr = Calculator.intToTime(walk.getArrivalTime());

        System.out.println("Walk from " + fromName + " (" + departureTimeStr + ")" +
                " to " + toName + " (" + arrivalTimeStr + ")");
    }

    /**
     * @brief Validates the time format.
     * @param time The time string to validate.
     * @return true if the time format is valid, false otherwise.
     */
    private boolean isValidTimeFormat(String time) {
        return time != null && time.matches("^\\d{2};\\d{2}(;\\d{2})?$");
    }
}