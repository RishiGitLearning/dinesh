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
 
 
/**
 *
 * @author  nrpithva
 * @version 
 */

public class clsItemHierarchy {

    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    public HashMap colUsers = new HashMap();
    
    private HashMap props;    
    public boolean Ready = false;

    public Variant getAttribute(String PropName)
    {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,int Value)
    {
         props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,double Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,float Value)
    {
         props.put(PropName,new Variant(Value));
    }

    public void setAttribute(String PropName,boolean Value)
    {
         props.put(PropName,new Variant(Value));
    }

    
    /** Creates new clsBusinessObject */
    public clsItemHierarchy() {
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("APPROVAL_NO",new Variant(0));
        props.put("DESCRIPTION",new Variant(""));
        props.put("IS_DEFAULT",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
    }

    public boolean LoadData(long pCompanyID)
    {
      Ready=false;
      try
        {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID));
            Ready=true;
            MoveFirst();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }

    
    //Navigation Methods
    public boolean MoveFirst()
    {
        try
        {
          rsResultSet.first();  
          setData();
          return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext()
    {
        try
        {
           if(rsResultSet.isAfterLast()||rsResultSet.isLast())
           {
               //Move pointer at last record
               //If it is beyond eof
               rsResultSet.last();
           }
           else
           {
            rsResultSet.next();
                if(rsResultSet.isAfterLast())
                {
                    rsResultSet.last();
                }
           }
           setData();
           return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious()
    {
        try
        {
            if(rsResultSet.isFirst()||rsResultSet.isBeforeFirst())
            {
               rsResultSet.first(); 
            }
            else
            {
                rsResultSet.previous();
                if(rsResultSet.isBeforeFirst())
                {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast()
    {
        try
        {
            rsResultSet.last();
            setData();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }

    
    
    
   public boolean Insert() 
    {
        try
        {                       
            ResultSet rsDetail;
            Statement StmtDetail;
            
            StmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsResultSet.first();
            
            //get Current set Company ID
            long lCompanyID= (long) getAttribute("COMPANY_ID").getVal();

            //Update Default stat first
            data.Execute("UPDATE D_INV_ITEM_HIERARCHY_MASTER SET IS_DEFAULT=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(lCompanyID));
            
            //Generating new Approval No. by using Max(APPROVAL_NO)+1
            //And Assigning it to class property
            setAttribute("APPROVAL_NO", data.getMaxID(lCompanyID,"D_INV_ITEM_HIERARCHY_MASTER","APPROVAL_NO"));
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("APPROVAL_NO",(long) getAttribute("APPROVAL_NO").getVal());
            rsResultSet.updateString("DESCRIPTION",(String) getAttribute("DESCRIPTION").getObj());
            rsResultSet.updateBoolean("IS_DEFAULT",(boolean)getAttribute("IS_DEFAULT").getBool());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.insertRow();           


            rsDetail=StmtDetail.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_DETAIL");

            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colUsers.size();
            
            for(cnt=1;cnt<=lnSize;cnt++)
            {
                clsItemHierarchyUser ObjUser=(clsItemHierarchyUser) colUsers.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",lCompanyID);
                rsDetail.updateLong("APPROVAL_NO",(long) getAttribute("APPROVAL_NO").getVal());
                rsDetail.updateLong("SR_NO",(long) ObjUser.getAttribute("SR_NO").getVal());
                rsDetail.updateLong("USER_ID",(long) ObjUser.getAttribute("USER_ID").getVal());
                rsDetail.updateBoolean("CAN_SKIP",(boolean) ObjUser.getAttribute("CAN_SKIP").getBool());
                rsDetail.updateBoolean("CAN_CHANGE",(boolean) ObjUser.getAttribute("CAN_CHANGE").getBool());
                rsDetail.updateBoolean("CAN_FINAL_APPROVE",(boolean) ObjUser.getAttribute("CAN_FINAL_APPROVE").getBool());
                rsDetail.insertRow();
            }
            
            
            MoveLast();
            return true;
        }                
        catch(Exception e)
        {
            LastError= e.getMessage();
            return false;
        }   
    }

    
    //Updates current record
    public boolean Update() 
    { 
        try
        {  
            ResultSet rsDetail;
            Statement StmtDetail;
            
            StmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            long lnCompanyID= (long) getAttribute("COMPANY_ID").getVal();            
            long lApprovalNo=(long) getAttribute("APPROVAL_NO").getVal();

            //Update Default stat first
            data.Execute("UPDATE D_INV_ITEM_HIERARCHY_MASTER SET IS_DEFAULT=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(lnCompanyID));
            
            //No Primary Keys will be updated
            rsResultSet.updateBoolean("IS_DEFAULT",(boolean)getAttribute("IS_DEFAULT").getBool());
            rsResultSet.updateString("DESCRIPTION",(String) getAttribute("DESCRIPTION").getObj());
            rsResultSet.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateRow();

            //First remove the old rows
            String mstrCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mstrApprovalNo=Long.toString((long)getAttribute("APPROVAL_NO").getVal());

            data.Execute("DELETE FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID="+mstrCompanyID+" AND APPROVAL_NO="+mstrApprovalNo);
            
            rsDetail=StmtDetail.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_DETAIL");

            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colUsers.size();
            
            for(cnt=1;cnt<=lnSize;cnt++)
            {
                clsItemHierarchyUser ObjUser=(clsItemHierarchyUser) colUsers.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",lnCompanyID);
                rsDetail.updateLong("APPROVAL_NO",lApprovalNo);
                rsDetail.updateLong("SR_NO",(long) ObjUser.getAttribute("SR_NO").getVal());
                rsDetail.updateLong("USER_ID",(long) ObjUser.getAttribute("USER_ID").getVal());
                rsDetail.updateBoolean("CAN_SKIP",(boolean) ObjUser.getAttribute("CAN_SKIP").getBool());
                rsDetail.updateBoolean("CAN_CHANGE",(boolean) ObjUser.getAttribute("CAN_CHANGE").getBool());
                rsDetail.updateBoolean("CAN_FINAL_APPROVE",(boolean) ObjUser.getAttribute("CAN_FINAL_APPROVE").getBool());
                rsDetail.insertRow();
           }
            
           
            
            
            return true;
        }                
        catch(Exception e)
        {
            //JOptionPane.showMessageDialog(null,e.getMessage());
            LastError = e.getMessage();
            return false;
        }           
    }
    

    //Deletes current record
    public boolean Delete()
    {
        try
        {  
            String lCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String lApprovalNo=Long.toString((long) getAttribute("APPROVAL_NO").getVal());
            
            String strQry = "DELETE FROM D_INV_ITEM_HIERARCHY_MASTER WHERE COMPANY_ID=" + lCompanyID +" AND APPROVAL_NO="+lApprovalNo;
            data.Execute(strQry);
            strQry = "DELETE FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND APPROVAL_NO="+lApprovalNo;
            data.Execute(strQry);
            
            LoadData(Long.parseLong(lCompanyID));
            return true;
            
        }                
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }        
    }
    
    public Object getObject(int pCompanyID) 
    {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID);
        clsCompany ObjCompany = new clsCompany();
        ObjCompany.Filter(strCondition);
        return ObjCompany;      
    }
    
    
   public static boolean CanSkip(int pCompanyID,int pApprovalNo,int pUserID)
   {
       Connection tmpConn;
       ResultSet rsTmp;
       Statement tmpStmt;
       boolean blnCanSkip=false;
       
       try
       {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT CAN_SKIP FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND APPROVAL_NO="+Integer.toString(pApprovalNo)+" AND USER_ID="+Integer.toString(pUserID));
            rsTmp.first();
        
            if(rsTmp.getRow()>0)
            {
              blnCanSkip=rsTmp.getBoolean("CAN_SKIP");
            }
            return blnCanSkip;
       }
       catch(Exception e)
       {
            return blnCanSkip;
       }
   }
   
   public static boolean CanFinalApprove(int pCompanyID,int pApprovalNo,int pUserID)
   {
       Connection tmpConn;
       ResultSet rsTmp;
       Statement tmpStmt;
       boolean blnCanFinal=false;
       
       try
       {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT CAN_FINAL_APPROVE FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND APPROVAL_NO="+Integer.toString(pApprovalNo)+" AND USER_ID="+Integer.toString(pUserID));
            rsTmp.first();
        
            if(rsTmp.getRow()>0)
            {
              blnCanFinal=rsTmp.getBoolean("CAN_FINAL_APPROVE");
            }
            return blnCanFinal;
       }
       catch(Exception e)
       {
            return blnCanFinal;
       }
   }

   
   public static HashMap getUserList(int pCompanyID,int pHierarchyID)
   {
    Connection tmpConn;
    Statement tmpStmt;
    ResultSet rsTmp;
    HashMap List=new HashMap();
    int Counter=0;
    
    try
    {
        tmpConn=data.getConn();
        tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND APPROVAL_NO="+Integer.toString(pHierarchyID)+" ORDER BY SR_NO");
        rsTmp.first();
        
        while(!rsTmp.isAfterLast())
        {
            Counter=Counter+1;
            
            clsUser ObjUser=new clsUser();
            
            ObjUser.setAttribute("USER_ID",rsTmp.getInt("USER_ID"));
            String lUserName=clsUser.getUserName(pCompanyID, rsTmp.getInt("USER_ID"));
            ObjUser.setAttribute("USER_NAME",lUserName);
  
            List.put(Integer.toString(Counter),ObjUser);
            rsTmp.next();
        }
        return List;
    }
    catch(Exception e)   
    {
        return List;
    }
   }
    
    
   public static HashMap getList(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp2;
        Statement tmpStmt2;

        HashMap List=new HashMap();
        long Counter=0;
        long InnerCounter=0;
        
        try
        {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement();

            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_MASTER "+pCondition);

            Counter=0;
            while(rsTmp.next())
            {
                Counter=Counter+1;
                clsItemHierarchy ObjItemH =new clsItemHierarchy();
                
                ObjItemH.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItemH.setAttribute("APPROVAL_NO",rsTmp.getLong("APPROVAL_NO"));
                ObjItemH.setAttribute("DESCRIPTION",rsTmp.getString("DESCRIPTION"));
                ObjItemH.setAttribute("IS_DEFAULT",rsTmp.getBoolean("IS_DEFAULT"));
                ObjItemH.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjItemH.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItemH.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjItemH.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
            //Now Populate the collection
            //first clear the collection
            ObjItemH.colUsers.clear();
            
            String mCompanyID=Long.toString( (long) ObjItemH.getAttribute("COMPANY_ID").getVal());
            String mApprovalNo=Long.toString((long) ObjItemH.getAttribute("APPROVAL_NO").getVal());
            
            tmpStmt2=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND APPROVAL_NO="+mApprovalNo);
            
            rsTmp2.first();
    
            InnerCounter=0;
            while(!rsTmp2.isAfterLast())
            {
                InnerCounter++;
                clsItemHierarchyUser ObjUser=new clsItemHierarchyUser();
                
                ObjUser.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                ObjUser.setAttribute("APPROVAL_NO",rsTmp2.getLong("APPROVAL_NO"));
                ObjUser.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjUser.setAttribute("USER_ID",rsTmp2.getLong("USER_ID"));
                ObjUser.setAttribute("CAN_SKIP",rsTmp2.getBoolean("CAN_SKIP"));
                ObjUser.setAttribute("CAN_CHANGE",rsTmp2.getBoolean("CAN_CHANGE"));
                ObjUser.setAttribute("CAN_FINAL_APPROVE",rsTmp2.getBoolean("CAN_FINAL_APPROVE"));
                ObjUser.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjUser.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjUser.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjUser.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                                
                ObjItemH.colUsers.put(Long.toString(InnerCounter),ObjUser);
                rsTmp2.next();
            }// Inner while
                
             List.put(Long.toString(Counter),ObjItemH);
            }
            return List;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Error in hierarchy "+e.getMessage());
            return List;     
        }
    }
    

   public static HashMap getListEx(String pCondition) {
        Connection tmpConn;
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp2;
        Statement tmpStmt2;

        HashMap List=new HashMap();
        long Counter=0;
        long InnerCounter=0;
        String strSQL="";
        
        try
        {
            tmpConn=data.getConn();
            
            //First find the hierarchy starting with logged in user
            strSQL="SELECT COUNT(*) AS THECOUNT FROM D_INV_ITEM_HIERARCHY_MASTER,D_INV_ITEM_HIERARCHY_DETAIL "+pCondition+" AND D_INV_ITEM_HIERARCHY_MASTER.COMPANY_ID=D_INV_ITEM_HIERARCHY_DETAIL.COMPANY_ID AND D_INV_ITEM_HIERARCHY_MASTER.APPROVAL_NO=D_INV_ITEM_HIERARCHY_DETAIL.APPROVAL_NO AND D_INV_ITEM_HIERARCHY_DETAIL.USER_ID="+EITLERPGLOBAL.gUserID+" AND SR_NO=1";
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("THECOUNT")>=1) //Hierarchies found starting with logged in user
            {
                //Retain the Query as it is
                strSQL="SELECT D_INV_ITEM_HIERARCHY_MASTER.*  FROM D_INV_ITEM_HIERARCHY_MASTER,D_INV_ITEM_HIERARCHY_DETAIL "+pCondition+" AND D_INV_ITEM_HIERARCHY_MASTER.COMPANY_ID=D_INV_ITEM_HIERARCHY_DETAIL.COMPANY_ID AND D_INV_ITEM_HIERARCHY_MASTER.APPROVAL_NO=D_INV_ITEM_HIERARCHY_DETAIL.APPROVAL_NO AND D_INV_ITEM_HIERARCHY_DETAIL.USER_ID="+EITLERPGLOBAL.gUserID+" AND SR_NO=1 ";
            }
            
            if(rsTmp.getInt("THECOUNT")<=0) //No hierarchy found starting with logged in user
            {
               return List; 
            }
            
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);

            Counter=0;
            while(rsTmp.next())
            {
                Counter=Counter+1;
                clsItemHierarchy ObjItemH =new clsItemHierarchy();
                
                ObjItemH.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItemH.setAttribute("APPROVAL_NO",rsTmp.getLong("APPROVAL_NO"));
                ObjItemH.setAttribute("DESCRIPTION",rsTmp.getString("DESCRIPTION"));
                ObjItemH.setAttribute("IS_DEFAULT",rsTmp.getBoolean("IS_DEFAULT"));
                ObjItemH.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjItemH.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItemH.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjItemH.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
            //Now Populate the collection
            //first clear the collection
            ObjItemH.colUsers.clear();
            
            String mCompanyID=Long.toString( (long) ObjItemH.getAttribute("COMPANY_ID").getVal());
            String mApprovalNo=Long.toString((long) ObjItemH.getAttribute("APPROVAL_NO").getVal());
            
            tmpStmt2=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND APPROVAL_NO="+mApprovalNo);
            
            rsTmp2.first();
    
            InnerCounter=0;
            while(!rsTmp2.isAfterLast())
            {
                InnerCounter++;
                clsItemHierarchyUser ObjUser=new clsItemHierarchyUser();
                
                ObjUser.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                ObjUser.setAttribute("APPROVAL_NO",rsTmp2.getLong("APPROVAL_NO"));
                ObjUser.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjUser.setAttribute("USER_ID",rsTmp2.getLong("USER_ID"));
                ObjUser.setAttribute("CAN_SKIP",rsTmp2.getBoolean("CAN_SKIP"));
                ObjUser.setAttribute("CAN_CHANGE",rsTmp2.getBoolean("CAN_CHANGE"));
                ObjUser.setAttribute("CAN_FINAL_APPROVE",rsTmp2.getBoolean("CAN_FINAL_APPROVE"));
                ObjUser.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjUser.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjUser.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjUser.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                                
                ObjItemH.colUsers.put(Long.toString(InnerCounter),ObjUser);
                rsTmp2.next();
            }// Inner while
                
             List.put(Long.toString(Counter),ObjItemH);
            }
            return List;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Error in hierarchy "+e.getMessage());
            return List;     
        }
    }
   
   
public boolean Filter(String pCondition) 
    {
        Ready=false;
        try
        {   
            String strSql = "SELECT * FROM D_INV_ITEM_HIERARCHY_MASTER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement();           
            
            if (Stmt.execute(strSql))
            {                
                rsResultSet = Stmt.getResultSet();                
                Ready=MoveFirst();
            }
            else
            {  
                Ready=false;
            }
           
            return Ready;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }       
    }
    

    public static int getDefaultHierarchy(int pCompanyID)
    {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        
        try
        {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT APPROVAL_NO FROM D_INV_ITEM_HIERARCHY_MASTER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND IS_DEFAULT=1");
            rsTmp.first();
            if(rsTmp.getRow()>0)
            {
                return rsTmp.getInt("APPROVAL_NO");
            }
            else
            {
                return 0;
            }
            
        }
        catch(Exception e)
        {
          return 0; 
        }
    }
    


    public boolean setData() 
    {
        ResultSet rsTmp;
        Statement tmpStmt;

        long Counter=0;
        
        try
        {
            setAttribute("COMPANY_ID",rsResultSet.getLong("COMPANY_ID"));            
            setAttribute("APPROVAL_NO",rsResultSet.getLong("APPROVAL_NO"));
            setAttribute("DESCRIPTION",rsResultSet.getString("DESCRIPTION"));
            setAttribute("IS_DEFAULT",rsResultSet.getBoolean("IS_DEFAULT"));
            setAttribute("CREATED_BY",rsResultSet.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));    
            
            //Now Populate the collection
            //first clear the collection
            colUsers.clear();
            
            String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mApprovalNo=Long.toString((long) getAttribute("APPROVAL_NO").getVal());
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_ITEM_HIERARCHY_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND APPROVAL_NO="+mApprovalNo);
            
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
                Counter=Counter+1;
                clsItemHierarchyUser ObjUser=new clsItemHierarchyUser();
                
                ObjUser.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjUser.setAttribute("APPROVAL_NO",rsTmp.getLong("APPROVAL_NO"));
                ObjUser.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjUser.setAttribute("USER_ID",rsTmp.getLong("USER_ID"));
                ObjUser.setAttribute("CAN_SKIP",rsTmp.getBoolean("CAN_SKIP"));
                ObjUser.setAttribute("CAN_CHANGE",rsTmp.getBoolean("CAN_CHANGE"));
                ObjUser.setAttribute("CAN_FINAL_APPROVE",rsTmp.getBoolean("CAN_FINAL_APPROVE"));
                ObjUser.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjUser.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjUser.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjUser.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                                
                colUsers.put(Long.toString(Counter),ObjUser);
                
                rsTmp.next();
            }

            return true;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
}
