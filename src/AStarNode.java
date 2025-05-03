import baseClasses.*;


public class AStarNode {
    private final Stop stop;
    private final AStarNode parent;
    private final double g; // cost from start to this node
    private final double h; // heuristic cost from this node to the goal

    public AStarNode(Stop stop, AStarNode parent, double g, double h) {
        this.stop = stop;
        this.parent = parent;
        this.g = g;
        this.h = h;
    }

    public Stop getStop() {
        return this.stop;
    }

    public AStarNode getParent() {
        return this.parent;
    }

    public double getCostFromStart() {
        return this.g;
    }

    public double getHeuristic() {
        return this.h;
    }
}