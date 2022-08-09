import java.util.List;
import java.util.PriorityQueue;
import java.util.LinkedList;


public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE
        if (k <= 0) {
            throw new IllegalArgumentException("k is non-positive");
        }
        K = k;
        readDict();
        getSize(boardFilePath);
        board = new char[N][M];
        visited = new boolean[N][M];
        readBoard(boardFilePath);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                dfs(i, j, "");
            }
        }
        LinkedList<String> solution = new LinkedList<>();
        while (!pq.isEmpty()) {
            solution.addFirst(pq.remove().word);
        }
        return solution;
        // System.out.printf("M = %d, N = %d\n", M, N);
        // for (int j = 0; j < board.length; j++) {
        //     for (int j2 = 0; j2 < M; j2++) {
        //         System.out.print(board[j][j2]);
        //     }
        //     System.out.print("\n");
        // }
    }

    private static void readDict() {
        dict = new Trie();
        try {
            In in = new In(dictPath);
            while (!in.isEmpty()) {
                dict.insert(in.readString());
            }
            in.close();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The dictionary file does not exits");
        }
    }

    private static void getSize(String boardFilePath) {
        In in = new In(boardFilePath);
        while (!in.isEmpty()) {
            String line = in.readLine();
            N++;
            int temp = line.length();
            M = M < temp ? temp : M;
        }
        in.close();
    }

    private static void readBoard(String boardFilePath) {
        In in = new In(boardFilePath);
        int i = 0;
        while (!in.isEmpty()) {
            String line = in.readLine();
            for (int j = 0; j < M; j++) {
                if (j < line.length()) {
                    board[i][j] = line.charAt(j);
                } else {
                    board[i][j] = ' ';
                }
            }
            i++;
        }
        in.close();
    }

    private static void dfs(int i, int j, String prefix) {
        prefix += board[i][j];
        if (!dict.hasPrefix(prefix)) {
            return;
        }
        if (dict.contains(prefix)) {
            Word w = new Word(prefix);
            if (!pq.contains(w)) {
                pq.add(w);
            }
            if (pq.size() > K) {
                pq.remove();
            }
        }
        visited[i][j] = true;
        for (int k = 0; k < 8; k++) {
            int newI = i + DIR[k][0];
            int newJ = j + DIR[k][1];
            if (inBound(newI, newJ) && !visited[newI][newJ]) {
                dfs(newI, newJ, prefix);
            }
        }
        visited[i][j] = false;
    }

    private static boolean inBound(int i, int j) {
        if (i < 0 || j < 0 || i >= N || j >= M) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        List<String> solution = solve(Integer.parseInt(args[0]), args[1]);
        for (String string : solution) {
            System.out.println(string);
        }
    }

    /**global variables */
    static int M = 0, N = 0, K;
    static char[][] board;
    static boolean[][] visited;
    static Trie dict;
    static final int[][] DIR = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
    static PriorityQueue<Word> pq = new PriorityQueue<>();
}
