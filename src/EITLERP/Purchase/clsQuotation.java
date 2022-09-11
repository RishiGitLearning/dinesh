/*
 * clsQuotation.java
 *
 * Created on May 12, 2004, 1:53 PM
 */

package EITLERP.Purchase;

import java.util.*;
import java.sql.*;
import EITLERP.*;
import org.nfunk.jep.*;
import EITLERP.Stores.*;


/**
 *
 * @author  jadave
 * @version
 */

public class clsQuotation {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    //QuotationItem Collection
    public HashMap colQuotationItems;
    public HashMap colQuotTerms=new HashMap();
    
    private JEP myParser=new JEP();
    private boolean Updating=false;
    private boolean Updating_H=false;
    private boolean DoNotEvaluate=false;
    
    private HashMap colHeaderColumns=new HashMap();
    private HashMap colLineColumns=new HashMap();
    private HashMap colHeaderVariables=new HashMap();
    
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
    
    
    /** Creates new clsQuotation */
    public clsQuotation() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("QUOT_ID", new Variant(""));
        props.put("QUOT_NO", new Variant(""));
        props.put("QUOT_DATE", new Variant(""));
        props.put("INQUIRY_NO", new Variant(""));
        props.put("SUPP_ID", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("COLUMN_1_ID", new Variant(0));
        props.put("COLUMN_1_FORMULA", new Variant(""));
        props.put("COLUMN_1_PER", new Variant(0.0));
        props.put("COLUMN_1_AMT", new Variant(0.0));
        props.put("COLUMN_1_CAPTION", new Variant(""));
        props.put("COLUMN_2_ID", new Variant(0));
        props.put("COLUMN_2_FORMULA", new Variant(""));
        props.put("COLUMN_2_PER", new Variant(0.0));
        props.put("COLUMN_2_AMT", new Variant(0.0));
        props.put("COLUMN_2_CAPTION", new Variant(""));
        props.put("COLUMN_3_ID", new Variant(0));
        props.put("COLUMN_3_FORMULA", new Variant(""));
        props.put("COLUMN_3_PER", new Variant(0.0));
        props.put("COLUMN_3_AMT", new Variant(0.0));
        props.put("COLUMN_3_CAPTION", new Variant(""));
        props.put("COLUMN_4_ID", new Variant(0));
        props.put("COLUMN_4_FORMULA", new Variant(""));
        props.put("COLUMN_4_PER", new Variant(0.0));
        props.put("COLUMN_4_AMT", new Variant(0.0));
        props.put("COLUMN_4_CAPTION", new Variant(""));
        props.put("COLUMN_5_ID", new Variant(0));
        props.put("COLUMN_5_FORMULA", new Variant(""));
        props.put("COLUMN_5_PER", new Variant(0.0));
        props.put("COLUMN_5_AMT", new Variant(0.0));
        props.put("COLUMN_5_CAPTION", new Variant(""));
        props.put("COLUMN_6_ID", new Variant(0));
        props.put("COLUMN_6_FORMULA", new Variant(""));
        props.put("COLUMN_6_PER", new Variant(0.0));
        props.put("COLUMN_6_AMT", new Variant(0.0));
        props.put("COLUMN_6_CAPTION", new Variant(""));
        props.put("COLUMN_7_ID", new Variant(0));
        props.put("COLUMN_7_FORMULA", new Variant(""));
        props.put("COLUMN_7_PER", new Variant(0.0));
        props.put("COLUMN_7_AMT", new Variant(0.0));
        props.put("COLUMN_7_CAPTION", new Variant(""));
        props.put("COLUMN_8_ID", new Variant(0));
        props.put("COLUMN_8_FORMULA", new Variant(""));
        props.put("COLUMN_8_PER", new Variant(0.0));
        props.put("COLUMN_8_AMT", new Variant(0.0));
        props.put("COLUMN_8_CAPTION", new Variant(""));
        props.put("COLUMN_9_ID", new Variant(0));
        props.put("COLUMN_9_FORMULA", new Variant(""));
        props.put("COLUMN_9_PER", new Variant(0.0));
        props.put("COLUMN_9_AMT", new Variant(0.0));
        props.put("COLUMN_9_CAPTION", new Variant(""));
        props.put("COLUMN_10_ID", new Variant(0));
        props.put("COLUMN_10_FORMULA", new Variant(""));
        props.put("COLUMN_10_PER", new Variant(0.0));
        props.put("COLUMN_10_AMT", new Variant(0.0));
        props.put("COLUMN_10_CAPTION", new Variant(""));
        props.put("COLUMN_11_ID", new Variant(0));
        props.put("COLUMN_11_FORMULA", new Variant(""));
        props.put("COLUMN_11_PER", new Variant(0.0));
        props.put("COLUMN_11_AMT", new Variant(0.0));
        props.put("COLUMN_11_CAPTION", new Variant(""));
        props.put("COLUMN_12_ID", new Variant(0));
        props.put("COLUMN_12_FORMULA", new Variant(""));
        props.put("COLUMN_12_PER", new Variant(0.0));
        props.put("COLUMN_12_AMT", new Variant(0.0));
        props.put("COLUMN_12_CAPTION", new Variant(""));
        props.put("COLUMN_13_ID", new Variant(0));
        props.put("COLUMN_13_FORMULA", new Variant(""));
        props.put("COLUMN_13_PER", new Variant(0.0));
        props.put("COLUMN_13_AMT", new Variant(0.0));
        props.put("COLUMN_13_CAPTION", new Variant(""));
        props.put("COLUMN_14_ID", new Variant(0));
        props.put("COLUMN_14_FORMULA", new Variant(""));
        props.put("COLUMN_14_PER", new Variant(0.0));
        props.put("COLUMN_14_AMT", new Variant(0.0));
        props.put("COLUMN_14_CAPTION", new Variant(""));
        props.put("COLUMN_15_ID", new Variant(0));
        props.put("COLUMN_15_FORMULA", new Variant(""));
        props.put("COLUMN_15_PER", new Variant(0.0));
        props.put("COLUMN_15_AMT", new Variant(0.0));
        props.put("COLUMN_15_CAPTION", new Variant(""));
        props.put("COLUMN_16_ID", new Variant(0));
        props.put("COLUMN_16_FORMULA", new Variant(""));
        props.put("COLUMN_16_PER", new Variant(0.0));
        props.put("COLUMN_16_AMT", new Variant(0.0));
        props.put("COLUMN_16_CAPTION", new Variant(""));
        props.put("PAYMENT_TERM",new Variant(""));
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
        props.put("CANCELLED",new Variant(false));
        
        props.put("CGST_TERM",new Variant(""));
        props.put("SGST_TERM",new Variant(""));
        props.put("IGST_TERM",new Variant(""));
        props.put("COMPOSITION_TERM",new Variant(""));
        props.put("RCM_TERM",new Variant(""));
        props.put("GST_COMPENSATION_CESS_TERM",new Variant(""));
        
        //Create a new object for QuotationItem collection
        colQuotationItems = new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND QUOT_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND QUOT_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY QUOT_ID";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
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
    
    public boolean Insert(String pPrefix,String pSuffix) {
        try {
            Statement stTmp,tmpStmt,stTerms,stHistory,stHDetail,stHTerms;
            ResultSet rsTmp,rsSupp,rsTerms,rsHistory,rsHDetail,rsHTerms;
            Statement stHeader;
            ResultSet rsHeader;
            
            String strSQL = "",InquiryNo = "";
            int InquirySrno = 0;
            double InquiryQty=0,PrevQty=0,CurrentQty=0;
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER_H WHERE QUOT_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL_H WHERE QUOT_ID='1'");
            rsHDetail.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS_H WHERE QUOT_ID='1'");
            rsHTerms.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER WHERE QUOT_ID='1'");
            //rsHeader.first();
            
            //===========Checking quantities entered against Inquiry --------------//
            for(int i=1;i<=colQuotationItems.size();i++) {
                clsQuotationItem ObjItem=(clsQuotationItem) colQuotationItems.get(Integer.toString(i));
                
                InquiryNo=(String)ObjItem.getAttribute("INQUIRY_NO").getObj();
                InquirySrno=(int)ObjItem.getAttribute("INQUIRY_SRNO").getVal();
                
                if(!InquiryNo.trim().equals("")&&InquirySrno>0) {
                    //Get the  Indent Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    strSQL="SELECT QTY,ITEM_CODE FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND SR_NO="+InquirySrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        InquiryQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in PO Against this Indent No.
                    PrevQty=0;
                    /*stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(INQUIRY_NO)='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                     
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }*/
                    
                    String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                        LastError="Item Code in Quotation doesn't match with Inquiry. Original Item code is "+rsTmp.getString("ITEM_CODE");
                        return false;
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > InquiryQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Inquiry No. "+InquiryNo+" Sr. No. "+InquirySrno+" qty "+InquiryQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            //===================================================================//
            
            
            String strQTID=clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 19, (int)getAttribute("FFNO").getVal() ,true);
            
            rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            setAttribute("QUOT_ID",strQTID);
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
            rsResultSet.updateString("QUOT_NO",(String)getAttribute("QUOT_NO").getObj());
            rsResultSet.updateString("QUOT_DATE", (String)getAttribute("QUOT_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            //rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("COLUMN_1_ID",(int) getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER", (double)getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT", (double)getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_2_ID",(int) getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER", (double)getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT", (double)getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_3_ID",(int) getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER", (double)getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT", (double)getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_4_ID",(int) getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER", (double)getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT", (double)getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_5_ID",(int) getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER", (double)getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT", (double)getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_6_ID",(int) getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER", (double)getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT", (double)getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_7_ID",(int) getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER", (double)getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT", (double)getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_8_ID",(int) getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER", (double)getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT", (double)getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_9_ID",(int) getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER", (double)getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT", (double)getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
            rsResultSet.updateInt("COLUMN_10_ID",(int) getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER", (double)getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT", (double)getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_11_ID",(int) getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER", (double)getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT", (double)getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_12_ID",(int) getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER", (double)getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT", (double)getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_13_ID",(int) getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER", (double)getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT", (double)getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_14_ID",(int) getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER", (double)getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT", (double)getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_15_ID",(int) getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER", (double)getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT", (double)getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_16_ID",(int) getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER", (double)getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT", (double)getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
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
            rsResultSet.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsResultSet.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsResultSet.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsResultSet.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsResultSet.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsResultSet.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
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
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
            rsHistory.updateString("QUOT_NO",(String)getAttribute("QUOT_NO").getObj());
            rsHistory.updateString("QUOT_DATE", (String)getAttribute("QUOT_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            //rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("COLUMN_1_ID",(int) getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER", (double)getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT", (double)getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_2_ID",(int) getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER", (double)getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT", (double)getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_3_ID",(int) getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER", (double)getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT", (double)getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_4_ID",(int) getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER", (double)getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT", (double)getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_5_ID",(int) getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER", (double)getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT", (double)getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_6_ID",(int) getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER", (double)getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT", (double)getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_7_ID",(int) getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER", (double)getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT", (double)getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_8_ID",(int) getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER", (double)getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT", (double)getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_9_ID",(int) getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER", (double)getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT", (double)getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
            rsHistory.updateInt("COLUMN_10_ID",(int) getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER", (double)getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT", (double)getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_11_ID",(int) getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER", (double)getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT", (double)getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_12_ID",(int) getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER", (double)getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT", (double)getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_13_ID",(int) getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER", (double)getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT", (double)getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_14_ID",(int) getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER", (double)getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT", (double)getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_15_ID",(int) getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER", (double)getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT", (double)getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_16_ID",(int) getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER", (double)getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT", (double)getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
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
            rsHistory.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsHistory.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsHistory.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsHistory.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsHistory.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsHistory.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            //Inserting into QUOTE DETAIL
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE QUOT_ID='1'");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            for(int i=1;i<=colQuotationItems.size();i++) {
                clsQuotationItem ObjQuotationItems=(clsQuotationItem) colQuotationItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsTmp.updateLong("SR_NO", getSRNO(EITLERPGLOBAL.gCompanyID,(String)getAttribute("QUOT_ID").getObj(),"D_PUR_QUOT_DETAIL","SR_NO"));
                rsTmp.updateString("INQUIRY_NO",(String)ObjQuotationItems.getAttribute("INQUIRY_NO").getObj());
                rsTmp.updateLong("INQUIRY_SRNO",(long)ObjQuotationItems.getAttribute("INQUIRY_SRNO").getVal());
                rsTmp.updateString("ITEM_ID",(String)ObjQuotationItems.getAttribute("ITEM_ID").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String)ObjQuotationItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("HSN_SAC_CODE",(String)ObjQuotationItems.getAttribute("HSN_SAC_CODE").getObj());
                rsTmp.updateString("MAKE",(String)ObjQuotationItems.getAttribute("MAKE").getObj());
                rsTmp.updateString("DELIVERY_DATE",(String)ObjQuotationItems.getAttribute("DELIVERY_DATE").getObj());
                rsTmp.updateLong("UNIT",(long)ObjQuotationItems.getAttribute("UNIT").getVal());
                rsTmp.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjQuotationItems.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsTmp.updateDouble("QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsTmp.updateDouble("PO_QTY",0);
                rsTmp.updateDouble("BAL_QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsTmp.updateBoolean("APPROVED",false);
                rsTmp.updateDouble("RATE",(double)ObjQuotationItems.getAttribute("RATE").getVal());
                rsTmp.updateDouble("TOTAL_AMOUNT",(double)ObjQuotationItems.getAttribute("TOTAL_AMOUNT").getVal());
                rsTmp.updateDouble("ACCESS_AMOUNT",(double)ObjQuotationItems.getAttribute("ACCESS_AMOUNT").getVal());
                rsTmp.updateString("REMARKS",(String)ObjQuotationItems.getAttribute("REMARKS").getObj());
                rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateLong("COLUMN_1_ID",(long)ObjQuotationItems.getAttribute("COLUMN_1_ID").getVal());
                rsTmp.updateString("COLUMN_1_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_1_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_1_PER",(double)ObjQuotationItems.getAttribute("COLUMN_1_PER").getVal());
                rsTmp.updateDouble("COLUMN_1_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_1_AMT").getVal());
                rsTmp.updateString("COLUMN_1_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_1_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_2_ID",(long)ObjQuotationItems.getAttribute("COLUMN_2_ID").getVal());
                rsTmp.updateString("COLUMN_2_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_2_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_2_PER",(double)ObjQuotationItems.getAttribute("COLUMN_2_PER").getVal());
                rsTmp.updateDouble("COLUMN_2_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_2_AMT").getVal());
                rsTmp.updateString("COLUMN_2_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_2_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_3_ID",(long)ObjQuotationItems.getAttribute("COLUMN_3_ID").getVal());
                rsTmp.updateString("COLUMN_3_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_3_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_3_PER",(double)ObjQuotationItems.getAttribute("COLUMN_3_PER").getVal());
                rsTmp.updateDouble("COLUMN_3_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_3_AMT").getVal());
                rsTmp.updateString("COLUMN_3_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_3_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_4_ID",(long)ObjQuotationItems.getAttribute("COLUMN_4_ID").getVal());
                rsTmp.updateString("COLUMN_4_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_4_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_4_PER",(double)ObjQuotationItems.getAttribute("COLUMN_4_PER").getVal());
                rsTmp.updateDouble("COLUMN_4_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_4_AMT").getVal());
                rsTmp.updateString("COLUMN_4_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_4_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_5_ID",(long)ObjQuotationItems.getAttribute("COLUMN_5_ID").getVal());
                rsTmp.updateString("COLUMN_5_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_5_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_5_PER",(double)ObjQuotationItems.getAttribute("COLUMN_5_PER").getVal());
                rsTmp.updateDouble("COLUMN_5_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_5_AMT").getVal());
                rsTmp.updateString("COLUMN_5_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_5_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_6_ID",(long)ObjQuotationItems.getAttribute("COLUMN_6_ID").getVal());
                rsTmp.updateString("COLUMN_6_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_6_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_6_PER",(double)ObjQuotationItems.getAttribute("COLUMN_6_PER").getVal());
                rsTmp.updateDouble("COLUMN_6_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_6_AMT").getVal());
                rsTmp.updateString("COLUMN_6_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_6_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_7_ID",(long)ObjQuotationItems.getAttribute("COLUMN_7_ID").getVal());
                rsTmp.updateString("COLUMN_7_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_7_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_7_PER",(double)ObjQuotationItems.getAttribute("COLUMN_7_PER").getVal());
                rsTmp.updateDouble("COLUMN_7_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_7_AMT").getVal());
                rsTmp.updateString("COLUMN_7_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_7_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_8_ID",(long)ObjQuotationItems.getAttribute("COLUMN_8_ID").getVal());
                rsTmp.updateString("COLUMN_8_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_8_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_8_PER",(double)ObjQuotationItems.getAttribute("COLUMN_8_PER").getVal());
                rsTmp.updateDouble("COLUMN_8_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_8_AMT").getVal());
                rsTmp.updateString("COLUMN_8_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_8_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_9_ID",(long)ObjQuotationItems.getAttribute("COLUMN_9_ID").getVal());
                rsTmp.updateString("COLUMN_9_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_9_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_9_PER",(double)ObjQuotationItems.getAttribute("COLUMN_9_PER").getVal());
                rsTmp.updateDouble("COLUMN_9_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_9_AMT").getVal());
                rsTmp.updateString("COLUMN_9_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
                rsTmp.updateLong("COLUMN_10_ID",(long)ObjQuotationItems.getAttribute("COLUMN_10_ID").getVal());
                rsTmp.updateString("COLUMN_10_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_10_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_10_PER",(double)ObjQuotationItems.getAttribute("COLUMN_10_PER").getVal());
                rsTmp.updateDouble("COLUMN_10_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_10_AMT").getVal());
                rsTmp.updateString("COLUMN_10_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_10_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_11_ID",(long)ObjQuotationItems.getAttribute("COLUMN_11_ID").getVal());
                rsTmp.updateString("COLUMN_11_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_11_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_11_PER",(double)ObjQuotationItems.getAttribute("COLUMN_11_PER").getVal());
                rsTmp.updateDouble("COLUMN_11_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_11_AMT").getVal());
                rsTmp.updateString("COLUMN_11_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_11_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_12_ID",(long)ObjQuotationItems.getAttribute("COLUMN_12_ID").getVal());
                rsTmp.updateString("COLUMN_12_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_12_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_12_PER",(double)ObjQuotationItems.getAttribute("COLUMN_12_PER").getVal());
                rsTmp.updateDouble("COLUMN_12_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_12_AMT").getVal());
                rsTmp.updateString("COLUMN_12_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_12_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_13_ID",(long)ObjQuotationItems.getAttribute("COLUMN_13_ID").getVal());
                rsTmp.updateString("COLUMN_13_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_13_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_13_PER",(double)ObjQuotationItems.getAttribute("COLUMN_13_PER").getVal());
                rsTmp.updateDouble("COLUMN_13_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_13_AMT").getVal());
                rsTmp.updateString("COLUMN_13_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_13_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_14_ID",(long)ObjQuotationItems.getAttribute("COLUMN_14_ID").getVal());
                rsTmp.updateString("COLUMN_14_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_14_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_14_PER",(double)ObjQuotationItems.getAttribute("COLUMN_14_PER").getVal());
                rsTmp.updateDouble("COLUMN_14_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_14_AMT").getVal());
                rsTmp.updateString("COLUMN_14_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_14_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_15_ID",(long)ObjQuotationItems.getAttribute("COLUMN_15_ID").getVal());
                rsTmp.updateString("COLUMN_15_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_15_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_15_PER",(double)ObjQuotationItems.getAttribute("COLUMN_15_PER").getVal());
                rsTmp.updateDouble("COLUMN_15_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_15_AMT").getVal());
                rsTmp.updateString("COLUMN_15_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_15_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_16_ID",(long)ObjQuotationItems.getAttribute("COLUMN_16_ID").getVal());
                rsTmp.updateString("COLUMN_16_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_16_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_16_PER",(double)ObjQuotationItems.getAttribute("COLUMN_16_PER").getVal());
                rsTmp.updateDouble("COLUMN_16_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_16_AMT").getVal());
                rsTmp.updateString("COLUMN_16_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsTmp.updateString("SUPP_ITEM_DESC",(String)ObjQuotationItems.getAttribute("SUPP_ITEM_DESC").getObj());
                rsTmp.updateString("PRICE_LIST_NO",(String)ObjQuotationItems.getAttribute("PRICE_LIST_NO").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsHDetail.updateLong("SR_NO", getSRNO(EITLERPGLOBAL.gCompanyID,(String)getAttribute("QUOT_ID").getObj(),"D_PUR_QUOT_DETAIL","SR_NO"));
                rsHDetail.updateString("INQUIRY_NO",(String)ObjQuotationItems.getAttribute("INQUIRY_NO").getObj());
                rsHDetail.updateLong("INQUIRY_SRNO",(long)ObjQuotationItems.getAttribute("INQUIRY_SRNO").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjQuotationItems.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjQuotationItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE",(String)ObjQuotationItems.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjQuotationItems.getAttribute("MAKE").getObj());
                rsHDetail.updateString("DELIVERY_DATE",(String)ObjQuotationItems.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateLong("UNIT",(long)ObjQuotationItems.getAttribute("UNIT").getVal());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjQuotationItems.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateDouble("QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("PO_QTY",0);
                rsHDetail.updateDouble("BAL_QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsHDetail.updateBoolean("APPROVED",false);
                rsHDetail.updateDouble("RATE",(double)ObjQuotationItems.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",(double)ObjQuotationItems.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("ACCESS_AMOUNT",(double)ObjQuotationItems.getAttribute("ACCESS_AMOUNT").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjQuotationItems.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsHDetail.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                //rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateLong("COLUMN_1_ID",(long)ObjQuotationItems.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER",(double)ObjQuotationItems.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_2_ID",(long)ObjQuotationItems.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER",(double)ObjQuotationItems.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_3_ID",(long)ObjQuotationItems.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER",(double)ObjQuotationItems.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_4_ID",(long)ObjQuotationItems.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER",(double)ObjQuotationItems.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_5_ID",(long)ObjQuotationItems.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER",(double)ObjQuotationItems.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_6_ID",(long)ObjQuotationItems.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER",(double)ObjQuotationItems.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_7_ID",(long)ObjQuotationItems.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER",(double)ObjQuotationItems.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_8_ID",(long)ObjQuotationItems.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER",(double)ObjQuotationItems.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_9_ID",(long)ObjQuotationItems.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER",(double)ObjQuotationItems.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
                rsHDetail.updateLong("COLUMN_10_ID",(long)ObjQuotationItems.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER",(double)ObjQuotationItems.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_10_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_11_ID",(long)ObjQuotationItems.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER",(double)ObjQuotationItems.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_11_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_12_ID",(long)ObjQuotationItems.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER",(double)ObjQuotationItems.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_12_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_13_ID",(long)ObjQuotationItems.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER",(double)ObjQuotationItems.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_13_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_14_ID",(long)ObjQuotationItems.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER",(double)ObjQuotationItems.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_14_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_15_ID",(long)ObjQuotationItems.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER",(double)ObjQuotationItems.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_15_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_16_ID",(long)ObjQuotationItems.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER",(double)ObjQuotationItems.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsHDetail.updateString("SUPP_ITEM_DESC",(String)ObjQuotationItems.getAttribute("SUPP_ITEM_DESC").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjQuotationItems.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS WHERE QUOT_ID='1'");
            
            for(int i=1;i<=colQuotTerms.size();i++) {
                clsQuotTerms ObjTerms=(clsQuotTerms) colQuotTerms.get(Integer.toString(i));
                
                rsTerms.moveToInsertRow();
                rsTerms.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTerms.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsTerms.updateLong("SR_NO",i);
                rsTerms.updateString("TERM_TYPE",(String)ObjTerms.getAttribute("TERM_TYPE").getObj());
                rsTerms.updateInt("TERM_CODE",(int)ObjTerms.getAttribute("TERM_CODE").getVal());
                rsTerms.updateString("TERM_DESC",(String)ObjTerms.getAttribute("TERM_DESC").getObj());
                rsTerms.updateBoolean("CHANGED",true);
                rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();
                
                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO",1);
                rsHTerms.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHTerms.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsHTerms.updateLong("SR_NO",i);
                rsHTerms.updateString("TERM_TYPE",(String)ObjTerms.getAttribute("TERM_TYPE").getObj());
                rsHTerms.updateInt("TERM_CODE",(int)ObjTerms.getAttribute("TERM_CODE").getVal());
                rsHTerms.updateString("TERM_DESC",(String)ObjTerms.getAttribute("TERM_DESC").getObj());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            
            
            //======== Update the Approval Flow =========
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=19; //QUOTATION MODULE ID
            ObjFlow.DocNo=(String)getAttribute("QUOT_ID").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_QUOT_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="QUOT_ID";
            
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
    
    public long getSRNO(long pCompanyID, String pQTID, String pTable, String pField) {
        try {
            Connection Conn=data.getConn();
            Statement stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT MAX("+pField+") AS MAXID FROM "+pTable+" WHERE COMPANY_ID="+Long.toString(pCompanyID) + " AND QUOT_ID='" + pQTID + "'" ;
            
            ResultSet rsTmp=stmt.executeQuery(strSQL);
            rsTmp.first();
            
            return rsTmp.getLong("MAXID")+1;
        }
        catch(Exception e) {
            return 1;
        }
    }
    
    //Updates current record
    public boolean Update() {
        try {
            Statement stTmp,tmpStmt,stTerms,stHistory,stHDetail,stHTerms;
            ResultSet rsTmp,rsSupp,rsTerms,rsHistory,rsHDetail,rsHTerms;
            Statement stHeader;
            ResultSet rsHeader;
            
            String strSQL = "",InquiryNo = "";
            int InquirySrno = 0;
            double InquiryQty=0,PrevQty=0,CurrentQty=0;
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER_H WHERE QUOT_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL_H WHERE QUOT_ID='1'");
            rsHDetail.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS_H WHERE QUOT_ID='1'");
            rsHTerms.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("QUOT_ID").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(QUOT_ID)='"+theDocNo+"'");
            //rsHeader.first();
            
            //===========Checking quantities entered against Inquiry --------------//
            for(int i=1;i<=colQuotationItems.size();i++) {
                clsQuotationItem ObjItem=(clsQuotationItem) colQuotationItems.get(Integer.toString(i));
                
                
                InquiryNo=(String)ObjItem.getAttribute("INQUIRY_NO").getObj();
                InquirySrno=(int)ObjItem.getAttribute("INQUIRY_SRNO").getVal();
                
                if(!InquiryNo.trim().equals("")&&InquirySrno>0) {
                    //Get the  Indent Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY,ITEM_CODE FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND SR_NO="+InquirySrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        InquiryQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in PO Against this Indent No.
                    PrevQty=0;
                    /*stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(INQUIRY_NO)='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrno+" AND TRIM(QUOT_ID)<>'"+(String)getAttribute("QUOT_ID").getObj()+"'";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                     
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }*/
                    
                    String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                    if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                        LastError="Item Code in Quotation doesn't match with Inquiry. Original Item code is "+rsTmp.getString("ITEM_CODE");
                        return false;
                    }
                    
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > InquiryQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Inquiry No. "+InquiryNo+" Sr. No. "+InquirySrno+" qty "+InquiryQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            //===================================================================//
            
            
            rsResultSet.updateString("QUOT_NO",(String)getAttribute("QUOT_NO").getObj());
            rsResultSet.updateString("QUOT_DATE", (String)getAttribute("QUOT_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            //rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("COLUMN_1_ID",(int) getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER", (double)getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT", (double)getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_2_ID",(int) getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER", (double)getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT", (double)getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_3_ID",(int) getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER", (double)getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT", (double)getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_4_ID",(int) getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER", (double)getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT", (double)getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_5_ID",(int) getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER", (double)getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT", (double)getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_6_ID",(int) getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER", (double)getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT", (double)getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_7_ID",(int) getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER", (double)getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT", (double)getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_8_ID",(int) getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER", (double)getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT", (double)getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_9_ID",(int) getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER", (double)getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT", (double)getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
            rsResultSet.updateInt("COLUMN_10_ID",(int) getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER", (double)getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT", (double)getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_11_ID",(int) getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER", (double)getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT", (double)getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_12_ID",(int) getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER", (double)getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT", (double)getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_13_ID",(int) getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER", (double)getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT", (double)getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_14_ID",(int) getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER", (double)getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT", (double)getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_15_ID",(int) getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER", (double)getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT", (double)getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_16_ID",(int) getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER", (double)getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT", (double)getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
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
            rsResultSet.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsResultSet.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsResultSet.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsResultSet.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsResultSet.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsResultSet.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_QUOT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUOT_ID='"+(String)getAttribute("QUOT_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("QUOT_ID").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
            rsHistory.updateString("QUOT_NO",(String)getAttribute("QUOT_NO").getObj());
            rsHistory.updateString("QUOT_DATE", (String)getAttribute("QUOT_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            //rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("COLUMN_1_ID",(int) getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER", (double)getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT", (double)getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_2_ID",(int) getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER", (double)getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT", (double)getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_3_ID",(int) getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER", (double)getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT", (double)getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_4_ID",(int) getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER", (double)getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT", (double)getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_5_ID",(int) getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER", (double)getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT", (double)getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_6_ID",(int) getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER", (double)getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT", (double)getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_7_ID",(int) getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER", (double)getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT", (double)getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_8_ID",(int) getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER", (double)getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT", (double)getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_9_ID",(int) getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER", (double)getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT", (double)getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
            rsHistory.updateInt("COLUMN_10_ID",(int) getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER", (double)getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT", (double)getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_11_ID",(int) getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER", (double)getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT", (double)getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_12_ID",(int) getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER", (double)getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT", (double)getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_13_ID",(int) getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER", (double)getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT", (double)getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_14_ID",(int) getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER", (double)getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT", (double)getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_15_ID",(int) getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER", (double)getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT", (double)getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_16_ID",(int) getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER", (double)getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT", (double)getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
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
            rsHistory.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsHistory.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsHistory.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsHistory.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsHistory.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsHistory.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mQTID=(String)getAttribute("QUOT_ID").getObj();
            
            data.Execute("DELETE FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='"+mQTID+ "'");
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE QUOT_ID='1'");
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            for(int i=1;i<=colQuotationItems.size();i++) {
                clsQuotationItem ObjQuotationItems=(clsQuotationItem) colQuotationItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsTmp.updateLong("SR_NO", getSRNO(EITLERPGLOBAL.gCompanyID,(String)getAttribute("QUOT_ID").getObj(),"D_PUR_QUOT_DETAIL","SR_NO"));
                rsTmp.updateString("INQUIRY_NO",(String)ObjQuotationItems.getAttribute("INQUIRY_NO").getObj());
                rsTmp.updateLong("INQUIRY_SRNO",(long)ObjQuotationItems.getAttribute("INQUIRY_SRNO").getVal());
                rsTmp.updateString("ITEM_ID",(String)ObjQuotationItems.getAttribute("ITEM_ID").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String)ObjQuotationItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("HSN_SAC_CODE",(String)ObjQuotationItems.getAttribute("HSN_SAC_CODE").getObj());
                rsTmp.updateString("MAKE",(String)ObjQuotationItems.getAttribute("MAKE").getObj());
                rsTmp.updateString("DELIVERY_DATE",(String)ObjQuotationItems.getAttribute("DELIVERY_DATE").getObj());
                rsTmp.updateLong("UNIT",(long)ObjQuotationItems.getAttribute("UNIT").getVal());
                rsTmp.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjQuotationItems.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsTmp.updateDouble("QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsTmp.updateDouble("PO_QTY",0);
                rsTmp.updateDouble("BAL_QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsTmp.updateDouble("RATE",(double)ObjQuotationItems.getAttribute("RATE").getVal());
                rsTmp.updateDouble("TOTAL_AMOUNT",(double)ObjQuotationItems.getAttribute("TOTAL_AMOUNT").getVal());
                rsTmp.updateDouble("ACCESS_AMOUNT",(double)ObjQuotationItems.getAttribute("ACCESS_AMOUNT").getVal());
                rsTmp.updateString("REMARKS",(String)ObjQuotationItems.getAttribute("REMARKS").getObj());
                //rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                //rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateLong("COLUMN_1_ID",(long)ObjQuotationItems.getAttribute("COLUMN_1_ID").getVal());
                rsTmp.updateString("COLUMN_1_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_1_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_1_PER",(double)ObjQuotationItems.getAttribute("COLUMN_1_PER").getVal());
                rsTmp.updateDouble("COLUMN_1_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_1_AMT").getVal());
                rsTmp.updateString("COLUMN_1_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_1_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_2_ID",(long)ObjQuotationItems.getAttribute("COLUMN_2_ID").getVal());
                rsTmp.updateString("COLUMN_2_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_2_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_2_PER",(double)ObjQuotationItems.getAttribute("COLUMN_2_PER").getVal());
                rsTmp.updateDouble("COLUMN_2_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_2_AMT").getVal());
                rsTmp.updateString("COLUMN_2_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_2_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_3_ID",(long)ObjQuotationItems.getAttribute("COLUMN_3_ID").getVal());
                rsTmp.updateString("COLUMN_3_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_3_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_3_PER",(double)ObjQuotationItems.getAttribute("COLUMN_3_PER").getVal());
                rsTmp.updateDouble("COLUMN_3_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_3_AMT").getVal());
                rsTmp.updateString("COLUMN_3_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_3_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_4_ID",(long)ObjQuotationItems.getAttribute("COLUMN_4_ID").getVal());
                rsTmp.updateString("COLUMN_4_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_4_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_4_PER",(double)ObjQuotationItems.getAttribute("COLUMN_4_PER").getVal());
                rsTmp.updateDouble("COLUMN_4_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_4_AMT").getVal());
                rsTmp.updateString("COLUMN_4_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_4_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_5_ID",(long)ObjQuotationItems.getAttribute("COLUMN_5_ID").getVal());
                rsTmp.updateString("COLUMN_5_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_5_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_5_PER",(double)ObjQuotationItems.getAttribute("COLUMN_5_PER").getVal());
                rsTmp.updateDouble("COLUMN_5_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_5_AMT").getVal());
                rsTmp.updateString("COLUMN_5_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_5_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_6_ID",(long)ObjQuotationItems.getAttribute("COLUMN_6_ID").getVal());
                rsTmp.updateString("COLUMN_6_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_6_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_6_PER",(double)ObjQuotationItems.getAttribute("COLUMN_6_PER").getVal());
                rsTmp.updateDouble("COLUMN_6_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_6_AMT").getVal());
                rsTmp.updateString("COLUMN_6_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_6_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_7_ID",(long)ObjQuotationItems.getAttribute("COLUMN_7_ID").getVal());
                rsTmp.updateString("COLUMN_7_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_7_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_7_PER",(double)ObjQuotationItems.getAttribute("COLUMN_7_PER").getVal());
                rsTmp.updateDouble("COLUMN_7_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_7_AMT").getVal());
                rsTmp.updateString("COLUMN_7_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_7_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_8_ID",(long)ObjQuotationItems.getAttribute("COLUMN_8_ID").getVal());
                rsTmp.updateString("COLUMN_8_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_8_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_8_PER",(double)ObjQuotationItems.getAttribute("COLUMN_8_PER").getVal());
                rsTmp.updateDouble("COLUMN_8_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_8_AMT").getVal());
                rsTmp.updateString("COLUMN_8_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_8_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_9_ID",(long)ObjQuotationItems.getAttribute("COLUMN_9_ID").getVal());
                rsTmp.updateString("COLUMN_9_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_9_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_9_PER",(double)ObjQuotationItems.getAttribute("COLUMN_9_PER").getVal());
                rsTmp.updateDouble("COLUMN_9_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_9_AMT").getVal());
                rsTmp.updateString("COLUMN_9_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
                rsTmp.updateLong("COLUMN_10_ID",(long)ObjQuotationItems.getAttribute("COLUMN_10_ID").getVal());
                rsTmp.updateString("COLUMN_10_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_10_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_10_PER",(double)ObjQuotationItems.getAttribute("COLUMN_10_PER").getVal());
                rsTmp.updateDouble("COLUMN_10_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_10_AMT").getVal());
                rsTmp.updateString("COLUMN_10_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_10_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_11_ID",(long)ObjQuotationItems.getAttribute("COLUMN_11_ID").getVal());
                rsTmp.updateString("COLUMN_11_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_11_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_11_PER",(double)ObjQuotationItems.getAttribute("COLUMN_11_PER").getVal());
                rsTmp.updateDouble("COLUMN_11_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_11_AMT").getVal());
                rsTmp.updateString("COLUMN_11_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_11_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_12_ID",(long)ObjQuotationItems.getAttribute("COLUMN_12_ID").getVal());
                rsTmp.updateString("COLUMN_12_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_12_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_12_PER",(double)ObjQuotationItems.getAttribute("COLUMN_12_PER").getVal());
                rsTmp.updateDouble("COLUMN_12_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_12_AMT").getVal());
                rsTmp.updateString("COLUMN_12_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_12_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_13_ID",(long)ObjQuotationItems.getAttribute("COLUMN_13_ID").getVal());
                rsTmp.updateString("COLUMN_13_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_13_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_13_PER",(double)ObjQuotationItems.getAttribute("COLUMN_13_PER").getVal());
                rsTmp.updateDouble("COLUMN_13_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_13_AMT").getVal());
                rsTmp.updateString("COLUMN_13_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_13_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_14_ID",(long)ObjQuotationItems.getAttribute("COLUMN_14_ID").getVal());
                rsTmp.updateString("COLUMN_14_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_14_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_14_PER",(double)ObjQuotationItems.getAttribute("COLUMN_14_PER").getVal());
                rsTmp.updateDouble("COLUMN_14_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_14_AMT").getVal());
                rsTmp.updateString("COLUMN_14_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_14_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_15_ID",(long)ObjQuotationItems.getAttribute("COLUMN_15_ID").getVal());
                rsTmp.updateString("COLUMN_15_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_15_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_15_PER",(double)ObjQuotationItems.getAttribute("COLUMN_15_PER").getVal());
                rsTmp.updateDouble("COLUMN_15_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_15_AMT").getVal());
                rsTmp.updateString("COLUMN_15_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_15_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_16_ID",(long)ObjQuotationItems.getAttribute("COLUMN_16_ID").getVal());
                rsTmp.updateString("COLUMN_16_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_16_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_16_PER",(double)ObjQuotationItems.getAttribute("COLUMN_16_PER").getVal());
                rsTmp.updateDouble("COLUMN_16_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_16_AMT").getVal());
                rsTmp.updateString("COLUMN_16_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsTmp.updateString("SUPP_ITEM_DESC",(String)ObjQuotationItems.getAttribute("SUPP_ITEM_DESC").getObj());
                rsTmp.updateString("PRICE_LIST_NO",(String)ObjQuotationItems.getAttribute("PRICE_LIST_NO").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsHDetail.updateLong("SR_NO", getSRNO(EITLERPGLOBAL.gCompanyID,(String)getAttribute("QUOT_ID").getObj(),"D_PUR_QUOT_DETAIL","SR_NO"));
                rsHDetail.updateString("INQUIRY_NO",(String)ObjQuotationItems.getAttribute("INQUIRY_NO").getObj());
                rsHDetail.updateLong("INQUIRY_SRNO",(long)ObjQuotationItems.getAttribute("INQUIRY_SRNO").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjQuotationItems.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjQuotationItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE",(String)ObjQuotationItems.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjQuotationItems.getAttribute("MAKE").getObj());
                rsHDetail.updateString("DELIVERY_DATE",(String)ObjQuotationItems.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateLong("UNIT",(long)ObjQuotationItems.getAttribute("UNIT").getVal());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjQuotationItems.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateDouble("QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RATE",(double)ObjQuotationItems.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",(double)ObjQuotationItems.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("ACCESS_AMOUNT",(double)ObjQuotationItems.getAttribute("ACCESS_AMOUNT").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjQuotationItems.getAttribute("REMARKS").getObj());
                //rsHDetail.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                //rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("COLUMN_1_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER",(double)ObjQuotationItems.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_2_ID",(long)ObjQuotationItems.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER",(double)ObjQuotationItems.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_3_ID",(long)ObjQuotationItems.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER",(double)ObjQuotationItems.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_4_ID",(long)ObjQuotationItems.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER",(double)ObjQuotationItems.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_5_ID",(long)ObjQuotationItems.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER",(double)ObjQuotationItems.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_6_ID",(long)ObjQuotationItems.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER",(double)ObjQuotationItems.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_7_ID",(long)ObjQuotationItems.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER",(double)ObjQuotationItems.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_8_ID",(long)ObjQuotationItems.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER",(double)ObjQuotationItems.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_9_ID",(long)ObjQuotationItems.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER",(double)ObjQuotationItems.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
                rsHDetail.updateLong("COLUMN_10_ID",(long)ObjQuotationItems.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER",(double)ObjQuotationItems.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_10_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_11_ID",(long)ObjQuotationItems.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER",(double)ObjQuotationItems.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_11_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_12_ID",(long)ObjQuotationItems.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER",(double)ObjQuotationItems.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_12_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_13_ID",(long)ObjQuotationItems.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER",(double)ObjQuotationItems.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_13_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_14_ID",(long)ObjQuotationItems.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER",(double)ObjQuotationItems.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_14_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_15_ID",(long)ObjQuotationItems.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER",(double)ObjQuotationItems.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_15_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_16_ID",(long)ObjQuotationItems.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER",(double)ObjQuotationItems.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsHDetail.updateString("SUPP_ITEM_DESC",(String)ObjQuotationItems.getAttribute("SUPP_ITEM_DESC").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjQuotationItems.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            //======== Update the Approval Flow =========
            
            data.Execute("DELETE FROM D_PUR_QUOT_TERMS WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='"+mQTID+ "'");
            
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS WHERE QUOT_ID='1'");
            
            for(int i=1;i<=colQuotTerms.size();i++) {
                clsQuotTerms ObjTerms=(clsQuotTerms) colQuotTerms.get(Integer.toString(i));
                
                rsTerms.moveToInsertRow();
                rsTerms.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTerms.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsTerms.updateLong("SR_NO",i);
                rsTerms.updateString("TERM_TYPE",(String)ObjTerms.getAttribute("TERM_TYPE").getObj());
                rsTerms.updateInt("TERM_CODE",(int)ObjTerms.getAttribute("TERM_CODE").getVal());
                rsTerms.updateString("TERM_DESC",(String)ObjTerms.getAttribute("TERM_DESC").getObj());
                rsTerms.updateBoolean("CHANGED",true);
                rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();
                
                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO",RevNo);
                rsHTerms.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHTerms.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsHTerms.updateLong("SR_NO",i);
                rsHTerms.updateString("TERM_TYPE",(String)ObjTerms.getAttribute("TERM_TYPE").getObj());
                rsHTerms.updateInt("TERM_CODE",(int)ObjTerms.getAttribute("TERM_CODE").getVal());
                rsHTerms.updateString("TERM_DESC",(String)ObjTerms.getAttribute("TERM_DESC").getObj());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=19; //QUOTATION MODULE ID
            ObjFlow.DocNo=(String)getAttribute("QUOT_ID").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_QUOT_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="QUOT_ID";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_QUOT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUOT_ID='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=19 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
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
    
    
    public boolean Amend() {
        try {
            Statement stTmp,tmpStmt,stTerms,stHistory,stHDetail,stHTerms;
            ResultSet rsTmp,rsSupp,rsTerms,rsHistory,rsHDetail,rsHTerms;
            Statement stHeader;
            ResultSet rsHeader;
            
            
            data.Execute("UPDATE D_PUR_QUOT_HEADER SET APPROVED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUOT_ID='"+(String)getAttribute("QUOT_ID").getObj()+"'");
            
            String strSQL = "",InquiryNo = "";
            int InquirySrno = 0;
            double InquiryQty=0,PrevQty=0,CurrentQty=0;
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER_H WHERE QUOT_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL_H WHERE QUOT_ID='1'");
            rsHDetail.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS_H WHERE QUOT_ID='1'");
            rsHTerms.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("QUOT_ID").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(QUOT_ID)='"+theDocNo+"'");
            //rsHeader.first();
            
            //===========Checking quantities entered against Inquiry --------------//
            for(int i=1;i<=colQuotationItems.size();i++) {
                clsQuotationItem ObjItem=(clsQuotationItem) colQuotationItems.get(Integer.toString(i));
                
                InquiryNo=(String)ObjItem.getAttribute("INQUIRY_NO").getObj();
                InquirySrno=(int)ObjItem.getAttribute("INQUIRY_SRNO").getVal();
                
                if(!InquiryNo.trim().equals("")&&InquirySrno>0) {
                    //Get the  Indent Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY,ITEM_CODE FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND SR_NO="+InquirySrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        InquiryQty=rsTmp.getDouble("QTY");
                        
                        String ItemID=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                        if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                            LastError="Item Code in Quotation doesn't match with Inquiry. Original Item code is "+rsTmp.getString("ITEM_CODE");
                            return false;
                        }
                        
                    }
                    
                    //Get Total Qty Entered in PO Against this Indent No.
                    PrevQty=0;
                    /*stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(INQUIRY_NO)='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrno+" AND TRIM(QUOT_ID)<>'"+(String)getAttribute("QUOT_ID").getObj()+"'";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                     
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }*/
                    
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > InquiryQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        //LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Inquiry No. "+InquiryNo+" Sr. No. "+InquirySrno+" qty "+InquiryQty+". Please verify the input.";
                        //return false;
                    }
                }
            }
            //===================================================================//
            
            
            rsResultSet.updateString("QUOT_NO",(String)getAttribute("QUOT_NO").getObj());
            rsResultSet.updateString("QUOT_DATE", (String)getAttribute("QUOT_DATE").getObj());
            rsResultSet.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsResultSet.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("COLUMN_1_ID",(int) getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER", (double)getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT", (double)getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_2_ID",(int) getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER", (double)getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT", (double)getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_3_ID",(int) getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER", (double)getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT", (double)getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_4_ID",(int) getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER", (double)getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT", (double)getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_5_ID",(int) getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER", (double)getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT", (double)getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_6_ID",(int) getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER", (double)getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT", (double)getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_7_ID",(int) getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER", (double)getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT", (double)getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_8_ID",(int) getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER", (double)getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT", (double)getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_9_ID",(int) getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER", (double)getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT", (double)getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
            rsResultSet.updateInt("COLUMN_10_ID",(int) getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER", (double)getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT", (double)getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_11_ID",(int) getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER", (double)getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT", (double)getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_12_ID",(int) getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER", (double)getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT", (double)getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_13_ID",(int) getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER", (double)getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT", (double)getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_14_ID",(int) getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER", (double)getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT", (double)getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_15_ID",(int) getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER", (double)getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT", (double)getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            rsResultSet.updateLong("COLUMN_16_ID",(int) getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER", (double)getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT", (double)getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
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
            rsResultSet.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsResultSet.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsResultSet.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsResultSet.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsResultSet.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsResultSet.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_QUOT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUOT_ID='"+(String)getAttribute("QUOT_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("QUOT_ID").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
            rsHistory.updateString("QUOT_NO",(String)getAttribute("QUOT_NO").getObj());
            rsHistory.updateString("QUOT_DATE", (String)getAttribute("QUOT_DATE").getObj());
            rsHistory.updateString("INQUIRY_NO",(String)getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("COLUMN_1_ID",(int) getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA",(String)getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER", (double)getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT", (double)getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION",(String)getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_2_ID",(int) getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA",(String)getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER", (double)getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT", (double)getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION",(String)getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_3_ID",(int) getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA",(String)getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER", (double)getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT", (double)getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION",(String)getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_4_ID",(int) getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA",(String)getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER", (double)getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT", (double)getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION",(String)getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_5_ID",(int) getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA",(String)getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER", (double)getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT", (double)getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION",(String)getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_6_ID",(int) getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA",(String)getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER", (double)getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT", (double)getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION",(String)getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_7_ID",(int) getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA",(String)getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER", (double)getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT", (double)getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION",(String)getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_8_ID",(int) getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA",(String)getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER", (double)getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT", (double)getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION",(String)getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_9_ID",(int) getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA",(String)getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER", (double)getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT", (double)getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION",(String)getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
            rsHistory.updateInt("COLUMN_10_ID",(int) getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA",(String)getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER", (double)getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT", (double)getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION",(String)getAttribute("COLUMN_10_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_11_ID",(int) getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA",(String)getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER", (double)getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT", (double)getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION",(String)getAttribute("COLUMN_11_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_12_ID",(int) getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA",(String)getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER", (double)getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT", (double)getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION",(String)getAttribute("COLUMN_12_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_13_ID",(int) getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA",(String)getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER", (double)getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT", (double)getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION",(String)getAttribute("COLUMN_13_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_14_ID",(int) getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA",(String)getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER", (double)getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT", (double)getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION",(String)getAttribute("COLUMN_14_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_15_ID",(int) getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA",(String)getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER", (double)getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT", (double)getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION",(String)getAttribute("COLUMN_15_CAPTION").getObj());
            rsHistory.updateLong("COLUMN_16_ID",(int) getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA",(String)getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER", (double)getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT", (double)getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION",(String)getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PAYMENT_TERM",(String)getAttribute("PAYMENT_TERM").getObj());
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
            rsHistory.updateString("CGST_TERM",(String)getAttribute("CGST_TERM").getObj());
            rsHistory.updateString("SGST_TERM",(String)getAttribute("SGST_TERM").getObj());
            rsHistory.updateString("IGST_TERM",(String)getAttribute("IGST_TERM").getObj());
            rsHistory.updateString("COMPOSITION_TERM",(String)getAttribute("COMPOSITION_TERM").getObj());
            rsHistory.updateString("RCM_TERM",(String)getAttribute("RCM_TERM").getObj());
            rsHistory.updateString("GST_COMPENSATION_CESS_TERM",(String)getAttribute("GST_COMPENSATION_CESS_TERM").getObj());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mQTID=(String)getAttribute("QUOT_ID").getObj();
            
            data.Execute("DELETE FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='"+mQTID+ "'");
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE QUOT_ID='1'");
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            for(int i=1;i<=colQuotationItems.size();i++) {
                clsQuotationItem ObjQuotationItems=(clsQuotationItem) colQuotationItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsTmp.updateLong("SR_NO", getSRNO(EITLERPGLOBAL.gCompanyID,(String)getAttribute("QUOT_ID").getObj(),"D_PUR_QUOT_DETAIL","SR_NO"));
                rsTmp.updateString("INQUIRY_NO",(String)ObjQuotationItems.getAttribute("INQUIRY_NO").getObj());
                rsTmp.updateLong("INQUIRY_SRNO",(long)ObjQuotationItems.getAttribute("INQUIRY_SRNO").getVal());
                rsTmp.updateString("ITEM_ID",(String)ObjQuotationItems.getAttribute("ITEM_ID").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String)ObjQuotationItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("HSN_SAC_CODE",(String)ObjQuotationItems.getAttribute("HSN_SAC_CODE").getObj());
                rsTmp.updateString("MAKE",(String)ObjQuotationItems.getAttribute("MAKE").getObj());
                rsTmp.updateString("PRICE_LIST_NO",(String)ObjQuotationItems.getAttribute("PRICE_LIST_NO").getObj());
                rsTmp.updateString("DELIVERY_DATE",(String)ObjQuotationItems.getAttribute("DELIVERY_DATE").getObj());
                rsTmp.updateLong("UNIT",(long)ObjQuotationItems.getAttribute("UNIT").getVal());
                rsTmp.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjQuotationItems.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsTmp.updateDouble("QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsTmp.updateDouble("PO_QTY",0);
                rsTmp.updateDouble("BAL_QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsTmp.updateDouble("RATE",(double)ObjQuotationItems.getAttribute("RATE").getVal());
                rsTmp.updateDouble("TOTAL_AMOUNT",(double)ObjQuotationItems.getAttribute("TOTAL_AMOUNT").getVal());
                rsTmp.updateDouble("ACCESS_AMOUNT",(double)ObjQuotationItems.getAttribute("ACCESS_AMOUNT").getVal());
                rsTmp.updateString("REMARKS",(String)ObjQuotationItems.getAttribute("REMARKS").getObj());
                rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateLong("COLUMN_1_ID",(long)ObjQuotationItems.getAttribute("COLUMN_1_ID").getVal());
                rsTmp.updateString("COLUMN_1_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_1_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_1_PER",(double)ObjQuotationItems.getAttribute("COLUMN_1_PER").getVal());
                rsTmp.updateDouble("COLUMN_1_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_1_AMT").getVal());
                rsTmp.updateString("COLUMN_1_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_1_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_2_ID",(long)ObjQuotationItems.getAttribute("COLUMN_2_ID").getVal());
                rsTmp.updateString("COLUMN_2_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_2_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_2_PER",(double)ObjQuotationItems.getAttribute("COLUMN_2_PER").getVal());
                rsTmp.updateDouble("COLUMN_2_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_2_AMT").getVal());
                rsTmp.updateString("COLUMN_2_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_2_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_3_ID",(long)ObjQuotationItems.getAttribute("COLUMN_3_ID").getVal());
                rsTmp.updateString("COLUMN_3_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_3_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_3_PER",(double)ObjQuotationItems.getAttribute("COLUMN_3_PER").getVal());
                rsTmp.updateDouble("COLUMN_3_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_3_AMT").getVal());
                rsTmp.updateString("COLUMN_3_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_3_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_4_ID",(long)ObjQuotationItems.getAttribute("COLUMN_4_ID").getVal());
                rsTmp.updateString("COLUMN_4_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_4_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_4_PER",(double)ObjQuotationItems.getAttribute("COLUMN_4_PER").getVal());
                rsTmp.updateDouble("COLUMN_4_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_4_AMT").getVal());
                rsTmp.updateString("COLUMN_4_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_4_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_5_ID",(long)ObjQuotationItems.getAttribute("COLUMN_5_ID").getVal());
                rsTmp.updateString("COLUMN_5_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_5_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_5_PER",(double)ObjQuotationItems.getAttribute("COLUMN_5_PER").getVal());
                rsTmp.updateDouble("COLUMN_5_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_5_AMT").getVal());
                rsTmp.updateString("COLUMN_5_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_5_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_6_ID",(long)ObjQuotationItems.getAttribute("COLUMN_6_ID").getVal());
                rsTmp.updateString("COLUMN_6_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_6_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_6_PER",(double)ObjQuotationItems.getAttribute("COLUMN_6_PER").getVal());
                rsTmp.updateDouble("COLUMN_6_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_6_AMT").getVal());
                rsTmp.updateString("COLUMN_6_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_6_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_7_ID",(long)ObjQuotationItems.getAttribute("COLUMN_7_ID").getVal());
                rsTmp.updateString("COLUMN_7_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_7_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_7_PER",(double)ObjQuotationItems.getAttribute("COLUMN_7_PER").getVal());
                rsTmp.updateDouble("COLUMN_7_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_7_AMT").getVal());
                rsTmp.updateString("COLUMN_7_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_7_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_8_ID",(long)ObjQuotationItems.getAttribute("COLUMN_8_ID").getVal());
                rsTmp.updateString("COLUMN_8_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_8_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_8_PER",(double)ObjQuotationItems.getAttribute("COLUMN_8_PER").getVal());
                rsTmp.updateDouble("COLUMN_8_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_8_AMT").getVal());
                rsTmp.updateString("COLUMN_8_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_8_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_9_ID",(long)ObjQuotationItems.getAttribute("COLUMN_9_ID").getVal());
                rsTmp.updateString("COLUMN_9_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_9_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_9_PER",(double)ObjQuotationItems.getAttribute("COLUMN_9_PER").getVal());
                rsTmp.updateDouble("COLUMN_9_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_9_AMT").getVal());
                rsTmp.updateString("COLUMN_9_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
                rsTmp.updateLong("COLUMN_10_ID",(long)ObjQuotationItems.getAttribute("COLUMN_10_ID").getVal());
                rsTmp.updateString("COLUMN_10_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_10_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_10_PER",(double)ObjQuotationItems.getAttribute("COLUMN_10_PER").getVal());
                rsTmp.updateDouble("COLUMN_10_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_10_AMT").getVal());
                rsTmp.updateString("COLUMN_10_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_10_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_11_ID",(long)ObjQuotationItems.getAttribute("COLUMN_11_ID").getVal());
                rsTmp.updateString("COLUMN_11_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_11_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_11_PER",(double)ObjQuotationItems.getAttribute("COLUMN_11_PER").getVal());
                rsTmp.updateDouble("COLUMN_11_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_11_AMT").getVal());
                rsTmp.updateString("COLUMN_11_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_11_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_12_ID",(long)ObjQuotationItems.getAttribute("COLUMN_12_ID").getVal());
                rsTmp.updateString("COLUMN_12_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_12_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_12_PER",(double)ObjQuotationItems.getAttribute("COLUMN_12_PER").getVal());
                rsTmp.updateDouble("COLUMN_12_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_12_AMT").getVal());
                rsTmp.updateString("COLUMN_12_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_12_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_13_ID",(long)ObjQuotationItems.getAttribute("COLUMN_13_ID").getVal());
                rsTmp.updateString("COLUMN_13_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_13_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_13_PER",(double)ObjQuotationItems.getAttribute("COLUMN_13_PER").getVal());
                rsTmp.updateDouble("COLUMN_13_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_13_AMT").getVal());
                rsTmp.updateString("COLUMN_13_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_13_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_14_ID",(long)ObjQuotationItems.getAttribute("COLUMN_14_ID").getVal());
                rsTmp.updateString("COLUMN_14_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_14_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_14_PER",(double)ObjQuotationItems.getAttribute("COLUMN_14_PER").getVal());
                rsTmp.updateDouble("COLUMN_14_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_14_AMT").getVal());
                rsTmp.updateString("COLUMN_14_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_14_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_15_ID",(long)ObjQuotationItems.getAttribute("COLUMN_15_ID").getVal());
                rsTmp.updateString("COLUMN_15_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_15_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_15_PER",(double)ObjQuotationItems.getAttribute("COLUMN_15_PER").getVal());
                rsTmp.updateDouble("COLUMN_15_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_15_AMT").getVal());
                rsTmp.updateString("COLUMN_15_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_15_CAPTION").getObj());
                rsTmp.updateLong("COLUMN_16_ID",(long)ObjQuotationItems.getAttribute("COLUMN_16_ID").getVal());
                rsTmp.updateString("COLUMN_16_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_16_FORMULA").getObj());
                rsTmp.updateDouble("COLUMN_16_PER",(double)ObjQuotationItems.getAttribute("COLUMN_16_PER").getVal());
                rsTmp.updateDouble("COLUMN_16_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_16_AMT").getVal());
                rsTmp.updateString("COLUMN_16_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsTmp.updateString("SUPP_ITEM_DESC",(String)ObjQuotationItems.getAttribute("SUPP_ITEM_DESC").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsHDetail.updateLong("SR_NO", getSRNO(EITLERPGLOBAL.gCompanyID,(String)getAttribute("QUOT_ID").getObj(),"D_PUR_QUOT_DETAIL","SR_NO"));
                rsHDetail.updateString("INQUIRY_NO",(String)ObjQuotationItems.getAttribute("INQUIRY_NO").getObj());
                rsHDetail.updateLong("INQUIRY_SRNO",(long)ObjQuotationItems.getAttribute("INQUIRY_SRNO").getVal());
                rsHDetail.updateString("ITEM_ID",(String)ObjQuotationItems.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String)ObjQuotationItems.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("HSN_SAC_CODE",(String)ObjQuotationItems.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("MAKE",(String)ObjQuotationItems.getAttribute("MAKE").getObj());
                rsHDetail.updateString("PRICE_LIST_NO",(String)ObjQuotationItems.getAttribute("PRICE_LIST_NO").getObj());
                rsHDetail.updateString("DELIVERY_DATE",(String)ObjQuotationItems.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateLong("UNIT",(long)ObjQuotationItems.getAttribute("UNIT").getVal());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN",ObjQuotationItems.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateDouble("QTY",(double)ObjQuotationItems.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("RATE",(double)ObjQuotationItems.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT",(double)ObjQuotationItems.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("ACCESS_AMOUNT",(double)ObjQuotationItems.getAttribute("ACCESS_AMOUNT").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjQuotationItems.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateLong("COLUMN_1_ID",(long)ObjQuotationItems.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER",(double)ObjQuotationItems.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_2_ID",(long)ObjQuotationItems.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER",(double)ObjQuotationItems.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_3_ID",(long)ObjQuotationItems.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER",(double)ObjQuotationItems.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_4_ID",(long)ObjQuotationItems.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER",(double)ObjQuotationItems.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_5_ID",(long)ObjQuotationItems.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER",(double)ObjQuotationItems.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_6_ID",(long)ObjQuotationItems.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER",(double)ObjQuotationItems.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_7_ID",(long)ObjQuotationItems.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER",(double)ObjQuotationItems.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_8_ID",(long)ObjQuotationItems.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER",(double)ObjQuotationItems.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_9_ID",(long)ObjQuotationItems.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER",(double)ObjQuotationItems.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_9_CAPTION").getObj().toString().substring(1,1));
                rsHDetail.updateLong("COLUMN_10_ID",(long)ObjQuotationItems.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER",(double)ObjQuotationItems.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_10_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_11_ID",(long)ObjQuotationItems.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER",(double)ObjQuotationItems.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_11_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_12_ID",(long)ObjQuotationItems.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER",(double)ObjQuotationItems.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_12_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_13_ID",(long)ObjQuotationItems.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER",(double)ObjQuotationItems.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_13_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_14_ID",(long)ObjQuotationItems.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER",(double)ObjQuotationItems.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_14_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_15_ID",(long)ObjQuotationItems.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER",(double)ObjQuotationItems.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_15_CAPTION").getObj());
                rsHDetail.updateLong("COLUMN_16_ID",(long)ObjQuotationItems.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA",(String)ObjQuotationItems.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER",(double)ObjQuotationItems.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT",(double)ObjQuotationItems.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION",(String)ObjQuotationItems.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsHDetail.updateString("SUPP_ITEM_DESC",(String)ObjQuotationItems.getAttribute("SUPP_ITEM_DESC").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            //======== Update the Approval Flow =========
            
            data.Execute("DELETE FROM D_PUR_QUOT_TERMS WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='"+mQTID+ "'");
            
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS WHERE QUOT_ID='1'");
            
            for(int i=1;i<=colQuotTerms.size();i++) {
                clsQuotTerms ObjTerms=(clsQuotTerms) colQuotTerms.get(Integer.toString(i));
                
                rsTerms.moveToInsertRow();
                rsTerms.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTerms.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsTerms.updateLong("SR_NO",i);
                rsTerms.updateString("TERM_TYPE",(String)ObjTerms.getAttribute("TERM_TYPE").getObj());
                rsTerms.updateInt("TERM_CODE",(int)ObjTerms.getAttribute("TERM_CODE").getVal());
                rsTerms.updateString("TERM_DESC",(String)ObjTerms.getAttribute("TERM_DESC").getObj());
                rsTerms.updateBoolean("CHANGED",true);
                rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();
                
                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO",RevNo);
                rsHTerms.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHTerms.updateString("QUOT_ID",(String)getAttribute("QUOT_ID").getObj());
                rsHTerms.updateLong("SR_NO",i);
                rsHTerms.updateString("TERM_TYPE",(String)ObjTerms.getAttribute("TERM_TYPE").getObj());
                rsHTerms.updateInt("TERM_CODE",(int)ObjTerms.getAttribute("TERM_CODE").getVal());
                rsHTerms.updateString("TERM_DESC",(String)ObjTerms.getAttribute("TERM_DESC").getObj());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=19; //QUOTATION MODULE ID
            ObjFlow.DocNo=(String)getAttribute("QUOT_ID").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_QUOT_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="QUOT_ID";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_QUOT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUOT_ID='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=19 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
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
    //This routine checks and returns whether the Quotation is deletable or not
    //Criteria is Approved Quotation cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pQTID,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND QUOT_ID='"+pQTID.trim()+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Requisition is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=19 AND DOC_NO='"+pQTID.trim()+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    //This routine checks and returns whether the Quotation is editable or not
    //Criteria is, Approved Quotation cannot be changed.
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pQTID,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND QUOT_ID='"+pQTID.trim()+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=19 AND DOC_NO='"+pQTID.trim()+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    //Deletes current record
    public boolean Delete() {
        try {
            String sCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String sQTID=(String) getAttribute("QUOT_ID").getObj();
            
            String strQry = "DELETE FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID=" + sCompanyID +" AND QUOT_ID='" + sQTID + "'" ;
            data.Execute(strQry);
            strQry = "DELETE FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID=" + sCompanyID +" AND QUOT_ID='" + sQTID + "'" ;
            data.Execute(strQry);
            
            LoadData((int)getAttribute("COMPANY_ID").getVal());
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID, String pQTID) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND QUOT_ID='" + pQTID + "'" ;
        clsQuotation ObjQuotation = new clsQuotation();
        ObjQuotation.Filter(strCondition);
        return ObjQuotation;
    }
    
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        
        HashMap List=new HashMap();
        long Counter=0,Counter1=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsQuotation ObjQuotation = new clsQuotation();
                ObjQuotation.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjQuotation.setAttribute("QUOT_ID",rsTmp.getString("QUOT_ID"));
                ObjQuotation.setAttribute("QUOT_NO",rsTmp.getString("QUOT_NO"));
                ObjQuotation.setAttribute("QUOT_DATE",rsTmp.getString("QUOT_DATE"));
                ObjQuotation.setAttribute("INQUIRY_NO",rsTmp.getString("INQUIRY_NO"));
                ObjQuotation.setAttribute("SUPP_ID",rsTmp.getString("SUPP_ID"));
                ObjQuotation.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjQuotation.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjQuotation.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjQuotation.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjQuotation.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjQuotation.setAttribute("HIERARCHY_ID",rsTmp.getLong("HIERARCHY_ID"));
                ObjQuotation.setAttribute("COLUMN_1_ID",rsTmp.getLong("COLUMN_1_ID"));
                ObjQuotation.setAttribute("COLUMN_1_FORMULA",rsTmp.getString("COLUMN_1_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_1_PER",rsTmp.getDouble("COLUMN_1_PER"));
                ObjQuotation.setAttribute("COLUMN_1_AMT",rsTmp.getDouble("COLUMN_1_AMT"));
                ObjQuotation.setAttribute("COLUMN_1_CAPTION",rsTmp.getString("COLUMN_1_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_2_ID",rsTmp.getLong("COLUMN_2_ID"));
                ObjQuotation.setAttribute("COLUMN_2_FORMULA",rsTmp.getString("COLUMN_2_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_2_PER",rsTmp.getDouble("COLUMN_2_PER"));
                ObjQuotation.setAttribute("COLUMN_2_AMT",rsTmp.getDouble("COLUMN_2_AMT"));
                ObjQuotation.setAttribute("COLUMN_2_CAPTION",rsTmp.getString("COLUMN_2_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_3_ID",rsTmp.getLong("COLUMN_3_ID"));
                ObjQuotation.setAttribute("COLUMN_3_FORMULA",rsTmp.getString("COLUMN_3_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_3_PER",rsTmp.getDouble("COLUMN_3_PER"));
                ObjQuotation.setAttribute("COLUMN_3_AMT",rsTmp.getDouble("COLUMN_3_AMT"));
                ObjQuotation.setAttribute("COLUMN_3_CAPTION",rsTmp.getString("COLUMN_3_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_4_ID",rsTmp.getLong("COLUMN_4_ID"));
                ObjQuotation.setAttribute("COLUMN_4_FORMULA",rsTmp.getString("COLUMN_4_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_4_PER",rsTmp.getDouble("COLUMN_4_PER"));
                ObjQuotation.setAttribute("COLUMN_4_AMT",rsTmp.getDouble("COLUMN_4_AMT"));
                ObjQuotation.setAttribute("COLUMN_4_CAPTION",rsTmp.getString("COLUMN_4_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_5_ID",rsTmp.getLong("COLUMN_5_ID"));
                ObjQuotation.setAttribute("COLUMN_5_FORMULA",rsTmp.getString("COLUMN_5_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_5_PER",rsTmp.getDouble("COLUMN_5_PER"));
                ObjQuotation.setAttribute("COLUMN_5_AMT",rsTmp.getDouble("COLUMN_5_AMT"));
                ObjQuotation.setAttribute("COLUMN_5_CAPTION",rsTmp.getString("COLUMN_5_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_6_ID",rsTmp.getLong("COLUMN_6_ID"));
                ObjQuotation.setAttribute("COLUMN_6_FORMULA",rsTmp.getString("COLUMN_6_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_6_PER",rsTmp.getDouble("COLUMN_6_PER"));
                ObjQuotation.setAttribute("COLUMN_6_AMT",rsTmp.getDouble("COLUMN_6_AMT"));
                ObjQuotation.setAttribute("COLUMN_6_CAPTION",rsTmp.getString("COLUMN_6_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_7_ID",rsTmp.getLong("COLUMN_7_ID"));
                ObjQuotation.setAttribute("COLUMN_7_FORMULA",rsTmp.getString("COLUMN_7_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_7_PER",rsTmp.getDouble("COLUMN_7_PER"));
                ObjQuotation.setAttribute("COLUMN_7_AMT",rsTmp.getDouble("COLUMN_7_AMT"));
                ObjQuotation.setAttribute("COLUMN_7_CAPTION",rsTmp.getString("COLUMN_7_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_8_ID",rsTmp.getLong("COLUMN_8_ID"));
                ObjQuotation.setAttribute("COLUMN_8_FORMULA",rsTmp.getString("COLUMN_8_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_8_PER",rsTmp.getDouble("COLUMN_8_PER"));
                ObjQuotation.setAttribute("COLUMN_8_AMT",rsTmp.getDouble("COLUMN_8_AMT"));
                ObjQuotation.setAttribute("COLUMN_8_CAPTION",rsTmp.getString("COLUMN_8_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_9_ID",rsTmp.getLong("COLUMN_9_ID"));
                ObjQuotation.setAttribute("COLUMN_9_FORMULA",rsTmp.getString("COLUMN_9_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_9_PER",rsTmp.getDouble("COLUMN_9_PER"));
                ObjQuotation.setAttribute("COLUMN_9_AMT",rsTmp.getDouble("COLUMN_9_AMT"));
                ObjQuotation.setAttribute("COLUMN_9_CAPTION",rsTmp.getString("COLUMN_9_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_10_ID",rsTmp.getLong("COLUMN_10_ID"));
                ObjQuotation.setAttribute("COLUMN_10_FORMULA",rsTmp.getString("COLUMN_10_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_10_PER",rsTmp.getDouble("COLUMN_10_PER"));
                ObjQuotation.setAttribute("COLUMN_10_AMT",rsTmp.getDouble("COLUMN_10_AMT"));
                ObjQuotation.setAttribute("COLUMN_10_CAPTION",rsTmp.getString("COLUMN_10_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_11_ID",rsTmp.getLong("COLUMN_11_ID"));
                ObjQuotation.setAttribute("COLUMN_11_FORMULA",rsTmp.getString("COLUMN_11_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_11_PER",rsTmp.getDouble("COLUMN_11_PER"));
                ObjQuotation.setAttribute("COLUMN_11_AMT",rsTmp.getDouble("COLUMN_11_AMT"));
                ObjQuotation.setAttribute("COLUMN_11_CAPTION",rsTmp.getString("COLUMN_11_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_12_ID",rsTmp.getLong("COLUMN_12_ID"));
                ObjQuotation.setAttribute("COLUMN_12_FORMULA",rsTmp.getString("COLUMN_12_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_12_PER",rsTmp.getDouble("COLUMN_12_PER"));
                ObjQuotation.setAttribute("COLUMN_12_AMT",rsTmp.getDouble("COLUMN_12_AMT"));
                ObjQuotation.setAttribute("COLUMN_12_CAPTION",rsTmp.getString("COLUMN_12_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_13_ID",rsTmp.getLong("COLUMN_13_ID"));
                ObjQuotation.setAttribute("COLUMN_13_FORMULA",rsTmp.getString("COLUMN_13_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_13_PER",rsTmp.getDouble("COLUMN_13_PER"));
                ObjQuotation.setAttribute("COLUMN_13_AMT",rsTmp.getDouble("COLUMN_13_AMT"));
                ObjQuotation.setAttribute("COLUMN_13_CAPTION",rsTmp.getString("COLUMN_13_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_14_ID",rsTmp.getLong("COLUMN_14_ID"));
                ObjQuotation.setAttribute("COLUMN_14_FORMULA",rsTmp.getString("COLUMN_14_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_14_PER",rsTmp.getDouble("COLUMN_14_PER"));
                ObjQuotation.setAttribute("COLUMN_14_AMT",rsTmp.getDouble("COLUMN_14_AMT"));
                ObjQuotation.setAttribute("COLUMN_14_CAPTION",rsTmp.getString("COLUMN_14_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_15_ID",rsTmp.getLong("COLUMN_15_ID"));
                ObjQuotation.setAttribute("COLUMN_15_FORMULA",rsTmp.getString("COLUMN_15_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_15_PER",rsTmp.getDouble("COLUMN_15_PER"));
                ObjQuotation.setAttribute("COLUMN_15_AMT",rsTmp.getDouble("COLUMN_15_AMT"));
                ObjQuotation.setAttribute("COLUMN_15_CAPTION",rsTmp.getString("COLUMN_15_CAPTION"));
                ObjQuotation.setAttribute("COLUMN_16_ID",rsTmp.getLong("COLUMN_16_ID"));
                ObjQuotation.setAttribute("COLUMN_16_FORMULA",rsTmp.getString("COLUMN_16_FORMULA"));
                ObjQuotation.setAttribute("COLUMN_16_PER",rsTmp.getDouble("COLUMN_16_PER"));
                ObjQuotation.setAttribute("COLUMN_16_AMT",rsTmp.getDouble("COLUMN_16_AMT"));
                ObjQuotation.setAttribute("COLUMN_16_CAPTION",rsTmp.getString("COLUMN_16_CAPTION"));
                
                //OTHER INFORMATION
                ObjQuotation.setAttribute("PAYMENT_CODE",rsTmp.getLong("PAYMENT_CODE"));
                ObjQuotation.setAttribute("PAYMENT_DESC",rsTmp.getString("PAYMENT_DESC"));
                ObjQuotation.setAttribute("DESPATCH_CODE",rsTmp.getLong("DESPATCH_CODE"));
                ObjQuotation.setAttribute("DESPATCH_DESC",rsTmp.getString("DESPATCH_DESC"));
                ObjQuotation.setAttribute("INSURANCE_CODE",rsTmp.getLong("INSURANCE_CODE"));
                ObjQuotation.setAttribute("INSURANCE_DESC",rsTmp.getString("INSURANCE_DESC"));
                ObjQuotation.setAttribute("LICENSE_CODE",rsTmp.getLong("LICENSE_CODE"));
                ObjQuotation.setAttribute("LICENSE_DESC",rsTmp.getString("LICENSE_DESC"));
                ObjQuotation.setAttribute("PACKING_CODE",rsTmp.getLong("PACKING_CODE"));
                ObjQuotation.setAttribute("PACKING_DESC",rsTmp.getString("PACKING_DESC"));
                ObjQuotation.setAttribute("FORWARDING_CODE",rsTmp.getLong("FORWARDING_CODE"));
                ObjQuotation.setAttribute("FORWARDING_DESC",rsTmp.getString("FORWARDING_DESC"));
                ObjQuotation.setAttribute("EXCISE_CODE",rsTmp.getLong("EXCISE_CODE"));
                ObjQuotation.setAttribute("EXCISE",rsTmp.getString("EXCISE"));
                ObjQuotation.setAttribute("OCTROI_CODE",rsTmp.getLong("OCTROI_CODE"));
                ObjQuotation.setAttribute("OCTROI",rsTmp.getString("OCTROI"));
                ObjQuotation.setAttribute("FREIGHT_CODE",rsTmp.getLong("FREIGHT_CODE"));
                ObjQuotation.setAttribute("FREIGHT",rsTmp.getString("FREIGHT"));
                ObjQuotation.setAttribute("TCC_CODE",rsTmp.getLong("TCC_CODE"));
                ObjQuotation.setAttribute("TCC",rsTmp.getString("TCC"));
                ObjQuotation.setAttribute("SERVICETAX_CODE",rsTmp.getLong("SERVICETAX_CODE"));
                ObjQuotation.setAttribute("SERVICETAX_DESC",rsTmp.getString("SERVICETAX_DESC"));
                ObjQuotation.setAttribute("ST_CODE",rsTmp.getLong("ST_CODE"));
                ObjQuotation.setAttribute("ST_DESC",rsTmp.getString("ST_DESC"));
                ObjQuotation.setAttribute("ESI_CODE",rsTmp.getLong("ESI_CODE"));
                ObjQuotation.setAttribute("ESI_DESC",rsTmp.getString("ESI_DESC"));
                ObjQuotation.setAttribute("FOR_CODE",rsTmp.getLong("FOR_CODE"));
                ObjQuotation.setAttribute("FOR_DESC",rsTmp.getString("FOR_DESC"));
                ObjQuotation.setAttribute("FOB_CODE",rsTmp.getLong("FOB_CODE"));
                ObjQuotation.setAttribute("FOB_DESC",rsTmp.getString("FOB_DESC"));
                
                ObjQuotation.colQuotationItems.clear();
                
                String mCompanyID=Long.toString((long)ObjQuotation.getAttribute("COMPANY_ID").getVal());
                String mQTID=(String)ObjQuotation.getAttribute("QUOT_ID").getObj();
                
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='" + mQTID + "'");
                
                Counter1=0;
                while(rsTmp2.next()) {
                    Counter1=Counter1+1;
                    clsQuotationItem ObjQuotationItems=new clsQuotationItem();
                    ObjQuotationItems.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjQuotationItems.setAttribute("QUOT_ID",rsTmp2.getString("QUOT_ID"));
                    ObjQuotationItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                    ObjQuotationItems.setAttribute("INQUIRY_NO",rsTmp2.getString("INQUIRY_NO"));
                    ObjQuotationItems.setAttribute("INQUIRY_SRNO",rsTmp2.getLong("INQUIRY_SRNO"));
                    ObjQuotationItems.setAttribute("ITEM_ID",rsTmp2.getString("ITEM_ID"));
                    ObjQuotationItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                    ObjQuotationItems.setAttribute("QTY",rsTmp2.getDouble("QTY"));
                    ObjQuotationItems.setAttribute("RATE",rsTmp2.getDouble("RATE"));
                    ObjQuotationItems.setAttribute("TOTAL_AMOUNT",rsTmp2.getDouble("TOTAL_AMOUNT"));
                    ObjQuotationItems.setAttribute("ACCESS_AMOUNT",rsTmp2.getDouble("ACCESS_AMOUNT"));
                    ObjQuotationItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                    ObjQuotationItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjQuotationItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjQuotationItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjQuotationItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    ObjQuotationItems.setAttribute("COLUMN_1_ID",rsTmp2.getLong("COLUMN_1_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_1_FORMULA",rsTmp2.getString("COLUMN_1_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_1_PER",rsTmp2.getDouble("COLUMN_1_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_1_AMT",rsTmp2.getDouble("COLUMN_1_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_1_CAPTION",rsTmp2.getString("COLUMN_1_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_2_ID",rsTmp2.getLong("COLUMN_2_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_2_FORMULA",rsTmp2.getString("COLUMN_2_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_2_PER",rsTmp2.getDouble("COLUMN_2_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_2_AMT",rsTmp2.getDouble("COLUMN_2_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_2_CAPTION",rsTmp2.getString("COLUMN_2_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_3_ID",rsTmp2.getLong("COLUMN_3_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_3_FORMULA",rsTmp2.getString("COLUMN_3_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_3_PER",rsTmp2.getDouble("COLUMN_3_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_3_AMT",rsTmp2.getDouble("COLUMN_3_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_3_CAPTION",rsTmp2.getString("COLUMN_3_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_4_ID",rsTmp2.getLong("COLUMN_4_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_4_FORMULA",rsTmp2.getString("COLUMN_4_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_4_PER",rsTmp2.getDouble("COLUMN_4_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_4_AMT",rsTmp2.getDouble("COLUMN_4_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_4_CAPTION",rsTmp2.getString("COLUMN_4_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_5_ID",rsTmp2.getLong("COLUMN_5_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_5_FORMULA",rsTmp2.getString("COLUMN_5_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_5_PER",rsTmp2.getDouble("COLUMN_5_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_5_AMT",rsTmp2.getDouble("COLUMN_5_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_5_CAPTION",rsTmp2.getString("COLUMN_5_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_6_ID",rsTmp2.getLong("COLUMN_6_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_6_FORMULA",rsTmp2.getString("COLUMN_6_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_6_PER",rsTmp2.getDouble("COLUMN_6_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_6_AMT",rsTmp2.getDouble("COLUMN_6_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_6_CAPTION",rsTmp2.getString("COLUMN_6_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_7_ID",rsTmp2.getLong("COLUMN_7_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_7_FORMULA",rsTmp2.getString("COLUMN_7_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_7_PER",rsTmp2.getDouble("COLUMN_7_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_7_AMT",rsTmp2.getDouble("COLUMN_7_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_7_CAPTION",rsTmp2.getString("COLUMN_7_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_8_ID",rsTmp2.getLong("COLUMN_8_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_8_FORMULA",rsTmp2.getString("COLUMN_8_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_8_PER",rsTmp2.getDouble("COLUMN_8_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_8_AMT",rsTmp2.getDouble("COLUMN_8_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_8_CAPTION",rsTmp2.getString("COLUMN_8_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_9_ID",rsTmp2.getLong("COLUMN_9_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_9_FORMULA",rsTmp2.getString("COLUMN_9_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_9_PER",rsTmp2.getDouble("COLUMN_9_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_9_AMT",rsTmp2.getDouble("COLUMN_9_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_9_CAPTION",rsTmp2.getString("COLUMN_9_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_10_ID",rsTmp2.getLong("COLUMN_10_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_10_FORMULA",rsTmp2.getString("COLUMN_10_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_10_PER",rsTmp2.getDouble("COLUMN_10_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_10_AMT",rsTmp2.getDouble("COLUMN_10_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_10_CAPTION",rsTmp2.getString("COLUMN_10_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_11_ID",rsTmp2.getLong("COLUMN_11_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_11_FORMULA",rsTmp2.getString("COLUMN_11_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_11_PER",rsTmp2.getDouble("COLUMN_11_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_11_AMT",rsTmp2.getDouble("COLUMN_11_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_11_CAPTION",rsTmp2.getString("COLUMN_11_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_12_ID",rsTmp2.getLong("COLUMN_12_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_12_FORMULA",rsTmp2.getString("COLUMN_12_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_12_PER",rsTmp2.getDouble("COLUMN_12_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_12_AMT",rsTmp2.getDouble("COLUMN_12_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_12_CAPTION",rsTmp2.getString("COLUMN_12_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_13_ID",rsTmp2.getLong("COLUMN_13_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_13_FORMULA",rsTmp2.getString("COLUMN_13_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_13_PER",rsTmp2.getDouble("COLUMN_13_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_13_AMT",rsTmp2.getDouble("COLUMN_13_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_13_CAPTION",rsTmp2.getString("COLUMN_13_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_14_ID",rsTmp2.getLong("COLUMN_14_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_14_FORMULA",rsTmp2.getString("COLUMN_14_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_14_PER",rsTmp2.getDouble("COLUMN_14_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_14_AMT",rsTmp2.getDouble("COLUMN_14_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_14_CAPTION",rsTmp2.getString("COLUMN_14_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_15_ID",rsTmp2.getLong("COLUMN_15_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_15_FORMULA",rsTmp2.getString("COLUMN_15_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_15_PER",rsTmp2.getDouble("COLUMN_15_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_15_AMT",rsTmp2.getDouble("COLUMN_15_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_15_CAPTION",rsTmp2.getString("COLUMN_15_CAPTION"));
                    ObjQuotationItems.setAttribute("COLUMN_16_ID",rsTmp2.getLong("COLUMN_16_ID"));
                    ObjQuotationItems.setAttribute("COLUMN_16_FORMULA",rsTmp2.getString("COLUMN_16_FORMULA"));
                    ObjQuotationItems.setAttribute("COLUMN_16_PER",rsTmp2.getDouble("COLUMN_16_PER"));
                    ObjQuotationItems.setAttribute("COLUMN_16_AMT",rsTmp2.getDouble("COLUMN_16_AMT"));
                    ObjQuotationItems.setAttribute("COLUMN_16_CAPTION",rsTmp2.getString("COLUMN_16_CAPTION"));
                    
                    ObjQuotationItems.setAttribute("SUPP_ITEM_DESC",rsTmp2.getString("SUPP_ITEM_DESC"));
                    
                    ObjQuotation.colQuotationItems.put(Long.toString(Counter1),ObjQuotationItems);
                }//INNER WHILE LOOP
                List.put(Long.toString(Counter),ObjQuotation);
            }//OUTER WHILE LOOP
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            
            rsTmp2.close();
            tmpStmt2.close();
            
        }
        catch(Exception e) {
            // LastError=e.getMessage();
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_QUOT_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if(rsResultSet.getRow()>0) {
                setData();
            }
            else {
                LoadData(EITLERPGLOBAL.gCompanyID);
                MoveLast();
                LastError="No Records found";
                return false;
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean setData() {
        try {
            ResultSet rsTmp2;
            Connection tmpConn;
            Statement tmpStmt;
            long Counter=0;
            int RevNo=0;
            
            tmpConn=data.getConn();
            
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("QUOT_ID",rsResultSet.getString("QUOT_ID"));
            setAttribute("QUOT_NO",rsResultSet.getString("QUOT_NO"));
            setAttribute("QUOT_DATE",rsResultSet.getString("QUOT_DATE"));
            setAttribute("INQUIRY_NO",rsResultSet.getString("INQUIRY_NO"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getLong("HIERARCHY_ID"));
            setAttribute("COLUMN_1_ID",rsResultSet.getLong("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA",rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER",rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT",rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION",rsResultSet.getString("COLUMN_1_CAPTION"));
            setAttribute("COLUMN_2_ID",rsResultSet.getLong("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA",rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER",rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT",rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION",rsResultSet.getString("COLUMN_2_CAPTION"));
            setAttribute("COLUMN_3_ID",rsResultSet.getLong("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA",rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER",rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT",rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION",rsResultSet.getString("COLUMN_3_CAPTION"));
            setAttribute("COLUMN_4_ID",rsResultSet.getLong("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA",rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER",rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT",rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION",rsResultSet.getString("COLUMN_4_CAPTION"));
            setAttribute("COLUMN_5_ID",rsResultSet.getLong("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA",rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER",rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT",rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION",rsResultSet.getString("COLUMN_5_CAPTION"));
            setAttribute("COLUMN_6_ID",rsResultSet.getLong("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA",rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER",rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT",rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION",rsResultSet.getString("COLUMN_6_CAPTION"));
            setAttribute("COLUMN_7_ID",rsResultSet.getLong("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA",rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER",rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT",rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION",rsResultSet.getString("COLUMN_7_CAPTION"));
            setAttribute("COLUMN_8_ID",rsResultSet.getLong("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA",rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER",rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT",rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION",rsResultSet.getString("COLUMN_8_CAPTION"));
            setAttribute("COLUMN_9_ID",rsResultSet.getLong("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA",rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER",rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT",rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION",rsResultSet.getString("COLUMN_9_CAPTION"));
            setAttribute("COLUMN_10_ID",rsResultSet.getLong("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA",rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER",rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT",rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION",rsResultSet.getString("COLUMN_10_CAPTION"));
            setAttribute("COLUMN_11_ID",rsResultSet.getLong("COLUMN_11_ID"));
            setAttribute("COLUMN_11_FORMULA",rsResultSet.getString("COLUMN_11_FORMULA"));
            setAttribute("COLUMN_11_PER",rsResultSet.getDouble("COLUMN_11_PER"));
            setAttribute("COLUMN_11_AMT",rsResultSet.getDouble("COLUMN_11_AMT"));
            setAttribute("COLUMN_11_CAPTION",rsResultSet.getString("COLUMN_11_CAPTION"));
            setAttribute("COLUMN_12_ID",rsResultSet.getLong("COLUMN_12_ID"));
            setAttribute("COLUMN_12_FORMULA",rsResultSet.getString("COLUMN_12_FORMULA"));
            setAttribute("COLUMN_12_PER",rsResultSet.getDouble("COLUMN_12_PER"));
            setAttribute("COLUMN_12_AMT",rsResultSet.getDouble("COLUMN_12_AMT"));
            setAttribute("COLUMN_12_CAPTION",rsResultSet.getString("COLUMN_12_CAPTION"));
            setAttribute("COLUMN_13_ID",rsResultSet.getLong("COLUMN_13_ID"));
            setAttribute("COLUMN_13_FORMULA",rsResultSet.getString("COLUMN_13_FORMULA"));
            setAttribute("COLUMN_13_PER",rsResultSet.getDouble("COLUMN_13_PER"));
            setAttribute("COLUMN_13_AMT",rsResultSet.getDouble("COLUMN_13_AMT"));
            setAttribute("COLUMN_13_CAPTION",rsResultSet.getString("COLUMN_13_CAPTION"));
            setAttribute("COLUMN_14_ID",rsResultSet.getLong("COLUMN_14_ID"));
            setAttribute("COLUMN_14_FORMULA",rsResultSet.getString("COLUMN_14_FORMULA"));
            setAttribute("COLUMN_14_PER",rsResultSet.getDouble("COLUMN_14_PER"));
            setAttribute("COLUMN_14_AMT",rsResultSet.getDouble("COLUMN_14_AMT"));
            setAttribute("COLUMN_14_CAPTION",rsResultSet.getString("COLUMN_14_CAPTION"));
            setAttribute("COLUMN_15_ID",rsResultSet.getLong("COLUMN_15_ID"));
            setAttribute("COLUMN_15_FORMULA",rsResultSet.getString("COLUMN_15_FORMULA"));
            setAttribute("COLUMN_15_PER",rsResultSet.getDouble("COLUMN_15_PER"));
            setAttribute("COLUMN_15_AMT",rsResultSet.getDouble("COLUMN_15_AMT"));
            setAttribute("COLUMN_15_CAPTION",rsResultSet.getString("COLUMN_15_CAPTION"));
            setAttribute("COLUMN_16_ID",rsResultSet.getLong("COLUMN_16_ID"));
            setAttribute("COLUMN_16_FORMULA",rsResultSet.getString("COLUMN_16_FORMULA"));
            setAttribute("COLUMN_16_PER",rsResultSet.getDouble("COLUMN_16_PER"));
            setAttribute("COLUMN_16_AMT",rsResultSet.getDouble("COLUMN_16_AMT"));
            setAttribute("COLUMN_16_CAPTION",rsResultSet.getString("COLUMN_16_CAPTION"));
            
            setAttribute("PAYMENT_TERM",rsResultSet.getString("PAYMENT_TERM"));
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
            setAttribute("CGST_TERM",rsResultSet.getString("CGST_TERM"));
            setAttribute("SGST_TERM",rsResultSet.getString("SGST_TERM"));
            setAttribute("IGST_TERM",rsResultSet.getString("IGST_TERM"));
            setAttribute("COMPOSITION_TERM",rsResultSet.getString("COMPOSITION_TERM"));
            setAttribute("RCM_TERM",rsResultSet.getString("RCM_TERM"));
            setAttribute("GST_COMPENSATION_CESS_TERM",rsResultSet.getString("GST_COMPENSATION_CESS_TERM"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            
            //Now Populate the collection
            //first clear the collection
            colQuotationItems.clear();
            colQuotTerms.clear();
            
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mQTID=(String)getAttribute("QUOT_ID").getObj();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp2=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='" + mQTID + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp2=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='" + mQTID + "' ORDER BY SR_NO");
            }
            
            Counter=0;
            while(rsTmp2.next()) {
                Counter=Counter+1;
                clsQuotationItem ObjQuotationItems=new clsQuotationItem();
                
                ObjQuotationItems.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                ObjQuotationItems.setAttribute("QUOT_ID",rsTmp2.getString("QUOT_ID"));
                ObjQuotationItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjQuotationItems.setAttribute("INQUIRY_NO",rsTmp2.getString("INQUIRY_NO"));
                ObjQuotationItems.setAttribute("INQUIRY_SRNO",rsTmp2.getLong("INQUIRY_SRNO"));
                ObjQuotationItems.setAttribute("ITEM_ID",rsTmp2.getString("ITEM_ID"));
                ObjQuotationItems.setAttribute("ITEM_EXTRA_DESC",rsTmp2.getString("ITEM_EXTRA_DESC"));
                ObjQuotationItems.setAttribute("HSN_SAC_CODE",rsTmp2.getString("HSN_SAC_CODE"));
                ObjQuotationItems.setAttribute("MAKE",rsTmp2.getString("MAKE"));
                ObjQuotationItems.setAttribute("DELIVERY_DATE",rsTmp2.getString("DELIVERY_DATE"));
                ObjQuotationItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                ObjQuotationItems.setAttribute("EXCISE_GATEPASS_GIVEN",rsTmp2.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjQuotationItems.setAttribute("QTY",rsTmp2.getDouble("QTY"));
                ObjQuotationItems.setAttribute("RATE",rsTmp2.getDouble("RATE"));
                ObjQuotationItems.setAttribute("TOTAL_AMOUNT",rsTmp2.getDouble("TOTAL_AMOUNT"));
                ObjQuotationItems.setAttribute("ACCESS_AMOUNT",rsTmp2.getDouble("ACCESS_AMOUNT"));
                ObjQuotationItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                ObjQuotationItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjQuotationItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjQuotationItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjQuotationItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                ObjQuotationItems.setAttribute("COLUMN_1_ID",rsTmp2.getLong("COLUMN_1_ID"));
                ObjQuotationItems.setAttribute("COLUMN_1_FORMULA",rsTmp2.getString("COLUMN_1_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_1_PER",rsTmp2.getDouble("COLUMN_1_PER"));
                ObjQuotationItems.setAttribute("COLUMN_1_AMT",rsTmp2.getDouble("COLUMN_1_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_1_CAPTION",rsTmp2.getString("COLUMN_1_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_2_ID",rsTmp2.getLong("COLUMN_2_ID"));
                ObjQuotationItems.setAttribute("COLUMN_2_FORMULA",rsTmp2.getString("COLUMN_2_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_2_PER",rsTmp2.getDouble("COLUMN_2_PER"));
                ObjQuotationItems.setAttribute("COLUMN_2_AMT",rsTmp2.getDouble("COLUMN_2_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_2_CAPTION",rsTmp2.getString("COLUMN_2_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_3_ID",rsTmp2.getLong("COLUMN_3_ID"));
                ObjQuotationItems.setAttribute("COLUMN_3_FORMULA",rsTmp2.getString("COLUMN_3_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_3_PER",rsTmp2.getDouble("COLUMN_3_PER"));
                ObjQuotationItems.setAttribute("COLUMN_3_AMT",rsTmp2.getDouble("COLUMN_3_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_3_CAPTION",rsTmp2.getString("COLUMN_3_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_4_ID",rsTmp2.getLong("COLUMN_4_ID"));
                ObjQuotationItems.setAttribute("COLUMN_4_FORMULA",rsTmp2.getString("COLUMN_4_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_4_PER",rsTmp2.getDouble("COLUMN_4_PER"));
                ObjQuotationItems.setAttribute("COLUMN_4_AMT",rsTmp2.getDouble("COLUMN_4_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_4_CAPTION",rsTmp2.getString("COLUMN_4_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_5_ID",rsTmp2.getLong("COLUMN_5_ID"));
                ObjQuotationItems.setAttribute("COLUMN_5_FORMULA",rsTmp2.getString("COLUMN_5_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_5_PER",rsTmp2.getDouble("COLUMN_5_PER"));
                ObjQuotationItems.setAttribute("COLUMN_5_AMT",rsTmp2.getDouble("COLUMN_5_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_5_CAPTION",rsTmp2.getString("COLUMN_5_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_6_ID",rsTmp2.getLong("COLUMN_6_ID"));
                ObjQuotationItems.setAttribute("COLUMN_6_FORMULA",rsTmp2.getString("COLUMN_6_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_6_PER",rsTmp2.getDouble("COLUMN_6_PER"));
                ObjQuotationItems.setAttribute("COLUMN_6_AMT",rsTmp2.getDouble("COLUMN_6_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_6_CAPTION",rsTmp2.getString("COLUMN_6_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_7_ID",rsTmp2.getLong("COLUMN_7_ID"));
                ObjQuotationItems.setAttribute("COLUMN_7_FORMULA",rsTmp2.getString("COLUMN_7_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_7_PER",rsTmp2.getDouble("COLUMN_7_PER"));
                ObjQuotationItems.setAttribute("COLUMN_7_AMT",rsTmp2.getDouble("COLUMN_7_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_7_CAPTION",rsTmp2.getString("COLUMN_7_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_8_ID",rsTmp2.getLong("COLUMN_8_ID"));
                ObjQuotationItems.setAttribute("COLUMN_8_FORMULA",rsTmp2.getString("COLUMN_8_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_8_PER",rsTmp2.getDouble("COLUMN_8_PER"));
                ObjQuotationItems.setAttribute("COLUMN_8_AMT",rsTmp2.getDouble("COLUMN_8_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_8_CAPTION",rsTmp2.getString("COLUMN_8_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_9_ID",rsTmp2.getLong("COLUMN_9_ID"));
                ObjQuotationItems.setAttribute("COLUMN_9_FORMULA",rsTmp2.getString("COLUMN_9_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_9_PER",rsTmp2.getDouble("COLUMN_9_PER"));
                ObjQuotationItems.setAttribute("COLUMN_9_AMT",rsTmp2.getDouble("COLUMN_9_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_9_CAPTION",rsTmp2.getString("COLUMN_9_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_10_ID",rsTmp2.getLong("COLUMN_10_ID"));
                ObjQuotationItems.setAttribute("COLUMN_10_FORMULA",rsTmp2.getString("COLUMN_10_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_10_PER",rsTmp2.getDouble("COLUMN_10_PER"));
                ObjQuotationItems.setAttribute("COLUMN_10_AMT",rsTmp2.getDouble("COLUMN_10_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_10_CAPTION",rsTmp2.getString("COLUMN_10_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_11_ID",rsTmp2.getLong("COLUMN_11_ID"));
                ObjQuotationItems.setAttribute("COLUMN_11_FORMULA",rsTmp2.getString("COLUMN_11_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_11_PER",rsTmp2.getDouble("COLUMN_11_PER"));
                ObjQuotationItems.setAttribute("COLUMN_11_AMT",rsTmp2.getDouble("COLUMN_11_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_11_CAPTION",rsTmp2.getString("COLUMN_11_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_12_ID",rsTmp2.getLong("COLUMN_12_ID"));
                ObjQuotationItems.setAttribute("COLUMN_12_FORMULA",rsTmp2.getString("COLUMN_12_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_12_PER",rsTmp2.getDouble("COLUMN_12_PER"));
                ObjQuotationItems.setAttribute("COLUMN_12_AMT",rsTmp2.getDouble("COLUMN_12_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_12_CAPTION",rsTmp2.getString("COLUMN_12_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_13_ID",rsTmp2.getLong("COLUMN_13_ID"));
                ObjQuotationItems.setAttribute("COLUMN_13_FORMULA",rsTmp2.getString("COLUMN_13_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_13_PER",rsTmp2.getDouble("COLUMN_13_PER"));
                ObjQuotationItems.setAttribute("COLUMN_13_AMT",rsTmp2.getDouble("COLUMN_13_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_13_CAPTION",rsTmp2.getString("COLUMN_13_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_14_ID",rsTmp2.getLong("COLUMN_14_ID"));
                ObjQuotationItems.setAttribute("COLUMN_14_FORMULA",rsTmp2.getString("COLUMN_14_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_14_PER",rsTmp2.getDouble("COLUMN_14_PER"));
                ObjQuotationItems.setAttribute("COLUMN_14_AMT",rsTmp2.getDouble("COLUMN_14_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_14_CAPTION",rsTmp2.getString("COLUMN_14_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_15_ID",rsTmp2.getLong("COLUMN_15_ID"));
                ObjQuotationItems.setAttribute("COLUMN_15_FORMULA",rsTmp2.getString("COLUMN_15_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_15_PER",rsTmp2.getDouble("COLUMN_15_PER"));
                ObjQuotationItems.setAttribute("COLUMN_15_AMT",rsTmp2.getDouble("COLUMN_15_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_15_CAPTION",rsTmp2.getString("COLUMN_15_CAPTION"));
                ObjQuotationItems.setAttribute("COLUMN_16_ID",rsTmp2.getLong("COLUMN_16_ID"));
                ObjQuotationItems.setAttribute("COLUMN_16_FORMULA",rsTmp2.getString("COLUMN_16_FORMULA"));
                ObjQuotationItems.setAttribute("COLUMN_16_PER",rsTmp2.getDouble("COLUMN_16_PER"));
                ObjQuotationItems.setAttribute("COLUMN_16_AMT",rsTmp2.getDouble("COLUMN_16_AMT"));
                ObjQuotationItems.setAttribute("COLUMN_16_CAPTION",rsTmp2.getString("COLUMN_16_CAPTION"));
                
                ObjQuotationItems.setAttribute("SUPP_ITEM_DESC",rsTmp2.getString("SUPP_ITEM_DESC"));
                ObjQuotationItems.setAttribute("PRICE_LIST_NO",rsTmp2.getString("PRICE_LIST_NO"));
                colQuotationItems.put(Long.toString(Counter),ObjQuotationItems);
            }
            
            
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView) {
                rsTmp2=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS_H WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='" + mQTID + "' AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp2=tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_TERMS WHERE COMPANY_ID="+mCompanyID+" AND QUOT_ID='" + mQTID + "'");
            }
            
            Counter=0;
            while(rsTmp2.next()) {
                Counter=Counter+1;
                clsQuotTerms ObjTerms=new clsQuotTerms();
                
                ObjTerms.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                ObjTerms.setAttribute("QUOT_ID",rsTmp2.getString("QUOT_ID"));
                ObjTerms.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjTerms.setAttribute("TERM_TYPE",rsTmp2.getString("TERM_TYPE"));
                ObjTerms.setAttribute("TERM_CODE",rsTmp2.getInt("TERM_CODE"));
                ObjTerms.setAttribute("TERM_DESC",rsTmp2.getString("TERM_DESC"));
                
                colQuotTerms.put(Long.toString(Counter),ObjTerms);
            }
            
            return true;
            
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public HashMap getPendingApprovals(long pCompanyID,long pUserID,int pOrder) {
        Connection tmpConn;
        
        ResultSet rsTmp3;
        Statement tmpStmt3;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSQL="";
        
        try {
            tmpConn=data.getConn();
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+ Long.toString(pCompanyID) + " AND USER_ID=" + Long.toString(pUserID) + " AND MODULE_ID=19 AND STATUS='W' ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+ Long.toString(pCompanyID) + " AND USER_ID=" + Long.toString(pUserID) + " AND MODULE_ID=19 AND STATUS='W' ORDER BY D_COM_DOC_DATA.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+ Long.toString(pCompanyID) + " AND USER_ID=" + Long.toString(pUserID) + " AND MODULE_ID=19 AND STATUS='W' ORDER BY D_COM_DOC_DATA.DOC_NO";
            }
            
            //strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+ Long.toString(pCompanyID) + " AND USER_ID=" + Long.toString(pUserID) + " AND MODULE_ID=19 AND STATUS='W'";
            tmpStmt3=tmpConn.createStatement();
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                Counter=Counter+1;
                
                List.put(Long.toString(Counter),getObject((int)pCompanyID,rsTmp3.getString("QUOT_ID")));
            }//end of while
            
            rsTmp3.close();
            tmpStmt3.close();
            //tmpConn.close();
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public static String getIndentDescription(int pCompanyID,String pQuotID,int pQuotSrNo) {
        ResultSet rsInquiry=null,rsIndent=null,rsTmp=null;
        String IndentNo,InquiryNo;
        String IndentDesc="";
        int IndentSrNo=0;
        int InquirySrNo=0;
        
        try {
            rsInquiry=data.getResult("SELECT INQUIRY_NO,INQUIRY_SRNO FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUOT_ID='"+pQuotID+"' AND SR_NO="+pQuotSrNo);
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                InquiryNo=rsInquiry.getString("INQUIRY_NO");
                InquirySrNo=rsInquiry.getInt("INQUIRY_SRNO");
                
                if((!InquiryNo.trim().equals(""))&&InquirySrNo>0) {
                    rsIndent=data.getResult("SELECT INDENT_NO,INDENT_SRNO FROM D_PUR_INQUIRY_DETAIL WHERE INQUIRY_NO='"+InquiryNo+"' AND SR_NO="+InquirySrNo);
                    rsIndent.first();
                    
                    if(rsIndent.getRow()>0) {
                        IndentNo=rsIndent.getString("INDENT_NO");
                        IndentSrNo=rsIndent.getInt("INDENT_SRNO");
                        
                        if((!IndentNo.trim().equals(""))&&IndentSrNo>0) {
                            
                            rsTmp=data.getResult("SELECT ITEM_EXTRA_DESC FROM D_INV_INDENT_DETAIL WHERE INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo);
                            rsTmp.first();
                            
                            if(rsTmp.getRow()>0) {
                                IndentDesc=rsTmp.getString("ITEM_EXTRA_DESC");
                            }
                            
                        }
                    }
                }
            }
            
            
            rsTmp.close();
            rsIndent.close();
            rsInquiry.close();
            
            
        }
        catch(Exception e) {
            
        }
        
        return IndentDesc;
        
    }
    
    
    public static String getSummaryNo(long pCompanyID,String QuotID) {
        Connection tmpConn;
        
        ResultSet rsTmp3;
        Statement tmpStmt3;
        
        String ApprovalNo="";
        
        try {
            tmpConn=data.getConn();
            
            tmpStmt3=tmpConn.createStatement();
            rsTmp3=tmpStmt3.executeQuery("SELECT APPROVAL_NO FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+ Long.toString(pCompanyID) + " AND QUOT_ID='"+QuotID+"'");
            rsTmp3.first();
            
            if(rsTmp3.getRow()>0) {
                ApprovalNo=rsTmp3.getString("APPROVAL_NO");
            }
            
            rsTmp3.close();
            tmpStmt3.close();
            //tmpConn.close();
        }
        catch(Exception e) {
        }
        
        return ApprovalNo;
    }
    
    
    
    public static String getSummaryDate(long pCompanyID,String QuotID) {
        Connection tmpConn;
        
        ResultSet rsTmp3;
        Statement tmpStmt3;
        
        String ApprovalDate="";
        
        try {
            tmpConn=data.getConn();
            
            tmpStmt3=tmpConn.createStatement();
            rsTmp3=tmpStmt3.executeQuery("SELECT A.APPROVAL_DATE FROM D_PUR_QUOT_APPROVAL_HEADER A,D_PUR_QUOT_APPROVAL_DETAIL B WHERE A.COMPANY_ID="+ Long.toString(pCompanyID) + " AND B.QUOT_ID='"+QuotID+"' AND A.APPROVAL_NO=B.APPROVAL_NO ");
            rsTmp3.first();
            
            if(rsTmp3.getRow()>0) {
                ApprovalDate=rsTmp3.getString("APPROVAL_DATE");
            }
            
            rsTmp3.close();
            tmpStmt3.close();
            //tmpConn.close();
        }
        catch(Exception e) {
        }
        
        return ApprovalDate;
    }
    
    
    private void InitParser() {
        //Initializes parser
        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table
    }
    
    
    private void GatherVariableValues_H() {
        String strVariable="";
        int varCol=0;
        double lnValue=0,lnSum=0;
        
        
        myParser.initSymTab(); // clear the contents of the symbol table
        myParser.addStandardConstants();
        myParser.addComplex(); // among other things adds i to the symbol table
        
        try {
            for(int i=1;i<colHeaderColumns.size();i++) {
                double lValue=0;
                clsColumn ObjCol=(clsColumn)colHeaderColumns.get(Integer.toString(i));
                
                if(ObjCol.getAttribute("VARIABLE").getObj()!=null) {
                    String lVariable=(String)ObjCol.getAttribute("VARIABLE").getObj();
                    if(!lVariable.trim().equals(""))    //If Variable not blank
                    {
                        //Add variable Value to Parser Table
                        lValue=ObjCol.getAttribute("VALUE").getVal();
                        
                        myParser.addVariable(lVariable,lValue);
                    }
                }
            }
            
            //Gather Variables - sum of line columns
            for(int i=1;i<=colLineColumns.size();i++) {
                //Get the Object
                clsColumn ObjVar=(clsColumn)colLineColumns.get(Integer.toString(i));
                
                //Add variable and value to parser table
                myParser.addVariable((String)ObjVar.getAttribute("VARIABLE").getObj(),ObjVar.getAttribute("VALUE").getVal());
            }
        }
        catch(Exception e)
        {}
    }
    
    private void SetupColumns_H() {
        HashMap List=new HashMap();
        
        List=clsColumn.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID=19 AND HEADER_LINE='H' ORDER BY COL_ORDER");
        //Clear Header Level Columns
        colHeaderColumns.clear();
        for(int i=1;i<=List.size();i++) {
            clsColumn ObjColumn=(clsColumn)List.get(Integer.toString(i));
            int lTaxID=(int)ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID=(int)ObjColumn.getAttribute("SR_NO").getVal();
            
            clsTaxColumn ObjTax = (clsTaxColumn)clsTaxColumn.getObject((int)EITLERPGLOBAL.gCompanyID,lTaxID);
            if((boolean)ObjTax.getAttribute("USE_PERCENT").getBool()) {
                //Add Percentage Column
                //DataModelL.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj()+"%");
                
                //Create a new Variable
                clsColumn ObjCol1=new clsColumn(); //To store percentage related values
                
                ObjCol1.setAttribute("CAPTION",(String)ObjColumn.getAttribute("CAPTION").getObj()+"%");
                ObjCol1.setAttribute("COL_ID",lColID);
                ObjCol1.setAttribute("VARIABLE","P_"+Integer.toString(lColID));
                ObjCol1.setAttribute("OPERATION","-");
                ObjCol1.setAttribute("INCLUDE",true);
                ObjCol1.setAttribute("FORMULA",(String)ObjTax.getAttribute("FORMULA").getObj());
                ObjCol1.setAttribute("VALUE",0);
                
                //Put it to Collection
                colHeaderColumns.put(Integer.toString(colHeaderColumns.size()+1),ObjCol1);
                
                //Create Another Variable
                clsColumn ObjCol2=new clsColumn(); //To store value calculated by percentage
                ObjCol2.setAttribute("CAPTION",(String)ObjColumn.getAttribute("CAPTION").getObj());
                ObjCol2.setAttribute("COL_ID",lColID);
                
                //Set Variable
                if(ObjColumn.getAttribute("VARIABLE_NAME").getObj()!=null) {
                    ObjCol2.setAttribute("VARIABLE",(String)ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }
                else {
                    ObjCol2.setAttribute("VARIABLE"," ");
                }
                ObjCol2.setAttribute("OPERATION",(String)ObjTax.getAttribute("OPERATION").getObj());
                ObjCol2.setAttribute("INCLUDE",(boolean)ObjTax.getAttribute("NO_CALCULATION").getBool());
                ObjCol2.setAttribute("FORMULA",(String)ObjTax.getAttribute("FORMULA").getObj());
                ObjCol2.setAttribute("VALUE",0);
                
                //Put it to collection
                colHeaderColumns.put(Integer.toString(colHeaderColumns.size()+1),ObjCol2);
            }
            else {
                //DataModelH.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj());
                //Set Column ID
                
                //Create a new Variable
                clsColumn ObjCol1=new clsColumn(); //To store percentage related values
                
                ObjCol1.setAttribute("CAPTION",(String)ObjColumn.getAttribute("CAPTION").getObj());
                ObjCol1.setAttribute("COL_ID",lColID);
                
                //Set Variable
                if(ObjColumn.getAttribute("VARIABLE_NAME").getObj()==null) {
                    ObjCol1.setAttribute("VARIABLE"," ");
                }
                else {
                    ObjCol1.setAttribute("VARIABLE",(String)ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }
                
                ObjCol1.setAttribute("OPERATION",(String)ObjTax.getAttribute("OPERATION").getObj());
                ObjCol1.setAttribute("INCLUDE",(boolean)ObjTax.getAttribute("NO_CALCULATION").getBool());
                ObjCol1.setAttribute("FORMULA",(String)ObjTax.getAttribute("FORMULA").getObj());
                ObjCol1.setAttribute("VALUE",0);
                
                //Put it to collection
                colHeaderColumns.put(Integer.toString(colHeaderColumns.size()+1),ObjCol1);
            }
        }
        
        
        //Adding Custom Tax Columns
        List=clsColumn.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID=19 AND HEADER_LINE='L' ORDER BY COL_ORDER");
        //Clear Header Level Columns
        colLineColumns.clear();
        for(int i=1;i<=List.size();i++) {
            clsColumn ObjColumn=(clsColumn)List.get(Integer.toString(i));
            int lTaxID=(int)ObjColumn.getAttribute("TAX_ID").getVal();
            int lColID=(int)ObjColumn.getAttribute("SR_NO").getVal();
            
            clsTaxColumn ObjTax = (clsTaxColumn)clsTaxColumn.getObject((int)EITLERPGLOBAL.gCompanyID,lTaxID);
            if((boolean)ObjTax.getAttribute("USE_PERCENT").getBool()) {
                //Add Percentage Column
                //DataModelL.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj()+"%");
                
                //Create a new Variable
                clsColumn ObjCol1=new clsColumn(); //To store percentage related values
                
                ObjCol1.setAttribute("CAPTION",(String)ObjColumn.getAttribute("CAPTION").getObj()+"%");
                ObjCol1.setAttribute("COL_ID",lColID);
                ObjCol1.setAttribute("VARIABLE","P_"+Integer.toString(lColID));
                ObjCol1.setAttribute("OPERATION","-");
                ObjCol1.setAttribute("INCLUDE",true);
                ObjCol1.setAttribute("FORMULA",(String)ObjTax.getAttribute("FORMULA").getObj());
                ObjCol1.setAttribute("VALUE",0);
                
                //Put it to Collection
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),ObjCol1);
                
                //Create Another Variable
                clsColumn ObjCol2=new clsColumn(); //To store value calculated by percentage
                ObjCol2.setAttribute("CAPTION",(String)ObjColumn.getAttribute("CAPTION").getObj());
                ObjCol2.setAttribute("COL_ID",lColID);
                
                //Set Variable
                if(ObjColumn.getAttribute("VARIABLE_NAME").getObj()!=null) {
                    ObjCol2.setAttribute("VARIABLE",(String)ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }
                else {
                    ObjCol2.setAttribute("VARIABLE"," ");
                }
                ObjCol2.setAttribute("OPERATION",(String)ObjTax.getAttribute("OPERATION").getObj());
                ObjCol2.setAttribute("INCLUDE",(boolean)ObjTax.getAttribute("NO_CALCULATION").getBool());
                ObjCol2.setAttribute("FORMULA",(String)ObjTax.getAttribute("FORMULA").getObj());
                ObjCol2.setAttribute("VALUE",0);
                
                //Put it to collection
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),ObjCol2);
            }
            else {
                //DataModelH.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj());
                //Set Column ID
                
                //Create a new Variable
                clsColumn ObjCol1=new clsColumn(); //To store percentage related values
                
                ObjCol1.setAttribute("CAPTION",(String)ObjColumn.getAttribute("CAPTION").getObj());
                ObjCol1.setAttribute("COL_ID",lColID);
                
                //Set Variable
                if(ObjColumn.getAttribute("VARIABLE_NAME").getObj()==null) {
                    ObjCol1.setAttribute("VARIABLE"," ");
                }
                else {
                    ObjCol1.setAttribute("VARIABLE",(String)ObjColumn.getAttribute("VARIABLE_NAME").getObj());
                }
                
                ObjCol1.setAttribute("OPERATION",(String)ObjTax.getAttribute("OPERATION").getObj());
                ObjCol1.setAttribute("INCLUDE",(boolean)ObjTax.getAttribute("NO_CALCULATION").getBool());
                ObjCol1.setAttribute("FORMULA",(String)ObjTax.getAttribute("FORMULA").getObj());
                ObjCol1.setAttribute("VALUE",0);
                
                //Put it to collection
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),ObjCol1);
            }
        }
        
    }
    
    private void UpdateResults_H(int pCol) {
        clsColumn ObjColumn=new clsColumn();
        clsTaxColumn ObjTax=new clsTaxColumn();
        
        ObjColumn.LoadData(EITLERPGLOBAL.gCompanyID);
        ObjTax.LoadData(EITLERPGLOBAL.gCompanyID);
        
        try {
            int ColID=0,TaxID=0,UpdateCol=0;
            String strFormula="",strItemID="",strVariable="",srcVariable="",srcVar2="";
            double lnPercentValue=0,lnFinalResult=0,lnNetAmount=0;
            Object result;
            boolean updateIt=true;
            int QtyCol=0,RateCol=0,GAmountCol=0;
            
            Updating_H=true; //Stops Recursion
            
            clsColumn ObjCol=(clsColumn) colHeaderColumns.get(Integer.toString(pCol));
            
            srcVariable=(String)ObjCol.getAttribute("VARIABLE").getObj(); //Variable name of currently updated Column
            
            //If this column is percentage column. Variable name would be P_XXX
            //We shoule use actual variable name, it will be found on it's associated next column
            if(srcVariable.substring(0,2).equals("P_")) {
                clsColumn ObjCol2=(clsColumn)colHeaderColumns.get(Integer.toString(pCol+1));
                srcVariable=(String)ObjCol2.getAttribute("VARIABLE").getObj();
            }
            
            GatherVariableValues_H();
            
            for(int i=1;i<=colHeaderColumns.size();i++) {
                clsColumn tmpCol=(clsColumn)colHeaderColumns.get(Integer.toString(i));
                
                strVariable=(String)tmpCol.getAttribute("VARIABLE").getObj();
                
                ColID=(int)tmpCol.getAttribute("COL_ID").getVal();
                
                TaxID=ObjColumn.getTaxID((int)EITLERPGLOBAL.gCompanyID,ColID);
                
                //Exclude Percentage Columns and System Columns
                if((!strVariable.substring(0,2).equals("P_"))&&(ColID!=0)) {
                    //If percentage is used
                    if(ObjTax.getUsePercentage((int)EITLERPGLOBAL.gCompanyID,TaxID)) {
                        
                        //Load the Formula for calculation
                        if((!EITLERPGLOBAL.UseCurrentFormula)) {
                            strFormula=clsTaxColumn.getFormula((int)EITLERPGLOBAL.gCompanyID,TaxID);
                        }
                        else {
                            strFormula=(String)tmpCol.getAttribute("FORMULA").getObj();
                        }
                        
                        //Now Read Associated Percentage Column
                        //It will be Prior Column
                        clsColumn tmpCol2=(clsColumn)colHeaderColumns.get(Integer.toString(i-1));
                        
                        lnPercentValue=tmpCol2.getAttribute("VALUE").getVal();
                        
                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result=myParser.getValueAsObject();
                        if(result!=null) {
                            //Now get the percentage of the main result
                            lnFinalResult=(Double.parseDouble(result.toString())*lnPercentValue)/100;
                            
                            //Update the Column
                            //srcVar2=DataModelH.getVariable(pCol+1);
                            
                            UpdateCol=i;
                            
                            updateIt=false;
                            
                            if(UpdateCol!=pCol) {
                                if(UpdateCol==pCol+1) {
                                    updateIt=true;
                                }
                                else {
                                    if((strFormula.indexOf(srcVariable)!=-1)) { //If this column is dependent on updated column
                                        updateIt=true; //Then update it
                                    }
                                    else {
                                        if((strFormula.indexOf("QTY")!=-1)||(strFormula.indexOf("RATE")!=-1)||(strFormula.indexOf("GROSS_AMOUNT")!=-1)) {
                                            if(pCol==QtyCol||pCol==RateCol||pCol==GAmountCol)
                                            { updateIt=true;  }
                                        }
                                    }
                                }
                            }
                            if(updateIt) {
                                //Put new object to Collection
                                tmpCol.setAttribute("VALUE",EITLERPGLOBAL.round(lnFinalResult,5));
                                colHeaderColumns.put(Integer.toString(i),tmpCol);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    }
                    else //Percentage Not Used
                    {
                        
                        //Load the Formula for calculation
                        if((!EITLERPGLOBAL.UseCurrentFormula)) {
                            strFormula=clsTaxColumn.getFormula((int)EITLERPGLOBAL.gCompanyID,TaxID);
                        }
                        else {
                            strFormula=(String)tmpCol.getAttribute("FORMULA").getObj();
                        }
                        
                        //Now Parse Main expression
                        myParser.parseExpression(strFormula);
                        result=myParser.getValueAsObject();
                        if(result!=null) {
                            //Now get the percentage of the main result
                            lnFinalResult=Double.parseDouble(result.toString());
                            //Update the Column
                            UpdateCol=i;
                            
                            updateIt=false;
                            
                            if(UpdateCol!=pCol) {
                                if(strFormula.indexOf(srcVariable)!=-1) {
                                    updateIt=true;
                                }
                                else {updateIt=true;
                                }
                            }
                            if(updateIt) {
                                //Put new object to Collection
                                tmpCol.setAttribute("VALUE",EITLERPGLOBAL.round(lnFinalResult,2));
                                colHeaderColumns.put(Integer.toString(i),tmpCol);
                            }
                            //Re Gather Fresh Variable Values
                            GatherVariableValues_H();
                        }
                    }
                }
            }
            Updating_H=false;
        }
        catch(Exception e) {
            Updating_H=false;
        }
    }
    
    
    private double GetUpdatedAmount(double pAmount) {
        
        //== Final Pass - Update the Net Amount ==
        double lnNetAmount=0;
        double lnColValue=0;
        double lnGrossAmount=0,lnSumNetAmount=0;
        int NetAmountCol=0,GrossAmountCol=0;
        
        
        for(int c=1;c<=colHeaderColumns.size();c++) {
            clsColumn ObjCol=(clsColumn)colHeaderColumns.get(Integer.toString(c));
            
            if(ObjCol.getAttribute("INCLUDE").getBool()==false) {
                //Read column value
                lnColValue=ObjCol.getAttribute("VALUE").getVal();
                
                String strOperation=(String)ObjCol.getAttribute("OPERATION").getObj();
                
                if(strOperation.trim().equals("+")) //Add
                {
                    lnGrossAmount=lnGrossAmount+lnColValue;
                }
                else //Substract
                {
                    lnGrossAmount=lnGrossAmount-lnColValue;
                }
            }
        }
        
        return pAmount+lnGrossAmount;
    }
    
    public HashMap getQuotItemList(int pCompanyID, String pInquiryNo) {
        HashMap List = new HashMap();
        long Counter = 0L;
        double lnLandingCost = 0.0D;
        double lnNetAmount = 0.0D;
        double lnTaxAmount = 0.0D;
        double HeaderTotal = 0.0D;
        double TotalAmount = 0.0D;
        double Qty = 0.0D;
        double Rate = 0.0D;
        String ItemID="";
        clsTaxColumn ObjTax = new clsTaxColumn();
        clsColumn ObjColumn = new clsColumn();
        
        Statement stMIR,stGRN;
        ResultSet rsMIR,rsGRN;
        
        try {
            ObjTax.LoadData(pCompanyID);
            ObjColumn.LoadData(pCompanyID);
            
            String strSQL = "SELECT D_PUR_QUOT_DETAIL.*,D_PUR_QUOT_HEADER.* FROM D_PUR_QUOT_HEADER,D_PUR_QUOT" +
            "_DETAIL WHERE D_PUR_QUOT_HEADER.COMPANY_ID=D_PUR_QUOT_DETAIL.COMPANY_ID AND D_PU" +
            "R_QUOT_HEADER.QUOT_ID=D_PUR_QUOT_DETAIL.QUOT_ID AND D_PUR_QUOT_HEADER.COMPANY_ID" +
            "="
            + pCompanyID + " AND D_PUR_QUOT_DETAIL.INQUIRY_NO='" + pInquiryNo + "' AND D_PUR_QUOT_HEADER.APPROVED=1 AND D_PUR_QUOT_HEADER.CANCELLED=0 GROUP BY D_PUR_QUOT_DETAIL.QUOT_ID,ITEM_ID ORDER BY ITEM_ID";
            Statement tmpStmt = Conn.createStatement();
            ResultSet rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            Counter = 0L;
            TotalAmount = 0.0D;
            HeaderTotal = 0.0D;
            for(; !rsTmp.isAfterLast() && rsTmp.getRow() > 0; rsTmp.next()) {
                lnNetAmount = rsTmp.getDouble("D_PUR_QUOT_DETAIL.TOTAL_AMOUNT");
                TotalAmount += lnNetAmount;
                HeaderTotal = 0.0D;
//                for(int i = 1; i <= 10; i++) {
                for(int i = 1; i <= 16; i++) {
                    int tColID = rsTmp.getInt("D_PUR_QUOT_HEADER.COLUMN_" + i + "_ID");
                    if(tColID != 0) {
                        int tTaxID = ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, tColID);
                        String tOperation = clsTaxColumn.getOperation(EITLERPGLOBAL.gCompanyID, tTaxID);
                        if(tOperation.equals("+")) {
                            HeaderTotal += rsTmp.getDouble("D_PUR_QUOT_HEADER.COLUMN_" + i + "_AMT");
                        } else {
                            HeaderTotal -= rsTmp.getDouble("D_PUR_QUOT_HEADER.COLUMN_" + i + "_AMT");
                        }
                    }
                }
                
            }
            
            rsTmp.first();
            for(; !rsTmp.isAfterLast() && rsTmp.getRow() > 0; rsTmp.next()) {
                clsQuotApprovalItem ObjItem = new clsQuotApprovalItem();
                ObjItem.setAttribute("SUPP_ID", rsTmp.getString("D_PUR_QUOT_HEADER.SUPP_ID"));
                
                ObjItem.setAttribute("QTY", rsTmp.getDouble("D_PUR_QUOT_DETAIL.QTY"));
                
                
                // ========= Get the sum of Qty =================//
                try {
                    ResultSet rsSum;
                    String InquiryNo=rsTmp.getString("D_PUR_QUOT_DETAIL.INQUIRY_NO");
                    int InquirySrNo=rsTmp.getInt("D_PUR_QUOT_DETAIL.INQUIRY_SRNO");
                    String QuotID=rsTmp.getString("D_PUR_QUOT_DETAIL.QUOT_ID");
                    System.out.println("SELECT SUM(QTY) AS THEQTY,SUM(TOTAL_AMOUNT) AS SUM_TOTAL_AMOUNT,SUM(ACCESS_AMOUNT) AS SUM_ACCESS_AMOUNT FROM D_PUR_QUOT_DETAIL WHERE QUOT_ID='"+QuotID+"' AND INQUIRY_NO='"+InquiryNo+"' AND ITEM_ID='"+rsTmp.getString("D_PUR_QUOT_DETAIL.ITEM_ID")+"'");
                    rsSum=data.getResult("SELECT SUM(QTY) AS THEQTY,SUM(TOTAL_AMOUNT) AS SUM_TOTAL_AMOUNT,SUM(ACCESS_AMOUNT) AS SUM_ACCESS_AMOUNT FROM D_PUR_QUOT_DETAIL WHERE QUOT_ID='"+QuotID+"' AND INQUIRY_NO='"+InquiryNo+"' AND ITEM_ID='"+rsTmp.getString("D_PUR_QUOT_DETAIL.ITEM_ID")+"'");
                    rsSum.first();
                    
                    if(rsSum.getRow()>0) {
                        ObjItem.setAttribute("QTY", rsSum.getDouble("THEQTY"));
                        ObjItem.setAttribute("NET_AMOUNT",rsSum.getDouble("SUM_ACCESS_AMOUNT"));
                    }
                }
                catch(Exception sum)
                {}
                // ============================================//
                
                
                ObjItem.setAttribute("RATE", rsTmp.getDouble("D_PUR_QUOT_DETAIL.RATE"));
                ObjItem.setAttribute("ITEM_CODE", rsTmp.getString("D_PUR_QUOT_DETAIL.ITEM_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC", rsTmp.getString("D_PUR_QUOT_DETAIL.ITEM_EXTRA_DESC"));
                //ObjItem.setAttribute("HSN_SAC_CODE", rsTmp.getString("D_PUR_QUOT_DETAIL.HSN_SAC_CODE"));
                ObjItem.setAttribute("MAKE", rsTmp.getString("D_PUR_QUOT_DETAIL.MAKE"));
                ObjItem.setAttribute("PRICE_LIST_NO", rsTmp.getString("D_PUR_QUOT_DETAIL.PRICE_LIST_NO"));
                ObjItem.setAttribute("INQUIRY_NO", rsTmp.getString("D_PUR_QUOT_DETAIL.INQUIRY_NO"));
                ObjItem.setAttribute("INQUIRY_SR_NO", rsTmp.getInt("D_PUR_QUOT_DETAIL.INQUIRY_SRNO"));
                ObjItem.setAttribute("QUOT_ID", rsTmp.getString("D_PUR_QUOT_DETAIL.QUOT_ID"));
                ObjItem.setAttribute("QUOT_SR_NO", rsTmp.getInt("D_PUR_QUOT_DETAIL.SR_NO"));
                
                ItemID=rsTmp.getString("D_PUR_QUOT_DETAIL.ITEM_ID");
                
                Counter++;
                lnLandingCost = rsTmp.getDouble("D_PUR_QUOT_DETAIL.TOTAL_AMOUNT");
                lnNetAmount = rsTmp.getDouble("D_PUR_QUOT_DETAIL.ACCESS_AMOUNT");
                TotalAmount += lnNetAmount;
                
                //ObjItem.setAttribute("NET_AMOUNT", lnNetAmount);
                
                Qty = rsTmp.getDouble("D_PUR_QUOT_DETAIL.QTY");
                lnTaxAmount =  lnNetAmount -lnLandingCost ;
                if(Qty > 0.0D) {
                    lnLandingCost = ((HeaderTotal / TotalAmount) * lnNetAmount + lnNetAmount) / Qty;
                } else {
                    lnLandingCost = 0.0D;
                }
                ObjItem.setAttribute("TAX_AMOUNT", EITLERPGLOBAL.round((lnTaxAmount + (HeaderTotal / TotalAmount) * lnNetAmount),5));
                ObjItem.setAttribute("LAND_COST", EITLERPGLOBAL.round(lnLandingCost, 5));
                ObjItem.setAttribute("LAST_PO_NO", "");
                ObjItem.setAttribute("LAST_PO_DATE","0000-00-00");
                ObjItem.setAttribute("LAST_LANDED_RATE", 0);
                ObjItem.setAttribute("LAST_PO_RATE",0);
                
                
                String LastPONo = clsPOGen.getLastPObyItem(EITLERPGLOBAL.gCompanyID, ItemID);
                if(!LastPONo.equals("")) {
                    clsPOGen tmpObj = new clsPOGen();
                    tmpObj.LoadData(EITLERPGLOBAL.gCompanyID, 1);
                    int POType=clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID,LastPONo);
                    clsPOGen ObjPO = (clsPOGen)tmpObj.getObject(EITLERPGLOBAL.gCompanyID, LastPONo,POType);
                    ObjItem.setAttribute("LAST_PO_NO", LastPONo);
                    ObjItem.setAttribute("LAST_PO_DATE", (String)ObjPO.getAttribute("PO_DATE").getObj());
                    ObjItem.setAttribute("LAST_PO_RATE", 0);
                    
                    for(int i = 1; i <= ObjPO.colPOItems.size(); i++) {
                        clsPOItem ObjPOItem = (clsPOItem)ObjPO.colPOItems.get(Integer.toString(i));
                        String POItemID = (String)ObjPOItem.getAttribute("ITEM_ID").getObj();
                        if(POItemID.equals(rsTmp.getString("D_PUR_QUOT_DETAIL.ITEM_ID"))) {
                            double LastLandedRate = ObjPOItem.getAttribute("RATE").getVal();
                            ObjItem.setAttribute("LAST_PO_RATE", LastLandedRate);
                        }
                        
                    }
                    
                    
                    //                    Object tmpObj2=clsGRN.getLastGRN(EITLERPGLOBAL.gCompanyID, ItemID,true);
                    //
                    //                    if(tmpObj2 instanceof clsGRN) {
                    //                        clsGRN ObjGRN=(clsGRN)tmpObj2;
                    //
                    //                        //Find the Selected Item
                    //                        for(int i=1;i<=ObjGRN.colGRNItems.size();i++) {
                    //                            clsGRNItem ObjGRNItem=(clsGRNItem)ObjGRN.colGRNItems.get(Integer.toString(i));
                    //                            String theItemID=(String)ObjGRNItem.getAttribute("ITEM_ID").getObj();
                    //
                    //                            if(theItemID.trim().equals(ItemID)) {
                    //                                ObjItem.setAttribute("LAST_LANDED_RATE", ObjGRNItem.getAttribute("LANDED_RATE").getVal());
                    //                                break;
                    //                            }
                    //                        }
                    //
                    //                    }
                    //
                    //
                    //                    if(tmpObj2 instanceof clsGRNGen) {
                    //                        clsGRNGen ObjGRN=(clsGRNGen)tmpObj2;
                    //
                    //                        //Find the Selected Item
                    //                        for(int i=1;i<=ObjGRN.colGRNItems.size();i++) {
                    //                            clsGRNGenItem ObjGRNItem=(clsGRNGenItem)ObjGRN.colGRNItems.get(Integer.toString(i));
                    //                            String theItemID=(String)ObjGRNItem.getAttribute("ITEM_ID").getObj();
                    //
                    //                            if(theItemID.trim().equals(ItemID)) {
                    //                                ObjItem.setAttribute("LAST_LANDED_RATE", ObjGRNItem.getAttribute("LANDED_RATE").getVal());
                    //                                break;
                    //                            }
                    //                        }
                    //
                    //                    }
                    
                    
                    
                    //==================== Filling up Last landed rate ======================//
                    String MIRNo="";
                    
                    stMIR=Conn.createStatement();
                    rsMIR=stMIR.executeQuery("SELECT D_INV_MIR_HEADER.MIR_NO FROM D_INV_MIR_HEADER,D_INV_MIR_DETAIL WHERE D_INV_MIR_HEADER.COMPANY_ID=D_INV_MIR_DETAIL.COMPANY_ID AND D_INV_MIR_HEADER.MIR_NO=D_INV_MIR_DETAIL.MIR_NO AND D_INV_MIR_HEADER.MIR_TYPE=D_INV_MIR_DETAIL.MIR_TYPE AND D_INV_MIR_DETAIL.ITEM_ID='"+ItemID+"' AND D_INV_MIR_HEADER.APPROVED=1 AND D_INV_MIR_HEADER.CANCELLED=0 AND PO_NO='"+LastPONo+"' GROUP BY D_INV_MIR_DETAIL.MIR_NO,MIR_DATE,D_INV_MIR_DETAIL.MIR_TYPE ORDER BY MIR_NO DESC,MIR_DATE DESC LIMIT 1");
                    rsMIR.first();
                    
                    if(rsMIR.getRow()>0) {
                        MIRNo=rsMIR.getString("MIR_NO");
                    }
                    
                    
                    //Clear existing Data
                    if(!MIRNo.trim().equals("")) {
                        stGRN=Conn.createStatement();
                        rsGRN=stGRN.executeQuery("SELECT D_INV_GRN_HEADER.GRN_NO,GRN_DATE,SUM(QTY) AS THEQTY,AVG(LANDED_RATE) AS LANDED_RATE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_DETAIL.QTY>0 AND D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_DETAIL.ITEM_ID='"+ItemID+"' AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 AND MIR_NO='"+MIRNo+"' AND D_INV_GRN_DETAIL.QTY>0 GROUP BY D_INV_GRN_DETAIL.GRN_NO,GRN_DATE,D_INV_GRN_DETAIL.GRN_TYPE  ORDER BY GRN_NO DESC,GRN_DATE DESC LIMIT 1");
                        rsGRN.first();
                        
                        if(rsGRN.getRow()>0) {
                            ObjItem.setAttribute("LAST_LANDED_RATE", rsGRN.getDouble("LANDED_RATE"));
                        }
                        else {
                            
                            stGRN=Conn.createStatement();
                            rsGRN=stGRN.executeQuery("SELECT D_INV_GRN_HEADER.GRN_NO,GRN_DATE,SUM(QTY) AS THEQTY,AVG(LANDED_RATE) AS LANDED_RATE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_DETAIL.QTY>0 AND D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_DETAIL.ITEM_ID='"+ItemID+"' AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0  GROUP BY D_INV_GRN_DETAIL.GRN_NO,GRN_DATE,D_INV_GRN_DETAIL.GRN_TYPE ORDER BY GRN_NO DESC,GRN_DATE DESC LIMIT 1");
                            rsGRN.first();
                            
                            if(rsGRN.getRow()>0) {
                                ObjItem.setAttribute("LAST_LANDED_RATE", rsGRN.getDouble("LANDED_RATE"));
                            }
                        }
                    }
                    else {
                        
                        stGRN=Conn.createStatement();
                        rsGRN=stGRN.executeQuery("SELECT D_INV_GRN_HEADER.GRN_NO,GRN_DATE,SUM(QTY) AS THEQTY,AVG(LANDED_RATE) AS LANDED_RATE FROM D_INV_GRN_HEADER,D_INV_GRN_DETAIL WHERE D_INV_GRN_DETAIL.QTY>0 AND D_INV_GRN_HEADER.COMPANY_ID=D_INV_GRN_DETAIL.COMPANY_ID AND D_INV_GRN_HEADER.GRN_NO=D_INV_GRN_DETAIL.GRN_NO AND D_INV_GRN_HEADER.GRN_TYPE=D_INV_GRN_DETAIL.GRN_TYPE AND D_INV_GRN_DETAIL.ITEM_ID='"+ItemID+"' AND D_INV_GRN_HEADER.APPROVED=1 AND D_INV_GRN_HEADER.CANCELLED=0 GROUP BY D_INV_GRN_DETAIL.GRN_NO,GRN_DATE,D_INV_GRN_DETAIL.GRN_TYPE ORDER BY GRN_NO DESC,GRN_DATE DESC LIMIT 1");
                        rsGRN.first();
                        
                        if(rsGRN.getRow()>0) {
                            ObjItem.setAttribute("LAST_LANDED_RATE", rsGRN.getDouble("LANDED_RATE"));
                        }
                    }
                    //======================================================================//
                    
                    
                    
                }
                List.put(Long.toString(Counter), ObjItem);
            }
            
        }
        catch(Exception e) {
            
            //JOptionPane.showMessageDialog(null,"Error from Quotation"+e.getMessage());
            
        }
        return List;
    }
    
    public static HashMap getQuotItemListEx(int pCompanyID, String pQuotNo, boolean pAllData) {
        int Counter1 = 0;
        HashMap List = new HashMap();
        long Counter = 0L;
        try {
            Statement stIndent=null;
            ResultSet rsIndent=null;
            
            Connection tmpConn = data.getConn();
            Statement tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rsTmp = tmpStmt.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND QUOT_ID='" + pQuotNo + "' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            if(rsTmp.getRow() > 0) {
                String strSql;
                if(pAllData) {
                    strSql = "SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " WHERE QUOT_ID='" + pQuotNo.trim() + "' ORDER BY ITEM_ID";
                } else {
                    strSql = "SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND QUOT_ID='" + pQuotNo.trim() + "' AND PO_QTY<=QTY ORDER BY ITEM_ID";
                }
                tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp = tmpStmt.executeQuery(strSql);
                rsTmp.first();
                Counter1 = 0;
                for(; !rsTmp.isAfterLast(); rsTmp.next()) {
                    Counter1++;
                    clsQuotationItem ObjItems = new clsQuotationItem();
                    ObjItems.setAttribute("COMPANY_ID", rsTmp.getInt("COMPANY_ID"));
                    ObjItems.setAttribute("QUOT_ID", rsTmp.getString("QUOT_ID"));
                    ObjItems.setAttribute("SR_NO", rsTmp.getInt("SR_NO"));
                    ObjItems.setAttribute("INQUIRY_NO", rsTmp.getString("INQUIRY_NO"));
                    ObjItems.setAttribute("INQUIRY_SRNO", rsTmp.getInt("INQUIRY_SRNO"));
                    ObjItems.setAttribute("ITEM_ID", rsTmp.getString("ITEM_ID"));
                    ObjItems.setAttribute("ITEM_EXTRA_DESC", rsTmp.getString("ITEM_EXTRA_DESC"));
                    ObjItems.setAttribute("HSN_SAC_CODE", rsTmp.getString("HSN_SAC_CODE"));
                    ObjItems.setAttribute("SUPP_ITEM_DESC", rsTmp.getString("SUPP_ITEM_DESC"));
                    ObjItems.setAttribute("PRICE_LIST_NO", rsTmp.getString("PRICE_LIST_NO"));
                    ObjItems.setAttribute("DELIVERY_DATE", rsTmp.getString("DELIVERY_DATE"));
                    ObjItems.setAttribute("UNIT", rsTmp.getInt("UNIT"));
                    ObjItems.setAttribute("EXCISE_GATEPASS_GIVEN", rsTmp.getBoolean("EXCISE_GATEPASS_GIVEN"));
                    ObjItems.setAttribute("QTY", rsTmp.getDouble("QTY"));
                    ObjItems.setAttribute("RATE", rsTmp.getDouble("RATE"));
                    ObjItems.setAttribute("TOTAL_AMOUNT", rsTmp.getDouble("TOTAL_AMOUNT"));
                    ObjItems.setAttribute("ACCESS_AMOUNT", rsTmp.getDouble("ACCESS_AMOUNT"));
                    ObjItems.setAttribute("REMARKS", rsTmp.getString("REMARKS"));
                    ObjItems.setAttribute("APPROVED", rsTmp.getInt("APPROVED"));
                    ObjItems.setAttribute("PO_QTY", rsTmp.getDouble("PO_QTY"));
                    ObjItems.setAttribute("BAL_QTY", rsTmp.getDouble("BAL_QTY"));
                    ObjItems.setAttribute("COLUMN_1_ID", rsTmp.getInt("COLUMN_1_ID"));
                    ObjItems.setAttribute("COLUMN_1_FORMULA", rsTmp.getString("COLUMN_1_FORMULA"));
                    ObjItems.setAttribute("COLUMN_1_PER", rsTmp.getDouble("COLUMN_1_PER"));
                    ObjItems.setAttribute("COLUMN_1_AMT", rsTmp.getDouble("COLUMN_1_AMT"));
                    ObjItems.setAttribute("COLUMN_1_CAPTION", rsTmp.getString("COLUMN_1_CAPTION"));
                    ObjItems.setAttribute("COLUMN_2_ID", rsTmp.getInt("COLUMN_2_ID"));
                    ObjItems.setAttribute("COLUMN_2_FORMULA", rsTmp.getString("COLUMN_2_FORMULA"));
                    ObjItems.setAttribute("COLUMN_2_PER", rsTmp.getDouble("COLUMN_2_PER"));
                    ObjItems.setAttribute("COLUMN_2_AMT", rsTmp.getDouble("COLUMN_2_AMT"));
                    ObjItems.setAttribute("COLUMN_2_CAPTION", rsTmp.getString("COLUMN_2_CAPTION"));
                    ObjItems.setAttribute("COLUMN_3_ID", rsTmp.getInt("COLUMN_3_ID"));
                    ObjItems.setAttribute("COLUMN_3_FORMULA", rsTmp.getString("COLUMN_3_FORMULA"));
                    ObjItems.setAttribute("COLUMN_3_PER", rsTmp.getDouble("COLUMN_3_PER"));
                    ObjItems.setAttribute("COLUMN_3_AMT", rsTmp.getDouble("COLUMN_3_AMT"));
                    ObjItems.setAttribute("COLUMN_3_CAPTION", rsTmp.getString("COLUMN_3_CAPTION"));
                    ObjItems.setAttribute("COLUMN_4_ID", rsTmp.getInt("COLUMN_4_ID"));
                    ObjItems.setAttribute("COLUMN_4_FORMULA", rsTmp.getString("COLUMN_4_FORMULA"));
                    ObjItems.setAttribute("COLUMN_4_PER", rsTmp.getDouble("COLUMN_4_PER"));
                    ObjItems.setAttribute("COLUMN_4_AMT", rsTmp.getDouble("COLUMN_4_AMT"));
                    ObjItems.setAttribute("COLUMN_4_CAPTION", rsTmp.getString("COLUMN_4_CAPTION"));
                    ObjItems.setAttribute("COLUMN_5_ID", rsTmp.getInt("COLUMN_5_ID"));
                    ObjItems.setAttribute("COLUMN_5_FORMULA", rsTmp.getString("COLUMN_5_FORMULA"));
                    ObjItems.setAttribute("COLUMN_5_PER", rsTmp.getDouble("COLUMN_5_PER"));
                    ObjItems.setAttribute("COLUMN_5_AMT", rsTmp.getDouble("COLUMN_5_AMT"));
                    ObjItems.setAttribute("COLUMN_5_CAPTION", rsTmp.getString("COLUMN_5_CAPTION"));
                    ObjItems.setAttribute("COLUMN_6_ID", rsTmp.getInt("COLUMN_6_ID"));
                    ObjItems.setAttribute("COLUMN_6_FORMULA", rsTmp.getString("COLUMN_6_FORMULA"));
                    ObjItems.setAttribute("COLUMN_6_PER", rsTmp.getDouble("COLUMN_6_PER"));
                    ObjItems.setAttribute("COLUMN_6_AMT", rsTmp.getDouble("COLUMN_6_AMT"));
                    ObjItems.setAttribute("COLUMN_6_CAPTION", rsTmp.getString("COLUMN_6_CAPTION"));
                    ObjItems.setAttribute("COLUMN_7_ID", rsTmp.getInt("COLUMN_7_ID"));
                    ObjItems.setAttribute("COLUMN_7_FORMULA", rsTmp.getString("COLUMN_7_FORMULA"));
                    ObjItems.setAttribute("COLUMN_7_PER", rsTmp.getDouble("COLUMN_7_PER"));
                    ObjItems.setAttribute("COLUMN_7_AMT", rsTmp.getDouble("COLUMN_7_AMT"));
                    ObjItems.setAttribute("COLUMN_7_CAPTION", rsTmp.getString("COLUMN_7_CAPTION"));
                    ObjItems.setAttribute("COLUMN_8_ID", rsTmp.getInt("COLUMN_8_ID"));
                    ObjItems.setAttribute("COLUMN_8_FORMULA", rsTmp.getString("COLUMN_8_FORMULA"));
                    ObjItems.setAttribute("COLUMN_8_PER", rsTmp.getDouble("COLUMN_8_PER"));
                    ObjItems.setAttribute("COLUMN_8_AMT", rsTmp.getDouble("COLUMN_8_AMT"));
                    ObjItems.setAttribute("COLUMN_8_CAPTION", rsTmp.getString("COLUMN_8_CAPTION"));
                    ObjItems.setAttribute("COLUMN_9_ID", rsTmp.getInt("COLUMN_9_ID"));
                    ObjItems.setAttribute("COLUMN_9_FORMULA", rsTmp.getString("COLUMN_9_FORMULA"));
                    ObjItems.setAttribute("COLUMN_9_PER", rsTmp.getDouble("COLUMN_9_PER"));
                    ObjItems.setAttribute("COLUMN_9_AMT", rsTmp.getDouble("COLUMN_9_AMT"));
                    ObjItems.setAttribute("COLUMN_9_CAPTION", rsTmp.getString("COLUMN_9_CAPTION"));
                    ObjItems.setAttribute("COLUMN_10_ID", rsTmp.getInt("COLUMN_10_ID"));
                    ObjItems.setAttribute("COLUMN_10_FORMULA", rsTmp.getString("COLUMN_10_FORMULA"));
                    ObjItems.setAttribute("COLUMN_10_PER", rsTmp.getDouble("COLUMN_10_PER"));
                    ObjItems.setAttribute("COLUMN_10_AMT", rsTmp.getDouble("COLUMN_10_AMT"));
                    ObjItems.setAttribute("COLUMN_10_CAPTION", rsTmp.getString("COLUMN_10_CAPTION"));
                    ObjItems.setAttribute("COLUMN_11_ID", rsTmp.getInt("COLUMN_11_ID"));
                    ObjItems.setAttribute("COLUMN_11_FORMULA", rsTmp.getString("COLUMN_11_FORMULA"));
                    ObjItems.setAttribute("COLUMN_11_PER", rsTmp.getDouble("COLUMN_11_PER"));
                    ObjItems.setAttribute("COLUMN_11_AMT", rsTmp.getDouble("COLUMN_11_AMT"));
                    ObjItems.setAttribute("COLUMN_11_CAPTION", rsTmp.getString("COLUMN_11_CAPTION"));
                    ObjItems.setAttribute("COLUMN_12_ID", rsTmp.getInt("COLUMN_12_ID"));
                    ObjItems.setAttribute("COLUMN_12_FORMULA", rsTmp.getString("COLUMN_12_FORMULA"));
                    ObjItems.setAttribute("COLUMN_12_PER", rsTmp.getDouble("COLUMN_12_PER"));
                    ObjItems.setAttribute("COLUMN_12_AMT", rsTmp.getDouble("COLUMN_12_AMT"));
                    ObjItems.setAttribute("COLUMN_12_CAPTION", rsTmp.getString("COLUMN_12_CAPTION"));
                    ObjItems.setAttribute("COLUMN_13_ID", rsTmp.getInt("COLUMN_13_ID"));
                    ObjItems.setAttribute("COLUMN_13_FORMULA", rsTmp.getString("COLUMN_13_FORMULA"));
                    ObjItems.setAttribute("COLUMN_13_PER", rsTmp.getDouble("COLUMN_13_PER"));
                    ObjItems.setAttribute("COLUMN_13_AMT", rsTmp.getDouble("COLUMN_13_AMT"));
                    ObjItems.setAttribute("COLUMN_13_CAPTION", rsTmp.getString("COLUMN_13_CAPTION"));
                    ObjItems.setAttribute("COLUMN_14_ID", rsTmp.getInt("COLUMN_14_ID"));
                    ObjItems.setAttribute("COLUMN_14_FORMULA", rsTmp.getString("COLUMN_14_FORMULA"));
                    ObjItems.setAttribute("COLUMN_14_PER", rsTmp.getDouble("COLUMN_14_PER"));
                    ObjItems.setAttribute("COLUMN_14_AMT", rsTmp.getDouble("COLUMN_14_AMT"));
                    ObjItems.setAttribute("COLUMN_14_CAPTION", rsTmp.getString("COLUMN_14_CAPTION"));
                    ObjItems.setAttribute("COLUMN_15_ID", rsTmp.getInt("COLUMN_15_ID"));
                    ObjItems.setAttribute("COLUMN_15_FORMULA", rsTmp.getString("COLUMN_15_FORMULA"));
                    ObjItems.setAttribute("COLUMN_15_PER", rsTmp.getDouble("COLUMN_15_PER"));
                    ObjItems.setAttribute("COLUMN_15_AMT", rsTmp.getDouble("COLUMN_15_AMT"));
                    ObjItems.setAttribute("COLUMN_15_CAPTION", rsTmp.getString("COLUMN_15_CAPTION"));
                    ObjItems.setAttribute("COLUMN_16_ID", rsTmp.getInt("COLUMN_16_ID"));
                    ObjItems.setAttribute("COLUMN_16_FORMULA", rsTmp.getString("COLUMN_16_FORMULA"));
                    ObjItems.setAttribute("COLUMN_16_PER", rsTmp.getDouble("COLUMN_16_PER"));
                    ObjItems.setAttribute("COLUMN_16_AMT", rsTmp.getDouble("COLUMN_16_AMT"));
                    ObjItems.setAttribute("COLUMN_16_CAPTION", rsTmp.getString("COLUMN_16_CAPTION"));
                    
                    
                    ObjItems.setAttribute("INDENT_NO","");
                    ObjItems.setAttribute("INDENT_SR_NO",0);
                    ObjItems.setAttribute("DEPT_ID",0);
                    
                    //Find out the Indent No. by referring inquiry
                    if(!rsTmp.getString("INQUIRY_NO").trim().equals("")&&rsTmp.getInt("INQUIRY_SRNO")>0) {
                        String InquiryNo=rsTmp.getString("INQUIRY_NO");
                        int InquirySrNo=rsTmp.getInt("INQUIRY_SRNO");
                        
                        stIndent=tmpConn.createStatement();
                        rsIndent=stIndent.executeQuery("SELECT INDENT_NO,INDENT_SRNO FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND SR_NO="+InquirySrNo);
                        rsIndent.first();
                        
                        if(rsIndent.getRow()>0) {
                            String IndentNo=rsIndent.getString("INDENT_NO");
                            int IndentSrNo=rsIndent.getInt("INDENT_SRNO");
                            int DeptID=0;
                            
                            DeptID=clsIndent.getDeptID(EITLERPGLOBAL.gCompanyID, IndentNo);
                            
                            ObjItems.setAttribute("INDENT_NO",IndentNo);
                            ObjItems.setAttribute("INDENT_SR_NO",IndentSrNo);
                            ObjItems.setAttribute("DEPT_ID",DeptID);
                            
                        }
                        
                    }
                    
                    List.put(Integer.toString(Counter1), ObjItems);
                }
                
            }
            
            stIndent.close();
            rsIndent.close();
            
            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            
        }
        catch(Exception e) { }
        return List;
    }
    
    
    public static boolean IsIndentExist(int pCompanyID,String pQuotationNo,int pSrNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strSQL="";
        String InquiryNo="";
        int InquirySrNo=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+pQuotationNo+"' AND SR_NO="+pSrNo;
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                InquiryNo=rsTmp.getString("INQUIRY_NO");
                InquirySrNo=rsTmp.getInt("INQUIRY_SRNO");
                
                strSQL="SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND SR_NO="+InquirySrNo;
                stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=stTmp.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    String IndentNo=rsTmp.getString("INDENT_NO");
                    int IndentSrNo=rsTmp.getInt("INDENT_SRNO");
                    
                    if(!IndentNo.trim().equals("")) {
                        
                        //tmpConn.close();
                        stTmp.close();
                        rsTmp.close();
                        
                        return true;
                    }
                    else {
                        //tmpConn.close();
                        stTmp.close();
                        rsTmp.close();
                        
                        return false;
                    }
                }
                else {
                    //tmpConn.close();
                    stTmp.close();
                    rsTmp.close();
                    
                    return false;
                }
            }
            else {
                
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return false;
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_QUOT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND QUOT_ID='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsQuotation ObjQuotation=new clsQuotation();
                
                ObjQuotation.setAttribute("QUOT_ID",rsTmp.getString("QUOT_ID"));
                ObjQuotation.setAttribute("QUOT_DATE",rsTmp.getString("QUOT_DATE"));
                ObjQuotation.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjQuotation.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjQuotation.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjQuotation.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjQuotation.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjQuotation);
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
    
    public static int getIndentDeptID(int pCompanyID,String pQuotNo,int pSrNo) {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            int DeptID=0;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT INQUIRY_NO,INQUIRY_SRNO FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+pQuotNo+"' AND SR_NO="+pSrNo);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                String InquiryNo=rsTmp.getString("INQUIRY_NO");
                int InquirySrNo=rsTmp.getInt("INQUIRY_SRNO");
                
                rsTmp.close();
                stTmp=tmpConn.createStatement();
                rsTmp=stTmp.executeQuery("SELECT INDENT_NO FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND SR_NO="+InquirySrNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    String IndentNo=rsTmp.getString("INDENT_NO");
                    DeptID=clsIndent.getDeptID(EITLERPGLOBAL.gCompanyID,IndentNo);
                }
            }
            
            return DeptID;
            
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    /*public HashMap getQuotItemList(int pCompanyID,String pInquiryNo) {
        ResultSet rsTmp;
        Statement tmpStmt;
        String strSQL;
     
        HashMap List=new HashMap();
        long Counter=0;
        double lnLandingCost=0,lnNetAmount=0,lnTaxAmount=0;
        clsTaxColumn ObjTax=new clsTaxColumn();
        clsColumn ObjColumn=new clsColumn();
     
        try {
            //Load the Data of Tax columns
            ObjTax.LoadData(pCompanyID);
            ObjColumn.LoadData(pCompanyID);
     
            //Build the SQL Query
            strSQL="SELECT D_PUR_QUOT_DETAIL.*,D_PUR_QUOT_HEADER.* FROM D_PUR_QUOT_HEADER,D_PUR_QUOT_DETAIL WHERE D_PUR_QUOT_HEADER.COMPANY_ID=D_PUR_QUOT_DETAIL.COMPANY_ID AND D_PUR_QUOT_HEADER.QUOT_ID=D_PUR_QUOT_DETAIL.QUOT_ID AND D_PUR_QUOT_HEADER.COMPANY_ID="+pCompanyID+" AND TRIM(D_PUR_QUOT_DETAIL.INQUIRY_NO)='"+pInquiryNo+"' AND D_PUR_QUOT_HEADER.APPROVED=1";
     
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
     
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                clsQuotApprovalItem ObjItem=new clsQuotApprovalItem();
                ObjItem.setAttribute("SUPP_ID",rsTmp.getString("D_PUR_QUOT_HEADER.SUPP_ID"));
                ObjItem.setAttribute("QTY",rsTmp.getDouble("D_PUR_QUOT_DETAIL.QTY"));
                ObjItem.setAttribute("RATE",rsTmp.getDouble("D_PUR_QUOT_DETAIL.RATE"));
                ObjItem.setAttribute("ITEM_CODE",rsTmp.getString("D_PUR_QUOT_DETAIL.ITEM_ID"));
                ObjItem.setAttribute("INQUIRY_NO",rsTmp.getString("D_PUR_QUOT_DETAIL.INQUIRY_NO"));
                ObjItem.setAttribute("INQUIRY_SR_NO",rsTmp.getInt("D_PUR_QUOT_DETAIL.INQUIRY_SRNO"));
                ObjItem.setAttribute("QUOT_ID",rsTmp.getString("D_PUR_QUOT_DETAIL.QUOT_ID"));
                ObjItem.setAttribute("QUOT_SR_NO",rsTmp.getInt("D_PUR_QUOT_DETAIL.SR_NO"));
     
     
                Counter=Counter+1;
                //Calculating Landing Cost first
                lnLandingCost=rsTmp.getDouble("D_PUR_QUOT_DETAIL.ACCESS_AMOUNT"); //Accessable Amount
     
                lnNetAmount=rsTmp.getDouble("D_PUR_QUOT_DETAIL.TOTAL_AMOUNT");
                ObjItem.setAttribute("NET_AMOUNT",lnNetAmount);
     
                //Line level Taxes
                lnTaxAmount=rsTmp.getDouble("D_PUR_QUOT_DETAIL.TOTAL_AMOUNT")-lnLandingCost;
     
                //Setup the columns
                SetupColumns_H();
     
                //Fill Line level Gross Amount,Net Amount, Access Amount and Qty,Rate
                {
                clsColumn tmpObj=new clsColumn();
                tmpObj.setAttribute("VARIABLE","SUM_GROSS_AMOUNT");
                tmpObj.setAttribute("VALUE",rsTmp.getDouble("D_PUR_QUOT_DETAIL.TOTAL_AMOUNT"));
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),tmpObj);
     
                tmpObj=new clsColumn();
                tmpObj.setAttribute("VARIABLE","SUM_NET_AMOUNT");
                tmpObj.setAttribute("VALUE",rsTmp.getDouble("D_PUR_QUOT_DETAIL.ACCESS_AMOUNT"));
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),tmpObj);
     
                tmpObj=new clsColumn();
                tmpObj.setAttribute("VARIABLE","SUM_ACCESS_AMOUNT");
                tmpObj.setAttribute("VALUE",rsTmp.getDouble("D_PUR_QUOT_DETAIL.ACCESS_AMOUNT"));
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),tmpObj);
     
                tmpObj=new clsColumn();
                tmpObj.setAttribute("VARIABLE","SUM_RATE");
                tmpObj.setAttribute("VALUE",rsTmp.getDouble("D_PUR_QUOT_DETAIL.RATE"));
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),tmpObj);
     
                tmpObj=new clsColumn();
                tmpObj.setAttribute("VARIABLE","SUM_QTY");
                tmpObj.setAttribute("VALUE",rsTmp.getDouble("D_PUR_QUOT_DETAIL.QTY"));
                colLineColumns.put(Integer.toString(colLineColumns.size()+1),tmpObj);
                }
     
                //Fill Up the Values with current
                int cnt=0;
                for(int i=1;i<=10;i++) {
                    int ColID=rsTmp.getInt("D_PUR_QUOT_HEADER.COLUMN_"+i+"_ID");
                    if(ColID>0) {
                        int TaxID=ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                        if(ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                            cnt++;
                            double lnPercentValue=rsTmp.getDouble("D_PUR_QUOT_HEADER.COLUMN_"+i+"_PER");
     
                            clsColumn tmpObj=(clsColumn)colHeaderColumns.get(Integer.toString(cnt));
                            tmpObj.setAttribute("VALUE",lnPercentValue);
                            colHeaderColumns.put(Integer.toString(cnt),tmpObj);
     
                            cnt++;
                            double lnValue=rsTmp.getDouble("D_PUR_QUOT_HEADER.COLUMN_"+i+"_AMT");
                            clsColumn tmpObj2=(clsColumn)colHeaderColumns.get(Integer.toString(cnt));
                            //tmpObj2.setAttribute("VALUE",lnValue);
                            tmpObj2.setAttribute("VALUE",0);
                            colHeaderColumns.put(Integer.toString(cnt),tmpObj2);
                        }
                        else {
                            cnt++;
                            double lnValue=rsTmp.getDouble("D_PUR_QUOT_HEADER.COLUMN_"+i+"_AMT");
                            clsColumn tmpObj2=(clsColumn)colHeaderColumns.get(Integer.toString(cnt));
                            tmpObj2.setAttribute("VALUE",lnValue);
                            colHeaderColumns.put(Integer.toString(cnt),tmpObj2);
                        }
                    }
                }
     
     
                //Fill Up the Values with current
                cnt=0;
                for(int i=1;i<=10;i++) {
                    int ColID=rsTmp.getInt("D_PUR_QUOT_DETAIL.COLUMN_"+i+"_ID");
                    if(ColID>0) {
                        int TaxID=ObjColumn.getTaxID(EITLERPGLOBAL.gCompanyID, ColID);
                        if(ObjTax.getUsePercentage(EITLERPGLOBAL.gCompanyID, TaxID)) {
                            cnt++;
                            double lnPercentValue=rsTmp.getDouble("D_PUR_QUOT_DETAIL.COLUMN_"+i+"_PER");
     
                            clsColumn tmpObj=(clsColumn)colLineColumns.get(Integer.toString(cnt));
                            tmpObj.setAttribute("VALUE",lnPercentValue);
                            colLineColumns.put(Integer.toString(cnt),tmpObj);
     
                            cnt++;
                            double lnValue=rsTmp.getDouble("D_PUR_QUOT_DETAIL.COLUMN_"+i+"_AMT");
                            clsColumn tmpObj2=(clsColumn)colLineColumns.get(Integer.toString(cnt));
                            tmpObj2.setAttribute("VALUE",lnValue);
                            colLineColumns.put(Integer.toString(cnt),tmpObj2);
                        }
                        else {
                            cnt++;
                            double lnValue=rsTmp.getDouble("D_PUR_QUOT_DETAIL.COLUMN_"+i+"_AMT");
                            clsColumn tmpObj2=(clsColumn)colLineColumns.get(Integer.toString(cnt));
                            tmpObj2.setAttribute("VALUE",lnValue);
                            colLineColumns.put(Integer.toString(cnt),tmpObj2);
                        }
                    }
                }
     
                for(int i=1;i<=colHeaderColumns.size();i++) {
                    //Call Update Results for each element
                    UpdateResults_H(i);
                }
     
                //Get the Updated Amount
                double lnTemp=lnLandingCost;
     
                lnLandingCost=GetUpdatedAmount(lnLandingCost);
     
                lnTaxAmount=lnTaxAmount+(lnLandingCost-lnTemp);
                ObjItem.setAttribute("TAX_AMOUNT",lnTaxAmount);
                ObjItem.setAttribute("LAND_COST",EITLERPGLOBAL.round(lnLandingCost,2));
     
                List.put(Long.toString(Counter),ObjItem);
     
                rsTmp.next();
            }//end of while
        }
        catch(Exception e) {
        }
     
        return List;
    }*/
    
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT QUOT_ID,APPROVED,CANCELLED FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    
                    if(rsTmp.getBoolean("CANCELLED")) {
                        strMessage="Document is cancelled";
                    }
                    else {
                        
                        String QuotNo=rsTmp.getString("QUOT_ID");
                        String strSQL="SELECT C.INDENT_NO,C.APPROVED,C.CANCELED FROM D_PUR_QUOT_DETAIL A,D_PUR_INQUIRY_DETAIL B,D_INV_INDENT_HEADER C WHERE A.INQUIRY_NO=B.INQUIRY_NO AND A.INQUIRY_SRNO=B.SR_NO  AND B.INDENT_NO=C.INDENT_NO AND ( C.APPROVED=0 OR C.CANCELED=1) AND A.QUOT_ID='"+QuotNo+"'";
                        
                        rsTmp=data.getResult(strSQL);
                        if(rsTmp.getRow()>0) {
                            strMessage="Indent is not approved.";
                        }
                        else {
                            
                            
                            strMessage="";
                        }
                        
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
    
    
    public static boolean CanCancel(int pCompanyID,String pQuotID) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT QUOT_ID FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+pQuotID+"' AND CANCELLED=0");
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
    
    
    public static boolean CancelQuotation(int pCompanyID,String pQuotID) {
        
        ResultSet rsTmp=null,rsQuot=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pQuotID)) {
                
                boolean ApprovedQuot=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_PUR_QUOT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+pQuotID+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedQuot=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedQuot) {
                    
                }
                else {
                    int ModuleID=19;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pQuotID+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_QUOT_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+pQuotID+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsQuot.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
}
