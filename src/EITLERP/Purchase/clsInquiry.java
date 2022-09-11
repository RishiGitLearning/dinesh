/*
 * clsInquiry.java
 *
 * Created on May 4, 2004, 9:25 AM
 */

package EITLERP.Purchase;

import java.sql.*;
import java.util.*;
import EITLERP.*;

/**
 *
 * @author  nhpatel
 * @version
 */
public class clsInquiry {
    
    public String LastError = "";
    private ResultSet rsInquiry;
    private Connection Conn;
    private Statement Stmt;
    public boolean Ready = false;
    
    public HashMap colLineItems;
    public HashMap colSupp;
    private HashMap props;
    
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
    
    
    /** Creates new clsInquiry */
    public clsInquiry() {
        props = new HashMap();
        
        props.put("COMPANY_ID",new Variant(0));
        props.put("INQUIRY_NO",new Variant(""));
        props.put("INQUIRY_DATE",new Variant(""));
        props.put("INQUIRY_TYPE",new Variant(""));
        props.put("PROJECT",new Variant(""));
        props.put("BUYER",new Variant(0));
        props.put("PURPOSE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("PREFIX",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("REJECTED",new Variant(false));
        props.put("APPROVED_DATE",  new Variant(""));
        props.put("STATUS",new Variant(""));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("REVISION_NO",new Variant(0));
        
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        
        props.put("LAST_QUOT_DATE",new Variant(""));
        //Create a new object for line items
        colLineItems= new HashMap();
        colSupp = new HashMap();
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            HistoryView=false;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSql = "SELECT * FROM D_PUR_INQUIRY_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND INQUIRY_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY INQUIRY_NO ";
            rsInquiry=Stmt.executeQuery(strSql);
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
            rsInquiry.close();
            
        }
        catch(Exception e) {
            
        }
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsInquiry.first();
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
            if(rsInquiry.isAfterLast()||rsInquiry.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsInquiry.last();
            }
            else {
                rsInquiry.next();
                if(rsInquiry.isAfterLast()) {
                    rsInquiry.last();
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
            if(rsInquiry.isFirst()||rsInquiry.isBeforeFirst()) {
                rsInquiry.first();
            }
            else {
                rsInquiry.previous();
                if(rsInquiry.isBeforeFirst()) {
                    rsInquiry.first();
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
            rsInquiry.last();
            setData();
            return true;
        }
        catch(Exception e) {
            LastError=e.getMessage();
            return false;
        }
    }
    
    
    public boolean Insert(String pPrefix,String pDocno) {
        Statement stTmp,stHistory,stHDetail,stHSupp;
        ResultSet rsTmp,rsSupp,rsHistory,rsHDetail,rsHSupp;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "",IndentNo = "";
        int IndentSrno = 0;
        double IndentQty=0,PrevQty=0,CurrentQty=0;
        
        try {
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("INQUIRY_TYPE").getObj();
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHSupp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER_H WHERE INQUIRY_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL_H WHERE INQUIRY_NO='1'");
            rsHDetail.first();
            rsHSupp=stHSupp.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP_H WHERE INQUIRY_NO='1'");
            rsHSupp.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER WHERE INQUIRY_NO='1'");
            
            
            //Check the Quantities entered against the Indent No.
            for(int i=1;i<=colLineItems.size();i++) {
                clsInquiryItem ObjItem=(clsInquiryItem) colLineItems.get(Integer.toString(i));
                IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                IndentSrno=(int)ObjItem.getAttribute("INDENT_SRNO").getVal();
                
                if(!IndentNo.trim().equals("")&&IndentSrno>0) {
                    //Get the  Indent Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY,ITEM_CODE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        IndentQty=rsTmp.getDouble("QTY");
                    }
                    
                    
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                        LastError="Item Code in Inquiry doesn't match with Indent. Original Item code is "+rsTmp.getString("ITEM_CODE");
                        return false;
                    }
                    //Get Total Qty Entered in PO Against this Indent No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND INDENT_SRNO="+IndentSrno+" AND INQUIRY_NO NOT IN(SELECT INQUIRY_NO FROM D_PUR_INQUIRY_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    PrevQty=0;
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > IndentQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Indent No. "+IndentNo+" Sr. No. "+IndentSrno+" qty "+IndentQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            
            // Stock Updataion started over here
            //=======================================//
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsInquiryItem ObjItem=(clsInquiryItem)colLineItems.get(Integer.toString(i));
                    
                    IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrno=(int)ObjItem.getAttribute("INDENT_SRNO").getVal();
                    double Qty=ObjItem.getAttribute("QTY").getVal();
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    Statement tmpStmt;
                    tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    
                    // Updating tables if Indent type is selected
                    if(gType.equals("I")) {
                        ResultSet rsMat;
                        Statement Stmt1;
                        Stmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                       /*String strSql = "UPDATE D_INV_INDENT_DETAIL SET INQUIRY_BAL_QTY = INQUIRY_BALQTY + "+Qty+" WHERE COMPANY_ID="+
                                                Long.toString(gCompanyID)+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+
                                                IndentSrno;
                       Stmt1.executeUpdate(strSql);*/
                    }
                }
            }
            
            setAttribute("INQUIRY_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,18, (int)getAttribute("FFNO").getVal(),true));
            
            rsInquiry.moveToInsertRow();
            rsInquiry.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsInquiry.updateString("INQUIRY_NO",(String) getAttribute("INQUIRY_NO").getObj());
            rsInquiry.updateString("INQUIRY_DATE",(String) getAttribute("INQUIRY_DATE").getObj());
            rsInquiry.updateString("INQUIRY_TYPE",(String) getAttribute("INQUIRY_TYPE").getObj());
            rsInquiry.updateString("PROJECT",(String) getAttribute("PROJECT").getObj());
            rsInquiry.updateInt("BUYER",(int) getAttribute("BUYER").getVal());
            rsInquiry.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsInquiry.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsInquiry.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsInquiry.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsInquiry.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsInquiry.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsInquiry.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsInquiry.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsInquiry.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsInquiry.updateString("LAST_QUOT_DATE",(String)getAttribute("LAST_QUOT_DATE").getObj());
            rsInquiry.updateBoolean("CHANGED",true);
            rsInquiry.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInquiry.updateBoolean("APPROVED",false);
            rsInquiry.updateString("APPROVED_DATE","0000-00-00");
            rsInquiry.updateBoolean("REJECTED",false);
            rsInquiry.updateString("REJECTED_DATE","0000-00-00");
            rsInquiry.updateBoolean("CANCELLED",false);
            rsInquiry.insertRow();
            
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("INQUIRY_NO",(String) getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String) getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("INQUIRY_TYPE",(String) getAttribute("INQUIRY_TYPE").getObj());
            rsHistory.updateString("PROJECT",(String) getAttribute("PROJECT").getObj());
            rsHistory.updateInt("BUYER",(int) getAttribute("BUYER").getVal());
            rsHistory.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsHistory.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("LAST_QUOT_DATE",(String)getAttribute("LAST_QUOT_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            // Inserting records in Inquiry Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND INQUIRY_NO='1'");
            String nInquiryno=(String) getAttribute("INQUIRY_NO").getObj();
            String nInquirytype = (String) getAttribute("INQUIRY_TYPE").getObj();
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsInquiryItem ObjItem=(clsInquiryItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("INQUIRY_NO",nInquiryno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("MAKE",(String) ObjItem.getAttribute("MAKE").getObj());
                rsTmp.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsTmp.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsTmp.updateString("DELIVERY_DATE",(String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsTmp.updateString("INDENT_NO",(String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsTmp.updateLong("INDENT_SRNO",(long) ObjItem.getAttribute("INDENT_SRNO").getVal());
                rsTmp.updateString("REMARKS",(String) ObjItem.getAttribute("REMARKS").getObj());
                rsTmp.updateBoolean("CANCELED",(boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsTmp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsTmp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                //rsTmp.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("INQUIRY_NO",nInquiryno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("MAKE",(String) ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateString("DELIVERY_DATE",(String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateString("INDENT_NO",(String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateLong("INDENT_SRNO",(long) ObjItem.getAttribute("INDENT_SRNO").getVal());
                rsHDetail.updateString("REMARKS",(String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                //rsHDetail.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                //rsHDetail.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
            }
            
            // Inserting records in Inquiru Supplier details
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsSupp=tmpStmt.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND INQUIRY_NO='1'");
            
            for(int i=1;i<=colSupp.size();i++) {
                clsInquirySupp ObjItem1=(clsInquirySupp) colSupp.get(Integer.toString(i));
                
                rsSupp.moveToInsertRow();
                rsSupp.updateLong("COMPANY_ID",nCompanyID);
                rsSupp.updateString("INQUIRY_NO",nInquiryno);
                rsSupp.updateLong("SR_NO",i);
                rsSupp.updateString("SUPP_ID",(String) ObjItem1.getAttribute("SUPP_ID").getObj());
                rsSupp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsSupp.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsSupp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsSupp.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsSupp.updateBoolean("CHANGED",true);
                rsSupp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsSupp.insertRow();
                
                rsHSupp.moveToInsertRow();
                rsHSupp.updateInt("REVISION_NO",1);
                rsHSupp.updateLong("COMPANY_ID",nCompanyID);
                rsHSupp.updateString("INQUIRY_NO",nInquiryno);
                rsHSupp.updateLong("SR_NO",i);
                rsHSupp.updateString("SUPP_ID",(String) ObjItem1.getAttribute("SUPP_ID").getObj());
                rsHSupp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHSupp.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
                rsHSupp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHSupp.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
                rsHSupp.updateBoolean("CHANGED",true);
                rsHSupp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHSupp.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            //Assign the Module ID as per PO Type
            ObjFlow.ModuleID=18;
            ObjFlow.DocNo=(String)getAttribute("INQUIRY_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_INQUIRY_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="INQUIRY_NO";
            
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
            //--------- Approval Flow Update complete -----------//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean Update() {
        Statement stTmp,stHistory,stHDetail,stHSupp;
        ResultSet rsTmp,rsSupp,rsHistory,rsHDetail,rsHSupp;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "",IndentNo = "";
        int IndentSrno = 0;
        double IndentQty=0,PrevQty=0,CurrentQty=0;
        
        try {
            //No Primary Keys will be updated & Updating of Header starts
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("INQUIRY_TYPE").getObj();
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHSupp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER_H WHERE INQUIRY_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL_H WHERE INQUIRY_NO='1'");
            rsHDetail.first();
            rsHSupp=stHSupp.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP_H WHERE INQUIRY_NO='1'");
            rsHSupp.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("INQUIRY_NO").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER WHERE TRIM(INQUIRY_NO)='"+theDocNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            //rsHeader.first();
            
            //Check the Quantities entered against the Indent No.
            for(int i=1;i<=colLineItems.size();i++) {
                clsInquiryItem ObjItem=(clsInquiryItem) colLineItems.get(Integer.toString(i));
                IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                IndentSrno=(int)ObjItem.getAttribute("INDENT_SRNO").getVal();
                
                if(!IndentNo.trim().equals("")&&IndentSrno>0) {
                    //Get the  Indent Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY,ITEM_CODE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        IndentQty=rsTmp.getDouble("QTY");
                    }
                    
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                        LastError="Item Code in Inquiry doesn't match with Indent. Original Item code is "+rsTmp.getString("ITEM_CODE");
                        return false;
                    }
                    
                    //Get Total Qty Entered in PO Against this Indent No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND INDENT_SRNO="+IndentSrno+" AND INQUIRY_NO<>'"+(String)getAttribute("INQUIRY_NO").getObj()+"' AND INQUIRY_NO NOT IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    PrevQty=0;
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > IndentQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Indent No. "+IndentNo+" Sr. No. "+IndentSrno+" qty "+IndentQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            // Stock Updataion started over here
            //=======================================//
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsInquiryItem ObjItem=(clsInquiryItem)colLineItems.get(Integer.toString(i));
                    
                    IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrno=(int)ObjItem.getAttribute("INDENT_SRNO").getVal();
                    double Qty=ObjItem.getAttribute("QTY").getVal();
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    Statement tmpStmt;
                    tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    
                    // Updating tables if Indent type is selected
                    if(gType.equals("I")) {
                        ResultSet rsMat;
                        Statement Stmt1;
                        Stmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                       /*String strSql = "UPDATE D_INV_INDENT_DETAIL SET INQUIRY_BAL_QTY = INQUIRY_BALQTY + "+Qty+" WHERE COMPANY_ID="+
                                                Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+
                                                IndentSrno;
                       Stmt1.executeUpdate(strSql);*/
                    }
                }
            }
            
            rsInquiry.updateString("INQUIRY_DATE",(String) getAttribute("INQUIRY_DATE").getObj());
            rsInquiry.updateString("INQUIRY_TYPE",(String) getAttribute("INQUIRY_TYPE").getObj());
            rsInquiry.updateString("PROJECT",(String) getAttribute("PROJECT").getObj());
            rsInquiry.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsInquiry.updateInt("BUYER",(int) getAttribute("BUYER").getVal());
             rsInquiry.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsInquiry.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsInquiry.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            //rsInquiry.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //rsInquiry.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsInquiry.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsInquiry.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInquiry.updateString("LAST_QUOT_DATE",(String)getAttribute("LAST_QUOT_DATE").getObj());
            rsInquiry.updateBoolean("CHANGED",true);
            rsInquiry.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInquiry.updateBoolean("CANCELLED",false);
            rsInquiry.updateRow();
            
            rsInquiry.refreshRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_INQUIRY_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+(String)getAttribute("INQUIRY_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("INQUIRY_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("INQUIRY_NO",(String) getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String) getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("INQUIRY_TYPE",(String) getAttribute("INQUIRY_TYPE").getObj());
            rsHistory.updateString("PROJECT",(String) getAttribute("PROJECT").getObj());
            rsHistory.updateInt("BUYER",(int) getAttribute("BUYER").getVal());
            rsHistory.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsHistory.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            //rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            //rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("LAST_QUOT_DATE",(String)getAttribute("LAST_QUOT_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            
            // Inserting records in Inquiry Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='1'");
            String nInquiryno=(String) getAttribute("INQUIRY_NO").getObj();
            String nInquirytype = (String) getAttribute("INQUIRY_TYPE").getObj();
            
            String Del_Query = "DELETE FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='"+nInquiryno.trim()+"'";
            data.Execute(Del_Query);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsInquiryItem ObjItem=(clsInquiryItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("INQUIRY_NO",nInquiryno);
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("MAKE",(String) ObjItem.getAttribute("MAKE").getObj());
                rsTmp.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsTmp.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsTmp.updateString("DELIVERY_DATE",(String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsTmp.updateString("INDENT_NO",(String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsTmp.updateLong("INDENT_SRNO",(long) ObjItem.getAttribute("INDENT_SRNO").getVal());
                rsTmp.updateString("REMARKS",(String) ObjItem.getAttribute("REMARKS").getObj());
                rsTmp.updateBoolean("CANCELED",(boolean) ObjItem.getAttribute("CANCELED").getBool());
                //rsTmp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                //rsTmp.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("INQUIRY_NO",nInquiryno);
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("MAKE",(String) ObjItem.getAttribute("MAKE").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateString("DELIVERY_DATE",(String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateString("INDENT_NO",(String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateLong("INDENT_SRNO",(long) ObjItem.getAttribute("INDENT_SRNO").getVal());
                rsHDetail.updateString("REMARKS",(String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjItem.getAttribute("CANCELED").getBool());
                //rsHDetail.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                //rsHDetail.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                // Updating Indent table
            }
            
            // Inserting records in Inquiru Supplier details
            Del_Query = "DELETE FROM D_PUR_INQUIRY_SUPP WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='"+nInquiryno.trim()+"'";
            data.Execute(Del_Query);
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsSupp=tmpStmt.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND INQUIRY_NO='1'");
            
            for(int i=1;i<=colSupp.size();i++) {
                clsInquirySupp ObjItem=(clsInquirySupp) colSupp.get(Integer.toString(i));
                
                rsSupp.moveToInsertRow();
                rsSupp.updateLong("COMPANY_ID",nCompanyID);
                rsSupp.updateString("INQUIRY_NO",nInquiryno);
                rsSupp.updateLong("SR_NO",i);
                rsSupp.updateString("SUPP_ID",(String) ObjItem.getAttribute("SUPP_ID").getObj());
                //rsSupp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                //rsSupp.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsSupp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsSupp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsSupp.updateBoolean("CHANGED",true);
                rsSupp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsSupp.insertRow();
                
                rsHSupp.moveToInsertRow();
                rsHSupp.updateInt("REVISION_NO",RevNo);
                rsHSupp.updateLong("COMPANY_ID",nCompanyID);
                rsHSupp.updateString("INQUIRY_NO",nInquiryno);
                rsHSupp.updateLong("SR_NO",i);
                rsHSupp.updateString("SUPP_ID",(String) ObjItem.getAttribute("SUPP_ID").getObj());
                //rsHSupp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                //rsHSupp.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsHSupp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsHSupp.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHSupp.updateBoolean("CHANGED",true);
                rsHSupp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHSupp.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            //Assign the Module ID as per PO Type
            ObjFlow.ModuleID=18;
            ObjFlow.DocNo=(String)getAttribute("INQUIRY_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_INQUIRY_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="INQUIRY_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_INQUIRY_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=18 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            //--------- Approval Flow Update complete -----------//
            
            
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    
    public boolean Amend() {
        Statement stTmp,stHistory,stHDetail,stHSupp;
        ResultSet rsTmp,rsSupp,rsHistory,rsHDetail,rsHSupp;
        Statement stHeader;
        ResultSet rsHeader;
        
        String strSQL = "",IndentNo = "";
        int IndentSrno = 0;
        double IndentQty=0,PrevQty=0,CurrentQty=0;
        
        try {
            
            //Open the Approval Flag
            data.Execute("UPDATE D_PUR_INQUIRY_HEADER SET APPROVED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+(String)getAttribute("INQUIRY_NO").getObj()+"'");
            
            //No Primary Keys will be updated & Updating of Header starts
            ApprovalFlow ObjFlow=new ApprovalFlow();
            // Inserting records in Header
            long gCompanyID= (long) getAttribute("COMPANY_ID").getVal();
            setAttribute("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            String gType = (String) getAttribute("INQUIRY_TYPE").getObj();
            
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHSupp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER_H WHERE INQUIRY_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL_H WHERE INQUIRY_NO='1'");
            rsHDetail.first();
            rsHSupp=stHSupp.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP_H WHERE INQUIRY_NO='1'");
            rsHSupp.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("INQUIRY_NO").getObj();
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER WHERE TRIM(INQUIRY_NO)='"+theDocNo+"' AND COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            //rsHeader.first();
            
            //Check the Quantities entered against the Indent No.
            for(int i=1;i<=colLineItems.size();i++) {
                clsInquiryItem ObjItem=(clsInquiryItem) colLineItems.get(Integer.toString(i));
                IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                IndentSrno=(int)ObjItem.getAttribute("INDENT_SRNO").getVal();
                
                if(!IndentNo.trim().equals("")&&IndentSrno>0) {
                    //Get the  Indent Qty.
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT QTY,ITEM_CODE FROM D_INV_INDENT_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+IndentSrno+" ";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        IndentQty=rsTmp.getDouble("QTY");
                    }
                    
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    if(!rsTmp.getString("ITEM_CODE").trim().equals(ItemID)) {
                        LastError="Item Code in Inquiry doesn't match with Indent. Original Item code is "+rsTmp.getString("ITEM_CODE");
                        return false;
                    }
                    
                    
                    //Get Total Qty Entered in PO Against this Indent No.
                    PrevQty=0;
                    stTmp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    strSQL="SELECT SUM(QTY) AS SUMQTY FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+IndentNo+"' AND INDENT_SRNO="+IndentSrno+" AND INQUIRY_NO<>'"+(String)getAttribute("INQUIRY_NO").getObj()+"' AND INQUIRY_NO NOT IN (SELECT INQUIRY_NO FROM D_PUR_INQUIRY_HEADER WHERE CANCELLED=1)";
                    rsTmp=stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if(rsTmp.getRow()>0) {
                        PrevQty=rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty=ObjItem.getAttribute("QTY").getVal();
                    
                    if((CurrentQty+PrevQty) > IndentQty) //If total Qty exceeds Indent Qty. Do not allow
                    {
                        LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds Indent No. "+IndentNo+" Sr. No. "+IndentSrno+" qty "+IndentQty+". Please verify the input.";
                        return false;
                    }
                }
            }
            
            
            // Stock Updataion started over here
            //=======================================//
            if(AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for(int i=1;i<=colLineItems.size();i++) {
                    clsInquiryItem ObjItem=(clsInquiryItem)colLineItems.get(Integer.toString(i));
                    
                    IndentNo=(String)ObjItem.getAttribute("INDENT_NO").getObj();
                    IndentSrno=(int)ObjItem.getAttribute("INDENT_SRNO").getVal();
                    double Qty=ObjItem.getAttribute("QTY").getVal();
                    String ItemID=(String)ObjItem.getAttribute("ITEM_CODE").getObj();
                    Statement tmpStmt;
                    tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    
                    // Updating tables if Indent type is selected
                    if(gType.equals("I")) {
                        ResultSet rsMat;
                        Statement Stmt1;
                        Stmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                       /*String strSql = "UPDATE D_INV_INDENT_DETAIL SET INQUIRY_BAL_QTY = INQUIRY_BALQTY + "+Qty+" WHERE COMPANY_ID="+
                                                Integer.toString(EITLERPGLOBAL.gCompanyID)+" AND INDENT_NO='"+IndentNo+"' AND SR_NO="+
                                                IndentSrno;
                       Stmt1.executeUpdate(strSql);*/
                    }
                }
            }
            
            rsInquiry.updateString("INQUIRY_DATE",(String) getAttribute("INQUIRY_DATE").getObj());
            rsInquiry.updateString("INQUIRY_TYPE",(String) getAttribute("INQUIRY_TYPE").getObj());
            rsInquiry.updateString("PROJECT",(String) getAttribute("PROJECT").getObj());
            rsInquiry.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsInquiry.updateInt("BUYER",(int) getAttribute("BUYER").getVal());
            rsInquiry.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsInquiry.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsInquiry.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsInquiry.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsInquiry.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsInquiry.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsInquiry.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsInquiry.updateString("LAST_QUOT_DATE",(String)getAttribute("LAST_QUOT_DATE").getObj());
            rsInquiry.updateBoolean("CHANGED",true);
            rsInquiry.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsInquiry.updateBoolean("CANCELLED",false);
            rsInquiry.updateRow();
            
            rsInquiry.refreshRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_PUR_INQUIRY_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+(String)getAttribute("INQUIRY_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("INQUIRY_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("INQUIRY_NO",(String) getAttribute("INQUIRY_NO").getObj());
            rsHistory.updateString("INQUIRY_DATE",(String) getAttribute("INQUIRY_DATE").getObj());
            rsHistory.updateString("INQUIRY_TYPE",(String) getAttribute("INQUIRY_TYPE").getObj());
            rsHistory.updateString("PROJECT",(String) getAttribute("PROJECT").getObj());
            rsHistory.updateInt("BUYER",(int) getAttribute("BUYER").getVal());
            rsHistory.updateString("STATUS",(String) getAttribute("STATUS").getObj());
            rsHistory.updateString("PURPOSE",(String) getAttribute("PURPOSE").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsHistory.updateString("REMARKS",(String) getAttribute("REMARKS").getObj());
            rsHistory.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateString("LAST_QUOT_DATE",(String)getAttribute("LAST_QUOT_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            
            // Inserting records in Inquiry Details
            Statement tmpStmt;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='1'");
            String nInquiryno=(String) getAttribute("INQUIRY_NO").getObj();
            String nInquirytype = (String) getAttribute("INQUIRY_TYPE").getObj();
            
            String Del_Query = "DELETE FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='"+nInquiryno.trim()+"'";
            data.Execute(Del_Query);
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            for(int i=1;i<=colLineItems.size();i++) {
                clsInquiryItem ObjItem=(clsInquiryItem) colLineItems.get(Integer.toString(i));
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",nCompanyID);
                rsTmp.updateString("INQUIRY_NO",nInquiryno);
                rsTmp.updateInt("SR_NO",(int)ObjItem.getAttribute("SR_NO").getVal());
                rsTmp.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsTmp.updateString("MAKE",(String) ObjItem.getAttribute("MAKE").getObj());
                rsTmp.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsTmp.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsTmp.updateString("DELIVERY_DATE",(String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsTmp.updateString("INDENT_NO",(String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsTmp.updateLong("INDENT_SRNO",(long) ObjItem.getAttribute("INDENT_SRNO").getVal());
                rsTmp.updateString("REMARKS",(String) ObjItem.getAttribute("REMARKS").getObj());
                rsTmp.updateBoolean("CANCELED",(boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsTmp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",nCompanyID);
                rsHDetail.updateString("INQUIRY_NO",nInquiryno);
                rsHDetail.updateLong("SR_NO",(int)ObjItem.getAttribute("SR_NO").getVal());
                rsHDetail.updateString("ITEM_CODE",(String) ObjItem.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC",(String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateLong("UNIT",(long) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("QTY",ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateString("DELIVERY_DATE",(String) ObjItem.getAttribute("DELIVERY_DATE").getObj());
                rsHDetail.updateString("INDENT_NO",(String) ObjItem.getAttribute("INDENT_NO").getObj());
                rsHDetail.updateLong("INDENT_SRNO",(long) ObjItem.getAttribute("INDENT_SRNO").getVal());
                rsHDetail.updateString("REMARKS",(String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateBoolean("CANCELED",(boolean) ObjItem.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                // Updating Indent table
            }
            
            // Inserting records in Inquiru Supplier details
            Del_Query = "DELETE FROM D_PUR_INQUIRY_SUPP WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='"+nInquiryno.trim()+"'";
            data.Execute(Del_Query);
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsSupp=tmpStmt.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP WHERE COMPANY_ID="+Long.toString(gCompanyID)+" AND INQUIRY_NO='1'");
            
            for(int i=1;i<=colSupp.size();i++) {
                clsInquirySupp ObjItem=(clsInquirySupp) colSupp.get(Integer.toString(i));
                
                rsSupp.moveToInsertRow();
                rsSupp.updateLong("COMPANY_ID",nCompanyID);
                rsSupp.updateString("INQUIRY_NO",nInquiryno);
                rsSupp.updateLong("SR_NO",i);
                rsSupp.updateString("SUPP_ID",(String) ObjItem.getAttribute("SUPP_ID").getObj());
                rsSupp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsSupp.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsSupp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsSupp.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsSupp.updateBoolean("CHANGED",true);
                rsSupp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsSupp.insertRow();
                
                rsHSupp.moveToInsertRow();
                rsHSupp.updateInt("REVISION_NO",RevNo);
                rsHSupp.updateLong("COMPANY_ID",nCompanyID);
                rsHSupp.updateString("INQUIRY_NO",nInquiryno);
                rsHSupp.updateLong("SR_NO",i);
                rsHSupp.updateString("SUPP_ID",(String) ObjItem.getAttribute("SUPP_ID").getObj());
                rsHSupp.updateLong("CREATED_BY",(long) ObjItem.getAttribute("CREATED_BY").getVal());
                rsHSupp.updateString("CREATED_DATE",(String) ObjItem.getAttribute("CREATED_DATE").getObj());
                rsHSupp.updateLong("MODIFIED_BY",(long) ObjItem.getAttribute("MODIFIED_BY").getVal());
                rsHSupp.updateString("MODIFIED_DATE",(String) ObjItem.getAttribute("MODIFIED_DATE").getObj());
                rsHSupp.updateBoolean("CHANGED",true);
                rsHSupp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHSupp.insertRow();
            }
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            //Assign the Module ID as per PO Type
            ObjFlow.ModuleID=18;
            ObjFlow.DocNo=(String)getAttribute("INQUIRY_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_PUR_INQUIRY_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName="INQUIRY_NO";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_PUR_INQUIRY_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=18 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            //--------- Approval Flow Update complete -----------//
            
            
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
    public boolean CanDelete(int pCompanyID,String pInquiryno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_INQUIRY_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND INQUIRY_NO='"+pInquiryno+"' AND APPROVED=true";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=18 AND DOC_NO='"+pInquiryno+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    public boolean IsEditable(int pCompanyID,String pInquiryno,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_PUR_INQUIRY_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND INQUIRY_NO='"+pInquiryno+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=18 AND DOC_NO='"+pInquiryno+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID=(int) getAttribute("COMPANY_ID").getVal();
            String lInquiryno=(String) getAttribute("INQUIRY_NO").getObj();
            
            if(CanDelete(lCompanyID,lInquiryno,pUserID)) {
                rollback();
                String strQry = "DELETE FROM D_INV_INQUIRY_HEADER WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='"+lInquiryno.trim()+"'";
                data.Execute(strQry);
            }
            rsInquiry.refreshRow();
            return MoveLast();
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    public Object getObject(long pCompanyID,String pInquiryNo) {
        String strCondition = " WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND INQUIRY_NO='"+pInquiryNo.trim()+"' ";
        clsInquiry ObjItem=new clsInquiry();
        ObjItem.Filter(strCondition);
        return ObjItem;
    }
    
    
    public boolean Filter(String pCondition) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_PUR_INQUIRY_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsInquiry=Stmt.executeQuery(strSql);
            
            if(!rsInquiry.first()) {
                strSql = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_DATE>='"+EITLERPGLOBAL.FinYearFrom+"' AND INQUIRY_DATE<='"+EITLERPGLOBAL.FinYearTo+"' ORDER BY INQUIRY_NO" ;
                rsInquiry=Stmt.executeQuery(strSql);
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
    
    
    private boolean setData() {
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp1;
        Statement tmpStmt1;
        
        ResultSet rsTmp2;
        
        HashMap List=new HashMap();
        long Counter=0;
        long Counter1 = 0;
        int RevNo=0;
        
        try {
            tmpStmt=Conn.createStatement();
            Counter=0;
            Counter=Counter+1;
            clsInquiry ObjInquiry=new clsInquiry();
            
            if(HistoryView) {
                RevNo=rsInquiry.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsInquiry.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            //Populate the user
            setAttribute("COMPANY_ID",rsInquiry.getLong("COMPANY_ID"));
            setAttribute("INQUIRY_NO",rsInquiry.getString("INQUIRY_NO"));
            setAttribute("INQUIRY_DATE",rsInquiry.getString("INQUIRY_DATE"));
            setAttribute("INQUIRY_TYPE",rsInquiry.getString("INQUIRY_TYPE"));
            setAttribute("PROJECT",rsInquiry.getString("PROJECT"));
            setAttribute("BUYER",rsInquiry.getInt("BUYER"));
            setAttribute("STATUS",rsInquiry.getString("STATUS"));
            setAttribute("PURPOSE",rsInquiry.getString("PURPOSE"));
            setAttribute("CANCELLED",rsInquiry.getInt("CANCELLED"));
            setAttribute("APPROVED",rsInquiry.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsInquiry.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsInquiry.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsInquiry.getString("REJECTED_DATE"));
            setAttribute("HIERARCHY_ID",rsInquiry.getInt("HIERARCHY_ID"));
            setAttribute("REMARKS",rsInquiry.getString("REMARKS"));
            setAttribute("CREATED_BY",rsInquiry.getLong("CREATED_BY"));
            setAttribute("CREATED_DATE",rsInquiry.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsInquiry.getLong("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsInquiry.getString("MODIFIED_DATE"));
            setAttribute("LAST_QUOT_DATE",rsInquiry.getString("LAST_QUOT_DATE"));
            //Now Populate the collection
            //first clear the collection
            colLineItems.clear();
            colSupp.clear();
            
            String mCompanyID=Long.toString((long) rsInquiry.getLong("COMPANY_ID"));
            String mInquiryno=(String) rsInquiry.getString("INQUIRY_NO");
            
            tmpStmt1=Conn.createStatement();
            
            if(HistoryView) {
                rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND INQUIRY_NO='"+mInquiryno.trim()+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO ");
            }
            else {
                rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_PUR_INQUIRY_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INQUIRY_NO='"+mInquiryno.trim()+"' ORDER BY SR_NO");
            }
            //rsTmp1.first();
            
            Counter1=0;
            while(rsTmp1.next()) {
                Counter1=Counter1+1;
                clsInquiryItem ObjItem=new clsInquiryItem();
                
                ObjItem.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                ObjItem.setAttribute("INQUIRY_NO",rsTmp1.getString("INQUIRY_NO"));
                ObjItem.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                ObjItem.setAttribute("ITEM_CODE",rsTmp1.getString("ITEM_CODE"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp1.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("MAKE",rsTmp1.getString("MAKE"));
                ObjItem.setAttribute("QTY",rsTmp1.getDouble("QTY"));
                ObjItem.setAttribute("UNIT",rsTmp1.getLong("UNIT"));
                ObjItem.setAttribute("DELIVERY_DATE",rsTmp1.getString("DELIVERY_DATE"));
                ObjItem.setAttribute("INDENT_NO",rsTmp1.getString("INDENT_NO"));
                ObjItem.setAttribute("INDENT_SRNO",rsTmp1.getLong("INDENT_SRNO"));
                ObjItem.setAttribute("REMARKS",rsTmp1.getString("REMARKS"));
                ObjItem.setAttribute("CANCELED",rsTmp1.getInt("CANCELED"));
                ObjItem.setAttribute("CREATED_BY",rsTmp1.getLong("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY",rsTmp1.getLong("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                
                colLineItems.put(Long.toString(Counter1),ObjItem);
            }// Innser while
            
            tmpStmt1=Conn.createStatement();
            if(HistoryView) {
                rsTmp2=tmpStmt1.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP_H WHERE COMPANY_ID="+mCompanyID+" AND INQUIRY_NO='"+mInquiryno.trim()+"' AND REVISION_NO="+RevNo);
            }
            else {
                rsTmp2=tmpStmt1.executeQuery("SELECT * FROM D_PUR_INQUIRY_SUPP WHERE COMPANY_ID="+mCompanyID+" AND INQUIRY_NO='"+mInquiryno.trim()+"'");
            }
            
            int Counter2=0;
            while(rsTmp2.next()) {
                Counter2=Counter2+1;
                clsInquirySupp ObjItemSupp=new clsInquirySupp();
                
                ObjItemSupp.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                ObjItemSupp.setAttribute("INQUIRY_NO",rsTmp2.getString("INQUIRY_NO"));
                ObjItemSupp.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjItemSupp.setAttribute("SUPP_ID",rsTmp2.getString("SUPP_ID"));
                ObjItemSupp.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjItemSupp.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjItemSupp.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjItemSupp.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                
                colSupp.put(Long.toString(Counter2),ObjItemSupp);
            }// Innser while
            //Put the prepared user object into list
            //List.put(Long.toString(Counter),ObjInquiry);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Statement tmpStmt;
        
        ResultSet rsTmp1;
        Statement tmpStmt1;
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpStmt=Conn.createStatement();
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_INQUIRY_HEADER"+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsInquiry ObjInquiry=new clsInquiry();
                
                ObjInquiry.setAttribute("COMPANY_ID",rsTmp.getLong("COMPANY_ID"));
                ObjInquiry.setAttribute("INQURIY_NO",rsTmp.getString("INQUIRY_NO"));
                ObjInquiry.setAttribute("INQUIRY_DATE",rsTmp.getString("INQUIRY_DATE"));
                ObjInquiry.setAttribute("INQUIRY_TYPE",rsTmp.getString("INQUIRY_TYPE"));
                ObjInquiry.setAttribute("PROJECT",rsTmp.getString("PROJECT"));
                ObjInquiry.setAttribute("BUYER",rsTmp.getInt("BUYER"));
                ObjInquiry.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjInquiry.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjInquiry.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjInquiry.setAttribute("REJECTED_DATE",rsTmp.getString("REJECTED_DATE"));
                ObjInquiry.setAttribute("REJECTED_REMARKS",rsTmp.getString("REJECTED_REMARKS"));
                ObjInquiry.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjInquiry.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));
                ObjInquiry.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjInquiry.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjInquiry.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjInquiry.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                //Now Populate the collection
                //first clear the collection
                ObjInquiry.colLineItems.clear();
                
                String mCompanyID=Long.toString((long) ObjInquiry.getAttribute("COMPANY_ID").getVal());
                String mInquiryno=(String) ObjInquiry.getAttribute("INQUIRY_NO").getObj();
                
                tmpStmt1=Conn.createStatement();
                rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_INV_INQUIRY_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND INQUIRY_NO='"+mInquiryno.trim()+"'");
                
                Counter=0;
                while(rsTmp1.next()) {
                    Counter=Counter+1;
                    clsInquiryItem ObjItem=new clsInquiryItem();
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                    ObjItem.setAttribute("INQUIRY_NO",rsTmp1.getString("INQUIRY_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                    ObjItem.setAttribute("ITEM_CODE",rsTmp1.getString("ITEM_CODE"));
                    ObjItem.setAttribute("QTY",rsTmp1.getLong("QTY"));
                    ObjItem.setAttribute("UNIT",rsTmp1.getLong("UNIT"));
                    ObjItem.setAttribute("DELIVERY_DATE",rsTmp1.getString("DELIVERY_DATE"));
                    ObjItem.setAttribute("INDENT_NO",rsTmp1.getString("INDENT_NO"));
                    ObjItem.setAttribute("INDENT_SRNO",rsTmp1.getLong("INDENT_SRNO"));
                    ObjItem.setAttribute("REMARKS",rsTmp1.getString("REMARKS"));
                    ObjItem.setAttribute("CANCELED",rsTmp1.getInt("CANCELED"));
                    ObjItem.setAttribute("CREATED_BY",rsTmp1.getLong("CREATED_BY"));
                    ObjItem.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                    ObjItem.setAttribute("MODIFIED_BY",rsTmp1.getLong("MODIFIED_BY"));
                    ObjItem.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                    
                    ObjInquiry.colLineItems.put(Long.toString(Counter),ObjItem);
                }// Innser while
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjInquiry);
            }
        }
        catch(Exception e) {
        }
        
        return List;
    }
    
    
    
    public boolean rollback() {
        try {
            ResultSet rsTmp1;
            Statement Stmt1;
            String nInquiryno = (String) getAttribute("INQUIRY_NO").getObj();
            String gInquirytype = (String) getAttribute("INQUIRY_TYPE").getObj();
            Stmt1=Conn.createStatement();
            rsTmp1=Stmt1.executeQuery("SELECT * FROM D_INV_INQUIRY_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND ISSUE_NO='"+
            nInquiryno.trim()+"'");
            
            while(rsTmp1.next()) {
                if(gInquirytype.equals("1")) // Updating Indent tables
                {
                    ResultSet rsIndent;
          /*      rsIndent=Stmt1.executeQuery("UPDATE D_INV_INDENT_DETAIL SET BAL_QTY = BALQTY -"+ rsTmp1.getLong("QTY")+" WHERE COMPANY_ID="+
                                             Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INDENT_NO='"+rsTmp1.getString("INDENT_NO")+" AND SR_NO="+
                                             rsTmp1.getLong("INDENT_SRNO"));*/
                }
            }
            ResultSet rsTmp;
            rsTmp=Stmt1.executeQuery("DELETE FROM D_INV_INQUIRY_DETAIL WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND INQUIRY_NO='" +
            nInquiryno.trim()+"'");
            return true;
        }
        catch(Exception e) {
            return false;
        }
        
    }
    
    public static HashMap getINQItemList(int pCompanyID,String pINQNo) {
        //Fourth argument pAllItems indicates whether you want all item listing
        
        Connection tmpConn;
        Statement stTmp=null,stTmp1=null;
        ResultSet rsTmp=null,rsTmp1=null;
        int Counter1=0,Counter2=0;
        HashMap List=new HashMap();
        String strSQL="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+pINQNo+"' AND APPROVED=1");
            rsTmp.first();
            if(rsTmp.getRow()>0) {
                
                strSQL="SELECT D_PUR_INQUIRY_DETAIL.* FROM D_PUR_INQUIRY_DETAIL,D_PUR_INQUIRY_HEADER  WHERE D_PUR_INQUIRY_DETAIL.COMPANY_ID=D_PUR_INQUIRY_HEADER.COMPANY_ID AND D_PUR_INQUIRY_DETAIL.INQUIRY_NO=D_PUR_INQUIRY_HEADER.INQUIRY_NO AND D_PUR_INQUIRY_DETAIL.COMPANY_ID="+pCompanyID+" AND D_PUR_INQUIRY_DETAIL.INQUIRY_NO='"+pINQNo+"' ORDER BY ITEM_CODE";
                
                stTmp1=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsTmp1=stTmp1.executeQuery(strSQL);
                rsTmp1.first();
                
                Counter1=0;
                
                while(!rsTmp1.isAfterLast()) {
                    Counter1++;
                    clsInquiryItem ObjItem=new clsInquiryItem();
                    
                    int INQSrNo=rsTmp1.getInt("SR_NO");
                    
                    ObjItem.setAttribute("COMPANY_ID",rsTmp1.getLong("COMPANY_ID"));
                    ObjItem.setAttribute("INQUIRY_NO",rsTmp1.getString("INQUIRY_NO"));
                    ObjItem.setAttribute("SR_NO",rsTmp1.getLong("SR_NO"));
                    ObjItem.setAttribute("ITEM_CODE",rsTmp1.getString("ITEM_CODE"));
                    ObjItem.setAttribute("ITEM_EXTRA_DESC",rsTmp1.getString("ITEM_EXTRA_DESC"));
                    ObjItem.setAttribute("MAKE",rsTmp1.getString("MAKE"));
                    ObjItem.setAttribute("QTY",rsTmp1.getDouble("QTY"));
                    ObjItem.setAttribute("UNIT",rsTmp1.getLong("UNIT"));
                    ObjItem.setAttribute("DELIVERY_DATE",rsTmp1.getString("DELIVERY_DATE"));
                    ObjItem.setAttribute("INDENT_NO",rsTmp1.getString("INDENT_NO"));
                    ObjItem.setAttribute("INDENT_SRNO",rsTmp1.getLong("INDENT_SRNO"));
                    ObjItem.setAttribute("REMARKS",rsTmp1.getString("REMARKS"));
                    ObjItem.setAttribute("CANCELED",rsTmp1.getInt("CANCELED"));
                    ObjItem.setAttribute("CREATED_BY",rsTmp1.getLong("CREATED_BY"));
                    ObjItem.setAttribute("CREATED_DATE",rsTmp1.getString("CREATED_DATE"));
                    ObjItem.setAttribute("MODIFIED_BY",rsTmp1.getLong("MODIFIED_BY"));
                    ObjItem.setAttribute("MODIFIED_DATE",rsTmp1.getString("MODIFIED_DATE"));
                    
                    //Put into list
                    List.put(Integer.toString(Counter1),ObjItem);
                    
                    rsTmp1.next();
                }
            }
            
            //tmpConn.close();
            stTmp.close();
            stTmp1.close();
            rsTmp.close();
            rsTmp1.close();
            
            return List;
        }
        catch(Exception e) {
            return List;
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
                strSQL="SELECT D_PUR_INQUIRY_HEADER.INQUIRY_NO,D_PUR_INQUIRY_HEADER.INQUIRY_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_INQUIRY_HEADER,D_COM_DOC_DATA WHERE D_PUR_INQUIRY_HEADER.INQUIRY_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_INQUIRY_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_INQUIRY_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=18 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_PUR_INQUIRY_HEADER.INQUIRY_NO,D_PUR_INQUIRY_HEADER.INQUIRY_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_INQUIRY_HEADER,D_COM_DOC_DATA WHERE D_PUR_INQUIRY_HEADER.INQUIRY_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_INQUIRY_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_INQUIRY_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=18 ORDER BY D_PUR_INQUIRY_HEADER.INQUIRY_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_PUR_INQUIRY_HEADER.INQUIRY_NO,D_PUR_INQUIRY_HEADER.INQUIRY_DATE,RECEIVED_DATE,0 AS DEPT_ID FROM D_PUR_INQUIRY_HEADER,D_COM_DOC_DATA WHERE D_PUR_INQUIRY_HEADER.INQUIRY_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_INQUIRY_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_INQUIRY_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=18 ORDER BY D_PUR_INQUIRY_HEADER.INQUIRY_NO";
            }
            
            //strSQL="SELECT D_PUR_INQUIRY_HEADER.INQUIRY_NO,D_PUR_INQUIRY_HEADER.INQUIRY_DATE FROM D_PUR_INQUIRY_HEADER,D_COM_DOC_DATA WHERE D_PUR_INQUIRY_HEADER.INQUIRY_NO=D_COM_DOC_DATA.DOC_NO AND D_PUR_INQUIRY_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_PUR_INQUIRY_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=18";
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp.getString("INQUIRY_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsInquiry ObjDoc=new clsInquiry();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("INQUIRY_NO",rsTmp.getString("INQUIRY_NO"));
                ObjDoc.setAttribute("INQUIRY_DATE",rsTmp.getString("INQUIRY_DATE"));
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
            rsInquiry=Stmt.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_PUR_INQUIRY_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INQUIRY_NO='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsInquiry ObjInquiry=new clsInquiry();
                
                ObjInquiry.setAttribute("INQUIRY_NO",rsTmp.getString("INQUIRY_NO"));
                ObjInquiry.setAttribute("INQUIRY_DATE",rsTmp.getString("INQUIRY_DATE"));
                ObjInquiry.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjInquiry.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjInquiry.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjInquiry.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjInquiry.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjInquiry);
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
    
    
    
    
    public static String getDocStatus(int pCompanyID,String pInquiryNo) {
        ResultSet rsTmp;
        String strMessage="";
        
        try {
            
            //First check that Document exist
            rsTmp=data.getResult("SELECT INQUIRY_NO,APPROVED,CANCELLED FROM D_PUR_INQUIRY_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+pInquiryNo+"'");
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
    
    
    
    
    public static boolean CanCancel(int pCompanyID,String pInquiryNo) {
        ResultSet rsTmp=null;
        boolean canCancel=false;
        
        try {
            rsTmp=data.getResult("SELECT INQUIRY_NO FROM D_PUR_INQUIRY_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+pInquiryNo+"' AND CANCELLED=0");
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
    
    
    public static boolean CancelInquiry(int pCompanyID,String pInquiryNo) {
        
        ResultSet rsTmp=null,rsIndent=null;
        boolean Cancelled=false;
        
        try {
            if(CanCancel(pCompanyID,pInquiryNo)) {
                
                boolean ApprovedInquiry=false;
                
                rsTmp=data.getResult("SELECT APPROVED FROM D_PUR_INQUIRY_HEADER WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+pInquiryNo+"'");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    ApprovedInquiry=rsTmp.getBoolean("APPROVED");
                }
                
                
                if(ApprovedInquiry) {
                    
                }
                else {
                    int ModuleID=18;
                    
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pInquiryNo+"' AND MODULE_ID="+(ModuleID));
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_PUR_INQUIRY_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND INQUIRY_NO='"+pInquiryNo+"'");
                
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
