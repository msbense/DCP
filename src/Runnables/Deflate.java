package Runnables;

import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.io.*;
import org.apache.commons.io.IOUtils;


/** 
 * to decompress back into a string: String outputString = new String(result, 0, resultLength, "UTF-8"); 
*/
public class Deflate  {
	public static void compress(InputStream in, OutputStream out) {
		try {
			//Read bytes directly into a dynamic byte array
                        byte[] input = IOUtils.toByteArray(in);
                        
                        DataOutputStream intWriter = new DataOutputStream(out);
                        intWriter.writeInt(input.length);
                        //compresses the data
			Deflater compressor = new Deflater();
			compressor.setInput(input);
			compressor.finish();
			byte[] temp = new byte[input.length];
			int compressedLength = compressor.deflate(temp);
			
			byte[] output = new byte[compressedLength];
			for(int i = 0; i < output.length; i++) {
				output[i] = temp[i];
			}

			compressor.end();
			
                        IOUtils.write(output, out);
                        intWriter.close();
			//outputs the data
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void expand(InputStream in, OutputStream out) {
		try {
                        
                    DataInputStream intReader = new DataInputStream(in);
                    int length = intReader.readInt();
			//decompresses
			Inflater decompresser = new Inflater();

                        byte[] outputArray = IOUtils.toByteArray(in);
                        System.out.println("OutputArray len = " + outputArray.length);
                        decompresser.setInput(outputArray, 0, outputArray.length);
                        byte[] result = new byte[length];
                        int resultLength = decompresser.inflate(result);
                        decompresser.end();
		    
                        out.write(result);
                        intReader.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
