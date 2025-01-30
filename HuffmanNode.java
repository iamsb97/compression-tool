import java.util.Comparator;

public class HuffmanNode implements Comparable<HuffmanNode> {
    private char key;
    private long value;
    private HuffmanNode leftNode;
    private HuffmanNode rightNode;

    public HuffmanNode () {
        this.key = '\0';
        this.value = 0;
        this.leftNode = null;
        this.rightNode = null;
    }

    public HuffmanNode (char key, long value) {
        this.key = key;
        this.value = value;
        this.leftNode = null;
        this.rightNode = null;
    }

    public HuffmanNode (long val, HuffmanNode left, HuffmanNode right) {
        this.key = '\0';
        this.value = val;
        this.leftNode = left;
        this.rightNode = right;
    }

    public void setKey (char key) {
        this.key = key;
    }

    public void setValue (long val) {
        this.value = val;
    }

    public void setLeftNode (HuffmanNode left) {
        this.leftNode = left;
    }

    public void setRightNode (HuffmanNode right) {
        this.rightNode = right;
    }

    public char getKey () { 
        return this.key;
    }

    public long getValue () {
        return this.value;
    }

    public HuffmanNode getLeftNode () {
        return this.leftNode;
    }

    public HuffmanNode getRightNode () {
        return this.rightNode;
    }

    @Override
    public int compareTo (HuffmanNode h) {
         return (int)(this.getValue() - h.getValue());
    }

    public static Comparator<HuffmanNode> HuffmanNodeComparator = new Comparator<HuffmanNode>() {
        
        @Override
        public int compare (HuffmanNode h1, HuffmanNode h2) {
            return (int)(h1.getValue() - h2.getValue());
        }
    };

}