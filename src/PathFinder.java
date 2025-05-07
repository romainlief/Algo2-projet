import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.Trip;
import baseClasses.Connexion;

public class PathFinder {
    private Map<String, Stop> stopMap;
    private Map<String, Trip> tripMap;
    private Map<String, Route> routeMap;
    private List<Connexion> connexions;

    public PathFinder(Map<String, Stop> stopMap, Map<String, Trip> tripMap, Map<String, Route> routeMap) {
        this.stopMap = stopMap;
        this.tripMap = tripMap;
        this.routeMap = routeMap;
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