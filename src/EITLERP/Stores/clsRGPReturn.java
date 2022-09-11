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
  
/** 
 *
 * @author  nrpithva
 * @version
 */
 
public class clsRGPReturn {
    
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
    public clsRGPReturn() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("RGP_NO",new Variant(""));
        props.put("RETURN_NO",new Variant(""));
        props.put("RETURN_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("PREFIX",new Variant("")); //For Autonumber generation
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("FROM_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_RGP_RETURN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND RETURN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND RETURN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY RETURN_NO");
            HistoryView=false;
            Ready=true;
            MoveLast();
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
        Statement stmtDetail,stLot,stHistory,stHDetail,stHLot;
        ResultSet rsDetail,rsLot,rsHistory,rsHDetail,rsHLot;
        
        Statement stItem,stStock,stTmp,stItemMaster;
        ResultSet rsItem,rsStock,rsTmp,rsItemMaster;
        
        String RGPNo="",strSQL;
        int RGPSrNo=0;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("RETURN_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0))
            {
               //Within the year 
            }
            else
            {
               LastError="Return date is not within financial year.";
               return false;
            }
            //=====================================================//
            
            
            
            //=========== Check the Quantities entered against RGP.============= //
            for(int i=1;i<=colItems.size();i++) {
                clsRGPReturnItem ObjItem=(clsRGPReturnItem)colItems.get(Integer.toString(i));
                RGPNo=(String)ObjItem.getAttribute("RGP_NO").getObj();
                RGPSrNo=(int)ObjItem.getAttribute("RGP_SR_NO").getVal();
                
                
                double RGPQty=0;
                double PrevQty=0; //Previously Entered Qty against RGP
                double CurrentQty=0; //Currently entered Qty.
                
                if((!RGPNo.trim().equals(""))&&(RGPSrNo>0)) //RGP No. entered
                {
                    //Get the  MIR Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+RGPNo+"' AND SR_NO="+RGPSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        RGPQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_RGP_RETURN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RGP_NO='"+RGPNo+"' AND RGP_SR_NO="+RGPSrNo+" AND RETURN_NO NOT IN(SELECT RETURN_NO FROM D_INV_RGP_RETURN_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > RGPQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds RGP No. "+RGPNo+" Sr. No. "+RGPSrNo+" qty "+RGPQty+". Please verify the input.";
                        return false;
                    }
                    
                }
            }
            //============== RGP Checking Completed ====================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_RGP_RETURN_HEADER_H WHERE RETURN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_RGP_RETURN_DETAIL_H WHERE RETURN_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_RGP_RETURN_LOT_H WHERE RETURN_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                //-------- Update RETURN QTY -------------//
                for(int i=1;i<=colItems.size();i++) {
                    clsRGPReturnItem ObjItem=(clsRGPReturnItem)colItems.get(Integer.toString(i));
                    
                    RGPNo=(String)ObjItem.getAttribute("RGP_NO").getObj();
                    RGPSrNo=(int)ObjItem.getAttribute("RGP_SR_NO").getVal();
                    double Qty=ObjItem.getAttribute("QTY").getVal();
                    
                    // Update RETURN  Qty
                    data.Execute("UPDATE D_INV_RGP_DETAIL SET RETURNED_QTY=RETURNED_QTY+"+Qty+",BAL_QTY=QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+RGPNo.trim()+"' AND SR_NO="+RGPSrNo+" ");
                    data.Execute("UPDATE D_INV_RGP_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+RGPNo.trim()+"'");
                }
            }
            
            //--------- Generate Return no.  ------------
            setAttribute("RETURN_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,40 , (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
            rsResultSet.updateString("RETURN_DATE",(String)getAttribute("RETURN_DATE").getObj());
            rsResultSet.updateString("RGP_NO",(String)getAttribute("RGP_NO").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.insertRow();
            

            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
            rsHistory.updateString("RETURN_DATE",(String)getAttribute("RETURN_DATE").getObj());
            rsHistory.updateString("RGP_NO",(String)getAttribute("RGP_NO").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.insertRow();
            
                
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_INV_RGP_RETURN_DETAIL");
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_RGP_RETURN_LOT");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsRGPReturnItem ObjItem=(clsRGPReturnItem) colItems.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsDetail.updateString("RGP_NO",(String)ObjItem.getAttribute("RGP_NO").getObj());
                rsDetail.updateInt("RGP_SR_NO",(int)ObjItem.getAttribute("RGP_SR_NO").getVal());
                rsDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.insertRow();

                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                rsHDetail.updateInt("SR_NO",cnt);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("RGP_NO",(String)ObjItem.getAttribute("RGP_NO").getObj());
                rsHDetail.updateInt("RGP_SR_NO",(int)ObjItem.getAttribute("RGP_SR_NO").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
                //Insert Lots
                for(int j=1;j<=ObjItem.colLot.size();j++)
                {
                   clsRGPReturnLot ObjLot=(clsRGPReturnLot) ObjItem.colLot.get(Integer.toString(j));
                   rsLot.moveToInsertRow();
                   rsLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                   rsLot.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                   rsLot.updateInt("RETURN_SR_NO",cnt);
                   rsLot.updateInt("SR_NO",j);
                   rsLot.updateString("LOT_NO",(String)ObjLot.getAttribute("LOT_NO").getObj());
                   rsLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                   rsLot.updateBoolean("CHANGED",true);
                   rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                   rsLot.insertRow();
                   
                   rsHLot.moveToInsertRow();
                   rsHLot.updateInt("REVISION_NO",1);
                   rsHLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                   rsHLot.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                   rsHLot.updateInt("RETURN_SR_NO",cnt);
                   rsHLot.updateInt("SR_NO",j);
                   rsHLot.updateString("LOT_NO",(String)ObjLot.getAttribute("LOT_NO").getObj());
                   rsHLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                   rsHLot.updateBoolean("CHANGED",true);
                   rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                   rsHLot.insertRow();
                }
            }

            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=40; //RGP Return
            ObjFlow.DocNo=(String)getAttribute("RETURN_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_RGP_RETURN_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="RETURN_NO";
            
            if(ObjFlow.Status.equals("H")) {
                ObjFlow.Status="A";
                ObjFlow.To=ObjFlow.From;
                ObjFlow.UpdateFlow();
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
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
        Statement stmtDetail,stLot,stHistory,stHDetail,stHLot;
        ResultSet rsDetail,rsLot,rsHistory,rsHDetail,rsHLot;
        
        Statement stItem,stStock,stTmp,stItemMaster;
        ResultSet rsItem,rsStock,rsTmp,rsItemMaster;
        
        String RGPNo="",strSQL="",ReturnNo="";
        int RGPSrNo=0;
        
        try {
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ReqDate=java.sql.Date.valueOf((String)getAttribute("RETURN_DATE").getObj());
            
            if((ReqDate.after(FinFromDate)||ReqDate.compareTo(FinFromDate)==0)&&(ReqDate.before(FinToDate)||ReqDate.compareTo(FinToDate)==0))
            {
               //Within the year 
            }
            else
            {
               LastError="Return date is not within financial year.";
               return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_RGP_RETURN_HEADER_H WHERE RETURN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_RGP_RETURN_DETAIL_H WHERE RETURN_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_RGP_RETURN_LOT_H WHERE RETURN_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            
            ReturnNo=(String)getAttribute("RETURN_NO").getObj();
            
            //=========== Check the Quantities entered against RGP.============= //
            for(int i=1;i<=colItems.size();i++) {
                clsRGPReturnItem ObjItem=(clsRGPReturnItem)colItems.get(Integer.toString(i));
                RGPNo=(String)ObjItem.getAttribute("RGP_NO").getObj();
                RGPSrNo=(int)ObjItem.getAttribute("RGP_SR_NO").getVal();
                
                
                double RGPQty=0;
                double PrevQty=0; //Previously Entered Qty against RGP
                double CurrentQty=0; //Currently entered Qty.
                
                if((!RGPNo.trim().equals(""))&&(RGPSrNo>0)) //RGP No. entered
                {
                    //Get the  MIR Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_RGP_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+RGPNo+"' AND SR_NO="+RGPSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        RGPQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_RGP_RETURN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RGP_NO='"+RGPNo+"' AND RGP_SR_NO="+RGPSrNo+" AND RETURN_NO<>'"+ReturnNo+"' AND RETURN_NO NOT IN(SELECT RETURN_NO FROM D_INV_RGP_RETURN_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > RGPQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds RGP No. "+RGPNo+" Sr. No. "+RGPSrNo+" qty "+RGPQty+". Please verify the input.";
                        return false;
                    }
                    
                }
            }
            //============== RGP Checking Completed ====================//
            
            
            stmtDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            if(AStatus.equals("F")) {
                //-------- Update RETURN QTY -------------//
                for(int i=1;i<=colItems.size();i++) {
                    clsRGPReturnItem ObjItem=(clsRGPReturnItem)colItems.get(Integer.toString(i));
                    
                    RGPNo=(String)ObjItem.getAttribute("RGP_NO").getObj();
                    RGPSrNo=(int)ObjItem.getAttribute("RGP_SR_NO").getVal();
                    double Qty=ObjItem.getAttribute("QTY").getVal();
                    
                    // Update RETURN  Qty
                    data.Execute("UPDATE D_INV_RGP_DETAIL SET RETURNED_QTY=RETURNED_QTY+"+Qty+",BAL_QTY=QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+RGPNo.trim()+"' AND SR_NO="+RGPSrNo+" ");
                    data.Execute("UPDATE D_INV_RGP_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+RGPNo.trim()+"'");
                }
            }
            
            
            rsResultSet.first();
            rsResultSet.updateString("RETURN_DATE",(String)getAttribute("RETURN_DATE").getObj());
            rsResultSet.updateString("RGP_NO",(String)getAttribute("RGP_NO").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_RGP_RETURN_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+(String)getAttribute("RETURN_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("RETURN_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
            rsHistory.updateString("RETURN_DATE",(String)getAttribute("RETURN_DATE").getObj());
            rsHistory.updateString("RGP_NO",(String)getAttribute("RGP_NO").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.insertRow();
            
            
            //==== Delete Old Records =====//
            data.Execute("DELETE FROM D_INV_RGP_RETURN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+ReturnNo+"'");
            data.Execute("DELETE FROM D_INV_RGP_RETURN_LOT WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+ReturnNo+"'");
            
            
            rsDetail=stmtDetail.executeQuery("SELECT * FROM D_INV_RGP_RETURN_DETAIL");
            rsLot=stLot.executeQuery("SELECT * FROM D_INV_RGP_RETURN_LOT");
            
            //Now Insert records into detail table
            int cnt=0;
            int lnSize=colItems.size();
            
            for(cnt=1;cnt<=lnSize;cnt++) {
                clsRGPReturnItem ObjItem=(clsRGPReturnItem) colItems.get(Integer.toString(cnt));
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsDetail.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                rsDetail.updateInt("SR_NO",cnt);
                rsDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsDetail.updateString("RGP_NO",(String)ObjItem.getAttribute("RGP_NO").getObj());
                rsDetail.updateInt("RGP_SR_NO",(int)ObjItem.getAttribute("RGP_SR_NO").getVal());
                rsDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsDetail.updateBoolean("CHANGED",true);
                rsDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsDetail.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                rsHDetail.updateInt("SR_NO",cnt);
                rsHDetail.updateString("ITEM_ID",(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateInt("UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("RGP_NO",(String)ObjItem.getAttribute("RGP_NO").getObj());
                rsHDetail.updateInt("RGP_SR_NO",(int)ObjItem.getAttribute("RGP_SR_NO").getVal());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                //Insert Lots
                for(int j=1;j<=ObjItem.colLot.size();j++)
                {
                   clsRGPReturnLot ObjLot=(clsRGPReturnLot) ObjItem.colLot.get(Integer.toString(j));
                   rsLot.moveToInsertRow();
                   rsLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                   rsLot.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                   rsLot.updateInt("RETURN_SR_NO",cnt);
                   rsLot.updateInt("SR_NO",j);
                   rsLot.updateString("LOT_NO",(String)ObjLot.getAttribute("LOT_NO").getObj());
                   rsLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                   rsLot.updateBoolean("CHANGED",true);
                   rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                   rsLot.insertRow();
                   
                   rsHLot.moveToInsertRow();
                   rsHLot.updateInt("REVISION_NO",RevNo);
                   rsHLot.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                   rsHLot.updateString("RETURN_NO",(String)getAttribute("RETURN_NO").getObj());
                   rsHLot.updateInt("RETURN_SR_NO",cnt);
                   rsHLot.updateInt("SR_NO",j);
                   rsHLot.updateString("LOT_NO",(String)ObjLot.getAttribute("LOT_NO").getObj());
                   rsHLot.updateDouble("LOT_QTY",ObjLot.getAttribute("LOT_QTY").getVal());
                   rsHLot.updateBoolean("CHANGED",true);
                   rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                   rsHLot.insertRow();
                }
            }

            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=40; //RGP Return
            ObjFlow.DocNo=(String)getAttribute("RETURN_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_RGP_RETURN_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="RETURN_NO";

            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_RGP_RETURN_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=40 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            if(ObjFlow.Status.equals("H")) {
                //Do nothing
                if(IsRejected) {
                    ObjFlow.Status="A";
                    ObjFlow.To=ObjFlow.From;
                    ObjFlow.UpdateFlow();
                }
            }
            else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            return false;
        }
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("RETURN_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM D_INV_RGP_RETURN_HEADER WHERE COMPANY_ID=" + lCompanyID +" AND RETURN_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM D_INV_RGP_RETURN_DETAIL WHERE COMPANY_ID=" + lCompanyID +" AND RETURN_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM D_INV_RGP_RETURN_LOT WHERE COMPANY_ID=" + lCompanyID +" AND RETURN_NO='"+lDocNo+"'";
                data.Execute(strQry);
                
                LoadData(lCompanyID);
                return true;
            }
            else {
                LastError="Record cannot be deleted. Either it is Approved/Rejected or you don't have rights to delete it";
                return false;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND RETURN_NO='"+pDocNo+"'";
        clsRGPReturn ObjReturn = new clsRGPReturn();
        ObjReturn.Filter(strCondition,pCompanyID);
        return ObjReturn;
    }
    
            
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_RGP_RETURN_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_RGP_RETURN_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND RETURN_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND RETURN_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY RETURN_NO";
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
    
    
    public boolean setData() {
        ResultSet rsTmp,rsLot;
        Statement tmpStmt,stLot;
        int Counter=0,Counter2=0;
        int RevNo=0;
        
        try {
            if(HistoryView)
            {
              RevNo=rsResultSet.getInt("REVISION_NO");
              setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));  
            }
            else
            {
              setAttribute("REVISION_NO",0);  
            }
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("RETURN_NO",rsResultSet.getString("RETURN_NO"));
            setAttribute("RETURN_DATE",rsResultSet.getString("RETURN_DATE"));
            setAttribute("RGP_NO",rsResultSet.getString("RGP_NO"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            //Now turn of detail records
            colItems.clear();
            
            int lCompanyID=(int)getAttribute("COMPANY_ID").getVal();
            String lDocNo=(String)getAttribute("RETURN_NO").getObj();
            
            if(HistoryView)
            {
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_RETURN_DETAIL_H WHERE COMPANY_ID="+lCompanyID+" AND RETURN_NO='"+lDocNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else
            {
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_RGP_RETURN_DETAIL WHERE COMPANY_ID="+lCompanyID+" AND RETURN_NO='"+lDocNo+"' ORDER BY SR_NO");    
            }
            rsTmp.first();
            
            Counter=0;
            
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                
                clsRGPReturnItem ObjItem=new clsRGPReturnItem();
                
                int ReturnSrNo=rsTmp.getInt("SR_NO");
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("RETURN_NO",rsTmp.getString("RETURN_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_ID",rsTmp.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_DESC",rsTmp.getString("ITEM_DESC"));
                ObjItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                ObjItem.setAttribute("RGP_NO",rsTmp.getString("RGP_NO"));
                ObjItem.setAttribute("RGP_SR_NO",rsTmp.getInt("RGP_SR_NO"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                
                //Get lot nos.
                if(HistoryView)
                {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_RGP_RETURN_LOT_H WHERE COMPANY_ID="+lCompanyID+" AND RETURN_NO='"+lDocNo+"' AND RETURN_SR_NO="+ReturnSrNo+" AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
                }
                else
                {
                    rsLot=stLot.executeQuery("SELECT * FROM D_INV_RGP_RETURN_LOT WHERE COMPANY_ID="+lCompanyID+" AND RETURN_NO='"+lDocNo+"' AND RETURN_SR_NO="+ReturnSrNo+" ORDER BY SR_NO");    
                }
                rsLot.first();
                
                Counter2=0;
                
                while(!rsLot.isAfterLast()&&rsLot.getRow()>0)
                {
                    Counter2++;
                    
                    clsRGPReturnLot ObjLot=new clsRGPReturnLot();
                    ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("RETURN_NO",rsLot.getString("RETURN_NO"));
                    ObjLot.setAttribute("RETURN_SR_NO",rsLot.getInt("RETURN_SR_NO"));
                    ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                    ObjLot.setAttribute("LOT_NO",rsLot.getString("LOT_NO"));
                    ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                    
                    ObjItem.colLot.put(Integer.toString(Counter2), ObjLot);
                    rsLot.next();
                }
                
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
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView)
            {
              return false;  
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_RGP_RETURN_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND RETURN_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=40 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    //Yes document is waiting for this user
                    return true;
                }
                else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pDocNo,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView)
            {
              return false;  
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_RGP_RETURN_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND RETURN_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=40 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    //Yes document is waiting for this user
                    return true;
                }
                else {
                    //Document is not editable by this user
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
    }

public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom)    
{
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;
      
        HashMap List=new HashMap();
        long Counter=0;
        
        try
        {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate)
            {
                strSQL="SELECT D_INV_RGP_RETURN_HEADER.RETURN_NO,D_INV_RGP_RETURN_HEADER.RETURN_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_RGP_RETURN_HEADER,D_COM_DOC_DATA WHERE D_INV_RGP_RETURN_HEADER.RETURN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_RETURN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_RETURN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=40 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate)
            {
                strSQL="SELECT D_INV_RGP_RETURN_HEADER.RETURN_NO,D_INV_RGP_RETURN_HEADER.RETURN_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_RGP_RETURN_HEADER,D_COM_DOC_DATA WHERE D_INV_RGP_RETURN_HEADER.RETURN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_RETURN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_RETURN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=40 ORDER BY D_INV_RGP_RETURN_HEADER.RETURN_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo)
            {
               strSQL="SELECT D_INV_RGP_RETURN_HEADER.RETURN_NO,D_INV_RGP_RETURN_HEADER.RETURN_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_RGP_RETURN_HEADER,D_COM_DOC_DATA WHERE D_INV_RGP_RETURN_HEADER.RETURN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_RETURN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_RETURN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=40 ORDER BY D_INV_RGP_RETURN_HEADER.RETURN_NO"; 
            }
            //strSQL="SELECT D_INV_RGP_RETURN_HEADER.RETURN_NO,D_INV_RGP_RETURN_HEADER.RETURN_DATE FROM D_INV_RGP_RETURN_HEADER,D_COM_DOC_DATA WHERE D_INV_RGP_RETURN_HEADER.RETURN_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_RGP_RETURN_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_RGP_RETURN_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=40";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast())
            {
            if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("RETURN_DATE"),FinYearFrom))    
            {
            Counter=Counter+1;
            clsRGPReturn ObjDoc=new clsRGPReturn();
                
            //------------- Header Fields --------------------//
            ObjDoc.setAttribute("RETURN_NO",rsTmp.getString("RETURN_NO"));
            ObjDoc.setAttribute("RETURN_DATE",rsTmp.getString("RETURN_DATE"));
            ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
            ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
            // ----------------- End of Header Fields ------------------------------------//
            
            //Put the prepared user object into list    
            List.put(Long.toString(Counter),ObjDoc);
            }
            
            if(!rsTmp.isAfterLast())
            {
            rsTmp.next();
            }
           }//Out While
            
        //tmpConn.close();
        rsTmp.close();
        tmpStmt.close();
            
        }
        catch(Exception e)
        {
        }
        
      return List; 
}
    

   public static HashMap getReturnItemList(int pCompanyID,String pGprNo,boolean pAllData)
    {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
        Statement stLot=null;
        ResultSet rsLot=null;
                
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        int Counter1 = 0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSql;
        
        try
        {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(pAllData)
            {
                strSql = "SELECT * FROM D_INV_RGP_RETURN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+pGprNo+"' AND RETURN_NO IN (SELECT RETURN_NO FROM D_INV_RGP_RETURN_HEADER WHERE RETURN_NO='"+pGprNo+"' AND APPROVED=1) ORDER BY RETURN_NO";
            }
            else{
                strSql = "SELECT * FROM D_INV_RGP_RETURN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+pGprNo.trim()+"' AND RETURN_NO IN (SELECT RETURN_NO FROM D_INV_RGP_RETURN_HEADER WHERE RETURN_NO='"+pGprNo+"' AND APPROVED=1) ORDER BY RETURN_NO";
            }
            
            rsTmp2=tmpStmt.executeQuery(strSql);
            rsTmp2.first();
       
            Counter1=0;            
            while(! rsTmp2.isAfterLast())
            {
               Counter1++;
               clsRGPReturnItem ObjMRItems=new clsRGPReturnItem();

                   ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                   ObjMRItems.setAttribute("RETURN_NO",rsTmp2.getString("RETURN_NO"));
                   ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                   ObjMRItems.setAttribute("ITEM_ID",rsTmp2.getString("ITEM_ID"));
                   ObjMRItems.setAttribute("ITEM_DESC",rsTmp2.getString("ITEM_DESC"));
                   ObjMRItems.setAttribute("QTY",rsTmp2.getFloat("QTY"));
                   ObjMRItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                   ObjMRItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                   ObjMRItems.setAttribute("RGP_NO",rsTmp2.getString("RGP_NO"));
                   ObjMRItems.setAttribute("RGP_SR_NO",rsTmp2.getString("RGP_SR_NO"));

                   //Now Fetch Lots.
                   int ReturnSrno=rsTmp2.getInt("SR_NO");
                   
                   stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                   rsLot=stLot.executeQuery("SELECT * FROM D_INV_RGP_RETURN_LOT WHERE COMPANY_ID="+pCompanyID+" AND RETURN_NO='"+pGprNo+"' AND RETURN_SR_NO="+ReturnSrno);
                   rsLot.first();

                   //Clear existing data
                   ObjMRItems.colLot.clear();
                   
                   
                   int Counter2 = 0;
                   if(rsLot.getRow()>0)
                   {
                       Counter2=0;
                       while(!rsLot.isAfterLast())
                       {
                           Counter2++;
                           clsRGPReturnLot ObjLot=new clsRGPReturnLot();

                           ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                           ObjLot.setAttribute("RETURN_NO",rsLot.getString("RETURN_NO"));
                           ObjLot.setAttribute("RETURN_SR_NO",rsLot.getInt("RETURN_SR_NO"));
                           ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                           ObjLot.setAttribute("LOT_NO",rsLot.getString("LOT_NO"));
                           ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));

                           ObjMRItems.colLot.put(Integer.toString(Counter2),ObjLot); 
                           rsLot.next();
                       }
                   }
                   
                List.put(Integer.toString(Counter1),ObjMRItems);
                rsTmp2.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
        stLot.close();
        rsLot.close();
        rsTmp2.close();
        tmpStmt2.close();
            
        }
        catch(Exception e)
        {            
        }
        
        return List; 
    }

   public static HashMap getReturnLotList(int pCompanyID,String pGprNo,int pSrno,boolean pAllData)
    {
        ResultSet rsTmp=null;
        Connection tmpConn=null;
        Statement tmpStmt=null;
                
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        int Counter1 = 0;
        
        HashMap List=new HashMap();
        long Counter=0;
        String strSql;
        
        try
        {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(pAllData)
            {
                strSql = "SELECT * FROM D_INV_RGP_RETURN_LOT WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY RETURN_NO";
            }
            else{
                strSql = "SELECT * FROM D_INV_RGP_RETURN_LOT WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+pGprNo.trim()+"' AND RETURN_SR_NO="+pSrno+" ORDER BY RETURN_NO";
            }
            
            rsTmp2=tmpStmt.executeQuery(strSql);
            rsTmp2.first();
       
            Counter1=0;            
            while(! rsTmp2.isAfterLast())
            {
               Counter1++;
               clsRGPReturnLot ObjMRItems=new clsRGPReturnLot();

                   ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                   ObjMRItems.setAttribute("RETURN_NO",rsTmp2.getString("RETURN_NO"));
                   ObjMRItems.setAttribute("RETURN_SR_NO",rsTmp2.getString("RETURN_SR_NO"));
                   ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                   ObjMRItems.setAttribute("LOT_NO",rsTmp2.getString("LOT_NO"));
                   ObjMRItems.setAttribute("LOT_QTY",rsTmp2.getFloat("LOT_QTY"));
                
                List.put(Integer.toString(Counter1),ObjMRItems);
                rsTmp2.next();
            }
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
        rsTmp2.close();
        tmpStmt2.close();
            
        }
        catch(Exception e)
        {            
        }
        
        return List; 
    }

    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_RGP_RETURN_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND RETURN_NO='"+pDocNo+"'");
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
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_RGP_RETURN_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RETURN_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsRGPReturn ObjRGP=new clsRGPReturn();
                
                ObjRGP.setAttribute("RETURN_NO",rsTmp.getString("RETURN_NO"));
                ObjRGP.setAttribute("RETURN_DATE",rsTmp.getString("RETURN_DATE"));
                ObjRGP.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjRGP.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjRGP.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjRGP.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjRGP.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjRGP);
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
   

    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT RETURN_NO,APPROVED,CANCELLED FROM D_INV_RGP_RETURN_HEADER WHERE COMPANY_ID="+pCompanyID+" AND RETURN_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELLED"))
                    {
                    strMessage="Document is cancelled";    
                    }
                    else
                    {
                    strMessage="";
                    }
                }
                else {
                    strMessage="Document is created but is under approval";
                }
                
            }
            else {
                strMessage="Document does not exist";
            }
            rsTmp.close();
        }
        catch(Exception e) {
        }
        
        return strMessage;
        
    }
    
}
