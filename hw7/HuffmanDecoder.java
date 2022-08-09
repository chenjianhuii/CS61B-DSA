import java.lang.reflect.Array;
import java.util.ArrayList;

public class HuffmanDecoder {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java HuffmanDecoder {input} {output}");
        } else {
            ObjectReader or = new ObjectReader(args[0]);
            Object x = or.readObject();
            Object y = or.readObject();
            BinaryTrie bt = (BinaryTrie) x;
            BitSequence bs = (BitSequence) y;
            ArrayList<Character> chars = new ArrayList<>();
            while (bs.length() > 0) {
                Match m = bt.longestPrefixMatch(bs);
                chars.add(m.getSymbol());
                bs = bs.allButFirstNBits(m.getSequence().length());
            }
            char[] output = new char[chars.size()];
            for (int i = 0; i < chars.size(); i++) {
                output[i] = chars.get(i);
            }
            FileUtils.writeCharArray(args[1], output);
        }
    }
    
}
