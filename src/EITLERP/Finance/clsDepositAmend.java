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

public class clsDepositAmend {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=106;
    
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
    public clsDepositAmend() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("AMEND_ID",new Variant(""));
        props.put("AMEND_DATE",new Variant(""));
        props.put("AMEND_REASON", new Variant(""));
        props.put("PARTY_CODE",new Variant("")); 
                
        props.put("PARTY_NAME",new Variant(""));
        //props.put("ADDRESS",new Variant(""));
        props.put("ADDRESS1",new Variant(""));
        props.put("ADDRESS2",new Variant(""));
        props.put("ADDRESS3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("PAN_NO",new Variant(""));
        props.put("PAN_DATE",new Variant(""));
                
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("REJECTED_REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        // -- For Update of Deposit Master --
        //props.put("PREVIOUS_RECEIPT_NO",new Variant(""));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FD_AMEND_MASTER ORDER BY AMEND_ID");
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
        
        Statement stHistory,stTmpH,stTmp,stTmpOld;
        ResultSet rsHistory,rsTmpH,rsTmp,rsTmpOld;
        
        try { 
            
            //===== History Related Changes =====//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_AMEND_MASTER_H WHERE AMEND_ID='1'");
            rsHistory.first();
            //----------------------------------//
            
            //** Validations **//
            if(!Validate()) { 
                return false;
            }
            
            //setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            //setAttribute("AMEND_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_FD_AMEND_MASTER","AMEND_ID"));
            
            if(data.IsRecordExist("SELECT AMEND_ID FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+ getAttribute("AMEND_ID").getString() +"'",FinanceGlobal.FinURL)) {
                LastError="Document with this Amend Id. already exist.";
                return false;
            }
            //*****************//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //========= Inserting Into Deposit Amend Master =================//
            
            setAttribute("AMEND_ID",clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID,ModuleID,getAttribute("FFNO").getInt(),true));
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsResultSet.updateString("AMEND_ID",getAttribute("AMEND_ID").getString());
            String AmendID = getAttribute("AMEND_ID").getString();
            rsResultSet.updateString("AMEND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("AMEND_DATE").getString()));
            rsResultSet.updateString("AMEND_REASON",getAttribute("AMEND_REASON").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            String PartyCode = getAttribute("PARTY_CODE").getString();
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsResultSet.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsResultSet.updateString("ADDRESS3",getAttribute("ADDRESS3").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());            
            rsResultSet.updateString("PAN_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            //--------------------------------------------------------//
            
            //========= Inserting Into History =================//
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateString("AMEND_ID",getAttribute("AMEND_ID").getString());
            rsHistory.updateString("AMEND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("AMEND_DATE").getString()));
            rsHistory.updateString("AMEND_REASON",getAttribute("AMEND_REASON").getString());
            
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsHistory.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsHistory.updateString("ADDRESS3",getAttribute("ADDRESS3").getString());
            rsHistory.updateString("CITY",getAttribute("CITY").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsHistory.updateString("PAN_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gNewUserID+"");
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
            
            //======== Update the Approval Flow =========//
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo = getAttribute("AMEND_ID").getString();  
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_DEPOSIT_AMEND";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="AMEND_ID";
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
            //--------- Approval Flow Update complete -----------
            
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                int CompID = getAttribute("COMPANY_ID").getInt();
                String Partycode = getAttribute("PARTY_CODE").getString();
                String AddDisp=null;
                AddDisp= getAttribute("ADDRESS1").getString() + getAttribute("ADDRESS2").getString() + getAttribute("ADDRESS3").getString();
                String Pan_No = getAttribute("PAN_NO").getString();
                String Pan_Date = getAttribute("PAN_DATE").getString();
                String City = getAttribute("CITY").getString();
                String Pincode = getAttribute("PINCODE").getString();
                String Address1=getAttribute("ADDRESS1").getString();
                String Address2=getAttribute("ADDRESS2").getString();
                String Address3=getAttribute("ADDRESS3").getString();
                
                data.Execute("UPDATE D_FIN_PARTY_MASTER SET ADDRESS='"+AddDisp+"',CITY='"+City+"',PINCODE='"+Pincode+"',PAN_NO='"+Pan_No+"',PAN_DATE='"+Pan_Date+"' WHEREE PARTY_CODE='"+Partycode+"' AND MAIN_ACCOUNT_CODE IN ('115012','115153','115160','115029','115036','115177','115201','115218','115225')",FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET ADDRESS1='"+Address1+"',ADDRESS2='"+Address2+"',ADDRESS3='"+Address3+"',CITY='"+City+"',PINCODE='"+Pincode+"',PAN_NO='"+Pan_No+"',PAN_DATE='"+Pan_Date+"' WHEREE PARTY_CODE='"+Partycode+"' AND DEPOSIT_STATUS=0 AND CANCELLED=0 ",FinanceGlobal.FinURL);
            }
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError= e.getMessage();
            return false;
        }
    }
 
    public boolean Update() {
        
        Statement stHistory,stTmp,stTmpH,stTmpOld;
        ResultSet rsHistory,rsTmp,rsTmpH,rsTmpOld;
        boolean Validate=true;
        
        
        try {
            String theDocNo=getAttribute("AMEND_ID").getString();
            
            //** Validations **//
           if(!Validate()) {
                return false;
            }
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FD_AMEND_MASTER_H WHERE AMEND_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            //========= Updating Into Deposit Refund =================//
            
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int CompanyID = EITLERPGLOBAL.gCompanyID;
            rsResultSet.updateString("AMEND_ID",getAttribute("AMEND_ID").getString());
            String AmendID = getAttribute("AMEND_ID").getString();
            rsResultSet.updateString("AMEND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("AMEND_DATE").getString()));
            rsResultSet.updateString("AMEND_REASON",getAttribute("AMEND_REASON").getString());
            rsResultSet.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            String PartyCode = getAttribute("PARTY_CODE").getString();
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsResultSet.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsResultSet.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsResultSet.updateString("ADDRESS3",getAttribute("ADDRESS3").getString());
            rsResultSet.updateString("CITY",getAttribute("CITY").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());            
            rsResultSet.updateString("PAN_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gNewUserID+"");
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
            
            //------------------------------------------------------//
            
            //========= Inserting Into History =================//
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FD_AMEND_MASTER_H WHERE AMEND_ID='"+theDocNo+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("AMEND_ID").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                      
            rsHistory.updateString("AMEND_ID",getAttribute("AMEND_ID").getString());
            rsHistory.updateString("AMEND_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("AMEND_DATE").getString()));
            
            rsHistory.updateString("AMEND_REASON",getAttribute("AMEND_REASON").getString());
            rsHistory.updateString("PARTY_CODE",getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            rsHistory.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsHistory.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsHistory.updateString("ADDRESS3",getAttribute("ADDRESS3").getString());
            rsHistory.updateString("CITY",getAttribute("CITY").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("PAN_NO",getAttribute("PAN_NO").getString());            
            rsHistory.updateString("PAN_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("PAN_DATE").getString()));
            
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gNewUserID+"");
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gNewUserID+"");
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
            
            //-------------------------------------------------//
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=EITLERPGLOBAL.gCompanyID;
            ObjFlow.ModuleID=ModuleID;
            ObjFlow.DocNo=getAttribute("AMEND_ID").getObj().toString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FD_AMEND_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="AMEND_ID";
            ObjFlow.UseSpecifiedURL=true;
            ObjFlow.SpecificURL=FinanceGlobal.FinURL;
            //==== Handling Rejected Documents ==========//
            
            if(AStatus.equals("R")) {
                ObjFlow.To=getAttribute("SEND_DOC_TO").getInt();
                ObjFlow.ExplicitSendTo=true;
            }
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_FD_AMEND_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE AMEND_ID="+theDocNo+"'",FinanceGlobal.FinURL);
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE MODULE_ID="+ModuleID+" AND AMEND_ID='"+theDocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFlow.Status.equals("H")) {
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
            
            ObjFlow.UseSpecifiedURL=false;
            MoveLast();
            if(ObjFlow.Status.equals("F")) {
                int CompID = getAttribute("COMPANY_ID").getInt();
                String Partycode = getAttribute("PARTY_CODE").getString();
                String AddDisp=null;
                AddDisp= getAttribute("ADDRESS1").getString() + getAttribute("ADDRESS2").getString() + getAttribute("ADDRESS3").getString();
                String Pan_No = getAttribute("PAN_NO").getString();
                String Pan_Date = getAttribute("PAN_DATE").getString();
                String City = getAttribute("CITY").getString();
                String Pincode = getAttribute("PINCODE").getString();
                String Address1=getAttribute("ADDRESS1").getString();
                String Address2=getAttribute("ADDRESS2").getString();
                String Address3=getAttribute("ADDRESS3").getString();
                
                data.Execute("UPDATE D_FIN_PARTY_MASTER SET ADDRESS='"+AddDisp+"',CITY='"+City+"',PINCODE='"+Pincode+"',PAN_NO='"+Pan_No+"',PAN_DATE='"+Pan_Date+"' WHERE PARTY_CODE='"+Partycode+"' AND MAIN_ACCOUNT_CODE IN ('115012','115153','115160','115029','115036','115177','115201','115218','115225')", FinanceGlobal.FinURL);
                data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET ADDRESS1='"+Address1+"',ADDRESS2='"+Address2+"',ADDRESS3='"+Address3+"',CITY='"+City+"',PINCODE='"+Pincode+"',PAN_NO='"+Pan_No+"',PAN_DATE='"+Pan_Date+"' WHERE PARTY_CODE='"+Partycode+"' AND DEPOSIT_STATUS=0 AND CANCELLED=0 ", FinanceGlobal.FinURL);
                
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean CanDelete(int CompanyID,String AmendID, int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"' AND (APPROVED=1)";
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
    public boolean IsEditable(int CompanyID,String AmendID,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT AMEND_ID FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"' AND APPROVED=1";
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+AmendID+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
            String AmendID=getAttribute("AMEND_ID").getObj().toString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,AmendID,UserID)) { 
                String strSQL = "DELETE FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"' ";
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
    
    public Object getObject(int CompanyID, String AmendID) {
        String strCondition = " WHERE AMEND_ID='" + AmendID +"' ";
        clsDepositAmend objDepositAmend = new clsDepositAmend();
        objDepositAmend.Filter(strCondition,CompanyID);
        return objDepositAmend;
    }
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_FD_AMEND_MASTER " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_FD_AMEND_MASTER ORDER BY AMEND_ID";
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
            setAttribute("AMEND_ID",UtilFunctions.getString(rsResultSet,"AMEND_ID",""));
            setAttribute("AMEND_DATE",UtilFunctions.getString(rsResultSet,"AMEND_DATE","0000-00-00"));
            setAttribute("AMEND_REASON",UtilFunctions.getString(rsResultSet,"AMEND_REASON",""));
            
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("PARTY_NAME",UtilFunctions.getString(rsResultSet,"PARTY_NAME",""));
            setAttribute("ADDRESS1",UtilFunctions.getString(rsResultSet,"ADDRESS1",""));
            setAttribute("ADDRESS2",UtilFunctions.getString(rsResultSet,"ADDRESS2",""));
            setAttribute("ADDRESS3",UtilFunctions.getString(rsResultSet,"ADDRESS3",""));
            setAttribute("CITY",UtilFunctions.getString(rsResultSet,"CITY",""));
            setAttribute("PINCODE",UtilFunctions.getString(rsResultSet,"PINCODE",""));
            setAttribute("PAN_NO",UtilFunctions.getString(rsResultSet,"PAN_NO",""));
            setAttribute("PAN_DATE",UtilFunctions.getString(rsResultSet,"PAN_DATE","0000-00-00"));
            
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00")));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00")));
            setAttribute("CHANGED",UtilFunctions.getBoolean(rsResultSet,"CHANGED",false));
            setAttribute("CHANGED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00")));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00")));
            setAttribute("REJECTED",UtilFunctions.getBoolean(rsResultSet,"REJECTED",false));
            setAttribute("REJECTED_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00")));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            
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
                strSQL="SELECT FINANCE.D_FD_AMEND_MASTER.AMEND_ID,FINANCE.D_FD_AMEND_MASTER.AMEND_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_AMEND_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_AMEND_MASTER.AMEND_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FD_AMEND_MASTER.AMEND_ID,FINANCE.D_FD_AMEND_MASTER.AMEND_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_AMEND_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_AMEND_MASTER.AMEND_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_AMEND_MASTER.AMEND_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FD_AMEND_MASTER.AMEND_ID,FINANCE.D_FD_AMEND_MASTER.AMEND_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FD_AMEND_MASTER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FD_AMEND_MASTER.AMEND_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_FD_AMEND_MASTER.AMEND_ID";
            }
            
            rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp.first();
            
            Counter=0;
            
            if(rsTemp.getRow()>0) {
                while(!rsTemp.isAfterLast()) {
                    Counter=Counter+1;
                    clsDepositAmend ObjDoc=new clsDepositAmend();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("AMEND_ID",UtilFunctions.getString(rsTemp,"AMEND_ID",""));
                    String AmendID = UtilFunctions.getString(rsTemp,"AMEND_ID","");
                    ObjDoc.setAttribute("AMEND_DATE",UtilFunctions.getString(rsTemp,"AMEND_DATE","0000-00-00"));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"' ",FinanceGlobal.FinURL);
                    String PartyName = data.getStringValueFromDB("SELECT PARTY_NAME FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"' ",FinanceGlobal.FinURL);
                    ObjDoc.setAttribute("PARTY_CODE",PartyCode);
                    ObjDoc.setAttribute("PARTY_NAME",PartyName);
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
    
    public boolean ShowHistory(int CompanyID,String AmendID) {
        Ready=false;
        try {
            String strSQL="SELECT * FROM D_FD_AMEND_MASTER_H WHERE AMEND_ID="+AmendID; 
            rsResultSet=data.getResult(strSQL,FinanceGlobal.FinURL);  
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
    
    public static HashMap getHistoryList(int CompanyID,String AmendID) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_FD_AMEND_MASTER_H WHERE AMEND_ID='"+AmendID+"'";
            rsTmp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDepositAmend objDR=new clsDepositAmend();
                    
                    objDR.setAttribute("AMEND_ID",UtilFunctions.getString(rsTmp,"AMEND_ID",""));
                    objDR.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objDR.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
                    objDR.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objDR.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objDR.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
                    List.put(Integer.toString(List.size()+1),objDR);
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
    
    public static String getDocStatus(int CompanyID,String AmendID) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
             
            String strSQL="SELECT AMEND_ID,APPROVED,CANCELLED FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"'"; 
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
    
    public static boolean CanCancel(int CompanyID,String AmendID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT AMEND_ID FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"' AND APPROVED=0 AND CANCELLED=0",FinanceGlobal.FinURL);
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
    
    public static boolean CancelDoc(int CompanyID,String AmendID) {
        
        ResultSet rsTmp=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,AmendID)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FD_AMEND_MASTER WHERE AMEND_ID='"+AmendID+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+AmendID+"' AND MODULE_ID="+ModuleID);
                }
                
                data.Execute("UPDATE D_FD_AMEND_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE AMEND_ID='"+AmendID+"'",FinanceGlobal.FinURL);
                Cancelled=true;
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        return Cancelled;
    }
    
   private boolean Validate() {
        
        if(getAttribute("PARTY_CODE").getString().trim().equals("")) {
            LastError="Please specify Party Code.";
            return false;
        } else if(!data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"' ",FinanceGlobal.FinURL)) {
            LastError="Party Code does not exist.";
            return false;
        }
        return true;
    }
}
