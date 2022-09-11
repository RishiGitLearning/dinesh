/*
 * clsBusinessObject.java
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
 
public class clsColumn {
    
    public String LastError="";
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
    
    
    /** Creates new clsBusinessObject */
    public clsColumn() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("HEADER_LINE",new Variant(""));
        props.put("COL_ORDER",new Variant(0));
        props.put("TAX_ID",new Variant(0));
        props.put("CAPTION",new Variant(""));
        props.put("VARIABLE_NAME",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_COLUMNS WHERE COMPANY_ID="+Integer.toString(pCompanyID));
            Ready=true;
            MoveFirst();
            return true;
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
            
            //Generate new no.
            
            setAttribute("SR_NO", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_COLUMNS","SR_NO"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("SR_NO",(long)getAttribute("SR_NO").getVal());
            rsResultSet.updateLong("MODULE_ID",(long)getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("HEADER_LINE",(String)getAttribute("HEADER_LINE").getObj());
            rsResultSet.updateLong("COL_ORDER",(long)getAttribute("COL_ORDER").getVal());
            rsResultSet.updateLong("TAX_ID",(long)getAttribute("TAX_ID").getVal());
            rsResultSet.updateString("CAPTION",(String)getAttribute("CAPTION").getObj());
            rsResultSet.updateString("VARIABLE_NAME",(String)getAttribute("VARIABLE_NAME").getObj());
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
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("SR_NO",(long)getAttribute("SR_NO").getVal());
            rsResultSet.updateLong("MODULE_ID",(long)getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("HEADER_LINE",(String)getAttribute("HEADER_LINE").getObj());
            rsResultSet.updateLong("COL_ORDER",(long)getAttribute("COL_ORDER").getVal());
            rsResultSet.updateLong("TAX_ID",(long)getAttribute("TAX_ID").getVal());
            rsResultSet.updateString("CAPTION",(String)getAttribute("CAPTION").getObj());
            rsResultSet.updateString("VARIABLE_NAME",(String)getAttribute("VARIABLE_NAME").getObj());
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
            String lCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String lSrNo=Long.toString((long) getAttribute("SR_NO").getVal());
            
            String strQry = "DELETE FROM D_COM_COLUMNS WHERE COMPANY_ID=" + lCompanyID +" AND SR_NO="+lSrNo;
            data.Execute(strQry);
            
            LoadData(Integer.parseInt(lCompanyID));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static Object getObject(int pCompanyID,int pSrNo)
    {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND SR_NO="+Integer.toString(pSrNo);
        clsColumn ObjColumn = new clsColumn();
        ObjColumn.Filter(strCondition);
        return ObjColumn;
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_COLUMNS "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) 
            {
                Counter=Counter+1;
                clsColumn ObjColumn =new clsColumn();
                ObjColumn.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjColumn.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjColumn.setAttribute("MODULE_ID",rsTmp.getLong("MODULE_ID"));
                ObjColumn.setAttribute("HEADER_LINE",rsTmp.getString("HEADER_LINE"));
                ObjColumn.setAttribute("COL_ORDER",rsTmp.getLong("COL_ORDER"));
                ObjColumn.setAttribute("TAX_ID",rsTmp.getLong("TAX_ID"));
                ObjColumn.setAttribute("CAPTION",rsTmp.getString("CAPTION"));
                ObjColumn.setAttribute("VARIABLE_NAME",rsTmp.getString("VARIABLE_NAME"));
                
                List.put(Long.toString(Counter),ObjColumn);
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
        try {
            String strSql = "SELECT * FROM D_COM_COLUMNS " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
            }
            else {
                Ready=false;
            }
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean setData() {
        try {
            setAttribute("COMPANY_ID",rsResultSet.getLong("COMPANY_ID"));
            setAttribute("SR_NO",rsResultSet.getLong("SR_NO"));
            setAttribute("MODULE_ID",rsResultSet.getLong("MODULE_ID"));
            setAttribute("HEADER_LINE",rsResultSet.getString("HEADER_LINE"));
            setAttribute("COL_ORDER",rsResultSet.getLong("COL_ORDER"));
            setAttribute("TAX_ID",rsResultSet.getLong("TAX_ID"));
            setAttribute("CAPTION",rsResultSet.getString("CAPTION"));
            setAttribute("VARIABLE_NAME",rsResultSet.getString("VARIABLE_NAME"));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }


  public static void UpdateSrNo(int pCompanyID,int pSrNo,int pColOrder) 
  {
    Connection tmpConn;
    Statement tmpStmt;
    String strSQL;
    
    try
    {
        tmpConn=data.getConn();
        tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        strSQL="UPDATE D_COM_COLUMNS SET COL_ORDER="+Integer.toString(pColOrder)+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SR_NO="+Integer.toString(pSrNo);
        tmpStmt.executeUpdate(strSQL);
        
        tmpStmt.close();
        //tmpConn.close();
        
    }
    catch(Exception e)  
    { }
  }

public int getTaxID(int pCompanyID,int pColID)
 {
     Connection tmpConn;
     Statement tmpStmt;
     ResultSet rsTmp;
     int lTaxID=0;
     
     try
     {
         tmpConn=data.getConn();
         tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
         rsTmp=tmpStmt.executeQuery("SELECT TAX_ID FROM D_COM_COLUMNS WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SR_NO="+Integer.toString(pColID));
         rsTmp.first();
         if(rsTmp.getRow()>0)
         {
            lTaxID=rsTmp.getInt("TAX_ID");
          }
       return lTaxID;         
     }
     catch(Exception e)
     {
         return 0;
     }
 }

public String getVariableName(int pCompanyID,int pColID)
 {
     Connection tmpConn;
     Statement tmpStmt;
     ResultSet rsTmp;
     String lVariable="";
     
     try
     {
         tmpConn=data.getConn();
         tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
         rsTmp=tmpStmt.executeQuery("SELECT VARIABLE_NAME FROM D_COM_COLUMNS WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SR_NO="+Integer.toString(pColID));
         rsTmp.first();
         if(rsTmp.getRow()>0)
         {
            lVariable=rsTmp.getString("VARIABLE_NAME");
         }
       return lVariable;         
     }
     catch(Exception e)
     {
         return "";
     }
 }


}
