/*
 * clsClient.java
 *
 * Created on September 26, 2011, 1:30 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import java.net.*;
import java.io.*;


public class clsClient{
    
    static final int serverPort = 50000;
    static final int packetSize = 1024;
    
    public static void main(String args[]) throws
    UnknownHostException, SocketException{
        DatagramSocket socket=null; //How we send packets
        DatagramPacket packet=null; //what we send it in
        InetAddress address=null; //Where to send
        String messageSend=null; //Message to be send
        String messageReturn=null; //What we get back  from the Server
        byte[] data;
        
        //Checks for the arguments that sent to  the java interpreter
        // Make sure command line parameters correctr
        
        //        if(args.length != 2) {
        //            System.out.println("Usage Error :  Java EchoClient < Server name> < Message>");
        //            System.exit(0);
        //        }
        
        // Gets the IP address of the Server
        //address = InetAddress.getByName(args[0]);
        try {
            address = InetAddress.getByName("200.0.0.227");//
            System.out.println(address);
        } catch(Exception e) {
            e.printStackTrace();
        }
        socket = new DatagramSocket();
        
        data = new byte[packetSize];
        messageSend = new String("Hi");
        messageSend.getBytes(0,messageSend.length(),data,0);
        
        // remember datagrams hold bytes
        packet = new DatagramPacket(data,data.length,address,serverPort);
        System.out.println(" Trying to Send the packet ");
        
        try {
            // sends the packet
            
            socket.send(packet);
            
        }
        catch(IOException ie) {
            System.out.println("Could not Send :"+ie.getMessage());
            System.exit(0);
        }
        
        //packet is reinitialized to use it for recieving
        
        packet = new DatagramPacket(data,data.length);
        
        try {
            // Receives the packet from the server
            
            socket.receive(packet);
            
        }
        catch(IOException iee) {
            System.out.println("Could not receive : "+iee.getMessage() );
            System.exit(0);
        }
        
        // display message received
        
        messageReturn = new String(packet.getData(),0);
        System.out.println("Message Returned : "+
        messageReturn.trim());
    }    // main
    
    
} // Class EchoClient
