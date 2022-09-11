/*
 * clsDeclarationForm.java
 *
 * Created on May 7, 2004, 4:12 PM
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
 * @author  jadave
 * @version
 */
 
public class clsDeclarationForm {
    
    public String LastError="";
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt,Stmt1;
    
    private HashMap props;
    public boolean Ready = false;
    public static int ModuleID=13;
    //DECLARATION FORM ITEM Collection
    public HashMap colDeclarationFormItems;
    //Declaration Form Item Detail Collection
    public HashMap colDeclarationFormItemDetails;
    
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
    
    
    /** Creates new clsDeclarationForm */
    public clsDeclarationForm() {
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("DECLARATION_ID", new Variant(""));
        props.put("DECLARATION_DATE", new Variant(""));
        props.put("CONTRACTOR_NAME", new Variant(""));
        props.put("ADD1", new Variant(""));
        props.put("ADD2", new Variant(""));
        props.put("ADD3", new Variant(""));
        props.put("CITY", new Variant(""));
        props.put("PINCODE", new Variant(""));
        props.put("PO_NO", new Variant(""));
        props.put("PO_DATE", new Variant(""));
        props.put("FOR_DEPT_ID", new Variant(0));
        props.put("RECEIVED_BY", new Variant(""));
        props.put("PURPOSE", new Variant(""));
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("CANCELLED", new Variant(false));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        
        //Create a new object for DeclarationFormItems collection
        colDeclarationFormItems = new HashMap();
        //Create a new object for DeclarationFormItemDetails collection
        colDeclarationFormItemDetails = new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
    }
    
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_DECLARATION_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND DECLARATION_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DECLARATION_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DECLARATION_ID";
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
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
    
    
    
    
    public boolean Insert(String pPrefix) {
        Statement stHistory,stHDetail,stHLot;
        ResultSet rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_DECLARATION_HEADER_H WHERE DECLARATION_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_H WHERE DECLARATION_ID='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL_H WHERE DECLARATION_ID='1'");
            rsHLot.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_DECLARATION_HEADER WHERE DECLARATION_ID='1'");
            //rsHeader.first();
            
            //Get First Free No
            /*String strDecNo = data.getFirstFree(EITLERPGLOBAL.gCompanyID, 13, pPrefix.trim());
            
            if(strDecNo=="") {
                LastError= "Error generating First Free number.";
                return false;
            }*/
            
            rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            String strDecNo=clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,13, (int)getAttribute("FFNO").getVal(),true);
            //--------- Generate New GRN No. ------------
            setAttribute("DECLARATION_ID",strDecNo);
            //-------------------------------------------------
            
