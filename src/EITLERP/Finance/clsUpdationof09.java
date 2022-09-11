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
import EITLERP.Purchase.*;
import EITLERP.Stores.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsUpdationof09 {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    //Requisition Material Collection
    public HashMap colItems;
    
    //History Related properties
    private boolean HistoryView=false;
    
    public static int ModuleID=177;
    
    
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
    public clsUpdationof09() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("VOUCHER_NO",new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        props.put("EXCLUDE_IN_ADJ",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED", new Variant(0));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(""));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    /*<b>Load Data</b>\nThis method loads data from database to Business Object*/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_09_UPDATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO");
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        } catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        } catch(Exception e) {
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
            if(!Validate()) {
                return false;
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_09_UPDATION_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo(getAttribute("COMPANY_ID").getInt(),clsUpdationof09.ModuleID, getAttribute("FFNO").getInt(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsResultSet.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsResultSet.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsHistory.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsHistory.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsUpdationof09.ModuleID; //Material Requisition
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_09_UPDATION";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
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
            ObjFlow.UseSpecifiedURL=false;
            //--------- Approval Flow Update complete -----------
            
            // === update status of voucher for auto-adjustment On Final Approval === //
            if(AStatus.equals("F")) {
                boolean ExcludeInAdj = false;
                if(getAttribute("EXCLUDE_IN_ADJ").getInt()==1) {
                    ExcludeInAdj = false;
                } else {
                    ExcludeInAdj = true;
                }
                String UpdateSQL = "UPDATE D_FIN_VOUCHER_HEADER SET EXCLUDE_IN_ADJ="+ExcludeInAdj+", CHANGED=1, CHANGED_DATE=CURDATE() " +
                "WHERE VOUCHER_NO='"+getAttribute("VOUCHER_NO").getString()+"'";
                data.Execute(UpdateSQL, FinanceGlobal.FinURL);
            }
            //------------------------------------------------------------------------//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            
            try {
                e.printStackTrace();
            }
            catch(Exception c) {
            }
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
            
            if(!Validate()) {
                return false;
            }
            
            String AStatus=getAttribute("APPROVAL_STATUS").getString();
            
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_09_UPDATION_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String theDocNo=getAttribute("DOC_NO").getString();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            rsResultSet.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsResultSet.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsResultSet.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsResultSet.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsResultSet.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsResultSet.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsResultSet.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_09_UPDATION_H WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+getAttribute("DOC_NO").getString()+"'",FinanceGlobal.FinURL);
            RevNo++;
            String RevDocNo=getAttribute("DOC_NO").getString();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("FROM_REMARKS").getString());
            rsHistory.updateLong("COMPANY_ID",getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("DOC_NO",getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", EITLERPGLOBAL.formatDateDB(getAttribute("DOC_DATE").getString()));
            rsHistory.updateString("VOUCHER_NO",getAttribute("VOUCHER_NO").getString());
            rsHistory.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("VOUCHER_DATE").getString()));
            rsHistory.updateInt("EXCLUDE_IN_ADJ",getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateLong("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("CREATED_DATE").getString()));
            rsHistory.updateString("MODIFIED_BY",getAttribute("MODIFIED_BY").getString());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("MODIFIED_DATE").getString()));
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=getAttribute("COMPANY_ID").getInt();
            ObjFlow.ModuleID=clsUpdationof09.ModuleID; //Gatepass Requisition
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=getAttribute("FROM").getInt();
            ObjFlow.To=getAttribute("TO").getInt();
            ObjFlow.Status=getAttribute("APPROVAL_STATUS").getString();
            ObjFlow.TableName="D_FIN_09_UPDATION";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=getAttribute("HIERARCHY_ID").getInt();
            ObjFlow.Remarks=getAttribute("FROM_REMARKS").getString();
            ObjFlow.FieldName="DOC_NO";
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
                data.Execute("UPDATE D_FIN_09_UPDATION SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND DOC_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
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
            //--------- Approval Flow Update complete -----------
            
            // === update status of voucher for auto-adjustment On Final Approval === //
            if(AStatus.equals("F")) {
                boolean ExcludeInAdj = false;
                if(getAttribute("EXCLUDE_IN_ADJ").getInt()==1) {
                    ExcludeInAdj = false;
                } else {
                    ExcludeInAdj = true;
                }
                String UpdateSQL = "UPDATE D_FIN_VOUCHER_HEADER SET EXCLUDE_IN_ADJ="+ExcludeInAdj+", CHANGED=1, CHANGED_DATE=CURDATE() " +
                "WHERE VOUCHER_NO='"+getAttribute("VOUCHER_NO").getString()+"'";
                data.Execute(UpdateSQL, FinanceGlobal.FinURL);
                
                InsertRevision(theDocNo);
            }
            //------------------------------------------------------------------------//
            
            return true;
        }
        catch(Exception e) {
            try {
            }
            catch(Exception c) {
            }
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_09_UPDATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
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
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FIN_09_UPDATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND STATUS='W'";
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
            String lDocNo=getAttribute("DOC_NO").getString();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(CompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_FIN_09_UPDATION WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
                data.Execute(strQry,FinanceGlobal.FinURL);
                strQry = "DELETE FROM D_FIN_BILL_MATCH_DETAIL WHERE COMPANY_ID=" + CompanyID +" AND DOC_NO='"+lDocNo+"'";
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
        String strCondition = " WHERE COMPANY_ID=" + CompanyID + " AND DOC_NO='" + VoucherNo + "'" ;
        clsVoucher objVoucher = new clsVoucher();
        objVoucher.Filter(strCondition,CompanyID);
        return objVoucher;
    }
    
    
    
    
    public boolean Filter(String Condition,int CompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FIN_09_UPDATION " + Condition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FIN_09_UPDATION WHERE COMPANY_ID="+CompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
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
        
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",UtilFunctions.getInt(rsResultSet,"REVISION_NO",0));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsResultSet,"DOC_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00"));
            setAttribute("VOUCHER_NO",UtilFunctions.getString(rsResultSet,"VOUCHER_NO",""));
            setAttribute("VOUCHER_DATE",UtilFunctions.getString(rsResultSet,"VOUCHER_DATE","0000-00-00"));
            setAttribute("EXCLUDE_IN_ADJ",UtilFunctions.getInt(rsResultSet,"EXCLUDE_IN_ADJ",0));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order,int FinYearFrom) {
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
                strSQL="SELECT FINANCE.D_FIN_09_UPDATION.DOC_NO,FINANCE.D_FIN_09_UPDATION.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE,FINANCE.D_FIN_09_UPDATION.VOUCHER_NO FROM FINANCE.D_FIN_09_UPDATION,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_09_UPDATION.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_09_UPDATION.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_09_UPDATION.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FIN_09_UPDATION.DOC_NO,FINANCE.D_FIN_09_UPDATION.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE,FINANCE.D_FIN_09_UPDATION.VOUCHER_NO FROM FINANCE.D_FIN_09_UPDATION,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_09_UPDATION.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_09_UPDATION.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_09_UPDATION.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FIN_09_UPDATION.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FIN_09_UPDATION.DOC_NO,FINANCE.D_FIN_09_UPDATION.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE,FINANCE.D_FIN_09_UPDATION.VOUCHER_NO FROM FINANCE.D_FIN_09_UPDATION,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FIN_09_UPDATION.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FIN_09_UPDATION.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FIN_09_UPDATION.COMPANY_ID="+CompanyID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FIN_09_UPDATION.DOC_NO";
            }
            
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsUpdationof09 ObjDoc=new clsUpdationof09();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("DOC_NO",rsTmp3.getString("DOC_NO"));
                    ObjDoc.setAttribute("DOC_DATE",rsTmp3.getString("DOC_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("VOUCHER_NO",rsTmp3.getString("VOUCHER_NO"));
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
    
    
    public boolean ShowHistory(int CompanyID,String DocNo) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FIN_09_UPDATION_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_09_UPDATION_H WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsUpdationof09 objVoucher=new clsUpdationof09();
                    
                    objVoucher.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    objVoucher.setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE")));
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FIN_09_UPDATION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELLED")) {
                        strMessage="Document is cancelled";
                    } else {
                        strMessage="";
                    }
                } else {
                    strMessage="Document is created but is under approval";
                }
            } else {
                strMessage="Document does not exist";
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return strMessage;
    }
    
    public static boolean CanCancel(int CompanyID,String DocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FIN_09_UPDATION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND CANCELLED=0 AND APPROVED = 0",FinanceGlobal.FinURL);
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
    
    public static boolean CancelDoc(int CompanyID,String DocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(CompanyID,DocNo)) {
                
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_FIN_09_UPDATION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                }
                else {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"' AND MODULE_ID="+clsUpdationof09.ModuleID);
                }
                
                data.Execute("UPDATE D_FIN_09_UPDATION SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'",FinanceGlobal.FinURL);
                
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
        return true;
    }
    
    public boolean InsertRevision(String theDocNo) {
        String VoucherNo = "";
        boolean ExcludeInAdj=false;
        int intExcludeInAdj=0;
        Statement stHistory = null, stHDetail = null;
        ResultSet rsHistory = null, rsHDetail = null;
        
        try {
            VoucherNo = data.getStringValueFromDB("SELECT VOUCHER_NO FROM D_FIN_09_UPDATION WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
            intExcludeInAdj = data.getIntValueFromDB("SELECT EXCLUDE_IN_ADJ FROM D_FIN_09_UPDATION WHERE DOC_NO='"+theDocNo+"' ",FinanceGlobal.FinURL);
            clsVoucher ObjVoucher = (clsVoucher)new clsVoucher().getObject(EITLERPGLOBAL.gCompanyID, VoucherNo);
            if(intExcludeInAdj==1) {
                ExcludeInAdj=false;
            }
            if(intExcludeInAdj==2) {
                ExcludeInAdj=true;
            }
            ObjVoucher.setAttribute("EXCLUDE_IN_ADJ", ExcludeInAdj);
            
            int RevNo=data.getIntValueFromDB("SELECT MAX(REVISION_NO) FROM D_FIN_VOUCHER_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO='"+VoucherNo+"' ",FinanceGlobal.FinURL);
            RevNo++;
            
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FIN_VOUCHER_HEADER_H WHERE VOUCHER_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FIN_VOUCHER_DETAIL_H WHERE VOUCHER_NO='1'");
            rsHDetail.first();
            
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS","F");
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS","Updated by " + EITLERPGLOBAL.gLoginID + " through Module : Updation / Deletion of 09");
            rsHistory.updateLong("COMPANY_ID",ObjVoucher.getAttribute("COMPANY_ID").getLong());
            rsHistory.updateString("VOUCHER_NO",ObjVoucher.getAttribute("VOUCHER_NO").getString());
            rsHistory.updateString("VOUCHER_DATE", EITLERPGLOBAL.formatDateDB(ObjVoucher.getAttribute("VOUCHER_DATE").getString()));
            rsHistory.updateInt("VOUCHER_TYPE",ObjVoucher.getAttribute("VOUCHER_TYPE").getInt());
            rsHistory.updateString("BOOK_CODE",ObjVoucher.getAttribute("BOOK_CODE").getString());
            rsHistory.updateString("CHEQUE_NO",ObjVoucher.getAttribute("CHEQUE_NO").getString());
            rsHistory.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(ObjVoucher.getAttribute("CHEQUE_DATE").getString()));
            rsHistory.updateString("BANK_NAME",ObjVoucher.getAttribute("BANK_NAME").getString());
            rsHistory.updateString("ST_CATEGORY",ObjVoucher.getAttribute("ST_CATEGORY").getString());
            rsHistory.updateString("REASON_CODE",ObjVoucher.getAttribute("REASON_CODE").getString());
            rsHistory.updateString("REMARKS",ObjVoucher.getAttribute("REMARKS").getString());
            rsHistory.updateLong("HIERARCHY_ID",ObjVoucher.getAttribute("HIERARCHY_ID").getLong());
            rsHistory.updateString("CREATED_BY","" );
            rsHistory.updateString("CREATED_DATE","0000-00-00");
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",true);
            rsHistory.updateString("APPROVED_DATE",ObjVoucher.getAttribute("APPROVED_DATE").getString());
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateDouble("CHEQUE_AMOUNT",ObjVoucher.getAttribute("CHEQUE_AMOUNT").getDouble());
            rsHistory.updateInt("PAYMENT_MODE",ObjVoucher.getAttribute("PAYMENT_MODE").getInt());
            rsHistory.updateString("LEGACY_NO",ObjVoucher.getAttribute("LEGACY_NO").getString());
            rsHistory.updateString("LINK_NO",ObjVoucher.getAttribute("LINK_NO").getString());
            rsHistory.updateString("LEGACY_DATE",EITLERPGLOBAL.formatDateDB(ObjVoucher.getAttribute("LEGACY_DATE").getString()));
            rsHistory.updateInt("EXCLUDE_IN_ADJ",ObjVoucher.getAttribute("EXCLUDE_IN_ADJ").getInt());
            rsHistory.updateString("CUSTOMER_BANK",ObjVoucher.getAttribute("CUSTOMER_BANK").getString());
            rsHistory.insertRow();
            
            for(int i=1;i<=ObjVoucher.colVoucherItems.size();i++) {
                clsVoucherItem objItem=(clsVoucherItem)ObjVoucher.colVoucherItems.get(Integer.toString(i));
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",ObjVoucher.getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("VOUCHER_NO",ObjVoucher.getAttribute("VOUCHER_NO").getString());
                rsHDetail.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsHDetail.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsHDetail.updateInt("ACCOUNT_ID",objItem.getAttribute("ACCOUNT_ID").getInt());
                rsHDetail.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHDetail.updateString("SUB_ACCOUNT_CODE",objItem.getAttribute("SUB_ACCOUNT_CODE").getString());
                rsHDetail.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateDouble("APPLICABLE_AMOUNT",objItem.getAttribute("APPLICABLE_AMOUNT").getDouble());
                rsHDetail.updateDouble("PERCENTAGE",objItem.getAttribute("PERCENTAGE").getDouble());
                rsHDetail.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY","");
                rsHDetail.updateString("CREATED_DATE","0000-00-00");
                rsHDetail.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateInt("CANCELLED",0);
                rsHDetail.updateString("PO_NO",objItem.getAttribute("PO_NO").getString());
                rsHDetail.updateString("PO_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("PO_DATE").getString()));
                rsHDetail.updateString("INVOICE_NO",objItem.getAttribute("INVOICE_NO").getString());
                rsHDetail.updateString("INVOICE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("INVOICE_DATE").getString()));
                rsHDetail.updateString("GRN_NO",objItem.getAttribute("GRN_NO").getString());
                rsHDetail.updateString("GRN_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("GRN_DATE").getString()));
                rsHDetail.updateInt("MODULE_ID",objItem.getAttribute("MODULE_ID").getInt());
                rsHDetail.updateDouble("INVOICE_AMOUNT",objItem.getAttribute("INVOICE_AMOUNT").getDouble());
                rsHDetail.updateInt("IS_DEDUCTION",objItem.getAttribute("IS_DEDUCTION").getInt());
                rsHDetail.updateInt("REF_COMPANY_ID",objItem.getAttribute("REF_COMPANY_ID").getInt());
                rsHDetail.updateString("LINK_NO",objItem.getAttribute("LINK_NO").getString());
                try {
                    rsHDetail.updateInt("REF_SR_NO",objItem.getAttribute("REF_SR_NO").getInt());
                }
                catch(Exception c){}
                rsHDetail.insertRow();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
