/*
 * clsTravelExp.java
 *
 * Created on July 29, 2009, 2:27 PM
 */

package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

/**
 *
 * @author  Prathmesh
 * @version
 */

public class clsTravelExp {
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colVoucherDetailDocs=new HashMap();
    
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
    
    /** Creates new clsMRItem */
    public clsTravelExp() {
        props=new HashMap();
        props.put("SR_NO",new Variant(0));
        props.put("COMPANY_ID",new Variant(0));
        props.put("VOUCHER_NO",new Variant(""));
        props.put("VOUCHER_DATE",new Variant(""));
        props.put("EMPLOYEE_NO",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        props.put("APPROVED",new Variant(0));
        props.put("APPROVED_DATE",new Variant(""));
        props.put("CANCELLED",new Variant(0));
    }
    
    public static boolean Insert(String TVoucherNo,String EmployeeNo) {
        try{
            String SQL = "SELECT EMPLOYEE_NO FROM D_FIN_EMPLOYEE_TRAVEL_EXP WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO='"+TVoucherNo+"' AND EMPLOYEE_NO='"+EmployeeNo+"' ";
            if(!data.IsRecordExist(SQL,FinanceGlobal.FinURL)) {
                Statement stExp;
                Connection Conn;
                Conn = data.getConn(FinanceGlobal.FinURL);
                stExp=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsExp = stExp.executeQuery("SELECT * FROM D_FIN_EMPLOYEE_TRAVEL_EXP WHERE VOUCHER_NO=1");
                long SrNo = data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_FIN_EMPLOYEE_TRAVEL_EXP", "SR_NO", FinanceGlobal.FinURL);
                rsExp.moveToInsertRow();
                rsExp.updateLong("SR_NO", SrNo);
                rsExp.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                rsExp.updateString("VOUCHER_NO", TVoucherNo);
                rsExp.updateString("VOUCHER_DATE", data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+TVoucherNo+"' ",FinanceGlobal.FinURL));
                rsExp.updateString("EMPLOYEE_NO", EmployeeNo);
                rsExp.updateBoolean("APPROVED", false);
                rsExp.updateString("APPROVED_DATE", "0000-00-00");
                rsExp.updateBoolean("CANCELLED", false);
                rsExp.insertRow();
                
                String str = "SELECT * FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+TVoucherNo+"' AND APPROVED=1 AND AND CANCELLED=0";
                if(data.IsRecordExist(str,FinanceGlobal.FinURL)) {
                    data.Execute("UPDATE D_FIN_EMPLOYEE_TRAVEL_EXP SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO='"+TVoucherNo+"' AND EMPLOYEE_NO='"+EmployeeNo+"' ",FinanceGlobal.FinURL);
                }
            } else {
                data.Execute("UPDATE D_FIN_EMPLOYEE_TRAVEL_EXP SET APPROVED=1, APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"' WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND VOUCHER_NO='"+TVoucherNo+"' AND EMPLOYEE_NO='"+EmployeeNo+"' ",FinanceGlobal.FinURL);
            }
        } catch(Exception e) {
        }
        return true;
    }
    
}