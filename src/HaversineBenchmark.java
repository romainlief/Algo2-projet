public class HaversineBenchmark {

    public static void main(String[] args) {
        Stop stopA = new Stop("A", "Stop A", 50.40876, 4.17245);
        Stop stopB = new Stop("B", "Stop B", 50.863666, 4.329612);

        // Echauffement (important pour éliminer les effets de compilation JIT)
        for (int i = 0; i < 1000000; i++) {
            haversine_distance(stopA, stopB);
        }

        long start = System.nanoTime();

        int N = 1000000; // changeable pour tester la performance plus précisément
        double total = 0;
        for (int i = 0; i < N; i++) {
            total += haversine_distance(stopA, stopB);
        }

        long end = System.nanoTime();
        double averageNs = (end - start) / (double) N;

        System.out.printf("Moyenne sur %d appels : %.2f ns%n", N, averageNs);
        System.out.println("Résultat final moyen (distance) : " + (total / N) + " m");
    }

    public static double haversine_distance(Stop a, Stop b) {
        final double R = 6371000.0; // rayon terrestre en mètres
        double lat1 = Math.toRadians(a.getStopLat());
        double lon1 = Math.toRadians(a.getStopLon());
        double lat2 = Math.toRadians(b.getStopLat());
        double lon2 = Math.toRadians(b.getStopLon());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double h = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                        * Math.pow(Math.sin(deltaLon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(h));
        return R * c;
    }

    static class Stop {
        private final String stopId;
        private final String name;
        private final double lat;
        private final double lon;

        public Stop(String stopId, String name, double lat, double lon) {
            this.stopId = stopId;
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }

        public double getStopLat() {
            return lat;
        }

        public double getStopLon() {
            return lon;
        }
    }
}
