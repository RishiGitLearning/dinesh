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

public class clsTaxColumn {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap colItems =new HashMap();
    
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
    public clsTaxColumn() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("TAX_ID",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("CAPTION",new Variant(""));
        props.put("VISIBLE_ON_FORM",new Variant(true));
        props.put("OPERATION",new Variant(""));
        props.put("INPUT",new Variant(0));
        props.put("FORMULA",new Variant(""));
        props.put("NO_CALCULATION",new Variant(false));
        props.put("USE_PERCENT",new Variant(true));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID));
            Ready=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
            
        }
        catch(Exception e) {
            
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
        ResultSet rsItem;
        Statement StmtItem;
        
        try {
            //Create Statement
            StmtItem=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsResultSet.first();
            //Generate new Tax ID
            setAttribute("TAX_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_TAX_HEADER","TAX_ID"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("TAX_ID",(long)getAttribute("TAX_ID").getVal());
            rsResultSet.updateLong("MODULE_ID",(long)getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("CAPTION",(String)getAttribute("CAPTION").getObj());
            rsResultSet.updateBoolean("VISIBLE_ON_FORM",(boolean)getAttribute("VISIBLE_ON_FORM").getBool());
            rsResultSet.updateString("OPERATION",(String)getAttribute("OPERATION").getObj());
            rsResultSet.updateLong("INPUT",(long)getAttribute("INPUT").getVal());
            rsResultSet.updateString("FORMULA",(String)getAttribute("FORMULA").getObj());
            rsResultSet.updateBoolean("NO_CALCULATION",(boolean)getAttribute("NO_CALCULATION").getBool());
            rsResultSet.updateBoolean("USE_PERCENT",(boolean)getAttribute("USE_PERCENT").getBool());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            rsItem=StmtItem.executeQuery("SELECT * FROM D_COM_TAX_DETAIL");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsTaxItem ObjItem=(clsTaxItem) colItems.get(Integer.toString(cnt));
                rsItem.moveToInsertRow();
                rsItem.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsItem.updateLong("TAX_ID",(long)getAttribute("TAX_ID").getVal());
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("FORMULA",(String)ObjItem.getAttribute("FORMULA").getObj());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
            }
            
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
        ResultSet rsItem;
        Statement StmtItem;
        
        try {
            StmtItem=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            long lnCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            long lTaxID=(long)getAttribute("TAX_ID").getVal();
            
            
            rsResultSet.updateLong("MODULE_ID",(long)getAttribute("MODULE_ID").getVal());
            rsResultSet.updateString("CAPTION",(String)getAttribute("CAPTION").getObj());
            rsResultSet.updateBoolean("VISIBLE_ON_FORM",(boolean)getAttribute("VISIBLE_ON_FORM").getBool());
            rsResultSet.updateString("OPERATION",(String)getAttribute("OPERATION").getObj());
            rsResultSet.updateLong("INPUT",(long)getAttribute("INPUT").getVal());
            rsResultSet.updateString("FORMULA",(String)getAttribute("FORMULA").getObj());
            rsResultSet.updateBoolean("NO_CALCULATION",getAttribute("NO_CALCULATION").getBool());
            rsResultSet.updateBoolean("USE_PERCENT",(boolean)getAttribute("USE_PERCENT").getBool());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            
            //First remove the old rows
            String mstrCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mstrTaxID=Long.toString((long)getAttribute("TAX_ID").getVal());
            
            data.Execute("DELETE FROM D_COM_TAX_DETAIL WHERE COMPANY_ID="+mstrCompanyID+" AND TAX_ID="+mstrTaxID);
            
            rsItem=StmtItem.executeQuery("SELECT * FROM D_COM_TAX_DETAIL");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsTaxItem ObjItem=(clsTaxItem) colItems.get(Integer.toString(cnt));
                rsItem.moveToInsertRow();
                rsItem.updateLong("COMPANY_ID",lnCompanyID);
                rsItem.updateLong("TAX_ID",lTaxID);
                rsItem.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("FORMULA",(String)ObjItem.getAttribute("FORMULA").getObj());
                rsItem.updateBoolean("CHANGED",true);
                rsItem.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItem.insertRow();
            }
            
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
            String lTaxID=Long.toString((long) getAttribute("TAX_ID").getVal());
            
            String strQry = "DELETE FROM D_COM_TAX_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND TAX_ID="+lTaxID;
            data.Execute(strQry);
            strQry = "DELETE FROM D_COM_TAX_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND TAX_ID="+lTaxID;
            data.Execute(strQry);
            
            LoadData(Integer.parseInt(lCompanyID));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public static Object getObject(int pCompanyID,int pTaxID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID);
        clsTaxColumn ObjTaxColumn = new clsTaxColumn();
        ObjTaxColumn.Filter(strCondition);
        return ObjTaxColumn;
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_TAX_HEADER "+pCondition);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsTaxColumn ObjTax=new clsTaxColumn();
                
                //Populate the user
                ObjTax.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjTax.setAttribute("TAX_ID",rsTmp.getLong("TAX_ID"));
                ObjTax.setAttribute("MODULE_ID",rsTmp.getLong("MODULE_ID"));
                ObjTax.setAttribute("CAPTION",rsTmp.getString("CAPTION"));
                ObjTax.setAttribute("VISIBLE_ON_FORM",rsTmp.getBoolean("VISIBLE_ON_FORM"));
                ObjTax.setAttribute("OPERATION",rsTmp.getString("OPERATION"));
                ObjTax.setAttribute("INPUT",rsTmp.getLong("INPUT"));
                ObjTax.setAttribute("FORMULA",rsTmp.getString("FORMULA"));
                ObjTax.setAttribute("NO_CALCULATION",rsTmp.getBoolean("NO_CALCULATION"));
                ObjTax.setAttribute("USE_PERCENT",rsTmp.getBoolean("USE_PERCENT"));
                
                //Now Populate the collection
                //first clear the collection
                ObjTax.colItems.clear();
                
                String mCompanyID=Long.toString((long)ObjTax.getAttribute("COMPANY_ID").getVal());
                String mTaxID=Long.toString((long)ObjTax.getAttribute("TAX_ID").getVal());
                
                tmpStmt2=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_COM_TAX_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND TAX_ID="+mTaxID);
                
                rsTmp2.first();
                
                if(rsTmp2.getRow()>0) {
                    int Counter2=0;
                    while(!rsTmp2.isAfterLast()) {
                        Counter2=Counter2+1;
                        clsTaxItem ObjItem=new clsTaxItem();
                        
                        ObjItem.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                        ObjItem.setAttribute("TAX_ID",rsTmp2.getLong("TAX_ID"));
                        ObjItem.setAttribute("ITEM_ID",rsTmp2.getString("ITEM_ID"));
                        ObjItem.setAttribute("FORMULA",rsTmp2.getString("FORMULA"));
                        
                        ObjTax.colItems.put(Long.toString(Counter2),ObjItem);
                        rsTmp2.next();
                        
                    }// Innser while
                }//If Condition
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjTax);
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            rsTmp2.close();
            tmpStmt2.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_TAX_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsResultSet=Stmt.executeQuery(strSql);
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveFirst();
                return false;
            }
            else {
                MoveFirst();
                Ready=true;
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    private boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            setAttribute("COMPANY_ID",rsResultSet.getLong("COMPANY_ID"));
            setAttribute("TAX_ID",rsResultSet.getLong("TAX_ID"));
            setAttribute("MODULE_ID",rsResultSet.getLong("MODULE_ID"));
            setAttribute("CAPTION",rsResultSet.getString("CAPTION"));
            setAttribute("VISIBLE_ON_FORM",rsResultSet.getBoolean("VISIBLE_ON_FORM"));
            setAttribute("OPERATION",rsResultSet.getString("OPERATION"));
            setAttribute("INPUT",rsResultSet.getLong("INPUT"));
            setAttribute("FORMULA",rsResultSet.getString("FORMULA"));
            setAttribute("NO_CALCULATION",rsResultSet.getBoolean("NO_CALCULATION"));
            setAttribute("USE_PERCENT",rsResultSet.getBoolean("USE_PERCENT"));
            
            //Now Populate the collection
            //first clear the collection
            colItems.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mTaxID=Long.toString((long) getAttribute("TAX_ID").getVal());
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_TAX_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND TAX_ID="+mTaxID);
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsTaxItem ObjItem=new clsTaxItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("TAX_ID",rsTmp.getLong("TAX_ID"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("FORMULA",rsTmp.getString("FORMULA"));
                
                colItems.put(Long.toString(Counter),ObjItem);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static String getFormula(int pCompanyID,int pTaxID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String lFormula="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT FORMULA FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID));
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lFormula=rsTmp.getString("FORMULA");
            }
            
            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            
            return lFormula;
        }
        catch(Exception e)
        { return "";}
    }
    
    
    public static boolean getInclude(int pCompanyID,int pTaxID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        boolean Include=false;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT NO_CALCULATION FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID));
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                Include=rsTmp.getBoolean("NO_CALCULATION");
            }
            
            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            
            return Include;
        }
        catch(Exception e)
        { return Include;}
    }
    
    
    public static String getFormula(int pCompanyID,int pTaxID,String pItemID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String lFormula="";
        Statement tmpStmt2=null;
        ResultSet rsTmp2=null;
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT FORMULA FROM D_COM_TAX_DETAIL WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID)+" AND ITEM_ID='"+pItemID+"'");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lFormula=rsTmp.getString("FORMULA");
            }
            else {
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT FORMULA FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID));
                rsTmp2.first();
                if(rsTmp2.getRow()>0) {
                    lFormula=rsTmp2.getString("FORMULA");
                }
            }
            
            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            tmpStmt2.close();
            rsTmp2.close();
            
            return lFormula;
        }
        catch(Exception e)
        { return "";}
    }
    
    
    public static String getCaption(int pCompanyID,int pTaxID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String lCaption="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT CAPTION FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID));
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lCaption=rsTmp.getString("CAPTION");
            }
            
            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            
            return lCaption;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getOperation(int pCompanyID,int pTaxID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String lCaption="";
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT OPERATION FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID));
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lCaption=rsTmp.getString("OPERATION");
            }
            
            //tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            
            return lCaption;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public boolean getUsePercentage(int pCompanyID,int pTaxID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        boolean lUse=false;
        
        try {
            tmpStmt=Conn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT USE_PERCENT FROM D_COM_TAX_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND TAX_ID="+Integer.toString(pTaxID));
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                lUse=rsTmp.getBoolean("USE_PERCENT");
            }
            return lUse;
        }
        catch(Exception e) {
            return false;
        }
    }
    
}
