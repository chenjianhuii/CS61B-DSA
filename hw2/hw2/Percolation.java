package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int numOpen, N;
    private boolean isPercolate;
    private int[][] site; // 0 - closed, 1 - opened, 2 - fulled, 3 - bottom
    private static final int[][] DIR = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    private WeightedQuickUnionUF uf;

    /** return true if (row, col) is a valid position. */
    private boolean valid(int row, int col) {
        return 0 <= row && row < N && 0 <= col && col < N;
    }

    /** return the index of (row, col) in disjoint set. */
    private int index(int row, int col) {
        return row * N + col;
    }

    /** return true if site(row, col) is closed. */
    private boolean isClosed(int s) {
        return site[s / N][s % N] == 0;
    }

    /** return true if site(row, col) is closed. */
    private boolean isOpen(int s) {
        return site[s / N][s % N] == 1;
    }

    /** return true if site(row, col) is closed. */
    private boolean isFull(int s) {
        return site[s / N][s % N] == 2;
    }

    /** return true if site(row, col) is connected to the bottom. */
    private boolean isBottom(int s) {
        return site[s / N][s % N] == 3;
    }

    /** set the status of site(row, col) to open. */
    private void setOpen(int s) {
        site[s / N][s % N] = 1;
    }
    /** set the status of site(row, col) to full if it is connected to top row. */
    private void setFull(int s) {
        site[s / N][s % N] = 2;
    }
    /** set the status of site(row, col) to bottom if it is connected to bottom row. */
    private void setBottom(int s) {
        site[s / N][s % N] = 3;
    }

    /** create N-by-N grid, with all sites initially blocked. */
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("N should be great than 0");
        }
        this.N = N;
        numOpen = 0;
        isPercolate = false;
        site = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                site[i][j] = 0;
            }
        }
        uf = new WeightedQuickUnionUF(N * N);
    }

    /** open the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        if (!valid(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Invalid position!");
        }
        int s = uf.find(index(row, col));
        if (isClosed(s)) {
            numOpen++;
            if (N == 1) {
                isPercolate = true;
                return;
            }
            if (row == 0) {
                setFull(s);
            } else if (row == N - 1) {
                setBottom(s);
            } else {
                setOpen(s);
            }
            for (int[] d : DIR) {
                int newRow = row + d[0];
                int newCol = col + d[1];
                if (valid(newRow, newCol)) {
                    int s2 = uf.find(index(newRow, newCol));
                    if (s != s2 && !isClosed(s2)) {
                        if (isFull(s)) {
                            if (isBottom(s2)) {
                                isPercolate = true;
                            }
                            uf.union(s, s2);
                            s = uf.find(index(row, col));
                            setFull(s);
                        } else if (isBottom(s)) {
                            if (isFull(s2)) {
                                isPercolate = true;
                                uf.union(s, s2);
                                s = uf.find(index(row, col));
                                setFull(s);
                            } else {
                                uf.union(s, s2);
                                s = uf.find(index(row, col));
                                setBottom(s);
                            }
                        } else {
                            if (isFull(s2)) {
                                setFull(s);
                            }
                            if (isBottom(s2)) {
                                setBottom(s);
                            }
                            uf.union(s, s2);
                            s = uf.find(index(row, col));
                        }
                    }
                }
            }
        }
    }

    /** is the site (row, col) open?. */
    public boolean isOpen(int row, int col) {
        if (!valid(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Invalid position!");
        }
        int s = uf.find(index(row, col));
        return site[s / N][s % N] > 0;
    }

    /** is the site (row, col) full?. */
    public boolean isFull(int row, int col) {
        if (!valid(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Invalid position!");
        }
        int s = uf.find(index(row, col));
        return site[s / N][s % N] == 2;
    }

    /** number of open sites. */
    public int numberOfOpenSites() {
        return numOpen;
    }

    /** does the system percolate?. */
    public boolean percolates() {
        return isPercolate;
    }
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(0, 0);
        p.open(1, 1);
        p.open(2, 2);
        p.open(1, 0);
//        p.open(1, 2);
        System.out.println(p.percolates());
    }   // use for unit testing (not required)
}
