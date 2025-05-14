package baseClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * @brief BallTree class used to partition stops into a tree structure in order
 *        to optimize the search for the nearest stop by having quick access to
 *        the neighbours of a Stop.
 * @details Its implementation is based on the Ball Tree Wikipedia page:
 *          https://en.wikipedia.org/wiki/Ball_tree
 *          Will be further developed in report.
 * 
 */
public class BallTree {
    private final BallTreeNode root;
    private final int leaf_size;

    // Node class that represents a node in the Ball Tree
    private class BallTreeNode {
        Stop center;
        double radius;
        List<Stop> stops;
        BallTreeNode left;
        BallTreeNode right;

        /**
         * @brief Constructor for a leaf.
         * 
         * @param stops
         */
        BallTreeNode(List<Stop> stops) {
            this.stops = stops;
        }

        /**
         * @brief Constuctor for a partitionning node.
         * 
         * @param center
         * @param radius
         */
        BallTreeNode(Stop center, double radius) {
            this.center = center;
            this.radius = radius;
        }

        /**
         * Checks if current node is a leaf node in the ball tree.
         * 
         * @return true if node contains stops data, false if it's an internal node
         */
        boolean isLeaf() {
            return this.stops != null;
        }
    }

    public BallTree(Collection<Stop> stops, int leaf_size) {
        this.leaf_size = leaf_size;
        this.root = buildTree(new ArrayList<Stop>(stops)); // O(n)
    }

    /**
     * @brief Builds the Ball Tree recursively based on a list of unordered stops.
     *
     * @param stops The list of stops to be used for building the tree.
     * @return The root node of the constructed Ball Tree.
     */
    private BallTreeNode buildTree(List<Stop> stops) {
        if (stops == null || stops.isEmpty()) {
            return null;
        }
        // Base case for recursion
        // If there is only one stop, create a leaf node
        if (stops.size() < this.leaf_size) {
            return new BallTreeNode(stops);
        }
        Stop[] pivots = findFarthest(stops);
        Stop pivot1 = pivots[0], pivot2 = pivots[1];

        List<Stop> left = new ArrayList<>();
        List<Stop> right = new ArrayList<>();

        // Partition selon la proximité aux deux pivots
        for (Stop s : stops) {
            double d1 = pivot1.getDistanceToOther(s);
            double d2 = pivot2.getDistanceToOther(s);
            if (d1 < d2)
                left.add(s);
            else
                right.add(s);
        }

        // Noeud interne avec centre et rayon
        Stop center = computeCenter(stops);
        double radius = computeRadius(stops, center);

        BallTreeNode node = new BallTreeNode(center, radius);
        node.left = buildTree(left);
        node.right = buildTree(right);
        return node;
    }

    private Stop[] findFarthest(List<Stop> stops) {
        Stop a = stops.get(0), b = a;
        double best = 0;
        for (Stop s1 : stops) {
            for (Stop s2 : stops) {
                double d = s1.getDistanceToOther(s2);
                if (d > best) {
                    best = d;
                    a = s1;
                    b = s2;
                }
            }
        }
        return new Stop[] { a, b };
    }

    private Stop computeCenter(List<Stop> stops) {
        double sumLat = 0, sumLon = 0;
        for (Stop s : stops) {
            sumLat += s.getStopLat();
            sumLon += s.getStopLon();
        }
        double avgLat = sumLat / stops.size();
        double avgLon = sumLon / stops.size();
        return new Stop("", "", avgLat, avgLon); // creating a virtual Stop used only for partitioning
    }

    private double computeRadius(List<Stop> stops, Stop center) {
        double maxDist = 0;
        for (Stop s : stops) {
            double d = center.getDistanceToOther(s);
            if (d > maxDist)
                maxDist = d;
        }
        return maxDist;
    }

    /**
     * Retourne tous les stops à distance ≤ maxDist (en mètres)
     * 
     * @param query   le Stop requête
     * @param maxDist rayon maximum en mètres
     */
    public List<Stop> range(Stop query, double maxDist) {
        List<Stop> result = new ArrayList<>();
        rangeSearch(root, query, maxDist, result);
        return result;
    }

    private void rangeSearch(BallTreeNode node, Stop q, double maxDist, List<Stop> out) {
        if (node == null)
            return;

        if (node.isLeaf()) {
            // Dans une feuille, test direct pour chaque Stop
            for (Stop s : node.stops) {
                if (q.getDistanceToOther(s) <= maxDist) {
                    out.add(s);
                }
            }
        } else {
            // Calcul de la distance de la requête au centre du nœud
            double distToCenter = q.getDistanceToOther(node.center);
            // Si la boule est trop loin (distance au centre – rayon > maxDist), on prune
            if (distToCenter - node.radius > maxDist) {
                return;
            }
            // Sinon on descend dans les deux branches (l’une pourrait encore contenir des
            // solutions)
            rangeSearch(node.left, q, maxDist, out);
            rangeSearch(node.right, q, maxDist, out);
        }
    }

}
