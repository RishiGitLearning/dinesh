/*
 *
 * clsMRItem.java
 *
 * Created on April 19, 2004, 2:27 PM
 */

package EITLERP.Production.ReportUI;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
 
/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsProformaItem {

    private HashMap props;    
    public boolean Ready = false;

    public Variant getAttribute(String PropName)
    {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,int Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,double Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,float Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,boolean Value)
    {
         props.put(PropName,new Variant(Value));
    }

    
    /** Creates new clsMRItem */
    public clsProformaItem() {
        props=new HashMap();
        props.put("PROFORMA_NO", new Variant(""));
        props.put("SR_NO",new Variant(0));
        props.put("PRIORITY_DATE",new Variant(""));
        props.put("INCHARGE_NAME",new Variant(""));
        props.put("PRIORITY",new Variant(""));        
        props.put("PIECE_NO", new Variant(""));
        props.put("ORDER_DATE",new Variant(""));
        props.put("RCVD_DATE",new Variant(""));
        props.put("DELIV_DATE",new Variant(""));
        props.put("COMM_DATE",new Variant(""));
        //props.put("PRODUCT_CODE",new Variant(""));
        props.put("PRODUCT_CD",new Variant(""));
        
        props.put("PO_NO",new Variant(""));
        props.put("PO_DATE",new Variant(""));
        props.put("Calculate_Weight",new Variant(false));
        
        props.put("ITEM",new Variant(""));
        props.put("STYLE",new Variant(""));
        props.put("LNGTH",new Variant(0.00));
        props.put("RCVD_MTR",new Variant(0.00));
        props.put("WIDTH",new Variant(0.00));
        props.put("RECD_WDTH",new Variant(0.00));
        props.put("GSQ",new Variant(0)); 
        props.put("WEIGHT",new Variant(0.00));
        props.put("RECD_KG",new Variant(0.00));
         props.put("RATE",new Variant(0.00));
        props.put("BAS_AMT",new Variant(0.00));
        
        props.put("MEMO_DATE",new Variant(""));
        props.put("DISC_PER",new Variant(0.00));
        props.put("DISAMT",new Variant(0.00));      
        props.put("DISBASAMT",new Variant(0.00));
        props.put("EXCISE", new Variant(0.00));
        props.put("SEAM_CHG",new Variant(0.00));
        props.put("SEAM_CHG_PER",new Variant(0.00));
        props.put("INSACC_AMT",new Variant(0.00));
        props.put("INV_AMT",new Variant(0.00));
        props.put("DAYS",new Variant(0));
        props.put("REF_NO",new Variant(""));
        props.put("CONF_NO",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));       
        props.put("POSITION",new Variant(""));       
        props.put("STATION",new Variant(""));
        props.put("ZONE",new Variant(""));
        props.put("INS_IND",new Variant(""));
        props.put("ITEM_DESC",new Variant(""));
        props.put("SYN_PER",new Variant(""));         
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        //props.put("CHEM_TRT_IN",new Variant(""));         
        //props.put("PIN_IND",new Variant(""));
        //props.put("CHARGES",new Variant(""));
        //props.put("SPR_IND",new Variant(""));
        //props.put("SQM_IND",new Variant(""));       
        
 
    }

}
