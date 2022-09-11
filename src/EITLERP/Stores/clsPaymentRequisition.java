/*
 * clsPaymentRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Stores;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsVoucher;
import EITLERP.Finance.clsVoucherItem;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *
 * @author  komal
 * @version
 */

public class clsPaymentRequisition {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int nModuleId = 204;
    
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
    public clsPaymentRequisition() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("PAY_REQ_NO", new Variant(""));
        props.put("PAY_REQ_DATE",new Variant(""));
        props.put("PAY_REQ_PARTY_CODE", new Variant(""));
        props.put("PAY_REQ_AMOUNT",new Variant(0));
        props.put("PAY_REQ_ITEM",new Variant(""));
        props.put("PAY_REQ_PO_NO", new Variant(""));                
        props.put("PAY_REQ_PO_DATE", new Variant(""));
        props.put("PAY_REQ_PAYMENT_TERMS",new Variant(""));
        props.put("PAY_REQ_INV_NO", new Variant(""));
        props.put("PAY_REQ_INV_DATE",new Variant(""));
        props.put("PAY_REQ_GRN_NO",new Variant(""));
        props.put("PAY_REQ_GRN_DATE", new Variant(""));        
        props.put("PAY_REQ_DUE_DATE", new Variant(""));        
        props.put("PAY_REQ_PJV_NO",new Variant(""));
        props.put("PAY_REQ_PJV_DATE", new Variant(""));        
        props.put("PAY_REQ_TYPE",new Variant(""));
        props.put("PAY_REQ_REMARKS", new Variant(""));        
        
