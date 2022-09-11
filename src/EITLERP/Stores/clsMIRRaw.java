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


/**
 *
 * @author  nrpithva
 * @version
 */

public class clsMIRRaw {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colMIRItems=new HashMap();
    
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
    public clsMIRRaw() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("MIR_NO",new Variant(""));
        props.put("MIR_DATE",new Variant(""));
        props.put("SUPP_ID",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("REFA",new Variant(""));
        props.put("CHALAN_NO",new Variant(""));
        props.put("CHALAN_DATE",new Variant(""));
        props.put("LR_NO",new Variant(""));
        props.put("LR_DATE",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("TRANSPORTER",new Variant(0));
        props.put("TRANSPORTER_NAME",new Variant(""));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("IMPORT_CONCESS",new Variant(false));
        props.put("ACCESSABLE_VALUE",new Variant(0));
        props.put("CURRENCY_ID",new Variant(0));
        props.put("CURRENCY_RATE",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("GROSS_AMOUNT",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CENVATED_ITEMS",new Variant(false));
        props.put("CANCELLED",new Variant(false));
        props.put("MIR_TYPE",new Variant(0));
        props.put("OPEN_STATUS",new Variant(""));
        props.put("FOR_STORE",new Variant(0));
        props.put("REMARKS",new Variant(""));
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
        props.put("INVOICE_AMOUNT",new Variant(0));
        
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
        
    }
    
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+pCompanyID+" AND MIR_TYPE=2 AND MIR_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND MIR_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY MIR_NO");
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
        Statement stItem,stLot,stStock,stTmp,stItemMaster,stPO,stSTM,stHistory,stHDetail,stHLot;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsPO,rsHistory,rsHDetail,rsHLot;
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="",LocationID="",PONo="",strSQL="",BOEDate="",STMNo="";
        int CompanyID=0,POSrNo=0,STMSrNo=0,POType=0;
        double RejectedQty=0,Qty=0,ToleranceLimit=0;
        Statement stHeader,stInward;
        ResultSet rsHeader,rsInward;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("MIR_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            if(getAttribute("SUPP_ID").getString().trim().equals("000000")) {
                //LastError="Supplier code is not valid";
                //return false;
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_MIR_HEADER_H WHERE MIR_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_MIR_DETAIL_H WHERE MIR_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_MIR_LOT_H WHERE MIR_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_MIR_HEADER WHERE MIR_NO='1'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //=========== Check the Quantities entered against P.O. ============= //
            for(int i=1;i<=colMIRItems.size();i++) {
                clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                PONo=(String)ObjItem.getAttribute("PO_NO").getObj();
                POSrNo=(int)ObjItem.getAttribute("PO_SR_NO").getVal();
                POType=(int)ObjItem.getAttribute("PO_TYPE").getVal();
                ItemID=ObjItem.getAttribute("ITEM_ID").getString();
                
                ToleranceLimit=clsItem.getToleranceLimit(CompanyID,(String)ObjItem.getAttribute("ITEM_ID").getObj());
                
                STMNo=(String)ObjItem.getAttribute("STM_NO").getObj();
                STMSrNo=(int)ObjItem.getAttribute("STM_SR_NO").getVal();
                
                //---New Change --
                int STMCompany=(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal();
                int STMCompanyYear=(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal();
                String dbURL=clsFinYear.getDBURL(STMCompany,STMCompanyYear);
                
                tmpConn=data.getConn(dbURL);
                //-------------
                
                double POQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
                
                
                if((!STMNo.trim().equals(""))&&(STMSrNo>0)) //S.T.M. No. entered
                {
                    //Get the PO Qty.
                    stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND SR_NO="+STMSrNo+" AND STM_TYPE=2";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        POQty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                    }
                    
                    //Get Total Qty Entered in MIR Against this STM No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND STM_SR_NO="+STMSrNo+" AND STM_TYPE=2 AND MIR_NO NOT IN(SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
                    }
                    
                    CurrentQty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                    
                    
                    /*if((CurrentQty+PrevQty- (( (CurrentQty+PrevQty) *ToleranceLimit)/100) ) > POQty) //If total Qty exceeds PO Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds STM No. "+STMNo+" Sr. No. "+STMSrNo+" qty "+POQty+". Please verify the input.";
                        return false;
                    }*/
                    
                    
                }
                
                
                
                if((!PONo.trim().equals(""))&&(POSrNo>0)) //P.O. No. entered
                {
                    //Get the PO Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY,ITEM_ID FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND SR_NO="+POSrNo+" AND PO_TYPE="+POType;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        POQty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                        
                        if((!ItemID.trim().equals(""))&&(POType!=2)&&(POType!=6)&&(POType!=7)) {
                            if(!rsTmp.getString("ITEM_ID").trim().equals(ItemID)) {
                                LastError="Item Code in MIR doesn't match with PO. Original Item code is "+rsTmp.getString("ITEM_ID");
                                return false;
                            }
                        }
                        
                    }
                    
                    //Get Total Qty Entered in MIR Against this PO No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_SR_NO="+POSrNo+" AND PO_TYPE="+POType+" AND MIR_NO NOT IN(SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
                    }
                    
                    CurrentQty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                    
                    /*if((CurrentQty+PrevQty-(( (CurrentQty+PrevQty)*ToleranceLimit)/100)) > POQty) //If total Qty exceeds PO Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds PO No. "+PONo+" Sr. No. "+POSrNo+" qty "+POQty+". Please verify the input.";
                        //return false;
                    }*/
                }
            }
            //============== GRN Checking Completed ====================//
            
            
            
            //=========== Check the Max Qty Level.============= //
            for(int i=1;i<=colMIRItems.size();i++) {
                clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                
                double MaxQty=clsItem.getMaxQty(CompanyID, ItemID);
                
                if(MaxQty>0) {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    double AvailableQty=clsItem.getAvailableQty(CompanyID, ItemID);
                    
                    if((CurrentQty+AvailableQty)>MaxQty) {
                        LastError="Total Receipt Qty "+(CurrentQty+AvailableQty)+" exceeds Maximum limit "+MaxQty+" for Item "+ItemID;
                        return false;
                    }
                }
            }
            //=================================================///
            
            
            
            //===============Update P.O.======================//
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                for(int i=1;i<=colMIRItems.size();i++) {
                    clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                    
                    PONo=(String)ObjItem.getAttribute("PO_NO").getObj();
                    POSrNo=(int)ObjItem.getAttribute("PO_SR_NO").getVal();
                    POType=(int)ObjItem.getAttribute("PO_TYPE").getVal();
                    STMNo=(String)ObjItem.getAttribute("STM_NO").getObj();
                    STMSrNo=(int)ObjItem.getAttribute("STM_SR_NO").getVal();
                    
                    Qty=ObjItem.getAttribute("QTY").getVal();
                    ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    
                    //================= Update STM ==============//
                    if((!STMNo.trim().equals(""))&&(STMSrNo>0)) {
                        //---Updating STM Qty -----------//
                        int STMCompany=(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal();
                        int STMCompanyYear=(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal();
                        String dbURL=clsFinYear.getDBURL(STMCompany,STMCompanyYear);
                        
                        tmpConn=data.getConn(dbURL);
                        stSTM=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        //-------------
                        
                        stSTM.executeUpdate("UPDATE D_INV_STM_DETAIL SET RECEIVED_QTY=RECEIVED_QTY+"+Qty+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE STM_NO='"+STMNo+"' AND SR_NO="+STMSrNo+" AND STM_TYPE=2");
                        stSTM.executeUpdate("UPDATE D_INV_STM_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE STM_NO='"+STMNo+"'");
                    }
                    //===========================================//
                    
                    
                    
                    //============== Update P.O. ============ //
                    if((!PONo.trim().equals(""))&&(POSrNo>0)) {
                        if(POType!=7) //Do not update P.O. in case of Contract Purchase. 7 - Contract Purchase
                        {
                            data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY+"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);
                            data.Execute("UPDATE D_PUR_PO_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"'");
                            data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY+"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);
                        }
                    }
                    //=============== P.O. Updation Completed ===============//
                }
            }
            //==========P.O. Updation Complete===============//
            
            
            //--------- Generate New GRN No. ------------
            setAttribute("MIR_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,6, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("MIR_NO",getAttribute("MIR_NO").getString());
            String MIRNo = getAttribute("MIR_NO").getString();
            rsResultSet.updateString("MIR_DATE",(String)getAttribute("MIR_DATE").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsResultSet.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsResultSet.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateDouble("ACCESSABLE_VALUE",EITLERPGLOBAL.round(getAttribute("ACCESSABLE_VALUE").getVal(),3));
            rsResultSet.updateDouble("INVOICE_AMOUNT",EITLERPGLOBAL.round(getAttribute("INVOICE_AMOUNT").getVal(),3));
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",EITLERPGLOBAL.round(getAttribute("CURRENCY_RATE").getVal(),3));
            rsResultSet.updateDouble("TOTAL_AMOUNT",EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),3));
            rsResultSet.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getVal(),3));
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("MIR_TYPE",2);
            rsResultSet.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsResultSet.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
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
            rsResultSet.insertRow();
            
            
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("MIR_NO",(String)getAttribute("MIR_NO").getObj());
            rsHistory.updateString("MIR_DATE",(String)getAttribute("MIR_DATE").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsHistory.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsHistory.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsHistory.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateDouble("ACCESSABLE_VALUE",EITLERPGLOBAL.round(getAttribute("ACCESSABLE_VALUE").getVal(),3));
            rsHistory.updateDouble("INVOICE_AMOUNT",EITLERPGLOBAL.round(getAttribute("INVOICE_AMOUNT").getVal(),3));
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",EITLERPGLOBAL.round(getAttribute("CURRENCY_RATE").getVal(),3));
            rsHistory.updateDouble("TOTAL_AMOUNT",EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),3));
            rsHistory.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getVal(),3));
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("MIR_TYPE",2);
            rsHistory.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsHistory.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
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
            rsHistory.insertRow();
            
            //-----------Update Inward -----------------//
            String theSuppID=(String)getAttribute("SUPP_ID").getObj();
            String theChalanNo=(String)getAttribute("CHALAN_NO").getObj();
            int InwardNo=0;
            
            stInward=Conn.createStatement();
            rsInward=stInward.executeQuery("SELECT INWARD_NO FROM D_COM_INWARD_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID='"+theSuppID+"' AND CHALAN_NO='"+theChalanNo+"'");
            rsInward.first();
            if(rsInward.getRow()>0) {
                InwardNo=rsInward.getInt("INWARD_NO");
            }
            //-------------------------------------------//
            
            
            //====== Now turn of MIR Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_MIR_DETAIL WHERE MIR_NO='1'");
            rsItem.first();
            
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_MIR_LOT WHERE MIR_NO='1' ");
            rsLot.first();
            
