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
            parse();
            solve();
        } catch (Exception e) {
            System.err.println("Error loading GTFS data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void parse() {
        System.out.println("[INFO] Parsing data...");
        Parser parser = new Parser("GTFS");

        Instant start_time = Instant.now();
        parser.readFiles();
        Instant end_time = Instant.now();
        Duration file_read_time = Duration.between(start_time, end_time);

        System.out.println("[INFO] CSV files read in " + file_read_time.getSeconds() + " seconds.");

        // key -> trip_id
        // value -> Trip object
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
    }

    private static void solve() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Getting departure spot
            System.out.print("From where would you like to travel ?: ");
            String start = scanner.nextLine();

            // Getting destination
            System.out.print("Where would you like to go ?: ");
            String destination = scanner.nextLine();

            // Getting departure_time
            System.out.print("What time do you wish to depart (format: 'hour;minute;second') ?: ");
            String departure_time = scanner.nextLine();

            // Searching for best path
            PathFinder finder = new PathFinder(Parser.getAllStops(), Parser.getAllTrips(), Parser.getAllRoutes(),
                    Parser.getAllConnexions());

            Instant start_time = Instant.now();
            finder.findPath(start, destination, departure_time);
            Instant end_time = Instant.now();
            Duration pathfinding_time = Duration.between(start_time, end_time);

            System.out.println("[INFO] Pathfinding took " + pathfinding_time.getSeconds() + " seconds.");

            // Ask if the user wants to search for another itinerary
            System.out.print("Would you like to search for another itinerary? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("no")) {
                System.out.println("Exiting the program!");
                break;
            }
        }

        scanner.close();
    }
}