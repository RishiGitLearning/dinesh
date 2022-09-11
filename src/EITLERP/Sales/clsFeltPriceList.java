/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Sales;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;


/**
 *
 * @author  jadave
 * @version
 */

public class clsFeltPriceList {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colPriceDetails;
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=148;
    
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
    public clsFeltPriceList() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO", new Variant(0));
        props.put("WH_CODE",new Variant(""));
        props.put("QUALITY_ID",new Variant(""));
        props.put("QUALITY_DESC", new Variant(""));
        props.put("SYN_PERC",new Variant(0));
        props.put("CHEM_TRTIN",new Variant(0));
        props.put("PIN_IND",new Variant(0));
        props.put("SPRL_IND",new Variant(0));
        props.put("SUR_CHGIND",new Variant(0));
        props.put("SQM_IND",new Variant(0));
        props.put("FILLER",new Variant(0));
        
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        //props.put("STATUS", new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        //Create a new object for MR Item collection
        colPriceDetails=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        // chk mode is used for new quality or update rate : true for new quality , false for update rate
        //props.put("CHK_MODE",new Variant(false));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID="+pCompanyID+"  ORDER BY DOC_NO");
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
        
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER_H WHERE QUALITY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL_H WHERE QUALITY_ID='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            long srno = data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_SAL_POLICY_FELT_PRICELIST_HEADER","DOC_NO");
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO",srno);
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
            rsResultSet.updateString("WH_CODE",(String)getAttribute("WH_CODE").getObj());
            rsResultSet.updateString("QUALITY_ID",(String)getAttribute("QUALITY_ID").getObj());
            rsResultSet.updateString("QUALITY_DESC",(String)getAttribute("QUALITY_DESC").getObj());
            rsResultSet.updateDouble("SYN_PERC",(double) getAttribute("SYN_PERC").getVal());
            rsResultSet.updateInt("CHEM_TRTIN",Integer.parseInt((String) getAttribute("CHEM_TRTIN").getObj()));
            rsResultSet.updateInt("PIN_IND",Integer.parseInt((String) getAttribute("PIN_IND").getObj()));
            rsResultSet.updateInt("SPRL_IND",Integer.parseInt((String) getAttribute("SPRL_IND").getObj()));
            rsResultSet.updateInt("SUR_CHGIND",Integer.parseInt((String) getAttribute("SUR_CHGIND").getObj()));
            rsResultSet.updateInt("SQM_IND",Integer.parseInt((String) getAttribute("SQM_IND").getObj()));
            rsResultSet.updateInt("FILLER",(int) getAttribute("FILLER").getVal());
            
            //rsResultSet.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            //rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateString("REJECTED_REMARKS",(String)getAttribute("REJECTED_REMARKS").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","");
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
            rsHistory.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
            rsHistory.updateString("WH_CODE",(String)getAttribute("WH_CODE").getObj());
            rsHistory.updateString("QUALITY_ID",(String)getAttribute("QUALITY_ID").getObj());
            rsHistory.updateString("QUALITY_DESC",(String)getAttribute("QUALITY_DESC").getObj());
            rsHistory.updateDouble("SYN_PERC",(double) getAttribute("SYN_PERC").getVal());
            rsHistory.updateInt("CHEM_TRTIN",Integer.parseInt((String) getAttribute("CHEM_TRTIN").getObj()));
            rsHistory.updateInt("PIN_IND",Integer.parseInt((String) getAttribute("PIN_IND").getObj()));
            rsHistory.updateInt("SPRL_IND",Integer.parseInt((String) getAttribute("SPRL_IND").getObj()));
            rsHistory.updateInt("SUR_CHGIND",Integer.parseInt((String) getAttribute("SUR_CHGIND").getObj()));
            rsHistory.updateInt("SQM_IND",Integer.parseInt((String) getAttribute("SQM_IND").getObj()));
            rsHistory.updateInt("FILLER",(int) getAttribute("FILLER").getVal());
            
