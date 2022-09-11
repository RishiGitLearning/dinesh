/*
 * clsCalcInterest.java
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
public class clsCalcInterest {
    
    public String LastError = "";
    private ResultSet rsInterest;
    private Connection Conn;
    private Statement Stmt;
    public boolean Ready = false;
    
    public HashMap colLineItems;
    private HashMap props;
    
    //History Related properties
    private boolean HistoryView=false;
    public static int ModuleID=86;
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
    public clsCalcInterest() {
        props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("EFFECTIVE_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("BOOK_CODE", new Variant(""));
        props.put("TDS_ONLY", new Variant(0));
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
            String strSql = "SELECT * FROM D_FD_INT_CALC_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO ";
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
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_INT_CALC_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsCalcInterest.ModuleID,getAttribute("FFNO").getInt(),true));
            
            //========= Inserting record into Header =========//
            rsInterest.moveToInsertRow();
            rsInterest.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsInterest.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            String theDocNo = getAttribute("DOC_NO").getString();
            rsInterest.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsInterest.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsInterest.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsInterest.updateString("BOOK_CODE", getAttribute("BOOK_CODE").getString());
            rsInterest.updateInt("TDS_ONLY", getAttribute("TDS_ONLY").getInt());
            rsInterest.updateBoolean("APPROVED",false);
            rsInterest.updateString("APPROVED_DATE","0000-00-00");
            rsInterest.updateBoolean("REJECTED",false);
            rsInterest.updateString("REJECTED_DATE","0000-00-00");
            rsInterest.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
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
            rsHistory.updateString("BOOK_CODE", getAttribute("BOOK_CODE").getString());
            rsHistory.updateInt("TDS_ONLY", getAttribute("TDS_ONLY").getInt());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
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
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND DOC_NO='1'");
            DocNo=getAttribute("DOC_NO").getString();
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsCalcInterestItem ObjItem=(clsCalcInterestItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",DocNo);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsTmp.updateInt("INTEREST_DAYS",(int)ObjItem.getAttribute("INTEREST_DAYS").getVal());
                rsTmp.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsTmp.updateString("INT_MAIN_ACCOUNT_CODE",ObjItem.getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateDouble("INTEREST_AMOUNT",ObjItem.getAttribute("INTEREST_AMOUNT").getDouble());
                rsTmp.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsTmp.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                rsTmp.updateInt("LEGACY_WARRANT_NO",ObjItem.getAttribute("LEGACY_WARRANT_NO").getInt());
                rsTmp.updateString("WARRANT_NO",ObjItem.getAttribute("WARRANT_NO").getString());
                rsTmp.updateString("WARRANT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("WARRANT_DATE").getString()));
                rsTmp.updateInt("MICR_NO",ObjItem.getAttribute("MICR_NO").getInt());
                rsTmp.updateString("WARRANT_CLEAR",ObjItem.getAttribute("WARRANT_CLEAR").getString());
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
                rsHDetail.updateInt("INTEREST_DAYS",(int)ObjItem.getAttribute("INTEREST_DAYS").getVal());
                rsHDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsHDetail.updateString("INT_MAIN_ACCOUNT_CODE",ObjItem.getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateDouble("INTEREST_AMOUNT",ObjItem.getAttribute("INTEREST_AMOUNT").getDouble());
                rsHDetail.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsHDetail.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                rsHDetail.updateInt("LEGACY_WARRANT_NO",ObjItem.getAttribute("LEGACY_WARRANT_NO").getInt());
                rsHDetail.updateString("WARRANT_NO",ObjItem.getAttribute("WARRANT_NO").getString());
                rsHDetail.updateString("WARRANT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("WARRANT_DATE").getString()));
                rsHDetail.updateInt("MICR_NO",ObjItem.getAttribute("MICR_NO").getInt());
                rsHDetail.updateString("WARRANT_CLEAR",ObjItem.getAttribute("WARRANT_CLEAR").getString());
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
            ObjFlow.ModuleID=86;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FD_INT_CALC_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            ObjFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            
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
            boolean DonePosting = false;
            if(ObjFlow.Status.equals("F")) {
                ResultSet rsItem = data.getResult("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+theDocNo+"' AND WARRANT_NO<>'0000000' ORDER BY WARRANT_NO",FinanceGlobal.FinURL);
                rsItem.first();
                if(rsItem.getRow()>0){
                    while(!rsItem.isAfterLast()) {
                        int CompanyID = rsItem.getInt("COMPANY_ID");
                        String ReceiptNo = rsItem.getString("RECEIPT_NO");
                        double Amount = rsItem.getDouble("NET_INTEREST");
                        String curDocNo = rsItem.getString("DOC_NO");
                        if(PostVoucher24(CompanyID,ReceiptNo,Amount,curDocNo)) {
                            if(PostVoucher(CompanyID,ReceiptNo,Amount,curDocNo)) {
                                String nextInterestCalcDate = getNextInterestDate(CompanyID, ReceiptNo);
                                data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                                data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                                
                            } else {
                                LastError += "\n Error while posting voucher for book code = " + getAttribute("BOOK_CODE").getString()+"  RECEIPT NO ="+ReceiptNo;;
                                DonePosting=false;
                                return false;
                            }
                        } else {
                            LastError += "\n Error while posting voucher for book code = 24 RECEIPT NO ="+ReceiptNo;
                            DonePosting=false;
                            return false;
                        }
                        rsItem.next();
                    }
                }
                DonePosting=true;
            }
            
            while(DonePosting) {
                String SQL = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+theDocNo+"' AND WARRANT_NO='0000000' ORDER BY RECEIPT_NO";
                ResultSet rsResult = data.getResult(SQL,FinanceGlobal.FinURL);
                int Counter = 0;
                rsResult.first();
                if(rsResult.getRow() > 0) {
                    while(!rsResult.isAfterLast()) {
                        Counter++;
                        String ReceiptNo = rsResult.getString("RECEIPT_NO");
                        String PartyCode = rsResult.getString("PARTY_CODE");
                        String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        String theDate = getNextInterestDate(EITLERPGLOBAL.gCompanyID, ReceiptNo);
                        System.out.println("Counter : "+ Counter +" Receipt No : "+ReceiptNo+ " PartyCode : " + PartyCode+" Int. Calc. Date : "+theDate + " Maturity Date : " + MaturityDate);
                        data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET INT_CALC_DATE='"+theDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+theDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                        rsResult.next();
                    }
                }
                DonePosting=false;
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
        String theDocNo = "";
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
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_INT_CALC_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            theDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsInterest.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsInterest.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsInterest.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsInterest.updateString("EFFECTIVE_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsInterest.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsInterest.updateString("BOOK_CODE", getAttribute("BOOK_CODE").getString());
            rsInterest.updateInt("TDS_ONLY", getAttribute("TDS_ONLY").getInt());
            rsInterest.updateBoolean("APPROVED",false);
            rsInterest.updateString("APPROVED_DATE","0000-00-00");
            rsInterest.updateBoolean("REJECTED",false);
            rsInterest.updateString("REJECTED_DATE","0000-00-00");
            rsInterest.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FINANCE.D_FD_INT_CALC_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
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
            rsHistory.updateString("BOOK_CODE", getAttribute("BOOK_CODE").getString());
            rsHistory.updateInt("TDS_ONLY", getAttribute("TDS_ONLY").getInt());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            //rsHistory.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
            //rsHistory.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
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
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            String nDocno=(String) getAttribute("DOC_NO").getObj();
            
            String Del_Query = "DELETE FROM FINANCE.D_FD_INT_CALC_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+nDocno.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsCalcInterestItem ObjItem=(clsCalcInterestItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",nDocno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("RECEIPT_NO",ObjItem.getAttribute("RECEIPT_NO").getString());
                rsTmp.updateInt("INTEREST_DAYS",(int)ObjItem.getAttribute("INTEREST_DAYS").getVal());
                rsTmp.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsTmp.updateString("INT_MAIN_ACCOUNT_CODE",ObjItem.getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateDouble("INTEREST_AMOUNT",ObjItem.getAttribute("INTEREST_AMOUNT").getDouble());
                rsTmp.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsTmp.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                rsTmp.updateInt("LEGACY_WARRANT_NO", ObjItem.getAttribute("LEGACY_WARRANT_NO").getInt());
                rsTmp.updateString("WARRANT_NO", ObjItem.getAttribute("WARRANT_NO").getString());
                rsTmp.updateString("WARRANT_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("WARRANT_DATE").getString()));
                rsTmp.updateInt("MICR_NO", ObjItem.getAttribute("MICR_NO").getInt());
                rsTmp.updateString("WARRANT_CLEAR", ObjItem.getAttribute("WARRANT_CLEAR").getString());
                //rsTmp.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
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
                rsHDetail.updateInt("INTEREST_DAYS",(int)ObjItem.getAttribute("INTEREST_DAYS").getVal());
                rsHDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                rsHDetail.updateString("INT_MAIN_ACCOUNT_CODE",ObjItem.getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateDouble("INTEREST_AMOUNT",ObjItem.getAttribute("INTEREST_AMOUNT").getDouble());
                rsHDetail.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                rsHDetail.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                rsHDetail.updateInt("LEGACY_WARRANT_NO", ObjItem.getAttribute("LEGACY_WARRANT_NO").getInt());
                rsHDetail.updateString("WARRANT_NO", ObjItem.getAttribute("WARRANT_NO").getString());
                rsHDetail.updateString("WARRANT_DATE", EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("WARRANT_DATE").getString()));
                rsHDetail.updateInt("MICR_NO", ObjItem.getAttribute("MICR_NO").getInt());
                rsHDetail.updateString("WARRANT_CLEAR", ObjItem.getAttribute("WARRANT_CLEAR").getString());
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
            ObjFlow.ModuleID=86;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FD_INT_CALC_HEADER";
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
                data.Execute("UPDATE FINANCE.D_FD_INT_CALC_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM FINANCE.D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=86 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            boolean DonePosting = false;
            if(ObjFlow.Status.equals("F")) {
                if(data.getIntValueFromDB("SELECT TDS_ONLY FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL)==0) {
                    ResultSet rsItem = data.getResult("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+theDocNo+"' AND WARRANT_NO<>'0000000' ORDER BY WARRANT_NO",FinanceGlobal.FinURL);
                    rsItem.first();
                    if(rsItem.getRow()>0){
                        while(!rsItem.isAfterLast()) {
                            int CompanyID = rsItem.getInt("COMPANY_ID");
                            String ReceiptNo = rsItem.getString("RECEIPT_NO");
                            double Amount = rsItem.getDouble("NET_INTEREST");
                            String curDocNo = rsItem.getString("DOC_NO");
                            if(PostVoucher24(CompanyID,ReceiptNo,Amount,curDocNo)) {
                                if(PostVoucher(CompanyID,ReceiptNo,Amount,curDocNo)) {
                                    String nextInterestCalcDate = getNextInterestDate(CompanyID, ReceiptNo);
                                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                                    
                                } else {
                                    LastError += "\n Error while posting voucher for book code = " + getAttribute("BOOK_CODE").getString()+"  RECEIPT NO ="+ReceiptNo;;
                                    DonePosting=false;
                                    return false;
                                }
                            } else {
                                LastError += "\n Error while posting voucher for book code = 24 RECEIPT NO ="+ReceiptNo;
                                DonePosting=false;
                                return false;
                            }
                            rsItem.next();
                        }
                    }
                    DonePosting=true;
                    
                    while(DonePosting) {
                        String SQL = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+theDocNo+"' AND WARRANT_NO='0000000' ORDER BY RECEIPT_NO";
                        ResultSet rsResult = data.getResult(SQL,FinanceGlobal.FinURL);
                        int Counter = 0;
                        rsResult.first();
                        if(rsResult.getRow() > 0) {
                            while(!rsResult.isAfterLast()) {
                                Counter++;
                                String ReceiptNo = rsResult.getString("RECEIPT_NO");
                                String PartyCode = rsResult.getString("PARTY_CODE");
                                String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                                String theDate = getNextInterestDate(EITLERPGLOBAL.gCompanyID, ReceiptNo);
                                //System.out.println("Counter : "+ Counter +" Receipt No : "+ReceiptNo+ " PartyCode : " + PartyCode+" Int. Calc. Date : "+theDate + " Maturity Date : " + MaturityDate);
                                data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET INT_CALC_DATE='"+theDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                                data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+theDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
                                rsResult.next();
                            }
                        }
                        DonePosting=false;
                    }
                } else {
                    ResultSet rsItem = data.getResult("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+theDocNo+"' AND WARRANT_NO='0000000' ORDER BY RECEIPT_NO",FinanceGlobal.FinURL);
                    rsItem.first();
                    while(!rsItem.isAfterLast()) {
                        int CompanyID = rsItem.getInt("COMPANY_ID");
                        String ReceiptNo = rsItem.getString("RECEIPT_NO");
                        if(!PostVoucher22(CompanyID,ReceiptNo,theDocNo)) {
                            LastError += "\n Error while posting voucher for book code = 22 RECEIPT NO ="+ReceiptNo;
                        }
                        rsItem.next();
                    }
                }
            }
            return true;
        } catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    private boolean PostVoucher22(int CompanyID,String ReceiptNo,String curDocNo) {
        try {
            clsVoucher objVoucher=new clsVoucher();
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            /*========== Select the Hierarchy ======== */
            setAttribute("FIN_HIERARCHY_ID",0);
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "2");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            /*========== End of Hierarchy Selection ======== */
            
            /*========== Select Prifix, Suffix and Firstfree No. ======== */
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.JournalVoucherModuleID);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            /*========== End of Prifix, Suffix and Firstfree No. ======== */
            
            //============= Gethering Data =================//
            int VoucherSrNo=0;
            String IntMainAccountCode = data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+curDocNo+"' AND RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            
            String MainAccountCode = "";
            List.clear();
            int DepositerCategory = data.getIntValueFromDB("SELECT DEPOSITER_CATEGORY FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
            if(DepositerCategory == 2) {
                List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "SHAREHOLDER_ACCOUNT_CODE", "");
            } else {
                List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "PUBLIC_ACCOUNT_CODE", "");
            }
            
            if(List.size()>0) {
                //Get cumulative interest payable account.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                MainAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String TDSAccountCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            
            double InterestAmount = data.getDoubleValueFromDB("SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+curDocNo+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            double TDSAmount = data.getDoubleValueFromDB("SELECT TDS_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+curDocNo+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            double netInterest = data.getDoubleValueFromDB("SELECT NET_INTEREST FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+curDocNo+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE MAIN_ACCOUNT_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String WarrantDate = data.getStringValueFromDB("SELECT WARRANT_DATE FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='"+curDocNo+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+SubAccountCode+"' ",FinanceGlobal.FinURL);
            //============= Gethering Data =======================//
            
            /*============== Preparing Voucher Header ================*/
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(WarrantDate));//
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","INTEREST PROVISION ON " + EITLERPGLOBAL.FinToDate);
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Final Approved --> Voucher
            /*============== End of Voucher Header ================*/
            
            /*============== Voucher Detail =================*/
            objVoucher.colVoucherItems.clear();
            
            VoucherSrNo++;
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",IntMainAccountCode); //Int Main Account Code
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); // cd interest transfer - 115160
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            if(TDSAmount>0) {
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                objVoucherItem.setAttribute("AMOUNT",TDSAmount);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
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
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            } else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean PostVoucher24(int CompanyID, String ReceiptNo, double InterestAmount, String curDocNo) {
        try {
            String MaturityDate ="";
            String InterestDate ="";
            double PaidTDSAmount = 0.0;
            double currentTDSAmount = 0.0;
            clsDepositMaster ObjDepositMaster=(clsDepositMaster)(new clsDepositMaster()).getObject(CompanyID,ReceiptNo);
            String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()) + "-04-01";
            String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1) + "-03-31";
            String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            if(EffectiveDate.substring(5,7).equals("04")) {
                StartFinYear = EITLERPGLOBAL.getFinYearStartDate(EffectiveDate);
                EndFinYear = EITLERPGLOBAL.getFinYearEndDate(EffectiveDate);
            }
            
            int DepositTypeID = data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            if(DepositTypeID == 2 ) {
                InterestDate = data.getStringValueFromDB("SELECT WARRANT_DATE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
                currentTDSAmount = data.getDoubleValueFromDB("SELECT SUM(TDS_AMOUNT) FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND WARRANT_DATE>='"+StartFinYear+"' AND WARRANT_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL);
                InterestAmount = data.getDoubleValueFromDB("SELECT SUM(INTEREST_AMOUNT) FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND WARRANT_DATE>='"+StartFinYear+"' AND WARRANT_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL);
            } else {
                InterestDate = data.getStringValueFromDB("SELECT WARRANT_DATE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
                currentTDSAmount = data.getDoubleValueFromDB("SELECT TDS_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
                InterestAmount = data.getDoubleValueFromDB("SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            }
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            // --- Start Common Elements --- //
            clsVoucher objVoucher=new clsVoucher();
            
            setAttribute("FIN_HIERARCHY_ID",0);
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "2");
            
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
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
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_BOOK_CODE", "DEPOSIT_INT_JOURNAL", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String WarrantNo = data.getStringValueFromDB("SELECT WARRANT_NO FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ReceiptDate = data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String TDSAccountCode = "";
            
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String IntMainAccountCode = data.getStringValueFromDB("SELECT INT_MAIN_ACCOUNT_CODE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode = "";
            List.clear();
            if(data.getIntValueFromDB("SELECT SCHEME_TYPE FROM D_FD_DEPOSIT_MASTER A, D_FD_SCHEME_MASTER B WHERE A.SCHEME_ID=B.SCHEME_ID AND A.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL)==3) {
                int DepositerCategory = data.getIntValueFromDB("SELECT DEPOSITER_CATEGORY FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(DepositerCategory == 2) {
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "SHAREHOLDER_ACCOUNT_CODE", "");
                } else {
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "PUBLIC_ACCOUNT_CODE", "");
                }
                if(List.size()>0) {
                    //Get cumulative interest payable account.
                    clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                    MainAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
                }
                
            } else {
                //MainAccountCode = ObjDepositMaster.getAttribute("MAIN_ACCOUNT_CODE").getString();
                MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            }
            String SubAccountCode = ObjDepositMaster.getAttribute("PARTY_CODE").getString();
            
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            objVoucher.setAttribute("BOOK_CODE",BookCode); //24
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL); // payment
            objVoucher.setAttribute("BANK_NAME",BankName); //Deposit Int. journal
            objVoucher.setAttribute("CHEQUE_NO",WarrantNo); //
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(InterestDate));
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(InterestDate));// interest calc date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",IntMainAccountCode); //Int Main Account Code
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); // cd interest transfer - 115160
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            if(currentTDSAmount > 0.0 ) {
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); //Int Main Account Code
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                objVoucherItem.setAttribute("AMOUNT",currentTDSAmount);
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",TDSAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",currentTDSAmount);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositMaster.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE - 24 (i.e. TR - 24) ENTRY NO. 1 - i.e FIRST VOUCHER ---//
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            } else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        }catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    private boolean PostVoucher(int CompanyID, String ReceiptNo, double InterestAmount, String curDocNo) {
        try {
            String MaturityDate ="";
            String InterestDate ="";
            double TDSAmount = 0.0;
            double GrossAmount = 0.0;
            clsDepositMaster ObjDepositMaster=(clsDepositMaster)(new clsDepositMaster()).getObject(CompanyID,ReceiptNo);
            String PartyCode = ObjDepositMaster.getAttribute("PARTY_CODE").getString();
            if(data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL)== 2 ) {
                InterestDate = data.getStringValueFromDB("SELECT WARRANT_DATE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
                GrossAmount = data.getDoubleValueFromDB("SELECT SUM(A.INTEREST_AMOUNT) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                //InterestAmount = data.getDoubleValueFromDB("SELECT SUM(A.NET_INTEREST) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                TDSAmount = data.getDoubleValueFromDB("SELECT SUM(A.TDS_AMOUNT) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO ",FinanceGlobal.FinURL);
                InterestAmount = EITLERPGLOBAL.round(GrossAmount - TDSAmount,2);
            } else {
                InterestDate = data.getStringValueFromDB("SELECT WARRANT_DATE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            }
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            // --- Start Common Elements --- //
            clsVoucher objVoucher=new clsVoucher();
            
            setAttribute("FIN_HIERARCHY_ID",0);
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "1");
            
            if(List.size()>0) {
                // Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.PaymentModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            // --- End Common Elements --- //
            
            //--- VOUCHER POSTING FOR BOOK CODE SECOND VOUCHER ---//
            
            //======== Gethering Requiered Data ========//
            int VoucherSrNo=0;
            String BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);//getAttribute("BOOK_CODE").getString();
            String curDocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);//getAttribute("BOOK_CODE").getString();
            String BankMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String WarrantNo = data.getStringValueFromDB("SELECT WARRANT_NO FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String MainAccountCode="";
            List.clear();
            if(data.getIntValueFromDB("SELECT SCHEME_TYPE FROM D_FD_DEPOSIT_MASTER A, D_FD_SCHEME_MASTER B WHERE A.SCHEME_ID=B.SCHEME_ID AND A.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL)==3) {
                int DepositerCategory = data.getIntValueFromDB("SELECT DEPOSITER_CATEGORY FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(DepositerCategory == 2) {
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "SHAREHOLDER_ACCOUNT_CODE", "");
                } else {
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "PUBLIC_ACCOUNT_CODE", "");
                }
                if(List.size()>0) {
                    //Get cumulative interest payable account.
                    clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                    MainAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
                }
                //MainAccountCode = "115201";
                //MainAccountCode = "115160";
            } else {
                MainAccountCode = ObjDepositMaster.getAttribute("MAIN_ACCOUNT_CODE").getString();
            }
            
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            //======== End of Gethering Requiered Data ========//
            
            //=======Preparing Voucher Header ============//
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); //24
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_PAYMENT); // pyament
            objVoucher.setAttribute("CHEQUE_NO",WarrantNo); //
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(InterestDate));
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(InterestDate));// interest calc date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); // FOR CD INTEREST - 115160/115201
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(curDocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsCalcInterest.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",BankMainCode); //Bank Main Account Code
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(curDocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsCalcInterest.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE SECOND VOUCHER ---//
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            } else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        } catch(Exception e) {
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_INT_CALC_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocno+"' AND APPROVED=true";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=86 AND DOC_NO='"+pDocno+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_INT_CALC_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=86 AND DOC_NO='"+pDocno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
                String strQry = "DELETE FROM D_FD_INT_CALC_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+lDocno.trim()+"'";
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
        clsCalcInterest ObjItem=new clsCalcInterest();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_FD_INT_CALC_HEADER " + pCondition ;
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsInterest=Stmt.executeQuery(strSql);
            
            if(!rsInterest.first()) {
                strSql = "SELECT * FROM D_FD_INT_CALC_HEADER ORDER BY DOC_NO" ;
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
            
            clsCalcInterest objCalcInterest=new clsCalcInterest();
            
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
            setAttribute("BOOK_CODE",UtilFunctions.getString(rsInterest,"BOOK_CODE", ""));
            setAttribute("TDS_ONLY",UtilFunctions.getInt(rsInterest, "TDS_ONLY", 0));
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
                SQL = "SELECT * FROM D_FD_INT_CALC_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO ";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            else {
                SQL = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' ORDER BY SR_NO";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsCalcInterestItem ObjItem=new clsCalcInterestItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjItem.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                ObjItem.setAttribute("INTEREST_DAYS",rsTmp.getInt("INTEREST_DAYS"));
                ObjItem.setAttribute("INTEREST_RATE",rsTmp.getDouble("INTEREST_RATE"));
                ObjItem.setAttribute("INT_MAIN_ACCOUNT_CODE",rsTmp.getString("INT_MAIN_ACCOUNT_CODE"));
                ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjItem.setAttribute("INTEREST_AMOUNT",rsTmp.getDouble("INTEREST_AMOUNT"));
                ObjItem.setAttribute("TDS_AMOUNT",rsTmp.getDouble("TDS_AMOUNT"));
                ObjItem.setAttribute("NET_INTEREST",rsTmp.getDouble("NET_INTEREST"));
                ObjItem.setAttribute("LEGACY_WARRANT_NO",rsTmp.getInt("LEGACY_WARRANT_NO"));
                ObjItem.setAttribute("WARRANT_NO",rsTmp.getString("WARRANT_NO"));
                ObjItem.setAttribute("WARRANT_DATE",rsTmp.getString("WARRANT_DATE"));
                ObjItem.setAttribute("MICR_NO",rsTmp.getInt("MICR_NO"));
                ObjItem.setAttribute("WARRANT_CLEAR",rsTmp.getString("WARRANT_CLEAR"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getString("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getString("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("CHANGED",rsTmp.getInt("CHANGED"));
                ObjItem.setAttribute("CHANGED_DATE",rsTmp.getString("CHANGED_DATE"));
                ObjItem.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                
                colLineItems.put(Long.toString(Counter),ObjItem);
                rsTmp.next();
            }// Inner while
            return true;
        }
        catch(Exception e) {
            //e.printStackTrace();
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
            rsTmp=Stmt.executeQuery("DELETE FROM D_FD_INT_CALC_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='" + nDocno.trim()+"' ");
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static HashMap getItemListCDFD(String EffectiveDate) {
        Connection tmpConn;
        Statement stTmp=null;
        ResultSet rsTmp=null;
        int Counter=0;
        HashMap List=new HashMap();
        String strSQL="";
        String previousWarrantNo = "";
        boolean updated = false;
        long LegacyWarrantNo=0;
        try {
            String MonthStartDate = EffectiveDate.trim().substring(6,10)+"-"+EffectiveDate.trim().substring(3,5)+"-01";
            LegacyWarrantNo = getNextLegacyWarrantNo(MonthStartDate);
            
            String FinStartDate = EITLERPGLOBAL.getFinYearStartDate(EITLERPGLOBAL.formatDateDB(EffectiveDate));
            String FinEndDate = EITLERPGLOBAL.getFinYearEndDate(EITLERPGLOBAL.formatDateDB(EffectiveDate));
            
            if(EffectiveDate.trim().substring(3,5).equals("10") || EffectiveDate.trim().substring(3,5).equals("04")) {
                //MonthStartDate = clsDepositMaster.deductDays(MonthStartDate, 1);
            } else {
                MonthStartDate = clsDepositMaster.deductDays(MonthStartDate, 1);
            }
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            
            strSQL = "SELECT A.RECEIPT_NO, A.RECEIPT_DATE, A.EFFECTIVE_DATE, A.AMOUNT, A.DEPOSIT_PERIOD, A.INTEREST_RATE, A.INT_CALC_DATE, " +
            "B.INT_MAIN_ACCOUNT_CODE, A.PARTY_CODE, A.MAIN_ACCOUNT_CODE, A.MATURITY_DATE, B.INTEREST_CALCULATION_PERIOD, " +
            "B.INTEREST_CALCULATION_TYPE, CASE WHEN B.SCHEME_TYPE=1 THEN B.SCHEME_TYPE+5 WHEN B.SCHEME_TYPE=2 THEN B.SCHEME_TYPE " +
            "WHEN B.SCHEME_TYPE=3 THEN B.SCHEME_TYPE+10 ELSE 0 END AS SCHEME_ORDER FROM D_FD_DEPOSIT_MASTER A, D_FD_SCHEME_MASTER B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.COMPANY_ID=2 AND A.SCHEME_ID=B.SCHEME_ID AND A.DEPOSIT_STATUS=0 " +
            "AND A.INT_CALC_DATE>='"+MonthStartDate+"' AND A.INT_CALC_DATE<='"+EITLERPGLOBAL.formatDateDB(EffectiveDate)+"' " +
            //"AND A.MATURITY_DATE>='"+MonthStartDate+"' AND A.MATURITY_DATE<='"+EITLERPGLOBAL.formatDateDB(EffectiveDate)+"' " +
            "AND A.INT_CALC_DATE < A.MATURITY_DATE AND A.APPROVED=1 AND A.CANCELLED=0 " +
            "ORDER BY SCHEME_ORDER,DAY(A.MATURITY_DATE),MONTH(A.MATURITY_DATE) ";
            //"ORDER BY SCHEME_ORDER,A.MATURITY_DATE ";
            
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            int SrNo=0;
            if(rsTmp.getRow()>0){
                while(!rsTmp.isAfterLast()) {
                    clsCalcInterestItem ObjItem=new clsCalcInterestItem();
                    String ReceiptNo = rsTmp.getString("RECEIPT_NO");
                    String eDate = rsTmp.getString("EFFECTIVE_DATE");
                    String MaturityDate = UtilFunctions.getString(rsTmp,"MATURITY_DATE","0000-00-00");
                    String interestCalcDate = UtilFunctions.getString(rsTmp,"INT_CALC_DATE","0000-00-00");
                    String Partycode = UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                    if(ReceiptNo.equals("M003017")) {
                        System.out.println();
                    }
                    String Check = "SELECT A.* FROM D_FD_INT_CALC_DETAIL A,D_FD_INT_CALC_HEADER B " +
                    "WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+Partycode+"' AND (A.WARRANT_DATE='"+MaturityDate+"' OR A.WARRANT_DATE='"+interestCalcDate+"') AND A.DOC_NO=B.DOC_NO AND B.CANCELLED=0"; //B.APPROVED=1
                    if(data.IsRecordExist(Check,FinanceGlobal.FinURL)) {
                        rsTmp.next();
                        continue;
                    }
                    
                    //                    if(!ValidateMonth(MaturityDate,EITLERPGLOBAL.formatDateDB(EffectiveDate))) {
                    //                        rsTmp.next();
                    //                        continue;
                    //                    }
                    
                    Counter++;
                    int Months = rsTmp.getInt("INTEREST_CALCULATION_PERIOD");
                    double Amount = rsTmp.getDouble("AMOUNT");
                    double Rate = rsTmp.getDouble("INTEREST_RATE");
                    double nyear = Months/12.0;
                    double nTimes = 12.0/Months;
                    double TDSAmount = 0.0;
                    double interestAmount=0.0;
                    double partyInterest=0.0;
                    double currentInterest = 0.0;
                    int InterestCalcType = rsTmp.getInt("INTEREST_CALCULATION_TYPE");
                    
                    /* Interest calculation begins from here according to interest calculation type.
                     * Interest Amount will be calculated according to period.
                     * Period is considered up to interest calculation date(INT_CALC_DATE in database).
                     */
                    
                    //=============Start of Interest Calculation =================//
                    if(InterestCalcType==2) {
                        //============For Cumulative Interest calculation===============//
                        /*if(data.getIntValueFromDB("SELECT TAX_EX_FORM_RECEIVED FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL)==0) {
                            partyInterest = clsDepositMaster.checkTDSAmount(rsTmp.getString("PARTY_CODE"));
                        }*/
                        if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FD_TAX_FORM_RECEIVED WHERE PARTY_CODE='"+Partycode+"' AND RECEIVED_DATE>='"+FinStartDate+"' AND RECEIVED_DATE<='"+FinEndDate+"' ",FinanceGlobal.FinURL)) {
                            partyInterest = clsDepositMaster.checkTDSAmount(rsTmp.getString("PARTY_CODE"));
                        }
                        String SQLQuery = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ ReceiptNo +"' ";
                        if(data.IsRecordExist(SQLQuery,FinanceGlobal.FinURL)) {
                            /** Add previous amounts to calculate next interest amount for cumulative interest And
                             *  change Receipt Date for date difference if record already exits
                             */
                            SQLQuery = "SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A,D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='" + ReceiptNo +"' AND A.PARTY_CODE='"+Partycode+"' AND A.DOC_NO=B.DOC_NO AND B.APPROVED=1 AND B.TDS_ONLY=0 ORDER BY WARRANT_DATE";
                            ResultSet rsResult = data.getResult(SQLQuery,FinanceGlobal.FinURL);
                            rsResult.first();
                            if(rsResult.getRow() > 0) {
                                while(!rsResult.isAfterLast()){
                                    Amount = EITLERPGLOBAL.round(Amount + rsResult.getDouble("INTEREST_AMOUNT"),0);
                                    eDate = addMonthToDate(eDate, Months);
                                    rsResult.next();
                                }
                            }
                            rsResult.close();
                            eDate = EITLERPGLOBAL.addDaysToDate(eDate,1,"yyyy-MM-dd");
                        }
                        
                        //interestAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,2); //FV = P*(1+r) raised to y
                        interestAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0); //FV = P*(1+r) raised to y
                        
                        //=============End of cumulative Interest calculation==============//
                    } else {
                        //=============For Fixed Interest calculation==================//
                        /*if(data.getIntValueFromDB("SELECT TAX_EX_FORM_RECEIVED FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+rsTmp.getString("RECEIPT_NO")+"' ",FinanceGlobal.FinURL)==0) {
                            partyInterest = clsDepositMaster.checkTDSAmount(rsTmp.getString("PARTY_CODE"));
                        }*/
                        
                        if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FD_TAX_FORM_RECEIVED WHERE PARTY_CODE='"+Partycode+"' AND RECEIVED_DATE>='"+FinStartDate+"' AND RECEIVED_DATE<='"+FinEndDate+"' ",FinanceGlobal.FinURL)) {
                            partyInterest = clsDepositMaster.checkTDSAmount(rsTmp.getString("PARTY_CODE"));
                        }
                        long totalDays=1;
                        String SQLQuery = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ ReceiptNo +"' ";
                        if(data.IsRecordExist(SQLQuery,FinanceGlobal.FinURL)){
                            //change Receipt Date for Date Difference if record already exits
                            SQLQuery = "SELECT INTEREST_DAYS FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='" + ReceiptNo +"' AND A.PARTY_CODE='"+Partycode+"' AND A.DOC_NO=B.DOC_NO AND B.APPROVED=1 ORDER BY WARRANT_DATE";
                            ResultSet rsResult = data.getResult(SQLQuery,FinanceGlobal.FinURL);
                            rsResult.first();
                            if(rsResult.getRow() > 0) {
                                while(!rsResult.isAfterLast()) {
                                    eDate = EITLERPGLOBAL.addDaysToDate(eDate, rsResult.getInt("INTEREST_DAYS"),"yyyy-MM-dd");
                                    rsResult.next();
                                }
                                rsResult.close();
                            }
                        }
                        totalDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(eDate), java.sql.Date.valueOf(interestCalcDate)) +1;
                        
                        GregorianCalendar cal = new GregorianCalendar();
                        //if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1)) {
                        if(cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearStartDate(interestCalcDate).substring(0,4))+1)) {
                            interestAmount = EITLERPGLOBAL.round(((Amount * Rate * totalDays)/(366* 100)),2); //INTEREST AMOUNT = (AMOUNT * RATE)/100
                        } else {
                            interestAmount = EITLERPGLOBAL.round(((Amount * Rate * totalDays)/(365* 100)),2); //INTEREST AMOUNT = (AMOUNT * RATE)/100
                        }
                        
                        //=============End of Fixed Interest calculation==================//
                    }
                    
                    //=============End of Interest Calculation =================//
                    
                    //======= Start collecting calculated data ================//
                    
                    SrNo++;
                    
                    ObjItem.setAttribute("SR_NO",SrNo);
                    ObjItem.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                    ObjItem.setAttribute("INTEREST_CALCULATION_TYPE",rsTmp.getInt("INTEREST_CALCULATION_TYPE"));
                    ObjItem.setAttribute("INT_MAIN_ACCOUNT_CODE",rsTmp.getString("INT_MAIN_ACCOUNT_CODE"));
                    ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                    ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                    ObjItem.setAttribute("INTEREST_RATE",rsTmp.getDouble("INTEREST_RATE"));
                    
                    if(partyInterest > 5000.0) {
                        if(InterestCalcType == 2) {
                            currentInterest = clsCalcInterest.getCurrentInterest(ReceiptNo,interestCalcDate);
                            TDSAmount = clsDepositMaster.calculateTDSAmount(currentInterest);
                        } else {
                            TDSAmount = clsDepositMaster.calculateTDSAmount(interestAmount);
                        }
                    }
                    
                    ObjItem.setAttribute("INTEREST_AMOUNT",interestAmount);
                    double netInterest = 0;
                    if(InterestCalcType==2) {
                        netInterest = EITLERPGLOBAL.round(interestAmount - TDSAmount,0);
                    } else {
                        netInterest = EITLERPGLOBAL.round(interestAmount - TDSAmount,2);
                    }
                    
                    ObjItem.setAttribute("TDS_AMOUNT",TDSAmount);
                    ObjItem.setAttribute("NET_INTEREST",netInterest);
                    
                    int DateDiffInDays = (int)EITLERPGLOBAL.DateDiff(EITLERPGLOBAL.ConvertDate(eDate), EITLERPGLOBAL.ConvertDate(interestCalcDate)) + 1;
                    
                    /* If deposit type is cumulative and it is last calculation for cumulative deposit then and only then Warrant No.
                     * should be generated and Warrant Date is Maturity Date otherwise Warrant No. should not be generated.
                     * If deposit type is fixed and it is last calculation for deposit then Warrant Date is MaturityDate.
                     * else Warrant No. is generated appropriately and Warrant Date is interest calculation date.
                     */
                    
                    //============== Start of Warrant Generation ==============//
                    String tmpDate = interestCalcDate;
                    tmpDate = EITLERPGLOBAL.addDaysToDate(tmpDate, 1, "yyyy-MM-dd");
                    
                    if(InterestCalcType == 2) {
                        if(java.sql.Date.valueOf(tmpDate).compareTo(java.sql.Date.valueOf(MaturityDate)) == 0 ) {
                            if(!updated) {
                                previousWarrantNo = getMaxWarrantNo(interestCalcDate);
                                updated=true;
                            } else {
                                previousWarrantNo = getNextWarrantNo(previousWarrantNo,interestCalcDate);
                            }
                            ObjItem.setAttribute("WARRANT_NO", previousWarrantNo);
                            ObjItem.setAttribute("LEGACY_WARRANT_NO", LegacyWarrantNo);
                            LegacyWarrantNo++;
                            ObjItem.setAttribute("WARRANT_DATE", MaturityDate);
                            ObjItem.setAttribute("MICR_NO", 0);
                            ObjItem.setAttribute("WARRANT_CLEAR", "I");
                        } else {
                            ObjItem.setAttribute("WARRANT_NO", "0000000");
                            ObjItem.setAttribute("WARRANT_DATE", interestCalcDate);
                            ObjItem.setAttribute("MICR_NO", 0);
                            ObjItem.setAttribute("WARRANT_CLEAR", "N");
                        }
                    } else {
                        if(!updated) {
                            previousWarrantNo = getMaxWarrantNo(interestCalcDate);
                            updated=true;
                        } else {
                            previousWarrantNo = getNextWarrantNo(previousWarrantNo,interestCalcDate);
                        }
                        ObjItem.setAttribute("WARRANT_NO", previousWarrantNo);
                        ObjItem.setAttribute("LEGACY_WARRANT_NO", LegacyWarrantNo);
                        LegacyWarrantNo++;
                        if(java.sql.Date.valueOf(tmpDate).compareTo(java.sql.Date.valueOf(MaturityDate)) == 0 ) {
                            ObjItem.setAttribute("WARRANT_DATE", MaturityDate);
                        } else {
                            ObjItem.setAttribute("WARRANT_DATE", interestCalcDate);
                        }
                        ObjItem.setAttribute("MICR_NO", 0);
                        ObjItem.setAttribute("WARRANT_CLEAR", "I");
                    }
                    //============== End of Warrant Generation ==============//
                    
                    ObjItem.setAttribute("INTEREST_DAYS",DateDiffInDays);
                    ObjItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                    ObjItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                    ObjItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("CHANGED",true);
                    ObjItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("CANCELED",false);
                    
                    //Put into list
                    List.put(Integer.toString(Counter),ObjItem);
                    rsTmp.next();
                }
            }
            return List;
        } catch(Exception e) {
            e.printStackTrace();
            return List;
        }
    }
    
    public static String addMonthToDate(String tmpDate, int months) {
        String DATE_FORMAT = "yyyy-MM-dd";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(tmpDate.substring(0,4)), Integer.parseInt(tmpDate.substring(5,7))-1, Integer.parseInt(tmpDate.substring(8,10)));
        
        // Add months to the calendar
        cal.add(Calendar.MONTH, months);
        
        //Substract 1 Day from Calendar
        //cal.add(Calendar.DATE, -1);
        
        return sdf.format(cal.getTime());
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
                strSQL="SELECT FINANCE.D_FD_INT_CALC_HEADER.DOC_NO,FINANCE.D_FD_INT_CALC_HEADER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_INT_CALC_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_INT_CALC_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_INT_CALC_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_INT_CALC_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID=86 ORDER BY "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_INT_CALC_HEADER.DOC_NO,FINANCE.D_FD_INT_CALC_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_INT_CALC_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_INT_CALC_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_INT_CALC_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_INT_CALC_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID=86 ORDER BY FINANCE.D_FD_INT_CALC_HEADER.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_INT_CALC_HEADER.DOC_NO,FINANCE.D_FD_INT_CALC_HEADER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_INT_CALC_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_INT_CALC_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FD_INT_CALC_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FD_INT_CALC_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID=86 ORDER BY FINANCE.D_FD_INT_CALC_HEADER.DOC_NO";
            }
            
            //strSQL="SELECT D_PUR_INQUIRY_HEADER.INQUIRY_NO,D_PUR_INQUIRY_HEADER.INQUIRY_DATE FROM D_PUR_INQUIRY_HEADER,D_COM_DOC_DATA WHERE D_PUR_INQUIRY_HEADER.INQUIRY_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_INQUIRY_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_INQUIRY_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=86";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                
                Counter=Counter+1;
                clsCalcInterest ObjDoc=new clsCalcInterest();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjDoc);
                
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//End of While
            
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
            rsInterest=Stmt.executeQuery("SELECT * FROM FINANCE.D_FD_INT_CALC_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FD_INT_CALC_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsCalcInterest objCalcInterest=new clsCalcInterest();
                
                objCalcInterest.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                objCalcInterest.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                objCalcInterest.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                objCalcInterest.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                objCalcInterest.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                objCalcInterest.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),objCalcInterest);
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_INT_CALC_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FD_INT_CALC_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND CANCELLED=0");
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
        if(!data.IsRecordExist("SELECT * FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+getAttribute("BOOK_CODE").getString()+"'",FinanceGlobal.FinURL)) {
            LastError = "Please specify Book Code.";
            return false;
        }
        return true;
    }
    
    public static String getMaxWarrantNo(String interestCalcDate) {
        String  newWarrantNo="";
        String FinYear = "";
        try {
            int intMonth = Integer.parseInt(interestCalcDate.substring(5,7));
            int intYear = Integer.parseInt(interestCalcDate.substring(0,4));
            if(intMonth>=4) {//April
                FinYear = Integer.toString(intYear);
            }
            else {
                intYear--;
                FinYear = Integer.toString(intYear);
            }
            FinYear= FinYear.substring(2,4);
            String strSQL="SELECT MAX(SUBSTRING(B.WARRANT_NO,LENGTH('"+FinYear+"')+1)) AS MAX_NO FROM D_FD_INT_CALC_HEADER A,D_FD_INT_CALC_DETAIL B WHERE A.DOC_NO=B.DOC_NO AND B.WARRANT_NO LIKE '"+FinYear+"%' AND A.APPROVED=1";
            int MaxNo=UtilFunctions.CInt(data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL))+1;
            String strMaxNo=Integer.toString(MaxNo);
            strMaxNo=EITLERPGLOBAL.Replicate("0", 5-strMaxNo.length())+strMaxNo;
            newWarrantNo=FinYear+strMaxNo;
        } catch (Exception e) {
        }
        return newWarrantNo;
    }
    
    public static String getNextWarrantNo(String previousWarrantNo,String interestCalcDate) {
        String  nextWarrantNo="";
        String FinYear = "";
        int nextNo = 0;
        try {
            int intMonth = Integer.parseInt(interestCalcDate.substring(5,7));
            int intYear = Integer.parseInt(interestCalcDate.substring(0,4));
            if(intMonth>=4) {//April
                FinYear = Integer.toString(intYear);
            }
            else {
                intYear--;
                FinYear = Integer.toString(intYear);
            }
            FinYear= FinYear.substring(2,4);
            if(FinYear.equals(previousWarrantNo.substring(0,2))) {
                nextNo = Integer.parseInt(previousWarrantNo.substring(2,7)) + 1;
            } else {
                String strSQL="SELECT MAX(SUBSTRING(WARRANT_NO,LENGTH('"+FinYear+"')+1)) AS MAX_NO FROM D_FD_INT_CALC_DETAIL WHERE WARRANT_NO LIKE '"+FinYear+"%'";
                nextNo = UtilFunctions.CInt(data.getStringValueFromDB(strSQL,FinanceGlobal.FinURL))+1;
            }
            String strNextNo=Integer.toString(nextNo);
            strNextNo=EITLERPGLOBAL.Replicate("0", 5-strNextNo.length())+strNextNo;
            nextWarrantNo=FinYear+strNextNo;
        } catch (Exception e) {
        }
        return nextWarrantNo;
    }
    
    public static String getNextInterestDate(int CompanyID, String ReceiptNo) {
        String nextInterestCalcDate="";
        try {
            int DepositType = data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"", FinanceGlobal.FinURL);
            String InterestCalcDate = data.getStringValueFromDB("SELECT INT_CALC_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
            String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
            
            String checkDate = EITLERPGLOBAL.addDaysToDate(InterestCalcDate, 1, "yyyy-MM-dd");
            if(java.sql.Date.valueOf(checkDate).compareTo(java.sql.Date.valueOf(MaturityDate)) == 0 ) {
                return InterestCalcDate;
            }
            
            if(DepositType == 2) {
                int InterestCalcPeriod = data.getIntValueFromDB("SELECT A.INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER A, D_FD_DEPOSIT_MASTER B WHERE A.COMPANY_ID=B.COMPANY_ID AND B.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.SCHEME_ID=B.SCHEME_ID AND B.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                nextInterestCalcDate = addMonthToDate(InterestCalcDate,InterestCalcPeriod);
                if(java.sql.Date.valueOf(nextInterestCalcDate).compareTo(java.sql.Date.valueOf(MaturityDate))==0 || java.sql.Date.valueOf(nextInterestCalcDate).after(java.sql.Date.valueOf(MaturityDate))) {
                    nextInterestCalcDate = clsDepositMaster.deductDays(MaturityDate, 1);
                }
            } else if(DepositType==1) {
                boolean Find = true;
                String tmpDate = "";
                int EffectiveYear = EITLERPGLOBAL.getYear(InterestCalcDate);
                ResultSet rsDate = data.getResult("SELECT * FROM D_FD_INT_CALC_DATE ORDER BY INTEREST_MONTH",FinanceGlobal.FinURL);
                rsDate.first();
                
                while(!rsDate.isAfterLast() && Find) {
                    tmpDate = EffectiveYear +"-"+ rsDate.getString("INTEREST_MONTH") +"-"+ rsDate.getString("INTEREST_DAYS");
                    if(java.sql.Date.valueOf(tmpDate).after(java.sql.Date.valueOf(InterestCalcDate))) {
                        nextInterestCalcDate=tmpDate;
                        Find=false;
                    }
                    rsDate.next();
                }
                
                if(Find) {
                    rsDate.first();
                    EffectiveYear++;
                    while(!rsDate.isAfterLast() && Find){
                        tmpDate = EffectiveYear +"-"+ rsDate.getString("INTEREST_MONTH") +"-"+ rsDate.getString("INTEREST_DAYS");
                        if(java.sql.Date.valueOf(tmpDate).after(java.sql.Date.valueOf(InterestCalcDate))) {
                            nextInterestCalcDate=tmpDate;
                            Find=false;
                        }
                        rsDate.next();
                    }
                }
                
                if(java.sql.Date.valueOf(nextInterestCalcDate).compareTo(java.sql.Date.valueOf(MaturityDate))==0 || java.sql.Date.valueOf(nextInterestCalcDate).after(java.sql.Date.valueOf(MaturityDate))) {
                    nextInterestCalcDate = clsDepositMaster.deductDays(MaturityDate,1);
                }
            }
        } catch(Exception e) {
            return nextInterestCalcDate;
        }
        return nextInterestCalcDate;
    }
    
    public static double getCurrentInterest(String ReceiptNo, String IntCalcDate) {
        double currentInterest = 0.0;
        try {
            String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()) +"-04-01";
            String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1) +"-03-31";
            String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            int Months = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER A, D_FD_DEPOSIT_MASTER B WHERE A.SCHEME_ID=B.SCHEME_ID AND B.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            double Rate = data.getDoubleValueFromDB("SELECT INTEREST_RATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String SQLQuery = "";
            int Days = 1;
            if(java.sql.Date.valueOf(EffectiveDate).after(java.sql.Date.valueOf(StartFinYear))) {
                Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(IntCalcDate))+1;
            } else {
                if(data.IsRecordExist("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND WARRANT_DATE>='"+StartFinYear+"' AND WARRANT_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL)) {
                    SQLQuery = "SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='" + ReceiptNo +"' AND A.DOC_NO=B.DOC_NO  AND  B.TDS_ONLY=0 AND A.PARTY_CODE='"+PartyCode+"'";
                    ResultSet rsResult = data.getResult(SQLQuery,FinanceGlobal.FinURL);
                    rsResult.first();
                    while(!rsResult.isAfterLast()){
                        Amount += rsResult.getDouble("INTEREST_AMOUNT");
                        EffectiveDate = addMonthToDate(EffectiveDate, Months);
                        rsResult.next();
                    }
                    rsResult.close();
                    EffectiveDate = EITLERPGLOBAL.addDaysToDate(EffectiveDate,1,"yyyy-MM-dd");
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(IntCalcDate))+1;
                } else {
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(IntCalcDate))+1;
                }
            }
            GregorianCalendar cal = new GregorianCalendar();
            if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1)) {
                //if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                currentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(366* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
            } else {
                currentInterest = EITLERPGLOBAL.round(((Amount * Rate * Days)/(365* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
            }
        } catch(Exception e) {
        }
        return currentInterest;
    }
    
    private static boolean ValidateMonth(String mDate, String eDate) {
        try {
            int mMonth = EITLERPGLOBAL.getMonth(mDate);
            int eMonth = EITLERPGLOBAL.getMonth(eDate);
            if(mMonth > eMonth) {
                return false;
            }
        }catch(Exception e) {
        }
        return true;
    }
    
    private static long getNextLegacyWarrantNo(String StartDate) {
        long legacyNo = 0;
        try {
            String FinStartDate = EITLERPGLOBAL.getFinYearStartDate(StartDate);
            String FinEndDate = EITLERPGLOBAL.getFinYearEndDate(StartDate);
            String strSQL="SELECT MAX(LEGACY_WARRANT_NO) AS MAX_NO FROM D_FD_INT_CALC_HEADER A, D_FD_INT_CALC_DETAIL B " +
            "WHERE A.DOC_NO=B.DOC_NO AND B.WARRANT_DATE>='"+FinStartDate+"' AND B.WARRANT_DATE<='"+FinEndDate+"' " +
            "AND A.TDS_ONLY<>1 AND A.APPROVED=1 AND A.CANCELLED=0 ";
            System.out.println(strSQL);
            legacyNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL)+1;
        }catch(Exception e) {
        }
        return legacyNo;
    }
    //fd premature activities start
    public void generateWarrant() {
        Connection tmpConn;
        Statement stTmp=null;
        ResultSet rsTmp=null;
        int Counter=0;
        HashMap List=new HashMap();
        String strSQL="";
        String previousWarrantNo = "";
        boolean updated = false;
        long LegacyWarrantNo=0;
        
        try {            
            String MonthStartDate = "2015-03-01";
            LegacyWarrantNo = getNextLegacyWarrantNo(MonthStartDate);
            System.out.println(LegacyWarrantNo);            
            tmpConn=data.getConn(FinanceGlobal.FinURL);            
            strSQL = "SELECT A.RECEIPT_NO, A.RECEIPT_DATE, A.EFFECTIVE_DATE, A.AMOUNT, A.DEPOSIT_PERIOD, A.INTEREST_RATE, A.INT_CALC_DATE, B.INT_MAIN_ACCOUNT_CODE, A.PARTY_CODE, A.MAIN_ACCOUNT_CODE, A.MATURITY_DATE, B.INTEREST_CALCULATION_PERIOD, B.INTEREST_CALCULATION_TYPE, CASE WHEN B.SCHEME_TYPE=1 THEN B.SCHEME_TYPE+5 WHEN B.SCHEME_TYPE=2 THEN B.SCHEME_TYPE WHEN B.SCHEME_TYPE=3 THEN B.SCHEME_TYPE+10 ELSE 0 END AS SCHEME_ORDER FROM D_FD_DEPOSIT_MASTER A, D_FD_SCHEME_MASTER B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.COMPANY_ID=2 AND A.SCHEME_ID=B.SCHEME_ID AND B.SCHEME_TYPE=3 AND A.DEPOSIT_STATUS=0 AND A.MATURITY_DATE>'2015-03-31' AND A.APPROVED=1 AND A.CANCELLED=0 ORDER BY A.RECEIPT_DATE";            
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            int SrNo=0;
            if(rsTmp.getRow()>0){
                while(!rsTmp.isAfterLast()) {
                    clsCalcInterestItem ObjItem=new clsCalcInterestItem();
                    String ReceiptNo = rsTmp.getString("RECEIPT_NO");
                    String eDate = rsTmp.getString("EFFECTIVE_DATE");
                    String MaturityDate = UtilFunctions.getString(rsTmp,"MATURITY_DATE","0000-00-00");
                    String interestCalcDate = UtilFunctions.getString(rsTmp,"INT_CALC_DATE","0000-00-00");
                    String Partycode = UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                    System.out.println(ReceiptNo);
                    System.out.println(eDate);
                    System.out.println(MaturityDate);
                    System.out.println(interestCalcDate);
                    System.out.println(Partycode);                                        
                    String Check = "SELECT A.* FROM D_FD_INT_CALC_DETAIL A,D_FD_INT_CALC_HEADER B " +
                    "WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+Partycode+"' AND (A.WARRANT_DATE='"+MaturityDate+"' OR A.WARRANT_DATE='"+interestCalcDate+"') AND A.DOC_NO=B.DOC_NO AND B.CANCELLED=0"; //B.APPROVED=1
                    if(data.IsRecordExist(Check,FinanceGlobal.FinURL)) {
                        rsTmp.next();
                        continue;
                    }
                    
                    //                    if(!ValidateMonth(MaturityDate,EITLERPGLOBAL.formatDateDB(EffectiveDate))) {
                    //                        rsTmp.next();
                    //                        continue;
                    //                    }
                    
                    Counter++;
                    int Months = rsTmp.getInt("INTEREST_CALCULATION_PERIOD");
                    double Amount = rsTmp.getDouble("AMOUNT");
                    double Rate = rsTmp.getDouble("INTEREST_RATE");
                    double nyear = Months/12.0;
                    double nTimes = 12.0/Months;
                    double TDSAmount = 0.0;
                    double interestAmount=0.0;
                    double partyInterest=0.0;
                    double currentInterest = 0.0;
                    int InterestCalcType = rsTmp.getInt("INTEREST_CALCULATION_TYPE");
                    System.out.println(Months);
                    System.out.println(Amount);
                    System.out.println(Rate);
                    System.out.println(nyear);
                    System.out.println(nTimes);
                    System.out.println(InterestCalcType);                    
                                        
                    /* Interest calculation begins from here according to interest calculation type.
                     * Interest Amount will be calculated according to period.
                     * Period is considered up to interest calculation date(INT_CALC_DATE in database).
                     */                                        
                    
                    //======= Start collecting calculated data ================//
                    
                    SrNo++;                    
                    ObjItem.setAttribute("SR_NO",SrNo);
                    ObjItem.setAttribute("RECEIPT_NO",rsTmp.getString("RECEIPT_NO"));
                    ObjItem.setAttribute("INTEREST_CALCULATION_TYPE",rsTmp.getInt("INTEREST_CALCULATION_TYPE"));
                    ObjItem.setAttribute("INT_MAIN_ACCOUNT_CODE",rsTmp.getString("INT_MAIN_ACCOUNT_CODE"));
                    ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                    ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                    ObjItem.setAttribute("INTEREST_RATE",rsTmp.getDouble("INTEREST_RATE"));
                    System.out.println(SrNo);
                    System.out.println(rsTmp.getString("RECEIPT_NO"));
                    System.out.println(rsTmp.getInt("INTEREST_CALCULATION_TYPE"));
                    System.out.println(rsTmp.getString("INT_MAIN_ACCOUNT_CODE"));
                    System.out.println(rsTmp.getString("PARTY_CODE"));
                    System.out.println(rsTmp.getString("MAIN_ACCOUNT_CODE"));
                    System.out.println(rsTmp.getDouble("INTEREST_RATE"));
                    /*
                    if(partyInterest > 5000.0) {
                        if(InterestCalcType == 2) {
                            currentInterest = clsCalcInterest.getCurrentInterest(ReceiptNo,interestCalcDate);
                            TDSAmount = clsDepositMaster.calculateTDSAmount(currentInterest);
                        } else {
                            TDSAmount = clsDepositMaster.calculateTDSAmount(interestAmount);
                        }
                    } 
                     */                   
                    //ObjItem.setAttribute("INTEREST_AMOUNT",interestAmount);
                    /*double netInterest = 0;
                    if(InterestCalcType==2) {
                        netInterest = EITLERPGLOBAL.round(interestAmount - TDSAmount,0);
                    } else {
                        netInterest = EITLERPGLOBAL.round(interestAmount - TDSAmount,2);
                    }
                    
                     */
                    //ObjItem.setAttribute("TDS_AMOUNT",TDSAmount);
                    //ObjItem.setAttribute("NET_INTEREST",netInterest);
                    
                    //int DateDiffInDays = (int)EITLERPGLOBAL.DateDiff(EITLERPGLOBAL.ConvertDate(eDate), EITLERPGLOBAL.ConvertDate(interestCalcDate)) + 1;
                    
                    /* If deposit type is cumulative and it is last calculation for cumulative deposit then and only then Warrant No.
                     * should be generated and Warrant Date is Maturity Date otherwise Warrant No. should not be generated.
                     * If deposit type is fixed and it is last calculation for deposit then Warrant Date is MaturityDate.
                     * else Warrant No. is generated appropriately and Warrant Date is interest calculation date.
                     */
                    
                    //============== Start of Warrant Generation ==============//
                    //String tmpDate = interestCalcDate;
                    //tmpDate = EITLERPGLOBAL.addDaysToDate(tmpDate, 1, "yyyy-MM-dd");                    
              
                       
                            if(!updated) {
                                //previousWarrantNo = getMaxWarrantNo(interestCalcDate);
                                previousWarrantNo = getMaxWarrantNo("2015-03-15");
                                updated=true;
                            } else {
                                previousWarrantNo = getNextWarrantNo(previousWarrantNo,"2015-03-15");
                            }
                    System.out.println(previousWarrantNo);
                    System.out.println(LegacyWarrantNo);
                            ObjItem.setAttribute("WARRANT_NO", previousWarrantNo);
                            ObjItem.setAttribute("LEGACY_WARRANT_NO", LegacyWarrantNo);
                            LegacyWarrantNo++;
                            ObjItem.setAttribute("WARRANT_DATE", MaturityDate);
                            ObjItem.setAttribute("MICR_NO", 0);
                            ObjItem.setAttribute("WARRANT_CLEAR", "I");
                            data.Execute("UPDATE FINANCE.D_FD_INT_CALC_DETAIL SET WARRANT_NO='"+previousWarrantNo+"',LEGACY_WARRANT_NO='"+LegacyWarrantNo+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+Partycode+"' AND DOC_NO='000102' ",FinanceGlobal.FinURL);
                            //data.Execute("UPDATE D_FD_INT_CALC_DETAIL SET WARRANT_NO='"+previousWarrantNo+"',LEGACY_WARRANT_NO='"+LegacyWarrantNo+"',WARRANT_DATE='"+MaturityDate+"',WARRANT_CLEAR='"I"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+Partycode+"'",FinanceGlobal.FinURL);
                        
                    
                    //============== End of Warrant Generation ==============//
                    /*
                    ObjItem.setAttribute("INTEREST_DAYS",DateDiffInDays);
                    ObjItem.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
                    ObjItem.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
                    ObjItem.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("CHANGED",true);
                    ObjItem.setAttribute("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    ObjItem.setAttribute("CANCELED",false);
                    
                    //Put into list
                    List.put(Integer.toString(Counter),ObjItem);
                     */
                    rsTmp.next();
                     
                }
            }
            
        } catch(Exception e) {
            e.printStackTrace();
           
        }
       
        try{
        //if(data.getIntValueFromDB("SELECT TDS_ONLY FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='000102'",FinanceGlobal.FinURL)==0) {
                    ResultSet rsItem = data.getResult("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE DOC_NO='000102' AND WARRANT_NO<>'0000000' ORDER BY WARRANT_NO",FinanceGlobal.FinURL);
                    rsItem.first();
                    if(rsItem.getRow()>0){
                        while(!rsItem.isAfterLast()) {
                            int CompanyID = rsItem.getInt("COMPANY_ID");
                            String ReceiptNo = rsItem.getString("RECEIPT_NO");
                            double Amount = rsItem.getDouble("INTEREST_AMOUNT");
                            
                            
                            String curDocNo = rsItem.getString("DOC_NO");
                            //if(PostVoucher24(CompanyID,ReceiptNo,Amount,curDocNo)) {
                                if(PostVoucher87(CompanyID,ReceiptNo,Amount,curDocNo)) {
                                    String nextInterestCalcDate = getNextInterestDate(CompanyID, ReceiptNo);
                                    //data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                                    //data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+nextInterestCalcDate+"' WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ", FinanceGlobal.FinURL);
                                    
                                } else {
                                    LastError += "\n Error while posting voucher for book code = " + getAttribute("BOOK_CODE").getString()+"  RECEIPT NO ="+ReceiptNo;;
                                    //DonePosting=false;
                                    //return false;
                                }
                            /*} else {
                                LastError += "\n Error while posting voucher for book code = 24 RECEIPT NO ="+ReceiptNo;
                                //DonePosting=false;
                                //return false;
                            }
                             */
                            rsItem.next();
                        }
                    }                    
       //}
        }catch(Exception e) {
            e.printStackTrace();
           
        }
    }
    
    private boolean PostVoucher87(int CompanyID, String ReceiptNo, double InterestAmount, String curDocNo) {
        try {
            String MaturityDate ="";
            String InterestDate ="";
            double TDSAmount = 0.0;
            double GrossAmount = 0.0;
            clsDepositMaster ObjDepositMaster=(clsDepositMaster)(new clsDepositMaster()).getObject(CompanyID,ReceiptNo);
            String PartyCode = ObjDepositMaster.getAttribute("PARTY_CODE").getString();
            if(data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL)== 2 ) {
                InterestDate = data.getStringValueFromDB("SELECT WARRANT_DATE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
                GrossAmount = data.getDoubleValueFromDB("SELECT SUM(A.INTEREST_AMOUNT) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                //InterestAmount = data.getDoubleValueFromDB("SELECT SUM(A.NET_INTEREST) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                TDSAmount = data.getDoubleValueFromDB("SELECT SUM(A.TDS_AMOUNT) AS TOTAL FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+ReceiptNo+"' AND A.PARTY_CODE='"+PartyCode+"' AND A.DOC_NO=B.DOC_NO ",FinanceGlobal.FinURL);
                InterestAmount = EITLERPGLOBAL.round(GrossAmount - TDSAmount+InterestAmount,2);
            } else {
                InterestDate = data.getStringValueFromDB("SELECT WARRANT_DATE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            }
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            
            // --- Start Common Elements --- //
            clsVoucher objVoucher=new clsVoucher();
            
            setAttribute("FIN_HIERARCHY_ID",0);
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "1");
            
            if(List.size()>0) {
                // Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.PaymentModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            // --- End Common Elements --- //
            
            //--- VOUCHER POSTING FOR BOOK CODE SECOND VOUCHER ---//
            
            //======== Gethering Requiered Data ========//
            int VoucherSrNo=0;
            String BookCode ="87"; //data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);//getAttribute("BOOK_CODE").getString();
            String curDocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FD_INT_CALC_HEADER WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);//getAttribute("BOOK_CODE").getString();
            String BankMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String WarrantNo = data.getStringValueFromDB("SELECT WARRANT_NO FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String MainAccountCode="";
            List.clear();
            if(data.getIntValueFromDB("SELECT SCHEME_TYPE FROM D_FD_DEPOSIT_MASTER A, D_FD_SCHEME_MASTER B WHERE A.SCHEME_ID=B.SCHEME_ID AND A.RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL)==3) {
                int DepositerCategory = data.getIntValueFromDB("SELECT DEPOSITER_CATEGORY FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                if(DepositerCategory == 2) {
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "SHAREHOLDER_ACCOUNT_CODE", "");
                } else {
                    List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "PUBLIC_ACCOUNT_CODE", "");
                }
                if(List.size()>0) {
                    //Get cumulative interest payable account.
                    clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                    MainAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
                }
                //MainAccountCode = "115201";
                //MainAccountCode = "115160";
            } else {
                MainAccountCode = ObjDepositMaster.getAttribute("MAIN_ACCOUNT_CODE").getString();
            }
            
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            //======== End of Gethering Requiered Data ========//
            
            //=======Preparing Voucher Header ============//
            objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); //24
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_PAYMENT); // pyament
            objVoucher.setAttribute("CHEQUE_NO",WarrantNo); //
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(InterestDate));
            objVoucher.setAttribute("BANK_NAME",BankName);
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(InterestDate));// interest calc date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); // FOR CD INTEREST - 115160/115201
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(curDocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsCalcInterest.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",BankMainCode); //Bank Main Account Code
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",InterestAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",curDocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(curDocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsCalcInterest.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            // ====== End of Voucher Detail =======//
            
            //--- VOUCHER POSTING FOR BOOK CODE SECOND VOUCHER ---//
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            } else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
     //fd premature activities end
}