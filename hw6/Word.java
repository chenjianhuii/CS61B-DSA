public class Word implements Comparable<Word> {

    public String word;

    public Word(String w) {
        word = w;
    }

    @Override
    public int compareTo(Word o) {
        if (word.length() != o.word.length()) {
            return word.length() - o.word.length();
        }
        return -word.compareTo(o.word);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Word other = (Word) o;
        return word.equals(other.word);
    }

    // public static void main(String[] args) {
    //     Word w1 = new Word("abc");
    //     Word w2 = new Word("def");
    //     Word w3 = new Word("defg");
    //     System.out.println(w1.compareTo(w2)); //expected negtive
    //     System.out.println(w1.compareTo(w3)); //expected positive
    // }
    
}
