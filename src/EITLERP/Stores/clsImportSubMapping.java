/*
 * clsItemMapping.java
 *
 * Created on June 22, 2005, 3:08 PM
 */

package EITLERP.Stores;

/**
 *
 * @author  root
 */

import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.*;
import java.awt.*;
import javax.swing.*;

public class clsImportSubMapping {
    
    
    private HashMap props;
    public boolean Ready = false;
    
    public String LastError="";
    
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
    
    /** Creates a new instance of clsItemMapping */
    public clsImportSubMapping() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("SUB_ITEM_ID",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    
    public static HashMap getList() {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_ITEM_MAPPING");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsItemMapping ObjItem=new clsItemMapping();
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItem.setAttribute("FIRST_ITEM_ID",rsTmp.getString("FIRST_ITEM_ID"));
                    ObjItem.setAttribute("SECOND_COMPANY_ID",rsTmp.getInt("SECOND_COMPANY_ID"));
                    ObjItem.setAttribute("SECOND_ITEM_ID",rsTmp.getString("SECOND_ITEM_ID"));
                    
                    List.put(Integer.toString(List.size()+1),ObjItem);
                    
                    rsTmp.next();
                }
            }
            rsTmp.close();
            stTmp.close();
        }
        catch(Exception e) {
            
        }
        return List;
    }
    
    
    public static boolean Delete(int pCompanyID,int pSrNo) {
        try {
            data.Execute("DELETE FROM D_COM_IMPORT_SUB_ITEMS WHERE COMPANY_ID="+pCompanyID+" AND SR_NO="+pSrNo);
        }
        catch(Exception e) {
            
        }
        return true;
    }
    
    
    
    public static void ShowSubstituteItem(String SubItemID) {
        try {
            String ImportItemID="";
            
            if(!data.IsRecordExist("SELECT ITEM_ID FROM D_COM_IMPORT_SUB_ITEMS WHERE SUB_ITEM_ID='"+SubItemID+"'")) {
                JOptionPane.showMessageDialog(null,"No substitute item found for this item");
                return;
            }
            
            ImportItemID=data.getStringValueFromDB("SELECT ITEM_ID FROM D_COM_IMPORT_SUB_ITEMS WHERE SUB_ITEM_ID='"+SubItemID+"'");
            
            frmSubImportItem objDialog=new frmSubImportItem();
            objDialog.AllowEdit=false;
            objDialog.ImportItemID=ImportItemID;
            objDialog.SubItemID=SubItemID;
            objDialog.ShowList();
        }
        catch(Exception e) {
            
        }
    }
    
    public static boolean Insert(Object pObjItemMapping) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            clsImportSubMapping ObjItem=(clsImportSubMapping)pObjItemMapping;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_IMPORT_SUB_ITEMS");
            rsTmp.first();
            
            long NextNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_IMPORT_SUB_ITEMS", "SR_NO")+1;
            
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsTmp.updateLong("SR_NO",NextNo);
            rsTmp.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
            rsTmp.updateString("SUB_ITEM_ID",(String)ObjItem.getAttribute("SUB_ITEM_ID").getObj());
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateInt("CREATED_BY",EITLERPGLOBAL.gUserID);
            rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateInt("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.insertRow();
            
            rsTmp.close();
            stTmp.close();
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    
}
