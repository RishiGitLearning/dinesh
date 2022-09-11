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

public class clsAuthority {
    
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
    public clsAuthority() {
        props=new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("AUTHORITY_USER_ID",new Variant(0));
        props.put("FROM_DATE",new Variant(""));
        props.put("TO_DATE",new Variant(""));
        props.put("USER_ID",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("DEPT_ID",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
    }
    
    
    
    public static clsAuthority getObject(int pCompanyID,int pEntryNo) {
        clsAuthority ObjAuthority=new clsAuthority();
        
        try {
            ResultSet rsTmp;
            
            String strSQL="SELECT * FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND SR_NO="+pEntryNo;
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ObjAuthority.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjAuthority.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjAuthority.setAttribute("AUTHORITY_USER_ID",rsTmp.getInt("AUTHORITY_USER_ID"));
                ObjAuthority.setAttribute("FROM_DATE",rsTmp.getString("FROM_DATE"));
                ObjAuthority.setAttribute("TO_DATE",rsTmp.getString("TO_DATE"));
                ObjAuthority.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                ObjAuthority.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                ObjAuthority.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                ObjAuthority.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjAuthority.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjAuthority.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjAuthority.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
            }
            
        }
        catch(Exception e) {
        }
        
        return ObjAuthority;
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Insert() {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            //Generate Entry No.
            
            int EntryNo=(int)data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_AUTHORITY", "SR_NO");
            setAttribute("SR_NO",EntryNo+1);
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_AUTHORITY WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            rsTmp.first();
            
            
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsTmp.updateInt("SR_NO",(int)getAttribute("SR_NO").getVal());
            rsTmp.updateInt("AUTHORITY_USER_ID",(int)getAttribute("AUTHORITY_USER_ID").getVal());
            rsTmp.updateString("FROM_DATE",(String)getAttribute("FROM_DATE").getObj());
            rsTmp.updateString("TO_DATE",(String)getAttribute("TO_DATE").getObj());
            rsTmp.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsTmp.updateInt("MODULE_ID",(int)getAttribute("MODULE_ID").getVal());
            rsTmp.updateInt("dept_ID",(int)getAttribute("DEPT_ID").getVal());
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
            data.Execute("DELETE FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND SR_NO="+pEntryNo);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static HashMap getAvailableAuthority(int pCompanyID,int pUserID) {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        
        try {
            
            
            //rsTmp=data.getResult("SELECT DISTINCT(AUTHORITY_USER_ID) AS AUTHORITY_USER_ID FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID);
            String sql="SELECT DISTINCT(AUTHORITY_USER_ID) AS AUTHORITY_USER_ID FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+""
                    + " AND CURDATE() BETWEEN FROM_DATE AND TO_DATE "
                    + " UNION ALL "
                    + "SELECT DISTINCT(AUTHORITY_USER_ID) AS AUTHORITY_USER_ID FROM D_COM_AUTHORITY_AUTO WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+""
                    + " AND CURDATE() BETWEEN FROM_DATE AND TO_DATE "
                     ;
            System.out.println(sql);
            rsTmp=data.getResult(sql);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsAuthority ObjAuthority=new clsAuthority();
                    ObjAuthority.setAttribute("AUTHORITY_USER_ID",rsTmp.getInt("AUTHORITY_USER_ID"));
                    
                    List.put(Integer.toString(List.size()+1),ObjAuthority);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        return List;
    }
    
    
    public static HashMap getList() {
        ResultSet rsTmp;
        HashMap List=new HashMap();
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_COM_AUTHORITY");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsAuthority ObjAuthority=new clsAuthority();
                    
                    ObjAuthority.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjAuthority.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjAuthority.setAttribute("AUTHORITY_USER_ID",rsTmp.getInt("AUTHORITY_USER_ID"));
                    ObjAuthority.setAttribute("FROM_DATE",rsTmp.getString("FROM_DATE"));
                    ObjAuthority.setAttribute("TO_DATE",rsTmp.getString("TO_DATE"));
                    ObjAuthority.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    ObjAuthority.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                    ObjAuthority.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    
                    List.put(Integer.toString(List.size()+1), ObjAuthority);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
            
        }
        
        return List;
    }
    
    public static boolean IsAuthorityGiven(int pCompanyID,int pUserID,int pAuthorityUserID,int pModuleID) {
        ResultSet rsTmp;
        boolean AuthorityGiven=false;
        String currentDate="";
        
        try {
            rsTmp=data.getResult("SELECT DATE_FORMAT(SYSDATE(),'%Y-%m-%d') AS CUR_DATE");
            rsTmp.first();
            
            currentDate=rsTmp.getString("CUR_DATE");
            
            
            //rsTmp=data.getResult("SELECT * FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND AUTHORITY_USER_ID="+pAuthorityUserID+" AND MODULE_ID="+pModuleID);
            String sql="SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND AUTHORITY_USER_ID="+pAuthorityUserID+" AND MODULE_ID="+pModuleID+""
                    + " UNION ALL "
                    + " SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY_AUTO WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND AUTHORITY_USER_ID="+pAuthorityUserID+" AND MODULE_ID="+pModuleID+" ";
            System.out.println(sql);
            rsTmp=data.getResult(sql);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Check the Dates
                String FromDate=rsTmp.getString("FROM_DATE");
                String ToDate=rsTmp.getString("TO_DATE");
                
                java.sql.Date dFromDate=java.sql.Date.valueOf(FromDate);
                java.sql.Date dToDate=java.sql.Date.valueOf(ToDate);
                
                java.sql.Date dCurDate=java.sql.Date.valueOf(currentDate);
                
                if((!FromDate.trim().equals(""))&&(!ToDate.trim().equals(""))&&(!FromDate.trim().equals("0000-00-00"))&&(!ToDate.trim().equals("0000-00-00")) ) {
                    if(  (dCurDate.equals(dFromDate)||dCurDate.after(dFromDate))&&(dCurDate.equals(dToDate)||dCurDate.before(dToDate))) {
                        AuthorityGiven=true;
                    }
                }
                else {
                    AuthorityGiven=true;
                }
                
            }
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        return AuthorityGiven;
    }
    
    
    public static boolean IsAuthorityGiven(int pCompanyID,int pUserID,int pAuthorityUserID,int pModuleID,int pDeptID) {
        ResultSet rsTmp;
        boolean AuthorityGiven=false;
        boolean HasAllDeptAuthority=false;
        String strSQL="";
        String currentDate="";
        
        try {
            rsTmp=data.getResult("SELECT DATE_FORMAT(SYSDATE(),'%Y-%m-%d') AS CUR_DATE");
            rsTmp.first();
            
            currentDate=rsTmp.getString("CUR_DATE");
            
            
            //rsTmp=data.getResult("SELECT * FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND AUTHORITY_USER_ID="+pAuthorityUserID+" AND MODULE_ID="+pModuleID+" AND DEPT_ID=0");
            String sql="SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND AUTHORITY_USER_ID="+pAuthorityUserID+" AND MODULE_ID="+pModuleID+" AND DEPT_ID=0"
                    + " UNION ALL "
                    + " SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY_AUTO WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND AUTHORITY_USER_ID="+pAuthorityUserID+" AND MODULE_ID="+pModuleID+" AND DEPT_ID=0";
            rsTmp=data.getResult(sql);
            rsTmp.first();
            
            if(rsTmp.getRow()>0)
            {
              HasAllDeptAuthority=true;  
                strSQL = "SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY WHERE COMPANY_ID=" + pCompanyID + " AND USER_ID=" + pUserID + " AND AUTHORITY_USER_ID=" + pAuthorityUserID + " AND MODULE_ID=" + pModuleID + " "
                        + " UNION ALL "
                        + " SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY_AUTO WHERE COMPANY_ID=" + pCompanyID + " AND USER_ID=" + pUserID + " AND AUTHORITY_USER_ID=" + pAuthorityUserID + " AND MODULE_ID=" + pModuleID + "";
            }
            else
            {
              HasAllDeptAuthority=false;   
                strSQL = "SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY WHERE COMPANY_ID=" + pCompanyID + " AND USER_ID=" + pUserID + " AND AUTHORITY_USER_ID=" + pAuthorityUserID + " AND MODULE_ID=" + pModuleID + " AND DEPT_ID=" + pDeptID + " "
                        + " UNION ALL "
                        + " SELECT FROM_DATE,TO_DATE FROM D_COM_AUTHORITY_AUTO WHERE COMPANY_ID=" + pCompanyID + " AND USER_ID=" + pUserID + " AND AUTHORITY_USER_ID=" + pAuthorityUserID + " AND MODULE_ID=" + pModuleID + " AND DEPT_ID=" + pDeptID + "";
            }
           
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Check the Dates
                String FromDate=rsTmp.getString("FROM_DATE");
                String ToDate=rsTmp.getString("TO_DATE");
                
                java.sql.Date dFromDate=java.sql.Date.valueOf(FromDate);
                java.sql.Date dToDate=java.sql.Date.valueOf(ToDate);
                
                java.sql.Date dCurDate=java.sql.Date.valueOf(currentDate);
                
                if((!FromDate.trim().equals(""))&&(!ToDate.trim().equals(""))&&(!FromDate.trim().equals("0000-00-00"))&&(!ToDate.trim().equals("0000-00-00")) ) {
                    if(  (dCurDate.equals(dFromDate)||dCurDate.after(dFromDate))&&(dCurDate.equals(dToDate)||dCurDate.before(dToDate))) {
                        AuthorityGiven=true;
                    }
                }
                else {
                    AuthorityGiven=true;
                }
            }
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        return AuthorityGiven;
    }
    
    
}
