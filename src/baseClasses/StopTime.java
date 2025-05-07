package baseClasses;

public class StopTime {

    // #### Attributes ####
    
    private final String departureTime;
    private final String stopId;
    private final int stopSequence;

    // #### Constructors ####

    /**
     * Constructor for StopTime class.
     *
     * @param departureTime Departure time of the stop.
     * @param stopId        Unique identifier for the stop.
     * @param stopSequence  Sequence number of the stop in the trip.
     */
    public StopTime(String departureTime, String stopId, int stopSequence) {
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
    }

    // #### Getters ####

    public String getTime() {
        return this.departureTime;
    }

    public String getStopId() {
        return this.stopId;
    }

    public int getStopSequence() {
        return this.stopSequence;
    }

    // #### toString Method ####
    @Override
    public String toString() {
        return "StopTime{" +
                ", departureTime = '" + departureTime + '\'' +
                ", stopId = '" + stopId + '\'' +
                ", stopSequence = '" + stopSequence + '\'' +
                "}\n";
    }
}
