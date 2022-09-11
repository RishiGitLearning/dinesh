/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP.Finance;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Stores.*;
import EITLERP.Sales.*;
import EITLERP.Finance.ReportsUI.clsPartyOutstandingItems;

/**
 *
 * @author  Nitin Pithva
 * @version
 */

public class clsMemorandumJV {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colVoucherItems=new HashMap();
    public HashMap colVoucherItemsEx=new HashMap();
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=195;
    public boolean DoNotValidateAccounts=false;
    public boolean IsInternalPosting=false;
    public boolean DepositTransfer=false;
    private int BlockCounter=0;
    
    public boolean UseSpecificVoucherNo=false;
    public String SpecificVoucherNo="";
    public boolean ChangeVoucherNo=false;
    public String OldVoucherNo="";
    
       
    public clsMemorandumJV objPayment;

    private String TVoucherNo = "";
    private String EmployeeNo = "";
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return (new Variant(""));
        }
        else {
            return (Variant) props.get(PropName);
        }
    }
    
    public void setAttribute(String PropName,String Value) {
        props.put(PropName,new Variant(Value));
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
    public clsMemorandumJV() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("VOUCHER_NO",new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        props.put("BOOK_CODE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED", new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        //Create a new object for MR Item collection
        colVoucherItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("LEGACY_NO",new Variant(""));
        props.put("LEGACY_DATE",new Variant(""));
        
        
        IsInternalPosting=false;
    }
    
    /*<b>Load Data</b>\nThis method loads data from database to Business Object*/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'  ORDER BY VOUCHER_DATE,VOUCHER_NO");
            System.out.println("SELECT * FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'  ORDER BY VOUCHER_NO");
            //===this comment for new finacial year start==========
            //rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_TYPE="+VoucherType+" ORDER BY VOUCHER_NO");
            //==================================================
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
        int RefModuleID = 0;
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf(EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            } else {
                LastError="Voucher date is not within financial year.";
                //return false;
            }
            //=====================================================//
            
            
//            //========= Check Document no. duplication ===================//
//            if(UseSpecificVoucherNo) {
//                String DocNo=SpecificVoucherNo;
//                
//                if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL)) {
//                    LastError="Voucher no. "+DocNo+" already exist. Please specify other voucher no.";
//                    return false;
//                }
//            }
            //===========================================================//
            
            
            if(!Validate()) {
            return false;
        }
            
            
            //data.SetAutoCommit(false);
            //Conn.setAutoCommit(false);
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_MEM_HEADER_H WHERE VOUCHER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_MEM_DETAIL_H WHERE VOUCHER_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
             
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsResultSet.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsResultSet.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID );
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsHistory.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsHistory.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            
            rsHistory.insertRow();
            
            
            ResultSet rsTmp,rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_MEM_DETAIL WHERE VOUCHER_NO='1'");
            
            int CreditCount=0,DebitCount=0;
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            
            //Now Insert records into detail table
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsMemorandumJVItem objItem=(clsMemorandumJVItem) colVoucherItems.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CreditCount++;
                } else {
                    DebitCount++;
                }
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",CompanyID);
                rsTmp.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsTmp.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsTmp.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsTmp.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                //rsTmp.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
                rsTmp.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                rsTmp.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",CompanyID);
                rsHDetail.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsHDetail.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsHDetail.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                //rsHDetail.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                
                rsHDetail.insertRow();
                
                
            }
            
                 
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsMemorandumJV.ModuleID;
            ObjFlow.DocNo=getAttribute("VOUCHER_NO").getString();
            //ObjFlow.DocDate=getAttribute("VOUCHER_DATE").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_MEM_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="VOUCHER_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            
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
            ObjFlow.UseSpecifiedURL=false; //Turn it off due to static nature
            //--------- Approval Flow Update complete -----------
            
           
            
            //MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                e.printStackTrace();
            } catch(Exception c) {
            }
            LastError= e.getMessage();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        Statement stHistory,stHDetail,stHeader;
        ResultSet rsHistory,rsHDetail,rsHeader,rsTmp;
        boolean Validate=true;
        int OldHierarchy=0;
        
        try {
            
             
            if(!Validate()) {
            return false;
        }
            
            //Check that Voucher no. has been changed
            rsResultSet.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsResultSet.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsResultSet.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            rsResultSet.updateRow();
         
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            String theDocNo=getAttribute("VOUCHER_NO").getString();
            
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;
            
            
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf( EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Withing the year
            } else {
                LastError="Voucher date is not within financial year.";
                //return false;
            }
            //=====================================================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_MEM_HEADER_H WHERE VOUCHER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_MEM_DETAIL_H WHERE VOUCHER_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_MEM_HEADER_H WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+getAttribute("VOUCHER_NO").getString()+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("VOUCHER_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsHistory.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString()); //EITLERPGLOBAL.gLoginID
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateString("LEGACY_NO",getAttribute("LEGACY_NO").getString());
            rsHistory.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LEGACY_DATE").getString()));
            
            rsHistory.insertRow();
            
            data.Execute("DELETE FROM D_FIN_MEM_DETAIL WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            
            
            ResultSet rsTmpEx;
            Statement tmpStmt,tmpStmtEx;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_MEM_DETAIL WHERE VOUCHER_NO='1'");
            
            int CreditCount=0,DebitCount=0;
            
            int Counter=0;
            
            //Now Insert records into detail table
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsMemorandumJVItem objItem=(clsMemorandumJVItem) colVoucherItems.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CreditCount++;
                }
                else {
                    DebitCount++;
                }
                
                Counter++;
                
                rsTmp.moveToInsertRow();
                rsTmp.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsTmp.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsTmp.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsTmp.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                //rsTmp.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsTmp.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateInt("CANCELLED",0);
                
                rsTmp.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                
                rsTmp.insertRow();
                
                Counter++;
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
                rsHDetail.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim());
                rsHDetail.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                //rsHDetail.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                
                rsHDetail.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                
                rsHDetail.insertRow();
                
                
            }
            
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
        
                
                //======== Update the Approval Flow =========
                ObjFlow.DocNo=getAttribute("VOUCHER_NO").getString();
                //ObjFlow.DocDate=getAttribute("VOUCHER_DATE").getString();
                  ObjFlow.ModuleID=clsMemorandumJV.ModuleID;
                ObjFlow.From=getAttribute("FROM").getInt();
                ObjFlow.To=getAttribute("TO").getInt();
                ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
                ObjFlow.TableName="D_FIN_MEM_HEADER";
                ObjFlow.IsCreator=false;
                ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
                ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
                ObjFlow.FieldName="VOUCHER_NO";
                ObjFlow.UseSpecifiedURL=true;
                ObjFlow.SpecificURL=FinanceGlobal.FinURL;
                //==== Handling Rejected Documents ==========//
                
                
                if(AStatus.equals("R")) {
                    ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                    ObjFlow.ExplicitSendTo=true;
                }
                //==========================================//
                
                
                //==== Handling Rejected Documents ==========//
                boolean IsRejected=getAttribute("REJECTED").getBool();
                
                if(IsRejected) {
                    //Remove the Rejected Flag First
                    data.Execute("UPDATE D_FIN_MEM_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND VOUCHER_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
                    //Remove Old Records from D_COM_DOC_DATA
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                    
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
                } else {
                    if(!ObjFlow.UpdateFlow()) {
                        LastError=ObjFlow.LastError;
                    }
                }
                ObjFlow.UseSpecifiedURL=false;
                //=========== Approval Flow Update complete ===============//
            
           
            setData();
            return true;
        }

        catch(Exception e) {
            
                e.printStackTrace();
           
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND TYPE='C' AND STATUS='W'";
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
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID,int VoucherModuleID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND VOUCHER_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+VoucherModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND STATUS='W'";
                rsTmp=data.getResult(strSQL);
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
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String lDocNo=getAttribute("VOUCHER_NO").getString();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(CompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_FIN_MEM_HEADER WHERE COMPANY_ID=" + CompanyID +" AND VOUCHER_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                strQry = "DELETE FROM D_FIN_MEM_DETAIL WHERE COMPANY_ID=" + CompanyID +" AND VOUCHER_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                
                LoadData(CompanyID);
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
    
    
    public Object getObject(int CompanyID, String VoucherNo) {
        String strCondition = " WHERE COMPANY_ID=" + CompanyID + " AND VOUCHER_NO='" + VoucherNo + "'" ;
        clsMemorandumJV objVoucher = new clsMemorandumJV();
        objVoucher.Filter(strCondition,CompanyID);
        return objVoucher;
    }
    
    
    public static int getVoucherCompanyID(String VoucherNo) {
        return data.getIntValueFromDB("SELECT COMPANY_ID FROM D_FIN_MEM_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_MEM_HEADER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            System.out.println(strSql);
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_MEM_HEADER A,D_FIN_MEM_DETAIL B WHERE A.COMPANY_ID="+CompanyID+" AND A.VOUCHER_NO = B.VOUCHER_NO AND VOUCHER_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND VOUCHER_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'  ORDER BY VOUCHER_NO";
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


    
    
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order, int VoucherModuleID,int FinYearFrom) {
        Connection tmpConn;
        ResultSet rsTmp3=null;
        Statement tmpStmt3=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            tmpStmt3=tmpConn.createStatement();
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FIN_MEM_HEADER.VOUCHER_NO,FINANCE.D_FIN_MEM_HEADER.VOUCHER_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_MEM_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_MEM_HEADER.VOUCHER_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_MEM_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_MEM_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+VoucherModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_MEM_HEADER.VOUCHER_NO,FINANCE.D_FIN_MEM_HEADER.VOUCHER_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_MEM_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_MEM_HEADER.VOUCHER_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_MEM_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_MEM_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+VoucherModuleID+" ORDER BY D_FIN_MEM_HEADER.VOUCHER_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_MEM_HEADER.VOUCHER_NO,FINANCE.D_FIN_MEM_HEADER.VOUCHER_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FIN_MEM_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_MEM_HEADER.VOUCHER_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_MEM_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_MEM_HEADER.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+VoucherModuleID+" ORDER BY D_FIN_MEM_HEADER.VOUCHER_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("VOUCHER_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsMemorandumJV ObjDoc=new clsMemorandumJV();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("VOUCHER_NO",rsTmp3.getString("VOUCHER_NO"));
                    ObjDoc.setAttribute("VOUCHER_DATE",rsTmp3.getString("VOUCHER_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    
                    String MainCode =data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_MEM_DETAIL WHERE VOUCHER_NO='"+rsTmp3.getString("VOUCHER_NO")+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID,FinanceGlobal.FinURL);
                    String PartyName=clsAccount.getAccountName(MainCode,"");
                    
                    //String PartyName=data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'",FinanceGlobal.FinURL);
                    
                   
                    ObjDoc.setAttribute("PARTY_NAME",PartyName);
                    
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
    
    
    
    public boolean ShowHistory(int CompanyID,String DocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_MEM_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'");
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
    
    
    
    public static HashMap getHistoryList(int CompanyID,String DocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_MEM_HEADER_H WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsMemorandumJV objVoucher=new clsMemorandumJV();
                    
                    objVoucher.setAttribute("VOUCHER_NO",rsTmp.getString("VOUCHER_NO"));
                    objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("VOUCHER_DATE")));
                    objVoucher.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                    objVoucher.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                    objVoucher.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                    objVoucher.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE")));
                    objVoucher.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                    
                    List.put(Integer.toString(List.size()+1),objVoucher);
                    
                    rsTmp.next();
                }
                
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
    
    
    
    public static String getDocStatus(int CompanyID,String DocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            rsTmp=data.getResult("SELECT VOUCHER_NO,APPROVED,CANCELLED FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
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
    
    public static boolean CanCancel(int CompanyID,String DocNo,int VoucherModuleID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT VOUCHER_NO FROM FINANCE.D_FIN_MEM_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"' AND APPROVED=0 AND CANCELLED=0",FinanceGlobal.FinURL);
           rsTmp.first();
           if(rsTmp.getRow()>0) {
                canCancel=true;
            }else{
                canCancel=false;
            }
           
        } catch(Exception e) {
            e.printStackTrace();
        }
        return canCancel;
    }
    
    public static boolean CancelDoc(int CompanyID,String DocNo,int VoucherModuleID) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo,VoucherModuleID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_MEM_HEADER WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+(VoucherModuleID));
                
                
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_FIN_MEM_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_MEM_DETAIL SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FIN_MEM_DETAIL_EX SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND VOUCHER_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    
    public boolean Validate() {
        try {
            
            if(getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                if(getAttribute("BOOK_CODE").getString().equals("99")) {
                    LastError="Dummy Book code 99 is not allowed in Final Approval action. Please enter proper book code and final approve";
                    return false;
                }
            }
            
            if(getAttribute("BOOK_CODE").getString().equals("")) {
                LastError="Please enter book code";
                return false;
            }
            
            double CrTotal=0;
            double DrTotal=0;
            
            for(int i=1;i<=colVoucherItems.size();i++) {
                clsMemorandumJVItem objItem=(clsMemorandumJVItem)colVoucherItems.get(Integer.toString(i));
                
                if(objItem.getAttribute("EFFECT").getString().equals("C")) {
                    CrTotal=EITLERPGLOBAL.round(CrTotal+EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2),2);
                }
                else {
                    DrTotal=EITLERPGLOBAL.round(DrTotal+EITLERPGLOBAL.round(objItem.getAttribute("AMOUNT").getDouble(),2),2);
                }
                
                String MainAccountCode=objItem.getAttribute("MAIN_ACCOUNT_CODE").getString().trim();
                 if(MainAccountCode.endsWith("000")) {
                    LastError = "This Main Account Code ("+MainAccountCode+") is not allowed to enter manually. Please Varify";
                    return false;
                }
                
                if(getAttribute("APPROVAL_STATUS").getString().equals("F")) {
                    DoNotValidateAccounts=false;
                }
                
                if(IsInternalPosting) {
                    DoNotValidateAccounts=true;
                }
               
            }
            
            if(EITLERPGLOBAL.round(CrTotal,2)!=EITLERPGLOBAL.round(DrTotal,2)) {
                if(DoNotValidateAccounts) {
                    
                }
                else {
                    LastError="Credit and Debit total doesn't match. Please verify the entry";
                    return false;
                }
            }
            
            
            //*** ===== CHECK THE EXCESS PAYMENT ==== ***//
            
            
            
            
            CrTotal=0;DrTotal=0;
            colVoucherItemsEx.clear();
            
            String CurrentEffect="",PreviousEffect="";
            
            HashMap sEntries=new HashMap();
          
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
        
    }
    

  
   
    
    private boolean isFirstApprovalAction(String VoucherNo) {
        try {
            if(data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_MEM_HEADER_H WHERE VOUCHER_NO='"+VoucherNo+"' AND APPROVAL_STATUS='A' ",FinanceGlobal.FinURL)) {
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
    
  
       public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        
        try {
            
            
            //General Retrieval of the Voucher
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            OldVoucherNo=UtilFunctions.getString(rsResultSet,"VOUCHER_NO","");
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00")));
            setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            setAttribute("LEGACY_NO",UtilFunctions.getString(rsResultSet,"LEGACY_NO",""));
            setAttribute("LINK_NO",UtilFunctions.getString(rsResultSet,"LINK_NO",""));
            setAttribute("LEGACY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"LEGACY_DATE","0000-00-00")));
            
            colVoucherItems.clear();
            
            int CompanyID=getAttribute("COMPANY_ID").getInt();
            String VoucherNo=getAttribute("VOUCHER_NO").getString();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_MEM_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND VOUCHER_NO='" + VoucherNo + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            } else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FIN_MEM_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND VOUCHER_NO='" + VoucherNo + "' ORDER BY SR_NO");
            }
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsMemorandumJVItem objItem = new clsMemorandumJVItem();
                
                objItem.setAttribute("COMPANY_ID",UtilFunctions.getInt(rsTmp,"COMPANY_ID", 0));
                objItem.setAttribute("VOUCHER_NO",UtilFunctions.getString(rsTmp,"VOUCHER_NO",""));
                objItem.setAttribute("SR_NO",UtilFunctions.getInt(rsTmp,"SR_NO",0));
                objItem.setAttribute("EFFECT",UtilFunctions.getString(rsTmp,"EFFECT",""));
                objItem.setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsTmp,"MAIN_ACCOUNT_CODE","").trim());
                objItem.setAttribute("AMOUNT",UtilFunctions.getDouble(rsTmp,"AMOUNT",0));
                objItem.setAttribute("REMARKS",UtilFunctions.getString(rsTmp,"REMARKS",""));
                //objItem.setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsTmp,"VOUCHER_DATE","0000-00-00"));
                objItem.setAttribute("CREATED_BY",UtilFunctions.getString(rsTmp,"CREATED_BY",""));
                objItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CREATED_DATE","0000-00-00")));
                objItem.setAttribute("MODIFIED_BY",UtilFunctions.getString(rsTmp,"MODIFIED_BY",""));
                objItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"MODIFIED_DATE","0000-00-00")));
                objItem.setAttribute("CHANGED",true);
                objItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"CHANGED_DATE","0000-00-00")));
                objItem.setAttribute("CANCELLED",0);
                objItem.setAttribute("LINK_NO",UtilFunctions.getString(rsTmp,"LINK_NO",""));
                
                colVoucherItems.put(Long.toString(Counter),objItem);
                rsTmp.next();
            }
            
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
       
       
       
}  
