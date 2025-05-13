package baseClasses;

import java.util.List;
import java.util.ArrayList;

public class Stop {

    // #### Attributes ####

    private final String stopId;
    private final String stopName;
    private final double stopLat;
    private final double stopLon;
    List<String> trip_ids = new ArrayList<String>(); // a list of trips departing from this stop

    private List<Walk> walks = new ArrayList<Walk>(); // all walks departing from this stop

    // #### Constructors ####

    /**
     * @brief Constructor for Stop class.
     *
     * @param stopId   Unique identifier for the stop.
     * @param stopName Name of the stop.
     * @param stopLat  Latitude of the stop.
     * @param stopLon  Longitude of the stop.
     */
    public Stop(String stopId, String stopName, double stopLat, double stopLon) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopLat = stopLat;
        this.stopLon = stopLon;
    }

    // #### Getters ####

    public String getStopId() {
        return this.stopId;
    }

    public String getStopName() {
        return this.stopName;
    }

    public double getStopLat() {
        return this.stopLat;
    }

    public double getStopLon() {
        return this.stopLon;
    }

    public List<String> getTripIds() {
        return this.trip_ids;
    }

    public List<Walk> getWalk() {
        return this.walks;
    }

    // #### Methods ####

    /**
     * @brief Adds a trip ID to the list of trip IDs departing from this stop.
     *
     * @param trip_id The trip ID to add.
     */
    public void addTrip(String trip_id) {
        this.trip_ids.add(trip_id);
    }

    /**
     * @brief Add a walk to this stop 
     * 
     * @param walk A new walk departing from this Stop
     */
    public void addWalk(Walk walk) {
        this.walks.add(walk);
    }

    /**
     * @brief Returns the distance to another stop.
     * 
     * @param other The stop to which the distance is calculated.
     * @return The distance in meters.
     */
    public double getDistanceToOther(Stop other) {
        return Calculator.haversine_distance(this, other);
    }

    // #### toString Method ####

    @Override
    public String toString() {
        return "Stop{" +
                "stopId ='" + stopId + '\'' +
                ", stopName ='" + stopName + '\'' +
                ", stopLat ='" + stopLat + '\'' +
                ", stopLon ='" + stopLon + '\'' +
                "}\n";
    }
}