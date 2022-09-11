/*
 * clsIssueItemDetail.java
 *
 * Created on October 1, 2009, 3:28 PM
 */

package EITLERP.Stores;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  root
 */
public class clsIssueLot {
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colISSUEItems=new HashMap();
    
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
    
    /** Creates a new instance of clsIssueItemDetail */
    public clsIssueLot() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant());
        props.put("ITEM_ID", new Variant(""));
        props.put("ISSUE_NO", new Variant(""));
        props.put("ISSUE_SR_NO", new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_LOT_NO",new Variant(""));
        props.put("AUTO_LOT_NO",new Variant(""));
        props.put("ISSUED_LOT_QTY",new Variant(0));
        props.put("ISSUE_TYPE",new Variant(0));
    }
    
}
