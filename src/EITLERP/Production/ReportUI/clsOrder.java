/*
 * clsIndent.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Production.ReportUI;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.*;
import EITLERP.Production.*;
import java.io.*;

/**
 *
 * @author  nhpatel
 * @version
 */

public class clsOrder{
  
    public String LastError="";
    private ResultSet rsOrder;
    private Connection Conn;
    private Statement Stmt;
    
    //Indent Line Items Collection
    public HashMap colLineItems=new HashMap();
    public HashMap colItemDetail=new HashMap();
    
    private HashMap props;
    public boolean Ready = false;
    
    //History Related properties
    private boolean HistoryView=false;
    
    private int LastPosition=0;
    
    
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
    
    
    /** Creates new clsOrder */
    public clsOrder() {
        props=new HashMap();
        props.put("PIECE_NO",new Variant(""));
        props.put("ORDER_DATE",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));
        props.put("PARTY_CD",new Variant(""));
       /* props.put("LNGTH",new Variant(0));
        props.put("WIDTH",new Variant(0));
        props.put("GSQ",new Variant(""));
        props.put("DELIV_DATE",new Variant(0));
        props.put("COMM_DATE",new Variant(false));
        props.put("ORDER_DATE",new Variant(0));
        props.put("GRUP",new Variant(false));
        props.put("INV_NO",new Variant(""));
        props.put("INV_DATE",new Variant(""));
        props.put("PROD_IND",new Variant(""));
        props.put("PROD_IND_A",new Variant(false));
        props.put("ORDER_DDMM_A",new Variant(0));
        props.put("PROD_IND_B",new Variant(""));
        props.put("ORDER_DDMM_B",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        
        props.put("REFRESH_FILE",new Variant(false));
        props.put("FILENAME",new Variant(""));
        props.put("ATTACHEMENT",new Variant(false));
        props.put("ATTACHEMENT_PATH",new Variant(""));
        props.put("DOC_ID",new Variant(0));
        
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));*/
        //Create a new object for rights collection
        colLineItems=new HashMap();
    }
    
    public boolean LoadData(String PartyCode,String PieceNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
           String strSQL ="SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD="+PartyCode+" AND PIECE_NO="+PieceNo+"";  
            rsOrder=Stmt.executeQuery(strSQL);
            
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
    
   /*  public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            HistoryView=false;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_COM_EMPLOYEE_MASTER WHERE COMPANY_ID="+Long.toString(pCompanyID));
            Ready=true;
            MoveLast();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }*/
    
    
    
    
    
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsOrder.close();
            
        }
        catch(Exception e) {
            
        }
        
        
    }
    
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsOrder.first();
            setData();
            LastPosition=rsOrder.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsOrder.isAfterLast()||rsOrder.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsOrder.last();
            }
            else {
                rsOrder.next();
                if(rsOrder.isAfterLast()) {
                    rsOrder.last();
                }
            }
            setData();
            LastPosition=rsOrder.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsOrder.isFirst()||rsOrder.isBeforeFirst()) {
                rsOrder.first();
            }
            else {
                rsOrder.previous();
                if(rsOrder.isBeforeFirst()) {
                    rsOrder.first();
                }
            }
            setData();
            LastPosition=rsOrder.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsOrder.last();
            setData();
            LastPosition=rsOrder.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    /*public boolean Insert(String pPrefix,String pDocno) {
     
        
    }*/
    
    
    //Updates current record
 /*   public boolean Update() {
   
      
    }*/
    
    
    
   /* public boolean Amend() {
     /*   Statement stHistory,stHDetail,stTmp,stItemDetail,stHItemDetail;
        ResultSet rsHistory,rsHDetail,rsTmp,rsItemDetail,rsHItemDetail;
        Statement stHeader;
        ResultSet rsHeader;
        String MRNo="",strSQL="";
        int MRSrNo=0;
        
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_INDENT_HEADER_H WHERE INDENT_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL_H WHERE INDENT_NO='1'");
            rsHDetail.first();
            
            stHItemDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stItemDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsItemDetail=stItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL WHERE INDENT_NO='1'"); // '1' for restricting all data retrieval
            rsItemDetail.first();
            rsHItemDetail=stHItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL_H WHERE INDENT_NO='1'");
            rsHItemDetail.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("INDENT_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(INDENT_NO)='"+theDocNo+"'");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            //No Primary Keys will be updated & Updating of Header starts
            
            //=========== Short Close Mode =============//
            //Qty. must be lowered not exceeded //
            for(int i=1;i<=colLineItems.size();i++) {
                clsOrderItem ObjItem=(clsOrderItem)colLineItems.get(Integer.toString(i));
                
                
                String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                MRNo=(String)ObjItem.getAttribute("MR_NO").getObj();
                MRSrNo=(int)ObjItem.getAttribute("MR_SR_NO").getVal();
                
                double MRQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
                
                
                if((!MRNo.trim().equals(""))&&(MRSrNo>0)) //MIR No. entered
                {
                    //Get the  MIR Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT REQ_QTY,ALLOCATED_QTY,ITEM_CODE FROM D_INV_REQ_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo+"' AND SR_NO="+MRSrNo;
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        MRQty=EITLERPGLOBAL.round(rsTmp.getDouble("REQ_QTY"),3)-EITLERPGLOBAL.round(rsTmp.getDouble("ALLOCATED_QTY"),3);
                        
                        //Cross Check Item Codes
                        if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                            LastError="Item Code in indent doesn't match with requsition. Original Item code is "+rsTmp.getString("ITEM_CODE");
                            return false;
                        }
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MR_NO='"+MRNo+"' AND MR_SR_NO="+MRSrNo+" AND INDENT_NO<>'"+(String)getAttribute("INDENT_NO").getObj()+"' AND INDENT_NO NOT IN(SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    double AllocatedQty=0;
                    CurrentQty=ObjItem.getAttribute("QTY").getVal()+AllocatedQty;
                    
                    if((CurrentQty+PrevQty) > MRQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        //LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds PR No. "+MRNo+" Sr. No. "+MRSrNo+" qty "+MRQty+". Please verify the input.";
                        //return false;
                    }
                    
                }
                
                
                double Qty=ObjItem.getAttribute("QTY").getVal();
                int SrNo=(int)ObjItem.getAttribute("SR_NO").getVal();
                double ExQty=0;
                double OriginalQty=0;
                
                //Get Original Qty.
                rsTmp=data.getResult("SELECT QTY FROM D_INV_INDENT_DETAIL WHERE INDENT_NO='"+theDocNo+"' AND SR_NO="+SrNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    OriginalQty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                }
                
                
                //Get Executed Qty
                rsTmp=data.getResult("SELECT SUM(QTY) AS SUMQTY FROM D_PUR_PO_HEADER A,D_PUR_PO_DETAIL B WHERE A.PO_NO=B.PO_NO AND A.PO_TYPE=B.PO_TYPE AND A.CANCELLED=0 AND INDENT_NO='"+theDocNo+"' AND INDENT_SR_NO="+SrNo);
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ExQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
                }
                
                
                /*if(Qty>OriginalQty) {
                    LastError="Qty cannot be increased in amend mode.";
                    return false;
                }*/
                
               /* if(Qty<ExQty) {
                    LastError="Qty cannot be decreased to "+Qty+". It can be short closed upto "+ExQty;
                    return false;
                }
                
            }
            //=========================================//
            
            
            //=======Short Close Indent ===========//
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsOrderItem ObjItem=(clsOrderItem)colLineItems.get(Integer.toString(i));
                
                double Qty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                double POQty=EITLERPGLOBAL.round(ObjItem.getAttribute("PO_QTY").getVal(),3);
                
                int SrNo=(int)ObjItem.getAttribute("SR_NO").getVal();
                
                data.Execute("UPDATE D_INV_INDENT_DETAIL SET QTY="+Qty+",BAL_QTY="+(Qty-POQty)+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE INDENT_NO='"+theDocNo+"' AND SR_NO="+SrNo);
                data.Execute("UPDATE D_INV_INDENT_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE INDENT_NO='"+theDocNo+"' AND SR_NO="+SrNo);
            }
            //=====================================//
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_INDENT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+(String)getAttribute("INDENT_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("INDENT_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS","I");
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("INDENT_NO",(String) getAttribute("INDENT_NO").getObj());
            rsHistory.updateString("INDENT_DATE",(String) getAttribute("INDENT_DATE").getObj());
            rsHistory.updateString("INDENT_TYPE",(String) getAttribute("INDENT_TYPE").getObj());
            rsHistory.updateLong("FOR_UNIT",(long) getAttribute("FOR_UNIT").getVal());
            rsHistory.updateLong("FOR_DEPT_ID",(long) getAttribute("FOR_DEPT_ID").getVal());
            rsHistory.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsHistory.updateLong("BUYER",(long) getAttribute("BUYER").getVal());
            rsHistory.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getVal(),3));
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            //RollBack will just update linked tables and make entries as it is
            //rollback();
            
            
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INDENT_NO='1'");
            String nIndentno=(String) getAttribute("INDENT_NO").getObj();
            //long nUserID=(long)getAttribute("USER_ID").getVal();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            
            for(int l=1;l<=colItemDetail.size();l++) {
                clsOrderItemDetail ObjLot=(clsOrderItemDetail)colItemDetail.get(Integer.toString(l));
                
                rsHItemDetail.moveToInsertRow();
                rsHItemDetail.updateInt("REVISION_NO",RevNo);
                rsHItemDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsHItemDetail.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                rsHItemDetail.updateInt("INDENT_SR_NO",0);
                rsHItemDetail.updateInt("SR_NO",l);
                rsHItemDetail.updateString("POSITION_DESC",(String)ObjLot.getAttribute("POSITION_DESC").getObj());
                rsHItemDetail.updateString("POSITION_NO",(String)ObjLot.getAttribute("POSITION_NO").getObj());
                rsHItemDetail.updateString("CARD_NO",(String)ObjLot.getAttribute("CARD_NO").getObj());
                rsHItemDetail.updateString("NAME",(String)ObjLot.getAttribute("NAME").getObj());
                rsHItemDetail.updateString("SHOE_SIZE",(String)ObjLot.getAttribute("SHOE_SIZE").getObj());
                rsHItemDetail.updateBoolean("GIVEN_LAST_YEAR",ObjLot.getAttribute("GIVEN_LAST_YEAR").getBool());
                rsHItemDetail.updateString("REMARKS",(String)ObjLot.getAttribute("REMARKS").getObj());
                rsHItemDetail.updateInt("NO_ELIGIBLE",(int)ObjLot.getAttribute("NO_ELIGIBLE").getVal());
                rsHItemDetail.updateInt("NO_LAST_YEAR",(int)ObjLot.getAttribute("NO_LAST_YEAR").getVal());
                rsHItemDetail.updateInt("NO_THIS_YEAR",(int)ObjLot.getAttribute("NO_THIS_YEAR").getVal());
                rsHItemDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsHItemDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsHItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHItemDetail.updateBoolean("CHANGED",true);
                rsHItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHItemDetail.insertRow();
            }
            
            
            // Inserting records in Indent Details
            for(int i=1;i<=colLineItems.size();i++) {
                clsOrderItem ObjOrder=(clsOrderItem) colLineItems.get(Integer.toString(i));
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("INDENT_NO",nIndentno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("MR_NO",(String) ObjOrder.getAttribute("MR_NO").getObj());
                rsHDetail.updateInt("MR_SR_NO",(int) ObjOrder.getAttribute("MR_SR_NO").getVal());
                rsHDetail.updateString("ITEM_CODE",(String) ObjOrder.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String) ObjOrder.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjOrder.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("QTY").getVal(),3));
                rsHDetail.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("BAL_QTY").getVal(),3));
                rsHDetail.updateDouble("PO_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("PO_QTY").getVal(),3));
                rsHDetail.updateDouble("ALLOCATED_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("ALLOCATED_QTY").getVal(),3));
                rsHDetail.updateDouble("STOCK_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("STOCK_QTY").getVal(),3));
                rsHDetail.updateDouble("RATE",EITLERPGLOBAL.round(ObjOrder.getAttribute("RATE").getVal(),3));
                rsHDetail.updateString("REQUIRED_DATE",(String) ObjOrder.getAttribute("REQUIRED_DATE").getObj());
                rsHDetail.updateDouble("NET_AMT",EITLERPGLOBAL.round(ObjOrder.getAttribute("NET_AMT").getVal(),3));
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjOrder.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY",(long) ObjOrder.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjOrder.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjOrder.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) ObjOrder.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("LAST_SUPP_ID",(String)ObjOrder.getAttribute("LAST_SUPP_ID").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjOrder.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("PEND_INSP_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("PEND_INSP_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_INDENT_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("PEND_INDENT_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_PO_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("PEND_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_PO_NO",(String)ObjOrder.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjOrder.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_PO_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("LAST_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_GRN_NO",(String)ObjOrder.getAttribute("LAST_GRN_NO").getObj());
                rsHDetail.updateString("LAST_GRN_DATE",(String)ObjOrder.getAttribute("LAST_GRN_DATE").getObj());
                rsHDetail.updateDouble("LAST_GRN_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("LAST_GRN_QTY").getVal(),3));
                rsHDetail.updateDouble("LAST_GRN_RATE",EITLERPGLOBAL.round(ObjOrder.getAttribute("LAST_GRN_RATE").getVal(),3));
                rsHDetail.updateDouble("AA_INDENT_QTY",EITLERPGLOBAL.round(ObjOrder.getAttribute("AA_INDENT_QTY").getVal(),3));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
                for(int l=1;l<=ObjOrder.colItemDetail.size();l++) {
                    clsOrderItemDetail ObjLot=(clsOrderItemDetail)ObjOrder.colItemDetail.get(Integer.toString(l));
                    
                    rsHItemDetail.moveToInsertRow();
                    rsHItemDetail.updateInt("REVISION_NO",RevNo);
                    rsHItemDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsHItemDetail.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                    rsHItemDetail.updateInt("INDENT_SR_NO",i);
                    rsHItemDetail.updateInt("SR_NO",l);
                    rsHItemDetail.updateString("POSITION_DESC",(String)ObjLot.getAttribute("POSITION_DESC").getObj());
                    rsHItemDetail.updateString("POSITION_NO",(String)ObjLot.getAttribute("POSITION_NO").getObj());
                    rsHItemDetail.updateString("CARD_NO",(String)ObjLot.getAttribute("CARD_NO").getObj());
                    rsHItemDetail.updateString("NAME",(String)ObjLot.getAttribute("NAME").getObj());
                    rsHItemDetail.updateString("SHOE_SIZE",(String)ObjLot.getAttribute("SHOE_SIZE").getObj());
                    rsHItemDetail.updateBoolean("GIVEN_LAST_YEAR",ObjLot.getAttribute("GIVEN_LAST_YEAR").getBool());
                    rsHItemDetail.updateString("REMARKS",(String)ObjLot.getAttribute("REMARKS").getObj());
                    rsHItemDetail.updateInt("NO_ELIGIBLE",(int)ObjLot.getAttribute("NO_ELIGIBLE").getVal());
                    rsHItemDetail.updateInt("NO_LAST_YEAR",(int)ObjLot.getAttribute("NO_LAST_YEAR").getVal());
                    rsHItemDetail.updateInt("NO_THIS_YEAR",(int)ObjLot.getAttribute("NO_THIS_YEAR").getVal());
                    rsHItemDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                    rsHItemDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsHItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                    rsHItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsHItemDetail.updateBoolean("CHANGED",true);
                    rsHItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHItemDetail.insertRow();
                }
            }
            
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
                */       
    /*}*/
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
  /*  public boolean CanDelete(int pCompanyID,String pIndentno,int pUserID) {
    /*    Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND INDENT_NO='"+pIndentno+"' AND APPROVED=true";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=1 AND DOC_NO='"+pIndentno+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
     */
  /*  }*/
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
 /*   public boolean IsEditable(int pCompanyID,String pIndentno,int pUserID) {
     /*   Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND INDENT_NO='"+pIndentno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=3 AND DOC_NO='"+pIndentno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
                tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp=tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if(rsTmp.getInt("COUNT")>0) {
                    
                    
                    if(EITLERPGLOBAL.gNewUserID!=EITLERPGLOBAL.gUserID) {
                        
                        int DeptID=0;
                        //User must be using other authority.
                        strSQL="SELECT FOR_DEPT_ID FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND INDENT_NO='"+pIndentno+"'";
                        tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        rsTmp=tmpStmt.executeQuery(strSQL);
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            DeptID=rsTmp.getInt("FOR_DEPT_ID");
                        }
                        
                        if(clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gUserID,EITLERPGLOBAL.gNewUserID,3,DeptID)) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        //Yes document is waiting for this user
                        return true;
                    }
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
      */
   /* }*/
    
    
    //Deletes current record
  /*  public boolean Delete(int pUserID) {
       /* try {
            int lCompanyID=(int) getAttribute("COMPANY_ID").getVal();
            String lIndentno=(String) getAttribute("INDENT_NO").getObj();
            int lUserID=(int)getAttribute("FROM").getVal();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lIndentno,lUserID)) {
                rollback();
                String strQry = "DELETE FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INDENT_NO='"+lIndentno.trim()+"'";
                data.Execute(strQry);
            }
            
            rsOrder.refreshRow();
            return MoveLast();
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }*/
 /*   }*/
    
    
  /*  public Object getObject(int pCompanyID,String pIndentNo) {
    //public Object getObject(String PartyCode,String PieceNo){
        String strCondition = " WHERE PARTY_CD="+PartyCode+" AND PIECE_NO='"+PieceNo+"'";
        clsOrder ObjOrder=new clsOrder();
        //ObjOrder.Filter(strCondition,pCompanyID);
        ObjOrder.Filter(strCondition,PartyCode);
        return ObjOrder;
    }*/
    
    
  //  public Object getObject(int pCompanyID,String pIndentNo,String pURL) {
       /* String strCondition = " WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND INDENT_NO='"+pIndentNo.trim()+"'";
        clsOrder ObjOrder=new clsOrder();
        ObjOrder.Filter(strCondition,pCompanyID,pURL);
        return ObjOrder;*/
    //}
    
    
  //  public HashMap getList(String pCondition) {
    /*    ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp2;
        Statement tmpStmt2;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=Conn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_INDENT_HEADER"+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsOrder ObjOrder=new clsOrder();
                
                //Populate the user
                ObjOrder.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjOrder.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjOrder.setAttribute("INDENT_DATE",rsTmp.getString("INDENT_DATE"));
                ObjOrder.setAttribute("INDENT_TYPE",rsTmp.getString("INDENT_TYPE"));
                ObjOrder.setAttribute("FOR_UNIT",rsTmp.getLong("FOR_UNIT"));
                ObjOrder.setAttribute("FOR_DEPT_ID",rsTmp.getLong("FOR_DEPT_ID"));
                ObjOrder.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjOrder.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjOrder.setAttribute("BUYER",rsTmp.getLong("BUYER"));
                ObjOrder.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjOrder.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjOrder.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjOrder.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                ObjOrder.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjOrder.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjOrder.setAttribute("STATUS",rsTmp.getString("STATUS"));
                ObjOrder.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjOrder.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjOrder.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjOrder.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjOrder.setAttribute("GROSS_AMOUNT",EITLERPGLOBAL.round(rsTmp.getLong("GROSS_AMOUNT"),3));
                
                //Now Populate the collection
                //first clear the collection
                ObjOrder.colLineItems.clear();
                
                String mCompanyID=Long.toString( (long) ObjOrder.getAttribute("COMPANY_ID").getVal());
                String mIndentno=(String) ObjOrder.getAttribute("INDENT_NO").getObj();
                
                tmpStmt2=Conn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno.trim()+"'");
                
                Counter=0;
                while(rsTmp2.next()) {
                    Counter=Counter+1;
                    clsOrderItem ObjOrderdetail=new clsOrderItem();
                    
                    ObjOrderdetail.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjOrderdetail.setAttribute("INDENT_NO",rsTmp2.getString("INDENT_NO"));
                    ObjOrderdetail.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                    ObjOrderdetail.setAttribute("MR_NO",rsTmp2.getString("MR_NO"));
                    ObjOrderdetail.setAttribute("MR_SR_NO",rsTmp2.getLong("MR_SR_NO"));
                    ObjOrderdetail.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                    ObjOrderdetail.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                    ObjOrderdetail.setAttribute("QTY",EITLERPGLOBAL.round(rsTmp2.getLong("QTY"),3));
                    ObjOrderdetail.setAttribute("BAL_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("BAL_QTY"),3));
                    ObjOrderdetail.setAttribute("PO_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("PO_QTY"),3));
                    ObjOrderdetail.setAttribute("TOTAL_REQ_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("TOTAL_RECEIPT_QTY"),3));
                    ObjOrderdetail.setAttribute("ALLOCATED_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("ALLOCATED_QTY"),3));
                    ObjOrderdetail.setAttribute("STOCK_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("STOCK_QTY"),3));
                    ObjOrderdetail.setAttribute("RATE",EITLERPGLOBAL.round(rsTmp2.getLong("RATE"),3));
                    ObjOrderdetail.setAttribute("NET_AMT",EITLERPGLOBAL.round(rsTmp2.getLong("NET_AMT"),3));
                    ObjOrderdetail.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
                    ObjOrderdetail.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                    ObjOrderdetail.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjOrderdetail.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjOrderdetail.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjOrderdetail.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    ObjOrder.colLineItems.put(Long.toString(Counter),ObjOrderdetail);
                }// Innser while
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjOrder);
            }//Out While
            
        }
        catch(Exception e) {
        }
        
        return List;
     */
   // }
  public boolean Filter(String pCondition,String pPartyCode,String pPieceNo){
      Ready = false;
      System.out.println(pPartyCode);
      System.out.println(pPieceNo); 
      System.out.println(pCondition);
  
        try{
                System.out.println(pPartyCode);
     System.out.println(pPieceNo);        
 
         //   String strSql = "SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER " + pCondition;
            String strSql = "SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD ="+pPartyCode+" AND PIECE_NO ="+pPieceNo+"";
            Conn = data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOrder=Stmt.executeQuery(strSql);
         if(!rsOrder.first()) {
                strSql = "SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD ="+pPartyCode+" AND PIECE_NO ="+pPieceNo+""; 
                rsOrder=Stmt.executeQuery(strSql);
                Ready=true;
                MoveToRow(LastPosition);
                return false;
            }
            else {
                Ready=true;
                MoveLast();
                return true;
            }           
        }
        catch(Exception e){
          LastError = e.getMessage();
            return false;  
        }
    }
  
  
   
    public boolean Filter(String pCondition,int pCompanyID){
        System.out.println(pCondition);
        System.out.println(pCompanyID);
    //public boolean Filter(String pCondition,String PartyCode){
        Ready=false;
        try {
            String strSql = "SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER " + pCondition ;   
            //String strSql = "SELECT * FROM D_INV_INDENT_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOrder=Stmt.executeQuery(strSql);
            
            //if(!rsOrder.first()) {
              //  strSql = "SELECT * FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD ="+PartyCode+""; 
              //  rsOrder=Stmt.executeQuery(strSql);
                Ready=true;
                MoveToRow(LastPosition);
                return false;
            //}
            /*else {
                Ready=true;
                MoveLast();
                return true;
            }*/
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
 //   public boolean Filter(String pCondition,int pCompanyID,String pURL) {
    /*    Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_INDENT_HEADER " + pCondition ;
            //String strSql = "SELECT * FROM PRODUCTION.FELT_ORDER_MASTER " + PCondition ;   
            Conn=data.getConn(pURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsOrder=Stmt.executeQuery(strSql);
            
            
            if(!rsOrder.first()) {
                strSql = "SELECT * FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY INDENT_NO";
                rsOrder=Stmt.executeQuery(strSql);
                Ready=true;
                rsOrder.first();
                setData(pURL);
                return false;
            }
            else {
                Ready=true;
                rsOrder.first();
                setData(pURL);
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }*/
 //   }
    
    
  private boolean setData() {
        ResultSet rsTmp,rsItemDetail;
        Connection tmpConn;
        Statement tmpStmt,stItemDetail;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            /*if(HistoryView) {
                RevNo=rsOrder.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsOrder.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }*/
            
           // setAttribute("DOC_ID",rsOrder.getLong("DOC_ID"));
           // setAttribute("ATTACHEMENT",rsOrder.getBoolean("ATTACHEMENT"));
           // setAttribute("ATTACHEMENT_PATH",rsOrder.getString("ATTACHEMENT_PATH"));
            
            setAttribute("PARTY_CD",rsOrder.getString("PARTY_CD"));
            setAttribute("PIECE_NO",rsOrder.getString("PIECE_NO"));
            setAttribute("PRODUCT_CODE",rsOrder.getString("PRODUCT_CODE"));
            
            return true;          
        }
        catch(Exception e) {
            return false;
        }    
   }
  
 public void MoveToRow(int RowNo) {
        try {
            rsOrder.absolute(RowNo);
            
        }
        catch(Exception e) {
            try {
                rsOrder.last();
            }
            catch(Exception m){}
        }
        setData();
        
    }
  
 public void abc(String aa) {
     System.out.println (aa);
     
     
     
     
 } 
  
    
    
  //  private boolean setData(String pURL) {
    /*    ResultSet rsTmp,rsItemDetail;
        Connection tmpConn;
        Statement tmpStmt,stItemDetail;
        
        tmpConn=data.getConn(pURL);
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            setAttribute("DOC_ID",rsOrder.getLong("DOC_ID"));
            setAttribute("ATTACHEMENT",rsOrder.getBoolean("ATTACHEMENT"));
            setAttribute("ATTACHEMENT_PATH",rsOrder.getString("ATTACHEMENT_PATH"));
            
            setAttribute("COMPANY_ID",rsOrder.getLong("COMPANY_ID"));
            setAttribute("INDENT_NO",rsOrder.getString("INDENT_NO"));
            setAttribute("INDENT_DATE",rsOrder.getString("INDENT_DATE"));
            setAttribute("INDENT_TYPE",rsOrder.getString("INDENT_TYPE"));
            setAttribute("FOR_UNIT",rsOrder.getLong("FOR_UNIT"));
            setAttribute("FOR_DEPT_ID",rsOrder.getLong("FOR_DEPT_ID"));
            setAttribute("PURPOSE",rsOrder.getString("PURPOSE"));
            setAttribute("BUYER",rsOrder.getLong("BUYER"));
            setAttribute("APPROVED",rsOrder.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsOrder.getString("APPROVED_DATE"));
            setAttribute("REJECTED_DATE",rsOrder.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsOrder.getString("REJECTED_REMARKS"));
            setAttribute("REMARKS",rsOrder.getString("REMARKS"));
            setAttribute("CANCELED",rsOrder.getInt("CANCELED"));
            setAttribute("STATUS",rsOrder.getString("STATUS"));
            setAttribute("CREATED_BY",rsOrder.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsOrder.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsOrder.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsOrder.getString("MODIFIED_DATE"));
            setAttribute("GROSS_AMOUNT",rsOrder.getDouble("GROSS_AMOUNT"));
            setAttribute("HIERARCHY_ID",rsOrder.getInt("HIERARCHY_ID"));
            
            
            colItemDetail.clear();
            
            String mCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mIndentno=(String) getAttribute("INDENT_NO").getObj();
            
            //== Insert Lot Nos. ==
            stItemDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsItemDetail=stItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno+"' AND INDENT_SR_NO=0 ORDER BY RIGHT(POSITION_NO,6) ");
            rsItemDetail.first();
            
            int LotCounter=0;
            while((!rsItemDetail.isAfterLast())&&(rsItemDetail.getRow()>0)) {
                LotCounter=LotCounter+1;
                
                clsIndentItemDetail ObjLot=new clsIndentItemDetail();
                ObjLot.setAttribute("COMPANY_ID",rsItemDetail.getInt("COMPANY_ID"));
                ObjLot.setAttribute("INDENT_NO",rsItemDetail.getString("INDENT_NO"));
                ObjLot.setAttribute("INDENT_SR_NO",rsItemDetail.getString("INDENT_SR_NO"));
                ObjLot.setAttribute("SR_NO",rsItemDetail.getInt("SR_NO"));
                ObjLot.setAttribute("POSITION_DESC",rsItemDetail.getString("POSITION_DESC"));
                ObjLot.setAttribute("POSITION_NO",rsItemDetail.getString("POSITION_NO"));
                ObjLot.setAttribute("CARD_NO",rsItemDetail.getString("CARD_NO"));
                ObjLot.setAttribute("NAME",rsItemDetail.getString("NAME"));
                ObjLot.setAttribute("SHOE_SIZE",rsItemDetail.getString("SHOE_SIZE"));
                ObjLot.setAttribute("GIVEN_LAST_YEAR",rsItemDetail.getBoolean("GIVEN_LAST_YEAR"));
                ObjLot.setAttribute("REMARKS",rsItemDetail.getString("REMARKS"));
                ObjLot.setAttribute("NO_ELIGIBLE",rsItemDetail.getInt("NO_ELIGIBLE"));
                ObjLot.setAttribute("NO_LAST_YEAR",rsItemDetail.getInt("NO_LAST_YEAR"));
                ObjLot.setAttribute("NO_THIS_YEAR",rsItemDetail.getInt("NO_THIS_YEAR"));
                
                colItemDetail.put(Integer.toString(LotCounter),ObjLot);
                rsItemDetail.next();
            }
            
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            
            tmpStmt=tmpConn.createStatement();
            String strSql="SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno.trim()+"' ORDER BY SR_NO";
            rsTmp=tmpStmt.executeQuery(strSql);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsIndentItem ObjItem=new clsIndentItem();
                
                int SrNo=rsTmp.getInt("SR_NO");
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjItem.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjItem.setAttribute("MR_NO",rsTmp.getString("MR_NO"));
                ObjItem.setAttribute("MR_SR_NO",rsTmp.getLong("MR_SR_NO"));
                ObjItem.setAttribute("ITEM_CODE",rsTmp.getString("ITEM_CODE"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("UNIT",rsTmp.getLong("UNIT"));
                ObjItem.setAttribute("QTY",EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3));
                ObjItem.setAttribute("OTHER_COMPANY_STOCK",EITLERPGLOBAL.round(rsTmp.getDouble("OTHER_COMPANY_STOCK"),3));
                ObjItem.setAttribute("BAL_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("BAL_QTY"),3));
                ObjItem.setAttribute("PO_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("PO_QTY"),3));
                ObjItem.setAttribute("ALLOCATED_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("ALLOCATED_QTY"),3));
                ObjItem.setAttribute("STOCK_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("STOCK_QTY"),3));
                ObjItem.setAttribute("NET_AMT",EITLERPGLOBAL.round(rsTmp.getDouble("NET_AMT"),3));
                ObjItem.setAttribute("RATE",EITLERPGLOBAL.round(rsTmp.getDouble("RATE"),3));
                ObjItem.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjItem.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("LAST_SUPP_ID",rsTmp.getString("LAST_SUPP_ID"));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("PEND_INSP_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("PEND_INSP_QTY"),3));
                ObjItem.setAttribute("PEND_INDENT_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("PEND_INDENT_QTY"),3));
                ObjItem.setAttribute("PEND_PO_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("PEND_PO_QTY"),3));
                ObjItem.setAttribute("LAST_PO_NO",rsTmp.getString("LAST_PO_NO"));
                ObjItem.setAttribute("LAST_PO_DATE",rsTmp.getString("LAST_PO_DATE"));
                ObjItem.setAttribute("LAST_PO_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("LAST_PO_QTY"),3));
                ObjItem.setAttribute("LAST_GRN_NO",rsTmp.getString("LAST_GRN_NO"));
                ObjItem.setAttribute("LAST_GRN_DATE",rsTmp.getString("LAST_GRN_DATE"));
                ObjItem.setAttribute("LAST_GRN_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("LAST_GRN_QTY"),3));
                ObjItem.setAttribute("LAST_GRN_RATE",EITLERPGLOBAL.round(rsTmp.getDouble("LAST_GRN_RATE"),3));
                ObjItem.setAttribute("AA_INDENT_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("AA_INDENT_QTY"),3));
                
                
                //== Insert Lot Nos. ==
                stItemDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsItemDetail=stItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno+"' AND INDENT_SR_NO="+SrNo+" ORDER BY RIGHT(POSITION_NO,6)");
                rsItemDetail.first();
                
                LotCounter=0;
                while((!rsItemDetail.isAfterLast())&&(rsItemDetail.getRow()>0)) {
                    LotCounter=LotCounter+1;
                    
                    clsIndentItemDetail ObjLot=new clsIndentItemDetail();
                    ObjLot.setAttribute("COMPANY_ID",rsItemDetail.getInt("COMPANY_ID"));
                    ObjLot.setAttribute("INDENT_NO",rsItemDetail.getString("INDENT_NO"));
                    ObjLot.setAttribute("INDENT_SR_NO",rsItemDetail.getString("INDENT_SR_NO"));
                    ObjLot.setAttribute("SR_NO",rsItemDetail.getInt("SR_NO"));
                    ObjLot.setAttribute("POSITION_DESC",rsItemDetail.getString("POSITION_DESC"));
                    ObjLot.setAttribute("POSITION_NO",rsItemDetail.getString("POSITION_NO"));
                    ObjLot.setAttribute("CARD_NO",rsItemDetail.getString("CARD_NO"));
                    ObjLot.setAttribute("NAME",rsItemDetail.getString("NAME"));
                    ObjLot.setAttribute("SHOE_SIZE",rsItemDetail.getString("SHOE_SIZE"));
                    ObjLot.setAttribute("GIVEN_LAST_YEAR",rsItemDetail.getBoolean("GIVEN_LAST_YEAR"));
                    ObjLot.setAttribute("REMARKS",rsItemDetail.getString("REMARKS"));
                    ObjLot.setAttribute("NO_ELIGIBLE",rsItemDetail.getInt("NO_ELIGIBLE"));
                    ObjLot.setAttribute("NO_LAST_YEAR",rsItemDetail.getInt("NO_LAST_YEAR"));
                    ObjLot.setAttribute("NO_THIS_YEAR",rsItemDetail.getInt("NO_THIS_YEAR"));
                    
                    ObjItem.colItemDetail.put(Integer.toString(LotCounter),ObjLot);
                    rsItemDetail.next();
                }
                
                colLineItems.put(Long.toString(Counter),ObjItem);
            }
            return true;
        }
        catch(Exception e) {
            return false;
        }
     */
   // }
    
  //  public boolean rollback() {
    /*    try {
            String mCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mIndentno=(String) getAttribute("INDENT_NO").getObj();
            String nIndenttype = (String) getAttribute("INDENT_TYPE").getObj();
            
            Statement Stmt;
            ResultSet rsTmp;
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=Stmt.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INDENT_NO='"+mIndentno.trim()+"'");
            
            // Updating Material Requisition & Item Master
            if(rsTmp.isAfterLast()||rsTmp.isBeforeFirst()) {
                rsTmp.first();
            }
            while(! rsTmp.isAfterLast()) {
                ResultSet rsMR;
                ResultSet rsItem;
                
                double Update_Qty = 0;
                Update_Qty = rsTmp.getLong("QTY") + rsTmp.getLong("ALLOCATED_QTY");
                
                if (nIndenttype.equals("R")) {
                    //Indent_Type = "M" ['M' means Indent Prepared from Material Requisition]
                    String strReq = "UPDATE D_INV_REQ_DETAIL SET INDENT_QTY = INDENT_QTY - "+
                    rsTmp.getLong("QTY")+", BAL_QTY= BAL_QTY + "+
                    Update_Qty+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND REQ_NO='"+
                    rsTmp.getString("INDENT_NO")+"' AND SR_NO="+rsTmp.getLong("SR_NO");
                    
                    Stmt.executeUpdate(strReq);
                }
                
                String strItem = "UPDATE D_INV_ITEM_MASTER SET ALLOCATED_QTY=ALLOCATED_QTY - "+
                rsTmp.getLong("ALLOCATED_QTY")+",ON_HAND_QTY=ON_HAND_QTY + "+
                rsTmp.getLong("ALLOCATED_QTY")+",CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND ITEM_ID='"+rsTmp.getString("ITEM_CODE")+"'";
                
                Stmt.executeUpdate(strItem);
                rsTmp.next();
            }
            
            //Removing records from Indent Detail
            data.Execute("DELETE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno.trim()+"'");
            return true;
        }
        catch(Exception e) {
            return false;
        }
     */        
   // }
    
  //  public static HashMap getPendingApprovals(long pCompanyID,long pUserID,int pOrder,int FinYearFrom) {
    /*    ResultSet rsTmp=null;
        Statement tmpStmt=null;
        
        ResultSet rsTmp2=null;
        Statement tmpStmt2=null;
        HashMap List=new HashMap();
        long Counter=0;
        String strSQL="";
        
        Connection tmpConn;
        
        try {
            Statement Stmt;
            tmpConn=data.getConn();
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT INDENT_NO,INDENT_DATE,RECEIVED_DATE,FOR_DEPT_ID AS DEPT_ID FROM D_INV_INDENT_HEADER,D_COM_DOC_DATA WHERE D_INV_INDENT_HEADER.INDENT_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_INDENT_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=3 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT INDENT_NO,INDENT_DATE,RECEIVED_DATE,FOR_DEPT_ID AS DEPT_ID FROM D_INV_INDENT_HEADER,D_COM_DOC_DATA WHERE D_INV_INDENT_HEADER.INDENT_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_INDENT_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=3 ORDER BY INDENT_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT INDENT_NO,INDENT_DATE,RECEIVED_DATE,FOR_DEPT_ID AS DEPT_ID FROM D_INV_INDENT_HEADER,D_COM_DOC_DATA WHERE D_INV_INDENT_HEADER.INDENT_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_INDENT_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=3 ORDER BY INDENT_NO";
            }
            
            //strSQL="SELECT INDENT_NO,INDENT_DATE FROM D_INV_INDENT_HEADER,D_COM_DOC_DATA WHERE D_INV_INDENT_HEADER.INDENT_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_INDENT_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=3";
            Stmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=Stmt.executeQuery("SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND USER_ID="+Long.toString(pUserID)+" AND STATUS='W' AND MODULE_ID=3");
            rsTmp=Stmt.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("INDENT_DATE"),FinYearFrom))
                {
                Counter++;
                ResultSet rsOrder1;
                String gIndentno=rsTmp.getString("INDENT_NO");
                
                clsIndent ObjOrder=new clsIndent();
                ObjOrder.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjOrder.setAttribute("INDENT_DATE",rsTmp.getString("INDENT_DATE"));
                ObjOrder.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjOrder.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                
                List.put(Long.toString(Counter),ObjOrder);
                }
            }
            
            rsTmp.close();
            tmpStmt.close();
            rsTmp2.close();
            tmpStmt2.close();
            //tmpConn.close();
            
        }
        catch(Exception e) {
        }
        return List;
     */
  //  }
    
  //  public static HashMap getIndentItemList(int pCompanyID,String pIndentNo,boolean pAllItems) {
        //Fourth argument pAllItems indicates whether you want all item listing or
        //Just pending Items where PO_QTY<QTY
        //i.e. PO entry is still pending
        
    /*  Connection tmpConn;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            if(pAllItems) //Retrieve All Items
            {
                strSQL="SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' ORDER BY ITEM_CODE";
            }
            else //Retrieve Only pending Items
            {
                //strSQL="SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' AND PO_QTY<QTY AND INDENT_NO IN (SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE INDENT_NO='"+pIndentNo+"' AND APPROVED=1)  ORDER BY ITEM_CODE";
                
                strSQL="SELECT B.* ";
                strSQL+="FROM ";
                strSQL+="D_INV_INDENT_HEADER A, ";
                strSQL+="D_INV_INDENT_DETAIL B ";
                strSQL+="LEFT JOIN D_PUR_PO_DETAIL P ON (P.INDENT_NO=B.INDENT_NO AND P.INDENT_SR_NO=B.SR_NO AND P.PO_NO IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_NO=P.PO_NO AND PO_TYPE=P.PO_TYPE AND APPROVED=1 AND CANCELLED=0)), ";
                strSQL+="D_COM_DEPT_MASTER D ";
                strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
                strSQL+="A.INDENT_NO=B.INDENT_NO AND ";
                strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
                strSQL+="A.APPROVED=1 AND A.CANCELED=0 AND ";
                strSQL+="B.COMPANY_ID=D.COMPANY_ID AND A.FOR_DEPT_ID=D.DEPT_ID AND ";
                strSQL+="A.INDENT_NO='"+pIndentNo+"' ";
                strSQL+="GROUP BY B.INDENT_NO,B.SR_NO ";
                strSQL+="HAVING IF(SUM(P.QTY) IS NULL,0,SUM(P.QTY))<B.QTY ";
                
            }
            
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            Counter1=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter1++;
                clsIndentItem ObjItem=new clsIndentItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_CODE",rsTmp.getString("ITEM_CODE"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                ObjItem.setAttribute("QTY",EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3));
                ObjItem.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                
                double POQty=data.getDoubleValueFromDB("SELECT SUM(B.QTY) AS PO_QTY FROM D_PUR_PO_HEADER A,D_PUR_PO_DETAIL B WHERE A.PO_NO=B.PO_NO AND A.PO_TYPE=B.PO_TYPE AND A.APPROVED=1 AND A.CANCELLED=0 AND B.INDENT_NO='"+pIndentNo+"' AND B.INDENT_SR_NO="+rsTmp.getInt("SR_NO"));
                
                ObjItem.setAttribute("PO_QTY",EITLERPGLOBAL.round(POQty,3));
                ObjItem.setAttribute("BAL_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3)-POQty);
                ObjItem.setAttribute("ALLOCATED_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("ALLOCATED_QTY"),3));
                ObjItem.setAttribute("STOCK_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("STOCK_QTY"),3));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("MR_NO",rsTmp.getString("MR_NO"));
                ObjItem.setAttribute("MR_SR_NO",rsTmp.getInt("MR_SR_NO"));
                ObjItem.setAttribute("TOTAL_REQ_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("TOTAL_REQ_QTY"),3));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                //Put into list
                List.put(Integer.toString(Counter1),ObjItem);
                
                rsTmp.next();
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
     */
   // }
    
    
 //   public static HashMap getIndentItemList(int pCompanyID,String pIndentNo,boolean pAllItems,String pURL) {
        //Fourth argument pAllItems indicates whether you want all item listing or
        //Just pending Items where PO_QTY<QTY
        //i.e. PO entry is still pending
        
  /*      Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn(pURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            if(pAllItems) //Retrieve All Items
            {
                strSQL="SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' AND INDENT_NO IN (SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE INDENT_NO='"+pIndentNo+"' AND APPROVED=1) ORDER BY ITEM_CODE";
            }
            else //Retrieve Only pending Items
            {
                strSQL="SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' AND PO_QTY<QTY  AND INDENT_NO IN (SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE INDENT_NO='"+pIndentNo+"' AND APPROVED=1) ORDER BY ITEM_CODE";
            }
            
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            Counter1=0;
            
            while(!rsTmp.isAfterLast()) {
                Counter1++;
                clsIndentItem ObjItem=new clsIndentItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjItem.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                ObjItem.setAttribute("ITEM_CODE",rsTmp.getString("ITEM_CODE"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("UNIT",rsTmp.getInt("UNIT"));
                ObjItem.setAttribute("QTY",EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3));
                ObjItem.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjItem.setAttribute("BAL_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("BAL_QTY"),3));
                ObjItem.setAttribute("PO_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("PO_QTY"),3));
                ObjItem.setAttribute("ALLOCATED_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("ALLOCATED_QTY"),3));
                ObjItem.setAttribute("STOCK_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("STOCK_QTY"),3));
                ObjItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjItem.setAttribute("MR_NO",rsTmp.getString("MR_NO"));
                ObjItem.setAttribute("MR_SR_NO",rsTmp.getInt("MR_SR_NO"));
                ObjItem.setAttribute("TOTAL_REQ_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("TOTAL_REQ_QTY"),3));
                ObjItem.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                //Put into list
                List.put(Integer.toString(Counter1),ObjItem);
                
                rsTmp.next();
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
        }
   */
 //   }
    
    
 //   public static double getPendingIndentQty(int pCompanyID,int pDeptID,String pItemID) {
 /*       Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        String strSQL="";
        double PendingQty=0;
        
        try {
            
            //Below is the old working Query
            //strSQL="SELECT SUM(D_INV_INDENT_DETAIL.QTY-D_INV_INDENT_DETAIL.PO_QTY) AS TOTAL_QTY FROM D_INV_INDENT_HEADER,D_INV_INDENT_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_INDENT_HEADER.COMPANY_ID=D_INV_INDENT_DETAIL.COMPANY_ID AND D_INV_INDENT_HEADER.INDENT_NO=D_INV_INDENT_DETAIL.INDENT_NO  AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_INDENT_DETAIL.PO_QTY<D_INV_INDENT_DETAIL.QTY  AND D_INV_INDENT_DETAIL.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_INDENT_HEADER.FOR_DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_INDENT_HEADER.FOR_DEPT_ID="+pDeptID+" AND D_INV_INDENT_DETAIL.ITEM_CODE='"+pItemID+"' AND D_INV_INDENT_HEADER.APPROVED=1 AND D_INV_INDENT_HEADER.CANCELED=0";

            strSQL="SELECT (B.QTY-IF(SUM(P.QTY) IS NULL,0,SUM(P.QTY))) AS PENDING_QTY,B.QTY ";
            strSQL+="FROM ";
            strSQL+="D_INV_INDENT_HEADER A, ";
            strSQL+="D_INV_INDENT_DETAIL B ";
            strSQL+="LEFT JOIN D_PUR_PO_DETAIL P ON (P.INDENT_NO=B.INDENT_NO AND P.INDENT_SR_NO=B.SR_NO AND P.PO_NO IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_NO=P.PO_NO AND APPROVED=1 AND CANCELLED=0) ),";
            strSQL+="D_COM_DEPT_MASTER D ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.INDENT_NO=B.INDENT_NO AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
            strSQL+="A.APPROVED=1 AND ";
            strSQL+="A.CANCELED=0 AND ";
            strSQL+="B.COMPANY_ID=D.COMPANY_ID AND ";
            strSQL+="A.FOR_DEPT_ID=D.DEPT_ID AND ";
            strSQL+="B.ITEM_CODE ='"+pItemID+"' AND ";
            strSQL+="A.FOR_DEPT_ID="+pDeptID+" ";
            strSQL+="AND A.INDENT_DATE>='2006-04-01' ";
            strSQL+="GROUP BY B.INDENT_NO,B.SR_NO ";
            strSQL+="HAVING IF(SUM(P.QTY) IS NULL,0,SUM(P.QTY))<B.QTY ";
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast())
                {
                    PendingQty+=rsTmp.getDouble("PENDING_QTY");    
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            return PendingQty;
        }
        catch(Exception e) {
            return PendingQty;
        }
  */
   // }
    
    
    
  //  public static double getTotalPendingIndentQty(int pCompanyID,String pItemID) {
    /*    Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        String strSQL="";
        double PendingQty=0;
        
        try {
            
            //Below is the old working Query
            //strSQL="SELECT SUM(D_INV_INDENT_DETAIL.QTY-D_INV_INDENT_DETAIL.PO_QTY) AS TOTAL_QTY FROM D_INV_INDENT_HEADER,D_INV_INDENT_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_INDENT_HEADER.COMPANY_ID=D_INV_INDENT_DETAIL.COMPANY_ID AND D_INV_INDENT_HEADER.INDENT_NO=D_INV_INDENT_DETAIL.INDENT_NO  AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_INDENT_DETAIL.PO_QTY<D_INV_INDENT_DETAIL.QTY  AND D_INV_INDENT_DETAIL.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_INDENT_HEADER.FOR_DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_INDENT_HEADER.FOR_DEPT_ID="+pDeptID+" AND D_INV_INDENT_DETAIL.ITEM_CODE='"+pItemID+"' AND D_INV_INDENT_HEADER.APPROVED=1 AND D_INV_INDENT_HEADER.CANCELED=0";

            strSQL="SELECT (B.QTY-IF(SUM(P.QTY) IS NULL,0,SUM(P.QTY))) AS PENDING_QTY,B.QTY ";
            strSQL+="FROM ";
            strSQL+="D_INV_INDENT_HEADER A, ";
            strSQL+="D_INV_INDENT_DETAIL B ";
            strSQL+="LEFT JOIN D_PUR_PO_DETAIL P ON (P.INDENT_NO=B.INDENT_NO AND P.INDENT_SR_NO=B.SR_NO AND P.PO_NO IN (SELECT PO_NO FROM D_PUR_PO_HEADER WHERE PO_NO=P.PO_NO AND APPROVED=1 AND CANCELLED=0) ),";
            strSQL+="D_COM_DEPT_MASTER D ";
            strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
            strSQL+="A.INDENT_NO=B.INDENT_NO AND ";
            strSQL+="A.COMPANY_ID="+pCompanyID+" AND ";
            strSQL+="A.APPROVED=1 AND ";
            strSQL+="A.CANCELED=0 AND ";
            strSQL+="B.COMPANY_ID=D.COMPANY_ID AND ";
            strSQL+="A.FOR_DEPT_ID=D.DEPT_ID AND ";
            strSQL+="B.ITEM_CODE ='"+pItemID+" ";
            strSQL+="AND A.INDENT_DATE>='2006-04-01' ";
            strSQL+="GROUP BY B.INDENT_NO,B.SR_NO ";
            strSQL+="HAVING IF(SUM(P.QTY) IS NULL,0,SUM(P.QTY))<B.QTY ";
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast())
                {
                    PendingQty+=rsTmp.getDouble("PENDING_QTY");    
                    rsTmp.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            return PendingQty;
        }
        catch(Exception e) {
            return PendingQty;
        }
     */        
  //  }
    
    
    
    
    
    // RIA Status
    // 0 - Not created
    // 1- Created but under approval
    // 2 - Created and Approved
    
  //  public static int IsRIACreated(int pCompanyID,String pIndentNo,int pSrNo) {
  /*      Connection tmpConn=null;
        Statement stInquiry=null,stQuotation=null,stRIA=null,stTmp=null;
        ResultSet rsInquiry=null,rsQuotation=null,rsRIA=null,rsTmp=null;
        String strSQL="";
        int RIAStatus=0; //By default not created
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' AND INDENT_SRNO="+pSrNo);
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                String InquiryNo=rsInquiry.getString("INQUIRY_NO");
                int InquirySrNo=rsInquiry.getInt("SR_NO");
                
                if((!InquiryNo.trim().equals(""))&&InquirySrNo>0) {
                    stQuotation=tmpConn.createStatement();
                    rsQuotation=stQuotation.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+pCompanyID+" INQUIRY_NO='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrNo+" AND QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND APPROVED=1)");
                    rsQuotation.first();
                    
                    if(rsQuotation.getRow()>0) {
                        String QuotID=rsQuotation.getString("QUOT_ID");
                        int QuotSrNo=rsQuotation.getInt("SR_NO");
                        
                        if((!QuotID.trim().equals(""))&&QuotSrNo>0) {
                            stRIA=tmpConn.createStatement();
                            rsRIA=stRIA.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+QuotID+"' AND QUOT_SR_NO="+QuotSrNo);
                            rsRIA.first();
                            
                            if(rsRIA.getRow()>0) {
                                String RIANo=rsRIA.getString("APPROVAL_NO");
                                
                                //Now find Approval Status of this RIA
                                stTmp=tmpConn.createStatement();
                                rsTmp=stTmp.executeQuery("SELECT APPROVED FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+RIANo+"'");
                                rsTmp.first();
                                if(rsTmp.getRow()>0) {
                                    if(rsTmp.getBoolean("APPROVED")) {
                                        RIAStatus=2;
                                    }
                                    else {
                                        RIAStatus=1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            
            return RIAStatus;
        }
        catch(Exception e) {
            return RIAStatus;
        }
   */
 //   }
    
    
    
    
    // This routine returns approved ria rates.
   // public static double getRIAQty(int pCompanyID,String pIndentNo,int pSrNo) {
     /*   Connection tmpConn=null;
        Statement stInquiry=null,stQuotation=null,stRIA=null,stTmp=null;
        ResultSet rsInquiry=null,rsQuotation=null,rsRIA=null,rsTmp=null;
        String strSQL="";
        double RIAQty=0; //By default not created
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' AND INDENT_SRNO="+pSrNo+"  AND INQUIRY_NO NOT IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_HEADER WHERE CANCELLED=1) ORDER BY INDENT_NO DESC");
            rsInquiry.first();
            
            if(rsInquiry.getRow()>0) {
                String InquiryNo=rsInquiry.getString("INQUIRY_NO");
                int InquirySrNo=rsInquiry.getInt("SR_NO");
                
                if((!InquiryNo.trim().equals(""))&&InquirySrNo>0) {
                    stQuotation=tmpConn.createStatement();
                    rsQuotation=stQuotation.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrNo+" AND QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVED=1) AND QUOT_ID NOT IN (SELECT QUOT_ID FROM D_PUR_QUOT_HEADER WHERE CANCELLED=1)");
                    rsQuotation.first();
                    
                    if(rsQuotation.getRow()>0) {
                        
                        while(!rsQuotation.isAfterLast()) {
                            String QuotID=rsQuotation.getString("QUOT_ID");
                            int QuotSrNo=rsQuotation.getInt("SR_NO");
                            
                            if((!QuotID.trim().equals(""))&&QuotSrNo>0) {
                                stRIA=tmpConn.createStatement();
                                rsRIA=stRIA.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+QuotID+"' AND QUOT_SR_NO="+QuotSrNo+" AND APPROVAL_NO NOT IN (SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER WHERE CANCELLED=1)");
                                rsRIA.first();
                                
                                if(rsRIA.getRow()>0) {
                                    String RIANo=rsRIA.getString("APPROVAL_NO");
                                    
                                    RIAQty=EITLERPGLOBAL.round(rsRIA.getDouble("CURRENT_QTY"),3);
                                    return RIAQty;
                                }
                            }
                            
                            rsQuotation.next();
                        }
                    }
                }
            }
            return RIAQty;
        }
        catch(Exception e) {
            return RIAQty;
        }
      */
 //   }
    
  //  public static String getRIANo(int pCompanyID,String pIndentNo,int pSrNo) {
  /*      Connection tmpConn=null;
        Statement stInquiry=null,stQuotation=null,stRIA=null,stTmp=null;
        ResultSet rsInquiry=null,rsQuotation=null,rsRIA=null,rsTmp=null;
        String strSQL="";
        String theRIANo=""; //By default not created
        
        try {
            tmpConn=data.getConn();
            
            stInquiry=tmpConn.createStatement();
            rsInquiry=stInquiry.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pIndentNo+"' AND INDENT_SRNO="+pSrNo+" AND INQUIRY_NO NOT IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_HEADER WHERE CANCELLED=1) ORDER BY INDENT_NO DESC");
            rsInquiry.first();
            
            //if(rsInquiry.getRow()>0) {
            while(!rsInquiry.isAfterLast()) {
                String InquiryNo=rsInquiry.getString("INQUIRY_NO");
                int InquirySrNo=rsInquiry.getInt("SR_NO");
                
                if((!InquiryNo.trim().equals(""))&&InquirySrNo>0) {
                    stQuotation=tmpConn.createStatement();
                    rsQuotation=stQuotation.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrNo+" AND QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE APPROVED=1) AND QUOT_ID NOT IN (SELECT QUOT_ID FROM D_PUR_QUOT_HEADER WHERE CANCELLED=1)");
                    rsQuotation.first();
                    
                    if(rsQuotation.getRow()>0) {
                        
                        while(!rsQuotation.isAfterLast()) {
                            String QuotID=rsQuotation.getString("QUOT_ID");
                            int QuotSrNo=rsQuotation.getInt("SR_NO");
                            
                            if((!QuotID.trim().equals(""))&&QuotSrNo>0) {
                                stRIA=tmpConn.createStatement();
                                rsRIA=stRIA.executeQuery("SELECT * FROM D_PUR_RATE_APPROVAL_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND QUOT_ID='"+QuotID+"' AND QUOT_SR_NO="+QuotSrNo+" AND APPROVAL_NO NOT IN (SELECT APPROVAL_NO FROM D_PUR_RATE_APPROVAL_HEADER WHERE CANCELLED=1)");
                                rsRIA.first();
                                
                                if(rsRIA.getRow()>0) {
                                    String RIANo=rsRIA.getString("APPROVAL_NO");
                                    
                                    //Now find Approval Status of this RIA
                                    stTmp=tmpConn.createStatement();
                                    rsTmp=stTmp.executeQuery("SELECT APPROVAL_NO,APPROVED FROM D_PUR_RATE_APPROVAL_HEADER WHERE COMPANY_ID="+pCompanyID+" AND APPROVAL_NO='"+RIANo+"'");
                                    rsTmp.first();
                                    if(rsTmp.getRow()>0) {
                                        theRIANo=rsTmp.getString("APPROVAL_NO");
                                        return theRIANo;
                                    }
                                }
                            }
                            
                            rsQuotation.next();
                        }
                    }                  
                    
                }
                rsInquiry.next();
            }
            
            return theRIANo;
        }
        catch(Exception e) {
            return theRIANo;
        }
   */
  //  }
    
   // public static double getAlreadyApprovedQty(int pCompanyID,int pDeptID,String pItemID) {
      /*  Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        String strSQL="";
        double PendingQty=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT SUM(D_INV_INDENT_DETAIL.QTY) AS TOTAL_QTY FROM D_INV_INDENT_HEADER,D_INV_INDENT_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_INDENT_HEADER.COMPANY_ID=D_INV_INDENT_DETAIL.COMPANY_ID AND D_INV_INDENT_HEADER.INDENT_NO=D_INV_INDENT_DETAIL.INDENT_NO  AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_INDENT_HEADER.APPROVED=1 AND D_INV_INDENT_DETAIL.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_INDENT_HEADER.FOR_DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_INDENT_HEADER.FOR_DEPT_ID="+pDeptID+" AND D_INV_INDENT_DETAIL.ITEM_CODE='"+pItemID+"' AND INDENT_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INDENT_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'";
            
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PendingQty=EITLERPGLOBAL.round(rsTmp.getDouble("TOTAL_QTY"),3);
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            return PendingQty;
        }
        catch(Exception e) {
            return PendingQty;
        }*/
 //   }
    
    
 //   public static double getTotalAlreadyApprovedQty(int pCompanyID,String pItemID) {
   /*     Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        String strSQL="";
        double PendingQty=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT SUM(D_INV_INDENT_DETAIL.QTY) AS TOTAL_QTY FROM D_INV_INDENT_HEADER,D_INV_INDENT_DETAIL,D_COM_DEPT_MASTER WHERE D_INV_INDENT_HEADER.COMPANY_ID=D_INV_INDENT_DETAIL.COMPANY_ID AND D_INV_INDENT_HEADER.INDENT_NO=D_INV_INDENT_DETAIL.INDENT_NO  AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_INDENT_HEADER.APPROVED=1 AND D_INV_INDENT_DETAIL.COMPANY_ID=D_COM_DEPT_MASTER.COMPANY_ID AND D_INV_INDENT_HEADER.FOR_DEPT_ID=D_COM_DEPT_MASTER.DEPT_ID AND D_INV_INDENT_DETAIL.ITEM_CODE='"+pItemID+"' AND INDENT_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INDENT_DATE<='"+EITLERPGLOBAL.FinToDateDB+"'";
            
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PendingQty=EITLERPGLOBAL.round(rsTmp.getDouble("TOTAL_QTY"),3);
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            return PendingQty;
        }
        catch(Exception e) {
            return PendingQty;
        }
    */
 //   }
    
    
  //  public static Object getLastIndent(int pCompanyID,String pItemID) {
     /*   Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        String strSQL="";
        double PendingQty=0;
        
        clsIndent ObjOrder=new clsIndent();
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            strSQL="SELECT D_INV_INDENT_HEADER.INDENT_NO FROM D_INV_INDENT_HEADER,D_INV_INDENT_DETAIL WHERE D_INV_INDENT_HEADER.COMPANY_ID=D_INV_INDENT_DETAIL.COMPANY_ID AND D_INV_INDENT_HEADER.INDENT_NO=D_INV_INDENT_DETAIL.INDENT_NO AND D_INV_INDENT_HEADER.COMPANY_ID="+pCompanyID+" AND D_INV_INDENT_DETAIL.ITEM_CODE='"+pItemID+"' AND D_INV_INDENT_HEADER.APPROVED=1 AND D_INV_INDENT_HEADER.CANCELED=0 AND D_INV_INDENT_DETAIL.PO_QTY>0 ORDER BY D_INV_INDENT_HEADER.INDENT_DATE DESC";
            
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                String IndentNo=rsTmp.getString("INDENT_NO");
                clsIndent tmpObj=new clsIndent();
                tmpObj.LoadData(pCompanyID);
                ObjOrder=(clsIndent)tmpObj.getObject(pCompanyID, IndentNo);
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            
            return ObjOrder;
        }
        catch(Exception e) {
            return ObjOrder;
        }*/
  //  }
    
  //  public boolean ShowHistory(int pCompanyID,String pDocNo) {
      /*  Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsOrder=Stmt.executeQuery("SELECT * FROM D_INV_INDENT_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
            Ready=true;
            HistoryView=true;
            MoveFirst();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }*/
  //  }
    
    
  //  public static HashMap getHistoryList(int pCompanyID,String pDocNo) {
    /*    HashMap List=new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_INDENT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsIndent ObjOrder=new clsIndent();
                
                ObjOrder.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjOrder.setAttribute("INDENT_DATE",rsTmp.getString("INDENT_DATE"));
                ObjOrder.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjOrder.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjOrder.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjOrder.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjOrder.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjOrder);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
            
        }
        catch(Exception e) {
            return List;
        }
     */
  //  }
    
    
//    public static int getDeptID(int pCompanyID,String pDocNo) {
     /*   try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            int DeptID=0;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT FOR_DEPT_ID FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                DeptID=rsTmp.getInt("FOR_DEPT_ID");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return DeptID;
        }
        catch(Exception e) {
            return 0;
        }
      */
 //   }
    
    
  //  public static String getDeliveryDate(int pCompanyID,String pDocNo,int pSrNo) {
     /*   try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            String DeliveryDate="";
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT REQUIRED_DATE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"' AND SR_NO="+pSrNo);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                DeliveryDate=rsTmp.getString("REQUIRED_DATE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return DeliveryDate;
        }
        catch(Exception e) {
            return "";
        }
      */
  //  }
    
    
  //  public static double getQty(int pCompanyID,String pDocNo,int pSrNo) {
     /*   try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            double theQty=0;
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT (QTY-PO_QTY) AS THEQTY FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"' AND SR_NO="+pSrNo);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                theQty=EITLERPGLOBAL.round(rsTmp.getDouble("THEQTY"),3);
                
                
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return theQty;
        }
        catch(Exception e) {
            return 0;
        }
      */
  //  }
    
    
  /*  public static boolean CanCancel(int pCompanyID,String pDocNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"' AND CANCELED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Now check every item for received qty.
                rsTmp=data.getResult("SELECT INDENT_NO FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"' AND PO_QTY>0 ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    //Can not cancel this PO. first MIR should be cancelled.
                }
                else {
                    canCancel=true;
                }
                
            }
            
            rsTmp.close();
        }
        catch(Exception e) {
            
            
        }
        
        return canCancel;
        */
  //  }
    
    
  //  public static boolean CancelIndent(int pCompanyID,String pDocNo) {
     /*   
        ResultSet rsTmp=null,rsOrder=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pDocNo)) {
                boolean ApprovedIndent=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedIndent=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedIndent) {
                    rsTmp=data.getResult("SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        while(!rsTmp.isAfterLast()) {
                            String MRNo=rsTmp.getString("MR_NO");
                            int MRSrNo=rsTmp.getInt("MR_SR_NO");
                            
                            if(!MRNo.trim().equals("")&&(MRSrNo>0)) {
                                double Qty=EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3);
                                double AllocatedQty=EITLERPGLOBAL.round(rsTmp.getDouble("ALLOCATED_QTY"),3);
                                
                                //Now Update the Indent Table
                                data.Execute("UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY=ALLOCATED_QTY-"+AllocatedQty+" INDENT_QTY=INDENT_QTY-"+(Qty+AllocatedQty)+",BAL_QTY=REQ_QTY-(INDENT_QTY+ALLOCATED_QTY),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND REQ_NO='"+MRNo+"' AND SR_NO="+MRSrNo);
                            }
                            
                            rsTmp.next();
                        }
                        
                    }
                    
                }
                else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pDocNo+"' AND MODULE_ID=3");
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_INDENT_HEADER SET CANCELED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
                
                Cancelled=true;
            }
            
            rsTmp.close();
            rsOrder.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
      */
  //  }
    
 //   public static String getDocStatus(int pCompanyID,String pDocNo) {
    /*   ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT INDENT_NO,APPROVED,CANCELED FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELED")) {
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
       */ 
 //   }
    
    
    
 //   public static String getDocStatus(int pCompanyID,String pDocNo,boolean Approved) {
   /*     ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT INDENT_NO,APPROVED,CANCELED FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
                if(rsTmp.getBoolean("CANCELED")) {
                    strMessage="Document is cancelled";
                }
                else {
                    strMessage="";
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
     */   
   // }
    
    
  // public static int getIndentDeptID(int pCompanyID,String pDocNo) {
    /*  ResultSet rsTmp;
        int DeptID=0;
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT FOR_DEPT_ID FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                DeptID=rsTmp.getInt("FOR_DEPT_ID");
                
            }
            rsTmp.close();
            
            
        }
        catch(Exception e) {
        }
        
        return DeptID;
        
    }*/
    
    
   // public static String getDocStatus(int pCompanyID,String pDocNo,String dbURL) {
    /*    ResultSet rsTmp;
        String strMessage="";
        
        try {
            //First check that Document exist
            rsTmp=data.getResult("SELECT INDENT_NO,APPROVED,CANCELED FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'",dbURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                if(rsTmp.getBoolean("APPROVED")) {
                    if(rsTmp.getBoolean("CANCELED")) {
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
      */  
   // }
    
    
  //  public void MoveToRow(int RowNo) {
    /*    try {
            rsOrder.absolute(RowNo);
            
        }
        catch(Exception e) {
            try {
                rsOrder.last();
            }
            catch(Exception m){}
        }
        setData();
      */  
  //  }
    
    
    //public static boolean userCanProcessIndent(int pCompanyID,String pIndentNo) {
      /*  ResultSet rsTmp;
        String strSQL="";
        boolean canProcess=false;
        
        try {
            
            int SelUserID=EITLERPGLOBAL.gUserID;
            
            strSQL=strSQL+" SELECT A.INDENT_NO,A.INDENT_DATE AS INDENT_DATE,A.FOR_DEPT_ID,A.CREATED_BY FROM ";
            strSQL=strSQL+" D_INV_INDENT_HEADER A, ";
            strSQL=strSQL+" D_INV_INDENT_DETAIL B ";
            strSQL=strSQL+" WHERE ";
            strSQL=strSQL+" A.INDENT_NO=B.INDENT_NO AND ";
            strSQL=strSQL+" A.APPROVED=1 AND ";
            strSQL=strSQL+" A.CANCELED=0 AND ";
            strSQL=strSQL+" A.INDENT_NO='"+pIndentNo+"' AND ";
            strSQL=strSQL+" A.FOR_DEPT_ID IN (SELECT DEPT_ID FROM D_COM_DEPT_BUYERS WHERE BUYER="+SelUserID+")  AND ";
            strSQL=strSQL+" B.PO_QTY<B.QTY AND ";
            strSQL=strSQL+" A.INDENT_NO NOT IN ( ";
            strSQL=strSQL+" SELECT DISTINCT(C.INDENT_NO)  AS INDENT_NO FROM ";
            strSQL=strSQL+" D_INV_INDENT_DETAIL A, ";
            strSQL=strSQL+" D_COM_BUYER_ITEMS B, ";
            strSQL=strSQL+" D_INV_INDENT_HEADER C ";
            strSQL=strSQL+" WHERE ";
            strSQL=strSQL+" A.INDENT_NO=C.INDENT_NO AND ";
            strSQL=strSQL+" C.APPROVED=1 AND C.CANCELED=0 AND ";
            strSQL=strSQL+" A.PO_QTY<QTY AND ";
            strSQL=strSQL+" B.BUYER="+SelUserID+" AND ";
            strSQL=strSQL+" B.ITEM_ID=SUBSTRING(A.ITEM_CODE,1,LENGTH(B.ITEM_ID)) ";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+" SELECT DISTINCT(A.INDENT_NO) AS INDENT_NO FROM  ";
            strSQL=strSQL+" D_INV_INDENT_DETAIL A, ";
            strSQL=strSQL+" D_COM_BUYER_ITEMS B, ";
            strSQL=strSQL+" D_INV_INDENT_HEADER C, ";
            strSQL=strSQL+" D_INV_ITEM_MASTER D ";
            strSQL=strSQL+" WHERE ";
            strSQL=strSQL+" A.INDENT_NO=C.INDENT_NO AND ";
            strSQL=strSQL+" C.APPROVED=1 AND C.CANCELED=0 AND ";
            strSQL=strSQL+" A.PO_QTY<QTY AND ";
            strSQL=strSQL+" B.BUYER<>"+SelUserID+" AND  ";
            strSQL=strSQL+" B.ITEM_CLASS<>'' AND ";
            strSQL=strSQL+" A.INDENT_NO='"+pIndentNo+"' AND ";
            strSQL=strSQL+" A.ITEM_CODE=D.ITEM_ID AND D.CANCELLED=0 AND ";
            strSQL=strSQL+" D.ABC=B.ITEM_CLASS ) ";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+" SELECT DISTINCT(C.INDENT_NO)  AS INDENT_NO,C.INDENT_DATE AS INDENT_DATE,C.FOR_DEPT_ID,C.CREATED_BY FROM  ";
            strSQL=strSQL+" D_INV_INDENT_DETAIL A, ";
            strSQL=strSQL+" D_COM_BUYER_ITEMS B, ";
            strSQL=strSQL+" D_INV_INDENT_HEADER C ";
            strSQL=strSQL+" WHERE ";
            strSQL=strSQL+" A.INDENT_NO=C.INDENT_NO AND ";
            strSQL=strSQL+" A.INDENT_NO='"+pIndentNo+"' AND ";
            strSQL=strSQL+" C.APPROVED=1 AND C.CANCELED=0 AND ";
            strSQL=strSQL+" A.PO_QTY<QTY AND ";
            strSQL=strSQL+" B.BUYER="+SelUserID+" AND  ";
            strSQL=strSQL+" B.ITEM_ID=SUBSTRING(A.ITEM_CODE,1,LENGTH(B.ITEM_ID)) ";
            strSQL=strSQL+" UNION ";
            strSQL=strSQL+" SELECT DISTINCT(A.INDENT_NO) AS INDENT_NO,C.INDENT_DATE AS INDENT_DATE,C.FOR_DEPT_ID,C.CREATED_BY FROM  ";
            strSQL=strSQL+" D_INV_INDENT_DETAIL A, ";
            strSQL=strSQL+" D_COM_BUYER_ITEMS B, ";
            strSQL=strSQL+" D_INV_INDENT_HEADER C, ";
            strSQL=strSQL+" D_INV_ITEM_MASTER D ";
            strSQL=strSQL+" WHERE ";
            strSQL=strSQL+" A.INDENT_NO=C.INDENT_NO AND ";
            strSQL=strSQL+" C.APPROVED=1 AND C.CANCELED=0 AND ";
            strSQL=strSQL+" A.PO_QTY<QTY AND ";
            strSQL=strSQL+" B.BUYER="+SelUserID+" AND  ";
            strSQL=strSQL+" B.ITEM_CLASS<>'' AND ";
            strSQL=strSQL+" A.ITEM_CODE=D.ITEM_ID AND D.CANCELLED=0 AND ";
            strSQL=strSQL+" D.ABC=B.ITEM_CLASS  ";
            strSQL=strSQL+" ORDER BY INDENT_DATE ";
            
            
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                canProcess=true;
            }
            
            
        }
        catch(Exception e) {
            
        }
        
        return canProcess;*/
    //}
}