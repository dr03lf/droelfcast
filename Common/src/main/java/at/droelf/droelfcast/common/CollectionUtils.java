package at.droelf.droelfcast.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods for Collections
 */
public class CollectionUtils {

    private CollectionUtils() {
        // Intentionally empty
    }

    /**
     * Checks to see if an array is empty
     * <p>
     * An empty array is one that is null or has zero elements.  Everything else is
     * considered non-empty.
     * </p>
     *
     * @param array The array to check
     * @param <T>   The type of the array
     * @return true if the collection is null or empty, false otherwise
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Checks to see if an array is not empty
     * <p>
     * An empty array is one that is null or has zero elements.  Everything else is
     * considered non-empty.
     * </p>
     *
     * @param array The array to check
     * @param <T>   The type of the array
     * @return true if the collection not empty, false otherwise
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    /**
     * Checks to see if a collection is empty
     * <p>
     * An empty collection is one that is null or has zero elements.  Everything else is
     * considered non-empty.
     * </p>
     *
     * @param collection The collection to check
     * @param <T>        The type of the collection
     * @return true if the collection is null or empty, false otherwise
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Checks to see if a collection is populated
     * <p>
     * An empty collection is one that is null or has zero elements.  Everything else is
     * considered non-empty.
     * </p>
     *
     * @param collection The collection to check
     * @param <T>        The type of the collection
     * @return true if the collection is not null and has at least one element, false otherwise
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * Ensures that we always have an empty list rather than null
     *
     * @param list The list to check
     * @param <T>  The type of objects in the list
     * @return An empty and modifiable list if the argument was null, the original list otherwise
     */
    public static <T> List<T> ensureEmpty(List<T> list) {

        if (isEmpty(list)) {
            return new ArrayList<>();
        }

        return list;
    }

    /**
     * Returns a wrapper on the specified list which throws an
     * {@code UnsupportedOperationException} whenever an attempt is made to
     * modify the list.
     * <p>
     * This is a thin wrapper around
     * {@link #unmodifiableList(List)} which
     * will ensure that it won't throw an {@code NullPointerException} if the supplied list is
     * null.
     * </p>
     *
     * @param list the list to wrap in an unmodifiable list.
     * @return an unmodifiable List.
     */
    @SuppressWarnings({"unchecked", "JavaDoc"})
    public static <T> List<T> unmodifiableList(List<T> list) {

        return Collections.unmodifiableList(CollectionUtils.ensureEmpty(list));
    }

    /**
     * Combines a number of lists of the specified type into one list by using
     * {@link Collections#addAll(Collection, Object[])}
     *
     * @param lists The lists to combine
     * @param <T>   The type of the list
     * @return A list that contains all of the values from the parameters. If no values were found
     * to combine then this will return an empty list.
     */
    @SafeVarargs
    public static <T> List<T> combineLists(List<T>... lists) {

        ArrayList<T> combinedList = new ArrayList<>();

        if (lists != null) {
            for (List<T> list : lists) {
                if (CollectionUtils.isNotEmpty(list)) {
                    combinedList.addAll(list);
                }
            }
        }
        return combinedList;
    }

    public static <T> O<T> getFirstElement(List<T> list){
        if(!CollectionUtils.isEmpty(list) && list.size() > 0){
            return O.c(list.get(0));
        }
        return O.n();
    }

}