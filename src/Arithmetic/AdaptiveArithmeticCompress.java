package Arithmetic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class AdaptiveArithmeticCompress {
	
	public static void Comp(InputStream CompressionInputStream, OutputStream CompressionOutputStream) {
            BitOutputStream Cout = new BitOutputStream(CompressionOutputStream);
            try {
            	compress(CompressionInputStream, Cout);
            }
            catch (IOException e){
                System.out.println("IO Exception in AdaptivearithmeticCompress.compress");
            }  
            finally{
                try {
                        Cout.close();
                        CompressionInputStream.close();
                    } catch (IOException ex) {
                        System.out.println("Error closing streams for AruthmeticComp");
                    }
                    
            }
        }
        
        public static void main(String[] args) throws IOException {
		// Show what command line arguments to use
		if (args.length == 0) {
			System.err.println("Usage: java ArithmeticCompress InputFile OutputFile");
			System.exit(1);
			return;
		}
		
		// Otherwise, compress
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
		BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
		try {
			compress(in, out);
		} finally {
			out.close();
			in.close();
		}
	}
	
	
	public static void compress(InputStream in, BitOutputStream out) throws IOException {
		FrequencyTable freq = new SimpleFrequencyTable(new FlatFrequencyTable(257));  // Initialize with all symbol frequencies at 1
		ArithmeticEncoder enc = new ArithmeticEncoder(out);
		while (true) {
			int b = in.read();
			if (b == -1){	
                            break;    
                        }
			enc.write(freq, b);
			freq.increment(b);
		}
		enc.write(freq, 256);  // EOF
		enc.finish();
	}
	
}