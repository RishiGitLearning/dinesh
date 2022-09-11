/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.clsOrderValueCalc;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author root
 */
public class clsRegisterUpdation {
    
    public static void main(String[] args) {
        InvoiceValueUpdation();
        runQuery();
        InvoiceValueUpdationWithoutDiscount();
    }
    private static void InvoiceValueUpdation()
    {
        clsOrderValueCalc calc = new clsOrderValueCalc();
        EITLERP.FeltSales.common.FeltInvCalc InvObj ;
        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_STAGE NOT IN ('INVOICED','DIVERTED','EXP-INVOICED','DIVIDED','EXP-INVOICE','CANCELED') ";
        System.out.println("SQL = " + SQL);
        try {
            Connection connection;
            Statement statement;
            ResultSet resultSet,rsHeader;
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            
            while (resultSet.next()) {
                 
                 try{ 
                     String PieceNo = resultSet.getString("PR_PIECE_NO");
                     String Product_Code = resultSet.getString("PR_BILL_PRODUCT_CODE");
                     String Piece_Stage = resultSet.getString("PR_PIECE_STAGE"); 
                     String Party_Code = resultSet.getString("PR_PARTY_CODE");
                     Float Length = resultSet.getFloat("PR_BILL_LENGTH");
                     Float Width = resultSet.getFloat("PR_BILL_WIDTH");
                     Float Weight = resultSet.getFloat("PR_BILL_WEIGHT");
                     Float SQMT = resultSet.getFloat("PR_BILL_SQMTR");
                     String CurDate = EITLERPGLOBAL.getCurrentDateDB();
                     String baleNo = resultSet.getString("PR_BALE_NO");
                     String baleDate = resultSet.getString("PR_PACKED_DATE");
                     
                     
                     if(Piece_Stage.equals("BSR"))
                     {
                        InvObj = clsOrderValueCalc.calculate(PieceNo, Product_Code, Party_Code, Length, Width, Weight, SQMT, CurDate, baleNo, baleDate);
                         System.out.println("PIECE NO : "+PieceNo+", STAGE : "+Piece_Stage+", BSR");
                     }
                     else
                     {
                         InvObj = clsOrderValueCalc.calculateWithoutGSTINNO(PieceNo, Product_Code, Party_Code, Length, Width, Weight, SQMT, CurDate);
                         System.out.println("PIECE NO : "+PieceNo+", STAGE : "+Piece_Stage+", Non BSR");
                     }
                     
                     float GST = InvObj.getFicGST();
                     float Inv_Amt = InvObj.getFicInvAmt();
                     float INVAMT_WITHOUT_GST = Inv_Amt - GST;
                     float BaseAmt = InvObj.getFicBasAmount();
                     System.out.println("Piece No : "+PieceNo+" BASE AMT : "+BaseAmt+", Inv Amt without GST "+INVAMT_WITHOUT_GST+", GST : "+GST+", Invoice Amt : "+Inv_Amt);
                     data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_FELT_VALUE_WITH_GST='"+Inv_Amt+"',PR_FELT_VALUE_WITHOUT_GST='"+INVAMT_WITHOUT_GST+"',PR_FELT_BASE_VALUE='"+BaseAmt+"' WHERE PR_PIECE_NO='"+PieceNo+"'");
                        
                 }catch(Exception e)
                 {
                     e.printStackTrace();
                 }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private static void runQuery()
    {
        
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER PR," +
                " " +
                " (SELECT" +
                " A.INVOICE_NO,A.INVOICE_DATE,PIECE_NO,A.PARTY_CODE,A.PARTY_NAME,A.TOTAL_NET_AMOUNT,C.VALUE_DATE AS V_DATE ,COALESCE(C.AMOUNT,0) AS AMOUNT," +
                " (TOTAL_NET_AMOUNT - COALESCE(C.AMOUNT,0)) AS SHORT_AMT,VOUCHER,1 AS M  FROM" +
                " " +
                " " +
                " ( SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE) AS" +
                " UID,INVOICE_NO,INVOICE_DATE,PIECE_NO,PARTY_CODE,PARTY_NAME,INVOICE_AMT AS" +
                " TOTAL_NET_AMOUNT FROM" +
                " PRODUCTION.FELT_SAL_INVOICE_HEADER,PRODUCTION.FELT_SALES_PIECE_REGISTER  " +
                " " +
                " WHERE  COALESCE(PR_ACT_PAY_CHQRC_DATE,'0000-00-00') ='0000-00-00' AND" +
                " PR_PIECE_STAGE = 'INVOICED'" +
                " " +
                " AND CANCELLED=0 AND PR_PIECE_NO =PIECE_NO )  AS A  " +
                " " +
                " LEFT JOIN  " +
                " " +
                "  (" +
                " " +
                " SELECT CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) AS UID,SUM(AMOUNT)AS" +
                " AMOUNT,MAX(VALUE_DATE) AS VALUE_DATE,INVOICE_NO,INVOICE_DATE,EFFECT," +
                " " +
                " GROUP_CONCAT(DISTINCT B.VOUCHER_NO,' ( ',AMOUNT,' /" +
                " ',DATE_FORMAT(VALUE_DATE,'%d/%m/%Y'),' ) '  ORDER BY A.VOUCHER_NO SEPARATOR" +
                " ' , ' ) AS VOUCHER  " +
                " " +
                " FROM FINANCE.D_FIN_VOUCHER_DETAIL A,FINANCE.D_FIN_VOUCHER_HEADER B  " +
                " " +
                " WHERE A.VOUCHER_NO = B.VOUCHER_NO AND B.APPROVED =1 AND B.CANCELLED =0" +
                " " +
                " AND CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE) IN" +
                " " +
                " (SELECT CONCAT(INVOICE_NO,INVOICE_DATE,PARTY_CODE)" +
                " " +
                " FROM PRODUCTION.FELT_SAL_INVOICE_HEADER,PRODUCTION.FELT_SALES_PIECE_REGISTER" +
                " " +
                " " +
                " WHERE  COALESCE(PR_ACT_PAY_CHQRC_DATE,'0000-00-00') ='0000-00-00' AND" +
                " PR_PIECE_STAGE = 'INVOICED'" +
                " " +
                " AND CANCELLED=0 AND PR_PIECE_NO =PIECE_NO" +
                " " +
                " )  " +
                " " +
                " AND MAIN_ACCOUNT_CODE = 210010  AND SUBSTRING(B.VOUCHER_NO,1,2) !='SJ' GROUP" +
                " BY CONCAT(INVOICE_NO,INVOICE_DATE,SUB_ACCOUNT_CODE),INVOICE_NO,INVOICE_DATE" +
                " " +
                " " +
                " ) AS C" +
                " " +
                " ON A.UID=C.UID ) AS PAY" +
                " " +
                " SET PR_ACT_PAY_CHQRC_DATE = COALESCE(V_DATE,'0000-00-00') ," +
                " " +
                " PR_PAYMENT_RCVD_VOUCHER = COALESCE(VOUCHER,'')," +
                " " +
                " PR_PAYMENT_SHORT_AMOUNT = COALESCE(SHORT_AMT,'0.00')," +
                " " +
                " PR_PAYMENT_RCVD_AMOUNT = COALESCE(AMOUNT,'0.00')" +
                " " +
                " WHERE INVOICE_NO =PR_INVOICE_NO" +
                " " +
                " AND INVOICE_DATE = PR_INVOICE_DATE" +
                " " +
                "AND PIECE_NO = PR_PIECE_NO");
        
        
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_PAYMENT_SHORT_AMOUNT = 0 WHERE  PR_PAYMENT_SHORT_AMOUNT < 0");
        
        
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_PAYMENT_SHORT_AMOUNT = 0 " +
                    " WHERE  PR_PAYMENT_SHORT_AMOUNT > 0 " +
                    " " +
                    " AND COALESCE(PR_PAYMENT_RCVD_AMOUNT,0) =0 ");
        
        
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET  PR_ACT_PAY_CHQRC_DATE = " +
                    " '0000-00-00'  WHERE  PR_PAYMENT_SHORT_AMOUNT > 0 AND " +
                    " COALESCE(PR_PAYMENT_RCVD_VOUCHER,'') !=''");
        
        
        data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER PR, " +
                    " " +
                    " (SELECT PIECE_NO AS PRO_PIECE_NO,PARTY_CD AS " +
                    " PRO_PARTY_CODE,MIN(PROFORMA_DATE) AS PRO_PROFORMA_DATE,MAX(PROFORMA_DATE) AS " +
                    " MAX_PROFORMA_DATE,GROUP_CONCAT(D.PROFORMA_NO,' (',DATE_FORMAT(PROFORMA_DATE, " +
                    " '%d/%m/%Y'),') ') AS PRO_NO_DATE   " +
                    " " +
                    "                      FROM PRODUCTION.FELT_PROFORMA_INVOICE_DETAIL D, " +
                    " PRODUCTION.FELT_PROFORMA_INVOICE_HEADER H   " +
                    " " +
                    "                      WHERE H.PROFORMA_NO = D.PROFORMA_NO AND APPROVED =1 " +
                    " AND CANCELED =0 GROUP BY PIECE_NO,PARTY_CD) AS PI " +
                    " " +
                    " SET PR_ACT_PI_DATE = MAX_PROFORMA_DATE " +
                    " " +
                    " WHERE PR_PIECE_NO = PRO_PIECE_NO AND PRO_PARTY_CODE = PR_PARTY_CODE");
        
