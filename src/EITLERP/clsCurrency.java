/*
 * clsCurrency.java
 *
 * Created on April 14, 2004, 2:07 PM
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

public class clsCurrency {

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

    
    /** Creates new clsBusinessObject */
    public clsCurrency() 
    {
        LastError = "";
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("CURRENCY_ID",new Variant(0));
        props.put("CURRENCY_DESC",new Variant(""));
        props.put("CURRENCY_DATE",new Variant(""));
        props.put("CURRENCY_RATE",new Variant(0.0));
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
            String strSql = "SELECT * FROM D_COM_CURRENCY_MASTER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " ORDER BY CURRENCY_ID";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
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
            setAttribute("CURRENCY_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_CURRENCY_MASTER","CURRENCY_ID"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("CURRENCY_ID",(long) getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateString("CURRENCY_DESC",(String)getAttribute("CURRENCY_DESC").getObj());
            rsResultSet.updateString("CURRENCY_DATE",(String)getAttribute("CURRENCY_DATE").getObj());
            rsResultSet.updateFloat("CURRENCY_RATE",(float) getAttribute("CURRENCY_RATE").getVal());            
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();           
            
            //MoveLast();
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
            rsResultSet.updateString("CURRENCY_DESC",(String)getAttribute("CURRENCY_DESC").getObj());
            rsResultSet.updateString("CURRENCY_DATE",(String)getAttribute("CURRENCY_DATE").getObj());
            rsResultSet.updateFloat("CURRENCY_RATE",(float) getAttribute("CURRENCY_RATE").getVal());            
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
            String lCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String lCurrID=Long.toString((long) getAttribute("CURRENCY_ID").getVal());
            
            String strQry = "DELETE FROM D_COM_CURRENCY_MASTER WHERE COMPANY_ID=" + lCompanyID +" AND CURRENCY_ID=" + lCurrID;
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
    
    public Object getObject(int pCompanyID,int pCurrID) 
    {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND CURRENCY_ID=" + pCurrID;
        clsCurrency ObjCurrency = new clsCurrency();
        ObjCurrency.Filter(strCondition);
        return ObjCurrency;      
    }
    
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Statement tmpStmt;
        Connection tmpConn;
        
        tmpConn=data.getConn();
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            tmpStmt=tmpConn.createStatement();

            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_CURRENCY_MASTER "+pCondition);

            Counter=0;
            while(rsTmp.next())
            {
                Counter=Counter+1;
                clsCurrency ObjCurrency =new clsCurrency();
                ObjCurrency.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjCurrency.setAttribute("CURRENCY_ID",rsTmp.getInt("CURRENCY_ID"));
                ObjCurrency.setAttribute("CURRENCY_DESC",rsTmp.getString("CURRENCY_DESC"));
                ObjCurrency.setAttribute("CURRENCY_DATE",rsTmp.getString("CURRENCY_DATE"));
                ObjCurrency.setAttribute("CURRENCY_RATE",rsTmp.getFloat("CURRENCY_RATE"));
                ObjCurrency.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjCurrency.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjCurrency.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjCurrency.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));    
                
                List.put(Long.toString(Counter),ObjCurrency);
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
            String strSql = "SELECT * FROM D_COM_CURRENCY_MASTER " + pCondition ;
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
            setAttribute("CURRENCY_ID",rsResultSet.getInt("CURRENCY_ID"));            
            setAttribute("CURRENCY_DESC",rsResultSet.getString("CURRENCY_DESC"));
            setAttribute("CURRENCY_DATE",rsResultSet.getString("CURRENCY_DATE"));
            setAttribute("CURRENCY_RATE",rsResultSet.getFloat("CURRENCY_RATE"));
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
    
    public static double getCurrentRate(long pCompanyID,long pCurrencyID)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;        
                
        double dCurrentRate = 0.00;        
        String strSql = "SELECT * FROM D_COM_CURRENCY_MASTER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND CURRENCY_ID=" + Long.toString(pCurrencyID);
        
        try
        {               
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();           
            rsTmp = tmpStmt.executeQuery(strSql);
            rsTmp.first();
            dCurrentRate = rsTmp.getDouble("CURRENCY_RATE");                
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
            
        }
        catch(Exception e)
        {
        }
        
        return dCurrentRate;
    }

    public boolean setCurrentRate(long pCompanyID,long pCurrencyID,double pCurrencyRate,String pCurrencyDate)
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;                        
        
        String strSql = "UPDATE D_COM_CURRENCY_MASTER SET CURRENCY_RATE=" + Double.toString(pCurrencyRate) + " , CURRENCY_DATE="+ pCurrencyDate + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND CURRENCY_ID=" + Long.toString(pCurrencyID) ;
        
        try
        {               
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();                                   
            tmpStmt.executeUpdate(strSql);                        
            return true;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();            
            return false;
        }       
    }

}
