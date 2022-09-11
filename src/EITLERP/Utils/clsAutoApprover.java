/*
 * clsAutoApprover.java
 *
 * Created on May 5, 2006, 11:45 AM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */
import EITLERP.*;
import EITLERP.Stores.*;
import EITLERP.Purchase.*;
import java.sql.*;
import java.net.*;
import java.util.*;
import javax.sql.*;
import java.io.*;


public class clsAutoApprover {
    
    private int SelCompanyID=2;
    private String FromYear="2005";
    private boolean UpdateFlags=false;
    
    /** Creates a new instance of clsAutoApprover */
    public clsAutoApprover() {
    }
    
    
    /*
     Expected Parameters from Command Line
     1. Company ID
     2. Financial Year From
     3. Financial Year To
     4. Forward Ahead Y/N
     */
    
    public static void main(String args[]) {
        
        try {
            if(args.length<4) {
                System.out.println("Insufficient Parameters");
                return;
            }
            
            data.OpenGlobalConnection();
            
            
            clsAutoApprover objSync=new clsAutoApprover();
            objSync.SelCompanyID=Integer.parseInt(args[0]);
            objSync.FromYear=args[1];
            
            if(args[3].equals("Y")) {
                objSync.UpdateFlags=true;
            }
            else {
                objSync.UpdateFlags=false;
            }
            
            objSync.AutoForwardMIR();
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // '' This routine automatically forward pending MIRs to next user for MIRs waiting ''
    // '' for user approval and waiting since 30 Days.
    // '' Waiting period = Document Receipt Date - Current Date > 30
    
    private void AutoForwardMIR() {
        
        HashMap Tables=new HashMap();
        
        new Thread(){
            public void run() {
                
                Connection srcConn=null;
                
                //===========ResultSet Section============//
                ResultSet rsModules=null;
                ResultSet rsPO=null;
                
                Statement stModules=null;
                Statement stPO=null;
                
                ResultSet rsTmp=null;
                Statement stTmp=null;
                
                int docModuleID=5; //MIR General
                int creatorDeptID=0;
                //=======================================//
                
                java.sql.Date CurrentDate=java.sql.Date.valueOf(EITLERPGLOBAL.getCurrentDateDB());
                java.sql.Date docDate=null;
                
                try {
                    
                    String FileName="autolog"+EITLERPGLOBAL.getCurrentDateDB()+".txt";
                    
                    BufferedWriter aFile=new BufferedWriter(new FileWriter(new File(FileName)));
                    
                    EITLERPGLOBAL.DBUserName="";
                    
                    //============== Get Source and Destination db URLs =============//
                    long nCompanyID = SelCompanyID;
                    int tmpFromYear=Integer.parseInt(FromYear);
                    
                    String currentURL=clsFinYear.getDBURL((int)nCompanyID,tmpFromYear);
                    System.out.println(currentURL);
                    //===============================================================//
                    
                    
                    //=======Open the Source and Destination Connections ==========//
                    System.out.println("Automatically Forwarding MIRs waiting for user approval since more than 30 Days. ");
                    
                    srcConn=data.getConn(currentURL);
                    srcConn.setAutoCommit(false);
                    srcConn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
                    
                    System.out.println("Got the connection of Source");
                    
                    stModules=srcConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    stModules.setQueryTimeout(1000);
                    //============================================================//
                    
                    System.out.println("Querying for Documents ... ");
                    
                    
                    //==== Get MIR Creator Department Code ========//
                    String strDept="SELECT USER_ID FROM `D_COM_HIERARCHY_RIGHTS` WHERE HIERARCHY_ID IN (SELECT HIERARCHY_ID FROM D_COM_HIERARCHY WHERE MODULE_ID=5) AND FINAL_APPROVER<>1 AND SR_NO=1 ";
                    
                    stTmp=srcConn.createStatement();
                    rsTmp=stTmp.executeQuery(strDept);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        int UserID=rsTmp.getInt("USER_ID");
                        
                        stTmp=srcConn.createStatement();
                        rsTmp=stTmp.executeQuery("SELECT DEPT_ID FROM D_COM_USER_MASTER WHERE USER_ID="+UserID);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            creatorDeptID=rsTmp.getInt("DEPT_ID");
                        }
                        
                    }
                    
                    //======== Now Fetch Modules =============//
                    String strSQL=" SELECT A.MIR_NO, A.MIR_DATE, A.CHALAN_NO, A.CHALAN_DATE, C.SUPP_NAME, B.PO_NO, D.DEPT_DESC, F.USER_NAME,E.RECEIVED_DATE,E.SR_NO AS FLOW_SR_NO,E.USER_ID AS FLOW_USER_ID FROM `D_INV_MIR_HEADER` A LEFT JOIN D_COM_SUPP_MASTER C ON (C.SUPPLIER_CODE=A.SUPP_ID AND C.COMPANY_ID=A.COMPANY_ID), D_INV_MIR_DETAIL B LEFT JOIN D_COM_DEPT_MASTER D ON (D.DEPT_ID=B.DEPT_ID AND D.COMPANY_ID=B.COMPANY_ID), D_COM_DOC_DATA E LEFT JOIN D_COM_USER_MASTER F ON (F.COMPANY_ID=E.COMPANY_ID AND F.USER_ID=E.USER_ID) LEFT JOIN D_COM_DEPT_MASTER UD ON (F.COMPANY_ID=UD.COMPANY_ID AND F.DEPT_ID=UD.DEPT_ID) WHERE A.MIR_NO=B.MIR_NO AND A.COMPANY_ID= B.COMPANY_ID AND A.MIR_NO=E.DOC_NO AND A.COMPANY_ID=E.COMPANY_ID AND A.APPROVED=0 AND A.CANCELLED=0  AND E.STATUS='W' AND E.MODULE_ID="+docModuleID+" AND E.SR_NO>1 AND UD.DEPT_ID<>"+creatorDeptID+" ORDER BY D.DEPT_DESC, F.USER_NAME, A.MIR_NO";
                    
                    stModules=srcConn.createStatement();
                    rsModules=stModules.executeQuery(strSQL);
                    rsModules.first();
                    
                    if(rsModules.getRow()>0) {
                        
                        
                        System.out.println("New Code ... ");
                        aFile.write("Pending MIR at user level. As on "+EITLERPGLOBAL.getCurrentDate()+" "+EITLERPGLOBAL.getCurrentTime());
                        aFile.newLine();
                        aFile.newLine();
                        aFile.write("MIR No.     "+"  "+"User Name           "+"  "+"Received Date"+"  "+"Pending");
                        aFile.newLine();
                        aFile.write("            "+"  "+"                    "+"  "+"             "+"  "+"Days   ");
                        aFile.newLine();
                                                
                        rsModules.first();
                        
                        while(!rsModules.isAfterLast()) {
                            
                            String MIRNo=rsModules.getString("MIR_NO");
                            int FlowSrNo=rsModules.getInt("FLOW_SR_NO");
                            String WaitingUser=rsModules.getString("USER_NAME");
                            
                            docDate=java.sql.Date.valueOf(rsModules.getString("RECEIVED_DATE"));
                            int PendingDays=0;
                            
                            
                            try {
                                
                                //Now Forward
                                String reportLine="";
                                
                                PendingDays=Math.abs(EITLERPGLOBAL.DateDiff(CurrentDate,docDate));
                                System.out.println("MIR No. : "+MIRNo+" Received Date "+docDate.toString()+" Current Date "+CurrentDate.toString()+" Days Diff : "+PendingDays);
                                

                                if(PendingDays>30) {
                                    
                                    System.out.println("Pending for more than 30 Days ... ");
                                    
                                    String SuppID="";
                                    
                                    //---Assume that PO in all line items of MIR present ----//
                                    boolean ItemPOPresent=true;
                                    
                                    stTmp=srcConn.createStatement();
                                    rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_MIR_HEADER WHERE MIR_NO='"+MIRNo+"'");
                                    rsTmp.first();
                                    
                                    if(rsTmp.getRow()>0) {
                                        SuppID=rsTmp.getString("SUPP_ID");
                                    }
                                    
                                    //======Now Check for PO ===========//
                                    stTmp=srcConn.createStatement();
                                    rsTmp=stTmp.executeQuery("SELECT IF(PO_NO IS NULL,'',PO_NO) PO_NO,ITEM_ID FROM D_INV_MIR_DETAIL WHERE MIR_NO='"+MIRNo+"'");
                                    rsTmp.first();
                                    
                                    if(rsTmp.getRow()>0) {
                                        while(!rsTmp.isAfterLast()) {
                                            String PONo=rsTmp.getString("PO_NO");
                                            String ItemID=rsTmp.getString("ITEM_ID");
                                            
                                            if(PONo.trim().equals("")) {
                                                System.out.println("PO No. is blank in MIR. Finding in PO file");
                                                
                                                //PO no. is blank. But see that any po has just final approved for the supplier
                                                stPO=srcConn.createStatement();
                                                rsPO=stPO.executeQuery("SELECT A.PO_NO,A.PO_DATE,A.SUPP_ID,B.ITEM_ID,B.SR_NO,B.PO_TYPE FROM D_PUR_PO_HEADER A,D_PUR_PO_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.PO_NO=B.PO_NO AND A.PO_TYPE=B.PO_TYPE AND A.SUPP_ID='"+SuppID+"' AND B.ITEM_ID='"+ItemID+"' AND B.RECD_QTY<QTY AND A.APPROVED=1");
                                                rsPO.first();
                                                
                                                if(rsPO.getRow()>0) {
                                                    
                                                    //PO Has been made
                                                    System.out.println("PO No. "+rsPO.getString("PO_NO")+" is final approved.");
                                                }
                                                else {
                                                    //PO not made
                                                    ItemPOPresent=false;
                                                    System.out.println("No PO has been approved.");
                                                }
                                            }
                                            else
                                            {
                                               System.out.println("PO No. in MIR "+PONo); 
                                            }
                                            rsTmp.next();
                                        }
                                    }
                                    //=================================//
                                                                        
                                    
                                    if(ItemPOPresent) {
                                        System.out.println("This MIR can be forwarded.");
                                        
                                        //Write doen in Report
                                        reportLine=MIRNo.trim()+EITLERPGLOBAL.Replicate(" ", 12-MIRNo.trim().length())+"  "+WaitingUser.trim()+EITLERPGLOBAL.Replicate(" ", 20-WaitingUser.trim().length())+"  "+EITLERPGLOBAL.formatDate(docDate.toString())+"     "+PendingDays;
                                        aFile.write(reportLine);
                                        aFile.newLine();
                                        
                                        if(UpdateFlags) {
                                            System.out.println("Updating MIR");
                                            
                                            int NextUserSrNo=FlowSrNo+1;
                                            int CurrentUserID=rsModules.getInt("FLOW_USER_ID");
                                            
                                            strSQL="UPDATE D_COM_DOC_DATA SET STATUS='A',REMARKS='#AUTO_FORWARDED#',ACTION_DATE=CURDATE(),CHANGED=1,CHANGED_DATE=CURDATE() WHERE MODULE_ID="+docModuleID+" AND DOC_NO='"+MIRNo+"' AND SR_NO="+FlowSrNo;
                                            stTmp=srcConn.createStatement();
                                            stTmp.executeUpdate(strSQL);
                                            
                                            strSQL="UPDATE D_COM_DOC_DATA SET STATUS='W',FROM_REMARKS='#AUTO_FORWARDED#',RECEIVED_DATE=CURDATE(),FROM_USER_ID="+CurrentUserID+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE MODULE_ID="+docModuleID+" AND DOC_NO='"+MIRNo+"' AND SR_NO="+NextUserSrNo;
                                            stTmp=srcConn.createStatement();
                                            stTmp.executeUpdate(strSQL);
                                            
                                            strSQL="UPDATE D_INV_MIR_DETAIL SET QTY=RECEIVED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE MIR_NO='"+MIRNo+"'";
                                            stTmp=srcConn.createStatement();
                                            stTmp.executeUpdate(strSQL);
                                            
                                            strSQL="UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE MIR_NO='"+MIRNo+"'";
                                            stTmp=srcConn.createStatement();
                                            stTmp.executeUpdate(strSQL);
                                            
                                            
                                            aFile.write("Forwarded to next user");
                                            aFile.newLine();
                                            
                                        }
                                    }
                                }
                                
                                //Commit at module level
                                srcConn.commit();
                            }
                            catch(Exception mod) {
                                srcConn.rollback();
                                mod.printStackTrace();
                            }
                            rsModules.next();
                        }
                    }
                    //========================================//
                    
                    
                    aFile.close();
                    
                    System.out.println("Now commiting changes ... ");
                    srcConn.commit();
                    System.out.println("Changes commited successfully.");
                    System.out.println("Process completed successfully");
                    
                    
                }
                catch(Exception e) {
                    System.out.println("Process failed. ");
                    try {
                        srcConn.rollback();
                        System.out.println("Rollback succeeded");
                    }
                    catch(Exception d) {
                        System.out.println("Rollback failed");
                    }
                    
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    
}
