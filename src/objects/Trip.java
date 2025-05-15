package objects;

import java.util.List;
import java.util.ArrayList;

public class Trip {

    // #### Attributes ####

    private final String tripId;
    private final String routeId;

    // key -> the stop_sequence of the stop
    // value -> StopTime
    private List<StopTime> stopTimes = new ArrayList<StopTime>();

    // #### Constructors ####
    /**
     * Constructor for Trip class.
     *
     * @param tripId  Unique identifier for the trip.
     * @param routeId Unique identifier for the route.
     */
    public Trip(String tripId, String routeId) {
        this.tripId = tripId;
        this.routeId = routeId;
    }

    // #### Getters ####

    public String getTripId() {
        return this.tripId;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public List<StopTime> getstopTimes() {
        return this.stopTimes;
    }

    // #### Methods ####

    /**
     * Adds a new StopTime to the list of stop times for this trip.
     *
     * @param newStop The StopTime to add.
     */
    public void addStopTime(StopTime newStop) {
        this.stopTimes.add(newStop);
    }

    /**
     * Sorts the list of StopTimes based on their stop_sequence.
     *
     */
    public void sortStopTimes() {
        this.stopTimes.sort((stop1, stop2) -> Integer.compare(stop1.getStopSequence(), stop2.getStopSequence()));
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId = '" + tripId + '\'' +
                ", routeId = '" + stopTimes + '\'' +
                "}\n";
    }
}