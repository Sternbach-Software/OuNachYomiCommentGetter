

public class A {
    public static void main(String[] args) {
        for (int x = 1; x < 11; x++) {
            System.out.println("X is: " + x);
        }
        int y = 1;
        while(y<11){
            System.out.println(y);
            y++;
        }
    }
    /**
     * <p>Finds the n-th index within a String, handling {@code null}.
     * This method uses {@link String#indexOf(String)} if possible.</p>
     * <p>Note that matches may overlap<p>
     *
     * <p>A {@code null} CharSequence will return {@code -1}.</p>
     *
     * @param str  the CharSequence to check, may be null
     * @param searchStr  the CharSequence to find, may be null
     * @param ordinal  the n-th {@code searchStr} to find, overlapping matches are allowed.
     * @param lastIndex true if lastOrdinalIndexOf() otherwise false if ordinalIndexOf()
     * @return the n-th index of the search CharSequence,
     *  {@code -1} if no match or {@code null} string input
     */
    private static int ordinalIndexOf(final String str, final String searchStr, final int ordinal, final boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        // set the initial index beyond the end of the string
        // this is to allow for the initial index decrement/increment
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = str.lastIndexOf(searchStr, index - 1); // step backwards thru string
            } else {
                index = str.indexOf(searchStr, index + 1); // step forwards through string
            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }
}
