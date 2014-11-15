package Runnables;

import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.*;
import java.io.*;

import Algorithms.BinaryStdIn;
import Algorithms.BinaryStdOut;
import org.apache.commons.io.IOUtils;


/** 
 * to decompress back into a string: String outputString = new String(result, 0, resultLength, "UTF-8"); 
*/
public class Deflate  {
	public static void compress(InputStream in, OutputStream out) {
		int compressedLength;
		try {
			//Read bytes directly into a dynamic byte array
                        byte[] input = IOUtils.toByteArray(in);
			
			//compresses the data
			Deflater compressor = new Deflater();
			compressor.setInput(input);
			compressor.finish();
			
			byte[] temp = new byte[input.length];
			compressedLength = compressor.deflate(temp);
			
			byte[] output = new byte[compressedLength];
			for(int i = 0; i < output.length; i++) {
				output[i] = temp[i];
			}
			
			compressor.end();
			
                        DataOutputStream headerWriter = new DataOutputStream(out);
                        headerWriter.writeInt(compressedLength);
                        System.out.println("Wrote CompressedLength");
                        headerWriter.writeInt(input.length);
                        System.out.println("Wrote len of uncompressed file");
			//write the headers
			BinaryStdOut.write(compressedLength); //length of compressed file
                        
			BinaryStdOut.write(input.length); //length of the uncompressed file
			//outputs the data
			IOUtils.write(output, out);
			
                        headerWriter.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void expand(InputStream in, OutputStream out) {
		try {
                        DataInputStream headerReader = new DataInputStream(in);
                        int compressedDataLength = headerReader.readInt();
			int length = headerReader.readInt();
                        
			//decompresses
			Inflater decompresser = new Inflater();
			byte[] output = new byte[compressedDataLength];
			IOUtils.read(in, output);
                        decompresser.setInput(output, 0, compressedDataLength);
                        byte[] result = new byte[length];
                        int resultLength = decompresser.inflate(result);
		    
                        decompresser.end();
		    
                        out.write(result);
                        
                        headerReader.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
