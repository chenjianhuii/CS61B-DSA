package lab11.graphs;

import edu.princeton.cs.algs4.In;

import java.util.Queue;
import java.util.LinkedList;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Queue<Integer> fringe;
    private boolean found = false;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        fringe = new LinkedList<>();
        // Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        fringe.offer(s);
        marked[s] = true;
        announce();
        while (!fringe.isEmpty()) {
            int v = fringe.poll();
            if (v == t) {
                found = true;
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    distTo[w] = distTo[v] + 1;
                    edgeTo[w] = v;
                    marked[w] = true;
                    fringe.offer(w);
                    announce();
                }
            }
        }
    }


    @Override
    public void solve() {
         bfs();
    }
}

