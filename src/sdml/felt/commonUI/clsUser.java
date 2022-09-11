/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

import java.util.*;
import java.sql.*;

/**
 *
 * @author  nrpithva
 * @version
 */

public class clsUser {
    
    public String LastError="";
    private ResultSet rsUser;
    private Connection Conn;
    private Statement Stmt;
    public int DepthLevel =0;
    
    //User Rights Collection
    public HashMap colRights;
    
    private HashMap props;
    public boolean Ready = false;
    
    public boolean ChildUserFound=false;
    
    
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
    public clsUser() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("USER_ID",new Variant(0));
        props.put("USER_NAME",new Variant(""));
        props.put("LOGIN_ID",new Variant(""));
        props.put("PASSWORD",new Variant(""));
        //props.put("USER_TYPE",new Variant(0));
        props.put("INTERNAL_EMAIL",new Variant(""));
        props.put("EXTERNAL_EMAIL",new Variant(""));
        props.put("LOCKED",new Variant(false));
        props.put("PASSWORD_EXPIRY_DATE",new Variant(""));
        
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("DEPT_ID",new Variant(0));
        props.put("SUPERIOR_ID",new Variant(0));
        //BY MUFFY
        props.put("LOCKED",new Variant("0"));
        
        //Create a new object for rights collection
        colRights=new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsUser=Stmt.executeQuery("SELECT * FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID));
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
            Stmt.close();
            rsUser.close();
        }
        catch(Exception e) {
            
        }
    }
    
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsUser.first();
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
            if(rsUser.isAfterLast()||rsUser.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsUser.last();
            }
            else {
                rsUser.next();
                if(rsUser.isAfterLast()) {
                    rsUser.last();
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
            if(rsUser.isFirst()||rsUser.isBeforeFirst()) {
                rsUser.first();
            }
            else {
                rsUser.previous();
                if(rsUser.isBeforeFirst()) {
                    rsUser.first();
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
            rsUser.last();
            setData();
            return true;
        } catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert() {
        try {
            ResultSet rsRights;
            Statement StmtRights;
            
            StmtRights=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsUser.first();
            
            //get Current set Company ID
            long lCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            
            //Check for the Duplication of Login ID
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(lCompanyID)+" AND LOGIN_ID='"+(String) getAttribute("LOGIN_ID").getObj()+"'";
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
            
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="Login ID already exist in the database. Please specify other login id";
                return false;
            }
            
            
            long SrNo=data.getMaxID(SDMLERPGLOBAL.gCompanyID, "D_COM_USER_RIGHTS", "SR_NO");
            
            //Generating new User id by using Max(Userid)+1
            //And Assigning it to class property
            setAttribute("USER_ID", data.getMaxID(lCompanyID,"D_COM_USER_MASTER","USER_ID"));
            
            rsUser.moveToInsertRow();
            rsUser.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsUser.updateLong("USER_ID",(long) getAttribute("USER_ID").getVal());
            rsUser.updateInt("SUPERIOR_ID",(int) getAttribute("SUPERIOR_ID").getVal());
            rsUser.updateString("USER_NAME",(String) getAttribute("USER_NAME").getObj());
            rsUser.updateString("LOGIN_ID",(String)getAttribute("LOGIN_ID").getObj());
            rsUser.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsUser.updateString("PASSWORD",getAttribute("PASSWORD").getString());
            //rsUser.updateLong("USER_TYPE",getAttribute("USER_TYPE").getInt());
            rsUser.updateString("INTERNAL_EMAIL",(String)getAttribute("INTERNAL_EMAIL").getObj());
            rsUser.updateString("EXTERNAL_EMAIL",(String)getAttribute("EXTERNAL_EMAIL").getObj());
            rsUser.updateBoolean("LOCKED",getAttribute("LOCKED").getBool());
            rsUser.updateString("PASSWORD_EXPIRY_DATE",SDMLERPGLOBAL.formatDateDB(getAttribute("PASSWORD_EXPIRY_DATE").getString()));
            
            rsUser.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsUser.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsUser.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsUser.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsUser.updateBoolean("CHANGED",true);
            rsUser.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
            rsUser.insertRow();
            
            
            rsRights=StmtRights.executeQuery("SELECT * FROM D_COM_USER_RIGHTS");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colRights.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsUserRights ObjRights=(clsUserRights) colRights.get(Integer.toString(cnt));
                rsRights.moveToInsertRow();
                rsRights.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsRights.updateLong("USER_ID",(long)getAttribute("USER_ID").getVal());
                rsRights.updateLong("MENU_ID",clsMenu.getMenuIDFromFunctionID(SDMLERPGLOBAL.gCompanyID,(int)ObjRights.getAttribute("FUNCTION_ID").getVal()));
                rsRights.updateLong("FUNCTION_ID",(long)ObjRights.getAttribute("FUNCTION_ID").getVal());
                rsRights.updateLong("SR_NO",SrNo);
                rsRights.updateBoolean("CHANGED",true);
                rsRights.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
                rsRights.insertRow();
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
        try {
            ResultSet rsRights;
            Statement StmtRights;
            
            
            StmtRights=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            long lnCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            long lUserID=(long) getAttribute("USER_ID").getVal();
            
            //Check for the Duplication of Login ID
            String strSQL="SELECT COUNT(*) AS THECOUNT FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(lnCompanyID)+" AND LOGIN_ID='"+(String) getAttribute("LOGIN_ID").getObj()+"' AND USER_ID<>"+Long.toString(lUserID);
            
            ResultSet rsCount;
            Statement stCount=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsCount=stCount.executeQuery(strSQL);
            rsCount.first();
            
            if(rsCount.getLong("THECOUNT")>0) {
                LastError="Login ID already exist in the database. Please specify other login id";
                return false;
            }
            
            long SrNo=data.getMaxID(SDMLERPGLOBAL.gCompanyID, "D_COM_USER_RIGHTS", "SR_NO");
            
            //No Primary Keys will be updated
            rsUser.updateString("USER_NAME",(String) getAttribute("USER_NAME").getObj());
            rsUser.updateString("LOGIN_ID",(String)getAttribute("LOGIN_ID").getObj());
            rsUser.updateInt("DEPT_ID",(int)getAttribute("DEPT_ID").getVal());
            rsUser.updateInt("SUPERIOR_ID",(int)getAttribute("SUPERIOR_ID").getVal());
            rsUser.updateString("PASSWORD",(String)getAttribute("PASSWORD").getObj());
            
            rsUser.updateString("INTERNAL_EMAIL",(String)getAttribute("INTERNAL_EMAIL").getObj());
            rsUser.updateString("EXTERNAL_EMAIL",(String)getAttribute("EXTERNAL_EMAIL").getObj());
            rsUser.updateBoolean("LOCKED",getAttribute("LOCKED").getBool());
            rsUser.updateString("PASSWORD_EXPIRY_DATE",SDMLERPGLOBAL.formatDateDB(getAttribute("PASSWORD_EXPIRY_DATE").getString()));
            //rsUser.updateLong("USER_TYPE",getAttribute("USER_TYPE").getInt());
            rsUser.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsUser.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsUser.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsUser.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsUser.updateBoolean("CHANGED",true);
            rsUser.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
            
            
            rsUser.updateRow();
            
            //First remove the old rows
            String mstrCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mstrUserID=Long.toString((long)getAttribute("USER_ID").getVal());
            
            data.Execute("DELETE FROM D_COM_USER_RIGHTS WHERE COMPANY_ID="+mstrCompanyID+" AND USER_ID="+mstrUserID);
            
            rsRights=StmtRights.executeQuery("SELECT * FROM D_COM_USER_RIGHTS");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colRights.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsUserRights ObjRights=(clsUserRights) colRights.get(Integer.toString(cnt));
                rsRights.moveToInsertRow();
                rsRights.updateLong("COMPANY_ID",lnCompanyID);
                rsRights.updateLong("USER_ID",lUserID);
                rsRights.updateLong("MENU_ID",clsMenu.getMenuIDFromFunctionID(SDMLERPGLOBAL.gCompanyID,(int)ObjRights.getAttribute("FUNCTION_ID").getVal()) );
                rsRights.updateLong("FUNCTION_ID",(long)ObjRights.getAttribute("FUNCTION_ID").getVal());
                rsRights.updateLong("SR_NO",SrNo);
                rsRights.updateBoolean("CHANGED",true);
                rsRights.updateString("CHANGED_DATE",SDMLERPGLOBAL.getCurrentDateDB());
                rsRights.insertRow();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete() {
        try {
            String lCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String lUserID=Long.toString((long) getAttribute("USER_ID").getVal());
            
            String strQry = "DELETE FROM D_COM_USER_MASTER WHERE COMPANY_ID=" + lCompanyID +" AND USER_ID="+lUserID;
            data.Execute(strQry);
            strQry = "DELETE FROM D_COM_USER_RIGHTS WHERE COMPANY_ID=" + lCompanyID +" AND USER_ID="+lUserID;
            data.Execute(strQry);
            
            LoadData(Long.parseLong(lCompanyID));
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static String getUserName(long pCompanyID,long pUserID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT USER_NAME FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID));
            rsTmp.first();
            
            String UserName= rsTmp.getString("USER_NAME");
            
            tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            
            return UserName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static int getSuperiorID(long pCompanyID,long pUserID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT SUPERIOR_ID FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID));
            rsTmp.first();
            
            int UserID= rsTmp.getInt("SUPERIOR_ID");
            
            tmpConn.close();
            tmpStmt.close();
            rsTmp.close();
            
            return UserID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static String getInternalEMail(int pCompanyID,int pUserID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT INTERNAL_EMAIL FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID));
            rsTmp.first();
            
            String EMail= rsTmp.getString("INTERNAL_EMAIL");
            
            tmpStmt.close();
            rsTmp.close();
            tmpConn.close();
            return EMail;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getExternalEMail(int pCompanyID,int pUserID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT EXTERNAL_EMAIL FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID));
            rsTmp.first();
            
            String EMail= rsTmp.getString("EXTERNAL_EMAIL");
            
            tmpStmt.close();
            rsTmp.close();
             tmpConn.close();
            return EMail;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getEMailID(long pCompanyID,long pUserID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT INTERNAL_EMAIL FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID));
            rsTmp.first();
            
            String email= rsTmp.getString("INTERNAL_EMAIL");
            
            tmpStmt.close();
            rsTmp.close();
             tmpConn.close();
            return email;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getUserName(long pCompanyID,long pUserID,String pURL) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn(pURL);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT USER_NAME FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID));
            rsTmp.first();
            
            String UserName= rsTmp.getString("USER_NAME");
            
            tmpStmt.close();
            rsTmp.close();
             tmpConn.close();
            return UserName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public Object getObject(long pCompanyID,long pUserID) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID);
        clsUser ObjUser=new clsUser();
        ObjUser.Filter(strCondition,pCompanyID);
        return ObjUser;
    }
    
    public HashMap getList(String pCondition) {
        Connection tmpConn;
        tmpConn=data.getCreatedConn();
        
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp2;
        Statement tmpStmt2;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM DINESHMILLS.D_COM_USER_MASTER "+pCondition+" ORDER BY USER_NAME");
            //System.out.println("SELECT * FROM DINESHMILLS.D_COM_USER_MASTER "+pCondition+" ORDER BY USER_NAME");
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsUser ObjUser=new clsUser();
                
                //Populate the user
                ObjUser.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjUser.setAttribute("USER_ID",rsTmp.getLong("USER_ID"));
                ObjUser.setAttribute("USER_NAME",rsTmp.getString("USER_NAME"));
                ObjUser.setAttribute("LOGIN_ID",rsTmp.getString("LOGIN_ID"));
                ObjUser.setAttribute("PASSWORD",rsTmp.getString("PASSWORD"));
                ObjUser.setAttribute("DEPT_ID",rsTmp.getLong("DEPT_ID"));
                ObjUser.setAttribute("SUPERIOR_ID",rsTmp.getLong("SUPERIOR_ID"));
                ObjUser.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjUser.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjUser.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjUser.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                
                //Now Populate the collection
                //first clear the collection
                ObjUser.colRights.clear();
                
                String mCompanyID=Long.toString( (long) ObjUser.getAttribute("COMPANY_ID").getVal());
                String mUserID=Long.toString((long) ObjUser.getAttribute("USER_ID").getVal());
                
                tmpStmt2=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_COM_USER_RIGHTS WHERE COMPANY_ID="+mCompanyID+" AND USER_ID="+mUserID);
                
                rsTmp2.first();
                
                if(rsTmp2.getRow()>0) {
                    int Counter2=0;
                    while(!rsTmp2.isAfterLast()) {
                        Counter2=Counter2+1;
                        clsUserRights ObjRights=new clsUserRights();
                        
                        ObjRights.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                        ObjRights.setAttribute("USER_ID",rsTmp2.getLong("USER_ID"));
                        ObjRights.setAttribute("MENU_ID",rsTmp2.getLong("MENU_ID"));
                        ObjRights.setAttribute("FUNCTION_ID",rsTmp2.getLong("FUNCTION_ID"));
                        ObjRights.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                        ObjRights.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                        ObjRights.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                        ObjRights.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                        
                        ObjUser.colRights.put(Long.toString(Counter2),ObjRights);
                        rsTmp2.next();
                        
                    }// Innser while
                }//If Condition
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjUser);
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
            tmpConn.close();
        }
        catch(Exception e) {
            //JOptionPane.showMessageDialog(null,"Error occured"+e.getMessage());
        }
         
        return List;
    }
    
    
    
    
    
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_USER_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsUser=Stmt.executeQuery(strSql);
            
            if(!rsUser.first()) {
                strSql = "SELECT * FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID);
                rsUser=Stmt.executeQuery(strSql);
                Ready=true;
                MoveFirst();
                return false;
            }
            else {
                Ready=true;
                MoveFirst();
                return true;
            }
            //NWM3J9KUAAJ3
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
            setAttribute("COMPANY_ID",rsUser.getLong("COMPANY_ID"));
            setAttribute("USER_ID",rsUser.getLong("USER_ID"));
            setAttribute("USER_NAME",rsUser.getString("USER_NAME"));
            setAttribute("LOGIN_ID",rsUser.getString("LOGIN_ID"));
            setAttribute("PASSWORD",rsUser.getString("PASSWORD"));
            
            setAttribute("INTERNAL_EMAIL",rsUser.getString("INTERNAL_EMAIL"));
            setAttribute("EXTERNAL_EMAIL",rsUser.getString("EXTERNAL_EMAIL"));
            setAttribute("LOCKED",rsUser.getBoolean("LOCKED"));
            setAttribute("PASSWORD_EXPIRY_DATE",rsUser.getString("PASSWORD_EXPIRY_DATE"));
            //setAttribute("USER_TYPE",rsUser.getInt("USER_TYPE"));
            setAttribute("DEPT_ID",rsUser.getInt("DEPT_ID"));
            setAttribute("SUPERIOR_ID",rsUser.getInt("SUPERIOR_ID"));
            setAttribute("CREATED_BY",rsUser.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsUser.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsUser.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsUser.getString("MODIFIED_DATE"));
            
            //Now Populate the collection
            //first clear the collection
            colRights.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mUserID=Long.toString((long) getAttribute("USER_ID").getVal());
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_USER_RIGHTS WHERE COMPANY_ID="+mCompanyID+" AND USER_ID="+mUserID);
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsUserRights ObjRights=new clsUserRights();
                
                ObjRights.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjRights.setAttribute("USER_ID",rsTmp.getLong("USER_ID"));
                ObjRights.setAttribute("MENU_ID",rsTmp.getLong("MENU_ID"));
                ObjRights.setAttribute("FUNCTION_ID",rsTmp.getLong("FUNCTION_ID"));
                ObjRights.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjRights.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjRights.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjRights.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                colRights.put(Long.toString(Counter),ObjRights);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    //Validates user id and password
    public static boolean ValidateUser(long pCompanyID,String pLoginID,String pPassword) {
        try {
            ResultSet rsTmp;
            Connection tmpConn;
            Statement tmpStmt;
            
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT COUNT(*) AS COUNT FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND LOGIN_ID='"+pLoginID+"' AND PASSWORD='"+MBMencode.MBMen(pPassword.trim().getBytes())+"'");
            
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0) {
                
                rsTmp.close();
                tmpStmt.close();
                
                return true;
            }
            else {
                rsTmp.close();
                tmpStmt.close();
                
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static boolean ValidateUser(long pCompanyID,String pLoginID,String pPassword,String dbURL) {
        try {
            ResultSet rsTmp;
            Connection tmpConn;
            Statement tmpStmt;
            
            tmpConn=data.getConn(dbURL);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String SQL = "SELECT COUNT(*) AS COUNT FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND LOGIN_ID='"+pLoginID+"' AND PASSWORD='"+MBMencode.MBMen(pPassword.trim().getBytes())+"' ";
            //System.out.println(SQL);
            rsTmp=tmpStmt.executeQuery(SQL);
            
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0) {
                rsTmp.close();
                tmpStmt.close();
                return true;
            }
            else {
                rsTmp.close();
                tmpStmt.close();
                
                return false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    //Validates user id and password
    public static boolean IsMenuAccessible(long pCompanyID,long pUserID,long pMenuID) {
        try {
            ResultSet rsTmp;
            Connection tmpConn;
            Statement tmpStmt;
            
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT COUNT(*) AS COUNT FROM D_COM_USER_RIGHTS WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID)+" AND MENU_ID="+Long.toString(pMenuID));
            
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0) {
                rsTmp.close();
                tmpStmt.close();
                
                return true;
            }
            else {
                rsTmp.close();
                tmpStmt.close();
                
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static int getFromLoginID(int pCompanyID,String pLoginID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        int lnUserID=0;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND LOGIN_ID='"+pLoginID+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lnUserID=rsTmp.getInt("USER_ID");
            }
            
            tmpStmt.close();
            rsTmp.close();
            
            return lnUserID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    
    public static boolean userCanProcessDept(int pCompanyID,int pDeptID) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        boolean canProcess=false;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_DEPT_BUYERS WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND BUYER="+SDMLERPGLOBAL.gUserID+" AND DEPT_ID="+pDeptID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canProcess=true;
            }
            
            tmpStmt.close();
            rsTmp.close();
            
            return canProcess;
        }
        catch(Exception e) {
            return canProcess;
        }
    }
    
    public static boolean IsFunctionGranted(long pCompanyID,long pUserID,long pMenuID,long pFunctionID) {
        try {
            ResultSet rsTmp;
            Connection tmpConn;
            Statement tmpStmt;
            
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT COUNT(*) AS COUNT FROM D_COM_USER_RIGHTS WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID)+" AND FUNCTION_ID="+Long.toString(pFunctionID));
            System.out.println("*** = SELECT COUNT(*) AS COUNT FROM D_COM_USER_RIGHTS WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID)+" AND FUNCTION_ID="+Long.toString(pFunctionID));
            rsTmp.first();
            if(rsTmp.getInt("COUNT")>0) {
                rsTmp.close();
                tmpStmt.close();
                
                return true;
            }
            else {
                rsTmp.close();
                tmpStmt.close();
                
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static int getDeptID(long pCompanyID,long pUserID) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT DEPT_ID FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID));
            rsTmp.first();
            
            int DeptID= rsTmp.getInt("DEPT_ID");
            
            tmpStmt.close();
            rsTmp.close();
            
            return DeptID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public boolean IsSuperior(int pChildUser,int pParentUser) {
        try {
            if(pChildUser==pParentUser) {
                return true;
            }
            
            ChildUserFound=false;
            DepthLevel=0;
            FindChild(pChildUser,pParentUser);
        }
        catch(Exception e) {
            
        }
        
        return ChildUserFound;
    }
    
    public void FindChild(int pChildUser,int pParentUser) {
        Statement tmpStmt;
        Connection tmpConn;
        ResultSet rsTmp;
        
        try {
            DepthLevel++;
            
            tmpConn=data.getCreatedConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT USER_ID FROM D_COM_USER_MASTER WHERE SUPERIOR_ID="+pParentUser);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                
                int UserID=rsTmp.getInt("USER_ID");
                
                if(UserID==pChildUser) {
                    ChildUserFound=true;
                    return;
                }
                else {
                    if(DepthLevel>30) {
                        
                    }
                    else {
                        FindChild(pChildUser,UserID);
                    }
                }
                
                rsTmp.next();
            }
            
            
        }
        catch(Exception e) {
        }
    }
    
    
    public static boolean hasPasswordExpired(int UserID) {
        try {
            String ExpiryDate=data.getStringValueFromDB("SELECT PASSWORD_EXPIRY_DATE FROM D_COM_USER_MASTER WHERE USER_ID="+UserID);
            String CurrentDate=SDMLERPGLOBAL.getCurrentDateDB();
            
            java.sql.Date dExpiryDate=java.sql.Date.valueOf(ExpiryDate);
            java.sql.Date dCurrentDate=java.sql.Date.valueOf(CurrentDate);
            
            if(dCurrentDate.after(dExpiryDate)) {
                return true;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsUserLocked(long pCompanyID,String pLoginID,String pPassword,String dbURL) {
        try {
            ResultSet rsTmp;
            Connection tmpConn;
            Statement tmpStmt;
            
            tmpConn=data.getConn(dbURL);
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsTmp=tmpStmt.executeQuery("SELECT LOCKED FROM D_COM_USER_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND LOGIN_ID='"+pLoginID+"' AND PASSWORD='"+MBMencode.MBMen(pPassword.trim().getBytes())+"'");
            
            rsTmp.first();
            if(rsTmp.getInt("LOCKED")>0) {
                rsTmp.close();
                tmpStmt.close();
                return false;
            }
            else {
                rsTmp.close();
                tmpStmt.close();
                
                return true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
