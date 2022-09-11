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

public class clsDepositSchemeMaster{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public HashMap colSchemePeriod=new HashMap();
    
    public static int ModuleID=84;
    
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
    public clsDepositSchemeMaster() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SCHEME_ID",new Variant(""));
        props.put("SCHEME_TYPE", new Variant(0));
        props.put("FFNO", new Variant(0));
        props.put("SCHEME_NAME",new Variant(""));
        props.put("MINIMUM_AMOUNT",new Variant(0));
        props.put("MINIMUM_PERIOD",new Variant(0));
        props.put("INTEREST_CALCULATION_TYPE",new Variant(0));
        props.put("INTEREST_CALCULATION_PERIOD",new Variant(0));
        props.put("DEPOSIT_TYPE_ID",new Variant(0));
        props.put("INT_MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("DEPOSIT_MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("PM_DEDUCTION_PERCENTAGE",new Variant(0));
        props.put("DEPOSIT_TYPE_ID",new Variant(0));
        props.put("DEPOSIT_TYPE_ID",new Variant(0));
        
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
        
        colSchemePeriod=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_SCHEME_MASTER WHERE COMPANY_ID="+pCompanyID+" ORDER BY SCHEME_ID");
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            if(data.IsRecordExist("SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+getAttribute("SCHEME_ID").getString()+"'",FinanceGlobal.FinURL)) {
                LastError="Scheme Id already exist.";
                return false;
            }
            //*****************//
            //======================Generate Scheme ID========================
            String newSchemeID = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID,84,getAttribute("FFNO").getInt(),true);
            setAttribute("SCHEME_ID",newSchemeID);
            //================================================================
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
            rsResultSet.updateInt("SCHEME_TYPE",getAttribute("SCHEME_TYPE").getInt());
            rsResultSet.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
            rsResultSet.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
            rsResultSet.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
            rsResultSet.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
            rsResultSet.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
            rsResultSet.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
            rsResultSet.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsResultSet.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
            
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
            rsHistory.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
            rsHistory.updateInt("SCHEME_TYPE",getAttribute("SCHEME_TYPE").getInt());
            rsHistory.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
            rsHistory.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
            rsHistory.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
            rsHistory.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
            rsHistory.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
            rsHistory.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
            rsHistory.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsHistory.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
            
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",false);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            ResultSet rsHDetail,rsPeriod;
            Statement stHDetail,stPeriod;
            
            stPeriod=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsPeriod=stPeriod.executeQuery("SELECT * FROM D_FD_SCHEME_PERIOD  WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
            
            
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_SCHEME_PERIOD_H  WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
            rsHDetail.first();
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colSchemePeriod.size();i++) {
                clsDepositSchemePeriod objItem=(clsDepositSchemePeriod) colSchemePeriod.get(Integer.toString(i));
                //System.out.println("int_month=" + Int_Month);
                rsPeriod.moveToInsertRow();
                rsPeriod.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsPeriod.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
                rsPeriod.updateInt("SR_NO",i);
                rsPeriod.updateDouble("INTEREST_MONTH",objItem.getAttribute("INTEREST_MONTH").getInt());
                rsPeriod.updateDouble("INTEREST_PERCENTAGE", objItem.getAttribute("INTEREST_PERCENTAGE").getDouble());
                rsPeriod.updateDouble("PERCENTAGE_OF_BROKER", objItem.getAttribute("PERCENTAGE_OF_BROKER").getDouble());
                rsPeriod.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsPeriod.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsPeriod.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsPeriod.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsPeriod.updateBoolean("CHANGED",false);
                rsPeriod.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsPeriod.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateDouble("INTEREST_MONTH",objItem.getAttribute("INTEREST_MONTH").getInt());
                rsHDetail.updateDouble("INTEREST_PERCENTAGE", objItem.getAttribute("INTEREST_PERCENTAGE").getDouble());
                rsHDetail.updateDouble("PERCENTAGE_OF_BROKER", objItem.getAttribute("PERCENTAGE_OF_BROKER").getDouble());
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",false);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========//
            
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("SCHEME_ID").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_SCHEME_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="SCHEME_ID";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            
            
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
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
        boolean Validate=true;
        
        try {
            
            String theDocNo=getAttribute("SCHEME_ID").getString();
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
            rsResultSet.updateInt("SCHEME_TYPE",getAttribute("SCHEME_TYPE").getInt());
            rsResultSet.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
            rsResultSet.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
            rsResultSet.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
            rsResultSet.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
            rsResultSet.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
            rsResultSet.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsResultSet.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
           // rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
           // rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='"+theDocNo+"'",FinanceGlobal.FinURL);
            
            RevNo++;
            
            String RevDocNo=getAttribute("SCHEME_ID").getString();
            
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
            rsHistory.updateInt("SCHEME_TYPE",getAttribute("SCHEME_TYPE").getInt());
            rsHistory.updateString("SCHEME_NAME",getAttribute("SCHEME_NAME").getString());
            rsHistory.updateDouble("MINIMUM_AMOUNT",getAttribute("MINIMUM_AMOUNT").getDouble());
            rsHistory.updateDouble("MINIMUM_PERIOD",getAttribute("MINIMUM_PERIOD").getDouble());
            rsHistory.updateInt("INTEREST_CALCULATION_TYPE",getAttribute("INTEREST_CALCULATION_TYPE").getInt());
            rsHistory.updateInt("INTEREST_CALCULATION_PERIOD",getAttribute("INTEREST_CALCULATION_PERIOD").getInt());
            rsHistory.updateInt("DEPOSIT_TYPE_ID",getAttribute("DEPOSIT_TYPE_ID").getInt());
            rsHistory.updateString("INT_MAIN_ACCOUNT_CODE",getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateString("DEPOSIT_MAIN_ACCOUNT_CODE",getAttribute("DEPOSIT_MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateDouble("PM_DEDUCTION_PERCENTAGE",getAttribute("PM_DEDUCTION_PERCENTAGE").getDouble());
            
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
           // rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
          //  rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            data.Execute("DELETE FROM D_FD_SCHEME_PERIOD WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SCHEME_ID='"+getAttribute("SCHEME_ID").getString()+"'",FinanceGlobal.FinURL);
            //data.Execute("DELETE FROM D_FD_SCHEME_PERIOD_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SCHEME_ID='"+getAttribute("SCHEME_ID").getString()+"'",FinanceGlobal.FinURL);
            
            ResultSet rsHDetail,rsPeriod;
            Statement stHDetail,stPeriod;
            
            stPeriod=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsPeriod=stPeriod.executeQuery("SELECT * FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
            
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_SCHEME_PERIOD_H WHERE SCHEME_ID='1'"); // '1' for restricting all data retrieval
            rsHDetail.first();
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colSchemePeriod.size();i++) {
                clsDepositSchemePeriod objItem=(clsDepositSchemePeriod) colSchemePeriod.get(Integer.toString(i));
                Counter++;
                
                rsPeriod.moveToInsertRow();
                rsPeriod.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsPeriod.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
                rsPeriod.updateInt("SR_NO",Counter);
                rsPeriod.updateInt("INTEREST_MONTH",objItem.getAttribute("INTEREST_MONTH").getInt());
                rsPeriod.updateDouble("INTEREST_PERCENTAGE", objItem.getAttribute("INTEREST_PERCENTAGE").getDouble());
                rsPeriod.updateDouble("PERCENTAGE_OF_BROKER", objItem.getAttribute("PERCENTAGE_OF_BROKER").getDouble());
                rsPeriod.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsPeriod.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsPeriod.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsPeriod.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsPeriod.updateBoolean("CHANGED",true);
                rsPeriod.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsPeriod.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateString("SCHEME_ID",getAttribute("SCHEME_ID").getString());
                rsHDetail.updateInt("SR_NO",Counter);
                rsHDetail.updateInt("INTEREST_MONTH",objItem.getAttribute("INTEREST_MONTH").getInt());
                rsHDetail.updateDouble("INTEREST_PERCENTAGE", objItem.getAttribute("INTEREST_PERCENTAGE").getDouble());
                rsHDetail.updateDouble("PERCENTAGE_OF_BROKER", objItem.getAttribute("PERCENTAGE_OF_BROKER").getDouble());
             //   rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
             //   rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("SCHEME_ID").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_SCHEME_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="SCHEME_ID";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FD_SCHEME_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SCHEME_ID='"+theDocNo+"'",FinanceGlobal.FinURL);
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
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean CanDelete(int CompanyID,String SchemeID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
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
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int CompanyID,String SchemeID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+SchemeID+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
    
    public Object getObject(int CompanyID, String SchemeID) {
        String strCondition = " WHERE SCHEME_ID='" + SchemeID + "' AND COMPANY_ID="+CompanyID+"";
        clsDepositSchemeMaster objScheme = new clsDepositSchemeMaster();
        objScheme.Filter(strCondition,CompanyID);
        return objScheme;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FD_SCHEME_MASTER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FD_SCHEME_MASTER ORDER BY SCHEME_ID ";
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
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
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
            setAttribute("SCHEME_ID",UtilFunctions.getString(rsResultSet,"SCHEME_ID", ""));
            setAttribute("SCHEME_TYPE",UtilFunctions.getInt(rsResultSet,"SCHEME_TYPE", 0));
            setAttribute("SCHEME_NAME",UtilFunctions.getString(rsResultSet,"SCHEME_NAME", ""));
            setAttribute("MINIMUM_AMOUNT",UtilFunctions.getDouble(rsResultSet,"MINIMUM_AMOUNT", 0));
            setAttribute("MINIMUM_PERIOD",UtilFunctions.getDouble(rsResultSet,"MINIMUM_PERIOD", 0));
            setAttribute("INTEREST_CALCULATION_TYPE",UtilFunctions.getInt(rsResultSet,"INTEREST_CALCULATION_TYPE", 0));
            setAttribute("INTEREST_CALCULATION_PERIOD",UtilFunctions.getInt(rsResultSet,"INTEREST_CALCULATION_PERIOD", 0));
            setAttribute("DEPOSIT_TYPE_ID",UtilFunctions.getInt(rsResultSet,"DEPOSIT_TYPE_ID", 0));
            setAttribute("INT_MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"INT_MAIN_ACCOUNT_CODE", ""));
            setAttribute("DEPOSIT_MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"DEPOSIT_MAIN_ACCOUNT_CODE", ""));
            setAttribute("PM_DEDUCTION_PERCENTAGE",UtilFunctions.getDouble(rsResultSet,"PM_DEDUCTION_PERCENTAGE", 0));
            
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
            
            colSchemePeriod.clear();
            String SchemeId=getAttribute("SCHEME_ID").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_SCHEME_PERIOD_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND SCHEME_ID='" + SchemeId + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
                //System.out.println("hi"+"SELECT * FROM D_FD_SCHEME_PERIOD_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND SCHEME_ID='" + UtilFunctions.getString(rsResultSet,"SCHEME_ID", "") + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_SCHEME_PERIOD WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND SCHEME_ID='" + SchemeId + "' ORDER BY SR_NO");
                //System.out.println("hi1"+"SELECT * FROM D_FD_SCHEME_PERIOD WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND SCHEME_ID='" + UtilFunctions.getString(rsResultSet,"SCHEME_ID", "") + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                clsDepositSchemePeriod objItem = new clsDepositSchemePeriod();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("SCHEME_ID",UtilFunctions.getString(rsTmp,"SCHEME_ID",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("INTEREST_MONTH",UtilFunctions.getInt(rsTmp,"INTEREST_MONTH",0));
                objItem.setAttribute("INTEREST_PERCENTAGE",UtilFunctions.getDouble(rsTmp,"INTEREST_PERCENTAGE", 0.0));
                objItem.setAttribute("PERCENTAGE_OF_BROKER",UtilFunctions.getDouble(rsTmp,"PERCENTAGE_OF_BROKER", 0.0));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                colSchemePeriod.put(Long.toString(Counter),objItem);
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
                strSQL="SELECT FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SCHEME_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SCHEME_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SCHEME_MASTER.SCHEME_ID";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SCHEME_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SCHEME_MASTER.SCHEME_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_SCHEME_MASTER.SCHEME_ID";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsDepositSchemeMaster ObjScheme=new clsDepositSchemeMaster();
                    
                    //------------- Header Fields --------------------//
                    ObjScheme.setAttribute("SCHEME_ID",UtilFunctions.getString(rsTemp,"SCHEME_ID",""));
                    ObjScheme.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjScheme);
                    rsTemp.next();
                }
            }
            rsTemp.close();
        }
        catch(Exception e) {
        }
        return List;
    }
    
    public boolean ShowHistory(int CompanyID,String SchemeID) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID + "'";
            rsResultSet=data.getResult(strSQL,FinanceGlobal.FinURL);
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
    
    public static HashMap getHistoryList(int CompanyID,String SchemeID) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_FD_SCHEME_MASTER_H WHERE SCHEME_ID='"+SchemeID+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDepositSchemeMaster objScheme=new clsDepositSchemeMaster();
                    
                    objScheme.setAttribute("SCHEME_ID",UtilFunctions.getString(rsTmp,"SCHEME_ID", ""));
                    objScheme.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objScheme.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objScheme.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objScheme.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objScheme.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objScheme);
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
    
    public static String getDocStatus(int CompanyID,String SchemeID) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT SCHEME_ID,APPROVED,CANCELLED FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
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
    
    public static boolean CanCancel(int CompanyID,String SchemeID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT SCHEME_ID FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' AND CANCELLED=0",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                String sScheme_ID=UtilFunctions.getString(rsTmp,"SCHEME_ID","");
                
                if(data.IsRecordExist("SELECT * FROM D_FD_SCHEME_MASTER WHERE CANCELLED=0 AND COMPANY_ID="+CompanyID+" AND SCHEME_ID='"+sScheme_ID+"'",FinanceGlobal.FinURL)) {
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
    
    public static boolean CancelDoc(int CompanyID,String SchemeID) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,SchemeID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID="+SchemeID,FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+SchemeID+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FD_SCHEME_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE SCHEME_ID="+SchemeID,FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return Cancelled;
    }
    
    private boolean Validate() {
        //** Validations **//
        if(getAttribute("SCHEME_ID").getString().equals("")) {
            LastError="Please specify Scheme Code";
            return false;
        }
        
        if(getAttribute("SCHEME_NAME").getString().equals("")) {
            LastError="Please specify Scheme Name";
            return false;
        }
        //*****************//
        return true;
    }
    
    public static String getAccountName(int CompanyID,String MainCode) {
        try {
            return data.getStringValueFromDB("SELECT ACCOUNT_NAME FROM D_FIN_GL WHERE MAIN_ACCOUNT_CODE='"+MainCode+"'",FinanceGlobal.FinURL);
        }
        catch(Exception e) {
            return "";
        }
    }
}