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
 * @author  root
 */
public class clsOrder_1 {
      
    public String LastError="";
    private ResultSet rsResultSet,rsResultSet1;
    private Connection Conn;
    private Statement Stmt;
    DecimalFormat df = new DecimalFormat("##.##");
 
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    public static int ModuleID=703; //72
   
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
    public clsOrder_1() {
        LastError = "";
        props=new HashMap();
        //props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        //props.put("ORDER_NO",new Variant(""));
        props.put("PIECE_NO",new Variant(""));
        props.put("ORDER_DATE",new Variant(""));
        props.put("PRODUCT_CODE",new Variant(""));                
        props.put("PARTY_CD",new Variant(""));
        props.put("LNGTH",new Variant(0.00));
        props.put("WIDTH",new Variant(0.00));        
        props.put("GSQ",new Variant(0));
        props.put("DELIV_DATE",new Variant(""));
        props.put("COMM_DATE",new Variant(""));
        props.put("ORDER_CODE",new Variant(""));
        props.put("GRUP",new Variant("")); //STATION_NAME
        props.put("INV_NO",new Variant(""));
        props.put("INV_DATE",new Variant(""));
        props.put("PROD_IND",new Variant(""));
        props.put("PROD_IND_A",new Variant(""));
        props.put("ORD_DDMM_A",new Variant(""));
        props.put("PROD_IND_B",new Variant(""));
        props.put("ORD_DDMM_B",new Variant(""));
        props.put("PROD_IND_C",new Variant(""));
        props.put("ORD_DDMM_C",new Variant(""));        
        props.put("PROD_CODE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("BALNK",new Variant(""));        
        props.put("WEIGHT",new Variant(0.00));
        props.put("CREATED_BY",new Variant(0));
        props.put("CREATED_DATE",new Variant(""));
        props.put("PRIORITY",new Variant(0));     
        props.put("INV_AMOUNT",new Variant(0.0));
        props.put("BAS_AMT",new Variant(0.0));
        props.put("WPSC",new Variant(0.0));
        props.put("OTHER",new Variant(0.0));
        props.put("DISCOUNT",new Variant(0.0));
        props.put("SEAM_CHG",new Variant(0.0));
        props.put("EXCISE",new Variant(0.0));
        props.put("APPROX_AMOUNT",new Variant(""));
        props.put("MACHINE_NO",new Variant(""));
        props.put("POSITION",new Variant(""));
        props.put("REF_NO",new Variant(""));           
        props.put("CONF_NO",new Variant(""));
        props.put("PRIORITY_DATE",new Variant(""));
        props.put("LAST_PRIO_USR",new Variant(""));
        props.put("WVG_DATE",new Variant(""));
        props.put("MND_DATE",new Variant(""));
        props.put("NDL_DATE",new Variant(""));
        props.put("COL006",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("MODIFIED_BY",new Variant(0));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("CHANGED",new Variant(true));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("REJECTED_REMARKS",new Variant(""));
        props.put("UPDATED_BY",new Variant(0));
        props.put("PREFIX",new Variant(""));
        /*props.put("TIN_DATE",new Variant(""));
        props.put("CST_NO",new Variant(""));
        props.put("CST_DATE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("CATEGORY",new Variant(""));
        */        
        // -- Approval Specific Fields --
        props.put("FROM",new Variant(0));
        props.put("TO",new Variant(0));
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY PIECE_NO ");
            //rsResultSet1=Stmt.executeQuery("SELECT A.*,PRIORITY_DESC FROM (SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY ORDER_DATE DESC) AS A LEFT JOIN (SELECT PRIORITY_DESC,PRIORITY_ID FROM PRODUCTION.FELT_PRIORITY_MASTER ) AS B ON A.PRIORITY=B.PRIORITY_ID");
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
    
    public boolean setData() {
        ResultSet rsTmp;
        Connection tmpConn;
        Statement tmpStmt;        
        tmpConn=data.getConn();        
        long Counter=0;
        int RevNo=0;        
        try {
            if(HistoryView) {
                //RevNo=UtilFunctions.getInt(rsResultSet,"REVISION_NO",0);
                //setAttribute("REVISION_NO",RevNo);
                RevNo=rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO",rsResultSet.getInt("REVISION_NO"));
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            //setAttribute("ORDER_NO",rsResultSet.getString("ORDER_NO"));
            setAttribute("PIECE_NO",rsResultSet.getString("PIECE_NO"));
            setAttribute("ORDER_DATE",rsResultSet.getString("ORDER_DATE"));            
            setAttribute("PRODUCT_CODE",rsResultSet.getString("PRODUCT_CODE"));
            setAttribute("PARTY_CD",rsResultSet.getString("PARTY_CD"));            
            setAttribute("LNGTH",EITLERPGLOBAL.round(rsResultSet.getDouble("LNGTH"),2));
            setAttribute("WIDTH",EITLERPGLOBAL.round(rsResultSet.getDouble("WIDTH"),2));
            //ObjMRItems.setAttribute("LNGTH",EITLERPGLOBAL.round(rsTmp.getDouble("LNGTH"),2));
            //ObjMRItems.setAttribute("RCVD_MTR",EITLERPGLOBAL.round(rsTmp.getDouble("RCVD_MTR"),2));
            
            setAttribute("GSQ",rsResultSet.getInt("GSQ"));
            setAttribute("DELIV_DATE",rsResultSet.getString("DELIV_DATE"));
            setAttribute("COMM_DATE",rsResultSet.getString("COMM_DATE"));
            setAttribute("ORDER_CODE",rsResultSet.getString("ORDER_CODE"));
            setAttribute("GRUP",rsResultSet.getString("GRUP"));
            setAttribute("INV_NO",rsResultSet.getString("INV_NO"));                
            setAttribute("INV_DATE",rsResultSet.getString("INV_DATE"));
            setAttribute("PROD_IND",rsResultSet.getString("PROD_IND"));
            setAttribute("PROD_IND_A",rsResultSet.getString("PROD_IND_A"));
            setAttribute("ORD_DDMM_A",rsResultSet.getString("ORD_DDMM_A"));
            setAttribute("PROD_IND_B",rsResultSet.getString("PROD_IND_B"));
            setAttribute("ORD_DDMM_B",rsResultSet.getString("ORD_DDMM_B"));
            setAttribute("PROD_IND_C",rsResultSet.getString("PROD_IND_C"));
            setAttribute("ORD_DDMM_C",rsResultSet.getString("ORD_DDMM_C"));
            setAttribute("PROD_CODE",rsResultSet.getString("PROD_CODE"));
            setAttribute("REMARKS",rsResultSet.getString("REMARKS"));
            setAttribute("BALNK",rsResultSet.getString("BALNK"));
            setAttribute("WEIGHT",rsResultSet.getDouble("WEIGHT"));            
            setAttribute("CREATED_BY",rsResultSet.getString("CREATED_BY"));
            setAttribute("CREATED_DATE",rsResultSet.getString("CREATED_DATE"));
            setAttribute("PRIORITY",rsResultSet.getInt("PRIORITY"));
            setAttribute("INV_AMOUNT",EITLERPGLOBAL.round(rsResultSet.getFloat("INV_AMOUNT"),2));
            setAttribute("BAS_AMT",EITLERPGLOBAL.round(rsResultSet.getFloat("BAS_AMT"),2));
            setAttribute("WPSC",EITLERPGLOBAL.round(rsResultSet.getFloat("WPSC"),2));
            setAttribute("OTHER",EITLERPGLOBAL.round(rsResultSet.getFloat("OTHER"),2));
            setAttribute("DISCOUNT",EITLERPGLOBAL.round(rsResultSet.getFloat("DISCOUNT"),2));
            setAttribute("SEAM_CHG",EITLERPGLOBAL.round(rsResultSet.getFloat("SEAM_CHG"),2));
            setAttribute("EXCISE",EITLERPGLOBAL.round(rsResultSet.getFloat("EXCISE"),2));
            setAttribute("APPROX_AMOUNT",rsResultSet.getString("APPROX_AMOUNT"));
            setAttribute("MACHINE_NO",rsResultSet.getString("MACHINE_NO"));
            setAttribute("POSITION",rsResultSet.getString("POSITION"));
            setAttribute("REF_NO",rsResultSet.getString("REF_NO"));            
            setAttribute("CONF_NO",rsResultSet.getString("CONF_NO"));
            setAttribute("PRIORITY_DATE",rsResultSet.getString("PRIORITY_DATE"));
            setAttribute("LAST_PRIO_USR",rsResultSet.getString("LAST_PRIO_USR"));            
            setAttribute("WVG_DATE",rsResultSet.getString("WVG_DATE"));          
            setAttribute("MND_DATE",rsResultSet.getString("MND_DATE"));
            setAttribute("NDL_DATE",rsResultSet.getString("NDL_DATE"));            
            setAttribute("COL006",rsResultSet.getString("COL006"));
            setAttribute("HIERARCHY_ID",rsResultSet.getInt("HIERARCHY_ID"));            
            setAttribute("MODIFIED_BY",rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE",rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("APPROVED",rsResultSet.getInt("APPROVED"));
            setAttribute("APPROVED_DATE",rsResultSet.getString("APPROVED_DATE"));
            setAttribute("REJECTED",rsResultSet.getBoolean("REJECTED"));
            setAttribute("REJECTED_DATE",rsResultSet.getString("REJECTED_DATE"));
            setAttribute("CANCELLED",rsResultSet.getInt("CANCELLED"));
            setAttribute("CHANGED",rsResultSet.getInt("CHANGED"));
            setAttribute("CHANGED_DATE",rsResultSet.getString("CHANGED_DATE"));
            setAttribute("REJECTED_REMARKS",rsResultSet.getString("REJECTED_REMARKS"));           
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
    public boolean IsEditable(int CompanyID,String PartyCode,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT PARTY_CODE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CODE='"+PartyCode+"'";
            
            if(data.IsRecordExist(strSQL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+PartyCode+"' AND USER_ID="+UserID+" AND STATUS='W'";
                if(data.IsRecordExist(strSQL)) {
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
    
    public static HashMap getHistoryList(String orderNo) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM PRODUCTION.FELT_ORDER_MASTER_H WHERE PIECE_NO='"+orderNo+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsOrder objParty=new clsOrder();
                    
                    objParty.setAttribute("PIECE_NO",UtilFunctions.getString(rsTmp,"PIECE_NO", ""));
                    objParty.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objParty.setAttribute("UPDATED_BY",UtilFunctions.getInt(rsTmp,"UPDATED_BY", 0));
                    objParty.setAttribute("APPROVAL_STATUS",UtilFunctions.getString(rsTmp,"APPROVAL_STATUS", ""));
                    objParty.setAttribute("ENTRY_DATE",EITLERPGLOBAL.formatDate(UtilFunctions.getString(rsTmp,"ENTRY_DATE","0000-00-00")));
                    objParty.setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    objParty.setAttribute("APPROVER_REMARKS",UtilFunctions.getString(rsTmp,"APPROVER_REMARKS", ""));
                    
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
    
    public boolean Insert() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try {            
            // ---- History Related Changes ------ //
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER_H WHERE PIECE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//    
            
            
            //--------- Generate Order no.  ------------
            //setAttribute("ORDER_NO",clsFirstFree.getNextFreeNo((int)EITLERPGLOBAL.gCompanyID,ModuleID, (int)getAttribute("FFNO").getVal(),true));
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            //rsResultSet.updateString("ORDER_NO", getAttribute("ORDER_NO").getString());
            rsResultSet.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsResultSet.updateString("ORDER_DATE", getAttribute("ORDER_DATE").getString());
            rsResultSet.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsResultSet.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            rsResultSet.updateDouble("LNGTH",getAttribute("LNGTH").getDouble());
            rsResultSet.updateDouble("WIDTH",getAttribute("WIDTH").getDouble());
            rsResultSet.updateInt("GSQ",getAttribute("GSQ").getInt());
            rsResultSet.updateString("DELIV_DATE",getAttribute("DELIV_DATE").getString());
            rsResultSet.updateString("COMM_DATE",getAttribute("COMM_DATE").getString());
            rsResultSet.updateString("ORDER_CODE",getAttribute("ORDER_CODE").getString());
            rsResultSet.updateString("GRUP",getAttribute("GRUP").getString());
            rsResultSet.updateString("INV_NO",getAttribute("INV_NO").getString());
            rsResultSet.updateString("INV_DATE",getAttribute("INV_DATE").getString());
            rsResultSet.updateString("PROD_IND",getAttribute("PROD_IND").getString());
            rsResultSet.updateString("PROD_IND_A",getAttribute("PROD_IND_A").getString());
            rsResultSet.updateString("ORD_DDMM_A",getAttribute("ORD_DDMM_A").getString());
            rsResultSet.updateString("PROD_IND_B",getAttribute("PROD_IND_B").getString());
            rsResultSet.updateString("ORD_DDMM_B",getAttribute("ORD_DDMM_B").getString());
            rsResultSet.updateString("PROD_IND_C",getAttribute("PROD_IND_C").getString());
            rsResultSet.updateString("ORD_DDMM_C",getAttribute("ORD_DDMM_C").getString());
            rsResultSet.updateString("PROD_CODE",getAttribute("PROD_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());            
            rsResultSet.updateString("BALNK",getAttribute("BALNK").getString());            
            rsResultSet.updateString("WEIGHT",df.format(getAttribute("WEIGHT").getVal()).toString());           
            rsResultSet.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
            rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            //rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsResultSet.updateString("PRIORITY",(String)getAttribute("PRIORITY").getObj());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateFloat("INV_AMOUNT",(float)getAttribute("INV_AMOUNT").getVal());
            rsResultSet.updateFloat("BAS_AMT",(float)getAttribute("BAS_AMT").getVal());
            rsResultSet.updateFloat("WPSC",(float)getAttribute("WPSC").getVal());
            rsResultSet.updateFloat("OTHER",(float)getAttribute("OTHER").getVal());
            rsResultSet.updateFloat("DISCOUNT",(float)getAttribute("DISCOUNT").getVal());
            rsResultSet.updateFloat("SEAM_CHG",(float)getAttribute("SEAM_CHG").getVal());
            rsResultSet.updateFloat("EXCISE",(float)getAttribute("EXCISE").getVal());
            rsResultSet.updateString("APPROX_AMOUNT",getAttribute("APPROX_AMOUNT").getString());
            rsResultSet.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsResultSet.updateString("POSITION",getAttribute("POSITION").getString());
            rsResultSet.updateString("REF_NO",getAttribute("REF_NO").getString());
            rsResultSet.updateString("CONF_NO",getAttribute("CONF_NO").getString());
            rsResultSet.updateString("PRIORITY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID+"-"+getAttribute("LAST_PRIO_USR").getString());
            //rsResultSet.updateString("PRIORITY_DATE", getAttribute("PRIORITY_DATE").getString());
            //rsResultSet.updateString("PRIORITY_DATE",date);
            rsResultSet.updateString("WVG_DATE",getAttribute("WVG_DATE").getString());
            rsResultSet.updateString("MND_DATE",getAttribute("MND_DATE").getString());
            rsResultSet.updateString("NDL_DATE",getAttribute("NDL_DATE").getString());
            rsResultSet.updateString("COL006",getAttribute("COL006").getString());            
            rsResultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateInt("MODIFIED_BY",0);
            rsResultSet.updateString("MODIFIED_DATE","0000-00-00");
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            
             //rsResultSet.updateString("ORDER_DDMM_D",getAttribute("ORD_DDMM_D").getString());
            //rsResultSet.updateInt("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getInt());
            
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",EITLERPGLOBAL.gNewUserID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REMARKS").getString());
            //rsHistory.updateString("ORDER_NO",getAttribute("ORDER_NO").getString());
            rsHistory.updateString("PIECE_NO",getAttribute("PIECE_NO").getString());
            rsHistory.updateString("ORDER_DATE",getAttribute("ORDER_DATE").getString());
            rsHistory.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHistory.updateString("PARTY_CD", getAttribute("PARTY_CD").getString());
            rsHistory.updateDouble("LNGTH",getAttribute("LNGTH").getDouble());
            rsHistory.updateDouble("WIDTH",getAttribute("WIDTH").getDouble());
            rsHistory.updateInt("GSQ",getAttribute("GSQ").getInt());
            rsHistory.updateString("DELIV_DATE",getAttribute("DELIV_DATE").getString());
            rsHistory.updateString("COMM_DATE",getAttribute("COMM_DATE").getString());
            rsHistory.updateString("ORDER_CODE",getAttribute("ORDER_CODE").getString());
            rsHistory.updateString("GRUP",getAttribute("GRUP").getString());
            rsHistory.updateString("INV_NO",getAttribute("INV_NO").getString());
            rsHistory.updateString("INV_DATE",getAttribute("INV_DATE").getString());
            rsHistory.updateString("PROD_IND",getAttribute("PROD_IND").getString());
            rsHistory.updateString("PROD_IND_A",getAttribute("PROD_IND_A").getString());
            rsHistory.updateString("ORD_DDMM_A",getAttribute("ORD_DDMM_A").getString());
            rsHistory.updateString("PROD_IND_B",getAttribute("PROD_IND_B").getString());
            rsHistory.updateString("ORD_DDMM_B",getAttribute("ORD_DDMM_B").getString());
            rsHistory.updateString("PROD_IND_C",getAttribute("PROD_IND_C").getString());
            rsHistory.updateString("ORD_DDMM_C",getAttribute("ORD_DDMM_C").getString());
            rsHistory.updateString("PROD_CODE",getAttribute("PROD_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("BALNK",getAttribute("BALNK").getString());
            rsHistory.updateString("WEIGHT",df.format(getAttribute("WEIGHT").getVal()).toString());
            rsHistory.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsHistory.updateFloat("INV_AMOUNT",(float)getAttribute("INV_AMOUNT").getVal());
            rsHistory.updateFloat("BAS_AMT",(float)getAttribute("BAS_AMT").getVal());
            rsHistory.updateFloat("WPSC",(float)getAttribute("WPSC").getVal());
            rsHistory.updateFloat("OTHER",(float)getAttribute("OTHER").getVal());
            rsHistory.updateFloat("DISCOUNT",(float)getAttribute("DISCOUNT").getVal());
            rsHistory.updateFloat("SEAM_CHG",(float)getAttribute("SEAM_CHG").getVal());
            rsHistory.updateFloat("EXCISE",(float)getAttribute("EXCISE").getVal());
            rsHistory.updateString("APPROX_AMOUNT",getAttribute("APPROX_AMOUNT").getString());
            rsHistory.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsHistory.updateString("POSITION",getAttribute("POSITION").getString());
            rsHistory.updateString("REF_NO",getAttribute("REF_NO").getString());
            rsHistory.updateString("CONF_NO",getAttribute("CONF_NO").getString());
            rsHistory.updateString("PRIORITY_DATE",getAttribute("PRIORITY_DATE").getString());
            rsHistory.updateString("LAST_PRIO_USR",getAttribute("LAST_PRIO_USR").getString());
            rsHistory.updateString("WVG_DATE",getAttribute("WVG_DATE").getString());
            rsHistory.updateString("MND_DATE",getAttribute("MND_DATE").getString());
            rsHistory.updateString("NDL_DATE",getAttribute("NDL_DATE").getString());
            rsHistory.updateString("COL006",getAttribute("COL006").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());            
            rsHistory.updateInt("MODIFIED_BY",0);
            rsHistory.updateString("MODIFIED_DATE","0000-00-00");
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.insertRow();
            
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();            
            ObjFlow.ModuleID=clsOrder_1.ModuleID;            
            ObjFlow.DocNo=(String)getAttribute("PIECE_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            //ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.TableName="PRODUCTION.FELT_ORDER_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="PIECE_NO";
            
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
            
            /*
            if(ObjFlow.Status.equals("F")) {
                String MainAccCode=getAttribute("ACCOUNT_CODES").getString();
                AddPartyToFinance((String)getAttribute("PARTY_CODE").getObj(),MainAccCode,getAttribute("MAIN_ACCOUNT_CODE").getString());
            }
             */
            //================= Approval Flow Update complete ===================//
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();           
            LastError= e.getMessage();
            return false;
        }
    }    
    
    
    public boolean Filter(String Condition) {
        Ready=false;        
        try {
            //String strSQL="";
            //String strSQL= "SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER " + Condition ;
            String strSQL= "SELECT * FROM PRODUCTION.FELT_ORDER_MASTER " + Condition ;
            /*
            String strSQL= "SELECT A.*,PRIORITY_DESC FROM (SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ";
            strSQL+= Condition;
            strSQL+=") AS A LEFT JOIN (SELECT PRIORITY_DESC,PRIORITY_ID FROM PRODUCTION.FELT_PRIORITY_MASTER ) AS B ON A.PRIORITY=B.PRIORITY_ID";
            System.out.println(strSQL);
             */
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY PARTY_CD ";
                //strSQL = "SELECT PIECE_NO,ORDER_DATE,PRODUCT_CODE,PARTY_CD FROM PRODUCTION.FELT_ORDER_MASTER ORDER BY PARTY_CD ";
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
    
    public boolean Update() {
        
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
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
            java.sql.Date OrderDate=java.sql.Date.valueOf((String)getAttribute("ORDER_DATE").getObj());
            
            if((OrderDate.after(FinFromDate)||OrderDate.compareTo(FinFromDate)==0)&&(OrderDate.before(FinToDate)||OrderDate.compareTo(FinToDate)==0)) {
                //Withing the year
            }
            else {
                LastError="Order date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
            rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER_H WHERE PIECE_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//            
            
            String theDocNo=getAttribute("PIECE_NO").getString();
            rsResultSet.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsResultSet.updateString("ORDER_DATE", getAttribute("ORDER_DATE").getString());
            rsResultSet.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsResultSet.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            rsResultSet.updateDouble("LNGTH",getAttribute("LNGTH").getDouble());
            rsResultSet.updateDouble("WIDTH",getAttribute("WIDTH").getDouble());
            rsResultSet.updateInt("GSQ",getAttribute("GSQ").getInt());
            rsResultSet.updateString("DELIV_DATE",getAttribute("DELIV_DATE").getString());
            rsResultSet.updateString("COMM_DATE",getAttribute("COMM_DATE").getString());
            rsResultSet.updateString("ORDER_CODE",getAttribute("ORDER_CODE").getString());
            rsResultSet.updateString("GRUP",getAttribute("GRUP").getString());
            rsResultSet.updateString("INV_NO",getAttribute("INV_NO").getString());
            rsResultSet.updateString("INV_DATE",getAttribute("INV_DATE").getString());
            rsResultSet.updateString("PROD_IND",getAttribute("PROD_IND").getString());
            rsResultSet.updateString("PROD_IND_A",getAttribute("PROD_IND_A").getString());
            rsResultSet.updateString("ORD_DDMM_A",getAttribute("ORD_DDMM_A").getString());
            rsResultSet.updateString("PROD_IND_B",getAttribute("PROD_IND_B").getString());
            rsResultSet.updateString("ORD_DDMM_B",getAttribute("ORD_DDMM_B").getString());
            rsResultSet.updateString("PROD_IND_C",getAttribute("PROD_IND_C").getString());
            rsResultSet.updateString("ORD_DDMM_C",getAttribute("ORD_DDMM_C").getString());
            rsResultSet.updateString("PROD_CODE",getAttribute("PROD_CODE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateString("BALNK",getAttribute("BALNK").getString());
            rsResultSet.updateString("WEIGHT",df.format(getAttribute("WEIGHT").getVal()).toString());
            //rsResultSet.updateString("CREATED_BY",getAttribute("CREATED_BY").getString());
            //rsResultSet.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsResultSet.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            //rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsResultSet.updateString("PRIORITY",(String)getAttribute("PRIORITY").getObj());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateFloat("INV_AMOUNT",(float)getAttribute("INV_AMOUNT").getVal());
            rsResultSet.updateFloat("BAS_AMT",(float)getAttribute("BAS_AMT").getVal());
            rsResultSet.updateFloat("WPSC",(float)getAttribute("WPSC").getVal());
            rsResultSet.updateFloat("OTHER",(float)getAttribute("OTHER").getVal());
            rsResultSet.updateFloat("DISCOUNT",(float)getAttribute("DISCOUNT").getVal());
            rsResultSet.updateFloat("SEAM_CHG",(float)getAttribute("SEAM_CHG").getVal());
            rsResultSet.updateFloat("EXCISE",(float)getAttribute("EXCISE").getVal());
            rsResultSet.updateString("APPROX_AMOUNT",getAttribute("APPROX_AMOUNT").getString());
            rsResultSet.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsResultSet.updateString("POSITION",getAttribute("POSITION").getString());
            rsResultSet.updateString("REF_NO",getAttribute("REF_NO").getString());
            rsResultSet.updateString("CONF_NO",getAttribute("CONF_NO").getString());
            rsResultSet.updateString("PRIORITY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID+"-"+getAttribute("LAST_PRIO_USR").getString());
            rsResultSet.updateString("WVG_DATE",getAttribute("WVG_DATE").getString());
            rsResultSet.updateString("MND_DATE",getAttribute("MND_DATE").getString());
            rsResultSet.updateString("NDL_DATE",getAttribute("NDL_DATE").getString());
            rsResultSet.updateString("COL006",getAttribute("COL006").getString());            
            rsResultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());    
            rsResultSet.updateBoolean("APPROVED",false);
            rsResultSet.updateString("APPROVED_DATE","0000-00-00");
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateRow();                        
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.           
            
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM PRODUCTION.FELT_ORDER_MASTER_H WHERE PIECE_NO='"+(String)getAttribute("PIECE_NO").getObj()+"'");
            RevNo++;
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY",EITLERPGLOBAL.gNewUserID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            //rsHistory.updateString("ORDER_NO", getAttribute("ORDER_NO").getString());
            rsHistory.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsHistory.updateString("ORDER_DATE",getAttribute("ORDER_DATE").getString());
            rsHistory.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsHistory.updateString("PARTY_CD", getAttribute("PARTY_CD").getString());
            rsHistory.updateDouble("LNGTH",getAttribute("LNGTH").getDouble());
            rsHistory.updateDouble("WIDTH",getAttribute("WIDTH").getDouble());            
            rsHistory.updateInt("GSQ", getAttribute("GSQ").getInt());
            rsHistory.updateString("DELIV_DATE",getAttribute("DELIV_DATE").getString());
            rsHistory.updateString("COMM_DATE",getAttribute("COMM_DATE").getString());
            rsHistory.updateString("ORDER_CODE",getAttribute("ORDER_CODE").getString());
            rsHistory.updateString("GRUP",getAttribute("GRUP").getString());            
            rsHistory.updateString("INV_NO",getAttribute("INV_NO").getString());            
            rsHistory.updateString("INV_DATE",getAttribute("INV_DATE").getString());
            rsHistory.updateString("PROD_IND",getAttribute("PROD_IND").getString());
            rsHistory.updateString("PROD_IND_A",getAttribute("PROD_IND_A").getString());
            rsHistory.updateString("ORD_DDMM_A",getAttribute("ORD_DDMM_A").getString());
            rsHistory.updateString("PROD_IND_B",getAttribute("PROD_IND_B").getString());
            rsHistory.updateString("ORD_DDMM_B",getAttribute("ORD_DDMM_B").getString());
            rsHistory.updateString("PROD_IND_C",getAttribute("PROD_IND_C").getString());
            rsHistory.updateString("ORD_DDMM_C",getAttribute("ORD_DDMM_C").getString());
            rsHistory.updateString("PROD_CODE",getAttribute("PROD_CODE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateString("BALNK",getAttribute("BALNK").getString());
            rsHistory.updateString("WEIGHT",df.format(getAttribute("WEIGHT").getVal()).toString());
            rsHistory.updateInt("CREATED_BY",getAttribute("CREATED_BY").getInt());
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateInt("PRIORITY",(int)getAttribute("PRIORITY").getVal());
            //rsHistory.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsHistory.updateString("PRIORITY",(String)getAttribute("PRIORITY").getObj());
            //rsHistory.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsHistory.updateFloat("INV_AMOUNT",(float)getAttribute("INV_AMOUNT").getVal());
            rsHistory.updateFloat("BAS_AMT",(float)getAttribute("BAS_AMT").getVal());
            rsHistory.updateFloat("WPSC",(float)getAttribute("WPSC").getVal());
            rsHistory.updateFloat("OTHER",(float)getAttribute("OTHER").getVal());
            rsHistory.updateFloat("DISCOUNT",(float)getAttribute("DISCOUNT").getVal());
            rsHistory.updateFloat("SEAM_CHG",(float)getAttribute("SEAM_CHG").getVal());
            rsHistory.updateFloat("EXCISE",(float)getAttribute("EXCISE").getVal());
            rsHistory.updateString("APPROX_AMOUNT",getAttribute("APPROX_AMOUNT").getString());
            rsHistory.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsHistory.updateString("POSITION",getAttribute("POSITION").getString());
            rsHistory.updateString("REF_NO",getAttribute("REF_NO").getString());
            rsHistory.updateString("CONF_NO",getAttribute("CONF_NO").getString());
            rsHistory.updateString("PRIORITY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            //rsResultSet.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("LAST_PRIO_USR",EITLERPGLOBAL.gLoginID+"-"+getAttribute("LAST_PRIO_USR").getString());
            rsHistory.updateString("WVG_DATE",getAttribute("WVG_DATE").getString());
            rsHistory.updateString("MND_DATE",getAttribute("MND_DATE").getString());
            rsHistory.updateString("NDL_DATE",getAttribute("NDL_DATE").getString());
            rsHistory.updateString("COL006",getAttribute("COL006").getString());            
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateInt("MODIFIED_BY",getAttribute("MODIFIED_BY").getInt());
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());    
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());                      
            rsHistory.insertRow();
            
           
            //======== Update the Approval Flow =========
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            clsFeltProductionApprovalFlow ObjFlow=new clsFeltProductionApprovalFlow();
            ObjFlow.ModuleID=clsOrder_1.ModuleID;            
            ObjFlow.DocNo=(String)getAttribute("PIECE_NO").getObj();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();            
            ObjFlow.TableName="PRODUCTION.FELT_ORDER_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="PIECE_NO";
            //String qry = "UPDATE FELT_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CODE").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsSales_Party.ModuleID;
            //String qry = "UPDATE PRODUCTION.FELT_PROD_DATA SET PROD_DOC_NO='"+getAttribute("ORDER_NO").getString()+"' WHERE PROD_DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsOrder.ModuleID;
            //data.Execute(qry);
            
            //==== Handling Rejected Documents ==========//
            //String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
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
                data.Execute("UPDATE PRODUCTION.FELT_ORDER_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE PIECE_NO='"+ObjFlow.DocNo+"' ");
                //Remove Old Records from FELT_DOC_DATA
                data.Execute("DELETE FROM FELT_PROD_DOC_DATA WHERE MODULE_ID="+clsOrder_1.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");                
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
            
            /*
            if(ObjFlow.Status.equals("F")) {
                String MainAccCode=getAttribute("ACCOUNT_CODES").getString();
                AddPartyToFinance((String)getAttribute("PARTY_CODE").getObj(),MainAccCode,getAttribute("MAIN_ACCOUNT_CODE").getString());
            }
            */
            return true;
       
        }
        catch(Exception e) {
            e.printStackTrace();
            LastError = e.getMessage();
            return false;
        }
    }
    
    public void AddPartyToFinance(String PartyCode,String MainAccCode,String MainCode) {
        //========== Add Party to Finance Party Master ===============//
        
        clsSales_Party ObjParty=new clsSales_Party();
        String sMainCode[] = MainAccCode.trim().split(",");
        ObjParty.Filter("WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ MainCode +"' ");
        for(int n=0;n<sMainCode.length;n++) {
            String Main_Code = sMainCode[n].trim();
            try {
                if(data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ Main_Code +"' ",FinanceGlobal.FinURL)) {
                    data.Execute("DELETE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='"+ Main_Code +"' ",FinanceGlobal.FinURL);
                }
                
                Connection FinConn;
                Statement stFinParty;
                ResultSet rsFinParty;
                
                FinConn=data.getConn(FinanceGlobal.FinURL);
                stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
                
                long Counter=data.getLongValueFromDB("SELECT MAX(PARTY_ID)+1 AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);
                
                rsFinParty.moveToInsertRow();
                rsFinParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                rsFinParty.updateString("PARTY_CODE",ObjParty.getAttribute("PARTY_CODE").getString());
                rsFinParty.updateString("MAIN_ACCOUNT_CODE",Main_Code);
                rsFinParty.updateLong("PARTY_ID",Counter);
                rsFinParty.updateString("PARTY_NAME",ObjParty.getAttribute("PARTY_NAME").getString());
                rsFinParty.updateString("SH_NAME","");
                rsFinParty.updateString("REMARKS",ObjParty.getAttribute("REMARKS").getString());
                rsFinParty.updateString("SH_CODE","");
                rsFinParty.updateDouble("CREDIT_DAYS",ObjParty.getAttribute("CREDIT_DAYS").getDouble());
                rsFinParty.updateDouble("CREDIT_LIMIT",0);
                rsFinParty.updateDouble("DEBIT_LIMIT",0);
                rsFinParty.updateString("TIN_NO",ObjParty.getAttribute("TIN_NO").getString());
                rsFinParty.updateString("TIN_DATE",ObjParty.getAttribute("TIN_DATE").getString());
                rsFinParty.updateString("CST_NO",ObjParty.getAttribute("CST_NO").getString());
                rsFinParty.updateString("CST_DATE",ObjParty.getAttribute("CST_DATE").getString());
                rsFinParty.updateString("ECC_NO",ObjParty.getAttribute("ECC_NO").getString());
                rsFinParty.updateString("ECC_DATE",ObjParty.getAttribute("ECC_DATE").getString());
                rsFinParty.updateString("SERVICE_TAX_NO","");
                rsFinParty.updateString("SERVICE_TAX_DATE","0000-00-00");
                rsFinParty.updateString("SSI_NO","");
                rsFinParty.updateString("SSI_DATE","0000-00-00");
                rsFinParty.updateString("ESI_NO","");
                rsFinParty.updateString("ESI_DATE","0000-00-00");
                rsFinParty.updateString("ADDRESS",ObjParty.getAttribute("ADDRESS1").getString().trim()+ " " +ObjParty.getAttribute("ADDRESS2").getString().trim());
                rsFinParty.updateString("CITY",ObjParty.getAttribute("CITY_ID").getString());
                rsFinParty.updateString("PINCODE",ObjParty.getAttribute("PINCODE").getString());
                rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString());
                rsFinParty.updateString("COUNTRY","");
                rsFinParty.updateString("PHONE",ObjParty.getAttribute("PHONE_NO").getString());
                rsFinParty.updateString("FAX","");
                rsFinParty.updateString("MOBILE",ObjParty.getAttribute("MOBILE_NO").getString());
                rsFinParty.updateString("EMAIL",ObjParty.getAttribute("EMAIL").getString());
                rsFinParty.updateString("URL",ObjParty.getAttribute("URL").getString());
                rsFinParty.updateInt("APPROVED",1);
                rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateInt("REJECTED",0);
                rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                rsFinParty.updateString("REJECTED_REMARKS","");
                rsFinParty.updateInt("HIERARCHY_ID",0);
                rsFinParty.updateInt("CANCELLED",0);
                rsFinParty.updateInt("BLOCKED",0);
                rsFinParty.updateString("PAN_NO",ObjParty.getAttribute("PAN_NO").getString());
                rsFinParty.updateString("PAN_DATE",ObjParty.getAttribute("PAN_DATE").getString());
                rsFinParty.updateInt("CHANGED",0);
                rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateString("CREATED_BY","System");
                rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateString("MODIFIED_BY","System");
                rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                rsFinParty.updateDouble("CLOSING_BALANCE",0);
                rsFinParty.updateString("CLOSING_EFFECT","D");
                rsFinParty.updateInt("PARTY_TYPE",ObjParty.getAttribute("PARTY_TYPE").getInt());
                rsFinParty.updateString("CHARGE_CODE",ObjParty.getAttribute("CHARGE_CODE").getString());
                rsFinParty.updateString("CHARGE_CODE_II",ObjParty.getAttribute("CHARGE_CODE_II").getString());
                rsFinParty.insertRow();
                
                //rsFinParty.close();
                //stFinParty.close();
                //FinConn.close();
                
            } catch(Exception p) {
                p.printStackTrace();
            }
        }
        //            }
        //            else {
        //                String Main_Code = sMainCode[n].trim();
        //                try {
        //
        //                    //data.Execute("DELETE FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='" + PartyCode + "' AND MAIN_ACCOUNT_CODE='"+ Main_Code +"' ",FinanceGlobal.FinURL);
        //
        //                    Connection FinConn;
        //                    Statement stFinParty;
        //                    ResultSet rsFinParty;
        //
        //                    FinConn=data.getConn(FinanceGlobal.FinURL);
        //                    stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        //                    rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
        //
        //                    long Counter=data.getLongValueFromDB("SELECT MAX(PARTY_ID)+1 AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);
        //                    ObjParty.Filter("WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ MainCode +"' ");
        //
        //                    rsFinParty.moveToInsertRow();
        //                    rsFinParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        //                    rsFinParty.updateString("PARTY_CODE",PartyCode);
        //                    rsFinParty.updateString("MAIN_ACCOUNT_CODE",Main_Code);
        //                    rsFinParty.updateLong("PARTY_ID",Counter);
        //                    rsFinParty.updateString("PARTY_NAME",ObjParty.getAttribute("PARTY_NAME").getString());
        //                    rsFinParty.updateString("SH_NAME","");
        //                    rsFinParty.updateString("REMARKS",ObjParty.getAttribute("REMARKS").getString());
        //                    rsFinParty.updateString("SH_CODE","");
        //                    rsFinParty.updateDouble("CREDIT_DAYS",ObjParty.getAttribute("CREDIT_DAYS").getDouble());
        //                    rsFinParty.updateDouble("CREDIT_LIMIT",0);
        //                    rsFinParty.updateDouble("DEBIT_LIMIT",0);
        //                    rsFinParty.updateString("TIN_NO",ObjParty.getAttribute("TIN_NO").getString());
        //                    rsFinParty.updateString("TIN_DATE",ObjParty.getAttribute("TIN_DATE").getString());
        //                    rsFinParty.updateString("CST_NO",ObjParty.getAttribute("CST_NO").getString());
        //                    rsFinParty.updateString("CST_DATE",ObjParty.getAttribute("CST_DATE").getString());
        //                    rsFinParty.updateString("ECC_NO",ObjParty.getAttribute("ECC_NO").getString());
        //                    rsFinParty.updateString("ECC_DATE",ObjParty.getAttribute("ECC_DATE").getString());
        //                    rsFinParty.updateString("SERVICE_TAX_NO","");
        //                    rsFinParty.updateString("SERVICE_TAX_DATE","0000-00-00");
        //                    rsFinParty.updateString("SSI_NO","");
        //                    rsFinParty.updateString("SSI_DATE","0000-00-00");
        //                    rsFinParty.updateString("ESI_NO","");
        //                    rsFinParty.updateString("ESI_DATE","0000-00-00");
        //                    rsFinParty.updateString("ADDRESS",ObjParty.getAttribute("ADDRESS1").getString().trim()+ " " +ObjParty.getAttribute("ADDRESS2").getString().trim());
        //                    rsFinParty.updateString("CITY",ObjParty.getAttribute("CITY_ID").getString());
        //                    rsFinParty.updateString("PINCODE",ObjParty.getAttribute("PINCODE").getString());
        //                    rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString());
        //                    rsFinParty.updateString("COUNTRY","");
        //                    rsFinParty.updateString("PHONE",ObjParty.getAttribute("PHONE_NO").getString());
        //                    rsFinParty.updateString("FAX","");
        //                    rsFinParty.updateString("MOBILE",ObjParty.getAttribute("MOBILE_NO").getString());
        //                    rsFinParty.updateString("EMAIL",ObjParty.getAttribute("EMAIL").getString());
        //                    rsFinParty.updateString("URL",ObjParty.getAttribute("URL").getString());
        //                    rsFinParty.updateInt("APPROVED",1);
        //                    rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateInt("REJECTED",0);
        //                    rsFinParty.updateString("REJECTED_DATE","0000-00-00");
        //                    rsFinParty.updateString("REJECTED_REMARKS","");
        //                    rsFinParty.updateInt("HIERARCHY_ID",0);
        //                    rsFinParty.updateInt("CANCELLED",0);
        //                    rsFinParty.updateInt("BLOCKED",0);
        //                    rsFinParty.updateString("PAN_NO",ObjParty.getAttribute("PAN_NO").getString());
        //                    rsFinParty.updateString("PAN_DATE",ObjParty.getAttribute("PAN_DATE").getString());
        //                    rsFinParty.updateInt("CHANGED",0);
        //                    rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateString("CREATED_BY","System");
        //                    rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateString("MODIFIED_BY","System");
        //                    rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //                    rsFinParty.updateDouble("CLOSING_BALANCE",0);
        //                    rsFinParty.updateString("CLOSING_EFFECT","D");
        //                    rsFinParty.updateInt("PARTY_TYPE",ObjParty.getAttribute("PARTY_TYPE").getInt());
        //                    rsFinParty.updateString("CHARGE_CODE",ObjParty.getAttribute("CHARGE_CODE").getString());
        //                    rsFinParty.updateString("CHARGE_CODE_II",ObjParty.getAttribute("CHARGE_CODE_II").getString());
        //                    rsFinParty.insertRow();
        //
        //                    //rsFinParty.close();
        //                    //stFinParty.close();
        //                    //FinConn.close();
        //                }
        //                catch(Exception p) {
        //                    p.printStackTrace();
        //                }
        //            }
        
        
       /* if(ObjParty.Filter("WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+ sMainCode[0].trim() +"' ")) {
            for(int n=0;n<sMainCode.length;n++) {
                String Main_Code = sMainCode[n].trim();
                try {
                    Connection FinConn;
                    Statement stFinParty;
                    ResultSet rsFinParty;
        
                    FinConn=data.getConn(FinanceGlobal.FinURL);
                    stFinParty=FinConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                    rsFinParty=stFinParty.executeQuery("SELECT * FROM D_FIN_PARTY_MASTER LIMIT 1");
        
                    long Counter=data.getLongValueFromDB("SELECT MAX(PARTY_ID)+1 AS PARTY_ID FROM D_FIN_PARTY_MASTER", FinanceGlobal.FinURL);
        
                    rsFinParty.moveToInsertRow();
                    rsFinParty.updateInt("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                    rsFinParty.updateString("PARTY_CODE",ObjParty.getAttribute("PARTY_CODE").getString());
                    rsFinParty.updateString("MAIN_ACCOUNT_CODE",Main_Code);
                    rsFinParty.updateLong("PARTY_ID",Counter);
                    rsFinParty.updateString("PARTY_NAME",ObjParty.getAttribute("PARTY_NAME").getString());
                    rsFinParty.updateString("SH_NAME","");
                    rsFinParty.updateString("REMARKS",ObjParty.getAttribute("REMARKS").getString());
                    rsFinParty.updateString("SH_CODE","");
                    rsFinParty.updateDouble("CREDIT_DAYS",ObjParty.getAttribute("CREDIT_DAYS").getDouble());
                    rsFinParty.updateDouble("CREDIT_LIMIT",0);
                    rsFinParty.updateDouble("DEBIT_LIMIT",0);
                    rsFinParty.updateString("TIN_NO",ObjParty.getAttribute("TIN_NO").getString());
                    rsFinParty.updateString("TIN_DATE",ObjParty.getAttribute("TIN_DATE").getString());
                    rsFinParty.updateString("CST_NO",ObjParty.getAttribute("CST_NO").getString());
                    rsFinParty.updateString("CST_DATE",ObjParty.getAttribute("CST_DATE").getString());
                    rsFinParty.updateString("ECC_NO",ObjParty.getAttribute("ECC_NO").getString());
                    rsFinParty.updateString("ECC_DATE",ObjParty.getAttribute("ECC_DATE").getString());
                    rsFinParty.updateString("SERVICE_TAX_NO","");
                    rsFinParty.updateString("SERVICE_TAX_DATE","0000-00-00");
                    rsFinParty.updateString("SSI_NO","");
                    rsFinParty.updateString("SSI_DATE","0000-00-00");
                    rsFinParty.updateString("ESI_NO","");
                    rsFinParty.updateString("ESI_DATE","0000-00-00");
                    rsFinParty.updateString("ADDRESS",ObjParty.getAttribute("ADDRESS1").getString().trim()+ " " +ObjParty.getAttribute("ADDRESS2").getString().trim());
                    rsFinParty.updateString("CITY",ObjParty.getAttribute("CITY_ID").getString());
                    rsFinParty.updateString("PINCODE",ObjParty.getAttribute("PINCODE").getString());
                    rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString());
                    rsFinParty.updateString("COUNTRY","");
                    rsFinParty.updateString("PHONE",ObjParty.getAttribute("PHONE_NO").getString());
                    rsFinParty.updateString("FAX","");
                    rsFinParty.updateString("MOBILE",ObjParty.getAttribute("MOBILE_NO").getString());
                    rsFinParty.updateString("EMAIL",ObjParty.getAttribute("EMAIL").getString());
                    rsFinParty.updateString("URL",ObjParty.getAttribute("URL").getString());
                    rsFinParty.updateInt("APPROVED",1);
                    rsFinParty.updateString("APPROVED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateInt("REJECTED",0);
                    rsFinParty.updateString("REJECTED_DATE","0000-00-00");
                    rsFinParty.updateString("REJECTED_REMARKS","");
                    rsFinParty.updateInt("HIERARCHY_ID",0);
                    rsFinParty.updateInt("CANCELLED",0);
                    rsFinParty.updateInt("BLOCKED",0);
                    rsFinParty.updateString("PAN_NO",ObjParty.getAttribute("PAN_NO").getString());
                    rsFinParty.updateString("PAN_DATE",ObjParty.getAttribute("PAN_DATE").getString());
                    rsFinParty.updateInt("CHANGED",0);
                    rsFinParty.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("CREATED_BY","System");
                    rsFinParty.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateString("MODIFIED_BY","System");
                    rsFinParty.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsFinParty.updateDouble("CLOSING_BALANCE",0);
                    rsFinParty.updateString("CLOSING_EFFECT","D");
                    rsFinParty.updateInt("PARTY_TYPE",ObjParty.getAttribute("PARTY_TYPE").getInt());
                    rsFinParty.updateString("CHARGE_CODE",ObjParty.getAttribute("CHARGE_CODE").getString());
                    rsFinParty.updateString("CHARGE_CODE_II",ObjParty.getAttribute("CHARGE_CODE_II").getString());
                    rsFinParty.insertRow();
        
                    rsFinParty.close();
                    //stFinParty.close();
                    //FinConn.close();
                }
                catch(Exception p) {
        
                }
        
            }*/
        //============================================================//
        
    }
    
    
    public boolean CanDelete(int CompanyID,String PartyCode) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL="SELECT COUNT(*) AS COUNT FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+Integer.toString(CompanyID)+" AND PARTY_CODE='"+PartyCode+"' AND (APPROVED=1)";
            int Count=data.getIntValueFromDB(strSQL);
            
            if(Count>0)  //Item is Approved
            {
                //Discard the request
                return false;
            }
            else {
                return true;
            }
            
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public boolean Delete() {
        try {
            String OrderNo=getAttribute("PIECE_NO").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,OrderNo)) {
                String strSQL = "DELETE FROM PRODUCTION.FELT_ORDER_MASTER WHERE PIECE_NO='"+OrderNo+"'";
                data.Execute(strSQL);
                
                LoadData(EITLERPGLOBAL.gCompanyID);
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
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(CompanyID) + " AND PARTY_CODE='" + PartyCode + "' ";
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
                data.Execute("DELETE FROM FELT_PROD_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' ");
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
                strSQL="SELECT PRODUCTION.FELT_ORDER_MASTER.PIECE_NO,PRODUCTION.FELT_ORDER_MASTER.ORDER_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_ORDER_MASTER.PIECE_NO=FELT_PROD_DOC_DATA.DOC_NO AND FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID="+clsOrder_1.ModuleID+" AND PRODUCTION.FELT_ORDER_MASTER.APPROVED=0 ORDER BY PRODUCTION.FELT_PROD_DOC_DATA.RECEIVED_DATE";                
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT PRODUCTION.FELT_ORDER_MASTER.PIECE_NO,PRODUCTION.FELT_ORDER_MASTER.ORDER_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_ORDER_MASTER.PIECE_NO=FELT_PROD_DOC_DATA.DOC_NO AND FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID="+clsOrder_1.ModuleID+" AND PRODUCTION.FELT_ORDER_MASTER.APPROVED=0 ORDER BY PRODUCTION.FELT_ORDER_MASTER.ORDER_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT PRODUCTION.FELT_ORDER_MASTER.PIECE_NO,PRODUCTION.FELT_ORDER_MASTER.ORDER_DATE,RECEIVED_DATE FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_PROD_DOC_DATA WHERE PRODUCTION.FELT_ORDER_MASTER.PIECE_NO=FELT_PROD_DOC_DATA.DOC_NO AND FELT_PROD_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND FELT_PROD_DOC_DATA.STATUS='W' AND MODULE_ID="+clsOrder_1.ModuleID+" AND PRODUCTION.FELT_ORDER_MASTER.APPROVED=0 ORDER BY PRODUCTION.FELT_ORDER_MASTER.PIECE_NO";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsOrder_1 ObjItem=new clsOrder_1();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("PIECE_NO",rsTmp.getString("PIECE_NO"));
                ObjItem.setAttribute("ORDER_DATE",rsTmp.getString("ORDER_DATE"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                //ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
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
    
    public boolean ShowHistory(int pCompanyID,String pDocNo) {
        Ready=false;
        try {
            //Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pDocNo+"' ORDER BY REVISION_NO");
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
     
      public static String getNewDisBasamt(String pDiscper,String pBasamt) {
        double Discper = Double.parseDouble(pDiscper);
        double Basamt = Double.parseDouble(pBasamt);
                
        double newDisBasamt=0.00;
        newDisBasamt=Basamt-((Basamt)*(Discper/100));
        //return Double.toString(Math.round(newDisamt));
        return Double.toString(newDisBasamt);
        
    }
      
      public static String getNewExcise(String pDisbasamt,String pWpsc) {
        double Disbasamt = Double.parseDouble(pDisbasamt);
        double Wpsc = Double.parseDouble(pWpsc);
                
        double newExcise=0.00;
        newExcise=((Disbasamt+Wpsc)*.12)+((Disbasamt+Wpsc)*.12)*.01+((Disbasamt+Wpsc)*.12)*.02;
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
        if(InsInd==1)
        {
            newInsaccamt=Math.round((Math.round(NewDisbasamt+NewExcise+WPSC)+(Math.round(NewDisbasamt+NewExcise+WPSC)*.10))*.0016);
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
        if(InsInd==1)
        {
              newInvamt=NewDisbasamt+NewExcise+WPSC+NewInsaccamt;        
              DecimalFormat df = new DecimalFormat("##.##");
            return df.format(newInvamt);
        }
        else{
            newInvamt=NewDisbasamt+NewExcise+WPSC;
            DecimalFormat df = new DecimalFormat("##.##");
            return df.format(newInvamt);   
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
            String strSQL="SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INS_IND,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0  END) ,2) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INS_IND,PRIORITY_DATE,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(NAME)),'PARTY DELETED IN COBOL') AS NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INS_IND,0)AS INS_IND,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1 FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1 FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE ";
            //strSQL+="B.PIECE_NO="+pPieceNo+" AND ";
            strSQL+="A.PIECE_NO='"+pPieceNo+"' AND ";
            if(!pPartyCode.equals("")){
            strSQL+="B.PARTY_CD='"+pPartyCode+"' AND ";
            }
            strSQL+="A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 AND ST_FLAG_1 !='P')) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PARTY_MASTER) AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.REGION ";
            strSQL+="UNION ALL ";
            strSQL+="SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INS_IND,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INS_IND,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,REGION FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,PRODUCTION.FELT_PARTY_MASTER WHERE ";
            strSQL+="PIECE_NO='"+pPieceNo+"' ";
            if(!pPartyCode.equals("")){
            strSQL+="AND PARTY_CD='"+pPartyCode+"' ";
            }
            strSQL+="AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5,6,7,8)) AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.REGION ";
            //rsTmp=stTmp.executeQuery("SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CD,ITEM_DESC  AS ITEM,STYLE,MTRS_RCVD AS LNGTH ,WIDTH_RCVD AS WIDTH,GSQ,KG_RCVD AS WEIGHT,RATE,ROUND(BAS_AMT,2) AS BAS_AMT,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INS_IND,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,RCVD_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM AS ITEM_DESC,STYLE,LNGTH AS MTRS_ORDER,RCVD_MTR AS MTRS_RCVD ,WIDTH AS WIDTH_ORDER,RECD_WDTH AS WIDTH_RCVD,GSQ,WEIGHT AS KG_CALC,RECD_KG AS KG_RCVD,(SQM_RATE+WT_RATE) AS RATE,ROUND(BAS_AMT,2) AS BAS_AMT,MEMO_DATE,DISC_PER,ROUND(DISAMT,0) AS DISAMT,ROUND(DISBASAMT,0) AS DISBASAMT,ROUND(EXCISE,2) AS EXCISE,ROUND(WPSC,2) AS SEAM_CHG,CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0 END AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  + (CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0  END) ,2) AS INV_AMT,DATEDIFF(NOW(),RCVD_DATE) AS DAYS,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,INS_IND,PRIORITY_DATE,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE ,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,MEMO_DATE,DELIV_DATE ,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT -  (BAS_AMT* (COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA.PIECE_NO,PARTY_CD,COALESCE(LTRIM(RTRIM(NAME)),'PARTY DELETED IN COBOL') AS NAME,COALESCE(ZONE,' ZERO ')AS ZONE,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,COALESCE(INS_IND,0)AS INS_IND,DELIV_DATE ,COMM_DATE, BAS_AMT,WPSC, ST_FLAG_1,REGION FROM (SELECT PRIORITY_DATE,PRIORITY_DESC,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,FA1.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,WPSC, ST_FLAG_1 FROM (SELECT PRIORITY_DATE,PRIORITY,GSQ,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,ORDER_DATE,PRODUCT_CD AS PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,RCVD_DATE,LNGTH,RCVD_MTR,WIDTH,RECD_WDTH,WEIGHT,RECD_KG,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN RCVD_MTR * RECD_WDTH*SQM_RATE WHEN SQM_IND =0 THEN RECD_KG * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN RECD_KG*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN  RECD_WDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+ CASE WHEN SPR_IND=1 THEN  RECD_WDTH* CHARGES WHEN SPR_IND =0 THEN 0 END AS WPSC, ST_FLAG_1 FROM PRODUCTION.FELT_PIECE_REGISTER A,PRODUCTION.FELT_ORDER_MASTER B,PRODUCTION.FELT_RATE_MASTER H WHERE B.PIECE_NO="+pPieceNo+" AND B.PARTY_CD='"+pPartyCode+"' AND A.PRODUCT_CD = H.ITEM_CODE AND ( A.WH_CD =0  OR (A.WH_CD =2 AND ST_FLAG_1 !='P')) AND A.ORDER_NO = B.PIECE_NO ) AS FA1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON FA1.PRIORITY=P1.PRIORITY_ID) AS FA LEFT JOIN (SELECT * FROM PRODUCTION.FELT_PARTY_MASTER) AS FP ON FA.PARTY_CD = FP.PARTY_CODE) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(SUBSTRING(M.PIECE_NO,2,5),M.PIECE_EXT) AND PARTY_CD =PARTY_CODE ) AS B ) AS C ) AS D LEFT JOIN (SELECT INCHARGE_CD,INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.REGION UNION ALL SELECT PARTY_CD,NAME,PRIORITY_DESC,PIECE_NO,ORDER_DATE,DELIV_DATE,COMM_DATE,PRODUCT_CODE AS PRODUCT_CD,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,RATE,BAS_AMT,DISC_PER,DISAMT,DISBASAMT,EXCISE,SEAM_CHG,INSACC_AMT,INV_AMT,INS_IND,REF_NO,CONF_NO,MACHINE_NO,POSITION,ZONE,PRIORITY_DATE,INCHARGE_NAME FROM (SELECT PRIORITY_DESC,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,GSQ,WEIGHT,(SQM_RATE+WT_RATE) AS RATE,DELIV_DATE,COMM_DATE,WVG_DATE,MND_DATE,NDL_DATE,INS_IND,BAS_AMT,MEMO_DATE,DISC_PER,DISAMT,DISBASAMT,ROUND(EXCISE,2) AS EXCISE,WPSC AS SEAM_CHG,CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0  END  AS INSACC_AMT,ROUND((DISBASAMT+EXCISE+WPSC)  +(CASE WHEN INS_IND =1 THEN ROUND((ROUND(DISBASAMT+EXCISE+WPSC,0) +(ROUND(DISBASAMT+EXCISE+WPSC,0)*.10) )*.0016,0) WHEN INS_IND !=1 THEN 0  END) ,2) AS INV_AMT,ZONE,PRIORITY_DATE,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DISBASAMT,WPSC,DISC_PER,MEMO_DATE,DISAMT,BAS_AMT,DELIV_DATE,COMM_DATE ,((DISBASAMT+COALESCE(WPSC,0)) *.12+ ((DISBASAMT+COALESCE(WPSC,0)) *.12)*.01+((DISBASAMT+COALESCE(WPSC,0)) *.12)*.02) AS EXCISE,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,A.PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,MEMO_DATE,DELIV_DATE,COMM_DATE,BAS_AMT,WPSC,COALESCE(DISC_PER,0) AS DISC_PER,(BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISAMT,BAS_AMT - (BAS_AMT*(COALESCE(DISC_PER,0)/100) ) AS DISBASAMT,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,ITEM,STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,WPSC,PRIORITY_DESC,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,PRIORITY,REGION FROM (SELECT ZONE,REF_NO,CONF_NO,MACHINE_NO,POSITION,PIECE_NO,PARTY_CD,NAME,ORDER_DATE,PRODUCT_CODE,H.GRUP AS ITEM,REPLACE(BALNK,' ','') AS STYLE,LNGTH,WIDTH,WEIGHT,SQM_RATE,WT_RATE,SQM_IND,PROD_IND_A,INS_IND,DELIV_DATE,COMM_DATE,CASE WHEN SQM_IND =1 THEN LNGTH * WIDTH*SQM_RATE WHEN SQM_IND =0 THEN WEIGHT * WT_RATE END AS BAS_AMT,CASE WHEN CHEM_TRT_IN=1 THEN WEIGHT*CHARGES WHEN CHEM_TRT_IN =0  THEN 0 END+CASE WHEN PIN_IND=1 THEN WIDTH * CHARGES WHEN PIN_IND =0  THEN 0 END+CASE WHEN SPR_IND=1 THEN WIDTH* CHARGES WHEN SPR_IND =0  THEN 0 END AS WPSC,PRIORITY,GSQ,WVG_DATE,MND_DATE,NDL_DATE,PRIORITY_DATE,REGION FROM PRODUCTION.FELT_ORDER_MASTER,PRODUCTION.FELT_RATE_MASTER H,PRODUCTION.FELT_PARTY_MASTER WHERE PIECE_NO="+pPieceNo+" AND PARTY_CD='"+pPartyCode+"' AND PRODUCT_CODE*10 = ITEM_CODE AND PARTY_CD = PARTY_CODE AND PROD_IND_A IN ('') AND PRIORITY IN (1,2,3,4,5) ) AS A1 LEFT JOIN (SELECT PRIORITY_ID,PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER) AS P1 ON A1.PRIORITY=P1.PRIORITY_ID) AS A LEFT JOIN (SELECT * FROM PRODUCTION.FELT_DISCOUNT_MEMO) AS M ON A.PIECE_NO = CONCAT(M.PIECE_NO,M.PIECE_EXT) AND PARTY_CD=PARTY_CODE) AS B) AS C )  AS D LEFT JOIN (SELECT * FROM PRODUCTION.FELT_INCHARGE) AS FI ON FI.INCHARGE_CD=D.REGION ");
            rsTmp=stTmp.executeQuery(strSQL);
            System.out.println(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Piecedetail[0]=rsTmp.getString("PARTY_CD");
                Piecedetail[1]=rsTmp.getString("NAME");
                Piecedetail[2]=rsTmp.getString("PRIORITY_DESC");
                Piecedetail[3]=rsTmp.getString("PIECE_NO");
                Piecedetail[4]=rsTmp.getString("ORDER_DATE");
                Piecedetail[5]=rsTmp.getString("DELIV_DATE");
                Piecedetail[6]=rsTmp.getString("COMM_DATE");
                Piecedetail[7]=rsTmp.getString("PRODUCT_CD");
                Piecedetail[8]=rsTmp.getString("ITEM");
                
                Piecedetail[9]=rsTmp.getString("STYLE");
                Piecedetail[10]=rsTmp.getString("LNGTH");
                Piecedetail[11]=rsTmp.getString("WIDTH");
                Piecedetail[12]=rsTmp.getString("GSQ");
                Piecedetail[13]=rsTmp.getString("WEIGHT");
                Piecedetail[14]=rsTmp.getString("RATE");
                Piecedetail[15]=rsTmp.getString("BAS_AMT");
                Piecedetail[16]=rsTmp.getString("DISC_PER");
                Piecedetail[17]=rsTmp.getString("DISAMT");
                
                Piecedetail[18]=rsTmp.getString("DISBASAMT");
                Piecedetail[19]=rsTmp.getString("EXCISE");
                Piecedetail[20]=rsTmp.getString("SEAM_CHG");
                Piecedetail[21]=rsTmp.getString("INSACC_AMT");
                Piecedetail[22]=rsTmp.getString("INV_AMT");
                
                Piecedetail[23]=rsTmp.getString("INS_IND");
                Piecedetail[24]=rsTmp.getString("REF_NO");
                Piecedetail[25]=rsTmp.getString("CONF_NO");
                Piecedetail[26]=rsTmp.getString("MACHINE_NO");
                Piecedetail[27]=rsTmp.getString("POSITION");
                Piecedetail[28]=rsTmp.getString("ZONE");
                Piecedetail[29]=EITLERPGLOBAL.formatDate(rsTmp.getString("PRIORITY_DATE"));
                Piecedetail[30]=rsTmp.getString("INCHARGE_NAME");
                /*Piecedetail[31]=rsTmp.getString("");
                Piecedetail[32]=rsTmp.getString("");
                Piecedetail[33]=rsTmp.getString("");
                Piecedetail[34]=rsTmp.getString("");
                Piecedetail[35]=rsTmp.getString("");
                           
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
    
    public static boolean deleteParty(String PartyCode) {
        try {
            data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE DOC_NO='"+PartyCode+"' AND MODULE_ID='"+clsOrder_1.ModuleID+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
            data.Execute("DELETE FROM PRODUCTION.FELT_PROD_DOC_DATA WHERE PARTY_CODE='"+PartyCode+"' ");
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
    
    public boolean Insertp(){
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader,rsTmp;
        
        try {
      
            /*String PartyCode = getAttribute("PARTY_CODE").getString();
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
            */
            
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            //rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            /*rsResultSet.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsResultSet.updateString("ORDER_DATE", getAttribute("ORDER_DATE").getString());
            rsResultSet.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            rsResultSet.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            
            rsResultSet.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsResultSet.updateString("CONF_NO",getAttribute("CONF_NO").getString());
            
            rsResultSet.updateString("POSITION",getAttribute("POSITION").getString());
            rsResultSet.updateString("REF_NO",getAttribute("REF_NO").getString());
            rsResultSet.updateString("ORDER_CODE",getAttribute("ORDER_CDE").getString());
            rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            rsResultSet.updateDouble("INV_AMT",getAttribute("INV_AMT").getDouble());
            rsResultSet.updateDouble("BAS_AMT",getAttribute("BAS_AMT").getDouble());
            rsResultSet.updateDouble("WPSC",getAttribute("WPSC").getDouble());
            rsResultSet.updateDouble("OTHER",getAttribute("OTHER").getDouble());
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
            rsResultSet.updateString("ORDER_DDMM_D",getAttribute("ORD_DDMM_D").getString());
            */
            rsResultSet.updateString("PRIORITY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            
               
            
            
            
            MoveLast();
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
            }
            catch(Exception c){}
            //data.SetRollback();
            //data.SetAutoCommit(true);
            LastError= e.getMessage();
            return false;
        }
        
        
    }
    public boolean Updatep(){
        Statement stHistory,stHeader;
        ResultSet rsHistory,rsHeader;
        boolean Validate=true;
        
        try {
            
            String theDocNo=getAttribute("PARTY_CD").getString();
            
            //** Open History Table Connections **//
          //  stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
          //  rsHistory=stHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_ORDER_MASTER WHERE PARTY_CD='1'"); // '1' for restricting all data retrieval
          //  rsHistory.first();
            //** --------------------------------**//
            
           /* rsResultSet.updateString("PIECE_NO", getAttribute("PIECE_NO").getString());
            rsResultSet.updateString("ORDER_DATE", getAttribute("ORDER_DATE").getString());
            rsResultSet.updateString("PRODUCT_CODE",getAttribute("PRODUCT_CODE").getString());
            rsResultSet.updateString("PARTY_CD",getAttribute("PARTY_CD").getString());
            */
            /*rsResultSet.updateString("MACHINE_NO",getAttribute("MACHINE_NO").getString());
            rsResultSet.updateString("CONF_NO",getAttribute("CONF_NO").getString());
            
            rsResultSet.updateString("POSITION",getAttribute("POSITION").getString());
            rsResultSet.updateString("REF_NO",getAttribute("REF_NO").getString());
            rsResultSet.updateString("ORDER_CODE",getAttribute("ORDER_CODE").getString());
            rsResultSet.updateInt("PRIORITY", getAttribute("PRIORITY").getInt());
            //rsResultSet.updateString("PRIORITY",getAttribute("PRIORITY").getString());
            /*rsResultSet.updateString("LNGTH",getAttribute("LNGTH").getString());
            rsResultSet.updateString("WIDTH",getAttribute("WIDTH").getString());
            rsResultSet.updateString("WEIGHT",getAttribute("WEIGHT").getString());
             */
            //rsResultSet.updateString("GSQ",getAttribute("GSQ").getString());
            rsResultSet.updateString("PRIORITY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            /*rsResultSet.updateString("DELIV_DATE",getAttribute("DELIV_DATE").getString());
            rsResultSet.updateString("COMM_DATE",getAttribute("COMM_DATE").getString());
            rsResultSet.updateString("GRUP",getAttribute("GRUP").getString());
            rsResultSet.updateString("INV_NO",getAttribute("INV_NO").getString());
            rsResultSet.updateString("INV_DATE",getAttribute("INV_DATE").getString());
            rsResultSet.updateDouble("INV_AMOUNT",getAttribute("INV_AMOUNT").getDouble());
            rsResultSet.updateDouble("BAS_AMT",getAttribute("BAS_AMT").getDouble());
            rsResultSet.updateDouble("WPSC",getAttribute("WPSC").getDouble());
            rsResultSet.updateDouble("OTHER",getAttribute("OTHER").getDouble());
            rsResultSet.updateDouble("DISCOUNT",getAttribute("DISCOUNT").getDouble());
            rsResultSet.updateDouble("SEAM_CHG",getAttribute("SEAM_CHG").getDouble());
            rsResultSet.updateDouble("EXCISE",getAttribute("EXCISE").getDouble());
            rsResultSet.updateString("APPROX_AMOUNT",getAttribute("APPROX_AMOUNT").getString());
            rsResultSet.updateString("PROD_IND",getAttribute("PROD_IND").getString());
            rsResultSet.updateString("PROD_IND_A",getAttribute("PROD_IND_A").getString());
            rsResultSet.updateString("PROD_IND_B",getAttribute("PROD_IND_B").getString());
            rsResultSet.updateString("PROD_IND_C",getAttribute("PROD_IND_C").getString());
            rsResultSet.updateString("PROD_IND_D",getAttribute("PROD_IND_D").getString());
            rsResultSet.updateString("ORD_DDMM_A",getAttribute("ORD_DDMM_A").getString());
            rsResultSet.updateString("ORD_DDMM_B",getAttribute("ORD_DDMM_B").getString());
            rsResultSet.updateString("ORD_DDMM_C",getAttribute("ORD_DDMM_C").getString());
            rsResultSet.updateString("ORD_DDMM_D",getAttribute("ORD_DDMM_D").getString());
            */
            
            /*rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
             **/
            rsResultSet.updateRow();
                      
            return true;
       
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //data.SetRollback();
            }
            catch(Exception c){}
            //data.SetAutoCommit(true);
            
            LastError = e.getMessage();
            return false;
        }
    }
}
