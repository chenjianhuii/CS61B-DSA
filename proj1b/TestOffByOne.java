import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testEqualChars() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('r', 'q'));
        assertTrue(offByOne.equalChars('%', '&'));
        assertFalse(offByOne.equalChars('a', 'e'));
        assertFalse(offByOne.equalChars('a', 'a'));
    }

//    @Test
//    public void testOffByN() {
//        OffByN offBy5 = new OffByN(5);
//        assertTrue(offBy5.equalChars('a', 'f'));
//        assertTrue(offBy5.equalChars('f', 'a'));
//        assertFalse(offBy5.equalChars('f', 'h'));
//    }
}
