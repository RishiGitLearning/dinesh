package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsInterestCalcDate {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colInterestCalcDate=new HashMap();
    
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
    
    
    
    /** Creates new clsMaterialRequisition */
    public clsInterestCalcDate() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("INTEREST_MONTH",new Variant(""));
        props.put("INTEREST_DAYS",new Variant(""));
        
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        
        colInterestCalcDate=new HashMap();
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_INT_CALC_DATE WHERE COMPANY_ID="+pCompanyID+" ORDER BY SR_NO");
            
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
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            //----------------//
            
            data.Execute("DELETE FROM D_FD_INT_CALC_DATE",FinanceGlobal.FinURL);
            
            for(int i=0;i<colInterestCalcDate.size();i++) {
                clsInterestCalcDate objItem=(clsInterestCalcDate) colInterestCalcDate.get(Integer.toString(i+1));
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",i+1);
                
                rsResultSet.updateString("INTEREST_MONTH",objItem.getAttribute("INTEREST_MONTH").getString());
                rsResultSet.updateString("INTEREST_DAYS",objItem.getAttribute("INTEREST_DAYS").getString());
                
                rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CANCELLED", false);
                rsResultSet.updateBoolean("CHANGED",false);
                rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsResultSet.insertRow();
            }
            
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        try {
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            //----------------//
            
            data.Execute("DELETE FROM D_FD_INT_CALC_DATE",FinanceGlobal.FinURL);
            
            for(int i=0;i<colInterestCalcDate.size();i++) {
                clsInterestCalcDate objItem=(clsInterestCalcDate) colInterestCalcDate.get(Integer.toString(i+1));
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",i+1);
                
                rsResultSet.updateString("INTEREST_MONTH",objItem.getAttribute("INTEREST_MONTH").getString());
                rsResultSet.updateString("INTEREST_DAYS",objItem.getAttribute("INTEREST_DAYS").getString());
                
                rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsResultSet.updateBoolean("CANCELLED",false);
                rsResultSet.updateBoolean("CHANGED",false);
                rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsResultSet.insertRow();
            }
            
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean CanDelete(int CompanyID,String SchemeID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' AND (APPROVED=1)";
            int Count=data.getIntValueFromDB(strSQL, FinanceGlobal.FinURL);
            
            if(Count>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                return true;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean Delete(int UserID) {
        try {
            String SchemeID=getAttribute("SCHEME_ID").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,SchemeID,UserID)) {
                String strSQL = "DELETE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"'";
                data.Execute(strSQL,FinanceGlobal.FinURL);
                
                strSQL = "DELETE FROM D_FD_SCHEME_PERIOD WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID +" AND SCHEME_ID='"+SchemeID+"'";
                data.Execute(strSQL,FinanceGlobal.FinURL);
                
                LoadData(EITLERPGLOBAL.gCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        int Counter=0;
        
        try {
            colInterestCalcDate.clear();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_INT_CALC_DATE ORDER BY SR_NO");
            
            rsTmp.first();
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter++;
                clsInterestCalcDate objItem = new clsInterestCalcDate();
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO", 0));
                objItem.setAttribute("INTEREST_MONTH",UtilFunctions.getString(rsTmp,"INTEREST_MONTH", ""));
                objItem.setAttribute("INTEREST_DAYS",UtilFunctions.getString(rsTmp,"INTEREST_DAYS", ""));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00"));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00"));
                objItem.setAttribute("CHANGED",UtilFunctions.getBoolean(rsTmp,"CHANGED",false));
                objItem.setAttribute("CHANGED_DATE",UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00"));
                
                colInterestCalcDate.put(Integer.toString(Counter), objItem);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    private boolean Validate() {
        //** Validations **//
        return true;
    }
}
