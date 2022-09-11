/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.FilterTrading;

import EITLERP.*;
import java.util.*;
import java.sql.*;


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsInvoiceEntry {
    
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
    public clsInvoiceEntry() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("PACKING_NOTE_NO",new Variant(""));
        props.put("DOC_NO",new Variant(""));
        props.put("PROCESSING_DATE",new Variant(""));
        props.put("PACKING_DATE",new Variant(""));
        props.put("CHARGE_CODE",new Variant(""));
        props.put("TRANSPORTER",new Variant(""));
        props.put("GROSS_WEIGHT",new Variant(0));
        props.put("CHECK_POST",new Variant(""));
        props.put("INSURANCE_CHARGE",new Variant(0));        
        props.put("ZERO_RATE",new Variant(0));
        
        
        props.put("VAT4",new Variant(0));
        props.put("VAT1",new Variant(0));
        props.put("CST5",new Variant(0));
        props.put("CST2",new Variant(0));
        
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER WHERE COMPANY_ID="+pCompanyID+" ORDER BY CREATED_DATE");
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" ORDER BY CREATED_DATE");
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsInvoiceEntry ObjDoc=new clsInvoiceEntry();
                
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjDoc.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjDoc.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjDoc.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjDoc.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjDoc.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
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
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
 
            
            clsFilterFabricApprovalFlow ObjFlow=new clsFilterFabricApprovalFlow();
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsResultSet.updateString("PROCESSING_DATE",(String)getAttribute("PROCESSING_DATE").getObj());
            rsResultSet.updateString("PACKING_NOTE_NO",(String)getAttribute("PACKING_NOTE_NO").getObj());
            rsResultSet.updateString("PACKING_DATE",(String)getAttribute("PACKING_DATE").getObj());
            rsResultSet.updateString("CHARGE_CODE",(String)getAttribute("CHARGE_CODE").getObj());            
            rsResultSet.updateDouble("GROSS_WEIGHT",(double)getAttribute("GROSS_WEIGHT").getVal());
            rsResultSet.updateString("TRANSPORTER",(String)getAttribute("TRANSPORTER").getObj());
            rsResultSet.updateString("CHECK_POST",(String)getAttribute("CHECK_POST").getObj());
            rsResultSet.updateInt("INSURANCE_CHARGE",(int)getAttribute("INSURANCE_CHARGE").getVal());
            rsResultSet.updateInt("ZERO_RATE",(int)getAttribute("ZERO_RATE").getVal());
            rsResultSet.updateInt("VAT4",(int)getAttribute("VAT4").getVal());
            rsResultSet.updateInt("VAT1",(int)getAttribute("VAT1").getVal());
            rsResultSet.updateInt("CST2",(int)getAttribute("CST2").getVal());
            rsResultSet.updateInt("CST5",(int)getAttribute("CST5").getVal());
            
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            //rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            //rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsHistory.updateString("PROCESSING_DATE",(String)getAttribute("PROCESSING_DATE").getObj());
            rsHistory.updateString("PACKING_NOTE_NO",(String)getAttribute("PACKING_NOTE_NO").getObj());
            rsHistory.updateString("PACKING_DATE",(String)getAttribute("PACKING_DATE").getObj());
            rsHistory.updateString("CHARGE_CODE",(String)getAttribute("CHARGE_CODE").getObj());            
            rsHistory.updateDouble("GROSS_WEIGHT",(double)getAttribute("GROSS_WEIGHT").getVal());
            rsHistory.updateString("TRANSPORTER",(String)getAttribute("TRANSPORTER").getObj());
            rsHistory.updateString("CHECK_POST",(String)getAttribute("CHECK_POST").getObj());
            rsHistory.updateInt("INSURANCE_CHARGE",(int)getAttribute("INSURANCE_CHARGE").getVal());
            rsHistory.updateInt("ZERO_RATE",(int)getAttribute("ZERO_RATE").getVal());
            rsHistory.updateInt("VAT4",(int)getAttribute("VAT4").getVal());
            rsHistory.updateInt("VAT1",(int)getAttribute("VAT1").getVal());
            rsHistory.updateInt("CST2",(int)getAttribute("CST2").getVal());
            rsHistory.updateInt("CST5",(int)getAttribute("CST5").getVal());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            //rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            //rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");            
            rsHistory.insertRow();
            //----------------------------------------------------------------//
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=749; //Doc. Cancel Request
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="FILTERFABRIC.FF_TRD_INVOICE_PARAMETER";
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
            
            //===Now Cancel the document =====//
            String AppStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            
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
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("DOC_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GATEPASS_REQ_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_REQ_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            
            
            clsFilterFabricApprovalFlow ObjFlow=new clsFilterFabricApprovalFlow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsResultSet.updateString("PROCESSING_DATE",(String)getAttribute("PROCESSING_DATE").getObj());
            rsResultSet.updateString("PACKING_NOTE_NO",(String)getAttribute("PACKING_NOTE_NO").getObj());
            rsResultSet.updateString("PACKING_DATE",(String)getAttribute("PACKING_DATE").getObj());
            rsResultSet.updateString("CHARGE_CODE",(String)getAttribute("CHARGE_CODE").getObj());            
            rsResultSet.updateDouble("GROSS_WEIGHT",(double)getAttribute("GROSS_WEIGHT").getVal());
            rsResultSet.updateString("TRANSPORTER",(String)getAttribute("TRANSPORTER").getObj());
            rsResultSet.updateString("CHECK_POST",(String)getAttribute("CHECK_POST").getObj());
            rsResultSet.updateInt("INSURANCE_CHARGE",(int)getAttribute("INSURANCE_CHARGE").getVal());
            rsResultSet.updateInt("ZERO_RATE",(int)getAttribute("ZERO_RATE").getVal());
            rsResultSet.updateInt("VAT4",(int)getAttribute("VAT4").getVal());
            rsResultSet.updateInt("VAT1",(int)getAttribute("VAT1").getVal());
            rsResultSet.updateInt("CST2",(int)getAttribute("CST2").getVal());
            rsResultSet.updateInt("CST5",(int)getAttribute("CST5").getVal());
            //rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsHistory.updateString("PROCESSING_DATE",(String)getAttribute("PROCESSING_DATE").getObj());
            rsHistory.updateString("PACKING_NOTE_NO",(String)getAttribute("PACKING_NOTE_NO").getObj());
            rsHistory.updateString("PACKING_DATE",(String)getAttribute("PACKING_DATE").getObj());
            rsHistory.updateString("CHARGE_CODE",(String)getAttribute("CHARGE_CODE").getObj());            
            rsHistory.updateDouble("GROSS_WEIGHT",(double)getAttribute("GROSS_WEIGHT").getVal());
            rsHistory.updateString("TRANSPORTER",(String)getAttribute("TRANSPORTER").getObj());
            rsHistory.updateString("CHECK_POST",(String)getAttribute("CHECK_POST").getObj());
            rsHistory.updateInt("INSURANCE_CHARGE",(int)getAttribute("INSURANCE_CHARGE").getVal());
            rsHistory.updateInt("ZERO_RATE",(int)getAttribute("ZERO_RATE").getVal());
            rsHistory.updateInt("VAT4",(int)getAttribute("VAT4").getVal());
            rsHistory.updateInt("VAT1",(int)getAttribute("VAT1").getVal());
            rsHistory.updateInt("CST2",(int)getAttribute("CST2").getVal());
            rsHistory.updateInt("CST5",(int)getAttribute("CST5").getVal());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            //rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.insertRow();
            //----------------------------------------------------------------//
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=749; //Doc. Cancel Request
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="FILTERFABRIC.FF_TRD_INVOICE_PARAMETER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from FILTERFABRIC.FF_TRD_DOC_DATA
                //data.Execute("DELETE FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE FILTERFABRIC.FF_TRD_INVOICE_PARAMETER SET REJECTED=0 WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from FILTERFABRIC.FF_TRD_DOC_DATA
                data.Execute("DELETE FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=749 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    //Deletes current record
   
    
    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"'";
        clsInvoiceEntry ObjDoc = new clsInvoiceEntry();
        ObjDoc.Filter(strCondition,pCompanyID);
        return ObjDoc;
    }
    
    
    
    
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if(rsResultSet.getRow()<=0) {
                strSql = "SELECT * FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND CREATED_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' ORDER BY CREATED_DATE";
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
            setAttribute("DOC_NO",rsResultSet.getString("DOC_NO"));
            setAttribute("PROCESSING_DATE",rsResultSet.getString("PROCESSING_DATE"));
            setAttribute("PACKING_NOTE_NO",rsResultSet.getString("PACKING_NOTE_NO"));
            setAttribute("PACKING_DATE",rsResultSet.getString("PACKING_DATE"));
            setAttribute("CHARGE_CODE",rsResultSet.getString("CHARGE_CODE"));
            setAttribute("TRANSPORTER",rsResultSet.getString("TRANSPORTER"));
            setAttribute("CHECK_POST",rsResultSet.getString("CHECK_POST"));
            setAttribute("GROSS_WEIGHT",rsResultSet.getDouble("GROSS_WEIGHT"));
            setAttribute("INSURANCE_CHARGE", rsResultSet.getInt("INSURANCE_CHARGE"));
            setAttribute("ZERO_RATE", rsResultSet.getInt("ZERO_RATE"));
            setAttribute("VAT4",rsResultSet.getInt("VAT4"));
            setAttribute("VAT1",rsResultSet.getInt("VAT1"));
            setAttribute("CST2",rsResultSet.getInt("CST2"));
            setAttribute("CST5",rsResultSet.getInt("CST5"));
         
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            
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
            strSQL="SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=749 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=749 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
                strSQL="SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,D_COM_DOC_CANCEL_REQUEST.DEPT_ID,D_COM_DOC_CANCEL_REQUEST.MODULE_ID FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID="+pUserID+" AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44 ORDER BY FILTERFABRIC.FF_TRD_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,D_COM_DOC_CANCEL_REQUEST.DEPT_ID,D_COM_DOC_CANCEL_REQUEST.MODULE_ID FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID="+pUserID+" AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44 ORDER BY D_COM_DOC_CANCEL_REQUEST.REQ_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE,RECEIVED_DATE,D_COM_DOC_CANCEL_REQUEST.DEPT_ID,D_COM_DOC_CANCEL_REQUEST.MODULE_ID FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID="+pUserID+" AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44 ORDER BY D_COM_DOC_CANCEL_REQUEST.REQ_NO";
            }
            
            //strSQL="SELECT D_COM_DOC_CANCEL_REQUEST.REQ_NO,D_COM_DOC_CANCEL_REQUEST.REQ_DATE FROM D_COM_DOC_CANCEL_REQUEST,FILTERFABRIC.FF_TRD_DOC_DATA WHERE D_COM_DOC_CANCEL_REQUEST.REQ_NO=FILTERFABRIC.FF_TRD_DOC_DATA.DOC_NO AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID=FILTERFABRIC.FF_TRD_DOC_DATA.COMPANY_ID AND D_COM_DOC_CANCEL_REQUEST.COMPANY_ID="+Integer.toString(pCompanyID)+" AND FILTERFABRIC.FF_TRD_DOC_DATA.USER_ID="+pUserID+" AND FILTERFABRIC.FF_TRD_DOC_DATA.STATUS='W' AND FILTERFABRIC.FF_TRD_DOC_DATA.MODULE_ID=44";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("REQ_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsInvoiceEntry ObjDoc=new clsInvoiceEntry();
                    
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED FROM D_COM_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"' AND CANCELLED=0");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_COM_DOC_CANCEL_REQUEST WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=44;
                    
                    data.Execute("DELETE FROM FILTERFABRIC.FF_TRD_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_COM_DOC_CANCEL_REQUEST SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+pDocNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    public static String getParyName(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT PARTY_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                PartyName = rsTmp.getString("PARTY_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return PartyName;
    }
    public static String getChargeCode1(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("CHARGE_CODE");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
    public static String getChargeCode2(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT CHARGE_CODE_II FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                ChargeCode = rsTmp.getString("CHARGE_CODE_II");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return ChargeCode;
    }
    public static String getTransporter(int pCompanyID, String pPartyID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Transporter = "";

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT T.TRANSPORTER_NAME FROM DINESHMILLS.D_SAL_PARTY_MASTER M,DINESHMILLS.D_SAL_TRANSPORTER_MASTER T WHERE M.TRANSPORTER_ID=T.TRANSPORTER_ID AND PARTY_CODE=" + pPartyID);
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                Transporter = rsTmp.getString("TRANSPORTER_NAME");
            }

            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        return Transporter;
    }
    public static String getNextDocNo(){
       Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String DocNo = "";
        int cnt=0;

        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT COUNT(*) AS DOC FROM FILTERFABRIC.FF_TRD_INVOICE_PARAMETER "
                    + " WHERE LEFT(DOC_NO,8)='"+String.valueOf(EITLERPGLOBAL.getCurrentYear())+String.valueOf(100+EITLERPGLOBAL.getCurrentMonth()).substring(1,3)+String.valueOf(100+EITLERPGLOBAL.getCurrentDay()).substring(1,3)+"'");
            rsTmp.first();

            if (rsTmp.getRow() > 0) {
                cnt=rsTmp.getInt("DOC");
                
            }
            cnt++;
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();

        } catch (Exception e) {

        }
        DocNo=String.valueOf(100+cnt).substring(1, 3);
        return DocNo;
    }
}
