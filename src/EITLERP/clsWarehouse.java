package EITLERP;
/*
 * clsWarehouse.java
 *
 * Created on March 27, 2004, 10:20 AM
 */
 
import java.util.*;
import java.sql.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;  
  
public class clsWarehouse {

    public String LastError;
    private HashMap props;
    
    public boolean Ready = false;
    
    private ResultSet rsWarehouse;
    private Connection conn;
    private Statement stmt;
    
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

    /** Creates new clsWarehouse */
    public clsWarehouse() {
    props=new HashMap();
    props.put("COMPANY_ID",new Variant());
    props.put("WAREHOUSE_ID", new Variant(""));
    props.put("WAREHOUSE_NAME", new Variant(""));
    props.put("RESPONSIBLE_PERSON",new Variant(0));
    props.put("CREATED_BY", new Variant(0));
    props.put("CREATED_DATE", new Variant(""));
    props.put("MODIFIED_BY",new Variant(0));
    props.put("MODIFIED_DATE", new Variant(""));
    }
    
   boolean LoadData() {
        Ready=false;
        try
        {
            String strSql = "Select * From D_INV_WAREHOUSE_MASTER WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID);
            conn=data.getConn();
            stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if (stmt.execute(strSql))
            {
                rsWarehouse = stmt.getResultSet();                
                Ready=MoveFirst();
            }
            else
                Ready=false;                                
                return Ready;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }
    }
   
   
   public void Close()
   {
     try
     {
       //conn.close();
       stmt.close();
       rsWarehouse.close();
     }
     catch(Exception e)
     {
         
     }
       
   }
                
    boolean MoveFirst() {
        try
        {
             rsWarehouse.first();
             return populateprops();
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }    
    }
    
