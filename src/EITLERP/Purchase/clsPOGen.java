/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Purchase;

import java.util.*;
import java.sql.*;
import EITLERP.*;
import EITLERP.Stores.*;
import javax.swing.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsPOGen {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colPOItems=new HashMap();
    public HashMap colPOTerms=new HashMap();
    
    public int POType=1; //Purchase Order Type - Added on 14 June to combine all the classes into one
    
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
    public clsPOGen() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("PO_TYPE",new Variant(1)); //By Default it will be one
        props.put("PO_REF",new Variant(""));
        props.put("SUPP_ID",new Variant(""));
        props.put("SUPP_NAME",new Variant(""));
        props.put("REF_A",new Variant(""));
        props.put("REF_B",new Variant(""));
        props.put("QUOTATION_NO",new Variant(""));
        props.put("QUOTATION_DATE",new Variant(""));
        props.put("INQUIRY_NO",new Variant(""));
        props.put("INQUIRY_DATE",new Variant(""));
        props.put("BUYER",new Variant(0));
        props.put("PURPOSE",new Variant(""));
        props.put("SUBJECT",new Variant(""));
        props.put("CURRENCY_ID",new Variant(0));
        props.put("CURRENCY_RATE",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("NET_AMOUNT",new Variant(0));
        props.put("STATUS",new Variant(""));
        props.put("ATTACHEMENT",new Variant(false));
        props.put("ATTACHEMENT_PATH",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("SHIP_ID",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CANCELLED",new Variant(false));
        props.put("IMPORT_CONCESS",new Variant(false));
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
        
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        props.put("AMEND_NO",new Variant(0)); //Total amendments made
        props.put("PRINT_LINE_1",new Variant(""));
        props.put("PRINT_LINE_2",new Variant(""));
        props.put("REVISION_NO",new Variant(0));
        props.put("TRANSPORT_MODE",new Variant(0));
        props.put("IMPORT_LICENSE",new Variant(""));
        props.put("PAYMENT_TERM",new Variant(""));
        
        props.put("PAYMENT_CODE",new Variant(0));
        props.put("CR_DAYS",new Variant(0));
        props.put("DEPT_ID", new Variant(0));
        
        props.put("PRICE_BASIS_TERM",new Variant(""));
        props.put("DISCOUNT_TERM",new Variant(""));
        props.put("EXCISE_TERM",new Variant(""));
        props.put("ST_TERM",new Variant(""));
        props.put("PF_TERM",new Variant(""));
        props.put("FREIGHT_TERM",new Variant(""));
        props.put("OCTROI_TERM",new Variant(""));
        props.put("FOB_TERM",new Variant(""));
        props.put("CIE_TERM",new Variant(""));
        props.put("INSURANCE_TERM",new Variant(""));
        props.put("TCC_TERM",new Variant(""));
        props.put("CENVAT_TERM",new Variant(""));
        props.put("DESPATCH_TERM",new Variant(""));
        props.put("SERVICE_TAX_TERM",new Variant(""));
        props.put("OTHERS_TERM",new Variant(""));
        props.put("FILE_TEXT",new Variant(""));
        
        props.put("CGST_TERM",new Variant(""));
        props.put("SGST_TERM",new Variant(""));
        props.put("IGST_TERM",new Variant(""));
        props.put("COMPOSITION_TERM",new Variant(""));
        props.put("RCM_TERM",new Variant(""));
        props.put("GST_COMPENSATION_CESS_TERM",new Variant(""));
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        //New Service Contract fields
        props.put("PREMISES_TYPE",new Variant(""));
        props.put("SCOPE",new Variant(""));
        props.put("SERVICE_PERIOD",new Variant(""));
        props.put("SERVICE_FREQUENCY",new Variant(""));
        props.put("CONTRACT_DETAILS",new Variant(""));
        props.put("SERVICE_REPORT",new Variant(""));
        props.put("ESI_TERMS",new Variant(""));
        props.put("TERMINATION_TERMS",new Variant(""));
        props.put("AMOUNT_IN_WORDS",new Variant(""));
        props.put("DIRECTOR_APPROVAL",new Variant(false));
        props.put("IMPORTED",new Variant(false));
        props.put("COVERING_TEXT",new Variant(""));
        
        props.put("REFRESH_FILE",new Variant(false));
        props.put("FILENAME",new Variant(""));
        
    }
    
    
    
    public boolean LoadData(int pCompanyID,int pType) {
        //Record PO type
        POType=pType;
        
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_TYPE="+POType+" AND PO_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PO_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PO_DATE,PO_NO");
            //rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_TYPE="+POType+" ");
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
    
    public boolean LoadData(int pCompanyID,String pURL,int pType) {
        Ready=false;
        try {
            Conn=data.getConn(pURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_TYPE="+pType+" "); //1- General P.O.
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_TYPE="+pType+" ORDER BY PO_DATE"); //1- General P.O.
            Ready=true;
            HistoryView=false;
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
        Statement stTmp,stIndent,stItem,stQuotation,stTerms,stHeader,stHistory,stHDetail,stHTerms;
        ResultSet rsTmp,rsIndent,rsItem,rsQuotation,rsTerms,rsHeader,rsHistory,rsHDetail,rsHTerms;
        String strSQL="",IndentNo="",QuotID;
        int CompanyID=0,IndentSrNo=0,QuotSrNo=0;
        double Qty=0;
        
        try {
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE PO_NO='1'");
            //rsHeader.first();
            
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("PO_DATE").getObj());
             
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_PO_HEADER_H WHERE PO_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_PO_DETAIL_H WHERE PO_NO='1'");
            rsHDetail.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_PUR_PO_TERMS_H WHERE PO_NO='1'");
            rsHTerms.first();
            //------------------------------------//
            
            
            //======= Supplier Validation =======//
            if(!ValidateSupplier()) {
                //LastError += "Supplier code is not valid.";
                return false;
            }
            //===================================//
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            
            
            //=========== Check the Quantities entered against Indent============= //
            for(int i=1;i<=colPOItems.size();i++) {
                clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                
                QuotID=(String)ObjItem.getAttribute("QUOT_ID").getObj();
                QuotSrNo=(int)ObjItem.getAttribute("QUOT_SR_NO").getVal();
                
                double IndentQty=0;
                double QuotQty=0;
                double PrevQty=0; //Previously Entered Qty against Indent
                double CurrentQty=0; //Currently entered Qty.
                
                if((!IndentNo.trim().equals(""))&&(IndentSrNo>0)) //Indent Entered
                {
                    //Get the  Indent Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    strSQL="SELECT QTY,ITEM_CODE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        IndentQty=rsTmp.getDouble("QTY");
                        
                        if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                            LastError="Item Code in PO doesn't match with Indent. Original Item code is "+rsTmp.getString("ITEM_CODE");
                            return false;
                        }
                    }
                    
                    //Get Total Qty Entered in PO Against this Indent No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo+"' AND INDENT_SR_NO="+IndentSrNo+" AND PO_NO NOT IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1) ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    
                    //Also find duplicate entries - made with Ctrl+C functionality
                    for(int d=1;d<=colPOItems.size();d++) {
                        if(d!=i) {
                            clsPOItem ObjD=(clsPOItem)colPOItems.get(Integer.toString(d));
                            String tmpIndentNo=(String)ObjD.getAttribute("INDENT_NO").getObj();
                            int tmpIndentSrNo=(int)ObjD.getAttribute("INDENT_SR_NO").getVal();
                            
                            if(tmpIndentNo.equals(IndentNo)&&tmpIndentSrNo==IndentSrNo) {
                                CurrentQty+=ObjD.getAttribute("QTY").getVal();
                            }
                        }
                    }
                    //===============================================================
                    
                    
                    
                    if((CurrentQty+PrevQty) > IndentQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Indent No. "+IndentNo+" Sr. No. "+IndentSrNo+" qty "+IndentQty+". Please verify the input.";
                        return false;
                    }
                }
                
                
                //---------  Checking Quotations -----------//
                if((!QuotID.trim().equals(""))&&(QuotSrNo>0)) //Quotation Entered
                {
                    //Get the  Quotation Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    strSQL="SELECT QTY FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID+"' AND SR_NO="+QuotSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        QuotQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in PO Against this Quotation No.
                    PrevQty=0;
                    
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    strSQL="SELECT IF (SUM(QTY) is null , 0,SUM(QTY)) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID+"' AND QUOT_SR_NO="+QuotSrNo+"  AND PO_NO NOT IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    //Find duplicate entries
                    for(int d=1;d<=colPOItems.size();d++) {
                        if(d!=i) {
                            clsPOItem ObjD=(clsPOItem)colPOItems.get(Integer.toString(d));
                            String tmpQuotID=(String)ObjD.getAttribute("QUOT_ID").getObj();
                            int tmpQuotSrNo=(int)ObjD.getAttribute("QUOT_SR_NO").getVal();
                            
                            if(tmpQuotID.equals(QuotID)&&tmpQuotSrNo==QuotSrNo) {
                                CurrentQty+=ObjD.getAttribute("QTY").getVal();
                            }
                            
                        }
                    }
                    
                    
                    
                    if((CurrentQty+PrevQty) > QuotQty) //If total Qty exceeds Quotation Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Quotation No. "+QuotID+" Sr. No. "+QuotSrNo+" qty "+QuotQty+". Please verify the input.";
                        return false;
                    }
                    
                    if(POType==1||POType==5 || POType==9 ) {
                        //Check that Indent is exist for this quoation
                        if(!clsQuotation.IsIndentExist(EITLERPGLOBAL.gCompanyID, QuotID, QuotSrNo )) {
                            LastError="Indent for the quotation No. "+QuotID+" does not exist. You cannot use this quotation";
                            return false;
                        }
                    }
                }
                //=========================================================//
                
            }
            //============== Indent Checking Completed ====================//
            
            
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            
            //======= Checking with RIA for approved qty =========//
            //===================================================//
            String Messages="";
            boolean ContinuePO=true;
            
            if(POType==1||POType==3||POType==5 || POType==9 ) {
                for(int i=1;i<=colPOItems.size();i++) {
                    clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                    IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                    String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    Qty=ObjItem.getAttribute("QTY").getVal();
                    
                    //First check that RIA is created
                    if((!IndentNo.trim().equals(""))&&IndentSrNo>0) {
                        String RIANo=clsIndent.getRIANo(EITLERPGLOBAL.gCompanyID, IndentNo,IndentSrNo);
                        if(!RIANo.trim().equals("")) {
                            int RIAStatus=clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                            double RIAQty=clsIndent.getRIAQty(EITLERPGLOBAL.gCompanyID, IndentNo, IndentSrNo);
                            
                            
                            if(RIAStatus==1)  //RIA is made and approved
                            {
                                if(Qty<=RIAQty) {
                                    // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                }
                                else {
                                    //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                    Messages+="\nOnly "+RIAQty+" is approved for item "+ItemID;
                                    //ContinuePO=false;
                                    ContinuePO=true;
                                }
                            }
                            else {  //RIA under approval. Reject it
                                Messages+="\nRIA No. "+RIANo+" is under approval of item "+ItemID;
                                ContinuePO=false;
                                
                            }
                            
                        }
                        else {
                            //Find the last RIA No.
                            
                            //Following statement will return any last RIA made for the item
                            String PODate=(String)getAttribute("PO_DATE").getObj();
                            
                            RIANo=clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID,ItemID,PODate);
                            if(RIANo.trim().equals("")) {
                                //Continue with last RIA No.
                            }
                            else {
                                //Put the code here if PO is to be restricted to make RIA compulsoary.
                            }
                        }
                    }
                    else {
                        //No Indent Reference
                        
                        //Following statement will return any last RIA made for the item
                        String PODate=(String)getAttribute("PO_DATE").getObj();
                        
                        //Following statement will return any last RIA made for the item
                        String RIANo=clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID, ItemID,PODate);
                        if(!RIANo.trim().equals("")) {
                            int RIAStatus=clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                            double RIAQty=clsRateApproval.getRIAApprovedQty(EITLERPGLOBAL.gCompanyID,RIANo,ItemID);
                            
                            if(RIAStatus==1)  //RIA is made and approved
                            {
                                if(Qty<=RIAQty) {
                                    // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                }
                                else {
                                    //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                    Messages+="\nOnly "+RIAQty+" is approved for item "+ItemID;
                                    ContinuePO=false;
                                }
                            }
                            else {  //RIA under approval. Reject it
                                Messages+="\nRIA No. "+RIANo+" is under approval of item "+ItemID;
                                ContinuePO=false;
                                
                            }
                        }
                    }
                    
                }
                
                
                
                //RIA Checking will only affec
                if(!AStatus.equals("F")) {
                    if(!ContinuePO) {
                        JOptionPane.showMessageDialog(null,"PO will not be final approved. Following RIAs are not complete"+Messages);
                    }
                }
                else {
                    if(!ContinuePO) {
                        LastError="Can not final approve the PO. Following RIAs are not complete"+Messages;
                        return false;
                    }
                }
            }
            //==================================================//
            //=================================================//
            
            
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colPOItems.size();i++) {
                    clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                    
                    IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                    QuotID=(String)ObjItem.getAttribute("QUOT_ID").getObj();
                    QuotSrNo=(int)ObjItem.getAttribute("QUOT_SR_NO").getVal();
                    
                    Qty=ObjItem.getAttribute("QTY").getVal();
                    
                    try {
                        // Update Indent PO Qty
                        data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY+"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo.trim()+"' AND SR_NO="+IndentSrNo+" ");
                        data.Execute("UPDATE D_INV_INDENT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo.trim()+"'");
                        
                        
                        //Update Quotation Qty
                        data.Execute("UPDATE D_PUR_QUOT_DETAIL SET PO_QTY=PO_QTY+"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID+"' AND SR_NO="+QuotSrNo+" ");
                        data.Execute("UPDATE D_PUR_QUOT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID+"'");
                    }
                    catch(Exception e) {
                        
                    }
                } //First for loop
                //=============Updation of stock completed=========================//
            } // End of First Approval Status condition
            
            
            
            
            //--------- Generate New GRN No. ------------//
            // As per the PO Type - Change the Module IDs
            
            if(POType==1) //General PO
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,21,(int)getAttribute("FFNO").getVal(),true));
            }
            
            if(POType==2) //Engineering PO
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,22,(int)getAttribute("FFNO").getVal(),true));
            }
            
            if(POType==3) //A Class PO
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,23,(String)getAttribute("PREFIX").getObj(),(String)getAttribute("SUFFIX").getObj(),true));
            }
            
            if(POType==4) //Raw Material PO
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,24,(String)getAttribute("PREFIX").getObj(),(String)getAttribute("SUFFIX").getObj(),true));
            }
            
            if(POType==5) //Spares PO
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,25,(int)getAttribute("FFNO").getVal(),true));
            }
            
            if(POType==6) //Capital PO
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,26,(int)getAttribute("FFNO").getVal(),true));
            }
            
            if(POType==7) //Contract PO
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,27,(int)getAttribute("FFNO").getVal(),true));
            }
            
            if(POType==8) //Service Contract
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,46,(int)getAttribute("FFNO").getVal(),true));
            }
            if(POType==9) //Service Contract
            {
                setAttribute("PO_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,153,(int)getAttribute("FFNO").getVal(),true));
            }
            //-------------------------------------------------
            
            
            //======== Store the Physical File into database =====//
            long DocID=0;
            
            if(!getAttribute("FILENAME").getObj().toString().trim().equals("")) {
                if(getAttribute("REFRESH_FILE").getBool()) {
                    String FileName=getAttribute("FILENAME").getObj().toString();
                    File f=new File(FileName);
                    
                    if(f.exists()) {
                        DocID=clsDocument.StoreDocument(EITLERPGLOBAL.gCompanyID,((String)getAttribute("PO_NO").getObj()),"", FileName);
                    }
                }
            }
            //====================================================//
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsResultSet.updateLong("DOC_ID",DocID);
            rsResultSet.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsResultSet.updateInt("PO_TYPE",POType);
            rsResultSet.updateString("PO_REF",(String)getAttribute("PO_REF").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("SUPP_NAME",(String)getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("REF_A",(String)getAttribute("REF_A").getObj());
            rsResultSet.updateString("REF_B",(String)getAttribute("REF_B").getObj());
            rsResultSet.updateString("QUOTATION_NO",(String)getAttribute("QUOTATION_NO").getObj());
            rsResultSet.updateString("QUOTATION_DATE",(String)getAttribute("QUOTATION_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateInt("BUYER",(int)getAttribute("BUYER").getVal());
            rsResultSet.updateInt("TRANSPORT_MODE",(int)getAttribute("TRANSPORT_MODE").getVal());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsResultSet.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("SHIP_ID",(int)getAttribute("SHIP_ID").getVal());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            //rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
            
            
            
            rsResultSet.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateString("PRINT_LINE_1",(String)getAttribute("PRINT_LINE_1").getObj());
            rsResultSet.updateString("PRINT_LINE_2",(String)getAttribute("PRINT_LINE_2").getObj());
            rsResultSet.updateString("IMPORT_LICENSE",(String)getAttribute("IMPORT_LICENSE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            rsResultSet.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
            
            rsResultSet.updateInt("PAYMENT_CODE",getAttribute("PAYMENT_CODE").getInt());
            rsResultSet.updateInt("CR_DAYS",getAttribute("CR_DAYS").getInt());
            rsResultSet.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());
            
            rsResultSet.updateString("PRICE_BASIS_TERM",(String)getAttribute("PRICE_BASIS_TERM").getObj());
            rsResultSet.updateString("DISCOUNT_TERM",(String)getAttribute("DISCOUNT_TERM").getObj());
            rsResultSet.updateString("EXCISE_TERM",(String)getAttribute("EXCISE_TERM").getObj());
            rsResultSet.updateString("ST_TERM",(String)getAttribute("ST_TERM").getObj());
            rsResultSet.updateString("PF_TERM",(String)getAttribute("PF_TERM").getObj());
            rsResultSet.updateString("FREIGHT_TERM",(String)getAttribute("FREIGHT_TERM").getObj());
            rsResultSet.updateString("OCTROI_TERM",(String)getAttribute("OCTROI_TERM").getObj());
            rsResultSet.updateString("FOB_TERM",(String)getAttribute("FOB_TERM").getObj());
            rsResultSet.updateString("CIE_TERM",(String)getAttribute("CIE_TERM").getObj());
            rsResultSet.updateString("INSURANCE_TERM",(String)getAttribute("INSURANCE_TERM").getObj());
            rsResultSet.updateString("TCC_TERM",(String)getAttribute("TCC_TERM").getObj());
            rsResultSet.updateString("CENVAT_TERM",(String)getAttribute("CENVAT_TERM").getObj());
            rsResultSet.updateString("DESPATCH_TERM",(String)getAttribute("DESPATCH_TERM").getObj());
            rsResultSet.updateString("SERVICE_TAX_TERM",(String)getAttribute("SERVICE_TAX_TERM").getObj());
            rsResultSet.updateString("OTHERS_TERM",(String)getAttribute("OTHERS_TERM").getObj());
            rsResultSet.updateString("FILE_TEXT",(String)getAttribute("FILE_TEXT").getObj());
            rsResultSet.updateString("COVERING_TEXT",(String)getAttribute("COVERING_TEXT").getObj());
            
            rsResultSet.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsResultSet.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsResultSet.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsResultSet.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsResultSet.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsResultSet.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            
            
            rsResultSet.updateString("PREMISES_TYPE",(String)getAttribute("PREMISES_TYPE").getObj());
            rsResultSet.updateString("SCOPE",(String)getAttribute("SCOPE").getObj());
            rsResultSet.updateString("SERVICE_PERIOD",(String)getAttribute("SERVICE_PERIOD").getObj());
            rsResultSet.updateString("SERVICE_FREQUENCY",(String)getAttribute("SERVICE_FREQUENCY").getObj());
            rsResultSet.updateString("CONTRACT_DETAILS",(String)getAttribute("CONTRACT_DETAILS").getObj());
            rsResultSet.updateString("SERVICE_REPORT",(String)getAttribute("SERVICE_REPORT").getObj());
            rsResultSet.updateString("ESI_TERMS",(String)getAttribute("ESI_TERMS").getObj());
            rsResultSet.updateString("TERMINATION_TERMS",(String)getAttribute("TERMINATION_TERMS").getObj());
            rsResultSet.updateString("AMOUNT_IN_WORDS",(String)getAttribute("AMOUNT_IN_WORDS").getObj());
            rsResultSet.updateBoolean("DIRECTOR_APPROVAL",getAttribute("DIRECTOR_APPROVAL").getBool());
            rsResultSet.updateBoolean("IMPORTED",getAttribute("IMPORTED").getBool());
            
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
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsResultSet.updateLong("DOC_ID",DocID);
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateInt("PO_TYPE",POType);
            rsHistory.updateString("PO_REF",(String)getAttribute("PO_REF").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("SUPP_NAME",(String)getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("REF_A",(String)getAttribute("REF_A").getObj());
            rsHistory.updateString("REF_B",(String)getAttribute("REF_B").getObj());
            rsHistory.updateString("QUOTATION_NO",(String)getAttribute("QUOTATION_NO").getObj());
            rsHistory.updateString("QUOTATION_DATE",(String)getAttribute("QUOTATION_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateInt("BUYER",(int)getAttribute("BUYER").getVal());
            rsHistory.updateInt("TRANSPORT_MODE",(int)getAttribute("TRANSPORT_MODE").getVal());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsHistory.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("SHIP_ID",(int)getAttribute("SHIP_ID").getVal());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            //rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
            
            
            rsHistory.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateString("PRINT_LINE_1",(String)getAttribute("PRINT_LINE_1").getObj());
            rsHistory.updateString("PRINT_LINE_2",(String)getAttribute("PRINT_LINE_2").getObj());
            rsHistory.updateString("IMPORT_LICENSE",(String)getAttribute("IMPORT_LICENSE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
            
            rsHistory.updateInt("PAYMENT_CODE",getAttribute("PAYMENT_CODE").getInt());
            rsHistory.updateInt("CR_DAYS",getAttribute("CR_DAYS").getInt());
            rsHistory.updateInt("DEPT_ID",getAttribute("DEPT_ID").getInt());
            
            rsHistory.updateString("PRICE_BASIS_TERM",(String)getAttribute("PRICE_BASIS_TERM").getObj());
            rsHistory.updateString("OTHERS_TERM",(String)getAttribute("OTHERS_TERM").getObj());
            rsHistory.updateString("DISCOUNT_TERM",(String)getAttribute("DISCOUNT_TERM").getObj());
            rsHistory.updateString("EXCISE_TERM",(String)getAttribute("EXCISE_TERM").getObj());
            rsHistory.updateString("ST_TERM",(String)getAttribute("ST_TERM").getObj());
            rsHistory.updateString("PF_TERM",(String)getAttribute("PF_TERM").getObj());
            rsHistory.updateString("FREIGHT_TERM",(String)getAttribute("FREIGHT_TERM").getObj());
            rsHistory.updateString("OCTROI_TERM",(String)getAttribute("OCTROI_TERM").getObj());
            rsHistory.updateString("FOB_TERM",(String)getAttribute("FOB_TERM").getObj());
            rsHistory.updateString("CIE_TERM",(String)getAttribute("CIE_TERM").getObj());
            rsHistory.updateString("INSURANCE_TERM",(String)getAttribute("INSURANCE_TERM").getObj());
            rsHistory.updateString("TCC_TERM",(String)getAttribute("TCC_TERM").getObj());
            rsHistory.updateString("CENVAT_TERM",(String)getAttribute("CENVAT_TERM").getObj());
            rsHistory.updateString("DESPATCH_TERM",(String)getAttribute("DESPATCH_TERM").getObj());
            rsHistory.updateString("SERVICE_TAX_TERM",(String)getAttribute("SERVICE_TAX_TERM").getObj());
            rsHistory.updateString("COVERING_TEXT",(String)getAttribute("COVERING_TEXT").getObj());
            
            rsHistory.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsHistory.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsHistory.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsHistory.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsHistory.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsHistory.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            
            rsHistory.updateString("PREMISES_TYPE",(String)getAttribute("PREMISES_TYPE").getObj());
            rsHistory.updateString("SCOPE",(String)getAttribute("SCOPE").getObj());
            rsHistory.updateString("SERVICE_PERIOD",(String)getAttribute("SERVICE_PERIOD").getObj());
            rsHistory.updateString("SERVICE_FREQUENCY",(String)getAttribute("SERVICE_FREQUENCY").getObj());
            rsHistory.updateString("CONTRACT_DETAILS",(String)getAttribute("CONTRACT_DETAILS").getObj());
            rsHistory.updateString("SERVICE_REPORT",(String)getAttribute("SERVICE_REPORT").getObj());
            rsHistory.updateString("ESI_TERMS",(String)getAttribute("ESI_TERMS").getObj());
            rsHistory.updateString("TERMINATION_TERMS",(String)getAttribute("TERMINATION_TERMS").getObj());
            rsHistory.updateString("FILE_TEXT",(String)getAttribute("FILE_TEXT").getObj());
            rsHistory.updateString("AMOUNT_IN_WORDS",(String)getAttribute("AMOUNT_IN_WORDS").getObj());
            rsHistory.updateBoolean("DIRECTOR_APPROVAL",getAttribute("DIRECTOR_APPROVAL").getBool());
            rsHistory.updateBoolean("IMPORTED",getAttribute("IMPORTED").getBool());
            
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            //====== Now turn of P.O. Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_DETAIL WHERE PO_NO='1' ");
            rsItem.first();
            
            for(int i=1;i<=colPOItems.size();i++) {
                clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("PO_TYPE",POType);
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("SDML_SHADE",(String)ObjItem.getAttribute("SDML_SHADE").getObj());
                rsItem.updateString("VENDOR_SHADE",(String)ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsItem.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsItem.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsItem.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsItem.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsItem.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsItem.updateString("PART_NO",(String)ObjItem.getAttribute("PART_NO").getObj());
                rsItem.updateString("EXCISE_TARRIF_NO",(String)ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("PENDING_QTY",ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("RECD_QTY",0);
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getVal());
                rsItem.updateDouble("DISC_PER",ObjItem.getAttribute("DISC_PER").getVal());
                rsItem.updateDouble("DISC_AMT",ObjItem.getAttribute("DISC_AMT").getVal());
                rsItem.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateString("REFERENCE",(String)ObjItem.getAttribute("REFERENCE").getObj());
                rsItem.updateString("DELIVERY_DATE",(String)ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsItem.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                //rsItem.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
                
                
                
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsItem.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsItem.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
                
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("PO_TYPE",POType);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("SDML_SHADE",(String)ObjItem.getAttribute("SDML_SHADE").getObj());
                rsHDetail.updateString("VENDOR_SHADE",(String)ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateString("PART_NO",(String)ObjItem.getAttribute("PART_NO").getObj());
                rsHDetail.updateString("EXCISE_TARRIF_NO",(String)ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("PENDING_QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RECD_QTY",0);
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("DISC_PER",ObjItem.getAttribute("DISC_PER").getVal());
                rsHDetail.updateDouble("DISC_AMT",ObjItem.getAttribute("DISC_AMT").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateString("REFERENCE",(String)ObjItem.getAttribute("REFERENCE").getObj());
                rsHDetail.updateString("DELIVERY_DATE",(String)ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                //rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
                
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            
            
            //===== Updating PO Terms --------------------------//
            
            //====== Now turn of P.O. Items ======
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_PUR_PO_TERMS WHERE PO_NO='1' ");
            rsTerms.first();
            
            for(int i=1;i<=colPOTerms.size();i++) {
                clsPOTerms ObjTerm=(clsPOTerms)colPOTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsTerms.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsTerms.updateInt("SR_NO",i);
                rsTerms.updateInt("PO_TYPE",POType);
                rsTerms.updateString("TERM_TYPE",(String)ObjTerm.getAttribute("TERM_TYPE").getObj());
                rsTerms.updateInt("TERM_CODE",(int)ObjTerm.getAttribute("TERM_CODE").getVal());
                rsTerms.updateString("TERM_DESC",(String)ObjTerm.getAttribute("TERM_DESC").getObj());
                rsTerms.updateBoolean("CHANGED",true);
                rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();
                
                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO",1);
                rsHTerms.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHTerms.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsHTerms.updateInt("SR_NO",i);
                rsHTerms.updateInt("PO_TYPE",POType);
                rsHTerms.updateString("TERM_TYPE",(String)ObjTerm.getAttribute("TERM_TYPE").getObj());
                rsHTerms.updateInt("TERM_CODE",(int)ObjTerm.getAttribute("TERM_CODE").getVal());
                rsHTerms.updateString("TERM_DESC",(String)ObjTerm.getAttribute("TERM_DESC").getObj());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            //Assign the Module ID as per PO Type
            ObjFlow.ModuleID=POType+20; //Bcz. PO Types starts from 1 and Module ID starts from 21
            
            if(POType==8) {
                ObjFlow.ModuleID=46;     //Specific Patch
            }
            if(POType==9) {
                ObjFlow.ModuleID=153;     //Specific Patch
            }
            ObjFlow.DocNo=(String)getAttribute("PO_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_PO_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="PO_NO";
            ObjFlow.DocDate=(String)getAttribute("PO_DATE").getObj();
            
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
        Statement stTmp,stIndent,stItem,stQuotation,stTerms,stHeader,stHistory,stHDetail,stHTerms;
        ResultSet rsTmp,rsIndent,rsItem,rsQuotation,rsTerms,rsHeader,rsHistory,rsHDetail,rsHTerms;
        String strSQL="",IndentNo="",PONo="",AmendNo="",QuotID="";
        int CompanyID=0,IndentSrNo=0,QuotSrNo=0;
        double Qty=0;
        boolean Validate=true;
        int OldHierarchy=0;
        
        try {
            System.out.println("Update PO start in cls...");
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            Validate=true;
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //======= Check the requisition date ============//
            /*java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate=java.sql.Date.valueOf((String)getAttribute("PO_DATE").getObj());
             
            if((DocDate.after(FinFromDate)||DocDate.compareTo(FinFromDate)==0)&&(DocDate.before(FinToDate)||DocDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Document date is not within financial year.";
                return false;
            }*/
            //=====================================================//
            System.out.println("Update PO start in cls...");
            
            String theDocNo=(String)getAttribute("PO_NO").getObj();
            rsTmp=data.getResult("SELECT HIERARCHY_ID FROM D_PUR_PO_HEADER WHERE PO_NO='"+theDocNo+"'");
            if(rsTmp.getRow()>0) {
                OldHierarchy=rsTmp.getInt("HIERARCHY_ID");
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_PO_HEADER_H WHERE PO_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_PO_DETAIL_H WHERE PO_NO='1'");
            rsHDetail.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_PUR_PO_TERMS_H WHERE PO_NO='1'");
            rsHTerms.first();
            //------------------------------------//
            
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            PONo=(String)getAttribute("PO_NO").getObj();
            if(Validate) {
                //======= Supplier Validation =======//
                System.out.println("Update PO validate start...");
                if(!ValidateSupplier()) {
                    return false;
                }
                //===================================//
                System.out.println("Update PO validate End 111...");
                //COMMENT BY MUFFY
                //CompanyID=(int)getAttribute("COMPANY_ID").getVal();
                //PONo=(String)getAttribute("PO_NO").getObj();
                
                //=========== Check the Quantities entered against Indent============= //
                for(int i=1;i<=colPOItems.size();i++) {
                    clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                    String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                    QuotID=(String)ObjItem.getAttribute("QUOT_ID").getObj();
                    QuotSrNo=(int)ObjItem.getAttribute("QUOT_SR_NO").getVal();
                    
                    double IndentQty=0;
                    double QuotQty=0;
                    double PrevQty=0; //Previously Entered Qty against Indent
                    double CurrentQty=0; //Currently entered Qty.
                    
                    if((!IndentNo.trim().equals(""))&&(IndentSrNo>0)) //Indent Entered
                    {
                        //Get the  Indent Qty.
                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        strSQL="SELECT QTY,ITEM_CODE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo+" ";
                        rsTmp=stTmp.executeQuery(strSQL);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            IndentQty=rsTmp.getDouble("QTY");
                            
                            if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                                LastError="Item Code in PO doesn't match with Indent. Original Item code is "+rsTmp.getString("ITEM_CODE");
                                return false;
                            }
                            
                        }
                        
                        //Get Total Qty Entered in PO Against this Indent No.
                        PrevQty=0;
                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo+"' AND INDENT_SR_NO="+IndentSrNo+" AND NOT(PO_NO='"+PONo+"' AND PO_TYPE="+POType+") AND PO_NO NOT IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1)";
                        rsTmp=stTmp.executeQuery(strSQL);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            PrevQty=rsTmp.getDouble("SUMQTY");
                        }
                        
                        CurrentQty=ObjItem.getAttribute("QTY").getVal();
                        
                        for(int d=1;d<=colPOItems.size();d++) {
                            if(d!=i) {
                                clsPOItem ObjD=(clsPOItem)colPOItems.get(Integer.toString(d));
                                
                                String tmpIndentNo=(String)ObjD.getAttribute("INDENT_NO").getObj();
                                int tmpIndentSrNo=(int)ObjD.getAttribute("INDENT_SR_NO").getVal();
                                
                                if(tmpIndentNo.equals(IndentNo)&&tmpIndentSrNo==IndentSrNo) {
                                    CurrentQty+=ObjD.getAttribute("QTY").getVal();
                                }
                                
                            }
                        }
                        
                        
                        if((CurrentQty+PrevQty) > IndentQty) //If total Qty exceeds Indent Qty. Do not allow
                        {
                            LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Indent No. "+IndentNo+" Sr. No. "+IndentSrNo+" qty "+IndentQty+". Please verify the input.";
                            return false;
                        }
                    }
                    
                    
                    if((!QuotID.trim().equals(""))&&(QuotSrNo>0)) //Quotation Entered
                    {
                        //Get the  Indent Qty.
                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        strSQL="SELECT QTY FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID+"' AND SR_NO="+QuotSrNo+" ";
                        rsTmp=stTmp.executeQuery(strSQL);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            QuotQty=rsTmp.getDouble("QTY");
                        }
                        
                        //Get Total Qty Entered in PO Against this Indent No.
                        PrevQty=0;
                        stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID+"' AND QUOT_SR_NO="+QuotSrNo+"  AND NOT(PO_NO='"+PONo+"' AND PO_TYPE="+POType+") AND PO_NO NOT IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE CANCELLED=1)";
                        rsTmp=stTmp.executeQuery(strSQL);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            PrevQty=rsTmp.getDouble("SUMQTY");
                        }
                        
                        CurrentQty=ObjItem.getAttribute("QTY").getVal();
                        
                        
                        
                        for(int d=1;d<=colPOItems.size();d++) {
                            if(d!=i) {
                                clsPOItem ObjD=(clsPOItem)colPOItems.get(Integer.toString(d));
                                String tmpQuotID=(String)ObjD.getAttribute("QUOT_ID").getObj();
                                int tmpQuotSrNo=(int)ObjD.getAttribute("QUOT_SR_NO").getVal();
                                
                                if(tmpQuotID.equals(QuotID)&&tmpQuotSrNo==QuotSrNo) {
                                    CurrentQty+=ObjD.getAttribute("QTY").getVal();
                                }
                            }
                        }
                        
                        
                        if((CurrentQty+PrevQty) > QuotQty) //If total Qty exceeds Indent Qty. Do not allow
                        {
                            LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Quotation No. "+QuotID+" Sr. No. "+QuotSrNo+" qty "+QuotQty+". Please verify the input.";
                            return false;
                        }
                        
                        if(POType==1||POType==5 || POType ==9) {
                            //Check that Indent is exist for this quoation
                            if(!clsQuotation.IsIndentExist(EITLERPGLOBAL.gCompanyID, QuotID, QuotSrNo )) {
                                LastError="Indent for the quotation No. "+QuotID+" does not exist. You cannot use this quotation";
                                return false;
                            }
                        }
                        
                    }
                }
                //============== Indent Checking Completed ====================//
                
                // Update the Stock only after Final Approval //
                
                System.out.println("Update PO validate End 222...");
                
                //======= Checking with RIA for approved qty =========//
                //===================================================//
                String Messages="";
                boolean ContinuePO=true;
                System.out.println("Update PO RIA Start...");
                if(POType==1||POType==3||POType==5 || POType ==9) {
                    for(int i=1;i<=colPOItems.size();i++) {
                        clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                        IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                        IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                        String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                        Qty=ObjItem.getAttribute("QTY").getVal();
                        
                        //First check that RIA is created
                        if((!IndentNo.trim().equals(""))&&IndentSrNo>0) {
                            String RIANo=clsIndent.getRIANo(EITLERPGLOBAL.gCompanyID, IndentNo,IndentSrNo);
                            if(!RIANo.trim().equals("")) {
                                int RIAStatus=clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                                double RIAQty=clsIndent.getRIAQty(EITLERPGLOBAL.gCompanyID, IndentNo, IndentSrNo);
                                
                                if(RIAStatus==1)  //RIA is made and approved
                                {
                                    if(Qty<=RIAQty) {
                                        // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                    }
                                    else {
                                        //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                        Messages+="\nOnly "+RIAQty+" is approved for item "+ItemID;
                                        //ContinuePO=false;
                                        ContinuePO=true;
                                    }
                                }
                                else {  //RIA under approval. Reject it
                                    Messages+="\nRIA No. "+RIANo+" is under approval of item "+ItemID;
                                    ContinuePO=false;
                                    
                                }
                                
                            }
                            else {
                                //Find the last RIA No.
                                
                                //Following statement will return any last RIA made for the item
                                String PODate=(String)getAttribute("PO_DATE").getObj();
                                
                                RIANo=clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID,ItemID,PODate);
                                if(RIANo.trim().equals("")) {
                                    //Continue with last RIA No.
                                }
                                else {
                                    //Put the code here if PO is to be restricted to make RIA compulsoary.
                                }
                                
                            }
                        }
                        else {
                            //No Indent Reference
                            
                            //Following statement will return any last RIA made for the item
                            String PODate=(String)getAttribute("PO_DATE").getObj();
                            
                            String RIANo=clsRateApproval.getRIANo(EITLERPGLOBAL.gCompanyID, ItemID,PODate);
                            if(!RIANo.trim().equals("")) {
                                int RIAStatus=clsRateApproval.getRIAStatus(EITLERPGLOBAL.gCompanyID, RIANo);
                                double RIAQty=clsRateApproval.getRIAApprovedQty(EITLERPGLOBAL.gCompanyID,RIANo,ItemID);
                                
                                if(RIAStatus==1)  //RIA is made and approved
                                {
                                    if(Qty<=RIAQty) {
                                        // APPROVED. CONTINUE PREPARING PURCHASE ORDER
                                    }
                                    else {
                                        //Qty in Purchase Order getting exceeded to RIA Approved qty.
                                        Messages+="\nOnly "+RIAQty+" is approved for item "+ItemID;
                                        // ContinuePO=false; //---->Change here on 14 May 2006 //
                                        ContinuePO=true;
                                    }
                                }
                                else {  //RIA under approval. Reject it
                                    Messages+="\nRIA No. "+RIANo+" is under approval of item "+ItemID;
                                    ContinuePO=false;
                                    
                                }
                            }
                        }
                        
                    }
                    
                    
                    
                    //RIA Checking will only affec
                    if(!AStatus.equals("F")) {
                        if(!ContinuePO) {
                            JOptionPane.showMessageDialog(null,"PO will not be final approved. Following RIAs are not complete"+Messages);
                        }
                    }
                    else {
                        if(!ContinuePO) {
                            LastError="Can not final approve the PO. Following RIAs are not complete"+Messages;
                            return false;
                        }
                        
                        
                    }
                }
                
                System.out.println("Update PO RIA End...");
                //==================================================//
                //=================================================//
                
                System.out.println("Update PO A status Start...");
                if(AStatus.equals("F")) {
                    //-------- First Update the stock -------------//
                    for(int i=1;i<=colPOItems.size();i++) {
                        clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                        
                        IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                        IndentSrNo=(int)ObjItem.getAttribute("INDENT_SR_NO").getVal();
                        QuotID=(String)ObjItem.getAttribute("QUOT_ID").getObj();
                        QuotSrNo=(int)ObjItem.getAttribute("QUOT_SR_NO").getVal();
                        Qty=ObjItem.getAttribute("QTY").getVal();
                        
                        try {
                            // Update Indent Qty
                            data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY+"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo.trim()+"' AND SR_NO="+IndentSrNo+" ");
                            data.Execute("UPDATE D_INV_INDENT_HEADER SET PO_QTY=PO_QTY+"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo.trim()+"' AND SR_NO="+IndentSrNo+" ");
                            
                            // Update Quotation Qty
                            data.Execute("UPDATE D_PUR_QUOT_DETAIL SET PO_QTY=PO_QTY+"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID.trim()+"' AND SR_NO="+QuotSrNo+" ");
                            data.Execute("UPDATE D_PUR_QUOT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND QUOT_ID='"+QuotID.trim()+"'");
                        }
                        catch(Exception e) {
                            
                        }
                    } //First for loop
                    //=============Updation of stock completed=========================//
                } // End of First Approval Status condition
                System.out.println("Update PO A status End...");
                //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+CompanyID+" AND TRIM(PO_NO)='"+PONo+"' AND PO_TYPE="+POType);
                //rsHeader.first();
            }
            
            
            
            //======== Store the Physical File into database =====//
            long DocID=(long)getAttribute("DOC_ID").getVal() ;
            
            if(DocID==0) {
                if(!getAttribute("FILENAME").getObj().toString().trim().equals("")) {
                    if(getAttribute("REFRESH_FILE").getBool()) {
                        String FileName=getAttribute("FILENAME").getObj().toString();
                        File f=new File(FileName);
                        
                        if(f.exists()) {
                            DocID=clsDocument.StoreDocument(EITLERPGLOBAL.gCompanyID,((String)getAttribute("PO_NO").getObj()),"", FileName);
                        }
                    }
                }
            } else {
                if(!getAttribute("FILENAME").getObj().toString().trim().equals("")) {
                    if(getAttribute("REFRESH_FILE").getBool()) {
                        String FileName=getAttribute("FILENAME").getObj().toString();
                        File f=new File(FileName);
                        
                        if(f.exists()) {
                            clsDocument.UpdateDocument(DocID,FileName);
                        }
                    }
                }
            }
            //====================================================//
            System.out.println("Update PO Header entry start...");
            
            rsResultSet.updateLong("DOC_ID",DocID);
            rsResultSet.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsResultSet.updateInt("PO_TYPE",POType);
            rsResultSet.updateString("PO_REF",(String)getAttribute("PO_REF").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("SUPP_NAME",(String)getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("REF_A",(String)getAttribute("REF_A").getObj());
            rsResultSet.updateString("REF_B",(String)getAttribute("REF_B").getObj());
            rsResultSet.updateString("QUOTATION_NO",(String)getAttribute("QUOTATION_NO").getObj());
            rsResultSet.updateString("QUOTATION_DATE",(String)getAttribute("QUOTATION_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsResultSet.updateInt("BUYER",(int)getAttribute("BUYER").getVal());
            rsResultSet.updateInt("TRANSPORT_MODE",(int)getAttribute("TRANSPORT_MODE").getVal());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsResultSet.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getDouble());
            rsResultSet.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsResultSet.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsResultSet.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateInt("SHIP_ID",(int)getAttribute("SHIP_ID").getVal());
            rsResultSet.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            //rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
            
            
            rsResultSet.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateString("PRINT_LINE_1",(String)getAttribute("PRINT_LINE_1").getObj());
            rsResultSet.updateString("PRINT_LINE_2",(String)getAttribute("PRINT_LINE_2").getObj());
            rsResultSet.updateString("IMPORT_LICENSE",(String)getAttribute("IMPORT_LICENSE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
            
            rsResultSet.updateInt("PAYMENT_CODE",getAttribute("PAYMENT_CODE").getInt());
            rsResultSet.updateInt("CR_DAYS",getAttribute("CR_DAYS").getInt());
            System.out.println(getAttribute("DEPT_ID").getInt());
            rsResultSet.updateInt("DEPT_ID",getAttribute("DEPT_ID").getInt());
            
            rsResultSet.updateString("PRICE_BASIS_TERM",(String)getAttribute("PRICE_BASIS_TERM").getObj());
            rsResultSet.updateString("DISCOUNT_TERM",(String)getAttribute("DISCOUNT_TERM").getObj());
            rsResultSet.updateString("EXCISE_TERM",(String)getAttribute("EXCISE_TERM").getObj());
            rsResultSet.updateString("ST_TERM",(String)getAttribute("ST_TERM").getObj());
            rsResultSet.updateString("PF_TERM",(String)getAttribute("PF_TERM").getObj());
            rsResultSet.updateString("FREIGHT_TERM",(String)getAttribute("FREIGHT_TERM").getObj());
            rsResultSet.updateString("OCTROI_TERM",(String)getAttribute("OCTROI_TERM").getObj());
            rsResultSet.updateString("FOB_TERM",(String)getAttribute("FOB_TERM").getObj());
            rsResultSet.updateString("CIE_TERM",(String)getAttribute("CIE_TERM").getObj());
            rsResultSet.updateString("INSURANCE_TERM",(String)getAttribute("INSURANCE_TERM").getObj());
            rsResultSet.updateString("TCC_TERM",(String)getAttribute("TCC_TERM").getObj());
            rsResultSet.updateString("CENVAT_TERM",(String)getAttribute("CENVAT_TERM").getObj());
            rsResultSet.updateString("DESPATCH_TERM",(String)getAttribute("DESPATCH_TERM").getObj());
            rsResultSet.updateString("SERVICE_TAX_TERM",(String)getAttribute("SERVICE_TAX_TERM").getObj());
            rsResultSet.updateString("OTHERS_TERM",(String)getAttribute("OTHERS_TERM").getObj());
            rsResultSet.updateString("COVERING_TEXT",(String)getAttribute("COVERING_TEXT").getObj());
            
            rsResultSet.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsResultSet.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsResultSet.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsResultSet.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsResultSet.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsResultSet.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            
            rsResultSet.updateString("PREMISES_TYPE",(String)getAttribute("PREMISES_TYPE").getObj());
            rsResultSet.updateString("SCOPE",(String)getAttribute("SCOPE").getObj());
            rsResultSet.updateString("SERVICE_PERIOD",(String)getAttribute("SERVICE_PERIOD").getObj());
            rsResultSet.updateString("SERVICE_FREQUENCY",(String)getAttribute("SERVICE_FREQUENCY").getObj());
            rsResultSet.updateString("CONTRACT_DETAILS",(String)getAttribute("CONTRACT_DETAILS").getObj());
            rsResultSet.updateString("SERVICE_REPORT",(String)getAttribute("SERVICE_REPORT").getObj());
            rsResultSet.updateString("ESI_TERMS",(String)getAttribute("ESI_TERMS").getObj());
            rsResultSet.updateString("TERMINATION_TERMS",(String)getAttribute("TERMINATION_TERMS").getObj());
            rsResultSet.updateString("FILE_TEXT",(String)getAttribute("FILE_TEXT").getObj());
            rsResultSet.updateString("AMOUNT_IN_WORDS",(String)getAttribute("AMOUNT_IN_WORDS").getObj());
            rsResultSet.updateBoolean("DIRECTOR_APPROVAL",getAttribute("DIRECTOR_APPROVAL").getBool());
            rsResultSet.updateBoolean("IMPORTED",getAttribute("IMPORTED").getBool());
            rsResultSet.updateBoolean("CANCELLED",false);
            
            
            rsResultSet.updateRow();
            
            System.out.println("Update PO Header entry end...");
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_PO_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+(String)getAttribute("PO_NO").getObj()+"' AND PO_TYPE="+POType);
            RevNo++;
            String RevDocNo=(String)getAttribute("PO_NO").getObj();
            System.out.println("Update PO Header History entry start...");
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHistory.updateLong("DOC_ID",DocID);
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateInt("PO_TYPE",POType);
            rsHistory.updateString("PO_REF",(String)getAttribute("PO_REF").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("SUPP_NAME",(String)getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("REF_A",(String)getAttribute("REF_A").getObj());
            rsHistory.updateString("REF_B",(String)getAttribute("REF_B").getObj());
            rsHistory.updateString("QUOTATION_NO",(String)getAttribute("QUOTATION_NO").getObj());
            rsHistory.updateString("QUOTATION_DATE",(String)getAttribute("QUOTATION_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String)getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateInt("BUYER",(int)getAttribute("BUYER").getVal());
            rsHistory.updateInt("TRANSPORT_MODE",(int)getAttribute("TRANSPORT_MODE").getVal());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("SUBJECT",(String)getAttribute("SUBJECT").getObj());
            rsHistory.updateInt("CURRENCY_ID",(int)getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE",getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT",getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("NET_AMOUNT",getAttribute("NET_AMOUNT").getVal());
            rsHistory.updateString("STATUS",(String)getAttribute("STATUS").getObj());
            rsHistory.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsHistory.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateInt("SHIP_ID",(int)getAttribute("SHIP_ID").getVal());
            rsHistory.updateBoolean("CANCELLED",getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            //rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //Now Custom Columns
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
            
            
            rsHistory.updateBoolean("IMPORT_CONCESS",getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateString("PRINT_LINE_1",(String)getAttribute("PRINT_LINE_1").getObj());
            rsHistory.updateString("PRINT_LINE_2",(String)getAttribute("PRINT_LINE_2").getObj());
            rsHistory.updateString("IMPORT_LICENSE",(String)getAttribute("IMPORT_LICENSE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
            
            rsHistory.updateInt("PAYMENT_CODE",getAttribute("PAYMENT_CODE").getInt());
            rsHistory.updateInt("CR_DAYS",getAttribute("CR_DAYS").getInt());
            System.out.println(getAttribute("DEPT_ID").getInt());
            rsHistory.updateInt("DEPT_ID", getAttribute("DEPT_ID").getInt());
            
            rsHistory.updateString("PRICE_BASIS_TERM",(String)getAttribute("PRICE_BASIS_TERM").getObj());
            rsHistory.updateString("DISCOUNT_TERM",(String)getAttribute("DISCOUNT_TERM").getObj());
            rsHistory.updateString("EXCISE_TERM",(String)getAttribute("EXCISE_TERM").getObj());
            rsHistory.updateString("ST_TERM",(String)getAttribute("ST_TERM").getObj());
            rsHistory.updateString("PF_TERM",(String)getAttribute("PF_TERM").getObj());
            rsHistory.updateString("FREIGHT_TERM",(String)getAttribute("FREIGHT_TERM").getObj());
            rsHistory.updateString("OCTROI_TERM",(String)getAttribute("OCTROI_TERM").getObj());
            rsHistory.updateString("FOB_TERM",(String)getAttribute("FOB_TERM").getObj());
            rsHistory.updateString("CIE_TERM",(String)getAttribute("CIE_TERM").getObj());
            rsHistory.updateString("INSURANCE_TERM",(String)getAttribute("INSURANCE_TERM").getObj());
            rsHistory.updateString("TCC_TERM",(String)getAttribute("TCC_TERM").getObj());
            rsHistory.updateString("CENVAT_TERM",(String)getAttribute("CENVAT_TERM").getObj());
            rsHistory.updateString("DESPATCH_TERM",(String)getAttribute("DESPATCH_TERM").getObj());
            rsHistory.updateString("SERVICE_TAX_TERM",(String)getAttribute("SERVICE_TAX_TERM").getObj());
            rsHistory.updateString("OTHERS_TERM",(String)getAttribute("OTHERS_TERM").getObj());
            rsHistory.updateString("COVERING_TEXT",(String)getAttribute("COVERING_TEXT").getObj());
            
            rsHistory.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsHistory.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsHistory.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsHistory.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsHistory.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsHistory.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            
            rsHistory.updateString("PREMISES_TYPE",(String)getAttribute("PREMISES_TYPE").getObj());
            rsHistory.updateString("SCOPE",(String)getAttribute("SCOPE").getObj());
            rsHistory.updateString("SERVICE_PERIOD",(String)getAttribute("SERVICE_PERIOD").getObj());
            rsHistory.updateString("SERVICE_FREQUENCY",(String)getAttribute("SERVICE_FREQUENCY").getObj());
            rsHistory.updateString("CONTRACT_DETAILS",(String)getAttribute("CONTRACT_DETAILS").getObj());
            rsHistory.updateString("SERVICE_REPORT",(String)getAttribute("SERVICE_REPORT").getObj());
            rsHistory.updateString("ESI_TERMS",(String)getAttribute("ESI_TERMS").getObj());
            rsHistory.updateString("TERMINATION_TERMS",(String)getAttribute("TERMINATION_TERMS").getObj());
            rsHistory.updateString("FILE_TEXT",(String)getAttribute("FILE_TEXT").getObj());
            rsHistory.updateString("AMOUNT_IN_WORDS",(String)getAttribute("AMOUNT_IN_WORDS").getObj());
            rsHistory.updateBoolean("DIRECTOR_APPROVAL",getAttribute("DIRECTOR_APPROVAL").getBool());
            rsHistory.updateBoolean("IMPORTED",getAttribute("IMPORTED").getBool());
            
            rsHistory.insertRow();
            System.out.println("Update PO Header History entry end...");
            
            //=== Delete old Records ======
            data.Execute("DELETE FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType);
            
            //====== Now turn of P.O. Items ======
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_DETAIL WHERE PO_NO='1' ");
            rsItem.first();
            System.out.println("Update PO Detail History entry start...");
            for(int i=1;i<=colPOItems.size();i++) {
                System.out.println("Update PO Detail History entry start ..." + i);
                clsPOItem ObjItem=(clsPOItem)colPOItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsItem.updateInt("SR_NO",i);
                rsItem.updateInt("PO_TYPE",POType);
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("SDML_SHADE",(String)ObjItem.getAttribute("SDML_SHADE").getObj());
                rsItem.updateString("VENDOR_SHADE",(String)ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsItem.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsItem.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsItem.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsItem.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsItem.updateString("PART_NO",(String)ObjItem.getAttribute("PART_NO").getObj());
                rsItem.updateString("EXCISE_TARRIF_NO",(String)ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsItem.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                
                if(AStatus.equals("F")) {
                    rsItem.updateDouble("PENDING_QTY",ObjItem.getAttribute("QTY").getVal());
                }
                
                rsItem.updateDouble("RECD_QTY",0);
                rsItem.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsItem.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getVal());
                rsItem.updateDouble("DISC_PER",ObjItem.getAttribute("DISC_PER").getVal());
                rsItem.updateDouble("DISC_AMT",ObjItem.getAttribute("DISC_AMT").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsItem.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsItem.updateString("REFERENCE",(String)ObjItem.getAttribute("REFERENCE").getObj());
                rsItem.updateString("DELIVERY_DATE",(String)ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsItem.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                //rsItem.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                //rsItem.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
                
                
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsItem.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsItem.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateInt("PO_TYPE",POType);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("SDML_SHADE",(String)ObjItem.getAttribute("SDML_SHADE").getObj());
                rsHDetail.updateString("VENDOR_SHADE",(String)ObjItem.getAttribute("VENDOR_SHADE").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE",(String)ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjItem.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateString("PART_NO",(String)ObjItem.getAttribute("PART_NO").getObj());
                rsHDetail.updateString("EXCISE_TARRIF_NO",(String)ObjItem.getAttribute("EXCISE_TARRIF_NO").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("TOLERANCE_LIMIT",ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("RATE",ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LANDED_RATE",ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("DISC_PER",ObjItem.getAttribute("DISC_PER").getVal());
                rsHDetail.updateDouble("DISC_AMT",ObjItem.getAttribute("DISC_AMT").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT",ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("INDENT_NO",(String)ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateInt("INDENT_SR_NO",(int)ObjItem.getAttribute("INDENT_SR_NO").getVal());
                rsHDetail.updateString("REFERENCE",(String)ObjItem.getAttribute("REFERENCE").getObj());
                rsHDetail.updateString("DELIVERY_DATE",(String)ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                //rsHDetail.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
                //rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
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
                
                
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS",ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("QUOT_ID",(String)ObjItem.getAttribute("QUOT_ID").getObj());
                rsHDetail.updateInt("QUOT_SR_NO",(int)ObjItem.getAttribute("QUOT_SR_NO").getVal());
                rsHDetail.updateInt("DEPT_ID",(int)ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                System.out.println("Update PO Detail History entry End ..." + i);
            }
            System.out.println("Update PO Detail entry end...");
            
            //====== Now turn of P.O. Items ======
            data.Execute("DELETE FROM D_PUR_PO_TERMS WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType);
            
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_PUR_PO_TERMS WHERE PO_NO='1' ");
            rsTerms.first();
            System.out.println("Update PO term entry start...");
            for(int i=1;i<=colPOTerms.size();i++) {
                clsPOTerms ObjTerm=(clsPOTerms)colPOTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsTerms.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsTerms.updateInt("SR_NO",i);
                rsTerms.updateInt("PO_TYPE",POType);
                rsTerms.updateString("TERM_TYPE",(String)ObjTerm.getAttribute("TERM_TYPE").getObj());
                rsTerms.updateInt("TERM_CODE",(int)ObjTerm.getAttribute("TERM_CODE").getVal());
                rsTerms.updateString("TERM_DESC",(String)ObjTerm.getAttribute("TERM_DESC").getObj());
                rsTerms.updateBoolean("CHANGED",true);
                rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();
                
                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO",RevNo);
                rsHTerms.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHTerms.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
                rsHTerms.updateInt("SR_NO",i);
                rsHTerms.updateInt("PO_TYPE",POType);
                rsHTerms.updateString("TERM_TYPE",(String)ObjTerm.getAttribute("TERM_TYPE").getObj());
                rsHTerms.updateInt("TERM_CODE",(int)ObjTerm.getAttribute("TERM_CODE").getVal());
                rsHTerms.updateString("TERM_DESC",(String)ObjTerm.getAttribute("TERM_DESC").getObj());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            System.out.println("Update PO term entry end...");
            
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=POType+20; //Refer Insert() method for explaination
            
            if(POType==8) {
                ObjFlow.ModuleID=46;
            }
            if(POType==9) {
                ObjFlow.ModuleID=153;
            }
            
            System.out.println("Update PO flow start...");
            if(ApprovalFlow.HierarchyUpdateNeeded(EITLERPGLOBAL.gCompanyID, ObjFlow.ModuleID, (String)getAttribute("PO_NO").getObj(),(int)getAttribute("HIERARCHY_ID").getVal(),OldHierarchy,EITLERPGLOBAL.gNewUserID,AStatus)) {
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("PO_NO").getObj()+"' AND MODULE_ID="+ObjFlow.ModuleID);
                
                ObjFlow.DocNo=(String)getAttribute("PO_NO").getObj();
                ObjFlow.From=(int)getAttribute("FROM").getVal();
                ObjFlow.To=(int)getAttribute("TO").getVal();
                ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
                ObjFlow.TableName="D_PUR_PO_HEADER";
                ObjFlow.IsCreator=true;
                ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
                ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
                ObjFlow.FieldName="PO_NO";
                
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
            } else {
                
                ObjFlow.DocNo=(String)getAttribute("PO_NO").getObj();
                ObjFlow.From=(int)getAttribute("FROM").getVal();
                ObjFlow.To=(int)getAttribute("TO").getVal();
                ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
                ObjFlow.TableName="D_PUR_PO_HEADER";
                ObjFlow.IsCreator=false;
                ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
                ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
                ObjFlow.FieldName="PO_NO";
                ObjFlow.DocDate=(String)getAttribute("PO_DATE").getObj();
                
                if(AStatus.equals("R")) {
                    //Remove the Rejected Flag First
                    //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                    //Remove Old Records from D_COM_DOC_DATA
                    //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                    
                    //ObjFlow.IsCreator=true;
                    ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                    ObjFlow.ExplicitSendTo=true;
                }
                
                //==== Handling Rejected Documents ==========//
                boolean IsRejected=getAttribute("REJECTED").getBool();
                
                if(IsRejected) {
                    //Remove the Rejected Flag First
                    data.Execute("UPDATE D_PUR_PO_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+ObjFlow.DocNo+"' AND PO_TYPE="+POType);
                    //Remove Old Records from D_COM_DOC_DATA
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+(ObjFlow.ModuleID)+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                    
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
            }
            System.out.println("Update PO completed bye...");
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
        String strSQL="",PONo="",IndentNo="";
        int CompanyID=0,IndentSrNo=0;
        double Qty=0;
        
        try {
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            PONo=(String)getAttribute("PO_NO").getObj();
            
            //Now give reverse effect to Indent Table
            strSQL="SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType;
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                IndentNo=rsTmp.getString("INDENT_NO");
                IndentSrNo=rsTmp.getInt("INDENT_SR_NO");
                Qty=rsTmp.getDouble("QTY");
                
                // Update GRN Received Qty
                data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY-"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+CompanyID+" AND INDENT_NO='"+IndentNo.trim()+"' AND SR_NO="+IndentSrNo+" ");
                
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
            String lDocNo=(String)getAttribute("PO_NO").getObj();
            String strSQL="";
            
            //First check that record is deletable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                
                //Give reverse effect to Stock
                //RevertStockEffect();
                
                data.Execute("DELETE FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+lCompanyID+" AND PO_NO='"+lDocNo.trim()+"' AND PO_TYPE="+POType);
                data.Execute("DELETE FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND PO_NO='"+lDocNo.trim()+"' AND PO_TYPE="+POType);
                
                LoadData(lCompanyID,POType);
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
    
    
    public Object getObject(int pCompanyID,String pPONo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND PO_NO='"+pPONo+"' AND PO_TYPE="+POType;
        clsPOGen ObjPO = new clsPOGen();
        ObjPO.Filter(strCondition,pCompanyID);
        return ObjPO;
    }
    
    public Object getObject(int pCompanyID,String pPONo,int pType) {
        POType=pType;
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND PO_NO='"+pPONo+"' AND PO_TYPE="+pType;
        clsPOGen ObjPO = new clsPOGen();
        ObjPO.Filter(strCondition,pCompanyID,pType);
        return ObjPO;
    }
    
    
    public Object getObject(int pCompanyID,String pPONo,String pURL,int pType) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND PO_NO='"+pPONo+"' AND PO_TYPE="+pType;
        clsPOGen ObjPO = new clsPOGen();
        ObjPO.LoadData(pCompanyID,pURL,pType);
        ObjPO.Filter(strCondition,pCompanyID,pURL,pType);
        return ObjPO;
    }
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_PO_HEADER " + pCondition +" ORDER BY PO_DATE";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if(rsResultSet.getRow()>0) {
                // (1.) strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+POType+" AND PO_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PO_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PO_DATE ";
                //strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+POType+" ";
                //(2.)rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return true; //(3.) return false;
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
            String strSql = "SELECT * FROM D_PUR_PO_HEADER " + pCondition +" ORDER BY PO_DATE";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+POType+" AND PO_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PO_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PO_DATE ";
                //strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+POType+" ";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                rsResultSet.first();
                setData(pType);
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID,String pURL,int pType) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_PO_HEADER " + pCondition +" ORDER BY PO_DATE";
            Conn=data.getConn(pURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                //strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+pType;
                strSql = "SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND PO_TYPE="+pType+" ORDER BY PO_DATE";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                rsResultSet.first();
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
        ResultSet rsItem,rsTmp;
        String PONo="";
        int CompanyID=0,ItemCounter=0,SrNo=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            setAttribute("PO_NO",rsResultSet.getString("PO_NO"));
            setAttribute("DOC_ID",rsResultSet.getLong("DOC_ID"));
            setAttribute("PO_DATE",rsResultSet.getString("PO_DATE"));
            setAttribute("PO_TYPE",rsResultSet.getInt("PO_TYPE"));
            setAttribute("PO_REF",rsResultSet.getString("PO_REF"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("SUPP_NAME",rsResultSet.getString("SUPP_NAME"));
            
            //----------------------------------------------------------//
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            //-----------------------------------------------------------//
            
            
            setAttribute("REF_A",rsResultSet.getString("REF_A"));
            setAttribute("REF_B",rsResultSet.getString("REF_B"));
            setAttribute("QUOTATION_NO",rsResultSet.getString("QUOTATION_NO"));
            setAttribute("QUOTATION_DATE",rsResultSet.getString("QUOTATION_DATE"));
            setAttribute("INQUIRY_NO",rsResultSet.getString("INQUIRY_NO"));
            setAttribute("INQUIRY_DATE",rsResultSet.getString("INQUIRY_DATE"));
            setAttribute("BUYER",rsResultSet.getInt("BUYER"));
            setAttribute("TRANSPORT_MODE",rsResultSet.getInt("TRANSPORT_MODE"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("SUBJECT",rsResultSet.getString("SUBJECT"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("NET_AMOUNT",rsResultSet.getDouble("NET_AMOUNT"));
            setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("ATTACHEMENT",rsResultSet.getBoolean("ATTACHEMENT"));
            setAttribute("ATTACHEMENT_PATH",rsResultSet.getString("ATTACHEMENT_PATH"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("SHIP_ID",rsResultSet.getInt("SHIP_ID"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
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
            
            
            setAttribute("IMPORT_CONCESS",rsResultSet.getBoolean("IMPORT_CONCESS"));
            setAttribute("PRINT_LINE_1",rsResultSet.getString("PRINT_LINE_1"));
            setAttribute("PRINT_LINE_2",rsResultSet.getString("PRINT_LINE_2"));
            setAttribute("IMPORT_LICENSE",rsResultSet.getString("IMPORT_LICENSE"));
            setAttribute("PAYMENT_TERM",rsResultSet.getString("PAYMENT_TERM"));
            
            setAttribute("PAYMENT_CODE",rsResultSet.getInt("PAYMENT_CODE"));
            setAttribute("CR_DAYS",rsResultSet.getInt("CR_DAYS"));
            setAttribute("DEPT_ID", rsResultSet.getInt("DEPT_ID"));
            
            setAttribute("PRICE_BASIS_TERM",rsResultSet.getString("PRICE_BASIS_TERM"));
            setAttribute("DISCOUNT_TERM",rsResultSet.getString("DISCOUNT_TERM"));
            setAttribute("EXCISE_TERM",rsResultSet.getString("EXCISE_TERM"));
            setAttribute("ST_TERM",rsResultSet.getString("ST_TERM"));
            setAttribute("PF_TERM",rsResultSet.getString("PF_TERM"));
            setAttribute("FREIGHT_TERM",rsResultSet.getString("FREIGHT_TERM"));
            setAttribute("OCTROI_TERM",rsResultSet.getString("OCTROI_TERM"));
            setAttribute("FOB_TERM",rsResultSet.getString("FOB_TERM"));
            setAttribute("CIE_TERM",rsResultSet.getString("CIE_TERM"));
            setAttribute("INSURANCE_TERM",rsResultSet.getString("INSURANCE_TERM"));
            setAttribute("TCC_TERM",rsResultSet.getString("TCC_TERM"));
            setAttribute("CENVAT_TERM",rsResultSet.getString("CENVAT_TERM"));
            setAttribute("DESPATCH_TERM",rsResultSet.getString("DESPATCH_TERM"));
            setAttribute("SERVICE_TAX_TERM",rsResultSet.getString("SERVICE_TAX_TERM"));
            setAttribute("OTHERS_TERM",rsResultSet.getString("OTHERS_TERM"));
            setAttribute("COVERING_TEXT",rsResultSet.getString("COVERING_TEXT"));
            setAttribute("CGST_TERM",rsResultSet.getString("CGST_TERM"));
            setAttribute("SGST_TERM",rsResultSet.getString("SGST_TERM"));
            setAttribute("IGST_TERM",rsResultSet.getString("IGST_TERM"));
            setAttribute("COMPOSITION_TERM",rsResultSet.getString("COMPOSITION_TERM"));
            setAttribute("RCM_TERM",rsResultSet.getString("RCM_TERM"));
            setAttribute("GST_COMPENSATION_CESS_TERM",rsResultSet.getString("GST_COMPENSATION_CESS_TERM"));
            setAttribute("PREMISES_TYPE",rsResultSet.getString("PREMISES_TYPE"));
            setAttribute("SCOPE",rsResultSet.getString("SCOPE"));
            setAttribute("SERVICE_PERIOD",rsResultSet.getString("SERVICE_PERIOD"));
            setAttribute("SERVICE_FREQUENCY",rsResultSet.getString("SERVICE_FREQUENCY"));
            setAttribute("CONTRACT_DETAILS",rsResultSet.getString("CONTRACT_DETAILS"));
            setAttribute("SERVICE_REPORT",rsResultSet.getString("SERVICE_REPORT"));
            setAttribute("ESI_TERMS",rsResultSet.getString("ESI_TERMS"));
            setAttribute("TERMINATION_TERMS",rsResultSet.getString("TERMINATION_TERMS"));
            setAttribute("FILE_TEXT",rsResultSet.getString("FILE_TEXT"));
            setAttribute("AMOUNT_IN_WORDS",rsResultSet.getString("AMOUNT_IN_WORDS"));
            setAttribute("DIRECTOR_APPROVAL",rsResultSet.getBoolean("DIRECTOR_APPROVAL"));
            setAttribute("IMPORTED",rsResultSet.getBoolean("IMPORTED"));
            
            
            colPOItems.clear();
            
            PONo=(String)getAttribute("PO_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            if(HistoryView) {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_DETAIL_H WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType+" AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType+" ORDER BY SR_NO");
            }
            rsItem.first();
            
            ItemCounter=0;
            
            while((!rsItem.isAfterLast())&&(rsItem.getRow()>0)) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsPOItem ObjItem=new clsPOItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("SDML_SHADE",rsItem.getString("SDML_SHADE"));
                ObjItem.setAttribute("VENDOR_SHADE",rsItem.getString("VENDOR_SHADE"));
                ObjItem.setAttribute("ITEM_DESC",UtilFunctions.getString(rsItem,"ITEM_DESC",""));
                ObjItem.setAttribute("HSN_SAC_CODE",UtilFunctions.getString(rsItem,"HSN_SAC_CODE",""));
                ObjItem.setAttribute("MAKE",rsItem.getString("MAKE"));
                ObjItem.setAttribute("PRICE_LIST_NO",rsItem.getString("PRICE_LIST_NO"));
                ObjItem.setAttribute("PART_NO",rsItem.getString("PART_NO"));
                ObjItem.setAttribute("EXCISE_TARRIF_NO",rsItem.getString("EXCISE_TARRIF_NO"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("TOLERANCE_LIMIT",rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("DEPT_ID",rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE",rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("RECD_QTY",rsItem.getDouble("RECD_QTY"));
                ObjItem.setAttribute("PENDING_QTY",rsItem.getDouble("PENDING_QTY"));
                ObjItem.setAttribute("DISC_PER",rsItem.getDouble("DISC_PER"));
                ObjItem.setAttribute("DISC_AMT",rsItem.getDouble("DISC_AMT"));
                ObjItem.setAttribute("TOTAL_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("INDENT_NO",rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO",rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("REFERENCE",rsItem.getString("REFERENCE"));
                ObjItem.setAttribute("DELIVERY_DATE",rsItem.getString("DELIVERY_DATE"));
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
                
                
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN",rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS",rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("QUOT_ID",rsItem.getString("QUOT_ID"));
                ObjItem.setAttribute("QUOT_SR_NO",rsItem.getInt("QUOT_SR_NO"));
                colPOItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            
            //====== Now fill up the total no. of amendments made for this PO======//
            setAttribute("AMEND_NO",0);
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT MAX(AMEND_SR_NO) AS AMENDNO FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                setAttribute("AMEND_NO",rsTmp.getInt("AMENDNO"));
            }
            //======== ============ ======== ========//
            
            
            colPOTerms.clear();
            
            PONo=(String)getAttribute("PO_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            if(HistoryView) {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_TERMS_H WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType+" AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_TERMS WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+POType+" ORDER BY SR_NO");
            }
            rsItem.first();
            
            ItemCounter=0;
            
            while((!rsItem.isAfterLast())&&(rsItem.getRow()>0)) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsPOTerms ObjItem=new clsPOTerms();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("TERM_TYPE",rsItem.getString("TERM_TYPE"));
                ObjItem.setAttribute("TERM_CODE",rsItem.getInt("TERM_CODE"));
                ObjItem.setAttribute("TERM_DESC",rsItem.getString("TERM_DESC"));
                colPOTerms.put(Integer.toString(ItemCounter),ObjItem);
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
        int ModuleID=0;
        
        try {
            if(HistoryView) {
                return false;
            }
            
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND PO_NO='"+pDocNo+"' AND (APPROVED=1) AND PO_TYPE="+POType;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                
                ModuleID=POType+20;
                
                if(POType==8) {
                    ModuleID=46;
                }
                if(POType==9) {
                    ModuleID=153;
                }
                //Change the Module ID
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+(ModuleID)+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        int ModuleID=0;
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND PO_NO='"+pDocNo+"' AND (APPROVED=1) AND PO_TYPE="+POType;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                
                ModuleID=POType+20;
                
                if(POType==8) {
                    ModuleID=46;
                }
                if(POType==9) {
                    ModuleID=153;
                }
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+(ModuleID)+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    public static boolean IsValidPONo(int pCompanyID,String pPONo,int pType) {
        Connection tmpConn;
        Statement tmpStmt=null;
        ResultSet rsTmp=null;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery("SELECT PO_NO FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"' AND PO_TYPE="+pType+" AND APPROVED=1");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return true;
            }
            else {
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return false;
            }
            
            
            
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static boolean IsValidPONo(String pPONo,int pType) {
        Connection tmpConn;
        Statement tmpStmt=null;
        ResultSet rsTmp=null;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery("SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_NO='"+pPONo+"' AND PO_TYPE="+pType+" AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return true;
            }
            else {
                
                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();
                
                return false;
            }
            
            
            
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public boolean setData(int pType) {
        Statement stItem,stTmp;
        ResultSet rsItem,rsTmp;
        String PONo="";
        int CompanyID=0,ItemCounter=0,SrNo=0;
        
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            setAttribute("PO_NO",rsResultSet.getString("PO_NO"));
            setAttribute("DOC_ID",rsResultSet.getLong("DOC_ID"));
            setAttribute("PO_DATE",rsResultSet.getString("PO_DATE"));
            setAttribute("PO_TYPE",rsResultSet.getInt("PO_TYPE"));
            setAttribute("PO_REF",rsResultSet.getString("PO_REF"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("SUPP_NAME",rsResultSet.getString("SUPP_NAME"));
            
            //------This Part Change------------//
            //--------------------------------------------------------------
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            //--------------------------------------------------------------
            
            setAttribute("REF_A",rsResultSet.getString("REF_A"));
            setAttribute("REF_B",rsResultSet.getString("REF_B"));
            setAttribute("QUOTATION_NO",rsResultSet.getString("QUOTATION_NO"));
            setAttribute("QUOTATION_DATE",rsResultSet.getString("QUOTATION_DATE"));
            setAttribute("INQUIRY_NO",rsResultSet.getString("INQUIRY_NO"));
            setAttribute("INQUIRY_DATE",rsResultSet.getString("INQUIRY_DATE"));
            setAttribute("BUYER",rsResultSet.getInt("BUYER"));
            setAttribute("TRANSPORT_MODE",rsResultSet.getInt("TRANSPORT_MODE"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("SUBJECT",rsResultSet.getString("SUBJECT"));
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE",rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("TOTAL_AMOUNT",rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("NET_AMOUNT",rsResultSet.getDouble("NET_AMOUNT"));
            setAttribute("STATUS",rsResultSet.getString("STATUS"));
            setAttribute("ATTACHEMENT",rsResultSet.getBoolean("ATTACHEMENT"));
            setAttribute("ATTACHEMENT_PATH",rsResultSet.getString("ATTACHEMENT_PATH"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("SHIP_ID",rsResultSet.getInt("SHIP_ID"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
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
            
            
            setAttribute("IMPORT_CONCESS",rsResultSet.getBoolean("IMPORT_CONCESS"));
            setAttribute("PRINT_LINE_1",rsResultSet.getString("PRINT_LINE_1"));
            setAttribute("PRINT_LINE_2",rsResultSet.getString("PRINT_LINE_2"));
            setAttribute("IMPORT_LICENSE",rsResultSet.getString("IMPORT_LICENSE"));
            setAttribute("PAYMENT_TERM",rsResultSet.getString("PAYMENT_TERM"));
            
            setAttribute("PAYMENT_CODE",rsResultSet.getInt("PAYMENT_CODE"));
            setAttribute("CR_DAYS",rsResultSet.getInt("CR_DAYS"));
            setAttribute("DEPT_ID",rsResultSet.getInt("DEPT_ID"));
            
            setAttribute("COVERING_TEXT",rsResultSet.getString("COVERING_TEXT"));
            setAttribute("PRICE_BASIS_TERM",rsResultSet.getString("PRICE_BASIS_TERM"));
            setAttribute("DISCOUNT_TERM",rsResultSet.getString("DISCOUNT_TERM"));
            setAttribute("EXCISE_TERM",rsResultSet.getString("EXCISE_TERM"));
            setAttribute("ST_TERM",rsResultSet.getString("ST_TERM"));
            setAttribute("PF_TERM",rsResultSet.getString("PF_TERM"));
            setAttribute("FREIGHT_TERM",rsResultSet.getString("FREIGHT_TERM"));
            setAttribute("OCTROI_TERM",rsResultSet.getString("OCTROI_TERM"));
            setAttribute("FOB_TERM",rsResultSet.getString("FOB_TERM"));
            setAttribute("CIE_TERM",rsResultSet.getString("CIE_TERM"));
            setAttribute("INSURANCE_TERM",rsResultSet.getString("INSURANCE_TERM"));
            setAttribute("TCC_TERM",rsResultSet.getString("TCC_TERM"));
            setAttribute("CENVAT_TERM",rsResultSet.getString("CENVAT_TERM"));
            setAttribute("DESPATCH_TERM",rsResultSet.getString("DESPATCH_TERM"));
            setAttribute("SERVICE_TAX_TERM",rsResultSet.getString("SERVICE_TAX_TERM"));
            setAttribute("OTHERS_TERM",rsResultSet.getString("OTHERS_TERM"));//ADD ON 02/08/2009
            setAttribute("CGST_TERM",rsResultSet.getString("CGST_TERM"));
            setAttribute("SGST_TERM",rsResultSet.getString("SGST_TERM"));
            setAttribute("IGST_TERM",rsResultSet.getString("IGST_TERM"));
            setAttribute("COMPOSITION_TERM",rsResultSet.getString("COMPOSITION_TERM"));
            setAttribute("RCM_TERM",rsResultSet.getString("RCM_TERM"));
            setAttribute("GST_COMPENSATION_CESS_TERM",rsResultSet.getString("GST_COMPENSATION_CESS_TERM"));
            setAttribute("PREMISES_TYPE",rsResultSet.getString("PREMISES_TYPE"));
            setAttribute("SCOPE",rsResultSet.getString("SCOPE"));
            setAttribute("SERVICE_PERIOD",rsResultSet.getString("SERVICE_PERIOD"));
            setAttribute("SERVICE_FREQUENCY",rsResultSet.getString("SERVICE_FREQUENCY"));
            setAttribute("CONTRACT_DETAILS",rsResultSet.getString("CONTRACT_DETAILS"));
            setAttribute("SERVICE_REPORT",rsResultSet.getString("SERVICE_REPORT"));
            setAttribute("ESI_TERMS",rsResultSet.getString("ESI_TERMS"));
            setAttribute("TERMINATION_TERMS",rsResultSet.getString("TERMINATION_TERMS"));
            setAttribute("FILE_TEXT",rsResultSet.getString("FILE_TEXT"));
            setAttribute("AMOUNT_IN_WORDS",rsResultSet.getString("AMOUNT_IN_WORDS"));
            setAttribute("DIRECTOR_APPROVAL",rsResultSet.getBoolean("DIRECTOR_APPROVAL"));
            setAttribute("IMPORTED",rsResultSet.getBoolean("IMPORTED"));
            
            colPOItems.clear();
            
            PONo=(String)getAttribute("PO_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+pType+" ORDER BY SR_NO");
            rsItem.first();
            
            ItemCounter=0;
            
            while((!rsItem.isAfterLast())&&(rsItem.getRow()>0)) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsPOItem ObjItem=new clsPOItem();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("SDML_SHADE",rsItem.getString("SDML_SHADE"));
                ObjItem.setAttribute("VENDOR_SHADE",rsItem.getString("VENDOR_SHADE"));
                ObjItem.setAttribute("ITEM_DESC",UtilFunctions.getString(rsItem,"ITEM_DESC",""));
                ObjItem.setAttribute("HSN_SAC_CODE",UtilFunctions.getString(rsItem,"HSN_SAC_CODE",""));
                ObjItem.setAttribute("MAKE",rsItem.getString("MAKE"));
                ObjItem.setAttribute("PART_NO",rsItem.getString("PART_NO"));
                ObjItem.setAttribute("EXCISE_TARRIF_NO",rsItem.getString("EXCISE_TARRIF_NO"));
                ObjItem.setAttribute("QTY",rsItem.getDouble("QTY"));
                ObjItem.setAttribute("TOLERANCE_LIMIT",rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("UNIT",rsItem.getInt("UNIT"));
                ObjItem.setAttribute("DEPT_ID",rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("RATE",rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE",rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("RECD_QTY",rsItem.getDouble("RECD_QTY"));
                ObjItem.setAttribute("PENDING_QTY",rsItem.getDouble("PENDING_QTY"));
                ObjItem.setAttribute("DISC_PER",rsItem.getDouble("DISC_PER"));
                ObjItem.setAttribute("DISC_AMT",rsItem.getDouble("DISC_AMT"));
                ObjItem.setAttribute("TOTAL_AMOUNT",rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT",rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("INDENT_NO",rsItem.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SR_NO",rsItem.getInt("INDENT_SR_NO"));
                ObjItem.setAttribute("REFERENCE",rsItem.getString("REFERENCE"));
                ObjItem.setAttribute("DELIVERY_DATE",rsItem.getString("DELIVERY_DATE"));
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
                
                
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN",rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS",rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("QUOT_ID",rsItem.getString("QUOT_ID"));
                ObjItem.setAttribute("QUOT_SR_NO",rsItem.getInt("QUOT_SR_NO"));
                
                colPOItems.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            
            //====== Now fill up the total no. of amendments made for this PO======//
            setAttribute("AMEND_NO",0);
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT MAX(AMEND_SR_NO) AS AMENDNO FROM D_PUR_AMEND_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+pType);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                setAttribute("AMEND_NO",rsTmp.getInt("AMENDNO"));
            }
            //======== ============ ======== ========//
            
            
            colPOTerms.clear();
            
            PONo=(String)getAttribute("PO_NO").getObj();
            
            stItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=stItem.executeQuery("SELECT * FROM D_PUR_PO_TERMS WHERE COMPANY_ID="+CompanyID+" AND PO_NO='"+PONo+"' AND PO_TYPE="+pType+" ORDER BY SR_NO");
            rsItem.first();
            
            ItemCounter=0;
            
            while((!rsItem.isAfterLast())&&(rsItem.getRow()>0)) {
                ItemCounter=ItemCounter+1;
                
                SrNo=rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsPOTerms ObjItem=new clsPOTerms();
                ObjItem.setAttribute("COMPANY_ID",rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("PO_NO",rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_TYPE",rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("SR_NO",rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("TERM_TYPE",rsItem.getString("TERM_TYPE"));
                ObjItem.setAttribute("TERM_CODE",rsItem.getInt("TERM_CODE"));
                ObjItem.setAttribute("TERM_DESC",rsItem.getString("TERM_DESC"));
                colPOTerms.put(Integer.toString(ItemCounter),ObjItem);
                rsItem.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getPOItemList(int pCompanyID,String pPONo,boolean pAllItems,int pType) {
        Connection tmpConn;
        Statement stTmp=null,stLot=null,stPO=null;
        ResultSet rsTmp=null,rsLot=null,rsPO=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        double ReceivedQty=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"' and PO_TYPE="+pType+" AND APPROVED=1");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                if(pAllItems) { //Retrieve All Items
                    strSQL="SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"' AND PO_TYPE="+pType+" ORDER BY ITEM_ID ";
                } else {
                    
                    
                    strSQL="SELECT B.*,SUM(M.QTY) ";
                    strSQL+="FROM ";
                    strSQL+="D_PUR_PO_HEADER A, ";
                    strSQL+="D_PUR_PO_DETAIL B ";
                    strSQL+="LEFT JOIN D_INV_MIR_DETAIL M ON (M.PO_NO=B.PO_NO AND M.PO_SR_NO=B.SR_NO AND M.PO_TYPE=B.PO_TYPE AND M.MIR_NO IN (SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE MIR_NO=M.MIR_NO AND MIR_TYPE=M.MIR_TYPE AND APPROVED=1 AND CANCELLED=0) ), ";
                    strSQL+="D_COM_DEPT_MASTER D ";
                    strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
                    strSQL+="A.PO_NO=B.PO_NO AND ";
                    strSQL+="A.PO_TYPE=B.PO_TYPE AND ";
                    strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
                    strSQL+="A.APPROVED=1 AND A.CANCELLED=0 AND ";
                    strSQL+="A.PO_NO='"+pPONo+"' AND ";
                    strSQL+="A.PO_TYPE="+pType+" AND ";
                    strSQL+="B.COMPANY_ID=D.COMPANY_ID AND B.DEPT_ID=D.DEPT_ID ";
                    strSQL+="GROUP BY B.PO_NO,B.PO_TYPE,B.SR_NO ";
                    strSQL+="HAVING IF(SUM(M.QTY) IS NULL,0,SUM(M.QTY))<B.QTY ";
                    
                    
                }
                
                
                stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp=stTmp.executeQuery(strSQL);
                rsTmp.first();
                
                Counter1=0;
                
                while(!rsTmp.isAfterLast()) {
                    Counter1++;
                    clsPOItem ObjItem=new clsPOItem();
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("PO_NO",rsTmp.getString("PO_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("PO_TYPE",rsTmp.getInt("PO_TYPE"));
                    ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjItem.setAttribute("SDML_SHADE",rsTmp.getString("SDML_SHADE"));
                    ObjItem.setAttribute("VENDOR_SHADE",rsTmp.getString("VENDOR_SHADE"));
                    ObjItem.setAttribute("ITEM_DESC",rsTmp.getString("ITEM_DESC"));
                    ObjItem.setAttribute("HSN_SAC_CODE",rsTmp.getString("HSN_SAC_CODE"));
                    ObjItem.setAttribute("MAKE",rsTmp.getString("MAKE"));
                    ObjItem.setAttribute("PRICE_LIST_NO",rsTmp.getString("PRICE_LIST_NO"));
                    ObjItem.setAttribute("PART_NO",rsTmp.getString("PART_NO"));
                    ObjItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                    ObjItem.setAttribute("PENDING_QTY",rsTmp.getDouble("PENDING_QTY"));
                    
                    ReceivedQty=data.getDoubleValueFromDB("SELECT SUM(B.QTY) AS SUMQTY FROM D_INV_MIR_HEADER A,D_INV_MIR_DETAIL B WHERE A.MIR_NO=B.MIR_NO AND A.MIR_TYPE=B.MIR_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.PO_NO='"+rsTmp.getString("PO_NO")+"' AND B.PO_TYPE="+rsTmp.getInt("PO_TYPE")+" AND B.PO_SR_NO="+rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("RECD_QTY",ReceivedQty);
                    
                    ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                    ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjItem.setAttribute("RATE",rsTmp.getDouble("RATE"));
                    ObjItem.setAttribute("DISC_PER",rsTmp.getDouble("DISC_PER"));
                    ObjItem.setAttribute("DISC_AMT",rsTmp.getDouble("DISC_AMT"));
                    ObjItem.setAttribute("TOTAL_AMOUNT",rsTmp.getDouble("TOTAL_AMOUNT"));
                    ObjItem.setAttribute("NET_AMOUNT",rsTmp.getDouble("NET_AMOUNT"));
                    ObjItem.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                    ObjItem.setAttribute("INDENT_SR_NO",rsTmp.getInt("INDENT_SR_NO"));
                    ObjItem.setAttribute("REFERENCE",rsTmp.getString("REFERENCE"));
                    ObjItem.setAttribute("DELIVERY_DATE",rsTmp.getString("DELIVERY_DATE"));
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
                    
                    ObjItem.setAttribute("COLUMN_11_ID",rsTmp.getInt("COLUMN_11_ID"));
                    ObjItem.setAttribute("COLUMN_11_FORMULA",rsTmp.getString("COLUMN_11_FORMULA"));
                    ObjItem.setAttribute("COLUMN_11_PER",rsTmp.getDouble("COLUMN_11_PER"));
                    ObjItem.setAttribute("COLUMN_11_AMT",rsTmp.getDouble("COLUMN_11_AMT"));
                    ObjItem.setAttribute("COLUMN_11_CAPTION",rsTmp.getString("COLUMN_11_CAPTION"));
                    ObjItem.setAttribute("COLUMN_12_ID",rsTmp.getInt("COLUMN_12_ID"));
                    ObjItem.setAttribute("COLUMN_12_FORMULA",rsTmp.getString("COLUMN_12_FORMULA"));
                    ObjItem.setAttribute("COLUMN_12_PER",rsTmp.getDouble("COLUMN_12_PER"));
                    ObjItem.setAttribute("COLUMN_12_AMT",rsTmp.getDouble("COLUMN_12_AMT"));
                    ObjItem.setAttribute("COLUMN_12_CAPTION",rsTmp.getString("COLUMN_12_CAPTION"));
                    ObjItem.setAttribute("COLUMN_13_ID",rsTmp.getInt("COLUMN_13_ID"));
                    ObjItem.setAttribute("COLUMN_13_FORMULA",rsTmp.getString("COLUMN_13_FORMULA"));
                    ObjItem.setAttribute("COLUMN_13_PER",rsTmp.getDouble("COLUMN_13_PER"));
                    ObjItem.setAttribute("COLUMN_13_AMT",rsTmp.getDouble("COLUMN_13_AMT"));
                    ObjItem.setAttribute("COLUMN_13_CAPTION",rsTmp.getString("COLUMN_13_CAPTION"));
                    ObjItem.setAttribute("COLUMN_14_ID",rsTmp.getInt("COLUMN_14_ID"));
                    ObjItem.setAttribute("COLUMN_14_FORMULA",rsTmp.getString("COLUMN_14_FORMULA"));
                    ObjItem.setAttribute("COLUMN_14_PER",rsTmp.getDouble("COLUMN_14_PER"));
                    ObjItem.setAttribute("COLUMN_14_AMT",rsTmp.getDouble("COLUMN_14_AMT"));
                    ObjItem.setAttribute("COLUMN_14_CAPTION",rsTmp.getString("COLUMN_14_CAPTION"));
                    ObjItem.setAttribute("COLUMN_15_ID",rsTmp.getInt("COLUMN_15_ID"));
                    ObjItem.setAttribute("COLUMN_15_FORMULA",rsTmp.getString("COLUMN_15_FORMULA"));
                    ObjItem.setAttribute("COLUMN_15_PER",rsTmp.getDouble("COLUMN_15_PER"));
                    ObjItem.setAttribute("COLUMN_15_AMT",rsTmp.getDouble("COLUMN_15_AMT"));
                    ObjItem.setAttribute("COLUMN_15_CAPTION",rsTmp.getString("COLUMN_15_CAPTION"));
                    
                    ObjItem.setAttribute("COLUMN_16_ID",rsTmp.getInt("COLUMN_16_ID"));                    ObjItem.setAttribute("COLUMN_16_FORMULA",rsTmp.getString("COLUMN_6_FORMULA"));
                    ObjItem.setAttribute("COLUMN_16_PER",rsTmp.getDouble("COLUMN_16_PER"));
                    ObjItem.setAttribute("COLUMN_16_AMT",rsTmp.getDouble("COLUMN_16_AMT"));
                    ObjItem.setAttribute("COLUMN_16_CAPTION",rsTmp.getString("COLUMN_16_CAPTION"));
                    ObjItem.setAttribute("COLUMN_17_ID",rsTmp.getInt("COLUMN_17_ID"));
                    ObjItem.setAttribute("COLUMN_17_FORMULA",rsTmp.getString("COLUMN_17_FORMULA"));
                    ObjItem.setAttribute("COLUMN_17_PER",rsTmp.getDouble("COLUMN_17_PER"));
                    ObjItem.setAttribute("COLUMN_17_AMT",rsTmp.getDouble("COLUMN_17_AMT"));
                    ObjItem.setAttribute("COLUMN_17_CAPTION",rsTmp.getString("COLUMN_17_CAPTION"));
                    ObjItem.setAttribute("COLUMN_18_ID",rsTmp.getInt("COLUMN_18_ID"));
                    ObjItem.setAttribute("COLUMN_18_FORMULA",rsTmp.getString("COLUMN_18_FORMULA"));
                    ObjItem.setAttribute("COLUMN_18_PER",rsTmp.getDouble("COLUMN_18_PER"));
                    ObjItem.setAttribute("COLUMN_18_AMT",rsTmp.getDouble("COLUMN_18_AMT"));
                    ObjItem.setAttribute("COLUMN_18_CAPTION",rsTmp.getString("COLUMN_18_CAPTION"));
                    ObjItem.setAttribute("COLUMN_19_ID",rsTmp.getInt("COLUMN_19_ID"));
                    ObjItem.setAttribute("COLUMN_19_FORMULA",rsTmp.getString("COLUMN_19_FORMULA"));
                    ObjItem.setAttribute("COLUMN_19_PER",rsTmp.getDouble("COLUMN_19_PER"));
                    ObjItem.setAttribute("COLUMN_19_AMT",rsTmp.getDouble("COLUMN_19_AMT"));
                    ObjItem.setAttribute("COLUMN_19_CAPTION",rsTmp.getString("COLUMN_19_CAPTION"));
                    ObjItem.setAttribute("COLUMN_20_ID",rsTmp.getInt("COLUMN_20_ID"));
                    ObjItem.setAttribute("COLUMN_20_FORMULA",rsTmp.getString("COLUMN_20_FORMULA"));
                    ObjItem.setAttribute("COLUMN_20_PER",rsTmp.getDouble("COLUMN_20_PER"));
                    ObjItem.setAttribute("COLUMN_20_AMT",rsTmp.getDouble("COLUMN_20_AMT"));
                    ObjItem.setAttribute("COLUMN_20_CAPTION",rsTmp.getString("COLUMN_20_CAPTION"));
                    
                    ObjItem.setAttribute("COLUMN_21_ID",rsTmp.getInt("COLUMN_21_ID"));
                    ObjItem.setAttribute("COLUMN_21_FORMULA",rsTmp.getString("COLUMN_21_FORMULA"));
                    ObjItem.setAttribute("COLUMN_21_PER",rsTmp.getDouble("COLUMN_21_PER"));
                    ObjItem.setAttribute("COLUMN_21_AMT",rsTmp.getDouble("COLUMN_21_AMT"));
                    ObjItem.setAttribute("COLUMN_21_CAPTION",rsTmp.getString("COLUMN_21_CAPTION"));
                    
                    
                    //Put into list
                    List.put(Integer.toString(Counter1),ObjItem);
                    
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            stPO.close();
            rsTmp.close();
            rsLot.close();
            rsPO.close();
            
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    //private boolean ValidateSupplier() {
    public boolean ValidateSupplier() {
        Statement stTmp;
        ResultSet rsTmp;
        
        int CompanyID=(int)getAttribute("COMPANY_ID").getVal();
        String SuppID=(String)getAttribute("SUPP_ID").getObj();        
        String SuppName=getAttribute("SUPP_NAME").getString();
        
        //Do not validate supplier in case of capital and contract PO
        //if(SuppID.trim().equals("000000")&&(!SuppName.trim().equals("")))
        //{
        //   return true;
        //}
        System.out.println("Update PO validate supplier 111...");
        if(SuppID.trim().equals("000000")) {
            LastError="Supplier code is not valid";
            return false;
        }
        
        System.out.println("Update PO validate supplier 222...");
        java.sql.Date CurrentDate=null;
        
        java.sql.Date FromRegDate=null;
        java.sql.Date ToRegDate=null;
        
        String strFromDate="",strToDate="",ApprovedDate="";
        
        try {
            
            //First check that Supplier is One time
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT ONETIME_SUPPLIER,FROM_DATE_REG,APPROVED_DATE,TO_DATE_REG FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+CompanyID+" AND SUPPLIER_CODE='"+SuppID+"' AND ONETIME_SUPPLIER=1");
            rsTmp.first();
            System.out.println("Update PO validate supplier 333...");
            if(rsTmp.getRow()>0) {
                CurrentDate=java.sql.Date.valueOf(EITLERPGLOBAL.getCurrentDateDB());
                
                strFromDate=UtilFunctions.getString(rsTmp,"FROM_DATE_REG","");
                System.out.println(strFromDate);
                strToDate=UtilFunctions.getString(rsTmp,"TO_DATE_REG","");
                System.out.println(strToDate);
                ApprovedDate=UtilFunctions.getString(rsTmp,"APPROVED_DATE","0000-00-00");
                System.out.println("Update PO validate supplier 444...");
                if(!strFromDate.equals("")&&!strToDate.equals("")&&!strToDate.equals("0000-00-00")&&!strFromDate.equals("0000-00-00")) {
                    FromRegDate=java.sql.Date.valueOf(strFromDate);
                    ToRegDate=java.sql.Date.valueOf(strToDate);
                }
                System.out.println("Update PO validate supplier 555...");
                if(rsTmp.getBoolean("ONETIME_SUPPLIER")) {
                    //Check that any P.O. is made
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    rsTmp=stTmp.executeQuery("SELECT COUNT(*) AS THECOUNT FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+CompanyID+" AND SUPP_ID='"+SuppID+"' AND CANCELLED=0 AND APPROVED=1 AND PO_DATE >='"+ApprovedDate+"'");
                    rsTmp.first();
                    System.out.println("Update PO validate supplier 666...");
                    if(rsTmp.getRow()>0) {
                        if(rsTmp.getInt("THECOUNT")>0) {
                            LastError="One P.O. is made for this supplier. Cannot make another P.O. as it is one time supplier.";
                            System.out.println("Update PO validate supplier 777...");
                            return false;
                        }
                    }
                }
                
                //if((!FromRegDate.equals(null)) && (!ToRegDate.equals(null))){
                    if(FromRegDate!=null && ToRegDate!=null){               //added modified condition of above line which is commented and gives NPE on 14/03/2019 bcoz without above line inner condition throws NPE
                    if(EITLERPGLOBAL.isDate(strFromDate)&&EITLERPGLOBAL.isDate(strToDate)) {
                        if((CurrentDate.after(FromRegDate)||CurrentDate.compareTo(FromRegDate)==0)&&(CurrentDate.before(ToRegDate)||CurrentDate.compareTo(ToRegDate)==0))
                        {    }
                        else {
                            System.out.println("Update PO validate supplier 888...");
                            LastError="This supplier is valid for only "+EITLERPGLOBAL.formatDate(strFromDate)+" to "+EITLERPGLOBAL.formatDate(strToDate);
                            return false;
                        }
                    }
                }
            }
            System.out.println("Update PO validate supplier 999...");
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("Update PO validate supplier 101010...");
            return false;
        }
    }
    
    public static String getLastPO(int pCompanyID,int pType,String pSuppID,String dbURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        
        try {
            
            tmpConn=data.getConn(dbURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            strSQL="SELECT PO_NO FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+"  AND SUPP_ID='"+pSuppID+"'  AND APPROVED=1 AND PO_TYPE="+pType+" AND CANCELLED=0 ORDER BY PO_DATE DESC ";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastPONo=rsTmp.getString("PO_NO");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return LastPONo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getLastPOEx(int pCompanyID,int pType,String pSuppID,String pItemID,String dbURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        
        try {
            
            tmpConn=data.getConn(dbURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            if(!pSuppID.trim().equals("")&&!pItemID.trim().equals("")) {
                strSQL="SELECT D_PUR_PO_HEADER.PO_NO AS LAST_PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+"  AND D_PUR_PO_HEADER.SUPP_ID='"+pSuppID+"' AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_HEADER.PO_TYPE="+pType+" ORDER BY D_PUR_PO_HEADER.PO_DATE DESC ";
            }
            
            if(!pSuppID.trim().equals("")&&pItemID.trim().equals("")) {
                strSQL="SELECT D_PUR_PO_HEADER.PO_NO AS LAST_PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+"  AND D_PUR_PO_HEADER.SUPP_ID='"+pSuppID+"' AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_HEADER.PO_TYPE="+pType+" ORDER BY D_PUR_PO_HEADER.PO_DATE DESC ";
            }
            
            if(pSuppID.trim().equals("")&&!pItemID.trim().equals("")) {
                strSQL="SELECT D_PUR_PO_HEADER.PO_NO AS LAST_PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+"  AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_HEADER.PO_TYPE="+pType+" ORDER BY D_PUR_PO_HEADER.PO_DATE DESC ";
            }
            
            if(pSuppID.trim().equals("")&&pItemID.trim().equals("")) {
                strSQL="SELECT D_PUR_PO_HEADER.PO_NO AS LAST_PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_HEADER.PO_TYPE="+pType+" ORDER BY D_PUR_PO_HEADER.PO_DATE DESC ";
            }
            
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastPONo=rsTmp.getString("LAST_PO_NO");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return LastPONo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    
    public static String getLastPO(int pCompanyID,int pDeptID,String pItemID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            strSQL="SELECT DISTINCT(D_PUR_PO_HEADER.PO_NO) AS PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL,D_COM_USER_MASTER WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_HEADER.CREATED_BY=D_COM_USER_MASTER.USER_ID AND D_PUR_PO_HEADER.COMPANY_ID=D_COM_USER_MASTER.COMPANY_ID AND D_COM_USER_MASTER.DEPT_ID="+pDeptID+" AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_PUR_PO_HEADER.PO_DATE DESC";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastPONo=rsTmp.getString("PO_NO");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return LastPONo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getLastPObyItem(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            strSQL="SELECT DISTINCT(D_PUR_PO_HEADER.PO_NO) AS PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' AND D_PUR_PO_DETAIL.QTY>0 ORDER BY D_PUR_PO_HEADER.PO_DATE DESC,D_PUR_PO_HEADER.PO_NO DESC LIMIT 1";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastPONo=rsTmp.getString("PO_NO");
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return LastPONo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getLastPObyItemByDate(int pCompanyID,String pItemID,String pBeforeDate,String PONo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            strSQL="SELECT DISTINCT(D_PUR_PO_HEADER.PO_NO) AS PO_NO FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' AND D_PUR_PO_HEADER.PO_DATE<='"+pBeforeDate+"' AND D_PUR_PO_HEADER.PO_NO<>'"+PONo+"' ORDER BY D_PUR_PO_HEADER.PO_DATE DESC,D_PUR_PO_HEADER.PO_NO DESC LIMIT 1";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastPONo=rsTmp.getString("PO_NO");
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return LastPONo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getLastPObyItem(int pCompanyID,String pItemID,String pGRNNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        String MIRNo="";
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+pGRNNo+"' AND ITEM_ID='"+pItemID+"' ";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                LastPONo=rsTmp.getString("PO_NO");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return LastPONo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getLastPO(int pCompanyID,String pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT PO_NO FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVED=1 AND CANCELLED=0 AND SUPP_ID='"+pSuppID+"' ORDER BY PO_DATE DESC";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastPONo=rsTmp.getString("PO_NO");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            
            return LastPONo;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    
    public static HashMap getLastFivePOs(int pCompanyID,int pDeptID,String pItemID,boolean pApproved) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String LastPONo="";
        HashMap List=new HashMap();
        int Counter=0;
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            //strSQL="SELECT DISTINCT(D_PUR_PO_HEADER.PO_NO) AS PO_NO,D_PUR_PO_HEADER.PO_TYPE FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL,D_COM_USER_MASTER WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_HEADER.CREATED_BY=D_COM_USER_MASTER.USER_ID AND D_PUR_PO_HEADER.COMPANY_ID=D_COM_USER_MASTER.COMPANY_ID AND D_COM_USER_MASTER.DEPT_ID="+pDeptID+" AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_PUR_PO_HEADER.PO_DATE DESC";
            if(pApproved) {
                strSQL="SELECT DISTINCT(D_PUR_PO_HEADER.PO_NO) AS PO_NO,D_PUR_PO_HEADER.PO_TYPE FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_PUR_PO_HEADER.PO_DATE DESC";
            }
            else {
                strSQL="SELECT DISTINCT(D_PUR_PO_HEADER.PO_NO) AS PO_NO,D_PUR_PO_HEADER.PO_TYPE FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_HEADER.CANCELLED=0 AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' ORDER BY D_PUR_PO_HEADER.PO_DATE DESC";
            }
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()&&Counter<=5) {
                    LastPONo=rsTmp.getString("PO_NO");
                    int POType=rsTmp.getInt("PO_TYPE");
                    
                    clsPOGen tmpObj=new clsPOGen();
                    tmpObj.POType=POType;
                    tmpObj.LoadData(pCompanyID,POType);
                    
                    clsPOGen ObjPO=new clsPOGen();
                    ObjPO=(clsPOGen)tmpObj.getObject(pCompanyID,LastPONo,POType);
                    
                    Counter++;
                    List.put(Integer.toString(Counter),ObjPO);
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    public static double getPendingQty(int pCompanyID,int pDeptID,String pItemID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        double PendingQty=0;
        
        try {
            
            //Below is old working Query
            //strSQL="SELECT SUM(D_PUR_PO_DETAIL.QTY-D_PUR_PO_DETAIL.RECD_QTY) AS TOTAL_QTY FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL,D_COM_DEPT_MASTER WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_DETAIL.RECD_QTY<QTY AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_DETAIL.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_PUR_PO_DETAIL.DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_PUR_PO_DETAIL.DEPT_ID="+pDeptID+" AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0";
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            strSQL="SELECT (B.QTY-IF(SUM(M.QTY) IS NULL,0,SUM(M.QTY))-IF(SUM(J.QTY) IS NULL,0,SUM(J.QTY))) AS PENDING_QTY,B.QTY ";
            strSQL+="FROM ";
            strSQL+="D_PUR_PO_HEADER A, ";
            strSQL+="D_PUR_PO_DETAIL B ";
            strSQL+="LEFT JOIN D_INV_MIR_DETAIL M ON (M.PO_NO=B.PO_NO AND M.PO_SR_NO=B.SR_NO AND M.PO_TYPE=B.PO_TYPE AND M.MIR_NO IN (SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE MIR_NO=M.MIR_NO AND MIR_TYPE=M.MIR_TYPE AND APPROVED=1 AND CANCELLED=0) ) ";
            strSQL+="LEFT JOIN D_INV_JOB_DETAIL J ON (J.PO_NO=B.PO_NO AND J.PO_SR_NO=B.SR_NO AND J.JOB_NO IN (SELECT JOB_NO FROM D_INV_JOB_HEADER WHERE JOB_NO=J.JOB_NO AND APPROVED=1 AND CANCELLED=0) ), ";
            strSQL+="D_COM_DEPT_MASTER D ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.PO_NO=B.PO_NO AND ";
            strSQL+="A.PO_TYPE=B.PO_TYPE AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
            strSQL+="A.APPROVED=1 AND A.CANCELLED=0 AND ";
            strSQL+="B.COMPANY_ID=D.COMPANY_ID AND B.DEPT_ID=D.DEPT_ID ";
            strSQL+="AND B.ITEM_ID='"+pItemID+"' ";
            strSQL+="AND B.DEPT_ID="+pDeptID+" ";
            strSQL+="AND A.PO_DATE>='2006-04-01' ";
            strSQL+="GROUP BY B.PO_NO,B.PO_TYPE,B.SR_NO ";
            strSQL+="HAVING ( IF(SUM(M.QTY) IS NULL,0,SUM(M.QTY))+IF(SUM(J.QTY) IS NULL,0,SUM(J.QTY)) ) <B.QTY ";
            
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    PendingQty+=rsTmp.getDouble("PENDING_QTY");
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PendingQty;
        }
        catch(Exception e) {
            return PendingQty;
        }
    }
    
    
    public static double getTotalPendingQty(int pCompanyID,String pItemID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        double PendingQty=0;
        
        try {
            
            //Below is old working Query
            //strSQL="SELECT SUM(D_PUR_PO_DETAIL.QTY-D_PUR_PO_DETAIL.RECD_QTY) AS TOTAL_QTY FROM D_PUR_PO_HEADER,D_PUR_PO_DETAIL,D_COM_DEPT_MASTER WHERE D_PUR_PO_HEADER.COMPANY_ID=D_PUR_PO_DETAIL.COMPANY_ID AND D_PUR_PO_HEADER.PO_NO=D_PUR_PO_DETAIL.PO_NO AND D_PUR_PO_HEADER.PO_TYPE=D_PUR_PO_DETAIL.PO_TYPE AND D_PUR_PO_HEADER.COMPANY_ID="+pCompanyID+" AND D_PUR_PO_DETAIL.RECD_QTY<QTY AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_DETAIL.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_PUR_PO_DETAIL.DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_PUR_PO_DETAIL.DEPT_ID="+pDeptID+" AND D_PUR_PO_DETAIL.ITEM_ID='"+pItemID+"' AND D_PUR_PO_HEADER.APPROVED=1 AND D_PUR_PO_HEADER.CANCELLED=0";
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            strSQL="SELECT (B.QTY- IF(SUM(M.QTY) IS NULL,0,SUM(M.QTY))-IF(SUM(J.QTY) IS NULL,0,SUM(J.QTY)) ) AS PENDING_QTY,B.QTY ";
            strSQL+="FROM ";
            strSQL+="D_PUR_PO_HEADER A, ";
            strSQL+="D_PUR_PO_DETAIL B ";
            strSQL+="LEFT JOIN D_INV_MIR_DETAIL M ON (M.PO_NO=B.PO_NO AND M.PO_SR_NO=B.SR_NO AND M.PO_TYPE=B.PO_TYPE AND M.MIR_NO IN (SELECT MIR_NO FROM D_INV_MIR_HEADER WHERE MIR_NO=M.MIR_NO AND MIR_TYPE=M.MIR_TYPE AND APPROVED=1 AND CANCELLED=0) ) ";
            strSQL+="LEFT JOIN D_INV_JOB_DETAIL J ON (J.PO_NO=B.PO_NO AND J.PO_SR_NO=B.SR_NO AND J.JOB_NO IN (SELECT JOB_NO FROM D_INV_JOB_HEADER WHERE JOB_NO=J.JOB_NO AND APPROVED=1 AND CANCELLED=0) ), ";
            strSQL+="D_COM_DEPT_MASTER D ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.PO_NO=B.PO_NO AND ";
            strSQL+="A.PO_TYPE=B.PO_TYPE AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
            strSQL+="A.APPROVED=1 AND A.CANCELLED=0 AND ";
            strSQL+="B.COMPANY_ID=D.COMPANY_ID AND B.DEPT_ID=D.DEPT_ID ";
            strSQL+="AND B.ITEM_ID='"+pItemID+"' ";
            strSQL+="AND A.PO_DATE>='2006-04-01' ";
            strSQL+="GROUP BY B.PO_NO,B.PO_TYPE,B.SR_NO ";
            strSQL+="HAVING ( IF(SUM(M.QTY) IS NULL,0,SUM(M.QTY))+IF(SUM(J.QTY) IS NULL,0,SUM(J.QTY)) ) <B.QTY ";
            
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    PendingQty+=rsTmp.getDouble("PENDING_QTY");
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PendingQty;
        }
        catch(Exception e) {
            return PendingQty;
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
        int ModuleID=0;
        
        
        try {
            ModuleID=pType+20;
            
            if(pType==8) {
                ModuleID=46;
            }
            if(pType==9) {
                ModuleID=153;
            }
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_PUR_PO_HEADER.PO_NO,D_PUR_PO_HEADER.PO_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_PO_HEADER,D_COM_DOC_DATA WHERE D_PUR_PO_HEADER.PO_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_PO_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_PO_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_PO_HEADER.PO_TYPE="+pType+" AND MODULE_ID="+(ModuleID)+" ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_PUR_PO_HEADER.PO_NO,D_PUR_PO_HEADER.PO_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_PO_HEADER,D_COM_DOC_DATA WHERE D_PUR_PO_HEADER.PO_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_PO_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_PO_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_PO_HEADER.PO_TYPE="+pType+" AND MODULE_ID="+(ModuleID)+" ORDER BY D_PUR_PO_HEADER.PO_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_PUR_PO_HEADER.PO_NO,D_PUR_PO_HEADER.PO_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_PO_HEADER,D_COM_DOC_DATA WHERE D_PUR_PO_HEADER.PO_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_PO_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_PO_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_PO_HEADER.PO_TYPE="+pType+" AND MODULE_ID="+(ModuleID)+" ORDER BY D_PUR_PO_HEADER.PO_NO";
            }
                        
            //strSQL="SELECT D_PUR_PO_HEADER.PO_NO,D_PUR_PO_HEADER.PO_DATE FROM D_PUR_PO_HEADER,D_COM_DOC_DATA WHERE D_PUR_PO_HEADER.PO_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_PO_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_PO_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_PUR_PO_HEADER.PO_TYPE="+pType+" AND MODULE_ID="+(ModuleID);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("PO_DATE"),FinYearFrom)) {
                    Counter=Counter+1;
                    clsPOGen ObjDoc=new clsPOGen();
                    
                    //------------- Header Fields --------------------//
                    ObjDoc.setAttribute("PO_NO",rsTmp.getString("PO_NO"));
                    ObjDoc.setAttribute("PO_DATE",rsTmp.getString("PO_DATE"));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_PO_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pDocNo+"' AND PO_TYPE="+POType);
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
    
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo,int pPOType) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_PO_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+pDocNo+"' AND PO_TYPE="+pPOType);
            
            while(rsTmp.next()) {
                clsPOGen ObjPO=new clsPOGen();
                
                ObjPO.setAttribute("PO_NO",rsTmp.getString("PO_NO"));
                ObjPO.setAttribute("PO_DATE",rsTmp.getString("PO_DATE"));
                ObjPO.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjPO.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjPO.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjPO.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjPO.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjPO);
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
    
    
    public static int getPOType(int pCompanyID,String pPONo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        int POType=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PO_TYPE FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                POType=rsTmp.getInt("PO_TYPE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return POType;
        }
        catch(Exception e) {
            return POType;
            
        }
    }
    
    
    public static boolean CanCancel(int pCompanyID,String pPONo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT PO_NO FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Now check every item for received qty.
                rsTmp=data.getResult("SELECT PO_NO FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"' AND RECD_QTY>0 ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Can not cancel this PO. first MIR should be cancelled.
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
    
    
    public static boolean CancelPO(int pCompanyID,String pPONo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pPONo)) {
                
                boolean ApprovedPO=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedPO=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedPO) {
                    //First Load PO Line.
                    rsTmp=data.getResult("SELECT * FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        while(!rsTmp.isAfterLast()) {
                            String IndentNo=rsTmp.getString("INDENT_NO");
                            int IndentSrNo=rsTmp.getInt("INDENT_SR_NO");
                            String QuotID=rsTmp.getString("QUOT_ID");
                            int QuotSrNo=rsTmp.getInt("QUOT_SR_NO");
                            
                            
                            if(IndentNo!=null) {
                                if(!IndentNo.trim().equals("")&&(IndentSrNo>0)) {
                                    double Qty=rsTmp.getDouble("QTY");
                                    
                                    //Now Update the Indent Table
                                    data.Execute("UPDATE D_INV_INDENT_DETAIL SET PO_QTY=PO_QTY-"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo);
                                }
                            }
                            
                            if(QuotID!=null) {
                                if(!QuotID.trim().equals("")&&(QuotSrNo>0)) {
                                    double Qty=rsTmp.getDouble("QTY");
                                    
                                    //Now Update the Indent Table
                                    data.Execute("UPDATE D_PUR_QUOT_DETAIL SET PO_QTY=PO_QTY-"+Qty+",BAL_QTY=QTY-PO_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+QuotID+"' AND SR_NO="+QuotSrNo);
                                }
                            }
                            
                            rsTmp.next();
                        }
                    }
                    
                }
                else {
                    int POType=getPOType(pCompanyID, pPONo);
                    int ModuleID=POType+20;
                    
                    if(POType==8) {
                        ModuleID=46;
                    }
                    if(POType==9) {
                        ModuleID=153;
                    }
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPONo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_PO_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pPONo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    public static boolean exportReportToFile(int pCompanyID,String pPONo,int POType,String FileName) {
        
        return true;
    }
    
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT PO_NO,APPROVED,CANCELLED FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pDocNo+"'");
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
    
    
    public static String getDocStatus(int pCompanyID,String pDocNo,String dbURL) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT PO_NO,APPROVED,CANCELLED FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+pCompanyID+" AND PO_NO='"+pDocNo+"'",dbURL);
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
    
    
}
