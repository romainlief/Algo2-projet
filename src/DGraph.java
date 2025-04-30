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

    /**
     * Returns the edges connected to a given node.
     *
     * @param node The node whose edges are to be retrieved.
     * @return A list of edges connected to the node.
     */
    public List<Edge> getEdges(Node node) {
        return graph.getOrDefault(node, new ArrayList<>());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Node, List<Edge>> entry : graph.entrySet()) {
            sb.append("Node: ").append(entry.getKey().getStop().getStopName()).append("\n");
            for (Edge edge : entry.getValue()) {
                sb.append("  -> ").append(edge.getNext().getStop().getStopName())
                        .append(" (Route: ").append(edge.getRoute().getRouteId()).append(")\n");
            }
        }
        return sb.toString();
    }
}
