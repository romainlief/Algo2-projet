import java.util.Map;
import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.StopTime;
import baseClasses.Trip;

import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("[ERROR] Parsing data...");

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

            DGraph graph = new DGraph();

            System.out.println("Graph created successfully!");
            System.out.println(graph);
        } catch (

        Exception e) {
            System.err.println("Error loading GTFS data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}