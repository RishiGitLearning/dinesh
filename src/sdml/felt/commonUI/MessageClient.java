/*
 * MessageClient.java
 *
 * Created on February 8, 2005, 2:18 PM
 */
package sdml.felt.commonUI;

/**
 *
 * @author
 */
import java.net.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

public class MessageClient {

    public HashMap MsgBuffer = new HashMap();
    public String ServerIP = "";
    public int Port = 5000;
    public Socket clSocket;
    public BufferedWriter out;
    public BufferedReader in;
    public boolean NewMessageReceived = false;
    private boolean CheckThreadStarted = false;

    /**
     * Creates a new instance of MessageClient
     */
    public MessageClient() {
    }

    public void Connect() {
        try {
            if (!CheckThreadStarted) {
                CheckThreadStarted = true;
                CheckServer();
            }

            clSocket = new Socket(ServerIP, Port);

            out = new BufferedWriter(new OutputStreamWriter(clSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clSocket.getInputStream()));

            sendMessage("##USER.INFO##       " + SDMLERPGLOBAL.padRight(20, SDMLERPGLOBAL.gLoginID, " ") + SDMLERPGLOBAL.padRight(20, clsDepartment.getDeptName(SDMLERPGLOBAL.gCompanyID, SDMLERPGLOBAL.gUserDeptID), " "));

            Listen();

        } catch (Exception e) {

        }
    }

    public void DisConnect() {
        try {
            clSocket.close();
        } catch (Exception e) {

        }
    }

    private void Listen() {

        new Thread() {
            public void run() {
                boolean Done = false;

                while (true) {
                    Done = false;

                    try {
                        clSocket.setSoTimeout(10);
                    } catch (Exception e) {
                    }

                    while (!Done) {
                        try {
                            String newMessage = in.readLine();

                            if (!newMessage.equals("###PingTest###")) {
                                NewMessageReceived = true;

                                String Header = newMessage.substring(0, 20);
                                String FromUser = newMessage.substring(20, 40);
                                String ToUser = newMessage.substring(40, 60);
                                String theMessage = newMessage.substring(60);

                                String finalMessage = SDMLERPGLOBAL.padRight(20, FromUser.trim(), " ") + "Message received from " + FromUser.trim() + " on " + SDMLERPGLOBAL.getCurrentDate() + " " + SDMLERPGLOBAL.getCurrentTime() + "\n\n" + theMessage;

                                MsgBuffer.put(Integer.toString(MsgBuffer.size() + 1), finalMessage);
                                MsgWindow aWin = new MsgWindow();
                                aWin.MsgBuffer = MsgBuffer;
                                aWin.ShowWindow();
                            }
                        } catch (Exception e) {
                            Done = true;
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        ;
    }

    .start();
    }
    
    
    
    private void CheckServer() {

        new Thread() {
            public void run() {
                boolean Done = false;

                while (true) {
                    Done = false;

                    try {
                        clSocket.setSoTimeout(10);
                    } catch (Exception e) {
                    }

                    //Send server ping test
                    try {
                        String newMessage = "##USER.INFO##       " + SDMLERPGLOBAL.padRight(20, SDMLERPGLOBAL.gLoginID, " ") + SDMLERPGLOBAL.padRight(20, clsDepartment.getDeptName(SDMLERPGLOBAL.gCompanyID, SDMLERPGLOBAL.gUserDeptID), " ");
                        out.write(newMessage);
                        out.newLine();
                        out.flush();
                        System.out.println("Server is on");
                    } catch (Exception e) {
                        //Server is off. Try connecting to it
                        Connect();

                    }

                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                    }

                }
            }
        ;
    }

    .start();
    }
    
    public void sendMessage(String newMessage) {
        try {
            out.write(newMessage);
            out.newLine();
            out.flush();
        } catch (Exception e) {

        }
    }

}
