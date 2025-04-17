import java.util.List;
import java.util.ArrayList;

import baseClasses.Stop;

public class Node {
    private Stop stop;
    private List<Edge> edges = new ArrayList<Edge>();

    // #### Constructor ####

    /**
     * Constructor of the Node class.
     * 
     * @param stop  The stop represented by the Node
     * @param edges The edges representing the trips
     */
    public Node(Stop stop, List<Edge> edges) {
        this.stop = stop;
        this.edges = edges;
    }

    // #### Getters and Setters ####

    public Stop getStop() {
        return this.stop;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public void setStop(Stop newStop) {
        this.stop = newStop;
    }

    public void setEdges(List<Edge> newEdges) {
        this.edges = newEdges;
    }
}
