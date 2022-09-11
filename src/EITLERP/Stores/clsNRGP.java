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
public class clsNRGP {
    
    public String LastError="";
    private ResultSet rsNRGP;
    private Connection Conn;
    private Statement Stmt;
    
    public HashMap colLineItems;
    private HashMap props;
    
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
    public clsNRGP() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("GATEPASS_NO",new Variant(""));
        props.put("GATEPASS_DATE",new Variant(""));
        props.put("FOR_DEPT",new Variant(0));
        props.put("GATEPASS_TYPE",new Variant(""));
        props.put("NRGP_WITH_LOT",new Variant(false));
        props.put("SUPP_ID",new Variant(""));
        props.put("MODE_TRANSPORT",new Variant(0));
        props.put("TRANPORTER",new Variant(0));
        props.put("TOTAL_AMOUNT",new Variant(0));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("REJECTED",new Variant(false));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(false));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));

        props.put("DESPATCH_MODE",new Variant(""));
        props.put("GROSS_WEIGHT",new Variant(""));
        
        props.put("TRANSPORTER_GSTIN",new Variant(""));
        
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
        props.put("FREIGHT",new Variant(0));
        props.put("OCTROI",new Variant(0));
        
        props.put("USER_ID",new Variant(0));
        props.put("PARTY_NAME",new Variant(""));
        props.put("ADD1",new Variant(""));
        props.put("ADD2",new Variant(""));
        props.put("ADD3",new Variant(""));
        props.put("CITY",new Variant(""));
        props.put("EXP_RETURN_DATE",new Variant(""));
        
        props.put("PACKING",new Variant(""));
        props.put("PURPOSE",new Variant(""));
        
        //Create a new object for line items
        colLineItems=new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsNRGP=Stmt.executeQuery("SELECT * FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GATEPASS_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' AND COALESCE(GATEPASS_TYPE,'') NOT IN ('FGP') ORDER BY GATEPASS_NO");
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
            rsNRGP.close();
        }
        catch(Exception e) {
            
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsNRGP.first();
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
            if(rsNRGP.isAfterLast()||rsNRGP.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsNRGP.last();
            }
            else {
                rsNRGP.next();
                if(rsNRGP.isAfterLast()) {
                    rsNRGP.last();
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
            if(rsNRGP.isFirst()||rsNRGP.isBeforeFirst()) {
                rsNRGP.first();
            }
            else {
                rsNRGP.previous();
                if(rsNRGP.isBeforeFirst()) {
                    rsNRGP.first();
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
            rsNRGP.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean Insert(String pPrefix,String pDocno) {
        Statement stTmp,stHistory,stHDetail,stHLot;
        ResultSet rsTmp,rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "",Gatepassno = "",Declno = "";
        String RJNNo = "";
        int RJNSrno = 0,GatepassSrno = 0,DeclSrno = 0;
        
        try {
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_NRGP_HEADER WHERE GATEPASS_NO='1'");
            //rsHeader.first();
            
            //=========== Check the Quantities entered against Rejection.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                
                RJNNo=(String) ObjItem.getAttribute("RJN_NO").getObj();
                RJNSrno=(int) ObjItem.getAttribute("RJN_SRNO").getVal();
                double RJNQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!RJNNo.trim().equals(""))&&(RJNSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        RJNQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND RJN_SRNO="+RJNSrno+" AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > RJNQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds RJN No. "+RJNNo+" Sr. No. "+RJNSrno+" qty "+RJNQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            
            //=========== Check the Quantities entered against Gatepass Requisition.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                
                Gatepassno=(String) ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                GatepassSrno=(int) ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                double GatepassQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Gatepassno.trim().equals(""))&&(GatepassSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        GatepassQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASSREQ_NO='"+Gatepassno+"' AND GATEPASSREQ_SRNO="+GatepassSrno+" AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > GatepassQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds GatepassNo. "+Gatepassno+" Sr. No. "+GatepassSrno+" qty "+GatepassQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            //=========== Check the Quantities entered against Declaration.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                
                Declno=(String) ObjItem.getAttribute("DECLARATION_ID").getObj();
                DeclSrno=(int) ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                double DeclQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Declno.trim().equals(""))&&(DeclSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT RECD_QTY FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        DeclQty=rsTmp.getDouble("RECD_QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND DECLARATION_SRNO="+DeclSrno+" AND GATEPASS_NO<>'"+pDocno+"' ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    strSQL="SELECT SUM(CONSUMED_QTY) AS SUMQTY FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DEC_ID='"+Declno+"' AND DEC_SR_NO="+DeclSrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty+=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > DeclQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Decl.No "+Declno+" Sr. No. "+DeclSrno+" recived qty "+DeclQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("GATEPASS_TYPE").getObj();
            boolean LotItem = (boolean) getAttribute("NRGP_WITH_LOT").getBool();
            
            
            // Stock Updataion started over here
            //=======================================//
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsNRGPItem ObjItem=(clsNRGPItem)colLineItems.get(Integer.toString(i));
                    
                    RJNNo=(String)ObjItem.getAttribute("RJN_NO").getObj();
                    RJNSrno=(int)ObjItem.getAttribute("RJN_SRNO").getVal();
                    double Qty=ObjItem.getAttribute("QTY").getVal();
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    Gatepassno=(String)ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                    GatepassSrno=(int)ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                    Declno=(String)ObjItem.getAttribute("DECLARATION_ID").getObj();
                    DeclSrno=(int)ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                    Statement tmpStmt;
                    tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    
                    
                    if((!Gatepassno.trim().equals(""))&&GatepassSrno>0) {
                        strSQL="UPDATE D_INV_GATEPASS_REQ_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_GATEPASS_REQ_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!Declno.trim().equals(""))&&DeclSrno>0) {
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET RETURNED_QTY=(RETURNED_QTY+"+Qty+") WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno; //,BAL_QTY=BAL_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() 
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET BAL_QTY=BAL_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!RJNNo.trim().equals(""))&&RJNSrno>0) {
                        strSQL="UPDATE D_INV_RJN_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_RJN_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"'";
                        data.Execute(strSQL);
                    }
                }// For Loop of Line items is completed
            } // Approval Final Approval If Comndition completed
            
            // Stock Updataion is Completed over here
            //=======================================//
            
            //Generating new Indent No by using Max(Indent_no)+1
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            
            //--------- Generate New MIR No. ------------
            if(UserDocNo) {
                rsTmp=data.getResult("SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE GATEPASS_NO='"+((String)getAttribute("GATEPASS_NO").getObj()).trim()+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    LastError="Document no. already exist. Please specify other document no.";
                    return false;
                }
            }
            else {
                setAttribute("GATEPASS_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,11, (String)getAttribute("PREFIX").getObj(),(String)getAttribute("SUFFIX").getObj(),true));
            }
            //-------------------------------------------------
            
            rsNRGP.moveToInsertRow();
            rsNRGP.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsNRGP.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsNRGP.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsNRGP.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsNRGP.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsNRGP.updateBoolean("NRGP_WITH_LOT",(boolean) getAttribute("NRGP_WITH_LOT").getBool());
            rsNRGP.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsNRGP.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsNRGP.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsNRGP.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsNRGP.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsNRGP.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsNRGP.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsNRGP.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsNRGP.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsNRGP.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsNRGP.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsNRGP.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsNRGP.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsNRGP.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsNRGP.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsNRGP.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsNRGP.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsNRGP.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsNRGP.updateBoolean("CHANGED",true);
            rsNRGP.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsNRGP.updateBoolean("APPROVED",false);
            rsNRGP.updateString("APPROVED_DATE","0000-00-00");
            rsNRGP.updateBoolean("REJECTED",false);
            rsNRGP.updateString("REJECTED_DATE","0000-00-00");
            rsNRGP.updateBoolean("CANCELED",false);
            rsNRGP.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsNRGP.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsNRGP.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());          
            
            rsNRGP.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            
            rsNRGP.updateString("TRANSPORTER_GSTIN",(String)getAttribute("TRANSPORTER_GSTIN").getObj());          
            rsNRGP.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateBoolean("NRGP_WITH_LOT",(boolean) getAttribute("NRGP_WITH_LOT").getBool());
            rsHistory.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsHistory.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsHistory.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            
            rsHistory.updateString("TRANSPORTER_GSTIN",(String)getAttribute("TRANSPORTER_GSTIN").getObj());
            rsHistory.insertRow();
            
            
            // Inserting records in NRGP Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND GATEPASS_NO='1'");
            String nNRGPno=(String) getAttribute("GATEPASS_NO").getObj();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            String Gatepass_type=(String) getAttribute("GATEPASS_TYPE").getObj();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjNRGPItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("GATEPASS_NO",nNRGPno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("WAREHOUSE_ID",(String) ObjNRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsTmp.updateString("LOCATION_ID",(String) ObjNRGPItem.getAttribute("LOCATION_ID").getObj());
                rsTmp.updateString("ITEM_CODE",(String) ObjNRGPItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateLong("UNIT",(long) ObjNRGPItem.getAttribute("UNIT").getVal());
                rsTmp.updateString("NRGP_DESC",(String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                rsTmp.updateDouble("QTY",ObjNRGPItem.getAttribute("QTY").getVal());
                rsTmp.updateLong("FREIGHT",(long) ObjNRGPItem.getAttribute("FREIGHT").getVal());
                rsTmp.updateLong("OCTROI",(long) ObjNRGPItem.getAttribute("OCTROI").getVal());
                rsTmp.updateLong("RATE",(long) ObjNRGPItem.getAttribute("RATE").getVal());
                rsTmp.updateString("RJN_NO",(String) ObjNRGPItem.getAttribute("RJN_NO").getObj());
                rsTmp.updateLong("RJN_SRNO",(long) ObjNRGPItem.getAttribute("RJN_SRNO").getVal());
                rsTmp.updateString("GATEPASSREQ_NO",(String) ObjNRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsTmp.updateLong("GATEPASSREQ_SRNO",(long) ObjNRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsTmp.updateString("DECLARATION_ID",(String) ObjNRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsTmp.updateLong("DECLARATION_SRNO", ObjNRGPItem.getAttribute("DECLARATION_SRNO").getInt());
                rsTmp.updateBoolean("CANCELED",(boolean) ObjNRGPItem.getAttribute("CANCELED").getBool());
                rsTmp.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateString("PACKING",(String)ObjNRGPItem.getAttribute("PACKING").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("GATEPASS_NO",nNRGPno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("WAREHOUSE_ID",(String) ObjNRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String) ObjNRGPItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("ITEM_CODE",(String) ObjNRGPItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjNRGPItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("NRGP_DESC",(String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjNRGPItem.getAttribute("QTY").getVal());
                rsHDetail.updateLong("FREIGHT",(long) ObjNRGPItem.getAttribute("FREIGHT").getVal());
                rsHDetail.updateLong("OCTROI",(long) ObjNRGPItem.getAttribute("OCTROI").getVal());
                rsHDetail.updateLong("RATE",(long) ObjNRGPItem.getAttribute("RATE").getVal());
                rsHDetail.updateString("RJN_NO",(String) ObjNRGPItem.getAttribute("RJN_NO").getObj());
                rsHDetail.updateLong("RJN_SRNO",(long) ObjNRGPItem.getAttribute("RJN_SRNO").getVal());
                rsHDetail.updateString("GATEPASSREQ_NO",(String) ObjNRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsHDetail.updateLong("GATEPASSREQ_SRNO",(long) ObjNRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsHDetail.updateString("DECLARATION_ID",(String) ObjNRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsHDetail.updateLong("DECLARATION_SRNO",(int) ObjNRGPItem.getAttribute("DECLARATION_SRNO").getInt());
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjNRGPItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("PACKING",(String)ObjNRGPItem.getAttribute("PACKING").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                //} // Entries inserting into Line file
                
                //  Inserting records in Items Detail
                ResultSet rsLot;
                Statement tmpLot;
                tmpLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsLot=tmpLot.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND GATEPASS_NO='1'");
                
                if (LotItem) {
                    for(int l=1;l<=ObjNRGPItem.colItemLot.size();l++) {
                        clsNRGPItemDetail ObjNRGPItem_Detail=(clsNRGPItemDetail)  ObjNRGPItem.colItemLot.get(Integer.toString(l));
                        rsLot.moveToInsertRow();
                        rsLot.updateLong("COMPANY_ID",nCompanyID);
                        rsLot.updateString("GATEPASS_NO",nNRGPno);
                        rsLot.updateLong("SR_NO",i);
                        rsLot.updateLong("SRNO",l);
                        rsLot.updateString("LOT_NO",(String) ObjNRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsLot.updateDouble("LOT_QTY",ObjNRGPItem_Detail.getAttribute("LOT_QTY").getVal());
                        rsLot.updateLong("CREATED_BY",(long) ObjNRGPItem_Detail.getAttribute("CREATED_BY").getVal());
                        rsLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                        rsLot.updateLong("MODIFIED_BY",(long) ObjNRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
                        rsLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsLot.updateBoolean("CHANGED",true);
                        rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsLot.insertRow();
                        
                        rsHLot.moveToInsertRow();
                        rsHLot.updateInt("REVISION_NO",1);
                        rsHLot.updateLong("COMPANY_ID",nCompanyID);
                        rsHLot.updateString("GATEPASS_NO",nNRGPno);
                        rsHLot.updateLong("SR_NO",i);
                        rsHLot.updateLong("SRNO",l);
                        rsHLot.updateString("LOT_NO",(String) ObjNRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsHLot.updateDouble("LOT_QTY",ObjNRGPItem_Detail.getAttribute("LOT_QTY").getVal());
                        rsHLot.updateLong("CREATED_BY",(long) ObjNRGPItem_Detail.getAttribute("CREATED_BY").getVal());
                        rsHLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                        rsHLot.updateLong("MODIFIED_BY",(long) ObjNRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
                        rsHLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsHLot.updateBoolean("CHANGED",true);
                        rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsHLot.insertRow();
                    }
                }
            }
            
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=11; //QUOTATION MODULE ID
            ObjFlow.DocNo=(String)getAttribute("GATEPASS_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_NRGP_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="GATEPASS_NO";
            
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
        String strSQL = "",Gatepassno = "",Declno = "";
        String RJNNo = "";
        int RJNSrno = 0,GatepassSrno = 0,DeclSrno = 0;
        
        try {
            
            String theDocNo=(String)getAttribute("GATEPASS_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GATEPASS_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            String GatepassNo=(String)getAttribute("GATEPASS_NO").getObj();
            
            //=========== Check the Quantities entered against Rejection.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                
                RJNNo=(String) ObjItem.getAttribute("RJN_NO").getObj();
                RJNSrno=(int) ObjItem.getAttribute("RJN_SRNO").getVal();
                double RJNQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!RJNNo.trim().equals(""))&&(RJNSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_RJN_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        RJNQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+RJNNo+"' AND RJN_SRNO="+RJNSrno+" AND GATEPASS_NO<>'"+GatepassNo+"' AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > RJNQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds RJN No. "+RJNNo+" Sr. No. "+RJNSrno+" qty "+RJNQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            //=========== Check the Quantities entered against Gatepass Requisition.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                
                Gatepassno=(String) ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                GatepassSrno=(int) ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                double GatepassQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Gatepassno.trim().equals(""))&&(GatepassSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY FROM D_INV_GATEPASS_REQ_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        GatepassQty=rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASSREQ_NO='"+Gatepassno+"' AND GATEPASSREQ_SRNO="+GatepassSrno+" AND GATEPASS_NO<>'"+GatepassNo+"' AND GATEPASS_NO NOT IN(SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > GatepassQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds GatepassNo. "+Gatepassno+" Sr. No. "+GatepassSrno+" qty "+GatepassQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            //=========== Check the Quantities entered against Declaration.============= //
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                
                Declno=(String) ObjItem.getAttribute("DECLARATION_ID").getObj();
                DeclSrno=(int) ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                double DeclQty=0;
                double PrevQty=0; //Previously Entered Qty against RJN
                double CurrentQty=0; //Currently entered Qty.
                
                if((!Declno.trim().equals(""))&&(DeclSrno>0)) //Rejection No. entered
                {
                    //Get the  Rejection Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT RECD_QTY FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        DeclQty=rsTmp.getDouble("RECD_QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+Declno+"' AND DECLARATION_SRNO="+DeclSrno+" AND GATEPASS_NO<>'"+theDocNo+"' ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    strSQL="SELECT SUM(CONSUMED_QTY) AS SUMQTY FROM D_INV_DEC_CONSUME_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DEC_ID='"+Declno+"' AND DEC_SR_NO="+DeclSrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty+=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > DeclQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Decl.No "+Declno+" Sr. No. "+DeclSrno+" recived qty "+DeclQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE GATEPASS_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_DETAIL_H WHERE GATEPASS_NO='1'");
            rsHLot.first();
            //------------------------------------//
            
            //No Primary Keys will be updated & Updating of Header starts
            ApprovalFlow ObjFlow=new ApprovalFlow();
            rsNRGP.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsNRGP.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsNRGP.updateBoolean("NRGP_WITH_LOT",(boolean) getAttribute("NRGP_WITH_LOT").getBool());
            rsNRGP.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsNRGP.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsNRGP.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsNRGP.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsNRGP.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsNRGP.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsNRGP.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsNRGP.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsNRGP.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsNRGP.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsNRGP.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsNRGP.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsNRGP.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsNRGP.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsNRGP.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsNRGP.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsNRGP.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsNRGP.updateBoolean("CHANGED",true);
            rsNRGP.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsNRGP.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsNRGP.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsNRGP.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());           
            
            rsNRGP.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            
            rsNRGP.updateString("TRANSPORTER_GSTIN",(String)getAttribute("TRANSPORTER_GSTIN").getObj());           
            
            rsNRGP.updateBoolean("CANCELED",false);
            rsNRGP.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_NRGP_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+(String)getAttribute("GATEPASS_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("GATEPASS_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GATEPASS_NO",(String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateString("GATEPASS_DATE",(String) getAttribute("GATEPASS_DATE").getObj());
            rsHistory.updateLong("FOR_DEPT",(long) getAttribute("FOR_DEPT").getVal());
            rsHistory.updateString("GATEPASS_TYPE",(String) getAttribute("GATEPASS_TYPE").getObj());
            rsHistory.updateBoolean("NRGP_WITH_LOT",(boolean) getAttribute("NRGP_WITH_LOT").getBool());
            rsHistory.updateString("SUPP_ID",(String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateLong("MODE_TRANSPORT",(long) getAttribute("MODE_TRANSPORT").getVal());
            rsHistory.updateLong("TRANSPORTER",(long) getAttribute("TRANSPORTER").getVal());
            rsHistory.updateLong("TOTAL_AMOUNT",(long) getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("USER_ID",(int)getAttribute("USER_ID").getVal());
            rsHistory.updateString("PARTY_NAME",(String)getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("EXP_RETURN_DATE",(String)getAttribute("EXP_RETURN_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("PACKING",(String)getAttribute("PACKING").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateString("DESPATCH_MODE",(String)getAttribute("DESPATCH_MODE").getObj());
            rsHistory.updateString("GROSS_WEIGHT",(String)getAttribute("GROSS_WEIGHT").getObj());
            
            rsHistory.updateString("TRANSPORTER_GSTIN",(String)getAttribute("TRANSPORTER_GSTIN").getObj());
            rsHistory.insertRow();
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("GATEPASS_TYPE").getObj();
            boolean LotItem = (boolean) getAttribute("NRGP_WITH_LOT").getBool();
            
            // Stock Updataion started over here
            //=======================================//
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsNRGPItem ObjItem=(clsNRGPItem)colLineItems.get(Integer.toString(i));
                    
                    RJNNo=(String)ObjItem.getAttribute("RJN_NO").getObj();
                    RJNSrno=(int)ObjItem.getAttribute("RJN_SRNO").getVal();
                    double Qty=ObjItem.getAttribute("QTY").getVal();
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    Gatepassno=(String)ObjItem.getAttribute("GATEPASSREQ_NO").getObj();
                    GatepassSrno=(int)ObjItem.getAttribute("GATEPASSREQ_SRNO").getVal();
                    Declno=(String)ObjItem.getAttribute("DECLARATION_ID").getObj();
                    DeclSrno=(int)ObjItem.getAttribute("DECLARATION_SRNO").getVal();
                    Statement tmpStmt;
                    tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    
                    
                    if((!Gatepassno.trim().equals(""))&&GatepassSrno>0) {
                        strSQL="UPDATE D_INV_GATEPASS_REQ_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"' AND SR_NO="+GatepassSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_GATEPASS_REQ_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_REQ_NO='"+Gatepassno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!Declno.trim().equals(""))&&DeclSrno>0) {
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET RETURNED_QTY=(RETURNED_QTY+"+Qty+") WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno; //,BAL_QTY=BAL_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() 
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_DETAIL SET BAL_QTY=BAL_QTY-RETURNED_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"' AND SR_NO="+DeclSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_DECLARATION_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+Declno+"'";
                        data.Execute(strSQL);
                    }
                    
                    if((!RJNNo.trim().equals(""))&&RJNSrno>0) {
                        strSQL="UPDATE D_INV_RJN_DETAIL SET GATEPASS_QTY=GATEPASS_QTY+"+Qty+",BAL_QTY=QTY-GATEPASS_QTY,CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"' AND SR_NO="+RJNSrno;
                        data.Execute(strSQL);
                        strSQL="UPDATE D_INV_RJN_HEADER SET CHANGED=1,CHANGED_DATE=SYSDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND RJN_NO='"+RJNNo+"'";
                        data.Execute(strSQL);
                    }
                }// For Loop of Line items is completed
            } // Approval Final Approval If Comndition completed
            
            // Stock Updataion is Completed over here
            //=======================================//
            
            // Inserting records in NRGP Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String nNRGPno=(String) getAttribute("GATEPASS_NO").getObj();
            
            String Del_H = "DELETE FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+nNRGPno.trim()+"'";
            data.Execute(Del_H);
            String Del_L = "DELETE FROM D_INV_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+nNRGPno.trim()+"'";
            data.Execute(Del_L);
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='1'");
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            String Gatepass_type=(String) getAttribute("GATEPASS_TYPE").getObj();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsNRGPItem ObjNRGPItem=(clsNRGPItem) colLineItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("GATEPASS_NO",nNRGPno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("WAREHOUSE_ID",(String) ObjNRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsTmp.updateString("LOCATION_ID",(String) ObjNRGPItem.getAttribute("LOCATION_ID").getObj());
                rsTmp.updateString("ITEM_CODE",(String) ObjNRGPItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateLong("UNIT",(long) ObjNRGPItem.getAttribute("UNIT").getVal());
                rsTmp.updateString("NRGP_DESC",(String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                rsTmp.updateDouble("QTY",ObjNRGPItem.getAttribute("QTY").getVal());
                rsTmp.updateLong("FREIGHT",(long) ObjNRGPItem.getAttribute("FREIGHT").getVal());
                rsTmp.updateLong("OCTROI",(long) ObjNRGPItem.getAttribute("OCTROI").getVal());
                rsTmp.updateLong("RATE",(long) ObjNRGPItem.getAttribute("RATE").getVal());
                rsTmp.updateString("RJN_NO",(String) ObjNRGPItem.getAttribute("RJN_NO").getObj());
                rsTmp.updateLong("RJN_SRNO",(long) ObjNRGPItem.getAttribute("RJN_SRNO").getVal());
                rsTmp.updateString("GATEPASSREQ_NO",(String) ObjNRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsTmp.updateLong("GATEPASSREQ_SRNO",(long) ObjNRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsTmp.updateString("DECLARATION_ID",(String) ObjNRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsTmp.updateLong("DECLARATION_SRNO", ObjNRGPItem.getAttribute("DECLARATION_SRNO").getInt());
                rsTmp.updateBoolean("CANCELED",(boolean) ObjNRGPItem.getAttribute("CANCELED").getBool());
                rsTmp.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateString("PACKING",(String)ObjNRGPItem.getAttribute("PACKING").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("GATEPASS_NO",nNRGPno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("WAREHOUSE_ID",(String) ObjNRGPItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID",(String) ObjNRGPItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("ITEM_CODE",(String) ObjNRGPItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjNRGPItem.getAttribute("UNIT").getVal());
                rsHDetail.updateString("NRGP_DESC",(String) ObjNRGPItem.getAttribute("NRGP_DESC").getObj());
                rsHDetail.updateDouble("QTY",ObjNRGPItem.getAttribute("QTY").getVal());
                rsHDetail.updateLong("FREIGHT",(long) ObjNRGPItem.getAttribute("FREIGHT").getVal());
                rsHDetail.updateLong("OCTROI",(long) ObjNRGPItem.getAttribute("OCTROI").getVal());
                rsHDetail.updateLong("RATE",(long) ObjNRGPItem.getAttribute("RATE").getVal());
                rsHDetail.updateString("RJN_NO",(String) ObjNRGPItem.getAttribute("RJN_NO").getObj());
                rsHDetail.updateLong("RJN_SRNO",(long) ObjNRGPItem.getAttribute("RJN_SRNO").getVal());
                rsHDetail.updateString("GATEPASSREQ_NO",(String) ObjNRGPItem.getAttribute("GATEPASSREQ_NO").getObj());
                rsHDetail.updateLong("GATEPASSREQ_SRNO",(long) ObjNRGPItem.getAttribute("GATEPASSREQ_SRNO").getVal());
                rsHDetail.updateString("DECLARATION_ID",(String) ObjNRGPItem.getAttribute("DECLARATION_ID").getObj());
                rsHDetail.updateLong("DECLARATION_SRNO", ObjNRGPItem.getAttribute("DECLARATION_SRNO").getInt());
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjNRGPItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateString("REMARKS",(String) ObjNRGPItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("PACKING",(String)ObjNRGPItem.getAttribute("PACKING").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                //  Inserting records in Items Detail
                ResultSet rsLot;
                Statement tmpLot;
                tmpLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsLot=tmpLot.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='1'");
                
                if (LotItem) {
                    for(int l=1;l<=ObjNRGPItem.colItemLot.size();l++) {
                        clsNRGPItemDetail ObjNRGPItem_Detail=(clsNRGPItemDetail)  ObjNRGPItem.colItemLot.get(Integer.toString(l));
                        rsLot.moveToInsertRow();
                        rsLot.updateLong("COMPANY_ID",nCompanyID);
                        rsLot.updateString("GATEPASS_NO",nNRGPno);
                        rsLot.updateLong("SR_NO",i);
                        rsLot.updateLong("SRNO",l);
                        rsLot.updateString("LOT_NO",(String) ObjNRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsLot.updateDouble("LOT_QTY",ObjNRGPItem_Detail.getAttribute("LOT_QTY").getVal());
                        rsLot.updateLong("CREATED_BY",(long) ObjNRGPItem_Detail.getAttribute("CREATED_BY").getVal());
                        rsLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                        rsLot.updateLong("MODIFIED_BY",(long) ObjNRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
                        rsLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsLot.updateBoolean("CHANGED",true);
                        rsLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsLot.insertRow();
                        
                        rsHLot.moveToInsertRow();
                        rsHLot.updateInt("REVISION_NO",RevNo);
                        rsHLot.updateLong("COMPANY_ID",nCompanyID);
                        rsHLot.updateString("GATEPASS_NO",nNRGPno);
                        rsHLot.updateLong("SR_NO",i);
                        rsHLot.updateLong("SRNO",l);
                        rsHLot.updateString("LOT_NO",(String) ObjNRGPItem_Detail.getAttribute("LOT_NO").getObj());
                        rsHLot.updateDouble("LOT_QTY",ObjNRGPItem_Detail.getAttribute("LOT_QTY").getVal());
                        rsHLot.updateLong("CREATED_BY",(long) ObjNRGPItem_Detail.getAttribute("CREATED_BY").getVal());
                        rsHLot.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                        rsHLot.updateLong("MODIFIED_BY",(long) ObjNRGPItem_Detail.getAttribute("MODIFIED_BY").getVal());
                        rsHLot.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                        rsHLot.updateBoolean("CHANGED",true);
                        rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsHLot.insertRow();
                    }
                }
            }
            
            
            
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=11; //QUOTATION MODULE ID
            ObjFlow.DocNo=(String)getAttribute("GATEPASS_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_NRGP_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="GATEPASS_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_NRGP_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=11 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            // updataion process is over
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
    public boolean CanDelete(int pCompanyID,String pGatepassno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_NO='"+pGatepassno+"' AND APPROVED=true";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=1 AND DOC_NO='"+pGatepassno+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    public boolean IsEditable(int pCompanyID,String pGatepassno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
                
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND GATEPASS_NO='"+pGatepassno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=11 AND DOC_NO='"+pGatepassno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
        try {
            int lCompanyID=(int) getAttribute("COMPANY_ID").getVal();
            String lGatepassno=(String) getAttribute("GATEPASS_NO").getObj();
            
            if(CanDelete(lCompanyID,lGatepassno,pUserID)) {
                String strQry = "DELETE FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
                data.Execute(strQry);
                
                strQry = "DELETE FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
                data.Execute(strQry);
                
                strQry = "DELETE FROM D_INV_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+lGatepassno.trim()+"'";
                data.Execute(strQry);
            }
            
            rsNRGP.refreshRow();
            return MoveLast();
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID,String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID)+" AND GATEPASS_NO='"+pDocNo+"'";
        clsNRGP ObjNRGP = new clsNRGP();
        ObjNRGP.Filter(strCondition,pCompanyID);
        return ObjNRGP;
    }
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_NRGP_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsNRGP=Stmt.executeQuery(strSql);
            
            if(!rsNRGP.first()) {
//                strSql = "SELECT * FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND GATEPASS_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND GATEPASS_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY GATEPASS_NO";
//                rsNRGP=Stmt.executeQuery(strSql);
//                Ready=true;
//                MoveLast();
                JOptionPane.showMessageDialog(null, "No Record found.");
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
    
    
    public HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp1;
        Statement tmpStmt1;
        
        ResultSet rsTmp2;
        Statement tmpStmt2;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=Conn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_HEADER"+pCondition);
            
            Counter=0;
            while(! rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsNRGP ObjNRGP=new clsNRGP();
                
                //Populate the user
                ObjNRGP.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjNRGP.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjNRGP.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
                ObjNRGP.setAttribute("GATEPASS_TYPE",rsTmp.getString("GATEPASS_TYPE"));
                ObjNRGP.setAttribute("FOR_DEPT",rsTmp.getLong("FOR_DEPT"));
                ObjNRGP.setAttribute("NRGP_WITH_LOT",rsTmp.getBoolean("NRGP_WITH_LOT"));
                ObjNRGP.setAttribute("MODE_TRANSPORT",rsTmp.getLong("MODE_TRANSPORT"));
                ObjNRGP.setAttribute("TRANSPORTER",rsTmp.getLong("TRANSPORTER"));
                ObjNRGP.setAttribute("SUPP_ID",rsTmp.getString("SUPP_ID"));
                ObjNRGP.setAttribute("TOTAL_AMOUNT",rsTmp.getLong("TOTAL_AMOUNT"));
                ObjNRGP.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjNRGP.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjNRGP.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjNRGP.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                ObjNRGP.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjNRGP.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjNRGP.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjNRGP.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjNRGP.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjNRGP.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                //Now Populate the collection
                //first clear the collection
                ObjNRGP.colLineItems.clear();
                
                String mCompanyID=Long.toString( (long) ObjNRGP.getAttribute("COMPANY_ID").getVal());
                String mGatepassno=(String) ObjNRGP.getAttribute("GATEPASS_NO").getObj();
                
                tmpStmt2=Conn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_NRGP_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mGatepassno.trim()+"'");
                
                Counter=0;
                clsNRGPItem ObjNRGPItem1=new clsNRGPItem();
                ObjNRGPItem1.colItemLot.clear();
                
                while(! rsTmp2.isAfterLast()) {
                    Counter=Counter+1;
                    clsNRGPItem ObjNRGPItem= new clsNRGPItem();
                    
                    ObjNRGPItem.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjNRGPItem.setAttribute("GATEPASS_NO",rsTmp2.getString("GATEPASS_NO"));
                    ObjNRGPItem.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                    ObjNRGPItem.setAttribute("WAREHOUSE_ID",rsTmp2.getString("WAREHOUSE_ID"));
                    ObjNRGPItem.setAttribute("LOCATION_ID",rsTmp2.getLong("LOCATION_ID"));
                    ObjNRGPItem.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                    ObjNRGPItem.setAttribute("NRGP_DESC",rsTmp2.getLong("NRGP_DESC"));
                    ObjNRGPItem.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                    ObjNRGPItem.setAttribute("QTY",rsTmp2.getLong("QTY"));
                    ObjNRGPItem.setAttribute("RJN_NO",rsTmp2.getString("RJN_NO"));
                    ObjNRGPItem.setAttribute("RJN_SRNO",rsTmp2.getLong("RJN_SRNO"));
                    ObjNRGPItem.setAttribute("GATEPASSREQ_NO",rsTmp2.getString("GATEPASSREQ_NO"));
                    ObjNRGPItem.setAttribute("GATEPASSREQ_SRNO",rsTmp2.getLong("GATEPASSREQ_SRNO"));
                    ObjNRGPItem.setAttribute("DECLARATAION_ID",rsTmp2.getString("DECLARATION_ID"));
                    ObjNRGPItem.setAttribute("DECLARATION_SRNO",rsTmp2.getLong("DECLARATION_SRNO"));
                    ObjNRGPItem.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
                    ObjNRGPItem.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                    ObjNRGPItem.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjNRGPItem.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjNRGPItem.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjNRGPItem.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    // Lot Items starting from here
                    tmpStmt1=Conn.createStatement();
                    rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND SR_NO="+rsTmp2.getInt("SR_NO")+"");
                    
                    int Counter1 = 0;
                    while(! rsTmp1.isAfterLast()) {
                        Counter1=Counter1+1;
                        clsNRGPItemDetail ObjNRGPItem_Detail=new clsNRGPItemDetail();
                        
                        ObjNRGPItem_Detail.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                        ObjNRGPItem_Detail.setAttribute("GATEPASS_NO",rsTmp1.getString("GATEPASS_NO"));
                        ObjNRGPItem_Detail.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                        ObjNRGPItem_Detail.setAttribute("SRNO",rsTmp1.getLong("SRNO"));
                        ObjNRGPItem_Detail.setAttribute("LOT_NO",rsTmp1.getString("LOT_NO"));
                        ObjNRGPItem_Detail.setAttribute("LOT_QTY",rsTmp1.getLong("LOT_QTY"));
                        ObjNRGPItem_Detail.setAttribute("CANCELED",rsTmp1.getInt("CANCELED"));
                        ObjNRGPItem_Detail.setAttribute("CREATED_BY",rsTmp1.getLong("CREATED_BY"));
                        ObjNRGPItem_Detail.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                        ObjNRGPItem_Detail.setAttribute("MODIFIED_BY",rsTmp1.getLong("MODIFIED_BY"));
                        ObjNRGPItem_Detail.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                        
                        ObjNRGPItem.colItemLot.put(Long.toString(Counter1),ObjNRGPItem_Detail);
                        rsTmp1.next();
                    }
                    ObjNRGP.colLineItems.put(Long.toString(Counter),ObjNRGPItem);
                    rsTmp2.next();
                }// Innser while
                //Put the prepared user object into list
                rsTmp.next();
                List.put(Long.toString(Counter),ObjNRGP);
            }//Out While
        }
        catch(Exception e) {
        }
        
        return List;
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
                RevNo=rsNRGP.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsNRGP.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",rsNRGP.getLong("COMPANY_ID"));
            setAttribute("GATEPASS_NO",rsNRGP.getString("GATEPASS_NO"));
            setAttribute("GATEPASS_DATE",rsNRGP.getString("GATEPASS_DATE"));
            setAttribute("GATEPASS_TYPE",rsNRGP.getString("GATEPASS_TYPE"));
            setAttribute("FOR_DEPT",rsNRGP.getLong("FOR_DEPT"));
            setAttribute("NRGP_WITH_LOT",rsNRGP.getBoolean("NRGP_WITH_LOT"));
            setAttribute("MODE_TRANSPORT",rsNRGP.getLong("MODE_TRANSPORT"));
            setAttribute("TRANSPORTER",rsNRGP.getLong("TRANSPORTER"));
            setAttribute("SUPP_ID",rsNRGP.getString("SUPP_ID"));
            setAttribute("TOTAL_AMOUNT",rsNRGP.getLong("TOTAL_AMOUNT"));
            setAttribute("APPROVED",rsNRGP.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsNRGP.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsNRGP.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsNRGP.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsNRGP.getString("REJECTED_REMARKS"));
            setAttribute("REMARKS",rsNRGP.getString("REMARKS"));
            setAttribute("CANCELED",rsNRGP.getInt("CANCELED"));
            setAttribute("CREATED_BY",rsNRGP.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsNRGP.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsNRGP.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsNRGP.getString("MODIFIED_DATE"));
            setAttribute("USER_ID",rsNRGP.getInt("USER_ID"));
            setAttribute("PARTY_NAME",rsNRGP.getString("PARTY_NAME"));
            setAttribute("ADD1",rsNRGP.getString("ADD1"));
            setAttribute("ADD2",rsNRGP.getString("ADD2"));
            setAttribute("ADD3",rsNRGP.getString("ADD3"));
            setAttribute("CITY",rsNRGP.getString("CITY"));
            setAttribute("EXP_RETURN_DATE",rsNRGP.getString("EXP_RETURN_DATE"));
            setAttribute("HIERARCHY_ID",rsNRGP.getInt("HIERARCHY_ID"));
            setAttribute("PACKING",rsNRGP.getString("PACKING"));
            setAttribute("PURPOSE",rsNRGP.getString("PURPOSE"));
            setAttribute("DESPATCH_MODE",rsNRGP.getString("DESPATCH_MODE"));
            setAttribute("GROSS_WEIGHT",rsNRGP.getString("GROSS_WEIGHT"));
            
            setAttribute("TRANSPORTER_GSTIN",rsNRGP.getString("TRANSPORTER_GSTIN"));
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            clsNRGPItem ObjNRGPItem1 = new clsNRGPItem();
            ObjNRGPItem1.colItemLot.clear();
            
            String mCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mGatepassno=(String) getAttribute("GATEPASS_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' ORDER BY SR_NO");
            }
            Counter=0;
            rsTmp.first();
            while(! rsTmp.isAfterLast()) {
                Counter=Counter+1;
                clsNRGPItem ObjNRGPItem=new clsNRGPItem();
                
                ObjNRGPItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjNRGPItem.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjNRGPItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjNRGPItem.setAttribute("WAREHOUSE_ID",rsTmp.getString("WAREHOUSE_ID"));
                ObjNRGPItem.setAttribute("LOCATION_ID",rsTmp.getString("LOCATION_ID"));
                ObjNRGPItem.setAttribute("ITEM_CODE",rsTmp.getString("ITEM_CODE"));
                ObjNRGPItem.setAttribute("NRGP_DESC",rsTmp.getString("NRGP_DESC"));
                ObjNRGPItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                ObjNRGPItem.setAttribute("QTY",rsTmp.getDouble("QTY"));
                ObjNRGPItem.setAttribute("FREIGHT",rsTmp.getDouble("FREIGHT"));
                ObjNRGPItem.setAttribute("OCTROI",rsTmp.getDouble("OCTROI"));
                ObjNRGPItem.setAttribute("RATE",rsTmp.getDouble("RATE"));
                ObjNRGPItem.setAttribute("RJN_NO",rsTmp.getString("RJN_NO"));
                ObjNRGPItem.setAttribute("RJN_SRNO",rsTmp.getInt("RJN_SRNO"));
                ObjNRGPItem.setAttribute("GATEPASSREQ_NO",rsTmp.getString("GATEPASSREQ_NO"));
                ObjNRGPItem.setAttribute("GATEPASSREQ_SRNO",rsTmp.getInt("GATEPASSREQ_SRNO"));
                ObjNRGPItem.setAttribute("DECLARATION_ID",rsTmp.getString("DECLARATION_ID"));
                ObjNRGPItem.setAttribute("DECLARATION_SRNO",rsTmp.getInt("DECLARATION_SRNO"));
                ObjNRGPItem.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjNRGPItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjNRGPItem.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjNRGPItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjNRGPItem.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjNRGPItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjNRGPItem.setAttribute("PACKING",rsTmp.getString("PACKING"));
                
                ResultSet rsTmp1;
                tmpStmt=tmpConn.createStatement();
                if(HistoryView) {
                    rsTmp1=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND SR_NO="+rsTmp.getInt("SR_NO")+" AND REVISION_NO="+RevNo);
                }
                else {
                    rsTmp1=tmpStmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND GATEPASS_NO='"+mGatepassno.trim()+"' AND SR_NO="+rsTmp.getInt("SR_NO")+"");
                }
                rsTmp1.first();
                
                int Counter1=0;
                while(!rsTmp1.isAfterLast()&&rsTmp1.getRow()>0) {
                    Counter1=Counter1+1;
                    clsNRGPItemDetail ObjNRGPItem_Detail =new clsNRGPItemDetail();
                    
                    ObjNRGPItem_Detail.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                    ObjNRGPItem_Detail.setAttribute("GATEPASS_NO",rsTmp1.getString("GATEPASS_NO"));
                    ObjNRGPItem_Detail.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                    ObjNRGPItem_Detail.setAttribute("SRNO",rsTmp1.getLong("SRNO"));
                    ObjNRGPItem_Detail.setAttribute("LOT_NO",rsTmp1.getString("LOT_NO"));
                    ObjNRGPItem_Detail.setAttribute("LOT_QTY",rsTmp1.getDouble("LOT_QTY"));
                    ObjNRGPItem_Detail.setAttribute("CANCELED",rsTmp1.getInt("CANCELED"));
                    ObjNRGPItem_Detail.setAttribute("CREATED_BY",rsTmp1.getLong("CREATED_BY"));
                    ObjNRGPItem_Detail.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                    ObjNRGPItem_Detail.setAttribute("MODIFIED_BY",rsTmp1.getLong("MODIFIED_BY"));
                    ObjNRGPItem_Detail.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                    
                    ObjNRGPItem.colItemLot.put(Long.toString(Counter1),ObjNRGPItem_Detail);
                    rsTmp1.next();
                }
                colLineItems.put(Long.toString(Counter),ObjNRGPItem);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    // updating records related to Line & Line Line Items
    public boolean rollback() {
        try {
            Statement Stmt;
            ResultSet rsItem;
            ResultSet rsItemDetail;
            
            // Assigning Variables Header Fields
            String nGatepasstype = (String) getAttribute("GATEPASS_TYPE").getObj();
            boolean LotItem=(boolean) getAttribute("WITH_LOT_ITEM").getBool();
            String mGatepassno = (String) getAttribute("GATEPASS_NO").getObj();
            
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItem=Stmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
            mGatepassno.trim()+"' ORDER BY SR_NO");
            
            if(rsItem.isAfterLast() || rsItem.isBeforeFirst()) {
                rsItem.first();
            }
            while(! rsItem.isAfterLast()) {
                // Gatepass type is General or Gatepass requistion
                if(nGatepasstype.equals("GEN") || nGatepasstype.equals("GPR")) {
                    ResultSet rsOther;
                    String Upd_Item = "UPDATE D_INV_ITEM_MASTER SET ON_HAND_QTY= ON_HAND_QTY + "+
                    rsItem.getLong("QTY")+", TOTAL_ISSUE_QTY= TOTAL_ISSUE_QTY - "+
                    rsItem.getLong("QTY")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+
                    rsItem.getString("ITEM_CODE")+"'";
                    Stmt.executeUpdate(Upd_Item);
                }
                
                // Gatepass type is Rejection Memo
                if(nGatepasstype.equals("RJN")) {
                    ResultSet rsRJN;
                    //updating Rejection form
                    String Upd_Rejn = "UPDATE D_INV_REJECTION_DETAIL SET BAL_QTY=BAL_QTY - "+
                    rsItem.getLong("QTY")+",RETURNED_QTY=RETURNED_QTY - "+
                    rsItem.getLong("QTY")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND RJN_NO='"+
                    rsItem.getString("RJN_NO")+"' AND SR_NO="+rsItem.getLong("SR_NO")+" AND ITEM_CODE="+
                    rsItem.getString("ITEM_CODE")+"'";
                    Stmt.executeUpdate(Upd_Rejn);
                }
                
                //Gatepass type is Declaration Form
                if(nGatepasstype.equals("DFO")) {
                    
                    ResultSet rsDFO;
                    //updating Declaration form
                    String Upd_Decl = "UPDATE D_INV_DECLARATION_DETAIL SET BAL_QTY= BAL_QTY - "+
                    rsItem.getLong("QTY")+",RETURNED_QTY= RETURNED_QTY - "+
                    rsItem.getLong("QTY")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='"+
                    rsItem.getString("DECLARATION_ID")+"' AND SR_NO="+rsItem.getLong("SR_NO")+" AND ITEM_CODE="+
                    rsItem.getString("ITEM_CODE")+"'";
                    Stmt.executeUpdate(Upd_Decl);
                }
                
                rsItem.next();
            }
            // While Loop is completed
            
            // Lot Items Enties Roll Backed from here
            if(LotItem) {
                rsItemDetail=Stmt.executeQuery("SELECT * FROM D_INV_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
                mGatepassno.trim()+"' ORDER BY SR_NO,SRNO");
                
                if(rsItemDetail.isAfterLast() || rsItemDetail.isBeforeFirst()) {
                    rsItemDetail.first();
                }
                while(! rsItemDetail.isAfterLast()) {
                    ResultSet rsItemLot;
                    if(nGatepasstype.equals("GPR") || nGatepasstype.equals("GEN")) {
                        String Upd_ItemLot = "UPDATE D_INV_ITEM_LOT_MASTER SET TOTAL_ISSUED_QTY= TOTAL_ISSUED_QTY - " +
                        rsItemDetail.getLong("LOT_QTY")+", ON_HAND_QTY= ON_HAND_QTY + " +
                        rsItemDetail.getLong("LOT_QTY")+",LAST_ISSUED_DATE=" +
                        rsItemDetail.getString("MODIFIED_DATE")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+
                        Long.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+ rsItemDetail.getString("ITEM_CODE")+"' AND LOT_NO='" +
                        rsItemDetail.getString("LOT_NO")+"'";
                        Stmt.executeUpdate(Upd_ItemLot);
                    }
                    
                    //Updating Declaration Form Item Lot tables
                    if(nGatepasstype.equals("DFO")) {
                        ResultSet rsDFOLot;
                        
                        String Upd_DeclLot = "UPDATE D_INV_DECLARATAION_DETAIL_DETAIL SET TOTAL_ISSUED_QTY = TOTAL_ISSUED_QTY - " +
                        rsItemDetail.getLong("LOT_QTY")+", BAL_QTY = BAL_QTY +" + rsItemDetail.getLong("BAL_QTY") + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+
                        Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='" + rsItemDetail.getString("DECLARATION_ID")+"' AND SR_NO=" +
                        rsItemDetail.getLong("SR_NO")+" AND ITEM_CODE='"+ rsItemDetail.getString("ITEM_CODE")+"'";
                        Stmt.executeUpdate(Upd_DeclLot);
                    }
                    
                    //Updating Rejection Form Item Lot tables
                    if(nGatepasstype.equals("RJN")) {
                        ResultSet rsRJNLot;
                        
                        String Upd_RejnLot = "UPDATE D_INV_REJECTION_DETAIL_DETAIL SET TOTAL_ISSUED_QTY = TOTAL_ISSUED_QTY - " +
                        rsItemDetail.getLong("LOT_QTY")+", BAL_QTY = BAL_QTY +" + rsItemDetail.getLong("BAL_QTY") + ",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+
                        Long.toString(EITLERPGLOBAL.gCompanyID)+" AND DECLARATION_ID='" + rsItemDetail.getString("DECLARATION_ID")+"' AND SR_NO=" +
                        rsItemDetail.getLong("SR_NO")+" AND ITEM_CODE='"+ rsItemDetail.getString("ITEM_CODE")+"'";
                        Stmt.executeUpdate(Upd_RejnLot);
                    }
                    
                    rsItemDetail.next();
                } // While Ends for Line items
            }  // LotItem Condition is completed over here
            
            rsItem=Stmt.executeQuery("DELETE FROM D_INV_NRGP_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
            mGatepassno.trim()+"'");
            rsItemDetail=Stmt.executeQuery("DELETE FROM D_INV_NRGP_DETAIL_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND GATEPASS_NO='"+
            mGatepassno.trim()+"'");
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public static HashMap getPendingApprovals(int pCompanyID,int pUserID,int pOrder,int FinYearFrom) {
        String strSQL="";
        Connection tmpConn;
        tmpConn=data.getConn();
        ResultSet rsTmp;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE,RECEIVED_DATE,D_INV_NRGP_HEADER.FOR_DEPT AS DEPT_ID FROM D_INV_NRGP_HEADER,D_COM_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=11 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE,RECEIVED_DATE,D_INV_NRGP_HEADER.FOR_DEPT AS DEPT_ID FROM D_INV_NRGP_HEADER,D_COM_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=11 ORDER BY D_INV_NRGP_HEADER.GATEPASS_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE,RECEIVED_DATE,D_INV_NRGP_HEADER.FOR_DEPT AS DEPT_ID FROM D_INV_NRGP_HEADER,D_COM_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=11 ORDER BY D_INV_NRGP_HEADER.GATEPASS_NO";
            }
            
            //strSQL="SELECT D_INV_NRGP_HEADER.GATEPASS_NO,D_INV_NRGP_HEADER.GATEPASS_DATE FROM D_INV_NRGP_HEADER,D_COM_DOC_DATA WHERE D_INV_NRGP_HEADER.GATEPASS_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_NRGP_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_NRGP_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=11";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("GATEPASS_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsNRGP ObjDoc=new clsNRGP();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjDoc.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjDoc);
                }
                
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
            rsNRGP=Stmt.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_NRGP_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsNRGP ObjNRGP=new clsNRGP();
                
                ObjNRGP.setAttribute("GATEPASS_NO",rsTmp.getString("GATEPASS_NO"));
                ObjNRGP.setAttribute("GATEPASS_DATE",rsTmp.getString("GATEPASS_DATE"));
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
            rsTmp=data.getResult("SELECT GATEPASS_NO,APPROVED,CANCELED FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELED"))
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
 
    
 public static boolean IsNRGPExist(int pCompanyID,String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
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
            rsTmp=data.getResult("SELECT GATEPASS_NO FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"' AND CANCELED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canCancel=true;
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
            
        }
        
        return canCancel;
        
    }
    
    
    public static boolean CancelNRGP(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            Connection tmpConn;
            Statement stSTM;
            
            if(CanCancel(pCompanyID,pDocNo)) {
                boolean ApprovedDoc=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_NRGP_HEADER WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedDoc=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedDoc) {
                        
                    
                }
                else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID=11");
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_NRGP_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND GATEPASS_NO='"+pDocNo+"'");
                
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