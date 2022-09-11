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
 * @author  nrpithva
 * @version
 */

public class clsPolicyMaster{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public HashMap colPolicyParties=new HashMap();
    public HashMap colPolicyTurnOverSlab=new HashMap();
    public HashMap colPolicySeasonSlab=new HashMap();
    public HashMap colPolicyPartyPer=new HashMap();
    
    public static int ModuleID=105;
    
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
    public clsPolicyMaster() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("POLICY_ID",new Variant(""));
        props.put("POLICY_NAME",new Variant(""));
        props.put("DISCOUNT_TYPE",new Variant(0));
        props.put("PARTY_TYPE",new Variant(0));
        props.put("INVOICE_TYPES",new Variant(""));
        props.put("INTEREST_CALCULATION_PERIOD",new Variant(0));
        props.put("APPLICABILITY",new Variant(0));
        props.put("APP_AMOUNT_LIMIT",new Variant(0));
        props.put("FLAT_PERCENTAGE",new Variant(0.0));
        props.put("DISCOUNT_PERCENTAGE",new Variant(0));
        props.put("PERIOD_BASE",new Variant(0));
        props.put("EFFECTIVE_DATE",new Variant(""));
        props.put("EXPIRY_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("TURNOVER_CALC_TYPE",new Variant(0));
        props.put("DEDUCTION_CODES",new Variant(""));
        props.put("PERIOD",new Variant(0));
        props.put("INVOICE_MAIN_TYPE",new Variant(0));
        props.put("CREDIT_NOTE_TYPE",new Variant(0));
        
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("STATUS", new Variant(""));
        
        colPolicyParties=new HashMap();
        colPolicyTurnOverSlab = new HashMap();
        colPolicySeasonSlab = new HashMap();
        colPolicyPartyPer = new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("FFNO",new Variant(0));
        props.put("PREFIX",new Variant(""));
        props.put("SUFFIX",new Variant(""));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_MASTER WHERE COMPANY_ID="+ pCompanyID +" ORDER BY POLICY_ID");
            HistoryView=false;
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_MASTER_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            if(data.IsRecordExist("SELECT POLICY_ID FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+getAttribute("POLICY_ID").getString()+"' ")) {
                LastError="Policy Id already exist.";
                return false;
            }
            //*****************//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //=============================Generate New Policy No.===============================
            setAttribute("POLICY_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsPolicyMaster.ModuleID,(int)getAttribute("FFNO").getVal(),true));
            //===============================================================================================
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsResultSet.updateString("POLICY_NAME",getAttribute("POLICY_NAME").getString());
            rsResultSet.updateInt("DISCOUNT_TYPE",getAttribute("DISCOUNT_TYPE").getInt());
            rsResultSet.updateInt("PARTY_TYPE",getAttribute("PARTY_TYPE").getInt());
            rsResultSet.updateString("INVOICE_TYPES",getAttribute("INVOICE_TYPES").getString());
            rsResultSet.updateInt("APPLICABILITY",getAttribute("APPLICABILITY").getInt());
            rsResultSet.updateInt("APP_AMOUNT_LIMIT",getAttribute("APP_AMOUNT_LIMIT").getInt());
            rsResultSet.updateDouble("FLAT_PERCENTAGE",getAttribute("FLAT_PERCENTAGE").getDouble());
            rsResultSet.updateInt("DISCOUNT_PERCENTAGE",getAttribute("DISCOUNT_PERCENTAGE").getInt());
            rsResultSet.updateInt("PERIOD_BASE",getAttribute("PERIOD_BASE").getInt());
            rsResultSet.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsResultSet.updateString("EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPIRY_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACOUNT_CODE").getString());
            rsResultSet.updateInt("TURNOVER_CALC_TYPE",getAttribute("TURNOVER_CALC_TYPE").getInt());
            rsResultSet.updateString("DEDUCTION_CODES",getAttribute("DEDUCTION_CODES").getString());
            rsResultSet.updateInt("PERIOD",getAttribute("PERIOD").getInt());
            rsResultSet.updateInt("INVOICE_MAIN_TYPE",getAttribute("INVOICE_MAIN_TYPE").getInt());
            rsResultSet.updateInt("CREDIT_NOTE_TYPE",getAttribute("CREDIT_NOTE_TYPE").getInt());
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",false);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsHistory.updateString("POLICY_NAME",getAttribute("POLICY_NAME").getString());
            rsHistory.updateInt("DISCOUNT_TYPE",getAttribute("DISCOUNT_TYPE").getInt());
            rsHistory.updateInt("PARTY_TYPE",getAttribute("PARTY_TYPE").getInt());
            rsHistory.updateString("INVOICE_TYPES",getAttribute("INVOICE_TYPES").getString());
            rsHistory.updateInt("APPLICABILITY",getAttribute("APPLICABILITY").getInt());
            rsHistory.updateInt("APP_AMOUNT_LIMIT",getAttribute("APP_AMOUNT_LIMIT").getInt());
            rsHistory.updateDouble("FLAT_PERCENTAGE",getAttribute("FLAT_PERCENTAGE").getDouble());
            rsHistory.updateInt("DISCOUNT_PERCENTAGE",getAttribute("DISCOUNT_PERCENTAGE").getInt());
            rsHistory.updateInt("PERIOD_BASE",getAttribute("PERIOD_BASE").getInt());
            rsHistory.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateString("EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPIRY_DATE").getString()));
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACOUNT_CODE").getString());
            rsHistory.updateInt("TURNOVER_CALC_TYPE",getAttribute("TURNOVER_CALC_TYPE").getInt());
            rsHistory.updateString("DEDUCTION_CODES",getAttribute("DEDUCTION_CODES").getString());
            rsHistory.updateInt("PERIOD",getAttribute("PERIOD").getInt());
            rsHistory.updateInt("INVOICE_MAIN_TYPE",getAttribute("INVOICE_MAIN_TYPE").getInt());
            rsHistory.updateInt("CREDIT_NOTE_TYPE",getAttribute("CREDIT_NOTE_TYPE").getInt());
            
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            ResultSet rsHParty,rsParty;
            Statement stHParty,stParty;
            
