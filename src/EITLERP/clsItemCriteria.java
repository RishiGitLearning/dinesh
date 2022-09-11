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

public class clsItemCriteria {
    
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
    public clsItemCriteria() {
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("ENTRY_NO",new Variant(0));
        props.put("ITEM_ID",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("ITEM_CRITERIA_TYPE",new Variant(0));
        props.put("FROM_ITEM_ID",new Variant(""));
        props.put("TO_ITEM_ID",new Variant(""));
        props.put("APPROVAL_AUTHORITY",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    
    
    public static clsItemCriteria getObject(int pCompanyID,int pEntryNo) {
        clsItemCriteria ObjItemCriteria=new clsItemCriteria();
        
        try {
            ResultSet rsTmp;
            
            String strSQL="SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+pCompanyID+" AND ENTRY_NO="+pEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ObjItemCriteria.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItemCriteria.setAttribute("ENTRY_NO",rsTmp.getInt("ENTRY_NO"));
                ObjItemCriteria.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItemCriteria.setAttribute("DEPT_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItemCriteria.setAttribute("ITEM_CRITERIA_TYPE",rsTmp.getInt("ITEM_CRITERIA_TYPE"));
                ObjItemCriteria.setAttribute("FROM_ITEM_ID",rsTmp.getString("FROM_ITEM_ID"));
                ObjItemCriteria.setAttribute("TO_ITEM_ID",rsTmp.getString("TO_ITEM_ID"));
                ObjItemCriteria.setAttribute("APPROVAL_AUTHORITY",rsTmp.getString("APPROVAL_AUTHORITY"));
                ObjItemCriteria.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjItemCriteria.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItemCriteria.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjItemCriteria.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
            }
            
        }
        catch(Exception e) {
        }
        
        return ObjItemCriteria;
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Insert() {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            //Generate Entry No.
            
            int EntryNo=(int)data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_ITEM_APPROVAL_CRITERIA", "ENTRY_NO");
            setAttribute("ENTRY_NO",EntryNo+1);
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            rsTmp.first();
            
            
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsTmp.updateInt("ENTRY_NO",(int)getAttribute("ENTRY_NO").getVal());
            rsTmp.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsTmp.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsTmp.updateInt("ITEM_CRITERIA_TYPE",(int)getAttribute("ITEM_CRITERIA_TYPE").getVal());
            rsTmp.updateString("FROM_ITEM_ID",(String)getAttribute("FROM_ITEM_ID").getObj());
            rsTmp.updateString("TO_ITEM_ID",(String)getAttribute("TO_ITEM_ID").getObj());
            rsTmp.updateString("APPROVAL_AUTHORITY",(String)getAttribute("APPROVAL_AUTHORITY").getObj());
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
            data.Execute("DELETE FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+pCompanyID+" AND ENTRY_NO="+pEntryNo);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static String getApprovalAuthority(String pItemID,int pDeptID) {
        Connection tmpConn;
        Statement stTmp,stStartsWith;
        ResultSet rsTmp,rsStartsWith;
        String Authority="";
        boolean ItemFound=false;
        
        try {

            
            //First check the item flag in item master. It overrides all other settings
            
            //first check for the department
            tmpConn=data.getConn();
            
            //First check for M.D. Flag. Overrides department logic
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT SPECIAL_APPROVAL FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+pItemID+"' AND SPECIAL_APPROVAL='M' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Authority=rsTmp.getString("SPECIAL_APPROVAL");
                return Authority;
            }

            
            
            //======= (4) Audit - New Condition  =========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_ID = SUBSTRING('"+pItemID+"',1,LENGTH(ITEM_ID)) AND ITEM_CRITERIA_TYPE=2 AND APPROVAL_AUTHORITY='A' "); //Starts with - 2
            rsStartsWith.first();

            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            
            
            
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT APPROVAL_AUTHORITY FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) //Yes Found
            {
                Authority=rsTmp.getString("APPROVAL_AUTHORITY");
                return Authority;
            }

            //All Department
            //======= (1) Check item code starts with condition =========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_ID = SUBSTRING('"+pItemID+"',1,LENGTH(ITEM_ID)) AND ITEM_CRITERIA_TYPE=2"); //Starts with - 2
            rsStartsWith.first();

            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (2) Check Exact item code --------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_ID = '"+pItemID+"' AND ITEM_CRITERIA_TYPE=1"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (3) Check  item code  Range ------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE '"+pItemID+"'>=FROM_ITEM_ID AND '"+pItemID+"'<=IF(TO_ITEM_ID='','999999999999',TO_ITEM_ID) AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID=0 AND ITEM_CRITERIA_TYPE=3"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            
            //======= (1) Check item code starts with condition =========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_ID = SUBSTRING('"+pItemID+"',1,"+pItemID.trim().length()+") AND ITEM_CRITERIA_TYPE=2"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (2) Check Exact item code --------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_ID = '"+pItemID+"' AND ITEM_CRITERIA_TYPE=1"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            //======= (3) Check  item code  Range ------------=========
            stStartsWith=tmpConn.createStatement();
            rsStartsWith=stStartsWith.executeQuery("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA WHERE '"+pItemID+"'>=FROM_ITEM_ID AND '"+pItemID+"'<=IF(TO_ITEM_ID='','999999999999',TO_ITEM_ID) AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID+" AND ITEM_CRITERIA_TYPE=3"); //Starts with - 2
            rsStartsWith.first();
            
            if(rsStartsWith.getRow()>0) {
                Authority=rsStartsWith.getString("APPROVAL_AUTHORITY");
                return Authority;
            }
            //========================================================//
            
            
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT SPECIAL_APPROVAL FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+pItemID+"' AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Authority=rsTmp.getString("SPECIAL_APPROVAL");
            }
            
            return Authority;
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        return Authority;
    }
    
    
    public static HashMap getList() {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_COM_ITEM_APPROVAL_CRITERIA");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsItemCriteria ObjCriteria=new clsItemCriteria();
                    
                    ObjCriteria.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjCriteria.setAttribute("ENTRY_NO",rsTmp.getInt("ENTRY_NO"));
                    ObjCriteria.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                    ObjCriteria.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjCriteria.setAttribute("ITEM_CRITERIA_TYPE",rsTmp.getInt("ITEM_CRITERIA_TYPE"));
                    ObjCriteria.setAttribute("FROM_ITEM_ID",rsTmp.getString("FROM_ITEM_ID"));
                    ObjCriteria.setAttribute("TO_ITEM_ID",rsTmp.getString("TO_ITEM_ID"));
                    ObjCriteria.setAttribute("APPROVAL_AUTHORITY",rsTmp.getString("APPROVAL_AUTHORITY"));
                    
                    List.put(Integer.toString(List.size()+1), ObjCriteria);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
}
