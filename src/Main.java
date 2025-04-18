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
        } catch (Exception e) {
            System.err.println("Error loading GTFS data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}