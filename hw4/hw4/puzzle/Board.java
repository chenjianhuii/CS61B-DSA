package hw4.puzzle;

import java.util.Arrays;
import java.util.LinkedList;

public class Board implements WorldState {

    private int[][] board;
    private static final int[][] DIR = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private boolean inBound(int i, int j) {
        if (i < 0 || i >= size() || j < 0 || j >= size()) {
            return false;
        }
        return true;
    }

    /**
     * Return a new Board instance with the tileAt(i, j) and tileAt(newI, newJ) swapped.
     * @param i
     * @param j
     * @param newI
     * @param newJ
     */
    private WorldState swap(int i, int j, int newI, int newJ) {
        if (inBound(newI, newJ)) {
            int N = size();
            int[][] newBoard = new int[N][N];
            for (int k = 0; k < newBoard.length; k++) {
                newBoard[k] = Arrays.copyOf(board[k], board[k].length);
            }
            int temp = newBoard[i][j];
            newBoard[i][j] = newBoard[newI][newJ];
            newBoard[newI][newJ] = temp;
            return new Board(newBoard);
        }
        return null;
    }

    public Board(int[][] tiles) {
        board = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            board[i] = Arrays.copyOf(tiles[i], tiles[i].length);
        }
    }

    public int tileAt(int i, int j) {
        if (!inBound(i, j)) {
            throw new java.lang.IndexOutOfBoundsException("Invalid position!");
        }
        return board[i][j];
    }

    public int size() {
        return board[0].length;
    }

    /** Returns the string representation of the board. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || this.getClass() != y.getClass()) {
            return false;
        }

        Board other = (Board) y;
        if (size() != other.size()) {
            return false;
        }
        int N = size();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tileAt(i, j) != other.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0, N = size();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result = result * 15 + tileAt(i, j);
            }
        }
        return result & 0x7fffffff;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public Iterable<WorldState> neighbors() {
        LinkedList<WorldState> l = new LinkedList<>();
        int row = 0, col = 0;
        boolean found = false;
        for (int i = 0; i < size() && !found; i++) {
            for (int j = 0; j < size() && !found; j++) {
                if (tileAt(i, j) == 0) {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
        }
        for (int[] d : DIR) {
            int newRow = row + d[0];
            int newCol = col + d[1];
            WorldState nextState = swap(row, col, newRow, newCol);
            if (nextState != null) {
                l.add(nextState);
            }
        }
        return l;
    }

    public int hamming() {
        int N = size(), dist = 0;
        for (int i = 0; i < N * N; i++) {
            int tile = tileAt(i / N, i % N);
            if (tile > 0 && tile != i + 1) {
                dist++;
            }
        }
        return dist;
    }

    public int manhattan() {
        int N = size(), dist = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tile = tileAt(i, j);
                if (tile > 0) {
                    dist += Math.abs((tile - 1) / N - i) + Math.abs((tile - 1) % N - j);
                }
            }
        }
        return dist;
    }
    
    // public static void main(String[] args) {
        // int[][] initial = new int[][] {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        // Board b = new Board(initial);
        // System.out.println("Hamming distance is: " + b.hamming());
        // System.out.println("Manhattan distance is: " + b.manhattan());

        /**Array equals doesn't compare each item. 
        int[] a = new int[] {1, 2, 3};
        int[] b = new int[] {1, 2, 3};
        System.out.println(a.equals(b)); // return false
        */
    // }
}