            stParty=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsParty=stParty.executeQuery("SELECT * FROM D_SAL_POLICY_PARTIES  WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            
            
            stHParty=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHParty=stHParty.executeQuery("SELECT * FROM D_SAL_POLICY_PARTIES_H  WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            rsHParty.first();
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colPolicyParties.size();i++) {
                clsPolicyParties objParty=(clsPolicyParties) colPolicyParties.get(Integer.toString(i));
                
                rsParty.moveToInsertRow();
                rsParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsParty.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                rsParty.updateInt("SR_NO",i);
                rsParty.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsParty.updateString("PARTY_MAIN_CODE",objParty.getAttribute("PARTY_MAIN_CODE").getString());
                rsParty.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsParty.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsParty.updateBoolean("CHANGED",false);
                rsParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsParty.updateBoolean("CANCELLED",false);
                
                rsParty.insertRow();
                
                rsHParty.moveToInsertRow();
                rsHParty.updateInt("REVISION_NO",1);
                rsHParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHParty.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                rsHParty.updateInt("SR_NO",i);
                rsHParty.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsHParty.updateString("PARTY_MAIN_CODE",objParty.getAttribute("PARTY_MAIN_CODE").getString());
                rsHParty.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHParty.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHParty.updateBoolean("CHANGED",false);
                rsHParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHParty.updateBoolean("CANCELLED",false);
                
                rsHParty.insertRow();
                
            }
            
            if (colPolicyPartyPer.size() > 0) {
                ResultSet rsHPartyPer,rsPartyPer;
                Statement stHPartyPer,stPartyPer;
                
                stPartyPer=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsPartyPer=stPartyPer.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_FLAT_PER  WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                
                
                stHPartyPer=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsHPartyPer=stHPartyPer.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_FLAT_PER_H  WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsHPartyPer.first();
                
                
                //Now Insert records into detail table
                for(int i=1;i<=colPolicyPartyPer.size();i++) {
                    clsPolicyPartyFlatPer objParty=(clsPolicyPartyFlatPer) colPolicyPartyPer.get(Integer.toString(i));
                    
                    rsPartyPer.moveToInsertRow();
                    rsPartyPer.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsPartyPer.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                    rsPartyPer.updateInt("SR_NO",i);
                    rsPartyPer.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                    rsPartyPer.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                    rsPartyPer.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                    rsPartyPer.updateString("PRODUCT_CODE",objParty.getAttribute("PRODUCT_CODE").getString());
                    rsPartyPer.updateDouble("FLAT_PERCENTAGE",objParty.getAttribute("FLAT_PERCENTAGE").getDouble());
                    rsPartyPer.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsPartyPer.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsPartyPer.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsPartyPer.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsPartyPer.updateBoolean("CHANGED",false);
                    rsPartyPer.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsPartyPer.updateBoolean("CANCELLED",false);
                    
                    rsPartyPer.insertRow();
                    
                    rsHPartyPer.moveToInsertRow();
                    rsHPartyPer.updateInt("REVISION_NO",1);
                    rsHPartyPer.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsHPartyPer.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                    rsHPartyPer.updateInt("SR_NO",i);
                    rsHPartyPer.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                    rsHPartyPer.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                    rsHPartyPer.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                    rsHPartyPer.updateString("PRODUCT_CODE",objParty.getAttribute("PRODUCT_CODE").getString());                    
                    rsHPartyPer.updateDouble("FLAT_PERCENTAGE",objParty.getAttribute("FLAT_PERCENTAGE").getDouble());
                    rsHPartyPer.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsHPartyPer.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHPartyPer.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsHPartyPer.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHPartyPer.updateBoolean("CHANGED",false);
                    rsHPartyPer.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHPartyPer.updateBoolean("CANCELLED",false);
                    
                    rsHPartyPer.insertRow();
                    
                }
            }
            
            //==================slabs add==============//
            Statement stSlabsHistory,stSlabsHeader;
            ResultSet rsSlabsHistory,rsSlabsHeader;
            if (colPolicyTurnOverSlab.size() > 0) {
                
                stSlabsHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHeader=stSlabsHeader.executeQuery("SELECT * FROM D_SAL_POLICY_TURNOVER_SLABS WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHeader.first();
                
                stSlabsHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHistory=stSlabsHistory.executeQuery("SELECT * FROM D_SAL_POLICY_TURNOVER_SLABS_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHistory.first();
                
                for(int i=1;i<=colPolicyTurnOverSlab.size();i++) {
                    clsPolicyTurnoverSlabs objSlabs=(clsPolicyTurnoverSlabs) colPolicyTurnOverSlab.get(Integer.toString(i));
                    
                    rsSlabsHeader.first();
                    rsSlabsHeader.moveToInsertRow();
                    rsSlabsHeader.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHeader.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHeader.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHeader.updateDouble("FROM_VALUE",objSlabs.getAttribute("FROM_VALUE").getDouble());
                    rsSlabsHeader.updateDouble("TO_VALUE",objSlabs.getAttribute("TO_VALUE").getDouble());
                    rsSlabsHeader.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    
                    rsSlabsHeader.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CHANGED",true);
                    rsSlabsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CANCELLED",false);
                    rsSlabsHeader.insertRow();
                    
                    
                    //========= Inserting Into History =================//
                    rsSlabsHistory.moveToInsertRow();
                    rsSlabsHistory.updateInt("REVISION_NO",1);
                    rsSlabsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHistory.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHistory.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHistory.updateDouble("FROM_VALUE",objSlabs.getAttribute("FROM_VALUE").getDouble());
                    rsSlabsHistory.updateDouble("TO_VALUE",objSlabs.getAttribute("TO_VALUE").getDouble());
                    rsSlabsHistory.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    rsSlabsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CHANGED",true);
                    rsSlabsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CANCELLED",false);
                    rsSlabsHistory.insertRow();
                }
                
                
            }
            
            if (colPolicySeasonSlab.size() > 0) {
                
                stSlabsHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHeader=stSlabsHeader.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHeader.first();
                
                stSlabsHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHistory=stSlabsHistory.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHistory.first();
                
                for(int i=1;i<=colPolicySeasonSlab.size();i++) {
                    clsPolicySeasonSlabs objSlabs=(clsPolicySeasonSlabs) colPolicySeasonSlab.get(Integer.toString(i));
                    
                    rsSlabsHeader.first();
                    rsSlabsHeader.moveToInsertRow();
                    rsSlabsHeader.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHeader.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHeader.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHeader.updateString("SEASON_ID",objSlabs.getAttribute("SEASON_ID").getString());
                    rsSlabsHeader.updateString("QUALITY_ID",objSlabs.getAttribute("QUALITY_ID").getString());
                    rsSlabsHeader.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("FROM_DATE").getString()));
                    rsSlabsHeader.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("TO_DATE").getString()));
                    rsSlabsHeader.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    
                    rsSlabsHeader.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CHANGED",true);
                    rsSlabsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CANCELLED",false);
                    rsSlabsHeader.insertRow();
                    
                    
                    //========= Inserting Into History =================//
                    rsSlabsHistory.moveToInsertRow();
                    rsSlabsHistory.updateInt("REVISION_NO",1);
                    rsSlabsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHistory.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHistory.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHistory.updateString("SEASON_ID",objSlabs.getAttribute("SEASON_ID").getString());
                    rsSlabsHistory.updateString("QUALITY_ID",objSlabs.getAttribute("QUALITY_ID").getString());
                    rsSlabsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("FROM_DATE").getString()));
                    rsSlabsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("TO_DATE").getString()));
                    rsSlabsHistory.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    
                    rsSlabsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CHANGED",true);
                    rsSlabsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CANCELLED",false);
                    rsSlabsHistory.insertRow();
                }
            }
            //=========================================//
            
            //======== Update the Approval Flow =========//
            
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("POLICY_ID").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_SAL_POLICY_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="POLICY_ID";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=EITLERPGLOBAL.DatabaseURL;
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            ObjFlow.UseSpecifiedURL=false;
            
            //--------- Approval Flow Update complete -----------
            
            //Conn.commit();
            //data.SetCommit();
            
            //data.SetAutoCommit(true);
            
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
        boolean Validate=true;
        
        try {
            
            String theDocNo=getAttribute("POLICY_ID").getString();
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_MASTER_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //Start Transaction
            //Conn.setAutoCommit(false);
            //data.SetAutoCommit(false);
            
            //rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            //rsResultSet.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsResultSet.updateString("POLICY_NAME",getAttribute("POLICY_NAME").getString());
            rsResultSet.updateInt("DISCOUNT_TYPE",getAttribute("DISCOUNT_TYPE").getInt());
            rsResultSet.updateInt("PARTY_TYPE",getAttribute("PARTY_TYPE").getInt());
            rsResultSet.updateString("INVOICE_TYPES",getAttribute("INVOICE_TYPES").getString());
            rsResultSet.updateInt("APPLICABILITY",getAttribute("APPLICABILITY").getInt());
            rsResultSet.updateInt("APP_AMOUNT_LIMIT",getAttribute("APP_AMOUNT_LIMIT").getInt());
            rsResultSet.updateDouble("FLAT_PERCENTAGE",getAttribute("FLAT_PERCENTAGE").getDouble());
            rsResultSet.updateInt("DISCOUNT_PERCENTAGE",getAttribute("DISCOUNT_PERCENTAGE").getInt());
            rsResultSet.updateInt("PERIOD_BASE",getAttribute("PERIOD_BASE").getInt());
            rsResultSet.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsResultSet.updateString("EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPIRY_DATE").getString()));
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACOUNT_CODE").getString());
            rsResultSet.updateInt("TURNOVER_CALC_TYPE",getAttribute("TURNOVER_CALC_TYPE").getInt());
            rsResultSet.updateString("DEDUCTION_CODES",getAttribute("DEDUCTION_CODES").getString());
            rsResultSet.updateInt("PERIOD",getAttribute("PERIOD").getInt());
            rsResultSet.updateInt("INVOICE_MAIN_TYPE",getAttribute("INVOICE_MAIN_TYPE").getInt());
            rsResultSet.updateInt("CREDIT_NOTE_TYPE",getAttribute("CREDIT_NOTE_TYPE").getInt());
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_MASTER_H WHERE POLICY_ID='"+theDocNo+"'");
            
            RevNo++;
            
            String RevDocNo=getAttribute("POLICY_ID").getString();
            //System.out.println("RevNo="+RevNo);
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
            rsHistory.updateString("POLICY_NAME",getAttribute("POLICY_NAME").getString());
            rsHistory.updateInt("DISCOUNT_TYPE",getAttribute("DISCOUNT_TYPE").getInt());
            rsHistory.updateInt("PARTY_TYPE",getAttribute("PARTY_TYPE").getInt());
            rsHistory.updateString("INVOICE_TYPES",getAttribute("INVOICE_TYPES").getString());
            rsHistory.updateInt("APPLICABILITY",getAttribute("APPLICABILITY").getInt());
            rsHistory.updateInt("APP_AMOUNT_LIMIT",getAttribute("APP_AMOUNT_LIMIT").getInt());
            rsHistory.updateDouble("FLAT_PERCENTAGE",getAttribute("FLAT_PERCENTAGE").getDouble());
            rsHistory.updateInt("DISCOUNT_PERCENTAGE",getAttribute("DISCOUNT_PERCENTAGE").getInt());
            rsHistory.updateInt("PERIOD_BASE",getAttribute("PERIOD_BASE").getInt());
            rsHistory.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateString("EXPIRY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EXPIRY_DATE").getString()));
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACOUNT_CODE").getString());
            rsHistory.updateInt("TURNOVER_CALC_TYPE",getAttribute("TURNOVER_CALC_TYPE").getInt());
            rsHistory.updateString("DEDUCTION_CODES",getAttribute("DEDUCTION_CODES").getString());
            rsHistory.updateInt("PERIOD", getAttribute("PERIOD").getInt());
            rsHistory.updateInt("INVOICE_MAIN_TYPE",getAttribute("INVOICE_MAIN_TYPE").getInt());
            rsHistory.updateInt("CREDIT_NOTE_TYPE",getAttribute("CREDIT_NOTE_TYPE").getInt());
            
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_PARTIES WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND POLICY_ID='"+getAttribute("POLICY_ID").getString()+"'");
            
            ResultSet rsHParty,rsParty;
            Statement stHParty,stParty;
            
            stParty=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsParty=stParty.executeQuery("SELECT * FROM D_SAL_POLICY_PARTIES WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            
            stHParty=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHParty=stHParty.executeQuery("SELECT * FROM D_SAL_POLICY_PARTIES_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
            rsHParty.first();
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colPolicyParties.size();i++) {
                clsPolicyParties objParty=(clsPolicyParties) colPolicyParties.get(Integer.toString(i));
                
                Counter++;
                
                rsParty.moveToInsertRow();
                rsParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsParty.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                rsParty.updateInt("SR_NO",Counter);
                rsParty.updateString("PARTY_MAIN_CODE",objParty.getAttribute("PARTY_MAIN_CODE").getString());
                rsParty.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsParty.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsParty.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsParty.updateBoolean("CHANGED",true);
                rsParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsParty.updateBoolean("CANCELLED",false);
                
                rsParty.insertRow();
                
                rsHParty.moveToInsertRow();
                rsHParty.updateInt("REVISION_NO",RevNo);
                rsHParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHParty.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                rsHParty.updateInt("SR_NO",Counter);
                rsHParty.updateString("PARTY_MAIN_CODE",objParty.getAttribute("PARTY_MAIN_CODE").getString());
                rsHParty.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                rsHParty.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHParty.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHParty.updateBoolean("CHANGED",true);
                rsHParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHParty.updateBoolean("CANCELLED",false);
                
                rsHParty.insertRow();
                
            }
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_PARTY_FLAT_PER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND POLICY_ID='"+getAttribute("POLICY_ID").getString()+"'");            
            
            if (colPolicyPartyPer.size() > 0) {
                ResultSet rsHPartyPer,rsPartyPer;
                Statement stHPartyPer,stPartyPer;
                
                stPartyPer=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsPartyPer=stPartyPer.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_FLAT_PER  WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                
                
                stHPartyPer=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsHPartyPer=stHPartyPer.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_FLAT_PER_H  WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsHPartyPer.first();
                
                
                //Now Insert records into detail table
                for(int i=1;i<=colPolicyPartyPer.size();i++) {
                    clsPolicyPartyFlatPer objParty=(clsPolicyPartyFlatPer) colPolicyPartyPer.get(Integer.toString(i));
                    
                    rsPartyPer.moveToInsertRow();
                    rsPartyPer.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsPartyPer.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                    rsPartyPer.updateInt("SR_NO",i);
                    rsPartyPer.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                    rsPartyPer.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                    rsPartyPer.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                    rsPartyPer.updateString("PRODUCT_CODE",objParty.getAttribute("PRODUCT_CODE").getString());                    
                    rsPartyPer.updateDouble("FLAT_PERCENTAGE",objParty.getAttribute("FLAT_PERCENTAGE").getDouble());                    
                    rsPartyPer.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsPartyPer.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsPartyPer.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsPartyPer.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsPartyPer.updateBoolean("CHANGED",false);
                    rsPartyPer.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsPartyPer.updateBoolean("CANCELLED",false);
                    
                    rsPartyPer.insertRow();
                    
                    rsHPartyPer.moveToInsertRow();
                    rsHPartyPer.updateInt("REVISION_NO",RevNo);
                    rsHPartyPer.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsHPartyPer.updateString("POLICY_ID",getAttribute("POLICY_ID").getString());
                    rsHPartyPer.updateInt("SR_NO",i);
                    rsHPartyPer.updateString("PARTY_ID",objParty.getAttribute("PARTY_ID").getString());
                    rsHPartyPer.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("FROM_DATE").getString()));
                    rsHPartyPer.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objParty.getAttribute("TO_DATE").getString()));
                    rsHPartyPer.updateString("PRODUCT_CODE",objParty.getAttribute("PRODUCT_CODE").getString());                    
                    rsHPartyPer.updateDouble("FLAT_PERCENTAGE",objParty.getAttribute("FLAT_PERCENTAGE").getDouble());
                    rsHPartyPer.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsHPartyPer.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHPartyPer.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsHPartyPer.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHPartyPer.updateBoolean("CHANGED",false);
                    rsHPartyPer.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHPartyPer.updateBoolean("CANCELLED",false);
                    
                    rsHPartyPer.insertRow();
                    
                }
            }
            
            //Remove Old Records
            data.Execute("DELETE FROM D_SAL_POLICY_TURNOVER_SLABS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND POLICY_ID='"+getAttribute("POLICY_ID").getString()+"'");
            
            data.Execute("DELETE FROM D_SAL_POLICY_SEASON_SLABS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND POLICY_ID='"+getAttribute("POLICY_ID").getString()+"'");
            
            
            //==================slabs add==============//
            Statement stSlabsHistory,stSlabsHeader;
            ResultSet rsSlabsHistory,rsSlabsHeader;
            if (colPolicyTurnOverSlab.size() > 0) {
                
                stSlabsHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHeader=stSlabsHeader.executeQuery("SELECT * FROM D_SAL_POLICY_TURNOVER_SLABS WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHeader.first();
                
                stSlabsHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHistory=stSlabsHistory.executeQuery("SELECT * FROM D_SAL_POLICY_TURNOVER_SLABS_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHistory.first();
                
                for(int i=1;i<=colPolicyTurnOverSlab.size();i++) {
                    clsPolicyTurnoverSlabs objSlabs=(clsPolicyTurnoverSlabs) colPolicyTurnOverSlab.get(Integer.toString(i));
                    
                    rsSlabsHeader.first();
                    rsSlabsHeader.moveToInsertRow();
                    rsSlabsHeader.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHeader.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHeader.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHeader.updateDouble("FROM_VALUE",objSlabs.getAttribute("FROM_VALUE").getDouble());
                    rsSlabsHeader.updateDouble("TO_VALUE",objSlabs.getAttribute("TO_VALUE").getDouble());
                    rsSlabsHeader.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    
                    rsSlabsHeader.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CHANGED",true);
                    rsSlabsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CANCELLED",false);
                    rsSlabsHeader.insertRow();
                    
                    
                    //========= Inserting Into History =================//
                    rsSlabsHistory.moveToInsertRow();
                    rsSlabsHistory.updateInt("REVISION_NO",RevNo);
                    rsSlabsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHistory.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHistory.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHistory.updateDouble("FROM_VALUE",objSlabs.getAttribute("FROM_VALUE").getDouble());
                    rsSlabsHistory.updateDouble("TO_VALUE",objSlabs.getAttribute("TO_VALUE").getDouble());
                    rsSlabsHistory.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    
                    rsSlabsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CHANGED",true);
                    rsSlabsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CANCELLED",false);
                    rsSlabsHistory.insertRow();
                }
                
                
            }
            
            if (colPolicySeasonSlab.size() > 0) {
                
                
                stSlabsHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHeader=stSlabsHeader.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHeader.first();
                
                stSlabsHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsSlabsHistory=stSlabsHistory.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS_H WHERE POLICY_ID='1'"); // '1' for restricting all data retrieval
                rsSlabsHistory.first();
                
                for(int i=1;i<=colPolicySeasonSlab.size();i++) {
                    clsPolicySeasonSlabs objSlabs=(clsPolicySeasonSlabs) colPolicySeasonSlab.get(Integer.toString(i));
                    
                    rsSlabsHeader.first();
                    rsSlabsHeader.moveToInsertRow();
                    rsSlabsHeader.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHeader.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHeader.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHeader.updateString("SEASON_ID",objSlabs.getAttribute("SEASON_ID").getString());
                    rsSlabsHeader.updateString("QUALITY_ID",objSlabs.getAttribute("QUALITY_ID").getString());
                    rsSlabsHeader.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("FROM_DATE").getString()));
                    rsSlabsHeader.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("TO_DATE").getString()));
                    rsSlabsHeader.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    
                    rsSlabsHeader.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHeader.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CHANGED",true);
                    rsSlabsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHeader.updateBoolean("CANCELLED",false);
                    rsSlabsHeader.insertRow();
                    
                    
                    //========= Inserting Into History =================//
                    rsSlabsHistory.moveToInsertRow();
                    rsSlabsHistory.updateInt("REVISION_NO",RevNo);
                    rsSlabsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsSlabsHistory.updateString("POLICY_ID",objSlabs.getAttribute("POLICY_ID").getString());
                    rsSlabsHistory.updateInt("SR_NO",objSlabs.getAttribute("SR_NO").getInt());
                    rsSlabsHistory.updateString("SEASON_ID",objSlabs.getAttribute("SEASON_ID").getString());
                    rsSlabsHistory.updateString("QUALITY_ID",objSlabs.getAttribute("QUALITY_ID").getString());
                    rsSlabsHistory.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("FROM_DATE").getString()));
                    rsSlabsHistory.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(objSlabs.getAttribute("TO_DATE").getString()));
                    rsSlabsHistory.updateDouble("PERCENTAGE",objSlabs.getAttribute("PERCENTAGE").getDouble());
                    
                    rsSlabsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                    rsSlabsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CHANGED",true);
                    rsSlabsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSlabsHistory.updateBoolean("CANCELLED",false);
                    rsSlabsHistory.insertRow();
                }
            }
            //=========================================//
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("POLICY_ID").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_SAL_POLICY_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="POLICY_ID";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=EITLERPGLOBAL.DatabaseURL;
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_SAL_POLICY_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE POLICY_ID='"+theDocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='"+theDocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            if(ObjFlow.Status.equals("H")) {
                if(IsRejected) {
                    ObjFlow.Status="A";
                    ObjFlow.To=ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            
            ObjFlow.UseSpecifiedURL=false;
            
            //Conn.commit();
            //data.SetCommit();
            
            //data.SetAutoCommit(true);
            
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //data.SetRollback();
            }
            catch(Exception c){}
            //data.SetAutoCommit(true);
            
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean CanDelete(int CompanyID,String PolicyID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+PolicyID+"' AND (APPROVED=1)";
            int Count=data.getIntValueFromDB(strSQL);
            
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
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int CompanyID,String PolicyID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT POLICY_ID FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+PolicyID+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+PolicyID+"' AND USER_ID="+UserID+" AND STATUS='W'";
                if(data.IsRecordExist(strSQL)) {
                    //Yes document is waiting for this user
                    return true;
                }
                else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public boolean Delete(int UserID) {
        try {
            String PolicyID=getAttribute("POLICY_ID").getString();
            String strSQL="";
            if(CanDelete(EITLERPGLOBAL.gCompanyID,PolicyID,UserID)) {
                strSQL = "DELETE FROM D_SAL_POLICY_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+PolicyID+"'";
                data.Execute(strSQL);
                
                strSQL = "DELETE FROM D_SAL_POLICY_PARTIES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+PolicyID+"'";
                data.Execute(strSQL);
                
                strSQL = "DELETE FROM D_SAL_POLICY_TURNOVER_SLABS WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+PolicyID+"'";
                data.Execute(strSQL);
                
                strSQL = "DELETE FROM D_SAL_POLICY_SEASON_SLABS WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID +" AND POLICY_ID='"+PolicyID+"'";
                data.Execute(strSQL);
                
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
    
    
    public Object getObject(int CompanyID, String PolicyID) {
        String strCondition = " WHERE POLICY_ID='" + PolicyID + "' AND COMPANY_ID=" + CompanyID;
        clsPolicyMaster objPolicy = new clsPolicyMaster();
        objPolicy.Filter(strCondition,CompanyID);
        return objPolicy;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_POLICY_MASTER " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_MASTER ORDER BY POLICY_ID ";
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
            if(HistoryView) {
                RevNo=UtilFunctions.getInt(rsResultSet,"REVISION_NO",0);
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("POLICY_ID",UtilFunctions.getString(rsResultSet,"POLICY_ID", ""));
            setAttribute("POLICY_NAME",UtilFunctions.getString(rsResultSet,"POLICY_NAME", ""));
            setAttribute("DISCOUNT_TYPE",UtilFunctions.getInt(rsResultSet,"DISCOUNT_TYPE", 0));
            setAttribute("PARTY_TYPE",UtilFunctions.getInt(rsResultSet,"PARTY_TYPE", 0));
            setAttribute("INVOICE_TYPES",UtilFunctions.getString(rsResultSet,"INVOICE_TYPES", ""));
            setAttribute("APPLICABILITY",UtilFunctions.getInt(rsResultSet,"APPLICABILITY", 0));
            setAttribute("APP_AMOUNT_LIMIT",UtilFunctions.getInt(rsResultSet,"APP_AMOUNT_LIMIT", 0));
            setAttribute("FLAT_PERCENTAGE",UtilFunctions.getDouble(rsResultSet,"FLAT_PERCENTAGE", 0));
            setAttribute("DISCOUNT_PERCENTAGE",UtilFunctions.getInt(rsResultSet,"DISCOUNT_PERCENTAGE", 0));
            setAttribute("PERIOD_BASE",UtilFunctions.getInt(rsResultSet,"PERIOD_BASE", 0));
            setAttribute("EFFECTIVE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"EFFECTIVE_DATE","0000-00-00")));
            setAttribute("EXPIRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"EXPIRY_DATE","0000-00-00")));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS", ""));
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE", ""));
            setAttribute("TURNOVER_CALC_TYPE",UtilFunctions.getInt(rsResultSet,"TURNOVER_CALC_TYPE", 0));
            setAttribute("DEDUCTION_CODES",UtilFunctions.getString(rsResultSet,"DEDUCTION_CODES", ""));
            setAttribute("PERIOD",UtilFunctions.getInt(rsResultSet,"PERIOD",0));
            setAttribute("INVOICE_MAIN_TYPE",UtilFunctions.getInt(rsResultSet,"INVOICE_MAIN_TYPE",0));
            setAttribute("CREDIT_NOTE_TYPE",UtilFunctions.getInt(rsResultSet,"CREDIT_NOTE_TYPE",0));
            
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("APPROVED",UtilFunctions.getBoolean(rsResultSet,"APPROVED",false));
            setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            setAttribute("REJECTED",UtilFunctions.getBoolean(rsResultSet,"REJECTED",false));
            setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            
            colPolicyParties.clear();
            String PolicyId=getAttribute("POLICY_ID").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTIES_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
                //System.out.println("hi"+"SELECT * FROM D_SAL_POLICY_PARTIES_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND SCHEME_ID='" + UtilFunctions.getString(rsResultSet,"SCHEME_ID", "") + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTIES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' ORDER BY SR_NO");
                //System.out.println("hi1"+"SELECT * FROM D_SAL_POLICY_PARTIES WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND SCHEME_ID='" + UtilFunctions.getString(rsResultSet,"SCHEME_ID", "") + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicyParties objItem = new clsPolicyParties();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("POLICY_ID",UtilFunctions.getString(rsTmp,"POLICY_ID",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("PARTY_MAIN_CODE",UtilFunctions.getString(rsTmp,"PARTY_MAIN_CODE",""));
                objItem.setAttribute("PARTY_ID",UtilFunctions.getString(rsTmp,"PARTY_ID",""));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                colPolicyParties.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            colPolicyPartyPer.clear();
            PolicyId=getAttribute("POLICY_ID").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_FLAT_PER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");                
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_FLAT_PER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' ORDER BY SR_NO");                
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicyPartyFlatPer objItem = new clsPolicyPartyFlatPer();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("POLICY_ID",UtilFunctions.getString(rsTmp,"POLICY_ID",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));                
                objItem.setAttribute("PARTY_ID",UtilFunctions.getString(rsTmp,"PARTY_ID",""));
                objItem.setAttribute("FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"FROM_DATE","0000-00-00")));
                objItem.setAttribute("TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"TO_DATE","0000-00-00")));
                objItem.setAttribute("PRODUCT_CODE",UtilFunctions.getString(rsTmp,"PRODUCT_CODE",""));
                objItem.setAttribute("FLAT_PERCENTAGE",UtilFunctions.getDouble(rsTmp,"FLAT_PERCENTAGE", 0));
                String PartyCode = UtilFunctions.getString(rsTmp,"PARTY_ID","");
                objItem.setAttribute("PARTY_MAIN_CODE",clsPolicyMaster.getMainAccountCode(EITLERPGLOBAL.gCompanyID,PartyCode));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                colPolicyPartyPer.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            
            colPolicySeasonSlab.clear();
            PolicyId=getAttribute("POLICY_ID").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_SEASON_SLABS WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicySeasonSlabs objSlabs = new clsPolicySeasonSlabs();
                
                objSlabs.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objSlabs.setAttribute("POLICY_ID",UtilFunctions.getString(rsTmp,"POLICY_ID",""));
                objSlabs.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objSlabs.setAttribute("SEASON_ID",UtilFunctions.getString(rsTmp,"SEASON_ID",""));
                objSlabs.setAttribute("QUALITY_ID",UtilFunctions.getString(rsTmp,"QUALITY_ID",""));
                objSlabs.setAttribute("FROM_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"FROM_DATE","0000-00-00")));
                objSlabs.setAttribute("TO_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"TO_DATE","0000-00-00")));
                objSlabs.setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsTmp,"PERCENTAGE",0));
                objSlabs.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objSlabs.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objSlabs.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objSlabs.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objSlabs.setAttribute("CHANGED",true);
                objSlabs.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objSlabs.setAttribute("CANCELLED",0);
                colPolicySeasonSlab.put(Long.toString(Counter),objSlabs);
                rsTmp.next();
            }
            
            colPolicyTurnOverSlab.clear();
            PolicyId=getAttribute("POLICY_ID").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_TURNOVER_SLABS_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_TURNOVER_SLABS WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND POLICY_ID='" + PolicyId + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsPolicyTurnoverSlabs objSlabs = new clsPolicyTurnoverSlabs();
                
                objSlabs.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objSlabs.setAttribute("POLICY_ID",UtilFunctions.getString(rsTmp,"POLICY_ID",""));
                objSlabs.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objSlabs.setAttribute("FROM_VALUE",UtilFunctions.getDouble(rsTmp,"FROM_VALUE",0));
                objSlabs.setAttribute("TO_VALUE",UtilFunctions.getDouble(rsTmp,"TO_VALUE",0));
                objSlabs.setAttribute("PERCENTAGE",UtilFunctions.getDouble(rsTmp,"PERCENTAGE",0));
                objSlabs.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objSlabs.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objSlabs.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objSlabs.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objSlabs.setAttribute("CHANGED",true);
                objSlabs.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objSlabs.setAttribute("CANCELLED",0);
                colPolicyTurnOverSlab.put(Long.toString(Counter),objSlabs);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order) {
        ResultSet rsTemp=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_SAL_POLICY_MASTER.POLICY_ID,D_SAL_POLICY_MASTER.POLICY_NAME,D_COM_DOC_DATA.RECEIVED_DATE FROM D_SAL_POLICY_MASTER,D_COM_DOC_DATA WHERE D_SAL_POLICY_MASTER.POLICY_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_DOC_DATA.USER_ID="+UserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_SAL_POLICY_MASTER.POLICY_ID,D_SAL_POLICY_MASTER.POLICY_NAME,D_COM_DOC_DATA.RECEIVED_DATE FROM D_SAL_POLICY_MASTER,D_COM_DOC_DATA WHERE D_SAL_POLICY_MASTER.POLICY_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_DOC_DATA.USER_ID="+UserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_SAL_POLICY_MASTER.POLICY_ID";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_SAL_POLICY_MASTER.POLICY_ID,D_SAL_POLICY_MASTER.POLICY_NAME,D_COM_DOC_DATA.RECEIVED_DATE FROM D_SAL_POLICY_MASTER,D_COM_DOC_DATA WHERE D_SAL_POLICY_MASTER.POLICY_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_DOC_DATA.USER_ID="+UserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_SAL_POLICY_MASTER.POLICY_ID";
            }
            
            rsTemp=data.getResult(strSQL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsPolicyMaster ObjPolicy=new clsPolicyMaster();
                    
                    //------------- Header Fields --------------------//
                    ObjPolicy.setAttribute("POLICY_ID",UtilFunctions.getString(rsTemp,"POLICY_ID",""));
                    ObjPolicy.setAttribute("POLICY_NAME",UtilFunctions.getString(rsTemp,"POLICY_NAME",""));
                    ObjPolicy.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjPolicy);
                    rsTemp.next();
                }
            }
            rsTemp.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean ShowHistory(int CompanyID,String PolicyID) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+PolicyID + "'";
            rsResultSet=data.getResult(strSQL);
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getHistoryList(int CompanyID,String PolicyID) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_SAL_POLICY_MASTER_H WHERE POLICY_ID='"+PolicyID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsPolicyMaster objPolicy=new clsPolicyMaster();
                    
                    objPolicy.setAttribute("POLICY_ID",UtilFunctions.getString(rsTmp,"POLICY_ID", ""));
                    objPolicy.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objPolicy.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objPolicy.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objPolicy.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objPolicy.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objPolicy);
                    rsTmp.next();
                }
            }
            rsTmp.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    public static String getDocStatus(int CompanyID,String PolicyID) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT POLICY_ID,APPROVED,CANCELLED FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+PolicyID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(UtilFunctions.getBoolean(rsTmp,"APPROVED",false))   {
                    if(UtilFunctions.getBoolean(rsTmp,"CANCELLED",false))  {
                        strMessage="Document is cancelled";
                    }
                    else {
                        strMessage="";
                    }
                }
                else {
                    strMessage="Document is created but is under approval";
                }
                
            }
            else {
                strMessage="Document does not exist";
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return strMessage;
        
    }
    
    
    public static boolean CanCancel(int CompanyID,String PolicyID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT POLICY_ID FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+PolicyID+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                String sPolicy_ID=UtilFunctions.getString(rsTmp,"POLICY_ID","");
                
                if(data.IsRecordExist("SELECT * FROM D_SAL_POLICY_MASTER WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND POLICY_ID='"+sPolicy_ID+"'")) {
                    canCancel=false;
                }
                else {
                    canCancel=true;
                }
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return canCancel;
    }
    
    
    
    public static boolean CancelDoc(int CompanyID,String PolicyID) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,PolicyID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_SAL_POLICY_MASTER WHERE POLICY_ID="+PolicyID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+PolicyID+"' AND MODULE_ID="+ModuleID);
                }
                
                data.Execute("UPDATE D_SAL_POLICY_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE POLICY_ID="+PolicyID);
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    
    
    public static String getPolicyName(int CompanyID,String PolicyID) {
        try {
            return data.getStringValueFromDB("SELECT POLICY_NAME FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+PolicyID+"' AND COMPANY_ID="+CompanyID);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getQualityName(int CompanyID,String QualityID) {
        try {
            return data.getStringValueFromDB("SELECT QUALITY_NAME FROM D_SAL_QUALITY_MASTER WHERE QUALITY_ID='"+QualityID+"' AND COMPANY_ID="+CompanyID);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getSeasonName(int CompanyID,String SeasonID) {
        try {
            return data.getStringValueFromDB("SELECT SEASON_NAME FROM D_SAL_SEASON_MASTER WHERE SEASON_ID='"+SeasonID+"' AND COMPANY_ID="+CompanyID);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMainAccountCode(int CompanyID,String PartyID) {
        try {
            return data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE = '"+ PartyID.trim() +"' AND COMPANY_ID="+CompanyID,FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getMainAccountName(int CompanyID,String MainAccountCode) {
        try {
            return data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE = '"+ MainAccountCode.trim() +"' AND COMPANY_ID="+CompanyID,FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPartyName(int CompanyID,String PartyID) {
        try {
            return data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE = '"+ PartyID.trim() +"' AND COMPANY_ID="+CompanyID,FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPartyName(int CompanyID,String PartyID,String MainAccountCode) {
        try {
            return data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE = '"+ PartyID.trim() +"' AND COMPANY_ID="+CompanyID+" AND MAIN_ACCOUNT_CODE = '"+ MainAccountCode.trim() +"'",FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static Object getObjectEx(int pCompanyID,String pPolicyID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND POLICY_ID='" + pPolicyID+"' ";
        clsPolicyMaster ObjPolicy = new clsPolicyMaster();
        ObjPolicy.LoadData(pCompanyID);
        ObjPolicy.Filter(strCondition,pCompanyID);
        return ObjPolicy;
    }
    
    public static String getPolicyInvoice(int CompanyID,String PolicyID) {
       try {
           return data.getStringValueFromDB("SELECT INVOICE_TYPES FROM D_SAL_POLICY_MASTER WHERE POLICY_ID='"+PolicyID+"' AND COMPANY_ID="+CompanyID);
       }
       catch(Exception e) {
           return "";
       }
   }  
}
