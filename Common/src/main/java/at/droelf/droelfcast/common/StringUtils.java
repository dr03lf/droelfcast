package at.droelf.droelfcast.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods for String operations
 */
public class StringUtils {

    /**
     * Constant defining the empty string ""
     */
    public static final String EMPTY_STRING = "";

    /**
     * Constant defining the line separator for the system we are running on.
     * Note that System.lineSeparator() will cause dalvik errors on older Android versions so please don't change this
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private StringUtils() {
        // Intentionally empty
    }

    /**
     * Determines whether a String has length or not
     * <p>
     * A string is defined as having length if it is not null and if its trimmed length is
     * greater than 0.
     * </p>
     *
     * @param s The string to check
     * @return true if the string has length, false otherwise.
     */
    public static boolean hasLength(String s) {
        return s != null && s.trim().length() > 0;
    }

    public static boolean hasLength(String... s){
        for(String string : s){
            if(!hasLength(string)){
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether a String has zero length or not.
     * <p>
     * A string is defined as having zero length if it is null or if its trimmed length is 0.
     * This method is the opposite of {@link #hasLength(String)}
     * </p>
     *
     * @param s The string to check
     * @return true if the string is empty, false otherwise.
     */
    public static boolean isEmpty(String s) {
        return !hasLength(s);
    }

    /**
     * Combines Strings into a single comma-separated string.
     *
     * @param values The Strings to concatenate into a single comma-separated String
     * @return A comma separated string of the given strings
     */
    public static String toCsvString(String... values) {

        List<String> valuesAsArray = values == null
                ? null
                : Arrays.asList(values);

        return toCsvString(valuesAsArray);
    }

    /**
     * Combines Strings into a single comma-separated string.
     * <br>
     * Note that this method doesn't escape commas
     *
     * @param values The Strings to concatenate into a single comma-separated String
     * @return A comma separated string of the given strings or null if values was null
     */
    public static String toCsvString(List<String> values) {

        String csvString = null;

        if (values != null) {

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < values.size(); i++) {

                String currentValue = values.get(i);

                if (StringUtils.hasLength(currentValue)) {
                    stringBuilder.append(values.get(i));

                    if (i < values.size() - 1) {
                        stringBuilder.append(",");
                    }
                }
            }

            csvString = stringBuilder.toString();
        }

        return csvString;
    }

    /**
     * Combines numbers into a single comma-separated string.
     * <br>
     * Note that this method doesn't escape commas
     *
     * @param values The numbers to concatenate into a single comma-separated String
     * @return A comma separated string of the given strings or null if values was null
     */
    public static String toCsvStringNumber(Number... values) {

        List<Number> valuesAsArray = values == null
                ? null
                : Arrays.asList(values);

        return toCsvStringNumber(valuesAsArray);
    }

    /**
     * Combines numbers into a single comma-separated string.
     * <br>
     * Note that this method doesn't escape commas
     *
     * @param values The numbers to concatenate into a single comma-separated String
     * @return A comma separated string of the given strings or null if values was null
     */
    public static String toCsvStringNumber(List<Number> values) {

        List<String> numbersAsStrings = null;

        if (values != null) {
            numbersAsStrings = new ArrayList<>();

            for (Number number : values) {
                if (number != null) {
                    /**
                     * Note: here we are relying on the fact that subclasses of Number return their numeric
                     * value in toString()
                     */
                    numbersAsStrings.add(number.toString());
                }
            }
        }

        return toCsvString(numbersAsStrings);
    }



    /**
     * Converts a single comma-separated string and returns a List of Strings split on the comma
     *
     * @param csvString The single comma-separated string
     * @return List of type String, returns empty if splitting fails
     */
    public static List<String> fromCsv(String csvString) {

        if (StringUtils.hasLength(csvString)) {
            String[] values = csvString.split(",");

            List<String> list = new ArrayList<>();

            for (String string : values) {
                if (StringUtils.hasLength(string)) {
                    list.add(string);
                }
            }

            return CollectionUtils.unmodifiableList(list);
        }

        return CollectionUtils.unmodifiableList(new ArrayList<String>(0));
    }

    /**
     * Converts the specified values into a comma-separated string (csv)
     *
     * @param values The values to convert into a csv string
     * @return The csv string or null if the arguments were null
     */
    public static String toCsvString(long... values) {

        if (values == null) {
            return null;
        }

        ArrayList<Number> valuesList = new ArrayList<>();

        for (long value : values) {
            valuesList.add(value);
        }

        return toCsvStringNumber(valuesList);
    }

    public static String ensureEmpty(String string){
        if(!StringUtils.hasLength(string)){
            return EMPTY_STRING;
        }
        return string;
    }

    public static String removeSpecialCharacters(String str) {
        return str.replaceAll("[^a-zA-Z0-9_-]", "");
    }
}