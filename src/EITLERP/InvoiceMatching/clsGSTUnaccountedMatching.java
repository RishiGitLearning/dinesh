/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.InvoiceMatching;

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
public class clsGSTUnaccountedMatching {
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
    public clsGSTUnaccountedMatching() {
        LastError = "";
        props=new HashMap();
        
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("MIR_NO",new Variant(""));        
        props.put("MIR_DATE",new Variant(""));
        props.put("MIR_SR_NO",new Variant(0));
        props.put("UPLOADED",new Variant(false));
        props.put("UPLOADED_MONTH",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("SUB_ACCOUNT_CODE",new Variant(""));
        props.put("SUB_ACCOUNT_NAME",new Variant(""));
        props.put("GSTIN_NO",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("MAIN_ACCOUNT_NAME",new Variant(""));
        props.put("EFFECT",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        
        props.put("JV_NO",new Variant(""));        
        props.put("JV_DATE",new Variant(""));
        props.put("JV_EFFECT",new Variant(""));        
        props.put("DIFFER_AMOUNT",new Variant(0));
        props.put("GSTPORTAL_OR_JV_AMOUNT",new Variant(0));
        
        props.put("HSN_SAC_CODE", new Variant(""));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE", new Variant(""));
        props.put("BOOK_CODE", new Variant(0));
        props.put("MIR_TYPE", new Variant(0));
        props.put("LEGACY_NO",new Variant(""));
        props.put("LEGACY_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        //props.put("MODIFIED_DATE",new Variant(""));
        
         //hmListItems= new HashMap(); 
        
    }

    public boolean Insert() {
        Connection Conn;
        Statement stListItem;
        ResultSet rsListItem;        
        try {
            Conn = data.getConn();            
            stListItem=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsListItem=stListItem.executeQuery("SELECT * FROM FINANCE.D_FIN_UNACCOUNTED_GST_UPLOADED_DETAIL WHERE MIR_NO='1'");
            rsListItem.first();
            
//            if(data.IsRecordExist("SELECT MIR_NO FROM FINANCE.D_FIN_GST_UPLOADED_DETAIL WHERE MIR_NO='"+getAttribute("MIR_NO").getString()+"'",FinanceGlobal.FinURL)) {
//                LastError="Voucher No already uploaded.";
//                return false;
//            }
            
            for (int i = 1; i <= hmListItems.size(); i++) {                

                clsGSTUnaccountedMatching Obj=(clsGSTUnaccountedMatching)hmListItems.get(Integer.toString(i));
                rsListItem.moveToInsertRow();
                rsListItem.updateString("MIR_NO", (String) Obj.getAttribute("MIR_NO").getObj());
                rsListItem.updateString("MIR_DATE", EITLERPGLOBAL.formatDateDB((String) Obj.getAttribute("MIR_DATE").getObj()));
                rsListItem.updateInt("COMPANY_ID", (int)Obj.getAttribute("COMPANY_ID").getVal());
                rsListItem.updateInt("MIR_SR_NO", (int)Obj.getAttribute("MIR_SR_NO").getVal());
                rsListItem.updateInt("UPLOADED", Obj.getAttribute("UPLOADED").getInt()); 
                rsListItem.updateString("UPLOADED_MONTH", (String) Obj.getAttribute("UPLOADED_MONTH").getObj());
                rsListItem.updateString("REMARKS", (String)Obj.getAttribute("REMARKS").getObj());
                rsListItem.updateString("SUB_ACCOUNT_CODE", (String) Obj.getAttribute("SUB_ACCOUNT_CODE").getObj());
                rsListItem.updateString("SUB_ACCOUNT_NAME",(String)Obj.getAttribute("SUB_ACCOUNT_NAME").getObj());
                rsListItem.updateString("GSTIN_NO", (String)Obj.getAttribute("GSTIN_NO").getObj());
                rsListItem.updateString("MAIN_ACCOUNT_CODE", (String)Obj.getAttribute("MAIN_ACCOUNT_CODE").getObj());
                rsListItem.updateString("MAIN_ACCOUNT_NAME", (String)Obj.getAttribute("MAIN_ACCOUNT_NAME").getObj());
                rsListItem.updateString("EFFECT", Obj.getAttribute("EFFECT").getString());
                rsListItem.updateDouble("AMOUNT", Obj.getAttribute("AMOUNT").getDouble());
                rsListItem.updateDouble("GSTPORTAL_OR_JV_AMOUNT", Obj.getAttribute("GSTPORTAL_OR_JV_AMOUNT").getDouble());
                rsListItem.updateDouble("DIFFER_AMOUNT", Obj.getAttribute("DIFFER_AMOUNT").getDouble());
                rsListItem.updateString("HSN_SAC_CODE", (String)Obj.getAttribute("HSN_SAC_CODE").getObj());
                rsListItem.updateString("INVOICE_NO", (String)Obj.getAttribute("INVOICE_NO").getObj());
                rsListItem.updateString("INVOICE_DATE", EITLERPGLOBAL.formatDateDB((String)Obj.getAttribute("INVOICE_DATE").getObj()));
                rsListItem.updateInt("BOOK_CODE", (int) Obj.getAttribute("BOOK_CODE").getVal());
                rsListItem.updateInt("MIR_TYPE", (int)Obj.getAttribute("MIR_TYPE").getVal());
                rsListItem.updateString("LEGACY_NO", (String) Obj.getAttribute("LEGACY_NO").getObj());
                rsListItem.updateString("LEGACY_DATE", EITLERPGLOBAL.formatDateDB((String) Obj.getAttribute("LEGACY_DATE").getObj()));
                rsListItem.updateInt("CREATED_BY", EITLERPGLOBAL.gNewUserID);
                rsListItem.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsListItem.updateString("JV_NO", (String) Obj.getAttribute("JV_NO").getObj());
                rsListItem.updateString("JV_DATE", EITLERPGLOBAL.formatDateDB((String) Obj.getAttribute("JV_DATE").getObj()));
                rsListItem.updateString("JV_EFFECT", Obj.getAttribute("JV_EFFECT").getString());
                
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
