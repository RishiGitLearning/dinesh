/*
 * clsModules.java
 *
 * Created on April 13, 2004, 10:14 AM
 */

package sdml.felt.commonUI;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
/**
 *
 * @author  nhpatel
 * @version
 */
public class clsModule {
    
    /** Creates new clsModules */
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
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
    
    /** Creates new clsBusinessObject */
    public clsModule() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("MODULE_DESC",new Variant(""));
        props.put("MAINTAIN_APPROVAL",new Variant(false));
        props.put("USE_APPROVAL",new Variant(false));
        props.put("DOC_NO_FIELD",new Variant(""));
        props.put("DOC_DATE_FIELD",new Variant(""));
        props.put("PACKAGE",new Variant(""));
        props.put("HEADER_TABLE_NAME",new Variant(""));
        props.put("DETAIL_TABLE_NAME",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);            
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_MODULES WHERE COMPANY_ID="+SDMLERPGLOBAL.gCompanyID+" AND PACKAGE='FELT_SALES'  ORDER BY MODULE_ID");
            MoveFirst();
            return true;
        }catch(Exception e) {
            LastError=e.getMessage();
            e.printStackTrace();
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
        try {
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",SDMLERPGLOBAL.gCompanyID);
            rsResultSet.updateInt("MODULE_ID",Integer.parseInt(getAttribute("MODULE_ID").getString()));
            rsResultSet.updateString("MODULE_DESC",(String)getAttribute("MODULE_DESC").getObj());
            rsResultSet.updateString("HEADER_TABLE_NAME",(String)getAttribute("HEADER_TABLE_NAME").getObj());
            rsResultSet.updateString("DETAIL_TABLE_NAME",(String)getAttribute("DETAIL_TABLE_NAME").getObj());
            rsResultSet.updateString("DOC_NO_FIELD",(String)getAttribute("DOC_NO_FIELD").getObj());
            rsResultSet.updateString("DOC_DATE_FIELD",(String)getAttribute("DOC_DATE_FIELD").getObj());
            rsResultSet.updateString("PACKAGE",(String)getAttribute("PACKAGE").getObj());
            rsResultSet.updateBoolean("USE_APPROVAL",true);
            rsResultSet.updateBoolean("MAINTAIN_STOCK",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
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
        try {
            rsResultSet.updateInt("COMPANY_ID",SDMLERPGLOBAL.gCompanyID);
            rsResultSet.updateInt("MODULE_ID",Integer.parseInt(getAttribute("MODULE_ID").getString()));
            rsResultSet.updateString("MODULE_DESC",(String)getAttribute("MODULE_DESC").getObj());
            rsResultSet.updateString("HEADER_TABLE_NAME",(String)getAttribute("HEADER_TABLE_NAME").getObj());
            rsResultSet.updateString("DETAIL_TABLE_NAME",(String)getAttribute("DETAIL_TABLE_NAME").getObj());
            rsResultSet.updateString("DOC_NO_FIELD",(String)getAttribute("DOC_NO_FIELD").getObj());
            rsResultSet.updateString("DOC_DATE_FIELD",(String)getAttribute("DOC_DATE_FIELD").getObj());
            rsResultSet.updateString("PACKAGE",(String)getAttribute("PACKAGE").getObj());
            rsResultSet.updateBoolean("USE_APPROVAL",true);
            rsResultSet.updateBoolean("MAINTAIN_STOCK",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean setData() {
       try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("MODULE_ID",rsResultSet.getInt("MODULE_ID"));
            setAttribute("MODULE_DESC",rsResultSet.getString("MODULE_DESC"));
            setAttribute("HEADER_TABLE_NAME",rsResultSet.getString("HEADER_TABLE_NAME"));
            setAttribute("DETAIL_TABLE_NAME",rsResultSet.getString("DETAIL_TABLE_NAME"));
            setAttribute("DOC_NO_FIELD",rsResultSet.getString("DOC_NO_FIELD"));
            setAttribute("DOC_DATE_FIELD",rsResultSet.getString("DOC_DATE_FIELD"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            setAttribute("CHANGED",rsResultSet.getBoolean("CHANGED"));
            setAttribute("PACKAGE",rsResultSet.getString("PACKAGE"));
            setAttribute("USE_APPROVAL",rsResultSet.getBoolean("USE_APPROVAL"));
            setAttribute("MAINTAIN_STOCK",rsResultSet.getBoolean("MAINTAIN_STOCK"));
            
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Filter(String pCondition) {
        try {
            String strSql = "SELECT * FROM D_COM_MODULES WHERE COMPANY_ID=2  AND PACKAGE='FELT_SALES' AND " + pCondition ;
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
    
    //Deletes current record
    public boolean Delete() {
        try {
            data.Execute("DELETE FROM D_COM_MODULES WHERE COMPANY_ID=2  AND MODULE_ID="+getAttribute("MODULE_ID").getInt());
            MoveLast();
            return true;
        }catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
}
