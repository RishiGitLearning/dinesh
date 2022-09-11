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

public class clsGateOutward {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int nModuleId = 96;
    
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
    public clsGateOutward() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("OUTWARD_NO", new Variant(""));
        props.put("OUTWARD_DATE",new Variant(""));
        props.put("GATEPASS_NO", new Variant(""));
        props.put("GATE_OUTWARD_DATE",new Variant(""));
        props.put("RGP_NRGP",new Variant(""));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND OUTWARD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND OUTWARD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY OUTWARD_NO");
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
            java.sql.Date OutwardDate=java.sql.Date.valueOf((String)getAttribute("OUTWARD_DATE").getObj());
            
            if((OutwardDate.after(FinFromDate)||OutwardDate.compareTo(FinFromDate)==0)&&(OutwardDate.before(FinToDate)||OutwardDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Outward date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GATE_OUTWARD_HEADER_H WHERE OUTWARD_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("OUTWARD_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,nModuleId, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            
            rsResultSet.updateString("OUTWARD_NO",(String)getAttribute("OUTWARD_NO").getObj());
            rsResultSet.updateString("OUTWARD_DATE",(String)getAttribute("OUTWARD_DATE").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateString("GATE_OUTWARD_DATE",(String)getAttribute("GATE_OUTWARD_DATE").getObj());
            rsResultSet.updateString("RGP_NRGP",(String)getAttribute("RGP_NRGP").getObj());
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
            rsHistory.updateString("OUTWARD_NO",(String)getAttribute("OUTWARD_NO").getObj());
            rsHistory.updateString("OUTWARD_DATE",(String)getAttribute("OUTWARD_DATE").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATE_OUTWARD_DATE",(String)getAttribute("GATE_OUTWARD_DATE").getObj());
            rsHistory.updateString("RGP_NRGP",(String)getAttribute("RGP_NRGP").getObj());
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
            ObjFlow.ModuleID=nModuleId; //GATE OUTWARD
            ObjFlow.DocNo=(String)getAttribute("OUTWARD_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GATE_OUTWARD_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="OUTWARD_NO";
            
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
            if (ObjFlow.Status.equals("F")) {
                String sql;
                sql = "UPDATE DINESHMILLS.D_INV_NRGP_HEADER H,DINESHMILLS.D_INV_GATE_OUTWARD_HEADER G "
                        + "SET H.WH_OUTWARD_NO=G.OUTWARD_NO,H.WH_OUTWARD_DATE=G.GATE_OUTWARD_DATE "
                        + "WHERE H.GATEPASS_NO=G.GATEPASS_NO AND H.GATEPASS_TYPE='FGP' AND "
                        + "G.OUTWARD_NO='" + (String) getAttribute("OUTWARD_NO").getObj() + "'";
                data.Execute(sql);
            }
            
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
            java.sql.Date OutwardDate=java.sql.Date.valueOf((String)getAttribute("OUTWARD_DATE").getObj());
            
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
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GATE_OUTWARD_HEADER_H WHERE OUTWARD_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("OUTWARD_NO").getObj();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("OUTWARD_DATE",(String)getAttribute("OUTWARD_DATE").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateString("GATE_OUTWARD_DATE",(String)getAttribute("GATE_OUTWARD_DATE").getObj());
            rsResultSet.updateString("RGP_NRGP",(String)getAttribute("RGP_NRGP").getObj());
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_GATE_OUTWARD_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND OUTWARD_NO='"+(String)getAttribute("OUTWARD_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("OUTWARD_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("OUTWARD_NO",(String)getAttribute("OUTWARD_NO").getObj());
            rsHistory.updateString("OUTWARD_DATE",(String)getAttribute("OUTWARD_DATE").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATE_OUTWARD_DATE",(String)getAttribute("GATE_OUTWARD_DATE").getObj());
            rsHistory.updateString("RGP_NRGP",(String)getAttribute("RGP_NRGP").getObj());
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
            ObjFlow.ModuleID=nModuleId; //GATE OUTWARD
            ObjFlow.DocNo=(String)getAttribute("OUTWARD_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GATE_OUTWARD_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="OUTWARD_NO";
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
                data.Execute("UPDATE D_INV_GATE_OUTWARD_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND OUTWARD_NO='"+ObjFlow.DocNo+"'");
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
            if (ObjFlow.Status.equals("F")) {
                String sql;
                sql = "UPDATE DINESHMILLS.D_INV_NRGP_HEADER H,DINESHMILLS.D_INV_GATE_OUTWARD_HEADER G "
                        + "SET H.WH_OUTWARD_NO=G.OUTWARD_NO,H.WH_OUTWARD_DATE=G.GATE_OUTWARD_DATE "
                        + "WHERE H.GATEPASS_NO=G.GATEPASS_NO AND H.GATEPASS_TYPE='FGP' AND "
                        + "G.OUTWARD_NO='" + (String) getAttribute("OUTWARD_NO").getObj() + "'";
                data.Execute(sql);
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND OUTWARD_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND OUTWARD_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
            String lDocNo=(String)getAttribute("OUTWARD_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND OUTWARD_NO='"+lDocNo+"'";
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
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND OUTWARD_NO='" + pOutwardNo + "'" ;
        clsGateOutward ObjGateOutward = new clsGateOutward();
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
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_GATE_OUTWARD_HEADER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsGateOutward ObjGateOutward =new clsGateOutward();
                ObjGateOutward.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjGateOutward.setAttribute("OUTWARD_NO",rsTmp.getString("OUTWARD_NO"));
                ObjGateOutward.setAttribute("OUTWARD_DATE",rsTmp.getString("OUTWARD_DATE"));
                ObjGateOutward.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjGateOutward.setAttribute("GATE_OUTWARD_DATE",rsTmp.getString("GATE_OUTWARD_DATE"));
                ObjGateOutward.setAttribute("RGP_NRGP",rsTmp.getString("RGP_NRGP"));
                ObjGateOutward.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjGateOutward.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjGateOutward.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjGateOutward.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjGateOutward.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjGateOutward.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                ObjGateOutward.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjGateOutward.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjGateOutward.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjGateOutward.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                List.put(Long.toString(Counter),ObjGateOutward);
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
            String strSql = "SELECT * FROM D_INV_GATE_OUTWARD_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND OUTWARD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND OUTWARD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY OUTWARD_NO";
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
            String Flag = rsResultSet.getString("OUTWARD_NO").toString().substring(0,1);
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("OUTWARD_NO",rsResultSet.getString("OUTWARD_NO"));
            setAttribute("OUTWARD_DATE",rsResultSet.getString("OUTWARD_DATE"));
            setAttribute("GATEPASS_NO",rsResultSet.getString("GATEPASS_NO"));
            setAttribute("GATE_OUTWARD_DATE",rsResultSet.getString("GATE_OUTWARD_DATE"));
            setAttribute("RGP_NRGP",Flag.trim());
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GATE_OUTWARD_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND OUTWARD_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_GATE_OUTWARD_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND OUTWARD_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsGateOutward ObjGateOutward=new clsGateOutward();
                
                ObjGateOutward.setAttribute("OUTWARD_NO",rsTmp.getString("OUTWARD_NO"));
                ObjGateOutward.setAttribute("OUTWARD_DATE",rsTmp.getString("OUTWARD_DATE"));
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
            rsTmp=data.getResult("SELECT OUTWARD_NO,APPROVED,CANCELED FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND OUTWARD_NO='"+pDocNo+"'");
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
            rsTmp=data.getResult("SELECT OUTWARD_NO FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND OUTWARD_NO='"+pDocNo+"' AND CANCELED=0");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_GATE_OUTWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND OUTWARD_NO='"+pDocNo+"'");
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
                data.Execute("UPDATE D_INV_GATE_OUTWARD_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND OUTWARD_NO='"+pDocNo+"'");
                
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
