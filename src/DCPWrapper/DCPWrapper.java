package DCPWrapper;

import Algorithms.BinaryDump;
import Algorithms.BinaryStdIn;
import Algorithms.BinaryStdOut;
import Algorithms.Huffman;
import Algorithms.LZW;
import Algorithms.RunLength;
import Arithmetic.AdaptiveArithmeticCompress;
import Arithmetic.AdaptiveArithmeticDecompress;
import Runnables.Deflate;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.io.IOUtils;
/**
 *
 * @author mbense
 */
public class DCPWrapper {
    
    static String host, port, role, path, alg, file, file2 = "";
    
    public static void main(String[] args) throws IOException, InterruptedException{
        if (args.length < 1){
            System.out.println("No args found; see readme for usage");
            return;
        }
        role = args[0];
        port = args[1];
         //other computer
                                                                
        if (role.equals("server")){
           if(args.length == 2) {
        		path = "";
        	}
        	else {
        		path = args[2];
        	}
            server(Integer.parseInt(port));
        }   
        else {
            host = args[2];
            alg = args[3];
            file = args[4];
            client(host, Integer.parseInt(port));
        }
    }
    public static void server(int port) throws IOException, InterruptedException{
        
        ServerSocket server = new ServerSocket(port);
        Socket socket;
        
        while (true){
            socket = server.accept();
            System.out.println(socket.getRemoteSocketAddress().toString() + " connected");
        
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((alg = in.readLine()).equals(null)){}
            while ((file = in.readLine()).equals(null)){}
            System.out.println("Recieved " + alg + " + " + file);
            System.out.println("Opened " + path + "/files/" + file);
            
            FileInputStream fileIn = new FileInputStream(new File(path + "files/" + file + ".txt"));
            
            @SuppressWarnings("unused")
			ByteArrayOutputStream bArrayout = new ByteArrayOutputStream();
            
            
            BufferedInputStream bis = new BufferedInputStream(fileIn);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(bOut);
            
            if (alg.toLowerCase().equals("huffman")){
                HuffmanCompress(bis, bos);
                bis.close();
                bos.close();
            }
            else if (alg.toLowerCase().equals("lzw")){
                LZWCompress(bis, bos);
                bis.close();
                bos.close();
            }
            else if (alg.toLowerCase().equals("runlength")){
                BinaryRunCom(bis, bos);
                bis.close();
                bos.close();
            }
            else if (alg.toLowerCase().equals("arithmetic")){
                AdaptiveArithmeticCompress.Comp(bis, bos);
                bis.close();
                bos.close();
            }
            else if (alg.toLowerCase().equals("deflate")){
               DeflateCompress(bis, bos);
               bis.close();
               bos.close();
            }
            else if(alg.equals("raw")) {
            	rawData(bis, bos);
            	bis.close();
            	bos.close();
            }
            
            System.out.println("Compressed data sent");
            byte[] compressed = bOut.toByteArray();
            IOUtils.copy(new ByteArrayInputStream(compressed), socket.getOutputStream());
            System.out.println(compressed.length + " compressed file bytes length");
            //close all
            in.close();
            fileIn.close();
            socket.close();
        }
    }

