package baseClasses;
public class StopTime {
    private String tripId;
    private String departureTime;
    private String stopId;
    private int stopSequence;

    // #### Constructors ####

    /**
     * Constructor for StopTime class.
     *
     * @param tripId        Unique identifier for the trip.
     * @param departureTime Departure time of the stop.
     * @param stopId        Unique identifier for the stop.
     * @param stopSequence  Sequence number of the stop in the trip.
     */
    public StopTime(String tripId, String departureTime, String stopId, int stopSequence) {
        this.tripId = tripId;
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
    }

    // #### Getters and Setters ####

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    // #### toString Method ####
    @Override
    public String toString() {
        return "StopTime{" +
                "tripId='" + tripId + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", stopId='" + stopId + '\'' +
                ", stopSequence='" + stopSequence + '\'' +
                '}';
    }
}
