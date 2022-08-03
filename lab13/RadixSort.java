/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxIndex = Integer.MIN_VALUE;
        for (String s : asciis) {
            maxIndex = maxIndex < s.length() ? s.length() : maxIndex;
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = asciis[i];
        }

        for (int i = 0; i < maxIndex; i++) {
            sortHelperLSD(sorted, i, maxIndex);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     * @param maxIndex The length of longest String in asciis.
     */
    private static void sortHelperLSD(String[] asciis, int index, int maxIndex) {
        // Optional LSD helper method for required LSD radix sort
        int[] counts = new int[257];
        for (String s : asciis) {
            counts[getChar(s, index, maxIndex)]++;
        }
        
        int[] starts = new int[257];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i += 1) {
            String s = asciis[i];
            int place = starts[getChar(s, index, maxIndex)];
            sorted[place] = s;
            starts[getChar(s, index, maxIndex)] += 1;
        }

        for (int i = 0; i < sorted.length; i++) {
            asciis[i] = sorted[i];
        }
    }

    private static int getChar(String s, int index, int maxIndex) {
        if (s.length() < maxIndex - index) {
            return 0;
        }
        return s.charAt(maxIndex - index - 1) + 1;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    // public static void main(String[] args) {
    //     String[] s = new String[] {"a", "b", "d", "e", "c"};
    //     for (String ss : sort(s)) {
    //         System.out.println(ss);
    //     }
    // }
}
