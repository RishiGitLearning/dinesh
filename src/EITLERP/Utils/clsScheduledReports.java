/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Utils;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;


public class clsScheduledReports {
    
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
    
    
    /** Creates new clsMaterialRequisition */
    public clsScheduledReports() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SR_NO",new Variant(0));
        props.put("JOB_NAME",new Variant(""));
        props.put("REPORT_URL",new Variant(""));
        props.put("SEND_TO",new Variant(""));
        props.put("SUBJECT",new Variant(""));
        props.put("CONTENT",new Variant(""));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_SCHEDULE_JOBS WHERE COMPANY_ID="+pCompanyID+" ORDER BY SR_NO ");
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close() {
        try {
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
            
            //Conn.setAutoCommit(false);
            
            if(!Validate()) {
                return false;
            }
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateLong("SR_NO",data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_SCHEDULE_JOBS","SR_NO")+1);
            rsResultSet.updateString("JOB_NAME",getAttribute("JOB_NAME").getString());
            rsResultSet.updateString("REPORT_URL",getAttribute("REPORT_URL").getString());
            rsResultSet.updateString("SEND_TO",getAttribute("SEND_TO").getString());
            rsResultSet.updateString("SUBJECT",getAttribute("SUBJECT").getString());
            rsResultSet.updateString("CONTENT",getAttribute("CONTENT").getString());
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            
            //Conn.commit();
            //Conn.setAutoCommit(true);
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //Conn.setAutoCommit(true);
            }catch(Exception c){}
            
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        try {
            
            //Conn.setAutoCommit(false);
            if(!Validate()) {
                return false;
            }
            
            
            rsResultSet.updateString("JOB_NAME",getAttribute("JOB_NAME").getString());
            rsResultSet.updateString("REPORT_URL",getAttribute("REPORT_URL").getString());
            rsResultSet.updateString("SEND_TO",getAttribute("SEND_TO").getString());
            rsResultSet.updateString("SUBJECT",getAttribute("SUBJECT").getString());
            rsResultSet.updateString("CONTENT",getAttribute("CONTENT").getString());
            rsResultSet.updateInt("CHANGED",1);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            
            //Conn.commit();
            //Conn.setAutoCommit(true);
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //Conn.setAutoCommit(true);
            }catch(Exception c){}
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    public boolean CanDelete(int pCompanyID,int SrNo) {
        return true;
    }
    
    
    
    public boolean Delete(long SrNo) {
        try {
            data.Execute("DELETE FROM D_COM_SCHEDULE_JOBS WHERE SR_NO="+SrNo);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_COM_SCHEDULE_JOBS " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_SCHEDULE_JOBS WHERE COMPANY_ID="+pCompanyID+" ORDER BY SR_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                MoveLast();
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
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("SR_NO",UtilFunctions.getLong(rsResultSet,"SR_NO",0));
            setAttribute("JOB_NAME",UtilFunctions.getString(rsResultSet,"JOB_NAME",""));
            setAttribute("REPORT_URL",UtilFunctions.getString(rsResultSet,"REPORT_URL",""));
            setAttribute("SEND_TO",UtilFunctions.getString(rsResultSet,"SEND_TO",""));
            setAttribute("SUBJECT",UtilFunctions.getString(rsResultSet,"SUBJECT",""));
            setAttribute("CONTENT",UtilFunctions.getString(rsResultSet,"CONTENT",""));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
        
    }
    
    private boolean Validate() {
        
        if(getAttribute("JOB_NAME").getString().trim().equals("")) {
            LastError="Please specify Job Name";
            return false;
        }
        
        if(getAttribute("REPORT_URL").getString().trim().equals("")) {
            LastError="Please specify the report URL";
            return false;
        }
        
        return true;
    }
    
    
    public static HashMap getApprovalRules() {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_COM_SCHEDULE_JOBS ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsScheduledReports objRule=new clsScheduledReports();
                    
                    objRule.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID",0));
                    objRule.setAttribute("SR_NO",UtilFunctions.getLong(rsTmp,"SR_NO",0));
                    objRule.setAttribute("JOB_NAME",UtilFunctions.getString(rsTmp,"JOB_NAME",""));
                    objRule.setAttribute("REPORT_URL",UtilFunctions.getString(rsTmp,"REPORT_URL",""));
                    objRule.setAttribute("SEND_TO",UtilFunctions.getString(rsTmp,"SEND_TO",""));
                    objRule.setAttribute("SUBJECT",UtilFunctions.getString(rsTmp,"SUBJECT",""));
                    objRule.setAttribute("CONTENT",UtilFunctions.getString(rsTmp,"CONTENT",""));
                    
                    List.put(Integer.toString(List.size()+1), objRule);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
}
