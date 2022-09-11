/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Excise;

import EITLERP.ApprovalFlow;
import EITLERP.EITLERPGLOBAL;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Variant;
import EITLERP.clsApprovalRules;
import EITLERP.clsFirstFree;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class clsMaterialInvReceived {
    public String LastError="";    
    
    
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap hmListItems = new HashMap();
    
    private boolean HistoryView=false;
    
    //public static int ModuleID=87;
    
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
    public clsMaterialInvReceived() {
        LastError = "";
        props=new HashMap();
        
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("MIR_NO",new Variant(""));        
        props.put("MIR_DATE",new Variant(""));
        props.put("MIR_SR_NO",new Variant(0));
        props.put("RECEIVED",new Variant(false));
        props.put("SUPPLIER_ID",new Variant(""));
        props.put("SUPPLIER_NAME",new Variant(""));
        props.put("SUPPLIER_GSTIN",new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE", new Variant(""));
        
        props.put("ITEM_ID",new Variant(""));
        props.put("ITEM_DESC",new Variant(""));       
        
        
        props.put("INVOICE_VALUE",new Variant(0));
        props.put("PLACE_OF_SUPPLY",new Variant(""));
        props.put("REVERSE_CHARGE",new Variant(0));
        props.put("INVOICE_TYPE",new Variant(""));        
        props.put("HSN_SAC_CODE",new Variant(""));
                
        props.put("QUANTITY",new Variant(0));
        
        props.put("UNIT",new Variant(0));        
        props.put("RATE",new Variant(0));
        props.put("TAXABLE_VALUE",new Variant(0));
        
        
        props.put("INTEGRATED_TAX_PAID", new Variant(0));
        props.put("CENTRAL_TAX_PAID", new Variant(0));
        props.put("STATE_TAX_PAID",new Variant(0));
        props.put("CESS_PAID",new Variant(0));
        props.put("ELIBILITY_FOR_ITC",new Variant(0));
        props.put("AVAILED__ITC_INTEGRATED_TAX", new Variant(0));
        props.put("AVAILED__ITC_CENTRAL_TAX", new Variant(0));
        props.put("AVAILED__ITC_STATE_TAX",new Variant(0));
        props.put("AVAILED__ITC_CESS",new Variant(0));
        
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MIR_APPROVED_DATE",new Variant(""));
        
        
         //hmListItems= new HashMap(); 
        
    }

    public boolean Insert() {
        Connection Conn;
        Statement stListItem;
        ResultSet rsListItem;        
        try {
            Conn = data.getConn();            
            stListItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsListItem=stListItem.executeQuery("SELECT * FROM DINESHMILLS.D_MIR_INV_RECEIVED WHERE MIR_NO='1'");
            rsListItem.first();
            
//            if(data.IsRecordExist("SELECT VOUCHER_NO FROM FINANCE.D_FIN_GST_UPLOADED_DETAIL WHERE VOUCHER_NO='"+getAttribute("VOUCHER_NO").getString()+"'",FinanceGlobal.FinURL)) {
//                LastError="Voucher No already uploaded.";
//                return false;
//            }
            
            for (int i = 1; i <= hmListItems.size(); i++) {                

                clsMaterialInvReceived Obj=(clsMaterialInvReceived)hmListItems.get(Integer.toString(i));
                rsListItem.moveToInsertRow();
                rsListItem.updateString("MIR_NO", (String) Obj.getAttribute("MIR_NO").getObj());
                rsListItem.updateString("MIR_DATE", EITLERPGLOBAL.formatDateDB((String) Obj.getAttribute("MIR_DATE").getObj()));
                rsListItem.updateInt("MIR_SR_NO", (int)Obj.getAttribute("MIR_SR_NO").getVal());
                rsListItem.updateInt("COMPANY_ID", (int)Obj.getAttribute("COMPANY_ID").getVal());
                rsListItem.updateInt("RECEIVED", Obj.getAttribute("RECEIVED").getInt()); 
                rsListItem.updateString("SUPPLIER_ID", (String) Obj.getAttribute("SUPPLIER_ID").getObj());
                rsListItem.updateString("SUPPLIER_NAME", (String)Obj.getAttribute("SUPPLIER_NAME").getObj());
                rsListItem.updateString("SUPPLIER_GSTIN", (String) Obj.getAttribute("SUPPLIER_GSTIN").getObj());
                rsListItem.updateString("INVOICE_NO", (String)Obj.getAttribute("INVOICE_NO").getObj());
                rsListItem.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB((String)Obj.getAttribute("INVOICE_DATE").getObj()));                                
                rsListItem.updateString("ITEM_ID",(String)Obj.getAttribute("ITEM_ID").getObj());
                rsListItem.updateString("ITEM_DESC", (String)Obj.getAttribute("ITEM_DESC").getObj());
                rsListItem.updateDouble("INVOICE_VALUE", Obj.getAttribute("INVOICE_VALUE").getDouble());                
                rsListItem.updateString("PLACE_OF_SUPPLY", (String)Obj.getAttribute("PLACE_OF_SUPPLY").getObj());
                rsListItem.updateString("REVERSE_CHARGE", (String)Obj.getAttribute("REVERSE_CHARGE").getObj());                
                rsListItem.updateString("INVOICE_TYPE", Obj.getAttribute("INVOICE_TYPE").getString());
                rsListItem.updateString("HSN_SAC_CODE", (String)Obj.getAttribute("HSN_SAC_CODE").getObj());                
                rsListItem.updateDouble("QUANTITY", Obj.getAttribute("QUANTITY").getDouble());
                rsListItem.updateInt("UNIT", (int) Obj.getAttribute("UNIT").getVal());
                rsListItem.updateDouble("RATE", Obj.getAttribute("RATE").getDouble());
                rsListItem.updateDouble("TAXABLE_VALUE", Obj.getAttribute("TAXABLE_VALUE").getDouble());
                rsListItem.updateDouble("INTEGRATED_TAX_PAID", Obj.getAttribute("INTEGRATED_TAX_PAID").getDouble());
                rsListItem.updateDouble("CENTRAL_TAX_PAID", Obj.getAttribute("CENTRAL_TAX_PAID").getDouble());
                rsListItem.updateDouble("STATE_TAX_PAID", Obj.getAttribute("STATE_TAX_PAID").getDouble());
                rsListItem.updateDouble("CESS_PAID", Obj.getAttribute("CESS_PAID").getDouble());
                rsListItem.updateDouble("ELIGIBILITY_FOR_ITC", Obj.getAttribute("ELIGIBILITY_FOR_ITC").getDouble());
                rsListItem.updateDouble("AVAILED_ITC_INTEGRATED_TAX", Obj.getAttribute("AVAILED_ITC_INTEGRATED_TAX").getDouble());
                rsListItem.updateDouble("AVAILED_ITC_CENTRAL_TAX", Obj.getAttribute("AVAILED_ITC_CENTRAL_TAX").getDouble());
                rsListItem.updateDouble("AVAILED_ITC_STATE_TAX", Obj.getAttribute("AVAILED_ITC_STATE_TAX").getDouble());
                rsListItem.updateDouble("AVAILED_ITC_CESS", Obj.getAttribute("AVAILED_ITC_CESS").getDouble());
                
                rsListItem.updateString("MIR_APPROVED_DATE", EITLERPGLOBAL.formatDateDB((String)Obj.getAttribute("MIR_APPROVED_DATE").getObj()));
                rsListItem.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
                rsListItem.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                //rsListItem.updateString("JV_NO", (String) Obj.getAttribute("JV_NO").getObj());
                //rsListItem.updateString("JV_DATE", EITLERPGLOBAL.formatDateDB((String) Obj.getAttribute("JV_DATE").getObj()));
                //rsListItem.updateString("JV_EFFECT", Obj.getAttribute("JV_EFFECT").getString());
                
                rsListItem.insertRow();
            }
                                    
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError= e.getMessage();
            return false;
        }
    }
    

    
}
