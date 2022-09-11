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
 
public class clsFinYear {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colHistory=new HashMap();
    
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
    public clsFinYear() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("YEAR_FROM",new Variant(0));
        props.put("YEAR_TO",new Variant(0));
        props.put("OPEN_STATUS",new Variant(""));
        
        props.put("DONE_BY",new Variant("")); //Year Opend by. Used while creating new year
    }
    
    public static String getDBURL(int pCompanyID,int pFromYear) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strURL="";
        
        try {
            tmpConn=data.getCreatedConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT DATABASE_URL FROM D_COM_FIN_YEAR WHERE COMPANY_ID="+pCompanyID+" AND YEAR_FROM="+pFromYear);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strURL=rsTmp.getString("DATABASE_URL");
            }

            
        //tmpConn.close();
        stTmp.close();
        rsTmp.close();
            
            return strURL;
            
        }
        catch(Exception e) {
            return "";
        }
    }


    public static String getDBURL(int pCompanyID,int pFromYear,String pStartURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strURL="";
        
        try {
            tmpConn=data.getConn(pStartURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT DATABASE_URL FROM D_COM_FIN_YEAR WHERE COMPANY_ID="+pCompanyID+" AND YEAR_FROM="+pFromYear);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strURL=rsTmp.getString("DATABASE_URL");
            }

            
        //tmpConn.close();
        stTmp.close();
        rsTmp.close();
            
            return strURL;
            
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getSyncURL(int pCompanyID,int pFromYear) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String strURL="";
        
        try {
            tmpConn=data.getCreatedConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SYNC_URL FROM D_COM_FIN_YEAR WHERE COMPANY_ID="+pCompanyID+" AND YEAR_FROM="+pFromYear);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                strURL=rsTmp.getString("SYNC_URL");
            }

            
        //tmpConn.close();
        stTmp.close();
        rsTmp.close();
            
            return strURL;
            
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getCreatedConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_FIN_YEAR WHERE COMPANY_ID="+pCompanyID);
            Ready=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
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
        Statement stHistory,stTmp;
        ResultSet rsHistory,rsTmp;
        
        try {
            rsResultSet.first();
            
            //Generate new Sr. No.
            int CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            setAttribute("SR_NO", data.getMaxID(CompanyID,"D_COM_FIN_YEAR","SR_NO"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_NAME").getVal());
            rsResultSet.updateInt("SR_NO",(int)getAttribute("SR_NO").getVal());
            rsResultSet.updateInt("YEAR_FROM",(int)getAttribute("YEAR_FROM").getVal());
            rsResultSet.updateInt("YEAR_TO",(int)getAttribute("YEAR_TO").getVal());
            rsResultSet.updateString("OPEN_STATUS",(String)getAttribute("OPEN_STATUS").getObj());
            rsResultSet.insertRow();
            
            //Insert record into History
            //Get the max. sr. no.
            int nSrNo=(int)data.getMaxID(CompanyID, "D_COM_FIN_YEAR_HISTORY", "SR_NO");
            
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_FIN_YEAR_HISTORY");
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateInt("FIN_SR_NO",(int)getAttribute("SR_NO").getVal());
            rsHistory.updateInt("SR_NO",nSrNo);
            rsHistory.updateString("ACTION","O");
            rsHistory.updateInt("DONE_BY",(int)getAttribute("DONE_BY").getVal());
            rsHistory.updateString("DONE_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID,int pYearFrom,int pYearTo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND YEAR_FROM="+pYearFrom+" AND YEAR_TO="+pYearTo;
        clsFinYear ObjYear = new clsFinYear();
        ObjYear.Filter(strCondition,pCompanyID);
        return ObjYear;
    }
    
    
    public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp=null,rsHistory=null;
        Statement tmpStmt=null,stHistory=null;
        HashMap List=new HashMap();
        long Counter=0,Counter2=0;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_FIN_YEAR "+pCondition);
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsFinYear ObjFinYear =new clsFinYear();
                ObjFinYear.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjFinYear.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjFinYear.setAttribute("YEAR_FROM",rsTmp.getInt("YEAR_FROM"));
                ObjFinYear.setAttribute("YEAR_TO",rsTmp.getInt("YEAR_TO"));
                ObjFinYear.setAttribute("OPEN_STATUS",rsTmp.getString("OPEN_STATUS"));
                
                int CompanyID=rsTmp.getInt("COMPANY_ID");
                int SrNo=rsTmp.getInt("SR_NO");
                
                stHistory=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_FIN_YEAR_HISTORY WHERE COMPANY_ID="+CompanyID+" AND FIN_SR_NO="+SrNo);
                rsHistory.first();
                
                Counter2=0;
                while(!rsHistory.isAfterLast()&&rsHistory.getRow()>0) {
                    Counter2++;
                    clsFinYearHistory ObjHistory=new clsFinYearHistory();
                    
                    ObjHistory.setAttribute("COMPANY_ID",rsHistory.getInt("COMPANY_ID"));
                    ObjHistory.setAttribute("FIN_SR_NO",rsHistory.getInt("FIN_SR_NO"));
                    ObjHistory.setAttribute("SR_NO",rsHistory.getInt("SR_NO"));
                    ObjHistory.setAttribute("ACTION",rsHistory.getString("ACTION"));
                    ObjHistory.setAttribute("DONE_BY",rsHistory.getInt("DONE_BY"));
                    ObjHistory.setAttribute("DONE_DATE",rsHistory.getString("DONE_DATE"));
                    
                    ObjFinYear.colHistory.put(Long.toString(Counter2),ObjHistory);
                    rsHistory.next();
                }
                
                List.put(Long.toString(Counter),ObjFinYear);
                rsTmp.next();
            }
            
        //tmpConn.close();
        rsTmp.close();
        rsHistory.close();
        tmpStmt.close();
        stHistory.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_FIN_YEAR " + pCondition;
            Conn=data.getCreatedConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_FIN_YEAR WHERE COMPANY_ID="+pCompanyID;
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveFirst();
                return false;
            }
            else {
                Ready=true;
                MoveFirst();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean setData() {
        Statement stHistory;
        ResultSet rsHistory;
        int Counter=0;
        
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("SR_NO",rsResultSet.getInt("SR_NO"));
            setAttribute("YEAR_FROM",rsResultSet.getInt("YEAR_FROM"));
            setAttribute("YEAR_TO",rsResultSet.getInt("YEAR_TO"));
            setAttribute("OPEN_STATUS",rsResultSet.getString("OPEN_STATUS"));
            setAttribute("DB_NAME",rsResultSet.getString("DB_NAME"));
            
            colHistory.clear();
            
            int CompanyID=rsResultSet.getInt("COMPANY_ID");
            int SrNo=rsResultSet.getInt("SR_NO");
            
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_FIN_YEAR_HISTORY WHERE COMPANY_ID="+CompanyID+" AND FIN_SR_NO="+SrNo);
            rsHistory.first();
            
            Counter=0;
            while(!rsHistory.isAfterLast()&&rsHistory.getRow()>0) {
                Counter++;
                clsFinYearHistory ObjHistory=new clsFinYearHistory();
                
                ObjHistory.setAttribute("COMPANY_ID",rsHistory.getInt("COMPANY_ID"));
                ObjHistory.setAttribute("FIN_SR_NO",rsHistory.getInt("FIN_SR_NO"));
                ObjHistory.setAttribute("SR_NO",rsHistory.getInt("SR_NO"));
                ObjHistory.setAttribute("ACTION",rsHistory.getString("ACTION"));
                ObjHistory.setAttribute("DONE_BY",rsHistory.getInt("DONE_BY"));
                ObjHistory.setAttribute("DONE_DATE",rsHistory.getString("DONE_DATE"));
                
                colHistory.put(Integer.toString(Counter),ObjHistory);
                rsHistory.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
}
