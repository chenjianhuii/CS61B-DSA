import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        String msg = "";
        for (int i = 0; i < 100; i++) {
            double rand = StdRandom.uniform();
            assertEquals(sad.size(), ads.size());
            if (rand < 0.25) {
                sad.addFirst(i);
                ads.addFirst(i);
                msg += "addFirst(" + i + ")\n";
            } else if (rand < 0.5) {
                sad.addLast(i);
                ads.addLast(i);
                msg += "addLast(" + i + ")\n";
            } else if (rand < 0.75) {
                if (!sad.isEmpty()) {
                    msg += "removeFirst()\n";
                    assertEquals(msg, ads.removeFirst(), sad.removeFirst());
                }
            } else {
                if (!sad.isEmpty()) {
                    msg += "removeLast()\n";
                    assertEquals(msg, ads.removeLast(), sad.removeLast());
                }
            }
        }
    }
}
