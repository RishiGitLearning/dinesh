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
import javax.swing.*;
import java.awt.*;
  
/** 
 *
 * @author  nrpithva
 * @version
 */

public class clsRJNGen {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colRJNItems=new HashMap();
    
    //History Related properties
    private boolean HistoryView=false;

    //Flag Indicating whether user has entered the document no.
    public boolean UserDocNo=false;
    
    
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
    public clsRJNGen() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("RJN_NO",new Variant(""));
        props.put("RJN_DATE",new Variant(""));
        props.put("RJN_TYPE",new Variant(1)); //1 - General, 2- Raw Material
        props.put("FOR_STORE",new Variant(0));
        props.put("SUPP_ID",new Variant(""));
        props.put("PARTY_NAME",new Variant(""));
        props.put("MODE_TRANSPORT",new Variant(0));
        props.put("TRANSPORTER",new Variant(0));
        props.put("GATEPASS_TYPE",new Variant(""));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("GATEPASS_DATE",new Variant(""));
        props.put("CURRENCY_ID",new Variant(0));
        props.put("CURRENCY_RATE",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("REMARKS",new Variant(0));
        props.put("CANCELLED",new Variant(false));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND RJN_TYPE=1 AND RJN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND RJN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY RJN_NO"); //1 - General Material
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
        //Conn.close();  
        Stmt.close();
        rsResultSet.close();
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
        Statement stItem,stLot,stStock,stTmp,stItemMaster,stPO,stHistory,stHDetail;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsPO,rsHistory,rsHDetail;
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="",LocationID="",GRNNo="",strSQL="";
        String PONo="";
        int CompanyID=0,GRNSrNo=0,POSrNo=0,POType=0;
        double RejectedQty=0,Qty=0;
        
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("RJN_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0))
            {
               //Withing the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }
            //=====================================================//

            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_RJN_HEADER_H WHERE RJN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_RJN_DETAIL_H WHERE RJN_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_RJN_HEADER WHERE RJN_NO='1'");
            //rsHeader.first();
                        
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            //=========== Check the Quantities entered against GRN. ============= //
            for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                GRNNo=(String)ObjItem.getAttribute("GRN_NO").getObj();
                GRNSrNo=(int)ObjItem.getAttribute("GRN_SR_NO").getVal();
                double GRNQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
                
                if((!GRNNo.trim().equals(""))&&(GRNSrNo>0)) //GRN No. entered
                {
                    //Get the GRN Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT REJECTED_QTY FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND SR_NO="+GRNSrNo+" AND GRN_TYPE=1";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        GRNQty=rsTmp.getDouble("REJECTED_QTY");
                    }
                    
                    //Get Total Qty Entered in RJN Against this GRN No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+GRNSrNo+" AND RJN_TYPE=1 AND RJN_NO NOT IN(SELECT RJN_NO FROM D_INV_RJN_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > GRNQty) //If total Qty exceeds GRN Rejected Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds GRN No. "+GRNNo+" Sr. No. "+GRNSrNo+" qty "+GRNQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            //============== GRN Checking Completed ====================//
        
            
            
            //=========== Check the On Hand Stock Level.============= //
            /*for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                
                double OnHandQty=clsItem.getOnHandQty(CompanyID, ItemID);
                double MinQty=clsItem.getMinQty(CompanyID,ItemID);
                
                if(MinQty>0)  //Minimum level maintained
                {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((OnHandQty-CurrentQty)<MinQty) {
                        LastError="Cannot deduct stock for item "+ItemID+". Stock level reached at minimum stock level";
                        return false;
                    }
                }
                else {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((OnHandQty-CurrentQty)<0) {
                        LastError="Cannot deduct stock for item "+ItemID+". Stock level reached at zero level";
                        return false;
                    }
                    
                }
            }*/
            //=================================================///
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F"))
            {
            //-------- First Update the stock -------------//
            for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                
                GRNNo=(String)ObjItem.getAttribute("GRN_NO").getObj();
                GRNSrNo=(int)ObjItem.getAttribute("GRN_SR_NO").getVal();
                
                PONo=(String)ObjItem.getAttribute("PO_NO").getObj();
                POSrNo=(int)ObjItem.getAttribute("PO_SR_NO").getVal();
                POType=(int)ObjItem.getAttribute("PO_TYPE").getVal();
                
                Qty=ObjItem.getAttribute("QTY").getVal();
                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                
                //PO
                //============== Update P.O. ============ //
                if(POType!=7) //Do not update P.O. in case of Contract Purchase. 7 - Contract Purchase
                {
                    data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY-"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);                                           
                }
                //=============== P.O. Updation Completed ===============//

                
                
                //Check that Stock is maintained for this item
                /*if(clsItem.getMaintainStock(CompanyID,ItemID)) {
                        BOENo=(String)ObjItem.getAttribute("BOE_NO").getObj();
                        ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                        BOESrNo=(String)ObjItem.getAttribute("BOE_SR_NO").getObj();
                        WareHouseID=(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                        LocationID=(String)ObjItem.getAttribute("LOCATION_ID").getObj();
                        
                        LotNo="X";
                        
                        //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND TRIM(ITEM_ID)='"+ItemID.trim()+"' AND TRIM(LOT_NO)='"+LotNo.trim()+"' AND TRIM(BOE_NO)='"+BOENo.trim()+"' AND TRIM(WAREHOUSE_ID)='"+WareHouseID.trim()+"' AND TRIM(LOCATION_ID)='"+LocationID.trim()+"'");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) //Entry must exist in item stock table.
                        {
                            stStock=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            rsStock=stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND TRIM(ITEM_ID)='"+ItemID.trim()+"' AND TRIM(LOT_NO)='"+LotNo.trim()+"' AND TRIM(BOE_NO)='"+BOENo.trim()+"' AND TRIM(WAREHOUSE_ID)='"+WareHouseID.trim()+"' AND TRIM(LOCATION_ID)='"+LocationID.trim()+"'");
                            rsStock.first(); //There will be a single record only
                            
                            double lnLotQty=ObjItem.getAttribute("QTY").getVal(); //Record current Qty
                            
                            rsStock.updateDouble("REJECTED_QTY",rsStock.getDouble("REJECTED_QTY")+lnLotQty);
                            rsStock.updateDouble("ON_HAND_QTY",rsStock.getDouble("ON_HAND_QTY")-lnLotQty);
                            rsStock.updateDouble("AVAILABLE_QTY",rsStock.getDouble("AVAILABLE_QTY")-lnLotQty);
                            rsStock.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                            rsStock.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                            rsStock.updateRow();
                        }
                        
                        
                        //======= Update the Item Master =========
                        stItemMaster=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        rsItemMaster=stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+CompanyID+" AND TRIM(ITEM_ID)='"+ItemID.trim()+"'");
                        rsItemMaster.first();
                        
                        if(rsItemMaster.getRow()>0) {
                            double lnQty=ObjItem.getAttribute("QTY").getVal();
                            rsItemMaster.updateDouble("REJECTED_QTY",rsItemMaster.getDouble("REJECTED_QTY")+lnQty);
                            rsItemMaster.updateDouble("ON_HAND_QTY",rsItemMaster.getDouble("ON_HAND_QTY")-lnQty);
                            rsItemMaster.updateDouble("AVAILABLE_QTY",rsItemMaster.getDouble("AVAILABLE_QTY")-lnQty);
                            rsItemMaster.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                            rsItemMaster.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                            rsItemMaster.updateRow();
                        }
                        //======= Item Master stock updation completed ==========
                } //If Condition
                */
            } //First for loop
            //=============Updation of stock completed=========================//
            } // End of approval status if condition
            
            
            //--------- Generate New MIR No. ------------
            if(UserDocNo) {
                rsTmp=data.getResult("SELECT RJN_NO FROM D_INV_RJN_HEADER WHERE RJN_NO='"+((String)getAttribute("RJN_NO").getObj()).trim()+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    LastError="Document no. already exist. Please specify other document no.";
                    return false;
                }
            }
            else {
            //--------- Generate New RJN No. ------------
            setAttribute("RJN_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,9, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            }
            //-------------------------------------------------
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("RJN_NO",(String)getAttribute("RJN_NO").getObj());
            rsResultSet.updateString("RJN_DATE",(String)getAttribute("RJN_DATE").getObj());
            rsResultSet.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateString("GATEPASS_DATE",(String)getAttribute("GATEPASS_DATE").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("RJN_TYPE",(int)getAttribute("RJN_TYPE").getVal());
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
            rsHistory.updateString("RJN_NO",(String)getAttribute("RJN_NO").getObj());
            rsHistory.updateString("RJN_DATE",(String)getAttribute("RJN_DATE").getObj());
            rsHistory.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String)getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("RJN_TYPE",(int)getAttribute("RJN_TYPE").getVal());
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
            
            
            //====== Now turn of RJN Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_RJN_DETAIL WHERE RJN_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("RJN_NO",(String)getAttribute("RJN_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("RJN_TYPE",1); // 1 - General Material
                rsItem.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("GRN_NO",(String)ObjItem.getAttribute("GRN_NO").getObj());
                rsItem.updateInt("GRN_SR_NO",(int)ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsItem.updateInt("GRN_TYPE",1);
                rsItem.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsItem.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsItem.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("BAL_QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
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
                rsHDetail.updateString("RJN_NO",(String)getAttribute("RJN_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("RJN_TYPE",1); // 1 - General Material
                rsHDetail.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("GRN_NO",(String)ObjItem.getAttribute("GRN_NO").getObj());
                rsHDetail.updateInt("GRN_SR_NO",(int)ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsHDetail.updateInt("GRN_TYPE",1);
                rsHDetail.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsHDetail.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("BAL_QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
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
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=9; //RJN General Material
            ObjFlow.DocNo=(String)getAttribute("RJN_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_RJN_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="RJN_NO";
            
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
            return false;
        }
    }
    
    
    
    //Updates current record
    public boolean Update() {
        Statement stItem,stLot,stStock,stTmp,stItemMaster,stPO,stHistory,stHDetail;
        ResultSet rsItem,rsLot,rsStock,rsTmp,rsItemMaster,rsPO,rsHistory,rsHDetail;
        String ItemID="",LotNo="",BOENo="",BOESrNo="",WareHouseID="",LocationID="",RJNNo="",GRNNo="",strSQL="",PONo="";
        int CompanyID=0,GRNSrNo=0,POSrNo=0,POType=0;
        double Qty=0,RejectedQty=0;
        
        Statement stHeader;
        ResultSet rsHeader;
                
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("RJN_DATE").getObj());
            
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0))
            {
               //Withing the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_RJN_HEADER_H WHERE RJN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_RJN_DETAIL_H WHERE RJN_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
           
            String theDocNo=(String)getAttribute("RJN_NO").getObj();
            stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+theDocNo+"' AND RJN_TYPE=1");
            rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
                        
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            RJNNo=(String)getAttribute("RJN_NO").getObj();
            
            //=========== Check the Quantities entered against GRN============= //
            for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                GRNNo=(String)ObjItem.getAttribute("GRN_NO").getObj();
                GRNSrNo=(int)ObjItem.getAttribute("GRN_SR_NO").getVal();
                double GRNQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
                
                if((!GRNNo.trim().equals(""))&&(GRNSrNo>0)) //GRN No. entered
                {
                    //Get the  GRN Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT REJECTED_QTY FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND SR_NO="+GRNSrNo+" AND GRN_TYPE=1";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        GRNQty=rsTmp.getDouble("REJECTED_QTY");
                    }
                    
                    //Get Total Qty Entered in RJN Against this GRN No. - Exclude current RJN No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+GRNSrNo+" AND GRN_TYPE=1 AND NOT(RJN_NO='"+RJNNo+"' AND RJN_TYPE=1) AND RJN_NO NOT IN(SELECT RJN_NO FROM D_INV_RJN_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > GRNQty) //If total Qty exceeds GRN Rejected Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds GRN No. "+GRNNo+" Sr. No. "+GRNSrNo+" qty "+GRNQty+". Please verify the input.";
                        return false;
                    }
                    
                }
            }
            //============== GRN Checking Completed ====================//
            
            
            
            //=========== Check the Max Qty Level.============= //
            /*for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                
                double MinQty=clsItem.getMinQty(CompanyID, ItemID);
                double OnHandQty=clsItem.getOnHandQty(CompanyID,ItemID);
                
                if(MinQty>0) { //Minimum level maintained
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    //Get old entered qty and deduct it from Available Qty
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsTmp=stTmp.executeQuery("SELECT QTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND TRIM(RJN_NO)='"+RJNNo+"' AND SR_NO="+i+" AND RJN_TYPE=1");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        OnHandQty=OnHandQty-rsTmp.getDouble("QTY");
                    }
                    
                    if((OnHandQty-CurrentQty)<MinQty) {
                        LastError="Cannot deduct stock of item "+ItemID+". Stock level reached at minimum level";
                        return false;
                    }
                }
                else {
                    double CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    //Get old entered qty and deduct it from Available Qty
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsTmp=stTmp.executeQuery("SELECT QTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND TRIM(RJN_NO)='"+RJNNo+"' AND SR_NO="+i+" AND RJN_TYPE=1");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        OnHandQty=OnHandQty-rsTmp.getDouble("QTY");
                    }
                    
                    if((OnHandQty-CurrentQty)<0) {
                        LastError="Cannot deduct stock of item "+ItemID+". Stock level reached at zero level";
                        return false;
                    }
                }
            }*/
            //=================================================///
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F"))
            {
            //Give Reverse Effect to Stock
            //RevertStockEffect();
            
            //-------- First Update the stock -------------//
            for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                
                GRNNo=(String)ObjItem.getAttribute("GRN_NO").getObj();
                GRNSrNo=(int)ObjItem.getAttribute("GRN_SR_NO").getVal();
                PONo=(String)ObjItem.getAttribute("PO_NO").getObj();
                POSrNo=(int)ObjItem.getAttribute("PO_SR_NO").getVal();
                POType=(int)ObjItem.getAttribute("PO_TYPE").getVal();
                
                Qty=ObjItem.getAttribute("QTY").getVal();
                ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();

                //PO
                //============== Update P.O. ============ //
                if(POType!=7) // Do not update P.O. in case of Contract Purchase. 7 - Contract Purchase
                {
                    data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY-"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);                                           
                }
                //=============== P.O. Updation Completed ===============//
                
                
                /*if(clsItem.getMaintainStock(CompanyID,ItemID)) {
                        BOENo=(String)ObjItem.getAttribute("BOE_NO").getObj();
                        ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                        BOESrNo=(String)ObjItem.getAttribute("BOE_SR_NO").getObj();
                        WareHouseID=(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                        LocationID=(String)ObjItem.getAttribute("LOCATION_ID").getObj();
                        
                        LotNo="X";
                        
                        //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND TRIM(ITEM_ID)='"+ItemID.trim()+"' AND TRIM(LOT_NO)='"+LotNo.trim()+"' AND TRIM(BOE_NO)='"+BOENo.trim()+"' AND TRIM(WAREHOUSE_ID)='"+WareHouseID.trim()+"' AND TRIM(LOCATION_ID)='"+LocationID.trim()+"'");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) //Entry muse exist in item stock table
                        {
                            stStock=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                            rsStock=stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND TRIM(ITEM_ID)='"+ItemID.trim()+"' AND TRIM(LOT_NO)='"+LotNo.trim()+"' AND TRIM(BOE_NO)='"+BOENo.trim()+"' AND TRIM(WAREHOUSE_ID)='"+WareHouseID.trim()+"' AND TRIM(LOCATION_ID)='"+LocationID.trim()+"'");
                            rsStock.first(); //There will be a single record only
                            
                            double lnLotQty=ObjItem.getAttribute("QTY").getVal(); //Record current Qty
                            
                            rsStock.updateDouble("REJECTED_QTY",rsStock.getDouble("REJECTED_QTY")+lnLotQty);
                            rsStock.updateDouble("ON_HAND_QTY",rsStock.getDouble("ON_HAND_QTY")-lnLotQty);
                            rsStock.updateDouble("AVAILABLE_QTY",rsStock.getDouble("AVAILABLE_QTY")-lnLotQty);
                            rsStock.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                            rsStock.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                            rsStock.updateRow();
                        }
                        
                        //======= Update the Item Master =========
                        stItemMaster=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        rsItemMaster=stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+CompanyID+" AND TRIM(ITEM_ID)='"+ItemID+"'");
                        rsItemMaster.first();
                        
                        if(rsItemMaster.getRow()>0) {
                            double lnQty=ObjItem.getAttribute("QTY").getVal();
                            rsItemMaster.updateDouble("REJECTED_QTY",rsItemMaster.getDouble("REJECTED_QTY")+lnQty);
                            rsItemMaster.updateDouble("ON_HAND_QTY",rsItemMaster.getDouble("ON_HAND_QTY")-lnQty);
                            rsItemMaster.updateDouble("AVAILABLE_QTY",rsItemMaster.getDouble("AVAILABLE_QTY")-lnQty);
                            rsItemMaster.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                            rsItemMaster.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                            rsItemMaster.updateRow();
                        }
                        //======= Item Master stock updation completed ==========
                } //If Condition - Maintain stock
                */
            }//First For loop
            //=============Updation of stock completed=========================//
            } //End of approval status if condition
            
            
            rsResultSet.updateString("RJN_DATE",(String)getAttribute("RJN_DATE").getObj());
            rsResultSet.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsResultSet.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsResultSet.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateString("GATEPASS_DATE",(String)getAttribute("GATEPASS_DATE").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("RJN_TYPE",(int)getAttribute("RJN_TYPE").getVal());
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
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_RJN_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+(String)getAttribute("RJN_NO").getObj()+"' AND RJN_TYPE=1");
            RevNo++;
            String RevDocNo=(String)getAttribute("RJN_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("RJN_NO",(String)getAttribute("RJN_NO").getObj());
            rsHistory.updateString("RJN_DATE",(String)getAttribute("RJN_DATE").getObj());
            rsHistory.updateInt("FOR_STORE",(int)getAttribute("FOR_STORE").getVal());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateInt("MODE_TRANSPORT",(int)getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateInt("TRANSPORTER",(int)getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String)getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateString("GATEPASS_NO",(String)getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String)getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateInt("RJN_TYPE",(int)getAttribute("RJN_TYPE").getVal());
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
            
            
            //==== Delete Previous Entries ====//
            RJNNo=(String)getAttribute("RJN_NO").getObj();
            
            data.Execute("DELETE FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND RJN_NO='"+RJNNo+"' AND RJN_TYPE=1");
            
            //====== Now turn of RJN Items ======//
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_INV_RJN_DETAIL WHERE RJN_NO='1'");
            rsItem.first();
            
            for(int i=1;i<=colRJNItems.size();i++) {
                clsRJNGenItem ObjItem=(clsRJNGenItem)colRJNItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("RJN_NO",(String)getAttribute("RJN_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("RJN_TYPE",1); //1 - General Material
                rsItem.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("GRN_NO",(String)ObjItem.getAttribute("GRN_NO").getObj());
                rsItem.updateInt("GRN_SR_NO",(int)ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsItem.updateInt("GRN_TYPE",1);
                rsItem.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsItem.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsItem.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
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
                rsHDetail.updateString("RJN_NO",(String)getAttribute("RJN_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("RJN_TYPE",1); // 1 - General Material
                rsHDetail.updateString("WAREHOUSE_ID",(String)ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String)ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("GRN_NO",(String)ObjItem.getAttribute("GRN_NO").getObj());
                rsHDetail.updateInt("GRN_SR_NO",(int)ObjItem.getAttribute("GRN_SR_NO").getVal());
                rsHDetail.updateInt("GRN_TYPE",1);
                rsHDetail.updateString("PO_NO",(String)ObjItem.getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("PO_SR_NO",(int)ObjItem.getAttribute("PO_SR_NO").getVal());
                rsHDetail.updateInt("PO_TYPE",(int)ObjItem.getAttribute("PO_TYPE").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("BOE_NO",(String)ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE",(String)ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO",(String)ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID",(int)ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
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
                
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=9; //RJN General Material
            ObjFlow.DocNo=(String)getAttribute("RJN_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_RJN_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="RJN_NO";

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_RJN_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+ObjFlow.DocNo+"' AND RJN_TYPE=1");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=9 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            
            
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
            LastError = e.getMessage();
            return false;
        }
    }
    
    private void RevertStockEffect() {
        Statement stItemMaster,stItem,stTmp;
        ResultSet rsItemMaster,rsItem,rsTmp;
        String strSQL="",RJNNo="",ItemID="",BOENo="",LotNo="",WareHouseID="",LocationID="",GRNNo="",PONo="";
        int CompanyID=0,GRNSrNo=0,POSrNo=0;
        double Qty=0,RejectedQty=0;
        
        try {
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            RJNNo=(String)getAttribute("RJN_NO").getObj();
            
            strSQL="SELECT * FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND RJN_NO='"+RJNNo+"' AND RJN_TYPE=1";
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                
                ItemID=rsTmp.getString("ITEM_ID");
                BOENo=rsTmp.getString("BOE_NO");
                LotNo="X";
                WareHouseID=rsTmp.getString("WAREHOUSE_ID");
                LocationID=rsTmp.getString("LOCATION_ID");
                Qty=rsTmp.getDouble("QTY");
                
                stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"' AND BOE_NO='"+BOENo.trim()+"' AND LOT_NO='"+LotNo.trim()+"' AND WAREHOUSE_ID='"+WareHouseID.trim()+"' AND LOCATION_ID='"+LocationID.trim()+"'");
                rsItem.first();
                
                if(rsItem.getRow()>0) {
                    rsItem.updateDouble("REJECTED_QTY",rsItem.getDouble("REJECTED_QTY")-Qty);
                    rsItem.updateDouble("ON_HAND_QTY",rsItem.getDouble("ON_HAND_QTY")+Qty);
                    rsItem.updateDouble("AVAILABLE_QTY",rsItem.getDouble("AVAILABLE_QTY")+Qty);
                    rsItem.updateBoolean("CHANGED",true);
                    rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsItem.updateRow();
                }
                
                stItemMaster=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsItemMaster=stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"'");
                rsItemMaster.first();
                
                if(rsItemMaster.getRow()>0) {
                    rsItemMaster.updateDouble("REJECTED_QTY",rsItemMaster.getDouble("REJECTED_QTY")-Qty);
                    rsItemMaster.updateDouble("ON_HAND_QTY",rsItemMaster.getDouble("ON_HAND_QTY")+Qty);
                    rsItemMaster.updateDouble("AVAILABLE_QTY",rsItemMaster.getDouble("AVAILABLE_QTY")+Qty);
                    rsItemMaster.updateBoolean("CHANGED",true);
                    rsItemMaster.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsItemMaster.updateRow();
                }
                
                rsTmp.next();
            }
            
            //Now give reverse effect to MIR Table
            strSQL="SELECT * FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND RJN_NO='"+RJNNo+"' AND RJN_TYPE=1";
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                PONo=rsTmp.getString("PO_NO");
                POSrNo=rsTmp.getInt("PO_SR_NO");
                Qty=rsTmp.getDouble("QTY");

                // Update Purchase Order - Update Received Qty 
                data.Execute("UPDATE D_PUR_PO_DETAIL SET RECD_QTY=RECD_QTY+"+Qty+",PENDING_QTY=QTY-RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo.trim()+"' AND SR_NO="+POSrNo);
                        
                rsTmp.next();
            }
        }
        catch(Exception e) {
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("RJN_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                //Give reverse effect to Stock
                //RevertStockEffect();
                
                data.Execute("DELETE FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+lCompanyID+" AND RJN_NO='"+lDocNo.trim()+"' AND RJN_TYPE=1");
                data.Execute("DELETE FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND RJN_NO='"+lDocNo.trim()+"' AND RJN_TYPE=1");
                
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
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND RJN_NO='"+pDocNo+"' AND RJN_TYPE=1";
        clsRJNGen ObjRJNGen = new clsRJNGen();
        ObjRJNGen.Filter(strCondition,pCompanyID);
        return ObjRJNGen;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_RJN_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND RJN_TYPE=1 AND RJN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND RJN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY RJN_NO";
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
        Statement stItem,stLot,stTmp;
        ResultSet rsItem,rsLot,rsTmp;
        String RJNNo="";
        int CompanyID=0,ItemCounter=0,LotCounter=0,SrNo=0;
        int RevNo=0;
        
        try {
            if(HistoryView)
            {
              RevNo=rsResultSet.getInt("REVISION_NO");  
              setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));  
            }
            else
            {
              setAttribute("REVISION_NO",0);  
            }
            
            CompanyID=rsResultSet.getInt("COMPANY_ID");
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("RJN_NO",rsResultSet.getString("RJN_NO"));
            setAttribute("RJN_DATE",rsResultSet.getString("RJN_DATE"));
            setAttribute("RJN_TYPE",rsResultSet.getInt("RJN_TYPE"));
            setAttribute("FOR_STORE",rsResultSet.getInt("FOR_STORE"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("PARTY_NAME",rsResultSet.getString("PARTY_NAME"));
            setAttribute("MODE_TRANSPORT",rsResultSet.getInt("MODE_TRANSPORT"));
            setAttribute("TRANSPORTER",rsResultSet.getInt("TRANSPORTER"));
            setAttribute("GATEPASS_TYPE",rsResultSet.getString("GATEPASS_TYPE"));
            setAttribute("GATEPASS_NO",rsResultSet.getString("GATEPASS_NO"));
            setAttribute("GATEPASS_DATE",rsResultSet.getString("GATEPASS_DATE"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
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
            
            colRJNItems.clear(); //Clear existing data
            
            RJNNo=(String)getAttribute("RJN_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(HistoryView)
            {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_RJN_DETAIL_H WHERE COMPANY_ID="+CompanyID+" AND RJN_NO='"+RJNNo+"' AND RJN_TYPE=1 AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else
            {
                rsItem=stItem.executeQuery("SELECT * FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+CompanyID+" AND RJN_NO='"+RJNNo+"' AND RJN_TYPE=1 ORDER BY SR_NO");    
            }
            rsItem.first();
            
            ItemCounter=0;
            
            while(!rsItem.isAfterLast()&&rsItem.getRow()>0) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsRJNGenItem ObjItem=new clsRJNGenItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("RJN_NO",rsItem.getString("RJN_NO"));
                ObjItem.setAttribute("RJN_TYPE",rsItem.getInt("RJN_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("WAREHOUSE_ID",rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID",rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("GRN_NO",rsItem.getString("GRN_NO"));
                ObjItem.setAttribute("GRN_SR_NO",rsItem.getInt("GRN_SR_NO"));
                ObjItem.setAttribute("GRN_TYPE",rsItem.getInt("GRN_TYPE"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_SR_NO",rsItem.getInt("PO_SR_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsItem.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("BOE_NO",rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO",rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE",rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("REJECTED_REASON_ID",rsItem.getInt("REJECTED_REASON_ID"));
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
                
                colRJNItems.put(Integer.toString(ItemCounter),ObjItem);
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
            if(HistoryView)
            {
              return false;  
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND RJN_NO='"+pDocNo+"' AND (APPROVED=1) AND RJN_TYPE=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=9 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND RJN_NO='"+pDocNo+"' AND (APPROVED=1) AND RJN_TYPE=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=9 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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

    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_RJN_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND RJN_NO='"+pDocNo+"' AND RJN_TYPE=1");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_RJN_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+pDocNo+"' AND RJN_TYPE=1");
            
            while(rsTmp.next()) {
                clsRJNGen ObjRJN=new clsRJNGen();
                
                ObjRJN.setAttribute("RJN_NO",rsTmp.getString("RJN_NO"));
                ObjRJN.setAttribute("RJN_DATE",rsTmp.getString("RJN_DATE"));
                ObjRJN.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjRJN.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjRJN.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjRJN.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjRJN.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjRJN);
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

    
 public static boolean IsRJNExist(int pCompanyID,String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT RJN_NO FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND RJN_NO='"+pDocNo+"' AND RJN_TYPE=1");
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

    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT RJN_NO FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND RJN_NO='"+pDocNo+"' AND CANCELLED=0 AND RJN_TYPE=1");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Now check every item for received qty.
                rsTmp=data.getResult("SELECT RJN_NO FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND RJN_NO='"+pDocNo+"' AND GATEPASS_QTY>0 AND RJN_TYPE=1");
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
    
    
    public static boolean CancelRJN(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            Connection tmpConn;
            Statement stSTM;
            
            if(CanCancel(pCompanyID,pDocNo)) {
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_RJN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND RJN_NO='"+pDocNo+"' AND RJN_TYPE=1");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                        
                    //DO NOTHING
                }
                else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID=9");
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_RJN_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND RJN_NO='"+pDocNo+"' AND RJN_TYPE=1");
                
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
