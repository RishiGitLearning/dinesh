/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Stores;

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
 * @author  komal
 * @version
 */

public class clsGateInward {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int nModuleId = 95;
    
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
    public clsGateInward() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("INWARD_NO", new Variant(""));
        props.put("INWARD_DATE",new Variant(""));
        props.put("SUPPLIER_CODE", new Variant(""));
        props.put("CHALAN_NO", new Variant(""));
        props.put("CHALAN_DATE",new Variant(""));
        props.put("INVOICE_NO", new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        
        props.put("STM_NO",new Variant(""));
        props.put("STM_DATE",new Variant(""));
        
        props.put("REMARKS", new Variant(""));        
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INWARD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INWARD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY INWARD_DATE");
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
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date InwardDate=java.sql.Date.valueOf((String)getAttribute("INWARD_DATE").getObj());
            
            if((InwardDate.after(FinFromDate)||InwardDate.compareTo(FinFromDate)==0)&&(InwardDate.before(FinToDate)||InwardDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Inward date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GATE_INWARD_HEADER_H WHERE INWARD_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_GATE_INWARD_DETAIL_H WHERE INWARD_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("INWARD_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,nModuleId, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            
            rsResultSet.updateString("INWARD_NO",(String)getAttribute("INWARD_NO").getObj());
            rsResultSet.updateString("INWARD_DATE",(String)getAttribute("INWARD_DATE").getObj());
            rsResultSet.updateString("SUPPLIER_CODE",(String)getAttribute("SUPPLIER_CODE").getObj());
            rsResultSet.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            
            rsResultSet.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
            rsResultSet.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
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
            rsHistory.updateString("INWARD_NO",(String)getAttribute("INWARD_NO").getObj());
            rsHistory.updateString("INWARD_DATE",(String)getAttribute("INWARD_DATE").getObj());
            rsHistory.updateString("SUPPLIER_CODE",(String)getAttribute("SUPPLIER_CODE").getObj());
            rsHistory.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            
            rsHistory.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
            rsHistory.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
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
            ObjFlow.ModuleID=nModuleId; //GATE INWARD
            ObjFlow.DocNo=(String)getAttribute("INWARD_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GATE_INWARD_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="INWARD_NO";
            
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
            java.sql.Date InwardDate=java.sql.Date.valueOf((String)getAttribute("INWARD_DATE").getObj());
            
            if((InwardDate.after(FinFromDate)||InwardDate.compareTo(FinFromDate)==0)&&(InwardDate.before(FinToDate)||InwardDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GATE_INWARD_HEADER_H WHERE INWARD_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("INWARD_NO").getObj();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("INWARD_DATE",(String)getAttribute("INWARD_DATE").getObj());
            rsResultSet.updateString("SUPPLIER_CODE",(String)getAttribute("SUPPLIER_CODE").getObj());
            rsResultSet.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            
            rsResultSet.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
            rsResultSet.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_GATE_INWARD_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INWARD_NO='"+(String)getAttribute("INWARD_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("INWARD_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("INWARD_NO",(String)getAttribute("INWARD_NO").getObj());
            rsHistory.updateString("INWARD_DATE",(String)getAttribute("INWARD_DATE").getObj());
            rsHistory.updateString("SUPPLIER_CODE",(String)getAttribute("SUPPLIER_CODE").getObj());
            rsHistory.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            
            rsHistory.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
            rsHistory.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());            
            rsHistory.updateBoolean("CANCELLED",false);
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
            ObjFlow.ModuleID=nModuleId; //GATE INWARD
            ObjFlow.DocNo=(String)getAttribute("INWARD_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GATE_INWARD_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="INWARD_NO";
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
                data.Execute("UPDATE D_INV_GATE_INWARD_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INWARD_NO='"+ObjFlow.DocNo+"'");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND INWARD_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND INWARD_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
            String lDocNo=(String)getAttribute("INWARD_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND INWARD_NO='"+lDocNo+"'";
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
    
    
    public Object getObject(int pCompanyID, String pInwardNo) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND INWARD_NO='" + pInwardNo + "'" ;
        clsGateInward ObjGateInward = new clsGateInward();
        ObjGateInward.Filter(strCondition,pCompanyID);
        return ObjGateInward;
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
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_GATE_INWARD_HEADER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsGateInward ObjGateInward =new clsGateInward();
                ObjGateInward.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjGateInward.setAttribute("INWARD_NO",rsTmp.getString("INWARD_NO"));
                ObjGateInward.setAttribute("INWARD_DATE",rsTmp.getString("INWARD_DATE"));
                ObjGateInward.setAttribute("SUPPLIER_CODE",rsTmp.getString("SUPPLIER_CODE"));
                ObjGateInward.setAttribute("CHALAN_NO",rsTmp.getString("CHALAN_NO"));
                ObjGateInward.setAttribute("CHALAN_DATE",rsTmp.getString("CHALAN_DATE"));
                ObjGateInward.setAttribute("INVOICE_NO",rsTmp.getString("INVOICE_NO"));
                ObjGateInward.setAttribute("INVOICE_DATE",rsTmp.getString("INVOICE_DATE"));
                
                ObjGateInward.setAttribute("STM_NO",rsTmp.getString("STM_NO"));
                ObjGateInward.setAttribute("STM_DATE",rsTmp.getString("STM_DATE"));
                
                ObjGateInward.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjGateInward.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjGateInward.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjGateInward.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjGateInward.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjGateInward.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                ObjGateInward.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjGateInward.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjGateInward.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjGateInward.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                List.put(Long.toString(Counter),ObjGateInward);
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
            String strSql = "SELECT * FROM D_INV_GATE_INWARD_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND INWARD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INWARD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY INWARD_NO";
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
            setAttribute("INWARD_NO",rsResultSet.getString("INWARD_NO"));
            setAttribute("INWARD_DATE",rsResultSet.getString("INWARD_DATE"));
            setAttribute("SUPPLIER_CODE",rsResultSet.getString("SUPPLIER_CODE"));
            setAttribute("CHALAN_NO",rsResultSet.getString("CHALAN_NO"));
            setAttribute("CHALAN_DATE",rsResultSet.getString("CHALAN_DATE"));
            
            setAttribute("STM_NO",rsResultSet.getString("STM_NO"));
            setAttribute("STM_DATE",rsResultSet.getString("STM_DATE"));
            
            setAttribute("INVOICE_NO",rsResultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE",rsResultSet.getString("INVOICE_DATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GATE_INWARD_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND INWARD_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_GATE_INWARD_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INWARD_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsGateInward ObjGateInward=new clsGateInward();
                
                ObjGateInward.setAttribute("INWARD_NO",rsTmp.getString("INWARD_NO"));
                ObjGateInward.setAttribute("INWARD_DATE",rsTmp.getString("INWARD_DATE"));
                ObjGateInward.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjGateInward.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjGateInward.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjGateInward.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjGateInward.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjGateInward);
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
            rsTmp=data.getResult("SELECT INWARD_NO,APPROVED,CANCELED FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INWARD_NO='"+pDocNo+"'");
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
            rsTmp=data.getResult("SELECT INWARD_NO FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INWARD_NO='"+pDocNo+"' AND CANCELED=0");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_GATE_INWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INWARD_NO='"+pDocNo+"'");
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
                data.Execute("UPDATE D_INV_GATE_INWARD_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INWARD_NO='"+pDocNo+"'");
                
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
