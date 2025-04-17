import baseClasses.Route;

public class Edge {
    private Node previous_stop;
    private Node next_stop;
    private Route route;

    // #### Constructor ####

    /**
     * 
     */
    public Edge(Node prev, Node next, Route route) {
        this.previous_stop = prev;
        this.next_stop = next;
        this.route = route;
    }

    // #### Getters and Setters ####

    public Node getPrevious() {
        return this.previous_stop;
    }

    public void setPrevious(Node newPrev) {
        this.previous_stop = newPrev;
    }

    public Node getNext() {
        return this.next_stop;
    }

    public void setNext(Node newNext) {
        this.next_stop = newNext;
    }

    public Route getRoute() {
        return this.route;
    }

    public void setRoute(Route newRoute) {
        this.route = newRoute;
    }
}