        props.put("PAY_REQ_CHK_TAXABLE_AMT",new Variant(0));               
        props.put("PAY_REQ_CHK_CGST_AMT",new Variant(0));               
        props.put("PAY_REQ_CHK_SGST_AMT",new Variant(0));               
        props.put("PAY_REQ_CHK_IGST_AMT",new Variant(0));                       
        props.put("PAY_REQ_TAXABLE_AMT",new Variant(0));                       
        props.put("PAY_REQ_CGST_AMT",new Variant(0));                       
        props.put("PAY_REQ_SGST_AMT",new Variant(0));                       
        props.put("PAY_REQ_IGST_AMT",new Variant(0));                       
        props.put("PAY_REQ_PAID_AMT",new Variant(0));                       
        props.put("PAY_REQ_PJV_AMT",new Variant(0));                       

        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));  
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));        
        props.put("CANCELLED", new Variant(false));        
        props.put("STATUS", new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
       
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID="+pCompanyID+" AND PAY_REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PAY_REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PAY_REQ_NO");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
        
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date OutwardDate=java.sql.Date.valueOf((String)getAttribute("PAY_REQ_DATE").getObj());
            
            if((OutwardDate.after(FinFromDate)||OutwardDate.compareTo(FinFromDate)==0)&&(OutwardDate.before(FinToDate)||OutwardDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Payment req date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_PAYMENT_REQ_H WHERE PAY_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Payment requisition no.  ------------
            setAttribute("PAY_REQ_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,nModuleId, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());            
            rsResultSet.updateString("PAY_REQ_NO",(String)getAttribute("PAY_REQ_NO").getObj());
            rsResultSet.updateString("PAY_REQ_DATE",(String)getAttribute("PAY_REQ_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_PARTY_CODE",(String)getAttribute("PAY_REQ_PARTY_CODE").getObj());
            rsResultSet.updateString("PAY_REQ_AMOUNT",(String)getAttribute("PAY_REQ_AMOUNT").getObj());
            rsResultSet.updateString("PAY_REQ_ITEM",(String)getAttribute("PAY_REQ_ITEM").getObj());
            rsResultSet.updateString("PAY_REQ_PO_NO",(String)getAttribute("PAY_REQ_PO_NO").getObj());
            rsResultSet.updateString("PAY_REQ_PO_DATE",(String)getAttribute("PAY_REQ_PO_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_PAYMENT_TERMS",(String)getAttribute("PAY_REQ_PAYMENT_TERMS").getObj());
            rsResultSet.updateString("PAY_REQ_INV_NO",(String)getAttribute("PAY_REQ_INV_NO").getObj());
            rsResultSet.updateString("PAY_REQ_INV_DATE",(String)getAttribute("PAY_REQ_INV_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_GRN_NO",(String)getAttribute("PAY_REQ_GRN_NO").getObj());
            rsResultSet.updateString("PAY_REQ_GRN_DATE",(String)getAttribute("PAY_REQ_GRN_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_TYPE",(String)getAttribute("PAY_REQ_TYPE").getObj());
            rsResultSet.updateString("PAY_REQ_REMARKS",(String)getAttribute("PAY_REQ_REMARKS").getObj());
            rsResultSet.updateString("PAY_REQ_DUE_DATE",(String)getAttribute("PAY_REQ_DUE_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_PJV_NO",(String)getAttribute("PAY_REQ_PJV_NO").getObj());
            rsResultSet.updateString("PAY_REQ_PJV_DATE",(String)getAttribute("PAY_REQ_PJV_DATE").getObj());
            
            rsResultSet.updateBoolean("PAY_REQ_CHK_TAXABLE_AMT",(boolean)getAttribute("PAY_REQ_CHK_TAXABLE_AMT").getBool());
            rsResultSet.updateBoolean("PAY_REQ_CHK_CGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_CGST_AMT").getBool());
            rsResultSet.updateBoolean("PAY_REQ_CHK_SGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_SGST_AMT").getBool());
            rsResultSet.updateBoolean("PAY_REQ_CHK_IGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_IGST_AMT").getBool());
            rsResultSet.updateString("PAY_REQ_TAXABLE_AMT",(String)getAttribute("PAY_REQ_TAXABLE_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_CGST_AMT",(String)getAttribute("PAY_REQ_CGST_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_SGST_AMT",(String)getAttribute("PAY_REQ_SGST_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_IGST_AMT",(String)getAttribute("PAY_REQ_IGST_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_PJV_AMT",(String)getAttribute("PAY_REQ_PJV_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_PAID_AMT",(String)getAttribute("PAY_REQ_PAID_AMT").getObj());
            
            rsResultSet.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());            
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
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
            
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("PAY_REQ_NO",(String)getAttribute("PAY_REQ_NO").getObj());
            rsHistory.updateString("PAY_REQ_DATE",(String)getAttribute("PAY_REQ_DATE").getObj());
            rsHistory.updateString("PAY_REQ_PARTY_CODE",(String)getAttribute("PAY_REQ_PARTY_CODE").getObj());
            rsHistory.updateString("PAY_REQ_AMOUNT",(String)getAttribute("PAY_REQ_AMOUNT").getObj());
            rsHistory.updateString("PAY_REQ_ITEM",(String)getAttribute("PAY_REQ_ITEM").getObj());
            rsHistory.updateString("PAY_REQ_PO_NO",(String)getAttribute("PAY_REQ_PO_NO").getObj());
            rsHistory.updateString("PAY_REQ_PO_DATE",(String)getAttribute("PAY_REQ_PO_DATE").getObj());
            rsHistory.updateString("PAY_REQ_PAYMENT_TERMS",(String)getAttribute("PAY_REQ_PAYMENT_TERMS").getObj());
            rsHistory.updateString("PAY_REQ_INV_NO",(String)getAttribute("PAY_REQ_INV_NO").getObj());
            rsHistory.updateString("PAY_REQ_INV_DATE",(String)getAttribute("PAY_REQ_INV_DATE").getObj());
            rsHistory.updateString("PAY_REQ_GRN_NO",(String)getAttribute("PAY_REQ_GRN_NO").getObj());
            rsHistory.updateString("PAY_REQ_GRN_DATE",(String)getAttribute("PAY_REQ_GRN_DATE").getObj());
            rsHistory.updateString("PAY_REQ_TYPE",(String)getAttribute("PAY_REQ_TYPE").getObj());
            rsHistory.updateString("PAY_REQ_REMARKS",(String)getAttribute("PAY_REQ_REMARKS").getObj());
            rsHistory.updateString("PAY_REQ_DUE_DATE",(String)getAttribute("PAY_REQ_DUE_DATE").getObj());rsHistory.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            rsHistory.updateString("PAY_REQ_PJV_NO",(String)getAttribute("PAY_REQ_PJV_NO").getObj());
            rsHistory.updateString("PAY_REQ_PJV_DATE",(String)getAttribute("PAY_REQ_PJV_DATE").getObj());
            
            rsHistory.updateBoolean("PAY_REQ_CHK_TAXABLE_AMT",(boolean)getAttribute("PAY_REQ_CHK_TAXABLE_AMT").getBool());
            rsHistory.updateBoolean("PAY_REQ_CHK_CGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_CGST_AMT").getBool());
            rsHistory.updateBoolean("PAY_REQ_CHK_SGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_SGST_AMT").getBool());
            rsHistory.updateBoolean("PAY_REQ_CHK_IGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_IGST_AMT").getBool());
            rsHistory.updateString("PAY_REQ_TAXABLE_AMT",(String)getAttribute("PAY_REQ_TAXABLE_AMT").getObj());
            rsHistory.updateString("PAY_REQ_CGST_AMT",(String)getAttribute("PAY_REQ_CGST_AMT").getObj());
            rsHistory.updateString("PAY_REQ_SGST_AMT",(String)getAttribute("PAY_REQ_SGST_AMT").getObj());
            rsHistory.updateString("PAY_REQ_IGST_AMT",(String)getAttribute("PAY_REQ_IGST_AMT").getObj());
            rsHistory.updateString("PAY_REQ_PJV_AMT",(String)getAttribute("PAY_REQ_PJV_AMT").getObj());
            rsHistory.updateString("PAY_REQ_PAID_AMT",(String)getAttribute("PAY_REQ_PAID_AMT").getObj());
            
            
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=nModuleId; //PAYMENT REQ
            ObjFlow.DocNo=(String)getAttribute("PAY_REQ_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_PAYMENT_REQ";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="PAY_REQ_NO";
            ObjFlow.DocDate=(String)getAttribute("PAY_REQ_DATE").getObj();
            
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
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        boolean Validate=true;
        
        
        try {
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;            
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date OutwardDate=java.sql.Date.valueOf((String)getAttribute("PAY_REQ_DATE").getObj());
            
            if((OutwardDate.after(FinFromDate)||OutwardDate.compareTo(FinFromDate)==0)&&(OutwardDate.before(FinToDate)||OutwardDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//
                        
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_PAYMENT_REQ_H WHERE PAY_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("PAY_REQ_NO").getObj();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("PAY_REQ_NO",(String)getAttribute("PAY_REQ_NO").getObj());
            rsResultSet.updateString("PAY_REQ_DATE",(String)getAttribute("PAY_REQ_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_PARTY_CODE",(String)getAttribute("PAY_REQ_PARTY_CODE").getObj());
            rsResultSet.updateString("PAY_REQ_AMOUNT",(String)getAttribute("PAY_REQ_AMOUNT").getObj());
            rsResultSet.updateString("PAY_REQ_ITEM",(String)getAttribute("PAY_REQ_ITEM").getObj());
            rsResultSet.updateString("PAY_REQ_PO_NO",(String)getAttribute("PAY_REQ_PO_NO").getObj());
            rsResultSet.updateString("PAY_REQ_PO_DATE",(String)getAttribute("PAY_REQ_PO_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_PAYMENT_TERMS",(String)getAttribute("PAY_REQ_PAYMENT_TERMS").getObj());
            rsResultSet.updateString("PAY_REQ_INV_NO",(String)getAttribute("PAY_REQ_INV_NO").getObj());
            rsResultSet.updateString("PAY_REQ_INV_DATE",(String)getAttribute("PAY_REQ_INV_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_GRN_NO",(String)getAttribute("PAY_REQ_GRN_NO").getObj());
            rsResultSet.updateString("PAY_REQ_GRN_DATE",(String)getAttribute("PAY_REQ_GRN_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_TYPE",(String)getAttribute("PAY_REQ_TYPE").getObj());
            rsResultSet.updateString("PAY_REQ_REMARKS",(String)getAttribute("PAY_REQ_REMARKS").getObj());
            rsResultSet.updateString("PAY_REQ_DUE_DATE",(String)getAttribute("PAY_REQ_DUE_DATE").getObj());
            rsResultSet.updateString("PAY_REQ_PJV_NO",(String)getAttribute("PAY_REQ_PJV_NO").getObj());
            rsResultSet.updateString("PAY_REQ_PJV_DATE",(String)getAttribute("PAY_REQ_PJV_DATE").getObj());
            
            rsResultSet.updateBoolean("PAY_REQ_CHK_TAXABLE_AMT",(boolean)getAttribute("PAY_REQ_CHK_TAXABLE_AMT").getBool());
            rsResultSet.updateBoolean("PAY_REQ_CHK_CGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_CGST_AMT").getBool());
            rsResultSet.updateBoolean("PAY_REQ_CHK_SGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_SGST_AMT").getBool());
            rsResultSet.updateBoolean("PAY_REQ_CHK_IGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_IGST_AMT").getBool());
            rsResultSet.updateString("PAY_REQ_TAXABLE_AMT",(String)getAttribute("PAY_REQ_TAXABLE_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_CGST_AMT",(String)getAttribute("PAY_REQ_CGST_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_SGST_AMT",(String)getAttribute("PAY_REQ_SGST_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_IGST_AMT",(String)getAttribute("PAY_REQ_IGST_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_PJV_AMT",(String)getAttribute("PAY_REQ_PJV_AMT").getObj());
            rsResultSet.updateString("PAY_REQ_PAID_AMT",(String)getAttribute("PAY_REQ_PAID_AMT").getObj());
                        
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_PAYMENT_REQ_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PAY_REQ_NO='"+(String)getAttribute("PAY_REQ_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("PAY_REQ_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("PAY_REQ_NO",(String)getAttribute("PAY_REQ_NO").getObj());
            rsHistory.updateString("PAY_REQ_DATE",(String)getAttribute("PAY_REQ_DATE").getObj());
            rsHistory.updateString("PAY_REQ_PARTY_CODE",(String)getAttribute("PAY_REQ_PARTY_CODE").getObj());
            rsHistory.updateString("PAY_REQ_AMOUNT",(String)getAttribute("PAY_REQ_AMOUNT").getObj());
            rsHistory.updateString("PAY_REQ_ITEM",(String)getAttribute("PAY_REQ_ITEM").getObj());
            rsHistory.updateString("PAY_REQ_PO_NO",(String)getAttribute("PAY_REQ_PO_NO").getObj());
            rsHistory.updateString("PAY_REQ_PO_DATE",(String)getAttribute("PAY_REQ_PO_DATE").getObj());
            rsHistory.updateString("PAY_REQ_PAYMENT_TERMS",(String)getAttribute("PAY_REQ_PAYMENT_TERMS").getObj());
            rsHistory.updateString("PAY_REQ_INV_NO",(String)getAttribute("PAY_REQ_INV_NO").getObj());
            rsHistory.updateString("PAY_REQ_INV_DATE",(String)getAttribute("PAY_REQ_INV_DATE").getObj());
            rsHistory.updateString("PAY_REQ_GRN_NO",(String)getAttribute("PAY_REQ_GRN_NO").getObj());
            rsHistory.updateString("PAY_REQ_GRN_DATE",(String)getAttribute("PAY_REQ_GRN_DATE").getObj());
            rsHistory.updateString("PAY_REQ_TYPE",(String)getAttribute("PAY_REQ_TYPE").getObj());
            rsHistory.updateString("PAY_REQ_REMARKS",(String)getAttribute("PAY_REQ_REMARKS").getObj());
            rsHistory.updateString("PAY_REQ_DUE_DATE",(String)getAttribute("PAY_REQ_DUE_DATE").getObj());rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateString("PAY_REQ_PJV_NO",(String)getAttribute("PAY_REQ_PJV_NO").getObj());
            rsHistory.updateString("PAY_REQ_PJV_DATE",(String)getAttribute("PAY_REQ_PJV_DATE").getObj());
            
            rsHistory.updateBoolean("PAY_REQ_CHK_TAXABLE_AMT",(boolean)getAttribute("PAY_REQ_CHK_TAXABLE_AMT").getBool());
            rsHistory.updateBoolean("PAY_REQ_CHK_CGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_CGST_AMT").getBool());
            rsHistory.updateBoolean("PAY_REQ_CHK_SGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_SGST_AMT").getBool());
            rsHistory.updateBoolean("PAY_REQ_CHK_IGST_AMT",(boolean)getAttribute("PAY_REQ_CHK_IGST_AMT").getBool());
            rsHistory.updateString("PAY_REQ_TAXABLE_AMT",(String)getAttribute("PAY_REQ_TAXABLE_AMT").getObj());
            rsHistory.updateString("PAY_REQ_CGST_AMT",(String)getAttribute("PAY_REQ_CGST_AMT").getObj());
            rsHistory.updateString("PAY_REQ_SGST_AMT",(String)getAttribute("PAY_REQ_SGST_AMT").getObj());
            rsHistory.updateString("PAY_REQ_IGST_AMT",(String)getAttribute("PAY_REQ_IGST_AMT").getObj());
            rsHistory.updateString("PAY_REQ_PJV_AMT",(String)getAttribute("PAY_REQ_PJV_AMT").getObj());
            rsHistory.updateString("PAY_REQ_PAID_AMT",(String)getAttribute("PAY_REQ_PAID_AMT").getObj());
            
            
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=nModuleId; //PAYMENT REQ
            ObjFlow.DocNo=(String)getAttribute("PAY_REQ_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_PAYMENT_REQ";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="PAY_REQ_NO";
            ObjFlow.DocDate=(String)getAttribute("PAY_REQ_DATE").getObj();
            //==== Handling Rejected Documents ==========//
            
            
            if(AStatus.equals("R")) {
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_PAYMENT_REQ SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PAY_REQ_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+nModuleId+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
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
            if(AStatus.equals("F")) {
                PostVoucher((String)getAttribute("PAY_REQ_NO").getObj(), 0, "");
            }            
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND PAY_REQ_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+nModuleId+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND PAY_REQ_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+nModuleId+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("PAY_REQ_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID=" + lCompanyID +" AND PAY_REQ_NO='"+lDocNo+"'";
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
    
    
    public Object getObject(int pCompanyID, String pOutwardNo) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND PAY_REQ_NO='" + pOutwardNo + "'" ;
        clsPaymentRequisition ObjGateOutward = new clsPaymentRequisition();
        ObjGateOutward.Filter(strCondition,pCompanyID);
        return ObjGateOutward;
    }
    
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_PAYMENT_REQ "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsPaymentRequisition objPaymentReq =new clsPaymentRequisition();
                objPaymentReq.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                objPaymentReq.setAttribute("PAY_REQ_NO",rsTmp.getString("PAY_REQ_NO"));
                objPaymentReq.setAttribute("PAY_REQ_DATE",rsTmp.getString("PAY_REQ_DATE"));
                objPaymentReq.setAttribute("PAY_REQ_PARTY_CODE",rsTmp.getString("PAY_REQ_PARTY_CODE"));
                objPaymentReq.setAttribute("PAY_REQ_AMOUNT",rsTmp.getString("PAY_REQ_AMOUNT"));
                objPaymentReq.setAttribute("PAY_REQ_PO_NO",rsTmp.getString("PAY_REQ_PO_NO"));
                objPaymentReq.setAttribute("PAY_REQ_PO_DATE",rsTmp.getString("PAY_REQ_PO_DATE"));
                objPaymentReq.setAttribute("PAY_REQ_INV_NO",rsTmp.getString("PAY_REQ_INV_NO"));
                objPaymentReq.setAttribute("PAY_REQ_INV_DATE",rsTmp.getString("PAY_REQ_INV_DATE"));
                objPaymentReq.setAttribute("PAY_REQ_GRN_NO",rsTmp.getString("PAY_REQ_GRN_NO"));
                objPaymentReq.setAttribute("PAY_REQ_GRN_DATE",rsTmp.getString("PAY_REQ_GRN_DATE"));
                objPaymentReq.setAttribute("PAY_REQ_TYPE",rsTmp.getString("PAY_REQ_TYPE"));
                objPaymentReq.setAttribute("PAY_REQ_REMARKS",rsTmp.getString("PAY_REQ_REMARKS"));
                objPaymentReq.setAttribute("PAY_REQ_PJV_NO", rsTmp.getString("PAY_REQ_PJV_NO"));
                objPaymentReq.setAttribute("PAY_REQ_PJV_DATE", rsTmp.getString("PAY_REQ_PJV_DATE"));
                
                
                objPaymentReq.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                objPaymentReq.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                objPaymentReq.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                objPaymentReq.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                objPaymentReq.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                objPaymentReq.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                objPaymentReq.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                objPaymentReq.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                objPaymentReq.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                List.put(Long.toString(Counter),objPaymentReq);
            }//Outer while
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            rsTmp2.close();
            tmpStmt2.close();
            
        }
        catch(Exception e) {
            //LastError=e.getMessage();
        }
        
        return List;
    }
    
    public static HashMap getPendingApprovals(int pUserID, int pOrder) {
        String strSQL = "";
        Connection tmpConn;
        tmpConn = data.getConn();

        ResultSet rsTmp;
        Statement tmpStmt;

        HashMap List = new HashMap();
        long Counter = 0;

        try {
            if (pOrder == EITLERPGLOBAL.OnRecivedDate) {
                strSQL = "SELECT DISTINCT D_INV_PAYMENT_REQ.PAY_REQ_NO,D_INV_PAYMENT_REQ.PAY_REQ_DATE,RECEIVED_DATE FROM D_INV_PAYMENT_REQ,D_COM_DOC_DATA WHERE D_COM_DOC_DATA.DOC_NO=D_INV_PAYMENT_REQ.PAY_REQ_NO AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+nModuleId+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocDate) {
                strSQL = "SELECT DISTINCT D_INV_PAYMENT_REQ.PAY_REQ_NO,D_INV_PAYMENT_REQ.PAY_REQ_DATE,RECEIVED_DATE FROM D_INV_PAYMENT_REQ,D_COM_DOC_DATA WHERE D_COM_DOC_DATA.DOC_NO=D_INV_PAYMENT_REQ.PAY_REQ_NO AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+nModuleId+" ORDER BY D_INV_PAYMENT_REQ.PAY_REQ_DATE";
            }

            if (pOrder == EITLERPGLOBAL.OnDocNo) {
                strSQL = "SELECT DISTINCT D_INV_PAYMENT_REQ.PAY_REQ_NO,D_INV_PAYMENT_REQ.PAY_REQ_DATE,RECEIVED_DATE FROM D_INV_PAYMENT_REQ,D_COM_DOC_DATA WHERE D_COM_DOC_DATA.DOC_NO=D_INV_PAYMENT_REQ.PAY_REQ_NO AND D_COM_DOC_DATA.USER_ID=" + Integer.toString(pUserID) + " AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+nModuleId+" ORDER BY D_INV_PAYMENT_REQ.PAY_REQ_NO";
            }

            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter = 0;
            while (!rsTmp.isAfterLast() && rsTmp.getRow() > 0) {

                Counter = Counter + 1;
                clsPaymentRequisition ObjPR = new clsPaymentRequisition();

                //------------- Header Fields --------------------//
                ObjPR.setAttribute("DOC_NO", rsTmp.getString("PAY_REQ_NO"));
                ObjPR.setAttribute("DOC_DATE", rsTmp.getString("PAY_REQ_DATE"));
                ObjPR.setAttribute("RECEIVED_DATE", rsTmp.getString("RECEIVED_DATE"));
                //ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));                
                // ----------------- End of Header Fields ------------------------------------//

                List.put(Long.toString(Counter), ObjPR);

                if (!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }

            rsTmp.close();
            tmpStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return List;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_PAYMENT_REQ " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PAY_REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PAY_REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PAY_REQ_NO";
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
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        //HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("PAY_REQ_NO",rsResultSet.getString("PAY_REQ_NO"));
            setAttribute("PAY_REQ_DATE",rsResultSet.getString("PAY_REQ_DATE"));
            setAttribute("PAY_REQ_PARTY_CODE",rsResultSet.getString("PAY_REQ_PARTY_CODE"));
            setAttribute("PAY_REQ_AMOUNT",rsResultSet.getDouble("PAY_REQ_AMOUNT"));
            setAttribute("PAY_REQ_PO_NO",rsResultSet.getString("PAY_REQ_PO_NO"));
            setAttribute("PAY_REQ_PO_DATE",rsResultSet.getString("PAY_REQ_PO_DATE"));
            setAttribute("PAY_REQ_INV_NO",rsResultSet.getString("PAY_REQ_INV_NO"));
            setAttribute("PAY_REQ_INV_DATE",rsResultSet.getString("PAY_REQ_INV_DATE"));
            setAttribute("PAY_REQ_GRN_NO",rsResultSet.getString("PAY_REQ_GRN_NO"));
            setAttribute("PAY_REQ_GRN_DATE",rsResultSet.getString("PAY_REQ_GRN_DATE"));
            setAttribute("PAY_REQ_TYPE",rsResultSet.getString("PAY_REQ_TYPE"));
            setAttribute("PAY_REQ_REMARKS",rsResultSet.getString("PAY_REQ_REMARKS"));
            setAttribute("PAY_REQ_PJV_NO", rsResultSet.getString("PAY_REQ_PJV_NO"));
            setAttribute("PAY_REQ_PJV_DATE", rsResultSet.getString("PAY_REQ_PJV_DATE"));
            setAttribute("PAY_REQ_DUE_DATE", rsResultSet.getString("PAY_REQ_DUE_DATE"));
            
            setAttribute("PAY_REQ_CHK_TAXABLE_AMT",rsResultSet.getBoolean("PAY_REQ_CHK_TAXABLE_AMT"));
            setAttribute("PAY_REQ_CHK_CGST_AMT",rsResultSet.getBoolean("PAY_REQ_CHK_CGST_AMT"));
            setAttribute("PAY_REQ_CHK_SGST_AMT",rsResultSet.getBoolean("PAY_REQ_CHK_SGST_AMT"));
            setAttribute("PAY_REQ_CHK_IGST_AMT",rsResultSet.getBoolean("PAY_REQ_CHK_IGST_AMT"));
            setAttribute("PAY_REQ_TAXABLE_AMT",rsResultSet.getDouble("PAY_REQ_TAXABLE_AMT"));
            setAttribute("PAY_REQ_CGST_AMT",rsResultSet.getDouble("PAY_REQ_CGST_AMT"));
            setAttribute("PAY_REQ_SGST_AMT",rsResultSet.getDouble("PAY_REQ_SGST_AMT"));
            setAttribute("PAY_REQ_IGST_AMT",rsResultSet.getDouble("PAY_REQ_IGST_AMT"));
            setAttribute("PAY_REQ_PJV_AMT",rsResultSet.getDouble("PAY_REQ_PJV_AMT"));
            setAttribute("PAY_REQ_PAID_AMT",rsResultSet.getDouble("PAY_REQ_PAID_AMT"));
            
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            //setAttribute("STATUS",rsResultSet.getString("STATUS"));
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
    
    /*
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom) {
        Connection tmpConn;
        ResultSet rsTmp3=null;
        Statement tmpStmt3=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt3=tmpConn.createStatement();
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_REQ_HEADER.REQ_NO,D_INV_REQ_HEADER.REQ_DATE,RECEIVED_DATE,D_INV_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=2 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_REQ_HEADER.REQ_NO,D_INV_REQ_HEADER.REQ_DATE,RECEIVED_DATE,D_INV_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=2 ORDER BY D_INV_REQ_HEADER.REQ_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_REQ_HEADER.REQ_NO,D_INV_REQ_HEADER.REQ_DATE,RECEIVED_DATE FROM D_INV_REQ_HEADER,D_COM_DOC_DATA,D_INV_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID WHERE D_INV_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=2 ORDER BY D_INV_REQ_HEADER.REQ_NO";
            }
            
            //strSQL="SELECT D_INV_REQ_HEADER.REQ_NO,D_INV_REQ_HEADER.REQ_DATE FROM D_INV_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=2";
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("REQ_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsMaterialRequisition ObjDoc=new clsMaterialRequisition();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("REQ_NO",rsTmp3.getString("REQ_NO"));
                ObjDoc.setAttribute("REQ_DATE",rsTmp3.getString("REQ_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("DEPT_ID",rsTmp3.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Long.toString(Counter),ObjDoc);
                }
            }//end of while
            
            //tmpConn.close();
            rsTmp3.close();
            tmpStmt3.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
     */
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_PAYMENT_REQ_H WHERE COMPANY_ID="+pCompanyID+" AND PAY_REQ_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_PAYMENT_REQ_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PAY_REQ_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsPaymentRequisition ObjGateOutward=new clsPaymentRequisition();
                
                ObjGateOutward.setAttribute("PAY_REQ_NO",rsTmp.getString("PAY_REQ_NO"));
                ObjGateOutward.setAttribute("PAY_REQ_DATE",rsTmp.getString("PAY_REQ_DATE"));
                ObjGateOutward.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjGateOutward.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjGateOutward.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjGateOutward.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjGateOutward.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjGateOutward);
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
            rsTmp=data.getResult("SELECT PAY_REQ_NO,APPROVED,CANCELED FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID="+pCompanyID+" AND PAY_REQ_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELED")) {
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
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT PAY_REQ_NO FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID="+pCompanyID+" AND PAY_REQ_NO='"+pDocNo+"' AND CANCELED=0");
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
    
    
    public static boolean CancelDoc(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pDocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_PAYMENT_REQ WHERE COMPANY_ID="+pCompanyID+" AND PAY_REQ_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID= nModuleId;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_PAYMENT_REQ SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND PAY_REQ_NO='"+pDocNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public boolean PostVoucher(String DocNo, int DocType, String ExVoucherNo) {
        try {

            boolean VoucherPosted = true;

            clsPaymentRequisition objPR = (clsPaymentRequisition) getObject(EITLERPGLOBAL.gCompanyID, DocNo);
            //*********** Select Voucher Hierarchy AS Default *********//                                    
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, 204, "CHOOSE_HIERARCHY", "DEFAULT", "");

            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }            
            //******************* Hierarchy Selected **********************//
            
            clsVoucher objVoucher;
            clsVoucherItem objVoucherItem;

            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;

            String GRNNo = objPR.getAttribute("PAY_REQ_GRN_NO").getString();
            String GRNDate = objPR.getAttribute("PAY_REQ_GRN_DATE").getString();
            String PONo = objPR.getAttribute("PAY_REQ_PO_NO").getString();
            String PODate = objPR.getAttribute("PAY_REQ_PO_DATE").getString();
            String InvoiceNo = objPR.getAttribute("PAY_REQ_INV_NO").getString();
            String InvoiceDate = objPR.getAttribute("PAY_REQ_INV_DATE").getString();
            String PartyCode = objPR.getAttribute("PAY_REQ_PARTY_CODE").getString();
            double Amount = objPR.getAttribute("PAY_REQ_AMOUNT").getDouble();

            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.PaymentModuleID);
            rsVoucher.first();

            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }

            if (Amount> 0) {
            //=======Normal PJV Entry #1 ==========//            
            double VoucherSrNo = 0;

            objVoucher = new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);

            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
            if (EITLERPGLOBAL.gCompanyID == 2) {
                objVoucher.setAttribute("BOOK_CODE", "58");
            }

            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", PONo);
            objVoucher.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucher.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucher.setAttribute("GRN_NO", GRNNo);
            objVoucher.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucher.setAttribute("MODULE_ID", 8); //48
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("REMARKS", "");

            objVoucher.setAttribute("LEGACY_NO", ""); // ADDEDE ON 19/01/2010 BY MRUGESH
            objVoucher.setAttribute("LEGACY_DATE", "0000-00-00"); // ADDEDE ON 19/01/2010 BY MRUGESH

            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
            objVoucher.colVoucherItems.clear();

            VoucherSrNo++;
            objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "D");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "125019");
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", Amount);
            objVoucherItem.setAttribute("REMARKS", "1");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", Amount);
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", 8); //48
            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);

            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            VoucherSrNo++;
            objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "C");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "214832");
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
            objVoucherItem.setAttribute("AMOUNT", Amount);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", Amount);
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", 8);  //48
            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);

            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);

            objVoucher.DoNotValidateAccounts = true;

            objVoucher.UseSpecificVoucherNo = false;
            objVoucher.SpecificVoucherNo = "";

            if (!ExVoucherNo.equals("")) {
                objVoucher.UseSpecificVoucherNo = true;
                objVoucher.SpecificVoucherNo = ExVoucherNo;
            }
            objVoucher.setAttribute("VOUCHER_TYPE", 2);

            objVoucher.IsInternalPosting = true;
            if (!objVoucher.Insert()) {
                VoucherPosted = false;
            }
        
            

            String VoucherNo = objVoucher.getAttribute("VOUCHER_NO").getString();
            }
            //=====================================//

            

            return VoucherPosted;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    
    
}
