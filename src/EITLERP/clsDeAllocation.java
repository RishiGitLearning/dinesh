/*
 * clsMaterialRequisition.java
 *
 * Created on April 19, 2004, 2:35 PM
 */

package EITLERP;

import java.util.*;
import java.lang.*; 
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
   

/**
 *
 * @author  jadave
 * @version 
 */
 
public class clsDeAllocation {

    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;

    private HashMap props;    
    public boolean Ready = false;
    //Requisition Material Collection

    //History Related properties
    private boolean HistoryView=false;
    
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

    
    /** Creates new clsMaterialRequisition */
    public clsDeAllocation() 
    {
        LastError = "";
        props=new HashMap();    
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(0));
        props.put("DOC_DATE",new Variant(""));
        props.put("ITEM_ID",new Variant(""));
        props.put("ALLOCATION_ID",new Variant(0));
        props.put("FREE_QTY",new Variant(0));
        props.put("UPDATED_BY",new Variant(0));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
       
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }

    public boolean LoadData(long pCompanyID)
    {
      Ready=false;
      try
        {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_STOCK_DEALLOCATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO"); 
            HistoryView=false;
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e)
        {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public void Close()
    {
      try
      {
        //Conn.close();
        Stmt.close();
        rsResultSet.close();
      }
      catch(Exception e)
      {
          
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
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try
        {           
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("DOC_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0))
            {
               //Within the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_STOCK_DEALLOCATION_H WHERE DOC_NO=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
           
                       
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //==========Make new document no. ===============//
            long DocNo=data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_STOCK_DEALLOCATION","DOC_NO")+1;
            setAttribute("DOC_NO",DocNo);
            //-------------------------------------------------
            
            rsResultSet.first();        
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
            rsResultSet.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsResultSet.updateLong("ALLOCATION_ID",(long)getAttribute("ALLOCATION_ID").getVal());
            rsResultSet.updateDouble("FREE_QTY",getAttribute("FREE_QTY").getVal());
            rsResultSet.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS","A"); //No Approval Implemented yet
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",""); //No Remarks Implemented yet
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
            rsHistory.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsHistory.updateLong("ALLOCATION_ID",(long)getAttribute("ALLOCATION_ID").getVal());
            rsHistory.updateDouble("FREE_QTY",getAttribute("FREE_QTY").getVal());
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.insertRow();           

            //Now do actual de-allocation
            //Now Update the Item master
            String strSQL="UPDATE D_INV_ITEM_MASTER SET ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"'";
            data.Execute(strSQL);
            
            //Now Free Indent Allocation.
            strSQL="UPDATE D_COM_STOCK_ALLOCATION SET DEALLOCATED_QTY=DEALLOCATED_QTY+"+getAttribute("FREE_QTY").getVal()+",ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+(long)getAttribute("ALLOCATION_ID").getVal();
            data.Execute(strSQL);
            
            //Now Free Indent Qty
            long AllocationID=(long)getAttribute("ALLOCATION_ID").getVal();
            
            String IndentNo="";
            int IndentSrNo=0;
            
            rsTmp=data.getResult("SELECT INDENT_NO,INDENT_SR_NO FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+AllocationID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IndentNo=rsTmp.getString("INDENT_NO");
                IndentSrNo=rsTmp.getInt("INDENT_SR_NO");
                
                //Update Indent
                strSQL="UPDATE D_INV_INDENT_DETAIL SET ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo;
                data.Execute(strSQL);
                
                //Update PR Records
                rsTmp=data.getResult("SELECT MR_NO,MR_SR_NO FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    String MRNo=rsTmp.getString("MR_NO");
                    int MRSrNo=rsTmp.getInt("MR_SR_NO");
                    
                    if((!MRNo.equals(""))&&MRSrNo>0) {
                        strSQL="UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo+"' AND SR_NO="+MRSrNo;
                        data.Execute(strSQL);
                    }
                    
                }
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
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try
        {              
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("DOC_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0))
            {
               //Withing the year 
            }
            else
            {
               LastError="Document date is not within financial year.";
               return false;
            }
            //=====================================================//
        
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_COM_STOCK_DEALLOCATION_H WHERE DOC_NO=1"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            long theDocNo=(long)getAttribute("DOC_NO").getVal();
                        

            rsResultSet.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsResultSet.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsResultSet.updateLong("ALLOCATION_ID",(long)getAttribute("ALLOCATION_ID").getVal());
            rsResultSet.updateDouble("FREE_QTY",getAttribute("FREE_QTY").getVal());
            rsResultSet.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateRow();
            
           
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_COM_STOCK_DEALLOCATION_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+(long)getAttribute("DOC_NO").getVal());
            RevNo++;
            long RevDocNo=(long)getAttribute("DOC_NO").getVal();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS","A");
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS","");

            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateLong("DOC_NO",(long)getAttribute("DOC_NO").getVal());
            rsHistory.updateString("DOC_DATE",(String)getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("ITEM_ID",(String)getAttribute("ITEM_ID").getObj());
            rsHistory.updateLong("ALLOCATION_ID",(long)getAttribute("ALLOCATION_ID").getVal());
            rsHistory.updateDouble("FREE_QTY",getAttribute("FREE_QTY").getVal());
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("UPDATED_BY").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.insertRow();           
            
            
            //====== Revert Back previous deallocation and again do it
            long DocNo=(long)getAttribute("DOC_NO").getVal();
            
            
            //Reverse previous effect
            RevertDeAllocation(EITLERPGLOBAL.gCompanyID, DocNo);
    
            
                //Now Update the Item master
                String strSQL="UPDATE D_INV_ITEM_MASTER SET ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"'";
                data.Execute(strSQL);
                
                //Now Free Indent Allocation.
                strSQL="UPDATE D_COM_STOCK_ALLOCATION SET DEALLOCATED_QTY=DEALLOCATED_QTY+"+getAttribute("FREE_QTY").getVal()+",ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+(long)getAttribute("ALLOCATION_ID").getVal();
                data.Execute(strSQL);
                
                //Now Free Indent Qty
                long AllocationID=(long)getAttribute("ALLOCATION_ID").getVal();
                
                String IndentNo="";
                int IndentSrNo=0;
                
                rsTmp=data.getResult("SELECT INDENT_NO,INDENT_SR_NO FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+AllocationID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    IndentNo=rsTmp.getString("INDENT_NO");
                    IndentSrNo=rsTmp.getInt("INDENT_SR_NO");
                    
                    //Update Indent
                    strSQL="UPDATE D_INV_INDENT_DETAIL SET ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo;
                    data.Execute(strSQL);
                    
                    //Update PR Records
                    rsTmp=data.getResult("SELECT MR_NO,MR_SR_NO FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        String MRNo=rsTmp.getString("MR_NO");
                        int MRSrNo=rsTmp.getInt("MR_SR_NO");
                        
                        if((!MRNo.equals(""))&&MRSrNo>0) {
                            strSQL="UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY=ALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo+"' AND SR_NO="+MRSrNo;
                            data.Execute(strSQL);
                        }
                        
                    }
                }
            
            
            return true;
        }                
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }           
    }

    
    
    //Deletes current record
    public boolean Delete()
    {
        try
        {   
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            long lDocNo=(long)getAttribute("DOC_NO").getVal();
            String strSQL="";
            

                String strQry = "DELETE FROM D_COM_STOCK_DEALLOCATION WHERE COMPANY_ID=" + lCompanyID +" AND DOC_NO="+lDocNo;
                data.Execute(strQry);
                
                RevertDeAllocation(EITLERPGLOBAL.gCompanyID, lDocNo);
                
                LoadData(lCompanyID);
                return true;
        }                
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }        
    }
    
    
    
    
    
    
    public boolean Filter(String pCondition,int pCompanyID) 
    {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_STOCK_DEALLOCATION " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_STOCK_DEALLOCATION WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveLast();
                return false;
            }
            else {
                Ready=true;
                MoveLast();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    

    
    public boolean setData() 
    {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        //HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
                
        try
        {
                if(HistoryView)
                {
                  RevNo=rsResultSet.getInt("REVISION_NO");  
                  setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));  
                }
                else
                {
                  setAttribute("REVISION_NO",0);  
                }
            
                setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
                setAttribute("DOC_NO",rsResultSet.getLong("DOC_NO"));
                setAttribute("DOC_DATE",rsResultSet.getString("DOC_DATE"));
                setAttribute("ITEM_ID",rsResultSet.getString("ITEM_ID"));
                setAttribute("ALLOCATION_ID",rsResultSet.getLong("ALLOCATION_ID"));
                setAttribute("FREE_QTY",rsResultSet.getDouble("FREE_QTY"));
                setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
                setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
                setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
                setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));                
                
            
             return true;
        }
        catch(Exception e)
        {
            LastError = e.getMessage();
            return false;
        }
    }
    
    

            
    public boolean ShowHistory(int pCompanyID,long pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_STOCK_DEALLOCATION_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO="+pDocNo);
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
    
    
    public static HashMap getHistoryList(int pCompanyID,long pDocNo) {
        HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_COM_STOCK_DEALLOCATION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+pDocNo);
            
            while(rsTmp.next()) {
                clsDeAllocation ObjDeAlloc=new clsDeAllocation();
                
                ObjDeAlloc.setAttribute("DOC_NO",rsTmp.getLong("DOC_NO"));
                ObjDeAlloc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjDeAlloc.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjDeAlloc.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjDeAlloc.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjDeAlloc.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjDeAlloc.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjDeAlloc);
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

        

    public boolean RevertDeAllocation(int pCompanyID,long pDocNo) {
        //Reverses the Effect of De Allocation -> Resulting to re-allocation.
        try {
            
            Connection Conn;
            Statement stAllocation,stTmp;
            ResultSet rsAllocation,rsTmp;
            
            //=====Load Previous Data ============//
            rsTmp=data.getResult("SELECT * FROM D_COM_STOCK_DEALLOCATION WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO="+pDocNo);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                setAttribute("DOC_NO",rsTmp.getInt("DOC_NO"));
                setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                setAttribute("ALLOCATION_ID",rsTmp.getLong("ALLOCATION_ID"));
                setAttribute("FREE_QTY",rsTmp.getDouble("FREE_QTY"));
                setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
            }
            
            
            //Now Update the Item master
            String strSQL="UPDATE D_INV_ITEM_MASTER SET ALLOCATED_QTY=ALLOCATED_QTY+"+getAttribute("FREE_QTY").getVal()+",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ITEM_ID='"+(String)getAttribute("ITEM_ID").getObj()+"'";
            data.Execute(strSQL);
            
            //Now Free Indent Allocation.
            strSQL="UPDATE D_COM_STOCK_ALLOCATION SET DEALLOCATED_QTY=DEALLOCATED_QTY-"+getAttribute("FREE_QTY").getVal()+",ALLOCATED_QTY=ALLOCATED_QTY+"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+(long)getAttribute("ALLOCATION_ID").getVal();
            data.Execute(strSQL);
            
            //Now Free Indent Qty
            long AllocationID=(long)getAttribute("ALLOCATION_ID").getVal();
            
            String IndentNo="";
            int IndentSrNo=0;
            
            rsTmp=data.getResult("SELECT INDENT_NO,INDENT_SR_NO FROM D_COM_STOCK_ALLOCATION WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO="+AllocationID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                IndentNo=rsTmp.getString("INDENT_NO");
                IndentSrNo=rsTmp.getInt("INDENT_SR_NO");
                
                //Update Indent
                strSQL="UPDATE D_INV_INDENT_DETAIL SET ALLOCATED_QTY=ALLOCATED_QTY+"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo;
                data.Execute(strSQL);
                
                //Update PR Records
                rsTmp=data.getResult("SELECT MR_NO,MR_SR_NO FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    String MRNo=rsTmp.getString("MR_NO");
                    int MRSrNo=rsTmp.getInt("MR_SR_NO");
                    
                    if((!MRNo.equals(""))&&MRSrNo>0) {
                        strSQL="UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY=ALLOCATED_QTY+"+getAttribute("FREE_QTY").getVal()+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo+"' AND SR_NO="+MRSrNo;
                        data.Execute(strSQL);
                    }
                    
                }
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
}
