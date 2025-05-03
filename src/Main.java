import java.util.Scanner;
import java.util.Map;
import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.Trip;

import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try {
            // initialisation phase
            System.out.println("[INFO] Parsing data...");

            Parser parser = new Parser("GTFS");

            Instant start_time = Instant.now();

            parser.readFiles();

            Instant end_time = Instant.now();

            Duration file_read_time = Duration.between(start_time, end_time);

            System.out.println("[INFO] CSV files read in " + file_read_time.getSeconds() + " seconds.");

            // key -> trip_id
            // value -> Trip object (or only route_id ???)
            Map<String, Trip> allTrips = Parser.getAllTrips();

            // key -> route_id
            // value -> Route object
            Map<String, Route> allRoutes = Parser.getAllRoutes();

            // key -> stop_id
            // value -> Stop object
            Map<String, Stop> allStops = Parser.getAllStops();

            System.out.println("[INFO] AllTrips map has " + allTrips.size() + " elements");

            System.out.println("[INFO] AllRoutes map has " + allRoutes.size() + " elements");

            System.out.println("[INFO] AllStops map has " + allStops.size() + " elements");

            // DGraph graph = new DGraph();

            // System.out.println("Graph created successfully!");
            // System.out.println(graph);

            // algorithm phase
            Scanner scanner = new Scanner(System.in);
            
            // Getting departure spot
            System.out.println("From where would you like to travel ?: ");
            String start = scanner.nextLine();

            // Getting destination
            System.out.println("Where would you like to go ?: ");
            String destination = scanner.nextLine();

            // Getting departure_time
            System.out.println("What time do you wish to depart ?: ");
            String departure_time = scanner.nextLine();

            // Searching for best path
            PathFinder finder = new PathFinder(allStops, allTrips, allRoutes);
            finder.findPath(null, null, null);

            scanner.close();
        } catch (

        Exception e) {
            System.err.println("Error loading GTFS data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}