package structures;

import objects.*;

import java.util.*;

// A data structure that we tried to use, but didn't in the end.
// We left it for the reader's appreciation.
public class HashGrid {

    // #### Attributes ####

    private final double cellSize; // in degrees
    private final Map<String, List<Stop>> grid;

    // #### Constructors ####

    /**
     * @brief Constructor of the HashGrid class.
     * 
     * @param cellSize The size of each cell in degrees, representing the spatial
     *                  resolution of the grid.
     *                  It defines the maximum range of distance within which stops
     *                  are grouped together in the same cell.
     */
    public HashGrid(double cellSize) {
        this.cellSize = cellSize;
        this.grid = new HashMap<String, List<Stop>>();
    }

    // #### Methods ####

    /**
     * @brief Adds a Stop to a cell of stops within range.
     * 
     * @param stop The Stop to add.
     */
    public void addStop(Stop stop) {
        String key = getCellKey(stop.getStopLat(), stop.getStopLon());
        grid.computeIfAbsent(key, k -> new ArrayList<>()).add(stop); // if absent creating a new list of neighbours
                                                                     // else completing list of neighbours
    }

    /**
     * @brief Builds grid based on a HashMap of Stops by iterating throught them and
     *        storing them in cells, regions.
     * 
     * @param stops The HashMap of Stops used to build the HashGrid.
     */
    public void buildGrid(Map<String, Stop> stops) {
        for (Stop stop : stops.values()) {
            addStop(stop);
        }
    }

    /**
     * @brief Gets all stops in neighbouring cells of the given stop by
     * 
     * @param stop The stop to find neighbors for.
     * @return A list of neighbouring stops from surrounding cells.
     */
    public List<Stop> getNeighbourStops(Stop stop) {
        List<Stop> neighbours = new ArrayList<Stop>();

        // getting hash key of the cell (region/area ~= 500m)
        int latBucket = (int) (stop.getStopLat() / this.cellSize);
        int longBucket = (int) (stop.getStopLon() / this.cellSize);

        // getting neighbours
        for (int deltaLat = -1; deltaLat <= 1; deltaLat++) {
            for (int deltaLong = -1; deltaLong <= 1; deltaLong++) {
                String neighbourKey = getCellKey((latBucket + deltaLat),
                        (longBucket + deltaLong));
                List<Stop> candidates = grid.getOrDefault(neighbourKey, new ArrayList<>());
                neighbours.addAll(candidates);
            }
        }

        return neighbours;
    }
    private String getCellKey(int latBucket, int lonBucket) {
        return latBucket + "_" + lonBucket;
    }
    /**
     * @brief Generates a unique key for a cell based on latitude and longitude
     *        coordinates.
     * 
     * @param lat The latitude coordinate to hash.
     * @param lon The longitude coordinate to hash.
     * @return A string key representing the cell's position in the grid.
     */
    private String getCellKey(double lat, double lon) {
        int latBucket = (int) (lat / this.cellSize);
        int longBucket = (int) (lon / this.cellSize);
        return latBucket + "_" + longBucket;
    }
}
