package Runnables;

import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.*;
import java.io.*;

import Algorithms.BinaryStdIn;
import Algorithms.BinaryStdOut;


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
			
			//write the headers
			BinaryStdOut.setOutputStream(out);
			BinaryStdOut.write(compressedLength); //length of compressed file
			BinaryStdOut.write(input.length); //length of the uncompressed file
			//outputs the data
			out.write(input);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void expand(BufferedInputStream in, BufferedOutputStream out) {
		try {
			BinaryStdIn.setIntputStream(in);
			int compressedDataLength = BinaryStdIn.readInt();
			int length = BinaryStdIn.readInt();
			
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
