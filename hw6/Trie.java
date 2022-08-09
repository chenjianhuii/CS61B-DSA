import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import org.junit.Test;

public class Trie {
    
    private class Node {
     
        boolean exist;
        HashMap<Character, Node> next;
        
        Node() {
            exist = false;
            next = new HashMap<>();
        }
    }

    private Node root;

    public Trie() {
        root = new Node();
    }

    public void insert(String w) {
        int N = w.length();
        Node p = root;
        for (int i = 0; i < N; i++) {
            char ch = w.charAt(i);
            if (!p.next.containsKey(ch)) {
                p.next.put(ch, new Node());
            }
            p = p.next.get(ch);
        }
        p.exist = true;
    }

    /**return -1 if trie doesn't contains word w
     * return 0 if contains only the prefix w
     * return 1 if contains entire word w
    */
    private int find(String w) {
        Node p = root;
        int N = w.length();
        for (int i = 0; i < N; i++) {
            char ch = w.charAt(i);
            if (!p.next.containsKey(ch)) {
                return -1;
            }
            p = p.next.get(ch);
        }
        return p.exist ? 1 : 0;
    }

    public boolean hasPrefix(String w) {
        return find(w) >= 0;
    }

    public boolean contains(String w) {
        return find(w) == 1;
    }

    @Test
    public void test() {
        Trie t = new Trie();
        t.insert("abcd");
        t.insert("abdf");
        t.insert("bcd");
        assertTrue(t.hasPrefix("ab"));
        assertTrue(t.contains("abcd"));
        assertFalse(t.contains("ab"));
        assertTrue(t.hasPrefix(""));
        assertFalse(t.contains(""));
    }

}
