package baseClasses;

public class Walk {
    private final String duration;
    private final Stop destination;

    public Walk(String duration, Stop destination) {
        this.duration = duration;
        this.destination = destination;
    }

    public String getDuration() {
        return this.duration;
    }

    public Stop getDestination() {
        return this.destination;
    }
}
