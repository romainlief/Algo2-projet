import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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

        // for every connexion sorted by decreasing departure time


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

    // on prend tous les trips_ids qui partent de l'arrêt actuel et on récupère les objets trips correspondants
    
    private void getConnexions(Stop stop) {
        // get all the corresponding trips
        List<String> trip_ids = stop.getTripIds();
        List<Trip> corresponding_trips = new ArrayList<>();
        for (String trip_id : trip_ids) {
            if (tripMap.containsKey(trip_id)) {
                corresponding_trips.add(tripMap.get(trip_id));
            }
        }


    }
}