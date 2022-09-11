/*
 * clsPolicyCreditNoteType.java
 *
 * Created on March 25, 2009, 1:13 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author  root
 */
public class clsPolicyCreditNoteType {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colTypeLot=new HashMap();
    
    private boolean HistoryView=false;
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return new Variant("");
        }
        else {
            return (Variant) props.get(PropName);
        }
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
        
    /** Creates a new instance of clsPolicyCreditNoteType */
    public clsPolicyCreditNoteType() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("TYPE_ID",new Variant(0));
        props.put("TYPE_DESC",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
    }
    
    public static HashMap getList(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            rsTmp=data.getResult("SELECT * FROM D_SAL_POLICY_CREDITNOTE_TYPE "+pCondition+" ORDER BY TYPE_ID");            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPolicyCreditNoteType ObjTypeMaster=new clsPolicyCreditNoteType();
                
                //Populate the user
                ObjTypeMaster.setAttribute("TYPE_ID",rsTmp.getInt("TYPE_ID"));
                ObjTypeMaster.setAttribute("TYPE_DESC",rsTmp.getString("TYPE_DESC"));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjTypeMaster);
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }
}
