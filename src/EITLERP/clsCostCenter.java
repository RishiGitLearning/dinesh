/*
 * clsDepartment.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
  
/** 
 *
 * @author  nrpithva
 * @version
 */

public class clsCostCenter {
    
    public String LastError="";
    public String strSql;
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
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
    
    
    /** Creates new clsDepartment */
    public clsCostCenter() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("COST_CENTER_ID",new Variant(0));
        props.put("COST_CENTER_NAME",new Variant(""));
        props.put("COST_CENTER_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSql = "SELECT * FROM D_COM_COST_CENTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready = MoveFirst();
                MoveFirst();
                return Ready;
            }
            else
                Ready = false;
            return Ready;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public void Close()
    {
      try
      {
        //Conn.close();
        Stmt.close();
        rsResultSet.close();
      }
      catch(Exception e)
      {
          
      }
        
        
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsResultSet.isAfterLast()||rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            }
            else {
                rsResultSet.next();
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            }
            else {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean Insert() {
        try {
            rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.moveToInsertRow();
            
            setAttribute("DEPT_ID",data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_COST_CENTER","COST_CENTER_ID"));
            
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("COST_CENTER_ID",(int) getAttribute("COST_CENTER_ID").getVal());
            rsResultSet.updateLong("DEPT_ID",(int) getAttribute("DEPT_ID").getVal());
            rsResultSet.updateString("COST_CENTER_NAME",(String) getAttribute("COST_CENTER_NAME").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    
    //Updates current record
    public boolean Update() {
        try {
            rsResultSet.updateLong("DEPT_ID",(int) getAttribute("DEPT_ID").getVal());
            rsResultSet.updateString("COST_CENTER_NAME",(String) getAttribute("COST_CENTER_NAME").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete() {
        try {
            long CostCenterID =(int) getAttribute("COST_CENTER_ID").getVal();
            String stmt = "DELETE FROM D_COM_COST_CENTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " And COST_CENTER_ID="+CostCenterID;
            data.Execute(stmt);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,int pCostCenterID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) +" AND COST_CENTER_ID=" + Integer.toString(pCostCenterID);
        clsDepartment ObjDepartment = new clsDepartment();
        ObjDepartment.Filter(strCondition);
        return ObjDepartment;
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_COST_CENTER "+pCondition);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsCostCenter ObjCost=new clsCostCenter();
                
                ObjCost.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjCost.setAttribute("COST_CENTER_ID",rsTmp.getInt("COST_CENTER_ID"));
                ObjCost.setAttribute("COST_CENTER_NAME",rsTmp.getString("COST_CENTER_NAME"));
                
                List.put(Long.toString(Counter),ObjCost);
                rsTmp.next();
            }
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
    }
    
    
    
    public static HashMap getList(String pCondition,String pURL) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn(pURL);
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_COST_CENTER "+pCondition);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsCostCenter ObjCost=new clsCostCenter();
                
                ObjCost.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjCost.setAttribute("COST_CENTER_ID",rsTmp.getInt("COST_CENTER_ID"));
                ObjCost.setAttribute("COST_CENTER_NAME",rsTmp.getString("COST_CENTER_NAME"));
                
                List.put(Long.toString(Counter),ObjCost);
                rsTmp.next();
            }
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
    }
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_COST_CENTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement();
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready= MoveFirst();
            }
            else
            {    Ready=false;}
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean setData() {
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("COST_CENTER_ID",rsResultSet.getInt("COST_CENTER_ID"));
            setAttribute("DEPT_ID",rsResultSet.getInt("DEPT_ID"));
            setAttribute("COST_CENTER_NAME",rsResultSet.getString("COST_CENTER_NAME"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Find(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_COST_CENTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID);
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
                while(! rsResultSet.isAfterLast()) {
                    if(rsResultSet.getString("COST_CENTER_ID").equals(pCondition)) {
                        setData();
                        return true;
                    }
                    else {
                        rsResultSet.next();
                    }
                }
            }
            else
            {    Ready=false;}
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
public static int getCostCenterID(int pCompanyID,int pDeptID)    
{
    Connection tmpConn;
    Statement stTmp;
    ResultSet rsTmp;
    int CostCenterID=0;
    
    try
    {
      tmpConn=data.getConn();
      stTmp=tmpConn.createStatement();
      rsTmp=stTmp.executeQuery("SELECT COST_CENTER_ID FROM D_COM_COST_CENTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID);
      rsTmp.first();
      
      if(rsTmp.getRow()>0)
      {
        CostCenterID=rsTmp.getInt("COST_CENTER_ID");  
      }

    //tmpConn.close();
    stTmp.close();
    rsTmp.close();
      
    }
    catch(Exception e)
    {
        
    }
    
    return CostCenterID;
}
    

public static int getDeptID(int pCompanyID,int pCostCenterID)    
{
    Connection tmpConn;
    Statement stTmp;
    ResultSet rsTmp;
    int DeptID=0;
    
    try
    {
      tmpConn=data.getConn();
      stTmp=tmpConn.createStatement();
      rsTmp=stTmp.executeQuery("SELECT DEPT_ID FROM D_COM_COST_CENTER WHERE COMPANY_ID="+pCompanyID+" AND COST_CENTER_ID="+pCostCenterID);
      rsTmp.first();
      
      if(rsTmp.getRow()>0)
      {
        DeptID=rsTmp.getInt("DEPT_ID");  
      }

    //tmpConn.close();
    stTmp.close();
    rsTmp.close();
      
    }
    catch(Exception e)
    {
        
    }
    
    return DeptID;
}


    public static String getCostCenterName(int pCompanyID,int pCostCenterID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String CostCenterName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT COST_CENTER_NAME FROM D_COM_COST_CENTER WHERE COMPANY_ID="+pCompanyID+" AND COST_CENTER_ID="+pCostCenterID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CostCenterName=rsTmp.getString("COST_CENTER_NAME");
            }

            
        //tmpConn.close();
        stTmp.close();
        rsTmp.close();
            
            return CostCenterName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static boolean IsValidCostCenterCode(int pCompanyID,int pCostCenterID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT COST_CENTER_NAME FROM D_COM_COST_CENTER WHERE COMPANY_ID="+pCompanyID+" AND COST_CENTER_ID="+pCostCenterID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
        //tmpConn.close();
        stTmp.close();
        rsTmp.close();
                
                return true;
            }else
            {//tmpConn.close();
        stTmp.close();
        rsTmp.close();
                
                return false;}
        }
        catch(Exception e) {
            return false;
        }
    }
    
}
