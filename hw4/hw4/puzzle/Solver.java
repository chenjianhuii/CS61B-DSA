package hw4.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    
    private class SearchNode implements Comparable<SearchNode> {
        
        WorldState state;
        int numOfMove;
        SearchNode prev;

        SearchNode(WorldState s, int n, SearchNode p) {
            state = s;
            numOfMove = n;
            prev = p;
        }

        private int cachedH(WorldState s) {
            int thisH;
            if (h.containsKey(s)) {
                thisH = h.get(s);
            } else {
                thisH = s.estimatedDistanceToGoal();
                h.put(s, thisH);
            }
            return thisH;
        }

        @Override
        public int compareTo(SearchNode o) {
            int thisPriority = numOfMove + cachedH(state);
            int otherPriority = o.numOfMove + cachedH(o.state);
            return thisPriority - otherPriority;
        }


    }

    private int minNumOfMove;
    private Stack<WorldState> S = new Stack<>();
    private Map<WorldState, Integer> h = new HashMap<>();
    private Set<WorldState> visited = new HashSet<>();

    /** Used for CommonBugDetector.java */
    // public int numInQueue = 0;

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        while (!pq.isEmpty()) {
            SearchNode cur = pq.delMin();
            visited.add(cur.state); //Only when the node deque can we mark it visited
            if (cur.state.isGoal()) {
                minNumOfMove = cur.numOfMove;
                while (cur != null) {
                    S.push(cur.state);
                    cur = cur.prev;
                }
                return;
            } else {
                for (WorldState s : cur.state.neighbors()) {
                    if (!visited.contains(s)) {
                        pq.insert(new SearchNode(s, cur.numOfMove + 1, cur));
                        // numInQueue++;
                    }
                }
            }
        }

    }

    public int moves() {
        return minNumOfMove;
    }

    public Iterable<WorldState> solution() {
        return S;
    }
}
