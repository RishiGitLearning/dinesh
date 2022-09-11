/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Stores;


import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.*;

/**
 *
 * @author  nrpatel
 * @version
 */

public class clsFortNightlyItem {
    
    private HashMap props;
    public HashMap ItemRecord;
    public boolean Ready = false;
    public String LastError = "";
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
    
    
    /** Creates new clsNoDataObject */
    public clsFortNightlyItem() {
        props=new HashMap();
        props.put("SR_NO",new Variant(0));
        props.put("COMPANY_ID",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("ITEM_NAME",new Variant(""));
        props.put("ITEM_DESC",new Variant(""));
        props.put("UNIT",new Variant(""));
        props.put("RECMN_MONTHLY_CONS",new Variant(0));
        props.put("RECMN_STOCK_LEVEL",new Variant(0));
        ItemRecord = new HashMap();
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Insert() {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            
            data.Execute("DELETE FROM D_INV_FORTNIGHT_ITEMS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_FORTNIGHT_ITEMS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            rsTmp.first();
            
            for(int i=1; i<=ItemRecord.size();i++) {
                clsFortNightlyItem objItem = (clsFortNightlyItem)ItemRecord.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                System.out.println(objItem.getAttribute("SR_NO").getInt() + " " + objItem.getAttribute("COMPANY_ID").getInt() +" " + objItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateInt("SR_NO",objItem.getAttribute("SR_NO").getInt());
                rsTmp.updateInt("COMPANY_ID",objItem.getAttribute("COMPANY_ID").getInt());
                rsTmp.updateString("ITEM_ID",objItem.getAttribute("ITEM_ID").getString());
                rsTmp.updateString("ITEM_NAME",objItem.getAttribute("ITEM_NAME").getString());
                rsTmp.updateString("ITEM_DESC",objItem.getAttribute("ITEM_DESC").getString());
                rsTmp.updateString("UNIT",objItem.getAttribute("UNIT").getString());
                rsTmp.updateDouble("RECMN_MONTHLY_CONS",objItem.getAttribute("RECMN_MONTHLY_CONS").getDouble());
                rsTmp.updateDouble("RECMN_STOCK_LEVEL",objItem.getAttribute("RECMN_MONTHLY_LEVEL").getDouble());
                rsTmp.insertRow();
            }
            //Close it down
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            
            return true;
            
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    } 
    
    public static HashMap getFortNightlyItemsList() {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_INV_FORTNIGHT_ITEMS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY SR_NO");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsFortNightlyItem ObjFortNightlyItem = new clsFortNightlyItem();
                    
                    ObjFortNightlyItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjFortNightlyItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjFortNightlyItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjFortNightlyItem.setAttribute("ITEM_NAME",rsTmp.getString("ITEM_NAME"));
                    ObjFortNightlyItem.setAttribute("ITEM_DESC",rsTmp.getString("ITEM_DESC"));
                    ObjFortNightlyItem.setAttribute("UNIT",rsTmp.getString("UNIT"));
                    ObjFortNightlyItem.setAttribute("RECMN_MONTHLY_CONS",rsTmp.getDouble("RECMN_MONTHLY_CONS"));
                    ObjFortNightlyItem.setAttribute("RECMN_STOCK_LEVEL",rsTmp.getDouble("RECMN_STOCK_LEVEL"));
                    
                    List.put(Integer.toString(List.size()+1), ObjFortNightlyItem);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
}
