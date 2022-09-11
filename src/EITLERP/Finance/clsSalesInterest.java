/*
 * clsSalesInterest.java
 *
 * Created on August 18, 2008, 3:37 PM
 */

package EITLERP.Finance;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;

/**
 *
 * @author  MRUGESH THAKER
 * @version
 */
public class clsSalesInterest {
    
    public String LastError = "";
    private ResultSet rsInterest;
    private Connection Conn;
    private Statement Stmt;
    public boolean Ready = false;
    
    public HashMap colLineItems;
    private HashMap props;
    
    //History Related properties
    private boolean HistoryView=false;
    public static int ModuleID=143;
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
    
    /** Creates new clsInquiry */
    public clsSalesInterest() {
        props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("EFFECTIVE_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("FFNO",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",  new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED", new Variant(false));
        
        // -- Approval Specific Fields --
        props.put("REVISION_NO",new Variant(0));
        
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        //Create a new object for line items
        colLineItems= new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            HistoryView=false;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSql = "SELECT * FROM D_FD_SALES_INTEREST_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DOC_NO ";
            rsInterest=Stmt.executeQuery(strSql);
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
            Stmt.close();
            rsInterest.close();
        } catch(Exception e) {
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsInterest.first();
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
            if(rsInterest.isAfterLast()||rsInterest.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsInterest.last();
            }
            else {
                rsInterest.next();
                if(rsInterest.isAfterLast()) {
                    rsInterest.last();
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
            if(rsInterest.isFirst()||rsInterest.isBeforeFirst()) {
                rsInterest.first();
            }
            else {
                rsInterest.previous();
                if(rsInterest.isBeforeFirst()) {
                    rsInterest.first();
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
            rsInterest.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert() {
        Statement stTmp,stHistory,stHDetail;
        ResultSet rsTmp,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "";
        String DocNo="";
        try {
            if(!Validate()) {
                return false;
            }
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            String AStatus = getAttribute("APPROVAL_STATUS").getString();
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SALES_INTEREST_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_SALES_INTEREST_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsSalesInterest.ModuleID,getAttribute("FFNO").getInt(),true));
            
            //========= Inserting record into Header =========//
            rsInterest.moveToInsertRow();
            rsInterest.updateLong("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            int CompanyID = getAttribute("COMPANY_ID").getInt();
            rsInterest.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            String theDocNo = getAttribute("DOC_NO").getString();
            rsInterest.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsInterest.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsInterest.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsInterest.updateBoolean("APPROVED",false);
            rsInterest.updateString("APPROVED_DATE","0000-00-00");
            rsInterest.updateBoolean("REJECTED",false);
            rsInterest.updateString("REJECTED_DATE","0000-00-00");
            rsInterest.updateString("REJECTED_REMARKS","");
            rsInterest.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsInterest.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsInterest.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInterest.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsInterest.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInterest.updateBoolean("CHANGED",true);
            rsInterest.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInterest.updateBoolean("CANCELLED",false);
            rsInterest.insertRow();
            //=========End of Inserting record into Header =========//
            
            //========= Inserting record into Header History=========//
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("UPDATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            //=========End of Inserting record into Header history=========//
            
            //========== Inserting records Detail =========//
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_SALES_INTEREST_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND DOC_NO='1'");
            DocNo=getAttribute("DOC_NO").getString();
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsSalesInterestItem ObjItem=(clsSalesInterestItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",DocNo);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsTmp.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("FROM_DATE").getString()));
                rsTmp.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("TO_DATE").getString()));
                rsTmp.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsTmp.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsTmp.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateDouble("GROSS_INTEREST",ObjItem.getAttribute("GROSS_INTEREST").getDouble());
                rsTmp.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsTmp.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                rsTmp.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("DOC_NO",DocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("RECEIPT_NO", ObjItem.getAttribute("RECEIPT_NO").getString());
                rsHDetail.updateString("FROM_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("FROM_DATE").getString()));
                rsHDetail.updateString("TO_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("TO_DATE").getString()));
                rsHDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsHDetail.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsHDetail.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateDouble("GROSS_INTEREST",ObjItem.getAttribute("GROSS_INTEREST").getDouble());
                rsHDetail.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsHDetail.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                rsHDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            //========== Inserting records Detail =========//
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsSalesInterest.ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FD_SALES_INTEREST_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
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
            ObjFlow.UseSpecifiedURL=false;
            //--------- Approval Flow Update complete -----------//
            
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                ResultSet rsItem = data.getResult("SELECT * FROM D_FD_SALES_INTEREST_DETAIL WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
                rsItem.first();
                while(!rsItem.isAfterLast()){
                    CompanyID = rsItem.getInt("COMPANY_ID");
                    String ReceiptNo = rsItem.getString("RECEIPT_NO");
                    if(PostVoucher24(CompanyID,ReceiptNo,theDocNo)) {
                        String nextInterestCalcDate = getNextInterestDate(CompanyID, ReceiptNo);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                    } else {
                        LastError += "\n Error while posting voucher for TR - 24";
                        return false;
                    }
                    rsItem.next();
                }
            }
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Update() {
        Statement stTmp,stHistory,stHDetail;
        ResultSet rsTmp,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "";
        
        boolean Validate=true;
        try {
            
            if(!Validate()) {
                return false;
            }
            //No Primary Keys will be updated & Updating of Header starts
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            //String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_SALES_INTEREST_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_SALES_INTEREST_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsInterest.updateLong("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            int CompanyID = getAttribute("COMPANY_ID").getInt();
            rsInterest.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            String DocNo = getAttribute("DOC_NO").getString();
            rsInterest.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsInterest.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsInterest.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsInterest.updateBoolean("APPROVED",false);
            rsInterest.updateString("APPROVED_DATE","0000-00-00");
            rsInterest.updateBoolean("REJECTED",false);
            rsInterest.updateString("REJECTED_DATE","0000-00-00");
            rsInterest.updateString("REJECTED_REMARKS","");
            //rsInterest.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            //rsInterest.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsInterest.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsInterest.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsInterest.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsInterest.updateBoolean("CHANGED",true);
            rsInterest.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInterest.updateBoolean("CANCELLED",false);
            rsInterest.updateRow();
            rsInterest.refreshRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FINANCE.D_FD_SALES_INTEREST_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            // Inserting records in Inquiry Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_SALES_INTEREST_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            String nDocno=(String) getAttribute("DOC_NO").getObj();
            
            String Del_Query = "DELETE FROM FINANCE.D_FD_SALES_INTEREST_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+nDocno.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsSalesInterestItem ObjItem=(clsSalesInterestItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",nDocno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsTmp.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("FROM_DATE").getString()));
                rsTmp.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("TO_DATE").getString()));
                rsTmp.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsTmp.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsTmp.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateDouble("GROSS_INTEREST",ObjItem.getAttribute("GROSS_INTEREST").getDouble());
                rsTmp.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsTmp.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
               // rsTmp.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
               // rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("DOC_NO",nDocno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("RECEIPT_NO", ObjItem.getAttribute("RECEIPT_NO").getString());
                rsHDetail.updateString("FROM_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("FROM_DATE").getString()));
                rsHDetail.updateString("TO_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("TO_DATE").getString()));
                rsHDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsHDetail.updateString("INTEREST_MAIN_CODE",ObjItem.getAttribute("INTEREST_MAIN_CODE").getString());
                rsHDetail.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateDouble("GROSS_INTEREST",ObjItem.getAttribute("GROSS_INTEREST").getDouble());
                rsHDetail.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsHDetail.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                rsHDetail.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsSalesInterest.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FD_SALES_INTEREST_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE FINANCE.D_FD_SALES_INTEREST_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM FINANCE.D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsSalesInterest.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFlow.Status.equals("H")) {
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
            //--------- Approval Flow Update complete -----------//
            
            ObjFlow.UseSpecifiedURL=false;
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                ResultSet rsItem = data.getResult("SELECT * FROM D_FD_SALES_INTEREST_DETAIL WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL); //AND INTEREST_MAIN_CODE='133203' 
                rsItem.first();
                while(!rsItem.isAfterLast()){
                    CompanyID = rsItem.getInt("COMPANY_ID");
                    String ReceiptNo = rsItem.getString("RECEIPT_NO");
                    if(PostVoucher24(CompanyID,ReceiptNo,DocNo)) {
                        String nextInterestCalcDate = getNextInterestDate(CompanyID, ReceiptNo);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_SALES_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                    } else {
                        JOptionPane.showMessageDialog(null,"Error while posting Journal Voucher for TR - 24");
                        return false;
                    }
                    rsItem.next();
                }
            }
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    private boolean PostVoucher24(int CompanyID, String ReceiptNo, String curDocNo) {
        try {
            HashMap List = new HashMap();
            double TDSAmount = 0.0;
            double GrossInterest = 0.0;
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_BOOK_CODE", "DEPOSIT_INT_JOURNAL", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            
            String TDSAccountCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String InterestMainCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_SALES_INTEREST_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_SALES_INTEREST_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_SALES_INTEREST_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            TDSAmount = data.getDoubleValueFromDB("SELECT TDS_AMOUNT FROM D_FD_SALES_INTEREST_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            GrossInterest = data.getDoubleValueFromDB("SELECT GROSS_INTEREST FROM D_FD_SALES_INTEREST_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String FromDate = data.getStringValueFromDB("SELECT FROM_DATE FROM D_FD_SALES_INTEREST_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ToDate = data.getStringValueFromDB("SELECT TO_DATE FROM D_FD_SALES_INTEREST_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            // --- Start Common Elements --- //
            setAttribute("FIN_HIERARCHY_ID",0);
            List.clear();
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            String InterestExpCode = "";
            List.clear();
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsSalesInterest.ModuleID , "GET_ACCOUNT_CODE", "INTEREST_ON_CUSTOMERS_DEPOSIT", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                InterestExpCode=objRule.getAttribute("RULE_OUTCOME").getString().trim();
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.JournalVoucherModuleID);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            // --- End Common Elements --- //
            
            //--- VOUCHER POSTING FOR BOOK CODE - 24 (i.e. TR - 24) ENTRY NO. 1 - i.e FIRST VOUCHER ---//
            //=======Preparing Voucher Header ============//
            int VoucherSrNo=0;
            
            
            clsVoucher objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(ToDate));// interest calc date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","YEARLY INTEREST OF SALES DEPOSIT ON " + EITLERPGLOBAL.formatDate(ToDate) + " FOR RECEIPT NO : " + ReceiptNo + " Receipt Date : " + EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucher.setAttribute("EXCLUDE_IN_ADJ",1);
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            //=======End of  Voucher Header ============//
            
            // ====== Preparing Voucher Detail =======//
            objVoucher.colVoucherItems.clear();
            
            VoucherSrNo++;
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestExpCode); //Int Main Account Code
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",GrossInterest);
            objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ToDate));
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            //objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            //objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            //objVoucherItem.setAttribute("MODULE_ID",clsSalesInterest.ModuleID);
            objVoucherItem.setAttribute("GRN_NO","");
            objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
            objVoucherItem.setAttribute("MODULE_ID",0);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode); // cd interest transfer - 115160
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",GrossInterest);
            objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ToDate));
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            //objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            //objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            //objVoucherItem.setAttribute("MODULE_ID",clsSalesInterest.ModuleID);
            objVoucherItem.setAttribute("GRN_NO","");
            objVoucherItem.setAttribute("GRN_DATE","0000-00-00");
            objVoucherItem.setAttribute("MODULE_ID",0);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            if(TDSAmount > 0.0 ) {
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode); //Int Main Account Code
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ToDate));
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsSalesInterest.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",TDSAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("VALUE_DATE",EITLERPGLOBAL.formatDate(ToDate));
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsSalesInterest.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE - 24 (i.e. TR - 24) ENTRY NO. 1 - i.e FIRST VOUCHER ---//
            
            if(objVoucher.Insert()) {
                return true;
            } else {
                return false;
            }
        }catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDocno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_SALES_INTEREST_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocno+"' AND APPROVED=true";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsSalesInterest.ModuleID +" AND DOC_NO='"+pDocno+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    public boolean IsEditable(int pCompanyID,String pDocno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_SALES_INTEREST_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+clsSalesInterest.ModuleID+" AND DOC_NO='"+pDocno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int) getAttribute("COMPANY_ID").getVal();
            String lDocno=(String) getAttribute("DOC_NO").getObj();
            
            if(CanDelete(lCompanyID,lDocno,pUserID)) {
                rollback();
                String strQry = "DELETE FROM D_FD_SALES_INTEREST_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+lDocno.trim()+"'";
                data.Execute(strQry);
            }
            rsInterest.refreshRow();
            return MoveLast();
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(long pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DOC_NO='"+pDocNo.trim()+"' ";
        clsSalesInterest ObjItem=new clsSalesInterest();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_FD_SALES_INTEREST_HEADER " + pCondition ;
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsInterest=Stmt.executeQuery(strSql);
            
            if(!rsInterest.first()) {
                strSql = "SELECT * FROM D_FD_SALES_INTEREST_HEADER ORDER BY DOC_NO" ;
                rsInterest=Stmt.executeQuery(strSql);
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
    
    private boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        Connection tmpConn;
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            
            clsSalesInterest clsSalesInterest=new clsSalesInterest();
            
            if(HistoryView) {
                RevNo=rsInterest.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsInterest.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Populate the user
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsInterest,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsInterest,"DOC_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsInterest,"DOC_DATE","0000-00-00"));
            setAttribute("EFFECTIVE_DATE",UtilFunctions.getString(rsInterest,"EFFECTIVE_DATE","0000-00-00"));
            setAttribute("REMARKS",UtilFunctions.getString(rsInterest,"REMARKS",""));
            setAttribute("APPROVED",UtilFunctions.getInt(rsInterest,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsInterest,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsInterest,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsInterest,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsInterest,"REJECTED_REMARKS",""));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsInterest,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsInterest,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsInterest,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsInterest,"MODIFIED_DATE","0000-00-00"));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsInterest,"HIERARCHY_ID",0));
            setAttribute("CHANGED",UtilFunctions.getInt(rsInterest,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsInterest,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsInterest,"CANCELLED",0));
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            long mCompanyID= getAttribute("COMPANY_ID").getLong();
            String mDocno= getAttribute("DOC_NO").getString();
            tmpStmt=tmpConn.createStatement();
            String SQL= "";
            if(HistoryView) {
                SQL = "SELECT * FROM D_FD_SALES_INTEREST_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO ";
                rsTmp=tmpStmt.executeQuery(SQL);
            } else {
                SQL = "SELECT * FROM D_FD_SALES_INTEREST_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' ORDER BY SR_NO";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsSalesInterestItem ObjItem=new clsSalesInterestItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjItem.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                ObjItem.setAttribute("FROM_DATE",rsTmp.getString("FROM_DATE"));
                ObjItem.setAttribute("TO_DATE",rsTmp.getString("TO_DATE"));
                ObjItem.setAttribute("INTEREST_RATE",rsTmp.getDouble("INTEREST_RATE"));
                ObjItem.setAttribute("INTEREST_MAIN_CODE",rsTmp.getString("INTEREST_MAIN_CODE"));
                ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjItem.setAttribute("GROSS_INTEREST",rsTmp.getDouble("GROSS_INTEREST"));
                ObjItem.setAttribute("TDS_AMOUNT",rsTmp.getDouble("TDS_AMOUNT"));
                ObjItem.setAttribute("NET_INTEREST",rsTmp.getDouble("NET_INTEREST"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getString("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getString("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("CHANGED",rsTmp.getInt("CHANGED"));
                ObjItem.setAttribute("CHANGED_DATE",rsTmp.getString("CHANGED_DATE"));
                ObjItem.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                
                colLineItems.put(Long.toString(Counter),ObjItem);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean rollback() {
        try {
            ResultSet rsTmp;
            Statement Stmt;
            String nDocno = (String) getAttribute("DOC_NO").getObj();
            Stmt=Conn.createStatement();
            rsTmp=Stmt.executeQuery("DELETE FROM D_FD_SALES_INTEREST_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='" + nDocno.trim()+"' ");
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static HashMap getItemList(String EffectiveDate) {
        Connection tmpConn;
        Statement stItem=null;
        ResultSet rsItem=null;
        int Counter=0;
        HashMap List=new HashMap();
        String strSQL="";
        String previousWarrantNo = "";
        boolean updated = false;
        try {
            EffectiveDate = EITLERPGLOBAL.formatDateDB(EffectiveDate);
            String MonthStartDate = EffectiveDate.trim().substring(0,4)+"-"+EffectiveDate.trim().substring(5,7)+"-01";
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            strSQL="SELECT RECEIPT_NO, EFFECTIVE_DATE, AMOUNT, INTEREST_RATE, INT_CALC_DATE, INTEREST_MAIN_CODE, PARTY_CODE, MAIN_ACCOUNT_CODE " +
            " FROM D_FD_SALES_DEPOSIT_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPOSIT_STATUS=0 " +
            " AND INT_CALC_DATE<='"+EffectiveDate+"' AND PARTY_CODE NOT IN ('061930','392822','491312','591330') "; //AND INT_CALC_DATE>='"+MonthStartDate+"'
            
            stItem=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsItem=stItem.executeQuery(strSQL);
            rsItem.first();
            int SrNo=0;
            String StartFinYear = EITLERPGLOBAL.getFinYearStartDate(EffectiveDate);
            int FinYear = EITLERPGLOBAL.getCurrentFinYear();
            //String StartFinYear = FinYear+"-04-01";
            //++FinYear;
            //String EndFinYear = FinYear+"-03-31";
            String EndFinYear = EITLERPGLOBAL.getFinYearStartDate(EffectiveDate);
            if(rsItem.getRow()>0){
                while(!rsItem.isAfterLast()) {
                    clsSalesInterestItem ObjItem=new clsSalesInterestItem();
                    String ReceiptNo = UtilFunctions.getString(rsItem,"RECEIPT_NO","");
                    String eDate = UtilFunctions.getString(rsItem,"EFFECTIVE_DATE","0000-00-00");
                    String MaturityDate = UtilFunctions.getString(rsItem,"MATURITY_DATE","0000-00-00");
                    String interestCalcDate = UtilFunctions.getString(rsItem,"INT_CALC_DATE","0000-00-00");
                    String PartyCode = UtilFunctions.getString(rsItem,"PARTY_CODE","");
                    String IntMainCode = UtilFunctions.getString(rsItem,"INTEREST_MAIN_CODE","");
                    String MainAccountCode = UtilFunctions.getString(rsItem,"MAIN_ACCOUNT_CODE","");
                    double Amount = rsItem.getDouble("AMOUNT");
                    double Rate = rsItem.getDouble("INTEREST_RATE");
                    String FromDate = "";
                    String ToDate = "";
                    if(data.getIntValueFromDB("SELECT DO_NOT_ALLOW_INTEREST FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND APPROVED=1 AND CANCELLED=0")==1) {
                        rsItem.next();
                        continue;
                    }
                    ++Counter;
                    double GrossInterest=0.0;
                    double TDSAmount = 0.0;
                    double netInterest = 0.0;
                    double partyInterest=0.0;
                    double currentInterest = 0.0;
                    //=============Start of Interest Calculation =================//
                    
                    long totalDays=0;
                    if(java.sql.Date.valueOf(eDate).after(java.sql.Date.valueOf(StartFinYear))) {
                        totalDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(eDate), java.sql.Date.valueOf(EffectiveDate)) + 1;
                        FromDate = eDate;
                    } else {
                        totalDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(EffectiveDate)) + 1;
                        FromDate = StartFinYear;
                    }
                    ToDate = EffectiveDate;
                    GregorianCalendar cal = new GregorianCalendar();
                    if(cal.isLeapYear(Integer.parseInt(EffectiveDate.substring(0,4)))) {
                        GrossInterest = EITLERPGLOBAL.round(((Amount * Rate * totalDays)/(366* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
                    } else {
                        GrossInterest = EITLERPGLOBAL.round(((Amount * Rate * totalDays)/(365* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
                    }
                    //=============End of Interest Calculation =================//
                    
                    
                    //======= Start collecting calculated data ================//
                    SrNo++;
                    ObjItem.setAttribute("SR_NO",SrNo);
                    ObjItem.setAttribute("RECEIPT_NO",ReceiptNo);
                    ObjItem.setAttribute("FROM_DATE",FromDate);
                    ObjItem.setAttribute("TO_DATE",ToDate);
                    String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
                    int SchemeType = data.getIntValueFromDB("SELECT SCHEME_TYPE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' ",FinanceGlobal.FinURL);
                    ObjItem.setAttribute("SCHEME_TYPE",SchemeType);
                    ObjItem.setAttribute("INTEREST_MAIN_CODE",IntMainCode);
                    ObjItem.setAttribute("PARTY_CODE",PartyCode);
                    ObjItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                    ObjItem.setAttribute("INTEREST_RATE",Rate);
                    
                    if(data.getIntValueFromDB("SELECT TAX_EX_FORM_RECEIVED FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL)==0) {
                        partyInterest = clsSalesDepositMaster.checkTDSAmount(PartyCode,ReceiptNo,EffectiveDate);
                        partyInterest += GrossInterest;
                        if(partyInterest > 5000.0) {
                            TDSAmount = clsSalesDepositMaster.calculateTDSAmount(GrossInterest,ReceiptNo);
                        }
                    }
                    
                    netInterest = EITLERPGLOBAL.round(GrossInterest - TDSAmount,2);
                    ObjItem.setAttribute("GROSS_INTEREST",GrossInterest);
                    ObjItem.setAttribute("TDS_AMOUNT",TDSAmount);
                    ObjItem.setAttribute("NET_INTEREST",netInterest);
                    
                    ObjItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                    ObjItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                    ObjItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("CHANGED",true);
                    ObjItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("CANCELLED",false);
                    
                    //Put into list
                    List.put(Integer.toString(Counter),ObjItem);
                    rsItem.next();
                }
            }
            return List;
        } catch(Exception e) {
            e.printStackTrace();
            return List;
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
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_NO,FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_INTEREST_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_SALES_INTEREST_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_SALES_INTEREST_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsSalesInterest.ModuleID+" ORDER BY "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_NO,FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_INTEREST_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_SALES_INTEREST_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_SALES_INTEREST_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsSalesInterest.ModuleID+" ORDER BY FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_NO,FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_SALES_INTEREST_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_SALES_INTEREST_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_SALES_INTEREST_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+clsSalesInterest.ModuleID+" ORDER BY FINANCE.D_FD_SALES_INTEREST_HEADER.DOC_NO";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsSalesInterest ObjDoc=new clsSalesInterest();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    //Put the prepared user object into list
                    List.put(Long.toString(Counter),ObjDoc);
                }//End of While
                rsTmp.next();
            }
            
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
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsInterest=Stmt.executeQuery("SELECT * FROM FINANCE.D_FD_SALES_INTEREST_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_SALES_INTEREST_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsSalesInterest objSalesInterest=new clsSalesInterest();
                
                objSalesInterest.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                objSalesInterest.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                objSalesInterest.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                objSalesInterest.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                objSalesInterest.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                objSalesInterest.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),objSalesInterest);
            }
            
            rsTmp.close();
            stTmp.close();
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_SALES_INTEREST_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FD_SALES_INTEREST_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND CANCELLED=0");
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
    
    private boolean Validate() {
        return true;
    }
    
    public static String getNextInterestDate(int CompanyID, String ReceiptNo) {
        String nextInterestCalcDate="";
        try {
            String InterestCalcDate = data.getStringValueFromDB("SELECT INT_CALC_DATE FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
            String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_SALES_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            int InterestCalcPeriod = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' ",FinanceGlobal.FinURL);
            nextInterestCalcDate = clsCalcInterest.addMonthToDate(InterestCalcDate, 12);
        } catch(Exception e) {
            return nextInterestCalcDate;
        }
        return nextInterestCalcDate;
    }
}
