/*
 * clsFASDeprCalculation.java
 *
 * Created on August 18, 2008, 3:37 PM
 */

package EITLERP.Finance;

import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Finance.*;
/**
 *
 * @author  MRUGESH THAKER
 * @version
 */
public class clsFASDeprCalculation {
    
    public String LastError = "";
    private ResultSet  rsDepreciation;
    private Connection Conn;
    private Statement Stmt;
    public boolean Ready = false;
    
    public HashMap colLineItems;
    private HashMap props;
    
    //History Related properties
    private boolean HistoryView=false;
    public static int ModuleID=199;
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
    public clsFASDeprCalculation() {
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
            String strSql = "SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DOC_NO ";
            rsDepreciation=Stmt.executeQuery(strSql);
            
            if(rsDepreciation.getRow()<=0)
            {
                return false;
            }
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
            rsDepreciation.close();
        } catch(Exception e) {
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsDepreciation.first();
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
            if(rsDepreciation.isAfterLast()||rsDepreciation.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsDepreciation.last();
            }
            else {
                rsDepreciation.next();
                if(rsDepreciation.isAfterLast()) {
                    rsDepreciation.last();
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
            if(rsDepreciation.isFirst()||rsDepreciation.isBeforeFirst()) {
                rsDepreciation.first();
            }
            else {
                rsDepreciation.previous();
                if(rsDepreciation.isBeforeFirst()) {
                    rsDepreciation.first();
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
            rsDepreciation.last();
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
            //if(!Validate()) {
            //   return false;
            // }
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            String AStatus = getAttribute("APPROVAL_STATUS").getString();
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_DEPRN_CALCULATION_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASDeprCalculation.ModuleID,getAttribute("FFNO").getInt(),true));
            
            //========= Inserting record into Header =========//
            rsDepreciation.moveToInsertRow();
            rsDepreciation.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsDepreciation.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            String theDocNo = getAttribute("DOC_NO").getString();
            rsDepreciation.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsDepreciation.updateString("EFFECTIVE_DATE", getAttribute("EFFECTIVE_DATE").getString());
            rsDepreciation.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsDepreciation.updateBoolean("APPROVED",false);
            rsDepreciation.updateString("APPROVED_DATE","0000-00-00");
            rsDepreciation.updateBoolean("REJECTED",false);
            rsDepreciation.updateString("REJECTED_DATE","0000-00-00");
            rsDepreciation.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsDepreciation.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsDepreciation.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsDepreciation.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepreciation.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsDepreciation.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepreciation.updateBoolean("CHANGED",true);
            rsDepreciation.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepreciation.updateBoolean("CANCELLED",false);
            rsDepreciation.insertRow();
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
            rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsHistory.updateString("EFFECTIVE_DATE", getAttribute("EFFECTIVE_DATE").getString());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
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
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_DEPRN_CALCULATION_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND DOC_NO='1'");
            DocNo=getAttribute("DOC_NO").getString();
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsFASCardwithoutGRNDetailEx ObjItem=(clsFASCardwithoutGRNDetailEx) colLineItems.get(Integer.toString(i));
                
                
                //                ObjCardDetailEx.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                //                ObjCardDetailEx.setAttribute("ASSET_NO",DataModelL.getValueByVariable("ASSET_NO",i));
                //                ObjCardDetailEx.setAttribute("ITEM_ID",DataModelL.getValueByVariable("ITEM_ID",i));
                //                ObjCardDetailEx.setAttribute("SR_NO",i+1);
                //                ObjCardDetailEx.setAttribute("DETAIL_SR_NO",DataModelL.getValueByVariable("ITEM_SR_NO",i));
                //                ObjCardDetailEx.setAttribute("YEAR",DataModelL.getValueByVariable("DEPRECIATION_YEAR",i));
                //                ObjCardDetailEx.setAttribute("OPENING_BALANCE",DataModelL.getValueByVariable("OPENING_VALUE",i));
                //                ObjCardDetailEx.setAttribute("ADDITIONS","");
                //                ObjCardDetailEx.setAttribute("CLOSING_BALANCE",DataModelL.getValueByVariable("",i));
                //                ObjCardDetailEx.setAttribute("DEPRECIATION_FROM_DATE",EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DEPRECIATION_FROM_DATE",i)));
                //                ObjCardDetailEx.setAttribute("DEPRECIATION_TO_DATE",EITLERPGLOBAL.formatDateDB(DataModelL.getValueByVariable("DEPRECIATION_TO_DATE",i)));
                //                ObjCardDetailEx.setAttribute("DEPRECIATION_PERCENTAGE",DataModelL.getValueByVariable("DEPRECIATION_PERCENTAGE",i));
                //                ObjCardDetailEx.setAttribute("DEPRECIATION_METHOD",DataModelL.getValueByVariable("DEPRECIATION_METHOD",i));
                //                ObjCardDetailEx.setAttribute("DEPRECIATION_METHOD_NAME",DataModelL.getValueByVariable("DEPRECIATION_METHOD_NAME",i));
                //                ObjCardDetailEx.setAttribute("DEPRECIATION_FOR_THE_YEAR",DataModelL.getValueByVariable("DEPRECIATION_FOR_THE_YEAR",i));
                //                ObjCardDetailEx.setAttribute("SHIFT_ALLOW_FOR_THE_YEAR","");
                //                ObjCardDetailEx.setAttribute("CUMULATIVE_DEPRECIATION",DataModelL.getValueByVariable("CUMULATIVE_DEPRECIATION",i));
                //                ObjCardDetailEx.setAttribute("WRITTEN_DOWN_VALUE",DataModelL.getValueByVariable("WRITTEN_DOWN_VALUE",i));
                //                ObjCardDetailEx.setAttribute("NET_BLOCK","");
                //                ObjCardDetailEx.setAttribute("REMARKS","");
                //                ObjCardDetailEx.setAttribute("SHIFT","");
                //                ObjCardDetailEx.setAttribute("TYPE","1");
                
                
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",DocNo);
                rsTmp.updateString("SR_NO",String.valueOf(ObjItem.getAttribute("SR_NO").getInt()));
                rsTmp.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsTmp.updateString("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getString());
                rsTmp.updateString("TYPE",ObjItem.getAttribute("TYPE").getString());
                rsTmp.updateString("ASSET_TYPE",ObjItem.getAttribute("ASSET_TYPE").getString());
                rsTmp.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateDouble("OPENING_BALANCE",Double.parseDouble(ObjItem.getAttribute("OPENING_BALANCE").getString()));
                rsTmp.updateString("ADDITIONS",ObjItem.getAttribute("ADDITIONS").getString());
                rsTmp.updateDouble("CLOSING_BALANCE",Double.parseDouble(ObjItem.getAttribute("CLOSING_BALANCE").getString()));
                rsTmp.updateString("DEPRECIATION_FROM_DATE",ObjItem.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsTmp.updateString("DEPRECIATION_TO_DATE",ObjItem.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsTmp.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(ObjItem.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsTmp.updateString("DEPRECIATION_METHOD",ObjItem.getAttribute("DEPRECIATION_METHOD").getString());
                rsTmp.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(ObjItem.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsTmp.updateString("SHIFT_ALLOW_FOR_THE_YEAR",ObjItem.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsTmp.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(ObjItem.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsTmp.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(ObjItem.getAttribute("WRITTEN_DOWN_VALUE").getString()));
               // rsTmp.updateString("NET_BLOCK",ObjItem.getAttribute("NET_BLOCK").getString());
                rsTmp.updateString("SHIFT",ObjItem.getAttribute("SHIFT").getString());
                rsTmp.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsTmp.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                
                rsHDetail.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("UPDATED_BY",getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                //rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("DOC_NO",DocNo);
                rsHDetail.updateString("SR_NO",String.valueOf(ObjItem.getAttribute("SR_NO").getInt()));
                rsHDetail.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsHDetail.updateString("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getString());
                rsHDetail.updateString("TYPE",ObjItem.getAttribute("TYPE").getString());
                rsHDetail.updateString("ASSET_TYPE",ObjItem.getAttribute("ASSET_TYPE").getString());
                rsHDetail.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateDouble("OPENING_BALANCE",Double.parseDouble(ObjItem.getAttribute("OPENING_BALANCE").getString()));
                rsHDetail.updateString("ADDITIONS",ObjItem.getAttribute("ADDITIONS").getString());
                rsHDetail.updateDouble("CLOSING_BALANCE",Double.parseDouble(ObjItem.getAttribute("CLOSING_BALANCE").getString()));
                rsHDetail.updateString("DEPRECIATION_FROM_DATE",ObjItem.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsHDetail.updateString("DEPRECIATION_TO_DATE",ObjItem.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsHDetail.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(ObjItem.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                rsHDetail.updateString("DEPRECIATION_METHOD",ObjItem.getAttribute("DEPRECIATION_METHOD").getString());
                rsHDetail.updateDouble("DEPRECIATION_FOR_THE_YEAR",Double.parseDouble(ObjItem.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString()));
                rsHDetail.updateString("SHIFT_ALLOW_FOR_THE_YEAR",ObjItem.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsHDetail.updateDouble("CUMULATIVE_DEPRECIATION",Double.parseDouble(ObjItem.getAttribute("CUMULATIVE_DEPRECIATION").getString()));
                rsHDetail.updateDouble("WRITTEN_DOWN_VALUE",Double.parseDouble(ObjItem.getAttribute("WRITTEN_DOWN_VALUE").getString()));
               // rsHDetail.updateString("NET_BLOCK",ObjItem.getAttribute("NET_BLOCK").getString());
                rsHDetail.updateString("SHIFT",ObjItem.getAttribute("SHIFT").getString());
                rsHDetail.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsHDetail.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
                
                
                //                rsHDetail.updateString("DOC_NO",DocNo);
                //                rsHDetail.updateLong("SR_NO",i);
                //                rsHDetail.updateString("RECEIPT_NO", ObjItem.getAttribute("RECEIPT_NO").getString());
                //                rsHDetail.updateInt("INTEREST_DAYS",(int)ObjItem.getAttribute("INTEREST_DAYS").getVal());
                //                rsHDetail.updateDouble("INTEREST_RATE",ObjItem.getAttribute("INTEREST_RATE").getDouble());
                //                rsHDetail.updateString("INT_MAIN_ACCOUNT_CODE",ObjItem.getAttribute("INT_MAIN_ACCOUNT_CODE").getString());
                //                rsHDetail.updateString("PARTY_CODE",ObjItem.getAttribute("PARTY_CODE").getString());
                //                rsHDetail.updateString("MAIN_ACCOUNT_CODE",ObjItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                //                rsHDetail.updateDouble("INTEREST_AMOUNT",ObjItem.getAttribute("INTEREST_AMOUNT").getDouble());
                //                rsHDetail.updateDouble("TDS_AMOUNT",ObjItem.getAttribute("TDS_AMOUNT").getDouble());
                //                rsHDetail.updateDouble("NET_INTEREST",ObjItem.getAttribute("NET_INTEREST").getDouble());
                //                rsHDetail.updateInt("LEGACY_WARRANT_NO",ObjItem.getAttribute("LEGACY_WARRANT_NO").getInt());
                //                rsHDetail.updateString("WARRANT_NO",ObjItem.getAttribute("WARRANT_NO").getString());
                //                rsHDetail.updateString("WARRANT_DATE",EITLERPGLOBAL.formatDateDB(ObjItem.getAttribute("WARRANT_DATE").getString()));
                //                rsHDetail.updateInt("MICR_NO",ObjItem.getAttribute("MICR_NO").getInt());
                //                rsHDetail.updateString("WARRANT_CLEAR",ObjItem.getAttribute("WARRANT_CLEAR").getString());
                //                rsHDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                //                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //                rsHDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                //                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //                rsHDetail.updateBoolean("CHANGED",true);
                //                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //                rsHDetail.updateBoolean("CANCELLED",false);
                //                rsHDetail.insertRow();
            }
            //========== Inserting records Detail =========//
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFASDeprCalculation.ModuleID;
            ObjFlow.DocNo=getAttribute("DOC_NO").getString();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FAS_DEPRN_CALCULATION_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
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
            //--------- Approval Flow Update complete -----------//
            
            MoveLast();
            //boolean DonePosting = false;
            if(ObjFlow.Status.equals("F")) {
                
                Statement stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
                rsDetailEx.first();
                String Type ="";
                for(int i=1;i<=colLineItems.size();i++) {
                    clsFASCardwithoutGRNDetailEx objDetailEx=(clsFASCardwithoutGRNDetailEx) colLineItems.get(Integer.toString(i));
                    
                    if(objDetailEx.getAttribute("TYPE").getString().equals("BOOK")) {
                        Type = "1";
                    }else {
                        Type = "2";
                    }
                    
                    String SQL="SELECT MAX(SR_NO) FROM D_FAS_MASTER_DETAIL_EX "+
                    "WHERE ASSET_NO = '" + objDetailEx.getAttribute("ASSET_NO").getString() + "' AND DETAIL_SR_NO = '" + objDetailEx.getAttribute("DETAIL_SR_NO").getString() + "' "+
                    "AND TYPE= '" + Type + "' ";
                    
                    System.out.println(SQL);
                    
                    int MaxSrNo= data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                    MaxSrNo++;
                    
                    rsDetailEx.moveToInsertRow();
                    rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                    rsDetailEx.updateInt("SR_NO",MaxSrNo);
                    rsDetailEx.updateString("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getString());
                    rsDetailEx.updateString("TYPE",Type);//  1 - Book , 2 - IT
                    rsDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                    rsDetailEx.updateDouble("OPENING_BALANCE",objDetailEx.getAttribute("OPENING_BALANCE").getVal());
                    rsDetailEx.updateString("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getString());
                    rsDetailEx.updateDouble("CLOSING_BALANCE",objDetailEx.getAttribute("CLOSING_BALANCE").getVal());
                    rsDetailEx.updateString("DEPRECIATION_FROM_DATE",EITLERPGLOBAL.formatDateDB(objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString()));
                    rsDetailEx.updateString("DEPRECIATION_TO_DATE",EITLERPGLOBAL.formatDateDB(objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString()));
                    rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                    rsDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                    rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getVal());
                    rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                    rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getVal());
                    rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getVal());
                    //rsDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                    rsDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                    rsDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                    rsDetailEx.updateBoolean("CHANGED",true);
                    rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailEx.updateBoolean("CANCELLED",false);
                    rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                    rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                    rsDetailEx.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                    rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                    rsDetailEx.insertRow();
                }
                
            }
            
            //            while(DonePosting) {
            //                String SQL = "SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE DOC_NO='"+theDocNo+"' AND WARRANT_NO='0000000' ORDER BY RECEIPT_NO";
            //                ResultSet rsResult = data.getResult(SQL,FinanceGlobal.FinURL);
            //                int Counter = 0;
            //                rsResult.first();
            //                if(rsResult.getRow() > 0) {
            //                    while(!rsResult.isAfterLast()) {
            //                        Counter++;
            //                        String ReceiptNo = rsResult.getString("RECEIPT_NO");
            //                        String PartyCode = rsResult.getString("PARTY_CODE");
            //                        String MaturityDate = data.getStringValueFromDB("SELECT MATURITY_DATE FROM D_FD_DEPOSIT_MASTER WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
            //                        String theDate = getNextInterestDate(EITLERPGLOBAL.gCompanyID, ReceiptNo);
            //                        System.out.println("Counter : "+ Counter +" Receipt No : "+ReceiptNo+ " PartyCode : " + PartyCode+" Int. Calc. Date : "+theDate + " Maturity Date : " + MaturityDate);
            //                        data.Execute("UPDATE D_FD_DEPOSIT_MASTER SET INT_CALC_DATE='"+theDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
            //                        data.Execute("UPDATE D_FD_DEPOSIT_MASTER_H SET INT_CALC_DATE='"+theDate+"' WHERE RECEIPT_NO='"+ReceiptNo+"' AND PARTY_CODE='"+PartyCode+"' ",FinanceGlobal.FinURL);
            //                        rsResult.next();
            //                    }
            //                }
            //                DonePosting=false;
            //            }
            
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
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_DEPRN_CALCULATION_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            theDocNo=(String)getAttribute("DOC_NO").getObj();
            
            
            rsDepreciation.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsDepreciation.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            //String theDocNo = getAttribute("DOC_NO").getString();
            rsDepreciation.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsDepreciation.updateString("EFFECTIVE_DATE", getAttribute("EFFECTIVE_DATE").getString());
            rsDepreciation.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsDepreciation.updateBoolean("APPROVED",false);
            rsDepreciation.updateString("APPROVED_DATE","0000-00-00");
            rsDepreciation.updateBoolean("REJECTED",false);
            rsDepreciation.updateString("REJECTED_DATE","0000-00-00");
            rsDepreciation.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsDepreciation.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsDepreciation.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsDepreciation.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepreciation.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsDepreciation.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepreciation.updateBoolean("CHANGED",true);
            rsDepreciation.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDepreciation.updateBoolean("CANCELLED",false);
            rsDepreciation.updateRow();
            rsDepreciation.refreshRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FINANCE.D_FAS_DEPRN_CALCULATION_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateInt("REVISION_NO",RevNo);
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("UPDATED_BY",getAttribute("CREATED_BY").getString());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateString("DOC_NO", getAttribute("DOC_NO").getString());
            rsHistory.updateString("DOC_DATE", getAttribute("DOC_DATE").getString());
            rsHistory.updateString("EFFECTIVE_DATE", getAttribute("EFFECTIVE_DATE").getString());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
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
            
            
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_DEPRN_CALCULATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='1'");
            String nDocno=(String) getAttribute("DOC_NO").getObj();
            
            String Del_Query = "DELETE FROM FINANCE.D_FAS_DEPRN_CALCULATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='"+nDocno.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsFASCardwithoutGRNDetailEx ObjItem=(clsFASCardwithoutGRNDetailEx) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("DOC_NO",nDocno);
                rsTmp.updateString("SR_NO",String.valueOf(ObjItem.getAttribute("SR_NO").getInt()));
                rsTmp.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsTmp.updateString("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getString());
                rsTmp.updateString("TYPE",ObjItem.getAttribute("TYPE").getString());
                rsTmp.updateString("ASSET_TYPE",ObjItem.getAttribute("ASSET_TYPE").getString());
                rsTmp.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateString("OPENING_BALANCE",ObjItem.getAttribute("OPENING_BALANCE").getString());
                rsTmp.updateString("ADDITIONS",ObjItem.getAttribute("ADDITIONS").getString());
                rsTmp.updateString("CLOSING_BALANCE",ObjItem.getAttribute("CLOSING_BALANCE").getString());
                rsTmp.updateString("DEPRECIATION_FROM_DATE",ObjItem.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsTmp.updateString("DEPRECIATION_TO_DATE",ObjItem.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsTmp.updateString("DEPRECIATION_PERCENTAGE",ObjItem.getAttribute("DEPRECIATION_PERCENTAGE").getString());
                rsTmp.updateString("DEPRECIATION_METHOD",ObjItem.getAttribute("DEPRECIATION_METHOD").getString());
                rsTmp.updateString("DEPRECIATION_FOR_THE_YEAR",ObjItem.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString());
                rsTmp.updateString("SHIFT_ALLOW_FOR_THE_YEAR",ObjItem.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsTmp.updateString("CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("CUMULATIVE_DEPRECIATION").getString());
                rsTmp.updateString("WRITTEN_DOWN_VALUE",ObjItem.getAttribute("WRITTEN_DOWN_VALUE").getString());
                //rsTmp.updateString("NET_BLOCK",ObjItem.getAttribute("NET_BLOCK").getString());
                rsTmp.updateString("SHIFT",ObjItem.getAttribute("SHIFT").getString());
                rsTmp.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsTmp.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                
                rsHDetail.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("UPDATED_BY",getAttribute("CREATED_BY").getString());
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                //rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("DOC_NO",nDocno);
                rsHDetail.updateString("SR_NO",String.valueOf(ObjItem.getAttribute("SR_NO").getInt()));
                rsHDetail.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsHDetail.updateString("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getString());
                rsHDetail.updateString("TYPE",ObjItem.getAttribute("TYPE").getString());
                rsHDetail.updateString("ASSET_TYPE",ObjItem.getAttribute("ASSET_TYPE").getString());
                rsHDetail.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateString("OPENING_BALANCE",ObjItem.getAttribute("OPENING_BALANCE").getString());
                rsHDetail.updateString("ADDITIONS",ObjItem.getAttribute("ADDITIONS").getString());
                rsHDetail.updateString("CLOSING_BALANCE",ObjItem.getAttribute("CLOSING_BALANCE").getString());
                rsHDetail.updateString("DEPRECIATION_FROM_DATE",ObjItem.getAttribute("DEPRECIATION_FROM_DATE").getString());
                rsHDetail.updateString("DEPRECIATION_TO_DATE",ObjItem.getAttribute("DEPRECIATION_TO_DATE").getString());
                rsHDetail.updateString("DEPRECIATION_PERCENTAGE",ObjItem.getAttribute("DEPRECIATION_PERCENTAGE").getString());
                rsHDetail.updateString("DEPRECIATION_METHOD",ObjItem.getAttribute("DEPRECIATION_METHOD").getString());
                rsHDetail.updateString("DEPRECIATION_FOR_THE_YEAR",ObjItem.getAttribute("DEPRECIATION_FOR_THE_YEAR").getString());
                rsHDetail.updateString("SHIFT_ALLOW_FOR_THE_YEAR",ObjItem.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                rsHDetail.updateString("CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("CUMULATIVE_DEPRECIATION").getString());
                rsHDetail.updateString("WRITTEN_DOWN_VALUE",ObjItem.getAttribute("WRITTEN_DOWN_VALUE").getString());
                //rsHDetail.updateString("NET_BLOCK",ObjItem.getAttribute("NET_BLOCK").getString());
                rsHDetail.updateString("SHIFT",ObjItem.getAttribute("SHIFT").getString());
                rsHDetail.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsHDetail.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                rsHDetail.updateString("CREATED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("MODIFIED_BY", EITLERPGLOBAL.gLoginID);
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFASDeprCalculation.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_FAS_DEPRN_CALCULATION_HEADER";
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
                data.Execute("UPDATE FINANCE.D_FAS_DEPRN_CALCULATION_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM FINANCE.D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID='" + clsFASDeprCalculation.ModuleID + "' AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
                
                Statement stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
                rsDetailEx.first();
                String Type ="";
                for(int i=1;i<=colLineItems.size();i++) {
                    clsFASCardwithoutGRNDetailEx objDetailEx=(clsFASCardwithoutGRNDetailEx) colLineItems.get(Integer.toString(i));
                    
                    if(objDetailEx.getAttribute("TYPE").getString().equals("BOOK")) {
                        Type = "1";
                    }else {
                        Type = "2";
                    }
                    
                    String SQL="SELECT MAX(SR_NO) FROM D_FAS_MASTER_DETAIL_EX "+
                    "WHERE ASSET_NO = '" + objDetailEx.getAttribute("ASSET_NO").getString() + "' AND DETAIL_SR_NO = '" + objDetailEx.getAttribute("DETAIL_SR_NO").getString() + "' "+
                    "AND TYPE= '" + Type + "' ";
                    
                    
                    int MaxSrNo= data.getIntValueFromDB(SQL,FinanceGlobal.FinURL);
                    MaxSrNo++;
                    
                    rsDetailEx.moveToInsertRow();
                    rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsDetailEx.updateString("ASSET_NO",objDetailEx.getAttribute("ASSET_NO").getString());
                    rsDetailEx.updateInt("SR_NO",MaxSrNo);
                    rsDetailEx.updateString("DETAIL_SR_NO",objDetailEx.getAttribute("DETAIL_SR_NO").getString());
                    rsDetailEx.updateString("TYPE",Type);//  1 - Book , 2 - IT
                    rsDetailEx.updateString("YEAR",objDetailEx.getAttribute("YEAR").getString());
                    rsDetailEx.updateDouble("OPENING_BALANCE",objDetailEx.getAttribute("OPENING_BALANCE").getVal());
                    rsDetailEx.updateString("ADDITIONS",objDetailEx.getAttribute("ADDITIONS").getString());
                    rsDetailEx.updateDouble("CLOSING_BALANCE",objDetailEx.getAttribute("CLOSING_BALANCE").getVal());
                    rsDetailEx.updateString("DEPRECIATION_FROM_DATE",EITLERPGLOBAL.formatDateDB(objDetailEx.getAttribute("DEPRECIATION_FROM_DATE").getString()));
                    rsDetailEx.updateString("DEPRECIATION_TO_DATE",EITLERPGLOBAL.formatDateDB(objDetailEx.getAttribute("DEPRECIATION_TO_DATE").getString()));
                    rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",Double.parseDouble(objDetailEx.getAttribute("DEPRECIATION_PERCENTAGE").getString()));
                    rsDetailEx.updateInt("DEPRECIATION_METHOD",Integer.parseInt(objDetailEx.getAttribute("DEPRECIATION_METHOD").getString()));
                    rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",objDetailEx.getAttribute("DEPRECIATION_FOR_THE_YEAR").getVal());
                    rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR",objDetailEx.getAttribute("SHIFT_ALLOW_FOR_THE_YEAR").getString());
                    rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",objDetailEx.getAttribute("CUMULATIVE_DEPRECIATION").getVal());
                    rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",objDetailEx.getAttribute("WRITTEN_DOWN_VALUE").getVal());
                    //rsDetailEx.updateString("NET_BLOCK",objDetailEx.getAttribute("NET_BLOCK").getString());
                    rsDetailEx.updateString("REMARKS",objDetailEx.getAttribute("REMARKS").getString());
                    rsDetailEx.updateString("SHIFT",objDetailEx.getAttribute("SHIFT").getString());
                    rsDetailEx.updateBoolean("CHANGED",true);
                    rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDetailEx.updateBoolean("CANCELLED",false);
                    rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                    rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                    rsDetailEx.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
                    rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                    rsDetailEx.insertRow();
                }
            }
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    
    
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM DINESHMILLS.D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID= '"+ clsFASDeprCalculation.ModuleID  +"' AND DOC_NO='"+pDocno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    
    
    public Object getObject(long pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DOC_NO='"+pDocNo.trim()+"' ";
        clsFASDeprCalculation ObjItem=new clsFASDeprCalculation();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER " + pCondition ;
            
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDepreciation=Stmt.executeQuery(strSql);
            
            if(!rsDepreciation.first()) {
                strSql = "SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER ORDER BY DOC_NO" ;
                rsDepreciation=Stmt.executeQuery(strSql);
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
            
            clsFASDeprCalculation objDeprnCalc=new clsFASDeprCalculation();
            
            if(HistoryView) {
                RevNo=rsDepreciation.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsDepreciation.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Populate the user
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsDepreciation,"COMPANY_ID",0));
            setAttribute("DOC_NO",UtilFunctions.getString(rsDepreciation,"DOC_NO",""));
            setAttribute("DOC_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDepreciation,"DOC_DATE","0000-00-00")));
            setAttribute("EFFECTIVE_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsDepreciation,"EFFECTIVE_DATE","0000-00-00")));
            setAttribute("REMARKS",UtilFunctions.getString(rsDepreciation,"REMARKS",""));
            setAttribute("APPROVED",UtilFunctions.getInt(rsDepreciation,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsDepreciation,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsDepreciation,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsDepreciation,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsDepreciation,"REJECTED_REMARKS",""));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsDepreciation,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsDepreciation,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsDepreciation,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsDepreciation,"MODIFIED_DATE","0000-00-00"));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsDepreciation,"HIERARCHY_ID",0));
            setAttribute("CHANGED",UtilFunctions.getInt(rsDepreciation,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsDepreciation,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsDepreciation,"CANCELLED",0));
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            long mCompanyID= getAttribute("COMPANY_ID").getLong();
            String mDocno= getAttribute("DOC_NO").getString();
            tmpStmt=tmpConn.createStatement();
            String SQL= "";
            if(HistoryView) {
                SQL = "SELECT * FROM D_FAS_DEPRN_CALCULATION_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO ";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            else {
                SQL = "SELECT * FROM D_FAS_DEPRN_CALCULATION_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DOC_NO='"+mDocno.trim()+"' ORDER BY SR_NO";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsFASCardwithoutGRNDetailEx ObjItem=new clsFASCardwithoutGRNDetailEx();
                
                
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getString("COMPANY_ID"));
                ObjItem.setAttribute("ASSET_NO",rsTmp.getString("ASSET_NO"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("SR_NO",rsTmp.getString("SR_NO"));
                ObjItem.setAttribute("DETAIL_SR_NO",rsTmp.getString("DETAIL_SR_NO"));
                ObjItem.setAttribute("YEAR",rsTmp.getString("YEAR"));
                ObjItem.setAttribute("OPENING_BALANCE",rsTmp.getDouble("OPENING_BALANCE"));
                ObjItem.setAttribute("ADDITIONS",rsTmp.getString("ADDITIONS"));
                ObjItem.setAttribute("CLOSING_BALANCE",rsTmp.getDouble("CLOSING_BALANCE"));
                ObjItem.setAttribute("DEPRECIATION_FROM_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("DEPRECIATION_FROM_DATE")));
                ObjItem.setAttribute("DEPRECIATION_TO_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("DEPRECIATION_TO_DATE")));
                ObjItem.setAttribute("DEPRECIATION_PERCENTAGE",rsTmp.getString("DEPRECIATION_PERCENTAGE"));
                ObjItem.setAttribute("DEPRECIATION_METHOD",rsTmp.getString("DEPRECIATION_METHOD"));
                ObjItem.setAttribute("DEPRECIATION_METHOD_NAME","");
                ObjItem.setAttribute("DEPRECIATION_FOR_THE_YEAR",rsTmp.getDouble("DEPRECIATION_FOR_THE_YEAR"));
                ObjItem.setAttribute("SHIFT_ALLOW_FOR_THE_YEAR",rsTmp.getString("SHIFT_ALLOW_FOR_THE_YEAR"));
                ObjItem.setAttribute("CUMULATIVE_DEPRECIATION",rsTmp.getDouble("CUMULATIVE_DEPRECIATION"));
                ObjItem.setAttribute("WRITTEN_DOWN_VALUE",rsTmp.getDouble("WRITTEN_DOWN_VALUE"));
                ObjItem.setAttribute("ASSET_TYPE",rsTmp.getString("ASSET_TYPE"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("SHIFT",rsTmp.getString("SHIFT"));
                
                if(rsTmp.getString("TYPE").equals("1")) {
                    ObjItem.setAttribute("TYPE","BOOK");
                }
                else {
                    ObjItem.setAttribute("TYPE","IT");
                }
                
                
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
            rsTmp=Stmt.executeQuery("DELETE FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DOC_NO='" + nDocno.trim()+"' ");
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
            //"ORDER BY SCHEME_ORDER,DAY(A.MATURITY_DATE),MONTH(A.MATURITY_DATE) ";
            "ORDER BY SCHEME_ORDER,A.MATURITY_DATE ";
            
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            int SrNo=0;
            if(rsTmp.getRow()>0){
                while(!rsTmp.isAfterLast()) {
                    clsFASCardwithoutGRNDetailEx ObjItem=new clsFASCardwithoutGRNDetailEx();
                    String ReceiptNo = rsTmp.getString("RECEIPT_NO");
                    String eDate = rsTmp.getString("EFFECTIVE_DATE");
                    String MaturityDate = UtilFunctions.getString(rsTmp,"MATURITY_DATE","0000-00-00");
                    String interestCalcDate = UtilFunctions.getString(rsTmp,"INT_CALC_DATE","0000-00-00");
                    String Partycode = UtilFunctions.getString(rsTmp,"PARTY_CODE","");
                    if(ReceiptNo.equals("M003017")) {
                        System.out.println();
                    }
                    String Check = "SELECT A.* FROM D_FAS_DEPRN_CALCULATION_HEADER A,D_FAS_DEPRN_CALCULATION_HEADER B " +
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
                        if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FD_TAX_FORM_RECEIVED WHERE PARTY_CODE='"+Partycode+"' AND RECEIVED_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND RECEIVED_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ",FinanceGlobal.FinURL)) {
                            partyInterest = clsDepositMaster.checkTDSAmount(rsTmp.getString("PARTY_CODE"));
                        }
                        String SQLQuery = "SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE RECEIPT_NO='"+ ReceiptNo +"' ";
                        if(data.IsRecordExist(SQLQuery,FinanceGlobal.FinURL)) {
                            /** Add previous amounts to calculate next interest amount for cumulative interest And
                             *  change Receipt Date for date difference if record already exits
                             */
                            SQLQuery = "SELECT A.INTEREST_AMOUNT FROM D_FAS_DEPRN_CALCULATION_HEADER A,D_FAS_DEPRN_CALCULATION_HEADER B WHERE A.RECEIPT_NO='" + ReceiptNo +"' AND A.PARTY_CODE='"+Partycode+"' AND A.DOC_NO=B.DOC_NO AND B.APPROVED=1 AND B.TDS_ONLY=0 ORDER BY WARRANT_DATE";
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
                        
                        if(!data.IsRecordExist("SELECT PARTY_CODE FROM D_FD_TAX_FORM_RECEIVED WHERE PARTY_CODE='"+Partycode+"' AND RECEIVED_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND RECEIVED_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ",FinanceGlobal.FinURL)) {
                            partyInterest = clsDepositMaster.checkTDSAmount(rsTmp.getString("PARTY_CODE"));
                        }
                        long totalDays=1;
                        String SQLQuery = "SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE RECEIPT_NO='"+ ReceiptNo +"' ";
                        if(data.IsRecordExist(SQLQuery,FinanceGlobal.FinURL)){
                            //change Receipt Date for Date Difference if record already exits
                            SQLQuery = "SELECT INTEREST_DAYS FROM D_FAS_DEPRN_CALCULATION_HEADER A, D_FAS_DEPRN_CALCULATION_HEADER B WHERE A.RECEIPT_NO='" + ReceiptNo +"' AND A.PARTY_CODE='"+Partycode+"' AND A.DOC_NO=B.DOC_NO AND B.APPROVED=1 ORDER BY WARRANT_DATE";
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
                        if(cal.isLeapYear(EITLERPGLOBAL.getCurrentFinYear()+1)) {
                            interestAmount = EITLERPGLOBAL.round(((Amount * Rate * totalDays)/(366* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
                        } else {
                            interestAmount = EITLERPGLOBAL.round(((Amount * Rate * totalDays)/(365* 100)),0); //INTEREST AMOUNT = (AMOUNT * RATE)/100
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
                            currentInterest = clsFASDeprCalculation.getCurrentInterest(ReceiptNo,interestCalcDate);
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
                        netInterest = EITLERPGLOBAL.round(interestAmount - TDSAmount,0);
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
                strSQL="SELECT FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_NO,FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_DEPRN_CALCULATION_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID='" + clsFASDeprCalculation.ModuleID + "' ORDER BY "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_NO,FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_DEPRN_CALCULATION_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID='" + clsFASDeprCalculation.ModuleID + "' ORDER BY FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_NO,FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_DATE, "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_DEPRN_CALCULATION_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.COMPANY_ID="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.COMPANY_ID AND FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID='" + clsFASDeprCalculation.ModuleID + "' ORDER BY FINANCE.D_FAS_DEPRN_CALCULATION_HEADER.DOC_NO";
            }
            
            //strSQL="SELECT D_PUR_INQUIRY_HEADER.INQUIRY_NO,D_PUR_INQUIRY_HEADER.INQUIRY_DATE FROM D_PUR_INQUIRY_HEADER,D_COM_DOC_DATA WHERE D_PUR_INQUIRY_HEADER.INQUIRY_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_INQUIRY_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_INQUIRY_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID='" + clsFASDeprCalculation.ModuleID + "'";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                
                Counter=Counter+1;
                clsFASDeprCalculation ObjDoc=new clsFASDeprCalculation();
                
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
            rsDepreciation=Stmt.executeQuery("SELECT * FROM FINANCE.D_FAS_DEPRN_CALCULATION_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsFASDeprCalculation objDeprnCalc=new clsFASDeprCalculation();
                
                objDeprnCalc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                objDeprnCalc.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                objDeprnCalc.setAttribute("UPDATED_BY",rsTmp.getString("UPDATED_BY"));
                objDeprnCalc.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                objDeprnCalc.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                objDeprnCalc.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),objDeprnCalc);
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=data.getResult("SELECT DOC_NO FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND CANCELLED=0");
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
        //if(!data.IsRecordExist("SELECT * FROM D_FIN_BOOK_MASTER WHERE BOOK_CODE='"+getAttribute("BOOK_CODE").getString()+"'",FinanceGlobal.FinURL)) {
        //    LastError = "Please specify Book Code.";
        //    return false;
        //}
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
            String strSQL="SELECT MAX(SUBSTRING(B.WARRANT_NO,LENGTH('"+FinYear+"')+1)) AS MAX_NO FROM D_FAS_DEPRN_CALCULATION_HEADER A,D_FAS_DEPRN_CALCULATION_HEADER B WHERE A.DOC_NO=B.DOC_NO AND B.WARRANT_NO LIKE '"+FinYear+"%' AND A.APPROVED=1";
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
                String strSQL="SELECT MAX(SUBSTRING(WARRANT_NO,LENGTH('"+FinYear+"')+1)) AS MAX_NO FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE WARRANT_NO LIKE '"+FinYear+"%'";
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
                if(data.IsRecordExist("SELECT * FROM D_FAS_DEPRN_CALCULATION_HEADER WHERE RECEIPT_NO='"+ReceiptNo+"' AND WARRANT_DATE>='"+StartFinYear+"' AND WARRANT_DATE<='"+EndFinYear+"' ",FinanceGlobal.FinURL)) {
                    SQLQuery = "SELECT A.INTEREST_AMOUNT FROM D_FAS_DEPRN_CALCULATION_HEADER A, D_FAS_DEPRN_CALCULATION_HEADER B WHERE A.RECEIPT_NO='" + ReceiptNo +"' AND A.DOC_NO=B.DOC_NO  AND  B.TDS_ONLY=0 AND A.PARTY_CODE='"+PartyCode+"'";
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
            String strSQL="SELECT MAX(LEGACY_WARRANT_NO) AS MAX_NO FROM D_FAS_DEPRN_CALCULATION_HEADER A, D_FAS_DEPRN_CALCULATION_HEADER B " +
            "WHERE A.DOC_NO=B.DOC_NO AND B.WARRANT_DATE>='"+FinStartDate+"' AND B.WARRANT_DATE<='"+FinEndDate+"' " +
            "AND A.TDS_ONLY<>1 AND A.APPROVED=1 AND A.CANCELLED=0 ";
            System.out.println(strSQL);
            legacyNo = data.getIntValueFromDB(strSQL,FinanceGlobal.FinURL)+1;
        }catch(Exception e) {
        }
        return legacyNo;
    }
}