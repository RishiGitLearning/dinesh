package EITLERP.Utils;

import java.io.*;
import java.util.*;
import javax.print.*;
  
public class TestComm {

    public static void main(String[] args) {
  
       SimpleRead portReader=new SimpleRead();	
       portReader.InitReader("COM1");

       portReader.getWeight("COM1");
       
       while(portReader.getReading().trim().equals(""))
        {
	//Wait for vajan kata to read the weight
        }		

        
    }

} 