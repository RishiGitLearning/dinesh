/*
 * clsFASSale.java
 * Created By Bhavesh Patel
 * Created on May 19, 2011, 2:35 PM
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


public class clsFASSale {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    public static int ModuleID=197;
    private HashMap props;
    public HashMap colLineItems = new HashMap();
    public boolean Ready = false;
    private boolean HistoryView=false;
    public HashMap colgFasItems=new HashMap();
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
    public clsFASSale() {
        LastError = "";
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SALE_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("SALE_DATE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
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
        
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_FAS_SALE_HEADER ORDER BY SALE_NO ");
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
        
        Statement stTmp,stHistory,stHDetail,stUpdate,stUpdateIT;
        ResultSet rsTmp,rsHistory,rsHDetail,rsUpdate,rsUpdateIT;
        Statement stHeader;
        ResultSet rsHeader;
        int CompanyID=0,AssetSrNo=0,cDept_id = 0;
        String AssetNo = "",cDept_name = "",Asset_Status = "";
        String strSQL = "";
        String DocNo="",sdate="";
        double svalue = 0.0;
        double depr_for_the_year = 0.0;
        double it_depr_for_the_year = 0.0;
        double wdv = 0.0,it_wdv = 0.0,Cumm_Dperi=0.0,it_Cumm_Dperi = 0.0;
        double net_block = 0.0;
        int DSrNo,DExSrNo;
        try {
            if(!Validate()) {
                return false;
            }
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID = (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            String AStatus = getAttribute("APPROVAL_STATUS").getString();
            // AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_SALE_HEADER_H WHERE SALE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_SALE_DETAIL_H WHERE SALE_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            
            setAttribute("SALE_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,clsFASSale.ModuleID,getAttribute("FFNO").getInt(),true));
            
            //========= Inserting record into Header =========//
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            CompanyID = getAttribute("COMPANY_ID").getInt();
            rsResultSet.updateString("SALE_NO", getAttribute("SALE_NO").getString());
            String theDocNo = getAttribute("SALE_NO").getString();
            rsResultSet.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsResultSet.updateString("INVOICE_DATE", (String)getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateString("SALE_DATE", (String)getAttribute("SALE_DATE").getObj());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            System.out.println("Created By :" +String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsResultSet.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsResultSet.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            //rsResultSet.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            //=========End of Inserting record into Header =========//
            
            //========= Inserting record into Header History=========//
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateString("SALE_NO", getAttribute("SALE_NO").getString());
            rsHistory.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsHistory.updateString("INVOICE_DATE", (String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateString("SALE_DATE", (String)getAttribute("SALE_DATE").getObj());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            System.out.println("Created By :" +String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsHistory.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
           // rsHistory.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
           // rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            //=========End of Inserting record into Header history=========//
            
            //========== Inserting records Detail =========//
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_SALE_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND SALE_NO='1'");
            DocNo=getAttribute("SALE_NO").getString();
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsFASSaleDetail ObjItem=(clsFASSaleDetail) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("SALE_NO",DocNo);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsTmp.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsTmp.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateDouble("BOOK_OPENING_VALUE",ObjItem.getAttribute("BOOK_OPENING_VALUE").getDouble());
                rsTmp.updateDouble("BOOK_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("BOOK_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsTmp.updateDouble("BOOK_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("BOOK_CUMULATIVE_DEPRECIATION").getDouble());
                rsTmp.updateDouble("BOOK_CLOSING_VALUE",ObjItem.getAttribute("BOOK_CLOSING_VALUE").getDouble());
                rsTmp.updateDouble("BOOK_PROFIT_LOSS",ObjItem.getAttribute("BOOK_PROFIT_LOSS").getDouble());
                rsTmp.updateDouble("IT_OPENING_VALUE",ObjItem.getAttribute("IT_OPENING_VALUE").getDouble());
                rsTmp.updateDouble("IT_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("IT_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsTmp.updateDouble("IT_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("IT_CUMULATIVE_DEPRECIATION").getDouble());
                rsTmp.updateDouble("IT_CLOSING_VALUE",ObjItem.getAttribute("IT_CLOSING_VALUE").getDouble());
                rsTmp.updateDouble("IT_PROFIT_LOSS",ObjItem.getAttribute("IT_PROFIT_LOSS").getDouble());
                rsTmp.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateInt("DEPT_ID",ObjItem.getAttribute("DEPT_ID").getInt());
                rsTmp.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsTmp.updateString("ASSET_STATUS",ObjItem.getAttribute("ASSET_STATUS").getString());
                rsTmp.updateDouble("SALE_VALUE",ObjItem.getAttribute("SALE_VALUE").getDouble());
                rsTmp.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                //rsTmp.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
                //rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("UPDATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("SALE_NO",DocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsHDetail.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetail.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                //rsHDetail.updateString("SALE_DATE",ObjItem.getAttribute("SALE_DATE").getString());
                rsHDetail.updateDouble("BOOK_OPENING_VALUE",ObjItem.getAttribute("BOOK_OPENING_VALUE").getDouble());
                rsHDetail.updateDouble("BOOK_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("BOOK_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("BOOK_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("BOOK_CUMULATIVE_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("BOOK_CLOSING_VALUE",ObjItem.getAttribute("BOOK_CLOSING_VALUE").getDouble());
                rsHDetail.updateDouble("BOOK_PROFIT_LOSS",ObjItem.getAttribute("BOOK_PROFIT_LOSS").getDouble());
                rsHDetail.updateDouble("IT_OPENING_VALUE",ObjItem.getAttribute("IT_OPENING_VALUE").getDouble());
                rsHDetail.updateDouble("IT_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("IT_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("IT_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("IT_CUMULATIVE_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("IT_CLOSING_VALUE",ObjItem.getAttribute("IT_CLOSING_VALUE").getDouble());
                rsHDetail.updateDouble("IT_PROFIT_LOSS",ObjItem.getAttribute("IT_PROFIT_LOSS").getDouble());
                rsHDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateInt("DEPT_ID",ObjItem.getAttribute("DEPT_ID").getInt());
                rsHDetail.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsHDetail.updateString("ASSET_STATUS",ObjItem.getAttribute("ASSET_STATUS").getString());
                rsHDetail.updateDouble("SALE_VALUE",ObjItem.getAttribute("SALE_VALUE").getDouble());
                rsHDetail.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                //rsHDetail.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
                //rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetail.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            //insert current dept id into fas master detail after final approved
            
            
            //========== Inserting records Detail =========//
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFASSale.ModuleID;
            ObjFlow.DocNo=getAttribute("SALE_NO").getString();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName=clsModules.getHeaderTableName(EITLERPGLOBAL.gCompanyID,clsFASSale.ModuleID);
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="SALE_NO";
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
            
            
            //// update
            
            
            
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsFASSaleDetail ObjItem=(clsFASSaleDetail)colLineItems.get(Integer.toString(i));
                    
                    
                    AssetSrNo=(int)ObjItem.getAttribute("DETAIL_SR_NO").getVal();
                    AssetNo=(String)ObjItem.getAttribute("ASSET_NO").getObj();
                    Asset_Status = ObjItem.getAttribute("ASSET_STATUS").getString();
                    DocNo = getAttribute("SALE_NO").getString();
                    sdate = getAttribute("SALE_DATE").getString();
                    svalue = ObjItem.getAttribute("SALE_VALUE").getDouble();
                    depr_for_the_year = ObjItem.getAttribute("BOOK_CURRENT_YEAR_DEPRECIATION").getDouble();
                    it_depr_for_the_year = ObjItem.getAttribute("IT_CURRENT_YEAR_DEPRECIATION").getDouble();
                    wdv = ObjItem.getAttribute("BOOK_CLOSING_VALUE").getDouble();
                    it_wdv = ObjItem.getAttribute("IT_CLOSING_VALUE").getDouble();
                    //net_block = ObjItem.getAttribute("NET_BLOCK").getDouble();
                    Cumm_Dperi = ObjItem.getAttribute("BOOK_CUMULATIVE_DEPRECIATION").getDouble();
                    it_Cumm_Dperi = ObjItem.getAttribute("IT_CUMULATIVE_DEPRECIATION").getDouble();
                    // Update Dept id
                    data.Execute("UPDATE D_FAS_MASTER_DETAIL SET ASSET_STATUS='"+Asset_Status+"',SALE_VALUE='"+svalue+"',SALE_DATE='"+sdate+"',SALE_DOC_NO='"+DocNo+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND SR_NO="+AssetSrNo+" ",FinanceGlobal.FinURL);
                    //data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo.trim()+"'");
                    
                    ////////////////////////////////// ------------ UPDATE SALES DATA FOR BOOK ---------------- ///////////////////////////////////
                    
                    String SQL ="SELECT B.SR_NO D_SR_NO,A.SR_NO DEX_SR_NO FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                    "WHERE A.DEPRECIATION_FROM_DATE >= '" + EITLERPGLOBAL.getFinYearStartDate(sdate) + "'  " +
                    "AND  A.DEPRECIATION_FROM_DATE <= '" + EITLERPGLOBAL.getFinYearEndDate(sdate) + "'  " +
                    "AND A.ASSET_NO = '" + AssetNo + "' " +
                    "AND A.ASSET_NO = B.ASSET_NO " +
                    "AND A.DETAIL_SR_NO  = B.SR_NO " +
                    "AND A.DETAIL_SR_NO = '" + AssetSrNo + "' AND TYPE = 1 " ;
                    
                    rsUpdate = data.getResult(SQL,FinanceGlobal.FinURL);
                    
                    
                    //String AssetDate = data.getStringValueFromDB("SELECT ASSET_DATE FROM D_FAS_MASTER_HEADER WHERE ASSET_NO = '" + AssetNo + "'",FinanceGlobal.FinURL);
                    if(rsUpdate.getRow()>0) {
                        DSrNo = rsUpdate.getInt("D_SR_NO");
                        DExSrNo = rsUpdate.getInt("DEX_SR_NO");
                        data.Execute("UPDATE D_FAS_MASTER_DETAIL_EX SET DEPRECIATION_TO_DATE='"+sdate+"',DEPRECIATION_FOR_THE_YEAR='"+depr_for_the_year+"',CUMULATIVE_DEPRECIATION = '"+Cumm_Dperi+"',WRITTEN_DOWN_VALUE = '"+wdv+"', CLOSING_BALANCE = '"+wdv+"', CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND DETAIL_SR_NO="+AssetSrNo+" AND TYPE = 1 AND SR_NO = "+DExSrNo+"  ",FinanceGlobal.FinURL);
                    }
                    else {
                        
                        //for the new record insert in the detail_ex table :- BOOK ENTRY
                        SQL = "SELECT A.* FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                        "WHERE A.ASSET_NO = '" + AssetNo + "' AND A.ASSET_NO = B.ASSET_NO AND " +
                        "A.DETAIL_SR_NO  = B.SR_NO AND A.DETAIL_SR_NO = '" + AssetSrNo + "'  AND TYPE = 1 " +
                        "ORDER BY A.SR_NO DESC ";
                        rsTmp=data.getResult(SQL,FinanceGlobal.FinURL);
                        
                        if(rsTmp.getRow()>0) {
                            double CumulativeDeprn = rsTmp.getDouble("CUMULATIVE_DEPRECIATION");
                            double ClosingBalance = rsTmp.getDouble("CLOSING_BALANCE");
                            Statement stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
                            rsDetailEx.first();
                            
                            rsDetailEx.moveToInsertRow();
                            rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                            rsDetailEx.updateString("ASSET_NO",AssetNo);
                            rsDetailEx.updateInt("SR_NO",rsTmp.getInt("SR_NO")+1);
                            rsDetailEx.updateInt("DETAIL_SR_NO",AssetSrNo);
                            rsDetailEx.updateString("TYPE","1");//  1 - Book , 2 - IT
                            rsDetailEx.updateInt("YEAR",rsTmp.getInt("YEAR")+1);
                            rsDetailEx.updateDouble("OPENING_BALANCE",ClosingBalance );
                            rsDetailEx.updateDouble("ADDITIONS",0);
                            
                            String DeprnToDate=EITLERPGLOBAL.addDaysToDate(rsTmp.getString("DEPRECIATION_TO_DATE"),1,"yyyy-MM-dd");
                            rsDetailEx.updateString("DEPRECIATION_FROM_DATE",DeprnToDate);
                            rsDetailEx.updateString("DEPRECIATION_TO_DATE",sdate);
                            
                            
                            
                            SQL="SELECT B.* FROM D_FAS_ITEM_MASTER_HEADER A,D_FAS_ITEM_MASTER_DETAIL B "+
                            "WHERE A.DOC_NO = B.DOC_NO "+
                            "AND A.ITEM_ID = '" + ObjItem.getAttribute("ITEM_ID").getString() + "' "+
                            "AND '" + sdate + "' >= B.FROM_DATE AND '" + sdate + "' <= B.TO_DATE "+
                            "ORDER BY B.SR_NO";
                            
                            double BookRate=0;
                            int MethodId = 0;
                            rsTmp = data.getResult(SQL,FinanceGlobal.FinURL);
                            if(rsTmp.getRow()>0) {
                                BookRate=rsTmp.getDouble("BOOK_PER");
                                MethodId =rsTmp.getInt("METHOD_ID");
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",rsTmp.getDouble("BOOK_PER"));
                                //DataModelB.setValueByVariable("DEPRN_PERCENTAGE",rsTmp.getString("BOOK_PER"),NewRow);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",MethodId );
                                //DataModelB.setValueByVariable("DEPRN_METHOD",String.valueOf(MethodId) ,NewRow);
                                //String Method_name = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"METHOD",MethodId);
                                //DataModelB.setValueByVariable("DEPRN_METHOD_NAME",Method_name,NewRow);
                            }
                            else {
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",0);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",0);
                                
                            }
                            double DeprAmount=0;
                            double AssetCostOriginal= (int)ObjItem.getAttribute("AMOUNT").getVal();
                            int month = EITLERPGLOBAL.getMonthDifference(sdate,DeprnToDate)+1;
                            if(MethodId==2) {
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(AssetCostOriginal, BookRate,month),0);
                            }
                            else if(MethodId==1) {
                                
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(ClosingBalance , BookRate,month),0);
                            }
                            double cDeprn =  CumulativeDeprn + DeprAmount;
                            
                            rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",DeprAmount);
                            rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR","");
                            rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",cDeprn );
                            rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateDouble("CLOSING_BALANCE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateString("REMARKS","");
                            rsDetailEx.updateString("SHIFT","");
                            rsDetailEx.updateBoolean("CHANGED",true);
                            rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsDetailEx.updateBoolean("CANCELLED",false);
                            rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                            rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                            rsDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                            rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                            rsDetailEx.insertRow();
                        }
                        
                    }//end else
                    
                    
                    /////////////////////////////////// ------------ UPDATE SALES DATA FOR IT ---------------- ///////////////////////////////////
                    
                    String SQLIT ="SELECT B.SR_NO D_SR_NO,A.SR_NO DEX_SR_NO FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                    "WHERE A.DEPRECIATION_FROM_DATE >= '" + EITLERPGLOBAL.getFinYearStartDate(sdate) + "'  " +
                    "AND  A.DEPRECIATION_FROM_DATE <= '" + EITLERPGLOBAL.getFinYearEndDate(sdate) + "'  " +
                    "AND A.ASSET_NO = '" + AssetNo + "' " +
                    "AND A.ASSET_NO = B.ASSET_NO " +
                    "AND A.DETAIL_SR_NO  = B.SR_NO " +
                    "AND A.DETAIL_SR_NO = '" + AssetSrNo + "' AND TYPE = 2 " ;
                    
                    rsUpdateIT = data.getResult(SQLIT,FinanceGlobal.FinURL);
                    
                    
                    //String AssetDate = data.getStringValueFromDB("SELECT ASSET_DATE FROM D_FAS_MASTER_HEADER WHERE ASSET_NO = '" + AssetNo + "'",FinanceGlobal.FinURL);
                    if(rsUpdate.getRow()>0) {
                        DSrNo = rsUpdateIT.getInt("D_SR_NO");
                        DExSrNo = rsUpdateIT.getInt("DEX_SR_NO");
                        data.Execute("UPDATE D_FAS_MASTER_DETAIL_EX SET DEPRECIATION_TO_DATE='"+sdate+"',DEPRECIATION_FOR_THE_YEAR='"+it_depr_for_the_year+"',CUMULATIVE_DEPRECIATION = '"+it_Cumm_Dperi+"',WRITTEN_DOWN_VALUE = '"+it_wdv+"', CLOSING_BALANCE = '"+it_wdv+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND DETAIL_SR_NO="+AssetSrNo+" AND TYPE = 2 AND SR_NO = "+DExSrNo+"  ",FinanceGlobal.FinURL);
                    }
                    
                    else {
                        //for the new record insert in the detail_ex table :- IT ENTRY
                        SQL = "SELECT A.* FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                        "WHERE A.ASSET_NO = '" + AssetNo + "' AND A.ASSET_NO = B.ASSET_NO AND " +
                        "A.DETAIL_SR_NO  = B.SR_NO AND A.DETAIL_SR_NO = '" + AssetSrNo + "'  AND TYPE = 2 " +
                        "ORDER BY A.SR_NO DESC ";
                        rsTmp=data.getResult(SQL,FinanceGlobal.FinURL);
                        
                        if(rsTmp.getRow()>0) {
                            double CumulativeDeprn = rsTmp.getDouble("CUMULATIVE_DEPRECIATION");
                            double ClosingBalance = rsTmp.getDouble("CLOSING_BALANCE");
                            Statement stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
                            rsDetailEx.first();
                            
                            rsDetailEx.moveToInsertRow();
                            rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                            rsDetailEx.updateString("ASSET_NO",AssetNo);
                            rsDetailEx.updateInt("SR_NO",rsTmp.getInt("SR_NO")+1);
                            rsDetailEx.updateInt("DETAIL_SR_NO",AssetSrNo);
                            rsDetailEx.updateString("TYPE","2");//  1 - Book , 2 - IT
                            rsDetailEx.updateInt("YEAR",rsTmp.getInt("YEAR")+1);
                            rsDetailEx.updateDouble("OPENING_BALANCE",ClosingBalance );
                            rsDetailEx.updateDouble("ADDITIONS",0);
                            
                            String DeprnToDate=EITLERPGLOBAL.addDaysToDate(rsTmp.getString("DEPRECIATION_TO_DATE"),1,"yyyy-MM-dd");
                            rsDetailEx.updateString("DEPRECIATION_FROM_DATE",DeprnToDate);
                            rsDetailEx.updateString("DEPRECIATION_TO_DATE",sdate);
                            
                            
                            
                            SQL="SELECT B.* FROM D_FAS_ITEM_MASTER_HEADER A,D_FAS_ITEM_MASTER_DETAIL B "+
                            "WHERE A.DOC_NO = B.DOC_NO "+
                            "AND A.ITEM_ID = '" + ObjItem.getAttribute("ITEM_ID").getString() + "' "+
                            "AND '" + sdate + "' >= B.FROM_DATE AND '" + sdate + "' <= B.TO_DATE "+
                            "ORDER BY B.SR_NO";
                            
                            double BookRate=0;
                            int MethodId = 0;
                            rsTmp = data.getResult(SQL,FinanceGlobal.FinURL);
                            if(rsTmp.getRow()>0) {
                                BookRate=rsTmp.getDouble("IT_PER");
                                MethodId =rsTmp.getInt("METHOD_ID");
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",rsTmp.getDouble("IT_PER"));
                                //DataModelB.setValueByVariable("DEPRN_PERCENTAGE",rsTmp.getString("BOOK_PER"),NewRow);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",MethodId );
                                //DataModelB.setValueByVariable("DEPRN_METHOD",String.valueOf(MethodId) ,NewRow);
                                //String Method_name = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"METHOD",MethodId);
                                //DataModelB.setValueByVariable("DEPRN_METHOD_NAME",Method_name,NewRow);
                            }
                            else {
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",0);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",0);
                                
                            }
                            double DeprAmount=0;
                            double AssetCostOriginal= (int)ObjItem.getAttribute("AMOUNT").getVal();
                            int month = EITLERPGLOBAL.getMonthDifference(sdate,DeprnToDate)+1;
                            if(MethodId==2) {
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(AssetCostOriginal, BookRate,month),0);
                            }
                            else if(MethodId==1) {
                                
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(ClosingBalance , BookRate,month),0);
                            }
                            double cDeprn =  CumulativeDeprn + DeprAmount;
                            
                            rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",DeprAmount);
                            rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR","");
                            rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",cDeprn );
                            rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateDouble("CLOSING_BALANCE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateString("REMARKS","");
                            rsDetailEx.updateString("SHIFT","");
                            rsDetailEx.updateBoolean("CHANGED",true);
                            rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsDetailEx.updateBoolean("CANCELLED",false);
                            rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                            rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                            rsDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                            rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                            rsDetailEx.insertRow();
                        }
                    }//end else
                }
                
                if(!PostSJForAsset(EITLERPGLOBAL.gCompanyID,DocNo, AssetNo)) {
                    JOptionPane.showMessageDialog(null,"Sales Journal Voucher not posted for Asset No : " + AssetNo +"");
                }
            }
            
            //MoveLast();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    
    //Updates current record
    public boolean Update() {
        Statement stTmp,stHistory,stHDetail,stUpdate,stUpdateIT;
        ResultSet rsTmp,rsHistory,rsHDetail,rsUpdate,rsUpdateIT;
        Statement stHeader;
        ResultSet rsHeader;
        int CompanyID=0,AssetSrNo=0,cDept_id = 0;
        String AssetNo = "",cDept_name = "",Asset_Status = "";
        String strSQL = "";
        String DocNo="",sdate="";
        double svalue = 0.0;
        double depr_for_the_year = 0.0,Cumm_Dperi = 0.0;
        double it_depr_for_the_year = 0.0,it_Cumm_Dperi = 0.0;
        double wdv = 0.0;
        double it_wdv = 0.0;
        double net_block = 0.0;
        int DSrNo,DExSrNo;
        
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
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            //String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            //String AStatus=getAttribute("APPROVAL_STATUS").getString();
            if(AStatus.equals("R")) {
                Validate=false; //Do not validate while in rejection mode.
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_FAS_SALE_HEADER_H WHERE SALE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_FAS_SALE_DETAIL_H WHERE SALE_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("SALE_NO").getObj();
            
            rsResultSet.updateLong("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            CompanyID = getAttribute("COMPANY_ID").getInt();
            rsResultSet.updateString("SALE_NO", getAttribute("SALE_NO").getString());
            //String theDocNo = getAttribute("SALE_NO").getString();
            rsResultSet.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsResultSet.updateString("INVOICE_DATE", (String)getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateString("SALE_DATE", (String)getAttribute("SALE_DATE").getObj());
            rsResultSet.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS","");
            //rsResultSet.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
            //rsResultSet.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsResultSet.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            //rsResultSet.refreshRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM FINANCE.D_FAS_SALE_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SALE_NO='"+(String)getAttribute("SALE_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("SALE_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            //rsHDetail.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("SALE_NO", getAttribute("SALE_NO").getString());
            rsHistory.updateString("DOC_DATE", (String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("REMARKS", getAttribute("REMARKS").getString());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("INVOICE_NO", getAttribute("INVOICE_NO").getString());
            rsHistory.updateString("INVOICE_DATE", (String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateString("SALE_DATE", (String)getAttribute("SALE_DATE").getObj());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS","");
            //rsHistory.updateString("CREATED_BY", String.valueOf(getAttribute("CREATED_BY").getInt()));
            //rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
            rsHistory.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            // Inserting records in Inquiry Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_FAS_SALE_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND SALE_NO='1'");
            String nDocno=(String) getAttribute("SALE_NO").getObj();
            
            String Del_Query = "DELETE FROM FINANCE.D_FAS_SALE_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND SALE_NO='"+nDocno.trim()+"'";
            data.Execute(Del_Query,FinanceGlobal.FinURL);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsFASSaleDetail ObjItem=(clsFASSaleDetail) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("SALE_NO",nDocno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsTmp.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsTmp.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                //rsTmp.updateString("SALE_DATE",ObjItem.getAttribute("SALE_DATE").getString());
                rsTmp.updateDouble("BOOK_OPENING_VALUE",ObjItem.getAttribute("BOOK_OPENING_VALUE").getDouble());
                rsTmp.updateDouble("BOOK_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("BOOK_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsTmp.updateDouble("BOOK_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("BOOK_CUMULATIVE_DEPRECIATION").getDouble());
                rsTmp.updateDouble("BOOK_CLOSING_VALUE",ObjItem.getAttribute("BOOK_CLOSING_VALUE").getDouble());
                rsTmp.updateDouble("BOOK_PROFIT_LOSS",ObjItem.getAttribute("BOOK_PROFIT_LOSS").getDouble());
                rsTmp.updateDouble("IT_OPENING_VALUE",ObjItem.getAttribute("IT_OPENING_VALUE").getDouble());
                rsTmp.updateDouble("IT_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("IT_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsTmp.updateDouble("IT_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("IT_CUMULATIVE_DEPRECIATION").getDouble());
                rsTmp.updateDouble("IT_CLOSING_VALUE",ObjItem.getAttribute("IT_CLOSING_VALUE").getDouble());
                rsTmp.updateDouble("IT_PROFIT_LOSS",ObjItem.getAttribute("IT_PROFIT_LOSS").getDouble());
                rsTmp.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateInt("DEPT_ID",ObjItem.getAttribute("DEPT_ID").getInt());
                rsTmp.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsTmp.updateString("ASSET_STATUS",ObjItem.getAttribute("ASSET_STATUS").getString());
                rsTmp.updateDouble("SALE_VALUE",ObjItem.getAttribute("SALE_VALUE").getDouble());
                rsTmp.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                //rsTmp.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
                //rsTmp.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsTmp.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CANCELLED",false);
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("UPDATED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetail.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
                rsHDetail.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
                rsHDetail.updateString("SALE_NO",nDocno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("ASSET_NO",ObjItem.getAttribute("ASSET_NO").getString());
                rsHDetail.updateInt("DETAIL_SR_NO",ObjItem.getAttribute("DETAIL_SR_NO").getInt());
                rsHDetail.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                //rsHDetail.updateString("SALE_DATE",ObjItem.getAttribute("SALE_DATE").getString());
                rsHDetail.updateDouble("BOOK_OPENING_VALUE",ObjItem.getAttribute("BOOK_OPENING_VALUE").getDouble());
                rsHDetail.updateDouble("BOOK_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("BOOK_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("BOOK_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("BOOK_CUMULATIVE_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("BOOK_CLOSING_VALUE",ObjItem.getAttribute("BOOK_CLOSING_VALUE").getDouble());
                rsHDetail.updateDouble("BOOK_PROFIT_LOSS",ObjItem.getAttribute("BOOK_PROFIT_LOSS").getDouble());
                rsHDetail.updateDouble("IT_OPENING_VALUE",ObjItem.getAttribute("IT_OPENING_VALUE").getDouble());
                rsHDetail.updateDouble("IT_CURRENT_YEAR_DEPRECIATION",ObjItem.getAttribute("IT_CURRENT_YEAR_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("IT_CUMULATIVE_DEPRECIATION",ObjItem.getAttribute("IT_CUMULATIVE_DEPRECIATION").getDouble());
                rsHDetail.updateDouble("IT_CLOSING_VALUE",ObjItem.getAttribute("IT_CLOSING_VALUE").getDouble());
                rsHDetail.updateDouble("IT_PROFIT_LOSS",ObjItem.getAttribute("IT_PROFIT_LOSS").getDouble());
                rsHDetail.updateDouble("AMOUNT",ObjItem.getAttribute("AMOUNT").getDouble());
                rsHDetail.updateInt("DEPT_ID",ObjItem.getAttribute("DEPT_ID").getInt());
                rsHDetail.updateString("YEAR",ObjItem.getAttribute("YEAR").getString());
                rsHDetail.updateString("ASSET_STATUS",ObjItem.getAttribute("ASSET_STATUS").getString());
                rsHDetail.updateDouble("SALE_VALUE",ObjItem.getAttribute("SALE_VALUE").getDouble());
                rsHDetail.updateString("REMARKS",ObjItem.getAttribute("REMARKS").getString());
                //rsHDetail.updateString("CREATED_BY",  String.valueOf(getAttribute("CREATED_BY").getInt()));
                //rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateString("MODIFIED_BY", String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CANCELLED",false);
                rsHDetail.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsFASSale.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("SALE_NO").getObj();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName=clsModules.getHeaderTableName(EITLERPGLOBAL.gCompanyID,clsFASSale.ModuleID);
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="SALE_NO";
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
                data.Execute("UPDATE D_FIN_SALE_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND SALE_NO='"+ObjFlow.DocNo+"'",FinanceGlobal.FinURL);
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
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            
            /////update
            
            
            
            
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsFASSaleDetail ObjItem=(clsFASSaleDetail)colLineItems.get(Integer.toString(i));
                    
                    
                    AssetSrNo=(int)ObjItem.getAttribute("DETAIL_SR_NO").getVal();
                    AssetNo=(String)ObjItem.getAttribute("ASSET_NO").getObj();
                    Asset_Status = ObjItem.getAttribute("ASSET_STATUS").getString();
                    DocNo = getAttribute("SALE_NO").getString();
                    sdate = getAttribute("SALE_DATE").getString();
                    svalue = ObjItem.getAttribute("SALE_VALUE").getDouble();
                    depr_for_the_year = ObjItem.getAttribute("BOOK_CURRENT_YEAR_DEPRECIATION").getDouble();
                    it_depr_for_the_year = ObjItem.getAttribute("IT_CURRENT_YEAR_DEPRECIATION").getDouble();
                    wdv = ObjItem.getAttribute("BOOK_CLOSING_VALUE").getDouble();
                    it_wdv = ObjItem.getAttribute("IT_CLOSING_VALUE").getDouble();
                    // net_block = ObjItem.getAttribute("NET_BLOCK").getDouble();
                    Cumm_Dperi = ObjItem.getAttribute("BOOK_CUMULATIVE_DEPRECIATION").getDouble();
                    it_Cumm_Dperi = ObjItem.getAttribute("IT_CUMULATIVE_DEPRECIATION").getDouble();
                    // Update Dept id
                    data.Execute("UPDATE D_FAS_MASTER_DETAIL SET ASSET_STATUS='"+Asset_Status+"',SALE_VALUE='"+svalue+"',SALE_DATE='"+sdate+"',SALE_DOC_NO='"+DocNo+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND SR_NO="+AssetSrNo+" ",FinanceGlobal.FinURL);
                    //data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo.trim()+"'");
                    
                    ////////////////////////////////// ------------ UPDATE SALES DATA FOR BOOK ---------------- ///////////////////////////////////
                    
                    String SQL ="SELECT B.SR_NO D_SR_NO,A.SR_NO DEX_SR_NO FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                    "WHERE A.DEPRECIATION_FROM_DATE >= '" + EITLERPGLOBAL.getFinYearStartDate(sdate) + "'  " +
                    "AND  A.DEPRECIATION_FROM_DATE <= '" + EITLERPGLOBAL.getFinYearEndDate(sdate) + "'  " +
                    "AND A.ASSET_NO = '" + AssetNo + "' " +
                    "AND A.ASSET_NO = B.ASSET_NO " +
                    "AND A.DETAIL_SR_NO  = B.SR_NO " +
                    "AND A.DETAIL_SR_NO = '" + AssetSrNo + "' AND TYPE = 1 " ;
                    
                    rsUpdate = data.getResult(SQL,FinanceGlobal.FinURL);
                    
                    
                    //String AssetDate = data.getStringValueFromDB("SELECT ASSET_DATE FROM D_FAS_MASTER_HEADER WHERE ASSET_NO = '" + AssetNo + "'",FinanceGlobal.FinURL);
                    if(rsUpdate.getRow()>0) {
                        DSrNo = rsUpdate.getInt("D_SR_NO");
                        DExSrNo = rsUpdate.getInt("DEX_SR_NO");
                        data.Execute("UPDATE D_FAS_MASTER_DETAIL_EX SET DEPRECIATION_TO_DATE='"+sdate+"',DEPRECIATION_FOR_THE_YEAR='"+depr_for_the_year+"',CUMULATIVE_DEPRECIATION = '"+Cumm_Dperi+"',WRITTEN_DOWN_VALUE = '"+wdv+"', CLOSING_BALANCE = '"+wdv+"', CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND DETAIL_SR_NO="+AssetSrNo+" AND TYPE = 1 AND SR_NO = "+DExSrNo+"  ",FinanceGlobal.FinURL);
                    }
                    else {
                        //for the new record insert in the detail_ex table :- BOOK ENTRY
                        SQL = "SELECT A.* FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                        "WHERE A.ASSET_NO = '" + AssetNo + "' AND A.ASSET_NO = B.ASSET_NO AND " +
                        "A.DETAIL_SR_NO  = B.SR_NO AND A.DETAIL_SR_NO = '" + AssetSrNo + "'  AND TYPE = 1 " +
                        "ORDER BY A.SR_NO DESC ";
                        rsTmp=data.getResult(SQL,FinanceGlobal.FinURL);
                        
                        if(rsTmp.getRow()>0) {
                            double CumulativeDeprn = rsTmp.getDouble("CUMULATIVE_DEPRECIATION");
                            double ClosingBalance = rsTmp.getDouble("CLOSING_BALANCE");
                            Statement stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
                            rsDetailEx.first();
                            
                            rsDetailEx.moveToInsertRow();
                            rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                            rsDetailEx.updateString("ASSET_NO",AssetNo);
                            rsDetailEx.updateInt("SR_NO",rsTmp.getInt("SR_NO")+1);
                            rsDetailEx.updateInt("DETAIL_SR_NO",AssetSrNo);
                            rsDetailEx.updateString("TYPE","1");//  1 - Book , 2 - IT
                            rsDetailEx.updateInt("YEAR",rsTmp.getInt("YEAR")+1);
                            rsDetailEx.updateDouble("OPENING_BALANCE",ClosingBalance );
                            rsDetailEx.updateDouble("ADDITIONS",0);
                            
                            String DeprnToDate=EITLERPGLOBAL.addDaysToDate(rsTmp.getString("DEPRECIATION_TO_DATE"),1,"yyyy-MM-dd");
                            rsDetailEx.updateString("DEPRECIATION_FROM_DATE",DeprnToDate);
                            rsDetailEx.updateString("DEPRECIATION_TO_DATE",sdate);
                            
                            
                            
                            SQL="SELECT B.* FROM D_FAS_ITEM_MASTER_HEADER A,D_FAS_ITEM_MASTER_DETAIL B "+
                            "WHERE A.DOC_NO = B.DOC_NO "+
                            "AND A.ITEM_ID = '" + ObjItem.getAttribute("ITEM_ID").getString() + "' "+
                            "AND '" + sdate + "' >= B.FROM_DATE AND '" + sdate + "' <= B.TO_DATE "+
                            "ORDER BY B.SR_NO";
                            
                            double BookRate=0;
                            int MethodId = 0;
                            rsTmp = data.getResult(SQL,FinanceGlobal.FinURL);
                            if(rsTmp.getRow()>0) {
                                BookRate=rsTmp.getDouble("BOOK_PER");
                                MethodId =rsTmp.getInt("METHOD_ID");
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",rsTmp.getDouble("BOOK_PER"));
                                //DataModelB.setValueByVariable("DEPRN_PERCENTAGE",rsTmp.getString("BOOK_PER"),NewRow);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",MethodId );
                                //DataModelB.setValueByVariable("DEPRN_METHOD",String.valueOf(MethodId) ,NewRow);
                                //String Method_name = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"METHOD",MethodId);
                                //DataModelB.setValueByVariable("DEPRN_METHOD_NAME",Method_name,NewRow);
                            }
                            else {
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",0);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",0);
                                
                            }
                            double DeprAmount=0;
                            double AssetCostOriginal= (int)ObjItem.getAttribute("AMOUNT").getVal();
                            int month = EITLERPGLOBAL.getMonthDifference(sdate,DeprnToDate)+1;
                            if(MethodId==2) {
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(AssetCostOriginal, BookRate,month),0);
                            }
                            else if(MethodId==1) {
                                
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(ClosingBalance , BookRate,month),0);
                            }
                            double cDeprn =  CumulativeDeprn + DeprAmount;
                            
                            rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",DeprAmount);
                            rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR","");
                            rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",cDeprn );
                            rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateDouble("CLOSING_BALANCE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateString("REMARKS","");
                            rsDetailEx.updateString("SHIFT","");
                            rsDetailEx.updateBoolean("CHANGED",true);
                            rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsDetailEx.updateBoolean("CANCELLED",false);
                            rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                            rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                            rsDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                            rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                            rsDetailEx.insertRow();
                        }
                        
                    }//end else
                    
                    /////////////////////////////////// ------------ UPDATE SALES DATA FOR IT ---------------- ///////////////////////////////////
                    
                    String SQLIT ="SELECT B.SR_NO D_SR_NO,A.SR_NO DEX_SR_NO FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                    "WHERE A.DEPRECIATION_FROM_DATE >= '" + EITLERPGLOBAL.getFinYearStartDate(sdate) + "'  " +
                    "AND  A.DEPRECIATION_FROM_DATE <= '" + EITLERPGLOBAL.getFinYearEndDate(sdate) + "'  " +
                    "AND A.ASSET_NO = '" + AssetNo + "' " +
                    "AND A.ASSET_NO = B.ASSET_NO " +
                    "AND A.DETAIL_SR_NO  = B.SR_NO " +
                    "AND A.DETAIL_SR_NO = '" + AssetSrNo + "' AND TYPE = 2 " ;
                    
                    rsUpdateIT = data.getResult(SQLIT,FinanceGlobal.FinURL);
                    
                    
                    //String AssetDate = data.getStringValueFromDB("SELECT ASSET_DATE FROM D_FAS_MASTER_HEADER WHERE ASSET_NO = '" + AssetNo + "'",FinanceGlobal.FinURL);
                    if(rsUpdate.getRow()>0) {
                        DSrNo = rsUpdateIT.getInt("D_SR_NO");
                        DExSrNo = rsUpdateIT.getInt("DEX_SR_NO");
                        data.Execute("UPDATE D_FAS_MASTER_DETAIL_EX SET DEPRECIATION_TO_DATE='"+sdate+"',DEPRECIATION_FOR_THE_YEAR='"+it_depr_for_the_year+"',CUMULATIVE_DEPRECIATION = '"+it_Cumm_Dperi+"',WRITTEN_DOWN_VALUE = '"+it_wdv+"', CLOSING_BALANCE = '"+it_wdv+"', CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+gCompanyID+" AND ASSET_NO='"+AssetNo.trim()+"' AND DETAIL_SR_NO="+AssetSrNo+" AND TYPE = 2 AND SR_NO = "+DExSrNo+"  ",FinanceGlobal.FinURL);
                    }
                    else {
                        //for the new record insert in the detail_ex table :- IT ENTRY
                        SQL = "SELECT A.* FROM  D_FAS_MASTER_DETAIL_EX A,D_FAS_MASTER_DETAIL B " +
                        "WHERE A.ASSET_NO = '" + AssetNo + "' AND A.ASSET_NO = B.ASSET_NO AND " +
                        "A.DETAIL_SR_NO  = B.SR_NO AND A.DETAIL_SR_NO = '" + AssetSrNo + "'  AND TYPE = 2 " +
                        "ORDER BY A.SR_NO DESC ";
                        rsTmp=data.getResult(SQL,FinanceGlobal.FinURL);
                        
                        if(rsTmp.getRow()>0) {
                            double CumulativeDeprn = rsTmp.getDouble("CUMULATIVE_DEPRECIATION");
                            double ClosingBalance = rsTmp.getDouble("CLOSING_BALANCE");
                            Statement stDetailEx=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            ResultSet rsDetailEx=stDetailEx.executeQuery("SELECT * FROM D_FAS_MASTER_DETAIL_EX WHERE ASSET_NO='1'");
                            rsDetailEx.first();
                            
                            rsDetailEx.moveToInsertRow();
                            rsDetailEx.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                            rsDetailEx.updateString("ASSET_NO",AssetNo);
                            rsDetailEx.updateInt("SR_NO",rsTmp.getInt("SR_NO")+1);
                            rsDetailEx.updateInt("DETAIL_SR_NO",AssetSrNo);
                            rsDetailEx.updateString("TYPE","2");//  1 - Book , 2 - IT
                            rsDetailEx.updateInt("YEAR",rsTmp.getInt("YEAR")+1);
                            rsDetailEx.updateDouble("OPENING_BALANCE",ClosingBalance );
                            rsDetailEx.updateDouble("ADDITIONS",0);
                            
                            String DeprnToDate=EITLERPGLOBAL.addDaysToDate(rsTmp.getString("DEPRECIATION_TO_DATE"),1,"yyyy-MM-dd");
                            rsDetailEx.updateString("DEPRECIATION_FROM_DATE",DeprnToDate);
                            rsDetailEx.updateString("DEPRECIATION_TO_DATE",sdate);
                            
                            
                            
                            SQL="SELECT B.* FROM D_FAS_ITEM_MASTER_HEADER A,D_FAS_ITEM_MASTER_DETAIL B "+
                            "WHERE A.DOC_NO = B.DOC_NO "+
                            "AND A.ITEM_ID = '" + ObjItem.getAttribute("ITEM_ID").getString() + "' "+
                            "AND '" + sdate + "' >= B.FROM_DATE AND '" + sdate + "' <= B.TO_DATE "+
                            "ORDER BY B.SR_NO";
                            
                            double BookRate=0;
                            int MethodId = 0;
                            rsTmp = data.getResult(SQL,FinanceGlobal.FinURL);
                            if(rsTmp.getRow()>0) {
                                BookRate=rsTmp.getDouble("IT_PER");
                                MethodId =rsTmp.getInt("METHOD_ID");
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",rsTmp.getDouble("IT_PER"));
                                //DataModelB.setValueByVariable("DEPRN_PERCENTAGE",rsTmp.getString("BOOK_PER"),NewRow);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",MethodId );
                                //DataModelB.setValueByVariable("DEPRN_METHOD",String.valueOf(MethodId) ,NewRow);
                                //String Method_name = clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"METHOD",MethodId);
                                //DataModelB.setValueByVariable("DEPRN_METHOD_NAME",Method_name,NewRow);
                            }
                            else {
                                rsDetailEx.updateDouble("DEPRECIATION_PERCENTAGE",0);
                                rsDetailEx.updateInt("DEPRECIATION_METHOD",0);
                                
                            }
                            double DeprAmount=0;
                            double AssetCostOriginal= (int)ObjItem.getAttribute("AMOUNT").getVal();
                            int month = EITLERPGLOBAL.getMonthDifference(sdate,DeprnToDate)+1;
                            if(MethodId==2) {
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(AssetCostOriginal, BookRate,month),0);
                            }
                            else if(MethodId==1) {
                                
                                DeprAmount = EITLERPGLOBAL.round(clsFASGlobal.WrittenDownMethod(ClosingBalance , BookRate,month),0);
                            }
                            double cDeprn =  CumulativeDeprn + DeprAmount;
                            
                            rsDetailEx.updateDouble("DEPRECIATION_FOR_THE_YEAR",DeprAmount);
                            rsDetailEx.updateString("SHIFT_ALLOW_FOR_THE_YEAR","");
                            rsDetailEx.updateDouble("CUMULATIVE_DEPRECIATION",cDeprn );
                            rsDetailEx.updateDouble("WRITTEN_DOWN_VALUE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateDouble("CLOSING_BALANCE",ClosingBalance  - DeprAmount);
                            rsDetailEx.updateString("REMARKS","");
                            rsDetailEx.updateString("SHIFT","");
                            rsDetailEx.updateBoolean("CHANGED",true);
                            rsDetailEx.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                            rsDetailEx.updateBoolean("CANCELLED",false);
                            rsDetailEx.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getInt()));
                            rsDetailEx.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
                            rsDetailEx.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getInt()));
                            rsDetailEx.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                            rsDetailEx.insertRow();
                        }
                    }//end else
                    
                }
                    if(!PostSJForAsset(EITLERPGLOBAL.gCompanyID,DocNo, AssetNo)) {
                        JOptionPane.showMessageDialog(null,"Sales Journal Voucher not posted for Asset No : " + AssetNo +"");
                    }
                    
                //}
                
                
                // rsUpdate.close();
                // rsUpdateIT.close();
            }
            
            
            ObjFlow.UseSpecifiedURL=false;
            return true;
            
        }
        catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_FAS_SALE_HEADER A,D_FAS_SALE_DETAIL B " + pCondition ;
            Conn=data.getConn(FinanceGlobal.FinURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_FAS_SALE_HEADER A,D_FAS_SALE_DETAIL B WHERE A.SALE_NO = B.SALE_NO AND A.COMPANY_ID="+pCompanyID+" AND A.DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND A.DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY A.SALE_NO";
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
    
    
    
    private boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        Connection tmpConn;
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        tmpConn=data.getConn(FinanceGlobal.FinURL);
        
        try {
            
            
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Populate the user
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("SALE_NO",UtilFunctions.getString(rsResultSet,"SALE_NO",""));
            setAttribute("DOC_DATE",UtilFunctions.getString(rsResultSet,"DOC_DATE","0000-00-00"));
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE",""));
            setAttribute("INVOICE_NO",UtilFunctions.getString(rsResultSet,"INVOICE_NO",""));
            setAttribute("INVOICE_DATE",UtilFunctions.getString(rsResultSet,"INVOICE_DATE","0000-00-00"));
            setAttribute("SALE_DATE",UtilFunctions.getString(rsResultSet,"SALE_DATE","0000-00-00"));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS",""));
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getInt(rsResultSet,"REJECTED",0));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS",""));
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY",""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY",""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            setAttribute("CHANGED",UtilFunctions.getInt(rsResultSet,"CHANGED",0));
            setAttribute("CHANGED_DATE",UtilFunctions.getString(rsResultSet,"CHANGED_DATE","0000-00-00"));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            long mCompanyID= getAttribute("COMPANY_ID").getLong();
            String mDocno= getAttribute("SALE_NO").getString();
            tmpStmt=tmpConn.createStatement();
            String SQL= "";
            if(HistoryView) {
                SQL = "SELECT * FROM D_FAS_SALE_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND SALE_NO='"+mDocno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SALE_NO ";
                rsTmp=tmpStmt.executeQuery(SQL);
            } else {
                SQL = "SELECT * FROM D_FAS_SALE_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND SALE_NO='"+mDocno.trim()+"' ORDER BY SALE_NO";
                rsTmp=tmpStmt.executeQuery(SQL);
            }
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsFASSaleDetail ObjItem=new clsFASSaleDetail();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("SALE_NO",rsTmp.getString("SALE_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjItem.setAttribute("ASSET_NO",rsTmp.getString("ASSET_NO"));
                ObjItem.setAttribute("DETAIL_SR_NO",rsTmp.getLong("DETAIL_SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                //ObjItem.setAttribute("SALE_DATE",rsTmp.getString("SALE_DATE"));
                ObjItem.setAttribute("BOOK_OPENING_VALUE",rsTmp.getDouble("BOOK_OPENING_VALUE"));
                ObjItem.setAttribute("BOOK_CURRENT_YEAR_DEPRECIATION",rsTmp.getDouble("BOOK_CURRENT_YEAR_DEPRECIATION"));
                ObjItem.setAttribute("BOOK_CUMULATIVE_DEPRECIATION",rsTmp.getDouble("BOOK_CUMULATIVE_DEPRECIATION"));
                ObjItem.setAttribute("BOOK_CLOSING_VALUE",rsTmp.getDouble("BOOK_CLOSING_VALUE"));
                ObjItem.setAttribute("BOOK_PROFIT_LOSS",rsTmp.getDouble("BOOK_PROFIT_LOSS"));
                ObjItem.setAttribute("IT_OPENING_VALUE",rsTmp.getDouble("IT_OPENING_VALUE"));
                ObjItem.setAttribute("IT_CURRENT_YEAR_DEPRECIATION",rsTmp.getDouble("IT_CURRENT_YEAR_DEPRECIATION"));
                ObjItem.setAttribute("IT_CUMULATIVE_DEPRECIATION",rsTmp.getDouble("IT_CUMULATIVE_DEPRECIATION"));
                ObjItem.setAttribute("IT_CLOSING_VALUE",rsTmp.getDouble("IT_CLOSING_VALUE"));
                ObjItem.setAttribute("IT_PROFIT_LOSS",rsTmp.getDouble("IT_PROFIT_LOSS"));
                ObjItem.setAttribute("DEPT_ID",rsTmp.getLong("DEPT_ID"));
                ObjItem.setAttribute("AMOUNT",rsTmp.getDouble("AMOUNT"));
                ObjItem.setAttribute("YEAR",rsTmp.getString("YEAR"));
                ObjItem.setAttribute("ASSET_STATUS",rsTmp.getString("ASSET_STATUS"));
                ObjItem.setAttribute("SALE_VALUE",rsTmp.getDouble("SALE_VALUE"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
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
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FAS_SALE_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SALE_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsFASSale ObjFASSale=new clsFASSale();
                
                ObjFASSale.setAttribute("SALE_NO",rsTmp.getString("SALE_NO"));
                ObjFASSale.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjFASSale.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjFASSale.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjFASSale.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjFASSale.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjFASSale.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjFASSale);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            
            return List;
            
        }
        catch(Exception e) {
            e.printStackTrace();
            return List;
            
        }
    }
    
    public boolean Amend() {
        Statement stTmp,stHistory,stHDetail,stHSupp;
        ResultSet rsTmp,rsSupp,rsHistory,rsHDetail,rsHSupp;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "",IndentNo = "";
        int IndentSrno = 0;
        double IndentQty=0,PrevQty=0,CurrentQty=0;
        
        try {
            
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    private boolean Validate() {
        return true;
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM FINANCE.D_FAS_SALE_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND SALE_NO='"+pDocNo+"' ");
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
    
    public static HashMap getPendingApprovals(int CompanyID,int UserID,int Order,int FinYearFrom)  {
        ResultSet rsTemp=null;
        String strSQL="";
        Statement tmpStmt3=null;
        Connection tmpConn;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt3=tmpConn.createStatement();
            
            if(Order==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT FINANCE.D_FAS_SALE_HEADER.SALE_NO,FINANCE.D_FAS_SALE_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_SALE_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_SALE_HEADER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FAS_SALE_HEADER.APPROVED=0 AND FINANCE.D_FAS_SALE_HEADER.SALE_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT FINANCE.D_FAS_SALE_HEADER.SALE_NO,FINANCE.D_FAS_SALE_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_SALE_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_SALE_HEADER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FAS_SALE_HEADER.APPROVED=0 AND FINANCE.D_FAS_SALE_HEADER.SALE_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FAS_SALE_HEADER.DOC_DATE";
            }
            
            if(Order==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT FINANCE.D_FAS_SALE_HEADER.SALE_NO,FINANCE.D_FAS_SALE_HEADER.DOC_DATE,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.RECEIVED_DATE FROM FINANCE.D_FAS_SALE_HEADER,"+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA WHERE FINANCE.D_FAS_SALE_HEADER.COMPANY_ID=DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID AND DINESHMILLS.D_COM_DOC_DATA.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND FINANCE.D_FAS_SALE_HEADER.APPROVED=0 AND FINANCE.D_FAS_SALE_HEADER.SALE_NO="+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.DOC_NO AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.USER_ID="+UserID+" AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.STATUS='W' AND "+EITLERPGLOBAL.DBName+".D_COM_DOC_DATA.MODULE_ID="+ModuleID+" ORDER BY FINANCE.D_FAS_SALE_HEADER.SALE_NO";
            }
            
            //rsTemp=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsTemp=tmpStmt3.executeQuery(strSQL);
            
            rsTemp.first();
            
            Counter=0;
            
            //if(rsTemp.getRow()>0) {
            
            while(!rsTemp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTemp.getString("DOC_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsFASSale ObjDoc=new clsFASSale();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("SALE_NO",UtilFunctions.getString(rsTemp,"SALE_NO",""));
                    ObjDoc.setAttribute("DOC_DATE",UtilFunctions.getString(rsTemp,"DOC_DATE","0000-00-00"));
                    ObjDoc.setAttribute("RECEIVED_DATE",UtilFunctions.getString(rsTemp,"RECEIVED_DATE","0000-00-00"));
                    // ----------------- End of Header Fields ------------------------------------//
                    
                    List.put(Long.toString(Counter),ObjDoc);
                    
                    
                    
                    //rsTemp.next();
                }
                
                rsTemp.next();
                
            }
            //}
            rsTemp.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    public static HashMap getAssetItemList(int pCompanyID,String pAssetNo) {
        //Fourth argument pAllItems indicates whether you want all item listing
        
        Connection tmpConn;
        Statement stTmp=null,stTmp1=null;
        ResultSet rsTmp=null,rsTmp1=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND ASSET_NO='"+pAssetNo+"' AND APPROVED=1");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                strSQL="SELECT D_FAS_MASTER_DETAIL.* FROM D_FAS_MASTER_DETAIL,D_FAS_MASTER_HEADER  WHERE D_FAS_MASTER_DETAIL.COMPANY_ID=D_FAS_MASTER_HEADER.COMPANY_ID AND D_FAS_MASTER_DETAIL.ASSET_NO=D_FAS_MASTER_HEADER.ASSET_NO AND D_FAS_MASTER_DETAIL.COMPANY_ID="+pCompanyID+" AND D_FAS_MASTER_DETAIL.ASSET_NO='"+pAssetNo+"' AND D_FAS_MASTER_DETAIL.ASSET_STATUS='' ORDER BY ASSET_NO";
                String Item_Id = rsTmp.getString("ITEM_ID").toString();
                stTmp1=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp1=stTmp1.executeQuery(strSQL);
                rsTmp1.first();
                
                Counter1=0;
                
                while(!rsTmp1.isAfterLast()) {
                    Counter1++;
                    clsFASMasterItem ObjItem=new clsFASMasterItem();
                    
                    //int SaleSrNo=rsTmp1.getInt("SR_NO");
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                    ObjItem.setAttribute("ASSET_NO",rsTmp1.getString("ASSET_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                    ObjItem.setAttribute("YEAR",rsTmp1.getString("YEAR"));
                    ObjItem.setAttribute("DEPT_ID",rsTmp1.getLong("DEPT_ID"));
                    ObjItem.setAttribute("ITEM_ID",rsTmp1.getString("ITEM_ID"));
                    ObjItem.setAttribute("AMOUNT",rsTmp1.getDouble("AMOUNT"));
                    ObjItem.setAttribute("ASSET_STATUS",rsTmp1.getString("ASSET_STATUS"));
                    ObjItem.setAttribute("SALE_VALUE",rsTmp1.getDouble("SALE_VALUE"));
                    ObjItem.setAttribute("SALE_DATE",rsTmp1.getString("SALE_DATE"));
                    ObjItem.setAttribute("SALE_DOC_NO",rsTmp1.getString("SALE_DOC_NO"));
                    //ObjItem.setAttribute("SJ_NUMBER",rsTmp1.getString("SJ_NUMBER"));
                    ObjItem.setAttribute("REMARKS",rsTmp1.getString("REMARKS"));
                    ObjItem.setAttribute("CANCELLED",rsTmp1.getInt("CANCELLED"));
                    ObjItem.setAttribute("CREATED_BY",rsTmp1.getString("CREATED_BY"));
                    ObjItem.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                    ObjItem.setAttribute("MODIFIED_BY",rsTmp1.getString("MODIFIED_BY"));
                    ObjItem.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                    ObjItem.setAttribute("CHANGED",rsTmp1.getInt("CHANGED"));
                    ObjItem.setAttribute("CHANGED_DATE",rsTmp1.getString("CHANGED_DATE"));
                    
                    //Put into list
                    List.put(Integer.toString(Counter1),ObjItem);
                    
                    rsTmp1.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            stTmp1.close();
            rsTmp.close();
            rsTmp1.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    public static HashMap getAssetItemList(int pCompanyID,String pAssetNo,int pSrNo) {
        //Fourth argument pAllItems indicates whether you want all item listing
        
        Connection tmpConn;
        Statement stTmp=null,stTmp1=null;
        ResultSet rsTmp=null,rsTmp1=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FAS_MASTER_HEADER WHERE COMPANY_ID="+pCompanyID+" AND ASSET_NO='"+pAssetNo+"' AND APPROVED=1");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                strSQL="SELECT D_FAS_MASTER_DETAIL.* FROM D_FAS_MASTER_DETAIL,D_FAS_MASTER_HEADER  WHERE D_FAS_MASTER_DETAIL.COMPANY_ID=D_FAS_MASTER_HEADER.COMPANY_ID AND D_FAS_MASTER_DETAIL.ASSET_NO=D_FAS_MASTER_HEADER.ASSET_NO AND D_FAS_MASTER_DETAIL.COMPANY_ID="+pCompanyID+" AND D_FAS_MASTER_DETAIL.ASSET_NO='"+pAssetNo+"' AND  D_FAS_MASTER_DETAIL.SR_NO='"+pSrNo+"' AND D_FAS_MASTER_DETAIL.ASSET_STATUS=' ' ORDER BY ASSET_NO";
                String Item_Id = rsTmp.getString("ITEM_ID").toString();
                stTmp1=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp1=stTmp1.executeQuery(strSQL);
                rsTmp1.first();
                
                Counter1=0;
                
                while(!rsTmp1.isAfterLast()) {
                    Counter1++;
                    clsFASMasterItem ObjItem=new clsFASMasterItem();
                    
                    //int SaleSrNo=rsTmp1.getInt("SR_NO");
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                    ObjItem.setAttribute("ASSET_NO",rsTmp1.getString("ASSET_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                    ObjItem.setAttribute("YEAR",rsTmp1.getString("YEAR"));
                    ObjItem.setAttribute("DEPT_ID",rsTmp1.getLong("DEPT_ID"));
                    ObjItem.setAttribute("ITEM_ID",rsTmp1.getString("ITEM_ID"));
                    ObjItem.setAttribute("AMOUNT",rsTmp1.getDouble("AMOUNT"));
                    ObjItem.setAttribute("ASSET_STATUS",rsTmp1.getString("ASSET_STATUS"));
                    ObjItem.setAttribute("SALE_VALUE",rsTmp1.getDouble("SALE_VALUE"));
                    ObjItem.setAttribute("SALE_DATE",rsTmp1.getString("SALE_DATE"));
                    ObjItem.setAttribute("SALE_DOC_NO",rsTmp1.getString("SALE_DOC_NO"));
                   // ObjItem.setAttribute("SJ_NUMBER",rsTmp1.getString("SJ_NUMBER"));
                    ObjItem.setAttribute("REMARKS",rsTmp1.getString("REMARKS"));
                    ObjItem.setAttribute("CANCELLED",rsTmp1.getInt("CANCELLED"));
                    ObjItem.setAttribute("CREATED_BY",rsTmp1.getString("CREATED_BY"));
                    ObjItem.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                    ObjItem.setAttribute("MODIFIED_BY",rsTmp1.getString("MODIFIED_BY"));
                    ObjItem.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                    ObjItem.setAttribute("CHANGED",rsTmp1.getInt("CHANGED"));
                    ObjItem.setAttribute("CHANGED_DATE",rsTmp1.getString("CHANGED_DATE"));
                    
                    //Put into list
                    //List.put(Integer.toString(Counter1),ObjItem);
                    List.put(Integer.toString(rsTmp1.getInt("SR_NO")),ObjItem);
                    
                    
                    rsTmp1.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            stTmp1.close();
            rsTmp.close();
            rsTmp1.close();
            
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
            rsTmp=data.getResult("SELECT ASSET_NO,APPROVED,CANCELLED FROM FINANCE.D_FAS_SALE_HEADER WHERE COMPANY_ID="+pCompanyID+" AND ASSET_NO='"+pDocNo+"' ",FinanceGlobal.FinURL);
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
    
    public Object getObject(int pCompanyID, String pReqNo) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND REQ_NO='" + pReqNo + "'" ;
        clsFASSale ObjSale = new clsFASSale();
        ObjSale.Filter(strCondition,pCompanyID);
        return ObjSale;
    }
    
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT SALE_NO FROM D_FAS_SALE_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SALE_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            
            if(data.IsRecordExist(strSQL,FinanceGlobal.FinURL)) {
                return false;
            }
            else {
                
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        
      /*  Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
       
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_FAS_SALE_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SALE_NO='"+pDocNo+"' AND (APPROVED=1)";
       
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
       
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                //EITLERPGLOBAL.DatabaseURL;
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        }*/
        catch(Exception e) {
            e.printStackTrace();
            return false;
            
        }
    }
    
    private boolean PostSJForAsset(int CompanyID,String DocNo, String AssetNo) {
        try {
            clsVoucher objVoucher=new clsVoucher();
            
            String SelPrefix="";
            String SelSuffix="";
            int FFNo=0;
            /*========== Select the Hierarchy ======== */
            int HierarchyID = 0;
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFASSale.ModuleID, "CHOOSE_HIERARCHY", "DEFAULT", "");
            
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
            }
            /*========== End of Hierarchy Selection ======== */
            
            /*========== Select Prifix, Suffix and Firstfree No. ======== */
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID="+clsVoucher.SalesJournalVoucherModuleID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SelPrefix=rsTmp.getString("PREFIX_CHARS");
                SelSuffix=rsTmp.getString("SUFFIX_CHARS");
                FFNo=rsTmp.getInt("FIRSTFREE_NO");
            }
            /*========== End of Prifix, Suffix and Firstfree No. ======== */
            
            //============= Gethering Data =================//
            int VoucherSrNo=0;
            String BookCode = "";
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFASSale.ModuleID , "GET_BOOK_CODE", "SALES_JOURNAL", "");
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                BookCode = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            String SaleofFixedAsset = "";
            
            List.clear();
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsFASSale.ModuleID , "GET_ACCOUNT_CODE", "SALE_OF_FIXED_ASSET", "");
            if(List.size()>0) {
                //Get TDS Account Code
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                SaleofFixedAsset = objRule.getAttribute("RULE_OUTCOME").getString();
            }
            
            double SaleAmount = data.getDoubleValueFromDB("SELECT SUM(SALE_VALUE) FROM D_FAS_SALE_DETAIL WHERE SALE_NO='"+DocNo+"' AND ASSET_NO='"+AssetNo+"' ",FinanceGlobal.FinURL);
            String SaleDate = data.getStringValueFromDB("SELECT SALE_DATE FROM D_FAS_SALE_HEADER WHERE SALE_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String PartyCode = data.getStringValueFromDB("SELECT PARTY_CODE FROM D_FAS_SALE_HEADER WHERE SALE_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
            String DocDate = data.getStringValueFromDB("SELECT DOC_DATE FROM D_FAS_SALE_HEADER WHERE SALE_NO='"+DocNo+"' ",FinanceGlobal.FinURL);
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
            objVoucher.setAttribute("LEGACY_NO","");
            objVoucher.setAttribute("LEGACY_DATE","0000-00-00");
            objVoucher.setAttribute("BOOK_CODE",BookCode);
            objVoucher.setAttribute("VOUCHER_TYPE",FinanceGlobal.TYPE_SALES_JOURNAL);
            objVoucher.setAttribute("CHEQUE_NO","");
            objVoucher.setAttribute("CHEQUE_DATE","0000-00-00");
            objVoucher.setAttribute("BANK_NAME","");
            objVoucher.setAttribute("VOUCHER_DATE",EITLERPGLOBAL.formatDate(SaleDate));//
            objVoucher.setAttribute("ST_CATEGORY","");
            objVoucher.setAttribute("MODULE_ID",0);
            objVoucher.setAttribute("REMARKS","");
            
            objVoucher.setAttribute("HIERARCHY_ID",HierarchyID);
            int FirstUserID=data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID="+HierarchyID+" AND SR_NO=1");
            objVoucher.setAttribute("FROM",FirstUserID);
            objVoucher.setAttribute("TO",FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS","");
            objVoucher.setAttribute("APPROVAL_STATUS","H"); //Final Approved --> Voucher
            /*============== End of Voucher Header ================*/
            
            /*============== Voucher Detail =================*/
            objVoucher.colVoucherItems.clear();
            VoucherSrNo++;
            clsVoucherItem objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","D");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE","210034");
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE",PartyCode);
            objVoucherItem.setAttribute("AMOUNT",SaleAmount);
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",DocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsFASSale.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            VoucherSrNo++;
            objVoucherItem=new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT","C");
            objVoucherItem.setAttribute("ACCOUNT_ID",1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",SaleofFixedAsset);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
            objVoucherItem.setAttribute("AMOUNT",SaleAmount);
            objVoucherItem.setAttribute("PO_NO","");
            objVoucherItem.setAttribute("PO_DATE","0000-00-00");
            objVoucherItem.setAttribute("REMARKS","");
            objVoucherItem.setAttribute("INVOICE_NO","");
            objVoucherItem.setAttribute("INVOICE_DATE","0000-00-00");
            objVoucherItem.setAttribute("INVOICE_AMOUNT",0);
            objVoucherItem.setAttribute("GRN_NO",DocNo);
            objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(DocDate));
            objVoucherItem.setAttribute("MODULE_ID",clsFASSale.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID",CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size()+1),objVoucherItem);
            
            /*============== End of Voucher Detail =================*/
            
            if(objVoucher.Insert()) {
                objVoucher.IsInternalPosting=false;
                return true;
            }
            else {
                objVoucher.IsInternalPosting=false;
                return false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