            for(int i=1;i<=colMIRItems.size();i++) {
                clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("MIR_NO",(String)getAttribute("MIR_NO").getObj());
                rsItem.updateInt("MIR_TYPE",2);
                rsItem.updateInt("SR_NO",i);
                rsItem.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsItem.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsItem.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsItem.updateDouble("EXCESS_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("EXCESS_QTY").getVal(),3));
                rsItem.updateDouble("RECEIVED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("RECEIVED_QTY").getVal(),3));
                rsItem.updateDouble("REJECTED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("REJECTED_QTY").getVal(),3));
                rsItem.updateDouble("GROSS_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("GROSS_QTY").getVal(),3));
                rsItem.updateDouble("TOLERANCE_LIMIT",EITLERPGLOBAL.round(ObjItem.getAttribute("TOLERANCE_LIMIT").getVal(),3));
                
                rsItem.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsItem.updateDouble("GRN_RECD_QTY",0);
                rsItem.updateDouble("ISSUED_QTY",0);
                
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsItem.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateDouble("RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("RATE").getVal(),3));
                rsItem.updateDouble("LANDED_RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("LANDED_RATE").getVal(),3));
                rsItem.updateDouble("TOTAL_AMOUNT",EITLERPGLOBAL.round(ObjItem.getAttribute("TOTAL_AMOUNT").getVal(),3));
                rsItem.updateString("SHADE",(String)ObjItem.getAttribute("SHADE").getObj());
                rsItem.updateString("W_MIE",(String)ObjItem.getAttribute("W_MIE").getObj());
                rsItem.updateString("NO_CASE",(String)ObjItem.getAttribute("NO_CASE").getObj());
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsItem.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsItem.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsItem.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsItem.updateDouble("EXCESS",EITLERPGLOBAL.round(ObjItem.getAttribute("EXCESS").getVal(),3));
                rsItem.updateDouble("SHORTAGE",EITLERPGLOBAL.round(ObjItem.getAttribute("SHORTAGE").getVal(),3));
                rsItem.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsItem.updateDouble("CHALAN_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("CHALAN_QTY").getVal(),3));
                rsItem.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateString("STM_NO",(String)ObjItem.getAttribute("STM_NO").getObj());
                rsItem.updateInt("STM_SR_NO",(int)ObjItem.getAttribute("STM_SR_NO").getVal());
                rsItem.updateInt("STM_TYPE",2);
                rsItem.updateInt("STM_COMPANY_ID",(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal());
                rsItem.updateInt("STM_COMPANY_YEAR",(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal());
                rsItem.updateDouble("NET_AMOUNT",EITLERPGLOBAL.round(ObjItem.getAttribute("NET_AMOUNT").getVal(),3));
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
                
                rsItem.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
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
                
                rsItem.updateInt("COLUMN_31_ID",(int)ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsItem.updateString("COLUMN_31_FORMULA",(String)ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
               // rsItem.updateDouble("COLUMN_31_PER",ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsItem.updateDouble("COLUMN_31_AMT",ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsItem.updateString("COLUMN_31_CAPTION",(String)ObjItem.getAttribute("COLUMN_31_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_32_ID",(int)ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsItem.updateString("COLUMN_32_FORMULA",(String)ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
                //rsItem.updateDouble("COLUMN_32_PER",ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsItem.updateDouble("COLUMN_32_AMT",ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsItem.updateString("COLUMN_32_CAPTION",(String)ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_33_ID",(int)ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsItem.updateString("COLUMN_33_FORMULA",(String)ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_33_PER",ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsItem.updateDouble("COLUMN_33_AMT",ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsItem.updateString("COLUMN_33_CAPTION",(String)ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_34_ID",(int)ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsItem.updateString("COLUMN_34_FORMULA",(String)ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
               // rsItem.updateDouble("COLUMN_34_PER",ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsItem.updateDouble("COLUMN_34_AMT",ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsItem.updateString("COLUMN_34_CAPTION",(String)ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_35_ID",(int)ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsItem.updateString("COLUMN_35_FORMULA",(String)ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
                //rsItem.updateDouble("COLUMN_35_PER",ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsItem.updateDouble("COLUMN_35_AMT",ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsItem.updateString("COLUMN_35_CAPTION",(String)ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_36_ID",(int)ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsItem.updateString("COLUMN_36_FORMULA",(String)ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
                //rsItem.updateDouble("COLUMN_36_PER",ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsItem.updateDouble("COLUMN_36_AMT",ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsItem.updateString("COLUMN_36_CAPTION",(String)ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_37_ID",(int)ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsItem.updateString("COLUMN_37_FORMULA",(String)ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
                //rsItem.updateDouble("COLUMN_37_PER",ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsItem.updateDouble("COLUMN_37_AMT",ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsItem.updateString("COLUMN_37_CAPTION",(String)ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_38_ID",(int)ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsItem.updateString("COLUMN_38_FORMULA",(String)ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
                //rsItem.updateDouble("COLUMN_38_PER",ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsItem.updateDouble("COLUMN_38_AMT",ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsItem.updateString("COLUMN_38_CAPTION",(String)ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
               
                
                
                rsItem.updateDouble("PO_QTY",ObjItem.getAttribute("PO_QTY").getVal());
                rsItem.updateDouble("BALANCE_PO_QTY",ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                
                String BarcodeType=(String)ObjItem.getAttribute("BARCODE_TYPE").getObj();
                if(BarcodeType.trim().equals("")||BarcodeType.trim().equals("0")||BarcodeType.trim().startsWith("N")) {
                    BarcodeType="N";
                }
                if(BarcodeType.trim().startsWith("S")) {
                    BarcodeType="S";
                }
                if(BarcodeType.trim().startsWith("I")) {
                    BarcodeType="I";
                }
                rsItem.updateString("BARCODE_TYPE",BarcodeType);
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.updateBoolean("PRINTED",false);
                rsItem.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsItem.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("MIR_NO",(String)getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_TYPE",2);
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsHDetail.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsHDetail.updateDouble("EXCESS_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("EXCESS_QTY").getVal(),3));
                rsHDetail.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsHDetail.updateDouble("GRN_RECD_QTY",0);
                rsHDetail.updateDouble("ISSUED_QTY",0);
                rsHDetail.updateDouble("RECEIVED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("RECEIVED_QTY").getVal(),3));
                rsHDetail.updateDouble("REJECTED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("REJECTED_QTY").getVal(),3));
                rsHDetail.updateDouble("GROSS_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("GROSS_QTY").getVal(),3));
                rsHDetail.updateDouble("TOLERANCE_LIMIT",EITLERPGLOBAL.round(ObjItem.getAttribute("TOLERANCE_LIMIT").getVal(),3));
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsHDetail.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateDouble("RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("RATE").getVal(),3));
                rsHDetail.updateDouble("LANDED_RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("LANDED_RATE").getVal(),3));
                rsHDetail.updateDouble("TOTAL_AMOUNT",EITLERPGLOBAL.round(ObjItem.getAttribute("TOTAL_AMOUNT").getVal(),3));
                rsHDetail.updateString("SHADE",(String)ObjItem.getAttribute("SHADE").getObj());
                rsHDetail.updateString("W_MIE",(String)ObjItem.getAttribute("W_MIE").getObj());
                rsHDetail.updateString("NO_CASE",(String)ObjItem.getAttribute("NO_CASE").getObj());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsHDetail.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsHDetail.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsHDetail.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsHDetail.updateDouble("EXCESS",EITLERPGLOBAL.round(ObjItem.getAttribute("EXCESS").getVal(),3));
                rsHDetail.updateDouble("SHORTAGE",EITLERPGLOBAL.round(ObjItem.getAttribute("SHORTAGE").getVal(),3));
                rsHDetail.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsHDetail.updateDouble("CHALAN_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("CHALAN_QTY").getVal(),3));
                rsHDetail.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateString("STM_NO",(String)ObjItem.getAttribute("STM_NO").getObj());
                rsHDetail.updateInt("STM_SR_NO",(int)ObjItem.getAttribute("STM_SR_NO").getVal());
                rsHDetail.updateInt("STM_TYPE",2);
                rsHDetail.updateInt("STM_COMPANY_ID",(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal());
                rsHDetail.updateInt("STM_COMPANY_YEAR",(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal());
                rsHDetail.updateDouble("NET_AMOUNT",EITLERPGLOBAL.round(ObjItem.getAttribute("NET_AMOUNT").getVal(),3));
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
                
                rsHDetail.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
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
                
                rsHDetail.updateInt("COLUMN_31_ID",(int)ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsHDetail.updateString("COLUMN_31_FORMULA",(String)ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_31_PER",ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsHDetail.updateDouble("COLUMN_31_AMT",ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsHDetail.updateString("COLUMN_31_CAPTION",(String)ObjItem.getAttribute("COLUMN_31_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_32_ID",(int)ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsHDetail.updateString("COLUMN_32_FORMULA",(String)ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
                //rsHDetail.updateDouble("COLUMN_32_PER",ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsHDetail.updateDouble("COLUMN_32_AMT",ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsHDetail.updateString("COLUMN_32_CAPTION",(String)ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_33_ID",(int)ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsHDetail.updateString("COLUMN_33_FORMULA",(String)ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_33_PER",ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsHDetail.updateDouble("COLUMN_33_AMT",ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsHDetail.updateString("COLUMN_33_CAPTION",(String)ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_34_ID",(int)ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsHDetail.updateString("COLUMN_34_FORMULA",(String)ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_34_PER",ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsHDetail.updateDouble("COLUMN_34_AMT",ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsHDetail.updateString("COLUMN_34_CAPTION",(String)ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_35_ID",(int)ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsHDetail.updateString("COLUMN_35_FORMULA",(String)ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_35_PER",ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsHDetail.updateDouble("COLUMN_35_AMT",ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsHDetail.updateString("COLUMN_35_CAPTION",(String)ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_36_ID",(int)ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsHDetail.updateString("COLUMN_36_FORMULA",(String)ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_36_PER",ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsHDetail.updateDouble("COLUMN_36_AMT",ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsHDetail.updateString("COLUMN_36_CAPTION",(String)ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_37_ID",(int)ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsHDetail.updateString("COLUMN_37_FORMULA",(String)ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
                //rsHDetail.updateDouble("COLUMN_37_PER",ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsHDetail.updateDouble("COLUMN_37_AMT",ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsHDetail.updateString("COLUMN_37_CAPTION",(String)ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_38_ID",(int)ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsHDetail.updateString("COLUMN_38_FORMULA",(String)ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_38_PER",ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsHDetail.updateDouble("COLUMN_38_AMT",ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsHDetail.updateString("COLUMN_38_CAPTION",(String)ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
                
              
                rsHDetail.updateDouble("PO_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("PO_QTY").getVal(),3));
                rsHDetail.updateDouble("BALANCE_PO_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("BALANCE_PO_QTY").getVal(),3));
                
                String BarcodeType1=(String)ObjItem.getAttribute("BARCODE_TYPE").getObj();
                if(BarcodeType1.trim().equals("")||BarcodeType1.trim().equals("0")||BarcodeType1.trim().startsWith("N")) {
                    BarcodeType1="N";
                }
                if(BarcodeType1.trim().startsWith("S")) {
                    BarcodeType1="S";
                }
                if(BarcodeType1.trim().startsWith("I")) {
                    BarcodeType1="I";
                }
                rsHDetail.updateString("BARCODE_TYPE",BarcodeType1);
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("PRINTED",false);
                rsHDetail.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsHDetail.insertRow();
                
                
                //Update Inward Table
                String theItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                data.Execute("UPDATE D_COM_INWARD_DETAIL SET MIR_UPDATED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INWARD_NO="+InwardNo);
                //-------------------//
                
                
                
                //Now insert lot nos.
                for(int l=1;l<=ObjItem.colMIRLot.size();l++) {
                    clsMIRRawItemDetail ObjLot=(clsMIRRawItemDetail)ObjItem.colMIRLot.get(Integer.toString(l));
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsLot.updateString("MIR_NO",getAttribute("MIR_NO").getString());
                    rsLot.updateInt("MIR_SR_NO",i);
                    rsLot.updateInt("MIR_TYPE",2);
                    rsLot.updateInt("SR_NO",l);
                    rsLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsLot.updateString("AUTO_LOT_NO",""); //ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsLot.updateDouble("LOT_RECEIVED_QTY",EITLERPGLOBAL.round(ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble(),3));
                    rsLot.updateDouble("LOT_REJECTED_QTY",EITLERPGLOBAL.round(ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble(),3));
                    rsLot.updateDouble("LOT_ACCEPTED_QTY",EITLERPGLOBAL.round(ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble(),3));
                    rsLot.updateBoolean("APPROVED",false);
                    rsLot.updateString("APPROVED_DATE","0000-00-00");
                    rsLot.updateBoolean("CANCELLED",false);
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",1);
                    rsHLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsHLot.updateString("MIR_NO",getAttribute("MIR_NO").getString());
                    rsHLot.updateInt("MIR_SR_NO",i);
                    rsHLot.updateInt("MIR_TYPE",2);
                    rsHLot.updateInt("SR_NO",l);
                    rsHLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsHLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsHLot.updateString("AUTO_LOT_NO",""); //ObjLot.getAttribute("AUTO_LOT_NO").getString());
                    rsHLot.updateDouble("LOT_RECEIVED_QTY",EITLERPGLOBAL.round(ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble(),3));
                    rsHLot.updateDouble("LOT_REJECTED_QTY",EITLERPGLOBAL.round(ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble(),3));
                    rsHLot.updateDouble("LOT_ACCEPTED_QTY",EITLERPGLOBAL.round(ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble(),3));
                    rsHLot.updateBoolean("APPROVED",false);
                    rsHLot.updateString("APPROVED_DATE","0000-00-00");
                    rsHLot.updateBoolean("CANCELLED",false);
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=6; //MIR Raw Material
            ObjFlow.DocNo=(String)getAttribute("MIR_NO").getObj();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_MIR_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="MIR_NO";
            
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
            
            //=========== Add R&D Users if any item is required R&D approval prior to MIR Approval -----------//
            strSQL="SELECT DISTINCT(USER_ID) FROM D_INV_ITEM_RND_APPROVERS A,D_INV_ITEM_MASTER B,D_INV_MIR_DETAIL C WHERE A.ITEM_SYS_ID=B.ITEM_SYS_ID AND A.COMPANY_ID=B.COMPANY_ID AND C.ITEM_ID=B.ITEM_ID AND C.COMPANY_ID=B.COMPANY_ID AND B.RND_APPROVAL=1 AND C.MIR_NO='"+(String)getAttribute("MIR_NO").getObj()+"' AND C.MIR_TYPE="+(int)getAttribute("MIR_TYPE").getVal();
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                HashMap rndUsers=new HashMap();
                
                while(!rsTmp.isAfterLast()) {
                    clsHierarchy ObjNewUser=new clsHierarchy();
                    
                    ObjNewUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjNewUser.setAttribute("MODULE_ID",6);
                    ObjNewUser.setAttribute("DOC_NO",(String)getAttribute("MIR_NO").getObj());
                    ObjNewUser.setAttribute("DOC_DATE",(String)getAttribute("MIR_DATE").getObj());
                    ObjNewUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    
                    rndUsers.put(Integer.toString(rndUsers.size()+1),ObjNewUser);
                    rsTmp.next();
                }
                
                ApprovalFlow.AppendUsers(rndUsers);
            }
            //===============================================================================================//
            
            
            
            //==== If User (to whome document to be forwared) is not decided in the hierarchy ---
            // -- i.e. it is conditional, To will be zero, we have to fill it up here based on the criteria ---
            HashMap NewUsers=new HashMap();
            
            String tMIRNo=(String)getAttribute("MIR_NO").getObj();
            int tMIRType=(int)getAttribute("MIR_TYPE").getVal();
            int RecCount=0;
            int Counter=0;
            int DeptID=0;
            
            rsTmp=data.getResult("SELECT DISTINCT(DEPT_ID),PO_NO,PO_SR_NO,PO_TYPE AS RECCOUNT FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+tMIRNo+"' AND MIR_TYPE="+tMIRType);
            rsTmp.last();
            
            RecCount=rsTmp.getRow();
            
            rsTmp=data.getResult("SELECT DISTINCT(DEPT_ID),PO_NO,PO_SR_NO,PO_TYPE FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+tMIRNo+"' AND MIR_TYPE="+tMIRType);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter++;
                    DeptID=rsTmp.getInt("DEPT_ID");
                    
                    PONo=rsTmp.getString("PO_NO");
                    POSrNo=rsTmp.getInt("PO_SR_NO");
                    
                    //If PO Specified
                    if((!PONo.trim().equals(""))&&POSrNo>0) {
                        
                        ResultSet rsPR=data.getResult("SELECT C.REQ_NO,D.BUYER FROM D_PUR_PO_DETAIL A,D_INV_INDENT_DETAIL B,D_INV_REQ_DETAIL C,D_INV_REQ_HEADER D WHERE A.INDENT_NO=B.INDENT_NO AND A.INDENT_SR_NO=B.SR_NO AND B.MR_NO=C.REQ_NO AND B.MR_SR_NO=C.SR_NO AND A.PO_NO='"+PONo+"' AND C.REQ_NO=D.REQ_NO AND A.SR_NO="+POSrNo);
                        rsPR.first();
                        
                        if(rsPR.getRow()>0) {
                            int UserID=rsPR.getInt("BUYER");
                            
                            clsHierarchy ObjNewUser=new clsHierarchy();
                            
                            ObjNewUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            ObjNewUser.setAttribute("MODULE_ID",6);
                            ObjNewUser.setAttribute("DOC_NO",(String)getAttribute("MIR_NO").getObj());
                            ObjNewUser.setAttribute("DOC_DATE",(String)getAttribute("MIR_DATE").getObj());
                            ObjNewUser.setAttribute("USER_ID",UserID);
                            
                            NewUsers.put(Integer.toString(NewUsers.size()+1),ObjNewUser);
                        }
                        else {
                            
                            HashMap MIRApprovers=clsDepartment.getApproverList(EITLERPGLOBAL.gCompanyID, 6, DeptID);
                            
                            if(MIRApprovers.size()>0) {
                                
                                clsDeptApprovers ObjUser=(clsDeptApprovers)MIRApprovers.get(Integer.toString(1));
                                
                                clsHierarchy ObjNewUser=new clsHierarchy();
                                
                                ObjNewUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                                ObjNewUser.setAttribute("MODULE_ID",6);
                                ObjNewUser.setAttribute("DOC_NO",(String)getAttribute("MIR_NO").getObj());
                                ObjNewUser.setAttribute("DOC_DATE",(String)getAttribute("MIR_DATE").getObj());
                                ObjNewUser.setAttribute("USER_ID",(int)ObjUser.getAttribute("USER_ID").getVal());
                                
                                NewUsers.put(Integer.toString(NewUsers.size()+1),ObjNewUser);
                                
                                
                            }
                            
                        }
                        
                        
                    }
                    else {
                        HashMap MIRApprovers=clsDepartment.getApproverList(EITLERPGLOBAL.gCompanyID, 6, DeptID);
                        
                        if(MIRApprovers.size()>0) {
                            
                            clsDeptApprovers ObjUser=(clsDeptApprovers)MIRApprovers.get(Integer.toString(1));
                            
                            clsHierarchy ObjNewUser=new clsHierarchy();
                            
                            ObjNewUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            ObjNewUser.setAttribute("MODULE_ID",6);
                            ObjNewUser.setAttribute("DOC_NO",(String)getAttribute("MIR_NO").getObj());
                            ObjNewUser.setAttribute("DOC_DATE",(String)getAttribute("MIR_DATE").getObj());
                            ObjNewUser.setAttribute("USER_ID",(int)ObjUser.getAttribute("USER_ID").getVal());
                            
                            NewUsers.put(Integer.toString(NewUsers.size()+1),ObjNewUser);
                            
                            
                        }
                        
                    }
                    
                    
                    if(Counter==RecCount) {
                        
                        HashMap MIRApprovers=clsDepartment.getApproverList(EITLERPGLOBAL.gCompanyID, 6, DeptID);
                        
                        if(MIRApprovers.size()>0) {
                            
                            clsDeptApprovers ObjUser=(clsDeptApprovers)MIRApprovers.get(Integer.toString(MIRApprovers.size()));
                            
                            clsHierarchy ObjNewUser=new clsHierarchy();
                            
                            ObjNewUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                            ObjNewUser.setAttribute("MODULE_ID",6);
                            ObjNewUser.setAttribute("DOC_NO",(String)getAttribute("MIR_NO").getObj());
                            ObjNewUser.setAttribute("DOC_DATE",(String)getAttribute("MIR_DATE").getObj());
                            ObjNewUser.setAttribute("USER_ID",(int)ObjUser.getAttribute("USER_ID").getVal());
                            
                            NewUsers.put(Integer.toString(NewUsers.size()+1),ObjNewUser);
                        }
                    }
                    
                    rsTmp.next();
                }
                ApprovalFlow.AppendUsersEx(NewUsers);
            }
            //===================================================================///
            
            String SQL = "SELECT DISTINCT MIR_SR_NO,SR_NO FROM D_INV_MIR_LOT WHERE MIR_NO='"+MIRNo+"' ORDER BY MIR_SR_NO";
            ResultSet rsLotUpdate = data.getResult(SQL);
            rsLotUpdate.first();
            String UpdateRecord = "";
            while(!rsLotUpdate.isAfterLast()) {
                int MIRSrNo = rsLotUpdate.getInt("MIR_SR_NO");
                int SrNo = rsLotUpdate.getInt("SR_NO");
                String AutoLotNo = EnterMIRLotQty.getNextLOTNo(EITLERPGLOBAL.gCompanyID);
                UpdateRecord = "UPDATE D_INV_MIR_LOT SET AUTO_LOT_NO='"+AutoLotNo+"' WHERE MIR_NO='"+MIRNo+"' " +
                "AND MIR_SR_NO="+ MIRSrNo +" AND SR_NO="+SrNo+" ";
                data.Execute(UpdateRecord);
                UpdateRecord = "UPDATE D_INV_MIR_LOT_H SET AUTO_LOT_NO='"+AutoLotNo+"' WHERE MIR_NO='"+MIRNo+"' " +
                "AND MIR_SR_NO="+ MIRSrNo +" AND SR_NO="+SrNo+" ";
                data.Execute(UpdateRecord);
                rsLotUpdate.next();
            }
            
            if(AStatus.equals("F")) {
                UpdateRecord = "UPDATE D_INV_MIR_LOT SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE MIR_NO='"+MIRNo+"' ";
                UpdateRecord = "UPDATE D_INV_MIR_LOT_H SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE MIR_NO='"+MIRNo+"' ";
            }
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    private void RevertStockEffect() {
        Statement stItemMaster,stItem,stTmp;
        ResultSet rsItemMaster,rsItem,rsTmp;
        String strSQL="",MIRNo="",ItemID="",BOENo="",LotNo="",WareHouseID="",LocationID="",PONo="";
        int CompanyID=0,POSrNo=0,POType=0;
        double Qty=0,RejectedQty=0;
        
        try {
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            MIRNo=(String)getAttribute("MIR_NO").getObj();
            
            
            //Now give reverse effect to MIR Table
            strSQL="SELECT * FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_TYPE=2";
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                PONo=rsTmp.getString("PO_NO");
                POSrNo=rsTmp.getInt("PO_SR_NO");
                POType=rsTmp.getInt("PO_TYPE");
                Qty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                
                // Update Purchase Order - Update Received Qty
                data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY-"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo+" AND PO_TYPE="+POType);
                
                rsTmp.next();
            }
        }
        catch(Exception e) {
        }
    }
    
    
    //Updates current record
    public boolean Update() {
        Connection tmpConn;
        Statement stItem,stLot,stStock,stTmp,stItemMaster,stPO,stSTM,stHistory,stHDetail,stHLot;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsPO,rsHistory,rsHDetail,rsHLot;
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="",LocationID="",PONo="",strSQL="",BOEDate="",STMNo="",MIRNo="";
        int CompanyID=0,POSrNo=0,STMSrNo=0,POType=0;
        double RejectedQty=0,Qty=0,ToleranceLimit=0;
        
        Statement stHeader;
        ResultSet rsHeader;
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
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("MIR_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            if(getAttribute("SUPP_ID").getString().trim().equals("000000")) {
                //LastError="Supplier code is not valid";
                //return false;
            }
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_MIR_HEADER_H WHERE MIR_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_MIR_DETAIL_H WHERE MIR_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_MIR_LOT_H WHERE MIR_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("MIR_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(MIR_NO)='"+theDocNo+"' AND MIR_TYPE=2");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            MIRNo=(String)getAttribute("MIR_NO").getObj();
            
            //=========== Check the Quantities entered against GRN. ============= //
            for(int i=1;i<=colMIRItems.size();i++) {
                clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                PONo=(String)ObjItem.getAttribute("PO_NO").getObj();
                POSrNo=(int)ObjItem.getAttribute("PO_SR_NO").getVal();
                POType=(int)ObjItem.getAttribute("PO_TYPE").getVal();
                ItemID=ObjItem.getAttribute("ITEM_ID").getString();
                
                ToleranceLimit=clsItem.getToleranceLimit(CompanyID,(String)ObjItem.getAttribute("ITEM_ID").getObj());
                
                double POQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
                
                STMNo=(String)ObjItem.getAttribute("STM_NO").getObj();
                STMSrNo=(int)ObjItem.getAttribute("STM_SR_NO").getVal();
                
                //---New Change --
                int STMCompany=(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal();
                int STMCompanyYear=(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal();
                String dbURL=clsFinYear.getDBURL(STMCompany,STMCompanyYear);
                
                tmpConn=data.getConn(dbURL);
                //-------------
                
                
                if((!STMNo.trim().equals(""))&&(STMSrNo>0)) //S.T.M. No. entered
                {
                    //Get the PO Qty.
                    stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_STM_DETAIL WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND SR_NO="+STMSrNo+" AND STM_TYPE=2";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        POQty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                    }
                    
                    //Get Total Qty Entered in MIR Against this STM No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND STM_NO='"+STMNo+"' AND STM_SR_NO="+STMSrNo+" AND STM_TYPE=2 AND NOT(MIR_NO='"+MIRNo+"' AND MIR_TYPE=2) AND MIR_NO NOT IN(SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
                    }
                    
                    CurrentQty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                    
                    /*if((CurrentQty+PrevQty-(( (CurrentQty+PrevQty)*ToleranceLimit)/100)) > POQty) //If total Qty exceeds PO Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds STM No. "+STMNo+" Sr. No. "+STMSrNo+" qty "+POQty+". Please verify the input.";
                        return false;
                    }*/
                }
                
                
                if((!PONo.trim().equals(""))&&(POSrNo>0)) //P.O. No. entered
                {
                    //Get the PO Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY,ITEM_ID FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND SR_NO="+POSrNo+" AND PO_TYPE="+POType;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        POQty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                        
                        if((!ItemID.trim().equals(""))&&(POType!=2)&&(POType!=6)&&(POType!=7)) {
                            if(!rsTmp.getString("ITEM_ID").trim().equals(ItemID)) {
                                LastError="Item Code in MIR doesn't match with PO. Original Item code is "+rsTmp.getString("ITEM_ID");
                                return false;
                            }
                        }
                        
                    }
                    
                    
                    //Get Total Qty Entered in MIR Against this PO No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_SR_NO="+POSrNo+" AND PO_TYPE="+POType+" AND NOT(MIR_NO='"+MIRNo+"' AND MIR_TYPE=2) AND MIR_NO NOT IN(SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
                    }
                    
                    CurrentQty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                    
                    /*if((CurrentQty+PrevQty-((CurrentQty+PrevQty)*ToleranceLimit)/100) > POQty) //If total Qty exceeds PO Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds PO No. "+PONo+" Sr. No. "+POSrNo+" qty "+POQty+". Please verify the input.";
                        //return false;
                    }*/
                }
            }
            //============== GRN Checking Completed ====================//
            
            
            if(Validate) {
                //=========== Check the Max Qty Level.============= //
                for(int i=1;i<=colMIRItems.size();i++) {
                    clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                    ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    
                    double MaxQty=clsItem.getMaxQty(CompanyID, ItemID);
                    
                    if(MaxQty>0) {
                        double CurrentQty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                        double AvailableQty=EITLERPGLOBAL.round(clsItem.getAvailableQty(CompanyID, ItemID),3);
                        
                        if((CurrentQty+AvailableQty)>MaxQty) {
                            LastError="Total Receipt Qty "+(CurrentQty+AvailableQty)+" exceeds Maximum limit "+MaxQty+" for Item "+ItemID;
                            return false;
                        }
                    }
                }
                //=================================================///
                
                
                
                //===============Update P.O.======================//
                // Update the Stock only after Final Approval //
                
                
                if(AStatus.equals("F")) {
                    for(int i=1;i<=colMIRItems.size();i++) {
                        clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                        
                        PONo=(String)ObjItem.getAttribute("PO_NO").getObj();
                        POSrNo=(int)ObjItem.getAttribute("PO_SR_NO").getVal();
                        POType=(int)ObjItem.getAttribute("PO_TYPE").getVal();
                        
                        Qty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                        ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                        
                        STMNo=(String)ObjItem.getAttribute("STM_NO").getObj();
                        STMSrNo=(int)ObjItem.getAttribute("STM_SR_NO").getVal();
                        
                        
                        
                        //================= Update STM ==============//
                        if((!STMNo.trim().equals(""))&&(STMSrNo>0)) {
                            //---Updating STM Qty -----------//
                            int STMCompany=(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal();
                            int STMCompanyYear=(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal();
                            String dbURL=clsFinYear.getDBURL(STMCompany,STMCompanyYear);
                            
                            tmpConn=data.getConn(dbURL);
                            stSTM=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            //-------------
                            
                            stSTM.executeUpdate("UPDATE D_INV_STM_DETAIL SET RECEIVED_QTY=RECEIVED_QTY+"+Qty+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE STM_NO='"+STMNo+"' AND SR_NO="+STMSrNo+" AND STM_TYPE=2");
                            stSTM.executeUpdate("UPDATE D_INV_STM_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE STM_NO='"+STMNo+"'");
                        }
                        //===========================================//
                        
                        //============== Update P.O. ============ //
                        if((!PONo.trim().equals(""))&&(POSrNo>0)) {
                            if(POType!=7) //Do not update P.O. in case of Contract Purchase. 7 - Contract Purchase
                            {
                                data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY+"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);
                                data.Execute("UPDATE D_PUR_PO_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"'");
                                data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY+"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);
                            }
                        }
                        //=============== P.O. Updation Completed ===============//
                    }
                }
                //==========P.O. Updation Complete===============//
            }
            
            rsResultSet.updateString("MIR_DATE",(String)getAttribute("MIR_DATE").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsResultSet.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsResultSet.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateDouble("ACCESSABLE_VALUE",EITLERPGLOBAL.round(getAttribute("ACCESSABLE_VALUE").getVal(),3));
            rsResultSet.updateDouble("INVOICE_AMOUNT",EITLERPGLOBAL.round(getAttribute("INVOICE_AMOUNT").getVal(),3));
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",EITLERPGLOBAL.round(getAttribute("CURRENCY_RATE").getVal(),3));
            rsResultSet.updateDouble("TOTAL_AMOUNT",EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),3));
            rsResultSet.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getVal(),3));
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("MIR_TYPE",2);
            rsResultSet.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsResultSet.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
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
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_MIR_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+(String)getAttribute("MIR_NO").getObj()+"' AND MIR_TYPE=2");
            RevNo++;
            String RevDocNo=(String)getAttribute("MIR_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("MIR_NO",(String)getAttribute("MIR_NO").getObj());
            rsHistory.updateString("MIR_DATE",(String)getAttribute("MIR_DATE").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("REFA",(String)getAttribute("REFA").getObj());
            rsHistory.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE",(String)getAttribute("CHALAN_DATE").getObj());
            rsHistory.updateString("LR_NO",(String)getAttribute("LR_NO").getObj());
            rsHistory.updateString("LR_DATE",(String)getAttribute("LR_DATE").getObj());
            rsHistory.updateString("INVOICE_NO",(String)getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE",(String)getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("TRANSPORTER_NAME",(String)getAttribute("TRANSPORTER_NAME").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateDouble("ACCESSABLE_VALUE",EITLERPGLOBAL.round(getAttribute("ACCESSABLE_VALUE").getVal(),3));
            rsHistory.updateDouble("INVOICE_AMOUNT",EITLERPGLOBAL.round(getAttribute("INVOICE_AMOUNT").getVal(),3));
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",EITLERPGLOBAL.round(getAttribute("CURRENCY_RATE").getVal(),3));
            rsHistory.updateDouble("TOTAL_AMOUNT",EITLERPGLOBAL.round(getAttribute("TOTAL_AMOUNT").getVal(),3));
            rsHistory.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getVal(),3));
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CENVATED_ITEMS",getAttribute("CENVATED_ITEMS").getBool());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("MIR_TYPE",2);
            rsHistory.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsHistory.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
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
            rsHistory.insertRow();
            
            
            //====== Now turn of MIR Items ======
            String LastLotNo = EnterMIRLotQty.getLastLotNo(EITLERPGLOBAL.gCompanyID); 
            data.Execute("DELETE FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_TYPE=2");
            data.Execute("DELETE FROM D_INV_MIR_LOT WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_TYPE=2");
            
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_MIR_DETAIL WHERE MIR_NO='1'");
            rsItem.first();
            
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_MIR_LOT WHERE MIR_NO='1' ");
            rsLot.first();
            
            for(int i=1;i<=colMIRItems.size();i++) {
                clsMIRRawItem ObjItem=(clsMIRRawItem)colMIRItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("MIR_NO",(String)getAttribute("MIR_NO").getObj());
                rsItem.updateInt("MIR_TYPE",2);
                rsItem.updateInt("SR_NO",i);
                rsItem.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsItem.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsItem.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsItem.updateDouble("EXCESS_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("EXCESS_QTY").getVal(),3));
                rsItem.updateDouble("RECEIVED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("RECEIVED_QTY").getVal(),3));
                rsItem.updateDouble("REJECTED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("REJECTED_QTY").getVal(),3));
                rsItem.updateDouble("GROSS_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("GROSS_QTY").getVal(),3));
                rsItem.updateDouble("TOLERANCE_LIMIT",EITLERPGLOBAL.round(ObjItem.getAttribute("TOLERANCE_LIMIT").getVal(),3));
                
                rsItem.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsItem.updateDouble("GRN_RECD_QTY",0);
                rsItem.updateDouble("ISSUED_QTY",0);
                
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsItem.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateDouble("RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("RATE").getVal(),3));
                rsItem.updateDouble("LANDED_RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("LANDED_RATE").getVal(),3));
                rsItem.updateDouble("TOTAL_AMOUNT",EITLERPGLOBAL.round(ObjItem.getAttribute("TOTAL_AMOUNT").getVal(),3));
                rsItem.updateString("SHADE",(String)ObjItem.getAttribute("SHADE").getObj());
                rsItem.updateString("W_MIE",(String)ObjItem.getAttribute("W_MIE").getObj());
                rsItem.updateString("NO_CASE",(String)ObjItem.getAttribute("NO_CASE").getObj());
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsItem.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsItem.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsItem.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsItem.updateDouble("EXCESS",EITLERPGLOBAL.round(ObjItem.getAttribute("EXCESS").getVal(),3));
                rsItem.updateDouble("SHORTAGE",EITLERPGLOBAL.round(ObjItem.getAttribute("SHORTAGE").getVal(),3));
                rsItem.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsItem.updateDouble("CHALAN_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("CHALAN_QTY").getVal(),3));
                rsItem.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateString("STM_NO",(String)ObjItem.getAttribute("STM_NO").getObj());
                rsItem.updateInt("STM_SR_NO",(int)ObjItem.getAttribute("STM_SR_NO").getVal());
                rsItem.updateInt("STM_TYPE",2);
                rsItem.updateInt("STM_COMPANY_ID",(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal());
                rsItem.updateInt("STM_COMPANY_YEAR",(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal());
                rsItem.updateDouble("NET_AMOUNT",EITLERPGLOBAL.round(ObjItem.getAttribute("NET_AMOUNT").getVal(),3));
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
                
                rsItem.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
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
                
                 rsItem.updateInt("COLUMN_31_ID",(int)ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsItem.updateString("COLUMN_31_FORMULA",(String)ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
               // rsItem.updateDouble("COLUMN_31_PER",ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsItem.updateDouble("COLUMN_31_AMT",ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsItem.updateString("COLUMN_31_CAPTION",(String)ObjItem.getAttribute("COLUMN_31_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_32_ID",(int)ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsItem.updateString("COLUMN_32_FORMULA",(String)ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
               // rsItem.updateDouble("COLUMN_32_PER",ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsItem.updateDouble("COLUMN_32_AMT",ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsItem.updateString("COLUMN_32_CAPTION",(String)ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_33_ID",(int)ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsItem.updateString("COLUMN_33_FORMULA",(String)ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
               rsItem.updateDouble("COLUMN_33_PER",ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsItem.updateDouble("COLUMN_33_AMT",ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsItem.updateString("COLUMN_33_CAPTION",(String)ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());
                
                
                rsItem.updateInt("COLUMN_34_ID",(int)ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsItem.updateString("COLUMN_34_FORMULA",(String)ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
               // rsItem.updateDouble("COLUMN_34_PER",ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsItem.updateDouble("COLUMN_34_AMT",ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsItem.updateString("COLUMN_34_CAPTION",(String)ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_35_ID",(int)ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsItem.updateString("COLUMN_35_FORMULA",(String)ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
                //rsItem.updateDouble("COLUMN_35_PER",ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsItem.updateDouble("COLUMN_35_AMT",ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsItem.updateString("COLUMN_35_CAPTION",(String)ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_36_ID",(int)ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsItem.updateString("COLUMN_36_FORMULA",(String)ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
                //rsItem.updateDouble("COLUMN_36_PER",ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsItem.updateDouble("COLUMN_36_AMT",ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsItem.updateString("COLUMN_36_CAPTION",(String)ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_37_ID",(int)ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsItem.updateString("COLUMN_37_FORMULA",(String)ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
               // rsItem.updateDouble("COLUMN_37_PER",ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsItem.updateDouble("COLUMN_37_AMT",ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsItem.updateString("COLUMN_37_CAPTION",(String)ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_38_ID",(int)ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsItem.updateString("COLUMN_38_FORMULA",(String)ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
               // rsItem.updateDouble("COLUMN_38_PER",ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsItem.updateDouble("COLUMN_38_AMT",ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsItem.updateString("COLUMN_38_CAPTION",(String)ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
             
                
                rsItem.updateDouble("PO_QTY",ObjItem.getAttribute("PO_QTY").getVal());
                rsItem.updateDouble("BALANCE_PO_QTY",ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                
                String BarcodeType=(String)ObjItem.getAttribute("BARCODE_TYPE").getObj();
                if(BarcodeType.trim().equals("")||BarcodeType.trim().equals("0")||BarcodeType.trim().startsWith("N")) {
                    BarcodeType="N";
                }
                if(BarcodeType.trim().startsWith("S")) {
                    BarcodeType="S";
                }
                if(BarcodeType.trim().startsWith("I")) {
                    BarcodeType="I";
                }
                
                rsItem.updateString("BARCODE_TYPE",BarcodeType);
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.updateBoolean("PRINTED",false);
                rsItem.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsItem.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("MIR_NO",(String)getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_TYPE",2);
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsHDetail.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("EXCESS_QTY",ObjItem.getAttribute("EXCESS_QTY").getVal());
                rsHDetail.updateDouble("RECEIVED_QTY",ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsHDetail.updateDouble("REJECTED_QTY",ObjItem.getAttribute("REJECTED_QTY").getVal());
                rsHDetail.updateDouble("GROSS_QTY",ObjItem.getAttribute("GROSS_QTY").getVal());
                rsHDetail.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsHDetail.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateString("SHADE",(String)ObjItem.getAttribute("SHADE").getObj());
                rsHDetail.updateString("W_MIE",(String)ObjItem.getAttribute("W_MIE").getObj());
                rsHDetail.updateString("NO_CASE",(String)ObjItem.getAttribute("NO_CASE").getObj());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateString("MATERIAL_CODE",(String)ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsHDetail.updateString("MATERIAL_DESC",(String)ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsHDetail.updateString("QUALITY_NO",(String)ObjItem.getAttribute("QUALITY_NO").getObj());
                rsHDetail.updateString("PAGE_NO",(String)ObjItem.getAttribute("PAGE_NO").getObj());
                rsHDetail.updateDouble("EXCESS",ObjItem.getAttribute("EXCESS").getVal());
                rsHDetail.updateDouble("SHORTAGE",ObjItem.getAttribute("SHORTAGE").getVal());
                rsHDetail.updateString("L_F_NO",(String)ObjItem.getAttribute("L_F_NO").getObj());
                rsHDetail.updateDouble("CHALAN_QTY",ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsHDetail.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateString("STM_NO",(String)ObjItem.getAttribute("STM_NO").getObj());
                rsHDetail.updateInt("STM_SR_NO",(int)ObjItem.getAttribute("STM_SR_NO").getVal());
                rsHDetail.updateInt("STM_TYPE",2);
                rsHDetail.updateInt("STM_COMPANY_ID",(int)ObjItem.getAttribute("STM_COMPANY_ID").getVal());
                rsHDetail.updateInt("STM_COMPANY_YEAR",(int)ObjItem.getAttribute("STM_COMPANY_YEAR").getVal());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
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
                
                rsHDetail.updateInt("COLUMN_11_ID",(int)ObjItem.getAttribute("COLUMN_11_ID").getVal());
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
                
                rsHDetail.updateInt("COLUMN_31_ID",(int)ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsHDetail.updateString("COLUMN_31_FORMULA",(String)ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
                //rsHDetail.updateDouble("COLUMN_31_PER",ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsHDetail.updateDouble("COLUMN_31_AMT",ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsHDetail.updateString("COLUMN_31_CAPTION",(String)ObjItem.getAttribute("COLUMN_31_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_32_ID",(int)ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsHDetail.updateString("COLUMN_32_FORMULA",(String)ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
                //rsHDetail.updateDouble("COLUMN_32_PER",ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsHDetail.updateDouble("COLUMN_32_AMT",ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsHDetail.updateString("COLUMN_32_CAPTION",(String)ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_33_ID",(int)ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsHDetail.updateString("COLUMN_33_FORMULA",(String)ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
               rsHDetail.updateDouble("COLUMN_33_PER",ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsHDetail.updateDouble("COLUMN_33_AMT",ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsHDetail.updateString("COLUMN_33_CAPTION",(String)ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_34_ID",(int)ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsHDetail.updateString("COLUMN_34_FORMULA",(String)ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_34_PER",ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsHDetail.updateDouble("COLUMN_34_AMT",ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsHDetail.updateString("COLUMN_34_CAPTION",(String)ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_35_ID",(int)ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsHDetail.updateString("COLUMN_35_FORMULA",(String)ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_35_PER",ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsHDetail.updateDouble("COLUMN_35_AMT",ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsHDetail.updateString("COLUMN_35_CAPTION",(String)ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_36_ID",(int)ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsHDetail.updateString("COLUMN_36_FORMULA",(String)ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_36_PER",ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsHDetail.updateDouble("COLUMN_36_AMT",ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsHDetail.updateString("COLUMN_36_CAPTION",(String)ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());

                rsHDetail.updateInt("COLUMN_37_ID",(int)ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsHDetail.updateString("COLUMN_37_FORMULA",(String)ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_37_PER",ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsHDetail.updateDouble("COLUMN_37_AMT",ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsHDetail.updateString("COLUMN_37_CAPTION",(String)ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_38_ID",(int)ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsHDetail.updateString("COLUMN_38_FORMULA",(String)ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
               // rsHDetail.updateDouble("COLUMN_38_PER",ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsHDetail.updateDouble("COLUMN_38_AMT",ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsHDetail.updateString("COLUMN_38_CAPTION",(String)ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
                
                rsHDetail.updateDouble("PO_QTY",ObjItem.getAttribute("PO_QTY").getVal());
                rsHDetail.updateDouble("BALANCE_PO_QTY",ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                
                String BarcodeType3=(String)ObjItem.getAttribute("BARCODE_TYPE").getObj();
                if(BarcodeType3.trim().equals("")||BarcodeType3.trim().equals("0")||BarcodeType3.trim().startsWith("N")) {
                    BarcodeType3="N";
                }
                if(BarcodeType3.trim().startsWith("S")) {
                    BarcodeType3="S";
                }
                if(BarcodeType3.trim().startsWith("I")) {
                    BarcodeType3="I";
                }
                rsHDetail.updateString("BARCODE_TYPE",BarcodeType3);
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("PRINTED",false);
                rsHDetail.updateString("RND_DEDUCTION_REASON",ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsHDetail.insertRow();
                
                
                //Now insert lot nos.
                for(int l=1;l<=ObjItem.colMIRLot.size();l++) {
                    clsMIRRawItemDetail ObjLot=(clsMIRRawItemDetail)ObjItem.colMIRLot.get(Integer.toString(l));
                    String AutoLotNo = "";
                    rsLot.moveToInsertRow();
                    rsLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsLot.updateString("MIR_NO",getAttribute("MIR_NO").getString());
                    rsLot.updateInt("MIR_SR_NO",i);
                    rsLot.updateInt("MIR_TYPE",2);
                    rsLot.updateInt("SR_NO",l);
                    rsLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    if(ObjLot.getAttribute("AUTO_LOT_NO").getString().equals("")) {
                        AutoLotNo = EnterMIRLotQty.getNextLOTNo(EITLERPGLOBAL.gCompanyID,LastLotNo);
                        LastLotNo = AutoLotNo;
                    } else{
                        AutoLotNo = ObjLot.getAttribute("AUTO_LOT_NO").getString();
                    }
                    rsLot.updateString("AUTO_LOT_NO",AutoLotNo);
                    rsLot.updateDouble("LOT_RECEIVED_QTY",ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble());
                    rsLot.updateDouble("LOT_REJECTED_QTY",ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble());
                    rsLot.updateDouble("LOT_ACCEPTED_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsLot.updateBoolean("APPROVED",false);
                    rsLot.updateString("APPROVED_DATE","0000-00-00");
                    rsLot.updateBoolean("CANCELLED",false);
                    rsLot.updateBoolean("CHANGED",true);
                    rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsLot.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",RevNo);
                    rsHLot.updateInt("COMPANY_ID",getAttribute("COMPANY_ID").getInt());
                    rsHLot.updateString("MIR_NO",getAttribute("MIR_NO").getString());
                    rsHLot.updateInt("MIR_SR_NO",i);
                    rsHLot.updateInt("MIR_TYPE",2);
                    rsHLot.updateInt("SR_NO",l);
                    rsHLot.updateString("ITEM_ID",ObjLot.getAttribute("ITEM_ID").getString());
                    rsHLot.updateString("ITEM_LOT_NO",ObjLot.getAttribute("ITEM_LOT_NO").getString());
                    rsHLot.updateString("AUTO_LOT_NO",AutoLotNo);
                    rsHLot.updateDouble("LOT_RECEIVED_QTY",ObjLot.getAttribute("LOT_RECEIVED_QTY").getDouble());
                    rsHLot.updateDouble("LOT_REJECTED_QTY",ObjLot.getAttribute("LOT_REJECTED_QTY").getDouble());
                    rsHLot.updateDouble("LOT_ACCEPTED_QTY",ObjLot.getAttribute("LOT_ACCEPTED_QTY").getDouble());
                    rsHLot.updateBoolean("APPROVED",false);
                    rsHLot.updateString("APPROVED_DATE","0000-00-00");
                    rsHLot.updateBoolean("CANCELLED",false);
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=6; //MIR Raw Material
            ObjFlow.DocNo=(String)getAttribute("MIR_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_MIR_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="MIR_NO";
            
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
                data.Execute("UPDATE D_INV_MIR_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+ObjFlow.DocNo+"' AND MIR_TYPE=2");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=6 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            
            
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
            //--------- Approval Flow Update complete -----------
            String UpdateRecord = "";
            if(AStatus.equals("F")) {
                UpdateRecord = "UPDATE D_INV_MIR_LOT SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE MIR_NO='"+MIRNo+"' ";
                data.Execute(UpdateRecord);
                UpdateRecord = "UPDATE D_INV_MIR_LOT_H SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"', " +
                "CHANGED=1, CHANGED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE MIR_NO='"+MIRNo+"' ";
                data.Execute(UpdateRecord);
            }
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
            String lDocNo=(String)getAttribute("MIR_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                //Give reverse effect to Stock
                //RevertStockEffect();
                
                data.Execute("DELETE FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+lCompanyID+" AND MIR_NO='"+lDocNo.trim()+"' AND MIR_TYPE=2");
                data.Execute("DELETE FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND MIR_NO='"+lDocNo.trim()+"' AND MIR_TYPE=2");
                data.Execute("DELETE FROM D_INV_MIR_LOT WHERE COMPANY_ID="+lCompanyID+" MIR_NO='"+lDocNo.trim()+"' AND MIR_TYPE=2");
                
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
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND MIR_NO='"+pDocNo+"' AND MIR_TYPE=2";
        clsMIRRaw ObjMIR = new clsMIRRaw();
        ObjMIR.Filter(strCondition,pCompanyID);
        return ObjMIR;
    }
    
    public Object getObject(int pCompanyID,String pDocNo,int pType) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND MIR_NO='"+pDocNo+"' AND MIR_TYPE="+pType;
        clsMIRRaw ObjMIR = new clsMIRRaw();
        ObjMIR.Filter(strCondition,pCompanyID,pType);
        return ObjMIR;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_MIR_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MIR_TYPE=2 AND MIR_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND MIR_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY MIR_NO" ;
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
            String strSql = "SELECT * FROM D_INV_MIR_HEADER " + pCondition ;
            Conn=data.getConn(URL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MIR_TYPE=2 AND MIR_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND MIR_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY MIR_NO";
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
            String strSql = "SELECT * FROM D_INV_MIR_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if(rsResultSet.getRow()>0) {
                Ready=true;
                rsResultSet.first();
                setData(pType);
                return true;
            }
            else {
                strSql = "SELECT * FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND MIR_TYPE="+pType+" AND MIR_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND MIR_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY MIR_NO" ;
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
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
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("MIR_NO",rsResultSet.getString("MIR_NO"));
            setAttribute("MIR_DATE",rsResultSet.getString("MIR_DATE"));
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
            setAttribute("INVOICE_AMOUNT",rsResultSet.getDouble("INVOICE_AMOUNT"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("GROSS_AMOUNT",rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CENVATED_ITEMS",rsResultSet.getBoolean("CENVATED_ITEMS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("MIR_TYPE",rsResultSet.getInt("MIR_TYPE"));
            setAttribute("OPEN_STATUS",rsResultSet.getString("OPEN_STATUS"));
            setAttribute("FOR_STORE",rsResultSet.getInt("FOR_STORE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
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
            
            colMIRItems.clear(); //Clear existing data
            
            String MIRNo=(String)getAttribute("MIR_NO").getObj();
            
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_MIR_DETAIL_H WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_TYPE=2 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_TYPE=2 ORDER BY SR_NO");
            }
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                
                clsMIRRawItem ObjItem=new clsMIRRawItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("MIR_NO",rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_SR_NO",rsItem.getInt("PO_SR_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsItem.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("EXCESS_QTY",rsItem.getDouble("EXCESS_QTY"));
                ObjItem.setAttribute("RECEIVED_QTY",rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("REJECTED_QTY",rsItem.getDouble("REJECTED_QTY"));
                ObjItem.setAttribute("GROSS_QTY",rsItem.getDouble("GROSS_QTY"));
                ObjItem.setAttribute("TOLERANCE_LIMIT",rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("REJECTED_REASON_ID",rsItem.getInt("REJECTED_REASON_ID"));
                ObjItem.setAttribute("DEPT_ID",rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE",rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("TOTAL_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("SHADE",rsItem.getString("SHADE"));
                ObjItem.setAttribute("W_MIE",rsItem.getString("W_MIE"));
                ObjItem.setAttribute("NO_CASE",rsItem.getString("NO_CASE"));
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN",rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS",rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
                ObjItem.setAttribute("MATERIAL_CODE",rsItem.getString("MATERIAL_CODE"));
                ObjItem.setAttribute("MATERIAL_DESC",rsItem.getString("MATERIAL_DESC"));
                ObjItem.setAttribute("QUALITY_NO",rsItem.getString("QUALITY_NO"));
                ObjItem.setAttribute("PAGE_NO",rsItem.getString("PAGE_NO"));
                ObjItem.setAttribute("EXCESS",rsItem.getDouble("EXCESS"));
                ObjItem.setAttribute("SHORTAGE",rsItem.getDouble("SHORTAGE"));
                ObjItem.setAttribute("L_F_NO",rsItem.getString("L_F_NO"));
                ObjItem.setAttribute("CHALAN_QTY",rsItem.getDouble("CHALAN_QTY"));
                ObjItem.setAttribute("INDENT_NO",rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO",rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("STM_NO",rsItem.getString("STM_NO"));
                ObjItem.setAttribute("STM_SR_NO",rsItem.getInt("STM_SR_NO"));
                ObjItem.setAttribute("STM_TYPE",rsItem.getInt("STM_TYPE"));
                ObjItem.setAttribute("STM_COMPANY_ID",rsItem.getInt("STM_COMPANY_ID"));
                ObjItem.setAttribute("STM_COMPANY_YEAR",rsItem.getInt("STM_COMPANY_YEAR"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("MIR_TYPE",rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
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
                
                ObjItem.setAttribute("COLUMN_31_ID",rsItem.getInt("COLUMN_31_ID"));
                ObjItem.setAttribute("COLUMN_31_FORMULA",rsItem.getString("COLUMN_31_FORMULA"));
                ObjItem.setAttribute("COLUMN_31_PER",rsItem.getDouble("COLUMN_31_PER"));
                ObjItem.setAttribute("COLUMN_31_AMT",rsItem.getDouble("COLUMN_31_AMT"));
                ObjItem.setAttribute("COLUMN_31_CAPTION",rsItem.getString("COLUMN_31_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_32_ID",rsItem.getInt("COLUMN_32_ID"));
                ObjItem.setAttribute("COLUMN_32_FORMULA",rsItem.getString("COLUMN_32_FORMULA"));
                ObjItem.setAttribute("COLUMN_32_PER",rsItem.getDouble("COLUMN_32_PER"));
                ObjItem.setAttribute("COLUMN_32_AMT",rsItem.getDouble("COLUMN_32_AMT"));
                ObjItem.setAttribute("COLUMN_32_CAPTION",rsItem.getString("COLUMN_32_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_33_ID",rsItem.getInt("COLUMN_33_ID"));
                ObjItem.setAttribute("COLUMN_33_FORMULA",rsItem.getString("COLUMN_33_FORMULA"));
                ObjItem.setAttribute("COLUMN_33_PER",rsItem.getDouble("COLUMN_33_PER"));
                ObjItem.setAttribute("COLUMN_33_AMT",rsItem.getDouble("COLUMN_33_AMT"));
                ObjItem.setAttribute("COLUMN_33_CAPTION",rsItem.getString("COLUMN_33_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_34_ID",rsItem.getInt("COLUMN_34_ID"));
                ObjItem.setAttribute("COLUMN_34_FORMULA",rsItem.getString("COLUMN_34_FORMULA"));
                ObjItem.setAttribute("COLUMN_34_PER",rsItem.getDouble("COLUMN_34_PER"));
                ObjItem.setAttribute("COLUMN_34_AMT",rsItem.getDouble("COLUMN_34_AMT"));
                ObjItem.setAttribute("COLUMN_34_CAPTION",rsItem.getString("COLUMN_34_CAPTION"));

                
                ObjItem.setAttribute("COLUMN_35_ID",rsItem.getInt("COLUMN_35_ID"));
                ObjItem.setAttribute("COLUMN_35_FORMULA",rsItem.getString("COLUMN_35_FORMULA"));
                ObjItem.setAttribute("COLUMN_35_PER",rsItem.getDouble("COLUMN_35_PER"));
                ObjItem.setAttribute("COLUMN_35_AMT",rsItem.getDouble("COLUMN_35_AMT"));
                ObjItem.setAttribute("COLUMN_35_CAPTION",rsItem.getString("COLUMN_35_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_36_ID",rsItem.getInt("COLUMN_36_ID"));
                ObjItem.setAttribute("COLUMN_36_FORMULA",rsItem.getString("COLUMN_36_FORMULA"));
                ObjItem.setAttribute("COLUMN_36_PER",rsItem.getDouble("COLUMN_36_PER"));
                ObjItem.setAttribute("COLUMN_36_AMT",rsItem.getDouble("COLUMN_36_AMT"));
                ObjItem.setAttribute("COLUMN_36_CAPTION",rsItem.getString("COLUMN_36_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_37_ID",rsItem.getInt("COLUMN_37_ID"));
                ObjItem.setAttribute("COLUMN_37_FORMULA",rsItem.getString("COLUMN_37_FORMULA"));
                ObjItem.setAttribute("COLUMN_37_PER",rsItem.getDouble("COLUMN_37_PER"));
                ObjItem.setAttribute("COLUMN_37_AMT",rsItem.getDouble("COLUMN_37_AMT"));
                ObjItem.setAttribute("COLUMN_37_CAPTION",rsItem.getString("COLUMN_37_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_38_ID",rsItem.getInt("COLUMN_38_ID"));
                ObjItem.setAttribute("COLUMN_38_FORMULA",rsItem.getString("COLUMN_38_FORMULA"));
                ObjItem.setAttribute("COLUMN_38_PER",rsItem.getDouble("COLUMN_38_PER"));
                ObjItem.setAttribute("COLUMN_38_AMT",rsItem.getDouble("COLUMN_38_AMT"));
                ObjItem.setAttribute("COLUMN_38_CAPTION",rsItem.getString("COLUMN_38_CAPTION"));
                
                ObjItem.setAttribute("BARCODE_TYPE",rsItem.getString("BARCODE_TYPE"));
                
                ObjItem.setAttribute("PO_QTY",rsItem.getDouble("PO_QTY"));
                ObjItem.setAttribute("BALANCE_PO_QTY",rsItem.getDouble("BALANCE_PO_QTY"));
                ObjItem.setAttribute("RND_DEDUCTION_REASON",rsItem.getString("RND_DEDUCTION_REASON"));
                
                //== Insert Lot Nos. ==
                stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                if(HistoryView) {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_MIR_LOT_H WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_SR_NO="+SrNo+" AND MIR_TYPE=2 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
                }
                else {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_MIR_LOT WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_SR_NO="+SrNo+" AND MIR_TYPE=2 ORDER BY SR_NO");
                }
                rsLot.first();
                
                LotCounter=0;
                while((!rsLot.isAfterLast())&&(rsLot.getRow()>0)) {
                    LotCounter=LotCounter+1;
                    
                    clsMIRRawItemDetail ObjLot=new clsMIRRawItemDetail();
                    ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("MIR_NO",rsLot.getString("MIR_NO"));
                    ObjLot.setAttribute("MIR_SR_NO",rsLot.getInt("MIR_SR_NO"));
                    ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("MIR_TYPE",rsLot.getInt("MIR_TYPE"));
                    ObjLot.setAttribute("ITEM_ID",rsLot.getString("ITEM_ID"));
                    ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                    ObjLot.setAttribute("AUTO_LOT_NO",rsLot.getString("AUTO_LOT_NO"));
                    ObjLot.setAttribute("LOT_RECEIVED_QTY",rsLot.getDouble("LOT_RECEIVED_QTY"));
                    ObjLot.setAttribute("LOT_REJECTED_QTY",rsLot.getDouble("LOT_REJECTED_QTY"));
                    ObjLot.setAttribute("LOT_ACCEPTED_QTY",rsLot.getDouble("LOT_ACCEPTED_QTY"));
                    
                    ObjItem.colMIRLot.put(Integer.toString(LotCounter),ObjLot);
                    rsLot.next();
                }
                
                colMIRItems.put(Integer.toString(ItemCounter),ObjItem);
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
        Statement stItem,stLot,stTmp;
        ResultSet rsItem,rsLot,rsTmp;
        String GRNNo="";
        int CompanyID=0,ItemCounter=0,LotCounter=0,SrNo=0;
        
        try {
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("MIR_NO",rsResultSet.getString("MIR_NO"));
            setAttribute("MIR_DATE",rsResultSet.getString("MIR_DATE"));
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
            setAttribute("INVOICE_AMOUNT",rsResultSet.getDouble("INVOICE_AMOUNT"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("GROSS_AMOUNT",rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CENVATED_ITEMS",rsResultSet.getBoolean("CENVATED_ITEMS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("MIR_TYPE",rsResultSet.getInt("MIR_TYPE"));
            setAttribute("OPEN_STATUS",rsResultSet.getString("OPEN_STATUS"));
            setAttribute("FOR_STORE",rsResultSet.getInt("FOR_STORE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
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
            
            
            
            colMIRItems.clear(); //Clear existing data
            
            String MIRNo=(String)getAttribute("MIR_NO").getObj();
            
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_TYPE="+pType+" ORDER BY SR_NO");
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                
                clsMIRRawItem ObjItem=new clsMIRRawItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("MIR_NO",rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_SR_NO",rsItem.getInt("PO_SR_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsItem.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("EXCESS_QTY",rsItem.getDouble("EXCESS_QTY"));
                ObjItem.setAttribute("RECEIVED_QTY",rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("REJECTED_QTY",rsItem.getDouble("REJECTED_QTY"));
                ObjItem.setAttribute("GROSS_QTY",rsItem.getDouble("GROSS_QTY"));
                ObjItem.setAttribute("TOLERANCE_LIMIT",rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("DEPT_ID",rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("REJECTED_REASON_ID",rsItem.getInt("REJECTED_REASON_ID"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE",rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("TOTAL_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("SHADE",rsItem.getString("SHADE"));
                ObjItem.setAttribute("W_MIE",rsItem.getString("W_MIE"));
                ObjItem.setAttribute("NO_CASE",rsItem.getString("NO_CASE"));
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN",rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS",rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("REMARKS",rsItem.getString("REMARKS"));
                ObjItem.setAttribute("MATERIAL_CODE",rsItem.getString("MATERIAL_CODE"));
                ObjItem.setAttribute("MATERIAL_DESC",rsItem.getString("MATERIAL_DESC"));
                ObjItem.setAttribute("QUALITY_NO",rsItem.getString("QUALITY_NO"));
                ObjItem.setAttribute("PO_QTY",rsItem.getDouble("PO_QTY"));
                ObjItem.setAttribute("BALANCE_PO_QTY",rsItem.getDouble("BALANCE_PO_QTY"));
                ObjItem.setAttribute("PAGE_NO",rsItem.getString("PAGE_NO"));
                ObjItem.setAttribute("EXCESS",rsItem.getDouble("EXCESS"));
                ObjItem.setAttribute("SHORTAGE",rsItem.getDouble("SHORTAGE"));
                ObjItem.setAttribute("L_F_NO",rsItem.getString("L_F_NO"));
                ObjItem.setAttribute("CHALAN_QTY",rsItem.getDouble("CHALAN_QTY"));
                ObjItem.setAttribute("INDENT_NO",rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO",rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("STM_NO",rsItem.getString("STM_NO"));
                ObjItem.setAttribute("STM_SR_NO",rsItem.getInt("STM_SR_NO"));
                ObjItem.setAttribute("STM_TYPE",rsItem.getInt("STM_TYPE"));
                ObjItem.setAttribute("STM_COMPANY_ID",rsItem.getInt("STM_COMPANY_ID"));
                ObjItem.setAttribute("STM_COMPANY_YEAR",rsItem.getInt("STM_COMPANY_YEAR"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("MIR_TYPE",rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                
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
                
                ObjItem.setAttribute("COLUMN_31_ID",rsItem.getInt("COLUMN_31_ID"));
                ObjItem.setAttribute("COLUMN_31_FORMULA",rsItem.getString("COLUMN_31_FORMULA"));
                ObjItem.setAttribute("COLUMN_31_PER",rsItem.getDouble("COLUMN_31_PER"));
                ObjItem.setAttribute("COLUMN_31_AMT",rsItem.getDouble("COLUMN_31_AMT"));
                ObjItem.setAttribute("COLUMN_31_CAPTION",rsItem.getString("COLUMN_31_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_32_ID",rsItem.getInt("COLUMN_32_ID"));
                ObjItem.setAttribute("COLUMN_32_FORMULA",rsItem.getString("COLUMN_32_FORMULA"));
                ObjItem.setAttribute("COLUMN_32_PER",rsItem.getDouble("COLUMN_32_PER"));
                ObjItem.setAttribute("COLUMN_32_AMT",rsItem.getDouble("COLUMN_32_AMT"));
                ObjItem.setAttribute("COLUMN_32_CAPTION",rsItem.getString("COLUMN_32_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_33_ID",rsItem.getInt("COLUMN_33_ID"));
                ObjItem.setAttribute("COLUMN_33_FORMULA",rsItem.getString("COLUMN_33_FORMULA"));
                ObjItem.setAttribute("COLUMN_33_PER",rsItem.getDouble("COLUMN_33_PER"));
                ObjItem.setAttribute("COLUMN_33_AMT",rsItem.getDouble("COLUMN_33_AMT"));
                ObjItem.setAttribute("COLUMN_33_CAPTION",rsItem.getString("COLUMN_33_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_34_ID",rsItem.getInt("COLUMN_34_ID"));
                ObjItem.setAttribute("COLUMN_34_FORMULA",rsItem.getString("COLUMN_34_FORMULA"));
                ObjItem.setAttribute("COLUMN_34_PER",rsItem.getDouble("COLUMN_34_PER"));
                ObjItem.setAttribute("COLUMN_34_AMT",rsItem.getDouble("COLUMN_34_AMT"));
                ObjItem.setAttribute("COLUMN_34_CAPTION",rsItem.getString("COLUMN_34_CAPTION"));

                
                ObjItem.setAttribute("COLUMN_35_ID",rsItem.getInt("COLUMN_35_ID"));
                ObjItem.setAttribute("COLUMN_35_FORMULA",rsItem.getString("COLUMN_35_FORMULA"));
                ObjItem.setAttribute("COLUMN_35_PER",rsItem.getDouble("COLUMN_35_PER"));
                ObjItem.setAttribute("COLUMN_35_AMT",rsItem.getDouble("COLUMN_35_AMT"));
                ObjItem.setAttribute("COLUMN_35_CAPTION",rsItem.getString("COLUMN_35_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_36_ID",rsItem.getInt("COLUMN_36_ID"));
                ObjItem.setAttribute("COLUMN_36_FORMULA",rsItem.getString("COLUMN_36_FORMULA"));
                ObjItem.setAttribute("COLUMN_36_PER",rsItem.getDouble("COLUMN_36_PER"));
                ObjItem.setAttribute("COLUMN_36_AMT",rsItem.getDouble("COLUMN_36_AMT"));
                ObjItem.setAttribute("COLUMN_36_CAPTION",rsItem.getString("COLUMN_36_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_37_ID",rsItem.getInt("COLUMN_37_ID"));
                ObjItem.setAttribute("COLUMN_37_FORMULA",rsItem.getString("COLUMN_37_FORMULA"));
                ObjItem.setAttribute("COLUMN_37_PER",rsItem.getDouble("COLUMN_37_PER"));
                ObjItem.setAttribute("COLUMN_37_AMT",rsItem.getDouble("COLUMN_37_AMT"));
                ObjItem.setAttribute("COLUMN_37_CAPTION",rsItem.getString("COLUMN_37_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_38_ID",rsItem.getInt("COLUMN_38_ID"));
                ObjItem.setAttribute("COLUMN_38_FORMULA",rsItem.getString("COLUMN_38_FORMULA"));
                ObjItem.setAttribute("COLUMN_38_PER",rsItem.getDouble("COLUMN_38_PER"));
                ObjItem.setAttribute("COLUMN_38_AMT",rsItem.getDouble("COLUMN_38_AMT"));
                ObjItem.setAttribute("COLUMN_38_CAPTION",rsItem.getString("COLUMN_38_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_39_ID",rsItem.getInt("COLUMN_39_ID"));
                ObjItem.setAttribute("COLUMN_39_FORMULA",rsItem.getString("COLUMN_39_FORMULA"));
                ObjItem.setAttribute("COLUMN_39_PER",rsItem.getDouble("COLUMN_39_PER"));
                ObjItem.setAttribute("COLUMN_39_AMT",rsItem.getDouble("COLUMN_39_AMT"));
                ObjItem.setAttribute("COLUMN_39_CAPTION",rsItem.getString("COLUMN_39_CAPTION"));
                
                ObjItem.setAttribute("RND_DEDUCTION_REASON",rsItem.getString("RND_DEDUCTION_REASON"));
                
                ObjItem.setAttribute("BARCODE_TYPE",rsItem.getString("BARCODE_TYPE"));
                
                //== Insert Lot Nos. ==
                stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsLot=stLot.executeQuery("SELECT * FROM D_INV_MIR_LOT WHERE COMPANY_ID="+CompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_SR_NO="+SrNo+" AND MIR_TYPE="+pType+" ORDER BY SR_NO");
                rsLot.first();
                
                LotCounter=0;
                while((!rsLot.isAfterLast())&&(rsLot.getRow()>0)) {
                    LotCounter=LotCounter+1;
                    
                    clsMIRRawItemDetail ObjLot=new clsMIRRawItemDetail();
                    ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("MIR_NO",rsLot.getString("MIR_NO"));
                    ObjLot.setAttribute("MIR_SR_NO",rsLot.getInt("MIR_SR_NO"));
                    ObjLot.setAttribute("MIR_TYPE",rsLot.getInt("MIR_TYPE"));
                    ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                    ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                    
                    ObjItem.colMIRLot.put(Integer.toString(LotCounter),ObjLot);
                    rsLot.next();
                }
                
                colMIRItems.put(Integer.toString(ItemCounter),ObjItem);
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MIR_NO='"+pDocNo+"' AND (APPROVED=1) AND MIR_TYPE=2";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=6 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MIR_NO='"+pDocNo+"' AND (APPROVED=1) AND MIR_TYPE=2";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=6 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    public static HashMap getMIRItemList(int pCompanyID,String pMIRNo,boolean pAllItems,int pType) {
        //Fourth argument pAllItems indicates whether you want all item listing or
        //Just pending Items where GRN_RECD_QTY<QTY
        //i.e. GRN entry is still pending
        
        Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pMIRNo+"' and MIR_TYPE="+pType+" AND APPROVED=1");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                if(pAllItems) //Retrieve All Items
                {
                    strSQL="SELECT D_INV_MIR_DETAIL.* FROM D_INV_MIR_DETAIL,D_INV_MIR_HEADER  WHERE D_INV_MIR_DETAIL.COMPANY_ID=D_INV_MIR_HEADER.COMPANY_ID AND D_INV_MIR_DETAIL.MIR_NO=D_INV_MIR_HEADER.MIR_NO  AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_DETAIL.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.MIR_NO='"+pMIRNo+"' AND D_INV_MIR_DETAIL.MIR_TYPE="+pType+" ORDER BY ITEM_ID";
                }
                else //Retrieve Only pending Items
                {
                    //strSQL="SELECT D_INV_MIR_DETAIL.* FROM D_INV_MIR_DETAIL,D_INV_MIR_HEADER  WHERE D_INV_MIR_DETAIL.COMPANY_ID=D_INV_MIR_HEADER.COMPANY_ID AND D_INV_MIR_DETAIL.MIR_NO=D_INV_MIR_HEADER.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_DETAIL.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.MIR_NO='"+pMIRNo+"' AND D_INV_MIR_DETAIL.MIR_TYPE="+pType+" AND GRN_RECD_QTY<(QTY+REJECTED_QTY) ORDER BY ITEM_ID";
                    
                    strSQL="SELECT B.* ";
                    strSQL+="FROM ";
                    strSQL+="D_INV_MIR_HEADER A, ";
                    strSQL+="D_INV_MIR_DETAIL B ";
                    strSQL+="LEFT JOIN D_INV_GRN_DETAIL G ON (G.MIR_NO=B.MIR_NO AND G.MIR_SR_NO=B.SR_NO AND G.MIR_TYPE=B.MIR_TYPE AND G.GRN_NO IN (SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO=G.GRN_NO AND GRN_TYPE=G.GRN_TYPE AND APPROVED=1 AND CANCELLED=0)), ";
                    strSQL+="D_COM_DEPT_MASTER D ";
                    strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
                    strSQL+="A.MIR_NO=B.MIR_NO AND ";
                    strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
                    strSQL+="A.APPROVED=1 AND A.CANCELLED=0 AND ";
                    strSQL+="B.COMPANY_ID=D.COMPANY_ID AND B.DEPT_ID=D.DEPT_ID AND A.MIR_TYPE="+pType+" ";
                    strSQL+=" AND A.MIR_NO='"+pMIRNo+"' ";
                    strSQL+="GROUP BY B.MIR_NO,B.MIR_TYPE,B.SR_NO ";
                    strSQL+="HAVING IF(SUM(G.QTY+G.REJECTED_QTY) IS NULL,0,SUM(G.QTY+G.REJECTED_QTY))<(B.QTY+B.REJECTED_QTY) ";
                    
                }
                
                
                stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=stTmp.executeQuery(strSQL);
                rsTmp.first();
                
                Counter1=0;
                
                while(!rsTmp.isAfterLast()) {
                    Counter1++;
                    clsMIRRawItem ObjItem=new clsMIRRawItem();
                    
                    int MIRSrNo=rsTmp.getInt("SR_NO");
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("MIR_NO",rsTmp.getString("MIR_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("WAREHOUSE_ID",rsTmp.getString("WAREHOUSE_ID"));
                    ObjItem.setAttribute("LOCATION_ID",rsTmp.getString("LOCATION_ID"));
                    ObjItem.setAttribute("PO_NO",rsTmp.getString("PO_NO"));
                    ObjItem.setAttribute("PO_SR_NO",rsTmp.getInt("PO_SR_NO"));
                    ObjItem.setAttribute("PO_TYPE",rsTmp.getInt("PO_TYPE"));
                    ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjItem.setAttribute("HSN_SAC_CODE",rsTmp.getString("HSN_SAC_CODE"));
                    ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                    ObjItem.setAttribute("BOE_NO",rsTmp.getString("BOE_NO"));
                    ObjItem.setAttribute("BOE_SR_NO",rsTmp.getString("BOE_SR_NO"));
                    ObjItem.setAttribute("BOE_DATE",rsTmp.getString("BOE_DATE"));
                    ObjItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                    ObjItem.setAttribute("EXCESS_QTY",rsTmp.getDouble("EXCESS_QTY"));
                    ObjItem.setAttribute("RECEIVED_QTY",rsTmp.getDouble("RECEIVED_QTY"));
                    ObjItem.setAttribute("REJECTED_QTY",rsTmp.getDouble("REJECTED_QTY"));
                    ObjItem.setAttribute("GROSS_QTY",rsTmp.getDouble("GROSS_QTY"));
                    
                    double ReceivedQty=data.getDoubleValueFromDB("SELECT SUM(QTY) AS TOTAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.MIR_NO='"+pMIRNo+"' AND B.MIR_TYPE="+pType+" AND B.MIR_SR_NO="+rsTmp.getInt("SR_NO"));
                    
                    ObjItem.setAttribute("GRN_RECD_QTY",ReceivedQty);
                    ObjItem.setAttribute("BAL_QTY",rsTmp.getDouble("QTY")-ReceivedQty);
                    ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                    ObjItem.setAttribute("REJECTED_REASON_ID",rsTmp.getInt("REJECTED_REASON_ID"));
                    ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjItem.setAttribute("RATE",rsTmp.getDouble("RATE"));
                    ObjItem.setAttribute("TOTAL_AMOUNT",rsTmp.getDouble("TOTAL_AMOUNT"));
                    ObjItem.setAttribute("NET_AMOUNT",rsTmp.getDouble("NET_AMOUNT"));
                    ObjItem.setAttribute("SHADE",rsTmp.getString("SHADE"));
                    ObjItem.setAttribute("W_MIE",rsTmp.getString("W_MIE"));
                    ObjItem.setAttribute("NO_CASE",rsTmp.getString("NO_CASE"));
                    ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN",rsTmp.getBoolean("EXCISE_GATEPASS_GIVEN"));
                    ObjItem.setAttribute("IMPORT_CONCESS",rsTmp.getBoolean("IMPORT_CONCESS"));
                    ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                    ObjItem.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                    ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                    ObjItem.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                    ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                    ObjItem.setAttribute("COLUMN_1_ID",rsTmp.getInt("COLUMN_1_ID"));
                    ObjItem.setAttribute("COLUMN_1_FORMULA",rsTmp.getString("COLUMN_1_FORMULA"));
                    ObjItem.setAttribute("COLUMN_1_PER",rsTmp.getDouble("COLUMN_1_PER"));
                    ObjItem.setAttribute("COLUMN_1_AMT",rsTmp.getDouble("COLUMN_1_AMT"));
                    ObjItem.setAttribute("COLUMN_1_CAPTION",rsTmp.getString("COLUMN_1_CAPTION"));
                    ObjItem.setAttribute("COLUMN_2_ID",rsTmp.getInt("COLUMN_2_ID"));
                    ObjItem.setAttribute("COLUMN_2_FORMULA",rsTmp.getString("COLUMN_2_FORMULA"));
                    ObjItem.setAttribute("COLUMN_2_PER",rsTmp.getDouble("COLUMN_2_PER"));
                    ObjItem.setAttribute("COLUMN_2_AMT",rsTmp.getDouble("COLUMN_2_AMT"));
                    ObjItem.setAttribute("COLUMN_2_CAPTION",rsTmp.getString("COLUMN_2_CAPTION"));
                    ObjItem.setAttribute("COLUMN_3_ID",rsTmp.getInt("COLUMN_3_ID"));
                    ObjItem.setAttribute("COLUMN_3_FORMULA",rsTmp.getString("COLUMN_3_FORMULA"));
                    ObjItem.setAttribute("COLUMN_3_PER",rsTmp.getDouble("COLUMN_3_PER"));
                    ObjItem.setAttribute("COLUMN_3_AMT",rsTmp.getDouble("COLUMN_3_AMT"));
                    ObjItem.setAttribute("COLUMN_3_CAPTION",rsTmp.getString("COLUMN_3_CAPTION"));
                    ObjItem.setAttribute("COLUMN_4_ID",rsTmp.getInt("COLUMN_4_ID"));
                    ObjItem.setAttribute("COLUMN_4_FORMULA",rsTmp.getString("COLUMN_4_FORMULA"));
                    ObjItem.setAttribute("COLUMN_4_PER",rsTmp.getDouble("COLUMN_4_PER"));
                    ObjItem.setAttribute("COLUMN_4_AMT",rsTmp.getDouble("COLUMN_4_AMT"));
                    ObjItem.setAttribute("COLUMN_4_CAPTION",rsTmp.getString("COLUMN_4_CAPTION"));
                    ObjItem.setAttribute("COLUMN_5_ID",rsTmp.getInt("COLUMN_5_ID"));
                    ObjItem.setAttribute("COLUMN_5_FORMULA",rsTmp.getString("COLUMN_5_FORMULA"));
                    ObjItem.setAttribute("COLUMN_5_PER",rsTmp.getDouble("COLUMN_5_PER"));
                    ObjItem.setAttribute("COLUMN_5_AMT",rsTmp.getDouble("COLUMN_5_AMT"));
                    ObjItem.setAttribute("COLUMN_5_CAPTION",rsTmp.getString("COLUMN_5_CAPTION"));
                    ObjItem.setAttribute("COLUMN_6_ID",rsTmp.getInt("COLUMN_6_ID"));
                    ObjItem.setAttribute("COLUMN_6_FORMULA",rsTmp.getString("COLUMN_6_FORMULA"));
                    ObjItem.setAttribute("COLUMN_6_PER",rsTmp.getDouble("COLUMN_6_PER"));
                    ObjItem.setAttribute("COLUMN_6_AMT",rsTmp.getDouble("COLUMN_6_AMT"));
                    ObjItem.setAttribute("COLUMN_6_CAPTION",rsTmp.getString("COLUMN_6_CAPTION"));
                    ObjItem.setAttribute("COLUMN_7_ID",rsTmp.getInt("COLUMN_7_ID"));
                    ObjItem.setAttribute("COLUMN_7_FORMULA",rsTmp.getString("COLUMN_7_FORMULA"));
                    ObjItem.setAttribute("COLUMN_7_PER",rsTmp.getDouble("COLUMN_7_PER"));
                    ObjItem.setAttribute("COLUMN_7_AMT",rsTmp.getDouble("COLUMN_7_AMT"));
                    ObjItem.setAttribute("COLUMN_7_CAPTION",rsTmp.getString("COLUMN_7_CAPTION"));
                    ObjItem.setAttribute("COLUMN_8_ID",rsTmp.getInt("COLUMN_8_ID"));
                    ObjItem.setAttribute("COLUMN_8_FORMULA",rsTmp.getString("COLUMN_8_FORMULA"));
                    ObjItem.setAttribute("COLUMN_8_PER",rsTmp.getDouble("COLUMN_8_PER"));
                    ObjItem.setAttribute("COLUMN_8_AMT",rsTmp.getDouble("COLUMN_8_AMT"));
                    ObjItem.setAttribute("COLUMN_8_CAPTION",rsTmp.getString("COLUMN_8_CAPTION"));
                    ObjItem.setAttribute("COLUMN_9_ID",rsTmp.getInt("COLUMN_9_ID"));
                    ObjItem.setAttribute("COLUMN_9_FORMULA",rsTmp.getString("COLUMN_9_FORMULA"));
                    ObjItem.setAttribute("COLUMN_9_PER",rsTmp.getDouble("COLUMN_9_PER"));
                    ObjItem.setAttribute("COLUMN_9_AMT",rsTmp.getDouble("COLUMN_9_AMT"));
                    ObjItem.setAttribute("COLUMN_9_CAPTION",rsTmp.getString("COLUMN_9_CAPTION"));
                    
                    ObjItem.setAttribute("COLUMN_10_ID",rsTmp.getInt("COLUMN_10_ID"));
                    ObjItem.setAttribute("COLUMN_10_FORMULA",rsTmp.getString("COLUMN_10_FORMULA"));
                    ObjItem.setAttribute("COLUMN_10_PER",rsTmp.getDouble("COLUMN_10_PER"));
                    ObjItem.setAttribute("COLUMN_10_AMT",rsTmp.getDouble("COLUMN_10_AMT"));
                    ObjItem.setAttribute("COLUMN_10_CAPTION",rsTmp.getString("COLUMN_10_CAPTION"));
                    
                    for(int it=11;it<=39;it++) {
                        
                        ObjItem.setAttribute("COLUMN_"+it+"_ID",rsTmp.getInt("COLUMN_"+it+"_ID"));
                        ObjItem.setAttribute("COLUMN_"+it+"_FORMULA",rsTmp.getString("COLUMN_"+it+"_FORMULA"));
                        ObjItem.setAttribute("COLUMN_"+it+"_PER",rsTmp.getDouble("COLUMN_"+it+"_PER"));
                        ObjItem.setAttribute("COLUMN_"+it+"_AMT",rsTmp.getDouble("COLUMN_"+it+"_AMT"));
                        ObjItem.setAttribute("COLUMN_"+it+"_CAPTION",rsTmp.getString("COLUMN_"+it+"_CAPTION"));
                        
                    }
                    
                    ObjItem.setAttribute("MATERIAL_CODE",rsTmp.getString("MATERIAL_CODE"));
                    ObjItem.setAttribute("MATERIAL_DESC",rsTmp.getString("MATERIAL_DESC"));
                    ObjItem.setAttribute("QUALITY_NO",rsTmp.getString("QUALITY_NO"));
                    ObjItem.setAttribute("PAGE_NO",rsTmp.getString("PAGE_NO"));
                    ObjItem.setAttribute("EXCESS",rsTmp.getDouble("EXCESS"));
                    ObjItem.setAttribute("SHORTAGE",rsTmp.getDouble("SHORTAGE"));
                    ObjItem.setAttribute("L_F_NO",rsTmp.getString("L_F_NO"));
                    ObjItem.setAttribute("CHALAN_QTY",rsTmp.getDouble("CHALAN_QTY"));
                    ObjItem.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                    ObjItem.setAttribute("INDENT_SR_NO",rsTmp.getInt("INDENT_SR_NO"));
                    ObjItem.setAttribute("STM_NO",rsTmp.getString("STM_NO"));
                    ObjItem.setAttribute("STM_SR_NO",rsTmp.getInt("STM_SR_NO"));
                    
                    ObjItem.setAttribute("PO_QTY",rsTmp.getDouble("PO_QTY"));
                    ObjItem.setAttribute("BALANCE_PO_QTY",rsTmp.getDouble("BALANCE_PO_QTY"));
                    
                    //Now Fetch Lots.
                    stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_MIR_LOT WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pMIRNo+"' AND MIR_SR_NO="+MIRSrNo);
                    rsLot.first();
                    
                    //Clear existing data
                    ObjItem.colMIRLot.clear();
                    
                    if(rsLot.getRow()>0) {
                        Counter2=0;
                        while(!rsLot.isAfterLast()) {
                            Counter2++;
                            clsMIRRawItemDetail ObjLot=new clsMIRRawItemDetail();
                            
                            ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                            ObjLot.setAttribute("MIR_NO",rsLot.getString("MIR_NO"));
                            ObjLot.setAttribute("MIR_SR_NO",rsLot.getInt("MIR_SR_NO"));
                            ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                            ObjLot.setAttribute("ITEM_ID",rsLot.getString("ITEM_ID"));
                            ObjLot.setAttribute("ITEM_LOT_NO",rsLot.getString("ITEM_LOT_NO"));
                            ObjLot.setAttribute("AUTO_LOT_NO",rsLot.getString("AUTO_LOT_NO"));
                            ObjLot.setAttribute("LOT_RECEIVED_QTY",rsLot.getDouble("LOT_RECEIVED_QTY"));
                            ObjLot.setAttribute("LOT_REJECTED_QTY",rsLot.getDouble("LOT_REJECTED_QTY"));
                            ObjLot.setAttribute("LOT_ACCEPTED_QTY",rsLot.getDouble("LOT_ACCEPTED_QTY"));
                            ObjItem.colMIRLot.put(Integer.toString(Counter2),ObjLot);
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
        int moduleID=0;
        if(pType == 1){
            moduleID=5;
        }else if(pType==2){
            moduleID=6;
        }else{
            moduleID=35;
        }
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+moduleID+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+moduleID+" ORDER BY D_INV_MIR_HEADER.MIR_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+moduleID+" ORDER BY D_INV_MIR_HEADER.MIR_NO";
            }
            
            //strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+(pType+4);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("MIR_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsMIRRaw ObjDoc=new clsMIRRaw();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("MIR_NO",rsTmp.getString("MIR_NO"));
                    ObjDoc.setAttribute("MIR_DATE",rsTmp.getString("MIR_DATE"));
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
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pType,int pOrder,int FinYearFrom) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+(pType+4)+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+(pType+4)+" ORDER BY D_INV_MIR_HEADER.MIR_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+(pType+4)+" ORDER BY D_INV_MIR_HEADER.MIR_NO";
            }
            
            //strSQL="SELECT D_INV_MIR_HEADER.MIR_NO,D_INV_MIR_HEADER.MIR_DATE FROM D_INV_MIR_HEADER,D_COM_DOC_DATA WHERE D_INV_MIR_HEADER.MIR_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_MIR_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_INV_MIR_HEADER.MIR_TYPE="+pType+" AND MODULE_ID="+(pType+4);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("MIR_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsMIRRaw ObjDoc=new clsMIRRaw();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("MIR_NO",rsTmp.getString("MIR_NO"));
                    ObjDoc.setAttribute("MIR_DATE",rsTmp.getString("MIR_DATE"));
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
    
    
    public static double getPendingGRNQty(int pCompanyID,int pDeptID,String pItemID) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        double PendingQty=0;
        
        try {
            
            //Below is the old working Query
            //strSQL="SELECT SUM(D_INV_MIR_DETAIL.QTY)  AS TOTAL_QTY FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.GRN_RECD_QTY<=0 AND D_INV_MIR_DETAIL.DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_MIR_DETAIL.DEPT_ID="+pDeptID+" AND D_INV_MIR_DETAIL.ITEM_ID='"+pItemID+"' AND D_INV_MIR_HEADER.APPROVED=1 AND D_INV_MIR_HEADER.CANCELLED=0";
            
            strSQL="SELECT (B.QTY-IF(SUM(G.QTY+G.REJECTED_QTY) IS NULL,0,SUM(G.QTY+G.REJECTED_QTY))) AS PENDING_QTY,B.QTY,B.REJECTED_QTY ";
            strSQL+="FROM  ";
            strSQL+="D_INV_MIR_HEADER A, ";
            strSQL+="D_INV_MIR_DETAIL B ";
            strSQL+="LEFT JOIN D_INV_GRN_DETAIL G ON (G.MIR_NO=B.MIR_NO AND G.MIR_TYPE=B.MIR_TYPE AND G.MIR_SR_NO=B.SR_NO AND G.GRN_NO IN (SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO=G.GRN_NO AND GRN_TYPE=G.GRN_TYPE AND APPROVED=1 AND CANCELLED=0)), ";
            strSQL+="D_COM_DEPT_MASTER D WHERE ";
            strSQL+="A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.MIR_NO=B.MIR_NO AND ";
            strSQL+="A.MIR_TYPE=B.MIR_TYPE AND ";
            strSQL+="A.CANCELLED=0 AND ";
            strSQL+="B.DEPT_ID=D.DEPT_ID AND ";
            strSQL+="A.COMPANY_ID=D.COMPANY_ID  AND ";
            strSQL+="B.ITEM_ID='"+pItemID+"' AND ";
            strSQL+="B.DEPT_ID="+pDeptID+" AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" ";
            strSQL+="AND A.MIR_DATE >='2007-01-01' AND A.APPROVED=1 ";
            strSQL+="GROUP BY B.MIR_NO,B.MIR_TYPE,B.SR_NO ";
            strSQL+="HAVING IF(SUM(G.QTY+G.REJECTED_QTY) IS NULL,0,SUM(G.QTY+G.REJECTED_QTY))<(B.QTY+B.REJECTED_QTY) ";
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    PendingQty+=rsTmp.getDouble("PENDING_QTY");
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return PendingQty;
    }
    
    
    
    public static double getPendingMIRQty(int pCompanyID,int pDeptID,String pItemID) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        double PendingQty=0;
        
        try {
            
            //Original Query
            //strSQL="SELECT SUM(D_INV_MIR_DETAIL.QTY) AS TOTAL_QTY FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_MIR_DETAIL.DEPT_ID="+pDeptID+" AND D_INV_MIR_DETAIL.ITEM_ID='"+pItemID+"' AND D_INV_MIR_HEADER.APPROVED=0 AND D_INV_MIR_HEADER.CANCELLED=0";
            
            strSQL="SELECT SUM(QTY) AS TOTAL_QTY ";
            strSQL+="FROM ";
            strSQL+="D_INV_MIR_HEADER A, ";
            strSQL+="D_INV_MIR_DETAIL B, ";
            strSQL+="D_COM_DEPT_MASTER D ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.MIR_NO=B.MIR_NO AND ";
            strSQL+="A.MIR_TYPE=B.MIR_TYPE AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
            strSQL+="A.APPROVED=0 AND ";
            strSQL+="A.CANCELLED=0 AND ";
            strSQL+="B.DEPT_ID=D.DEPT_ID AND ";
            strSQL+="A.COMPANY_ID=D.COMPANY_ID  AND ";
            strSQL+="B.DEPT_ID="+pDeptID+" AND ";
            strSQL+="B.ITEM_ID='"+pItemID+"' ";
            strSQL+="GROUP BY ITEM_ID ";
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                PendingQty=rsTmp.getDouble("TOTAL_QTY");
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return PendingQty;
    }
    
    
    public static double getTotalPendingGRNQty(int pCompanyID,String pItemID) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        double PendingQty=0;
        
        try {
            
            //Below is the old working Query
            //strSQL="SELECT SUM(D_INV_MIR_DETAIL.QTY)  AS TOTAL_QTY FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.GRN_RECD_QTY<=0 AND D_INV_MIR_DETAIL.DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_MIR_DETAIL.DEPT_ID="+pDeptID+" AND D_INV_MIR_DETAIL.ITEM_ID='"+pItemID+"' AND D_INV_MIR_HEADER.APPROVED=1 AND D_INV_MIR_HEADER.CANCELLED=0";
            
            strSQL="SELECT (B.QTY-IF(SUM(G.QTY) IS NULL,0,SUM(G.QTY))) AS PENDING_QTY,B.QTY ";
            strSQL+="FROM  ";
            strSQL+="D_INV_MIR_HEADER A, ";
            strSQL+="D_INV_MIR_DETAIL B ";
            strSQL+="LEFT JOIN D_INV_GRN_DETAIL G ON (G.MIR_NO=B.MIR_NO AND G.MIR_TYPE=B.MIR_TYPE AND G.MIR_SR_NO=B.SR_NO), ";
            strSQL+="D_COM_DEPT_MASTER D WHERE ";
            strSQL+="A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.MIR_NO=B.MIR_NO AND ";
            strSQL+="A.MIR_TYPE=B.MIR_TYPE AND ";
            strSQL+="A.CANCELLED=0 AND ";
            strSQL+="B.DEPT_ID=D.DEPT_ID AND ";
            strSQL+="A.COMPANY_ID=D.COMPANY_ID  AND ";
            strSQL+="B.ITEM_ID='"+pItemID+"' AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" ";
            strSQL+="AND A.MIR_DATE >='2007-01-01' AND A.APPROVED=1";
            strSQL+="GROUP BY B.MIR_NO,B.MIR_TYPE,B.SR_NO ";
            strSQL+="HAVING IF(SUM(G.QTY) IS NULL,0,SUM(G.QTY))<B.QTY ";
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    PendingQty+=rsTmp.getDouble("PENDING_QTY");
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return PendingQty;
        
        
    }
    
    
    public static double getTotalPendingMIRQty(int pCompanyID,String pItemID) {
        
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        double PendingQty=0;
        
        try {
            
            //Original Query
            //strSQL="SELECT SUM(D_INV_MIR_DETAIL.QTY) AS TOTAL_QTY FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_MIR_DETAIL.DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_MIR_HEADER.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_MIR_DETAIL.DEPT_ID="+pDeptID+" AND D_INV_MIR_DETAIL.ITEM_ID='"+pItemID+"' AND D_INV_MIR_HEADER.APPROVED=0 AND D_INV_MIR_HEADER.CANCELLED=0";
            
            strSQL="SELECT SUM(QTY) AS TOTAL_QTY ";
            strSQL+="FROM ";
            strSQL+="D_INV_MIR_HEADER A, ";
            strSQL+="D_INV_MIR_DETAIL B, ";
            strSQL+="D_COM_DEPT_MASTER D ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.MIR_NO=B.MIR_NO AND ";
            strSQL+="A.MIR_TYPE=B.MIR_TYPE AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
            strSQL+="A.APPROVED=0 AND ";
            strSQL+="A.CANCELLED=0 AND ";
            strSQL+="B.DEPT_ID=D.DEPT_ID AND ";
            strSQL+="A.COMPANY_ID=D.COMPANY_ID  AND ";
            strSQL+="B.ITEM_ID='"+pItemID+"' ";
            strSQL+="GROUP BY ITEM_ID ";
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                PendingQty=rsTmp.getDouble("TOTAL_QTY");
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return PendingQty;
        
        
    }
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_MIR_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pDocNo+"' AND MIR_TYPE=2");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_MIR_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+pDocNo+"' AND MIR_TYPE=2");
            
            while(rsTmp.next()) {
                clsMIRRaw ObjMIR=new clsMIRRaw();
                
                ObjMIR.setAttribute("MIR_NO",rsTmp.getString("MIR_NO"));
                ObjMIR.setAttribute("MIR_DATE",rsTmp.getString("MIR_DATE"));
                ObjMIR.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjMIR.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjMIR.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjMIR.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjMIR.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjMIR);
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
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pDocNo+"' AND CANCELLED=0 AND MIR_TYPE=2");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Now check every item for received qty.
                rsTmp=data.getResult("SELECT MIR_NO FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pDocNo+"' AND GRN_RECD_QTY>0 AND MIR_TYPE=2");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Can not cancel
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
    
    
    public static boolean CancelMIR(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            Connection tmpConn;
            Statement stSTM;
            
            if(CanCancel(pCompanyID,pDocNo)) {
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_MIR_HEADER WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pDocNo+"' AND MIR_TYPE=2");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                    rsTmp=data.getResult("SELECT * FROM D_INV_MIR_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pDocNo+"' AND MIR_TYPE=2");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        while(!rsTmp.isAfterLast()) {
                            String PONo=rsTmp.getString("PO_NO");
                            int POType=rsTmp.getInt("PO_TYPE");
                            int POSrNo=rsTmp.getInt("PO_SR_NO");
                            
                            String STMNo=rsTmp.getString("STM_NO");
                            int STMSrNo=rsTmp.getInt("STM_SR_NO");
                            
                            double Qty=rsTmp.getDouble("QTY");
                            String ItemID=rsTmp.getString("ITEM_ID");
                            
                            
                            //================= Update STM ==============//
                            if((!STMNo.trim().equals(""))&&(STMSrNo>0)) {
                                //---Updating STM Qty -----------//
                                int STMCompany=rsTmp.getInt("STM_COMPANY_ID");
                                int STMCompanyYear=rsTmp.getInt("STM_COMPANY_YEAR");
                                String dbURL=clsFinYear.getDBURL(STMCompany,STMCompanyYear);
                                
                                tmpConn=data.getConn(dbURL);
                                stSTM=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                                //-------------
                                
                                stSTM.executeUpdate("UPDATE D_INV_STM_DETAIL SET RECEIVED_QTY=RECEIVED_QTY-"+Qty+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND STM_NO='"+STMNo+"' AND SR_NO="+STMSrNo+" AND STM_TYPE=2");
                            }
                            //===========================================//
                            
                            
                            //============== Update P.O. ============ //
                            if((!PONo.trim().equals(""))&&(POSrNo>0)) {
                                if(POType!=7) //Do not update P.O. in case of Contract Purchase. 7 - Contract Purchase
                                {
                                    data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY-"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);
                                }
                            }
                            //=============== P.O. Updation Completed ===============//
                            
                            rsTmp.next();
                        }
                        
                    }
                    
                }
                else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID=6");
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_MIR_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MIR_NO='"+pDocNo+"' AND MIR_TYPE=2");
                
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
