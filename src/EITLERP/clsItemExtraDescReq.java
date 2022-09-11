/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP;


import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author  nrpatel
 * @version
 */

public class clsItemExtraDescReq {
    
    private HashMap props;
    public boolean Ready = false;
    
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
    public clsItemExtraDescReq() {
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("ENTRY_NO",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("ITEM_CRITERIA_TYPE",new Variant(0));
        props.put("FROM_ITEM_ID",new Variant(""));
        props.put("TO_ITEM_ID",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    
    
    public static clsItemExtraDescReq getObject(int pCompanyID,int pEntryNo) {
        clsItemExtraDescReq ObjItemExtraDescReq=new clsItemExtraDescReq();
        
        try {
            ResultSet rsTmp;
            
            String strSQL="SELECT * FROM D_COM_ITEM_EXTRA_DESC_REQ WHERE COMPANY_ID="+pCompanyID+" AND ENTRY_NO="+pEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ObjItemExtraDescReq.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItemExtraDescReq.setAttribute("ENTRY_NO",rsTmp.getInt("ENTRY_NO"));
                ObjItemExtraDescReq.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItemExtraDescReq.setAttribute("ITEM_CRITERIA_TYPE",rsTmp.getInt("ITEM_CRITERIA_TYPE"));
                ObjItemExtraDescReq.setAttribute("FROM_ITEM_ID",rsTmp.getString("FROM_ITEM_ID"));
                ObjItemExtraDescReq.setAttribute("TO_ITEM_ID",rsTmp.getString("TO_ITEM_ID"));
                ObjItemExtraDescReq.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjItemExtraDescReq.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItemExtraDescReq.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjItemExtraDescReq.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
            }
            
        }
        catch(Exception e) {
        }
        
        return ObjItemExtraDescReq;
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Insert() {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            //Generate Entry No.
            
            int EntryNo=(int)data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_ITEM_EXTRA_DESC_REQ", "ENTRY_NO");
            setAttribute("ENTRY_NO",EntryNo);
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_ITEM_EXTRA_DESC_REQ WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            rsTmp.first();
            
            
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsTmp.updateInt("ENTRY_NO",(int)getAttribute("ENTRY_NO").getVal());
            rsTmp.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsTmp.updateInt("ITEM_CRITERIA_TYPE",(int)getAttribute("ITEM_CRITERIA_TYPE").getVal());
            rsTmp.updateString("FROM_ITEM_ID",(String)getAttribute("FROM_ITEM_ID").getObj());
            rsTmp.updateString("TO_ITEM_ID",(String)getAttribute("TO_ITEM_ID").getObj());
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsTmp.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsTmp.insertRow();
            
            
            //Close it down
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            
            return true;
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
            return false;
        }
    }
    
    
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Remove(int pCompanyID,int pEntryNo) {
        try {
            data.Execute("DELETE FROM D_COM_ITEM_EXTRA_DESC_REQ WHERE COMPANY_ID="+pCompanyID+" AND ENTRY_NO="+pEntryNo);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }       
    
    public static HashMap getList() {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_COM_ITEM_EXTRA_DESC_REQ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsItemExtraDescReq ObjItemExtraDescReq=new clsItemExtraDescReq();
                    
                    ObjItemExtraDescReq.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItemExtraDescReq.setAttribute("ENTRY_NO",rsTmp.getInt("ENTRY_NO"));
                    ObjItemExtraDescReq.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjItemExtraDescReq.setAttribute("ITEM_CRITERIA_TYPE",rsTmp.getInt("ITEM_CRITERIA_TYPE"));
                    ObjItemExtraDescReq.setAttribute("FROM_ITEM_ID",rsTmp.getString("FROM_ITEM_ID"));
                    ObjItemExtraDescReq.setAttribute("TO_ITEM_ID",rsTmp.getString("TO_ITEM_ID"));
                    
                    List.put(Integer.toString(List.size()+1), ObjItemExtraDescReq);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static boolean IsItemExtraDescReq(int CompanyID,String ItemID) {
        String str = "SELECT * FROM D_COM_ITEM_EXTRA_DESC_REQ WHERE COMPANY_ID="+CompanyID+" AND (ITEM_ID LIKE '"+ItemID+"%' OR "+
                " (FROM_ITEM_ID <= "+ItemID+" AND TO_ITEM_ID >="+ItemID+"))";
        if(data.IsRecordExist(str)) {
            return true;
        }
        else {
            return false;
        }
    }
    
}
