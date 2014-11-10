/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCPWrapper;
import Algorithms.BinaryStdIn;
import Algorithms.BinaryStdOut;
import Algorithms.Huffman;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import org.apache.commons.lang3.time.StopWatch;
/**
 *
 * @author mbense
 */
public class DCPWrapper {
    
    static String host, port, role, path, alg, file, file2 = "";
    
    public static void main(String[] args) throws IOException, InterruptedException{
        
        role = args[0];
        port = args[1];
         //other computer
                                                                
        if (role.equals("server")){
            path = args[2];
            server(Integer.parseInt(port));
        }   
        else {
            host = args[2];
            path = args[3];
            alg = args[4];
            file = args[5];
            if (alg.equals("arithmeticcompress")){
                file2 = args[6];
            }
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
            if (alg.equals("arithmeticcompress")){
                while ((file2 = in.readLine()).equals(null)){}
            }
            System.out.println("Recieved algorithm + filename");
//          
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(new File(path + "/files/" + file + ".txt")));
            System.out.println("Opened " + path + "/files/" + file + ".txt");
            
            BinaryStdIn.setInputStream(fileIn);
            
            BinaryStdOut.setOutputStream(out);
            
            Huffman.compress();
            System.out.println("Compressed data sent");
            in.close();
            out.close();
            fileIn.close();
            socket.close();
            
            
        }
          //close all

    }

    private static void client(String host, int port) throws IOException, InterruptedException {
        
        
        Socket socket = new Socket(InetAddress.getByName(host), port);
        System.out.println("Connected to host");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        
        out.println(alg);
        out.println(file);
        
        if (alg.equals("arithmeticcompress")) out.println(file2);
        System.out.println("Sent alg + file");
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        System.out.println("Stopwatch started");
        
        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
        BinaryStdIn.setInputStream(in);
        
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BinaryStdOut.setOutputStream(new BufferedOutputStream(byteArray));
        
        Huffman.expand();
        
        System.out.println(byteArray.toString() + "\n");
        
        stopwatch.stop();
        System.out.println(stopwatch.getNanoTime() + " nanoseconds");
        
        byteArray.close();
        in.close();
        socket.close();
        out.close();
        
    }
}
