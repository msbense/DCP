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
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            
            while ((alg = in.readLine()).equals(null)){}
            while ((file = in.readLine()).equals(null)){}
            if (alg.equals("arithmeticcompress")){
                while ((file2 = in.readLine()).equals(null)){}
            }
            
//            System.out.println("cmd.exe " +  "/C " +  "cd " + path + " && " + alg + "compress " + file + ((!file2.equals("")) ? "" : " " + file2));
//            ProcessBuilder b = new ProcessBuilder("cmd.exe", "/C", "cd " + path + " && " + alg + "compress " + file + ((!file2.equals("")) ? "" : " " + file2));
//            b.redirectOutput(ProcessBuilder.Redirect.PIPE);
//            b.redirectError(ProcessBuilder.Redirect.PIPE);
//            Process process = b.start();
//
//            Scanner cmd = new Scanner(process.getInputStream());
//            Scanner error = new Scanner(new InputStreamReader(process.getErrorStream()));
//            while(error.hasNextLine()){
//                System.out.println(error.nextLine());
//            }
            //int bufferSize = socket.getSendBufferSize();
//            byte[] line = new byte[100000];
//            int count = 0;
//            System.out.println(cmd.nextLine());
//            while (cmd.hasNextByte()){
//                out.write(cmd.nextByte());
//                System.out.println((byte) count);
//            }
            BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(new File(path + "/files/" + file + ".txt")));
            BinaryStdIn.setInputStream(fileIn);
            BinaryStdOut.setOutputStream(out);
            
            Huffman.compress();
            
            in.close();
            out.close();
            fileIn.close();
            //cmd.close();
            socket.close();
            
            
        }
          //close all

    }

    private static void client(String host, int port) throws IOException, InterruptedException {
        
        
        Socket socket = new Socket(InetAddress.getByName(host), port);
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
        
        
        out.println(alg);
        out.println(file);
        if (alg.equals("arithmeticcompress")) out.println(file2);
        
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        
        BufferedOutputStream binWriter = new BufferedOutputStream(new FileOutputStream(new File(path + "/files/" + file + "compressed.bin")));
        BinaryStdIn.setInputStream(in);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BinaryStdOut.setOutputStream(new BufferedOutputStream(byteArray));
        Huffman.expand();
        System.out.println(byteArray.toString());
//        int bufferSize = socket.getReceiveBufferSize();
//        byte[] compressed = new byte[bufferSize];
//        int count = 0;
//        while ((count = in.read(compressed)) > 0){
//            System.out.println(count);
//            binWriter.write(count);
//        }
//        System.out.println("Wrote " + file + "compressed");
//        System.out.println("cmd.exe" + "/C" + "cd " + path + " && " + alg + "decompress " + file + "compressed" + ((!file2.equals("")) ? "" : " " + file2));
//        ProcessBuilder b = new ProcessBuilder("cmd.exe", "/C", "cd " + path + " && " + alg + "decompress " + file + "compressed" + ((!file2.equals("")) ? "" : " " + file2));
//        b.redirectOutput(ProcessBuilder.Redirect.PIPE);
//        b.redirectError(ProcessBuilder.Redirect.PIPE);
//        Process process = b.start();
//        Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
//        Scanner error = new Scanner(new InputStreamReader(process.getErrorStream()));
//        while(error.hasNextLine()){
//                System.out.println(error.nextLine());
//            }
//        
//        String line = "";
//        while (scanner.hasNextLine()){
//            line += scanner.nextLine();
//        }
//        
        //System.out.println("");
        
        
        
        stopwatch.stop();
            
        //System.out.println(line + "\n");
        System.out.println(stopwatch.getNanoTime() + " nanoseconds");
        
        in.close();
        socket.close();
        out.close();
        
    }
}
