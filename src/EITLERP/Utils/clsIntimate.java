/*
 * clsIntimate.java
 *
 * Created on September 28, 2005, 12:59 PM
 */

package EITLERP.Utils;

/**
 *
 * @author  root
 */

import java.util.*;
import EITLERP.*;
import java.io.*;
import EITLERP.JavaMail.*;


public class clsIntimate {
    
    private String dbURL="";
    private int CompanyID=0;
    
    /** Creates a new instance of clsIntimate */
    public clsIntimate() {
        
    }
    
    public static void main(String[] args) {
        if(args.length<1)
        {
          System.out.println("Specify  Company ID and Databsae URL. ");
          return;
        }
        
        clsIntimate objIntimate=new clsIntimate();
        objIntimate.CompanyID=Integer.parseInt(args[0]);
        objIntimate.dbURL=args[1];
        objIntimate.StartProcess();
    }
    
    public void StartProcess() {
        new Thread() {
            
            public void run() {
                HashMap List=new HashMap();
                HashMap userList=new HashMap();
                
                int TotalPendingCount=0;
                String EMailID="";
                String EMailText="";
                
                EITLERPGLOBAL.gCompanyID=CompanyID;
                EITLERPGLOBAL.DatabaseURL=dbURL;
                
                data.OpenGlobalConnection(dbURL);
                
                System.out.println(dbURL);
                String CityName=clsCompany.getCityName(CompanyID);
                try {
                    
                    frmPendingApprovals ObjPending=new frmPendingApprovals();
                    ObjPending.init();
                    ObjPending.start();
                    
                    System.setSecurityManager(null);
                               
                    
                    clsUser tmpObj=new clsUser();
                    userList=tmpObj.getList("");
                    
                    BufferedWriter aFile2=new BufferedWriter(new FileWriter(new File("/root/MailLog.txt")));
                    
                    for(int u=1;u<=userList.size();u++) {
                        
                        clsUser ObjUser=(clsUser)userList.get(Integer.toString(u));
                        int UserID=(int)ObjUser.getAttribute("USER_ID").getVal();
                        String UserName=(String)ObjUser.getAttribute("USER_NAME").getObj();
                        
                        
                        System.out.println("Processing User :"+UserName);
                        
                        EMailID=clsUser.getEMailID(EITLERPGLOBAL.gCompanyID, UserID);
                        
                        //Get the pending count
                        TotalPendingCount=0;
                        
                        String strCondition=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY MODULE_ID";
                        List=clsModules.getList(strCondition);
                        for(int i=1;i<=List.size();i++) {
                            clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
                            //Check that Module Access Rights are given
                            int ModuleID=(int)ObjModules.getAttribute("MODULE_ID").getVal();
                            
                            EITLERPGLOBAL.gUserID=UserID; //Bcz. all module refer to this global variable.
                            
                            TotalPendingCount+=ObjPending.GetCount(ModuleID);
                        }
                        
                        
                        
                        if(TotalPendingCount>0&&(!EMailID.trim().equals(""))) {
                            //Create a Text file
                            
                            EMailText="";
                            
                            BufferedWriter aFile=new BufferedWriter(new FileWriter(new File("/root/"+UserName+".txt")));
                            
                            aFile.write("Shri Dinesh Mills Ltd. "+CityName);
                            aFile.newLine();
                            aFile.newLine();
                            aFile.newLine();
                            
                            aFile.write("Dear sir/madam");
                            
                            EMailText+="Shri Dinesh Mills Ltd. "+CityName;
                            EMailText+=new String("\n");
                            EMailText+=new String("\n");
                                                        
                            EMailText+="For "+UserName;
                            EMailText+=new String("\n");
                            EMailText+="Dear sir/madam";
                            
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.write("You have following documents waiting for your approval");
                            EMailText+="You have following documents waiting for your approval";
                            
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.newLine();
                            EMailText+=new String("\n");
                            
                            strCondition=" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY MODULE_ID";
                            List=clsModules.getList(strCondition);
                            for(int i=1;i<=List.size();i++) {
                                clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
                                //Check that Module Access Rights are given
                                int ModuleID=(int)ObjModules.getAttribute("MODULE_ID").getVal();
                                
                                EITLERPGLOBAL.gUserID=UserID; //Bcz. all module refer to this global variable.
                                
                                int Count= ObjPending.GetCount(ModuleID);
                                String ModuleName=clsModules.getModuleName(EITLERPGLOBAL.gCompanyID,ModuleID);
                                
                                if(Count>0) {
                                    aFile.write(ModuleName+" - "+Count);
                                    EMailText+=ModuleName+" - "+Count;
                                    
                                    aFile.newLine();
                                    EMailText+=new String("\n");
                                }
                            }
                            
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.write("Kindly approve these documents.");
                            EMailText+="Kindly approve these documents.";
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.write("EITLERP System ");
                            EMailText+="EITLERP System ";
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.write("NOTE : DO NOT REPLY TO THIS MAIL. IT IS AUTOMATICALLY GENERATED EMAIL FROM EITLERP SYSTEM");
                            EMailText+="NOTE : DO NOT REPLY TO THIS MAIL. IT IS AUTOMATICALLY GENERATED EMAIL FROM EITLERP SYSTEM";
                            aFile.newLine();
                            EMailText+=new String("\n");
                            aFile.close();
                            
                            if(!EMailID.trim().equals("")) {
                                try {
                                    JMail.SendMail(EITLERPGLOBAL.SMTPHostIP,"eitlerp@dineshmills.com", EMailID,EMailText, "EITLERP - Documents waiting for your approval","eitl");
                                    aFile2.write("Mail sent to "+EMailID);
                                    aFile2.newLine();
                                }
                                catch(Exception e) {
                                    aFile2.write("Mail not sent to "+EMailID+" "+e.getMessage());
                                    aFile2.newLine();
                                    
                                }
                            }
                            
                            
                            
                        }
                    }
                    

                    aFile2.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
        
    }
    
}
