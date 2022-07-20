public class FindMax {
    /** Returns the maximum value from m. */
    public static int max(int[] m) {
        int min = m[0];
        for (int i = 1; i < m.length; i++) {
            if (m[i] > min){
                min = m[i];
            }
        }
        return min;
    }
    public static void main(String[] args) {
       int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};      
       System.out.println(max(numbers));
    }
}