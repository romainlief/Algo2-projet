package baseClasses;

public class StopTime {
    private String departureTime;
    private String stopId;
    private String stopSequence;

    // #### Constructors ####

    /**
     * Constructor for StopTime class.
     *
     * @param departureTime Departure time of the stop.
     * @param stopId        Unique identifier for the stop.
     * @param stopSequence  Sequence number of the stop in the trip.
     */
    public StopTime(String departureTime, String stopId, String stopSequence) {
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
    }

    // #### Getters and Setters ####

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

    public String getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.stopSequence = stopSequence;
    }

    // #### toString Method ####
    @Override
    public String toString() {
        return "StopTime{" +
                ", departureTime='" + departureTime + '\'' +
                ", stopId='" + stopId + '\'' +
                ", stopSequence='" + stopSequence + '\'' +
                '}';
    }
}
