import java.util.Map;
import baseClasses.Route;
import baseClasses.Stop;
import baseClasses.StopTime;
import baseClasses.Trip;

public class Main {

    public static void main(String[] args) {
        try {
            Parser parser = new Parser("GTFS");
            parser.readFiles();

            Map<String, Route> allRoutes = Parser.getAllRoutes();
            Map<String, Stop> allStops = Parser.getAllStops();
            Map<String, StopTime> allStopTimes = Parser.getAllStopTimes();
            Map<String, Trip> allTrips = Parser.getAllTrips();

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