package Runnables;

import java.util.zip.Deflater;
import java.util.*;
import java.io.*;

public class Deflate  {
	public static void main(String[] args) {
		try {
			Scanner scan = new Scanner(System.in);
			double time ; //the time it takes to compress in milliseconds 
			
			//gets the file to compress and the input stream
			System.out.println("The name of the file to compress");
			FileReader fr = new FileReader("files to test/" + scan.next());
			BufferedReader br = new BufferedReader(fr);
			
			//make string of data and convert to bits
			String message = "";
			while(br.ready()) {
				message += br.readLine();
			}
			byte[] input = message.getBytes();
			
			//get the start time
			double startTime = System.currentTimeMillis();
			
			//compresses the data
			Deflater compressor = new Deflater();
			compressor.setInput(input);
			compressor.finish();
			int compressedData = compressor.deflate(input);
			
			//gets end time
			double endTime = System.currentTimeMillis();
			
			//computes the total time
			time = endTime - startTime; 
			
			//outputs the data
			System.out.println("file compressed down to " + compressedData + " bytes");
			System.out.println("Compressed in " + time + " milliseconds");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
