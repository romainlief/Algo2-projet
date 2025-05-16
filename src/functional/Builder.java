package functional;

import objects.Connexion;
import objects.Stop;
import objects.StopTime;
import objects.Trip;
import objects.Walk;
import structures.BallTree;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Builder {

    // #### Attributes ####

    private double MAX_FOOT_DISTANCE = 500; // in meters
    private double AVERAGE_WALKING_SPEED = 1.0; // 1 m/s
    private int BALL_TREE_LEAFE_SIZE = 30; // number of stops per leaf:
                                           // -> higher number means a less deeper tree thus taking less space in memory
                                           // -> a lower number means a deeper tree with less Stops per leaf thus having
                                           // a
                                           // better search efficiency

    private List<Connexion> connexions = new ArrayList<>();
    private BallTree tree = null;

    // #### Constructors ####

    /**
     * @brief Constructor for the Builder class.
     * 
     * @param max_foot_distance     The maximum distance a person can walk to
     *                              transfer between two stops.
     * @param average_walking_speed The average walking speed of a person in m/s.
     * @param ball_tree_leaf_size   The number of stops per leaf in the Ball Tree.
     */
    Builder(double max_foot_distance, double average_walking_speed, int ball_tree_leaf_size) {
        this.MAX_FOOT_DISTANCE = max_foot_distance;
        this.AVERAGE_WALKING_SPEED = average_walking_speed;
        this.BALL_TREE_LEAFE_SIZE = ball_tree_leaf_size;
    }

    public void buildConnexions(Map<String, Trip> trips) {

        for (Trip trip : trips.values()) {
            List<StopTime> ordered_StopTimes = trip.getstopTimes(); // a list of ordered stopTimes
            for (int stop_sequence = 0; stop_sequence < ordered_StopTimes.size() - 1; stop_sequence++) {
                StopTime departure = ordered_StopTimes.get(stop_sequence);
                StopTime destination = ordered_StopTimes.get(stop_sequence + 1);

                // error management
                if (departure == null || destination == null) {
                    System.out.println("[\033[91mERROR\033[0m] Departure or destination is null");
                    continue;
                }

                Connexion connexion = new Connexion(trip.getTripId(), departure.getStopId(),
                        destination.getStopId(),
                        departure.getTime(), destination.getTime());
                this.connexions.add(connexion);
            }
        }
        Collections.sort(this.connexions); // sort connexions by departure time
    }

    public void buildBallTree(Map<String, Stop> stopsMap) {
        Collection<Stop> stops = stopsMap.values();
        this.tree = new BallTree(stops, this.BALL_TREE_LEAFE_SIZE);
    }

    public void buildWalks(Map<String, Stop> stopsMap) {
        if (this.tree == null) {
            System.out.println("[\033[91mERROR\033[0m] BallTree not built yet. Please build the BallTree first.");
            return;
        }

        for (Stop stopA : stopsMap.values()) {
            Collection<Stop> neighbours = this.tree.range(stopA, MAX_FOOT_DISTANCE);

            for (Stop stopB : neighbours) {
                if (stopA.getStopId().equals(stopB.getStopId()))
                    continue;

                double distance = stopA.getDistanceToOther(stopB);
                if (distance < MAX_FOOT_DISTANCE) {
                    double walkDuration = distance / AVERAGE_WALKING_SPEED;
                    int duration = (int) walkDuration;
                    Walk walk = new Walk(stopA, stopB, duration);
                    stopA.addWalk(walk);
                }
            }
        }
    }

    // #### Getters ####
    /**
     * @brief Getter for the list of connexions.
     * 
     * @return A list of Connexion objects.
     *         Each Connexion object representing a connection between two stops.
     */
    public List<Connexion> getConnexions() {
        return this.connexions;
    }
}
