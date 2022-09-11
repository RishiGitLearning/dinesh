/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP;

import java.util.*;
import java.sql.*;
import java.net.*;
import java.awt.*;
import javax.swing.*; 
  
/** 
 *
 * @author  nhpatel
 * @version 
 */

public class clsLocation {

    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    private String strSql;

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

    
    /** Creates new clsBusinessObject */
    public clsLocation() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("WAREHOUSE_ID",new Variant(""));
        props.put("LOCATION_ID",new Variant(""));
        props.put("LOCATION_NAME",new Variant(""));
        props.put("TOTAL_CAPACITY",new Variant(0));
        props.put("WARNING_CAPACITY",new Variant(0));
        props.put("MULTI_LEVEL_LOCATION",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }

    public boolean LoadData()
    {
      Ready=false;
      try
        {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSql = "SELECT * FROM D_INV_LOCATION_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(Stmt.execute(strSql))
            {   
                rsResultSet = Stmt.getResultSet();                
                MoveFirst();
                return true;
            }
            else
               Ready=false;
               return false;
        }
        catch(Exception e)
        {
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
    public boolean MoveFirst()
    {
        try
        {
          rsResultSet.first();  
          setData();
          return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext()
    {
        try
        {
           if(rsResultSet.isAfterLast()||rsResultSet.isLast())
           {
               //Move pointer at last record
               //If it is beyond eof
               rsResultSet.last();
           }
           else
           {
            rsResultSet.next();
                if(rsResultSet.isAfterLast())
                {
                    rsResultSet.last();
                }
           }
           setData();
           return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious()
    {
        try
        {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst())
            {
               rsResultSet.first(); 
            }
            else
            {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst())
                {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast()
    {
        try
        {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }


    public boolean Insert() 
    {
        try
        {                       
             setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);

            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("WAREHOUSE_ID",(String) getAttribute("WAREHOUSE_ID").getObj());
            rsResultSet.updateString("LOCATION_ID",(String) getAttribute("LOCATION_ID").getObj());
            rsResultSet.updateString("LOCATION_NAME",(String) getAttribute("LOCATION_NAME").getObj());
            rsResultSet.updateFloat("TOTAL_CAPACITY",(float) getAttribute("TOTAL_CAPACITY").getVal());
            rsResultSet.updateFloat("WARNING_CAPACITY",(float) getAttribute("WARNING_CAPACITY").getVal());
            rsResultSet.updateBoolean("MULTI_LEVEL_LOCATION",(boolean) getAttribute("MULTI_LEVEL_LOCATION").getBool());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();           
            return true;
        }
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(null,e.getMessage());
            LastError= e.getMessage();
            return false;
        }   
    }

    
    //Updates current record
    public boolean Update() 
    { 
        try
        {         
            rsResultSet.updateString("LOCATION_NAME",(String) getAttribute("LOCATION_NAME").getObj());
            rsResultSet.updateFloat("TOTAL_CAPACITY",(float) getAttribute("TOTAL_CAPACITY").getVal());
            rsResultSet.updateFloat("WARNING_CAPACITY",(float) getAttribute("WARNING_CAPACITY").getVal());
            rsResultSet.updateBoolean("MULTI_LEVEL_LOCATION",(boolean) getAttribute("MULTI_LEVEL_LOCATION").getBool());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            return true;
        }                
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(null,e.getMessage());            
            LastError = e.getMessage();
            return false;
        }           
    }
    

    //Deletes current record
    public boolean Delete()
    {
      try
      {
         String LocationID =(String) getAttribute("LOCATION_ID").getObj();
         String stmt = "DELETE FROM D_INV_LOCATION_MASTER WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID) + " And LOCATION_ID='"+LocationID.trim()+"'";
         data.Execute(stmt);
         return true;                
      }                
      catch(Exception e)
      {
         LastError = e.getMessage();
         return false;
      }        
    }
    
    public Object getObject(int pCompanyID) 
    {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID);
        clsLocation ObjLocation = new clsLocation();
        ObjLocation.Filter(strCondition);
        return ObjLocation;      
    }
    

    public static HashMap getList(String pCondition)
    {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try
        {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);            

            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_LOCATION_MASTER "+pCondition);
            rsTmp.first();    
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsLocation ObjLocation=new clsLocation();

                ObjLocation.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjLocation.setAttribute("LOCATION_ID",rsTmp.getString("LOCATION_ID"));
                ObjLocation.setAttribute("LOCATION_NAME",rsTmp.getString("LOCATION_NAME"));
                
                List.put(Long.toString(Counter),ObjLocation);
                rsTmp.next();
            }
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
            
            return List;     
        }
        catch(Exception e)
        {
        }
        return List; 
    }
    
    
    
    boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_INV_LOCATION_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement();           
            
            if (Stmt.execute(strSql))
            {                
                rsResultSet = Stmt.getResultSet();
                Ready= MoveFirst();
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
   

    
    public boolean setData() 
    {
        try
        {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));            
            setAttribute("WAREHOUSE_ID",rsResultSet.getString("WAREHOUSE_ID"));
            setAttribute("LOCATION_ID",rsResultSet.getString("LOCATION_ID"));
            setAttribute("LOCATION_NAME",rsResultSet.getString("LOCATION_NAME"));
            setAttribute("TOTAL_CAPACITY",rsResultSet.getFloat("TOTAL_CAPACITY"));
            setAttribute("WARNING_CAPACITY",rsResultSet.getFloat("WARNING_CAPACITY"));
            setAttribute("MULTI_LEVEL_LOCATION",rsResultSet.getBoolean("MULTI_LEVEL_LOCATION"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));    
            return true;
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
            String strSql = "SELECT * FROM D_INV_LOCATION_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if (Stmt.execute(strSql))
            {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
                while(! rsResultSet.isAfterLast())
                {
                    if(rsResultSet.getString("LOCATION_ID").equals(pCondition))
                    {
                        setData();
                        return true;
                    }
                    else
                    {  
                      rsResultSet.next();
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

public static String getLocationName(int pCompanyId,String pLocationID)    
    {
     Connection conn;
     Statement stmt;
     ResultSet rsTmp;
     String getLocationName = "";
     
     try{
        conn = data.getConn();
        stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        
        String strSql = "SELECT * FROM D_INV_LOCATION_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyId)+" AND LOCATION_ID='"+pLocationID.trim()+"'";
        rsTmp = stmt.executeQuery(strSql);
        
        if(! rsTmp.isAfterLast())
        {
            rsTmp.first();
            getLocationName = rsTmp.getString("LOCATION_NAME");
        }
        
     //conn.close();
     stmt.close();
     rsTmp.close();
        
        return getLocationName;
     }
     catch(Exception e)
     {
        return getLocationName; 
     }
        
    }
   
}
