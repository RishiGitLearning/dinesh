/*
 * clsNRGP.java
 *
 * Created on April 23, 2004, 3:25 PM
 */

package EITLERP.Stores;

import java.net.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import EITLERP.*;

/**
 *
 * @author  nhpatel
 * @version
 */
public class clsDecConsumed {
    
    public String LastError="";
    private ResultSet rsDec;
    private Connection Conn;
    private Statement Stmt;
    
    public HashMap colLineItems;
    private HashMap props;
    public static int ModuleID=39;
    public boolean Ready = false;
    
    //History Related properties
    private boolean HistoryView=false;
    
    //Flag Indicating whether user has entered the document no.
    public boolean UserDocNo=false;
    
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
    
    
    /** Creates new clsNRGP */
    public clsDecConsumed() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("DOC_NO",new Variant(""));
        props.put("DOC_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CANCELLED",new Variant(false));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(false));
        props.put("CHANGED_DATE",new Variant(""));
        
        //Create a new object for line items
        colLineItems=new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDec=Stmt.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO");
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
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsDec.close();
        }
        catch(Exception e) {
            
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsDec.first();
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
            if(rsDec.isAfterLast()||rsDec.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsDec.last();
            }
            else {
                rsDec.next();
                if(rsDec.isAfterLast()) {
                    rsDec.last();
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
            if(rsDec.isFirst()||rsDec.isBeforeFirst()) {
                rsDec.first();
            }
            else {
                rsDec.previous();
                if(rsDec.isBeforeFirst()) {
                    rsDec.first();
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
            rsDec.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert(String pPrefix,String pDocno) {
        Statement stTmp,stHistory,stHDetail;
        ResultSet rsTmp,rsHistory,rsHDetail;
        
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "";
        String DecNo="";
        int DecSrNo=0;
        
        
        try {
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            
            //=========== Check the Quantities entered against Rejection.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsDecConsumedItem ObjItem=(clsDecConsumedItem) colLineItems.get(Integer.toString(i));
                
                DecNo=(String) ObjItem.getAttribute("DEC_ID").getObj();
                DecSrNo=(int) ObjItem.getAttribute("DEC_SR_NO").getVal();
                
                double DecQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!DecNo.trim().equals(""))&&(DecSrNo>0)) {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT RECD_QTY FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        DecQty=rsTmp.getDouble("RECD_QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    //strSQL="SELECT SUM(CONSUMED_QTY) AS SUMQTY FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DEC_ID='"+DecNo+"' AND DEC_SR_NO="+DecSrNo+" AND DOC_NO NOT IN (SELECT DOC_NO FROM D_INV_DEC_CONSUME_HEADER WHERE CANCELED=1)";
                    strSQL="SELECT SUM(CONSUMED_QTY) AS SUMQTY FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DEC_ID='"+DecNo+"' AND DEC_SR_NO="+DecSrNo+" AND DOC_NO <> '"+pDocno+"' ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+DecNo+"' AND DECLARATION_SRNO="+DecSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    if(rsTmp.getRow()>0) {
                        PrevQty+=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("CONSUMED_QTY").getVal();
                    
                    if((EITLERPGLOBAL.round(CurrentQty+PrevQty,2)) > EITLERPGLOBAL.round(DecQty,2)) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Declaration No. "+DecNo+" Sr. No. "+DecSrNo+" qty "+DecQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            //=======================================//
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsDecConsumedItem ObjItem=(clsDecConsumedItem)colLineItems.get(Integer.toString(i));
                    
                    DecNo=(String)ObjItem.getAttribute("DEC_ID").getObj();
                    DecSrNo=(int)ObjItem.getAttribute("DEC_SR_NO").getVal();
                    
                    double Qty=ObjItem.getAttribute("CONSUMED_QTY").getVal();
                    
                    
                    if((!DecNo.trim().equals(""))&&DecSrNo>0) {
                        //strSQL="UPDATE D_INV_DEC_CONSUMED_DETAIL SET RETURNED_QTY=RETURNED_QTY+"+Qty+",BAL_QTY=RECD_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLRATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo;
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET CONSUMED_QTY=(CONSUMED_QTY+"+Qty+") WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo; //,BAL_QTY=BAL_QTY-CONSUMED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() 
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET BAL_QTY=BAL_QTY-CONSUMED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo; //CONSUMED_QTY=(CONSUMED_QTY+"+Qty+"),
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+DecNo+"' ";
                        data.Execute(strSQL);
                    }
                }
            }
            //=======================================//
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_HEADER_H WHERE DOC_NO='1'");
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            
            //--------- Generate New MIR No. ------------
            if(UserDocNo) {
                rsTmp=data.getResult("SELECT DOC_NO FROM D_INV_DEC_CONSUME_HEADER WHERE DOC_NO='"+((String)getAttribute("DOC_NO").getObj()).trim()+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    LastError="Document no. already exist. Please specify other document no.";
                    return false;
                }
            }
            else {
                setAttribute("DOC_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,39, (String)getAttribute("PREFIX").getObj(),(String)getAttribute("SUFFIX").getObj(),true));
            }
            //-------------------------------------------------
            
            rsDec.moveToInsertRow();
            rsDec.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsDec.updateString("DOC_NO",(String) getAttribute("DOC_NO").getObj());
            rsDec.updateString("DOC_DATE",(String) getAttribute("DOC_DATE").getObj());
            rsDec.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsDec.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsDec.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsDec.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsDec.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsDec.updateBoolean("CHANGED",true);
            rsDec.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDec.updateBoolean("APPROVED",false);
            rsDec.updateString("APPROVED_DATE","0000-00-00");
            rsDec.updateBoolean("REJECTED",false);
            rsDec.updateString("REJECTED_DATE","0000-00-00");
            rsDec.updateBoolean("CANCELLED",false);
            rsDec.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO",(String) getAttribute("DOC_NO").getObj());
            rsHistory.updateString("DOC_DATE",(String) getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            // Inserting records in NRGP Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND DOC_NO='1'");
            String DocNo=(String) getAttribute("DOC_NO").getObj();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsDecConsumedItem ObjNRGPItem=(clsDecConsumedItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsTmp.updateString("DOC_NO",DocNo);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("DEC_ID",(String) ObjNRGPItem.getAttribute("DEC_ID").getObj());
                rsTmp.updateInt("DEC_SR_NO",(int)ObjNRGPItem.getAttribute("DEC_SR_NO").getVal());
                rsTmp.updateString("ITEM_DESC",(String) ObjNRGPItem.getAttribute("ITEM_DESC").getObj());
                rsTmp.updateInt("UNIT_ID",(int) ObjNRGPItem.getAttribute("UNIT_ID").getVal());
                rsTmp.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsTmp.updateDouble("DEC_QTY",ObjNRGPItem.getAttribute("DEC_QTY").getVal());
                rsTmp.updateDouble("CONSUMED_QTY",ObjNRGPItem.getAttribute("CONSUMED_QTY").getVal());
                rsTmp.updateLong("CREATED_BY",(long) ObjNRGPItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) ObjNRGPItem.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) ObjNRGPItem.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) ObjNRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateString("DOC_NO",DocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("DEC_ID",(String) ObjNRGPItem.getAttribute("DEC_ID").getObj());
                rsHDetail.updateInt("DEC_SR_NO",(int)ObjNRGPItem.getAttribute("DEC_SR_NO").getVal());
                rsHDetail.updateString("ITEM_DESC",(String) ObjNRGPItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateInt("UNIT_ID",(int) ObjNRGPItem.getAttribute("UNIT_ID").getVal());
                rsHDetail.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("DEC_QTY",ObjNRGPItem.getAttribute("DEC_QTY").getVal());
                rsHDetail.updateDouble("CONSUMED_QTY",ObjNRGPItem.getAttribute("CONSUMED_QTY").getVal());
                rsHDetail.updateLong("CREATED_BY",(long) ObjNRGPItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjNRGPItem.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjNRGPItem.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) ObjNRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=39;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_DEC_CONSUME_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
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
            
            // Specified procedure over here
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError= e.getMessage();
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean Update() {
        Statement stTmp,stHistory,stHDetail,stHLot;
        ResultSet rsTmp,rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        String strSQL = "";
        String DecNo="";
        int DecSrNo=0;
        
        try {
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_HEADER_H WHERE DOC_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_DETAIL_H WHERE DOC_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String DocNo=(String)getAttribute("DOC_NO").getObj();
            
            //=========== Check the Quantities entered against Rejection.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsDecConsumedItem ObjItem=(clsDecConsumedItem) colLineItems.get(Integer.toString(i));
                
                DecNo=(String) ObjItem.getAttribute("DEC_ID").getObj();
                DecSrNo=(int) ObjItem.getAttribute("DEC_SR_NO").getVal();
                
                double DecQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!DecNo.trim().equals(""))&&(DecSrNo>0)) {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT RECD_QTY FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        DecQty=rsTmp.getDouble("RECD_QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    //strSQL="SELECT SUM(CONSUMED_QTY) AS SUMQTY FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DEC_ID='"+DecNo+"' AND DEC_SR_NO="+DecSrNo+" AND DOC_NO NOT IN (SELECT DOC_NO FROM D_INV_DEC_CONSUME_HEADER WHERE CANCELED=1)";
                    strSQL="SELECT SUM(CONSUMED_QTY) AS SUMQTY FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DEC_ID='"+DecNo+"' AND DEC_SR_NO="+DecSrNo+" AND DOC_NO <> '"+DocNo+"' ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+DecNo+"' AND DECLARATION_SRNO="+DecSrNo+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    if(rsTmp.getRow()>0) {
                        PrevQty+=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("CONSUMED_QTY").getVal();
                    
                    if((EITLERPGLOBAL.round(CurrentQty+PrevQty,2)) > EITLERPGLOBAL.round(DecQty,2)) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Declaration No. "+DecNo+" Sr. No. "+DecSrNo+" qty "+DecQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            //=======================================//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsDecConsumedItem ObjItem=(clsDecConsumedItem)colLineItems.get(Integer.toString(i));
                    
                    DecNo=(String)ObjItem.getAttribute("DEC_ID").getObj();
                    DecSrNo=(int)ObjItem.getAttribute("DEC_SR_NO").getVal();
                    
                    double Qty=ObjItem.getAttribute("CONSUMED_QTY").getVal();
                    
                    
                    if((!DecNo.trim().equals(""))&&DecSrNo>0) {
                        //strSQL="UPDATE D_INV_DEC_CONSUMED_DETAIL SET RETURNED_QTY=RETURNED_QTY+"+Qty+",BAL_QTY=RECD_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLRATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo;
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET CONSUMED_QTY=(CONSUMED_QTY+"+Qty+") WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo; //,BAL_QTY=BAL_QTY-CONSUMED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() 
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET BAL_QTY=BAL_QTY-CONSUMED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+DecNo+"' AND SR_NO="+DecSrNo; //CONSUMED_QTY=(CONSUMED_QTY+"+Qty+"),
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+DecNo+"' ";
                        data.Execute(strSQL);
                    }
                }
            }
            //=======================================//
            
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            rsDec.updateString("DOC_DATE",(String) getAttribute("DOC_DATE").getObj());
            rsDec.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsDec.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsDec.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsDec.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsDec.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsDec.updateBoolean("CHANGED",true);
            rsDec.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsDec.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsDec.updateBoolean("APPROVED",false);
            rsDec.updateString("APPROVED_DATE","0000-00-00");
            rsDec.updateBoolean("REJECTED",false);
            rsDec.updateString("REJECTED_DATE","0000-00-00");
            rsDec.updateBoolean("CANCELLED",false);
            rsDec.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_DEC_CONSUME_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("DOC_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("DOC_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DOC_NO",(String) getAttribute("DOC_NO").getObj());
            rsHistory.updateString("DOC_DATE",(String) getAttribute("DOC_DATE").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            data.Execute("DELETE FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+DocNo+"'");
            
            Statement tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='1'");
            
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsDecConsumedItem ObjNRGPItem=(clsDecConsumedItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsTmp.updateString("DOC_NO",DocNo);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("DEC_ID",(String) ObjNRGPItem.getAttribute("DEC_ID").getObj());
                rsTmp.updateInt("DEC_SR_NO",(int)ObjNRGPItem.getAttribute("DEC_SR_NO").getVal());
                rsTmp.updateString("ITEM_DESC",(String) ObjNRGPItem.getAttribute("ITEM_DESC").getObj());
                rsTmp.updateInt("UNIT_ID",(int) ObjNRGPItem.getAttribute("UNIT_ID").getVal());
                rsTmp.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsTmp.updateDouble("DEC_QTY",ObjNRGPItem.getAttribute("DEC_QTY").getVal());
                rsTmp.updateDouble("CONSUMED_QTY",ObjNRGPItem.getAttribute("CONSUMED_QTY").getVal());
                rsTmp.updateLong("CREATED_BY",(long) ObjNRGPItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) ObjNRGPItem.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) ObjNRGPItem.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) ObjNRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsHDetail.updateString("DOC_NO",DocNo);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("DEC_ID",(String) ObjNRGPItem.getAttribute("DEC_ID").getObj());
                rsHDetail.updateInt("DEC_SR_NO",(int)ObjNRGPItem.getAttribute("DEC_SR_NO").getVal());
                rsHDetail.updateString("ITEM_DESC",(String) ObjNRGPItem.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateInt("UNIT_ID",(int) ObjNRGPItem.getAttribute("UNIT_ID").getVal());
                rsHDetail.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("DEC_QTY",ObjNRGPItem.getAttribute("DEC_QTY").getVal());
                rsHDetail.updateDouble("CONSUMED_QTY",ObjNRGPItem.getAttribute("CONSUMED_QTY").getVal());
                rsHDetail.updateLong("CREATED_BY",(long) ObjNRGPItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjNRGPItem.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjNRGPItem.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) ObjNRGPItem.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
            }
            
            
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=39;
            ObjFlow.DocNo=(String)getAttribute("DOC_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_DEC_CONSUME_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="DOC_NO";
            
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                ObjFlow.IsCreator=true;
            }
            //==========================================//
            
            
            if(ObjFlow.Status.equals("H")) {
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
            
            
            return true;
        }
        catch(Exception e) {
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
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0) {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID=39 AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND TYPE='C' AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
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
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID=39 AND DOC_NO='"+pDocNo+"' AND USER_ID="+pUserID+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
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
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        return false;
    }
    
    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND DOC_NO='"+pDocNo+"'";
        clsDecConsumed ObjDoc = new clsDecConsumed();
        ObjDoc.Filter(strCondition,pCompanyID);
        return ObjDoc;
    }
    
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_DEC_CONSUME_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsDec=Stmt.executeQuery(strSql);
            
            if(!rsDec.first()) {
                strSql = "SELECT * FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DOC_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DOC_NO";
                rsDec=Stmt.executeQuery(strSql);
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
    
    
    private boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsDec.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsDec.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsDec.getLong("COMPANY_ID"));
            setAttribute("DOC_NO",rsDec.getString("DOC_NO"));
            setAttribute("DOC_DATE",rsDec.getString("DOC_DATE"));
            setAttribute("REMARKS",rsDec.getString("REMARKS"));
            setAttribute("CANCELLED",rsDec.getInt("CANCELLED"));
            setAttribute("APPROVED",rsDec.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsDec.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsDec.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsDec.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsDec.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsDec.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsDec.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsDec.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsDec.getString("MODIFIED_DATE"));
            setAttribute("HIERARCHY_ID",rsDec.getInt("HIERARCHY_ID"));
            
            colLineItems.clear();
            
            String DocNo=(String) getAttribute("DOC_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_DETAIL_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+DocNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+DocNo+"' ORDER BY SR_NO");
            }
            
            Counter=0;
            rsTmp.first();
            while(!rsTmp.isAfterLast()) {
                
                Counter=Counter+1;
                
                clsDecConsumedItem ObjNRGPItem=new clsDecConsumedItem();
                
                ObjNRGPItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjNRGPItem.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjNRGPItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjNRGPItem.setAttribute("DEC_ID",rsTmp.getString("DEC_ID"));
                ObjNRGPItem.setAttribute("DEC_SR_NO",rsTmp.getInt("DEC_SR_NO"));
                ObjNRGPItem.setAttribute("ITEM_DESC",rsTmp.getString("ITEM_DESC"));
                ObjNRGPItem.setAttribute("UNIT_ID",rsTmp.getInt("UNIT_ID"));
                ObjNRGPItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjNRGPItem.setAttribute("DEC_QTY",rsTmp.getDouble("DEC_QTY"));
                ObjNRGPItem.setAttribute("CONSUMED_QTY",rsTmp.getDouble("CONSUMED_QTY"));
                ObjNRGPItem.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjNRGPItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjNRGPItem.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjNRGPItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                colLineItems.put(Long.toString(Counter),ObjNRGPItem);
                
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_DEC_CONSUME_HEADER.DOC_NO,D_INV_DEC_CONSUME_HEADER.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_DEC_CONSUME_HEADER,D_COM_DOC_DATA WHERE D_INV_DEC_CONSUME_HEADER.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_DEC_CONSUME_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_DEC_CONSUME_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_DOC_DATA.USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=39 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_DEC_CONSUME_HEADER.DOC_NO,D_INV_DEC_CONSUME_HEADER.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_DEC_CONSUME_HEADER,D_COM_DOC_DATA WHERE D_INV_DEC_CONSUME_HEADER.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_DEC_CONSUME_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_DEC_CONSUME_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_DOC_DATA.USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=39 ORDER BY D_INV_DEC_CONSUME_HEADER.DOC_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_DEC_CONSUME_HEADER.DOC_NO,D_INV_DEC_CONSUME_HEADER.DOC_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_INV_DEC_CONSUME_HEADER,D_COM_DOC_DATA WHERE D_INV_DEC_CONSUME_HEADER.DOC_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_DEC_CONSUME_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_DEC_CONSUME_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_DOC_DATA.USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=39 ORDER BY D_INV_DEC_CONSUME_HEADER.DOC_NO";
            }
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsDecConsumed ObjDoc=new clsDecConsumed();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjDoc.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjDoc);
                
                if(!rsTmp.isAfterLast()) {
                    rsTmp.next();
                }
            }//Out While
            
            //tmpConn.close();
            rsTmp.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsDec=Stmt.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_DEC_CONSUME_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsDecConsumed ObjNRGP=new clsDecConsumed();
                
                ObjNRGP.setAttribute("DOC_NO",rsTmp.getString("DOC_NO"));
                ObjNRGP.setAttribute("DOC_DATE",rsTmp.getString("DOC_DATE"));
                ObjNRGP.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjNRGP.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjNRGP.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjNRGP.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjNRGP.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjNRGP);
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
            rsTmp=data.getResult("SELECT DOC_NO,APPROVED,CANCELLED FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELLED")) {
                        strMessage="Document is cancelled";
                    }
                    else {
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
    
    
    public static boolean IsNRGPExist(int pCompanyID,String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT DOC_NO FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                isExist=true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        }
        catch(Exception e) {
            return isExist;
        }
    }
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT DOC_NO FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        } catch(Exception e) {
        }
        return canCancel;
    }
    
    
    public static boolean CancelDoc(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            
            Connection tmpConn;
            Statement stSTM;
            
            if(CanCancel(pCompanyID,pDocNo)) {
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_DEC_CONSUME_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                if(ApprovedDoc) {
                } else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID=39");
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_DEC_CONSUME_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
}