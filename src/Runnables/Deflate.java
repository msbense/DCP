package Runnables;

import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.*;
import java.io.*;


/** 
 * to decompress back into a string: String outputString = new String(result, 0, resultLength, "UTF-8"); 
*/
public class Deflate  {
	public static void compress(BufferedInputStream in, BufferedOutputStream out) {
		int compressedLength;
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
			
			byte[] temp = new byte[input.length];
			compressedLength = compressor.deflate(temp);
			
			byte[] output = new byte[compressedLength];
			for(int i = 0; i < output.length; i++) {
				output[i] = temp[i];
			}
			
			compressor.end();
			
			//writes the head for the length of the compressed data
			out.write(Integer.toBinaryString(compressedLength).getBytes());
			//writes the original length of the message
			out.write(Integer.toBinaryString(input.length).getBytes());
			//outputs the data
			out.write(input);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void expand(BufferedInputStream in, BufferedOutputStream out) {
		try {
			//reads the header
			byte[] header1 = new byte[32]; //ints are 32 bits
			in.read(header1);
			byte[] header2 = new byte[32]; // ^^
			in.read(header2);
			
			//sets the header to int values
			int length = Integer.parseInt(new String(header2, "UTF-8"), 2);
			int compressedDataLength = Integer.parseInt(new String(header1, "UTF-8"), 2);
			
			//decompresses
			Inflater decompresser = new Inflater();
			byte[] output = new byte[compressedDataLength];
			in.read(output);
		    decompresser.setInput(output, 0, compressedDataLength);
		    
		    byte[] result = new byte[length];
		    int resultLength = decompresser.inflate(result);
		    
		    decompresser.end();
		    
		    out.write(result);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