         data.Execute("CALL PRODUCTION.FELT_PIECE_REGISTER_DAILY_UPDATE()");
        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_BILL_STYLE=WIP_STYLE WHERE WIP_BILL_STYLE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_DATE = COALESCE(WIP_DATE,'0000-00-00') WHERE WIP_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_DATE = COALESCE(WIP_WARP_DATE,'0000-00-00') WHERE WIP_WARP_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_A_DATE = COALESCE(WIP_WARP_A_DATE,'0000-00-00') WHERE WIP_WARP_A_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_B_DATE = COALESCE(WIP_WARP_B_DATE,'0000-00-00') WHERE WIP_WARP_B_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_ORDER_DATE = COALESCE(WIP_ORDER_DATE,'0000-00-00') WHERE WIP_ORDER_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WVG_DATE ='0000-00-00' WHERE WIP_WVG_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WVG_A_DATE ='0000-00-00' WHERE WIP_WVG_A_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WVG_B_DATE ='0000-00-00' WHERE WIP_WVG_B_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_MND_DATE ='0000-00-00' WHERE WIP_MND_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_MND_A_DATE ='0000-00-00' WHERE WIP_MND_A_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_MND_B_DATE ='0000-00-00' WHERE WIP_MND_B_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_NDL_DATE ='0000-00-00' WHERE WIP_NDL_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_SPLICE_DATE ='0000-00-00' WHERE WIP_SPLICE_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_SEAM_DATE ='0000-00-00' WHERE  WIP_SEAM_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_WARP_EXECUTE_DATE ='0000-00-00' WHERE WIP_WARP_EXECUTE_DATE IS NULL");
//       
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OBSOLETE_DATE ='0000-00-00' WHERE  WIP_OBSOLETE_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER, DINESHMILLS.D_SAL_PARTY_MASTER SET WIP_INCHARGE = INCHARGE_CD WHERE PARTY_CODE = WIP_PARTY_CODE");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OBSOLETE_DATE ='0000-00-00' WHERE  WIP_OBSOLETE_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OA_DATE ='0000-00-00' WHERE  WIP_OA_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OA_DATE ='0000-00-00' WHERE  WIP_OA_DATE =''");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OC_DATE ='0000-00-00' WHERE  WIP_OC_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OC_LAST_DDMMYY ='0000-00-00' WHERE  WIP_OC_LAST_DDMMYY IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_CURRENT_SCH_LAST_DDMMYY ='0000-00-00' WHERE  WIP_CURRENT_SCH_LAST_DDMMYY IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_SP_LAST_DDMMYY ='0000-00-00' WHERE  WIP_SP_LAST_DDMMYY IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_SPL_REQUEST_DATE ='0000-00-00' WHERE  WIP_SPL_REQUEST_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_EXP_WIP_DELIVERY_DATE ='0000-00-00' WHERE  WIP_EXP_WIP_DELIVERY_DATE IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_REQ_MTH_LAST_DDMMYY ='0000-00-00' WHERE  WIP_REQ_MTH_LAST_DDMMYY IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OA_NO ='' WHERE  WIP_OA_NO IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OC_NO ='' WHERE  WIP_OC_NO IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OC_MONTHYEAR ='' WHERE  WIP_OC_MONTHYEAR IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_CURRENT_SCH_MONTH ='' WHERE  WIP_CURRENT_SCH_MONTH IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_SP_MONTHYEAR ='' WHERE  WIP_SP_MONTHYEAR IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_GIDC_STATUS ='' WHERE  WIP_GIDC_STATUS IS NULL");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_REQUESTED_MONTH ='' WHERE  WIP_REQUESTED_MONTH ='0'");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                            "SET WIP_OC_MONTHYEAR = PR_OC_MONTHYEAR, " +
//                            "WIP_OC_LAST_DDMMYY = PR_OC_LAST_DDMMYY, " +
//                            "WIP_OC_NO = PR_OC_NO, " +
//                            "WIP_OC_DATE = PR_OC_DATE" +
//                            "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                            "AND PR_OC_MONTHYEAR != WIP_OC_MONTHYEAR " +
//                            "AND PR_OC_MONTHYEAR != ''");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                            "SET WIP_OA_NO = PR_OA_NO, " +
//                            "WIP_OA_DATE = PR_OA_DATE " +
//                            "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                            "AND PR_OA_NO != WIP_OC_NO");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                            "SET WIP_REQUESTED_MONTH = PR_REQUESTED_MONTH, " +
//                            "WIP_REQ_MTH_LAST_DDMMYY = PR_REQ_MTH_LAST_DDMMYY " +
//                            "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                            "AND WIP_REQUESTED_MONTH != PR_REQUESTED_MONTH " +
//                            "AND PR_REQUESTED_MONTH != ''  ");
//        
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                            "SET WIP_UPN = PR_UPN " +
//                            "WHERE PR_PIECE_NO = WIP_PIECE_NO " +
//                            "AND WIP_UPN != PR_UPN");
//        
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                            "SET WIP_CURRENT_SCH_MONTH = PR_CURRENT_SCH_MONTH, " +
//                            "WIP_CURRENT_SCH_LAST_DDMMYY = PR_CURRENT_SCH_LAST_DDMMYY  " +
//                            "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                            "AND WIP_CURRENT_SCH_MONTH != PR_CURRENT_SCH_MONTH " +
//                            "AND PR_CURRENT_SCH_MONTH != ''");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_REQUESTED_MONTH = '' WHERE WIP_OBSOLETE = 'OBSOLETE' AND WIP_PRIORITY_HOLD_CAN_FLAG IN (0,1,2,3,4,5) ");
//            
//            data.Execute("UPDATE  PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_OC_LAST_DDMMYY = LAST_DAY(CONCAT(RIGHT(TRIM(WIP_OC_MONTHYEAR),4),'-',CASE WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Jan' THEN '01'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Feb' THEN '02'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Mar' THEN '03'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Apr' THEN '04'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'May' THEN '05'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Jun' THEN '06'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Jul' THEN '07'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Aug' THEN '08'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Sep' THEN '09'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Oct' THEN '10'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Nov' THEN '11'  WHEN SUBSTRING(TRIM(WIP_OC_MONTHYEAR),1,3) = 'Dec' THEN '12' END,'-01'))");
//            
//            data.Execute("UPDATE  PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_REQ_MTH_LAST_DDMMYY = LAST_DAY(CONCAT(RIGHT(TRIM(WIP_REQUESTED_MONTH),4),'-',CASE WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Jan' THEN '01'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Feb' THEN '02'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Mar' THEN '03'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Apr' THEN '04'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'May' THEN '05'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Jun' THEN '06'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Jul' THEN '07'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Aug' THEN '08'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Sep' THEN '09'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Oct' THEN '10'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Nov' THEN '11'  WHEN SUBSTRING(TRIM(WIP_REQUESTED_MONTH),1,3) = 'Dec' THEN '12' END,'-01'))");
//            
//            data.Execute("UPDATE  PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_CURRENT_SCH_LAST_DDMMYY = LAST_DAY(CONCAT(RIGHT(TRIM(WIP_CURRENT_SCH_MONTH),4),'-',CASE WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Jan' THEN '01'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Feb' THEN '02'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Mar' THEN '03'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Apr' THEN '04'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'May' THEN '05'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Jun' THEN '06'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Jul' THEN '07'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Aug' THEN '08'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Sep' THEN '09'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Oct' THEN '10'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Nov' THEN '11'  WHEN SUBSTRING(TRIM(WIP_CURRENT_SCH_MONTH),1,3) = 'Dec' THEN '12' ELSE '0' END,'-01'))");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER  SET WIP_REQ_MTH_LAST_DDMMYY ='0000-00-00' WHERE WIP_REQUESTED_MONTH IS NULL OR WIP_REQUESTED_MONTH ='' OR WIP_REQUESTED_MONTH ='0'");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER  SET WIP_OC_LAST_DDMMYY ='0000-00-00' WHERE WIP_OC_MONTHYEAR IS NULL OR WIP_OC_MONTHYEAR ='' OR WIP_OC_MONTHYEAR ='0'");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER  SET WIP_CURRENT_SCH_LAST_DDMMYY ='0000-00-00' WHERE WIP_CURRENT_SCH_MONTH IS NULL OR WIP_CURRENT_SCH_MONTH ='' OR WIP_CURRENT_SCH_MONTH ='0'");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET WIP_LENGTH = PR_LENGTH " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_LENGTH != PR_LENGTH");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET WIP_BILL_LENGTH = PR_BILL_LENGTH " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_BILL_LENGTH != PR_BILL_LENGTH");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_WIDTH = PR_WIDTH " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_WIDTH != PR_WIDTH");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_BILL_WIDTH = PR_BILL_WIDTH " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_BILL_WIDTH != PR_BILL_WIDTH");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_SQMTR = PR_SQMTR " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_SQMTR != PR_SQMTR");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_BILL_SQMTR = PR_BILL_SQMTR " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_BILL_SQMTR != PR_BILL_SQMTR");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_BILL_GSM = PR_BILL_GSM " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_BILL_GSM != PR_BILL_GSM");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_GSM = PR_GSM " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_GSM != PR_GSM");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_PRODUCT_CODE = PR_PRODUCT_CODE " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_PRODUCT_CODE != PR_PRODUCT_CODE");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_BILL_PRODUCT_CODE = PR_BILL_PRODUCT_CODE " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_BILL_PRODUCT_CODE != PR_BILL_PRODUCT_CODE");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_BILL_STYLE = PR_BILL_STYLE " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_BILL_STYLE != PR_BILL_STYLE");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER,PRODUCTION.FELT_WIP_PIECE_REGISTER " +
//                        "SET  WIP_STYLE = PR_STYLE " +
//                        "WHERE PR_PIECE_NO = WIP_PIECE_NO  " +
//                        "AND WIP_STYLE != PR_STYLE");
//            
//            data.Execute("UPDATE PRODUCTION.FELT_WIP_PIECE_REGISTER SET WIP_THORITICAL_WEIGHT =0 ,WIP_BILL_WEIGHT =0   WHERE WIP_PIECE_AB_FLAG ='AB' AND RIGHT(WIP_EXT_PIECE_NO,2) ='-B' AND WIP_THORITICAL_WEIGHT !=0");
//            
            
            
    }
    
    private static void InvoiceValueUpdationWithoutDiscount()  {
        clsOrderValueCalc calc = new clsOrderValueCalc();
        EITLERP.FeltSales.common.FeltInvCalc InvObj ;
        String SQL = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER ";
//        System.out.println("SQL = " + SQL);
        try {
            Connection connection;
            Statement statement;
            ResultSet resultSet,rsHeader;
            connection = data.getConn();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(SQL);
            
            while (resultSet.next()) {
                 
                 try{ 
                     String PieceNo = resultSet.getString("PR_PIECE_NO");
                     String Product_Code = resultSet.getString("PR_BILL_PRODUCT_CODE");
                     String Piece_Stage = resultSet.getString("PR_PIECE_STAGE"); 
                     String Party_Code = resultSet.getString("PR_PARTY_CODE");
                     Float Length = resultSet.getFloat("PR_BILL_LENGTH");
                     Float Width = resultSet.getFloat("PR_BILL_WIDTH");
                     Float Weight = resultSet.getFloat("PR_BILL_WEIGHT");
                     Float SQMT = resultSet.getFloat("PR_BILL_SQMTR");
                     String CurDate = EITLERPGLOBAL.getCurrentDateDB();
                     String baleNo = resultSet.getString("PR_BALE_NO");
                     String baleDate = resultSet.getString("PR_PACKED_DATE");
                     
                     
                     InvObj = clsOrderValueCalc.calculateWithoutDiscount(PieceNo, Product_Code, Party_Code, Length, Width, Weight, SQMT, CurDate);
                     
                     float pRate = InvObj.getFicGrossRate();
                     float pInvAmt = InvObj.getFicInvAmt();
//                     System.out.println("Piece No : "+PieceNo+". Felt Rate : " + pRate + ". Inv Amt without Discount : " + pInvAmt + " ");
                     data.Execute("UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER SET PR_FELT_RATE='"+pRate+"',PR_FELT_VALUE_WITHOUT_DISCOUNT='"+pInvAmt+"' WHERE PR_PIECE_NO='"+PieceNo+"'");
                        
                 }catch(Exception e)
                 {
                     e.printStackTrace();
                 }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
