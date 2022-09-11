/*
 * clsModules.java
 *
 * Created on April 13, 2004, 10:14 AM
 */

package sdml.felt.commonUI;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.sql.*;

/**
 *
 * @author  nhpatel
 * @version
 */
public class clsModules {
    
    /** Creates new clsFirstFree */
    
    public String LastError="";
    public String strSql;
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
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
    public clsModules() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("MODULE_DESC",new Variant(""));
        props.put("MAINTAIN_APPROVAL",new Variant(false));
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSql = "SELECT * FROM D_COM_MODULES WHERE COMPANY_ID="+Integer.toString(SDMLERPGLOBAL.gCompanyID)+"  AND PACKAGE='FELT_SALES' ORDER BY MODULE_ID";
            rsResultSet=Stmt.executeQuery(strSql);
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                return Ready;
            }
            else
                Ready = false;
            return Ready;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getCreatedConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM DINESHMILLS.D_COM_MODULES  "+pCondition);
            System.out.println("SELECT * FROM DINESHMILLS.D_COM_MODULES "+pCondition);
                    
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsModules ObjModules=new clsModules();
                
                ObjModules.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjModules.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                ObjModules.setAttribute("MODULE_DESC",rsTmp.getString("MODULE_DESC"));
                ObjModules.setAttribute("MAINTAIN_STOCK",rsTmp.getBoolean("MAINTAIN_STOCK"));
                
                List.put(Long.toString(Counter),ObjModules);
                rsTmp.next();
            }
            
            tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error on Fetch Module : "+e.getMessage());
        }
        return List;
    }
    
    public static String getModuleName(long pCompanyID,long pModuleID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT MODULE_DESC FROM D_COM_MODULES WHERE COMPANY_ID="+Long.toString(pCompanyID)+"  AND PACKAGE='FELT_SALES' AND MODULE_ID="+Long.toString(pModuleID));
            rsTmp.first();
            
            String Desc= rsTmp.getString("MODULE_DESC");
            
            tmpStmt.close();
            //tmpConn.close();
            rsTmp.close();
            
            return Desc;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getHeaderTableName(long pCompanyID,long pModuleID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT HEADER_TABLE_NAME FROM D_COM_MODULES WHERE COMPANY_ID="+Long.toString(pCompanyID)+"  AND PACKAGE='FELT_SALES' AND MODULE_ID="+Long.toString(pModuleID));
            rsTmp.first();
            
            String Table= rsTmp.getString("HEADER_TABLE_NAME");
            System.out.println("clsModule get Table : "+Table);
            tmpStmt.close();
            //tmpConn.close();
            rsTmp.close();
            
            return Table;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getDocNoFieldName(long pCompanyID,long pModuleID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            System.out.println("GET FIELD NAME query = SELECT DOC_NO_FIELD FROM D_COM_MODULES WHERE COMPANY_ID="+Long.toString(pCompanyID)+"  AND PACKAGE='FELT_SALES' AND MODULE_ID="+Long.toString(pModuleID));
            rsTmp=tmpStmt.executeQuery("SELECT DOC_NO_FIELD FROM D_COM_MODULES WHERE COMPANY_ID="+Long.toString(pCompanyID)+"  AND PACKAGE='FELT_SALES' AND MODULE_ID="+Long.toString(pModuleID));
            rsTmp.first();
            
            String Table= rsTmp.getString("DOC_NO_FIELD");
            
            tmpStmt.close();
            //tmpConn.close();
            rsTmp.close();
            
            return Table;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    
    public static String getDocDateFieldName(long pCompanyID,long pModuleID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT DOC_DATE_FIELD FROM D_COM_MODULES WHERE COMPANY_ID="+Long.toString(pCompanyID)+"  AND PACKAGE='FELT_SALES' AND MODULE_ID="+Long.toString(pModuleID));
            rsTmp.first();
            
            String Table= rsTmp.getString("DOC_DATE_FIELD");
            
            tmpStmt.close();
            //tmpConn.close();
            rsTmp.close();
            
            return Table;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getDetailTableName(long pCompanyID,long pModuleID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT DETAIL_TABLE_NAME FROM D_COM_MODULES WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MODULE_ID="+Long.toString(pModuleID));
            rsTmp.first();
            
            String Table= rsTmp.getString("DETAIL_TABLE_NAME");
            
            tmpStmt.close();
            //tmpConn.close();
            rsTmp.close();
            
            return Table;
        }
        catch(Exception e) {
            return "";
        }
    }
    
}
