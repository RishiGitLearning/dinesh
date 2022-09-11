/*
 * clsShippingAddress.java
 *
 * Created on April 17, 2004, 10:36 AM
 */

package EITLERP;

import java.util.*;
import java.sql.*;
import java.net.*;

 

/** 
 *
 * @author  jadave
 * @version 
 */

public class clsShippingAddress {

    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;

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

    
    /** Creates new clsShippingAddress */
    public clsShippingAddress() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("SHIP_ID",new Variant(0));
        props.put("ADD1",new Variant(""));
        props.put("ADD2",new Variant(""));
        props.put("ADD3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("STATE",new Variant(""));
        props.put("COUNTRY",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }

    public boolean LoadData(int pCompanyID)
    {
      Ready=false;
      try
        {
            String strQry = "SELECT * FROM D_COM_SHIPING WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " ORDER BY SHIP_ID";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strQry);
            Ready=true;
            MoveFirst();
            return true;
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
            rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            setAttribute("SHIP_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_SHIPING","SHIP_ID"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("SHIP_ID",(long) getAttribute("SHIP_ID").getVal());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
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
        catch(Exception e)
        {
            LastError= e.getMessage();
            return false;
        }   
    }

    
    
    //Updates current record
    public boolean Update() 
    { 
        try
        {         
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("STATE",(String)getAttribute("STATE").getObj());
            rsResultSet.updateString("COUNTRY",(String)getAttribute("COUNTRY").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());            
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            return true;
        }                
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }           
    }
    

    //Deletes current record
    public boolean Delete()
    {
        try
        {   
            String strQry = "DELETE FROM D_COM_SHIPING WHERE COMPANY_ID=" + getAttribute("COMPANY_ID").getVal() + " AND SHIP_ID=" + getAttribute("SHIP_ID").getVal();
                        
            data.Execute(strQry);
            LoadData((int)getAttribute("COMPANY_ID").getVal());
            
            return true;         
        }                
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }        
    }
    
    public static Object getObject(int pCompanyID,int pShipID) 
    {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND SHIP_ID="+pShipID;
        clsShippingAddress ObjShipping = new clsShippingAddress();
        ObjShipping.Filter(strCondition);
        return ObjShipping;      
    }
    
    
    public static HashMap getList(String pCondition) 
    {
        ResultSet rsTmp;
        Statement tmpStmt;
        Connection tmpConn;
           
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_SHIPING " + pCondition);

            Counter=0;
            while(rsTmp.next())
            {
                Counter=Counter+1;
                clsShippingAddress ObjShipping = new clsShippingAddress();
                ObjShipping.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjShipping.setAttribute("SHIP_ID",rsTmp.getInt("SHIP_ID"));
                ObjShipping.setAttribute("ADD1",rsTmp.getString("ADD1"));
                ObjShipping.setAttribute("ADD2",rsTmp.getString("ADD2"));
                ObjShipping.setAttribute("ADD3",rsTmp.getString("ADD3"));
                ObjShipping.setAttribute("CITY",rsTmp.getString("CITY"));
                ObjShipping.setAttribute("STATE",rsTmp.getString("STATE"));
                ObjShipping.setAttribute("PINCODE",rsTmp.getString("PINCODE"));
                ObjShipping.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjShipping.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjShipping.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjShipping.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));                    
                
                List.put(Long.toString(Counter),ObjShipping);
            }
            
        rsTmp.close();
        tmpStmt.close();
        //tmpConn.close();
            
        }
        catch(Exception e)
        {
            //LastError=e.getMessage();
        }
        
        return List; 
    }
    
    
    boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_COM_SHIPING " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement();           
            
            
            if (Stmt.execute(strSql))
            {                
                rsResultSet = Stmt.getResultSet();                
                Ready=MoveFirst();
            }
            else
            {  
                Ready=false;
            }
           
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
            setAttribute("SHIP_ID",rsResultSet.getInt("SHIP_ID"));
            setAttribute("ADD1",rsResultSet.getString("ADD1"));
            setAttribute("ADD2",rsResultSet.getString("ADD2"));
            setAttribute("ADD3",rsResultSet.getString("ADD3"));
            setAttribute("CITY",rsResultSet.getString("CITY"));
            setAttribute("STATE",rsResultSet.getString("STATE"));
            setAttribute("COUNTRY",rsResultSet.getString("COUNTRY"));
            setAttribute("PINCODE",rsResultSet.getString("PINCODE"));               
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
    
    
}
