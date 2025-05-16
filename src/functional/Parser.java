package functional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import objects.*;

public class Parser {

    // #### Attributes ####

    private String directory;
    private String[] entreprises = {
            "SNCB",
            "TEC",
            "STIB",
            "DELIJN"
    };

    private Map<String, Trip> allTrips = new HashMap<>();
    private Map<String, Stop> allStops = new HashMap<>();
    private Map<String, Route> allRoutes = new HashMap<>();

    // #### Constructors ####

    /**
     * @brief Constructor for the Parser class.
     * 
     * @param directory The directory where the GTFS files are located.
     */
    public Parser(String directory) {
        this.directory = directory;
    }

    /**
     * @brief Reads all GTFS files from the specified directory and loads them into
     *        corresponding maps.
     */
    public void readFiles() {

        for (String entreprise : entreprises) {
            String filePath = directory + "/" + entreprise + "/";
            try {
                loadTrips(filePath + "trips.csv");
                loadStops(filePath + "stops.csv");
                loadRoutes(filePath + "routes.csv");
                loadStopTimes(filePath + "stop_times.csv");
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath + "routes.csv");
                e.printStackTrace();
            }
        }
    }

    /**
     * @brief Parses a CSV line while handling quoted fields with commas.
     * 
     * @param line The CSV line to parse.
     * @return An array of strings representing the fields in the line.
     */
    private String[] parseCSVLine(String line) {
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
     * @brief Loads trips from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public void loadTrips(String filePath) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length < 2)
                    continue;
                try {
                    String tripId = parts[0];
                    String routeId = parts[1];
                    this.allTrips.put(tripId, new Trip(tripId, routeId));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in line: " + line);
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
    }

    /**
     * @brief Loads stops from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public void loadStops(String filePath) throws IOException {
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
                    this.allStops.put(id, new Stop(id, name, lat, lon));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in line: " + line);
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
    }

    /**
     * @brief Loads routes from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    private void loadRoutes(String filePath) throws IOException {
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
                this.allRoutes.put(id, new Route(id, shortName, longName, type));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
    }

    /**
     * @brief Loads stop times from a CSV file into a map.
     *
     * @param filePath Path to the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    private void loadStopTimes(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length < 4)
                    continue;
                try {
                    String tripId = parts[0];
                    int departureTime = Calculator.timeToInt(parts[1]);
                    String stopId = parts[2];
                    int stopSequence = Integer.parseInt(parts[3]);

                    if (this.allTrips.containsKey((tripId))) {
                        Trip temp = this.allTrips.get(tripId);
                        temp.addStopTime(new StopTime(departureTime, stopId, stopSequence));
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Invalid numeric value in line: " + line);
                    continue;
                }
            }
            for (Trip trip : this.allTrips.values()) {
                trip.sortStopTimes();
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            throw e;
        }
    }

    /**
     * @brief Returns all routes.
     *
     * @return A map of route IDs to Route objects.
     */
    public Map<String, Route> getAllRoutes() {
        return this.allRoutes;
    }

    /**
     * @brief Returns all stops.
     *
     * @return A map of stop IDs to Stop objects.
     */
    public Map<String, Stop> getAllStops() {
        return this.allStops;
    }

    /**
     * @brief Returns all trips.
     *
     * @return A map of trip IDs to Trip objects.
     */
    public Map<String, Trip> getAllTrips() {
        return this.allTrips;
    }
}