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

public class clsSupplierAmend {
    
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
    
    public boolean AmendChilds=false;
    public HashMap colBlocked=new HashMap();
    
    
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
    public clsSupplierAmend() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("AMEND_ID",new Variant(0));
        props.put("AMEND_DATE",new Variant(""));
        props.put("AMEND_REASON",new Variant(""));
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
        props.put("STATE_GST_CODE",new Variant(""));
        props.put("STATE_CODE",new Variant(""));
        
        props.put("DISTANCE_KM",new Variant(0));
        props.put("MSME",new Variant(false)); 
        props.put("MSME_UAN",new Variant(""));
        props.put("MSME_DIC_NO",new Variant(""));
        
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        
        props.put("PARENT_SUPP_ID",new Variant(0));
        
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
            String strSql = "SELECT * FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " ORDER BY SUPP_NAME";
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
        Statement stHistory,stHCategory,stHTerms,stHChilds,stOSupplier,stOItemCategory,stOTerms,stOChilds;
        ResultSet rsHistory,rsHCategory,rsHTerms,rsHChilds,rsOSupplier,rsOItemCategory,rsOTerms,rsOChilds;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHCategory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_MASTER_H WHERE AMEND_ID=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHCategory=stHCategory.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_ITEMCATEGORY_H WHERE AMEND_ID=1");
            rsHCategory.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_TERMS_H WHERE AMEND_ID=1");
            rsHTerms.first();
            rsHChilds=stHChilds.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_CHILDS_H WHERE AMEND_ID=1");
            rsHChilds.first();
            //------------------------------------//
            
            //rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            setAttribute("AMEND_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_SUPP_AMEND_MASTER","AMEND_ID"));
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            //==== Parent ID =======//
            if(AStatus.equals("F")) {
                if(AmendChilds) {
                    for(int i=1;i<=colBlocked.size();i++) {
                        clsSuppChilds objSupp=(clsSuppChilds)colBlocked.get(Integer.toString(i));
                        
                        int bSuppID=(int)objSupp.getAttribute("SUPP_ID").getVal();
                        boolean bBlocked=objSupp.getAttribute("BLOCKED").getBool();
                        
                        if(bBlocked) {
                            data.Execute("UPDATE D_COM_SUPP_MASTER SET BLOCKED='Y' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+bSuppID);
                        }
                        else {
                            data.Execute("UPDATE D_COM_SUPP_MASTER SET BLOCKED='N' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+bSuppID);
                        }
                    }
                }
            }
            
            
            if(AStatus.equals("F")) {
                String SupplierCode=(String)getAttribute("SUPPLIER_CODE").getObj();
                int SuppID=clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID,SupplierCode);
                
                clsSupplier ObjSupp=(clsSupplier)clsSupplier.getObjectEx(EITLERPGLOBAL.gCompanyID, SupplierCode);
                
                //Delete previous data
                data.Execute("DELETE FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                data.Execute("DELETE FROM D_COM_SUPP_ITEMCATEGORY WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                data.Execute("DELETE FROM D_COM_SUPP_TERMS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                data.Execute("DELETE FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                
                stOSupplier=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOSupplier=stOSupplier.executeQuery("SELECT * FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='1'");
                rsOSupplier.first();
                rsOSupplier.moveToInsertRow();
                rsOSupplier.updateLong("COMPANY_ID", (long)getAttribute("COMPANY_ID").getVal());
                rsOSupplier.updateLong("SUPP_ID", SuppID);
                rsOSupplier.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
                rsOSupplier.updateString("DUMMY_SUPPLIER_CODE",(String)ObjSupp.getAttribute("DUMMY_SUPPLIER_CODE").getObj());
                rsOSupplier.updateString("SUPP_NAME",(String)getAttribute("SUPP_NAME").getObj());
                rsOSupplier.updateString("ATTN",(String) getAttribute("ATTN").getObj());
                rsOSupplier.updateString("ADD1",(String)getAttribute("ADD1").getObj());
                rsOSupplier.updateString("ADD2",(String)getAttribute("ADD2").getObj());
                rsOSupplier.updateString("ADD3",(String)getAttribute("ADD3").getObj());
                rsOSupplier.updateString("CITY",(String)getAttribute("CITY").getObj());
                rsOSupplier.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
                rsOSupplier.updateString("STATE",(String)getAttribute("STATE").getObj());
                rsOSupplier.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
                rsOSupplier.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
                rsOSupplier.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
                rsOSupplier.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
                rsOSupplier.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
                rsOSupplier.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
                rsOSupplier.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
                rsOSupplier.updateString("PHONE_RES",(String)getAttribute("PHONE_RES").getObj());
                rsOSupplier.updateString("FAX_NO",(String)getAttribute("FAX_NO").getObj());
                rsOSupplier.updateString("MOBILE_NO",(String)getAttribute("MOBILE_NO").getObj());
                rsOSupplier.updateString("EMAIL_ADD",(String)getAttribute("EMAIL_ADD").getObj());
                rsOSupplier.updateString("URL",(String)getAttribute("URL").getObj());
                rsOSupplier.updateString("WEB_SITE",(String)getAttribute("WEB_SITE").getObj());
                rsOSupplier.updateString("REGISTERATION",(String)getAttribute("REGISTERATION").getObj());
                rsOSupplier.updateString("SERVICETAX_NO",(String)getAttribute("SERVICETAX_NO").getObj());
                rsOSupplier.updateString("SERVICETAX_DATE",(String)getAttribute("SERVICETAX_DATE").getObj());
                rsOSupplier.updateString("CST_NO",(String)getAttribute("CST_NO").getObj());
                rsOSupplier.updateString("CST_DATE",(String)getAttribute("CST_DATE").getObj());
                rsOSupplier.updateString("GST_NO",(String)getAttribute("GST_NO").getObj());
                rsOSupplier.updateString("GST_DATE",(String)getAttribute("GST_DATE").getObj());
                rsOSupplier.updateString("GSTIN_NO",(String)getAttribute("GSTIN_NO").getObj());
                rsOSupplier.updateString("GSTIN_DATE",(String)getAttribute("GSTIN_DATE").getObj());
                rsOSupplier.updateString("ECC_NO",(String)getAttribute("ECC_NO").getObj());
                rsOSupplier.updateString("ECC_DATE",(String)getAttribute("ECC_DATE").getObj());
                rsOSupplier.updateString("SSIREG_NO",(String)getAttribute("SSIREG_NO").getObj());
                rsOSupplier.updateString("SSIREG_DATE",(String)getAttribute("SSIREG_DATE").getObj());
                rsOSupplier.updateString("ESIREG_NO",(String)getAttribute("ESIREG_NO").getObj());
                rsOSupplier.updateString("ESIREG_DATE",(String)getAttribute("ESIREG_DATE").getObj());
                
                rsOSupplier.updateString("PAN_NO",getAttribute("PAN_NO").getString());
                rsOSupplier.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());

                rsOSupplier.updateBoolean("MSME",(boolean)getAttribute("MSME").getBool());
                rsOSupplier.updateString("MSME_UAN",getAttribute("MSME_UAN").getString());
                rsOSupplier.updateString("MSME_DIC_NO",getAttribute("MSME_DIC_NO").getString());              

                rsOSupplier.updateLong("FORM",(long)getAttribute("FORM").getVal());
                rsOSupplier.updateString("CONTACT_PERSON_1",(String)getAttribute("CONTACT_PERSON_1").getObj());
                rsOSupplier.updateString("CONTACT_PERSON_2",(String)getAttribute("CONTACT_PERSON_2").getObj());
                rsOSupplier.updateBoolean("ONETIME_SUPPLIER",(boolean)getAttribute("ONETIME_SUPPLIER").getBool());
                if(getAttribute("FROM_DATE_REG").getObj().equals(""))
                 {
                rsOSupplier.updateString("FROM_DATE_REG",null);
                    }
                 else
                    {
                    rsOSupplier.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
                }
                if(getAttribute("TO_DATE_REG").getObj().equals(""))
                {
                rsOSupplier.updateString("TO_DATE_REG",null);
                }
                else
                {
                rsOSupplier.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
                }
                //rsOSupplier.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
               // rsOSupplier.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
                rsOSupplier.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
                rsOSupplier.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
                rsOSupplier.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
                rsOSupplier.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
                rsOSupplier.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
                rsOSupplier.updateInt("PAYMENT_TERM_CODE",(int)getAttribute("PAYMENT_TERM_CODE").getVal());
                rsOSupplier.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
                if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
                {
                rsOSupplier.updateString("LAST_TRANS_DATE",null);
                }
                else
                {
                rsOSupplier.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
                }
                
                rsOSupplier.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
                //rsOSupplier.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
                rsOSupplier.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsOSupplier.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsOSupplier.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsOSupplier.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsOSupplier.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
                rsOSupplier.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
                rsOSupplier.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
                rsOSupplier.updateBoolean("CANCELLED",false);
                rsOSupplier.updateBoolean("CHANGED",true);
                rsOSupplier.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOSupplier.updateBoolean("APPROVED",true);
                rsOSupplier.updateString("APPROVED_DATE",(String)ObjSupp.getAttribute("APPROVED_DATE").getObj());
                rsOSupplier.updateBoolean("REJECTED",ObjSupp.getAttribute("REJECTED").getBool());
                rsOSupplier.updateString("REJECTED_DATE",(String)ObjSupp.getAttribute("REJECTED_DATE").getObj());
                rsOSupplier.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
                rsOSupplier.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsOSupplier.insertRow();
                
                
                stOItemCategory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOItemCategory=stOItemCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY WHERE SUPP_ID=1");
                rsOItemCategory.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colSupplierItems.size();i++) {
                    clsSupplierItem ObjItems=(clsSupplierItem) colSupplierItems.get(Integer.toString(i));
                    rsOItemCategory.moveToInsertRow();
                    rsOItemCategory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsOItemCategory.updateLong("SUPP_ID",SuppID);
                    rsOItemCategory.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                    rsOItemCategory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsOItemCategory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsOItemCategory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsOItemCategory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsOItemCategory.updateBoolean("CHANGED",true);
                    rsOItemCategory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsOItemCategory.insertRow();
                }
                
                
                stOTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOTerms=stOTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS WHERE SUPP_ID=1");
                rsOTerms.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colSuppTerms.size();i++) {
                    clsSuppTerms ObjItems=(clsSuppTerms) colSuppTerms.get(Integer.toString(i));
                    rsOTerms.moveToInsertRow();
                    rsOTerms.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsOTerms.updateLong("SUPP_ID",SuppID);
                    rsOTerms.updateInt("SR_NO",i);
                    rsOTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                    rsOTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                    rsOTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                    rsOTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                    rsOTerms.updateBoolean("CHANGED",true);
                    rsOTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsOTerms.insertRow();
                }
                
                
                stOChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOChilds=stOChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS WHERE SUPP_ID=1");
                rsOChilds.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colSuppChilds.size();i++) {
                    clsSuppChilds ObjItems=(clsSuppChilds) colSuppChilds.get(Integer.toString(i));
                    rsOChilds.moveToInsertRow();
                    rsOChilds.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsOChilds.updateLong("SUPP_ID",SuppID);
                    rsOChilds.updateInt("SR_NO",i);
                    rsOChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                    rsOChilds.updateBoolean("CHANGED",true);
                    rsOChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsOChilds.insertRow();
                }
                
                
                //Set older data to current
                setAttribute("SUPP_NAME",(String)ObjSupp.getAttribute("SUPP_NAME").getObj());
                setAttribute("ATTN",(String)ObjSupp.getAttribute("ATTN").getObj());
                setAttribute("ADD1",(String)ObjSupp.getAttribute("ADD1").getObj());
                setAttribute("ADD2",(String)ObjSupp.getAttribute("ADD2").getObj());
                setAttribute("ADD3",(String)ObjSupp.getAttribute("ADD3").getObj());
                setAttribute("CITY",(String)ObjSupp.getAttribute("CITY").getObj());
                setAttribute("PROPOSED_ITEM",(String)ObjSupp.getAttribute("PROPOSED_ITEM").getObj());
                setAttribute("STATE",(String)ObjSupp.getAttribute("STATE").getObj());
                setAttribute("COUNTRY",(String)ObjSupp.getAttribute("COUNTRY").getObj());
                setAttribute("STATE_CODE",(String)ObjSupp.getAttribute("STATE_CODE").getObj());
                setAttribute("STATE_GST_CODE",(String)ObjSupp.getAttribute("STATE_GST_CODE").getObj());
                setAttribute("PLACE_OF_SUPPLY",(String)ObjSupp.getAttribute("PLACE_OF_SUPPLY").getObj());
                setAttribute("PINCODE",(String)ObjSupp.getAttribute("PINCODE").getObj());
                setAttribute("PHONE_O",(String)ObjSupp.getAttribute("PHONE_O").getObj());
                setAttribute("PHONE_RES",(String)ObjSupp.getAttribute("PHONE_RES").getObj());
                setAttribute("FAX_NO",(String)ObjSupp.getAttribute("FAX_NO").getObj());
                setAttribute("MOBILE_NO",(String)ObjSupp.getAttribute("MOBILE_NO").getObj());
                setAttribute("EMAIL_ADD",(String)ObjSupp.getAttribute("EMAIL_ADD").getObj());
                setAttribute("URL",(String)ObjSupp.getAttribute("URL").getObj());
                setAttribute("WEB_SITE",(String)ObjSupp.getAttribute("WEB_SITE").getObj());
                setAttribute("REGISTERATION",(String)ObjSupp.getAttribute("REGISTERATION").getObj());
                setAttribute("SERVICETAX_NO",(String)ObjSupp.getAttribute("SERVICETAX_NO").getObj());
                setAttribute("SERVICETAX_DATE",(String)ObjSupp.getAttribute("SERVICETAX_DATE").getObj());
                setAttribute("CST_NO",(String)ObjSupp.getAttribute("CST_NO").getObj());
                setAttribute("CST_DATE",(String)ObjSupp.getAttribute("CST_DATE").getObj());
                setAttribute("GST_NO",(String)ObjSupp.getAttribute("GST_NO").getObj());
                setAttribute("GST_DATE",(String)ObjSupp.getAttribute("GST_DATE").getObj());
                setAttribute("GSTIN_NO",(String)ObjSupp.getAttribute("GSTIN_NO").getObj());
                setAttribute("GSTIN_DATE",(String)ObjSupp.getAttribute("GSTIN_DATE").getObj());
                setAttribute("ECC_NO",(String)ObjSupp.getAttribute("ECC_NO").getObj());
                setAttribute("ECC_DATE",(String)ObjSupp.getAttribute("ECC_DATE").getObj());
                setAttribute("SSIREG_NO",(String)ObjSupp.getAttribute("SSIREG_NO").getObj());
                setAttribute("SSIREG_DATE",(String)ObjSupp.getAttribute("SSIREG_DATE").getObj());
                setAttribute("ESIREG_NO",(String)ObjSupp.getAttribute("ESIREG_NO").getObj());
                setAttribute("ESIREG_DATE",(String)ObjSupp.getAttribute("ESIREG_DATE").getObj());
                
