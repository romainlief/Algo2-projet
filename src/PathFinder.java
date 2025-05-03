import java.util.Map;
import java.util.PriorityQueue;

import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.Trip;

public class PathFinder {
    private Map<String, Stop> stopMap;
    private Map<String, Trip> tripMap;
    private Map<String, Route> routeMap;

    public PathFinder(Map<String, Stop> stopMap, Map<String, Trip> tripMap, Map<String, Route> routeMap) {
        this.stopMap = stopMap;
        this.tripMap = tripMap;
        this.routeMap = routeMap;
    }

    /**
     * @brief Implementation of the A* algorithm to find the best path between start
     *        and end positions.
     * @param start The starting position.
     * @param destination   The ending position.
     * @param time  The time at which the journey starts
     */
    public void findPath(String start, String destination, String time) {
        Stop starting_stop = findStop(start);
        Stop end_stop = findStop(destination);

        if (starting_stop == null) {
            System.err.println("Invalid stop given in input");
            return;
        }
        else if (end_stop == null) {
            System.err.println("Invalid destination given");
            return;
        }
        // add a time verification




        AStarNode startNode = new AStarNode(starting_stop, null, 0, 0);

        // all nodes that we are currently considering
        PriorityQueue openSet = new PriorityQueue<>();

        if (startNode.getStop() == end_stop) {
            // manage end case
            return;
        }

    }

    // TODO: pass it as a parameter in the constructor for a more modular experience
    // we could use a class that has multiple methods to calculate the heuristic
    // @romain wdyt ??
    /**
     * @brief Calculates the heuristic value based on its parameters.
     * 
     */
    private void heuristic() {

    }

    /**
     * @brief Finds the Stop based on its name in stopMap map.
     * @param stopName The name of the stop to find.
     * @return The Stop.
     */
    private Stop findStop(String stopName) {
        for (Stop stop : stopMap.values()) {
            if (stop.getStopName() == stopName) {
                return stop;
            }
        }
        return null;
    }
}