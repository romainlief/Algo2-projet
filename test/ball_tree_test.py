from sklearn.neighbors import BallTree
import numpy as np
import csv
import os
import time

EARTH_RADIUS = 6371000


class Stop:
    def __init__(self, id,  name, latitude, longitude):
        self.id = id
        self.name = name
        self.latitude = latitude
        self.longitude = longitude

    def get_id(self):
        return self.id

    def get_name(self):
        return self.name

    def get_latitude(self):
        return self.latitude
    
    def get_longitude(self):
        return self.longitude

# Read stops from CSV file
def read_stops_from_csv(file_path):
    stops = []
    with open(file_path, 'r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            stop = Stop(
                row['stop_id'],
                row['stop_name'],
                float(row['stop_lat']),
                float(row['stop_lon'])
            )
            stops.append(stop)
    return stops


def haversine(stopA, stopB):
        # Convert latitude/longitude to radians
    lat1, lon1 = np.radians(stopA.get_latitude()), np.radians(stopA.get_longitude())
    lat2, lon2 = np.radians(stopB.get_latitude()), np.radians(stopB.get_longitude())
    
    # Haversine formula
    dlat = lat2 - lat1
    dlon = lon2 - lon1
    a = np.sin(dlat/2)**2 + np.cos(lat1) * np.cos(lat2) * np.sin(dlon/2)**2
    c = 2 * np.arcsin(np.sqrt(a))
    return EARTH_RADIUS * c

def query_stops_within_distance(tree, coords_rad, stops, stop, distance_meters):
    stop_coord_rad = np.radians([[stop.get_latitude(), stop.get_longitude()]])
    radius_rad = distance_meters / EARTH_RADIUS
    indices = tree.query_radius(stop_coord_rad, r=radius_rad)[0]
    return [stops[i] for i in indices if stops[i] != stop]


def main():
    stops = []
    base_path = "GTFS"
    for root, dirs, files in os.walk(base_path):
        if 'stops.csv' in files:
            csv_path = os.path.join(root, 'stops.csv')
            stops.extend(read_stops_from_csv(csv_path))

    print(f"Stops: {len(stops)}")
    
    # footpaths = []
    # for stopA in stops:
    #     for stopB in stops:
    #         if haversine(stopA, stopB) < 500:
    #             footpaths.append((stopA, stopB))
    #             footpaths.append((stopB, stopA))

    coords_rad = np.array([
        [np.radians(stop.get_latitude()), np.radians(stop.get_longitude())]
        for stop in stops
    ])

    # Build BallTree with haversine distance
    print("Building BallTree")
    start_time = time.time()
    tree = BallTree(coords_rad, metric='haversine', leaf_size=40)
    build_time = time.time() - start_time
    print(f"BallTree built in {build_time:.2f} seconds")

    neigbours = query_stops_within_distance(tree, coords_rad, stops, stops[500], 500)
    print(f"Stop: {stops[500].get_name()} ({stops[500].get_latitude()}, {stops[500].get_longitude()}), id: {stops[500].get_id()}")
    print(f"Printing the {len(neigbours)} neigbours within 500m:")
    for stop in neigbours:
        print(f"- {stop.get_name()} ({stop.get_latitude()}, {stop.get_longitude()}) -> distance: {haversine(stops[500], stop):.2f}m")

if __name__ == "__main__":
    main()