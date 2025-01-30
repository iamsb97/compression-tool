import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Decompression {
    private long total_data;

    public void decompress (String filename) throws IOException {
        FileInputStream inFile = new FileInputStream(filename);
        DataInputStream dis = new DataInputStream(inFile);
        
        String outFilename = filename.split("\\.")[0] + "_decoded." + filename.split("\\.")[1];
        FileWriter outFile = new FileWriter(outFilename);
        
        Map<String, Character> prefixTable = this.readHeader(dis);
        HuffmanNode decodeTree = this.generateDecodeTree(prefixTable);
        this.readWriteText(dis, outFile, decodeTree);

        inFile.close();
        dis.close();
        outFile.close();
    }

    private HuffmanNode generateDecodeTree(Map<String, Character> preTab) {
        HuffmanNode root = new HuffmanNode();

        for (String str: preTab.keySet()) {
            this.insertNode(root, str, preTab.get(str));
        }

        return root;
    }

    private void insertNode(HuffmanNode root, String str, Character ch) {
        HuffmanNode tempNode = root;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0') {
                if (null == tempNode.getLeftNode()) {
                    tempNode.setLeftNode(new HuffmanNode());
                }
                tempNode = tempNode.getLeftNode();
            } else {
                if (null == tempNode.getRightNode()) {
                    tempNode.setRightNode(new HuffmanNode());
                }
                tempNode = tempNode.getRightNode();
            }
        }
        tempNode.setKey(ch);
    }

    private Map<String, Character> readHeader (DataInputStream file) throws IOException {
        total_data = file.readLong();
        int headerSize = file.readInt();

        Map<String, Character> preTab = new HashMap<>(headerSize);

        int b = 0;
        char key;
        char temp;
        int val_size = 0;
        int curr_bit = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < headerSize; i++) {
            key = file.readChar();
            val_size = file.readByte() & 0xFF;

            curr_bit = 0;
            sb.delete(0, sb.length());
            for (int j = 0; j < val_size; j++) {
                if (curr_bit % 8 == 0) {
                    b = file.readByte();
                    curr_bit = 0;
                }
                temp = (b & (1 << (7 - curr_bit))) == 0 ? '0' : '1';
                sb.append(temp);
                curr_bit++;
            }

            preTab.put(sb.toString(), key);
        }

        return preTab;
    }

    private void readWriteText(DataInputStream dis, FileWriter outFile, HuffmanNode root) throws IOException {
        byte b = 0;
        HuffmanNode currNode = root;

        boolean eof = false;
        char temp;
        while (!eof) {
            try {
                b = dis.readByte();
                for (int i = 7; i >= 0; i--) {
                    temp = (b & (1 << i)) == 0 ? '0' : '1';
                    if (temp == '0') {
                        currNode = currNode.getLeftNode();
                    } else {
                        currNode = currNode.getRightNode();
                    }
                    if (null == currNode.getLeftNode() && null == currNode.getRightNode()) {
                        outFile.write(currNode.getKey());
                        currNode = root;
                        total_data--;
                    }
                    if (total_data == 0) {
                        eof = true;
                        break;
                    }
                }
                
            } catch (EOFException e) {
                eof = true;
            }
        }
    }
}
