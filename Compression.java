import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Compression {
    private long total_data = 0;
    
    public void compress (String filename) throws IOException {
        FileReader file1 = new FileReader(filename);
        Map<Character, Long> frequency = this.frequencyCount(file1);
        file1.close();
        
        HuffmanEncoding huffObj = new HuffmanEncoding(frequency);
        huffObj.encode();
        Map<Character, String> prefixTable = huffObj.getPrefixTable();

        String outFilename = filename + ".sc";
        FileReader file2 = new FileReader(filename);
        FileOutputStream outFile = new FileOutputStream(outFilename);
        this.write(file2, outFile, prefixTable);
        file2.close();
        outFile.close();
    }

    private Map<Character, Long> frequencyCount (FileReader file) throws IOException {
        Map<Character, Long> freqCount = new HashMap<>();

        int currChar;
        while ((currChar = file.read()) != -1) { 
            total_data++;
            char c = (char)currChar;
            if (freqCount.containsKey(c)){
                freqCount.put(c, freqCount.get(c) + 1);
            } else {
                freqCount.put(c, 1L);
            }
        }

        return freqCount;
    }

    private void write (FileReader inFile, FileOutputStream outFile, Map<Character, String> preTab) throws IOException {
        outFile.write(this.writeHeader(preTab));
        outFile.write(this.writeText(inFile, preTab));
    }

    private byte[] writeHeader (Map<Character, String> preTab) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteStream);

        dos.writeLong(total_data);
        dos.writeInt(preTab.size());
        for (Character key: preTab.keySet()) {
            dos.writeChar(key);
            String val = preTab.get(key);
            dos.writeByte(val.length());
            
            byte b = 0;
            int b_len = 0;
            for (int i = 0; i < val.length(); i++) {
                b |= (val.charAt(i) == '1' ? 1 : 0) << (7 - b_len);
                b_len++;
                if (b_len == 8) {
                    dos.writeByte(b);
                    b = 0;
                    b_len = 0;
                }
            }

            if (b_len > 0) {
                dos.writeByte(b);
            }
        }

        dos.flush();
        return byteStream.toByteArray();
    }

    private byte[] writeText (FileReader inFile, Map<Character, String> preTab) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte b = 0;
        int b_len = 0;

        int currChar;
        while ((currChar = inFile.read()) != -1) {
            char ch = (char)currChar;
            String val = preTab.get(ch);

            for (int i = 0; i < val.length(); i++) {
                b |= (val.charAt(i) == '1' ? 1 : 0) << (7 - b_len);
                b_len++;
                if (b_len == 8) {
                    byteStream.write(b);
                    b = 0;
                    b_len = 0;
                }
            }
        }

        while ((b_len % 8) > 0) {
            b |= 0 << (7 - b_len);
            b_len++;
        }
        byteStream.write(b);

        return byteStream.toByteArray();
    }
}
