import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class HuffmanEncoder {
    
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        HashMap<Character, Integer> ft = new HashMap<>();
        for (char ch : inputSymbols) {
            if (!ft.containsKey(ch)) {
                ft.put(ch, 1);
            } else {
                ft.put(ch, ft.get(ch) + 1);
            }
        }
        return ft;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HuffmanEncoder {filename}");
        } else {
            String filename = args[0];
            char[] inputSymbols = FileUtils.readFile(filename);
            Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);
            BinaryTrie bt = new BinaryTrie(frequencyTable);
            ObjectWriter ow = new ObjectWriter(filename + ".huf");
            ow.writeObject(bt);
            Map<Character, BitSequence> lookupTable = bt.buildLookupTable();
            List<BitSequence> list = new LinkedList<>();
            for (char ch : inputSymbols) {
                list.add(lookupTable.get(ch));
            }
            BitSequence all = BitSequence.assemble(list);
            ow.writeObject(all);
        }
    }
}
