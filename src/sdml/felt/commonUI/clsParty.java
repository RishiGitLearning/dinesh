/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package sdml.felt.commonUI;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsParty {
    
    public String LastError="";
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
    public clsParty() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("PARTY_ID",new Variant(0));
        props.put("SUPPLIER_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("APPROVED_SUPPLIER",new Variant(false));
        props.put("ADD1",new Variant(""));
        props.put("ADD2",new Variant(""));
        props.put("ADD3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("STATE",new Variant(""));
        props.put("COUNTRY",new Variant(""));
        props.put("PHONE_O",new Variant(""));
        props.put("PHONE_R",new Variant(""));
        props.put("MOBILE",new Variant(""));
        props.put("EMAIL",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_PARTY WHERE COMPANY_ID="+pCompanyID+" ORDER BY PARTY_NAME");
            Ready=true;
            MoveFirst();
            return true;
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
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("PARTY_ID",(int)data.getMaxID(SDMLERPGLOBAL.gCompanyID, "D_COM_PARTY","PARTY_ID"));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateInt("PARTY_ID",(int)getAttribute("PARTY_ID").getVal());
            rsResultSet.updateString("SUPPLIER_CODE",(String)getAttribute("SUPPLIER_CODE").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateBoolean("APPROVED_SUPPLIER",getAttribute("APPROVED_SUPPLIER").getBool());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsResultSet.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
            rsResultSet.updateString("PHONE_R",(String)getAttribute("PHONE_R").getObj());
            rsResultSet.updateString("MOBILE",(String)getAttribute("MOBILE").getObj());
            rsResultSet.updateString("EMAIL",(String)getAttribute("EMAIL").getObj());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
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
            
            rsResultSet.updateString("SUPPLIER_CODE",(String)getAttribute("SUPPLIER_CODE").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateBoolean("APPROVED_SUPPLIER",getAttribute("APPROVED_SUPPLIER").getBool());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsResultSet.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
            rsResultSet.updateString("PHONE_R",(String)getAttribute("PHONE_R").getObj());
            rsResultSet.updateString("MOBILE",(String)getAttribute("MOBILE").getObj());
            rsResultSet.updateString("EMAIL",(String)getAttribute("EMAIL").getObj());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
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
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            int lDocNo=(int)getAttribute("PARTY_ID").getVal();
            String strSQL="";
            
            //First check that record is editable
            String strQry = "DELETE FROM D_COM_PARTY WHERE COMPANY_ID=" + lCompanyID +" AND PARTY_ID="+lDocNo;
            data.Execute(strQry);
            
            LoadData(lCompanyID);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,int pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND PARTY_ID="+pDocNo;
        clsParty ObjParty = new clsParty();
        ObjParty.Filter(strCondition,pCompanyID);
        return ObjParty;
    }
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_PARTY " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_PARTY WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY PARTY_NAME";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveFirst();
                return false;
            }
            else {
                Ready=true;
                MoveFirst();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean setData() {
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("PARTY_ID",rsResultSet.getInt("PARTY_ID"));
            setAttribute("SUPPLIER_CODE",rsResultSet.getString("SUPPLIER_CODE"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("APPROVED_SUPPLIER",rsResultSet.getBoolean("APPROVED_SUPPLIER"));
            setAttribute("ADD1",rsResultSet.getString("ADD1"));
            setAttribute("ADD2",rsResultSet.getString("ADD2"));
            setAttribute("ADD3",rsResultSet.getString("ADD3"));
            setAttribute("CITY",rsResultSet.getString("CITY"));
            setAttribute("STATE",rsResultSet.getString("STATE"));
            setAttribute("COUNTRY",rsResultSet.getString("COUNTRY"));
            setAttribute("PHONE_O",rsResultSet.getString("PHONE_O"));
            setAttribute("PHONE_R",rsResultSet.getString("PHONE_R"));
            setAttribute("MOBILE",rsResultSet.getString("MOBILE"));
            setAttribute("EMAIL",rsResultSet.getString("EMAIL"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static String getPartyNameByPartyID(int pCompanyID,int pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_COM_PARTY WHERE COMPANY_ID="+pCompanyID+" AND PARTY_ID="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PARTY_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }

    public static String getPartyEMail(int pCompanyID,int pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String email="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT EMAIL FROM D_COM_PARTY WHERE COMPANY_ID="+pCompanyID+" AND PARTY_ID="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                email=rsTmp.getString("EMAIL");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return email;
    }
    
    
    public static String getPartyNameFromSuppMaster(int pCompanyID,int pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT SUPPLIER_CODE FROM D_COM_PARTY WHERE COMPANY_ID="+pCompanyID+" AND PARTY_ID="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                String SuppCode=rsTmp.getString("SUPPLIER_CODE");
                
                
                stTmp=tmpConn.createStatement();
                rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+SuppCode+"'");
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                    PartyName=rsTmp.getString("SUPP_NAME");
                }
                
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyName;
    }
    
    
    
    public static String getSupplierCode(int pCompanyID,int pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String SuppCode="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT SUPPLIER_CODE FROM D_COM_PARTY WHERE COMPANY_ID="+pCompanyID+" AND PARTY_ID="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SuppCode=rsTmp.getString("SUPPLIER_CODE");
                
                
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return SuppCode;
    }

    
    public static int getPartyIDFromSupplier(int pCompanyID,String pSuppCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        int PartyID=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PARTY_ID FROM D_COM_PARTY WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyID=rsTmp.getInt("PARTY_ID");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return PartyID;
    }


    
    public static boolean IsValidPartyID(int pCompanyID,int pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean IsValid=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PARTY_ID FROM D_COM_PARTY WHERE COMPANY_ID="+pCompanyID+" AND PARTY_ID="+pPartyID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IsValid=true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        return IsValid;
    }
    
    
}
