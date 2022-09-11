/*
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Finance.ReportsUI;

import java.util.*;
import EITLERP.Variant;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.data;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsDebtorsOutstandingItems {

    private HashMap props;    
    public boolean Ready = false;

    public HashMap colVoucherDetailDocs=new HashMap();
    
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
    
    /** Creates new clsMRItem */
    public clsDebtorsOutstandingItems() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("ENTRY_NO",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("INVOICE_TYPE",new Variant(0));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("SUB_ACCOUNT_CODE",new Variant(""));        
        props.put("PARTY_NAME", new Variant(""));
        props.put("PARTY_TYPE",new Variant(""));        
        props.put("BOOK_CODE",new Variant(""));
        props.put("CHARGE_CODE",new Variant(0));
        props.put("INVOICE_NO",new Variant(""));
        props.put("INVOICE_DATE",new Variant(""));
        props.put("LINK_NO",new Variant(""));
        props.put("VOUCHER_NO", new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        props.put("LEGACY_NO", new Variant(""));
        props.put("LEGACY_DATE",new Variant("")); 
        props.put("EFFECT",new Variant(""));
        props.put("DEBIT_AMOUNT",new Variant(0));
        props.put("CREDIT_AMOUNT",new Variant(0));
        props.put("BALE_NO",new Variant(""));
        props.put("LR_NO",new Variant(""));
        props.put("OBC_NO",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(0));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("MATCHED",new Variant(0));
        props.put("MATCHED_DATE",new Variant(""));
        props.put("CLOSING_BALANCE",new Variant(0));
    }
    
    public static int LastClosingEntry(String theDate) {
        int EntryNo = 0;
        try {
            EntryNo = data.getIntValueFromDB("SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='"+theDate+"' ORDER BY ENTRY_DATE DESC",FinanceGlobal.FinURL);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EntryNo;
    }
}
