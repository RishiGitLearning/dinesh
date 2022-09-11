    /*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */

package EITLERP.Production.ReportUI;

import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;
import java.text.DecimalFormat;
import java.lang.Double;

/**
 *
 * @author  root
 */
public class clsComplain {
      
    public String LastError="";
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
 
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=717;
    public HashMap colMRItems;
   
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
    
    /** Creates a new instance of clsSales_Party */
    public clsComplain() {
        LastError = "";
        props=new HashMap();       
        props.put("CPNT_NO",new Variant(""));
        props.put("CPNT_DATE",new Variant(""));
        props.put("CPNT_NATURE",new Variant(""));
        props.put("CPNT_ATTENDED_ON",new Variant(""));                
        props.put("CPNT_CLOSE_DATE",new Variant(""));
        props.put("CPNT_REMARKS",new Variant(""));
        props.put("CPNT_PARTYCODE",new Variant(""));   
        props.put("CPNT_PARTY_NAME",new Variant(""));
        props.put("CPNT_INVOICE_NO",new Variant(""));
        props.put("CPNT_INVOICE_DATE",new Variant(""));        
        props.put("CPNT_INVOICE_AMOUNT",new Variant(""));
        props.put("CPNT_PC_NO",new Variant(""));
        props.put("CPNT_MC_NO",new Variant(""));
        props.put("CPNT_POSITION",new Variant(""));
        props.put("CPNT_SIZE_LENGTH",new Variant(""));
        props.put("CPNT_SIZE_WIDTH",new Variant(""));
        props.put("CPNT_GSM",new Variant(""));
        props.put("CPNT_ACTION_DATE1",new Variant(""));
        props.put("CPNT_ACTION_REMARK1",new Variant(""));
        props.put("CPNT_ACTION_DATE2",new Variant(""));
        props.put("CPNT_ACTION_REMARK2",new Variant(""));
        props.put("CPNT_ACTION_DATE3",new Variant(""));
        props.put("CPNT_ACTION_REMARK3",new Variant(""));
        props.put("CPNT_ACTION_DATE4",new Variant(""));
        props.put("CPNT_ACTION_REMARK4",new Variant(""));
        props.put("CPNT_ACTION_DATE5",new Variant(""));
        props.put("CPNT_ACTION_REMARK5",new Variant(""));
        props.put("CPNT_ACTION_DATE6",new Variant(""));
        props.put("CPNT_ACTION_REMARK6",new Variant(""));
        props.put("CPNT_ACTION_DATE7",new Variant(""));
        props.put("CPNT_ACTION_REMARK7",new Variant(""));
        props.put("CPNT_ACTION_DATE8",new Variant(""));
        props.put("CPNT_ACTION_REMARK8",new Variant(""));
        props.put("CPNT_ACTION_DATE9",new Variant(""));
        props.put("CPNT_ACTION_REMARK9",new Variant(""));
        props.put("CPNT_ACTION_DATE10",new Variant(""));
        props.put("CPNT_ACTION_REMARK10",new Variant(""));
        props.put("CPNT_RESOLUTION",new Variant(""));
        props.put("CREATED_BY",new Variant("")); 
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
        // -- Approval Specific Fields --
        /*props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("SEND_DOC_TO",new Variant(0));
        props.put("APPROVER_REMARKS",new Variant(0));
        //----------------------
         */
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_COMPLAINTS ORDER BY CPNT_NO DESC");
            //rsResultSet1=Stmt.executeQuery("SELECT A.*,PRIORITY_DESC FROM (SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC) AS A LEFT JOIN (SELECT PRIORITY_DESC,PRIORITY_ID FROM PRODUCTION.FELT_PRIORITY_MASTER ) AS B ON A.PRIORITY=B.PRIORITY_ID");
            HistoryView=false;
            Ready=true;
            MoveFirst();
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
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;        
        tmpConn=data.getConn();        
        long Counter=0;
        int RevNo=0;        
        try {
           /* if(HistoryView) {
                RevNo=UtilFunctions.getInt(rsResultSet,"REVISION_NO",0);
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }*/
            setAttribute("CPNT_NO",UtilFunctions.getString(rsResultSet,"CPNT_NO", ""));
            setAttribute("CPNT_DATE",UtilFunctions.getString(rsResultSet,"CPNT_DATE","0000-00-00"));
            setAttribute("CPNT_NATURE",UtilFunctions.getString(rsResultSet,"CPNT_NATURE", ""));
            setAttribute("CPNT_ATTENDED_ON",UtilFunctions.getString(rsResultSet,"CPNT_ATTENDED_ON",""));            
            setAttribute("CPNT_CLOSE_DATE",UtilFunctions.getString(rsResultSet,"CPNT_CLOSE_DATE","0000-00-00"));
            setAttribute("CPNT_REMARKS",UtilFunctions.getString(rsResultSet,"CPNT_REMARKS",""));
            setAttribute("CPNT_PARTYCODE",UtilFunctions.getString(rsResultSet,"CPNT_PARTYCODE",""));
            setAttribute("CPNT_PARTY_NAME",UtilFunctions.getString(rsResultSet,"CPNT_PARTY_NAME",""));
            setAttribute("CPNT_INVOICE_NO",UtilFunctions.getString(rsResultSet,"CPNT_INVOICE_NO", ""));
            //setAttribute("ORDER_CODE",UtilFunctions.getString(rsResultSet,"ORDER_CODE", ""));
            //setAttribute("PRIORITY",UtilFunctions.getInt(rsResultSet,"PRIORITY",0));
            setAttribute("CPNT_INVOICE_DATE",UtilFunctions.getString(rsResultSet,"CPNT_INVOICE_DATE","0000-00-00"));
            setAttribute("CPNT_INVOICE_AMOUNT",UtilFunctions.getString(rsResultSet,"CPNT_INVOICE_AMOUNT", ""));
            setAttribute("CPNT_PC_NO",UtilFunctions.getString(rsResultSet,"CPNT_PC_NO", ""));
            setAttribute("CPNT_MC_NO",UtilFunctions.getString(rsResultSet,"CPNT_MC_NO", ""));
            setAttribute("CPNT_POSITION",UtilFunctions.getString(rsResultSet,"CPNT_POSITION", ""));
            setAttribute("CPNT_SIZE_LENGTH",UtilFunctions.getString(rsResultSet,"CPNT_SIZE_LENGTH", ""));
            setAttribute("CPNT_SIZE_WIDTH",UtilFunctions.getString(rsResultSet,"CPNT_SIZE_WIDTH", ""));
            setAttribute("CPNT_GSM",UtilFunctions.getString(rsResultSet,"CPNT_GSM", ""));
            setAttribute("CPNT_ACTION_DATE1",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE1", ""));
            setAttribute("CPNT_ACTION_REMARK1",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK1", ""));
            setAttribute("CPNT_ACTION_DATE2",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE2", ""));
            setAttribute("CPNT_ACTION_REMARK2",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK2", ""));
            setAttribute("CPNT_ACTION_DATE3",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE3", ""));
            setAttribute("CPNT_ACTION_REMARK3",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK3", ""));
            setAttribute("CPNT_ACTION_DATE4",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE4", ""));
            setAttribute("CPNT_ACTION_REMARK4",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK4", ""));
            setAttribute("CPNT_ACTION_DATE5",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE5", ""));
            setAttribute("CPNT_ACTION_REMARK5",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK5", ""));
            setAttribute("CPNT_ACTION_DATE6",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE6", ""));
            setAttribute("CPNT_ACTION_REMARK6",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK6", ""));
            setAttribute("CPNT_ACTION_DATE7",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE7", ""));
            setAttribute("CPNT_ACTION_REMARK7",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK7", ""));
            setAttribute("CPNT_ACTION_DATE8",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE8", ""));
            setAttribute("CPNT_ACTION_REMARK8",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK8", ""));
            setAttribute("CPNT_ACTION_DATE9",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE9", ""));
            setAttribute("CPNT_ACTION_REMARK9",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK9", ""));
            setAttribute("CPNT_ACTION_DATE10",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_DATE10", ""));
            setAttribute("CPNT_ACTION_REMARK10",UtilFunctions.getString(rsResultSet,"CPNT_ACTION_REMARK10", ""));
            setAttribute("CPNT_RESOLUTION",UtilFunctions.getString(rsResultSet,"CPNT_RESOLUTION", ""));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY", ""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            /*
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));            
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getBoolean(rsResultSet,"REJECTED",false));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS", ""));
           */
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
    public boolean IsEditable(int CompanyID,String PartyCode,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT PARTY_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CODE='"+PartyCode+"'";
            
            if(data.IsRecordExist(strSQL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM PRODUCTION.FELT_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+PartyCode+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
    
    public static HashMap getHistoryList(int CompanyID,String PartyCode) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_SAL_PARTY_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+PartyCode+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsSales_Party objParty=new clsSales_Party();
                    
                    objParty.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE", ""));
                    objParty.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objParty.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objParty.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objParty.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objParty.setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    objParty.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objParty);
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
    
    public boolean Insert() {
        
        Statement stHistory,stHeader,stHDetail;
        ResultSet rsHistory,rsHeader,rsTmp,rsHDetail;
        
        try {            
            //======= Check the Complain date ============//
            /*
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ComplainDate=java.sql.Date.valueOf((String)getAttribute("CPNT_DATE").getObj());
            
            if((ComplainDate.after(FinFromDate)||ComplainDate.compareTo(FinFromDate)==0)&&(ComplainDate.before(FinToDate)||ComplainDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Complain date is not within financial year.";
                return false;
            }
             */
            //=====================================================//
            
            // ---- History Related Changes ------ //
            /*
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL_H WHERE PROFORMA_NO='1'");
            rsHDetail.first();
             */
            //------------------------------------//
            
             //--------- Generate Complaint no.  ------------
           setAttribute("CPNT_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID, 717, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            //rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("CPNT_NO", getAttribute("CPNT_NO").getString());
            rsResultSet.updateString("CPNT_DATE", getAttribute("CPNT_DATE").getString());
            rsResultSet.updateString("CPNT_NATURE",getAttribute("CPNT_NATURE").getString());
            rsResultSet.updateString("CPNT_ATTENDED_ON",getAttribute("CPNT_ATTENDED_ON").getString());            
            rsResultSet.updateString("CPNT_CLOSE_DATE",getAttribute("CPNT_CLOSE_DATE").getString());            
            rsResultSet.updateString("CPNT_REMARKS",getAttribute("CPNT_REMARKS").getString());
            rsResultSet.updateString("CPNT_PARTYCODE",getAttribute("CPNT_PARTYCODE").getString());
            rsResultSet.updateString("CPNT_PARTY_NAME",getAttribute("CPNT_PARTY_NAME").getString());
            rsResultSet.updateString("CPNT_INVOICE_NO",getAttribute("CPNT_INVOICE_NO").getString());
            rsResultSet.updateString("CPNT_INVOICE_DATE",getAttribute("CPNT_INVOICE_DATE").getString());
            //rsResultSet.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            //rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            rsResultSet.updateString("CPNT_INVOICE_AMOUNT",(String)getAttribute("CPNT_INVOICE_AMOUNT").getObj());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateString("CPNT_PC_NO",getAttribute("CPNT_PC_NO").getString());
            rsResultSet.updateString("CPNT_MC_NO",getAttribute("CPNT_MC_NO").getString());
            rsResultSet.updateString("CPNT_POSITION",getAttribute("CPNT_POSITION").getString());
            rsResultSet.updateString("CPNT_SIZE_LENGTH",getAttribute("CPNT_SIZE_LENGTH").getString());
            rsResultSet.updateString("CPNT_SIZE_WIDTH",getAttribute("CPNT_SIZE_WIDTH").getString());
            rsResultSet.updateString("CPNT_GSM",getAttribute("CPNT_GSM").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE1",getAttribute("CPNT_ACTION_DATE1").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK1",getAttribute("CPNT_ACTION_REMARK1").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE2",getAttribute("CPNT_ACTION_DATE2").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK2",getAttribute("CPNT_ACTION_REMARK2").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE3",getAttribute("CPNT_ACTION_DATE3").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK3",getAttribute("CPNT_ACTION_REMARK3").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE4",getAttribute("CPNT_ACTION_DATE4").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK5",getAttribute("CPNT_ACTION_REMARK5").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE6",getAttribute("CPNT_ACTION_DATE6").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK6",getAttribute("CPNT_ACTION_REMARK6").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE7",getAttribute("CPNT_ACTION_DATE7").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK8",getAttribute("CPNT_ACTION_REMARK8").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE9",getAttribute("CPNT_ACTION_DATE9").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK9",getAttribute("CPNT_ACTION_REMARK9").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE10",getAttribute("CPNT_ACTION_DATE10").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK10",getAttribute("CPNT_ACTION_REMARK10").getString());
            rsResultSet.updateString("CPNT_RESOLUTION",getAttribute("CPNT_RESOLUTION").getString());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.insertRow();
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID);
                        
            //rsResultSet.updateString("PRIORITY_DATE",date);            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            /*
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);            
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());                        
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            */
            //===================== Update the Approval Flow ======================//
            /*
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsOrderParty.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("PARTY_CD").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName="FELT_ORDER_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="PARTY_CODE";
            
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
             */
            //================= Approval Flow Update complete ===================//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            /*
            try {
                //Conn.rollback();
            }
            catch(Exception c){}
             */
            //data.SetRollback();
            //data.SetAutoCommit(true);
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }    
    
    
    public boolean Filter(String Condition) {
        Ready=false;        
        try {
            String strSQL= "SELECT * FROM PRODUCTION.FELT_COMPLAINTS  " + Condition ;           
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_COMPLAINTS ORDER BY CPNT_NO ";
                //strSQL = "SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY PARTY_CD ";
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
    
    public boolean Update() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
        boolean Validate=true;
        
        try {
            
            String theDocNo=getAttribute("CPNT_NO").getString();
            
            //** Open History Table Connections **//
          //  stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
          //  rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD='1'"); // '1' for restricting all data retrieval
          //  rsHistory.first();
            //** --------------------------------**//
            
            rsResultSet.updateString("CPNT_NO", getAttribute("CPNT_NO").getString());
            rsResultSet.updateString("CPNT_DATE", getAttribute("CPNT_DATE").getString());
            rsResultSet.updateString("CPNT_NATURE",getAttribute("CPNT_NATURE").getString());
            rsResultSet.updateString("CPNT_ATTENDED_ON",getAttribute("CPNT_ATTENDED_ON").getString());
            rsResultSet.updateString("CPNT_CLOSE_DATE",getAttribute("CPNT_CLOSE_DATE").getString());
            rsResultSet.updateString("CPNT_REMARKS",getAttribute("CPNT_REMARKS").getString());
            rsResultSet.updateString("CPNT_PARTYCODE",getAttribute("CPNT_PARTYCODE").getString());
            rsResultSet.updateString("CPNT_PARTY_NAME",getAttribute("CPNT_PARTY_NAME").getString());
            rsResultSet.updateString("CPNT_INVOICE_NO",getAttribute("CPNT_INVOICE_NO").getString());
            rsResultSet.updateString("CPNT_INVOICE_DATE",getAttribute("CPNT_INVOICE_DATE").getString());
            //rsResultSet.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            //rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            rsResultSet.updateString("CPNT_INVOICE_AMOUNT",(String)getAttribute("CPNT_INVOICE_AMOUNT").getObj());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateString("CPNT_PC_NO",getAttribute("CPNT_PC_NO").getString());
            rsResultSet.updateString("CPNT_MC_NO",getAttribute("CPNT_MC_NO").getString());
            rsResultSet.updateString("CPNT_POSITION",getAttribute("CPNT_POSITION").getString());            
            rsResultSet.updateString("CPNT_SIZE_LENGTH",getAttribute("CPNT_SIZE_LENGTH").getString());
            rsResultSet.updateString("CPNT_SIZE_WIDTH",getAttribute("CPNT_SIZE_WIDTH").getString());
            rsResultSet.updateString("CPNT_GSM",getAttribute("CPNT_GSM").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE1",getAttribute("CPNT_ACTION_DATE1").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK1",getAttribute("CPNT_ACTION_REMARK1").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE2",getAttribute("CPNT_ACTION_DATE2").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK2",getAttribute("CPNT_ACTION_REMARK2").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE3",getAttribute("CPNT_ACTION_DATE3").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK3",getAttribute("CPNT_ACTION_REMARK3").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE4",getAttribute("CPNT_ACTION_DATE4").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK4",getAttribute("CPNT_ACTION_REMARK4").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE5",getAttribute("CPNT_ACTION_DATE5").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK5",getAttribute("CPNT_ACTION_REMARK5").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE6",getAttribute("CPNT_ACTION_DATE6").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK6",getAttribute("CPNT_ACTION_REMARK6").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE7",getAttribute("CPNT_ACTION_DATE7").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK7",getAttribute("CPNT_ACTION_REMARK7").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE8",getAttribute("CPNT_ACTION_DATE8").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK8",getAttribute("CPNT_ACTION_REMARK8").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE9",getAttribute("CPNT_ACTION_DATE9").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK9",getAttribute("CPNT_ACTION_REMARK9").getString());
            rsResultSet.updateString("CPNT_ACTION_DATE10",getAttribute("CPNT_ACTION_DATE10").getString());
            rsResultSet.updateString("CPNT_ACTION_REMARK10",getAttribute("CPNT_ACTION_REMARK10").getString());
            rsResultSet.updateString("CPNT_RESOLUTION",getAttribute("CPNT_RESOLUTION").getString());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());            
            //rsResultSet.updateString("PRIORITY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID);
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID+"-"+getAttribute("LAST_PRIO_USR").getString());
            /*rsResultSet.updateString("DELIV_DATE",getAttribute("DELIV_DATE").getString());
            rsResultSet.updateString("COMM_DATE",getAttribute("COMM_DATE").getString());
            rsResultSet.updateString("GRUP",getAttribute("GRUP").getString());                       
             */
            /*rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
             */
            rsResultSet.updateRow();            
        /*    
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_PARTY_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+(String)getAttribute("PARTY_CODE").getObj()+"'");
            RevNo++;            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);            
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());            
            rsHistory.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());            
            rsHistory.updateString("PARENT_PARTY_CODE", " ");
            rsHistory.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("SEASON_CODE",getAttribute("SEASON_CODE").getString());
            rsHistory.updateString("REG_DATE",getAttribute("REG_DATE").getString());
            rsHistory.updateInt("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getInt());            
            rsHistory.updateDouble("AMOUNT_LIMIT",getAttribute("AMOUNT_LIMIT").getDouble());            
            rsHistory.updateInt("OTHER_BANK_ID",getAttribute("OTHER_BANK_ID").getInt());                                                                        
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);            
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);            
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());          
            rsHistory.insertRow();   */ 
            return true;
       
        }
        catch(Exception e) {
            /*
            try {
                //Conn.rollback();
                //data.SetRollback();
            }
            catch(Exception c){}
            //data.SetAutoCommit(true);
            */
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean CanDelete(int CompanyID,String PartyCode) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND PARTY_CODE='"+PartyCode+"' AND (APPROVED=1)";
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
    
    public boolean Delete() {
        try {
            String PartyCode=getAttribute("PARTY_CODE").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,PartyCode)) {
                String strSQL = "DELETE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'";
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
    
    
    public Object getObject(int CompanyID, String PartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(CompanyID) + " AND PARTY_CODE='" + PartyCode + "' ";
        clsSales_Party objParty = new clsSales_Party();
        objParty.Filter(strCondition);
        return objParty;
    }
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_SAL_PARTY_MASTER.PARTY_CODE,D_SAL_PARTY_MASTER.MAIN_ACCOUNT_CODE,D_SAL_PARTY_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_MASTER,FELT_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=FELT_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=FELT_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FELT_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND FELT_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSales_Party.ModuleID+" AND D_SAL_PARTY_MASTER.APPROVED=0 ORDER BY FELT_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_SAL_PARTY_MASTER.PARTY_CODE,D_SAL_PARTY_MASTER.MAIN_ACCOUNT_CODE,D_SAL_PARTY_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_MASTER,FELT_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=FELT_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=FELT_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FELT_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND FELT_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSales_Party.ModuleID+" AND D_SAL_PARTY_MASTER.APPROVED=0 ORDER BY D_SAL_PARTY_MASTER.PARTY_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_SAL_PARTY_MASTER.PARTY_CODE,D_SAL_PARTY_MASTER.MAIN_ACCOUNT_CODE,D_SAL_PARTY_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_MASTER,FELT_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=FELT_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=FELT_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FELT_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND FELT_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSales_Party.ModuleID+" AND D_SAL_PARTY_MASTER.APPROVED=0 ORDER BY D_SAL_PARTY_MASTER.PARTY_CODE";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSales_Party ObjItem=new clsSales_Party();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjItem.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
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
            //Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pDocNo+"' ORDER BY REVISION_NO");
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
    
    public static boolean IsPartyExistEx(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsPartyExist(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND BLOCKED='N' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
   
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        long Counter2=0;
        
        tmpConn=data.getConn();
        
        try {
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSales_Party ObjParty =new clsSales_Party();
                ObjParty.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjParty.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjParty.setAttribute("PARTY_TYPE",rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                
                ObjParty.setAttribute("SEASON_CODE",rsTmp.getString("SEASON_CODE"));
                ObjParty.setAttribute("REG_DATE",rsTmp.getString("REG_DATE"));
                
                ObjParty.setAttribute("AREA_ID",rsTmp.getString("AREA_ID"));
                ObjParty.setAttribute("ADDRESS1",rsTmp.getString("ADDRESS1"));
                ObjParty.setAttribute("ADDRESS2",rsTmp.getString("ADDRESS2"));
                ObjParty.setAttribute("CITY_ID",rsTmp.getString("CITY_ID"));
                ObjParty.setAttribute("CITY_NAME",rsTmp.getString("CITY_NAME"));
                ObjParty.setAttribute("DISPATCH_STATION",rsTmp.getString("DISPATCH_STATION"));
                ObjParty.setAttribute("DISTRICT",rsTmp.getString("DISTRICT"));
                ObjParty.setAttribute("PINCODE",rsTmp.getString("PINCODE"));
                ObjParty.setAttribute("PHONE_NO",rsTmp.getString("PHONE_NO"));
                ObjParty.setAttribute("MOBILE_NO",rsTmp.getString("MOBILE_NO"));
                ObjParty.setAttribute("EMAIL",rsTmp.getString("EMAIL"));
                ObjParty.setAttribute("URL",rsTmp.getString("URL"));
                ObjParty.setAttribute("CONTACT_PERSON",rsTmp.getString("CONTACT_PERSON"));
                ObjParty.setAttribute("BANK_ID",rsTmp.getLong("BANK_ID"));
                ObjParty.setAttribute("BANK_NAME",rsTmp.getString("BANK_NAME"));
                ObjParty.setAttribute("BANK_ADDRESS",rsTmp.getString("BANK_ADDRESS"));
                ObjParty.setAttribute("BANK_CITY",rsTmp.getString("BANK_CITY"));
                ObjParty.setAttribute("CST_NO",rsTmp.getString("CST_NO"));
                ObjParty.setAttribute("CST_DATE",rsTmp.getString("CST_DATE"));
                ObjParty.setAttribute("ECC_NO",rsTmp.getString("ECC_NO"));
                ObjParty.setAttribute("ECC_DATE",rsTmp.getString("ECC_DATE"));
                ObjParty.setAttribute("TIN_NO",rsTmp.getString("TIN_NO"));
                ObjParty.setAttribute("TIN_DATE",rsTmp.getString("TIN_DATE"));
                ObjParty.setAttribute("PAN_NO",rsTmp.getString("PAN_NO"));
                ObjParty.setAttribute("PAN_DATE",rsTmp.getString("PAN_DATE"));
                ObjParty.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjParty.setAttribute("CHARGE_CODE",rsTmp.getString("CHARGE_CODE"));
                ObjParty.setAttribute("CHARGE_CODE_II",rsTmp.getString("CHARGE_CODE_II"));
                ObjParty.setAttribute("CREDIT_DAYS",rsTmp.getDouble("CREDIT_DAYS"));
                ObjParty.setAttribute("DOCUMENT_THROUGH",rsTmp.getString("DOCUMENT_THROUGH"));
                ObjParty.setAttribute("TRANSPORTER_ID",rsTmp.getLong("TRANSPORTER_ID"));
                ObjParty.setAttribute("TRANSPORTER_NAME",rsTmp.getString("TRANSPORTER_NAME"));
                ObjParty.setAttribute("AMOUNT_LIMIT",rsTmp.getDouble("AMOUNT_LIMIT"));
                ObjParty.setAttribute("OTHER_BANK_ID",rsTmp.getLong("OTHER_BANK_ID"));
                ObjParty.setAttribute("OTHER_BANK_NAME",rsTmp.getString("OTHER_BANK_NAME"));
                ObjParty.setAttribute("OTHER_BANK_ADDRESS",rsTmp.getString("OTHER_BANK_ADDRESS"));
                ObjParty.setAttribute("OTHER_BANK_CITY",rsTmp.getString("OTHER_BANK_CITY"));
                ObjParty.setAttribute("CATEGORY",rsTmp.getString("CATEGORY"));
                ObjParty.setAttribute("INSURANCE_CODE",rsTmp.getString("INSURANCE_CODE"));
                ObjParty.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjParty.setAttribute("DELAY_INTEREST_PER",rsTmp.getDouble("DELAY_INTEREST_PER"));
                ObjParty.setAttribute("ACCOUNT_CODES",rsTmp.getString("ACCOUNT_CODES"));
                
                ObjParty.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjParty.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjParty.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjParty.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjParty.setAttribute("HIERARCHY_ID",rsTmp.getLong("HIERARCHY_ID"));
                
            }//Outer while
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            //LastError = e.getMessage();
        }
        
        return List;
    }
    
    public static Object getObjectEx(int pCompanyID,String pPartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode+"' ";
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }
    
    public static Object getObjectExN(int pCompanyID,String pPartyCode, String MainCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode+"' AND MAIN_ACCOUNT_CODE="+MainCode;
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }
    
    public static boolean deleteParty(String PartyCode) {
        try {
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE DOC_NO='"+PartyCode+"' AND MODULE_ID='"+clsOrderParty.ModuleID+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    
     public static String getInvoiceAmount(String pInvoiceNo,String pInvoiceDate){
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        //pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        System.out.println(pInvoiceDate+pInvoiceNo);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT NET_AMOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("NET_AMOUNT");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String _getInvoiceAmount(String pInvoiceDate,String pInvoiceNo){
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        System.out.println(pInvoiceNo+pInvoiceDate);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT NET_AMOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("NET_AMOUNT");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
    
     
     public static String getInvoiceAmount(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        System.out.println(pInvoiceNo);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT NET_AMOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("NET_AMOUNT");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String _getInvoiceAmount(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        System.out.println(pInvoiceNo);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT NET_AMOUNT FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("NET_AMOUNT");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     
     public static String getPieceNo(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
       // pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PIECE_NO");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String _getPieceNo(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PIECE_NO");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
      
       public static String _getPieceNo(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
       
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PIECE_NO");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String getLength(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT LENGTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("LENGTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String _getLength(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT LENGTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("LENGTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String getLength(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT LENGTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("LENGTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String _getLength(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT LENGTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("LENGTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     public static String getWidth(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("WIDTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String _getWidth(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("WIDTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String getPartyCode(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_CODE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String getPartyName(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_NAME");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
      
       public static String _getPartyName(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_NAME");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
       public static String getPartyName(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_NAME");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
       
       public static String _getPartyName(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_NAME");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
      
     public static String getWidth(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("WIDTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String _getWidth(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("WIDTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String getGSM(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("GSQ");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String _getGSM(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("GSQ");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String getGSM(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        System.out.println(pInvoiceNo);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("GSQ");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     public static String getMachineNo(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT MACHINE_NO FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("MACHINE_NO");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
  
     public static String _getMachineNo(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT MACHINE_NO FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("MACHINE_NO");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String getPosition(String pInvoiceNo,String pInvoiceDate) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("POSITION");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
      
       public static String _getPosition(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
         pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+pInvoiceDate+"' AND INVOICE_NO='"+pInvoiceNo+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("POSITION");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String getPosition(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("POSITION");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
      public static String _getPosition(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO=(SELECT PIECE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"')");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("POSITION");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
      
    public static String getInvoiceNo(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        System.out.println(pInvoiceNo);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("INVOICE_NO");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String _getInvoiceNo(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        System.out.println(pInvoiceNo);
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("INVOICE_NO");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
    
     public static String getPartyCode(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
      try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_CODE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
     public static String _getPartyCode(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
      try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_CODE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      public static String _getPartyCode(String pInvoiceDate,String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        pInvoiceDate=EITLERPGLOBAL.formatDateDB(pInvoiceDate);
      try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+pInvoiceNo+"' AND INVOICE_DATE='"+pInvoiceDate+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("PARTY_CODE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
     
      
      public static String getInvoiceDate(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_DATE FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("INVOICE_DATE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
         public static String _getInvoiceDate(String pInvoiceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INVOICE_DATE FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+pInvoiceNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("INVOICE_DATE");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }   
      
}
