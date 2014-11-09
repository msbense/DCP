/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DCPWrapper;
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
    
    static String host, port, role, path, alg, file;
    
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
            alg = args[3];
            file = args[4];
            client(host, Integer.parseInt(port));
        }
    }
    public static void server(int port) throws IOException, InterruptedException{
        Scanner flowcontrol = new Scanner(System.in); 
        
        ServerSocket server = new ServerSocket(port);
        Socket socket;
        
        while (true){
            
            socket = server.accept();
            System.out.println(socket.getRemoteSocketAddress().toString() + " connected");
        
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            while ((alg = in.readLine()).equals(null)){}
            while ((file = in.readLine()).equals(null)){}
            
            ProcessBuilder b = new ProcessBuilder("cmd.exe", "/C", "cd " + path + " && " + alg + " " + file);
            b.redirectOutput(ProcessBuilder.Redirect.PIPE);
            Process process = b.start();
            Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
            
            String line = "";
            while (scanner.hasNextLine()){
                line += scanner.nextLine();
            }
        
            out.println(line);
            
            out.close();
            scanner.close();
            socket.close();
            
            
        }
          //close all

    }

    private static void client(String host, int port) throws IOException, InterruptedException {
        
        
        Socket socket = new Socket(InetAddress.getByName(host), port);
        
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        out.println(alg);
        out.println(file);
        System.out.println("Wrote files");
        
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        String line = in.readLine();
        stopwatch.stop();
            
        
        System.out.println(line + "\n");
        System.out.println(stopwatch.getNanoTime() + " nanoseconds");
        
        in.close();
        socket.close();
        out.close();
        
    }
}
