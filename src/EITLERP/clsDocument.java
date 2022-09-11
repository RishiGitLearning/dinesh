/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Purchase.*;
import EITLERP.Stores.*;
import java.io.*;


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsDocument {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    //History Related properties
    private boolean HistoryView=false;
    
    public String OldEmplID="";
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    
    /** Creates new clsBusinessObject */
    public clsDocument() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_ID",new Variant(""));
        props.put("DOC_NAME",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("DOCUMENT",new Variant(""));
        props.put("FILENAME",new Variant(""));
    }
    
    
    public static long StoreDocument(int CompanyID,String DocName,String Remarks,String FileName) {
        long DocID=0;
        try {
            
            Connection tmpConn=data.getConn();
            ResultSet rsDoc;
            
            if(FileName.trim().equals("")) {
                return 0;
            }
            
            File fileIn = new File(FileName);
            
            rsDoc=data.getResult("SELECT MAX(DOC_ID) AS MAX_DOC_ID FROM D_COM_DOCUMENTS");
            rsDoc.first();
            
            if(rsDoc.getRow()>0) {
                DocID=rsDoc.getLong("MAX_DOC_ID")+1;
            }
            else {
                DocID=1;
            }
            
            PreparedStatement ps = tmpConn.prepareStatement("INSERT INTO D_COM_DOCUMENTS (COMPANY_ID,DOC_ID,DOC_NAME,REMARKS,DOCUMENT,FILENAME) VALUES ("+CompanyID+","+DocID+",'"+DocName+"','"+Remarks+"',?,'"+FileName+"')");
            int fileLength = (int) fileIn.length();
            InputStream streamedFile = new FileInputStream(fileIn);
            ps.setBinaryStream(1, streamedFile, fileLength);
            ps.executeUpdate();
            ps.close();
            streamedFile.close();
            
            return DocID;
        }
        catch(Exception e) {
            e.printStackTrace();
            return DocID;
        }
    }
    

        
    
    public static boolean UpdateDocument(long pDocID,String pFileName) {
        try {
            
            Connection tmpConn=data.getConn();
            ResultSet rsDoc;
            
            //Old Values
            int CompanyID=0;
            String DocName="";
            String Remarks="";
            String FileName=pFileName;
            
            
            rsDoc=data.getResult("SELECT * FROM D_COM_DOCUMENTS WHERE DOC_ID="+pDocID);
            rsDoc.first();
            
            if(rsDoc.getRow()>0) {

                data.Execute("DELETE FROM D_COM_DOCUMENTS WHERE DOC_ID="+pDocID);
            }
                
                File fileIn = new File(FileName);
                
                PreparedStatement ps = tmpConn.prepareStatement("INSERT INTO D_COM_DOCUMENTS (COMPANY_ID,DOC_ID,DOC_NAME,REMARKS,DOCUMENT,FILENAME) VALUES ("+CompanyID+","+pDocID+",'"+DocName+"','"+Remarks+"',?,'"+FileName+"')");
                int fileLength = (int) fileIn.length();
                InputStream streamedFile = new FileInputStream(fileIn);
                ps.setBinaryStream(1, streamedFile, fileLength);
                ps.executeUpdate();
                ps.close();
                streamedFile.close();
                
            
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return true;
        }
    }
    

    
    public static clsDocument getDocument(int CompanyID,long DocID) {
        clsDocument objDoc=new clsDocument();
        
        try {
            
            String FileName="";
            String arrFullPath[];
            
            Connection tmpConn=data.getConn();
            
            ResultSet rs = tmpConn.createStatement().executeQuery("SELECT * FROM D_COM_DOCUMENTS WHERE DOC_ID="+DocID);
            rs.first();
            
            if(rs.getRow()>0) {
                
                arrFullPath=rs.getString("FILENAME").split("/");
                
                if(arrFullPath.length==1) {
                    arrFullPath=rs.getString("FILENAME").split("\\\\");
                    FileName= arrFullPath[arrFullPath.length-1];
                }
                else {
                    FileName= arrFullPath[arrFullPath.length-1];
                }
                                
                Blob blob = rs.getBlob("DOCUMENT");
                
                objDoc.setAttribute("COMPANY_ID",rs.getInt("COMPANY_ID"));
                objDoc.setAttribute("DOC_ID",rs.getLong("DOC_ID"));
                objDoc.setAttribute("DOC_NAME",rs.getString("DOC_NAME"));
                objDoc.setAttribute("REMARKS",rs.getString("REMARKS"));
                objDoc.setAttribute("FILENAME",FileName);
                
                OutputStream fwriter = new FileOutputStream(FileName);
                readFromBlob(blob, fwriter);
                fwriter.close();
            }
            
            
        }
        catch(Exception e) {
            
        }
        
        return objDoc;
    }
    
    
    
    public static long readFromBlob(Blob blob, OutputStream out)throws SQLException, IOException {
        InputStream in = blob.getBinaryStream();
        long bBufLen=blob.length();
        int length = -1;
        long read = 0;
        
        byte[] buf = new byte[1024];
        
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
            read += length;
        }
        in.close();
        return read;
    }
    
public static void SavetoFile(int CompanyID,long DocID,File FileName) {
        clsDocument objDoc=new clsDocument();
       
        try {
            String arrFullPath[];
            Connection tmpConn=data.getConn();
            ResultSet rs = tmpConn.createStatement().executeQuery("SELECT * FROM D_COM_DOCUMENTS WHERE DOC_ID="+DocID);
            rs.first();
           
            if(rs.getRow()>0) {
                Blob blob = rs.getBlob("DOCUMENT");
                objDoc.setAttribute("COMPANY_ID",rs.getInt("COMPANY_ID"));
                objDoc.setAttribute("DOC_ID",rs.getLong("DOC_ID"));
                objDoc.setAttribute("DOC_NAME",rs.getString("DOC_NAME"));
                objDoc.setAttribute("REMARKS",rs.getString("REMARKS"));
                objDoc.setAttribute("FILENAME",FileName);
               
                OutputStream fwriter = new FileOutputStream(FileName);
                readFromBlob(blob, fwriter);
                fwriter.close();
            }
           
           
        }
        catch(Exception e) {
           
        }
               
    }
    
}
