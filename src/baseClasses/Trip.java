package baseClasses;

import java.util.List;
import java.util.ArrayList;

public class Trip {
    private String tripId;
    private List<StopTime> stopTimes = new ArrayList<StopTime>();

    public Trip(String tripId, List<StopTime> stopTimes) {
        this.tripId = tripId;
        this.stopTimes = stopTimes;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public List<StopTime> getstopTimes() {
        return stopTimes;
    }

    public void setRouteId(List<StopTime> stopTimes) {
        this.stopTimes = stopTimes;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId='" + tripId + '\'' +
                ", routeId='" + stopTimes + '\'' +
                '}';
    }
}