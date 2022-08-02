package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        int X = maze.toX(v);
        int Y = maze.toY(v);
        return Math.abs(X - targetX) + Math.abs(Y - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        int minNode = -1, minDist = Integer.MAX_VALUE;
        for (int i = 0; i < maze.V(); i++) {
            if (!marked[i] && distTo[i] != Integer.MAX_VALUE) {
                int priority = distTo[i] + h(i);
                if (priority < minDist) {
                    minNode = i;
                    minDist = priority;
                }
            }
        }
        return minNode;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        while (true) {
            if (s == t) {
                targetFound = true;
                announce();
                break;
            }
            for (int w : maze.adj(s)) {
                int newDist = distTo[s] + 1;
                if (newDist < distTo[w]) {
                    edgeTo[w] = s;
                    distTo[w] = newDist;
                    announce();
                }
            }
            s = findMinimumUnmarked();
            if (s == -1) break;
            marked[s] = true;
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

