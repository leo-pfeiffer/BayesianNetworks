package utils;

import java.util.HashSet;
import java.util.Set;

public class SetUtils {
    /**
     * Returns the union of two sets.
     * @param a first set
     * @param b second set
     * @return union of the two sets
     * */
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>();
        result.addAll(a);
        result.addAll(b);
        return result;
    }

    /**
     * Returns the intersection of two sets.
     * @param a first set
     * @param b second set
     * @return intersection of the two sets
     * */
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }
}
