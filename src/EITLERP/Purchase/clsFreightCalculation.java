/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Purchase;

import java.util.*;
import java.sql.*;
import EITLERP.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsFreightCalculation{
    
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
    public clsFreightCalculation() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("CONSIGNMENT",new Variant(""));
        props.put("SUPP_ID",new Variant(""));
        props.put("AIRWAY_BILL",new Variant(""));
        props.put("FLIGHT",new Variant(""));
        props.put("IGM",new Variant(""));
        props.put("GROSS_WEIGHT",new Variant(0));
        props.put("GROSS_UNIT_ID",new Variant(0));
        props.put("CARTON_WEIGHT",new Variant(0));
        props.put("CARTON_UNIT_ID",new Variant(0));
        props.put("CARTONS",new Variant(""));
        props.put("SIZE",new Variant(""));
        props.put("CBM",new Variant(""));
        props.put("AGENT_AIRLINES",new Variant(""));
        props.put("AGENT_INDIAN_AGENT",new Variant(""));
        props.put("AGENT_FREIGHT_PO",new Variant(0));
        props.put("AGENT_FREIGHT_AWB",new Variant(0));
        props.put("AGENT_AWB_RATE",new Variant(0));
        props.put("AGENT_FREIGHT_INVOICE",new Variant(0));
        props.put("AGENT_DIFFERENCE",new Variant(0));
        props.put("AGENT_AIRPORT",new Variant(""));
        props.put("CURRENCY_ID",new Variant(0));
        props.put("SUPP_AIRLINES",new Variant(""));
        props.put("SUPP_INDIAN_AGENT",new Variant(""));
        props.put("SUPP_FREIGHT_PO",new Variant(0));
        props.put("SUPP_FREIGHT_AWB",new Variant(0));
        props.put("SUPP_AWB_RATE",new Variant(0));
        props.put("SUPP_FREIGHT_INVOICE",new Variant(0));
        props.put("SUPP_DIFFERENCE",new Variant(0));
        props.put("SUPP_AIRPORT",new Variant(""));
        props.put("REMARKS",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        props.put("CANCELLED",new Variant(false));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            
            rsTmp=data.getResult("SELECT DOC_NO FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND CANCELLED=0");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=43;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_FREIGHT_CALCULATION SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO");
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
        Statement stTmp,stItem,stHistory,stHDetail;
        ResultSet rsTmp,rsItem,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL="";
        int CompanyID=0;
        
        try {
            
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("DOC_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_FREIGHT_CALCULATION_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE APPROVAL_NO='1'");
            //rsHeader.first();
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //--------- Generate New Approval No. ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,43,(int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsResultSet.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsResultSet.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsResultSet.updateString("CONSIGNMENT",(String)getAttribute("CONSIGNMENT").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("AIRWAY_BILL",(String)getAttribute("AIRWAY_BILL").getObj());
            rsResultSet.updateString("FLIGHT",(String)getAttribute("FLIGHT").getObj());
            rsResultSet.updateString("IGM",(String)getAttribute("IGM").getObj());
            rsResultSet.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsResultSet.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsResultSet.updateDouble("CARTON_WEIGHT",getAttribute("CARTON_WEIGHT").getVal());
            rsResultSet.updateInt("CARTON_UNIT_ID",(int)getAttribute("CARTON_UNIT_ID").getVal());
            rsResultSet.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsResultSet.updateString("SIZE",(String)getAttribute("SIZE").getObj());
            rsResultSet.updateString("CBM",(String)getAttribute("CBM").getObj());
            rsResultSet.updateString("AGENT_AIRLINES",(String)getAttribute("AGENT_AIRLINES").getObj());
            rsResultSet.updateString("AGENT_INDIAN_AGENT",(String)getAttribute("AGENT_INDIAN_AGENT").getObj());
            rsResultSet.updateDouble("AGENT_FREIGHT_PO",getAttribute("AGENT_FREIGHT_PO").getVal());
            rsResultSet.updateDouble("AGENT_FREIGHT_AWB",getAttribute("AGENT_FREIGHT_AWB").getVal());
            rsResultSet.updateDouble("AGENT_AWB_RATE",getAttribute("AGENT_AWB_RATE").getVal());
            rsResultSet.updateDouble("AGENT_FREIGHT_INVOICE",getAttribute("AGENT_FREIGHT_INVOICE").getVal());
            rsResultSet.updateDouble("AGENT_DIFFERENCE",getAttribute("AGENT_DIFFERENCE").getVal());
            rsResultSet.updateString("AGENT_AIRPORT",(String)getAttribute("AGENT_AIRPORT").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateString("SUPP_AIRLINES",(String)getAttribute("SUPP_AIRLINES").getObj());
            rsResultSet.updateString("SUPP_INDIAN_AGENT",(String)getAttribute("SUPP_INDIAN_AGENT").getObj());
            rsResultSet.updateDouble("SUPP_FREIGHT_PO",getAttribute("SUPP_FREIGHT_PO").getVal());
            rsResultSet.updateDouble("SUPP_FREIGHT_AWB",getAttribute("SUPP_FREIGHT_AWB").getVal());
            rsResultSet.updateDouble("SUPP_AWB_RATE",getAttribute("SUPP_AWB_RATE").getVal());
            rsResultSet.updateDouble("SUPP_FREIGHT_INVOICE",getAttribute("SUPP_FREIGHT_INVOICE").getVal());
            rsResultSet.updateDouble("SUPP_DIFFERENCE",getAttribute("SUPP_DIFFERENCE").getVal());
            rsResultSet.updateString("SUPP_AIRPORT",(String)getAttribute("SUPP_AIRPORT").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            //rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsHistory.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateString("CONSIGNMENT",(String)getAttribute("CONSIGNMENT").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("AIRWAY_BILL",(String)getAttribute("AIRWAY_BILL").getObj());
            rsHistory.updateString("FLIGHT",(String)getAttribute("FLIGHT").getObj());
            rsHistory.updateString("IGM",(String)getAttribute("IGM").getObj());
            rsHistory.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsHistory.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsHistory.updateDouble("CARTON_WEIGHT",getAttribute("CARTON_WEIGHT").getVal());
            rsHistory.updateInt("CARTON_UNIT_ID",(int)getAttribute("CARTON_UNIT_ID").getVal());
            rsHistory.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsHistory.updateString("SIZE",(String)getAttribute("SIZE").getObj());
            rsHistory.updateString("CBM",(String)getAttribute("CBM").getObj());
            rsHistory.updateString("AGENT_AIRLINES",(String)getAttribute("AGENT_AIRLINES").getObj());
            rsHistory.updateString("AGENT_INDIAN_AGENT",(String)getAttribute("AGENT_INDIAN_AGENT").getObj());
            rsHistory.updateDouble("AGENT_FREIGHT_PO",getAttribute("AGENT_FREIGHT_PO").getVal());
            rsHistory.updateDouble("AGENT_FREIGHT_AWB",getAttribute("AGENT_FREIGHT_AWB").getVal());
            rsHistory.updateDouble("AGENT_AWB_RATE",getAttribute("AGENT_AWB_RATE").getVal());
            rsHistory.updateDouble("AGENT_FREIGHT_INVOICE",getAttribute("AGENT_FREIGHT_INVOICE").getVal());
            rsHistory.updateDouble("AGENT_DIFFERENCE",getAttribute("AGENT_DIFFERENCE").getVal());
            rsHistory.updateString("AGENT_AIRPORT",(String)getAttribute("AGENT_AIRPORT").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateString("SUPP_AIRLINES",(String)getAttribute("SUPP_AIRLINES").getObj());
            rsHistory.updateString("SUPP_INDIAN_AGENT",(String)getAttribute("SUPP_INDIAN_AGENT").getObj());
            rsHistory.updateDouble("SUPP_FREIGHT_PO",getAttribute("SUPP_FREIGHT_PO").getVal());
            rsHistory.updateDouble("SUPP_FREIGHT_AWB",getAttribute("SUPP_FREIGHT_AWB").getVal());
            rsHistory.updateDouble("SUPP_AWB_RATE",getAttribute("SUPP_AWB_RATE").getVal());
            rsHistory.updateDouble("SUPP_FREIGHT_INVOICE",getAttribute("SUPP_FREIGHT_INVOICE").getVal());
            rsHistory.updateDouble("SUPP_DIFFERENCE",getAttribute("SUPP_DIFFERENCE").getVal());
            rsHistory.updateString("SUPP_AIRPORT",(String)getAttribute("SUPP_AIRPORT").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsHistory.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            //rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=43; //Freight Comparison
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_FREIGHT_CALCULATION";
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
            //--------- Approval Flow Update complete -----------//
            
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
        Statement stTmp,stItem,stHistory,stHDetail;
        ResultSet rsTmp,rsItem,rsHistory,rsHDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL="";
        int CompanyID=0;
        
        try {
            
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("DOC_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_FREIGHT_CALCULATION_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("DOC_NO").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(APPROVAL_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            
            rsResultSet.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsResultSet.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsResultSet.updateString("CONSIGNMENT",(String)getAttribute("CONSIGNMENT").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("AIRWAY_BILL",(String)getAttribute("AIRWAY_BILL").getObj());
            rsResultSet.updateString("FLIGHT",(String)getAttribute("FLIGHT").getObj());
            rsResultSet.updateString("IGM",(String)getAttribute("IGM").getObj());
            rsResultSet.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsResultSet.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsResultSet.updateDouble("CARTON_WEIGHT",getAttribute("CARTON_WEIGHT").getVal());
            rsResultSet.updateInt("CARTON_UNIT_ID",(int)getAttribute("CARTON_UNIT_ID").getVal());
            rsResultSet.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsResultSet.updateString("SIZE",(String)getAttribute("SIZE").getObj());
            rsResultSet.updateString("CBM",(String)getAttribute("CBM").getObj());
            rsResultSet.updateString("AGENT_AIRLINES",(String)getAttribute("AGENT_AIRLINES").getObj());
            rsResultSet.updateString("AGENT_INDIAN_AGENT",(String)getAttribute("AGENT_INDIAN_AGENT").getObj());
            rsResultSet.updateDouble("AGENT_FREIGHT_PO",getAttribute("AGENT_FREIGHT_PO").getVal());
            rsResultSet.updateDouble("AGENT_FREIGHT_AWB",getAttribute("AGENT_FREIGHT_AWB").getVal());
            rsResultSet.updateDouble("AGENT_AWB_RATE",getAttribute("AGENT_AWB_RATE").getVal());
            rsResultSet.updateDouble("AGENT_FREIGHT_INVOICE",getAttribute("AGENT_FREIGHT_INVOICE").getVal());
            rsResultSet.updateDouble("AGENT_DIFFERENCE",getAttribute("AGENT_DIFFERENCE").getVal());
            rsResultSet.updateString("AGENT_AIRPORT",(String)getAttribute("AGENT_AIRPORT").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateString("SUPP_AIRLINES",(String)getAttribute("SUPP_AIRLINES").getObj());
            rsResultSet.updateString("SUPP_INDIAN_AGENT",(String)getAttribute("SUPP_INDIAN_AGENT").getObj());
            rsResultSet.updateDouble("SUPP_FREIGHT_PO",getAttribute("SUPP_FREIGHT_PO").getVal());
            rsResultSet.updateDouble("SUPP_FREIGHT_AWB",getAttribute("SUPP_FREIGHT_AWB").getVal());
            rsResultSet.updateDouble("SUPP_AWB_RATE",getAttribute("SUPP_AWB_RATE").getVal());
            rsResultSet.updateDouble("SUPP_FREIGHT_INVOICE",getAttribute("SUPP_FREIGHT_INVOICE").getVal());
            rsResultSet.updateDouble("SUPP_DIFFERENCE",getAttribute("SUPP_DIFFERENCE").getVal());
            rsResultSet.updateString("SUPP_AIRPORT",(String)getAttribute("SUPP_AIRPORT").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            //rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            //rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_FREIGHT_CALCULATION_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
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
            rsHistory.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateString("CONSIGNMENT",(String)getAttribute("CONSIGNMENT").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("AIRWAY_BILL",(String)getAttribute("AIRWAY_BILL").getObj());
            rsHistory.updateString("FLIGHT",(String)getAttribute("FLIGHT").getObj());
            rsHistory.updateString("IGM",(String)getAttribute("IGM").getObj());
            rsHistory.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsHistory.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsHistory.updateDouble("CARTON_WEIGHT",getAttribute("CARTON_WEIGHT").getVal());
            rsHistory.updateInt("CARTON_UNIT_ID",(int)getAttribute("CARTON_UNIT_ID").getVal());
            rsHistory.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsHistory.updateString("SIZE",(String)getAttribute("SIZE").getObj());
            rsHistory.updateString("CBM",(String)getAttribute("CBM").getObj());
            rsHistory.updateString("AGENT_AIRLINES",(String)getAttribute("AGENT_AIRLINES").getObj());
            rsHistory.updateString("AGENT_INDIAN_AGENT",(String)getAttribute("AGENT_INDIAN_AGENT").getObj());
            rsHistory.updateDouble("AGENT_FREIGHT_PO",getAttribute("AGENT_FREIGHT_PO").getVal());
            rsHistory.updateDouble("AGENT_FREIGHT_AWB",getAttribute("AGENT_FREIGHT_AWB").getVal());
            rsHistory.updateDouble("AGENT_AWB_RATE",getAttribute("AGENT_AWB_RATE").getVal());
            rsHistory.updateDouble("AGENT_FREIGHT_INVOICE",getAttribute("AGENT_FREIGHT_INVOICE").getVal());
            rsHistory.updateDouble("AGENT_DIFFERENCE",getAttribute("AGENT_DIFFERENCE").getVal());
            rsHistory.updateString("AGENT_AIRPORT",(String)getAttribute("AGENT_AIRPORT").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateString("SUPP_AIRLINES",(String)getAttribute("SUPP_AIRLINES").getObj());
            rsHistory.updateString("SUPP_INDIAN_AGENT",(String)getAttribute("SUPP_INDIAN_AGENT").getObj());
            rsHistory.updateDouble("SUPP_FREIGHT_PO",getAttribute("SUPP_FREIGHT_PO").getVal());
            rsHistory.updateDouble("SUPP_FREIGHT_AWB",getAttribute("SUPP_FREIGHT_AWB").getVal());
            rsHistory.updateDouble("SUPP_AWB_RATE",getAttribute("SUPP_AWB_RATE").getVal());
            rsHistory.updateDouble("SUPP_FREIGHT_INVOICE",getAttribute("SUPP_FREIGHT_INVOICE").getVal());
            rsHistory.updateDouble("SUPP_DIFFERENCE",getAttribute("SUPP_DIFFERENCE").getVal());
            rsHistory.updateString("SUPP_AIRPORT",(String)getAttribute("SUPP_AIRPORT").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            //rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=43; //
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_FREIGHT_CALCULATION";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_FREIGHT_CALCULATION SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=43 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
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
            //--------- Approval Flow Update complete -----------//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("DOC_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                data.Execute("DELETE FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+lCompanyID+" AND DOC_NO='"+lDocNo.trim()+"'");
                
                LoadData(lCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted. Either it is Approved/Rejected. Only creator of the document can delete.";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"'";
        clsFreightCalculation ObjFCM = new clsFreightCalculation();
        ObjFCM.Filter(strCondition,pCompanyID);
        return ObjFCM;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_FREIGHT_CALCULATION " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
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
        Statement stItem,stTmp;
        ResultSet rsItem,rsTmp;
        String ApprovalNo="";
        int CompanyID=0,ItemCounter=0,SrNo=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            setAttribute("DOC_NO",rsResultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",rsResultSet.getString("DOC_DATE"));
            setAttribute("PO_NO",rsResultSet.getString("PO_NO"));
            setAttribute("PO_DATE",rsResultSet.getString("PO_DATE"));
            setAttribute("CONSIGNMENT",rsResultSet.getString("CONSIGNMENT"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("AIRWAY_BILL",rsResultSet.getString("AIRWAY_BILL"));
            setAttribute("FLIGHT",rsResultSet.getString("FLIGHT"));
            setAttribute("IGM",rsResultSet.getString("IGM"));
            setAttribute("GROSS_WEIGHT",rsResultSet.getDouble("GROSS_WEIGHT"));
            setAttribute("GROSS_UNIT_ID",rsResultSet.getInt("GROSS_UNIT_ID"));
            setAttribute("CARTON_WEIGHT",rsResultSet.getDouble("CARTON_WEIGHT"));
            setAttribute("CARTON_UNIT_ID",rsResultSet.getInt("CARTON_UNIT_ID"));
            setAttribute("CARTONS",rsResultSet.getString("CARTONS"));
            setAttribute("SIZE",rsResultSet.getString("SIZE"));
            setAttribute("CBM",rsResultSet.getString("CBM"));
            setAttribute("AGENT_AIRLINES",rsResultSet.getString("AGENT_AIRLINES"));
            setAttribute("AGENT_INDIAN_AGENT",rsResultSet.getString("AGENT_INDIAN_AGENT"));
            setAttribute("AGENT_FREIGHT_PO",rsResultSet.getDouble("AGENT_FREIGHT_PO"));
            setAttribute("AGENT_FREIGHT_AWB",rsResultSet.getDouble("AGENT_FREIGHT_AWB"));
            setAttribute("AGENT_AWB_RATE",rsResultSet.getDouble("AGENT_AWB_RATE"));
            setAttribute("AGENT_FREIGHT_INVOICE",rsResultSet.getDouble("AGENT_FREIGHT_INVOICE"));
            setAttribute("AGENT_DIFFERENCE",rsResultSet.getDouble("AGENT_DIFFERENCE"));
            setAttribute("AGENT_AIRPORT",rsResultSet.getString("AGENT_AIRPORT"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("SUPP_AIRLINES",rsResultSet.getString("SUPP_AIRLINES"));
            setAttribute("SUPP_INDIAN_AGENT",rsResultSet.getString("SUPP_INDIAN_AGENT"));
            setAttribute("SUPP_FREIGHT_PO",rsResultSet.getDouble("SUPP_FREIGHT_PO"));
            setAttribute("SUPP_FREIGHT_AWB",rsResultSet.getDouble("SUPP_FREIGHT_AWB"));
            setAttribute("SUPP_AWB_RATE",rsResultSet.getDouble("SUPP_AWB_RATE"));
            setAttribute("SUPP_FREIGHT_INVOICE",rsResultSet.getDouble("SUPP_FREIGHT_INVOICE"));
            setAttribute("SUPP_DIFFERENCE",rsResultSet.getDouble("SUPP_DIFFERENCE"));
            setAttribute("SUPP_AIRPORT",rsResultSet.getString("SUPP_AIRPORT"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=43 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=43 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
                strSQL="SELECT D_PUR_FREIGHT_CALCULATION.DOC_NO,D_PUR_FREIGHT_CALCULATION.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_FREIGHT_CALCULATION,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_CALCULATION.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=43 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_PUR_FREIGHT_CALCULATION.DOC_NO,D_PUR_FREIGHT_CALCULATION.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_FREIGHT_CALCULATION,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_CALCULATION.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=43 ORDER BY D_PUR_FREIGHT_CALCULATION.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_PUR_FREIGHT_CALCULATION.DOC_NO,D_PUR_FREIGHT_CALCULATION.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_FREIGHT_CALCULATION,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_CALCULATION.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=43 ORDER BY D_PUR_FREIGHT_CALCULATION.DOC_NO";
            }
            
            
            //strSQL="SELECT D_PUR_FREIGHT_CALCULATION.DOC_NO,D_PUR_FREIGHT_CALCULATION.DOC_DATE FROM D_PUR_FREIGHT_CALCULATION,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_CALCULATION.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_CALCULATION.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=43";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("DOC_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsFreightCalculation ObjDoc=new clsFreightCalculation();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
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
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_FREIGHT_CALCULATION_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_FREIGHT_CALCULATION_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsFreightCalculation ObjApproval=new clsFreightCalculation();
                
                ObjApproval.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjApproval.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjApproval.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjApproval.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjApproval.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjApproval.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjApproval.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjApproval);
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
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED FROM D_PUR_FREIGHT_CALCULATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    strMessage="";
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
    
}