                setAttribute("PAN_NO",ObjSupp.getAttribute("PAN_NO").getString());
                setAttribute("PAN_DATE",ObjSupp.getAttribute("PAN_DATE").getString());
                setAttribute("MSME",ObjSupp.getAttribute("MSME").getBool());
                setAttribute("MSME_UAN",ObjSupp.getAttribute("MSME_UAN").getString());
                setAttribute("MSME_DIC_NO",ObjSupp.getAttribute("MSME_DIC_NO").getString());
                setAttribute("FORM",(long)ObjSupp.getAttribute("FORM").getVal());
                setAttribute("ST35_REGISTERED",ObjSupp.getAttribute("ST35_REGISTERED").getBool());
                setAttribute("SSIREG",ObjSupp.getAttribute("SSIREG").getBool());
                setAttribute("CONTACT_PERSON_1",(String)ObjSupp.getAttribute("CONTACT_PERSON_1").getObj());
                setAttribute("CONTACT_PERSON_2",(String)ObjSupp.getAttribute("CONTACT_PERSON_2").getObj());
                setAttribute("ONETIME_SUPPLIER",ObjSupp.getAttribute("ONETIME_SUPPLIER").getBool());
                setAttribute("FROM_DATE_REG",(String)ObjSupp.getAttribute("FROM_DATE_REG").getObj());
                
                setAttribute("TO_DATE_REG",(String)ObjSupp.getAttribute("TO_DATE_REG").getObj());
                setAttribute("BLOCKED",(String)ObjSupp.getAttribute("BLOCKED").getObj());
                setAttribute("SLOW_MOVING",ObjSupp.getAttribute("SLOW_MOVING").getBool());
                setAttribute("PAYMENT_DAYS",(int)ObjSupp.getAttribute("PAYMENT_DAYS").getVal());
                setAttribute("PAYMENT_TERM_CODE",(int)ObjSupp.getAttribute("PAYMENT_TERM_CODE").getVal());
                setAttribute("LAST_PO_NO",(String)ObjSupp.getAttribute("LAST_PO_NO").getObj());
                setAttribute("LAST_TRANS_DATE",(String)ObjSupp.getAttribute("LAST_TRANS_DATE").getObj());
                setAttribute("BANK_ID",(long)ObjSupp.getAttribute("BANK_ID").getVal());
                setAttribute("BANK_NAME",(String)ObjSupp.getAttribute("BANK_NAME").getObj());
                setAttribute("CREATED_BY",(int)ObjSupp.getAttribute("CREATED_BY").getVal());
                setAttribute("CREATED_DATE",(String)ObjSupp.getAttribute("CREATED_DATE").getObj());
                setAttribute("MODIFIED_BY",(int)ObjSupp.getAttribute("MODIFIED_BY").getVal());
                setAttribute("MODIFIED_DATE",(String)ObjSupp.getAttribute("MODIFIED_DATE").getObj());
                //setAttribute("HIERARCHY_ID",(long)ObjSupp.getAttribute("HIERARCHY_ID").getVal());
                setAttribute("APPROVED",ObjSupp.getAttribute("APPROVED").getBool());
                setAttribute("APPROVED_DATE",(String)ObjSupp.getAttribute("APPROVED_DATE").getObj());
                setAttribute("REJECTED",ObjSupp.getAttribute("REJECTED").getBool());
                setAttribute("REJECTED_DATE",(String)ObjSupp.getAttribute("REJECTED_DATE").getObj());
                setAttribute("PARENT_SUPP_ID",(int)ObjSupp.getAttribute("PARENT_SUPP_ID").getVal());
                setAttribute("MAIN_ACCOUNT_CODE",ObjSupp.getAttribute("MAIN_ACCOUNT_CODE").getString());
               
                setAttribute("DISTANCE_KM",(int)ObjSupp.getAttribute("DISTANCE_KM").getVal());


                //Now Populate the collection
                //first clear the collection
                colSupplierItems.clear();
                
