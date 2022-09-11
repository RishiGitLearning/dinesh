/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Purchase;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsRateApproval {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap colItems=new HashMap();
    
    //History Related properties
    private boolean HistoryView=false;
    
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
    public clsRateApproval() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("APPROVAL_NO",new Variant(""));
        props.put("APPROVAL_DATE",new Variant(""));
        props.put("INQUIRY_NO",new Variant(""));
        props.put("INQUIRY_DATE",new Variant(""));
        props.put("QUOT_APPROVAL_NO",new Variant(""));
        props.put("QUOT_APPROVAL_DATE",new Variant(""));
        props.put("INDENT_NO",new Variant(""));
        props.put("INDENT_DATE",new Variant(""));
        props.put("REQ_NO",new Variant(""));
        props.put("REQ_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("USER_ID",new Variant(0));
        props.put("CANCELLED",new Variant(false));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND APPROVAL_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY APPROVAL_NO");
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
        Statement stmtDetail,stLot,stHistory,stHDetail;
        ResultSet rsDetail,rsLot,rsHistory,rsHDetail,rsTmp;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("APPROVAL_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Approval date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER_H WHERE APPROVAL_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL_H WHERE APPROVAL_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER WHERE APPROVAL_NO='1'");
            //rsHeader.first();
            
            
            //=========Check That any Summary is already made for this inquiry =========//
            String InquiryNo=(String)getAttribute("INQUIRY_NO").getObj();
            
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsRateApprovalItem ObjItem=(clsRateApprovalItem) colItems.get(Integer.toString(cnt));
                String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                
                
                rsTmp=data.getResult("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER A,D_PUR_RATE_APPROVAL_DETAIL B WHERE A.APPROVAL_NO=B.APPROVAL_NO AND A.INQUIRY_NO='"+InquiryNo+"' AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"'");
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                    LastError="One RIA of item "+ItemID+" has already prepared for this inquiry. Cannot create another summary against this inquiry no.";
                    return false;
                }
                
            }
            //==========================================================================//
            
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("APPROVAL_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,38, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
            rsResultSet.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateString("QUOT_APPROVAL_NO",(String)getAttribute("QUOT_APPROVAL_NO").getObj());
            rsResultSet.updateString("QUOT_APPROVAL_DATE",(String)getAttribute("QUOT_APPROVAL_DATE").getObj());
            rsResultSet.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
            rsResultSet.updateString("INDENT_DATE",(String)getAttribute("INDENT_DATE").getObj());
            rsResultSet.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsResultSet.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsResultSet.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
            rsHistory.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("QUOT_APPROVAL_NO",(String)getAttribute("QUOT_APPROVAL_NO").getObj());
            rsHistory.updateString("QUOT_APPROVAL_DATE",(String)getAttribute("QUOT_APPROVAL_DATE").getObj());
            rsHistory.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
            rsHistory.updateString("INDENT_DATE",(String)getAttribute("INDENT_DATE").getObj());
            rsHistory.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsHistory.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE APPROVAL_NO='1'");
            
            //Now Insert records into detail table
            cnt=0;
            lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsRateApprovalItem ObjItem=(clsRateApprovalItem) colItems.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsDetail.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsDetail.updateString("OTHERS_TERM",(String)ObjItem.getAttribute("OTHERS_TERM").getObj());
                rsDetail.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsDetail.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsDetail.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsDetail.updateDouble("LAST_PO_QTY",ObjItem.getAttribute("LAST_PO_QTY").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE",ObjItem.getAttribute("RATE_DIFFERENCE").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE_PER",ObjItem.getAttribute("RATE_DIFFERENCE_PER").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_RATE").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE_PER_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_PER_RATE").getVal());
                rsDetail.updateDouble("CURRENT_RATE",ObjItem.getAttribute("CURRENT_RATE").getVal());
                rsDetail.updateDouble("CURRENT_LAND_RATE",ObjItem.getAttribute("CURRENT_LAND_RATE").getVal());
                rsDetail.updateDouble("CURRENT_QTY",ObjItem.getAttribute("CURRENT_QTY").getVal());
                rsDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateString("PAYMENT_TERM",(String)ObjItem.getAttribute("PAYMENT_TERM").getObj());
                rsDetail.updateString("PRICE_BASIS_TERM",(String)ObjItem.getAttribute("PRICE_BASIS_TERM").getObj());
                
                rsDetail.updateString("IGST_TERM",(String)ObjItem.getAttribute("IGST_TERM").getObj());
                rsDetail.updateString("CGST_TERM",(String)ObjItem.getAttribute("CGST_TERM").getObj());
                rsDetail.updateString("SGST_TERM",(String)ObjItem.getAttribute("SGST_TERM").getObj());
                rsDetail.updateString("COMPOSITION_TERM",(String)ObjItem.getAttribute("COMPOSITION_TERM").getObj());
                rsDetail.updateString("RCM_TERM",(String)ObjItem.getAttribute("RCM_TERM").getObj());
                rsDetail.updateString("GST_COMPENSATION_CESS_TERM",(String)ObjItem.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
                
                rsDetail.updateString("DISCOUNT_TERM",(String)ObjItem.getAttribute("DISCOUNT_TERM").getObj());
                rsDetail.updateString("EXCISE_TERM",(String)ObjItem.getAttribute("EXCISE_TERM").getObj());
                rsDetail.updateString("ST_TERM",(String)ObjItem.getAttribute("ST_TERM").getObj());
                rsDetail.updateString("PF_TERM",(String)ObjItem.getAttribute("PF_TERM").getObj());
                rsDetail.updateString("FREIGHT_TERM",(String)ObjItem.getAttribute("FREIGHT_TERM").getObj());
                rsDetail.updateString("OCTROI_TERM",(String)ObjItem.getAttribute("OCTROI_TERM").getObj());
                rsDetail.updateString("FOB_TERM",(String)ObjItem.getAttribute("FOB_TERM").getObj());
                rsDetail.updateString("CIE_TERM",(String)ObjItem.getAttribute("CIE_TERM").getObj());
                rsDetail.updateString("INSURANCE_TERM",(String)ObjItem.getAttribute("INSURANCE_TERM").getObj());
                rsDetail.updateString("TCC_TERM",(String)ObjItem.getAttribute("TCC_TERM").getObj());
                rsDetail.updateString("CENVAT_TERM",(String)ObjItem.getAttribute("CENVAT_TERM").getObj());
                rsDetail.updateString("DESPATCH_TERM",(String)ObjItem.getAttribute("DESPATCH_TERM").getObj());
                rsDetail.updateString("SERVICE_TAX_TERM",(String)ObjItem.getAttribute("SERVICE_TAX_TERM").getObj());
                rsDetail.updateInt("UNIT_ID",(int)ObjItem.getAttribute("UNIT_ID").getVal());
                rsDetail.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsHDetail.updateInt("SR_NO",cnt);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateString("OTHERS_TERM",(String)ObjItem.getAttribute("OTHERS_TERM").getObj());
                rsHDetail.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsHDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsHDetail.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsHDetail.updateDouble("LAST_PO_QTY",ObjItem.getAttribute("LAST_PO_QTY").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE",ObjItem.getAttribute("RATE_DIFFERENCE").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE_PER",ObjItem.getAttribute("RATE_DIFFERENCE_PER").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_RATE").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE_PER_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_PER_RATE").getVal());
                rsHDetail.updateDouble("CURRENT_RATE",ObjItem.getAttribute("CURRENT_RATE").getVal());
                rsHDetail.updateDouble("CURRENT_LAND_RATE",ObjItem.getAttribute("CURRENT_LAND_RATE").getVal());
                rsHDetail.updateDouble("CURRENT_QTY",ObjItem.getAttribute("CURRENT_QTY").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("PAYMENT_TERM",(String)ObjItem.getAttribute("PAYMENT_TERM").getObj());
                rsHDetail.updateString("PRICE_BASIS_TERM",(String)ObjItem.getAttribute("PRICE_BASIS_TERM").getObj());
                
                rsHDetail.updateString("IGST_TERM",(String)ObjItem.getAttribute("IGST_TERM").getObj());
                rsHDetail.updateString("CGST_TERM",(String)ObjItem.getAttribute("CGST_TERM").getObj());
                rsHDetail.updateString("SGST_TERM",(String)ObjItem.getAttribute("SGST_TERM").getObj());
                rsHDetail.updateString("COMPOSITION_TERM",(String)ObjItem.getAttribute("COMPOSITION_TERM").getObj());
                rsHDetail.updateString("RCM_TERM",(String)ObjItem.getAttribute("RCM_TERM").getObj());
                rsHDetail.updateString("GST_COMPENSATION_CESS_TERM",(String)ObjItem.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
                
                rsHDetail.updateString("DISCOUNT_TERM",(String)ObjItem.getAttribute("DISCOUNT_TERM").getObj());
                rsHDetail.updateString("EXCISE_TERM",(String)ObjItem.getAttribute("EXCISE_TERM").getObj());
                rsHDetail.updateString("ST_TERM",(String)ObjItem.getAttribute("ST_TERM").getObj());
                rsHDetail.updateString("PF_TERM",(String)ObjItem.getAttribute("PF_TERM").getObj());
                rsHDetail.updateString("FREIGHT_TERM",(String)ObjItem.getAttribute("FREIGHT_TERM").getObj());
                rsHDetail.updateString("OCTROI_TERM",(String)ObjItem.getAttribute("OCTROI_TERM").getObj());
                rsHDetail.updateString("FOB_TERM",(String)ObjItem.getAttribute("FOB_TERM").getObj());
                rsHDetail.updateString("CIE_TERM",(String)ObjItem.getAttribute("CIE_TERM").getObj());
                rsHDetail.updateString("INSURANCE_TERM",(String)ObjItem.getAttribute("INSURANCE_TERM").getObj());
                rsHDetail.updateString("TCC_TERM",(String)ObjItem.getAttribute("TCC_TERM").getObj());
                rsHDetail.updateString("CENVAT_TERM",(String)ObjItem.getAttribute("CENVAT_TERM").getObj());
                rsHDetail.updateString("DESPATCH_TERM",(String)ObjItem.getAttribute("DESPATCH_TERM").getObj());
                rsHDetail.updateString("SERVICE_TAX_TERM",(String)ObjItem.getAttribute("SERVICE_TAX_TERM").getObj());
                rsHDetail.updateInt("UNIT_ID",(int)ObjItem.getAttribute("UNIT_ID").getVal());
                rsHDetail.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsHDetail.insertRow();
            }
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=38; //Rate Approval
            ObjFlow.DocNo=(String)getAttribute("APPROVAL_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_RATE_APPROVAL_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="APPROVAL_NO";
            
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            //==== Handling Rejected Documents ==========//
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
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
            //--------- Approval Flow Update complete -----------
            
            
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
        Statement stmtDetail,stLot,stHistory,stHDetail;
        ResultSet rsDetail,rsLot,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("APPROVAL_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Approval date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER_H WHERE APPROVAL_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL_H WHERE APPROVAL_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(APPROVAL_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            rsResultSet.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateString("QUOT_APPROVAL_NO",(String)getAttribute("QUOT_APPROVAL_NO").getObj());
            rsResultSet.updateString("QUOT_APPROVAL_DATE",(String)getAttribute("QUOT_APPROVAL_DATE").getObj());
            rsResultSet.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
            rsResultSet.updateString("INDENT_DATE",(String)getAttribute("INDENT_DATE").getObj());
            rsResultSet.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsResultSet.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsResultSet.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_RATE_APPROVAL_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+(String)getAttribute("APPROVAL_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
            rsHistory.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("QUOT_APPROVAL_NO",(String)getAttribute("QUOT_APPROVAL_NO").getObj());
            rsHistory.updateString("QUOT_APPROVAL_DATE",(String)getAttribute("QUOT_APPROVAL_DATE").getObj());
            rsHistory.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
            rsHistory.updateString("INDENT_DATE",(String)getAttribute("INDENT_DATE").getObj());
            rsHistory.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsHistory.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //Remove old records
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            String strSQL="DELETE FROM D_PUR_RATE_APPROVAL_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND APPROVAL_NO='"+lDocNo+"'";
            data.Execute(strSQL);
            
            //Insert new records
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE APPROVAL_NO='1'");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsRateApprovalItem ObjItem=(clsRateApprovalItem) colItems.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsDetail.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsDetail.updateString("OTHERS_TERM",(String)ObjItem.getAttribute("OTHERS_TERM").getObj());
                rsDetail.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsDetail.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsDetail.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsDetail.updateDouble("LAST_PO_QTY",ObjItem.getAttribute("LAST_PO_QTY").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE",ObjItem.getAttribute("RATE_DIFFERENCE").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE_PER",ObjItem.getAttribute("RATE_DIFFERENCE_PER").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_RATE").getVal());
                rsDetail.updateDouble("RATE_DIFFERENCE_PER_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_PER_RATE").getVal());
                rsDetail.updateDouble("CURRENT_RATE",ObjItem.getAttribute("CURRENT_RATE").getVal());
                rsDetail.updateDouble("CURRENT_LAND_RATE",ObjItem.getAttribute("CURRENT_LAND_RATE").getVal());
                rsDetail.updateDouble("CURRENT_QTY",ObjItem.getAttribute("CURRENT_QTY").getVal());
                rsDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateString("PAYMENT_TERM",(String)ObjItem.getAttribute("PAYMENT_TERM").getObj());
                rsDetail.updateString("PRICE_BASIS_TERM",(String)ObjItem.getAttribute("PRICE_BASIS_TERM").getObj());
                
                rsDetail.updateString("IGST_TERM",(String)ObjItem.getAttribute("IGST_TERM").getObj());
                rsDetail.updateString("CGST_TERM",(String)ObjItem.getAttribute("CGST_TERM").getObj());
                rsDetail.updateString("SGST_TERM",(String)ObjItem.getAttribute("SGST_TERM").getObj());
                rsDetail.updateString("COMPOSITION_TERM",(String)ObjItem.getAttribute("COMPOSITION_TERM").getObj());
                rsDetail.updateString("RCM_TERM",(String)ObjItem.getAttribute("RCM_TERM").getObj());
                rsDetail.updateString("GST_COMPENSATION_CESS_TERM",(String)ObjItem.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
                
                rsDetail.updateString("DISCOUNT_TERM",(String)ObjItem.getAttribute("DISCOUNT_TERM").getObj());
                rsDetail.updateString("EXCISE_TERM",(String)ObjItem.getAttribute("EXCISE_TERM").getObj());
                rsDetail.updateString("ST_TERM",(String)ObjItem.getAttribute("ST_TERM").getObj());
                rsDetail.updateString("PF_TERM",(String)ObjItem.getAttribute("PF_TERM").getObj());
                rsDetail.updateString("FREIGHT_TERM",(String)ObjItem.getAttribute("FREIGHT_TERM").getObj());
                rsDetail.updateString("OCTROI_TERM",(String)ObjItem.getAttribute("OCTROI_TERM").getObj());
                rsDetail.updateString("FOB_TERM",(String)ObjItem.getAttribute("FOB_TERM").getObj());
                rsDetail.updateString("CIE_TERM",(String)ObjItem.getAttribute("CIE_TERM").getObj());
                rsDetail.updateString("INSURANCE_TERM",(String)ObjItem.getAttribute("INSURANCE_TERM").getObj());
                rsDetail.updateString("TCC_TERM",(String)ObjItem.getAttribute("TCC_TERM").getObj());
                rsDetail.updateString("CENVAT_TERM",(String)ObjItem.getAttribute("CENVAT_TERM").getObj());
                rsDetail.updateString("DESPATCH_TERM",(String)ObjItem.getAttribute("DESPATCH_TERM").getObj());
                rsDetail.updateString("SERVICE_TAX_TERM",(String)ObjItem.getAttribute("SERVICE_TAX_TERM").getObj());
                rsDetail.updateInt("UNIT_ID",(int)ObjItem.getAttribute("UNIT_ID").getVal());
                rsDetail.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsHDetail.updateInt("SR_NO",cnt);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateString("OTHERS_TERM",(String)ObjItem.getAttribute("OTHERS_TERM").getObj());
                rsHDetail.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsHDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsHDetail.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsHDetail.updateDouble("LAST_PO_QTY",ObjItem.getAttribute("LAST_PO_QTY").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE",ObjItem.getAttribute("RATE_DIFFERENCE").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE_PER",ObjItem.getAttribute("RATE_DIFFERENCE_PER").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_RATE").getVal());
                rsHDetail.updateDouble("RATE_DIFFERENCE_PER_RATE",ObjItem.getAttribute("RATE_DIFFERENCE_PER_RATE").getVal());
                rsHDetail.updateDouble("CURRENT_RATE",ObjItem.getAttribute("CURRENT_RATE").getVal());
                rsHDetail.updateDouble("CURRENT_LAND_RATE",ObjItem.getAttribute("CURRENT_LAND_RATE").getVal());
                rsHDetail.updateDouble("CURRENT_QTY",ObjItem.getAttribute("CURRENT_QTY").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("PAYMENT_TERM",(String)ObjItem.getAttribute("PAYMENT_TERM").getObj());
                rsHDetail.updateString("PRICE_BASIS_TERM",(String)ObjItem.getAttribute("PRICE_BASIS_TERM").getObj());
                
                rsHDetail.updateString("IGST_TERM",(String)ObjItem.getAttribute("IGST_TERM").getObj());
                rsHDetail.updateString("CGST_TERM",(String)ObjItem.getAttribute("CGST_TERM").getObj());
                rsHDetail.updateString("SGST_TERM",(String)ObjItem.getAttribute("SGST_TERM").getObj());
                rsHDetail.updateString("COMPOSITION_TERM",(String)ObjItem.getAttribute("COMPOSITION_TERM").getObj());
                rsHDetail.updateString("RCM_TERM",(String)ObjItem.getAttribute("RCM_TERM").getObj());
                rsHDetail.updateString("GST_COMPENSATION_CESS_TERM",(String)ObjItem.getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
                
                rsHDetail.updateString("DISCOUNT_TERM",(String)ObjItem.getAttribute("DISCOUNT_TERM").getObj());
                rsHDetail.updateString("EXCISE_TERM",(String)ObjItem.getAttribute("EXCISE_TERM").getObj());
                rsHDetail.updateString("ST_TERM",(String)ObjItem.getAttribute("ST_TERM").getObj());
                rsHDetail.updateString("PF_TERM",(String)ObjItem.getAttribute("PF_TERM").getObj());
                rsHDetail.updateString("FREIGHT_TERM",(String)ObjItem.getAttribute("FREIGHT_TERM").getObj());
                rsHDetail.updateString("OCTROI_TERM",(String)ObjItem.getAttribute("OCTROI_TERM").getObj());
                rsHDetail.updateString("FOB_TERM",(String)ObjItem.getAttribute("FOB_TERM").getObj());
                rsHDetail.updateString("CIE_TERM",(String)ObjItem.getAttribute("CIE_TERM").getObj());
                rsHDetail.updateString("INSURANCE_TERM",(String)ObjItem.getAttribute("INSURANCE_TERM").getObj());
                rsHDetail.updateString("TCC_TERM",(String)ObjItem.getAttribute("TCC_TERM").getObj());
                rsHDetail.updateString("CENVAT_TERM",(String)ObjItem.getAttribute("CENVAT_TERM").getObj());
                rsHDetail.updateString("DESPATCH_TERM",(String)ObjItem.getAttribute("DESPATCH_TERM").getObj());
                rsHDetail.updateString("SERVICE_TAX_TERM",(String)ObjItem.getAttribute("SERVICE_TAX_TERM").getObj());
                rsHDetail.updateInt("UNIT_ID",(int)ObjItem.getAttribute("UNIT_ID").getVal());
                rsHDetail.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=38; //Rate Approval
            ObjFlow.DocNo=(String)getAttribute("APPROVAL_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_RATE_APPROVAL_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="APPROVAL_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_RATE_APPROVAL_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=38 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            //==== Handling Rejected Documents ==========//
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=false;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Do nothing
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
            //--------- Approval Flow Update complete -----------
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND APPROVAL_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM D_PUR_RATE_APPROVAL_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND APPROVAL_NO='"+lDocNo+"'";
                data.Execute(strQry);
                
                LoadData(lCompanyID);
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
    
    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND APPROVAL_NO='"+pDocNo+"'";
        clsRateApproval ObjApproval = new clsRateApproval();
        ObjApproval.Filter(strCondition,pCompanyID);
        return ObjApproval;
    }
    
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_RATE_APPROVAL_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND APPROVAL_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND APPROVAL_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY APPROVAL_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
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
        ResultSet rsTmp,rsLot;
        Statement tmpStmt,stLot;
        int Counter=0,Counter2=0;
        int RevNo=0;
        
        try {
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("APPROVAL_NO",rsResultSet.getString("APPROVAL_NO"));
            setAttribute("APPROVAL_DATE",rsResultSet.getString("APPROVAL_DATE"));
            setAttribute("INQUIRY_NO",rsResultSet.getString("INQUIRY_NO"));
            setAttribute("INQUIRY_DATE",rsResultSet.getString("INQUIRY_DATE"));
            setAttribute("QUOT_APPROVAL_NO",rsResultSet.getString("QUOT_APPROVAL_NO"));
            setAttribute("QUOT_APPROVAL_DATE",rsResultSet.getString("QUOT_APPROVAL_DATE"));
            setAttribute("INDENT_NO",rsResultSet.getString("INDENT_NO"));
            setAttribute("INDENT_DATE",rsResultSet.getString("INDENT_DATE"));
            setAttribute("REQ_NO",rsResultSet.getString("REQ_NO"));
            setAttribute("REQ_DATE",rsResultSet.getString("REQ_DATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("DEPT_ID",rsResultSet.getInt("DEPT_ID"));
            setAttribute("USER_ID",rsResultSet.getInt("USER_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            
            //Now turn of detail records
            colItems.clear();
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL_H WHERE COMPANY_ID="+lCompanyID+" AND APPROVAL_NO='"+lDocNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND APPROVAL_NO='"+lDocNo+"' ORDER BY SR_NO");
            }
            rsTmp.first();
            
            Counter=0;
            
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                
                clsRateApprovalItem ObjItem=new clsRateApprovalItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("APPROVAL_NO",rsTmp.getString("APPROVAL_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("HSN_SAC_CODE",rsTmp.getString("HSN_SAC_CODE"));
                ObjItem.setAttribute("MAKE",rsTmp.getString("MAKE"));
                ObjItem.setAttribute("PRICE_LIST_NO",rsTmp.getString("PRICE_LIST_NO"));
                ObjItem.setAttribute("OTHERS_TERM",rsTmp.getString("OTHERS_TERM"));
                ObjItem.setAttribute("SUPP_ID",rsTmp.getString("SUPP_ID"));
                ObjItem.setAttribute("QUOT_ID",rsTmp.getString("QUOT_ID"));
                ObjItem.setAttribute("QUOT_SR_NO",rsTmp.getInt("QUOT_SR_NO"));
                ObjItem.setAttribute("LAST_PO_NO",rsTmp.getString("LAST_PO_NO"));
                ObjItem.setAttribute("LAST_PO_DATE",rsTmp.getString("LAST_PO_DATE"));
                ObjItem.setAttribute("LAST_PO_RATE",rsTmp.getDouble("LAST_PO_RATE"));
                ObjItem.setAttribute("LAST_LANDED_RATE",rsTmp.getDouble("LAST_LANDED_RATE"));
                ObjItem.setAttribute("LAST_PO_QTY",rsTmp.getDouble("LAST_PO_QTY"));
                ObjItem.setAttribute("RATE_DIFFERENCE",rsTmp.getDouble("RATE_DIFFERENCE"));
                ObjItem.setAttribute("RATE_DIFFERENCE_PER",rsTmp.getDouble("RATE_DIFFERENCE_PER"));
                ObjItem.setAttribute("RATE_DIFFERENCE_RATE",rsTmp.getDouble("RATE_DIFFERENCE_RATE"));
                ObjItem.setAttribute("RATE_DIFFERENCE_PER_RATE",rsTmp.getDouble("RATE_DIFFERENCE_PER_RATE"));
                ObjItem.setAttribute("CURRENT_RATE",rsTmp.getDouble("CURRENT_RATE"));
                ObjItem.setAttribute("CURRENT_LAND_RATE",rsTmp.getDouble("CURRENT_LAND_RATE"));
                ObjItem.setAttribute("CURRENT_QTY",rsTmp.getDouble("CURRENT_QTY"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("PAYMENT_TERM",rsTmp.getString("PAYMENT_TERM"));
                ObjItem.setAttribute("PRICE_BASIS_TERM",rsTmp.getString("PRICE_BASIS_TERM"));
                
                ObjItem.setAttribute("IGST_TERM",rsTmp.getString("IGST_TERM"));
                ObjItem.setAttribute("CGST_TERM",rsTmp.getString("CGST_TERM"));
                ObjItem.setAttribute("SGST_TERM",rsTmp.getString("SGST_TERM"));
                ObjItem.setAttribute("COMPOSITION_TERM",rsTmp.getString("COMPOSITION_TERM"));
                ObjItem.setAttribute("RCM_TERM",rsTmp.getString("RCM_TERM"));
                ObjItem.setAttribute("GST_COMPENSATION_CESS_TERM",rsTmp.getString("GST_COMPENSATION_CESS_TERM"));
                
                ObjItem.setAttribute("DISCOUNT_TERM",rsTmp.getString("DISCOUNT_TERM"));
                ObjItem.setAttribute("EXCISE_TERM",rsTmp.getString("EXCISE_TERM"));
                ObjItem.setAttribute("ST_TERM",rsTmp.getString("ST_TERM"));
                ObjItem.setAttribute("PF_TERM",rsTmp.getString("PF_TERM"));
                ObjItem.setAttribute("FREIGHT_TERM",rsTmp.getString("FREIGHT_TERM"));
                ObjItem.setAttribute("OCTROI_TERM",rsTmp.getString("OCTROI_TERM"));
                ObjItem.setAttribute("FOB_TERM",rsTmp.getString("FOB_TERM"));
                ObjItem.setAttribute("CIE_TERM",rsTmp.getString("CIE_TERM"));
                ObjItem.setAttribute("INSURANCE_TERM",rsTmp.getString("INSURANCE_TERM"));
                ObjItem.setAttribute("TCC_TERM",rsTmp.getString("TCC_TERM"));
                ObjItem.setAttribute("CENVAT_TERM",rsTmp.getString("CENVAT_TERM"));
                ObjItem.setAttribute("DESPATCH_TERM",rsTmp.getString("DESPATCH_TERM"));
                ObjItem.setAttribute("SERVICE_TAX_TERM",rsTmp.getString("SERVICE_TAX_TERM"));
                ObjItem.setAttribute("UNIT_ID",rsTmp.getInt("UNIT_ID"));
                ObjItem.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                
                colItems.put(Long.toString(Counter),ObjItem);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND APPROVAL_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=38 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
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
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND APPROVAL_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=38 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
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
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp=null;
        Statement tmpStmt=null;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO,D_PUR_RATE_APPROVAL_HEADER.APPROVAL_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_RATE_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=38 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO,D_PUR_RATE_APPROVAL_HEADER.APPROVAL_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_RATE_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=38 ORDER BY D_PUR_RATE_APPROVAL_HEADER.APPROVAL_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO,D_PUR_RATE_APPROVAL_HEADER.APPROVAL_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_RATE_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=38 ORDER BY D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO";
            }
            
            //strSQL="SELECT D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO,D_PUR_RATE_APPROVAL_HEADER.APPROVAL_DATE FROM D_PUR_RATE_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_RATE_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_RATE_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=38";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("APPROVAL_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsRateApproval ObjDoc=new clsRateApproval();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("APPROVAL_NO",rsTmp.getString("APPROVAL_NO"));
                ObjDoc.setAttribute("APPROVAL_DATE",rsTmp.getString("APPROVAL_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjDoc);
                }
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pDocNo+"'");
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
    
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsRateApproval ObjRateApproval=new clsRateApproval();
                
                ObjRateApproval.setAttribute("APPROVAL_NO",rsTmp.getString("APPROVAL_NO"));
                ObjRateApproval.setAttribute("APPROVAL_DATE",rsTmp.getString("APPROVAL_DATE"));
                ObjRateApproval.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjRateApproval.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjRateApproval.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjRateApproval.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjRateApproval.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjRateApproval);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    public static String getSummaryNo(long pCompanyID,String QuotID) {
        Connection tmpConn;
        
        ResultSet rsTmp3;
        Statement tmpStmt3;
        
        String ApprovalNo="";
        
        try {
            tmpConn=data.getConn();
            
            tmpStmt3=tmpConn.createStatement();
            rsTmp3=tmpStmt3.executeQuery("SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_DETAIL WHERE COMPANY_ID="+ Long.toString(pCompanyID) + " AND QUOT_ID='"+QuotID+"' AND APPROVAL_NO NOT IN (SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER WHERE CANCELLED=1)");
            rsTmp3.first();
            
            if(rsTmp3.getRow()>0) {
                ApprovalNo=rsTmp3.getString("APPROVAL_NO");
            }
            
            rsTmp3.close();
            tmpStmt3.close();
            //tmpConn.close();
        }
        catch(Exception e) {
        }
        
        return ApprovalNo;
    }
    
    
    public static String getSummaryDate(long pCompanyID,String QuotID) {
        Connection tmpConn;
        
        ResultSet rsTmp3;
        Statement tmpStmt3;
        
        String ApprovalDate="";
        
        try {
            tmpConn=data.getConn();
            
            tmpStmt3=tmpConn.createStatement();
            rsTmp3=tmpStmt3.executeQuery("SELECT A.APPROVAL_DATE FROM D_PUR_RATE_APPROVAL_HEADER A,D_PUR_RATE_APPROVAL_DETAIL B WHERE A.COMPANY_ID="+ Long.toString(pCompanyID) + " AND B.QUOT_ID='"+QuotID+"' AND A.APPROVAL_NO NOT IN (SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER WHERE CANCELLED=1) AND A.APPROVAL_NO=B.APPROVAL_NO");
            rsTmp3.first();
            
            if(rsTmp3.getRow()>0) {
                ApprovalDate=rsTmp3.getString("APPROVAL_DATE");
            }
            
            rsTmp3.close();
            tmpStmt3.close();
            //tmpConn.close();
        }
        catch(Exception e) {
        }
        
        return ApprovalDate;
    }
    
    
    public static String getRIANo(long pCompanyID,String pIndentNo,int pIndentSrNo) {
        Connection tmpConn;
        
        ResultSet rsInquiry=null,rsRIA=null;
        Statement stInquiry=null,stRIA=null;
        
        String RIANo="";
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            
            rsInquiry=stInquiry.executeQuery("SELECT A.APPROVAL_NO FROM D_PUR_RATE_APPROVAL_DETAIL A,D_PUR_RATE_APPROVAL_HEADER H,D_PUR_QUOT_DETAIL B,D_PUR_INQUIRY_DETAIL C,D_INV_INDENT_DETAIL D WHERE H.APPROVAL_NO=A.APPROVAL_NO AND H.CANCELLED=0 AND A.QUOT_ID=B.QUOT_ID AND A.QUOT_SR_NO=B.SR_NO AND B.INQUIRY_NO=C.INQUIRY_NO AND B.INQUIRY_SRNO=C.SR_NO AND C.INDENT_NO=D.INDENT_NO AND C.INDENT_SRNO=D.SR_NO  AND D.INDENT_NO='"+pIndentNo+"' AND D.SR_NO="+pIndentSrNo);
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                RIANo=rsInquiry.getString("APPROVAL_NO");
            }
        }
        catch(Exception e) {
        }
        
        return RIANo;
    }
    
    public static String getRIANo(long pCompanyID,String pIndentNo,int pIndentSrNo, String pQuotNo) {
        Connection tmpConn;
        
        ResultSet rsInquiry=null,rsRIA=null;
        Statement stInquiry=null,stRIA=null;
        
        String RIANo="";
        
        try {
            tmpConn=data.getConn();
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT A.APPROVAL_NO FROM D_PUR_RATE_APPROVAL_DETAIL A,D_PUR_RATE_APPROVAL_HEADER H,D_PUR_QUOT_DETAIL B,D_PUR_INQUIRY_DETAIL C,D_INV_INDENT_DETAIL D WHERE H.APPROVAL_NO=A.APPROVAL_NO AND H.CANCELLED=0 AND A.QUOT_ID=B.QUOT_ID AND A.QUOT_SR_NO=B.SR_NO AND B.INQUIRY_NO=C.INQUIRY_NO AND B.INQUIRY_SRNO=C.SR_NO AND C.INDENT_NO=D.INDENT_NO AND C.INDENT_SRNO=D.SR_NO  AND D.INDENT_NO='"+pIndentNo+"' AND D.SR_NO="+pIndentSrNo + " AND B.QUOT_ID="+pQuotNo);
            rsInquiry.first();
            if(rsInquiry.getRow()>0) {
                RIANo=rsInquiry.getString("APPROVAL_NO");
            }
        } catch(Exception e) {
        }
        return RIANo;
    }

    
    
    public static int getRIAStatus(long pCompanyID,String pRIANo) {
        Connection tmpConn;
        
        ResultSet rsRIA=null;
        Statement stRIA=null;
        
        int RIAStatus=0; //Not Created by default
        
        try {
            tmpConn=data.getConn();
            
            stRIA=tmpConn.createStatement();
            rsRIA=stRIA.executeQuery("SELECT APPROVED FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pRIANo+"'");
            rsRIA.first();
            
            if(rsRIA.getRow()>0) {
                if(rsRIA.getBoolean("APPROVED")) {
                    RIAStatus=1;
                }
                else {
                    RIAStatus=2;
                }
                
            }
        }
        catch(Exception e) {
        }
        
        return RIAStatus;
    }
    
    
    
    public static String getRIANo(long pCompanyID,String pItemID) {
        Connection tmpConn;
        
        ResultSet rsInquiry=null,rsRIA=null;
        Statement stInquiry=null,stRIA=null;
        
        String RIANo="";
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER  WHERE CANCELLED=0 AND COMPANY_ID="+pCompanyID+" AND APPROVAL_NO IN (SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_DETAIL WHERE ITEM_ID='"+pItemID+"')  ORDER BY APPROVAL_DATE DESC ");
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                RIANo=rsInquiry.getString("APPROVAL_NO");
            }
        }
        catch(Exception e) {
        }
        
        return RIANo;
    }
    
    
    public static String getRIANo(long pCompanyID,String pItemID,String pBeforeDate) {
        Connection tmpConn;
        
        ResultSet rsInquiry=null,rsRIA=null;
        Statement stInquiry=null,stRIA=null;
        
        String RIANo="";
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER  WHERE CANCELLED=0 AND COMPANY_ID="+pCompanyID+" AND APPROVAL_DATE<='"+pBeforeDate+"' AND APPROVAL_NO IN (SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_DETAIL WHERE ITEM_ID='"+pItemID+"')  ORDER BY APPROVAL_DATE DESC ");
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                RIANo=rsInquiry.getString("APPROVAL_NO");
            }
        }
        catch(Exception e) {
        }
        
        return RIANo;
    }
    
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT APPROVAL_NO,APPROVED FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    strMessage="";
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
    
    
    
    public static boolean CanCancel(int pCompanyID,String pRIANo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pRIANo+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
            
        }
        
        return canCancel;
        
    }
    
    
    public static boolean IsDocExist(int pCompanyID,String pRIANo) {
        ResultSet rsTmp=null;
        boolean IsExist=false;
        
        try {
            rsTmp=data.getResult("SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pRIANo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IsExist=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
            
        }
        
        return IsExist;
        
    }
    
    
    public static boolean CancelDoc(int pCompanyID,String pRIANo) {
        
        ResultSet rsTmp=null,rsRIA=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pRIANo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pRIANo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=38;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pRIANo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_RATE_APPROVAL_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pRIANo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsRIA.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    // This routine returns approved ria rates.
    public static double getRIAApprovedQty(int pCompanyID,String pItemID) {
        Connection tmpConn=null;
        Statement stRIA=null,stTmp=null;
        ResultSet rsRIA=null,rsTmp=null;
        String strSQL="";
        double RIAQty=0; //By default not created
        
        try {
            tmpConn=data.getConn();
            
            stRIA=tmpConn.createStatement();
            rsRIA=stRIA.executeQuery("SELECT A.APPROVAL_NO,A.APPROVAL_DATE,CURRENT_QTY,ITEM_ID FROM D_PUR_RATE_APPROVAL_HEADER  A,D_PUR_RATE_APPROVAL_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.APPROVAL_NO=B.APPROVAL_NO AND B.ITEM_ID='"+pItemID+"' AND B.COMPANY_ID="+pCompanyID+" ORDER BY APPROVAL_DATE DESC");
            rsRIA.first();
            
            if(rsRIA.getRow()>0) {
                String RIANo=rsRIA.getString("APPROVAL_NO");
                RIAQty=rsRIA.getDouble("CURRENT_QTY");
            }
            
            return RIAQty;
        }
        catch(Exception e) {
            return RIAQty;
        }
    }
    
    
    public static double getRIAApprovedQty(int pCompanyID,String pRIANo,String pItemID) {
        Connection tmpConn=null;
        Statement stRIA=null,stTmp=null;
        ResultSet rsRIA=null,rsTmp=null;
        String strSQL="";
        double RIAQty=0; //By default not created
        
        try {
            tmpConn=data.getConn();
            
            stRIA=tmpConn.createStatement();
            rsRIA=stRIA.executeQuery("SELECT A.APPROVAL_NO,A.APPROVAL_DATE,CURRENT_QTY,ITEM_ID FROM D_PUR_RATE_APPROVAL_HEADER  A,D_PUR_RATE_APPROVAL_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.APPROVAL_NO=B.APPROVAL_NO AND B.ITEM_ID='"+pItemID+"' AND B.COMPANY_ID="+pCompanyID+" AND A.APPROVAL_NO='"+pRIANo+"' ORDER BY APPROVAL_DATE DESC");
            rsRIA.first();
            
            if(rsRIA.getRow()>0) {
                String RIANo=rsRIA.getString("APPROVAL_NO");
                RIAQty=rsRIA.getDouble("CURRENT_QTY");
            }
            
            return RIAQty;
        }
        catch(Exception e) {
            return RIAQty;
        }
    }
    
    
    
    
    
}
