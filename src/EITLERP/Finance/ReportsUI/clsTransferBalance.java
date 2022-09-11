/*
 * clsTransferBalance.java
 *
 * Created on May 15, 2009, 12:29 PM
 */

package EITLERP.Finance.ReportsUI;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Finance.*;
import EITLERP.data;
import EITLERP.EITLComboModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.LOV;
import EITLERP.ComboData;
import EITLERP.Finance.*;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import TReportWriter.TReportEngine;
import EITLERP.Utils.frmProgress;
import java.net.URL;
import EITLERP.Sales.clsSalesInvoice;
import java.math.BigDecimal;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.PreparedStatement.*;

public class clsTransferBalance {

    
    /** Creates a new instance of clsTrialBalalnce */
    public clsTransferBalance() {
     
    }
    
    
    
     public static void main(String[] args) {
           //0 - Invoice Type
        //1 - FileName
        
        
        // original start 1
        
        if(args.length<1) {
            System.out.println("Insufficient arguments. Please specify \n 1. Invoice Type (1 - Suiting, 2 - Felt, 3 -Filter)  \n2. Line sequential file name \n3. Financial Year From \n4. Post SJ ? (Y or N)");
            return;
        }
        
        String Type=args[0];
        String maincode ;
       // String FileName=args[1];
      //  int FinYearFrom=Integer.parseInt(args[2]);
       // boolean PostSJ=false;
        
  
      clsTransferBalance objtransferbalance = new clsTransferBalance();
       
      if(Type.equals("1")) {
            maincode = "210027"; 
            objtransferbalance.TransferBalance(maincode,Type); 
        }
        
        if(Type.equals("2")) {
              maincode = "210010"; 
            objtransferbalance.TransferBalance(maincode,Type);  
        }
        
        if(Type.equals("3")) {
                    maincode = "210072"; 
            objtransferbalance.TransferBalance(maincode,Type); 
        }
     }
     
     
  private void TransferBalance(String MainCode ,String InvoiceType) {
        // TODO add your handling code here:
        try {
            int packetSize = 1024;
            int serverPort = 50000;
            DatagramSocket socket=null; //How we send packets
            DatagramPacket packet=null; //what we send it in
            InetAddress address=null; //Where to send
            String messageSend=null; //Message to be send
            String messageReturn=null; //What we get back  from the Server
            byte[] data;
           // int InvoiceType = 0;
         //   if(maincode.equals("210010")) {
            //    InvoiceType = 2;
           // } else if(txtMainCode.getText().trim().equals("210027")) {
           //     InvoiceType = 1;
         //   }
            // Gets the IP address of the Server
            //address = InetAddress.getByName(args[0]);
            
            address = InetAddress.getByName("200.0.0.227");//
            System.out.println(address);
            
            socket = new DatagramSocket();
            data = new byte[packetSize];
            messageSend = new String(InvoiceType);
            messageSend.getBytes(0,messageSend.length(),data,0);
            
            // remember datagrams hold bytes
            packet = new DatagramPacket(data,data.length,address,serverPort);
            System.out.println(" Trying to Send the packet ");
            
            // sends the packet
            socket.send(packet);
            
            //packet is reinitialized to use it for recieving
            packet = new DatagramPacket(data,data.length);
            
            // Receives the packet from the server
            socket.receive(packet);
            
            // display message received
            messageReturn = new String(packet.getData(),0);
            System.out.println("Message Returned : "+
            messageReturn.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
  }
  
}
