import java.util.Scanner;
import functional.PathFinder;
import functional.Initializer;

import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("#############################################################");
            System.out.println("[\033[92mINFO\033[0m] Starting the program...");

            // declaring constants
            final String DIRECTORY = "GTFS"; // Path to GTFS data
            final double MAX_FOOT_DISTANCE = 500; // in meters
            final double AVERAGE_WALKING_SPEED = 1.0; // in m/s
            final int BALL_TREE_LEAFE_SIZE = 30; // number of stops per leaf

            System.out.println("[\033[92mINFO\033[0m] Parameters:");
            System.out.println("[\033[92mINFO\033[0m] Max foot distance: " + MAX_FOOT_DISTANCE + " m");
            System.out.println("[\033[92mINFO\033[0m] Average walking speed: " + AVERAGE_WALKING_SPEED + " m/s");
            System.out.println("[\033[92mINFO\033[0m] Ball tree leaf size: " + BALL_TREE_LEAFE_SIZE);

            System.out.println("#############################################################");
            System.out.println("#-----------------------------------------------------------#");
            System.out.println("#############################################################");
            System.out.println("[\033[92mINFO\033[0m] Initializing the GTFS data...");
            Initializer initializer = new Initializer(DIRECTORY, MAX_FOOT_DISTANCE, AVERAGE_WALKING_SPEED,
                    BALL_TREE_LEAFE_SIZE);
            initializer.setup(true);

            System.out.println("[\033[92mINFO\033[0m] GTFS data loaded successfully!");
            System.out.println("#############################################################");
            System.out.println("#-----------------------------------------------------------#");
            System.out.println("#############################################################");
            System.out.println("[\033[92mINFO\033[0m] PathFinder ready.");
            // finding the best path
            solve(initializer);
        } catch (Exception e) {
            System.err.println("Error loading GTFS data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void solve(Initializer initializer) {
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

            System.out.print("Would you like to use the default mode or the variant mode ? (default/variant): ");
            String mode = scanner.nextLine().trim().toLowerCase();

            if (mode == null || mode.isEmpty()) {
                System.out.println("[\033[91mERROR\033[0m] Invalid mode. Please enter 'default' or 'variant'.");
                continue;
            }
            if (!mode.equals("default") && !mode.equals("variant")) {
                System.out.println("[\033[91mERROR\033[0m] Invalid mode. Please enter 'default' or 'variant'.");
                continue;
            }
            PathFinder finder = new PathFinder(initializer.getStops(), initializer.getTrips(), initializer.getRoutes(),
                    initializer.getConnexions());
            boolean variant = false;
            if (mode.equals("variant")) {
                variant = true;
                System.out.print("Which transportation mode would you like to use ? (bus/train/metro/tram): \n");

                System.out.print("bus: (true/false): ");
                String bus = scanner.nextLine().trim().toLowerCase();
                finder.setTrain(bus.equals("true"));

                System.out.print("train: (true/false): ");
                String train = scanner.nextLine().trim().toLowerCase();
                finder.setTrain(train.equals("true"));

                System.out.print("metro: (true/false): ");
                String metro = scanner.nextLine().trim().toLowerCase();
                finder.setMetro(metro.equals("true"));

                System.out.print("tram: (true/false): ");
                String tram = scanner.nextLine().trim().toLowerCase();
                finder.setTram(tram.equals("true"));

            } else if (mode.equals("default")) {
                variant = false;
                System.out.println("[\033[92mINFO\033[0m] Using default mode.");
            }

            Instant start_time = Instant.now();
            // Searching for best path
            finder.findPath(start, destination, departure_time, variant);
            Instant end_time = Instant.now();
            Duration pathfinding_time = Duration.between(start_time, end_time);

            System.out.println("[\033[92mINFO\033[0m] Pathfinding took " + pathfinding_time.toMillis() + " ms.");

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