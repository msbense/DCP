package DCPWrapper;
import Algorithms.BinaryDump;
import Algorithms.BinaryStdIn;
import Algorithms.BinaryStdOut;
import Algorithms.Huffman;
import Algorithms.LZW;
import Algorithms.RunLength;
import Arithmetic.AdaptiveArithmeticCompress;
import Arithmetic.AdaptiveArithmeticDecompress;
import Arithmetic.BitInputStream;
import Arithmetic.BitOutputStream;
import Runnables.Deflate;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import org.apache.commons.lang3.time.StopWatch;
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
            
            System.out.println("Recieved + " + alg + " " + file);
            ByteArrayOutputStream bArrayout = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(bArrayout);
            BitOutputStream bitout = new BitOutputStream(out);
            
            
            InputStream fileIn = new BufferedInputStream(new FileInputStream(new File(path + "files/" + file + ".txt")));
            System.out.println("Opened " + path + "/files/" + file + ".txt");
            
            
            BinaryStdIn.setInputStream((BufferedInputStream) fileIn);
            BinaryStdOut.setOutputStream(out);
            
            if (alg.toLowerCase().equals("huffman")){
                Huffman.compress();
            }
            else if (alg.toLowerCase().equals("lzw")){
                LZW.compress();
            }
            else if (alg.toLowerCase().equals("runlength")){
                 RunLength.compress();
            }
            else if (alg.toLowerCase().equals("arithmetic")){
                AdaptiveArithmeticCompress.compress(fileIn, bitout);
            }
            else if (alg.toLowerCase().equals("deflate")){
                Deflate.compress((BufferedInputStream)fileIn, out);
            }
            
            out.flush();
            bArrayout.flush();
            byte[] compressed = bArrayout.toByteArray();
            OutputStream s = socket.getOutputStream();
            s.write(compressed, 0, compressed.length);
            s.flush();
            System.out.println(compressed.length);
            System.out.println("Compressed data sent");
            
            
            
            bArrayout.close();
            bitout.close();
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
        
        
        ByteArrayOutputStream socketBytes = new ByteArrayOutputStream();
        InputStream sock = socket.getInputStream();
        byte[] buffer = new byte[256];
        int count = 0;
        System.out.println("entering loop");
        while ((count = sock.read(buffer)) > 0){
            socketBytes.write(buffer, 0, count);
            System.out.print(Arrays.toString(buffer));
        }
        
        socketBytes.flush();
        ByteArrayInputStream cArray = new ByteArrayInputStream(socketBytes.toByteArray());
        BufferedInputStream soc = new BufferedInputStream(cArray);
        BitInputStream bitsoc = new BitInputStream(cArray);
        BinaryStdIn.setInputStream(soc);
        
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOut = new BufferedOutputStream(byteArray);
        BinaryStdOut.setOutputStream(bufferedOut);
        
        if (alg.equals("huffman")){
                Huffman.expand();
            }
            else if (alg.equals("lzw")){
                LZW.expand();
            }
            else if (alg.equals("RunLength")){
                RunLength.expand();
            }
            else if (alg.equals("arithmetic")){
                AdaptiveArithmeticDecompress.decompress(bitsoc, bufferedOut);
            }
            else if(alg.equals("deflate")){
                Deflate.expand(soc, bufferedOut);
            }
            
        bufferedOut.flush();
        byteArray.flush();
        System.out.println(byteArray.toString() + "\n");
        
        stopwatch.stop();
        System.out.println(stopwatch.getNanoTime() + " nanoseconds");
        
        socketBytes.close();
        byteArray.close();
        sock.close();
        cArray.close();
        soc.close();
        bufferedOut.close();
        bitsoc.close();
        socket.close();
        out.close();
    }
}
