import java.io.IOException;

public class compression_tool {

    public static void main (String[] args) {
        if (args.length < 2) {
            System.err.println("Invalid number of arguments!");
        }

        String operation = args[0];
        String filename = args[1];

        try {

            switch (operation) {
                case "compress":
                    Compression comp = new Compression();
                    comp.compress(filename);
                    break;
                
                case "decompress":
                    Decompression decomp = new Decompression();
                    decomp.decompress(filename);
                    break;

                default:
                    System.err.println("Incorrect usage!");
                    break;
            }

        } catch (IOException e) {
            System.err.println(e);
        }
        
    } 
}
