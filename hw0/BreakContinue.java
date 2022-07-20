public class BreakContinue {
    public static void windowPosSum(int[] a, int n) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0) continue;
            for (int j = 1; j <= 3; j++) {
                if (i + j < a.length) {
                    a[i] += a[i+j];
                } else {
                    break;
                } 
            } 
        }
    }
    
    public static void main(String[] args) {
        int[] a = {1, 2, -3, 4, 5, 4};
        int n = 3;
        windowPosSum(a, n);
    
        // Should print 4, 8, -3, 13, 9, 4
        System.out.println(java.util.Arrays.toString(a));
    }
}