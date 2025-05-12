import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import java.time.Duration;
import java.time.Instant;

import baseClasses.*;

public class Parser {
    private final double MAX_FOOT_DISTANCE = 500; // in meters
    private final double AVERAGE_WALKING_SPEED = 1.0; // 1 m/s -> TODO, calculer une moyenne sur plusieurs sources ou
                                                      // simplement tester plusieurs valeurs
    private static String directory;
    private static String[] entreprises = {
            "SNCB",
            "TEC",
            "STIB",
            "DELIJN"
    };

    public static Map<String, Trip> allTrips = new HashMap<>();
    public static Map<String, Stop> allStops = new HashMap<>();
    public static Map<String, Route> allRoutes = new HashMap<>();
    public static List<Connexion> allConnexions = new ArrayList<>();

    /**
     * Constructor for the Parser class.
     */
    public Parser(String directory) {
        Parser.directory = directory;
    }

    /**
     * Reads all GTFS files from the specified directory and loads them into
     * corresponding maps.
     */
    public void readFiles() {

        Instant start_time = Instant.now();
        for (String entreprise : entreprises) {
            String filePath = directory + "/" + entreprise + "/";
            try {
                allTrips.putAll(loadTrips(filePath + "trips.csv"));
                allStops.putAll(loadStops(filePath + "stops.csv"));
                allRoutes.putAll(loadRoutes(filePath + "routes.csv"));
                loadStopTimes(filePath + "stop_times.csv");
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath + "routes.csv");
                e.printStackTrace();
            }
        }
        Instant end_time = Instant.now();
        Duration file_read_time = Duration.between(start_time, end_time);
        System.out.println("[INFO] CSV files read in " + file_read_time.getSeconds() + " seconds.");

        // Instant start_time_connexions = Instant.now();
        // Building connexions
        // for (Trip trip : allTrips.values()) {
            // List<StopTime> orederedStopTimes = trip.getstopTimes(); // a list of ordered stopTimes
            // for (int stop_sequence = 0; stop_sequence < orederedStopTimes.size() - 1; stop_sequence++) {
                // StopTime departure = orederedStopTimes.get(stop_sequence);
                // StopTime destination = orederedStopTimes.get(stop_sequence + 1);
// 
                // error management
                // if (departure == null || destination == null) {
                    // System.out.println("[ERROR] Departure or destination is null");
                    // continue;
                // }
// 
                // Connexion connexion = new Connexion(trip.getTripId(), departure.getStopId(), destination.getStopId(),
                        // departure.getTime(), destination.getTime());
                // allConnexions.add(connexion);
            // }
        // }
        // Instant end_time_connexions = Instant.now();
        // Duration connexions_time = Duration.between(start_time_connexions, end_time_connexions);
        // System.out.println("[INFO] Connexions built in " + connexions_time.getSeconds() + " seconds.");
// 
        // 
        Instant start_time_foot = Instant.now();
        // Building on foot connexions
        for (Stop stopA : allStops.values()) {
            for (Stop stopB : allStops.values()) {
                if (stopA.getStopId() == stopB.getStopId()) {
                    continue;
                }
                Instant start_time_haversine = Instant.now();
                double distance = stopA.getDistanceToOther(stopB);
                Instant end_time_haversine = Instant.now();
                Duration haversine_time = Duration.between(start_time_haversine, end_time_haversine);
                System.out.println("[INFO] Haversine distance calculated in " + haversine_time.getNano() + " nanoseconds.");
                if (distance < MAX_FOOT_DISTANCE) {
                    double walk_duration = distance / AVERAGE_WALKING_SPEED; // v = d / t => t = d / v
                    String duration = Calculator.timeToString(walk_duration);
                    Connexion connexion = new Connexion("WALK", stopA.getStopId(), stopB.getStopId(), "00:00:00",
                            duration);
                    allConnexions.add(connexion);
                }
// 
            }
        }
        Instant end_time_foot = Instant.now();
        Duration foot_time = Duration.between(start_time_foot, end_time_foot);
        System.out.println("[INFO] Foot connexions built in " + foot_time.getSeconds() + " seconds.");
// 
        System.out.println("[INFO] Number of connexions: " + allConnexions.size());

        // Sorting connexions by departure time
        Instant start_time_sort = Instant.now();
        Collections.sort(allConnexions);
        Instant end_time_sort = Instant.now();
        Duration sort_time = Duration.between(start_time_sort, end_time_sort);
        System.out.println("[INFO] Connexions sorted in " + sort_time.getSeconds() + " seconds.");
    }

    /**
     * Parses a CSV line while handling quoted fields with commas.
     */
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (byte i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString().trim());
        return result.toArray(new String[0]);
    }

    /**
     * Loads trips from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @return A map of trip IDs to Trip objects.
     * @throws IOException If an error occurs while reading the file.
     */
    public static Map<String, Trip> loadTrips(String filePath) throws IOException {
        Map<String, Trip> trips = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length < 2)
                    continue;
                try {
                    String trip_id = parts[0];
                    String route_id = parts[1];
                    trips.put(trip_id, new Trip(trip_id, route_id));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in line: " + line);
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
        return trips;
    }

    /**
     * Loads stops from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @return A map of stop IDs to Stop objects.
     * @throws IOException If an error occurs while reading the file.
     */
    public static Map<String, Stop> loadStops(String filePath) throws IOException {
        Map<String, Stop> stops = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length < 4)
                    continue;
                try {
                    String id = parts[0];
                    String name = parts[1];
                    double lat = Double.parseDouble(parts[2]);
                    double lon = Double.parseDouble(parts[3]);
                    stops.put(id, new Stop(id, name, lat, lon));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in line: " + line);
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
        return stops;
    }

    /**
     * Loads routes from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @return A map of route IDs to Route objects.
     * @throws IOException If an error occurs while reading the file.
     */
    public static Map<String, Route> loadRoutes(String filePath) throws IOException {
        Map<String, Route> routes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length < 4)
                    continue;
                String id = parts[0];
                String shortName = parts[1];
                String longName = parts[2];
                String type = parts[3];
                routes.put(id, new Route(id, shortName, longName, type));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
        return routes;
    }

    /**
     * Loads stop times from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public static void loadStopTimes(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length < 4)
                    continue;
                try {
                    String tripId = parts[0];
                    String departureTime = parts[1];
                    String stopId = parts[2];
                    int stopSequence = Integer.parseInt(parts[3]);

                    if (allTrips.containsKey((tripId))) {
                        Trip temp = allTrips.get(tripId);
                        temp.addStopTime(new StopTime(departureTime, stopId, stopSequence));
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in line: " + line);
                    continue;
                }
            }
            for (Trip trip : allTrips.values()) {
                trip.sortStopTimes();
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
    }

    /**
     * Returns all routes.
     *
     * @return A map of route IDs to Route objects.
     */
    public static Map<String, Route> getAllRoutes() {
        return allRoutes;
    }

    /**
     * Returns all stops.
     *
     * @return A map of stop IDs to Stop objects.
     */
    public static Map<String, Stop> getAllStops() {
        return allStops;
    }

    /**
     * Returns all trips.
     *
     * @return A map of trip IDs to Trip objects.
     */
    public static Map<String, Trip> getAllTrips() {
        return allTrips;
    }

    /**
     * Returns all connexions.
     *
     * @return A list of Connexion objects.
     */
    public static List<Connexion> getAllConnexions() {
        return allConnexions;
    }
}