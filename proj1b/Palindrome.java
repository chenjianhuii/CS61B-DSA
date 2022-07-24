public class Palindrome {

    /** return a deque each node contain a character in word. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    /** return true if word is a palindrome. */
    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        while (d.size() > 1) {
            char first = d.removeFirst();
            char last = d.removeLast();
            if (first != last) {
                return false;
            }
        }
        return true;
    }

    /** return true is word is a palindrome according to comparator cc. */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        while (d.size() > 1) {
            char first = d.removeFirst();
            char last = d.removeLast();
            if (!cc.equalChars(first, last)) {
                return false;
            }
        }
        return true;
    }
}
