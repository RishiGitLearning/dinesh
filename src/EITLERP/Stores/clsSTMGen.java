/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Stores;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsSTMGen {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    public static int STMgenType =1 ;
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colSTMItems=new HashMap();
    
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
    public clsSTMGen() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("STM_NO",new Variant(""));
        props.put("STM_TYPE",new Variant(0));
        props.put("STM_DATE",new Variant(""));
        props.put("FOR_DEPT_ID",new Variant(0));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("MODE_TRANSPORT",new Variant(0));
        props.put("TRANSPORTER",new Variant(0));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("GROSS_AMOUNT",new Variant(0));
        props.put("TRANSFER_TO_UNIT",new Variant(0));
        props.put("PURPOSE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("STATUS",new Variant(""));
        props.put("CANCELLED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        props.put("COLUMN_1_ID",new Variant(0));
        props.put("COLUMN_1_FORMULA",new Variant(""));
        props.put("COLUMN_1_PER",new Variant(0));
        props.put("COLUMN_1_AMT",new Variant(0));
        props.put("COLUMN_1_CAPTION",new Variant(""));
        props.put("COLUMN_2_ID",new Variant(0));
        props.put("COLUMN_2_FORMULA",new Variant(""));
        props.put("COLUMN_2_PER",new Variant(0));
        props.put("COLUMN_2_AMT",new Variant(0));
        props.put("COLUMN_2_CAPTION",new Variant(""));
        props.put("COLUMN_3_ID",new Variant(0));
        props.put("COLUMN_3_FORMULA",new Variant(""));
        props.put("COLUMN_3_PER",new Variant(0));
        props.put("COLUMN_3_AMT",new Variant(0));
        props.put("COLUMN_3_CAPTION",new Variant(""));
        props.put("COLUMN_4_ID",new Variant(0));
        props.put("COLUMN_4_FORMULA",new Variant(""));
        props.put("COLUMN_4_PER",new Variant(0));
        props.put("COLUMN_4_AMT",new Variant(0));
        props.put("COLUMN_4_CAPTION",new Variant(""));
        props.put("COLUMN_5_ID",new Variant(0));
        props.put("COLUMN_5_FORMULA",new Variant(""));
        props.put("COLUMN_5_PER",new Variant(0));
        props.put("COLUMN_5_AMT",new Variant(0));
        props.put("COLUMN_5_CAPTION",new Variant(""));
        props.put("COLUMN_6_ID",new Variant(0));
        props.put("COLUMN_6_FORMULA",new Variant(""));
        props.put("COLUMN_6_PER",new Variant(0));
        props.put("COLUMN_6_AMT",new Variant(0));
        props.put("COLUMN_6_CAPTION",new Variant(""));
        props.put("COLUMN_7_ID",new Variant(0));
        props.put("COLUMN_7_FORMULA",new Variant(""));
        props.put("COLUMN_7_PER",new Variant(0));
        props.put("COLUMN_7_AMT",new Variant(0));
        props.put("COLUMN_7_CAPTION",new Variant(""));
        props.put("COLUMN_8_ID",new Variant(0));
        props.put("COLUMN_8_FORMULA",new Variant(""));
        props.put("COLUMN_8_PER",new Variant(0));
        props.put("COLUMN_8_AMT",new Variant(0));
        props.put("COLUMN_8_CAPTION",new Variant(""));
        props.put("COLUMN_9_ID",new Variant(0));
        props.put("COLUMN_9_FORMULA",new Variant(""));
        props.put("COLUMN_9_PER",new Variant(0));
        props.put("COLUMN_9_AMT",new Variant(0));
        props.put("COLUMN_9_CAPTION",new Variant(""));
        props.put("COLUMN_10_ID",new Variant(0));
        props.put("COLUMN_10_FORMULA",new Variant(""));
        props.put("COLUMN_10_PER",new Variant(0));
        props.put("COLUMN_10_AMT",new Variant(0));
        props.put("COLUMN_10_CAPTION",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_STM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_TYPE=1 AND STM_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND STM_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY STM_NO");
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
        Connection tmpConn;
        
        Statement stItem,stStock,stTmp,stItemMaster,stHistory,stHDetail;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsHistory,rsHDetail;
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="",LocationID="",IndentNo="",strSQL="",BOEDate="";
        int CompanyID=0,IndentSrNo=0;
        double RejectedQty=0,Qty=0,ToleranceLimit=0;
        String StmDate="";
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("STM_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_STM_HEADER_H WHERE STM_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_STM_DETAIL_H WHERE STM_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_STM_HEADER WHERE STM_NO='1'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            
            //REMAINING FOR CHECK AGAINST STM REQUISITION
            //=========== Check the Quantities entered against Indent============= //
            /*for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                int IndentCompany=(int)ObjItem.getAttribute("INDENT_COMPANY_ID").getVal();
                int IndentCompanyYear=(int)ObjItem.getAttribute("INDENT_COMPANY_YEAR").getVal();
             
                //ToleranceLimit=clsItem.getToleranceLimit(CompanyID,(String)ObjItem.getAttribute("ITEM_ID").getObj());
                tmpConn=data.getConn(clsFinYear.getDBURL(IndentCompany,IndentCompanyYear));
             
                double IndentQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
             
                if((!IndentNo.trim().equals(""))&&(IndentSrNo>0)) //MIR No. entered
                {
                    //Get the  MIR Qty.
                    stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_INDENT_DETAIL WHERE INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
             
                    if(rsTmp.getRow()>0) {
                        IndentQty=rsTmp.getDouble("QTY");
                    }
             
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_STM_DETAIL WHERE INDENT_NO='"+IndentNo+"' AND INDENT_SR_NO="+IndentSrNo+" AND INDENT_COMPANY_ID="+IndentCompany+" AND INDENT_COMPANY_YEAR="+IndentCompanyYear+" AND STM_NO NOT IN(SELECT STM_NO FROM D_INV_STM_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
             
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
             
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
             
                    if((CurrentQty+PrevQty) > IndentQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Indent No. "+IndentNo+" Sr. No. "+IndentSrNo+" qty "+IndentQty+". Please verify the input.";
                        return false;
                    }
                }
            }*/
            //============== Indent Checking Completed ====================//
            
            
            //========================= Stock Checking ===============================//
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String docDate=(String)getAttribute("STM_DATE").getObj();
            
            for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                
                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                
                clsStockInfo objStock=(new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID,EITLERPGLOBAL.getCurrentDateDB());
                double OnHandQty=objStock.StockQty;
                double MinQty=clsItem.getMinQty(EITLERPGLOBAL.gCompanyID,ItemID);
                
                String StmReqNo=(String)ObjItem.getAttribute("MIR_NO").getObj();
                
                int StmReqSrNo =(int)ObjItem.getAttribute("MIR_SR_NO").getVal();
                double qty = (double)ObjItem.getAttribute("QTY").getDouble();
                double qtyReqd = (double)ObjItem.getAttribute("QTY_REQD").getDouble();
                
                /*String strSql="SELECT SUM(QTY) FROM D_INV_STM_DETAIL WHERE COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND STM_REQ_NO = '" + StmReqNo + "' " +
                "AND STM_REQ_SR_NO = '" + StmReqSrNo + "' AND ITEM_ID = '" + ItemID + "' ";
                */
                String strSql="SELECT SUM(B.QTY) FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B " +
                "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.STM_TYPE=B.STM_TYPE " +
                "AND B.COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND B.STM_REQ_NO = '" + StmReqNo + "' " +
                "AND B.STM_REQ_SR_NO ='" + StmReqSrNo + "' AND B.ITEM_ID ='" + ItemID + "' AND A.CANCELLED=0";
                
                double TotalStmReqQty = data.getDoubleValueFromDB(strSql);
                System.out.println("QTY=" + qty + " totla= " + TotalStmReqQty);
                
                if(qty+TotalStmReqQty >qtyReqd) {
                    //LastError="Quantity entered:" + qty + " Previous STM Requestion Qty entered:" + TotalStmReqQty + " must be lesst then to Required Qty: " + qtyReqd;
                    LastError="STM Requestion Qty: " + qtyReqd + " already transfered thru STM Qty :" + TotalStmReqQty + " Required STM Qty is :" + qty;
                    return false;
                }
            }
            
            if(AStatus.equals("F")) {
                String ToDate = getAttribute("STM_DATE").getString();
                String FromDate =data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+ToDate+"' ORDER BY ENTRY_DATE DESC LIMIT 1");
                
                for(int i=1;i<=colSTMItems.size();i++) {
                    clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                    Qty=ObjItem.getAttribute("QTY").getDouble();
                    ItemID=ObjItem.getAttribute("ITEM_ID").getString();
                    
                    double ClosingRate = new clsItemStock().ProcessLedger(FromDate, ToDate,ItemID);
                    ObjItem.setAttribute("RATE",EITLERPGLOBAL.round(ClosingRate, 3));
                    ObjItem.setAttribute("NET_AMOUNT", EITLERPGLOBAL.round(ClosingRate*Qty,3));
                    ObjItem.setAttribute("TOTAL_AMOUNT", EITLERPGLOBAL.round(ClosingRate*Qty,3));
                    colSTMItems.put(Integer.toString(i),ObjItem);
                }
            }
            // comment by muffy
                /*
                 //========= New Code of Allow Excess Issue ===========//
                boolean AllowExcess=false;
                //====================================================//
                if(MinQty>0)  //Minimum level maintained
                {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                 
                    //Get the sum of all repeated item codes
                    for(int j=1;i<=colSTMItems.size();i++) {
                        clsSTMGenItem ObjItemEx=(clsSTMGenItem)colSTMItems.get(Integer.toString(j));
                        if(ObjItemEx.getAttribute("ITEM_ID").getObj().toString().equals(ItemID)&&j!=i) {
                            CurrentQty+=ObjItemEx.getAttribute("QTY").getVal();
                        }
                    }
                 
                 
                    if((OnHandQty-CurrentQty)<MinQty && !AllowExcess) {
                        LastError="Cannot deduct stock for item "+ItemID+". Stock level reached at minimum stock level";
                        return false; //Temporaritly suppressed
                    }
                }
                else {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                 
                    if((OnHandQty-CurrentQty)<0 && !AllowExcess) {
                        LastError="Cannot deduct stock for item "+ItemID+". Stock level reached at zero level";//. And No MIR No. specified in the issue entry";
                        return false; //Temporarily suppressed
                    }
                }
                 */
            //===================== Stock checking over =========================//
            
            
            
            
            
            // ================== Stock Updataion ========================//
            //============================================================//
            /*
             if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colSTMItems.size();i++) {
                    clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
             
                    double TotalZeroValQty=0;
             
             
                    Qty=ObjItem.getAttribute("QTY").getVal();
                    ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    BOENo=(String)ObjItem.getAttribute("BOE_NO").getObj();
                    WareHouseID=(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                    LocationID=(String)ObjItem.getAttribute("LOCATION_ID").getObj();
             
             
                    //======== New Code of Excess Issue of Item ====================//
                    //=============================================================//
                    clsStockInfo objStock=(new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID,docDate);
                    double OnHandQty=objStock.StockQty;
                    double ExcessQty=0;
                    double AddExcess=0;
             
             
                    if(Qty>OnHandQty) {
                        ExcessQty=Qty-OnHandQty;
             
                        AddExcess=0;
             
                        //First find out the duplication of Item Code in Issue Detail
                        for(int a=1;a<=colSTMItems.size();a++) {
                            clsSTMGenItem ObjTempItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(a));
             
                            String tmpItemID=(String)ObjTempItem.getAttribute("ITEM_ID").getObj();
                            if(a!=i && tmpItemID.trim().equals(ItemID)) {
                                AddExcess=ObjTempItem.getAttribute("EXCESS_ISSUE_QTY").getVal();
                            }
                        }
                        //========================================================//
             
                        ExcessQty+=AddExcess;
                    }
             
             
                    //Actual Excess Qty for the Line of Issue
                    if(Qty>OnHandQty) {
                        ExcessQty=Qty-OnHandQty;
             
                        //Bring down the Issue Qty to On Hand Qty - Do not negate the stock //
                        Qty=OnHandQty;
             
                    }
                    else {
                        ExcessQty=0;
                    }
             
                    //Update the Excess Issue Qty.
                    ObjItem.setAttribute("EXCESS_ISSUE_QTY",ExcessQty);
                    //===================================================================//
             
             
                    if(BOENo.trim().equals("")) {
                        BOENo="X";
                    }
             
                    Statement tmpStmt;
                    tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
             
                    //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                    Statement tmpStmt1;
                    tmpStmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsTmp=tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+ItemID.trim()+"' AND  BOE_NO='"+BOENo+"' AND WAREHOUSE_ID='"+WareHouseID+"' AND LOCATION_ID='"+LocationID+"' AND LOT_NO='X'");
                    rsTmp.first();
             
                    //Check that entry exist. Else create a new record
                    if(rsTmp.getRow()<=0) {
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateString("ITEM_ID",ItemID);
                        rsTmp.updateString("LOT_NO","X");
                        rsTmp.updateString("BOE_NO",BOENo);
                        rsTmp.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                        rsTmp.updateString("WAREHOUSE_ID",WareHouseID);
                        rsTmp.updateString("LOCATION_ID",LocationID);
                        rsTmp.updateBoolean("CHANGED",true);
                        rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDate());
                        rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                        rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID);
                        rsTmp.updateDouble("ALLOCATED_QTY",0);
                        rsTmp.updateDouble("AVAILABLE_QTY",0);
                        rsTmp.updateDouble("ON_HAND_QTY",0);
                        rsTmp.updateDouble("REJECTED_QTY",0);
                        rsTmp.updateDouble("ZERO_VAL_QTY",0);
                        rsTmp.updateDouble("ZERO_ISSUED_QTY",0);
                        rsTmp.updateDouble("ZERO_RECEIPT_QTY",0);
                        rsTmp.updateDouble("ZERO_OPENING_QTY",0);
                        rsTmp.updateString("LAST_ISSUED_DATE","0000-00-00");
                        rsTmp.updateString("LAST_RECEIPT_DATE","0000-00-00");
                        rsTmp.updateDouble("TOTAL_ISSUED_QTY",0);
                        rsTmp.updateDouble("TOTAL_RECEIPT_QTY",0);
                        rsTmp.updateDouble("OPENING_RATE",0);
                        rsTmp.updateDouble("OPENING_QTY",0);
                        rsTmp.insertRow();
                    }
             
                    //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                    tmpStmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsTmp=tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+ItemID.trim()+"' AND  BOE_NO='"+BOENo+"' AND WAREHOUSE_ID='"+WareHouseID+"' AND LOCATION_ID='"+LocationID+"' AND LOT_NO='X'");
                    rsTmp.first();
             
                    // Updating tables
                    if(clsItem.getMaintainStock(EITLERPGLOBAL.gCompanyID,ItemID)) {
                        //===== New Code of Zero Value Issue Qty ----------//
                        double ActualIssQty=Qty;
                        double ZeroIssQty=0;
             
                        if(rsTmp.getRow()>0) {
                            double ZeroValQty=rsTmp.getDouble("ZERO_VAL_QTY");
             
                            if(ZeroValQty>Qty) {
                                ActualIssQty=0;
                                ZeroIssQty=Qty;
             
                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY",rsTmp.getDouble("ZERO_ISSUED_QTY")+Qty);
                                rsTmp.updateDouble("ZERO_VAL_QTY",rsTmp.getDouble("ZERO_VAL_QTY")-Qty);
             
                                TotalZeroValQty+=ZeroIssQty;
                            }
                            else {
                                ActualIssQty=Qty-ZeroValQty;
                                ZeroIssQty=Qty-ActualIssQty;
             
                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY",rsTmp.getDouble("ZERO_ISSUED_QTY")+ZeroIssQty);
                                rsTmp.updateDouble("ZERO_VAL_QTY",0);
             
                                TotalZeroValQty+=ZeroIssQty;
                            }
                        }
                        //================================================================================//
             
             
                        //Now Calculate the Actual Issue Value after Zero Val Qty deduction //
                        double Rate=ObjItem.getAttribute("RATE").getVal();
                        double IssueValue=ActualIssQty*Rate;
                        //ObjItem.setAttribute("ISSUE_VALUE",IssueValue);
                        //================================================================= //
             
                        rsTmp.updateDouble("TOTAL_ISSUED_QTY",rsTmp.getDouble("TOTAL_ISSUED_QTY")+ActualIssQty);
                        rsTmp.updateString("LAST_ISSUED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateDouble("ON_HAND_QTY",rsTmp.getDouble("ON_HAND_QTY")-Qty);
                        rsTmp.updateDouble("AVAILABLE_QTY",rsTmp.getDouble("AVAILABLE_QTY")-Qty);
                        rsTmp.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                        rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                        rsTmp.updateBoolean("CHANGED",true);
                        rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateRow();
                    }
             
                    ObjItem.setAttribute("ZERO_VAL_QTY",TotalZeroValQty);
                    colSTMItems.put(Integer.toString(i),ObjItem);
             
             
             
                }
            } */
            // ================== Stock Updataion Over ========================//
            //=================================================================//
            
            
            
            
            
            // ============ Update the Stock only after Final Approval =================== //
            if(AStatus.equals("F")) {
                int CompanyId=2;
                String dburl="";
                if(EITLERPGLOBAL.gCompanyID == 2) {
                    CompanyId = 2;
                    //dburl="jdbc:mysql://200.0.0.227:3306/DINESHMILLSA?user=root";
                    dburl=clsFinYear.getDBURL(CompanyId,EITLERPGLOBAL.getCurrentFinYear());
                    
                }
                else if(EITLERPGLOBAL.gCompanyID == 3) {
                    CompanyId = 3;
                    //dburl="jdbc:mysql://200.0.0.227:3306/DINESHMILLS?user=root";
                    dburl=clsFinYear.getDBURL(CompanyId,EITLERPGLOBAL.getCurrentFinYear());
                }
                //-------- First Update the stock -------------//
                for(int i=1;i<=colSTMItems.size();i++) {
                    clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                    
                    Qty=ObjItem.getAttribute("QTY").getDouble();
                    ItemID=ObjItem.getAttribute("ITEM_ID").getString();
                    String Reqno=ObjItem.getAttribute("MIR_NO").getString();
                    int ReqSrno=ObjItem.getAttribute("MIR_SR_NO").getInt();
                    
                    //String strQry="SELECT FIRST_ITEM_ID FROM D_COM_ITEM_MAPPING WHERE SECOND_ITEM_ID = '"+ItemID+"'";
                    //data.getStringValueFromDB(strQry,dburl);
                    ResultSet req = data.getResult("SELECT * FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID="+CompanyId+" AND STM_REQ_NO='"+Reqno.trim()+"' AND STM_REQ_SR_NO="+ReqSrno+" AND ITEM_ID='"+ItemID+"'",dburl);
                    
                    req.first();
                    
                    double IssueQty = req.getDouble("ISSUED_QTY")+Qty;
                    double RequiredQty = req.getDouble("STM_REQ_QTY") - IssueQty;
                    
                    String str = "UPDATE D_INV_STM_REQ_DETAIL SET ISSUED_QTY="+Qty+",BAL_QTY="+RequiredQty+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyId+" AND STM_REQ_NO='"+Reqno.trim()+"' AND STM_REQ_SR_NO="+ReqSrno+" AND ITEM_ID='"+ItemID+"' ";
                    data.Execute(str,dburl);
                    str="UPDATE D_INV_STM_REQ_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyId+" AND STM_REQ_NO='"+Reqno.trim()+"'";
                    data.Execute(str,dburl);
                    
                }
                
                
                //-------- END Update the stock -------------//
            } // End of First Approval Status condition
            
            
            
            //--------- Generate New GRN No. ------------
            setAttribute("STM_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,16, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
            rsResultSet.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            
            StmDate=(String)getAttribute("STM_DATE").getObj();
            
            rsResultSet.updateInt("STM_TYPE",(int)getAttribute("STM_TYPE").getVal());
            rsResultSet.updateInt("FOR_DEPT_ID",(int)getAttribute("FOR_DEPT_ID").getVal());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsResultSet.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsResultSet.updateInt("TRANSFER_TO_UNIT",(int)getAttribute("TRANSFER_TO_UNIT").getVal());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            
            //Now Custom Columns
            rsResultSet.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
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
            rsHistory.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
            rsHistory.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            rsHistory.updateInt("STM_TYPE",(int)getAttribute("STM_TYPE").getVal());
            rsHistory.updateInt("FOR_DEPT_ID",(int)getAttribute("FOR_DEPT_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsHistory.updateInt("TRANSFER_TO_UNIT",(int)getAttribute("TRANSFER_TO_UNIT").getVal());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            //====== Now turn of Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_STM_DETAIL WHERE STM_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("STM_TYPE",1);// 1 - General
                rsItem.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("RECEIVED_QTY",0);
                rsItem.updateDouble("ZERO_VAL_QTY",ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateInt("COST_CENTER_ID",(int)ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getDouble());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getDouble());
                rsItem.updateString("STM_DESC",(String)ObjItem.getAttribute("STM_DESC").getObj());
                rsItem.updateDouble("QTY_REQD",ObjItem.getAttribute("QTY_REQD").getVal());
                rsItem.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateInt("INDENT_COMPANY_ID",(int)ObjItem.getAttribute("INDENT_COMPANY_ID").getVal());
                rsItem.updateInt("INDENT_COMPANY_YEAR",(int)ObjItem.getAttribute("INDENT_COMPANY_YEAR").getVal());
                // MUFFY DELETE 4 MIR LINES
                rsItem.updateString("MIR_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsItem.updateInt("MIR_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsItem.updateInt("MIR_TYPE",(int)ObjItem.getAttribute("MIR_TYPE").getVal());
                
                rsItem.updateString("STM_REQ_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsItem.updateInt("STM_REQ_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("STM_TYPE",1);// 1 - General
                rsHDetail.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RECEIVED_QTY",0);
                rsHDetail.updateDouble("ZERO_VAL_QTY",ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("COST_CENTER_ID",(int)ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getDouble());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getDouble());
                rsHDetail.updateString("STM_DESC",(String)ObjItem.getAttribute("STM_DESC").getObj());
                rsHDetail.updateDouble("QTY_REQD",ObjItem.getAttribute("QTY_REQD").getVal());
                rsHDetail.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateInt("INDENT_COMPANY_ID",(int)ObjItem.getAttribute("INDENT_COMPANY_ID").getVal());
                rsHDetail.updateInt("INDENT_COMPANY_YEAR",(int)ObjItem.getAttribute("INDENT_COMPANY_YEAR").getVal());
                
                // BY MUFFY DELETE 3 LINES
                rsHDetail.updateString("MIR_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE",(int)ObjItem.getAttribute("MIR_TYPE").getVal());
                
                
                rsHDetail.updateString("STM_REQ_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("STM_REQ_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                
                
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
                //===================================================================//
                //====================== PROCESS WEIGHTED AVERAGE   =================//
                //===================================================================//
                //======= Find the last cut-off date stock entry =================//
                
                String Qry = "SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='" + StmDate + "' ORDER BY ENTRY_DATE DESC";
                rsTmp=data.getResult(Qry);
                rsTmp.first();
                String StockEntryDate="";
                if(rsTmp.getRow()>0) {
                    
                    StockEntryDate=rsTmp.getString("ENTRY_DATE");
                }
                String ToDate=(String)getAttribute("STM_DATE").getObj();
                clsItemStock objItemStock=new clsItemStock();
                double Rate = objItemStock.ProcessLedger(StockEntryDate, ToDate,ObjItem.getAttribute("ITEM_ID").getString());
                double Amount =EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal()*Rate,3);
                Qry = "UPDATE D_INV_STM_DETAIL SET RATE='" + Rate  + "',TOTAL_AMOUNT='"+ Amount +"',NET_AMOUNT='"+ Amount +"' WHERE STM_NO = '"+(String)getAttribute("STM_NO").getObj()+"' AND ITEM_ID = '" + ObjItem.getAttribute("ITEM_ID").getString() + "' AND SR_NO = '" + ObjItem.getAttribute("SR_NO").getString() + "' ";
                boolean test = data.Execute(Qry);
                
                //======================END WEIGHTED AVG. PROCESS ===================//
                
                
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=16; //STM Raw Material
            ObjFlow.DocNo=(String)getAttribute("STM_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_STM_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="STM_NO";
            
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
            
            
            
            
            
            
            
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean Update() {
        Connection tmpConn;
        
        Statement stItem,stStock,stTmp,stItemMaster,stHistory,stHDetail;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsHistory,rsHDetail;
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="",LocationID="",IndentNo="",strSQL="",BOEDate="";
        int CompanyID=0,IndentSrNo=0;
        double RejectedQty=0,Qty=0,ToleranceLimit=0;
        String StmDate="";
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("STM_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_STM_HEADER_H WHERE STM_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_STM_DETAIL_H WHERE STM_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("STM_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_STM_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(STM_NO)='"+theDocNo+"' AND STM_TYPE=1");
            //rsHeader.first();
            
            String STMNo=(String)getAttribute("STM_NO").getObj();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //=========== Check the Quantities entered against Indent============= //
            for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                int IndentCompany=(int)ObjItem.getAttribute("INDENT_COMPANY_ID").getVal();
                int IndentCompanyYear=(int)ObjItem.getAttribute("INDENT_COMPANY_YEAR").getVal();
                int STMSrNo=(int)ObjItem.getAttribute("SR_NO").getVal();
                
                //ToleranceLimit=clsItem.getToleranceLimit(CompanyID,(String)ObjItem.getAttribute("ITEM_ID").getObj());
                tmpConn=data.getConn(clsFinYear.getDBURL(IndentCompany,IndentCompanyYear));
                
                double IndentQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
                
                if((!IndentNo.trim().equals(""))&&(IndentSrNo>0)) //MIR No. entered
                {
                    //Get the  MIR Qty.
                    stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        IndentQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo+"' AND INDENT_SR_NO="+IndentSrNo+" AND INDENT_COMPANY_ID="+IndentCompany+" AND INDENT_COMPANY_YEAR="+IndentCompanyYear+" AND NOT(STM_NO='"+STMNo+"' AND SR_NO="+STMSrNo+" AND STM_TYPE=1) AND STM_NO NOT IN(SELECT STM_NO FROM D_INV_STM_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > IndentQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Indent No. "+IndentNo+" Sr. No. "+IndentSrNo+" qty "+IndentQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            //============== Indent Checking Completed ====================//
            
            
            
            //========================= Stock Checking ===============================//
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String docDate=(String)getAttribute("STM_DATE").getObj();
            
            for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                
                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                
                clsStockInfo objStock=(new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID,EITLERPGLOBAL.getCurrentDateDB());
                double OnHandQty=objStock.StockQty;
                double MinQty=clsItem.getMinQty(EITLERPGLOBAL.gCompanyID,ItemID);
                
                
                //========= New Code of Allow Excess Issue ===========//
                boolean AllowExcess=false;
                //====================================================//
                
                // BY MUFFY
                /*
                if(MinQty>0)  //Minimum level maintained
                {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                 
                    //Get the sum of all repeated item codes
                    for(int j=1;i<=colSTMItems.size();i++) {
                        clsSTMGenItem ObjItemEx=(clsSTMGenItem)colSTMItems.get(Integer.toString(j));
                        if(ObjItemEx.getAttribute("ITEM_ID").getObj().toString().equals(ItemID)&&j!=i) {
                            CurrentQty+=ObjItemEx.getAttribute("QTY").getVal();
                        }
                    }
                 
                 
                    if((OnHandQty-CurrentQty)<MinQty && !AllowExcess) {
                        LastError="Cannot deduct stock for item "+ItemID+". Stock level reached at minimum stock level";
                        return false; //Temporaritly suppressed
                    }
                }
                else {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                 
                    if((OnHandQty-CurrentQty)<0 && !AllowExcess) {
                        LastError="Cannot deduct stock for item "+ItemID+". Stock level reached at zero level. And No MIR No. specified in the issue entry";
                        return false; //Temporarily suppressed
                    }
                }
                 **/
            }
            //===================== Stock checking over =========================//
            
            
            
            
            
            // ================== Stock Updataion ========================//
            //============================================================//
            /*if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colSTMItems.size();i++) {
                    clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
             
                    double TotalZeroValQty=0;
             
             
                    Qty=ObjItem.getAttribute("QTY").getVal();
                    ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    BOENo=(String)ObjItem.getAttribute("BOE_NO").getObj();
                    WareHouseID=(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                    LocationID=(String)ObjItem.getAttribute("LOCATION_ID").getObj();
             
             
                    //======== New Code of Excess Issue of Item ====================//
                    //=============================================================//
                    clsStockInfo objStock=(new clsItemStock()).getOnHandQtyOn(EITLERPGLOBAL.gCompanyID, ItemID,docDate);
                    double OnHandQty=objStock.StockQty;
                    double ExcessQty=0;
                    double AddExcess=0;
             
             
                    if(Qty>OnHandQty) {
                        ExcessQty=Qty-OnHandQty;
             
                        AddExcess=0;
             
                        //First find out the duplication of Item Code in Issue Detail
                        for(int a=1;a<=colSTMItems.size();a++) {
                            clsSTMGenItem ObjTempItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(a));
             
                            String tmpItemID=(String)ObjTempItem.getAttribute("ITEM_ID").getObj();
                            if(a!=i && tmpItemID.trim().equals(ItemID)) {
                                AddExcess=ObjTempItem.getAttribute("EXCESS_ISSUE_QTY").getVal();
                            }
                        }
                        //========================================================//
             
                        ExcessQty+=AddExcess;
                    }
             
             
                    //Actual Excess Qty for the Line of Issue
                    if(Qty>OnHandQty) {
                        ExcessQty=Qty-OnHandQty;
             
                        //Bring down the Issue Qty to On Hand Qty - Do not negate the stock //
                        Qty=OnHandQty;
             
                    }
                    else {
                        ExcessQty=0;
                    }
             
                    //Update the Excess Issue Qty.
                    ObjItem.setAttribute("EXCESS_ISSUE_QTY",ExcessQty);
                    //===================================================================//
             
             
                    if(BOENo.trim().equals("")) {
                        BOENo="X";
                    }
             
                    Statement tmpStmt;
                    tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
             
                    //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                    Statement tmpStmt1;
                    tmpStmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsTmp=tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+ItemID.trim()+"' AND  BOE_NO='"+BOENo+"' AND WAREHOUSE_ID='"+WareHouseID+"' AND LOCATION_ID='"+LocationID+"' AND LOT_NO='X'");
                    rsTmp.first();
             
                    //Check that entry exist. Else create a new record
                    if(rsTmp.getRow()<=0) {
                        rsTmp.moveToInsertRow();
                        rsTmp.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsTmp.updateString("ITEM_ID",ItemID);
                        rsTmp.updateString("LOT_NO","X");
                        rsTmp.updateString("BOE_NO",BOENo);
                        rsTmp.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                        rsTmp.updateString("WAREHOUSE_ID",WareHouseID);
                        rsTmp.updateString("LOCATION_ID",LocationID);
                        rsTmp.updateBoolean("CHANGED",true);
                        rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDate());
                        rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                        rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID);
                        rsTmp.updateDouble("ALLOCATED_QTY",0);
                        rsTmp.updateDouble("AVAILABLE_QTY",0);
                        rsTmp.updateDouble("ON_HAND_QTY",0);
                        rsTmp.updateDouble("REJECTED_QTY",0);
                        rsTmp.updateDouble("ZERO_VAL_QTY",0);
                        rsTmp.updateDouble("ZERO_ISSUED_QTY",0);
                        rsTmp.updateDouble("ZERO_RECEIPT_QTY",0);
                        rsTmp.updateDouble("ZERO_OPENING_QTY",0);
                        rsTmp.updateString("LAST_ISSUED_DATE","0000-00-00");
                        rsTmp.updateString("LAST_RECEIPT_DATE","0000-00-00");
                        rsTmp.updateDouble("TOTAL_ISSUED_QTY",0);
                        rsTmp.updateDouble("TOTAL_RECEIPT_QTY",0);
                        rsTmp.updateDouble("OPENING_RATE",0);
                        rsTmp.updateDouble("OPENING_QTY",0);
                        rsTmp.insertRow();
                    }
             
                    //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                    tmpStmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsTmp=tmpStmt1.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+ItemID.trim()+"' AND  BOE_NO='"+BOENo+"' AND WAREHOUSE_ID='"+WareHouseID+"' AND LOCATION_ID='"+LocationID+"' AND LOT_NO='X'");
                    rsTmp.first();
             
                    // Updating tables
                    if(clsItem.getMaintainStock(EITLERPGLOBAL.gCompanyID,ItemID)) {
                        //===== New Code of Zero Value Issue Qty ----------//
                        double ActualIssQty=Qty;
                        double ZeroIssQty=0;
             
                        if(rsTmp.getRow()>0) {
                            double ZeroValQty=rsTmp.getDouble("ZERO_VAL_QTY");
             
                            if(ZeroValQty>Qty) {
                                ActualIssQty=0;
                                ZeroIssQty=Qty;
             
                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY",rsTmp.getDouble("ZERO_ISSUED_QTY")+Qty);
                                rsTmp.updateDouble("ZERO_VAL_QTY",rsTmp.getDouble("ZERO_VAL_QTY")-Qty);
             
                                TotalZeroValQty+=ZeroIssQty;
                            }
                            else {
                                ActualIssQty=Qty-ZeroValQty;
                                ZeroIssQty=Qty-ActualIssQty;
             
                                //Update the Zero Value Qty.
                                rsTmp.updateDouble("ZERO_ISSUED_QTY",rsTmp.getDouble("ZERO_ISSUED_QTY")+ZeroIssQty);
                                rsTmp.updateDouble("ZERO_VAL_QTY",0);
             
                                TotalZeroValQty+=ZeroIssQty;
                            }
                        }
                        //================================================================================//
             
             
                        //Now Calculate the Actual Issue Value after Zero Val Qty deduction //
                        double Rate=ObjItem.getAttribute("RATE").getVal();
                        double IssueValue=ActualIssQty*Rate;
                        ObjItem.setAttribute("ISSUE_VALUE",IssueValue);
                        //================================================================= //
             
                        rsTmp.updateDouble("TOTAL_ISSUED_QTY",rsTmp.getDouble("TOTAL_ISSUED_QTY")+ActualIssQty);
                        rsTmp.updateString("LAST_ISSUED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateDouble("ON_HAND_QTY",rsTmp.getDouble("ON_HAND_QTY")-Qty);
                        rsTmp.updateDouble("AVAILABLE_QTY",rsTmp.getDouble("AVAILABLE_QTY")-Qty);
                        rsTmp.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                        rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                        rsTmp.updateBoolean("CHANGED",true);
                        rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsTmp.updateRow();
                    }
             
                    ObjItem.setAttribute("ZERO_VAL_QTY",TotalZeroValQty);
                    colSTMItems.put(Integer.toString(i),ObjItem);
                }
            }*/
            // ================== Stock Updataion Over ========================//
            //=================================================================//
            
            
            if(AStatus.equals("F")) {
                String ToDate = getAttribute("STM_DATE").getString();
                String FromDate =data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+ToDate+"' ORDER BY ENTRY_DATE DESC LIMIT 1");
                
                for(int i=1;i<=colSTMItems.size();i++) {
                    clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                    Qty=ObjItem.getAttribute("QTY").getDouble();
                    ItemID=ObjItem.getAttribute("ITEM_ID").getString();
                    
                    double ClosingRate = new clsItemStock().ProcessLedger(FromDate, ToDate,ItemID);
                    ObjItem.setAttribute("RATE",EITLERPGLOBAL.round(ClosingRate, 3));
                    ObjItem.setAttribute("NET_AMOUNT", EITLERPGLOBAL.round(ClosingRate*Qty,3));
                    ObjItem.setAttribute("TOTAL_AMOUNT", EITLERPGLOBAL.round(ClosingRate*Qty,3));
                    colSTMItems.put(Integer.toString(i),ObjItem);
                }
            }
            
            // ============ Update the Stock only after Final Approval =================== //
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                int CompanyId=2;
                String dburl="";
                if(EITLERPGLOBAL.gCompanyID == 2) {
                    CompanyId = 2;
                    //dburl="jdbc:mysql://200.0.0.227:3306/DINESHMILLSA?user=root";
                    dburl=clsFinYear.getDBURL(CompanyId,EITLERPGLOBAL.getCurrentFinYear());
                    
                }
                else if(EITLERPGLOBAL.gCompanyID == 3) {
                    CompanyId = 3;
                    //dburl="jdbc:mysql://200.0.0.227:3306/DINESHMILLS?user=root";
                    dburl=clsFinYear.getDBURL(CompanyId,EITLERPGLOBAL.getCurrentFinYear());
                }
                //-------- First Update the stock -------------//
                for(int i=1;i<=colSTMItems.size();i++) {
                    clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                    
                    Qty=ObjItem.getAttribute("QTY").getDouble();
                    ItemID=ObjItem.getAttribute("ITEM_ID").getString();
                    String Reqno=ObjItem.getAttribute("MIR_NO").getString();
                    int ReqSrno=ObjItem.getAttribute("MIR_SR_NO").getInt();
                    
                    //String strQry="SELECT FIRST_ITEM_ID FROM D_COM_ITEM_MAPPING WHERE SECOND_ITEM_ID = '"+ItemID+"'";
                    //data.getStringValueFromDB(strQry,dburl);
                    ResultSet req = data.getResult("SELECT * FROM D_INV_STM_REQ_DETAIL WHERE COMPANY_ID="+CompanyId+" AND STM_REQ_NO='"+Reqno.trim()+"' AND STM_REQ_SR_NO="+ReqSrno+" AND ITEM_ID='"+ItemID+"'",dburl);
                    
                    req.first();
                    
                    double IssueQty = req.getDouble("ISSUED_QTY")+Qty;
                    double RequiredQty = req.getDouble("STM_REQ_QTY") - IssueQty;
                    
                    String str = "UPDATE D_INV_STM_REQ_DETAIL SET ISSUED_QTY="+Qty+",BAL_QTY="+RequiredQty+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyId+" AND STM_REQ_NO='"+Reqno.trim()+"' AND STM_REQ_SR_NO="+ReqSrno+" AND ITEM_ID='"+ItemID+"' ";
                    data.Execute(str,dburl);
                    str="UPDATE D_INV_STM_REQ_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyId+" AND STM_REQ_NO='"+Reqno.trim()+"'";
                    data.Execute(str,dburl);
                    
                }
                //=============Updation of stock completed=========================//
            } // End of First Approval Status condition
            
            StmDate=(String)getAttribute("STM_DATE").getObj();
            rsResultSet.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            rsResultSet.updateInt("FOR_DEPT_ID",(int)getAttribute("FOR_DEPT_ID").getVal());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsResultSet.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsResultSet.updateInt("TRANSFER_TO_UNIT",(int)getAttribute("TRANSFER_TO_UNIT").getVal());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            
            //Now Custom Columns
            rsResultSet.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_STM_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_NO='"+(String)getAttribute("STM_NO").getObj()+"' AND STM_TYPE=1");
            RevNo++;
            String RevDocNo=(String)getAttribute("STM_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
            rsHistory.updateString("STM_DATE",(String)getAttribute("STM_DATE").getObj());
            rsHistory.updateInt("STM_TYPE",(int)getAttribute("STM_TYPE").getVal());
            rsHistory.updateInt("FOR_DEPT_ID",(int)getAttribute("FOR_DEPT_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsHistory.updateInt("TRANSFER_TO_UNIT",(int)getAttribute("TRANSFER_TO_UNIT").getVal());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("COLUMN_1_ID",(int)getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER",getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT",getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID",(int)getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER",getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT",getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID",(int)getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER",getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT",getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID",(int)getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER",getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT",getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID",(int)getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER",getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT",getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID",(int)getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER",getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT",getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID",(int)getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER",getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT",getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID",(int)getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER",getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT",getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID",(int)getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER",getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT",getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID",(int)getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER",getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT",getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            //Delete Previous Records
            data.Execute("DELETE FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_NO='"+STMNo+"' AND STM_TYPE=1");
            data.Execute("DELETE FROM D_INV_STM_LOT WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_NO='"+STMNo+"' AND STM_TYPE=1");
            
            //====== Now turn of Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_STM_DETAIL WHERE STM_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMGenItem ObjItem=(clsSTMGenItem)colSTMItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("STM_TYPE",1);// 1 - General
                rsItem.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("RECEIVED_QTY",0);
                rsItem.updateDouble("ZERO_VAL_QTY",ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateInt("COST_CENTER_ID",(int)ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("STM_DESC",(String)ObjItem.getAttribute("STM_DESC").getObj());
                rsItem.updateDouble("QTY_REQD",ObjItem.getAttribute("QTY_REQD").getVal());
                rsItem.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateInt("INDENT_COMPANY_ID",(int)ObjItem.getAttribute("INDENT_COMPANY_ID").getVal());
                rsItem.updateInt("INDENT_COMPANY_YEAR",(int)ObjItem.getAttribute("INDENT_COMPANY_YEAR").getVal());
                
                // BY MUFFY DELETE 3 LINES
                rsItem.updateString("MIR_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsItem.updateInt("MIR_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsItem.updateInt("MIR_TYPE",(int)ObjItem.getAttribute("MIR_TYPE").getVal());
                
                rsItem.updateString("STM_REQ_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsItem.updateInt("STM_REQ_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("STM_NO",(String)getAttribute("STM_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("STM_TYPE",1);// 1 - General
                rsHDetail.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("ZERO_VAL_QTY",ObjItem.getAttribute("ZERO_VAL_QTY").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("COST_CENTER_ID",(int)ObjItem.getAttribute("COST_CENTER_ID").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("STM_DESC",(String)ObjItem.getAttribute("STM_DESC").getObj());
                rsHDetail.updateDouble("QTY_REQD",ObjItem.getAttribute("QTY_REQD").getVal());
                rsHDetail.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateInt("INDENT_COMPANY_ID",(int)ObjItem.getAttribute("INDENT_COMPANY_ID").getVal());
                rsHDetail.updateInt("INDENT_COMPANY_YEAR",(int)ObjItem.getAttribute("INDENT_COMPANY_YEAR").getVal());
                
                // BY MUFFY DELETE 3 LINES
                rsHDetail.updateString("MIR_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE",(int)ObjItem.getAttribute("MIR_TYPE").getVal());
                
                rsHDetail.updateString("STM_REQ_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("STM_REQ_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateInt("COLUMN_1_ID",(int)ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA",(String)ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER",ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT",ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION",(String)ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID",(int)ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA",(String)ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER",ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT",ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION",(String)ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID",(int)ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA",(String)ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER",ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT",ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION",(String)ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID",(int)ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA",(String)ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER",ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT",ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION",(String)ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID",(int)ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA",(String)ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER",ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT",ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION",(String)ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID",(int)ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA",(String)ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER",ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT",ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION",(String)ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID",(int)ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA",(String)ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER",ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT",ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION",(String)ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID",(int)ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA",(String)ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER",ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT",ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION",(String)ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID",(int)ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA",(String)ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER",ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT",ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION",(String)ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID",(int)ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA",(String)ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER",ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT",ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION",(String)ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
                //===================================================================//
                //====================== PROCESS WEIGHTED AVERAGE   =================//
                //===================================================================//
                //======= Find the last cut-off date stock entry =================//
                
                String Qry = "SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='" + StmDate + "' ORDER BY ENTRY_DATE DESC";
                rsTmp=data.getResult(Qry);
                rsTmp.first();
                String StockEntryDate="";
                if(rsTmp.getRow()>0) {
                    
                    StockEntryDate=rsTmp.getString("ENTRY_DATE");
                }
                String ToDate=(String)getAttribute("STM_DATE").getObj();
                clsItemStock objItemStock=new clsItemStock();
                double Rate = objItemStock.ProcessLedger(StockEntryDate, ToDate,ObjItem.getAttribute("ITEM_ID").getString());
                double Amount =EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal()*Rate,3);
                Qry = "UPDATE D_INV_STM_DETAIL SET RATE='" + Rate  + "',TOTAL_AMOUNT='"+ Amount +"',NET_AMOUNT='"+ Amount +"' WHERE STM_NO = '"+(String)getAttribute("STM_NO").getObj()+"' AND ITEM_ID = '" + ObjItem.getAttribute("ITEM_ID").getString() + "' AND SR_NO = '" + ObjItem.getAttribute("SR_NO").getString() + "'  ";
                boolean test = data.Execute(Qry);
                
                //======================END WEIGHTED AVG. PROCESS ===================//
                
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=16; //STM Raw Material
            ObjFlow.DocNo=(String)getAttribute("STM_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_STM_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="STM_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_STM_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND STM_NO='"+ObjFlow.DocNo+"' AND STM_TYPE=1");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=16 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            //--------- Approval Flow Update complete -----------
            
            
            
            
            
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    //Updates current record
    
    
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("STM_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                //Give reverse effect to Stock
                //RevertStockEffect();
                
                data.Execute("DELETE FROM D_INV_STM_HEADER WHERE COMPANY_ID="+lCompanyID+" AND STM_NO='"+lDocNo.trim()+"' AND STM_TYPE=1");
                data.Execute("DELETE FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND STM_NO='"+lDocNo.trim()+"' AND STM_TYPE=1");
                data.Execute("DELETE FROM D_INV_STM_LOT WHERE COMPANY_ID="+lCompanyID+" STM_NO='"+lDocNo.trim()+"' AND STM_TYPE=1");
                
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
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND STM_NO='"+pDocNo+"' AND STM_TYPE=1";
        clsSTMGen ObjSTM = new clsSTMGen();
        ObjSTM.Filter(strCondition,pCompanyID);
        return ObjSTM;
    }
    
    
    public Object getObject(int pCompanyID,String pDocNo,String pURL) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND STM_NO='"+pDocNo+"' AND STM_TYPE=1";
        clsSTMGen ObjSTM = new clsSTMGen();
        ObjSTM.Filter(strCondition,pCompanyID,pURL);
        return ObjSTM;
    }
    
    public Object getObject(int pCompanyID,String pDocNo,int pType) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND STM_NO='"+pDocNo+"' AND STM_TYPE="+pType;
        clsSTMGen ObjSTM = new clsSTMGen();
        ObjSTM.Filter(strCondition,pCompanyID,pType);
        return ObjSTM;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_STM_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_STM_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND STM_TYPE=1 AND STM_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND STM_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY STM_NO";
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
    
    
    public boolean Filter(String pCondition,int pCompanyID,String pURL) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_STM_HEADER " + pCondition ;
            Conn=data.getConn(pURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_STM_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND STM_TYPE=1 AND STM_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND STM_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY STM_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                rsResultSet.first();
                setData(pURL);
                return false;
            }
            else {
                Ready=true;
                rsResultSet.first();
                setData(pURL);
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID,int pType) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_STM_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_STM_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND STM_TYPE="+pType+" AND STM_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND STM_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY STM_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                MoveLast();
                setData(pType);
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
        ResultSet rsItem,rsLot,rsTmp;
        String STMNo="";
        int CompanyID=0,ItemCounter=0,LotCounter=0,SrNo=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("STM_NO",rsResultSet.getString("STM_NO"));
            setAttribute("STM_DATE",rsResultSet.getString("STM_DATE"));
            setAttribute("STM_TYPE",rsResultSet.getInt("STM_TYPE"));
            setAttribute("FOR_DEPT_ID",rsResultSet.getInt("FOR_DEPT_ID"));
            setAttribute("GATEPASS_NO",rsResultSet.getString("GATEPASS_NO"));
            setAttribute("MODE_TRANSPORT",rsResultSet.getInt("MODE_TRANSPORT"));
            setAttribute("TRANSPORTER",rsResultSet.getInt("TRANSPORTER"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("GROSS_AMOUNT",rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("TRANSFER_TO_UNIT",rsResultSet.getInt("TRANSFER_TO_UNIT"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("COLUMN_1_ID",rsResultSet.getInt("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA",rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER",rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT",rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION",rsResultSet.getString("COLUMN_1_CAPTION"));
            setAttribute("COLUMN_2_ID",rsResultSet.getInt("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA",rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER",rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT",rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION",rsResultSet.getString("COLUMN_2_CAPTION"));
            setAttribute("COLUMN_3_ID",rsResultSet.getInt("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA",rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER",rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT",rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION",rsResultSet.getString("COLUMN_3_CAPTION"));
            setAttribute("COLUMN_4_ID",rsResultSet.getInt("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA",rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER",rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT",rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION",rsResultSet.getString("COLUMN_4_CAPTION"));
            setAttribute("COLUMN_5_ID",rsResultSet.getInt("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA",rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER",rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT",rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION",rsResultSet.getString("COLUMN_5_CAPTION"));
            setAttribute("COLUMN_6_ID",rsResultSet.getInt("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA",rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER",rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT",rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION",rsResultSet.getString("COLUMN_6_CAPTION"));
            setAttribute("COLUMN_7_ID",rsResultSet.getInt("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA",rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER",rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT",rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION",rsResultSet.getString("COLUMN_7_CAPTION"));
            setAttribute("COLUMN_8_ID",rsResultSet.getInt("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA",rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER",rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT",rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION",rsResultSet.getString("COLUMN_8_CAPTION"));
            setAttribute("COLUMN_9_ID",rsResultSet.getInt("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA",rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER",rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT",rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION",rsResultSet.getString("COLUMN_9_CAPTION"));
            setAttribute("COLUMN_10_ID",rsResultSet.getInt("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA",rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER",rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT",rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION",rsResultSet.getString("COLUMN_10_CAPTION"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            
            colSTMItems.clear(); //Clear existing data
            
            STMNo=(String)getAttribute("STM_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            if(HistoryView) {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_STM_DETAIL_H WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND STM_TYPE=1 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND STM_TYPE=1 ORDER BY SR_NO");
            }
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsSTMGenItem ObjItem=new clsSTMGenItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("STM_NO",rsItem.getString("STM_NO"));
                ObjItem.setAttribute("STM_TYPE",rsItem.getInt("STM_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("QTY_REQD",rsItem.getDouble("QTY_REQD"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("COST_CENTER_ID",rsItem.getInt("COST_CENTER_ID"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("GROSS_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("STM_DESC",rsItem.getString("STM_DESC"));
                ObjItem.setAttribute("INDENT_NO",rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO",rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("INDENT_COMPANY_ID",rsItem.getInt("INDENT_COMPANY_ID"));
                ObjItem.setAttribute("INDENT_COMPANY_YEAR",rsItem.getInt("INDENT_COMPANY_YEAR"));
                ObjItem.setAttribute("MIR_NO",rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("MIR_SR_NO",rsItem.getInt("MIR_SR_NO"));
                ObjItem.setAttribute("MIR_TYPE",rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
                ObjItem.setAttribute("RECEIVED_QTY",rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("ZERO_VAL_QTY",rsItem.getDouble("ZERO_VAL_QTY"));
                ObjItem.setAttribute("CREATED_BY",rsItem.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsItem.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsItem.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsItem.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("COLUMN_1_ID",rsItem.getInt("COLUMN_1_ID"));
                ObjItem.setAttribute("COLUMN_1_FORMULA",rsItem.getString("COLUMN_1_FORMULA"));
                ObjItem.setAttribute("COLUMN_1_PER",rsItem.getDouble("COLUMN_1_PER"));
                ObjItem.setAttribute("COLUMN_1_AMT",rsItem.getDouble("COLUMN_1_AMT"));
                ObjItem.setAttribute("COLUMN_1_CAPTION",rsItem.getString("COLUMN_1_CAPTION"));
                ObjItem.setAttribute("COLUMN_2_ID",rsItem.getInt("COLUMN_2_ID"));
                ObjItem.setAttribute("COLUMN_2_FORMULA",rsItem.getString("COLUMN_2_FORMULA"));
                ObjItem.setAttribute("COLUMN_2_PER",rsItem.getDouble("COLUMN_2_PER"));
                ObjItem.setAttribute("COLUMN_2_AMT",rsItem.getDouble("COLUMN_2_AMT"));
                ObjItem.setAttribute("COLUMN_2_CAPTION",rsItem.getString("COLUMN_2_CAPTION"));
                ObjItem.setAttribute("COLUMN_3_ID",rsItem.getInt("COLUMN_3_ID"));
                ObjItem.setAttribute("COLUMN_3_FORMULA",rsItem.getString("COLUMN_3_FORMULA"));
                ObjItem.setAttribute("COLUMN_3_PER",rsItem.getDouble("COLUMN_3_PER"));
                ObjItem.setAttribute("COLUMN_3_AMT",rsItem.getDouble("COLUMN_3_AMT"));
                ObjItem.setAttribute("COLUMN_3_CAPTION",rsItem.getString("COLUMN_3_CAPTION"));
                ObjItem.setAttribute("COLUMN_4_ID",rsItem.getInt("COLUMN_4_ID"));
                ObjItem.setAttribute("COLUMN_4_FORMULA",rsItem.getString("COLUMN_4_FORMULA"));
                ObjItem.setAttribute("COLUMN_4_PER",rsItem.getDouble("COLUMN_4_PER"));
                ObjItem.setAttribute("COLUMN_4_AMT",rsItem.getDouble("COLUMN_4_AMT"));
                ObjItem.setAttribute("COLUMN_4_CAPTION",rsItem.getString("COLUMN_4_CAPTION"));
                ObjItem.setAttribute("COLUMN_5_ID",rsItem.getInt("COLUMN_5_ID"));
                ObjItem.setAttribute("COLUMN_5_FORMULA",rsItem.getString("COLUMN_5_FORMULA"));
                ObjItem.setAttribute("COLUMN_5_PER",rsItem.getDouble("COLUMN_5_PER"));
                ObjItem.setAttribute("COLUMN_5_AMT",rsItem.getDouble("COLUMN_5_AMT"));
                ObjItem.setAttribute("COLUMN_5_CAPTION",rsItem.getString("COLUMN_5_CAPTION"));
                ObjItem.setAttribute("COLUMN_6_ID",rsItem.getInt("COLUMN_6_ID"));
                ObjItem.setAttribute("COLUMN_6_FORMULA",rsItem.getString("COLUMN_6_FORMULA"));
                ObjItem.setAttribute("COLUMN_6_PER",rsItem.getDouble("COLUMN_6_PER"));
                ObjItem.setAttribute("COLUMN_6_AMT",rsItem.getDouble("COLUMN_6_AMT"));
                ObjItem.setAttribute("COLUMN_6_CAPTION",rsItem.getString("COLUMN_6_CAPTION"));
                ObjItem.setAttribute("COLUMN_7_ID",rsItem.getInt("COLUMN_7_ID"));
                ObjItem.setAttribute("COLUMN_7_FORMULA",rsItem.getString("COLUMN_7_FORMULA"));
                ObjItem.setAttribute("COLUMN_7_PER",rsItem.getDouble("COLUMN_7_PER"));
                ObjItem.setAttribute("COLUMN_7_AMT",rsItem.getDouble("COLUMN_7_AMT"));
                ObjItem.setAttribute("COLUMN_7_CAPTION",rsItem.getString("COLUMN_7_CAPTION"));
                ObjItem.setAttribute("COLUMN_8_ID",rsItem.getInt("COLUMN_8_ID"));
                ObjItem.setAttribute("COLUMN_8_FORMULA",rsItem.getString("COLUMN_8_FORMULA"));
                ObjItem.setAttribute("COLUMN_8_PER",rsItem.getDouble("COLUMN_8_PER"));
                ObjItem.setAttribute("COLUMN_8_AMT",rsItem.getDouble("COLUMN_8_AMT"));
                ObjItem.setAttribute("COLUMN_8_CAPTION",rsItem.getString("COLUMN_8_CAPTION"));
                ObjItem.setAttribute("COLUMN_9_ID",rsItem.getInt("COLUMN_9_ID"));
                ObjItem.setAttribute("COLUMN_9_FORMULA",rsItem.getString("COLUMN_9_FORMULA"));
                ObjItem.setAttribute("COLUMN_9_PER",rsItem.getDouble("COLUMN_9_PER"));
                ObjItem.setAttribute("COLUMN_9_AMT",rsItem.getDouble("COLUMN_9_AMT"));
                ObjItem.setAttribute("COLUMN_9_CAPTION",rsItem.getString("COLUMN_9_CAPTION"));
                ObjItem.setAttribute("COLUMN_10_ID",rsItem.getInt("COLUMN_10_ID"));
                ObjItem.setAttribute("COLUMN_10_FORMULA",rsItem.getString("COLUMN_10_FORMULA"));
                ObjItem.setAttribute("COLUMN_10_PER",rsItem.getDouble("COLUMN_10_PER"));
                ObjItem.setAttribute("COLUMN_10_AMT",rsItem.getDouble("COLUMN_10_AMT"));
                ObjItem.setAttribute("COLUMN_10_CAPTION",rsItem.getString("COLUMN_10_CAPTION"));
                
                colSTMItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean setData(String pURL) {
        Connection tmpConn;
        Statement stItem,stTmp;
        ResultSet rsItem,rsLot,rsTmp;
        String STMNo="";
        int CompanyID=0,ItemCounter=0,LotCounter=0,SrNo=0;
        
        tmpConn=data.getConn(pURL);
        
        try {
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("STM_NO",rsResultSet.getString("STM_NO"));
            setAttribute("STM_DATE",rsResultSet.getString("STM_DATE"));
            setAttribute("STM_TYPE",rsResultSet.getInt("STM_TYPE"));
            setAttribute("FOR_DEPT_ID",rsResultSet.getInt("FOR_DEPT_ID"));
            setAttribute("GATEPASS_NO",rsResultSet.getString("GATEPASS_NO"));
            setAttribute("MODE_TRANSPORT",rsResultSet.getInt("MODE_TRANSPORT"));
            setAttribute("TRANSPORTER",rsResultSet.getInt("TRANSPORTER"));
            setAttribute("GROSS_AMOUNT",rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("TRANSFER_TO_UNIT",rsResultSet.getInt("TRANSFER_TO_UNIT"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("COLUMN_1_ID",rsResultSet.getInt("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA",rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER",rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT",rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION",rsResultSet.getString("COLUMN_1_CAPTION"));
            setAttribute("COLUMN_2_ID",rsResultSet.getInt("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA",rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER",rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT",rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION",rsResultSet.getString("COLUMN_2_CAPTION"));
            setAttribute("COLUMN_3_ID",rsResultSet.getInt("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA",rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER",rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT",rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION",rsResultSet.getString("COLUMN_3_CAPTION"));
            setAttribute("COLUMN_4_ID",rsResultSet.getInt("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA",rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER",rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT",rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION",rsResultSet.getString("COLUMN_4_CAPTION"));
            setAttribute("COLUMN_5_ID",rsResultSet.getInt("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA",rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER",rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT",rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION",rsResultSet.getString("COLUMN_5_CAPTION"));
            setAttribute("COLUMN_6_ID",rsResultSet.getInt("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA",rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER",rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT",rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION",rsResultSet.getString("COLUMN_6_CAPTION"));
            setAttribute("COLUMN_7_ID",rsResultSet.getInt("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA",rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER",rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT",rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION",rsResultSet.getString("COLUMN_7_CAPTION"));
            setAttribute("COLUMN_8_ID",rsResultSet.getInt("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA",rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER",rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT",rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION",rsResultSet.getString("COLUMN_8_CAPTION"));
            setAttribute("COLUMN_9_ID",rsResultSet.getInt("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA",rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER",rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT",rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION",rsResultSet.getString("COLUMN_9_CAPTION"));
            setAttribute("COLUMN_10_ID",rsResultSet.getInt("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA",rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER",rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT",rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION",rsResultSet.getString("COLUMN_10_CAPTION"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            
            colSTMItems.clear(); //Clear existing data
            
            STMNo=(String)getAttribute("STM_NO").getObj();
            
            stItem=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND STM_TYPE=1 ORDER BY SR_NO");
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsSTMGenItem ObjItem=new clsSTMGenItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("STM_NO",rsItem.getString("STM_NO"));
                ObjItem.setAttribute("STM_TYPE",rsItem.getInt("STM_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("QTY_REQD",rsItem.getDouble("QTY_REQD"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("COST_CENTER_ID",rsItem.getInt("COST_CENTER_ID"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("GROSS_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("STM_DESC",rsItem.getString("STM_DESC"));
                ObjItem.setAttribute("INDENT_NO",rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO",rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("INDENT_COMPANY_ID",rsItem.getInt("INDENT_COMPANY_ID"));
                ObjItem.setAttribute("INDENT_COMPANY_YEAR",rsItem.getInt("INDENT_COMPANY_YEAR"));
                ObjItem.setAttribute("MIR_NO",rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("MIR_SR_NO",rsItem.getInt("MIR_SR_NO"));
                ObjItem.setAttribute("MIR_TYPE",rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
                ObjItem.setAttribute("RECEIVED_QTY",rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("ZERO_VAL_QTY",rsItem.getDouble("ZERO_VAL_QTY"));
                ObjItem.setAttribute("CREATED_BY",rsItem.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsItem.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsItem.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsItem.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("COLUMN_1_ID",rsItem.getInt("COLUMN_1_ID"));
                ObjItem.setAttribute("COLUMN_1_FORMULA",rsItem.getString("COLUMN_1_FORMULA"));
                ObjItem.setAttribute("COLUMN_1_PER",rsItem.getDouble("COLUMN_1_PER"));
                ObjItem.setAttribute("COLUMN_1_AMT",rsItem.getDouble("COLUMN_1_AMT"));
                ObjItem.setAttribute("COLUMN_1_CAPTION",rsItem.getString("COLUMN_1_CAPTION"));
                ObjItem.setAttribute("COLUMN_2_ID",rsItem.getInt("COLUMN_2_ID"));
                ObjItem.setAttribute("COLUMN_2_FORMULA",rsItem.getString("COLUMN_2_FORMULA"));
                ObjItem.setAttribute("COLUMN_2_PER",rsItem.getDouble("COLUMN_2_PER"));
                ObjItem.setAttribute("COLUMN_2_AMT",rsItem.getDouble("COLUMN_2_AMT"));
                ObjItem.setAttribute("COLUMN_2_CAPTION",rsItem.getString("COLUMN_2_CAPTION"));
                ObjItem.setAttribute("COLUMN_3_ID",rsItem.getInt("COLUMN_3_ID"));
                ObjItem.setAttribute("COLUMN_3_FORMULA",rsItem.getString("COLUMN_3_FORMULA"));
                ObjItem.setAttribute("COLUMN_3_PER",rsItem.getDouble("COLUMN_3_PER"));
                ObjItem.setAttribute("COLUMN_3_AMT",rsItem.getDouble("COLUMN_3_AMT"));
                ObjItem.setAttribute("COLUMN_3_CAPTION",rsItem.getString("COLUMN_3_CAPTION"));
                ObjItem.setAttribute("COLUMN_4_ID",rsItem.getInt("COLUMN_4_ID"));
                ObjItem.setAttribute("COLUMN_4_FORMULA",rsItem.getString("COLUMN_4_FORMULA"));
                ObjItem.setAttribute("COLUMN_4_PER",rsItem.getDouble("COLUMN_4_PER"));
                ObjItem.setAttribute("COLUMN_4_AMT",rsItem.getDouble("COLUMN_4_AMT"));
                ObjItem.setAttribute("COLUMN_4_CAPTION",rsItem.getString("COLUMN_4_CAPTION"));
                ObjItem.setAttribute("COLUMN_5_ID",rsItem.getInt("COLUMN_5_ID"));
                ObjItem.setAttribute("COLUMN_5_FORMULA",rsItem.getString("COLUMN_5_FORMULA"));
                ObjItem.setAttribute("COLUMN_5_PER",rsItem.getDouble("COLUMN_5_PER"));
                ObjItem.setAttribute("COLUMN_5_AMT",rsItem.getDouble("COLUMN_5_AMT"));
                ObjItem.setAttribute("COLUMN_5_CAPTION",rsItem.getString("COLUMN_5_CAPTION"));
                ObjItem.setAttribute("COLUMN_6_ID",rsItem.getInt("COLUMN_6_ID"));
                ObjItem.setAttribute("COLUMN_6_FORMULA",rsItem.getString("COLUMN_6_FORMULA"));
                ObjItem.setAttribute("COLUMN_6_PER",rsItem.getDouble("COLUMN_6_PER"));
                ObjItem.setAttribute("COLUMN_6_AMT",rsItem.getDouble("COLUMN_6_AMT"));
                ObjItem.setAttribute("COLUMN_6_CAPTION",rsItem.getString("COLUMN_6_CAPTION"));
                ObjItem.setAttribute("COLUMN_7_ID",rsItem.getInt("COLUMN_7_ID"));
                ObjItem.setAttribute("COLUMN_7_FORMULA",rsItem.getString("COLUMN_7_FORMULA"));
                ObjItem.setAttribute("COLUMN_7_PER",rsItem.getDouble("COLUMN_7_PER"));
                ObjItem.setAttribute("COLUMN_7_AMT",rsItem.getDouble("COLUMN_7_AMT"));
                ObjItem.setAttribute("COLUMN_7_CAPTION",rsItem.getString("COLUMN_7_CAPTION"));
                ObjItem.setAttribute("COLUMN_8_ID",rsItem.getInt("COLUMN_8_ID"));
                ObjItem.setAttribute("COLUMN_8_FORMULA",rsItem.getString("COLUMN_8_FORMULA"));
                ObjItem.setAttribute("COLUMN_8_PER",rsItem.getDouble("COLUMN_8_PER"));
                ObjItem.setAttribute("COLUMN_8_AMT",rsItem.getDouble("COLUMN_8_AMT"));
                ObjItem.setAttribute("COLUMN_8_CAPTION",rsItem.getString("COLUMN_8_CAPTION"));
                ObjItem.setAttribute("COLUMN_9_ID",rsItem.getInt("COLUMN_9_ID"));
                ObjItem.setAttribute("COLUMN_9_FORMULA",rsItem.getString("COLUMN_9_FORMULA"));
                ObjItem.setAttribute("COLUMN_9_PER",rsItem.getDouble("COLUMN_9_PER"));
                ObjItem.setAttribute("COLUMN_9_AMT",rsItem.getDouble("COLUMN_9_AMT"));
                ObjItem.setAttribute("COLUMN_9_CAPTION",rsItem.getString("COLUMN_9_CAPTION"));
                ObjItem.setAttribute("COLUMN_10_ID",rsItem.getInt("COLUMN_10_ID"));
                ObjItem.setAttribute("COLUMN_10_FORMULA",rsItem.getString("COLUMN_10_FORMULA"));
                ObjItem.setAttribute("COLUMN_10_PER",rsItem.getDouble("COLUMN_10_PER"));
                ObjItem.setAttribute("COLUMN_10_AMT",rsItem.getDouble("COLUMN_10_AMT"));
                ObjItem.setAttribute("COLUMN_10_CAPTION",rsItem.getString("COLUMN_10_CAPTION"));
                
                colSTMItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData(int pType) {
        Statement stItem,stTmp;
        ResultSet rsItem,rsLot,rsTmp;
        String STMNo="";
        int CompanyID=0,ItemCounter=0,LotCounter=0,SrNo=0;
        
        try {
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("STM_NO",rsResultSet.getString("STM_NO"));
            setAttribute("STM_DATE",rsResultSet.getString("STM_DATE"));
            setAttribute("STM_TYPE",rsResultSet.getInt("STM_TYPE"));
            setAttribute("FOR_DEPT_ID",rsResultSet.getInt("FOR_DEPT_ID"));
            setAttribute("GATEPASS_NO",rsResultSet.getString("GATEPASS_NO"));
            setAttribute("MODE_TRANSPORT",rsResultSet.getInt("MODE_TRANSPORT"));
            setAttribute("TRANSPORTER",rsResultSet.getInt("TRANSPORTER"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("GROSS_AMOUNT",rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("TRANSFER_TO_UNIT",rsResultSet.getInt("TRANSFER_TO_UNIT"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("COLUMN_1_ID",rsResultSet.getInt("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA",rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER",rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT",rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION",rsResultSet.getString("COLUMN_1_CAPTION"));
            setAttribute("COLUMN_2_ID",rsResultSet.getInt("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA",rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER",rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT",rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION",rsResultSet.getString("COLUMN_2_CAPTION"));
            setAttribute("COLUMN_3_ID",rsResultSet.getInt("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA",rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER",rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT",rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION",rsResultSet.getString("COLUMN_3_CAPTION"));
            setAttribute("COLUMN_4_ID",rsResultSet.getInt("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA",rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER",rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT",rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION",rsResultSet.getString("COLUMN_4_CAPTION"));
            setAttribute("COLUMN_5_ID",rsResultSet.getInt("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA",rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER",rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT",rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION",rsResultSet.getString("COLUMN_5_CAPTION"));
            setAttribute("COLUMN_6_ID",rsResultSet.getInt("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA",rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER",rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT",rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION",rsResultSet.getString("COLUMN_6_CAPTION"));
            setAttribute("COLUMN_7_ID",rsResultSet.getInt("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA",rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER",rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT",rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION",rsResultSet.getString("COLUMN_7_CAPTION"));
            setAttribute("COLUMN_8_ID",rsResultSet.getInt("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA",rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER",rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT",rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION",rsResultSet.getString("COLUMN_8_CAPTION"));
            setAttribute("COLUMN_9_ID",rsResultSet.getInt("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA",rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER",rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT",rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION",rsResultSet.getString("COLUMN_9_CAPTION"));
            setAttribute("COLUMN_10_ID",rsResultSet.getInt("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA",rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER",rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT",rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION",rsResultSet.getString("COLUMN_10_CAPTION"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            
            colSTMItems.clear(); //Clear existing data
            
            STMNo=(String)getAttribute("STM_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND STM_TYPE="+pType+" ORDER BY SR_NO");
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsSTMGenItem ObjItem=new clsSTMGenItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("STM_NO",rsItem.getString("STM_NO"));
                ObjItem.setAttribute("STM_TYPE",rsItem.getInt("STM_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("QTY_REQD",rsItem.getDouble("QTY_REQD"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("COST_CENTER_ID",rsItem.getInt("COST_CENTER_ID"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("GROSS_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("STM_DESC",rsItem.getString("STM_DESC"));
                ObjItem.setAttribute("INDENT_NO",rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO",rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("INDENT_COMPANY_ID",rsItem.getInt("INDENT_COMPANY_ID"));
                ObjItem.setAttribute("INDENT_COMPANY_YEAR",rsItem.getInt("INDENT_COMPANY_YEAR"));
                ObjItem.setAttribute("MIR_NO",rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("MIR_SR_NO",rsItem.getInt("MIR_SR_NO"));
                ObjItem.setAttribute("MIR_TYPE",rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
                ObjItem.setAttribute("RECEIVED_QTY",rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("ZERO_VAL_QTY",rsItem.getDouble("ZERO_VAL_QTY"));
                ObjItem.setAttribute("CREATED_BY",rsItem.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsItem.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsItem.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsItem.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("COLUMN_1_ID",rsItem.getInt("COLUMN_1_ID"));
                ObjItem.setAttribute("COLUMN_1_FORMULA",rsItem.getString("COLUMN_1_FORMULA"));
                ObjItem.setAttribute("COLUMN_1_PER",rsItem.getDouble("COLUMN_1_PER"));
                ObjItem.setAttribute("COLUMN_1_AMT",rsItem.getDouble("COLUMN_1_AMT"));
                ObjItem.setAttribute("COLUMN_1_CAPTION",rsItem.getString("COLUMN_1_CAPTION"));
                ObjItem.setAttribute("COLUMN_2_ID",rsItem.getInt("COLUMN_2_ID"));
                ObjItem.setAttribute("COLUMN_2_FORMULA",rsItem.getString("COLUMN_2_FORMULA"));
                ObjItem.setAttribute("COLUMN_2_PER",rsItem.getDouble("COLUMN_2_PER"));
                ObjItem.setAttribute("COLUMN_2_AMT",rsItem.getDouble("COLUMN_2_AMT"));
                ObjItem.setAttribute("COLUMN_2_CAPTION",rsItem.getString("COLUMN_2_CAPTION"));
                ObjItem.setAttribute("COLUMN_3_ID",rsItem.getInt("COLUMN_3_ID"));
                ObjItem.setAttribute("COLUMN_3_FORMULA",rsItem.getString("COLUMN_3_FORMULA"));
                ObjItem.setAttribute("COLUMN_3_PER",rsItem.getDouble("COLUMN_3_PER"));
                ObjItem.setAttribute("COLUMN_3_AMT",rsItem.getDouble("COLUMN_3_AMT"));
                ObjItem.setAttribute("COLUMN_3_CAPTION",rsItem.getString("COLUMN_3_CAPTION"));
                ObjItem.setAttribute("COLUMN_4_ID",rsItem.getInt("COLUMN_4_ID"));
                ObjItem.setAttribute("COLUMN_4_FORMULA",rsItem.getString("COLUMN_4_FORMULA"));
                ObjItem.setAttribute("COLUMN_4_PER",rsItem.getDouble("COLUMN_4_PER"));
                ObjItem.setAttribute("COLUMN_4_AMT",rsItem.getDouble("COLUMN_4_AMT"));
                ObjItem.setAttribute("COLUMN_4_CAPTION",rsItem.getString("COLUMN_4_CAPTION"));
                ObjItem.setAttribute("COLUMN_5_ID",rsItem.getInt("COLUMN_5_ID"));
                ObjItem.setAttribute("COLUMN_5_FORMULA",rsItem.getString("COLUMN_5_FORMULA"));
                ObjItem.setAttribute("COLUMN_5_PER",rsItem.getDouble("COLUMN_5_PER"));
                ObjItem.setAttribute("COLUMN_5_AMT",rsItem.getDouble("COLUMN_5_AMT"));
                ObjItem.setAttribute("COLUMN_5_CAPTION",rsItem.getString("COLUMN_5_CAPTION"));
                ObjItem.setAttribute("COLUMN_6_ID",rsItem.getInt("COLUMN_6_ID"));
                ObjItem.setAttribute("COLUMN_6_FORMULA",rsItem.getString("COLUMN_6_FORMULA"));
                ObjItem.setAttribute("COLUMN_6_PER",rsItem.getDouble("COLUMN_6_PER"));
                ObjItem.setAttribute("COLUMN_6_AMT",rsItem.getDouble("COLUMN_6_AMT"));
                ObjItem.setAttribute("COLUMN_6_CAPTION",rsItem.getString("COLUMN_6_CAPTION"));
                ObjItem.setAttribute("COLUMN_7_ID",rsItem.getInt("COLUMN_7_ID"));
                ObjItem.setAttribute("COLUMN_7_FORMULA",rsItem.getString("COLUMN_7_FORMULA"));
                ObjItem.setAttribute("COLUMN_7_PER",rsItem.getDouble("COLUMN_7_PER"));
                ObjItem.setAttribute("COLUMN_7_AMT",rsItem.getDouble("COLUMN_7_AMT"));
                ObjItem.setAttribute("COLUMN_7_CAPTION",rsItem.getString("COLUMN_7_CAPTION"));
                ObjItem.setAttribute("COLUMN_8_ID",rsItem.getInt("COLUMN_8_ID"));
                ObjItem.setAttribute("COLUMN_8_FORMULA",rsItem.getString("COLUMN_8_FORMULA"));
                ObjItem.setAttribute("COLUMN_8_PER",rsItem.getDouble("COLUMN_8_PER"));
                ObjItem.setAttribute("COLUMN_8_AMT",rsItem.getDouble("COLUMN_8_AMT"));
                ObjItem.setAttribute("COLUMN_8_CAPTION",rsItem.getString("COLUMN_8_CAPTION"));
                ObjItem.setAttribute("COLUMN_9_ID",rsItem.getInt("COLUMN_9_ID"));
                ObjItem.setAttribute("COLUMN_9_FORMULA",rsItem.getString("COLUMN_9_FORMULA"));
                ObjItem.setAttribute("COLUMN_9_PER",rsItem.getDouble("COLUMN_9_PER"));
                ObjItem.setAttribute("COLUMN_9_AMT",rsItem.getDouble("COLUMN_9_AMT"));
                ObjItem.setAttribute("COLUMN_9_CAPTION",rsItem.getString("COLUMN_9_CAPTION"));
                ObjItem.setAttribute("COLUMN_10_ID",rsItem.getInt("COLUMN_10_ID"));
                ObjItem.setAttribute("COLUMN_10_FORMULA",rsItem.getString("COLUMN_10_FORMULA"));
                ObjItem.setAttribute("COLUMN_10_PER",rsItem.getDouble("COLUMN_10_PER"));
                ObjItem.setAttribute("COLUMN_10_AMT",rsItem.getDouble("COLUMN_10_AMT"));
                ObjItem.setAttribute("COLUMN_10_CAPTION",rsItem.getString("COLUMN_10_CAPTION"));
                
                colSTMItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_STM_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND STM_NO='"+pDocNo+"' AND (APPROVED=1) AND STM_TYPE=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=16 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_STM_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND STM_NO='"+pDocNo+"' AND (APPROVED=1) AND STM_TYPE=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=16 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    public static HashMap getSTMItemList(int pCompanyID,String pSTMNo,boolean pAllItems,int pType,String pdbUrl) {
        //Fourth argument pAllItems indicates whether you want all item listing or
        //Just pending Items where GRN_RECD_QTY<QTY
        //i.e. GRN entry is still pending
        
        Connection tmpConn;
        Statement stTmp=null;
        ResultSet rsTmp=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn(pdbUrl);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_STM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_NO='"+pSTMNo+"' AND STM_TYPE="+pType+" AND APPROVED=1");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(pAllItems) //Retrieve All Items
                {
                    strSQL="SELECT D_INV_STM_DETAIL.* FROM D_INV_STM_DETAIL,D_INV_STM_HEADER  WHERE D_INV_STM_DETAIL.COMPANY_ID=D_INV_STM_HEADER.COMPANY_ID AND D_INV_STM_DETAIL.STM_NO=D_INV_STM_HEADER.STM_NO AND D_INV_STM_DETAIL.COMPANY_ID="+pCompanyID+" AND D_INV_STM_DETAIL.STM_NO='"+pSTMNo+"' AND D_INV_STM_HEADER.STM_TYPE="+pType+" ORDER BY ITEM_ID";
                }
                else //Retrieve Only pending Items
                {
                    strSQL="SELECT D_INV_STM_DETAIL.* FROM D_INV_STM_DETAIL,D_INV_STM_HEADER WHERE D_INV_STM_DETAIL.COMPANY_ID=D_INV_STM_HEADER.COMPANY_ID AND D_INV_STM_DETAIL.STM_NO=D_INV_STM_HEADER.STM_NO AND D_INV_STM_DETAIL.COMPANY_ID="+pCompanyID+" AND D_INV_STM_DETAIL.STM_NO='"+pSTMNo+"' AND D_INV_STM_HEADER.STM_TYPE="+pType+" AND RECEIVED_QTY<QTY ORDER BY ITEM_ID";
                }
                
                
                stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=stTmp.executeQuery(strSQL);
                rsTmp.first();
                
                Counter1=0;
                
                while(!rsTmp.isAfterLast()) {
                    Counter1++;
                    clsSTMGenItem ObjItem=new clsSTMGenItem();
                    
                    int STMSrNo=rsTmp.getInt("SR_NO");
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("STM_NO",rsTmp.getString("STM_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("WAREHOUSE_ID",rsTmp.getString("WAREHOUSE_ID"));
                    ObjItem.setAttribute("LOCATION_ID",rsTmp.getString("LOCATION_ID"));
                    ObjItem.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                    ObjItem.setAttribute("INDENT_SR_NO",rsTmp.getInt("INDENT_SR_NO"));
                    ObjItem.setAttribute("INDENT_COMPANY_ID",rsTmp.getInt("INDENT_COMPANY_ID"));
                    ObjItem.setAttribute("INDENT_COMPANY_YEAR",rsTmp.getInt("INDENT_COMPANY_YEAR"));
                    ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjItem.setAttribute("BOE_NO",rsTmp.getString("BOE_NO"));
                    ObjItem.setAttribute("BOE_SR_NO",rsTmp.getString("BOE_SR_NO"));
                    ObjItem.setAttribute("BOE_DATE",rsTmp.getString("BOE_DATE"));
                    ObjItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                    ObjItem.setAttribute("RECEIVED_QTY",rsTmp.getDouble("RECEIVED_QTY"));
                    ObjItem.setAttribute("ZERO_VAL_QTY",rsTmp.getDouble("ZERO_VAL_QTY"));
                    ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                    ObjItem.setAttribute("COST_CENTER_ID",rsTmp.getInt("COST_CENTER_ID"));
                    ObjItem.setAttribute("RATE",rsTmp.getDouble("RATE"));
                    ObjItem.setAttribute("STM_DESC",rsTmp.getString("STM_DESC"));
                    ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                    
                    //Put into list
                    List.put(Integer.toString(Counter1),ObjItem);
                    
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            return List;
        }
        return List;
    }
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pType,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_STM_HEADER.STM_NO,D_INV_STM_HEADER.STM_DATE,RECEIVED_DATE FROM D_INV_STM_HEADER,D_COM_DOC_DATA WHERE D_INV_STM_HEADER.STM_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_STM_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_STM_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_STM_HEADER.STM_TYPE="+pType+" AND MODULE_ID="+(15+pType)+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_STM_HEADER.STM_NO,D_INV_STM_HEADER.STM_DATE,RECEIVED_DATE FROM D_INV_STM_HEADER,D_COM_DOC_DATA WHERE D_INV_STM_HEADER.STM_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_STM_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_STM_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_STM_HEADER.STM_TYPE="+pType+" AND MODULE_ID="+(15+pType)+" ORDER BY D_INV_STM_HEADER.STM_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_STM_HEADER.STM_NO,D_INV_STM_HEADER.STM_DATE,RECEIVED_DATE FROM D_INV_STM_HEADER,D_COM_DOC_DATA WHERE D_INV_STM_HEADER.STM_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_STM_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_STM_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_STM_HEADER.STM_TYPE="+pType+" AND MODULE_ID="+(15+pType)+" ORDER BY D_INV_STM_HEADER.STM_NO";
            }
            
            //strSQL="SELECT D_INV_STM_HEADER.STM_NO,D_INV_STM_HEADER.STM_DATE FROM D_INV_STM_HEADER,D_COM_DOC_DATA WHERE D_INV_STM_HEADER.STM_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_STM_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_STM_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_STM_HEADER.STM_TYPE="+pType+" AND MODULE_ID="+(15+pType);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSTMGen ObjDoc=new clsSTMGen();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("STM_NO",rsTmp.getString("STM_NO"));
                ObjDoc.setAttribute("STM_DATE",rsTmp.getString("STM_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjDoc);
                
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_STM_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND STM_NO='"+pDocNo+"' AND STM_TYPE=1");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_STM_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(STM_NO)='"+pDocNo+"' AND STM_TYPE=1");
            
            while(rsTmp.next()) {
                clsSTMGen ObjSTM=new clsSTMGen();
                
                ObjSTM.setAttribute("STM_NO",rsTmp.getString("STM_NO"));
                ObjSTM.setAttribute("STM_DATE",rsTmp.getString("STM_DATE"));
                ObjSTM.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjSTM.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjSTM.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjSTM.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjSTM.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjSTM);
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
            rsTmp=data.getResult("SELECT STM_NO,APPROVED,CANCELLED FROM D_INV_STM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_NO='"+pDocNo+"'");
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
    
    public static String getDocStatus(String pDocNo,String dbURL) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT STM_NO,APPROVED,CANCELLED FROM D_INV_STM_HEADER WHERE STM_NO='"+pDocNo+"'",dbURL);
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
            
            rsTmp=data.getResult("SELECT STM_NO FROM D_INV_STM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_NO='"+pDocNo+"' AND CANCELLED=0 AND STM_TYPE=1");
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
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_STM_HEADER WHERE COMPANY_ID="+pCompanyID+" AND STM_NO='"+pDocNo+"' AND STM_TYPE=1");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    
                }
                else {
                    int ModuleID=16;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID="+16);
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_STM_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND STM_NO='"+pDocNo+"' AND STM_TYPE=1");
                
                Cancelled=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
}
