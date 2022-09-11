/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP;


import java.util.*;
import java.sql.*;
import java.net.*;
import java.util.*;
 
/**
 * 
 * @author  nrpatel
 * @version
 */

public class clsSystemColumn {
    
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
    
    /** Creates new clsNoDataObject */
    public clsSystemColumn() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("ORDER",new Variant(0));
        props.put("CAPTION",new Variant(""));
        props.put("READONLY",new Variant(false));
        props.put("HIDDEN",new Variant(false));
        props.put("VARIABLE",new Variant(false));
        props.put("NUMERIC",new Variant(false));
    }

 public static void setOrder(int pCompanyID,int pModuleID,int pSrNo,int pOrder)
 {
    data.Execute("UPDATE D_COM_SYSTEM_COLUMNS SET D_COM_SYSTEM_COLUMNS.ORDER="+pOrder+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND SR_NO="+pSrNo);
 }
    
 public static HashMap getList(String pCondition)   
 {
    Connection tmpConn;
    Statement stTmp;
    ResultSet rsTmp;
    HashMap List=new HashMap();
    int Counter=0;
    
    try
    {
        tmpConn=data.getConn();
        stTmp=tmpConn.createStatement();
        rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_SYSTEM_COLUMNS "+pCondition);
        rsTmp.first();
        
        Counter=0;
        while(!rsTmp.isAfterLast())
        {
            Counter++;
            clsSystemColumn ObjColumn=new clsSystemColumn();
            ObjColumn.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
            ObjColumn.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
            ObjColumn.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
            ObjColumn.setAttribute("ORDER",rsTmp.getInt("ORDER"));
            ObjColumn.setAttribute("CAPTION",rsTmp.getString("CAPTION"));
            ObjColumn.setAttribute("READONLY",rsTmp.getBoolean("READONLY"));
            ObjColumn.setAttribute("HIDDEN",rsTmp.getBoolean("HIDDEN"));
            ObjColumn.setAttribute("VARIABLE",rsTmp.getString("VARIABLE"));
            ObjColumn.setAttribute("NUMERIC",rsTmp.getBoolean("NUMERIC"));
            List.put(Integer.toString(Counter),ObjColumn);
            rsTmp.next();
        }

    //tmpConn.close();
    stTmp.close();
    rsTmp.close();
        
        return List;
    }
    catch(Exception e)
    {
        return List; 
    }
 }
}
