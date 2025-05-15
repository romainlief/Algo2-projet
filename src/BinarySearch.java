
import java.util.List;
import baseClasses.Connexion;

public class BinarySearch {

    /**
     * @brief Find the index of the first connexion that departs after the given
     *        time.
     * @param connexions    The list of connexions to search.
     * @param userStartTime The time to search for.
     * @return The index of the first connexion that departs after the given time.
     */
    public static int findStartIndex(List<Connexion> connexions, int userStartTime) {
        int left = 0, right = connexions.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (connexions.get(mid).getDepartureTime() >= userStartTime) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}