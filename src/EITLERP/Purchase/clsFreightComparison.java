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

public class clsFreightComparison{
    
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
    public clsFreightComparison() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("DESCRIPTION",new Variant(""));
        props.put("SUPP_ID",new Variant(""));
        props.put("ORDER_MODE",new Variant(""));
        props.put("TRANSPORT_ID",new Variant(0));
        props.put("GROSS_WEIGHT",new Variant(0));
        props.put("CARTONS",new Variant(""));
        props.put("VOLUME_CMT",new Variant(0));
        props.put("VOLUME_WEIGHT",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("CURRENCY_ID",new Variant(0));
        props.put("CURRENCY_RATE",new Variant(0));
        props.put("SUPP_PICKUP",new Variant(0));
        props.put("SUPP_MIN_CHARGES",new Variant(0));
        props.put("SUPP_CC_FEES",new Variant(0));
        props.put("SUPP_FSC",new Variant(0));
        props.put("SUPP_SSC",new Variant(0));
        props.put("SUPP_OTHERS",new Variant(0));
        props.put("SUPP_LOCAL_CHARGES",new Variant(0));
        props.put("SUPP_LOCAL_CHARGES_DESC",new Variant(""));
        props.put("AGENT_PICKUP",new Variant(0));
        props.put("AGENT_MIN_CHARGES",new Variant(0));
        props.put("AGENT_CC_FEES",new Variant(0));
        props.put("AGENT_FSC",new Variant(0));
        props.put("AGENT_OTHERS",new Variant(0));
        props.put("AGENT_LOCAL_DO_FEES",new Variant(0));
        props.put("AGENT_LOCAL_BB",new Variant(0));
        props.put("AGENT_LOCAL_CARTAGE",new Variant(0));
        props.put("AGENT_LOCAL_OTHERS",new Variant(0));
        props.put("SUPP_EXWORKS_SELECTED",new Variant(false));
        props.put("SUPP_FREIGHT_SELECTED",new Variant(false));
        props.put("SUPP_LOCAL_SELECTED",new Variant(false));
        props.put("AFL_EXWORKS_SELECTED",new Variant(false));
        props.put("AFL_FREIGHT_SELECTED",new Variant(false));
        props.put("AFL_LOCAL_SELECTED",new Variant(false));
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
            
            rsTmp=data.getResult("SELECT DOC_NO FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND CANCELLED=0");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=42;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_FREIGHT_COMPARISON SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO"); 
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
    
    
    public void Close()
    {
      try
      {
         
        rsResultSet.close();
        Stmt.close();
        //Conn.close();
      }
      catch(Exception e)
      {
          
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
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0))
            {
               //Withing the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }*/
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_FREIGHT_COMPARISON_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_APPROVAL_HEADER WHERE APPROVAL_NO='1'");
            //rsHeader.first();
                        
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //--------- Generate New Approval No. ------------
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,42,(int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("DOC_NO",(String)getAttribute("DOC_NO").getObj());
            rsResultSet.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsResultSet.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsResultSet.updateString("DESCRIPTION",(String)getAttribute("DESCRIPTION").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("ORDER_MODE",(String)getAttribute("ORDER_MODE").getObj());
            rsResultSet.updateInt("TRANSPORT_ID",(int)getAttribute("TRANSPORT_ID").getVal());
            rsResultSet.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsResultSet.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsResultSet.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsResultSet.updateDouble("VOLUME_CMT",getAttribute("VOLUME_CMT").getVal());
            rsResultSet.updateDouble("VOLUME_WEIGHT",getAttribute("VOLUME_WEIGHT").getVal());
            rsResultSet.updateInt("VOLUME_UNIT_ID",(int)getAttribute("VOLUME_UNIT_ID").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("SUPP_PICKUP",getAttribute("SUPP_PICKUP").getVal());
            rsResultSet.updateDouble("SUPP_MIN_CHARGES",getAttribute("SUPP_MIN_CHARGES").getVal());
            rsResultSet.updateDouble("SUPP_CC_FEES",getAttribute("SUPP_CC_FEES").getVal());
            rsResultSet.updateDouble("SUPP_FSC",getAttribute("SUPP_FSC").getVal());
            rsResultSet.updateDouble("SUPP_SSC",getAttribute("SUPP_SSC").getVal());
            rsResultSet.updateDouble("SUPP_OTHERS",getAttribute("SUPP_OTHERS").getVal());
            rsResultSet.updateDouble("SUPP_LOCAL_CHARGES",getAttribute("SUPP_LOCAL_CHARGES").getVal());
            rsResultSet.updateString("SUPP_LOCAL_CHARGES_DESC",(String)getAttribute("SUPP_LOCAL_CHARGES_DESC").getObj());
            rsResultSet.updateDouble("AGENT_PICKUP",getAttribute("AGENT_PICKUP").getVal());
            rsResultSet.updateDouble("AGENT_MIN_CHARGES",getAttribute("AGENT_MIN_CHARGES").getVal());
            rsResultSet.updateDouble("AGENT_CC_FEES",getAttribute("AGENT_CC_FEES").getVal());
            rsResultSet.updateDouble("AGENT_FSC",getAttribute("AGENT_FSC").getVal());
            rsResultSet.updateDouble("AGENT_SSC",getAttribute("AGENT_SSC").getVal());
            rsResultSet.updateDouble("AGENT_OTHERS",getAttribute("AGENT_OTHERS").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_DO_FEES",getAttribute("AGENT_LOCAL_DO_FEES").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_BB",getAttribute("AGENT_LOCAL_BB").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_CARTAGE",getAttribute("AGENT_LOCAL_CARTAGE").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_OTHERS",getAttribute("AGENT_LOCAL_OTHERS").getVal());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());            
            
            rsResultSet.updateBoolean("SUPP_EXWORKS_SELECTED",getAttribute("SUPP_EXWORKS_SELECTED").getBool());
            rsResultSet.updateBoolean("SUPP_FREIGHT_SELECTED",getAttribute("SUPP_FREIGHT_SELECTED").getBool());
            rsResultSet.updateBoolean("SUPP_LOCAL_SELECTED",getAttribute("SUPP_LOCAL_SELECTED").getBool());
            rsResultSet.updateBoolean("AFL_EXWORKS_SELECTED",getAttribute("AFL_EXWORKS_SELECTED").getBool());
            rsResultSet.updateBoolean("AFL_FREIGHT_SELECTED",getAttribute("AFL_FREIGHT_SELECTED").getBool());
            rsResultSet.updateBoolean("AFL_LOCAL_SELECTED",getAttribute("AFL_LOCAL_SELECTED").getBool());
                       
            
            
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
            rsHistory.updateString("DESCRIPTION",(String)getAttribute("DESCRIPTION").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("ORDER_MODE",(String)getAttribute("ORDER_MODE").getObj());
            rsHistory.updateInt("TRANSPORT_ID",(int)getAttribute("TRANSPORT_ID").getVal());
            rsHistory.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsHistory.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsHistory.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsHistory.updateDouble("VOLUME_CMT",getAttribute("VOLUME_CMT").getVal());
            rsHistory.updateDouble("VOLUME_WEIGHT",getAttribute("VOLUME_WEIGHT").getVal());
            rsHistory.updateInt("VOLUME_UNIT_ID",(int)getAttribute("VOLUME_UNIT_ID").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("SUPP_PICKUP",getAttribute("SUPP_PICKUP").getVal());
            rsHistory.updateDouble("SUPP_MIN_CHARGES",getAttribute("SUPP_MIN_CHARGES").getVal());
            rsHistory.updateDouble("SUPP_CC_FEES",getAttribute("SUPP_CC_FEES").getVal());
            rsHistory.updateDouble("SUPP_FSC",getAttribute("SUPP_FSC").getVal());
            rsHistory.updateDouble("SUPP_SSC",getAttribute("SUPP_SSC").getVal());
            rsHistory.updateDouble("SUPP_OTHERS",getAttribute("SUPP_OTHERS").getVal());
            rsHistory.updateDouble("SUPP_LOCAL_CHARGES",getAttribute("SUPP_LOCAL_CHARGES").getVal());
            rsHistory.updateString("SUPP_LOCAL_CHARGES_DESC",(String)getAttribute("SUPP_LOCAL_CHARGES_DESC").getObj());
            rsHistory.updateDouble("AGENT_PICKUP",getAttribute("AGENT_PICKUP").getVal());
            rsHistory.updateDouble("AGENT_MIN_CHARGES",getAttribute("AGENT_MIN_CHARGES").getVal());
            rsHistory.updateDouble("AGENT_CC_FEES",getAttribute("AGENT_CC_FEES").getVal());
            rsHistory.updateDouble("AGENT_FSC",getAttribute("AGENT_FSC").getVal());
            rsHistory.updateDouble("AGENT_SSC",getAttribute("AGENT_SSC").getVal());
            rsHistory.updateDouble("AGENT_OTHERS",getAttribute("AGENT_OTHERS").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_DO_FEES",getAttribute("AGENT_LOCAL_DO_FEES").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_BB",getAttribute("AGENT_LOCAL_BB").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_CARTAGE",getAttribute("AGENT_LOCAL_CARTAGE").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_OTHERS",getAttribute("AGENT_LOCAL_OTHERS").getVal());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());            
            
            rsHistory.updateBoolean("SUPP_EXWORKS_SELECTED",getAttribute("SUPP_EXWORKS_SELECTED").getBool());
            rsHistory.updateBoolean("SUPP_FREIGHT_SELECTED",getAttribute("SUPP_FREIGHT_SELECTED").getBool());
            rsHistory.updateBoolean("SUPP_LOCAL_SELECTED",getAttribute("SUPP_LOCAL_SELECTED").getBool());
            rsHistory.updateBoolean("AFL_EXWORKS_SELECTED",getAttribute("AFL_EXWORKS_SELECTED").getBool());
            rsHistory.updateBoolean("AFL_FREIGHT_SELECTED",getAttribute("AFL_FREIGHT_SELECTED").getBool());
            rsHistory.updateBoolean("AFL_LOCAL_SELECTED",getAttribute("AFL_LOCAL_SELECTED").getBool());
                        
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
            ObjFlow.ModuleID=42; //Freight Comparison
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_FREIGHT_COMPARISON";
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
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0))
            {
               //Withing the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }*/
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_FREIGHT_COMPARISON_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
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
            rsResultSet.updateString("DESCRIPTION",(String)getAttribute("DESCRIPTION").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("ORDER_MODE",(String)getAttribute("ORDER_MODE").getObj());
            rsResultSet.updateInt("TRANSPORT_ID",(int)getAttribute("TRANSPORT_ID").getVal());
            rsResultSet.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsResultSet.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsResultSet.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsResultSet.updateDouble("VOLUME_CMT",getAttribute("VOLUME_CMT").getVal());
            rsResultSet.updateDouble("VOLUME_WEIGHT",getAttribute("VOLUME_WEIGHT").getVal());
            rsResultSet.updateInt("VOLUME_UNIT_ID",(int)getAttribute("VOLUME_UNIT_ID").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("SUPP_PICKUP",getAttribute("SUPP_PICKUP").getVal());
            rsResultSet.updateDouble("SUPP_MIN_CHARGES",getAttribute("SUPP_MIN_CHARGES").getVal());
            rsResultSet.updateDouble("SUPP_CC_FEES",getAttribute("SUPP_CC_FEES").getVal());
            rsResultSet.updateDouble("SUPP_FSC",getAttribute("SUPP_FSC").getVal());
            rsResultSet.updateDouble("SUPP_SSC",getAttribute("SUPP_SSC").getVal());
            rsResultSet.updateDouble("SUPP_OTHERS",getAttribute("SUPP_OTHERS").getVal());
            rsResultSet.updateDouble("SUPP_LOCAL_CHARGES",getAttribute("SUPP_LOCAL_CHARGES").getVal());
            rsResultSet.updateString("SUPP_LOCAL_CHARGES_DESC",(String)getAttribute("SUPP_LOCAL_CHARGES_DESC").getObj());
            rsResultSet.updateDouble("AGENT_PICKUP",getAttribute("AGENT_PICKUP").getVal());
            rsResultSet.updateDouble("AGENT_MIN_CHARGES",getAttribute("AGENT_MIN_CHARGES").getVal());
            rsResultSet.updateDouble("AGENT_CC_FEES",getAttribute("AGENT_CC_FEES").getVal());
            rsResultSet.updateDouble("AGENT_FSC",getAttribute("AGENT_FSC").getVal());
            rsResultSet.updateDouble("AGENT_SSC",getAttribute("AGENT_SSC").getVal());
            rsResultSet.updateDouble("AGENT_OTHERS",getAttribute("AGENT_OTHERS").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_DO_FEES",getAttribute("AGENT_LOCAL_DO_FEES").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_BB",getAttribute("AGENT_LOCAL_BB").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_CARTAGE",getAttribute("AGENT_LOCAL_CARTAGE").getVal());
            rsResultSet.updateDouble("AGENT_LOCAL_OTHERS",getAttribute("AGENT_LOCAL_OTHERS").getVal());
            
            rsResultSet.updateBoolean("SUPP_EXWORKS_SELECTED",getAttribute("SUPP_EXWORKS_SELECTED").getBool());
            rsResultSet.updateBoolean("SUPP_FREIGHT_SELECTED",getAttribute("SUPP_FREIGHT_SELECTED").getBool());
            rsResultSet.updateBoolean("SUPP_LOCAL_SELECTED",getAttribute("SUPP_LOCAL_SELECTED").getBool());
            rsResultSet.updateBoolean("AFL_EXWORKS_SELECTED",getAttribute("AFL_EXWORKS_SELECTED").getBool());
            rsResultSet.updateBoolean("AFL_FREIGHT_SELECTED",getAttribute("AFL_FREIGHT_SELECTED").getBool());
            rsResultSet.updateBoolean("AFL_LOCAL_SELECTED",getAttribute("AFL_LOCAL_SELECTED").getBool());
            
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
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_FREIGHT_COMPARISON_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
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
            rsHistory.updateString("DESCRIPTION",(String)getAttribute("DESCRIPTION").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("ORDER_MODE",(String)getAttribute("ORDER_MODE").getObj());
            rsHistory.updateInt("TRANSPORT_ID",(int)getAttribute("TRANSPORT_ID").getVal());
            rsHistory.updateDouble("GROSS_WEIGHT",getAttribute("GROSS_WEIGHT").getVal());
            rsHistory.updateInt("GROSS_UNIT_ID",(int)getAttribute("GROSS_UNIT_ID").getVal());
            rsHistory.updateString("CARTONS",(String)getAttribute("CARTONS").getObj());
            rsHistory.updateDouble("VOLUME_CMT",getAttribute("VOLUME_CMT").getVal());
            rsHistory.updateDouble("VOLUME_WEIGHT",getAttribute("VOLUME_WEIGHT").getVal());
            rsHistory.updateInt("VOLUME_UNIT_ID",(int)getAttribute("VOLUME_UNIT_ID").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("SUPP_PICKUP",getAttribute("SUPP_PICKUP").getVal());
            rsHistory.updateDouble("SUPP_MIN_CHARGES",getAttribute("SUPP_MIN_CHARGES").getVal());
            rsHistory.updateDouble("SUPP_CC_FEES",getAttribute("SUPP_CC_FEES").getVal());
            rsHistory.updateDouble("SUPP_FSC",getAttribute("SUPP_FSC").getVal());
            rsHistory.updateDouble("SUPP_SSC",getAttribute("SUPP_SSC").getVal());
            rsHistory.updateDouble("SUPP_OTHERS",getAttribute("SUPP_OTHERS").getVal());
            rsHistory.updateDouble("SUPP_LOCAL_CHARGES",getAttribute("SUPP_LOCAL_CHARGES").getVal());
            rsHistory.updateString("SUPP_LOCAL_CHARGES_DESC",(String)getAttribute("SUPP_LOCAL_CHARGES_DESC").getObj());
            rsHistory.updateDouble("AGENT_PICKUP",getAttribute("AGENT_PICKUP").getVal());
            rsHistory.updateDouble("AGENT_MIN_CHARGES",getAttribute("AGENT_MIN_CHARGES").getVal());
            rsHistory.updateDouble("AGENT_CC_FEES",getAttribute("AGENT_CC_FEES").getVal());
            rsHistory.updateDouble("AGENT_FSC",getAttribute("AGENT_FSC").getVal());
            rsHistory.updateDouble("AGENT_SSC",getAttribute("AGENT_SSC").getVal());
            rsHistory.updateDouble("AGENT_OTHERS",getAttribute("AGENT_OTHERS").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_DO_FEES",getAttribute("AGENT_LOCAL_DO_FEES").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_BB",getAttribute("AGENT_LOCAL_BB").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_CARTAGE",getAttribute("AGENT_LOCAL_CARTAGE").getVal());
            rsHistory.updateDouble("AGENT_LOCAL_OTHERS",getAttribute("AGENT_LOCAL_OTHERS").getVal());
            
            rsHistory.updateBoolean("SUPP_EXWORKS_SELECTED",getAttribute("SUPP_EXWORKS_SELECTED").getBool());
            rsHistory.updateBoolean("SUPP_FREIGHT_SELECTED",getAttribute("SUPP_FREIGHT_SELECTED").getBool());
            rsHistory.updateBoolean("SUPP_LOCAL_SELECTED",getAttribute("SUPP_LOCAL_SELECTED").getBool());
            rsHistory.updateBoolean("AFL_EXWORKS_SELECTED",getAttribute("AFL_EXWORKS_SELECTED").getBool());
            rsHistory.updateBoolean("AFL_FREIGHT_SELECTED",getAttribute("AFL_FREIGHT_SELECTED").getBool());
            rsHistory.updateBoolean("AFL_LOCAL_SELECTED",getAttribute("AFL_LOCAL_SELECTED").getBool());
                        
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
            ObjFlow.ModuleID=42; //
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_FREIGHT_COMPARISON";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_FREIGHT_COMPARISON SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=42 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
                
                data.Execute("DELETE FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+lCompanyID+" AND DOC_NO='"+lDocNo.trim()+"'");
                                
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
        clsFreightComparison ObjFCM = new clsFreightComparison();
        ObjFCM.Filter(strCondition,pCompanyID);
        return ObjFCM;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_FREIGHT_COMPARISON " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
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
            if(HistoryView)
            {
              setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));  
            }
            else
            {
              setAttribute("REVISION_NO",0);  
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            setAttribute("DOC_NO",rsResultSet.getString("DOC_NO"));
            setAttribute("DOC_DATE",rsResultSet.getString("DOC_DATE"));
            setAttribute("PO_NO",rsResultSet.getString("PO_NO"));
            setAttribute("PO_DATE",rsResultSet.getString("PO_DATE"));
            setAttribute("DESCRIPTION",rsResultSet.getString("DESCRIPTION"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("ORDER_MODE",rsResultSet.getString("ORDER_MODE"));
            setAttribute("TRANSPORT_ID",rsResultSet.getInt("TRANSPORT_ID"));
            setAttribute("GROSS_WEIGHT",rsResultSet.getDouble("GROSS_WEIGHT"));
            setAttribute("CARTONS",rsResultSet.getString("CARTONS"));
            setAttribute("VOLUME_CMT",rsResultSet.getDouble("VOLUME_CMT"));
            setAttribute("VOLUME_WEIGHT",rsResultSet.getDouble("VOLUME_WEIGHT"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("GROSS_UNIT_ID",rsResultSet.getInt("GROSS_UNIT_ID"));
            setAttribute("VOLUME_UNIT_ID",rsResultSet.getInt("VOLUME_UNIT_ID"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("SUPP_PICKUP",rsResultSet.getDouble("SUPP_PICKUP"));
            setAttribute("SUPP_MIN_CHARGES",rsResultSet.getDouble("SUPP_MIN_CHARGES"));
            setAttribute("SUPP_CC_FEES",rsResultSet.getDouble("SUPP_CC_FEES"));
            setAttribute("SUPP_FSC",rsResultSet.getDouble("SUPP_FSC"));
            setAttribute("SUPP_SSC",rsResultSet.getDouble("SUPP_SSC"));
            setAttribute("SUPP_OTHERS",rsResultSet.getDouble("SUPP_OTHERS"));
            setAttribute("SUPP_LOCAL_CHARGES",rsResultSet.getDouble("SUPP_LOCAL_CHARGES"));
            setAttribute("SUPP_LOCAL_CHARGES_DESC",rsResultSet.getString("SUPP_LOCAL_CHARGES_DESC"));
            setAttribute("AGENT_PICKUP",rsResultSet.getDouble("AGENT_PICKUP"));
            setAttribute("AGENT_MIN_CHARGES",rsResultSet.getDouble("AGENT_MIN_CHARGES"));
            setAttribute("AGENT_CC_FEES",rsResultSet.getDouble("AGENT_CC_FEES"));
            setAttribute("AGENT_FSC",rsResultSet.getDouble("AGENT_FSC"));
            setAttribute("AGENT_SSC",rsResultSet.getDouble("AGENT_SSC"));
            setAttribute("AGENT_OTHERS",rsResultSet.getDouble("AGENT_OTHERS"));
            setAttribute("AGENT_LOCAL_DO_FEES",rsResultSet.getDouble("AGENT_LOCAL_DO_FEES"));
            setAttribute("AGENT_LOCAL_BB",rsResultSet.getDouble("AGENT_LOCAL_BB"));
            setAttribute("AGENT_LOCAL_CARTAGE",rsResultSet.getDouble("AGENT_LOCAL_CARTAGE"));
            setAttribute("AGENT_LOCAL_OTHERS",rsResultSet.getDouble("AGENT_LOCAL_OTHERS"));
            setAttribute("SUPP_EXWORKS_SELECTED",rsResultSet.getBoolean("SUPP_EXWORKS_SELECTED"));
            setAttribute("SUPP_FREIGHT_SELECTED",rsResultSet.getBoolean("SUPP_FREIGHT_SELECTED"));
            setAttribute("SUPP_LOCAL_SELECTED",rsResultSet.getBoolean("SUPP_LOCAL_SELECTED"));
            setAttribute("AFL_EXWORKS_SELECTED",rsResultSet.getBoolean("AFL_EXWORKS_SELECTED"));
            setAttribute("AFL_FREIGHT_SELECTED",rsResultSet.getBoolean("AFL_FREIGHT_SELECTED"));
            setAttribute("AFL_LOCAL_SELECTED",rsResultSet.getBoolean("AFL_LOCAL_SELECTED"));
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
            if(HistoryView)
            {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=42 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            if(HistoryView)
            {
              return false;  
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=42 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    
public static HashMap getQuotApprovalItems(int pCompanyID,String pInquiryNo)    
{
   String strSQL="";
   Connection tmpConn;
   Statement stTmp=null,stPO=null;
   ResultSet rsTmp=null,rsPO=null;
   
   HashMap List=new HashMap();
   int Counter=0;
   
   try
   {
     tmpConn=data.getConn();
     stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
     
     strSQL="SELECT D_PUR_QUOT_APPROVAL_DETAIL.* FROM D_PUR_QUOT_APPROVAL_HEADER,D_PUR_QUOT_APPROVAL_DETAIL WHERE D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID=D_PUR_QUOT_APPROVAL_DETAIL.COMPANY_ID AND D_PUR_QUOT_APPROVAL_HEADER.APPROVAL_NO=D_PUR_QUOT_APPROVAL_DETAIL.APPROVAL_NO AND D_PUR_QUOT_APPROVAL_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_QUOT_APPROVAL_HEADER.APPROVED=1 AND D_PUR_QUOT_APPROVAL_DETAIL.INQUIRY_NO='"+pInquiryNo+"' AND D_PUR_QUOT_APPROVAL_DETAIL.APPROVED=1 AND D_PUR_QUOT_APPROVAL_DETAIL.LAST_LANDED_RATE<>D_PUR_QUOT_APPROVAL_DETAIL.LAND_COST";
     rsTmp=stTmp.executeQuery(strSQL);
     rsTmp.first();
     
     if(rsTmp.getRow()>0)
     {
         Counter=0;
         while(!rsTmp.isAfterLast())
         {
             Counter++;
             clsQuotApprovalItem ObjItem=new clsQuotApprovalItem();
            
             double RateDiff=0,RateDiffPer=0;
             String PONo="",ItemID="";
                          
             ObjItem.setAttribute("SR_NO",Counter);
             ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_CODE"));
             ItemID=rsTmp.getString("ITEM_CODE");
             ObjItem.setAttribute("SUPP_ID",rsTmp.getString("SUPP_ID"));
             ObjItem.setAttribute("LAST_PO_NO",rsTmp.getString("LAST_PO_NO"));
             ObjItem.setAttribute("LAST_PO_DATE",rsTmp.getString("LAST_PO_DATE"));
             ObjItem.setAttribute("LAST_LANDED_RATE",rsTmp.getDouble("LAST_LANDED_RATE"));
             ObjItem.setAttribute("LAST_PO_RATE",rsTmp.getDouble("LAST_PO_RATE"));
             
             PONo=rsTmp.getString("LAST_PO_NO");
             ObjItem.setAttribute("LAST_PO_QTY",0);
             
             double LastRate=0;
             if(!PONo.trim().equals(""))
             {
               stPO=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
               rsPO=stPO.executeQuery("SELECT QTY,RATE  FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+PONo+"'");
               rsPO.first();
               if(rsPO.getRow()>0)
               {
                 ObjItem.setAttribute("LAST_PO_QTY",rsPO.getDouble("QTY"));  
                 
               }
             }

             LastRate=rsTmp.getDouble("LAST_LANDED_RATE");
             
             ObjItem.setAttribute("CURRENT_QTY",rsTmp.getDouble("QTY"));
             ObjItem.setAttribute("CURRENT_LAND_RATE",rsTmp.getDouble("LAND_COST"));
             ObjItem.setAttribute("CURRENT_RATE",rsTmp.getDouble("RATE"));
             
             RateDiff=rsTmp.getDouble("LAND_COST")-LastRate;
             if(LastRate>0)
             {
                RateDiffPer=EITLERPGLOBAL.round((RateDiff*100)/LastRate,2);
             }
             else
             {
                RateDiffPer=0;    
             }
             
             ObjItem.setAttribute("RATE_DIFFERENCE",EITLERPGLOBAL.round(RateDiff,2));
             ObjItem.setAttribute("RATE_DIFFERENCE_PER",EITLERPGLOBAL.round(RateDiffPer,2));

             RateDiff=rsTmp.getDouble("RATE")-rsTmp.getDouble("LAST_PO_RATE");
             
             if(rsTmp.getDouble("LAST_PO_RATE")>0)
             {
                RateDiffPer=EITLERPGLOBAL.round((RateDiff*100)/rsTmp.getDouble("LAST_PO_RATE"),2);
             }
             else
             {
                RateDiffPer=0;    
             }
             
             ObjItem.setAttribute("RATE_DIFFERENCE_RATE",EITLERPGLOBAL.round(RateDiff,2));
             ObjItem.setAttribute("RATE_DIFFERENCE_PER_RATE",EITLERPGLOBAL.round(RateDiffPer,2));
             ObjItem.setAttribute("QUOT_ID",rsTmp.getString("QUOT_ID"));
             ObjItem.setAttribute("QUOT_SR_NO",rsTmp.getInt("QUOT_SR_NO"));
             
             if(rsTmp.getDouble("LAST_PO_RATE")==0)
             {
                ObjItem.setAttribute("REMARKS","New Item");
                ObjItem.setAttribute("RATE_DIFFERENCE",0);
                ObjItem.setAttribute("RATE_DIFFERENCE_PER",0);
                ObjItem.setAttribute("RATE_DIFFERENCE_RATE",0);
                ObjItem.setAttribute("RATE_DIFFERENCE_PER_RATE",0);
             }
             else
             {
                ObjItem.setAttribute("REMARKS","");    
             }
             
             List.put(Integer.toString(Counter),ObjItem);             
             rsTmp.next();
         }
     }
     
     
   //tmpConn.close();
   stTmp.close();
   stPO.close();
   rsTmp.close();
   rsPO.close();
     
   }
   catch(Exception e) 
   {
       //JOptionPane.showMessageDialog(null,e.getMessage());
   }
   
   return List;
}
public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom)    
{
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
      
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate)
            {
              strSQL="SELECT D_PUR_FREIGHT_COMPARISON.DOC_NO,D_PUR_FREIGHT_COMPARISON.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_FREIGHT_COMPARISON,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_COMPARISON.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=42 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";  
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate)
            {
              strSQL="SELECT D_PUR_FREIGHT_COMPARISON.DOC_NO,D_PUR_FREIGHT_COMPARISON.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_FREIGHT_COMPARISON,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_COMPARISON.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=42 ORDER BY D_PUR_FREIGHT_COMPARISON.DOC_DATE";  
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo)
            {
              strSQL="SELECT D_PUR_FREIGHT_COMPARISON.DOC_NO,D_PUR_FREIGHT_COMPARISON.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_FREIGHT_COMPARISON,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_COMPARISON.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=42 ORDER BY D_PUR_FREIGHT_COMPARISON.DOC_NO";  
            }
            
            //strSQL="SELECT D_PUR_FREIGHT_COMPARISON.DOC_NO,D_PUR_FREIGHT_COMPARISON.DOC_DATE FROM D_PUR_FREIGHT_COMPARISON,D_COM_DOC_DATA WHERE D_PUR_FREIGHT_COMPARISON.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_FREIGHT_COMPARISON.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=42";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
            if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("DOC_DATE"),FinYearFrom))    
            {
            Counter=Counter+1;
            clsFreightComparison ObjDoc=new clsFreightComparison();
                
            //------------- Header Fields --------------------//
            ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
            ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
            ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
            ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
            // ----------------- End of Header Fields ------------------------------------//
            
            //Put the prepared user object into list    
            List.put(Long.toString(Counter),ObjDoc);
            }
            
            if(!rsTmp.isAfterLast())
            {
            rsTmp.next();
            }
           }//Out While
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
            
        }
        catch(Exception e)
        {
        }
        
      return List; 
}


    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_FREIGHT_COMPARISON_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_FREIGHT_COMPARISON_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsFreightComparison ObjApproval=new clsFreightComparison();
                
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED FROM D_PUR_FREIGHT_COMPARISON WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
