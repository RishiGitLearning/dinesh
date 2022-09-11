/*
 * clsBusinessObject.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Stores;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
 
 
/** 
 *
 * @author  nrpithva
 * @version
 */ 

public class clsInward {
    
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    public HashMap colItems=new HashMap();
    
    //History Related properties
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
    public clsInward() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("INWARD_NO",new Variant(0));
        props.put("INWARD_DATE",new Variant(""));
        props.put("CHALAN_NO",new Variant(""));
        props.put("SUPP_ID",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        HistoryView=false;
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_INWARD_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INWARD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INWARD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY INWARD_NO");
            Ready=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
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
        Statement stmtDetail,stTmp;
        ResultSet rsDetail,rsTmp;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("INWARD_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Inward date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        
            stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM D_COM_INWARD_HEADER WHERE INWARD_NO=1");
            rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("INWARD_NO",(int)data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_COM_INWARD_HEADER","INWARD_NO"));
            //-------------------------------------------------
            
            rsHeader.first();
            rsHeader.moveToInsertRow();
            rsHeader.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHeader.updateInt("INWARD_NO",(int)getAttribute("INWARD_NO").getVal());
            rsHeader.updateString("INWARD_DATE",(String)getAttribute("INWARD_DATE").getObj());
            rsHeader.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHeader.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHeader.updateBoolean("CHANGED",true);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.insertRow();
            
            
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_COM_INWARD_DETAIL WHERE INWARD_NO=1");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsInwardItem ObjItem=(clsInwardItem) colItems.get(Integer.toString(cnt));
                
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateInt("INWARD_NO",(int)getAttribute("INWARD_NO").getVal());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsDetail.updateDouble("GROSS_QTY",ObjItem.getAttribute("GROSS_QTY").getVal());
                rsDetail.updateBoolean("MIR_UPDATED",ObjItem.getAttribute("MIR_UPDATED").getBool());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.insertRow();
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
        Statement stmtDetail;
        ResultSet rsDetail;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("INWARD_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Inward date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            int theDocNo=(int)getAttribute("INWARD_NO").getVal();
            
            stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHeader=stHeader.executeQuery("SELECT * FROM D_COM_INWARD_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INWARD_NO="+theDocNo);
            rsHeader.first();
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);

            
            rsHeader.updateString("INWARD_DATE",(String)getAttribute("INWARD_DATE").getObj());
            rsHeader.updateString("CHALAN_NO",(String)getAttribute("CHALAN_NO").getObj());
            rsHeader.updateString("SUPP_ID",(String)getAttribute("SUPP_ID").getObj());
            rsHeader.updateBoolean("CHANGED",true);
            rsHeader.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHeader.updateRow();
            
            //Remove old records
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            int lDocNo=(int)getAttribute("INWARD_NO").getVal();
            
            String strSQL="DELETE FROM D_COM_INWARD_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND INWARD_NO="+lDocNo;
            data.Execute(strSQL);
            
            
            //Insert new records
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_COM_INWARD_DETAIL WHERE INWARD_NO=1");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsInwardItem ObjItem=(clsInwardItem) colItems.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateInt("INWARD_NO",(int)getAttribute("INWARD_NO").getVal());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsDetail.updateDouble("GROSS_QTY",ObjItem.getAttribute("GROSS_QTY").getVal());
                rsDetail.updateBoolean("MIR_UPDATED",ObjItem.getAttribute("MIR_UPDATED").getBool());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.insertRow();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            int lDocNo=(int)getAttribute("INWARD_NO").getVal();
            String strSQL="";
            
            //First check that record is editable
                String strQry = "DELETE FROM D_COM_INWARD_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND INWARD_NO="+lDocNo;
                data.Execute(strQry);
                strQry = "DELETE FROM D_COM_INWARD_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND INWARD_NO="+lDocNo;
                data.Execute(strQry);
                
                LoadData(lCompanyID);
                return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,int pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND INWARD_NO="+pDocNo;
        clsInward ObjInward = new clsInward();
        ObjInward.Filter(strCondition,pCompanyID);
        return ObjInward;
    }
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_COM_INWARD_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_COM_INWARD_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND INWARD_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INWARD_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY INWARD_NO";
                rsResultSet=Stmt.executeQuery(strSql);
                Ready=true;
                MoveFirst();
                return false;
            }
            else {
                Ready=true;
                MoveFirst();
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean setData() {
        ResultSet rsTmp,rsLot;
        Statement tmpStmt,stLot;
        int Counter=0,Counter2=0;
        
        try {
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("INWARD_NO",rsResultSet.getInt("INWARD_NO"));
            setAttribute("INWARD_DATE",rsResultSet.getString("INWARD_DATE"));
            setAttribute("CHALAN_NO",rsResultSet.getString("CHALAN_NO"));
            setAttribute("SUPP_ID",rsResultSet.getString("SUPP_ID"));
            
            //Now turn of detail records
            colItems.clear();
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            int lDocNo=(int)getAttribute("INWARD_NO").getVal();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_COM_INWARD_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND INWARD_NO="+lDocNo+" ORDER BY SR_NO");
            
            rsTmp.first();
            
            Counter=0;
            
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                
                clsInwardItem ObjItem=new clsInwardItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("INWARD_NO",rsTmp.getInt("INWARD_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("GROSS_QTY",rsTmp.getDouble("GROSS_QTY"));
                ObjItem.setAttribute("MIR_UPDATED",rsTmp.getBoolean("MIR_UPDATED"));
                
                colItems.put(Long.toString(Counter),ObjItem);
                rsTmp.next();
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    

public static double getGrossWeight(int pCompanyID,String pChalanNo,String pSuppID,String pItemID)    
{
   Connection tmpConn;
   Statement stTmp;
   ResultSet rsTmp;
   double GrossQty=0;
       
    try
    {
        tmpConn=data.getConn();
        stTmp=tmpConn.createStatement();
        rsTmp=stTmp.executeQuery("SELECT GROSS_QTY FROM D_COM_INWARD_HEADER,D_COM_INWARD_DETAIL WHERE D_COM_INWARD_HEADER.COMPANY_ID=D_COM_INWARD_DETAIL.COMPANY_ID AND D_COM_INWARD_HEADER.INWARD_NO=D_COM_INWARD_DETAIL.INWARD_NO AND D_COM_INWARD_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_INWARD_HEADER.CHALAN_NO='"+pChalanNo+"' AND D_COM_INWARD_HEADER.SUPP_ID='"+pSuppID+"' AND D_COM_INWARD_DETAIL.ITEM_ID='"+pItemID+"'");
        rsTmp.first();
        
        if(rsTmp.getRow()>0)
        {
          GrossQty=rsTmp.getDouble("GROSS_QTY");
        }

   //tmpConn.close();
   stTmp.close();
   rsTmp.close();
        
    }
    catch(Exception e)
    {
        
    }
   return GrossQty;
}
    
}
