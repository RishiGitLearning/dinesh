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

public class clsQuotApproval {
    
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
    public clsQuotApproval() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("APPROVAL_NO",new Variant(""));
        props.put("APPROVAL_DATE",new Variant(""));
        props.put("INQUIRY_NO",new Variant(""));
        props.put("INQUIRY_DATE",new Variant(""));
        props.put("STATUS",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        props.put("CANCELLED",new Variant(false));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("REMARKS",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND APPROVAL_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY APPROVAL_NO");
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
        Statement stTmp,stItem,stHistory,stHDetail;
        ResultSet rsTmp,rsItem,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL="";
        int CompanyID=0;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("APPROVAL_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER_H WHERE APPROVAL_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL_H WHERE APPROVAL_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE APPROVAL_NO='1'");
            //rsHeader.first();
            
            //=========Check That any Summary is already made for this inquiry =========//
            String InquiryNo=(String)getAttribute("INQUIRY_NO").getObj();
            rsTmp=data.getResult("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE INQUIRY_NO='"+InquiryNo+"' AND CANCELLED=0");
            rsTmp.first();
            if(rsTmp.getRow()>0)
            {
              LastError="One Summary has already prepared for this inquiry. Cannot create another summary against this inquiry no.";
              return false;
            }
            //==========================================================================//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //--------- Generate New Approval No. ------------
            setAttribute("APPROVAL_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,20,(int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
            rsResultSet.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
            rsHistory.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            //====== Now turn of CIF Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVAL_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colItems.size();i++) {
                clsQuotApprovalItem ObjItem=(clsQuotApprovalItem)colItems.get(Integer.toString(i));
                
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateString("INQUIRY_NO",(String)ObjItem.getAttribute("INQUIRY_NO").getObj());
                rsItem.updateInt("INQUIRY_SR_NO",(int)ObjItem.getAttribute("INQUIRY_SR_NO").getVal());
                rsItem.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsItem.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsItem.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsItem.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsItem.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsItem.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LAND_COST",ObjItem.getAttribute("LAND_COST").getVal());
                rsItem.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsItem.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsItem.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsItem.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsItem.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateDouble("TAX_AMOUNT",ObjItem.getAttribute("TAX_AMOUNT").getVal());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("INQUIRY_NO",(String)ObjItem.getAttribute("INQUIRY_NO").getObj());
                rsHDetail.updateInt("INQUIRY_SR_NO",(int)ObjItem.getAttribute("INQUIRY_SR_NO").getVal());
                rsHDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsHDetail.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LAND_COST",ObjItem.getAttribute("LAND_COST").getVal());
                rsHDetail.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsHDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsHDetail.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateDouble("TAX_AMOUNT",ObjItem.getAttribute("TAX_AMOUNT").getVal());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=20; //Quotation Approval
            ObjFlow.DocNo=(String)getAttribute("APPROVAL_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_QUOT_APPROVAL_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="APPROVAL_NO";
            
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
            //--------- Approval Flow Update complete -----------//
            
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
        Statement stTmp,stItem,stHistory,stHDetail;
        ResultSet rsTmp,rsItem,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL="";
        int CompanyID=0;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("APPROVAL_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER_H WHERE APPROVAL_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL_H WHERE APPROVAL_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(APPROVAL_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            rsResultSet.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_QUOT_APPROVAL_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+(String)getAttribute("APPROVAL_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
            rsHistory.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.insertRow();
            
            //=== Delete previous rows ===
            String ApprovalNo=(String)getAttribute("APPROVAL_NO").getObj();
            data.Execute("DELETE FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+CompanyID+" AND APPROVAL_NO='"+ApprovalNo+"'");
            
            //====== Now turn of Quot Approval Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVAL_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colItems.size();i++) {
                clsQuotApprovalItem ObjItem=(clsQuotApprovalItem)colItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateString("INQUIRY_NO",(String)ObjItem.getAttribute("INQUIRY_NO").getObj());
                rsItem.updateInt("INQUIRY_SR_NO",(int)ObjItem.getAttribute("INQUIRY_SR_NO").getVal());
                rsItem.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsItem.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsItem.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsItem.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsItem.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsItem.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LAND_COST",ObjItem.getAttribute("LAND_COST").getVal());
                rsItem.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsItem.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsItem.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsItem.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsItem.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateDouble("TAX_AMOUNT",ObjItem.getAttribute("TAX_AMOUNT").getVal());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("INQUIRY_NO",(String)ObjItem.getAttribute("INQUIRY_NO").getObj());
                rsHDetail.updateInt("INQUIRY_SR_NO",(int)ObjItem.getAttribute("INQUIRY_SR_NO").getVal());
                rsHDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsHDetail.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LAND_COST",ObjItem.getAttribute("LAND_COST").getVal());
                rsHDetail.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsHDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsHDetail.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateDouble("TAX_AMOUNT",ObjItem.getAttribute("TAX_AMOUNT").getVal());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=20; //Quot Approval
            ObjFlow.DocNo=(String)getAttribute("APPROVAL_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_QUOT_APPROVAL_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="APPROVAL_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_QUOT_APPROVAL_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=20 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Nothing to do
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
            //--------- Approval Flow Update complete -----------//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean Amend() {
        Statement stTmp,stItem,stHistory,stHDetail;
        ResultSet rsTmp,rsItem,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL="";
        int CompanyID=0;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("APPROVAL_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            data.Execute("UPDATE D_PUR_QUOT_APPROVAL_HEADER SET APPROVED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE APPROVAL_NO='"+(String)getAttribute("APPROVAL_NO").getObj()+"'");
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER_H WHERE APPROVAL_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL_H WHERE APPROVAL_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(APPROVAL_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            rsResultSet.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_QUOT_APPROVAL_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+(String)getAttribute("APPROVAL_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
            rsHistory.updateString("APPROVAL_DATE",(String)getAttribute("APPROVAL_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.insertRow();
            
            //=== Delete previous rows ===
            String ApprovalNo=(String)getAttribute("APPROVAL_NO").getObj();
            data.Execute("DELETE FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+CompanyID+" AND APPROVAL_NO='"+ApprovalNo+"'");
            
            //====== Now turn of Quot Approval Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVAL_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colItems.size();i++) {
                clsQuotApprovalItem ObjItem=(clsQuotApprovalItem)colItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateString("INQUIRY_NO",(String)ObjItem.getAttribute("INQUIRY_NO").getObj());
                rsItem.updateInt("INQUIRY_SR_NO",(int)ObjItem.getAttribute("INQUIRY_SR_NO").getVal());
                rsItem.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsItem.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsItem.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsItem.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsItem.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsItem.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LAND_COST",ObjItem.getAttribute("LAND_COST").getVal());
                rsItem.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsItem.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsItem.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsItem.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsItem.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateDouble("TAX_AMOUNT",ObjItem.getAttribute("TAX_AMOUNT").getVal());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("APPROVAL_NO",(String)getAttribute("APPROVAL_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("INQUIRY_NO",(String)ObjItem.getAttribute("INQUIRY_NO").getObj());
                rsHDetail.updateInt("INQUIRY_SR_NO",(int)ObjItem.getAttribute("INQUIRY_SR_NO").getVal());
                rsHDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateString("SUPP_ID",(String)ObjItem.getAttribute("SUPP_ID").getObj());
                rsHDetail.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LAND_COST",ObjItem.getAttribute("LAND_COST").getVal());
                rsHDetail.updateBoolean("APPROVED",ObjItem.getAttribute("APPROVED").getBool());
                rsHDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_LANDED_RATE",ObjItem.getAttribute("LAST_LANDED_RATE").getVal());
                rsHDetail.updateDouble("LAST_PO_RATE",ObjItem.getAttribute("LAST_PO_RATE").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateDouble("TAX_AMOUNT",ObjItem.getAttribute("TAX_AMOUNT").getVal());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=20; //Quot Approval
            ObjFlow.DocNo=(String)getAttribute("APPROVAL_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_QUOT_APPROVAL_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="APPROVAL_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_QUOT_APPROVAL_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=20 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Nothing to do
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
            //--------- Approval Flow Update complete -----------//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("APPROVAL_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                data.Execute("DELETE FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+lCompanyID+" AND APPROVAL_NO='"+lDocNo.trim()+"'");
                data.Execute("DELETE FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND APPROVAL_NO='"+lDocNo.trim()+"'");
                
                LoadData(lCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted. Either it is Approved/Rejected. Only creator of the document can delete.";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID,String pApprovalNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND APPROVAL_NO='"+pApprovalNo+"'";
        clsQuotApproval ObjApproval = new clsQuotApproval();
        ObjApproval.Filter(strCondition,pCompanyID);
        return ObjApproval;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND APPROVAL_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND APPROVAL_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY APPROVAL_NO";
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
        Statement stItem,stTmp;
        ResultSet rsItem,rsTmp;
        String ApprovalNo="";
        int CompanyID=0,ItemCounter=0,SrNo=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            setAttribute("APPROVAL_NO",rsResultSet.getString("APPROVAL_NO"));
            setAttribute("APPROVAL_DATE",rsResultSet.getString("APPROVAL_DATE"));
            setAttribute("INQUIRY_NO",rsResultSet.getString("INQUIRY_NO"));
            setAttribute("INQUIRY_DATE",rsResultSet.getString("INQUIRY_DATE"));
            setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            
            colItems.clear();
            
            ApprovalNo=(String)getAttribute("APPROVAL_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            if(HistoryView) {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL_H WHERE COMPANY_ID="+CompanyID+" AND APPROVAL_NO='"+ApprovalNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+CompanyID+" AND APPROVAL_NO='"+ApprovalNo+"' ORDER BY SR_NO");
            }
            
            rsItem.first();
            
            ItemCounter=0;
            
            while((!rsItem.isAfterLast())&&(rsItem.getRow()>0)) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsQuotApprovalItem ObjItem=new clsQuotApprovalItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("APPROVAL_NO",rsItem.getString("APPROVAL_NO"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("MAIN_SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("INQUIRY_NO",rsItem.getString("INQUIRY_NO"));
                ObjItem.setAttribute("INQUIRY_SR_NO",rsItem.getInt("INQUIRY_SR_NO"));
                ObjItem.setAttribute("QUOT_ID",rsItem.getString("QUOT_ID"));
                ObjItem.setAttribute("QUOT_SR_NO",rsItem.getInt("QUOT_SR_NO"));
                ObjItem.setAttribute("SUPP_ID",rsItem.getString("SUPP_ID"));
                ObjItem.setAttribute("ITEM_CODE",rsItem.getString("ITEM_CODE"));
                ObjItem.setAttribute("MAKE",rsItem.getString("MAKE"));
                ObjItem.setAttribute("PRICE_LIST_NO",rsItem.getString("PRICE_LIST_NO"));
                ObjItem.setAttribute("APPROVED",rsItem.getInt("APPROVED"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LAND_COST",rsItem.getDouble("LAND_COST"));
                ObjItem.setAttribute("LAST_PO_NO",rsItem.getString("LAST_PO_NO"));
                ObjItem.setAttribute("LAST_PO_DATE",rsItem.getString("LAST_PO_DATE"));
                ObjItem.setAttribute("LAST_LANDED_RATE",rsItem.getDouble("LAST_LANDED_RATE"));
                ObjItem.setAttribute("LAST_PO_RATE",rsItem.getDouble("LAST_PO_RATE"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY",rsItem.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsItem.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsItem.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsItem.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("TAX_AMOUNT",rsItem.getDouble("TAX_AMOUNT"));
                colItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND APPROVAL_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=20 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND APPROVAL_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=20 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    
    public static HashMap getQuotApprovalItems(int pCompanyID,String pInquiryNo) {
        String strSQL="";
        Connection tmpConn;
        Statement stTmp=null,stPO=null;
        ResultSet rsTmp=null,rsPO=null;
        
        HashMap List=new HashMap();
        int Counter=0;
        String ApprovalNo="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            //Finding Latest Summary in case if more than one summary is created
            strSQL="SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 AND APPROVAL_NO IN (SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE INQUIRY_NO='"+pInquiryNo+"') ORDER BY APPROVAL_DATE DESC";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ApprovalNo=rsTmp.getString("APPROVAL_NO");
            }
            
            //strSQL="SELECT D_PUR_QUOT_APPROVAL_DETAIL.* FROM D_PUR_QUOT_APPROVAL_HEADER,D_PUR_QUOT_APPROVAL_DETAIL WHERE D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_PUR_QUOT_APPROVAL_DETAIL.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_PUR_QUOT_APPROVAL_DETAIL.APPROVAL_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_QUOT_APPROVAL_HEADER.APPROVED=1 AND D_PUR_QUOT_APPROVAL_DETAIL.INQUIRY_NO='"+pInquiryNo+"' AND D_PUR_QUOT_APPROVAL_DETAIL.APPROVED=1 AND D_PUR_QUOT_APPROVAL_DETAIL.LAST_LANDED_RATE<>D_PUR_QUOT_APPROVAL_DETAIL.LAND_COST ORDER BY ITEM_CODE";
            if(!ApprovalNo.trim().equals("")) {
                strSQL="SELECT D_PUR_QUOT_APPROVAL_DETAIL.*,D_PUR_QUOT_APPROVAL_HEADER.* FROM D_PUR_QUOT_APPROVAL_HEADER,D_PUR_QUOT_APPROVAL_DETAIL WHERE D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_PUR_QUOT_APPROVAL_DETAIL.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_PUR_QUOT_APPROVAL_DETAIL.APPROVAL_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_QUOT_APPROVAL_HEADER.APPROVED=1 AND D_PUR_QUOT_APPROVAL_DETAIL.INQUIRY_NO='"+pInquiryNo+"' AND D_PUR_QUOT_APPROVAL_DETAIL.APPROVED=1 AND D_PUR_QUOT_APPROVAL_DETAIL.APPROVAL_NO='"+ApprovalNo+"' ORDER BY ITEM_CODE";
            }
            else {
                strSQL="SELECT D_PUR_QUOT_APPROVAL_DETAIL.*,D_PUR_QUOT_APPROVAL_HEADER.* FROM D_PUR_QUOT_APPROVAL_HEADER,D_PUR_QUOT_APPROVAL_DETAIL WHERE D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_PUR_QUOT_APPROVAL_DETAIL.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_PUR_QUOT_APPROVAL_DETAIL.APPROVAL_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_QUOT_APPROVAL_HEADER.APPROVED=1 AND D_PUR_QUOT_APPROVAL_DETAIL.INQUIRY_NO='"+pInquiryNo+"' AND D_PUR_QUOT_APPROVAL_DETAIL.APPROVED=1 ORDER BY ITEM_CODE";
            }
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Counter=0;
                while(!rsTmp.isAfterLast()) {
                    
                    if(rsTmp.getBoolean("APPROVED")) {
                        Counter++;
                        clsQuotApprovalItem ObjItem=new clsQuotApprovalItem();
                        
                        double RateDiff=0,RateDiffPer=0;
                        String PONo="",ItemID="";
                        
                        ObjItem.setAttribute("SR_NO",Counter);
                        ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_CODE"));
                        ObjItem.setAttribute("HSN_SAC_CODE",clsItem.getHsnSacCode(EITLERPGLOBAL.gCompanyID,rsTmp.getString("ITEM_CODE")));
                        ObjItem.setAttribute("MAKE",rsTmp.getString("MAKE"));
                        ObjItem.setAttribute("PRICE_LIST_NO",rsTmp.getString("PRICE_LIST_NO"));
                        ItemID=rsTmp.getString("ITEM_CODE");
                        ObjItem.setAttribute("SUPP_ID",rsTmp.getString("SUPP_ID"));
                        ObjItem.setAttribute("LAST_PO_NO",rsTmp.getString("LAST_PO_NO"));
                        ObjItem.setAttribute("LAST_PO_DATE",rsTmp.getString("LAST_PO_DATE"));
                        ObjItem.setAttribute("LAST_LANDED_RATE",rsTmp.getDouble("LAST_LANDED_RATE"));
                        ObjItem.setAttribute("LAST_PO_RATE",rsTmp.getDouble("LAST_PO_RATE"));
                        
                        
                        PONo=rsTmp.getString("LAST_PO_NO");
                        ObjItem.setAttribute("LAST_PO_QTY",0);
                        
                        double LastRate=0;
                        if(!PONo.trim().equals("")) {
                            stPO=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                            rsPO=stPO.executeQuery("SELECT QTY,RATE,COLUMN_1_PER  FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+PONo+"' AND ITEM_ID='"+rsTmp.getString("ITEM_CODE")+"'");
                            rsPO.first();
                            if(rsPO.getRow()>0) {
                                ObjItem.setAttribute("LAST_PO_QTY",rsPO.getDouble("QTY"));
                                ObjItem.setAttribute("COLUMN_1_PER",rsPO.getDouble("COLUMN_1_PER"));
                            }
                        }
                        
                        LastRate=rsTmp.getDouble("LAST_LANDED_RATE");
                        
                        ObjItem.setAttribute("CURRENT_QTY",rsTmp.getDouble("QTY"));
                        ObjItem.setAttribute("CURRENT_LAND_RATE",rsTmp.getDouble("LAND_COST"));
                        ObjItem.setAttribute("CURRENT_RATE",rsTmp.getDouble("RATE"));
                        
                        RateDiff=rsTmp.getDouble("LAND_COST")-LastRate;
                        if(LastRate>0) {
                            RateDiffPer=EITLERPGLOBAL.round((RateDiff*100)/LastRate,5);
                        }
                        else {
                            RateDiffPer=0;
                        }
                        
                        ObjItem.setAttribute("RATE_DIFFERENCE",EITLERPGLOBAL.round(RateDiff,5));
                        ObjItem.setAttribute("RATE_DIFFERENCE_PER",EITLERPGLOBAL.round(RateDiffPer,5));
                        
                        RateDiff=rsTmp.getDouble("RATE")-rsTmp.getDouble("LAST_PO_RATE");
                        
                        if(rsTmp.getDouble("LAST_PO_RATE")>0) {
                            RateDiffPer=EITLERPGLOBAL.round((RateDiff*100)/rsTmp.getDouble("LAST_PO_RATE"),5);
                        }
                        else {
                            RateDiffPer=0;
                        }
                        
                        ObjItem.setAttribute("RATE_DIFFERENCE_RATE",EITLERPGLOBAL.round(RateDiff,5));
                        ObjItem.setAttribute("RATE_DIFFERENCE_PER_RATE",EITLERPGLOBAL.round(RateDiffPer,5));
                        ObjItem.setAttribute("QUOT_ID",rsTmp.getString("QUOT_ID"));
                        ObjItem.setAttribute("QUOT_SR_NO",rsTmp.getInt("QUOT_SR_NO"));
                        
                        
                        
                        
                        stPO=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        rsPO=stPO.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+rsTmp.getString("QUOT_ID")+"'");
                        System.out.println("QUERY : SELECT * FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+rsTmp.getString("QUOT_ID")+"'");       
                        rsPO.first();
                        if(rsPO.getRow()>0) {
                            ObjItem.setAttribute("PAYMENT_TERM",rsPO.getString("PAYMENT_TERM"));
                            ObjItem.setAttribute("PRICE_BASIS_TERM",rsPO.getString("PRICE_BASIS_TERM"));
                            ObjItem.setAttribute("DISCOUNT_TERM",rsPO.getString("DISCOUNT_TERM"));
                            
                            if(rsPO.getString("DISCOUNT_TERM").trim().equals("")) {
                                ObjItem.setAttribute("DISCOUNT_TERM",Double.toString(ObjItem.getAttribute("COLUMN_1_PER").getVal())+" %");
                            }
                            
                            
                            //DAXESH
                            ObjItem.setAttribute("CGST_TERM", rsPO.getString("CGST_TERM"));
                            ObjItem.setAttribute("SGST_TERM", rsPO.getString("SGST_TERM"));
                            ObjItem.setAttribute("IGST_TERM", rsPO.getString("IGST_TERM"));
                            ObjItem.setAttribute("COMPOSITION_TERM", rsPO.getString("COMPOSITION_TERM"));
                            ObjItem.setAttribute("RCM_TERM", rsPO.getString("RCM_TERM"));
                            ObjItem.setAttribute("GST_COMPENSATION_CESS_TERM", rsPO.getString("GST_COMPENSATION_CESS_TERM"));
                            //
                            
                            
                            ObjItem.setAttribute("EXCISE_TERM",rsPO.getString("EXCISE_TERM"));
                            ObjItem.setAttribute("ST_TERM",rsPO.getString("ST_TERM"));
                            ObjItem.setAttribute("PF_TERM",rsPO.getString("PF_TERM"));
                            ObjItem.setAttribute("FREIGHT_TERM",rsPO.getString("FREIGHT_TERM"));
                            ObjItem.setAttribute("OCTROI_TERM",rsPO.getString("OCTROI_TERM"));
                            ObjItem.setAttribute("FOB_TERM",rsPO.getString("FOB_TERM"));
                            ObjItem.setAttribute("CIE_TERM",rsPO.getString("CIE_TERM"));
                            ObjItem.setAttribute("INSURANCE_TERM",rsPO.getString("INSURANCE_TERM"));
                            ObjItem.setAttribute("TCC_TERM",rsPO.getString("TCC_TERM"));
                            ObjItem.setAttribute("CENVAT_TERM",rsPO.getString("CENVAT_TERM"));
                            ObjItem.setAttribute("DESPATCH_TERM",rsPO.getString("DESPATCH_TERM"));
                            ObjItem.setAttribute("SERVICE_TAX_TERM",rsPO.getString("SERVICE_TAX_TERM"));
                            ObjItem.setAttribute("OTHERS_TERM",rsPO.getString("OTHERS_TERM"));
                        }
                        
                        ObjItem.setAttribute("ITEM_EXTRA_DESC","");
                        ObjItem.setAttribute("UNIT_ID",0);
                        
                        stPO=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        rsPO=stPO.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+rsTmp.getString("QUOT_ID")+"' AND SR_NO="+rsTmp.getInt("QUOT_SR_NO"));
                        rsPO.first();
                        if(rsPO.getRow()>0) {
                            ObjItem.setAttribute("ITEM_EXTRA_DESC",rsPO.getString("SUPP_ITEM_DESC"));
                            ObjItem.setAttribute("UNIT_ID",rsPO.getInt("UNIT"));
                        }
                        
                        if(rsTmp.getDouble("LAST_PO_RATE")==0) {
                            ObjItem.setAttribute("REMARKS","New Item");
                            ObjItem.setAttribute("RATE_DIFFERENCE",0);
                            ObjItem.setAttribute("RATE_DIFFERENCE_PER",0);
                            ObjItem.setAttribute("RATE_DIFFERENCE_RATE",0);
                            ObjItem.setAttribute("RATE_DIFFERENCE_PER_RATE",0);
                        }
                        else {
                            ObjItem.setAttribute("REMARKS","");
                        }
                        
                        //Now fetching Terms from Quotation (if any)
                        List.put(Integer.toString(Counter),ObjItem);
                    }
                    rsTmp.next();
                }
            }
            //tmpConn.close();
            stTmp.close();
            stPO.close();
            rsTmp.close();
            rsPO.close();
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Error : "+e.getLocalizedMessage());
                    
        }
        
        return List;
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
                strSQL="SELECT D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO,D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_QUOT_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=20 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO,D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_QUOT_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=20 ORDER BY D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO,D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_QUOT_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=20 ORDER BY D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO";
            }
            
            //strSQL="SELECT D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO,D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_DATE FROM D_PUR_QUOT_APPROVAL_HEADER,D_COM_DOC_DATA WHERE D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=20";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("APPROVAL_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsQuotApproval ObjDoc=new clsQuotApproval();
                
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pDocNo+"'");
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
        Connection tmpConn=null;
        Statement stTmp=null;
        ResultSet rsTmp=null;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVAL_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsQuotApproval ObjApproval=new clsQuotApproval();
                
                ObjApproval.setAttribute("APPROVAL_NO",rsTmp.getString("APPROVAL_NO"));
                ObjApproval.setAttribute("APPROVAL_DATE",rsTmp.getString("APPROVAL_DATE"));
                ObjApproval.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjApproval.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjApproval.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjApproval.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjApproval.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjApproval);
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
    
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT APPROVAL_NO,APPROVED FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pDocNo+"'");
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
    
    
    public static boolean IsDocExist(int pCompanyID,String pSummaryNo) {
        ResultSet rsTmp=null;
        boolean IsExist=false;
        
        try {
            rsTmp=data.getResult("SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pSummaryNo+"'");
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
    
    public static String getRIANo(long pCompanyID,String pIndentNo,int pIndentSrNo) {
        Connection tmpConn;
        
        ResultSet rsInquiry=null,rsRIA=null;
        Statement stInquiry=null,stRIA=null;
        
        String RIANo="";
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' AND INDENT_SRNO="+pIndentSrNo+" ORDER BY INQUIRY_NO DESC");
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                while(!rsInquiry.isAfterLast()) {
                    String InquiryNo=rsInquiry.getString("INQUIRY_NO");
                    int InquirySrNo=rsInquiry.getInt("SR_NO");

                    
                    stRIA=tmpConn.createStatement();
                    //rsRIA=stRIA.executeQuery("SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_HEADER WHERE CANCELLED=0 AND APPROVAL_NO IN (SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_DETAIL WHERE INQUIRY_NO='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrNo+"))");
                    rsRIA=stRIA.executeQuery("SELECT DISTINCT(A.APPROVAL_NO) APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_HEADER A,D_PUR_QUOT_APPROVAL_DETAIL B,D_PUR_QUOT_DETAIL C WHERE A.APPROVAL_NO=B.APPROVAL_NO AND A.CANCELLED=0 AND B.QUOT_ID=C.QUOT_ID AND C.INQUIRY_NO="+InquiryNo+" AND INQUIRY_SRNO="+InquirySrNo);
                    rsRIA.first();
                    
                    if(rsRIA.getRow()>0) {
                        RIANo=rsRIA.getString("APPROVAL_NO");
                        return rsRIA.getString("APPROVAL_NO");
                    }
                    
                    
                    rsInquiry.next();
                }
            }
        }
        catch(Exception e) {
            
            
        }
        
        return RIANo;
    }
    
    
    public static String getRIANo(long pCompanyID,String pItemID) {
        Connection tmpConn;
        
        ResultSet rsInquiry=null,rsRIA=null;
        Statement stInquiry=null,stRIA=null;
        
        String RIANo="";
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_HEADER  WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO IN (SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE ITEM_CODE='"+pItemID+"') AND APPROVED=1 ORDER BY APPROVAL_DATE DESC ");
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                RIANo=rsInquiry.getString("APPROVAL_NO");
            }
        }
        catch(Exception e) {
            
        }
        
        return RIANo;
    }
    
    
    
    
    public static int CanAmend(int pCompanyID,String pSummaryNo) {
        
        ResultSet rsTmp=null;
        int canAmend=0;
        
        try {
            
            rsTmp=data.getResult("SELECT QUOT_APPROVAL_NO,APPROVED FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND QUOT_APPROVAL_NO='"+pSummaryNo+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED"))
                {
                  canAmend=2; //Restriction
                }
                else
                {
                  canAmend=1; //Warning   
                }
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return canAmend;
    }
    
    
    
    public static boolean CanCancel(int pCompanyID,String pSummaryNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pSummaryNo+"' AND CANCELLED=0");
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
    
    
    public static boolean CancelSummary(int pCompanyID,String pSummaryNo) {
        
        ResultSet rsTmp=null,rsSummary=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pSummaryNo)) {
                
                boolean ApprovedSummary=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pSummaryNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedSummary=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedSummary) {
                    
                }
                else {
                    int ModuleID=20;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pSummaryNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_QUOT_APPROVAL_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+pSummaryNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsSummary.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    
}
