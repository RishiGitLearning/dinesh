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
import EITLERP.Finance.*;
import EITLERP.Purchase.*;


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsSTMReceiptRaw {
    
    public static int ModuleID=181;
    public static int GRNType=3; //1-General 2-Raw Material 3-STM Raw Material
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
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
    public clsSTMReceiptRaw() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GRN_NO",new Variant(""));
        props.put("GRN_DATE",new Variant(""));
        props.put("GRN_TYPE",new Variant(GRNType)); 
        props.put("SUPP_ID",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("CHALAN_NO",new Variant(""));
        props.put("CHALAN_DATE",new Variant(""));
        props.put("LR_NO",new Variant(""));
        props.put("LR_DATE",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("TRANSPORTER",new Variant(0));
        props.put("TRANSPORTER_NAME",new Variant(""));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("IMPROT_CONCESS",new Variant(false));
        props.put("ACCESSABLE_VALUE",new Variant(0));
        props.put("CURRENCY_ID",new Variant(0));
        props.put("CURRENCY_RATE",new Variant(0));
        props.put("CURRENCY_RATE_PAYMENT",new Variant(0));
        props.put("FOR_STORE",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("GROSS_AMOUNT",new Variant(0));
        props.put("GRN_PENDING",new Variant(false));
        props.put("GRN_PENDING_REASON",new Variant(0));
        props.put("OPEN_STATUS",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("APPROVED_ON",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CENVATED_ITEMS",new Variant(false));
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
        
        
        props.put("PF_POST",new Variant(0));
        props.put("FREIGHT_POST",new Variant(0));
        props.put("OCTROI_POST",new Variant(0));
        props.put("INSURANCE_POST",new Variant(0));
        props.put("CLEARANCE_POST",new Variant(0));
        props.put("AIR_FREIGHT_POST",new Variant(0));
        props.put("OTHERS_POST",new Variant(0));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("COLUMN_11_ID",new Variant(0));
        props.put("COLUMN_11_FORMULA",new Variant(""));
        props.put("COLUMN_11_PER",new Variant(0));
        props.put("COLUMN_11_AMT",new Variant(0));
        props.put("COLUMN_11_CAPTION",new Variant(""));
        props.put("COLUMN_12_ID",new Variant(0));
        props.put("COLUMN_12_FORMULA",new Variant(""));
        props.put("COLUMN_12_PER",new Variant(0));
        props.put("COLUMN_12_AMT",new Variant(0));
        props.put("COLUMN_12_CAPTION",new Variant(""));
        props.put("COLUMN_13_ID",new Variant(0));
        props.put("COLUMN_13_FORMULA",new Variant(""));
        props.put("COLUMN_13_PER",new Variant(0));
        props.put("COLUMN_13_AMT",new Variant(0));
        props.put("COLUMN_13_CAPTION",new Variant(""));
        props.put("COLUMN_14_ID",new Variant(0));
        props.put("COLUMN_14_FORMULA",new Variant(""));
        props.put("COLUMN_14_PER",new Variant(0));
        props.put("COLUMN_14_AMT",new Variant(0));
        props.put("COLUMN_14_CAPTION",new Variant(""));
        props.put("COLUMN_15_ID",new Variant(0));
        props.put("COLUMN_15_FORMULA",new Variant(""));
        props.put("COLUMN_15_PER",new Variant(0));
        props.put("COLUMN_15_AMT",new Variant(0));
        props.put("COLUMN_15_CAPTION",new Variant(""));
        props.put("COLUMN_16_ID",new Variant(0));
        props.put("COLUMN_16_FORMULA",new Variant(""));
        props.put("COLUMN_16_PER",new Variant(0));
        props.put("COLUMN_16_AMT",new Variant(0));
        props.put("COLUMN_16_CAPTION",new Variant(""));
        props.put("COLUMN_17_ID",new Variant(0));
        props.put("COLUMN_17_FORMULA",new Variant(""));
        props.put("COLUMN_17_PER",new Variant(0));
        props.put("COLUMN_17_AMT",new Variant(0));
        props.put("COLUMN_17_CAPTION",new Variant(""));
        props.put("COLUMN_18_ID",new Variant(0));
        props.put("COLUMN_18_FORMULA",new Variant(""));
        props.put("COLUMN_18_PER",new Variant(0));
        props.put("COLUMN_18_AMT",new Variant(0));
        props.put("COLUMN_18_CAPTION",new Variant(""));
        props.put("COLUMN_19_ID",new Variant(0));
        props.put("COLUMN_19_FORMULA",new Variant(""));
        props.put("COLUMN_19_PER",new Variant(0));
        props.put("COLUMN_19_AMT",new Variant(0));
        props.put("COLUMN_19_CAPTION",new Variant(""));
        props.put("COLUMN_20_ID",new Variant(0));
        props.put("COLUMN_20_FORMULA",new Variant(""));
        props.put("COLUMN_20_PER",new Variant(0));
        props.put("COLUMN_20_AMT",new Variant(0));
        props.put("COLUMN_20_CAPTION",new Variant(""));
        
        props.put("COLUMN_21_ID",new Variant(0));
        props.put("COLUMN_21_FORMULA",new Variant(""));
        props.put("COLUMN_21_PER",new Variant(0));
        props.put("COLUMN_21_AMT",new Variant(0));
        props.put("COLUMN_21_CAPTION",new Variant(""));
        props.put("COLUMN_22_ID",new Variant(0));
        props.put("COLUMN_22_FORMULA",new Variant(""));
        props.put("COLUMN_22_PER",new Variant(0));
        props.put("COLUMN_22_AMT",new Variant(0));
        props.put("COLUMN_22_CAPTION",new Variant(""));
        props.put("COLUMN_23_ID",new Variant(0));
        props.put("COLUMN_23_FORMULA",new Variant(""));
        props.put("COLUMN_23_PER",new Variant(0));
        props.put("COLUMN_23_AMT",new Variant(0));
        props.put("COLUMN_23_CAPTION",new Variant(""));
        props.put("COLUMN_24_ID",new Variant(0));
        props.put("COLUMN_24_FORMULA",new Variant(""));
        props.put("COLUMN_24_PER",new Variant(0));
        props.put("COLUMN_24_AMT",new Variant(0));
        props.put("COLUMN_24_CAPTION",new Variant(""));
        props.put("COLUMN_25_ID",new Variant(0));
        props.put("COLUMN_25_FORMULA",new Variant(""));
        props.put("COLUMN_25_PER",new Variant(0));
        props.put("COLUMN_25_AMT",new Variant(0));
        props.put("COLUMN_25_CAPTION",new Variant(""));
        props.put("COLUMN_26_ID",new Variant(0));
        props.put("COLUMN_26_FORMULA",new Variant(""));
        props.put("COLUMN_26_PER",new Variant(0));
        props.put("COLUMN_26_AMT",new Variant(0));
        props.put("COLUMN_26_CAPTION",new Variant(""));
        props.put("COLUMN_27_ID",new Variant(0));
        props.put("COLUMN_27_FORMULA",new Variant(""));
        props.put("COLUMN_27_PER",new Variant(0));
        props.put("COLUMN_27_AMT",new Variant(0));
        props.put("COLUMN_27_CAPTION",new Variant(""));
        props.put("COLUMN_28_ID",new Variant(0));
        props.put("COLUMN_28_FORMULA",new Variant(""));
        props.put("COLUMN_28_PER",new Variant(0));
        props.put("COLUMN_28_AMT",new Variant(0));
        props.put("COLUMN_28_CAPTION",new Variant(""));
        props.put("COLUMN_29_ID",new Variant(0));
        props.put("COLUMN_29_FORMULA",new Variant(""));
        props.put("COLUMN_29_PER",new Variant(0));
        props.put("COLUMN_29_AMT",new Variant(0));
        props.put("COLUMN_29_CAPTION",new Variant(""));
        props.put("COLUMN_30_ID",new Variant(0));
        props.put("COLUMN_30_FORMULA",new Variant(""));
        props.put("COLUMN_30_PER",new Variant(0));
        props.put("COLUMN_30_AMT",new Variant(0));
        props.put("COLUMN_30_CAPTION",new Variant(""));
        
        props.put("PF_POST",new Variant(0));
        props.put("FREIGHT_POST",new Variant(0));
        props.put("OCTROI_POST",new Variant(0));
        props.put("INSURANCE_POST",new Variant(0));
        props.put("CLEARANCE_POST",new Variant(0));
        props.put("AIR_FREIGHT_POST",new Variant(0));
        props.put("OTHERS_POST",new Variant(0));
        
        props.put("FIN_HIERARCHY_ID",new Variant(0));
        props.put("PAYMENT_TYPE",new Variant(0));
        props.put("INVOICE_AMOUNT",new Variant(0));
        
    }
    
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String t = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GRN_TYPE='" + GRNType + "' AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GRN_NO";
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GRN_TYPE='" + GRNType + "' AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GRN_NO");
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
        Statement stItem,stLot,stStock,stTmp,stItemMaster,stHistory,stHDetail,stHLot;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsHistory,rsHDetail,rsHLot;
        
        Statement stIssue,stHeader;
        ResultSet rsIssue,rsHeader;
        
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="",LocationID="",MIRNo="",strSQL="",BOEDate="";
        int CompanyID=0,MIRSrNo=0;
        double RejectedQty=0,Qty=0,ToleranceLimit=0;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("GRN_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            if(getAttribute("SUPP_ID").getString().trim().equals("000000"))
            {
               if(getAttribute("PAYMENT_TYPE").getInt()!=1)  {
               LastError="Supplier code is not valid";
               return false;
               }
            }
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE GRN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_GRN_DETAIL_H WHERE GRN_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_GRN_LOT_H WHERE GRN_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='1'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //=========== Check the Quantities entered against MIR.============= //
//                for(int i=1;i<=colGRNItems.size();i++) {
//                    clsGRNItem ObjItem=(clsGRNItem)colGRNItems.get(Integer.toString(i));
//                    MIRNo=(String)ObjItem.getAttribute("MIR_NO").getObj();
//                    MIRSrNo=(int)ObjItem.getAttribute("MIR_SR_NO").getVal();
//
//                    ToleranceLimit=clsItem.getToleranceLimit(CompanyID,(String)ObjItem.getAttribute("ITEM_ID").getObj());
//
//                    double MIRQty=0;
//                    double PrevQty=0; //Previously Entered Qty against MIR
//                    double CurrentQty=0; //Currently entered Qty.
//
//                    if((!MIRNo.trim().equals(""))&&(MIRSrNo>0)) //MIR No. entered
//                    {
//                        //Get the  MIR Qty.
//                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//                        strSQL="SELECT QTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND SR_NO="+MIRSrNo+" AND MIR_TYPE=2";
//                        rsTmp=stTmp.executeQuery(strSQL);
//                        rsTmp.first();
//
//                        if(rsTmp.getRow()>0) {
//                            MIRQty=rsTmp.getDouble("QTY");
//                        }
//
//                        //Get Total Qty Entered in GRN Against this MIR No.
//                        PrevQty=0;
//                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//                        strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_SR_NO="+MIRSrNo+" AND GRN_TYPE=3 AND GRN_NO NOT IN(SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE CANCELLED=1)";
//                        rsTmp=stTmp.executeQuery(strSQL);
//                        rsTmp.first();
//
//                        if(rsTmp.getRow()>0) {
//                            PrevQty=rsTmp.getDouble("SUMQTY");
//                        }
//
//                        CurrentQty=ObjItem.getAttribute("QTY").getVal();
//
//                        /*if((CurrentQty+PrevQty-(( (CurrentQty+PrevQty)*ToleranceLimit)/100) ) > MIRQty) //If total Qty exceeds MIR Qty. Do not allow
//                        {
//                            LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds MIR No. "+MIRNo+" Sr. No. "+MIRSrNo+" qty "+MIRQty+". Please verify the input.";
//                            return false;
//                        }*/
//
//                    }
//                }
            //============== MIR Checking Completed ====================//
            
            
            //=========== Check the Max Qty Level.============= //
//            for(int i=1;i<=colGRNItems.size();i++) {
//                clsGRNItem ObjItem=(clsGRNItem)colGRNItems.get(Integer.toString(i));
//                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
//                
//                double MaxQty=clsItem.getMaxQty(CompanyID, ItemID);
//                
//                if(MaxQty>0) {
//                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
//                    double AvailableQty=clsItem.getAvailableQty(CompanyID, ItemID);
//                    
//                    if((CurrentQty+AvailableQty)>MaxQty) {
//                        LastError="Total Receipt Qty "+(CurrentQty+AvailableQty)+" exceeds Maximum limit "+MaxQty+" for Item "+ItemID;
//                        return false;
//                    }
//                }
//            }
            //=================================================///
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
//                for(int i=1;i<=colSTMItems.size();i++) {
//                    clsSTMRawItem ObjItem=(clsSTMRawItem)colSTMItems.get(Integer.toString(i));
//                    
//                    MIRNo=(String)ObjItem.getAttribute("MIR_NO").getObj();
//                    MIRSrNo=(int)ObjItem.getAttribute("MIR_SR_NO").getVal();
//                    Qty=ObjItem.getAttribute("QTY").getVal();
//                    RejectedQty=ObjItem.getAttribute("REJECTED_QTY").getVal();
//                    ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
//                    
//                    
//                    // Update GRN Received Qty
//                    data.Execute("UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY=GRN_RECD_QTY+"+Qty+",BAL_QTY=QTY-GRN_RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo.trim()+"' AND SR_NO="+MIRSrNo+" AND MIR_TYPE=2");
//                    data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo.trim()+"'");
//                    
//                    //Check that Stock is maintained for this item
//                    
//                    if(clsItem.getMaintainStock(CompanyID,ItemID)) {
//                        //for(int l=1;l<=ObjItem.colItemLot.size();l++) {
//                        //  clsGRNLot ObjLot=(clsGRNLot)ObjItem.colItemLot.get(Integer.toString(l));
//                        
//                        BOENo=(String)ObjItem.getAttribute("BOE_NO").getObj();
//                        ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
//                        BOESrNo=(String)ObjItem.getAttribute("BOE_SR_NO").getObj();
//                        BOEDate=(String)ObjItem.getAttribute("BOE_DATE").getObj();
//                        
//                        double Rate=ObjItem.getAttribute("RATE").getVal();
//                        
//                        WareHouseID=(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj();
//                        LocationID=(String)ObjItem.getAttribute("LOCATION_ID").getObj();
//                        
//                        //LotNo=(String)ObjLot.getAttribute("ITEM_LOT_NO").getObj();
//                        LotNo="X"; //Fix Lot No. for General Items
//                        
//                        //========= New Code of Updating Issue Against the Item =========== //
//                        double IssuedQty=0;
//                        Qty=ObjItem.getAttribute("QTY").getVal();
//                        
//                        rsIssue=data.getResult("SELECT A.ISSUE_NO,QTY,SR_NO,EXCESS_ISSUE_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND B.MIR_NO='"+MIRNo+"' AND B.MIR_SR_NO="+MIRSrNo+" AND EXCESS_ISSUE_QTY>0 AND A.APPROVED=1 AND A.CANCELED=0 AND ITEM_CODE='"+ItemID+"'");
//                        rsIssue.first();
//                        
//                        if(rsIssue.getRow()>0) {
//                            while(!rsIssue.isAfterLast()) {
//                                IssuedQty=rsIssue.getDouble("EXCESS_ISSUE_QTY");
//                                String IssueNo=rsIssue.getString("ISSUE_NO");
//                                int IssueSrNo=rsIssue.getInt("SR_NO");
//                                
//                                if(IssuedQty>Qty) {
//                                    data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-"+(IssuedQty-Qty)+" WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
//                                    Qty=0;
//                                }
//                                
//                                if(IssuedQty==Qty) {
//                                    data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=0 WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
//                                    Qty=0;
//                                }
//                                
//                                if(IssuedQty<Qty) {
//                                    data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-"+(Qty-IssuedQty)+" WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
//                                    Qty=Qty-IssuedQty;
//                                }
//                                
//                                rsIssue.next();
//                            }
//                        }
//                        
//                        if(Qty>0) {
//                            
//                            rsIssue=data.getResult("SELECT A.ISSUE_NO,QTY,SR_NO,EXCESS_ISSUE_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND EXCESS_ISSUE_QTY>0 AND A.APPROVED=1 AND A.CANCELED=0 AND ITEM_CODE='"+ItemID+"'");
//                            rsIssue.first();
//                            
//                            if(rsIssue.getRow()>0) {
//                                while(!rsIssue.isAfterLast()) {
//                                    IssuedQty=rsIssue.getDouble("EXCESS_ISSUE_QTY");
//                                    String IssueNo=rsIssue.getString("ISSUE_NO");
//                                    int IssueSrNo=rsIssue.getInt("SR_NO");
//                                    
//                                    if(IssuedQty>Qty) {
//                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-"+(IssuedQty-Qty)+" WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
//                                        Qty=0;
//                                    }
//                                    
//                                    if(IssuedQty==Qty) {
//                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=0 WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
//                                        Qty=0;
//                                    }
//                                    
//                                    if(IssuedQty<Qty) {
//                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-"+(Qty-IssuedQty)+" WHERE ISSUE_NO='"+IssueNo+"' AND SR_NO="+IssueSrNo);
//                                        Qty=Qty-IssuedQty;
//                                    }
//                                    
//                                    rsIssue.next();
//                                }
//                            }
//                            
//                        }
//                        // ================================================================ //
//                        
//                        
//                        
//                        //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
//                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//                        rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"' AND LOT_NO='"+LotNo.trim()+"' AND BOE_NO='"+BOENo.trim()+"' AND WAREHOUSE_ID='"+WareHouseID.trim()+"' AND LOCATION_ID='"+LocationID.trim()+"'");
//                        rsTmp.first();
//                        
//                        if(rsTmp.getRow()<=0) //Entry does not exist. Create new entry
//                        {
//                            stStock=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//                            rsStock=stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER");
//                            
//                            //Insert the records. Opening Qty will be 0.
//                            rsStock.moveToInsertRow();
//                            rsStock.updateInt("COMPANY_ID",CompanyID);
//                            rsStock.updateString("ITEM_ID",ItemID);
//                            rsStock.updateString("BOE_NO",BOENo);
//                            rsStock.updateString("LOT_NO",LotNo);
//                            rsStock.updateString("BOE_SR_NO",BOESrNo);
//                            rsStock.updateString("BOE_DATE",BOEDate);
//                            rsStock.updateDouble("OPENING_QTY",0);
//                            rsStock.updateDouble("OPENING_RATE",0);
//                            rsStock.updateDouble("TOTAL_RECEIPT_QTY",Qty);
//                            rsStock.updateDouble("TOTAL_ISSUED_QTY",0);
//                            rsStock.updateString("LAST_RECEIPT_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                            rsStock.updateString("LAST_ISSUED_DATE","0000-00-00");
//                            
//                            if(Rate<=0) {
//                                rsStock.updateDouble("ZERO_RECEIPT_QTY",Qty);
//                                rsStock.updateDouble("ZERO_ISSUED_QTY",0);
//                                rsStock.updateDouble("ZERO_VAL_QTY",Qty);
//                            }
//                            else {
//                                rsStock.updateDouble("ZERO_RECEIPT_QTY",0);
//                                rsStock.updateDouble("ZERO_ISSUED_QTY",0);
//                                rsStock.updateDouble("ZERO_VAL_QTY",0);
//                            }
//                            
//                            rsStock.updateDouble("REJECTED_QTY",0);
//                            rsStock.updateDouble("ON_HAND_QTY",Qty);
//                            rsStock.updateDouble("AVAILABLE_QTY",Qty);
//                            rsStock.updateDouble("ALLOCATED_QTY",0);
//                            rsStock.updateString("WAREHOUSE_ID",WareHouseID);
//                            rsStock.updateString("LOCATION_ID",LocationID);
//                            rsStock.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
//                            rsStock.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//                            rsStock.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
//                            rsStock.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
//                            rsStock.updateBoolean("CHANGED",true);
//                            rsStock.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                            rsStock.insertRow();
//                        }
//                        else  //Entry already exist. Update the stock values.
//                        {
//                            stStock=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//                            rsStock=stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"' AND LOT_NO='"+LotNo.trim()+"' AND BOE_NO='"+BOENo.trim()+"' AND WAREHOUSE_ID='"+WareHouseID.trim()+"' AND LOCATION_ID='"+LocationID.trim()+"'");
//                            rsStock.first(); //There will be a single record only
//                            
//                            //double lnLotQty=ObjLot.getAttribute("LOT_QTY").getVal(); //Record current Qty
//                            double lnLotQty=Qty; //Record current Qty
//                            
//                            //Now check the Issued made (with Excess Qty) in Issue Detail
//                                /*stIssue=Conn.createStatement();
//                                rsIssue=stIssue.executeQuery("SELECT D_INV_ISSUE_DETAIL_DETAIL.EXCESS_ISSUE_QTY AS EXCESS_ISSUE FROM D_INV_ISSUE_DETAIL,D_INV_ISSUE_DETAIL_DETAIL WHERE D_INV_ISSUE_DETAIL.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_INV_ISSUE_DETAIL.COMPANY_ID=D_INV_ISSUE_DETAIL_DETAIL.COMPANY_ID AND D_INV_ISSUE_DETAIL.ISSUE_NO=D_INV_ISSUE_DETAIL_DETAIL.ISSUE_NO AND D_INV_ISSUE_DETAIL.MIR_NO='"+MIRNo+"' AND D_INV_ISSUE_DETAIL.MIR_SR_NO="+MIRSrNo);
//                                rsIssue.first();
//                                if(rsIssue.getRow()>0)
//                                {
//                                  double ExcessQty=rsIssue.getDouble("EXCESS_ISSUE");
//                                  lnLotQty=lnLotQty-ExcessQty;
//                                }*/
//                            //======================================================//
//                            
//                            if(Rate<=0) {
//                                rsStock.updateDouble("ZERO_RECEIPT_QTY",rsStock.getDouble("ZERO_RECEIPT_QTY")+lnLotQty);
//                                rsStock.updateDouble("ZERO_VAL_QTY",rsStock.getDouble("ZERO_VAL_QTY")+lnLotQty);
//                            }
//                            
//                            rsStock.updateDouble("TOTAL_RECEIPT_QTY",rsStock.getDouble("TOTAL_RECEIPT_QTY")+lnLotQty);
//                            rsStock.updateString("LAST_RECEIPT_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                            rsStock.updateDouble("ON_HAND_QTY",rsStock.getDouble("ON_HAND_QTY")+lnLotQty);
//                            rsStock.updateDouble("AVAILABLE_QTY",rsStock.getDouble("AVAILABLE_QTY")+lnLotQty);
//                            rsStock.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
//                            rsStock.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
//                            rsStock.updateBoolean("CHANGED",true);
//                            rsStock.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                            rsStock.updateRow();
//                        }
//                        
//                        //======= Update the Item Master =========
//                        stItemMaster=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
//                        rsItemMaster=stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"'");
//                        rsItemMaster.first();
//                        
//                        if(rsItemMaster.getRow()>0) {
//                            double lnQty=ObjItem.getAttribute("QTY").getVal();
//                            rsItemMaster.updateDouble("TOTAL_RECEIPT_QTY",rsItemMaster.getDouble("TOTAL_RECEIPT_QTY")+lnQty);
//                            rsItemMaster.updateString("LAST_RECEIPT_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                            rsItemMaster.updateDouble("ON_HAND_QTY",rsItemMaster.getDouble("ON_HAND_QTY")+lnQty);
//                            rsItemMaster.updateDouble("AVAILABLE_QTY",rsItemMaster.getDouble("AVAILABLE_QTY")+lnQty);
//                            rsItemMaster.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
//                            rsItemMaster.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
//                            rsItemMaster.updateBoolean("CHANGED",true);
//                            rsItemMaster.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
//                            rsItemMaster.updateRow();
//                        }
//                        //======= Item Master stock updation completed ==========
//                        // } //Second for loop
//                    } //If Condition
//                } //First for loop
                //=============Updation of stock completed=========================//
            } // End of First Approval Status condition
            
            
            //--------- Generate New GRN No. ------------
            setAttribute("GRN_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,181, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("GRN_NO",(String)getAttribute("GRN_NO").getObj());
            String GRNNo = getAttribute("GRN_NO").getString();
            rsResultSet.updateString("GRN_DATE",(String)getAttribute("GRN_DATE").getObj());
            rsResultSet.updateString("APPROVED_ON",(String)getAttribute("APPROVED_ON").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsResultSet.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsResultSet.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateDouble("ACCESSABLE_VALUE",getAttribute("ACCESSABLE_VALUE").getVal());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE_PAYMENT",getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsResultSet.updateBoolean("GRN_PENDING",getAttribute("GRN_PENDING").getBool());
            rsResultSet.updateInt("GRN_PENDING_REASON",(int)getAttribute("GRN_PENDING_REASON").getVal());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("GRN_TYPE",(int)getAttribute("GRN_TYPE").getVal());
            rsResultSet.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsResultSet.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
//            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            
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
            
            
            rsResultSet.updateInt("COLUMN_11_ID",(int)getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER",getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT",getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_12_ID",(int)getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER",getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT",getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_13_ID",(int)getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER",getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT",getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_14_ID",(int)getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER",getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT",getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_15_ID",(int)getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER",getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT",getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_16_ID",(int)getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER",getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT",getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_17_ID",(int)getAttribute("COLUMN_17_ID").getVal());
            rsResultSet.updateString("COLUMN_17_FORMULA",(String)getAttribute("COLUMN_17_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_17_PER",getAttribute("COLUMN_17_PER").getVal());
            rsResultSet.updateDouble("COLUMN_17_AMT",getAttribute("COLUMN_17_AMT").getVal());
            rsResultSet.updateString("COLUMN_17_CAPTION",(String)getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_18_ID",(int)getAttribute("COLUMN_18_ID").getVal());
            rsResultSet.updateString("COLUMN_18_FORMULA",(String)getAttribute("COLUMN_18_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_18_PER",getAttribute("COLUMN_18_PER").getVal());
            rsResultSet.updateDouble("COLUMN_18_AMT",getAttribute("COLUMN_18_AMT").getVal());
            rsResultSet.updateString("COLUMN_18_CAPTION",(String)getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_19_ID",(int)getAttribute("COLUMN_19_ID").getVal());
            rsResultSet.updateString("COLUMN_19_FORMULA",(String)getAttribute("COLUMN_19_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_19_PER",getAttribute("COLUMN_19_PER").getVal());
            rsResultSet.updateDouble("COLUMN_19_AMT",getAttribute("COLUMN_19_AMT").getVal());
            rsResultSet.updateString("COLUMN_19_CAPTION",(String)getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_20_ID",(int)getAttribute("COLUMN_20_ID").getVal());
            rsResultSet.updateString("COLUMN_20_FORMULA",(String)getAttribute("COLUMN_20_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_20_PER",getAttribute("COLUMN_20_PER").getVal());
            rsResultSet.updateDouble("COLUMN_20_AMT",getAttribute("COLUMN_20_AMT").getVal());
            rsResultSet.updateString("COLUMN_20_CAPTION",(String)getAttribute("COLUMN_20_CAPTION").getObj());
            
            
            rsResultSet.updateInt("COLUMN_21_ID",(int)getAttribute("COLUMN_21_ID").getVal());
            rsResultSet.updateString("COLUMN_21_FORMULA",(String)getAttribute("COLUMN_21_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_21_PER",getAttribute("COLUMN_21_PER").getVal());
            rsResultSet.updateDouble("COLUMN_21_AMT",getAttribute("COLUMN_21_AMT").getVal());
            rsResultSet.updateString("COLUMN_21_CAPTION",(String)getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_22_ID",(int)getAttribute("COLUMN_22_ID").getVal());
            rsResultSet.updateString("COLUMN_22_FORMULA",(String)getAttribute("COLUMN_22_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_22_PER",getAttribute("COLUMN_22_PER").getVal());
            rsResultSet.updateDouble("COLUMN_22_AMT",getAttribute("COLUMN_22_AMT").getVal());
            rsResultSet.updateString("COLUMN_22_CAPTION",(String)getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_23_ID",(int)getAttribute("COLUMN_23_ID").getVal());
            rsResultSet.updateString("COLUMN_23_FORMULA",(String)getAttribute("COLUMN_23_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_23_PER",getAttribute("COLUMN_23_PER").getVal());
            rsResultSet.updateDouble("COLUMN_23_AMT",getAttribute("COLUMN_23_AMT").getVal());
            rsResultSet.updateString("COLUMN_23_CAPTION",(String)getAttribute("COLUMN_23_CAPTION").getObj());
            
            
            rsResultSet.updateInt("COLUMN_24_ID",(int)getAttribute("COLUMN_24_ID").getVal());
            rsResultSet.updateString("COLUMN_24_FORMULA",(String)getAttribute("COLUMN_24_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_24_PER",getAttribute("COLUMN_24_PER").getVal());
            rsResultSet.updateDouble("COLUMN_24_AMT",getAttribute("COLUMN_24_AMT").getVal());
            rsResultSet.updateString("COLUMN_24_CAPTION",(String)getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_25_ID",(int)getAttribute("COLUMN_25_ID").getVal());
            rsResultSet.updateString("COLUMN_25_FORMULA",(String)getAttribute("COLUMN_25_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_25_PER",getAttribute("COLUMN_25_PER").getVal());
            rsResultSet.updateDouble("COLUMN_25_AMT",getAttribute("COLUMN_25_AMT").getVal());
            rsResultSet.updateString("COLUMN_25_CAPTION",(String)getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_26_ID",(int)getAttribute("COLUMN_26_ID").getVal());
            rsResultSet.updateString("COLUMN_26_FORMULA",(String)getAttribute("COLUMN_26_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_26_PER",getAttribute("COLUMN_26_PER").getVal());
            rsResultSet.updateDouble("COLUMN_26_AMT",getAttribute("COLUMN_26_AMT").getVal());
            rsResultSet.updateString("COLUMN_26_CAPTION",(String)getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_27_ID",(int)getAttribute("COLUMN_27_ID").getVal());
            rsResultSet.updateString("COLUMN_27_FORMULA",(String)getAttribute("COLUMN_27_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_27_PER",getAttribute("COLUMN_27_PER").getVal());
            rsResultSet.updateDouble("COLUMN_27_AMT",getAttribute("COLUMN_27_AMT").getVal());
            rsResultSet.updateString("COLUMN_27_CAPTION",(String)getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_28_ID",(int)getAttribute("COLUMN_28_ID").getVal());
            rsResultSet.updateString("COLUMN_28_FORMULA",(String)getAttribute("COLUMN_28_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_28_PER",getAttribute("COLUMN_28_PER").getVal());
            rsResultSet.updateDouble("COLUMN_28_AMT",getAttribute("COLUMN_28_AMT").getVal());
            rsResultSet.updateString("COLUMN_28_CAPTION",(String)getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_29_ID",(int)getAttribute("COLUMN_29_ID").getVal());
            rsResultSet.updateString("COLUMN_29_FORMULA",(String)getAttribute("COLUMN_29_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_29_PER",getAttribute("COLUMN_29_PER").getVal());
            rsResultSet.updateDouble("COLUMN_29_AMT",getAttribute("COLUMN_29_AMT").getVal());
            rsResultSet.updateString("COLUMN_29_CAPTION",(String)getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_30_ID",(int)getAttribute("COLUMN_30_ID").getVal());
            rsResultSet.updateString("COLUMN_30_FORMULA",(String)getAttribute("COLUMN_30_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_30_PER",getAttribute("COLUMN_30_PER").getVal());
            rsResultSet.updateDouble("COLUMN_30_AMT",getAttribute("COLUMN_30_AMT").getVal());
            rsResultSet.updateString("COLUMN_30_CAPTION",(String)getAttribute("COLUMN_30_CAPTION").getObj());
            
            
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            
            rsResultSet.updateInt("PF_POST",getAttribute("PF_POST").getInt());
            rsResultSet.updateInt("FREIGHT_POST",getAttribute("FREIGHT_POST").getInt());
            rsResultSet.updateInt("OCTROI_POST",getAttribute("OCTROI_POST").getInt());
            rsResultSet.updateInt("INSURANCE_POST",getAttribute("INSURANCE_POST").getInt());
            rsResultSet.updateInt("CLEARANCE_POST",getAttribute("CLEARANCE_POST").getInt());
            rsResultSet.updateInt("AIR_FREIGHT_POST",getAttribute("AIR_FREIGHT_POST").getInt());
            rsResultSet.updateInt("OTHERS_POST",getAttribute("OTHERS_POST").getInt());
            rsResultSet.updateInt("PAYMENT_TYPE",getAttribute("PAYMENT_TYPE").getInt());
            
            
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
            rsHistory.updateString("GRN_NO",(String)getAttribute("GRN_NO").getObj());
            rsHistory.updateString("GRN_DATE",(String)getAttribute("GRN_DATE").getObj());
            rsHistory.updateString("APPROVED_ON",(String)getAttribute("APPROVED_ON").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsHistory.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsHistory.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsHistory.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateDouble("ACCESSABLE_VALUE",getAttribute("ACCESSABLE_VALUE").getVal());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("CURRENCY_RATE_PAYMENT",getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsHistory.updateBoolean("GRN_PENDING",getAttribute("GRN_PENDING").getBool());
            rsHistory.updateInt("GRN_PENDING_REASON",(int)getAttribute("GRN_PENDING_REASON").getVal());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("GRN_TYPE",(int)getAttribute("GRN_TYPE").getVal());
            rsHistory.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsHistory.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
//            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
            
            rsHistory.updateInt("COLUMN_11_ID",(int)getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER",getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT",getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_12_ID",(int)getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER",getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT",getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_13_ID",(int)getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER",getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT",getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_14_ID",(int)getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER",getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT",getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_15_ID",(int)getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER",getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT",getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_16_ID",(int)getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER",getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT",getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_17_ID",(int)getAttribute("COLUMN_17_ID").getVal());
            rsHistory.updateString("COLUMN_17_FORMULA",(String)getAttribute("COLUMN_17_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_17_PER",getAttribute("COLUMN_17_PER").getVal());
            rsHistory.updateDouble("COLUMN_17_AMT",getAttribute("COLUMN_17_AMT").getVal());
            rsHistory.updateString("COLUMN_17_CAPTION",(String)getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_18_ID",(int)getAttribute("COLUMN_18_ID").getVal());
            rsHistory.updateString("COLUMN_18_FORMULA",(String)getAttribute("COLUMN_18_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_18_PER",getAttribute("COLUMN_18_PER").getVal());
            rsHistory.updateDouble("COLUMN_18_AMT",getAttribute("COLUMN_18_AMT").getVal());
            rsHistory.updateString("COLUMN_18_CAPTION",(String)getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_19_ID",(int)getAttribute("COLUMN_19_ID").getVal());
            rsHistory.updateString("COLUMN_19_FORMULA",(String)getAttribute("COLUMN_19_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_19_PER",getAttribute("COLUMN_19_PER").getVal());
            rsHistory.updateDouble("COLUMN_19_AMT",getAttribute("COLUMN_19_AMT").getVal());
            rsHistory.updateString("COLUMN_19_CAPTION",(String)getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_20_ID",(int)getAttribute("COLUMN_20_ID").getVal());
            rsHistory.updateString("COLUMN_20_FORMULA",(String)getAttribute("COLUMN_20_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_20_PER",getAttribute("COLUMN_20_PER").getVal());
            rsHistory.updateDouble("COLUMN_20_AMT",getAttribute("COLUMN_20_AMT").getVal());
            rsHistory.updateString("COLUMN_20_CAPTION",(String)getAttribute("COLUMN_20_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_21_ID",(int)getAttribute("COLUMN_21_ID").getVal());
            rsHistory.updateString("COLUMN_21_FORMULA",(String)getAttribute("COLUMN_21_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_21_PER",getAttribute("COLUMN_21_PER").getVal());
            rsHistory.updateDouble("COLUMN_21_AMT",getAttribute("COLUMN_21_AMT").getVal());
            rsHistory.updateString("COLUMN_21_CAPTION",(String)getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_22_ID",(int)getAttribute("COLUMN_22_ID").getVal());
            rsHistory.updateString("COLUMN_22_FORMULA",(String)getAttribute("COLUMN_22_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_22_PER",getAttribute("COLUMN_22_PER").getVal());
            rsHistory.updateDouble("COLUMN_22_AMT",getAttribute("COLUMN_22_AMT").getVal());
            rsHistory.updateString("COLUMN_22_CAPTION",(String)getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_23_ID",(int)getAttribute("COLUMN_23_ID").getVal());
            rsHistory.updateString("COLUMN_23_FORMULA",(String)getAttribute("COLUMN_23_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_23_PER",getAttribute("COLUMN_23_PER").getVal());
            rsHistory.updateDouble("COLUMN_23_AMT",getAttribute("COLUMN_23_AMT").getVal());
            rsHistory.updateString("COLUMN_23_CAPTION",(String)getAttribute("COLUMN_23_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_24_ID",(int)getAttribute("COLUMN_24_ID").getVal());
            rsHistory.updateString("COLUMN_24_FORMULA",(String)getAttribute("COLUMN_24_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_24_PER",getAttribute("COLUMN_24_PER").getVal());
            rsHistory.updateDouble("COLUMN_24_AMT",getAttribute("COLUMN_24_AMT").getVal());
            rsHistory.updateString("COLUMN_24_CAPTION",(String)getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_25_ID",(int)getAttribute("COLUMN_25_ID").getVal());
            rsHistory.updateString("COLUMN_25_FORMULA",(String)getAttribute("COLUMN_25_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_25_PER",getAttribute("COLUMN_25_PER").getVal());
            rsHistory.updateDouble("COLUMN_25_AMT",getAttribute("COLUMN_25_AMT").getVal());
            rsHistory.updateString("COLUMN_25_CAPTION",(String)getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_26_ID",(int)getAttribute("COLUMN_26_ID").getVal());
            rsHistory.updateString("COLUMN_26_FORMULA",(String)getAttribute("COLUMN_26_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_26_PER",getAttribute("COLUMN_26_PER").getVal());
            rsHistory.updateDouble("COLUMN_26_AMT",getAttribute("COLUMN_26_AMT").getVal());
            rsHistory.updateString("COLUMN_26_CAPTION",(String)getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_27_ID",(int)getAttribute("COLUMN_27_ID").getVal());
            rsHistory.updateString("COLUMN_27_FORMULA",(String)getAttribute("COLUMN_27_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_27_PER",getAttribute("COLUMN_27_PER").getVal());
            rsHistory.updateDouble("COLUMN_27_AMT",getAttribute("COLUMN_27_AMT").getVal());
            rsHistory.updateString("COLUMN_27_CAPTION",(String)getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_28_ID",(int)getAttribute("COLUMN_28_ID").getVal());
            rsHistory.updateString("COLUMN_28_FORMULA",(String)getAttribute("COLUMN_28_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_28_PER",getAttribute("COLUMN_28_PER").getVal());
            rsHistory.updateDouble("COLUMN_28_AMT",getAttribute("COLUMN_28_AMT").getVal());
            rsHistory.updateString("COLUMN_28_CAPTION",(String)getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_29_ID",(int)getAttribute("COLUMN_29_ID").getVal());
            rsHistory.updateString("COLUMN_29_FORMULA",(String)getAttribute("COLUMN_29_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_29_PER",getAttribute("COLUMN_29_PER").getVal());
            rsHistory.updateDouble("COLUMN_29_AMT",getAttribute("COLUMN_29_AMT").getVal());
            rsHistory.updateString("COLUMN_29_CAPTION",(String)getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_30_ID",(int)getAttribute("COLUMN_30_ID").getVal());
            rsHistory.updateString("COLUMN_30_FORMULA",(String)getAttribute("COLUMN_30_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_30_PER",getAttribute("COLUMN_30_PER").getVal());
            rsHistory.updateDouble("COLUMN_30_AMT",getAttribute("COLUMN_30_AMT").getVal());
            rsHistory.updateString("COLUMN_30_CAPTION",(String)getAttribute("COLUMN_30_CAPTION").getObj());
            
            
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            
            rsHistory.updateInt("PF_POST",getAttribute("PF_POST").getInt());
            rsHistory.updateInt("FREIGHT_POST",getAttribute("FREIGHT_POST").getInt());
            rsHistory.updateInt("OCTROI_POST",getAttribute("OCTROI_POST").getInt());
            rsHistory.updateInt("INSURANCE_POST",getAttribute("INSURANCE_POST").getInt());
            rsHistory.updateInt("CLEARANCE_POST",getAttribute("CLEARANCE_POST").getInt());
            rsHistory.updateInt("AIR_FREIGHT_POST",getAttribute("AIR_FREIGHT_POST").getInt());
            rsHistory.updateInt("OTHERS_POST",getAttribute("OTHERS_POST").getInt());
            rsHistory.updateInt("PAYMENT_TYPE",getAttribute("PAYMENT_TYPE").getInt());
            
            rsHistory.insertRow();
            
            
            //====== Now turn of GRN Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL WHERE GRN_NO='1'");
            rsItem.first();
            
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_GRN_LOT WHERE GRN_NO='1'");
            rsLot.first();
            
            for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMRawItem ObjItem=(clsSTMRawItem)colSTMItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsItem.updateString("GRN_NO",getAttribute("GRN_NO").getString());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("GRN_TYPE",GRNType);// 3 - STM Raw Material
                rsItem.updateString("WAREHOUSE_ID",ObjItem.getAttribute("WAREHOUSE_ID").getString());
                rsItem.updateString("LOCATION_ID",ObjItem.getAttribute("LOCATION_ID").getString());
                rsItem.updateString("MIR_NO",ObjItem.getAttribute("MIR_NO").getString());
                rsItem.updateInt("MIR_SR_NO",ObjItem.getAttribute("MIR_SR_NO").getInt());
                rsItem.updateInt("MIR_TYPE",2);
                rsItem.updateString("PO_NO",ObjItem.getAttribute("PO_NO").getString());
                rsItem.updateInt("PO_SR_NO",ObjItem.getAttribute("PO_SR_NO").getInt());
                rsItem.updateInt("PO_TYPE",ObjItem.getAttribute("PO_TYPE").getInt());
                rsItem.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsItem.updateString("BOE_NO",ObjItem.getAttribute("BOE_NO").getString());
                rsItem.updateString("BOE_DATE",ObjItem.getAttribute("BOE_DATE").getString());
                rsItem.updateString("BOE_SR_NO",ObjItem.getAttribute("BOE_SR_NO").getString());
                rsItem.updateDouble("MIR_QTY",ObjItem.getAttribute("MIR_QTY").getDouble());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getDouble());
                rsItem.updateDouble("EXCESS_QTY",ObjItem.getAttribute("EXCESS_QTY").getDouble());
                rsItem.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getDouble());
                rsItem.updateDouble("REJECTED_QTY",ObjItem.getAttribute("REJECTED_QTY").getDouble());
                rsItem.updateInt("UNIT",ObjItem.getAttribute("UNIT").getInt());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getDouble());
                rsItem.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getDouble());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getDouble());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getDouble());
                rsItem.updateString("SHADE",ObjItem.getAttribute("SHADE").getString());
                rsItem.updateString("W_MIE",ObjItem.getAttribute("W_MIE").getString());
                rsItem.updateString("NO_CASE",ObjItem.getAttribute("NO_CASE").getString());
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//                rsItem.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
//                rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
                
                /*rsItem.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsItem.updateString("COLUMN_11_FORMULA",(String)ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_11_PER",ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsItem.updateDouble("COLUMN_11_AMT",ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsItem.updateString("COLUMN_11_CAPTION",(String)ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_12_ID",(int)ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsItem.updateString("COLUMN_12_FORMULA",(String)ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_12_PER",ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsItem.updateDouble("COLUMN_12_AMT",ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsItem.updateString("COLUMN_12_CAPTION",(String)ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_13_ID",(int)ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsItem.updateString("COLUMN_13_FORMULA",(String)ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_13_PER",ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsItem.updateDouble("COLUMN_13_AMT",ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsItem.updateString("COLUMN_13_CAPTION",(String)ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_14_ID",(int)ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsItem.updateString("COLUMN_14_FORMULA",(String)ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_14_PER",ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsItem.updateDouble("COLUMN_14_AMT",ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsItem.updateString("COLUMN_14_CAPTION",(String)ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_15_ID",(int)ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsItem.updateString("COLUMN_15_FORMULA",(String)ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_15_PER",ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsItem.updateDouble("COLUMN_15_AMT",ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsItem.updateString("COLUMN_15_CAPTION",(String)ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_16_ID",(int)ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsItem.updateString("COLUMN_16_FORMULA",(String)ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_16_PER",ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsItem.updateDouble("COLUMN_16_AMT",ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsItem.updateString("COLUMN_16_CAPTION",(String)ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_17_ID",(int)ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsItem.updateString("COLUMN_17_FORMULA",(String)ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_17_PER",ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsItem.updateDouble("COLUMN_17_AMT",ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsItem.updateString("COLUMN_17_CAPTION",(String)ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_18_ID",(int)ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsItem.updateString("COLUMN_18_FORMULA",(String)ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_18_PER",ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsItem.updateDouble("COLUMN_18_AMT",ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsItem.updateString("COLUMN_18_CAPTION",(String)ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_19_ID",(int)ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsItem.updateString("COLUMN_19_FORMULA",(String)ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_19_PER",ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsItem.updateDouble("COLUMN_19_AMT",ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsItem.updateString("COLUMN_19_CAPTION",(String)ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_20_ID",(int)ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsItem.updateString("COLUMN_20_FORMULA",(String)ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_20_PER",ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsItem.updateDouble("COLUMN_20_AMT",ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsItem.updateString("COLUMN_20_CAPTION",(String)ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                
                rsItem.updateInt("COLUMN_21_ID",(int)ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsItem.updateString("COLUMN_21_FORMULA",(String)ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_21_PER",ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsItem.updateDouble("COLUMN_21_AMT",ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsItem.updateString("COLUMN_21_CAPTION",(String)ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_22_ID",(int)ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsItem.updateString("COLUMN_22_FORMULA",(String)ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_22_PER",ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsItem.updateDouble("COLUMN_22_AMT",ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsItem.updateString("COLUMN_22_CAPTION",(String)ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_23_ID",(int)ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsItem.updateString("COLUMN_23_FORMULA",(String)ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_23_PER",ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsItem.updateDouble("COLUMN_23_AMT",ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsItem.updateString("COLUMN_23_CAPTION",(String)ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_24_ID",(int)ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsItem.updateString("COLUMN_24_FORMULA",(String)ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_24_PER",ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsItem.updateDouble("COLUMN_24_AMT",ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsItem.updateString("COLUMN_24_CAPTION",(String)ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_25_ID",(int)ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsItem.updateString("COLUMN_25_FORMULA",(String)ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_25_PER",ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsItem.updateDouble("COLUMN_25_AMT",ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsItem.updateString("COLUMN_25_CAPTION",(String)ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_26_ID",(int)ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsItem.updateString("COLUMN_26_FORMULA",(String)ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_26_PER",ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsItem.updateDouble("COLUMN_26_AMT",ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsItem.updateString("COLUMN_26_CAPTION",(String)ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_27_ID",(int)ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsItem.updateString("COLUMN_27_FORMULA",(String)ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_27_PER",ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsItem.updateDouble("COLUMN_27_AMT",ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsItem.updateString("COLUMN_27_CAPTION",(String)ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_28_ID",(int)ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsItem.updateString("COLUMN_28_FORMULA",(String)ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_28_PER",ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsItem.updateDouble("COLUMN_28_AMT",ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsItem.updateString("COLUMN_28_CAPTION",(String)ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_29_ID",(int)ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsItem.updateString("COLUMN_29_FORMULA",(String)ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_29_PER",ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsItem.updateDouble("COLUMN_29_AMT",ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsItem.updateString("COLUMN_29_CAPTION",(String)ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_30_ID",(int)ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsItem.updateString("COLUMN_30_FORMULA",(String)ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_30_PER",ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsItem.updateDouble("COLUMN_30_AMT",ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsItem.updateString("COLUMN_30_CAPTION",(String)ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                
                */
                rsItem.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsItem.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsItem.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsItem.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsItem.updateDouble("EXCESS",ObjItem.getAttribute("EXCESS").getVal());
                rsItem.updateDouble("SHORTAGE",ObjItem.getAttribute("SHORTAGE").getVal());
                rsItem.updateDouble("CHALAN_QTY",ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsItem.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsItem.updateString("BARCODE_TYPE",(String)ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsItem.updateDouble("PO_QTY",ObjItem.getAttribute("PO_QTY").getVal());
                rsItem.updateDouble("RECEIVED_QTY",ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsItem.updateDouble("BALANCE_PO_QTY",ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsItem.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsItem.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsItem.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                rsHDetail.updateString("GRN_NO",getAttribute("GRN_NO").getString());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("GRN_TYPE",GRNType);// 3 - STM Raw Material
                rsHDetail.updateString("WAREHOUSE_ID",ObjItem.getAttribute("WAREHOUSE_ID").getString());
                rsHDetail.updateString("LOCATION_ID",ObjItem.getAttribute("LOCATION_ID").getString());
                rsHDetail.updateString("MIR_NO",ObjItem.getAttribute("MIR_NO").getString());
                rsHDetail.updateInt("MIR_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE",2);
                rsHDetail.updateString("PO_NO",ObjItem.getAttribute("PO_NO").getString());
                rsHDetail.updateInt("PO_SR_NO",ObjItem.getAttribute("PO_SR_NO").getInt());
                rsHDetail.updateInt("PO_TYPE",ObjItem.getAttribute("PO_TYPE").getInt());
                rsHDetail.updateString("ITEM_ID",ObjItem.getAttribute("ITEM_ID").getString());
                rsHDetail.updateString("BOE_NO",ObjItem.getAttribute("BOE_NO").getString());
                rsHDetail.updateString("BOE_DATE",ObjItem.getAttribute("BOE_DATE").getString());
                rsHDetail.updateString("BOE_SR_NO",ObjItem.getAttribute("BOE_SR_NO").getString());
                rsHDetail.updateDouble("MIR_QTY",ObjItem.getAttribute("MIR_QTY").getDouble());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getDouble());
                rsHDetail.updateDouble("EXCESS_QTY",ObjItem.getAttribute("EXCESS_QTY").getDouble());
                rsHDetail.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getDouble());
                rsHDetail.updateDouble("REJECTED_QTY",ObjItem.getAttribute("REJECTED_QTY").getDouble());
                rsHDetail.updateInt("UNIT",ObjItem.getAttribute("UNIT").getInt());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getDouble());
                rsHDetail.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getDouble());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getDouble());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getDouble());
                rsHDetail.updateString("SHADE",ObjItem.getAttribute("SHADE").getString());
                rsHDetail.updateString("W_MIE",ObjItem.getAttribute("W_MIE").getString());
                rsHDetail.updateString("NO_CASE",ObjItem.getAttribute("NO_CASE").getString());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
//                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
                
                /*rsHDetail.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA",(String)ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER",ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT",ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION",(String)ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_12_ID",(int)ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA",(String)ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER",ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT",ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION",(String)ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_13_ID",(int)ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA",(String)ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER",ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT",ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION",(String)ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_14_ID",(int)ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA",(String)ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER",ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT",ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION",(String)ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_15_ID",(int)ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA",(String)ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER",ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT",ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION",(String)ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_16_ID",(int)ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA",(String)ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER",ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT",ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION",(String)ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_17_ID",(int)ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsHDetail.updateString("COLUMN_17_FORMULA",(String)ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_17_PER",ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsHDetail.updateDouble("COLUMN_17_AMT",ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsHDetail.updateString("COLUMN_17_CAPTION",(String)ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_18_ID",(int)ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsHDetail.updateString("COLUMN_18_FORMULA",(String)ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_18_PER",ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsHDetail.updateDouble("COLUMN_18_AMT",ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsHDetail.updateString("COLUMN_18_CAPTION",(String)ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_19_ID",(int)ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsHDetail.updateString("COLUMN_19_FORMULA",(String)ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_19_PER",ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsHDetail.updateDouble("COLUMN_19_AMT",ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsHDetail.updateString("COLUMN_19_CAPTION",(String)ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_20_ID",(int)ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsHDetail.updateString("COLUMN_20_FORMULA",(String)ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_20_PER",ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsHDetail.updateDouble("COLUMN_20_AMT",ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsHDetail.updateString("COLUMN_20_CAPTION",(String)ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_21_ID",(int)ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsHDetail.updateString("COLUMN_21_FORMULA",(String)ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_21_PER",ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsHDetail.updateDouble("COLUMN_21_AMT",ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsHDetail.updateString("COLUMN_21_CAPTION",(String)ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_22_ID",(int)ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsHDetail.updateString("COLUMN_22_FORMULA",(String)ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_22_PER",ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsHDetail.updateDouble("COLUMN_22_AMT",ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsHDetail.updateString("COLUMN_22_CAPTION",(String)ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_23_ID",(int)ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsHDetail.updateString("COLUMN_23_FORMULA",(String)ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_23_PER",ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsHDetail.updateDouble("COLUMN_23_AMT",ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsHDetail.updateString("COLUMN_23_CAPTION",(String)ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_24_ID",(int)ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsHDetail.updateString("COLUMN_24_FORMULA",(String)ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_24_PER",ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsHDetail.updateDouble("COLUMN_24_AMT",ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsHDetail.updateString("COLUMN_24_CAPTION",(String)ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_25_ID",(int)ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsHDetail.updateString("COLUMN_25_FORMULA",(String)ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_25_PER",ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsHDetail.updateDouble("COLUMN_25_AMT",ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsHDetail.updateString("COLUMN_25_CAPTION",(String)ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_26_ID",(int)ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsHDetail.updateString("COLUMN_26_FORMULA",(String)ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_26_PER",ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsHDetail.updateDouble("COLUMN_26_AMT",ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsHDetail.updateString("COLUMN_26_CAPTION",(String)ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_27_ID",(int)ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsHDetail.updateString("COLUMN_27_FORMULA",(String)ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_27_PER",ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsHDetail.updateDouble("COLUMN_27_AMT",ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsHDetail.updateString("COLUMN_27_CAPTION",(String)ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_28_ID",(int)ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsHDetail.updateString("COLUMN_28_FORMULA",(String)ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_28_PER",ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsHDetail.updateDouble("COLUMN_28_AMT",ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsHDetail.updateString("COLUMN_28_CAPTION",(String)ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_29_ID",(int)ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsHDetail.updateString("COLUMN_29_FORMULA",(String)ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_29_PER",ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsHDetail.updateDouble("COLUMN_29_AMT",ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsHDetail.updateString("COLUMN_29_CAPTION",(String)ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_30_ID",(int)ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsHDetail.updateString("COLUMN_30_FORMULA",(String)ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_30_PER",ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsHDetail.updateDouble("COLUMN_30_AMT",ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsHDetail.updateString("COLUMN_30_CAPTION",(String)ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                */
                rsHDetail.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsHDetail.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsHDetail.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsHDetail.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsHDetail.updateDouble("EXCESS",ObjItem.getAttribute("EXCESS").getVal());
                rsHDetail.updateDouble("SHORTAGE",ObjItem.getAttribute("SHORTAGE").getVal());
                rsHDetail.updateDouble("CHALAN_QTY",ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsHDetail.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsHDetail.updateString("BARCODE_TYPE",(String)ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsHDetail.updateDouble("PO_QTY",ObjItem.getAttribute("PO_QTY").getVal());
                rsHDetail.updateDouble("RECEIVED_QTY",ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsHDetail.updateDouble("BALANCE_PO_QTY",ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsHDetail.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsHDetail.insertRow();
                
                //Now insert lot nos.
                for(int l=1;l<=ObjItem.colSTMLot.size();l++) {
                    clsSTMReceiptRawLot ObjLot=(clsSTMReceiptRawLot)ObjItem.colSTMLot.get(Integer.toString(l));
                    String AutoLotNo = EnterSTMReceiptRawLotQty.getNextLOTNo(EITLERPGLOBAL.gCompanyID);
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsLot.updateString("GRN_NO",getAttribute("GRN_NO").getString());
                    rsLot.updateInt("GRN_SR_NO",i);
                    rsLot.updateInt("GRN_TYPE",GRNType); //3 - STM RAW Receipt
                    rsLot.updateInt("SR_NO",l);
                    rsLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsLot.updateString("AUTO_LOT_NO", AutoLotNo);
                    rsLot.updateDouble("LOT_RECEIVED_QTY",ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble());
                    rsLot.updateDouble("LOT_REJECTED_QTY",ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble());
                    rsLot.updateDouble("LOT_ACCEPTED_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsLot.updateDouble("BALANCE_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsLot.updateBoolean("LOT_CLOSE",false);
                    rsLot.updateBoolean("APPROVED",false);
                    rsLot.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.updateBoolean("CANCELLED",false);
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",1);
                    rsHLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsHLot.updateString("GRN_NO",getAttribute("GRN_NO").getString());
                    rsHLot.updateInt("GRN_SR_NO",i);
                    rsHLot.updateInt("GRN_TYPE",GRNType);
                    rsHLot.updateInt("SR_NO",l);
                    rsHLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsHLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsHLot.updateString("AUTO_LOT_NO",AutoLotNo);
                    rsHLot.updateDouble("LOT_RECEIVED_QTY",ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble());
                    rsHLot.updateDouble("LOT_REJECTED_QTY",ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble());
                    rsHLot.updateDouble("LOT_ACCEPTED_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsHLot.updateDouble("BALANCE_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsHLot.updateBoolean("LOT_CLOSE",false);
                    rsHLot.updateBoolean("APPROVED",false);
                    rsHLot.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.updateBoolean("CANCELLED",false);
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=181; //GRN Raw Material
            ObjFlow.DocNo=(String)getAttribute("GRN_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GRN_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="GRN_NO";
            
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
            
            
            
            //===============Accounts PJV Generation =============//
            if(AStatus.equals("F")) {

                String SQL = "SELECT * FROM D_INV_GRN_LOT WHERE GRN_NO='"+GRNNo+"' ORDER BY GRN_NO,GRN_SR_NO,SR_NO";
                ResultSet rsLotUpdate = data.getResult(SQL);
                rsLotUpdate.first();
                while(!rsLotUpdate.isAfterLast()) {
                    int GRNSrNo = rsLotUpdate.getInt("GRN_SR_NO");
                    int SrNo = rsLotUpdate.getInt("SR_NO");
                    String UpdateRecord = "UPDATE D_INV_GRN_LOT SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                    "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+ GRNSrNo +" AND SR_NO="+SrNo+" ";
                    data.Execute(UpdateRecord);
                    
                    UpdateRecord = "UPDATE D_INV_GRN_LOT_H SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                    "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+ GRNSrNo +" AND SR_NO="+SrNo+" ";
                    data.Execute(UpdateRecord);
                    
                    rsLotUpdate.next();
                }
            }
            //====================================================//
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            
            e.printStackTrace();
            return false;
        }
    }
    
    private void RevertStockEffect() {
        Statement stItemMaster,stItem,stTmp;
        ResultSet rsItemMaster,rsItem,rsTmp;
        String strSQL="",GRNNo="",ItemID="",BOENo="",LotNo="",WareHouseID="",LocationID="",MIRNo="";
        int CompanyID=0,MIRSrNo=0;
        double Qty=0,RejectedQty=0;
        
        try {
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            GRNNo=(String)getAttribute("GRN_NO").getObj();
            
            strSQL="SELECT A.COMPANY_ID,A.GRN_NO,A.SR_NO,A.WAREHOUSE_ID,A.LOCATION_ID,B.LOT_QTY,A.ITEM_ID,B.ITEM_LOT_NO,A.BOE_NO FROM D_INV_GRN_DETAIL A,D_INV_GRN_LOT B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.SR_NO=B.GRN_SR_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.COMPANY_ID="+CompanyID+" AND A.GRN_NO='"+GRNNo+"' AND A.GRN_TYPE='" + GRNType + "'";
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                
                ItemID=rsTmp.getString("ITEM_ID");
                BOENo=rsTmp.getString("BOE_NO");
                LotNo=rsTmp.getString("ITEM_LOT_NO");
                WareHouseID=rsTmp.getString("WAREHOUSE_ID");
                LocationID=rsTmp.getString("LOCATION_ID");
                Qty=rsTmp.getDouble("LOT_QTY");
                
                stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"' AND BOE_NO='"+BOENo.trim()+"' AND LOT_NO='"+LotNo.trim()+"' AND WAREHOUSE_ID='"+WareHouseID.trim()+"' AND LOCATION_ID='"+LocationID.trim()+"'");
                rsItem.first();
                
                if(rsItem.getRow()>0) {
                    rsItem.updateDouble("TOTAL_RECEIPT_QTY",rsItem.getDouble("TOTAL_RECEIPT_QTY")-Qty);
                    rsItem.updateDouble("ON_HAND_QTY",rsItem.getDouble("ON_HAND_QTY")-Qty);
                    rsItem.updateDouble("AVAILABLE_QTY",rsItem.getDouble("AVAILABLE_QTY")-Qty);
                    rsItem.updateRow();
                }
                
                stItemMaster=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsItemMaster=stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"'");
                rsItemMaster.first();
                
                if(rsItemMaster.getRow()>0) {
                    rsItemMaster.updateDouble("TOTAL_RECEIPT_QTY",rsItemMaster.getDouble("TOTAL_RECEIPT_QTY")-Qty);
                    rsItemMaster.updateDouble("ON_HAND_QTY",rsItemMaster.getDouble("ON_HAND_QTY")-Qty);
                    rsItemMaster.updateDouble("AVAILABLE_QTY",rsItemMaster.getDouble("AVAILABLE_QTY")-Qty);
                    rsItemMaster.updateRow();
                }
                
                rsTmp.next();
            }
            
            //Now give reverse effect to MIR Table
            strSQL="SELECT * FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_TYPE='" + GRNType + "'";
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                MIRNo=rsTmp.getString("MIR_NO");
                MIRSrNo=rsTmp.getInt("MIR_SR_NO");
                Qty=rsTmp.getDouble("QTY");
                RejectedQty=rsTmp.getDouble("REJECTED_QTY");
                
                // Update GRN Received Qty
                data.Execute("UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY=GRN_RECD_QTY-"+Qty+",BAL_QTY=QTY-GRN_RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND SR_NO="+MIRSrNo+" AND MIR_TYPE=2");
                rsTmp.next();
            }
        }
        catch(Exception e) {
        }
    }
    
    
    //Updates current record
    public boolean Update() {
        Statement stItem,stLot,stStock,stTmp,stItemMaster,stCheck,stHistory,stHDetail,stHLot;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsCheck,rsHistory,rsHDetail,rsHLot;
        Statement stIssue,stHeader;
        ResultSet rsIssue,rsHeader;
        
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="";
        String LocationID="",GRNNo="",MIRNo="",strSQL="",BOEDate="";
        int CompanyID=0,MIRSrNo=0;
        double Qty=0,RejectedQty=0,ToleranceLimit=0;
        boolean Validate=true;
        
        try {
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("GRN_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
           
            if(getAttribute("SUPP_ID").getString().trim().equals("000000"))
            {
               if(getAttribute("PAYMENT_TYPE").getInt()!=1) {
               LastError="Supplier code is not valid";
               return false;
               }
            }
            
          
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE GRN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_GRN_DETAIL_H WHERE GRN_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_GRN_LOT_H WHERE GRN_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("GRN_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GRN_NO)='"+theDocNo+"' AND GRN_TYPE=2");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            GRNNo=(String)getAttribute("GRN_NO").getObj();
            

            rsResultSet.updateString("GRN_DATE",(String)getAttribute("GRN_DATE").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsResultSet.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("APPROVED_ON",(String)getAttribute("APPROVED_ON").getObj());
            rsResultSet.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsResultSet.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateDouble("ACCESSABLE_VALUE",getAttribute("ACCESSABLE_VALUE").getVal());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE_PAYMENT",getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsResultSet.updateBoolean("GRN_PENDING",getAttribute("GRN_PENDING").getBool());
            rsResultSet.updateInt("GRN_PENDING_REASON",(int)getAttribute("GRN_PENDING_REASON").getVal());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("GRN_TYPE",(int)getAttribute("GRN_TYPE").getVal());
            rsResultSet.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsResultSet.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
//            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
//            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
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
            
            rsResultSet.updateInt("COLUMN_11_ID",(int)getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER",getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT",getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_12_ID",(int)getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER",getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT",getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_13_ID",(int)getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER",getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT",getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_14_ID",(int)getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER",getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT",getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_15_ID",(int)getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER",getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT",getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_16_ID",(int)getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER",getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT",getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_17_ID",(int)getAttribute("COLUMN_17_ID").getVal());
            rsResultSet.updateString("COLUMN_17_FORMULA",(String)getAttribute("COLUMN_17_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_17_PER",getAttribute("COLUMN_17_PER").getVal());
            rsResultSet.updateDouble("COLUMN_17_AMT",getAttribute("COLUMN_17_AMT").getVal());
            rsResultSet.updateString("COLUMN_17_CAPTION",(String)getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_18_ID",(int)getAttribute("COLUMN_18_ID").getVal());
            rsResultSet.updateString("COLUMN_18_FORMULA",(String)getAttribute("COLUMN_18_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_18_PER",getAttribute("COLUMN_18_PER").getVal());
            rsResultSet.updateDouble("COLUMN_18_AMT",getAttribute("COLUMN_18_AMT").getVal());
            rsResultSet.updateString("COLUMN_18_CAPTION",(String)getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_19_ID",(int)getAttribute("COLUMN_19_ID").getVal());
            rsResultSet.updateString("COLUMN_19_FORMULA",(String)getAttribute("COLUMN_19_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_19_PER",getAttribute("COLUMN_19_PER").getVal());
            rsResultSet.updateDouble("COLUMN_19_AMT",getAttribute("COLUMN_19_AMT").getVal());
            rsResultSet.updateString("COLUMN_19_CAPTION",(String)getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_20_ID",(int)getAttribute("COLUMN_20_ID").getVal());
            rsResultSet.updateString("COLUMN_20_FORMULA",(String)getAttribute("COLUMN_20_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_20_PER",getAttribute("COLUMN_20_PER").getVal());
            rsResultSet.updateDouble("COLUMN_20_AMT",getAttribute("COLUMN_20_AMT").getVal());
            rsResultSet.updateString("COLUMN_20_CAPTION",(String)getAttribute("COLUMN_20_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_21_ID",(int)getAttribute("COLUMN_21_ID").getVal());
            rsResultSet.updateString("COLUMN_21_FORMULA",(String)getAttribute("COLUMN_21_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_21_PER",getAttribute("COLUMN_21_PER").getVal());
            rsResultSet.updateDouble("COLUMN_21_AMT",getAttribute("COLUMN_21_AMT").getVal());
            rsResultSet.updateString("COLUMN_21_CAPTION",(String)getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_22_ID",(int)getAttribute("COLUMN_22_ID").getVal());
            rsResultSet.updateString("COLUMN_22_FORMULA",(String)getAttribute("COLUMN_22_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_22_PER",getAttribute("COLUMN_22_PER").getVal());
            rsResultSet.updateDouble("COLUMN_22_AMT",getAttribute("COLUMN_22_AMT").getVal());
            rsResultSet.updateString("COLUMN_22_CAPTION",(String)getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_23_ID",(int)getAttribute("COLUMN_23_ID").getVal());
            rsResultSet.updateString("COLUMN_23_FORMULA",(String)getAttribute("COLUMN_23_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_23_PER",getAttribute("COLUMN_23_PER").getVal());
            rsResultSet.updateDouble("COLUMN_23_AMT",getAttribute("COLUMN_23_AMT").getVal());
            rsResultSet.updateString("COLUMN_23_CAPTION",(String)getAttribute("COLUMN_23_CAPTION").getObj());
            
            
            rsResultSet.updateInt("COLUMN_24_ID",(int)getAttribute("COLUMN_24_ID").getVal());
            rsResultSet.updateString("COLUMN_24_FORMULA",(String)getAttribute("COLUMN_24_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_24_PER",getAttribute("COLUMN_24_PER").getVal());
            rsResultSet.updateDouble("COLUMN_24_AMT",getAttribute("COLUMN_24_AMT").getVal());
            rsResultSet.updateString("COLUMN_24_CAPTION",(String)getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_25_ID",(int)getAttribute("COLUMN_25_ID").getVal());
            rsResultSet.updateString("COLUMN_25_FORMULA",(String)getAttribute("COLUMN_25_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_25_PER",getAttribute("COLUMN_25_PER").getVal());
            rsResultSet.updateDouble("COLUMN_25_AMT",getAttribute("COLUMN_25_AMT").getVal());
            rsResultSet.updateString("COLUMN_25_CAPTION",(String)getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_26_ID",(int)getAttribute("COLUMN_26_ID").getVal());
            rsResultSet.updateString("COLUMN_26_FORMULA",(String)getAttribute("COLUMN_26_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_26_PER",getAttribute("COLUMN_26_PER").getVal());
            rsResultSet.updateDouble("COLUMN_26_AMT",getAttribute("COLUMN_26_AMT").getVal());
            rsResultSet.updateString("COLUMN_26_CAPTION",(String)getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_27_ID",(int)getAttribute("COLUMN_27_ID").getVal());
            rsResultSet.updateString("COLUMN_27_FORMULA",(String)getAttribute("COLUMN_27_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_27_PER",getAttribute("COLUMN_27_PER").getVal());
            rsResultSet.updateDouble("COLUMN_27_AMT",getAttribute("COLUMN_27_AMT").getVal());
            rsResultSet.updateString("COLUMN_27_CAPTION",(String)getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_28_ID",(int)getAttribute("COLUMN_28_ID").getVal());
            rsResultSet.updateString("COLUMN_28_FORMULA",(String)getAttribute("COLUMN_28_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_28_PER",getAttribute("COLUMN_28_PER").getVal());
            rsResultSet.updateDouble("COLUMN_28_AMT",getAttribute("COLUMN_28_AMT").getVal());
            rsResultSet.updateString("COLUMN_28_CAPTION",(String)getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_29_ID",(int)getAttribute("COLUMN_29_ID").getVal());
            rsResultSet.updateString("COLUMN_29_FORMULA",(String)getAttribute("COLUMN_29_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_29_PER",getAttribute("COLUMN_29_PER").getVal());
            rsResultSet.updateDouble("COLUMN_29_AMT",getAttribute("COLUMN_29_AMT").getVal());
            rsResultSet.updateString("COLUMN_29_CAPTION",(String)getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_30_ID",(int)getAttribute("COLUMN_30_ID").getVal());
            rsResultSet.updateString("COLUMN_30_FORMULA",(String)getAttribute("COLUMN_30_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_30_PER",getAttribute("COLUMN_30_PER").getVal());
            rsResultSet.updateDouble("COLUMN_30_AMT",getAttribute("COLUMN_30_AMT").getVal());
            rsResultSet.updateString("COLUMN_30_CAPTION",(String)getAttribute("COLUMN_30_CAPTION").getObj());
            
            
            
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            
            rsResultSet.updateInt("PF_POST",getAttribute("PF_POST").getInt());
            rsResultSet.updateInt("FREIGHT_POST",getAttribute("FREIGHT_POST").getInt());
            rsResultSet.updateInt("OCTROI_POST",getAttribute("OCTROI_POST").getInt());
            rsResultSet.updateInt("INSURANCE_POST",getAttribute("INSURANCE_POST").getInt());
            rsResultSet.updateInt("CLEARANCE_POST",getAttribute("CLEARANCE_POST").getInt());
            rsResultSet.updateInt("AIR_FREIGHT_POST",getAttribute("AIR_FREIGHT_POST").getInt());
            rsResultSet.updateInt("OTHERS_POST",getAttribute("OTHERS_POST").getInt());
            rsResultSet.updateInt("PAYMENT_TYPE",getAttribute("PAYMENT_TYPE").getInt());
            
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_GRN_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_NO='"+(String)getAttribute("GRN_NO").getObj()+"' AND GRN_TYPE='" + GRNType + "'");
            RevNo++;
            String RevDocNo=(String)getAttribute("GRN_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GRN_NO",(String)getAttribute("GRN_NO").getObj());
            rsHistory.updateString("GRN_DATE",(String)getAttribute("GRN_DATE").getObj());
            rsHistory.updateString("APPROVED_ON",(String)getAttribute("APPROVED_ON").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsHistory.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsHistory.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsHistory.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateDouble("INVOICE_AMOUNT",getAttribute("INVOICE_AMOUNT").getDouble());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateDouble("ACCESSABLE_VALUE",getAttribute("ACCESSABLE_VALUE").getVal());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("CURRENCY_RATE_PAYMENT",getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("GROSS_AMOUNT",getAttribute("GROSS_AMOUNT").getVal());
            rsHistory.updateBoolean("GRN_PENDING",getAttribute("GRN_PENDING").getBool());
            rsHistory.updateInt("GRN_PENDING_REASON",(int)getAttribute("GRN_PENDING_REASON").getVal());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("GRN_TYPE",(int)getAttribute("GRN_TYPE").getVal());
            rsHistory.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsHistory.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
//            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
//            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
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
            
            rsHistory.updateInt("COLUMN_11_ID",(int)getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER",getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT",getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_12_ID",(int)getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER",getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT",getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_13_ID",(int)getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER",getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT",getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_14_ID",(int)getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER",getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT",getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_15_ID",(int)getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER",getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT",getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_16_ID",(int)getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER",getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT",getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_17_ID",(int)getAttribute("COLUMN_17_ID").getVal());
            rsHistory.updateString("COLUMN_17_FORMULA",(String)getAttribute("COLUMN_17_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_17_PER",getAttribute("COLUMN_17_PER").getVal());
            rsHistory.updateDouble("COLUMN_17_AMT",getAttribute("COLUMN_17_AMT").getVal());
            rsHistory.updateString("COLUMN_17_CAPTION",(String)getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_18_ID",(int)getAttribute("COLUMN_18_ID").getVal());
            rsHistory.updateString("COLUMN_18_FORMULA",(String)getAttribute("COLUMN_18_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_18_PER",getAttribute("COLUMN_18_PER").getVal());
            rsHistory.updateDouble("COLUMN_18_AMT",getAttribute("COLUMN_18_AMT").getVal());
            rsHistory.updateString("COLUMN_18_CAPTION",(String)getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_19_ID",(int)getAttribute("COLUMN_19_ID").getVal());
            rsHistory.updateString("COLUMN_19_FORMULA",(String)getAttribute("COLUMN_19_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_19_PER",getAttribute("COLUMN_19_PER").getVal());
            rsHistory.updateDouble("COLUMN_19_AMT",getAttribute("COLUMN_19_AMT").getVal());
            rsHistory.updateString("COLUMN_19_CAPTION",(String)getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_20_ID",(int)getAttribute("COLUMN_20_ID").getVal());
            rsHistory.updateString("COLUMN_20_FORMULA",(String)getAttribute("COLUMN_20_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_20_PER",getAttribute("COLUMN_20_PER").getVal());
            rsHistory.updateDouble("COLUMN_20_AMT",getAttribute("COLUMN_20_AMT").getVal());
            rsHistory.updateString("COLUMN_20_CAPTION",(String)getAttribute("COLUMN_20_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_21_ID",(int)getAttribute("COLUMN_21_ID").getVal());
            rsHistory.updateString("COLUMN_21_FORMULA",(String)getAttribute("COLUMN_21_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_21_PER",getAttribute("COLUMN_21_PER").getVal());
            rsHistory.updateDouble("COLUMN_21_AMT",getAttribute("COLUMN_21_AMT").getVal());
            rsHistory.updateString("COLUMN_21_CAPTION",(String)getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_22_ID",(int)getAttribute("COLUMN_22_ID").getVal());
            rsHistory.updateString("COLUMN_22_FORMULA",(String)getAttribute("COLUMN_22_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_22_PER",getAttribute("COLUMN_22_PER").getVal());
            rsHistory.updateDouble("COLUMN_22_AMT",getAttribute("COLUMN_22_AMT").getVal());
            rsHistory.updateString("COLUMN_22_CAPTION",(String)getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_23_ID",(int)getAttribute("COLUMN_23_ID").getVal());
            rsHistory.updateString("COLUMN_23_FORMULA",(String)getAttribute("COLUMN_23_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_23_PER",getAttribute("COLUMN_23_PER").getVal());
            rsHistory.updateDouble("COLUMN_23_AMT",getAttribute("COLUMN_23_AMT").getVal());
            rsHistory.updateString("COLUMN_23_CAPTION",(String)getAttribute("COLUMN_23_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_24_ID",(int)getAttribute("COLUMN_24_ID").getVal());
            rsHistory.updateString("COLUMN_24_FORMULA",(String)getAttribute("COLUMN_24_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_24_PER",getAttribute("COLUMN_24_PER").getVal());
            rsHistory.updateDouble("COLUMN_24_AMT",getAttribute("COLUMN_24_AMT").getVal());
            rsHistory.updateString("COLUMN_24_CAPTION",(String)getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_25_ID",(int)getAttribute("COLUMN_25_ID").getVal());
            rsHistory.updateString("COLUMN_25_FORMULA",(String)getAttribute("COLUMN_25_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_25_PER",getAttribute("COLUMN_25_PER").getVal());
            rsHistory.updateDouble("COLUMN_25_AMT",getAttribute("COLUMN_25_AMT").getVal());
            rsHistory.updateString("COLUMN_25_CAPTION",(String)getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_26_ID",(int)getAttribute("COLUMN_26_ID").getVal());
            rsHistory.updateString("COLUMN_26_FORMULA",(String)getAttribute("COLUMN_26_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_26_PER",getAttribute("COLUMN_26_PER").getVal());
            rsHistory.updateDouble("COLUMN_26_AMT",getAttribute("COLUMN_26_AMT").getVal());
            rsHistory.updateString("COLUMN_26_CAPTION",(String)getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_27_ID",(int)getAttribute("COLUMN_27_ID").getVal());
            rsHistory.updateString("COLUMN_27_FORMULA",(String)getAttribute("COLUMN_27_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_27_PER",getAttribute("COLUMN_27_PER").getVal());
            rsHistory.updateDouble("COLUMN_27_AMT",getAttribute("COLUMN_27_AMT").getVal());
            rsHistory.updateString("COLUMN_27_CAPTION",(String)getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_28_ID",(int)getAttribute("COLUMN_28_ID").getVal());
            rsHistory.updateString("COLUMN_28_FORMULA",(String)getAttribute("COLUMN_28_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_28_PER",getAttribute("COLUMN_28_PER").getVal());
            rsHistory.updateDouble("COLUMN_28_AMT",getAttribute("COLUMN_28_AMT").getVal());
            rsHistory.updateString("COLUMN_28_CAPTION",(String)getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_29_ID",(int)getAttribute("COLUMN_29_ID").getVal());
            rsHistory.updateString("COLUMN_29_FORMULA",(String)getAttribute("COLUMN_29_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_29_PER",getAttribute("COLUMN_29_PER").getVal());
            rsHistory.updateDouble("COLUMN_29_AMT",getAttribute("COLUMN_29_AMT").getVal());
            rsHistory.updateString("COLUMN_29_CAPTION",(String)getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_30_ID",(int)getAttribute("COLUMN_30_ID").getVal());
            rsHistory.updateString("COLUMN_30_FORMULA",(String)getAttribute("COLUMN_30_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_30_PER",getAttribute("COLUMN_30_PER").getVal());
            rsHistory.updateDouble("COLUMN_30_AMT",getAttribute("COLUMN_30_AMT").getVal());
            rsHistory.updateString("COLUMN_30_CAPTION",(String)getAttribute("COLUMN_30_CAPTION").getObj());
            
            
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsHistory.updateInt("PF_POST",getAttribute("PF_POST").getInt());
            rsHistory.updateInt("FREIGHT_POST",getAttribute("FREIGHT_POST").getInt());
            rsHistory.updateInt("OCTROI_POST",getAttribute("OCTROI_POST").getInt());
            rsHistory.updateInt("INSURANCE_POST",getAttribute("INSURANCE_POST").getInt());
            rsHistory.updateInt("CLEARANCE_POST",getAttribute("CLEARANCE_POST").getInt());
            rsHistory.updateInt("AIR_FREIGHT_POST",getAttribute("AIR_FREIGHT_POST").getInt());
            rsHistory.updateInt("OTHERS_POST",getAttribute("OTHERS_POST").getInt());
            rsHistory.updateInt("PAYMENT_TYPE",getAttribute("PAYMENT_TYPE").getInt());
            
            rsHistory.insertRow();
            
            
            //==== Delete Previous Entries ====//
            GRNNo=(String)getAttribute("GRN_NO").getObj();
            
            data.Execute("DELETE FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_TYPE='" + GRNType + "'");
            data.Execute("DELETE FROM D_INV_GRN_LOT WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_TYPE='" + GRNType + "'");
            
            //====== Now turn of GRN Items ======//
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL WHERE GRN_NO='1'");
            rsItem.first();
            
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_GRN_LOT WHERE GRN_NO='1'");
            rsLot.first();
            
            for(int i=1;i<=colSTMItems.size();i++) {
                clsSTMRawItem ObjItem=(clsSTMRawItem)colSTMItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("GRN_NO",(String)getAttribute("GRN_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("GRN_TYPE",GRNType);
                rsItem.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("MIR_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsItem.updateInt("MIR_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsItem.updateInt("MIR_TYPE",2);
                rsItem.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsItem.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsItem.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("MIR_QTY",ObjItem.getAttribute("MIR_QTY").getVal());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("EXCESS_QTY",ObjItem.getAttribute("EXCESS_QTY").getVal());
                rsItem.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsItem.updateDouble("REJECTED_QTY",ObjItem.getAttribute("REJECTED_QTY").getVal());
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("SHADE",(String)ObjItem.getAttribute("SHADE").getObj());
                rsItem.updateString("W_MIE",(String)ObjItem.getAttribute("W_MIE").getObj());
                rsItem.updateString("NO_CASE",(String)ObjItem.getAttribute("NO_CASE").getObj());
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
//                rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
//                rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
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
                
                /*rsItem.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsItem.updateString("COLUMN_11_FORMULA",(String)ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_11_PER",ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsItem.updateDouble("COLUMN_11_AMT",ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsItem.updateString("COLUMN_11_CAPTION",(String)ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_12_ID",(int)ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsItem.updateString("COLUMN_12_FORMULA",(String)ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_12_PER",ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsItem.updateDouble("COLUMN_12_AMT",ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsItem.updateString("COLUMN_12_CAPTION",(String)ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_13_ID",(int)ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsItem.updateString("COLUMN_13_FORMULA",(String)ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_13_PER",ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsItem.updateDouble("COLUMN_13_AMT",ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsItem.updateString("COLUMN_13_CAPTION",(String)ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_14_ID",(int)ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsItem.updateString("COLUMN_14_FORMULA",(String)ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_14_PER",ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsItem.updateDouble("COLUMN_14_AMT",ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsItem.updateString("COLUMN_14_CAPTION",(String)ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_15_ID",(int)ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsItem.updateString("COLUMN_15_FORMULA",(String)ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_15_PER",ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsItem.updateDouble("COLUMN_15_AMT",ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsItem.updateString("COLUMN_15_CAPTION",(String)ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_16_ID",(int)ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsItem.updateString("COLUMN_16_FORMULA",(String)ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_16_PER",ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsItem.updateDouble("COLUMN_16_AMT",ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsItem.updateString("COLUMN_16_CAPTION",(String)ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_17_ID",(int)ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsItem.updateString("COLUMN_17_FORMULA",(String)ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_17_PER",ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsItem.updateDouble("COLUMN_17_AMT",ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsItem.updateString("COLUMN_17_CAPTION",(String)ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_18_ID",(int)ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsItem.updateString("COLUMN_18_FORMULA",(String)ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_18_PER",ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsItem.updateDouble("COLUMN_18_AMT",ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsItem.updateString("COLUMN_18_CAPTION",(String)ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_19_ID",(int)ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsItem.updateString("COLUMN_19_FORMULA",(String)ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_19_PER",ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsItem.updateDouble("COLUMN_19_AMT",ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsItem.updateString("COLUMN_19_CAPTION",(String)ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_20_ID",(int)ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsItem.updateString("COLUMN_20_FORMULA",(String)ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_20_PER",ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsItem.updateDouble("COLUMN_20_AMT",ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsItem.updateString("COLUMN_20_CAPTION",(String)ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                
                rsItem.updateInt("COLUMN_21_ID",(int)ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsItem.updateString("COLUMN_21_FORMULA",(String)ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_21_PER",ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsItem.updateDouble("COLUMN_21_AMT",ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsItem.updateString("COLUMN_21_CAPTION",(String)ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_22_ID",(int)ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsItem.updateString("COLUMN_22_FORMULA",(String)ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_22_PER",ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsItem.updateDouble("COLUMN_22_AMT",ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsItem.updateString("COLUMN_22_CAPTION",(String)ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_23_ID",(int)ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsItem.updateString("COLUMN_23_FORMULA",(String)ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_23_PER",ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsItem.updateDouble("COLUMN_23_AMT",ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsItem.updateString("COLUMN_23_CAPTION",(String)ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_24_ID",(int)ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsItem.updateString("COLUMN_24_FORMULA",(String)ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_24_PER",ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsItem.updateDouble("COLUMN_24_AMT",ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsItem.updateString("COLUMN_24_CAPTION",(String)ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_25_ID",(int)ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsItem.updateString("COLUMN_25_FORMULA",(String)ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_25_PER",ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsItem.updateDouble("COLUMN_25_AMT",ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsItem.updateString("COLUMN_25_CAPTION",(String)ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_26_ID",(int)ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsItem.updateString("COLUMN_26_FORMULA",(String)ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_26_PER",ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsItem.updateDouble("COLUMN_26_AMT",ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsItem.updateString("COLUMN_26_CAPTION",(String)ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_27_ID",(int)ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsItem.updateString("COLUMN_27_FORMULA",(String)ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_27_PER",ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsItem.updateDouble("COLUMN_27_AMT",ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsItem.updateString("COLUMN_27_CAPTION",(String)ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_28_ID",(int)ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsItem.updateString("COLUMN_28_FORMULA",(String)ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_28_PER",ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsItem.updateDouble("COLUMN_28_AMT",ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsItem.updateString("COLUMN_28_CAPTION",(String)ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_29_ID",(int)ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsItem.updateString("COLUMN_29_FORMULA",(String)ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_29_PER",ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsItem.updateDouble("COLUMN_29_AMT",ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsItem.updateString("COLUMN_29_CAPTION",(String)ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_30_ID",(int)ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsItem.updateString("COLUMN_30_FORMULA",(String)ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_30_PER",ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsItem.updateDouble("COLUMN_30_AMT",ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsItem.updateString("COLUMN_30_CAPTION",(String)ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                */
                
                rsItem.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsItem.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsItem.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsItem.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsItem.updateDouble("EXCESS",ObjItem.getAttribute("EXCESS").getVal());
                rsItem.updateDouble("SHORTAGE",ObjItem.getAttribute("SHORTAGE").getVal());
                rsItem.updateDouble("CHALAN_QTY",ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsItem.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsItem.updateString("BARCODE_TYPE",(String)ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsItem.updateDouble("PO_QTY",ObjItem.getAttribute("PO_QTY").getVal());
                rsItem.updateDouble("RECEIVED_QTY",ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsItem.updateDouble("BALANCE_PO_QTY",ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsItem.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsItem.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("GRN_NO",(String)getAttribute("GRN_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("GRN_TYPE",GRNType);
                rsHDetail.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("MIR_NO",(String)ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_SR_NO",(int)ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE",2);
                rsHDetail.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsHDetail.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("MIR_QTY",ObjItem.getAttribute("MIR_QTY").getVal());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("EXCESS_QTY",ObjItem.getAttribute("EXCESS_QTY").getVal());
                rsHDetail.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateDouble("REJECTED_QTY",ObjItem.getAttribute("REJECTED_QTY").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("SHADE",(String)ObjItem.getAttribute("SHADE").getObj());
                rsHDetail.updateString("W_MIE",(String)ObjItem.getAttribute("W_MIE").getObj());
                rsHDetail.updateString("NO_CASE",(String)ObjItem.getAttribute("NO_CASE").getObj());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
//                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
//                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
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
                
                /*rsHDetail.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA",(String)ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER",ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT",ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION",(String)ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_12_ID",(int)ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA",(String)ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER",ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT",ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION",(String)ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_13_ID",(int)ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA",(String)ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER",ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT",ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION",(String)ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_14_ID",(int)ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA",(String)ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER",ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT",ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION",(String)ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_15_ID",(int)ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA",(String)ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER",ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT",ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION",(String)ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_16_ID",(int)ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA",(String)ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER",ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT",ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION",(String)ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_17_ID",(int)ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsHDetail.updateString("COLUMN_17_FORMULA",(String)ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_17_PER",ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsHDetail.updateDouble("COLUMN_17_AMT",ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsHDetail.updateString("COLUMN_17_CAPTION",(String)ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_18_ID",(int)ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsHDetail.updateString("COLUMN_18_FORMULA",(String)ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_18_PER",ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsHDetail.updateDouble("COLUMN_18_AMT",ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsHDetail.updateString("COLUMN_18_CAPTION",(String)ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_19_ID",(int)ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsHDetail.updateString("COLUMN_19_FORMULA",(String)ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_19_PER",ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsHDetail.updateDouble("COLUMN_19_AMT",ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsHDetail.updateString("COLUMN_19_CAPTION",(String)ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_20_ID",(int)ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsHDetail.updateString("COLUMN_20_FORMULA",(String)ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_20_PER",ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsHDetail.updateDouble("COLUMN_20_AMT",ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsHDetail.updateString("COLUMN_20_CAPTION",(String)ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_21_ID",(int)ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsHDetail.updateString("COLUMN_21_FORMULA",(String)ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_21_PER",ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsHDetail.updateDouble("COLUMN_21_AMT",ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsHDetail.updateString("COLUMN_21_CAPTION",(String)ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_22_ID",(int)ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsHDetail.updateString("COLUMN_22_FORMULA",(String)ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_22_PER",ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsHDetail.updateDouble("COLUMN_22_AMT",ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsHDetail.updateString("COLUMN_22_CAPTION",(String)ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_23_ID",(int)ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsHDetail.updateString("COLUMN_23_FORMULA",(String)ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_23_PER",ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsHDetail.updateDouble("COLUMN_23_AMT",ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsHDetail.updateString("COLUMN_23_CAPTION",(String)ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_24_ID",(int)ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsHDetail.updateString("COLUMN_24_FORMULA",(String)ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_24_PER",ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsHDetail.updateDouble("COLUMN_24_AMT",ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsHDetail.updateString("COLUMN_24_CAPTION",(String)ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_25_ID",(int)ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsHDetail.updateString("COLUMN_25_FORMULA",(String)ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_25_PER",ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsHDetail.updateDouble("COLUMN_25_AMT",ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsHDetail.updateString("COLUMN_25_CAPTION",(String)ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_26_ID",(int)ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsHDetail.updateString("COLUMN_26_FORMULA",(String)ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_26_PER",ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsHDetail.updateDouble("COLUMN_26_AMT",ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsHDetail.updateString("COLUMN_26_CAPTION",(String)ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_27_ID",(int)ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsHDetail.updateString("COLUMN_27_FORMULA",(String)ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_27_PER",ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsHDetail.updateDouble("COLUMN_27_AMT",ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsHDetail.updateString("COLUMN_27_CAPTION",(String)ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_28_ID",(int)ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsHDetail.updateString("COLUMN_28_FORMULA",(String)ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_28_PER",ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsHDetail.updateDouble("COLUMN_28_AMT",ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsHDetail.updateString("COLUMN_28_CAPTION",(String)ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_29_ID",(int)ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsHDetail.updateString("COLUMN_29_FORMULA",(String)ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_29_PER",ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsHDetail.updateDouble("COLUMN_29_AMT",ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsHDetail.updateString("COLUMN_29_CAPTION",(String)ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_30_ID",(int)ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsHDetail.updateString("COLUMN_30_FORMULA",(String)ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_30_PER",ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsHDetail.updateDouble("COLUMN_30_AMT",ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsHDetail.updateString("COLUMN_30_CAPTION",(String)ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                
                
                rsHDetail.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsHDetail.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsHDetail.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsHDetail.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsHDetail.updateDouble("EXCESS",ObjItem.getAttribute("EXCESS").getVal());
                rsHDetail.updateDouble("SHORTAGE",ObjItem.getAttribute("SHORTAGE").getVal());
                rsHDetail.updateDouble("CHALAN_QTY",ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsHDetail.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsHDetail.updateString("BARCODE_TYPE",(String)ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsHDetail.updateDouble("PO_QTY",ObjItem.getAttribute("PO_QTY").getVal());
                rsHDetail.updateDouble("RECEIVED_QTY",ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsHDetail.updateDouble("BALANCE_PO_QTY",ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsHDetail.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsHDetail.insertRow();
                */
                
                //Now insert lot nos.
                for(int l=1;l<=ObjItem.colSTMLot.size();l++) {
                    clsSTMReceiptRawLot ObjLot=(clsSTMReceiptRawLot)ObjItem.colSTMLot.get(Integer.toString(l));
                    
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsLot.updateString("GRN_NO",getAttribute("GRN_NO").getString());
                    rsLot.updateInt("GRN_SR_NO",i);
                    rsLot.updateInt("GRN_TYPE",GRNType);
                    rsLot.updateInt("SR_NO",l);
                    rsLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsLot.updateString("AUTO_LOT_NO",ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsLot.updateDouble("LOT_RECEIVED_QTY",ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble());
                    rsLot.updateDouble("LOT_REJECTED_QTY",ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble());
                    rsLot.updateDouble("LOT_ACCEPTED_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsLot.updateDouble("BALANCE_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsLot.updateBoolean("LOT_CLOSE",false);
                    rsLot.updateBoolean("APPROVED",false);
                    rsLot.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.updateBoolean("CANCELLED",false);
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",RevNo);
                    rsHLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsHLot.updateString("GRN_NO",getAttribute("GRN_NO").getString());
                    rsHLot.updateInt("GRN_SR_NO",i);
                    rsHLot.updateInt("GRN_TYPE",GRNType);
                    rsHLot.updateInt("SR_NO",l);
                    rsHLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsHLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsHLot.updateString("AUTO_LOT_NO",ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsHLot.updateDouble("LOT_RECEIVED_QTY",ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble());
                    rsHLot.updateDouble("LOT_REJECTED_QTY",ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble());
                    rsHLot.updateDouble("LOT_ACCEPTED_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsHLot.updateDouble("BALANCE_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsHLot.updateBoolean("LOT_CLOSE",false);
                    rsHLot.updateBoolean("APPROVED",false);
                    rsHLot.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.updateBoolean("CANCELLED",false);
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=181; //GRN Raw Material
            ObjFlow.DocNo=(String)getAttribute("GRN_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_GRN_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="GRN_NO";
            
            
            //==== Handling Rejected Documents ==========//
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_GRN_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_NO='"+ObjFlow.DocNo+"' AND GRN_TYPE='" + GRNType + "'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=181 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            
            
            //===============Accounts PJV Generation =============//
            try {
                if(AStatus.equals("F")) {
                    String SQL = "SELECT * FROM D_INV_GRN_LOT WHERE GRN_NO='"+GRNNo+"' ORDER BY GRN_NO,GRN_SR_NO,SR_NO";
                    ResultSet rsLotUpdate = data.getResult(SQL);
                    rsLotUpdate.first();
                    while(!rsLotUpdate.isAfterLast()) {
                        int GRNSrNo = rsLotUpdate.getInt("GRN_SR_NO");
                        int SrNo = rsLotUpdate.getInt("SR_NO");
                        String UpdateRecord = "UPDATE D_INV_GRN_LOT SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                        "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+ GRNSrNo +" AND SR_NO="+SrNo+" ";
                        data.Execute(UpdateRecord);

                        UpdateRecord = "UPDATE D_INV_GRN_LOT_H SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                        "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+ GRNSrNo +" AND SR_NO="+SrNo+" ";
                        data.Execute(UpdateRecord);

                        rsLotUpdate.next();
                    }
                }
            }
            catch(Exception e) {
            }
            //====================================================//
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("GRN_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                //Give reverse effect to Stock
                //RevertStockEffect();
                
                data.Execute("DELETE FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+lCompanyID+" AND GRN_NO='"+lDocNo.trim()+"' AND GRN_TYPE='" + GRNType + "'");
                data.Execute("DELETE FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND GRN_NO='"+lDocNo.trim()+"' AND GRN_TYPE='" + GRNType + "'");
                data.Execute("DELETE FROM D_INV_GRN_LOT WHERE COMPANY_ID="+lCompanyID+" GRN_NO='"+lDocNo.trim()+"' AND GRN_TYPE='" + GRNType + "'");
                
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
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "'";
        clsGRN ObjGRN = new clsGRN();
        ObjGRN.Filter(strCondition,pCompanyID);
        return ObjGRN;
    }
    
    public Object getObject(int pCompanyID,String pDocNo,int pType) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE="+pType;
        clsGRN ObjGRN = new clsGRN();
        ObjGRN.Filter(strCondition,pCompanyID,pType);
        return ObjGRN;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_GRN_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GRN_TYPE='" + GRNType + "' AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GRN_NO" ;
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
    
    public boolean Filter(String pCondition,int pCompanyID,String URL) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_GRN_HEADER " + pCondition ;
            Conn=data.getConn(URL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GRN_TYPE='" + GRNType + "' AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GRN_NO" ;
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
    
    public boolean Filter(String pCondition,int pCompanyID,int pType) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_GRN_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GRN_TYPE="+pType+" AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GRN_NO" ;
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
    
    
    public boolean Filter(String pCondition,int pCompanyID,int pType,String URL) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_GRN_HEADER " + pCondition ;
            Conn=data.getConn(URL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GRN_TYPE="+pType+" AND GRN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GRN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GRN_NO" ;
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
        Statement stItem,stLot,stTmp;
        ResultSet rsItem,rsLot,rsTmp;
        String GRNNo="";
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
            setAttribute("GRN_NO",rsResultSet.getString("GRN_NO"));
            setAttribute("GRN_DATE",rsResultSet.getString("GRN_DATE"));
            setAttribute("APPROVED_ON",rsResultSet.getString("APPROVED_ON"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("REFA",rsResultSet.getString("REFA"));
            setAttribute("CHALAN_NO",rsResultSet.getString("CHALAN_NO"));
            setAttribute("CHALAN_DATE",rsResultSet.getString("CHALAN_DATE"));
            setAttribute("LR_NO",rsResultSet.getString("LR_NO"));
            setAttribute("LR_DATE",rsResultSet.getString("LR_DATE"));
            setAttribute("INVOICE_NO",rsResultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE",rsResultSet.getString("INVOICE_DATE"));
            setAttribute("TRANSPORTER",rsResultSet.getInt("TRANSPORTER"));
            setAttribute("TRANSPORTER_NAME",rsResultSet.getString("TRANSPORTER_NAME"));
            setAttribute("GATEPASS_NO",rsResultSet.getString("GATEPASS_NO"));
            setAttribute("IMPORT_CONCESS",rsResultSet.getBoolean("IMPORT_CONCESS"));
            setAttribute("ACCESSABLE_VALUE",rsResultSet.getDouble("ACCESSABLE_VALUE"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("CURRENCY_RATE_PAYMENT",rsResultSet.getDouble("CURRENCY_RATE_PAYMENT"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("GROSS_AMOUNT",rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("GRN_PENDING",rsResultSet.getBoolean("GRN_PENDING"));
            setAttribute("GRN_PENDING_REASON",rsResultSet.getInt("GRN_PENDING_REASON"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CENVATED_ITEMS",rsResultSet.getBoolean("CENVATED_ITEMS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("GRN_TYPE",rsResultSet.getInt("GRN_TYPE"));
            setAttribute("OPEN_STATUS",rsResultSet.getString("OPEN_STATUS"));
            setAttribute("FOR_STORE",rsResultSet.getInt("FOR_STORE"));
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
            
            setAttribute("COLUMN_11_ID",rsResultSet.getInt("COLUMN_11_ID"));
            setAttribute("COLUMN_11_FORMULA",rsResultSet.getString("COLUMN_11_FORMULA"));
            setAttribute("COLUMN_11_PER",rsResultSet.getDouble("COLUMN_11_PER"));
            setAttribute("COLUMN_11_AMT",rsResultSet.getDouble("COLUMN_11_AMT"));
            setAttribute("COLUMN_11_CAPTION",rsResultSet.getString("COLUMN_11_CAPTION"));
            
            setAttribute("COLUMN_12_ID",rsResultSet.getInt("COLUMN_12_ID"));
            setAttribute("COLUMN_12_FORMULA",rsResultSet.getString("COLUMN_12_FORMULA"));
            setAttribute("COLUMN_12_PER",rsResultSet.getDouble("COLUMN_12_PER"));
            setAttribute("COLUMN_12_AMT",rsResultSet.getDouble("COLUMN_12_AMT"));
            setAttribute("COLUMN_12_CAPTION",rsResultSet.getString("COLUMN_12_CAPTION"));
            
            setAttribute("COLUMN_13_ID",rsResultSet.getInt("COLUMN_13_ID"));
            setAttribute("COLUMN_13_FORMULA",rsResultSet.getString("COLUMN_13_FORMULA"));
            setAttribute("COLUMN_13_PER",rsResultSet.getDouble("COLUMN_13_PER"));
            setAttribute("COLUMN_13_AMT",rsResultSet.getDouble("COLUMN_13_AMT"));
            setAttribute("COLUMN_13_CAPTION",rsResultSet.getString("COLUMN_13_CAPTION"));
            
            setAttribute("COLUMN_14_ID",rsResultSet.getInt("COLUMN_14_ID"));
            setAttribute("COLUMN_14_FORMULA",rsResultSet.getString("COLUMN_14_FORMULA"));
            setAttribute("COLUMN_14_PER",rsResultSet.getDouble("COLUMN_14_PER"));
            setAttribute("COLUMN_14_AMT",rsResultSet.getDouble("COLUMN_14_AMT"));
            setAttribute("COLUMN_14_CAPTION",rsResultSet.getString("COLUMN_14_CAPTION"));
            
            setAttribute("COLUMN_15_ID",rsResultSet.getInt("COLUMN_15_ID"));
            setAttribute("COLUMN_15_FORMULA",rsResultSet.getString("COLUMN_15_FORMULA"));
            setAttribute("COLUMN_15_PER",rsResultSet.getDouble("COLUMN_15_PER"));
            setAttribute("COLUMN_15_AMT",rsResultSet.getDouble("COLUMN_15_AMT"));
            setAttribute("COLUMN_15_CAPTION",rsResultSet.getString("COLUMN_15_CAPTION"));
            
            setAttribute("COLUMN_16_ID",rsResultSet.getInt("COLUMN_16_ID"));
            setAttribute("COLUMN_16_FORMULA",rsResultSet.getString("COLUMN_16_FORMULA"));
            setAttribute("COLUMN_16_PER",rsResultSet.getDouble("COLUMN_16_PER"));
            setAttribute("COLUMN_16_AMT",rsResultSet.getDouble("COLUMN_16_AMT"));
            setAttribute("COLUMN_16_CAPTION",rsResultSet.getString("COLUMN_16_CAPTION"));
            
            setAttribute("COLUMN_17_ID",rsResultSet.getInt("COLUMN_17_ID"));
            setAttribute("COLUMN_17_FORMULA",rsResultSet.getString("COLUMN_17_FORMULA"));
            setAttribute("COLUMN_17_PER",rsResultSet.getDouble("COLUMN_17_PER"));
            setAttribute("COLUMN_17_AMT",rsResultSet.getDouble("COLUMN_17_AMT"));
            setAttribute("COLUMN_17_CAPTION",rsResultSet.getString("COLUMN_17_CAPTION"));
            
            setAttribute("COLUMN_18_ID",rsResultSet.getInt("COLUMN_18_ID"));
            setAttribute("COLUMN_18_FORMULA",rsResultSet.getString("COLUMN_18_FORMULA"));
            setAttribute("COLUMN_18_PER",rsResultSet.getDouble("COLUMN_18_PER"));
            setAttribute("COLUMN_18_AMT",rsResultSet.getDouble("COLUMN_18_AMT"));
            setAttribute("COLUMN_18_CAPTION",rsResultSet.getString("COLUMN_18_CAPTION"));
            
            setAttribute("COLUMN_19_ID",rsResultSet.getInt("COLUMN_19_ID"));
            setAttribute("COLUMN_19_FORMULA",rsResultSet.getString("COLUMN_19_FORMULA"));
            setAttribute("COLUMN_19_PER",rsResultSet.getDouble("COLUMN_19_PER"));
            setAttribute("COLUMN_19_AMT",rsResultSet.getDouble("COLUMN_19_AMT"));
            setAttribute("COLUMN_19_CAPTION",rsResultSet.getString("COLUMN_19_CAPTION"));
            
            setAttribute("COLUMN_20_ID",rsResultSet.getInt("COLUMN_20_ID"));
            setAttribute("COLUMN_20_FORMULA",rsResultSet.getString("COLUMN_20_FORMULA"));
            setAttribute("COLUMN_20_PER",rsResultSet.getDouble("COLUMN_20_PER"));
            setAttribute("COLUMN_20_AMT",rsResultSet.getDouble("COLUMN_20_AMT"));
            setAttribute("COLUMN_20_CAPTION",rsResultSet.getString("COLUMN_20_CAPTION"));
            
            setAttribute("COLUMN_21_ID",rsResultSet.getInt("COLUMN_21_ID"));
            setAttribute("COLUMN_21_FORMULA",rsResultSet.getString("COLUMN_21_FORMULA"));
            setAttribute("COLUMN_21_PER",rsResultSet.getDouble("COLUMN_21_PER"));
            setAttribute("COLUMN_21_AMT",rsResultSet.getDouble("COLUMN_21_AMT"));
            setAttribute("COLUMN_21_CAPTION",rsResultSet.getString("COLUMN_21_CAPTION"));
            
            setAttribute("COLUMN_22_ID",rsResultSet.getInt("COLUMN_22_ID"));
            setAttribute("COLUMN_22_FORMULA",rsResultSet.getString("COLUMN_22_FORMULA"));
            setAttribute("COLUMN_22_PER",rsResultSet.getDouble("COLUMN_22_PER"));
            setAttribute("COLUMN_22_AMT",rsResultSet.getDouble("COLUMN_22_AMT"));
            setAttribute("COLUMN_22_CAPTION",rsResultSet.getString("COLUMN_22_CAPTION"));
            
            setAttribute("COLUMN_23_ID",rsResultSet.getInt("COLUMN_23_ID"));
            setAttribute("COLUMN_23_FORMULA",rsResultSet.getString("COLUMN_23_FORMULA"));
            setAttribute("COLUMN_23_PER",rsResultSet.getDouble("COLUMN_23_PER"));
            setAttribute("COLUMN_23_AMT",rsResultSet.getDouble("COLUMN_23_AMT"));
            setAttribute("COLUMN_23_CAPTION",rsResultSet.getString("COLUMN_23_CAPTION"));
            
            setAttribute("COLUMN_24_ID",rsResultSet.getInt("COLUMN_24_ID"));
            setAttribute("COLUMN_24_FORMULA",rsResultSet.getString("COLUMN_24_FORMULA"));
            setAttribute("COLUMN_24_PER",rsResultSet.getDouble("COLUMN_24_PER"));
            setAttribute("COLUMN_24_AMT",rsResultSet.getDouble("COLUMN_24_AMT"));
            setAttribute("COLUMN_24_CAPTION",rsResultSet.getString("COLUMN_24_CAPTION"));
            
            setAttribute("COLUMN_25_ID",rsResultSet.getInt("COLUMN_25_ID"));
            setAttribute("COLUMN_25_FORMULA",rsResultSet.getString("COLUMN_25_FORMULA"));
            setAttribute("COLUMN_25_PER",rsResultSet.getDouble("COLUMN_25_PER"));
            setAttribute("COLUMN_25_AMT",rsResultSet.getDouble("COLUMN_25_AMT"));
            setAttribute("COLUMN_25_CAPTION",rsResultSet.getString("COLUMN_25_CAPTION"));
            
            setAttribute("COLUMN_26_ID",rsResultSet.getInt("COLUMN_26_ID"));
            setAttribute("COLUMN_26_FORMULA",rsResultSet.getString("COLUMN_26_FORMULA"));
            setAttribute("COLUMN_26_PER",rsResultSet.getDouble("COLUMN_26_PER"));
            setAttribute("COLUMN_26_AMT",rsResultSet.getDouble("COLUMN_26_AMT"));
            setAttribute("COLUMN_26_CAPTION",rsResultSet.getString("COLUMN_26_CAPTION"));
            
            setAttribute("COLUMN_27_ID",rsResultSet.getInt("COLUMN_27_ID"));
            setAttribute("COLUMN_27_FORMULA",rsResultSet.getString("COLUMN_27_FORMULA"));
            setAttribute("COLUMN_27_PER",rsResultSet.getDouble("COLUMN_27_PER"));
            setAttribute("COLUMN_27_AMT",rsResultSet.getDouble("COLUMN_27_AMT"));
            setAttribute("COLUMN_27_CAPTION",rsResultSet.getString("COLUMN_27_CAPTION"));
            
            setAttribute("COLUMN_28_ID",rsResultSet.getInt("COLUMN_28_ID"));
            setAttribute("COLUMN_28_FORMULA",rsResultSet.getString("COLUMN_28_FORMULA"));
            setAttribute("COLUMN_28_PER",rsResultSet.getDouble("COLUMN_28_PER"));
            setAttribute("COLUMN_28_AMT",rsResultSet.getDouble("COLUMN_28_AMT"));
            setAttribute("COLUMN_28_CAPTION",rsResultSet.getString("COLUMN_28_CAPTION"));
            
            setAttribute("COLUMN_29_ID",rsResultSet.getInt("COLUMN_29_ID"));
            setAttribute("COLUMN_29_FORMULA",rsResultSet.getString("COLUMN_29_FORMULA"));
            setAttribute("COLUMN_29_PER",rsResultSet.getDouble("COLUMN_29_PER"));
            setAttribute("COLUMN_29_AMT",rsResultSet.getDouble("COLUMN_29_AMT"));
            setAttribute("COLUMN_29_CAPTION",rsResultSet.getString("COLUMN_29_CAPTION"));
            
            setAttribute("COLUMN_30_ID",rsResultSet.getInt("COLUMN_30_ID"));
            setAttribute("COLUMN_30_FORMULA",rsResultSet.getString("COLUMN_30_FORMULA"));
            setAttribute("COLUMN_30_PER",rsResultSet.getDouble("COLUMN_30_PER"));
            setAttribute("COLUMN_30_AMT",rsResultSet.getDouble("COLUMN_30_AMT"));
            setAttribute("COLUMN_30_CAPTION",rsResultSet.getString("COLUMN_30_CAPTION"));
            
            
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            
            setAttribute("PF_POST",rsResultSet.getInt("PF_POST"));
            setAttribute("FREIGHT_POST",rsResultSet.getInt("FREIGHT_POST"));
            setAttribute("OCTROI_POST",rsResultSet.getInt("OCTROI_POST"));
            setAttribute("INSURANCE_POST",rsResultSet.getInt("INSURANCE_POST"));
            setAttribute("CLEARANCE_POST",rsResultSet.getInt("CLEARANCE_POST"));
            setAttribute("AIR_FREIGHT_POST",rsResultSet.getInt("AIR_FREIGHT_POST"));
            setAttribute("OTHERS_POST",rsResultSet.getInt("OTHERS_POST"));
            setAttribute("PAYMENT_TYPE",rsResultSet.getInt("PAYMENT_TYPE"));
            setAttribute("INVOICE_AMOUNT",rsResultSet.getDouble("INVOICE_AMOUNT"));
            
            colSTMItems.clear(); //Clear existing data
            
            GRNNo=(String)getAttribute("GRN_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL_H WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_TYPE='" + GRNType + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_TYPE='" + GRNType + "' ORDER BY SR_NO");
            }
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                //new
                clsSTMRawItem ObjItem=new clsSTMRawItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("GRN_NO",rsItem.getString("GRN_NO"));
                ObjItem.setAttribute("GRN_TYPE",rsItem.getInt("GRN_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("MIR_NO",rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("MIR_SR_NO",rsItem.getInt("MIR_SR_NO"));
                ObjItem.setAttribute("MIR_TYPE",rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_SR_NO",rsItem.getInt("PO_SR_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("MIR_QTY",rsItem.getDouble("MIR_QTY"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("EXCESS_QTY",rsItem.getDouble("EXCESS_QTY"));
                ObjItem.setAttribute("PO_QTY",rsItem.getDouble("PO_QTY"));
                ObjItem.setAttribute("RECEIVED_QTY",rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("BALANCE_PO_QTY",rsItem.getDouble("BALANCE_PO_QTY"));
                ObjItem.setAttribute("REJECTED_REASON_ID",rsItem.getInt("REJECTED_REASON_ID"));
                ObjItem.setAttribute("DEPT_ID",rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsItem.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("TOLERANCE_LIMIT",rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("REJECTED_QTY",rsItem.getDouble("REJECTED_QTY"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE",rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("TOTAL_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("SHADE",rsItem.getString("SHADE"));
                ObjItem.setAttribute("W_MIE",rsItem.getString("W_MIE"));
                ObjItem.setAttribute("NO_CASE",rsItem.getString("NO_CASE"));
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN",rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS",rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
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
                
                ObjItem.setAttribute("COLUMN_11_ID",rsItem.getInt("COLUMN_11_ID"));
                ObjItem.setAttribute("COLUMN_11_FORMULA",rsItem.getString("COLUMN_11_FORMULA"));
                ObjItem.setAttribute("COLUMN_11_PER",rsItem.getDouble("COLUMN_11_PER"));
                ObjItem.setAttribute("COLUMN_11_AMT",rsItem.getDouble("COLUMN_11_AMT"));
                ObjItem.setAttribute("COLUMN_11_CAPTION",rsItem.getString("COLUMN_11_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_12_ID",rsItem.getInt("COLUMN_12_ID"));
                ObjItem.setAttribute("COLUMN_12_FORMULA",rsItem.getString("COLUMN_12_FORMULA"));
                ObjItem.setAttribute("COLUMN_12_PER",rsItem.getDouble("COLUMN_12_PER"));
                ObjItem.setAttribute("COLUMN_12_AMT",rsItem.getDouble("COLUMN_12_AMT"));
                ObjItem.setAttribute("COLUMN_12_CAPTION",rsItem.getString("COLUMN_12_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_13_ID",rsItem.getInt("COLUMN_13_ID"));
                ObjItem.setAttribute("COLUMN_13_FORMULA",rsItem.getString("COLUMN_13_FORMULA"));
                ObjItem.setAttribute("COLUMN_13_PER",rsItem.getDouble("COLUMN_13_PER"));
                ObjItem.setAttribute("COLUMN_13_AMT",rsItem.getDouble("COLUMN_13_AMT"));
                ObjItem.setAttribute("COLUMN_13_CAPTION",rsItem.getString("COLUMN_13_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_14_ID",rsItem.getInt("COLUMN_14_ID"));
                ObjItem.setAttribute("COLUMN_14_FORMULA",rsItem.getString("COLUMN_14_FORMULA"));
                ObjItem.setAttribute("COLUMN_14_PER",rsItem.getDouble("COLUMN_14_PER"));
                ObjItem.setAttribute("COLUMN_14_AMT",rsItem.getDouble("COLUMN_14_AMT"));
                ObjItem.setAttribute("COLUMN_14_CAPTION",rsItem.getString("COLUMN_14_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_15_ID",rsItem.getInt("COLUMN_15_ID"));
                ObjItem.setAttribute("COLUMN_15_FORMULA",rsItem.getString("COLUMN_15_FORMULA"));
                ObjItem.setAttribute("COLUMN_15_PER",rsItem.getDouble("COLUMN_15_PER"));
                ObjItem.setAttribute("COLUMN_15_AMT",rsItem.getDouble("COLUMN_15_AMT"));
                ObjItem.setAttribute("COLUMN_15_CAPTION",rsItem.getString("COLUMN_15_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_16_ID",rsItem.getInt("COLUMN_16_ID"));
                ObjItem.setAttribute("COLUMN_16_FORMULA",rsItem.getString("COLUMN_16_FORMULA"));
                ObjItem.setAttribute("COLUMN_16_PER",rsItem.getDouble("COLUMN_16_PER"));
                ObjItem.setAttribute("COLUMN_16_AMT",rsItem.getDouble("COLUMN_16_AMT"));
                ObjItem.setAttribute("COLUMN_16_CAPTION",rsItem.getString("COLUMN_16_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_17_ID",rsItem.getInt("COLUMN_17_ID"));
                ObjItem.setAttribute("COLUMN_17_FORMULA",rsItem.getString("COLUMN_17_FORMULA"));
                ObjItem.setAttribute("COLUMN_17_PER",rsItem.getDouble("COLUMN_17_PER"));
                ObjItem.setAttribute("COLUMN_17_AMT",rsItem.getDouble("COLUMN_17_AMT"));
                ObjItem.setAttribute("COLUMN_17_CAPTION",rsItem.getString("COLUMN_17_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_18_ID",rsItem.getInt("COLUMN_18_ID"));
                ObjItem.setAttribute("COLUMN_18_FORMULA",rsItem.getString("COLUMN_18_FORMULA"));
                ObjItem.setAttribute("COLUMN_18_PER",rsItem.getDouble("COLUMN_18_PER"));
                ObjItem.setAttribute("COLUMN_18_AMT",rsItem.getDouble("COLUMN_18_AMT"));
                ObjItem.setAttribute("COLUMN_18_CAPTION",rsItem.getString("COLUMN_18_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_19_ID",rsItem.getInt("COLUMN_19_ID"));
                ObjItem.setAttribute("COLUMN_19_FORMULA",rsItem.getString("COLUMN_19_FORMULA"));
                ObjItem.setAttribute("COLUMN_19_PER",rsItem.getDouble("COLUMN_19_PER"));
                ObjItem.setAttribute("COLUMN_19_AMT",rsItem.getDouble("COLUMN_19_AMT"));
                ObjItem.setAttribute("COLUMN_19_CAPTION",rsItem.getString("COLUMN_19_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_20_ID",rsItem.getInt("COLUMN_20_ID"));
                ObjItem.setAttribute("COLUMN_20_FORMULA",rsItem.getString("COLUMN_20_FORMULA"));
                ObjItem.setAttribute("COLUMN_20_PER",rsItem.getDouble("COLUMN_20_PER"));
                ObjItem.setAttribute("COLUMN_20_AMT",rsItem.getDouble("COLUMN_20_AMT"));
                ObjItem.setAttribute("COLUMN_20_CAPTION",rsItem.getString("COLUMN_20_CAPTION"));
                
                
                ObjItem.setAttribute("COLUMN_21_ID",rsItem.getInt("COLUMN_21_ID"));
                ObjItem.setAttribute("COLUMN_21_FORMULA",rsItem.getString("COLUMN_21_FORMULA"));
                ObjItem.setAttribute("COLUMN_21_PER",rsItem.getDouble("COLUMN_21_PER"));
                ObjItem.setAttribute("COLUMN_21_AMT",rsItem.getDouble("COLUMN_21_AMT"));
                ObjItem.setAttribute("COLUMN_21_CAPTION",rsItem.getString("COLUMN_21_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_22_ID",rsItem.getInt("COLUMN_22_ID"));
                ObjItem.setAttribute("COLUMN_22_FORMULA",rsItem.getString("COLUMN_22_FORMULA"));
                ObjItem.setAttribute("COLUMN_22_PER",rsItem.getDouble("COLUMN_22_PER"));
                ObjItem.setAttribute("COLUMN_22_AMT",rsItem.getDouble("COLUMN_22_AMT"));
                ObjItem.setAttribute("COLUMN_22_CAPTION",rsItem.getString("COLUMN_22_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_23_ID",rsItem.getInt("COLUMN_23_ID"));
                ObjItem.setAttribute("COLUMN_23_FORMULA",rsItem.getString("COLUMN_23_FORMULA"));
                ObjItem.setAttribute("COLUMN_23_PER",rsItem.getDouble("COLUMN_23_PER"));
                ObjItem.setAttribute("COLUMN_23_AMT",rsItem.getDouble("COLUMN_23_AMT"));
                ObjItem.setAttribute("COLUMN_23_CAPTION",rsItem.getString("COLUMN_23_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_24_ID",rsItem.getInt("COLUMN_24_ID"));
                ObjItem.setAttribute("COLUMN_24_FORMULA",rsItem.getString("COLUMN_24_FORMULA"));
                ObjItem.setAttribute("COLUMN_24_PER",rsItem.getDouble("COLUMN_24_PER"));
                ObjItem.setAttribute("COLUMN_24_AMT",rsItem.getDouble("COLUMN_24_AMT"));
                ObjItem.setAttribute("COLUMN_24_CAPTION",rsItem.getString("COLUMN_24_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_25_ID",rsItem.getInt("COLUMN_25_ID"));
                ObjItem.setAttribute("COLUMN_25_FORMULA",rsItem.getString("COLUMN_25_FORMULA"));
                ObjItem.setAttribute("COLUMN_25_PER",rsItem.getDouble("COLUMN_25_PER"));
                ObjItem.setAttribute("COLUMN_25_AMT",rsItem.getDouble("COLUMN_25_AMT"));
                ObjItem.setAttribute("COLUMN_25_CAPTION",rsItem.getString("COLUMN_25_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_26_ID",rsItem.getInt("COLUMN_26_ID"));
                ObjItem.setAttribute("COLUMN_26_FORMULA",rsItem.getString("COLUMN_26_FORMULA"));
                ObjItem.setAttribute("COLUMN_26_PER",rsItem.getDouble("COLUMN_26_PER"));
                ObjItem.setAttribute("COLUMN_26_AMT",rsItem.getDouble("COLUMN_26_AMT"));
                ObjItem.setAttribute("COLUMN_26_CAPTION",rsItem.getString("COLUMN_26_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_27_ID",rsItem.getInt("COLUMN_27_ID"));
                ObjItem.setAttribute("COLUMN_27_FORMULA",rsItem.getString("COLUMN_27_FORMULA"));
                ObjItem.setAttribute("COLUMN_27_PER",rsItem.getDouble("COLUMN_27_PER"));
                ObjItem.setAttribute("COLUMN_27_AMT",rsItem.getDouble("COLUMN_27_AMT"));
                ObjItem.setAttribute("COLUMN_27_CAPTION",rsItem.getString("COLUMN_27_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_28_ID",rsItem.getInt("COLUMN_28_ID"));
                ObjItem.setAttribute("COLUMN_28_FORMULA",rsItem.getString("COLUMN_28_FORMULA"));
                ObjItem.setAttribute("COLUMN_28_PER",rsItem.getDouble("COLUMN_28_PER"));
                ObjItem.setAttribute("COLUMN_28_AMT",rsItem.getDouble("COLUMN_28_AMT"));
                ObjItem.setAttribute("COLUMN_28_CAPTION",rsItem.getString("COLUMN_28_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_29_ID",rsItem.getInt("COLUMN_29_ID"));
                ObjItem.setAttribute("COLUMN_29_FORMULA",rsItem.getString("COLUMN_29_FORMULA"));
                ObjItem.setAttribute("COLUMN_29_PER",rsItem.getDouble("COLUMN_29_PER"));
                ObjItem.setAttribute("COLUMN_29_AMT",rsItem.getDouble("COLUMN_29_AMT"));
                ObjItem.setAttribute("COLUMN_29_CAPTION",rsItem.getString("COLUMN_29_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_30_ID",rsItem.getInt("COLUMN_30_ID"));
                ObjItem.setAttribute("COLUMN_30_FORMULA",rsItem.getString("COLUMN_30_FORMULA"));
                ObjItem.setAttribute("COLUMN_30_PER",rsItem.getDouble("COLUMN_30_PER"));
                ObjItem.setAttribute("COLUMN_30_AMT",rsItem.getDouble("COLUMN_30_AMT"));
                ObjItem.setAttribute("COLUMN_30_CAPTION",rsItem.getString("COLUMN_30_CAPTION"));
                
                
                ObjItem.setAttribute("MATERIAL_CODE",rsItem.getString("MATERIAL_CODE"));
                ObjItem.setAttribute("MATERIAL_DESC",rsItem.getString("MATERIAL_DESC"));
                ObjItem.setAttribute("QUALITY_NO",rsItem.getString("QUALITY_NO"));
                ObjItem.setAttribute("PAGE_NO",rsItem.getString("PAGE_NO"));
                ObjItem.setAttribute("EXCESS",rsItem.getDouble("EXCESS"));
                ObjItem.setAttribute("SHORTAGE",rsItem.getDouble("SHORTAGE"));
                ObjItem.setAttribute("CHALAN_QTY",rsItem.getDouble("CHALAN_QTY"));
                ObjItem.setAttribute("L_F_NO",rsItem.getString("L_F_NO"));
                ObjItem.setAttribute("BARCODE_TYPE",rsItem.getString("BARCODE_TYPE"));
                ObjItem.setAttribute("RND_DEDUCTION_REASON",rsItem.getString("RND_DEDUCTION_REASON"));
                
                //== Insert Lot Nos. ==
                stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                if(HistoryView) {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_GRN_LOT_H WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+SrNo+" AND GRN_TYPE='" + GRNType + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
                }
                else {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_GRN_LOT WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+SrNo+" AND GRN_TYPE='" + GRNType + "' ORDER BY SR_NO");
                }
                
                rsLot.first();
                
                LotCounter=0;
                while((!rsLot.isAfterLast())&&(rsLot.getRow()>0)) {
                    LotCounter=LotCounter+1;
                    
                    clsSTMReceiptRawLot ObjLot=new clsSTMReceiptRawLot();
                    ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("GRN_NO",rsLot.getString("GRN_NO"));
                    ObjLot.setAttribute("GRN_SR_NO",rsLot.getInt("GRN_SR_NO"));
                    ObjLot.setAttribute("GRN_TYPE",rsLot.getInt("GRN_TYPE"));
                    ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("ITEM_ID",rsLot.getString("ITEM_ID"));
                    ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                    ObjLot.setAttribute("AUTO_LOT_NO",rsLot.getString("AUTO_LOT_NO"));
                    ObjLot.setAttribute("LOT_RECEIVED_QTY",rsLot.getDouble("LOT_RECEIVED_QTY"));
                    ObjLot.setAttribute("LOT_REJECTED_QTY",rsLot.getDouble("LOT_REJECTED_QTY"));
                    ObjLot.setAttribute("LOT_ACCEPTED_QTY",rsLot.getDouble("LOT_ACCEPTED_QTY"));
                    ObjLot.setAttribute("BALANCE_QTY",rsLot.getDouble("BALANCE_QTY"));
                    ObjLot.setAttribute("LOT_CLOSE",rsLot.getBoolean("LOT_CLOSE"));
                    
                    ObjItem.colSTMLot.put(Integer.toString(LotCounter),ObjLot);
                    rsLot.next();
                }
                
                colSTMItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            //e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean setData(int pType) {
        Statement stItem,stLot,stTmp;
        ResultSet rsItem,rsLot,rsTmp;
        String GRNNo="";
        int CompanyID=0,ItemCounter=0,LotCounter=0,SrNo=0;
        
        try {
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("GRN_NO",rsResultSet.getString("GRN_NO"));
            setAttribute("GRN_DATE",rsResultSet.getString("GRN_DATE"));
            setAttribute("APPROVED_ON",rsResultSet.getString("APPROVED_ON"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("REFA",rsResultSet.getString("REFA"));
            setAttribute("CHALAN_NO",rsResultSet.getString("CHALAN_NO"));
            setAttribute("CHALAN_DATE",rsResultSet.getString("CHALAN_DATE"));
            setAttribute("LR_NO",rsResultSet.getString("LR_NO"));
            setAttribute("LR_DATE",rsResultSet.getString("LR_DATE"));
            setAttribute("INVOICE_NO",rsResultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE",rsResultSet.getString("INVOICE_DATE"));
            setAttribute("TRANSPORTER",rsResultSet.getInt("TRANSPORTER"));
            setAttribute("TRANSPORTER_NAME",rsResultSet.getString("TRANSPORTER_NAME"));
            setAttribute("GATEPASS_NO",rsResultSet.getString("GATEPASS_NO"));
            setAttribute("IMPORT_CONCESS",rsResultSet.getBoolean("IMPORT_CONCESS"));
            setAttribute("ACCESSABLE_VALUE",rsResultSet.getDouble("ACCESSABLE_VALUE"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("CURRENCY_RATE_PAYMENT",rsResultSet.getDouble("CURRENCY_RATE_PAYMENT"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("GROSS_AMOUNT",rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("GRN_PENDING",rsResultSet.getBoolean("GRN_PENDING"));
            setAttribute("GRN_PENDING_REASON",rsResultSet.getInt("GRN_PENDING_REASON"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CENVATED_ITEMS",rsResultSet.getBoolean("CENVATED_ITEMS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("GRN_TYPE",rsResultSet.getInt("GRN_TYPE"));
            setAttribute("OPEN_STATUS",rsResultSet.getString("OPEN_STATUS"));
            setAttribute("FOR_STORE",rsResultSet.getInt("FOR_STORE"));
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
            
            setAttribute("COLUMN_11_ID",rsResultSet.getInt("COLUMN_11_ID"));
            setAttribute("COLUMN_11_FORMULA",rsResultSet.getString("COLUMN_11_FORMULA"));
            setAttribute("COLUMN_11_PER",rsResultSet.getDouble("COLUMN_11_PER"));
            setAttribute("COLUMN_11_AMT",rsResultSet.getDouble("COLUMN_11_AMT"));
            setAttribute("COLUMN_11_CAPTION",rsResultSet.getString("COLUMN_11_CAPTION"));
            
            setAttribute("COLUMN_12_ID",rsResultSet.getInt("COLUMN_12_ID"));
            setAttribute("COLUMN_12_FORMULA",rsResultSet.getString("COLUMN_12_FORMULA"));
            setAttribute("COLUMN_12_PER",rsResultSet.getDouble("COLUMN_12_PER"));
            setAttribute("COLUMN_12_AMT",rsResultSet.getDouble("COLUMN_12_AMT"));
            setAttribute("COLUMN_12_CAPTION",rsResultSet.getString("COLUMN_12_CAPTION"));
            
            setAttribute("COLUMN_13_ID",rsResultSet.getInt("COLUMN_13_ID"));
            setAttribute("COLUMN_13_FORMULA",rsResultSet.getString("COLUMN_13_FORMULA"));
            setAttribute("COLUMN_13_PER",rsResultSet.getDouble("COLUMN_13_PER"));
            setAttribute("COLUMN_13_AMT",rsResultSet.getDouble("COLUMN_13_AMT"));
            setAttribute("COLUMN_13_CAPTION",rsResultSet.getString("COLUMN_13_CAPTION"));
            
            setAttribute("COLUMN_14_ID",rsResultSet.getInt("COLUMN_14_ID"));
            setAttribute("COLUMN_14_FORMULA",rsResultSet.getString("COLUMN_14_FORMULA"));
            setAttribute("COLUMN_14_PER",rsResultSet.getDouble("COLUMN_14_PER"));
            setAttribute("COLUMN_14_AMT",rsResultSet.getDouble("COLUMN_14_AMT"));
            setAttribute("COLUMN_14_CAPTION",rsResultSet.getString("COLUMN_14_CAPTION"));
            
            setAttribute("COLUMN_15_ID",rsResultSet.getInt("COLUMN_15_ID"));
            setAttribute("COLUMN_15_FORMULA",rsResultSet.getString("COLUMN_15_FORMULA"));
            setAttribute("COLUMN_15_PER",rsResultSet.getDouble("COLUMN_15_PER"));
            setAttribute("COLUMN_15_AMT",rsResultSet.getDouble("COLUMN_15_AMT"));
            setAttribute("COLUMN_15_CAPTION",rsResultSet.getString("COLUMN_15_CAPTION"));
            
            setAttribute("COLUMN_16_ID",rsResultSet.getInt("COLUMN_16_ID"));
            setAttribute("COLUMN_16_FORMULA",rsResultSet.getString("COLUMN_16_FORMULA"));
            setAttribute("COLUMN_16_PER",rsResultSet.getDouble("COLUMN_16_PER"));
            setAttribute("COLUMN_16_AMT",rsResultSet.getDouble("COLUMN_16_AMT"));
            setAttribute("COLUMN_16_CAPTION",rsResultSet.getString("COLUMN_16_CAPTION"));
            
            setAttribute("COLUMN_17_ID",rsResultSet.getInt("COLUMN_17_ID"));
            setAttribute("COLUMN_17_FORMULA",rsResultSet.getString("COLUMN_17_FORMULA"));
            setAttribute("COLUMN_17_PER",rsResultSet.getDouble("COLUMN_17_PER"));
            setAttribute("COLUMN_17_AMT",rsResultSet.getDouble("COLUMN_17_AMT"));
            setAttribute("COLUMN_17_CAPTION",rsResultSet.getString("COLUMN_17_CAPTION"));
            
            setAttribute("COLUMN_18_ID",rsResultSet.getInt("COLUMN_18_ID"));
            setAttribute("COLUMN_18_FORMULA",rsResultSet.getString("COLUMN_18_FORMULA"));
            setAttribute("COLUMN_18_PER",rsResultSet.getDouble("COLUMN_18_PER"));
            setAttribute("COLUMN_18_AMT",rsResultSet.getDouble("COLUMN_18_AMT"));
            setAttribute("COLUMN_18_CAPTION",rsResultSet.getString("COLUMN_18_CAPTION"));
            
            setAttribute("COLUMN_19_ID",rsResultSet.getInt("COLUMN_19_ID"));
            setAttribute("COLUMN_19_FORMULA",rsResultSet.getString("COLUMN_19_FORMULA"));
            setAttribute("COLUMN_19_PER",rsResultSet.getDouble("COLUMN_19_PER"));
            setAttribute("COLUMN_19_AMT",rsResultSet.getDouble("COLUMN_19_AMT"));
            setAttribute("COLUMN_19_CAPTION",rsResultSet.getString("COLUMN_19_CAPTION"));
            
            setAttribute("COLUMN_20_ID",rsResultSet.getInt("COLUMN_20_ID"));
            setAttribute("COLUMN_20_FORMULA",rsResultSet.getString("COLUMN_20_FORMULA"));
            setAttribute("COLUMN_20_PER",rsResultSet.getDouble("COLUMN_20_PER"));
            setAttribute("COLUMN_20_AMT",rsResultSet.getDouble("COLUMN_20_AMT"));
            setAttribute("COLUMN_20_CAPTION",rsResultSet.getString("COLUMN_20_CAPTION"));
            
            
            setAttribute("COLUMN_21_ID",rsResultSet.getInt("COLUMN_21_ID"));
            setAttribute("COLUMN_21_FORMULA",rsResultSet.getString("COLUMN_21_FORMULA"));
            setAttribute("COLUMN_21_PER",rsResultSet.getDouble("COLUMN_21_PER"));
            setAttribute("COLUMN_21_AMT",rsResultSet.getDouble("COLUMN_21_AMT"));
            setAttribute("COLUMN_21_CAPTION",rsResultSet.getString("COLUMN_21_CAPTION"));
            
            setAttribute("COLUMN_22_ID",rsResultSet.getInt("COLUMN_22_ID"));
            setAttribute("COLUMN_22_FORMULA",rsResultSet.getString("COLUMN_22_FORMULA"));
            setAttribute("COLUMN_22_PER",rsResultSet.getDouble("COLUMN_22_PER"));
            setAttribute("COLUMN_22_AMT",rsResultSet.getDouble("COLUMN_22_AMT"));
            setAttribute("COLUMN_22_CAPTION",rsResultSet.getString("COLUMN_22_CAPTION"));
            
            setAttribute("COLUMN_23_ID",rsResultSet.getInt("COLUMN_23_ID"));
            setAttribute("COLUMN_23_FORMULA",rsResultSet.getString("COLUMN_23_FORMULA"));
            setAttribute("COLUMN_23_PER",rsResultSet.getDouble("COLUMN_23_PER"));
            setAttribute("COLUMN_23_AMT",rsResultSet.getDouble("COLUMN_23_AMT"));
            setAttribute("COLUMN_23_CAPTION",rsResultSet.getString("COLUMN_23_CAPTION"));
            
            setAttribute("COLUMN_24_ID",rsResultSet.getInt("COLUMN_24_ID"));
            setAttribute("COLUMN_24_FORMULA",rsResultSet.getString("COLUMN_24_FORMULA"));
            setAttribute("COLUMN_24_PER",rsResultSet.getDouble("COLUMN_24_PER"));
            setAttribute("COLUMN_24_AMT",rsResultSet.getDouble("COLUMN_24_AMT"));
            setAttribute("COLUMN_24_CAPTION",rsResultSet.getString("COLUMN_24_CAPTION"));
            
            setAttribute("COLUMN_25_ID",rsResultSet.getInt("COLUMN_25_ID"));
            setAttribute("COLUMN_25_FORMULA",rsResultSet.getString("COLUMN_25_FORMULA"));
            setAttribute("COLUMN_25_PER",rsResultSet.getDouble("COLUMN_25_PER"));
            setAttribute("COLUMN_25_AMT",rsResultSet.getDouble("COLUMN_25_AMT"));
            setAttribute("COLUMN_25_CAPTION",rsResultSet.getString("COLUMN_25_CAPTION"));
            
            setAttribute("COLUMN_26_ID",rsResultSet.getInt("COLUMN_26_ID"));
            setAttribute("COLUMN_26_FORMULA",rsResultSet.getString("COLUMN_26_FORMULA"));
            setAttribute("COLUMN_26_PER",rsResultSet.getDouble("COLUMN_26_PER"));
            setAttribute("COLUMN_26_AMT",rsResultSet.getDouble("COLUMN_26_AMT"));
            setAttribute("COLUMN_26_CAPTION",rsResultSet.getString("COLUMN_26_CAPTION"));
            
            setAttribute("COLUMN_27_ID",rsResultSet.getInt("COLUMN_27_ID"));
            setAttribute("COLUMN_27_FORMULA",rsResultSet.getString("COLUMN_27_FORMULA"));
            setAttribute("COLUMN_27_PER",rsResultSet.getDouble("COLUMN_27_PER"));
            setAttribute("COLUMN_27_AMT",rsResultSet.getDouble("COLUMN_27_AMT"));
            setAttribute("COLUMN_27_CAPTION",rsResultSet.getString("COLUMN_27_CAPTION"));
            
            setAttribute("COLUMN_28_ID",rsResultSet.getInt("COLUMN_28_ID"));
            setAttribute("COLUMN_28_FORMULA",rsResultSet.getString("COLUMN_28_FORMULA"));
            setAttribute("COLUMN_28_PER",rsResultSet.getDouble("COLUMN_28_PER"));
            setAttribute("COLUMN_28_AMT",rsResultSet.getDouble("COLUMN_28_AMT"));
            setAttribute("COLUMN_28_CAPTION",rsResultSet.getString("COLUMN_28_CAPTION"));
            
            setAttribute("COLUMN_29_ID",rsResultSet.getInt("COLUMN_29_ID"));
            setAttribute("COLUMN_29_FORMULA",rsResultSet.getString("COLUMN_29_FORMULA"));
            setAttribute("COLUMN_29_PER",rsResultSet.getDouble("COLUMN_29_PER"));
            setAttribute("COLUMN_29_AMT",rsResultSet.getDouble("COLUMN_29_AMT"));
            setAttribute("COLUMN_29_CAPTION",rsResultSet.getString("COLUMN_29_CAPTION"));
            
            setAttribute("COLUMN_30_ID",rsResultSet.getInt("COLUMN_30_ID"));
            setAttribute("COLUMN_30_FORMULA",rsResultSet.getString("COLUMN_30_FORMULA"));
            setAttribute("COLUMN_30_PER",rsResultSet.getDouble("COLUMN_30_PER"));
            setAttribute("COLUMN_30_AMT",rsResultSet.getDouble("COLUMN_30_AMT"));
            setAttribute("COLUMN_30_CAPTION",rsResultSet.getString("COLUMN_30_CAPTION"));
            
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            
            setAttribute("PF_POST",rsResultSet.getInt("PF_POST"));
            setAttribute("FREIGHT_POST",rsResultSet.getInt("FREIGHT_POST"));
            setAttribute("OCTROI_POST",rsResultSet.getInt("OCTROI_POST"));
            setAttribute("INSURANCE_POST",rsResultSet.getInt("INSURANCE_POST"));
            setAttribute("CLEARANCE_POST",rsResultSet.getInt("CLEARANCE_POST"));
            setAttribute("AIR_FREIGHT_POST",rsResultSet.getInt("AIR_FREIGHT_POST"));
            setAttribute("OTHERS_POST",rsResultSet.getInt("OTHERS_POST"));
            setAttribute("PAYMENT_TYPE",rsResultSet.getInt("PAYMENT_TYPE"));
            setAttribute("INVOICE_AMOUNT",rsResultSet.getDouble("INVOICE_AMOUNT"));
            
            colSTMItems.clear(); //Clear existing data
            
            GRNNo=(String)getAttribute("GRN_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_TYPE="+pType+" ORDER BY SR_NO");
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                //new 
                clsSTMRawItem ObjItem=new clsSTMRawItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("GRN_NO",rsItem.getString("GRN_NO"));
                ObjItem.setAttribute("GRN_TYPE",rsItem.getInt("GRN_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("MIR_NO",rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("MIR_SR_NO",rsItem.getInt("MIR_SR_NO"));
                ObjItem.setAttribute("MIR_TYPE",rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_SR_NO",rsItem.getInt("PO_SR_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("MIR_QTY",rsItem.getDouble("MIR_QTY"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("EXCESS_QTY",rsItem.getDouble("EXCESS_QTY"));
                ObjItem.setAttribute("PO_QTY",rsItem.getDouble("PO_QTY"));
                ObjItem.setAttribute("RECEIVED_QTY",rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("BALANCE_PO_QTY",rsItem.getDouble("BALANCE_PO_QTY"));
                ObjItem.setAttribute("REJECTED_REASON_ID",rsItem.getInt("REJECTED_REASON_ID"));
                ObjItem.setAttribute("DEPT_ID",rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsItem.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("TOLERANCE_LIMIT",rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("REJECTED_QTY",rsItem.getDouble("REJECTED_QTY"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE",rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("TOTAL_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("SHADE",rsItem.getString("SHADE"));
                ObjItem.setAttribute("W_MIE",rsItem.getString("W_MIE"));
                ObjItem.setAttribute("NO_CASE",rsItem.getString("NO_CASE"));
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN",rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS",rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
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
                
                ObjItem.setAttribute("COLUMN_11_ID",rsItem.getInt("COLUMN_11_ID"));
                ObjItem.setAttribute("COLUMN_11_FORMULA",rsItem.getString("COLUMN_11_FORMULA"));
                ObjItem.setAttribute("COLUMN_11_PER",rsItem.getDouble("COLUMN_11_PER"));
                ObjItem.setAttribute("COLUMN_11_AMT",rsItem.getDouble("COLUMN_11_AMT"));
                ObjItem.setAttribute("COLUMN_11_CAPTION",rsItem.getString("COLUMN_11_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_12_ID",rsItem.getInt("COLUMN_12_ID"));
                ObjItem.setAttribute("COLUMN_12_FORMULA",rsItem.getString("COLUMN_12_FORMULA"));
                ObjItem.setAttribute("COLUMN_12_PER",rsItem.getDouble("COLUMN_12_PER"));
                ObjItem.setAttribute("COLUMN_12_AMT",rsItem.getDouble("COLUMN_12_AMT"));
                ObjItem.setAttribute("COLUMN_12_CAPTION",rsItem.getString("COLUMN_12_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_13_ID",rsItem.getInt("COLUMN_13_ID"));
                ObjItem.setAttribute("COLUMN_13_FORMULA",rsItem.getString("COLUMN_13_FORMULA"));
                ObjItem.setAttribute("COLUMN_13_PER",rsItem.getDouble("COLUMN_13_PER"));
                ObjItem.setAttribute("COLUMN_13_AMT",rsItem.getDouble("COLUMN_13_AMT"));
                ObjItem.setAttribute("COLUMN_13_CAPTION",rsItem.getString("COLUMN_13_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_14_ID",rsItem.getInt("COLUMN_14_ID"));
                ObjItem.setAttribute("COLUMN_14_FORMULA",rsItem.getString("COLUMN_14_FORMULA"));
                ObjItem.setAttribute("COLUMN_14_PER",rsItem.getDouble("COLUMN_14_PER"));
                ObjItem.setAttribute("COLUMN_14_AMT",rsItem.getDouble("COLUMN_14_AMT"));
                ObjItem.setAttribute("COLUMN_14_CAPTION",rsItem.getString("COLUMN_14_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_15_ID",rsItem.getInt("COLUMN_15_ID"));
                ObjItem.setAttribute("COLUMN_15_FORMULA",rsItem.getString("COLUMN_15_FORMULA"));
                ObjItem.setAttribute("COLUMN_15_PER",rsItem.getDouble("COLUMN_15_PER"));
                ObjItem.setAttribute("COLUMN_15_AMT",rsItem.getDouble("COLUMN_15_AMT"));
                ObjItem.setAttribute("COLUMN_15_CAPTION",rsItem.getString("COLUMN_15_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_16_ID",rsItem.getInt("COLUMN_16_ID"));
                ObjItem.setAttribute("COLUMN_16_FORMULA",rsItem.getString("COLUMN_16_FORMULA"));
                ObjItem.setAttribute("COLUMN_16_PER",rsItem.getDouble("COLUMN_16_PER"));
                ObjItem.setAttribute("COLUMN_16_AMT",rsItem.getDouble("COLUMN_16_AMT"));
                ObjItem.setAttribute("COLUMN_16_CAPTION",rsItem.getString("COLUMN_16_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_17_ID",rsItem.getInt("COLUMN_17_ID"));
                ObjItem.setAttribute("COLUMN_17_FORMULA",rsItem.getString("COLUMN_17_FORMULA"));
                ObjItem.setAttribute("COLUMN_17_PER",rsItem.getDouble("COLUMN_17_PER"));
                ObjItem.setAttribute("COLUMN_17_AMT",rsItem.getDouble("COLUMN_17_AMT"));
                ObjItem.setAttribute("COLUMN_17_CAPTION",rsItem.getString("COLUMN_17_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_18_ID",rsItem.getInt("COLUMN_18_ID"));
                ObjItem.setAttribute("COLUMN_18_FORMULA",rsItem.getString("COLUMN_18_FORMULA"));
                ObjItem.setAttribute("COLUMN_18_PER",rsItem.getDouble("COLUMN_18_PER"));
                ObjItem.setAttribute("COLUMN_18_AMT",rsItem.getDouble("COLUMN_18_AMT"));
                ObjItem.setAttribute("COLUMN_18_CAPTION",rsItem.getString("COLUMN_18_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_19_ID",rsItem.getInt("COLUMN_19_ID"));
                ObjItem.setAttribute("COLUMN_19_FORMULA",rsItem.getString("COLUMN_19_FORMULA"));
                ObjItem.setAttribute("COLUMN_19_PER",rsItem.getDouble("COLUMN_19_PER"));
                ObjItem.setAttribute("COLUMN_19_AMT",rsItem.getDouble("COLUMN_19_AMT"));
                ObjItem.setAttribute("COLUMN_19_CAPTION",rsItem.getString("COLUMN_19_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_20_ID",rsItem.getInt("COLUMN_20_ID"));
                ObjItem.setAttribute("COLUMN_20_FORMULA",rsItem.getString("COLUMN_20_FORMULA"));
                ObjItem.setAttribute("COLUMN_20_PER",rsItem.getDouble("COLUMN_20_PER"));
                ObjItem.setAttribute("COLUMN_20_AMT",rsItem.getDouble("COLUMN_20_AMT"));
                ObjItem.setAttribute("COLUMN_20_CAPTION",rsItem.getString("COLUMN_20_CAPTION"));
                
                
                ObjItem.setAttribute("COLUMN_21_ID",rsItem.getInt("COLUMN_21_ID"));
                ObjItem.setAttribute("COLUMN_21_FORMULA",rsItem.getString("COLUMN_21_FORMULA"));
                ObjItem.setAttribute("COLUMN_21_PER",rsItem.getDouble("COLUMN_21_PER"));
                ObjItem.setAttribute("COLUMN_21_AMT",rsItem.getDouble("COLUMN_21_AMT"));
                ObjItem.setAttribute("COLUMN_21_CAPTION",rsItem.getString("COLUMN_21_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_22_ID",rsItem.getInt("COLUMN_22_ID"));
                ObjItem.setAttribute("COLUMN_22_FORMULA",rsItem.getString("COLUMN_22_FORMULA"));
                ObjItem.setAttribute("COLUMN_22_PER",rsItem.getDouble("COLUMN_22_PER"));
                ObjItem.setAttribute("COLUMN_22_AMT",rsItem.getDouble("COLUMN_22_AMT"));
                ObjItem.setAttribute("COLUMN_22_CAPTION",rsItem.getString("COLUMN_22_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_23_ID",rsItem.getInt("COLUMN_23_ID"));
                ObjItem.setAttribute("COLUMN_23_FORMULA",rsItem.getString("COLUMN_23_FORMULA"));
                ObjItem.setAttribute("COLUMN_23_PER",rsItem.getDouble("COLUMN_23_PER"));
                ObjItem.setAttribute("COLUMN_23_AMT",rsItem.getDouble("COLUMN_23_AMT"));
                ObjItem.setAttribute("COLUMN_23_CAPTION",rsItem.getString("COLUMN_23_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_24_ID",rsItem.getInt("COLUMN_24_ID"));
                ObjItem.setAttribute("COLUMN_24_FORMULA",rsItem.getString("COLUMN_24_FORMULA"));
                ObjItem.setAttribute("COLUMN_24_PER",rsItem.getDouble("COLUMN_24_PER"));
                ObjItem.setAttribute("COLUMN_24_AMT",rsItem.getDouble("COLUMN_24_AMT"));
                ObjItem.setAttribute("COLUMN_24_CAPTION",rsItem.getString("COLUMN_24_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_25_ID",rsItem.getInt("COLUMN_25_ID"));
                ObjItem.setAttribute("COLUMN_25_FORMULA",rsItem.getString("COLUMN_25_FORMULA"));
                ObjItem.setAttribute("COLUMN_25_PER",rsItem.getDouble("COLUMN_25_PER"));
                ObjItem.setAttribute("COLUMN_25_AMT",rsItem.getDouble("COLUMN_25_AMT"));
                ObjItem.setAttribute("COLUMN_25_CAPTION",rsItem.getString("COLUMN_25_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_26_ID",rsItem.getInt("COLUMN_26_ID"));
                ObjItem.setAttribute("COLUMN_26_FORMULA",rsItem.getString("COLUMN_26_FORMULA"));
                ObjItem.setAttribute("COLUMN_26_PER",rsItem.getDouble("COLUMN_26_PER"));
                ObjItem.setAttribute("COLUMN_26_AMT",rsItem.getDouble("COLUMN_26_AMT"));
                ObjItem.setAttribute("COLUMN_26_CAPTION",rsItem.getString("COLUMN_26_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_27_ID",rsItem.getInt("COLUMN_27_ID"));
                ObjItem.setAttribute("COLUMN_27_FORMULA",rsItem.getString("COLUMN_27_FORMULA"));
                ObjItem.setAttribute("COLUMN_27_PER",rsItem.getDouble("COLUMN_27_PER"));
                ObjItem.setAttribute("COLUMN_27_AMT",rsItem.getDouble("COLUMN_27_AMT"));
                ObjItem.setAttribute("COLUMN_27_CAPTION",rsItem.getString("COLUMN_27_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_28_ID",rsItem.getInt("COLUMN_28_ID"));
                ObjItem.setAttribute("COLUMN_28_FORMULA",rsItem.getString("COLUMN_28_FORMULA"));
                ObjItem.setAttribute("COLUMN_28_PER",rsItem.getDouble("COLUMN_28_PER"));
                ObjItem.setAttribute("COLUMN_28_AMT",rsItem.getDouble("COLUMN_28_AMT"));
                ObjItem.setAttribute("COLUMN_28_CAPTION",rsItem.getString("COLUMN_28_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_29_ID",rsItem.getInt("COLUMN_29_ID"));
                ObjItem.setAttribute("COLUMN_29_FORMULA",rsItem.getString("COLUMN_29_FORMULA"));
                ObjItem.setAttribute("COLUMN_29_PER",rsItem.getDouble("COLUMN_29_PER"));
                ObjItem.setAttribute("COLUMN_29_AMT",rsItem.getDouble("COLUMN_29_AMT"));
                ObjItem.setAttribute("COLUMN_29_CAPTION",rsItem.getString("COLUMN_29_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_30_ID",rsItem.getInt("COLUMN_30_ID"));
                ObjItem.setAttribute("COLUMN_30_FORMULA",rsItem.getString("COLUMN_30_FORMULA"));
                ObjItem.setAttribute("COLUMN_30_PER",rsItem.getDouble("COLUMN_30_PER"));
                ObjItem.setAttribute("COLUMN_30_AMT",rsItem.getDouble("COLUMN_30_AMT"));
                ObjItem.setAttribute("COLUMN_30_CAPTION",rsItem.getString("COLUMN_30_CAPTION"));
                
                ObjItem.setAttribute("MATERIAL_CODE",rsItem.getString("MATERIAL_CODE"));
                ObjItem.setAttribute("MATERIAL_DESC",rsItem.getString("MATERIAL_DESC"));
                ObjItem.setAttribute("QUALITY_NO",rsItem.getString("QUALITY_NO"));
                ObjItem.setAttribute("PAGE_NO",rsItem.getString("PAGE_NO"));
                ObjItem.setAttribute("EXCESS",rsItem.getDouble("EXCESS"));
                ObjItem.setAttribute("SHORTAGE",rsItem.getDouble("SHORTAGE"));
                ObjItem.setAttribute("CHALAN_QTY",rsItem.getDouble("CHALAN_QTY"));
                ObjItem.setAttribute("L_F_NO",rsItem.getString("L_F_NO"));
                ObjItem.setAttribute("BARCODE_TYPE",rsItem.getString("BARCODE_TYPE"));
                ObjItem.setAttribute("RND_DEDUCTION_REASON",rsItem.getString("RND_DEDUCTION_REASON"));
                
                //== Insert Lot Nos. ==
                
                stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsLot=stLot.executeQuery("SELECT * FROM D_INV_GRN_LOT WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+SrNo+" AND GRN_TYPE="+pType+" ORDER BY SR_NO");
                rsLot.first();
                
                LotCounter=0;
                while((!rsLot.isAfterLast())&&(rsLot.getRow()>0)) {
                    LotCounter=LotCounter+1;

                    clsSTMReceiptRawLot ObjLot=new clsSTMReceiptRawLot();
                    ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("GRN_NO",rsLot.getString("GRN_NO"));
                    ObjLot.setAttribute("GRN_SR_NO",rsLot.getInt("GRN_SR_NO"));
                    ObjLot.setAttribute("GRN_TYPE",rsLot.getInt("GRN_TYPE"));
                    ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("ITEM_ID",rsLot.getString("ITEM_ID"));
                    ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                    ObjLot.setAttribute("AUTO_LOT_NO",rsLot.getString("AUTO_LOT_NO"));
                    ObjLot.setAttribute("LOT_RECEIVED_QTY",rsLot.getDouble("LOT_RECEIVED_QTY"));
                    ObjLot.setAttribute("LOT_REJECTED_QTY",rsLot.getDouble("LOT_REJECTED_QTY"));
                    ObjLot.setAttribute("LOT_ACCEPTED_QTY",rsLot.getDouble("LOT_ACCEPTED_QTY"));
                    ObjLot.setAttribute("BALANCE_QTY",rsLot.getDouble("BALANCE_QTY"));
                    ObjLot.setAttribute("LOT_CLOSE",rsLot.getBoolean("LOT_CLOSE"));
                    
                    ObjItem.colSTMLot.put(Integer.toString(LotCounter),ObjLot);
                    rsLot.next();
                }
                
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GRN_NO='"+pDocNo+"' AND (APPROVED=1) AND GRN_TYPE='" + GRNType + "'";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=181 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GRN_NO='"+pDocNo+"' AND (APPROVED=1) AND GRN_TYPE='" + GRNType + "'";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=181 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    public static HashMap getGRNItemList(int pCompanyID,String pGRNNo,boolean pAllItems,int pType) {
        //Fourth argument pAllItems indicates whether you want all item listing or
        //Just pending Items where GRN_RECD_QTY<QTY
        //i.e. GRN entry is still pending
        
        Connection tmpConn;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pGRNNo+"' AND GRN_TYPE="+pType+" AND APPROVED=1");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(pAllItems) //Retrieve All Items
                {
                    strSQL="SELECT D_INV_GRN_DETAIL.* FROM D_INV_GRN_DETAIL,D_INV_GRN_HEADER  WHERE D_INV_GRN_DETAIL.COMPANY_ID=D_INV_GRN_HEADER.COMPANY_ID AND D_INV_GRN_DETAIL.GRN_NO=D_INV_GRN_HEADER.GRN_NO AND D_INV_GRN_DETAIL.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.GRN_NO='"+pGRNNo+"' AND D_INV_GRN_HEADER.GRN_TYPE="+pType+" ORDER BY ITEM_ID";
                }
                else //Retrieve Only pending Items
                {
                    strSQL="SELECT D_INV_GRN_DETAIL.* FROM D_INV_GRN_DETAIL,D_INV_GRN_HEADER WHERE D_INV_GRN_DETAIL.COMPANY_ID=D_INV_GRN_HEADER.COMPANY_ID AND D_INV_GRN_DETAIL.GRN_NO=D_INV_GRN_HEADER.GRN_NO AND D_INV_GRN_DETAIL.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.GRN_NO='"+pGRNNo+"' AND D_INV_GRN_HEADER.GRN_TYPE="+pType+" AND REJECTED_QTY>0 ORDER BY ITEM_ID";
                }
                
                
                stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=stTmp.executeQuery(strSQL);
                rsTmp.first();
                
                Counter1=0;
                
                while(!rsTmp.isAfterLast()) {
                    Counter1++;
                    clsGRNItem ObjItem=new clsGRNItem();
                    
                    int GRNSrNo=rsTmp.getInt("SR_NO");
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("GRN_NO",rsTmp.getString("GRN_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("WAREHOUSE_ID",rsTmp.getString("WAREHOUSE_ID"));
                    ObjItem.setAttribute("LOCATION_ID",rsTmp.getString("LOCATION_ID"));
                    ObjItem.setAttribute("PO_NO",rsTmp.getString("PO_NO"));
                    ObjItem.setAttribute("PO_SR_NO",rsTmp.getInt("PO_SR_NO"));
                    ObjItem.setAttribute("PO_TYPE",rsTmp.getInt("PO_TYPE"));
                    ObjItem.setAttribute("MIR_NO",rsTmp.getString("MIR_NO"));
                    ObjItem.setAttribute("MIR_SR_NO",rsTmp.getInt("MIR_SR_NO"));
                    ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                    ObjItem.setAttribute("BOE_NO",rsTmp.getString("BOE_NO"));
                    ObjItem.setAttribute("BOE_SR_NO",rsTmp.getString("BOE_SR_NO"));
                    ObjItem.setAttribute("BOE_DATE",rsTmp.getString("BOE_DATE"));
                    ObjItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                    ObjItem.setAttribute("EXCESS_QTY",rsTmp.getDouble("EXCESS_QTY"));
                    ObjItem.setAttribute("PO_QTY",rsTmp.getDouble("PO_QTY"));
                    ObjItem.setAttribute("RECEIVED_QTY",rsTmp.getDouble("RECEIVED_QTY"));
                    ObjItem.setAttribute("BALANCE_PO_QTY",rsTmp.getDouble("BALANCE_PO_QTY"));
                    ObjItem.setAttribute("REJECTED_REASON_ID",rsTmp.getInt("REJECTED_REASON_ID"));
                    ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                    ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                    ObjItem.setAttribute("REJECTED_QTY",rsTmp.getDouble("REJECTED_QTY"));
                    
                    //Now Fetch Lots.
                    stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_GRN_LOT WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pGRNNo+"' AND GRN_SR_NO="+GRNSrNo);
                    rsLot.first();
                    
                    //Clear existing data
                    ObjItem.colItemLot.clear();
                    
                    if(rsLot.getRow()>0) {
                        Counter2=0;
                        while(!rsLot.isAfterLast()) {
                            Counter2++;
                            clsGRNLot ObjLot=new clsGRNLot();
                            
                            ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                            ObjLot.setAttribute("GRN_NO",rsLot.getString("GRN_NO"));
                            ObjLot.setAttribute("GRN_SR_NO",rsLot.getInt("GRN_SR_NO"));
                            ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                            ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                            ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                            
                            ObjItem.colItemLot.put(Integer.toString(Counter2),ObjLot);
                            rsLot.next();
                        }
                    }
                    //Put into list
                    List.put(Integer.toString(Counter1),ObjItem);
                    
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pType,int pOrder,int FinYearFrom) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_GRN_HEADER.GRN_NO,D_INV_GRN_HEADER.GRN_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_GRN_HEADER,D_COM_DOC_DATA WHERE D_INV_GRN_HEADER.GRN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GRN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GRN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_GRN_HEADER.GRN_TYPE="+pType+" AND MODULE_ID="+(pType+6)+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_GRN_HEADER.GRN_NO,D_INV_GRN_HEADER.GRN_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_GRN_HEADER,D_COM_DOC_DATA WHERE D_INV_GRN_HEADER.GRN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GRN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GRN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_GRN_HEADER.GRN_TYPE="+pType+" AND MODULE_ID="+(pType+6)+" ORDER BY D_INV_GRN_HEADER.GRN_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_GRN_HEADER.GRN_NO,D_INV_GRN_HEADER.GRN_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_GRN_HEADER,D_COM_DOC_DATA WHERE D_INV_GRN_HEADER.GRN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GRN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GRN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_GRN_HEADER.GRN_TYPE="+pType+" AND MODULE_ID="+(pType+6)+" ORDER BY D_INV_GRN_HEADER.GRN_NO";
            }
            
            //strSQL="SELECT D_INV_GRN_HEADER.GRN_NO,D_INV_GRN_HEADER.GRN_DATE FROM D_INV_GRN_HEADER,D_COM_DOC_DATA WHERE D_INV_GRN_HEADER.GRN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_GRN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_GRN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_GRN_HEADER.GRN_TYPE="+pType+" AND MODULE_ID="+(pType+6);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("GRN_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsGRN ObjDoc=new clsGRN();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("GRN_NO",rsTmp.getString("GRN_NO"));
                ObjDoc.setAttribute("GRN_DATE",rsTmp.getString("GRN_DATE"));
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
    
    public static Object getLastGRN(int pCompanyID,int pDeptID,String pItemID) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        String LastGRNNo="";
        
        Object ObjGRN=null;
        
        try {
            strSQL="SELECT DISTINCT(D_INV_GRN_HEADER.GRN_NO) AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_COM_USER_MASTER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.CREATED_BY=D_COM_USER_MASTER.USER_ID AND D_INV_GRN_HEADER.COMPANY_ID=D_COM_USER_MASTER.COMPANY_ID AND D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_USER_MASTER.DEPT_ID="+pDeptID+"  AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastGRNNo=rsTmp.getString("LAST_GRN_NO");
                int GRNType=rsTmp.getInt("GRN_TYPE");
                
                if(GRNType==1) {
                    clsGRNGen tmpObj=new clsGRNGen();
                    tmpObj.LoadData(pCompanyID);
                    
                    ObjGRN=(clsGRNGen)tmpObj.getObject(pCompanyID,LastGRNNo,GRNType);
                }
                else {
                    clsGRN tmpObj=new clsGRN();
                    tmpObj.LoadData(pCompanyID);
                    
                    ObjGRN=(clsGRN)tmpObj.getObject(pCompanyID,LastGRNNo,GRNType);
                }
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return ObjGRN;
    }
    
    
    public static Object getLastGRN(int pCompanyID,String pItemID,boolean pApproved) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        String LastGRNNo="";
        
        Object ObjGRN=null;
        
        try {
            if(pApproved) {
                strSQL="SELECT DISTINCT(D_INV_GRN_HEADER.GRN_NO) AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC";
            }
            else {
                strSQL="SELECT DISTINCT(D_INV_GRN_HEADER.GRN_NO) AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC";
            }
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastGRNNo=rsTmp.getString("LAST_GRN_NO");
                int GRNType=rsTmp.getInt("GRN_TYPE");
                
                if(GRNType==1) {
                    clsGRNGen tmpObj=new clsGRNGen();
                    tmpObj.LoadData(pCompanyID);
                    
                    ObjGRN=(clsGRNGen)tmpObj.getObject(pCompanyID,LastGRNNo,GRNType);
                }
                else if(GRNType==2){
                    clsGRN tmpObj=new clsGRN();
                    tmpObj.LoadData(pCompanyID);
                    
                    ObjGRN=(clsGRN)tmpObj.getObject(pCompanyID,LastGRNNo,GRNType);
                }
                else if(GRNType==3){
                    clsSTMReceiptRaw tmpObj=new clsSTMReceiptRaw();
                    tmpObj.LoadData(pCompanyID);
                    
                    ObjGRN=(clsSTMReceiptRaw)tmpObj.getObject(pCompanyID,LastGRNNo,GRNType);
                }
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            
            //JOptionPane.showMessageDialog(null,"GRN"+e.getMessage());
            
        }
        
        return ObjGRN;
    }
    
    
    public static String getLastGRNNo(int pCompanyID,String pItemID,boolean pApproved) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        String LastGRNNo="";
        
        try {
            if(pApproved) {
                strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' AND D_INV_GRN_DETAIL.QTY>0 ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
            }
            else {
                strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' AND D_INV_GRN_DETAIL.QTY>0 ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
            }
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastGRNNo=rsTmp.getString("LAST_GRN_NO");
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            
        }
        catch(Exception e) {
            int a=0;
        }
        
        return LastGRNNo;
    }
    
    
    
    public static String getLastGRNNo(int pCompanyID,String pItemID,boolean pApproved,String LastPONo) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        String LastGRNNo="";
        String MIRNo="";
        
        try {
            
            if(pApproved) {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_HEADER.APPROVED=1 AND D_INV_MIR_HEADER.CANCELLED=0 AND D_INV_MIR_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.ITEM_ID='"+pItemID+"' AND PO_NO='"+LastPONo+"' AND D_INV_GRN_DETAIL.QTY>0 ORDER BY D_INV_MIR_HEADER.MIR_DATE DESC LIMIT 1";
            }
            else {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_HEADER.CANCELLED=0 AND D_INV_MIR_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.ITEM_ID='"+pItemID+"' AND PO_NO='"+LastPONo+"' AND D_INV_GRN_DETAIL.QTY>0 ORDER BY D_INV_MIR_HEADER.MIR_DATE DESC LIMIT 1";
            }
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                MIRNo=rsTmp.getString("MIR_NO");
            }
            
            
            if(!MIRNo.trim().equals("")) {
                
                if(pApproved) {
                    strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' AND MIR_NO='"+MIRNo+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
                }
                else {
                    strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' AND MIR_NO='"+MIRNo+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
                }
                tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    LastGRNNo=rsTmp.getString("LAST_GRN_NO");
                } else {
                    if(pApproved) {
                        strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
                    } else {
                        strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
                    }
                    tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    rsTmp=tmpStmt.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        LastGRNNo=rsTmp.getString("LAST_GRN_NO");
                    }
                }
            } else {
                if(pApproved) {
                    strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
                }
                else {
                    strSQL="SELECT D_INV_GRN_HEADER.GRN_NO AS LAST_GRN_NO,D_INV_GRN_HEADER.GRN_TYPE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_HEADER.CANCELLED=0 AND D_INV_GRN_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_GRN_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_INV_GRN_HEADER.GRN_DATE DESC LIMIT 1";
                }
                tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    LastGRNNo=rsTmp.getString("LAST_GRN_NO");
                }
            }
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
        } catch(Exception e) {
            
        }
        
        return LastGRNNo;
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "'");
            
            while(rsTmp.next()) {
                clsSTMReceiptRaw ObjGRN=new clsSTMReceiptRaw();
                
                ObjGRN.setAttribute("GRN_NO",rsTmp.getString("GRN_NO"));
                ObjGRN.setAttribute("GRN_DATE",rsTmp.getString("GRN_DATE"));
                ObjGRN.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjGRN.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjGRN.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjGRN.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjGRN.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjGRN);
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
    
    public static int getGRNType(int pCompanyID,String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        int GRNType=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT GRN_TYPE FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                GRNType=rsTmp.getInt("GRN_TYPE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return GRNType;
            
        }
        catch(Exception e) {
            return GRNType;
        }
    }
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT GRN_NO,APPROVED,CANCELLED FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"'");
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
            rsTmp=data.getResult("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"' AND CANCELLED=0 AND GRN_TYPE='" + GRNType + "'");
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
    
    
    public static boolean CancelGRN(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            
            if(CanCancel(pCompanyID,pDocNo)) {
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    rsTmp=data.getResult("SELECT * FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        while(!rsTmp.isAfterLast()) {
                            String MIRNo=rsTmp.getString("MIR_NO");
                            int MIRSrNo=rsTmp.getInt("MIR_SR_NO");
                            
                            //============== Update P.O. ============ //
                            if((!MIRNo.trim().equals(""))&&(MIRSrNo>0)) {
                                
                                //Reverse the Stock Effect
                                double Qty=rsTmp.getDouble("QTY");
                                String ItemID=rsTmp.getString("ITEM_ID");
                                String WarehouseID=rsTmp.getString("WAREHOUSE_ID");
                                String LocationID=rsTmp.getString("LOCATION_ID");
                                String BOENo=rsTmp.getString("BOE_NO");
                                //String LOTNo=rsTmp.getString("LOT_NO");
                                String LOTNo="X";
                                
                                String strSQL="UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY=GRN_RECD_QTY-"+Qty+" WHERE MIR_NO='"+MIRNo+"' AND SR_NO="+MIRSrNo+" AND MIR_TYPE=2";
                                data.Execute(strSQL);
                                
                                strSQL="UPDATE D_INV_ITEM_LOT_MASTER SET ON_HAND_QTY=ON_HAND_QTY-"+Qty+",TOTAL_RECEIPT_QTY=TOTAL_RECEIPT_QTY-"+Qty+",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY WHERE ITEM_ID='"+ItemID+"' AND WAREHOUSE_ID='"+WarehouseID+"' AND LOCATION_ID='"+LocationID+"' AND BOE_NO='"+BOENo+"' AND LOT_NO='"+LOTNo+"'";
                                data.Execute(strSQL);
                            }
                            //=============== P.O. Updation Completed ===============//
                            
                            rsTmp.next();
                        }
                        
                    }
                    
                }
                else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID=181");
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_GRN_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "'");
                data.Execute("UPDATE D_INV_GRN_LOT SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    
    
    
    public static boolean IsGRNExist(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                isExist=true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        }
        catch(Exception e) {
            return isExist;
        }
    }

    public static boolean IsGRNExist(String pDocNo,String dbURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist=false;
        
        try {
            tmpConn=data.getConn(dbURL);
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='"+pDocNo+"' AND GRN_TYPE='" + GRNType + "' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                isExist=true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        }
        catch(Exception e) {
            return isExist;
        }
    }
    
    
    public static double getGRNTotalAmount(String DocNo,String dbURL) {
        
        double GRNAmount=0;
        
        
        try {
            
            GRNAmount=data.getDoubleValueFromDB("SELECT SUM(NET_AMOUNT) AS AMOUNT FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+DocNo+"'",dbURL);
            
            ResultSet rsTemp=data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='"+DocNo+"'",dbURL);
            rsTemp.first();
            
            if(rsTemp.getRow()>=0) {
                GRNAmount=GRNAmount-UtilFunctions.getDouble(rsTemp,"COLUMN_1_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_2_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_6_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_9_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_18_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_20_AMT",0);
                
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_3_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_4_AMT",0);
                GRNAmount=GRNAmount-UtilFunctions.getDouble(rsTemp,"COLUMN_8_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_5_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_7_AMT",0);
                
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_11_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_22_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_12_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_13_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_14_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_15_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_16_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_17_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_19_AMT",0);
                
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_21_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_23_AMT",0);
                GRNAmount+=UtilFunctions.getDouble(rsTemp,"COLUMN_24_AMT",0);
                
            }
        }
        catch(Exception e) {
            
        }
        
        return EITLERPGLOBAL.round(GRNAmount,0);
    }
    
    public boolean IsPostingNecessary(String DocNo,int DocType,String ExVoucherNo) {
        
        try {
            
            //Load GRN
            clsGRN objGRN=(clsGRN)getObject(EITLERPGLOBAL.gCompanyID,DocNo,DocType);
            
            //*********** Select Voucher Hierarchy based on Rules *********//
            //Note: This routine overrides hierarchy selected by user in GRN
            
            
            //(2) Based on Payment Type
            int PaymentType=objGRN.getAttribute("PAYMENT_TYPE").getInt();
            
            HashMap List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, 8, "CHOOSE_HIERARCHY", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if(List.size()>0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int HierarchyID=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID",HierarchyID);
            }
            
            //(3) Based on Post Voucher Condition
            String SuppID=objGRN.getAttribute("SUPP_ID").getString();
            
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, 8, "POST_VOUCHER", "SUPP_ID", SuppID);
            
            if(List.size()>0) {
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int PostVoucher=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if(PostVoucher==0) {
                    return false; //Do not post voucher
                }
            }
            
            //(4) Based on Payment Type, decide whether to post voucher
            List=clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, 8, "POST_VOUCHER", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if(List.size()>0) {
                clsApprovalRules objRule=(clsApprovalRules)List.get(Integer.toString(1));
                int PostVoucher=UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if(PostVoucher==0) {
                    return false; //Do not post voucher
                }
            }
            //******************* Hierarchy Selected **********************//
            
            
            return true;
        }
        catch(Exception e) {
            return true;
        }
        
        
    }
    
        
    public double getHeaderAmount(String GRNNo,int GRNType) {
        double HeaderAmount=0;
        
        try {
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='"+GRNNo+"' AND GRN_TYPE="+GRNType);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                HeaderAmount=HeaderAmount-UtilFunctions.getDouble(rsTmp,"COLUMN_1_AMT",0); //Discount
                HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_9_AMT",0); //FCA
                HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_23_AMT",0); //Additional
                HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_24_AMT",0); //FShipping
                
                if(UtilFunctions.getInt(rsTmp,"PF_POST",0)==0) //Packing & Forwarding
                {
                    HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_2_AMT",0); //PF
                }
                
                if(UtilFunctions.getInt(rsTmp,"FREIGHT_POST",0)==0) //Freight
                {
                    HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_6_AMT",0); //Freight
                }
                
                if(UtilFunctions.getInt(rsTmp,"INSURANCE_POST",0)==0) //Insurance
                {
                    HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_18_AMT",0); //Insurance
                }
                
                if(UtilFunctions.getInt(rsTmp,"AIR_FREIGHT_POST",0)==0) //Air Freight
                {
                    HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_20_AMT",0); //Air Freight
                }
                
                if(UtilFunctions.getInt(rsTmp,"OTHERS_POST",0)==0) //Other Expenses
                {
                    HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_21_AMT",0); //Others
                }
                
                if(UtilFunctions.getInt(rsTmp,"OCTROI_POST",0)==0) {
                    HeaderAmount=HeaderAmount+UtilFunctions.getDouble(rsTmp,"COLUMN_7_AMT",0); //Octroi
                }
            }
        } catch(Exception e) {
        }
        return HeaderAmount;
    }
}
