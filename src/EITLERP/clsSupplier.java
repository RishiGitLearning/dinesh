/*
 * clsSupplier.java
 *
 * Created on April 13, 2004, 9:02 AM
 */

package EITLERP;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author  jadave
 * @version
 */

public class clsSupplier {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    //Supplier Item Collection
    public HashMap colSupplierItems;
    public HashMap colSuppTerms=new HashMap();
    public HashMap colSuppChilds=new HashMap();
    
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
    public clsSupplier() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SUPP_ID",new Variant(0));
        props.put("SUPPLIER_CODE",new Variant(""));
        props.put("DUMMY_SUPPLIER_CODE",new Variant(""));
        props.put("SUPP_NAME",new Variant(""));
        props.put("ATTN",new Variant(""));
        props.put("ADD1",new Variant(""));
        props.put("ADD2",new Variant(""));
        props.put("ADD3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("STATE",new Variant(""));
        props.put("COUNTRY",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("PHONE_O",new Variant(""));
        props.put("PHONE_RES",new Variant(""));
        props.put("FAX_NO",new Variant(""));
        props.put("MOBILE_NO",new Variant(""));
        props.put("EMAIL_ADD",new Variant(""));
        props.put("URL",new Variant(""));
        props.put("WEB_SITE",new Variant(""));
        props.put("REGISTERATION",new Variant(""));
        props.put("SERVICETAX_NO",new Variant(""));
        props.put("SERVICETAX_DATE",new Variant(""));
        props.put("CST_NO",new Variant(""));
        props.put("CST_DATE",new Variant(""));
        props.put("GST_NO",new Variant(""));
        props.put("GST_DATE",new Variant(""));
        props.put("GSTIN_NO",new Variant(""));
        props.put("GSTIN_DATE",new Variant(""));
        props.put("ECC_NO",new Variant(""));
        props.put("ECC_DATE",new Variant(""));
        props.put("SSIREG",new Variant(false));
        props.put("SSIREG_NO",new Variant(""));
        props.put("SSIREG_DATE",new Variant(""));
        props.put("ESIREG_NO",new Variant(""));
        props.put("ESIREG_DATE",new Variant(""));
        props.put("PAN_NO",new Variant(""));
        props.put("PAN_DATE",new Variant(""));
        props.put("FORM",new Variant(0));
        props.put("ST35_REGISTERED",new Variant(false));
        props.put("CONTACT_PERSON_1",new Variant(""));
        props.put("CONTACT_PERSON_2",new Variant(""));
        props.put("ONETIME_SUPPLIER",new Variant(false));
        props.put("FROM_DATE_REG",new Variant(""));
        props.put("TO_DATE_REG",new Variant(""));
        props.put("BLOCKED",new Variant(""));
        props.put("SLOW_MOVING",new Variant(false));
        props.put("PAYMENT_DAYS",new Variant(0));
        props.put("LAST_PO_NO",new Variant(""));
        props.put("LAST_TRANS_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("BANK_ID",new Variant(0));
        props.put("BANK_NAME",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("PAYMENT_TERM_CODE",new Variant(0));
        props.put("PROPOSED_ITEM",new Variant(""));
        props.put("PLACE_OF_SUPPLY",new Variant(""));
        props.put("STATE_CODE",new Variant(""));
        props.put("STATE_GST_CODE",new Variant(""));
        props.put("MSME",new Variant(false));
        props.put("MSME_UAN",new Variant(""));
        props.put("MSME_DIC_NO",new Variant(""));
        
        props.put("DISTANCE_KM",new Variant(0));
         
        props.put("APPROVED",new Variant(false));
        props.put("CANCELLED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        
        props.put("PARENT_SUPP_ID",new Variant(0));
        
        props.put("DISTANCE_KM",new Variant(0));
        //Create a new object for Supplier Item collection
        colSupplierItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
    }
    
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " ORDER BY SUPP_NAME";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            HistoryView=false;
            Ready=true;
            MoveFirst();
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
        Statement stHistory,stHCategory,stHTerms,stHChilds,stParent;
        ResultSet rsHistory,rsHCategory,rsHTerms,rsHChilds,rsParent;
        
        try {
            
            
            if(!Validate()) {
                return false;
            }
            
            String newURL=clsFinYear.getDBURL(3, EITLERPGLOBAL.FinYearFrom);
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHCategory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SUPP_MASTER_H WHERE SUPP_ID=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHCategory=stHCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY_H WHERE SUPP_ID=1");
            rsHCategory.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS_H WHERE SUPP_ID=1");
            rsHTerms.first();
            rsHChilds=stHChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS_H WHERE SUPP_ID=1");
            rsHChilds.first();
            //------------------------------------//
            
            //rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            setAttribute("SUPP_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_SUPP_MASTER","SUPP_ID"));
            
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID", (long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("SUPP_ID", (long)getAttribute("SUPP_ID").getVal());
            rsResultSet.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            String DummyCode=Long.toString((long)getAttribute("SUPP_ID").getVal());
            rsResultSet.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
            rsResultSet.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            //rsResultSet.updateInt("STATE",(int)getAttribute("STATE").getVal());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsResultSet.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsResultSet.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            //rsResultSet.updateInt("COUNTRY",(int)getAttribute("COUNTRY").getVal());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
            rsResultSet.updateString("PHONE_RES",(String)getAttribute("PHONE_RES").getObj());
            rsResultSet.updateString("FAX_NO",(String)getAttribute("FAX_NO").getObj());
            rsResultSet.updateString("MOBILE_NO",(String)getAttribute("MOBILE_NO").getObj());
            rsResultSet.updateString("EMAIL_ADD",(String)getAttribute("EMAIL_ADD").getObj());
            rsResultSet.updateString("URL",(String)getAttribute("URL").getObj());
            rsResultSet.updateString("WEB_SITE",(String)getAttribute("WEB_SITE").getObj());
            rsResultSet.updateString("REGISTERATION",(String)getAttribute("REGISTERATION").getObj());
            rsResultSet.updateString("SERVICETAX_NO",(String)getAttribute("SERVICETAX_NO").getObj());
            rsResultSet.updateString("SERVICETAX_DATE",(String)getAttribute("SERVICETAX_DATE").getObj());
            rsResultSet.updateString("CST_NO",(String)getAttribute("CST_NO").getObj());
            rsResultSet.updateString("CST_DATE",(String)getAttribute("CST_DATE").getObj());
            rsResultSet.updateString("GST_NO",(String)getAttribute("GST_NO").getObj());
            rsResultSet.updateString("GST_DATE",(String)getAttribute("GST_DATE").getObj());
            rsResultSet.updateString("GSTIN_NO",(String)getAttribute("GSTIN_NO").getObj());
            rsResultSet.updateString("GSTIN_DATE",(String)getAttribute("GSTIN_DATE").getObj());
            rsResultSet.updateString("ECC_NO",(String)getAttribute("ECC_NO").getObj());
            rsResultSet.updateString("ECC_DATE",(String)getAttribute("ECC_DATE").getObj());
            rsResultSet.updateString("SSIREG_NO",(String)getAttribute("SSIREG_NO").getObj());
            rsResultSet.updateString("SSIREG_DATE",(String)getAttribute("SSIREG_DATE").getObj());
            rsResultSet.updateString("ESIREG_NO",(String)getAttribute("ESIREG_NO").getObj());
            rsResultSet.updateString("ESIREG_DATE",(String)getAttribute("ESIREG_DATE").getObj());
            
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsResultSet.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());
    
            rsResultSet.updateBoolean("MSME",(boolean)getAttribute("MSME").getBool());
            rsResultSet.updateString("MSME_UAN",getAttribute("MSME_UAN").getString());
            rsResultSet.updateString("MSME_DIC_NO",getAttribute("MSME_DIC_NO").getString());

            rsResultSet.updateLong("FORM",(long)getAttribute("FORM").getVal());
            rsResultSet.updateString("CONTACT_PERSON_1",(String)getAttribute("CONTACT_PERSON_1").getObj());
            rsResultSet.updateString("CONTACT_PERSON_2",(String)getAttribute("CONTACT_PERSON_2").getObj());
            rsResultSet.updateBoolean("ONETIME_SUPPLIER",(boolean)getAttribute("ONETIME_SUPPLIER").getBool());
            if(getAttribute("FROM_DATE_REG").getObj().equals(""))
            {
                rsResultSet.updateString("FROM_DATE_REG",null);
            }
            else
            {
                rsResultSet.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            }
            if(getAttribute("TO_DATE_REG").getObj().equals(""))
            {
                rsResultSet.updateString("TO_DATE_REG",null);
            }
            else
            {
                rsResultSet.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            }
            //rsResultSet.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            rsResultSet.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
            rsResultSet.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
            rsResultSet.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
            rsResultSet.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
            rsResultSet.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
            rsResultSet.updateInt("PAYMENT_TERM_CODE",(int)getAttribute("PAYMENT_TERM_CODE").getVal());
            rsResultSet.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
//            if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
//            {
//                rsResultSet.updateString("LAST_TRANS_DATE",null);
//            }
//            else
//            {
//                rsResultSet.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            }
            
            rsResultSet.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsResultSet.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
//            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsResultSet.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateBoolean("CANCELLED",false);
            //BHAVESH 
             rsResultSet.updateString("APPROVED_DATE","0000-00-00");
             rsResultSet.updateBoolean("REJECTED",false);
             rsResultSet.updateString("REJECTED_DATE","0000-00-00");
             rsResultSet.updateBoolean("PAYMENT_TERMS",false);
             rsResultSet.updateBoolean("DELIVERY_TERMS",false);
             rsResultSet.updateBoolean("OTHER_TERMS",false);
             rsResultSet.updateString("PTC","");
            /////////////////
            
            
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID", (long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("SUPP_ID", (long)getAttribute("SUPP_ID").getVal());
            rsHistory.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            rsHistory.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
            rsHistory.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            //rsHistory.updateInt("STATE",(int)getAttribute("STATE").getVal());
            rsHistory.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsHistory.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsHistory.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsHistory.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            //rsHistory.updateInt("COUNTRY",(int)getAttribute("COUNTRY").getVal());
            rsHistory.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
            rsHistory.updateString("PHONE_RES",(String)getAttribute("PHONE_RES").getObj());
            rsHistory.updateString("FAX_NO",(String)getAttribute("FAX_NO").getObj());
            rsHistory.updateString("MOBILE_NO",(String)getAttribute("MOBILE_NO").getObj());
            rsHistory.updateString("EMAIL_ADD",(String)getAttribute("EMAIL_ADD").getObj());
            rsHistory.updateString("URL",(String)getAttribute("URL").getObj());
            rsHistory.updateString("WEB_SITE",(String)getAttribute("WEB_SITE").getObj());
            rsHistory.updateString("REGISTERATION",(String)getAttribute("REGISTERATION").getObj());
            rsHistory.updateString("SERVICETAX_NO",(String)getAttribute("SERVICETAX_NO").getObj());
            rsHistory.updateString("SERVICETAX_DATE",(String)getAttribute("SERVICETAX_DATE").getObj());
            rsHistory.updateString("CST_NO",(String)getAttribute("CST_NO").getObj());
            rsHistory.updateString("CST_DATE",(String)getAttribute("CST_DATE").getObj());
            rsHistory.updateString("GST_NO",(String)getAttribute("GST_NO").getObj());
            rsHistory.updateString("GST_DATE",(String)getAttribute("GST_DATE").getObj());
            rsHistory.updateString("GSTIN_NO",(String)getAttribute("GSTIN_NO").getObj());
            rsHistory.updateString("GSTIN_DATE",(String)getAttribute("GSTIN_DATE").getObj());
            rsHistory.updateString("ECC_NO",(String)getAttribute("ECC_NO").getObj());
            rsHistory.updateString("ECC_DATE",(String)getAttribute("ECC_DATE").getObj());
            rsHistory.updateString("SSIREG_NO",(String)getAttribute("SSIREG_NO").getObj());
            rsHistory.updateString("SSIREG_DATE",(String)getAttribute("SSIREG_DATE").getObj());
            rsHistory.updateString("ESIREG_NO",(String)getAttribute("ESIREG_NO").getObj());
            rsHistory.updateString("ESIREG_DATE",(String)getAttribute("ESIREG_DATE").getObj());
            
            rsHistory.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsHistory.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());

rsHistory.updateBoolean("MSME",(boolean)getAttribute("MSME").getBool());
            rsHistory.updateString("MSME_UAN",getAttribute("MSME_UAN").getString());
            rsHistory.updateString("MSME_DIC_NO",getAttribute("MSME_DIC_NO").getString());
            
            rsHistory.updateLong("FORM",(long)getAttribute("FORM").getVal());
            rsHistory.updateString("CONTACT_PERSON_1",(String)getAttribute("CONTACT_PERSON_1").getObj());
            rsHistory.updateString("CONTACT_PERSON_2",(String)getAttribute("CONTACT_PERSON_2").getObj());
            rsHistory.updateBoolean("ONETIME_SUPPLIER",(boolean)getAttribute("ONETIME_SUPPLIER").getBool());
            if(getAttribute("FROM_DATE_REG").getObj().equals(""))
            {
                rsHistory.updateString("FROM_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            }
                
            //rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            if(getAttribute("TO_DATE_REG").getObj().equals(""))
            {
                rsHistory.updateString("TO_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            }
            //rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            rsHistory.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
            rsHistory.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
            rsHistory.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
            rsHistory.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
            rsHistory.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
            rsHistory.updateInt("PAYMENT_TERM_CODE",(int)getAttribute("PAYMENT_TERM_CODE").getVal());
            rsHistory.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
//            if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",null);
//            }
//            else
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            }
            
            rsHistory.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
//            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsHistory.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
            // BHAVESH
             rsHistory.updateBoolean("APPROVED",false);
             rsHistory.updateBoolean("CANCELLED",false);
             rsHistory.updateString("APPROVED_DATE","0000-00-00");
             rsHistory.updateBoolean("REJECTED",false);
             rsHistory.updateString("REJECTED_DATE","0000-00-00");
             rsHistory.updateBoolean("PAYMENT_TERMS",false);
             rsHistory.updateBoolean("DELIVERY_TERMS",false);
             rsHistory.updateBoolean("OTHER_TERMS",false);
             rsHistory.updateString("PTC","");
            
            
            rsHistory.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.insertRow();
            
            
            ResultSet rsTmp,rsTerms,rsChilds;
            Statement tmpStmt,stTerms,stChilds;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            long nSuppID=(long)getAttribute("SUPP_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colSupplierItems.size();i++) {
                clsSupplierItem ObjItems=(clsSupplierItem) colSupplierItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateLong("SUPP_ID",nSuppID);
                rsTmp.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHCategory.moveToInsertRow();
                rsHCategory.updateInt("REVISION_NO",1);
                rsHCategory.updateLong("COMPANY_ID",nCompanyID);
                rsHCategory.updateLong("SUPP_ID",nSuppID);
                rsHCategory.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                rsHCategory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHCategory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHCategory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsHCategory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHCategory.updateBoolean("CHANGED",true);
                rsHCategory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHCategory.insertRow();
            }
            
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS");
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppTerms.size();i++) {
                clsSuppTerms ObjItems=(clsSuppTerms) colSuppTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateLong("COMPANY_ID",nCompanyID);
                rsTerms.updateLong("SUPP_ID",nSuppID);
                rsTerms.updateInt("SR_NO",i);
                rsTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                rsTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                rsTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                rsTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                rsTerms.updateBoolean("CHANGED",true);
                rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();
                
                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO",1);
                rsHTerms.updateLong("COMPANY_ID",nCompanyID);
                rsHTerms.updateLong("SUPP_ID",nSuppID);
                rsHTerms.updateInt("SR_NO",i);
                rsHTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                rsHTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                rsHTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                rsHTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            
            
            stChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsChilds=stChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS");
            
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppChilds.size();i++) {
                clsSuppChilds ObjItems=(clsSuppChilds) colSuppChilds.get(Integer.toString(i));
                rsChilds.moveToInsertRow();
                rsChilds.updateLong("COMPANY_ID",nCompanyID);
                rsChilds.updateLong("SUPP_ID",nSuppID);
                rsChilds.updateInt("SR_NO",i);
                rsChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                rsChilds.updateBoolean("CHANGED",true);
                rsChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsChilds.insertRow();
                
                rsHChilds.moveToInsertRow();
                rsHChilds.updateInt("REVISION_NO",1);
                rsHChilds.updateLong("COMPANY_ID",nCompanyID);
                rsHChilds.updateLong("SUPP_ID",nSuppID);
                rsHChilds.updateInt("SR_NO",i);
                rsHChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                rsHChilds.updateBoolean("CHANGED",true);
                rsHChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHChilds.insertRow();
            }
            
            
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=37; //SUPPLIER MASTER MODULE ID
            ObjFlow.DocNo=Long.toString((long)getAttribute("SUPP_ID").getVal());
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_COM_SUPP_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="SUPP_ID";
            
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
            //================= Approval Flow Update complete ===================//
            
            //==========Sync Party data with other modules =============//
            if(ObjFlow.Status.equals("F")) {
                if(EITLERPGLOBAL.gCompanyID==2) {
                    AddPartyToExternalDB(getAttribute("SUPPLIER_CODE").getString(), newURL);
                }
                AddPartyToUnregMaster(getAttribute("SUPPLIER_CODE").getString());
                AddPartyToFinance(getAttribute("SUPPLIER_CODE").getString());
            }
            //==========================================================//
            
            //=========Add to Supplier in case of parent supplier ------------//
            if(ObjFlow.Status.equals("F")) {
                
                stParent=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsParent=stChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS");
                
                int ParentSuppID=(int)getAttribute("PARENT_SUPP_ID").getVal();
                if(ParentSuppID!=0) {
                    int MaxSrNo=0;
                    rsTmp=data.getResult("SELECT IF(MAX(SR_NO) IS NULL,0,MAX(SR_NO)) AS MAXNO FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+ParentSuppID);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        MaxSrNo=rsTmp.getInt("MAXNO")+1;
                    }
                    
                    rsTmp=data.getResult("SELECT * FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+ParentSuppID+" AND CHILD_SUPP_ID="+nSuppID);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()<=0) {
                        rsParent.moveToInsertRow();
                        rsParent.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsParent.updateInt("SR_NO",MaxSrNo);
                        rsParent.updateInt("SUPP_ID",ParentSuppID);
                        rsParent.updateLong("CHILD_SUPP_ID",nSuppID);
                        rsParent.updateBoolean("CHANGED",true);
                        rsParent.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsParent.insertRow();
                    }
                }
            }
            //=============================================================================//
            
            
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
        Statement stHistory,stHCategory,stHTerms,stHChilds,stParent;
        ResultSet rsHistory,rsHCategory,rsHTerms,rsHChilds,rsParent;
        
        try {
            
            if(!Validate()) {
                return false;
            }
            
            String newURL=clsFinYear.getDBURL(3, EITLERPGLOBAL.FinYearFrom);
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHCategory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SUPP_MASTER_H WHERE SUPP_ID=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHCategory=stHCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY_H WHERE SUPP_ID=1");
            rsHCategory.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS_H WHERE SUPP_ID=1");
            rsHTerms.first();
            rsHChilds=stHChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS_H WHERE SUPP_ID=1");
            rsHChilds.first();
            //------------------------------------//
            
            rsResultSet.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            rsResultSet.updateString("DUMMY_SUPPLIER_CODE",(String) getAttribute("DUMMY_SUPPLIER_CODE").getObj());
            rsResultSet.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            //rsResultSet.updateInt("STATE",(int)getAttribute("STATE").getVal());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsResultSet.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsResultSet.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
            rsResultSet.updateString("PHONE_RES",(String)getAttribute("PHONE_RES").getObj());
            rsResultSet.updateString("FAX_NO",(String)getAttribute("FAX_NO").getObj());
            rsResultSet.updateString("MOBILE_NO",(String)getAttribute("MOBILE_NO").getObj());
            rsResultSet.updateString("EMAIL_ADD",(String)getAttribute("EMAIL_ADD").getObj());
            rsResultSet.updateString("URL",(String)getAttribute("URL").getObj());
            rsResultSet.updateString("WEB_SITE",(String)getAttribute("WEB_SITE").getObj());
            rsResultSet.updateString("REGISTERATION",(String)getAttribute("REGISTERATION").getObj());
            rsResultSet.updateString("SERVICETAX_NO",(String)getAttribute("SERVICETAX_NO").getObj());
            rsResultSet.updateString("SERVICETAX_DATE",(String)getAttribute("SERVICETAX_DATE").getObj());
            rsResultSet.updateString("CST_NO",(String)getAttribute("CST_NO").getObj());
            rsResultSet.updateString("CST_DATE",(String)getAttribute("CST_DATE").getObj());
            rsResultSet.updateString("GST_NO",(String)getAttribute("GST_NO").getObj());
            rsResultSet.updateString("GST_DATE",(String)getAttribute("GST_DATE").getObj());
            rsResultSet.updateString("GSTIN_NO",(String)getAttribute("GSTIN_NO").getObj());
            rsResultSet.updateString("GSTIN_DATE",(String)getAttribute("GSTIN_DATE").getObj());
            rsResultSet.updateString("ECC_NO",(String)getAttribute("ECC_NO").getObj());
            rsResultSet.updateString("ECC_DATE",(String)getAttribute("ECC_DATE").getObj());
            rsResultSet.updateString("SSIREG_NO",(String)getAttribute("SSIREG_NO").getObj());
            rsResultSet.updateString("SSIREG_DATE",(String)getAttribute("SSIREG_DATE").getObj());
            rsResultSet.updateString("ESIREG_NO",(String)getAttribute("ESIREG_NO").getObj());
            rsResultSet.updateString("ESIREG_DATE",(String)getAttribute("ESIREG_DATE").getObj());
            
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsResultSet.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());

            rsResultSet.updateBoolean("MSME",(boolean)getAttribute("MSME").getBool());
            rsResultSet.updateString("MSME_UAN",getAttribute("MSME_UAN").getString());
            rsResultSet.updateString("MSME_DIC_NO",getAttribute("MSME_DIC_NO").getString());
            
            rsResultSet.updateLong("FORM",(long)getAttribute("FORM").getVal());
            rsResultSet.updateString("CONTACT_PERSON_1",(String)getAttribute("CONTACT_PERSON_1").getObj());
            rsResultSet.updateString("CONTACT_PERSON_2",(String)getAttribute("CONTACT_PERSON_2").getObj());
            rsResultSet.updateBoolean("ONETIME_SUPPLIER",(boolean)getAttribute("ONETIME_SUPPLIER").getBool());
            if(getAttribute("FROM_DATE_REG").getObj().equals(""))
            {
                rsResultSet.updateString("FROM_DATE_REG",null);
            }
            else
            {
                rsResultSet.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            }


            //rsResultSet.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            
            if(getAttribute("TO_DATE_REG").getObj().equals(""))
            {
                rsResultSet.updateString("TO_DATE_REG",null);
            }
            else
            {
                rsResultSet.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            }
            //rsResultSet.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            rsResultSet.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
            rsResultSet.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
            rsResultSet.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
            rsResultSet.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
            rsResultSet.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
            rsResultSet.updateInt("PAYMENT_TERM_CODE",(int) getAttribute("PAYMENT_TERM_CODE").getVal());
            
            rsResultSet.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            //rsResultSet.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
            //rsResultSet.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsResultSet.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            
            ///BHAVESH
            
                rsResultSet.updateString("APPROVED_DATE","0000-00-00");
             rsResultSet.updateBoolean("REJECTED",false);
             rsResultSet.updateString("REJECTED_DATE","0000-00-00");
             rsResultSet.updateBoolean("PAYMENT_TERMS",false);
             rsResultSet.updateBoolean("DELIVERY_TERMS",false);
             rsResultSet.updateBoolean("OTHER_TERMS",false);
             rsResultSet.updateString("PTC","");
             /////
            
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_COM_SUPP_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+(int)getAttribute("SUPP_ID").getVal());
            RevNo++;
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID", (long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("SUPP_ID", (long)getAttribute("SUPP_ID").getVal());
            rsHistory.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            rsHistory.updateString("DUMMY_SUPPLIER_CODE",(String) getAttribute("DUMMY_SUPPLIER_CODE").getObj());
            rsHistory.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            //rsHistory.updateInt("STATE",(int)getAttribute("STATE").getVal());
            rsHistory.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsHistory.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsHistory.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsHistory.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsHistory.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
            rsHistory.updateString("PHONE_RES",(String)getAttribute("PHONE_RES").getObj());
            rsHistory.updateString("FAX_NO",(String)getAttribute("FAX_NO").getObj());
            rsHistory.updateString("MOBILE_NO",(String)getAttribute("MOBILE_NO").getObj());
            rsHistory.updateString("EMAIL_ADD",(String)getAttribute("EMAIL_ADD").getObj());
            rsHistory.updateString("URL",(String)getAttribute("URL").getObj());
            rsHistory.updateString("WEB_SITE",(String)getAttribute("WEB_SITE").getObj());
            rsHistory.updateString("REGISTERATION",(String)getAttribute("REGISTERATION").getObj());
            rsHistory.updateString("SERVICETAX_NO",(String)getAttribute("SERVICETAX_NO").getObj());
            rsHistory.updateString("SERVICETAX_DATE",(String)getAttribute("SERVICETAX_DATE").getObj());
            rsHistory.updateString("CST_NO",(String)getAttribute("CST_NO").getObj());
            rsHistory.updateString("CST_DATE",(String)getAttribute("CST_DATE").getObj());
            rsHistory.updateString("GST_NO",(String)getAttribute("GST_NO").getObj());
            rsHistory.updateString("GST_DATE",(String)getAttribute("GST_DATE").getObj());
            rsHistory.updateString("GSTIN_NO",(String)getAttribute("GSTIN_NO").getObj());
            rsHistory.updateString("GSTIN_DATE",(String)getAttribute("GSTIN_DATE").getObj());
            rsHistory.updateString("ECC_NO",(String)getAttribute("ECC_NO").getObj());
            rsHistory.updateString("ECC_DATE",(String)getAttribute("ECC_DATE").getObj());
            rsHistory.updateString("SSIREG_NO",(String)getAttribute("SSIREG_NO").getObj());
            rsHistory.updateString("SSIREG_DATE",(String)getAttribute("SSIREG_DATE").getObj());
            rsHistory.updateString("ESIREG_NO",(String)getAttribute("ESIREG_NO").getObj());
            rsHistory.updateString("ESIREG_DATE",(String)getAttribute("ESIREG_DATE").getObj());
            
            rsHistory.updateString("PAN_NO",(String)getAttribute("PAN_NO").getObj());
            rsHistory.updateString("PAN_DATE",(String)getAttribute("PAN_DATE").getObj());

            rsHistory.updateBoolean("MSME",(boolean)getAttribute("MSME").getBool());
            rsHistory.updateString("MSME_UAN",(String)getAttribute("MSME_UAN").getObj());
            rsHistory.updateString("MSME_DIC_NO",(String)getAttribute("MSME_DIC_NO").getObj());
            
            rsHistory.updateLong("FORM",(long)getAttribute("FORM").getVal());
            rsHistory.updateString("CONTACT_PERSON_1",(String)getAttribute("CONTACT_PERSON_1").getObj());
            rsHistory.updateString("CONTACT_PERSON_2",(String)getAttribute("CONTACT_PERSON_2").getObj());
            rsHistory.updateBoolean("ONETIME_SUPPLIER",(boolean)getAttribute("ONETIME_SUPPLIER").getBool());
            if(getAttribute("FROM_DATE_REG").getObj().equals(""))
            {
                rsHistory.updateString("FROM_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            }

            //rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            if(getAttribute("TO_DATE_REG").getObj().equals(""))
            {
                rsHistory.updateString("TO_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            }
            
            //rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            rsHistory.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
            rsHistory.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
            rsHistory.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
            rsHistory.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
            rsHistory.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
            rsHistory.updateInt("PAYMENT_TERM_CODE",(int) getAttribute("PAYMENT_TERM_CODE").getVal());
            rsHistory.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
            
//            if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",null);
//            }
//            else
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            }
            
            rsHistory.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
            //rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsHistory.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            ///BHAVESH
                rsHistory.updateBoolean("APPROVED",false);
             rsHistory.updateBoolean("CANCELLED",false);
             rsHistory.updateString("APPROVED_DATE","0000-00-00");
             rsHistory.updateBoolean("REJECTED",false);
             rsHistory.updateString("REJECTED_DATE","0000-00-00");
             rsHistory.updateBoolean("PAYMENT_TERMS",false);
             rsHistory.updateBoolean("DELIVERY_TERMS",false);
             rsHistory.updateBoolean("OTHER_TERMS",false);
             rsHistory.updateString("PTC","");
             ///////////////
            
            
            rsHistory.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.insertRow();
            
            
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mSuppID=Long.toString((long)getAttribute("SUPP_ID").getVal());
            
            data.Execute("DELETE FROM D_COM_SUPP_ITEMCATEGORY WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSuppID);
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY");
            long nSuppID=(long)getAttribute("SUPP_ID").getVal();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colSupplierItems.size();i++) {
                clsSupplierItem ObjItems=(clsSupplierItem) colSupplierItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateLong("SUPP_ID",nSuppID);
                rsTmp.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long)ObjItems.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHCategory.moveToInsertRow();
                rsHCategory.updateInt("REVISION_NO",RevNo);
                rsHCategory.updateLong("COMPANY_ID",nCompanyID);
                rsHCategory.updateLong("SUPP_ID",nSuppID);
                rsHCategory.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                rsHCategory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHCategory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHCategory.updateLong("MODIFIED_BY",(long)ObjItems.getAttribute("MODIFIED_BY").getVal());
                rsHCategory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHCategory.updateBoolean("CHANGED",true);
                rsHCategory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHCategory.insertRow();
            }
            
            
            data.Execute("DELETE FROM D_COM_SUPP_TERMS WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSuppID);
            
            ResultSet rsTerms;
            Statement stTerms;
            
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS");
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppTerms.size();i++) {
                clsSuppTerms ObjItems=(clsSuppTerms) colSuppTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateLong("COMPANY_ID",nCompanyID);
                rsTerms.updateLong("SUPP_ID",nSuppID);
                rsTerms.updateInt("SR_NO",i);
                rsTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                rsTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                rsTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                rsTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                rsTerms.updateBoolean("CHANGED",true);
                rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTerms.insertRow();
                
                rsHTerms.moveToInsertRow();
                rsHTerms.updateInt("REVISION_NO",RevNo);
                rsHTerms.updateLong("COMPANY_ID",nCompanyID);
                rsHTerms.updateLong("SUPP_ID",nSuppID);
                rsHTerms.updateInt("SR_NO",i);
                rsHTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                rsHTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                rsHTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                rsHTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            
            
            
            data.Execute("DELETE FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSuppID);
            
            ResultSet rsChilds;
            Statement stChilds;
            
            stChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsChilds=stChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS");
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppChilds.size();i++) {
                clsSuppChilds ObjItems=(clsSuppChilds) colSuppChilds.get(Integer.toString(i));
                rsChilds.moveToInsertRow();
                rsChilds.updateLong("COMPANY_ID",nCompanyID);
                rsChilds.updateLong("SUPP_ID",nSuppID);
                rsChilds.updateInt("SR_NO",i);
                rsChilds.updateInt("CHILD_SUPP_ID", (int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                rsChilds.updateBoolean("CHANGED",true);
                rsChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsChilds.insertRow();
                
                rsHChilds.moveToInsertRow();
                rsHChilds.updateInt("REVISION_NO",RevNo);
                rsHChilds.updateLong("COMPANY_ID",nCompanyID);
                rsHChilds.updateLong("SUPP_ID",nSuppID);
                rsHChilds.updateInt("SR_NO",i);
                rsHChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                rsHChilds.updateBoolean("CHANGED",true);
                rsHChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHChilds.insertRow();
            }
            
            
            //======== Update the Approval Flow =========
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=37; //SUPPLIER MASTER MODULE ID
            ObjFlow.DocNo=Long.toString((long)getAttribute("SUPP_ID").getVal());
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_COM_SUPP_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="SUPP_ID";
            
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
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
                data.Execute("UPDATE D_COM_SUPP_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+ObjFlow.DocNo+"");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=37 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            
            //==========Sync Party data with other modules =============//
            if(ObjFlow.Status.equals("F")) {
                if(EITLERPGLOBAL.gCompanyID==2) {
                    AddPartyToExternalDB(getAttribute("SUPPLIER_CODE").getString(), newURL);
                }
                AddPartyToUnregMaster(getAttribute("SUPPLIER_CODE").getString());
                AddPartyToFinance(getAttribute("SUPPLIER_CODE").getString());
            }
            //==========================================================//
              //==========================================================//
            //Externally Add Party in Finance partyMaster if Supplier Code not exist in party Master      
            
            Add_in_Fin_PartyMaster_notexist();
            
            //=========Add to Supplier in case of parent supplier ------------//
            if(ObjFlow.Status.equals("F")) {
                
                stParent=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsParent=stChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS");
                
                int ParentSuppID=(int)getAttribute("PARENT_SUPP_ID").getVal();
                if(ParentSuppID!=0) {
                    int MaxSrNo=0;
                    rsTmp=data.getResult("SELECT IF(MAX(SR_NO) IS NULL,0,MAX(SR_NO)) AS MAXNO FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+ParentSuppID);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        MaxSrNo=rsTmp.getInt("MAXNO")+1;
                    }
                    
                    rsTmp=data.getResult("SELECT * FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+ParentSuppID+" AND CHILD_SUPP_ID="+(long)getAttribute("SUPP_ID").getVal());
                    rsTmp.first();
                    
                    if(rsTmp.getRow()<=0) {
                        rsParent.moveToInsertRow();
                        rsParent.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        rsParent.updateInt("SR_NO",MaxSrNo);
                        rsParent.updateInt("SUPP_ID",ParentSuppID);
                        rsParent.updateLong("CHILD_SUPP_ID",(long)getAttribute("SUPP_ID").getVal());
                        rsParent.updateBoolean("CHANGED",true);
                        rsParent.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsParent.insertRow();
                    }
                }
            }
            //============================================================================//
            
            
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pSuppCode,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SUPP_ID="+pSuppCode+" AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=37 AND DOC_NO='"+pSuppCode+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pSuppCode,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SUPP_ID="+pSuppCode+" AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=37 AND DOC_NO='"+pSuppCode+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    
    //Deletes current record
    public boolean Delete() {
        try {
            // String strQry = "DELETE FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + getAttribute("COMPANY_ID").getVal() + " AND SUPP_ID=" + getAttribute("SUPP_ID").getVal() ;
            String lCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String sSuppCode=(String) getAttribute("SUPPLIER_CODE").getObj();
            
            String strQry = "DELETE FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + lCompanyID +" AND SUPPLIER_CODE='"+sSuppCode+"'";
            data.Execute(strQry);
            strQry = "DELETE FROM D_COM_SUPP_ITEMCATEGORY WHERE COMPANY_ID=" + lCompanyID +" AND SUPPLIER_CODE='"+sSuppCode+"'";
            data.Execute(strQry);
            
            setData();
            return MoveLast();
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,String pSuppID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND SUPPLIER_CODE='" + pSuppID+"' ";
        clsSupplier ObjSupplier = new clsSupplier();
        ObjSupplier.Filter(strCondition);
        return ObjSupplier;
    }
    
    
    public static Object getObjectEx(int pCompanyID,String pSuppID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND SUPPLIER_CODE='" + pSuppID+"' ";
        
        clsSupplier ObjSupplier = new clsSupplier();
        ObjSupplier.LoadData(pCompanyID);
        ObjSupplier.Filter(strCondition);
        return ObjSupplier;
    }
    
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        
        HashMap List=new HashMap();
        long Counter=0;
        long Counter2=0;
        
        tmpConn=data.getConn();
        
        try {
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSupplier ObjSupplier =new clsSupplier();
                ObjSupplier.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjSupplier.setAttribute("SUPP_ID",rsTmp.getLong("SUPP_ID"));
                ObjSupplier.setAttribute("SUPPLIER_CODE",rsTmp.getString("SUPPLIER_CODE"));
                ObjSupplier.setAttribute("DUMMY_SUPPLIER_CODE",rsTmp.getString("DUMMY_SUPPLIER_CODE"));
                ObjSupplier.setAttribute("SUPP_NAME",rsTmp.getString("SUPP_NAME"));
                ObjSupplier.setAttribute("ATTN",rsTmp.getString("ATTN"));
                ObjSupplier.setAttribute("ADD1",rsTmp.getString("ADD1"));
                ObjSupplier.setAttribute("ADD2",rsTmp.getString("ADD2"));
                ObjSupplier.setAttribute("ADD3",rsTmp.getString("ADD3"));
                ObjSupplier.setAttribute("CITY",rsTmp.getString("CITY"));
                ObjSupplier.setAttribute("STATE",rsTmp.getString("STATE"));
                ObjSupplier.setAttribute("COUNTRY",rsTmp.getString("COUNTRY"));
                ObjSupplier.setAttribute("PINCODE",rsTmp.getString("PINCODE"));
                ObjSupplier.setAttribute("PHONE_O",rsTmp.getString("PHONE_O"));
                ObjSupplier.setAttribute("PHONE_RES",rsTmp.getString("PHONE_RES"));
                ObjSupplier.setAttribute("FAX_NO",rsTmp.getString("FAX_NO"));
                ObjSupplier.setAttribute("MOBILE_NO",rsTmp.getString("MOBILE_NO"));
                ObjSupplier.setAttribute("EMAIL_ADD",rsTmp.getString("EMAIL_ADD"));
                ObjSupplier.setAttribute("URL",rsTmp.getString("URL"));
                ObjSupplier.setAttribute("WEB_SITE",rsTmp.getString("WEB_SITE"));
                ObjSupplier.setAttribute("REGISTERATION",rsTmp.getString("REGISTERATION"));
                ObjSupplier.setAttribute("SERVICETAX_NO",rsTmp.getString("SERVICETAX_NO"));
                ObjSupplier.setAttribute("SERVICETAX_DATE",rsTmp.getString("SERVICETAX_DATE"));
                ObjSupplier.setAttribute("CST_NO",rsTmp.getString("CST_NO"));
                ObjSupplier.setAttribute("CST_DATE",rsTmp.getString("CST_DATE"));
                ObjSupplier.setAttribute("GST_NO",rsTmp.getString("GST_NO"));
                ObjSupplier.setAttribute("GST_DATE",rsTmp.getString("GST_DATE"));
                ObjSupplier.setAttribute("GSTIN_NO",rsTmp.getString("GSTIN_NO"));
                ObjSupplier.setAttribute("GSTIN_DATE",rsTmp.getString("GSTIN_DATE"));
                ObjSupplier.setAttribute("ECC_NO",rsTmp.getString("ECC_NO"));
                ObjSupplier.setAttribute("ECC_DATE",rsTmp.getString("ECC_DATE"));
                ObjSupplier.setAttribute("SSIREG_NO",rsTmp.getString("SSIREG_NO"));
                ObjSupplier.setAttribute("SSIREG_DATE",rsTmp.getString("SSIREG_DATE"));
                ObjSupplier.setAttribute("ESIREG_NO",rsTmp.getString("ESIREG_NO"));
                ObjSupplier.setAttribute("ESIREG_DATE",rsTmp.getString("ESIREG_DATE"));
                ObjSupplier.setAttribute("FORM",rsTmp.getLong("FORM"));
                ObjSupplier.setAttribute("ST35_REGISTERED",rsTmp.getBoolean("ST35_REGISTERED"));
                ObjSupplier.setAttribute("SSIREG",rsTmp.getBoolean("SSIREG"));
                ObjSupplier.setAttribute("CONTACT_PERSON_1",rsTmp.getString("CONTACT_PERSON_1"));
                ObjSupplier.setAttribute("CONTACT_PERSON_2",rsTmp.getString("CONTACT_PERSON_2"));
                ObjSupplier.setAttribute("ONETIME_SUPPLIER",rsTmp.getBoolean("ONETIME_SUPPLIER"));
                ObjSupplier.setAttribute("FROM_DATE_REG",rsTmp.getString("FROM_DATE_REG"));
                ObjSupplier.setAttribute("TO_DATE_REG",rsTmp.getString("TO_DATE_REG"));
                ObjSupplier.setAttribute("BLOCKED",rsTmp.getString("BLOCKED"));
                ObjSupplier.setAttribute("SLOW_MOVING",rsTmp.getBoolean("SLOW_MOVING"));
                ObjSupplier.setAttribute("PAYMENT_DAYS",rsTmp.getInt("PAYMENT_DAYS"));
                ObjSupplier.setAttribute("LAST_PO_NO",rsTmp.getString("LAST_PO_NO"));
                ObjSupplier.setAttribute("LAST_TRANS_DATE",rsTmp.getString("LAST_TRANS_DATE"));
                ObjSupplier.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjSupplier.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjSupplier.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjSupplier.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjSupplier.setAttribute("BANK_ID",rsTmp.getLong("BANK_ID"));
                ObjSupplier.setAttribute("BANK_NAME",rsTmp.getString("BANK_NAME"));
                
                ObjSupplier.setAttribute("PAYMENT_CODE",rsTmp.getLong("PAYMENT_CODE"));
                ObjSupplier.setAttribute("PAYMENT_DESC",rsTmp.getString("PAYMENT_DESC"));
                ObjSupplier.setAttribute("DESPATCH_CODE",rsTmp.getLong("DESPATCH_CODE"));
                ObjSupplier.setAttribute("DESPATCH_DESC",rsTmp.getString("DESPATCH_DESC"));
                ObjSupplier.setAttribute("INSURANCE_CODE",rsTmp.getLong("INSURANCE_CODE"));
                ObjSupplier.setAttribute("INSURANCE_DESC",rsTmp.getString("INSURANCE_DESC"));
                ObjSupplier.setAttribute("LICENSE_CODE",rsTmp.getLong("LICENSE_CODE"));
                ObjSupplier.setAttribute("LICENSE_DESC",rsTmp.getString("LICENSE_DESC"));
                ObjSupplier.setAttribute("PACKING_CODE",rsTmp.getLong("PACKING_CODE"));
                ObjSupplier.setAttribute("PACKING_DESC",rsTmp.getString("PACKING_DESC"));
                ObjSupplier.setAttribute("FORWARDING_CODE",rsTmp.getLong("FORWARDING_CODE"));
                ObjSupplier.setAttribute("FORWARDING_DESC",rsTmp.getString("FORWARDING_DESC"));
                ObjSupplier.setAttribute("EXCISE_CODE",rsTmp.getLong("EXCISE_CODE"));
                ObjSupplier.setAttribute("EXCISE",rsTmp.getString("EXCISE"));
                ObjSupplier.setAttribute("OCTROI_CODE",rsTmp.getLong("OCTROI_CODE"));
                ObjSupplier.setAttribute("OCTROI",rsTmp.getString("OCTROI"));
                ObjSupplier.setAttribute("FREIGHT_CODE",rsTmp.getLong("FREIGHT_CODE"));
                ObjSupplier.setAttribute("FREIGHT",rsTmp.getString("FREIGHT"));
                ObjSupplier.setAttribute("TCC_CODE",rsTmp.getLong("TCC_CODE"));
                ObjSupplier.setAttribute("TCC",rsTmp.getString("TCC"));
                ObjSupplier.setAttribute("SERVICETAX_CODE",rsTmp.getLong("SERVICETAX_CODE"));
                ObjSupplier.setAttribute("SERVICETAX_DESC",rsTmp.getString("SERVICETAX_DESC"));
                ObjSupplier.setAttribute("ST_CODE",rsTmp.getLong("ST_CODE"));
                ObjSupplier.setAttribute("ST_DESC",rsTmp.getString("ST_DESC"));
                ObjSupplier.setAttribute("ESI_CODE",rsTmp.getLong("ESI_CODE"));
                ObjSupplier.setAttribute("ESI_DESC",rsTmp.getString("ESI_DESC"));
                ObjSupplier.setAttribute("FOR_CODE",rsTmp.getLong("FOR_CODE"));
                ObjSupplier.setAttribute("FOR_DESC",rsTmp.getString("FOR_DESC"));
                ObjSupplier.setAttribute("FOB_CODE",rsTmp.getLong("FOB_CODE"));
                ObjSupplier.setAttribute("FOB_DESC",rsTmp.getString("FOB_DESC"));
                
                ObjSupplier.setAttribute("DISTANCE_KM",rsTmp.getInt("DISTANCE_KM"));
                
                ObjSupplier.setAttribute("HIERARCHY_ID",rsTmp.getLong("HIERARCHY_ID"));
                ObjSupplier.colSupplierItems.clear();
                
                String mCompanyID=Long.toString( (long) ObjSupplier.getAttribute("COMPANY_ID").getVal());
                long mSupp=(long)ObjSupplier.getAttribute("SUPP_ID").getVal();
                
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSupp+" ORDER BY CATEGORY_ID");
                
                Counter2=0;
                while(rsTmp2.next()) {
                    Counter2=Counter2+1;
                    clsSupplierItem ObjItems=new clsSupplierItem();
                    
                    ObjItems.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjItems.setAttribute("SUPP_ID",rsTmp2.getLong("SUPP_ID"));
                    ObjItems.setAttribute("CATEGORY_ID",rsTmp2.getLong("CATEGORY_ID"));
                    ObjItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    ObjSupplier.colSupplierItems.put(Long.toString(Counter2),ObjItems);
                }// Innser while
                
                List.put(Long.toString(Counter),ObjSupplier);
            }//Outer while
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            
            rsTmp2.close();
            tmpStmt2.close();
            
        }
        catch(Exception e) {
            //LastError = e.getMessage();
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            
            String strSql = "SELECT * FROM D_COM_SUPP_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            System.out.println("STR : "+strSql);
            rsResultSet = Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if (rsResultSet.getRow()>0) {
                setData();
                Ready = true;
            }
            else {
                Ready = false;
            }
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        //HashMap List=new HashMap();
        long Counter=0;
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
            setAttribute("SUPP_ID",rsResultSet.getLong("SUPP_ID"));
            setAttribute("SUPPLIER_CODE",rsResultSet.getString("SUPPLIER_CODE"));
            setAttribute("DUMMY_SUPPLIER_CODE",rsResultSet.getString("DUMMY_SUPPLIER_CODE"));
            setAttribute("SUPP_NAME",rsResultSet.getString("SUPP_NAME"));
            setAttribute("ATTN",rsResultSet.getString("ATTN"));
            setAttribute("ADD1",rsResultSet.getString("ADD1"));
            setAttribute("ADD2",rsResultSet.getString("ADD2"));
            setAttribute("ADD3",rsResultSet.getString("ADD3"));
            setAttribute("CITY",rsResultSet.getString("CITY"));
            setAttribute("PROPOSED_ITEM",rsResultSet.getString("PROPOSED_ITEM"));
            //setAttribute("STATE",rsResultSet.getInt("STATE"));
            setAttribute("STATE",rsResultSet.getString("STATE"));
            setAttribute("COUNTRY",rsResultSet.getString("COUNTRY"));
            System.out.println(rsResultSet.getString("STATE_CODE"));
            setAttribute("STATE_CODE",rsResultSet.getString("STATE_CODE"));
            setAttribute("STATE_GST_CODE",rsResultSet.getString("STATE_GST_CODE"));
            setAttribute("PLACE_OF_SUPPLY",rsResultSet.getString("PLACE_OF_SUPPLY"));
            setAttribute("PINCODE",rsResultSet.getString("PINCODE"));
            setAttribute("PHONE_O",rsResultSet.getString("PHONE_O"));
            setAttribute("PHONE_RES",rsResultSet.getString("PHONE_RES"));
            setAttribute("FAX_NO",rsResultSet.getString("FAX_NO"));
            setAttribute("MOBILE_NO",rsResultSet.getString("MOBILE_NO"));
            setAttribute("EMAIL_ADD",rsResultSet.getString("EMAIL_ADD"));
            setAttribute("URL",rsResultSet.getString("URL"));
            setAttribute("WEB_SITE",rsResultSet.getString("WEB_SITE"));
            setAttribute("REGISTERATION",rsResultSet.getString("REGISTERATION"));
            setAttribute("SERVICETAX_NO",rsResultSet.getString("SERVICETAX_NO"));
            setAttribute("SERVICETAX_DATE",rsResultSet.getString("SERVICETAX_DATE"));
            setAttribute("CST_NO",rsResultSet.getString("CST_NO"));
            setAttribute("CST_DATE",rsResultSet.getString("CST_DATE"));
            setAttribute("GST_NO",rsResultSet.getString("GST_NO"));
            setAttribute("GST_DATE",rsResultSet.getString("GST_DATE"));
            setAttribute("GSTIN_NO",rsResultSet.getString("GSTIN_NO"));
            setAttribute("GSTIN_DATE",rsResultSet.getString("GSTIN_DATE"));
            setAttribute("ECC_NO",rsResultSet.getString("ECC_NO"));
            setAttribute("ECC_DATE",rsResultSet.getString("ECC_DATE"));
            setAttribute("SSIREG_NO",rsResultSet.getString("SSIREG_NO"));
            setAttribute("SSIREG_DATE",rsResultSet.getString("SSIREG_DATE"));
            setAttribute("ESIREG_NO",rsResultSet.getString("ESIREG_NO"));
            setAttribute("ESIREG_DATE",rsResultSet.getString("ESIREG_DATE"));
            
            setAttribute("PAN_NO",rsResultSet.getString("PAN_NO"));
            setAttribute("PAN_DATE",rsResultSet.getString("PAN_DATE"));
            
setAttribute("MSME",rsResultSet.getBoolean("MSME"));
            setAttribute("MSME_UAN",rsResultSet.getString("MSME_UAN"));
            setAttribute("MSME_DIC_NO",rsResultSet.getString("MSME_DIC_NO"));

            setAttribute("FORM",rsResultSet.getLong("FORM"));
            setAttribute("ST35_REGISTERED",rsResultSet.getBoolean("ST35_REGISTERED"));
            setAttribute("SSIREG",rsResultSet.getBoolean("SSIREG"));
            setAttribute("CONTACT_PERSON_1",rsResultSet.getString("CONTACT_PERSON_1"));
            setAttribute("CONTACT_PERSON_2",rsResultSet.getString("CONTACT_PERSON_2"));
            setAttribute("ONETIME_SUPPLIER",rsResultSet.getBoolean("ONETIME_SUPPLIER"));
            setAttribute("FROM_DATE_REG",rsResultSet.getString("FROM_DATE_REG"));
            setAttribute("TO_DATE_REG",rsResultSet.getString("TO_DATE_REG"));
            setAttribute("BLOCKED",rsResultSet.getString("BLOCKED"));
            setAttribute("SLOW_MOVING",rsResultSet.getBoolean("SLOW_MOVING"));
            setAttribute("PAYMENT_DAYS",rsResultSet.getInt("PAYMENT_DAYS"));
            setAttribute("PAYMENT_TERM_CODE",rsResultSet.getInt("PAYMENT_TERM_CODE"));
            setAttribute("LAST_PO_NO",rsResultSet.getString("LAST_PO_NO"));
            setAttribute("LAST_TRANS_DATE",rsResultSet.getString("LAST_TRANS_DATE"));
            setAttribute("BANK_ID",rsResultSet.getLong("BANK_ID"));
            setAttribute("BANK_NAME",rsResultSet.getString("BANK_NAME"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getLong("HIERARCHY_ID"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("PARENT_SUPP_ID",rsResultSet.getInt("PARENT_SUPP_ID"));
            setAttribute("MAIN_ACCOUNT_CODE",rsResultSet.getString("MAIN_ACCOUNT_CODE"));
            
            setAttribute("DISTANCE_KM",rsResultSet.getInt("DISTANCE_KM"));
            
            rsTmp=data.getResult("SELECT SUPP_ID FROM D_COM_SUPP_CHILDS WHERE CHILD_SUPP_ID="+(int)getAttribute("SUPP_ID").getVal());
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                setAttribute("PARENT_SUPP_ID",rsTmp.getInt("SUPP_ID"));
            }
            
            
            //Now Populate the collection
            //first clear the collection
            colSupplierItems.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            long mSupp=(long)getAttribute("SUPP_ID").getVal();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY_H WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSupp + " AND REVISION_NO="+RevNo+" ORDER BY CATEGORY_ID");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSupp + " ORDER BY CATEGORY_ID");
            }
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSupplierItem ObjItems=new clsSupplierItem();
                
                ObjItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItems.setAttribute("SUPP_ID",rsTmp.getLong("SUPP_ID"));
                ObjItems.setAttribute("CATEGORY_ID",rsTmp.getLong("CATEGORY_ID"));
                ObjItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                colSupplierItems.put(Long.toString(Counter),ObjItems);
            }
            
            
            colSuppTerms.clear();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_TERMS_H WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSupp + " AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_TERMS WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSupp + " ");
            }
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSuppTerms ObjItems=new clsSuppTerms();
                
                ObjItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItems.setAttribute("SUPP_ID",rsTmp.getLong("SUPP_ID"));
                ObjItems.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItems.setAttribute("TERM_TYPE",rsTmp.getString("TERM_TYPE"));
                ObjItems.setAttribute("TERM_CODE",rsTmp.getInt("TERM_CODE"));
                ObjItems.setAttribute("TERM_DAYS",rsTmp.getInt("TERM_DAYS"));
                ObjItems.setAttribute("TERM_DESC",rsTmp.getString("TERM_DESC"));
                
                colSuppTerms.put(Long.toString(Counter),ObjItems);
            }
            
            
            
            colSuppChilds.clear();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS_H WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSupp + " AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+mCompanyID+" AND SUPP_ID="+mSupp + " ");
            }
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSuppChilds ObjItems=new clsSuppChilds();
                
                ObjItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItems.setAttribute("SUPP_ID",rsTmp.getLong("SUPP_ID"));
                ObjItems.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItems.setAttribute("CHILD_SUPP_ID",rsTmp.getInt("CHILD_SUPP_ID"));
                
                colSuppChilds.put(Long.toString(Counter),ObjItems);
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean IsRegisteredSupplier(long pCompanyID,String pSuppCode) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        String strQry = "SELECT COUNT(*) FROM D_COM_SUPP_MASTER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND SUPPLIER_CODE='" + pSuppCode + "' AND ST35_REGISTERED=1 AND BLOCKED='N' AND APPROVED=1";
        
        tmpConn=data.getConn();
        
        try {
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery(strQry);
            rsTmp.first();
            if (rsTmp.getInt("COUNT")>0) {
                return true;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
        
    }
    
    /*public HashMap getSuppliersByItem(long pCompanyID,String pItemCode)
    {
     
    }*/
    
    
    
    public static String getSupplierName(int pCompanyID,String pSuppCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String SuppName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppCode+"' AND SUPPLIER_CODE<>''");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SuppName=rsTmp.getString("SUPP_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return SuppName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getSupplierEMail(int pCompanyID,String pSuppCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String email="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT EMAIL_ADD FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppCode+"' AND SUPPLIER_CODE<>''");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                email=rsTmp.getString("EMAIL_ADD");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return email;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static int getSupplierID(int pCompanyID,String pSuppCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        int SuppID=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SuppID=rsTmp.getInt("SUPP_ID");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return SuppID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static int getSupplierID(int pCompanyID,String pSuppCode,String pDBURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        int SuppID=0;
        
        try {
            tmpConn=data.getConn(pDBURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SuppID=rsTmp.getInt("SUPP_ID");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return SuppID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static String getSupplierCode(int pCompanyID,int pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String SuppCode="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPPLIER_CODE FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPP_ID="+pSuppID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SuppCode=rsTmp.getString("SUPPLIER_CODE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return SuppCode;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getSupplierName(int pCompanyID,String pSuppID,String pURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String SuppName="";
        
        try {
            tmpConn=data.getConn(pURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"' AND SUPPLIER_CODE<>''");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                SuppName=rsTmp.getString("SUPP_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return SuppName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    
    public static boolean IsRegistrationNoExist(int pCompanyID,String pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean Registered=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CST_NO,GST_NO,GSTIN_NO FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"' AND SUPPLIER_CODE<>''");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                String CSTNo=rsTmp.getString("CST_NO");
                String GSTNo=rsTmp.getString("GST_NO");
                String GSTINNo=rsTmp.getString("GSTIN_NO");
                
                
                //Either Not registered or Applied
                if((CSTNo.trim().equals("APPLIED")||CSTNo.trim().equals("N/A")) && (GSTNo.trim().equals("APPLIED")||GSTNo.trim().equals("N/A"))&& (GSTINNo.trim().equals("APPLIED")||GSTINNo.trim().equals("N/A"))) {
                    Registered=false;
                }
                else {
                    Registered=true;
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Registered;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    
    public static boolean IsValidSuppCode(int pCompanyID,String pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"' AND BLOCKED='N' AND APPROVED=1 AND CANCELLED=0 AND ST35_REGISTERED=1 ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    
    public static boolean IsValidSuppCodeEx(int pCompanyID,String pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"' AND BLOCKED='N' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsValidSuppCodeEx1(int pCompanyID,String pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"' AND APPROVED=1 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static boolean IsSupplierExist(int pCompanyID,String pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"' AND BLOCKED='N' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static boolean IsSupplierExistEx(int pCompanyID,String pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static boolean IsDuplicateSuppCode(int pCompanyID,String pSuppID,boolean pDummy ) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if (pDummy==true) {
                rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DUMMY_SUPPLIER_CODE='"+pSuppID+"'");
            }
            else {
                rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"'");
            }
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return true;
        }
    }
    
    public static boolean IsDuplicateSuppCode(int pCompanyID,String pSuppID,boolean pDummy,int pCurrentSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if (pDummy==true) {
                rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DUMMY_SUPPLIER_CODE='"+pSuppID+"' AND SUPP_ID<>"+pCurrentSuppID);
            }
            else {
                rsTmp=stTmp.executeQuery("SELECT SUPP_NAME FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"' AND SUPP_ID<>"+pCurrentSuppID);
            }
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return true;
        }
    }
    
    public static HashMap getSuppList(int pCompanyID,String pSupp,boolean pAllData) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSql;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(pSupp.equals("")) {
                strSql = "SELECT * FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY SUPP_NAME";
            }
            else{
                strSql = "SELECT * FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPPLIER_CODE='"+pSupp.trim()+"' ORDER BY SUPP_NAME";
            }
            
            rsTmp=tmpStmt.executeQuery(strSql);
            rsTmp.first();
            
            Counter = 0;
            while(! rsTmp.isAfterLast()) {
                clsSupplier ObjSupp = new clsSupplier();
                Counter++;
                
                ObjSupp.setAttribute("SUPPLIER_CODE",rsTmp.getString("SUPPLIER_CODE"));
                ObjSupp.setAttribute("DUMMY_SUPPLIER_CODE",rsTmp.getString("DUMMY_SUPPLIER_CODE"));
                ObjSupp.setAttribute("SUPP_NAME",rsTmp.getString("SUPP_NAME"));
                
                List.put(Long.toString(Counter),ObjSupp);
                rsTmp.next();
            }
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return List;
    }
    
    
    public boolean ShowHistory(int pCompanyID,int pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_SUPP_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND SUPP_ID="+pDocNo+" ORDER BY REVISION_NO");
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
    
    
    public static HashMap getHistoryList(int pCompanyID,int pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_SUPP_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+pDocNo+" ORDER BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsSupplier ObjSupp=new clsSupplier();
                
                ObjSupp.setAttribute("SUPPLIER_CODE",rsTmp.getString("SUPPLIER_CODE"));
                ObjSupp.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjSupp.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjSupp.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjSupp.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjSupp.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjSupp);
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
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                //strSQL="SELECT D_COM_SUPP_MASTER.SUPP_ID,D_COM_SUPP_MASTER.SUPPLIER_CODE,D_COM_SUPP_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
                strSQL="SELECT D_COM_SUPP_MASTER.SUPP_ID,D_COM_SUPP_MASTER.SUPPLIER_CODE,D_COM_SUPP_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37 AND D_COM_SUPP_MASTER.CANCELLED=0 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                //strSQL="SELECT D_COM_SUPP_MASTER.SUPP_ID,D_COM_SUPP_MASTER.SUPPLIER_CODE,D_COM_SUPP_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37 ORDER BY D_COM_SUPP_MASTER.SUPP_ID";
                strSQL="SELECT D_COM_SUPP_MASTER.SUPP_ID,D_COM_SUPP_MASTER.SUPPLIER_CODE,D_COM_SUPP_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37 AND D_COM_SUPP_MASTER.CANCELLED=0 ORDER BY D_COM_SUPP_MASTER.SUPP_ID";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                //strSQL="SELECT D_COM_SUPP_MASTER.SUPP_ID,D_COM_SUPP_MASTER.SUPPLIER_CODE,D_COM_SUPP_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37 ORDER BY D_COM_SUPP_MASTER.SUPP_ID";
                strSQL="SELECT D_COM_SUPP_MASTER.SUPP_ID,D_COM_SUPP_MASTER.SUPPLIER_CODE,D_COM_SUPP_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37 AND D_COM_SUPP_MASTER.CANCELLED=0 ORDER BY D_COM_SUPP_MASTER.SUPP_ID";
            }
            
            //strSQL="SELECT D_COM_SUPP_MASTER.SUPP_ID,D_COM_SUPP_MASTER.SUPPLIER_CODE,D_COM_SUPP_MASTER.SUPP_NAME FROM D_COM_SUPP_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_MASTER.SUPP_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=37";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSupplier ObjItem=new clsSupplier();
                
                //------------- Header Fields --------------------//
                ObjItem.setAttribute("SUPP_ID",rsTmp.getInt("SUPP_ID"));
                ObjItem.setAttribute("SUPPLIER_CODE",rsTmp.getString("SUPPLIER_CODE"));
                ObjItem.setAttribute("SUPP_NAME",rsTmp.getString("SUPP_NAME"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
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
    
    
    public static boolean CanCancelSupplier(int pCompanyID,int pSuppID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT SUPPLIER_CODE FROM D_COM_SUPP_MASTER WHERE SUPP_ID="+pSuppID+" AND COMPANY_ID="+pCompanyID+" AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        
        return CanCancel;
    }
    
    
    public static void CancelSupplier(int pCompanyID,int pSuppID) {
        ResultSet rsTmp=null;
        
        if(CanCancelSupplier(pCompanyID,pSuppID)) {
            
            boolean Approved=false;
            
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPP_ID="+pSuppID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(!Approved) {
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pSuppID+"' AND MODULE_ID=37");
                }
                
                //data.Execute("UPDATE D_COM_SUPP_MASTER SET BLOCKED='Y',SUPPLIER_CODE='"+"_"+pSuppID+"' WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppID+"'");
                data.Execute("UPDATE D_COM_SUPP_MASTER SET BLOCKED='Y',CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND SUPP_ID="+pSuppID);
            }
            catch(Exception e) {
                
            }
        }
        
    }
    
    
    
    
    public static HashMap getPaymentTerms(String SuppCode) {
        HashMap List=new HashMap();
        
        try {
            int PartyID=data.getIntValueFromDB("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+SuppCode+"'");
            
            if(PartyID!=0) {
                ResultSet rsTmp=data.getResult("SELECT TERM_CODE FROM D_COM_SUPP_TERMS WHERE SUPP_ID="+PartyID+" AND TERM_TYPE='P'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    while(!rsTmp.isAfterLast()) {
                        
                        System.out.println(Integer.toString(UtilFunctions.getInt(rsTmp,"TERM_CODE",0)));
                        List.put(Integer.toString(List.size()+1), Integer.toString(UtilFunctions.getInt(rsTmp,"TERM_CODE",0)));
                        
                        rsTmp.next();
                    }
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    
    public void AddPartyToFinance(String SupplierCode) {
        //========== Add Party to Finance Party Master ===============//
        
        clsSupplier objSupplier=new clsSupplier();
        
        if(objSupplier.Filter(" WHERE SUPPLIER_CODE='"+SupplierCode+"' ")) {
            try {
                Connection FinConn;
                Statement stFinParty;
                ResultSet rsFinParty;
                
                FinConn=data.getConn(FinanceGlobal.FinURL);
                stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
                
                String MainCodes[] = objSupplier.getAttribute("MAIN_ACCOUNT_CODE").getString().split(",");
                
                for(int i=0;i<MainCodes.length;i++) {
                    
                    long Counter=data.getLongValueFromDB("SELECT MAX(PARTY_ID) AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);
                    Counter++;
                    rsFinParty.moveToInsertRow();
                    rsFinParty.updateInt("COMPANY_ID",2);
                    rsFinParty.updateString("PARTY_CODE",objSupplier.getAttribute("SUPPLIER_CODE").getString());
                    rsFinParty.updateString("MAIN_ACCOUNT_CODE",MainCodes[i]);
                    rsFinParty.updateLong("PARTY_ID",Counter);
                    rsFinParty.updateString("PARTY_NAME",objSupplier.getAttribute("SUPP_NAME").getString());
                    rsFinParty.updateString("SH_NAME","");
                    rsFinParty.updateString("REMARKS","");
                    rsFinParty.updateString("SH_CODE","");
                    rsFinParty.updateDouble("CREDIT_DAYS",0);
                    rsFinParty.updateDouble("CREDIT_LIMIT",0);
                    rsFinParty.updateDouble("DEBIT_LIMIT",0);
                    rsFinParty.updateString("TIN_NO",objSupplier.getAttribute("GST_NO").getString());
                    rsFinParty.updateString("TIN_DATE",objSupplier.getAttribute("GST_DATE").getString());
                    rsFinParty.updateString("GSTIN_NO",objSupplier.getAttribute("GSTIN_NO").getString());
                    rsFinParty.updateString("GSTIN_DATE",objSupplier.getAttribute("GSTIN_DATE").getString());
                    rsFinParty.updateString("CST_NO",objSupplier.getAttribute("CST_NO").getString());
                    rsFinParty.updateString("CST_DATE",objSupplier.getAttribute("CST_DATE").getString());
                    rsFinParty.updateString("ECC_NO",objSupplier.getAttribute("ECC_NO").getString());
                    rsFinParty.updateString("ECC_DATE",objSupplier.getAttribute("ECC_DATE").getString());
                    rsFinParty.updateString("SERVICE_TAX_NO",objSupplier.getAttribute("SERVICETAX_NO").getString());
                    rsFinParty.updateString("SERVICE_TAX_DATE",objSupplier.getAttribute("SERVICETAX_DATE").getString());
                    rsFinParty.updateString("SSI_NO",objSupplier.getAttribute("SSIREG_NO").getString());
                    rsFinParty.updateString("SSI_DATE",objSupplier.getAttribute("SSIREG_DATE").getString());
                    rsFinParty.updateString("ESI_NO",objSupplier.getAttribute("ESIREG_NO").getString());
                    rsFinParty.updateString("ESI_DATE",objSupplier.getAttribute("ESIREG_DATE").getString());
                    rsFinParty.updateString("ADDRESS",objSupplier.getAttribute("ADD1").getString().trim()+objSupplier.getAttribute("ADD2").getString().trim()+objSupplier.getAttribute("ADD3").getString().trim());
                    rsFinParty.updateString("CITY",objSupplier.getAttribute("CITY").getString());
                    rsFinParty.updateString("PINCODE",objSupplier.getAttribute("PINCODE").getString());
                    rsFinParty.updateInt("STATE",(int)objSupplier.getAttribute("STATE").getVal());
                    rsFinParty.updateInt("COUNTRY",(int)objSupplier.getAttribute("COUNTRY").getVal());
                    rsFinParty.updateString("STATE_CODE",objSupplier.getAttribute("STATE_CODE").getString());
                    rsFinParty.updateString("STATE_GST_CODE",objSupplier.getAttribute("STATE_GST_CODE").getString());
                    rsFinParty.updateString("PLACE_OF_SUPPLY",objSupplier.getAttribute("PLACE_OF_SUPPLY").getString());
                    rsFinParty.updateString("PHONE",objSupplier.getAttribute("PHONE_O").getString());
                    rsFinParty.updateString("FAX",objSupplier.getAttribute("FAX_NO").getString());
                    rsFinParty.updateString("MOBILE",objSupplier.getAttribute("MOBILE_NO").getString());
                    rsFinParty.updateString("EMAIL",objSupplier.getAttribute("EMAIL_ADD").getString());
                    rsFinParty.updateString("URL",objSupplier.getAttribute("URL").getString());
                    
                    rsFinParty.updateInt("DISTANCE_KM",objSupplier.getAttribute("DISTANCE_KM").getInt()); //new code by vivek on 16/09/2013 to add state & country.
                    
                    rsFinParty.updateInt("APPROVED",1);
                    rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateInt("REJECTED",0);
                    rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                    rsFinParty.updateString("REJECTED_REMARKS","");
                    rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                    rsFinParty.updateInt("HIERARCHY_ID",0);
                    rsFinParty.updateInt("CANCELLED",0);
                    rsFinParty.updateInt("BLOCKED",0);
                    rsFinParty.updateString("PAN_NO",objSupplier.getAttribute("PAN_NO").getString().trim());
                    rsFinParty.updateString("PAN_DATE",objSupplier.getAttribute("PAN_DATE").getString().trim());

rsFinParty.updateBoolean("MSME",objSupplier.getAttribute("MSME").getBool());
                    rsFinParty.updateString("MSME_UAN",objSupplier.getAttribute("MSME_UAN").getString().trim());
                    rsFinParty.updateString("MSME_DIC_NO",objSupplier.getAttribute("MSME_DIC_NO").getString().trim());

                    rsFinParty.updateInt("CHANGED",1);
                    rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("CREATED_BY","System");
                    rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("MODIFIED_BY","System");
                    rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateDouble("CLOSING_BALANCE",0);
                    rsFinParty.updateString("CLOSING_EFFECT","D");
                    
                    //BHAVESH
                    rsFinParty.updateInt("PARTY_TYPE",0);
                    rsFinParty.updateString("CHARGE_CODE_II","");
                    rsFinParty.updateString("CHARGE_CODE","");
                    
                    rsFinParty.insertRow();
                }
                
                //rsFinParty.close();
                //stFinParty.close();
                //FinConn.close();
            }
            catch(Exception p) {
                
            }
            //============================================================//
        }
    }
    
    public void AddPartyToExternalDB(String SupplierCode,String dbURL) {
        try {
            
            int NewCompanyID=3;
            
            clsSupplier objSupplier=new clsSupplier();
            
            //Load record of supplier
            if(objSupplier.Filter("WHERE SUPPLIER_CODE='"+SupplierCode+"'")) {
                
                //long SuppID=objSupplier.getAttribute("SUPP_ID").getLong();
                long SuppID=data.getLongValueFromDB("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+SupplierCode+"' ",dbURL);
                
                //Remove old record from the external database
                long SuppIDNew = 0;
                if(data.IsRecordExist("SELECT * FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE ='"+SupplierCode+"'",dbURL)) {
                    SuppIDNew = data.getLongValueFromDB("SELECT SUPP_ID FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE ='"+SupplierCode+"'",dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+SupplierCode+"'",dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_MASTER_H WHERE SUPP_ID="+SuppIDNew,dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_TERMS WHERE SUPP_ID="+SuppIDNew,dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_TERMS_H WHERE SUPP_ID="+SuppIDNew,dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_ITEMCATEGORY WHERE SUPP_ID="+SuppIDNew,dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_ITEMCATEGORY_H WHERE SUPP_ID="+SuppIDNew,dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_CHILDS WHERE SUPP_ID="+SuppIDNew,dbURL);
                    data.Execute("DELETE FROM D_COM_SUPP_CHILDS_H WHERE SUPP_ID="+SuppIDNew,dbURL);
                }
                else {
                    SuppIDNew = (data.getIntValueFromDB("SELECT MAX(SUPP_ID) FROM D_COM_SUPP_MASTER",dbURL))+1;
                }
                Connection objExConn=data.getConn(dbURL);
                Statement stSupplier=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsSupplier=stSupplier.executeQuery("SELECT * FROM D_COM_SUPP_MASTER LIMIT 1");
                
                Statement stHistory=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SUPP_MASTER_H LIMIT 1");
                
                rsSupplier.moveToInsertRow();
                rsSupplier.updateLong("COMPANY_ID", NewCompanyID);
                rsSupplier.updateLong("SUPP_ID",SuppIDNew);
                rsSupplier.updateString("SUPPLIER_CODE",objSupplier.getAttribute("SUPPLIER_CODE").getString());
                String DummyCode=Long.toString((long)objSupplier.getAttribute("SUPP_ID").getVal());
                rsSupplier.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
                rsSupplier.updateString("SUPP_NAME",(String)objSupplier.getAttribute("SUPP_NAME").getObj());
                rsSupplier.updateString("ATTN",(String)objSupplier.getAttribute("ATTN").getObj());
                rsSupplier.updateString("ADD1",(String)objSupplier.getAttribute("ADD1").getObj());
                rsSupplier.updateString("ADD2",(String)objSupplier.getAttribute("ADD2").getObj());
                rsSupplier.updateString("ADD3",(String)objSupplier.getAttribute("ADD3").getObj());
                rsSupplier.updateString("CITY",(String)objSupplier.getAttribute("CITY").getObj());
                rsSupplier.updateString("PROPOSED_ITEM",(String)objSupplier.getAttribute("PROPOSED_ITEM").getObj());
                rsSupplier.updateString("STATE",(String)objSupplier.getAttribute("STATE").getObj());
                rsSupplier.updateString("COUNTRY",(String)objSupplier.getAttribute("COUNTRY").getObj());
                rsSupplier.updateString("STATE_CODE",(String)objSupplier.getAttribute("STATE_CODE").getObj());
                rsSupplier.updateString("STATE_GST_CODE",(String)objSupplier.getAttribute("STATE_GST_CODE").getObj());
                rsSupplier.updateString("PLACE_OF_SUPPLY",(String)objSupplier.getAttribute("PLACE_OF_SUPPLY").getObj());
                rsSupplier.updateString("PINCODE",(String)objSupplier.getAttribute("PINCODE").getObj());
                rsSupplier.updateString("PHONE_O",(String)objSupplier.getAttribute("PHONE_O").getObj());
                rsSupplier.updateString("PHONE_RES",(String)objSupplier.getAttribute("PHONE_RES").getObj());
                rsSupplier.updateString("FAX_NO",(String)objSupplier.getAttribute("FAX_NO").getObj());
                rsSupplier.updateString("MOBILE_NO",(String)objSupplier.getAttribute("MOBILE_NO").getObj());
                rsSupplier.updateString("EMAIL_ADD",(String)objSupplier.getAttribute("EMAIL_ADD").getObj());
                rsSupplier.updateString("URL",(String)objSupplier.getAttribute("URL").getObj());
                rsSupplier.updateString("WEB_SITE",(String)objSupplier.getAttribute("WEB_SITE").getObj());
                rsSupplier.updateString("REGISTERATION",(String)objSupplier.getAttribute("REGISTERATION").getObj());
                rsSupplier.updateString("SERVICETAX_NO",(String)objSupplier.getAttribute("SERVICETAX_NO").getObj());
                rsSupplier.updateString("SERVICETAX_DATE",(String)objSupplier.getAttribute("SERVICETAX_DATE").getObj());
                rsSupplier.updateString("CST_NO",(String)objSupplier.getAttribute("CST_NO").getObj());
                rsSupplier.updateString("CST_DATE",(String)objSupplier.getAttribute("CST_DATE").getObj());
                rsSupplier.updateString("GST_NO",(String)objSupplier.getAttribute("GST_NO").getObj());
                rsSupplier.updateString("GST_DATE",(String)objSupplier.getAttribute("GST_DATE").getObj());
                rsSupplier.updateString("GSTIN_NO",(String)objSupplier.getAttribute("GSTIN_NO").getObj());
                rsSupplier.updateString("GSTIN_DATE",(String)objSupplier.getAttribute("GSTIN_DATE").getObj());
                rsSupplier.updateString("ECC_NO",(String)objSupplier.getAttribute("ECC_NO").getObj());
                rsSupplier.updateString("ECC_DATE",(String)objSupplier.getAttribute("ECC_DATE").getObj());
                rsSupplier.updateString("SSIREG_NO",(String)objSupplier.getAttribute("SSIREG_NO").getObj());
                rsSupplier.updateString("SSIREG_DATE",(String)objSupplier.getAttribute("SSIREG_DATE").getObj());
                rsSupplier.updateString("ESIREG_NO",(String)objSupplier.getAttribute("ESIREG_NO").getObj());
                rsSupplier.updateString("ESIREG_DATE",(String)objSupplier.getAttribute("ESIREG_DATE").getObj());
                
                rsSupplier.updateString("PAN_NO",objSupplier.getAttribute("PAN_NO").getString());
                if(getAttribute("PAN_DATE").getObj().equals(""))
                {
                rsSupplier.updateString("PAN_DATE",null);
                }
                else
                {
                rsSupplier.updateString("PAN_DATE",(String)getAttribute("PAN_DATE").getObj());
                }
rsSupplier.updateBoolean("MSME",(boolean)objSupplier.getAttribute("MSME").getBool());
                rsSupplier.updateString("MSME_UAN",objSupplier.getAttribute("MSME_UAN").getString());
                rsSupplier.updateString("MSME_DIC_NO",objSupplier.getAttribute("MSME_DIC_NO").getString());
                 //rsSupplier.updateString("PAN_DATE",objSupplier.getAttribute("PAN_DATE").getString());
                
                rsSupplier.updateLong("FORM",(long)objSupplier.getAttribute("FORM").getVal());
                rsSupplier.updateString("CONTACT_PERSON_1",(String)objSupplier.getAttribute("CONTACT_PERSON_1").getObj());
                rsSupplier.updateString("CONTACT_PERSON_2",(String)objSupplier.getAttribute("CONTACT_PERSON_2").getObj());
                rsSupplier.updateBoolean("ONETIME_SUPPLIER",(boolean)objSupplier.getAttribute("ONETIME_SUPPLIER").getBool());
                rsSupplier.updateString("FROM_DATE_REG",(String)objSupplier.getAttribute("FROM_DATE_REG").getObj());
                rsSupplier.updateString("TO_DATE_REG",(String)objSupplier.getAttribute("TO_DATE_REG").getObj());
                rsSupplier.updateString("BLOCKED",(String)objSupplier.getAttribute("BLOCKED").getObj());
                rsSupplier.updateBoolean("SLOW_MOVING",(boolean)objSupplier.getAttribute("SLOW_MOVING").getBool());
                rsSupplier.updateBoolean("ST35_REGISTERED",(boolean)objSupplier.getAttribute("ST35_REGISTERED").getBool());
                rsSupplier.updateBoolean("SSIREG",(boolean)objSupplier.getAttribute("SSIREG").getBool());
                rsSupplier.updateLong("PAYMENT_DAYS",(long)objSupplier.getAttribute("PAYMENT_DAYS").getVal());
                rsSupplier.updateInt("PAYMENT_TERM_CODE",(int)objSupplier.getAttribute("PAYMENT_TERM_CODE").getVal());
                rsSupplier.updateString("LAST_PO_NO",(String)objSupplier.getAttribute("LAST_PO_NO").getObj());
                rsSupplier.updateString("LAST_TRANS_DATE",(String)objSupplier.getAttribute("LAST_TRANS_DATE").getObj());
                rsSupplier.updateLong("CREATED_BY",(long) objSupplier.getAttribute("CREATED_BY").getVal());
                rsSupplier.updateString("CREATED_DATE",(String)objSupplier.getAttribute("CREATED_DATE").getObj());
                rsSupplier.updateLong("MODIFIED_BY",(long) objSupplier.getAttribute("MODIFIED_BY").getVal());
                rsSupplier.updateString("MODIFIED_DATE",(String)objSupplier.getAttribute("MODIFIED_DATE").getObj());
                rsSupplier.updateLong("BANK_ID",(long) objSupplier.getAttribute("BANK_ID").getVal());
                rsSupplier.updateString("BANK_NAME",(String)objSupplier.getAttribute("BANK_NAME").getObj());
                rsSupplier.updateLong("HIERARCHY_ID",(long) objSupplier.getAttribute("HIERARCHY_ID").getVal());
                rsSupplier.updateBoolean("APPROVED",true);
                rsSupplier.updateBoolean("CANCELLED",false);
                rsSupplier.updateBoolean("CHANGED",true);
                rsSupplier.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsSupplier.updateInt("PARENT_SUPP_ID",(int)objSupplier.getAttribute("PARENT_SUPP_ID").getVal());
                rsSupplier.updateString("MAIN_ACCOUNT_CODE",objSupplier.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsSupplier.insertRow();
                
                
                //========= Inserting Into History =================//
                //Get the Maximum Revision No.
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY",(int)objSupplier.getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS",(String)objSupplier.getAttribute("APPROVAL_STATUS").getObj());
                rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateLong("COMPANY_ID",NewCompanyID);
                rsHistory.updateLong("SUPP_ID", SuppIDNew);
                rsHistory.updateString("SUPPLIER_CODE",(String) objSupplier.getAttribute("SUPPLIER_CODE").getObj());
                rsHistory.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
                rsHistory.updateString("SUPP_NAME",(String) objSupplier.getAttribute("SUPP_NAME").getObj());
                rsHistory.updateString("ATTN",(String) objSupplier.getAttribute("ATTN").getObj());
                rsHistory.updateString("ADD1",(String)objSupplier.getAttribute("ADD1").getObj());
                rsHistory.updateString("ADD2",(String)objSupplier.getAttribute("ADD2").getObj());
                rsHistory.updateString("ADD3",(String)objSupplier.getAttribute("ADD3").getObj());
                rsHistory.updateString("CITY",(String)objSupplier.getAttribute("CITY").getObj());
                rsHistory.updateString("PROPOSED_ITEM",(String)objSupplier.getAttribute("PROPOSED_ITEM").getObj());
                rsHistory.updateString("STATE",(String)objSupplier.getAttribute("STATE").getObj());
                rsHistory.updateString("COUNTRY",(String)objSupplier.getAttribute("COUNTRY").getObj());
                rsHistory.updateString("STATE_CODE",(String)objSupplier.getAttribute("STATE_CODE").getObj());
                rsHistory.updateString("STATE_GST_CODE",(String)objSupplier.getAttribute("STATE_GST_CODE").getObj());
                rsHistory.updateString("PLACE_OF_SUPPLY",(String)objSupplier.getAttribute("PLACE_OF_SUPPLY").getObj());
                rsHistory.updateString("PINCODE",(String)objSupplier.getAttribute("PINCODE").getObj());
                rsHistory.updateString("PHONE_O",(String)objSupplier.getAttribute("PHONE_O").getObj());
                rsHistory.updateString("PHONE_RES",(String)objSupplier.getAttribute("PHONE_RES").getObj());
                rsHistory.updateString("FAX_NO",(String)objSupplier.getAttribute("FAX_NO").getObj());
                rsHistory.updateString("MOBILE_NO",(String)objSupplier.getAttribute("MOBILE_NO").getObj());
                rsHistory.updateString("EMAIL_ADD",(String)objSupplier.getAttribute("EMAIL_ADD").getObj());
                rsHistory.updateString("URL",(String)objSupplier.getAttribute("URL").getObj());
                rsHistory.updateString("WEB_SITE",(String)objSupplier.getAttribute("WEB_SITE").getObj());
                rsHistory.updateString("REGISTERATION",(String)objSupplier.getAttribute("REGISTERATION").getObj());
                rsHistory.updateString("SERVICETAX_NO",(String)objSupplier.getAttribute("SERVICETAX_NO").getObj());
                rsHistory.updateString("SERVICETAX_DATE",(String)objSupplier.getAttribute("SERVICETAX_DATE").getObj());
                rsHistory.updateString("CST_NO",(String)objSupplier.getAttribute("CST_NO").getObj());
                rsHistory.updateString("CST_DATE",(String)objSupplier.getAttribute("CST_DATE").getObj());
                rsHistory.updateString("GST_NO",(String)objSupplier.getAttribute("GST_NO").getObj());
                rsHistory.updateString("GST_DATE",(String)objSupplier.getAttribute("GST_DATE").getObj());
                rsHistory.updateString("GSTIN_NO",(String)objSupplier.getAttribute("GSTIN_NO").getObj());
                rsHistory.updateString("GSTIN_DATE",(String)objSupplier.getAttribute("GSTIN_DATE").getObj());
                rsHistory.updateString("ECC_NO",(String)objSupplier.getAttribute("ECC_NO").getObj());
                rsHistory.updateString("ECC_DATE",(String)objSupplier.getAttribute("ECC_DATE").getObj());
                rsHistory.updateString("SSIREG_NO",(String)objSupplier.getAttribute("SSIREG_NO").getObj());
                rsHistory.updateString("SSIREG_DATE",(String)objSupplier.getAttribute("SSIREG_DATE").getObj());
                rsHistory.updateString("ESIREG_NO",(String)objSupplier.getAttribute("ESIREG_NO").getObj());
                rsHistory.updateString("ESIREG_DATE",(String)objSupplier.getAttribute("ESIREG_DATE").getObj());
                
                rsHistory.updateString("PAN_NO",objSupplier.getAttribute("PAN_NO").getString());
                if(getAttribute("PAN_DATE").getObj().equals(""))
                {
                rsHistory.updateString("PAN_DATE",null);
                }
                else
                {
                rsHistory.updateString("PAN_DATE",(String)getAttribute("PAN_DATE").getObj());
                }
                rsHistory.updateBoolean("MSME",(boolean)objSupplier.getAttribute("MSME").getBool());
                rsHistory.updateString("MSME_UAN",objSupplier.getAttribute("MSME_UAN").getString());
                rsHistory.updateString("MSME_DIC_NO",objSupplier.getAttribute("MSME_DIC_NO").getString());
                //rsHistory.updateString("PAN_DATE",objSupplier.getAttribute("PAN_DATE").getString());
                
                rsHistory.updateLong("FORM",(long)objSupplier.getAttribute("FORM").getVal());
                rsHistory.updateString("CONTACT_PERSON_1",(String)objSupplier.getAttribute("CONTACT_PERSON_1").getObj());
                rsHistory.updateString("CONTACT_PERSON_2",(String)objSupplier.getAttribute("CONTACT_PERSON_2").getObj());
                rsHistory.updateBoolean("ONETIME_SUPPLIER",(boolean)objSupplier.getAttribute("ONETIME_SUPPLIER").getBool());
                rsHistory.updateString("FROM_DATE_REG",(String)objSupplier.getAttribute("FROM_DATE_REG").getObj());
                rsHistory.updateString("TO_DATE_REG",(String)objSupplier.getAttribute("TO_DATE_REG").getObj());
                rsHistory.updateString("BLOCKED",(String)objSupplier.getAttribute("BLOCKED").getObj());
                rsHistory.updateBoolean("SLOW_MOVING",(boolean)objSupplier.getAttribute("SLOW_MOVING").getBool());
                rsHistory.updateBoolean("ST35_REGISTERED",(boolean)objSupplier.getAttribute("ST35_REGISTERED").getBool());
                rsHistory.updateBoolean("SSIREG",(boolean)objSupplier.getAttribute("SSIREG").getBool());
                rsHistory.updateLong("PAYMENT_DAYS",(long) objSupplier.getAttribute("PAYMENT_DAYS").getVal());
                rsHistory.updateInt("PAYMENT_TERM_CODE",(int)objSupplier.getAttribute("PAYMENT_TERM_CODE").getVal());
                rsHistory.updateString("LAST_PO_NO",(String)objSupplier.getAttribute("LAST_PO_NO").getObj());
                rsHistory.updateString("LAST_TRANS_DATE",(String)objSupplier.getAttribute("LAST_TRANS_DATE").getObj());
                rsHistory.updateLong("CREATED_BY",(long) objSupplier.getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("CREATED_DATE",(String)objSupplier.getAttribute("CREATED_DATE").getObj());
                rsHistory.updateLong("MODIFIED_BY",(long) objSupplier.getAttribute("MODIFIED_BY").getVal());
                rsHistory.updateString("MODIFIED_DATE",(String)objSupplier.getAttribute("MODIFIED_DATE").getObj());
                rsHistory.updateLong("BANK_ID",(long) objSupplier.getAttribute("BANK_ID").getVal());
                rsHistory.updateString("BANK_NAME",(String)objSupplier.getAttribute("BANK_NAME").getObj());
                rsHistory.updateLong("HIERARCHY_ID",(long) objSupplier.getAttribute("HIERARCHY_ID").getVal());
                rsHistory.updateBoolean("CHANGED",true);
                rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateInt("PARENT_SUPP_ID",(int)objSupplier.getAttribute("PARENT_SUPP_ID").getVal());
                rsHistory.updateString("MAIN_ACCOUNT_CODE",objSupplier.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsHistory.insertRow();
                
                
                
                //==============Inserting Into Party Master=======//
                
                Statement stParty=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsParty=stParty.executeQuery("SELECT * FROM D_COM_PARTY LIMIT 1");
                int PartyID = ((int)data.getMaxID(NewCompanyID,"D_COM_PARTY","PARTY_ID",dbURL));
                
                rsParty.moveToInsertRow();
                rsParty.updateInt("COMPANY_ID",NewCompanyID);
                rsParty.updateInt("PARTY_ID",PartyID);
                rsParty.updateString("SUPPLIER_CODE",(String)objSupplier.getAttribute("SUPPLIER_CODE").getObj());
                rsParty.updateString("PARTY_NAME",(String)objSupplier.getAttribute("SUPP_NAME").getObj());
                rsParty.updateBoolean("APPROVED_SUPPLIER",true);
                rsParty.updateString("ADD1",(String)objSupplier.getAttribute("ADD1").getObj());
                rsParty.updateString("ADD2",(String)objSupplier.getAttribute("ADD2").getObj());
                rsParty.updateString("ADD3",(String)objSupplier.getAttribute("ADD3").getObj());
                rsParty.updateString("CITY",(String)objSupplier.getAttribute("CITY").getObj());
                rsParty.updateString("STATE",(String)objSupplier.getAttribute("STATE").getObj());
                rsParty.updateString("COUNTRY",(String)objSupplier.getAttribute("COUNTRY").getObj());
                rsParty.updateString("PHONE_O",(String)objSupplier.getAttribute("PHONE_O").getObj());
                rsParty.updateString("PHONE_R",(String)objSupplier.getAttribute("PHONE_RES").getObj());
                rsParty.updateString("MOBILE",(String)objSupplier.getAttribute("MOBILE_NO").getObj());
                rsParty.updateString("EMAIL",(String)objSupplier.getAttribute("EMAIL_ADD").getObj());
                rsParty.updateInt("CREATED_BY",(int)objSupplier.getAttribute("CREATED_BY").getVal());
                rsParty.updateString("CREATED_DATE",(String)objSupplier.getAttribute("CREATED_DATE").getObj());
                rsParty.updateInt("MODIFIED_BY",(int)objSupplier.getAttribute("MODIFIED_BY").getVal());
                rsParty.updateString("MODIFIED_DATE",(String)objSupplier.getAttribute("MODIFIED_DATE").getObj());
                rsParty.updateString("REMARKS",(String)objSupplier.getAttribute("REMARKS").getObj());
                rsParty.updateBoolean("CHANGED",true);
                rsParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsParty.insertRow();
                
                
                Statement stSuppItemCategory=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsSuppItemCategory=stSuppItemCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY LIMIT 1");
                
                Statement stHCategory=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsHCategory=stHCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY_H LIMIT 1");
                
                long nCompanyID=(long)objSupplier.getAttribute("COMPANY_ID").getVal();
                long nSuppID=SuppIDNew;
                
                //Now Insert records into detail table
                for(int i=1;i<=objSupplier.colSupplierItems.size();i++) {
                    clsSupplierItem ObjItems=(clsSupplierItem)objSupplier.colSupplierItems.get(Integer.toString(i));
                    rsSuppItemCategory.moveToInsertRow();
                    rsSuppItemCategory.updateLong("COMPANY_ID",NewCompanyID);
                    rsSuppItemCategory.updateLong("SUPP_ID",nSuppID);
                    rsSuppItemCategory.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                    rsSuppItemCategory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsSuppItemCategory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsSuppItemCategory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsSuppItemCategory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsSuppItemCategory.updateBoolean("CHANGED",true);
                    rsSuppItemCategory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsSuppItemCategory.insertRow();
                    
                    rsHCategory.moveToInsertRow();
                    rsHCategory.updateInt("REVISION_NO",1);
                    rsHCategory.updateLong("COMPANY_ID",NewCompanyID);
                    rsHCategory.updateLong("SUPP_ID",nSuppID);
                    rsHCategory.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                    rsHCategory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsHCategory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsHCategory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsHCategory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsHCategory.updateBoolean("CHANGED",true);
                    rsHCategory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHCategory.insertRow();
                }
                
                Statement stTerms=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTerms=stTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS LIMIT 1");
                
                Statement stHTerms=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsHTerms=stHTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS_H LIMIT 1");
                
                //Now Insert records into detail table
                for(int i=1;i<=objSupplier.colSuppTerms.size();i++) {
                    clsSuppTerms ObjItems=(clsSuppTerms) objSupplier.colSuppTerms.get(Integer.toString(i));
                    rsTerms.moveToInsertRow();
                    rsTerms.updateLong("COMPANY_ID",NewCompanyID);
                    rsTerms.updateLong("SUPP_ID",nSuppID);
                    rsTerms.updateInt("SR_NO",i);
                    rsTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                    rsTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                    rsTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                    rsTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                    rsTerms.updateBoolean("CHANGED",true);
                    rsTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTerms.insertRow();
                    
                    rsHTerms.moveToInsertRow();
                    rsHTerms.updateInt("REVISION_NO",1);
                    rsHTerms.updateLong("COMPANY_ID",NewCompanyID);
                    rsHTerms.updateLong("SUPP_ID",nSuppID);
                    rsHTerms.updateInt("SR_NO",i);
                    rsHTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                    rsHTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                    rsHTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                    rsHTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                    rsHTerms.updateBoolean("CHANGED",true);
                    rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHTerms.insertRow();
                }
                
                
                Statement stChilds=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsChilds=stChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS LIMIT 1");
                
                Statement stHChilds=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsHChilds=stHChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS_H LIMIT 1");
                
                //Now Insert records into detail table
                for(int i=1;i<=objSupplier.colSuppChilds.size();i++) {
                    clsSuppChilds ObjItems=(clsSuppChilds) objSupplier.colSuppChilds.get(Integer.toString(i));
                    rsChilds.moveToInsertRow();
                    rsChilds.updateLong("COMPANY_ID",NewCompanyID);
                    rsChilds.updateLong("SUPP_ID",nSuppID);
                    rsChilds.updateInt("SR_NO",i);
                    rsChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                    rsChilds.updateBoolean("CHANGED",true);
                    rsChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsChilds.insertRow();
                    
                    rsHChilds.moveToInsertRow();
                    rsHChilds.updateInt("REVISION_NO",1);
                    rsHChilds.updateLong("COMPANY_ID",NewCompanyID);
                    rsHChilds.updateLong("SUPP_ID",nSuppID);
                    rsHChilds.updateInt("SR_NO",i);
                    rsHChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                    rsHChilds.updateBoolean("CHANGED",true);
                    rsHChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHChilds.insertRow();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void AddPartyToUnregMaster(String SupplierCode) {
        try {
            clsSupplier objSupplier=new clsSupplier();
            
            if(objSupplier.Filter("WHERE SUPPLIER_CODE='"+SupplierCode+"'")) {
                clsParty ObjParty=new clsParty();
                ObjParty.LoadData(EITLERPGLOBAL.gCompanyID);
                
                //Setup the Data
                ObjParty.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                ObjParty.setAttribute("PARTY_NAME",(String)objSupplier.getAttribute("SUPP_NAME").getObj());
                ObjParty.setAttribute("APPROVED_SUPPLIER",true);
                ObjParty.setAttribute("SUPPLIER_CODE",(String)objSupplier.getAttribute("SUPPLIER_CODE").getObj());
                ObjParty.setAttribute("ADD1",(String)objSupplier.getAttribute("ADD1").getObj());
                ObjParty.setAttribute("ADD2",(String)objSupplier.getAttribute("ADD2").getObj());
                ObjParty.setAttribute("ADD3",(String)objSupplier.getAttribute("ADD3").getObj());
                ObjParty.setAttribute("CITY",(String)objSupplier.getAttribute("CITY").getObj());
                ObjParty.setAttribute("STATE",(String)objSupplier.getAttribute("STATE").getObj());
                ObjParty.setAttribute("COUNTRY",(String)objSupplier.getAttribute("COUNTRY").getObj());
                ObjParty.setAttribute("PHONE_O",(String)objSupplier.getAttribute("PHONE_O").getObj());
                ObjParty.setAttribute("PHONE_R",(String)objSupplier.getAttribute("PHONE_RES").getObj());
                ObjParty.setAttribute("MOBILE",(String)objSupplier.getAttribute("MOBILE_NO").getObj());
                ObjParty.setAttribute("EMAIL",(String)objSupplier.getAttribute("EMAIL_ADD").getObj());
                ObjParty.setAttribute("REMARKS","");
                ObjParty.setAttribute("DISTANCE_KM",(int)objSupplier.getAttribute("DISTANCE_KM").getVal());
                //Remove old Party (if exist);
                data.Execute("DELETE FROM D_COM_PARTY WHERE SUPPLIER_CODE='"+SupplierCode+"'");
                
                ObjParty.Insert();
            }
            
        }
        catch(Exception e) {
            
        }
    }
    
           private void Add_in_Fin_PartyMaster_notexist(){
        try{
            Connection conn=data.getConn();
            Statement stmt=conn.createStatement();

            // --------------INSERT VALUE FOR 125019
            stmt.execute("INSERT INTO FINANCE.D_FIN_PARTY_MASTER (COMPANY_ID,PARTY_CODE,MAIN_ACCOUNT_CODE,PARTY_ID,PARTY_NAME,SH_NAME,REMARKS,SH_CODE,CREDIT_DAYS,CREDIT_LIMIT,DEBIT_LIMIT,TIN_NO,TIN_DATE,CST_NO,CST_DATE,ECC_NO,ECC_DATE,SERVICE_TAX_NO,SERVICE_TAX_DATE,SSI_NO,SSI_DATE,ESI_NO,ESI_DATE,ADDRESS,CITY,PINCODE,STATE,COUNTRY,PHONE,FAX,MOBILE,EMAIL,URL,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,HIERARCHY_ID,CANCELLED,BLOCKED,PAN_NO,PAN_DATE,CHANGED,CHANGED_DATE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CLOSING_BALANCE,CLOSING_EFFECT,PARTY_TYPE,CHARGE_CODE_II,CHARGE_CODE,GSTIN_NO,GSTIN_DATE,PLACE_OF_SUPPLY,STATE_CODE,STATE_GST_CODE,MSME_UAN,MSME,MSME_DIC_NO)  SELECT S.COMPANY_ID,SUPPLIER_CODE,S.MAIN_ACCOUNT_CODE,0,SUPP_NAME,'','','',0,0,0,GST_NO,GST_DATE,S.CST_NO,S.CST_DATE,S.ECC_NO,S.ECC_DATE,SERVICETAX_NO,SERVICETAX_DATE,SSIREG_NO,SSIREG_DATE,ESIREG_NO,ESIREG_DATE,ADD1,S.CITY,S.PINCODE,S.STATE,S.COUNTRY,S.PHONE_O,S.FAX_NO,S.MOBILE_NO,S.EMAIL_ADD,S.URL, 1, S.APPROVED_DATE,0,'0000-00-00','',0,0,0,S.PAN_NO,S.PAN_DATE,1,S.CHANGED_DATE,'SYSTEM',S.CREATED_DATE,'SYSTEM',S.MODIFIED_DATE,0,'D',0,'','',S.GSTIN_NO,S.GSTIN_DATE,S.PLACE_OF_SUPPLY,S.STATE_CODE,S.STATE_GST_CODE,S.MSME_UAN,S.MSME,S.MSME_DIC_NO  FROM (SELECT * FROM DINESHMILLS.D_COM_SUPP_MASTER  WHERE MAIN_ACCOUNT_CODE = '125019' AND APPROVED =1 AND CANCELLED =0) AS S LEFT JOIN (SELECT * FROM FINANCE.D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = '125019') AS F ON SUPPLIER_CODE = PARTY_CODE WHERE PARTY_CODE IS NULL");
                   
            //----INSERT VALUE FOR 125033
             stmt.execute("INSERT INTO FINANCE.D_FIN_PARTY_MASTER (COMPANY_ID,PARTY_CODE,MAIN_ACCOUNT_CODE,PARTY_ID,PARTY_NAME,SH_NAME,REMARKS,SH_CODE,CREDIT_DAYS,CREDIT_LIMIT,DEBIT_LIMIT,TIN_NO,TIN_DATE,CST_NO,CST_DATE,ECC_NO,ECC_DATE,SERVICE_TAX_NO,SERVICE_TAX_DATE,SSI_NO,SSI_DATE,ESI_NO,ESI_DATE,ADDRESS,CITY,PINCODE,STATE,COUNTRY,PHONE,FAX,MOBILE,EMAIL,URL,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,REJECTED_REMARKS,HIERARCHY_ID,CANCELLED,BLOCKED,PAN_NO,PAN_DATE,CHANGED,CHANGED_DATE,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,CLOSING_BALANCE,CLOSING_EFFECT,PARTY_TYPE,CHARGE_CODE_II,CHARGE_CODE,GSTIN_NO,GSTIN_DATE,PLACE_OF_SUPPLY,STATE_CODE,STATE_GST_CODE,MSME_UAN,MSME,MSME_DIC_NO) SELECT S.COMPANY_ID,SUPPLIER_CODE,S.MAIN_ACCOUNT_CODE,0,SUPP_NAME,'','','',0,0,0,GST_NO,GST_DATE,S.CST_NO,S.CST_DATE,S.ECC_NO,S.ECC_DATE,SERVICETAX_NO,SERVICETAX_DATE,SSIREG_NO,SSIREG_DATE,ESIREG_NO,ESIREG_DATE,ADD1,S.CITY,S.PINCODE,S.STATE,S.COUNTRY,S.PHONE_O,S.FAX_NO,S.MOBILE_NO,S.EMAIL_ADD,S.URL, 1, S.APPROVED_DATE,0,'0000-00-00','',0,0,0,S.PAN_NO,S.PAN_DATE,1,S.CHANGED_DATE,'SYSTEM',S.CREATED_DATE,'SYSTEM',S.MODIFIED_DATE,0,'D',0,'','',S.GSTIN_NO,S.GSTIN_DATE,S.PLACE_OF_SUPPLY,S.STATE_CODE,S.STATE_GST_CODE,S.MSME_UAN,S.MSME,S.MSME_DIC_NO FROM (SELECT * FROM DINESHMILLS.D_COM_SUPP_MASTER  WHERE MAIN_ACCOUNT_CODE = '125033' AND APPROVED =1 AND CANCELLED =0) AS S LEFT JOIN (SELECT * FROM FINANCE.D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = '125033') AS F ON SUPPLIER_CODE = PARTY_CODE WHERE PARTY_CODE IS NULL");
          
            
                }catch(SQLException e){e.printStackTrace();}
    
    }  
    
    
    
    private boolean Validate() {
        //** Validations **//
        //komal ***
        String status = getAttribute("APPROVAL_STATUS").getString();
        
        if (status.trim().equals("F")) {
            String SupplierCode = (String) getAttribute("SUPPLIER_CODE").getObj();
            int len = SupplierCode.length();
            if (len == 6) {
                if (! EITLERPGLOBAL.IsNumber(SupplierCode)) {
                    JOptionPane.showMessageDialog(null,"Supplier Code Must be Numeric");
                }
            }
            else {
                LastError="Please enter Supplier Code";
                return false;
            }
        }
        
        
        //***komal
        //*****************//
        
        return true;
    }
}
