package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(8);
        for (int i = 0; i < 10; i++) {
            arb.enqueue(i);
            assertEquals((Integer) i, arb.dequeue());
        }
        for (int i = 0; i < 8; i++) {
            arb.enqueue(i);
        }
        int[] expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        int[] actual = new int[8];
        for (int i : arb) {
            actual[i] = i;
        }
        assertArrayEquals(expected, actual);
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
