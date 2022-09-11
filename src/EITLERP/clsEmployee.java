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

public class clsEmployee {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    //History Related properties
    private boolean HistoryView=false;
    
    public String OldEmplID="";
    
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
    public clsEmployee() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("EMPLOYEE_SYS_ID",new Variant(0));
        props.put("EMPLOYEE_ID",new Variant(""));
        props.put("EMPLOYEE_NAME",new Variant(""));
        props.put("ADD1",new Variant(""));
        props.put("ADD2",new Variant(""));
        props.put("ADD3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("PHONE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("DESIGNATION_ID",new Variant(""));
        props.put("SECTION_ID",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            HistoryView=false;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID));
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASTER_H WHERE EMPLOYEE_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            
            //--------- Check for the Duplication of Login ID ------
            long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Long.toString(lCompanyID)+" AND EMPLOYEE_ID='"+(String) getAttribute("EMPLOYEE_ID").getObj()+"'";
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
            
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="Employee code already exist in the database. Please specify other employee code";
                return false;
            }
            //------- Duplication check over ----------------------
            
            
            
            //--------- Generate new system id ------------
            setAttribute("EMPLOYEE_SYS_ID", data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_EMPLOYEE_MASTER","EMPLOYEE_SYS_ID"));
            //-------------------------------------------------
            
            
            //----------- Save Header Record ----------------
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.updateLong("EMPLOYEE_SYS_ID",(long)getAttribute("EMPLOYEE_SYS_ID").getVal());
            rsResultSet.updateString("EMPLOYEE_ID",(String)getAttribute("EMPLOYEE_ID").getObj());
            rsResultSet.updateString("EMPLOYEE_NAME",(String)getAttribute("EMPLOYEE_NAME").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PHONE",(String)getAttribute("PHONE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("DESIGNATION_ID",(String)getAttribute("DESIGNATION_ID").getObj());
            rsResultSet.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsResultSet.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
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
            rsHistory.updateLong("EMPLOYEE_SYS_ID",(long)getAttribute("EMPLOYEE_SYS_ID").getVal());
            rsHistory.updateString("EMPLOYEE_ID",(String)getAttribute("EMPLOYEE_ID").getObj());
            rsHistory.updateString("EMPLOYEE_NAME",(String)getAttribute("EMPLOYEE_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("PHONE",(String)getAttribute("PHONE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("DESIGNATION_ID",(String)getAttribute("DESIGNATION_ID").getObj());
            rsHistory.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsHistory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASTER_H WHERE EMPLOYEE_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            
            //--------- Check for the Duplication of Login ID ------
            long lCompanyID=(long) getAttribute("COMPANY_ID").getVal();
            long lEmpSysID=(long) getAttribute("EMPLOYEE_SYS_ID").getVal();
            
            
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Long.toString(lCompanyID)+" AND EMPLOYEE_ID='"+(String) getAttribute("EMPLOYEE_ID").getObj()+"' AND EMPLOYEE_SYS_ID<>"+Long.toString(lEmpSysID);
            System.out.println(strSQL);
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
            
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="Employee id. already exist in the database. Please specify other employee id";
                return false;
            }
            //------- Duplication check over ----------------------
            
            
            data.Execute("UPDATE D_INV_INDENT_ITEM_DETAIL SET CARD_NO='"+(String)getAttribute("EMPLOYEE_ID").getObj()+"' WHERE CARD_NO='"+OldEmplID+"'");
                        
            //----------- Save Header Record ----------------
            rsResultSet.updateString("EMPLOYEE_ID",(String)getAttribute("EMPLOYEE_ID").getObj());
            rsResultSet.updateString("EMPLOYEE_NAME",(String)getAttribute("EMPLOYEE_NAME").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PHONE",(String)getAttribute("PHONE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateString("DESIGNATION_ID",(String)getAttribute("DESIGNATION_ID").getObj());
            rsResultSet.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsResultSet.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_COM_EMPLOYEE_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND EMPLOYEE_SYS_ID="+(int)getAttribute("EMPLOYEE_SYS_ID").getVal()+" ");
            RevNo++;
            String RevDocNo=(String)getAttribute("EMPLOYEE_ID").getObj();
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS"," ");
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS"," ");
            rsHistory.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsHistory.updateLong("EMPLOYEE_SYS_ID",(long)getAttribute("EMPLOYEE_SYS_ID").getVal());
            rsHistory.updateString("EMPLOYEE_ID",(String)getAttribute("EMPLOYEE_ID").getObj());
            rsHistory.updateString("EMPLOYEE_NAME",(String)getAttribute("EMPLOYEE_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("PHONE",(String)getAttribute("PHONE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateString("DESIGNATION_ID",(String)getAttribute("DESIGNATION_ID").getObj());
            rsHistory.updateString("SECTION_ID",(String)getAttribute("SECTION_ID").getObj());
            rsHistory.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
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
            String lEmployeeID=(String)getAttribute("EMPLOYEE_ID").getObj();
            
            /*ResultSet rsTmp=data.getResult("SELECT * FROM D_INV_INDENT_ITEM_DETAIL WHERE CARD_NO='"+lEmployeeID+"' ");
            rsTmp.first();
            if(rsTmp.getRow()>0)
            {
               LastError="Employee is referred by indent records. Please first delete indent records";
               return false;
            }*/
                        
            String strSQL="DELETE FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Integer.toString(lCompanyID)+" AND EMPLOYEE_ID='"+lEmployeeID+"'";
            data.Execute(strSQL);
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID,String pEmployeeID) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND EMPLOYEE_ID='"+pEmployeeID+"'";
        clsEmployee ObjItem= new clsEmployee();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_EMPLOYEE_MASTER " + pCondition ;
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
            setAttribute("EMPLOYEE_SYS_ID",rsResultSet.getLong("EMPLOYEE_SYS_ID"));
            setAttribute("EMPLOYEE_ID",rsResultSet.getString("EMPLOYEE_ID"));
            OldEmplID=rsResultSet.getString("EMPLOYEE_ID");
            setAttribute("EMPLOYEE_NAME",rsResultSet.getString("EMPLOYEE_NAME"));
            setAttribute("ADD1",rsResultSet.getString("ADD1"));
            setAttribute("ADD2",rsResultSet.getString("ADD2"));
            setAttribute("ADD3",rsResultSet.getString("ADD3"));
            setAttribute("CITY",rsResultSet.getString("CITY"));
            setAttribute("PINCODE",rsResultSet.getString("PINCODE"));
            setAttribute("PHONE",rsResultSet.getString("PHONE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("DESIGNATION_ID",rsResultSet.getString("DESIGNATION_ID"));
            setAttribute("SECTION_ID",rsResultSet.getString("SECTION_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("DEPT_ID",rsResultSet.getInt("DEPT_ID"));
            // ----------------- End of Header Fields ------------------------------------//
            
            
            //All data have benn filled
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static String getEmployeeName(int pCompanyID,String pEmployeeID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lEmployeeName="";
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT EMPLOYEE_NAME FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND EMPLOYEE_ID='"+pEmployeeID+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lEmployeeName=rsTmp.getString("EMPLOYEE_NAME");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lEmployeeName;
        }
        catch(Exception e) {
            return lEmployeeName;
        }
        
    }
    
    
    public static String getEmpDesignation(int pCompanyID,String pEmployeeID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        String lEmployeeDes="";
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT DESIGNATION_ID FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND EMPLOYEE_ID='"+pEmployeeID+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lEmployeeDes=rsTmp.getString("DESIGNATION_ID");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return lEmployeeDes;
        }
        catch(Exception e) {
            return lEmployeeDes;
        }
    }
    
    
    
    public static long getEmployeeSystemID(int pCompanyID,String pEmployeeID) {
        Connection tmpConn;
        Statement Stmt;
        ResultSet rsTmp;
        long EmployeeSysID=0;
        
        try {
            tmpConn=data.getConn();
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=Stmt.executeQuery("SELECT EMPLOYEE_SYS_ID FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND EMPLOYEE_ID='"+pEmployeeID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                EmployeeSysID=rsTmp.getLong("EMPLOYEE_SYS_ID");
            }
            //tmpConn.close();
            Stmt.close();
            rsTmp.close();
            
            return EmployeeSysID;
        }
        catch(Exception e) {
            return EmployeeSysID;
        }
    }
    
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND EMPLOYEE_ID='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASSTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND EMPLOYEE_ID='"+pDocNo+"' ORDER BY REVISION_NO");
            
            while(rsTmp.next()) {
                clsEmployee ObjItem=new clsEmployee();
                
                ObjItem.setAttribute("EMPLOYEE_ID",rsTmp.getString("EMPLOYEE_ID"));
                ObjItem.setAttribute("EMPLOYEE_DATE","0000-00-00");
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

    
    
    
    public static HashMap getDeptList(int pCompanyID,int pDeptID) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+pDeptID+" ORDER BY EMPLOYEE_NAME");
            
            while(rsTmp.next()) {
                clsEmployee ObjItem=new clsEmployee();
                
                ObjItem.setAttribute("EMPLOYEE_ID",rsTmp.getString("EMPLOYEE_ID"));
                ObjItem.setAttribute("EMPLOYEE_NAME",rsTmp.getString("EMPLOYEE_NAME"));
                ObjItem.setAttribute("DESIGNATION_ID",rsTmp.getString("DESIGNATION_ID"));
                ObjItem.setAttribute("DESIGNATION_NAME",clsDesignation.getDesignationName(EITLERPGLOBAL.gCompanyID,rsTmp.getString("DESIGNATION_ID")));

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

    
    
    public static HashMap getSectionList(int pCompanyID,String pSectionID) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND SECTION_ID='"+pSectionID+"' ORDER BY EMPLOYEE_NAME");
            
            while(rsTmp.next()) {
                clsEmployee ObjItem=new clsEmployee();
                
                ObjItem.setAttribute("EMPLOYEE_ID",rsTmp.getString("EMPLOYEE_ID"));
                ObjItem.setAttribute("EMPLOYEE_NAME",rsTmp.getString("EMPLOYEE_NAME"));
                ObjItem.setAttribute("DESIGNATION_ID",rsTmp.getString("DESIGNATION_ID"));
                ObjItem.setAttribute("DESIGNATION_NAME",clsDesignation.getDesignationName(EITLERPGLOBAL.gCompanyID,rsTmp.getString("DESIGNATION_ID")));

                ObjItem.setAttribute("SECTION_ID",rsTmp.getString("SECTION_ID"));
                ObjItem.setAttribute("SECTION_NAME",clsSection.getSectionName(EITLERPGLOBAL.gCompanyID,rsTmp.getString("SECTION_ID")));
                
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
    
}
