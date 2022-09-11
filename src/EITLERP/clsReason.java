/*
 * clsReason.java
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
 * @author  nhpatel
 * @version 
 */

public class clsReason {

    public String LastError="";
    public String strSql;
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

    
    /** Creates new clsReason */
    public clsReason() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("REASON_ID",new Variant(0));
        props.put("REASON_DESC",new Variant(""));
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
            strSql = "SELECT * FROM D_COM_REASON_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if (Stmt.execute(strSql))
            {
                rsResultSet = Stmt.getResultSet();
                Ready = MoveFirst();
                return Ready;
            }    
            else
                Ready = false;
                return Ready;
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
            rsResultSet.moveToInsertRow();
            setAttribute("REASON_ID",data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_REASON_MASTER","REASON_ID"));
            
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("REASON_ID",(int) getAttribute("REASON_ID").getVal());
            rsResultSet.updateString("REASON_DESC",(String) getAttribute("REASON_DESC").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
//            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
            rsResultSet.updateString("REASON_DESC",(String) getAttribute("REASON_DESC").getObj());
//            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
//            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
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
         long ReasonID =(int) getAttribute("REASON_ID").getVal();
         String stmt = "DELETE FROM D_COM_REASON_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID) + " And REASON_ID="+ReasonID;
         data.Execute(stmt);
         return true;                
      }                
      catch(Exception e)
      {
         LastError = e.getMessage();
         return false;
      }        
    }
    
    public Object getObject(int pCompanyID,int pREASONID) 
    {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) +" AND REASON_ID=" + Integer.toString(pREASONID);
        clsReason ObjReason = new clsReason();
        ObjReason.Filter(strCondition);
        return ObjReason;      
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();

        tmpConn= data.getConn();
        long Counter=0;
        
        try
        {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_REASON_MASTER "+pCondition);
            rsTmp.first();    
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsReason ObjReason=new clsReason();
                
                ObjReason.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjReason.setAttribute("REASON_ID",rsTmp.getInt("REASON_ID"));
                ObjReason.setAttribute("REASON_DESC",rsTmp.getString("REASON_DESC"));
                
                List.put(Long.toString(Counter),ObjReason);
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
    
    
    public boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_COM_DEPT_MASTER " + pCondition ;
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
            setAttribute("REASON_ID",rsResultSet.getInt("REASON_ID"));
            setAttribute("REASON_DESC",rsResultSet.getString("REASON_DESC"));
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
            String strSql = "SELECT * FROM D_COM_REASON_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID);
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if (Stmt.execute(strSql))
            {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
                while(! rsResultSet.isAfterLast())
                {
                    if(rsResultSet.getString("REASON_ID").equals(pCondition))
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
    
}
