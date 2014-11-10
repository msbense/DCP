Usage:
===
When you want DCP to act as a server:
    
    java -jar dcp.jar server [port] [path]
  where 
    [port] is the port the server will listen on, and 
    [path] is the path to the directory containing the files directory
  
===  
When you want DCP to act as a client:
    
    java -jar dcp.jar client [port] [host] [alg] [file]
  where 
    [port] is the port the client will connect to, 
    [host] is the ip/hostname of the server, 
    [alg] is the name of algorithm to use, and 
    [file] is the file to run it on
  
Behavior
===
When acting as a server, DCP will listen on the specified port. When someone connects, the server will run the alg and file 
  specified on the client, send the result, close the connection, and continue listening.

When acting as a client, DCP will send the request and measure the time it takes to recieve the response and decompress. 