            //rsHistory.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            //rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateString("REJECTED_REMARKS",(String)getAttribute("REJECTED_REMARKS").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE QUALITY_ID='1'");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colPriceDetails.size();i++) {
                clsFeltPriceListDetail ObjPriceDtl=(clsFeltPriceListDetail) colPriceDetails.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("QUALITY_ID",(String)ObjPriceDtl.getAttribute("QUALITY_ID").getObj());
                //rsTmp.updateString("QUALITY_DESC",(String)ObjPriceDtl.getAttribute("QUALITY_DESC").getObj());
                rsTmp.updateDouble("SQM_RATE",(double)ObjPriceDtl.getAttribute("SQM_RATE").getVal());
                rsTmp.updateString("SQM_RATE_DATE",(String)ObjPriceDtl.getAttribute("SQM_RATE_DATE").getObj());
                rsTmp.updateDouble("WT_RATE",(double)ObjPriceDtl.getAttribute("WT_RATE").getVal());
                rsTmp.updateString("WT_RATE_DATE",(String)ObjPriceDtl.getAttribute("WT_RATE_DATE").getObj());
                
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED", false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("QUALITY_ID",(String)ObjPriceDtl.getAttribute("QUALITY_ID").getObj());
                //rsHDetail.updateString("QUALITY_DESC",(String)ObjPriceDtl.getAttribute("QUALITY_DESC").getObj());
                rsHDetail.updateDouble("SQM_RATE",(double)ObjPriceDtl.getAttribute("SQM_RATE").getVal());
                rsHDetail.updateString("SQM_RATE_DATE",(String)ObjPriceDtl.getAttribute("SQM_RATE_DATE").getObj());
                rsHDetail.updateDouble("WT_RATE",(double)ObjPriceDtl.getAttribute("WT_RATE").getVal());
                rsHDetail.updateString("WT_RATE_DATE",(String)ObjPriceDtl.getAttribute("WT_RATE_DATE").getObj());
                
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED", false);
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFeltPriceList.ModuleID;
            ObjFlow.DocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_POLICY_FELT_PRICELIST_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
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
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER_H WHERE QUALITY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL_H WHERE QUALITY_ID='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("WH_CODE",(String)getAttribute("WH_CODE").getObj());
            rsResultSet.updateString("QUALITY_DESC",(String)getAttribute("QUALITY_DESC").getObj());
            rsResultSet.updateDouble("SYN_PERC",(double) getAttribute("SYN_PERC").getVal());
            rsResultSet.updateInt("CHEM_TRTIN",Integer.parseInt((String) getAttribute("CHEM_TRTIN").getObj()));
            rsResultSet.updateInt("PIN_IND",Integer.parseInt((String) getAttribute("PIN_IND").getObj()));
            rsResultSet.updateInt("SPRL_IND",Integer.parseInt((String) getAttribute("SPRL_IND").getObj()));
            rsResultSet.updateInt("SUR_CHGIND",Integer.parseInt((String) getAttribute("SUR_CHGIND").getObj()));
            rsResultSet.updateInt("SQM_IND",Integer.parseInt((String) getAttribute("SQM_IND").getObj()));
            rsResultSet.updateInt("FILLER",(int) getAttribute("FILLER").getVal());
            //rsResultSet.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            //rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateString("REJECTED_REMARKS",(String)getAttribute("REJECTED_REMARKS").getObj());
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_FELT_PRICELIST_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+(long)getAttribute("DOC_NO").getVal()+" ");
            RevNo++;
            String RevDocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
            rsHistory.updateString("WH_CODE",(String)getAttribute("WH_CODE").getObj());
            rsHistory.updateString("QUALITY_ID",(String)getAttribute("QUALITY_ID").getObj());
            rsHistory.updateString("QUALITY_DESC",(String)getAttribute("QUALITY_DESC").getObj());
            rsHistory.updateDouble("SYN_PERC",(double) getAttribute("SYN_PERC").getVal());
            rsHistory.updateInt("CHEM_TRTIN",Integer.parseInt((String) getAttribute("CHEM_TRTIN").getObj()));
            rsHistory.updateInt("PIN_IND",Integer.parseInt((String) getAttribute("PIN_IND").getObj()));
            rsHistory.updateInt("SPRL_IND",Integer.parseInt((String) getAttribute("SPRL_IND").getObj()));
            rsHistory.updateInt("SUR_CHGIND",Integer.parseInt((String) getAttribute("SUR_CHGIND").getObj()));
            rsHistory.updateInt("SQM_IND",Integer.parseInt((String) getAttribute("SQM_IND").getObj()));
            rsHistory.updateInt("FILLER",(int) getAttribute("FILLER").getVal());
            //rsHistory.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            //rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateString("REJECTED_REMARKS",(String)getAttribute("REJECTED_REMARKS").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            int docno=(int)getAttribute("DOC_NO").getVal();
            
            data.Execute("DELETE FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO="+docno+" ");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE QUALITY_ID='1'");
            
            //Now Insert records into detail table
            for(int i=1;i<=colPriceDetails.size();i++) {
                clsFeltPriceListDetail ObjPriceDtl=(clsFeltPriceListDetail) colPriceDetails.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsTmp.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("QUALITY_ID",(String)ObjPriceDtl.getAttribute("QUALITY_ID").getObj());
                //rsTmp.updateString("QUALITY_DESC",(String)ObjPriceDtl.getAttribute("QUALITY_DESC").getObj());
                rsTmp.updateDouble("SQM_RATE",(double)ObjPriceDtl.getAttribute("SQM_RATE").getVal());
                rsTmp.updateString("SQM_RATE_DATE",(String)ObjPriceDtl.getAttribute("SQM_RATE_DATE").getObj());
                rsTmp.updateDouble("WT_RATE",(double)ObjPriceDtl.getAttribute("WT_RATE").getVal());
                rsTmp.updateString("WT_RATE_DATE",(String)ObjPriceDtl.getAttribute("WT_RATE_DATE").getObj());
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("QUALITY_ID",(String)ObjPriceDtl.getAttribute("QUALITY_ID").getObj());
                //rsHDetail.updateString("QUALITY_DESC",(String)ObjPriceDtl.getAttribute("QUALITY_DESC").getObj());
                rsHDetail.updateDouble("SQM_RATE",(double)ObjPriceDtl.getAttribute("SQM_RATE").getVal());
                rsHDetail.updateString("SQM_RATE_DATE",(String)ObjPriceDtl.getAttribute("SQM_RATE_DATE").getObj());
                rsHDetail.updateDouble("WT_RATE",(double)ObjPriceDtl.getAttribute("WT_RATE").getVal());
                rsHDetail.updateString("WT_RATE_DATE",(String)ObjPriceDtl.getAttribute("WT_RATE_DATE").getObj());
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED", false);
                rsHDetail.insertRow();
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFeltPriceList.ModuleID;
            ObjFlow.DocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_POLICY_FELT_PRICELIST_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
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
            
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_SAL_POLICY_FELT_PRICELIST_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"' ");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsFeltPriceList.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean PriceUpdate() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER_H WHERE QUALITY_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL_H WHERE QUALITY_ID='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("WH_CODE",(String)getAttribute("WH_CODE").getObj());
            rsResultSet.updateString("QUALITY_DESC",(String)getAttribute("QUALITY_DESC").getObj());
            rsResultSet.updateDouble("SYN_PERC",(double) getAttribute("SYN_PERC").getVal());
            rsResultSet.updateInt("CHEM_TRTIN",Integer.parseInt((String) getAttribute("CHEM_TRTIN").getObj()));
            rsResultSet.updateInt("PIN_IND",Integer.parseInt((String) getAttribute("PIN_IND").getObj()));
            rsResultSet.updateInt("SPRL_IND",Integer.parseInt((String) getAttribute("SPRL_IND").getObj()));
            rsResultSet.updateInt("SUR_CHGIND",Integer.parseInt((String) getAttribute("SUR_CHGIND").getObj()));
            rsResultSet.updateInt("SQM_IND",Integer.parseInt((String) getAttribute("SQM_IND").getObj()));
            rsResultSet.updateInt("FILLER",(int) getAttribute("FILLER").getVal());
            //rsResultSet.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            //rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateString("REJECTED_REMARKS",(String)getAttribute("REJECTED_REMARKS").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","");
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_FELT_PRICELIST_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+(long)getAttribute("DOC_NO").getVal()+" ");
            RevNo++;
            String RevDocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
            rsHistory.updateString("WH_CODE",(String)getAttribute("WH_CODE").getObj());
            rsHistory.updateString("QUALITY_ID",(String)getAttribute("QUALITY_ID").getObj());
            rsHistory.updateString("QUALITY_DESC",(String)getAttribute("QUALITY_DESC").getObj());
            rsHistory.updateDouble("SYN_PERC",(double) getAttribute("SYN_PERC").getVal());
            rsHistory.updateInt("CHEM_TRTIN",Integer.parseInt((String) getAttribute("CHEM_TRTIN").getObj()));
            rsHistory.updateInt("PIN_IND",Integer.parseInt((String) getAttribute("PIN_IND").getObj()));
            rsHistory.updateInt("SPRL_IND",Integer.parseInt((String) getAttribute("SPRL_IND").getObj()));
            rsHistory.updateInt("SUR_CHGIND",Integer.parseInt((String) getAttribute("SUR_CHGIND").getObj()));
            rsHistory.updateInt("SQM_IND",Integer.parseInt((String) getAttribute("SQM_IND").getObj()));
            rsHistory.updateInt("FILLER",(int) getAttribute("FILLER").getVal());
            //rsHistory.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            //rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateString("REJECTED_REMARKS",(String)getAttribute("REJECTED_REMARKS").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            int docno=(int)getAttribute("DOC_NO").getVal();
            
            data.Execute("DELETE FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO="+docno+" ");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE QUALITY_ID='1'");
            
            //Now Insert records into detail table
            for(int i=1;i<=colPriceDetails.size();i++) {
                clsFeltPriceListDetail ObjPriceDtl=(clsFeltPriceListDetail) colPriceDetails.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsTmp.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("QUALITY_ID",(String)ObjPriceDtl.getAttribute("QUALITY_ID").getObj());
                //rsTmp.updateString("QUALITY_DESC",(String)ObjPriceDtl.getAttribute("QUALITY_DESC").getObj());
                rsTmp.updateDouble("SQM_RATE",(double)ObjPriceDtl.getAttribute("SQM_RATE").getVal());
                rsTmp.updateString("SQM_RATE_DATE",(String)ObjPriceDtl.getAttribute("SQM_RATE_DATE").getObj());
                rsTmp.updateDouble("WT_RATE",(double)ObjPriceDtl.getAttribute("WT_RATE").getVal());
                rsTmp.updateString("WT_RATE_DATE",(String)ObjPriceDtl.getAttribute("WT_RATE_DATE").getObj());
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("QUALITY_ID",(String)ObjPriceDtl.getAttribute("QUALITY_ID").getObj());
                //rsHDetail.updateString("QUALITY_DESC",(String)ObjPriceDtl.getAttribute("QUALITY_DESC").getObj());
                rsHDetail.updateDouble("SQM_RATE",(double)ObjPriceDtl.getAttribute("SQM_RATE").getVal());
                rsHDetail.updateString("SQM_RATE_DATE",(String)ObjPriceDtl.getAttribute("SQM_RATE_DATE").getObj());
                rsHDetail.updateDouble("WT_RATE",(double)ObjPriceDtl.getAttribute("WT_RATE").getVal());
                rsHDetail.updateString("WT_RATE_DATE",(String)ObjPriceDtl.getAttribute("WT_RATE_DATE").getObj());
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED", false);
                rsHDetail.insertRow();
                
            }
            String str = "";
            str = "DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+clsFeltPriceList.ModuleID+" AND DOC_NO ='"+Integer.toString((int)getAttribute("DOC_NO").getVal())+"' ";
            data.Execute(str);
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFeltPriceList.ModuleID;
            ObjFlow.DocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_POLICY_FELT_PRICELIST_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
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
            
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_SAL_POLICY_FELT_PRICELIST_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"' ");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsFeltPriceList.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
                else {
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsFeltPriceList.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsFeltPriceList.ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            String lDocNo=Integer.toString((int)getAttribute("DOC_NO").getVal());
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND DOC_NO='"+lDocNo+"' ";
                data.Execute(strQry);
                strQry = "DELETE FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND DOC_NO='"+lDocNo+"'";
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
    
    
    public Object getObject(int pCompanyID, String lDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND DOC_NO='" + lDocNo + "'" ;
        clsFeltPriceList ObjPriceList = new clsFeltPriceList();
        ObjPriceList.Filter(strCondition,pCompanyID);
        return ObjPriceList;
    }
    
    public static Object getObjectEx(int pCompanyID,String lDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND QUALITY_ID='" + lDocNo + "'" ;
        clsFeltPriceList ObjPriceList = new clsFeltPriceList();
        ObjPriceList.LoadData(pCompanyID);
        ObjPriceList.Filter(strCondition,pCompanyID);
        return ObjPriceList;
        
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
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsFeltPriceList ObjPrice =new clsFeltPriceList();
                ObjPrice.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjPrice.setAttribute("DOC_NO",rsTmp.getInt("DOC_NO"));
                ObjPrice.setAttribute("WH_CODE",rsTmp.getString("WH_CODE"));
                ObjPrice.setAttribute("QUALITY_ID",rsTmp.getString("QUALITY_ID"));
                ObjPrice.setAttribute("QUALITY_DESC",rsTmp.getString("QUALITY_DESC"));
                ObjPrice.setAttribute("SYN_PERC",rsTmp.getDouble("SYN_PERC"));
                ObjPrice.setAttribute("CHEM_TRTIN",rsTmp.getInt("CHEM_TRTIN"));
                ObjPrice.setAttribute("PIN_IND",rsTmp.getInt("PIN_IND"));
                ObjPrice.setAttribute("SPRL_IND",rsTmp.getInt("SPRL_IND"));
                ObjPrice.setAttribute("SUR_CHGIND",rsTmp.getInt("SUR_CHGIND"));
                ObjPrice.setAttribute("SQM_IND",rsTmp.getInt("SQM_IND"));
                ObjPrice.setAttribute("FILLER",rsTmp.getInt("FILLER"));
                
                ObjPrice.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjPrice.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjPrice.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjPrice.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjPrice.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                ObjPrice.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                //ObjPriceDtl.setAttribute("STATUS",rsTmp.getString("STATUS"));
                ObjPrice.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjPrice.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjPrice.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjPrice.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjPrice.colPriceDetails.clear();
                String mCompanyID=Long.toString((long)ObjPrice.getAttribute("COMPANY_ID").getVal());
                String mDocNo=Integer.toString((int)ObjPrice.getAttribute("DOC_NO").getVal());
                
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='" + mDocNo + "' ");
                
                Counter=0;
                while(rsTmp2.next()) {
                    Counter=Counter+1;
                    clsFeltPriceListDetail ObjPriceDtl=new clsFeltPriceListDetail();
                    
                    ObjPriceDtl.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjPriceDtl.setAttribute("DOC_NO",rsTmp2.getInt("DOC_NO"));
                    ObjPriceDtl.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                    ObjPriceDtl.setAttribute("QUALITY_ID",rsTmp2.getString("QUALITY_ID"));
                    ObjPriceDtl.setAttribute("QUALITY_DESC",rsTmp2.getString("QUALITY_DESC"));
                    ObjPriceDtl.setAttribute("SQM_RATE",rsTmp2.getDouble("SQM_RATE"));
                    ObjPriceDtl.setAttribute("SQM_RATE_DATE",rsTmp2.getString("SQM_RATE_DATE"));
                    ObjPriceDtl.setAttribute("WT_RATE",rsTmp2.getDouble("WT_RATE"));
                    ObjPriceDtl.setAttribute("WT_RATE_DATE",rsTmp2.getString("WT_RATE_DATE"));
                    ObjPriceDtl.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjPriceDtl.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjPriceDtl.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjPriceDtl.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    ObjPrice.colPriceDetails.put(Long.toString(Counter),ObjPriceDtl);
                }// Innser while
                
                List.put(Long.toString(Counter),ObjPrice);
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
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY DOC_NO";
                //strSql = "SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER " + pCondition ;
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
            setAttribute("DOC_NO",rsResultSet.getInt("DOC_NO"));
            setAttribute("WH_CODE",rsResultSet.getString("WH_CODE"));
            setAttribute("QUALITY_ID",rsResultSet.getString("QUALITY_ID"));
            setAttribute("QUALITY_DESC",rsResultSet.getString("QUALITY_DESC"));
            setAttribute("SYN_PERC",rsResultSet.getDouble("SYN_PERC"));
            setAttribute("CHEM_TRTIN",rsResultSet.getString("CHEM_TRTIN"));
            setAttribute("PIN_IND",rsResultSet.getString("PIN_IND"));
            setAttribute("SPRL_IND",rsResultSet.getString("SPRL_IND"));
            setAttribute("SUR_CHGIND",rsResultSet.getString("SUR_CHGIND"));
            setAttribute("SQM_IND",rsResultSet.getString("SQM_IND"));
            setAttribute("FILLER",rsResultSet.getInt("FILLER"));
            
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            //setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            //Now Populate the collection
            //first clear the collection
            colPriceDetails.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mDocNo=Integer.toString((int) getAttribute("DOC_NO").getVal());
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL_H WHERE COMPANY_ID=" + mCompanyID + " AND DOC_NO='" + mDocNo + "' AND REVISION_NO="+RevNo+" ORDER BY DOC_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE COMPANY_ID=" + mCompanyID + " AND DOC_NO='" + mDocNo + "'  ORDER BY DOC_NO");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFeltPriceListDetail ObjPriceDtl = new clsFeltPriceListDetail();
                
                ObjPriceDtl.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjPriceDtl.setAttribute("DOC_NO",rsTmp.getInt("DOC_NO"));
                ObjPriceDtl.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjPriceDtl.setAttribute("QUALITY_ID",rsTmp.getString("QUALITY_ID"));
                ObjPriceDtl.setAttribute("SQM_RATE",rsTmp.getDouble("SQM_RATE"));
                ObjPriceDtl.setAttribute("SQM_RATE_DATE",rsTmp.getString("SQM_RATE_DATE"));
                ObjPriceDtl.setAttribute("WT_RATE",rsTmp.getDouble("WT_RATE"));
                ObjPriceDtl.setAttribute("WT_RATE_DATE",rsTmp.getString("WT_RATE_DATE"));
                ObjPriceDtl.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjPriceDtl.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjPriceDtl.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjPriceDtl.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                
                colPriceDetails.put(Long.toString(Counter),ObjPriceDtl);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder) {
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
                strSQL="SELECT D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO,D_SAL_POLICY_FELT_PRICELIST_HEADER.QUALITY_ID,RECEIVED_DATE FROM D_SAL_POLICY_FELT_PRICELIST_HEADER,D_COM_DOC_DATA WHERE D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_SAL_POLICY_FELT_PRICELIST_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_POLICY_FELT_PRICELIST_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+"  AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsFeltPriceList.ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO,D_SAL_POLICY_FELT_PRICELIST_HEADER.QUALITY_ID,RECEIVED_DATE FROM D_SAL_POLICY_FELT_PRICELIST_HEADER,D_COM_DOC_DATA WHERE D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_SAL_POLICY_FELT_PRICELIST_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_POLICY_FELT_PRICELIST_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+"  AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsFeltPriceList.ModuleID+" ORDER BY D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO,D_SAL_POLICY_FELT_PRICELIST_HEADER.QUALITY_ID,RECEIVED_DATE FROM D_SAL_POLICY_FELT_PRICELIST_HEADER,D_COM_DOC_DATA WHERE D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_SAL_POLICY_FELT_PRICELIST_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_POLICY_FELT_PRICELIST_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsFeltPriceList.ModuleID+" ORDER BY D_SAL_POLICY_FELT_PRICELIST_HEADER.DOC_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                
                Counter=Counter+1;
                clsFeltPriceList ObjDoc=new clsFeltPriceList();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp3.getString("DOC_NO"));
                ObjDoc.setAttribute("QUALITY_ID",rsTmp3.getString("QUALITY_ID"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                //ObjDoc.setAttribute("DEPT_ID",rsTmp3.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Long.toString(Counter),ObjDoc);
                
            }//end of while
            
            //tmpConn.close();
            rsTmp3.close();
            tmpStmt3.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    //    public static HashMap getPriceDtlList(int pCompanyID,String pQualityID,boolean pAllData,int pType) {
    //        ResultSet rsTmp=null;
    //        Connection tmpConn=null;
    //        Statement tmpStmt=null;
    //
    //        ResultSet rsTmp2=null;
    //        Statement tmpStmt2=null;
    //        int Counter1 = 0;
    //
    //        HashMap List=new HashMap();
    //        long Counter=0;
    //        String strSQL;
    //
    //        try {
    //            tmpConn=data.getConn();
    //            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //            if(pAllData) {
    //                strSQL = "SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUALITY_ID='"+pQualityID.trim()+"' AND QUALITY_ID IN (SELECT QUALITY_ID FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE QUALITY_ID='"+pQualityID+"' AND APPROVED=1) ORDER BY SR_NO";
    //            }
    //            else{
    //
    //                strSQL="SELECT B.* ";
    //                strSQL+="FROM ";
    //                strSQL+="D_SAL_POLICY_FELT_PRICELIST_HEADER A, ";
    //                strSQL+="D_SAL_POLICY_FELT_PRICELIST_DETAIL B ";
    //                strSQL+="LEFT JOIN D_INV_ISSUE_DETAIL I ON (I.REQ_NO=B.REQ_NO AND I.REQ_SRNO=B.SR_NO AND I.ISSUE_NO IN (SELECT ISSUE_NO FROM D_INV_ISSUE_HEADER WHERE ISSUE_NO=I.ISSUE_NO AND APPROVED=1 AND CANCELED=0)), ";
    //                strSQL+="D_COM_DEPT_MASTER D ";
    //                strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
    //                strSQL+="A.REQ_NO=B.REQ_NO AND ";
    //                strSQL+="A.REQ_TYPE=B.REQ_TYPE AND ";
    //                strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
    //                strSQL+="A.APPROVED=1 AND A.CANCELED=0 AND ";
    //                strSQL+="B.COMPANY_ID=D.COMPANY_ID AND A.DEST_DEPT_ID=D.DEPT_ID AND ";
    //                strSQL+="A.REQ_TYPE="+pType+" AND A.REQ_NO='"+pMRNo+"' ";
    //                strSQL+="GROUP BY B.REQ_NO,B.REQ_TYPE,B.SR_NO ";
    //                strSQL+="HAVING IF(SUM(I.QTY) IS NULL,0,SUM(I.QTY)) <B.REQ_QTY ";
    //
    //            }
    //
    //            rsTmp2=tmpStmt.executeQuery(strSQL);
    //            rsTmp2.first();
    //
    //            Counter1=0;
    //            while(! rsTmp2.isAfterLast()) {
    //                Counter1++;
    //                clsFeltPriceListDetail ObjPriceDtl=new clsFeltPriceListDetail();
    //
    //                ObjPriceDtl.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
    //                ObjPriceDtl.setAttribute("REQ_NO",rsTmp2.getString("REQ_NO"));
    //                ObjPriceDtl.setAttribute("REQ_TYPE",rsTmp2.getInt("REQ_TYPE"));
    //                ObjPriceDtl.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
    //                ObjPriceDtl.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
    //                ObjPriceDtl.setAttribute("ITEM_EXTRA_DESC",rsTmp2.getString("ITEM_EXTRA_DESC"));
    //                ObjPriceDtl.setAttribute("REQ_QTY",rsTmp2.getFloat("REQ_QTY"));
    //
    //                double IssuedQty=data.getDoubleValueFromDB("SELECT SUM(QTY) AS TOTAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.REQ_NO='"+pMRNo+"' AND A.REQ_SRNO="+rsTmp2.getLong("SR_NO"));
    //
    //                ObjPriceDtl.setAttribute("ISSUED_QTY",IssuedQty);
    //                ObjPriceDtl.setAttribute("BAL_QTY",rsTmp2.getDouble("REQ_QTY")-IssuedQty);
    //                ObjPriceDtl.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
    //                ObjPriceDtl.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
    //                ObjPriceDtl.setAttribute("MFG_PROG_NO",rsTmp2.getString("MFG_PROG_NO"));
    //                ObjPriceDtl.setAttribute("BLEND_CODE",rsTmp2.getString("BLEND_CODE"));
    //                ObjPriceDtl.setAttribute("REQUIRED_DATE",rsTmp2.getString("REQUIRED_DATE"));
    //                ObjPriceDtl.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
    //                ObjPriceDtl.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
    //                ObjPriceDtl.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
    //                ObjPriceDtl.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
    //                ObjPriceDtl.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
    //
    //                List.put(Integer.toString(Counter1),ObjPriceDtl);
    //                rsTmp2.next();
    //            }
    //
    //            rsTmp.close();
    //            //tmpConn.close();
    //            tmpStmt.close();
    //            rsTmp2.close();
    //            tmpStmt2.close();
    //
    //        }
    //        catch(Exception e) {
    //        }
    //
    //        return List;
    //    }
    //
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' ");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_SAL_POLICY_FELT_PRICELIST_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"' ");
            
            while(rsTmp.next()) {
                clsFeltPriceList ObjPrice=new clsFeltPriceList();
                
                ObjPrice.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjPrice.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjPrice.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjPrice.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjPrice.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjPrice.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjPrice);
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
            rsTmp=data.getResult("SELECT DOC_NO,QUALITY_ID,APPROVED,CANCELLED FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELLED")) {
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
            rsTmp=data.getResult("SELECT DOC_NO,QUALITY_ID FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND CANCELLED=0  ");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_SAL_POLICY_FELT_PRICELIST_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(clsFeltPriceList.ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_SAL_POLICY_FELT_PRICELIST_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' ");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
}
