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

    public Initializer(String directory, double maxFootDistance, double averageWalkingSpeed, int leafSize) {
        this.parser = new Parser(directory);
        this.builder = new Builder(maxFootDistance, averageWalkingSpeed, leafSize);
    }

    public void setup(boolean is_timed) {
        if (is_timed) {
            Instant startRead = Instant.now();
            parser.readFiles();
            Instant endRead = Instant.now();
            Duration durationRead = Duration.between(startRead, endRead);
            System.out.println("[\033[92mINFO\033[0m] Files read in " + durationRead.toMillis() + " ms.");
            System.out.println("[\033[92mINFO\033[0m] Trips: " + parser.getAllTrips().size());
            System.out.println("[\033[92mINFO\033[0m] Stops: " + parser.getAllStops().size());
            System.out.println("[\033[92mINFO\033[0m] Routes: " + parser.getAllRoutes().size());

            Instant startBuild = Instant.now();
            builder.buildConnexions(parser.getAllTrips());
            Instant endBuild = Instant.now();
            Duration durationBuild = Duration.between(startBuild, endBuild);
            System.out.println("[\033[92mINFO\033[0m] " + builder.getConnexions().size()
                + " connexions built and sorted in " + durationBuild.toMillis() + " ms.");

            Instant startBallTree = Instant.now();
            builder.buildBallTree(parser.getAllStops());
            Instant endBallTree = Instant.now();
            Duration durationBallTree = Duration.between(startBallTree, endBallTree);
            System.out.println("[\033[92mINFO\033[0m] BallTree built in " + durationBallTree.toMillis() + " ms.");

            Instant startWalks = Instant.now();
            builder.buildWalks(parser.getAllStops());
            Instant endWalks = Instant.now();
            Duration durationWalks = Duration.between(startWalks, endWalks);
            System.out.println("[\033[92mINFO\033[0m] Walks built in " + durationWalks.toMillis() + " ms.");

            System.out.println("[\033[92mINFO\033[0m] All data loaded and processed in " + (durationRead.toMillis()
                + durationBuild.toMillis() + durationBallTree.toMillis() + durationWalks.toMillis()) + " ms.");

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