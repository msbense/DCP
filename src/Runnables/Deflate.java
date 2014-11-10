package Runnables;

import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.*;
import java.io.*;


/** 
 * to decompress back into a string: String outputString = new String(result, 0, resultLength, "UTF-8"); 
*/
public class Deflate  {
	private static int compressedDataLength = 0;
	private static byte[] output = new byte[100];
	public static void compress(BufferedInputStream in, BufferedOutputStream out) {
		try {
			//make string of data and convert to bits
			String message = "";
			while(in.read() != -1) {
				message += in.read();
			}
			byte[] input = message.getBytes();
		
			//compresses the data
			Deflater compressor = new Deflater();
			compressor.setInput(input);
			compressor.finish();
			compressedDataLength = compressor.deflate(output);
			compressor.end();
			
			//outputs the data
			out.write(input);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void expand(BufferedInputStream in, BufferedOutputStream out) {
		try {
			Inflater decompresser = new Inflater();
		    decompresser.setInput(output, 0, compressedDataLength);
		    
		    byte[] result = new byte[100];
		    int resultLength = decompresser.inflate(result);
		    
		    decompresser.end();
		    
		    out.write(result);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
