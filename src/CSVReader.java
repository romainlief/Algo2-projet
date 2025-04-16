import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import baseClasses.*;

public class CSVReader {
    private static String directory = "GTFS";
    private static String[] entreprises = { "SNCB", "TEC", "STIB", "DELIJN" };

    public static Map<String, Route> allRoutes = new HashMap<>();
    public static Map<String, Stop> stops = new HashMap<>();
    public static Map<String, StopTime> stopTimes = new HashMap<>();
    public static Map<String, Trip> trips = new HashMap<>();

    public static void readFiles() {
        for (String entreprise : entreprises) {
            String filePath = directory + "/" + entreprise + "/";
            try {
                allRoutes.putAll(loadRoutes(filePath + "routes.csv"));
            } catch (IOException e) {
                System.err.println("Error reading file: " + filePath + "routes.csv");
                e.printStackTrace();
            }
        }
    }

    public static Map<String, Route> loadRoutes(String filePath) throws IOException {
        Map<String, Route> routes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;
                String id = parts[0];
                String shortName = parts[1];
                String longName = parts[2];
                String type = parts[3];
                routes.put(id, new Route(id, shortName, longName, type));
            }
        }
        return routes;
    }




    
}