/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Stores;

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
 
public class clsGPR {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap colGPRItems=new HashMap();
    
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
    public clsGPR() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_REQ_NO",new Variant(""));
        props.put("GATEPASS_REQ_DATE",new Variant(""));
        props.put("SOURCE_DEPT_ID",new Variant(0));
        props.put("DEST_DEPT_ID",new Variant(0));
        props.put("GATEPASS_TYPE",new Variant(""));
        props.put("PURPOSE",new Variant(""));
        props.put("ADD1",new Variant(""));
        props.put("ADD2",new Variant(""));
        props.put("ADD3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("REQUIRED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CANCELLED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        props.put("USER_ID",new Variant(0));
        props.put("PARTY_NAME",new Variant(""));
        props.put("EXP_RETURN_DATE",new Variant(""));
        props.put("DESPATCH_MODE",new Variant(""));
        props.put("FREIGHT_PAID_BY",new Variant(""));
        props.put("REVISION_NO",new Variant(0));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        HistoryView=false;
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GATEPASS_REQ_NO");
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
  
    public void Close()
    {
      try
      {
        //Conn.close();
        Stmt.close();
        rsResultSet.close();
          
      }
      catch(Exception e)
      {
          
      }
        
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsGPR ObjGPR=new clsGPR();
                
                ObjGPR.setAttribute("GATEPASS_REQ_NO",rsTmp.getString("GATEPASS_REQ_NO"));
                ObjGPR.setAttribute("GATEPASS_REQ_DATE",rsTmp.getString("GATEPASS_REQ_DATE"));
                ObjGPR.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjGPR.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjGPR.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjGPR.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjGPR.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjGPR);
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
        Statement stmtDetail,stLot,stHistory,stHDetail,stHLot,stTmp;
        ResultSet rsDetail,rsLot,rsHistory,rsHDetail,rsHLot,rsTmp;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("GATEPASS_REQ_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER_H WHERE GATEPASS_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_DETAIL_H WHERE GATEPASS_REQ_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT_H WHERE GATEPASS_REQ_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE GATEPASS_REQ_NO='1'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("GATEPASS_REQ_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,4, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
            rsResultSet.updateString("GATEPASS_REQ_DATE",(String)getAttribute("GATEPASS_REQ_DATE").getObj());
            rsResultSet.updateInt("SOURCE_DEPT_ID",(int)getAttribute("SOURCE_DEPT_ID").getVal());
            rsResultSet.updateInt("DEST_DEPT_ID",(int)getAttribute("DEST_DEPT_ID").getVal());
            rsResultSet.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsResultSet.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsResultSet.updateString("FREIGHT_PAID_BY",(String)getAttribute("FREIGHT_PAID_BY").getObj());
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
            rsHistory.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
            rsHistory.updateString("GATEPASS_REQ_DATE",(String)getAttribute("GATEPASS_REQ_DATE").getObj());
            rsHistory.updateInt("SOURCE_DEPT_ID",(int)getAttribute("SOURCE_DEPT_ID").getVal());
            rsHistory.updateInt("DEST_DEPT_ID",(int)getAttribute("DEST_DEPT_ID").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsHistory.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsHistory.updateString("FREIGHT_PAID_BY",(String)getAttribute("FREIGHT_PAID_BY").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            //----------------------------------------------------------------//
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=4; //Gatepass Requisition
            ObjFlow.DocNo=(String)getAttribute("GATEPASS_REQ_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GATEPASS_REQ_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="GATEPASS_REQ_NO";
            
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
            
            
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_DETAIL WHERE GATEPASS_REQ_NO='1'");
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE GATEPASS_REQ_NO='1'");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colGPRItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsGPRItem ObjItem=(clsGPRItem) colGPRItems.get(Integer.toString(cnt));
                
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsDetail.updateDouble("BAL_QTY",ObjItem.getAttribute("QTY").getVal());
                rsDetail.updateDouble("GATEPASS_QTY",0);
                rsDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsDetail.updateString("PACKING",(String)ObjItem.getAttribute("PACKING").getObj());
                rsDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateString("DEC_ID",(String)ObjItem.getAttribute("DEC_ID").getObj());
                rsDetail.updateInt("DEC_SR_NO",(int)ObjItem.getAttribute("DEC_SR_NO").getVal());
                
                rsDetail.insertRow();
                
                //===== History Tables ==========//
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
                rsHDetail.updateInt("SR_NO",cnt);
                rsHDetail.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("BAL_QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("GATEPASS_QTY",0);
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("PACKING",(String)ObjItem.getAttribute("PACKING").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("DEC_ID",(String)ObjItem.getAttribute("DEC_ID").getObj());
                rsHDetail.updateInt("DEC_SR_NO",(int)ObjItem.getAttribute("DEC_SR_NO").getVal());
                rsHDetail.insertRow();
                //===================================//
                
                //Insert Lots
                for(int j=1;j<=ObjItem.colLot.size();j++) {
                    clsGPRLot ObjLot=(clsGPRLot) ObjItem.colLot.get(Integer.toString(j));
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsLot.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
                    rsLot.updateInt("GATEPASS_SR_NO",cnt);
                    rsLot.updateInt("SR_NO",j);
                    rsLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",1);
                    rsHLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsHLot.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
                    rsHLot.updateInt("GATEPASS_SR_NO",cnt);
                    rsHLot.updateInt("SR_NO",j);
                    rsHLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsHLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
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
        Statement stmtDetail,stLot,stHistory,stHDetail,stHLot;
        ResultSet rsDetail,rsLot,rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("GATEPASS_REQ_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER_H WHERE GATEPASS_REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_DETAIL_H WHERE GATEPASS_REQ_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT_H WHERE GATEPASS_REQ_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("GATEPASS_REQ_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateString("GATEPASS_REQ_DATE",(String)getAttribute("GATEPASS_REQ_DATE").getObj());
            rsResultSet.updateInt("SOURCE_DEPT_ID",(int)getAttribute("SOURCE_DEPT_ID").getVal());
            rsResultSet.updateInt("DEST_DEPT_ID",(int)getAttribute("DEST_DEPT_ID").getVal());
            rsResultSet.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsResultSet.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsResultSet.updateString("FREIGHT_PAID_BY",(String)getAttribute("FREIGHT_PAID_BY").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_GATEPASS_REQ_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+(String)getAttribute("GATEPASS_REQ_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("GATEPASS_REQ_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
            rsHistory.updateString("GATEPASS_REQ_DATE",(String)getAttribute("GATEPASS_REQ_DATE").getObj());
            rsHistory.updateInt("SOURCE_DEPT_ID",(int)getAttribute("SOURCE_DEPT_ID").getVal());
            rsHistory.updateInt("DEST_DEPT_ID",(int)getAttribute("DEST_DEPT_ID").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("REQUIRED_DATE",(String)getAttribute("REQUIRED_DATE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsHistory.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsHistory.updateString("FREIGHT_PAID_BY",(String)getAttribute("FREIGHT_PAID_BY").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            //----------------------------------------------------------------//
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=4; //Gatepass Requisition
            ObjFlow.DocNo=(String)getAttribute("GATEPASS_REQ_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GATEPASS_REQ_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="GATEPASS_REQ_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_GATEPASS_REQ_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=4 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            
            //Remove old records
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("GATEPASS_REQ_NO").getObj();
            
            String strSQL="DELETE FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"'";
            data.Execute(strSQL);
            
            strSQL="DELETE FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"'";
            data.Execute(strSQL);
            
            //Insert new records
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_DETAIL WHERE GATEPASS_REQ_NO='1'");
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE GATEPASS_REQ_NO='1'");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colGPRItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsGPRItem ObjItem=(clsGPRItem) colGPRItems.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsDetail.updateDouble("BAL_QTY",ObjItem.getAttribute("QTY").getVal());
                rsDetail.updateDouble("GATEPASS_QTY",0);
                rsDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsDetail.updateString("PACKING",(String)ObjItem.getAttribute("PACKING").getObj());
                rsDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.updateString("DEC_ID",(String)ObjItem.getAttribute("DEC_ID").getObj());
                rsDetail.updateInt("DEC_SR_NO",(int)ObjItem.getAttribute("DEC_SR_NO").getVal());
                
                rsDetail.insertRow();
                
                //===== History Tables ==========//
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("GATEPASS_REQ_NO",RevDocNo);
                rsHDetail.updateInt("SR_NO",cnt);
                rsHDetail.updateString("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("BAL_QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("GATEPASS_QTY",0);
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("PACKING",(String)ObjItem.getAttribute("PACKING").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("DEC_ID",(String)ObjItem.getAttribute("DEC_ID").getObj());
                rsHDetail.updateInt("DEC_SR_NO",(int)ObjItem.getAttribute("DEC_SR_NO").getVal());
                rsHDetail.insertRow();
                //===================================//
                
                //Insert Lots
                for(int j=1;j<=ObjItem.colLot.size();j++) {
                    clsGPRLot ObjLot=(clsGPRLot) ObjItem.colLot.get(Integer.toString(j));
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsLot.updateString("GATEPASS_REQ_NO",(String)getAttribute("GATEPASS_REQ_NO").getObj());
                    rsLot.updateInt("GATEPASS_SR_NO",cnt);
                    rsLot.updateInt("SR_NO",j);
                    rsLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",RevNo);
                    rsHLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsHLot.updateString("GATEPASS_REQ_NO",RevDocNo);
                    rsHLot.updateInt("GATEPASS_SR_NO",cnt);
                    rsHLot.updateInt("SR_NO",j);
                    rsHLot.updateString("ITEM_LOT_NO",(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj());
                    rsHLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }
            
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
            String lDocNo=(String)getAttribute("GATEPASS_REQ_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND GATEPASS_REQ_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND GATEPASS_REQ_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID=" + lCompanyID +" AND GATEPASS_REQ_NO='"+lDocNo+"'";
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
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND GATEPASS_REQ_NO='"+pDocNo+"'";
        clsGPR ObjGPR = new clsGPR();
        ObjGPR.Filter(strCondition,pCompanyID);
        return ObjGPR;
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt=null,stLot=null;
        ResultSet rsDetail=null,rsLot=null;
        Statement tmpDetail=null;
        
        HashMap List=new HashMap();
        long Counter=0,Counter2=0,Counter3=0;
        
        try {
            tmpConn=data.getConn();
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            tmpDetail=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER "+pCondition);
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsGPR ObjGPR =new clsGPR();
                ObjGPR.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjGPR.setAttribute("GATEPASS_REQ_NO",rsTmp.getString("GATEPASS_REQ_NO"));
                ObjGPR.setAttribute("GATEPASS_REQ_DATE",rsTmp.getString("GATEPASS_REQ_DATE"));
                ObjGPR.setAttribute("SOURCE_DEPT_ID",rsTmp.getInt("SOURCE_DEPT_ID"));
                ObjGPR.setAttribute("DEST_DEPT_ID",rsTmp.getInt("DEST_DEPT_ID"));
                ObjGPR.setAttribute("GATEPASS_TYPE",rsTmp.getString("GATEPASS_TYPE"));
                ObjGPR.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjGPR.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjGPR.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjGPR.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjGPR.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjGPR.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjGPR.setAttribute("HIERARCHY_ID",rsTmp.getInt("HIERARCHY_ID"));
                ObjGPR.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjGPR.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                ObjGPR.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjGPR.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjGPR.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjGPR.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjGPR.setAttribute("DEC_ID",rsTmp.getString("DEC_ID"));
                ObjGPR.setAttribute("DEC_SR_NO",rsTmp.getInt("DEC_SR_NO"));
                //Now turn of detail records
                ObjGPR.colGPRItems.clear();
                
                int lCompanyID=(int)ObjGPR.getAttribute("COMPANY_ID").getVal();
                String lDocNo=(String)ObjGPR.getAttribute("GATEPASS_REQ_NO").getObj();
                
                rsDetail=tmpDetail.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"'");
                rsDetail.first();
                
                Counter2=0;
                
                while(!rsDetail.isAfterLast()) {
                    Counter2=Counter2+1;
                    
                    clsGPRItem ObjItem=new clsGPRItem();
                    ObjItem.setAttribute("COMPANY_ID",rsDetail.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("GATEPASS_REQ_NO",rsDetail.getString("GATEPASS_REQ_NO"));
                    ObjItem.setAttribute("SR_NO",rsDetail.getInt("SR_NO"));
                    int GPRSrNo=rsDetail.getInt("SR_NO");
                    ObjItem.setAttribute("ITEM_CODE",rsDetail.getString("ITEM_CODE"));
                    ObjItem.setAttribute("QTY",rsDetail.getDouble("QTY"));
                    ObjItem.setAttribute("UNIT",rsDetail.getInt("UNIT"));
                    ObjItem.setAttribute("PACKING",rsDetail.getString("PACKING"));
                    ObjItem.setAttribute("REMARKS",rsDetail.getString("REMARKS"));
                    ObjItem.setAttribute("CREATED_BY",rsDetail.getInt("CREATED_BY"));
                    ObjItem.setAttribute("CREATED_DATE",rsDetail.getString("CREATED_DATE"));
                    ObjItem.setAttribute("MODIFIED_BY",rsDetail.getInt("MODIFIED_BY"));
                    ObjItem.setAttribute("MODIFIED_DATE",rsDetail.getString("MODIFIED_DATE"));
                    
                    //=========== Lot Nos. ==========//
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"' AND GATEPASS_SR_NO="+GPRSrNo);
                    rsLot.first();
                    
                    Counter3=0;
                    while(!rsLot.isAfterLast()&&rsLot.getRow()>0) {
                        Counter3++;
                        clsGPRLot ObjLot=new clsGPRLot();
                        ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                        ObjLot.setAttribute("GATEPASS_REQ_NO",rsLot.getString("GATEPASS_REQ_NO"));
                        ObjLot.setAttribute("GATEPASS_SR_NO",rsLot.getInt("GATEPASS_SR_NO"));
                        ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                        ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                        ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                        
                        ObjItem.colLot.put(Long.toString(Counter3),ObjLot);
                        
                        rsLot.next();
                    }
                    //===============================//
                    
                    ObjGPR.colGPRItems.put(Long.toString(Counter2),ObjItem);
                    rsDetail.next();
                }
                
                List.put(Long.toString(Counter),ObjGPR);
                rsTmp.next();
            }
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
        stLot.close();
        rsDetail.close();
        rsLot.close();
        tmpDetail.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_GATEPASS_REQ_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GATEPASS_REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GATEPASS_REQ_NO";
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
        ResultSet rsTmp,rsLot;
        Statement tmpStmt,stLot;
        int Counter=0,Counter2=0;
        int RevNo=0;
        
        try {
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if(HistoryView)
            {
             setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));   
            }
            else
            {
             setAttribute("REVISION_NO",0);      
            }
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("GATEPASS_REQ_NO",rsResultSet.getString("GATEPASS_REQ_NO"));
            setAttribute("GATEPASS_REQ_DATE",rsResultSet.getString("GATEPASS_REQ_DATE"));
            setAttribute("SOURCE_DEPT_ID",rsResultSet.getInt("SOURCE_DEPT_ID"));
            setAttribute("DEST_DEPT_ID",rsResultSet.getInt("DEST_DEPT_ID"));
            setAttribute("GATEPASS_TYPE",rsResultSet.getString("GATEPASS_TYPE"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("REQUIRED_DATE",rsResultSet.getString("REQUIRED_DATE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("ADD1",rsResultSet.getString("ADD1"));
            setAttribute("ADD2",rsResultSet.getString("ADD2"));
            setAttribute("ADD3",rsResultSet.getString("ADD3"));
            setAttribute("CITY",rsResultSet.getString("CITY"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            setAttribute("USER_ID",rsResultSet.getInt("USER_ID"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("EXP_RETURN_DATE",rsResultSet.getString("EXP_RETURN_DATE"));
            setAttribute("DESPATCH_MODE",rsResultSet.getString("DESPATCH_MODE"));
            setAttribute("FREIGHT_PAID_BY",rsResultSet.getString("FREIGHT_PAID_BY"));
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
            }
            
            //Now turn of detail records
            colGPRItems.clear();
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("GATEPASS_REQ_NO").getObj();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_DETAIL_H WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                
                clsGPRItem ObjItem=new clsGPRItem();
                
                int GPRSrNo=rsTmp.getInt("SR_NO");
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("GATEPASS_REQ_NO",rsTmp.getString("GATEPASS_REQ_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_CODE",rsTmp.getString("ITEM_CODE"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                ObjItem.setAttribute("BAL_QTY",rsTmp.getDouble("BAL_QTY"));
                ObjItem.setAttribute("GATEPASS_QTY",rsTmp.getDouble("GATEPASS_QTY"));
                ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                ObjItem.setAttribute("PACKING",rsTmp.getString("PACKING"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjItem.setAttribute("DEC_ID",rsTmp.getString("DEC_ID"));
                ObjItem.setAttribute("DEC_SR_NO",rsTmp.getInt("DEC_SR_NO"));
                
                                
                
                //Get lot nos.
                if(HistoryView) {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT_H WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"' AND GATEPASS_SR_NO="+GPRSrNo+" AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
                }
                else {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID="+lCompanyID+" AND GATEPASS_REQ_NO='"+lDocNo+"' AND GATEPASS_SR_NO="+GPRSrNo+" ORDER BY SR_NO");
                }
                rsLot.first();
                
                Counter2=0;
                
                while(!rsLot.isAfterLast()&&rsLot.getRow()>0) {
                    Counter2++;
                    
                    clsGPRLot ObjLot=new clsGPRLot();
                    ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("GATEPASS_REQ_NO",rsLot.getString("GATEPASS_REQ_NO"));
                    ObjLot.setAttribute("GATEPASS_SR_NO",rsLot.getInt("GATEPASS_SR_NO"));
                    ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                    ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                    
                    ObjItem.colLot.put(Integer.toString(Counter2), ObjLot);
                    rsLot.next();
                }
                
                colGPRItems.put(Long.toString(Counter),ObjItem);
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
            //Deletion in History Records Not Allowed
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_REQ_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=4 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            //Updation in History Records Not Allowed
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_REQ_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=4 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate)
            {
            strSQL="SELECT D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO,D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_DATE,RECEIVED_DATE,D_INV_GATEPASS_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_GATEPASS_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=4 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";    
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate)
            {
            strSQL="SELECT D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO,D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_DATE,RECEIVED_DATE,D_INV_GATEPASS_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_GATEPASS_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=4 ORDER BY D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_DATE";    
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo)
            {
            strSQL="SELECT D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO,D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_DATE,RECEIVED_DATE,D_INV_GATEPASS_REQ_HEADER.DEST_DEPT_ID AS DEPT_ID FROM D_INV_GATEPASS_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=4 ORDER BY D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO";    
            }
            
            //strSQL="SELECT D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO,D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_DATE FROM D_INV_GATEPASS_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=4";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("GATEPASS_REQ_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsGPR ObjDoc=new clsGPR();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("GATEPASS_REQ_NO",rsTmp.getString("GATEPASS_REQ_NO"));
                ObjDoc.setAttribute("GATEPASS_REQ_DATE",rsTmp.getString("GATEPASS_REQ_DATE"));
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
    
    
    public static HashMap getGPRItemList(int pCompanyID,String pGprNo,boolean pAllData) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        Statement stLot=null;
        ResultSet rsLot=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        int Counter1 = 0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSql;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            if(pAllData) {
                strSql = "SELECT * FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+pGprNo.trim()+"' AND GATEPASS_REQ_NO IN (SELECT GATEPASS_REQ_NO FROM D_INV_GATEPASS_REQ_HEADER WHERE GATEPASS_REQ_NO='"+pGprNo.trim()+"' AND APPROVED=1) ";
            }
            else{
                strSql = "SELECT * FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+pGprNo.trim()+"' AND GATEPASS_REQ_NO IN (SELECT GATEPASS_REQ_NO FROM D_INV_GATEPASS_REQ_HEADER WHERE GATEPASS_REQ_NO='"+pGprNo.trim()+"' AND APPROVED=1) ";
            }
            
            rsTmp2=tmpStmt.executeQuery(strSql);
            rsTmp2.first();
            
            Counter1=0;
            while(! rsTmp2.isAfterLast()) {
                Counter1++;
                clsGPRItem ObjMRItems=new clsGPRItem();
                
                ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                ObjMRItems.setAttribute("GATEPASS_REQ_NO",rsTmp2.getString("GATEPASS_REQ_NO"));
                ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjMRItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                ObjMRItems.setAttribute("ITEM_EXTRA_DESC",rsTmp2.getString("ITEM_EXTRA_DESC"));
                ObjMRItems.setAttribute("PACKING",rsTmp2.getString("PACKING"));
                ObjMRItems.setAttribute("QTY",rsTmp2.getFloat("QTY"));
                ObjMRItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                ObjMRItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                ObjMRItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                
                ObjMRItems.setAttribute("DEC_ID",rsTmp2.getString("DEC_ID"));
                ObjMRItems.setAttribute("DEC_SR_NO",rsTmp2.getInt("DEC_SR_NO"));
                
                
                //Now Fetch Lots.
                int GprSrno=rsTmp2.getInt("SR_NO");
                
                stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsLot=stLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_NO='"+pGprNo+"' AND GATEPASS_SR_NO="+GprSrno);
                rsLot.first();
                
                //Clear existing data
                ObjMRItems.colLot.clear();
                
                int Counter2 = 0;
                if(rsLot.getRow()>0) {
                    Counter2=0;
                    while(!rsLot.isAfterLast()) {
                        Counter2++;
                        clsGPRLot ObjLot=new clsGPRLot();
                        
                        ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                        ObjLot.setAttribute("GATEPASS_REQ_NO",rsLot.getString("GATEPASS_REQ_NO"));
                        ObjLot.setAttribute("GATEPASS_SR_NO",rsLot.getInt("GATEPASS_SR_NO"));
                        ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                        ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                        ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                        
                        ObjMRItems.colLot.put(Integer.toString(Counter2),ObjLot);
                        rsLot.next();
                    }
                }
                
                List.put(Integer.toString(Counter1),ObjMRItems);
                rsTmp2.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
        stLot.close();
        rsLot.close();
        
        rsTmp2.close();
        tmpStmt2.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public static HashMap getGPRItemList(int pCompanyID,String pGprNo,boolean pAllData,String Type) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        Statement stLot=null;
        ResultSet rsLot=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        int Counter1 = 0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSql;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            if(pAllData) {
                strSql = "SELECT * FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ";
            }
            else{
                strSql = "SELECT D_INV_GATEPASS_REQ_DETAIL.* FROM D_INV_GATEPASS_REQ_HEADER,D_INV_GATEPASS_REQ_DETAIL WHERE D_INV_GATEPASS_REQ_HEADER.COMPANY_ID=D_INV_GATEPASS_REQ_DETAIL.COMPANY_ID AND D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO=D_INV_GATEPASS_REQ_DETAIL.GATEPASS_REQ_NO AND D_INV_GATEPASS_REQ_HEADER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_INV_GATEPASS_REQ_HEADER.GATEPASS_REQ_NO='"+pGprNo.trim()+"' AND D_INV_GATEPASS_REQ_HEADER.GATEPASS_TYPE='"+Type+"' AND D_INV_GATEPASS_REQ_HEADER.APPROVED=1 ";
            }
            
            rsTmp2=tmpStmt.executeQuery(strSql);
            rsTmp2.first();
            
            Counter1=0;
            while(! rsTmp2.isAfterLast()) {
                Counter1++;
                clsGPRItem ObjMRItems=new clsGPRItem();
                
                ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                ObjMRItems.setAttribute("GATEPASS_REQ_NO",rsTmp2.getString("GATEPASS_REQ_NO"));
                ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjMRItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                ObjMRItems.setAttribute("ITEM_EXTRA_DESC",rsTmp2.getString("ITEM_EXTRA_DESC"));
                ObjMRItems.setAttribute("PACKING",rsTmp2.getString("PACKING"));
                ObjMRItems.setAttribute("QTY",rsTmp2.getFloat("QTY"));
                ObjMRItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                ObjMRItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                ObjMRItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                ObjMRItems.setAttribute("DEC_ID",rsTmp2.getString("DEC_ID"));
                ObjMRItems.setAttribute("DEC_SR_NO",rsTmp2.getInt("DEC_SR_NO"));
                
                //Now Fetch Lots.
                int GprSrno=rsTmp2.getInt("SR_NO");
                
                stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsLot=stLot.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_NO='"+pGprNo+"' AND GATEPASS_SR_NO="+GprSrno);
                rsLot.first();
                
                //Clear existing data
                ObjMRItems.colLot.clear();
                
                int Counter2 = 0;
                if(rsLot.getRow()>0) {
                    Counter2=0;
                    while(!rsLot.isAfterLast()) {
                        Counter2++;
                        clsGPRLot ObjLot=new clsGPRLot();
                        
                        ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                        ObjLot.setAttribute("GATEPASS_REQ_NO",rsLot.getString("GATEPASS_REQ_NO"));
                        ObjLot.setAttribute("GATEPASS_SR_NO",rsLot.getInt("GATEPASS_SR_NO"));
                        ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                        ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                        ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                        
                        ObjMRItems.colLot.put(Integer.toString(Counter2),ObjLot);
                        rsLot.next();
                    }
                }
                
                List.put(Integer.toString(Counter1),ObjMRItems);
                rsTmp2.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
        stLot.close();
        rsLot.close();
        
        rsTmp2.close();
        tmpStmt2.close();
            
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        return List;
    }
    
    public static HashMap getGPRLotList(int pCompanyID,String pGprNo,int pSrno,boolean pAllData) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        int Counter1 = 0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSql;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(pAllData) {
                strSql = "SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY GATEPASS_REQ_NO";
            }
            else{
                strSql = "SELECT * FROM D_INV_GATEPASS_REQ_LOT WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+pGprNo.trim()+"' AND GATEPASS_SR_NO="+pSrno+" ORDER BY GATEPASS_SR_NO";
            }
            
            rsTmp2=tmpStmt.executeQuery(strSql);
            rsTmp2.first();
            
            Counter1=0;
            while(! rsTmp2.isAfterLast()) {
                Counter1++;
                clsGPRLot ObjMRItems=new clsGPRLot();
                
                ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                ObjMRItems.setAttribute("GATEPASS_REQ_NO",rsTmp2.getString("GATEPASS_REQ_NO"));
                ObjMRItems.setAttribute("GATEPASS_SR_NO",rsTmp2.getString("GATEPASS_SR_NO"));
                ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjMRItems.setAttribute("ITEM_LOT_NO",rsTmp2.getString("ITEM_LOT_NO"));
                ObjMRItems.setAttribute("LOT_QTY",rsTmp2.getFloat("LOT_QTY"));
                
                List.put(Integer.toString(Counter1),ObjMRItems);
                rsTmp2.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
        
        rsTmp2.close();
        tmpStmt2.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }

    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT GATEPASS_REQ_NO,APPROVED,CANCELLED FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    
                    if(rsTmp.getBoolean("CANCELLED"))
                    {
                     strMessage="Document is cancelled";   
                    }
                    else
                    {
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
            rsTmp=data.getResult("SELECT GATEPASS_REQ_NO FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_NO='"+pDocNo+"' AND CANCELLED=0");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=4;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_GATEPASS_REQ_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_REQ_NO='"+pDocNo+"'");
                
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
