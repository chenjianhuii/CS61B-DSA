public class OffByN implements CharacterComparator {

    private int N;
    public OffByN(int n) {
        N = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        return diff * diff == N * N ? true : false;
    }
}
