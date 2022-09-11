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

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsPolicyPartyGrouping{
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    public static int ModuleId= 187;
    public HashMap colChildPartyGrp=new HashMap();
    private boolean HistoryView=false;
    
    
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
    public clsPolicyPartyGrouping() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("PARENT_PARTY_ID",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("CHILD_PARTY_ID",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("REVISION_NO",new Variant(0));
        props.put("CANCELLED", new Variant(false));
        props.put("PREFIX",new Variant(""));
        props.put("SUFFIX",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        //colChildPartyGrp=new HashMap();
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_HEADER WHERE DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO");
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
        
        Statement stHistory,stHeader,stHDetail,stDetail;
        ResultSet rsHistory,rsHeader,rsTmp,rsHDetail,rsDetail;
        int CompanyID=0;
        try {
            
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsPolicyPartyGrouping.ModuleId, (int)getAttribute("FFNO").getVal(),true));
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",getAttribute("DOC_DATE").getString());
            rsResultSet.updateString("FROM_DATE",getAttribute("FROM_DATE").getString());
            rsResultSet.updateString("TO_DATE",getAttribute("T0_DATE").getString());
            rsResultSet.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",getAttribute("DOC_DATE").getString());
            rsHistory.updateString("FROM_DATE",getAttribute("FROM_DATE").getString());
            rsHistory.updateString("TO_DATE",getAttribute("T0_DATE").getString());
            rsHistory.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            
            rsHistory.updateString("REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            
            
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_DETAIL WHERE DOC_NO='1'");
            rsDetail.first();
            
            for(int i=1;i<=colChildPartyGrp.size();i++) {
                clsPolicyPartyGroupingChild ObjChild=(clsPolicyPartyGroupingChild)colChildPartyGrp.get(Integer.toString(i));
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",ObjChild.getAttribute("COMPANY_ID").getInt());
                rsDetail.updateString("DOC_NO",ObjChild.getAttribute("DOC_NO").getString());
                rsDetail.updateInt("SR_NO",ObjChild.getAttribute("SR_NO").getInt());
                rsDetail.updateString("CHILD_PARTY_ID",ObjChild.getAttribute("CHILD_PARTY_ID").getString());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                
                
                rsDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                
                rsDetail.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",ObjChild.getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("DOC_NO",ObjChild.getAttribute("DOC_NO").getString());
                rsHDetail.updateInt("SR_NO",ObjChild.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("CHILD_PARTY_ID",ObjChild.getAttribute("CHILD_PARTY_ID").getString());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                
                
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsHDetail.insertRow();
                
            }
            
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsPolicyPartyGrouping.ModuleId; //MIR Raw Material
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("DOC_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_POLICY_PARTY_GROUPING_HEADER";
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
    
    
    
    
    public boolean Update() {
        
        Statement stHistory,stHeader,stHDetail,stDetail;
        ResultSet rsHistory,rsHeader,rsTmp,rsHDetail,rsDetail;
        int CompanyID=0;
        boolean Validate=true;
        String DocNo="";
        
        try {
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            Validate=true;
            
            /*
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("MIR_DATE").getObj());
             
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
             */
            
            
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            DocNo=getAttribute("DOC_NO").getString();
            
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",getAttribute("DOC_DATE").getString());
            rsResultSet.updateString("FROM_DATE",getAttribute("FROM_DATE").getString());
            rsResultSet.updateString("TO_DATE",getAttribute("T0_DATE").getString());
            rsResultSet.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            
            
            rsResultSet.updateRow();
            
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_POLICY_PARTY_GROUPING_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"' ");
            RevNo++;
            String RevDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",getAttribute("DOC_DATE").getString());
            rsHistory.updateString("FROM_DATE",getAttribute("FROM_DATE").getString());
            rsHistory.updateString("TO_DATE",getAttribute("T0_DATE").getString());
            rsHistory.updateString("PARENT_PARTY_ID",getAttribute("PARENT_PARTY_ID").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            
            rsHistory.updateString("REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            
            rsHistory.insertRow();
            
            
            data.Execute("DELETE FROM D_SAL_POLICY_PARTY_GROUPING_DETAIL WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' ");
            
            
            
            stDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDetail=stDetail.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_DETAIL WHERE DOC_NO='1'");
            rsDetail.first();
            
            for(int i=1;i<=colChildPartyGrp.size();i++) {
                clsPolicyPartyGroupingChild ObjChild=(clsPolicyPartyGroupingChild)colChildPartyGrp.get(Integer.toString(i));
                rsDetail.moveToInsertRow();
                rsDetail.updateInt("COMPANY_ID",ObjChild.getAttribute("COMPANY_ID").getInt());
                rsDetail.updateString("DOC_NO",ObjChild.getAttribute("DOC_NO").getString());
                rsDetail.updateInt("SR_NO",ObjChild.getAttribute("SR_NO").getInt());
                rsDetail.updateString("CHILD_PARTY_ID",ObjChild.getAttribute("CHILD_PARTY_ID").getString());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                
                
                //rsDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                //rsDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                
                rsDetail.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",ObjChild.getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("DOC_NO",ObjChild.getAttribute("DOC_NO").getString());
                rsHDetail.updateInt("SR_NO",ObjChild.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("CHILD_PARTY_ID",ObjChild.getAttribute("CHILD_PARTY_ID").getString());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsHDetail.insertRow();
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsPolicyPartyGrouping.ModuleId; //MIR Raw Material
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("DOC_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_POLICY_PARTY_GROUPING_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
            
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
            
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_SAL_POLICY_PARTY_GROUPING_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+ObjFlow.DocNo+"' AND MIR_TYPE=2");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=" + clsPolicyPartyGrouping.ModuleId + " AND DOC_NO='"+ObjFlow.DocNo+"'"); 
                
                ObjFlow.IsCreator=true;
            }
            
            
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
            //--------- Approval Flow Update complete -----------
            
            
            return true;
        }
        catch(Exception e) {
            
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    
    
    
    
    public Object getObject(int CompanyID, String ParentPartyID,int SrNo) {
        String strCondition = " WHERE PARENT_PARTY_ID='" + ParentPartyID + "' AND SR_NO="+SrNo;
        clsPolicyPartyGrouping objGrouping = new clsPolicyPartyGrouping();
        objGrouping.Filter(strCondition,CompanyID);
        return objGrouping;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_HEADER " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_POLICY_PARTY_GROUPING ORDER BY SR_NO ";
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
        ResultSet rsTmp,rsDetail;
        Connection tmpConn;
        Statement tmpStmt,rstDetail;
        int CompanyID=0;
        tmpConn=data.getConn();
        
        long Counter=0;
        int RevNo=0;
        
        try {
            
            
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("DOC_NO",rsResultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",rsResultSet.getString("DOC_DATE"));
            setAttribute("FROM_DATE",rsResultSet.getString("FROM_DATE"));
            setAttribute("TO_DATE",rsResultSet.getString("TO_DATE"));
            setAttribute("PARENT_PARTY_ID",rsResultSet.getString("PARENT_PARTY_ID"));
            setAttribute("MAIN_ACCOUNT_CODE",rsResultSet.getString("MAIN_ACCOUNT_CODE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getString("HIERARCHY_ID"));
            setAttribute("CANCELLED",rsResultSet.getString("CANCELLED"));
            setAttribute("CREATED_BY",rsResultSet.getString("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getString("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED",rsResultSet.getString("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getString("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CHANGED",rsResultSet.getString("CHANGED"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            
            
            
            colChildPartyGrp.clear(); //Clear existing data
            
            String DocNo=(String)getAttribute("DOC_NO").getObj();
            
            
            rstDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                rsDetail=rstDetail.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsDetail=rstDetail.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_DETAIL WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' ORDER BY SR_NO");
            }
            rsDetail.first();
            int DetailCounter=0;
            while(!rsDetail.isAfterLast()&&rsDetail.getRow()>0) {
                DetailCounter=DetailCounter+1;
                clsPolicyPartyGroupingChild ObjChild=new clsPolicyPartyGroupingChild();
                ObjChild.setAttribute("COMPANY_ID",rsDetail.getInt("COMPANY_ID"));
                ObjChild.setAttribute("DOC_NO",rsDetail.getString("DOC_NO"));
                ObjChild.setAttribute("SR_NO",rsDetail.getInt("SR_NO"));
                ObjChild.setAttribute("CHILD_PARTY_ID",rsDetail.getString("CHILD_PARTY_ID"));
                ObjChild.setAttribute("CREATED_BY",rsDetail.getString("CREATED_BY"));
                ObjChild.setAttribute("CREATED_DATE",rsDetail.getString("CREATED_DATE"));
                ObjChild.setAttribute("MODIFIED_BY",rsDetail.getString("MODIFIED_BY"));
                ObjChild.setAttribute("MODIFIED_DATE",rsDetail.getString("MODIFIED_DATE"));
                ObjChild.setAttribute("CHANGED",rsDetail.getString("CHANGED"));
                ObjChild.setAttribute("CHANGED_DATE",rsDetail.getString("CHANGED_DATE"));
                
                rsDetail.next();
                
                colChildPartyGrp.put(Integer.toString(DetailCounter),ObjChild);
            }
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        int ModuleID=clsPolicyPartyGrouping.ModuleId;
        
        try {
            if(HistoryView) {
                return false;
            }
            
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_POLICY_PARTY_GROUPING_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+(ModuleID)+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_SAL_POLICY_PARTY_GROUPING_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"' ");
            
            while(rsTmp.next()) {
                clsPolicyPartyGrouping ObjMIR=new clsPolicyPartyGrouping();
                
                ObjMIR.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjMIR.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjMIR.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjMIR.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjMIR.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjMIR.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjMIR.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjMIR);
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
    
    
    
    
    
}