                for(int i=1;i<=ObjSupp.colSupplierItems.size();i++) {
                    clsSupplierItem ObjItem=(clsSupplierItem)ObjSupp.colSupplierItems.get(Integer.toString(i));
                    clsSupplierItem NewItem=new clsSupplierItem();
                    
                    
                    NewItem.setAttribute("COMPANY_ID",(int)ObjItem.getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("AMEND_ID",(int)ObjItem.getAttribute("AMEND_ID").getVal());
                    NewItem.setAttribute("CATEGORY_ID",(int)ObjItem.getAttribute("CATEGORY_ID").getVal());
                    NewItem.setAttribute("CREATED_BY",(int)ObjItem.getAttribute("CREATED_BY").getVal());
                    NewItem.setAttribute("CREATED_DATE",(String)ObjItem.getAttribute("CREATED_DATE").getObj());
                    NewItem.setAttribute("MODIFIED_BY",(int)ObjItem.getAttribute("MODIFIED_BY").getVal());
                    NewItem.setAttribute("MODIFIED_DATE",(String)ObjItem.getAttribute("MODIFIED_DATE").getObj());
                    
                    colSupplierItems.put(Long.toString(i),NewItem);
                }
                
                colSuppTerms.clear();
                for(int i=1;i<=ObjSupp.colSuppTerms.size();i++) {
                    clsSuppTerms ObjItem=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(i));
                    clsSuppTerms NewItem=new clsSuppTerms();
                    
                    NewItem.setAttribute("COMPANY_ID",ObjItem.getAttribute("COMPANY_ID").getInt());
                    NewItem.setAttribute("AMEND_ID",ObjItem.getAttribute("SUPP_ID").getInt());
                    NewItem.setAttribute("SR_NO",ObjItem.getAttribute("SR_NO").getInt());
                    NewItem.setAttribute("TERM_TYPE",ObjItem.getAttribute("TERM_TYPE").getString());
                    NewItem.setAttribute("TERM_CODE",ObjItem.getAttribute("TERM_CODE").getInt());
                    NewItem.setAttribute("TERM_DAYS",ObjItem.getAttribute("TERM_DAYS").getInt());
                    NewItem.setAttribute("TERM_DESC",ObjItem.getAttribute("TERM_DESC").getString());
                    
                    colSuppTerms.put(Long.toString(i),NewItem);
                }
                
