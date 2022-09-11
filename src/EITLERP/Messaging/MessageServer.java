/*
 * MessageServer.java
 *
 * Created on February 8, 2005, 1:57 PM
 */

package EITLERP.Messaging;

/**
 *
 * @author  root
 */
import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import EITLERP.*;
import java.sql.*;

public class MessageServer {
    
    
    public boolean NewMessageReceived=false;
    public HashMap MsgBuffer=new HashMap();
    
    /** Creates a new instance of MessageServer */
    public MessageServer() {
        EITLERPGLOBAL.Clients=new HashMap(); //Erase all previous clients
    }
    
    public void startService() {
        //Starts the server to listen for clients
        try {
            EITLERPGLOBAL.theServer=new ServerSocket(EITLERPGLOBAL.Port);
            
            //Now start listening for the clients
            ListenMessages();
            ListenConnections();
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    public boolean sendMessage(int ClientSrNo,String Message) {
        try {
            Client ObjClient=(Client)EITLERPGLOBAL.Clients.get(Integer.toString(ClientSrNo));
            
            if(ObjClient.clSocket.isConnected()) {
                ObjClient.out.write(Message);
                ObjClient.out.newLine();
                ObjClient.out.flush();
                return true;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    private void ListenConnections() {
        new Thread() {
            public void run() {
                while(true) {
                    Client NewClient=new Client();
                    
                    try {
                        //Update the client object
                        NewClient.clSocket=EITLERPGLOBAL.theServer.accept();
                        NewClient.in=new BufferedReader(new InputStreamReader(NewClient.clSocket.getInputStream()));
                        NewClient.out=new BufferedWriter(new OutputStreamWriter(NewClient.clSocket.getOutputStream()));
                        NewClient.ClientIP=NewClient.clSocket.getLocalSocketAddress().toString();
                        NewClient.MsgBuffer=new HashMap();
                        NewClient.Connected=true;
                        //System.out.println("Connection created");
                        EITLERPGLOBAL.Clients.put(Integer.toString(EITLERPGLOBAL.Clients.size()+1),NewClient);
                    }
                    catch(Exception e){}
                    
                    try {
                        Thread.sleep(1000);
                    }
                    catch(Exception e){}
                }
            };
        }.start();
    }
    
    private void ListenMessages() {
        new Thread() {
            public void run() {
                boolean done=false;
                while(true) {
                    Iterator iclients=EITLERPGLOBAL.Clients.keySet().iterator();
                    
                    while(iclients.hasNext()){
                        String key=(String)iclients.next();
                        Client ObjClient=(Client)EITLERPGLOBAL.Clients.get(key);
                        
                        if(ClientIsConnected(ObjClient)) {
                            try {
                                ObjClient.clSocket.setSoTimeout(10);
                            }
                            catch(Exception e){}
                            
                            
                            done=false;
                            while(!done) {
                                try {
                                    String newMessage=ObjClient.in.readLine();
                                    if(newMessage==null){
                                        done=true;
                                    }
                                    else {
                                        //System.out.println(newMessage);
                                        parseMessage(ObjClient,newMessage,key);
                                    }
                                }
                                catch(Exception e){
                                    done=true;
                                }
                            }
                        }
                        else {
                            try {
                                
                                //System.out.println("Client has just closed connection");
                                ObjClient.Connected=false;
                                
                                Client theUser=(Client)EITLERPGLOBAL.Clients.get(key);
                                EITLERPGLOBAL.Clients.remove(key);
                                MessageServer.removeUser(theUser.UserName);
                                System.out.println("Client has closed connection");
                            }
                            catch(Exception e) {
                                
                            }
                        }
                        
                        
                    }
                    
                    
                    //Hault the execution
                    try {
                        Thread.sleep(200);
                    }
                    catch(Exception e){}
                }
            };
        }.start();
        
    }
    
    
    private boolean ClientIsConnected(Client pSocket) {
        try {
            pSocket.out.write("###PingTest###"); //Client will not display this message
            pSocket.out.newLine();
            pSocket.out.flush();
            return true;
        }
        catch(Throwable t) {
            return false;
        }
        
    }
    
    
    public static void AddUser(String pUserName) {
        //Add user to database
        data.Execute("INSERT INTO D_COM_MSG_USERS (USER_NAME) VALUES ('"+pUserName+"')");
    }
    
    public static void removeUser(String pUserName) {
        //Add user to database
        data.Execute("DELETE FROM D_COM_MSG_USERS WHERE USER_NAME='"+pUserName+"'");
    }
    
    public static HashMap getUserList() {
        HashMap usrList=new HashMap();
        try {
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_MSG_USERS");
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                String usrName=rsTmp.getString("USER_NAME");
                usrList.put(Integer.toString(usrList.size()+1),usrName);
                
                rsTmp.next();
            }
        }
        catch(Exception e) {
            
        }
        return usrList;
    }
    
    
    private void parseMessage(Client pClient,String pMessage,String cSrNo) {
        //(1) Parse - User Introduction
        
        if(pMessage.length()<20) {
            return; //Invalid message received. Incorrect protocol
        }
        
        String Header=pMessage.substring(0,20); //First twenty characters specifies the command
        
        
        //(1) USER INTRODUCTION
        if(Header.trim().equals("##USER.INFO##")) {
            String UserName=pMessage.substring(20,40).trim();
            String DeptName=pMessage.substring(40,60).trim();
            
            pClient.UserName=UserName;
            pClient.Dept=DeptName;
            
            EITLERPGLOBAL.Clients.put(cSrNo,pClient); //client information saved to the list
            
            AddUser(UserName);
            return;
        }
        
        //(2) MESSAGE REDIRECTION
        if(Header.trim().equals("##MSG##")) {
            String FromUser=pMessage.substring(20,40).trim();
            String ToUser=pMessage.substring(40,60).trim();
            String theMessage=pMessage.substring(60).trim();
            String actualMessage=theMessage;
            
            //System.out.println(ToUser);
            if(ToUser.trim().equals("##SERVER##")) {
                //Logg the message to server
                
                theMessage=EITLERPGLOBAL.padRight(20, FromUser.trim()," ")+"Message received from "+FromUser.trim()+" On "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime()+"\n\n"+theMessage;
                MsgBuffer.put(Integer.toString(MsgBuffer.size()+1),theMessage);
                NewMessageReceived=true;
                
                MsgWindow aWin=new MsgWindow();
                aWin.MsgBuffer=MsgBuffer;
                aWin.ShowWindow(); //Display message
            }
            
            if(ToUser.trim().equals("##EVERYBODY##")) {
                theMessage=EITLERPGLOBAL.padRight(20, FromUser.trim()," ")+"Message received from "+FromUser.trim()+" On "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime()+"\n\n"+theMessage;
                MsgBuffer.put(Integer.toString(MsgBuffer.size()+1),theMessage);
                NewMessageReceived=true;
                
                //Now redirect this message to everybody
                //Prepare a message to broadcast
                Iterator iclients=EITLERPGLOBAL.Clients.keySet().iterator();
                
                while(iclients.hasNext()){
                    String key=(String)iclients.next();
                    
                    Client ObjClient=(Client)EITLERPGLOBAL.Clients.get(key);
                    String newMessage=EITLERPGLOBAL.padRight(20, "##MSG##", " ")+EITLERPGLOBAL.padRight(20, FromUser, " ")+EITLERPGLOBAL.padRight(20,ObjClient.UserName," ")+actualMessage;
                    
                    try {
                        ObjClient.out.write(newMessage);
                        ObjClient.out.newLine();
                        ObjClient.out.flush();
                    }
                    catch(Exception e){}
                }
                
                MsgWindow aWin=new MsgWindow();
                aWin.MsgBuffer=MsgBuffer;
                aWin.ShowWindow(); //Display message
            }
            
            //Else to specific user redirection
            if((!ToUser.trim().equals("##SERVER##"))&&(!ToUser.trim().equals("##EVERYBODY##"))) {
                Iterator iclients=EITLERPGLOBAL.Clients.keySet().iterator();
                
                while(iclients.hasNext()){
                    String key=(String)iclients.next();
                    Client ObjClient=(Client)EITLERPGLOBAL.Clients.get(key);
                    
                    if(ObjClient.UserName.trim().equals(ToUser.trim())) {
                        String newMessage=EITLERPGLOBAL.padRight(20, "##MSG##", " ")+EITLERPGLOBAL.padRight(20, FromUser, " ")+EITLERPGLOBAL.padRight(20,ObjClient.UserName," ")+actualMessage;
                        
                        try {
                            ObjClient.out.write(newMessage);
                            ObjClient.out.newLine();
                            ObjClient.out.flush();
                        }
                        catch(Exception e){}
                    }
                }
                
            }
        }
    }
    
}
