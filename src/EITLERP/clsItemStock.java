/**
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 **/

package EITLERP;


import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Utils.*;
import java.text.*;
import EITLERP.Stores.*;
import EITLERP.Finance.UtilFunctions;

/**
 *
 * @author  nrpatel
 * @version
 */

public class clsItemStock {
    
    private HashMap props;
    public boolean Ready = false;
    public boolean ProcessDone = false;
    
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
    
    /** Creates new clsNoDataObject */
    public clsItemStock() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("LOT_NO",new Variant(""));
        props.put("BOE_NO",new Variant(""));
        props.put("BOE_SR_NO",new Variant(""));
        props.put("OPENING_QTY",new Variant(0));
        props.put("OPENING_RATE",new Variant(0));
        props.put("TOTAL_RECEIPT_QTY",new Variant(0));
        props.put("TOTAL_ISSUED_QTY",new Variant(0));
        props.put("LAST_RECEIPT_DATE",new Variant(""));
        props.put("LAST_ISSUED_DATE",new Variant(""));
        props.put("ZERO_RECEIPT_QTY",new Variant(0));
        props.put("ZERO_ISSUED_QTY",new Variant(0));
        props.put("REJECTED_QTY",new Variant(0));
        props.put("ON_HAND_QTY",new Variant(0));
        props.put("AVAILABLE_QTY",new Variant(0));
        props.put("ALLOCATED_QTY",new Variant(0));
        props.put("CREATED_QTY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    /**Done with STM**/
    public static void CloseStockOriginal(final String pFromDate,final String pToDate) {
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        
        String strSQL="";
        String strCondition="";
        String FromDate=pFromDate;
        String ToDate=pToDate;
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        //======== Some Report writing Fields ==========//
        String strLine="";
        String ItemID="";
        String ItemName="";
        String UnitName="";
        
        double OpeningQty=0;
        double OpeningZeroQty=0;
        double OpeningRate=0;
        double OpeningValue=0;
        double InwardQty=0;
        double InwardZeroQty=0;
        double InwardRate=0;
        double InwardValue=0;
        double OutwardQty=0;
        double OutwardZeroQty=0;
        double OutwardRate=0;
        double OutwardValue=0;
        double ClosingQty=0;
        double ClosingZeroQty=0;
        double ClosingRate=0;
        double ClosingValue=0;
        double IssueQty=0;
        double IssueZeroQty=0;
        double IssueValue=0;
        double IssueRate=0;
        double DiffQty=0;
        
        double srcClosingQty=0;
        double srcClosingValue=0;
        
        //======Set of variables for Summary ============//
        double lnQty=0;
        double lnZeroValQty=0;
        //===============================================//
        
        boolean Done=false;
        
        int Row=0;
        
        long StockEntryNo=0;
        long NewEntryNo=0;
        
        String StockEntryDate="";
        
        try {
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+FromDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            //================================================================//
            
            Statement stClosingHeader=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsClosingHeader=stClosingHeader.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_HEADER LIMIT 1");
            
            Statement stClosingDetail=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsClosingDetail=stClosingDetail.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_DETAIL LIMIT 1");
            
            
            NewEntryNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_OPENING_STOCK_HEADER", "ENTRY_NO");
            
            
            rsClosingHeader.moveToInsertRow();
            rsClosingHeader.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsClosingHeader.updateLong("ENTRY_NO",NewEntryNo);
            rsClosingHeader.updateString("ENTRY_DATE", pToDate);
            rsClosingHeader.updateString("STATUS","C");
            rsClosingHeader.updateString("REMARKS","");
            rsClosingHeader.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID);
            rsClosingHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            rsClosingHeader.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.updateBoolean("CHANGED", true);
            rsClosingHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.insertRow();
            
            //========= Create a text file ==============//
            strSQL="SELECT ITEM_ID,ITEM_DESCRIPTION,UNIT FROM D_INV_ITEM_MASTER A WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" " +
            "AND MAINTAIN_STOCK=1 AND APPROVED=1 AND CANCELLED=0 "+strCondition+" ORDER BY ITEM_ID";
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            int SrNo=0;
            boolean IncludeItem=false;
            
            //Loop through each item
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                //Fill Up the variables first
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                ItemName=rsItem.getString("ITEM_DESCRIPTION");
                
                //Now Decide the Opening Stock and Value
                OpeningQty=0;
                OpeningZeroQty=0;
                OpeningValue=0;
                
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=rsTmp.getDouble("OPENING_QTY");
                    OpeningZeroQty=rsTmp.getDouble("ZERO_VAL_OPENING_QTY");
                    OpeningValue=rsTmp.getDouble("OPENING_VALUE");
                    
                    if(OpeningQty==0) {
                        OpeningValue=0;
                        OpeningRate=0;
                    }
                    else {
                        //OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                        OpeningRate=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_RATE"),2);
                    }
                }
                
                // Get the Inwards from opening stock date to upto from date
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+FromDate+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),2);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),2);
                    
                    if(OpeningValue==0) {
                        OpeningZeroQty+=OpeningQty;
                    }
                }
                
                
                // Get the Issues from opening stock date to upto from date
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+FromDate+"' AND B.ITEM_CODE='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
                    OpeningZeroQty=EITLERPGLOBAL.round(OpeningZeroQty-rsTmp.getDouble("SUM_ZERO_QTY"),2);
                }
                
                // Get the STM Issues from opening stock date to upto from date
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+FromDate+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
                    OpeningZeroQty=EITLERPGLOBAL.round(OpeningZeroQty-rsTmp.getDouble("SUM_ZERO_QTY"),2);
                }
                
                
                //we have Opening Stock and Qty.
                //Take the Average Rate
                if((OpeningQty-OpeningZeroQty)==0) {
                    OpeningRate=0;
                }
                else {
                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/(OpeningQty-OpeningZeroQty),2);
                }
                
                
                
                //Get the transactions between date
                strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+FromDate+"' AND GRN_DATE<='"+ToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+FromDate+"' AND ISSUE_DATE<='"+ToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+FromDate+"' AND STM_DATE<='"+ToDate+"'";
                strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {
                        String Operation=rsTmp.getString("OPERATION");
                        String strDocNo=rsTmp.getString("DOC_NO");
                        String strOrder=rsTmp.getString("TORDER");
                        int DocSrNo=rsTmp.getInt("SR_NO");
                        
                        if(Operation.equals("+")) {
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),2);
                            OpeningZeroQty=EITLERPGLOBAL.round(OpeningZeroQty+rsTmp.getDouble("ZERO_VAL_QTY"),2);
                            OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),2);
                            
                            
                            if((OpeningQty-OpeningZeroQty)==0) {
                                OpeningRate=0;
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/(OpeningQty-OpeningZeroQty),2);
                            }
                        }
                        
                        
                        if(Operation.equals("-")) {
                            if(OpeningQty==0) {
                                OpeningRate=0;
                                OpeningValue=0;
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                            }
                            
                            double lnIssueValue=EITLERPGLOBAL.round( (rsTmp.getDouble("QTY")-rsTmp.getDouble("ZERO_VAL_QTY")) *OpeningRate,2);
                            double lnIssueZeroQty=EITLERPGLOBAL.round( rsTmp.getDouble("ZERO_VAL_QTY"),2);
                            double lnIssueRate=0;
                            double lnIssueQty=rsTmp.getDouble("QTY")-rsTmp.getDouble("ZERO_VAL_QTY");
                            
                            
                            if(rsTmp.getDouble("QTY")==0) {
                                lnIssueRate=0;
                            }
                            else {
                                lnIssueRate=EITLERPGLOBAL.round((rsTmp.getDouble("QTY")*OpeningRate)/rsTmp.getDouble("QTY"),2);
                            }
                            
                            //Update Issue as per the weighted avg. rate
                            if(strOrder.equals("2")) {
                                data.Execute("UPDATE D_INV_ISSUE_DETAIL SET ISSUE_VALUE="+lnIssueValue+",RATE="+lnIssueRate+" WHERE ISSUE_NO='"+strDocNo+"' AND SR_NO="+DocSrNo);
                            }
                            
                            if(strOrder.equals("3")) {
                                data.Execute("UPDATE D_INV_STM_DETAIL SET NET_AMOUNT="+lnIssueValue+",RATE="+lnIssueRate+" WHERE STM_NO='"+strDocNo+"' AND SR_NO="+DocSrNo);
                            }
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("QTY"),2);
                            OpeningZeroQty=EITLERPGLOBAL.round(OpeningZeroQty-rsTmp.getDouble("ZERO_VAL_QTY"),2);
                            OpeningValue=OpeningValue- EITLERPGLOBAL.round(((rsTmp.getDouble("QTY")-rsTmp.getDouble("ZERO_VAL_QTY"))*OpeningRate),2);
                            
                            if((OpeningQty-OpeningZeroQty)==0) {
                                OpeningRate=0;
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/(OpeningQty-OpeningZeroQty),2);
                            }
                            
                            if(OpeningQty==0) {
                                OpeningValue=0;
                                OpeningRate=0;
                            }
                            
                            data.Execute("UPDATE D_INV_ITEM_MASTER SET CLOSING_VALUE="+OpeningValue+" WHERE ITEM_ID='"+ItemID+"'");
                        }
                        
                        
                        rsTmp.next();
                        
                    }
                }
                
                if((OpeningQty-OpeningZeroQty)==0) {
                    OpeningValue=0;
                }
                
                
                
                //Here is Closing Entrie will go
                
                String WareHouseID=clsItem.getItemWareHouseID(EITLERPGLOBAL.gCompanyID, ItemID);
                String LocationID=clsItem.getItemLocationID(EITLERPGLOBAL.gCompanyID, ItemID);
                String BOENo="X";
                String LOTNo="X";
                
                SrNo++;
                
                rsClosingDetail.moveToInsertRow();
                rsClosingDetail.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsClosingDetail.updateLong("ENTRY_NO",NewEntryNo);
                rsClosingDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.formatDateDB(pToDate));
                rsClosingDetail.updateLong("SR_NO",SrNo);
                rsClosingDetail.updateString("ITEM_ID",ItemID);
                rsClosingDetail.updateString("WAREHOUSE_ID",WareHouseID);
                rsClosingDetail.updateString("LOCATION_ID",LocationID);
                rsClosingDetail.updateString("BOE_NO",BOENo);
                rsClosingDetail.updateString("LOT_NO",LOTNo);
                rsClosingDetail.updateDouble("OPENING_QTY",OpeningQty);
                rsClosingDetail.updateDouble("OPENING_RATE",OpeningRate);
                rsClosingDetail.updateDouble("OPENING_VALUE",OpeningValue);
                rsClosingDetail.updateDouble("ZERO_VAL_OPENING_QTY",OpeningZeroQty);
                rsClosingDetail.updateDouble("CREATED_BY",EITLERPGLOBAL.gUserID);
                rsClosingDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsClosingDetail.updateDouble("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                rsClosingDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsClosingDetail.insertRow();
                
                
                rsItem.next();
            }
        }
        catch(Exception e) {
            
        }
        
    }
    
    /**Done with STM**/
    public void ProcessLedger(final String pFromDate,final String pToDate) {
        
        ProcessDone=false;
        
        new Thread() {
            public void run() {
                ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
                
                String strSQL="";
                String strCondition="";
                String FromDate=pFromDate;
                String ToDate=pToDate;
                
                Connection tmpConn;
                tmpConn=data.getConn();
                
                //======== Some Report writing Fields ==========//
                frmProgress objProgress = new frmProgress();
                objProgress.Initialize();
                objProgress.ShowDialog();
                
                String strLine="";
                String ItemID="";
                String ItemName="";
                String UnitName="";
                
                double OpeningQty=0;
                double OpeningZeroQty=0;
                double OpeningRate=0;
                double OpeningValue=0;
                double InwardQty=0;
                double InwardZeroQty=0;
                double InwardRate=0;
                double InwardValue=0;
                double OutwardQty=0;
                double OutwardZeroQty=0;
                double OutwardRate=0;
                double OutwardValue=0;
                double ClosingQty=0;
                double ClosingZeroQty=0;
                double ClosingRate=0;
                double ClosingValue=0;
                double IssueQty=0;
                double IssueZeroQty=0;
                double IssueValue=0;
                double IssueRate=0;
                double DiffQty=0;
                
                double srcClosingQty=0;
                double srcClosingValue=0;
                
                //======Set of variables for Summary ============//
                double lnQty=0;
                double lnZeroValQty=0;
                //===============================================//
                
                boolean Done=false;
                
                int Row=0;
                
                long StockEntryNo=0;
                long NewEntryNo=0;
                int Counter=0;
                int Max=0;
                
                String StockEntryDate="";
                
                try {
                    
                    //======= Find the last cut-off date stock entry =================//
                    rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+FromDate+"' ORDER BY ENTRY_DATE DESC ");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        StockEntryNo=rsTmp.getLong("ENTRY_NO");
                        StockEntryDate=rsTmp.getString("ENTRY_DATE");
                    }
                    //================================================================//
                    
                    
                    //========= Get the count ============//
                    strSQL="SELECT COUNT(A.ITEM_ID) AS THECOUNT FROM D_INV_ITEM_MASTER A,STOCK_LEDGER_TEMP B WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MAINTAIN_STOCK=1 AND A.APPROVED=1 AND A.CANCELLED=0 AND A.ITEM_ID=B.ITEM_ID AND A.ITEM_ID NOT LIKE 'RM%' "; // AND A.ITEM_ID='20201001' 
                    rsItem=data.getResult(strSQL);
                    rsItem.first();
                    
                    if(rsItem.getRow()>0) {
                        Max=rsItem.getInt("THECOUNT");
                    }
                    
                    
                    //===================================================================//
                    //====================== PROCESS WEIGHTED AVERAGE   =================//
                    //===================================================================//
                    objProgress.SetMax(Max);
                    objProgress.SetMin(0);
                    objProgress.SetValue(0);
                    objProgress.repaint();
                    
                    objProgress.SetText("Starting Process");
                    
                    //========= Create a text file ==============//
                    strSQL="SELECT DISTINCT(A.ITEM_ID),A.ITEM_DESCRIPTION,A.UNIT FROM D_INV_ITEM_MASTER A,STOCK_LEDGER_TEMP B WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MAINTAIN_STOCK=1 AND A.APPROVED=1 AND A.CANCELLED=0  AND A.ITEM_ID=B.ITEM_ID "+strCondition+" AND A.ITEM_ID NOT LIKE 'RM%' GROUP BY ITEM_ID"; // AND A.ITEM_ID='20201001' 
                    rsItem=data.getResult(strSQL);
                    rsItem.first();
                    
                    while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                        objProgress.SetValue(rsItem.getRow());
                        objProgress.repaint();
                        
                        ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                        
                        objProgress.SetText("Processing Item : "+ItemID);
                        objProgress.repaint();
                        
                        OpeningQty=0;
                        OpeningRate=0;
                        OpeningValue=0;
                        
                        strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                        rsTmp=data.getResult(strSQL);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            OpeningQty=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_QTY"),3);
                            OpeningRate=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_RATE"),3);
                            OpeningValue=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_VALUE"),3);
                            
                            //if(OpeningQty==0){
                            if(OpeningQty<=0){ // change on 08/06/2009 -- Mrugesh
                                OpeningRate=0;
                                OpeningValue=0; // change on 08/06/2009 -- Mrugesh
                                OpeningQty=0; // change on 08/06/2009 -- Mrugesh
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                        }
                        
                        strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+StockEntryDate+"' AND GRN_DATE<='"+pToDate+"'";
                        strSQL=strSQL+" UNION ";
                        strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+StockEntryDate+"' AND ISSUE_DATE<='"+pToDate+"'";
                        strSQL=strSQL+" UNION ";
                        strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.STM_TYPE=B.STM_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+StockEntryDate+"' AND STM_DATE<='"+pToDate+"'";
                        strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                        
                        rsTmp=data.getResult(strSQL);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            while(!rsTmp.isAfterLast()) {
                                
                                String Operation=rsTmp.getString("OPERATION");
                                String strDocNo=rsTmp.getString("DOC_NO");
                                String strOrder=rsTmp.getString("TORDER");
                                int SrNo=rsTmp.getInt("SR_NO");
                                
                                if(Operation.equals("+")) {
                                    OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                                    
                                    if(OpeningQty==0) {
                                        OpeningRate=0;
                                        //System.out.println("GRN  Item Id = "+ItemID+ " Running Opening Qty = "+OpeningQty+" OpeningValue = " + OpeningValue);
                                        OpeningValue=0; // change as on 07/06/2009
                                    }
                                    else {
                                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    }
                                }
                                
                                if(Operation.equals("-")) {
                                    double currentIssueValue=0;
                                    double currentIssueRate=0;
                                    double currentIssueQty=0;
                                    
                                    //========NEW CODE=========//
                                    currentIssueQty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                                    
                                    if(OpeningValue<0) {
                                        OpeningRate=0;
                                        OpeningValue=0;
                                    } else {
                                        if(OpeningQty>0) {
                                            if (currentIssueQty==OpeningQty) {
                                                currentIssueValue=EITLERPGLOBAL.round(OpeningValue,3);
                                            } else {
                                                currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                                            }
                                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                            //OpeningValue=EITLERPGLOBAL.round(OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3),3);
                                            OpeningValue=EITLERPGLOBAL.round(OpeningValue - currentIssueValue,3);
                                        } else {
                                            OpeningRate=0;
                                            OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                                        }
                                    }
                                    
                                    
                                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                                    currentIssueRate=EITLERPGLOBAL.round(OpeningRate,3);
                                    
                                    
                                    //=====END OF NEW CODE=====//
                                    
                                    if(strOrder.equals("2")) {
                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET ISSUE_VALUE="+EITLERPGLOBAL.round(currentIssueValue,3)+",RATE="+EITLERPGLOBAL.round(currentIssueRate,3)+" WHERE ISSUE_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                                    }
                                    
                                    if(strOrder.equals("3")) {
                                        data.Execute("UPDATE D_INV_STM_DETAIL SET NET_AMOUNT="+EITLERPGLOBAL.round(currentIssueValue,3)+",RATE="+EITLERPGLOBAL.round(currentIssueRate,3)+" WHERE STM_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                                    }
                                    
                                    
                                    if(OpeningQty!=0) {
                                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    } else {
                                        //System.out.println("Issue  Item Id = "+ItemID+ " Running Opening Qty = "+OpeningQty+" OpeningValue = " + OpeningValue);
                                        OpeningRate=0;
                                        OpeningValue=0; //change on 07/06/2009
                                    }
                                }
                                
                                rsTmp.next();
                            }
                        }
                        rsItem.next();
                    }
                    //===========================END WEIGHTED AVG. PROCESS ================================//
                    
                    
                    ProcessDone=true;
                    objProgress.Hide();
                }
                catch(Exception e) {
                    ProcessDone=true;
                }
                
            };
        }.start();
        
    }
    
    public double ProcessLedger(final String pFromDate,final String pToDate, String pItemID) {
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        
        String strSQL="";
        String strCondition="";
        String FromDate=pFromDate;
        String ToDate=pToDate;
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        //======== Some Report writing Fields ==========//
        double OpeningQty=0;
        double OpeningRate=0;
        double OpeningValue=0;
        double IssueQty=0;
        double IssueValue=0;
        double IssueRate=0;
        long StockEntryNo=0;
        String StockEntryDate="";
        
        try {
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+FromDate+"' ORDER BY ENTRY_DATE DESC ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            //================================================================//
            
            
            
            //===================================================================//
            //====================== PROCESS WEIGHTED AVERAGE   =================//
            //===================================================================//
            
            //========= Create a text file ==============//
            strSQL="SELECT ITEM_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 AND ITEM_ID='"+pItemID+"'"; //
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            
            
            pItemID=pItemID.trim()+EITLERPGLOBAL.Replicate(" ", 12-pItemID.trim().length());
            
            OpeningQty=0;
            OpeningRate=0;
            OpeningValue=0;
            
            strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+pItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_QTY"),3);
                OpeningRate=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_RATE"),3);
                OpeningValue=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_VALUE"),3);
                
                if(OpeningQty<=0){
                    OpeningRate=0;
                    OpeningValue=0;
                    OpeningQty=0;
                } else {
                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                }
            }
            
            strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+pItemID+"' AND A.GRN_DATE>='"+StockEntryDate+"' AND A.GRN_DATE<='"+pToDate+"'";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+pItemID+"' AND A.ISSUE_DATE>='"+StockEntryDate+"' AND A.ISSUE_DATE<='"+pToDate+"'";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.STM_TYPE=B.STM_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+pItemID+"' AND A.STM_DATE>='"+StockEntryDate+"' AND A.STM_DATE<='"+pToDate+"'";
            strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
            
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    String Operation=rsTmp.getString("OPERATION");
                    String strDocNo=rsTmp.getString("DOC_NO");
                    String strOrder=rsTmp.getString("TORDER");
                    int SrNo=rsTmp.getInt("SR_NO");
                    
                    if(Operation.equals("+")) {
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                        OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                        
                        if(OpeningQty==0) {
                            OpeningRate=0;
                            //System.out.println("GRN  Item Id = "+ItemID+ " Running Opening Qty = "+OpeningQty+" OpeningValue = " + OpeningValue);
                            OpeningValue=0;
                        } else {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                        }
                    }
                    
                    if(Operation.equals("-")) {
                        double currentIssueValue=0;
                        double currentIssueRate=0;
                        double currentIssueQty=0;
                        
                        //========NEW CODE=========//
                        currentIssueQty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                        
                        if(OpeningValue<0) {
                            OpeningRate=0;
                            OpeningValue=0;
                        } else {
                            if(OpeningQty>0) {
                                if (currentIssueQty==OpeningQty) {
                                    currentIssueValue=EITLERPGLOBAL.round(OpeningValue,3);
                                } else {
                                    currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                                }
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                OpeningValue=EITLERPGLOBAL.round(OpeningValue - currentIssueValue,3);
                            } else {
                                OpeningRate=0;
                                OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                            }
                        }
                        
                        
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                        currentIssueRate=EITLERPGLOBAL.round(OpeningRate,3);
                        
                        
                        //=====END OF NEW CODE=====//
                        
                        if(strOrder.equals("2")) {
                            data.Execute("UPDATE D_INV_ISSUE_DETAIL SET ISSUE_VALUE="+EITLERPGLOBAL.round(currentIssueValue,3)+",RATE="+EITLERPGLOBAL.round(currentIssueRate,3)+" WHERE ISSUE_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                        }
                        
                        if(strOrder.equals("3")) {
                            data.Execute("UPDATE D_INV_STM_DETAIL SET NET_AMOUNT="+EITLERPGLOBAL.round(currentIssueValue,3)+",RATE="+EITLERPGLOBAL.round(currentIssueRate,3)+" WHERE STM_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                        }
                        
                        
                        if(OpeningQty!=0) {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                        } else {
                            OpeningRate=0;
                            OpeningValue=0; //change on 07/06/2009
                        }
                    }
                    
                    rsTmp.next();
                }
            }
            //===========================END WEIGHTED AVG. PROCESS ================================//
            
        }
        catch(Exception e) {
            return 0;
        }
        return OpeningRate;
    }
    
    
    /**Done with STM**/
    public void CloseStockGen(final String pFromDate,final String pToDate) {
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        ResultSet rsClosingHeader,rsClosingDetail;
        ResultSet rsItemGroup;
        Statement stClosingHeader,stClosingDetail;
        
        String strSQL="";
        String strCondition="";
        String strOrder="";
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        String strLine="",ItemID="",ItemName="",UnitName="";
        int ItemCount=0;
        double OpeningQty=0,OpeningRate=0,OpeningValue=0,InwardQty=0;
        double InwardZeroQty=0,InwardRate=0,InwardValue=0,OutwardQty=0;
        double OutwardZeroQty=0,OutwardRate=0,OutwardValue=0,ClosingQty=0;
        double ClosingZeroQty=0,ClosingRate=0,ClosingValue=0,IssueQty=0;
        double IssueZeroQty=0,IssueValue=0,IssueRate=0;
        boolean TransactionsFound=true,Done=false;
        
        String strDocNo="";
        String strDocDate="";
        
        long StockEntryNo=0,SrNo=0;
        String StockEntryDate="",strFromDate="";
        
        
        try {
            System.out.println("Genaral Item closing start...");
            strOrder=" ORDER BY ITEM_ID ";
            
            data.Execute("DELETE FROM STOCK_LEDGER_TEMP");
            
            strCondition="INSERT INTO STOCK_LEDGER_TEMP (ITEM_ID) ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_ID) FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND GRN_DATE>='"+pFromDate+"' AND GRN_DATE <='"+pToDate+"'";
            strCondition=strCondition+" UNION ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_CODE) FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND A.ISSUE_DATE>='"+pFromDate+"' AND ISSUE_DATE <='"+pToDate+"'";
            strCondition=strCondition+" UNION ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_ID) FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO AND A.STM_DATE>='"+pFromDate+"' AND STM_DATE <='"+pToDate+"'";
            
            
            data.Execute(strCondition);
            
            strCondition="";
            
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pFromDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
                strFromDate=EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE"));
            }
            //================================================================//
            
            
            
            stClosingHeader=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsClosingHeader=stClosingHeader.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_HEADER LIMIT 1");
            
            stClosingDetail=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsClosingDetail=stClosingDetail.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_DETAIL LIMIT 1");
            
            
            long NewEntryNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_OPENING_STOCK_HEADER", "ENTRY_NO");
            
            
            rsClosingHeader.moveToInsertRow();
            rsClosingHeader.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsClosingHeader.updateLong("ENTRY_NO",NewEntryNo);
            rsClosingHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.addDaysToDate(pToDate,1,"yyyy-MM-dd"));
            rsClosingHeader.updateInt("STATUS",1);
            int Year = EITLERPGLOBAL.getYear(pToDate);
            rsClosingHeader.updateString("REMARKS","Opening Stock as on 1 Apr. " + Year);
            rsClosingHeader.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID);
            rsClosingHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            rsClosingHeader.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.updateBoolean("CHANGED", true);
            rsClosingHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.insertRow();
            
            
            
            //===================================================================//
            //====================== PROCESS WEIGHTED AVERAGE   =================//
            //===================================================================//
            
            strSQL="SELECT DISTINCT(A.ITEM_ID),A.ITEM_DESCRIPTION,A.UNIT FROM D_INV_ITEM_MASTER A,STOCK_LEDGER_TEMP B WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.APPROVED=1 AND A.CANCELLED=0  AND A.ITEM_ID=B.ITEM_ID "+strCondition+" AND A.CATEGORY_ID NOT IN (10,11,12,13,14,3) GROUP BY A.ITEM_ID"; //AND A.MAINTAIN_STOCK=1
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                
                OpeningQty=0;
                OpeningRate=0;
                OpeningValue=0;
                
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_QTY"),3);
                    OpeningRate=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_RATE"),3);
                    OpeningValue=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_VALUE"),3);
                    
                    //if(OpeningQty==0) {
                    if(OpeningQty<=0) { // change as on 10/06/2009 -- Mrugesh
                        OpeningRate=0;
                        OpeningValue=0; // change as on 10/06/2009 -- Mrugesh
                        OpeningQty=0;   // change as on 10/06/2009 -- Mrugesh
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                    }
                }
                
                strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pToDate+"'";
                strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                
                
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {
                        
                        String Operation=rsTmp.getString("OPERATION");
                        strDocNo=rsTmp.getString("DOC_NO");
                        strOrder=rsTmp.getString("TORDER");
                        SrNo=rsTmp.getInt("SR_NO");
                        
                        if(Operation.equals("+")) {
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                            OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                            
                            //if(OpeningQty==0) {
                            if(OpeningQty<=0) {
                                OpeningRate=0;
                                //System.out.println("GRN  Item Id = "+ItemID+ " Running Opening Qty = "+OpeningQty+" OpeningValue = " + OpeningValue); // change as on 10/06/2009 -- Mrugesh
                                OpeningValue=0; // change as on 10/06/2009 -- Mrugesh
                                
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                        }
                        
                        
                        if(Operation.equals("-")) {
                            double currentIssueValue=0;
                            double currentIssueRate=0;
                            double currentIssueQty=0;
                            
                            //========NEW CODE=========//
                            currentIssueQty=rsTmp.getDouble("QTY");
                            
                            if(OpeningValue<0) {
                                OpeningRate=0;
                                OpeningValue=0;
                            }
                            else {
                                if(OpeningQty>0) {
                                    //OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                                    //OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                                    
                                    // change as on 10/06/2009 -- start -- Mrugesh
                                    if (currentIssueQty==OpeningQty) {
                                        currentIssueValue=EITLERPGLOBAL.round(OpeningValue,3);
                                    } else {
                                        currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                                    }
                                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    //OpeningValue=EITLERPGLOBAL.round(OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3),3);
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue - currentIssueValue,3);
                                    // change as on 10/06/2009 -- end -- Mrugesh
                                    
                                }
                                else {
                                    OpeningRate=0;
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                                }
                            }
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                            currentIssueRate=EITLERPGLOBAL.round(OpeningRate,3);
                            currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                            //=====END OF NEW CODE=====//
                            
                            if(strOrder.equals("2")) {
                                data.Execute("UPDATE D_INV_ISSUE_DETAIL SET ISSUE_VALUE="+currentIssueValue+",RATE="+currentIssueRate+" WHERE ISSUE_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                            }
                            
                            if(strOrder.equals("3")) {
                                data.Execute("UPDATE D_INV_STM_DETAIL SET NET_AMOUNT="+currentIssueValue+",RATE="+currentIssueRate+" WHERE STM_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                            }
                            
                            if(OpeningQty!=0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                            else {
                                //System.out.println("Issue  Item Id = "+ItemID+ " Running Opening Qty = "+OpeningQty+" OpeningValue = " + OpeningValue);
                                OpeningRate=0;
                                OpeningValue=0; // change as on 10/06/2009 -- Mrugesh
                            }
                        }
                        
                        rsTmp.next();
                    }
                }
                
                rsItem.next();
            }
            //===========================END WEIGHTED AVG. PROCESS ================================//
            
            
            
            
            //==========Get the Data from Item Master for ===============//
            boolean Continue = false;
            
            strSQL="SELECT ITEM_ID,ITEM_DESCRIPTION,UNIT,LOCATION_ID FROM D_INV_ITEM_MASTER A WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 "+strCondition+" AND A.CATEGORY_ID NOT IN (10,11,12,13,14,3) GROUP BY A.ITEM_ID ORDER BY ITEM_ID "; // AND MAINTAIN_STOCK=1
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            ItemCount=0;
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                
                ItemCount++;
                
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                
                OpeningQty=0;
                OpeningValue=0;
                
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=rsTmp.getDouble("OPENING_QTY");
                    OpeningValue=rsTmp.getDouble("OPENING_VALUE");
                    
                    //if(OpeningQty==0) {
                    if(OpeningQty<=0) { // change as on 10/06/2009 - Mrugesh
                        OpeningValue=0;
                        OpeningRate=0;
                        OpeningQty=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                    }
                }
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),3);
                }
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_CODE='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
                }
                
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
                }
                
                //if(OpeningQty==0) {
                if(OpeningQty<=0) {
                    OpeningRate=0;
                    OpeningValue=0; //change as on 10/06/2008 -- Mrugesh
                    OpeningQty=0; //change as on 10/06/2008  -- Mrugesh
                }
                else {
                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                }
                
                TransactionsFound=false;
                
                strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pToDate+"'";
                strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    TransactionsFound=true;
                    while(!rsTmp.isAfterLast()) {
                        
                        String Operation=rsTmp.getString("OPERATION");
                        strOrder=rsTmp.getString("TORDER");
                        strDocNo=rsTmp.getString("DOC_NO");
                        strDocNo=strDocNo+EITLERPGLOBAL.Replicate(" ", 12-strDocNo.length());
                        strDocDate=EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE"));
                        
                        if(Operation.equals("+")) {
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                            OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                            
                            if(OpeningQty==0) {
                                OpeningRate=0;
                                OpeningValue=0; //change as on 10/06/2009
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                        }
                        
                        
                        if(Operation.equals("-")) {
                            double currentIssueValue=0;
                            double currentIssueRate=0;
                            double currentIssueQty=0;
                            
                            //========NEW CODE=========//
                            currentIssueQty=rsTmp.getDouble("QTY");
                            
                            if(OpeningValue<0) {
                                OpeningRate=0;
                                OpeningValue=0; //change on 10/06/2009 -- Mrugesh
                            }
                            else {
                                if(OpeningQty>0) {
                                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue- EITLERPGLOBAL.round(rsTmp.getDouble("VALUE"),3),3); //change as on 10/06/2009
                                    //OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                                }
                                else {
                                    OpeningRate=0;
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                                }
                            }
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                            
                            currentIssueRate=OpeningRate;
                            //currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                            currentIssueValue=EITLERPGLOBAL.round(rsTmp.getDouble("VALUE"),3); // change as on 10/06/2009 -- Mrugesh
                            //=====END OF NEW CODE=====//
                            
                            
                            if(OpeningQty!=0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                            else {
                                OpeningRate=0;
                                OpeningValue=0; // change as on 10/06/2009 - Mrugesh
                            }
                        }
                        
                        rsTmp.next();
                    }
                }
                
                if(OpeningQty==0) {
                    OpeningValue=0;
                }
                
                //UPDATE CLOSING QTY AND VALUE HERE
                String WareHouseID=clsItem.getItemWareHouseID(EITLERPGLOBAL.gCompanyID, ItemID);
                String LocationID=clsItem.getItemLocationID(EITLERPGLOBAL.gCompanyID, ItemID);
                String BOENo="X";
                String LOTNo="X";
                
                SrNo++;
                
                rsClosingDetail.moveToInsertRow();
                rsClosingDetail.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsClosingDetail.updateLong("ENTRY_NO",NewEntryNo);
                rsClosingDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.addDaysToDate(pToDate,1,"yyyy-MM-dd"));
                rsClosingDetail.updateLong("SR_NO",SrNo);
                rsClosingDetail.updateString("ITEM_ID",ItemID);
                rsClosingDetail.updateString("WAREHOUSE_ID",WareHouseID);
                rsClosingDetail.updateString("LOCATION_ID",LocationID);
                rsClosingDetail.updateString("BOE_NO",BOENo);
                rsClosingDetail.updateString("LOT_NO",LOTNo);
                rsClosingDetail.updateDouble("OPENING_QTY",OpeningQty);
                rsClosingDetail.updateDouble("OPENING_RATE",OpeningRate);
                rsClosingDetail.updateDouble("OPENING_VALUE",OpeningValue);
                rsClosingDetail.updateDouble("ZERO_VAL_OPENING_QTY",0);
                rsClosingDetail.updateDouble("CREATED_BY",EITLERPGLOBAL.gUserID);
                rsClosingDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsClosingDetail.updateDouble("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                rsClosingDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsClosingDetail.insertRow();
                
                rsItem.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Raw Material closing End...");
    }
    
    
    public void CloseStockRaw(final String pFromDate,final String pToDate) {
        
        //==============================================================================================================
        //==============================================================================================================
        //=====================START OF CLOSING STOCK RAW MATERIAL PROGRAM==============================================
        //==============================================================================================================
        //==============================================================================================================
        System.out.println("Raw Material closing Start...");
        try{
            
            Connection tmpConn;
            tmpConn=data.getConn();
            String BOENo="";
            int StockEntryNo=0;
            int SrNo=0;
            String ItemId="",StockEntryDate="";
            
            //======= Find the last cut-off date stock entry =================//
            ResultSet rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pFromDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getInt("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            //================================================================//
            
            //========GET stock header entry ==========D_COM_OPENING_STOCK_HEADER=================
            
            ResultSet rsClosingHeader,rsClosingDetail;
            Statement stClosingHeader,stClosingDetail;
            
            long NewEntryNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_OPENING_STOCK_HEADER", "ENTRY_NO");
            NewEntryNo--;
            //========GET stock header entry END ==========D_COM_OPENING_STOCK_HEADER=================
            
            
            //========GET stock DETAIL entry ==========D_COM_OPENING_STOCK_DETAIL=================
            SrNo = Integer.parseInt(data.getStringValueFromDB("SELECT MAX(SR_NO) AS SR_NO FROM D_COM_OPENING_STOCK_DETAIL WHERE ENTRY_NO = "+NewEntryNo+" ORDER BY SR_NO DESC"));
            
            stClosingDetail=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsClosingDetail=stClosingDetail.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_DETAIL LIMIT 1");
            
            
            String Heading="";
            ResultSet rsMain;
            
            String Qry="SELECT A.ITEM_ID,A.LOT_NO  AS AUTO_LOT_NO, A.ITEM_LOT_NO, ROUND(A.OPENING_QTY,3) AS QTY, "+
            "ROUND(A.OPENING_VALUE,3) AS VALUE, ROUND(A.OPENING_RATE,3) AS RATE,A.BOE_NO  "+
            "FROM D_COM_OPENING_STOCK_DETAIL A, D_INV_ITEM_MASTER B  "+
            "WHERE A.ITEM_ID = B.ITEM_ID AND A.COMPANY_ID = B.COMPANY_ID  "+
            "AND B.CATEGORY_ID IN (SELECT CATEGORY_ID FROM D_INV_ITEM_CATEGORY WHERE CATEGORY_TYPE =2 )  "+
            "AND A.OPENING_QTY >0 AND B.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.ENTRY_NO ="+StockEntryNo+" " +
            "AND B.APPROVED=1 AND B.CANCELLED=0 "+
            "UNION  "+
            "SELECT B.ITEM_ID, AUTO_LOT_NO AS AUTO_LOT_NO, C.ITEM_LOT_NO, "+
            "ROUND(C.LOT_ACCEPTED_QTY,3) AS QTY,ROUND(C.LOT_ACCEPTED_QTY*B.LANDED_RATE,3) AS VALUE,  "+
            "ROUND(B.LANDED_RATE,3) AS RATE,B.BOE_NO  "+
            "FROM D_INV_GRN_HEADER A, D_INV_GRN_DETAIL B, D_INV_GRN_LOT C,D_INV_ITEM_MASTER D "+
            "WHERE A.GRN_NO = B.GRN_NO AND A.GRN_NO=C.GRN_NO AND B.GRN_NO=C.GRN_NO  "+
            "AND A.COMPANY_ID = B.COMPANY_ID AND A.COMPANY_ID=C.COMPANY_ID  "+
            "AND A.COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" AND B.COMPANY_ID =C.COMPANY_ID AND B.SR_NO = C.GRN_SR_NO  "+
            "AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID = C.ITEM_ID AND B.ITEM_ID = D.ITEM_ID " +
            "AND D.APPROVED=1 AND D.CANCELLED=0 "+
            "AND D.CATEGORY_ID IN  "+
            "(SELECT CATEGORY_ID FROM D_INV_ITEM_CATEGORY WHERE CATEGORY_TYPE =2 ) "+
            "AND A.GRN_DATE >='"+StockEntryDate +"' AND A.GRN_DATE <='"+ pToDate + "'  "+
            "ORDER BY ITEM_ID, AUTO_LOT_NO";
            
            
            rsMain=data.getResult(Qry,clsFinYear.getDBURL(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.FinYearFrom));
            int counter=0;
            rsMain.first();
            if(rsMain.getRow()>0) {
                while (!rsMain.isAfterLast()){
                    counter++;
                    double ItemLotStockQty=0, ItemLotStockValue=0;
                    ItemId= rsMain.getString("ITEM_ID");
                    
                    String AutoLotNo= rsMain.getString("AUTO_LOT_NO");
                    String LotNo= rsMain.getString("ITEM_LOT_NO");
                    ItemLotStockQty=EITLERPGLOBAL.round(rsMain.getDouble("QTY"),3);
                    ItemLotStockValue=EITLERPGLOBAL.round(rsMain.getDouble("VALUE"),3);
                    double ItemRate = EITLERPGLOBAL.round(rsMain.getDouble("RATE"),3);
                    BOENo = rsMain.getString("BOE_NO");
                    //System.out.println("Item ID : "+ItemId + " Auto Lot No:"+ AutoLotNo+" Qty.: "+ItemLotStockQty+ " Value:"+ItemLotStockValue);
                    
                    Qry="SELECT A.ISSUE_NO,B.SR_NO,ROUND(C.ISSUED_LOT_QTY,3) AS ISSUED_LOT_QTY,ROUND(C.ISSUED_LOT_QTY*B.RATE,3) AS RECEIPT_VALUE " +
                    "FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_DETAIL B, D_INV_ISSUE_LOT C "+
                    "WHERE A.ISSUE_NO = B.ISSUE_NO AND A.ISSUE_NO=C.ISSUE_NO AND B.ISSUE_NO=C.ISSUE_NO "+
                    "AND A.COMPANY_ID = B.COMPANY_ID AND A.COMPANY_ID=C.COMPANY_ID AND B.COMPANY_ID =C.COMPANY_ID "+
                    "AND B.ITEM_CODE = C.ITEM_ID AND B.ITEM_CODE = '"+ItemId+"'  AND C.AUTO_LOT_NO='"+AutoLotNo+"' "+
                    "AND B.SR_NO = C.ISSUE_SR_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" "+
                    "AND A.ISSUE_DATE >='"+StockEntryDate+"' AND A.ISSUE_DATE <='"+ pToDate + "' "+
                    " UNION " +
                    "SELECT A.STM_NO AS ISSUE_NO,B.SR_NO,ROUND(C.ISSUED_LOT_QTY,3) AS ISSUED_LOT_QTY ,ROUND(C.ISSUED_LOT_QTY * B.RATE,3) AS RECEIPT_VALUE "+
                    "FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B,D_INV_STM_LOT C "+
                    "WHERE A.COMPANY_ID = "+EITLERPGLOBAL.gCompanyID+" AND A.STM_NO=B.STM_NO AND A.STM_NO=C.STM_NO " +
                    "AND A.STM_TYPE=B.STM_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 " +
                    "AND B.ITEM_ID = C.ITEM_ID AND B.SR_NO = C.STM_SR_NO "+
                    "AND A.STM_DATE >='"+StockEntryDate+"' AND A.STM_DATE <='"+ pToDate + "'  " +
                    "AND B.ITEM_ID = '"+ItemId+"' AND C.AUTO_LOT_NO='"+AutoLotNo+"' ";
                    
                    /*if(ItemId.equals("RM11135003")) {
                        System.out.println(Qry);
                        System.out.println("stop");
                    }*/
                    
                    double IssueQty=0;
                    double IssueValue=0;
                    double tempQty = 0;
                    rsTmp = data.getResult(Qry);
                    rsTmp.first();
                    if (rsTmp.getRow()>0){
                        while (!rsTmp.isAfterLast()) {
                            tempQty = EITLERPGLOBAL.round(rsTmp.getDouble("ISSUED_LOT_QTY"),3);
                            IssueQty+=tempQty;
                            IssueValue+=EITLERPGLOBAL.round(ItemRate*tempQty,3);
                            rsTmp.next();
                        }
                    }
                    ItemLotStockQty-= EITLERPGLOBAL.round(IssueQty,3);
                    ItemLotStockValue-=EITLERPGLOBAL.round(IssueValue,3);
                    
                    if(ItemLotStockQty ==0) {
                        ItemLotStockValue=0.0;
                    }
                    
                    String ItemDescription = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, ItemId);
                    int UnitID = clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID, ItemId);
                    String UnitDesc =clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT", UnitID);
                    
                    System.out.println(counter +" " + ItemId + " " + AutoLotNo + " " + " " + EITLERPGLOBAL.round(ItemLotStockQty,3) + " " + EITLERPGLOBAL.round(ItemLotStockValue,3));
                    
                    String WareHouseID=clsItem.getItemWareHouseID(EITLERPGLOBAL.gCompanyID, ItemId);
                    String LocationID=clsItem.getItemLocationID(EITLERPGLOBAL.gCompanyID, ItemId);
                    
                    if(ItemLotStockQty >0) {
                        SrNo++;
                        rsClosingDetail.moveToInsertRow();
                        rsClosingDetail.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsClosingDetail.updateLong("ENTRY_NO",NewEntryNo);
                        rsClosingDetail.updateString("ENTRY_DATE",EITLERPGLOBAL.addDaysToDate(pToDate,1,"yyyy-MM-dd"));
                        rsClosingDetail.updateLong("SR_NO",SrNo);
                        rsClosingDetail.updateString("ITEM_ID",ItemId);
                        rsClosingDetail.updateString("WAREHOUSE_ID",WareHouseID);
                        rsClosingDetail.updateString("LOCATION_ID",LocationID);
                        rsClosingDetail.updateString("BOE_NO",BOENo);
                        rsClosingDetail.updateString("ITEM_LOT_NO",LotNo);
                        rsClosingDetail.updateString("LOT_NO",AutoLotNo);
                        rsClosingDetail.updateDouble("OPENING_QTY",EITLERPGLOBAL.round(ItemLotStockQty,3));
                        rsClosingDetail.updateDouble("OPENING_RATE",ItemRate);
                        rsClosingDetail.updateDouble("OPENING_VALUE",EITLERPGLOBAL.round(ItemLotStockValue,3));
                        rsClosingDetail.updateDouble("ZERO_VAL_OPENING_QTY",0);
                        rsClosingDetail.updateDouble("CREATED_BY",EITLERPGLOBAL.gUserID);
                        rsClosingDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsClosingDetail.updateDouble("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                        rsClosingDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsClosingDetail.insertRow();
                    }
                    rsMain.next();
                }
                
            }
            //========GET stock DETAIL entry ==========D_COM_OPENING_STOCK_DETAIL=================
        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Raw Material closing End...");
        //==============================================================================================================
        //==============================================================================================================
        //=====================END OF CLOSING STOCK RAW MATERIAL PROGRAM================================================
        //==============================================================================================================
        //==============================================================================================================
        
    }
    
    
    public void CloseStockTemp(final String pFromDate,final String pToDate) {
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        ResultSet rsClosingHeader,rsClosingDetail;
        ResultSet rsItemGroup;
        Statement stClosingHeader,stClosingDetail;
        
        String strSQL="";
        String strCondition="";
        String strOrder="";
        
        Connection tmpConn;
        
        int CompanyID = 3;
        String strLine="",ItemID="",ItemName="",UnitName="";
        int ItemCount=0;
        double OpeningQty=0,OpeningRate=0,OpeningValue=0,InwardQty=0;
        double InwardZeroQty=0,InwardRate=0,InwardValue=0,OutwardQty=0;
        double OutwardZeroQty=0,OutwardRate=0,OutwardValue=0,ClosingQty=0;
        double ClosingZeroQty=0,ClosingRate=0,ClosingValue=0,IssueQty=0;
        double IssueZeroQty=0,IssueValue=0,IssueRate=0;
        boolean TransactionsFound=true,Done=false;
        
        String strDocNo="";
        String strDocDate="";
        String dbURL = "";
        long StockEntryNo=0,SrNo=0;
        String StockEntryDate="",strFromDate="";
        
        
        try {
            
            dbURL = clsFinYear.getDBURL(CompanyID, 2009);
            tmpConn=data.getConn(dbURL);
            strOrder=" ORDER BY ITEM_ID ";
            
            data.Execute("DELETE FROM STOCK_LEDGER_TEMP",dbURL);
            
            strCondition="INSERT INTO STOCK_LEDGER_TEMP (ITEM_ID) ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_ID) FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND GRN_DATE>='"+pFromDate+"' AND GRN_DATE <='"+pToDate+"' AND B.ITEM_ID NOT LIKE 'RM%' ";
            strCondition=strCondition+" UNION ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_CODE) FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND A.ISSUE_DATE>='"+pFromDate+"' AND ISSUE_DATE <='"+pToDate+"' AND B.ITEM_CODE NOT LIKE 'RM%' ";
            strCondition=strCondition+" UNION ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_ID) FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.STM_NO=B.STM_NO AND A.STM_DATE>='"+pFromDate+"' AND STM_DATE <='"+pToDate+"' AND B.ITEM_ID NOT LIKE 'RM%' ";
            
            
            data.Execute(strCondition,dbURL);
            
            strCondition="";
            
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pFromDate+"' ORDER BY ENTRY_DATE DESC",dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
                strFromDate=EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE"));
            }
            //================================================================//
            
            
            
            //stClosingHeader=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsClosingHeader=stClosingHeader.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_HEADER LIMIT 1");
            
            stClosingDetail=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsClosingDetail=stClosingDetail.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_DETAIL LIMIT 1");
            
            
            long NewEntryNo=5; //data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_OPENING_STOCK_HEADER", "ENTRY_NO");
            
            
            /*rsClosingHeader.moveToInsertRow();
            rsClosingHeader.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            rsClosingHeader.updateLong("ENTRY_NO",NewEntryNo);
            rsClosingHeader.updateString("ENTRY_DATE", EITLERPGLOBAL.addDaysToDate(pToDate,1,"yyyy-MM-dd"));
            rsClosingHeader.updateInt("STATUS",1);
            int Year = EITLERPGLOBAL.getYear(pToDate);
            rsClosingHeader.updateString("REMARKS","Opening Stock as on 1 Apr. " + Year);
            rsClosingHeader.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID);
            rsClosingHeader.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            rsClosingHeader.updateString("MODIFIED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.updateBoolean("CHANGED", true);
            rsClosingHeader.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsClosingHeader.insertRow();*/
            
            
            
            //===================================================================//
            //====================== PROCESS WEIGHTED AVERAGE   =================//
            //===================================================================//
            
            strSQL="SELECT DISTINCT(A.ITEM_ID),A.ITEM_DESCRIPTION,A.UNIT FROM D_INV_ITEM_MASTER A,STOCK_LEDGER_TEMP B WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MAINTAIN_STOCK=1 AND A.APPROVED=1 AND A.CANCELLED=0  AND A.ITEM_ID=B.ITEM_ID "+strCondition+" AND A.ITEM_ID NOT LIKE 'RM%' GROUP BY A.ITEM_ID";
            rsItem=data.getResult(strSQL,dbURL);
            rsItem.first();
            
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                
                OpeningQty=0;
                OpeningRate=0;
                OpeningValue=0;
                
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_QTY"),3);
                    OpeningRate=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_RATE"),3);
                    OpeningValue=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_VALUE"),3);
                    
                    //if(OpeningQty==0) {
                    if(OpeningQty<=0) { // change as on 10/06/2009 -- Mrugesh
                        OpeningRate=0;
                        OpeningValue=0; // change as on 10/06/2009 -- Mrugesh
                        OpeningQty=0;   // change as on 10/06/2009 -- Mrugesh
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                    }
                }
                
                strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pToDate+"'";
                strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                
                
                rsTmp=data.getResult(strSQL,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {
                        
                        String Operation=rsTmp.getString("OPERATION");
                        strDocNo=rsTmp.getString("DOC_NO");
                        strOrder=rsTmp.getString("TORDER");
                        SrNo=rsTmp.getInt("SR_NO");
                        
                        if(Operation.equals("+")) {
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                            OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                            
                            //if(OpeningQty==0) {
                            if(OpeningQty<=0) {
                                OpeningRate=0;
                                System.out.println("GRN  Item Id = "+ItemID+ " Running Opening Qty = "+OpeningQty+" OpeningValue = " + OpeningValue); // change as on 10/06/2009 -- Mrugesh
                                OpeningValue=0; // change as on 10/06/2009 -- Mrugesh
                                
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                        }
                        
                        
                        if(Operation.equals("-")) {
                            double currentIssueValue=0;
                            double currentIssueRate=0;
                            double currentIssueQty=0;
                            
                            //========NEW CODE=========//
                            currentIssueQty=rsTmp.getDouble("QTY");
                            
                            if(OpeningValue<0) {
                                OpeningRate=0;
                                OpeningValue=0;
                            }
                            else {
                                if(OpeningQty>0) {
                                    //OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                                    //OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                                    
                                    // change as on 10/06/2009 -- start -- Mrugesh
                                    if (currentIssueQty==OpeningQty) {
                                        currentIssueValue=EITLERPGLOBAL.round(OpeningValue,3);
                                    } else {
                                        currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                                    }
                                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    //OpeningValue=EITLERPGLOBAL.round(OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3),3);
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue - currentIssueValue,3);
                                    // change as on 10/06/2009 -- end -- Mrugesh
                                    
                                }
                                else {
                                    OpeningRate=0;
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                                }
                            }
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                            currentIssueRate=EITLERPGLOBAL.round(OpeningRate,3);
                            currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                            //=====END OF NEW CODE=====//
                            
                            if(strOrder.equals("2")) {
                                data.Execute("UPDATE D_INV_ISSUE_DETAIL SET ISSUE_VALUE="+currentIssueValue+",RATE="+currentIssueRate+" WHERE ISSUE_NO='"+strDocNo+"' AND SR_NO="+SrNo,dbURL);
                            }
                            
                            if(strOrder.equals("3")) {
                                data.Execute("UPDATE D_INV_STM_DETAIL SET NET_AMOUNT="+currentIssueValue+",RATE="+currentIssueRate+" WHERE STM_NO='"+strDocNo+"' AND SR_NO="+SrNo,dbURL);
                            }
                            
                            if(OpeningQty!=0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                            else {
                                System.out.println("Issue  Item Id = "+ItemID+ " Running Opening Qty = "+OpeningQty+" OpeningValue = " + OpeningValue);
                                OpeningRate=0;
                                OpeningValue=0; // change as on 10/06/2009 -- Mrugesh
                            }
                        }
                        
                        rsTmp.next();
                    }
                }
                
                rsItem.next();
            }
            //===========================END WEIGHTED AVG. PROCESS ================================//
            
            
            
            
            //==========Get the Data from Item Master for ===============//
            boolean Continue = false;
            
            strSQL="SELECT ITEM_ID,ITEM_DESCRIPTION,UNIT,LOCATION_ID FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAINTAIN_STOCK=1 AND APPROVED=1 AND CANCELLED=0 "+strCondition+" AND ITEM_ID NOT LIKE 'RM%' GROUP BY ITEM_ID ORDER BY ITEM_ID ";//+strOrder;
            rsItem=data.getResult(strSQL,dbURL);
            rsItem.first();
            
            ItemCount=0;
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                
                ItemCount++;
                
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                
                OpeningQty=0;
                OpeningValue=0;
                
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=rsTmp.getDouble("OPENING_QTY");
                    OpeningValue=rsTmp.getDouble("OPENING_VALUE");
                    
                    //if(OpeningQty==0) {
                    if(OpeningQty<=0) { // change as on 10/06/2009 - Mrugesh
                        OpeningValue=0;
                        OpeningRate=0;
                        OpeningQty=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                    }
                }
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),3);
                }
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_CODE='"+ItemID+"'";
                rsTmp=data.getResult(strSQL,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
                }
                
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
                }
                
                //if(OpeningQty==0) {
                if(OpeningQty<=0) {
                    OpeningRate=0;
                    OpeningValue=0; //change as on 10/06/2008 -- Mrugesh
                    OpeningQty=0; //change as on 10/06/2008  -- Mrugesh
                }
                else {
                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                }
                
                TransactionsFound=false;
                
                strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pToDate+"'";
                strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                
                rsTmp=data.getResult(strSQL,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    TransactionsFound=true;
                    while(!rsTmp.isAfterLast()) {
                        
                        String Operation=rsTmp.getString("OPERATION");
                        strOrder=rsTmp.getString("TORDER");
                        strDocNo=rsTmp.getString("DOC_NO");
                        strDocNo=strDocNo+EITLERPGLOBAL.Replicate(" ", 12-strDocNo.length());
                        strDocDate=EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE"));
                        
                        if(Operation.equals("+")) {
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                            OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                            
                            if(OpeningQty==0) {
                                OpeningRate=0;
                                OpeningValue=0; //change as on 10/06/2009
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                        }
                        
                        
                        if(Operation.equals("-")) {
                            double currentIssueValue=0;
                            double currentIssueRate=0;
                            double currentIssueQty=0;
                            
                            //========NEW CODE=========//
                            currentIssueQty=rsTmp.getDouble("QTY");
                            
                            if(OpeningValue<0) {
                                OpeningRate=0;
                                OpeningValue=0; //change on 10/06/2009 -- Mrugesh
                            }
                            else {
                                if(OpeningQty>0) {
                                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue- EITLERPGLOBAL.round(rsTmp.getDouble("VALUE"),3),3); //change as on 10/06/2009
                                    //OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                                }
                                else {
                                    OpeningRate=0;
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                                }
                            }
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                            
                            currentIssueRate=OpeningRate;
                            //currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                            currentIssueValue=EITLERPGLOBAL.round(rsTmp.getDouble("VALUE"),3); // change as on 10/06/2009 -- Mrugesh
                            //=====END OF NEW CODE=====//
                            
                            
                            if(OpeningQty!=0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                            else {
                                OpeningRate=0;
                                OpeningValue=0; // change as on 10/06/2009 - Mrugesh
                            }
                        }
                        
                        rsTmp.next();
                    }
                }
                
                if(OpeningQty==0) {
                    OpeningValue=0;
                }
                
                //UPDATE CLOSING QTY AND VALUE HERE
                String WareHouseID=clsItem.getItemWareHouseID(CompanyID, ItemID);
                String LocationID=clsItem.getItemLocationID(CompanyID, ItemID);
                String BOENo="X";
                String LOTNo="X";
                
                SrNo = (data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_COM_OPENING_STOCK_DETAIL WHERE ENTRY_NO=5", dbURL) + 1);
                
                rsClosingDetail.moveToInsertRow();
                rsClosingDetail.updateInt("COMPANY_ID",CompanyID);
                rsClosingDetail.updateLong("ENTRY_NO",5);
                rsClosingDetail.updateString("ENTRY_DATE","2009-10-01");
                rsClosingDetail.updateLong("SR_NO",SrNo);
                rsClosingDetail.updateString("ITEM_ID",ItemID);
                rsClosingDetail.updateString("WAREHOUSE_ID",WareHouseID);
                rsClosingDetail.updateString("LOCATION_ID",LocationID);
                rsClosingDetail.updateString("BOE_NO",BOENo);
                rsClosingDetail.updateString("LOT_NO",LOTNo);
                rsClosingDetail.updateDouble("OPENING_QTY",OpeningQty);
                rsClosingDetail.updateDouble("OPENING_RATE",OpeningRate);
                rsClosingDetail.updateDouble("OPENING_VALUE",OpeningValue);
                rsClosingDetail.updateDouble("ZERO_VAL_OPENING_QTY",0);
                rsClosingDetail.updateDouble("CREATED_BY",EITLERPGLOBAL.gUserID);
                rsClosingDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsClosingDetail.updateDouble("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                rsClosingDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsClosingDetail.insertRow();
                
                rsItem.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**Done with STM**/
    public clsStockInfo getOnHandQtyOn(int pCompanyID,String pItemID,String pDate) {
        
        clsStockInfo objStock=new clsStockInfo();
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        ResultSet rsClosingHeader,rsClosingDetail;
        ResultSet rsItemGroup;
        Statement stClosingHeader,stClosingDetail;
        
        String strSQL="";
        String strCondition="";
        String strOrder="";
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        String strLine="",ItemID=pItemID,ItemName="",UnitName="";
        int ItemCount=0;
        double OpeningQty=0,OpeningRate=0,OpeningValue=0,InwardQty=0;
        double InwardZeroQty=0,InwardRate=0,InwardValue=0,OutwardQty=0;
        double OutwardZeroQty=0,OutwardRate=0,OutwardValue=0,ClosingQty=0;
        double ClosingZeroQty=0,ClosingRate=0,ClosingValue=0,IssueQty=0;
        double IssueZeroQty=0,IssueValue=0,IssueRate=0;
        boolean TransactionsFound=true,Done=false;
        
        String strDocNo="";
        String strDocDate="";
        
        long StockEntryNo=0,SrNo=0;
        String StockEntryDate="",strFromDate="";
        
        
        try {
            
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
                strFromDate=EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE"));
            }
            //================================================================//
            
            
            
            //==========Get the Data from Item Master for ===============//
            
            ItemID=ItemID.trim()+EITLERPGLOBAL.Replicate(" ", 12-ItemID.trim().length());
            
            OpeningQty=0;
            OpeningValue=0;
            
            strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    OpeningQty += rsTmp.getDouble("OPENING_QTY");
                    OpeningValue += rsTmp.getDouble("OPENING_VALUE");
                    
                    if(OpeningQty==0) {
                        OpeningValue=0;
                        OpeningRate=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                    }
                    
                    rsTmp.next();
                }
                
            }
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),3);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),3);
            }
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_CODE='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
            }
            
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
            }
            
            if(OpeningQty==0) {
                OpeningRate=0;
            }
            else {
                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
            }
            
            TransactionsFound=false;
            
            strSQL="SELECT B.SR_NO, A.GRN_NO AS DOC_NO, A.GRN_DATE AS DOC_DATE,'+' AS OPERATION, '1' AS TORDER, B.QTY, B.LANDED_RATE*B.QTY AS VALUE, 0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pDate+"' ";
            strSQL=strSQL+"UNION ";
            strSQL=strSQL+"SELECT B.SR_NO, A.ISSUE_NO AS DOC_NO, A.ISSUE_DATE AS DOC_DATE, '-' AS OPERATION, '2' AS TORDER, B.QTY, B.ISSUE_VALUE AS VALUE, B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pDate+"' ";
            strSQL=strSQL+"UNION ";
            strSQL=strSQL+"SELECT B.SR_NO, A.STM_NO AS DOC_NO, A.STM_DATE AS DOC_DATE, '-' AS OPERATION, '3' AS TORDER, B.QTY, B.NET_AMOUNT AS VALUE, B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A, D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pDate+"' ";
            strSQL=strSQL+"ORDER BY DOC_DATE,TORDER,DOC_NO ";
            
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TransactionsFound=true;
                while(!rsTmp.isAfterLast()) {
                    
                    String Operation=rsTmp.getString("OPERATION");
                    strOrder=rsTmp.getString("TORDER");
                    
                    strDocNo=rsTmp.getString("DOC_NO");
                    strDocNo=strDocNo+EITLERPGLOBAL.Replicate(" ", 12-strDocNo.length());
                    strDocDate=EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE"));
                    
                    if(Operation.equals("+")) {
                        
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                        OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                        
                        if(OpeningQty==0) {
                            OpeningRate=0;
                        }
                        else {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                        }
                    }
                    
                    
                    if(Operation.equals("-")) {
                        double currentIssueValue=0;
                        double currentIssueRate=0;
                        double currentIssueQty=0;
                        
                        //========NEW CODE=========//
                        currentIssueQty=rsTmp.getDouble("QTY");
                        
                        if(OpeningValue<0) {
                            OpeningRate=0;
                            OpeningValue=0;
                        }
                        else {
                            if(OpeningQty>0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                            }
                            else {
                                OpeningRate=0;
                                OpeningValue=OpeningValue-0;
                            }
                        }
                        
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                        
                        currentIssueRate=OpeningRate;
                        currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                        //=====END OF NEW CODE=====//
                        
                        
                        if(OpeningQty!=0) {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                        }
                        else {
                            OpeningRate=0;
                        }
                    }
                    
                    rsTmp.next();
                }
            }
            
            if(OpeningQty==0) {
                OpeningValue=0;
            }
            
            objStock.StockQty=EITLERPGLOBAL.round(OpeningQty,3);
            objStock.StockValue=EITLERPGLOBAL.round(OpeningValue,3);
            
            return objStock;
        }
        catch(Exception e) {
            e.printStackTrace();
            return new clsStockInfo();
        }
        
    }
    
    public static clsStockInfo getOnHandQtyOnEx1(int pCompanyID,String pItemID,String pDate) {
        
        clsStockInfo objStock=new clsStockInfo();
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        ResultSet rsClosingHeader,rsClosingDetail;
        ResultSet rsItemGroup;
        Statement stClosingHeader,stClosingDetail;
        
        String strSQL="";
        String strCondition="";
        String strOrder="";
        String dbURL = "";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        String strLine="",ItemID=pItemID,ItemName="",UnitName="";
        int ItemCount=0;
        double OpeningQty=0,OpeningRate=0,OpeningValue=0,InwardQty=0;
        double InwardZeroQty=0,InwardRate=0,InwardValue=0,OutwardQty=0;
        double OutwardZeroQty=0,OutwardRate=0,OutwardValue=0,ClosingQty=0;
        double ClosingZeroQty=0,ClosingRate=0,ClosingValue=0,IssueQty=0;
        double IssueZeroQty=0,IssueValue=0,IssueRate=0;
        boolean TransactionsFound=true,Done=false;
        
        String strDocNo="";
        String strDocDate="";
        
        long StockEntryNo=0,SrNo=0;
        String StockEntryDate="",strFromDate="";
        
        
        try {
            
            dbURL = clsFinYear.getDBURL(pCompanyID,Integer.parseInt(pDate.substring(0,4).trim()));
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pDate+"' ORDER BY ENTRY_DATE DESC",dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
                strFromDate=EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE"));
            }
            //================================================================//
            
            
            
            //==========Get the Data from Item Master for ===============//
            
            ItemID=ItemID.trim()+EITLERPGLOBAL.Replicate(" ", 12-ItemID.trim().length());
            
            OpeningQty=0;
            OpeningValue=0;
            
            strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL,dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=rsTmp.getDouble("OPENING_QTY");
                OpeningValue=rsTmp.getDouble("OPENING_VALUE");
                
                if(OpeningQty==0) {
                    OpeningValue=0;
                    OpeningRate=0;
                }
                else {
                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                }
            }
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL,dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),2);
            }
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_CODE='"+ItemID+"'";
            rsTmp=data.getResult(strSQL,dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL,dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            if(OpeningQty==0) {
                OpeningRate=0;
            }
            else {
                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
            }
            
            TransactionsFound=false;
            
            strSQL="SELECT B.SR_NO, A.GRN_NO AS DOC_NO, A.GRN_DATE AS DOC_DATE,'+' AS OPERATION, '1' AS TORDER, B.QTY, B.LANDED_RATE*B.QTY AS VALUE, 0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pDate+"' ";
            strSQL=strSQL+"UNION ";
            strSQL=strSQL+"SELECT B.SR_NO, A.ISSUE_NO AS DOC_NO, A.ISSUE_DATE AS DOC_DATE, '-' AS OPERATION, '2' AS TORDER, B.QTY, B.ISSUE_VALUE AS VALUE, B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pDate+"' ";
            strSQL=strSQL+"UNION ";
            strSQL=strSQL+"SELECT B.SR_NO, A.STM_NO AS DOC_NO, A.STM_DATE AS DOC_DATE, '-' AS OPERATION, '3' AS TORDER, B.QTY, B.NET_AMOUNT AS VALUE, B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A, D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pDate+"' ";
            strSQL=strSQL+"ORDER BY DOC_DATE,TORDER,DOC_NO ";
            
            rsTmp=data.getResult(strSQL,dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TransactionsFound=true;
                while(!rsTmp.isAfterLast()) {
                    
                    String Operation=rsTmp.getString("OPERATION");
                    strOrder=rsTmp.getString("TORDER");
                    
                    strDocNo=rsTmp.getString("DOC_NO");
                    strDocNo=strDocNo+EITLERPGLOBAL.Replicate(" ", 12-strDocNo.length());
                    strDocDate=EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE"));
                    
                    if(Operation.equals("+")) {
                        
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),2);
                        OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),2);
                        
                        if(OpeningQty==0) {
                            OpeningRate=0;
                        }
                        else {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                        }
                    }
                    
                    
                    if(Operation.equals("-")) {
                        double currentIssueValue=0;
                        double currentIssueRate=0;
                        double currentIssueQty=0;
                        
                        //========NEW CODE=========//
                        currentIssueQty=rsTmp.getDouble("QTY");
                        
                        if(OpeningValue<0) {
                            OpeningRate=0;
                            OpeningValue=0;
                        }
                        else {
                            if(OpeningQty>0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                                OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                            }
                            else {
                                OpeningRate=0;
                                OpeningValue=OpeningValue-0;
                            }
                        }
                        
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,2);
                        
                        currentIssueRate=OpeningRate;
                        currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                        //=====END OF NEW CODE=====//
                        
                        
                        if(OpeningQty!=0) {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                        }
                        else {
                            OpeningRate=0;
                        }
                    }
                    
                    rsTmp.next();
                }
            }
            
            if(OpeningQty==0) {
                OpeningValue=0;
            }
            
            objStock.StockQty=EITLERPGLOBAL.round(OpeningQty,2);
            objStock.StockValue=EITLERPGLOBAL.round(OpeningValue,2);
            
            return objStock;
        }
        catch(Exception e) {
            e.printStackTrace();
            return new clsStockInfo();
        }
    }
    
    /**Done with STM. STM = N/A **/
    public double getOnHandQtyOnEx(int pCompanyID,String pItemID,String pDate) {
        
        double OpeningQty=0;
        
        try {
            
            //======= Find the last cut-off date stock entry =================//
            long StockEntryNo=0;
            String StockEntryDate="";
            String ToDate=EITLERPGLOBAL.addDaysToDate(pDate, -1, "yyyy-MM-dd");
            
            ToDate=pDate;
            
            ResultSet rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
            }
            
            String ItemID=pItemID;
            ItemID=ItemID.trim()+EITLERPGLOBAL.Replicate(" ", 12-ItemID.trim().length());
            
            String strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningQty=rsTmp.getDouble("OPENING_QTY");
            }
            
            OpeningQty=OpeningQty+getMIRInwardQtyWSTM(ItemID, StockEntryDate, ToDate);
            OpeningQty=OpeningQty-getIssueReqQty(ItemID, StockEntryDate, ToDate);
            //================================================================//
        }
        catch(Exception e) {
            
        }
        
        return OpeningQty;
    }
    
    /**Unapproved Issue Document //add on 28/08/2009**/
    public double getOnHandQtyOn_Hold(int pCompanyID,String pItemID,String IssueNo) {
        double stock = 0;
        String str = "SELECT B.QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B "+
        " WHERE A.COMPANY_ID="+pCompanyID+" AND A.APPROVED=0 AND A.CANCELED=0 "+
        " AND A.COMPANY_ID=A.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO "+
        " AND A.ISSUE_NO<>'"+IssueNo+"' AND B.ITEM_CODE='"+pItemID+"' ";
        stock = data.getDoubleValueFromDB(str);
        return stock;
    }
    
    
    /**Done with STM**/
    public double getOnHandValueOn(int pCompanyID,String pItemID,String pDate) {
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        ResultSet rsClosingHeader,rsClosingDetail;
        ResultSet rsItemGroup;
        Statement stClosingHeader,stClosingDetail;
        
        String strSQL="";
        String strCondition="";
        String strOrder="";
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        String strLine="",ItemID=pItemID,ItemName="",UnitName="";
        int ItemCount=0;
        double OpeningQty=0,OpeningRate=0,OpeningValue=0,InwardQty=0;
        double InwardZeroQty=0,InwardRate=0,InwardValue=0,OutwardQty=0;
        double OutwardZeroQty=0,OutwardRate=0,OutwardValue=0,ClosingQty=0;
        double ClosingZeroQty=0,ClosingRate=0,ClosingValue=0,IssueQty=0;
        double IssueZeroQty=0,IssueValue=0,IssueRate=0;
        boolean TransactionsFound=true,Done=false;
        
        String strDocNo="";
        String strDocDate="";
        
        long StockEntryNo=0,SrNo=0;
        String StockEntryDate="",strFromDate="";
        
        
        try {
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
                strFromDate=EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE"));
            }
            //================================================================//
            
            
            //==========Get the Data from Item Master for ===============//
            ItemID=ItemID.trim()+EITLERPGLOBAL.Replicate(" ", 12-ItemID.trim().length());
            
            OpeningQty=0;
            OpeningValue=0;
            
            strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            //strSQL="SELECT IF(SUM(OPENING_QTY) IS NULL,0,SUM(OPENING_QTY)) AS OPENING_QTY, "+
            //" IF(SUM(OPENING_VALUE)IS NULL,0,SUM(OPENING_VALUE)) AS OPENING_VALUE  FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    OpeningQty += rsTmp.getDouble("OPENING_QTY");
                    OpeningValue += rsTmp.getDouble("OPENING_VALUE");
                    
                    if(OpeningQty==0) {
                        OpeningValue=0;
                        OpeningRate=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                    }
                    
                    rsTmp.next();
                }
                
            }
            
            
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),2);
            }
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_CODE='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }
            
            
            if(OpeningQty==0) {
                OpeningRate=0;
            }
            else {
                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
            }
            
            TransactionsFound=false;
            
            strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pDate+"'";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pDate+"'";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pDate+"'";
            strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
            
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TransactionsFound=true;
                while(!rsTmp.isAfterLast()) {
                    
                    String Operation=rsTmp.getString("OPERATION");
                    strOrder=rsTmp.getString("TORDER");
                    
                    strDocNo=rsTmp.getString("DOC_NO");
                    strDocNo=strDocNo+EITLERPGLOBAL.Replicate(" ", 12-strDocNo.length());
                    strDocDate=EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE"));
                    
                    if(Operation.equals("+")) {
                        
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),2);
                        OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),2);
                        
                        if(OpeningQty==0) {
                            OpeningRate=0;
                        }
                        else {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                        }
                    }
                    
                    
                    if(Operation.equals("-")) {
                        double currentIssueValue=0;
                        double currentIssueRate=0;
                        double currentIssueQty=0;
                        
                        //========NEW CODE=========//
                        currentIssueQty=rsTmp.getDouble("QTY");
                        
                        if(OpeningValue<0) {
                            OpeningRate=0;
                            OpeningValue=0;
                        }
                        else {
                            if(OpeningQty>0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                                OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                            }
                            else {
                                OpeningRate=0;
                                OpeningValue=OpeningValue-0;
                            }
                        }
                        
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,2);
                        
                        currentIssueRate=OpeningRate;
                        currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                        //=====END OF NEW CODE=====//
                        
                        
                        if(OpeningQty!=0) {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                        }
                        else {
                            OpeningRate=0;
                        }
                    }
                    
                    rsTmp.next();
                }
            }
            
            if(OpeningQty==0) {
                OpeningValue=0;
            }
            
            return OpeningValue;
        }
        catch(Exception e) {
            return 0;
        }
        
    }
    
    
    public double getOnHandValueOnRaw(int pCompanyID,String pItemID,String pDate) {
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        ResultSet rsClosingHeader,rsClosingDetail;
        ResultSet rsItemGroup;
        Statement stClosingHeader,stClosingDetail;
        
        String strSQL="";
        String strCondition="";
        String strOrder="";
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        String strLine="",ItemID=pItemID,ItemName="",UnitName="";
        int ItemCount=0;
        double OpeningQty=0,OpeningRate=0,OpeningValue=0,InwardQty=0;
        double InwardZeroQty=0,InwardRate=0,InwardValue=0,OutwardQty=0;
        double OutwardZeroQty=0,OutwardRate=0,OutwardValue=0,ClosingQty=0;
        double ClosingZeroQty=0,ClosingRate=0,ClosingValue=0,IssueQty=0;
        double IssueZeroQty=0,IssueValue=0,IssueRate=0;
        boolean TransactionsFound=true,Done=false;
        
        String strDocNo="";
        String strDocDate="";
        
        long StockEntryNo=0,SrNo=0;
        String StockEntryDate="",strFromDate="";
        
        
        try {
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
                strFromDate=EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE"));
            }
            //================================================================//
            
            
            //==========Get the Data from Item Master for ===============//
            ItemID=ItemID.trim()+EITLERPGLOBAL.Replicate(" ", 12-ItemID.trim().length());
            
            OpeningQty=0;
            OpeningValue=0;
            
            strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            //strSQL="SELECT IF(SUM(OPENING_QTY) IS NULL,0,SUM(OPENING_QTY)) AS OPENING_QTY, "+
            //" IF(SUM(OPENING_VALUE)IS NULL,0,SUM(OPENING_VALUE)) AS OPENING_VALUE  FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    
                    OpeningQty += rsTmp.getDouble("OPENING_QTY");
                    OpeningValue += rsTmp.getDouble("OPENING_VALUE");
                    
                    if(OpeningQty==0) {
                        OpeningValue=0;
                        OpeningRate=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                    }
                    
                    rsTmp.next();
                }
                
            }
            
            
            
            /*strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
             
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),2);
            }*/
            
            /*strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_CODE='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
             
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }*/
            
            
            /*strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
             
            if(rsTmp.getRow()>0) {
                OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),2);
                OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),2);
            }*/
            
            
            if(OpeningQty==0) {
                OpeningRate=0;
            }
            else {
                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
            }
            
            TransactionsFound=false;
            
            strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pDate+"'";
            //strSQL=strSQL+" UNION ";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TransactionsFound=true;
                while(!rsTmp.isAfterLast()) {
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),2);
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),2);
                    rsTmp.next();
                }
            }
            
            strSQL="SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pDate+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TransactionsFound=true;
                while(!rsTmp.isAfterLast()) {
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("QTY"),2);
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("VALUE"),2);
                    rsTmp.next();
                }
            }
            
            strSQL="SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pDate+"'";
            
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TransactionsFound=true;
                while(!rsTmp.isAfterLast()) {
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("QTY"),2);
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("VALUE"),2);
                    rsTmp.next();
                }
            }
            
            /*rsTmp=data.getResult(strSQL);
            rsTmp.first();*/
            
            /*if(rsTmp.getRow()>0) {
                TransactionsFound=true;
                while(!rsTmp.isAfterLast()) {
             
                    String Operation=rsTmp.getString("OPERATION");
                    strOrder=rsTmp.getString("TORDER");
             
                    strDocNo=rsTmp.getString("DOC_NO");
                    strDocNo=strDocNo+EITLERPGLOBAL.Replicate(" ", 12-strDocNo.length());
                    strDocDate=EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE"));
             
                    if(Operation.equals("+")) {
             
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),2);
                        OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),2);
             
                        if(OpeningQty==0) {
                            OpeningRate=0;
                        }
                        else {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                        }
                    }
             
             
                    if(Operation.equals("-")) {
                        double currentIssueValue=0;
                        double currentIssueRate=0;
                        double currentIssueQty=0;
             
                        //========NEW CODE=========//
                        currentIssueQty=rsTmp.getDouble("QTY");
             
                        if(OpeningValue<0) {
                            OpeningRate=0;
                            OpeningValue=0;
                        }
                        else {
                            if(OpeningQty>0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                                OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                            }
                            else {
                                OpeningRate=0;
                                OpeningValue=OpeningValue-0;
                            }
                        }
             
                        OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,2);
             
                        currentIssueRate=OpeningRate;
                        currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),2);
                        //=====END OF NEW CODE=====//
             
             
                        if(OpeningQty!=0) {
                            OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,2);
                        }
                        else {
                            OpeningRate=0;
                        }
                    }
             
                    rsTmp.next();
                }
            }*/
            
            if(OpeningQty==0) {
                OpeningValue=0;
            }
            
            return OpeningValue;
        }
        catch(Exception e) {
            return 0;
        }
        
    }
    
    /**     MIR Accepted qty without STM <BR>
     *      --> Supplier code 888888 & 999999 are not included.<BR>
     *      --> Only Accepted qty
     **/
    public double getMIRInwardQtyWOSTM(String ItemID,String FromDate,String ToDate) {
        
        double MIRInwardQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.QTY) IS NULL,0,SUM(B.QTY)) AS QTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.MIR_NO=B.MIR_NO AND A.SUPP_ID NOT IN ('888888','999999') AND A.MIR_TYPE=B.MIR_TYPE AND  A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.MIR_DATE>='"+FromDate+"' AND A.MIR_DATE<='"+ToDate+"'";
            
            System.out.println(strSQL);
            
            MIRInwardQty=data.getDoubleValueFromDB(strSQL);
            
        }
        catch(Exception e){
        }
        
        return MIRInwardQty;
        
    }
    
    /**     MIR Accepted qty with STM <BR>
     *      --> Supplier code 888888 & 999999 are included. <BR>
     *      --> Only Accepted qty
     **/
    public double getMIRInwardQtyWSTM(String ItemID,String FromDate,String ToDate) {
        
        double MIRInwardQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.QTY) IS NULL,0,SUM(B.QTY)) AS QTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.MIR_NO=B.MIR_NO AND A.MIR_TYPE=B.MIR_TYPE AND  A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.MIR_DATE>='"+FromDate+"' AND A.MIR_DATE<='"+ToDate+"'";
            
            System.out.println(strSQL);
            
            MIRInwardQty=data.getDoubleValueFromDB(strSQL);
            
        }
        catch(Exception e){
        }
        return MIRInwardQty;
    }
    
    /**     MIR Accepted qty with only STM <BR>
     *      --> only Supplier code 888888 & 999999 are considered. <BR>
     *      --> Only Accepted qty
     **/
    public double getMIRInwardQtyOSTM(String ItemID,String FromDate,String ToDate) {
        
        double MIRInwardQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.QTY) IS NULL,0,SUM(B.QTY)) AS QTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.MIR_NO=B.MIR_NO AND A.SUPP_ID IN ('888888','999999') AND A.MIR_TYPE=B.MIR_TYPE AND  A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.MIR_DATE>='"+FromDate+"' AND A.MIR_DATE<='"+ToDate+"'";
            
            System.out.println(strSQL);
            
            MIRInwardQty=data.getDoubleValueFromDB(strSQL);
            
        }
        catch(Exception e){
        }
        
        return MIRInwardQty;
        
    }
    
    /**     MIR Accepted qty without STM <BR>
     *      --> Supplier code 888888 & 999999 are not included. <BR>
     *      --> Only Received qty
     **/
    public double getInspectionWOQty(String ItemID,String FromDate,String ToDate) {
        
        double MIRInwardQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.RECEIVED_QTY) IS NULL,0,SUM(B.RECEIVED_QTY)) AS QTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.MIR_NO=B.MIR_NO AND A.SUPP_ID NOT IN ('888888','999999') AND A.MIR_TYPE=B.MIR_TYPE AND A.APPROVED=0 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.MIR_DATE>='"+FromDate+"' AND A.MIR_DATE<='"+ToDate+"'";
            
            MIRInwardQty=data.getDoubleValueFromDB(strSQL);
            
        }
        catch(Exception e){
        }
        
        return MIRInwardQty;
        
    }
    
    /**     MIR Accepted qty with STM <BR>
     *      --> Supplier code 888888 & 999999 are included. <BR>
     *      --> Only Received qty
     **/
    public double getInspectionWQty(String ItemID,String FromDate,String ToDate) {
        
        double MIRInwardQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.RECEIVED_QTY) IS NULL,0,SUM(B.RECEIVED_QTY)) AS QTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.MIR_NO=B.MIR_NO AND A.MIR_TYPE=B.MIR_TYPE AND A.APPROVED=0 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.MIR_DATE>='"+FromDate+"' AND A.MIR_DATE<='"+ToDate+"'";
            
            MIRInwardQty=data.getDoubleValueFromDB(strSQL);
            
        }
        catch(Exception e){
        }
        
        return MIRInwardQty;
        
    }
    
    /**     MIR Accepted qty with only STM <BR>
     *      --> only Supplier code 888888 & 999999 are considered. <BR>
     *      --> Only Received qty
     **/
    public double getInspectionOQty(String ItemID,String FromDate,String ToDate) {
        
        double MIRInwardQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.RECEIVED_QTY) IS NULL,0,SUM(B.RECEIVED_QTY)) AS QTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.MIR_NO=B.MIR_NO AND A.SUPP_ID IN ('888888','999999') AND A.MIR_TYPE=B.MIR_TYPE AND A.APPROVED=0 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.MIR_DATE>='"+FromDate+"' AND A.MIR_DATE<='"+ToDate+"'";
            
            MIRInwardQty=data.getDoubleValueFromDB(strSQL);
            
        }
        catch(Exception e){
        }
        
        return MIRInwardQty;
        
    }
    
    public double getMIRQtyForFortnight(String ItemID, String ToDate) {
        double MIRQty=0;
        //String FromDate = EITLERPGLOBAL.getFinYearStartDate(ToDate);
        String FromDate = "2008-04-01";
        String strSQL = "";
        try {
            strSQL="SELECT * FROM (SELECT A.MIR_NO,A.MIR_DATE,B.SR_NO,IF((B.RECEIVED_QTY-B.REJECTED_QTY) IS NULL,0,(B.RECEIVED_QTY-B.REJECTED_QTY)) AS QTY " +
            "FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.MIR_NO=B.MIR_NO " +
            "AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.MIR_DATE>='"+FromDate+"' " +
            "AND A.MIR_DATE<='"+ToDate+"') AS MYTABLE WHERE MYTABLE.QTY>0";
            
            
            ResultSet rsMIR = data.getResult(strSQL);
            rsMIR.first();
            if(rsMIR.getRow() > 0) {
                while(!rsMIR.isAfterLast()) {
                    String MIRNo = UtilFunctions.getString(rsMIR, "MIR_NO", "");
                    String MIRSrNo = UtilFunctions.getString(rsMIR, "SR_NO", "");
                    double Qty = UtilFunctions.getDouble(rsMIR, "QTY", 0);
                    strSQL = "SELECT A.GRN_NO FROM D_INV_GRN_HEADER A, D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND B.ITEM_ID='"+ItemID+"' " +
                    "AND B.MIR_NO='"+MIRNo+"' AND B.MIR_SR_NO='"+MIRSrNo+"' AND A.APPROVED=1 AND CANCELLED=0";
                    if(!data.IsRecordExist(strSQL)) {
                        MIRQty += Qty;
                    }
                    rsMIR.next();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        return MIRQty;
    }
    
    public double getGRNQtyForFortnight(String ItemID, String FromDate, String ToDate) {
        double GRNQty=0;
        String strSQL = "";
        try {
            strSQL="SELECT IF(SUM(B.QTY) IS NULL,0,SUM(B.QTY)) AS QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE " +
            "AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.APPROVED_DATE>='"+FromDate+"' " +
            "AND A.APPROVED_DATE<='"+ToDate+"'";
            GRNQty = data.getDoubleValueFromDB(strSQL);
        }
        catch(Exception e){
            return 0;
        }
        return GRNQty;
    }
    
    public double getIssueQtyForFortnight(String ItemID, String FromDate, String ToDate) {
        double IssueQty=0;
        String strSQL = "";
        try {
            strSQL="SELECT IF(SUM(B.QTY) IS NULL,0,SUM(B.QTY)) AS QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 " +
            "AND B.ITEM_CODE='"+ItemID+"' AND A.APPROVED_DATE>='"+FromDate+"' AND A.APPROVED_DATE<='"+ToDate+"'";
            IssueQty = data.getDoubleValueFromDB(strSQL);
        }
        catch(Exception e){
            return 0;
        }
        return IssueQty;
    }
    
    public double getSTMQtyForFortnight(String ItemID, String FromDate, String ToDate) {
        double STMQty=0;
        String strSQL = "";
        try {
            strSQL="SELECT IF(SUM(B.QTY) IS NULL,0,SUM(B.QTY)) AS QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 " +
            "AND B.ITEM_ID='"+ItemID+"' AND A.APPROVED_DATE>='"+FromDate+"' AND A.APPROVED_DATE<='"+ToDate+"'";
            STMQty = data.getDoubleValueFromDB(strSQL);
        }
        catch(Exception e){
            return 0;
        }
        return STMQty;
    }
    
    /**STM = N/A**/
    public double getIssueQty(String ItemID,String FromDate,String ToDate) {
        
        double IssueQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.QTY) IS NULL,0,SUM(B.QTY)) AS QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B " +
            "WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 " +
            "AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+FromDate+"' AND ISSUE_DATE<='"+ToDate+"'";
            IssueQty=data.getDoubleValueFromDB(strSQL);
            
            //Do not include STM in issued qty calculations, as its not an issue but transfer
            
        }
        catch(Exception e){
        }
        
        return IssueQty;
    }
    
    /**STM = N/A**/
    public double getIssueReqQty(String ItemID,String FromDate,String ToDate) {
        
        double IssueQty=0;
        
        try {
            
            String strSQL="SELECT IF(SUM(B.REQ_QTY) IS NULL,0,SUM(B.REQ_QTY)) AS QTY FROM D_INV_ISSUE_REQ_HEADER A,D_INV_ISSUE_REQ_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.REQ_NO=B.REQ_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.REQ_DATE>='"+FromDate+"' AND A.REQ_DATE<='"+ToDate+"'";
            
            System.out.println(strSQL);
            IssueQty=data.getDoubleValueFromDB(strSQL);
            
        }
        catch(Exception e){
        }
        
        return IssueQty;
    }
    
    /**STM = N/A**/
    public void CloseStockRemove() {
        
        
        String pFromDate="2008-04-01";
        String pToDate="2009-03-31";
        
        ResultSet rsTmp,rsItem,rsRcpt,rsIssue,rsSource;
        ResultSet rsClosingHeader,rsClosingDetail;
        ResultSet rsItemGroup;
        Statement stClosingHeader,stClosingDetail;
        
        String strSQL="";
        String strCondition="";
        String strOrder="";
        
        Connection tmpConn;
        tmpConn=data.getConn();
        
        String strLine="",ItemID="",ItemName="",UnitName="";
        int ItemCount=0;
        double OpeningQty=0,OpeningRate=0,OpeningValue=0,InwardQty=0;
        double InwardZeroQty=0,InwardRate=0,InwardValue=0,OutwardQty=0;
        double OutwardZeroQty=0,OutwardRate=0,OutwardValue=0,ClosingQty=0;
        double ClosingZeroQty=0,ClosingRate=0,ClosingValue=0,IssueQty=0;
        double IssueZeroQty=0,IssueValue=0,IssueRate=0;
        boolean TransactionsFound=true,Done=false;
        
        String strDocNo="";
        String strDocDate="";
        
        long StockEntryNo=0,SrNo=0;
        String StockEntryDate="",strFromDate="";
        
        
        try {
            
            strOrder=" ORDER BY ITEM_ID ";
            
            data.Execute("DELETE FROM STOCK_LEDGER_TEMP");
            
            strCondition="INSERT INTO STOCK_LEDGER_TEMP (ITEM_ID) ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_ID) FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND GRN_DATE>='"+pFromDate+"' AND GRN_DATE <='"+pToDate+"' AND B.ITEM_ID LIKE 'RM%' ";
            strCondition=strCondition+" UNION ";
            strCondition=strCondition+" SELECT DISTINCT(ITEM_CODE) FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.ISSUE_NO=B.ISSUE_NO AND A.ISSUE_DATE>='"+pFromDate+"' AND ISSUE_DATE <='"+pToDate+"' AND B.ITEM_CODE LIKE 'RM%' ";
            
            data.Execute(strCondition);
            
            strCondition=" AND A.ITEM_ID LIKE 'RM%' ";
            
            
            //======= Find the last cut-off date stock entry =================//
            rsTmp=data.getResult("SELECT ENTRY_NO,ENTRY_DATE FROM D_COM_OPENING_STOCK_HEADER WHERE ENTRY_DATE<='"+pFromDate+"' ORDER BY ENTRY_DATE DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                StockEntryNo=rsTmp.getLong("ENTRY_NO");
                StockEntryDate=rsTmp.getString("ENTRY_DATE");
                strFromDate=EITLERPGLOBAL.formatDate(rsTmp.getString("ENTRY_DATE"));
            }
            //================================================================//
            
            
            
            stClosingHeader=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsClosingHeader=stClosingHeader.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_HEADER LIMIT 1");
            
            stClosingDetail=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsClosingDetail=stClosingDetail.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_DETAIL LIMIT 1");
            
            
            long NewEntryNo=4;//data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_OPENING_STOCK_HEADER", "ENTRY_NO");
            
            
            //===================================================================//
            //====================== PROCESS WEIGHTED AVERAGE   =================//
            //===================================================================//
            
            strSQL="SELECT DISTINCT(A.ITEM_ID),A.ITEM_DESCRIPTION,A.UNIT FROM D_INV_ITEM_MASTER A,STOCK_LEDGER_TEMP B WHERE A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MAINTAIN_STOCK=1 AND A.APPROVED=1 AND A.CANCELLED=0  AND A.ITEM_ID=B.ITEM_ID "+strCondition+" GROUP BY A.ITEM_ID";
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                
                OpeningQty=0;
                OpeningRate=0;
                OpeningValue=0;
                
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_QTY"),3);
                    OpeningRate=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_RATE"),3);
                    OpeningValue=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_VALUE"),3);
                    
                    if(OpeningQty==0) {
                        OpeningRate=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                    }
                }
                
                strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY AS ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pToDate+"'";
                strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                
                
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {
                        
                        String Operation=rsTmp.getString("OPERATION");
                        strOrder=rsTmp.getString("TORDER");
                        strDocNo=rsTmp.getString("DOC_NO");
                        SrNo=rsTmp.getInt("SR_NO");
                        
                        if(Operation.equals("+")) {
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                            OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                            
                            if(OpeningQty==0) {
                                OpeningRate=0;
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                        }
                        
                        
                        if(Operation.equals("-")) {
                            double currentIssueValue=0;
                            double currentIssueRate=0;
                            double currentIssueQty=0;
                            
                            //========NEW CODE=========//
                            currentIssueQty=rsTmp.getDouble("QTY");
                            
                            if(OpeningValue<0) {
                                OpeningRate=0;
                                OpeningValue=0;
                            }
                            else {
                                if(OpeningQty>0) {
                                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                                }
                                else {
                                    OpeningRate=0;
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                                }
                            }
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                            currentIssueRate=OpeningRate;
                            currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                            //=====END OF NEW CODE=====//
                            
                            if(strOrder.equals("2")) {
                                data.Execute("UPDATE D_INV_ISSUE_DETAIL SET ISSUE_VALUE="+currentIssueValue+",RATE="+currentIssueRate+" WHERE ISSUE_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                            }
                            
                            if(strOrder.equals("3")) {
                                data.Execute("UPDATE D_INV_STM_DETAIL SET NET_AMOUNT="+currentIssueValue+",RATE="+currentIssueRate+" WHERE STM_NO='"+strDocNo+"' AND SR_NO="+SrNo);
                            }
                            
                            if(OpeningQty!=0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                            else {
                                OpeningRate=0;
                            }
                        }
                        
                        rsTmp.next();
                    }
                }
                
                rsItem.next();
            }
            //===========================END WEIGHTED AVG. PROCESS ================================//
            
            
            
            
            //==========Get the Data from Item Master for ===============//
            boolean Continue = false;
            
            strSQL="SELECT ITEM_ID,ITEM_DESCRIPTION,UNIT,LOCATION_ID FROM D_INV_ITEM_MASTER A WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MAINTAIN_STOCK=1 AND APPROVED=1 AND CANCELLED=0 "+strCondition+"  ORDER BY ITEM_ID "; //+strOrder
            rsItem=data.getResult(strSQL);
            rsItem.first();
            
            ItemCount=0;
            while((!rsItem.isAfterLast())&&rsItem.getRow()>0) {
                
                ItemCount++;
                
                ItemID=rsItem.getString("ITEM_ID").trim()+EITLERPGLOBAL.Replicate(" ", 12-rsItem.getString("ITEM_ID").trim().length());
                
                OpeningQty=0;
                OpeningValue=0;
                
                strSQL="SELECT * FROM D_COM_OPENING_STOCK_DETAIL WHERE ITEM_ID='"+ItemID+"' AND ENTRY_NO="+StockEntryNo;
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningQty=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_QTY"),3);
                    OpeningValue=EITLERPGLOBAL.round(rsTmp.getDouble("OPENING_VALUE"),3);
                    
                    if(OpeningQty==0) {
                        OpeningValue=0;
                        OpeningRate=0;
                    }
                    else {
                        OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                    }
                }
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY, IF(SUM(LANDED_RATE*QTY) IS NULL,0,SUM(LANDED_RATE*QTY)) AS SUM_VALUE FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND A.GRN_DATE>'"+StockEntryDate+"' AND GRN_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("SUM_QTY"),3);
                }
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(ISSUE_VALUE) IS NULL,0,SUM(ISSUE_VALUE)) AS SUM_VALUE FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND A.ISSUE_DATE>'"+StockEntryDate+"' AND ISSUE_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_CODE='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
                }
                
                
                strSQL="SELECT IF(SUM(QTY) IS NULL,0,SUM(QTY)) AS SUM_QTY,IF(SUM(ZERO_VAL_QTY) IS NULL,0,SUM(ZERO_VAL_QTY)) AS SUM_ZERO_QTY, IF(SUM(NET_AMOUNT) IS NULL,0,SUM(NET_AMOUNT)) AS SUM_VALUE FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND A.STM_DATE>'"+StockEntryDate+"' AND STM_DATE<'"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND B.ITEM_ID='"+ItemID+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-rsTmp.getDouble("SUM_VALUE"),3);
                    OpeningQty=EITLERPGLOBAL.round(OpeningQty-rsTmp.getDouble("SUM_QTY"),3);
                }
                
                
                if(OpeningQty==0) {
                    OpeningValue=0;
                    OpeningRate=0;
                }
                else {
                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                }
                
                TransactionsFound=false;
                
                strSQL="SELECT B.SR_NO,A.GRN_NO AS DOC_NO,A.GRN_DATE AS DOC_DATE,'+' AS OPERATION,'1' AS TORDER,B.QTY,B.LANDED_RATE*B.QTY AS VALUE,0 AS ZERO_VAL_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.GRN_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND GRN_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.ISSUE_NO AS DOC_NO,A.ISSUE_DATE AS DOC_DATE,'-' AS OPERATION,'2' AS TORDER,B.QTY,B.ISSUE_VALUE AS VALUE,B.ZERO_VAL_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND A.APPROVED=1 AND A.CANCELED=0 AND B.ITEM_CODE='"+ItemID+"' AND A.ISSUE_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND ISSUE_DATE<='"+pToDate+"'";
                strSQL=strSQL+" UNION ";
                strSQL=strSQL+"  SELECT B.SR_NO,A.STM_NO AS DOC_NO,A.STM_DATE AS DOC_DATE,'-' AS OPERATION,'3' AS TORDER,B.QTY,B.NET_AMOUNT AS VALUE,B.ZERO_VAL_QTY FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.STM_NO=B.STM_NO AND A.APPROVED=1 AND A.CANCELLED=0 AND B.ITEM_ID='"+ItemID+"' AND A.STM_DATE>='"+EITLERPGLOBAL.formatDateDB(strFromDate)+"' AND STM_DATE<='"+pToDate+"'";
                strSQL=strSQL+"  ORDER BY DOC_DATE,TORDER,DOC_NO";
                
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    TransactionsFound=true;
                    while(!rsTmp.isAfterLast()) {
                        
                        String Operation=rsTmp.getString("OPERATION");
                        
                        strDocNo=rsTmp.getString("DOC_NO");
                        strDocNo=strDocNo+EITLERPGLOBAL.Replicate(" ", 12-strDocNo.length());
                        strDocDate=EITLERPGLOBAL.formatDate(rsTmp.getString("DOC_DATE"));
                        
                        if(Operation.equals("+")) {
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty+rsTmp.getDouble("QTY"),3);
                            OpeningValue=EITLERPGLOBAL.round(OpeningValue+rsTmp.getDouble("VALUE"),3);
                            
                            if(OpeningQty==0) {
                                OpeningRate=0;
                            }
                            else {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                        }
                        
                        
                        if(Operation.equals("-")) {
                            double currentIssueValue=0;
                            double currentIssueRate=0;
                            double currentIssueQty=0;
                            
                            //========NEW CODE=========//
                            currentIssueQty=rsTmp.getDouble("QTY");
                            
                            if(OpeningValue<0) {
                                OpeningRate=0;
                                OpeningValue=0;
                            }
                            else {
                                if(OpeningQty>0) {
                                    OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                                    OpeningValue=OpeningValue- EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                                }
                                else {
                                    OpeningRate=0;
                                    OpeningValue=EITLERPGLOBAL.round(OpeningValue-0,3);
                                }
                            }
                            
                            OpeningQty=EITLERPGLOBAL.round(OpeningQty-currentIssueQty,3);
                            
                            currentIssueRate=OpeningRate;
                            currentIssueValue=EITLERPGLOBAL.round((currentIssueQty*OpeningRate),3);
                            //=====END OF NEW CODE=====//
                            
                            
                            if(OpeningQty!=0) {
                                OpeningRate=EITLERPGLOBAL.round(OpeningValue/OpeningQty,3);
                            }
                            else {
                                OpeningRate=0;
                            }
                        }
                        
                        rsTmp.next();
                    }
                }
                
                if(OpeningQty==0) {
                    OpeningValue=0;
                    OpeningRate=0;
                }
                
                
                //UPDATE CLOSING QTY AND VALUE HERE
                String WareHouseID=clsItem.getItemWareHouseID(EITLERPGLOBAL.gCompanyID, ItemID);
                String LocationID=clsItem.getItemLocationID(EITLERPGLOBAL.gCompanyID, ItemID);
                String BOENo="X";
                String LOTNo="X";
                
                SrNo++;
                
                if(!data.IsRecordExist("SELECT ITEM_ID FROM D_COM_OPENING_STOCK_DETAIL WHERE ENTRY_NO=4 AND ITEM_ID='"+ItemID+"'")) {
                    
                    long EntrySrNo=data.getLongValueFromDB("SELECT MAX(SR_NO) FROM D_COM_OPENING_STOCK_DETAIL WHERE ENTRY_NO=4");
                    EntrySrNo++;
                    data.Execute("INSERT INTO D_COM_OPENING_STOCK_DETAIL (COMPANY_ID,ENTRY_NO,ITEM_ID,SR_NO) VALUES(2,4,'"+ItemID+"',"+EntrySrNo);
                }
                
                data.Execute("UPDATE D_COM_OPENING_STOCK_DETAIL SET OPENING_QTY="+OpeningQty+",OPENING_VALUE="+OpeningValue+" WHERE ENTRY_NO=4 AND ITEM_ID='"+ItemID+"'");
                
                rsItem.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**Unapproved STM Document //add on 26/09/2009**/
    public double getOnHandQtyOn_STMHold(int pCompanyID,String pItemID,String STMNo) {
        double stock = 0;
        String str = "SELECT B.QTY FROM D_INV_STM_HEADER A, D_INV_STM_DETAIL B "+
        " WHERE A.COMPANY_ID="+pCompanyID+" AND A.APPROVED=0  AND A.CANCELLED=0 "+
        " AND A.COMPANY_ID=A.COMPANY_ID AND A.STM_NO=B.STM_NO "+
        " AND A.STM_NO<>'"+STMNo+"' AND B.ITEM_ID='"+pItemID+"' ";
        stock = data.getDoubleValueFromDB(str);
        return stock;
    }
    /**Done with STM**/
    
}
