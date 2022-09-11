/*
 * clsMenuMaster.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsMenuMaster {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    public HashMap colMenufun=new HashMap();    
    private HashMap props;
    
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
    
    /** Creates new clsMenuMaster */
    public clsMenuMaster() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("MENU_ID",new Variant(0));
        props.put("MENU_TYPE",new Variant(""));
        props.put("PARENT_ID",new Variant(0));
        props.put("MENU_CAPTION",new Variant(""));
        props.put("MODULE_ID",new Variant(0));
        props.put("CLASS_NAME",new Variant(""));
        props.put("PACKAGE",new Variant(""));
        props.put("ACCESS_TYPE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        try {
            String strSql = "SELECT * FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+pCompanyID+" ORDER BY MENU_ID";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            MoveFirst();
            return true;            
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }    
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        }catch(Exception e) {
            
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        }catch(Exception e) {
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
            }else {
                rsResultSet.next();
                if(rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            }else {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert() {
        Statement stTmp,stHistory,stMenuFunction;
        ResultSet rsTmp,rsHistory,rsMenuFunction;
        int pCompanyID=getAttribute("COMPANY_ID").getInt();
        try {                        
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateInt("MENU_ID",getAttribute("MENU_ID").getInt());
            rsResultSet.updateString("MENU_TYPE",getAttribute("MENU_TYPE").getString());
            rsResultSet.updateInt("PARENT_ID",getAttribute("PARENT_ID").getInt());
            rsResultSet.updateString("MENU_CAPTION",getAttribute("MENU_CAPTION").getString());
            rsResultSet.updateInt("MODULE_ID",getAttribute("MODULE_ID").getInt());
            rsResultSet.updateString("CLASS_NAME",getAttribute("CLASS_NAME").getString());
            rsResultSet.updateString("PACKAGE",getAttribute("PACKAGE").getString());
            rsResultSet.updateString("ACCESS_TYPE",getAttribute("ACCESS_TYPE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            //insert into Menu Function            
            stMenuFunction=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsMenuFunction=stMenuFunction.executeQuery("SELECT * FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID=" + pCompanyID+" LIMIT 1");
            
            for(int l=1;l<=colMenufun.size();l++) {
                clsMenuFunction ObjMenuFun=(clsMenuFunction)colMenufun.get(Integer.toString(l));
                rsMenuFunction.first();
                rsMenuFunction.moveToInsertRow();
                rsMenuFunction.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsMenuFunction.updateInt("MENU_ID",getAttribute("MENU_ID").getInt());
                rsMenuFunction.updateInt("SR_NO",ObjMenuFun.getAttribute("SR_NO").getInt());
                rsMenuFunction.updateInt("FUNCTION_ID",ObjMenuFun.getAttribute("FUNCTION_ID").getInt());
                rsMenuFunction.updateString("FUNCTION_NAME",ObjMenuFun.getAttribute("FUNCTION_NAME").getString());
                rsMenuFunction.updateBoolean("CHANGED",true);
                rsMenuFunction.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsMenuFunction.insertRow();
            }
            
            MoveLast();
            return true;
        }catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Updates current record
    public boolean Update() {
        Statement stTmp,stHistory,stMenuFunction;
        ResultSet rsTmp,rsHistory,rsMenuFunction;        
        try {            
            rsResultSet.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateInt("MENU_ID",getAttribute("MENU_ID").getInt());
            rsResultSet.updateString("MENU_TYPE",getAttribute("MENU_TYPE").getString());
            rsResultSet.updateInt("PARENT_ID",getAttribute("PARENT_ID").getInt());
            rsResultSet.updateString("MENU_CAPTION",getAttribute("MENU_CAPTION").getString());
            rsResultSet.updateLong("MODULE_ID",getAttribute("MODULE_ID").getLong());
            rsResultSet.updateString("CLASS_NAME",getAttribute("CLASS_NAME").getString());
            rsResultSet.updateString("PACKAGE",getAttribute("PACKAGE").getString());
            rsResultSet.updateString("ACCESS_TYPE",getAttribute("ACCESS_TYPE").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            // DELETE FROM MENU FUNCTION TABLE THEN INSERT UPDATED RECORD
            data.Execute("DELETE FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID=" +getAttribute("COMPANY_ID").getInt()+" AND MENU_ID="+getAttribute("MENU_ID").getInt());
            stMenuFunction=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);            
            rsMenuFunction=stMenuFunction.executeQuery("SELECT * FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID=" +getAttribute("COMPANY_ID").getInt()+" LIMIT 1");
            
            for(int l=1;l<=colMenufun.size();l++) {
                clsMenuFunction ObjMenuFun=(clsMenuFunction)colMenufun.get(Integer.toString(l));
                rsMenuFunction.first();
                rsMenuFunction.moveToInsertRow();
                rsMenuFunction.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsMenuFunction.updateInt("MENU_ID",getAttribute("MENU_ID").getInt());
                rsMenuFunction.updateInt("SR_NO",ObjMenuFun.getAttribute("SR_NO").getInt());
                rsMenuFunction.updateInt("FUNCTION_ID",ObjMenuFun.getAttribute("FUNCTION_ID").getInt());
                rsMenuFunction.updateString("FUNCTION_NAME",ObjMenuFun.getAttribute("FUNCTION_NAME").getString());
                rsMenuFunction.updateBoolean("CHANGED",true);
                rsMenuFunction.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsMenuFunction.insertRow();
            }
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    //Deletes current record
    public boolean Delete() {
        try {                        
            String strSQL="DELETE FROM D_COM_MENU_MASTER WHERE COMPANY_ID="+getAttribute("COMPANY_ID").getInt()+" AND MENU_ID="+getAttribute("MENU_ID").getInt();
            data.Execute(strSQL);
            MoveLast();
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,String pMenuID) {
        String strCondition = " WHERE COMPANY_ID=" +pCompanyID+" AND MENU_ID="+pMenuID;
        clsMenuMaster ObjItem= new clsMenuMaster();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    public boolean Filter(String pCondition) {
        try {
            String strSql = "SELECT * FROM D_COM_MENU_MASTER WHERE COMPANY_ID=2 AND " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if (rsResultSet.getRow()>0) {
                MoveFirst();
            }
            
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        ResultSet rsTmp,rsMenuFunction;
        Statement tmpStmt,stMenuFunction;
        int RevNo=0;
        int Counter=0;
        
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("MENU_ID",rsResultSet.getInt("MENU_ID"));
            setAttribute("MENU_TYPE",rsResultSet.getString("MENU_TYPE"));
            setAttribute("PARENT_ID",rsResultSet.getInt("PARENT_ID"));
            setAttribute("MENU_CAPTION",rsResultSet.getString("MENU_CAPTION"));
            setAttribute("MODULE_ID",rsResultSet.getLong("MODULE_ID"));
            setAttribute("CLASS_NAME",rsResultSet.getString("CLASS_NAME"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            setAttribute("CHANGED",rsResultSet.getBoolean("CHANGED"));
            setAttribute("PACKAGE",rsResultSet.getString("PACKAGE"));
            setAttribute("ACCESS_TYPE",rsResultSet.getString("ACCESS_TYPE"));
            
            stMenuFunction=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);            
            rsMenuFunction=stMenuFunction.executeQuery("SELECT * FROM D_COM_MENU_FUNCTION WHERE COMPANY_ID=" +rsResultSet.getInt("COMPANY_ID")+" AND MENU_ID =" + rsResultSet.getInt("MENU_ID"));
            colMenufun.clear();
            rsMenuFunction.first();
            while((!rsMenuFunction.isAfterLast())&&(rsMenuFunction.getRow()>0)) { 
                clsMenuFunction ObjMenuFunc=new clsMenuFunction();
                ObjMenuFunc.setAttribute("COMPANY_ID",rsMenuFunction.getInt("COMPANY_ID"));
                ObjMenuFunc.setAttribute("SR_NO",rsMenuFunction.getInt("SR_NO"));
                ObjMenuFunc.setAttribute("MENU_ID",rsMenuFunction.getInt("MENU_ID"));
                ObjMenuFunc.setAttribute("FUNCTION_ID",rsMenuFunction.getInt("FUNCTION_ID"));
                ObjMenuFunc.setAttribute("FUNCTION_NAME",rsMenuFunction.getString("FUNCTION_NAME"));
                
                colMenufun.put(Integer.toString(colMenufun.size()+1),ObjMenuFunc);
                rsMenuFunction.next();
            }
            
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
}
