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
import EITLERP.Purchase.*;
import EITLERP.Stores.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsSection {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    //History Related properties
    private boolean HistoryView=false;
    
    private String OldSectionID="";
        
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
    public clsSection() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("SECTION_SYS_ID",new Variant(0));
        props.put("SECTION_ID",new Variant(""));
        props.put("SECTION_NAME",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            HistoryView=false;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_SECTION_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID));
            Ready=true;
            MoveLast();
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
        Statement stTmp,stHistory;
        ResultSet rsTmp,rsHistory;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SECTION_MASTER_H WHERE SECTION_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            
            //--------- Check for the Duplication of Login ID ------
            long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_COM_SECTION_MASTER WHERE COMPANY_ID="+Long.toString(lCompanyID)+" AND SECTION_ID='"+(String) getAttribute("SECTION_ID").getObj()+"'";
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
            
            
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="Section code already exist in the database. Please specify other section code";
                return false;
            }
            //------- Duplication check over ----------------------

            
            //--------- Generate new system id ------------
            setAttribute("SECTION_SYS_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_SECTION_MASTER","SECTION_SYS_ID"));
            //-------------------------------------------------
            
            
            //----------- Save Header Record ----------------
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateLong("SECTION_SYS_ID",(long)getAttribute("SECTION_SYS_ID").getVal());
            rsResultSet.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsResultSet.updateString("SECTION_NAME",(String)getAttribute("SECTION_NAME").getObj());
            rsResultSet.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS"," ");
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS"," ");
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateLong("SECTION_SYS_ID",(long)getAttribute("SECTION_SYS_ID").getVal());
            rsHistory.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsHistory.updateString("SECTION_NAME",(String)getAttribute("SECTION_NAME").getObj());
            rsHistory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
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
        Statement stTmp,stHistory;
        ResultSet rsTmp,rsHistory;
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_SECTION_MASTER_H WHERE SECTION_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            
            //--------- Check for the Duplication of Login ID ------
            long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            long lDesSysID=(long) getAttribute("SECTION_SYS_ID").getVal();
            
            
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_COM_SECTION_MASTER WHERE COMPANY_ID="+Long.toString(lCompanyID)+" AND SECTION_ID='"+(String) getAttribute("SECTION_ID").getObj()+"' AND SECTION_SYS_ID<>"+Long.toString(lDesSysID);
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
            
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="Section id. already exist in the database. Please specify other section id";
                return false;
            }
            //------- Duplication check over ----------------------
            
            //Update all child records
            data.Execute("UPDATE D_COM_EMPLOYEE_MASTER SET SECTION_ID='"+(String)getAttribute("SECTION_ID").getObj()+"' WHERE SECTION_ID='"+OldSectionID+"'");
            
            //----------- Save Header Record ----------------
            rsResultSet.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsResultSet.updateString("SECTION_NAME",(String)getAttribute("SECTION_NAME").getObj());
            rsResultSet.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_COM_SECTION_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SECTION_SYS_ID='"+(int)getAttribute("SECTION_SYS_ID").getVal()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("SECTION_ID").getObj();
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS"," ");
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS"," ");
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("SECTION_SYS_ID",(long)getAttribute("SECTION_SYS_ID").getVal());
            rsHistory.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsHistory.updateString("SECTION_NAME",(String)getAttribute("SECTION_NAME").getObj());
            rsHistory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
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
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDesID=(String)getAttribute("SECTION_ID").getObj();
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_EMPLOYEE_MASTER WHERE SECTION_ID='"+lDesID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                LastError="Section ID is currently referred by employee master record. Please remove the employee first";
                return false;
            }
            
            String strSQL="DELETE FROM D_COM_SECTION_MASTER WHERE COMPANY_ID="+Integer.toString(lCompanyID)+" AND SECTION_ID='"+lDesID+"'";
            data.Execute(strSQL);
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID,String pDesID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND SECTION_ID='"+pDesID+"'";
        clsSection ObjItem= new clsSection();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    
    
    
    
    
    
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_SECTION_MASTER " + pCondition ;
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
    
    
    
    public boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        int RevNo=0;
        int Counter=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //------------- Header Fields --------------------//
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("SECTION_SYS_ID",rsResultSet.getLong("SECTION_SYS_ID"));
            setAttribute("SECTION_ID",rsResultSet.getString("SECTION_ID"));
            OldSectionID=rsResultSet.getString("SECTION_ID");
            setAttribute("SECTION_NAME",rsResultSet.getString("SECTION_NAME"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            // ----------------- End of Header Fields ------------------------------------//
            
            
            //All data have benn filled
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static String getSectionName(int pCompanyID,String pDesID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lSectionName="";
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT SECTION_NAME FROM D_COM_SECTION_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SECTION_ID='"+pDesID+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lSectionName=rsTmp.getString("SECTION_NAME");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lSectionName;
        }
        catch(Exception e) {
            return lSectionName;
        }
        
    }
    
    
    
    
    public static long getSectionSystemID(int pCompanyID,String pDesignationID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        long DesSysID=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT SECTION_SYS_ID FROM D_COM_SECTION_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND SECTION_ID='"+pDesignationID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                DesSysID=rsTmp.getLong("SECTION_SYS_ID");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return DesSysID;
        }
        catch(Exception e) {
            return DesSysID;
        }
    }
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_SECTION_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND SECTION_ID='"+pDocNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_SECTION_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SECTION_ID='"+pDocNo+"' ORDER BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsSection ObjItem=new clsSection();
                
                ObjItem.setAttribute("SECTION_ID",rsTmp.getString("SECTION_ID"));
                ObjItem.setAttribute("SECTION_DATE","0000-00-00");
                ObjItem.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjItem.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjItem.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjItem.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjItem.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjItem);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    public static HashMap getList(String pCondition) {
        
        ResultSet rsTmp;
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_COM_SECTION_MASTER "+pCondition+" ORDER BY SECTION_NAME");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsSection ObjDesignation=new clsSection();
                
                //Populate the user
                ObjDesignation.setAttribute("SECTION_ID",rsTmp.getString("SECTION_ID"));
                ObjDesignation.setAttribute("SECTION_NAME",rsTmp.getString("SECTION_NAME"));
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjDesignation);
                
                
                rsTmp.next();
            }//Out While
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
        
        return List;
    }
    
}
