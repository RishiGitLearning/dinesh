/*
 * clsFirstFree.java
 *
 * Created on April 13, 2004, 10:14 AM
 */

package EITLERP;

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
public class clsFirstFree {
    
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
    public clsFirstFree() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("FIRSTFREE_NO",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("PREFIX_CHARS",new Variant(""));
        props.put("SUFFIX_CHARS",new Variant(""));
        props.put("LAST_USED_NO",new Variant(""));
        props.put("BLOCKED",new Variant(""));
        props.put("PADDING_BY",new Variant(""));
        props.put("NO_LENGTH",new Variant(0));
        props.put("BLOCKED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSql = "SELECT * FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" ORDER BY MODULE_ID";
            rsResultSet=Stmt.executeQuery(strSql);
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready = MoveFirst();
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
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
            
        }
        catch(Exception e) {
            
        }
        
    }
    
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsResultSet.isAfterLast()||rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            }
            else {
                rsResultSet.next();
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            }
            else {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean Insert() {
        try {
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("FIRSTFREE_NO", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_FIRSTFREE","FIRSTFREE_NO"));
            rsResultSet.updateLong("MODULE_ID",(long) getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("PREFIX_CHARS",(String) getAttribute("PREFIX_CHARS").getObj());
            rsResultSet.updateString("SUFFIX_CHARS",(String) getAttribute("SUFFIX_CHARS").getObj());
            rsResultSet.updateString("LAST_USED_NO",(String) getAttribute("LAST_USED_NO").getObj());
            rsResultSet.updateInt("LAST_NO",(int) getAttribute("LAST_NO").getVal());
            rsResultSet.updateString("BLOCKED",(String) getAttribute("BLOCKED").getObj());
            rsResultSet.updateString("PADDING_BY",(String) getAttribute("PADDING_BY").getObj());
            rsResultSet.updateLong("NO_LENGTH",(long) getAttribute("NO_LENGTH").getVal());
            rsResultSet.updateString("BLOCKED_DATE",(String) getAttribute("BLOCKED_DATE").getObj());
            rsResultSet.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    //Updates current record
    public boolean Update() {
        try {
            rsResultSet.updateLong("MODULE_ID",(long) getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("PREFIX_CHARS",(String) getAttribute("PREFIX_CHARS").getObj());
            rsResultSet.updateString("SUFFIX_CHARS",(String) getAttribute("SUFFIX_CHARS").getObj());
            rsResultSet.updateInt("LAST_NO",(int) getAttribute("LAST_NO").getVal());
            rsResultSet.updateString("LAST_USED_NO",(String) getAttribute("LAST_USED_NO").getObj());
            rsResultSet.updateString("PADDING_BY",(String) getAttribute("PADDING_BY").getObj());
            rsResultSet.updateLong("NO_LENGTH",(long) getAttribute("NO_LENGTH").getVal());
            rsResultSet.updateString("BLOCKED",(String) getAttribute("BLOCKED").getObj());
            rsResultSet.updateString("BLOCKED_DATE",(String) getAttribute("BLOCKED_DATE").getObj());
            rsResultSet.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete() {
        try {
            long FirstFree =(int) getAttribute("FIRSTFREE_NO").getVal();
            String stmt = "DELETE FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" And FIRSTFREE_NO="+FirstFree;
            data.Execute(stmt);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,int pFirstFree) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND FIRSTFREE_NO=" + Integer.toString(pFirstFree);
        clsFirstFree ObjFirstFree = new clsFirstFree();
        ObjFirstFree.Filter(strCondition);
        return ObjFirstFree;
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_FIRSTFREE "+pCondition);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsFirstFree ObjFirstFree=new clsFirstFree();
                
                ObjFirstFree.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjFirstFree.setAttribute("FIRSTFREE_NO",rsTmp.getInt("FIRSTFREE_NO"));
                ObjFirstFree.setAttribute("PREFIX_CHARS",rsTmp.getString("PREFIX_CHARS"));
                ObjFirstFree.setAttribute("SUFFIX_CHARS",rsTmp.getString("SUFFIX_CHARS"));
                ObjFirstFree.setAttribute("LAST_USED_NO",rsTmp.getString("LAST_USED_NO"));
                ObjFirstFree.setAttribute("LAST_NO",rsTmp.getInt("LAST_NO"));
                ObjFirstFree.setAttribute("PADDING_BY",rsTmp.getString("PADDING_BY"));
                ObjFirstFree.setAttribute("NO_LENGTH",rsTmp.getInt("NO_LENGTH"));
                
                List.put(Long.toString(Counter),ObjFirstFree);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
    }
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_FIRSTFREE " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
            }
            else {
                Ready=false;
            }
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean setData() {
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("FIRSTFREE_NO",rsResultSet.getInt("FIRSTFREE_NO"));
            setAttribute("MODULE_ID",rsResultSet.getInt("MODULE_ID"));
            setAttribute("PREFIX_CHARS",rsResultSet.getString("PREFIX_CHARS"));
            setAttribute("SUFFIX_CHARS",rsResultSet.getString("SUFFIX_CHARS"));
            setAttribute("LAST_USED_NO",rsResultSet.getString("LAST_USED_NO"));
            setAttribute("LAST_NO",rsResultSet.getInt("LAST_NO"));
            setAttribute("BLOCKED",rsResultSet.getString("BLOCKED"));
            setAttribute("BLOCKED_DATE",rsResultSet.getString("BLOCKED_DATE"));
            setAttribute("PADDING_BY",rsResultSet.getString("PADDING_BY"));
            setAttribute("NO_LENGTH",rsResultSet.getInt("NO_LENGTH"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static String getNextFreeNo(int pCompanyID,int pModuleID,String pPrefix,String pSuffix,boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        String strNewNo="";
        int lnNewNo=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' ";
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lnNewNo=rsTmp.getInt("LAST_USED_NO")+1;
                strNewNo=EITLERPGLOBAL.Padding(Integer.toString(lnNewNo),rsTmp.getInt("NO_LENGTH"),rsTmp.getString("PADDING_BY"));
                
                if(UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' ");
                }
                
                strNewNo = pPrefix+ strNewNo+pSuffix;
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return strNewNo;
            }
            else {
                return "";
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getNextFreeNo(int pCompanyID,int pModuleID,int pFirstFreeNo,boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        String strNewNo="";
        int lnNewNo=0;
        String Prefix="";
        String Suffix="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFreeNo;
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lnNewNo=rsTmp.getInt("LAST_USED_NO")+1;
                strNewNo=EITLERPGLOBAL.Padding(Integer.toString(lnNewNo),rsTmp.getInt("NO_LENGTH"),rsTmp.getString("PADDING_BY"));
                Prefix=rsTmp.getString("PREFIX_CHARS");
                Suffix=rsTmp.getString("SUFFIX_CHARS");
                
                if(UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND FIRSTFREE_NO="+pFirstFreeNo);
                }
                
                strNewNo = Prefix+ strNewNo+Suffix;
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return strNewNo;
            }
            else {
                return "";
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getDefaultPrefix(int pCompanyID,int pModuleID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        String thePrefix="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL="SELECT PREFIX_CHARS FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID;
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                thePrefix=rsTmp.getString("PREFIX_CHARS");
                tmpStmt.close();
                rsTmp.close();
                
                return thePrefix;
            }
            else {
                return thePrefix;
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
   
    public static int getDefaultFirstFreeNo(int pCompanyID,int pModuleID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        int FFNo=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL="SELECT PREFIX_CHARS,FIRSTFREE_NO FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID;
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
                tmpStmt.close();
                rsTmp.close();
                
                return FFNo;
            }
            else {
                return FFNo;
            }
        }
        catch(Exception e) {
            return 0;
        }
    }
    
}

