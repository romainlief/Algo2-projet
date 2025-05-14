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
        Parser parser = new Parser("GTFS"); // Path to GTFS data

        Instant start_time = Instant.now();
        parser.readFiles();
        Instant end_time = Instant.now();
        Duration file_read_time = Duration.between(start_time, end_time);

        System.out.println("[INFO] Parsing done in " + file_read_time.toMillis() + " ms.");
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
            System.out.print("Would you like to search for another itinerary? (no to exit): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("no")) {
                System.out.println("Exiting the program!");
                break;
            }
        }
        scanner.close();
    }
}