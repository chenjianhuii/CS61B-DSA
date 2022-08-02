package lab11.graphs;

import javax.swing.text.StyledEditorKit.BoldAction;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private boolean finished = false;
    

    public MazeCycles(Maze m) {
        super(m);
    }

    public boolean dfs(int v, int p) {
        marked[v] = true;
        for (int w : maze.adj(v)) {
            if (w != p) {
                if (marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    return true;
                } else {
                    if (dfs(w, v) && edgeTo[w] == Integer.MAX_VALUE) {
                        if (!finished) {
                            edgeTo[w] = v;
                            announce();
                        }
                        return true;
                    }
                    if (edgeTo[w] != Integer.MAX_VALUE) {
                        finished = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void solve() {
        int N = maze.V();
        for (int i = 0; i < N; i++) {
            if (dfs(i, -1)) {
                break;
            }
        }
    }
    // Helper methods go here
}

