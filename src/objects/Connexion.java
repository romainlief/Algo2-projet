package objects;

public class Connexion implements Comparable<Connexion> {
    private final String tripId;
    private final String fromId; // stopId
    private final String toId; // stopId
    private final int departureTime;
    private final int arrivalTime;

    public Connexion(String tripId, String fromId, String toId, int departureTime, int arrivalTime) {
        this.tripId = tripId;
        this.fromId = fromId; // starting stop
        this.toId = toId; // destination stop
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getTripId() {
        return this.tripId;
    }

    public String getFromId() {
        return this.fromId;
    }

    public String getToId() {
        return this.toId;
    }

    public int getDepartureTime() {
        return this.departureTime;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    @Override
    public int compareTo(Connexion other) {
        return Integer.compare(this.departureTime, other.departureTime);
    }
}
