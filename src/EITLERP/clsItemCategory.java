/*
 * clsItemCategory.java 
 *
 * Created on April 22, 2004, 9:02 AM
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

public class clsItemCategory {

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
    public clsItemCategory() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("CATEGORY_ID",new Variant(0));
        props.put("CATEGORY_DESC",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }

    public boolean LoadData(long pCompanyID)
    {
      Ready=false;
      try
        {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY CATEGORY_DESC");
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
            
            //Generate new Category ID
            setAttribute("CATEGORY_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_INV_CATEGORY_MASTER","CATEGORY_ID"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("CATEGORY_ID",(long)getAttribute("CATEGORY_ID").getVal());
            rsResultSet.updateString("CATEGORY_DESC",(String)getAttribute("CATEGORY_DESC").getObj());
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
            rsResultSet.updateString("CATEGORY_DESC",(String)getAttribute("CATEGORY_DESC").getObj());
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
            String lCID=Long.toString((long) getAttribute("CATEGORY_ID").getVal());
            
            String strQry = "DELETE FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID=" +lCompanyID+" AND CATEGORY_ID="+lCID;
                        
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
    
    public Object getObject(int pCompanyID,int pCategoryID) 
    {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND CATEGORY_ID="+Integer.toString(pCategoryID);
        clsItemCategory ObjCategory = new clsItemCategory();
        ObjCategory.Filter(strCondition);
        return ObjCategory;      
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
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_CATEGORY_MASTER "+pCondition);
            rsTmp.first();    
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsItemCategory ObjCategory =new clsItemCategory();
                ObjCategory.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjCategory.setAttribute("CATEGORY_ID",rsTmp.getInt("CATEGORY_ID"));
                ObjCategory.setAttribute("CATEGORY_DESC",rsTmp.getString("CATEGORY_DESC"));
                ObjCategory.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjCategory.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjCategory.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjCategory.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                List.put(Long.toString(Counter),ObjCategory);
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

    public static HashMap getCategoryList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        long Counter=0;
        
        tmpConn=data.getConn();
        
        try
        {
            String str = "";
            str = "SELECT COMPANY_ID,CATEGORY_ID,CATEGORY_DESC as CATEGORY_DESC, "+
                " CREATED_BY,CREATED_DATE ,MODIFIED_BY,MODIFIED_DATE "+
                " FROM D_INV_CATEGORY_MASTER "+ pCondition + "  AND CATEGORY_ID <> 2 " +
                " UNION "+
                " SELECT COMPANY_ID,CATEGORY_ID,CATEGORY_NAME as CATEGORY_DESC, "+
                " '' as CREATED_BY , '' as CREATED_DATE, '' as MODIFIED_BY, '' as MODIFIED_DATE "+
                " FROM D_INV_ITEM_CATEGORY " + pCondition + " "; 
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(str);
            rsTmp.first();    
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsItemCategory ObjCategory =new clsItemCategory();
                ObjCategory.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjCategory.setAttribute("CATEGORY_ID",rsTmp.getInt("CATEGORY_ID"));
                ObjCategory.setAttribute("CATEGORY_DESC",rsTmp.getString("CATEGORY_DESC"));
                ObjCategory.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjCategory.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjCategory.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjCategory.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                List.put(Long.toString(Counter),ObjCategory);
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
    
    public static HashMap getCategoryListEx(int groupCode) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        long Counter=0;
        
        tmpConn=data.getConn();
        
        try
        {
            String str = "";
            if(groupCode==1) {
                str="SELECT COMPANY_ID,CATEGORY_ID,CATEGORY_DESC as CATEGORY_DESC, "+
                "CREATED_BY,CREATED_DATE ,MODIFIED_BY,MODIFIED_DATE "+
                "FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID) + " " +
                "AND CATEGORY_ID="+groupCode +" AND CATEGORY_ID <> 2 "; 
            } else if(groupCode==2){
                str="SELECT COMPANY_ID,CATEGORY_ID,CATEGORY_NAME as CATEGORY_DESC, '' as CREATED_BY , '' as CREATED_DATE, '' as MODIFIED_BY, " +
                "'' as MODIFIED_DATE FROM D_INV_ITEM_CATEGORY WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID) ;
            }else if(groupCode==3) {
                String GCode="3,4,5,7,8";
                str="SELECT COMPANY_ID,CATEGORY_ID,CATEGORY_DESC as CATEGORY_DESC, "+
                "CREATED_BY,CREATED_DATE ,MODIFIED_BY,MODIFIED_DATE "+
                "FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID) + " " +
                "AND CATEGORY_ID IN ("+GCode +") AND CATEGORY_ID <> 2 "; 
            }else if(groupCode==4) {
                String GCode="9";
                str="SELECT COMPANY_ID,CATEGORY_ID,CATEGORY_DESC as CATEGORY_DESC, "+
                "CREATED_BY,CREATED_DATE ,MODIFIED_BY,MODIFIED_DATE "+
                "FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID) + " " +
                "AND CATEGORY_ID IN ("+GCode +") AND CATEGORY_ID <> 2 "; 
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(str);
            rsTmp.first();    
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsItemCategory ObjCategory =new clsItemCategory();
                ObjCategory.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjCategory.setAttribute("CATEGORY_ID",rsTmp.getInt("CATEGORY_ID"));
                ObjCategory.setAttribute("CATEGORY_DESC",rsTmp.getString("CATEGORY_DESC"));
                ObjCategory.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjCategory.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjCategory.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjCategory.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                List.put(Long.toString(Counter),ObjCategory);
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
    
 public boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_INV_CATEGORY_MASTER " + pCondition ;
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
            setAttribute("CATEGORY_ID",rsResultSet.getInt("CATEGORY_ID"));
            setAttribute("CATEGORY_DESC",rsResultSet.getString("CATEGORY_DESC"));
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
            String strSql = "SELECT * FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID) + " AND CATEGORY_ID=" + pCondition;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if (Stmt.execute(strSql))
            {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
                while(rsResultSet.next())
                {
                    System.out.println("--" + rsResultSet.getString("CATEGORY_ID"));
                    if(rsResultSet.getString("CATEGORY_ID").equals(pCondition))
                    {
                        populateprops();
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
    
    boolean populateprops() 
    {
        try
        {
            setAttribute("COMPANY_ID",rsResultSet.getLong("COMPANY_ID"));            
            setAttribute("CATEGORY_ID",rsResultSet.getLong("CATEGORY_ID"));
            setAttribute("CATEGORY_DESC",rsResultSet.getString("CATEGORY_DESC"));
            setAttribute("CREATED_BY",rsResultSet.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));    
            
            return true;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }
    } 
    
    public static String getCategoryDesc(int pCompanyID,long pCategoryID)    
{
    Connection tmpConn;
    Statement stTmp;
    ResultSet rsTmp;
    String CategoryDesc="";
    
    try
    {
       tmpConn=data.getConn();
       stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
       rsTmp=stTmp.executeQuery("SELECT CATEGORY_DESC FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND CATEGORY_ID="+pCategoryID);
       rsTmp.first();
       
       if(rsTmp.getRow()>0)
       {
          CategoryDesc=rsTmp.getString("CATEGORY_DESC");
       }

    //tmpConn.close();
    stTmp.close();
    rsTmp.close();
       
       return CategoryDesc;
    }
    catch(Exception e)
    {
       return "";
    }
}   
    
public static boolean IsValidCategoryID(int pCompanyID,long pCategoryID)
{
    Connection tmpConn;
    Statement Stmt;
    ResultSet rsTmp;
    
    try
    {
       tmpConn=data.getConn();
       Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
       rsTmp=Stmt.executeQuery("SELECT CATEGORY_ID FROM D_INV_CATEGORY_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND CATEGORY_ID="+pCategoryID);
       rsTmp.first();
       if(rsTmp.getRow()>0)
       {
    //tmpConn.close();
    Stmt.close();
    rsTmp.close();
           
         return true;  
       }
       else
       {
    //tmpConn.close();
    Stmt.close();
    rsTmp.close();
           
         return false;
       }
    }
    catch(Exception e)
    {
      return false; 
    }
}


}
