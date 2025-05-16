package functional;

import objects.Connexion;
import objects.Trip;
import objects.Stop;
import objects.Route;

import java.util.List;
import java.util.Map;

import java.time.Duration;
import java.time.Instant;

public class Initializer {

    Parser parser;
    Builder builder;

    public Initializer(String directory, double max_foot_distance, double average_walking_speed, int leaf_size) {
        this.parser = new Parser(directory);
        this.builder = new Builder(max_foot_distance, average_walking_speed, leaf_size);
    }

    public void setup(boolean is_timed) {
        if (is_timed) {

            Instant start_read = Instant.now();
            parser.readFiles();
            Instant end_read = Instant.now();
            Duration duration_read = Duration.between(start_read, end_read);
            System.out.println("[\033[92mINFO\033[0m] Files read in " + duration_read.toMillis() + " ms.");
            System.out.println("[\033[92mINFO\033[0m] Trips: " + parser.getAllTrips().size());
            System.out.println("[\033[92mINFO\033[0m] Stops: " + parser.getAllStops().size());
            System.out.println("[\033[92mINFO\033[0m] Routes: " + parser.getAllRoutes().size());

            Instant start_build = Instant.now();
            builder.buildConnexions(parser.getAllTrips());
            Instant end_build = Instant.now();
            Duration duration_build = Duration.between(start_build, end_build);
            System.out.println("[\033[92mINFO\033[0m] " + builder.getConnexions().size()
                    + " connexions built and sorted in " + duration_build.toMillis() + " ms.");

            Instant start_balltree = Instant.now();
            builder.buildBallTree(parser.getAllStops());
            Instant end_balltree = Instant.now();
            Duration duration_balltree = Duration.between(start_balltree, end_balltree);
            System.out.println("[\033[92mINFO\033[0m] BallTree built in " + duration_balltree.toMillis() + " ms.");

            Instant start_walks = Instant.now();
            builder.buildWalks(parser.getAllStops());
            Instant end_walks = Instant.now();
            Duration duration_walks = Duration.between(start_walks, end_walks);
            System.out.println("[\033[92mINFO\033[0m] Walks built in " + duration_walks.toMillis() + " ms.");

            System.out.println("[\033[92mINFO\033[0m] All data loaded and processed in " + (duration_read.toMillis()
                    + duration_build.toMillis() + duration_balltree.toMillis() + duration_walks.toMillis()) + " ms.");

        } else {
            parser.readFiles();
            builder.buildConnexions(parser.getAllTrips());
            builder.buildBallTree(parser.getAllStops());
            builder.buildWalks(parser.getAllStops());
        }
    }

    // #### Getters ####

    /*
     * @brief Returns all the stops.
     * 
     * @return A map of all stops, where the key is the stop ID and the value is the
     * Stop object itself.
     */
    public Map<String, Stop> getStops() {
        return this.parser.getAllStops();
    }

    /*
     * @brief Returns all the trips.
     * 
     * @return A map of all trips, where the key is the trip ID and the value is the
     * Trip object itself.
     */
    public Map<String, Trip> getTrips() {
        return this.parser.getAllTrips();
    }

    /*
     * @brief Returns all the routes.
     * 
     * @return A map of all routes, where the key is the route ID and the value is
     * the Route object itself.
     */
    public Map<String, Route> getRoutes() {
        return this.parser.getAllRoutes();
    }

    /*
     * @brief Returns all the connections.
     * 
     * @return A list of all connections between stops.
     */
    public List<Connexion> getConnexions() {
        return this.builder.getConnexions();
    }

}