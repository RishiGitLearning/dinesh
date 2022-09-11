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
import EITLERP.Finance.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsPolicyTargetMaster{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colPeriodRange=new HashMap();
    public HashMap colRateRange=new HashMap();
    public static int ModuleID=147;
    
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
    public clsPolicyTargetMaster() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SR_NO",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("SEASON_ID",new Variant(""));
        props.put("PARTY_ID",new Variant(""));
        props.put("PARTY_SELECTION_TYPE", new Variant(""));
        props.put("PERIOD_FROM_DATE",new Variant(""));
        props.put("PERIOD_TO_DATE",new Variant(""));
        props.put("PERIOD_AMOUNT",new Variant(0.0));
        props.put("METERS_FROM_DATE",new Variant(""));
        props.put("METERS_TO_DATE",new Variant(""));
        props.put("METERS",new Variant(0.0));
        props.put("EXMILLRATE_FROM_RANGE",new Variant(0.0));
        props.put("EXMILLRATE_TO_RANGE",new Variant(0.0));
        props.put("EXMILLRATE_PERCENTAGE",new Variant(0.0));
        props.put("RATE_FROM_DATE",new Variant(""));
        props.put("RATE_TO_DATE",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        
        props.put("FFNO",new Variant(0));
        props.put("PREFIX",new Variant(""));
        props.put("SUFFIX",new Variant(""));
        
        colPeriodRange=new HashMap();
        colRateRange=new HashMap();
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_TARGET_MASTER ORDER BY DOC_NO,SR_NO ");
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
                String DocNo = UtilFunctions.getString(rsResultSet,"DOC_NO", "");
                
                while(!rsResultSet.isAfterLast()) {
                    String sDocNo = UtilFunctions.getString(rsResultSet,"DOC_NO", "");
                    if (DocNo.trim().equals(sDocNo)) {
                        rsResultSet.next();
                    }
                    else {
                        break;
                    }
                }
                
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
                String DocNo = UtilFunctions.getString(rsResultSet,"DOC_NO", "");
                
                while(!rsResultSet.isBeforeFirst()) {
                    String sDocNo = UtilFunctions.getString(rsResultSet,"DOC_NO", "");
                    if (DocNo.trim().equals(sDocNo)) {
                        rsResultSet.previous();
                    }
                    else {
                        break;
                    }
                }
                
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_TARGET_MASTER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            //=============================Generate New Policy No.===============================
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsPolicyTargetMaster.ModuleID,(int)getAttribute("FFNO").getVal(),true));
            //===============================================================================================
            
            
            for(int i=1;i<=colPeriodRange.size();i++) {
                clsTargetPeriodRange objPeriod=(clsTargetPeriodRange) colPeriodRange.get(Integer.toString(i));
                
                setAttribute("SR_NO",i);
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsResultSet.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsResultSet.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsResultSet.updateString("PARTY_ID",objPeriod.getAttribute("PARTY_ID").getString());
                rsResultSet.updateString("PERIOD_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_FROM_DATE").getString()));
                rsResultSet.updateString("PERIOD_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_TO_DATE").getString()));
                rsResultSet.updateDouble("PERIOD_AMOUNT",objPeriod.getAttribute("PERIOD_AMOUNT").getDouble());
                rsResultSet.updateString("METERS_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_FROM_DATE").getString()));
                rsResultSet.updateString("METERS_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_TO_DATE").getString()));
                rsResultSet.updateDouble("METERS",objPeriod.getAttribute("METERS").getDouble());
                
                rsResultSet.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsResultSet.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsResultSet.updateDouble("EXMILLRATE_FROM_RANGE",0);
                rsResultSet.updateDouble("EXMILLRATE_TO_RANGE",0);
                rsResultSet.updateDouble("EXMILLRATE_PERCENTAGE",0);
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
                rsHistory.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHistory.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsHistory.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsHistory.updateString("PARTY_ID",objPeriod.getAttribute("PARTY_ID").getString());
                rsHistory.updateString("PERIOD_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_FROM_DATE").getString()));
                rsHistory.updateString("PERIOD_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_TO_DATE").getString()));
                rsHistory.updateDouble("PERIOD_AMOUNT",objPeriod.getAttribute("PERIOD_AMOUNT").getDouble());
                rsHistory.updateString("METERS_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_FROM_DATE").getString()));
                rsHistory.updateString("METERS_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_TO_DATE").getString()));
                rsHistory.updateDouble("METERS",objPeriod.getAttribute("METERS").getDouble());
                
                rsHistory.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsHistory.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsHistory.updateDouble("EXMILLRATE_FROM_RANGE",0);
                rsHistory.updateDouble("EXMILLRATE_TO_RANGE",0);
                rsHistory.updateDouble("EXMILLRATE_PERCENTAGE",0);
                
                rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CHANGED",true);
                rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CANCELLED",false);
                
                rsHistory.insertRow();
            }
            
            for(int i=1;i<=colRateRange.size();i++) {
                clsTargetRateRange objRate=(clsTargetRateRange) colRateRange.get(Integer.toString(i));
                
                setAttribute("SR_NO",i);
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsResultSet.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsResultSet.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsResultSet.updateString("PARTY_ID",objRate.getAttribute("PARTY_ID").getString());
                rsResultSet.updateString("PERIOD_FROM_DATE","0000-00-00");
                rsResultSet.updateString("PERIOD_TO_DATE","0000-00-00");
                rsResultSet.updateDouble("PERIOD_AMOUNT",0);
                rsResultSet.updateString("METERS_FROM_DATE","0000-00-00");
                rsResultSet.updateString("METERS_TO_DATE","0000-00-00");
                rsResultSet.updateDouble("METERS",0);
                rsResultSet.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsResultSet.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsResultSet.updateDouble("EXMILLRATE_FROM_RANGE",objRate.getAttribute("EXMILLRATE_FROM_RANGE").getDouble());
                rsResultSet.updateDouble("EXMILLRATE_TO_RANGE",objRate.getAttribute("EXMILLRATE_TO_RANGE").getDouble());
                rsResultSet.updateDouble("EXMILLRATE_PERCENTAGE",objRate.getAttribute("EXMILLRATE_PERCENTAGE").getDouble());
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
                rsHistory.updateInt("SR_NO",getAttribute("SR_NO").getInt());
                rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHistory.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsHistory.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsHistory.updateString("PARTY_ID",objRate.getAttribute("PARTY_ID").getString());
                rsHistory.updateString("PERIOD_FROM_DATE","0000-00-00");
                rsHistory.updateString("PERIOD_TO_DATE","0000-00-00");
                rsHistory.updateDouble("PERIOD_AMOUNT",0);
                rsHistory.updateString("METERS_FROM_DATE","0000-00-00");
                rsHistory.updateString("METERS_TO_DATE","0000-00-00");
                rsHistory.updateDouble("METERS",0);
                rsHistory.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsHistory.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsHistory.updateDouble("EXMILLRATE_FROM_RANGE",objRate.getAttribute("EXMILLRATE_FROM_RANGE").getDouble());
                rsHistory.updateDouble("EXMILLRATE_TO_RANGE",objRate.getAttribute("EXMILLRATE_TO_RANGE").getDouble());
                rsHistory.updateDouble("EXMILLRATE_PERCENTAGE",objRate.getAttribute("EXMILLRATE_PERCENTAGE").getDouble());
                
                rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CHANGED",true);
                rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CANCELLED",false);
                
                rsHistory.insertRow();
            }
            
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
            int RevNo;
            String RevDocNo;
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_TARGET_MASTER_H WHERE SEASON_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_TARGET_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"' AND SEASON_ID='"+getAttribute("SEASON_ID").getString()+"'");
            
            //========= Inserting Into History =================//
            RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_TARGET_MASTER_H WHERE DOC_NO='"+getAttribute("DOC_NO").getString()+"' AND SEASON_ID='"+getAttribute("SEASON_ID").getString()+"'");
            
            RevNo++;
            
            RevDocNo=getAttribute("DOC_NO").getString();
            //System.out.println("RevNo="+RevNo);
            
            for(int i=1;i<=colPeriodRange.size();i++) {
                clsTargetPeriodRange objPeriod=(clsTargetPeriodRange) colPeriodRange.get(Integer.toString(i));
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",i);
                rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsResultSet.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsResultSet.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsResultSet.updateString("PARTY_ID",objPeriod.getAttribute("PARTY_ID").getString());
                rsResultSet.updateString("PERIOD_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_FROM_DATE").getString()));
                rsResultSet.updateString("PERIOD_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_TO_DATE").getString()));
                rsResultSet.updateDouble("PERIOD_AMOUNT",objPeriod.getAttribute("PERIOD_AMOUNT").getDouble());
                rsResultSet.updateString("METERS_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_FROM_DATE").getString()));
                rsResultSet.updateString("METERS_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_TO_DATE").getString()));
                rsResultSet.updateDouble("METERS",objPeriod.getAttribute("METERS").getDouble());
                
                rsResultSet.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsResultSet.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsResultSet.updateDouble("EXMILLRATE_FROM_RANGE",0);
                rsResultSet.updateDouble("EXMILLRATE_TO_RANGE",0);
                rsResultSet.updateDouble("EXMILLRATE_PERCENTAGE",0);
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
                rsHistory.updateInt("REVISION_NO",RevNo);
                rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHistory.updateInt("SR_NO",i);
                rsHistory.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHistory.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsHistory.updateString("PARTY_ID",objPeriod.getAttribute("PARTY_ID").getString());
                rsHistory.updateString("PERIOD_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_FROM_DATE").getString()));
                rsHistory.updateString("PERIOD_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("PERIOD_TO_DATE").getString()));
                rsHistory.updateDouble("PERIOD_AMOUNT",objPeriod.getAttribute("PERIOD_AMOUNT").getDouble());
                rsHistory.updateString("METERS_FROM_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_FROM_DATE").getString()));
                rsHistory.updateString("METERS_TO_DATE",EITLERPGLOBAL.formatDateDB(objPeriod.getAttribute("METERS_TO_DATE").getString()));
                rsHistory.updateDouble("METERS",objPeriod.getAttribute("METERS").getDouble());
                
                rsHistory.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsHistory.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsHistory.updateDouble("EXMILLRATE_FROM_RANGE",0);
                rsHistory.updateDouble("EXMILLRATE_TO_RANGE",0);
                rsHistory.updateDouble("EXMILLRATE_PERCENTAGE",0);
                rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CHANGED",true);
                rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CANCELLED",false);
                rsHistory.insertRow();
            }
            
            for(int i=1;i<=colRateRange.size();i++) {
                clsTargetRateRange objRate=(clsTargetRateRange) colRateRange.get(Integer.toString(i));
                
                rsResultSet.first();
                rsResultSet.moveToInsertRow();
                rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsResultSet.updateInt("SR_NO",i);
                rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsResultSet.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsResultSet.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsResultSet.updateString("PARTY_ID",objRate.getAttribute("PARTY_ID").getString());
                rsResultSet.updateString("PERIOD_FROM_DATE","0000-00-00");
                rsResultSet.updateString("PERIOD_TO_DATE","0000-00-00");
                rsResultSet.updateDouble("PERIOD_AMOUNT",0);
                rsResultSet.updateString("METERS_FROM_DATE","0000-00-00");
                rsResultSet.updateString("METERS_TO_DATE","0000-00-00");
                rsResultSet.updateDouble("METERS",0);
                rsResultSet.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsResultSet.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsResultSet.updateDouble("EXMILLRATE_FROM_RANGE",objRate.getAttribute("EXMILLRATE_FROM_RANGE").getDouble());
                rsResultSet.updateDouble("EXMILLRATE_TO_RANGE",objRate.getAttribute("EXMILLRATE_TO_RANGE").getDouble());
                rsResultSet.updateDouble("EXMILLRATE_PERCENTAGE",objRate.getAttribute("EXMILLRATE_PERCENTAGE").getDouble());
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
                rsHistory.updateInt("REVISION_NO",RevNo);
                rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHistory.updateInt("SR_NO",i);
                rsHistory.updateInt("PARTY_SELECTION_TYPE",getAttribute("PARTY_SELECTION_TYPE").getInt());
                rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
                rsHistory.updateString("SEASON_ID",getAttribute("SEASON_ID").getString());
                rsHistory.updateString("PARTY_ID",objRate.getAttribute("PARTY_ID").getString());
                rsHistory.updateString("PERIOD_FROM_DATE","0000-00-00");
                rsHistory.updateString("PERIOD_TO_DATE","0000-00-00");
                rsHistory.updateDouble("PERIOD_AMOUNT",0);
                rsHistory.updateString("METERS_FROM_DATE","0000-00-00");
                rsHistory.updateString("METERS_TO_DATE","0000-00-00");
                rsHistory.updateDouble("METERS",0);
                rsHistory.updateString("EXMILLRATE_FROM_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_FROM_DATE").getString()));
                rsHistory.updateString("EXMILLRATE_TO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("RATE_TO_DATE").getString()));
                rsHistory.updateDouble("EXMILLRATE_FROM_RANGE",objRate.getAttribute("EXMILLRATE_FROM_RANGE").getDouble());
                rsHistory.updateDouble("EXMILLRATE_TO_RANGE",objRate.getAttribute("EXMILLRATE_TO_RANGE").getDouble());
                rsHistory.updateDouble("EXMILLRATE_PERCENTAGE",objRate.getAttribute("EXMILLRATE_PERCENTAGE").getDouble());
                rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CHANGED",true);
                rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateBoolean("CANCELLED",false);
                rsHistory.insertRow();
                
            }
            
            
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
    
    
    public boolean CanDelete(int CompanyID,String DocNo,String SeasonID,int UserID,int SrNo) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_TARGET_MASTER WHERE DOC_NO='"+ DocNo +"' AND SEASON_ID='"+SeasonID+"' AND SR_NO="+ SrNo +" ";
            int Count=data.getIntValueFromDB(strSQL);
            
            if(Count>0) {
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
            String SeasonID=getAttribute("SEASON_ID").getString();
            String DocNo=getAttribute("DOC_NO").getString();
            int SrNo = getAttribute("SR_NO").getInt();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,DocNo,SeasonID,UserID,SrNo)) {
                String strSQL = "DELETE FROM D_SAL_POLICY_TARGET_MASTER WHERE DOC_NO='"+DocNo+"' AND SEASON_ID='"+SeasonID+"' AND SR_NO ="+SrNo;
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
    
    
    public Object getObject(int CompanyID, String DocNo) {
        String strCondition = " WHERE DOC_NO='" + DocNo + "' ";
        clsPolicyTargetMaster objSlabs = new clsPolicyTargetMaster();
        objSlabs.Filter(strCondition,CompanyID);
        return objSlabs;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_POLICY_TARGET_MASTER " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_TARGET_MASTER ORDER BY DOC_NO,SR_NO ";
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
            setAttribute("SR_NO",UtilFunctions.getInt(rsResultSet,"SR_NO", 0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO", ""));
            setAttribute("SEASON_ID",UtilFunctions.getString(rsResultSet,"SEASON_ID", ""));
            setAttribute("PARTY_SELECTION_TYPE",UtilFunctions.getInt(rsResultSet,"PARTY_SELECTION_TYPE", 0));
            setAttribute("RATE_FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"EXMILLRATE_FROM_DATE","0000-00-00")));
            setAttribute("RATE_TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"EXMILLRATE_TO_DATE","0000-00-00")));
            
            
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            
            colPeriodRange.clear();
            colRateRange.clear();
            
            String SeasonId=getAttribute("SEASON_ID").getString();
            //String PartyId=getAttribute("PARTY_ID").getString();
            String DocNo=getAttribute("DOC_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_TARGET_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND SEASON_ID='" + SeasonId + "' AND DOC_NO='" + DocNo + "' ORDER BY SR_NO");
            
            rsTmp.first();
            
            Counter=0;
            if (! rsTmp.getString("PERIOD_FROM_DATE").trim().equals("0000-00-00")) {
                while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                    
                    Counter=Counter+1;
                    clsTargetPeriodRange objItem = new clsTargetPeriodRange();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                    objItem.setAttribute("SEASON_ID",UtilFunctions.getString(rsTmp,"SEASON_ID",""));
                    objItem.setAttribute("PARTY_ID",UtilFunctions.getString(rsTmp,"PARTY_ID",""));
                    objItem.setAttribute("PERIOD_FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PERIOD_FROM_DATE","0000-00-00")));
                    objItem.setAttribute("PERIOD_TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"PERIOD_TO_DATE","0000-00-00")));
                    objItem.setAttribute("PERIOD_AMOUNT",Double.parseDouble(UtilFunctions.getString(rsTmp,"PERIOD_AMOUNT","")));
                    objItem.setAttribute("METERS_FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"METERS_FROM_DATE","0000-00-00")));
                    objItem.setAttribute("METERS_TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"METERS_TO_DATE","0000-00-00")));
                    objItem.setAttribute("METERS",Double.parseDouble(UtilFunctions.getString(rsTmp,"METERS","")));
                    
                    colPeriodRange.put(Long.toString(Counter),objItem);
                    rsTmp.next();
                }
            }
            else if (rsTmp.getString("PERIOD_FROM_DATE").trim().equals("0000-00-00")) {
                while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                    
                    Counter=Counter+1;
                    clsTargetRateRange objItem = new clsTargetRateRange();
                    
                    objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                    objItem.setAttribute("SEASON_ID",UtilFunctions.getString(rsTmp,"SEASON_ID",""));
                    objItem.setAttribute("PARTY_ID",UtilFunctions.getString(rsTmp,"PARTY_ID",""));
                    objItem.setAttribute("EXMILLRATE_FROM_RANGE",Double.parseDouble(UtilFunctions.getString(rsTmp,"EXMILLRATE_FROM_RANGE","")));
                    objItem.setAttribute("EXMILLRATE_TO_RANGE",Double.parseDouble(UtilFunctions.getString(rsTmp,"EXMILLRATE_TO_RANGE","")));
                    objItem.setAttribute("EXMILLRATE_PERCENTAGE",Double.parseDouble(UtilFunctions.getString(rsTmp,"EXMILLRATE_PERCENTAGE","")));
                    colRateRange.put(Long.toString(Counter),objItem);
                    rsTmp.next();
                }
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
}
