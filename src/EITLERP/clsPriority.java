/*
 * clsDepartment.java
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

public class clsPriority {
    
    public String LastError="";
    public String strSql;
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colUsers=new HashMap();
    public HashMap colApprovers=new HashMap();
   
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
    
    
    /* Creates new clsDepartment */
    public clsPriority() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("PRIORITY_ID",new Variant(0));
        props.put("PRIORITY_DESC",new Variant(""));
      /*props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HOD",new Variant(0));
      */
    }
    
    public boolean LoadData() {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSql = "SELECT * FROM D_COM_DEPT_MASTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID)+" ORDER BY DEPT_DESC";
            rsResultSet=Stmt.executeQuery(strSql);
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready = MoveFirst();
                MoveFirst();
                return Ready;
            }
            else
                Ready = false;
            return Ready;
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
        
        Statement stUsers,stApprovers;
        ResultSet rsUsers,rsApprovers;
        
        try {
            
            rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            rsResultSet.moveToInsertRow();
            
            setAttribute("DEPT_ID",data.getMaxID(EITLERPGLOBAL.gCompanyID,"D_COM_DEPT_MASTER","DEPT_ID"));
            
            int DeptID=(int)getAttribute("DEPT_ID").getVal();
            
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("DEPT_ID",(int) getAttribute("DEPT_ID").getVal());
            rsResultSet.updateString("DEPT_DESC",(String) getAttribute("DEPT_DESC").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("HOD",(int)getAttribute("HOD").getVal());
            rsResultSet.insertRow();
            
            //=====Now Insert Users in the department=========//
            stUsers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsUsers=stUsers.executeQuery("SELECT * FROM D_COM_DEPT_USERS");
            rsUsers.first();
            
            for(int i=1;i<=colUsers.size();i++) {
                clsDeptUsers ObjUser=(clsDeptUsers)colUsers.get(Integer.toString(i));
                
                rsUsers.moveToInsertRow();
                rsUsers.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsUsers.updateInt("DEPT_ID",DeptID);
                rsUsers.updateInt("SR_NO",i);
                rsUsers.updateInt("USER_ID",(int)ObjUser.getAttribute("USER_ID").getVal());
                rsUsers.updateBoolean("CHANGED",true);
                rsUsers.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsUsers.insertRow();
            }
            //===========Updation Completed====================//
            
            
            
            
            //=====Now Insert Approvers in the department=========//
            stApprovers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsApprovers=stApprovers.executeQuery("SELECT * FROM D_COM_DOC_APPROVERS");
            rsApprovers.first();
            
            for(int i=1;i<=colApprovers.size();i++) {
                clsDeptApprovers ObjUser=(clsDeptApprovers)colApprovers.get(Integer.toString(i));
                
                rsApprovers.moveToInsertRow();
                rsApprovers.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsApprovers.updateInt("DEPT_ID",DeptID);
                rsApprovers.updateInt("SR_NO",i);
                rsApprovers.updateInt("MODULE_ID",(int)ObjUser.getAttribute("MODULE_ID").getVal());
                rsApprovers.updateInt("USER_ID",(int)ObjUser.getAttribute("USER_ID").getVal());
                rsApprovers.updateBoolean("FINAL_APPROVER",ObjUser.getAttribute("FINAL_APPROVER").getBool());
                rsApprovers.updateBoolean("CHANGED",true);
                rsApprovers.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsApprovers.insertRow();
            }
            //===========Updation Completed====================//
            
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
        
        Statement stUsers,stApprovers;
        ResultSet rsUsers,rsApprovers;
        
        try {
            rsResultSet.updateString("DEPT_DESC",(String) getAttribute("DEPT_DESC").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateInt("HOD",(int)getAttribute("HOD").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateRow();
            
            int DeptID=rsResultSet.getInt("DEPT_ID");
            
            //=====Now Insert Users in the department=========//
            
            //Remove Old Records
            data.Execute("DELETE FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+DeptID);
            data.Execute("DELETE FROM D_COM_DOC_APPROVERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+DeptID);
            
            stUsers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsUsers=stUsers.executeQuery("SELECT * FROM D_COM_DEPT_USERS");
            rsUsers.first();
            
            for(int i=1;i<=colUsers.size();i++) {
                clsDeptUsers ObjUser=(clsDeptUsers)colUsers.get(Integer.toString(i));
                
                rsUsers.moveToInsertRow();
                rsUsers.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsUsers.updateInt("DEPT_ID",DeptID);
                rsUsers.updateInt("SR_NO",i);
                rsUsers.updateInt("USER_ID",(int)ObjUser.getAttribute("USER_ID").getVal());
                rsUsers.updateBoolean("CHANGED",true);
                rsUsers.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsUsers.insertRow();
            }
            //===========Updation Completed====================//
            
            
            //=====Now Insert Approvers in the department=========//
            stApprovers=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsApprovers=stApprovers.executeQuery("SELECT * FROM D_COM_DOC_APPROVERS");
            rsApprovers.first();
            
            for(int i=1;i<=colApprovers.size();i++) {
                clsDeptApprovers ObjUser=(clsDeptApprovers)colApprovers.get(Integer.toString(i));
                
                rsApprovers.moveToInsertRow();
                rsApprovers.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsApprovers.updateInt("DEPT_ID",DeptID);
                rsApprovers.updateInt("SR_NO",i);
                rsApprovers.updateInt("MODULE_ID",(int)ObjUser.getAttribute("MODULE_ID").getVal());
                rsApprovers.updateInt("USER_ID",(int)ObjUser.getAttribute("USER_ID").getVal());
                rsApprovers.updateBoolean("FINAL_APPROVER",ObjUser.getAttribute("FINAL_APPROVER").getBool());
                rsApprovers.updateBoolean("CHANGED",true);
                rsApprovers.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsApprovers.insertRow();
            }
            //===========Updation Completed====================//
            
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
            long DeptID =(int) getAttribute("DEPT_ID").getVal();
            String stmt = "DELETE FROM D_COM_DEPT_MASTER WHERE COMPANY_ID=" + Integer.toString(EITLERPGLOBAL.gCompanyID) + " And DEPT_ID="+DeptID;
            data.Execute(stmt);
            
            data.Execute("DELETE FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+DeptID);
            data.Execute("DELETE FROM D_COM_DOC_APPROVERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+DeptID);
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
        
    }
    
    public Object getObject(int pCompanyID,int pDeptID) {
        
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) +" AND DEPT_ID=" + Integer.toString(pDeptID);
        clsDepartment ObjDepartment = new clsDepartment();
        ObjDepartment.Filter(strCondition);
        return ObjDepartment;
        
    }
    
    
    public static HashMap getList(String pCondition) {
        
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            //tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            tmpStmt=tmpConn.createStatement();
            //rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_MASTER "+pCondition+" ORDER BY DEPT_DESC");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PRIORITY_MASTER "+pCondition+"");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPriority ObjDept=new clsPriority();
                
                ObjDept.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjDept.setAttribute("PRIORITY_ID",rsTmp.getInt("PRIORITY_ID"));
                ObjDept.setAttribute("PRIORITY_DESC",rsTmp.getString("PRIORITY_DESC"));
                
                List.put(Long.toString(Counter),ObjDept);
                rsTmp.next();
            }
            
          //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
    }
    
    public static HashMap getInchargeList(String pCondition) {
        
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            //tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            tmpStmt=tmpConn.createStatement();
            //rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_MASTER "+pCondition+" ORDER BY DEPT_DESC");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_INCHARGE "+pCondition+"");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPriority ObjDept=new clsPriority();
                ObjDept.setAttribute("INCHARGE_CD",rsTmp.getInt("INCHARGE_CD"));
                ObjDept.setAttribute("INCHARGE_NAME",rsTmp.getString("INCHARGE_NAME"));
                List.put(Long.toString(Counter),ObjDept);
                rsTmp.next();
            }            
          //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
    }
    
    
    
    
    public static HashMap getDeptUsersList(int pCompanyID,int pDeptID) {
        
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID);
            rsTmp.first();
            Counter=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsDeptUsers ObjUser=new clsDeptUsers();
                
                String UserName=clsUser.getUserName(pCompanyID,rsTmp.getInt("USER_ID"));
                
                ObjUser.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                ObjUser.setAttribute("USER_NAME",UserName);
                
                List.put(Long.toString(Counter),ObjUser);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
        
    }
    
    public static HashMap getDeptList(int pCompanyID,int pUserID) {
       
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PRIORITY_MASTER WHERE COMPANY_ID="+pCompanyID+" ");
            rsTmp.first();
            Counter=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPriority ObjDept=new clsPriority();
                
                //String DeptName=clsPriority.getDeptName(pCompanyID,rsTmp.getInt("DEPT_ID"));
                String DeptName=clsPriority.getDeptName(pCompanyID,rsTmp.getInt("PRIORITY_ID"));
                
                ObjDept.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjDept.setAttribute("PRIORITY_ID",rsTmp.getInt("PRIORITY_ID"));
                ObjDept.setAttribute("PRIORITY_DESC",DeptName);
                //ObjDept.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                //ObjDept.setAttribute("DEPT_NAME",DeptName);
                
                
                List.put(Long.toString(Counter),ObjDept);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
       
    }
    
    
        public static HashMap getInchargeList(int pCompanyID,int pUserID) {
       
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_ACTIVE ='Y'");
            rsTmp.first();
            Counter=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPriority ObjDept=new clsPriority();
                
                //String DeptName=clsPriority.getDeptName(pCompanyID,rsTmp.getInt("DEPT_ID"));
                String DeptName=clsPriority.getInchargeName(rsTmp.getInt("INCHARGE_CD"));
                
                //ObjDept.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjDept.setAttribute("INCHARGE_CD",rsTmp.getInt("INCHARGE_CD"));
                ObjDept.setAttribute("INCHARGE_NAME",DeptName);
                                
                List.put(Long.toString(Counter),ObjDept);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
       
    }
    
 /*   public static HashMap getLastDDMMYYYY(int pCompanyID,int pUserID) {
       
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID);
          //  rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_INCHARGE");
            rsTmp=tmpStmt.executeQuery("SELECT DISTICT SUBSTRING(MONTH_CLOSING_DATE,1,7) AS DT,MONTH_CLOSING_DATE, FROM PRODUCTION.FELT_PIECE_REGISTER_MONTH_CLOSING");
            rsTmp.first();
            Counter=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPriority ObjDept=new clsPriority();
                
                //String DeptName=clsPriority.getDeptName(pCompanyID,rsTmp.getInt("DEPT_ID"));
              //  String DeptName=clsPriority.getInchargeName(rsTmp.getInt(",MONTH_CLOSING_DATE"));
                
                //ObjDept.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjDept.setAttribute("INCHARGE_CD",rsTmp.getInt("INCHARGE_CD"));
                ObjDept.setAttribute("INCHARGE_NAME",DeptName);
                                
                List.put(Long.toString(Counter),ObjDept);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
       
    }
   */ 
    
    public static boolean IsUserComesInDept(int pCompanyID,int pUserID,int pDeptID) {
       
        boolean IsExist=false;
        ResultSet rsTmp;
        
        try {
            rsTmp=data.getResult("SELECT * FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND DEPT_ID="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IsExist=true;
            }
            return IsExist;
        }
        catch(Exception e) {
        }
        return IsExist;
       
    }
    
    
    
    public static boolean IsFinalApprover(int pCompanyID,int pModuleID,int pDeptID,int pUserID) {
        
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        boolean HasRights=false;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DOC_APPROVERS WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DEPT_ID="+pDeptID+" AND USER_ID="+pUserID);
            rsTmp.first();
            Counter=0;
            
            if(rsTmp.getRow()>0) {
                HasRights=rsTmp.getBoolean("FINAL_APPROVER");
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            
            return HasRights;
        }
        catch(Exception e) {
        }
        return HasRights;
       
    }
    
    
    public static HashMap getApproverList(int pCompanyID,int pModuleID,int pDeptID) {
     
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DOC_APPROVERS WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DEPT_ID="+pDeptID+" ORDER BY SR_NO");
            
            System.out.println("SELECT * FROM D_COM_DOC_APPROVERS WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DEPT_ID="+pDeptID+" ORDER BY SR_NO");
            rsTmp.first();
            Counter=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsDeptApprovers ObjDept=new clsDeptApprovers();
                
                int UserID=rsTmp.getInt("USER_ID");
                
                String UserName=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,UserID);
                
                ObjDept.setAttribute("USER_ID",UserID);
                ObjDept.setAttribute("USER_NAME",UserName);
                
                List.put(Long.toString(Counter),ObjDept);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
      
    }
    
    
    public static HashMap getList(String pCondition,String pURL) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        HashMap List=new HashMap();
        
        tmpConn= data.getConn(pURL);
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_MASTER "+pCondition+" ORDER BY DEPT_DESC");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PRIORITY_MASTER "+pCondition+" ");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsPriority ObjDept=new clsPriority();
                
                ObjDept.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjDept.setAttribute("PRIORITY_ID",rsTmp.getInt("PRIORITY"));
                ObjDept.setAttribute("PRIORITY_DESC",rsTmp.getString("PRIORITY_DESC"));
                
                List.put(Long.toString(Counter),ObjDept);
                rsTmp.next();
            }
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
            return List;
        }
        catch(Exception e) {
        }
        return List;
    }
    
    public boolean Filter(String pCondition) {
    
     Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_DEPT_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement();
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready= MoveFirst();
            }
            else
            {    Ready=false;}
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    
    }
    
    
    
    public boolean setData() {
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("DEPT_ID",rsResultSet.getInt("DEPT_ID"));
            setAttribute("DEPT_DESC",rsResultSet.getString("DEPT_DESC"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("HOD",rsResultSet.getInt("HOD"));
            
            
            int Counter=0;
            //Clear existing records
            colUsers.clear();
            
            //Now Fetch Department Users
            stTmp=Conn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_DEPT_USERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+(int)getAttribute("DEPT_ID").getVal());
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    Counter++;
                    clsDeptUsers ObjUser=new clsDeptUsers();
                    
                    ObjUser.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjUser.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjUser.setAttribute("SR_NO",Counter);
                    ObjUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    
                    colUsers.put(Integer.toString(colUsers.size()+1),ObjUser);
                    
                    rsTmp.next();
                }
            }
            
            
            //Clear existing records
            colApprovers.clear();
            
            //Now Fetch Department Users
            stTmp=Conn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_DOC_APPROVERS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DEPT_ID="+(int)getAttribute("DEPT_ID").getVal());
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDeptApprovers ObjUser=new clsDeptApprovers();
                    
                    ObjUser.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjUser.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                    ObjUser.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjUser.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                    ObjUser.setAttribute("FINAL_APPROVER",rsTmp.getBoolean("FINAL_APPROVER"));
                    ObjUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    
                    colApprovers.put(Integer.toString(colApprovers.size()+1),ObjUser);
                    
                    rsTmp.next();
                }
            }
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
       
    }
    
    
    public boolean Find(String pCondition) {
       
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_DEPT_MASTER WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID);
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            if (Stmt.execute(strSql)) {
                rsResultSet = Stmt.getResultSet();
                Ready=MoveFirst();
                while(! rsResultSet.isAfterLast()) {
                    if(rsResultSet.getString("DEPT_ID").equals(pCondition)) {
                        setData();
                        return true;
                    }
                    else {
                        rsResultSet.next();
                    }
                }
            }
            else
            {    Ready=false;}
            
            return Ready;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
       
    }
    
    public static String getDeptName(int pCompanyID,String pDeptID) {
       
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String DeptName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID='"+pDeptID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                DeptName=rsTmp.getString("DEPT_DESC");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return DeptName;
        }
        catch(Exception e) {
            return "";
        }
       
    }
    
    
    public static String getDeptName(int pCompanyID,int pDeptID) {
       
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String DeptName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID);
            rsTmp=stTmp.executeQuery("SELECT PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PRIORITY_ID="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //DeptName=rsTmp.getString("DEPT_DESC");
                DeptName=rsTmp.getString("PRIORITY_DESC");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return DeptName;
        }
        catch(Exception e) {
            return "";
        }       
    }
    
    public static String getInchargeName(int pDeptID) {
       
        //System.out.println(pDeptID);
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String DeptName="";        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID);
            rsTmp=stTmp.executeQuery("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //DeptName=rsTmp.getString("DEPT_DESC");
                DeptName=rsTmp.getString("INCHARGE_NAME");
            }            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return DeptName;
        }
        catch(Exception e) {
            return "";
        }       
    }
    
    
    
    public static int getDeptHeadID(int pCompanyID,int pDeptID) {
       
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        int HOD=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT HOD FROM D_COM_DEPT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                HOD=rsTmp.getInt("HOD");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            
            return HOD;
        }
        catch(Exception e) {
            return 0;
        }
       
    }
    
    public static boolean IsValidDeptCode(int pCompanyID,int pDeptID) {
       
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID="+pCompanyID+" AND DEPT_ID="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                
                return true;
            }else{
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return false;}
        }
        catch(Exception e) {
            return false;
        }
       
    }
    
}
