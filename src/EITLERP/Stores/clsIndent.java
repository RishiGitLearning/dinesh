/*
 * clsIndent.java
 *
 * Created on April 5, 2004, 3:02 PM
 */

package EITLERP.Stores;

import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.*;
import EITLERP.Purchase.*;
import java.io.*;

/**
 *
 * @author  nhpatel
 * @version
 */

public class clsIndent {
    
    public String LastError="";
    private ResultSet rsIndent;
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
    
    
    /** Creates new clsIndent */
    public clsIndent() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(0));
        props.put("INDENT_NO",new Variant(0));
        props.put("INDENT_DATE",new Variant(""));
        props.put("INDENT_TYPE",new Variant(""));
        props.put("FOR_UNIT",new Variant(0));
        props.put("FOR_DEPT_ID",new Variant(0));
        props.put("PURPOSE",new Variant(""));
        props.put("BUYER",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("REJECTED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("CANCELED",new Variant(false));
        props.put("GROSS_AMOUNT",new Variant(0));
        props.put("STATUS",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
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
        props.put("APPROVAL_STATUS",new Variant(""));
        //Create a new object for rights collection
        colLineItems=new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsIndent=Stmt.executeQuery("SELECT * FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND INDENT_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INDENT_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY INDENT_NO");
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
            rsIndent.close();
            
        }
        catch(Exception e) {
            
        }
        
        
    }
    
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsIndent.first();
            setData();
            LastPosition=rsIndent.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if(rsIndent.isAfterLast()||rsIndent.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsIndent.last();
            }
            else {
                rsIndent.next();
                if(rsIndent.isAfterLast()) {
                    rsIndent.last();
                }
            }
            setData();
            LastPosition=rsIndent.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean MovePrevious() {
        try {
            if(rsIndent.isFirst()||rsIndent.isBeforeFirst()) {
                rsIndent.first();
            }
            else {
                rsIndent.previous();
                if(rsIndent.isBeforeFirst()) {
                    rsIndent.first();
                }
            }
            setData();
            LastPosition=rsIndent.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsIndent.last();
            setData();
            LastPosition=rsIndent.getRow();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean Insert(String pPrefix,String pDocno) {
        Statement stHistory,stHDetail,stTmp,stItemDetail,stHItemDetail;
        ResultSet rsHistory,rsHDetail,rsTmp,rsItemDetail,rsHItemDetail;
        Statement stHeader;
        ResultSet rsHeader;
        String MRNo="",strSQL="";
        int MRSrNo=0;
        
        try {
            ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            String gMaxID = pPrefix.trim()+ pDocno.trim();
            
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
            
            
            // Update the Stock only after Final Approval //
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
            //================= Check the qty against MR if it is inserted from MR =================//
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                MRNo=(String)ObjItem.getAttribute("MR_NO").getObj();
                MRSrNo=(int)ObjItem.getAttribute("MR_SR_NO").getVal();
                
                
                //=========== Check Allocation =====================//
                //                String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                //                double AvailableQty=clsItem.getAvailableQty(EITLERPGLOBAL.gCompanyID, ItemID);
                //                double AllocatedQty=ObjItem.getAttribute("ALLOCATED_QTY").getVal();
                //
                //                if(AllocatedQty>AvailableQty) {
                //                    LastError="There is no quantity available for item "+ItemID+" for allocation.\nYou can allocate quantity upto "+AvailableQty;
                //                    return false;
                //                }
                //============ Allocation check over =============//
                
                double MRQty=0;
                double PrevQty=0; //Previously Entered Qty against MIR
                double CurrentQty=0; //Currently entered Qty.
                
                if((!MRNo.trim().equals(""))&&(MRSrNo>0)) {
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
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MR_NO='"+MRNo+"' AND MR_SR_NO="+MRSrNo+" AND INDENT_NO NOT IN(SELECT INDENT_NO FROM D_INV_INDENT_HEADER WHERE CANCELED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
                    }
                    
                    double AllocatedQty=0;
                    CurrentQty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3)+EITLERPGLOBAL.round(AllocatedQty,3);
                    
                    if((CurrentQty+PrevQty) > MRQty) //If total Qty exceeds MIR Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds PR No. "+MRNo+" Sr. No. "+MRSrNo+" qty "+MRQty+". Please verify the input.";
                        return false;
                    }
                    
                }
            }
            
            
            //--------- Generate New Indent No. ------------
            setAttribute("INDENT_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,3, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            
            //======== Store the Physical File into database =====//
            long DocID=0;
            
            if(!getAttribute("FILENAME").getObj().toString().trim().equals("")) {
                if(getAttribute("REFRESH_FILE").getBool()) {
                    String FileName=getAttribute("FILENAME").getObj().toString();
                    File f=new File(FileName);
                    
                    if(f.exists()) {
                        DocID=clsDocument.StoreDocument(EITLERPGLOBAL.gCompanyID,((String)getAttribute("INDENT_NO").getObj()),"", FileName);
                    }
                }
            }
            //====================================================//
            
            //-------- Allocation -------------//
            if(AStatus.equals("F")) {
                for(int i=1;i<=colLineItems.size();i++) {
                    clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                    
                    double Qty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                    double AllocatedQty=EITLERPGLOBAL.round(ObjItem.getAttribute("ALLOCATED_QTY").getVal(),3);
                    double StockQty=clsItem.getOnHandQty(EITLERPGLOBAL.gCompanyID,(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                    MRNo=(String)ObjItem.getAttribute("MR_NO").getObj();
                    MRSrNo=(int)ObjItem.getAttribute("MR_SR_NO").getVal();
                    
                    
                    // Update Parent Table
                    //data.Execute("UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY="+AllocatedQty+",INDENT_QTY=INDENT_QTY+"+(Qty+AllocatedQty)+",BAL_QTY=REQ_QTY-(INDENT_QTY+ALLOCATED_QTY),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo.trim()+"' AND SR_NO="+MRSrNo);
                    data.Execute("UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY="+AllocatedQty+",REQ_QTY="+Qty+",INDENT_QTY=REQ_QTY,BAL_QTY=REQ_QTY-INDENT_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo.trim()+"' AND SR_NO="+MRSrNo);
                    data.Execute("UPDATE D_INV_REQ_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo.trim()+"'");
                    
                    //======= Now allocating the quantity for selected deparment =======//
                    clsAllocation ObjAllocation=new clsAllocation();
                    
                    ObjAllocation.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjAllocation.setAttribute("ITEM_ID",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                    ObjAllocation.setAttribute("STOCK_QTY",EITLERPGLOBAL.round(StockQty,3));
                    ObjAllocation.setAttribute("ALLOCATED_QTY",EITLERPGLOBAL.round(AllocatedQty,3));
                    ObjAllocation.setAttribute("DEPT_ID",(int)getAttribute("FOR_DEPT_ID").getVal());
                    ObjAllocation.setAttribute("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                    ObjAllocation.setAttribute("INDENT_SR_NO",i);
                    ObjAllocation.setAttribute("STATUS","O");
                    
                    ObjAllocation.doAllocation();
                    //=================================================================//
                }
            }
            
            
            if(AStatus.equals("F")) {
                
                //=========================================================================================//
                //    Update the Redundent Item History while final approval
                //========================================================================================//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                    
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    
                    double PendInspQty=clsMIRRaw.getTotalPendingGRNQty(EITLERPGLOBAL.gCompanyID, ItemID);
                    double PendIndentQty=clsIndent.getTotalPendingIndentQty(EITLERPGLOBAL.gCompanyID,  ItemID);
                    double PendPOQty=clsPOGen.getTotalPendingQty(EITLERPGLOBAL.gCompanyID, ItemID);
                    double AAQty=clsIndent.getTotalAlreadyApprovedQty(EITLERPGLOBAL.gCompanyID, ItemID);
                    
                    String LastGRNNo=clsGRN.getLastGRNNo(EITLERPGLOBAL.gCompanyID, ItemID,true);
                    
                    
                    ObjItem.setAttribute("LAST_GRN_NO",LastGRNNo);
                    
                    
                    stTmp=Conn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_NO='"+LastGRNNo+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        
                        ObjItem.setAttribute("LAST_GRN_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("GRN_DATE")));
                    }
                    
                    stTmp=Conn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT QTY,LANDED_RATE FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_NO='"+LastGRNNo+"' AND ITEM_ID='"+ItemID+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        ObjItem.setAttribute("LAST_GRN_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3));
                        ObjItem.setAttribute("LAST_GRN_RATE",EITLERPGLOBAL.round(rsTmp.getDouble("LANDED_RATE"),3));
                        ObjItem.setAttribute("RATE",EITLERPGLOBAL.round(rsTmp.getDouble("LANDED_RATE"),3));
                    }
                    
                    
                    String LastPONo=clsPOGen.getLastPObyItem(EITLERPGLOBAL.gCompanyID,ItemID,LastGRNNo);
                    
                    
                    ObjItem.setAttribute("PEND_INSP_QTY",EITLERPGLOBAL.round(PendInspQty,3));
                    ObjItem.setAttribute("PEND_INDENT_QTY",EITLERPGLOBAL.round(PendIndentQty,3));
                    ObjItem.setAttribute("PEND_PO_QTY",EITLERPGLOBAL.round(PendPOQty,3));
                    ObjItem.setAttribute("LAST_PO_NO",LastPONo);
                    ObjItem.setAttribute("AA_INDENT_QTY",EITLERPGLOBAL.round(AAQty,3));
                    
                    stTmp=Conn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+LastPONo+"' ");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        ObjItem.setAttribute("LAST_PO_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("PO_DATE")));
                        ObjItem.setAttribute("LAST_SUPP_ID",rsTmp.getString("SUPP_ID"));
                        ObjItem.setAttribute("LAST_SUPP_NAME",clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,rsTmp.getString("SUPP_ID")));
                    }
                    else {
                        ObjItem.setAttribute("LAST_PO_DATE","0000-00-00");
                        ObjItem.setAttribute("LAST_SUPP_ID","");
                        ObjItem.setAttribute("LAST_SUPP_NAME","");
                    }
                    
                    stTmp=Conn.createStatement();
                    rsTmp=stTmp.executeQuery("SELECT SUM(QTY) AS THEQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+LastPONo+"' AND ITEM_ID='"+ItemID+"'");
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        ObjItem.setAttribute("LAST_PO_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("THEQTY"),3));
                    }
                }
                //===========================================================================================//
                
            }
            //=================Updation of MR Completed =========================//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_INDENT_HEADER WHERE INDENT_NO='1'");
            //rsHeader.first();
            
            
            rsIndent.moveToInsertRow();
            
            rsIndent.updateLong("DOC_ID",DocID);
            rsIndent.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsIndent.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            
            rsIndent.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsIndent.updateString("INDENT_NO",(String) getAttribute("INDENT_NO").getObj());
            rsIndent.updateString("INDENT_DATE",(String) getAttribute("INDENT_DATE").getObj());
            rsIndent.updateString("INDENT_TYPE",(String) getAttribute("INDENT_TYPE").getObj());
            rsIndent.updateLong("FOR_UNIT",(long) getAttribute("FOR_UNIT").getVal());
            rsIndent.updateLong("FOR_DEPT_ID",(long) getAttribute("FOR_DEPT_ID").getVal());
            rsIndent.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsIndent.updateLong("BUYER",(long) getAttribute("BUYER").getVal());
            rsIndent.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsIndent.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsIndent.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
            rsIndent.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsIndent.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            //rsIndent.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            //rsIndent.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsIndent.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getVal(),3));
            rsIndent.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsIndent.updateBoolean("CHANGED",true);
            rsIndent.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsIndent.updateBoolean("APPROVED",false);
            rsIndent.updateString("APPROVED_DATE","0000-00-00");
            rsIndent.updateBoolean("REJECTED",false);
            rsIndent.updateString("REJECTED_DATE","0000-00-00");
            rsIndent.updateBoolean("CANCELED",false);
            rsIndent.insertRow();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("DOC_ID",DocID);
            rsHistory.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsHistory.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
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
            //rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            //rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getVal(),3));
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.insertRow();
            
            
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID=" + Long.toString(gCompanyID)+" AND INDENT_NO='1'");
            String nIndentno=(String) getAttribute("INDENT_NO").getObj();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            
            for(int l=1;l<=colItemDetail.size();l++) {
                clsIndentItemDetail ObjLot=(clsIndentItemDetail)colItemDetail.get(Integer.toString(l));
                
                rsItemDetail.moveToInsertRow();
                rsItemDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItemDetail.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                rsItemDetail.updateInt("INDENT_SR_NO",0);
                rsItemDetail.updateInt("SR_NO",l);
                rsItemDetail.updateString("POSITION_DESC",(String)ObjLot.getAttribute("POSITION_DESC").getObj());
                rsItemDetail.updateString("POSITION_NO",(String)ObjLot.getAttribute("POSITION_NO").getObj());
                rsItemDetail.updateString("CARD_NO",(String)ObjLot.getAttribute("CARD_NO").getObj());
                rsItemDetail.updateString("NAME",(String)ObjLot.getAttribute("NAME").getObj());
                rsItemDetail.updateString("SHOE_SIZE",(String)ObjLot.getAttribute("SHOE_SIZE").getObj());
                rsItemDetail.updateBoolean("GIVEN_LAST_YEAR",ObjLot.getAttribute("GIVEN_LAST_YEAR").getBool());
                rsItemDetail.updateString("REMARKS",(String)ObjLot.getAttribute("REMARKS").getObj());
                rsItemDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                rsItemDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                //rsItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                //rsItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItemDetail.updateInt("NO_ELIGIBLE",(int)ObjLot.getAttribute("NO_ELIGIBLE").getVal());
                rsItemDetail.updateInt("NO_LAST_YEAR",(int)ObjLot.getAttribute("NO_LAST_YEAR").getVal());
                rsItemDetail.updateInt("NO_THIS_YEAR",(int)ObjLot.getAttribute("NO_THIS_YEAR").getVal());
                rsItemDetail.updateBoolean("CHANGED",true);
                rsItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItemDetail.insertRow();
                
                rsHItemDetail.moveToInsertRow();
                rsHItemDetail.updateInt("REVISION_NO",1);
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
                //rsHItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                //rsHItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHItemDetail.updateBoolean("CHANGED",true);
                rsHItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHItemDetail.insertRow();
            }
            
            
            
            // Inserting records in Indent Details
            for(int i=1;i<=colLineItems.size();i++) {
                clsIndentItem ObjItem=(clsIndentItem) colLineItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("INDENT_NO",nIndentno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("MR_NO",(String) ObjItem.getAttribute("MR_NO").getObj());
                rsTmp.updateLong("MR_SR_NO",(long) ObjItem.getAttribute("MR_SR_NO").getVal());
                rsTmp.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsTmp.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsTmp.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsTmp.updateDouble("PO_QTY",0);
                rsTmp.updateDouble("STOCK_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("STOCK_QTY").getDouble(),3));
                rsTmp.updateDouble("ALLOCATED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("ALLOCATED_QTY").getDouble(),3));
                rsTmp.updateDouble("RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("RATE").getVal(),3));
                rsTmp.updateString("REQUIRED_DATE",(String) ObjItem.getAttribute("REQUIRED_DATE").getObj());
                rsTmp.updateLong("NET_AMT",(long) ObjItem.getAttribute("NET_AMT").getVal());
                rsTmp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                //rsTmp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateString("LAST_SUPP_ID",(String)ObjItem.getAttribute("LAST_SUPP_ID").getObj());
                rsTmp.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsTmp.updateDouble("PEND_INSP_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("PEND_INSP_QTY").getVal(),3));
                rsTmp.updateDouble("PEND_INDENT_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("PEND_INDENT_QTY").getVal(),3));
                rsTmp.updateDouble("PEND_PO_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("PEND_PO_QTY").getVal(),3));
                rsTmp.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                System.out.println("insert"+(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsTmp.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsTmp.updateDouble("LAST_PO_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("LAST_PO_QTY").getVal(),3));
                rsTmp.updateString("LAST_GRN_NO",(String)ObjItem.getAttribute("LAST_GRN_NO").getObj());
                System.out.println("insert"+(String)ObjItem.getAttribute("LAST_GRN_DATE").getObj());
                rsTmp.updateString("LAST_GRN_DATE",(String)ObjItem.getAttribute("LAST_GRN_DATE").getObj());
                rsTmp.updateDouble("LAST_GRN_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("LAST_GRN_QTY").getVal(),3));
                rsTmp.updateDouble("LAST_GRN_RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("LAST_GRN_RATE").getVal(),3));
                rsTmp.updateDouble("AA_INDENT_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("AA_INDENT_QTY").getVal(),3));
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("INDENT_NO",nIndentno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("MR_NO",(String) ObjItem.getAttribute("MR_NO").getObj());
                rsHDetail.updateLong("MR_SR_NO",(long) ObjItem.getAttribute("MR_SR_NO").getVal());
                rsHDetail.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsHDetail.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3));
                rsHDetail.updateDouble("PO_QTY",0);
                rsHDetail.updateDouble("STOCK_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("STOCK_QTY").getDouble(),3));
                rsHDetail.updateDouble("ALLOCATED_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("ALLOCATED_QTY").getDouble(),3));
                rsHDetail.updateDouble("RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("RATE").getVal(),3));
                rsHDetail.updateString("REQUIRED_DATE",(String) ObjItem.getAttribute("REQUIRED_DATE").getObj());
                rsHDetail.updateLong("NET_AMT",(long) ObjItem.getAttribute("NET_AMT").getVal());
                rsHDetail.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                //rsHDetail.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                //rsHDetail.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("LAST_SUPP_ID",(String)ObjItem.getAttribute("LAST_SUPP_ID").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("PEND_INSP_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("PEND_INSP_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_INDENT_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("PEND_INDENT_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_PO_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("PEND_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_PO_NO",(String)ObjItem.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjItem.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_PO_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("LAST_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_GRN_NO",(String)ObjItem.getAttribute("LAST_GRN_NO").getObj());
                rsHDetail.updateString("LAST_GRN_DATE",(String)ObjItem.getAttribute("LAST_GRN_DATE").getObj());
                rsHDetail.updateDouble("LAST_GRN_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("LAST_GRN_QTY").getVal(),3));
                rsHDetail.updateDouble("LAST_GRN_RATE",EITLERPGLOBAL.round(ObjItem.getAttribute("LAST_GRN_RATE").getVal(),3));
                rsHDetail.updateDouble("AA_INDENT_QTY",EITLERPGLOBAL.round(ObjItem.getAttribute("AA_INDENT_QTY").getVal(),3));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
                for(int l=1;l<=ObjItem.colItemDetail.size();l++) {
                    clsIndentItemDetail ObjLot=(clsIndentItemDetail)ObjItem.colItemDetail.get(Integer.toString(l));
                    
                    rsItemDetail.moveToInsertRow();
                    rsItemDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsItemDetail.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                    rsItemDetail.updateInt("INDENT_SR_NO",i);
                    rsItemDetail.updateInt("SR_NO",l);
                    rsItemDetail.updateString("POSITION_DESC",(String)ObjLot.getAttribute("POSITION_DESC").getObj());
                    rsItemDetail.updateString("POSITION_NO",(String)ObjLot.getAttribute("POSITION_NO").getObj());
                    rsItemDetail.updateString("CARD_NO",(String)ObjLot.getAttribute("CARD_NO").getObj());
                    rsItemDetail.updateString("NAME",(String)ObjLot.getAttribute("NAME").getObj());
                    rsItemDetail.updateString("SHOE_SIZE",(String)ObjLot.getAttribute("SHOE_SIZE").getObj());
                    rsItemDetail.updateBoolean("GIVEN_LAST_YEAR",ObjLot.getAttribute("GIVEN_LAST_YEAR").getBool());
                    rsItemDetail.updateString("REMARKS",(String)ObjLot.getAttribute("REMARKS").getObj());
                    rsItemDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                    rsItemDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    //rsItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                    //rsItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsItemDetail.updateInt("NO_ELIGIBLE",(int)ObjLot.getAttribute("NO_ELIGIBLE").getVal());
                    rsItemDetail.updateInt("NO_LAST_YEAR",(int)ObjLot.getAttribute("NO_LAST_YEAR").getVal());
                    rsItemDetail.updateInt("NO_THIS_YEAR",(int)ObjLot.getAttribute("NO_THIS_YEAR").getVal());
                    rsItemDetail.updateBoolean("CHANGED",true);
                    rsItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsItemDetail.insertRow();
                    
                    rsHItemDetail.moveToInsertRow();
                    rsHItemDetail.updateInt("REVISION_NO",1);
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
                    //rsHItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                    //rsHItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsHItemDetail.updateBoolean("CHANGED",true);
                    rsHItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHItemDetail.insertRow();
                }
            }
            
            
            //============== Update the Approval Flow ====================//
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=3; //Indent
            ObjFlow.DocNo=(String)getAttribute("INDENT_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_INDENT_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="INDENT_NO";
            
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
            //================ Approval Flow Update complete ===============//
            
            
            //=== special addition ----------//
            //Decide based on the Item ID whether this indent should be floated
            //to E.D. or M.D. or Audit can final approve it.
            // Inserting records in Indent  Details
            
            boolean MDApprovalRequired=false;
            boolean EDApprovalRequired=false;
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsIndentItem ObjItem=(clsIndentItem) colLineItems.get(Integer.toString(i));
                String theItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                
                
                String Authority=clsItemCriteria.getApprovalAuthority(theItemID, (int)getAttribute("FOR_DEPT_ID").getVal());
                
                
                if(Authority.trim().equals("E")) {
                    EDApprovalRequired=true;
                }
                
                if(Authority.trim().equals("M")) {
                    MDApprovalRequired=true;
                }
            }
            
            HashMap NewUsers=new HashMap();
            
            //Check whether this user has been already added to the hierarchy.
            rsTmp=data.getResult("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND HIERARCHY_ID="+(int)getAttribute("HIERARCHY_ID").getVal()+" AND USER_ID=11");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
            }
            else {
                if(EDApprovalRequired) {
                    clsHierarchy ObjUser=new clsHierarchy();
                    ObjUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjUser.setAttribute("MODULE_ID",3);
                    ObjUser.setAttribute("DOC_NO",(String)getAttribute("INDENT_NO").getObj());
                    ObjUser.setAttribute("DOC_DATE",(String)getAttribute("INDENT_DATE").getObj());
                    ObjUser.setAttribute("USER_ID",11); //11 - for Executive Director
                    NewUsers.put(Integer.toString(NewUsers.size()+1),ObjUser);
                }
            }
            
            
            //Check whether this user has been already added to the hierarchy.
            rsTmp=data.getResult("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND HIERARCHY_ID="+(int)getAttribute("HIERARCHY_ID").getVal()+" AND USER_ID=72");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                
            }
            else {
                
                if(MDApprovalRequired) {
                    clsHierarchy ObjUser=new clsHierarchy();
                    ObjUser.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    ObjUser.setAttribute("MODULE_ID",3);
                    ObjUser.setAttribute("DOC_NO",(String)getAttribute("INDENT_NO").getObj());
                    ObjUser.setAttribute("DOC_DATE",(String)getAttribute("INDENT_DATE").getObj());
                    ObjUser.setAttribute("USER_ID",72); //72 - for Managing Director
                    NewUsers.put(Integer.toString(NewUsers.size()+1),ObjUser);
                    
                    //Remove the ED's Username from the Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+(String)getAttribute("INDENT_NO").getObj()+"' AND USER_ID=11");
                }
            }
            ApprovalFlow.AppendUsers(NewUsers);
            //==============================//
            
            
            // Updating Module = 3 in First Free Number
            ResultSet rsFree;
            Statement StmtFree;
            StmtFree = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSql = "UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+pDocno+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID=3 AND PREFIX_CHARS='"+pPrefix.trim()+"'";
            StmtFree.executeUpdate(strSql);
            
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
        Statement stHistory,stHDetail,stTmp,stItemDetail,stHItemDetail;
        ResultSet rsHistory,rsHDetail,rsTmp,rsItemDetail,rsHItemDetail;
        Statement stHeader;
        ResultSet rsHeader;
        String MRNo="",strSQL="";
        int MRSrNo=0;
        int OldHierarchy=0;
        boolean Validate=true;
        
        try {
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }
            
            Validate=true;
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
            
            if(Validate) {
                rsTmp=data.getResult("SELECT HIERARCHY_ID FROM D_INV_INDENT_HEADER WHERE INDENT_NO='"+theDocNo+"'");
                if(rsTmp.getRow()>0) {
                    OldHierarchy=rsTmp.getInt("HIERARCHY_ID");
                }
                
                //================= Check the qty against MR if it is inserted from MR =================//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                    
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    MRNo=(String)ObjItem.getAttribute("MR_NO").getObj();
                    MRSrNo=(int)ObjItem.getAttribute("MR_SR_NO").getVal();
                    
                    double MRQty=0;
                    double PrevQty=0; //Previously Entered Qty against MIR
                    double CurrentQty=0; //Currently entered Qty.
                    
                    //=========== Check Allocation =====================//
                    //                String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    //                double AvailableQty=clsItem.getAvailableQty(EITLERPGLOBAL.gCompanyID, ItemID);
                    //                double AllocatedQty=ObjItem.getAttribute("ALLOCATED_QTY").getVal();
                    //
                    //                if(AllocatedQty>AvailableQty) {
                    //                    LastError="There is no quantity available for item "+ItemID+" for allocation.\nYou can allocate quantity upto "+AvailableQty;
                    //                    return false;
                    //                }
                    //============ Allocation check over =============//
                    
                    
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
                            PrevQty=EITLERPGLOBAL.round(rsTmp.getDouble("SUMQTY"),3);
                        }
                        
                        double AllocatedQty=0;
                        CurrentQty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal()+AllocatedQty,3);
                        
                        if((CurrentQty+PrevQty) > MRQty) //If total Qty exceeds MIR Qty. Do not allow
                        {
                            
                            if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,499)) {
                                //Allow to increase the qty.
                            }
                            else {
                                LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds PR No. "+MRNo+" Sr. No. "+MRSrNo+" qty "+MRQty+". Please verify the input.";
                                return false;
                            }
                        }
                        
                    }
                }
                
                
                // Update the Stock only after Final Approval //
                
                
                //-------- First Update the stock -------------//
                if(AStatus.equals("F")) {
                    for(int i=1;i<=colLineItems.size();i++) {
                        clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                        
                        MRNo=ObjItem.getAttribute("MR_NO").getString();
                        MRSrNo=ObjItem.getAttribute("MR_SR_NO").getInt();
                        double Qty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getDouble(),3);
                        double AllocatedQty=EITLERPGLOBAL.round(ObjItem.getAttribute("ALLOCATED_QTY").getDouble(),3);
                        double StockQty=EITLERPGLOBAL.round(clsItem.getOnHandQty(EITLERPGLOBAL.gCompanyID,(String)ObjItem.getAttribute("ITEM_CODE").getObj()),3);
                        
                        
                        // Update GRN Received Qty
                        //data.Execute("UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY="+AllocatedQty+",INDENT_QTY=INDENT_QTY+"+(Qty+AllocatedQty)+",BAL_QTY=REQ_QTY-(INDENT_QTY+ALLOCATED_QTY),CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo.trim()+"' AND SR_NO="+MRSrNo);
                        data.Execute("UPDATE D_INV_REQ_DETAIL SET ALLOCATED_QTY="+AllocatedQty+",REQ_QTY="+Qty+",INDENT_QTY=REQ_QTY,BAL_QTY=REQ_QTY-INDENT_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo.trim()+"' AND SR_NO="+MRSrNo);
                        data.Execute("UPDATE D_INV_REQ_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND REQ_NO='"+MRNo.trim()+"'");
                        
                        //======= Now allocating the quantity for selected deparment =======//
                        clsAllocation ObjAllocation=new clsAllocation();
                        
                        ObjAllocation.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                        ObjAllocation.setAttribute("ITEM_ID",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                        ObjAllocation.setAttribute("STOCK_QTY",EITLERPGLOBAL.round(StockQty,3));
                        ObjAllocation.setAttribute("ALLOCATED_QTY",EITLERPGLOBAL.round(AllocatedQty,3));
                        ObjAllocation.setAttribute("DEPT_ID",(int)getAttribute("FOR_DEPT_ID").getVal());
                        ObjAllocation.setAttribute("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                        ObjAllocation.setAttribute("INDENT_SR_NO",i);
                        ObjAllocation.setAttribute("STATUS","O");
                        
                        ObjAllocation.doAllocation();
                        //=================================================================//
                        
                    }
                }
                
                
                if(AStatus.equals("F")) {
                    
                    //=========================================================================================//
                    //    Update the Redundent Item History while final approval
                    //========================================================================================//
                    for(int i=1;i<=colLineItems.size();i++) {
                        clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                        
                        String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                        
                        double PendInspQty=EITLERPGLOBAL.round(clsMIRRaw.getTotalPendingGRNQty(EITLERPGLOBAL.gCompanyID, ItemID),3);
                        double PendIndentQty=EITLERPGLOBAL.round(clsIndent.getTotalPendingIndentQty(EITLERPGLOBAL.gCompanyID,  ItemID),3);
                        double PendPOQty=EITLERPGLOBAL.round(clsPOGen.getTotalPendingQty(EITLERPGLOBAL.gCompanyID, ItemID),3);
                        double AAQty=EITLERPGLOBAL.round(clsIndent.getTotalAlreadyApprovedQty(EITLERPGLOBAL.gCompanyID, ItemID),3);
                        
                        String LastGRNNo=clsGRN.getLastGRNNo(EITLERPGLOBAL.gCompanyID, ItemID,true);
                        
                        
                        ObjItem.setAttribute("LAST_GRN_NO",LastGRNNo);
                        
                        
                        stTmp=Conn.createStatement();
                        rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_NO='"+LastGRNNo+"'");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            
                            ObjItem.setAttribute("LAST_GRN_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("GRN_DATE")));
                        }
                        
                        stTmp=Conn.createStatement();
                        rsTmp=stTmp.executeQuery("SELECT QTY,LANDED_RATE FROM D_INV_GRN_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND GRN_NO='"+LastGRNNo+"' AND ITEM_ID='"+ItemID+"'");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            ObjItem.setAttribute("LAST_GRN_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("QTY"),3));
                            ObjItem.setAttribute("LAST_GRN_RATE",EITLERPGLOBAL.round(rsTmp.getDouble("LANDED_RATE"),3));
                            ObjItem.setAttribute("RATE",EITLERPGLOBAL.round(rsTmp.getDouble("LANDED_RATE"),3));
                        }
                        
                        
                        String LastPONo=clsPOGen.getLastPObyItem(EITLERPGLOBAL.gCompanyID,ItemID,LastGRNNo);
                        
                        
                        ObjItem.setAttribute("PEND_INSP_QTY",EITLERPGLOBAL.round(PendInspQty,3));
                        ObjItem.setAttribute("PEND_INDENT_QTY",EITLERPGLOBAL.round(PendIndentQty,3));
                        ObjItem.setAttribute("PEND_PO_QTY",EITLERPGLOBAL.round(PendPOQty,3));
                        ObjItem.setAttribute("LAST_PO_NO",LastPONo);
                        ObjItem.setAttribute("AA_INDENT_QTY",EITLERPGLOBAL.round(AAQty,3));
                        
                        stTmp=Conn.createStatement();
                        rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_PO_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+LastPONo+"' ");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            System.out.println("update1"+EITLERPGLOBAL.formatDate(rsTmp.getString("PO_DATE")));
                            ObjItem.setAttribute("LAST_PO_DATE",EITLERPGLOBAL.formatDate(rsTmp.getString("PO_DATE")));
                            ObjItem.setAttribute("LAST_SUPP_ID",rsTmp.getString("SUPP_ID"));
                            ObjItem.setAttribute("LAST_SUPP_NAME",clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID,rsTmp.getString("SUPP_ID")));
                        }
                        else {
                            System.out.println("update1"+"0000-00-00");
                            ObjItem.setAttribute("LAST_PO_DATE","0000-00-00");
                            ObjItem.setAttribute("LAST_SUPP_ID","");
                            ObjItem.setAttribute("LAST_SUPP_NAME","");
                        }
                        
                        stTmp=Conn.createStatement();
                        rsTmp=stTmp.executeQuery("SELECT SUM(QTY) AS THEQTY FROM D_PUR_PO_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PO_NO='"+LastPONo+"' AND ITEM_ID='"+ItemID+"'");
                        rsTmp.first();
                        
                        if(rsTmp.getRow()>0) {
                            ObjItem.setAttribute("LAST_PO_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("THEQTY"),3));
                        }
                    }
                    //===========================================================================================//
                    
                }
                //=================Updation of MR Completed =========================//
            }
            
            
            
            
            //======== Store the Physical File into database =====//
            long DocID=(long)getAttribute("DOC_ID").getVal() ;
            
            if(DocID==0) {
                if(!getAttribute("FILENAME").getObj().toString().trim().equals("")) {
                    if(getAttribute("REFRESH_FILE").getBool()) {
                        String FileName=getAttribute("FILENAME").getObj().toString();
                        File f=new File(FileName);
                        
                        if(f.exists()) {
                            DocID=clsDocument.StoreDocument(EITLERPGLOBAL.gCompanyID,((String)getAttribute("INDENT_NO").getObj()),"", FileName);
                        }
                    }
                }
            }
            else {
                if(!getAttribute("FILENAME").getObj().toString().trim().equals("")) {
                    if(getAttribute("REFRESH_FILE").getBool()) {
                        String FileName=getAttribute("FILENAME").getObj().toString();
                        File f=new File(FileName);
                        
                        if(f.exists()) {
                            clsDocument.UpdateDocument(DocID,FileName);
                        }
                    }
                }
            }
            //====================================================//
            
            rsIndent.updateLong("DOC_ID",DocID);
            rsIndent.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsIndent.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            
            rsIndent.updateString("INDENT_TYPE",(String) getAttribute("INDENT_TYPE").getObj());
            rsIndent.updateLong("FOR_UNIT",(long) getAttribute("FOR_UNIT").getVal());
            rsIndent.updateLong("FOR_DEPT_ID",(long) getAttribute("FOR_DEPT_ID").getVal());
            rsIndent.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsIndent.updateLong("BUYER",(long) getAttribute("BUYER").getVal());
            rsIndent.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsIndent.updateBoolean("CANCELED",(boolean) getAttribute("CANCELED").getBool());
        //    rsIndent.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
        //    rsIndent.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsIndent.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsIndent.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsIndent.updateLong("GROSS_AMOUNT",(long) getAttribute("GROSS_AMOUNT").getVal());
            rsIndent.updateBoolean("CHANGED",true);
            rsIndent.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsIndent.updateBoolean("CANCELED",false);
            rsIndent.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsIndent.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_INDENT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+(String)getAttribute("INDENT_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("INDENT_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateLong("DOC_ID",DocID);
            rsHistory.updateBoolean("ATTACHEMENT",getAttribute("ATTACHEMENT").getBool());
            rsHistory.updateString("ATTACHEMENT_PATH",(String)getAttribute("ATTACHEMENT_PATH").getObj());
            
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
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
            //rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateDouble("GROSS_AMOUNT",EITLERPGLOBAL.round(getAttribute("GROSS_AMOUNT").getDouble(),3));
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            //RollBack will just update linked tables and make entries as it is
            //rollback();
            
            data.Execute("DELETE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+RevDocNo+"'");
            data.Execute("DELETE FROM D_INV_INDENT_ITEM_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+RevDocNo+"'");
            
             System.out.println("A1");
             
            
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID=" + Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INDENT_NO='1'");
            String nIndentno=(String) getAttribute("INDENT_NO").getObj();
            //long nUserID=(long)getAttribute("USER_ID").getVal();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
             System.out.println("A2");
            
            for(int l=1;l<=colItemDetail.size();l++) {
                clsIndentItemDetail ObjLot=(clsIndentItemDetail)colItemDetail.get(Integer.toString(l));
                
                rsItemDetail.moveToInsertRow();
                System.out.println(l);
                rsItemDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                rsItemDetail.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                rsItemDetail.updateInt("INDENT_SR_NO",0);
                rsItemDetail.updateInt("SR_NO",l);
                   System.out.println("A3  " + l);
                rsItemDetail.updateString("POSITION_DESC",(String)ObjLot.getAttribute("POSITION_DESC").getObj());
                rsItemDetail.updateString("POSITION_NO",(String)ObjLot.getAttribute("POSITION_NO").getObj());
                rsItemDetail.updateString("CARD_NO",(String)ObjLot.getAttribute("CARD_NO").getObj());
                rsItemDetail.updateString("NAME",(String)ObjLot.getAttribute("NAME").getObj());
                rsItemDetail.updateString("SHOE_SIZE",(String)ObjLot.getAttribute("SHOE_SIZE").getObj());
                rsItemDetail.updateBoolean("GIVEN_LAST_YEAR",ObjLot.getAttribute("GIVEN_LAST_YEAR").getBool());
                rsItemDetail.updateString("REMARKS",(String)ObjLot.getAttribute("REMARKS").getObj());
                //rsItemDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                //rsItemDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                rsItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsItemDetail.updateInt("NO_ELIGIBLE",(int)ObjLot.getAttribute("NO_ELIGIBLE").getVal());
                rsItemDetail.updateInt("NO_LAST_YEAR",(int)ObjLot.getAttribute("NO_LAST_YEAR").getVal());
                rsItemDetail.updateInt("NO_THIS_YEAR",(int)ObjLot.getAttribute("NO_THIS_YEAR").getVal());
                rsItemDetail.updateBoolean("CHANGED",true);
                rsItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsItemDetail.insertRow();
                
                 
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
                
                 System.out.println("A3  " + l);
            }
            
            
            // Inserting records in Indent Details
            for(int i=1;i<=colLineItems.size();i++) {
                clsIndentItem ObjIndent=(clsIndentItem) colLineItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("INDENT_NO",nIndentno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("MR_NO",(String) ObjIndent.getAttribute("MR_NO").getObj());
                rsTmp.updateInt("MR_SR_NO",(int)ObjIndent.getAttribute("MR_SR_NO").getVal());
                rsTmp.updateString("ITEM_CODE",(String) ObjIndent.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String) ObjIndent.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateLong("UNIT",(long) ObjIndent.getAttribute("UNIT").getVal());
                rsTmp.updateDouble("QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("QTY").getVal(),3));
                rsTmp.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("BAL_QTY").getVal(),3));
                rsTmp.updateDouble("PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PO_QTY").getVal(),3));
                rsTmp.updateDouble("ALLOCATED_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("ALLOCATED_QTY").getVal(),3));
                rsTmp.updateDouble("STOCK_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("STOCK_QTY").getVal(),3));
                rsTmp.updateDouble("RATE",EITLERPGLOBAL.round(ObjIndent.getAttribute("RATE").getVal(),3));
                rsTmp.updateString("REQUIRED_DATE",(String) ObjIndent.getAttribute("REQUIRED_DATE").getObj());
                rsTmp.updateDouble("NET_AMT",EITLERPGLOBAL.round(ObjIndent.getAttribute("NET_AMT").getVal(),3));
                rsTmp.updateBoolean("CANCELED",(boolean) ObjIndent.getAttribute("CANCELED").getBool());
                rsTmp.updateLong("CREATED_BY",(long) ObjIndent.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) ObjIndent.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) ObjIndent.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) ObjIndent.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateString("LAST_SUPP_ID",(String)ObjIndent.getAttribute("LAST_SUPP_ID").getObj());
                rsTmp.updateString("REMARKS",(String)ObjIndent.getAttribute("REMARKS").getObj());
                rsTmp.updateDouble("PEND_INSP_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_INSP_QTY").getVal(),3));
                rsTmp.updateDouble("PEND_INDENT_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_INDENT_QTY").getVal(),3));
                rsTmp.updateDouble("PEND_PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_PO_QTY").getVal(),3));
                rsTmp.updateString("LAST_PO_NO",(String)ObjIndent.getAttribute("LAST_PO_NO").getObj());
                System.out.println("Test");
                System.out.println("Test1 "+(String)ObjIndent.getAttribute("LAST_PO_DATE").getObj());
                rsTmp.updateString("LAST_PO_DATE",(String)ObjIndent.getAttribute("LAST_PO_DATE").getObj());
                //rsTmp.updateString("LAST_PO_DATE",EITLERPGLOBAL.formatDateDB(getAttribute("LAST_PO_DATE").getString()));
              
                //System.out.println("update33 "+EITLERPGLOBAL.formatDateDB(getAttribute("LAST_PO_DATE").getString()));
                
                System.out.println("update2 "+(String)ObjIndent.getAttribute("LAST_PO_DATE").getObj());
                rsTmp.updateDouble("LAST_PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_PO_QTY").getVal(),3));
                rsTmp.updateString("LAST_GRN_NO",(String)ObjIndent.getAttribute("LAST_GRN_NO").getObj());
                System.out.println("update2"+(String)ObjIndent.getAttribute("LAST_GRN_DATE").getObj());
                rsTmp.updateString("LAST_GRN_DATE",(String)ObjIndent.getAttribute("LAST_GRN_DATE").getObj());
                rsTmp.updateDouble("LAST_GRN_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_GRN_QTY").getVal(),3));
                rsTmp.updateDouble("LAST_GRN_RATE",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_GRN_RATE").getVal(),3));
                rsTmp.updateDouble("AA_INDENT_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("AA_INDENT_QTY").getVal(),3));
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("INDENT_NO",nIndentno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("MR_NO",(String) ObjIndent.getAttribute("MR_NO").getObj());
                rsHDetail.updateInt("MR_SR_NO",(int) ObjIndent.getAttribute("MR_SR_NO").getVal());
                rsHDetail.updateString("ITEM_CODE",(String) ObjIndent.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String) ObjIndent.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjIndent.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("QTY").getVal(),3));
                rsHDetail.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("BAL_QTY").getVal(),3));
                rsHDetail.updateDouble("PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PO_QTY").getVal(),3));
                rsHDetail.updateDouble("ALLOCATED_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("ALLOCATED_QTY").getVal(),3));
                rsHDetail.updateDouble("STOCK_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("STOCK_QTY").getVal(),3));
                rsHDetail.updateDouble("RATE",EITLERPGLOBAL.round(ObjIndent.getAttribute("RATE").getVal(),3));
                rsHDetail.updateString("REQUIRED_DATE",(String) ObjIndent.getAttribute("REQUIRED_DATE").getObj());
                rsHDetail.updateDouble("NET_AMT",EITLERPGLOBAL.round(ObjIndent.getAttribute("NET_AMT").getVal(),3));
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjIndent.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY",(long) ObjIndent.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjIndent.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjIndent.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) ObjIndent.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("LAST_SUPP_ID",(String)ObjIndent.getAttribute("LAST_SUPP_ID").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjIndent.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("PEND_INSP_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_INSP_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_INDENT_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_INDENT_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_PO_NO",(String)ObjIndent.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjIndent.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_GRN_NO",(String)ObjIndent.getAttribute("LAST_GRN_NO").getObj());
                rsHDetail.updateString("LAST_GRN_DATE",(String)ObjIndent.getAttribute("LAST_GRN_DATE").getObj());
                rsHDetail.updateDouble("LAST_GRN_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_GRN_QTY").getVal(),3));
                rsHDetail.updateDouble("LAST_GRN_RATE",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_GRN_RATE").getVal(),3));
                rsHDetail.updateDouble("AA_INDENT_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("AA_INDENT_QTY").getVal(),3));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                      System.out.println("A4  " + i);
                rsHDetail.insertRow();
                
                
                for(int l=1;l<=ObjIndent.colItemDetail.size();l++) {
                    clsIndentItemDetail ObjLot=(clsIndentItemDetail)ObjIndent.colItemDetail.get(Integer.toString(l));
                    
                    rsItemDetail.moveToInsertRow();
                    rsItemDetail.updateInt("COMPANY_ID",(int)getAttribute("COMPANY_ID").getVal());
                    rsItemDetail.updateString("INDENT_NO",(String)getAttribute("INDENT_NO").getObj());
                    rsItemDetail.updateInt("INDENT_SR_NO",i);
                    rsItemDetail.updateInt("SR_NO",l);
                    rsItemDetail.updateString("POSITION_DESC",(String)ObjLot.getAttribute("POSITION_DESC").getObj());
                    rsItemDetail.updateString("POSITION_NO",(String)ObjLot.getAttribute("POSITION_NO").getObj());
                    rsItemDetail.updateString("CARD_NO",(String)ObjLot.getAttribute("CARD_NO").getObj());
                    rsItemDetail.updateString("NAME",(String)ObjLot.getAttribute("NAME").getObj());
                    rsItemDetail.updateString("SHOE_SIZE",(String)ObjLot.getAttribute("SHOE_SIZE").getObj());
                    rsItemDetail.updateBoolean("GIVEN_LAST_YEAR",ObjLot.getAttribute("GIVEN_LAST_YEAR").getBool());
                    rsItemDetail.updateString("REMARKS",(String)ObjLot.getAttribute("REMARKS").getObj());
                    rsItemDetail.updateInt("NO_ELIGIBLE",(int)ObjLot.getAttribute("NO_ELIGIBLE").getVal());
                    rsItemDetail.updateInt("NO_LAST_YEAR",(int)ObjLot.getAttribute("NO_LAST_YEAR").getVal());
                    rsItemDetail.updateInt("NO_THIS_YEAR",(int)ObjLot.getAttribute("NO_THIS_YEAR").getVal());
                    rsItemDetail.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
                    rsItemDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsItemDetail.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
                    rsItemDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsItemDetail.updateBoolean("CHANGED",true);
                    rsItemDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsItemDetail.insertRow();
                    
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
                     System.out.println("A5  " + l);
                }
                
            }
            
            if(ApprovalFlow.HierarchyUpdateNeeded(EITLERPGLOBAL.gCompanyID, 3, (String)getAttribute("INDENT_NO").getObj(),(int)getAttribute("HIERARCHY_ID").getVal(),OldHierarchy,EITLERPGLOBAL.gNewUserID,AStatus)) {
                
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DOC_NO='"+(String)getAttribute("INDENT_NO").getObj()+"' AND MODULE_ID=3");
                
                //============== Update the Approval Flow ====================//
                ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
                ObjFlow.ModuleID=3; //Indent
                ObjFlow.DocNo=(String)getAttribute("INDENT_NO").getObj();
                ObjFlow.From=(int)getAttribute("FROM").getVal();
                ObjFlow.To=(int)getAttribute("TO").getVal();
                ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
                ObjFlow.TableName="D_INV_INDENT_HEADER";
                ObjFlow.IsCreator=true;
                ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
                ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
                ObjFlow.FieldName="INDENT_NO";
                
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
                //================ Approval Flow Update complete ===============//
                
                
            }
            else {
                //======== Update the Approval Flow =========
                ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
                ObjFlow.ModuleID=3; //Indent
                ObjFlow.DocNo=(String)getAttribute("INDENT_NO").getObj();
                ObjFlow.From=(int)getAttribute("FROM").getVal();
                ObjFlow.To=(int)getAttribute("TO").getVal();
                ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
                ObjFlow.TableName="D_INV_INDENT_HEADER";
                ObjFlow.IsCreator=false;
                ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
                ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
                ObjFlow.FieldName="INDENT_NO";
                ObjFlow.ExplicitSendTo=false;
                
                //==== Handling Rejected Documents ==========//
                if(AStatus.equals("R")) {
                    //Remove the Rejected Flag First
                    //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                    //Remove Old Records from D_COM_DOC_DATA
                    //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                    
                    //ObjFlow.IsCreator=true;
                    ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                    ObjFlow.ExplicitSendTo=true;
                }
                //==========================================//
                
                
                if(ObjFlow.Status.equals("H")) {
                    //Do nothing
                    if(AStatus.equals("R")) {
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
            }
            
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    
    public boolean Amend() {
        Statement stHistory,stHDetail,stTmp,stItemDetail,stHItemDetail;
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
                clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                
                
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
                
                if(Qty<ExQty) {
                    LastError="Qty cannot be decreased to "+Qty+". It can be short closed upto "+ExQty;
                    return false;
                }
                
            }
            //=========================================//
            
            
            //=======Short Close Indent ===========//
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsIndentItem ObjItem=(clsIndentItem)colLineItems.get(Integer.toString(i));
                
                double Qty=EITLERPGLOBAL.round(ObjItem.getAttribute("QTY").getVal(),3);
                double POQty=EITLERPGLOBAL.round(ObjItem.getAttribute("PO_QTY").getVal(),3);
                // Below program Modified by ASHUTOSH on 03-01-2019
                if(POQty<0){
                    continue;
                }
                // --------End of ASHUTOSH program
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
                clsIndentItemDetail ObjLot=(clsIndentItemDetail)colItemDetail.get(Integer.toString(l));
                
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
                clsIndentItem ObjIndent=(clsIndentItem) colLineItems.get(Integer.toString(i));
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("INDENT_NO",nIndentno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("MR_NO",(String) ObjIndent.getAttribute("MR_NO").getObj());
                rsHDetail.updateInt("MR_SR_NO",(int) ObjIndent.getAttribute("MR_SR_NO").getVal());
                rsHDetail.updateString("ITEM_CODE",(String) ObjIndent.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String) ObjIndent.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjIndent.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("QTY").getVal(),3));
                rsHDetail.updateDouble("BAL_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("BAL_QTY").getVal(),3));
                rsHDetail.updateDouble("PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PO_QTY").getVal(),3));
                rsHDetail.updateDouble("ALLOCATED_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("ALLOCATED_QTY").getVal(),3));
                rsHDetail.updateDouble("STOCK_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("STOCK_QTY").getVal(),3));
                rsHDetail.updateDouble("RATE",EITLERPGLOBAL.round(ObjIndent.getAttribute("RATE").getVal(),3));
                rsHDetail.updateString("REQUIRED_DATE",(String) ObjIndent.getAttribute("REQUIRED_DATE").getObj());
                rsHDetail.updateDouble("NET_AMT",EITLERPGLOBAL.round(ObjIndent.getAttribute("NET_AMT").getVal(),3));
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjIndent.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY",(long) ObjIndent.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjIndent.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjIndent.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) ObjIndent.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateString("LAST_SUPP_ID",(String)ObjIndent.getAttribute("LAST_SUPP_ID").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjIndent.getAttribute("REMARKS").getObj());
                rsHDetail.updateDouble("PEND_INSP_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_INSP_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_INDENT_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_INDENT_QTY").getVal(),3));
                rsHDetail.updateDouble("PEND_PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("PEND_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_PO_NO",(String)ObjIndent.getAttribute("LAST_PO_NO").getObj());
                rsHDetail.updateString("LAST_PO_DATE",(String)ObjIndent.getAttribute("LAST_PO_DATE").getObj());
                rsHDetail.updateDouble("LAST_PO_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_PO_QTY").getVal(),3));
                rsHDetail.updateString("LAST_GRN_NO",(String)ObjIndent.getAttribute("LAST_GRN_NO").getObj());
                rsHDetail.updateString("LAST_GRN_DATE",(String)ObjIndent.getAttribute("LAST_GRN_DATE").getObj());
                rsHDetail.updateDouble("LAST_GRN_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_GRN_QTY").getVal(),3));
                rsHDetail.updateDouble("LAST_GRN_RATE",EITLERPGLOBAL.round(ObjIndent.getAttribute("LAST_GRN_RATE").getVal(),3));
                rsHDetail.updateDouble("AA_INDENT_QTY",EITLERPGLOBAL.round(ObjIndent.getAttribute("AA_INDENT_QTY").getVal(),3));
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                
                for(int l=1;l<=ObjIndent.colItemDetail.size();l++) {
                    clsIndentItemDetail ObjLot=(clsIndentItemDetail)ObjIndent.colItemDetail.get(Integer.toString(l));
                    
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
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pIndentno,int pUserID) {
        Statement tmpStmt;
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
    }
    
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pIndentno,int pUserID) {
        Statement tmpStmt;
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
    }
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
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
            
            rsIndent.refreshRow();
            return MoveLast();
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(int pCompanyID,String pIndentNo) {
        String strCondition = " WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND INDENT_NO='"+pIndentNo.trim()+"'";
        clsIndent ObjIndent=new clsIndent();
        ObjIndent.Filter(strCondition,pCompanyID);
        return ObjIndent;
    }
    
    
    public Object getObject(int pCompanyID,String pIndentNo,String pURL) {
        String strCondition = " WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND INDENT_NO='"+pIndentNo.trim()+"'";
        clsIndent ObjIndent=new clsIndent();
        ObjIndent.Filter(strCondition,pCompanyID,pURL);
        return ObjIndent;
    }
    
    
    public HashMap getList(String pCondition) {
        ResultSet rsTmp;
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
                clsIndent ObjIndent=new clsIndent();
                
                //Populate the user
                ObjIndent.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjIndent.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjIndent.setAttribute("INDENT_DATE",rsTmp.getString("INDENT_DATE"));
                ObjIndent.setAttribute("INDENT_TYPE",rsTmp.getString("INDENT_TYPE"));
                ObjIndent.setAttribute("FOR_UNIT",rsTmp.getLong("FOR_UNIT"));
                ObjIndent.setAttribute("FOR_DEPT_ID",rsTmp.getLong("FOR_DEPT_ID"));
                ObjIndent.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjIndent.setAttribute("REQUIRED_DATE",rsTmp.getString("REQUIRED_DATE"));
                ObjIndent.setAttribute("BUYER",rsTmp.getLong("BUYER"));
                ObjIndent.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjIndent.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjIndent.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjIndent.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                ObjIndent.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjIndent.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjIndent.setAttribute("STATUS",rsTmp.getString("STATUS"));
                ObjIndent.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjIndent.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjIndent.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjIndent.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                ObjIndent.setAttribute("GROSS_AMOUNT",EITLERPGLOBAL.round(rsTmp.getLong("GROSS_AMOUNT"),3));
                
                //Now Populate the collection
                //first clear the collection
                ObjIndent.colLineItems.clear();
                
                String mCompanyID=Long.toString( (long) ObjIndent.getAttribute("COMPANY_ID").getVal());
                String mIndentno=(String) ObjIndent.getAttribute("INDENT_NO").getObj();
                
                tmpStmt2=Conn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno.trim()+"'");
                
                Counter=0;
                while(rsTmp2.next()) {
                    Counter=Counter+1;
                    clsIndentItem ObjIndentdetail=new clsIndentItem();
                    
                    ObjIndentdetail.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjIndentdetail.setAttribute("INDENT_NO",rsTmp2.getString("INDENT_NO"));
                    ObjIndentdetail.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                    ObjIndentdetail.setAttribute("MR_NO",rsTmp2.getString("MR_NO"));
                    ObjIndentdetail.setAttribute("MR_SR_NO",rsTmp2.getLong("MR_SR_NO"));
                    ObjIndentdetail.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                    ObjIndentdetail.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                    ObjIndentdetail.setAttribute("QTY",EITLERPGLOBAL.round(rsTmp2.getLong("QTY"),3));
                    ObjIndentdetail.setAttribute("BAL_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("BAL_QTY"),3));
                    ObjIndentdetail.setAttribute("PO_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("PO_QTY"),3));
                    ObjIndentdetail.setAttribute("TOTAL_REQ_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("TOTAL_RECEIPT_QTY"),3));
                    ObjIndentdetail.setAttribute("ALLOCATED_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("ALLOCATED_QTY"),3));
                    ObjIndentdetail.setAttribute("STOCK_QTY",EITLERPGLOBAL.round(rsTmp2.getLong("STOCK_QTY"),3));
                    ObjIndentdetail.setAttribute("RATE",EITLERPGLOBAL.round(rsTmp2.getLong("RATE"),3));
                    ObjIndentdetail.setAttribute("NET_AMT",EITLERPGLOBAL.round(rsTmp2.getLong("NET_AMT"),3));
                    ObjIndentdetail.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
                    ObjIndentdetail.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                    ObjIndentdetail.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjIndentdetail.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjIndentdetail.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjIndentdetail.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    ObjIndent.colLineItems.put(Long.toString(Counter),ObjIndentdetail);
                }// Innser while
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjIndent);
            }//Out While
            
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_INDENT_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsIndent=Stmt.executeQuery(strSql);
            
            if(!rsIndent.first()) {
                strSql = "SELECT * FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY INDENT_NO";
                rsIndent=Stmt.executeQuery(strSql);
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
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public boolean Filter(String pCondition,int pCompanyID,String pURL) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_INDENT_HEADER " + pCondition ;
            Conn=data.getConn(pURL);
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsIndent=Stmt.executeQuery(strSql);
            
            
            if(!rsIndent.first()) {
                strSql = "SELECT * FROM D_INV_INDENT_HEADER WHERE COMPANY_ID="+Long.toString(pCompanyID)+" ORDER BY INDENT_NO";
                rsIndent=Stmt.executeQuery(strSql);
                Ready=true;
                rsIndent.first();
                setData(pURL);
                return false;
            }
            else {
                Ready=true;
                rsIndent.first();
                setData(pURL);
                return true;
            }
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    private boolean setData() {
        ResultSet rsTmp,rsItemDetail;
        Connection tmpConn;
        Statement tmpStmt,stItemDetail;
        
        tmpConn=data.getConn();
        
        HashMap List=new HashMap();
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsIndent.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsIndent.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("DOC_ID",rsIndent.getLong("DOC_ID"));
            setAttribute("ATTACHEMENT",rsIndent.getBoolean("ATTACHEMENT"));
            setAttribute("ATTACHEMENT_PATH",rsIndent.getString("ATTACHEMENT_PATH"));
            
            setAttribute("COMPANY_ID",rsIndent.getLong("COMPANY_ID"));
            setAttribute("INDENT_NO",rsIndent.getString("INDENT_NO"));
            setAttribute("INDENT_DATE",rsIndent.getString("INDENT_DATE"));
            setAttribute("INDENT_TYPE",rsIndent.getString("INDENT_TYPE"));
            setAttribute("FOR_UNIT",rsIndent.getLong("FOR_UNIT"));
            setAttribute("FOR_DEPT_ID",rsIndent.getLong("FOR_DEPT_ID"));
            setAttribute("PURPOSE",rsIndent.getString("PURPOSE"));
            setAttribute("BUYER",rsIndent.getLong("BUYER"));
            setAttribute("APPROVED",rsIndent.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsIndent.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsIndent.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsIndent.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsIndent.getString("REJECTED_REMARKS"));
            setAttribute("REMARKS",rsIndent.getString("REMARKS"));
            setAttribute("CANCELED",rsIndent.getInt("CANCELED"));
            setAttribute("STATUS",rsIndent.getString("STATUS"));
            setAttribute("CREATED_BY",rsIndent.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsIndent.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsIndent.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsIndent.getString("MODIFIED_DATE"));
            setAttribute("GROSS_AMOUNT",EITLERPGLOBAL.round(rsIndent.getDouble("GROSS_AMOUNT"),3));
            setAttribute("HIERARCHY_ID",rsIndent.getInt("HIERARCHY_ID"));
            
            
            //Now Populate the collection
            //first clear the collection
            colItemDetail.clear();
            
            String mCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String mIndentno=(String) getAttribute("INDENT_NO").getObj();
            
            //== Insert Lot Nos. ==
            stItemDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(HistoryView) {
                rsItemDetail=stItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno+"' AND INDENT_SR_NO=0 AND REVISION_NO="+RevNo+" ORDER BY RIGHT(POSITION_NO,6) ");
            }
            else {
                rsItemDetail=stItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno+"' AND INDENT_SR_NO=0 ORDER BY RIGHT(POSITION_NO,6)");
            }
            
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
            
            
            
            
            colLineItems.clear();
            tmpStmt=tmpConn.createStatement();
            
            String strSql="";
            if(HistoryView) {
                strSql="SELECT * FROM D_INV_INDENT_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO";
            }
            else {
                strSql="SELECT * FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno.trim()+"' ORDER BY SR_NO";
            }
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
                ObjItem.setAttribute("LAST_GRN_QTY",rsTmp.getDouble("LAST_GRN_QTY"));
                ObjItem.setAttribute("LAST_GRN_RATE",EITLERPGLOBAL.round(rsTmp.getDouble("LAST_GRN_RATE"),3));
                ObjItem.setAttribute("AA_INDENT_QTY",EITLERPGLOBAL.round(rsTmp.getDouble("AA_INDENT_QTY"),3));
                
                
                //== Insert Lot Nos. ==
                stItemDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                if(HistoryView) {
                    rsItemDetail=stItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno+"' AND INDENT_SR_NO="+SrNo+" AND REVISION_NO="+RevNo+" ORDER BY RIGHT(POSITION_NO,6) ");
                }
                else {
                    rsItemDetail=stItemDetail.executeQuery("SELECT * FROM D_INV_INDENT_ITEM_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INDENT_NO='"+mIndentno+"' AND INDENT_SR_NO="+SrNo+" ORDER BY RIGHT(POSITION_NO,6)");
                }
                
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
    }
    
    
    private boolean setData(String pURL) {
        ResultSet rsTmp,rsItemDetail;
        Connection tmpConn;
        Statement tmpStmt,stItemDetail;
        
        tmpConn=data.getConn(pURL);
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            
            setAttribute("DOC_ID",rsIndent.getLong("DOC_ID"));
            setAttribute("ATTACHEMENT",rsIndent.getBoolean("ATTACHEMENT"));
            setAttribute("ATTACHEMENT_PATH",rsIndent.getString("ATTACHEMENT_PATH"));
            
            setAttribute("COMPANY_ID",rsIndent.getLong("COMPANY_ID"));
            setAttribute("INDENT_NO",rsIndent.getString("INDENT_NO"));
            setAttribute("INDENT_DATE",rsIndent.getString("INDENT_DATE"));
            setAttribute("INDENT_TYPE",rsIndent.getString("INDENT_TYPE"));
            setAttribute("FOR_UNIT",rsIndent.getLong("FOR_UNIT"));
            setAttribute("FOR_DEPT_ID",rsIndent.getLong("FOR_DEPT_ID"));
            setAttribute("PURPOSE",rsIndent.getString("PURPOSE"));
            setAttribute("BUYER",rsIndent.getLong("BUYER"));
            setAttribute("APPROVED",rsIndent.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsIndent.getString("APPROVED_DATE"));
            setAttribute("REJECTED_DATE",rsIndent.getString("REJECTED_DATE"));
            setAttribute("REJECTED_REMARKS",rsIndent.getString("REJECTED_REMARKS"));
            setAttribute("REMARKS",rsIndent.getString("REMARKS"));
            setAttribute("CANCELED",rsIndent.getInt("CANCELED"));
            setAttribute("STATUS",rsIndent.getString("STATUS"));
            setAttribute("CREATED_BY",rsIndent.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsIndent.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsIndent.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsIndent.getString("MODIFIED_DATE"));
            setAttribute("GROSS_AMOUNT",rsIndent.getDouble("GROSS_AMOUNT"));
            setAttribute("HIERARCHY_ID",rsIndent.getInt("HIERARCHY_ID"));
            
            
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
    }
    
    public boolean rollback() {
        try {
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
        
    }
    
    public static HashMap getPendingApprovals(long pCompanyID,long pUserID,int pOrder,int FinYearFrom) {
        ResultSet rsTmp=null;
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
                ResultSet rsIndent1;
                String gIndentno=rsTmp.getString("INDENT_NO");
                
                clsIndent ObjIndent=new clsIndent();
                ObjIndent.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjIndent.setAttribute("INDENT_DATE",rsTmp.getString("INDENT_DATE"));
                ObjIndent.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjIndent.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));
                
                List.put(Long.toString(Counter),ObjIndent);
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
    }
    
    public static HashMap getIndentItemList(int pCompanyID,String pIndentNo,boolean pAllItems) {
        //Fourth argument pAllItems indicates whether you want all item listing or
        //Just pending Items where PO_QTY<QTY
        //i.e. PO entry is still pending
        
        Connection tmpConn;
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
    }
    
    
    public static HashMap getIndentItemList(int pCompanyID,String pIndentNo,boolean pAllItems,String pURL) {
        //Fourth argument pAllItems indicates whether you want all item listing or
        //Just pending Items where PO_QTY<QTY
        //i.e. PO entry is still pending
        
        Connection tmpConn=null;
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
    }
    
    
    public static double getPendingIndentQty(int pCompanyID,int pDeptID,String pItemID) {
        Connection tmpConn=null;
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
    }
    
    
    
    public static double getTotalPendingIndentQty(int pCompanyID,String pItemID) {
        Connection tmpConn=null;
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
        
    }
    
    
    
    
    
    // RIA Status
    // 0 - Not created
    // 1- Created but under approval
    // 2 - Created and Approved
    
    public static int IsRIACreated(int pCompanyID,String pIndentNo,int pSrNo) {
        Connection tmpConn=null;
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
                    rsQuotation=stQuotation.executeQuery("SELECT * FROM D_PUR_QUOT_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+InquiryNo+"' AND INQUIRY_SRNO="+InquirySrNo+" AND QUOT_ID IN (SELECT QUOT_ID FROM D_PUR_QUOT_APPROVAL_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND APPROVED=1)");
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
    }
    
    
    
    
    // This routine returns approved ria rates.
    public static double getRIAQty(int pCompanyID,String pIndentNo,int pSrNo) {
        Connection tmpConn=null;
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
    }
    
    public static String getRIANo(int pCompanyID,String pIndentNo,int pSrNo) {
        Connection tmpConn=null;
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
    }
    
    
    
    
    
    
    public static double getAlreadyApprovedQty(int pCompanyID,int pDeptID,String pItemID) {
        Connection tmpConn=null;
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
        }
    }
    
    
    public static double getTotalAlreadyApprovedQty(int pCompanyID,String pItemID) {
        Connection tmpConn=null;
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
    }
    
    
    public static Object getLastIndent(int pCompanyID,String pItemID) {
        Connection tmpConn=null;
        Statement stTmp=null,stLot=null;
        ResultSet rsTmp=null,rsLot=null;
        String strSQL="";
        double PendingQty=0;
        
        clsIndent ObjIndent=new clsIndent();
        
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
                ObjIndent=(clsIndent)tmpObj.getObject(pCompanyID, IndentNo);
            }
            
            //tmpConn.close();
            stTmp.close();
            stLot.close();
            rsTmp.close();
            rsLot.close();
            
            
            return ObjIndent;
        }
        catch(Exception e) {
            return ObjIndent;
        }
    }
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsIndent=Stmt.executeQuery("SELECT * FROM D_INV_INDENT_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND INDENT_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_INDENT_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsIndent ObjIndent=new clsIndent();
                
                ObjIndent.setAttribute("INDENT_NO",rsTmp.getString("INDENT_NO"));
                ObjIndent.setAttribute("INDENT_DATE",rsTmp.getString("INDENT_DATE"));
                ObjIndent.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjIndent.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjIndent.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjIndent.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjIndent.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjIndent);
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
    
    
    public static int getDeptID(int pCompanyID,String pDocNo) {
        try {
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
    }
    
    
    public static String getDeliveryDate(int pCompanyID,String pDocNo,int pSrNo) {
        try {
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
    }
    
    
    public static double getQty(int pCompanyID,String pDocNo,int pSrNo) {
        try {
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
    }
    
    
    public static boolean CanCancel(int pCompanyID,String pDocNo) {
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
        
    }
    
    
    public static boolean CancelIndent(int pCompanyID,String pDocNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
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
            rsIndent.close();
        }
        catch(Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public static String getDocStatus(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
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
        
    }
    
    
    
    public static String getDocStatus(int pCompanyID,String pDocNo,boolean Approved) {
        ResultSet rsTmp;
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
        
    }
    
    
    public static int getIndentDeptID(int pCompanyID,String pDocNo) {
        ResultSet rsTmp;
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
        
    }
    
    
    public static String getDocStatus(int pCompanyID,String pDocNo,String dbURL) {
        ResultSet rsTmp;
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
        
    }
    
    
    public void MoveToRow(int RowNo) {
        try {
            rsIndent.absolute(RowNo);
            
        }
        catch(Exception e) {
            try {
                rsIndent.last();
            }
            catch(Exception m){}
        }
        setData();
        
    }
    
    
    public static boolean userCanProcessIndent(int pCompanyID,String pIndentNo) {
        ResultSet rsTmp;
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
        
        return canProcess;
    }
}
