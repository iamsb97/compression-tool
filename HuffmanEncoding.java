import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanEncoding {
    private Map<Character, Long> frequency;
    private PriorityQueue<HuffmanNode> tree;
    private Map<Character, String> prefix;

    public HuffmanEncoding(Map<Character, Long> map) {
        frequency = map;
        tree = new PriorityQueue<HuffmanNode>(HuffmanNode.HuffmanNodeComparator);
        prefix = new HashMap<>();
    }    

    public PriorityQueue<HuffmanNode> getHuffmanTree () {
        return this.tree;
    }

    public Map<Character, String> getPrefixTable () {
        return this.prefix;
    }

    private void createTree () {
        for (Character c: frequency.keySet()) {
            HuffmanNode node = new HuffmanNode(c, frequency.get(c));
            tree.add(node);
        }
    }

    private void buildTree () {
        if (tree.size() == 1) {
            return;
        }

        this.createTree();
        while (tree.size() > 1) {
            HuffmanNode node1 = tree.poll();
            HuffmanNode node2 = tree.poll();
            HuffmanNode parNode = new HuffmanNode(node1.getValue() + node2.getValue(), node1, node2);
            tree.add(parNode);
        }
    }

    private void traverseToLeaf (HuffmanNode node, StringBuilder s) {
        if (null == node) {
            return;
        }
        if (null == node.getLeftNode() && null == node.getRightNode()) {
            String huffCode = s.toString();
            prefix.put(node.getKey(), huffCode);
        } 
        traverseToLeaf(node.getLeftNode(), s.append('0'));
        s.deleteCharAt(s.length()-1);
        traverseToLeaf(node.getRightNode(), s.append('1'));
        s.deleteCharAt(s.length()-1);
    }

    private void generatePrefixTable () {
        if (prefix.size() > 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        traverseToLeaf(tree.peek(), sb);
    }

    public void encode () {
        this.buildTree();
        this.generatePrefixTable();
    }
}