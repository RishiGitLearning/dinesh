/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */
 
package EITLERP;

import java.util.*;
import java.sql.*;
import java.net.*;

/**  
 *
 * @author  nrpithva
 * @version 
 */ 

public class clsBusinessObject {

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
    public clsBusinessObject() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant());
    }

    public boolean LoadData()
    {
      Ready=false;
      try
        {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("Query");
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
            setAttribute("COMPANY_ID", data.getMaxID(1,"TABLE","FIELD"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_NAME").getVal());

            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
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
            rsResultSet.updateString("COMPANY_NAME",(String) getAttribute("COMPANY_NAME").getObj());
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
            String strQry = "DELETE FROM D_COM_COMPANY_MASTER WHERE COMPANY_ID=" + getAttribute("COMPANY_ID").getVal();
                        
            if (data.Execute(strQry))
            {
                rsResultSet.refreshRow();
                return MoveLast();                
            }
            else
            {
                return false;
            }           
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
        clsCompany ObjCompany = new clsCompany();
        ObjCompany.Filter(strCondition);
        return ObjCompany;      
    }
    
    
    Object getList(String pCondition) {
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            tmpStmt=Conn.createStatement();

            rsTmp=tmpStmt.executeQuery("SELECT * FROM COMPANY_MASTER "+pCondition);

            Counter=0;
            while(rsTmp.next())
            {
                Counter=Counter+1;
                clsCompany ObjCompany =new clsCompany();
                ObjCompany.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                List.put(Long.toString(Counter),ObjCompany);
            }
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
        }
        
        return List; 
    }
    
    
    boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_COM_COMPANY_MASTER " + pCondition ;
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
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getDate("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getDate("MODIFIED_DATE"));    
            return true;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
}
