import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import baseClasses.Route;

public class DGraph {
    private Map<Node, List<Edge>> graph = new HashMap<>();

    /**
     * Adds a node to the graph.
     *
     * @param node The node to be added.
     */
    public void addNode(Node node) {
        graph.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Adds an edge to the graph.
     *
     * @param from  The starting node of the edge.
     * @param to    The ending node of the edge.
     * @param route The route associated with the edge.
     */
    public void addEdge(Node from, Node to, Route route) {
        graph.putIfAbsent(from, new ArrayList<>());
        Edge edge = new Edge(from, to, route);
        graph.get(from).add(edge);
    }
}
