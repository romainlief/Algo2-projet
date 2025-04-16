package baseClasses;
/**
 * Class representing a public transport route.
 */
public class Route {

    // #### Attributes ####

    private String routeId;
    private String routeShortName;
    private String routeLongName;
    private String routeType;

    // #### Constructors ####
    
    /**
     * Constructor for Route class.
     *
     * @param routeId        Unique identifier for the route.
     * @param routeShortName Short name of the route.
     * @param routeLongName  Long name of the route.
     * @param routeType      Type of the route (e.g., bus, train).
     */
    public Route(String routeId, String routeShortName, String routeLongName, String routeType) {
        this.routeId = routeId;
        this.routeShortName = routeShortName;
        this.routeLongName = routeLongName;
        this.routeType = routeType;
    }

    // #### Getters and Setters ####

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public void setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    // #### toString Method ####
    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", routeShortName='" + routeShortName + '\'' +
                ", routeLongName='" + routeLongName + '\'' +
                ", routeType='" + routeType + '\'' +
                '}';
    }
}