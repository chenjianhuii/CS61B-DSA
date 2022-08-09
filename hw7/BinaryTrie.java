import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {

    private Node root;
    private Map<Character, BitSequence> lookupTable;
    
    private static class Node implements Comparable<Node>, Serializable {

        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int f, Node l, Node r) {
            this.ch = ch;
            freq = f;
            left = l;
            right = r;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }
        
        @Override
        public int compareTo(Node o) {
            return freq - o.freq;
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> e : frequencyTable.entrySet()) {
            pq.add(new Node(e.getKey(), e.getValue(), null, null));
        }
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            pq.add(new Node('\0', left.freq + right.freq, left, right));
        }
        root = pq.poll();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node p = root;
        int s = querySequence.length();
        for (int i = 0; i < s; i++) {
            if (querySequence.bitAt(i) == 0) {
                p = p.left;
            } else {
                p = p.right;
            }
            if (p.isLeaf()) {
                return new Match(querySequence.firstNBits(i + 1), p.ch);
            }
        }
        return null;
    }

    public Map<Character, BitSequence> buildLookupTable() {
        lookupTable = new HashMap<>();
        dfs(root, new BitSequence());
        return lookupTable;
    }

    private void dfs(Node p, BitSequence prefix) {
        if (p.isLeaf()) {
            lookupTable.put(p.ch, prefix);
        } else {
            dfs(p.left, prefix.appended(0));
            dfs(p.right, prefix.appended(1));
        }
    }
}
