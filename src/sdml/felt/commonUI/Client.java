/*
 * Client.java
 *
 * Created on February 8, 2005, 1:52 PM
 */
package sdml.felt.commonUI;

/**
 *
 * @author
 */
import java.net.*;
import java.util.*;
import java.io.*;

public class Client {

    public Socket clSocket = null;
    public String UserName = "";
    public String Dept = "";
    public String ClientIP = "";
    public BufferedReader in = null;
    public BufferedWriter out = null;
    public HashMap MsgBuffer = new HashMap();
    public boolean Connected = false;

    /**
     * Creates a new instance of Client
     */
    public Client() {
    }

}
