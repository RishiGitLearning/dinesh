package EITLERP.Utils;


import java.io.*;
import java.util.*;
import gnu.io.*;
import javax.print.*;
import javax.swing.*;
import java.awt.*;

public class SimpleRead implements SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;
    String LastRead="";
    public static String Reading="";
    public String LastError="";
    
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    boolean StopIt=false;
    boolean CompletedRead=false;
    
    SimpleRead theReader;
    
    public SimpleRead() {
        LastError="";
    }
    
    public String getReading() {
        return Reading.trim();
        
    }
    
    public boolean InitReader(String PortNo) {
        boolean PortFound=false;
        
        System.setSecurityManager(null);
        String driverName = "";
        
        driverName = "gnu.io.RXTXCommDriver";
        
        try{
            RXTXCommDriver commDriver = (RXTXCommDriver) Class.forName( driverName ).newInstance();
            commDriver.initialize();
        }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
        catch (InstantiationException e) { e.printStackTrace(); }
        catch (IllegalAccessException e) { e.printStackTrace(); }
        catch (Exception e){ e.printStackTrace(); }
        
        
        portList = CommPortIdentifier.getPortIdentifiers();
        portList = portId.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println(portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                
                if (portId.getName().equals(PortNo)) {
                    PortFound=true;
                    StartReading();
                }
            }
        }
        
        return PortFound;
    }
    
    
    public void StartReading() {
        try {
            if(!StopIt) {
                serialPort = (SerialPort) portId.open("SimpleReadApp", 500);
                StopIt=true;
            }
        } catch (PortInUseException e) {
            LastError=e.getMessage();
        }
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            LastError=e.getMessage();
        }
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
            LastError=e.getMessage();
        }
        serialPort.notifyOnDataAvailable(true);
        try {
            serialPort.setSerialPortParams(1200,
            SerialPort.DATABITS_7,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
            LastError=e.getMessage();
        }
    }
    
    
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[1];
                try {
                    while (inputStream.available() > 0) {
                        String readByte="";
                        inputStream.read(readBuffer);
                        readByte=new String(readBuffer);
                        
                        if(readBuffer[0]==3) {
                            if(!LastRead.trim().equals("")) {
                                Reading=LastRead;
                                serialPort.notifyOnDataAvailable(false);
                                serialPort.removeEventListener();
                                serialPort.close();
                                serialPort=null;
                            }
                            LastRead="";
                            
                        }
                        
                        if(IsDigit(readByte.substring(0,1))) {
                            LastRead+=readByte;
                        }
                    }
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    LastError=e.getMessage();
                }
                break;
        }
        
    }
    
    
    private boolean IsDigit(String pChar) {
        if(pChar.equals("0")||pChar.equals("1")||pChar.equals("2")||pChar.equals("3")||pChar.equals("4")||pChar.equals("5")||pChar.equals("6")||pChar.equals("7")||pChar.equals("8")||pChar.equals("9")||pChar.equals(".")) {
            return true;
        }
        else {
            return false;
        }
    }

    
    public void StopListener()
    {
        serialPort.removeEventListener();
    }
    
    public static synchronized String getWeight(String pPort) {
                
        SimpleRead portReader=new SimpleRead();
        String LastReading="";
        
        if(portReader.InitReader(pPort)) {
            while(LastReading.trim().equals("")) {
                LastReading=portReader.getReading();
            }
        }
        
        portReader.StopListener();

        return LastReading;
    }
    
}

