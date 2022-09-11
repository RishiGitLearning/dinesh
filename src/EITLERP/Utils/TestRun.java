/*
 * TestRun.java
 *
 * Created on June 21, 2006, 3:20 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import java.sql.*;
import java.util.*;
import java.net.*;
import EITLERP.*;



public class TestRun {
    
    /** Creates a new instance of TestRun */
    public TestRun() {
    }
    
    public static void main(String args[]) {
        try {
            
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            DriverManager.setLoginTimeout(1000);
            
            try
            {
            Connection Conn=DriverManager.getConnection("jdbc:mysql://200.0.0.180:3306/DINESHMILLSA");
            System.out.println("Got first connection");
            }
            catch(Exception e1){}


            try
            {
            Connection Conn1=DriverManager.getConnection("jdbc:mysql://200.0.0.180:3306/DINESHMILLSA");
            System.out.println("Got first connection 1");
            }
            catch(Exception e2){}
            
            
            try
            {
            Connection Conn2=DriverManager.getConnection("jdbc:mysql://200.0.0.180:3306/DINESHMILLSA");
            System.out.println("Got first connection 2");
            }
            catch(Exception e3){}
            
            
        }
        catch(Exception e) {
            
        }
        
        System.out.println("Done ...");
        System.out.println("1");
        
    }
    
}
