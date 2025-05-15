package structures;

import objects.*;

import java.util.*;

// A data structure that we tried to use, but didn't in the end
// We left it for the reader's appreciation
public class HashGrid {

    // #### Attributes ####

    private final double cell_size; // in degrees
    private final Map<String, List<Stop>> grid;

    // #### Constructors ####

    /**
     * @brief Constructor of the HashGrid class.
     * 
     * @param cell_size The size of each cell in degrees, representing the spatial
     *                  resolution of the grid.
     *                  It defines the maximum range of distance within which stops
     *                  are grouped together in the same cell.
     */
    public HashGrid(double cell_size) {
        this.cell_size = cell_size;
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
        int lat_bucket = (int) (stop.getStopLat() / this.cell_size);
        int lon_bucket = (int) (stop.getStopLon() / this.cell_size);

        // getting neighbours
        for (int delta_lat = -1; delta_lat <= 1; delta_lat++) {
            for (int delta_lon = -1; delta_lon <= 1; delta_lon++) {
                String neighbour_key = getCellKey((lat_bucket + delta_lat),
                        (lon_bucket + delta_lon));
                List<Stop> candidates = grid.getOrDefault(neighbour_key, new ArrayList<>());
                neighbours.addAll(candidates);
            }
        }

        return neighbours;
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
        int lat_bucket = (int) (lat / this.cell_size);
        int lon_bucket = (int) (lon / this.cell_size);
        return lat_bucket + "_" + lon_bucket;
    }
}