    boolean MoveNext() {
        try
        {
          if(rsWarehouse.isAfterLast() || rsWarehouse.isLast())  
          {   
             rsWarehouse.last();
          }
            else
            {   
               rsWarehouse.next();
               if (rsWarehouse.isLast())
               {
                   rsWarehouse.last();
               }
            }  
            return populateprops();
          }   
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }    
    }
    
    boolean MovePrevious() {
        try
        {
          if(rsWarehouse.isBeforeFirst()||rsWarehouse.isFirst())  
          {   
             rsWarehouse.first();
          }
            else
            {   
               rsWarehouse.previous();
               if (rsWarehouse.isFirst())
               {
                   rsWarehouse.first();
               }
            }  
               return populateprops();
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }
    }
    
    boolean MoveLast() {
        try
        {
            rsWarehouse.last();
            return populateprops();
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }    
    }

    boolean Add() {
        try
        {                       
            ResultSet rstemp;
            Statement stmtemp = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
             setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            rsWarehouse.moveToInsertRow();
            rsWarehouse.updateLong("COMPANY_ID", (long) getAttribute("COMPANY_ID").getVal());
            rsWarehouse.updateString("WAREHOUSE_ID",(String) getAttribute("WAREHOUSE_ID").getObj());
            rsWarehouse.updateString("WAREHOUSE_NAME",(String) getAttribute("WAREHOUSE_NAME").getObj());
            rsWarehouse.updateInt("RESPONSIBLE_PERSON",(int) getAttribute("RESPONSIBLE_PERSON").getVal());
            rsWarehouse.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsWarehouse.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsWarehouse.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsWarehouse.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsWarehouse.updateBoolean("CHANGED",true);
            rsWarehouse.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsWarehouse.insertRow();           
            return true;
        }                
        catch(Exception e)
        {
            
            //JOptionPane.showMessageDialog(null,e.getMessage());
            LastError = e.getMessage();
            return false;
        }   
        
    }
    
    boolean Edit() {
       try{
            rsWarehouse.updateString("WAREHOUSE_NAME",(String) getAttribute("WAREHOUSE_NAME").getObj());
            rsWarehouse.updateInt("RESPONSIBLE_PERSON",(int) getAttribute("RESPONSIBLE_PERSON").getVal());
            rsWarehouse.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsWarehouse.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsWarehouse.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsWarehouse.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsWarehouse.updateBoolean("CHANGED",true);
            rsWarehouse.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsWarehouse.updateRow();
            return true;
          }
       catch(Exception e)
       {
         LastError = e.getMessage();
         return false;
       }
    }
    
   boolean Delete() {
      try
      {
         String WarehouseID =(String) getAttribute("WAREHOUSE_ID").getObj();
         String stmt = "DELETE FROM D_INV_WAREHOUSE_MASTER WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " And WAREHOUSE_ID='"+WarehouseID+"'" ;
         data.Execute(stmt);
         return true;                
       }
      catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }        
   }
   
  public static Object getObject(int pCompanyID,String pWarehouseId) 
    {
        String strCondition = " where WAREHOUSE_ID='" + pWarehouseId + "' And COMPANY_ID=" + pCompanyID;
        clsWarehouse objWarehouse = new clsWarehouse();
        objWarehouse.Filter(strCondition);
        return objWarehouse;
    }

    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn=data.getConn();
        
        long Counter=0;
        
        try
        {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_WAREHOUSE_MASTER "+pCondition);
            rsTmp.first();    
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsWarehouse ObjWareHouse=new clsWarehouse();

                ObjWareHouse.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjWareHouse.setAttribute("WAREHOUSE_ID",rsTmp.getString("WAREHOUSE_ID"));
                ObjWareHouse.setAttribute("WAREHOUSE_NAME",rsTmp.getString("WAREHOUSE_NAME"));
                
                List.put(Long.toString(Counter),ObjWareHouse);
                rsTmp.next();
            }
            return List;     
        }
        catch(Exception e)
        {
            return List; 
        }
        
        
    }
	

   public boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_INV_WAREHOUSE_MASTER " + pCondition ;
            conn=data.getConn();
            stmt=conn.createStatement();           
            
            if (stmt.execute(strSql))
            {                
                rsWarehouse = stmt.getResultSet();
                Ready=MoveFirst();
            }
            else
            {    Ready=false;}
           
            return Ready;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }       
    }

   public boolean Find(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_INV_WAREHOUSE_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);
            conn=data.getConn();
            stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if (stmt.execute(strSql))
            {
                rsWarehouse = stmt.getResultSet();
                Ready=MoveFirst();
                while(rsWarehouse.next())
                {
                    if(rsWarehouse.getString("WAREHOUSE_ID").equals(pCondition))
                    {
                        populateprops();
                        return true;
                    }
                    else
                    {  
                      rsWarehouse.next();
                    }  
                }
            }
            else
            {    Ready=false;}
           
            return Ready;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }       
    }
   
    boolean populateprops() 
    {
        try
        {
            setAttribute("COMPANY_ID",rsWarehouse.getLong("COMPANY_ID"));            
            setAttribute("WAREHOUSE_ID",rsWarehouse.getString("WAREHOUSE_ID"));
            setAttribute("WAREHOUSE_NAME",rsWarehouse.getString("WAREHOUSE_NAME"));
            setAttribute("RESPONSIBLE_PERSON",rsWarehouse.getInt("RESPONSIBLE_PERSON"));
            setAttribute("CREATED_BY",rsWarehouse.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsWarehouse.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsWarehouse.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsWarehouse.getString("MODIFIED_DATE"));    
            
            return true;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }
    }    
    
public static String getWarehouseName(int pCompanyId,String pWarehouseID)    
    {
     Connection conn;
     Statement stmt;
     ResultSet rsTmp;
     String getWarehouseName = "";
     
     try{
        conn = data.getConn();
        stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        
        String strSql = "SELECT * FROM D_INV_WAREHOUSE_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyId)+" AND WAREHOUSE_ID='"+pWarehouseID.trim()+"'";
        rsTmp = stmt.executeQuery(strSql);
        
        if(! rsTmp.isAfterLast())
        {
            rsTmp.first();
            getWarehouseName = rsTmp.getString("WAREHOUSE_NAME");
        }
        return getWarehouseName;
     }
     catch(Exception e)
     {
        return getWarehouseName; 
     }
        
    }

}