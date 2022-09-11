    /*
     * clsSales_Party.java
     *
     * Created on April 3, 2009, 10:22 AM
     */

package EITLERP.Production.ReportUI;

import java.util.*;
import java.util.Date;
import java.text.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;
import java.text.DecimalFormat;
import java.lang.Double;
import EITLERP.Production.clsFeltProductionApprovalFlow;

/**
 *
 * @author  ashutosh
 */
public class clsProforma {
    
    public String LastError="";
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=708; //72
    public HashMap colMRItems;
    
    public Variant getAttribute(String PropName) {
        if(!props.containsKey(PropName)) {
            return new Variant("");
        }
        else {
            return (Variant) props.get(PropName);
        }
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
    
    /** Creates a new instance of clsSales_Party */
    public clsProforma() {
        LastError = "";
        props=new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("PROFORMA_NO",new Variant(""));
        props.put("PROFORMA_DATE",new Variant(""));
        props.put("PARTY_CD",new Variant(""));
        props.put("NAME",new Variant(""));
        props.put("STATION",new Variant(""));
        props.put("CONTACT",new Variant(""));
        props.put("PHONE",new Variant(""));
        
        props.put("REMARK1",new Variant(0));
        props.put("REMARK2",new Variant(0));
        props.put("REMARK3",new Variant(0));
        props.put("REMARK4",new Variant(0));
        props.put("REMARK5",new Variant(0));
        
        
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("REJECTED",new Variant(0));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("CANCELED", new Variant(0));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("PREFIX",new Variant(""));
        
        //Create a new object for MR Item collection
        colMRItems=new HashMap();
        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("APPROVAL_STATUS",new Variant(""));
        props.put("SEND_DOC_TO",new Variant(0));
        props.put("APPROVER_REMARKS",new Variant(0));
    }
    
    /**Load Data. This method loads data from database to Business Object**/
    public boolean LoadData(long pCompanyID) {
        Ready=false;
        try {
            
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC");
            //rsResultSet1=Stmt.executeQuery("SELECT A.*,PRIORITY_DESC FROM (SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC) AS A LEFT JOIN (SELECT PRIORITY_DESC,PRIORITY_ID FROM PRODUCTION.FELT_PRIORITY_MASTER ) AS B ON A.PRIORITY=B.PRIORITY_ID");
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROFORMA_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROFORMA_NO");
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
            rsResultSet.close();
        }
        catch(Exception e) {
            
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
        
        Statement stHistory,stHeader,stHDetail;
        ResultSet rsHistory,rsHeader,rsHDetail;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ProformaDate=java.sql.Date.valueOf((String)getAttribute("PROFORMA_DATE").getObj());
            
            if((ProformaDate.after(FinFromDate)||ProformaDate.compareTo(FinFromDate)==0)&&(ProformaDate.before(FinToDate)||ProformaDate.compareTo(FinToDate)==0)) {
                //Within the year
            }
            else {
                LastError="Requisition date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL_H WHERE PROFORMA_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //--------- Generate Gatepass requisition no.  ------------
            setAttribute("PROFORMA_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,708, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            /*
            String PartyCode = getAttribute("PARTY_CD").getString();
            if (PartyCode.trim().substring(0,4).equals("NEWD")) {
                int Counter=data.getIntValueFromDB("SELECT MAX(CONVERT(SUBSTR(PARTY_CODE,5),SIGNED))+1 FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE LIKE 'N%'");
                PartyCode = "NEWD"+Counter;
                setAttribute("SEASON_CODE","");
                setAttribute("REG_DATE","0000-00-00");
            } else {
                setAttribute("SEASON_CODE",getSeasonCode());
                setAttribute("REG_DATE",EITLERPGLOBAL.getCurrentDateDB());
            }
             
            //rsResultSet.first();
            setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            setAttribute("PARTY_CODE", PartyCode);
             
            if(data.IsRecordExist("SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+getAttribute("PARTY_CODE").getString()+"'")) {
                LastError="Party Code already exist.";
                return false;
            }
             
             */
            
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            
            //rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsResultSet.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsResultSet.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            rsResultSet.updateString("NAME",getAttribute("NAME").getString());
            
            rsResultSet.updateString("STATION",getAttribute("STATION").getString());
            rsResultSet.updateString("CONTACT",getAttribute("CONTACT").getString());
            
            rsResultSet.updateString("PHONE",getAttribute("PHONE").getString());
            
            rsResultSet.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsResultSet.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsResultSet.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsResultSet.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsResultSet.updateString("REMARK5",getAttribute("REMARK5").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("ORDER_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            //rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsResultSet.updateString("PRIORITY",(String)getAttribute("PRIORITY").getObj());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELED",false);
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateDouble("OTHER",getAttribute("OTHER").getDouble());
            /*
            rsResultSet.updateDouble("DISCOUNT",getAttribute("DISCOUNT").getDouble());
            rsResultSet.updateDouble("SEAM_CHG",getAttribute("SEAM_CHG").getDouble());
            rsResultSet.updateDouble("EXCISE",getAttribute("EXCISE").getDouble());
            rsResultSet.updateString("APPROX_AMOUNT",getAttribute("APPROX_AMOUNT").getString());
            rsResultSet.updateString("PROD_IND",getAttribute("PROD_IND").getString());
            rsResultSet.updateString("PROD_IND_A",getAttribute("PROD_IND_A").getString());
            rsResultSet.updateString("PROD_IND_B",getAttribute("PROD_IND_B").getString());
            rsResultSet.updateString("PROD_IND_C",getAttribute("PROD_IND_C").getString());
            rsResultSet.updateString("PROD_IND_D",getAttribute("PROD_IND_D").getString());
            rsResultSet.updateString("ORDER_DDMM_A",getAttribute("ORD_DDMM_A").getString());
            rsResultSet.updateString("ORDER_DDMM_B",getAttribute("ORD_DDMM_B").getString());
            rsResultSet.updateDouble("ORDER_DDMM_C",getAttribute("ORD_DDMM_C").getDouble());
             */
            //rsResultSet.updateString("ORDER_DDMM_D",getAttribute("ORD_DDMM_D").getString());
            //rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            //rsResultSet.updateString("PRIORITY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID);
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID+"-"+getAttribute("LAST_PRIO_USR").getString());
            //rsResultSet.updateString("PRIORITY_DATE", getAttribute("PRIORITY_DATE").getString());
            //rsResultSet.updateString("PRIORITY_DATE",date);
            /*rsResultSet.updateInt("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getInt());
            rsResultSet.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsResultSet.updateDouble("AMOUNT_LIMIT",getAttribute("AMOUNT_LIMIT").getDouble());
            rsResultSet.updateString("CST_NO",getAttribute("CST_NO").getString());
            rsResultSet.updateString("CST_DATE",getAttribute("CST_DATE").getString());
            rsResultSet.updateString("ECC_NO",getAttribute("ECC_NO").getString());
            rsResultSet.updateString("ECC_DATE",getAttribute("ECC_DATE").getString());
            rsResultSet.updateString("TIN_NO",getAttribute("TIN_NO").getString());
            rsResultSet.updateString("TIN_DATE",getAttribute("TIN_DATE").getString());
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsResultSet.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateString("CATEGORY",getAttribute("CATEGORY").getString());
            rsResultSet.updateInt("OTHER_BANK_ID",getAttribute("OTHER_BANK_ID").getInt());
            rsResultSet.updateString("OTHER_BANK_NAME",getAttribute("OTHER_BANK_NAME").getString());
            rsResultSet.updateString("OTHER_BANK_ADDRESS",getAttribute("OTHER_BANK_ADDRESS").getString());
            rsResultSet.updateString("OTHER_BANK_CITY",getAttribute("OTHER_BANK_CITY").getString());
            rsResultSet.updateString("INSURANCE_CODE",getAttribute("INSURANCE_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateDouble("DELAY_INTEREST_PER",getAttribute("DELAY_INTEREST_PER").getDouble());
            rsResultSet.updateString("ACCOUNT_CODES",getAttribute("ACCOUNT_CODES").getString());
            rsResultSet.updateInt("DO_NOT_ALLOW_INTEREST",getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
             
            rsResultSet.updateString("ACCOUNT_CODES",getAttribute("ACCOUNT_CODES").getString());
             
            rsResultSet.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
             */
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("APPROVER_REMARKS").getString());
            
            rsHistory.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsHistory.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsHistory.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            rsHistory.updateString("NAME",getAttribute("NAME").getString());
            
            rsHistory.updateString("STATION",getAttribute("STATION").getString());
            rsHistory.updateString("CONTACT",getAttribute("CONTACT").getString());
            rsHistory.updateString("PHONE",getAttribute("PHONE").getString());
            
            rsHistory.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsHistory.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsHistory.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsHistory.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsHistory.updateString("REMARK5",getAttribute("REMARK5").getString());
            
            
            
            
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            System.out.println(1);
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            
            //long nCompanyID=(long)getAttribute("COMPANY_ID").getVal();
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsProformaItem ObjMRItems=(clsProformaItem) colMRItems.get(Integer.toString(i));
                
                //ObjMRItems.setAttribute("BAL_QTY",(float)ObjMRItems.getAttribute("REQ_QTY").getVal());
                rsTmp.moveToInsertRow();
                
              //rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsTmp.updateString("PROFORMA_NO",(String)getAttribute("PROFORMA_NO").getObj());
                rsTmp.updateInt("SR_NO",i);
                rsTmp.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("ITEM_DESC",(String)ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsTmp.updateString("POSITION",(String)ObjMRItems.getAttribute("POSITION").getObj());
                rsTmp.updateDouble("LNGTH",(double)ObjMRItems.getAttribute("LNGTH").getVal());
                rsTmp.updateDouble("WIDTH",(double)ObjMRItems.getAttribute("WIDTH").getVal());
                rsTmp.updateLong("GSQ",(int)ObjMRItems.getAttribute("GSQ").getVal());
                
                rsTmp.updateString("STYLE",(String)ObjMRItems.getAttribute("STYLE").getObj());
                rsTmp.updateString("WEIGHT",df.format(ObjMRItems.getAttribute("WEIGHT").getVal()).toString());
                rsTmp.updateDouble("RATE",(double)ObjMRItems.getAttribute("RATE").getVal());
                rsTmp.updateFloat("BAS_AMT",(float)ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsTmp.updateString("DISC_PER",df.format(ObjMRItems.getAttribute("DISC_PER").getVal()).toString());
                rsTmp.updateFloat("DISAMT",(float)ObjMRItems.getAttribute("DISAMT").getVal());
                rsTmp.updateFloat("DISBASAMT",(float)ObjMRItems.getAttribute("DISBASAMT").getVal()); //Balance Qty will be balance qty
                rsTmp.updateFloat("EXCISE",(float)ObjMRItems.getAttribute("EXCISE").getVal());
                rsTmp.updateFloat("SEAM_CHG",(float)ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsTmp.updateFloat("SEAM_CHG_PER",(float)ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                rsTmp.updateFloat("INSACC_AMT",(float)ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsTmp.updateFloat("INV_AMT",(float)ObjMRItems.getAttribute("INV_AMT").getVal());
                rsTmp.updateString("INSURANCE_CODE",(String)ObjMRItems.getAttribute("INSURANCE_CODE").getObj());
                rsTmp.updateString("REF_NO",(String)ObjMRItems.getAttribute("REF_NO").getObj());
                rsTmp.updateString("CONF_NO",(String)ObjMRItems.getAttribute("CONF_NO").getObj());
                rsTmp.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsTmp.updateString("ZONE",(String)ObjMRItems.getAttribute("ZONE").getObj());
                rsTmp.updateString("PRIORITY_DATE",ObjMRItems.getAttribute("PRIORITY_DATE").getString());
              //System.out.println(ObjMRItems.getAttribute("PRIORITY_DATE").getString());
                rsTmp.updateString("INCHARGE_NAME",(String)ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsTmp.updateString("PRODUCT_CD",(String)ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                
                rsTmp.updateString("PO_NO",(String)ObjMRItems.getAttribute("PO_NO").getObj());
                rsTmp.updateString("PO_DATE",ObjMRItems.getAttribute("PO_DATE").getString());
                
                
                rsTmp.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                //rsTmp.updateBoolean("Calculate_Weight",false);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());                
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",1);
                //rsTmp.updateLong("COMPANY_ID",(long)nCompanyID);
                rsHDetail.updateString("PROFORMA_NO",(String)getAttribute("PROFORMA_NO").getObj());
                rsHDetail.updateInt("SR_NO",i);
                rsHDetail.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("POSITION",(String)ObjMRItems.getAttribute("POSITION").getObj());
                rsHDetail.updateDouble("LNGTH",(double)ObjMRItems.getAttribute("LNGTH").getVal());
                rsHDetail.updateDouble("WIDTH",(double)ObjMRItems.getAttribute("WIDTH").getVal());
                rsHDetail.updateLong("GSQ",(int)ObjMRItems.getAttribute("GSQ").getVal());
                rsHDetail.updateString("STYLE",(String)ObjMRItems.getAttribute("STYLE").getObj());
                rsHDetail.updateString("WEIGHT",df.format(ObjMRItems.getAttribute("WEIGHT").getVal()).toString());
                rsHDetail.updateDouble("RATE",(double)ObjMRItems.getAttribute("RATE").getVal());
                rsHDetail.updateFloat("BAS_AMT",(float)ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsHDetail.updateString("DISC_PER",df.format(ObjMRItems.getAttribute("DISC_PER").getVal()).toString());
                rsHDetail.updateFloat("DISAMT",(float)ObjMRItems.getAttribute("DISAMT").getVal());
                rsHDetail.updateFloat("DISBASAMT",(float)ObjMRItems.getAttribute("DISBASAMT").getVal()); //Balance Qty will be balance qty
                rsHDetail.updateFloat("EXCISE",(float)ObjMRItems.getAttribute("EXCISE").getVal());
                rsHDetail.updateFloat("SEAM_CHG",(float)ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsHDetail.updateFloat("SEAM_CHG_PER",(float)ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                rsHDetail.updateFloat("INSACC_AMT",(float)ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsHDetail.updateFloat("INV_AMT",(float)ObjMRItems.getAttribute("INV_AMT").getVal());
                rsHDetail.updateString("INSURANCE_CODE",(String)ObjMRItems.getAttribute("INSURANCE_CODE").getObj());
                rsHDetail.updateString("REF_NO",(String)ObjMRItems.getAttribute("REF_NO").getObj());
                rsHDetail.updateString("CONF_NO",(String)ObjMRItems.getAttribute("CONF_NO").getObj());
                rsHDetail.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsHDetail.updateString("ZONE",(String)ObjMRItems.getAttribute("ZONE").getObj());
                rsHDetail.updateString("PRIORITY_DATE",(String)ObjMRItems.getAttribute("PRIORITY_DATE").getObj());
                rsHDetail.updateString("INCHARGE_NAME",(String)ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsHDetail.updateString("PRODUCT_CD",(String)ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                
                rsHDetail.updateString("PO_NO",(String)ObjMRItems.getAttribute("PO_NO").getObj());
                rsHDetail.updateString("PO_DATE",(String)ObjMRItems.getAttribute("PO_DATE").getObj());
                //rsHDetail.updateBoolean("Calculate_Weight",false);
                
                
                rsHDetail.updateLong("CREATED_BY",(long)getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                
                rsHDetail.insertRow();
            }
            
            
            //===================== Update the Approval Flow ======================//
            
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
             
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow = new clsFeltProductionApprovalFlow();
            
            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsProforma.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("PROFORMA_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("PROFORMA_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName="PRODUCTION.FELT_PROFORMA_INVOICE_HEADER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="PROFORMA_NO";
             
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
            
            //================= Approval Flow Update complete ===================//
            
            MoveLast();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            LastError= e.getMessage();
            
            return false;
        }
        
    }
        
     public boolean Update() {
        
        Statement stHistory,stHeader,stHDetail;
        ResultSet rsHistory,rsHeader,rsHDetail;
        boolean Validate=true;
        
        try {
            
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            if(AStatus.equals("R")) {
                Validate=false;
            }            
            Validate=true;            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate=java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date ProformaDate=java.sql.Date.valueOf((String)getAttribute("PROFORMA_DATE").getObj());
            
            if((ProformaDate.after(FinFromDate)||ProformaDate.compareTo(FinFromDate)==0)&&(ProformaDate.before(FinToDate)||ProformaDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Proforma date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stHDetail=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail=stHDetail.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL_H WHERE PROFORMA_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo=getAttribute("PROFORMA_NO").getString();
            
            //** Open History Table Connections **//
            //  stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
            //  rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD='1'"); // '1' for restricting all data retrieval
            //  rsHistory.first();
            //** --------------------------------**//
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            
            
            // rsResultSet.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsResultSet.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsResultSet.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            rsResultSet.updateString("NAME",getAttribute("NAME").getString());
            
            rsResultSet.updateString("STATION",getAttribute("STATION").getString());
            rsResultSet.updateString("CONTACT",getAttribute("CONTACT").getString());
            
            rsResultSet.updateString("PHONE",getAttribute("PHONE").getString());
            
            rsResultSet.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsResultSet.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsResultSet.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsResultSet.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsResultSet.updateString("REMARK5",getAttribute("REMARK5").getString());
            
            rsResultSet.updateInt("HIERARCHY_ID",(int)getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateInt("CREATED_BY",(int)getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            //rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsResultSet.updateString("PRIORITY",(String)getAttribute("PRIORITY").getObj());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateInt("MODIFIED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELED",false);
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='"+(String)getAttribute("PROFORMA_NO").getObj()+"'");
            RevNo++;
            String RevDocNo=(String)getAttribute("PROFORMA_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            //rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateInt("UPDATED_BY",(int)getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
            rsHistory.updateString("PROFORMA_NO", getAttribute("PROFORMA_NO").getString());
            rsHistory.updateString("PROFORMA_DATE", getAttribute("PROFORMA_DATE").getString());
            rsHistory.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            rsHistory.updateString("NAME",getAttribute("NAME").getString());
            
            rsHistory.updateString("STATION",getAttribute("STATION").getString());
            rsHistory.updateString("CONTACT",getAttribute("CONTACT").getString());
            rsHistory.updateString("PHONE",getAttribute("PHONE").getString());
            
            rsHistory.updateString("REMARK1",getAttribute("REMARK1").getString());
            rsHistory.updateString("REMARK2",getAttribute("REMARK2").getString());
            rsHistory.updateString("REMARK3",getAttribute("REMARK3").getString());
            rsHistory.updateString("REMARK4",getAttribute("REMARK4").getString());
            rsHistory.updateString("REMARK5",getAttribute("REMARK5").getString());
            
            
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.insertRow();
            
            //First remove the old rows
            String mProformaNo=(String)getAttribute("PROFORMA_NO").getObj();
            
            data.Execute("DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='"+mProformaNo+"'");
            
            ResultSet rsTmp;
            Statement tmpStmt;
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            rsTmp=tmpStmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='1'");
            
            //Now Insert records into detail table
            for(int i=1;i<=colMRItems.size();i++) {
                clsProformaItem ObjMRItems=(clsProformaItem) colMRItems.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                rsTmp.updateString("PROFORMA_NO",(String)getAttribute("PROFORMA_NO").getObj());
                rsTmp.updateLong("SR_NO",i);
                rsTmp.updateString("PRIORITY_DATE",(String)ObjMRItems.getAttribute("PRIORITY_DATE").getObj());
                rsTmp.updateString("INCHARGE_NAME",(String)ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsTmp.updateString("PRIORITY",(String)ObjMRItems.getAttribute("PRIORITY").getObj());
                //rsTmp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                
                rsTmp.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsTmp.updateString("ORDER_DATE",(String)ObjMRItems.getAttribute("ORDER_DATE").getObj());
                rsTmp.updateString("RCVD_DATE",(String)ObjMRItems.getAttribute("RCVD_DATE").getObj());
                rsTmp.updateString("DELIV_DATE",(String)ObjMRItems.getAttribute("DELIV_DATE").getObj());
                rsTmp.updateString("COMM_DATE",(String)ObjMRItems.getAttribute("COMM_DATE").getObj());
                rsTmp.updateString("PRODUCT_CD",(String)ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                rsTmp.updateString("ITEM",(String)ObjMRItems.getAttribute("ITEM").getObj());
                rsTmp.updateString("STYLE",(String)ObjMRItems.getAttribute("STYLE").getObj());
                rsTmp.updateString("LNGTH",Double.toString(ObjMRItems.getAttribute("LNGTH").getVal()));
                rsTmp.updateString("RCVD_MTR",Double.toString(ObjMRItems.getAttribute("RCVD_MTR").getVal()));
                rsTmp.updateString("WIDTH",Double.toString(ObjMRItems.getAttribute("WIDTH").getVal()));
                rsTmp.updateString("RECD_WDTH",Double.toString(ObjMRItems.getAttribute("RECD_WDTH").getVal()));
                rsTmp.updateInt("GSQ",(int)ObjMRItems.getAttribute("GSQ").getVal());
                rsTmp.updateString("WEIGHT",Double.toString(ObjMRItems.getAttribute("WEIGHT").getVal()));
                rsTmp.updateFloat("RECD_KG",(float)ObjMRItems.getAttribute("RECD_KG").getVal());
                rsTmp.updateString("RATE",Double.toString(ObjMRItems.getAttribute("RATE").getVal()));
                rsTmp.updateFloat("BAS_AMT",(float)ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsTmp.updateString("MEMO_DATE",(String)ObjMRItems.getAttribute("MEMO_DATE").getObj());
                rsTmp.updateString("DISC_PER",Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsTmp.updateFloat("DISAMT",(float)ObjMRItems.getAttribute("DISAMT").getVal());
                rsTmp.updateFloat("DISBASAMT",(float)ObjMRItems.getAttribute("DISBASAMT").getVal());
                rsTmp.updateFloat("EXCISE",(float)ObjMRItems.getAttribute("EXCISE").getVal());
                rsTmp.updateFloat("SEAM_CHG",(float)ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsTmp.updateFloat("SEAM_CHG_PER",(float)ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                //rsTmp.updateFloat("WPSC",(float)ObjMRItems.getAttribute("WPSC").getVal());
                rsTmp.updateFloat("INSACC_AMT",(float)ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsTmp.updateFloat("INV_AMT",(float)ObjMRItems.getAttribute("INV_AMT").getVal());
                rsTmp.updateInt("DAYS",(int)ObjMRItems.getAttribute("DAYS").getVal());
                rsTmp.updateString("REF_NO",(String)ObjMRItems.getAttribute("REF_NO").getObj());
                rsTmp.updateString("CONF_NO",(String)ObjMRItems.getAttribute("CONF_NO").getObj());
                rsTmp.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsTmp.updateString("POSITION",(String)ObjMRItems.getAttribute("POSITION").getObj());
                rsTmp.updateString("STATION",(String)ObjMRItems.getAttribute("STATION").getObj());
                rsTmp.updateString("ZONE",(String)ObjMRItems.getAttribute("ZONE").getObj());
                rsTmp.updateString("INSURANCE_CODE",(String)ObjMRItems.getAttribute("INSURANCE_CODE").getObj());
                rsTmp.updateString("ITEM_DESC",(String)ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsTmp.updateString("SYN_PER",(String)ObjMRItems.getAttribute("SYN_PER").getObj());
                
                rsTmp.updateString("PO_NO",(String)ObjMRItems.getAttribute("PO_NO").getObj());
                rsTmp.updateString("PO_DATE",(String)ObjMRItems.getAttribute("PO_DATE").getObj());
                //rsTmp.updateBoolean("Calculate_Weight",false);
                
                
                rsTmp.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsTmp.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsTmp.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsTmp.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsTmp.updateBoolean("CHANGED",true);
                rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsTmp.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO",RevNo);
                rsHDetail.updateString("PROFORMA_NO",(String)getAttribute("PROFORMA_NO").getObj());
                rsHDetail.updateLong("SR_NO",i);
                rsHDetail.updateString("PRIORITY_DATE",(String)ObjMRItems.getAttribute("PRIORITY_DATE").getObj());
                rsHDetail.updateString("INCHARGE_NAME",(String)ObjMRItems.getAttribute("INCHARGE_NAME").getObj());
                rsHDetail.updateString("PRIORITY",(String)ObjMRItems.getAttribute("PRIORITY").getObj());
                //rsTmp.updateLong("COMPANY_ID",(long)getAttribute("COMPANY_ID").getVal());
                
                rsHDetail.updateString("PIECE_NO",(String)ObjMRItems.getAttribute("PIECE_NO").getObj());
                rsHDetail.updateString("ORDER_DATE",(String)ObjMRItems.getAttribute("ORDER_DATE").getObj());
                rsHDetail.updateString("RCVD_DATE",(String)ObjMRItems.getAttribute("RCVD_DATE").getObj());
                rsHDetail.updateString("DELIV_DATE",(String)ObjMRItems.getAttribute("DELIV_DATE").getObj());
                rsHDetail.updateString("COMM_DATE",(String)ObjMRItems.getAttribute("COMM_DATE").getObj());
                rsHDetail.updateString("PRODUCT_CD",(String)ObjMRItems.getAttribute("PRODUCT_CD").getObj());
                rsHDetail.updateString("ITEM",(String)ObjMRItems.getAttribute("ITEM").getObj());
                rsHDetail.updateString("STYLE",(String)ObjMRItems.getAttribute("STYLE").getObj());
                rsHDetail.updateString("LNGTH",Double.toString(ObjMRItems.getAttribute("LNGTH").getVal()));
                rsHDetail.updateString("RCVD_MTR",Double.toString(ObjMRItems.getAttribute("RCVD_MTR").getVal()));
                rsHDetail.updateString("WIDTH",Double.toString(ObjMRItems.getAttribute("WIDTH").getVal()));
                rsHDetail.updateString("RECD_WDTH",Double.toString(ObjMRItems.getAttribute("RECD_WDTH").getVal()));
                rsHDetail.updateInt("GSQ",(int)ObjMRItems.getAttribute("GSQ").getVal());
                rsHDetail.updateString("WEIGHT",Double.toString(ObjMRItems.getAttribute("WEIGHT").getVal()));
                rsHDetail.updateFloat("RECD_KG",(float)ObjMRItems.getAttribute("RECD_KG").getVal());
                rsHDetail.updateString("RATE",Double.toString(ObjMRItems.getAttribute("RATE").getVal()));
                rsHDetail.updateFloat("BAS_AMT",(float)ObjMRItems.getAttribute("BAS_AMT").getVal());
                rsHDetail.updateString("MEMO_DATE",(String)ObjMRItems.getAttribute("MEMO_DATE").getObj());
                rsHDetail.updateString("DISC_PER",Double.toString(ObjMRItems.getAttribute("DISC_PER").getVal()));
                rsHDetail.updateFloat("DISAMT",(float)ObjMRItems.getAttribute("DISAMT").getVal());
                rsHDetail.updateFloat("DISBASAMT",(float)ObjMRItems.getAttribute("DISBASAMT").getVal());
                rsHDetail.updateFloat("EXCISE",(float)ObjMRItems.getAttribute("EXCISE").getVal());
                rsHDetail.updateFloat("SEAM_CHG",(float)ObjMRItems.getAttribute("SEAM_CHG").getVal());
                rsHDetail.updateFloat("SEAM_CHG_PER",(float)ObjMRItems.getAttribute("SEAM_CHG_PER").getVal());
                
                rsHDetail.updateFloat("PO_NO",(float)ObjMRItems.getAttribute("PO_NO").getVal());
                rsHDetail.updateFloat("PO_DATE",(float)ObjMRItems.getAttribute("PO_DATE").getVal());
                //rsHDetail.updateBoolean("Calculate_Weight",false);
                
                //rsHDetail.updateFloat("WPSC",(float)ObjMRItems.getAttribute("WPSC").getVal());
                rsHDetail.updateFloat("INSACC_AMT",(float)ObjMRItems.getAttribute("INSACC_AMT").getVal());
                rsHDetail.updateFloat("INV_AMT",(float)ObjMRItems.getAttribute("INV_AMT").getVal());
                rsHDetail.updateInt("DAYS",(int)ObjMRItems.getAttribute("DAYS").getVal());
                rsHDetail.updateString("REF_NO",(String)ObjMRItems.getAttribute("REF_NO").getObj());
                rsHDetail.updateString("CONF_NO",(String)ObjMRItems.getAttribute("CONF_NO").getObj());
                rsHDetail.updateString("MACHINE_NO",(String)ObjMRItems.getAttribute("MACHINE_NO").getObj());
                rsHDetail.updateString("POSITION",(String)ObjMRItems.getAttribute("POSITION").getObj());
                rsHDetail.updateString("STATION",(String)ObjMRItems.getAttribute("STATION").getObj());
                rsHDetail.updateString("ZONE",(String)ObjMRItems.getAttribute("ZONE").getObj());
                rsHDetail.updateString("INSURANCE_CODE",(String)ObjMRItems.getAttribute("INSURANCE_CODE").getObj());
                rsHDetail.updateString("ITEM_DESC",(String)ObjMRItems.getAttribute("ITEM_DESC").getObj());
                rsHDetail.updateString("SYN_PER",(String)ObjMRItems.getAttribute("SYN_PER").getObj());
                
                
                rsHDetail.updateLong("CREATED_BY",(long) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE",(String)getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY",(long) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE",(String)getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateBoolean("CHANGED",true);
                rsHDetail.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.insertRow();
            }
            
      
            //======== Update the Approval Flow =========
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
       
            //ApprovalFlow ObjFlow=new ApprovalFlow();
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
            
            //ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.ModuleID=clsProforma.ModuleID;
            //ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocNo=(String)getAttribute("PROFORMA_NO").getObj();
            ObjFlow.DocDate=(String)getAttribute("PROFORMA_DATE").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="PRODUCTION.FELT_PROFORMA_INVOICE_HEADER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="PROFORMA_NO";
            //String qry = "UPDATE FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CODE").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsSales_Party.ModuleID;
            //String qry = "UPDATE PRODUCTION.FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CD").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsOrderParty.ModuleID;
            //data.Execute(qry);
       
            //==== Handling Rejected Documents ==========//
             AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();       
            if(AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from FELT_DOC_DATA
                //data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
       
                //ObjFlow.IsCreator=true;
                ObjFlow.To=(int)getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo=true;
            }
            //==========================================//
       
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
       
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE PRODUCTION.FELT_PROFORMA_INVOICE_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PROFORMA_NO='"+ObjFlow.DocNo+"' ");
                 
                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID="+clsProforma.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                ObjFlow.IsCreator=true;
            }
            //==========================================//
       
       
            if(ObjFlow.Status.equals("H")) {
                //Don't update the Flow if on hold
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
            return true;
            
        }
        catch(Exception e) {
            e.printStackTrace(); 
            LastError = e.getMessage();
            return false;
        }
    }         
        
    
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        tmpConn=data.getConn();
        
        long Counter=0;
        int RevNo=0;
        
        try {
            if(HistoryView) {
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("PROFORMA_NO",rsResultSet.getString("PROFORMA_NO"));
            setAttribute("PROFORMA_DATE",rsResultSet.getString("PROFORMA_DATE"));
            setAttribute("PARTY_CD",rsResultSet.getString("PARTY_CD"));
            setAttribute("NAME",rsResultSet.getString("NAME"));
            setAttribute("STATION",rsResultSet.getString("STATION"));
            setAttribute("CONTACT",rsResultSet.getString("CONTACT"));
            setAttribute("PHONE",rsResultSet.getString("PHONE"));
            
            setAttribute("REMARK1",rsResultSet.getString("REMARK1"));
            setAttribute("REMARK2",rsResultSet.getString("REMARK2"));
            setAttribute("REMARK3",rsResultSet.getString("REMARK3"));
            setAttribute("REMARK4",rsResultSet.getString("REMARK4"));
            setAttribute("REMARK5",rsResultSet.getString("REMARK5"));
            
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CREATED_BY",rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            //setAttribute("REJECTED",rsResultSet.getInt("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELED",rsResultSet.getInt("CANCELED"));
            setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));
            
            //Now Populate the collection
            //first clear the collection
            colMRItems.clear();
            
            //String mCompanyID=Long.toString( (long) getAttribute("COMPANY_ID").getVal());
            String mProformaNo=(String) getAttribute("PROFORMA_NO").getObj();
            
            tmpStmt=tmpConn.createStatement();
            if(HistoryView) {
                rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight,0 as SEAM_CHG_PER FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL_H WHERE PROFORMA_NO='"+mProformaNo+"' AND REVISION_NO="+RevNo+" ORDER BY SR_NO");
            }
            else {
                rsTmp=tmpStmt.executeQuery("SELECT *,false as Calculate_Weight,0 as SEAM_CHG_PER FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='"+mProformaNo+"' ORDER BY SR_NO");
            }
            rsTmp.first();
            
            Counter=0;
            while(!rsTmp.isAfterLast() && rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsProformaItem ObjMRItems = new clsProformaItem();                
                ObjMRItems.setAttribute("PROFORMA_NO",rsTmp.getString("PROFORMA_NO"));
                ObjMRItems.setAttribute("SR_NO",rsTmp.getLong("SR_NO"));
                ObjMRItems.setAttribute("PRIORITY_DATE",rsTmp.getString("PRIORITY_DATE"));
                ObjMRItems.setAttribute("INCHARGE_NAME",rsTmp.getString("INCHARGE_NAME"));
                //System.out.println(rsTmp.getString("INCHARGE_NAME"));
                ObjMRItems.setAttribute("PRIORITY",rsTmp.getString("PRIORITY"));
                ObjMRItems.setAttribute("PIECE_NO",rsTmp.getString("PIECE_NO"));
                ObjMRItems.setAttribute("ORDER_DATE",rsTmp.getString("ORDER_DATE"));
                ObjMRItems.setAttribute("RCVD_DATE",rsTmp.getString("RCVD_DATE"));
                ObjMRItems.setAttribute("DELIV_DATE",rsTmp.getString("DELIV_DATE"));
                ObjMRItems.setAttribute("COMM_DATE",rsTmp.getString("COMM_DATE"));
                ObjMRItems.setAttribute("PRODUCT_CD",rsTmp.getString("PRODUCT_CD"));
                ObjMRItems.setAttribute("ITEM",rsTmp.getString("ITEM"));
                ObjMRItems.setAttribute("STYLE",rsTmp.getString("STYLE"));
                ObjMRItems.setAttribute("LNGTH",EITLERPGLOBAL.round(rsTmp.getDouble("LNGTH"),2));
                ObjMRItems.setAttribute("RCVD_MTR",EITLERPGLOBAL.round(rsTmp.getDouble("RCVD_MTR"),2));
                ObjMRItems.setAttribute("WIDTH",EITLERPGLOBAL.round(rsTmp.getDouble("WIDTH"),2));
                ObjMRItems.setAttribute("RECD_WDTH",EITLERPGLOBAL.round(rsTmp.getDouble("RECD_WDTH"),2));
                ObjMRItems.setAttribute("GSQ",rsTmp.getInt("GSQ"));
                ObjMRItems.setAttribute("WEIGHT",rsTmp.getDouble("WEIGHT"));
                ObjMRItems.setAttribute("RECD_KG",EITLERPGLOBAL.round(rsTmp.getFloat("RECD_KG"),2));
                ObjMRItems.setAttribute("RATE",EITLERPGLOBAL.round(rsTmp.getDouble("RATE"),2));
                ObjMRItems.setAttribute("BAS_AMT",EITLERPGLOBAL.round(rsTmp.getFloat("BAS_AMT"),2));
                ObjMRItems.setAttribute("MEMO_DATE",rsTmp.getString("MEMO_DATE"));                
                ObjMRItems.setAttribute("DISC_PER",rsTmp.getDouble("DISC_PER"));
                ObjMRItems.setAttribute("DISAMT",EITLERPGLOBAL.round(rsTmp.getFloat("DISAMT"),2));
                ObjMRItems.setAttribute("DISBASAMT",EITLERPGLOBAL.round(rsTmp.getFloat("DISBASAMT"),2));
                ObjMRItems.setAttribute("EXCISE",EITLERPGLOBAL.round(rsTmp.getFloat("EXCISE"),2));
                ObjMRItems.setAttribute("SEAM_CHG",EITLERPGLOBAL.round(rsTmp.getFloat("SEAM_CHG"),2)); //SEAM CHG
                ObjMRItems.setAttribute("SEAM_CHG_PER",EITLERPGLOBAL.round(rsTmp.getFloat("SEAM_CHG_PER"), 2)); 
                ObjMRItems.setAttribute("INSACC_AMT",EITLERPGLOBAL.round(rsTmp.getFloat("INSACC_AMT"),2));
                ObjMRItems.setAttribute("INV_AMT",EITLERPGLOBAL.round(rsTmp.getFloat("INV_AMT"),2));
                ObjMRItems.setAttribute("DAYS",rsTmp.getInt("DAYS"));
                ObjMRItems.setAttribute("REF_NO",rsTmp.getString("REF_NO"));
                ObjMRItems.setAttribute("CONF_NO",rsTmp.getString("CONF_NO"));
                ObjMRItems.setAttribute("MACHINE_NO",rsTmp.getString("MACHINE_NO"));
                ObjMRItems.setAttribute("POSITION",rsTmp.getString("POSITION"));
                ObjMRItems.setAttribute("STATION",rsTmp.getString("STATION"));
                ObjMRItems.setAttribute("ZONE",rsTmp.getString("ZONE"));
                ObjMRItems.setAttribute("INSURANCE_CODE",rsTmp.getString("INSURANCE_CODE"));
                ObjMRItems.setAttribute("ITEM_DESC",rsTmp.getString("ITEM_DESC"));
                ObjMRItems.setAttribute("SYN_PER",rsTmp.getString("SYN_PER"));
                
                ObjMRItems.setAttribute("PO_NO",rsTmp.getString("PO_NO"));
                ObjMRItems.setAttribute("PO_DATE",rsTmp.getString("PO_DATE"));
                ObjMRItems.setAttribute("Calculate_Weight",rsTmp.getBoolean("Calculate_Weight"));
                
                ObjMRItems.setAttribute("CREATED_BY",rsTmp.getLong("CREATED_BY"));
                ObjMRItems.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjMRItems.setAttribute("MODIFIED_BY",rsTmp.getLong("MODIFIED_BY"));
                ObjMRItems.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                //ObjMRItems.setAttribute("CANCELED",rsTmp.getInt("CANCELED"));                
                colMRItems.put(Long.toString(Counter),ObjMRItems);
                rsTmp.next();
            }
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=708 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND STATUS='W'";
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
    
    public static HashMap getHistoryList(int CompanyID,String ProformaNo) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='"+ProformaNo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                clsProforma objParty=new clsProforma();
                    
                objParty.setAttribute("PROFORMA_NO",rsTmp.getString("PROFORMA_NO"));
                objParty.setAttribute("PROFORMA_DATE",rsTmp.getString("PROFORMA_DATE"));
                objParty.setAttribute("REVISION_NO",rsTmp.getInt("REVISION_NO"));
                objParty.setAttribute("UPDATED_BY",rsTmp.getInt("UPDATED_BY"));
                objParty.setAttribute("APPROVAL_STATUS",rsTmp.getString("APPROVAL_STATUS"));
                objParty.setAttribute("ENTRY_DATE",rsTmp.getString("ENTRY_DATE"));
                objParty.setAttribute("APPROVER_REMARKS",rsTmp.getString("APPROVER_REMARKS"));
                    
                    List.put(Integer.toString(List.size()+1),objParty);
                    rsTmp.next();
                }
            }
            rsTmp.close();
            return List;
        }
        catch(Exception e) {
            return List;
        }
    }
    

    
    public boolean Abc(String Condition1){
        System.out.println(Condition1);
        return true;
    }
    
    
    public boolean Filter(String Condition) {
        Ready=false;
        try {
            String strSQL="";
            strSQL+= "SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_DATE>='"+EITLERPGLOBAL.FinFromDateDB+"' AND PROFORMA_DATE<='"+EITLERPGLOBAL.FinToDateDB+"' ORDER BY PROFORMA_NO";
                rsResultSet=Stmt.executeQuery(strSQL);
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
            strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='"+pDocNo+"' AND (APPROVED=1)";
            
            tmpStmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsTmp=tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getInt("COUNT")>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                strSQL="SELECT COUNT(*) AS COUNT FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE MODULE_ID=708 AND DOC_NO='"+pDocNo+"' AND USER_ID="+Integer.toString(pUserID)+" AND TYPE='C' AND STATUS='W'";
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
            int lCompanyID=EITLERPGLOBAL.gCompanyID;
            String lDocNo=(String)getAttribute("PROFORMA_NO").getObj();
            String strSQL="";
            
            //First check that record is editable
            if(CanDelete(lCompanyID,lDocNo,pUserID)) {
                String strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER WHERE PROFORMA_NO='"+lDocNo+"'";
                data.Execute(strQry);
                strQry = "DELETE FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL WHERE PROFORMA_NO='"+lDocNo+"'";
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
    
    
    public Object getObject(int CompanyID, String PartyCode) {
        String strCondition = " WHERE PROFORMA_NO='" + PartyCode + "' ";
        clsSales_Party objParty = new clsSales_Party();
        objParty.Filter(strCondition);
        return objParty;
    }
    
    public static void CancelParty(int pCompanyID,String pPartyCode,String MainCode) {
        ResultSet rsTmp=null;
        
        if(CanCancelParty(pCompanyID,pPartyCode,MainCode)) {
            boolean Approved=false;
            try {
                rsTmp=data.getResult("SELECT APPROVED FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' ");
                rsTmp.first();
                
                if(rsTmp.getRow()>0) {
                    Approved=rsTmp.getBoolean("APPROVED");
                }
                rsTmp.close();
                
                /*if(!Approved) {
                    data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' AND MODULE_ID="+clsSales_Party.ModuleID);
                }*/
                
                data.Execute("UPDATE D_SAL_PARTY_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' ");
                data.Execute("DELETE FROM FELT_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' ");
            } catch(Exception e) {
            }
        }
    }
    
    public static boolean CanCancelParty(int pCompanyID,String pPartyCode, String MainCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean CanCancel=false;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND COMPANY_ID="+pCompanyID+" AND APPROVED=0 AND CANCELLED=0");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                CanCancel=true;
            }
            
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
        }
        catch(Exception e) {
            
        }
        
        return CanCancel;
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
                strSQL="SELECT PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO,PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=708 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";                
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO,PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=708 ORDER BY PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE";                
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO,PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_PROD_DOC_DATA.DOC_NO=PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO AND PRODUCTION.FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND PRODUCTION.FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID=708 ORDER BY PRODUCTION.FELT_PROFORMA_INVOICE_HEADER.PROFORMA_NO";                
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                
                Counter=Counter+1;
                //clsSales_Party ObjItem=new clsSales_Party();
                clsProforma ObjItem = new clsProforma();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("PROFORMA_NO",rsTmp.getString("PROFORMA_NO"));
                ObjItem.setAttribute("PROFORMA_DATE",rsTmp.getString("PROFORMA_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("DEPT_ID",rsTmp.getInt("DEPT_ID"));                
                // ----------------- End of Header Fields ------------------------------------//
                
                //Put the prepared user object into list
                List.put(Long.toString(Counter),ObjItem);
                
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
    
    public boolean ShowHistory(int pCompanyID,String pProformaNo) {
        Ready=false;
        try {
            //Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_PROFORMA_INVOICE_HEADER_H WHERE PROFORMA_NO='"+pProformaNo+"' ORDER BY REVISION_NO");
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
    
    public static boolean IsPartyExistEx(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static boolean IsPartyExist(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND BLOCKED='N' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //tmpConn.close();
                stTmp.close();
                rsTmp.close();
                
                return true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return false;
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public static String getPartyName(int pCompanyID,String pPartyCode,String pURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn(pURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PARTY_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PartyName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getItemName(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"' ");
            /*String strSQL="SELECT ITEM_DESC FROM PRODUCTION.FELT_RATE_MASTER WHERE SUBSTRING(ITEM_CODE,1,6)=(SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" ";
            if(!pPartyCode.equals("")){
                strSQL+="AND PARTY_CD='"+pPartyCode+"'";
            }
            strSQL+=")";
             */
            String strSQL="SELECT ITEM_DESC FROM PRODUCTION.FELT_RATE_MASTER WHERE SUBSTRING(ITEM_CODE,1,6)= ";
            strSQL+="(SELECT PRODUCT_CODE FROM PRODUCTION.FELT_ORDER_MASTER  WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"' UNION ALL SELECT SUBSTRING(PRODUCT_CD,1,6) FROM PRODUCTION.FELT_PIECE_REGISTER WHERE PIECE_NO='"+pPieceNo+"' AND PARTY_CODE='"+pPartyCode+"')";
            rsTmp=stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("ITEM_DESC");
                
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PartyName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getItemPosition(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Position="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT POSITION FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Position=rsTmp.getString("POSITION");
                
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Position;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getItemLength(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Length="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT LNGTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Length=rsTmp.getString("LNGTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return Length;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getPinInd(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Pinind="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PIN_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Pinind=rsTmp.getString("PIN_IND");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return Pinind;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getSPRInd(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Sprind="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SPR_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Sprind=rsTmp.getString("SPR_IND");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return Sprind;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getChemTrtIn(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Chemtrtin="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CHEM_TRT_IN FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Chemtrtin=rsTmp.getString("CHEM_TRT_IN");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return Chemtrtin;
        }
        catch(Exception e) {
            return "";
        }
    }
    
     public static float getCharges(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float Charges=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CHARGES FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Charges=Float.parseFloat(rsTmp.getString("CHARGES"));
                //Charges=Float.valueOf(rsTmp.getString("CHEM_TRT_IN"));
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return Charges;
        }
        catch(Exception e) {
            return 0;
        }
    }
     
     public static String getSqmind(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Sqmind="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SQM_IND FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Sqmind=rsTmp.getString("SQM_IND");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return Sqmind;
        }
        catch(Exception e) {
            return "";
        }
    }  
     
     public static float getSQMRate(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float sqmrate=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT SQM_RATE FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                sqmrate=Float.parseFloat(rsTmp.getString("SQM_RATE"));
                //Charges=Float.valueOf(rsTmp.getString("CHEM_TRT_IN"));
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return sqmrate;
        }
        catch(Exception e) {
            return 0;
        }
    }
     
     public static float getWTRate(String pItemCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        float wtrate=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WT_RATE FROM PRODUCTION.FELT_RATE_MASTER WHERE ITEM_CODE="+pItemCode+"");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                wtrate=Float.parseFloat(rsTmp.getString("WT_RATE"));
                //Charges=Float.valueOf(rsTmp.getString("CHEM_TRT_IN"));
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return wtrate;
        }
        catch(Exception e) {
            return 0;
        }
    }
     
    
    
    
    public static double getItemWidth(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        double Width=0.00;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT WIDTH FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                //Width=rsTmp.getString("WIDTH");
                Width=rsTmp.getDouble("WIDTH");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Width;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static String getItemStyle(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Style="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT BALNK FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Style=rsTmp.getString("BALNK");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Style;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static String getNewDisamt(String pDiscper,String pBasamt) {
        
       double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);
        
        double newDisamt=0.00;
        newDisamt=(Basamt)*(Discper/100);
        newDisamt=EITLERPGLOBAL.round(newDisamt, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newDisamt);
        /*
                kgsum += Double.parseDouble(rsTmp.getString("WEIGHT"));
                  invsum += Double.parseDouble(rsTmp.getString("INV_AMT"));
                  //txttotal.setText(Double.toString(Math.round(sum)));
                  //txttotal.setText(Double.toString(sum));
                   DecimalFormat df = new DecimalFormat("##.##");
                   txtkgtotal.setText(df.format(kgsum));
                   txtinvtotal.setText(df.format(invsum));
         
         */
    }
    
    public static String getWeight(String pLength,String pWidth,String pGsq,String pProduct,String pPieceNo,String pPartyCode){
       double Length = Double.parseDouble(pLength);
       double Width = Double.parseDouble(pWidth);
       double Gsq = Double.parseDouble(pGsq);
       double Product = Double.parseDouble(pProduct);       
       double newWeight=0.00;
       Connection tmpConn;
       Statement stTmp;
       ResultSet rsTmp;       
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT RECD_KG FROM PRODUCTION.FELT_PIECE_REGISTER WHERE ORDER_CD="+pPieceNo+" AND PARTY_CODE='"+pPartyCode+"'");
            rsTmp.first();
            
            if(Product==7190110 || Product==7190210 || Product==7190310 || Product==7190410 || Product==7190510 || Product==7290000)
            {
                newWeight=((Length*Width));
            }
            else{
                  if(rsTmp.getRow()>0) {                
                      newWeight=rsTmp.getFloat("RECD_KG");
                    }
                    else
                     {
                        newWeight=0;    
                     }
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();           
        }
        catch(Exception e) {
            return "";
        }      
       
       newWeight=EITLERPGLOBAL.round(newWeight, 2);
       return Double.toString(newWeight);       
    }
    
    
    public static String getNewWeight(String pLength,String pWidth,String pGsq,String pProduct){
       double Length = Double.parseDouble(pLength);
       double Width = Double.parseDouble(pWidth);
       double Gsq = Double.parseDouble(pGsq);
       double Product = Double.parseDouble(pProduct);       
       double newWeight=0.00;
       
       if(Product==7190110 || Product==7190210 || Product==7190310 || Product==7190410 || Product==7190510 || Product==7290000)
       {
          newWeight=((Length*Width));       
       }else
       {
         newWeight=(Length*Width*(Gsq/1000));    
       }       
       
       newWeight=EITLERPGLOBAL.round(newWeight, 2);
       return Double.toString(newWeight);       
    }
    
    public static String getBasamt(String pSqmInd,String pLength,String pWidth,float pSQMrate,String pWeight,float pWTrate) {
        
       double Length = Double.parseDouble(pLength);
       double Width = Double.parseDouble(pWidth);
       double Weight = Double.parseDouble(pWeight);
       double SQMrate = pSQMrate;
       double WTrate = pWTrate;
        
        double newBasamt=0.00;
        if(pSqmInd.equals("1")){
           newBasamt=(Length)*(Width)*(SQMrate);
           newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }
        else{
           newBasamt=0.00;  
           newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }        
        //return Double.toString(Math.round(newBasamt));
        return Double.toString(newBasamt);
        
    }
    
    
    public static String getNewBasamt(String pSqmInd,String pLength,String pWidth,float pSQMrate,String pWeight,float pWTrate) {
        
       double Length = Double.parseDouble(pLength);
       double Width = Double.parseDouble(pWidth);
       double Weight = Double.parseDouble(pWeight);
       double SQMrate = pSQMrate;
       double WTrate = pWTrate;
        
        double newBasamt=0.00;
        if(pSqmInd.equals("1")){
           newBasamt=(Length)*(Width)*(SQMrate);
           newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }
        else{
           newBasamt=(Weight)*(WTrate);  
           newBasamt=EITLERPGLOBAL.round(newBasamt, 2);
        }        
        //return Double.toString(Math.round(newBasamt));
        return Double.toString(newBasamt);
        
    }
    
  /*  public static String getNewBasamtelse(String pWeight,float pWTrate) {
       
       double Weight = Double.parseDouble(pWeight);
       double WTrate = pWTrate;
        
        double newBasamt=0.00;
        newBasamt=(Weight)*(WTrate);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newBasamt);
        
    }
   */
    
    public static String getNewWPSC(String pChemTrtIn,String pPinInd,String pSprInd,String pWeight,float pCharges,String pWidth, String pChargesDisPer) {
       
       double Weight = Double.parseDouble(pWeight);
       double Width = Double.parseDouble(pWidth);       
       double ChargesDisPer= Double.parseDouble(pChargesDisPer);
       double Charges = pCharges;
       Charges=Charges*((100-ChargesDisPer)/100);
       
        
        double newWPSC1=0.00;
        double newWPSC2=0.00;
        double newWPSC3=0.00;
        double newWPSC=0.00;
        if(pChemTrtIn.equals("1")){
            newWPSC1=(Weight)*(Charges);            
        }
        else
        {
            newWPSC1=0;
        }
        if(pPinInd.equals("1")){
            newWPSC2=(Width)*(Charges);
        }
        else
        {
            newWPSC2=0;
        }
         if(pSprInd.equals("1")){
            newWPSC3=(Width)*(Charges);
        }
        else
        {
            newWPSC3=0;
        }
        
        newWPSC=newWPSC1+newWPSC2+newWPSC3;
        newWPSC=EITLERPGLOBAL.round(newWPSC, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newWPSC);
        
    }
    
    public static String getNewDisBasamt(String pDiscper,String pBasamt) {
        double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);
        
        double newDisBasamt=0.00;
        newDisBasamt=Basamt-((Basamt)*(Discper/100));
        newDisBasamt=EITLERPGLOBAL.round(newDisBasamt, 2);
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newDisBasamt);
        
    }
    
    public static String getNewExcise(String pDisbasamt,String pWpsc) {
        double Disbasamt = Double.parseDouble(pDisbasamt);
        double Wpsc = Double.parseDouble(pWpsc);
        
        double newExcise=0.00;
        //newExcise=((Disbasamt+Wpsc)*.12)+((Disbasamt+Wpsc)*.12)*.01+((Disbasamt+Wpsc)*.12)*.02;
        //New Changes in Budget 2015-16
        newExcise=((Disbasamt+Wpsc)*.125);
        //return Double.toString(Math.round(newDisamt));
        //return Double.toString(newExcise);
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(newExcise);
        
    }
    
    public static String getNewInsaccamt(String pInsInd,String pNewDisbasamt,String pNewExcise,String pWPSC) {
        double InsInd = Double.parseDouble(pInsInd);
        double WPSC = Double.parseDouble(pWPSC);
        double NewDisbasamt=Double.parseDouble(pNewDisbasamt);
        double NewExcise=Double.parseDouble(pNewExcise);
        
        double newInsaccamt=0.00;
        if(InsInd==1) {
            //newInsaccamt=Math.round((Math.round(NewDisbasamt+NewExcise+WPSC)+(Math.round(NewDisbasamt+NewExcise+WPSC)*.10))*.0039);            
            newInsaccamt=(ConvertToNextThousand((Math.round(NewDisbasamt+NewExcise+WPSC)+(Math.round(NewDisbasamt+NewExcise+WPSC)*.10)))*0.0039)+0.05;
            return Double.toString(newInsaccamt);
        }
        else{
            return Double.toString(0);
        }
    }
    
    public static String getNewInvamt(String pInsInd,String pNewDisbasamt,String pNewExcise,String pWPSC,String pNewInsaccamt) {
        double InsInd = Double.parseDouble(pInsInd);
        double WPSC = Double.parseDouble(pWPSC);
        double NewDisbasamt=Double.parseDouble(pNewDisbasamt);
        double NewExcise=Double.parseDouble(pNewExcise);
        double NewInsaccamt=Double.parseDouble(pNewInsaccamt);
        
        double newInvamt=0.00;
        if(InsInd==1) {
            newInvamt=NewDisbasamt+NewExcise+WPSC+NewInsaccamt;
            DecimalFormat df1 = new DecimalFormat("##");
            return df1.format(newInvamt);
        }
        else{
            newInvamt=NewDisbasamt+NewExcise+WPSC;
            DecimalFormat df2 = new DecimalFormat("##");
            return df2.format(newInvamt);
        }
    }
    
    public static double ConvertToNextThousand(double pamount){
        double returnamt=0;
        if (pamount<=1000){
            return pamount+(1000-pamount);
        }else{
            if(pamount%1000==0){
                return pamount;
            }else{
                return pamount+(1000-(pamount%1000));
            }                
        }          
        
    }
    
    
    public static int getItemGsq(String pPartyCode,String pPieceNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        int Gsq=0;
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Gsq=rsTmp.getInt("GSQ");
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Gsq;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public static String[] getPiecedetail(String pPartyCode,String pPieceNo){
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String []Piecedetail = new String[40];
        String []error = {"error"};        
        
        try {
            
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsTmp=stTmp.executeQuery("SELECT GSQ FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"'");
            String strSQL="SELECT *,false as Calculate_Weight,0 as SEAM_CHG_PER FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(PARTY_NAME)),'PARTY DELETED IN COBOL') AS PARTY_NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE ";
            //String strSQL="SELECT * FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(NAME)),'PARTY DELETED IN COBOL') AS NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE ";
            //strSQL+="B.PIECE_NO="+pPieceNo+" AND ";
            //strSQL+="A.PIECE_NO='"+pPieceNo+"' AND ";
            if(!pPartyCode.equals("")){
                strSQL+="B.PARTY_CD='"+pPartyCode+"' AND ";
            }
            strSQL+="A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 )) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010') AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ";
            strSQL+="UNION ALL ";
            strSQL+="SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN  (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="PIECE_NO='"+pPieceNo+"' ";
            if(!pPartyCode.equals("")){
                strSQL+="PARTY_CD='"+pPartyCode+"' ";
            }
            strSQL+="AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5,6,7,8) AND MAIN_ACCOUNT_CODE='210010') AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ) AS P WHERE PIECE_NO='"+pPieceNo+"' ";
            //rsTmp=stTmp.executeQuery("SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(NAME)),'PARTY DELETED IN COBOL') AS NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1 FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1 FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE B.PIECE_NO="+pPieceNo+" AND B.PARTY_CD='"+pPartyCode+"' AND A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 AND ST_FLAG_1 !='P')) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER) AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD UNION ALL SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"' AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5) ) AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ");
            System.out.println("Piece Detail Query :");
            System.out.println(strSQL);
            rsTmp=stTmp.executeQuery(strSQL);            
            rsTmp.first();
            
            if(rsTmp.getRow() > 0) {
                Piecedetail[0]=rsTmp.getString("PARTY_CD");
                Piecedetail[1]=rsTmp.getString("PARTY_NAME");
                Piecedetail[2]=rsTmp.getString("PRIORITY_DESC");
                Piecedetail[3]=rsTmp.getString("PIECE_NO");
                Piecedetail[4]=rsTmp.getString("ORDER_DATE");
                Piecedetail[5]=rsTmp.getString("DELIV_DATE");
                Piecedetail[6]=rsTmp.getString("COMM_DATE");
                Piecedetail[7]=(rsTmp.getString("PRODUCT_CD")+'0').substring(0,7);
                Piecedetail[8]=rsTmp.getString("ITEM");                
                Piecedetail[9]=rsTmp.getString("STYLE");
                Piecedetail[10]=rsTmp.getString("LNGTH");
                Piecedetail[11]=rsTmp.getString("WIDTH");
                Piecedetail[12]=rsTmp.getString("GSQ");
                //Piecedetail[13]=rsTmp.getString("WEIGHT");        
                
                
                String Productcode=(Piecedetail[7]+'0').substring(0,7);                
                String PinInd=clsProforma.getPinInd(Productcode);
                String SprInd=clsProforma.getSPRInd(Productcode);
                String ChemTrtIn=clsProforma.getChemTrtIn(Productcode);
                float Charges=clsProforma.getCharges(Productcode);
                String SqmInd=clsProforma.getSqmind(Productcode);
                float SQMrate=clsProforma.getSQMRate(Productcode);
                float WTrate=clsProforma.getWTRate(Productcode);
                                
                Piecedetail[13]=getWeight(Piecedetail[10], Piecedetail[11], Piecedetail[12], Piecedetail[7], Piecedetail[3], Piecedetail[0]);
               
                
                Piecedetail[14]=rsTmp.getString("RATE");                
                //Piecedetail[15]=rsTmp.getString("BAS_AMT");
                
                                                                                                            
                //Piecedetail[15]=getBasamt(SprInd, Piecedetail[10], Piecedetail[11], SQMrate, Piecedetail[13], WTrate);
                
                Piecedetail[16]=rsTmp.getString("DISC_PER");
                
                
                if (Piecedetail[13].equals("0.0")){
                    Piecedetail[15]="0.00";
                    Piecedetail[17]="0.00";
                    Piecedetail[18]="0.00";
                    Piecedetail[19]="0.00";
                    Piecedetail[20]="0.00";
                    Piecedetail[21]="0.00";
                    Piecedetail[22]="0.00";
                }else{
                    Piecedetail[15]=rsTmp.getString("BAS_AMT");
                    Piecedetail[17]=rsTmp.getString("DISAMT");
                    Piecedetail[18]=rsTmp.getString("DISBASAMT");                
                    //Piecedetail[18]=getNewDisBasamt(Piecedetail[16],Piecedetail[15]);                   
                
                    Piecedetail[19]=rsTmp.getString("EXCISE");                    
                
                    //Piecedetail[19]=getNewExcise(Piecedetail[16], getNewWPSC(ChemTrtIn, PinInd, SprInd, Piecedetail[13], Charges, Piecedetail[11],Piecedetail[34]));
                
                    Piecedetail[20]=rsTmp.getString("SEAM_CHG");
                    Piecedetail[21]=rsTmp.getString("INSACC_AMT");
                    Piecedetail[22]=rsTmp.getString("INV_AMT");
                }
                //Piecedetail[17]=getNewDisamt(Piecedetail[16], Piecedetail[15]);                
                
                Piecedetail[23]=rsTmp.getString("INSURANCE_CODE");
                Piecedetail[24]=rsTmp.getString("REF_NO");
                Piecedetail[25]=rsTmp.getString("CONF_NO");
                Piecedetail[26]=rsTmp.getString("MACHINE_NO");
                Piecedetail[27]=rsTmp.getString("POSITION");
                Piecedetail[28]=rsTmp.getString("ZONE");
                Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("PRIORITY_DATE"));
                Piecedetail[30]=rsTmp.getString("INCHARGE_NAME");
                
                Piecedetail[31]=rsTmp.getString("PO_NO");
                Piecedetail[32]=EITLERPGLOBAL.formatDate(rsTmp.getString("PO_DATE"));
                Piecedetail[33]=String.valueOf(rsTmp.getBoolean("Calculate_Weight"));
                Piecedetail[34]=rsTmp.getString("SEAM_CHG_PER");
                
                
                
                
                /*Piecedetail[31]=rsTmp.getString("CHEM_TRT_IN");
                Piecedetail[32]=rsTmp.getString("PIN_IND");
                Piecedetail[33]=rsTmp.getString("CHARGES");
                Piecedetail[34]=rsTmp.getString("SPR_IND");
                Piecedetail[35]=rsTmp.getString("SQM_IND");                
                */
            }
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            return Piecedetail;
            
        }
        catch(Exception e) {
            e.printStackTrace();
            return error;
        }
    }
    
    
    public static String[] getOtherPiecedetail(String pPartyCode,String pPieceNo){
        Connection tmpConnOther;
        Statement stTmpOther;
        ResultSet rsTmpOther;
        String []OtherPiecedetail = new String[40];
        String []error = {"error"};
        
        try {
            tmpConnOther=data.getConn();
            
            stTmpOther=tmpConnOther.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String strSQL="SELECT *,false as Calculate_Weight,0 as SEAM_CHG_PER FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INSURANCE_CODE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(PARTY_NAME)),'PARTY DELETED IN COBOL') AS PARTY_NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INSURANCE_CODE,0)AS INSURANCE_CODE,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1,PO_NO,PO_DATE FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE ";
            if(!pPartyCode.equals("")){
                strSQL+="B.PARTY_CD='"+pPartyCode+"' AND ";
            }
            strSQL+="A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 )) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='210010') AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ";
            strSQL+="UNION ALL ";
            strSQL+="SELECT PARTY_CD,PARTY_NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,ROUND(INV_AMT,0) AS INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,PO_NO,PO_DATE FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN CASE WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)<=1000 THEN ((1000*0.0039)+0.05) WHEN (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)=0 AND ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 THEN ((((DISBASAMT+EXCISE+WPSC)*1.1)*0.0039)+0.05) WHEN ((DISBASAMT+EXCISE+WPSC)*1.1)>1000 AND (((DISBASAMT+EXCISE+WPSC)*1.1)%1000)!=0 THEN  (((((DISBASAMT+EXCISE+WPSC)*1.1)+(1000-(((DISBASAMT+EXCISE+WPSC)*1.1)%1000)))*0.0039)+0.05) END WHEN INSURANCE_CODE !=1 THEN 0  END) ,0) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.125) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,PO_NO,PO_DATE FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,PARTY_NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,PO_NO,PO_DATE FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INSURANCE_CODE,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INSURANCE_CODE,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INSURANCE_CODE =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0039,0) WHEN INSURANCE_CODE !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND,SQM_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INSURANCE_CODE,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,INCHARGE_CD,CHEM_TRT_IN,PIN_IND,CHARGES,SPR_IND FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,DINESHMILLS.D_SAL_PARTY_MASTER WHERE ";
            //strSQL+="PIECE_NO='"+pPieceNo+"' ";
            if(!pPartyCode.equals("")){
                strSQL+="PARTY_CD='"+pPartyCode+"' ";
            }
            strSQL+="AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5,6,7,8) AND MAIN_ACCOUNT_CODE='210010') AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.INCHARGE_CD ) AS P WHERE PIECE_NO='"+pPieceNo+"' ";
            System.out.println("Other Piece Detail Query:");
            System.out.print(strSQL);
            
            rsTmpOther=stTmpOther.executeQuery(strSQL);            
            rsTmpOther.first();
            
            if(rsTmpOther.getRow() > 0) {
                OtherPiecedetail[0]=rsTmpOther.getString("PARTY_CD");
                OtherPiecedetail[1]=rsTmpOther.getString("PARTY_NAME");
                OtherPiecedetail[2]=rsTmpOther.getString("PRIORITY_DESC");
                OtherPiecedetail[3]=rsTmpOther.getString("PIECE_NO");
                OtherPiecedetail[4]=rsTmpOther.getString("ORDER_DATE");
                OtherPiecedetail[5]=rsTmpOther.getString("DELIV_DATE");
                OtherPiecedetail[6]=rsTmpOther.getString("COMM_DATE");
                OtherPiecedetail[7]=rsTmpOther.getString("PRODUCT_CD");
                OtherPiecedetail[8]=rsTmpOther.getString("ITEM");
                
                OtherPiecedetail[9]=rsTmpOther.getString("STYLE");
                OtherPiecedetail[10]=rsTmpOther.getString("LNGTH");
                OtherPiecedetail[11]=rsTmpOther.getString("WIDTH");
                OtherPiecedetail[12]=rsTmpOther.getString("GSQ");
                OtherPiecedetail[13]=rsTmpOther.getString("WEIGHT");
                OtherPiecedetail[14]=rsTmpOther.getString("RATE");
                OtherPiecedetail[15]=rsTmpOther.getString("BAS_AMT");
                OtherPiecedetail[16]=rsTmpOther.getString("DISC_PER");
                OtherPiecedetail[17]=rsTmpOther.getString("DISAMT");
                
                OtherPiecedetail[18]=rsTmpOther.getString("DISBASAMT");
                OtherPiecedetail[19]=rsTmpOther.getString("EXCISE");
                OtherPiecedetail[20]=rsTmpOther.getString("SEAM_CHG");
                OtherPiecedetail[21]=rsTmpOther.getString("INSACC_AMT");
                OtherPiecedetail[22]=rsTmpOther.getString("INV_AMT");
                
                OtherPiecedetail[23]=rsTmpOther.getString("INSURANCE_CODE");
                OtherPiecedetail[24]=rsTmpOther.getString("REF_NO");
                OtherPiecedetail[25]=rsTmpOther.getString("CONF_NO");
                OtherPiecedetail[26]=rsTmpOther.getString("MACHINE_NO");
                OtherPiecedetail[27]=rsTmpOther.getString("POSITION");
                OtherPiecedetail[28]=rsTmpOther.getString("ZONE");
                OtherPiecedetail[29]=EITLERPGLOBAL.formatDate(rsTmpOther.getString("PRIORITY_DATE"));
                OtherPiecedetail[30]=rsTmpOther.getString("INCHARGE_NAME");
                
                OtherPiecedetail[31]=rsTmpOther.getString("PO_NO");
                OtherPiecedetail[32]=EITLERPGLOBAL.formatDate(rsTmpOther.getString("PO_DATE"));
                OtherPiecedetail[33]=String.valueOf(rsTmpOther.getBoolean("Calculate_Weight"));
                OtherPiecedetail[34]=rsTmpOther.getString("SEAM_CHG_PER");
                
                
                /*
                OtherPiecedetail[31]=rsTmpOther.getString("CHEM_TRT_IN");
                OtherPiecedetail[32]=rsTmpOther.getString("PIN_IND");
                OtherPiecedetail[33]=rsTmpOther.getString("CHARGES");
                OtherPiecedetail[34]=rsTmpOther.getString("SPR_IND");
                OtherPiecedetail[35]=rsTmpOther.getString("SQM_IND");                 
                */
            }
            //tmpConn.close();
            stTmpOther.close();
            rsTmpOther.close();
            return OtherPiecedetail;
            
        }
        catch(Exception e) {
            e.printStackTrace();
            return error;
        }
    }
    
    
    
    public static String getPartyName(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String PartyName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARTY_NAME FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                PartyName=rsTmp.getString("PARTY_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return PartyName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public static HashMap getList(String pCondition) {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;
        
        HashMap List=new HashMap();
        long Counter=0;
        long Counter2=0;
        
        tmpConn=data.getConn();
        
        try {
            tmpStmt=tmpConn.createStatement();
            
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER "+pCondition);
            
            Counter=0;
            while(rsTmp.next()) {
                Counter=Counter+1;
                clsSales_Party ObjParty =new clsSales_Party();
                ObjParty.setAttribute("COMPANY_ID",rsTmp.getInt("COMPANY_ID"));
                ObjParty.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjParty.setAttribute("PARTY_TYPE",rsTmp.getInt("PARTY_TYPE"));
                ObjParty.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                
                ObjParty.setAttribute("SEASON_CODE",rsTmp.getString("SEASON_CODE"));
                ObjParty.setAttribute("REG_DATE",rsTmp.getString("REG_DATE"));
                
                ObjParty.setAttribute("AREA_ID",rsTmp.getString("AREA_ID"));
                ObjParty.setAttribute("ADDRESS1",rsTmp.getString("ADDRESS1"));
                ObjParty.setAttribute("ADDRESS2",rsTmp.getString("ADDRESS2"));
                ObjParty.setAttribute("CITY_ID",rsTmp.getString("CITY_ID"));
                ObjParty.setAttribute("CITY_NAME",rsTmp.getString("CITY_NAME"));
                ObjParty.setAttribute("DISPATCH_STATION",rsTmp.getString("DISPATCH_STATION"));
                ObjParty.setAttribute("DISTRICT",rsTmp.getString("DISTRICT"));
                ObjParty.setAttribute("PINCODE",rsTmp.getString("PINCODE"));
                ObjParty.setAttribute("PHONE_NO",rsTmp.getString("PHONE_NO"));
                
                ObjParty.setAttribute("REMARK1",rsTmp.getString("REMARK1"));
                ObjParty.setAttribute("REMARK2",rsTmp.getString("REMARK2"));
                ObjParty.setAttribute("REMARK3",rsTmp.getString("REMARK3"));
                ObjParty.setAttribute("REMARK4",rsTmp.getString("REMARK4"));
                ObjParty.setAttribute("REMARK5",rsTmp.getString("REMARK5"));
                
                ObjParty.setAttribute("MOBILE_NO",rsTmp.getString("MOBILE_NO"));
                ObjParty.setAttribute("EMAIL",rsTmp.getString("EMAIL"));
                ObjParty.setAttribute("URL",rsTmp.getString("URL"));
                ObjParty.setAttribute("CONTACT_PERSON",rsTmp.getString("CONTACT_PERSON"));
                ObjParty.setAttribute("BANK_ID",rsTmp.getLong("BANK_ID"));
                ObjParty.setAttribute("BANK_NAME",rsTmp.getString("BANK_NAME"));
                ObjParty.setAttribute("BANK_ADDRESS",rsTmp.getString("BANK_ADDRESS"));
                ObjParty.setAttribute("BANK_CITY",rsTmp.getString("BANK_CITY"));
                ObjParty.setAttribute("CST_NO",rsTmp.getString("CST_NO"));
                ObjParty.setAttribute("CST_DATE",rsTmp.getString("CST_DATE"));
                ObjParty.setAttribute("ECC_NO",rsTmp.getString("ECC_NO"));
                ObjParty.setAttribute("ECC_DATE",rsTmp.getString("ECC_DATE"));
                ObjParty.setAttribute("TIN_NO",rsTmp.getString("TIN_NO"));
                ObjParty.setAttribute("TIN_DATE",rsTmp.getString("TIN_DATE"));
                ObjParty.setAttribute("PAN_NO",rsTmp.getString("PAN_NO"));
                ObjParty.setAttribute("PAN_DATE",rsTmp.getString("PAN_DATE"));
                ObjParty.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                ObjParty.setAttribute("CHARGE_CODE",rsTmp.getString("CHARGE_CODE"));
                ObjParty.setAttribute("CHARGE_CODE_II",rsTmp.getString("CHARGE_CODE_II"));
                ObjParty.setAttribute("CREDIT_DAYS",rsTmp.getDouble("CREDIT_DAYS"));
                ObjParty.setAttribute("DOCUMENT_THROUGH",rsTmp.getString("DOCUMENT_THROUGH"));
                ObjParty.setAttribute("TRANSPORTER_ID",rsTmp.getLong("TRANSPORTER_ID"));
                ObjParty.setAttribute("TRANSPORTER_NAME",rsTmp.getString("TRANSPORTER_NAME"));
                ObjParty.setAttribute("AMOUNT_LIMIT",rsTmp.getDouble("AMOUNT_LIMIT"));
                ObjParty.setAttribute("OTHER_BANK_ID",rsTmp.getLong("OTHER_BANK_ID"));
                ObjParty.setAttribute("OTHER_BANK_NAME",rsTmp.getString("OTHER_BANK_NAME"));
                ObjParty.setAttribute("OTHER_BANK_ADDRESS",rsTmp.getString("OTHER_BANK_ADDRESS"));
                ObjParty.setAttribute("OTHER_BANK_CITY",rsTmp.getString("OTHER_BANK_CITY"));
                ObjParty.setAttribute("CATEGORY",rsTmp.getString("CATEGORY"));
                ObjParty.setAttribute("INSURANCE_CODE",rsTmp.getString("INSURANCE_CODE"));
                ObjParty.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                ObjParty.setAttribute("DELAY_INTEREST_PER",rsTmp.getDouble("DELAY_INTEREST_PER"));
                ObjParty.setAttribute("ACCOUNT_CODES",rsTmp.getString("ACCOUNT_CODES"));
                
                ObjParty.setAttribute("CREATED_BY",rsTmp.getInt("CREATED_BY"));
                ObjParty.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                ObjParty.setAttribute("MODIFIED_BY",rsTmp.getInt("MODIFIED_BY"));
                ObjParty.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                
                ObjParty.setAttribute("HIERARCHY_ID",rsTmp.getLong("HIERARCHY_ID"));
                
            }//Outer while
            
            rsTmp.close();
            //tmpConn.close();
            tmpStmt.close();
            
        }
        catch(Exception e) {
            //LastError = e.getMessage();
        }
        
        return List;
    }
    
    public static Object getObjectEx(int pCompanyID,String pPartyCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode+"' ";
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }
    
    public static Object getObjectExN(int pCompanyID,String pPartyCode, String MainCode) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND PARTY_CODE='" + pPartyCode+"' AND MAIN_ACCOUNT_CODE="+MainCode;
        clsSales_Party ObjParty = new clsSales_Party();
        ObjParty.LoadData(pCompanyID);
        ObjParty.Filter(strCondition);
        return ObjParty;
    }
    
    
    
    public static boolean deleteParty(String PartyCode) {
        try {
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE DOC_NO='"+PartyCode+"' AND MODULE_ID='"+clsOrderParty.ModuleID+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    
    public static String getSeasonCode() {
        String SeasonCode = "";
        try {
            SeasonCode = data.getStringValueFromDB("SELECT SEASON_ID FROM D_SAL_SEASON_MASTER WHERE CURDATE() BETWEEN DATE_FROM AND DATE_TO");
        } catch(Exception e) {
            return SeasonCode;
        }
        return SeasonCode;
    }
    
   /* 
    public static boolean getBackup(String PartyCode) {
        Connection tmpConn = null;
        Statement tmpStmt = null;
        ResultSet rsTmp = null;
        int SrNo = 0;
        try {
            tmpConn=data.getConn();
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_BKUP ORDER BY PARTY_CODE");
            clsSales_Party tmpParty = (clsSales_Party)clsSales_Party.getObjectExN(EITLERPGLOBAL.gCompanyID, PartyCode,"210027");
            SrNo = data.getIntValueFromDB("SELECT MAX(SR_NO) FROM D_SAL_PARTY_MASTER_BKUP")+1;
            rsTmp.first();
            rsTmp.moveToInsertRow();
            rsTmp.updateInt("SR_NO", SrNo);
            rsTmp.updateInt("COMPANY_ID", tmpParty.getAttribute("COMAPNY_ID").getInt());
            rsTmp.updateString("PARTY_CODE", tmpParty.getAttribute("PARTY_CODE").getString());
            rsTmp.updateInt("PARTY_TYPE", tmpParty.getAttribute("PARTY_TYPE").getInt());
            rsTmp.updateString("PARENT_PARTY_CODE", " ");
            rsTmp.updateString("PARTY_NAME",tmpParty.getAttribute("PARTY_NAME").getString());
            
            rsTmp.updateString("SEASON_CODE",tmpParty.getAttribute("SEASON_CODE").getString());
            rsTmp.updateString("REG_DATE",tmpParty.getAttribute("REG_DATE").getString());
            
            rsTmp.updateString("AREA_ID",tmpParty.getAttribute("AREA_ID").getString());
            rsTmp.updateString("ADDRESS1",tmpParty.getAttribute("ADDRESS1").getString());
            rsTmp.updateString("ADDRESS2",tmpParty.getAttribute("ADDRESS2").getString());
            rsTmp.updateString("CITY_ID",tmpParty.getAttribute("CITY_ID").getString());
            rsTmp.updateString("CITY_NAME",tmpParty.getAttribute("CITY_NAME").getString());
            rsTmp.updateString("DISPATCH_STATION",tmpParty.getAttribute("DISPATCH_STATION").getString());
            rsTmp.updateString("DISTRICT",tmpParty.getAttribute("DISTRICT").getString());
            rsTmp.updateString("PINCODE",tmpParty.getAttribute("PINCODE").getString());
            rsTmp.updateString("PHONE_NO",tmpParty.getAttribute("PHONE_NO").getString());
            rsTmp.updateString("MOBILE_NO",tmpParty.getAttribute("MOBILE_NO").getString());
            rsTmp.updateString("EMAIL",tmpParty.getAttribute("EMAIL").getString());
            rsTmp.updateString("URL",tmpParty.getAttribute("URL").getString());
            rsTmp.updateString("CONTACT_PERSON",tmpParty.getAttribute("CONTACT_PERSON").getString());
            rsTmp.updateInt("BANK_ID",tmpParty.getAttribute("BANK_ID").getInt());
            rsTmp.updateString("BANK_NAME",tmpParty.getAttribute("BANK_NAME").getString());
            rsTmp.updateString("BANK_ADDRESS",tmpParty.getAttribute("BANK_ADDRESS").getString());
            rsTmp.updateString("BANK_CITY",tmpParty.getAttribute("BANK_CITY").getString());
            rsTmp.updateString("CHARGE_CODE",tmpParty.getAttribute("CHARGE_CODE").getString());
            rsTmp.updateString("CHARGE_CODE_II",tmpParty.getAttribute("CHARGE_CODE_II").getString());
            rsTmp.updateDouble("CREDIT_DAYS",tmpParty.getAttribute("CREDIT_DAYS").getDouble());
            rsTmp.updateString("DOCUMENT_THROUGH",tmpParty.getAttribute("DOCUMENT_THROUGH").getString());
            rsTmp.updateInt("TRANSPORTER_ID",tmpParty.getAttribute("TRANSPORTER_ID").getInt());
            rsTmp.updateString("TRANSPORTER_NAME",tmpParty.getAttribute("TRANSPORTER_NAME").getString());
            rsTmp.updateDouble("AMOUNT_LIMIT",tmpParty.getAttribute("AMOUNT_LIMIT").getDouble());
            rsTmp.updateString("CST_NO",tmpParty.getAttribute("CST_NO").getString());
            rsTmp.updateString("CST_DATE",tmpParty.getAttribute("CST_DATE").getString());
            rsTmp.updateString("ECC_NO",tmpParty.getAttribute("ECC_NO").getString());
            rsTmp.updateString("ECC_DATE",tmpParty.getAttribute("ECC_DATE").getString());
            rsTmp.updateString("TIN_NO",tmpParty.getAttribute("TIN_NO").getString());
            rsTmp.updateString("TIN_DATE",tmpParty.getAttribute("TIN_DATE").getString());
            rsTmp.updateString("PAN_NO",tmpParty.getAttribute("PAN_NO").getString());
            rsTmp.updateString("PAN_DATE",tmpParty.getAttribute("PAN_DATE").getString());
            rsTmp.updateString("MAIN_ACCOUNT_CODE",tmpParty.getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsTmp.updateString("CATEGORY",tmpParty.getAttribute("CATEGORY").getString());
            rsTmp.updateInt("OTHER_BANK_ID",tmpParty.getAttribute("OTHER_BANK_ID").getInt());
            rsTmp.updateString("OTHER_BANK_NAME",tmpParty.getAttribute("OTHER_BANK_NAME").getString());
            rsTmp.updateString("OTHER_BANK_ADDRESS",tmpParty.getAttribute("OTHER_BANK_ADDRESS").getString());
            rsTmp.updateString("OTHER_BANK_CITY",tmpParty.getAttribute("OTHER_BANK_CITY").getString());
            rsTmp.updateString("INSURANCE_CODE",tmpParty.getAttribute("INSURANCE_CODE").getString());
            rsTmp.updateString("REMARKS",tmpParty.getAttribute("REMARKS").getString());
            rsTmp.updateDouble("DELAY_INTEREST_PER",tmpParty.getAttribute("DELAY_INTEREST_PER").getDouble());
            rsTmp.updateString("ACCOUNT_CODES",tmpParty.getAttribute("ACCOUNT_CODES").getString());
            
            rsTmp.updateString("CREATED_BY",tmpParty.getAttribute("CREATED_BY").getString());
            rsTmp.updateString("CREATED_DATE",tmpParty.getAttribute("CREATED_DATE").getString());
            rsTmp.updateString("MODIFIED_BY",tmpParty.getAttribute("MODIFIED_DATE").getString());
            rsTmp.updateString("MODIFIED_DATE",tmpParty.getAttribute("MODIFIED_DATE").getString());
            rsTmp.updateInt("HIERARCHY_ID",tmpParty.getAttribute("HIERARCHY_ID").getInt());
            rsTmp.updateBoolean("APPROVED",tmpParty.getAttribute("APPROVED").getBool());
            rsTmp.updateString("APPROVED_DATE",tmpParty.getAttribute("APPROVED_DATE").getString());
            rsTmp.updateBoolean("CANCELLED",tmpParty.getAttribute("CANCELLED").getBool());
            rsTmp.updateBoolean("CHANGED",true);
            rsTmp.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsTmp.updateBoolean("REJECTED",false);
            rsTmp.updateString("REJECTED_DATE","0000-00-00");
            rsTmp.updateString("REJECTED_REMARKS",tmpParty.getAttribute("REJECTED_REMARKS").getString());
            rsTmp.insertRow();
        } catch(Exception e) {
            return false;
        }
        return true;
    }
    */
    
   
}
