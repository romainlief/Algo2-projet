package baseClasses;
public class Stop {
    private String stopId;
    private String stopName;
    private double stopLat;
    private double stopLon;

    // #### Constructors ####
    
    /**
     * Constructor for Stop class.
     *
     * @param stopId   Unique identifier for the stop.
     * @param stopName Name of the stop.
     * @param stopLat  Latitude of the stop.
     * @param stopLon  Longitude of the stop.
     */
    public Stop(String stopId, String stopName, double stopLat, double stopLon) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopLat =  stopLat;
        this.stopLon = stopLon;
    }

    // #### Getters and Setters ####

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public double getStopLat() {
        return stopLat;
    }

    public void setStopLat(double stopLat) {
        this.stopLat = stopLat;
    }

    public double getStopLon() {
        return stopLon;
    }

    public void setStopLon(double stopLon) {
        this.stopLon = stopLon;
    }

    // #### toString Method ####

    @Override
    public String toString() {
        return "Stop{" +
                "stopId='" + stopId + '\'' +
                ", stopName='" + stopName + '\'' +
                ", stopLat='" + stopLat + '\'' +
                ", stopLon='" + stopLon + '\'' +
                '}';
    }
}