package EITLERP.Finance;

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
 * @author  nrpithva
 * @version
 */

public class clsDepositPM {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=88;
    
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
    public clsDepositPM() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DOC_NO",new Variant(""));
        props.put("FFNO", new Variant(0));
        props.put("DOC_DATE",new Variant(""));
        props.put("RECEIPT_NO",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("CHEQUE_NO",new Variant(""));
        props.put("CHEQUE_DATE",new Variant(""));
        props.put("BOOK_CODE",new Variant(""));
        props.put("EFFECTIVE_DATE",new Variant(""));
        props.put("PRINCIPLE_AMOUNT", new Variant(0.0));
        props.put("INTEREST_RATE", new Variant(0.0));
        props.put("PAID_TDS", new Variant(0.0));
        props.put("CURRENT_TDS", new Variant(0.0));
        props.put("BROKERAGE", new Variant(0.0));
        props.put("INTEREST_AMOUNT", new Variant(0.0));
        props.put("PAID_INTEREST", new Variant(0.0));
        props.put("NET_PAYABLE_AMOUNT", new Variant(0.0));
        props.put("REMARKS",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("STATUS", new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_DEPOSIT_PM WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DOC_NO");
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
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_DEPOSIT_PM_H WHERE DOC_NO='1' ");
            rsHistory.first();
            //------------------------------------//
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate New Doc No ------------
            String DocNo = clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, clsDepositPM.ModuleID , getAttribute("FFNO").getInt(),  true);
            setAttribute("DOC_NO",DocNo);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            //------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsResultSet.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsResultSet.updateDouble("PRINCIPLE_AMOUNT", getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsResultSet.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsResultSet.updateDouble("PAID_TDS", getAttribute("PAID_TDS").getDouble());
            rsResultSet.updateDouble("CURRENT_TDS", getAttribute("CURRENT_TDS").getDouble());
            rsResultSet.updateDouble("BROKERAGE", getAttribute("BROKERAGE").getDouble());
            rsResultSet.updateDouble("INTEREST_AMOUNT", getAttribute("INTEREST_AMOUNT").getDouble());
            rsResultSet.updateDouble("PAID_INTEREST", getAttribute("PAID_INTEREST").getDouble());
            rsResultSet.updateDouble("NET_PAYABLE_AMOUNT", getAttribute("NET_PAYABLE_AMOUNT").getDouble());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",false);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsHistory.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateDouble("PRINCIPLE_AMOUNT", getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsHistory.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsHistory.updateDouble("PAID_TDS", getAttribute("PAID_TDS").getDouble());
            rsHistory.updateDouble("CURRENT_TDS", getAttribute("CURRENT_TDS").getDouble());
            rsHistory.updateDouble("BROKERAGE", getAttribute("BROKERAGE").getDouble());
            rsHistory.updateDouble("INTEREST_AMOUNT", getAttribute("INTEREST_AMOUNT").getDouble());
            rsHistory.updateDouble("PAID_INTEREST", getAttribute("PAID_INTEREST").getDouble());
            rsHistory.updateDouble("NET_PAYABLE_AMOUNT", getAttribute("NET_PAYABLE_AMOUNT").getDouble());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",false);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========//
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_DEPOSIT_PM";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
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
            //--------- Approval Flow Update complete -----------
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                //int CompanyID = getAttribute("COMPANY_ID").getInt();
                String ReceiptNo = data.getStringValueFromDB("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+DocNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
                String PMDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+DocNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
                //EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString());
                //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
                double dAmount = data.getDoubleValueFromDB("SELECT PRINCIPLE_AMOUNT FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+DocNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
                //getAttribute("PRINCIPLE_AMOUNT").getDouble();
                
                if(PostVoucher(CompanyID,ReceiptNo,dAmount,DocNo)) {
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET PM_DATE='"+PMDate+"', PREMATURE=1, DEPOSIT_STATUS=2 WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET PM_DATE='"+PMDate+"', PREMATURE=1, DEPOSIT_STATUS=2 WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                } else {
                    LastError = "Voucher not posted properly.";
                    return false;
                }
            }
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    public boolean Update() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
        boolean Validate=true;
        
        try {
            
            String theDocNo=getAttribute("DOC_NO").getString();
            
            //** Validations **//
            if(!Validate()) {
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_DEPOSIT_PM_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            String DocNo = getAttribute("DOC_NO").getString();
            rsResultSet.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsResultSet.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsResultSet.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsResultSet.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsResultSet.updateDouble("PRINCIPLE_AMOUNT", getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsResultSet.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsResultSet.updateDouble("PAID_TDS", getAttribute("PAID_TDS").getDouble());
            rsResultSet.updateDouble("CURRENT_TDS", getAttribute("CURRENT_TDS").getDouble());
            rsResultSet.updateDouble("BROKERAGE", getAttribute("BROKERAGE").getDouble());
            rsResultSet.updateDouble("INTEREST_AMOUNT", getAttribute("INTEREST_AMOUNT").getDouble());
            rsResultSet.updateDouble("PAID_INTEREST", getAttribute("PAID_INTEREST").getDouble());
            rsResultSet.updateDouble("NET_PAYABLE_AMOUNT", getAttribute("NET_PAYABLE_AMOUNT").getDouble());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            //rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            //rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_DEPOSIT_PM_H WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("DOC_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("RECEIPT_NO",getAttribute("RECEIPT_NO").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("CHEQUE_NO",getAttribute("CHEQUE_NO").getString());
            rsHistory.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CHEQUE_DATE").getString()));
            rsHistory.updateString("BOOK_CODE",getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("EFFECTIVE_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString()));
            rsHistory.updateDouble("PRINCIPLE_AMOUNT", getAttribute("PRINCIPLE_AMOUNT").getDouble());
            rsHistory.updateDouble("INTEREST_RATE", getAttribute("INTEREST_RATE").getDouble());
            rsHistory.updateDouble("PAID_TDS", getAttribute("PAID_TDS").getDouble());
            rsHistory.updateDouble("CURRENT_TDS", getAttribute("CURRENT_TDS").getDouble());
            rsHistory.updateDouble("BROKERAGE", getAttribute("BROKERAGE").getDouble());
            rsHistory.updateDouble("INTEREST_AMOUNT", getAttribute("INTEREST_AMOUNT").getDouble());
            rsHistory.updateDouble("PAID_INTEREST", getAttribute("PAID_INTEREST").getDouble());
            rsHistory.updateDouble("NET_PAYABLE_AMOUNT", getAttribute("NET_PAYABLE_AMOUNT").getDouble());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_DEPOSIT_PM";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            //ObjFlow.DocDate = EITLERPGLOBAL.getCurrentDateDB();
            //==== Handling Rejected Documents ==========//
            
            if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FD_DEPOSIT_PM SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+theDocNo+"'",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND DOC_NO='"+theDocNo+"'");
                
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
            
            ObjFlow.UseSpecifiedURL=false;
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                //int CompanyID = getAttribute("COMPANY_ID").getInt();
                String ReceiptNo = data.getStringValueFromDB("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+DocNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
                String PMDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+DocNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
                //EITLERPGLOBAL.formatDateDB(getAttribute("EFFECTIVE_DATE").getString());
                //String ReceiptNo = getAttribute("RECEIPT_NO").getString();
                double dAmount = data.getDoubleValueFromDB("SELECT PRINCIPLE_AMOUNT FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+DocNo+"' AND COMPANY_ID="+CompanyID+"",FinanceGlobal.FinURL);
                //getAttribute("PRINCIPLE_AMOUNT").getDouble();
                
                if(PostVoucher(CompanyID,ReceiptNo,dAmount,DocNo)) {
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET PM_DATE='"+PMDate+"', PREMATURE=1, DEPOSIT_STATUS=2 WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                    data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET PM_DATE='"+PMDate+"', PREMATURE=1, DEPOSIT_STATUS=2 WHERE COMPANY_ID="+CompanyID+" AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                } else {
                    LastError = "Voucher not posted properly.";
                    return false;
                }
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean CanDelete(int CompanyID,String Doc_No,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+Doc_No+"' AND (APPROVED=1)";
            int Count=data.getIntValueFromDB(strSQL, FinanceGlobal.FinURL);
            
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
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int CompanyID,String Doc_No,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT DOC_NO FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+Doc_No+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+Doc_No+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
    
    public boolean Delete(int UserID) {
        try {
            String Doc_No=getAttribute("DOC_NO").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,Doc_No,UserID)) {
                String strSQL = "DELETE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+Doc_No+"'";
                data.Execute(strSQL,FinanceGlobal.FinURL);
                
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
    
    public Object getObject(int CompanyID, String Doc_No) {
        String strCondition = " WHERE DOC_NO='" + Doc_No+"'";
        clsDepositPM objDepositPM = new clsDepositPM();
        objDepositPM.Filter(strCondition,CompanyID);
        return objDepositPM;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FD_DEPOSIT_PM " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FD_DEPOSIT_PM ORDER BY DOC_NO ";
                rsResultSet=Stmt.executeQuery(strSQL);
                Ready=true;
                MoveLast();
                return false;
            } else {
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
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=UtilFunctions.getInt(rsResultSet,"REVISION_NO",0);
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00"));
            setAttribute("RECEIPT_NO",UtilFunctions.getString(rsResultSet,"RECEIPT_NO",""));
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("CHEQUE_NO",UtilFunctions.getString(rsResultSet,"CHEQUE_NO",""));
            setAttribute("CHEQUE_DATE",UtilFunctions.getString(rsResultSet,"CHEQUE_DATE","0000-00-00"));
            setAttribute("BOOK_CODE",UtilFunctions.getString(rsResultSet,"BOOK_CODE",""));
            setAttribute("EFFECTIVE_DATE",UtilFunctions.getString(rsResultSet,"EFFECTIVE_DATE","0000-00-00"));
            setAttribute("PRINCIPLE_AMOUNT", UtilFunctions.getDouble(rsResultSet,"PRINCIPLE_AMOUNT",0.0));
            setAttribute("INTEREST_RATE", UtilFunctions.getDouble(rsResultSet,"INTEREST_RATE",0.0));
            setAttribute("PAID_TDS", UtilFunctions.getDouble(rsResultSet,"PAID_TDS",0.0));
            setAttribute("CURRENT_TDS", UtilFunctions.getDouble(rsResultSet,"CURRENT_TDS",0.0));
            setAttribute("BROKERAGE", UtilFunctions.getDouble(rsResultSet,"BROKERAGE", 0.0));
            setAttribute("INTEREST_AMOUNT",UtilFunctions.getDouble(rsResultSet,"INTEREST_AMOUNT", 0.0));
            setAttribute("PAID_INTEREST", UtilFunctions.getDouble(rsResultSet,"PAID_INTEREST", 0.0));
            setAttribute("NET_PAYABLE_AMOUNT", UtilFunctions.getDouble(rsResultSet,"NET_PAYABLE_AMOUNT",0.0));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("APPROVED",UtilFunctions.getBoolean(rsResultSet,"APPROVED",false));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getBoolean(rsResultSet,"REJECTED",false));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
            setAttribute("CANCELLED",UtilFunctions.getBoolean(rsResultSet,"CANCELLED",false));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order) {
        ResultSet rsTemp=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_PM.DOC_NO,FINANCE.D_FD_DEPOSIT_PM.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_PM,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_PM.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_PM.DOC_NO,FINANCE.D_FD_DEPOSIT_PM.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_PM,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_PM.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_DEPOSIT_PM.DOC_NO";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_DEPOSIT_PM.DOC_NO,FINANCE.D_FD_DEPOSIT_PM.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_DEPOSIT_PM,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_DEPOSIT_PM.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_DEPOSIT_PM.DOC_NO";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsDepositPM ObjDoc=new clsDepositPM();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",UtilFunctions.getString(rsTemp,"DOC_NO",""));
                    String DocNo = UtilFunctions.getString(rsTemp,"DOC_NO","");
                    ObjDoc.setAttribute("DOC_DATE",UtilFunctions.getString(rsTemp,"DOC_DATE","0000-00-00"));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    String ReceiptNo = data.getStringValueFromDB("SELECT RECEIPT_NO FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
                    ObjDoc.setAttribute("RECEIPT_NO",ReceiptNo);
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                    rsTemp.next();
                }
            }
            rsTemp.close();
        }
        catch(Exception e) {
        }
        return List;
    }
    
    public boolean ShowHistory(int CompanyID,String Doc_No) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_FD_DEPOSIT_PM_H WHERE DOC_NO='"+Doc_No+"'";
            rsResultSet=data.getResult(strSQL,FinanceGlobal.FinURL);
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        } catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public static HashMap getHistoryList(int CompanyID,String Doc_No) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            
            String strSQL="SELECT * FROM D_FD_DEPOSIT_PM_H WHERE DOC_NO='"+Doc_No+"'";
            
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDepositPM objAccount=new clsDepositPM();
                    
                    objAccount.setAttribute("DOC_NO",UtilFunctions.getString(rsTmp,"DOC_NO", ""));
                    objAccount.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objAccount.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objAccount.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objAccount.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objAccount.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objAccount);
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
    
    public static String getDocStatus(int CompanyID,String Doc_No) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            String strSQL="SELECT DOC_NO,APPROVED,CANCELLED FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+Doc_No+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(UtilFunctions.getBoolean(rsTmp,"APPROVED",false))   {
                    if(UtilFunctions.getBoolean(rsTmp,"CANCELLED",false))  {
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
    
    public static boolean CanCancel(int CompanyID,String Doc_No) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO,RECEIPT_NO FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+Doc_No+"' AND CANCELLED=0",FinanceGlobal.FinURL);
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
    
    public static boolean CancelDoc(int CompanyID,String Doc_No) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,Doc_No)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+Doc_No+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+Doc_No+"' AND MODULE_ID="+ModuleID,FinanceGlobal.FinURL);
                }
                
                data.Execute("UPDATE D_FD_DEPOSIT_PM SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE DOC_NO='"+Doc_No+"'",FinanceGlobal.FinURL);
                
                Cancelled=true;
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return Cancelled;
    }
    
    private boolean Validate() {
        //** Validations **//
        if(getAttribute("RECEIPT_NO").getString().equals("")) {
            LastError="Please specify Receipt No.";
            return false;
        }
        
        if(getAttribute("EFFECTIVE_DATE").getString().equals("")) {
            LastError="Please specify Effective Date";
            return false;
        }
        //*****************//
        return true;
    }
    
    public static double getIntPercentage(String ReceiptNo,String PMDate) {
        double iPercentage = 0.0;
        String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
        int SchemeType = data.getIntValueFromDB("SELECT SCHEME_TYPE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' ",FinanceGlobal.FinURL);
        String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
        EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, 6);
        if(java.sql.Date.valueOf(PMDate).before(java.sql.Date.valueOf(EffectiveDate)) || java.sql.Date.valueOf(PMDate).compareTo(java.sql.Date.valueOf(EffectiveDate))==0) {
            return iPercentage;
        }
        EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, 12);
        if(java.sql.Date.valueOf(PMDate).before(java.sql.Date.valueOf(EffectiveDate)) || java.sql.Date.valueOf(PMDate).compareTo(java.sql.Date.valueOf(EffectiveDate))==0) {
            iPercentage = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeID+"' AND INTEREST_MONTH=12",FinanceGlobal.FinURL) - 1;
            return iPercentage;
        }
        EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, 12);
        if(java.sql.Date.valueOf(PMDate).before(java.sql.Date.valueOf(EffectiveDate)) || java.sql.Date.valueOf(PMDate).compareTo(java.sql.Date.valueOf(EffectiveDate))==0) {
            iPercentage = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeID+"' AND INTEREST_MONTH=24",FinanceGlobal.FinURL) - 1;
            return iPercentage;
        } else {
            iPercentage = data.getDoubleValueFromDB("SELECT INTEREST_PERCENTAGE FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeID+"' AND INTEREST_MONTH=36",FinanceGlobal.FinURL) - 1;
            return iPercentage;
        }
    }
    
    public static double getBrokerPercentage(String ReceiptNo, String PMDate) {
        double bPercentage = 0.0;
        String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
        String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
        int DepositPeriod = data.getIntValueFromDB("SELECT DEPOSIT_PERIOD FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
        EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, 12);
        if(java.sql.Date.valueOf(PMDate).before(java.sql.Date.valueOf(EffectiveDate))) {
            bPercentage = data.getDoubleValueFromDB("SELECT PERCENTAGE_OF_BROKER FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeID+"' AND INTEREST_MONTH="+DepositPeriod,FinanceGlobal.FinURL);
            return bPercentage;
        }
        if(DepositPeriod == 24) {
            bPercentage = 0.25;
        } else if(DepositPeriod==36){
            EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, 12);
            if(java.sql.Date.valueOf(PMDate).before(java.sql.Date.valueOf(EffectiveDate)) || java.sql.Date.valueOf(PMDate).compareTo(java.sql.Date.valueOf(EffectiveDate)) == 0) {
                bPercentage = 0.50;
            } else {
                bPercentage = 0.25;
            }
        }
        return bPercentage;
    }
    
    public static double calculateBrokerage(String ReceiptNo, String PMDate, double dAmount) {
        double bAmount = 0.0;
        double bPercentage = getBrokerPercentage(ReceiptNo, PMDate);
        bAmount = (dAmount * bPercentage)/100;
        return EITLERPGLOBAL.round(bAmount,2);
    }
    
    public static double calculateInterest(String ReceiptNo, String PMDate) {
        double InterestAmount = 0.0;
        double iPercentage = getIntPercentage(ReceiptNo, PMDate);
        String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
        double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
        int Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate),java.sql.Date.valueOf(PMDate));
        InterestAmount = (Amount * iPercentage * Days)/36500;
        return EITLERPGLOBAL.round(InterestAmount,2);
    }
    
    private boolean PostVoucher(int CompanyID, String ReceiptNo, double Amount,String curDocNo) {
        try {
            String MaturityDate ="";
            String InterestDate ="";
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            HashMap List = new HashMap();
            //****** Prepare Voucher Object ********//
            
            //**********Required Data For Voucher Posting**************//
            int VoucherSrNo=0;
            String BookCode = data.getStringValueFromDB("SELECT BOOK_CODE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ChequeDate = data.getStringValueFromDB("SELECT CHEQUE_DATE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String ChequeNo = data.getStringValueFromDB("SELECT CHEQUE_NO FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String BankMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+ BookCode +"' ",FinanceGlobal.FinURL);
            String BankName = data.getStringValueFromDB("SELECT BOOK_NAME FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+BookCode+"' ",FinanceGlobal.FinURL);
            String PMDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode = "";
            String InterestMainCode = "";
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
                    InterestMainCode = objRule.getAttribute("RULE_OUTCOME").getString();
                }
                MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                //MainAccountCode = "115201";
                //MainAccountCode = "115160";
            } else {
                InterestMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            }
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String IntAccountCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String ReceiptDate =  data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            
            double PaidInterest = data.getDoubleValueFromDB("SELECT PAID_INTEREST FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            double InterestAmount = data.getDoubleValueFromDB("SELECT INTEREST_AMOUNT FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            int SchemeType = data.getIntValueFromDB("SELECT SCHEME_TYPE FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+SchemeID+"' ",FinanceGlobal.FinURL);
            
            double TDSAmount = 0.0;
            if(SchemeType == 3) {
                TDSAmount += data.getDoubleValueFromDB("SELECT CURRENT_TDS FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
                TDSAmount += data.getDoubleValueFromDB("SELECT PAID_TDS FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            } else {
                TDSAmount += data.getDoubleValueFromDB("SELECT CURRENT_TDS FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            }
            
            double Brokerage = data.getDoubleValueFromDB("SELECT BROKERAGE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            
            if(TDSAmount>0 || Brokerage>0) {
                if(!PostVoucherDeduction(EITLERPGLOBAL.gCompanyID,ReceiptNo,TDSAmount,Brokerage,curDocNo)) {
                    return false;
                }
            }
            
            double netPayableAmount = data.getDoubleValueFromDB("SELECT NET_PAYABLE_AMOUNT FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            
            double PayableInterest = 0.0;
            if(InterestAmount > PaidInterest) {
                PayableInterest = InterestAmount - PaidInterest;
                if(!PostVoucher24(EITLERPGLOBAL.gCompanyID,ReceiptNo,PayableInterest,curDocNo)) {
                    return false;
                }
            }
            
            setAttribute("FIN_HIERARCHY_ID",0);
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositPM.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.Payment2ModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            //*********************************************************//
            
            clsVoucher objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); // Ask for Book Code
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_PAYMENT_2);
            objVoucher.setAttribute("CHEQUE_NO",ChequeNo); //Ask for Check No.
            objVoucher.setAttribute("CHEQUE_DATE",EITLERPGLOBAL.formatDate(ChequeDate)); // Ask for Check Date
            objVoucher.setAttribute("BANK_NAME",BankName); // According to Book Name
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(PMDate));// Ask for Voucher Date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            
            if(InterestAmount > PaidInterest) {
                PayableInterest = InterestAmount - PaidInterest;
                if(PayableInterest > 0 ) {
                    VoucherSrNo++;
                    objVoucherItem=new clsVoucherItem();
                    objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                    objVoucherItem.setAttribute("EFFECT","D");
                    objVoucherItem.setAttribute("ACCOUNT_ID",1);
                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode);
                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                    if(SchemeType == 3) {
                        objVoucherItem.setAttribute("AMOUNT",InterestAmount);
                    } else {
                        objVoucherItem.setAttribute("AMOUNT",PayableInterest);
                    }
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
                
                double dAmount = Amount - TDSAmount - Brokerage;
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                objVoucherItem.setAttribute("AMOUNT",dAmount);
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
                
                double netpayableInterest = 0.0;
                if(SchemeType==3) {
                    netpayableInterest = Amount + InterestAmount - TDSAmount - Brokerage;
                } else {
                    netpayableInterest = Amount + PayableInterest - TDSAmount - Brokerage;
                }
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",BankMainCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",netPayableAmount);
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
                
            } else {
                double recoveryInterest = PaidInterest - InterestAmount;
                if(recoveryInterest > 0) {
                    PostVoucherRecovery(EITLERPGLOBAL.gCompanyID, ReceiptNo, recoveryInterest, curDocNo);
                }
                
                double dAmount = 0;
                if(SchemeType==3) {
                    dAmount = Amount - TDSAmount - Brokerage - recoveryInterest + PaidInterest;
                } else {
                    dAmount = Amount - TDSAmount - Brokerage - recoveryInterest;
                }
                
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                objVoucherItem.setAttribute("AMOUNT",dAmount);
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
                
                double netpayableInterest = 0.0;
                if(SchemeType==3) {
                    netpayableInterest = Amount + PaidInterest - recoveryInterest - TDSAmount - Brokerage;
                } else {
                    netpayableInterest = Amount - recoveryInterest - TDSAmount - Brokerage;
                }
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",BankMainCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",netPayableAmount);
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
    
    private boolean PostVoucherDeduction(int CompanyID, String ReceiptNo,double TDSAmount,double Brokerage,String curDocNo) {
        try {
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            int VoucherSrNo = 0;
            HashMap List = new HashMap();
            
            String TDSAccountCode = "127174";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "TDS_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                TDSAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String BrokerAccountCode = "450193";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_ACCOUNT_CODE", "BROKERAGE_ACCOUNT_CODE", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BrokerAccountCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            // --- Start Common Elements --- //
            setAttribute("FIN_HIERARCHY_ID",0);
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "2");
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
            
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositPM.ModuleID , "GET_BOOK_CODE", "PREMATURE_JOURNAL", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String PMDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode = "";
            String InterestMainCode = "";
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
                    InterestMainCode = objRule.getAttribute("RULE_OUTCOME").getString();
                }
                MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                //MainAccountCode = "115201";
                //MainAccountCode = "115160";
            } else {
                InterestMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            }
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String IntAccountCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String ReceiptDate =  data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            
            clsVoucher objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); // Ask for Book Code
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME",clsBook.getBookName(EITLERPGLOBAL.gCompanyID,BookCode));
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(PMDate));// Ask for Voucher Date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            
            if(TDSAmount > 0.0 ) {
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
                objVoucherItem.setAttribute("MODULE_ID",clsDepositPM.ModuleID);
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
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositPM.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            
            if(Brokerage > 0.0 ) {
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","D");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
                objVoucherItem.setAttribute("AMOUNT",Brokerage);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositPM.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
                
                VoucherSrNo++;
                objVoucherItem=new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT","C");
                objVoucherItem.setAttribute("ACCOUNT_ID",1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",BrokerAccountCode); // Broker Account
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                objVoucherItem.setAttribute("AMOUNT",Brokerage);
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("PO_NO","");
                objVoucherItem.setAttribute("PO_DATE","0000-00-00");
                objVoucherItem.setAttribute("REMARKS","");
                objVoucherItem.setAttribute("INVOICE_NO","");
                objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
                objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
                objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
                objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
                objVoucherItem.setAttribute("MODULE_ID",clsDepositPM.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            }
            
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
    
    private boolean PostVoucherRecovery(int CompanyID, String ReceiptNo, double RecoveryInterest, String curDocNo) {
        try {
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            int VoucherSrNo = 0;
            HashMap List = new HashMap();
            
            // --- Start Common Elements --- //
            setAttribute("FIN_HIERARCHY_ID",0);
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "CHOOSE_HIERARCHY", "DEFAULT", "2");
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
            
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositPM.ModuleID , "GET_BOOK_CODE", "PREMATURE_JOURNAL", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String PMDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String MainAccountCode = "";
            String InterestMainCode = "";
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
                    InterestMainCode = objRule.getAttribute("RULE_OUTCOME").getString();
                }
                MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                //MainAccountCode = "115201";
                //MainAccountCode = "115160";
            } else {
                InterestMainCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
                MainAccountCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            }
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            String IntAccountCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String ReceiptDate =  data.getStringValueFromDB("SELECT RECEIPT_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            
            clsVoucher objVoucher=new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            objVoucher.IsInternalPosting=true;
            objVoucher.setAttribute("PREFIX",SelPrefix);
            objVoucher.setAttribute("SUFFIX",SelSuffix);
            objVoucher.setAttribute("FFNO",FFNo);
            objVoucher.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_NO","");
            
            objVoucher.setAttribute("BOOK_CODE",BookCode); // Ask for Book Code
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME",clsBook.getBookName(EITLERPGLOBAL.gCompanyID,BookCode));
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(PMDate));// Ask for Voucher Date
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
            objVoucher.setAttribute("HIERARCHY_ID",getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+getAttribute("FIN_HIERARCHY_ID").getInt()+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","F"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",InterestMainCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",SubAccountCode);
            objVoucherItem.setAttribute("AMOUNT",RecoveryInterest);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositPM.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",IntAccountCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",RecoveryInterest);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",ReceiptNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(ReceiptDate));
            objVoucherItem.setAttribute("MODULE_ID",clsDepositPM.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
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
    
    private boolean PostVoucher24(int CompanyID, String ReceiptNo, double InterestAmount,String curDocNo) {
        try {
            String InterestDate ="";
            clsDepositMaster ObjDepositMaster=(clsDepositMaster)(new clsDepositMaster()).getObject(CompanyID,ReceiptNo);
            int DepositTypeID = data.getIntValueFromDB("SELECT DEPOSIT_TYPE_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"'",FinanceGlobal.FinURL);
            InterestDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_PM WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            InterestDate = clsDepositMaster.deductDays(InterestDate, 1);
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
            //List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsCalcInterest.ModuleID , "GET_BOOK_CODE", "DEPOSIT_INT_JOURNAL", "");
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsDepositPM.ModuleID , "GET_BOOK_CODE", "PREMATURE_JOURNAL", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            //String WarrantNo = data.getStringValueFromDB("SELECT WARRANT_NO FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
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
            String IntMainAccountCode = data.getStringValueFromDB("SELECT INTEREST_MAIN_CODE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            
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
                //MainAccountCode = "115201";
                //MainAccountCode = "115160";
            } else {
                MainAccountCode = ObjDepositMaster.getAttribute("MAIN_ACCOUNT_CODE").getString();
            }
            
            String SubAccountCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_DEPOSIT_PM WHERE DOC_NO='"+curDocNo+"' ",FinanceGlobal.FinURL);
            
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
            objVoucher.setAttribute("CHEQUE_NO",""); //
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
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
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainAccountCode); // cd interest transfer - 115160 / 115201
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
    
    public static double checkTDSAmount(String PartyCode,String ReceiptNo) {
        double interestAmount = 0.0;
        String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear())+"-04-01";
        String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1)+"-03-31";
        String EffectiveDate = "";
        String MaturityDate = "";
        String PMDate = "";
        int DiffofDays = 1;
        double Rate = 0.0;
        double Amount = 0.0;
        int DepositType = 0;
        GregorianCalendar cal = new GregorianCalendar();
        String StartDate="";
        try {
            // Matured and Closed within financial year.
            ResultSet rsTDSClose = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=1 AND PARTY_CODE='"+PartyCode+"' AND MATURITY_DATE>='"+StartFinYear+"' AND MATURITY_DATE<='"+EndFinYear+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            rsTDSClose.first();
            if(rsTDSClose.getRow() > 0 ) {
                while(!rsTDSClose.isAfterLast()) {
                    DepositType = rsTDSClose.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSClose.getDouble("AMOUNT");
                    Rate = rsTDSClose.getDouble("INTEREST_RATE");
                    MaturityDate = rsTDSClose.getString("MATURITY_DATE");
                    if(DepositType==2)  {
                        ResultSet rsTmp = data.getResult("SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+rsTDSClose.getString("RECEIPT_NO")+"' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                    }
                    DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(MaturityDate));
                    StartDate=StartFinYear;
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear())) {
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSClose.next();
                }
            }
            rsTDSClose.close();
            StartDate="";
            // Open within financial year.
            ResultSet rsTDSOpen = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=0 AND PARTY_CODE='"+PartyCode+"' AND RECEIPT_NO<>'"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            rsTDSOpen.first();
            if(rsTDSOpen.getRow() > 0 ) {
                while(!rsTDSOpen.isAfterLast()) {
                    DepositType = rsTDSOpen.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSOpen.getDouble("AMOUNT");
                    Rate = rsTDSOpen.getDouble("INTEREST_RATE");
                    EffectiveDate = rsTDSOpen.getString("EFFECTIVE_DATE");
                    MaturityDate = rsTDSOpen.getString("MATURITY_DATE");
                    if(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                        if(java.sql.Date.valueOf(MaturityDate).before(java.sql.Date.valueOf(EndFinYear))){
                            DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(MaturityDate));
                            StartDate=StartFinYear;
                        } else {
                            DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(EndFinYear))+1;
                            StartDate=StartFinYear;
                        }
                    } else {
                        DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(EndFinYear))+1;
                        StartDate=EffectiveDate;
                    }
                    
                    if(DepositType==2)  {
                        ResultSet rsTmp = data.getResult("SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+rsTDSOpen.getString("RECEIPT_NO")+"' ' AND A.DOC_NO=B.DOC_NO AND B.TDS_ONLY=0",FinanceGlobal.FinURL);
                        //ResultSet rsTmp = data.getResult("SELECT A.INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL A, D_FD_INT_CALC_HEADER B WHERE A.RECEIPT_NO='"+rsTDSOpen.getString("RECEIPT_NO")+"' ",FinanceGlobal.FinURL);
                        rsTmp.first();
                        if(rsTmp.getRow() > 0) {
                            while(!rsTmp.isAfterLast()) {
                                Amount += rsTmp.getDouble("INTEREST_AMOUNT");
                                rsTmp.next();
                            }
                        }
                        rsTmp.close();
                        
                    }
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1) ) {
                    //if (cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()) ) {
                    if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                    } else {
                        interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                    }
                    rsTDSOpen.next();
                }
            }
            rsTDSOpen.close();
            StartDate="";
            // Premature within financial year.
            ResultSet rsTDSPM = data.getResult("SELECT * FROM D_FD_DEPOSIT_MASTER WHERE DEPOSIT_STATUS=2 AND PARTY_CODE='"+PartyCode+"' AND PM_DATE>='"+StartFinYear+"' AND PM_DATE<='"+EndFinYear+"' AND RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            rsTDSPM.first();
            double PMAmount = 0.0;
            String tmpDate1 = "";
            if(rsTDSPM.getRow() > 0) {
                while(!rsTDSPM.isAfterLast()) {
                    DepositType = rsTDSPM.getInt("DEPOSIT_TYPE_ID");
                    Amount = rsTDSPM.getDouble("AMOUNT");
                    Rate = rsTDSPM.getDouble("INTEREST_RATE")-1;
                    EffectiveDate = rsTDSPM.getString("EFFECTIVE_DATE");
                    PMDate = rsTDSPM.getString("PM_DATE");
                    int Months = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_MASTER WHERE SCHEME_ID='"+rsTDSPM.getString("SCHEME_ID")+"' ",FinanceGlobal.FinURL);
                    double nyear = Months/12.0;
                    double nTimes = 12.0/Months;
                    String tmpDate = clsCalcInterest.addMonthToDate(EffectiveDate, 6);
                    if(java.sql.Date.valueOf(tmpDate).before(java.sql.Date.valueOf(PMDate))) {
                        if(java.sql.Date.valueOf(EffectiveDate).after(java.sql.Date.valueOf(StartFinYear))) {
                            if(DepositType == 2 ) {
                                while(java.sql.Date.valueOf(tmpDate).before(java.sql.Date.valueOf(PMDate))) {
                                    PMAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0);
                                    tmpDate1 = tmpDate;
                                    tmpDate = clsCalcInterest.addMonthToDate(tmpDate, Months);
                                    Amount += PMAmount;
                                }
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(tmpDate1), java.sql.Date.valueOf(PMDate));
                                StartDate=tmpDate1;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(tmpDate1)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            } else {
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(PMDate));
                                StartDate=EffectiveDate;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(tmpDate1)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            }
                        } else {
                            if(DepositType == 2) {
                                while(java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(PMDate)) && java.sql.Date.valueOf(EffectiveDate).before(java.sql.Date.valueOf(StartFinYear))) {
                                    PMAmount = EITLERPGLOBAL.round((Amount * Math.pow((1 + ((Rate/100)/nTimes)),nTimes*nyear)) - Amount,0);
                                    tmpDate1 = EffectiveDate;
                                    EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, Months);
                                    Amount += PMAmount;
                                }
                                DiffofDays = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(PMDate));
                                StartDate=StartFinYear;
                                //if (cal.isLeapYear(EITLERPGLOBAL.getYear(StartFinYear)+1) ) {
                                if (cal.isLeapYear(Integer.parseInt(EITLERPGLOBAL.getFinYearEndDate(StartDate).substring(0,4)))) {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36600,0);
                                } else {
                                    interestAmount += EITLERPGLOBAL.round((Amount *  Rate * DiffofDays)/36500,0);
                                }
                            }
                        }
                    }
                    rsTDSPM.next();
                }
            }
            rsTDSPM.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interestAmount;
    }
    
    public static double getCurrentInterest(String ReceiptNo, String PMDate) {
        double currentInterest = 0.0;
        int Days = 0;
        try {
            String StartFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear())+"-04-01";
            String EndFinYear = Integer.toString(EITLERPGLOBAL.getCurrentFinYear()+1)+"-03-31";
            String EffectiveDate = data.getStringValueFromDB("SELECT EFFECTIVE_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            String SchemeID = data.getStringValueFromDB("SELECT SCHEME_ID FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            int SchemeType = data.getIntValueFromDB("SELECT SCHEME_ID FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeID+"' ",FinanceGlobal.FinURL);
            int Months = data.getIntValueFromDB("SELECT INTEREST_CALCULATION_PERIOD FROM D_FD_SCHEME_PERIOD WHERE SCHEME_ID='"+SchemeID+"' ",FinanceGlobal.FinURL);
            double iPercentage = 0.0;
            double Amount = data.getDoubleValueFromDB("SELECT AMOUNT FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' ",FinanceGlobal.FinURL);
            //String SQLQuery = "SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ ReceiptNo +"' ";
            //if(data.IsRecordExist(SQLQuery,FinanceGlobal.FinURL)){
            String SQLQuery = "";
            ResultSet rsResult = null;
            if(SchemeType != 3) {
                SQLQuery = "SELECT INTEREST_DAYS FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='" + ReceiptNo+"' ";
                rsResult = data.getResult(SQLQuery,FinanceGlobal.FinURL);
                rsResult.first();
                while(!rsResult.isAfterLast()) {
                    EffectiveDate = EITLERPGLOBAL.addDaysToDate(EffectiveDate, rsResult.getInt("INTEREST_DAYS"),"yyyy-MM-dd");
                    rsResult.next();
                }
                Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(PMDate));
                iPercentage = getIntPercentage(ReceiptNo, PMDate);
                currentInterest = EITLERPGLOBAL.round(((Amount * iPercentage * Days)/36500),2);
            } else {
                if(java.sql.Date.valueOf(EffectiveDate).after(java.sql.Date.valueOf(StartFinYear))) {
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(PMDate));
                } else {
                    /*if(data.IsRecordExist("SELECT * FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='"+ReceiptNo+"' AND WARRANT_DATE>='"+StartFinYear+"' WARRANT_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL)) {
                        SQLQuery = "SELECT INTEREST_AMOUNT FROM D_FD_INT_CALC_DETAIL WHERE RECEIPT_NO='" + ReceiptNo +"' ";
                        rsResult = data.getResult(SQLQuery,FinanceGlobal.FinURL);
                        rsResult.first();
                        while(!rsResult.isAfterLast()){
                            EffectiveDate = clsCalcInterest.addMonthToDate(EffectiveDate, Months);
                            rsResult.next();
                        }
                        rsResult.close();
                        EffectiveDate = EITLERPGLOBAL.addDaysToDate(EffectiveDate,1,"yyyy-MM-dd");
                        Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(EffectiveDate), java.sql.Date.valueOf(PMDate));
                    } else {*/
                    Days = EITLERPGLOBAL.DateDiff(java.sql.Date.valueOf(StartFinYear), java.sql.Date.valueOf(PMDate));
                    //}
                }
                iPercentage = getIntPercentage(ReceiptNo, PMDate);
                GregorianCalendar cal = new GregorianCalendar();
                if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1)) {
                    currentInterest = EITLERPGLOBAL.round(((Amount * iPercentage * Days)/(366* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
                } else {
                    currentInterest = EITLERPGLOBAL.round(((Amount * iPercentage * Days)/(365* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
                }
            }
            rsResult.close();
            
        } catch (Exception e) {
            return currentInterest;
        }
        return currentInterest;
    }
}