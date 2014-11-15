package Arithmetic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AdaptiveArithmeticDecompress {
	
        public static void Decomp(InputStream DecompressionInputStream, OutputStream DecompressionOutputStream){
            
            BitInputStream Din = new BitInputStream(DecompressionInputStream);
            try {
                decompress(Din, DecompressionOutputStream);
            } catch (IOException ex) {
                System.out.println("IO Exception in AdaptivearithmeticCompress.decompress");
            }
            finally{
                try {
                    Din.close();
                    DecompressionOutputStream.close();
                } catch (IOException ex) {
                    System.out.println("Error closing streams for ArithmeticDecomp");
                }
            }
        }
    
	public static void main(String[] args) throws IOException {
		// Show what command line arguments to use
		if (args.length == 0) {
			System.err.println("Usage: java ArithmeticDecompress InputFile OutputFile");
			System.exit(1);
			return;
		}
		
		// Otherwise, decompress
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));
		try {
			decompress(in, out);
		} finally {
			out.close();
			in.close();
		}
	}
	
	
	public static void decompress(BitInputStream in, OutputStream out) throws IOException {
		FrequencyTable freq = new SimpleFrequencyTable(new FlatFrequencyTable(257));  // Initialize with all symbol frequencies at 1
		ArithmeticDecoder dec = new ArithmeticDecoder(in);
                
		while (true) {
			int symbol = dec.read(freq);
			if (symbol == 256)  // EOF symbol
				break;
			out.write(symbol);
                        
			freq.increment(symbol);
                        
		}
	}
	
}