    private static void client(String host, int port) throws IOException, InterruptedException {

        Scanner s = new Scanner(System.in);
        System.out.println("Hit enter to start");
        s.nextLine();
        Socket socket = new Socket(InetAddress.getByName(host), port);
        System.out.println("Connected to host");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                
        out.println(alg);
        out.println(file);
        if (alg.equals("arithmeticcompress")) out.println(file2);
        System.out.println("Sent " + alg + " and " +  file);
        
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        System.out.println("Stopwatch started");
        
        InputStream sock = socket.getInputStream(); 
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        BufferedInputStream bis = new BufferedInputStream(sock);
        BufferedOutputStream bos = new BufferedOutputStream(byteArray);
        
        if (alg.equals("huffman")){
                HuffmanDecompress(bis, bos);
                bis.close();
                bos.close();
            }
            else if (alg.equals("lzw")){
                LZWDecompress(bis, bos);
                bis.close();
                bos.close();
            }
            else if (alg.equals("RunLength")){
                BinaryRunDecom(bis, bos);
            }
            else if (alg.equals("arithmetic")){
                AdaptiveArithmeticDecompress.Decomp(sock, bos);
                bis.close();
                bos.close();
            }
            else if(alg.equals("deflate")){
                DeflateDecompress(bis, bos);
                bis.close();
                bos.close();
            }
            else if(alg.equals("raw")) {
            	rawData(bis, bos);
            	bis.close();
            	bos.close();
            }
            
//      bos.flush();
//      byteArray.flush();
        
        
        stopwatch.stop();
        System.out.println(byteArray.toString() + "\n");
        System.out.println(byteArray.size() + " original file bytes length\n");
        System.out.println(stopwatch.getNanoTime() + " nanoseconds");
        System.out.println(stopwatch.getTime() + " milliseconds\n");
        
        //close everything
        byteArray.close();
        sock.close();
        socket.close();
        out.close();
    }
    
    public static void HuffmanCompress(BufferedInputStream bis, BufferedOutputStream bos) {
        try {
            BinaryStdIn.setInputStream(bis);
            BinaryStdOut.setOutputStream(bos);
            Huffman.compress();
        } catch (Exception ex) {
            System.out.println("Exception in huffmanCompress");
        }
    }
    public static void HuffmanDecompress(BufferedInputStream bis, BufferedOutputStream bos){
        try{
            BinaryStdIn.setInputStream(bis);
            BinaryStdOut.setOutputStream(bos);
            Huffman.expand();
        }
        catch (Exception e){
            System.out.println("Exception in huffmanDecompress");
        }
    }
    
    public static void LZWCompress(BufferedInputStream bis, BufferedOutputStream bos){
        try{
            BinaryStdIn.setInputStream(bis);
            BinaryStdOut.setOutputStream(bos);
            LZW.compress();
        }
        catch (Exception e){
            System.out.println("Exception in LZWCompress");
        }
    }
    public static void LZWDecompress(BufferedInputStream bis, BufferedOutputStream bos){
        try{
            BinaryStdIn.setInputStream(bis);
            BinaryStdOut.setOutputStream(bos);
            LZW.expand();
        }
        catch (Exception e){
            System.out.println("Exception in LZWDecompress");
        }
    }
    public static void DeflateCompress(BufferedInputStream bis, BufferedOutputStream bos) {
//    	  BinaryStdIn.setInputStream(bis);
//          BinaryStdOut.setOutputStream(bos);
          Deflate.compress(bis, bos);
    }
    public static void DeflateDecompress(BufferedInputStream bis, BufferedOutputStream bos) {
//    	 BinaryStdIn.setInputStream(bis);
//         BinaryStdOut.setOutputStream(bos);
         Deflate.expand(bis, bos);
    }
    public static void BinaryRunDecom(BufferedInputStream bis, BufferedOutputStream bos){
    	BinaryStdIn.setInputStream(bis);
    	BinaryStdOut.setOutputStream(bos);
    	RunLength.expand();
    }
    public static void BinaryRunCom(BufferedInputStream bis, BufferedOutputStream bos) {
    	BinaryStdIn.setInputStream(bis);
    	ByteArrayInputStream bais = new ByteArrayInputStream(BinaryDump.compressToBinary().getBytes());
    	BinaryStdIn.setInputStream(new BufferedInputStream(bais));
    	BinaryStdOut.setOutputStream(bos);
    	
    	
    	RunLength.compress();
    }
    public static void rawData(BufferedInputStream bis, BufferedOutputStream bos) {
    	try {
	    	IOUtils.copy(bis, bos);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}

