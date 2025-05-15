package baseClasses;

public class Walk {
    // #### Attributes ####

    private final Stop departure;
    private final Stop destination;
    private final int duration;

    // #### Constructors ####

    /**
     * @brief Constructor for Walk class.
     *
     * @param departure   The Stop where the walk begins.
     * @param destination The Stop where the walk ends.
     * @param duration    Duration of the walk in format "HH:MM:SS".
     */
    public Walk(Stop departure, Stop destination, int duration) {
        this.departure = departure;
        this.destination = destination;
        this.duration = duration;
    }

    // #### Getters ####

    /**
     * @brief Gets the departure Stop of the walk.
     *
     * @return The Stop where the walk begins.
     */
    public Stop getDeparture() {
        return this.departure;
    }

    /**
     * @brief Gets the destination Stop of the walk.
     *
     * @return The Stop where the walk ends.
     */
    public Stop getDestination() {
        return this.destination;
    }

    /**
     * @brief Gets the duration of the walk.
     *
     * @return The duration in format "HH:MM:SS".
     */
    public int getDuration() {
        return this.duration;
    }
}