            rsResultSet.moveToInsertRow();
            rsResultSet.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("DECLARATION_ID",(String)getAttribute("DECLARATION_ID").getObj());
            rsResultSet.updateString("DECLARATION_DATE",(String)getAttribute("DECLARATION_DATE").getObj());
            rsResultSet.updateString("CONTRACTOR_NAME",(String)getAttribute("CONTRACTOR_NAME").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsResultSet.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsResultSet.updateLong("FOR_DEPT_ID", (long)getAttribute("FOR_DEPT_ID").getVal());
            rsResultSet.updateString("RECEIVED_BY",(String)getAttribute("RECEIVED_BY").getObj());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DECLARATION_ID",(String)getAttribute("DECLARATION_ID").getObj());
            rsHistory.updateString("DECLARATION_DATE",(String)getAttribute("DECLARATION_DATE").getObj());
            rsHistory.updateString("CONTRACTOR_NAME",(String)getAttribute("CONTRACTOR_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateLong("FOR_DEPT_ID", (long)getAttribute("FOR_DEPT_ID").getVal());
            rsHistory.updateString("RECEIVED_BY",(String)getAttribute("RECEIVED_BY").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateBoolean("APPROVED",(boolean)getAttribute("APPROVED").getBool());
            rsHistory.updateString("APPROVED_DATE",(String)getAttribute("APPROVED_DATE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.insertRow();
            
            //Inserting into DECLARATION FORM DETAIL
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL WHERE DECLARATION_ID='1'");
            
            //Inserting into DECLARATION FORM DETAIL DETAIL
            ResultSet rsTmp1;
            Statement tmpStmt1;
            
            tmpStmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL WHERE DECLARATION_ID='1'");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            long lSrNo = 0;
            
            for(int i=1;i<=colDeclarationFormItems.size();i++) {
                clsDeclarationFormItem ObjDeclarationFormItems=(clsDeclarationFormItem) colDeclarationFormItems.get(Integer.toString(i));
                lSrNo = (long)getSRNO(EITLERPGLOBAL.gCompanyID,strDecNo,"D_INV_DECLARATION_DETAIL","SR_NO");
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("DECLARATION_ID",(String)getAttribute("DECLARATION_ID").getObj());
                rsTmp.updateLong("SR_NO",(long)lSrNo);
                rsTmp.updateString("ITEM_CODE",(String)ObjDeclarationFormItems.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("DECLARATION_DESC",(String)ObjDeclarationFormItems.getAttribute("DECLARATION_DESC").getObj());
                rsTmp.updateLong("UNIT",(long)ObjDeclarationFormItems.getAttribute("UNIT").getVal());
                rsTmp.updateDouble("RECD_QTY",(double)ObjDeclarationFormItems.getAttribute("RECD_QTY").getVal());
                rsTmp.updateDouble("BAL_QTY",(double)ObjDeclarationFormItems.getAttribute("RECD_QTY").getVal());
                rsTmp.updateDouble("CONSUMED_QTY",0);
                rsTmp.updateDouble("RETURNED_QTY",0);
                rsTmp.updateBoolean("RETURNED",(boolean)ObjDeclarationFormItems.getAttribute("RETURNED").getBool());
                rsTmp.updateString("EXP_RETURN_DATE",(String)ObjDeclarationFormItems.getAttribute("EXP_RETURN_DATE").getObj());
                rsTmp.updateString("RETURNED_DATE",(String)ObjDeclarationFormItems.getAttribute("RETURNED_DATE").getObj());
                rsTmp.updateString("REMARKS",(String)ObjDeclarationFormItems.getAttribute("REMARKS").getObj());
                rsTmp.updateBoolean("CANCELED",(boolean)ObjDeclarationFormItems.getAttribute("CANCELED").getBool());
                rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("DECLARATION_ID",(String)getAttribute("DECLARATION_ID").getObj());
                rsHDetail.updateLong("SR_NO",(long)lSrNo);
                rsHDetail.updateString("ITEM_CODE",(String)ObjDeclarationFormItems.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("DECLARATION_DESC",(String)ObjDeclarationFormItems.getAttribute("DECLARATION_DESC").getObj());
                rsHDetail.updateLong("UNIT",(long)ObjDeclarationFormItems.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("RECD_QTY",(double)ObjDeclarationFormItems.getAttribute("RECD_QTY").getVal());
                rsHDetail.updateDouble("BAL_QTY",(double)ObjDeclarationFormItems.getAttribute("RECD_QTY").getVal());
                rsHDetail.updateDouble("CONSUMED_QTY",0);
                rsHDetail.updateDouble("RETURNED_QTY",0);
                rsHDetail.updateBoolean("RETURNED",(boolean)ObjDeclarationFormItems.getAttribute("RETURNED").getBool());
                rsHDetail.updateString("EXP_RETURN_DATE",(String)ObjDeclarationFormItems.getAttribute("EXP_RETURN_DATE").getObj());
                rsHDetail.updateString("RETURNED_DATE",(String)ObjDeclarationFormItems.getAttribute("RETURNED_DATE").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjDeclarationFormItems.getAttribute("REMARKS").getObj());
                rsHDetail.updateBoolean("CANCELED",(boolean)ObjDeclarationFormItems.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                //=== Inserting into Declaration Form Detail Detail ===
                for(int j=1;j<=ObjDeclarationFormItems.colItemLot.size();j++) {
                    clsDeclarationFormItemDetail ObjDeclarationFormItemDetail=(clsDeclarationFormItemDetail) ObjDeclarationFormItems.colItemLot.get(Integer.toString(j));
                    rsTmp1.moveToInsertRow();
                    rsTmp1.updateLong("COMPANY_ID",(long)nCompanyID);
                    rsTmp1.updateString("DECLARATION_ID",(String)getAttribute("DECLARATION_ID").getObj());
                    rsTmp1.updateLong("SR_NO", (long)lSrNo);
                    rsTmp1.updateLong("SRNO", (long)getLINENO(nCompanyID,strDecNo,lSrNo,"D_INV_DECLARATION_DETAIL_DETAIL","SRNO"));
                    rsTmp1.updateString("ITEM_ID",(String)ObjDeclarationFormItemDetail.getAttribute("ITEM_ID").getObj());
                    rsTmp1.updateString("LOT_NO",(String)ObjDeclarationFormItemDetail.getAttribute("LOT_NO").getObj());
                    rsTmp1.updateDouble("LOT_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("LOT_QTY").getVal());
                    rsTmp1.updateDouble("TOTAL_ISSUED_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("TOTAL_ISSUED_QTY").getVal());
                    rsTmp1.updateDouble("BAL_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("BAL_QTY").getVal());
                    rsTmp1.updateBoolean("CANCELED", (boolean)ObjDeclarationFormItemDetail.getAttribute("CANCELED").getBool());
                    rsTmp1.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsTmp1.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsTmp1.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsTmp1.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsTmp1.updateBoolean("CHANGED",true);
                    rsTmp1.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmp1.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",1);
                    rsHLot.updateLong("COMPANY_ID",(long)nCompanyID);
                    rsHLot.updateString("DECLARATION_ID",(String)getAttribute("DECLARATION_ID").getObj());
                    rsHLot.updateLong("SR_NO", (long)lSrNo);
                    rsHLot.updateLong("SRNO", (long)getLINENO(nCompanyID,strDecNo,lSrNo,"D_INV_DECLARATION_DETAIL_DETAIL","SRNO"));
                    rsHLot.updateString("ITEM_ID",(String)ObjDeclarationFormItemDetail.getAttribute("ITEM_ID").getObj());
                    rsHLot.updateString("LOT_NO",(String)ObjDeclarationFormItemDetail.getAttribute("LOT_NO").getObj());
                    rsHLot.updateDouble("LOT_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("LOT_QTY").getVal());
                    rsHLot.updateDouble("TOTAL_ISSUED_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("TOTAL_ISSUED_QTY").getVal());
                    rsHLot.updateDouble("BAL_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("BAL_QTY").getVal());
                    rsHLot.updateBoolean("CANCELED", (boolean)ObjDeclarationFormItemDetail.getAttribute("CANCELED").getBool());
                    rsHLot.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsHLot.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsHLot.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsHLot.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
                //=== Inserted into Declaration Form Detail Detail ===
            }
            
            //======== Update the Approval Flow =========
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=13; //Declaration Form module id
            ObjFlow.DocNo=(String)getAttribute("DECLARATION_ID").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_DECLARATION_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="DECLARATION_ID";
            
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
    
    public long getSRNO(long pCompanyID, String pDecID, String pTable, String pField) {
        try {
            Connection Conn=data.getConn();
            Statement stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT MAX("+pField+") AS MAXID FROM "+pTable+" WHERE COMPANY_ID="+Long.toString(pCompanyID) + " AND DECLARATION_ID='" + pDecID + "'" ;
            
            ResultSet rsTmp=stmt.executeQuery(strSQL);
            rsTmp.first();
            
            return rsTmp.getLong("MAXID")+1;
        }
        catch(Exception e) {
            return 1;
        }
    }
    
    public long getLINENO(long pCompanyID, String pDecNo, long pSrno,String pTable, String pField) {
        try {
            Connection Conn=data.getConn();
            Statement stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT MAX("+pField+") AS MAXID FROM "+pTable+" WHERE COMPANY_ID="+Long.toString(pCompanyID) + " AND DECLARATION_ID='" + pDecNo + "' AND SR_NO=" + pSrno ;
            ResultSet rsTmp=stmt.executeQuery(strSQL);
            rsTmp.first();
            
            return rsTmp.getLong("MAXID")+1;
        }
        catch(Exception e) {
            return 1;
        }
    }
    
    //Updates current record
    public boolean Update() {
        Statement stHistory,stHDetail,stHLot;
        ResultSet rsHistory,rsHDetail,rsHLot;
        Statement stHeader;
        ResultSet rsHeader;
        
        try {
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHLot=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM D_INV_DECLARATION_HEADER_H WHERE DECLARATION_ID='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_H WHERE DECLARATION_ID='1'");
            rsHDetail.first();
            rsHLot=stHLot.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL_H WHERE DECLARATION_ID='1'");
            rsHLot.first();
            //------------------------------------//
            
            String theDocNo=(String)getAttribute("DECLARATION_ID").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_DECLARATION_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(DECLARATION_ID)='"+theDocNo+"'");
            //rsHeader.first();
            
            rsResultSet.updateString("DECLARATION_DATE",(String)getAttribute("DECLARATION_DATE").getObj());
            rsResultSet.updateString("CONTRACTOR_NAME",(String)getAttribute("CONTRACTOR_NAME").getObj());
            rsResultSet.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsResultSet.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsResultSet.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsResultSet.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsResultSet.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsResultSet.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsResultSet.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsResultSet.updateLong("FOR_DEPT_ID", (long)getAttribute("FOR_DEPT_ID").getVal());
            rsResultSet.updateString("RECEIVED_BY",(String)getAttribute("RECEIVED_BY").getObj());
            rsResultSet.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsResultSet.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            rsResultSet.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_DECLARATION_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+(String)getAttribute("DECLARATION_ID").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("DECLARATION_ID").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",(String)getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",(String)getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateLong("COMPANY_ID",(long) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("DECLARATION_ID",(String)getAttribute("DECLARATION_ID").getObj());
            rsHistory.updateString("DECLARATION_DATE",(String)getAttribute("DECLARATION_DATE").getObj());
            rsHistory.updateString("CONTRACTOR_NAME",(String)getAttribute("CONTRACTOR_NAME").getObj());
            rsHistory.updateString("ADD1",(String)getAttribute("ADD1").getObj());
            rsHistory.updateString("ADD2",(String)getAttribute("ADD2").getObj());
            rsHistory.updateString("ADD3",(String)getAttribute("ADD3").getObj());
            rsHistory.updateString("CITY",(String)getAttribute("CITY").getObj());
            rsHistory.updateString("PINCODE",(String)getAttribute("PINCODE").getObj());
            rsHistory.updateString("PO_NO",(String)getAttribute("PO_NO").getObj());
            rsHistory.updateString("PO_DATE",(String)getAttribute("PO_DATE").getObj());
            rsHistory.updateLong("FOR_DEPT_ID", (long)getAttribute("FOR_DEPT_ID").getVal());
            rsHistory.updateString("RECEIVED_BY",(String)getAttribute("RECEIVED_BY").getObj());
            rsHistory.updateString("PURPOSE",(String)getAttribute("PURPOSE").getObj());
            rsHistory.updateBoolean("APPROVED",(boolean)getAttribute("APPROVED").getBool());
            rsHistory.updateString("APPROVED_DATE",(String)getAttribute("APPROVED_DATE").getObj());
            rsHistory.updateString("REMARKS",(String)getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CANCELLED",(boolean)getAttribute("CANCELLED").getBool());
            rsHistory.updateLong("HIERARCHY_ID",(long) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateLong("CREATED_BY",(int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY",(int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mDecID=(String)getAttribute("DECLARATION_ID").getObj();
            
            data.Execute("DELETE FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='"+mDecID+ "'");
            data.Execute("DELETE FROM D_INV_DECLARATION_DETAIL_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='"+mDecID+ "'");
            
            ResultSet rsTmp,rsTmp1;
            Statement tmpStmt,tmpStmt1;
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL WHERE DECLARATION_ID='1'");
            
            tmpStmt1=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp1=tmpStmt1.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL WHERE DECLARATION_ID='1'");
            
            long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            String strDecNo = (String)getAttribute("DECLARATION_ID").getObj();
            long lSrNo=0;
            
            for(int i=1;i<=colDeclarationFormItems.size();i++) {
                clsDeclarationFormItem ObjDeclarationFormItems=(clsDeclarationFormItem) colDeclarationFormItems.get(Integer.toString(i));
                lSrNo = getSRNO(EITLERPGLOBAL.gCompanyID,mDecID,"D_INV_DECLARATION_DETAIL","SR_NO");
                
                rsTmp.moveToInsertRow();
                rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("DECLARATION_ID",(String)strDecNo);
                rsTmp.updateLong("SR_NO", lSrNo);
                rsTmp.updateString("ITEM_CODE",(String)ObjDeclarationFormItems.getAttribute("ITEM_CODE").getObj());
                rsTmp.updateString("DECLARATION_DESC",(String)ObjDeclarationFormItems.getAttribute("DECLARATION_DESC").getObj());
                rsTmp.updateLong("UNIT",(long)ObjDeclarationFormItems.getAttribute("UNIT").getVal());
                rsTmp.updateDouble("RECD_QTY",(double)ObjDeclarationFormItems.getAttribute("RECD_QTY").getVal());
                rsTmp.updateDouble("CONSUMED_QTY",0);
                rsTmp.updateDouble("RETURNED_QTY",0);                
                rsTmp.updateDouble("BAL_QTY",(double)ObjDeclarationFormItems.getAttribute("BAL_QTY").getVal());
                rsTmp.updateBoolean("RETURNED",(boolean)ObjDeclarationFormItems.getAttribute("RETURNED").getBool());
                rsTmp.updateString("EXP_RETURN_DATE",(String)ObjDeclarationFormItems.getAttribute("EXP_RETURN_DATE").getObj());
                rsTmp.updateString("RETURNED_DATE",(String)ObjDeclarationFormItems.getAttribute("RETURNED_DATE").getObj());
                rsTmp.updateString("REMARKS",(String)ObjDeclarationFormItems.getAttribute("REMARKS").getObj());
                rsTmp.updateBoolean("CANCELED",(boolean)ObjDeclarationFormItems.getAttribute("CANCELED").getBool());
                rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("DECLARATION_ID",(String)strDecNo);
                rsHDetail.updateLong("SR_NO", lSrNo);
                rsHDetail.updateString("ITEM_CODE",(String)ObjDeclarationFormItems.getAttribute("ITEM_CODE").getObj());
                rsHDetail.updateString("DECLARATION_DESC",(String)ObjDeclarationFormItems.getAttribute("DECLARATION_DESC").getObj());
                rsHDetail.updateLong("UNIT",(long)ObjDeclarationFormItems.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("RECD_QTY",(double)ObjDeclarationFormItems.getAttribute("RECD_QTY").getVal());
                rsHDetail.updateDouble("CONSUMED_QTY",0);
                rsHDetail.updateDouble("RETURNED_QTY",0);
                rsHDetail.updateDouble("BAL_QTY",(double)ObjDeclarationFormItems.getAttribute("BAL_QTY").getVal());
                rsHDetail.updateBoolean("RETURNED",(boolean)ObjDeclarationFormItems.getAttribute("RETURNED").getBool());
                rsHDetail.updateString("EXP_RETURN_DATE",(String)ObjDeclarationFormItems.getAttribute("EXP_RETURN_DATE").getObj());
                rsHDetail.updateString("RETURNED_DATE",(String)ObjDeclarationFormItems.getAttribute("RETURNED_DATE").getObj());
                rsHDetail.updateString("REMARKS",(String)ObjDeclarationFormItems.getAttribute("REMARKS").getObj());
                rsHDetail.updateBoolean("CANCELED",(boolean)ObjDeclarationFormItems.getAttribute("CANCELED").getBool());
                rsHDetail.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
                
                //=== Inserting into Declaration Form Detail Detail ===
                for(int j=1;j<=colDeclarationFormItemDetails.size();j++) {
                    clsDeclarationFormItemDetail ObjDeclarationFormItemDetail=(clsDeclarationFormItemDetail) ObjDeclarationFormItems.colItemLot.get(Integer.toString(j));
                    rsTmp1.moveToInsertRow();
                    rsTmp1.updateLong("COMPANY_ID",(long)nCompanyID);
                    rsTmp1.updateString("DECLARATION_ID",(String)strDecNo);
                    rsTmp1.updateLong("SR_NO", (long)lSrNo);
                    rsTmp1.updateLong("SRNO", (long)getLINENO(nCompanyID,strDecNo,lSrNo,"D_INV_DECLARATION_DETAIL_DETAIL","SRNO"));
                    rsTmp1.updateString("ITEM_ID",(String)ObjDeclarationFormItems.getAttribute("ITEM_ID").getObj());
                    rsTmp1.updateString("LOT_NO",(String)ObjDeclarationFormItemDetail.getAttribute("LOT_NO").getObj());
                    rsTmp1.updateDouble("LOT_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("LOT_QTY").getVal());
                    rsTmp1.updateDouble("TOTAL_ISSUED_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("TOTAL_ISSUED_QTY").getVal());
                    rsTmp1.updateDouble("BAL_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("BAL_QTY").getVal());
                    rsTmp1.updateBoolean("CANCELED", (boolean)ObjDeclarationFormItemDetail.getAttribute("CANCELED").getBool());
                    rsTmp1.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsTmp1.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsTmp1.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsTmp1.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsTmp1.updateBoolean("CHANGED",true);
                    rsTmp1.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsTmp1.insertRow();
                    
                    rsHLot.moveToInsertRow();
                    rsHLot.updateInt("REVISION_NO",RevNo);
                    rsHLot.updateLong("COMPANY_ID",(long)nCompanyID);
                    rsHLot.updateString("DECLARATION_ID",(String)strDecNo);
                    rsHLot.updateLong("SR_NO", (long)lSrNo);
                    rsHLot.updateLong("SRNO", (long)getLINENO(nCompanyID,strDecNo,lSrNo,"D_INV_DECLARATION_DETAIL_DETAIL","SRNO"));
                    rsHLot.updateString("ITEM_ID",(String)ObjDeclarationFormItems.getAttribute("ITEM_ID").getObj());
                    rsHLot.updateString("LOT_NO",(String)ObjDeclarationFormItemDetail.getAttribute("LOT_NO").getObj());
                    rsHLot.updateDouble("LOT_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("LOT_QTY").getVal());
                    rsHLot.updateDouble("TOTAL_ISSUED_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("TOTAL_ISSUED_QTY").getVal());
                    rsHLot.updateDouble("BAL_QTY", (double)ObjDeclarationFormItemDetail.getAttribute("BAL_QTY").getVal());
                    rsHLot.updateBoolean("CANCELED", (boolean)ObjDeclarationFormItemDetail.getAttribute("CANCELED").getBool());
                    rsHLot.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                    rsHLot.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                    rsHLot.updateLong("MODIFIED_BY",(long)getAttribute("MODIFIED_BY").getVal());
                    rsHLot.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                    rsHLot.updateBoolean("CHANGED",true);
                    rsHLot.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsHLot.insertRow();
                }
                //=== Inserted into Declaration Form Detail Detail ===
            }
            //======== Update the Approval Flow =========
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=13; //Declaration Form Module ID
            ObjFlow.DocNo=(String)getAttribute("DECLARATION_ID").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_INV_DECLARATION_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REMARKS").getObj();
            ObjFlow.FieldName="DECLARATION_ID";
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_DECLARATION_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=13 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            } else {
                if(!ObjFlow.UpdateFlow()) {
                    LastError=ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            return true;
            
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    
    //This routine checks and returns whether the DECLARATION FORM is deletable or not
    //Criteria is Approved DECLARATION FORM cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID,String pDecID,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView)
            {
              return false;  
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_DECLARATION_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DECLARATION_ID='"+pDecID.trim()+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Requisition is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=13 AND DOC_NO='"+pDecID.trim()+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
    
    //This routine checks and returns whether the Declaration Form is editable or not
    //Criteria is Approved Declaration Form cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID,String pDecID,int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            if(HistoryView)
            {
              return false;  
            }
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_INV_DECLARATION_HEADER WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DECLARATION_ID='"+pDecID.trim()+"' AND APPROVED=1";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND MODULE_ID=13 AND DOC_NO='"+pDecID.trim()+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    public boolean Delete() {
        try {
            String sCompanyID=Long.toString((long) getAttribute("COMPANY_ID").getVal());
            String sDecID=(String) getAttribute("DECLARATION_ID").getObj();
            
            String strQry = "DELETE FROM D_INV_DECLARATION_DETAIL_DETAIL WHERE COMPANY_ID=" + sCompanyID +" AND DECLARATION_ID='" + sDecID + "'" ;
            data.Execute(strQry);
            strQry = "DELETE FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID=" + sCompanyID +" AND DECLARATION_ID='" + sDecID + "'" ;
            data.Execute(strQry);
            strQry = "DELETE FROM D_INV_DECLARATION_HEADER WHERE COMPANY_ID=" + sCompanyID +" AND DECLARATION_ID='" + sDecID + "'" ;
            data.Execute(strQry);
            LoadData((long)getAttribute("COMPANY_ID").getVal());
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID, String pDecID) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND DECLARATION_ID='" + pDecID + "'" ;
        clsDeclarationForm ObjDeclarationForm = new clsDeclarationForm();
        ObjDeclarationForm.Filter(strCondition,pCompanyID);
        return ObjDeclarationForm;
    }
    
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        ResultSet rsTmp2=null,rsTmp3=null;
        Statement tmpStmt2=null,tmpStmt3=null;
        
        HashMap List=new HashMap();
        long Counter=0,Counter1=0,Counter2=0;
        
        try {
            tmpConn=data.getConn();
            
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_INV_DECLARATION_HEADER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsDeclarationForm ObjDeclarationForm = new clsDeclarationForm();
                ObjDeclarationForm.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjDeclarationForm.setAttribute("DECLARATION_ID",rsTmp.getString("DECLARATION_ID"));
                ObjDeclarationForm.setAttribute("DECLARATION_DATE",rsTmp.getString("DECLARATION_DATE"));
                ObjDeclarationForm.setAttribute("CONTRACTOR_NAME",rsTmp.getString("CONTRACTOR_NAME"));
                ObjDeclarationForm.setAttribute("ADD1",rsTmp.getString("ADD1"));
                ObjDeclarationForm.setAttribute("ADD2",rsTmp.getString("ADD2"));
                ObjDeclarationForm.setAttribute("ADD3",rsTmp.getString("ADD3"));
                ObjDeclarationForm.setAttribute("CITY",rsTmp.getString("CITY"));
                ObjDeclarationForm.setAttribute("PINCODE",rsTmp.getString("PINCODE"));
                ObjDeclarationForm.setAttribute("PO_NO",rsTmp.getString("PO_NO"));
                ObjDeclarationForm.setAttribute("PO_DATE",rsTmp.getString("PO_DATE"));
                ObjDeclarationForm.setAttribute("FOR_DEPT_ID",rsTmp.getLong("FOR_DEPT_ID"));
                ObjDeclarationForm.setAttribute("RECEIVED_BY",rsTmp.getString("RECEIVED_BY"));
                ObjDeclarationForm.setAttribute("PURPOSE",rsTmp.getString("PURPOSE"));
                ObjDeclarationForm.setAttribute("APPROVED",rsTmp.getInt("APPROVED"));
                ObjDeclarationForm.setAttribute("APPROVED_DATE",rsTmp.getString("APPROVED_DATE"));
                ObjDeclarationForm.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjDeclarationForm.setAttribute("CANCELLED",rsTmp.getInt("CANCELLED"));
                ObjDeclarationForm.setAttribute("HIERARCHY_ID",rsTmp.getLong("HIERARCHY_ID"));
                ObjDeclarationForm.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjDeclarationForm.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjDeclarationForm.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjDeclarationForm.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjDeclarationForm.colDeclarationFormItems.clear();
                ObjDeclarationForm.colDeclarationFormItemDetails.clear();
                
                String mCompanyID=Long.toString((long)ObjDeclarationForm.getAttribute("COMPANY_ID").getVal());
                String mDecID=(String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj();
                
                tmpStmt2=tmpConn.createStatement();
                rsTmp2=tmpStmt2.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='" + mDecID + "'");
                
                tmpStmt3=tmpConn.createStatement();
                
                Counter1=0;
                while(rsTmp2.next()) {
                    Counter1=Counter1+1;
                    clsDeclarationFormItem ObjDeclarationFormItems=new clsDeclarationFormItem();
                    
                    ObjDeclarationFormItems.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                    ObjDeclarationFormItems.setAttribute("DECLARATION_ID",rsTmp2.getString("DECLARATION_ID"));
                    ObjDeclarationFormItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                    ObjDeclarationFormItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                    ObjDeclarationFormItems.setAttribute("DECLARATION_DESC",rsTmp2.getString("DECLARATION_DESC"));
                    ObjDeclarationFormItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                    ObjDeclarationFormItems.setAttribute("RECD_QTY",rsTmp2.getDouble("RECD_QTY"));
                    ObjDeclarationFormItems.setAttribute("BAL_QTY",rsTmp2.getDouble("BAL_QTY"));
                    ObjDeclarationFormItems.setAttribute("RETURNED",rsTmp2.getBoolean("RETURNED"));
                    ObjDeclarationFormItems.setAttribute("EXP_RETURN_DATE",rsTmp2.getString("EXP_RETURN_DATE"));
                    ObjDeclarationFormItems.setAttribute("RETURNED_DATE",rsTmp2.getString("RETURNED_DATE"));
                    ObjDeclarationFormItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                    ObjDeclarationFormItems.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
                    ObjDeclarationFormItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                    ObjDeclarationFormItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                    ObjDeclarationFormItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                    ObjDeclarationFormItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                    
                    ObjDeclarationForm.colDeclarationFormItems.put(Long.toString(Counter1),ObjDeclarationFormItems);
                    
                    rsTmp3=tmpStmt3.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='" + mDecID + "' AND SR_NO="+ rsTmp2.getLong("SR_NO"));
                    Counter2=0;
                    while(rsTmp3.next()) {
                        Counter2=Counter2+1;
                        clsDeclarationFormItemDetail ObjDeclarationFormItemDetails=new clsDeclarationFormItemDetail();
                        ObjDeclarationFormItemDetails.setAttribute("COMPANY_ID",rsTmp3.getLong("COMPANY_ID"));
                        ObjDeclarationFormItemDetails.setAttribute("DECLARATION_ID",rsTmp3.getString("DECLARATION_ID"));
                        ObjDeclarationFormItemDetails.setAttribute("SR_NO",rsTmp3.getLong("SR_NO"));
                        ObjDeclarationFormItemDetails.setAttribute("SRNO",rsTmp3.getLong("SRNO"));
                        ObjDeclarationFormItemDetails.setAttribute("ITEM_ID",rsTmp3.getString("ITEM_ID"));
                        ObjDeclarationFormItemDetails.setAttribute("LOT_NO",rsTmp3.getString("LOT_NO"));
                        ObjDeclarationFormItemDetails.setAttribute("LOT_QTY",rsTmp3.getDouble("LOT_QTY"));
                        ObjDeclarationFormItemDetails.setAttribute("TOTAL_ISSUED_QTY",rsTmp3.getDouble("TOTAL_ISSUED_QTY"));
                        ObjDeclarationFormItemDetails.setAttribute("BAL_QTY",rsTmp3.getDouble("BAL_QTY"));
                        ObjDeclarationFormItemDetails.setAttribute("CANCELED",rsTmp3.getInt("CANCELED"));
                        ObjDeclarationFormItemDetails.setAttribute("CREATED_BY",rsTmp3.getLong("CREATED_BY"));
                        ObjDeclarationFormItemDetails.setAttribute("CREATED_DATE",rsTmp3.getString("CREATED_DATE"));
                        ObjDeclarationFormItemDetails.setAttribute("MODIFIED_BY",rsTmp3.getLong("MODIFIED_BY"));
                        ObjDeclarationFormItemDetails.setAttribute("MODIFIED_DATE",rsTmp3.getString("MODIFIED_DATE"));
                        
                        ObjDeclarationForm.colDeclarationFormItemDetails.put(Long.toString(Counter2),ObjDeclarationFormItemDetails);
                        
                    }//innermost while loop
                    
                }//INNER WHILE LOOP
                
                List.put(Long.toString(Counter),ObjDeclarationForm);
            }//OUTER WHILE LOOP
            
        rsTmp.close();
        //tmpConn.close();
        tmpStmt.close();
        
        rsTmp2.close();
        rsTmp3.close();
        tmpStmt2.close();
        tmpStmt3.close();
            
        }
        catch(Exception e) {
            //LastError=e.getMessage();
        }
        
        return List;
    }
    
    
    public boolean Filter(String pCondition,long pCompanyID) {
        Ready=false;
        try {
            String strSql = "SELECT * FROM D_INV_DECLARATION_HEADER " + pCondition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSql);
            
            if(!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+Long.toString(pCompanyID)+" AND DECLARATION_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND DECLARATION_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY DECLARATION_ID";
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
        try {
            ResultSet rsTmp2,rsTmp3;
            Connection tmpConn;
            Statement tmpStmt,tmpStmt3;
            long Counter=0,Counter1=0;
            int RevNo=0;
            
            if(HistoryView)
            {
              RevNo=rsResultSet.getInt("REVISION_NO");  
              setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));  
            }
            else
            {
              setAttribute("REVISION_NO",0);  
            }
            
            tmpConn=data.getConn();
            setAttribute("COMPANY_ID",rsResultSet.getInt("COMPANY_ID"));
            setAttribute("DECLARATION_ID",rsResultSet.getString("DECLARATION_ID"));
            setAttribute("DECLARATION_DATE",rsResultSet.getString("DECLARATION_DATE"));
            setAttribute("CONTRACTOR_NAME",rsResultSet.getString("CONTRACTOR_NAME"));
            setAttribute("ADD1",rsResultSet.getString("ADD1"));
            setAttribute("ADD2",rsResultSet.getString("ADD2"));
            setAttribute("ADD3",rsResultSet.getString("ADD3"));
            setAttribute("CITY",rsResultSet.getString("CITY"));
            setAttribute("PINCODE",rsResultSet.getString("PINCODE"));
            setAttribute("PO_NO",rsResultSet.getString("PO_NO"));
            setAttribute("PO_DATE",rsResultSet.getString("PO_DATE"));
            setAttribute("FOR_DEPT_ID",rsResultSet.getLong("FOR_DEPT_ID"));
            setAttribute("RECEIVED_BY",rsResultSet.getString("RECEIVED_BY"));
            setAttribute("PURPOSE",rsResultSet.getString("PURPOSE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("HIERARCHY_ID",rsResultSet.getLong("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            
            //Now Populate the collection
            //first clear the collections
            colDeclarationFormItems.clear();
            colDeclarationFormItemDetails.clear();
            
            String mCompanyID=Long.toString((long)getAttribute("COMPANY_ID").getVal());
            String mDecID=(String)getAttribute("DECLARATION_ID").getObj();
            
            tmpStmt=tmpConn.createStatement();
            
            if(HistoryView)
            {
                rsTmp2=tmpStmt.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='" + mDecID + "' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else
            {
                rsTmp2=tmpStmt.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='" + mDecID + "' ORDER BY SR_NO");    
            }
            Counter=0;
            while(rsTmp2.next()) {
                Counter=Counter+1;
                clsDeclarationFormItem ObjDeclarationFormItems=new clsDeclarationFormItem();
                ObjDeclarationFormItems.setAttribute("COMPANY_ID",rsTmp2.getLong("COMPANY_ID"));
                ObjDeclarationFormItems.setAttribute("DECLARATION_ID",rsTmp2.getString("DECLARATION_ID"));
                ObjDeclarationFormItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjDeclarationFormItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                ObjDeclarationFormItems.setAttribute("DECLARATION_DESC",rsTmp2.getString("DECLARATION_DESC"));
                ObjDeclarationFormItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                ObjDeclarationFormItems.setAttribute("RECD_QTY",rsTmp2.getDouble("RECD_QTY"));
                ObjDeclarationFormItems.setAttribute("BAL_QTY",rsTmp2.getDouble("BAL_QTY"));
                ObjDeclarationFormItems.setAttribute("CONSUMED_QTY",rsTmp2.getDouble("CONSUMED_QTY"));
                ObjDeclarationFormItems.setAttribute("RETURNED_QTY",rsTmp2.getDouble("RETURNED_QTY"));
                ObjDeclarationFormItems.setAttribute("RETURNED",rsTmp2.getBoolean("RETURNED"));
                ObjDeclarationFormItems.setAttribute("EXP_RETURN_DATE",rsTmp2.getString("EXP_RETURN_DATE"));
                ObjDeclarationFormItems.setAttribute("RETURNED_DATE",rsTmp2.getString("RETURNED_DATE"));
                ObjDeclarationFormItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                ObjDeclarationFormItems.setAttribute("CANCELED",rsTmp2.getInt("CANCELED"));
                ObjDeclarationFormItems.setAttribute("CREATED_BY",rsTmp2.getLong("CREATED_BY"));
                ObjDeclarationFormItems.setAttribute("CREATED_DATE",rsTmp2.getString("CREATED_DATE"));
                ObjDeclarationFormItems.setAttribute("MODIFIED_BY",rsTmp2.getLong("MODIFIED_BY"));
                ObjDeclarationFormItems.setAttribute("MODIFIED_DATE",rsTmp2.getString("MODIFIED_DATE"));
                
                colDeclarationFormItems.put(Long.toString(Counter),ObjDeclarationFormItems);
                
                String mSrNo=Long.toString((long)rsTmp2.getLong("SR_NO"));
                
                tmpStmt3=tmpConn.createStatement();
                if(HistoryView)
                {
                rsTmp3=tmpStmt3.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL_H WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='" + mDecID + "' AND SR_NO=" + mSrNo+" AND REVISION_NO="+RevNo);
                }
                else
                {
                rsTmp3=tmpStmt3.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL WHERE COMPANY_ID="+mCompanyID+" AND DECLARATION_ID='" + mDecID + "' AND SR_NO=" + mSrNo);    
                }
                Counter1=0;
                while(rsTmp3.next()) {
                    Counter1=Counter1+1;
                    clsDeclarationFormItemDetail ObjDeclarationFormItemDetails=new clsDeclarationFormItemDetail();
                    ObjDeclarationFormItemDetails.setAttribute("COMPANY_ID",rsTmp3.getLong("COMPANY_ID"));
                    ObjDeclarationFormItemDetails.setAttribute("DECLARATION_ID",rsTmp3.getString("DECLARATION_ID"));
                    ObjDeclarationFormItemDetails.setAttribute("SR_NO",rsTmp3.getLong("SR_NO"));
                    ObjDeclarationFormItemDetails.setAttribute("SRNO",rsTmp3.getLong("SRNO"));
                    ObjDeclarationFormItemDetails.setAttribute("ITEM_ID",rsTmp3.getString("ITEM_ID"));
                    ObjDeclarationFormItemDetails.setAttribute("LOT_NO",rsTmp3.getString("LOT_NO"));
                    ObjDeclarationFormItemDetails.setAttribute("LOT_QTY",rsTmp3.getDouble("LOT_QTY"));
                    ObjDeclarationFormItemDetails.setAttribute("TOTAL_ISSUED_QTY",rsTmp3.getDouble("TOTAL_ISSUED_QTY"));
                    ObjDeclarationFormItemDetails.setAttribute("BAL_QTY",rsTmp3.getDouble("BAL_QTY"));
                    ObjDeclarationFormItemDetails.setAttribute("CANCELED",rsTmp3.getInt("CANCELED"));
                    ObjDeclarationFormItemDetails.setAttribute("CREATED_BY",rsTmp3.getLong("CREATED_BY"));
                    ObjDeclarationFormItemDetails.setAttribute("CREATED_DATE",rsTmp3.getString("CREATED_DATE"));
                    ObjDeclarationFormItemDetails.setAttribute("MODIFIED_BY",rsTmp3.getLong("MODIFIED_BY"));
                    ObjDeclarationFormItemDetails.setAttribute("MODIFIED_DATE",rsTmp3.getString("MODIFIED_DATE"));
                    
                    colDeclarationFormItemDetails.put(Long.toString(Counter1),ObjDeclarationFormItemDetails);
                    
                }//innermost while loop
            }
            
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public  static HashMap getPendingApprovals(long pCompanyID,long pUserID,int pOrder,int FinYearFrom) {
        Connection tmpConn;
        ResultSet rsTmp3=null;
        Statement tmpStmt3=null;
        String strSQL="";
        
        HashMap List=new HashMap();
        long Counter=0;
        
        try {
            tmpConn=data.getConn();
            tmpStmt3=tmpConn.createStatement();
            
            if(pOrder==EITLERPGLOBAL.OnRecivedDate) {
                strSQL="SELECT D_INV_DECLARATION_HEADER.DECLARATION_ID,D_INV_DECLARATION_HEADER.DECLARATION_DATE,RECEIVED_DATE,D_INV_DECLARATION_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_DECLARATION_HEADER,D_COM_DOC_DATA WHERE D_INV_DECLARATION_HEADER.DECLARATION_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_DECLARATION_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_DECLARATION_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_DOC_DATA.USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=13 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_INV_DECLARATION_HEADER.DECLARATION_ID,D_INV_DECLARATION_HEADER.DECLARATION_DATE,RECEIVED_DATE,D_INV_DECLARATION_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_DECLARATION_HEADER,D_COM_DOC_DATA WHERE D_INV_DECLARATION_HEADER.DECLARATION_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_DECLARATION_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_DECLARATION_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_DOC_DATA.USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=13 ORDER BY D_INV_DECLARATION_HEADER.DECLARATION_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_INV_DECLARATION_HEADER.DECLARATION_ID,D_INV_DECLARATION_HEADER.DECLARATION_DATE,RECEIVED_DATE,D_INV_DECLARATION_HEADER.FOR_DEPT_ID AS DEPT_ID FROM D_INV_DECLARATION_HEADER,D_COM_DOC_DATA WHERE D_INV_DECLARATION_HEADER.DECLARATION_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_DECLARATION_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_DECLARATION_HEADER.COMPANY_ID="+pCompanyID+" AND D_COM_DOC_DATA.USER_ID="+pUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=13 ORDER BY D_INV_DECLARATION_HEADER.DECLARATION_ID";
            }
            
            //strSQL="SELECT D_INV_REQ_HEADER.REQ_NO,D_INV_REQ_HEADER.REQ_DATE FROM D_INV_REQ_HEADER,D_COM_DOC_DATA WHERE D_INV_REQ_HEADER.REQ_NO=D_COM_DOC_DATA.DOC_NO AND D_INV_REQ_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_REQ_HEADER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID=2";
            rsTmp3=tmpStmt3.executeQuery(strSQL);
            
            Counter=0;
            while(rsTmp3.next()) {
                if(EITLERPGLOBAL.isWithinDate(rsTmp3.getString("DECLARATION_DATE"),FinYearFrom))
                {
                Counter=Counter+1;
                clsDeclarationForm ObjDoc=new clsDeclarationForm();
                
                //------------- Header Fields --------------------//
                ObjDoc.setAttribute("DECLARATION_ID",rsTmp3.getString("DECLARATION_ID"));
                ObjDoc.setAttribute("DECLARATION_DATE",rsTmp3.getString("DECLARATION_DATE"));
                ObjDoc.setAttribute("RECEIVED_DATE",rsTmp3.getString("RECEIVED_DATE"));
                ObjDoc.setAttribute("DEPT_ID",rsTmp3.getInt("DEPT_ID"));
                // ----------------- End of Header Fields ------------------------------------//
                
                List.put(Long.toString(Counter),ObjDoc);
                }
            }//end of while
            
            //tmpConn.close();
            rsTmp3.close();
            tmpStmt3.close();
            
        }
        catch(Exception e) {
        }
        
        return List;

    }
    
    public static HashMap getDFItemList(int pCompanyID,String pDFNo,boolean pAllData) {
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
        
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            if(pAllData) {
                strSql = "SELECT * FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY ITEM_CODE";
            }
            else{
                strSql = "SELECT * FROM D_INV_DECLARATION_DETAIL WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+pDFNo.trim()+"' " +
                "AND ROUND(CONSUMED_QTY+RETURNED_QTY,2)<ROUND(RECD_QTY,2) ORDER BY ITEM_CODE";
            }
            
            rsTmp2=tmpStmt.executeQuery(strSql);
            rsTmp2.first();
            
            Counter1=0;
            while(! rsTmp2.isAfterLast()) {
                Counter1++;
                clsDeclarationFormItem ObjMRItems=new clsDeclarationFormItem();
                
                ObjMRItems.setAttribute("COMPANY_ID",rsTmp2.getInt("COMPANY_ID"));
                ObjMRItems.setAttribute("DECLARATION_ID",rsTmp2.getString("DECLARATION_ID"));
                ObjMRItems.setAttribute("SR_NO",rsTmp2.getLong("SR_NO"));
                ObjMRItems.setAttribute("ITEM_CODE",rsTmp2.getString("ITEM_CODE"));
                ObjMRItems.setAttribute("DECLARATION_DESC",rsTmp2.getString("DECLARATION_DESC"));
                ObjMRItems.setAttribute("UNIT",rsTmp2.getLong("UNIT"));
                ObjMRItems.setAttribute("RECD_QTY",rsTmp2.getDouble("RECD_QTY"));
                ObjMRItems.setAttribute("CONSUMED_QTY",rsTmp2.getDouble("CONSUMED_QTY"));
                ObjMRItems.setAttribute("RETURNED_QTY",rsTmp2.getDouble("RETURNED_QTY"));
                ObjMRItems.setAttribute("BAL_QTY",rsTmp2.getDouble("BAL_QTY"));
                ObjMRItems.setAttribute("RETURNED",rsTmp2.getBoolean("RETURNED"));
                ObjMRItems.setAttribute("EXP_RETURN_DATE",rsTmp2.getString("EXP_RETURN_DATE"));
                ObjMRItems.setAttribute("RETURNED_DATE",rsTmp2.getString("RETURNED_DATE"));
                ObjMRItems.setAttribute("REMARKS",rsTmp2.getString("REMARKS"));
                
                //Now Fetch Lots.
                int DFSrno=rsTmp2.getInt("SR_NO");
                
                stLot=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                rsLot=stLot.executeQuery("SELECT * FROM D_INV_DECLARATION_DETAIL_DETAIL WHERE COMPANY_ID="+pCompanyID+" AND DECLARATION_ID='"+pDFNo+"' AND SR_NO="+DFSrno);
                rsLot.first();
                
                //Clear existing data
                ObjMRItems.colItemLot.clear();
                
                int Counter2 = 0;
                if(rsLot.getRow()>0) {
                    Counter2=0;
                    while(!rsLot.isAfterLast()) {
                        Counter2++;
                        clsDeclarationFormItemDetail ObjLot=new clsDeclarationFormItemDetail();
                        
                        ObjLot.setAttribute("COMPANY_ID",rsLot.getInt("COMPANY_ID"));
                        ObjLot.setAttribute("DECLARATION_ID",rsLot.getString("DECLARATION_ID"));
                        ObjLot.setAttribute("SR_NO",rsLot.getInt("SR_NO"));
                        ObjLot.setAttribute("SRNO",rsLot.getInt("SRNO"));
                        ObjLot.setAttribute("LOT_NO",rsLot.getString("LOT_NO"));
                        ObjLot.setAttribute("LOT_QTY",rsLot.getDouble("LOT_QTY"));
                        
                        ObjMRItems.colItemLot.put(Integer.toString(Counter2),ObjLot);
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
        catch(Exception e) {
        }
        
        return List;
    }
    

    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_INV_DECLARATION_HEADER_H WHERE COMPANY_ID="+pCompanyID+" AND DECLARATION_ID='"+pDocNo+"'");
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
            rsTmp=stTmp.executeQuery("SELECT * FROM D_INV_DECLARATION_HEADER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND DECLARATION_ID='"+pDocNo+"'");
            
            while(rsTmp.next()) {
                clsDeclarationForm ObjDF=new clsDeclarationForm();
                
                ObjDF.setAttribute("DECLARATION_ID",rsTmp.getString("DECLARATION_ID"));
                ObjDF.setAttribute("DECLARATION_DATE",rsTmp.getString("DECLARATION_DATE"));
                ObjDF.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                ObjDF.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                ObjDF.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                ObjDF.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                ObjDF.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size()+1),ObjDF);
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
            rsTmp=data.getResult("SELECT DECLARATION_ID,APPROVED,CANCELLED FROM D_INV_DECLARATION_HEADER WHERE COMPANY_ID="+pCompanyID+" AND DECLARATION_ID='"+pDocNo+"'");
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
