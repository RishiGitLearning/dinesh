/*
 * ApprovalFlow.java
 *
 * Created on April 22, 2004, 4:52 PM
 */

package EITLERP;


import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import EITLERP.Logging.*;
import EITLERP.Finance.FinanceGlobal;

/**
 *
 * @author  nrpithva
 * @version
 */

public class ApprovalFlow {
    
    
    public int CompanyID=0; //Company ID
    public int ModuleID=0; //Fixed Module IDs, Refer to Document
    public String DocNo=""; //Document Number
    public String DocDate=""; //Document Date
    public String lDocNo=""; //Document Date
    public int From=0; //Editing User.
    public int To=0; //Forwarded to User
    public String Status=""; // A - Approve and forward, F - Final Approved, R - Rejected
    public String TableName=""; //Table to be updated. APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE will be updated
    public boolean IsCreator=false; //Is used at the time of new record update
    public String LastError="";
    public int HierarchyID=0; //Hierarchy Used for approval.
    public String Remarks="";
    public String FieldName="";
    public boolean ExplicitSendTo=false;
    public int SendToUser=0;
    
    public boolean UseSpecifiedURL=false;
    public String SpecificURL="";
    
    public String TestVar="";
    
    
    public static void AppendUsers(HashMap pUsers) {
        Connection tmpConn;
        ResultSet rsDocData,rsTmp;
        Statement stDocData;
        String strSQL;
        boolean UserFound=false;
        
        try {
            tmpConn=data.getConn();
            stDocData=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA WHERE DOC_NO='1'");
            rsDocData.first();
            
            for(int i=1;i<=pUsers.size();i++) {
                clsHierarchy ObjDoc=(clsHierarchy) pUsers.get(Integer.toString(i));
                //Get the Record into Variables
                int CompanyID=(int)ObjDoc.getAttribute("COMPANY_ID").getVal();
                int ModuleID=(int)ObjDoc.getAttribute("MODULE_ID").getVal();
                String DocNo=(String)ObjDoc.getAttribute("DOC_NO").getObj();
                String DocDate=(String)ObjDoc.getAttribute("DOC_DATE").getObj();
                int UserID=(int)ObjDoc.getAttribute("USER_ID").getVal();
                int SrNo=0;
                
                //Now get the Maximum Sr. No.
                strSQL="SELECT MAX(SR_NO) AS SRNO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                    SrNo=rsTmp.getInt("SRNO")+1;
                }
                
                
                //Find the duplication
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND USER_ID="+UserID);
                rsTmp.first();
                
                UserFound=false;
                
                if(rsTmp.getRow()>0) //User Exist Do not do the entry
                {
                    UserFound=true;
                }
                
                if(!UserFound) {
                    //Inser the Records Into Table
                    rsDocData.moveToInsertRow();
                    rsDocData.updateInt("COMPANY_ID",CompanyID);
                    rsDocData.updateInt("MODULE_ID",ModuleID);
                    rsDocData.updateString("DOC_NO",DocNo);
                    rsDocData.updateString("DOC_DATE",DocDate);
                    rsDocData.updateInt("USER_ID",UserID);
                    rsDocData.updateInt("SR_NO",SrNo);
                    rsDocData.updateString("STATUS","P");
                    rsDocData.updateString("TYPE","A");
                    rsDocData.updateString("REMARKS","");
                    rsDocData.updateInt("FROM_USER_ID",0);
                    rsDocData.updateString("FROM_REMARKS","");
                    rsDocData.updateString("RECEIVED_DATE","0000-00-00");
                    rsDocData.updateString("ACTION_DATE","0000-00-00");
                    rsDocData.updateBoolean("CHANGED",true);
                    rsDocData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDocData.insertRow();
                }
            }
        }
        catch(Exception e) {
            
        }
    }
    
    
    
    public static void AppendUsersEx(HashMap pUsers) {
        Connection tmpConn;
        ResultSet rsDocData,rsTmp;
        Statement stDocData;
        String strSQL;
        boolean UserFound=false;
        
        try {
            tmpConn=data.getConn();
            stDocData=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA WHERE DOC_NO='1'");
            rsDocData.first();
            
            for(int i=1;i<=pUsers.size();i++) {
                clsHierarchy ObjDoc=(clsHierarchy) pUsers.get(Integer.toString(i));
                //Get the Record into Variables
                int CompanyID=(int)ObjDoc.getAttribute("COMPANY_ID").getVal();
                int ModuleID=(int)ObjDoc.getAttribute("MODULE_ID").getVal();
                String DocNo=(String)ObjDoc.getAttribute("DOC_NO").getObj();
                String DocDate=(String)ObjDoc.getAttribute("DOC_DATE").getObj();
                int UserID=(int)ObjDoc.getAttribute("USER_ID").getVal();
                int SrNo=0;
                
                //Now get the Maximum Sr. No.
                strSQL="SELECT MAX(SR_NO) AS SRNO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"'";
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                if(rsTmp.getRow()>0) {
                    SrNo=rsTmp.getInt("SRNO")+1;
                }
                
                
                //Find the duplication
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND USER_ID="+UserID);
                rsTmp.first();
                
                UserFound=false;
                
                if(rsTmp.getRow()>0) //User Exist Do not do the entry
                {
                    UserFound=true;
                }
                
                if(!UserFound) {
                    //Inser the Records Into Table
                    rsDocData.moveToInsertRow();
                    rsDocData.updateInt("COMPANY_ID",CompanyID);
                    rsDocData.updateInt("MODULE_ID",ModuleID);
                    rsDocData.updateString("DOC_NO",DocNo);
                    rsDocData.updateString("DOC_DATE",DocDate);
                    rsDocData.updateInt("USER_ID",UserID);
                    rsDocData.updateInt("SR_NO",SrNo);
                    rsDocData.updateString("STATUS","P");
                    rsDocData.updateString("TYPE","A");
                    rsDocData.updateString("REMARKS","");
                    rsDocData.updateInt("FROM_USER_ID",0);
                    rsDocData.updateString("FROM_REMARKS","");
                    rsDocData.updateString("RECEIVED_DATE","0000-00-00");
                    rsDocData.updateString("ACTION_DATE","0000-00-00");
                    rsDocData.updateBoolean("CHANGED",true);
                    rsDocData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsDocData.insertRow();
                }
            }
        }
        catch(Exception e) {
            
        }
    }
    
    
    public static int getFromID(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND STATUS='W'";
            
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                return rsTmp.getInt("FROM_USER_ID");
            }
            else {
                return 0;
            }
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static String getFromRemarks(int pCompanyID,int pModuleID,int pUserID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND STATUS='W'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                return rsTmp.getString("FROM_REMARKS");
            }
            else {
                return "";
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static int getNextUser(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        int SrNo=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' ORDER BY SR_NO";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while(!rsTmp.isAfterLast()) {
                if(rsTmp.getString("STATUS").equals("W")) {
                    SrNo=rsTmp.getInt("SR_NO");
                }
                rsTmp.next();
            }
            
            if(SrNo!=0) //We got Sr. No. of current user id. Get Next no.
            {
                //Get Next No.
                stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND SR_NO="+Integer.toString(SrNo+1);
                rsTmp=stTmp.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Next User Found
                    return rsTmp.getInt("USER_ID");
                }
                else {
                    //Current User is Last user
                    return 0;
                }
            }
            
        }
        catch(Exception e) {
            return 0;
        }
        
        return 0;
    }
    
    
    public static int getWaitingUser(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        int UserID=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND STATUS='W'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                UserID=rsTmp.getInt("USER_ID");
            }
            
            return UserID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    public static String getWaitingReceivedDate(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        String ReceivedDate="";
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND STATUS='W' ";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ReceivedDate=rsTmp.getString("RECEIVED_DATE");
            }
            
            return ReceivedDate;
        }
        catch(Exception e) {
            return ReceivedDate;
        }
    }
    
    
    public static int getCreator(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        int UserID=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND TYPE='C'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                UserID=rsTmp.getInt("USER_ID");
            }
            
            return UserID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    
    
    
    public static HashMap getDocumentFlow(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        HashMap List=new HashMap();
        int UserID=0;
        //Returns From user id where
        try {
            
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO ='"+pDocNo+"' ORDER BY SR_NO ";
            //System.out.println("String"+strSQL);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    clsDocFlow ObjFlow=new clsDocFlow();
                    
                    ObjFlow.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                    ObjFlow.setAttribute("MODULE_ID",rsTmp.getInt("MODULE_ID"));
                    ObjFlow.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    ObjFlow.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                    ObjFlow.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    
                    if(rsTmp.getString("STATUS").equals("A")) {
                        
                        ObjFlow.setAttribute("STATUS","Approved");
                        
                    }
                    
                    if(rsTmp.getString("STATUS").equals("F")) {
                        
                        ObjFlow.setAttribute("STATUS","Final Approved");
                        
                    }
                    
                    if(rsTmp.getString("STATUS").equals("W")) {
                        
                        ObjFlow.setAttribute("STATUS","Waiting");
                        
                    }
                    
                    if(rsTmp.getString("STATUS").equals("R")) {
                        
                        ObjFlow.setAttribute("STATUS","Rejected");
                        
                    }
                    
                    if(rsTmp.getString("STATUS").equals("P")) {
                        ObjFlow.setAttribute("STATUS","Pending");
                    }
                    
                    if(rsTmp.getString("STATUS").equals("C")) {
                        ObjFlow.setAttribute("STATUS","Skiped");
                    }
                    
                    ObjFlow.setAttribute("TYPE",rsTmp.getString("TYPE"));
                    ObjFlow.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                    ObjFlow.setAttribute("DEPT_ID",clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, rsTmp.getInt("USER_ID")));
                    ObjFlow.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                    ObjFlow.setAttribute("ACTION_DATE",rsTmp.getString("ACTION_DATE"));
                    
                    List.put(Integer.toString(List.size()+1),ObjFlow);
                    
                    rsTmp.next();
                }
            }
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    public static HashMap getDocumentList(int pCompanyID,int pModuleID,int pDeptID) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        HashMap List=new HashMap();
        int UserID=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT DISTINCT(DOC_NO),DOC_DATE FROM D_COM_DOC_DATA,D_COM_USER_MASTER WHERE D_COM_DOC_DATA.USER_ID=D_COM_USER_MASTER.USER_ID AND D_COM_DOC_DATA.COMPANY_ID=D_COM_USER_MASTER.COMPANY_ID AND STATUS<>'F' AND D_COM_DOC_DATA.COMPANY_ID="+pCompanyID+" AND D_COM_USER_MASTER.DEPT_ID="+pDeptID+" AND D_COM_DOC_DATA.MODULE_ID="+pModuleID+" AND D_COM_DOC_DATA.TYPE='C' ";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDocFlow ObjFlow=new clsDocFlow();
                    ObjFlow.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                    ObjFlow.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                    List.put(Integer.toString(List.size()+1),ObjFlow);
                    rsTmp.next();
                }
            }
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    public static HashMap getRemainingUsers(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        HashMap List=new HashMap();
        int Counter=0;
        int SrNo=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT SR_NO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND STATUS='W'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                SrNo=rsTmp.getInt("SR_NO");
                
                stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND SR_NO>"+Integer.toString(SrNo)+" ORDER BY SR_NO";
                rsTmp=stTmp.executeQuery(strSQL);
                rsTmp.first();
                if(rsTmp.getRow()>0){
                    while(!rsTmp.isAfterLast()) {
                        Counter++;
                        clsUser ObjUser=new clsUser();
                        
                        ObjUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                        String lUserName=clsUser.getUserName(pCompanyID, rsTmp.getInt("USER_ID"));
                        
                        if(lUserName.trim().equals("")) {
                            ObjUser.setAttribute("USER_NAME","To be decided");
                        }
                        else {
                            ObjUser.setAttribute("USER_NAME",lUserName);
                        }
                        
                        List.put(Integer.toString(Counter),ObjUser);
                        rsTmp.next();
                    }
                }
            }
            return List;
        }
        catch(Exception e) {
            e.printStackTrace();
            return List;
        }
    }
    
    
    
    public static HashMap getUserList(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        HashMap List=new HashMap();
        int Counter=0;
        int SrNo=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement();
            
            strSQL="SELECT USER_ID FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                while(!rsTmp.isAfterLast()) {
                    Counter++;
                    clsUser ObjUser=new clsUser();
                    
                    ObjUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
                    String lUserName=clsUser.getUserName(pCompanyID, rsTmp.getInt("USER_ID"));
                    ObjUser.setAttribute("USER_NAME",lUserName);
                    
                    List.put(Integer.toString(Counter),ObjUser);
                    rsTmp.next();
                }
            }
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    
    
    public static int getFinalApprover(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        int UserID=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement();
            
            strSQL="SELECT USER_ID FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND STATUS='F'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                UserID=rsTmp.getInt("USER_ID");
            }
            return UserID;
        }
        catch(Exception e) {
            return UserID;
        }
    }
    
    
    public static String getCreatorStatus(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        String Status="";
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT STATUS FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"' AND TYPE='C'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                Status=rsTmp.getString("STATUS");
            }
            return Status;
        }
        catch(Exception e) {
            return Status;
        }
    }
    
    
    //This routine is specifically for those users which are appended dynamically
    //after document creation. i.e. in case of MIR once hierarchy selected, final
    //approvers are decided based on the departments present in the MIR.
    //The definitions of the users (final approvers) are stored in D_COM_DOC_APPROVERS
    //which is linked with the department master.
    
    public static boolean CanFinalApprove(int pCompanyID,int pModuleID,int pUserID) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        boolean CanFinalApprove=false;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT FINAL_APPROVER FROM D_COM_DOC_APPROVERS WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND USER_ID="+pUserID+ " AND FINAL_APPROVER=1";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                CanFinalApprove=rsTmp.getBoolean("FINAL_APPROVER");
            }
            return CanFinalApprove;
        }
        catch(Exception e) {
            return CanFinalApprove;
        }
    }
    
    public static boolean IsCreator(int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        String Status="";
        boolean Creator=false;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT STATUS FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND TYPE='C' AND USER_ID="+EITLERPGLOBAL.gUserID;
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                Creator=true;
            }
            return Creator;
        }
        catch(Exception e) {
            return Creator;
        }
    }
    
    
    public static int getRecordCount(int pCompanyID,int pModuleID,String pDocNo) {
        Connection Conn;
        ResultSet rsTmp;
        Statement stTmp;
        String strSQL;
        int lnCount=0;
        
        //Returns From user id where
        try {
            Conn=data.getConn();
            stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            strSQL="SELECT COUNT(*) AS RECCOUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"'";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                lnCount=rsTmp.getInt("RECCOUNT");
            }
            return lnCount;
        }
        catch(Exception e) {
            return lnCount;
        }
    }
    
    public static void RemoveRecords(int pCompanyID,int pModuleID,String pDocNo) {
        //Removes All the records
        try {
            data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID="+Integer.toString(pModuleID)+" AND DOC_NO='"+pDocNo+"'");
        }
        catch(Exception e) {
        }
    }
    
    
    public static boolean IsOnceRejectedDoc(int pCompanyID,int pModuleID,String pDocNo) {
        //Removes All the records
        boolean Rejected=false;
        try {
            String TableName=clsModules.getHeaderTableName(pCompanyID, pModuleID);
            String FieldName=clsModules.getDocNoFieldName(pCompanyID, pModuleID);
            
            ResultSet rsTmp;
            
            rsTmp=data.getResult("SELECT REJECTED FROM "+TableName+" WHERE COMPANY_ID="+pCompanyID+" AND "+FieldName+"='"+pDocNo+"'");
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Rejected=rsTmp.getBoolean("REJECTED");
            }
        }
        catch(Exception e) {
        }
        return Rejected;
    }
    
    
    public static boolean IsOnceRejectedDoc(int pCompanyID,int pModuleID,String pDocNo,String pURL) {
        //Removes All the records
        boolean Rejected=false;
        try {
            String TableName=clsModules.getHeaderTableName(pCompanyID, pModuleID);
            String FieldName=clsModules.getDocNoFieldName(pCompanyID, pModuleID);
            
            ResultSet rsTmp;
            
            rsTmp=data.getResult("SELECT REJECTED FROM "+TableName+" WHERE COMPANY_ID="+pCompanyID+" AND "+FieldName+"='"+pDocNo+"'",pURL);
            
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Rejected=rsTmp.getBoolean("REJECTED");
            }
        }
        catch(Exception e) {
        }
        return Rejected;
    }
    
    
    public static boolean IncludeUserInApproval(int pCompanyID,int pModuleID,String pDocNo,int pUserID,int pFromUserID) {
        //Removes All the records
        boolean Include=false;
        boolean FirstDecision=false;
        int FromSrNo=0;
        int ToSrNo=0;
        
        try {
            
            if(pUserID==pFromUserID) {
                return false;
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pFromUserID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                FromSrNo=rsTmp.getInt("SR_NO");
                
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ToSrNo=rsTmp.getInt("SR_NO");
                    if(ToSrNo>=FromSrNo) {
                        Include=true;
                        FirstDecision=true;
                    }
                }
                
                
                if(IsOnceRejectedDoc(pCompanyID,pModuleID,pDocNo,FinanceGlobal.FinURL)) {
                    rsTmp=data.getResult("SELECT MAX(SR_NO) AS MAX_SR_NO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND STATUS='V'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        int MaxSrNo=rsTmp.getInt("MAX_SR_NO");
                        
                        if(MaxSrNo==0||MaxSrNo==1) {
                            //This will happen in old cases.
                            Include=FirstDecision; //For those cases only
                        }
                        else {
                            if(ToSrNo>(MaxSrNo)) {
                                //One Special Case if From user ID is Higher than Max Sr. No.
                                Include=false;
                            }
                            
                        }
                    }
                }
                
            }
        }
        catch(Exception e) {
            
        }
        return Include;
    }
    
    public static boolean IncludeUserInApproval(int pCompanyID,int pModuleID,String pDocNo,int pUserID,int pFromUserID,String dbURL) {
        //Removes All the records
        boolean Include=false;
        boolean FirstDecision=false;
        int FromSrNo=0;
        int ToSrNo=0;
        
        try {
            
            if(pUserID==pFromUserID) {
                return false;
            }
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pFromUserID,dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                FromSrNo=rsTmp.getInt("SR_NO");
                
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID,dbURL);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ToSrNo=rsTmp.getInt("SR_NO");
                    if(ToSrNo>=FromSrNo) {
                        Include=true;
                        FirstDecision=true;
                    }
                }
                
                
                if(IsOnceRejectedDoc(pCompanyID,pModuleID,pDocNo,dbURL)) {
                    rsTmp=data.getResult("SELECT MAX(SR_NO) AS MAX_SR_NO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND STATUS='V'", dbURL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        int MaxSrNo=rsTmp.getInt("MAX_SR_NO");
                        
                        if(MaxSrNo==0||MaxSrNo==1) {
                            //This will happen in old cases.
                            Include=FirstDecision; //For those cases only
                        }
                        else {
                            if(ToSrNo>(MaxSrNo)) {
                                //One Special Case if From user ID is Higher than Max Sr. No.
                                Include=false;
                            }
                            
                        }
                    }
                }
                
            }
        }
        catch(Exception e) {
            
        }
        return Include;
    }
    
    public static boolean IncludeUserInRejection(int pCompanyID,int pModuleID,String pDocNo,int pUserID,int pFromUserID) {
        //Removes All the records
        boolean Include=false;
        int FromSrNo=0;
        
        try {
            if(pUserID==pFromUserID) {
                return false;
            }
            
            
            ResultSet rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pFromUserID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                FromSrNo=rsTmp.getInt("SR_NO");
                
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    if(rsTmp.getInt("SR_NO")<FromSrNo) {
                        Include=true;
                    }
                }
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        return Include;
    }
    
    
    public static boolean HierarchyUpdateNeeded(int pCompanyID,int pModuleID,String pDocNo,int pCurrentHierarchyID,int OldHierarchy,int pUserID,String aStatus) {
        //Removes All the records
        int OriginalCreator=0;
        String TableName="";
        String FieldName="";
        boolean needChange=false;
        
        
        try {
            OriginalCreator=getCreator(pCompanyID, pModuleID, pDocNo);
            
            if(pUserID==OriginalCreator && (aStatus.equals("A")||aStatus.equals("H"))) {
                //Only if document is being forwarded by original Creator
                TableName = clsModules.getHeaderTableName(pCompanyID, pModuleID);
                TableName=TableName+"_H";
                FieldName=clsModules.getDocNoFieldName(pCompanyID, pModuleID);
                
                
                if(OldHierarchy!=pCurrentHierarchyID) {
                    needChange=true;
                }
                
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        
        //=== TO CHANGE HIERARCHY UNCOMMENT FOLLOWING LINE ===//
        
        //needChange=true;
        //===================================================//
        
        return needChange;
    }
    
    
    
    
    
    
    
    
    public boolean UpdateFlow() {
        
        boolean Done=false;
        int FromSrNo=0;
        int CurrentUserSrNo=0; //This will be derived automatically by this module from logged in user id
        int ToSrNo=0;
        int CreatorSrNo=0;
        String strSQL="";
        
        Connection Conn;
        Conn=data.getConn();
        
        ResultSet rsHierarchy,rsTmp;
        Statement stHierarchy,stTmp;
        
        ResultSet rsDocData;
        Statement stDocData;
        
        int DocSentTo=0;
        
        clsSystemLog objLog=new clsSystemLog();
        
        try {
            
            Remarks=Remarks.replaceAll("'"," ");
            Remarks=Remarks.replaceAll("\""," ");
            
            strSQL="SELECT A.COMPANY_ID,A.HIERARCHY_ID,A.MODULE_ID,B.USER_ID,B.APPROVAL_SEQUENCE FROM D_COM_HIERARCHY A,D_COM_HIERARCHY_RIGHTS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.HIERARCHY_ID=B.HIERARCHY_ID AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MODULE_ID="+ModuleID+" AND A.HIERARCHY_ID="+HierarchyID+" ORDER BY B.APPROVAL_SEQUENCE";
            //System.out.println(strSQL);
            
            stHierarchy=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHierarchy=stHierarchy.executeQuery(strSQL);
            rsHierarchy.first();
            
            stDocData=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA LIMIT 1");
            rsDocData.first();
            
            //If User is creator of the document
            if(IsCreator&&rsHierarchy.getRow()>0) {
                //String str = "SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" "+
                //" AND DOC_NO='"+DocNo+"' AND USER_ID="+rsHierarchy.getInt("USER_ID")+" AND SR_NO="+rsHierarchy.getInt("APPROVAL_SEQUENCE");
                String str = "SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID=2 AND MODULE_ID="+ModuleID+" "+
                " AND DOC_NO='"+DocNo+"' AND USER_ID="+rsHierarchy.getInt("USER_ID")+" AND SR_NO="+rsHierarchy.getInt("APPROVAL_SEQUENCE");
                if (!data.IsRecordExist(str)) {
                    //Insert Records into D_COM_DOC_DATA from the Hierarchy
                    while(!rsHierarchy.isAfterLast()) {
                        rsDocData.moveToInsertRow();
                        rsDocData.updateInt("COMPANY_ID",CompanyID);
                        rsDocData.updateInt("MODULE_ID",ModuleID);
                        rsDocData.updateString("DOC_NO",DocNo);
                        //rsDocData.updateString("DOC_DATE",DocDate);
                        
                        if(DocDate.equals(""))
                            DocDate="0000-00-00";
                            
                        rsDocData.updateString("DOC_DATE",DocDate);
                        
                        rsDocData.updateInt("USER_ID",rsHierarchy.getInt("USER_ID"));
                        rsDocData.updateString("STATUS","P"); //Pending
                        
                        if(rsHierarchy.getInt("USER_ID")==From  && rsHierarchy.getInt("APPROVAL_SEQUENCE")==1 ) {
                            rsDocData.updateString("TYPE","C"); //User is creator of the document
                        }
                        else {
                            rsDocData.updateString("TYPE","A"); //User is creator of the document
                        }
                        /*if(rsHierarchy.getInt("USER_ID")==From) {
                            rsDocData.updateString("TYPE","C"); //User is creator of the document
                        }
                        else {
                            rsDocData.updateString("TYPE","A"); //User is creator of the document
                        }*/
                        rsDocData.updateString("REMARKS",""); //Will be updated later
                        
                        rsDocData.updateInt("SR_NO",rsHierarchy.getInt("APPROVAL_SEQUENCE"));
                        
                        rsDocData.updateString("FROM_REMARKS","");
                        rsDocData.updateInt("FROM_USER_ID",0);
                        rsDocData.updateString("RECEIVED_DATE","0000-00-00");
                        rsDocData.updateString("ACTION_DATE","0000-00-00");
                        
                        rsDocData.updateBoolean("CHANGED",true);
                        rsDocData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsDocData.insertRow();
                        
                        rsHierarchy.next();
                    }
                }
                rsDocData.close();
                stDocData.close();
            }
            
            stDocData=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' ORDER BY SR_NO ");
            
            rsDocData.first();
            
            //by mufffy for the HIERARCHY ID 837 BRD and 681 ANK Cash Voucher
            if((HierarchyID == 837 && EITLERPGLOBAL.gCompanyID == 2) || (HierarchyID == 681 && EITLERPGLOBAL.gCompanyID == 3)) {
                while(!rsDocData.isAfterLast()) {
                    if(rsDocData.getInt("USER_ID")==From ) {
                        if(!rsDocData.getString("TYPE").equals("A") || rsDocData.getString("STATUS").equals("W")){
                            FromSrNo=rsDocData.getInt("SR_NO");
                        }
                    }
                    if(rsDocData.getInt("USER_ID")==To) {
                        if(!rsDocData.getString("STATUS").equals("W")){
                            ToSrNo=rsDocData.getInt("SR_NO");
                        }
                    }
                    if(rsDocData.getString("TYPE").equals("C")) {
                        CreatorSrNo=rsDocData.getInt("SR_NO");
                    }
                    rsDocData.next();
                }
                
            }//end muffy
            else{
                //Find out the From User ID record
                while(!rsDocData.isAfterLast()) {
                    if(rsDocData.getInt("USER_ID")==From) {
                        FromSrNo=rsDocData.getInt("SR_NO");
                    }
                    
                    if(rsDocData.getInt("USER_ID")==To) {
                        ToSrNo=rsDocData.getInt("SR_NO");
                    }
                    
                    if(rsDocData.getString("TYPE").equals("C")) {
                        CreatorSrNo=rsDocData.getInt("SR_NO");
                    }
                    
                    rsDocData.next();
                }
            }
            rsTmp=data.getResult("SELECT SR_NO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND USER_ID="+EITLERPGLOBAL.gNewUserID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CurrentUserSrNo=rsTmp.getInt("SR_NO");
            }
            
            
            //Updating status of approval
            if(Status.equals("A")) //Approve and Forward
            {
                //Set the A Flag for From User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='A',REMARKS='"+Remarks+"',ACTION_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+Integer.toString(FromSrNo);
                data.Execute(strSQL);
                
                //Set the W Flag for To User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='W',FROM_USER_ID="+Integer.toString(From)+",FROM_REMARKS='"+Remarks+"',RECEIVED_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+ToSrNo;
                data.Execute(strSQL);
                
                //Set Completed for users between From and To
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='C',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO>"+Integer.toString(FromSrNo)+" AND SR_NO<"+Integer.toString(ToSrNo);
                data.Execute(strSQL);
                
                //Set Pending for users greater than To
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='P',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO>"+Integer.toString(ToSrNo);
                data.Execute(strSQL);
                
                //Now check whether document sent successfully to recipient. If not set it to current user only
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND STATUS='W'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Yes. Successfull
                }
                else {
                    //Set it to current user only
                    data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND SR_NO="+CurrentUserSrNo);
                }
                //============== Check Completed =======================//
                
                
            }
            
            
            if(Status.equals("F")) //Final Approval
            {
                //Set the A Flag for From User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='F',REMARKS='"+Remarks+"',ACTION_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+EITLERPGLOBAL.gNewUserID;
                data.Execute(strSQL);
                
                //Set Approved Flag in Table Specified
                strSQL="UPDATE "+TableName+" SET APPROVED=1,APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                }
                else {
                    data.Execute(strSQL);
                }
                
                //========= Check again for the update of final approval ============//
                
                String reCheck="SELECT APPROVED FROM "+TableName+" WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    if(data.getIntValueFromDB(reCheck,SpecificURL)==0) {
                        objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
                        objLog.ModuleName="ApprovalFlow";
                        objLog.Method="UpdateFlow";
                        objLog.Trace=" Logged on "+EITLERPGLOBAL.getCurrentDate()+": Update to table did not work. Approval Flag not set by query. Query was "+strSQL;
                        objLog.Remarks="";
                        objLog.LogEvent();
                        
                        data.Execute(strSQL,SpecificURL);
                    }
                }
                else {
                    if(data.getIntValueFromDB(reCheck)==0) {
                        objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
                        objLog.ModuleName="ApprovalFlow";
                        objLog.Method="UpdateFlow";
                        objLog.Trace=" Logged on "+EITLERPGLOBAL.getCurrentDate()+": Update to table did not work. Approval Flag not set by query. Query was "+strSQL;
                        objLog.Remarks="";
                        objLog.LogEvent();
                        
                        data.Execute(strSQL);
                    }
                }
                //=====================================================================//
            }
            
            
            
            if(Status.equals("R")) //Rejected
            {
                
                boolean Continue=false;
                
                if(ExplicitSendTo) {
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        CreatorSrNo=rsTmp.getInt("SR_NO");
                    }
                }
                
                if(ExplicitSendTo) {
                    //Check that User is in the hierarchy
                    System.out.println("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()<=0) {
                        HashMap newUser=new HashMap();
                        clsHierarchy ObjUser=new clsHierarchy();
                        ObjUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        ObjUser.setAttribute("MODULE_ID",ModuleID);
                        ObjUser.setAttribute("DOC_NO",DocNo);
                        ObjUser.setAttribute("DOC_DATE",DocDate);
                        ObjUser.setAttribute("USER_ID",To);
                        newUser.put("1", ObjUser);
                        AppendUsers(newUser);
                    }
                    
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        CreatorSrNo=rsTmp.getInt("SR_NO");
                    }
                }
                
                
                
                
                //Set the W Flag for Creator
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='W',RECEIVED_DATE=CURDATE(),ACTION_DATE='0000-00-00',FROM_USER_ID="+Integer.toString(From)+",FROM_REMARKS='"+Remarks+" - Rejected Document"+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+Integer.toString(CreatorSrNo);
                data.Execute(strSQL);
                
                //System.out.println(strSQL);
                
                //Set the P Flag for all other users
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='V',CHANGED=1,CHANGED_DATE=CURDATE(),REMARKS='',RECEIVED_DATE='',ACTION_DATE='' WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO<>"+Integer.toString(CreatorSrNo)+" AND SR_NO<="+FromSrNo;
                data.Execute(strSQL);
                
                //Set Approved Flag in Table Specified
                strSQL="UPDATE "+TableName+" SET APPROVED=0,REJECTED=1,REJECTED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                }
                else {
                    data.Execute(strSQL);
                }
                
                
                //Now check whether document sent successfully to recipient. If not set it to current user only
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND STATUS='W'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Yes. Successfull
                }
                else {
                    //Set it to current user only
                    data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND SR_NO="+CurrentUserSrNo);
                }
                //============== Check Completed =======================//
                
            }
            return true;
        }
        catch(Exception e) {
            
            e.printStackTrace();
            objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
            objLog.ModuleName="ApprovalFlow";
            objLog.Method="UpdateFlow";
            objLog.Trace=e.getMessage();
            objLog.Remarks="";
            objLog.LogEvent();
            
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean UpdateFlow(String dbURL) {
        
        boolean Done=false;
        int FromSrNo=0;
        int CurrentUserSrNo=0; //This will be derived automatically by this module from logged in user id
        int ToSrNo=0;
        int CreatorSrNo=0;
        String strSQL="";
        
        Connection Conn;
        Conn=data.getConn(dbURL);
        
        ResultSet rsHierarchy,rsTmp;
        Statement stHierarchy,stTmp;
        
        ResultSet rsDocData;
        Statement stDocData;
        
        int DocSentTo=0;
        
        clsSystemLog objLog=new clsSystemLog();
        
        try {
            
            Remarks=Remarks.replaceAll("'"," ");
            Remarks=Remarks.replaceAll("\""," ");
            
            strSQL="SELECT A.COMPANY_ID,A.HIERARCHY_ID,A.MODULE_ID,B.USER_ID,B.APPROVAL_SEQUENCE FROM D_COM_HIERARCHY A,D_COM_HIERARCHY_RIGHTS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.HIERARCHY_ID=B.HIERARCHY_ID AND A.COMPANY_ID="+CompanyID+" AND A.MODULE_ID="+ModuleID+" AND A.HIERARCHY_ID="+HierarchyID+" ORDER BY B.APPROVAL_SEQUENCE";
            //System.out.println(strSQL);
            
            stHierarchy=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHierarchy=stHierarchy.executeQuery(strSQL);
            rsHierarchy.first();
            
            stDocData=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA LIMIT 1");
            rsDocData.first();
            
            //If User is creator of the document
            if(IsCreator&&rsHierarchy.getRow()>0) {
                String str = "SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" "+
                " AND DOC_NO='"+DocNo+"' AND USER_ID="+rsHierarchy.getInt("USER_ID")+" AND SR_NO="+rsHierarchy.getInt("APPROVAL_SEQUENCE");
                if (!data.IsRecordExist(str)) {
                    //Insert Records into D_COM_DOC_DATA from the Hierarchy
                    while(!rsHierarchy.isAfterLast()) {
                        rsDocData.moveToInsertRow();
                        rsDocData.updateInt("COMPANY_ID",CompanyID);
                        rsDocData.updateInt("MODULE_ID",ModuleID);
                        rsDocData.updateString("DOC_NO",DocNo);
                        
                        if(DocDate.equals(""))
                            DocDate="0000-00-00";
                        
                        rsDocData.updateString("DOC_DATE",DocDate);
                        rsDocData.updateInt("USER_ID",rsHierarchy.getInt("USER_ID"));
                        rsDocData.updateString("STATUS","P"); //Pending
                        
                        if(rsHierarchy.getInt("USER_ID")==From) {
                            rsDocData.updateString("TYPE","C"); //User is creator of the document
                        }
                        else {
                            rsDocData.updateString("TYPE","A"); //User is creator of the document
                        }
                        rsDocData.updateString("REMARKS",""); //Will be updated later
                        
                        rsDocData.updateInt("SR_NO",rsHierarchy.getInt("APPROVAL_SEQUENCE"));
                        
                        rsDocData.updateString("FROM_REMARKS","");
                        rsDocData.updateInt("FROM_USER_ID",0);
                        rsDocData.updateString("RECEIVED_DATE","0000-00-00");
                        rsDocData.updateString("ACTION_DATE","0000-00-00");
                        
                        rsDocData.updateBoolean("CHANGED",true);
                        rsDocData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsDocData.insertRow();
                        
                        rsHierarchy.next();
                    }
                }
                rsDocData.close();
                stDocData.close();
            }
            
            stDocData=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' ORDER BY SR_NO ");
            
            rsDocData.first();
            //Find out the From User ID record
            //-----------
            int fromt=0;
            int fromf=0;
            int fromc=0;
            //-----------
            while(!rsDocData.isAfterLast()) {
                if(rsDocData.getInt("USER_ID")==From) {
                    FromSrNo=rsDocData.getInt("SR_NO");
                    
                }
                
                if(rsDocData.getInt("USER_ID")==To) {
                    ToSrNo=rsDocData.getInt("SR_NO");
                    
                }
                
                if(rsDocData.getString("TYPE").equals("C")) {
                    CreatorSrNo=rsDocData.getInt("SR_NO");
                }
                
                rsDocData.next();
            }
            
            
            rsTmp=data.getResult("SELECT SR_NO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND USER_ID="+EITLERPGLOBAL.gNewUserID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CurrentUserSrNo=rsTmp.getInt("SR_NO");
            }
            
            
            //Updating status of approval
            if(Status.equals("A")) //Approve and Forward
            {
                //Set the A Flag for From User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='A',REMARKS='"+Remarks+"',ACTION_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+Integer.toString(FromSrNo);
                data.Execute(strSQL);
                
                //Set the W Flag for To User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='W',FROM_USER_ID="+Integer.toString(From)+",FROM_REMARKS='"+Remarks+"',RECEIVED_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+ToSrNo;
                data.Execute(strSQL);
                
                //Set Completed for users between From and To
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='C',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO>"+Integer.toString(FromSrNo)+" AND SR_NO<"+Integer.toString(ToSrNo);
                data.Execute(strSQL);
                
                //Set Pending for users greater than To
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='P',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO>"+Integer.toString(ToSrNo);
                data.Execute(strSQL);
                
                //Now check whether document sent successfully to recipient. If not set it to current user only
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND STATUS='W'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Yes. Successfull
                }
                else {
                    //Set it to current user only
                    data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND SR_NO="+CurrentUserSrNo);
                }
                //============== Check Completed =======================//
                
                
            }
            
            
            if(Status.equals("F")) //Final Approval
            {
                //Set the A Flag for From User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='F',REMARKS='"+Remarks+"',ACTION_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+From;
		if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                } else {
                    data.Execute(strSQL);
                }
                //data.Execute(strSQL);
                
                //Set Approved Flag in Table Specified
                strSQL="UPDATE "+TableName+" SET APPROVED=1,APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                    strSQL="UPDATE "+TableName+"_H SET APPROVED=1,APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                    data.Execute(strSQL,SpecificURL);
                } else {
                    data.Execute(strSQL);
                    strSQL="UPDATE "+TableName+"_H SET APPROVED=1,APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                    data.Execute(strSQL,SpecificURL);
                }
                
                //========= Check again for the update of final approval ============//
                
                String reCheck="SELECT APPROVED FROM "+TableName+" WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    if(data.getIntValueFromDB(reCheck,SpecificURL)==0) {
                        objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
                        objLog.ModuleName="ApprovalFlow";
                        objLog.Method="UpdateFlow";
                        objLog.Trace=" Logged on "+EITLERPGLOBAL.getCurrentDate()+": Update to table did not work. Approval Flag not set by query. Query was "+strSQL;
                        objLog.Remarks="";
                        objLog.LogEvent();
                        
                        data.Execute(strSQL,SpecificURL);
                    }
                }
                else {
                    if(data.getIntValueFromDB(reCheck)==0) {
                        objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
                        objLog.ModuleName="ApprovalFlow";
                        objLog.Method="UpdateFlow";
                        objLog.Trace=" Logged on "+EITLERPGLOBAL.getCurrentDate()+": Update to table did not work. Approval Flag not set by query. Query was "+strSQL;
                        objLog.Remarks="";
                        objLog.LogEvent();
                        
                        data.Execute(strSQL);
                    }
                }
                //=====================================================================//
            }
            
            
            
            if(Status.equals("R")) //Rejected
            {
                
                boolean Continue=false;
                
                if(ExplicitSendTo) {
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        CreatorSrNo=rsTmp.getInt("SR_NO");
                    }
                }
                
                if(ExplicitSendTo) {
                    //Check that User is in the hierarchy
                    System.out.println("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()<=0) {
                        HashMap newUser=new HashMap();
                        clsHierarchy ObjUser=new clsHierarchy();
                        ObjUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        ObjUser.setAttribute("MODULE_ID",ModuleID);
                        ObjUser.setAttribute("DOC_NO",DocNo);
                        ObjUser.setAttribute("DOC_DATE",DocDate);
                        ObjUser.setAttribute("USER_ID",To);
                        newUser.put("1", ObjUser);
                        AppendUsers(newUser);
                    }
                    
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        CreatorSrNo=rsTmp.getInt("SR_NO");
                    }
                }
                
                
                
                
                //Set the W Flag for Creator
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='W',RECEIVED_DATE=CURDATE(),ACTION_DATE='0000-00-00',FROM_USER_ID="+Integer.toString(From)+",FROM_REMARKS='"+Remarks+" - Rejected Document"+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+Integer.toString(CreatorSrNo);
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                } else {
                    data.Execute(strSQL);
                }
                
                
                //System.out.println(strSQL);
                
                //Set the P Flag for all other users
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='V',CHANGED=1,CHANGED_DATE=CURDATE(),REMARKS='',RECEIVED_DATE='',ACTION_DATE='' WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO<>"+Integer.toString(CreatorSrNo)+" AND SR_NO<="+FromSrNo;
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                } else {
                    data.Execute(strSQL);
                }
                
                //Set Approved Flag in Table Specified
                strSQL="UPDATE "+TableName+" SET APPROVED=0,REJECTED=1,REJECTED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                } else {
                    data.Execute(strSQL);
                }
                
                
                //Now check whether document sent successfully to recipient. If not set it to current user only
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND STATUS='W'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Yes. Successfull
                } else {
                    //Set it to current user only
                    data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND SR_NO="+CurrentUserSrNo);
                }
                //============== Check Completed =======================//
                
            }
            return true;
        }
        catch(Exception e) {
            
            e.printStackTrace();
            objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
            objLog.ModuleName="ApprovalFlow";
            objLog.Method="UpdateFlow";
            objLog.Trace=e.getMessage();
            objLog.Remarks="";
            objLog.LogEvent();
            
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean UpdateFlowOLD(String dbURL) {
        
        boolean Done=false;
        int FromSrNo=0;
        int CurrentUserSrNo=0; //This will be derived automatically by this module from logged in user id
        int ToSrNo=0;
        int CreatorSrNo=0;
        String strSQL="";
        
        Connection Conn;
        Conn=data.getConn(dbURL);
        
        ResultSet rsHierarchy,rsTmp;
        Statement stHierarchy,stTmp;
        
        ResultSet rsDocData;
        Statement stDocData;
        
        int DocSentTo=0;
        
        clsSystemLog objLog=new clsSystemLog();
        
        try {
            
            Remarks=Remarks.replaceAll("'"," ");
            Remarks=Remarks.replaceAll("\""," ");
            
            strSQL="SELECT A.COMPANY_ID,A.HIERARCHY_ID,A.MODULE_ID,B.USER_ID,B.APPROVAL_SEQUENCE FROM D_COM_HIERARCHY A,D_COM_HIERARCHY_RIGHTS B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.HIERARCHY_ID=B.HIERARCHY_ID AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.MODULE_ID="+ModuleID+" AND A.HIERARCHY_ID="+HierarchyID+" ORDER BY B.APPROVAL_SEQUENCE";
            //System.out.println(strSQL);
            
            stHierarchy=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHierarchy=stHierarchy.executeQuery(strSQL);
            rsHierarchy.first();
            
            stDocData=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA LIMIT 1");
            rsDocData.first();
            
            //If User is creator of the document
            if(IsCreator&&rsHierarchy.getRow()>0) {
                String str = "SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" "+
                " AND DOC_NO='"+DocNo+"' AND USER_ID="+rsHierarchy.getInt("USER_ID")+" AND SR_NO="+rsHierarchy.getInt("APPROVAL_SEQUENCE");
                if (!data.IsRecordExist(str)) {
                    //Insert Records into D_COM_DOC_DATA from the Hierarchy
                    while(!rsHierarchy.isAfterLast()) {
                        rsDocData.moveToInsertRow();
                        rsDocData.updateInt("COMPANY_ID",CompanyID);
                        rsDocData.updateInt("MODULE_ID",ModuleID);
                        rsDocData.updateString("DOC_NO",DocNo);
                        rsDocData.updateString("DOC_DATE",DocDate);
                        rsDocData.updateInt("USER_ID",rsHierarchy.getInt("USER_ID"));
                        rsDocData.updateString("STATUS","P"); //Pending
                        
                        if(rsHierarchy.getInt("USER_ID")==From) {
                            rsDocData.updateString("TYPE","C"); //User is creator of the document
                        }
                        else {
                            rsDocData.updateString("TYPE","A"); //User is creator of the document
                        }
                        rsDocData.updateString("REMARKS",""); //Will be updated later
                        
                        rsDocData.updateInt("SR_NO",rsHierarchy.getInt("APPROVAL_SEQUENCE"));
                        
                        rsDocData.updateString("FROM_REMARKS","");
                        rsDocData.updateInt("FROM_USER_ID",0);
                        rsDocData.updateString("RECEIVED_DATE","0000-00-00");
                        rsDocData.updateString("ACTION_DATE","0000-00-00");
                        
                        rsDocData.updateBoolean("CHANGED",true);
                        rsDocData.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsDocData.insertRow();
                        
                        rsHierarchy.next();
                    }
                }
                rsDocData.close();
                stDocData.close();
            }
            
            stDocData=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDocData=stDocData.executeQuery("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' ORDER BY SR_NO ");
            
            rsDocData.first();
            
            //by mufffy for the HIERARCHY ID 837 BRD and 681 ANK Cash Voucher
            if((HierarchyID == 837 && EITLERPGLOBAL.gCompanyID == 2) || (HierarchyID == 681 && EITLERPGLOBAL.gCompanyID == 3)) {
                int f=0;
                int t=0;
                int c=0;
                
                while(!rsDocData.isAfterLast()) {
                    if(rsDocData.getInt("USER_ID")==From && f==0) {
                        FromSrNo=rsDocData.getInt("SR_NO");
                        f=1;
                    }
                    if(rsDocData.getInt("USER_ID")==To && t==0) {
                        ToSrNo=rsDocData.getInt("SR_NO");
                        t=1;
                    }
                    if(rsDocData.getString("TYPE").equals("C") && c==0) {
                        CreatorSrNo=rsDocData.getInt("SR_NO");
                        c=1;
                    }
                    rsDocData.next();
                }
            }//end muffy
            else{
                //Find out the From User ID record
                while(!rsDocData.isAfterLast()) {
                    if(rsDocData.getInt("USER_ID")==From) {
                        FromSrNo=rsDocData.getInt("SR_NO");
                    }
                    
                    if(rsDocData.getInt("USER_ID")==To) {
                        ToSrNo=rsDocData.getInt("SR_NO");
                    }
                    
                    if(rsDocData.getString("TYPE").equals("C")) {
                        CreatorSrNo=rsDocData.getInt("SR_NO");
                    }
                    
                    rsDocData.next();
                }
            }
            
            rsTmp=data.getResult("SELECT SR_NO FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND USER_ID="+EITLERPGLOBAL.gNewUserID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CurrentUserSrNo=rsTmp.getInt("SR_NO");
            }
            
            
            //Updating status of approval
            if(Status.equals("A")) //Approve and Forward
            {
                //Set the A Flag for From User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='A',REMARKS='"+Remarks+"',ACTION_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+Integer.toString(FromSrNo);
                data.Execute(strSQL);
                
                //Set the W Flag for To User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='W',FROM_USER_ID="+Integer.toString(From)+",FROM_REMARKS='"+Remarks+"',RECEIVED_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+ToSrNo;
                data.Execute(strSQL);
                
                //Set Completed for users between From and To
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='C',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO>"+Integer.toString(FromSrNo)+" AND SR_NO<"+Integer.toString(ToSrNo);
                data.Execute(strSQL);
                
                //Set Pending for users greater than To
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='P',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO>"+Integer.toString(ToSrNo);
                data.Execute(strSQL);
                
                //Now check whether document sent successfully to recipient. If not set it to current user only
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND STATUS='W'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Yes. Successfull
                }
                else {
                    //Set it to current user only
                    data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND SR_NO="+CurrentUserSrNo);
                }
                //============== Check Completed =======================//
                
                
            }
            
            
            if(Status.equals("F")) //Final Approval
            {
                //Set the A Flag for From User ID
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='F',REMARKS='"+Remarks+"',ACTION_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+EITLERPGLOBAL.gNewUserID;
                data.Execute(strSQL);
                
                //Set Approved Flag in Table Specified
                strSQL="UPDATE "+TableName+" SET APPROVED=1,APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                }
                else {
                    data.Execute(strSQL);
                }
                
                //========= Check again for the update of final approval ============//
                
                String reCheck="SELECT APPROVED FROM "+TableName+" WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    if(data.getIntValueFromDB(reCheck,SpecificURL)==0) {
                        objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
                        objLog.ModuleName="ApprovalFlow";
                        objLog.Method="UpdateFlow";
                        objLog.Trace=" Logged on "+EITLERPGLOBAL.getCurrentDate()+": Update to table did not work. Approval Flag not set by query. Query was "+strSQL;
                        objLog.Remarks="";
                        objLog.LogEvent();
                        
                        data.Execute(strSQL,SpecificURL);
                    }
                }
                else {
                    if(data.getIntValueFromDB(reCheck)==0) {
                        objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
                        objLog.ModuleName="ApprovalFlow";
                        objLog.Method="UpdateFlow";
                        objLog.Trace=" Logged on "+EITLERPGLOBAL.getCurrentDate()+": Update to table did not work. Approval Flag not set by query. Query was "+strSQL;
                        objLog.Remarks="";
                        objLog.LogEvent();
                        
                        data.Execute(strSQL);
                    }
                }
                //=====================================================================//
            }
            
            
            
            if(Status.equals("R")) //Rejected
            {
                
                boolean Continue=false;
                
                if(ExplicitSendTo) {
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        CreatorSrNo=rsTmp.getInt("SR_NO");
                    }
                }
                
                if(ExplicitSendTo) {
                    //Check that User is in the hierarchy
                    System.out.println("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()<=0) {
                        HashMap newUser=new HashMap();
                        clsHierarchy ObjUser=new clsHierarchy();
                        ObjUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        ObjUser.setAttribute("MODULE_ID",ModuleID);
                        ObjUser.setAttribute("DOC_NO",DocNo);
                        ObjUser.setAttribute("DOC_DATE",DocDate);
                        ObjUser.setAttribute("USER_ID",To);
                        newUser.put("1", ObjUser);
                        AppendUsers(newUser);
                    }
                    
                    rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND USER_ID="+To);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        CreatorSrNo=rsTmp.getInt("SR_NO");
                    }
                }
                
                
                
                
                //Set the W Flag for Creator
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='W',RECEIVED_DATE=CURDATE(),ACTION_DATE='0000-00-00',FROM_USER_ID="+Integer.toString(From)+",FROM_REMARKS='"+Remarks+" - Rejected Document"+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO="+Integer.toString(CreatorSrNo);
                data.Execute(strSQL);
                
                //System.out.println(strSQL);
                
                //Set the P Flag for all other users
                strSQL="UPDATE D_COM_DOC_DATA SET STATUS='V',CHANGED=1,CHANGED_DATE=CURDATE(),REMARKS='',RECEIVED_DATE='',ACTION_DATE='' WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND MODULE_ID="+Integer.toString(ModuleID)+" AND DOC_NO='"+DocNo+"' AND SR_NO<>"+Integer.toString(CreatorSrNo)+" AND SR_NO<="+FromSrNo;
                data.Execute(strSQL);
                
                //Set Approved Flag in Table Specified
                strSQL="UPDATE "+TableName+" SET APPROVED=0,REJECTED=1,REJECTED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE  "+FieldName+"='"+DocNo+"' AND COMPANY_ID="+Integer.toString(CompanyID);
                
                if(UseSpecifiedURL) {
                    data.Execute(strSQL,SpecificURL);
                }
                else {
                    data.Execute(strSQL);
                }
                
                
                //Now check whether document sent successfully to recipient. If not set it to current user only
                rsTmp=data.getResult("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND STATUS='W'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Yes. Successfull
                }
                else {
                    //Set it to current user only
                    data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+DocNo+"' AND SR_NO="+CurrentUserSrNo);
                }
                //============== Check Completed =======================//
                
            }
            return true;
        }
        catch(Exception e) {
            
            e.printStackTrace();
            objLog.CompanyID=EITLERPGLOBAL.gCompanyID;
            objLog.ModuleName="ApprovalFlow";
            objLog.Method="UpdateFlow";
            objLog.Trace=e.getMessage();
            objLog.Remarks="";
            objLog.LogEvent();
            
            LastError=e.getMessage();
            return false;
        }
    }
    
}
