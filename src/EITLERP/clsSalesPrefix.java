/*
 * clsModules.java
 *
 * Created on April 13, 2004, 10:14 AM
 */

package EITLERP;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.sql.*;

/**
 *
 * @author  nhpatel
 * @version
 */
public class clsSalesPrefix {
    
    /** Creates new clsFirstFree */
    
    public String LastError="";
    public String strSql;
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    public HashMap colArea;
    private HashMap props;
    public boolean Ready = false;
    private boolean HistoryView=false;
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
    public clsSalesPrefix() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("SR_NO",new Variant(0));
        props.put("USER_ID",new Variant(0));
        props.put("MODULE_ID",new Variant(0));
        props.put("FF_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(false));
        colArea = new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //strSql = "SELECT * FROM D_SAL_DOC_PREFIX WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" ORDER BY FF_ID";
            
            //strSql = "SELECT A.* FROM D_COM_USER_MASTER B  LEFT JOIN D_SAL_DOC_PREFIX A ON (A.USER_ID = B.USER_ID) WHERE B.DEPT_ID = 33 ORDER BY B.USER_ID";
            strSql = "SELECT A.COMPANY_ID,A.SR_NO,B.USER_ID,A.MODULE_ID,A.FF_ID,A.CREATED_BY,A.CREATED_DATE,A.MODIFIED_BY, "+
            "A.MODIFIED_DATE,A.CHANGED,A.CHANGED_DATE,A.CANCELLED,B.USER_TYPE "+
            "FROM D_COM_USER_MASTER B  LEFT JOIN D_SAL_DOC_PREFIX A ON (A.USER_ID = B.USER_ID)  "+
            "WHERE B.DEPT_ID = 33 "+
            "ORDER BY B.USER_ID ";
            
            
            rsResultSet=Stmt.executeQuery(strSql);
            HistoryView=false;
            Ready=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
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
        }
        catch(Exception e) {
            
        }
    }
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
        Statement stArea,stDocPrefix;
        ResultSet rsArea,rsDocPrefix;
        long pCompanyID;
        pCompanyID=(long)getAttribute("COMPANY_ID").getVal();
        try {
            
            
            
            
            //--------- Check for the Duplication of Login ID ------
            //    long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            
            stDocPrefix=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocPrefix = stDocPrefix.executeQuery("SELECT * FROM D_SAL_DOC_PREFIX LIMIT 1");
            
           String FFID = (String)getAttribute("FF_ID").getObj();
           
           int usertype = (int) getAttribute("USER_TYPE").getInt();
           int userid = Integer.parseInt(getAttribute("USER_ID").getString());
           data.Execute("UPDATE D_COM_USER_MASTER SET USER_TYPE = '"+usertype+"' WHERE USER_ID = '"+userid+"' ");
           if(!FFID.equals("86"))
           {   
            //----------- Save Header Record ----------------
            //rsDocPrefix.first();
            rsDocPrefix.moveToInsertRow();
            rsDocPrefix.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int SRNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_DOC_PREFIX WHERE MODULE_ID = 82 AND COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" ") + 1;
            rsDocPrefix.updateInt("SR_NO",SRNo);
            rsDocPrefix.updateInt("MODULE_ID",Integer.parseInt(getAttribute("MODULE_ID").getString()));
            rsDocPrefix.updateInt("USER_ID",Integer.parseInt(getAttribute("USER_ID").getString()));
            rsDocPrefix.updateInt("FF_ID",Integer.parseInt(getAttribute("FF_ID").getString()));
            rsDocPrefix.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getString()));
            rsDocPrefix.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsDocPrefix.updateString("MODIFIED_BY",String.valueOf(getAttribute("CREATED_BY").getString()));
            rsDocPrefix.updateString("MODIFIED_DATE",getAttribute("CREATED_DATE").getString());
            rsDocPrefix.updateBoolean("CHANGED",true);
            rsDocPrefix.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDocPrefix.updateBoolean("CANCELLED",false);
            rsDocPrefix.insertRow();
            //insert into Menu Function
           }
            
            
            stArea=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsArea=stArea.executeQuery("SELECT * FROM D_SAL_USER_AREA_MAPPING ORDER BY AREA_ID");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            //String nAreaID= getAttribute("AREA_ID").getString();
            int nUserID=Integer.parseInt(getAttribute("USER_ID").getString());
            //Now Insert records into detail table
            for(int i=1;i<=colArea.size();i++) {
                clsSalesAreaMapping ObjItems=(clsSalesAreaMapping) colArea.get(Integer.toString(i));
                String nAreaID= ObjItems.getAttribute("AREA_ID").getString();
                rsArea.moveToInsertRow();
                rsArea.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsArea.updateString("AREA_ID",nAreaID);
                rsArea.updateInt("SR_NO",i);
                rsArea.updateInt("USER_ID",nUserID);
                rsArea.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getString()));
                rsArea.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
                rsArea.updateString("MODIFIED_BY",String.valueOf(getAttribute("CREATED_BY").getString()));
                rsArea.updateString("MODIFIED_DATE",getAttribute("CREATED_DATE").getString());
                rsArea.updateBoolean("CHANGED",true);
                rsArea.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsArea.insertRow();
            }
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    //Updates current record
    public boolean Update() {
        Statement stArea,stDocPrefix;
        ResultSet rsArea,rsDocPrefix;
        long pCompanyID;
        pCompanyID=(long)getAttribute("COMPANY_ID").getVal();
        try {
            
            
            
            
            //--------- Check for the Duplication of Login ID ------
            //    long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            
            stDocPrefix=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocPrefix = stDocPrefix.executeQuery("SELECT * FROM D_SAL_DOC_PREFIX LIMIT 1");
           int usertype = (int) getAttribute("USER_TYPE").getInt();
           int userid = Integer.parseInt(getAttribute("USER_ID").getString());
           data.Execute("UPDATE D_COM_USER_MASTER SET USER_TYPE = '"+usertype+"' WHERE USER_ID = '"+userid+"' ");
           String FFID = (String)getAttribute("FF_ID").getObj();
           if(!FFID.equals("86"))
           {   
            //----------- Save Header Record ----------------
            //rsDocPrefix.first();
            rsDocPrefix.moveToInsertRow();
            rsDocPrefix.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            int SRNo=data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_DOC_PREFIX WHERE MODULE_ID = 82 AND COMPANY_ID ="+EITLERPGLOBAL.gCompanyID+" ") + 1;
            rsDocPrefix.updateInt("SR_NO",SRNo);
            rsDocPrefix.updateInt("MODULE_ID",Integer.parseInt(getAttribute("MODULE_ID").getString()));
            rsDocPrefix.updateInt("USER_ID",Integer.parseInt(getAttribute("USER_ID").getString()));
            rsDocPrefix.updateInt("FF_ID",Integer.parseInt(getAttribute("FF_ID").getString()));
            rsDocPrefix.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getString()));
            rsDocPrefix.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDocPrefix.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getString()));
            rsDocPrefix.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDocPrefix.updateBoolean("CHANGED",true);
            rsDocPrefix.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDocPrefix.updateBoolean("CANCELLED",false);
            rsDocPrefix.updateRow();
            //insert into Menu Function
           }
            int nUserID=Integer.parseInt(getAttribute("USER_ID").getString());
            data.Execute("DELETE FROM D_SAL_USER_AREA_MAPPING WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND USER_ID='"+nUserID+"'");
            stArea=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsArea=stArea.executeQuery("SELECT * FROM D_SAL_USER_AREA_MAPPING ORDER BY AREA_ID");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            //String nAreaID= getAttribute("AREA_ID").getString();
            
            //Now Insert records into detail table
            for(int i=1;i<=colArea.size();i++) {
                clsSalesAreaMapping ObjItems=(clsSalesAreaMapping) colArea.get(Integer.toString(i));
                String nAreaID= ObjItems.getAttribute("AREA_ID").getString();
                rsArea.moveToInsertRow();
                rsArea.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsArea.updateString("AREA_ID",nAreaID);
                rsArea.updateInt("SR_NO",i);
                rsArea.updateInt("USER_ID",nUserID);
                rsArea.updateString("CREATED_BY",String.valueOf(getAttribute("CREATED_BY").getString()));
                rsArea.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsArea.updateString("MODIFIED_BY",String.valueOf(getAttribute("MODIFIED_BY").getString()));
                rsArea.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsArea.updateBoolean("CHANGED",true);
                rsArea.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsArea.insertRow();
            }
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    public boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        Connection tmpConn;
        int RevNo=0;
        int Counter=0;
        tmpConn=data.getConn();
        try {
            
            
            //------------- Header Fields --------------------//
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("SR_NO",rsResultSet.getInt("SR_NO"));
            setAttribute("USER_ID",rsResultSet.getInt("USER_ID"));
            setAttribute("USER_TYPE",rsResultSet.getInt("USER_TYPE"));
            
            if(rsResultSet.getInt("MODULE_ID")<=0) {
                setAttribute("MODULE_ID","82");
            }
            else {
                setAttribute("MODULE_ID",rsResultSet.getString("MODULE_ID"));
            }
            if(rsResultSet.getInt("FF_ID")<=0) {
                setAttribute("FF_ID","86");
            }
            else {
                setAttribute("FF_ID",rsResultSet.getString("FF_ID"));
            }
            //setAttribute("FF_ID",rsResultSet.getInt("FF_ID"));
            //setAttribute("FF_ID",rsResultSet.getInt("FF_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            setAttribute("CHANGED",rsResultSet.getBoolean("CHANGED"));
            setAttribute("CANCELLED",rsResultSet.getBoolean("CANCELLED"));
            
            // ----------------- End of Header Fields ------------------------------------//
            colArea.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            
            tmpStmt=tmpConn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_USER_AREA_MAPPING WHERE COMPANY_ID='"+EITLERPGLOBAL.gCompanyID+"' AND USER_ID ='"+rsResultSet.getInt("USER_ID")+"' ORDER BY AREA_ID ");
            
            try {
                Counter=0;
                while(rsTmp.next()) {
                    Counter=Counter+1;
                    clsSalesAreaMapping ObjItems=new clsSalesAreaMapping();
                    String mArea= rsTmp.getString("AREA_ID");//(String)ObjItems.getAttribute("AREA_ID").getString();
                    ObjItems.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                    ObjItems.setAttribute("AREA_ID",mArea);
                    ObjItems.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjItems.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    
                    
                    colArea.put(Long.toString(Counter),ObjItems);
                }
            }catch(Exception e) {
                e.printStackTrace();
                LastError = e.getMessage();
                return false;
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_MODULES " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsResultSet=Stmt.executeQuery(strSql);
            rsResultSet.first();
            
            if (rsResultSet.getRow()>0) {
                MoveFirst();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
}
