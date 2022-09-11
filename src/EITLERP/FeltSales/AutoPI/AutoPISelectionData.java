/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.AutoPI;

import EITLERP.clsFirstFree;
import EITLERP.data;
import java.sql.*;
import java.util.*;

/**
 *
 * @author root
 */
public class AutoPISelectionData {

    /**
     * @param args the command line arguments
     */
    
    private static EITLERP.FeltSales.common.FeltInvCalc inv_calculation;
    public static void main(String[] args) {
        // TODO code application logic here
        Auto_PI_Data();
    }

    private static void Auto_PI_Data() {
        String sql="",pDocNo="",pDocDate="",SubDate="";
        
        try {
//            SubDate = data.getStringValueFromDB("SELECT SUBDATE(CURDATE(), INTERVAL 90 DAY) FROM DUAL");
            SubDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
            
            Connection Conn1;
            Statement stmt1;
            ResultSet rsData1;
            Conn1 = data.getConn();
            stmt1 = Conn1.createStatement();
            rsData1 = stmt1.executeQuery("SELECT * FROM PRODUCTION.FELT_PROD_DATA FF, DINESHMILLS.D_SAL_PARTY_MASTER PM, PRODUCTION.FELT_SALES_PIECE_REGISTER SP "
                    + "WHERE PM.PARTY_CODE=FF.PROD_PARTY_CODE AND PM.CHARGE_CODE IN ('08','09') AND COALESCE(PM.INCHARGE_CD,0)!=6  "
                    + "AND FF.PROD_PIECE_NO=SP.PR_PIECE_NO "
                    + "AND FF.PROD_DEPT='FELT FINISHING' AND FF.FINAL_APPROVAL_DATE='"+SubDate+"' ");
            rsData1.first();
            
            if (rsData1.getRow() > 0) {
                pDocNo = clsFirstFree.getNextFreeNo(2, 630, 305, true);
                pDocDate = data.getStringValueFromDB("SELECT CURDATE() FROM DUAL");
            
                while (!rsData1.isAfterLast()) {
                    
                    String pPieceNo = rsData1.getString("PR_PIECE_NO");
                    String pPartyCode = rsData1.getString("PROD_PARTY_CODE");
                    String pPartyName = rsData1.getString("PARTY_NAME");
                    String plength = rsData1.getString("PR_BILL_LENGTH");
                    String pwidth = rsData1.getString("PR_BILL_WIDTH");
                    String pgsm = rsData1.getString("PR_BILL_GSM");
                    String pweight = rsData1.getString("PR_BILL_WEIGHT");
                    String psqmtr = rsData1.getString("PR_BILL_SQMTR");
                    String pproductcode = rsData1.getString("PR_BILL_PRODUCT_CODE");
                    
                    inv_calculation = EITLERP.FeltSales.common.clsOrderValueCalc.calculate(pPieceNo, pproductcode, rsData1.getString("PROD_PARTY_CODE"), Float.parseFloat(plength), Float.parseFloat(pwidth), Float.parseFloat(pweight), Float.parseFloat(psqmtr), pDocDate, "");

                    sql = "INSERT INTO PRODUCTION.FELT_AUTO_PI_DATA "                            
                            + "(DOC_NO,DOC_DATE,PI_SELECTED_FLAG,PIECE_NO,PARTY_CODE,PARTY_NAME,PRODUCT_CODE,LENGTH,WIDTH,WEIGHT,"
                            + "PI_VALUE,PI_GENERATED_FLAG,PI_MAILED_FLAG,REMARKS,FINAL_APPROVAL_DATE,"
                            + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,"
                            + "HIERARCHY_ID,CHANGED,CHANGED_DATE) "
                            + "VALUES('" + pDocNo + "','" + pDocDate + "','0','" + pPieceNo + "','" + pPartyCode + "','" + pPartyName + "','" + pproductcode + "'," + plength + "," + pwidth + "," + pweight + ","
                            + "" + inv_calculation.getFicInvAmt() + ",'0','0','AUTO GENERATED PI SELECTION DATA','0000-00-00',"
                            + "333,'" + pDocDate + "',NULL,NULL,0,'0000-00-00',0,'0000-00-00',0,"
                            + "3799,1,'" + pDocDate + "')";
                    System.out.println("Insert Into Auto PI Data :" + sql);
                    data.Execute(sql);
                    
                    sql = "INSERT INTO PRODUCTION.FELT_AUTO_PI_DATA_H "                            
                            + "(REVISION_NO,UPDATED_BY,ENTRY_DATE,APPROVAL_STATUS,APPROVER_REMARKS,"
                            + "DOC_NO,DOC_DATE,PI_SELECTED_FLAG,PIECE_NO,PARTY_CODE,PARTY_NAME,PRODUCT_CODE,LENGTH,WIDTH,WEIGHT,"
                            + "PI_VALUE,PI_GENERATED_FLAG,PI_MAILED_FLAG,REMARKS,FINAL_APPROVAL_DATE,"
                            + "CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,APPROVED,APPROVED_DATE,REJECTED,REJECTED_DATE,CANCELED,"
                            + "HIERARCHY_ID,CHANGED,CHANGED_DATE) "
                            + "VALUES (1,333,'" + pDocDate + "','W','',"
                            + "'" + pDocNo + "','" + pDocDate + "','0','" + pPieceNo + "','" + pPartyCode + "','" + pPartyName + "','" + pproductcode + "'," + plength + "," + pwidth + "," + pweight + ","
                            + "" + inv_calculation.getFicInvAmt() + ",'0','0','AUTO GENERATED PI SELECTION DATA','0000-00-00',"
                            + "333,'" + pDocDate + "',NULL,NULL,0,'0000-00-00',0,'0000-00-00',0,"
                            + "3799,1,'" + pDocDate + "')";
                    System.out.println("Insert Into History of Auto PI Data :" + sql);
                    data.Execute(sql);
                    
                    sql = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA "
                            + "(MODULE_ID,DOC_NO,DOC_DATE,USER_ID,STATUS,TYPE,REMARKS,SR_NO,FROM_USER_ID,FROM_REMARKS,RECEIVED_DATE,ACTION_DATE,CHANGED,CHANGED_DATE) "
                            + "VALUES(630,'" + pDocNo + "','" + pDocDate + "',333,'W','C','ERP SYSTEM AUTO GENERATED',1,0,NULL,'" + pDocDate + "','0000-00-00',1,'" + pDocDate + "')";
                    System.out.println("Insert Into Felt Prod Doc Data :" + sql);
                    data.Execute(sql);
                    
                    rsData1.next();
                }
            }
        } catch (Exception e) {
            System.out.println("Error while Saving : " + e.getMessage());
        }
    }
}
