package baseClasses;

import java.util.List;
import java.util.ArrayList;

public class Trip {
    private String tripId;
    private String routeId;

    // key -> the stop_sequence of the stop
    // value -> StopTime
    private List<StopTime> stopTimes = new ArrayList<StopTime>();

    public Trip(String tripId, String routeId) {
        this.tripId = tripId;
        this.routeId = routeId;
    }

    public String getTripId() {
        return this.tripId;
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

    public List<StopTime> getstopTimes() {
        return this.stopTimes;
    }

    public void addStopTime(StopTime newStop) {
        stopTimes.add(newStop);
    }

    public void sortStopTimes() {
        stopTimes.sort((stop1, stop2) -> Integer.compare(stop1.getStopSequence(), stop2.getStopSequence()));
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId = '" + tripId + '\'' +
                ", routeId = '" + stopTimes + '\'' +
                "}\n";
    }
}