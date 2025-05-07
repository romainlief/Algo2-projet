package baseClasses;

/**
 * Class representing a public transport route.
 */
public class Route {

    // #### Attributes ####

    private final String routeId;
    private final String routeShortName;
    private final String routeLongName;
    private final String routeType;

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

    // #### Getters ####

    public String getRouteId() {
        return this.routeId;
    }

    public String getRouteShortName() {
        return this.routeShortName;
    }

    public String getRouteLongName() {
        return this.routeLongName;
    }

    public String getRouteType() {
        return this.routeType;
    }

    // #### toString Method ####
    @Override
    public String toString() {
        return "Route{" +
                "routeId = '" + routeId + '\'' +
                ", routeShortName = '" + routeShortName + '\'' +
                ", routeLongName = '" + routeLongName + '\'' +
                ", routeType = '" + routeType + '\'' +
                "}\n";
    }
}