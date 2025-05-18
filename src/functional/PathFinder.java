package functional;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import objects.Route;
import objects.Stop;
import objects.Trip;
import objects.Walk;
import objects.Connexion;

public class PathFinder {
    // #### Attributes ####
    private Map<String, Stop> stopMap;
    private Map<String, Trip> tripMap;
    private Map<String, Route> routeMap;
    private List<Connexion> connexions;

    // flags
    private boolean bus = true;
    private boolean train = true;
    private boolean tram = true;
    private boolean metro = false;

    // #### Constructors ####
    /**
     * @brief Constructor for the PathFinder class.
     * @param stopMap    The map of stops.
     * @param tripMap    The map of trips.
     * @param routeMap   The map of routes.
     * @param connexions The list of connexions.
     */
    public PathFinder(Map<String, Stop> stopMap, Map<String, Trip> tripMap, Map<String, Route> routeMap,
            List<Connexion> connexions) {
        this.stopMap = stopMap;
        this.tripMap = tripMap;
        this.routeMap = routeMap;
        this.connexions = connexions;
    }

    // #### Methods ####
    /**
     * @brief Implementation of the CSA algorithm to find the best path between
     *        start
     *        and end positions with the starting and stopping criterion.
     * @param start       The starting position.
     * @param destination The ending position.
     * @param time        The time at which the journey starts
     */
    public void findPath(String start, String destination, String time, boolean variant) {
        // #######################################################################################
        // Check the validity of the input parameters
        // #######################################################################################
        if (!isValidTimeFormat(time)) {
            System.err.println("Invalid time format: " + time);
            return;
        }
        int userStartTime = Calculator.timeToInt(time); // Convert time to int
        List<Stop> startingStops = findStopsByName(start); // Find all stops with the given name
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
        // #######################################################################################

        // #######################################################################################
        // Initialize the shortest path and previous connection maps
        // #######################################################################################
        Map<String, Integer> shortestPath = new HashMap<>(); // for every connexion sorted by decreasing departure time

        for (String stopId : stopMap.keySet()) { // for every stop put in the map
            shortestPath.put(stopId, Integer.MAX_VALUE); // the biggest value for each stop
        }
        for (Stop startingStop : startingStops) { // for every starting stop put in the map
            shortestPath.put(startingStop.getStopId(), userStartTime); // set the departure time for each starting stop
        }

        Map<String, Connexion> previousConnection = new HashMap<>(); // map to store the previous connection for each
                                                                     // stop
        // ########################################################################################

        // ########################################################################################
        // Init the best arrival time to the biggest value possible and chose the
        // startIndex
        // ########################################################################################
        int bestArrivalTime = Integer.MAX_VALUE;

        // This is the "Starting criterion" optimisation
        // ----------------------------------------------------------------------------------------
        // With a binary search we can find the first connexion that has a departure
        // time >= the userStartTime
        // That optimisation is called "Starting criterion"
        // In the algorithm we will only consider connexions that have a departure
        // time >= the userStartTime.
        // ------------------------------------------------------------------------------------------
        // Complexity:
        // So that means that the complexity of the
        // algorithm is not O(n) like the basic CSA but is O(n - k) where k is the
        // number of connexions that have a departure time < the userStartTime.
        // In the worst case we have to process all the connexions without the Starting
        // criterion, and have a complexity of O(n) or O(n - m) if there is the Stopping
        // criterion (see line 127).
        int startIndex = BinarySearch.findStartIndex(connexions, userStartTime);
        // ########################################################################################

        // ########################################################################################
        // Main loop of the algorithm
        // Runs all the connexions sorted by increasing departure time
        // We sort the connexions by increasing departure time in the Builder class
        // ########################################################################################
        for (int i = startIndex; i < connexions.size(); i++) {
            Connexion connexion = connexions.get(i);
            if (variant) {
                if (!checkFlags(connexion)) {
                    continue; // skip the connexion if not demanded by the user
                }
            }
            // ####################################################################################
            // If the departure time of the connexion is greater than the best arrival
            // time then we can stop the algorithm, we have found the best path
            // ####################################################################################
            // This is the "Stopping criterion" optimisation
            // --------------------------------------------------------------------------------------
            // This optimization allows us to terminate the algorithm early when further
            // processing of connections cannot improve the result. Specifically, we stop
            // the algorithm as soon as the departure time of the current connection (cdep)
            // exceeds the best arrival time found so far (bestArrivalTime).
            // This works because the connections are processed in order of increasing
            // departure time. If the departure time of the current connection is already
            // greater than the best arrival time found so far, then all subsequent
            // connections will also have a departure time greater than the best arrival
            // time.
            // Therefore, we can safely stop the algorithm at this point without missing
            // any potential improvements to the result.
            // --------------------------------------------------------------------------------------
            // Complexity:
            // So that means that the complexity of the
            // algorithm is not O(n) like the basic CSA but is O(n - m) where m
            // is the number of connexions that have a departure time < the best arrival
            // time
            // If we add the starting criterion with the stopping criterion we have:
            // Complexity: O(n - k - m) where k is the number of connexions that have a
            // departure time < the userStartTime and m is the number of connexions that
            // have
            // a departure time < the best arrival time
            // So the complexity of the algorithm is O(n - k - m) where n is the number of
            // connexions.
            // In the worst case we have to process all the connexions without the Stopping
            // criterion
            // and have a complexity of O(n - k).
            if (connexion.getDepartureTime() > bestArrivalTime) {
                break;
            }
            // ####################################################################################

            String departureStopId = connexion.getFromId();
            String arrivalStopId = connexion.getToId();
            int departureTime = connexion.getDepartureTime();
            int arrivalTime = connexion.getArrivalTime();
            int currentDepartureTime = shortestPath.get(departureStopId);
            int currentArrivalTime = shortestPath.get(arrivalStopId);

            // ####################################################################################
            // Check if the connection improves the shortest path
            // ####################################################################################
            if (currentDepartureTime <= departureTime && currentArrivalTime > arrivalTime) {
                // Update the shortest path and previous connection maps
                shortestPath.put(arrivalStopId, arrivalTime);
                previousConnection.put(arrivalStopId, connexion);
                for (Stop endStop : endStops) { // Update bestArrivalTime if this arrivalStop is one of the destination
                                                // stops.
                                                // Part of the Stopping criterion
                    if (arrivalStopId.equals(endStop.getStopId()) && arrivalTime < bestArrivalTime) {
                        bestArrivalTime = arrivalTime;
                    }
                }
                // ################################################################################
                // Check if there are any walks from the arrival stop to other stops
                // --------------------------------------------------------------------------------
                // Complexity:
                // O(n) where n is the number of walks from the arrival stop
                // ################################################################################
                Stop arrivalStop = stopMap.get(arrivalStopId);
                if (arrivalStop != null && arrivalStop.getWalk() != null) {
                    for (Walk walk : arrivalStop.getWalk()) { // Run through all the walks from the arrival
                                                              // stop
                        String walkDestId = walk.getDestination().getStopId();
                        int walkArrivalTime = arrivalTime + walk.getDuration();
                        int currentWalkArrivalTime = shortestPath.get(walkDestId);
                        // Check if the walk improves the shortest path
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
                // ################################################################################
            }
        }
        // ########################################################################################
        // Find the best end stop in the list of end stops with the same name
        // The best end stop is the one with the minimum arrival time
        // ########################################################################################
        Stop bestEndStop = null;
        int minArrivalTime = Integer.MAX_VALUE;
        for (Stop endStop : endStops) {
            int arrivalTime = shortestPath.get(endStop.getStopId());
            if (arrivalTime < minArrivalTime) {
                minArrivalTime = arrivalTime;
                bestEndStop = endStop;
            }
        }
        // ########################################################################################
        // ########################################################################################

        // ########################################################################################
        // Build the path from the best end stop to the starting stops
        // ########################################################################################
        List<Connexion> path = new ArrayList<>();
        Stop currentStop = bestEndStop;
        // Remake the path from the best end stop to the starting stops by following the
        // previous connection
        while (currentStop != null && previousConnection.containsKey(currentStop.getStopId())) {
            Connexion connexion = previousConnection.get(currentStop.getStopId());
            path.add(0, connexion); // Add to the beginning of the path
            currentStop = stopMap.get(connexion.getFromId()); // Get the previous stop
        }

        // ########################################################################################
        // Print the path
        // ########################################################################################
        if (path.isEmpty() || bestEndStop == null) {
            System.out.println("No path found from " + start + " to " + destination);
        } else {
            System.out.println("Path from " + start + " to " + destination + ":");
            for (Connexion connexion : path) {
                if (connexion.getTripId() != null) {
                    printTransport(connexion, previousConnection.get(connexion.getToId()));
                } else {
                    printWalk(connexion);
                }
            }
        }
        // ########################################################################################
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
        if (walk.getFromId().equals(walk.getToId()) || walk.getDepartureTime() == walk.getArrivalTime()) {
            return; // Ne rien imprimer si le walk est invalide
        }

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

    /**
     * @brief Checks the flags for the given connexion.
     * @param connexion The connexion to check.
     * @return true if the flags are valid, false otherwise.
     */
    private boolean checkFlags(Connexion connexion) {
        // Si c’est une marche (pas de tripId), on l'autorise toujours
        if (connexion.getTripId() == null) {
            return true;
        }

        String routeType = getRouteType(connexion);
        if (routeType == null) {
            return false; // On exclut les connexions avec type inconnu
        }
        switch (routeType.toLowerCase()) {
            case "bus":
                return bus;
            case "train":
                return train;
            case "tram":
                return tram;
            case "metro":
                return metro;
            default:
                return false; // On exclut les types inconnus
        }
    }

    /**
     * @brief Gets the route type for a given connexion.
     * @param connexion The connexion to get the route type for.
     * @return The route type as a string.
     */
    private String getRouteType(Connexion connexion) {
        if (connexion.getTripId() == null) {
            return null;
        }
        String routeId = tripMap.get(connexion.getTripId()).getRouteId();
        return routeMap.get(routeId).getRouteType();
    }

    /**
     * @brief Sets the bus flag.
     * @param bus The bus flag to set.
     */
    public void setBus(boolean bus) {
        this.bus = bus;
    }

    /**
     * @brief Sets the train flag.
     * @param train The train flag to set.
     */
    public void setTrain(boolean train) {
        this.train = train;
    }

    /**
     * @brief Sets the tram flag.
     * @param tram The tram flag to set.
     */
    public void setTram(boolean tram) {
        this.tram = tram;
    }

    /**
     * @brief Sets the metro flag.
     * @param metro The metro flag to set.
     */
    public void setMetro(boolean metro) {
        this.metro = metro;
    }
}