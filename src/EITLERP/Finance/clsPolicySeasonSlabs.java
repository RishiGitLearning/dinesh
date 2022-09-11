package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsPolicySeasonSlabs{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
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
    public clsPolicySeasonSlabs() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("POLICY_ID",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("SEASON_ID",new Variant(""));
        props.put("QUALITY_ID",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("PERCENTAGE",new Variant(0.0));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS ORDER BY SR_NO");
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
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            
            long SrNo=data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_SAL_POLICY_SEASON_SLABS","SR_NO");
            //--------- Generate New Internal Deposit Type ID  ------------
            setAttribute("SR_NO",SrNo);
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsResultSet.updateInt("SR_NO",getAttribute("SR_NO").getInt());
            rsResultSet.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
            rsResultSet.updateString("QUALITY_ID",getAttribute("QUALITY_ID").getString());
            rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsResultSet.updateDouble("PERCENTAGE",getAttribute("PERCENTAGE").getDouble());
                        
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1);
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsHistory.updateInt("SR_NO",getAttribute("SR_NO").getInt());
            rsHistory.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
            rsHistory.updateString("QUALITY_ID",getAttribute("QUALITY_ID").getString());
            rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsHistory.updateDouble("PERCENTAGE",getAttribute("PERCENTAGE").getDouble());
                        
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
            }
            catch(Exception c){}
            //data.SetRollback();
            //data.SetAutoCommit(true);
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    
    
    public boolean Update() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
        
        try {
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            //rsResultSet.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsResultSet.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
            rsResultSet.updateString("QUALITY_ID",getAttribute("QUALITY_ID").getString());
            rsResultSet.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsResultSet.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsResultSet.updateDouble("PERCENTAGE",getAttribute("PERCENTAGE").getDouble());
                        
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_SEASON_SLABS_H WHERE POLICY_ID='"+getAttribute("POLICY_ID").getString()+"'");
            
            RevNo++;
            
            String RevDocNo=getAttribute("POLICY_ID").getString();
            //System.out.println("RevNo="+RevNo);
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsHistory.updateInt("SR_NO",getAttribute("SR_NO").getInt());
            rsHistory.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
            rsHistory.updateString("QUALITY_ID",getAttribute("QUALITY_ID").getString());
            rsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("FROM_DATE").getString()));
            rsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("TO_DATE").getString()));
            rsHistory.updateDouble("PERCENTAGE",getAttribute("PERCENTAGE").getDouble());
                        
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            return true;
        }
        catch(Exception e) {
            try {
            
            }
            catch(Exception c){}
                LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean CanDelete(int CompanyID,String PolicyID,int UserID,int SrNo) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_SEASON_SLABS WHERE POLICY_ID='"+PolicyID+"' AND SR_NO="+ SrNo +" ";
            int Count=data.getIntValueFromDB(strSQL);
            
            if(Count>0) 
            {
                return true;
            }
            else {
                return false;
            }
            
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean Delete(int UserID) {
        try {
            String PolicyID=getAttribute("POLICY_ID").getString();
            int SrNo = getAttribute("SR_NO").getInt();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,PolicyID,UserID,SrNo)) {
                String strSQL = "DELETE FROM D_SAL_POLICY_SEASON_SLABS WHERE POLICY_ID='"+PolicyID+"' AND SR_NO ="+SrNo;
                data.Execute(strSQL);
                
                LoadData(EITLERPGLOBAL.gCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted.";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int CompanyID, String PolicyID,int SrNo) {
        String strCondition = " WHERE POLICY_ID='" + PolicyID + "' AND SR_NO="+SrNo;
        clsPolicySeasonSlabs objSlabs = new clsPolicySeasonSlabs();
        objSlabs.Filter(strCondition,CompanyID);
        return objSlabs;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_POLICY_SEASON_SLABS " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_SEASON_SLABS ORDER BY SR_NO ";
                rsResultSet=Stmt.executeQuery(strSQL);
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
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        long Counter=0;
        int RevNo=0;
        
        try {
                        
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("POLICY_ID",UtilFunctions.getString(rsResultSet,"POLICY_ID", ""));
            setAttribute("SR_NO",UtilFunctions.getInt(rsResultSet,"SR_NO", 0));
            setAttribute("SEASON_ID",UtilFunctions.getString(rsResultSet,"SEASON_ID", ""));
            setAttribute("QUALITY_ID",UtilFunctions.getString(rsResultSet,"QUALITY_ID", ""));
            setAttribute("FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"FROM_DATE","0000-00-00")));
            setAttribute("TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"TO_DATE","0000-00-00")));
            setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsResultSet,"PERCENTAGE", 0));
            
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
       
}
