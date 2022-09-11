/*
 * clsTempLot.java
 *
 * Created on October 7, 2009, 5:25 PM
 */

package EITLERP.Stores;

import EITLERP.*;
import java.sql.*;
import EITLERP.Finance.UtilFunctions;

/**
 *
 * @author  root
 */
public class clsTempLot {
    
    /** Creates a new instance of clsTempLot */
    public clsTempLot() {
    }
    
    public static void Insert(String ItemID) {
        try {
            data.Execute("DELETE FROM D_INV_TEMP_LOT WHERE ITEM_ID='"+ItemID+"' AND COMPANY_ID='"+EITLERPGLOBAL.gCompanyID+"' ");
            String SQL = "SELECT ITEM_LOT_NO, AUTO_LOT_NO, ITEM_ID FROM D_INV_GRN_LOT WHERE ITEM_ID='"+ItemID+"' " +
            "AND COMPANY_ID='"+EITLERPGLOBAL.gCompanyID+"' AND AUTO_LOT_NO<>'' AND LOT_CLOSE<>1 AND APPROVED=1 AND CANCELLED=0";
            
            ResultSet rsData = data.getResult(SQL);
            rsData.first();
            Connection conn = data.getConn();
            
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsResultSet = stmt.executeQuery("SELECT * FROM D_INV_TEMP_LOT WHERE SR_NO=1");
            if(rsData.getRow()>0) {
                while(!rsData.isAfterLast()) {
                    String AutoLotNo = UtilFunctions.getString(rsData, "AUTO_LOT_NO", "");
                    String ItemLotNo = UtilFunctions.getString(rsData, "ITEM_LOT_NO", "");
                    //double BalanceQty = clsTempLot.getLOTBalanceQTY(AutoLotNo,ItemLotNo);
                    
                    double BalanceQty = clsTempLot.getLOTBalanceQTYItemWise(AutoLotNo,ItemID);
                    
                    if(BalanceQty > 0 ) {
                        int SrNo = (int) data.getMaxID(EITLERPGLOBAL.gCompanyID, "D_INV_TEMP_LOT", "SR_NO");
                        rsResultSet.moveToInsertRow();
                        rsResultSet.updateInt("SR_NO", SrNo);
                        rsResultSet.updateInt("COMPANY_ID", EITLERPGLOBAL.gCompanyID);
                        rsResultSet.updateString("ITEM_ID", ItemID);
                        rsResultSet.updateString("ITEM_LOT_NO", UtilFunctions.getString(rsData, "ITEM_LOT_NO", ""));
                        rsResultSet.updateString("AUTO_LOT_NO", UtilFunctions.getString(rsData, "AUTO_LOT_NO", ""));
                        rsResultSet.updateDouble("BALANCE_QTY", BalanceQty);
                        rsResultSet.insertRow();
                    }
                    rsData.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static double getLOTBalanceQTYItemWise(String aLotNo, String ItemID) {
        double BalanceQty = 0;
        double IssuedQty = 0;
        double AcceptedQty = 0;
        try {
            String SQL = "SELECT SUM(ISSUED_LOT_QTY) FROM D_INV_ISSUE_REQ_LOT WHERE AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' AND CANCELLED=0";
            IssuedQty = EITLERPGLOBAL.round(IssuedQty + data.getDoubleValueFromDB(SQL), 3);
            SQL = "SELECT SUM(ISSUED_LOT_QTY) FROM D_INV_STM_LOT WHERE AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' AND CANCELLED=0";
            IssuedQty = EITLERPGLOBAL.round(IssuedQty + data.getDoubleValueFromDB(SQL), 3);
            
            SQL = "SELECT LOT_ACCEPTED_QTY FROM D_INV_GRN_LOT WHERE AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' AND CANCELLED=0";
            AcceptedQty = EITLERPGLOBAL.round(AcceptedQty + data.getDoubleValueFromDB(SQL), 3);
            
            BalanceQty = EITLERPGLOBAL.round(AcceptedQty - IssuedQty, 3);
            data.Execute("UPDATE D_INV_GRN_LOT SET BALANCE_QTY="+BalanceQty+ ", CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
            "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");
            
            data.Execute("UPDATE D_INV_GRN_LOT_H SET BALANCE_QTY="+BalanceQty+ ", CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
            "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");

            if(BalanceQty <= 0 ) {
                data.Execute("UPDATE D_INV_GRN_LOT SET LOT_CLOSE=1, CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
                "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");
                
                data.Execute("UPDATE D_INV_GRN_LOT_H SET LOT_CLOSE=1, CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
                "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");
            }
        } catch(Exception e) {
            return BalanceQty;
        }
        return BalanceQty;
    }
    
    
    public static double getLOTActualBalanceQTYItemWise(String aLotNo, String ItemID) {
        double BalanceQty = 0;
        double IssuedQty = 0;
        double AcceptedQty = 0;
        try {
            String SQL = "SELECT SUM(B.ISSUED_LOT_QTY) FROM D_INV_ISSUE_HEADER A, D_INV_ISSUE_LOT B WHERE A.ISSUE_NO = B.ISSUE_NO AND A.APPROVED = 1 AND A.CANCELED=0 AND B.AUTO_LOT_NO='"+aLotNo+"' AND B.ITEM_ID='"+ItemID+"' ";
            IssuedQty = EITLERPGLOBAL.round(IssuedQty + data.getDoubleValueFromDB(SQL), 3);
            SQL = "SELECT SUM(B.ISSUED_LOT_QTY) FROM D_INV_STM_HEADER A,D_INV_STM_LOT B WHERE A.STM_NO = B.STM_NO AND A.APPROVED = 1 AND B.AUTO_LOT_NO='"+aLotNo+"' AND B.ITEM_ID='"+ItemID+"' AND A.CANCELLED=0";
            IssuedQty = EITLERPGLOBAL.round(IssuedQty + data.getDoubleValueFromDB(SQL), 3);
            
            SQL = "SELECT B.LOT_ACCEPTED_QTY FROM D_INV_GRN_HEADER A,D_INV_GRN_LOT B WHERE A.GRN_NO = B.GRN_NO AND A.APPROVED = 1 AND B.AUTO_LOT_NO='"+aLotNo+"' AND B.ITEM_ID='"+ItemID+"' AND A.CANCELLED=0";
            AcceptedQty = EITLERPGLOBAL.round(AcceptedQty + data.getDoubleValueFromDB(SQL), 3);
            
            BalanceQty = EITLERPGLOBAL.round(AcceptedQty - IssuedQty, 3);
            //data.Execute("UPDATE D_INV_GRN_LOT SET BALANCE_QTY="+BalanceQty+ ", CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
            //"AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");
            
            //data.Execute("UPDATE D_INV_GRN_LOT_H SET BALANCE_QTY="+BalanceQty+ ", CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
            //"AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");

            /*if(BalanceQty <= 0 ) {
                data.Execute("UPDATE D_INV_GRN_LOT SET LOT_CLOSE=1, CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
                "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");
                
                data.Execute("UPDATE D_INV_GRN_LOT_H SET LOT_CLOSE=1, CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
                "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_ID='"+ItemID+"' ");
            }*/
        } catch(Exception e) {
            return BalanceQty;
        }
        return BalanceQty;
    }
    
    public static double getLOTBalanceQTY(String aLotNo, String iLotNo) {
        double BalanceQty = 0;
        double IssuedQty = 0;
        double AcceptedQty = 0;
        try {
            String SQL = "SELECT SUM(ISSUED_LOT_QTY) FROM D_INV_ISSUE_REQ_LOT WHERE AUTO_LOT_NO='"+aLotNo+"' AND ITEM_LOT_NO='"+iLotNo+"' AND CANCELLED=0";
            IssuedQty = EITLERPGLOBAL.round(IssuedQty + data.getDoubleValueFromDB(SQL), 3);
            SQL = "SELECT SUM(ISSUED_LOT_QTY) FROM D_INV_STM_LOT WHERE AUTO_LOT_NO='"+aLotNo+"' AND ITEM_LOT_NO='"+iLotNo+"' AND CANCELLED=0";
            IssuedQty = EITLERPGLOBAL.round(IssuedQty + data.getDoubleValueFromDB(SQL), 3);
            
            SQL = "SELECT LOT_ACCEPTED_QTY FROM D_INV_GRN_LOT WHERE AUTO_LOT_NO='"+aLotNo+"' AND ITEM_LOT_NO='"+iLotNo+"' AND CANCELLED=0";
            AcceptedQty = EITLERPGLOBAL.round(AcceptedQty + data.getDoubleValueFromDB(SQL), 3);
            
            BalanceQty = EITLERPGLOBAL.round(AcceptedQty - IssuedQty, 3);
            data.Execute("UPDATE D_INV_GRN_LOT SET BALANCE_QTY="+BalanceQty+ ", CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
            "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_LOT_NO='"+iLotNo+"' ");
            
            data.Execute("UPDATE D_INV_GRN_LOT_H SET BALANCE_QTY="+BalanceQty+ ", CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
            "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_LOT_NO='"+iLotNo+"' ");

            if(BalanceQty <= 0 ) {
                data.Execute("UPDATE D_INV_GRN_LOT SET LOT_CLOSE=1, CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
                "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_LOT_NO='"+iLotNo+"' ");
                
                data.Execute("UPDATE D_INV_GRN_LOT_H SET LOT_CLOSE=1, CHANGED=1, CHANGED_DATE=CURDATE() WHERE " +
                "AUTO_LOT_NO='"+aLotNo+"' AND ITEM_LOT_NO='"+iLotNo+"' ");
            }
        } catch(Exception e) {
            return BalanceQty;
        }
        return BalanceQty;
    }
}
