package baseClasses;

public class Connexion implements Comparable<Connexion> {
    private final String trip_id;
    private final String from_id; // stop id
    private final String to_id; // stop_id
    private final String departure_time;
    private final String arrival_time;

    public Connexion(String trip_id, String from_id, String to_id, String departure_time, String arrival_time) {
        this.trip_id = trip_id;
        this.from_id = from_id; // starting stop
        this.to_id = to_id; // destination stop
        this.departure_time = departure_time;
        this.arrival_time = arrival_time;
    }

    public String getTripId() {
        
        return this.trip_id;
    }

    public String getFromId() {
        return this.from_id;
    }

    public String getToId() {
        return this.to_id;
    }

    public String getDepartureTime() {
        return this.departure_time;
    }

    public String getArrivalTime() {
        return this.arrival_time;
    }

    @Override
    public int compareTo(Connexion other) {
        return this.departure_time.compareTo(other.departure_time);
    }
}