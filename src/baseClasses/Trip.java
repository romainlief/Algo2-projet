package baseClasses;

import java.util.Map;
import java.util.HashMap;

public class Trip {
    private String tripId;
    private String routeId;

    // key   -> the stop_sequence of the stop
    // value -> StopTime
    private Map<String, StopTime> stopTimes = new HashMap<String, StopTime>();

    public Trip(String tripId, String routeId) {
        this.tripId = tripId;
        this.routeId = routeId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getRouteId() {
        return this.routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Map<String, StopTime> getstopTimes() {
        return stopTimes;
    }

    public void addStopTime(String trip_id, StopTime newStop) {
        if (!stopTimes.containsKey(trip_id)) {
            stopTimes.put(trip_id, newStop);
        }
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId='" + tripId + '\'' +
                ", routeId='" + stopTimes + '\'' +
                '}';
    }
}