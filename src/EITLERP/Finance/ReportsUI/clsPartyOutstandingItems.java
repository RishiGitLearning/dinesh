/*
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Finance.ReportsUI;


import java.util.*;
import EITLERP.*;
import EITLERP.Finance.FinanceGlobal;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsPartyOutstandingItems {

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
    public clsPartyOutstandingItems() {
        props=new HashMap();    
        
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("PARTY_CODE",new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("PAY_TERM",new Variant(0));
        props.put("PAY_DAY",new Variant(0));
        props.put("BILL_NO",new Variant(""));
        props.put("BILL_DATE",new Variant(""));
        props.put("BOOK_CODE",new Variant(""));
        props.put("VOUCHER_NO", new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        props.put("LEGACY_NO", new Variant(""));
        props.put("LEGACY_DATE",new Variant(""));
        props.put("EFFECT",new Variant(""));
        props.put("DEBIT_AMOUNT",new Variant(0));
        props.put("CREDIT_AMOUNT",new Variant(0));
        props.put("CLOSING_BALANCE",new Variant(0));
    }
    
    public static int LastClosingEntry(String theDate) {
        int EntryNo = 0;
        try {
            EntryNo = data.getIntValueFromDB("SELECT ENTRY_NO FROM D_FIN_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='"+theDate+"' ORDER BY ENTRY_DATE DESC",FinanceGlobal.FinURL);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return EntryNo;
    }
}
