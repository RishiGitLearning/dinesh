/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.FeltSales.FeltInvReport;

//import EITLERP.*;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Production.clsFeltProductionApprovalFlow;
import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Variant;
import EITLERP.clsFirstFree;
import EITLERP.clsModuleInterface;
import EITLERP.data;


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsDocCancel {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
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
    public clsDocCancel() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("REQ_NO",new Variant(""));
        props.put("REQ_DATE",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("USER_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
        props.put("MAIN_CODE",new Variant(""));
        props.put("PURPOSE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        props.put("REVISION_NO",new Variant(0));
        props.put("CANCELLED",new Variant(false));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("SEND_DOC_TO",new Variant(0));
    }
    
    public boolean LoadData(int pCompanyID) {
        HistoryView=false;
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+pCompanyID+" AND REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY REQ_DATE");
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean LoadData(int pCompanyID,int pModuleID) {
        HistoryView=false;
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+pCompanyID+" AND REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND MODULE_ID="+pModuleID+" ORDER BY REQ_DATE");
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
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST_H WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsDocCancel ObjDoc=new clsDocCancel();
                
                ObjDoc.setAttribute("REQ_NO",rsTmp.getString("REQ_NO"));
                ObjDoc.setAttribute("REQ_DATE",rsTmp.getString("REQ_DATE"));
                ObjDoc.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjDoc.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjDoc.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjDoc.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjDoc.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                ObjDoc.setAttribute("FROM_IP",rsTmp.getString("FROM_IP"));
                
                List.put(Integer.toString(List.size()+1),ObjDoc);
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
        Statement stHistory,stTmp;
        ResultSet rsHistory,rsTmp;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("REQ_DATE").getObj());
             
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST_H WHERE REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE GATEPASS_REQ_NO='1'");
            //rsHeader.first();
            
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("REQ_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,610, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsResultSet.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsResultSet.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsResultSet.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsResultSet.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsResultSet.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateInt("MODULE_ID",(int)getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsHistory.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsHistory.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsHistory.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateInt("MODULE_ID",(int)getAttribute("MODULE_ID").getVal());
            rsHistory.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","");
            rsHistory.updateBoolean("CANCELLED",false);
            
            ResultSet rsTmp1=data.getResult("SELECT USER()");
            rsTmp1.first();
            String str = rsTmp1.getString(1);
            String str_split[] = str.split("@");
            
            rsHistory.updateString("FROM_IP",""+str_split[1]);
            rsHistory.insertRow();
            //----------------------------------------------------------------//
            
            
            //======== Update the Approval Flow =========
            ObjFlow.ModuleID=610; //Doc. Cancel Request
            ObjFlow.DocNo=(String)getAttribute("REQ_NO").getObj();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="REQ_NO";
            
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
            
            //===Now Cancel the document =====//
            String AppStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
//          if(AppStatus.equals("F")) {
//              clsModuleInterface.CancelDocument(EITLERPGLOBAL.gCompanyID, (int)getAttribute("MODULE_ID").getVal(),(String)getAttribute("DOC_NO").getObj());
//          }
            
            if(AppStatus.equals("F")) {
                if ((int)getAttribute("MODULE_ID").getVal() == clsFeltSalesInvoice.ModuleID) {
                    clsFeltSalesInvoice.CancelDoc(EITLERPGLOBAL.gCompanyID, (String)getAttribute("DOC_NO").getObj(),(String)getAttribute("DOC_DATE").getObj());
                }
                
                String theBaleNo = data.getStringValueFromDB("SELECT BALE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+(String)getAttribute("DOC_NO").getObj()+"' AND INVOICE_DATE='"+(String)getAttribute("DOC_DATE").getObj()+"' ");
                String theBaleDate = data.getStringValueFromDB("SELECT PACKING_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+(String)getAttribute("DOC_NO").getObj()+"' AND INVOICE_DATE='"+(String)getAttribute("DOC_DATE").getObj()+"' ");
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET INVOICE_FLG=0 WHERE PKG_BALE_NO='"+theBaleNo+"' AND PKG_BALE_DATE='"+theBaleDate+"'");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='BSR',PR_INVOICE_NO='',PR_INVOICE_DATE='0000-00-00',PR_INVOICE_AMOUNT='0' WHERE PR_INVOICE_NO='"+(String)getAttribute("DOC_NO").getObj()+"' AND PR_INVOICE_DATE='"+ (String)getAttribute("DOC_DATE").getObj() +"'");
                                
                String sql = "UPDATE DINESHMILLS.D_SAL_INVOICE_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='" + (String)getAttribute("DOC_NO").getObj() + "' AND INVOICE_DATE='" + (String)getAttribute("DOC_DATE").getObj() + "' ";
                System.out.println("Changes Dineshmills query : "+sql);
                data.Execute(sql);
                
                data.Execute("UPDATE FINANCE.D_SAL_GSTR_INVOICE_ERP SET APPROVED=0,CANCELLED=1 WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='" + (String)getAttribute("DOC_NO").getObj() + "' AND INVOICE_DATE='" + (String)getAttribute("DOC_DATE").getObj() + "' ");
            }
            
            //===============================//
            
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
        Statement stHistory;
        ResultSet rsHistory;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("REQ_DATE").getObj());
             
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST_H WHERE REQ_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("REQ_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            
            
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
            rsResultSet.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsResultSet.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsResultSet.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsResultSet.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsResultSet.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateInt("MODULE_ID",(int)getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+(String)getAttribute("REQ_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("REQ_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateTimeDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("REQ_NO",(String)getAttribute("REQ_NO").getObj());
            rsHistory.updateString("REQ_DATE",(String)getAttribute("REQ_DATE").getObj());
            rsHistory.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsHistory.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateInt("MODULE_ID",(int)getAttribute("MODULE_ID").getVal());
            rsHistory.updateString("MAIN_CODE",getAttribute("MAIN_CODE").getString());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","");
            
            ResultSet rsTmp1=data.getResult("SELECT USER()");
            rsTmp1.first();
            String str = rsTmp1.getString(1);
            String str_split[] = str.split("@");
            
            rsHistory.updateString("FROM_IP",""+str_split[1]);
            rsHistory.insertRow();
            //----------------------------------------------------------------//
            
            
            //======== Update the Approval Flow =========
            ObjFlow.ModuleID=610; //Doc. Cancel Request
            ObjFlow.DocNo=(String)getAttribute("REQ_NO").getObj();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="REQ_NO";
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from PRODUCTION.FELT_PROD_DOC_DATA
                //data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from PRODUCTION.FELT_PROD_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=610 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            //--------- Approval Flow Update complete -----------
            
            //===Now Cancel the document =====//
            String AppStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            //            if(AppStatus.equals("F")) {
            //                clsModuleInterface.CancelDocument(EITLERPGLOBAL.gCompanyID, (int)getAttribute("MODULE_ID").getVal(),(String)getAttribute("DOC_NO").getObj());
            //            }
            
            if(AppStatus.equals("F")) {
                if (getAttribute("MODULE_ID").getInt() == clsFeltSalesInvoice.ModuleID) {
                    clsFeltSalesInvoice.CancelDoc(EITLERPGLOBAL.gCompanyID, (String)getAttribute("DOC_NO").getObj(),(String)getAttribute("DOC_DATE").getObj());
                }
                
                String cMonth = data.getStringValueFromDB("SELECT CONCAT(DATE_FORMAT(CURDATE(),'%b'),' - ',YEAR(CURDATE())) FROM DUAL");
                String cDate = data.getStringValueFromDB("SELECT LAST_DAY(CURDATE()) FROM DUAL");
                String theBaleNo = data.getStringValueFromDB("SELECT BALE_NO FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+(String)getAttribute("DOC_NO").getObj()+"' AND INVOICE_DATE='"+(String)getAttribute("DOC_DATE").getObj()+"' ");
                String theBaleDate = data.getStringValueFromDB("SELECT PACKING_DATE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='"+(String)getAttribute("DOC_NO").getObj()+"' AND INVOICE_DATE='"+(String)getAttribute("DOC_DATE").getObj()+"' ");
                data.Execute("UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET INVOICE_FLG=0 WHERE PKG_BALE_NO='"+theBaleNo+"' AND PKG_BALE_DATE='"+theBaleDate+"'");
                data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_PIECE_STAGE='BSR',PR_INVOICE_NO='',PR_INVOICE_DATE='0000-00-00',PR_INVOICE_AMOUNT='0',PR_CURRENT_SCH_MONTH='"+cMonth+"',PR_CURRENT_SCH_LAST_DDMMYY='"+cDate+"',PR_PIECE_CURRENT_MONTH_IT_REMARK='Change Schedule to current month ("+cMonth+") as invoice cancellation' WHERE PR_INVOICE_NO='"+(String)getAttribute("DOC_NO").getObj()+"' AND PR_INVOICE_DATE='"+ (String)getAttribute("DOC_DATE").getObj() +"'");
                                
                String sql = "UPDATE DINESHMILLS.D_SAL_INVOICE_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='" + (String)getAttribute("DOC_NO").getObj() + "' AND INVOICE_DATE='" + (String)getAttribute("DOC_DATE").getObj() + "' ";
                System.out.println("Changes Dineshmills query : "+sql);
                data.Execute(sql);
                
                data.Execute("UPDATE FINANCE.D_SAL_GSTR_INVOICE_ERP SET APPROVED=0,CANCELLED=1 WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND INVOICE_NO='" + (String)getAttribute("DOC_NO").getObj() + "' AND INVOICE_DATE='" + (String)getAttribute("DOC_DATE").getObj() + "' ");
            }
            //===============================//
            
            
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
            String lDocNo=(String)getAttribute("REQ_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID=" + lCompanyID +" AND REQ_NO='"+lDocNo+"'";
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
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND REQ_NO='"+pDocNo+"'";
        clsDocCancel ObjDoc = new clsDocCancel();
        ObjDoc.Filter(strCondition,pCompanyID);
        return ObjDoc;
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0,Counter2=0,Counter3=0;
        
        try {
            tmpConn=data.getConn();
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST "+pCondition);
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsDocCancel ObjDoc =new clsDocCancel();
                ObjDoc.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjDoc.setAttribute("REQ_NO",rsTmp.getString("REQ_NO"));
                ObjDoc.setAttribute("REQ_DATE",rsTmp.getString("REQ_DATE"));
                ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                ObjDoc.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                ObjDoc.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjDoc.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjDoc.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjDoc.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjDoc.setAttribute("REJECTED",rsTmp.getBoolean("REJECTED"));
                ObjDoc.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjDoc.setAttribute("HIERARCHY_ID",rsTmp.getInt("HIERARCHY_ID"));
                ObjDoc.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjDoc.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjDoc.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjDoc.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                List.put(Long.toString(Counter),ObjDoc);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if(rsResultSet.getRow()<=0) {
                strSql = "SELECT * FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND REQ_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND REQ_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY REQ_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                LastError="No Records found";
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
        Statement tmpStmt;
        int Counter=0,Counter2=0;
        int RevNo=0;
        
        try {
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if(HistoryView) {
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("REQ_NO",rsResultSet.getString("REQ_NO"));
            setAttribute("REQ_DATE",rsResultSet.getString("REQ_DATE"));
            setAttribute("DEPT_ID",rsResultSet.getInt("DEPT_ID"));
            setAttribute("USER_ID",rsResultSet.getInt("USER_ID"));
            setAttribute("DOC_NO",rsResultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",rsResultSet.getString("DOC_DATE"));
            setAttribute("MODULE_ID",rsResultSet.getInt("MODULE_ID"));
            setAttribute("MAIN_CODE",rsResultSet.getString("MAIN_CODE"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND REQ_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=610 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND REQ_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=610 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.DEPT_ID,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.MODULE_ID FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=610 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.DEPT_ID,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.MODULE_ID FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=610 ORDER BY PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.DEPT_ID,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.MODULE_ID FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=610 ORDER BY PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO";
            }
            
            //strSQL="SELECT PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO,PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_DATE FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.REQ_NO=PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO AND PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.COMPANY_ID=PRODUCTION.FELT_PROD_DOC_DATA.COMPANY_ID AND PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+pUserID+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND PRODUCTION.FELT_PROD_DOC_DATA.MODULE_ID=610";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("REQ_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsDocCancel ObjDoc=new clsDocCancel();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("REQ_NO",rsTmp.getString("REQ_NO"));
                    ObjDoc.setAttribute("REQ_DATE",rsTmp.getString("REQ_DATE"));
                    ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                    ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjDoc.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
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
    
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    
                }
                else {
                    canCancel=true;
                }
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=610;
                    
                    data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE PRODUCTION.FELT_PROD_DOC_CANCEL_REQUEST SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"'");
                
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