                colSuppChilds.clear();
                for(int i=1;i<=ObjSupp.colSuppChilds.size();i++) {
                    clsSuppChilds ObjItem=(clsSuppChilds)ObjSupp.colSuppChilds.get(Integer.toString(i));
                    clsSuppChilds NewItem=new clsSuppChilds();
                    
                    NewItem.setAttribute("COMPANY_ID",(int)ObjItem.getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("AMEND_ID",(int)ObjItem.getAttribute("SUPP_ID").getVal());
                    NewItem.setAttribute("SR_NO",(int)ObjItem.getAttribute("SR_NO").getVal());
                    NewItem.setAttribute("CHILD_SUPP_ID",(int)ObjItem.getAttribute("CHILD_SUPP_ID").getVal());
                    
                    colSuppChilds.put(Long.toString(i),NewItem);
                }
                
                
                //==========Sync Party data with other modules =============//
                if(AStatus.equals("F")) {
                    if(EITLERPGLOBAL.gCompanyID==2) {
                        String newURL=clsFinYear.getDBURL(3,EITLERPGLOBAL.FinYearFrom);
                        AddPartyToExternalDB(getAttribute("SUPPLIER_CODE").getString(), newURL);
                    }
                    AddPartyToUnregMaster(getAttribute("SUPPLIER_CODE").getString());
                    AddPartyToFinance(getAttribute("SUPPLIER_CODE").getString());
                }
                //==========================================================//
                
                
            }
            
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID", (long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("AMEND_ID", (long)getAttribute("AMEND_ID").getVal());
            rsResultSet.updateString("AMEND_DATE",(String)getAttribute("AMEND_DATE").getObj());
            rsResultSet.updateString("AMEND_REASON",(String)getAttribute("AMEND_REASON").getObj());
            rsResultSet.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            String DummyCode=Long.toString((long)getAttribute("AMEND_ID").getVal());
            rsResultSet.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
            rsResultSet.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsResultSet.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsResultSet.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsResultSet.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
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
            rsResultSet.updateInt("PAYMENT_TERM_CODE",(int)getAttribute("PAYMENT_TERM_CODE").getVal());
            rsResultSet.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
            rsResultSet.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
//            {
//                rsResultSet.updateString("LAST_TRANS_DATE",null);
//            }
//            else
//            {
//                rsResultSet.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            }
            
            
            rsResultSet.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
//            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsResultSet.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateBoolean("CANCELLED",false);
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
            rsHistory.updateLong("AMEND_ID", (long)getAttribute("AMEND_ID").getVal());
            rsHistory.updateString("AMEND_DATE", (String)getAttribute("AMEND_DATE").getObj());
            rsHistory.updateString("AMEND_REASON", (String)getAttribute("AMEND_REASON").getObj());
            rsHistory.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            rsHistory.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
            rsHistory.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            rsHistory.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsHistory.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsHistory.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsHistory.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
            rsHistory.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
            rsHistory.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
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
            //rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            if(getAttribute("FROM_DATE_REG").getObj().equals(""))
            {
                rsHistory.updateString("FROM_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            }
            //rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            if(getAttribute("TO_DATE_REG").getObj().equals(""))
            {
                rsHistory.updateString("TO_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            }
            rsHistory.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
            rsHistory.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
            rsHistory.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
            rsHistory.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
            rsHistory.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
            rsHistory.updateInt("PAYMENT_TERM_CODE",(int)getAttribute("PAYMENT_TERM_CODE").getVal());
            rsHistory.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
            rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",null);
//            }
//            else
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            }
            
            rsHistory.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
//            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsHistory.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.insertRow();
            
            
            ResultSet rsTmp,rsTerms,rsChilds;
            Statement tmpStmt,stTerms,stChilds;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_ITEMCATEGORY");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            long nSuppID=(long)getAttribute("AMEND_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colSupplierItems.size();i++) {
                clsSupplierItem ObjItems=(clsSupplierItem) colSupplierItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateLong("AMEND_ID",nSuppID);
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
                rsHCategory.updateLong("AMEND_ID",nSuppID);
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
            rsTerms=stTerms.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_TERMS");
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppTerms.size();i++) {
                clsSuppTerms ObjItems=(clsSuppTerms) colSuppTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateLong("COMPANY_ID",nCompanyID);
                rsTerms.updateLong("AMEND_ID",nSuppID);
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
                rsHTerms.updateLong("AMEND_ID",nSuppID);
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
            rsChilds=stChilds.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_CHILDS");
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppChilds.size();i++) {
                clsSuppChilds ObjItems=(clsSuppChilds) colSuppChilds.get(Integer.toString(i));
                rsChilds.moveToInsertRow();
                rsChilds.updateLong("COMPANY_ID",nCompanyID);
                rsChilds.updateLong("AMEND_ID",nSuppID);
                rsChilds.updateInt("SR_NO",i);
                rsChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                rsChilds.updateBoolean("CHANGED",true);
                rsChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsChilds.insertRow();
                
                rsHChilds.moveToInsertRow();
                rsHChilds.updateInt("REVISION_NO",1);
                rsHChilds.updateLong("COMPANY_ID",nCompanyID);
                rsHChilds.updateLong("AMEND_ID",nSuppID);
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
            ObjFlow.ModuleID=50; //SUPPLIER AMENDMENT MASTER MODULE ID
            ObjFlow.DocNo=Long.toString((long)getAttribute("AMEND_ID").getVal());
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_COM_SUPP_AMEND_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="AMEND_ID";
            
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
        Statement stHistory,stHCategory,stHTerms,stHChilds,stOSupplier,stOItemCategory,stOTerms,stOChilds;
        ResultSet rsHistory,rsHCategory,rsHTerms,rsHChilds,rsOSupplier,rsOItemCategory,rsOTerms,rsOChilds;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHCategory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_MASTER_H WHERE AMEND_ID=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHCategory=stHCategory.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_ITEMCATEGORY_H WHERE AMEND_ID=1");
            rsHCategory.first();
            rsHTerms=stHTerms.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_TERMS_H WHERE AMEND_ID=1");
            rsHTerms.first();
            rsHChilds=stHChilds.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_CHILDS_H WHERE AMEND_ID=1");
            rsHChilds.first();
            //------------------------------------//
            
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                if(AmendChilds) {
                    
                    for(int i=1;i<=colBlocked.size();i++) {
                        clsSuppChilds objSupp=(clsSuppChilds)colBlocked.get(Integer.toString(i));
                        
                        int bSuppID=(int)objSupp.getAttribute("SUPP_ID").getVal();
                        boolean bBlocked=objSupp.getAttribute("BLOCKED").getBool();
                        
                        if(bBlocked) {
                            data.Execute("UPDATE D_COM_SUPP_MASTER SET BLOCKED='Y' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+bSuppID);
                        }
                        else {
                            data.Execute("UPDATE D_COM_SUPP_MASTER SET BLOCKED='N' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+bSuppID);
                        }
                    }
                }
            }
            
            
            if(AStatus.equals("F")) {
                String SupplierCode=(String)getAttribute("SUPPLIER_CODE").getObj();
                int SuppID=clsSupplier.getSupplierID(EITLERPGLOBAL.gCompanyID,SupplierCode);
                
                clsSupplier ObjSupp=(clsSupplier)clsSupplier.getObjectEx(EITLERPGLOBAL.gCompanyID, SupplierCode);
                
                //Delete previous data
                data.Execute("DELETE FROM D_COM_SUPP_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                data.Execute("DELETE FROM D_COM_SUPP_ITEMCATEGORY WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                data.Execute("DELETE FROM D_COM_SUPP_TERMS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                data.Execute("DELETE FROM D_COM_SUPP_CHILDS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SUPP_ID="+SuppID);
                
                
                stOSupplier=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOSupplier=stOSupplier.executeQuery("SELECT * FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='1'");
                rsOSupplier.first();
                rsOSupplier.moveToInsertRow();
                rsOSupplier.updateLong("COMPANY_ID", (long)getAttribute("COMPANY_ID").getVal());
                rsOSupplier.updateLong("SUPP_ID", SuppID);
                rsOSupplier.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
                rsOSupplier.updateString("DUMMY_SUPPLIER_CODE",(String)ObjSupp.getAttribute("DUMMY_SUPPLIER_CODE").getObj());
                rsOSupplier.updateString("SUPP_NAME",(String)getAttribute("SUPP_NAME").getObj());
                rsOSupplier.updateString("ATTN",(String) getAttribute("ATTN").getObj());
                rsOSupplier.updateString("ADD1",(String)getAttribute("ADD1").getObj());
                rsOSupplier.updateString("ADD2",(String)getAttribute("ADD2").getObj());
                rsOSupplier.updateString("ADD3",(String)getAttribute("ADD3").getObj());
                rsOSupplier.updateString("CITY",(String)getAttribute("CITY").getObj());
                rsOSupplier.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
                rsOSupplier.updateString("STATE",(String)getAttribute("STATE").getObj());
                rsOSupplier.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
                rsOSupplier.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
                rsOSupplier.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
                rsOSupplier.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
                rsOSupplier.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj()); 
                rsOSupplier.updateString("PHONE_O",(String)getAttribute("PHONE_O").getObj());
                rsOSupplier.updateString("PHONE_RES",(String)getAttribute("PHONE_RES").getObj());
                rsOSupplier.updateString("FAX_NO",(String)getAttribute("FAX_NO").getObj());
                rsOSupplier.updateString("MOBILE_NO",(String)getAttribute("MOBILE_NO").getObj());
                rsOSupplier.updateString("EMAIL_ADD",(String)getAttribute("EMAIL_ADD").getObj());
                rsOSupplier.updateString("URL",(String)getAttribute("URL").getObj());
                rsOSupplier.updateString("WEB_SITE",(String)getAttribute("WEB_SITE").getObj());
                rsOSupplier.updateString("REGISTERATION",(String)getAttribute("REGISTERATION").getObj());
                rsOSupplier.updateString("SERVICETAX_NO",(String)getAttribute("SERVICETAX_NO").getObj());
                rsOSupplier.updateString("SERVICETAX_DATE",(String)getAttribute("SERVICETAX_DATE").getObj());
                rsOSupplier.updateString("CST_NO",(String)getAttribute("CST_NO").getObj());
                rsOSupplier.updateString("CST_DATE",(String)getAttribute("CST_DATE").getObj());
                rsOSupplier.updateString("GST_NO",(String)getAttribute("GST_NO").getObj());
                rsOSupplier.updateString("GST_DATE",(String)getAttribute("GST_DATE").getObj());
                rsOSupplier.updateString("GSTIN_NO",(String)getAttribute("GSTIN_NO").getObj());
                rsOSupplier.updateString("GSTIN_DATE",(String)getAttribute("GSTIN_DATE").getObj());
                rsOSupplier.updateString("ECC_NO",(String)getAttribute("ECC_NO").getObj());
                rsOSupplier.updateString("ECC_DATE",(String)getAttribute("ECC_DATE").getObj());
                rsOSupplier.updateString("SSIREG_NO",(String)getAttribute("SSIREG_NO").getObj());
                rsOSupplier.updateString("SSIREG_DATE",(String)getAttribute("SSIREG_DATE").getObj());
                rsOSupplier.updateString("ESIREG_NO",(String)getAttribute("ESIREG_NO").getObj());
                rsOSupplier.updateString("ESIREG_DATE",(String)getAttribute("ESIREG_DATE").getObj());
                
                rsOSupplier.updateString("PAN_NO",getAttribute("PAN_NO").getString());
                rsOSupplier.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());
rsOSupplier.updateBoolean("MSME",(boolean)getAttribute("MSME").getBool());
                rsOSupplier.updateString("MSME_UAN",getAttribute("MSME_UAN").getString());
                rsOSupplier.updateString("MSME_DIC_NO",getAttribute("MSME_DIC_NO").getString());                

                rsOSupplier.updateLong("FORM",(long)getAttribute("FORM").getVal());
                rsOSupplier.updateString("CONTACT_PERSON_1",(String)getAttribute("CONTACT_PERSON_1").getObj());
                rsOSupplier.updateString("CONTACT_PERSON_2",(String)getAttribute("CONTACT_PERSON_2").getObj());
                rsOSupplier.updateBoolean("ONETIME_SUPPLIER",(boolean)getAttribute("ONETIME_SUPPLIER").getBool());
                //rsOSupplier.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
                if(getAttribute("FROM_DATE_REG").getObj().equals(""))
            {
                rsOSupplier.updateString("FROM_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
            }
                //rsOSupplier.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
                if(getAttribute("TO_DATE_REG").getObj().equals(""))
            {
                rsOSupplier.updateString("TO_DATE_REG",null);
            }
            else
            {
                rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
            }
                rsOSupplier.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
                rsOSupplier.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
                rsOSupplier.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
                rsOSupplier.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
                rsOSupplier.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
                rsOSupplier.updateInt("PAYMENT_TERM_CODE",(int)getAttribute("PAYMENT_TERM_CODE").getVal());
                rsOSupplier.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
                rsOSupplier.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//                if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
//            {
//                rsOSupplier.updateString("LAST_TRANS_DATE",null);
//            }
//            else
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            }
                
                rsOSupplier.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
                
                rsOSupplier.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsOSupplier.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsOSupplier.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsOSupplier.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsOSupplier.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
                rsOSupplier.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
                rsOSupplier.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
                rsOSupplier.updateBoolean("CANCELLED",false);
                rsOSupplier.updateBoolean("CHANGED",true);
                rsOSupplier.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOSupplier.updateBoolean("APPROVED",true);
                rsOSupplier.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOSupplier.updateBoolean("REJECTED",ObjSupp.getAttribute("REJECTED").getBool());
                rsOSupplier.updateString("REJECTED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsOSupplier.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
                rsOSupplier.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsOSupplier.insertRow();
                
                
                stOItemCategory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOItemCategory=stOItemCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY WHERE SUPP_ID=1");
                rsOItemCategory.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colSupplierItems.size();i++) {
                    clsSupplierItem ObjItems=(clsSupplierItem) colSupplierItems.get(Integer.toString(i));
                    rsOItemCategory.moveToInsertRow();
                    rsOItemCategory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsOItemCategory.updateLong("SUPP_ID",SuppID);
                    rsOItemCategory.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                    rsOItemCategory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsOItemCategory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsOItemCategory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsOItemCategory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsOItemCategory.updateBoolean("CHANGED",true);
                    rsOItemCategory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsOItemCategory.insertRow();
                }
                
                
                stOTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOTerms=stOTerms.executeQuery("SELECT * FROM D_COM_SUPP_TERMS WHERE SUPP_ID=1");
                rsOTerms.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colSuppTerms.size();i++) {
                    clsSuppTerms ObjItems=(clsSuppTerms) colSuppTerms.get(Integer.toString(i));
                    rsOTerms.moveToInsertRow();
                    rsOTerms.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsOTerms.updateLong("SUPP_ID",SuppID);
                    rsOTerms.updateInt("SR_NO",i);
                    rsOTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                    rsOTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                    rsOTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                    rsOTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                    rsOTerms.updateBoolean("CHANGED",true);
                    rsOTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsOTerms.insertRow();
                }
                
                
                stOChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsOChilds=stOChilds.executeQuery("SELECT * FROM D_COM_SUPP_CHILDS WHERE SUPP_ID=1");
                rsOChilds.first();
                
                //Now Insert records into detail table
                for(int i=1;i<=colSuppChilds.size();i++) {
                    clsSuppChilds ObjItems=(clsSuppChilds) colSuppChilds.get(Integer.toString(i));
                    rsOChilds.moveToInsertRow();
                    rsOChilds.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsOChilds.updateLong("SUPP_ID",SuppID);
                    rsOChilds.updateInt("SR_NO",i);
                    rsOChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                    rsOChilds.updateBoolean("CHANGED",true);
                    rsOChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsOChilds.insertRow();
                }
                
                //Set older data to current
                setAttribute("SUPP_NAME",(String)ObjSupp.getAttribute("SUPP_NAME").getObj());
                setAttribute("ATTN",(String)ObjSupp.getAttribute("ATTN").getObj());
                setAttribute("ADD1",(String)ObjSupp.getAttribute("ADD1").getObj());
                setAttribute("ADD2",(String)ObjSupp.getAttribute("ADD2").getObj());
                setAttribute("ADD3",(String)ObjSupp.getAttribute("ADD3").getObj());
                setAttribute("CITY",(String)ObjSupp.getAttribute("CITY").getObj());
                setAttribute("PROPOSED_ITEM",(String)ObjSupp.getAttribute("PROPOSED_ITEM").getObj());
                setAttribute("STATE",(String)ObjSupp.getAttribute("STATE").getObj());
                setAttribute("COUNTRY",(String)ObjSupp.getAttribute("COUNTRY").getObj());
//                setAttribute("STATE_CODE",(String)ObjSupp.getAttribute("STATE_CODE").getObj());
//                setAttribute("STATE_GST_CODE",(String)ObjSupp.getAttribute("STATE_GST_CODE").getObj());
                setAttribute("PINCODE",(String)ObjSupp.getAttribute("PINCODE").getObj());
                setAttribute("PLACE_OF_SUPPLY",(String)ObjSupp.getAttribute("PLACE_OF_SUPPLY").getObj());
                setAttribute("PHONE_O",(String)ObjSupp.getAttribute("PHONE_O").getObj());
                setAttribute("PHONE_RES",(String)ObjSupp.getAttribute("PHONE_RES").getObj());
                setAttribute("FAX_NO",(String)ObjSupp.getAttribute("FAX_NO").getObj());
                setAttribute("MOBILE_NO",(String)ObjSupp.getAttribute("MOBILE_NO").getObj());
                setAttribute("EMAIL_ADD",(String)ObjSupp.getAttribute("EMAIL_ADD").getObj());
                setAttribute("URL",(String)ObjSupp.getAttribute("URL").getObj());
                setAttribute("WEB_SITE",(String)ObjSupp.getAttribute("WEB_SITE").getObj());
                setAttribute("REGISTERATION",(String)ObjSupp.getAttribute("REGISTERATION").getObj());
                setAttribute("SERVICETAX_NO",(String)ObjSupp.getAttribute("SERVICETAX_NO").getObj());
                setAttribute("SERVICETAX_DATE",(String)ObjSupp.getAttribute("SERVICETAX_DATE").getObj());
                setAttribute("CST_NO",(String)ObjSupp.getAttribute("CST_NO").getObj());
                setAttribute("CST_DATE",(String)ObjSupp.getAttribute("CST_DATE").getObj());
                setAttribute("GST_NO",(String)ObjSupp.getAttribute("GST_NO").getObj());
                setAttribute("GST_DATE",(String)ObjSupp.getAttribute("GST_DATE").getObj());
                setAttribute("GSTIN_NO",(String)ObjSupp.getAttribute("GSTIN_NO").getObj());
                setAttribute("GSTIN_DATE",(String)ObjSupp.getAttribute("GSTIN_DATE").getObj());
                setAttribute("ECC_NO",(String)ObjSupp.getAttribute("ECC_NO").getObj());
                setAttribute("ECC_DATE",(String)ObjSupp.getAttribute("ECC_DATE").getObj());
                setAttribute("SSIREG_NO",(String)ObjSupp.getAttribute("SSIREG_NO").getObj());
                setAttribute("SSIREG_DATE",(String)ObjSupp.getAttribute("SSIREG_DATE").getObj());
                setAttribute("ESIREG_NO",(String)ObjSupp.getAttribute("ESIREG_NO").getObj());
                setAttribute("ESIREG_DATE",(String)ObjSupp.getAttribute("ESIREG_DATE").getObj());
                
                setAttribute("PAN_NO",ObjSupp.getAttribute("PAN_NO").getString());
                setAttribute("PAN_DATE",ObjSupp.getAttribute("PAN_DATE").getString());
setAttribute("MSME",ObjSupp.getAttribute("MSME").getBool());
                setAttribute("MSME_UAN",ObjSupp.getAttribute("MSME_UAN").getString());
                setAttribute("MSME_DIC_NO",ObjSupp.getAttribute("MSME_DIC_NO").getString());               

                setAttribute("FORM",(long)ObjSupp.getAttribute("FORM").getVal());
                setAttribute("ST35_REGISTERED",ObjSupp.getAttribute("ST35_REGISTERED").getBool());
                setAttribute("SSIREG",ObjSupp.getAttribute("SSIREG").getBool());
                setAttribute("CONTACT_PERSON_1",(String)ObjSupp.getAttribute("CONTACT_PERSON_1").getObj());
                setAttribute("CONTACT_PERSON_2",(String)ObjSupp.getAttribute("CONTACT_PERSON_2").getObj());
                setAttribute("ONETIME_SUPPLIER",ObjSupp.getAttribute("ONETIME_SUPPLIER").getBool());
                setAttribute("FROM_DATE_REG",(String)ObjSupp.getAttribute("FROM_DATE_REG").getObj());
                setAttribute("TO_DATE_REG",(String)ObjSupp.getAttribute("TO_DATE_REG").getObj());
                setAttribute("BLOCKED",(String)ObjSupp.getAttribute("BLOCKED").getObj());
                setAttribute("SLOW_MOVING",ObjSupp.getAttribute("SLOW_MOVING").getBool());
                setAttribute("PAYMENT_DAYS",(int)ObjSupp.getAttribute("PAYMENT_DAYS").getVal());
                setAttribute("PAYMENT_TERM_CODE",(int)ObjSupp.getAttribute("PAYMENT_TERM_CODE").getVal());
                setAttribute("LAST_PO_NO",(String)ObjSupp.getAttribute("LAST_PO_NO").getObj());
                setAttribute("LAST_TRANS_DATE",(String)ObjSupp.getAttribute("LAST_TRANS_DATE").getObj());
                setAttribute("BANK_ID",(long)ObjSupp.getAttribute("BANK_ID").getVal());
                setAttribute("BANK_NAME",(String)ObjSupp.getAttribute("BANK_NAME").getObj());
                setAttribute("CREATED_BY",(int)ObjSupp.getAttribute("CREATED_BY").getVal());
                setAttribute("CREATED_DATE",(String)ObjSupp.getAttribute("CREATED_DATE").getObj());
                setAttribute("MODIFIED_BY",(int)ObjSupp.getAttribute("MODIFIED_BY").getVal());
                setAttribute("MODIFIED_DATE",(String)ObjSupp.getAttribute("MODIFIED_DATE").getObj());
                //setAttribute("HIERARCHY_ID",(long)ObjSupp.getAttribute("HIERARCHY_ID").getVal());
                setAttribute("APPROVED",ObjSupp.getAttribute("APPROVED").getBool());
                setAttribute("APPROVED_DATE",(String)ObjSupp.getAttribute("APPROVED_DATE").getObj());
                setAttribute("REJECTED",ObjSupp.getAttribute("REJECTED").getBool());
                setAttribute("REJECTED_DATE",(String)ObjSupp.getAttribute("REJECTED_DATE").getObj());
                setAttribute("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
                setAttribute("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
                
                setAttribute("DISTANCE_KM",(int)getAttribute("DISTANCE_KM").getVal());
                
                //Now Populate the collection
                //first clear the collection
                colSupplierItems.clear();
                
                for(int i=1;i<=ObjSupp.colSupplierItems.size();i++) {
                    clsSupplierItem ObjItem=(clsSupplierItem)ObjSupp.colSupplierItems.get(Integer.toString(i));
                    clsSupplierItem NewItem=new clsSupplierItem();
                    
                    NewItem.setAttribute("COMPANY_ID",(int)ObjItem.getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("AMEND_ID",(int)ObjItem.getAttribute("AMEND_ID").getVal());
                    NewItem.setAttribute("CATEGORY_ID",(int)ObjItem.getAttribute("CATEGORY_ID").getVal());
                    NewItem.setAttribute("CREATED_BY",(int)ObjItem.getAttribute("CREATED_BY").getVal());
                    NewItem.setAttribute("CREATED_DATE",(String)ObjItem.getAttribute("CREATED_DATE").getObj());
                    NewItem.setAttribute("MODIFIED_BY",(int)ObjItem.getAttribute("MODIFIED_BY").getVal());
                    NewItem.setAttribute("MODIFIED_DATE",(String)ObjItem.getAttribute("MODIFIED_DATE").getObj());
                    
                    colSupplierItems.put(Long.toString(i),NewItem);
                }
                
                colSuppTerms.clear();
                for(int i=1;i<=ObjSupp.colSuppTerms.size();i++) {
                    clsSuppTerms ObjItem=(clsSuppTerms)ObjSupp.colSuppTerms.get(Integer.toString(i));
                    clsSuppTerms NewItem=new clsSuppTerms();
                    
                    NewItem.setAttribute("COMPANY_ID",ObjItem.getAttribute("COMPANY_ID").getInt());
                    NewItem.setAttribute("AMEND_ID",ObjItem.getAttribute("SUPP_ID").getInt());
                    NewItem.setAttribute("SR_NO",ObjItem.getAttribute("SR_NO").getInt());
                    NewItem.setAttribute("TERM_TYPE",ObjItem.getAttribute("TERM_TYPE").getString());
                    NewItem.setAttribute("TERM_CODE",ObjItem.getAttribute("TERM_CODE").getInt());
                    NewItem.setAttribute("TERM_DAYS",ObjItem.getAttribute("TERM_DAYS").getInt());
                    NewItem.setAttribute("TERM_DESC",ObjItem.getAttribute("TERM_DESC").getString());
                    
                    colSuppTerms.put(Long.toString(i),NewItem);
                }
                
                
                colSuppChilds.clear();
                for(int i=1;i<=ObjSupp.colSuppChilds.size();i++) {
                    clsSuppChilds ObjItem=(clsSuppChilds) ObjSupp.colSuppChilds.get(Integer.toString(i));
                    clsSuppChilds NewItem=new clsSuppChilds();
                    
                    NewItem.setAttribute("COMPANY_ID",(int)ObjItem.getAttribute("COMPANY_ID").getVal());
                    NewItem.setAttribute("AMEND_ID",(int)ObjItem.getAttribute("SUPP_ID").getVal());
                    NewItem.setAttribute("SR_NO",(int)ObjItem.getAttribute("SR_NO").getVal());
                    NewItem.setAttribute("CHILD_SUPP_ID",(int)ObjItem.getAttribute("CHILD_SUPP_ID").getVal());
                    
                    colSuppChilds.put(Long.toString(i),NewItem);
                }
                
                /*
                //==========Sync Party data with other modules =============//
                if(AStatus.equals("F")) {
                    if(EITLERPGLOBAL.gCompanyID==2) {
                        String newURL=clsFinYear.getDBURL(3,EITLERPGLOBAL.FinYearFrom);
                        AddPartyToExternalDB(getAttribute("SUPPLIER_CODE").getString(), newURL);
                    }
                    AddPartyToUnregMaster(getAttribute("SUPPLIER_CODE").getString());
                    AddPartyToFinance(getAttribute("SUPPLIER_CODE").getString());
                }
                //==========================================================//
                */
                
                
            }
            
            
            
            rsResultSet.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            rsResultSet.updateString("AMEND_DATE",(String) getAttribute("AMEND_DATE").getObj());
            rsResultSet.updateString("AMEND_REASON",(String) getAttribute("AMEND_REASON").getObj());
            rsResultSet.updateString("DUMMY_SUPPLIER_CODE",(String) getAttribute("DUMMY_SUPPLIER_CODE").getObj());
            rsResultSet.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsResultSet.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsResultSet.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsResultSet.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
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
            rsResultSet.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
//            if(getAttribute("FROM_DATE_REG").getObj().equals(""))
//            {
//                rsResultSet.updateString("FROM_DATE_REG",null);
//            }
//            else
//            {
//                rsResultSet.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
//            }
            
           rsResultSet.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
//            if(getAttribute("TO_DATE_REG").getObj().equals(""))
//            {
//                rsResultSet.updateString("TO_DATE_REG",null);
//            }
//            else
//            {
//                rsResultSet.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
//            }
            rsResultSet.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
            rsResultSet.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
            rsResultSet.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
            rsResultSet.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
            rsResultSet.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
            rsResultSet.updateInt("PAYMENT_TERM_CODE",(int) getAttribute("PAYMENT_TERM_CODE").getVal());
            //rsResultSet.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
            //rsResultSet.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
            
            rsResultSet.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsResultSet.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_COM_SUPP_AMEND_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND AMEND_ID="+(int)getAttribute("AMEND_ID").getVal());
            RevNo++;
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID", (long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("AMEND_ID", (long)getAttribute("AMEND_ID").getVal());
            rsHistory.updateString("AMEND_DATE", (String)getAttribute("AMEND_DATE").getObj());
            rsHistory.updateString("AMEND_REASON", (String)getAttribute("AMEND_REASON").getObj());
            rsHistory.updateString("SUPPLIER_CODE",(String) getAttribute("SUPPLIER_CODE").getObj());
            rsHistory.updateString("DUMMY_SUPPLIER_CODE",(String) getAttribute("DUMMY_SUPPLIER_CODE").getObj());
            rsHistory.updateString("SUPP_NAME",(String) getAttribute("SUPP_NAME").getObj());
            rsHistory.updateString("ATTN",(String) getAttribute("ATTN").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PROPOSED_ITEM",(String)getAttribute("PROPOSED_ITEM").getObj());
            rsHistory.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsHistory.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsHistory.updateString("STATE_CODE",(String)getAttribute("STATE_CODE").getObj());
            rsHistory.updateString("STATE_GST_CODE",(String)getAttribute("STATE_GST_CODE").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("PLACE_OF_SUPPLY",(String)getAttribute("PLACE_OF_SUPPLY").getObj());
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
            rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
//            if(getAttribute("FROM_DATE_REG").getObj().equals(""))
//            {
//                rsHistory.updateString("FROM_DATE_REG",null);
//            }
//            else
//            {
//                rsHistory.updateString("FROM_DATE_REG",(String)getAttribute("FROM_DATE_REG").getObj());
//            }
           rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
//            if(getAttribute("TO_DATE_REG").getObj().equals(""))
//            {
//                rsHistory.updateString("TO_DATE_REG",null);
//            }
//            else
//            {
//                rsHistory.updateString("TO_DATE_REG",(String)getAttribute("TO_DATE_REG").getObj());
//            }
            rsHistory.updateString("BLOCKED",(String)getAttribute("BLOCKED").getObj());
            rsHistory.updateBoolean("SLOW_MOVING",(boolean)getAttribute("SLOW_MOVING").getBool());
            rsHistory.updateBoolean("ST35_REGISTERED",(boolean)getAttribute("ST35_REGISTERED").getBool());
            rsHistory.updateBoolean("SSIREG",(boolean)getAttribute("SSIREG").getBool());
            rsHistory.updateLong("PAYMENT_DAYS",(long) getAttribute("PAYMENT_DAYS").getVal());
            rsHistory.updateInt("PAYMENT_TERM_CODE",(int) getAttribute("PAYMENT_TERM_CODE").getVal());
            rsHistory.updateString("LAST_PO_NO",(String)getAttribute("LAST_PO_NO").getObj());
            rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            if(getAttribute("LAST_TRANS_DATE").getObj().equals(""))
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",null);
//            }
//            else
//            {
//                rsHistory.updateString("LAST_TRANS_DATE",(String)getAttribute("LAST_TRANS_DATE").getObj());
//            }
            
            rsHistory.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateLong("BANK_ID",(long) getAttribute("BANK_ID").getVal());
            rsHistory.updateString("BANK_NAME",(String)getAttribute("BANK_NAME").getObj());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("PARENT_SUPP_ID",(int)getAttribute("PARENT_SUPP_ID").getVal());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mSuppID=Long.toString((long)getAttribute("AMEND_ID").getVal());
            
            data.Execute("DELETE FROM D_COM_SUPP_AMEND_ITEMCATEGORY WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSuppID);
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_ITEMCATEGORY");
            long nSuppID=(long)getAttribute("AMEND_ID").getVal();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colSupplierItems.size();i++) {
                clsSupplierItem ObjItems=(clsSupplierItem) colSupplierItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateLong("AMEND_ID",nSuppID);
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
                rsHCategory.updateLong("AMEND_ID",nSuppID);
                rsHCategory.updateLong("CATEGORY_ID",(long)ObjItems.getAttribute("CATEGORY_ID").getVal());
                rsHCategory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHCategory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHCategory.updateLong("MODIFIED_BY",(long)ObjItems.getAttribute("MODIFIED_BY").getVal());
                rsHCategory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHCategory.updateBoolean("CHANGED",true);
                rsHCategory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHCategory.insertRow();
            }
            
            data.Execute("DELETE FROM D_COM_SUPP_AMEND_TERMS WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSuppID);
            
            ResultSet rsTerms;
            Statement stTerms;
            
            stTerms=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTerms=stTerms.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_TERMS");
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppTerms.size();i++) {
                clsSuppTerms ObjItems=(clsSuppTerms) colSuppTerms.get(Integer.toString(i));
                rsTerms.moveToInsertRow();
                rsTerms.updateLong("COMPANY_ID",nCompanyID);
                rsTerms.updateLong("AMEND_ID",nSuppID);
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
                rsHTerms.updateLong("AMEND_ID",nSuppID);
                rsHTerms.updateInt("SR_NO",i);
                rsHTerms.updateString("TERM_TYPE",ObjItems.getAttribute("TERM_TYPE").getString());
                rsHTerms.updateInt("TERM_CODE",ObjItems.getAttribute("TERM_CODE").getInt());
                rsHTerms.updateInt("TERM_DAYS",ObjItems.getAttribute("TERM_DAYS").getInt());
                rsHTerms.updateString("TERM_DESC",ObjItems.getAttribute("TERM_DESC").getString());
                rsHTerms.updateBoolean("CHANGED",true);
                rsHTerms.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHTerms.insertRow();
            }
            
            
            
            
            data.Execute("DELETE FROM D_COM_SUPP_AMEND_CHILDS WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSuppID);
            
            ResultSet rsChilds;
            Statement stChilds;
            
            stChilds=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsChilds=stChilds.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_CHILDS");
            
            //Now Insert records into detail table
            for(int i=1;i<=colSuppChilds.size();i++) {
                clsSuppChilds ObjItems=(clsSuppChilds) colSuppChilds.get(Integer.toString(i));
                rsChilds.moveToInsertRow();
                rsChilds.updateLong("COMPANY_ID",nCompanyID);
                rsChilds.updateLong("AMEND_ID",nSuppID);
                rsChilds.updateInt("SR_NO",i);
                rsChilds.updateInt("CHILD_SUPP_ID",(int)ObjItems.getAttribute("CHILD_SUPP_ID").getVal());
                rsChilds.updateBoolean("CHANGED",true);
                rsChilds.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsChilds.insertRow();
                
                rsHChilds.moveToInsertRow();
                rsHChilds.updateInt("REVISION_NO",RevNo);
                rsHChilds.updateLong("COMPANY_ID",nCompanyID);
                rsHChilds.updateLong("AMEND_ID",nSuppID);
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
            ObjFlow.ModuleID=50; //SUPPLIER AMEND MASTER MODULE ID
            ObjFlow.DocNo=Long.toString((long)getAttribute("AMEND_ID").getVal());
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_COM_SUPP_AMEND_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="AMEND_ID";
            
            
            //==== Handling Rejected Documents ==========//
            AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
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
                data.Execute("UPDATE D_COM_SUPP_AMEND_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND AMEND_ID="+ObjFlow.DocNo+"");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=50 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            } else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            //==========Sync Party data with other modules =============//
                if(AStatus.equals("F")) {
                    if(EITLERPGLOBAL.gCompanyID==2) {
                        String newURL=clsFinYear.getDBURL(3,EITLERPGLOBAL.FinYearFrom);
                        AddPartyToExternalDB(getAttribute("SUPPLIER_CODE").getString(), newURL);
                    }
                    AddPartyToUnregMaster(getAttribute("SUPPLIER_CODE").getString());
                    AddPartyToFinance(getAttribute("SUPPLIER_CODE").getString());
                }
                //==========================================================//
                
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND AMEND_ID="+pSuppCode+" AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=50 AND DOC_NO='"+pSuppCode+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND AMEND_ID="+pSuppCode+" AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=50 AND DOC_NO='"+pSuppCode+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
            
            String strQry = "DELETE FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID=" + lCompanyID +" AND SUPPLIER_CODE='"+sSuppCode+"'";
            data.Execute(strQry);
            strQry = "DELETE FROM D_COM_SUPP_AMEND_ITEMCATEGORY WHERE COMPANY_ID=" + lCompanyID +" AND SUPPLIER_CODE='"+sSuppCode+"'";
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
        clsSupplierAmend ObjSupplier = new clsSupplierAmend();
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
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSupplierAmend ObjSupplier =new clsSupplierAmend();
                ObjSupplier.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjSupplier.setAttribute("AMEND_ID",rsTmp.getLong("AMEND_ID"));
                ObjSupplier.setAttribute("AMEND_DATE",rsTmp.getString("AMEND_DATE"));
                ObjSupplier.setAttribute("AMEND_REASON",rsTmp.getString("AMEND_REASON"));
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
                ObjSupplier.setAttribute("MSME",rsTmp.getBoolean("MSME"));
                ObjSupplier.setAttribute("MSME_UAN",rsTmp.getString("MSME_UAN"));
                ObjSupplier.setAttribute("MSME_DIC_NO",rsTmp.getString("MSME_DIC_NO"));
                ObjSupplier.setAttribute("HIERARCHY_ID",rsTmp.getLong("HIERARCHY_ID"));
                ObjSupplier.colSupplierItems.clear();
                
                String mCompanyID=Long.toString( (long) ObjSupplier.getAttribute("COMPANY_ID").getVal());
                long mSupp=(long)ObjSupplier.getAttribute("AMEND_ID").getVal();
                
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_ITEMCATEGORY WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSupp+" ORDER BY CATEGORY_ID");
                
                Counter2=0;
                while(rsTmp2.next()) {
                    Counter2=Counter2+1;
                    clsSupplierItem ObjItems=new clsSupplierItem();
                    
                    ObjItems.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjItems.setAttribute("AMEND_ID",rsTmp2.getLong("AMEND_ID"));
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
            String strSql = "SELECT * FROM D_COM_SUPP_AMEND_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
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
            setAttribute("AMEND_ID",rsResultSet.getLong("AMEND_ID"));
            setAttribute("AMEND_DATE",rsResultSet.getString("AMEND_DATE"));
            setAttribute("AMEND_REASON",rsResultSet.getString("AMEND_REASON"));
            setAttribute("SUPPLIER_CODE",rsResultSet.getString("SUPPLIER_CODE"));
            setAttribute("DUMMY_SUPPLIER_CODE",rsResultSet.getString("DUMMY_SUPPLIER_CODE"));
            setAttribute("SUPP_NAME",rsResultSet.getString("SUPP_NAME"));
            setAttribute("ATTN",rsResultSet.getString("ATTN"));
            setAttribute("ADD1",rsResultSet.getString("ADD1"));
            setAttribute("ADD2",rsResultSet.getString("ADD2"));
            setAttribute("ADD3",rsResultSet.getString("ADD3"));
            setAttribute("CITY",rsResultSet.getString("CITY"));
            setAttribute("PROPOSED_ITEM",rsResultSet.getString("PROPOSED_ITEM"));
            setAttribute("STATE",rsResultSet.getString("STATE"));
            setAttribute("COUNTRY",rsResultSet.getString("COUNTRY"));
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
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("MAIN_ACCOUNT_CODE",rsResultSet.getString("MAIN_ACCOUNT_CODE"));
            
            setAttribute("DISTANCE_KM",rsResultSet.getInt("DISTANCE_KM"));
            
            //Now Populate the collection
            //first clear the collection
            colSupplierItems.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            long mSupp=(long)getAttribute("AMEND_ID").getVal();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_ITEMCATEGORY_H WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSupp + " AND REVISION_NO="+RevNo+" ORDER BY CATEGORY_ID");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_ITEMCATEGORY WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSupp + " ORDER BY CATEGORY_ID");
            }
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSupplierItem ObjItems=new clsSupplierItem();
                
                ObjItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItems.setAttribute("AMEND_ID",rsTmp.getLong("AMEND_ID"));
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
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_TERMS_H WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSupp + " AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_TERMS WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSupp + " ");
            }
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSuppTerms ObjItems=new clsSuppTerms();
                
                ObjItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItems.setAttribute("AMEND_ID",rsTmp.getLong("AMEND_ID"));
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
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_CHILDS_H WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSupp + " AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_CHILDS WHERE COMPANY_ID="+mCompanyID+" AND AMEND_ID="+mSupp + " ");
            }
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSuppChilds ObjItems=new clsSuppChilds();
                
                ObjItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItems.setAttribute("AMEND_ID",rsTmp.getLong("AMEND_ID"));
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
    
    
    public boolean ShowHistory(int pCompanyID,int pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND AMEND_ID="+pDocNo+" ORDER BY REVISION_NO");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_SUPP_AMEND_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND AMEND_ID="+pDocNo+" ORDER BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsSupplierAmend ObjSupp=new clsSupplierAmend();
                
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
                //strSQL="SELECT D_COM_SUPP_AMEND_MASTER.AMEND_ID,D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE,D_COM_SUPP_AMEND_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_AMEND_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=50 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
                strSQL="SELECT D_COM_SUPP_AMEND_MASTER.AMEND_ID,D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE,D_COM_SUPP_AMEND_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_AMEND_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=50 AND D_COM_SUPP_AMEND_MASTER.CANCELLED=0 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                //strSQL="SELECT D_COM_SUPP_AMEND_MASTER.AMEND_ID,D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE,D_COM_SUPP_AMEND_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_AMEND_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=50 ORDER BY D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE";
                strSQL="SELECT D_COM_SUPP_AMEND_MASTER.AMEND_ID,D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE,D_COM_SUPP_AMEND_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_AMEND_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=50 AND D_COM_SUPP_AMEND_MASTER.CANCELLED=0 ORDER BY D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                //strSQL="SELECT D_COM_SUPP_AMEND_MASTER.AMEND_ID,D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE,D_COM_SUPP_AMEND_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_AMEND_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=50 ORDER BY D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE";
                strSQL="SELECT D_COM_SUPP_AMEND_MASTER.AMEND_ID,D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE,D_COM_SUPP_AMEND_MASTER.SUPP_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_COM_SUPP_AMEND_MASTER,D_COM_DOC_DATA WHERE D_COM_SUPP_AMEND_MASTER.AMEND_ID=D_COM_DOC_DATA.DOC_NO AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_COM_SUPP_AMEND_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=50 AND D_COM_SUPP_AMEND_MASTER.CANCELLED=0 ORDER BY D_COM_SUPP_AMEND_MASTER.SUPPLIER_CODE";
            }
            
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSupplierAmend ObjItem=new clsSupplierAmend();
                
                //------------- Header Fields --------------------//
                ObjItem.setAttribute("AMEND_ID",rsTmp.getInt("AMEND_ID"));
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
    
    
    
    
    public static int getAmendmentNo(int pCompanyID,String pSuppCode) {
        String strSQL="";
        
        ResultSet rsTmp;
        
        int AmendNo=0;
        
        try {
            rsTmp=data.getResult("SELECT COUNT(*) AS THECOUNT FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppCode+"' AND SUPPLIER_CODE IN (SELECT SUPPLIER_CODE FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+pSuppCode+"' AND APPROVED=1)");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                AmendNo=rsTmp.getInt("THECOUNT");
            }
            
        }
        catch(Exception e) {
        }
        
        return AmendNo;
    }
    
    
    
    public static int getUnderApprovalCount(int pCompanyID,String pSuppCode) {
        String strSQL="";
        
        ResultSet rsTmp;
        
        int AmendCount=0;
        
        try {
            rsTmp=data.getResult("SELECT COUNT(*) AS THECOUNT FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID="+pCompanyID+" AND SUPPLIER_CODE='"+pSuppCode+"' AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                AmendCount=rsTmp.getInt("THECOUNT");
            }
            
        }
        catch(Exception e) {
        }
        
        return AmendCount;
    }
    
    
    
    public static HashMap getUpdationHistory(int pCompanyID,String pSuppCode) {
        String strSQL="";
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            
            rsTmp=data.getResult("SELECT AMEND_ID,AMEND_DATE,AMEND_REASON FROM D_COM_SUPP_AMEND_MASTER WHERE SUPPLIER_CODE='"+pSuppCode+"' AND APPROVED=1 ORDER BY AMEND_ID DESC");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    
                    clsSupplierAmend ObjSupp=new clsSupplierAmend();
                    
                    ObjSupp.setAttribute("AMEND_ID",rsTmp.getInt("AMEND_ID"));
                    ObjSupp.setAttribute("AMEND_DATE",rsTmp.getString("AMEND_DATE"));
                    ObjSupp.setAttribute("AMEND_REASON",rsTmp.getString("AMEND_REASON"));
                    
                    List.put(Integer.toString(List.size()+1),ObjSupp);
                    rsTmp.next();
                }
            }
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public static boolean CanCancel(int pCompanyID,String pAmendNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT AMEND_ID FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID="+pCompanyID+" AND AMEND_ID="+pAmendNo+" AND CANCELLED=0 AND APPROVED=0");
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
    
    
    public static boolean CancelAmendment(int pCompanyID,String pAmendNo) {
        
        ResultSet rsTmp=null;
        ResultSet rsIndent=null;
        ResultSet rsSupp=null;
        
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pAmendNo)) {
                
                boolean ApprovedAmendment=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_COM_SUPP_AMEND_MASTER WHERE COMPANY_ID="+pCompanyID+" AND AMEND_ID="+pAmendNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedAmendment=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedAmendment) {
                    
                }
                else {
                    int ModuleID=50;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pAmendNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_COM_SUPP_AMEND_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND AMEND_ID="+pAmendNo);
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    
    public void AddPartyToFinance(String SupplierCode) {
        //========== Add Party to Finance Party Master ===============//
        
        clsSupplier objSupplier=(clsSupplier) new clsSupplier().getObject(2,SupplierCode);
        
        //if(objSupplier.Filter("WHERE SUPPLIER_CODE='"+SupplierCode+"'")) {
        try {
            Connection FinConn;
            Statement stFinParty;
            ResultSet rsFinParty;
            
            FinConn=data.getConn(FinanceGlobal.FinURL);
            stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
            
            String MainCodes[] = getAttribute("MAIN_ACCOUNT_CODE").getString().split(",");
            String MainAccountCode = "";
            for(int i=0;i<MainCodes.length;i++) {
                MainAccountCode = MainCodes[i];
                if(data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+SupplierCode+"' " ,FinanceGlobal.FinURL)) {
                    data.Execute("DELETE FROM D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='"+MainAccountCode+"' AND PARTY_CODE='"+SupplierCode+"' " ,FinanceGlobal.FinURL);
                }
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
                //---------------------------------------------------
                
                String add = objSupplier.getAttribute("ADD1").getString() + " " + objSupplier.getAttribute("ADD2").getString() + " " + objSupplier.getAttribute("ADD3").getString();
                rsFinParty.updateString("ADDRESS",add);
                rsFinParty.updateString("CITY",objSupplier.getAttribute("CITY").getString());
                rsFinParty.updateString("PINCODE",objSupplier.getAttribute("PINCODE").getString());
                rsFinParty.updateString("STATE",objSupplier.getAttribute("STATE").getString());
                rsFinParty.updateString("COUNTRY",objSupplier.getAttribute("COUNTRY").getString());
                rsFinParty.updateString("PHONE",objSupplier.getAttribute("PHONE_O").getString());
                rsFinParty.updateString("FAX",objSupplier.getAttribute("FAX_NO").getString());
                rsFinParty.updateString("MOBILE",objSupplier.getAttribute("MOBILE_NO").getString());
                rsFinParty.updateString("EMAIL",objSupplier.getAttribute("EMAIL_ADD").getString());
                rsFinParty.updateString("URL",objSupplier.getAttribute("URL").getString());
                
                rsFinParty.updateInt("DISTANCE_KM",objSupplier.getAttribute("DISTANCE_KM").getInt()); 
                
                rsFinParty.updateInt("APPROVED",1);
                rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateInt("REJECTED",0);
                rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                rsFinParty.updateString("REJECTED_REMARKS","");
                rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                rsFinParty.updateInt("HIERARCHY_ID",0);
                rsFinParty.updateInt("CANCELLED",0);
                rsFinParty.updateInt("BLOCKED",0);
                rsFinParty.updateString("PAN_NO","");
                rsFinParty.updateString("PAN_DATE","0000-00-00");
rsFinParty.updateBoolean("MSME", objSupplier.getAttribute("MSME").getBool());
                rsFinParty.updateString("MSME_UAN",objSupplier.getAttribute("MSME_UAN").getString());
                rsFinParty.updateString("MSME_DIC_NO",objSupplier.getAttribute("MSME_DIC_NO").getString());
                rsFinParty.updateInt("CHANGED",1);
                rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateString("CREATED_BY","System");
                rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateString("MODIFIED_BY","System");
                rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateDouble("CLOSING_BALANCE",0);
                rsFinParty.updateString("CLOSING_EFFECT","D");
                rsFinParty.insertRow();
            }
            rsFinParty.close();
            stFinParty.close();
            FinConn.close();
        }
        catch(Exception p) {
            
        }
        //============================================================//
        //}
    }
    
    
    public void AddPartyToUnregMaster(String SupplierCode) {
        try {
            clsSupplier objSupplier=new clsSupplier();
            
            if(objSupplier.Filter("WHERE SUPPLIER_CODE='"+SupplierCode+"'")) {
                clsParty ObjParty=new clsParty();
                ObjParty.LoadData(EITLERPGLOBAL.gCompanyID);
                
                //Setup the Data
                ObjParty.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                ObjParty.setAttribute("PARTY_ID",(int)objSupplier.getAttribute("SUPP_ID").getVal());
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
                
                //ObjParty.Insert();
                ObjParty.InsertAfterAmend();
                
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
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
                data.Execute("DELETE FROM D_COM_SUPP_MASTER WHERE SUPPLIER_CODE='"+SupplierCode+"'",dbURL);
                data.Execute("DELETE FROM D_COM_SUPP_MASTER_H WHERE SUPP_ID="+SuppID,dbURL);
                data.Execute("DELETE FROM D_COM_SUPP_TERMS WHERE SUPP_ID="+SuppID,dbURL);
                data.Execute("DELETE FROM D_COM_SUPP_TERMS_H WHERE SUPP_ID="+SuppID,dbURL);
                data.Execute("DELETE FROM D_COM_SUPP_ITEMCATEGORY WHERE SUPP_ID="+SuppID,dbURL);
                data.Execute("DELETE FROM D_COM_SUPP_ITEMCATEGORY_H WHERE SUPP_ID="+SuppID,dbURL);
                data.Execute("DELETE FROM D_COM_SUPP_CHILDS WHERE SUPP_ID="+SuppID,dbURL);
                data.Execute("DELETE FROM D_COM_SUPP_CHILDS_H WHERE SUPP_ID="+SuppID,dbURL);
                
                Connection objExConn=data.getConn(dbURL);
                Statement stSupplier=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsSupplier=stSupplier.executeQuery("SELECT * FROM D_COM_SUPP_MASTER LIMIT 1");
                
                Statement stHistory=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SUPP_MASTER_H LIMIT 1");
                
                rsSupplier.moveToInsertRow();
                rsSupplier.updateLong("COMPANY_ID", NewCompanyID);
                rsSupplier.updateLong("SUPP_ID", SuppID);
                rsSupplier.updateString("SUPPLIER_CODE",objSupplier.getAttribute("SUPPLIER_CODE").getString());
                String DummyCode=Long.toString(SuppID);
                rsSupplier.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
                rsSupplier.updateString("SUPP_NAME",(String)objSupplier.getAttribute("SUPP_NAME").getObj());
                rsSupplier.updateString("ATTN",(String)objSupplier.getAttribute("ATTN").getObj());
                rsSupplier.updateString("ADD1",(String)objSupplier.getAttribute("ADD1").getObj());
                rsSupplier.updateString("ADD2",(String)objSupplier.getAttribute("ADD2").getObj());
                rsSupplier.updateString("ADD3",(String)objSupplier.getAttribute("ADD3").getObj());
                rsSupplier.updateString("CITY",(String)objSupplier.getAttribute("CITY").getObj());
                rsSupplier.updateString("PROPOSED_ITEM",(String)objSupplier.getAttribute("PROPOSED_ITEM").getObj());
                rsSupplier.updateInt("STATE",(int)objSupplier.getAttribute("STATE").getVal());
                rsSupplier.updateInt("COUNTRY",(int)objSupplier.getAttribute("COUNTRY").getVal());
//                rsSupplier.updateString("STATE_CODE",(String)objSupplier.getAttribute("STATE_CODE").getObj());
//                rsSupplier.updateString("STATE_GST_CODE",(String)objSupplier.getAttribute("STATE_GST_CODE").getObj());
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
                rsSupplier.updateInt("DISTANCE_KM",(int)objSupplier.getAttribute("DISTANCE_KM").getVal());
rsSupplier.updateBoolean("MSME",(boolean)objSupplier.getAttribute("MSME").getBool());
                rsSupplier.updateString("MSME_UAN",(String)objSupplier.getAttribute("MSME_UAN").getObj());
                rsSupplier.updateString("MSME_DIC_NO",(String)objSupplier.getAttribute("MSME_DIC_NO").getObj());
                rsSupplier.insertRow();
                
                
                //========= Inserting Into History =================//
                //Get the Maximum Revision No.
                rsHistory.moveToInsertRow();
                rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
                rsHistory.updateInt("UPDATED_BY",(int)objSupplier.getAttribute("CREATED_BY").getVal());
                rsHistory.updateString("APPROVAL_STATUS",(String)objSupplier.getAttribute("APPROVAL_STATUS").getObj());
                rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHistory.updateLong("COMPANY_ID",NewCompanyID);
                rsHistory.updateLong("SUPP_ID", SuppID);
                rsHistory.updateString("SUPPLIER_CODE",(String) objSupplier.getAttribute("SUPPLIER_CODE").getObj());
                rsHistory.updateString("DUMMY_SUPPLIER_CODE",DummyCode);
                rsHistory.updateString("SUPP_NAME",(String) objSupplier.getAttribute("SUPP_NAME").getObj());
                rsHistory.updateString("ATTN",(String) objSupplier.getAttribute("ATTN").getObj());
                rsHistory.updateString("ADD1",(String)objSupplier.getAttribute("ADD1").getObj());
                rsHistory.updateString("ADD2",(String)objSupplier.getAttribute("ADD2").getObj());
                rsHistory.updateString("ADD3",(String)objSupplier.getAttribute("ADD3").getObj());
                rsHistory.updateString("CITY",(String)objSupplier.getAttribute("CITY").getObj());
                rsHistory.updateString("PROPOSED_ITEM",(String)objSupplier.getAttribute("PROPOSED_ITEM").getObj());
                rsHistory.updateInt("STATE",(int)objSupplier.getAttribute("STATE").getVal());
                rsHistory.updateInt("COUNTRY",(int)objSupplier.getAttribute("COUNTRY").getVal());
//                rsHistory.updateString("STATE_CODE",(String)objSupplier.getAttribute("STATE_CODE").getObj());
//                rsHistory.updateString("STATE_GST_CODE",(String)objSupplier.getAttribute("STATE_GST_CODE").getObj());
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
                rsHistory.updateInt("DISTANCE_KM",(int)objSupplier.getAttribute("DISTANCE_KM").getVal());
rsHistory.updateBoolean("MSME",(boolean)objSupplier.getAttribute("MSME").getBool());
                rsHistory.updateString("MSME_UAN",(String)objSupplier.getAttribute("MSME_UAN").getObj());
                rsHistory.updateString("MSME_DIC_NO",(String)objSupplier.getAttribute("MSME_DIC_NO").getObj());
                rsHistory.insertRow();
                
                Statement stSuppItemCategory=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsSuppItemCategory=stSuppItemCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY LIMIT 1");
                
                Statement stHCategory=objExConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsHCategory=stHCategory.executeQuery("SELECT * FROM D_COM_SUPP_ITEMCATEGORY_H LIMIT 1");
                
                long nCompanyID=(long)objSupplier.getAttribute("COMPANY_ID").getVal();
                long nSuppID=SuppID;
                
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
    
}

