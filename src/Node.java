import java.util.List;
import java.time.LocalTime;
import java.util.ArrayList;

import baseClasses.Stop;

public class Node {
    private Stop stop;
    private LocalTime time;
    private List<Edge> edges = new ArrayList<Edge>();

    // #### Constructor ####

    /**
     * Constructor of the Node class.
     * 
     * @param stop  The stop represented by the Node
     * @param edges The edges representing the trips
     */
    public Node(Stop stop, LocalTime time, List<Edge> edges) {
        this.stop = stop;
        this.time = time;
        this.edges = edges;
    }

    // #### Getters and Setters ####

    public Stop getStop() {
        return this.stop;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setStop(Stop newStop) {
        this.stop = newStop;
    }

    public void setEdges(List<Edge> newEdges) {
        // this.edges = newEdges;
    }

    public void setTime(LocalTime newTime) {
        this.time = newTime;
    }
}
