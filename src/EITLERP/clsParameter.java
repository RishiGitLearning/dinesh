/*
 * clsParameter.java
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

public class clsParameter {

    public String LastError="";
    public String strSql;
    private ResultSet rsResultSet;
    private Connection conn;
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

    
    /** Creates new clsParameter */
    public clsParameter() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("PARA_ID",new Variant(""));
        props.put("PARA_CODE",new Variant(0));
        props.put("PARA_DESC",new Variant(""));
        props.put("DESC",new Variant(""));
        props.put("VALUE",new Variant(0));
        props.put("REMARKS",new Variant(""));
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
            conn=data.getConn();
            Stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSql = "Select * From D_COM_PARAMETER_MAST WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if (Stmt.execute(strSql))
            {
                rsResultSet = Stmt.getResultSet();
                Ready = MoveFirst();
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
        //conn.close();
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
            //rsResultSet.first();
            Statement stmtemp = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);            
            
            ResultSet rsTmp;
            String paraid = (String) getAttribute("PARA_ID").getObj();
            String rsTmp1 = "SELECT MAX(PARA_CODE) AS PARACODE FROM D_COM_PARAMETER_MAST WHERE PARA_ID='"+paraid.trim()+"' AND COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID);
	    rsTmp = stmtemp.executeQuery(rsTmp1);
            
            rsTmp.first();
            int gMaxID = rsTmp.getInt("PARACODE") + 1;
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("PARA_ID",(String) getAttribute("PARA_ID").getObj());
            rsResultSet.updateInt("PARA_CODE",(int) gMaxID);
            rsResultSet.updateString("DESC",(String) getAttribute("DESC").getObj());
            rsResultSet.updateDouble("VALUE",(double) getAttribute("VALUE").getVal());
            rsResultSet.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
//            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
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
            rsResultSet.updateString("PARA_DESC",(String) getAttribute("PARA_DESC").getObj());
            rsResultSet.updateString("DESC",(String) getAttribute("DESC").getObj());
            rsResultSet.updateLong("VALUE",(long) getAttribute("VALUE").getVal());
            rsResultSet.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
//            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
//            rsResultSet.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
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
            String strQry = "DELETE FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID) ;
            data.Execute(strQry);
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
        clsParameter objParameter = new clsParameter();
        objParameter.Filter(strCondition);
        return objParameter;      
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        long Counter=0;
        tmpConn=data.getConn();
        
        try
        {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_PARAMETER_MAST "+pCondition);
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsParameter objParameter =new clsParameter();
                objParameter.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                objParameter.setAttribute("PARA_ID",rsTmp.getString("PARA_ID"));
                objParameter.setAttribute("PARA_CODE",rsTmp.getInt("PARA_CODE"));
                objParameter.setAttribute("DESC",rsTmp.getString("DESC"));
                objParameter.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                List.put(Long.toString(Counter),objParameter);
                rsTmp.next();
            }
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
            
            return List;
        }
        catch(Exception e)
        {
            return List;
        }
    }
    
public static String getParaDescription(int pCompanyID,String pParaID,int pParaCode)    
{
    Connection tmpConn;
    Statement tmpStmt;
    ResultSet rsTmp;
    String strParaDesc="";
    
    try
    {
        tmpConn=data.getConn();
        tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID="+pCompanyID+" AND PARA_ID='"+pParaID+"' AND PARA_CODE="+pParaCode);
        rsTmp.first();
       
        if(rsTmp.getRow()>0)
        {
          strParaDesc=rsTmp.getString("DESC");  
        }
        
    //tmpConn.close();
    tmpStmt.close();
    rsTmp.close();

        return strParaDesc;
    }
    catch(Exception e)
    {
        return strParaDesc;
        
    }
}

public static String getParaDescription(int pCompanyID,String pParaID,int pParaCode,String pURL)    
{
    Connection tmpConn;
    Statement tmpStmt;
    ResultSet rsTmp;
    String strParaDesc="";
    
    try
    {
        tmpConn=data.getConn(pURL);
        tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID="+pCompanyID+" AND PARA_ID='"+pParaID+"' AND PARA_CODE="+pParaCode);
        rsTmp.first();
       
        if(rsTmp.getRow()>0)
        {
          strParaDesc=rsTmp.getString("DESC");  
        }

    //tmpConn.close();
    tmpStmt.close();
    rsTmp.close();
        
        return strParaDesc;
    }
    catch(Exception e)
    {
        return strParaDesc;
        
    }
}


    public boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_COM_PARAMETER_MAST " + pCondition ;
            conn=data.getConn();
            Stmt=conn.createStatement();           
            
            
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
            setAttribute("PARA_ID",rsResultSet.getString("PARA_ID"));
            setAttribute("PARA_CODE",rsResultSet.getInt("PARA_CODE"));
            setAttribute("DESC",rsResultSet.getString("DESC"));
            setAttribute("PARA_DESC",rsResultSet.getString("PARA_DESC"));
            setAttribute("VALUE",rsResultSet.getLong("VALUE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
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