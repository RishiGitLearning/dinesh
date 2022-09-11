/*
 * clsSales_Party.java
 *
 * Created on April 3, 2009, 10:22 AM
 */

package EITLERP;

import java.util.*;
import java.lang.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import EITLERP.Finance.*;

/**
 *
 * @author  root
 */
public class clsSales_Party {
    public String LastError="";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    private boolean HistoryView=false;
    
    public static int ModuleID=72;
    
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
    public clsSales_Party() {
        LastError = "";
        props=new HashMap();
        props.put("COMPANY_ID",new Variant(EITLERPGLOBAL.gCompanyID));
        props.put("PARTY_CODE",new Variant(""));
        props.put("OLD_PARTY_CODE",new Variant(""));
        props.put("PARTY_TYPE",new Variant(0));
        props.put("PARTY_NAME",new Variant(""));
        
        props.put("SEASON_CODE",new Variant(""));
        props.put("REG_DATE",new Variant(""));
        
        props.put("PARTY_NAME",new Variant(""));
        props.put("AREA_ID",new Variant(""));
        props.put("ADDRESS1",new Variant(""));
        props.put("ADDRESS2",new Variant(""));
        props.put("PINCODE",new Variant(""));
        props.put("CITY_ID",new Variant(""));
        props.put("CITY_NAME",new Variant(""));
        props.put("DISPATCH_STATION",new Variant("")); //STATION_NAME
        props.put("DISTRICT",new Variant(""));
        props.put("PHONE_NO",new Variant(""));
        props.put("MOBILE_NO",new Variant(""));
        props.put("MOBILE_NO_2",new Variant(""));
        props.put("MOBILE_NO_3",new Variant(""));
        props.put("CORR_ADDRESS",new Variant(""));
        props.put("COUNTRY_ID",new Variant(0)); //new code by vivek on 16/09/2013 to add state & country.
        props.put("STATE_ID",new Variant(0)); //new code by vivek on 16/09/2013 to add state & country.
        
        
        props.put("MARKET_SEGMENT",new Variant("")); //new code by vivek on 16/09/2013 to add state & country.
        props.put("PRODUCT_SEGMENT",new Variant("")); //new code by vivek on 16/09/2013 to add state & country.
        props.put("CLIENT_CATEGORY",new Variant("")); //new code by vivek on 16/09/2013 to add state & country.
        props.put("DESIGNER_INCHARGE",new Variant("")); //new code by vivek on 16/09/2013 to add state & country.
        props.put("INCHARGE_CODE",new Variant(0)); //new code by vivek on 16/09/2013 to add state & country.
        props.put("ZONE",new Variant("")); //new code by vivek on 16/09/2013 to add state & country.
              
        
        props.put("EMAIL",new Variant(""));
        props.put("EMAIL_ID2",new Variant(""));
        props.put("EMAIL_ID3",new Variant(""));        
        
        props.put("URL",new Variant(""));
        props.put("CONTACT_PERSON",new Variant(""));
        props.put("CONTACT_PERSON_2",new Variant(""));
        props.put("CONTACT_PERSON_3",new Variant(""));
        props.put("CONT_PERS_DESIGNATION",new Variant(""));
        props.put("CONT_PERS_DESIGNATION_2",new Variant(""));
        props.put("CONT_PERS_DESIGNATION_3",new Variant(""));
        props.put("BANK_ID",new Variant(0));
        props.put("BANK_NAME",new Variant(""));
        props.put("BANK_ADDRESS",new Variant(""));
        props.put("CREDIT_DAYS",new Variant(0));
        props.put("EXTRA_CREDIT_DAYS",new Variant(0));
        props.put("GRACE_CREDIT_DAYS",new Variant(0));
        props.put("BANK_CITY",new Variant(""));
        props.put("CHARGE_CODE",new Variant(""));
        props.put("CHARGE_CODE_II",new Variant(""));
        props.put("DOCUMENT_THROUGH",new Variant(""));
        props.put("TRANSPORTER_ID",new Variant(0));
        props.put("TRANSPORTER_NAME",new Variant(""));
        props.put("AMOUNT_LIMIT",new Variant(0));
        props.put("ECC_NO",new Variant(""));
        props.put("ECC_DATE",new Variant(""));
        props.put("PAN_NO",new Variant(""));
        props.put("PAN_DATE",new Variant(""));
        props.put("TIN_NO",new Variant(""));
        props.put("TIN_DATE",new Variant(""));
        props.put("CST_NO",new Variant(""));
        props.put("CST_DATE",new Variant(""));
        props.put("GSTIN_NO",new Variant(""));
        props.put("GSTIN_DATE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("CATEGORY",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        props.put("HIERARCHY_ID",new Variant(0));
        props.put("APPROVED",new Variant(false));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("REJECTED",new Variant(false));
        props.put("REJECTED_DATE",new Variant(""));
        props.put("CHANGED",new Variant(true));
        props.put("CHANGED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
        props.put("OTHER_BANK_ID",new Variant(0));
        props.put("OTHER_BANK_NAME",new Variant(""));
        props.put("OTHER_BANK_ADDRESS",new Variant(""));
        props.put("OTHER_BANK_CITY",new Variant(""));
        props.put("INSURANCE_CODE",new Variant(""));
        props.put("REMARKS",new Variant(""));
        props.put("DELAY_INTEREST_PER",new Variant(""));
        props.put("ACCOUNT_CODES",new Variant(""));
        props.put("DISTANCE_KM",new Variant(0));
        props.put("TAGGING_APPROVAL_IND", new Variant(0));
        props.put("PARTY_CLOSE_IND", new Variant(0));
        props.put("PO_NO_REQUIRED", new Variant(0));
        props.put("KEY_CLIENT_IND", new Variant(0));
        
        props.put("DELIVERY_MODE",new Variant(""));
        
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
            rsResultSet=Stmt.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER ORDER BY PARTY_CODE");
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
                RevNo=UtilFunctions.getInt(rsResultSet,"REVISION_NO",0);
                setAttribute("REVISION_NO",RevNo);
            }
            else {
                setAttribute("REVISION_NO",0);
            }
            
            setAttribute("COMPANY_ID",UtilFunctions.getInt(rsResultSet,"COMPANY_ID",0));
            setAttribute("PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE", ""));
            setAttribute("OLD_PARTY_CODE",UtilFunctions.getString(rsResultSet,"PARTY_CODE", ""));
            setAttribute("PARTY_TYPE",UtilFunctions.getInt(rsResultSet,"PARTY_TYPE",0));
            setAttribute("PARTY_NAME",UtilFunctions.getString(rsResultSet,"PARTY_NAME", ""));
            
            setAttribute("SEASON_CODE",UtilFunctions.getString(rsResultSet,"SEASON_CODE", ""));
            setAttribute("REG_DATE",UtilFunctions.getString(rsResultSet,"REG_DATE","0000-00-00"));
            
            setAttribute("AREA_ID",UtilFunctions.getString(rsResultSet,"AREA_ID", ""));
            setAttribute("ADDRESS1",UtilFunctions.getString(rsResultSet,"ADDRESS1", ""));
            setAttribute("ADDRESS2",UtilFunctions.getString(rsResultSet,"ADDRESS2", ""));
            setAttribute("CITY_ID",UtilFunctions.getString(rsResultSet,"CITY_ID", ""));
            setAttribute("CITY_NAME",UtilFunctions.getString(rsResultSet,"CITY_NAME", ""));
            setAttribute("DISPATCH_STATION",UtilFunctions.getString(rsResultSet,"DISPATCH_STATION", ""));
            setAttribute("DISTRICT",UtilFunctions.getString(rsResultSet,"DISTRICT", ""));
            setAttribute("PINCODE",UtilFunctions.getString(rsResultSet,"PINCODE", ""));
            setAttribute("PHONE_NO",UtilFunctions.getString(rsResultSet,"PHONE_NO", ""));
            setAttribute("MOBILE_NO",UtilFunctions.getString(rsResultSet,"MOBILE_NO", ""));
            setAttribute("MOBILE_NO_2",UtilFunctions.getString(rsResultSet,"MOBILE_NO_2", ""));
            setAttribute("MOBILE_NO_3",UtilFunctions.getString(rsResultSet,"MOBILE_NO_3", ""));
            setAttribute("CORR_ADDRESS",UtilFunctions.getString(rsResultSet,"CORR_ADDRESS", ""));
            setAttribute("STATE_ID",UtilFunctions.getInt(rsResultSet,"STATE_ID", 0)); //new code by vivek on 16/09/2013 to add state & country.
            setAttribute("COUNTRY_ID",UtilFunctions.getInt(rsResultSet,"COUNTRY_ID", 0)); //new code by vivek on 16/09/2013 to add state & country.
           
            setAttribute("MARKET_SEGMENT",UtilFunctions.getString(rsResultSet,"MARKET_SEGMENT", "")); //new code by Rishi on 01/03/2017.
            setAttribute("PRODUCT_SEGMENT",UtilFunctions.getString(rsResultSet,"PRODUCT_SEGMENT", "")); //new code by Rishi on 01/03/2017.
            setAttribute("CLIENT_CATEGORY",UtilFunctions.getString(rsResultSet,"CLIENT_CATEGORY", ""));//new code by Rishi on 01/03/2017.
            setAttribute("DESIGNER_INCHARGE",UtilFunctions.getString(rsResultSet,"DESIGNER_INCHARGE", ""));//new code by Rishi on 01/03/2017.
        
            setAttribute("INCHARGE_CD",UtilFunctions.getString(rsResultSet,"INCHARGE_CD", ""));//new code by Rishi on 01/03/2017.
            setAttribute("ZONE",UtilFunctions.getString(rsResultSet,"ZONE", ""));//new code by Rishi on 01/03/2017.
            
            String gMainCode = UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE", "");
            String gArea = UtilFunctions.getString(rsResultSet,"ZONE", "");
            if (gMainCode.equals("210010") && (gArea.equals("SOUTH") || gArea.equals("NORTH") || gArea.equals("EAST/WEST"))) {
                setAttribute("ZONE","OTHER");
            } else {
                setAttribute("ZONE",UtilFunctions.getString(rsResultSet,"ZONE", ""));
            }
            
            setAttribute("EMAIL",UtilFunctions.getString(rsResultSet,"EMAIL", ""));
            
            setAttribute("EMAIL_ID2",UtilFunctions.getString(rsResultSet,"EMAIL_ID2", "")); 
            setAttribute("EMAIL_ID3",UtilFunctions.getString(rsResultSet,"EMAIL_ID3", ""));
            
            setAttribute("URL",UtilFunctions.getString(rsResultSet,"URL", ""));
            setAttribute("CONTACT_PERSON",UtilFunctions.getString(rsResultSet,"CONTACT_PERSON", ""));
            setAttribute("CONTACT_PERSON_2",UtilFunctions.getString(rsResultSet,"CONTACT_PERSON_2", ""));
            setAttribute("CONTACT_PERSON_3",UtilFunctions.getString(rsResultSet,"CONTACT_PERSON_3", ""));
            setAttribute("CONT_PERS_DESIGNATION",UtilFunctions.getString(rsResultSet,"CONT_PERS_DESIGNATION", ""));
            setAttribute("CONT_PERS_DESIGNATION_2",UtilFunctions.getString(rsResultSet,"CONT_PERS_DESIGNATION_2", ""));
            setAttribute("CONT_PERS_DESIGNATION_3",UtilFunctions.getString(rsResultSet,"CONT_PERS_DESIGNATION_3", ""));            
            setAttribute("BANK_ID",UtilFunctions.getInt(rsResultSet,"BANK_ID",0));
            setAttribute("BANK_NAME",UtilFunctions.getString(rsResultSet,"BANK_NAME", ""));
            setAttribute("BANK_ADDRESS",UtilFunctions.getString(rsResultSet,"BANK_ADDRESS", ""));
            setAttribute("BANK_CITY",UtilFunctions.getString(rsResultSet,"BANK_CITY", ""));
            setAttribute("CST_NO",UtilFunctions.getString(rsResultSet,"CST_NO", ""));
            setAttribute("CST_DATE",UtilFunctions.getString(rsResultSet,"CST_DATE","0000-00-00"));
            setAttribute("GSTIN_NO",UtilFunctions.getString(rsResultSet,"GSTIN_NO", ""));
            setAttribute("GSTIN_DATE",UtilFunctions.getString(rsResultSet,"GSTIN_DATE","0000-00-00"));
            setAttribute("ECC_NO",UtilFunctions.getString(rsResultSet,"ECC_NO", ""));
            setAttribute("ECC_DATE",UtilFunctions.getString(rsResultSet,"ECC_DATE","0000-00-00"));
            setAttribute("TIN_NO",UtilFunctions.getString(rsResultSet,"TIN_NO", ""));
            setAttribute("TIN_DATE",UtilFunctions.getString(rsResultSet,"TIN_DATE","0000-00-00"));
            setAttribute("PAN_NO",UtilFunctions.getString(rsResultSet,"PAN_NO", ""));
            setAttribute("PAN_DATE",UtilFunctions.getString(rsResultSet,"PAN_DATE","0000-00-00"));
            setAttribute("MAIN_ACCOUNT_CODE",UtilFunctions.getString(rsResultSet,"MAIN_ACCOUNT_CODE", ""));
            setAttribute("CATEGORY",UtilFunctions.getString(rsResultSet,"CATEGORY",""));
            setAttribute("CHARGE_CODE",UtilFunctions.getString(rsResultSet,"CHARGE_CODE", ""));
            setAttribute("CHARGE_CODE_II",UtilFunctions.getString(rsResultSet,"CHARGE_CODE_II", ""));
            setAttribute("CREDIT_DAYS",UtilFunctions.getDouble(rsResultSet,"CREDIT_DAYS", 0));
            setAttribute("EXTRA_CREDIT_DAYS",UtilFunctions.getDouble(rsResultSet,"EXTRA_CREDIT_DAYS", 0));
            setAttribute("GRACE_CREDIT_DAYS",UtilFunctions.getDouble(rsResultSet,"GRACE_CREDIT_DAYS", 0));
            setAttribute("DOCUMENT_THROUGH",UtilFunctions.getString(rsResultSet,"DOCUMENT_THROUGH", ""));
            setAttribute("TRANSPORTER_ID",UtilFunctions.getInt(rsResultSet,"TRANSPORTER_ID",0));
            setAttribute("TRANSPORTER_NAME",UtilFunctions.getString(rsResultSet,"TRANSPORTER_NAME", ""));
            setAttribute("AMOUNT_LIMIT",UtilFunctions.getDouble(rsResultSet,"AMOUNT_LIMIT", 0));
            setAttribute("OTHER_BANK_ID",UtilFunctions.getInt(rsResultSet,"OTHER_BANK_ID",0));
            setAttribute("OTHER_BANK_NAME",UtilFunctions.getString(rsResultSet,"OTHER_BANK_NAME", ""));
            setAttribute("OTHER_BANK_ADDRESS",UtilFunctions.getString(rsResultSet,"OTHER_BANK_ADDRESS", ""));
            setAttribute("OTHER_BANK_CITY",UtilFunctions.getString(rsResultSet,"OTHER_BANK_CITY", ""));
            setAttribute("INSURANCE_CODE",UtilFunctions.getString(rsResultSet,"INSURANCE_CODE", ""));
            setAttribute("REMARKS",UtilFunctions.getString(rsResultSet,"REMARKS", ""));
            setAttribute("DELAY_INTEREST_PER",UtilFunctions.getDouble(rsResultSet,"DELAY_INTEREST_PER", 0));
            setAttribute("ACCOUNT_CODES",UtilFunctions.getString(rsResultSet,"ACCOUNT_CODES", ""));
            setAttribute("DO_NOT_ALLOW_INTEREST",UtilFunctions.getInt(rsResultSet,"DO_NOT_ALLOW_INTEREST", 0));
            setAttribute("CRITICAL_LIMIT_UNCHECK",UtilFunctions.getInt(rsResultSet,"CRITICAL_LIMIT_UNCHECK", 0));
            setAttribute("CRITICAL_LIMIT_UNCHECK",UtilFunctions.getInt(rsResultSet,"CRITICAL_LIMIT_UNCHECK", 0));
            
            setAttribute("DISTANCE_KM",UtilFunctions.getInt(rsResultSet,"DISTANCE_KM",0));
            setAttribute("TAGGING_APPROVAL_IND", UtilFunctions.getInt(rsResultSet, "TAGGING_APPROVAL_IND", 0));
            
            setAttribute("PO_NO_REQUIRED",UtilFunctions.getInt(rsResultSet,"PO_NO_REQUIRED", 0));
            setAttribute("KEY_CLIENT_IND",UtilFunctions.getInt(rsResultSet,"KEY_CLIENT_IND", 0));
            
            setAttribute("DELIVERY_MODE",UtilFunctions.getString(rsResultSet,"DELIVERY_MODE", ""));
            
            setAttribute("CREATED_BY",UtilFunctions.getString(rsResultSet,"CREATED_BY", ""));
            setAttribute("CREATED_DATE",UtilFunctions.getString(rsResultSet,"CREATED_DATE","0000-00-00"));
            setAttribute("MODIFIED_BY",UtilFunctions.getString(rsResultSet,"MODIFIED_BY", ""));
            setAttribute("MODIFIED_DATE",UtilFunctions.getString(rsResultSet,"MODIFIED_DATE","0000-00-00"));
            
            setAttribute("HIERARCHY_ID",UtilFunctions.getInt(rsResultSet,"HIERARCHY_ID",0));
            
            setAttribute("APPROVED",UtilFunctions.getInt(rsResultSet,"APPROVED",0));
            setAttribute("CANCELLED",UtilFunctions.getInt(rsResultSet,"CANCELLED",0));
            setAttribute("APPROVED_DATE",UtilFunctions.getString(rsResultSet,"APPROVED_DATE","0000-00-00"));
            setAttribute("REJECTED",UtilFunctions.getBoolean(rsResultSet,"REJECTED",false));
            setAttribute("REJECTED_DATE",UtilFunctions.getString(rsResultSet,"REJECTED_DATE","0000-00-00"));
            setAttribute("REJECTED_REMARKS",UtilFunctions.getString(rsResultSet,"REJECTED_REMARKS", ""));
            
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
    public boolean IsEditable(int CompanyID,String PartyCode,String MainCode,int UserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL="";
        
        try {
            
            if(HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL="SELECT PARTY_CODE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND (APPROVED=1)";
            
            if(data.IsRecordExist(strSQL)) {
                return false;
            }
            else {
                strSQL="SELECT * FROM D_COM_DOC_DATA WHERE COMPANY_ID="+CompanyID+" AND MODULE_ID="+ModuleID+" AND DOC_NO='"+PartyCode+"' AND USER_ID="+UserID+" AND STATUS='W'";
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
    
    public static String getTransporterName(long pCompanyID, long pTranID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String TranName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT TRANSPORTER_NAME FROM D_SAL_TRANSPORTER_MASTER WHERE COMPANY_ID="+pCompanyID+" AND TRANSPORTER_ID="+pTranID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TranName=rsTmp.getString("TRANSPORTER_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return TranName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
        public static String getFeltInchargeName(long pTranID) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String TranName="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE WHERE INCHARGE_CD ="+pTranID);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                TranName=rsTmp.getString("INCHARGE_NAME");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return TranName;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static String getChargeCode(long pCompanyID, String pPartyCode,String MainCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode="";
        
        try {
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CHARGE_CODE FROM D_FIN_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ChargeCode=rsTmp.getString("CHARGE_CODE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return ChargeCode;
        }
        catch(Exception e) {
            return "";
        }
    }

public static String getChargeCode(long pCompanyID, String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String ChargeCode="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CHARGE_CODE FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"'");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                ChargeCode=rsTmp.getString("CHARGE_CODE");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return ChargeCode;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    public static HashMap getHistoryList(int CompanyID,String PartyCode) {
        HashMap List=new HashMap();
        ResultSet rsTmp;
        
        try {
            String strSQL="SELECT * FROM D_SAL_PARTY_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+PartyCode+"'";
            rsTmp=data.getResult(strSQL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsSales_Party objParty=new clsSales_Party();
                    
                    objParty.setAttribute("PARTY_CODE",UtilFunctions.getString(rsTmp,"PARTY_CODE", ""));
                    objParty.setAttribute("REVISION_NO",UtilFunctions.getInt(rsTmp,"REVISION_NO", 0));
                    objParty.setAttribute("UPDATED_BY",UtilFunctions.getString(rsTmp,"UPDATED_BY", ""));
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
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //------------------------------------//
            
            String PartyCode = getAttribute("PARTY_CODE").getString();
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
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsResultSet.updateString("PARENT_PARTY_CODE", " ");
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsResultSet.updateString("SEASON_CODE",getAttribute("SEASON_CODE").getString());
            rsResultSet.updateString("REG_DATE",getAttribute("REG_DATE").getString());
            
            rsResultSet.updateString("AREA_ID",getAttribute("AREA_ID").getString());
            rsResultSet.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsResultSet.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsResultSet.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            rsResultSet.updateString("CITY_NAME",getAttribute("CITY_NAME").getString());
            rsResultSet.updateString("DISPATCH_STATION",getAttribute("DISPATCH_STATION").getString());
            rsResultSet.updateString("DISTRICT",getAttribute("DISTRICT").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            rsResultSet.updateString("MOBILE_NO",getAttribute("MOBILE_NO").getString());
            rsResultSet.updateString("MOBILE_NO_2",getAttribute("MOBILE_NO_2").getString());
            rsResultSet.updateString("MOBILE_NO_3",getAttribute("MOBILE_NO_3").getString());
            rsResultSet.updateString("CORR_ADDRESS",getAttribute("CORR_ADDRESS").getString());
            rsResultSet.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateString("EMAIL",getAttribute("EMAIL").getString());
            rsResultSet.updateString("EMAIL_ID2",getAttribute("EMAIL_ID2").getString());
            rsResultSet.updateString("EMAIL_ID3",getAttribute("EMAIL_ID3").getString());
            
            
            rsResultSet.updateString("MARKET_SEGMENT",getAttribute("MARKET_SEGMENT").getString());
            rsResultSet.updateString("PRODUCT_SEGMENT",getAttribute("PRODUCT_SEGMENT").getString());
            rsResultSet.updateString("CLIENT_CATEGORY",getAttribute("CLIENT_CATEGORY").getString());
            rsResultSet.updateString("DESIGNER_INCHARGE",getAttribute("DESIGNER_INCHARGE").getString());
            rsResultSet.updateString("INCHARGE_CD",getAttribute("INCHARGE_CD").getString());
            rsResultSet.updateString("ZONE",getAttribute("ZONE").getString());
       
            
            
            
            
            rsResultSet.updateString("URL",getAttribute("URL").getString());
            rsResultSet.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            rsResultSet.updateString("CONTACT_PERSON_2",getAttribute("CONTACT_PERSON_2").getString());
            rsResultSet.updateString("CONTACT_PERSON_3",getAttribute("CONTACT_PERSON_3").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION",getAttribute("CONT_PERS_DESIGNATION").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_2",getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_3",getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsResultSet.updateInt("BANK_ID",getAttribute("BANK_ID").getInt());
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsResultSet.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsResultSet.updateString("CHARGE_CODE",getAttribute("CHARGE_CODE").getString());
            rsResultSet.updateString("CHARGE_CODE_II",getAttribute("CHARGE_CODE_II").getString());
            rsResultSet.updateDouble("CREDIT_DAYS",getAttribute("CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("EXTRA_CREDIT_DAYS",getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("GRACE_CREDIT_DAYS",getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsResultSet.updateString("DOCUMENT_THROUGH",getAttribute("DOCUMENT_THROUGH").getString());
            rsResultSet.updateInt("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getInt());
            rsResultSet.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsResultSet.updateDouble("AMOUNT_LIMIT",getAttribute("AMOUNT_LIMIT").getDouble());
            rsResultSet.updateString("CST_NO",getAttribute("CST_NO").getString());
            rsResultSet.updateString("CST_DATE",getAttribute("CST_DATE").getString());
            rsResultSet.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            rsResultSet.updateString("GSTIN_DATE",getAttribute("GSTIN_DATE").getString());
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
            rsResultSet.updateInt("CRITICAL_LIMIT_UNCHECK",getAttribute("CRITICAL_LIMIT_UNCHECK").getInt());
            
            rsResultSet.updateString("ACCOUNT_CODES",getAttribute("ACCOUNT_CODES").getString());
            
            rsResultSet.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsResultSet.updateString("DELIVERY_MODE",getAttribute("DELIVERY_MODE").getString());
            
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
            rsResultSet.updateInt("TAGGING_APPROVAL_IND",getAttribute("TAGGING_APPROVAL_IND").getInt());
            rsResultSet.updateInt("PO_NO_REQUIRED",getAttribute("PO_NO_REQUIRED").getInt());
            rsResultSet.updateInt("PO_NO_REQUIRED",getAttribute("KEY_CLIENT_IND").getInt());            
            rsResultSet.updateInt("PARTY_CLOSE_IND",0);
            rsResultSet.insertRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",1); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsHistory.updateString("PARENT_PARTY_CODE", " ");
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsHistory.updateString("SEASON_CODE",getAttribute("SEASON_CODE").getString());
            rsHistory.updateString("REG_DATE",getAttribute("REG_DATE").getString());
            
            rsHistory.updateString("AREA_ID",getAttribute("AREA_ID").getString());
            rsHistory.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsHistory.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsHistory.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            rsHistory.updateString("CITY_NAME",getAttribute("CITY_NAME").getString());
            rsHistory.updateString("DISPATCH_STATION",getAttribute("DISPATCH_STATION").getString());
            rsHistory.updateString("DISTRICT",getAttribute("DISTRICT").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            rsHistory.updateString("MOBILE_NO",getAttribute("MOBILE_NO").getString());
            rsHistory.updateString("MOBILE_NO_2",getAttribute("MOBILE_NO_2").getString());
            rsHistory.updateString("MOBILE_NO_3",getAttribute("MOBILE_NO_3").getString());
            rsHistory.updateString("CORR_ADDRESS",getAttribute("CORR_ADDRESS").getString());
            rsHistory.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsHistory.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
           
            rsHistory.updateString("MARKET_SEGMENT",getAttribute("MARKET_SEGMENT").getString());
            rsHistory.updateString("PRODUCT_SEGMENT",getAttribute("PRODUCT_SEGMENT").getString());
            rsHistory.updateString("CLIENT_CATEGORY",getAttribute("CLIENT_CATEGORY").getString());
            rsHistory.updateString("DESIGNER_INCHARGE",getAttribute("DESIGNER_INCHARGE").getString());
            rsHistory.updateString("INCHARGE_CD",getAttribute("INCHARGE_CD").getString());
            rsHistory.updateString("ZONE",getAttribute("ZONE").getString());
          
            
            
            rsHistory.updateString("EMAIL",getAttribute("EMAIL").getString());
            rsHistory.updateString("EMAIL_ID2",getAttribute("EMAIL_ID2").getString());
            rsHistory.updateString("EMAIL_ID3",getAttribute("EMAIL_ID3").getString());
            rsHistory.updateString("URL",getAttribute("URL").getString());
            rsHistory.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            rsHistory.updateString("CONTACT_PERSON_2",getAttribute("CONTACT_PERSON_2").getString());
            rsHistory.updateString("CONTACT_PERSON_3",getAttribute("CONTACT_PERSON_3").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION",getAttribute("CONT_PERS_DESIGNATION").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_2",getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_3",getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsHistory.updateInt("BANK_ID",getAttribute("BANK_ID").getInt());
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsHistory.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsHistory.updateString("CHARGE_CODE",getAttribute("CHARGE_CODE").getString());
            rsHistory.updateString("CHARGE_CODE_II",getAttribute("CHARGE_CODE_II").getString());
            rsHistory.updateDouble("CREDIT_DAYS",getAttribute("CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("EXTRA_CREDIT_DAYS",getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("GRACE_CREDIT_DAYS",getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsHistory.updateString("DOCUMENT_THROUGH",getAttribute("DOCUMENT_THROUGH").getString());
            rsHistory.updateInt("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getInt());
            rsHistory.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsHistory.updateDouble("AMOUNT_LIMIT",getAttribute("AMOUNT_LIMIT").getDouble());
            rsHistory.updateString("CST_NO",getAttribute("CST_NO").getString());
            rsHistory.updateString("CST_DATE",getAttribute("CST_DATE").getString());
            rsHistory.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            rsHistory.updateString("GSTIN_DATE",getAttribute("GSTIN_DATE").getString());
            rsHistory.updateString("ECC_NO",getAttribute("ECC_NO").getString());
            rsHistory.updateString("ECC_DATE",getAttribute("ECC_DATE").getString());
            rsHistory.updateString("TIN_NO",getAttribute("TIN_NO").getString());
            rsHistory.updateString("TIN_DATE",getAttribute("TIN_DATE").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("OTHER_BANK_ID",getAttribute("OTHER_BANK_ID").getInt());
            rsHistory.updateString("OTHER_BANK_NAME",getAttribute("OTHER_BANK_NAME").getString());
            rsHistory.updateString("OTHER_BANK_ADDRESS",getAttribute("OTHER_BANK_ADDRESS").getString());
            rsHistory.updateString("OTHER_BANK_CITY",getAttribute("OTHER_BANK_CITY").getString());
            rsHistory.updateString("CATEGORY",getAttribute("CATEGORY").getString());
            rsHistory.updateString("INSURANCE_CODE",getAttribute("INSURANCE_CODE").getString());
            rsHistory.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsHistory.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateDouble("DELAY_INTEREST_PER",getAttribute("DELAY_INTEREST_PER").getDouble());
            rsHistory.updateString("ACCOUNT_CODES",getAttribute("ACCOUNT_CODES").getString());
            rsHistory.updateInt("DO_NOT_ALLOW_INTEREST",getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
            rsHistory.updateInt("CRITICAL_LIMIT_UNCHECK",getAttribute("CRITICAL_LIMIT_UNCHECK").getInt());
            
            rsHistory.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsHistory.updateString("DELIVERY_MODE",getAttribute("DELIVERY_MODE").getString());
            
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateInt("TAGGING_APPROVAL_IND",getAttribute("TAGGING_APPROVAL_IND").getInt());   
            rsHistory.updateInt("PO_NO_REQUIRED",getAttribute("PO_NO_REQUIRED").getInt());            
            rsHistory.updateInt("PO_NO_REQUIRED",getAttribute("KEY_CLIENT_IND").getInt());            
            rsHistory.insertRow();
            
            //===================== Update the Approval Flow ======================//
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.IsCreator=true;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="PARTY_CODE";
            
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
            
            
            if(ObjFlow.Status.equals("F")) {
                String MainAccCode=getAttribute("ACCOUNT_CODES").getString();
                AddPartyToFinance((String)getAttribute("PARTY_CODE").getObj(),MainAccCode,getAttribute("MAIN_ACCOUNT_CODE").getString());
            }
            //================= Approval Flow Update complete ===================//
            
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
    
    public boolean Filter(String Condition) {
        Ready=false;
        
        try {
            String strSQL = "SELECT * FROM D_SAL_PARTY_MASTER " + Condition ;
            Conn=data.getConn();
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsResultSet=Stmt.executeQuery(strSQL);
            
            if(!rsResultSet.first()) {
                strSQL = "SELECT * FROM D_SAL_PARTY_MASTER ORDER BY PARTY_CODE ";
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
            
            String theDocNo=getAttribute("PARTY_CODE").getString();
            
            //** Open History Table Connections **//
            stHistory=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsHistory=stHistory.executeQuery("SELECT * FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            //** --------------------------------**//
            
            rsResultSet.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsResultSet.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsResultSet.updateString("PARENT_PARTY_CODE", " ");
            rsResultSet.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsResultSet.updateString("SEASON_CODE",getAttribute("SEASON_CODE").getString());
            rsResultSet.updateString("REG_DATE",getAttribute("REG_DATE").getString());
            
            rsResultSet.updateString("AREA_ID",getAttribute("AREA_ID").getString());
            rsResultSet.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsResultSet.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsResultSet.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            rsResultSet.updateString("CITY_NAME",getAttribute("CITY_NAME").getString());
            rsResultSet.updateString("DISPATCH_STATION",getAttribute("DISPATCH_STATION").getString());
            rsResultSet.updateString("DISTRICT",getAttribute("DISTRICT").getString());
            rsResultSet.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsResultSet.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            rsResultSet.updateString("MOBILE_NO",getAttribute("MOBILE_NO").getString());
            rsResultSet.updateString("MOBILE_NO_2",getAttribute("MOBILE_NO_2").getString());
            rsResultSet.updateString("MOBILE_NO_3",getAttribute("MOBILE_NO_3").getString());
            rsResultSet.updateString("CORR_ADDRESS",getAttribute("CORR_ADDRESS").getString());
            rsResultSet.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsResultSet.updateString("MARKET_SEGMENT",getAttribute("MARKET_SEGMENT").getString());
            rsResultSet.updateString("PRODUCT_SEGMENT",getAttribute("PRODUCT_SEGMENT").getString());
            rsResultSet.updateString("CLIENT_CATEGORY",getAttribute("CLIENT_CATEGORY").getString());
            rsResultSet.updateString("DESIGNER_INCHARGE",getAttribute("DESIGNER_INCHARGE").getString());
            rsResultSet.updateString("INCHARGE_CD",getAttribute("INCHARGE_CD").getString());
            rsResultSet.updateString("ZONE",getAttribute("ZONE").getString());
            rsResultSet.updateString("EMAIL",getAttribute("EMAIL").getString());
            rsResultSet.updateString("EMAIL_ID2",getAttribute("EMAIL_ID2").getString());
            rsResultSet.updateString("EMAIL_ID3",getAttribute("EMAIL_ID3").getString());
            
            rsResultSet.updateString("URL",getAttribute("URL").getString());
            rsResultSet.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            rsResultSet.updateString("CONTACT_PERSON_2",getAttribute("CONTACT_PERSON_2").getString());
            rsResultSet.updateString("CONTACT_PERSON_3",getAttribute("CONTACT_PERSON_3").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION",getAttribute("CONT_PERS_DESIGNATION").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_2",getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsResultSet.updateString("CONT_PERS_DESIGNATION_3",getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsResultSet.updateInt("BANK_ID",getAttribute("BANK_ID").getInt());
            rsResultSet.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsResultSet.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsResultSet.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsResultSet.updateString("CHARGE_CODE",getAttribute("CHARGE_CODE").getString());
            rsResultSet.updateString("CHARGE_CODE_II",getAttribute("CHARGE_CODE_II").getString());
            rsResultSet.updateDouble("CREDIT_DAYS",getAttribute("CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("EXTRA_CREDIT_DAYS",getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsResultSet.updateDouble("GRACE_CREDIT_DAYS",getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsResultSet.updateString("DOCUMENT_THROUGH",getAttribute("DOCUMENT_THROUGH").getString());
            rsResultSet.updateInt("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getInt());
            rsResultSet.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsResultSet.updateDouble("AMOUNT_LIMIT",getAttribute("AMOUNT_LIMIT").getDouble());
            rsResultSet.updateString("CST_NO",getAttribute("CST_NO").getString());
            rsResultSet.updateString("CST_DATE",getAttribute("CST_DATE").getString());
            rsResultSet.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            rsResultSet.updateString("GSTIN_DATE",getAttribute("GSTIN_DATE").getString());
            rsResultSet.updateString("ECC_NO",getAttribute("ECC_NO").getString());
            rsResultSet.updateString("ECC_DATE",getAttribute("ECC_DATE").getString());
            rsResultSet.updateString("TIN_NO",getAttribute("TIN_NO").getString());
            rsResultSet.updateString("TIN_DATE",getAttribute("TIN_DATE").getString());
            rsResultSet.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsResultSet.updateInt("OTHER_BANK_ID",getAttribute("OTHER_BANK_ID").getInt());
            rsResultSet.updateString("OTHER_BANK_NAME",getAttribute("OTHER_BANK_NAME").getString());
            rsResultSet.updateString("OTHER_BANK_ADDRESS",getAttribute("OTHER_BANK_ADDRESS").getString());
            rsResultSet.updateString("OTHER_BANK_CITY",getAttribute("OTHER_BANK_CITY").getString());
            rsResultSet.updateString("CATEGORY",getAttribute("CATEGORY").getString());
            rsResultSet.updateString("INSURANCE_CODE",getAttribute("INSURANCE_CODE").getString());
            rsResultSet.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsResultSet.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());
            rsResultSet.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsResultSet.updateDouble("DELAY_INTEREST_PER",getAttribute("DELAY_INTEREST_PER").getDouble());
            rsResultSet.updateString("ACCOUNT_CODES",getAttribute("ACCOUNT_CODES").getString());
            
            rsResultSet.updateInt("DO_NOT_ALLOW_INTEREST",getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
            rsResultSet.updateInt("CRITICAL_LIMIT_UNCHECK",getAttribute("CRITICAL_LIMIT_UNCHECK").getInt());
            
            rsResultSet.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsResultSet.updateString("DELIVERY_MODE",getAttribute("DELIVERY_MODE").getString());
            
            rsResultSet.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsResultSet.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsResultSet.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsResultSet.updateBoolean("CANCELLED",false);
            rsResultSet.updateBoolean("CHANGED",true);
            rsResultSet.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("REJECTED",false);
            rsResultSet.updateString("REJECTED_DATE","0000-00-00");
            rsResultSet.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsResultSet.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());
            rsResultSet.updateInt("PO_NO_REQUIRED",getAttribute("PO_NO_REQUIRED").getInt());
            rsResultSet.updateInt("PO_NO_REQUIRED",getAttribute("KEY_CLIENT_IND").getInt());            
            rsResultSet.updateRow();
            
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo=EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_SAL_PARTY_MASTER_H WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+(String)getAttribute("PARTY_CODE").getObj()+"'");
            RevNo++;
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO",RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateString("UPDATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("APPROVAL_STATUS",getAttribute("APPROVAL_STATUS").getString());
            rsHistory.updateString("ENTRY_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateInt("COMPANY_ID", getAttribute("COMPANY_ID").getInt());
            rsHistory.updateString("PARTY_CODE", getAttribute("PARTY_CODE").getString());
            rsHistory.updateString("PARENT_PARTY_CODE", " ");
            rsHistory.updateInt("PARTY_TYPE", getAttribute("PARTY_TYPE").getInt());
            rsHistory.updateString("PARTY_NAME",getAttribute("PARTY_NAME").getString());
            
            rsHistory.updateString("SEASON_CODE",getAttribute("SEASON_CODE").getString());
            rsHistory.updateString("REG_DATE",getAttribute("REG_DATE").getString());
            
            rsHistory.updateString("AREA_ID",getAttribute("AREA_ID").getString());
            rsHistory.updateString("ADDRESS1",getAttribute("ADDRESS1").getString());
            rsHistory.updateString("ADDRESS2",getAttribute("ADDRESS2").getString());
            rsHistory.updateString("CITY_ID",getAttribute("CITY_ID").getString());
            rsHistory.updateString("CITY_NAME",getAttribute("CITY_NAME").getString());
            rsHistory.updateString("DISPATCH_STATION",getAttribute("DISPATCH_STATION").getString());
            rsHistory.updateString("DISTRICT",getAttribute("DISTRICT").getString());
            rsHistory.updateString("PINCODE",getAttribute("PINCODE").getString());
            rsHistory.updateString("PHONE_NO",getAttribute("PHONE_NO").getString());
            rsHistory.updateString("MOBILE_NO",getAttribute("MOBILE_NO").getString());
            rsHistory.updateString("MOBILE_NO_2",getAttribute("MOBILE_NO_2").getString());
            rsHistory.updateString("MOBILE_NO_3",getAttribute("MOBILE_NO_3").getString());
            rsHistory.updateString("CORR_ADDRESS",getAttribute("CORR_ADDRESS").getString());
            rsHistory.updateInt("STATE_ID", getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsHistory.updateInt("COUNTRY_ID", getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.

            rsHistory.updateString("MARKET_SEGMENT",getAttribute("MARKET_SEGMENT").getString());
            rsHistory.updateString("PRODUCT_SEGMENT",getAttribute("PRODUCT_SEGMENT").getString());
            rsHistory.updateString("CLIENT_CATEGORY",getAttribute("CLIENT_CATEGORY").getString());
            rsHistory.updateString("DESIGNER_INCHARGE",getAttribute("DESIGNER_INCHARGE").getString());
            rsHistory.updateString("INCHARGE_CD",getAttribute("INCHARGE_CD").getString());
            rsHistory.updateString("ZONE",getAttribute("ZONE").getString());
       
            
            
            rsHistory.updateString("EMAIL",getAttribute("EMAIL").getString());
            rsHistory.updateString("EMAIL_ID2",getAttribute("EMAIL_ID2").getString());
            rsHistory.updateString("EMAIL_ID3",getAttribute("EMAIL_ID3").getString());
            
            rsHistory.updateString("URL",getAttribute("URL").getString());
            rsHistory.updateString("CONTACT_PERSON",getAttribute("CONTACT_PERSON").getString());
            rsHistory.updateString("CONTACT_PERSON_2",getAttribute("CONTACT_PERSON_2").getString());
            rsHistory.updateString("CONTACT_PERSON_3",getAttribute("CONTACT_PERSON_3").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION",getAttribute("CONT_PERS_DESIGNATION").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_2",getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsHistory.updateString("CONT_PERS_DESIGNATION_3",getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsHistory.updateInt("BANK_ID",getAttribute("BANK_ID").getInt());
            rsHistory.updateString("BANK_NAME",getAttribute("BANK_NAME").getString());
            rsHistory.updateString("BANK_ADDRESS",getAttribute("BANK_ADDRESS").getString());
            rsHistory.updateString("BANK_CITY",getAttribute("BANK_CITY").getString());
            rsHistory.updateString("CHARGE_CODE",getAttribute("CHARGE_CODE").getString());
            rsHistory.updateString("CHARGE_CODE_II",getAttribute("CHARGE_CODE_II").getString());
            rsHistory.updateDouble("CREDIT_DAYS",getAttribute("CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("EXTRA_CREDIT_DAYS",getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsHistory.updateDouble("GRACE_CREDIT_DAYS",getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsHistory.updateString("DOCUMENT_THROUGH",getAttribute("DOCUMENT_THROUGH").getString());
            rsHistory.updateInt("TRANSPORTER_ID",getAttribute("TRANSPORTER_ID").getInt());
            rsHistory.updateString("TRANSPORTER_NAME",getAttribute("TRANSPORTER_NAME").getString());
            rsHistory.updateDouble("AMOUNT_LIMIT",getAttribute("AMOUNT_LIMIT").getDouble());
            rsHistory.updateString("CST_NO",getAttribute("CST_NO").getString());
            rsHistory.updateString("CST_DATE",getAttribute("CST_DATE").getString());
            rsHistory.updateString("GSTIN_NO",getAttribute("GSTIN_NO").getString());
            rsHistory.updateString("GSTIN_DATE",getAttribute("GSTIN_DATE").getString());
            rsHistory.updateString("ECC_NO",getAttribute("ECC_NO").getString());
            rsHistory.updateString("ECC_DATE",getAttribute("ECC_DATE").getString());
            rsHistory.updateString("TIN_NO",getAttribute("TIN_NO").getString());
            rsHistory.updateString("TIN_DATE",getAttribute("TIN_DATE").getString());
            rsHistory.updateString("MAIN_ACCOUNT_CODE",getAttribute("MAIN_ACCOUNT_CODE").getString());
            rsHistory.updateInt("OTHER_BANK_ID",getAttribute("OTHER_BANK_ID").getInt());
            rsHistory.updateString("OTHER_BANK_NAME",getAttribute("OTHER_BANK_NAME").getString());
            rsHistory.updateString("OTHER_BANK_ADDRESS",getAttribute("OTHER_BANK_ADDRESS").getString());
            rsHistory.updateString("OTHER_BANK_CITY",getAttribute("OTHER_BANK_CITY").getString());
            rsHistory.updateString("CATEGORY",getAttribute("CATEGORY").getString());
            rsHistory.updateString("INSURANCE_CODE",getAttribute("INSURANCE_CODE").getString());
            rsHistory.updateString("PAN_NO",getAttribute("PAN_NO").getString());
            rsHistory.updateString("PAN_DATE",getAttribute("PAN_DATE").getString());
            rsHistory.updateString("REMARKS",getAttribute("REMARKS").getString());
            rsHistory.updateDouble("DELAY_INTEREST_PER",getAttribute("DELAY_INTEREST_PER").getDouble());
            rsHistory.updateString("ACCOUNT_CODES",getAttribute("ACCOUNT_CODES").getString());
            
            rsHistory.updateInt("DO_NOT_ALLOW_INTEREST",getAttribute("DO_NOT_ALLOW_INTEREST").getInt());
            rsHistory.updateInt("CRITICAL_LIMIT_UNCHECK",getAttribute("CRITICAL_LIMIT_UNCHECK").getInt());
            
            rsHistory.updateInt("DISTANCE_KM",getAttribute("DISTANCE_KM").getInt());
            
            rsHistory.updateString("DELIVERY_MODE",getAttribute("DELIVERY_MODE").getString());
            
            rsHistory.updateString("CREATED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("CREATED_DATE",getAttribute("CREATED_DATE").getString());
            rsHistory.updateString("MODIFIED_BY",EITLERPGLOBAL.gLoginID);
            rsHistory.updateString("MODIFIED_DATE",getAttribute("MODIFIED_DATE").getString());
            rsHistory.updateInt("HIERARCHY_ID",getAttribute("HIERARCHY_ID").getInt());
            rsHistory.updateBoolean("APPROVED",false);
            rsHistory.updateString("APPROVED_DATE","0000-00-00");
            rsHistory.updateBoolean("CANCELLED",false);
            rsHistory.updateBoolean("CHANGED",true);
            rsHistory.updateString("CHANGED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("REJECTED",false);
            rsHistory.updateString("REJECTED_DATE","0000-00-00");
            rsHistory.updateString("REJECTED_REMARKS",getAttribute("REJECTED_REMARKS").getString());
            rsHistory.updateInt("TAGGING_APPROVAL_IND", getAttribute("TAGGING_APPROVAL_IND").getInt());
            rsHistory.updateInt("PO_NO_REQUIRED",getAttribute("PO_NO_REQUIRED").getInt());
            rsHistory.updateInt("PO_NO_REQUIRED",getAttribute("KEY_CLIENT_IND").getInt());            
            rsHistory.insertRow();
            
            
            //======== Update the Approval Flow =========
            setAttribute("FROM",EITLERPGLOBAL.gUserID);
            
            ApprovalFlow ObjFlow=new ApprovalFlow();
            ObjFlow.CompanyID=(int)getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID=clsSales_Party.ModuleID;
            ObjFlow.DocNo=(String)getAttribute("PARTY_CODE").getObj();
            ObjFlow.DocDate=EITLERPGLOBAL.getCurrentDateDB();
            ObjFlow.From=(int)getAttribute("FROM").getVal();
            ObjFlow.To=(int)getAttribute("TO").getVal();
            ObjFlow.Status=(String)getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName="D_SAL_PARTY_MASTER";
            ObjFlow.IsCreator=false;
            ObjFlow.HierarchyID=(int)getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks=(String)getAttribute("REJECTED_REMARKS").getObj();
            ObjFlow.FieldName="PARTY_CODE";
            String qry = "UPDATE D_COM_DOC_DATA SET DOC_NO='"+getAttribute("PARTY_CODE").getString()+"' WHERE DOC_NO='"+getAttribute("OLD_PARTY_CODE").getString()+"' AND MODULE_ID="+clsSales_Party.ModuleID;
            data.Execute(qry);
            
            //==== Handling Rejected Documents ==========//
            String AStatus=(String)getAttribute("APPROVAL_STATUS").getObj();
            
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
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected=getAttribute("REJECTED").getBool();
            
            if(IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_SAL_PARTY_MASTER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+ObjFlow.DocNo+"' ");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsSales_Party.ModuleID+" AND DOC_NO='"+ObjFlow.DocNo+"'");
                
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
            
            
            if(ObjFlow.Status.equals("F")) {
                String MainAccCode=getAttribute("ACCOUNT_CODES").getString();
                AddPartyToFinance((String)getAttribute("PARTY_CODE").getObj(),MainAccCode,getAttribute("MAIN_ACCOUNT_CODE").getString());
            }
            
            return true;
        }
        catch(Exception e) {
            try {
                //Conn.rollback();
                //data.SetRollback();
            }
            catch(Exception c){}
            //data.SetAutoCommit(true);
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
                rsFinParty.updateString("GSTIN_NO",ObjParty.getAttribute("GSTIN_NO").getString());
                rsFinParty.updateString("GSTIN_DATE",ObjParty.getAttribute("GSTIN_DATE").getString());
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
                //rsFinParty.updateString("STATE",ObjParty.getAttribute("DISTRICT").getString()); //commented by vivek on 16/09/2013 to add state & country.
                //rsFinParty.updateString("COUNTRY",""); //commented by vivek on 16/09/2013 to add state & country.
                rsFinParty.updateString("STATE", getStateName(ObjParty.getAttribute("STATE_ID").getInt())); //new code by vivek on 16/09/2013 to add state & country.
                rsFinParty.updateString("COUNTRY", getCountryName(ObjParty.getAttribute("COUNTRY_ID").getInt())); //new code by vivek on 16/09/2013 to add state & country.
                rsFinParty.updateString("PHONE",ObjParty.getAttribute("PHONE_NO").getString());
                rsFinParty.updateString("FAX","");
                rsFinParty.updateString("MOBILE",ObjParty.getAttribute("MOBILE_NO").getString());
                rsFinParty.updateString("EMAIL",ObjParty.getAttribute("EMAIL").getString());
                rsFinParty.updateString("URL",ObjParty.getAttribute("URL").getString());
                rsFinParty.updateInt("DISTANCE_KM",ObjParty.getAttribute("DISTANCE_KM").getInt()); //new code by vivek on 16/09/2013 to add state & country.
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
                rsFinParty.updateInt("KEY_CLIENT_IND",ObjParty.getAttribute("KEY_CLIENT_IND").getInt());
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
            String PartyCode=getAttribute("PARTY_CODE").getString();
            
            if(CanDelete(EITLERPGLOBAL.gCompanyID,PartyCode)) {
                String strSQL = "DELETE FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"'";
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
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' AND MODULE_ID="+clsSales_Party.ModuleID);
                }*/
                
                data.Execute("UPDATE D_SAL_PARTY_MASTER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' ");
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+pCompanyID+" AND DOC_NO='"+pPartyCode+"' ");
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
                strSQL="SELECT D_SAL_PARTY_MASTER.PARTY_CODE,D_SAL_PARTY_MASTER.MAIN_ACCOUNT_CODE,D_SAL_PARTY_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSales_Party.ModuleID+" AND D_SAL_PARTY_MASTER.APPROVED=0 AND D_SAL_PARTY_MASTER.CANCELLED=0 ORDER BY D_COM_DOC_DATA.RECEIVED_DATE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocDate) {
                strSQL="SELECT D_SAL_PARTY_MASTER.PARTY_CODE,D_SAL_PARTY_MASTER.MAIN_ACCOUNT_CODE,D_SAL_PARTY_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSales_Party.ModuleID+" AND D_SAL_PARTY_MASTER.APPROVED=0 AND D_SAL_PARTY_MASTER.CANCELLED=0 ORDER BY D_SAL_PARTY_MASTER.PARTY_CODE";
            }
            
            if(pOrder==EITLERPGLOBAL.OnDocNo) {
                strSQL="SELECT D_SAL_PARTY_MASTER.PARTY_CODE,D_SAL_PARTY_MASTER.MAIN_ACCOUNT_CODE,D_SAL_PARTY_MASTER.PARTY_NAME,RECEIVED_DATE,0 AS DEPT_ID FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+Integer.toString(pCompanyID)+" AND D_COM_DOC_DATA.USER_ID="+Integer.toString(pUserID)+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsSales_Party.ModuleID+" AND D_SAL_PARTY_MASTER.APPROVED=0 AND D_SAL_PARTY_MASTER.CANCELLED=0 ORDER BY D_SAL_PARTY_MASTER.PARTY_CODE";
            }
            
            tmpStmt=tmpConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=tmpStmt.executeQuery(strSQL);
            
            rsTmp.first();
            Counter=0;
            while(!rsTmp.isAfterLast()&&rsTmp.getRow()>0) {
                Counter=Counter+1;
                clsSales_Party ObjItem=new clsSales_Party();
                
                //------------- Header Fields --------------------//
                
                ObjItem.setAttribute("PARTY_CODE",rsTmp.getString("PARTY_CODE"));
                ObjItem.setAttribute("PARTY_NAME",rsTmp.getString("PARTY_NAME"));
                ObjItem.setAttribute("RECEIVED_DATE",rsTmp.getString("RECEIVED_DATE"));
                ObjItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
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
    
    
    public static String getParaext1(int pCompanyID,String pURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String Paraext1="";
        
        try {
            tmpConn=data.getConn(pURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT PARA_EXT1 FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'CLIENT_CATEGORY' AND COMPANY_ID =2 ORDER BY PARA_CODE");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                Paraext1=rsTmp.getString("PARA_EXT1");
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return Paraext1;
        }
        catch(Exception e) {
            return "";
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

public static String getCity(int pCompanyID,String pPartyCode) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        String City="";
        
        try {
            tmpConn=data.getConn();
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT CITY_ID FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+pCompanyID+" AND PARTY_CODE='"+pPartyCode+"' ");
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                City=rsTmp.getString("CITY_ID");
            }            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();            
            return City;
        }
        catch(Exception e) {
            e.printStackTrace();
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
                ObjParty.setAttribute("MOBILE_NO_2",rsTmp.getString("MOBILE_NO_2"));
                ObjParty.setAttribute("MOBILE_NO_3",rsTmp.getString("MOBILE_NO_3"));
                ObjParty.setAttribute("CORR_ADDRESS",rsTmp.getString("CORR_ADDRESS"));
                ObjParty.setAttribute("STATE_ID",rsTmp.getInt("STATE_ID")); //new code by vivek on 16/09/2013 to add state & country.
                ObjParty.setAttribute("COUNTRY_ID",rsTmp.getInt("COUNTRY_ID")); //new code by vivek on 16/09/2013 to add state & country.
                
                ObjParty.setAttribute("MARKET_SEGMENT",rsTmp.getString("MARKET_SEGMENT"));
                ObjParty.setAttribute("PRODUCT_SEGMENT",rsTmp.getString("PRODUCT_SEGMENT"));
                ObjParty.setAttribute("CLIENT_CATEGORY",rsTmp.getString("CLIENT_CATEGORY"));
                ObjParty.setAttribute("DESIGNER_INCHARGE",rsTmp.getString("DESIGNER_INCHARGE"));
                ObjParty.setAttribute("INCHARGE_CD",rsTmp.getString("INCHARGE_CD"));
                ObjParty.setAttribute("ZONE",rsTmp.getString("ZONE"));
                
                ObjParty.setAttribute("EMAIL",rsTmp.getString("EMAIL"));
                ObjParty.setAttribute("EMAIL_ID2",rsTmp.getString("EMAIL_ID2"));
                ObjParty.setAttribute("EMAIL_ID3",rsTmp.getString("EMAIL_ID3"));                
                
                ObjParty.setAttribute("URL",rsTmp.getString("URL"));
                ObjParty.setAttribute("CONTACT_PERSON",rsTmp.getString("CONTACT_PERSON"));
                ObjParty.setAttribute("CONTACT_PERSON_2",rsTmp.getString("CONTACT_PERSON_2"));
                ObjParty.setAttribute("CONTACT_PERSON_3",rsTmp.getString("CONTACT_PERSON_3"));
                ObjParty.setAttribute("CONT_PERS_DESIGNATION",rsTmp.getString("CONT_PERS_DESIGNATION"));
                ObjParty.setAttribute("CONT_PERS_DESIGNATION_2",rsTmp.getString("CONT_PERS_DESIGNATION_2"));
                ObjParty.setAttribute("CONT_PERS_DESIGNATION_3",rsTmp.getString("CONT_PERS_DESIGNATION_3"));
                ObjParty.setAttribute("BANK_ID",rsTmp.getLong("BANK_ID"));
                ObjParty.setAttribute("BANK_NAME",rsTmp.getString("BANK_NAME"));
                ObjParty.setAttribute("BANK_ADDRESS",rsTmp.getString("BANK_ADDRESS"));
                ObjParty.setAttribute("BANK_CITY",rsTmp.getString("BANK_CITY"));
                ObjParty.setAttribute("CST_NO",rsTmp.getString("CST_NO"));
                ObjParty.setAttribute("CST_DATE",rsTmp.getString("CST_DATE"));
                ObjParty.setAttribute("GSTIN_NO",rsTmp.getString("GSTIN_NO"));
                ObjParty.setAttribute("GSTIN_DATE",rsTmp.getString("GSTIN_DATE"));
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
                ObjParty.setAttribute("EXTRA_CREDIT_DAYS",rsTmp.getDouble("EXTRA_CREDIT_DAYS"));
                ObjParty.setAttribute("GRACE_CREDIT_DAYS",rsTmp.getDouble("GRACE_CREDIT_DAYS"));
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
                
                ObjParty.setAttribute("DISTANCE_KM",rsTmp.getInt("DISTANCE_KM"));
                
                ObjParty.setAttribute("DELIVERY_MODE",rsTmp.getString("DELIVERY_MODE"));
                
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
            rsTmp.updateString("MOBILE_NO_2",tmpParty.getAttribute("MOBILE_NO_2").getString());
            rsTmp.updateString("MOBILE_NO_3",tmpParty.getAttribute("MOBILE_NO_3").getString());
            rsTmp.updateString("CORR_ADDRESS",tmpParty.getAttribute("CORR_ADDRESS").getString());
            rsTmp.updateInt("STATE_ID", tmpParty.getAttribute("STATE_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsTmp.updateInt("COUNTRY_ID", tmpParty.getAttribute("COUNTRY_ID").getInt()); //new code by vivek on 16/09/2013 to add state & country.
            rsTmp.updateString("MARKET_SEGMENT",tmpParty.getAttribute("MARKET_SEGMENT").getString());
            rsTmp.updateString("PRODUCT_SEGMENT",tmpParty.getAttribute("PRODUCT_SEGMENT").getString());
            rsTmp.updateString("CLIENT_CATEGORY",tmpParty.getAttribute("CLIENT_CATEGORY").getString());
            rsTmp.updateString("DESIGNER_INCHARGE",tmpParty.getAttribute("DESIGNER_INCHARGE").getString());
            rsTmp.updateString("INCHARGE_CD",tmpParty.getAttribute("INCHARGE_CD").getString());
            rsTmp.updateString("ZONE",tmpParty.getAttribute("ZONE").getString());
            rsTmp.updateString("EMAIL",tmpParty.getAttribute("EMAIL").getString());
            rsTmp.updateString("URL",tmpParty.getAttribute("URL").getString());
            rsTmp.updateString("CONTACT_PERSON",tmpParty.getAttribute("CONTACT_PERSON").getString());
            rsTmp.updateString("CONTACT_PERSON_2",tmpParty.getAttribute("CONTACT_PERSON_2").getString());
            rsTmp.updateString("CONTACT_PERSON_3",tmpParty.getAttribute("CONTACT_PERSON_3").getString());
            rsTmp.updateString("CONT_PERS_DESIGNATION",tmpParty.getAttribute("CONT_PERS_DESIGNATION").getString());
            rsTmp.updateString("CONT_PERS_DESIGNATION_2",tmpParty.getAttribute("CONT_PERS_DESIGNATION_2").getString());
            rsTmp.updateString("CONT_PERS_DESIGNATION_3",tmpParty.getAttribute("CONT_PERS_DESIGNATION_3").getString());
            rsTmp.updateInt("BANK_ID",tmpParty.getAttribute("BANK_ID").getInt());
            rsTmp.updateString("BANK_NAME",tmpParty.getAttribute("BANK_NAME").getString());
            rsTmp.updateString("BANK_ADDRESS",tmpParty.getAttribute("BANK_ADDRESS").getString());
            rsTmp.updateString("BANK_CITY",tmpParty.getAttribute("BANK_CITY").getString());
            rsTmp.updateString("CHARGE_CODE",tmpParty.getAttribute("CHARGE_CODE").getString());
            rsTmp.updateString("CHARGE_CODE_II",tmpParty.getAttribute("CHARGE_CODE_II").getString());
            rsTmp.updateDouble("CREDIT_DAYS",tmpParty.getAttribute("CREDIT_DAYS").getDouble());
            rsTmp.updateDouble("EXTRA_CREDIT_DAYS",tmpParty.getAttribute("EXTRA_CREDIT_DAYS").getDouble());
            rsTmp.updateDouble("GRACE_CREDIT_DAYS",tmpParty.getAttribute("GRACE_CREDIT_DAYS").getDouble());
            rsTmp.updateString("DOCUMENT_THROUGH",tmpParty.getAttribute("DOCUMENT_THROUGH").getString());
            rsTmp.updateInt("TRANSPORTER_ID",tmpParty.getAttribute("TRANSPORTER_ID").getInt());
            rsTmp.updateString("TRANSPORTER_NAME",tmpParty.getAttribute("TRANSPORTER_NAME").getString());
            rsTmp.updateDouble("AMOUNT_LIMIT",tmpParty.getAttribute("AMOUNT_LIMIT").getDouble());
            rsTmp.updateString("CST_NO",tmpParty.getAttribute("CST_NO").getString());
            rsTmp.updateString("CST_DATE",tmpParty.getAttribute("CST_DATE").getString());
            rsTmp.updateString("GSTIN_NO",tmpParty.getAttribute("GSTIN_NO").getString());
            rsTmp.updateString("GSTIN_DATE",tmpParty.getAttribute("GSTIN_DATE").getString());
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
            
            rsTmp.updateInt("DISTANCE_KM",tmpParty.getAttribute("DISTANCE_KM").getInt());
            
            
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
            data.Execute("DELETE FROM DINESHMILLS.D_COM_DOC_DATA WHERE DOC_NO='"+PartyCode+"' AND MODULE_ID='"+clsSales_Party.ModuleID+"' ");
            data.Execute("DELETE FROM DINESHMILLS.D_SAL_PARTY_MASTER_H WHERE PARTY_CODE='"+PartyCode+"' ");
            data.Execute("DELETE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCode+"' ");
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
    
    //new code by vivek on 16/09/2013 to add state & country. start
    // it creates list of Country Name
    public static HashMap getCountryNameList(){
        HashMap countryList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ComboData cData=new ComboData();
            cData.Code=0;
            cData.Text="COUNTRY";
            countryList.put(new Integer(counter++), cData);
            
            ResultSet rsTmp=stTmp.executeQuery("SELECT COUNTRY_ID, COUNTRY_NAME FROM D_SAL_COUNTRY_MASTER");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("COUNTRY_ID");
                aData.Text=rsTmp.getString("COUNTRY_NAME");
                countryList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return countryList;
    }
    
    // it creates list of State Name
    public static HashMap getStateNameList(){
        HashMap stateList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ComboData cData=new ComboData();
            cData.Code=0;
            cData.Text="STATE";
            stateList.put(new Integer(counter++), cData);
            
            ResultSet rsTmp=stTmp.executeQuery("SELECT STATE_ID, STATE_NAME FROM D_SAL_STATE_MASTER WHERE COUNTRY_ID=1");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("STATE_ID");
                aData.Text=rsTmp.getString("STATE_NAME");
                stateList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return stateList;
    }
    
    public HashMap getInchargeNameList(){
        HashMap hmInchargeNameList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT INCHARGE_CD, INCHARGE_NAME FROM PRODUCTION.FELT_INCHARGE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("INCHARGE_CD");
                aData.Text=rsTmp.getString("INCHARGE_NAME");
                hmInchargeNameList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            LastError = e.getMessage();
            e.printStackTrace();
        }
        return hmInchargeNameList;
    }
    
    
      public HashMap getClientCategoryList(){
        HashMap hmClientCategoryList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
              ResultSet rsTmp=stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'CLIENT_CATEGORY' AND COMPANY_ID =2 ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                hmClientCategoryList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            LastError = e.getMessage();
            e.printStackTrace();
        }
        return hmClientCategoryList;
    }
    
    
        public HashMap getMarketSegmentList(){
        HashMap hmMarketSegmentList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'MARKET_SEGMENT' AND COMPANY_ID =2 ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                hmMarketSegmentList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            LastError = e.getMessage();
            e.printStackTrace();
        }
        return hmMarketSegmentList;
    }
         
        public HashMap getProductSegmentList(){
        HashMap hmProductSegmentList= new HashMap();
        try{
            Connection Conn=data.getConn();
            Statement stTmp=Conn.createStatement();
            int counter=1;
            ResultSet rsTmp=stTmp.executeQuery("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID = 'PRODUCT_SEGMENT' AND COMPANY_ID =2 ORDER BY PARA_CODE");
            while(rsTmp.next()){
                ComboData aData=new ComboData();
                aData.Code=rsTmp.getInt("PARA_CODE");
                aData.Text=rsTmp.getString("PARA_DESC");
                hmProductSegmentList.put(new Integer(counter++), aData);
            }
        }catch(SQLException e){
            LastError = e.getMessage();
            e.printStackTrace();
        }
        return hmProductSegmentList;
    }
    
    public static String getStateName(int stateId){
        return data.getStringValueFromDB("SELECT STATE_NAME FROM D_SAL_STATE_MASTER WHERE STATE_ID="+stateId);
    }
    
    public static String getCountryName(int countryId){
        return data.getStringValueFromDB("SELECT COUNTRY_NAME FROM D_SAL_COUNTRY_MASTER WHERE COUNTRY_ID="+countryId);
    }
    //new code by vivek on 16/09/2013 to add state & country. end    
}