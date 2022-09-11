 /*
  * clsNoDataObject.java
  *
  * Created on April 6, 2004, 9:32 AM
  */
package EITLERP.Stores;

import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.Finance.*;
import EITLERP.Purchase.*;
import EITLERP.Sales.clsSalesInvoice;

public class clsGRNGen {
    
    public static int ModuleID = 7;
    public String LastError = "";
    private ResultSet rsResultSet;
    private Connection Conn;
    private Statement Stmt;
    
    private HashMap props;
    public boolean Ready = false;
    
    public HashMap colGRNItems = new HashMap();
    public HashMap colHSNGRNPJVItems = new HashMap();
    
    //History Related properties
    private boolean HistoryView = false;
    
    //Flag Indicating whether user has entered the document no.
    public boolean UserDocNo = false;
    
    public String RJNNo = "";
    public String NRGPNo = "";
    
    public int chkPostVoucher = 1;
                    
    
    public boolean RJNCreated = false;
    public boolean NRGPCreated = false;
    
    public String ErrorMessages = "";
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName, Object Value) {
        props.put(PropName, new Variant(Value));
    }
    
    public void setAttribute(String PropName, int Value) {
        props.put(PropName, new Variant(Value));
    }
    
    public void setAttribute(String PropName, long Value) {
        props.put(PropName, new Variant(Value));
    }
    
    public void setAttribute(String PropName, double Value) {
        props.put(PropName, new Variant(Value));
    }
    
    public void setAttribute(String PropName, float Value) {
        props.put(PropName, new Variant(Value));
    }
    
    public void setAttribute(String PropName, boolean Value) {
        props.put(PropName, new Variant(Value));
    }
    
    /**
     * Creates new clsBusinessObject
     */
    public clsGRNGen() {
        props = new HashMap();
        props.put("COMPANY_ID", new Variant(0));
        props.put("GRN_NO", new Variant(""));
        props.put("GRN_DATE", new Variant(""));
        props.put("GRN_TYPE", new Variant(1)); // 1 - General Material
        props.put("SUPP_ID", new Variant(""));
        props.put("PARTY_NAME", new Variant(""));
        props.put("CHALAN_NO", new Variant(""));
        props.put("CHALAN_DATE", new Variant(""));
        props.put("LR_NO", new Variant(""));
        props.put("LR_DATE", new Variant(""));
        props.put("INVOICE_NO", new Variant(""));
        props.put("INVOICE_DATE", new Variant(""));
        props.put("TRANSPORTER", new Variant(0));
        props.put("TRANSPORTER_NAME", new Variant(""));
        props.put("GATEPASS_NO", new Variant(""));
        props.put("IMPROT_CONCESS", new Variant(false));
        props.put("ACCESSABLE_VALUE", new Variant(0));
        props.put("CURRENCY_ID", new Variant(0));
        props.put("CURRENCY_RATE", new Variant(0));
        props.put("CURRENCY_RATE_PAYMENT", new Variant(0));
        props.put("FOR_STORE", new Variant(0));
        props.put("TOTAL_AMOUNT", new Variant(0));
        props.put("GROSS_AMOUNT", new Variant(0));
        props.put("GRN_PENDING", new Variant(false));
        props.put("GRN_PENDING_REASON", new Variant(0));
        props.put("OPEN_STATUS", new Variant(""));
        props.put("APPROVED", new Variant(false));
        props.put("APPROVED_DATE", new Variant(""));
        props.put("APPROVED_ON", new Variant(""));
        props.put("REJECTED", new Variant(false));
        props.put("REJECTED_DATE", new Variant(""));
        props.put("HIERARCHY_ID", new Variant(0));
        props.put("CENVATED_ITEMS", new Variant(false));
        props.put("CANCELLED", new Variant(false));
        props.put("CREATED_BY", new Variant(0));
        props.put("CREATED_DATE", new Variant(""));
        props.put("MODIFIED_BY", new Variant(0));
        props.put("MODIFIED_DATE", new Variant(""));
        props.put("COLUMN_1_ID", new Variant(0));
        props.put("COLUMN_1_FORMULA", new Variant(""));
        props.put("COLUMN_1_PER", new Variant(0));
        props.put("COLUMN_1_AMT", new Variant(0));
        props.put("COLUMN_1_CAPTION", new Variant(""));
        props.put("COLUMN_2_ID", new Variant(0));
        props.put("COLUMN_2_FORMULA", new Variant(""));
        props.put("COLUMN_2_PER", new Variant(0));
        props.put("COLUMN_2_AMT", new Variant(0));
        props.put("COLUMN_2_CAPTION", new Variant(""));
        props.put("COLUMN_3_ID", new Variant(0));
        props.put("COLUMN_3_FORMULA", new Variant(""));
        props.put("COLUMN_3_PER", new Variant(0));
        props.put("COLUMN_3_AMT", new Variant(0));
        props.put("COLUMN_3_CAPTION", new Variant(""));
        props.put("COLUMN_4_ID", new Variant(0));
        props.put("COLUMN_4_FORMULA", new Variant(""));
        props.put("COLUMN_4_PER", new Variant(0));
        props.put("COLUMN_4_AMT", new Variant(0));
        props.put("COLUMN_4_CAPTION", new Variant(""));
        props.put("COLUMN_5_ID", new Variant(0));
        props.put("COLUMN_5_FORMULA", new Variant(""));
        props.put("COLUMN_5_PER", new Variant(0));
        props.put("COLUMN_5_AMT", new Variant(0));
        props.put("COLUMN_5_CAPTION", new Variant(""));
        props.put("COLUMN_6_ID", new Variant(0));
        props.put("COLUMN_6_FORMULA", new Variant(""));
        props.put("COLUMN_6_PER", new Variant(0));
        props.put("COLUMN_6_AMT", new Variant(0));
        props.put("COLUMN_6_CAPTION", new Variant(""));
        props.put("COLUMN_7_ID", new Variant(0));
        props.put("COLUMN_7_FORMULA", new Variant(""));
        props.put("COLUMN_7_PER", new Variant(0));
        props.put("COLUMN_7_AMT", new Variant(0));
        props.put("COLUMN_7_CAPTION", new Variant(""));
        props.put("COLUMN_8_ID", new Variant(0));
        props.put("COLUMN_8_FORMULA", new Variant(""));
        props.put("COLUMN_8_PER", new Variant(0));
        props.put("COLUMN_8_AMT", new Variant(0));
        props.put("COLUMN_8_CAPTION", new Variant(""));
        props.put("COLUMN_9_ID", new Variant(0));
        props.put("COLUMN_9_FORMULA", new Variant(""));
        props.put("COLUMN_9_PER", new Variant(0));
        props.put("COLUMN_9_AMT", new Variant(0));
        props.put("COLUMN_9_CAPTION", new Variant(""));
        props.put("COLUMN_10_ID", new Variant(0));
        props.put("COLUMN_10_FORMULA", new Variant(""));
        props.put("COLUMN_10_PER", new Variant(0));
        props.put("COLUMN_10_AMT", new Variant(0));
        props.put("COLUMN_10_CAPTION", new Variant(""));
        props.put("REMARKS", new Variant(""));
        props.put("PREFIX", new Variant("")); //For Autonumber generation
        
        props.put("PF_POST", new Variant(0));
        props.put("FREIGHT_POST", new Variant(0));
        props.put("OCTROI_POST", new Variant(0));
        props.put("INSURANCE_POST", new Variant(0));
        props.put("CLEARANCE_POST", new Variant(0));
        props.put("AIR_FREIGHT_POST", new Variant(0));
        props.put("OTHERS_POST", new Variant(0));
        
        // -- Approval Specific Fields --
        props.put("FROM", new Variant(0));
        props.put("TO", new Variant(0));
        props.put("FROM_REMARKS", new Variant(""));
        props.put("APPROVAL_STATUS", new Variant(""));
        
        props.put("COLUMN_11_ID", new Variant(0));
        props.put("COLUMN_11_FORMULA", new Variant(""));
        props.put("COLUMN_11_PER", new Variant(0));
        props.put("COLUMN_11_AMT", new Variant(0));
        props.put("COLUMN_11_CAPTION", new Variant(""));
        props.put("COLUMN_12_ID", new Variant(0));
        props.put("COLUMN_12_FORMULA", new Variant(""));
        props.put("COLUMN_12_PER", new Variant(0));
        props.put("COLUMN_12_AMT", new Variant(0));
        props.put("COLUMN_12_CAPTION", new Variant(""));
        props.put("COLUMN_13_ID", new Variant(0));
        props.put("COLUMN_13_FORMULA", new Variant(""));
        props.put("COLUMN_13_PER", new Variant(0));
        props.put("COLUMN_13_AMT", new Variant(0));
        props.put("COLUMN_13_CAPTION", new Variant(""));
        props.put("COLUMN_14_ID", new Variant(0));
        props.put("COLUMN_14_FORMULA", new Variant(""));
        props.put("COLUMN_14_PER", new Variant(0));
        props.put("COLUMN_14_AMT", new Variant(0));
        props.put("COLUMN_14_CAPTION", new Variant(""));
        props.put("COLUMN_15_ID", new Variant(0));
        props.put("COLUMN_15_FORMULA", new Variant(""));
        props.put("COLUMN_15_PER", new Variant(0));
        props.put("COLUMN_15_AMT", new Variant(0));
        props.put("COLUMN_15_CAPTION", new Variant(""));
        props.put("COLUMN_16_ID", new Variant(0));
        props.put("COLUMN_16_FORMULA", new Variant(""));
        props.put("COLUMN_16_PER", new Variant(0));
        props.put("COLUMN_16_AMT", new Variant(0));
        props.put("COLUMN_16_CAPTION", new Variant(""));
        props.put("COLUMN_17_ID", new Variant(0));
        props.put("COLUMN_17_FORMULA", new Variant(""));
        props.put("COLUMN_17_PER", new Variant(0));
        props.put("COLUMN_17_AMT", new Variant(0));
        props.put("COLUMN_17_CAPTION", new Variant(""));
        props.put("COLUMN_18_ID", new Variant(0));
        props.put("COLUMN_18_FORMULA", new Variant(""));
        props.put("COLUMN_18_PER", new Variant(0));
        props.put("COLUMN_18_AMT", new Variant(0));
        props.put("COLUMN_18_CAPTION", new Variant(""));
        props.put("COLUMN_19_ID", new Variant(0));
        props.put("COLUMN_19_FORMULA", new Variant(""));
        props.put("COLUMN_19_PER", new Variant(0));
        props.put("COLUMN_19_AMT", new Variant(0));
        props.put("COLUMN_19_CAPTION", new Variant(""));
        props.put("COLUMN_20_ID", new Variant(0));
        props.put("COLUMN_20_FORMULA", new Variant(""));
        props.put("COLUMN_20_PER", new Variant(0));
        props.put("COLUMN_20_AMT", new Variant(0));
        props.put("COLUMN_20_CAPTION", new Variant(""));
        
        props.put("COLUMN_21_ID", new Variant(0));
        props.put("COLUMN_21_FORMULA", new Variant(""));
        props.put("COLUMN_21_PER", new Variant(0));
        props.put("COLUMN_21_AMT", new Variant(0));
        props.put("COLUMN_21_CAPTION", new Variant(""));
        props.put("COLUMN_22_ID", new Variant(0));
        props.put("COLUMN_22_FORMULA", new Variant(""));
        props.put("COLUMN_22_PER", new Variant(0));
        props.put("COLUMN_22_AMT", new Variant(0));
        props.put("COLUMN_22_CAPTION", new Variant(""));
        props.put("COLUMN_23_ID", new Variant(0));
        props.put("COLUMN_23_FORMULA", new Variant(""));
        props.put("COLUMN_23_PER", new Variant(0));
        props.put("COLUMN_23_AMT", new Variant(0));
        props.put("COLUMN_23_CAPTION", new Variant(""));
        props.put("COLUMN_24_ID", new Variant(0));
        props.put("COLUMN_24_FORMULA", new Variant(""));
        props.put("COLUMN_24_PER", new Variant(0));
        props.put("COLUMN_24_AMT", new Variant(0));
        props.put("COLUMN_24_CAPTION", new Variant(""));
        props.put("COLUMN_25_ID", new Variant(0));
        props.put("COLUMN_25_FORMULA", new Variant(""));
        props.put("COLUMN_25_PER", new Variant(0));
        props.put("COLUMN_25_AMT", new Variant(0));
        props.put("COLUMN_25_CAPTION", new Variant(""));
        props.put("COLUMN_26_ID", new Variant(0));
        props.put("COLUMN_26_FORMULA", new Variant(""));
        props.put("COLUMN_26_PER", new Variant(0));
        props.put("COLUMN_26_AMT", new Variant(0));
        props.put("COLUMN_26_CAPTION", new Variant(""));
        props.put("COLUMN_27_ID", new Variant(0));
        props.put("COLUMN_27_FORMULA", new Variant(""));
        props.put("COLUMN_27_PER", new Variant(0));
        props.put("COLUMN_27_AMT", new Variant(0));
        props.put("COLUMN_27_CAPTION", new Variant(""));
        props.put("COLUMN_28_ID", new Variant(0));
        props.put("COLUMN_28_FORMULA", new Variant(""));
        props.put("COLUMN_28_PER", new Variant(0));
        props.put("COLUMN_28_AMT", new Variant(0));
        props.put("COLUMN_28_CAPTION", new Variant(""));
        props.put("COLUMN_29_ID", new Variant(0));
        props.put("COLUMN_29_FORMULA", new Variant(""));
        props.put("COLUMN_29_PER", new Variant(0));
        props.put("COLUMN_29_AMT", new Variant(0));
        props.put("COLUMN_29_CAPTION", new Variant(""));
        props.put("COLUMN_30_ID", new Variant(0));
        props.put("COLUMN_30_FORMULA", new Variant(""));
        props.put("COLUMN_30_PER", new Variant(0));
        props.put("COLUMN_30_AMT", new Variant(0));
        props.put("COLUMN_30_CAPTION", new Variant(""));
        
        props.put("COLUMN_31_ID", new Variant(0));
        props.put("COLUMN_31_FORMULA", new Variant(""));
        props.put("COLUMN_31_PER", new Variant(0));
        props.put("COLUMN_31_AMT", new Variant(0));
        props.put("COLUMN_31_CAPTION", new Variant(""));
        
        props.put("COLUMN_32_ID", new Variant(0));
        props.put("COLUMN_32_FORMULA", new Variant(""));
        props.put("COLUMN_32_PER", new Variant(0));
        props.put("COLUMN_32_AMT", new Variant(0));
        props.put("COLUMN_32_CAPTION", new Variant(""));
        
        props.put("COLUMN_33_ID", new Variant(0));
        props.put("COLUMN_33_FORMULA", new Variant(""));
        props.put("COLUMN_33_PER", new Variant(0));
        props.put("COLUMN_33_AMT", new Variant(0));
        props.put("COLUMN_33_CAPTION", new Variant(""));
        
        props.put("COLUMN_34_ID", new Variant(0));
        props.put("COLUMN_34_FORMULA", new Variant(""));
        props.put("COLUMN_34_PER", new Variant(0));
        props.put("COLUMN_34_AMT", new Variant(0));
        props.put("COLUMN_34_CAPTION", new Variant(""));
        
        props.put("COLUMN_35_ID", new Variant(0));
        props.put("COLUMN_35_FORMULA", new Variant(""));
        props.put("COLUMN_35_PER", new Variant(0));
        props.put("COLUMN_35_AMT", new Variant(0));
        props.put("COLUMN_35_CAPTION", new Variant(""));
        
        props.put("COLUMN_36_ID", new Variant(0));
        props.put("COLUMN_36_FORMULA", new Variant(""));
        props.put("COLUMN_36_PER", new Variant(0));
        props.put("COLUMN_36_AMT", new Variant(0));
        props.put("COLUMN_36_CAPTION", new Variant(""));
        
        props.put("COLUMN_37_ID", new Variant(0));
        props.put("COLUMN_37_FORMULA", new Variant(""));
        props.put("COLUMN_37_PER", new Variant(0));
        props.put("COLUMN_37_AMT", new Variant(0));
        props.put("COLUMN_37_CAPTION", new Variant(""));
        
        props.put("COLUMN_38_ID", new Variant(0));
        props.put("COLUMN_38_FORMULA", new Variant(""));
        props.put("COLUMN_38_PER", new Variant(0));
        props.put("COLUMN_38_AMT", new Variant(0));
        props.put("COLUMN_38_CAPTION", new Variant(""));
        
        props.put("COLUMN_39_ID", new Variant(0));
        props.put("COLUMN_39_FORMULA", new Variant(""));
        props.put("COLUMN_39_PER", new Variant(0));
        props.put("COLUMN_39_AMT", new Variant(0));
        props.put("COLUMN_39_CAPTION", new Variant(""));
        
        props.put("INV_GROSS_AMT", new Variant(0));
        props.put("INV_NET_AMT", new Variant(0));
        props.put("INV_FINAL_AMT", new Variant(0));
        props.put("INV_INVOICE_AMT", new Variant(0));
        props.put("INV_CGST", new Variant(0));
        props.put("INV_SGST", new Variant(0));
        props.put("INV_IGST", new Variant(0));
        props.put("INV_RCM", new Variant(0));
        props.put("INV_COMPOSITION", new Variant(0));
        props.put("INV_GST_COMPENSATION_CESS", new Variant(0));
        
        props.put("RCVD_GROSS_AMT", new Variant(0));
        props.put("RCVD_NET_AMT", new Variant(0));
        props.put("RCVD_FINAL_AMT", new Variant(0));
        props.put("RCVD_INVOICE_AMT", new Variant(0));
        props.put("RCVD_CGST", new Variant(0));
        props.put("RCVD_SGST", new Variant(0));
        props.put("RCVD_IGST", new Variant(0));
        props.put("RCVD_RCM", new Variant(0));
        props.put("RCVD_COMPOSITION", new Variant(0));
        props.put("RCVD_GST_COMPENSATION_CESS", new Variant(0));
        
        props.put("DB_GROSS_AMT", new Variant(0));
        props.put("DB_NET_AMT", new Variant(0));
        props.put("DB_FINAL_AMT", new Variant(0));
        props.put("DB_INVOICE_AMT", new Variant(0));
        props.put("DB_CGST", new Variant(0));
        props.put("DB_SGST", new Variant(0));
        props.put("DB_IGST", new Variant(0));
        props.put("DB_RCM", new Variant(0));
        props.put("DB_COMPOSITION", new Variant(0));
        props.put("DB_GST_COMPENSATION_CESS", new Variant(0));
        
        props.put("FIN_HIERARCHY_ID", new Variant(0));
        props.put("PAYMENT_TYPE", new Variant(0));
        props.put("INVOICE_AMOUNT", new Variant(0));
    }
    
    public boolean LoadData(int pCompanyID) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND GRN_TYPE=1 AND GRN_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND GRN_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY GRN_DATE");
            HistoryView = false;
            Ready = true;
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static boolean IsGRNExist(int pCompanyID, String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist = false;
        
        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1");
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                isExist = true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        } catch (Exception e) {
            return isExist;
        }
    }
    
    public static boolean IsGRNExist(String pDocNo) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist = false;
        
        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='" + pDocNo + "' AND GRN_TYPE=1 AND CANCELLED=0");
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                isExist = true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        } catch (Exception e) {
            return isExist;
        }
    }
    
    public static boolean IsGRNExist(String pDocNo, int Type) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist = false;
        
        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='" + pDocNo + "' AND GRN_TYPE=" + Type + " AND CANCELLED=0");
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                isExist = true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        } catch (Exception e) {
            return isExist;
        }
    }
    
    public static boolean IsGRNExist(String pDocNo, String dbURL) {
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        boolean isExist = false;
        
        try {
            tmpConn = data.getConn(dbURL);
            stTmp = tmpConn.createStatement();
            rsTmp = stTmp.executeQuery("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='" + pDocNo + "' AND GRN_TYPE=1 AND CANCELLED=0");
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                isExist = true;
            }
            
            //tmpConn.close();
            stTmp.close();
            rsTmp.close();
            
            return isExist;
            
        } catch (Exception e) {
            return isExist;
        }
    }
    
    public void Close() {
        try {
            //Conn.close();
            Stmt.close();
            rsResultSet.close();
        } catch (Exception e) {
            
        }
        
    }
    
    //Navigation Methods
    public boolean MoveFirst() {
        try {
            rsResultSet.first();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
        
    }
    
    public boolean MoveNext() {
        try {
            if (rsResultSet.isAfterLast() || rsResultSet.isLast()) {
                //Move pointer at last record
                //If it is beyond eof
                rsResultSet.last();
            } else {
                rsResultSet.next();
                if (rsResultSet.isAfterLast()) {
                    rsResultSet.last();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean MovePrevious() {
        try {
            if (rsResultSet.isFirst() || rsResultSet.isBeforeFirst()) {
                rsResultSet.first();
            } else {
                rsResultSet.previous();
                if (rsResultSet.isBeforeFirst()) {
                    rsResultSet.first();
                }
            }
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean MoveLast() {
        try {
            rsResultSet.last();
            setData();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean Insert() {
        
        Statement stItem, stLot, stStock, stTmp, stItemMaster, stHistory, stHDetail;
        ResultSet rsItem, rsLot, rsStock, rsTmp, rsItemMaster, rsHistory, rsHDetail;
        Statement stIssue, stHeader;
        ResultSet rsIssue, rsHeader;
        String ItemID = "", LotNo = "", BOENo = "", BOESrNo = "", WareHouseID = "", LocationID = "", MIRNo = "", strSQL = "", BOEDate = "";
        int CompanyID = 0, MIRSrNo = 0;
        double RejectedQty = 0, Qty = 0, ToleranceLimit = 0;
        
        try {
            
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate = java.sql.Date.valueOf((String) getAttribute("GRN_DATE").getObj());
            
            if ((DocDate.after(FinFromDate) || DocDate.compareTo(FinFromDate) == 0) && (DocDate.before(FinToDate) || DocDate.compareTo(FinToDate) == 0)) {
                //Withing the year
            } else {
                LastError = "Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rsHistory = stHistory.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE GRN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM D_INV_GRN_DETAIL_H WHERE GRN_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='1'");
            //rsHeader.first();
            ApprovalFlow ObjFlow = new ApprovalFlow();
            
            CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            
            if (getAttribute("SUPP_ID").getString().trim().equals("000000")) {
                if (getAttribute("PAYMENT_TYPE").getInt() != 1) {
                    LastError = "Supplier code is not valid";
                    return false;
                }
            }
            
            //=========== Check the Quantities entered against MIR.============= //
            for (int i = 1; i <= colGRNItems.size(); i++) {
                clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                
                ToleranceLimit = clsItem.getToleranceLimit(CompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                
                double MIRQty = 0;
                double PrevQty = 0; //Previously Entered Qty against MIR
                double CurrentQty = 0; //Currently entered Qty.
                
                if ((!MIRNo.trim().equals("")) && (MIRSrNo > 0)) //MIR No. entered
                {
                    //Get the  MIR Qty.
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT QTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo + "' AND SR_NO=" + MIRSrNo + " AND MIR_TYPE=1";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if (rsTmp.getRow() > 0) {
                        MIRQty = rsTmp.getDouble("QTY");
                    }
                    
                    //Get Total Qty Entered in GRN Against this MIR No.
                    PrevQty = 0;
                    stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo + "' AND MIR_SR_NO=" + MIRSrNo + " AND GRN_TYPE=1 AND GRN_NO NOT IN (SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE CANCELLED=1)";
                    rsTmp = stTmp.executeQuery(strSQL);
                    rsTmp.first();
                    
                    if (rsTmp.getRow() > 0) {
                        PrevQty = rsTmp.getDouble("SUMQTY");
                    }
                    
                    CurrentQty = ObjItem.getAttribute("QTY").getVal();
                    
                    /*if((CurrentQty+PrevQty-((PrevQty*ToleranceLimit)/100)) > MIRQty) //If total Qty exceeds MIR Qty. Do not allow
                     {
                     LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds MIR No. "+MIRNo+" Sr. No. "+MIRSrNo+" qty "+MIRQty+". Please verify the input.";
                     return false;
                     }*/
                }
            }
            //============== MIR Checking Completed ====================//
            
            //=========== Check the Max Qty Level.============= //
            for (int i = 1; i <= colGRNItems.size(); i++) {
                clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                
                double MaxQty = clsItem.getMaxQty(CompanyID, ItemID);
                
                if (MaxQty > 0) {
                    double CurrentQty = ObjItem.getAttribute("QTY").getVal();
                    double AvailableQty = clsItem.getAvailableQty(CompanyID, ItemID);
                    
                    if ((CurrentQty + AvailableQty) > MaxQty) {
                        LastError = "Total Receipt Qty " + (CurrentQty + AvailableQty) + " exceeds Maximum limit " + MaxQty + " for Item " + ItemID;
                        return false;
                    }
                }
            }
            //=================================================///
            
            // Update the Stock only after Final Approval //
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            
            if (AStatus.equals("F")) {
                //-------- First Update the stock -------------//
                for (int i = 1; i <= colGRNItems.size(); i++) {
                    clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                    
                    MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                    MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                    Qty = ObjItem.getAttribute("QTY").getVal();
                    RejectedQty = ObjItem.getAttribute("REJECTED_QTY").getVal();
                    ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                    String HSN_SAC_CODE = (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj();
                    
                    // Update GRN Received Qty
                    data.Execute("UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY=GRN_RECD_QTY+" + Qty + ",BAL_QTY=QTY-GRN_RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo.trim() + "' AND SR_NO=" + MIRSrNo + " AND MIR_TYPE=1");
                    data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo.trim() + "'");
                    
                    //Check that Stock is maintained for this item
                    if (clsItem.getMaintainStock(CompanyID, ItemID)) {
                        BOENo = (String) ObjItem.getAttribute("BOE_NO").getObj();
                        ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                        
                        HSN_SAC_CODE = (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj();
                        
                        BOESrNo = (String) ObjItem.getAttribute("BOE_SR_NO").getObj();
                        BOEDate = (String) ObjItem.getAttribute("BOE_DATE").getObj();
                        WareHouseID = (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                        LocationID = (String) ObjItem.getAttribute("LOCATION_ID").getObj();
                        
                        LotNo = "X"; //Fix Lot No. for General Items
                        
                        //========= New Code of Updating Issue Against the Item =========== //
                        double IssuedQty = 0;
                        Qty = ObjItem.getAttribute("QTY").getVal();
                        
                        rsIssue = data.getResult("SELECT A.ISSUE_NO,QTY,SR_NO,EXCESS_ISSUE_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND B.MIR_NO='" + MIRNo + "' AND B.MIR_SR_NO=" + MIRSrNo + " AND EXCESS_ISSUE_QTY>0 AND A.APPROVED=1 AND A.CANCELED=0 AND ITEM_CODE='" + ItemID + "'");
                        rsIssue.first();
                        
                        if (rsIssue.getRow() > 0) {
                            while (!rsIssue.isAfterLast()) {
                                IssuedQty = rsIssue.getDouble("EXCESS_ISSUE_QTY");
                                String IssueNo = rsIssue.getString("ISSUE_NO");
                                int IssueSrNo = rsIssue.getInt("SR_NO");
                                
                                if (IssuedQty > Qty) {
                                    data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (IssuedQty - Qty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                    Qty = 0;
                                }
                                
                                if (IssuedQty == Qty) {
                                    data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=0 WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                    Qty = 0;
                                }
                                
                                if (IssuedQty < Qty) {
                                    data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (Qty - IssuedQty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                    Qty = Qty - IssuedQty;
                                }
                                
                                rsIssue.next();
                            }
                        }
                        
                        if (Qty > 0) {
                            
                            rsIssue = data.getResult("SELECT A.ISSUE_NO,QTY,SR_NO,EXCESS_ISSUE_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND EXCESS_ISSUE_QTY>0 AND A.APPROVED=1 AND A.CANCELED=0 AND ITEM_CODE='" + ItemID + "'");
                            rsIssue.first();
                            
                            if (rsIssue.getRow() > 0) {
                                while (!rsIssue.isAfterLast()) {
                                    IssuedQty = rsIssue.getDouble("EXCESS_ISSUE_QTY");
                                    String IssueNo = rsIssue.getString("ISSUE_NO");
                                    int IssueSrNo = rsIssue.getInt("SR_NO");
                                    
                                    if (IssuedQty > Qty) {
                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (IssuedQty - Qty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                        Qty = 0;
                                    }
                                    
                                    if (IssuedQty == Qty) {
                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=0 WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                        Qty = 0;
                                    }
                                    
                                    if (IssuedQty < Qty) {
                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (Qty - IssuedQty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                        Qty = Qty - IssuedQty;
                                    }
                                    
                                    rsIssue.next();
                                }
                            }
                        }
                        // ================================================================ //
                        
                        double Rate = ObjItem.getAttribute("RATE").getVal();
                        
                        //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        rsTmp = stTmp.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID.trim() + "' AND LOT_NO='" + LotNo.trim() + "' AND BOE_NO='" + BOENo.trim() + "' AND WAREHOUSE_ID='" + WareHouseID.trim() + "' AND LOCATION_ID='" + LocationID.trim() + "'");
                        rsTmp.first();
                        
                        if (rsTmp.getRow() <= 0) //Entry does not exist. Create new entry
                        {
                            stStock = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            rsStock = stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER");
                            
                            //Insert the records. Opening Qty will be 0.
                            rsStock.moveToInsertRow();
                            rsStock.updateInt("COMPANY_ID", CompanyID);
                            rsStock.updateString("ITEM_ID", ItemID);
                            rsStock.updateString("BOE_NO", BOENo);
                            rsStock.updateString("LOT_NO", LotNo);
                            rsStock.updateString("BOE_SR_NO", BOESrNo);
                            rsStock.updateString("BOE_DATE", BOEDate);
                            rsStock.updateDouble("OPENING_QTY", 0);
                            rsStock.updateDouble("OPENING_RATE", 0);
                            rsStock.updateDouble("TOTAL_RECEIPT_QTY", Qty);
                            rsStock.updateDouble("TOTAL_ISSUED_QTY", 0);
                            rsStock.updateString("LAST_RECEIPT_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsStock.updateString("LAST_ISSUED_DATE", "0000-00-00");
                            
                            if (Rate <= 0) {
                                rsStock.updateDouble("ZERO_RECEIPT_QTY", Qty);
                                rsStock.updateDouble("ZERO_ISSUED_QTY", 0);
                                rsStock.updateDouble("ZERO_VAL_QTY", Qty);
                            } else {
                                rsStock.updateDouble("ZERO_RECEIPT_QTY", 0);
                                rsStock.updateDouble("ZERO_ISSUED_QTY", 0);
                                rsStock.updateDouble("ZERO_VAL_QTY", 0);
                            }
                            
                            rsStock.updateDouble("REJECTED_QTY", 0);
                            rsStock.updateDouble("ON_HAND_QTY", Qty);
                            rsStock.updateDouble("AVAILABLE_QTY", Qty);
                            rsStock.updateDouble("ALLOCATED_QTY", 0);
                            rsStock.updateString("WAREHOUSE_ID", WareHouseID);
                            rsStock.updateString("LOCATION_ID", LocationID);
                            rsStock.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                            rsStock.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                            rsStock.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                            rsStock.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                            rsStock.updateBoolean("CHANGED", true);
                            rsStock.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsStock.insertRow();
                        } else //Entry already exist. Update the stock values.
                        {
                            stStock = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            rsStock = stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID.trim() + "' AND LOT_NO='" + LotNo.trim() + "' AND BOE_NO='" + BOENo.trim() + "' AND WAREHOUSE_ID='" + WareHouseID.trim() + "' AND LOCATION_ID='" + LocationID.trim() + "'");
                            rsStock.first(); //There will be a single record only
                            
                            double lnLotQty = ObjItem.getAttribute("QTY").getVal(); //Record current Qty
                            
                            //Now check the Issued made (with Excess Qty) in Issue Detail
                            /*stIssue=Conn.createStatement();
                             rsIssue=stIssue.executeQuery("SELECT EXCESS_ISSUE_QTY AS EXCESS_ISSUE FROM D_INV_ISSUE_DETAIL WHERE D_INV_ISSUE_DETAIL.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_SR_NO="+MIRSrNo);
                             rsIssue.first();
                             if(rsIssue.getRow()>0) {
                             double ExcessQty=rsIssue.getDouble("EXCESS_ISSUE");
                             lnLotQty=lnLotQty-ExcessQty;
                             }*/
                            //======================================================//
                            if (Rate <= 0) {
                                rsStock.updateDouble("ZERO_RECEIPT_QTY", rsStock.getDouble("ZERO_RECEIPT_QTY") + Qty);
                                rsStock.updateDouble("ZERO_VAL_QTY", rsStock.getDouble("ZERO_VAL_QTY") + Qty);
                            }
                            
                            rsStock.updateDouble("TOTAL_RECEIPT_QTY", rsStock.getDouble("TOTAL_RECEIPT_QTY") + Qty);
                            rsStock.updateString("LAST_RECEIPT_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsStock.updateDouble("ON_HAND_QTY", rsStock.getDouble("ON_HAND_QTY") + Qty);
                            rsStock.updateDouble("AVAILABLE_QTY", rsStock.getDouble("AVAILABLE_QTY") + Qty);
                            rsStock.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                            rsStock.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                            rsStock.updateBoolean("CHANGED", true);
                            rsStock.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsStock.updateRow();
                        }
                        
                        //======= Update the Item Master =========
                        stItemMaster = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        rsItemMaster = stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID.trim() + "' AND CANCELLED=0 ");
                        rsItemMaster.first();
                        
                        if (rsItemMaster.getRow() > 0) {
                            double lnQty = ObjItem.getAttribute("QTY").getVal();
                            rsItemMaster.updateDouble("TOTAL_RECEIPT_QTY", rsItemMaster.getDouble("TOTAL_RECEIPT_QTY") + Qty);
                            rsItemMaster.updateString("LAST_RECEIPT_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsItemMaster.updateDouble("ON_HAND_QTY", rsItemMaster.getDouble("ON_HAND_QTY") + Qty);
                            rsItemMaster.updateDouble("AVAILABLE_QTY", rsItemMaster.getDouble("AVAILABLE_QTY") + Qty);
                            rsItemMaster.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                            rsItemMaster.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                            rsItemMaster.updateBoolean("CHANGED", true);
                            rsItemMaster.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsItemMaster.updateRow();
                        }
                        //======= Item Master stock updation completed ==========
                    } //If Condition
                } //First for loop
                //=============Updation of stock completed=========================//
            } //End of Approval Status condition
            
            //--------- Generate New MIR No. ------------
            if (UserDocNo) {
                rsTmp = data.getResult("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='" + ((String) getAttribute("GRN_NO").getObj()).trim() + "' AND COMPANY_ID=" + EITLERPGLOBAL.gCompanyID);
                rsTmp.first();
                
                if (rsTmp.getRow() > 0) {
                    LastError = "Document no. already exist. Please specify other document no.";
                    return false;
                }
            } else {
                setAttribute("GRN_NO", clsFirstFree.getNextFreeNo((int) EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, (int) getAttribute("FFNO").getVal(), true));
            }
            //-------------------------------------------------
            
            rsResultSet.first();
            rsResultSet.moveToInsertRow();
            rsResultSet.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsResultSet.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
            rsResultSet.updateString("GRN_DATE", (String) getAttribute("GRN_DATE").getObj());
            rsResultSet.updateString("APPROVED_ON", (String) getAttribute("APPROVED_ON").getObj());
            rsResultSet.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("REFA", (String) getAttribute("REFA").getObj());
            rsResultSet.updateString("CHALAN_NO", (String) getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE", (String) getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("LR_NO", (String) getAttribute("LR_NO").getObj());
            rsResultSet.updateString("LR_DATE", (String) getAttribute("LR_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO", (String) getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE", (String) getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateDouble("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getDouble());
            rsResultSet.updateInt("TRANSPORTER", (int) getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("TRANSPORTER_NAME", (String) getAttribute("TRANSPORTER_NAME").getObj());
            rsResultSet.updateString("GATEPASS_NO", (String) getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateDouble("ACCESSABLE_VALUE", getAttribute("ACCESSABLE_VALUE").getVal());
            rsResultSet.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE_PAYMENT", getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("GROSS_AMOUNT", getAttribute("GROSS_AMOUNT").getVal());
            rsResultSet.updateBoolean("GRN_PENDING", getAttribute("GRN_PENDING").getBool());
            rsResultSet.updateInt("GRN_PENDING_REASON", (int) getAttribute("GRN_PENDING_REASON").getVal());
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CENVATED_ITEMS", getAttribute("CENVATED_ITEMS").getBool());
            rsResultSet.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("GRN_TYPE", (int) getAttribute("GRN_TYPE").getVal());
            rsResultSet.updateString("OPEN_STATUS", (String) getAttribute("OPEN_STATUS").getObj());
            rsResultSet.updateInt("FOR_STORE", (int) getAttribute("FOR_STORE").getVal());
            rsResultSet.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            
            //Now Custom Columns
            rsResultSet.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsResultSet.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsResultSet.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsResultSet.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsResultSet.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsResultSet.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsResultSet.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsResultSet.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsResultSet.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsResultSet.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsResultSet.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsResultSet.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsResultSet.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsResultSet.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsResultSet.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsResultSet.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_22_ID", (int) getAttribute("COLUMN_22_ID").getVal());
            rsResultSet.updateString("COLUMN_22_FORMULA", (String) getAttribute("COLUMN_22_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_22_PER", getAttribute("COLUMN_22_PER").getVal());
            rsResultSet.updateDouble("COLUMN_22_AMT", getAttribute("COLUMN_22_AMT").getVal());
            rsResultSet.updateString("COLUMN_22_CAPTION", (String) getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_23_ID", (int) getAttribute("COLUMN_23_ID").getVal());
            rsResultSet.updateString("COLUMN_23_FORMULA", (String) getAttribute("COLUMN_23_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_23_PER", getAttribute("COLUMN_23_PER").getVal());
            rsResultSet.updateDouble("COLUMN_23_AMT", getAttribute("COLUMN_23_AMT").getVal());
            rsResultSet.updateString("COLUMN_23_CAPTION", (String) getAttribute("COLUMN_23_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_24_ID", (int) getAttribute("COLUMN_24_ID").getVal());
            rsResultSet.updateString("COLUMN_24_FORMULA", (String) getAttribute("COLUMN_24_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_24_PER", getAttribute("COLUMN_24_PER").getVal());
            rsResultSet.updateDouble("COLUMN_24_AMT", getAttribute("COLUMN_24_AMT").getVal());
            rsResultSet.updateString("COLUMN_24_CAPTION", (String) getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_25_ID", (int) getAttribute("COLUMN_25_ID").getVal());
            rsResultSet.updateString("COLUMN_25_FORMULA", (String) getAttribute("COLUMN_25_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_25_PER", getAttribute("COLUMN_25_PER").getVal());
            rsResultSet.updateDouble("COLUMN_25_AMT", getAttribute("COLUMN_25_AMT").getVal());
            rsResultSet.updateString("COLUMN_25_CAPTION", (String) getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_26_ID", (int) getAttribute("COLUMN_26_ID").getVal());
            rsResultSet.updateString("COLUMN_26_FORMULA", (String) getAttribute("COLUMN_26_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_26_PER", getAttribute("COLUMN_26_PER").getVal());
            rsResultSet.updateDouble("COLUMN_26_AMT", getAttribute("COLUMN_26_AMT").getVal());
            rsResultSet.updateString("COLUMN_26_CAPTION", (String) getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_27_ID", (int) getAttribute("COLUMN_27_ID").getVal());
            rsResultSet.updateString("COLUMN_27_FORMULA", (String) getAttribute("COLUMN_27_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_27_PER", getAttribute("COLUMN_27_PER").getVal());
            rsResultSet.updateDouble("COLUMN_27_AMT", getAttribute("COLUMN_27_AMT").getVal());
            rsResultSet.updateString("COLUMN_27_CAPTION", (String) getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_28_ID", (int) getAttribute("COLUMN_28_ID").getVal());
            rsResultSet.updateString("COLUMN_28_FORMULA", (String) getAttribute("COLUMN_28_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_28_PER", getAttribute("COLUMN_28_PER").getVal());
            rsResultSet.updateDouble("COLUMN_28_AMT", getAttribute("COLUMN_28_AMT").getVal());
            rsResultSet.updateString("COLUMN_28_CAPTION", (String) getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_29_ID", (int) getAttribute("COLUMN_29_ID").getVal());
            rsResultSet.updateString("COLUMN_29_FORMULA", (String) getAttribute("COLUMN_29_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_29_PER", getAttribute("COLUMN_29_PER").getVal());
            rsResultSet.updateDouble("COLUMN_29_AMT", getAttribute("COLUMN_29_AMT").getVal());
            rsResultSet.updateString("COLUMN_29_CAPTION", (String) getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_30_ID", (int) getAttribute("COLUMN_30_ID").getVal());
            rsResultSet.updateString("COLUMN_30_FORMULA", (String) getAttribute("COLUMN_30_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_30_PER", getAttribute("COLUMN_30_PER").getVal());
            rsResultSet.updateDouble("COLUMN_30_AMT", getAttribute("COLUMN_30_AMT").getVal());
            
            //            rsResultSet.updateInt("COLUMN_31_ID",(int)getAttribute("COLUMN_31_ID").getVal());
            //            rsResultSet.updateString("COLUMN_31_FORMULA",(String)getAttribute("COLUMN_31_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_31_PER",getAttribute("COLUMN_31_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_31_AMT",getAttribute("COLUMN_31_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_31_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
            //
            //            rsResultSet.updateInt("COLUMN_32_ID",(int)getAttribute("COLUMN_32_ID").getVal());
            //            rsResultSet.updateString("COLUMN_32_FORMULA",(String)getAttribute("COLUMN_32_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_32_PER",getAttribute("COLUMN_32_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_32_AMT",getAttribute("COLUMN_32_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_32_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
            //
            //            rsResultSet.updateInt("COLUMN_33_ID",(int)getAttribute("COLUMN_33_ID").getVal());
            //            rsResultSet.updateString("COLUMN_33_FORMULA",(String)getAttribute("COLUMN_33_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_33_PER",getAttribute("COLUMN_33_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_33_AMT",getAttribute("COLUMN_33_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_33_CAPTION",(String)getAttribute("COLUMN_33_CAPTION").getObj());
            //
            //            rsResultSet.updateInt("COLUMN_34_ID",(int)getAttribute("COLUMN_34_ID").getVal());
            //            rsResultSet.updateString("COLUMN_34_FORMULA",(String)getAttribute("COLUMN_34_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_34_PER",getAttribute("COLUMN_34_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_34_AMT",getAttribute("COLUMN_34_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_34_CAPTION",(String)getAttribute("COLUMN_34_CAPTION").getObj());
            //
            //            rsResultSet.updateInt("COLUMN_35_ID",(int)getAttribute("COLUMN_35_ID").getVal());
            //            rsResultSet.updateString("COLUMN_35_FORMULA",(String)getAttribute("COLUMN_35_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_35_PER",getAttribute("COLUMN_35_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_35_AMT",getAttribute("COLUMN_35_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_35_CAPTION",(String)getAttribute("COLUMN_35_CAPTION").getObj());
            //
            //            rsResultSet.updateInt("COLUMN_36_ID",(int)getAttribute("COLUMN_36_ID").getVal());
            //            rsResultSet.updateString("COLUMN_36_FORMULA",(String)getAttribute("COLUMN_36_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_36_PER",getAttribute("COLUMN_36_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_36_AMT",getAttribute("COLUMN_36_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_36_CAPTION",(String)getAttribute("COLUMN_36_CAPTION").getObj());
            //            rsResultSet.updateInt("COLUMN_37_ID",(int)getAttribute("COLUMN_37_ID").getVal());
            //            rsResultSet.updateString("COLUMN_37_FORMULA",(String)getAttribute("COLUMN_37_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_37_PER",getAttribute("COLUMN_37_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_37_AMT",getAttribute("COLUMN_37_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_37_CAPTION",(String)getAttribute("COLUMN_37_CAPTION").getObj());
            //
            //            rsResultSet.updateInt("COLUMN_38_ID",(int)getAttribute("COLUMN_38_ID").getVal());
            //            rsResultSet.updateString("COLUMN_38_FORMULA",(String)getAttribute("COLUMN_38_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_38_PER",getAttribute("COLUMN_38_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_38_AMT",getAttribute("COLUMN_38_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_38_CAPTION",(String)getAttribute("COLUMN_38_CAPTION").getObj());
            //
            //            rsResultSet.updateInt("COLUMN_39_ID",(int)getAttribute("COLUMN_39_ID").getVal());
            //            rsResultSet.updateString("COLUMN_39_FORMULA",(String)getAttribute("COLUMN_39_FORMULA").getObj());
            //            rsResultSet.updateDouble("COLUMN_39_PER",getAttribute("COLUMN_39_PER").getVal());
            //            rsResultSet.updateDouble("COLUMN_39_AMT",getAttribute("COLUMN_39_AMT").getVal());
            //            rsResultSet.updateString("COLUMN_39_CAPTION",(String)getAttribute("COLUMN_39_CAPTION").getObj());
            //
            rsResultSet.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            
            rsResultSet.updateBoolean("APPROVED", false);
            rsResultSet.updateString("APPROVED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("REJECTED", false);
            rsResultSet.updateString("REJECTED_DATE", "0000-00-00");
            rsResultSet.updateBoolean("CANCELLED", false);
            
            rsResultSet.updateInt("PF_POST", getAttribute("PF_POST").getInt());
            rsResultSet.updateInt("FREIGHT_POST", getAttribute("FREIGHT_POST").getInt());
            rsResultSet.updateInt("OCTROI_POST", getAttribute("OCTROI_POST").getInt());
            rsResultSet.updateInt("INSURANCE_POST", getAttribute("INSURANCE_POST").getInt());
            rsResultSet.updateInt("CLEARANCE_POST", getAttribute("CLEARANCE_POST").getInt());
            rsResultSet.updateInt("AIR_FREIGHT_POST", getAttribute("AIR_FREIGHT_POST").getInt());
            rsResultSet.updateInt("OTHERS_POST", getAttribute("OTHERS_POST").getInt());
            rsResultSet.updateInt("PAYMENT_TYPE", getAttribute("PAYMENT_TYPE").getInt());
            
            rsResultSet.insertRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", 1); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
            rsHistory.updateString("GRN_DATE", (String) getAttribute("GRN_DATE").getObj());
            rsHistory.updateString("APPROVED_ON", (String) getAttribute("APPROVED_ON").getObj());
            rsHistory.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("REFA", (String) getAttribute("REFA").getObj());
            rsHistory.updateString("CHALAN_NO", (String) getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE", (String) getAttribute("CHALAN_DATE").getObj());
            rsHistory.updateString("LR_NO", (String) getAttribute("LR_NO").getObj());
            rsHistory.updateString("LR_DATE", (String) getAttribute("LR_DATE").getObj());
            rsHistory.updateString("INVOICE_NO", (String) getAttribute("INVOICE_NO").getObj());
            rsHistory.updateDouble("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getDouble());
            rsHistory.updateString("INVOICE_DATE", (String) getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateInt("TRANSPORTER", (int) getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("TRANSPORTER_NAME", (String) getAttribute("TRANSPORTER_NAME").getObj());
            rsHistory.updateString("GATEPASS_NO", (String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateDouble("ACCESSABLE_VALUE", getAttribute("ACCESSABLE_VALUE").getVal());
            rsHistory.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("CURRENCY_RATE_PAYMENT", getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("GROSS_AMOUNT", getAttribute("GROSS_AMOUNT").getVal());
            rsHistory.updateBoolean("GRN_PENDING", getAttribute("GRN_PENDING").getBool());
            rsHistory.updateInt("GRN_PENDING_REASON", (int) getAttribute("GRN_PENDING_REASON").getVal());
            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CENVATED_ITEMS", getAttribute("CENVATED_ITEMS").getBool());
            rsHistory.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("GRN_TYPE", (int) getAttribute("GRN_TYPE").getVal());
            rsHistory.updateString("OPEN_STATUS", (String) getAttribute("OPEN_STATUS").getObj());
            rsHistory.updateInt("FOR_STORE", (int) getAttribute("FOR_STORE").getVal());
            rsHistory.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsHistory.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsHistory.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsHistory.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsHistory.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsHistory.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsHistory.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsHistory.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsHistory.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsHistory.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsHistory.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsHistory.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsHistory.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsHistory.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsHistory.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsHistory.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_22_ID", (int) getAttribute("COLUMN_22_ID").getVal());
            rsHistory.updateString("COLUMN_22_FORMULA", (String) getAttribute("COLUMN_22_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_22_PER", getAttribute("COLUMN_22_PER").getVal());
            rsHistory.updateDouble("COLUMN_22_AMT", getAttribute("COLUMN_22_AMT").getVal());
            rsHistory.updateString("COLUMN_22_CAPTION", (String) getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_23_ID", (int) getAttribute("COLUMN_23_ID").getVal());
            rsHistory.updateString("COLUMN_23_FORMULA", (String) getAttribute("COLUMN_23_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_23_PER", getAttribute("COLUMN_23_PER").getVal());
            rsHistory.updateDouble("COLUMN_23_AMT", getAttribute("COLUMN_23_AMT").getVal());
            rsHistory.updateString("COLUMN_23_CAPTION", (String) getAttribute("COLUMN_23_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_24_ID", (int) getAttribute("COLUMN_24_ID").getVal());
            rsHistory.updateString("COLUMN_24_FORMULA", (String) getAttribute("COLUMN_24_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_24_PER", getAttribute("COLUMN_24_PER").getVal());
            rsHistory.updateDouble("COLUMN_24_AMT", getAttribute("COLUMN_24_AMT").getVal());
            rsHistory.updateString("COLUMN_24_CAPTION", (String) getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_25_ID", (int) getAttribute("COLUMN_25_ID").getVal());
            rsHistory.updateString("COLUMN_25_FORMULA", (String) getAttribute("COLUMN_25_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_25_PER", getAttribute("COLUMN_25_PER").getVal());
            rsHistory.updateDouble("COLUMN_25_AMT", getAttribute("COLUMN_25_AMT").getVal());
            rsHistory.updateString("COLUMN_25_CAPTION", (String) getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_26_ID", (int) getAttribute("COLUMN_26_ID").getVal());
            rsHistory.updateString("COLUMN_26_FORMULA", (String) getAttribute("COLUMN_26_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_26_PER", getAttribute("COLUMN_26_PER").getVal());
            rsHistory.updateDouble("COLUMN_26_AMT", getAttribute("COLUMN_26_AMT").getVal());
            rsHistory.updateString("COLUMN_26_CAPTION", (String) getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_27_ID", (int) getAttribute("COLUMN_27_ID").getVal());
            rsHistory.updateString("COLUMN_27_FORMULA", (String) getAttribute("COLUMN_27_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_27_PER", getAttribute("COLUMN_27_PER").getVal());
            rsHistory.updateDouble("COLUMN_27_AMT", getAttribute("COLUMN_27_AMT").getVal());
            rsHistory.updateString("COLUMN_27_CAPTION", (String) getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_28_ID", (int) getAttribute("COLUMN_28_ID").getVal());
            rsHistory.updateString("COLUMN_28_FORMULA", (String) getAttribute("COLUMN_28_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_28_PER", getAttribute("COLUMN_28_PER").getVal());
            rsHistory.updateDouble("COLUMN_28_AMT", getAttribute("COLUMN_28_AMT").getVal());
            rsHistory.updateString("COLUMN_28_CAPTION", (String) getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_29_ID", (int) getAttribute("COLUMN_29_ID").getVal());
            rsHistory.updateString("COLUMN_29_FORMULA", (String) getAttribute("COLUMN_29_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_29_PER", getAttribute("COLUMN_29_PER").getVal());
            rsHistory.updateDouble("COLUMN_29_AMT", getAttribute("COLUMN_29_AMT").getVal());
            rsHistory.updateString("COLUMN_29_CAPTION", (String) getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_30_ID", (int) getAttribute("COLUMN_30_ID").getVal());
            rsHistory.updateString("COLUMN_30_FORMULA", (String) getAttribute("COLUMN_30_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_30_PER", getAttribute("COLUMN_30_PER").getVal());
            rsHistory.updateDouble("COLUMN_30_AMT", getAttribute("COLUMN_30_AMT").getVal());
            rsHistory.updateString("COLUMN_30_CAPTION", (String) getAttribute("COLUMN_30_CAPTION").getObj());
            
            
            //            rsHistory.updateInt("COLUMN_31_ID",(int)getAttribute("COLUMN_31_ID").getVal());
            //            rsHistory.updateString("COLUMN_31_FORMULA",(String)getAttribute("COLUMN_31_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_31_PER",getAttribute("COLUMN_31_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_31_AMT",getAttribute("COLUMN_31_AMT").getVal());
            //            rsHistory.updateString("COLUMN_31_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
            //
            //            rsHistory.updateInt("COLUMN_32_ID",(int)getAttribute("COLUMN_32_ID").getVal());
            //            rsHistory.updateString("COLUMN_32_FORMULA",(String)getAttribute("COLUMN_32_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_32_PER",getAttribute("COLUMN_32_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_32_AMT",getAttribute("COLUMN_32_AMT").getVal());
            //            rsHistory.updateString("COLUMN_32_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
            //
            //            rsHistory.updateInt("COLUMN_33_ID",(int)getAttribute("COLUMN_33_ID").getVal());
            //            rsHistory.updateString("COLUMN_33_FORMULA",(String)getAttribute("COLUMN_33_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_33_PER",getAttribute("COLUMN_33_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_33_AMT",getAttribute("COLUMN_33_AMT").getVal());
            //            rsHistory.updateString("COLUMN_33_CAPTION",(String)getAttribute("COLUMN_33_CAPTION").getObj());
            //
            //            rsHistory.updateInt("COLUMN_34_ID",(int)getAttribute("COLUMN_34_ID").getVal());
            //            rsHistory.updateString("COLUMN_34_FORMULA",(String)getAttribute("COLUMN_34_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_34_PER",getAttribute("COLUMN_34_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_34_AMT",getAttribute("COLUMN_34_AMT").getVal());
            //            rsHistory.updateString("COLUMN_34_CAPTION",(String)getAttribute("COLUMN_34_CAPTION").getObj());
            //
            //            rsHistory.updateInt("COLUMN_35_ID",(int)getAttribute("COLUMN_35_ID").getVal());
            //            rsHistory.updateString("COLUMN_35_FORMULA",(String)getAttribute("COLUMN_35_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_35_PER",getAttribute("COLUMN_35_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_35_AMT",getAttribute("COLUMN_35_AMT").getVal());
            //            rsHistory.updateString("COLUMN_35_CAPTION",(String)getAttribute("COLUMN_35_CAPTION").getObj());
            //
            //            rsHistory.updateInt("COLUMN_36_ID",(int)getAttribute("COLUMN_36_ID").getVal());
            //            rsHistory.updateString("COLUMN_36_FORMULA",(String)getAttribute("COLUMN_36_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_36_PER",getAttribute("COLUMN_36_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_36_AMT",getAttribute("COLUMN_36_AMT").getVal());
            //            rsHistory.updateString("COLUMN_36_CAPTION",(String)getAttribute("COLUMN_36_CAPTION").getObj());
            //            rsHistory.updateInt("COLUMN_37_ID",(int)getAttribute("COLUMN_37_ID").getVal());
            //            rsHistory.updateString("COLUMN_37_FORMULA",(String)getAttribute("COLUMN_37_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_37_PER",getAttribute("COLUMN_37_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_37_AMT",getAttribute("COLUMN_37_AMT").getVal());
            //            rsHistory.updateString("COLUMN_37_CAPTION",(String)getAttribute("COLUMN_37_CAPTION").getObj());
            //
            //            rsHistory.updateInt("COLUMN_38_ID",(int)getAttribute("COLUMN_38_ID").getVal());
            //            rsHistory.updateString("COLUMN_38_FORMULA",(String)getAttribute("COLUMN_38_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_38_PER",getAttribute("COLUMN_38_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_38_AMT",getAttribute("COLUMN_38_AMT").getVal());
            //            rsHistory.updateString("COLUMN_38_CAPTION",(String)getAttribute("COLUMN_38_CAPTION").getObj());
            //
            //            rsHistory.updateInt("COLUMN_39_ID",(int)getAttribute("COLUMN_39_ID").getVal());
            //            rsHistory.updateString("COLUMN_39_FORMULA",(String)getAttribute("COLUMN_39_FORMULA").getObj());
            //            rsHistory.updateDouble("COLUMN_39_PER",getAttribute("COLUMN_39_PER").getVal());
            //            rsHistory.updateDouble("COLUMN_39_AMT",getAttribute("COLUMN_39_AMT").getVal());
            //            rsHistory.updateString("COLUMN_39_CAPTION",(String)getAttribute("COLUMN_39_CAPTION").getObj());
            //
            rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateBoolean("APPROVED", false);
            rsHistory.updateString("APPROVED_DATE", "0000-00-00");
            rsHistory.updateBoolean("REJECTED", false);
            rsHistory.updateString("REJECTED_DATE", "0000-00-00");
            rsHistory.updateBoolean("CANCELLED", false);
            
            rsHistory.updateInt("PF_POST", getAttribute("PF_POST").getInt());
            rsHistory.updateInt("FREIGHT_POST", getAttribute("FREIGHT_POST").getInt());
            rsHistory.updateInt("OCTROI_POST", getAttribute("OCTROI_POST").getInt());
            rsHistory.updateInt("INSURANCE_POST", getAttribute("INSURANCE_POST").getInt());
            rsHistory.updateInt("CLEARANCE_POST", getAttribute("CLEARANCE_POST").getInt());
            rsHistory.updateInt("AIR_FREIGHT_POST", getAttribute("AIR_FREIGHT_POST").getInt());
            rsHistory.updateInt("OTHERS_POST", getAttribute("OTHERS_POST").getInt());
            rsHistory.updateInt("PAYMENT_TYPE", getAttribute("PAYMENT_TYPE").getInt());
            
            rsHistory.insertRow();
            
            //====== Now turn of GRN Items ======
            stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsItem = stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL WHERE GRN_NO='1'");
            rsItem.first();
            
            for (int i = 1; i <= colGRNItems.size(); i++) {
                clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
                rsItem.updateInt("SR_NO", i);
                rsItem.updateInt("GRN_TYPE", 1); //1 - General Material
                rsItem.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsItem.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsItem.updateInt("MIR_TYPE", 1);
                rsItem.updateString("PO_NO", (String) ObjItem.getAttribute("PO_NO").getObj());
                rsItem.updateInt("PO_SR_NO", (int) ObjItem.getAttribute("PO_SR_NO").getVal());
                rsItem.updateInt("PO_TYPE", (int) ObjItem.getAttribute("PO_TYPE").getVal());
                rsItem.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                rsItem.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsItem.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE", (String) ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("MIR_QTY", ObjItem.getAttribute("MIR_QTY").getVal());
                rsItem.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("EXCESS_QTY", ObjItem.getAttribute("EXCESS_QTY").getVal());
                rsItem.updateDouble("PO_QTY", ObjItem.getAttribute("PO_QTY").getVal());
                rsItem.updateDouble("RECEIVED_QTY", ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsItem.updateDouble("BALANCE_PO_QTY", ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsItem.updateInt("REJECTED_REASON_ID", (int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsItem.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsItem.updateDouble("REJECTED_QTY", ObjItem.getAttribute("REJECTED_QTY").getVal());
                rsItem.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsItem.updateString("W_MIE", (String) ObjItem.getAttribute("W_MIE").getObj());
                rsItem.updateString("NO_CASE", (String) ObjItem.getAttribute("NO_CASE").getObj());
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsItem.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsItem.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsItem.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsItem.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsItem.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsItem.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsItem.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsItem.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsItem.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsItem.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsItem.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsItem.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsItem.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsItem.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsItem.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsItem.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsItem.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsItem.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsItem.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsItem.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsItem.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsItem.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsItem.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsItem.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsItem.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsItem.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsItem.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsItem.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsItem.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsItem.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsItem.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsItem.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsItem.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_22_ID", (int) ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsItem.updateString("COLUMN_22_FORMULA", (String) ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_22_PER", ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsItem.updateDouble("COLUMN_22_AMT", ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsItem.updateString("COLUMN_22_CAPTION", (String) ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_23_ID", (int) ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsItem.updateString("COLUMN_23_FORMULA", (String) ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_23_PER", ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsItem.updateDouble("COLUMN_23_AMT", ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsItem.updateString("COLUMN_23_CAPTION", (String) ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_24_ID", (int) ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsItem.updateString("COLUMN_24_FORMULA", (String) ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_24_PER", ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsItem.updateDouble("COLUMN_24_AMT", ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsItem.updateString("COLUMN_24_CAPTION", (String) ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_25_ID", (int) ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsItem.updateString("COLUMN_25_FORMULA", (String) ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_25_PER", ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsItem.updateDouble("COLUMN_25_AMT", ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsItem.updateString("COLUMN_25_CAPTION", (String) ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_26_ID", (int) ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsItem.updateString("COLUMN_26_FORMULA", (String) ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_26_PER", ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsItem.updateDouble("COLUMN_26_AMT", ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsItem.updateString("COLUMN_26_CAPTION", (String) ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_27_ID", (int) ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsItem.updateString("COLUMN_27_FORMULA", (String) ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_27_PER", ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsItem.updateDouble("COLUMN_27_AMT", ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsItem.updateString("COLUMN_27_CAPTION", (String) ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_28_ID", (int) ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsItem.updateString("COLUMN_28_FORMULA", (String) ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_28_PER", ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsItem.updateDouble("COLUMN_28_AMT", ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsItem.updateString("COLUMN_28_CAPTION", (String) ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_29_ID", (int) ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsItem.updateString("COLUMN_29_FORMULA", (String) ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_29_PER", ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsItem.updateDouble("COLUMN_29_AMT", ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsItem.updateString("COLUMN_29_CAPTION", (String) ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_30_ID", (int) ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsItem.updateString("COLUMN_30_FORMULA", (String) ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_30_PER", ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsItem.updateDouble("COLUMN_30_AMT", ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsItem.updateString("COLUMN_30_CAPTION", (String) ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_31_ID", (int) ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsItem.updateString("COLUMN_31_FORMULA", (String) ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_31_PER", ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsItem.updateDouble("COLUMN_31_AMT", ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsItem.updateString("COLUMN_31_CAPTION", (String) ObjItem.getAttribute("COLUMN_31_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_32_ID", (int) ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsItem.updateString("COLUMN_32_FORMULA", (String) ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_32_PER", ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsItem.updateDouble("COLUMN_32_AMT", ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsItem.updateString("COLUMN_32_CAPTION", (String) ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_33_ID", (int) ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsItem.updateString("COLUMN_33_FORMULA", (String) ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_33_PER", ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsItem.updateDouble("COLUMN_33_AMT", ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsItem.updateString("COLUMN_33_CAPTION", (String) ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_34_ID", (int) ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsItem.updateString("COLUMN_34_FORMULA", (String) ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_34_PER", ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsItem.updateDouble("COLUMN_34_AMT", ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsItem.updateString("COLUMN_34_CAPTION", (String) ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_35_ID", (int) ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsItem.updateString("COLUMN_35_FORMULA", (String) ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_35_PER", ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsItem.updateDouble("COLUMN_35_AMT", ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsItem.updateString("COLUMN_35_CAPTION", (String) ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_36_ID", (int) ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsItem.updateString("COLUMN_36_FORMULA", (String) ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_36_PER", ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsItem.updateDouble("COLUMN_36_AMT", ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsItem.updateString("COLUMN_36_CAPTION", (String) ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_37_ID", (int) ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsItem.updateString("COLUMN_37_FORMULA", (String) ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_37_PER", ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsItem.updateDouble("COLUMN_37_AMT", ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsItem.updateString("COLUMN_37_CAPTION", (String) ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_38_ID", (int) ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsItem.updateString("COLUMN_38_FORMULA", (String) ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_38_PER", ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsItem.updateDouble("COLUMN_38_AMT", ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsItem.updateString("COLUMN_38_CAPTION", (String) ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_39_ID", (int) ObjItem.getAttribute("COLUMN_39_ID").getVal());
                rsItem.updateString("COLUMN_39_FORMULA", (String) ObjItem.getAttribute("COLUMN_39_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_39_PER", ObjItem.getAttribute("COLUMN_39_PER").getVal());
                rsItem.updateDouble("COLUMN_39_AMT", ObjItem.getAttribute("COLUMN_39_AMT").getVal());
                rsItem.updateString("COLUMN_39_CAPTION", (String) ObjItem.getAttribute("COLUMN_39_CAPTION").getObj());
                
                //                rsItem.updateInt("COLUMN_31_ID",(int)getAttribute("COLUMN_31_ID").getVal());
                //                rsItem.updateString("COLUMN_31_FORMULA",(String)getAttribute("COLUMN_31_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_31_PER",getAttribute("COLUMN_31_PER").getVal());
                //                rsItem.updateDouble("COLUMN_31_AMT",getAttribute("COLUMN_31_AMT").getVal());
                //                rsItem.updateString("COLUMN_31_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_32_ID",(int)getAttribute("COLUMN_32_ID").getVal());
                //                rsItem.updateString("COLUMN_32_FORMULA",(String)getAttribute("COLUMN_32_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_32_PER",getAttribute("COLUMN_32_PER").getVal());
                //                rsItem.updateDouble("COLUMN_32_AMT",getAttribute("COLUMN_32_AMT").getVal());
                //                rsItem.updateString("COLUMN_32_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_33_ID",(int)getAttribute("COLUMN_33_ID").getVal());
                //                rsItem.updateString("COLUMN_33_FORMULA",(String)getAttribute("COLUMN_33_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_33_PER",getAttribute("COLUMN_33_PER").getVal());
                //                rsItem.updateDouble("COLUMN_33_AMT",getAttribute("COLUMN_33_AMT").getVal());
                //                rsItem.updateString("COLUMN_33_CAPTION",(String)getAttribute("COLUMN_33_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_34_ID",(int)getAttribute("COLUMN_34_ID").getVal());
                //                rsItem.updateString("COLUMN_34_FORMULA",(String)getAttribute("COLUMN_34_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_34_PER",getAttribute("COLUMN_34_PER").getVal());
                //                rsItem.updateDouble("COLUMN_34_AMT",getAttribute("COLUMN_34_AMT").getVal());
                //                rsItem.updateString("COLUMN_34_CAPTION",(String)getAttribute("COLUMN_34_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_35_ID",(int)getAttribute("COLUMN_35_ID").getVal());
                //                rsItem.updateString("COLUMN_35_FORMULA",(String)getAttribute("COLUMN_35_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_35_PER",getAttribute("COLUMN_35_PER").getVal());
                //                rsItem.updateDouble("COLUMN_35_AMT",getAttribute("COLUMN_35_AMT").getVal());
                //                rsItem.updateString("COLUMN_35_CAPTION",(String)getAttribute("COLUMN_35_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_36_ID",(int)getAttribute("COLUMN_36_ID").getVal());
                //                rsItem.updateString("COLUMN_36_FORMULA",(String)getAttribute("COLUMN_36_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_36_PER",getAttribute("COLUMN_36_PER").getVal());
                //                rsItem.updateDouble("COLUMN_36_AMT",getAttribute("COLUMN_36_AMT").getVal());
                //                rsItem.updateString("COLUMN_36_CAPTION",(String)getAttribute("COLUMN_36_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_37_ID",(int)getAttribute("COLUMN_37_ID").getVal());
                //                rsItem.updateString("COLUMN_37_FORMULA",(String)getAttribute("COLUMN_37_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_37_PER",getAttribute("COLUMN_37_PER").getVal());
                //                rsItem.updateDouble("COLUMN_37_AMT",getAttribute("COLUMN_37_AMT").getVal());
                //                rsItem.updateString("COLUMN_37_CAPTION",(String)getAttribute("COLUMN_37_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_38_ID",(int)getAttribute("COLUMN_38_ID").getVal());
                //                rsItem.updateString("COLUMN_38_FORMULA",(String)getAttribute("COLUMN_38_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_38_PER",getAttribute("COLUMN_38_PER").getVal());
                //                rsItem.updateDouble("COLUMN_38_AMT",getAttribute("COLUMN_38_AMT").getVal());
                //                rsItem.updateString("COLUMN_38_CAPTION",(String)getAttribute("COLUMN_38_CAPTION").getObj());
                //
                //                rsItem.updateInt("COLUMN_39_ID",(int)getAttribute("COLUMN_39_ID").getVal());
                //                rsItem.updateString("COLUMN_39_FORMULA",(String)getAttribute("COLUMN_39_FORMULA").getObj());
                //                rsItem.updateDouble("COLUMN_39_PER",getAttribute("COLUMN_39_PER").getVal());
                //                rsItem.updateDouble("COLUMN_39_AMT",getAttribute("COLUMN_39_AMT").getVal());
                //                rsItem.updateString("COLUMN_39_CAPTION",(String)getAttribute("COLUMN_39_CAPTION").getObj());
                rsItem.updateString("MATERIAL_CODE", (String) ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsItem.updateString("MATERIAL_DESC", (String) ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsItem.updateString("QUALITY_NO", (String) ObjItem.getAttribute("QUALITY_NO").getObj());
                rsItem.updateString("PAGE_NO", (String) ObjItem.getAttribute("PAGE_NO").getObj());
                rsItem.updateDouble("EXCESS", ObjItem.getAttribute("EXCESS").getVal());
                rsItem.updateDouble("SHORTAGE", ObjItem.getAttribute("SHORTAGE").getVal());
                rsItem.updateDouble("CHALAN_QTY", ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsItem.updateString("L_F_NO", (String) ObjItem.getAttribute("L_F_NO").getObj());
                rsItem.updateString("BARCODE_TYPE", (String) ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsItem.updateBoolean("CHANGED", true);
                rsItem.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsItem.updateString("RND_DEDUCTION_REASON", ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", 1);
                rsHDetail.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
                rsHDetail.updateInt("SR_NO", i);
                rsHDetail.updateInt("GRN_TYPE", 1); //1 - General Material
                rsHDetail.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE", 1);
                rsHDetail.updateString("PO_NO", (String) ObjItem.getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("PO_SR_NO", (int) ObjItem.getAttribute("PO_SR_NO").getVal());
                rsHDetail.updateInt("PO_TYPE", (int) ObjItem.getAttribute("PO_TYPE").getVal());
                rsHDetail.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE", (String) ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("MIR_QTY", ObjItem.getAttribute("MIR_QTY").getVal());
                rsHDetail.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("EXCESS_QTY", ObjItem.getAttribute("EXCESS_QTY").getVal());
                rsHDetail.updateDouble("PO_QTY", ObjItem.getAttribute("PO_QTY").getVal());
                rsHDetail.updateDouble("RECEIVED_QTY", ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsHDetail.updateDouble("BALANCE_PO_QTY", ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID", (int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsHDetail.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateDouble("REJECTED_QTY", ObjItem.getAttribute("REJECTED_QTY").getVal());
                rsHDetail.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsHDetail.updateString("W_MIE", (String) ObjItem.getAttribute("W_MIE").getObj());
                rsHDetail.updateString("NO_CASE", (String) ObjItem.getAttribute("NO_CASE").getObj());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsHDetail.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsHDetail.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsHDetail.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsHDetail.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsHDetail.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsHDetail.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsHDetail.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsHDetail.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsHDetail.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsHDetail.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsHDetail.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsHDetail.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsHDetail.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsHDetail.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsHDetail.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_22_ID", (int) ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsHDetail.updateString("COLUMN_22_FORMULA", (String) ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_22_PER", ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsHDetail.updateDouble("COLUMN_22_AMT", ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsHDetail.updateString("COLUMN_22_CAPTION", (String) ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_23_ID", (int) ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsHDetail.updateString("COLUMN_23_FORMULA", (String) ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_23_PER", ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsHDetail.updateDouble("COLUMN_23_AMT", ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsHDetail.updateString("COLUMN_23_CAPTION", (String) ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_24_ID", (int) ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsHDetail.updateString("COLUMN_24_FORMULA", (String) ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_24_PER", ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsHDetail.updateDouble("COLUMN_24_AMT", ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsHDetail.updateString("COLUMN_24_CAPTION", (String) ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_25_ID", (int) ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsHDetail.updateString("COLUMN_25_FORMULA", (String) ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_25_PER", ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsHDetail.updateDouble("COLUMN_25_AMT", ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsHDetail.updateString("COLUMN_25_CAPTION", (String) ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_26_ID", (int) ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsHDetail.updateString("COLUMN_26_FORMULA", (String) ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_26_PER", ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsHDetail.updateDouble("COLUMN_26_AMT", ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsHDetail.updateString("COLUMN_26_CAPTION", (String) ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_27_ID", (int) ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsHDetail.updateString("COLUMN_27_FORMULA", (String) ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_27_PER", ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsHDetail.updateDouble("COLUMN_27_AMT", ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsHDetail.updateString("COLUMN_27_CAPTION", (String) ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_28_ID", (int) ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsHDetail.updateString("COLUMN_28_FORMULA", (String) ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_28_PER", ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsHDetail.updateDouble("COLUMN_28_AMT", ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsHDetail.updateString("COLUMN_28_CAPTION", (String) ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_29_ID", (int) ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsHDetail.updateString("COLUMN_29_FORMULA", (String) ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_29_PER", ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsHDetail.updateDouble("COLUMN_29_AMT", ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsHDetail.updateString("COLUMN_29_CAPTION", (String) ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_30_ID", (int) ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsHDetail.updateString("COLUMN_30_FORMULA", (String) ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_30_PER", ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsHDetail.updateDouble("COLUMN_30_AMT", ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsHDetail.updateString("COLUMN_30_CAPTION", (String) ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_31_ID", (int) ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsHDetail.updateString("COLUMN_31_FORMULA", (String) ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_31_PER", ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsHDetail.updateDouble("COLUMN_31_AMT", ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsHDetail.updateString("COLUMN_31_CAPTION", (String) ObjItem.getAttribute("COLUMN_31_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_32_ID", (int) ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsHDetail.updateString("COLUMN_32_FORMULA", (String) ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_32_PER", ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsHDetail.updateDouble("COLUMN_32_AMT", ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsHDetail.updateString("COLUMN_32_CAPTION", (String) ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_33_ID", (int) ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsHDetail.updateString("COLUMN_33_FORMULA", (String) ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_33_PER", ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsHDetail.updateDouble("COLUMN_33_AMT", ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsHDetail.updateString("COLUMN_33_CAPTION", (String) ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_34_ID", (int) ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsHDetail.updateString("COLUMN_34_FORMULA", (String) ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_34_PER", ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsHDetail.updateDouble("COLUMN_34_AMT", ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsHDetail.updateString("COLUMN_34_CAPTION", (String) ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_35_ID", (int) ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsHDetail.updateString("COLUMN_35_FORMULA", (String) ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_35_PER", ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsHDetail.updateDouble("COLUMN_35_AMT", ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsHDetail.updateString("COLUMN_35_CAPTION", (String) ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_36_ID", (int) ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsHDetail.updateString("COLUMN_36_FORMULA", (String) ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_36_PER", ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsHDetail.updateDouble("COLUMN_36_AMT", ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsHDetail.updateString("COLUMN_36_CAPTION", (String) ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_37_ID", (int) ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsHDetail.updateString("COLUMN_37_FORMULA", (String) ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_37_PER", ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsHDetail.updateDouble("COLUMN_37_AMT", ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsHDetail.updateString("COLUMN_37_CAPTION", (String) ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_38_ID", (int) ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsHDetail.updateString("COLUMN_38_FORMULA", (String) ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_38_PER", ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsHDetail.updateDouble("COLUMN_38_AMT", ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsHDetail.updateString("COLUMN_38_CAPTION", (String) ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_39_ID", (int) ObjItem.getAttribute("COLUMN_39_ID").getVal());
                rsHDetail.updateString("COLUMN_39_FORMULA", (String) ObjItem.getAttribute("COLUMN_39_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_39_PER", ObjItem.getAttribute("COLUMN_39_PER").getVal());
                rsHDetail.updateDouble("COLUMN_39_AMT", ObjItem.getAttribute("COLUMN_39_AMT").getVal());
                rsHDetail.updateString("COLUMN_39_CAPTION", (String) ObjItem.getAttribute("COLUMN_39_CAPTION").getObj());
                
                //                rsHDetail.updateInt("COLUMN_31_ID",(int)getAttribute("COLUMN_31_ID").getVal());
                //                rsHDetail.updateString("COLUMN_31_FORMULA",(String)getAttribute("COLUMN_31_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_31_PER",getAttribute("COLUMN_31_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_31_AMT",getAttribute("COLUMN_31_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_31_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_32_ID",(int)getAttribute("COLUMN_32_ID").getVal());
                //                rsHDetail.updateString("COLUMN_32_FORMULA",(String)getAttribute("COLUMN_32_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_32_PER",getAttribute("COLUMN_32_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_32_AMT",getAttribute("COLUMN_32_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_32_CAPTION",(String)getAttribute("COLUMN_32_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_33_ID",(int)getAttribute("COLUMN_33_ID").getVal());
                //                rsHDetail.updateString("COLUMN_33_FORMULA",(String)getAttribute("COLUMN_33_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_33_PER",getAttribute("COLUMN_33_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_33_AMT",getAttribute("COLUMN_33_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_33_CAPTION",(String)getAttribute("COLUMN_33_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_34_ID",(int)getAttribute("COLUMN_34_ID").getVal());
                //                rsHDetail.updateString("COLUMN_34_FORMULA",(String)getAttribute("COLUMN_34_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_34_PER",getAttribute("COLUMN_34_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_34_AMT",getAttribute("COLUMN_34_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_34_CAPTION",(String)getAttribute("COLUMN_34_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_35_ID",(int)getAttribute("COLUMN_35_ID").getVal());
                //                rsHDetail.updateString("COLUMN_35_FORMULA",(String)getAttribute("COLUMN_35_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_35_PER",getAttribute("COLUMN_35_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_35_AMT",getAttribute("COLUMN_35_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_35_CAPTION",(String)getAttribute("COLUMN_35_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_36_ID",(int)getAttribute("COLUMN_36_ID").getVal());
                //                rsHDetail.updateString("COLUMN_36_FORMULA",(String)getAttribute("COLUMN_36_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_36_PER",getAttribute("COLUMN_36_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_36_AMT",getAttribute("COLUMN_36_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_36_CAPTION",(String)getAttribute("COLUMN_36_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_37_ID",(int)getAttribute("COLUMN_37_ID").getVal());
                //                rsHDetail.updateString("COLUMN_37_FORMULA",(String)getAttribute("COLUMN_37_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_37_PER",getAttribute("COLUMN_37_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_37_AMT",getAttribute("COLUMN_37_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_37_CAPTION",(String)getAttribute("COLUMN_37_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_38_ID",(int)getAttribute("COLUMN_38_ID").getVal());
                //                rsHDetail.updateString("COLUMN_38_FORMULA",(String)getAttribute("COLUMN_38_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_38_PER",getAttribute("COLUMN_38_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_38_AMT",getAttribute("COLUMN_38_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_38_CAPTION",(String)getAttribute("COLUMN_38_CAPTION").getObj());
                //
                //                rsHDetail.updateInt("COLUMN_39_ID",(int)getAttribute("COLUMN_39_ID").getVal());
                //                rsHDetail.updateString("COLUMN_39_FORMULA",(String)getAttribute("COLUMN_39_FORMULA").getObj());
                //                rsHDetail.updateDouble("COLUMN_39_PER",getAttribute("COLUMN_39_PER").getVal());
                //                rsHDetail.updateDouble("COLUMN_39_AMT",getAttribute("COLUMN_39_AMT").getVal());
                //                rsHDetail.updateString("COLUMN_39_CAPTION",(String)getAttribute("COLUMN_39_CAPTION").getObj());
                //
                rsHDetail.updateString("MATERIAL_CODE", (String) ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsHDetail.updateString("MATERIAL_DESC", (String) ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsHDetail.updateString("QUALITY_NO", (String) ObjItem.getAttribute("QUALITY_NO").getObj());
                rsHDetail.updateString("PAGE_NO", (String) ObjItem.getAttribute("PAGE_NO").getObj());
                rsHDetail.updateDouble("EXCESS", ObjItem.getAttribute("EXCESS").getVal());
                rsHDetail.updateDouble("SHORTAGE", ObjItem.getAttribute("SHORTAGE").getVal());
                rsHDetail.updateDouble("CHALAN_QTY", ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsHDetail.updateString("L_F_NO", (String) ObjItem.getAttribute("L_F_NO").getObj());
                rsHDetail.updateString("BARCODE_TYPE", (String) ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("RND_DEDUCTION_REASON", ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsHDetail.insertRow();
            }
            
            //====== Now turn of HSN GRN Items ======
            ResultSet rsHSN;
            Statement stHSN;
            
            stHSN = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHSN = stHSN.executeQuery("SELECT * FROM D_INV_GRN_HSN WHERE GRN_NO='1'");
            
            for (int i = 1; i <= colHSNGRNPJVItems.size(); i++) {
                clsHSNGRNPJVItem ObjHSNItem = (clsHSNGRNPJVItem) colHSNGRNPJVItems.get(Integer.toString(i));
                rsHSN.moveToInsertRow();
                
                rsHSN.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
                rsHSN.updateString("GRN_DATE", (String) getAttribute("GRN_DATE").getObj());
                rsHSN.updateString("HSN_CODE", (String) ObjHSNItem.getAttribute("HSN_CODE").getObj());
                rsHSN.updateDouble("INVOICE_AMOUNT", ObjHSNItem.getAttribute("INVOICE_AMOUNT").getVal());
                rsHSN.updateDouble("INVOICE_CGST", ObjHSNItem.getAttribute("INVOICE_CGST").getVal());
                rsHSN.updateDouble("INVOICE_SGST", ObjHSNItem.getAttribute("INVOICE_SGST").getVal());
                rsHSN.updateDouble("INVOICE_IGST", ObjHSNItem.getAttribute("INVOICE_IGST").getVal());
                rsHSN.updateDouble("INVOICE_RCM", ObjHSNItem.getAttribute("INVOICE_RCM").getVal());
                rsHSN.updateDouble("INVOICE_COMPOSITION", ObjHSNItem.getAttribute("INVOICE_COMPOSITION").getVal());
                rsHSN.updateDouble("INVOICE_GST_COMP_CESS", ObjHSNItem.getAttribute("INVOICE_GST_COMP_CESS").getVal());
                rsHSN.updateDouble("RECEIVED_AMOUNT", ObjHSNItem.getAttribute("RECEIVED_AMOUNT").getVal());
                rsHSN.updateDouble("RECEIVED_CGST", ObjHSNItem.getAttribute("RECEIVED_CGST").getVal());
                rsHSN.updateDouble("RECEIVED_SGST", ObjHSNItem.getAttribute("RECEIVED_SGST").getVal());
                rsHSN.updateDouble("RECEIVED_IGST", ObjHSNItem.getAttribute("RECEIVED_IGST").getVal());
                rsHSN.updateDouble("RECEIVED_RCM", ObjHSNItem.getAttribute("RECEIVED_RCM").getVal());
                rsHSN.updateDouble("RECEIVED_COMPOSITION", ObjHSNItem.getAttribute("RECEIVED_COMPOSITION").getVal());
                rsHSN.updateDouble("RECEIVED_GST_COMP_CESS", ObjHSNItem.getAttribute("RECEIVED_GST_COMP_CESS").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_AMOUNT", ObjHSNItem.getAttribute("DEBIT_NOTE_AMOUNT").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_CGST", ObjHSNItem.getAttribute("DEBIT_NOTE_CGST").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_SGST", ObjHSNItem.getAttribute("DEBIT_NOTE_SGST").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_IGST", ObjHSNItem.getAttribute("DEBIT_NOTE_IGST").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_RCM", ObjHSNItem.getAttribute("DEBIT_NOTE_RCM").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_COMPOSITION", ObjHSNItem.getAttribute("DEBIT_NOTE_COMPOSITION").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_GST_COMP_CESS", ObjHSNItem.getAttribute("DEBIT_NOTE_GST_COMP_CESS").getVal());
                
                rsHSN.updateDouble("CGST_INPUT_CR_PER", ObjHSNItem.getAttribute("CGST_INPUT_CR_PER").getVal());
                rsHSN.updateDouble("CGST_INPUT_CR_AMT", ObjHSNItem.getAttribute("CGST_INPUT_CR_AMT").getVal());
                rsHSN.updateDouble("SGST_INPUT_CR_PER", ObjHSNItem.getAttribute("SGST_INPUT_CR_PER").getVal());
                rsHSN.updateDouble("SGST_INPUT_CR_AMT", ObjHSNItem.getAttribute("SGST_INPUT_CR_AMT").getVal());
                rsHSN.updateDouble("IGST_INPUT_CR_PER", ObjHSNItem.getAttribute("IGST_INPUT_CR_PER").getVal());
                rsHSN.updateDouble("IGST_INPUT_CR_AMT", ObjHSNItem.getAttribute("IGST_INPUT_CR_AMT").getVal());
                
                rsHSN.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHSN.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHSN.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHSN.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHSN.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsHSN.updateBoolean("APPROVED", false);
                rsHSN.updateString("APPROVED_DATE", "0000-00-00");
                rsHSN.updateBoolean("REJECTED", false);
                rsHSN.updateString("REJECTED_DATE", "0000-00-00");
                rsHSN.updateBoolean("CHANGED", true);
                rsHSN.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHSN.updateBoolean("CANCELLED", false);
                
                rsHSN.insertRow();
            }
            //=================================================
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = clsGRNGen.ModuleID; //GRN General Materials
            ObjFlow.DocNo = (String) getAttribute("GRN_NO").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_INV_GRN_HEADER";
            ObjFlow.IsCreator = true;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName = "GRN_NO";
            ObjFlow.UseSpecifiedURL = false;
            ObjFlow.DocDate = (String) getAttribute("GRN_DATE").getObj();
            
            if (ObjFlow.Status.equals("H")) {
                ObjFlow.Status = "A";
                ObjFlow.To = ObjFlow.From;
                ObjFlow.UpdateFlow();
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            //===============Accounts PJV Generation =============//
            try {
                
                if (AStatus.equals("F")) {
                    data.Execute("UPDATE D_INV_GRN_HSN SET APPROVED=1,APPROVED_DATE=CURDATE(),CANCELLED=0,REJECTED=0 WHERE GRN_NO='" + ObjFlow.DocNo + "' ");
                    
                    //---------for solving not post voucher----------//
                    String Qry = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND GRN_NO = '" + getAttribute("GRN_NO").getString() + "' ";
                    for (int i = 1; i <= 3; i++) {
                        if (!data.IsRecordExist(Qry, FinanceGlobal.FinURL)) {
                            
                            //                           // PostVoucher(getAttribute("GRN_NO").getString(), 1,"");
                            //                            String GRNCAPTION = data.getStringValueFromDB("SELECT DISTINCT COLUMN_3_CAPTION FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                            //                            System.out.print("" + GRNCAPTION);
                            //
                            //                            if (GRNCAPTION.equals("CGST")) {
                            //                                System.out.println("New EXCISE Based");
                            //                                PostVoucherNonGST(getAttribute("GRN_NO").getString(), 1, "");
                            //                            } else {
                            //                                System.out.println("OLd EXCISE Based");
                            //                                PostVoucher(getAttribute("GRN_NO").getString(), 1, "");
                            //                            }
                            
                            String GRNCAPTION = data.getStringValueFromDB("SELECT DISTINCT COLUMN_3_CAPTION FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                            System.out.print("" + GRNCAPTION);
                            
                            //double GST_value = data.getDoubleValueFromDB("SELECT  COALESCE(COLUMN_3_AMT,0)+COALESCE(COLUMN_4_AMT,0)+COALESCE(COLUMN_5_AMT,0)+COALESCE(COLUMN_6_AMT,0)+COALESCE(COLUMN_7_AMT,0)+COALESCE(COLUMN_8_AMT,0) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                            double GST_value = data.getDoubleValueFromDB("SELECT  SUM(COALESCE(INVOICE_AMOUNT,0)+COALESCE(INVOICE_CGST,0)+COALESCE(INVOICE_SGST,0)+COALESCE(INVOICE_IGST,0)+COALESCE(INVOICE_RCM,0)+COALESCE(INVOICE_COMPOSITION,0)+COALESCE(INVOICE_GST_COMP_CESS,0)) FROM D_INV_GRN_HSN WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");  //ADDED ON 14/09/2017
                            //double DN_PostAmt = data.getDoubleValueFromDB("SELECT  COALESCE(DB_NET_AMOUNT,0) FROM D_INV_GRN_HEADER WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                            double DN_PostAmt = data.getDoubleValueFromDB("SELECT  SUM(COALESCE(DEBIT_NOTE_AMOUNT,0)+COALESCE(DEBIT_NOTE_CGST,0)+COALESCE(DEBIT_NOTE_SGST,0)+COALESCE(DEBIT_NOTE_IGST,0)+COALESCE(DEBIT_NOTE_RCM,0)+COALESCE(DEBIT_NOTE_COMPOSITION,0)+COALESCE(DEBIT_NOTE_GST_COMP_CESS,0)) FROM D_INV_GRN_HSN WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                            
                            if (GRNCAPTION.equals("CGST")) {
                                
                                System.out.println("New EXCISE Based");
                                // PostVoucherNonGST(getAttribute("GRN_NO").getString(), 1,"");
                                if (GST_value > 0) {
                                    System.out.println("NEW GST > 0 : GST");
                                    //PostVoucherGST(getAttribute("GRN_NO").getString(), 1,"");
                                    PostVoucherHSNWiseGST(getAttribute("GRN_NO").getString(), 1, "");
                                    //PostDNGST((int) getAttribute("COMPANY_ID").getVal(), getAttribute("GRN_NO").getString(), getAttribute("GRN_DATE").getString());
                                    System.out.println("DEBIT_NOTE TOTAL AMOUNT : " + DN_PostAmt);
                                    if (DN_PostAmt > 5) {
                                        System.out.println("DEBIT_NOTE POSTING START");
                                        PostDNHSNGST((int) getAttribute("COMPANY_ID").getVal(), getAttribute("GRN_NO").getString(), getAttribute("GRN_DATE").getString());
                                        System.out.println("DEBIT_NOTE POSTING END");
                                    }
                                } else {
                                    System.out.println("NEW GST Not > 0 : Non GST");
                                    PostVoucherNonGST(getAttribute("GRN_NO").getString(), 1, "");
                                }
                                
                            } else {
                                System.out.println("OLd EXCISE Based");
                                PostVoucher(getAttribute("GRN_NO").getString(), 1, "");
                            }
                            
                        } else {
                            break;
                        }
                    }
                    //---------for solving not post voucher----------///
                    
                    //PostVoucher(getAttribute("GRN_NO").getString(), 1,"");
                    
                    //-------------ADDED ON 12/09/2017------------------------------------------
                    String Qry1 = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND GRN_NO = '" + getAttribute("GRN_NO").getString() + "' ";
                    if (!data.IsRecordExist(Qry1, FinanceGlobal.FinURL)) {
                        
                        if (chkPostVoucher != 0) { 
                        
                        data.Execute("UPDATE D_INV_GRN_HSN SET APPROVED=0,APPROVED_DATE='0000-00-00',CANCELLED=0,REJECTED=0 WHERE GRN_NO='" + ObjFlow.DocNo + "' ");
                        data.Execute("UPDATE D_INV_GRN_HEADER SET APPROVED=0,APPROVED_DATE='0000-00-00' WHERE GRN_NO='" + ObjFlow.DocNo + "' ");
                        data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE DOC_NO='" + ObjFlow.DocNo + "' AND MODULE_ID='"+clsGRNGen.ModuleID+"' AND STATUS='F'");
                        data.Execute("UPDATE D_INV_GRN_HEADER_H SET APPROVER_REMARKS='AUTO PJV POSTING FAILED',APPROVAL_STATUS='W' WHERE GRN_NO='" + ObjFlow.DocNo + "' AND APPROVAL_STATUS='F' ");
                        
                        JOptionPane.showMessageDialog(null, "PJV Posting Failed. GRN not Final Approved.");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Payment Type is CASH. PJV not be Posting.");
                        }
                    }
                    
                    //--------------------------------------------------------
                }
                
                // ========= Creation of RJN and NRGP from this GRN =============//
                if (!PostRJN()) {
                    JOptionPane.showMessageDialog(null, "Rejection Memo not created. Following error is occured\n" + ErrorMessages);
                } else {
                    if (RJNCreated && NRGPCreated) {
                        JOptionPane.showMessageDialog(null, "RJN No. " + RJNNo + " is created. \nNRGP No. " + NRGPNo + " is created");
                    }
                }
                //===============================================================//
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            //====================================================//
            
            MoveLast();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    private void RevertStockEffect() {
        Statement stItemMaster, stItem, stTmp;
        ResultSet rsItemMaster, rsItem, rsTmp;
        String strSQL = "", GRNNo = "", ItemID = "", BOENo = "", LotNo = "", WareHouseID = "", LocationID = "", MIRNo = "";
        int CompanyID = 0, MIRSrNo = 0;
        double Qty = 0, RejectedQty = 0;
        
        try {
            CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            GRNNo = (String) getAttribute("GRN_NO").getObj();
            
            strSQL = "SELECT COMPANY_ID,GRN_NO,SR_NO,WAREHOUSE_ID,LOCATION_ID,QTY,ITEM_ID,BOE_NO FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND GRN_NO='" + GRNNo + "' AND GRN_TYPE=1";
            
            stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while (!rsTmp.isAfterLast()) {
                
                ItemID = rsTmp.getString("ITEM_ID");
                BOENo = rsTmp.getString("BOE_NO");
                LotNo = "X";
                WareHouseID = rsTmp.getString("WAREHOUSE_ID");
                LocationID = rsTmp.getString("LOCATION_ID");
                Qty = rsTmp.getDouble("QTY");
                
                stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsItem = stItem.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID.trim() + "' AND BOE_NO='" + BOENo.trim() + "' AND LOT_NO='" + LotNo.trim() + "' AND WAREHOUSE_ID='" + WareHouseID.trim() + "' AND LOCATION_ID='" + LocationID.trim() + "'");
                rsItem.first();
                
                if (rsItem.getRow() > 0) {
                    rsItem.updateDouble("TOTAL_RECEIPT_QTY", rsItem.getDouble("TOTAL_RECEIPT_QTY") - Qty);
                    rsItem.updateDouble("ON_HAND_QTY", rsItem.getDouble("ON_HAND_QTY") - Qty);
                    rsItem.updateDouble("AVAILABLE_QTY", rsItem.getDouble("AVAILABLE_QTY") - Qty);
                    rsItem.updateBoolean("CHANGED", true);
                    rsItem.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsItem.updateRow();
                }
                
                stItemMaster = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rsItemMaster = stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID.trim() + "' AND CANCELLED=0 ");
                rsItemMaster.first();
                
                if (rsItemMaster.getRow() > 0) {
                    rsItemMaster.updateDouble("TOTAL_RECEIPT_QTY", rsItemMaster.getDouble("TOTAL_RECEIPT_QTY") - Qty);
                    rsItemMaster.updateDouble("ON_HAND_QTY", rsItemMaster.getDouble("ON_HAND_QTY") - Qty);
                    rsItemMaster.updateDouble("AVAILABLE_QTY", rsItemMaster.getDouble("AVAILABLE_QTY") - Qty);
                    rsItemMaster.updateBoolean("CHANGED", true);
                    rsItemMaster.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    rsItemMaster.updateRow();
                }
                
                rsTmp.next();
            }
            
            //Now give reverse effect to MIR Table
            strSQL = "SELECT * FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND GRN_NO='" + GRNNo + "' AND GRN_TYPE=1";
            
            stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTmp = stTmp.executeQuery(strSQL);
            rsTmp.first();
            
            while (!rsTmp.isAfterLast()) {
                MIRNo = rsTmp.getString("MIR_NO");
                MIRSrNo = rsTmp.getInt("MIR_SR_NO");
                Qty = rsTmp.getDouble("QTY");
                RejectedQty = rsTmp.getDouble("REJECTED_QTY");
                
                // Update GRN Received Qty
                data.Execute("UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY=GRN_RECD_QTY-" + Qty + ",BAL_QTY=QTY-GRN_RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo + "' AND SR_NO=" + MIRSrNo + " AND GRN_TYPE=1");
                rsTmp.next();
            }
        } catch (Exception e) {
        }
    }
    
    //Updates current record
    public boolean Update() {
        Statement stItem, stLot, stStock, stTmp, stItemMaster, stHistory, stHDetail;
        ResultSet rsItem, rsLot, rsStock, rsTmp, rsItemMaster, rsHistory, rsHDetail;
        Statement stIssue, stHeader;
        ResultSet rsIssue, rsHeader;
        String ItemID = "", LotNo = "", BOENo = "", BOESrNo = "", WareHouseID = "", LocationID = "", GRNNo = "", MIRNo = "", strSQL = "", BOEDate = "";
        int CompanyID = 0, MIRSrNo = 0;
        double Qty = 0, RejectedQty = 0, ToleranceLimit = 0;
        boolean Validate = true;
        
        try {
            
            String AStatus = (String) getAttribute("APPROVAL_STATUS").getObj();
            if (AStatus.equals("R")) {
                Validate = false;
            }
            
            Validate = true;
            //======= Check the requisition date ============//
            java.sql.Date FinFromDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinFromDateDB);
            java.sql.Date FinToDate = java.sql.Date.valueOf(EITLERPGLOBAL.FinToDateDB);
            java.sql.Date DocDate = java.sql.Date.valueOf((String) getAttribute("GRN_DATE").getObj());
            
            if ((DocDate.after(FinFromDate) || DocDate.compareTo(FinFromDate) == 0) && (DocDate.before(FinToDate) || DocDate.compareTo(FinToDate) == 0)) {
                //Withing the year
            } else {
                LastError = "Document date is not within financial year.";
                return false;
            }
            //=====================================================//
            
            if (getAttribute("SUPP_ID").getString().trim().equals("000000")) {
                if (getAttribute("PAYMENT_TYPE").getInt() != 1) {
                    LastError = "Supplier code is not valid";
                    return false;
                }
            }
            
            // ---- History Related Changes ------ //
            stHistory = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stHDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rsHistory = stHistory.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE GRN_NO='1'"); // '1' for restricting all data retrieval
            rsHistory.first();
            rsHDetail = stHDetail.executeQuery("SELECT * FROM D_INV_GRN_DETAIL_H WHERE GRN_NO='1'");
            rsHDetail.first();
            //------------------------------------//
            
            String theDocNo = (String) getAttribute("GRN_NO").getObj();
            //stHeader=Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //rsHeader=stHeader.executeQuery("SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND TRIM(GRN_NO)='"+theDocNo+"' AND GRN_TYPE=1");
            //rsHeader.first();
            
            ApprovalFlow ObjFlow = new ApprovalFlow();
            
            CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            GRNNo = (String) getAttribute("GRN_NO").getObj();
            
            if (Validate) {
                //=========== Check the Quantities entered against MIR.============= //
                for (int i = 1; i <= colGRNItems.size(); i++) {
                    clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                    MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                    MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                    
                    ToleranceLimit = clsItem.getToleranceLimit(CompanyID, (String) ObjItem.getAttribute("ITEM_ID").getObj());
                    
                    double MIRQty = 0;
                    double PrevQty = 0; //Previously Entered Qty against MIR
                    double CurrentQty = 0; //Currently entered Qty.
                    
                    if ((!MIRNo.trim().equals("")) && (MIRSrNo > 0)) //MIR No. entered
                    {
                        //Get the  MIR Qty.
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        strSQL = "SELECT QTY FROM D_INV_MIR_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo + "' AND SR_NO=" + MIRSrNo + " AND MIR_TYPE=1";
                        rsTmp = stTmp.executeQuery(strSQL);
                        rsTmp.first();
                        
                        if (rsTmp.getRow() > 0) {
                            MIRQty = rsTmp.getDouble("QTY");
                        }
                        
                        //Get Total Qty Entered in GRN Against this MIR No. - Exclude current GRN No.
                        PrevQty = 0;
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        strSQL = "SELECT SUM(QTY) AS SUMQTY FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo + "' AND MIR_SR_NO=" + MIRSrNo + " AND MIR_TYPE=1 AND NOT(GRN_NO='" + GRNNo + "' AND GRN_TYPE=1) AND GRN_NO NOT IN(SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE CANCELLED=1)";
                        rsTmp = stTmp.executeQuery(strSQL);
                        rsTmp.first();
                        
                        if (rsTmp.getRow() > 0) {
                            PrevQty = rsTmp.getDouble("SUMQTY");
                        }
                        
                        CurrentQty = ObjItem.getAttribute("QTY").getVal();
                        
                        /*if((CurrentQty+PrevQty-((PrevQty*ToleranceLimit)/100)) > MIRQty) //If total Qty exceeds MIR Qty. Do not allow
                         {
                         LastError="Total quantity entered "+(CurrentQty+PrevQty)+" exceeds MIR No. "+MIRNo+" Sr. No. "+MIRSrNo+" qty "+MIRQty+". Please verify the input.";
                         return false;
                         }*/
                    }
                }
                //============== MIR Checking Completed ====================//
                
                //=========== Check the Max Qty Level.============= //
                for (int i = 1; i <= colGRNItems.size(); i++) {
                    clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                    ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                    
                    double MaxQty = clsItem.getMaxQty(CompanyID, ItemID);
                    
                    if (MaxQty > 0) {
                        double CurrentQty = ObjItem.getAttribute("QTY").getVal();
                        double AvailableQty = clsItem.getAvailableQty(CompanyID, ItemID);
                        
                        //Get old entered qty and deduct it from Available Qty
                        stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        rsTmp = stTmp.executeQuery("SELECT QTY FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND GRN_NO='" + GRNNo + "' AND SR_NO=" + i + " AND GRN_TYPE=1");
                        rsTmp.first();
                        
                        if (rsTmp.getRow() > 0) {
                            AvailableQty = AvailableQty - rsTmp.getDouble("QTY");
                        }
                        
                        if ((CurrentQty + AvailableQty) > MaxQty) {
                            LastError = "Total Receipt Qty " + (CurrentQty + AvailableQty) + " exceeds Maximum limit " + MaxQty + " for Item " + ItemID;
                            return false;
                        }
                    }
                }
                //=================================================///
                
                // Update the Stock only after Final Approval //
                if (AStatus.equals("F")) {
                    //Give Reverse Effect to Stock
                    //RevertStockEffect();
                    
                    //-------- First Update the stock -------------//
                    for (int i = 1; i <= colGRNItems.size(); i++) {
                        clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                        
                        MIRNo = (String) ObjItem.getAttribute("MIR_NO").getObj();
                        MIRSrNo = (int) ObjItem.getAttribute("MIR_SR_NO").getVal();
                        Qty = ObjItem.getAttribute("QTY").getVal();
                        RejectedQty = ObjItem.getAttribute("REJECTED_QTY").getVal();
                        ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                        
                        // Update GRN Received Qty
                        data.Execute("UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY=GRN_RECD_QTY+" + Qty + ",BAL_QTY=QTY-GRN_RECD_QTY,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo.trim() + "' AND SR_NO=" + MIRSrNo + " AND MIR_TYPE=1");
                        data.Execute("UPDATE D_INV_MIR_HEADER SET CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + CompanyID + " AND MIR_NO='" + MIRNo.trim() + "'");
                        
                        if (clsItem.getMaintainStock(CompanyID, ItemID)) {
                            
                            BOENo = (String) ObjItem.getAttribute("BOE_NO").getObj();
                            ItemID = (String) ObjItem.getAttribute("ITEM_ID").getObj();
                            BOESrNo = (String) ObjItem.getAttribute("BOE_SR_NO").getObj();
                            BOEDate = (String) ObjItem.getAttribute("BOE_DATE").getObj();
                            WareHouseID = (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj();
                            LocationID = (String) ObjItem.getAttribute("LOCATION_ID").getObj();
                            
                            LotNo = "X"; //Fixed lot no. for general Item
                            
                            //========= New Code of Updating Issue Against the Item =========== //
                            double IssuedQty = 0;
                            Qty = ObjItem.getAttribute("QTY").getVal();
                            
                            rsIssue = data.getResult("SELECT A.ISSUE_NO,QTY,SR_NO,EXCESS_ISSUE_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND B.MIR_NO='" + MIRNo + "' AND B.MIR_SR_NO=" + MIRSrNo + " AND EXCESS_ISSUE_QTY>0 AND A.APPROVED=1 AND A.CANCELED=0 AND ITEM_CODE='" + ItemID + "'");
                            rsIssue.first();
                            
                            if (rsIssue.getRow() > 0) {
                                while (!rsIssue.isAfterLast()) {
                                    IssuedQty = rsIssue.getDouble("EXCESS_ISSUE_QTY");
                                    String IssueNo = rsIssue.getString("ISSUE_NO");
                                    int IssueSrNo = rsIssue.getInt("SR_NO");
                                    
                                    if (IssuedQty > Qty) {
                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (IssuedQty - Qty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                        Qty = 0;
                                    }
                                    
                                    if (IssuedQty == Qty) {
                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=0 WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                        Qty = 0;
                                    }
                                    
                                    if (IssuedQty < Qty) {
                                        data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (Qty - IssuedQty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                        Qty = Qty - IssuedQty;
                                    }
                                    
                                    rsIssue.next();
                                }
                            }
                            
                            if (Qty > 0) {
                                
                                rsIssue = data.getResult("SELECT A.ISSUE_NO,QTY,SR_NO,EXCESS_ISSUE_QTY FROM D_INV_ISSUE_HEADER A,D_INV_ISSUE_DETAIL B WHERE A.COMPANY_ID=B.COMPANY_ID AND A.ISSUE_NO=B.ISSUE_NO AND EXCESS_ISSUE_QTY>0 AND A.APPROVED=1 AND A.CANCELED=0 AND ITEM_CODE='" + ItemID + "'");
                                rsIssue.first();
                                
                                if (rsIssue.getRow() > 0) {
                                    while (!rsIssue.isAfterLast()) {
                                        IssuedQty = rsIssue.getDouble("EXCESS_ISSUE_QTY");
                                        String IssueNo = rsIssue.getString("ISSUE_NO");
                                        int IssueSrNo = rsIssue.getInt("SR_NO");
                                        
                                        if (IssuedQty > Qty) {
                                            data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (IssuedQty - Qty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                            Qty = 0;
                                        }
                                        
                                        if (IssuedQty == Qty) {
                                            data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=0 WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                            Qty = 0;
                                        }
                                        
                                        if (IssuedQty < Qty) {
                                            data.Execute("UPDATE D_INV_ISSUE_DETAIL SET EXCESS_ISSUE_QTY=EXCESS_ISSUE_QTY-" + (Qty - IssuedQty) + " WHERE ISSUE_NO='" + IssueNo + "' AND SR_NO=" + IssueSrNo);
                                            Qty = Qty - IssuedQty;
                                        }
                                        
                                        rsIssue.next();
                                    }
                                }
                                
                            }
                            // ================================================================ //
                            
                            double Rate = ObjItem.getAttribute("RATE").getVal();
                            
                            //Check that ITEM_ID+LOT_NO+BOE_NO entry exist in D_INV_ITEM_LOST_MASTER
                            stTmp = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            String tmp = "SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID.trim() + "' AND LOT_NO='" + LotNo.trim() + "' AND BOE_NO='" + BOENo.trim() + "' AND WAREHOUSE_ID='" + WareHouseID.trim() + "' AND LOCATION_ID='" + LocationID.trim() + "'";
                            rsTmp = stTmp.executeQuery(tmp);
                            //String tmp= "SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID="+CompanyID+" AND ITEM_ID='"+ItemID.trim()+"' AND LOT_NO='"+LotNo.trim()+"' AND BOE_NO='"+BOENo.trim()+"' AND WAREHOUSE_ID='"+WareHouseID.trim()+"' AND LOCATION_ID='"+LocationID.trim()+"'";
                            rsTmp.first();
                            
                            if (rsTmp.getRow() <= 0) //Entry does not exist. Create new entry
                            {
                                stStock = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                rsStock = stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER");
                                
                                //Insert the records. Opening Qty will be 0.
                                rsStock.moveToInsertRow();
                                rsStock.updateInt("COMPANY_ID", CompanyID);
                                rsStock.updateString("ITEM_ID", ItemID);
                                rsStock.updateString("BOE_NO", BOENo);
                                rsStock.updateString("LOT_NO", LotNo);
                                rsStock.updateString("BOE_SR_NO", BOESrNo);
                                rsStock.updateString("BOE_DATE", BOEDate);
                                rsStock.updateDouble("OPENING_QTY", 0);
                                rsStock.updateDouble("OPENING_RATE", 0);
                                rsStock.updateDouble("TOTAL_RECEIPT_QTY", Qty);
                                rsStock.updateDouble("TOTAL_ISSUED_QTY", 0);
                                rsStock.updateString("LAST_RECEIPT_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsStock.updateString("LAST_ISSUED_DATE", "0000-00-00");
                                
                                if (Rate <= 0) {
                                    rsStock.updateDouble("ZERO_RECEIPT_QTY", Qty);
                                    rsStock.updateDouble("ZERO_ISSUED_QTY", 0);
                                    rsStock.updateDouble("ZERO_VAL_QTY", Qty);
                                } else {
                                    rsStock.updateDouble("ZERO_RECEIPT_QTY", 0);
                                    rsStock.updateDouble("ZERO_ISSUED_QTY", 0);
                                    rsStock.updateDouble("ZERO_VAL_QTY", 0);
                                }
                                
                                rsStock.updateDouble("REJECTED_QTY", 0);
                                rsStock.updateDouble("ON_HAND_QTY", Qty);
                                rsStock.updateDouble("AVAILABLE_QTY", Qty);
                                rsStock.updateDouble("ALLOCATED_QTY", 0);
                                rsStock.updateString("WAREHOUSE_ID", WareHouseID);
                                rsStock.updateString("LOCATION_ID", LocationID);
                                rsStock.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                                rsStock.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                                rsStock.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                                rsStock.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                                rsStock.updateBoolean("CHANGED", true);
                                rsStock.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsStock.insertRow();
                            } else //Entry already exist. Update the stock values.
                            {
                                stStock = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                rsStock = stStock.executeQuery("SELECT * FROM D_INV_ITEM_LOT_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID.trim() + "' AND LOT_NO='" + LotNo.trim() + "' AND BOE_NO='" + BOENo.trim() + "' AND WAREHOUSE_ID='" + WareHouseID.trim() + "' AND LOCATION_ID='" + LocationID.trim() + "'");
                                rsStock.first(); //There will be a single record only
                                
                                double lnLotQty = ObjItem.getAttribute("QTY").getVal(); //Record current Qty
                                
                                //Now check the Issued made (with Excess Qty) in Issue Detail
                            /*stIssue=Conn.createStatement();
                                 rsIssue=stIssue.executeQuery("SELECT EXCESS_ISSUE_QTY AS EXCESS_ISSUE FROM D_INV_ISSUE_DETAIL WHERE D_INV_ISSUE_DETAIL.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MIR_NO='"+MIRNo+"' AND MIR_SR_NO="+MIRSrNo);
                                 rsIssue.first();
                                 if(rsIssue.getRow()>0) {
                                 double ExcessQty=rsIssue.getDouble("EXCESS_ISSUE");
                                 lnLotQty=lnLotQty-ExcessQty;
                                 }*/
                                //======================================================//
                                if (Rate <= 0) {
                                    rsStock.updateDouble("ZERO_RECEIPT_QTY", rsStock.getDouble("ZERO_RECEIPT_QTY") + Qty);
                                    rsStock.updateDouble("ZERO_VAL_QTY", rsStock.getDouble("ZERO_VAL_QTY") + Qty);
                                }
                                
                                rsStock.updateDouble("TOTAL_RECEIPT_QTY", rsStock.getDouble("TOTAL_RECEIPT_QTY") + Qty);
                                rsStock.updateString("LAST_RECEIPT_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsStock.updateDouble("ON_HAND_QTY", rsStock.getDouble("ON_HAND_QTY") + Qty);
                                rsStock.updateDouble("AVAILABLE_QTY", rsStock.getDouble("AVAILABLE_QTY") + Qty);
                                rsStock.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                                rsStock.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                                rsStock.updateBoolean("CHANGED", true);
                                rsStock.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsStock.updateRow();
                            }
                            
                            //======= Update the Item Master =========
                            stItemMaster = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            rsItemMaster = stItemMaster.executeQuery("SELECT * FROM D_INV_ITEM_MASTER WHERE COMPANY_ID=" + CompanyID + " AND ITEM_ID='" + ItemID + "' AND CANCELLED=0");
                            rsItemMaster.first();
                            
                            if (rsItemMaster.getRow() > 0) {
                                double lnQty = ObjItem.getAttribute("QTY").getVal();
                                rsItemMaster.updateDouble("TOTAL_RECEIPT_QTY", rsItemMaster.getDouble("TOTAL_RECEIPT_QTY") + Qty);
                                rsItemMaster.updateString("LAST_RECEIPT_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsItemMaster.updateDouble("ON_HAND_QTY", rsItemMaster.getDouble("ON_HAND_QTY") + Qty);
                                rsItemMaster.updateDouble("AVAILABLE_QTY", rsItemMaster.getDouble("AVAILABLE_QTY") + Qty);
                                rsItemMaster.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                                rsItemMaster.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                                rsItemMaster.updateBoolean("CHANGED", true);
                                rsItemMaster.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                                rsItemMaster.updateRow();
                            }
                            //======= Item Master stock updation completed ==========
                        }//If Condition
                    }//first for loop
                    //=============Updation of stock completed=========================//
                } //End of Approval Status if condition
            }
            String GRNDate = (String) getAttribute("GRN_DATE").getObj();
            rsResultSet.updateString("GRN_DATE", (String) getAttribute("GRN_DATE").getObj());
            rsResultSet.updateString("APPROVED_ON", (String) getAttribute("APPROVED_ON").getObj());
            rsResultSet.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsResultSet.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
            rsResultSet.updateString("REFA", (String) getAttribute("REFA").getObj());
            rsResultSet.updateString("CHALAN_NO", (String) getAttribute("CHALAN_NO").getObj());
            rsResultSet.updateString("CHALAN_DATE", (String) getAttribute("CHALAN_DATE").getObj());
            rsResultSet.updateString("LR_NO", (String) getAttribute("LR_NO").getObj());
            rsResultSet.updateString("LR_DATE", (String) getAttribute("LR_DATE").getObj());
            rsResultSet.updateString("INVOICE_NO", (String) getAttribute("INVOICE_NO").getObj());
            rsResultSet.updateString("INVOICE_DATE", (String) getAttribute("INVOICE_DATE").getObj());
            rsResultSet.updateDouble("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getDouble());
            rsResultSet.updateInt("TRANSPORTER", (int) getAttribute("TRANSPORTER").getVal());
            rsResultSet.updateString("TRANSPORTER_NAME", (String) getAttribute("TRANSPORTER_NAME").getObj());
            rsResultSet.updateString("GATEPASS_NO", (String) getAttribute("GATEPASS_NO").getObj());
            rsResultSet.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsResultSet.updateDouble("ACCESSABLE_VALUE", getAttribute("ACCESSABLE_VALUE").getVal());
            rsResultSet.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsResultSet.updateDouble("CURRENCY_RATE_PAYMENT", getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsResultSet.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsResultSet.updateDouble("GROSS_AMOUNT", getAttribute("GROSS_AMOUNT").getVal());
            rsResultSet.updateBoolean("GRN_PENDING", getAttribute("GRN_PENDING").getBool());
            rsResultSet.updateInt("GRN_PENDING_REASON", (int) getAttribute("GRN_PENDING_REASON").getVal());
            rsResultSet.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsResultSet.updateBoolean("CENVATED_ITEMS", getAttribute("CENVATED_ITEMS").getBool());
            rsResultSet.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsResultSet.updateInt("GRN_TYPE", (int) getAttribute("GRN_TYPE").getVal());
            rsResultSet.updateString("OPEN_STATUS", (String) getAttribute("OPEN_STATUS").getObj());
            rsResultSet.updateInt("FOR_STORE", (int) getAttribute("FOR_STORE").getVal());
            rsResultSet.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsResultSet.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsResultSet.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsResultSet.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            
            //Now Custom Columns
            rsResultSet.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsResultSet.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsResultSet.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsResultSet.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsResultSet.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsResultSet.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsResultSet.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsResultSet.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsResultSet.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsResultSet.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsResultSet.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsResultSet.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsResultSet.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsResultSet.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsResultSet.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsResultSet.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsResultSet.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsResultSet.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsResultSet.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsResultSet.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsResultSet.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsResultSet.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsResultSet.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsResultSet.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsResultSet.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsResultSet.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsResultSet.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsResultSet.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsResultSet.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsResultSet.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsResultSet.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsResultSet.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsResultSet.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsResultSet.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsResultSet.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsResultSet.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsResultSet.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsResultSet.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsResultSet.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsResultSet.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsResultSet.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsResultSet.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsResultSet.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsResultSet.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsResultSet.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsResultSet.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsResultSet.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsResultSet.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsResultSet.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsResultSet.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsResultSet.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsResultSet.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsResultSet.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsResultSet.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsResultSet.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsResultSet.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsResultSet.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsResultSet.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsResultSet.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsResultSet.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsResultSet.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsResultSet.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsResultSet.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsResultSet.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsResultSet.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_22_ID", (int) getAttribute("COLUMN_22_ID").getVal());
            rsResultSet.updateString("COLUMN_22_FORMULA", (String) getAttribute("COLUMN_22_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_22_PER", getAttribute("COLUMN_22_PER").getVal());
            rsResultSet.updateDouble("COLUMN_22_AMT", getAttribute("COLUMN_22_AMT").getVal());
            rsResultSet.updateString("COLUMN_22_CAPTION", (String) getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_23_ID", (int) getAttribute("COLUMN_23_ID").getVal());
            rsResultSet.updateString("COLUMN_23_FORMULA", (String) getAttribute("COLUMN_23_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_23_PER", getAttribute("COLUMN_23_PER").getVal());
            rsResultSet.updateDouble("COLUMN_23_AMT", getAttribute("COLUMN_23_AMT").getVal());
            rsResultSet.updateString("COLUMN_23_CAPTION", (String) getAttribute("COLUMN_23_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_24_ID", (int) getAttribute("COLUMN_24_ID").getVal());
            rsResultSet.updateString("COLUMN_24_FORMULA", (String) getAttribute("COLUMN_24_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_24_PER", getAttribute("COLUMN_24_PER").getVal());
            rsResultSet.updateDouble("COLUMN_24_AMT", getAttribute("COLUMN_24_AMT").getVal());
            rsResultSet.updateString("COLUMN_24_CAPTION", (String) getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_25_ID", (int) getAttribute("COLUMN_25_ID").getVal());
            rsResultSet.updateString("COLUMN_25_FORMULA", (String) getAttribute("COLUMN_25_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_25_PER", getAttribute("COLUMN_25_PER").getVal());
            rsResultSet.updateDouble("COLUMN_25_AMT", getAttribute("COLUMN_25_AMT").getVal());
            rsResultSet.updateString("COLUMN_25_CAPTION", (String) getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_26_ID", (int) getAttribute("COLUMN_26_ID").getVal());
            rsResultSet.updateString("COLUMN_26_FORMULA", (String) getAttribute("COLUMN_26_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_26_PER", getAttribute("COLUMN_26_PER").getVal());
            rsResultSet.updateDouble("COLUMN_26_AMT", getAttribute("COLUMN_26_AMT").getVal());
            rsResultSet.updateString("COLUMN_26_CAPTION", (String) getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_27_ID", (int) getAttribute("COLUMN_27_ID").getVal());
            rsResultSet.updateString("COLUMN_27_FORMULA", (String) getAttribute("COLUMN_27_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_27_PER", getAttribute("COLUMN_27_PER").getVal());
            rsResultSet.updateDouble("COLUMN_27_AMT", getAttribute("COLUMN_27_AMT").getVal());
            rsResultSet.updateString("COLUMN_27_CAPTION", (String) getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_28_ID", (int) getAttribute("COLUMN_28_ID").getVal());
            rsResultSet.updateString("COLUMN_28_FORMULA", (String) getAttribute("COLUMN_28_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_28_PER", getAttribute("COLUMN_28_PER").getVal());
            rsResultSet.updateDouble("COLUMN_28_AMT", getAttribute("COLUMN_28_AMT").getVal());
            rsResultSet.updateString("COLUMN_28_CAPTION", (String) getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_29_ID", (int) getAttribute("COLUMN_29_ID").getVal());
            rsResultSet.updateString("COLUMN_29_FORMULA", (String) getAttribute("COLUMN_29_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_29_PER", getAttribute("COLUMN_29_PER").getVal());
            rsResultSet.updateDouble("COLUMN_29_AMT", getAttribute("COLUMN_29_AMT").getVal());
            rsResultSet.updateString("COLUMN_29_CAPTION", (String) getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsResultSet.updateInt("COLUMN_30_ID", (int) getAttribute("COLUMN_30_ID").getVal());
            rsResultSet.updateString("COLUMN_30_FORMULA", (String) getAttribute("COLUMN_30_FORMULA").getObj());
            rsResultSet.updateDouble("COLUMN_30_PER", getAttribute("COLUMN_30_PER").getVal());
            rsResultSet.updateDouble("COLUMN_30_AMT", getAttribute("COLUMN_30_AMT").getVal());
            rsResultSet.updateString("COLUMN_30_CAPTION", (String) getAttribute("COLUMN_30_CAPTION").getObj());
            
            //            rsResultSet.updateDouble("INV_GROSS_AMT",(Double)getAttribute("INV_GROSS_AMT").getObj());
            //            rsResultSet.updateDouble("INV_NET_AMT",(Double)getAttribute("INV_NET_AMT").getObj());
            //            rsResultSet.updateDouble("INV_FINAL_AMT",(Double)getAttribute("INV_FINAL_AMT").getObj());
            //            rsResultSet.updateDouble("INV_INVOICE_AMT",(Double)getAttribute("INV_INVOICE_AMT").getObj());
            //            rsResultSet.updateDouble("INV_CGST",(Double)getAttribute("INV_CGST").getObj());
            //            rsResultSet.updateDouble("INV_SGST",(Double)getAttribute("INV_SGST").getObj());
            //            rsResultSet.updateDouble("INV_IGST",(Double)getAttribute("INV_IGST").getObj());
            //            rsResultSet.updateDouble("INV_RCM",(Double)getAttribute("INV_RCM").getObj());
            //            rsResultSet.updateDouble("INV_COMPOSITION",(Double)getAttribute("INV_COMPOSITION").getObj());
            //            rsResultSet.updateDouble("INV_GST_COMPENSATION_CESS",(Double)getAttribute("INV_GST_COMPENSATION_CESS").getObj());
            //
            //            rsResultSet.updateDouble("RCVD_GROSS_AMT",(Double)getAttribute("RCVD_GROSS_AMT").getObj());
            //            rsResultSet.updateDouble("RCVD_NET_AMT",(Double)getAttribute("RCVD_NET_AMT").getObj());
            //            rsResultSet.updateDouble("RCVD_FINAL_AMT",(Double)getAttribute("RCVD_FINAL_AMT").getObj());
            //            rsResultSet.updateDouble("RCVD_INVOICE_AMT",(Double)getAttribute("RCVD_INVOICE_AMT").getObj());
            //            rsResultSet.updateDouble("RCVD_CGST",(Double)getAttribute("RCVD_CGST").getObj());
            //            rsResultSet.updateDouble("RCVD_SGST",(Double)getAttribute("RCVD_SGST").getObj());
            //            rsResultSet.updateDouble("RCVD_IGST",(Double)getAttribute("RCVD_IGST").getObj());
            //            rsResultSet.updateDouble("RCVD_RCM",(Double)getAttribute("RCVD_RCM").getObj());
            //            rsResultSet.updateDouble("RCVD_COMPOSITION",(Double)getAttribute("RCVD_COMPOSITION").getObj());
            //            rsResultSet.updateDouble("RCVD_GST_COMPENSATION_CESS",(Double)getAttribute("RCVD_GST_COMPENSATION_CESS").getObj());
            //
            //            rsResultSet.updateDouble("DB_GROSS_AMT",(Double)getAttribute("DB_GROSS_AMT").getObj());
            //            rsResultSet.updateDouble("DB_NET_AMT",(Double)getAttribute("DB_NET_AMT").getObj());
            //            rsResultSet.updateDouble("DB_FINAL_AMT",(Double)getAttribute("DB_FINAL_AMT").getObj());
            //            rsResultSet.updateDouble("DB_INVOICE_AMT",(Double)getAttribute("DB_INVOICE_AMT").getObj());
            //            rsResultSet.updateDouble("DB_CGST",(Double)getAttribute("DB_CGST").getObj());
            //            rsResultSet.updateDouble("DB_SGST",(Double)getAttribute("DB_SGST").getObj());
            //            rsResultSet.updateDouble("DB_IGST",(Double)getAttribute("DB_IGST").getObj());
            //            rsResultSet.updateDouble("DB_RCM",(Double)getAttribute("DB_RCM").getObj());
            //            rsResultSet.updateDouble("DB_COMPOSITION",(Double)getAttribute("DB_COMPOSITION").getObj());
            //            rsResultSet.updateDouble("DB_GST_COMPENSATION_CESS",(Double)getAttribute("DB_GST_COMPENSATION_CESS").getObj());
            //
            
            //
            //            System.out.println(" DB_GROSS_AMT : "+getAttribute("DB_GROSS_AMT").getVal());
            //            System.out.println(" DB_NET_AMT : "+getAttribute("DB_NET_AMT").getVal());
            //            System.out.println(" DB_FINAL_AMT : "+getAttribute("DB_FINAL_AMT").getVal());
            //            System.out.println(" DB_INVOICE_AMT : "+getAttribute("DB_INVOICE_AMT").getVal());
            //            System.out.println(" DB_CGST : "+getAttribute("DB_CGST").getVal());
            //            System.out.println(" DB_SGST : "+getAttribute("DB_SGST").getVal());
            //            System.out.println(" DB_IGST : "+getAttribute("DB_IGST").getVal());
            //            System.out.println(" DB_RCM : "+getAttribute("DB_RCM").getVal());
            //            System.out.println(" DB_COMPOSITION : "+getAttribute("DB_COMPOSITION").getVal());
            //            System.out.println(" DB_GST_COMPENSATION_CESS : "+getAttribute("DB_GST_COMPENSATION_CESS").getVal());
            
            rsResultSet.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsResultSet.updateBoolean("CHANGED", true);
            rsResultSet.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsResultSet.updateBoolean("CANCELLED", false);
            
            rsResultSet.updateInt("PF_POST", getAttribute("PF_POST").getInt());
            rsResultSet.updateInt("FREIGHT_POST", getAttribute("FREIGHT_POST").getInt());
            rsResultSet.updateInt("OCTROI_POST", getAttribute("OCTROI_POST").getInt());
            rsResultSet.updateInt("INSURANCE_POST", getAttribute("INSURANCE_POST").getInt());
            rsResultSet.updateInt("CLEARANCE_POST", getAttribute("CLEARANCE_POST").getInt());
            rsResultSet.updateInt("AIR_FREIGHT_POST", getAttribute("AIR_FREIGHT_POST").getInt());
            rsResultSet.updateInt("OTHERS_POST", getAttribute("OTHERS_POST").getInt());
            rsResultSet.updateInt("PAYMENT_TYPE", getAttribute("PAYMENT_TYPE").getInt());
            rsResultSet.updateRow();
            
            //========= Inserting Into History =================//
            //Get the Maximum Revision No.
            int RevNo = EITLERPGLOBAL.GetQueryResult("SELECT MAX(REVISION_NO) FROM D_INV_GRN_HEADER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND GRN_NO='" + (String) getAttribute("GRN_NO").getObj() + "' AND GRN_TYPE=1");
            RevNo++;
            String RevDocNo = (String) getAttribute("GRN_NO").getObj();
            
            rsHistory.moveToInsertRow();
            rsHistory.updateInt("REVISION_NO", RevNo); //Revision No. 1 in case of Insert
            rsHistory.updateInt("UPDATED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("APPROVAL_STATUS", (String) getAttribute("APPROVAL_STATUS").getObj());
            rsHistory.updateString("ENTRY_DATE", EITLERPGLOBAL.getCurrentDateDB());
            rsHistory.updateString("APPROVER_REMARKS", (String) getAttribute("FROM_REMARKS").getObj());
            rsHistory.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
            rsHistory.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
            rsHistory.updateString("GRN_DATE", (String) getAttribute("GRN_DATE").getObj());
            rsHistory.updateString("APPROVED_ON", (String) getAttribute("APPROVED_ON").getObj());
            rsHistory.updateString("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
            rsHistory.updateString("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
            rsHistory.updateString("REFA", (String) getAttribute("REFA").getObj());
            rsHistory.updateString("CHALAN_NO", (String) getAttribute("CHALAN_NO").getObj());
            rsHistory.updateString("CHALAN_DATE", (String) getAttribute("CHALAN_DATE").getObj());
            rsHistory.updateString("LR_NO", (String) getAttribute("LR_NO").getObj());
            rsHistory.updateString("LR_DATE", (String) getAttribute("LR_DATE").getObj());
            rsHistory.updateString("INVOICE_NO", (String) getAttribute("INVOICE_NO").getObj());
            rsHistory.updateString("INVOICE_DATE", (String) getAttribute("INVOICE_DATE").getObj());
            rsHistory.updateDouble("INVOICE_AMOUNT", getAttribute("INVOICE_AMOUNT").getDouble());
            rsHistory.updateInt("TRANSPORTER", (int) getAttribute("TRANSPORTER").getVal());
            rsHistory.updateString("TRANSPORTER_NAME", (String) getAttribute("TRANSPORTER_NAME").getObj());
            rsHistory.updateString("GATEPASS_NO", (String) getAttribute("GATEPASS_NO").getObj());
            rsHistory.updateBoolean("IMPORT_CONCESS", getAttribute("IMPORT_CONCESS").getBool());
            rsHistory.updateDouble("ACCESSABLE_VALUE", getAttribute("ACCESSABLE_VALUE").getVal());
            rsHistory.updateInt("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
            rsHistory.updateDouble("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
            rsHistory.updateDouble("CURRENCY_RATE_PAYMENT", getAttribute("CURRENCY_RATE_PAYMENT").getVal());
            rsHistory.updateDouble("TOTAL_AMOUNT", getAttribute("TOTAL_AMOUNT").getVal());
            rsHistory.updateDouble("GROSS_AMOUNT", getAttribute("GROSS_AMOUNT").getVal());
            rsHistory.updateBoolean("GRN_PENDING", getAttribute("GRN_PENDING").getBool());
            rsHistory.updateInt("GRN_PENDING_REASON", (int) getAttribute("GRN_PENDING_REASON").getVal());
            rsHistory.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
            rsHistory.updateBoolean("CENVATED_ITEMS", getAttribute("CENVATED_ITEMS").getBool());
            rsHistory.updateBoolean("CANCELLED", getAttribute("CANCELLED").getBool());
            rsHistory.updateInt("GRN_TYPE", (int) getAttribute("GRN_TYPE").getVal());
            rsHistory.updateString("OPEN_STATUS", (String) getAttribute("OPEN_STATUS").getObj());
            rsHistory.updateInt("FOR_STORE", (int) getAttribute("FOR_STORE").getVal());
            rsHistory.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
            rsHistory.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
            rsHistory.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
            rsHistory.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
            rsHistory.updateInt("COLUMN_1_ID", (int) getAttribute("COLUMN_1_ID").getVal());
            rsHistory.updateString("COLUMN_1_FORMULA", (String) getAttribute("COLUMN_1_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_1_PER", getAttribute("COLUMN_1_PER").getVal());
            rsHistory.updateDouble("COLUMN_1_AMT", getAttribute("COLUMN_1_AMT").getVal());
            rsHistory.updateString("COLUMN_1_CAPTION", (String) getAttribute("COLUMN_1_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_2_ID", (int) getAttribute("COLUMN_2_ID").getVal());
            rsHistory.updateString("COLUMN_2_FORMULA", (String) getAttribute("COLUMN_2_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_2_PER", getAttribute("COLUMN_2_PER").getVal());
            rsHistory.updateDouble("COLUMN_2_AMT", getAttribute("COLUMN_2_AMT").getVal());
            rsHistory.updateString("COLUMN_2_CAPTION", (String) getAttribute("COLUMN_2_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_3_ID", (int) getAttribute("COLUMN_3_ID").getVal());
            rsHistory.updateString("COLUMN_3_FORMULA", (String) getAttribute("COLUMN_3_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_3_PER", getAttribute("COLUMN_3_PER").getVal());
            rsHistory.updateDouble("COLUMN_3_AMT", getAttribute("COLUMN_3_AMT").getVal());
            rsHistory.updateString("COLUMN_3_CAPTION", (String) getAttribute("COLUMN_3_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_4_ID", (int) getAttribute("COLUMN_4_ID").getVal());
            rsHistory.updateString("COLUMN_4_FORMULA", (String) getAttribute("COLUMN_4_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_4_PER", getAttribute("COLUMN_4_PER").getVal());
            rsHistory.updateDouble("COLUMN_4_AMT", getAttribute("COLUMN_4_AMT").getVal());
            rsHistory.updateString("COLUMN_4_CAPTION", (String) getAttribute("COLUMN_4_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_5_ID", (int) getAttribute("COLUMN_5_ID").getVal());
            rsHistory.updateString("COLUMN_5_FORMULA", (String) getAttribute("COLUMN_5_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_5_PER", getAttribute("COLUMN_5_PER").getVal());
            rsHistory.updateDouble("COLUMN_5_AMT", getAttribute("COLUMN_5_AMT").getVal());
            rsHistory.updateString("COLUMN_5_CAPTION", (String) getAttribute("COLUMN_5_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_6_ID", (int) getAttribute("COLUMN_6_ID").getVal());
            rsHistory.updateString("COLUMN_6_FORMULA", (String) getAttribute("COLUMN_6_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_6_PER", getAttribute("COLUMN_6_PER").getVal());
            rsHistory.updateDouble("COLUMN_6_AMT", getAttribute("COLUMN_6_AMT").getVal());
            rsHistory.updateString("COLUMN_6_CAPTION", (String) getAttribute("COLUMN_6_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_7_ID", (int) getAttribute("COLUMN_7_ID").getVal());
            rsHistory.updateString("COLUMN_7_FORMULA", (String) getAttribute("COLUMN_7_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_7_PER", getAttribute("COLUMN_7_PER").getVal());
            rsHistory.updateDouble("COLUMN_7_AMT", getAttribute("COLUMN_7_AMT").getVal());
            rsHistory.updateString("COLUMN_7_CAPTION", (String) getAttribute("COLUMN_7_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_8_ID", (int) getAttribute("COLUMN_8_ID").getVal());
            rsHistory.updateString("COLUMN_8_FORMULA", (String) getAttribute("COLUMN_8_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_8_PER", getAttribute("COLUMN_8_PER").getVal());
            rsHistory.updateDouble("COLUMN_8_AMT", getAttribute("COLUMN_8_AMT").getVal());
            rsHistory.updateString("COLUMN_8_CAPTION", (String) getAttribute("COLUMN_8_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_9_ID", (int) getAttribute("COLUMN_9_ID").getVal());
            rsHistory.updateString("COLUMN_9_FORMULA", (String) getAttribute("COLUMN_9_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_9_PER", getAttribute("COLUMN_9_PER").getVal());
            rsHistory.updateDouble("COLUMN_9_AMT", getAttribute("COLUMN_9_AMT").getVal());
            rsHistory.updateString("COLUMN_9_CAPTION", (String) getAttribute("COLUMN_9_CAPTION").getObj());
            rsHistory.updateInt("COLUMN_10_ID", (int) getAttribute("COLUMN_10_ID").getVal());
            rsHistory.updateString("COLUMN_10_FORMULA", (String) getAttribute("COLUMN_10_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_10_PER", getAttribute("COLUMN_10_PER").getVal());
            rsHistory.updateDouble("COLUMN_10_AMT", getAttribute("COLUMN_10_AMT").getVal());
            rsHistory.updateString("COLUMN_10_CAPTION", (String) getAttribute("COLUMN_10_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_11_ID", (int) getAttribute("COLUMN_11_ID").getVal());
            rsHistory.updateString("COLUMN_11_FORMULA", (String) getAttribute("COLUMN_11_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_11_PER", getAttribute("COLUMN_11_PER").getVal());
            rsHistory.updateDouble("COLUMN_11_AMT", getAttribute("COLUMN_11_AMT").getVal());
            rsHistory.updateString("COLUMN_11_CAPTION", (String) getAttribute("COLUMN_11_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_12_ID", (int) getAttribute("COLUMN_12_ID").getVal());
            rsHistory.updateString("COLUMN_12_FORMULA", (String) getAttribute("COLUMN_12_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_12_PER", getAttribute("COLUMN_12_PER").getVal());
            rsHistory.updateDouble("COLUMN_12_AMT", getAttribute("COLUMN_12_AMT").getVal());
            rsHistory.updateString("COLUMN_12_CAPTION", (String) getAttribute("COLUMN_12_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_13_ID", (int) getAttribute("COLUMN_13_ID").getVal());
            rsHistory.updateString("COLUMN_13_FORMULA", (String) getAttribute("COLUMN_13_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_13_PER", getAttribute("COLUMN_13_PER").getVal());
            rsHistory.updateDouble("COLUMN_13_AMT", getAttribute("COLUMN_13_AMT").getVal());
            rsHistory.updateString("COLUMN_13_CAPTION", (String) getAttribute("COLUMN_13_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_14_ID", (int) getAttribute("COLUMN_14_ID").getVal());
            rsHistory.updateString("COLUMN_14_FORMULA", (String) getAttribute("COLUMN_14_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_14_PER", getAttribute("COLUMN_14_PER").getVal());
            rsHistory.updateDouble("COLUMN_14_AMT", getAttribute("COLUMN_14_AMT").getVal());
            rsHistory.updateString("COLUMN_14_CAPTION", (String) getAttribute("COLUMN_14_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_15_ID", (int) getAttribute("COLUMN_15_ID").getVal());
            rsHistory.updateString("COLUMN_15_FORMULA", (String) getAttribute("COLUMN_15_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_15_PER", getAttribute("COLUMN_15_PER").getVal());
            rsHistory.updateDouble("COLUMN_15_AMT", getAttribute("COLUMN_15_AMT").getVal());
            rsHistory.updateString("COLUMN_15_CAPTION", (String) getAttribute("COLUMN_15_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_16_ID", (int) getAttribute("COLUMN_16_ID").getVal());
            rsHistory.updateString("COLUMN_16_FORMULA", (String) getAttribute("COLUMN_16_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_16_PER", getAttribute("COLUMN_16_PER").getVal());
            rsHistory.updateDouble("COLUMN_16_AMT", getAttribute("COLUMN_16_AMT").getVal());
            rsHistory.updateString("COLUMN_16_CAPTION", (String) getAttribute("COLUMN_16_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_17_ID", (int) getAttribute("COLUMN_17_ID").getVal());
            rsHistory.updateString("COLUMN_17_FORMULA", (String) getAttribute("COLUMN_17_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_17_PER", getAttribute("COLUMN_17_PER").getVal());
            rsHistory.updateDouble("COLUMN_17_AMT", getAttribute("COLUMN_17_AMT").getVal());
            rsHistory.updateString("COLUMN_17_CAPTION", (String) getAttribute("COLUMN_17_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_18_ID", (int) getAttribute("COLUMN_18_ID").getVal());
            rsHistory.updateString("COLUMN_18_FORMULA", (String) getAttribute("COLUMN_18_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_18_PER", getAttribute("COLUMN_18_PER").getVal());
            rsHistory.updateDouble("COLUMN_18_AMT", getAttribute("COLUMN_18_AMT").getVal());
            rsHistory.updateString("COLUMN_18_CAPTION", (String) getAttribute("COLUMN_18_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_19_ID", (int) getAttribute("COLUMN_19_ID").getVal());
            rsHistory.updateString("COLUMN_19_FORMULA", (String) getAttribute("COLUMN_19_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_19_PER", getAttribute("COLUMN_19_PER").getVal());
            rsHistory.updateDouble("COLUMN_19_AMT", getAttribute("COLUMN_19_AMT").getVal());
            rsHistory.updateString("COLUMN_19_CAPTION", (String) getAttribute("COLUMN_19_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_20_ID", (int) getAttribute("COLUMN_20_ID").getVal());
            rsHistory.updateString("COLUMN_20_FORMULA", (String) getAttribute("COLUMN_20_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_20_PER", getAttribute("COLUMN_20_PER").getVal());
            rsHistory.updateDouble("COLUMN_20_AMT", getAttribute("COLUMN_20_AMT").getVal());
            rsHistory.updateString("COLUMN_20_CAPTION", (String) getAttribute("COLUMN_20_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_21_ID", (int) getAttribute("COLUMN_21_ID").getVal());
            rsHistory.updateString("COLUMN_21_FORMULA", (String) getAttribute("COLUMN_21_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_21_PER", getAttribute("COLUMN_21_PER").getVal());
            rsHistory.updateDouble("COLUMN_21_AMT", getAttribute("COLUMN_21_AMT").getVal());
            rsHistory.updateString("COLUMN_21_CAPTION", (String) getAttribute("COLUMN_21_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_22_ID", (int) getAttribute("COLUMN_22_ID").getVal());
            rsHistory.updateString("COLUMN_22_FORMULA", (String) getAttribute("COLUMN_22_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_22_PER", getAttribute("COLUMN_22_PER").getVal());
            rsHistory.updateDouble("COLUMN_22_AMT", getAttribute("COLUMN_22_AMT").getVal());
            rsHistory.updateString("COLUMN_22_CAPTION", (String) getAttribute("COLUMN_22_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_23_ID", (int) getAttribute("COLUMN_23_ID").getVal());
            rsHistory.updateString("COLUMN_23_FORMULA", (String) getAttribute("COLUMN_23_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_23_PER", getAttribute("COLUMN_23_PER").getVal());
            rsHistory.updateDouble("COLUMN_23_AMT", getAttribute("COLUMN_23_AMT").getVal());
            rsHistory.updateString("COLUMN_23_CAPTION", (String) getAttribute("COLUMN_23_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_24_ID", (int) getAttribute("COLUMN_24_ID").getVal());
            rsHistory.updateString("COLUMN_24_FORMULA", (String) getAttribute("COLUMN_24_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_24_PER", getAttribute("COLUMN_24_PER").getVal());
            rsHistory.updateDouble("COLUMN_24_AMT", getAttribute("COLUMN_24_AMT").getVal());
            rsHistory.updateString("COLUMN_24_CAPTION", (String) getAttribute("COLUMN_24_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_25_ID", (int) getAttribute("COLUMN_25_ID").getVal());
            rsHistory.updateString("COLUMN_25_FORMULA", (String) getAttribute("COLUMN_25_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_25_PER", getAttribute("COLUMN_25_PER").getVal());
            rsHistory.updateDouble("COLUMN_25_AMT", getAttribute("COLUMN_25_AMT").getVal());
            rsHistory.updateString("COLUMN_25_CAPTION", (String) getAttribute("COLUMN_25_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_26_ID", (int) getAttribute("COLUMN_26_ID").getVal());
            rsHistory.updateString("COLUMN_26_FORMULA", (String) getAttribute("COLUMN_26_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_26_PER", getAttribute("COLUMN_26_PER").getVal());
            rsHistory.updateDouble("COLUMN_26_AMT", getAttribute("COLUMN_26_AMT").getVal());
            rsHistory.updateString("COLUMN_26_CAPTION", (String) getAttribute("COLUMN_26_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_27_ID", (int) getAttribute("COLUMN_27_ID").getVal());
            rsHistory.updateString("COLUMN_27_FORMULA", (String) getAttribute("COLUMN_27_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_27_PER", getAttribute("COLUMN_27_PER").getVal());
            rsHistory.updateDouble("COLUMN_27_AMT", getAttribute("COLUMN_27_AMT").getVal());
            rsHistory.updateString("COLUMN_27_CAPTION", (String) getAttribute("COLUMN_27_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_28_ID", (int) getAttribute("COLUMN_28_ID").getVal());
            rsHistory.updateString("COLUMN_28_FORMULA", (String) getAttribute("COLUMN_28_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_28_PER", getAttribute("COLUMN_28_PER").getVal());
            rsHistory.updateDouble("COLUMN_28_AMT", getAttribute("COLUMN_28_AMT").getVal());
            rsHistory.updateString("COLUMN_28_CAPTION", (String) getAttribute("COLUMN_28_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_29_ID", (int) getAttribute("COLUMN_29_ID").getVal());
            rsHistory.updateString("COLUMN_29_FORMULA", (String) getAttribute("COLUMN_29_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_29_PER", getAttribute("COLUMN_29_PER").getVal());
            rsHistory.updateDouble("COLUMN_29_AMT", getAttribute("COLUMN_29_AMT").getVal());
            rsHistory.updateString("COLUMN_29_CAPTION", (String) getAttribute("COLUMN_29_CAPTION").getObj());
            
            rsHistory.updateInt("COLUMN_30_ID", (int) getAttribute("COLUMN_30_ID").getVal());
            rsHistory.updateString("COLUMN_30_FORMULA", (String) getAttribute("COLUMN_30_FORMULA").getObj());
            rsHistory.updateDouble("COLUMN_30_PER", getAttribute("COLUMN_30_PER").getVal());
            rsHistory.updateDouble("COLUMN_30_AMT", getAttribute("COLUMN_30_AMT").getVal());
            rsHistory.updateString("COLUMN_30_CAPTION", (String) getAttribute("COLUMN_30_CAPTION").getObj());
            
            //            rsHistory.updateDouble("INV_GROSS_AMT",(Double)getAttribute("INV_GROSS_AMT").getObj());
            //            rsHistory.updateDouble("INV_NET_AMT",(Double)getAttribute("INV_NET_AMT").getObj());
            //            rsHistory.updateDouble("INV_FINAL_AMT",(Double)getAttribute("INV_FINAL_AMT").getObj());
            //            rsHistory.updateDouble("INV_INVOICE_AMT",(Double)getAttribute("INV_INVOICE_AMT").getObj());
            //            rsHistory.updateDouble("INV_CGST",(Double)getAttribute("INV_CGST").getObj());
            //            rsHistory.updateDouble("INV_SGST",(Double)getAttribute("INV_SGST").getObj());
            //            rsHistory.updateDouble("INV_IGST",(Double)getAttribute("INV_IGST").getObj());
            //            rsHistory.updateDouble("INV_RCM",(Double)getAttribute("INV_RCM").getObj());
            //            rsHistory.updateDouble("INV_COMPOSITION",(Double)getAttribute("INV_COMPOSITION").getObj());
            //            rsHistory.updateDouble("INV_GST_COMPENSATION_CESS",(Double)getAttribute("INV_GST_COMPENSATION_CESS").getObj());
            //
            //            rsHistory.updateDouble("RCVD_GROSS_AMT",(Double)getAttribute("RCVD_GROSS_AMT").getObj());
            //            rsHistory.updateDouble("RCVD_NET_AMT",(Double)getAttribute("RCVD_NET_AMT").getObj());
            //            rsHistory.updateDouble("RCVD_FINAL_AMT",(Double)getAttribute("RCVD_FINAL_AMT").getObj());
            //            rsHistory.updateDouble("RCVD_INVOICE_AMT",(Double)getAttribute("RCVD_INVOICE_AMT").getObj());
            //            rsHistory.updateDouble("RCVD_CGST",(Double)getAttribute("RCVD_CGST").getObj());
            //            rsHistory.updateDouble("RCVD_SGST",(Double)getAttribute("RCVD_SGST").getObj());
            //            rsHistory.updateDouble("RCVD_IGST",(Double)getAttribute("RCVD_IGST").getObj());
            //            rsHistory.updateDouble("RCVD_RCM",(Double)getAttribute("RCVD_RCM").getObj());
            //            rsHistory.updateDouble("RCVD_COMPOSITION",(Double)getAttribute("RCVD_COMPOSITION").getObj());
            //            rsHistory.updateDouble("RCVD_GST_COMPENSATION_CESS",(Double)getAttribute("RCVD_GST_COMPENSATION_CESS").getObj());
            //
            //            rsHistory.updateDouble("DB_GROSS_AMT",(Double)getAttribute("DB_GROSS_AMT").getObj());
            //            rsHistory.updateDouble("DB_NET_AMT",(Double)getAttribute("DB_NET_AMT").getObj());
            //            rsHistory.updateDouble("DB_FINAL_AMT",(Double)getAttribute("DB_FINAL_AMT").getObj());
            //            rsHistory.updateDouble("DB_INVOICE_AMT",(Double)getAttribute("DB_INVOICE_AMT").getObj());
            //            rsHistory.updateDouble("DB_CGST",(Double)getAttribute("DB_CGST").getObj());
            //            rsHistory.updateDouble("DB_SGST",(Double)getAttribute("DB_SGST").getObj());
            //            rsHistory.updateDouble("DB_IGST",(Double)getAttribute("DB_IGST").getObj());
            //            rsHistory.updateDouble("DB_RCM",(Double)getAttribute("DB_RCM").getObj());
            //            rsHistory.updateDouble("DB_COMPOSITION",(Double)getAttribute("DB_COMPOSITION").getObj());
            
            rsHistory.updateString("REMARKS", (String) getAttribute("REMARKS").getObj());
            rsHistory.updateBoolean("CHANGED", true);
            rsHistory.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
            
            rsHistory.updateInt("PF_POST", getAttribute("PF_POST").getInt());
            rsHistory.updateInt("FREIGHT_POST", getAttribute("FREIGHT_POST").getInt());
            rsHistory.updateInt("OCTROI_POST", getAttribute("OCTROI_POST").getInt());
            rsHistory.updateInt("INSURANCE_POST", getAttribute("INSURANCE_POST").getInt());
            rsHistory.updateInt("CLEARANCE_POST", getAttribute("CLEARANCE_POST").getInt());
            rsHistory.updateInt("AIR_FREIGHT_POST", getAttribute("AIR_FREIGHT_POST").getInt());
            rsHistory.updateInt("OTHERS_POST", getAttribute("OTHERS_POST").getInt());
            rsHistory.updateInt("PAYMENT_TYPE", getAttribute("PAYMENT_TYPE").getInt());
            rsHistory.insertRow();
            
            //==== Delete Previous Entries ====//
            GRNNo = (String) getAttribute("GRN_NO").getObj();
            
            data.Execute("DELETE FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND GRN_NO='" + GRNNo + "' AND GRN_TYPE=1");
            
            //====== Now turn of GRN Items ======//
            stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsItem = stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL WHERE GRN_NO='1'");
            rsItem.first();
            
            for (int i = 1; i <= colGRNItems.size(); i++) {
                clsGRNGenItem ObjItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                rsItem.moveToInsertRow();
                rsItem.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsItem.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
                rsItem.updateInt("SR_NO", i);
                rsItem.updateInt("GRN_TYPE", 1); // 1 - General Material
                rsItem.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsItem.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsItem.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsItem.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsItem.updateInt("MIR_TYPE", 1);
                rsItem.updateString("PO_NO", (String) ObjItem.getAttribute("PO_NO").getObj());
                rsItem.updateInt("PO_SR_NO", (int) ObjItem.getAttribute("PO_SR_NO").getVal());
                rsItem.updateInt("PO_TYPE", (int) ObjItem.getAttribute("PO_TYPE").getVal());
                rsItem.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                
                rsItem.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsItem.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsItem.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsItem.updateString("BOE_DATE", (String) ObjItem.getAttribute("BOE_DATE").getObj());
                rsItem.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsItem.updateDouble("MIR_QTY", ObjItem.getAttribute("MIR_QTY").getVal());
                rsItem.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsItem.updateDouble("EXCESS_QTY", ObjItem.getAttribute("EXCESS_QTY").getVal());
                rsItem.updateDouble("PO_QTY", ObjItem.getAttribute("PO_QTY").getVal());
                rsItem.updateDouble("RECEIVED_QTY", ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsItem.updateDouble("BALANCE_PO_QTY", ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsItem.updateInt("REJECTED_REASON_ID", (int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsItem.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsItem.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsItem.updateDouble("REJECTED_QTY", ObjItem.getAttribute("REJECTED_QTY").getVal());
                rsItem.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                rsItem.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsItem.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsItem.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsItem.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsItem.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsItem.updateString("W_MIE", (String) ObjItem.getAttribute("W_MIE").getObj());
                rsItem.updateString("NO_CASE", (String) ObjItem.getAttribute("NO_CASE").getObj());
                rsItem.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsItem.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsItem.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsItem.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsItem.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsItem.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsItem.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsItem.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsItem.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsItem.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsItem.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsItem.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsItem.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsItem.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsItem.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsItem.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsItem.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsItem.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsItem.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsItem.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsItem.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsItem.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsItem.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsItem.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsItem.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsItem.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsItem.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsItem.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsItem.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsItem.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsItem.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsItem.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsItem.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsItem.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsItem.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsItem.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsItem.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsItem.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsItem.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsItem.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsItem.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsItem.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsItem.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsItem.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsItem.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsItem.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsItem.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsItem.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsItem.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsItem.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsItem.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsItem.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsItem.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsItem.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsItem.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsItem.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsItem.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsItem.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsItem.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsItem.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsItem.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsItem.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsItem.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsItem.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsItem.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsItem.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsItem.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsItem.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsItem.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsItem.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsItem.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsItem.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsItem.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsItem.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsItem.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsItem.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsItem.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsItem.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsItem.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsItem.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_22_ID", (int) ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsItem.updateString("COLUMN_22_FORMULA", (String) ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_22_PER", ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsItem.updateDouble("COLUMN_22_AMT", ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsItem.updateString("COLUMN_22_CAPTION", (String) ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_23_ID", (int) ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsItem.updateString("COLUMN_23_FORMULA", (String) ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_23_PER", ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsItem.updateDouble("COLUMN_23_AMT", ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsItem.updateString("COLUMN_23_CAPTION", (String) ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_24_ID", (int) ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsItem.updateString("COLUMN_24_FORMULA", (String) ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_24_PER", ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsItem.updateDouble("COLUMN_24_AMT", ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsItem.updateString("COLUMN_24_CAPTION", (String) ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_25_ID", (int) ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsItem.updateString("COLUMN_25_FORMULA", (String) ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_25_PER", ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsItem.updateDouble("COLUMN_25_AMT", ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsItem.updateString("COLUMN_25_CAPTION", (String) ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_26_ID", (int) ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsItem.updateString("COLUMN_26_FORMULA", (String) ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_26_PER", ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsItem.updateDouble("COLUMN_26_AMT", ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsItem.updateString("COLUMN_26_CAPTION", (String) ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_27_ID", (int) ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsItem.updateString("COLUMN_27_FORMULA", (String) ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_27_PER", ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsItem.updateDouble("COLUMN_27_AMT", ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsItem.updateString("COLUMN_27_CAPTION", (String) ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_28_ID", (int) ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsItem.updateString("COLUMN_28_FORMULA", (String) ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_28_PER", ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsItem.updateDouble("COLUMN_28_AMT", ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsItem.updateString("COLUMN_28_CAPTION", (String) ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_29_ID", (int) ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsItem.updateString("COLUMN_29_FORMULA", (String) ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_29_PER", ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsItem.updateDouble("COLUMN_29_AMT", ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsItem.updateString("COLUMN_29_CAPTION", (String) ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_30_ID", (int) ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsItem.updateString("COLUMN_30_FORMULA", (String) ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_30_PER", ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsItem.updateDouble("COLUMN_30_AMT", ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsItem.updateString("COLUMN_30_CAPTION", (String) ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_31_ID", (int) ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsItem.updateString("COLUMN_31_FORMULA", (String) ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_31_PER", ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsItem.updateDouble("COLUMN_31_AMT", ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsItem.updateString("COLUMN_31_CAPTION", (String) ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_32_ID", (int) ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsItem.updateString("COLUMN_32_FORMULA", (String) ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_32_PER", ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsItem.updateDouble("COLUMN_32_AMT", ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsItem.updateString("COLUMN_32_CAPTION", (String) ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_33_ID", (int) ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsItem.updateString("COLUMN_33_FORMULA", (String) ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_33_PER", ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsItem.updateDouble("COLUMN_33_AMT", ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsItem.updateString("COLUMN_33_CAPTION", (String) ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_34_ID", (int) ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsItem.updateString("COLUMN_34_FORMULA", (String) ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_34_PER", ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsItem.updateDouble("COLUMN_34_AMT", ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsItem.updateString("COLUMN_34_CAPTION", (String) ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_35_ID", (int) ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsItem.updateString("COLUMN_35_FORMULA", (String) ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_35_PER", ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsItem.updateDouble("COLUMN_35_AMT", ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsItem.updateString("COLUMN_35_CAPTION", (String) ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_36_ID", (int) ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsItem.updateString("COLUMN_36_FORMULA", (String) ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_36_PER", ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsItem.updateDouble("COLUMN_36_AMT", ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsItem.updateString("COLUMN_36_CAPTION", (String) ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_37_ID", (int) ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsItem.updateString("COLUMN_37_FORMULA", (String) ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_37_PER", ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsItem.updateDouble("COLUMN_37_AMT", ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsItem.updateString("COLUMN_37_CAPTION", (String) ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_38_ID", (int) ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsItem.updateString("COLUMN_38_FORMULA", (String) ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_38_PER", ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsItem.updateDouble("COLUMN_38_AMT", ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsItem.updateString("COLUMN_38_CAPTION", (String) ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
                rsItem.updateInt("COLUMN_39_ID", (int) ObjItem.getAttribute("COLUMN_39_ID").getVal());
                rsItem.updateString("COLUMN_39_FORMULA", (String) ObjItem.getAttribute("COLUMN_39_FORMULA").getObj());
                rsItem.updateDouble("COLUMN_39_PER", ObjItem.getAttribute("COLUMN_39_PER").getVal());
                rsItem.updateDouble("COLUMN_39_AMT", ObjItem.getAttribute("COLUMN_39_AMT").getVal());
                rsItem.updateString("COLUMN_39_CAPTION", (String) ObjItem.getAttribute("COLUMN_39_CAPTION").getObj());
                
                rsItem.updateString("MATERIAL_CODE", (String) ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsItem.updateString("MATERIAL_DESC", (String) ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsItem.updateString("QUALITY_NO", (String) ObjItem.getAttribute("QUALITY_NO").getObj());
                rsItem.updateString("PAGE_NO", (String) ObjItem.getAttribute("PAGE_NO").getObj());
                rsItem.updateDouble("EXCESS", ObjItem.getAttribute("EXCESS").getVal());
                rsItem.updateDouble("SHORTAGE", ObjItem.getAttribute("SHORTAGE").getVal());
                rsItem.updateDouble("CHALAN_QTY", ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsItem.updateString("L_F_NO", (String) ObjItem.getAttribute("L_F_NO").getObj());
                rsItem.updateString("BARCODE_TYPE", (String) ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsItem.updateBoolean("CHANGED", true);
                rsItem.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsItem.updateString("RND_DEDUCTION_REASON", ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsItem.insertRow();
                
                rsHDetail.moveToInsertRow();
                rsHDetail.updateInt("REVISION_NO", RevNo);
                rsHDetail.updateInt("COMPANY_ID", (int) getAttribute("COMPANY_ID").getVal());
                rsHDetail.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
                rsHDetail.updateInt("SR_NO", i);
                rsHDetail.updateInt("GRN_TYPE", 1); //1 - General Material
                rsHDetail.updateString("WAREHOUSE_ID", (String) ObjItem.getAttribute("WAREHOUSE_ID").getObj());
                rsHDetail.updateString("LOCATION_ID", (String) ObjItem.getAttribute("LOCATION_ID").getObj());
                rsHDetail.updateString("MIR_NO", (String) ObjItem.getAttribute("MIR_NO").getObj());
                rsHDetail.updateInt("MIR_SR_NO", (int) ObjItem.getAttribute("MIR_SR_NO").getVal());
                rsHDetail.updateInt("MIR_TYPE", 1);
                rsHDetail.updateString("PO_NO", (String) ObjItem.getAttribute("PO_NO").getObj());
                rsHDetail.updateInt("PO_SR_NO", (int) ObjItem.getAttribute("PO_SR_NO").getVal());
                rsHDetail.updateInt("PO_TYPE", (int) ObjItem.getAttribute("PO_TYPE").getVal());
                rsHDetail.updateString("ITEM_ID", (String) ObjItem.getAttribute("ITEM_ID").getObj());
                rsHDetail.updateString("HSN_SAC_CODE", (String) ObjItem.getAttribute("HSN_SAC_CODE").getObj());
                rsHDetail.updateString("ITEM_EXTRA_DESC", (String) ObjItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                rsHDetail.updateString("BOE_NO", (String) ObjItem.getAttribute("BOE_NO").getObj());
                rsHDetail.updateString("BOE_DATE", (String) ObjItem.getAttribute("BOE_DATE").getObj());
                rsHDetail.updateString("BOE_SR_NO", (String) ObjItem.getAttribute("BOE_SR_NO").getObj());
                rsHDetail.updateDouble("MIR_QTY", ObjItem.getAttribute("MIR_QTY").getVal());
                rsHDetail.updateDouble("QTY", ObjItem.getAttribute("QTY").getVal());
                rsHDetail.updateDouble("EXCESS_QTY", ObjItem.getAttribute("EXCESS_QTY").getVal());
                rsHDetail.updateDouble("PO_QTY", ObjItem.getAttribute("PO_QTY").getVal());
                rsHDetail.updateDouble("RECEIVED_QTY", ObjItem.getAttribute("RECEIVED_QTY").getVal());
                rsHDetail.updateDouble("BALANCE_PO_QTY", ObjItem.getAttribute("BALANCE_PO_QTY").getVal());
                rsHDetail.updateInt("REJECTED_REASON_ID", (int) ObjItem.getAttribute("REJECTED_REASON_ID").getVal());
                rsHDetail.updateInt("DEPT_ID", (int) ObjItem.getAttribute("DEPT_ID").getVal());
                rsHDetail.updateDouble("TOLERANCE_LIMIT", ObjItem.getAttribute("TOLERANCE_LIMIT").getVal());
                rsHDetail.updateDouble("REJECTED_QTY", ObjItem.getAttribute("REJECTED_QTY").getVal());
                rsHDetail.updateInt("UNIT", (int) ObjItem.getAttribute("UNIT").getVal());
                rsHDetail.updateDouble("RATE", ObjItem.getAttribute("RATE").getVal());
                rsHDetail.updateDouble("LANDED_RATE", ObjItem.getAttribute("LANDED_RATE").getVal());
                rsHDetail.updateDouble("TOTAL_AMOUNT", ObjItem.getAttribute("TOTAL_AMOUNT").getVal());
                rsHDetail.updateDouble("NET_AMOUNT", ObjItem.getAttribute("NET_AMOUNT").getVal());
                rsHDetail.updateString("SHADE", (String) ObjItem.getAttribute("SHADE").getObj());
                rsHDetail.updateString("W_MIE", (String) ObjItem.getAttribute("W_MIE").getObj());
                rsHDetail.updateString("NO_CASE", (String) ObjItem.getAttribute("NO_CASE").getObj());
                rsHDetail.updateBoolean("EXCISE_GATEPASS_GIVEN", ObjItem.getAttribute("EXCISE_GATEPASS_GIVEN").getBool());
                rsHDetail.updateBoolean("IMPORT_CONCESS", ObjItem.getAttribute("IMPORT_CONCESS").getBool());
                rsHDetail.updateString("REMARKS", (String) ObjItem.getAttribute("REMARKS").getObj());
                rsHDetail.updateLong("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHDetail.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHDetail.updateLong("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHDetail.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHDetail.updateInt("COLUMN_1_ID", (int) ObjItem.getAttribute("COLUMN_1_ID").getVal());
                rsHDetail.updateString("COLUMN_1_FORMULA", (String) ObjItem.getAttribute("COLUMN_1_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_1_PER", ObjItem.getAttribute("COLUMN_1_PER").getVal());
                rsHDetail.updateDouble("COLUMN_1_AMT", ObjItem.getAttribute("COLUMN_1_AMT").getVal());
                rsHDetail.updateString("COLUMN_1_CAPTION", (String) ObjItem.getAttribute("COLUMN_1_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_2_ID", (int) ObjItem.getAttribute("COLUMN_2_ID").getVal());
                rsHDetail.updateString("COLUMN_2_FORMULA", (String) ObjItem.getAttribute("COLUMN_2_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_2_PER", ObjItem.getAttribute("COLUMN_2_PER").getVal());
                rsHDetail.updateDouble("COLUMN_2_AMT", ObjItem.getAttribute("COLUMN_2_AMT").getVal());
                rsHDetail.updateString("COLUMN_2_CAPTION", (String) ObjItem.getAttribute("COLUMN_2_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_3_ID", (int) ObjItem.getAttribute("COLUMN_3_ID").getVal());
                rsHDetail.updateString("COLUMN_3_FORMULA", (String) ObjItem.getAttribute("COLUMN_3_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_3_PER", ObjItem.getAttribute("COLUMN_3_PER").getVal());
                rsHDetail.updateDouble("COLUMN_3_AMT", ObjItem.getAttribute("COLUMN_3_AMT").getVal());
                rsHDetail.updateString("COLUMN_3_CAPTION", (String) ObjItem.getAttribute("COLUMN_3_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_4_ID", (int) ObjItem.getAttribute("COLUMN_4_ID").getVal());
                rsHDetail.updateString("COLUMN_4_FORMULA", (String) ObjItem.getAttribute("COLUMN_4_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_4_PER", ObjItem.getAttribute("COLUMN_4_PER").getVal());
                rsHDetail.updateDouble("COLUMN_4_AMT", ObjItem.getAttribute("COLUMN_4_AMT").getVal());
                rsHDetail.updateString("COLUMN_4_CAPTION", (String) ObjItem.getAttribute("COLUMN_4_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_5_ID", (int) ObjItem.getAttribute("COLUMN_5_ID").getVal());
                rsHDetail.updateString("COLUMN_5_FORMULA", (String) ObjItem.getAttribute("COLUMN_5_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_5_PER", ObjItem.getAttribute("COLUMN_5_PER").getVal());
                rsHDetail.updateDouble("COLUMN_5_AMT", ObjItem.getAttribute("COLUMN_5_AMT").getVal());
                rsHDetail.updateString("COLUMN_5_CAPTION", (String) ObjItem.getAttribute("COLUMN_5_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_6_ID", (int) ObjItem.getAttribute("COLUMN_6_ID").getVal());
                rsHDetail.updateString("COLUMN_6_FORMULA", (String) ObjItem.getAttribute("COLUMN_6_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_6_PER", ObjItem.getAttribute("COLUMN_6_PER").getVal());
                rsHDetail.updateDouble("COLUMN_6_AMT", ObjItem.getAttribute("COLUMN_6_AMT").getVal());
                rsHDetail.updateString("COLUMN_6_CAPTION", (String) ObjItem.getAttribute("COLUMN_6_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_7_ID", (int) ObjItem.getAttribute("COLUMN_7_ID").getVal());
                rsHDetail.updateString("COLUMN_7_FORMULA", (String) ObjItem.getAttribute("COLUMN_7_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_7_PER", ObjItem.getAttribute("COLUMN_7_PER").getVal());
                rsHDetail.updateDouble("COLUMN_7_AMT", ObjItem.getAttribute("COLUMN_7_AMT").getVal());
                rsHDetail.updateString("COLUMN_7_CAPTION", (String) ObjItem.getAttribute("COLUMN_7_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_8_ID", (int) ObjItem.getAttribute("COLUMN_8_ID").getVal());
                rsHDetail.updateString("COLUMN_8_FORMULA", (String) ObjItem.getAttribute("COLUMN_8_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_8_PER", ObjItem.getAttribute("COLUMN_8_PER").getVal());
                rsHDetail.updateDouble("COLUMN_8_AMT", ObjItem.getAttribute("COLUMN_8_AMT").getVal());
                rsHDetail.updateString("COLUMN_8_CAPTION", (String) ObjItem.getAttribute("COLUMN_8_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_9_ID", (int) ObjItem.getAttribute("COLUMN_9_ID").getVal());
                rsHDetail.updateString("COLUMN_9_FORMULA", (String) ObjItem.getAttribute("COLUMN_9_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_9_PER", ObjItem.getAttribute("COLUMN_9_PER").getVal());
                rsHDetail.updateDouble("COLUMN_9_AMT", ObjItem.getAttribute("COLUMN_9_AMT").getVal());
                rsHDetail.updateString("COLUMN_9_CAPTION", (String) ObjItem.getAttribute("COLUMN_9_CAPTION").getObj());
                rsHDetail.updateInt("COLUMN_10_ID", (int) ObjItem.getAttribute("COLUMN_10_ID").getVal());
                rsHDetail.updateString("COLUMN_10_FORMULA", (String) ObjItem.getAttribute("COLUMN_10_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_10_PER", ObjItem.getAttribute("COLUMN_10_PER").getVal());
                rsHDetail.updateDouble("COLUMN_10_AMT", ObjItem.getAttribute("COLUMN_10_AMT").getVal());
                rsHDetail.updateString("COLUMN_10_CAPTION", (String) ObjItem.getAttribute("COLUMN_10_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_11_ID", (int) ObjItem.getAttribute("COLUMN_11_ID").getVal());
                rsHDetail.updateString("COLUMN_11_FORMULA", (String) ObjItem.getAttribute("COLUMN_11_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_11_PER", ObjItem.getAttribute("COLUMN_11_PER").getVal());
                rsHDetail.updateDouble("COLUMN_11_AMT", ObjItem.getAttribute("COLUMN_11_AMT").getVal());
                rsHDetail.updateString("COLUMN_11_CAPTION", (String) ObjItem.getAttribute("COLUMN_11_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_12_ID", (int) ObjItem.getAttribute("COLUMN_12_ID").getVal());
                rsHDetail.updateString("COLUMN_12_FORMULA", (String) ObjItem.getAttribute("COLUMN_12_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_12_PER", ObjItem.getAttribute("COLUMN_12_PER").getVal());
                rsHDetail.updateDouble("COLUMN_12_AMT", ObjItem.getAttribute("COLUMN_12_AMT").getVal());
                rsHDetail.updateString("COLUMN_12_CAPTION", (String) ObjItem.getAttribute("COLUMN_12_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_13_ID", (int) ObjItem.getAttribute("COLUMN_13_ID").getVal());
                rsHDetail.updateString("COLUMN_13_FORMULA", (String) ObjItem.getAttribute("COLUMN_13_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_13_PER", ObjItem.getAttribute("COLUMN_13_PER").getVal());
                rsHDetail.updateDouble("COLUMN_13_AMT", ObjItem.getAttribute("COLUMN_13_AMT").getVal());
                rsHDetail.updateString("COLUMN_13_CAPTION", (String) ObjItem.getAttribute("COLUMN_13_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_14_ID", (int) ObjItem.getAttribute("COLUMN_14_ID").getVal());
                rsHDetail.updateString("COLUMN_14_FORMULA", (String) ObjItem.getAttribute("COLUMN_14_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_14_PER", ObjItem.getAttribute("COLUMN_14_PER").getVal());
                rsHDetail.updateDouble("COLUMN_14_AMT", ObjItem.getAttribute("COLUMN_14_AMT").getVal());
                rsHDetail.updateString("COLUMN_14_CAPTION", (String) ObjItem.getAttribute("COLUMN_14_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_15_ID", (int) ObjItem.getAttribute("COLUMN_15_ID").getVal());
                rsHDetail.updateString("COLUMN_15_FORMULA", (String) ObjItem.getAttribute("COLUMN_15_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_15_PER", ObjItem.getAttribute("COLUMN_15_PER").getVal());
                rsHDetail.updateDouble("COLUMN_15_AMT", ObjItem.getAttribute("COLUMN_15_AMT").getVal());
                rsHDetail.updateString("COLUMN_15_CAPTION", (String) ObjItem.getAttribute("COLUMN_15_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_16_ID", (int) ObjItem.getAttribute("COLUMN_16_ID").getVal());
                rsHDetail.updateString("COLUMN_16_FORMULA", (String) ObjItem.getAttribute("COLUMN_16_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_16_PER", ObjItem.getAttribute("COLUMN_16_PER").getVal());
                rsHDetail.updateDouble("COLUMN_16_AMT", ObjItem.getAttribute("COLUMN_16_AMT").getVal());
                rsHDetail.updateString("COLUMN_16_CAPTION", (String) ObjItem.getAttribute("COLUMN_16_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_17_ID", (int) ObjItem.getAttribute("COLUMN_17_ID").getVal());
                rsHDetail.updateString("COLUMN_17_FORMULA", (String) ObjItem.getAttribute("COLUMN_17_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_17_PER", ObjItem.getAttribute("COLUMN_17_PER").getVal());
                rsHDetail.updateDouble("COLUMN_17_AMT", ObjItem.getAttribute("COLUMN_17_AMT").getVal());
                rsHDetail.updateString("COLUMN_17_CAPTION", (String) ObjItem.getAttribute("COLUMN_17_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_18_ID", (int) ObjItem.getAttribute("COLUMN_18_ID").getVal());
                rsHDetail.updateString("COLUMN_18_FORMULA", (String) ObjItem.getAttribute("COLUMN_18_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_18_PER", ObjItem.getAttribute("COLUMN_18_PER").getVal());
                rsHDetail.updateDouble("COLUMN_18_AMT", ObjItem.getAttribute("COLUMN_18_AMT").getVal());
                rsHDetail.updateString("COLUMN_18_CAPTION", (String) ObjItem.getAttribute("COLUMN_18_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_19_ID", (int) ObjItem.getAttribute("COLUMN_19_ID").getVal());
                rsHDetail.updateString("COLUMN_19_FORMULA", (String) ObjItem.getAttribute("COLUMN_19_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_19_PER", ObjItem.getAttribute("COLUMN_19_PER").getVal());
                rsHDetail.updateDouble("COLUMN_19_AMT", ObjItem.getAttribute("COLUMN_19_AMT").getVal());
                rsHDetail.updateString("COLUMN_19_CAPTION", (String) ObjItem.getAttribute("COLUMN_19_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_20_ID", (int) ObjItem.getAttribute("COLUMN_20_ID").getVal());
                rsHDetail.updateString("COLUMN_20_FORMULA", (String) ObjItem.getAttribute("COLUMN_20_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_20_PER", ObjItem.getAttribute("COLUMN_20_PER").getVal());
                rsHDetail.updateDouble("COLUMN_20_AMT", ObjItem.getAttribute("COLUMN_20_AMT").getVal());
                rsHDetail.updateString("COLUMN_20_CAPTION", (String) ObjItem.getAttribute("COLUMN_20_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_21_ID", (int) ObjItem.getAttribute("COLUMN_21_ID").getVal());
                rsHDetail.updateString("COLUMN_21_FORMULA", (String) ObjItem.getAttribute("COLUMN_21_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_21_PER", ObjItem.getAttribute("COLUMN_21_PER").getVal());
                rsHDetail.updateDouble("COLUMN_21_AMT", ObjItem.getAttribute("COLUMN_21_AMT").getVal());
                rsHDetail.updateString("COLUMN_21_CAPTION", (String) ObjItem.getAttribute("COLUMN_21_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_22_ID", (int) ObjItem.getAttribute("COLUMN_22_ID").getVal());
                rsHDetail.updateString("COLUMN_22_FORMULA", (String) ObjItem.getAttribute("COLUMN_22_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_22_PER", ObjItem.getAttribute("COLUMN_22_PER").getVal());
                rsHDetail.updateDouble("COLUMN_22_AMT", ObjItem.getAttribute("COLUMN_22_AMT").getVal());
                rsHDetail.updateString("COLUMN_22_CAPTION", (String) ObjItem.getAttribute("COLUMN_22_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_23_ID", (int) ObjItem.getAttribute("COLUMN_23_ID").getVal());
                rsHDetail.updateString("COLUMN_23_FORMULA", (String) ObjItem.getAttribute("COLUMN_23_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_23_PER", ObjItem.getAttribute("COLUMN_23_PER").getVal());
                rsHDetail.updateDouble("COLUMN_23_AMT", ObjItem.getAttribute("COLUMN_23_AMT").getVal());
                rsHDetail.updateString("COLUMN_23_CAPTION", (String) ObjItem.getAttribute("COLUMN_23_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_24_ID", (int) ObjItem.getAttribute("COLUMN_24_ID").getVal());
                rsHDetail.updateString("COLUMN_24_FORMULA", (String) ObjItem.getAttribute("COLUMN_24_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_24_PER", ObjItem.getAttribute("COLUMN_24_PER").getVal());
                rsHDetail.updateDouble("COLUMN_24_AMT", ObjItem.getAttribute("COLUMN_24_AMT").getVal());
                rsHDetail.updateString("COLUMN_24_CAPTION", (String) ObjItem.getAttribute("COLUMN_24_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_25_ID", (int) ObjItem.getAttribute("COLUMN_25_ID").getVal());
                rsHDetail.updateString("COLUMN_25_FORMULA", (String) ObjItem.getAttribute("COLUMN_25_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_25_PER", ObjItem.getAttribute("COLUMN_25_PER").getVal());
                rsHDetail.updateDouble("COLUMN_25_AMT", ObjItem.getAttribute("COLUMN_25_AMT").getVal());
                rsHDetail.updateString("COLUMN_25_CAPTION", (String) ObjItem.getAttribute("COLUMN_25_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_26_ID", (int) ObjItem.getAttribute("COLUMN_26_ID").getVal());
                rsHDetail.updateString("COLUMN_26_FORMULA", (String) ObjItem.getAttribute("COLUMN_26_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_26_PER", ObjItem.getAttribute("COLUMN_26_PER").getVal());
                rsHDetail.updateDouble("COLUMN_26_AMT", ObjItem.getAttribute("COLUMN_26_AMT").getVal());
                rsHDetail.updateString("COLUMN_26_CAPTION", (String) ObjItem.getAttribute("COLUMN_26_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_27_ID", (int) ObjItem.getAttribute("COLUMN_27_ID").getVal());
                rsHDetail.updateString("COLUMN_27_FORMULA", (String) ObjItem.getAttribute("COLUMN_27_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_27_PER", ObjItem.getAttribute("COLUMN_27_PER").getVal());
                rsHDetail.updateDouble("COLUMN_27_AMT", ObjItem.getAttribute("COLUMN_27_AMT").getVal());
                rsHDetail.updateString("COLUMN_27_CAPTION", (String) ObjItem.getAttribute("COLUMN_27_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_28_ID", (int) ObjItem.getAttribute("COLUMN_28_ID").getVal());
                rsHDetail.updateString("COLUMN_28_FORMULA", (String) ObjItem.getAttribute("COLUMN_28_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_28_PER", ObjItem.getAttribute("COLUMN_28_PER").getVal());
                rsHDetail.updateDouble("COLUMN_28_AMT", ObjItem.getAttribute("COLUMN_28_AMT").getVal());
                rsHDetail.updateString("COLUMN_28_CAPTION", (String) ObjItem.getAttribute("COLUMN_28_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_29_ID", (int) ObjItem.getAttribute("COLUMN_29_ID").getVal());
                rsHDetail.updateString("COLUMN_29_FORMULA", (String) ObjItem.getAttribute("COLUMN_29_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_29_PER", ObjItem.getAttribute("COLUMN_29_PER").getVal());
                rsHDetail.updateDouble("COLUMN_29_AMT", ObjItem.getAttribute("COLUMN_29_AMT").getVal());
                rsHDetail.updateString("COLUMN_29_CAPTION", (String) ObjItem.getAttribute("COLUMN_29_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_30_ID", (int) ObjItem.getAttribute("COLUMN_30_ID").getVal());
                rsHDetail.updateString("COLUMN_30_FORMULA", (String) ObjItem.getAttribute("COLUMN_30_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_30_PER", ObjItem.getAttribute("COLUMN_30_PER").getVal());
                rsHDetail.updateDouble("COLUMN_30_AMT", ObjItem.getAttribute("COLUMN_30_AMT").getVal());
                rsHDetail.updateString("COLUMN_30_CAPTION", (String) ObjItem.getAttribute("COLUMN_30_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_31_ID", (int) ObjItem.getAttribute("COLUMN_31_ID").getVal());
                rsHDetail.updateString("COLUMN_31_FORMULA", (String) ObjItem.getAttribute("COLUMN_31_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_31_PER", ObjItem.getAttribute("COLUMN_31_PER").getVal());
                rsHDetail.updateDouble("COLUMN_31_AMT", ObjItem.getAttribute("COLUMN_31_AMT").getVal());
                rsHDetail.updateString("COLUMN_31_CAPTION", (String) ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_32_ID", (int) ObjItem.getAttribute("COLUMN_32_ID").getVal());
                rsHDetail.updateString("COLUMN_32_FORMULA", (String) ObjItem.getAttribute("COLUMN_32_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_32_PER", ObjItem.getAttribute("COLUMN_32_PER").getVal());
                rsHDetail.updateDouble("COLUMN_32_AMT", ObjItem.getAttribute("COLUMN_32_AMT").getVal());
                rsHDetail.updateString("COLUMN_32_CAPTION", (String) ObjItem.getAttribute("COLUMN_32_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_33_ID", (int) ObjItem.getAttribute("COLUMN_33_ID").getVal());
                rsHDetail.updateString("COLUMN_33_FORMULA", (String) ObjItem.getAttribute("COLUMN_33_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_33_PER", ObjItem.getAttribute("COLUMN_33_PER").getVal());
                rsHDetail.updateDouble("COLUMN_33_AMT", ObjItem.getAttribute("COLUMN_33_AMT").getVal());
                rsHDetail.updateString("COLUMN_33_CAPTION", (String) ObjItem.getAttribute("COLUMN_33_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_34_ID", (int) ObjItem.getAttribute("COLUMN_34_ID").getVal());
                rsHDetail.updateString("COLUMN_34_FORMULA", (String) ObjItem.getAttribute("COLUMN_34_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_34_PER", ObjItem.getAttribute("COLUMN_34_PER").getVal());
                rsHDetail.updateDouble("COLUMN_34_AMT", ObjItem.getAttribute("COLUMN_34_AMT").getVal());
                rsHDetail.updateString("COLUMN_34_CAPTION", (String) ObjItem.getAttribute("COLUMN_34_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_35_ID", (int) ObjItem.getAttribute("COLUMN_35_ID").getVal());
                rsHDetail.updateString("COLUMN_35_FORMULA", (String) ObjItem.getAttribute("COLUMN_35_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_35_PER", ObjItem.getAttribute("COLUMN_35_PER").getVal());
                rsHDetail.updateDouble("COLUMN_35_AMT", ObjItem.getAttribute("COLUMN_35_AMT").getVal());
                rsHDetail.updateString("COLUMN_35_CAPTION", (String) ObjItem.getAttribute("COLUMN_35_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_36_ID", (int) ObjItem.getAttribute("COLUMN_36_ID").getVal());
                rsHDetail.updateString("COLUMN_36_FORMULA", (String) ObjItem.getAttribute("COLUMN_36_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_36_PER", ObjItem.getAttribute("COLUMN_36_PER").getVal());
                rsHDetail.updateDouble("COLUMN_36_AMT", ObjItem.getAttribute("COLUMN_36_AMT").getVal());
                rsHDetail.updateString("COLUMN_36_CAPTION", (String) ObjItem.getAttribute("COLUMN_36_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_37_ID", (int) ObjItem.getAttribute("COLUMN_37_ID").getVal());
                rsHDetail.updateString("COLUMN_37_FORMULA", (String) ObjItem.getAttribute("COLUMN_37_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_37_PER", ObjItem.getAttribute("COLUMN_37_PER").getVal());
                rsHDetail.updateDouble("COLUMN_37_AMT", ObjItem.getAttribute("COLUMN_37_AMT").getVal());
                rsHDetail.updateString("COLUMN_37_CAPTION", (String) ObjItem.getAttribute("COLUMN_37_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_38_ID", (int) ObjItem.getAttribute("COLUMN_38_ID").getVal());
                rsHDetail.updateString("COLUMN_38_FORMULA", (String) ObjItem.getAttribute("COLUMN_38_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_38_PER", ObjItem.getAttribute("COLUMN_38_PER").getVal());
                rsHDetail.updateDouble("COLUMN_38_AMT", ObjItem.getAttribute("COLUMN_38_AMT").getVal());
                rsHDetail.updateString("COLUMN_38_CAPTION", (String) ObjItem.getAttribute("COLUMN_38_CAPTION").getObj());
                
                rsHDetail.updateInt("COLUMN_39_ID", (int) ObjItem.getAttribute("COLUMN_39_ID").getVal());
                rsHDetail.updateString("COLUMN_39_FORMULA", (String) ObjItem.getAttribute("COLUMN_39_FORMULA").getObj());
                rsHDetail.updateDouble("COLUMN_39_PER", ObjItem.getAttribute("COLUMN_39_PER").getVal());
                rsHDetail.updateDouble("COLUMN_39_AMT", ObjItem.getAttribute("COLUMN_39_AMT").getVal());
                rsHDetail.updateString("COLUMN_39_CAPTION", (String) ObjItem.getAttribute("COLUMN_39_CAPTION").getObj());
                
                rsHDetail.updateString("MATERIAL_CODE", (String) ObjItem.getAttribute("MATERIAL_CODE").getObj());
                rsHDetail.updateString("MATERIAL_DESC", (String) ObjItem.getAttribute("MATERIAL_DESC").getObj());
                rsHDetail.updateString("QUALITY_NO", (String) ObjItem.getAttribute("QUALITY_NO").getObj());
                rsHDetail.updateString("PAGE_NO", (String) ObjItem.getAttribute("PAGE_NO").getObj());
                rsHDetail.updateDouble("EXCESS", ObjItem.getAttribute("EXCESS").getVal());
                rsHDetail.updateDouble("SHORTAGE", ObjItem.getAttribute("SHORTAGE").getVal());
                rsHDetail.updateDouble("CHALAN_QTY", ObjItem.getAttribute("CHALAN_QTY").getVal());
                rsHDetail.updateString("L_F_NO", (String) ObjItem.getAttribute("L_F_NO").getObj());
                rsHDetail.updateString("BARCODE_TYPE", (String) ObjItem.getAttribute("BARCODE_TYPE").getObj());
                rsHDetail.updateBoolean("CHANGED", true);
                rsHDetail.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHDetail.updateString("RND_DEDUCTION_REASON", ObjItem.getAttribute("RND_DEDUCTION_REASON").getString());
                rsHDetail.insertRow();
                
            }
            
            //====== Now turn of HSN GRN Items ======
            String mGRNNo = (String) getAttribute("GRN_NO").getObj();
            String mGRNDate = (String) getAttribute("GRN_DATE").getObj();
            
            data.Execute("DELETE FROM D_INV_GRN_HSN WHERE GRN_NO='" + mGRNNo + "' AND GRN_DATE='" + mGRNDate + "'");
            
            ResultSet rsHSN;
            Statement stHSN;
            
            stHSN = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsHSN = stHSN.executeQuery("SELECT * FROM D_INV_GRN_HSN WHERE GRN_NO='1'");
            
            for (int i = 1; i <= colHSNGRNPJVItems.size(); i++) {
                clsHSNGRNPJVItem ObjHSNItem = (clsHSNGRNPJVItem) colHSNGRNPJVItems.get(Integer.toString(i));
                rsHSN.moveToInsertRow();
                
                rsHSN.updateString("GRN_NO", (String) getAttribute("GRN_NO").getObj());
                rsHSN.updateString("GRN_DATE", (String) getAttribute("GRN_DATE").getObj());
                rsHSN.updateString("HSN_CODE", (String) ObjHSNItem.getAttribute("HSN_CODE").getObj());
                rsHSN.updateDouble("INVOICE_AMOUNT", ObjHSNItem.getAttribute("INVOICE_AMOUNT").getVal());
                rsHSN.updateDouble("INVOICE_CGST", ObjHSNItem.getAttribute("INVOICE_CGST").getVal());
                rsHSN.updateDouble("INVOICE_SGST", ObjHSNItem.getAttribute("INVOICE_SGST").getVal());
                rsHSN.updateDouble("INVOICE_IGST", ObjHSNItem.getAttribute("INVOICE_IGST").getVal());
                rsHSN.updateDouble("INVOICE_RCM", ObjHSNItem.getAttribute("INVOICE_RCM").getVal());
                rsHSN.updateDouble("INVOICE_COMPOSITION", ObjHSNItem.getAttribute("INVOICE_COMPOSITION").getVal());
                rsHSN.updateDouble("INVOICE_GST_COMP_CESS", ObjHSNItem.getAttribute("INVOICE_GST_COMP_CESS").getVal());
                rsHSN.updateDouble("RECEIVED_AMOUNT", ObjHSNItem.getAttribute("RECEIVED_AMOUNT").getVal());
                rsHSN.updateDouble("RECEIVED_CGST", ObjHSNItem.getAttribute("RECEIVED_CGST").getVal());
                rsHSN.updateDouble("RECEIVED_SGST", ObjHSNItem.getAttribute("RECEIVED_SGST").getVal());
                rsHSN.updateDouble("RECEIVED_IGST", ObjHSNItem.getAttribute("RECEIVED_IGST").getVal());
                rsHSN.updateDouble("RECEIVED_RCM", ObjHSNItem.getAttribute("RECEIVED_RCM").getVal());
                rsHSN.updateDouble("RECEIVED_COMPOSITION", ObjHSNItem.getAttribute("RECEIVED_COMPOSITION").getVal());
                rsHSN.updateDouble("RECEIVED_GST_COMP_CESS", ObjHSNItem.getAttribute("RECEIVED_GST_COMP_CESS").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_AMOUNT", ObjHSNItem.getAttribute("DEBIT_NOTE_AMOUNT").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_CGST", ObjHSNItem.getAttribute("DEBIT_NOTE_CGST").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_SGST", ObjHSNItem.getAttribute("DEBIT_NOTE_SGST").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_IGST", ObjHSNItem.getAttribute("DEBIT_NOTE_IGST").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_RCM", ObjHSNItem.getAttribute("DEBIT_NOTE_RCM").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_COMPOSITION", ObjHSNItem.getAttribute("DEBIT_NOTE_COMPOSITION").getVal());
                rsHSN.updateDouble("DEBIT_NOTE_GST_COMP_CESS", ObjHSNItem.getAttribute("DEBIT_NOTE_GST_COMP_CESS").getVal());
                
                rsHSN.updateDouble("CGST_INPUT_CR_PER", ObjHSNItem.getAttribute("CGST_INPUT_CR_PER").getVal());
                rsHSN.updateDouble("CGST_INPUT_CR_AMT", ObjHSNItem.getAttribute("CGST_INPUT_CR_AMT").getVal());
                rsHSN.updateDouble("SGST_INPUT_CR_PER", ObjHSNItem.getAttribute("SGST_INPUT_CR_PER").getVal());
                rsHSN.updateDouble("SGST_INPUT_CR_AMT", ObjHSNItem.getAttribute("SGST_INPUT_CR_AMT").getVal());
                rsHSN.updateDouble("IGST_INPUT_CR_PER", ObjHSNItem.getAttribute("IGST_INPUT_CR_PER").getVal());
                rsHSN.updateDouble("IGST_INPUT_CR_AMT", ObjHSNItem.getAttribute("IGST_INPUT_CR_AMT").getVal());
                
                rsHSN.updateInt("CREATED_BY", (int) getAttribute("CREATED_BY").getVal());
                rsHSN.updateString("CREATED_DATE", (String) getAttribute("CREATED_DATE").getObj());
                rsHSN.updateInt("MODIFIED_BY", (int) getAttribute("MODIFIED_BY").getVal());
                rsHSN.updateString("MODIFIED_DATE", (String) getAttribute("MODIFIED_DATE").getObj());
                rsHSN.updateInt("HIERARCHY_ID", (int) getAttribute("HIERARCHY_ID").getVal());
                rsHSN.updateBoolean("APPROVED", false);
                rsHSN.updateString("APPROVED_DATE", "0000-00-00");
                rsHSN.updateBoolean("REJECTED", false);
                rsHSN.updateString("REJECTED_DATE", "0000-00-00");
                rsHSN.updateBoolean("CHANGED", true);
                rsHSN.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                rsHSN.updateBoolean("CANCELLED", false);
                
                rsHSN.insertRow();
            }
            //=================================================
            
            //======== Update the Approval Flow =========
            ObjFlow.CompanyID = (int) getAttribute("COMPANY_ID").getVal();
            ObjFlow.ModuleID = clsGRNGen.ModuleID; //GRN General Material
            ObjFlow.DocNo = (String) getAttribute("GRN_NO").getObj();
            ObjFlow.From = (int) getAttribute("FROM").getVal();
            ObjFlow.To = (int) getAttribute("TO").getVal();
            ObjFlow.Status = (String) getAttribute("APPROVAL_STATUS").getObj();
            ObjFlow.TableName = "D_INV_GRN_HEADER";
            ObjFlow.IsCreator = false;
            ObjFlow.HierarchyID = (int) getAttribute("HIERARCHY_ID").getVal();
            ObjFlow.Remarks = (String) getAttribute("FROM_REMARKS").getObj();
            ObjFlow.FieldName = "GRN_NO";
            ObjFlow.UseSpecifiedURL = false;
            
            //==== Handling Rejected Documents ==========//
            if (AStatus.equals("R")) {
                //Remove the Rejected Flag First
                //data.Execute("UPDATE D_INV_INDENT_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND INDENT_NO='"+ObjFlow.DocNo+"'");
                //Remove Old Records from D_COM_DOC_DATA
                //data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=3 AND DOC_NO='"+ObjFlow.DocNo+"'");
                
                //ObjFlow.IsCreator=true;
                ObjFlow.To = (int) getAttribute("SEND_DOC_TO").getVal();
                ObjFlow.ExplicitSendTo = true;
            }
            //==========================================//
            
            //==== Handling Rejected Documents ==========//
            boolean IsRejected = getAttribute("REJECTED").getBool();
            
            if (IsRejected) {
                //Remove the Rejected Flag First
                data.Execute("UPDATE D_INV_GRN_HEADER SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND GRN_NO='" + ObjFlow.DocNo + "' AND GRN_TYPE=1");
                data.Execute("UPDATE D_INV_GRN_HSN SET REJECTED=0,CHANGED=1,CHANGED_DATE=CURDATE() WHERE GRN_NO='" + ObjFlow.DocNo + "' ");
                //Remove Old Records from D_COM_DOC_DATA
                data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=" + clsGRNGen.ModuleID + " AND DOC_NO='" + ObjFlow.DocNo + "'");
                
                ObjFlow.IsCreator = true;
            }
            //==========================================//
            
            if (ObjFlow.Status.equals("H")) {
                //Do nothing
                if (IsRejected) {
                    ObjFlow.Status = "A";
                    ObjFlow.To = ObjFlow.From;
                    ObjFlow.UpdateFlow();
                    
                }
            } else {
                if (!ObjFlow.UpdateFlow()) {
                    LastError = ObjFlow.LastError;
                }
            }
            //--------- Approval Flow Update complete -----------
            
            //===============Accounts PJV Generation =============//
            try {
                if (AStatus.equals("F")) {
                    data.Execute("UPDATE D_INV_GRN_HSN SET APPROVED=1,APPROVED_DATE=CURDATE(),CANCELLED=0,REJECTED=0 WHERE GRN_NO='" + ObjFlow.DocNo + "' ");
                    //---------for solving not post voucher----------//
                    //String Qry="SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND GRN_NO = '" + getAttribute("GRN_NO").getString() + "' AND GRN_DATE = '" + EITLERPGLOBAL.formatDateDB(getAttribute("GRN_DATE").getString())+ "'";
                    //                    for(int i=1;i<=3;i++) {
                    //if(!data.IsRecordExist(Qry,FinanceGlobal.FinURL)) {
                    System.out.println("GRNDate :: " + GRNDate);
                    //EITLERPGLOBAL.ConvertDate(GRNDate);
                    System.out.println("GRN DATE Convert :: " + EITLERPGLOBAL.ConvertDate(GRNDate));
                    System.out.println("DATE Convert :: " + EITLERPGLOBAL.ConvertDate("2017-07-01"));
                    
                    ///  tmpConn=data.getConn(dbURL);
                    //  s/tTmp=tmpConn.createStatement();
                    String GRNCAPTION = data.getStringValueFromDB("SELECT DISTINCT COLUMN_3_CAPTION FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                    System.out.print("" + GRNCAPTION);
                    
                    //double GST_value = data.getDoubleValueFromDB("SELECT  COALESCE(COLUMN_3_AMT,0)+COALESCE(COLUMN_4_AMT,0)+COALESCE(COLUMN_5_AMT,0)+COALESCE(COLUMN_6_AMT,0)+COALESCE(COLUMN_7_AMT,0)+COALESCE(COLUMN_8_AMT,0) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                    double GST_value = data.getDoubleValueFromDB("SELECT  SUM(COALESCE(INVOICE_AMOUNT,0)+COALESCE(INVOICE_CGST,0)+COALESCE(INVOICE_SGST,0)+COALESCE(INVOICE_IGST,0)+COALESCE(INVOICE_RCM,0)+COALESCE(INVOICE_COMPOSITION,0)+COALESCE(INVOICE_GST_COMP_CESS,0)) FROM D_INV_GRN_HSN WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");  //ADDED ON 14/09/2017
                    //double DN_PostAmt = data.getDoubleValueFromDB("SELECT  COALESCE(DB_NET_AMOUNT,0) FROM D_INV_GRN_HEADER WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                    double DN_PostAmt = data.getDoubleValueFromDB("SELECT  SUM(COALESCE(DEBIT_NOTE_AMOUNT,0)+COALESCE(DEBIT_NOTE_CGST,0)+COALESCE(DEBIT_NOTE_SGST,0)+COALESCE(DEBIT_NOTE_IGST,0)+COALESCE(DEBIT_NOTE_RCM,0)+COALESCE(DEBIT_NOTE_COMPOSITION,0)+COALESCE(DEBIT_NOTE_GST_COMP_CESS,0))  FROM D_INV_GRN_HSN WHERE GRN_NO='" + getAttribute("GRN_NO").getString() + "'");
                    
                    if (GRNCAPTION.equals("CGST")) {
                        
                        System.out.println("New EXCISE Based");
                        // PostVoucherNonGST(getAttribute("GRN_NO").getString(), 1,"");
                        if (GST_value > 0) {
                            System.out.println("NEW GST > 0 : GST");
                            //PostVoucherGST(getAttribute("GRN_NO").getString(), 1,"");
                            PostVoucherHSNWiseGST(getAttribute("GRN_NO").getString(), 1, "");
                            //PostDNGST((int) getAttribute("COMPANY_ID").getVal(), getAttribute("GRN_NO").getString(), getAttribute("GRN_DATE").getString());
                            System.out.println("DEBIT_NOTE TOTAL AMOUNT : " + DN_PostAmt);
                            if (DN_PostAmt > 5) {
                                System.out.println("DEBIT_NOTE POSTING START");
                                PostDNHSNGST((int) getAttribute("COMPANY_ID").getVal(), getAttribute("GRN_NO").getString(), getAttribute("GRN_DATE").getString());
                                System.out.println("DEBIT_NOTE POSTING END");
                            }
                        } else {
                            System.out.println("NEW GST Not > 0 : Non GST");
                            PostVoucherNonGST(getAttribute("GRN_NO").getString(), 1, "");
                        }
                        
                    } else {
                        System.out.println("OLd EXCISE Based");
                        PostVoucher(getAttribute("GRN_NO").getString(), 1, "");
                    }
                    
                    /*
                     if(EITLERPGLOBAL.ConvertDate(GRNDate).before(EITLERPGLOBAL.ConvertDate("2017-07-01")))
                     {
                     PostVoucher(getAttribute("GRN_NO").getString(), 1,"");
                     //  System.out.println("GST PJV CALL");
                     }
                     else
                     {
                     //PostVoucherNonGST(getAttribute("GRN_NO").getString(), 1,"");
                     // System.out.println("NON GST PJV CALL");
                     
                     if((Double)getAttribute("RCVD_SGST").getObj() > 0 || (Double)getAttribute("RCVD_IGST").getObj() > 0)
                     {
                     // PostVoucherGST(getAttribute("GRN_NO").getString(), 1,"");
                     
                     }
                     else
                     {
                     
                     }
                     }*/
                    //old pjv call before gst
                    // PostVoucher(getAttribute("GRN_NO").getString(), 1,"");
                    //}
                    //                        else {
                    //                            break;
                    //                        }
                    //                    }
                    //---------for solving not post voucher----------///
                    //PostVoucher(getAttribute("GRN_NO").getString(), 1,"");
                    
                    //-------------ADDED ON 12/09/2017------------------------------------------
                    String Qry1 = "SELECT * FROM D_FIN_VOUCHER_DETAIL WHERE COMPANY_ID = '" + EITLERPGLOBAL.gCompanyID + "' AND GRN_NO = '" + getAttribute("GRN_NO").getString() + "' ";
                    if (!data.IsRecordExist(Qry1, FinanceGlobal.FinURL)) {
                        
                        if (chkPostVoucher != 0) { 
                        
                        data.Execute("UPDATE D_INV_GRN_HSN SET APPROVED=0,APPROVED_DATE='0000-00-00',CANCELLED=0,REJECTED=0 WHERE GRN_NO='" + ObjFlow.DocNo + "' ");
                        data.Execute("UPDATE D_INV_GRN_HEADER SET APPROVED=0,APPROVED_DATE='0000-00-00' WHERE GRN_NO='" + ObjFlow.DocNo + "' ");
                        data.Execute("UPDATE D_COM_DOC_DATA SET STATUS='W' WHERE DOC_NO='" + ObjFlow.DocNo + "' AND MODULE_ID='"+clsGRNGen.ModuleID+"' AND STATUS='F'");
                        data.Execute("UPDATE D_INV_GRN_HEADER_H SET APPROVER_REMARKS='AUTO PJV POSTING FAILED',APPROVAL_STATUS='W' WHERE GRN_NO='" + ObjFlow.DocNo + "' AND APPROVAL_STATUS='F' ");
                        
                        JOptionPane.showMessageDialog(null, "PJV Posting Failed. GRN not Final Approved.");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Payment Type is CASH. PJV not be Posting.");
                        }
                    }
                    
                    //--------------------------------------------------------
                    
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            //====================================================//
            
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    //Deletes current record
    public boolean Delete(int pUserID) {
        try {
            int lCompanyID = (int) getAttribute("COMPANY_ID").getVal();
            String lDocNo = (String) getAttribute("GRN_NO").getObj();
            String strSQL = "";
            
            //First check that record is deletable
            if (CanDelete(lCompanyID, lDocNo, pUserID)) {
                
                //Give reverse effect to Stock
                //RevertStockEffect();
                data.Execute("DELETE FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + lCompanyID + " AND GRN_NO='" + lDocNo.trim() + "' AND GRN_TYPE=1");
                data.Execute("DELETE FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + lCompanyID + " AND GRN_NO='" + lDocNo.trim() + "' AND GRN_TYPE=1");
                
                LoadData(lCompanyID);
                return true;
            } else {
                LastError = "Record cannot be deleted. Either it is Approved/Rejected. Only creator of the document can delete.";
                return false;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID, String pDocNo) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1";
        clsGRNGen ObjGRNGen = new clsGRNGen();
        ObjGRNGen.Filter(strCondition, pCompanyID);
        return ObjGRNGen;
    }
    
    public Object getObject(int pCompanyID, String pDocNo, int pType) {
        String strCondition = " WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=" + pType;
        clsGRNGen ObjGRN = new clsGRNGen();
        ObjGRN.Filter(strCondition, pCompanyID);
        return ObjGRN;
    }
    
    public boolean Filter(String pCondition, int pCompanyID) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_INV_GRN_HEADER " + pCondition;
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("QUERY out if condition : " + strSql);
            rsResultSet = Stmt.executeQuery(strSql);
            
            if (!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND GRN_TYPE=1 AND GRN_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND GRN_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY GRN_NO";
                System.out.println("QUERY in if condition : " + strSql);
                rsResultSet = Stmt.executeQuery(strSql);
                Ready = true;
                MoveLast();
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean Filter(String pCondition, int pCompanyID, String URL) {
        Ready = false;
        try {
            String strSql = "SELECT * FROM D_INV_GRN_HEADER " + pCondition;
            Conn = data.getConn(URL);
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsResultSet = Stmt.executeQuery(strSql);
            
            if (!rsResultSet.first()) {
                strSql = "SELECT * FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND GRN_TYPE=1 AND GRN_DATE>='" + EITLERPGLOBAL.FinFromDateDB + "' AND GRN_DATE<='" + EITLERPGLOBAL.FinToDateDB + "' ORDER BY GRN_NO";
                rsResultSet = Stmt.executeQuery(strSql);
                Ready = true;
                MoveLast();
                return false;
            } else {
                Ready = true;
                MoveLast();
                return true;
            }
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public boolean setData() {
        Statement stItem, stLot, stTmp, stHSNItem;
        ResultSet rsItem, rsLot, rsTmp, rsHSNItem;
        String GRNNo = "";
        String GRNDate = "";
        int CompanyID = 0, ItemCounter = 0, LotCounter = 0, SrNo = 0;
        int RevNo = 0;
        
        try {
            if (HistoryView) {
                RevNo = rsResultSet.getInt("REVISION_NO");
                setAttribute("REVISION_NO", rsResultSet.getInt("REVISION_NO"));
            } else {
                setAttribute("REVISION_NO", 0);
            }
            
            CompanyID = rsResultSet.getInt("COMPANY_ID");
            
            setAttribute("COMPANY_ID", rsResultSet.getInt("COMPANY_ID"));
            setAttribute("GRN_NO", rsResultSet.getString("GRN_NO"));
            setAttribute("GRN_DATE", rsResultSet.getString("GRN_DATE"));
            setAttribute("APPROVED_ON", rsResultSet.getString("APPROVED_ON"));
            setAttribute("SUPP_ID", rsResultSet.getString("SUPP_ID"));
            setAttribute("PARTY_NAME", rsResultSet.getString("PARTY_NAME"));
            setAttribute("REFA", rsResultSet.getString("REFA"));
            setAttribute("CHALAN_NO", rsResultSet.getString("CHALAN_NO"));
            setAttribute("CHALAN_DATE", rsResultSet.getString("CHALAN_DATE"));
            setAttribute("LR_NO", rsResultSet.getString("LR_NO"));
            setAttribute("LR_DATE", rsResultSet.getString("LR_DATE"));
            setAttribute("INVOICE_NO", rsResultSet.getString("INVOICE_NO"));
            setAttribute("INVOICE_DATE", rsResultSet.getString("INVOICE_DATE"));
            setAttribute("TRANSPORTER", rsResultSet.getInt("TRANSPORTER"));
            setAttribute("TRANSPORTER_NAME", rsResultSet.getString("TRANSPORTER_NAME"));
            setAttribute("GATEPASS_NO", rsResultSet.getString("GATEPASS_NO"));
            setAttribute("IMPORT_CONCESS", rsResultSet.getBoolean("IMPORT_CONCESS"));
            setAttribute("ACCESSABLE_VALUE", rsResultSet.getDouble("ACCESSABLE_VALUE"));
            setAttribute("CURRENCY_ID", rsResultSet.getInt("CURRENCY_ID"));
            setAttribute("CURRENCY_RATE", rsResultSet.getDouble("CURRENCY_RATE"));
            setAttribute("CURRENCY_RATE_PAYMENT", rsResultSet.getDouble("CURRENCY_RATE_PAYMENT"));
            setAttribute("TOTAL_AMOUNT", rsResultSet.getDouble("TOTAL_AMOUNT"));
            setAttribute("GROSS_AMOUNT", rsResultSet.getDouble("GROSS_AMOUNT"));
            setAttribute("GRN_PENDING", rsResultSet.getBoolean("GRN_PENDING"));
            setAttribute("GRN_PENDING_REASON", rsResultSet.getInt("GRN_PENDING_REASON"));
            setAttribute("HIERARCHY_ID", rsResultSet.getInt("HIERARCHY_ID"));
            setAttribute("CENVATED_ITEMS", rsResultSet.getBoolean("CENVATED_ITEMS"));
            setAttribute("CANCELLED", rsResultSet.getInt("CANCELLED"));
            setAttribute("APPROVED", rsResultSet.getInt("APPROVED"));
            setAttribute("GRN_TYPE", rsResultSet.getInt("GRN_TYPE"));
            setAttribute("OPEN_STATUS", rsResultSet.getString("OPEN_STATUS"));
            setAttribute("FOR_STORE", rsResultSet.getInt("FOR_STORE"));
            setAttribute("COLUMN_1_ID", rsResultSet.getInt("COLUMN_1_ID"));
            setAttribute("COLUMN_1_FORMULA", rsResultSet.getString("COLUMN_1_FORMULA"));
            setAttribute("COLUMN_1_PER", rsResultSet.getDouble("COLUMN_1_PER"));
            setAttribute("COLUMN_1_AMT", rsResultSet.getDouble("COLUMN_1_AMT"));
            setAttribute("COLUMN_1_CAPTION", rsResultSet.getString("COLUMN_1_CAPTION"));
            setAttribute("COLUMN_2_ID", rsResultSet.getInt("COLUMN_2_ID"));
            setAttribute("COLUMN_2_FORMULA", rsResultSet.getString("COLUMN_2_FORMULA"));
            setAttribute("COLUMN_2_PER", rsResultSet.getDouble("COLUMN_2_PER"));
            setAttribute("COLUMN_2_AMT", rsResultSet.getDouble("COLUMN_2_AMT"));
            setAttribute("COLUMN_2_CAPTION", rsResultSet.getString("COLUMN_2_CAPTION"));
            setAttribute("COLUMN_3_ID", rsResultSet.getInt("COLUMN_3_ID"));
            setAttribute("COLUMN_3_FORMULA", rsResultSet.getString("COLUMN_3_FORMULA"));
            setAttribute("COLUMN_3_PER", rsResultSet.getDouble("COLUMN_3_PER"));
            setAttribute("COLUMN_3_AMT", rsResultSet.getDouble("COLUMN_3_AMT"));
            setAttribute("COLUMN_3_CAPTION", rsResultSet.getString("COLUMN_3_CAPTION"));
            setAttribute("COLUMN_4_ID", rsResultSet.getInt("COLUMN_4_ID"));
            setAttribute("COLUMN_4_FORMULA", rsResultSet.getString("COLUMN_4_FORMULA"));
            setAttribute("COLUMN_4_PER", rsResultSet.getDouble("COLUMN_4_PER"));
            setAttribute("COLUMN_4_AMT", rsResultSet.getDouble("COLUMN_4_AMT"));
            setAttribute("COLUMN_4_CAPTION", rsResultSet.getString("COLUMN_4_CAPTION"));
            setAttribute("COLUMN_5_ID", rsResultSet.getInt("COLUMN_5_ID"));
            setAttribute("COLUMN_5_FORMULA", rsResultSet.getString("COLUMN_5_FORMULA"));
            setAttribute("COLUMN_5_PER", rsResultSet.getDouble("COLUMN_5_PER"));
            setAttribute("COLUMN_5_AMT", rsResultSet.getDouble("COLUMN_5_AMT"));
            setAttribute("COLUMN_5_CAPTION", rsResultSet.getString("COLUMN_5_CAPTION"));
            setAttribute("COLUMN_6_ID", rsResultSet.getInt("COLUMN_6_ID"));
            setAttribute("COLUMN_6_FORMULA", rsResultSet.getString("COLUMN_6_FORMULA"));
            setAttribute("COLUMN_6_PER", rsResultSet.getDouble("COLUMN_6_PER"));
            setAttribute("COLUMN_6_AMT", rsResultSet.getDouble("COLUMN_6_AMT"));
            setAttribute("COLUMN_6_CAPTION", rsResultSet.getString("COLUMN_6_CAPTION"));
            setAttribute("COLUMN_7_ID", rsResultSet.getInt("COLUMN_7_ID"));
            setAttribute("COLUMN_7_FORMULA", rsResultSet.getString("COLUMN_7_FORMULA"));
            setAttribute("COLUMN_7_PER", rsResultSet.getDouble("COLUMN_7_PER"));
            setAttribute("COLUMN_7_AMT", rsResultSet.getDouble("COLUMN_7_AMT"));
            setAttribute("COLUMN_7_CAPTION", rsResultSet.getString("COLUMN_7_CAPTION"));
            setAttribute("COLUMN_8_ID", rsResultSet.getInt("COLUMN_8_ID"));
            setAttribute("COLUMN_8_FORMULA", rsResultSet.getString("COLUMN_8_FORMULA"));
            setAttribute("COLUMN_8_PER", rsResultSet.getDouble("COLUMN_8_PER"));
            setAttribute("COLUMN_8_AMT", rsResultSet.getDouble("COLUMN_8_AMT"));
            setAttribute("COLUMN_8_CAPTION", rsResultSet.getString("COLUMN_8_CAPTION"));
            setAttribute("COLUMN_9_ID", rsResultSet.getInt("COLUMN_9_ID"));
            setAttribute("COLUMN_9_FORMULA", rsResultSet.getString("COLUMN_9_FORMULA"));
            setAttribute("COLUMN_9_PER", rsResultSet.getDouble("COLUMN_9_PER"));
            setAttribute("COLUMN_9_AMT", rsResultSet.getDouble("COLUMN_9_AMT"));
            setAttribute("COLUMN_9_CAPTION", rsResultSet.getString("COLUMN_9_CAPTION"));
            setAttribute("COLUMN_10_ID", rsResultSet.getInt("COLUMN_10_ID"));
            setAttribute("COLUMN_10_FORMULA", rsResultSet.getString("COLUMN_10_FORMULA"));
            setAttribute("COLUMN_10_PER", rsResultSet.getDouble("COLUMN_10_PER"));
            setAttribute("COLUMN_10_AMT", rsResultSet.getDouble("COLUMN_10_AMT"));
            setAttribute("COLUMN_10_CAPTION", rsResultSet.getString("COLUMN_10_CAPTION"));
            
            setAttribute("COLUMN_11_ID", rsResultSet.getInt("COLUMN_11_ID"));
            setAttribute("COLUMN_11_FORMULA", rsResultSet.getString("COLUMN_11_FORMULA"));
            setAttribute("COLUMN_11_PER", rsResultSet.getDouble("COLUMN_11_PER"));
            setAttribute("COLUMN_11_AMT", rsResultSet.getDouble("COLUMN_11_AMT"));
            setAttribute("COLUMN_11_CAPTION", rsResultSet.getString("COLUMN_11_CAPTION"));
            
            setAttribute("COLUMN_12_ID", rsResultSet.getInt("COLUMN_12_ID"));
            setAttribute("COLUMN_12_FORMULA", rsResultSet.getString("COLUMN_12_FORMULA"));
            setAttribute("COLUMN_12_PER", rsResultSet.getDouble("COLUMN_12_PER"));
            setAttribute("COLUMN_12_AMT", rsResultSet.getDouble("COLUMN_12_AMT"));
            setAttribute("COLUMN_12_CAPTION", rsResultSet.getString("COLUMN_12_CAPTION"));
            
            setAttribute("COLUMN_13_ID", rsResultSet.getInt("COLUMN_13_ID"));
            setAttribute("COLUMN_13_FORMULA", rsResultSet.getString("COLUMN_13_FORMULA"));
            setAttribute("COLUMN_13_PER", rsResultSet.getDouble("COLUMN_13_PER"));
            setAttribute("COLUMN_13_AMT", rsResultSet.getDouble("COLUMN_13_AMT"));
            setAttribute("COLUMN_13_CAPTION", rsResultSet.getString("COLUMN_13_CAPTION"));
            
            setAttribute("COLUMN_14_ID", rsResultSet.getInt("COLUMN_14_ID"));
            setAttribute("COLUMN_14_FORMULA", rsResultSet.getString("COLUMN_14_FORMULA"));
            setAttribute("COLUMN_14_PER", rsResultSet.getDouble("COLUMN_14_PER"));
            setAttribute("COLUMN_14_AMT", rsResultSet.getDouble("COLUMN_14_AMT"));
            setAttribute("COLUMN_14_CAPTION", rsResultSet.getString("COLUMN_14_CAPTION"));
            
            setAttribute("COLUMN_15_ID", rsResultSet.getInt("COLUMN_15_ID"));
            setAttribute("COLUMN_15_FORMULA", rsResultSet.getString("COLUMN_15_FORMULA"));
            setAttribute("COLUMN_15_PER", rsResultSet.getDouble("COLUMN_15_PER"));
            setAttribute("COLUMN_15_AMT", rsResultSet.getDouble("COLUMN_15_AMT"));
            setAttribute("COLUMN_15_CAPTION", rsResultSet.getString("COLUMN_15_CAPTION"));
            
            setAttribute("COLUMN_16_ID", rsResultSet.getInt("COLUMN_16_ID"));
            setAttribute("COLUMN_16_FORMULA", rsResultSet.getString("COLUMN_16_FORMULA"));
            setAttribute("COLUMN_16_PER", rsResultSet.getDouble("COLUMN_16_PER"));
            setAttribute("COLUMN_16_AMT", rsResultSet.getDouble("COLUMN_16_AMT"));
            setAttribute("COLUMN_16_CAPTION", rsResultSet.getString("COLUMN_16_CAPTION"));
            
            setAttribute("COLUMN_17_ID", rsResultSet.getInt("COLUMN_17_ID"));
            setAttribute("COLUMN_17_FORMULA", rsResultSet.getString("COLUMN_17_FORMULA"));
            setAttribute("COLUMN_17_PER", rsResultSet.getDouble("COLUMN_17_PER"));
            setAttribute("COLUMN_17_AMT", rsResultSet.getDouble("COLUMN_17_AMT"));
            setAttribute("COLUMN_17_CAPTION", rsResultSet.getString("COLUMN_17_CAPTION"));
            
            setAttribute("COLUMN_18_ID", rsResultSet.getInt("COLUMN_18_ID"));
            setAttribute("COLUMN_18_FORMULA", rsResultSet.getString("COLUMN_18_FORMULA"));
            setAttribute("COLUMN_18_PER", rsResultSet.getDouble("COLUMN_18_PER"));
            setAttribute("COLUMN_18_AMT", rsResultSet.getDouble("COLUMN_18_AMT"));
            setAttribute("COLUMN_18_CAPTION", rsResultSet.getString("COLUMN_18_CAPTION"));
            
            setAttribute("COLUMN_19_ID", rsResultSet.getInt("COLUMN_19_ID"));
            setAttribute("COLUMN_19_FORMULA", rsResultSet.getString("COLUMN_19_FORMULA"));
            setAttribute("COLUMN_19_PER", rsResultSet.getDouble("COLUMN_19_PER"));
            setAttribute("COLUMN_19_AMT", rsResultSet.getDouble("COLUMN_19_AMT"));
            setAttribute("COLUMN_19_CAPTION", rsResultSet.getString("COLUMN_19_CAPTION"));
            
            setAttribute("COLUMN_20_ID", rsResultSet.getInt("COLUMN_20_ID"));
            setAttribute("COLUMN_20_FORMULA", rsResultSet.getString("COLUMN_20_FORMULA"));
            setAttribute("COLUMN_20_PER", rsResultSet.getDouble("COLUMN_20_PER"));
            setAttribute("COLUMN_20_AMT", rsResultSet.getDouble("COLUMN_20_AMT"));
            setAttribute("COLUMN_20_CAPTION", rsResultSet.getString("COLUMN_20_CAPTION"));
            
            setAttribute("COLUMN_21_ID", rsResultSet.getInt("COLUMN_21_ID"));
            setAttribute("COLUMN_21_FORMULA", rsResultSet.getString("COLUMN_21_FORMULA"));
            setAttribute("COLUMN_21_PER", rsResultSet.getDouble("COLUMN_21_PER"));
            setAttribute("COLUMN_21_AMT", rsResultSet.getDouble("COLUMN_21_AMT"));
            setAttribute("COLUMN_21_CAPTION", rsResultSet.getString("COLUMN_21_CAPTION"));
            
            setAttribute("COLUMN_22_ID", rsResultSet.getInt("COLUMN_22_ID"));
            setAttribute("COLUMN_22_FORMULA", rsResultSet.getString("COLUMN_22_FORMULA"));
            setAttribute("COLUMN_22_PER", rsResultSet.getDouble("COLUMN_22_PER"));
            setAttribute("COLUMN_22_AMT", rsResultSet.getDouble("COLUMN_22_AMT"));
            setAttribute("COLUMN_22_CAPTION", rsResultSet.getString("COLUMN_22_CAPTION"));
            
            setAttribute("COLUMN_23_ID", rsResultSet.getInt("COLUMN_23_ID"));
            setAttribute("COLUMN_23_FORMULA", rsResultSet.getString("COLUMN_23_FORMULA"));
            setAttribute("COLUMN_23_PER", rsResultSet.getDouble("COLUMN_23_PER"));
            setAttribute("COLUMN_23_AMT", rsResultSet.getDouble("COLUMN_23_AMT"));
            setAttribute("COLUMN_23_CAPTION", rsResultSet.getString("COLUMN_23_CAPTION"));
            
            setAttribute("COLUMN_24_ID", rsResultSet.getInt("COLUMN_24_ID"));
            setAttribute("COLUMN_24_FORMULA", rsResultSet.getString("COLUMN_24_FORMULA"));
            setAttribute("COLUMN_24_PER", rsResultSet.getDouble("COLUMN_24_PER"));
            setAttribute("COLUMN_24_AMT", rsResultSet.getDouble("COLUMN_24_AMT"));
            setAttribute("COLUMN_24_CAPTION", rsResultSet.getString("COLUMN_24_CAPTION"));
            
            setAttribute("COLUMN_25_ID", rsResultSet.getInt("COLUMN_25_ID"));
            setAttribute("COLUMN_25_FORMULA", rsResultSet.getString("COLUMN_25_FORMULA"));
            setAttribute("COLUMN_25_PER", rsResultSet.getDouble("COLUMN_25_PER"));
            setAttribute("COLUMN_25_AMT", rsResultSet.getDouble("COLUMN_25_AMT"));
            setAttribute("COLUMN_25_CAPTION", rsResultSet.getString("COLUMN_25_CAPTION"));
            
            setAttribute("COLUMN_26_ID", rsResultSet.getInt("COLUMN_26_ID"));
            setAttribute("COLUMN_26_FORMULA", rsResultSet.getString("COLUMN_26_FORMULA"));
            setAttribute("COLUMN_26_PER", rsResultSet.getDouble("COLUMN_26_PER"));
            setAttribute("COLUMN_26_AMT", rsResultSet.getDouble("COLUMN_26_AMT"));
            setAttribute("COLUMN_26_CAPTION", rsResultSet.getString("COLUMN_26_CAPTION"));
            
            setAttribute("COLUMN_27_ID", rsResultSet.getInt("COLUMN_27_ID"));
            setAttribute("COLUMN_27_FORMULA", rsResultSet.getString("COLUMN_27_FORMULA"));
            setAttribute("COLUMN_27_PER", rsResultSet.getDouble("COLUMN_27_PER"));
            setAttribute("COLUMN_27_AMT", rsResultSet.getDouble("COLUMN_27_AMT"));
            setAttribute("COLUMN_27_CAPTION", rsResultSet.getString("COLUMN_27_CAPTION"));
            
            setAttribute("COLUMN_28_ID", rsResultSet.getInt("COLUMN_28_ID"));
            setAttribute("COLUMN_28_FORMULA", rsResultSet.getString("COLUMN_28_FORMULA"));
            setAttribute("COLUMN_28_PER", rsResultSet.getDouble("COLUMN_28_PER"));
            setAttribute("COLUMN_28_AMT", rsResultSet.getDouble("COLUMN_28_AMT"));
            setAttribute("COLUMN_28_CAPTION", rsResultSet.getString("COLUMN_28_CAPTION"));
            
            setAttribute("COLUMN_29_ID", rsResultSet.getInt("COLUMN_29_ID"));
            setAttribute("COLUMN_29_FORMULA", rsResultSet.getString("COLUMN_29_FORMULA"));
            setAttribute("COLUMN_29_PER", rsResultSet.getDouble("COLUMN_29_PER"));
            setAttribute("COLUMN_29_AMT", rsResultSet.getDouble("COLUMN_29_AMT"));
            setAttribute("COLUMN_29_CAPTION", rsResultSet.getString("COLUMN_29_CAPTION"));
            
            setAttribute("COLUMN_30_ID", rsResultSet.getInt("COLUMN_30_ID"));
            setAttribute("COLUMN_30_FORMULA", rsResultSet.getString("COLUMN_30_FORMULA"));
            setAttribute("COLUMN_30_PER", rsResultSet.getDouble("COLUMN_30_PER"));
            setAttribute("COLUMN_30_AMT", rsResultSet.getDouble("COLUMN_30_AMT"));
            setAttribute("COLUMN_30_CAPTION", rsResultSet.getString("COLUMN_30_CAPTION"));
            
            
            //            setAttribute("RCVD_GROSS_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(0, 1)+""))));
            //            setAttribute("RCVD_NET_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(1, 1)+""))));
            //            setAttribute("RCVD_FINAL_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(2, 1)+""))));
            //            setAttribute("RCVD_INVOICE_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(3, 1)+""))));
            //            setAttribute("RCVD_CGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(4, 1)+""))));
            //            setAttribute("RCVD_SGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(5, 1)+""))));
            //            setAttribute("RCVD_IGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(6, 1)+""))));
            //            setAttribute("RCVD_RCM", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(7, 1)+""))));
            //            setAttribute("RCVD_COMPOSITION", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(8, 1)+""))));
            //            setAttribute("RCVD_GST_COMPENSATION_CESS", Double.valueOf(newFormat.format(Double.parseDouble(Table_RECEIVED.getValueAt(9, 1)+""))));
            //
            //            setAttribute("DB_GROSS_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(0, 1)+""))));
            //            setAttribute("DB_NET_AMT",Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(1, 1)+""))));
            //            setAttribute("DB_FINAL_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(2, 1)+""))));
            //            setAttribute("DB_INVOICE_AMT", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(3, 1)+""))));
            //            setAttribute("DB_CGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(4, 1)+""))));
            //            setAttribute("DB_SGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(5, 1)+""))));
            //            setAttribute("DB_IGST", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(6, 1)+""))));
            //            setAttribute("DB_RCM", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(7, 1)+""))));
            //            setAttribute("DB_COMPOSITION", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(8, 1)+""))));
            //            setAttribute("DB_GST_COMPENSATION_CESS", Double.valueOf(newFormat.format(Double.parseDouble(Table_DEBIT_NOTE.getValueAt(9, 1)+""))));
            setAttribute("CREATED_BY", rsResultSet.getInt("CREATED_BY"));
            setAttribute("CREATED_DATE", rsResultSet.getString("CREATED_DATE"));
            setAttribute("MODIFIED_BY", rsResultSet.getInt("MODIFIED_BY"));
            setAttribute("MODIFIED_DATE", rsResultSet.getString("MODIFIED_DATE"));
            setAttribute("REMARKS", rsResultSet.getString("REMARKS"));
            
            setAttribute("PF_POST", rsResultSet.getInt("PF_POST"));
            setAttribute("FREIGHT_POST", rsResultSet.getInt("FREIGHT_POST"));
            setAttribute("OCTROI_POST", rsResultSet.getInt("OCTROI_POST"));
            setAttribute("INSURANCE_POST", rsResultSet.getInt("INSURANCE_POST"));
            setAttribute("CLEARANCE_POST", rsResultSet.getInt("CLEARANCE_POST"));
            setAttribute("AIR_FREIGHT_POST", rsResultSet.getInt("AIR_FREIGHT_POST"));
            setAttribute("OTHERS_POST", rsResultSet.getInt("OTHERS_POST"));
            setAttribute("PAYMENT_TYPE", rsResultSet.getInt("PAYMENT_TYPE"));
            setAttribute("INVOICE_AMOUNT", rsResultSet.getDouble("INVOICE_AMOUNT"));
            
            colGRNItems.clear(); //Clear existing data
            
            GRNNo = (String) getAttribute("GRN_NO").getObj();
            
            stItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (HistoryView) {
                rsItem = stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL_H WHERE COMPANY_ID=" + CompanyID + " AND GRN_NO='" + GRNNo + "' AND GRN_TYPE=1 AND REVISION_NO=" + RevNo + " ORDER BY SR_NO");
            } else {
                rsItem = stItem.executeQuery("SELECT * FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + CompanyID + " AND GRN_NO='" + GRNNo + "' AND GRN_TYPE=1 ORDER BY SR_NO");
            }
            rsItem.first();
            
            ItemCounter = 0;
            
            while (!rsItem.isAfterLast() && rsItem.getRow() > 0) {
                ItemCounter = ItemCounter + 1;
                
                SrNo = rsItem.getInt("SR_NO"); //Sr. No. of Item Detail
                clsGRNGenItem ObjItem = new clsGRNGenItem();
                ObjItem.setAttribute("COMPANY_ID", rsItem.getInt("COMPANY_ID"));
                ObjItem.setAttribute("GRN_NO", rsItem.getString("GRN_NO"));
                ObjItem.setAttribute("SR_NO", rsItem.getInt("SR_NO"));
                ObjItem.setAttribute("GRN_TYPE", rsItem.getInt("GRN_TYPE"));
                ObjItem.setAttribute("WAREHOUSE_ID", rsItem.getString("WAREHOUSE_ID"));
                ObjItem.setAttribute("LOCATION_ID", rsItem.getString("LOCATION_ID"));
                ObjItem.setAttribute("MIR_NO", rsItem.getString("MIR_NO"));
                ObjItem.setAttribute("MIR_SR_NO", rsItem.getInt("MIR_SR_NO"));
                ObjItem.setAttribute("MIR_TYPE", rsItem.getInt("MIR_TYPE"));
                ObjItem.setAttribute("PO_NO", rsItem.getString("PO_NO"));
                ObjItem.setAttribute("PO_SR_NO", rsItem.getInt("PO_SR_NO"));
                ObjItem.setAttribute("PO_TYPE", rsItem.getInt("PO_TYPE"));
                ObjItem.setAttribute("ITEM_ID", rsItem.getString("ITEM_ID"));
                ObjItem.setAttribute("ITEM_EXTRA_DESC", rsItem.getString("ITEM_EXTRA_DESC"));
                ObjItem.setAttribute("HSN_SAC_CODE", rsItem.getString("HSN_SAC_CODE"));
                ObjItem.setAttribute("BOE_NO", rsItem.getString("BOE_NO"));
                ObjItem.setAttribute("BOE_SR_NO", rsItem.getString("BOE_SR_NO"));
                ObjItem.setAttribute("BOE_DATE", rsItem.getString("BOE_DATE"));
                ObjItem.setAttribute("MIR_QTY", rsItem.getDouble("MIR_QTY"));
                ObjItem.setAttribute("QTY", rsItem.getDouble("QTY"));
                ObjItem.setAttribute("EXCESS_QTY", rsItem.getDouble("EXCESS_QTY"));
                ObjItem.setAttribute("PO_QTY", rsItem.getDouble("PO_QTY"));
                ObjItem.setAttribute("RECEIVED_QTY", rsItem.getDouble("RECEIVED_QTY"));
                ObjItem.setAttribute("BALANCE_PO_QTY", rsItem.getDouble("BALANCE_PO_QTY"));
                ObjItem.setAttribute("REJECTED_REASON_ID", rsItem.getInt("REJECTED_REASON_ID"));
                ObjItem.setAttribute("DEPT_ID", rsItem.getInt("DEPT_ID"));
                ObjItem.setAttribute("TOLERANCE_LIMIT", rsItem.getDouble("TOLERANCE_LIMIT"));
                ObjItem.setAttribute("REJECTED_QTY", rsItem.getDouble("REJECTED_QTY"));
                ObjItem.setAttribute("UNIT", rsItem.getInt("UNIT"));
                ObjItem.setAttribute("RATE", rsItem.getDouble("RATE"));
                ObjItem.setAttribute("LANDED_RATE", rsItem.getDouble("LANDED_RATE"));
                ObjItem.setAttribute("TOTAL_AMOUNT", rsItem.getDouble("TOTAL_AMOUNT"));
                ObjItem.setAttribute("NET_AMOUNT", rsItem.getDouble("NET_AMOUNT"));
                ObjItem.setAttribute("SHADE", rsItem.getString("SHADE"));
                ObjItem.setAttribute("W_MIE", rsItem.getString("W_MIE"));
                ObjItem.setAttribute("NO_CASE", rsItem.getString("NO_CASE"));
                ObjItem.setAttribute("EXCISE_GATEPASS_GIVEN", rsItem.getBoolean("EXCISE_GATEPASS_GIVEN"));
                ObjItem.setAttribute("IMPORT_CONCESS", rsItem.getBoolean("IMPORT_CONCESS"));
                ObjItem.setAttribute("REMARKS", rsItem.getString("REMARKS"));
                ObjItem.setAttribute("CREATED_BY", rsItem.getInt("CREATED_BY"));
                ObjItem.setAttribute("CREATED_DATE", rsItem.getString("CREATED_DATE"));
                ObjItem.setAttribute("MODIFIED_BY", rsItem.getInt("MODIFIED_BY"));
                ObjItem.setAttribute("MODIFIED_DATE", rsItem.getString("MODIFIED_DATE"));
                ObjItem.setAttribute("COLUMN_1_ID", rsItem.getInt("COLUMN_1_ID"));
                ObjItem.setAttribute("COLUMN_1_FORMULA", rsItem.getString("COLUMN_1_FORMULA"));
                ObjItem.setAttribute("COLUMN_1_PER", rsItem.getDouble("COLUMN_1_PER"));
                ObjItem.setAttribute("COLUMN_1_AMT", rsItem.getDouble("COLUMN_1_AMT"));
                ObjItem.setAttribute("COLUMN_1_CAPTION", rsItem.getString("COLUMN_1_CAPTION"));
                ObjItem.setAttribute("COLUMN_2_ID", rsItem.getInt("COLUMN_2_ID"));
                ObjItem.setAttribute("COLUMN_2_FORMULA", rsItem.getString("COLUMN_2_FORMULA"));
                ObjItem.setAttribute("COLUMN_2_PER", rsItem.getDouble("COLUMN_2_PER"));
                ObjItem.setAttribute("COLUMN_2_AMT", rsItem.getDouble("COLUMN_2_AMT"));
                ObjItem.setAttribute("COLUMN_2_CAPTION", rsItem.getString("COLUMN_2_CAPTION"));
                ObjItem.setAttribute("COLUMN_3_ID", rsItem.getInt("COLUMN_3_ID"));
                ObjItem.setAttribute("COLUMN_3_FORMULA", rsItem.getString("COLUMN_3_FORMULA"));
                ObjItem.setAttribute("COLUMN_3_PER", rsItem.getDouble("COLUMN_3_PER"));
                ObjItem.setAttribute("COLUMN_3_AMT", rsItem.getDouble("COLUMN_3_AMT"));
                ObjItem.setAttribute("COLUMN_3_CAPTION", rsItem.getString("COLUMN_3_CAPTION"));
                ObjItem.setAttribute("COLUMN_4_ID", rsItem.getInt("COLUMN_4_ID"));
                ObjItem.setAttribute("COLUMN_4_FORMULA", rsItem.getString("COLUMN_4_FORMULA"));
                ObjItem.setAttribute("COLUMN_4_PER", rsItem.getDouble("COLUMN_4_PER"));
                ObjItem.setAttribute("COLUMN_4_AMT", rsItem.getDouble("COLUMN_4_AMT"));
                ObjItem.setAttribute("COLUMN_4_CAPTION", rsItem.getString("COLUMN_4_CAPTION"));
                ObjItem.setAttribute("COLUMN_5_ID", rsItem.getInt("COLUMN_5_ID"));
                ObjItem.setAttribute("COLUMN_5_FORMULA", rsItem.getString("COLUMN_5_FORMULA"));
                ObjItem.setAttribute("COLUMN_5_PER", rsItem.getDouble("COLUMN_5_PER"));
                ObjItem.setAttribute("COLUMN_5_AMT", rsItem.getDouble("COLUMN_5_AMT"));
                ObjItem.setAttribute("COLUMN_5_CAPTION", rsItem.getString("COLUMN_5_CAPTION"));
                ObjItem.setAttribute("COLUMN_6_ID", rsItem.getInt("COLUMN_6_ID"));
                ObjItem.setAttribute("COLUMN_6_FORMULA", rsItem.getString("COLUMN_6_FORMULA"));
                ObjItem.setAttribute("COLUMN_6_PER", rsItem.getDouble("COLUMN_6_PER"));
                ObjItem.setAttribute("COLUMN_6_AMT", rsItem.getDouble("COLUMN_6_AMT"));
                ObjItem.setAttribute("COLUMN_6_CAPTION", rsItem.getString("COLUMN_6_CAPTION"));
                ObjItem.setAttribute("COLUMN_7_ID", rsItem.getInt("COLUMN_7_ID"));
                ObjItem.setAttribute("COLUMN_7_FORMULA", rsItem.getString("COLUMN_7_FORMULA"));
                ObjItem.setAttribute("COLUMN_7_PER", rsItem.getDouble("COLUMN_7_PER"));
                ObjItem.setAttribute("COLUMN_7_AMT", rsItem.getDouble("COLUMN_7_AMT"));
                ObjItem.setAttribute("COLUMN_7_CAPTION", rsItem.getString("COLUMN_7_CAPTION"));
                ObjItem.setAttribute("COLUMN_8_ID", rsItem.getInt("COLUMN_8_ID"));
                ObjItem.setAttribute("COLUMN_8_FORMULA", rsItem.getString("COLUMN_8_FORMULA"));
                ObjItem.setAttribute("COLUMN_8_PER", rsItem.getDouble("COLUMN_8_PER"));
                ObjItem.setAttribute("COLUMN_8_AMT", rsItem.getDouble("COLUMN_8_AMT"));
                ObjItem.setAttribute("COLUMN_8_CAPTION", rsItem.getString("COLUMN_8_CAPTION"));
                ObjItem.setAttribute("COLUMN_9_ID", rsItem.getInt("COLUMN_9_ID"));
                ObjItem.setAttribute("COLUMN_9_FORMULA", rsItem.getString("COLUMN_9_FORMULA"));
                ObjItem.setAttribute("COLUMN_9_PER", rsItem.getDouble("COLUMN_9_PER"));
                ObjItem.setAttribute("COLUMN_9_AMT", rsItem.getDouble("COLUMN_9_AMT"));
                ObjItem.setAttribute("COLUMN_9_CAPTION", rsItem.getString("COLUMN_9_CAPTION"));
                ObjItem.setAttribute("COLUMN_10_ID", rsItem.getInt("COLUMN_10_ID"));
                ObjItem.setAttribute("COLUMN_10_FORMULA", rsItem.getString("COLUMN_10_FORMULA"));
                ObjItem.setAttribute("COLUMN_10_PER", rsItem.getDouble("COLUMN_10_PER"));
                ObjItem.setAttribute("COLUMN_10_AMT", rsItem.getDouble("COLUMN_10_AMT"));
                ObjItem.setAttribute("COLUMN_10_CAPTION", rsItem.getString("COLUMN_10_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_11_ID", rsItem.getInt("COLUMN_11_ID"));
                ObjItem.setAttribute("COLUMN_11_FORMULA", rsItem.getString("COLUMN_11_FORMULA"));
                ObjItem.setAttribute("COLUMN_11_PER", rsItem.getDouble("COLUMN_11_PER"));
                ObjItem.setAttribute("COLUMN_11_AMT", rsItem.getDouble("COLUMN_11_AMT"));
                ObjItem.setAttribute("COLUMN_11_CAPTION", rsItem.getString("COLUMN_11_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_12_ID", rsItem.getInt("COLUMN_12_ID"));
                ObjItem.setAttribute("COLUMN_12_FORMULA", rsItem.getString("COLUMN_12_FORMULA"));
                ObjItem.setAttribute("COLUMN_12_PER", rsItem.getDouble("COLUMN_12_PER"));
                ObjItem.setAttribute("COLUMN_12_AMT", rsItem.getDouble("COLUMN_12_AMT"));
                ObjItem.setAttribute("COLUMN_12_CAPTION", rsItem.getString("COLUMN_12_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_13_ID", rsItem.getInt("COLUMN_13_ID"));
                ObjItem.setAttribute("COLUMN_13_FORMULA", rsItem.getString("COLUMN_13_FORMULA"));
                ObjItem.setAttribute("COLUMN_13_PER", rsItem.getDouble("COLUMN_13_PER"));
                ObjItem.setAttribute("COLUMN_13_AMT", rsItem.getDouble("COLUMN_13_AMT"));
                ObjItem.setAttribute("COLUMN_13_CAPTION", rsItem.getString("COLUMN_13_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_14_ID", rsItem.getInt("COLUMN_14_ID"));
                ObjItem.setAttribute("COLUMN_14_FORMULA", rsItem.getString("COLUMN_14_FORMULA"));
                ObjItem.setAttribute("COLUMN_14_PER", rsItem.getDouble("COLUMN_14_PER"));
                ObjItem.setAttribute("COLUMN_14_AMT", rsItem.getDouble("COLUMN_14_AMT"));
                ObjItem.setAttribute("COLUMN_14_CAPTION", rsItem.getString("COLUMN_14_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_15_ID", rsItem.getInt("COLUMN_15_ID"));
                ObjItem.setAttribute("COLUMN_15_FORMULA", rsItem.getString("COLUMN_15_FORMULA"));
                ObjItem.setAttribute("COLUMN_15_PER", rsItem.getDouble("COLUMN_15_PER"));
                ObjItem.setAttribute("COLUMN_15_AMT", rsItem.getDouble("COLUMN_15_AMT"));
                ObjItem.setAttribute("COLUMN_15_CAPTION", rsItem.getString("COLUMN_15_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_16_ID", rsItem.getInt("COLUMN_16_ID"));
                ObjItem.setAttribute("COLUMN_16_FORMULA", rsItem.getString("COLUMN_16_FORMULA"));
                ObjItem.setAttribute("COLUMN_16_PER", rsItem.getDouble("COLUMN_16_PER"));
                ObjItem.setAttribute("COLUMN_16_AMT", rsItem.getDouble("COLUMN_16_AMT"));
                ObjItem.setAttribute("COLUMN_16_CAPTION", rsItem.getString("COLUMN_16_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_17_ID", rsItem.getInt("COLUMN_17_ID"));
                ObjItem.setAttribute("COLUMN_17_FORMULA", rsItem.getString("COLUMN_17_FORMULA"));
                ObjItem.setAttribute("COLUMN_17_PER", rsItem.getDouble("COLUMN_17_PER"));
                ObjItem.setAttribute("COLUMN_17_AMT", rsItem.getDouble("COLUMN_17_AMT"));
                ObjItem.setAttribute("COLUMN_17_CAPTION", rsItem.getString("COLUMN_17_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_18_ID", rsItem.getInt("COLUMN_18_ID"));
                ObjItem.setAttribute("COLUMN_18_FORMULA", rsItem.getString("COLUMN_18_FORMULA"));
                ObjItem.setAttribute("COLUMN_18_PER", rsItem.getDouble("COLUMN_18_PER"));
                ObjItem.setAttribute("COLUMN_18_AMT", rsItem.getDouble("COLUMN_18_AMT"));
                ObjItem.setAttribute("COLUMN_18_CAPTION", rsItem.getString("COLUMN_18_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_19_ID", rsItem.getInt("COLUMN_19_ID"));
                ObjItem.setAttribute("COLUMN_19_FORMULA", rsItem.getString("COLUMN_19_FORMULA"));
                ObjItem.setAttribute("COLUMN_19_PER", rsItem.getDouble("COLUMN_19_PER"));
                ObjItem.setAttribute("COLUMN_19_AMT", rsItem.getDouble("COLUMN_19_AMT"));
                ObjItem.setAttribute("COLUMN_19_CAPTION", rsItem.getString("COLUMN_19_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_20_ID", rsItem.getInt("COLUMN_20_ID"));
                ObjItem.setAttribute("COLUMN_20_FORMULA", rsItem.getString("COLUMN_20_FORMULA"));
                ObjItem.setAttribute("COLUMN_20_PER", rsItem.getDouble("COLUMN_20_PER"));
                ObjItem.setAttribute("COLUMN_20_AMT", rsItem.getDouble("COLUMN_20_AMT"));
                ObjItem.setAttribute("COLUMN_20_CAPTION", rsItem.getString("COLUMN_20_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_21_ID", rsItem.getInt("COLUMN_21_ID"));
                ObjItem.setAttribute("COLUMN_21_FORMULA", rsItem.getString("COLUMN_21_FORMULA"));
                ObjItem.setAttribute("COLUMN_21_PER", rsItem.getDouble("COLUMN_21_PER"));
                ObjItem.setAttribute("COLUMN_21_AMT", rsItem.getDouble("COLUMN_21_AMT"));
                ObjItem.setAttribute("COLUMN_21_CAPTION", rsItem.getString("COLUMN_21_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_22_ID", rsItem.getInt("COLUMN_22_ID"));
                ObjItem.setAttribute("COLUMN_22_FORMULA", rsItem.getString("COLUMN_22_FORMULA"));
                ObjItem.setAttribute("COLUMN_22_PER", rsItem.getDouble("COLUMN_22_PER"));
                ObjItem.setAttribute("COLUMN_22_AMT", rsItem.getDouble("COLUMN_22_AMT"));
                ObjItem.setAttribute("COLUMN_22_CAPTION", rsItem.getString("COLUMN_22_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_23_ID", rsItem.getInt("COLUMN_23_ID"));
                ObjItem.setAttribute("COLUMN_23_FORMULA", rsItem.getString("COLUMN_23_FORMULA"));
                ObjItem.setAttribute("COLUMN_23_PER", rsItem.getDouble("COLUMN_23_PER"));
                ObjItem.setAttribute("COLUMN_23_AMT", rsItem.getDouble("COLUMN_23_AMT"));
                ObjItem.setAttribute("COLUMN_23_CAPTION", rsItem.getString("COLUMN_23_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_24_ID", rsItem.getInt("COLUMN_24_ID"));
                ObjItem.setAttribute("COLUMN_24_FORMULA", rsItem.getString("COLUMN_24_FORMULA"));
                ObjItem.setAttribute("COLUMN_24_PER", rsItem.getDouble("COLUMN_24_PER"));
                ObjItem.setAttribute("COLUMN_24_AMT", rsItem.getDouble("COLUMN_24_AMT"));
                ObjItem.setAttribute("COLUMN_24_CAPTION", rsItem.getString("COLUMN_24_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_25_ID", rsItem.getInt("COLUMN_25_ID"));
                ObjItem.setAttribute("COLUMN_25_FORMULA", rsItem.getString("COLUMN_25_FORMULA"));
                ObjItem.setAttribute("COLUMN_25_PER", rsItem.getDouble("COLUMN_25_PER"));
                ObjItem.setAttribute("COLUMN_25_AMT", rsItem.getDouble("COLUMN_25_AMT"));
                ObjItem.setAttribute("COLUMN_25_CAPTION", rsItem.getString("COLUMN_25_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_26_ID", rsItem.getInt("COLUMN_26_ID"));
                ObjItem.setAttribute("COLUMN_26_FORMULA", rsItem.getString("COLUMN_26_FORMULA"));
                ObjItem.setAttribute("COLUMN_26_PER", rsItem.getDouble("COLUMN_26_PER"));
                ObjItem.setAttribute("COLUMN_26_AMT", rsItem.getDouble("COLUMN_26_AMT"));
                ObjItem.setAttribute("COLUMN_26_CAPTION", rsItem.getString("COLUMN_26_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_27_ID", rsItem.getInt("COLUMN_27_ID"));
                ObjItem.setAttribute("COLUMN_27_FORMULA", rsItem.getString("COLUMN_27_FORMULA"));
                ObjItem.setAttribute("COLUMN_27_PER", rsItem.getDouble("COLUMN_27_PER"));
                ObjItem.setAttribute("COLUMN_27_AMT", rsItem.getDouble("COLUMN_27_AMT"));
                ObjItem.setAttribute("COLUMN_27_CAPTION", rsItem.getString("COLUMN_27_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_28_ID", rsItem.getInt("COLUMN_28_ID"));
                ObjItem.setAttribute("COLUMN_28_FORMULA", rsItem.getString("COLUMN_28_FORMULA"));
                ObjItem.setAttribute("COLUMN_28_PER", rsItem.getDouble("COLUMN_28_PER"));
                ObjItem.setAttribute("COLUMN_28_AMT", rsItem.getDouble("COLUMN_28_AMT"));
                ObjItem.setAttribute("COLUMN_28_CAPTION", rsItem.getString("COLUMN_28_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_29_ID", rsItem.getInt("COLUMN_29_ID"));
                ObjItem.setAttribute("COLUMN_29_FORMULA", rsItem.getString("COLUMN_29_FORMULA"));
                ObjItem.setAttribute("COLUMN_29_PER", rsItem.getDouble("COLUMN_29_PER"));
                ObjItem.setAttribute("COLUMN_29_AMT", rsItem.getDouble("COLUMN_29_AMT"));
                ObjItem.setAttribute("COLUMN_29_CAPTION", rsItem.getString("COLUMN_29_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_30_ID", rsItem.getInt("COLUMN_30_ID"));
                ObjItem.setAttribute("COLUMN_30_FORMULA", rsItem.getString("COLUMN_30_FORMULA"));
                ObjItem.setAttribute("COLUMN_30_PER", rsItem.getDouble("COLUMN_30_PER"));
                ObjItem.setAttribute("COLUMN_30_AMT", rsItem.getDouble("COLUMN_30_AMT"));
                ObjItem.setAttribute("COLUMN_30_CAPTION", rsItem.getString("COLUMN_30_CAPTION"));
                
                //
                ObjItem.setAttribute("COLUMN_31_ID", rsItem.getInt("COLUMN_31_ID"));
                ObjItem.setAttribute("COLUMN_31_FORMULA", rsItem.getString("COLUMN_31_FORMULA"));
                ObjItem.setAttribute("COLUMN_31_PER", rsItem.getDouble("COLUMN_31_PER"));
                ObjItem.setAttribute("COLUMN_31_AMT", rsItem.getDouble("COLUMN_31_AMT"));
                ObjItem.setAttribute("COLUMN_31_CAPTION", rsItem.getString("COLUMN_31_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_32_ID", rsItem.getInt("COLUMN_32_ID"));
                ObjItem.setAttribute("COLUMN_32_FORMULA", rsItem.getString("COLUMN_32_FORMULA"));
                ObjItem.setAttribute("COLUMN_32_PER", rsItem.getDouble("COLUMN_32_PER"));
                ObjItem.setAttribute("COLUMN_32_AMT", rsItem.getDouble("COLUMN_32_AMT"));
                ObjItem.setAttribute("COLUMN_32_CAPTION", rsItem.getString("COLUMN_32_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_33_ID", rsItem.getInt("COLUMN_33_ID"));
                ObjItem.setAttribute("COLUMN_33_FORMULA", rsItem.getString("COLUMN_33_FORMULA"));
                ObjItem.setAttribute("COLUMN_33_PER", rsItem.getDouble("COLUMN_33_PER"));
                ObjItem.setAttribute("COLUMN_33_AMT", rsItem.getDouble("COLUMN_33_AMT"));
                ObjItem.setAttribute("COLUMN_33_CAPTION", rsItem.getString("COLUMN_33_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_34_ID", rsItem.getInt("COLUMN_34_ID"));
                ObjItem.setAttribute("COLUMN_34_FORMULA", rsItem.getString("COLUMN_34_FORMULA"));
                ObjItem.setAttribute("COLUMN_34_PER", rsItem.getDouble("COLUMN_34_PER"));
                ObjItem.setAttribute("COLUMN_34_AMT", rsItem.getDouble("COLUMN_34_AMT"));
                ObjItem.setAttribute("COLUMN_34_CAPTION", rsItem.getString("COLUMN_34_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_35_ID", rsItem.getInt("COLUMN_35_ID"));
                ObjItem.setAttribute("COLUMN_35_FORMULA", rsItem.getString("COLUMN_35_FORMULA"));
                ObjItem.setAttribute("COLUMN_35_PER", rsItem.getDouble("COLUMN_35_PER"));
                ObjItem.setAttribute("COLUMN_35_AMT", rsItem.getDouble("COLUMN_35_AMT"));
                ObjItem.setAttribute("COLUMN_35_CAPTION", rsItem.getString("COLUMN_35_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_36_ID", rsItem.getInt("COLUMN_36_ID"));
                ObjItem.setAttribute("COLUMN_36_FORMULA", rsItem.getString("COLUMN_36_FORMULA"));
                ObjItem.setAttribute("COLUMN_36_PER", rsItem.getDouble("COLUMN_36_PER"));
                ObjItem.setAttribute("COLUMN_36_AMT", rsItem.getDouble("COLUMN_36_AMT"));
                ObjItem.setAttribute("COLUMN_36_CAPTION", rsItem.getString("COLUMN_36_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_37_ID", rsItem.getInt("COLUMN_37_ID"));
                ObjItem.setAttribute("COLUMN_37_FORMULA", rsItem.getString("COLUMN_37_FORMULA"));
                ObjItem.setAttribute("COLUMN_37_PER", rsItem.getDouble("COLUMN_37_PER"));
                ObjItem.setAttribute("COLUMN_37_AMT", rsItem.getDouble("COLUMN_37_AMT"));
                ObjItem.setAttribute("COLUMN_37_CAPTION", rsItem.getString("COLUMN_37_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_38_ID", rsItem.getInt("COLUMN_38_ID"));
                ObjItem.setAttribute("COLUMN_38_FORMULA", rsItem.getString("COLUMN_38_FORMULA"));
                ObjItem.setAttribute("COLUMN_38_PER", rsItem.getDouble("COLUMN_38_PER"));
                ObjItem.setAttribute("COLUMN_38_AMT", rsItem.getDouble("COLUMN_38_AMT"));
                ObjItem.setAttribute("COLUMN_38_CAPTION", rsItem.getString("COLUMN_38_CAPTION"));
                
                ObjItem.setAttribute("COLUMN_39_ID", rsItem.getInt("COLUMN_39_ID"));
                ObjItem.setAttribute("COLUMN_39_FORMULA", rsItem.getString("COLUMN_39_FORMULA"));
                ObjItem.setAttribute("COLUMN_39_PER", rsItem.getDouble("COLUMN_39_PER"));
                ObjItem.setAttribute("COLUMN_39_AMT", rsItem.getDouble("COLUMN_39_AMT"));
                ObjItem.setAttribute("COLUMN_39_CAPTION", rsItem.getString("COLUMN_39_CAPTION"));
                
                ObjItem.setAttribute("MATERIAL_CODE", rsItem.getString("MATERIAL_CODE"));
                ObjItem.setAttribute("MATERIAL_DESC", rsItem.getString("MATERIAL_DESC"));
                ObjItem.setAttribute("QUALITY_NO", rsItem.getString("QUALITY_NO"));
                ObjItem.setAttribute("PAGE_NO", rsItem.getString("PAGE_NO"));
                ObjItem.setAttribute("EXCESS", rsItem.getDouble("EXCESS"));
                ObjItem.setAttribute("SHORTAGE", rsItem.getDouble("SHORTAGE"));
                ObjItem.setAttribute("CHALAN_QTY", rsItem.getDouble("CHALAN_QTY"));
                ObjItem.setAttribute("L_F_NO", rsItem.getString("L_F_NO"));
                ObjItem.setAttribute("BARCODE_TYPE", rsItem.getString("BARCODE_TYPE"));
                ObjItem.setAttribute("RND_DEDUCTION_REASON", rsItem.getString("RND_DEDUCTION_REASON"));
                colGRNItems.put(Integer.toString(ItemCounter), ObjItem);
                rsItem.next();
            }
            
            colHSNGRNPJVItems.clear();
            
            GRNDate = (String) getAttribute("GRN_DATE").getObj();
            stHSNItem = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "SELECT * FROM D_INV_GRN_HSN WHERE GRN_NO='" + GRNNo + "' AND GRN_DATE='" + GRNDate + "' ";
            rsHSNItem = stHSNItem.executeQuery(sql);
            rsHSNItem.first();
            int cnt = 0;
            
            while (!rsHSNItem.isAfterLast()) {
                cnt = cnt + 1;
                clsHSNGRNPJVItem ObjHSNItem = new clsHSNGRNPJVItem();
                ObjHSNItem.setAttribute("GRN_NO", rsHSNItem.getString("GRN_NO"));
                ObjHSNItem.setAttribute("GRN_DATE", rsHSNItem.getString("GRN_DATE"));
                ObjHSNItem.setAttribute("HSN_CODE", rsHSNItem.getString("HSN_CODE"));
                ObjHSNItem.setAttribute("INVOICE_AMOUNT", rsHSNItem.getDouble("INVOICE_AMOUNT"));
                ObjHSNItem.setAttribute("INVOICE_CGST", rsHSNItem.getDouble("INVOICE_CGST"));
                ObjHSNItem.setAttribute("INVOICE_SGST", rsHSNItem.getDouble("INVOICE_SGST"));
                ObjHSNItem.setAttribute("INVOICE_IGST", rsHSNItem.getDouble("INVOICE_IGST"));
                ObjHSNItem.setAttribute("INVOICE_RCM", rsHSNItem.getDouble("INVOICE_RCM"));
                ObjHSNItem.setAttribute("INVOICE_COMPOSITION", rsHSNItem.getDouble("INVOICE_COMPOSITION"));
                ObjHSNItem.setAttribute("INVOICE_GST_COMP_CESS", rsHSNItem.getDouble("INVOICE_GST_COMP_CESS"));
                ObjHSNItem.setAttribute("RECEIVED_AMOUNT", rsHSNItem.getDouble("RECEIVED_AMOUNT"));
                ObjHSNItem.setAttribute("RECEIVED_CGST", rsHSNItem.getDouble("RECEIVED_CGST"));
                ObjHSNItem.setAttribute("RECEIVED_SGST", rsHSNItem.getDouble("RECEIVED_SGST"));
                ObjHSNItem.setAttribute("RECEIVED_IGST", rsHSNItem.getDouble("RECEIVED_IGST"));
                ObjHSNItem.setAttribute("RECEIVED_RCM", rsHSNItem.getDouble("RECEIVED_RCM"));
                ObjHSNItem.setAttribute("RECEIVED_COMPOSITION", rsHSNItem.getDouble("RECEIVED_COMPOSITION"));
                ObjHSNItem.setAttribute("RECEIVED_GST_COMP_CESS", rsHSNItem.getDouble("RECEIVED_GST_COMP_CESS"));
                ObjHSNItem.setAttribute("DEBIT_NOTE_AMOUNT", rsHSNItem.getDouble("DEBIT_NOTE_AMOUNT"));
                ObjHSNItem.setAttribute("DEBIT_NOTE_CGST", rsHSNItem.getDouble("DEBIT_NOTE_CGST"));
                ObjHSNItem.setAttribute("DEBIT_NOTE_SGST", rsHSNItem.getDouble("DEBIT_NOTE_SGST"));
                ObjHSNItem.setAttribute("DEBIT_NOTE_IGST", rsHSNItem.getDouble("DEBIT_NOTE_IGST"));
                ObjHSNItem.setAttribute("DEBIT_NOTE_RCM", rsHSNItem.getDouble("DEBIT_NOTE_RCM"));
                ObjHSNItem.setAttribute("DEBIT_NOTE_COMPOSITION", rsHSNItem.getDouble("DEBIT_NOTE_COMPOSITION"));
                ObjHSNItem.setAttribute("DEBIT_NOTE_GST_COMP_CESS", rsHSNItem.getDouble("DEBIT_NOTE_GST_COMP_CESS"));
                
                ObjHSNItem.setAttribute("CGST_INPUT_CR_PER", rsHSNItem.getDouble("CGST_INPUT_CR_PER"));
                ObjHSNItem.setAttribute("CGST_INPUT_CR_AMT", rsHSNItem.getDouble("CGST_INPUT_CR_AMT"));
                ObjHSNItem.setAttribute("SGST_INPUT_CR_PER", rsHSNItem.getDouble("SGST_INPUT_CR_PER"));
                ObjHSNItem.setAttribute("SGST_INPUT_CR_AMT", rsHSNItem.getDouble("SGST_INPUT_CR_AMT"));
                ObjHSNItem.setAttribute("IGST_INPUT_CR_PER", rsHSNItem.getDouble("IGST_INPUT_CR_PER"));
                ObjHSNItem.setAttribute("IGST_INPUT_CR_AMT", rsHSNItem.getDouble("IGST_INPUT_CR_AMT"));
                                
                colHSNGRNPJVItems.put(Integer.toString(cnt), ObjHSNItem);
                rsHSNItem.next();
            }
            
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    //This routine checks and returns whether the item is editable or not
    //Criteria is Approved item cannot be changed
    //and if not approved then user id is checked whether doucment
    //is waiting for his approval.
    public boolean IsEditable(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        
        try {
            if (HistoryView) {
                return false;
            }
            
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GRN_NO='" + pDocNo + "' AND (APPROVED=1) AND GRN_TYPE=1";
            
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=" + clsGRNGen.ModuleID + "  AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if (rsTmp.getInt("COUNT") > 0) {
                    //Yes document is waiting for this user
                    return true;
                } else {
                    //Document is not editable by this user
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    //This routine checks and returns whether the item is deletable or not
    //Criteria is Approved item cannot be deleted
    //and if not approved then user id is checked whether doucment
    //is created by the user. Only creator can delete the document
    public boolean CanDelete(int pCompanyID, String pDocNo, int pUserID) {
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        
        try {
            if (HistoryView) {
                return false;
            }
            //First check that document is approved or not
            strSQL = "SELECT COUNT(*) AS COUNT FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND GRN_NO='" + pDocNo + "' AND (APPROVED=1) AND GRN_TYPE=1";
            
            tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsTmp = tmpStmt.executeQuery(strSQL);
            rsTmp.first();
            
            if (rsTmp.getInt("COUNT") > 0) //Item is Approved
            {
                //Discard the request
                return false;
            } else {
                strSQL = "SELECT COUNT(*) AS COUNT FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + Integer.toString(pCompanyID) + " AND MODULE_ID=" + clsGRNGen.ModuleID + "  AND DOC_NO='" + pDocNo + "' AND USER_ID=" + Integer.toString(pUserID) + " AND TYPE='C' AND STATUS='W'";
                tmpStmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rsTmp = tmpStmt.executeQuery(strSQL);
                rsTmp.first();
                
                if (rsTmp.getInt("COUNT") > 0) {
                    //Yes document is waiting for this user
                    return true;
                } else {
                    //Document is not editable by this user
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean ShowHistory(int pCompanyID, String pDocNo) {
        Ready = false;
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsResultSet = Stmt.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE COMPANY_ID=" + pCompanyID + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1");
            Ready = true;
            HistoryView = true;
            MoveFirst();
            return true;
        } catch (Exception e) {
            LastError = e.getMessage();
            return false;
        }
    }
    
    public static HashMap getHistoryList(int pCompanyID, String pDocNo) {
        HashMap List = new HashMap();
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        
        try {
            tmpConn = data.getConn();
            stTmp = tmpConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stTmp.setFetchSize(Integer.MIN_VALUE);
            rsTmp = stTmp.executeQuery("SELECT * FROM D_INV_GRN_HEADER_H WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1");
            
            while (rsTmp.next()) {
                clsGRNGen ObjGRN = new clsGRNGen();
                
                ObjGRN.setAttribute("GRN_NO", rsTmp.getString("GRN_NO"));
                ObjGRN.setAttribute("GRN_DATE", rsTmp.getString("GRN_DATE"));
                ObjGRN.setAttribute("REVISION_NO", rsTmp.getInt("REVISION_NO"));
                ObjGRN.setAttribute("UPDATED_BY", rsTmp.getInt("UPDATED_BY"));
                ObjGRN.setAttribute("APPROVAL_STATUS", rsTmp.getString("APPROVAL_STATUS"));
                ObjGRN.setAttribute("ENTRY_DATE", rsTmp.getString("ENTRY_DATE"));
                ObjGRN.setAttribute("APPROVER_REMARKS", rsTmp.getString("APPROVER_REMARKS"));
                
                List.put(Integer.toString(List.size() + 1), ObjGRN);
            }
            
            rsTmp.close();
            stTmp.close();
            //tmpConn.close();
            return List;
            
        } catch (Exception e) {
            return List;
        }
    }
    
    //This routine creates Rejection Memo from the GRN
    public boolean PostRJN() {
    //private boolean PostRJN() {
        boolean RejectedItemsFound = false;
        
        int Cnt = 0;
        
        int RJNHierarchy = 0, NRGPHierarchy = 0;
        clsRJNGen ObjRJNGen = new clsRJNGen();
        ObjRJNGen.LoadData(EITLERPGLOBAL.gCompanyID); //Load the Data
        
        clsNRGP ObjNRGP = new clsNRGP();
        ObjNRGP.LoadData(EITLERPGLOBAL.gCompanyID);
        
        for (int i = 1; i <= colGRNItems.size(); i++) {
            
            clsGRNGenItem ObjGRNItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
            clsRJNGenItem ObjItem = new clsRJNGenItem();
            
            double RejectedQty = ObjGRNItem.getAttribute("REJECTED_QTY").getVal();
            
            if (RejectedQty > 0) {
                RejectedItemsFound = true;
            }
        }
        
        if (RejectedItemsFound) {
            
            try {
                HashMap List = new HashMap();
                
                //----- Generate cmbType ------- //
                List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=9");
                clsHierarchy ObjHierarchy = (clsHierarchy) List.get("1");
                RJNHierarchy = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
                //------------------------------ //
                
                ObjRJNGen.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                ObjRJNGen.setAttribute("PREFIX", clsFirstFree.getDefaultPrefix(EITLERPGLOBAL.gCompanyID, 9));
                ObjRJNGen.setAttribute("SUFFIX", "");
                ObjRJNGen.setAttribute("FFNO", clsFirstFree.getDefaultFirstFreeNo(EITLERPGLOBAL.gCompanyID, 9));
                ObjRJNGen.setAttribute("RJN_DATE", (String) getAttribute("GRN_DATE").getObj());
                ObjRJNGen.setAttribute("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
                ObjRJNGen.setAttribute("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
                ObjRJNGen.setAttribute("MODE_TRANSPORTER", 0);
                ObjRJNGen.setAttribute("TRANSPORTER", (int) getAttribute("TRANSPORTER").getVal());
                ObjRJNGen.setAttribute("GATEPASS_TYPE", "N");
                ObjRJNGen.setAttribute("GATEPASS_NO", ""); //will be updated later
                ObjRJNGen.setAttribute("GATEPASS_DATE", "0000-00-00");
                ObjRJNGen.setAttribute("CURRENCY_ID", (int) getAttribute("CURRENCY_ID").getVal());
                ObjRJNGen.setAttribute("CURRENCY_RATE", getAttribute("CURRENCY_RATE").getVal());
                ObjRJNGen.setAttribute("CANCELLED", false);
                ObjRJNGen.setAttribute("RJN_TYPE", 1); //Fixed type 2 - Raw Material, 1 - General
                ObjRJNGen.setAttribute("FOR_STORE", clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID));
                ObjRJNGen.setAttribute("REMARKS", (String) getAttribute("REMARKS").getObj());
                
                //----- Update Approval Specific Fields -----------//
                ObjRJNGen.setAttribute("HIERARCHY_ID", RJNHierarchy);
                ObjRJNGen.setAttribute("FROM", EITLERPGLOBAL.gUserID);
                ObjRJNGen.setAttribute("TO", EITLERPGLOBAL.gUserID);
                ObjRJNGen.setAttribute("FROM_REMARKS", "");
                ObjRJNGen.setAttribute("APPROVAL_STATUS", "H");
                //-------------------------------------------------//
                
                ObjRJNGen.setAttribute("CREATED_BY", EITLERPGLOBAL.gUserID);
                ObjRJNGen.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                
                //=================== Setting up Line Items ==================//
                ObjRJNGen.colRJNItems.clear();
                
                Cnt = 0;
                for (int i = 1; i <= colGRNItems.size(); i++) {
                    
                    clsGRNGenItem ObjGRNItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                    clsRJNGenItem ObjItem = new clsRJNGenItem();
                    
                    double RejectedQty = ObjGRNItem.getAttribute("REJECTED_QTY").getVal();
                    
                    if (RejectedQty > 0) {
                        Cnt++;
                        ObjItem.setAttribute("SR_NO", Cnt);
                        ObjItem.setAttribute("ITEM_ID", (String) ObjGRNItem.getAttribute("ITEM_ID").getObj());
                        ObjItem.setAttribute("ITEM_EXTRA_DESC", clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String) ObjGRNItem.getAttribute("ITEM_ID").getObj()) + " " + (String) ObjGRNItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                        ObjItem.setAttribute("QTY", ObjGRNItem.getAttribute("REJECTED_QTY").getVal() + ObjGRNItem.getAttribute("EXCESS_QTY").getVal());
                        ObjItem.setAttribute("UNIT", (int) ObjGRNItem.getAttribute("UNIT").getVal());
                        ObjItem.setAttribute("BOE_NO", (String) ObjGRNItem.getAttribute("BOE_NO").getObj());
                        ObjItem.setAttribute("BOE_SR_NO", (String) ObjGRNItem.getAttribute("BOE_SR_NO").getObj());
                        ObjItem.setAttribute("BOE_DATE", (String) ObjGRNItem.getAttribute("BOE_DATE").getObj());
                        ObjItem.setAttribute("REJECTED_REASON_ID", (int) ObjGRNItem.getAttribute("REJECTED_REASON_ID").getVal());
                        ObjItem.setAttribute("REMARKS", (String) ObjGRNItem.getAttribute("REMARKS").getObj());
                        ObjItem.setAttribute("WAREHOUSE_ID", (String) ObjGRNItem.getAttribute("WAREHOUSE_ID").getObj());
                        ObjItem.setAttribute("LOCATION_ID", (String) ObjGRNItem.getAttribute("LOCATION_ID").getObj());
                        ObjItem.setAttribute("PO_NO", (String) ObjGRNItem.getAttribute("PO_NO").getObj());
                        ObjItem.setAttribute("PO_SR_NO", (int) ObjGRNItem.getAttribute("PO_SR_NO").getVal());
                        ObjItem.setAttribute("PO_TYPE", (int) ObjGRNItem.getAttribute("PO_TYPE").getVal());
                        ObjItem.setAttribute("GRN_NO", (String) getAttribute("GRN_NO").getObj());
                        ObjItem.setAttribute("GRN_SR_NO", i);
                        
                        ObjRJNGen.colRJNItems.put(Integer.toString(ObjRJNGen.colRJNItems.size() + 1), ObjItem);
                    }
                }
                //======================Completed ===========================//
                
                // ========== Now insert ============//
                if (ObjRJNGen.Insert()) {
                    RJNCreated = true;
                    RJNNo = (String) ObjRJNGen.getAttribute("RJN_NO").getObj();
                } else {
                    ErrorMessages = ObjRJNGen.LastError;
                    RJNCreated = false;
                }
                // =================================//
                
                if (RJNCreated) //Now Generate NRGP
                {
                    //Now posting NRGP from the RJN
                    
                    //----- Generate cmbType ------- //
                    List = clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " AND MODULE_ID=11");
                    ObjHierarchy = (clsHierarchy) List.get("1");
                    NRGPHierarchy = (int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
                    //------------------------------ //
                    
                    ObjNRGP.setAttribute("PREFIX", clsFirstFree.getDefaultPrefix(EITLERPGLOBAL.gCompanyID, 11));
                    ObjNRGP.setAttribute("SUFFIX", "");
                    ObjNRGP.setAttribute("FFNO", clsFirstFree.getDefaultFirstFreeNo(EITLERPGLOBAL.gCompanyID, 11));
                    ObjNRGP.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                    ObjNRGP.setAttribute("GATEPASS_DATE", (String) getAttribute("GRN_DATE").getObj());
                    ObjNRGP.setAttribute("SUPP_ID", (String) getAttribute("SUPP_ID").getObj());
                    ObjNRGP.setAttribute("REMARKS", (String) getAttribute("REMARKS").getObj());
                    ObjNRGP.setAttribute("TOTAL_AMOUNT", 0);
                    ObjNRGP.setAttribute("GATEPASS_TYPE", 0);
                    ObjNRGP.setAttribute("FOR_DEPT", 0);
                    ObjNRGP.setAttribute("MODE_TRASNPORT", 0);
                    ObjNRGP.setAttribute("TRANSPORTER", (int) getAttribute("TRANSPORTER").getVal());
                    ObjNRGP.setAttribute("USER_ID", 0);
                    
                    if (((String) getAttribute("SUPP_ID").getObj()).equals("000000")) {
                        ObjNRGP.setAttribute("PARTY_NAME", (String) getAttribute("PARTY_NAME").getObj());
                        ObjNRGP.setAttribute("ADD1", "");
                        ObjNRGP.setAttribute("ADD2", "");
                        ObjNRGP.setAttribute("ADD3", "");
                        ObjNRGP.setAttribute("CITY", "");
                        
                    } else {
                        ObjNRGP.setAttribute("PARTY_NAME", clsSupplier.getSupplierName(EITLERPGLOBAL.gCompanyID, (String) getAttribute("SUPP_ID").getObj()));
                        
                        clsSupplier ObjSupplier = (clsSupplier) clsSupplier.getObjectEx(EITLERPGLOBAL.gCompanyID, (String) getAttribute("SUPP_ID").getObj());
                        ObjNRGP.setAttribute("ADD1", (String) ObjSupplier.getAttribute("ADD1").getObj());
                        ObjNRGP.setAttribute("ADD2", (String) ObjSupplier.getAttribute("ADD2").getObj());
                        ObjNRGP.setAttribute("ADD3", (String) ObjSupplier.getAttribute("ADD3").getObj());
                        ObjNRGP.setAttribute("CITY", (String) ObjSupplier.getAttribute("CITY").getObj());
                        
                    }
                    
                    ObjNRGP.setAttribute("NRGP_WITH_LOT", false);
                    
                    ObjNRGP.setAttribute("CANCELED", false);
                    
                    //----- Update Approval Specific Fields -----------//
                    ObjNRGP.setAttribute("HIERARCHY_ID", NRGPHierarchy);
                    ObjNRGP.setAttribute("FROM", EITLERPGLOBAL.gUserID);
                    ObjNRGP.setAttribute("TO", EITLERPGLOBAL.gUserID);
                    ObjNRGP.setAttribute("FROM_REMARKS", "");
                    
                    ObjNRGP.setAttribute("APPROVAL_STATUS", "H");
                    //-------------------------------------------------//
                    
                    ObjNRGP.setAttribute("CREATED_BY", EITLERPGLOBAL.gUserID);
                    ObjNRGP.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                    
                    //=================== Setting up Line Items ==================//
                    ObjNRGP.colLineItems.clear();
                    Cnt = 0;
                    for (int i = 1; i <= colGRNItems.size(); i++) {
                        clsGRNGenItem ObjGRNItem = (clsGRNGenItem) colGRNItems.get(Integer.toString(i));
                        clsNRGPItem ObjNRGPItem = new clsNRGPItem();
                        
                        double RejectedQty = ObjGRNItem.getAttribute("REJECTED_QTY").getVal();
                        if (RejectedQty > 0) {
                            Cnt++;
                            ObjNRGPItem.setAttribute("SR_NO", Cnt);
                            ObjNRGPItem.setAttribute("ITEM_CODE", (String) ObjGRNItem.getAttribute("ITEM_ID").getObj());
                            ObjNRGPItem.setAttribute("WAREHOUSE_ID", (String) ObjGRNItem.getAttribute("WAREHOUSE_ID").getObj());
                            ObjNRGPItem.setAttribute("LOCATION_ID", (String) ObjGRNItem.getAttribute("LOCATION_ID").getObj());
                            ObjNRGPItem.setAttribute("NRGP_DESC", (String) ObjGRNItem.getAttribute("ITEM_EXTRA_DESC").getObj());
                            ObjNRGPItem.setAttribute("QTY", ObjGRNItem.getAttribute("REJECTED_QTY").getVal() + ObjGRNItem.getAttribute("EXCESS_QTY").getVal());
                            ObjNRGPItem.setAttribute("FREIGHT", 0);
                            ObjNRGPItem.setAttribute("OCTROI", 0);
                            ObjNRGPItem.setAttribute("UNIT", (int) ObjGRNItem.getAttribute("UNIT").getVal());
                            ObjNRGPItem.setAttribute("RATE", ObjGRNItem.getAttribute("RATE").getVal());
                            ObjNRGPItem.setAttribute("REMARKS", (String) ObjGRNItem.getAttribute("REMARKS").getObj());
                            ObjNRGPItem.setAttribute("RJN_NO", RJNNo);
                            ObjNRGPItem.setAttribute("RJN_SRNO", Cnt);
                            ObjNRGPItem.setAttribute("GATEPASSREQ_NO", "");
                            ObjNRGPItem.setAttribute("GATEPASSREQ_SRNO", 0);
                            ObjNRGPItem.setAttribute("DECLARATION_ID", "");
                            ObjNRGPItem.setAttribute("DECLARATION_SRNO", 0);
                            ObjNRGPItem.setAttribute("PACKING", "");
                            ObjNRGPItem.setAttribute("CREATED_BY", EITLERPGLOBAL.gUserID);
                            ObjNRGPItem.setAttribute("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            
                            ObjNRGP.colLineItems.put(Integer.toString(ObjNRGP.colLineItems.size() + 1), ObjNRGPItem);
                        }
                    }
                    
                    //========= Now insert the record ===========//
                    if (ObjNRGP.Insert("", "")) {
                        ErrorMessages += " " + ObjNRGP.LastError;
                        NRGPCreated = true;
                        NRGPNo = (String) ObjNRGP.getAttribute("GATEPASS_NO").getObj();
                        
                        data.Execute("UPDATE D_INV_RJN_HEADER SET GATEPASS_NO='" + NRGPNo + "',GATEPASS_DATE='" + (String) getAttribute("GRN_DATE").getObj() + "' WHERE RJN_NO='" + RJNNo + "'");
                    } else {
                        NRGPCreated = false;
                    }
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            
        }
        return (RJNCreated && NRGPCreated);
    }
    
    public static boolean CanCancel(int pCompanyID, String pDocNo) {
        ResultSet rsTmp = null;
        boolean canCancel = false;
        
        try {
            rsTmp = data.getResult("SELECT GRN_NO FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND GRN_NO='" + pDocNo + "' AND APPROVED=0 AND CANCELLED=0 AND GRN_TYPE=1");
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                canCancel = true;
            }
            
            rsTmp.close();
        } catch (Exception e) {
            
        }
        
        return canCancel;
        
    }
    
    public static boolean CancelGRN(int pCompanyID, String pDocNo) {
        
        ResultSet rsTmp = null, rsIndent = null;
        boolean Cancelled = false;
        
        try {
            
            if (CanCancel(pCompanyID, pDocNo)) {
                boolean ApprovedDoc = false;
                
                rsTmp = data.getResult("SELECT APPROVED FROM D_INV_GRN_HEADER WHERE COMPANY_ID=" + pCompanyID + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1");
                rsTmp.first();
                
                if (rsTmp.getRow() > 0) {
                    ApprovedDoc = rsTmp.getBoolean("APPROVED");
                }
                
                if (ApprovedDoc) {
                    rsTmp = data.getResult("SELECT * FROM D_INV_GRN_DETAIL WHERE COMPANY_ID=" + pCompanyID + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1");
                    rsTmp.first();
                    
                    if (rsTmp.getRow() > 0) {
                        
                        while (!rsTmp.isAfterLast()) {
                            String MIRNo = rsTmp.getString("MIR_NO");
                            int MIRSrNo = rsTmp.getInt("MIR_SR_NO");
                            
                            //============== Update P.O. ============ //
                            if ((!MIRNo.trim().equals("")) && (MIRSrNo > 0)) {
                                
                                //Reverse the Stock Effect
                                double Qty = rsTmp.getDouble("QTY");
                                String ItemID = rsTmp.getString("ITEM_ID");
                                String WarehouseID = rsTmp.getString("WAREHOUSE_ID");
                                String LocationID = rsTmp.getString("LOCATION_ID");
                                String BOENo = rsTmp.getString("BOE_NO");
                                String LOTNo = "X";
                                
                                String strSQL = "UPDATE D_INV_MIR_DETAIL SET GRN_RECD_QTY=GRN_RECD_QTY-" + Qty + " WHERE MIR_NO='" + MIRNo + "' AND SR_NO=" + MIRSrNo + " AND MIR_TYPE=1";
                                data.Execute(strSQL);
                                
                                strSQL = "UPDATE D_INV_ITEM_LOT_MASTER SET ON_HAND_QTY=ON_HAND_QTY-" + Qty + ",TOTAL_RECEIPT_QTY=TOTAL_RECEIPT_QTY-" + Qty + ",AVAILABLE_QTY=ON_HAND_QTY-ALLOCATED_QTY WHERE ITEM_ID='" + ItemID + "' AND WAREHOUSE_ID='" + WarehouseID + "' AND LOCATION_ID='" + LocationID + "' AND BOE_NO='" + BOENo + "' AND LOT_NO='" + LOTNo + "'";
                                data.Execute(strSQL);
                            }
                            //=============== P.O. Updation Completed ===============//
                            
                            rsTmp.next();
                        }
                        
                    }
                    
                } else {
                    //Remove it from Approval Hierarchy
                    data.Execute("DELETE FROM D_COM_DOC_DATA WHERE COMPANY_ID=" + pCompanyID + " AND DOC_NO='" + pDocNo + "' AND MODULE_ID=" + clsGRNGen.ModuleID);
                }
                
                //Now Update the header with cancel falg to true
                data.Execute("UPDATE D_INV_GRN_HEADER SET CANCELLED=1,CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND GRN_NO='" + pDocNo + "' AND GRN_TYPE=1");
                
                Cancelled = true;
            }
            
            rsTmp.close();
            rsIndent.close();
        } catch (Exception e) {
            
        }
        
        return Cancelled;
    }
    
    public static double getGRNTotalAmount(String DocNo, String dbURL) {
        
        double GRNAmount = 0;
        
        try {
            
            GRNAmount = data.getDoubleValueFromDB("SELECT SUM(NET_AMOUNT) AS AMOUNT FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + DocNo + "'", dbURL);
            
            ResultSet rsTemp = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + DocNo + "'", dbURL);
            rsTemp.first();
            
            if (rsTemp.getRow() >= 0) {
                GRNAmount = GRNAmount - UtilFunctions.getDouble(rsTemp, "COLUMN_1_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_2_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_6_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_9_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_18_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_20_AMT", 0);
                
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_3_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_4_AMT", 0);
                GRNAmount = GRNAmount - UtilFunctions.getDouble(rsTemp, "COLUMN_8_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_5_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_7_AMT", 0);
                
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_11_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_22_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_12_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_13_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_14_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_15_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_16_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_17_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_19_AMT", 0);
                
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_21_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_23_AMT", 0);
                GRNAmount += UtilFunctions.getDouble(rsTemp, "COLUMN_24_AMT", 0);
                
            }
        } catch (Exception e) {
            
        }
        
        return EITLERPGLOBAL.round(GRNAmount, 2);
    }
    
    public boolean PostVoucher(String DocNo, int DocType, String ExVoucherNo) {
        
        try {
            
            boolean VoucherPosted = true;
            
            //Load GRN
            clsGRNGen objGRN = (clsGRNGen) getObject(EITLERPGLOBAL.gCompanyID, DocNo, DocType);
            
            //*********** Select Voucher Hierarchy based on Rules *********//
            //Note: This routine overrides hierarchy selected by user in GRN
            //(1) Based on PO Type
            int POType = 0;
            
            String PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + DocNo + "' AND GRN_TYPE=" + DocType + " AND PO_NO<>''");
            
            if (!PONo.trim().equals("")) {
                POType = clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID, PONo);
                
                HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PO_TYPE", Integer.toString(POType));
                
                if (List.size() > 0) {
                    //Get the Result of the Rule which would be the hierarchy no.
                    clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                    int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                    setAttribute("FIN_HIERARCHY_ID", HierarchyID);
                }
            }
            
            //(2) Based on Payment Type
            int PaymentType = objGRN.getAttribute("PAYMENT_TYPE").getInt();
            
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }
            
            //(3) Based on Post Voucher Condition
            String SuppID = objGRN.getAttribute("SUPP_ID").getString();
            
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "SUPP_ID", SuppID);
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    return true; //Do not post voucher
                }
            }
            
            //(4) Based on Payment Type, decide whether to post voucher
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    return true; //Do not post voucher
                }
            }
            
            //******************* Hierarchy Selected **********************//
            clsVoucher objVoucher;
            clsVoucherItem objVoucherItem;
            
            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;
            
            String GRNNo = objGRN.getAttribute("GRN_NO").getString();
            String GRNDate = objGRN.getAttribute("GRN_DATE").getString();
            
            PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND PO_NO<>'' ");
            String PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "'");
            String InvoiceNo = objGRN.getAttribute("INVOICE_NO").getString();
            String InvoiceDate = objGRN.getAttribute("INVOICE_DATE").getString();
            String PartyCode = objGRN.getAttribute("SUPP_ID").getString();
            double InvoiceAmount = 0;
            
            //Get MIR No.
            String MIRNo = data.getStringValueFromDB("SELECT MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=1");
            
            if (!MIRNo.trim().equals("")) {
                InvoiceAmount = data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_INV_MIR_HEADER WHERE MIR_NO='" + MIRNo + "' AND MIR_TYPE=1");
            }
            
            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.PJVModuleID);
            rsVoucher.first();
            
            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }
            
            //========================== Amount Bifurcations =========================//
            double Discount = 0, PF = 0, Freight = 0, FCA = 0, Insurance = 0, AirFreight = 0, Others = 0, Add = 0, FShipping = 0;
            double Excise = 0, Cess = 0, Cenvat = 0, ST = 0, Octroi = 0, GrossAmount = 0;
            double CustomDuty = 0, LessDuty = 0, ImportEduCess = 0, CVD = 0, Surcharge = 0, CustomEduCess = 0, SpAddDuty = 0, SpExcise = 0, Clear = 0;
            double ToPayAmount = 0, VoucherSrNo = 0;
            String ExcisePer = "", STPer = "", CustomsPer = "", OctroiPer = "";
            String DefaultMainCode = "";
            
            for (int i = 1; i <= objGRN.colGRNItems.size(); i++) {
                clsGRNGenItem objItem = (clsGRNGenItem) objGRN.colGRNItems.get(Integer.toString(i));
                
                Discount += objItem.getAttribute("COLUMN_1_AMT").getDouble();
                
                //Add Other Discounts
                Discount += objItem.getAttribute("COLUMN_27_AMT").getDouble(); //Trade Discount
                Discount += objItem.getAttribute("COLUMN_28_AMT").getDouble(); //R&D Deduction
                
                PF += objItem.getAttribute("COLUMN_2_AMT").getDouble();
                Freight += objItem.getAttribute("COLUMN_6_AMT").getDouble();
                FCA += objItem.getAttribute("COLUMN_9_AMT").getDouble();
                Insurance += objItem.getAttribute("COLUMN_18_AMT").getDouble();
                AirFreight += objItem.getAttribute("COLUMN_20_AMT").getDouble();
                
                Excise += objItem.getAttribute("COLUMN_3_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_3_PER").getDouble() > 0) {
                    ExcisePer = Double.toString(objItem.getAttribute("COLUMN_3_PER").getDouble());
                }
                
                Cess += objItem.getAttribute("COLUMN_4_AMT").getDouble();
                Cenvat += objItem.getAttribute("COLUMN_8_AMT").getDouble();
                ST += objItem.getAttribute("COLUMN_5_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_5_PER").getDouble() > 0) {
                    STPer = Double.toString(objItem.getAttribute("COLUMN_5_PER").getDouble());
                }
                
                Octroi += objItem.getAttribute("COLUMN_7_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_7_PER").getDouble() > 0) {
                    OctroiPer = Double.toString(objItem.getAttribute("COLUMN_7_PER").getDouble());
                }
                
                CustomDuty += objItem.getAttribute("COLUMN_11_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_11_PER").getDouble() > 0) {
                    CustomsPer = Double.toString(objItem.getAttribute("COLUMN_11_PER").getDouble());
                }
                
                Others += objItem.getAttribute("COLUMN_21_AMT").getDouble();
                LessDuty += objItem.getAttribute("COLUMN_22_AMT").getDouble();
                ImportEduCess += objItem.getAttribute("COLUMN_12_AMT").getDouble();
                CVD += objItem.getAttribute("COLUMN_13_AMT").getDouble();
                Surcharge += objItem.getAttribute("COLUMN_14_AMT").getDouble();
                CustomEduCess += objItem.getAttribute("COLUMN_15_AMT").getDouble();
                SpAddDuty += objItem.getAttribute("COLUMN_16_AMT").getDouble();
                SpExcise += objItem.getAttribute("COLUMN_17_AMT").getDouble();
                Clear += objItem.getAttribute("COLUMN_19_AMT").getDouble();
                
                GrossAmount += objItem.getAttribute("TOTAL_AMOUNT").getDouble();
            }
            //========================================================================//
            
            Discount += objGRN.getAttribute("COLUMN_1_AMT").getDouble();
            PF += objGRN.getAttribute("COLUMN_2_AMT").getDouble();
            Freight += objGRN.getAttribute("COLUMN_6_AMT").getDouble();
            FCA += objGRN.getAttribute("COLUMN_9_AMT").getDouble();
            Insurance += objGRN.getAttribute("COLUMN_18_AMT").getDouble();
            AirFreight += objGRN.getAttribute("COLUMN_20_AMT").getDouble();
            
            Excise += objGRN.getAttribute("COLUMN_3_AMT").getDouble();
            Cess += objGRN.getAttribute("COLUMN_4_AMT").getDouble();
            Cenvat += objGRN.getAttribute("COLUMN_8_AMT").getDouble();
            ST += objGRN.getAttribute("COLUMN_5_AMT").getDouble();
            Octroi += objGRN.getAttribute("COLUMN_7_AMT").getDouble();
            
            CustomDuty += objGRN.getAttribute("COLUMN_11_AMT").getDouble();
            LessDuty += objGRN.getAttribute("COLUMN_22_AMT").getDouble();
            ImportEduCess += objGRN.getAttribute("COLUMN_12_AMT").getDouble();
            CVD += objGRN.getAttribute("COLUMN_13_AMT").getDouble();
            Surcharge += objGRN.getAttribute("COLUMN_14_AMT").getDouble();
            CustomEduCess += objGRN.getAttribute("COLUMN_15_AMT").getDouble();
            SpAddDuty += objGRN.getAttribute("COLUMN_16_AMT").getDouble();
            SpExcise += objGRN.getAttribute("COLUMN_17_AMT").getDouble();
            Clear += objGRN.getAttribute("COLUMN_19_AMT").getDouble();
            
            Others += objGRN.getAttribute("COLUMN_21_AMT").getDouble();
            Add = objGRN.getAttribute("COLUMN_23_AMT").getDouble();
            FShipping = objGRN.getAttribute("COLUMN_24_AMT").getDouble();
            
            DefaultMainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
            
            //(1) PJV 1
            //double Entry1=EITLERPGLOBAL.round((GrossAmount-Discount)+PF+Freight+FCA+Insurance+AirFreight+Others+FShipping,3);
            double Entry1 = EITLERPGLOBAL.round((GrossAmount - Discount) + FCA + FShipping, 3);
            
            //Check whether to Credit Amounts to Party or to Us
            if (objGRN.getAttribute("PF_POST").getInt() == 0) //Packing & Forwarding
            {
                Entry1 += PF;
            }
            
            if (objGRN.getAttribute("FREIGHT_POST").getInt() == 0) //Freight
            {
                Entry1 += Freight;
            }
            
            if (objGRN.getAttribute("INSURANCE_POST").getInt() == 0) //Insurance
            {
                Entry1 += Insurance;
            }
            
            if (objGRN.getAttribute("AIR_FREIGHT_POST").getInt() == 0) //Air Freight
            {
                Entry1 += AirFreight;
            }
            
            if (objGRN.getAttribute("OTHERS_POST").getInt() == 0) //Other Expenses
            {
                Entry1 += Others;
            }
            
            double Entry2 = EITLERPGLOBAL.round(Excise + Cess + SpExcise, 2);
            double Entry3 = EITLERPGLOBAL.round(ST, 2);
            double Entry4 = 0;
            
            if (objGRN.getAttribute("OCTROI_POST").getInt() == 0) {
                Entry4 = EITLERPGLOBAL.round(Octroi, 2);
            }
            
            //=======Normal PJV Entry #1 ==========//
            VoucherSrNo = 0;
            
            objVoucher = new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
            
            if (EITLERPGLOBAL.gCompanyID == 2) {
                objVoucher.setAttribute("BOOK_CODE", "40");
            } else {
                objVoucher.setAttribute("BOOK_CODE", "61");
            }
            
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", PONo);
            objVoucher.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucher.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucher.setAttribute("GRN_NO", GRNNo);
            objVoucher.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucher.setAttribute("REMARKS", "");
            
            objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
            objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
            
            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            
            //Get Debit Entries
            Entry1 = 0;
            Entry2 = 0;
            Entry3 = 0;
            Entry4 = 0;
            HashMap debitEntries = getDebitPJVEntries(GRNNo, 1, PONo, PODate, InvoiceNo, InvoiceDate, InvoiceAmount, GRNDate);
            
            for (int e = 1; e <= debitEntries.size(); e++) {
                VoucherSrNo++;
                clsVoucherItem objItem = (clsVoucherItem) debitEntries.get(Integer.toString(e));
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 1) {
                    Entry1 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 2) {
                    Entry2 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 3) {
                    Entry3 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 4) {
                    Entry4 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                //Entry1+=objItem.getAttribute("AMOUNT").getDouble();
                DefaultMainCode = objItem.getAttribute("MAIN_ACCOUNT_CODE").getString();
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objItem);
            }
            
            //Round the Entry 1
            Entry1 = EITLERPGLOBAL.round(Entry1, 2);
            
            VoucherSrNo++;
            objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "C");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "125019");
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", Entry1 + Entry2 + Entry3 + Entry4);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            
            objVoucher.DoNotValidateAccounts = true;
            
            ToPayAmount = 0;
            
            if (Entry1 > 0 || Entry2 > 0 || Entry3 > 0 || Entry4 > 0) {
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    System.out.println(objVoucher.LastError);
                    VoucherPosted = false;
                }
            }
            
            String VoucherNo = objVoucher.getAttribute("VOUCHER_NO").getString();
            //=====================================//
            
            //(2) PJV 2
            double Entry5 = EITLERPGLOBAL.round(CustomDuty - LessDuty + ImportEduCess + CVD + Surcharge + CustomEduCess + SpAddDuty + Add, 3);
            
            if (Entry5 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Customs Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", CustomsPer + "% Customs Duty");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            //(3) PJV 3
            double Entry6 = EITLERPGLOBAL.round(Clear, 3);
            
            if (Entry6 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00"); // ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Clearance Agent Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Clearance Charges");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            return VoucherPosted;
        } catch (Exception e) {
            
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean IsPostingNecessary(String DocNo, int DocType, String ExVoucherNo) {
        
        try {
            
            //Load GRN
            clsGRNGen objGRN = (clsGRNGen) getObject(EITLERPGLOBAL.gCompanyID, DocNo, DocType);
            
            //*********** Select Voucher Hierarchy based on Rules *********//
            //Note: This routine overrides hierarchy selected by user in GRN
            //(2) Based on Payment Type
            int PaymentType = objGRN.getAttribute("PAYMENT_TYPE").getInt();
            
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }
            
            //(3) Based on Post Voucher Condition
            String SuppID = objGRN.getAttribute("SUPP_ID").getString();
            
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "SUPP_ID", SuppID);
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    return false; //Do not post voucher
                }
            }
            
            //(4) Based on Payment Type, decide whether to post voucher
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    return false; //Do not post voucher
                }
            }
            //******************* Hierarchy Selected **********************//
            return true;
        } catch (Exception e) {
            return true;
        }
        
    }
    
    public HashMap getDebitPJVEntries(String GRNNo, int GRNType, String PONo, String PODate, String InvoiceNo, String InvoiceDate, double InvoiceAmount, String GRNDate) {
        
        HashMap DebitEntries = new HashMap();
        
        try {
            
            ResultSet rsGRN, rsTmp, rsHeader;
            int VoucherSrNo = 0;
            
            //Get Unique Items from GRN based on Itemwise Account Heads
            String strSQL = "";
            
            strSQL = "SELECT DISTINCT(ITEM_CODE) ";
            strSQL += "FROM ";
            strSQL += "D_FIN_ITEM_CODE_MAPPING A, ";
            strSQL += EITLERPGLOBAL.DBName + ".D_INV_GRN_DETAIL B ";
            strSQL += "WHERE ";
            strSQL += "B.GRN_NO='" + GRNNo + "' AND B.GRN_TYPE=" + GRNType + " AND ";
            strSQL += "SUBSTRING(B.ITEM_ID,1,LENGTH(A.ITEM_CODE))=A.ITEM_CODE AND A.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ";
            
            ResultSet rsItems = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsItems.first();
            
            //Get GRN Header
            rsGRN = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            
            double TotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            double FreightAmount = 0;
            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 1) {//Freight
                FreightAmount = data.getDoubleValueFromDB("SELECT COLUMN_6_AMT FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                FreightAmount += data.getDoubleValueFromDB("SELECT SUM(COLUMN_6_AMT) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            }
            TotalNetAmount = TotalNetAmount - FreightAmount;
            double ItemTotalNetAmount = 0;
            double ItemWeightage = 0;
            double HeaderAmount = getHeaderAmount(GRNNo, GRNType);
            double ItemGrossAmount = 0;
            double AccountedAmount = 0;
            
            double hExcise = 0, dExcise = 0, totalExcise = 0;
            double hCess = 0, dCess = 0, totalCess = 0;
            double hCenvat = 0, dCenvat = 0, totalCenvat;
            double hST = 0, dST = 0, totalST = 0;
            double hOctroi = 0, dOctroi = 0, totalOctroi = 0;
            double hSpExcise = 0, dSpExcise = 0, totalSpExcise = 0;
            
            //Derive header level Tax Amounts
            rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            rsHeader.first();
            if (rsHeader.getRow() > 0) {
                hExcise = UtilFunctions.getDouble(rsHeader, "COLUMN_3_AMT", 0);
                hCess = UtilFunctions.getDouble(rsHeader, "COLUMN_4_AMT", 0);
                hCenvat = UtilFunctions.getDouble(rsHeader, "COLUMN_8_AMT", 0);
                hST = UtilFunctions.getDouble(rsHeader, "COLUMN_5_AMT", 0);
                hOctroi = UtilFunctions.getDouble(rsHeader, "COLUMN_7_AMT", 0);
                hSpExcise = UtilFunctions.getDouble(rsHeader, "COLUMN_17_AMT", 0);
            }
            
            //Derive line level tax Amounts
            rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_9_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_6_AMT) AS FREIGHT,SUM(COLUMN_18_AMT) AS INSURANCE,SUM(COLUMN_20_AMT) AS AIR_FREIGHT,SUM(COLUMN_21_AMT) AS OTHERS,SUM(COLUMN_27_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_28_AMT) AS RND_DISCOUNT,SUM(COLUMN_3_AMT) AS EXCISE,SUM(COLUMN_4_AMT) AS CESS,SUM(COLUMN_8_AMT) AS CENVAT,SUM(COLUMN_5_AMT) AS ST,SUM(COLUMN_7_AMT) AS OCTROI,SUM(COLUMN_17_AMT) AS SP_EXCISE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                dExcise = UtilFunctions.getDouble(rsTmp, "EXCISE", 0);
                dCess = UtilFunctions.getDouble(rsTmp, "CESS", 0);
                dCenvat = UtilFunctions.getDouble(rsTmp, "CENVAT", 0);
                dST = UtilFunctions.getDouble(rsTmp, "ST", 0);
                dOctroi = UtilFunctions.getDouble(rsTmp, "OCTROI", 0);
                dSpExcise = UtilFunctions.getDouble(rsTmp, "SP_EXCISE", 0);
                
            }
            
            totalExcise = hExcise + dExcise;
            totalCess = hCess + dCess;
            totalCenvat = hCenvat + dCenvat;
            totalST = hST + dST;
            totalOctroi = hOctroi + dOctroi;
            totalSpExcise = hSpExcise + dSpExcise;
            
            if (TotalNetAmount > 0) {
                if (rsItems.getRow() > 0) {
                    while (!rsItems.isAfterLast()) {
                        //Get the Group
                        String ItemGroup = rsItems.getString("ITEM_CODE");
                        String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, ItemGroup);
                        
                        //Calculate Weightage (Pro-rata basis) of the items having this group
                        ItemTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                        ItemWeightage = (ItemTotalNetAmount * 100) / TotalNetAmount;
                        
                        //Calculate Item level distribution of Total Header Amount
                        ItemGrossAmount = (HeaderAmount * ItemWeightage) / 100;
                        
                        rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_9_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_6_AMT) AS FREIGHT,SUM(COLUMN_18_AMT) AS INSURANCE,SUM(COLUMN_20_AMT) AS AIR_FREIGHT,SUM(COLUMN_21_AMT) AS OTHERS,SUM(COLUMN_27_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_28_AMT) AS RND_DISCOUNT,SUM(COLUMN_3_AMT) AS EXCISE,SUM(COLUMN_4_AMT) AS CESS,SUM(COLUMN_8_AMT) AS CENVAT,SUM(COLUMN_5_AMT) AS ST,SUM(COLUMN_7_AMT) AS OCTROI,SUM(COLUMN_17_AMT) AS SP_EXCISE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                        rsTmp.first();
                        if (rsTmp.getRow() > 0) {
                            
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "TRADE_DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "RND_DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "FCA", 0);
                            ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "GROSS_AMOUNT", 0);
                            
                            if (UtilFunctions.getInt(rsGRN, "PF_POST", 0) == 0) //Packing & Forwarding
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "PF", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 0) //Freight
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "FREIGHT", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "INSURANCE_POST", 0) == 0) //Insurance
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "INSURANCE", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "AIR_FREIGHT_POST", 0) == 0) //Air Freight
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "AIR_FREIGHT", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "OTHERS_POST", 0) == 0) //Other Expenses
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "OTHERS", 0);
                            }
                            
                            double Entry2 = ((hExcise * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "EXCISE", 0)) + ((hCess * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "CESS", 0)) + ((hSpExcise * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "SP_EXCISE", 0));
                            double Entry3 = ((hST * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "ST", 0));
                            double Entry4 = ((hOctroi * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "OCTROI", 0));
                            
                            if (Entry2 < 0) {
                                ItemGrossAmount = ItemGrossAmount - Math.abs(Entry2);
                                Entry2 = 0;
                            }
                            
                            totalExcise = totalExcise - (((hExcise * ItemWeightage) / 100) + UtilFunctions.getDouble(rsTmp, "EXCISE", 0));
                            totalST = totalST - (((hST * ItemWeightage) / 100) + UtilFunctions.getDouble(rsTmp, "ST", 0));
                            totalOctroi = totalOctroi - (((hOctroi * ItemWeightage) / 100) + UtilFunctions.getDouble(rsTmp, "OCTROI", 0));
                            
                            //Do the Entry
                            AccountedAmount += ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "ST", 0);
                            
                            clsVoucherItem objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(ItemGrossAmount, 2));
                            objVoucherItem.setAttribute("REMARKS", "");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            
                            //**** Include Sales Tax, Excise and Octroi Amounts bifurcations *******//
                            if (Entry2 > 0) {
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry2, 2));
                                objVoucherItem.setAttribute("REMARKS", "Excise");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (Entry3 > 0) {
                                double STPercentage = data.getDoubleValueFromDB("SELECT COLUMN_5_PER FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                                String ExCode = data.getStringValueFromDB("SELECT EX_ACCOUNT_CODE FROM D_FIN_EX_ACCOUNT_MASTER WHERE PERCENTAGE=" + STPercentage, FinanceGlobal.FinURL);
                                String STAcCode = MainCode;
                                
                                if (!ExCode.trim().equals("")) {
                                    STAcCode = STAcCode + "." + ExCode;
                                }
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", STAcCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry3, 2));
                                objVoucherItem.setAttribute("REMARKS", "ST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 3);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (rsHeader.getInt("OCTROI_POST") == 0) {
                                if (Entry4 > 0) {
                                    if (Entry2 > 0) {
                                        objVoucherItem = new clsVoucherItem();
                                        VoucherSrNo++;
                                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                        objVoucherItem.setAttribute("EFFECT", "D");
                                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry4, 2));
                                        objVoucherItem.setAttribute("REMARKS", "Octroi");
                                        objVoucherItem.setAttribute("PO_NO", PONo);
                                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                        objVoucherItem.setAttribute("ENTRY_TYPE", 4);
                                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                    }
                                }
                            }
                            //**********************************************************************//
                        }
                        rsItems.next();
                    }
                    
                    //Add Default
                    double Diff = EITLERPGLOBAL.round(TotalNetAmount - AccountedAmount, 2);
                    
                    if (EITLERPGLOBAL.round(Diff, 2) > 0) {
                        
                        String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                        
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Diff, 2));
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                        objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        
                        //************************** Include Difference Tax Amounts ***************************//
                        double Entry2 = totalExcise + totalCess + totalSpExcise;
                        double Entry3 = totalST;
                        double Entry4 = totalOctroi;
                        
                        if (Entry2 > 0) {
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry2, 2));
                            objVoucherItem.setAttribute("REMARKS", "Excise");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (Entry3 > 0) {
                            double STPercentage = data.getDoubleValueFromDB("SELECT COLUMN_5_PER FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                            String ExCode = data.getStringValueFromDB("SELECT EX_ACCOUNT_CODE FROM D_FIN_EX_ACCOUNT_MASTER WHERE PERCENTAGE=" + STPercentage, FinanceGlobal.FinURL);
                            String STAcCode = MainCode;
                            
                            if (!ExCode.trim().equals("")) {
                                STAcCode = STAcCode + "." + ExCode;
                            }
                            
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", STAcCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry3, 2));
                            objVoucherItem.setAttribute("REMARKS", "ST");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 3);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (rsHeader.getInt("OCTROI_POST") == 0) {
                            if (Entry4 > 0) {
                                if (Entry2 > 0) {
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry4, 2));
                                    objVoucherItem.setAttribute("REMARKS", "Octroi");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 4);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                            }
                        }
                        
                        //*************************************************************************************//
                    }
                    
                } else {
                    //Default Account Header
                    
                    //Get the Group
                    String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                    
                    //Calculate Weightage (Pro-rata basis) of the items having this group
                    ItemTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                    ItemWeightage = (ItemTotalNetAmount * 100) / TotalNetAmount;
                    
                    //Calculate Item level distribution of Total Header Amount
                    ItemGrossAmount = (HeaderAmount * ItemWeightage) / 100;
                    
                    double Excise = 0;
                    double Cess = 0;
                    double Cenvat = 0;
                    double ST = 0;
                    double Octroi = 0;
                    double SpExcise = 0;
                    
                    rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                    rsHeader.first();
                    if (rsHeader.getRow() > 0) {
                        Excise = (UtilFunctions.getDouble(rsHeader, "COLUMN_3_AMT", 0) * ItemWeightage) / 100;
                        Cess = (UtilFunctions.getDouble(rsHeader, "COLUMN_4_AMT", 0) * ItemWeightage) / 100;
                        Cenvat = (UtilFunctions.getDouble(rsHeader, "COLUMN_8_AMT", 0) * ItemWeightage) / 100;
                        ST = (UtilFunctions.getDouble(rsHeader, "COLUMN_5_AMT", 0) * ItemWeightage) / 100;
                        Octroi = (UtilFunctions.getDouble(rsHeader, "COLUMN_7_AMT", 0) * ItemWeightage) / 100;
                        SpExcise = (UtilFunctions.getDouble(rsHeader, "COLUMN_17_AMT", 0) * ItemWeightage) / 100;
                    }
                    
                    rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_9_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_6_AMT) AS FREIGHT,SUM(COLUMN_18_AMT) AS INSURANCE,SUM(COLUMN_20_AMT) AS AIR_FREIGHT,SUM(COLUMN_21_AMT) AS OTHERS,SUM(COLUMN_27_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_28_AMT) AS RND_DISCOUNT,SUM(COLUMN_3_AMT) AS EXCISE,SUM(COLUMN_4_AMT) AS CESS,SUM(COLUMN_8_AMT) AS CENVAT,SUM(COLUMN_5_AMT) AS ST,SUM(COLUMN_7_AMT) AS OCTROI,SUM(COLUMN_17_AMT) AS SP_EXCISE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                    rsTmp.first();
                    if (rsTmp.getRow() > 0) {
                        
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "TRADE_DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "RND_DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "FCA", 0);
                        ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "GROSS_AMOUNT", 0);
                        
                        Excise += UtilFunctions.getDouble(rsTmp, "EXCISE", 0);
                        Cess += UtilFunctions.getDouble(rsTmp, "CESS", 0);
                        Cenvat += UtilFunctions.getDouble(rsTmp, "CENVAT", 0);
                        ST += UtilFunctions.getDouble(rsTmp, "ST", 0);
                        Octroi += UtilFunctions.getDouble(rsTmp, "OCTROI", 0);
                        SpExcise += UtilFunctions.getDouble(rsTmp, "SP_EXCISE", 0);
                        
                        if (UtilFunctions.getInt(rsGRN, "PF_POST", 0) == 0) //Packing & Forwarding
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "PF", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 0) //Freight
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "FREIGHT", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "INSURANCE_POST", 0) == 0) //Insurance
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "INSURANCE", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "AIR_FREIGHT_POST", 0) == 0) //Air Freight
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "AIR_FREIGHT", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "OTHERS_POST", 0) == 0) //Other Expenses
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "OTHERS", 0);
                        }
                        
                        //Do the Entry
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(ItemGrossAmount, 2));
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        
                        //**** Include Sales Tax, Excise and Octroi Amounts bifurcations *******//
                        double Entry2 = Excise + Cess + SpExcise;
                        double Entry3 = ST;
                        double Entry4 = Octroi;
                        
                        if (Entry2 > 0) {
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry2, 2));
                            objVoucherItem.setAttribute("REMARKS", "Excise");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (Entry3 > 0) {
                            double STPercentage = data.getDoubleValueFromDB("SELECT COLUMN_5_PER FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                            String ExCode = data.getStringValueFromDB("SELECT EX_ACCOUNT_CODE FROM D_FIN_EX_ACCOUNT_MASTER WHERE PERCENTAGE=" + STPercentage, FinanceGlobal.FinURL);
                            String STAcCode = MainCode;
                            
                            if (!ExCode.trim().equals("")) {
                                STAcCode = STAcCode + "." + ExCode;
                            }
                            
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", STAcCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry3, 2));
                            objVoucherItem.setAttribute("REMARKS", "ST");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 3);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (rsHeader.getInt("OCTROI_POST") == 0) {
                            if (Entry4 > 0) {
                                if (Entry2 > 0) {
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry4, 2));
                                    objVoucherItem.setAttribute("REMARKS", "Octroi");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 4);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                            }
                        }
                        //**********************************************************************//
                        
                    }
                    
                }
                
            }
        } catch (Exception e) {
            
        }
        
        return DebitEntries;
    }
    
    public double getHeaderAmount(String GRNNo, int GRNType) {
        double HeaderAmount = 0;
        
        try {
            
            ResultSet rsTmp = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                
                HeaderAmount = HeaderAmount - UtilFunctions.getDouble(rsTmp, "COLUMN_1_AMT", 0); //Discount
                HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_9_AMT", 0); //FCA
                HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_23_AMT", 0); //Additional
                HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_24_AMT", 0); //FShipping
                
                if (UtilFunctions.getInt(rsTmp, "PF_POST", 0) == 0) //Packing & Forwarding
                {
                    HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_2_AMT", 0); //PF
                }
                
                if (UtilFunctions.getInt(rsTmp, "FREIGHT_POST", 0) == 0) //Freight
                {
                    HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_6_AMT", 0); //Freight
                }
                
                if (UtilFunctions.getInt(rsTmp, "INSURANCE_POST", 0) == 0) //Insurance
                {
                    HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_18_AMT", 0); //Insurance
                }
                
                if (UtilFunctions.getInt(rsTmp, "AIR_FREIGHT_POST", 0) == 0) //Air Freight
                {
                    HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_20_AMT", 0); //Air Freight
                }
                
                if (UtilFunctions.getInt(rsTmp, "OTHERS_POST", 0) == 0) //Other Expenses
                {
                    HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_21_AMT", 0); //Others
                }
                
                if (UtilFunctions.getInt(rsTmp, "OCTROI_POST", 0) == 0) {
                    HeaderAmount = HeaderAmount + UtilFunctions.getDouble(rsTmp, "COLUMN_7_AMT", 0); //Octroi
                }
                
            }
        } catch (Exception e) {
        }
        return HeaderAmount;
    }
    
    public boolean PostVoucherNonGST(String DocNo, int DocType, String ExVoucherNo) {
        
        try {
            
            boolean VoucherPosted = true;
            
            //Load GRN
            clsGRNGen objGRN = (clsGRNGen) getObject(EITLERPGLOBAL.gCompanyID, DocNo, DocType);
            
            //*********** Select Voucher Hierarchy based on Rules *********//
            //Note: This routine overrides hierarchy selected by user in GRN
            //(1) Based on PO Type
            int POType = 0;
            
            String PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + DocNo + "' AND GRN_TYPE=" + DocType + " AND PO_NO<>''");
            
            if (!PONo.trim().equals("")) {
                POType = clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID, PONo);
                
                HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PO_TYPE", Integer.toString(POType));
                
                if (List.size() > 0) {
                    //Get the Result of the Rule which would be the hierarchy no.
                    clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                    int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                    setAttribute("FIN_HIERARCHY_ID", HierarchyID);
                }
            }
            
            //(2) Based on Payment Type
            int PaymentType = objGRN.getAttribute("PAYMENT_TYPE").getInt();
            
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }
            
            //(3) Based on Post Voucher Condition
            String SuppID = objGRN.getAttribute("SUPP_ID").getString();
            
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "SUPP_ID", SuppID);
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    return true; //Do not post voucher
                }
            }
            
            //(4) Based on Payment Type, decide whether to post voucher
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    chkPostVoucher = PostVoucher; //added on 30/12/2020 to final approve GRN without posting PJV
                    
                    return true; //Do not post voucher
                }
            }
            
            //******************* Hierarchy Selected **********************//
            clsVoucher objVoucher;
            clsVoucherItem objVoucherItem;
            
            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;
            
            String GRNNo = objGRN.getAttribute("GRN_NO").getString();
            String GRNDate = objGRN.getAttribute("GRN_DATE").getString();
            
            PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND PO_NO<>'' ");
            String PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "'");
            String InvoiceNo = objGRN.getAttribute("INVOICE_NO").getString();
            String InvoiceDate = objGRN.getAttribute("INVOICE_DATE").getString();
            String PartyCode = objGRN.getAttribute("SUPP_ID").getString();
            double InvoiceAmount = 0;
            
            //Get MIR No.
            String MIRNo = data.getStringValueFromDB("SELECT MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=1");
            
            if (!MIRNo.trim().equals("")) {
                InvoiceAmount = data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_INV_MIR_HEADER WHERE MIR_NO='" + MIRNo + "' AND MIR_TYPE=1");
            }
            
            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.PJVModuleID);
            rsVoucher.first();
            
            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }
            
            //========================== Amount Bifurcations =========================//
            double Discount = 0, PF = 0, Freight = 0, FCA = 0, Insurance = 0, AirFreight = 0, Others = 0, Add = 0, FShipping = 0;
            double Excise = 0, Cess = 0, Cenvat = 0, ST = 0, Octroi = 0, GrossAmount = 0;
            double CustomDuty = 0, LessDuty = 0, ImportEduCess = 0, CVD = 0, Surcharge = 0, CustomEduCess = 0, SpAddDuty = 0, SpExcise = 0, Clear = 0;
            double ToPayAmount = 0, VoucherSrNo = 0;
            String ExcisePer = "", STPer = "", CustomsPer = "", OctroiPer = "";
            String DefaultMainCode = "";
            
            for (int i = 1; i <= objGRN.colGRNItems.size(); i++) {
                clsGRNGenItem objItem = (clsGRNGenItem) objGRN.colGRNItems.get(Integer.toString(i));
                
                Discount += objItem.getAttribute("COLUMN_1_AMT").getDouble();
                Discount += objItem.getAttribute("COLUMN_1_AMT").getDouble();
                
                //Add Other Discounts
                //  Discount+=objItem.getAttribute("COLUMN_27_AMT").getDouble(); //Trade Discount
                // Discount+=objItem.getAttribute("COLUMN_28_AMT").getDouble(); //R&D Deduction
                //Add Other Discounts
                Discount += objItem.getAttribute("COLUMN_37_AMT").getDouble(); //Trade Discount
                Discount += objItem.getAttribute("COLUMN_38_AMT").getDouble(); //R&D Deduction
                
                PF += objItem.getAttribute("COLUMN_2_AMT").getDouble();
                //old rishi  Freight+=objItem.getAttribute("COLUMN_6_AMT").getDouble();
                Freight += objItem.getAttribute("COLUMN_15_AMT").getDouble();
                
                //FCA+=objItem.getAttribute("COLUMN_9_AMT").getDouble();
                FCA += objItem.getAttribute("COLUMN_18_AMT").getDouble();
                // Insurance+=objItem.getAttribute("COLUMN_18_AMT").getDouble();
                Insurance += objItem.getAttribute("COLUMN_28_AMT").getDouble();
                
                // AirFreight+=objItem.getAttribute("COLUMN_20_AMT").getDouble();
                AirFreight += objItem.getAttribute("COLUMN_30_AMT").getDouble();
                
                //Excise+=objItem.getAttribute("COLUMN_3_AMT").getDouble();
                Excise += objItem.getAttribute("COLUMN_12_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_12_PER").getDouble() > 0) {
                    ExcisePer = Double.toString(objItem.getAttribute("COLUMN_12_PER").getDouble());
                }
                
                Cess += objItem.getAttribute("COLUMN_13_AMT").getDouble();
                Cenvat += objItem.getAttribute("COLUMN_17_AMT").getDouble();
                ST += objItem.getAttribute("COLUMN_14_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_14_PER").getDouble() > 0) {
                    STPer = Double.toString(objItem.getAttribute("COLUMN_14_PER").getDouble());
                }
                
                Octroi += objItem.getAttribute("COLUMN_16_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_16_PER").getDouble() > 0) {
                    OctroiPer = Double.toString(objItem.getAttribute("COLUMN_16_PER").getDouble());
                }
                
                CustomDuty += objItem.getAttribute("COLUMN_20_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_20_PER").getDouble() > 0) {
                    CustomsPer = Double.toString(objItem.getAttribute("COLUMN_20_PER").getDouble());
                }
                
                Others += objItem.getAttribute("COLUMN_31_AMT").getDouble();
                LessDuty += objItem.getAttribute("COLUMN_32_AMT").getDouble();
                ImportEduCess += objItem.getAttribute("COLUMN_21_AMT").getDouble();
                CVD += objItem.getAttribute("COLUMN_22_AMT").getDouble();
                
                Surcharge += objItem.getAttribute("COLUMN_23_AMT").getDouble();
                
                CustomEduCess += objItem.getAttribute("COLUMN_24_AMT").getDouble();
                
                SpAddDuty += objItem.getAttribute("COLUMN_25_AMT").getDouble();
                SpExcise += objItem.getAttribute("COLUMN_26_AMT").getDouble();
                Clear += objItem.getAttribute("COLUMN_29_AMT").getDouble();
                
                GrossAmount += objItem.getAttribute("TOTAL_AMOUNT").getDouble();
            }
            //========================================================================//
            
            Discount += objGRN.getAttribute("COLUMN_1_AMT").getDouble();
            PF += objGRN.getAttribute("COLUMN_2_AMT").getDouble();
            Freight += objGRN.getAttribute("COLUMN_15_AMT").getDouble();
            FCA += objGRN.getAttribute("COLUMN_18_AMT").getDouble();
            Insurance += objGRN.getAttribute("COLUMN_28_AMT").getDouble();
            AirFreight += objGRN.getAttribute("COLUMN_30_AMT").getDouble();
            
            Excise += objGRN.getAttribute("COLUMN_12_AMT").getDouble();
            Cess += objGRN.getAttribute("COLUMN_13_AMT").getDouble();
            Cenvat += objGRN.getAttribute("COLUMN_17_AMT").getDouble();
            ST += objGRN.getAttribute("COLUMN_14_AMT").getDouble();
            Octroi += objGRN.getAttribute("COLUMN_16_AMT").getDouble();
            
            CustomDuty += objGRN.getAttribute("COLUMN_20_AMT").getDouble();
            LessDuty += objGRN.getAttribute("COLUMN_32_AMT").getDouble();
            ImportEduCess += objGRN.getAttribute("COLUMN_21_AMT").getDouble();
            CVD += objGRN.getAttribute("COLUMN_22_AMT").getDouble();
            Surcharge += objGRN.getAttribute("COLUMN_23_AMT").getDouble();
            CustomEduCess += objGRN.getAttribute("COLUMN_24_AMT").getDouble();
            SpAddDuty += objGRN.getAttribute("COLUMN_25_AMT").getDouble();
            SpExcise += objGRN.getAttribute("COLUMN_26_AMT").getDouble();
            Clear += objGRN.getAttribute("COLUMN_29_AMT").getDouble();
            
            Others += objGRN.getAttribute("COLUMN_31_AMT").getDouble();
            Add = objGRN.getAttribute("COLUMN_33_AMT").getDouble();
            FShipping = objGRN.getAttribute("COLUMN_34_AMT").getDouble();
            
            DefaultMainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
            
            //(1) PJV 1
            //double Entry1=EITLERPGLOBAL.round((GrossAmount-Discount)+PF+Freight+FCA+Insurance+AirFreight+Others+FShipping,3);
            double Entry1 = EITLERPGLOBAL.round((GrossAmount - Discount) + FCA + FShipping, 3);
            
            //Check whether to Credit Amounts to Party or to Us
            if (objGRN.getAttribute("PF_POST").getInt() == 0) //Packing & Forwarding
            {
                Entry1 += PF;
            }
            
            if (objGRN.getAttribute("FREIGHT_POST").getInt() == 0) //Freight
            {
                Entry1 += Freight;
            }
            
            if (objGRN.getAttribute("INSURANCE_POST").getInt() == 0) //Insurance
            {
                Entry1 += Insurance;
            }
            
            if (objGRN.getAttribute("AIR_FREIGHT_POST").getInt() == 0) //Air Freight
            {
                Entry1 += AirFreight;
            }
            
            if (objGRN.getAttribute("OTHERS_POST").getInt() == 0) //Other Expenses
            {
                Entry1 += Others;
            }
            
            double Entry2 = EITLERPGLOBAL.round(Excise + Cess + SpExcise, 2);
            double Entry3 = EITLERPGLOBAL.round(ST, 2);
            double Entry4 = 0;
            
            if (objGRN.getAttribute("OCTROI_POST").getInt() == 0) {
                Entry4 = EITLERPGLOBAL.round(Octroi, 2);
            }
            
            //=======Normal PJV Entry #1 ==========//
            VoucherSrNo = 0;
            
            objVoucher = new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
            
            if (EITLERPGLOBAL.gCompanyID == 2) {
                objVoucher.setAttribute("BOOK_CODE", "40");
            } else {
                objVoucher.setAttribute("BOOK_CODE", "61");
            }
            
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", PONo);
            objVoucher.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucher.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucher.setAttribute("GRN_NO", GRNNo);
            objVoucher.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucher.setAttribute("REMARKS", "");
            
            objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
            objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
            
            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            
            //Get Debit Entries
            Entry1 = 0;
            Entry2 = 0;
            Entry3 = 0;
            Entry4 = 0;
            HashMap debitEntries = getDebitPJVEntriesNonGST(GRNNo, 1, PONo, PODate, InvoiceNo, InvoiceDate, InvoiceAmount, GRNDate);
            
            for (int e = 1; e <= debitEntries.size(); e++) {
                VoucherSrNo++;
                clsVoucherItem objItem = (clsVoucherItem) debitEntries.get(Integer.toString(e));
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 1) {
                    Entry1 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 2) {
                    Entry2 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 3) {
                    Entry3 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 4) {
                    Entry4 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                //Entry1+=objItem.getAttribute("AMOUNT").getDouble();
                DefaultMainCode = objItem.getAttribute("MAIN_ACCOUNT_CODE").getString();
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objItem);
            }
            
            //Round the Entry 1
            Entry1 = EITLERPGLOBAL.round(Entry1, 2);
            
            VoucherSrNo++;
            objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "C");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "125019");
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", Entry1 + Entry2 + Entry3 + Entry4);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            
            objVoucher.DoNotValidateAccounts = true;
            
            ToPayAmount = 0;
            
            if (Entry1 > 0 || Entry2 > 0 || Entry3 > 0 || Entry4 > 0) {
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    System.out.println(objVoucher.LastError);
                    VoucherPosted = false;
                }
            }
            
            String VoucherNo = objVoucher.getAttribute("VOUCHER_NO").getString();
            //=====================================//
            
            //(2) PJV 2
            double Entry5 = EITLERPGLOBAL.round(CustomDuty - LessDuty + ImportEduCess + CVD + Surcharge + CustomEduCess + SpAddDuty + Add, 3);
            
            if (Entry5 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Customs Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", CustomsPer + "% Customs Duty");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            //(3) PJV 3
            double Entry6 = EITLERPGLOBAL.round(Clear, 3);
            
            if (Entry6 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00"); // ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Clearance Agent Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Clearance Charges");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            return VoucherPosted;
        } catch (Exception e) {
            
            e.printStackTrace();
            return false;
        }
        
    }
    
    public HashMap getDebitPJVEntriesNonGST(String GRNNo, int GRNType, String PONo, String PODate, String InvoiceNo, String InvoiceDate, double InvoiceAmount, String GRNDate) {
        
        HashMap DebitEntries = new HashMap();
        
        try {
            
            ResultSet rsGRN, rsTmp, rsHeader;
            int VoucherSrNo = 0;
            
            //Get Unique Items from GRN based on Itemwise Account Heads
            String strSQL = "";
            
            strSQL = "SELECT DISTINCT(ITEM_CODE) ";
            strSQL += "FROM ";
            strSQL += "D_FIN_ITEM_CODE_MAPPING A, ";
            strSQL += EITLERPGLOBAL.DBName + ".D_INV_GRN_DETAIL B ";
            strSQL += "WHERE ";
            strSQL += "B.GRN_NO='" + GRNNo + "' AND B.GRN_TYPE=" + GRNType + " AND ";
            strSQL += "SUBSTRING(B.ITEM_ID,1,LENGTH(A.ITEM_CODE))=A.ITEM_CODE AND A.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ";
            
            ResultSet rsItems = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsItems.first();
            
            //Get GRN Header
            rsGRN = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            
            double TotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            double FreightAmount = 0;
            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 1) {//Freight
                FreightAmount = data.getDoubleValueFromDB("SELECT COLUMN_6_AMT FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                //    FreightAmount += data.getDoubleValueFromDB("SELECT SUM(COLUMN_6_AMT) FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND GRN_TYPE="+GRNType);
                FreightAmount += data.getDoubleValueFromDB("SELECT SUM(COLUMN_15_AMT) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            }
            TotalNetAmount = TotalNetAmount - FreightAmount;
            double ItemTotalNetAmount = 0;
            double ItemWeightage = 0;
            double HeaderAmount = getHeaderAmount(GRNNo, GRNType);
            double ItemGrossAmount = 0;
            double AccountedAmount = 0;
            
            double hExcise = 0, dExcise = 0, totalExcise = 0;
            double hCess = 0, dCess = 0, totalCess = 0;
            double hCenvat = 0, dCenvat = 0, totalCenvat;
            double hST = 0, dST = 0, totalST = 0;
            double hOctroi = 0, dOctroi = 0, totalOctroi = 0;
            double hSpExcise = 0, dSpExcise = 0, totalSpExcise = 0;
            
            //Derive header level Tax Amounts
            rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            rsHeader.first();
            if (rsHeader.getRow() > 0) {
                hExcise = UtilFunctions.getDouble(rsHeader, "COLUMN_3_AMT", 0);
                hCess = UtilFunctions.getDouble(rsHeader, "COLUMN_4_AMT", 0);
                hCenvat = UtilFunctions.getDouble(rsHeader, "COLUMN_8_AMT", 0);
                hST = UtilFunctions.getDouble(rsHeader, "COLUMN_5_AMT", 0);
                hOctroi = UtilFunctions.getDouble(rsHeader, "COLUMN_7_AMT", 0);
                hSpExcise = UtilFunctions.getDouble(rsHeader, "COLUMN_17_AMT", 0);
            }
            
            //Derive line level tax Amounts
            rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_9_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_6_AMT) AS FREIGHT,SUM(COLUMN_18_AMT) AS INSURANCE,SUM(COLUMN_20_AMT) AS AIR_FREIGHT,SUM(COLUMN_21_AMT) AS OTHERS,SUM(COLUMN_27_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_28_AMT) AS RND_DISCOUNT,SUM(COLUMN_3_AMT) AS EXCISE,SUM(COLUMN_4_AMT) AS CESS,SUM(COLUMN_8_AMT) AS CENVAT,SUM(COLUMN_5_AMT) AS ST,SUM(COLUMN_7_AMT) AS OCTROI,SUM(COLUMN_17_AMT) AS SP_EXCISE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
            rsTmp.first();
            
            if (rsTmp.getRow() > 0) {
                dExcise = UtilFunctions.getDouble(rsTmp, "EXCISE", 0);
                dCess = UtilFunctions.getDouble(rsTmp, "CESS", 0);
                dCenvat = UtilFunctions.getDouble(rsTmp, "CENVAT", 0);
                dST = UtilFunctions.getDouble(rsTmp, "ST", 0);
                dOctroi = UtilFunctions.getDouble(rsTmp, "OCTROI", 0);
                dSpExcise = UtilFunctions.getDouble(rsTmp, "SP_EXCISE", 0);
                
            }
            
            totalExcise = hExcise + dExcise;
            totalCess = hCess + dCess;
            totalCenvat = hCenvat + dCenvat;
            totalST = hST + dST;
            totalOctroi = hOctroi + dOctroi;
            totalSpExcise = hSpExcise + dSpExcise;
            
            if (TotalNetAmount > 0) {
                if (rsItems.getRow() > 0) {
                    while (!rsItems.isAfterLast()) {
                        //Get the Group
                        String ItemGroup = rsItems.getString("ITEM_CODE");
                        String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, ItemGroup);
                        
                        //Calculate Weightage (Pro-rata basis) of the items having this group
                        ItemTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                        ItemWeightage = (ItemTotalNetAmount * 100) / TotalNetAmount;
                        
                        //Calculate Item level distribution of Total Header Amount
                        ItemGrossAmount = (HeaderAmount * ItemWeightage) / 100;
                        
                        rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_18_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_15_AMT) AS FREIGHT,SUM(COLUMN_28_AMT) AS INSURANCE,SUM(COLUMN_30_AMT) AS AIR_FREIGHT,SUM(COLUMN_31_AMT) AS OTHERS,SUM(COLUMN_37_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_38_AMT) AS RND_DISCOUNT,SUM(COLUMN_12_AMT) AS EXCISE,SUM(COLUMN_13_AMT) AS CESS,SUM(COLUMN_17_AMT) AS CENVAT,SUM(COLUMN_14_AMT) AS ST,SUM(COLUMN_16_AMT) AS OCTROI,SUM(COLUMN_26_AMT) AS SP_EXCISE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                        rsTmp.first();
                        if (rsTmp.getRow() > 0) {
                            
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "TRADE_DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "RND_DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "FCA", 0);
                            ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "GROSS_AMOUNT", 0);
                            
                            if (UtilFunctions.getInt(rsGRN, "PF_POST", 0) == 0) //Packing & Forwarding
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "PF", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 0) //Freight
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "FREIGHT", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "INSURANCE_POST", 0) == 0) //Insurance
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "INSURANCE", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "AIR_FREIGHT_POST", 0) == 0) //Air Freight
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "AIR_FREIGHT", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "OTHERS_POST", 0) == 0) //Other Expenses
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "OTHERS", 0);
                            }
                            
                            double Entry2 = ((hExcise * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "EXCISE", 0)) + ((hCess * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "CESS", 0)) + ((hSpExcise * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "SP_EXCISE", 0));
                            double Entry3 = ((hST * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "ST", 0));
                            double Entry4 = ((hOctroi * ItemWeightage) / 100) + (UtilFunctions.getDouble(rsTmp, "OCTROI", 0));
                            
                            if (Entry2 < 0) {
                                ItemGrossAmount = ItemGrossAmount - Math.abs(Entry2);
                                Entry2 = 0;
                            }
                            
                            totalExcise = totalExcise - (((hExcise * ItemWeightage) / 100) + UtilFunctions.getDouble(rsTmp, "EXCISE", 0));
                            totalST = totalST - (((hST * ItemWeightage) / 100) + UtilFunctions.getDouble(rsTmp, "ST", 0));
                            totalOctroi = totalOctroi - (((hOctroi * ItemWeightage) / 100) + UtilFunctions.getDouble(rsTmp, "OCTROI", 0));
                            
                            //Do the Entry
                            AccountedAmount += ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "ST", 0);
                            
                            clsVoucherItem objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(ItemGrossAmount, 2));
                            objVoucherItem.setAttribute("REMARKS", "");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            
                            //**** Include Sales Tax, Excise and Octroi Amounts bifurcations *******//
                            if (Entry2 > 0) {
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry2, 2));
                                objVoucherItem.setAttribute("REMARKS", "Excise");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (Entry3 > 0) {
                                double STPercentage = data.getDoubleValueFromDB("SELECT COLUMN_14_PER FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                                String ExCode = data.getStringValueFromDB("SELECT EX_ACCOUNT_CODE FROM D_FIN_EX_ACCOUNT_MASTER WHERE PERCENTAGE=" + STPercentage, FinanceGlobal.FinURL);
                                String STAcCode = MainCode;
                                
                                if (!ExCode.trim().equals("")) {
                                    STAcCode = STAcCode + "." + ExCode;
                                }
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", STAcCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry3, 2));
                                objVoucherItem.setAttribute("REMARKS", "ST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 3);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (rsHeader.getInt("OCTROI_POST") == 0) {
                                if (Entry4 > 0) {
                                    if (Entry2 > 0) {
                                        objVoucherItem = new clsVoucherItem();
                                        VoucherSrNo++;
                                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                        objVoucherItem.setAttribute("EFFECT", "D");
                                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry4, 2));
                                        objVoucherItem.setAttribute("REMARKS", "Octroi");
                                        objVoucherItem.setAttribute("PO_NO", PONo);
                                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                        objVoucherItem.setAttribute("ENTRY_TYPE", 4);
                                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                    }
                                }
                            }
                            //**********************************************************************//
                        }
                        rsItems.next();
                    }
                    
                    //Add Default
                    double Diff = EITLERPGLOBAL.round(TotalNetAmount - AccountedAmount, 2);
                    
                    if (EITLERPGLOBAL.round(Diff, 2) > 0) {
                        
                        String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                        
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Diff, 2));
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                        objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        
                        //************************** Include Difference Tax Amounts ***************************//
                        double Entry2 = totalExcise + totalCess + totalSpExcise;
                        double Entry3 = totalST;
                        double Entry4 = totalOctroi;
                        
                        if (Entry2 > 0) {
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry2, 2));
                            objVoucherItem.setAttribute("REMARKS", "Excise");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (Entry3 > 0) {
                            double STPercentage = data.getDoubleValueFromDB("SELECT COLUMN_5_PER FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                            String ExCode = data.getStringValueFromDB("SELECT EX_ACCOUNT_CODE FROM D_FIN_EX_ACCOUNT_MASTER WHERE PERCENTAGE=" + STPercentage, FinanceGlobal.FinURL);
                            String STAcCode = MainCode;
                            
                            if (!ExCode.trim().equals("")) {
                                STAcCode = STAcCode + "." + ExCode;
                            }
                            
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", STAcCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry3, 2));
                            objVoucherItem.setAttribute("REMARKS", "ST");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 3);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (rsHeader.getInt("OCTROI_POST") == 0) {
                            if (Entry4 > 0) {
                                if (Entry2 > 0) {
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry4, 2));
                                    objVoucherItem.setAttribute("REMARKS", "Octroi");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 4);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                            }
                        }
                        
                        //*************************************************************************************//
                    }
                    
                } else {
                    //Default Account Header
                    
                    //Get the Group
                    String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                    
                    //Calculate Weightage (Pro-rata basis) of the items having this group
                    ItemTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                    ItemWeightage = (ItemTotalNetAmount * 100) / TotalNetAmount;
                    
                    //Calculate Item level distribution of Total Header Amount
                    ItemGrossAmount = (HeaderAmount * ItemWeightage) / 100;
                    
                    double Excise = 0;
                    double Cess = 0;
                    double Cenvat = 0;
                    double ST = 0;
                    double Octroi = 0;
                    double SpExcise = 0;
                    
                    rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                    rsHeader.first();
                    if (rsHeader.getRow() > 0) {
                        Excise = (UtilFunctions.getDouble(rsHeader, "COLUMN_3_AMT", 0) * ItemWeightage) / 100;
                        Cess = (UtilFunctions.getDouble(rsHeader, "COLUMN_4_AMT", 0) * ItemWeightage) / 100;
                        Cenvat = (UtilFunctions.getDouble(rsHeader, "COLUMN_8_AMT", 0) * ItemWeightage) / 100;
                        ST = (UtilFunctions.getDouble(rsHeader, "COLUMN_5_AMT", 0) * ItemWeightage) / 100;
                        Octroi = (UtilFunctions.getDouble(rsHeader, "COLUMN_7_AMT", 0) * ItemWeightage) / 100;
                        SpExcise = (UtilFunctions.getDouble(rsHeader, "COLUMN_17_AMT", 0) * ItemWeightage) / 100;
                    }
                    
                    rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_18_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_15_AMT) AS FREIGHT,SUM(COLUMN_28_AMT) AS INSURANCE,SUM(COLUMN_30_AMT) AS AIR_FREIGHT,SUM(COLUMN_31_AMT) AS OTHERS,SUM(COLUMN_37_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_38_AMT) AS RND_DISCOUNT,SUM(COLUMN_12_AMT) AS EXCISE,SUM(COLUMN_13_AMT) AS CESS,SUM(COLUMN_17_AMT) AS CENVAT,SUM(COLUMN_14_AMT) AS ST,SUM(COLUMN_16_AMT) AS OCTROI,SUM(COLUMN_26_AMT) AS SP_EXCISE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                    rsTmp.first();
                    if (rsTmp.getRow() > 0) {
                        
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "TRADE_DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "RND_DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "FCA", 0);
                        ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "GROSS_AMOUNT", 0);
                        
                        Excise += UtilFunctions.getDouble(rsTmp, "EXCISE", 0);
                        Cess += UtilFunctions.getDouble(rsTmp, "CESS", 0);
                        Cenvat += UtilFunctions.getDouble(rsTmp, "CENVAT", 0);
                        ST += UtilFunctions.getDouble(rsTmp, "ST", 0);
                        Octroi += UtilFunctions.getDouble(rsTmp, "OCTROI", 0);
                        SpExcise += UtilFunctions.getDouble(rsTmp, "SP_EXCISE", 0);
                        
                        if (UtilFunctions.getInt(rsGRN, "PF_POST", 0) == 0) //Packing & Forwarding
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "PF", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 0) //Freight
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "FREIGHT", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "INSURANCE_POST", 0) == 0) //Insurance
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "INSURANCE", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "AIR_FREIGHT_POST", 0) == 0) //Air Freight
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "AIR_FREIGHT", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "OTHERS_POST", 0) == 0) //Other Expenses
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "OTHERS", 0);
                        }
                        
                        //Do the Entry
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(ItemGrossAmount, 2));
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        
                        //**** Include Sales Tax, Excise and Octroi Amounts bifurcations *******//
                        double Entry2 = Excise + Cess + SpExcise;
                        double Entry3 = ST;
                        double Entry4 = Octroi;
                        
                        if (Entry2 > 0) {
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry2, 2));
                            objVoucherItem.setAttribute("REMARKS", "Excise");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (Entry3 > 0) {
                            double STPercentage = data.getDoubleValueFromDB("SELECT COLUMN_14_PER FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                            String ExCode = data.getStringValueFromDB("SELECT EX_ACCOUNT_CODE FROM D_FIN_EX_ACCOUNT_MASTER WHERE PERCENTAGE=" + STPercentage, FinanceGlobal.FinURL);
                            String STAcCode = MainCode;
                            
                            if (!ExCode.trim().equals("")) {
                                STAcCode = STAcCode + "." + ExCode;
                            }
                            
                            objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", STAcCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry3, 2));
                            objVoucherItem.setAttribute("REMARKS", "ST");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 3);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        }
                        
                        if (rsHeader.getInt("OCTROI_POST") == 0) {
                            if (Entry4 > 0) {
                                if (Entry2 > 0) {
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(Entry4, 2));
                                    objVoucherItem.setAttribute("REMARKS", "Octroi");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 4);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                            }
                        }
                        //**********************************************************************//
                        
                    }
                    
                }
                
            }
        } catch (Exception e) {
            
        }
        
        return DebitEntries;
    }
    
    public boolean PostVoucherGST(String DocNo, int DocType, String ExVoucherNo) {
        
        try {
            
            boolean VoucherPosted = true;
            
            //Load GRN
            clsGRNGen objGRN = (clsGRNGen) getObject(EITLERPGLOBAL.gCompanyID, DocNo, DocType);
            
            //*********** Select Voucher Hierarchy based on Rules *********//
            //Note: This routine overrides hierarchy selected by user in GRN
            //(1) Based on PO Type
            int POType = 0;
            
            String PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + DocNo + "' AND GRN_TYPE=" + DocType + " AND PO_NO<>''");
            
            if (!PONo.trim().equals("")) {
                POType = clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID, PONo);
                
                HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PO_TYPE", Integer.toString(POType));
                
                if (List.size() > 0) {
                    //Get the Result of the Rule which would be the hierarchy no.
                    clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                    int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                    setAttribute("FIN_HIERARCHY_ID", HierarchyID);
                }
            }
            
            //(2) Based on Payment Type
            int PaymentType = objGRN.getAttribute("PAYMENT_TYPE").getInt();
            
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }
            
            //(3) Based on Post Voucher Condition
            String SuppID = objGRN.getAttribute("SUPP_ID").getString();
            
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "SUPP_ID", SuppID);
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    return true; //Do not post voucher
                }
            }
            
            //(4) Based on Payment Type, decide whether to post voucher
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "PAYMENT_TYPE", Integer.toString(PaymentType));
            
            if (List.size() > 0) {
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    return true; //Do not post voucher
                }
            }
            
            //******************* Hierarchy Selected **********************//
            clsVoucher objVoucher;
            clsVoucherItem objVoucherItem;
            
            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;
            
            String GRNNo = objGRN.getAttribute("GRN_NO").getString();
            String GRNDate = objGRN.getAttribute("GRN_DATE").getString();
            
            PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND PO_NO<>'' ");
            String PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "'");
            String InvoiceNo = objGRN.getAttribute("INVOICE_NO").getString();
            String InvoiceDate = objGRN.getAttribute("INVOICE_DATE").getString();
            String PartyCode = objGRN.getAttribute("SUPP_ID").getString();
            double InvoiceAmount = 0;
            
            //Get MIR No.
            String MIRNo = data.getStringValueFromDB("SELECT MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=1");
            
            if (!MIRNo.trim().equals("")) {
                InvoiceAmount = data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_INV_MIR_HEADER WHERE MIR_NO='" + MIRNo + "' AND MIR_TYPE=1");
            }
            
            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.PJVModuleID);
            rsVoucher.first();
            
            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }
            
            //========================== Amount Bifurcations =========================//
            double Discount = 0, PF = 0, Freight = 0, FCA = 0, Insurance = 0, AirFreight = 0, Others = 0, Add = 0, FShipping = 0;
            //   double Excise=0,Cess=0,Cenvat=0,ST=0,Octroi=0,GrossAmount=0;
            double CustomDuty = 0, LessDuty = 0, ImportEduCess = 0, CVD = 0, Surcharge = 0, CustomEduCess = 0, SpAddDuty = 0, Clear = 0, GrossAmount = 0;
            String CustomsPer = "";
            double ToPayAmount = 0, VoucherSrNo = 0;
            //  String ExcisePer="",STPer="",OctroiPer=""SpExcise=0,;
            String DefaultMainCode = "";
            
            for (int i = 1; i <= objGRN.colGRNItems.size(); i++) {
                clsGRNGenItem objItem = (clsGRNGenItem) objGRN.colGRNItems.get(Integer.toString(i));
                
                Discount += objItem.getAttribute("COLUMN_1_AMT").getDouble();
                Discount += objItem.getAttribute("COLUMN_1_AMT").getDouble();
                
                //Add Other Discounts
                //  Discount+=objItem.getAttribute("COLUMN_27_AMT").getDouble(); //Trade Discount
                // Discount+=objItem.getAttribute("COLUMN_28_AMT").getDouble(); //R&D Deduction
                //Add Other Discounts
                Discount += objItem.getAttribute("COLUMN_37_AMT").getDouble(); //Trade Discount
                Discount += objItem.getAttribute("COLUMN_38_AMT").getDouble(); //R&D Deduction
                
                PF += objItem.getAttribute("COLUMN_2_AMT").getDouble();
                //old rishi  Freight+=objItem.getAttribute("COLUMN_6_AMT").getDouble();
                Freight += objItem.getAttribute("COLUMN_15_AMT").getDouble();
                
                //FCA+=objItem.getAttribute("COLUMN_9_AMT").getDouble();
                FCA += objItem.getAttribute("COLUMN_18_AMT").getDouble();
                // Insurance+=objItem.getAttribute("COLUMN_18_AMT").getDouble();
                Insurance += objItem.getAttribute("COLUMN_28_AMT").getDouble();
                
                // AirFreight+=objItem.getAttribute("COLUMN_20_AMT").getDouble();
                AirFreight += objItem.getAttribute("COLUMN_30_AMT").getDouble();
                
                //Excise+=objItem.getAttribute("COLUMN_3_AMT").getDouble();
                //  Excise+=objItem.getAttribute("COLUMN_12_AMT").getDouble();
                //   if(objItem.getAttribute("COLUMN_12_PER").getDouble()>0) {
                //       ExcisePer=Double.toString(objItem.getAttribute("COLUMN_12_PER").getDouble());
                //   }
                //   Cess+=objItem.getAttribute("COLUMN_13_AMT").getDouble();
                //  Cenvat+=objItem.getAttribute("COLUMN_17_AMT").getDouble();
                //   ST+=objItem.getAttribute("COLUMN_14_AMT").getDouble();
                //  if(objItem.getAttribute("COLUMN_14_PER").getDouble()>0) {
                //     STPer=Double.toString(objItem.getAttribute("COLUMN_14_PER").getDouble());
                //   }
                //   Octroi+=objItem.getAttribute("COLUMN_16_AMT").getDouble();
                //   if(objItem.getAttribute("COLUMN_16_PER").getDouble()>0) {
                //       OctroiPer=Double.toString(objItem.getAttribute("COLUMN_16_PER").getDouble());
                //   }
                CustomDuty += objItem.getAttribute("COLUMN_20_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_20_PER").getDouble() > 0) {
                    CustomsPer = Double.toString(objItem.getAttribute("COLUMN_20_PER").getDouble());
                }
                
                Others += objItem.getAttribute("COLUMN_31_AMT").getDouble();
                LessDuty += objItem.getAttribute("COLUMN_32_AMT").getDouble();
                ImportEduCess += objItem.getAttribute("COLUMN_21_AMT").getDouble();
                CVD += objItem.getAttribute("COLUMN_22_AMT").getDouble();
                
                Surcharge += objItem.getAttribute("COLUMN_23_AMT").getDouble();
                
                CustomEduCess += objItem.getAttribute("COLUMN_24_AMT").getDouble();
                
                SpAddDuty += objItem.getAttribute("COLUMN_25_AMT").getDouble();
                //  SpExcise+=objItem.getAttribute("COLUMN_26_AMT").getDouble();
                Clear += objItem.getAttribute("COLUMN_29_AMT").getDouble();
                
                GrossAmount += objItem.getAttribute("TOTAL_AMOUNT").getDouble();
            }
            //========================================================================//
            
            Discount += objGRN.getAttribute("COLUMN_1_AMT").getDouble();
            PF += objGRN.getAttribute("COLUMN_2_AMT").getDouble();
            Freight += objGRN.getAttribute("COLUMN_15_AMT").getDouble();
            FCA += objGRN.getAttribute("COLUMN_18_AMT").getDouble();
            Insurance += objGRN.getAttribute("COLUMN_28_AMT").getDouble();
            AirFreight += objGRN.getAttribute("COLUMN_30_AMT").getDouble();
            
            //   Excise+=objGRN.getAttribute("COLUMN_12_AMT").getDouble();
            //  Cess+=objGRN.getAttribute("COLUMN_13_AMT").getDouble();
            //    Cenvat+=objGRN.getAttribute("COLUMN_17_AMT").getDouble();
            //  ST+=objGRN.getAttribute("COLUMN_14_AMT").getDouble();
            //  Octroi+=objGRN.getAttribute("COLUMN_16_AMT").getDouble();
            CustomDuty += objGRN.getAttribute("COLUMN_20_AMT").getDouble();
            LessDuty += objGRN.getAttribute("COLUMN_32_AMT").getDouble();
            ImportEduCess += objGRN.getAttribute("COLUMN_21_AMT").getDouble();
            CVD += objGRN.getAttribute("COLUMN_22_AMT").getDouble();
            Surcharge += objGRN.getAttribute("COLUMN_23_AMT").getDouble();
            CustomEduCess += objGRN.getAttribute("COLUMN_24_AMT").getDouble();
            SpAddDuty += objGRN.getAttribute("COLUMN_25_AMT").getDouble();
            //    SpExcise+=objGRN.getAttribute("COLUMN_26_AMT").getDouble();
            Clear += objGRN.getAttribute("COLUMN_29_AMT").getDouble();
            
            Others += objGRN.getAttribute("COLUMN_31_AMT").getDouble();
            Add = objGRN.getAttribute("COLUMN_33_AMT").getDouble();
            FShipping = objGRN.getAttribute("COLUMN_34_AMT").getDouble();
            
            DefaultMainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
            
            //(1) PJV 1
            //double Entry1=EITLERPGLOBAL.round((GrossAmount-Discount)+PF+Freight+FCA+Insurance+AirFreight+Others+FShipping,3);
            double Entry1 = EITLERPGLOBAL.round((GrossAmount - Discount) + FCA + FShipping, 3);
            
            //Check whether to Credit Amounts to Party or to Us
            if (objGRN.getAttribute("PF_POST").getInt() == 0) //Packing & Forwarding
            {
                Entry1 += PF;
            }
            
            if (objGRN.getAttribute("FREIGHT_POST").getInt() == 0) //Freight
            {
                Entry1 += Freight;
            }
            
            if (objGRN.getAttribute("INSURANCE_POST").getInt() == 0) //Insurance
            {
                Entry1 += Insurance;
            }
            
            if (objGRN.getAttribute("AIR_FREIGHT_POST").getInt() == 0) //Air Freight
            {
                Entry1 += AirFreight;
            }
            
            if (objGRN.getAttribute("OTHERS_POST").getInt() == 0) //Other Expenses
            {
                Entry1 += Others;
            }
            
            double Entry2a = 0;
            //   double Entry3=EITLERPGLOBAL.round(ST,2);
            //   double Entry4=0;
            
            //  if(objGRN.getAttribute("OCTROI_POST").getInt()==0) {
            //     Entry4=EITLERPGLOBAL.round(Octroi,2);
            // }
            //=======Normal PJV Entry #1 ==========//
            VoucherSrNo = 0;
            
            objVoucher = new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
            
            if (EITLERPGLOBAL.gCompanyID == 2) {
                objVoucher.setAttribute("BOOK_CODE", "40");
            } else {
                objVoucher.setAttribute("BOOK_CODE", "61");
            }
            
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", PONo);
            objVoucher.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucher.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucher.setAttribute("GRN_NO", GRNNo);
            objVoucher.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucher.setAttribute("REMARKS", "");
            
            objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
            objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
            
            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            
            //Get Debit Entries
            Entry1 = 0;
            Entry2a = 0;
            // Entry3=0;
            //  Entry4=0;
            HashMap debitEntries = getDebitPJVEntriesGST(GRNNo, 1, PONo, PODate, InvoiceNo, InvoiceDate, InvoiceAmount, GRNDate);
            
            for (int e = 1; e <= debitEntries.size(); e++) {
                VoucherSrNo++;
                clsVoucherItem objItem = (clsVoucherItem) debitEntries.get(Integer.toString(e));
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 1) {
                    Entry1 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 2) {
                    Entry2a += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                //Entry1+=objItem.getAttribute("AMOUNT").getDouble();
                DefaultMainCode = objItem.getAttribute("MAIN_ACCOUNT_CODE").getString();
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objItem);
            }
            
            //Round the Entry 1
            Entry1 = EITLERPGLOBAL.round(Entry1, 2);
            
            VoucherSrNo++;
            objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "C");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "125019");
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", Entry1 + Entry2a);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            
            objVoucher.DoNotValidateAccounts = true;
            
            ToPayAmount = 0;
            
            if (Entry1 > 0 || Entry2a > 0) {
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    System.out.println(objVoucher.LastError);
                    VoucherPosted = false;
                }
            }
            
            String VoucherNo = objVoucher.getAttribute("VOUCHER_NO").getString();
            //=====================================//
            
            //(2) PJV 2
            double Entry5 = EITLERPGLOBAL.round(CustomDuty - LessDuty + ImportEduCess + CVD + Surcharge + CustomEduCess + SpAddDuty + Add, 3);
            
            if (Entry5 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Customs Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", CustomsPer + "% Customs Duty");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            //(3) PJV 3
            double Entry6 = EITLERPGLOBAL.round(Clear, 3);
            
            if (Entry6 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00"); // ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Clearance Agent Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Clearance Charges");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            return VoucherPosted;
        } catch (Exception e) {
            
            e.printStackTrace();
            return false;
        }
        
    }
    
    public boolean PostVoucherHSNWiseGST(String DocNo, int DocType, String ExVoucherNo) {
        
        try {
            
            boolean VoucherPosted = true;
            
            //Load GRN
            clsGRNGen objGRN = (clsGRNGen) getObject(EITLERPGLOBAL.gCompanyID, DocNo, DocType);
            
            //*********** Select Voucher Hierarchy based on Rules *********//
            //Note: This routine overrides hierarchy selected by user in GRN
            //(1) Based on PO Type
            int POType = 0;
            System.out.println("---1---");
            String PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + DocNo + "' AND GRN_TYPE=" + DocType + " AND PO_NO<>''");
            System.out.println("---2---");
            if (!PONo.trim().equals("")) {
                POType = clsPOGen.getPOType(EITLERPGLOBAL.gCompanyID, PONo);
                System.out.println("---3---");
                HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PO_TYPE", Integer.toString(POType));
                System.out.println("---4---");
                if (List.size() > 0) {
                    System.out.println("---5---");
                    //Get the Result of the Rule which would be the hierarchy no.
                    clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                    int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                    setAttribute("FIN_HIERARCHY_ID", HierarchyID);
                }
            }
            
            //(2) Based on Payment Type
            int PaymentType = objGRN.getAttribute("PAYMENT_TYPE").getInt();
            System.out.println("---6---");
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "CHOOSE_HIERARCHY", "PAYMENT_TYPE", Integer.toString(PaymentType));
            System.out.println("---7---");
            if (List.size() > 0) {
                System.out.println("---8---");
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }
            System.out.println("---9---");
            //(3) Based on Post Voucher Condition
            String SuppID = objGRN.getAttribute("SUPP_ID").getString();
            
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "SUPP_ID", SuppID);
            System.out.println("---10---");
            if (List.size() > 0) {
                System.out.println("---11---");
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    chkPostVoucher = PostVoucher;
                    
                    return true; //Do not post voucher
                }
            }
            System.out.println("---12---");
            //(4) Based on Payment Type, decide whether to post voucher
            List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsGRNGen.ModuleID, "POST_VOUCHER", "PAYMENT_TYPE", Integer.toString(PaymentType));
            System.out.println("---13---");
            if (List.size() > 0) {
                System.out.println("---14---");
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int PostVoucher = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                
                if (PostVoucher == 0) {
                    chkPostVoucher = PostVoucher;
                    
                    return true; //Do not post voucher
                }
            }
            System.out.println("---15---");
            //******************* Hierarchy Selected **********************//
            clsVoucher objVoucher;
            clsVoucherItem objVoucherItem;
            
            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;
            
            String GRNNo = objGRN.getAttribute("GRN_NO").getString();
            String GRNDate = objGRN.getAttribute("GRN_DATE").getString();
            
            PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND PO_NO<>'' ");
            String PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "'");
            String InvoiceNo = objGRN.getAttribute("INVOICE_NO").getString();
            String InvoiceDate = objGRN.getAttribute("INVOICE_DATE").getString();
            String PartyCode = objGRN.getAttribute("SUPP_ID").getString();
            double InvoiceAmount = 0;
            
            //Get MIR No.
            String MIRNo = data.getStringValueFromDB("SELECT MIR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=1");
            
            if (!MIRNo.trim().equals("")) {
                InvoiceAmount = data.getDoubleValueFromDB("SELECT INVOICE_AMOUNT FROM D_INV_MIR_HEADER WHERE MIR_NO='" + MIRNo + "' AND MIR_TYPE=1");
            }
            
            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.PJVModuleID);
            rsVoucher.first();
            
            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }
            
            //========================== Amount Bifurcations =========================//
            double Discount = 0, PF = 0, Freight = 0, FCA = 0, Insurance = 0, AirFreight = 0, Others = 0, Add = 0, FShipping = 0;
            //   double Excise=0,Cess=0,Cenvat=0,ST=0,Octroi=0,GrossAmount=0;
            double CustomDuty = 0, LessDuty = 0, ImportEduCess = 0, CVD = 0, Surcharge = 0, CustomEduCess = 0, SpAddDuty = 0, Clear = 0, GrossAmount = 0;
            String CustomsPer = "";
            double ToPayAmount = 0, VoucherSrNo = 0;
            //  String ExcisePer="",STPer="",OctroiPer=""SpExcise=0,;
            String DefaultMainCode = "";
            System.out.println("---16---");
            for (int i = 1; i <= objGRN.colGRNItems.size(); i++) {
                clsGRNGenItem objItem = (clsGRNGenItem) objGRN.colGRNItems.get(Integer.toString(i));
                
                Discount += objItem.getAttribute("COLUMN_1_AMT").getDouble();
                //Discount += objItem.getAttribute("COLUMN_1_AMT").getDouble();
                
                //Add Other Discounts
                //  Discount+=objItem.getAttribute("COLUMN_27_AMT").getDouble(); //Trade Discount
                // Discount+=objItem.getAttribute("COLUMN_28_AMT").getDouble(); //R&D Deduction
                //Add Other Discounts
                Discount += objItem.getAttribute("COLUMN_37_AMT").getDouble(); //Trade Discount
                Discount += objItem.getAttribute("COLUMN_38_AMT").getDouble(); //R&D Deduction
                
                PF += objItem.getAttribute("COLUMN_2_AMT").getDouble();
                //old rishi  Freight+=objItem.getAttribute("COLUMN_6_AMT").getDouble();
                Freight += objItem.getAttribute("COLUMN_15_AMT").getDouble();
                
                //FCA+=objItem.getAttribute("COLUMN_9_AMT").getDouble();
                FCA += objItem.getAttribute("COLUMN_18_AMT").getDouble();
                // Insurance+=objItem.getAttribute("COLUMN_18_AMT").getDouble();
                Insurance += objItem.getAttribute("COLUMN_28_AMT").getDouble();
                
                // AirFreight+=objItem.getAttribute("COLUMN_20_AMT").getDouble();
                AirFreight += objItem.getAttribute("COLUMN_30_AMT").getDouble();
                
                //Excise+=objItem.getAttribute("COLUMN_3_AMT").getDouble();
                //  Excise+=objItem.getAttribute("COLUMN_12_AMT").getDouble();
                //   if(objItem.getAttribute("COLUMN_12_PER").getDouble()>0) {
                //       ExcisePer=Double.toString(objItem.getAttribute("COLUMN_12_PER").getDouble());
                //   }
                //   Cess+=objItem.getAttribute("COLUMN_13_AMT").getDouble();
                //  Cenvat+=objItem.getAttribute("COLUMN_17_AMT").getDouble();
                //   ST+=objItem.getAttribute("COLUMN_14_AMT").getDouble();
                //  if(objItem.getAttribute("COLUMN_14_PER").getDouble()>0) {
                //     STPer=Double.toString(objItem.getAttribute("COLUMN_14_PER").getDouble());
                //   }
                //   Octroi+=objItem.getAttribute("COLUMN_16_AMT").getDouble();
                //   if(objItem.getAttribute("COLUMN_16_PER").getDouble()>0) {
                //       OctroiPer=Double.toString(objItem.getAttribute("COLUMN_16_PER").getDouble());
                //   }
                CustomDuty += objItem.getAttribute("COLUMN_20_AMT").getDouble();
                
                if (objItem.getAttribute("COLUMN_20_PER").getDouble() > 0) {
                    CustomsPer = Double.toString(objItem.getAttribute("COLUMN_20_PER").getDouble());
                }
                
                Others += objItem.getAttribute("COLUMN_31_AMT").getDouble();
                LessDuty += objItem.getAttribute("COLUMN_32_AMT").getDouble();
                ImportEduCess += objItem.getAttribute("COLUMN_21_AMT").getDouble();
                CVD += objItem.getAttribute("COLUMN_22_AMT").getDouble();
                
                Surcharge += objItem.getAttribute("COLUMN_23_AMT").getDouble();
                
                CustomEduCess += objItem.getAttribute("COLUMN_24_AMT").getDouble();
                
                SpAddDuty += objItem.getAttribute("COLUMN_25_AMT").getDouble();
                //  SpExcise+=objItem.getAttribute("COLUMN_26_AMT").getDouble();
                Clear += objItem.getAttribute("COLUMN_29_AMT").getDouble();
                
                GrossAmount += objItem.getAttribute("TOTAL_AMOUNT").getDouble();
            }
            //========================================================================//
            
            Discount += objGRN.getAttribute("COLUMN_1_AMT").getDouble();
            PF += objGRN.getAttribute("COLUMN_2_AMT").getDouble();
            Freight += objGRN.getAttribute("COLUMN_6_AMT").getDouble();
            FCA += objGRN.getAttribute("COLUMN_9_AMT").getDouble();
            Insurance += objGRN.getAttribute("COLUMN_18_AMT").getDouble();
            AirFreight += objGRN.getAttribute("COLUMN_20_AMT").getDouble();
            
            //            Excise += objGRN.getAttribute("COLUMN_3_AMT").getDouble();
            //            Cess += objGRN.getAttribute("COLUMN_4_AMT").getDouble();
            //            Cenvat += objGRN.getAttribute("COLUMN_8_AMT").getDouble();
            //            ST += objGRN.getAttribute("COLUMN_5_AMT").getDouble();
            //            Octroi += objGRN.getAttribute("COLUMN_7_AMT").getDouble();
            
            CustomDuty += objGRN.getAttribute("COLUMN_11_AMT").getDouble();
            LessDuty += objGRN.getAttribute("COLUMN_22_AMT").getDouble();
            ImportEduCess += objGRN.getAttribute("COLUMN_12_AMT").getDouble();
            CVD += objGRN.getAttribute("COLUMN_13_AMT").getDouble();
            Surcharge += objGRN.getAttribute("COLUMN_14_AMT").getDouble();
            CustomEduCess += objGRN.getAttribute("COLUMN_15_AMT").getDouble();
            SpAddDuty += objGRN.getAttribute("COLUMN_16_AMT").getDouble();
            //            SpExcise += objGRN.getAttribute("COLUMN_17_AMT").getDouble();
            Clear += objGRN.getAttribute("COLUMN_19_AMT").getDouble();
            
            Others += objGRN.getAttribute("COLUMN_21_AMT").getDouble();
            Add = objGRN.getAttribute("COLUMN_23_AMT").getDouble();
            FShipping = objGRN.getAttribute("COLUMN_24_AMT").getDouble();
            
            DefaultMainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
            
            //(1) PJV 1
            //double Entry1=EITLERPGLOBAL.round((GrossAmount-Discount)+PF+Freight+FCA+Insurance+AirFreight+Others+FShipping,3);
            double Entry1 = EITLERPGLOBAL.round((GrossAmount - Discount) + FCA + FShipping, 3);
            
            //Check whether to Credit Amounts to Party or to Us
            if (objGRN.getAttribute("PF_POST").getInt() == 0) //Packing & Forwarding
            {
                Entry1 += PF;
            }
            
            if (objGRN.getAttribute("FREIGHT_POST").getInt() == 0) //Freight
            {
                Entry1 += Freight;
            }
            
            if (objGRN.getAttribute("INSURANCE_POST").getInt() == 0) //Insurance
            {
                Entry1 += Insurance;
            }
            
            if (objGRN.getAttribute("AIR_FREIGHT_POST").getInt() == 0) //Air Freight
            {
                Entry1 += AirFreight;
            }
            
            if (objGRN.getAttribute("OTHERS_POST").getInt() == 0) //Other Expenses
            {
                Entry1 += Others;
            }
            
            double Entry2a = 0;
            //   double Entry3=EITLERPGLOBAL.round(ST,2);
            //   double Entry4=0;
            
            //  if(objGRN.getAttribute("OCTROI_POST").getInt()==0) {
            //     Entry4=EITLERPGLOBAL.round(Octroi,2);
            // }
            //=======Normal PJV Entry #1 ==========//
            VoucherSrNo = 0;
            System.out.println("---17---");
            objVoucher = new clsVoucher();
            objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
            System.out.println("---18---");
            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
            
            if (EITLERPGLOBAL.gCompanyID == 2) {
                objVoucher.setAttribute("BOOK_CODE", "40");
            } else {
                objVoucher.setAttribute("BOOK_CODE", "61");
            }
            
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", PONo);
            objVoucher.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucher.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucher.setAttribute("GRN_NO", GRNNo);
            objVoucher.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucher.setAttribute("REMARKS", "");
            
            objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
            objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
            
            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
            
            objVoucher.colVoucherItems.clear();
            
            //Get Debit Entries
            Entry1 = 0;
            Entry2a = 0;
            // Entry3=0;
            //  Entry4=0;
            System.out.println("---19---");
//            HashMap debitEntries = getDebitPJVEntriesHSNGST(GRNNo, 1, PONo, PODate, InvoiceNo, InvoiceDate, InvoiceAmount, GRNDate);
            HashMap debitEntries = getDebitPJVEntriesHSNGSTwithInputCredit(GRNNo, 1, PONo, PODate, InvoiceNo, InvoiceDate, InvoiceAmount, GRNDate);
            System.out.println("---20---");
            for (int e = 1; e <= debitEntries.size(); e++) {
                System.out.println("---21---");
                VoucherSrNo++;
                clsVoucherItem objItem = (clsVoucherItem) debitEntries.get(Integer.toString(e));
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 1) {
                    Entry1 += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                if (objItem.getAttribute("ENTRY_TYPE").getInt() == 2) {
                    Entry2a += objItem.getAttribute("AMOUNT").getDouble();
                }
                
                //Entry1+=objItem.getAttribute("AMOUNT").getDouble();
                DefaultMainCode = objItem.getAttribute("MAIN_ACCOUNT_CODE").getString();
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objItem);
            }
            
            System.out.println("---22---");
            //Round the Entry 1
            Entry1 = EITLERPGLOBAL.round(Entry1, 2);
            
            VoucherSrNo++;
            objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "C");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "125019");
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", Entry1 + Entry2a);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucherItem.setAttribute("HSN_SAC_CODE", "");
            objVoucherItem.setAttribute("ITEM_DESCRIPTION", "");
            objVoucherItem.setAttribute("GSTN_RATE", "");
            
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            
            objVoucher.DoNotValidateAccounts = true;
            
            ToPayAmount = 0;
            
            if (Entry1 > 0 || Entry2a > 0) {
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    System.out.println(objVoucher.LastError);
                    VoucherPosted = false;
                }
            }
            
            String VoucherNo = objVoucher.getAttribute("VOUCHER_NO").getString();
            //=====================================//
            
            //(2) PJV 2
            double Entry5 = EITLERPGLOBAL.round(CustomDuty - LessDuty + ImportEduCess + CVD + Surcharge + CustomEduCess + SpAddDuty + Add, 3);
            
            if (Entry5 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00");// ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Customs Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", CustomsPer + "% Customs Duty");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucherItem.setAttribute("HSN_SAC_CODE", "");
                objVoucherItem.setAttribute("ITEM_DESCRIPTION", "");
                objVoucherItem.setAttribute("GSTN_RATE", "");
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry5);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucherItem.setAttribute("HSN_SAC_CODE", "");
                objVoucherItem.setAttribute("ITEM_DESCRIPTION", "");
                objVoucherItem.setAttribute("GSTN_RATE", "");
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            //(3) PJV 3
            double Entry6 = EITLERPGLOBAL.round(Clear, 3);
            
            if (Entry6 > 0) {
                //=======Normal PJV Entry #2 ==========//
                objVoucher = new clsVoucher();
                objVoucher.LoadData(EITLERPGLOBAL.gCompanyID);
                
                objVoucher.setAttribute("PREFIX", SelPrefix);
                objVoucher.setAttribute("SUFFIX", SelSuffix);
                objVoucher.setAttribute("FFNO", FFNo);
                objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.getCurrentDate());
                if (EITLERPGLOBAL.gCompanyID == 2) {
                    objVoucher.setAttribute("BOOK_CODE", "40");
                } else {
                    objVoucher.setAttribute("BOOK_CODE", "61");
                }
                
                objVoucher.setAttribute("CHEQUE_NO", "");
                objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
                objVoucher.setAttribute("BANK_NAME", "");
                objVoucher.setAttribute("PO_NO", PONo);
                objVoucher.setAttribute("PO_DATE", PODate);
                objVoucher.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucher.setAttribute("INVOICE_DATE", InvoiceDate);
                objVoucher.setAttribute("GRN_NO", GRNNo);
                objVoucher.setAttribute("GRN_DATE", GRNDate);
                objVoucher.setAttribute("ST_CATEGORY", "");
                objVoucher.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucher.setAttribute("REMARKS", "");
                
                objVoucher.setAttribute("LEGACY_NO", ""); // ADDED ON 19/01/2010 BY MRUGESH
                objVoucher.setAttribute("LEGACY_DATE", "0000-00-00"); // ADDED ON 19/01/2010 BY MRUGESH
                
                objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
                FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
                objVoucher.setAttribute("FROM", FirstUserID);
                objVoucher.setAttribute("TO", FirstUserID);
                objVoucher.setAttribute("FROM_REMARKS", "");
                objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Hold Voucher
                
                objVoucher.colVoucherItems.clear();
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 1);
                objVoucherItem.setAttribute("EFFECT", "D");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Clearance Agent Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Clearance Charges");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucherItem.setAttribute("HSN_SAC_CODE", "");
                objVoucherItem.setAttribute("ITEM_DESCRIPTION", "");
                objVoucherItem.setAttribute("GSTN_RATE", "");
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
                objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", 2);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "<Bank Code>");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", Entry6);
                objVoucherItem.setAttribute("REMARKS", "Bank Code");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                objVoucherItem.setAttribute("HSN_SAC_CODE", "");
                objVoucherItem.setAttribute("ITEM_DESCRIPTION", "");
                objVoucherItem.setAttribute("GSTN_RATE", "");
                
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                //=====================================//
                
                objVoucher.DoNotValidateAccounts = true;
                
                objVoucher.UseSpecificVoucherNo = false;
                objVoucher.SpecificVoucherNo = "";
                
                if (!ExVoucherNo.equals("")) {
                    objVoucher.UseSpecificVoucherNo = true;
                    objVoucher.SpecificVoucherNo = ExVoucherNo;
                }
                
                objVoucher.setAttribute("VOUCHER_TYPE", 1);
                objVoucher.IsInternalPosting = true;
                if (!objVoucher.Insert()) {
                    VoucherPosted = false;
                }
            }
            
            return VoucherPosted;
        } catch (Exception e) {
            
            e.printStackTrace();
            return false;
        }
        
    }
    
    public HashMap getDebitPJVEntriesGST(String GRNNo, int GRNType, String PONo, String PODate, String InvoiceNo, String InvoiceDate, double InvoiceAmount, String GRNDate) {
        
        HashMap DebitEntries = new HashMap();
        
        try {
            
            ResultSet rsGRN, rsTmp, rsHeader;
            int VoucherSrNo = 0;
            
            //Get Unique Items from GRN based on Itemwise Account Heads
            String strSQL = "";
            
            strSQL = "SELECT DISTINCT(ITEM_CODE) ";
            strSQL += "FROM ";
            strSQL += "D_FIN_ITEM_CODE_MAPPING A, ";
            strSQL += EITLERPGLOBAL.DBName + ".D_INV_GRN_DETAIL B ";
            strSQL += "WHERE ";
            strSQL += "B.GRN_NO='" + GRNNo + "' AND B.GRN_TYPE=" + GRNType + " AND ";
            strSQL += "SUBSTRING(B.ITEM_ID,1,LENGTH(A.ITEM_CODE))=A.ITEM_CODE AND A.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ";
            
            ResultSet rsItems = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsItems.first();
            
            //Get GRN Header
            rsGRN = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            
            double TotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            double FreightAmount = 0;
            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 1) {//Freight
                FreightAmount = data.getDoubleValueFromDB("SELECT COLUMN_6_AMT FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                FreightAmount += data.getDoubleValueFromDB("SELECT SUM(COLUMN_15_AMT) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            }
            TotalNetAmount = TotalNetAmount - FreightAmount;
            double ItemTotalNetAmount = 0;
            double ItemWeightage = 0;
            double HeaderAmount = getHeaderAmount(GRNNo, GRNType);
            double ItemGrossAmount = 0;
            double AccountedAmount = 0;
            
            double hIGST = 0, hSGST = 0, hCGST = 0, hRCM = 0, hComposition = 0, hGSTCess = 0, hInvAmount = 0;
            
            //Derive header level Tax Amounts
            rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            rsHeader.first();
            if (rsHeader.getRow() > 0) {
                
                hIGST = UtilFunctions.getDouble(rsHeader, "INV_IGST", 0);
                hCGST = UtilFunctions.getDouble(rsHeader, "INV_CGST", 0);
                hSGST = UtilFunctions.getDouble(rsHeader, "INV_SGST", 0);
                hRCM = UtilFunctions.getDouble(rsHeader, "INV_RCM", 0);
                hComposition = UtilFunctions.getDouble(rsHeader, "INV_COMPOSITION", 0);
                hGSTCess = UtilFunctions.getDouble(rsHeader, "INV_GST_COMPOSITION_CESS", 0);
                hInvAmount = UtilFunctions.getDouble(rsHeader, "INV_INVOICE_AMT", 0);
                
            }
            
            //Derive line level tax Amounts
            rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_18_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_15_AMT) AS FREIGHT,SUM(COLUMN_28_AMT) AS INSURANCE,SUM(COLUMN_30_AMT) AS AIR_FREIGHT,SUM(COLUMN_31_AMT) AS OTHERS,SUM(COLUMN_37_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_38_AMT) AS RND_DISCOUNT FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
            rsTmp.first();
            
            if (TotalNetAmount > 0) {
                if (rsItems.getRow() > 0) {
                    while (!rsItems.isAfterLast()) {
                        //Get the Group
                        String ItemGroup = rsItems.getString("ITEM_CODE");
                        String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, ItemGroup);
                        
                        //Calculate Weightage (Pro-rata basis) of the items having this group
                        ItemTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                        ItemWeightage = (ItemTotalNetAmount * 100) / TotalNetAmount;
                        
                        //Calculate Item level distribution of Total Header Amount
                        ItemGrossAmount = (HeaderAmount * ItemWeightage) / 100;
                        
                        rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_18_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_15_AMT) AS FREIGHT,SUM(COLUMN_28_AMT) AS INSURANCE,SUM(COLUMN_30_AMT) AS AIR_FREIGHT,SUM(COLUMN_31_AMT) AS OTHERS,SUM(COLUMN_37_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_38_AMT) AS RND_DISCOUNT FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND ITEM_ID LIKE '" + ItemGroup + "%'");
                        rsTmp.first();
                        if (rsTmp.getRow() > 0) {
                            
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "TRADE_DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "RND_DISCOUNT", 0);
                            ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "FCA", 0);
                            ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "GROSS_AMOUNT", 0);
                            
                            if (UtilFunctions.getInt(rsGRN, "PF_POST", 0) == 0) //Packing & Forwarding
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "PF", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 0) //Freight
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "FREIGHT", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "INSURANCE_POST", 0) == 0) //Insurance
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "INSURANCE", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "AIR_FREIGHT_POST", 0) == 0) //Air Freight
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "AIR_FREIGHT", 0);
                            }
                            
                            if (UtilFunctions.getInt(rsGRN, "OTHERS_POST", 0) == 0) //Other Expenses
                            {
                                ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "OTHERS", 0);
                            }
                            
                            double Entry2a = hIGST + hSGST + hCGST + hRCM + hComposition + hGSTCess;
                            
                            AccountedAmount += ItemGrossAmount;
                            
                            clsVoucherItem objVoucherItem = new clsVoucherItem();
                            VoucherSrNo++;
                            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                            objVoucherItem.setAttribute("EFFECT", "D");
                            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                            objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hInvAmount, 2));
                            objVoucherItem.setAttribute("REMARKS", "");
                            objVoucherItem.setAttribute("PO_NO", PONo);
                            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                            objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                            objVoucherItem.setAttribute("GRN_NO", GRNNo);
                            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                            objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                            objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                            objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                            DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            
                            if (Entry2a > 0) {
                                
                                if (hIGST > 0) {
                                    
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    //    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231758");
                                    
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hIGST, 2));
                                    objVoucherItem.setAttribute("REMARKS", "IGST");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                                
                                if (hSGST > 0) {
                                    
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    //    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231757");
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hSGST, 2));
                                    objVoucherItem.setAttribute("REMARKS", "SGST");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                                if (hCGST > 0) {
                                    
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    //    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231756");
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hCGST, 2));
                                    objVoucherItem.setAttribute("REMARKS", "CGST");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                                if (hRCM > 0) {
                                    
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hRCM, 2));
                                    objVoucherItem.setAttribute("REMARKS", "RCM");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                                
                                if (hComposition > 0) {
                                    
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hComposition, 2));
                                    objVoucherItem.setAttribute("REMARKS", "Composition");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                                if (hGSTCess > 0) {
                                    
                                    objVoucherItem = new clsVoucherItem();
                                    VoucherSrNo++;
                                    objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                    objVoucherItem.setAttribute("EFFECT", "D");
                                    objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                    objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                    objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hGSTCess, 2));
                                    objVoucherItem.setAttribute("REMARKS", "GST Cess");
                                    objVoucherItem.setAttribute("PO_NO", PONo);
                                    objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                    objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                    objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                    objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                    objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                    objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                    objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                    objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                    objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                    DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                }
                            }
                            //**********************************************************************//
                        }
                        rsItems.next();
                    }
                    
                    /*
                     //Add Default
                     double Diff=EITLERPGLOBAL.round(TotalNetAmount-AccountedAmount,2);
                     
                     if(EITLERPGLOBAL.round(Diff,2)>0) {
                     
                     String MainCode=clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                     
                     clsVoucherItem objVoucherItem=new clsVoucherItem();
                     VoucherSrNo++;
                     objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                     objVoucherItem.setAttribute("EFFECT","D");
                     objVoucherItem.setAttribute("ACCOUNT_ID",1);
                     objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                     objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                     objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(Diff,2));
                     objVoucherItem.setAttribute("REMARKS","diff");
                     objVoucherItem.setAttribute("PO_NO",PONo);
                     objVoucherItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(PODate));
                     objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                     objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                     objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                     objVoucherItem.setAttribute("GRN_NO",GRNNo);
                     objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(GRNDate));
                     objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                     objVoucherItem.setAttribute("MODULE_ID",clsGRNGen.ModuleID);
                     objVoucherItem.setAttribute("ENTRY_TYPE",1);
                     DebitEntries.put(Integer.toString(DebitEntries.size()+1), objVoucherItem);
                     
                     
                     //************************** Include Difference Tax Amounts ***************************
                     double Entry2a = hIGST+hSGST+hCGST+hRCM+hComposition+hGSTCess;
                     //     double Entry3=totalST;
                     //     double Entry4=totalOctroi;
                     
                     
                     if(Entry2a>0) {
                     
                     if(hIGST>0) {
                     
                     objVoucherItem=new clsVoucherItem();
                     VoucherSrNo++;
                     objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                     objVoucherItem.setAttribute("EFFECT","D");
                     objVoucherItem.setAttribute("ACCOUNT_ID",1);
                     objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                     objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                     objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(hIGST,2));
                     objVoucherItem.setAttribute("REMARKS","IGST");
                     objVoucherItem.setAttribute("PO_NO",PONo);
                     objVoucherItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(PODate));
                     objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                     objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                     objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                     objVoucherItem.setAttribute("GRN_NO",GRNNo);
                     objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(GRNDate));
                     objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                     objVoucherItem.setAttribute("MODULE_ID",clsGRNGen.ModuleID);
                     objVoucherItem.setAttribute("ENTRY_TYPE",2);
                     DebitEntries.put(Integer.toString(DebitEntries.size()+1), objVoucherItem);
                     }
                     
                     if(hSGST>0) {
                     
                     objVoucherItem=new clsVoucherItem();
                     VoucherSrNo++;
                     objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                     objVoucherItem.setAttribute("EFFECT","D");
                     objVoucherItem.setAttribute("ACCOUNT_ID",1);
                     objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                     objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                     objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(hSGST,2));
                     objVoucherItem.setAttribute("REMARKS","SGST");
                     objVoucherItem.setAttribute("PO_NO",PONo);
                     objVoucherItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(PODate));
                     objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                     objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                     objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                     objVoucherItem.setAttribute("GRN_NO",GRNNo);
                     objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(GRNDate));
                     objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                     objVoucherItem.setAttribute("MODULE_ID",clsGRNGen.ModuleID);
                     objVoucherItem.setAttribute("ENTRY_TYPE",2);
                     DebitEntries.put(Integer.toString(DebitEntries.size()+1), objVoucherItem);
                     }
                     if(hCGST>0) {
                     
                     objVoucherItem=new clsVoucherItem();
                     VoucherSrNo++;
                     objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                     objVoucherItem.setAttribute("EFFECT","D");
                     objVoucherItem.setAttribute("ACCOUNT_ID",1);
                     objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                     objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                     objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(hCGST,2));
                     objVoucherItem.setAttribute("REMARKS","CGST");
                     objVoucherItem.setAttribute("PO_NO",PONo);
                     objVoucherItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(PODate));
                     objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                     objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                     objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                     objVoucherItem.setAttribute("GRN_NO",GRNNo);
                     objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(GRNDate));
                     objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                     objVoucherItem.setAttribute("MODULE_ID",clsGRNGen.ModuleID);
                     objVoucherItem.setAttribute("ENTRY_TYPE",2);
                     DebitEntries.put(Integer.toString(DebitEntries.size()+1), objVoucherItem);
                     }
                     if(hRCM>0) {
                     
                     objVoucherItem=new clsVoucherItem();
                     VoucherSrNo++;
                     objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                     objVoucherItem.setAttribute("EFFECT","D");
                     objVoucherItem.setAttribute("ACCOUNT_ID",1);
                     objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                     objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                     objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(hRCM,2));
                     objVoucherItem.setAttribute("REMARKS","RCM");
                     objVoucherItem.setAttribute("PO_NO",PONo);
                     objVoucherItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(PODate));
                     objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                     objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                     objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                     objVoucherItem.setAttribute("GRN_NO",GRNNo);
                     objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(GRNDate));
                     objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                     objVoucherItem.setAttribute("MODULE_ID",clsGRNGen.ModuleID);
                     objVoucherItem.setAttribute("ENTRY_TYPE",2);
                     DebitEntries.put(Integer.toString(DebitEntries.size()+1), objVoucherItem);
                     }
                     
                     
                     if(hComposition>0) {
                     
                     objVoucherItem=new clsVoucherItem();
                     VoucherSrNo++;
                     objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                     objVoucherItem.setAttribute("EFFECT","D");
                     objVoucherItem.setAttribute("ACCOUNT_ID",1);
                     objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                     objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                     objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(hComposition,2));
                     objVoucherItem.setAttribute("REMARKS","Composition");
                     objVoucherItem.setAttribute("PO_NO",PONo);
                     objVoucherItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(PODate));
                     objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                     objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                     objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                     objVoucherItem.setAttribute("GRN_NO",GRNNo);
                     objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(GRNDate));
                     objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                     objVoucherItem.setAttribute("MODULE_ID",clsGRNGen.ModuleID);
                     objVoucherItem.setAttribute("ENTRY_TYPE",2);
                     DebitEntries.put(Integer.toString(DebitEntries.size()+1), objVoucherItem);
                     }
                     if(hGSTCess>0) {
                     
                     objVoucherItem=new clsVoucherItem();
                     VoucherSrNo++;
                     objVoucherItem.setAttribute("SR_NO",VoucherSrNo);
                     objVoucherItem.setAttribute("EFFECT","D");
                     objVoucherItem.setAttribute("ACCOUNT_ID",1);
                     objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                     objVoucherItem.setAttribute("SUB_ACCOUNT_CODE","");
                     objVoucherItem.setAttribute("AMOUNT",EITLERPGLOBAL.round(hGSTCess,2));
                     objVoucherItem.setAttribute("REMARKS","GST Cess");
                     objVoucherItem.setAttribute("PO_NO",PONo);
                     objVoucherItem.setAttribute("PO_DATE",EITLERPGLOBAL.formatDate(PODate));
                     objVoucherItem.setAttribute("INVOICE_NO",InvoiceNo);
                     objVoucherItem.setAttribute("INVOICE_DATE",EITLERPGLOBAL.formatDate(InvoiceDate));
                     objVoucherItem.setAttribute("INVOICE_AMOUNT",InvoiceAmount);
                     objVoucherItem.setAttribute("GRN_NO",GRNNo);
                     objVoucherItem.setAttribute("GRN_DATE",EITLERPGLOBAL.formatDate(GRNDate));
                     objVoucherItem.setAttribute("REF_COMPANY_ID",EITLERPGLOBAL.gCompanyID);
                     objVoucherItem.setAttribute("MODULE_ID",clsGRNGen.ModuleID);
                     objVoucherItem.setAttribute("ENTRY_TYPE",2);
                     DebitEntries.put(Integer.toString(DebitEntries.size()+1), objVoucherItem);
                     }
                     }
                     
                     
                     
                     
                     
                     
                     }*/
                } else {
                    //Default Account Header
                    
                    //Get the Group
                    String MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                    
                    //Calculate Weightage (Pro-rata basis) of the items having this group
                    ItemTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                    ItemWeightage = (ItemTotalNetAmount * 100) / TotalNetAmount;
                    
                    //Calculate Item level distribution of Total Header Amount
                    ItemGrossAmount = (HeaderAmount * ItemWeightage) / 100;
                    
                    double Excise = 0;
                    double Cess = 0;
                    double Cenvat = 0;
                    double ST = 0;
                    double Octroi = 0;
                    double SpExcise = 0;
                    
                    rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                    rsHeader.first();
                    if (rsHeader.getRow() > 0) {
                        Excise = (UtilFunctions.getDouble(rsHeader, "COLUMN_3_AMT", 0) * ItemWeightage) / 100;
                        Cess = (UtilFunctions.getDouble(rsHeader, "COLUMN_4_AMT", 0) * ItemWeightage) / 100;
                        Cenvat = (UtilFunctions.getDouble(rsHeader, "COLUMN_8_AMT", 0) * ItemWeightage) / 100;
                        ST = (UtilFunctions.getDouble(rsHeader, "COLUMN_5_AMT", 0) * ItemWeightage) / 100;
                        Octroi = (UtilFunctions.getDouble(rsHeader, "COLUMN_7_AMT", 0) * ItemWeightage) / 100;
                        SpExcise = (UtilFunctions.getDouble(rsHeader, "COLUMN_17_AMT", 0) * ItemWeightage) / 100;
                    }
                    
                    rsTmp = data.getResult("SELECT SUM(TOTAL_AMOUNT) AS GROSS_AMOUNT,SUM(COLUMN_1_AMT) AS DISCOUNT,SUM(COLUMN_18_AMT) AS FCA,SUM(COLUMN_2_AMT) AS PF,SUM(COLUMN_15_AMT) AS FREIGHT,SUM(COLUMN_28_AMT) AS INSURANCE,SUM(COLUMN_30_AMT) AS AIR_FREIGHT,SUM(COLUMN_31_AMT) AS OTHERS,SUM(COLUMN_37_AMT) AS TRADE_DISCOUNT,SUM(COLUMN_38_AMT) AS RND_DISCOUNT,SUM(COLUMN_12_AMT) AS EXCISE,SUM(COLUMN_13_AMT) AS CESS,SUM(COLUMN_17_AMT) AS CENVAT,SUM(COLUMN_14_AMT) AS ST,SUM(COLUMN_16_AMT) AS OCTROI,SUM(COLUMN_26_AMT) AS SP_EXCISE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " ");
                    rsTmp.first();
                    if (rsTmp.getRow() > 0) {
                        
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "TRADE_DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount - UtilFunctions.getDouble(rsTmp, "RND_DISCOUNT", 0);
                        ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "FCA", 0);
                        ItemGrossAmount = ItemGrossAmount + UtilFunctions.getDouble(rsTmp, "GROSS_AMOUNT", 0);
                        
                        Excise += UtilFunctions.getDouble(rsTmp, "EXCISE", 0);
                        Cess += UtilFunctions.getDouble(rsTmp, "CESS", 0);
                        Cenvat += UtilFunctions.getDouble(rsTmp, "CENVAT", 0);
                        ST += UtilFunctions.getDouble(rsTmp, "ST", 0);
                        Octroi += UtilFunctions.getDouble(rsTmp, "OCTROI", 0);
                        SpExcise += UtilFunctions.getDouble(rsTmp, "SP_EXCISE", 0);
                        
                        if (UtilFunctions.getInt(rsGRN, "PF_POST", 0) == 0) //Packing & Forwarding
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "PF", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 0) //Freight
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "FREIGHT", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "INSURANCE_POST", 0) == 0) //Insurance
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "INSURANCE", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "AIR_FREIGHT_POST", 0) == 0) //Air Freight
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "AIR_FREIGHT", 0);
                        }
                        
                        if (UtilFunctions.getInt(rsGRN, "OTHERS_POST", 0) == 0) //Other Expenses
                        {
                            ItemGrossAmount += UtilFunctions.getDouble(rsTmp, "OTHERS", 0);
                        }
                        
                        //Do the Entry
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hInvAmount, 2));
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        
                        //**** Include Sales Tax, Excise and Octroi Amounts bifurcations *******//
                        double Entry2a = hIGST + hSGST + hCGST + hRCM + hComposition + hGSTCess;
                        //     double Entry3=totalST;
                        //     double Entry4=totalOctroi;
                        
                        if (Entry2a > 0) {
                            
                            if (hIGST > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                //    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231758");
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hIGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "IGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (hSGST > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                //    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231757");
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hSGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "SGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hCGST > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                //    objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE",MainCode);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231756");
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hCGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "CGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hRCM > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hRCM, 2));
                                objVoucherItem.setAttribute("REMARKS", "RCM");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (hComposition > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hComposition, 2));
                                objVoucherItem.setAttribute("REMARKS", "Composition");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hGSTCess > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hGSTCess, 2));
                                objVoucherItem.setAttribute("REMARKS", "GST Cess");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                        }
                        
                    }
                    //**********************************************************************//
                    
                }
                
            }
            
        } catch (Exception e) {
            
        }
        
        return DebitEntries;
    }
    
    public HashMap getDebitPJVEntriesHSNGST(String GRNNo, int GRNType, String PONo, String PODate, String InvoiceNo, String InvoiceDate, double InvoiceAmount, String GRNDate) {
        
        HashMap DebitEntries = new HashMap();
        try {
            ResultSet rsGRN, rsTmp, rsHeader, rsHSNHeader;
            int VoucherSrNo = 0;
            //Get Unique Items from GRN based on Itemwise Account Heads
            String strSQL = "";
            
            //Get GRN Header
            rsGRN = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            
            double TotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            double GRNHSNNetAmount = data.getDoubleValueFromDB("SELECT  SUM(COALESCE(INVOICE_AMOUNT,0)+COALESCE(INVOICE_CGST,0)+COALESCE(INVOICE_SGST,0)+COALESCE(INVOICE_IGST,0)+COALESCE(INVOICE_RCM,0)+COALESCE(INVOICE_COMPOSITION,0)+COALESCE(INVOICE_GST_COMP_CESS,0)) FROM D_INV_GRN_HSN WHERE GRN_NO='" + GRNNo + "'");
            
            double FreightAmount = 0;
            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 1) {//Freight
                FreightAmount = data.getDoubleValueFromDB("SELECT COLUMN_6_AMT FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                FreightAmount += data.getDoubleValueFromDB("SELECT SUM(COLUMN_15_AMT) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            }
            TotalNetAmount = TotalNetAmount - FreightAmount;
            double ItemTotalNetAmount = 0;
            double hsnTotalNetAmount = 0;
            double ItemWeightage = 0;
            double HeaderAmount = getHeaderAmount(GRNNo, GRNType);
            
            double hIGST = 0, hSGST = 0, hCGST = 0, hRCM = 0, hComposition = 0, hGSTCess = 0, hInvAmount = 0;
            
            //Derive header level Tax Amounts
            rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            rsHeader.first();
            if (rsHeader.getRow() > 0) {
                hIGST = UtilFunctions.getDouble(rsHeader, "INV_IGST", 0);
                hCGST = UtilFunctions.getDouble(rsHeader, "INV_CGST", 0);
                hSGST = UtilFunctions.getDouble(rsHeader, "INV_SGST", 0);
                hRCM = UtilFunctions.getDouble(rsHeader, "INV_RCM", 0);
                hComposition = UtilFunctions.getDouble(rsHeader, "INV_COMPOSITION", 0);
                hGSTCess = UtilFunctions.getDouble(rsHeader, "INV_GST_COMPOSITION_CESS", 0);
                hInvAmount = UtilFunctions.getDouble(rsHeader, "INV_INVOICE_AMT", 0);
            }
            
            double Total = TotalNetAmount+GRNHSNNetAmount;
            System.out.println("Total Amount HSNGRN = "+Total);
            
            String strHSN = "SELECT * FROM D_INV_GRN_HSN WHERE GRN_NO='" + GRNNo + "'";
            rsHSNHeader = data.getResult(strHSN);
            rsHSNHeader.first();
            
            
            if (Total > 0) {
                if (rsHSNHeader.getRow() > 0) {
                    while (!rsHSNHeader.isAfterLast()) {
                        String hsnsacCode = UtilFunctions.getString(rsHSNHeader, "HSN_CODE", "");
                        String ItemGroup = data.getStringValueFromDB("SELECT DISTINCT(ITEM_CODE) FROM D_FIN_ITEM_CODE_MAPPING A," + EITLERPGLOBAL.DBName + ".D_INV_GRN_DETAIL B WHERE B.GRN_NO='" + GRNNo + "' AND B.HSN_SAC_CODE='" + hsnsacCode + "' AND SUBSTRING(B.ITEM_ID,1,LENGTH(A.ITEM_CODE))=A.ITEM_CODE AND A.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "", FinanceGlobal.FinURL);
                        String item = data.getStringValueFromDB("SELECT DISTINCT ITEM_ID  FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND HSN_SAC_CODE='" + hsnsacCode + "'");
                        String itemDesc = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, item);
                        String MainCode = "";
                        if (ItemGroup.equals("")) {
                            MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                        } else {
                            MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, ItemGroup);
                        }
                        
                        double hsnInvAmt = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_AMOUNT", 0);
                        double hsnInvCGST = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_CGST", 0);
                        double hsnInvSGST = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_SGST", 0);
                        double hsnInvIGST = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_IGST", 0);
                        double hsnInvRCM = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_RCM", 0);
                        double hsnInvComposition = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_COMPOSITION", 0);
                        double hsnInvGSTCess = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_GST_COMP_CESS", 0);
                        
                        hsnTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND HSN_SAC_CODE='" + hsnsacCode + "'");
                        
                        double Entry2a = hsnInvIGST + hsnInvSGST + hsnInvCGST + hsnInvRCM + hsnInvComposition + hsnInvGSTCess;
                        
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvAmt, 2));
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                        objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                        objVoucherItem.setAttribute("GSTN_RATE", "");
                        
                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        
                        if (Entry2a > 0) {
                            
                            if (hsnInvIGST > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231758");
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvIGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "IGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                
                            }
                            
                            if (hsnInvCGST > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231756");
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvCGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "CGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hsnInvSGST > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231757");
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvSGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "SGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hsnInvRCM > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvRCM, 2));
                                objVoucherItem.setAttribute("REMARKS", "RCM");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (hsnInvComposition > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvComposition, 2));
                                objVoucherItem.setAttribute("REMARKS", "Composition");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hsnInvGSTCess > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvGSTCess, 2));
                                objVoucherItem.setAttribute("REMARKS", "GST Cess");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                        }
                        rsHSNHeader.next();
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DebitEntries;
    }
    
    public HashMap getDebitPJVEntriesHSNGSTwithInputCredit(String GRNNo, int GRNType, String PONo, String PODate, String InvoiceNo, String InvoiceDate, double InvoiceAmount, String GRNDate) {
        
        HashMap DebitEntries = new HashMap();
        try {
            ResultSet rsGRN, rsTmp, rsHeader, rsHSNHeader;
            int VoucherSrNo = 0;
            //Get Unique Items from GRN based on Itemwise Account Heads
            String strSQL = "";
            
            //Get GRN Header
            rsGRN = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            
            double TotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            double GRNHSNNetAmount = data.getDoubleValueFromDB("SELECT  SUM(COALESCE(INVOICE_AMOUNT,0)+COALESCE(INVOICE_CGST,0)+COALESCE(INVOICE_SGST,0)+COALESCE(INVOICE_IGST,0)+COALESCE(INVOICE_RCM,0)+COALESCE(INVOICE_COMPOSITION,0)+COALESCE(INVOICE_GST_COMP_CESS,0)) FROM D_INV_GRN_HSN WHERE GRN_NO='" + GRNNo + "'");
            
            double FreightAmount = 0;
            if (UtilFunctions.getInt(rsGRN, "FREIGHT_POST", 0) == 1) {//Freight
                FreightAmount = data.getDoubleValueFromDB("SELECT COLUMN_6_AMT FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
                FreightAmount += data.getDoubleValueFromDB("SELECT SUM(COLUMN_15_AMT) FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            }
            TotalNetAmount = TotalNetAmount - FreightAmount;
            double ItemTotalNetAmount = 0;
            double hsnTotalNetAmount = 0;
            double ItemWeightage = 0;
            double HeaderAmount = getHeaderAmount(GRNNo, GRNType);
            
            double hIGST = 0, hSGST = 0, hCGST = 0, hRCM = 0, hComposition = 0, hGSTCess = 0, hInvAmount = 0;
            
            //Derive header level Tax Amounts
            rsHeader = data.getResult("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType);
            rsHeader.first();
            if (rsHeader.getRow() > 0) {
                hIGST = UtilFunctions.getDouble(rsHeader, "INV_IGST", 0);
                hCGST = UtilFunctions.getDouble(rsHeader, "INV_CGST", 0);
                hSGST = UtilFunctions.getDouble(rsHeader, "INV_SGST", 0);
                hRCM = UtilFunctions.getDouble(rsHeader, "INV_RCM", 0);
                hComposition = UtilFunctions.getDouble(rsHeader, "INV_COMPOSITION", 0);
                hGSTCess = UtilFunctions.getDouble(rsHeader, "INV_GST_COMPOSITION_CESS", 0);
                hInvAmount = UtilFunctions.getDouble(rsHeader, "INV_INVOICE_AMT", 0);
            }
            
            double Total = TotalNetAmount+GRNHSNNetAmount;
            System.out.println("Total Amount HSNGRN = "+Total);
            
            String strHSN = "SELECT * FROM D_INV_GRN_HSN WHERE GRN_NO='" + GRNNo + "'";
            rsHSNHeader = data.getResult(strHSN);
            rsHSNHeader.first();
            
            
            if (Total > 0) {
                if (rsHSNHeader.getRow() > 0) {
                    while (!rsHSNHeader.isAfterLast()) {
                        String hsnsacCode = UtilFunctions.getString(rsHSNHeader, "HSN_CODE", "");
                        String ItemGroup = data.getStringValueFromDB("SELECT DISTINCT(ITEM_CODE) FROM D_FIN_ITEM_CODE_MAPPING A," + EITLERPGLOBAL.DBName + ".D_INV_GRN_DETAIL B WHERE B.GRN_NO='" + GRNNo + "' AND B.HSN_SAC_CODE='" + hsnsacCode + "' AND SUBSTRING(B.ITEM_ID,1,LENGTH(A.ITEM_CODE))=A.ITEM_CODE AND A.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "", FinanceGlobal.FinURL);
                        String item = data.getStringValueFromDB("SELECT DISTINCT ITEM_ID  FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND HSN_SAC_CODE='" + hsnsacCode + "'");
                        String itemDesc = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, item);
                        String MainCode = "";
                        if (ItemGroup.equals("")) {
                            MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                        } else {
                            MainCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, ItemGroup);
                        }
                        
                        double hsnInvAmt = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_AMOUNT", 0);
                        double hsnInvCGST = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_CGST", 0);
                        double hsnInvSGST = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_SGST", 0);
                        double hsnInvIGST = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_IGST", 0);
                        double hsnInvRCM = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_RCM", 0);
                        double hsnInvComposition = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_COMPOSITION", 0);
                        double hsnInvGSTCess = UtilFunctions.getDouble(rsHSNHeader, "INVOICE_GST_COMP_CESS", 0);
                        
                        double hsnInputCGSTCrPer = UtilFunctions.getDouble(rsHSNHeader, "CGST_INPUT_CR_PER", 0);
                        double hsnInputSGSTCrPer = UtilFunctions.getDouble(rsHSNHeader, "SGST_INPUT_CR_PER", 0);
                        double hsnInputIGSTCrPer = UtilFunctions.getDouble(rsHSNHeader, "IGST_INPUT_CR_PER", 0);

                        double hsnInputCGSTCr = UtilFunctions.getDouble(rsHSNHeader, "CGST_INPUT_CR_AMT", 0);
                        double hsnInputSGSTCr = UtilFunctions.getDouble(rsHSNHeader, "SGST_INPUT_CR_AMT", 0);
                        double hsnInputIGSTCr = UtilFunctions.getDouble(rsHSNHeader, "IGST_INPUT_CR_AMT", 0);
                        
                        hsnTotalNetAmount = data.getDoubleValueFromDB("SELECT SUM(QTY*LANDED_RATE) AS VALUE FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND GRN_TYPE=" + GRNType + " AND HSN_SAC_CODE='" + hsnsacCode + "'");
                        
                        double Entry2a = hsnInvIGST + hsnInvSGST + hsnInvCGST + hsnInvRCM + hsnInvComposition + hsnInvGSTCess;
                        
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        VoucherSrNo++;
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "D");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvAmt, 2));
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                        objVoucherItem.setAttribute("ENTRY_TYPE", 1);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                        objVoucherItem.setAttribute("GSTN_RATE", "");
                        
                        DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                        
                        if (Entry2a > 0) {
                            
                            if (hsnInvIGST > 0) {
                                String inputcrmaincode="";
                                //double inputcrpercent=(hsnInputIGSTCr*100)/hsnInvAmt;
                                double inputcrpercent = hsnInputIGSTCrPer ;
                                if(inputcrpercent==12){
                                    inputcrmaincode="231777";
                                } else if(inputcrpercent==18){
                                    inputcrmaincode="231779";
                                } else if(inputcrpercent==28){
                                    inputcrmaincode="231780";
                                } else if(inputcrpercent==5){
                                    inputcrmaincode="231776";
                                } else{
                                    inputcrmaincode="231758";
                                }                                
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                //objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231758");
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", inputcrmaincode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvIGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "IGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                                
                            }
                            
                            if (hsnInvCGST > 0) {
                                String inputcrmaincode="";
                                //double inputcrpercent=(hsnInputCGSTCr*100)/hsnInvAmt;
                                double inputcrpercent=hsnInputCGSTCrPer;
                                if(inputcrpercent==14){
                                    inputcrmaincode="231774";
                                } else if(inputcrpercent==9){
                                    inputcrmaincode="231772";
                                } else if(inputcrpercent==6){
                                    inputcrmaincode="231770";
                                } else if(inputcrpercent==2.5){
                                    inputcrmaincode="231768";
                                } else{
                                    inputcrmaincode="231756";
                                }
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                //objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231756");
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", inputcrmaincode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvCGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "CGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hsnInvSGST > 0) {
                                String inputcrmaincode="";
                                //double inputcrpercent=(hsnInputSGSTCr*100)/hsnInvAmt;
                                double inputcrpercent=hsnInputSGSTCrPer;
                                if(inputcrpercent==14){
                                    inputcrmaincode="231775";
                                } else if(inputcrpercent==9){
                                    inputcrmaincode="231773";
                                } else if(inputcrpercent==6){
                                    inputcrmaincode="231771";
                                } else if(inputcrpercent==2.5){
                                    inputcrmaincode="231769";
                                } else{
                                    inputcrmaincode="231757";
                                }
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                //objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "231757");
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", inputcrmaincode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvSGST, 2));
                                objVoucherItem.setAttribute("REMARKS", "SGST");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hsnInvRCM > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvRCM, 2));
                                objVoucherItem.setAttribute("REMARKS", "RCM");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            
                            if (hsnInvComposition > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvComposition, 2));
                                objVoucherItem.setAttribute("REMARKS", "Composition");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                            if (hsnInvGSTCess > 0) {
                                
                                objVoucherItem = new clsVoucherItem();
                                VoucherSrNo++;
                                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                                objVoucherItem.setAttribute("EFFECT", "D");
                                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", MainCode);
                                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                                objVoucherItem.setAttribute("AMOUNT", EITLERPGLOBAL.round(hsnInvGSTCess, 2));
                                objVoucherItem.setAttribute("REMARKS", "GST Cess");
                                objVoucherItem.setAttribute("PO_NO", PONo);
                                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                                objVoucherItem.setAttribute("INVOICE_AMOUNT", InvoiceAmount);
                                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                                objVoucherItem.setAttribute("REF_COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                                objVoucherItem.setAttribute("MODULE_ID", clsGRNGen.ModuleID);
                                objVoucherItem.setAttribute("ENTRY_TYPE", 2);
                                objVoucherItem.setAttribute("HSN_SAC_CODE", hsnsacCode);
                                objVoucherItem.setAttribute("ITEM_DESCRIPTION", itemDesc);
                                objVoucherItem.setAttribute("GSTN_RATE", "");
                                
                                DebitEntries.put(Integer.toString(DebitEntries.size() + 1), objVoucherItem);
                            }
                        }
                        rsHSNHeader.next();
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DebitEntries;
    }
    
    
    public boolean PostDNGST(int CompanyID, String GRNNo, String GRNDate) {
        try {
            //Get Object
            
            System.out.println("SSSS 0");
            System.out.println("GRNN0  " + GRNNo);
            System.out.println("GRNDate  " + GRNDate);
            
            clsGRNGen objGRNGen = (clsGRNGen) (new clsGRNGen()).getObject(CompanyID, GRNNo, GRNDate);
            
            String ItemGroup = "";
            String GRNAccountCode = "";
            
            String strSQL = "";
            
            strSQL = "SELECT DISTINCT(ITEM_CODE) ";
            strSQL += "FROM ";
            strSQL += "D_FIN_ITEM_CODE_MAPPING A, ";
            strSQL += EITLERPGLOBAL.DBName + ".D_INV_GRN_DETAIL B ";
            strSQL += "WHERE ";
            strSQL += "B.GRN_NO='" + GRNNo + "' AND B.GRN_TYPE=1 AND ";
            strSQL += "SUBSTRING(B.ITEM_ID,1,LENGTH(A.ITEM_CODE))=A.ITEM_CODE AND A.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ";
            
            ResultSet rsItems = data.getResult(strSQL, FinanceGlobal.FinURL);
            rsItems.first();
            
            clsVoucher objVoucher = new clsVoucher();
            //.getAttribute("DB_GROSS_AMT").getVal()
            double DB_INVOICE_AMOUNT = objGRNGen.getAttribute("DB_INVOICE_AMT").getVal();
            double DB_CGST = objGRNGen.getAttribute("DB_CGST").getVal();
            double DB_SGST = objGRNGen.getAttribute("DB_SGST").getVal();
            double DB_IGST = objGRNGen.getAttribute("DB_IGST").getVal();
            double DB_RCM = objGRNGen.getAttribute("DB_RCM").getVal();
            double DB_COMPOSITION = objGRNGen.getAttribute("DB_COMPOSITION").getVal();
            double DB_GST_COMPENSATION_CESS = objGRNGen.getAttribute("DB_GST_COMPENSATION_CESS").getVal();
            double DB_NET_AMOUNT = objGRNGen.getAttribute("DB_NET_AMT").getVal();
            
            String PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND PO_NO<>'' ");
            String PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "'");
            String InvoiceNo = data.getStringValueFromDB("SELECT INVOICE_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND INVOICE_NO<>'' ");
            String InvoiceDate = data.getStringValueFromDB("SELECT INVOICE_DATE FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND INVOICE_NO<>'' ");
            
            String sql1 = "SELECT DISTINCT COLUMN_5_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ";
            String sql2 = "SELECT DISTINCT COLUMN_4_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ";
            String sql3 = "SELECT DISTINCT COLUMN_3_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ";
            
            System.out.println(sql1);
            System.out.println(sql2);
            System.out.println(sql3);
            
            Double IGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_5_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ");
            Double SGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_4_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ");
            Double CGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_3_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ");
            
            String IGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='IGST' AND GST_PERCENT =" + IGSTPER);
            String SGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='SGST' AND GST_PERCENT =" + SGSTPER);
            String CGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='CGST' AND GST_PERCENT =" + CGSTPER);
            
            System.out.println("IGST PER " + IGSTPER);
            System.out.println("SGST PER " + SGSTPER);
            System.out.println("CGST PER " + CGSTPER);
            System.out.println("IGST PAYABLE ACCOUNT : " + IGSTPayableCode);
            System.out.println("SGST PAYABLE ACCOUNT : " + SGSTPayableCode);
            System.out.println("CGST PAYABLE ACCOUNT : " + CGSTPayableCode);
            
            if (IGSTPayableCode.equals("")) {
                IGSTPayableCode = "<IGST Payable Code>";
            }
            if (SGSTPayableCode.equals("")) {
                SGSTPayableCode = "<SGST Payable Code>";
            }
            if (CGSTPayableCode.equals("")) {
                CGSTPayableCode = "<CGST Payable Code>";
            }
            
            System.out.println("PO NO " + PONo);
            System.out.println("PO DATE " + PODate);
            System.out.println("INVOICE NO " + InvoiceNo);
            System.out.println("INVOICE DATE " + InvoiceDate);
            
            System.out.println("DB INVOICE AMT = " + objGRNGen.getAttribute("DB_INVOICE_AMT").getVal());
            System.out.println("DB CGST = " + objGRNGen.getAttribute("DB_CGST").getVal());
            System.out.println("DB SGST = " + objGRNGen.getAttribute("DB_SGST").getVal());
            System.out.println("DB IGST = " + objGRNGen.getAttribute("DB_IGST").getVal());
            System.out.println("DB RCM = " + objGRNGen.getAttribute("DB_RCM").getVal());
            System.out.println("DB COMPOSITION = " + objGRNGen.getAttribute("DB_COMPOSITION").getVal());
            System.out.println("DB_GST_COMPENSATION_CESS = " + objGRNGen.getAttribute("DB_GST_COMPENSATION_CESS").getVal());
            System.out.println("DB NET AMT = " + objGRNGen.getAttribute("DB_NET_AMT").getVal());
            
            if (rsItems.getRow() > 0) {
                while (!rsItems.isAfterLast()) {
                    //Get the Group
                    ItemGroup = rsItems.getString("ITEM_CODE");
                    GRNAccountCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, ItemGroup);
                    
                    System.out.println("GRN ACCOUNT CODE1 " + GRNAccountCode);
                    
                    rsItems.next();
                }
            }
            
            if (GRNAccountCode.equals("")) {
                
                GRNAccountCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                System.out.println("GRN ACCOUNT CODE default " + GRNAccountCode);
            }
            
            System.out.println("GRN ACCOUNT CODE2 " + GRNAccountCode);
            String PartyCode = objGRNGen.getAttribute("SUPP_ID").getString();
            
            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;
            
            System.out.println("SSSS 1");
            //****** Prepare Voucher Object ********//
            
            //  setAttribute("FIN_HIERARCHY_ID",0);
            setAttribute("FIN_HIERARCHY_ID", 1796); //FOR TEST SERVER
            //  setAttribute("FIN_HIERARCHY_ID",1668); // FOR LIVE
            
            //(1) Select the Hierarchy
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "CHOOSE_HIERARCHY", "PUR_DN_AUTO", "");
            
            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                System.out.println("HID 1 1" + HierarchyID);
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }
            
            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.DebitNoteModuleID);
            rsVoucher.first();
            
            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }
            
            int VoucherSrNo = 0;
            
            objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            
            //SUITING
            objVoucher.setAttribute("BOOK_CODE", "45");
            //GRNAccountCode="405021";
            
            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_DEBIT_NOTE);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "0000-00-00");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "0000-00-00");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
            objVoucher.setAttribute("REMARKS", "");
            
            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            System.out.println("Hierarchy ID = " + getAttribute("FIN_HIERARCHY_ID").getInt());
            System.out.println("First user ID = " + FirstUserID);
            
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Final Approve
            
            objVoucher.colVoucherItems.clear();
            
            //(1) Entry no.1 => Debit party with net amount
            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            String PartyMainCode = "125019";
            
            //(1) Sales Account
            if (DB_INVOICE_AMOUNT > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", GRNAccountCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", DB_INVOICE_AMOUNT);
                objVoucherItem.setAttribute("REMARKS", "");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            
            //(2) Hundi Charges
            if (DB_RCM > 0) {
                
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450227");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", DB_RCM);
                objVoucherItem.setAttribute("REMARKS", "RCM");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
            }
            
            //(3) Insurance Charges
            if (DB_COMPOSITION > 0) {
                
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", DB_COMPOSITION);
                objVoucherItem.setAttribute("REMARKS", "COMPOSITION");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
            }
            
            //(4) Bank Charges
            if (DB_GST_COMPENSATION_CESS > 0) {
                
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450038");
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", DB_GST_COMPENSATION_CESS);
                objVoucherItem.setAttribute("REMARKS", "GST Compensation cess");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                
            }
            
            //(10) GST Amount
            if (DB_CGST > 0) {
                
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", CGSTPayableCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", DB_CGST);
                objVoucherItem.setAttribute("REMARKS", "CGST");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            
            if (DB_SGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", SGSTPayableCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", DB_SGST);
                objVoucherItem.setAttribute("REMARKS", "SGST");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            
            if (DB_IGST > 0) {
                VoucherSrNo++;
                clsVoucherItem objVoucherItem = new clsVoucherItem();
                objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                objVoucherItem.setAttribute("EFFECT", "C");
                objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", IGSTPayableCode);
                objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                objVoucherItem.setAttribute("AMOUNT", DB_IGST);
                objVoucherItem.setAttribute("REMARKS", "IGST");
                objVoucherItem.setAttribute("PO_NO", PONo);
                objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                objVoucherItem.setAttribute("GRN_NO", GRNNo);
                objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            }
            
            VoucherSrNo++;
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "D");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", DB_NET_AMOUNT);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            
            objVoucher.DoNotValidateAccounts = true;
            
            if (objVoucher.Insert()) {
                
                //SJ Posted. Automatically adjust advance receipt amount
                String DNNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=0 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + DNNo + "'", FinanceGlobal.FinURL);
                System.out.println("Purchase DebitNote  Posted " + DNNo);
                return true;
            } else {
                LastError = objVoucher.LastError;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean PostDNHSNGST(int CompanyID, String GRNNo, String GRNDate) {
        try {
            //Get Object
            
            System.out.println("SSSS 0");
            System.out.println("GRNN0  " + GRNNo);
            System.out.println("GRNDate  " + GRNDate);
            
            clsGRNGen objGRNGen = (clsGRNGen) (new clsGRNGen()).getObject(CompanyID, GRNNo, GRNDate);
            
            String item = "";
            String ItemGroup = "";
            String GRNAccountCode = "";
            String HSNSAC_CODE = "";
            String ITEM_DESC = "";
            double DB_INVOICE_AMOUNT = 0;
            double DB_CGST = 0;
            double DB_SGST = 0;
            double DB_IGST = 0;
            double DB_RCM = 0;
            double DB_COMPOSITION = 0;
            double DB_GST_COMPENSATION_CESS = 0;
            double DB_NET_AMOUNT = 0;
            
            clsVoucher objVoucher = new clsVoucher();
            
            String PartyCode = objGRNGen.getAttribute("SUPP_ID").getString();
            //String PartyMainCode=clsBook.getBookMainCode(CompanyID, objVoucher.getAttribute("BOOK_CODE").getString());
            String PartyMainCode = "125019";
            
            String PONo = data.getStringValueFromDB("SELECT PO_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND PO_NO<>'' ");
            String PODate = data.getStringValueFromDB("SELECT PO_DATE FROM D_PUR_PO_HEADER WHERE PO_NO='" + PONo + "'");
            String InvoiceNo = data.getStringValueFromDB("SELECT INVOICE_NO FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND INVOICE_NO<>'' ");
            String InvoiceDate = data.getStringValueFromDB("SELECT INVOICE_DATE FROM D_INV_GRN_HEADER WHERE GRN_NO='" + GRNNo + "' AND INVOICE_NO<>'' ");
            
            String SelPrefix = "";
            String SelSuffix = "";
            int FFNo = 0;
            
            System.out.println("SSSS 1");
            //****** Prepare Voucher Object ********//
            
            //  setAttribute("FIN_HIERARCHY_ID",0);
            setAttribute("FIN_HIERARCHY_ID", 1796); //FOR TEST SERVER
            //  setAttribute("FIN_HIERARCHY_ID",1668); // FOR LIVE
            
            //(1) Select the Hierarchy
            HashMap List = clsApprovalRules.getApprovalRules(EITLERPGLOBAL.gCompanyID, clsVoucher.DebitNoteModuleID, "CHOOSE_HIERARCHY", "PUR_DN_AUTO", "");
            
            if (List.size() > 0) {
                //Get the Result of the Rule which would be the hierarchy no.
                clsApprovalRules objRule = (clsApprovalRules) List.get(Integer.toString(1));
                int HierarchyID = UtilFunctions.CInt(objRule.getAttribute("RULE_OUTCOME").getString());
                System.out.println("HID 1 1" + HierarchyID);
                setAttribute("FIN_HIERARCHY_ID", HierarchyID);
            }
            
            ResultSet rsVoucher = data.getResult("SELECT * FROM D_COM_FIRSTFREE WHERE MODULE_ID=" + clsVoucher.DebitNoteModuleID);
            rsVoucher.first();
            
            if (rsVoucher.getRow() > 0) {
                SelPrefix = rsVoucher.getString("PREFIX_CHARS");
                SelSuffix = rsVoucher.getString("SUFFIX_CHARS");
                FFNo = rsVoucher.getInt("FIRSTFREE_NO");
            }
            
            int VoucherSrNo = 0;
            
            objVoucher = new clsVoucher();
            objVoucher.LoadDataEx(EITLERPGLOBAL.gCompanyID);
            
            objVoucher.setAttribute("PREFIX", SelPrefix);
            objVoucher.setAttribute("SUFFIX", SelSuffix);
            objVoucher.setAttribute("FFNO", FFNo);
            objVoucher.setAttribute("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
            objVoucher.setAttribute("VOUCHER_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            
            //SUITING
            objVoucher.setAttribute("BOOK_CODE", "45");
            //GRNAccountCode="405021";
            
            objVoucher.setAttribute("VOUCHER_TYPE", FinanceGlobal.TYPE_DEBIT_NOTE);
            objVoucher.setAttribute("CHEQUE_NO", "");
            objVoucher.setAttribute("CHEQUE_DATE", "0000-00-00");
            objVoucher.setAttribute("BANK_NAME", "");
            objVoucher.setAttribute("PO_NO", "");
            objVoucher.setAttribute("PO_DATE", "0000-00-00");
            objVoucher.setAttribute("INVOICE_NO", "");
            objVoucher.setAttribute("INVOICE_DATE", "0000-00-00");
            objVoucher.setAttribute("GRN_NO", "");
            objVoucher.setAttribute("GRN_DATE", "0000-00-00");
            objVoucher.setAttribute("ST_CATEGORY", "");
            objVoucher.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
            objVoucher.setAttribute("REMARKS", "");
            
            objVoucher.setAttribute("HIERARCHY_ID", getAttribute("FIN_HIERARCHY_ID").getInt());
            int FirstUserID = data.getIntValueFromDB("SELECT USER_ID FROM D_COM_HIERARCHY_RIGHTS WHERE HIERARCHY_ID=" + getAttribute("FIN_HIERARCHY_ID").getInt() + " AND SR_NO=1");
            System.out.println("Hierarchy ID = " + getAttribute("FIN_HIERARCHY_ID").getInt());
            System.out.println("First user ID = " + FirstUserID);
            
            objVoucher.setAttribute("FROM", FirstUserID);
            objVoucher.setAttribute("TO", FirstUserID);
            objVoucher.setAttribute("FROM_REMARKS", "");
            objVoucher.setAttribute("APPROVAL_STATUS", "H"); //Final Approve
            
            objVoucher.colVoucherItems.clear();
            
            String strSQLHSN = "";
            strSQLHSN = "SELECT * FROM D_INV_GRN_HSN WHERE GRN_NO='" + GRNNo + "' AND APPROVED=1 AND CANCELLED=0 ";
            
            ResultSet rsHSNData = data.getResult(strSQLHSN);
            rsHSNData.first();
            
            if (rsHSNData.getRow() > 0) {
                while (!rsHSNData.isAfterLast()) {
                    
                    HSNSAC_CODE = rsHSNData.getString("HSN_CODE");
                    
                    System.out.println("HSN CODE " + rsHSNData.getString("HSN_CODE"));
                    
                    DB_INVOICE_AMOUNT = rsHSNData.getDouble("DEBIT_NOTE_AMOUNT");
                    DB_CGST = rsHSNData.getDouble("DEBIT_NOTE_CGST");
                    DB_SGST = rsHSNData.getDouble("DEBIT_NOTE_SGST");
                    DB_IGST = rsHSNData.getDouble("DEBIT_NOTE_IGST");
                    DB_RCM = rsHSNData.getDouble("DEBIT_NOTE_RCM");
                    DB_COMPOSITION = rsHSNData.getDouble("DEBIT_NOTE_COMPOSITION");
                    DB_GST_COMPENSATION_CESS = rsHSNData.getDouble("DEBIT_NOTE_GST_COMP_CESS");
                    
                    DB_NET_AMOUNT += DB_INVOICE_AMOUNT + DB_CGST + DB_SGST + DB_IGST + DB_RCM + DB_COMPOSITION + DB_GST_COMPENSATION_CESS;
                    
                    String sql1 = "SELECT DISTINCT COLUMN_5_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ";
                    String sql2 = "SELECT DISTINCT COLUMN_4_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ";
                    String sql3 = "SELECT DISTINCT COLUMN_3_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ";
                    
                    System.out.println(sql1);
                    System.out.println(sql2);
                    System.out.println(sql3);
                    
                    double IGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_5_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ");
                    double SGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_4_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ");
                    double CGSTPER = data.getDoubleValueFromDB("SELECT DISTINCT COLUMN_3_PER  FROM D_INV_GRN_DETAIL WHERE GRN_NO ='" + GRNNo + "' ");
                    
                    String IGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='IGST' AND GST_PERCENT =" + EITLERPGLOBAL.round(IGSTPER, 0));
                    String SGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='SGST' AND GST_PERCENT =" + EITLERPGLOBAL.round(SGSTPER, 0));
                    String CGSTPayableCode = data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM FINANCE.D_FIN_GL WHERE GST_CATG ='CGST' AND GST_PERCENT =" + EITLERPGLOBAL.round(CGSTPER, 0));
                    
                    System.out.println("IGST PER " + IGSTPER);
                    System.out.println("SGST PER " + SGSTPER);
                    System.out.println("CGST PER " + CGSTPER);
                    System.out.println("IGST PAYABLE ACCOUNT : " + IGSTPayableCode);
                    System.out.println("SGST PAYABLE ACCOUNT : " + SGSTPayableCode);
                    System.out.println("CGST PAYABLE ACCOUNT : " + CGSTPayableCode);
                    
                    if (IGSTPayableCode.equals("")) {
                        IGSTPayableCode = "<IGST Payable Code>";
                    }
                    if (SGSTPayableCode.equals("")) {
                        SGSTPayableCode = "<SGST Payable Code>";
                    }
                    if (CGSTPayableCode.equals("")) {
                        CGSTPayableCode = "<CGST Payable Code>";
                    }
                    
                    System.out.println("PO NO " + PONo);
                    System.out.println("PO DATE " + PODate);
                    System.out.println("INVOICE NO " + InvoiceNo);
                    System.out.println("INVOICE DATE " + InvoiceDate);
                    
                    System.out.println("DB INVOICE AMT = " + DB_INVOICE_AMOUNT);
                    System.out.println("DB CGST = " + DB_CGST);
                    System.out.println("DB SGST = " + DB_SGST);
                    System.out.println("DB IGST = " + DB_IGST);
                    System.out.println("DB RCM = " + DB_RCM);
                    System.out.println("DB COMPOSITION = " + DB_COMPOSITION);
                    System.out.println("DB GST_COMPENSATION_CESS = " + DB_GST_COMPENSATION_CESS);
                    System.out.println("DB NET AMOUNT = " + DB_NET_AMOUNT);
                    
                    String strSQL = "";
                    
                    strSQL = "SELECT DISTINCT(ITEM_CODE) ";
                    strSQL += "FROM ";
                    strSQL += "D_FIN_ITEM_CODE_MAPPING A, ";
                    strSQL += EITLERPGLOBAL.DBName + ".D_INV_GRN_DETAIL B ";
                    strSQL += "WHERE ";
                    strSQL += "B.GRN_NO='" + GRNNo + "' AND B.GRN_TYPE=1 AND B.HSN_SAC_CODE='" + HSNSAC_CODE + "' AND ";
                    strSQL += "SUBSTRING(B.ITEM_ID,1,LENGTH(A.ITEM_CODE))=A.ITEM_CODE AND A.COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ";
                    
                    System.out.println("Mapping Sql : " + strSQL);
                    
                    ResultSet rsItems = data.getResult(strSQL, FinanceGlobal.FinURL);
                    rsItems.first();
                    
                    if (rsItems.getRow() > 0) {
                        while (!rsItems.isAfterLast()) {
                            //Get the Group
                            ItemGroup = rsItems.getString("ITEM_CODE");
                            item = data.getStringValueFromDB("SELECT DISTINCT ITEM_ID  FROM D_INV_GRN_DETAIL WHERE GRN_NO='" + GRNNo + "' AND HSN_SAC_CODE='" + HSNSAC_CODE + "'");
                            ITEM_DESC = clsItem.getItemName(EITLERPGLOBAL.gCompanyID, item);
                            
                            GRNAccountCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, ItemGroup);
                            
                            System.out.println("GRN ACCOUNT CODE1 " + GRNAccountCode);
                            
                            rsItems.next();
                        }
                    }
                    
                    if (GRNAccountCode.equals("")) {
                        
                        GRNAccountCode = clsTRMapping.getMainCodeFromItemCode(EITLERPGLOBAL.gCompanyID, "DEFAULT");
                        System.out.println("GRN ACCOUNT CODE default " + GRNAccountCode);
                    }
                    
                    System.out.println("GRN ACCOUNT CODE2 " + GRNAccountCode);
                    
                    //(1) Entry no.1 => Debit party with net amount
                    //(1) Sales Account
                    if (DB_INVOICE_AMOUNT > 0) {
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", GRNAccountCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", DB_INVOICE_AMOUNT);
                        objVoucherItem.setAttribute("REMARKS", "");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }
                    
                    //(2) Hundi Charges
                    if (DB_RCM > 0) {
                        
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450227");
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", DB_RCM);
                        objVoucherItem.setAttribute("REMARKS", "RCM");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                        
                    }
                    
                    //(3) Insurance Charges
                    if (DB_COMPOSITION > 0) {
                        
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "427027");
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", DB_COMPOSITION);
                        objVoucherItem.setAttribute("REMARKS", "COMPOSITION");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                        
                    }
                    
                    //(4) Bank Charges
                    if (DB_GST_COMPENSATION_CESS > 0) {
                        
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", "450038");
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", DB_GST_COMPENSATION_CESS);
                        objVoucherItem.setAttribute("REMARKS", "GST Compensation cess");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                        
                    }
                    
                    //(10) GST Amount
                    if (DB_CGST > 0) {
                        
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", CGSTPayableCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", DB_CGST);
                        objVoucherItem.setAttribute("REMARKS", "CGST");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }
                    
                    if (DB_SGST > 0) {
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", SGSTPayableCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", DB_SGST);
                        objVoucherItem.setAttribute("REMARKS", "SGST");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }
                    
                    if (DB_IGST > 0) {
                        VoucherSrNo++;
                        clsVoucherItem objVoucherItem = new clsVoucherItem();
                        objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
                        objVoucherItem.setAttribute("EFFECT", "C");
                        objVoucherItem.setAttribute("ACCOUNT_ID", 1);
                        objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", IGSTPayableCode);
                        objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", "");
                        objVoucherItem.setAttribute("AMOUNT", DB_IGST);
                        objVoucherItem.setAttribute("REMARKS", "IGST");
                        objVoucherItem.setAttribute("PO_NO", PONo);
                        objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
                        objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
                        objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
                        objVoucherItem.setAttribute("INVOICE_AMOUNT", 0);
                        objVoucherItem.setAttribute("GRN_NO", GRNNo);
                        objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
                        objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
                        objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
                        objVoucherItem.setAttribute("HSN_SAC_CODE", HSNSAC_CODE);
                        objVoucherItem.setAttribute("ITEM_DESCRIPTION", ITEM_DESC);
                        objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
                    }
                    
                    rsHSNData.next();
                }
            }
            
            //.getAttribute("DB_GROSS_AMT").getVal()
            // double DB_INVOICE_AMOUNT=objGRNGen.getAttribute("DB_INVOICE_AMT").getVal();
            VoucherSrNo++;
            clsVoucherItem objVoucherItem = new clsVoucherItem();
            objVoucherItem.setAttribute("SR_NO", VoucherSrNo);
            objVoucherItem.setAttribute("EFFECT", "D");
            objVoucherItem.setAttribute("ACCOUNT_ID", 1);
            objVoucherItem.setAttribute("MAIN_ACCOUNT_CODE", PartyMainCode);
            objVoucherItem.setAttribute("SUB_ACCOUNT_CODE", PartyCode);
            objVoucherItem.setAttribute("AMOUNT", DB_NET_AMOUNT);
            objVoucherItem.setAttribute("REMARKS", "");
            objVoucherItem.setAttribute("PO_NO", "");
            objVoucherItem.setAttribute("PO_NO", PONo);
            objVoucherItem.setAttribute("PO_DATE", EITLERPGLOBAL.formatDate(PODate));
            objVoucherItem.setAttribute("INVOICE_NO", InvoiceNo);
            objVoucherItem.setAttribute("INVOICE_DATE", EITLERPGLOBAL.formatDate(InvoiceDate));
            objVoucherItem.setAttribute("GRN_NO", GRNNo);
            objVoucherItem.setAttribute("GRN_DATE", EITLERPGLOBAL.formatDate(GRNDate));
            objVoucherItem.setAttribute("MODULE_ID", clsVoucher.DebitNoteModuleID);
            objVoucherItem.setAttribute("REF_COMPANY_ID", CompanyID);
            objVoucher.colVoucherItems.put(Integer.toString(objVoucher.colVoucherItems.size() + 1), objVoucherItem);
            
            objVoucher.DoNotValidateAccounts = true;
            
            if (objVoucher.Insert()) {
                
                //SJ Posted. Automatically adjust advance receipt amount
                String DNNo = objVoucher.getAttribute("VOUCHER_NO").getString();
                data.Execute("UPDATE D_FIN_VOUCHER_HEADER SET APPROVED=0 AND APPROVED_DATE=CURDATE() WHERE VOUCHER_NO='" + DNNo + "'", FinanceGlobal.FinURL);
                System.out.println("Purchase DebitNote  Posted " + DNNo);
                return true;
            } else {
                LastError = objVoucher.LastError;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Object getObject(int pCompanyID, String pReqNo, String pReqDate) {
        String strCondition = " WHERE COMPANY_ID=" + Long.toString(pCompanyID) + " AND GRN_NO='" + pReqNo + "' AND GRN_DATE='" + pReqDate + "'";
        clsGRNGen objGRNGen = new clsGRNGen();
        objGRNGen.Filter(strCondition, pCompanyID);
        return objGRNGen;
    }
    
}
