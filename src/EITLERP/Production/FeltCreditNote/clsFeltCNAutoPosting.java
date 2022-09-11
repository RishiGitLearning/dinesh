/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.Production.FeltCreditNote;

import EITLERP.*;
import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.FeltInvReport.NumWord;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class clsFeltCNAutoPosting {

    private ResultSet rsResultSet, rsResultSet1,rsResultSet2;
    private Connection Conn;
    private Statement Stmt;

    String fyr = String.valueOf(EITLERPGLOBAL.FinYearFrom).substring(2, 4);
    String tyr = String.valueOf(EITLERPGLOBAL.FinYearTo).substring(2, 4);

    public void GenerateGRCN(String docNo) {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_GOODS_RETURN,LCN_COMP,LCN_MAIN_ACCOUNT_CODE) SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_INVOICE_AMT,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,D.GOODS_RETURN_AMT,D.COMPENSATION_AMT,210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID='" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION = CONCAT('FROM ','" + EITLERPGLOBAL.FinFromDate + "',' TO ','" + EITLERPGLOBAL.FinToDate + "')  ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL = CONCAT('" + EITLERPGLOBAL.FinYearFrom + "','/','" + EITLERPGLOBAL.FinYearTo + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3,CND_EXT4,CND_EXT5) SELECT 1,LCN_NO,'GOODSRTN',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_GOODS_RETURN,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END,LCN_GOODS_RETURN,LCN_COMP FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,305080,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,ROUND(SUM(CND_EXT4)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,'01',CONCAT('Goods Return Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void GRCNVoucherPosting(String docNo) {
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)=17 AND SUBSTRING(VOUCHER_NO,3,2)='" + frmYr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='GOODSRTN' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + frmYr + "',1700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);

                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                    }
                }
            }

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);
        } catch (SQLException sqe) {
            sqe.printStackTrace();

        }

    }

    public void GenerateGRICN(String docNo) {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            //stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_GOODS_RETURN,LCN_COMP,LCN_MAIN_ACCOUNT_CODE) SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_INVOICE_AMT,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,D.GOODS_RETURN_AMT,D.COMPENSATION_AMT,210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID='" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN') ");
            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_GOODS_RETURN,LCN_COMP,LCN_CGST_PER,LCN_CGST_AMT,LCN_SGST_PER,LCN_SGST_AMT,LCN_IGST_PER,LCN_IGST_AMT,LCN_INS_CHECK,LCN_INS_CLAIM,EXT3,LCN_MAIN_ACCOUNT_CODE) "
                    + "SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_INVOICE_AMT,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,D.GOODS_RETURN_AMT,D.COMPENSATION_AMT,D.CGST_ITC_PER,D.CGST_ITC_AMT,D.SGST_ITC_PER,D.SGST_ITC_AMT,D.IGST_ITC_PER,D.IGST_ITC_AMT,D.INSURANCE_CHECK,D.INSURANCE,ROUND(D.CN_GROSS_VALUE-D.CN_DISC_BILL,2),210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID='" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 "
                    + " AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN')");
            
            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION = CONCAT('FROM ','" + EITLERPGLOBAL.FinFromDate + "',' TO ','" + EITLERPGLOBAL.FinToDate + "')  ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL = CONCAT('" + EITLERPGLOBAL.FinYearFrom + "','/','" + EITLERPGLOBAL.FinYearTo + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            //stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3,CND_EXT4,CND_EXT5) SELECT 1,LCN_NO,'GOODSRTN',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_GOODS_RETURN,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END,LCN_GOODS_RETURN,LCN_COMP FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");
            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3,CND_EXT4,CND_EXT5,CND_EXT6,CND_EXT8,CND_EXT9,CND_EXT10,CND_EXT11,CND_EXT12,CND_EXT13,CND_EXT14) "
                    + "SELECT 1,LCN_NO,'GOODSRTN',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_GOODS_RETURN,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END,LCN_GOODS_RETURN,LCN_INS_CHECK,LCN_INS_CLAIM,LCN_CGST_PER,LCN_SGST_PER,LCN_IGST_PER,LCN_CGST_AMT,LCN_SGST_AMT,LCN_IGST_AMT,EXT3 FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");
            
            stmt.execute("UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL PD,FINANCE.D_FIN_VOUCHER_DETAIL FD "
                    + "SET PD.CND_EXT14=FD.AMOUNT WHERE PD.CND_INVOICE_NO=FD.INVOICE_NO AND PD.CND_INVOICE_DATE=FD.INVOICE_DATE AND FD.EFFECT='C' AND FD.MAIN_ACCOUNT_CODE='301378' "
                    + " AND PD.CND_YEAR_MON_ID=CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
            
            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
            
            //stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,305080,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,ROUND(SUM(CND_EXT4)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,'01',CONCAT('Goods Return Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");
            //stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,305080,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,ROUND(SUM(CND_EXT4)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,'01',CONCAT('Goods Return Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");
            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,305080,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,ROUND(SUM(CND_EXT4)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-08',CND_LINK_NO,'01',CONCAT('Goods Return Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");
            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }       
    
    public void GRICNVoucherPosting(String docNo) {
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            //String frmYr = "19";
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                //rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)=17 AND SUBSTRING(VOUCHER_NO,3,2)='" + frmYr + "' ) AS A");
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)='08' AND SUBSTRING(VOUCHER_NO,3,2)='" + frmYr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='GOODSRTN' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        //String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + frmYr + "',1700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + frmYr + "','0',800000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);
                        
                        String SQL4 = "UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL D,PRODUCTION.D_CREDIT_NOTE_HEADER H SET D.CND_FIN_VOUCHER_NO = H.CNH_FIN_VOUCHER_NO  WHERE D.CND_YEAR_MON_ID=H.CNH_YEAR_MON_ID AND CND_TYPE ='GOODSRTN' AND CND_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 4 : " + SQL4);
                        st2.execute(SQL4);

                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                    }
                }
                
                ResultSet rsSchemeDetail = Stmt.executeQuery("SELECT CND_SUB_ACCOUNT_CODE,CND_PIECE_NO,CND_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_TYPE ='GOODSRTN' ORDER BY CND_SUB_ACCOUNT_CODE");

                rsSchemeDetail.first();
                if (rsSchemeDetail.getRow() > 0) {
                    while (!rsSchemeDetail.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeDetail.getString("CND_SUB_ACCOUNT_CODE"));
                        
                        Statement st2 = Conn.createStatement();
                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeDetail.getDouble("CND_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL SET CND_REMARKS = '" + rsInWord + "' WHERE CND_TYPE ='GOODSRTN' AND CND_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='" + rsSchemeDetail.getString("CND_PIECE_NO") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeDetail.next();
                        
                    }
                }
            }
            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            //Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
            //Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
           Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,'08','DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            //Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");

            
            String detailInsertQuery="";
            String grossAmtQuery="";
            String insAmtQuery="";
            String gstAmtQuery="";
            String crNoteAmtQuery="";
            detailInsertQuery += "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,"
                                 + "MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";
            rsResultSet1 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN' AND CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");              
                String grossAmt="";
                String insCheck="";
                String insAmt="";
                double cgstPer=0 ,sgstPer=0, igstPer=0;
                double cgstAmt=0 ,sgstAmt=0, igstAmt=0;
                String crNoteAmt="";
                String pieceNo="";
                int srno=0;
                rsResultSet1.first();
                while (!rsResultSet1.isAfterLast() && rsResultSet1.getRow() > 0) {
                    grossAmt = rsResultSet1.getString("CND_EXT14");
                    insCheck = rsResultSet1.getString("CND_EXT5");
                    insAmt = rsResultSet1.getString("CND_EXT6");                    
                    
                    cgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT8"));
                    sgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT9"));
                    igstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT10"));
                    cgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT11"));
                    sgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT12"));
                    igstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT13"));
                    crNoteAmt = rsResultSet1.getString("CND_EXT4");
                    pieceNo=rsResultSet1.getString("CND_PIECE_NO");
                    if(srno==0){
                        srno=srno+1;
                    detailInsertQuery +=" SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }else{
                        srno=srno+1;
                    detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }
                    
                    
                    if(insCheck.equals("1")){
                    srno=srno+1;
                    detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',427027,'',"
                            + "CND_EXT6,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                
                    }
                    if(igstPer==12){
                        if(insCheck.equals("1")){
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',231777,'',"   //127570
                            + "ROUND((CND_EXT14+CND_EXT6)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',231777,'',"   //127570
                            + "ROUND((CND_EXT14)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    } else {
                        if(insCheck.equals("1")){
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',231770,'',"    //127566
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',231771,'',"    //127568
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',231770,'',"     //127566
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',231771,'',"    //127568
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    }
                    srno=srno+1;
                    detailInsertQuery +="UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'C',CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,"
                            + "CND_EXT4,70,2,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                    
                                        
                    rsResultSet1.next();
                }
             System.out.println(detailInsertQuery);
                Stmt.execute(detailInsertQuery);
            
            

            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");
            //Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");
           
            String detailEXInsertQuery="";
            //String grossAmtQuery="";
            //String insAmtQuery="";
            //String gstAmtQuery="";
            //String crNoteAmtQuery="";
            detailEXInsertQuery += "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,"
                                 + "MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";
            rsResultSet2 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN' AND CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");              
            /*    String grossAmt="";
                String insCheck="";
                String insAmt="";
                double cgstPer=0 ,sgstPer=0, igstPer=0;
                double cgstAmt=0 ,sgstAmt=0, igstAmt=0;
                String crNoteAmt="";
                String pieceNo="";
                    */
                int srnoex=0;
                    
                rsResultSet2.first();
                while (!rsResultSet2.isAfterLast() && rsResultSet2.getRow() > 0) {
                    grossAmt = rsResultSet2.getString("CND_EXT14");
                    insCheck = rsResultSet2.getString("CND_EXT5");
                    insAmt = rsResultSet2.getString("CND_EXT6");                    
                    
                    cgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT8"));
                    sgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT9"));
                    igstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT10"));
                    cgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT11"));
                    sgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT12"));
                    igstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT13"));
                    crNoteAmt = rsResultSet2.getString("CND_EXT4");
                    pieceNo=rsResultSet2.getString("CND_PIECE_NO");
                    if(srnoex==0){
                        srnoex=srnoex+1;
                    detailEXInsertQuery +=" SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }else{
                        srnoex=srnoex+1;
                    detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }
                    
                    
                    if(insCheck.equals("1") && Double.parseDouble(insAmt)!=0){
                    srnoex=srnoex+1;
                    detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',427027,'',"
                            + "CND_EXT6,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                
                    }
                    if(igstPer==12){
                        if(insCheck.equals("1")){
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',231777,'',"    //127570
                            + "ROUND((CND_EXT14+CND_EXT6)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',231777,'',"    //127570
                            + "ROUND((CND_EXT14)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    } else {
                        if(insCheck.equals("1")){
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',231770,'',"   //127566
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',231771,'',"    //127568
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',231770,'',"   //127566
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',231771,'',"   //127568
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    }
                    srnoex=srnoex+1;
                    detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'C',CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,"
                            + "CND_EXT4,70,2,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                    
                                        
                    rsResultSet2.next();
                }
             System.out.println(detailEXInsertQuery);
                Stmt.execute(detailEXInsertQuery);
            
            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);
        } catch (SQLException sqe) {
            sqe.printStackTrace();

        }

    }
    
    public void GenerateGRPCN(String docNo) {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            //stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_GOODS_RETURN,LCN_COMP,LCN_MAIN_ACCOUNT_CODE) SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_INVOICE_AMT,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,D.GOODS_RETURN_AMT,D.COMPENSATION_AMT,210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID='" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN') ");
            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_GOODS_RETURN,LCN_COMP,LCN_CGST_PER,LCN_CGST_AMT,LCN_SGST_PER,LCN_SGST_AMT,LCN_IGST_PER,LCN_IGST_AMT,LCN_INS_CHECK,LCN_INS_CLAIM,EXT3,LCN_MAIN_ACCOUNT_CODE) "
                    + "SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_INVOICE_AMT,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,D.GOODS_RETURN_AMT,D.COMPENSATION_AMT,D.CGST_PER,D.CGST_AMT,D.SGST_PER,D.SGST_AMT,D.IGST_PER,D.IGST_AMT,D.INSURANCE_CHECK,D.INSURANCE,ROUND(D.CN_GROSS_VALUE-D.CN_DISC_BILL,2),210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID='" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 "
                    + " AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN')");
            
            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION = CONCAT('FROM ','" + EITLERPGLOBAL.FinFromDate + "',' TO ','" + EITLERPGLOBAL.FinToDate + "')  ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL = CONCAT('" + EITLERPGLOBAL.FinYearFrom + "','/','" + EITLERPGLOBAL.FinYearTo + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            //stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3,CND_EXT4,CND_EXT5) SELECT 1,LCN_NO,'GOODSRTN',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_GOODS_RETURN,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END,LCN_GOODS_RETURN,LCN_COMP FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");
            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3,CND_EXT4,CND_EXT5,CND_EXT6,CND_EXT8,CND_EXT9,CND_EXT10,CND_EXT11,CND_EXT12,CND_EXT13,CND_EXT14) "
                    + "SELECT 1,LCN_NO,'GOODSRTN',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_GOODS_RETURN,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END,LCN_GOODS_RETURN,LCN_INS_CHECK,LCN_INS_CLAIM,LCN_CGST_PER,LCN_SGST_PER,LCN_IGST_PER,LCN_CGST_AMT,LCN_SGST_AMT,LCN_IGST_AMT,EXT3 FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");
            
            stmt.execute("UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL PD,FINANCE.D_FIN_VOUCHER_DETAIL FD "
                    + "SET PD.CND_EXT14=FD.AMOUNT WHERE PD.CND_INVOICE_NO=FD.INVOICE_NO AND PD.CND_INVOICE_DATE=FD.INVOICE_DATE AND FD.EFFECT='C' AND FD.MAIN_ACCOUNT_CODE='301378' "
                    + " AND PD.CND_YEAR_MON_ID=CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
            
            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
            
            //stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,305080,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,ROUND(SUM(CND_EXT4)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,'01',CONCAT('Goods Return Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");
            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,305080,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,ROUND(SUM(CND_EXT4)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-07',CND_LINK_NO,'01',CONCAT('Goods Return Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");
            
            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }       
    
    public void GRPCNVoucherPosting(String docNo) {
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            //String frmYr = "19";
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)='07' AND SUBSTRING(VOUCHER_NO,3,2)='" + frmYr + "' ) AS A");
            
//            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,5) = 'FECN/'", FinanceGlobal.FinURL)) {
//                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,5) = 'FECN/' ) AS A");
//                rsResultSet = Stmt.executeQuery("SELECT COALESCE(VOUCHER_NO,'FECN/0000') AS VOUCHER_NO,COALESCE(SUBSTRING(VOUCHER_NO,1,5),'FECN/') AS CNV,COALESCE(RIGHT(VOUCHER_NO,4),0000) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,5) = 'FECN/') AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkSrNo = "";
                

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='GOODSRTN' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + frmYr + "','0',700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
//                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('FECN/',lpad(ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ','')),4,'0')),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);
                        
                        String SQL4 = "UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL D,PRODUCTION.D_CREDIT_NOTE_HEADER H SET D.CND_FIN_VOUCHER_NO = H.CNH_FIN_VOUCHER_NO  WHERE D.CND_YEAR_MON_ID=H.CNH_YEAR_MON_ID AND CND_TYPE ='GOODSRTN' AND CND_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 4 : " + SQL4);
                        st2.execute(SQL4);

                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                    }
                }
            }
//            else{                
//                
//                
//                String draftNo = "FECN/";
//                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
//                String linkSrNo = "";
//                
//                int counter = 1;
//                String strsql="SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='GOODSRTN' ORDER BY CNH_SUB_ACCOUNT_CODE";
//                System.out.println(strsql);
//                ResultSet rsSchemeHeader = Stmt.executeQuery(strsql);
//               
//                rsSchemeHeader.first();
//                System.out.println(rsSchemeHeader.getRow());
//                if (rsSchemeHeader.getRow() > 0) {
//                    while (!rsSchemeHeader.isAfterLast()) {
//                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));
//
//                        Statement st2 = Conn.createStatement();
//                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + 0001 + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
//                        System.out.println("SQL 1 : " + SQL1);
//                        st2.execute(SQL1);
//
//                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = 'FECN/0001',CNH_LEGACY_NO = '0001'  WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
//                        System.out.println("SQL 2 : " + SQL2);
//                        st2.execute(SQL2);
//                        
//                        String SQL4 = "UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL D,PRODUCTION.D_CREDIT_NOTE_HEADER H SET D.CND_FIN_VOUCHER_NO = H.CNH_FIN_VOUCHER_NO  WHERE D.CND_YEAR_MON_ID=H.CNH_YEAR_MON_ID AND CND_TYPE ='GOODSRTN' AND CND_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
//                        System.out.println("SQL 4 : " + SQL4);
//                        st2.execute(SQL4);
//
//                        NumWord num = new NumWord();
//                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
//                        
//                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
//                        System.out.println("SQL 3 : " + SQL3);
//                        st2.execute(SQL3);
//
//                        rsSchemeHeader.next();
//                        counter++;
//                    }
//                }
//            }
            
                ResultSet rsSchemeDetail = Stmt.executeQuery("SELECT CND_SUB_ACCOUNT_CODE,CND_PIECE_NO,CND_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_TYPE ='GOODSRTN' ORDER BY CND_SUB_ACCOUNT_CODE");

                rsSchemeDetail.first();
                if (rsSchemeDetail.getRow() > 0) {
                    while (!rsSchemeDetail.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeDetail.getString("CND_SUB_ACCOUNT_CODE"));
                        
                        Statement st2 = Conn.createStatement();
                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeDetail.getDouble("CND_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL SET CND_REMARKS = '" + rsInWord + "' WHERE CND_TYPE ='GOODSRTN' AND CND_YEAR_MON_ID =CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='" + rsSchemeDetail.getString("CND_PIECE_NO") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeDetail.next();
                        
                    }
                }
            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            //Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");
            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,EXCLUDE_IN_ADJ,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,'07','DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,1,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            //Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");

            
            String detailInsertQuery="";
            String grossAmtQuery="";
            String insAmtQuery="";
            String gstAmtQuery="";
            String crNoteAmtQuery="";
            detailInsertQuery += "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,"
                                 + "MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";
            rsResultSet1 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN' AND CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");              
                String grossAmt="";
                String insCheck="";
                String insAmt="";
                double cgstPer=0 ,sgstPer=0, igstPer=0;
                double cgstAmt=0 ,sgstAmt=0, igstAmt=0;
                String crNoteAmt="";
                String pieceNo="";
                int srno=0;
                rsResultSet1.first();
                while (!rsResultSet1.isAfterLast() && rsResultSet1.getRow() > 0) {
                    grossAmt = rsResultSet1.getString("CND_EXT14");
                    insCheck = rsResultSet1.getString("CND_EXT5");
                    insAmt = rsResultSet1.getString("CND_EXT6");                    
                    
                        cgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT8"));
                    sgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT9"));
                    igstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT10"));
                    cgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT11"));
                    sgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT12"));
                    igstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT13"));
                    crNoteAmt = rsResultSet1.getString("CND_EXT4");
                    pieceNo=rsResultSet1.getString("CND_PIECE_NO");
                    if(srno==0){
                        srno=srno+1;
                    detailInsertQuery +=" SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }else{
                        srno=srno+1;
                    detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }
                    
                    
                    if(insCheck.equals("1")){
                    srno=srno+1;
                    detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',427027,'',"
                            + "CND_EXT6,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                
                    }
                    if(igstPer==12){
                        if(insCheck.equals("1")){
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',127570,'',"
                            + "ROUND((CND_EXT14+CND_EXT6)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',127570,'',"
                            + "ROUND((CND_EXT14)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    } else {
                        if(insCheck.equals("1")){
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',127566,'',"
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',127568,'',"
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',127566,'',"
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srno=srno+1;
                        detailInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'D',127568,'',"
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    }
                    srno=srno+1;
                    detailInsertQuery +="UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srno+",'C',CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,"
                            + "CND_EXT4,70,2,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                    
                                        
                    rsResultSet1.next();
                }
             System.out.println(detailInsertQuery);
                Stmt.execute(detailInsertQuery);
            
            

            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");
            //Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='GOODSRTN' AND CNH_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') ");
           
            String detailEXInsertQuery="";
            //String grossAmtQuery="";
            //String insAmtQuery="";
            //String gstAmtQuery="";
            //String crNoteAmtQuery="";
            detailEXInsertQuery += "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,"
                                 + "MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";
            rsResultSet2 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='GOODSRTN' AND CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "')");              
            /*    String grossAmt="";
                String insCheck="";
                String insAmt="";
                double cgstPer=0 ,sgstPer=0, igstPer=0;
                double cgstAmt=0 ,sgstAmt=0, igstAmt=0;
                String crNoteAmt="";
                String pieceNo="";
                    */
                int srnoex=0;
                    
                rsResultSet2.first();
                while (!rsResultSet2.isAfterLast() && rsResultSet2.getRow() > 0) {
                    grossAmt = rsResultSet2.getString("CND_EXT14");
                    insCheck = rsResultSet2.getString("CND_EXT5");
                    insAmt = rsResultSet2.getString("CND_EXT6");                    
                    
                    cgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT8"));
                    sgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT9"));
                    igstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT10"));
                    cgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT11"));
                    sgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT12"));
                    igstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT13"));
                    crNoteAmt = rsResultSet2.getString("CND_EXT4");
                    pieceNo=rsResultSet2.getString("CND_PIECE_NO");
                    if(srnoex==0){
                        srnoex=srnoex+1;
                    detailEXInsertQuery +=" SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }else{
                        srnoex=srnoex+1;
                    detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',305080,'',"
                            + "CND_EXT14,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";
                    }
                    
                    
                    if(insCheck.equals("1") && Double.parseDouble(insAmt)!=0){
                    srnoex=srnoex+1;
                    detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',427027,'',"
                            + "CND_EXT6,0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                
                    }
                    if(igstPer==12){
                        if(insCheck.equals("1")){
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',127570,'',"
                            + "ROUND((CND_EXT14+CND_EXT6)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',127570,'',"
                            + "ROUND((CND_EXT14)*0.12,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    } else {
                        if(insCheck.equals("1")){
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',127566,'',"
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',127568,'',"
                            + "ROUND((CND_EXT14+CND_EXT6)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }else{
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',127566,'',"
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        srnoex=srnoex+1;
                        detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'D',127568,'',"
                            + "ROUND((CND_EXT14)*0.06,0),0,0,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                                                                                            
                        }
                    }
                    srnoex=srnoex+1;
                    detailEXInsertQuery +=" UNION ALL SELECT 2,CND_FIN_VOUCHER_NO,"+srnoex+",'C',CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,"
                            + "CND_EXT4,70,2,0,'" + EITLERPGLOBAL.getCurrentDateDB() + "',CND_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE ='GOODSRTN' AND"
                            + " CND_YEAR_MON_ID = CONCAT('GR','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_PIECE_NO='"+pieceNo+"'";                    
                                        
                    rsResultSet2.next();
                }
             System.out.println(detailEXInsertQuery);
                Stmt.execute(detailEXInsertQuery);
            
            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);
        } catch (SQLException sqe) {
            sqe.printStackTrace();

        }

    }

    
    public void GenerateCompCN(String docNo) {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_GOODS_RETURN,LCN_COMP,LCN_MAIN_ACCOUNT_CODE) SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_INVOICE_AMT,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,D.GOODS_RETURN_AMT,D.COMPENSATION_AMT,210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID = '" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='COMP') "); //AND CND_INVOICE_DATE BETWEEN '" + frmdt + "' AND '" + todt + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "')"); //WHERE LCN_INVOICE_DATE>='" + frmdt + "' AND LCN_INVOICE_DATE<='" + todt + "'");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION = CONCAT('FROM ','" + EITLERPGLOBAL.FinFromDate + "',' TO ','" + EITLERPGLOBAL.FinToDate + "')  ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL = CONCAT('" + EITLERPGLOBAL.FinYearFrom + "','/','" + EITLERPGLOBAL.FinYearTo + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3,CND_EXT4,CND_EXT5) SELECT 1,LCN_NO,'COMP',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_COMP,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END,LCN_GOODS_RETURN,LCN_COMP FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,435118,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,ROUND(SUM(CND_EXT5)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,'03',CONCAT('Compensation Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void CompCNVoucherPosting(String docNo) {
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)=17 AND SUBSTRING(VOUCHER_NO,3,2)='" + frmYr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='COMP' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID =CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + frmYr + "',1700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID =CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);
                        
                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID =CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                    }
                }
            }

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='COMP' AND CNH_YEAR_MON_ID = CONCAT('CM','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);

        } catch (SQLException sqe) {
            sqe.printStackTrace();

        }
    }

    public void GenerateOEMCN(String docNo) {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_MAIN_ACCOUNT_CODE) SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_NET_BASIC,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID = '" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='OEM') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "')"); //WHERE LCN_INVOICE_DATE>='" + frmdt + "' AND LCN_INVOICE_DATE<='" + todt + "'");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION = CONCAT('FROM ','" + EITLERPGLOBAL.FinFromDate + "',' TO ','" + EITLERPGLOBAL.FinToDate + "')  ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL = CONCAT('" + EITLERPGLOBAL.FinYearFrom + "','/','" + EITLERPGLOBAL.FinYearTo + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3) SELECT 1,LCN_NO,'OEM',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_OEM,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,435132,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,SUM(CND_EXT2),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,11,CONCAT('OEM Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void OEMCNVoucherPosting(String docNo) {
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)=17 AND SUBSTRING(VOUCHER_NO,3,2)='" + frmYr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='OEM' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID =CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + frmYr + "',1700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID =CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);

                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_LINK_NO = CONCAT(CNH_LEGACY_NO,RIGHT(CNH_LINK_NO,10)) WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID =CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);
                        
                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL4 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID =CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 4 : " + SQL4);
                        st2.execute(SQL4);

                        rsSchemeHeader.next();
                        counter++;
                    }
                }
            }

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='OEM' AND CNH_YEAR_MON_ID = CONCAT('OEM','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);

        } catch (SQLException sqe) {
            sqe.printStackTrace();

        }

    }

    public void GenerateInsClmCN(String docNo) {
        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,EXT12,EXT13,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,EXT2,EXT4,LCN_UNADJ,LCN_YEAR_END,LCN_OEM,LCN_GOODS_RETURN,LCN_COMP,LCN_INS_CLAIM,LCN_MAIN_ACCOUNT_CODE) SELECT D.CN_ID,D.CN_PARTY_CODE,D.CN_PARTY_NAME,D.CN_INV_PRODUCT_CODE,D.CN_INV_PIECE_NO,D.CN_INVOICE_NO,D.CN_INVOICE_DATE,D.CN_INVOICE_AMT,D.CN_OEM,D.CN_INV_WI_SQMTR,D.CN_GROSS_VALUE,D.CN_UNADJUSTED_RS,D.CN_YEAR_END_DISC_RS,D.CN_OEM_VALUE,D.GOODS_RETURN_AMT,D.COMPENSATION_AMT,INSURANCE_CLAIM_AMT,210010 FROM PRODUCTION.FELT_CN_TEMP_DETAIL D,PRODUCTION.FELT_CN_TEMP_HEADER H WHERE D.CN_ID=H.CN_ID AND D.CN_ID='" + docNo + "' AND H.CANCELED =0 AND H.APPROVED =1 AND CONCAT(D.CN_INVOICE_NO,SUBSTRING(D.CN_INVOICE_DATE,1,10)) NOT IN (SELECT CONCAT(CND_INVOICE_NO,CND_INVOICE_DATE) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='INSCLAIM') "); //AND CND_INVOICE_DATE BETWEEN '" + frmdt + "' AND '" + todt + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "')"); //WHERE LCN_INVOICE_DATE>='" + frmdt + "' AND LCN_INVOICE_DATE<='" + todt + "'");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION = CONCAT('FROM ','" + EITLERPGLOBAL.FinFromDate + "',' TO ','" + EITLERPGLOBAL.FinToDate + "')  ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL = CONCAT('" + EITLERPGLOBAL.FinYearFrom + "','/','" + EITLERPGLOBAL.FinYearTo + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PIECE_NO,CND_QUALITY,CND_EXT7,CND_GROSS_AMT,CND_EXT1,CND_EXT2,CND_EXT3,CND_EXT4,CND_EXT5,CND_EXT6) SELECT 1,LCN_NO,'INSCLAIM',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_INS_CLAIM,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT13,EXT12,EXT2,EXT4,LCN_UNADJ,LCN_OEM,LCN_YEAR_END,LCN_GOODS_RETURN,LCN_COMP,LCN_INS_CLAIM FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,132587,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,SUM(CND_EXT6),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,17,CONCAT('Insurance Claim Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_PERCENT LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void ICCNVoucherPosting(String docNo) {
        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)=17 AND SUBSTRING(VOUCHER_NO,3,2)='" + frmYr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='INSCLAIM' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID =CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + frmYr + "',1700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID =CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);

                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_LINK_NO = CONCAT(CNH_LEGACY_NO,RIGHT(CNH_LINK_NO,10)) WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID =CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);
                        
                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL4 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID =CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 4 : " + SQL4);
                        st2.execute(SQL4);

                        rsSchemeHeader.next();
                        counter++;
                    }
                }
            }

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='INSCLAIM' AND CNH_YEAR_MON_ID = CONCAT('IC','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);

        } catch (SQLException sqe) {
            sqe.printStackTrace();

        }

    }

    public void GenerateUnadjCN(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);

        String frmdt = EITLERPGLOBAL.formatDateDB(fromDate);
        String frmyr = fromDate.substring(6, 10);
        String fyr = frmyr.substring(2, 4);
        System.out.println(frmdt);
        System.out.println(frmyr);
        System.out.println(fyr);
        String todt = EITLERPGLOBAL.formatDateDB(toDate);
        String toyr = toDate.substring(6, 10);
        String tyr = toyr.substring(2, 4);
        System.out.println(todt);
        System.out.println(toyr);
        System.out.println(tyr);
        
        String docId = docNo.substring(0, 2);
        System.out.println(docId);

        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,LCN_RC_VOUCHER,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,LCN_CREDIT_AMOUNT,EXT2,LCN_MAIN_ACCOUNT_CODE) SELECT H.UNADJ_ID,PARTY_CODE,PARTY_NAME,PRODUCT_CODE,INVOICE_NO,INVOICE_DATE,INV_BASIC_AMT,CASE WHEN WORK_DISC_PER>0 THEN WORK_DISC_PER ELSE SANC_SEAM_CHARGES END AS DISC_PER,DISC_AMT,PIECE_NO,210010 FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL D,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H WHERE D.UNADJ_ID = H.UNADJ_ID AND H.UNADJ_ID='" + docNo + "'  AND CANCELLED =0 AND APPROVED =1 AND INVOICE_NO NOT IN (SELECT CND_INVOICE_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID LIKE '%"+docId+"%' AND CND_INVOICE_DATE BETWEEN '" + frmdt + "' AND '" + todt + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') WHERE LCN_INVOICE_DATE>='" + frmdt + "' AND LCN_INVOICE_DATE<='" + todt + "'");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION =CONCAT('FROM ','" + fromDate + "',' TO ','" + toDate + "')");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL =CONCAT('" + frmyr + "','/','" + toyr + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT('/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_CATG) SELECT 1,LCN_NO,'UNADJ',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_CREDIT_AMOUNT,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT2 FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,435132,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),ROUND(SUM(CND_CREDIT_AMOUNT)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',CND_LINK_NO,'10',CONCAT('Unadjusted Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_TYPE,CND_LC_OPENER,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void UnadjCNVoucherPosting(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);

        String frmdt = EITLERPGLOBAL.formatDateDB(fromDate);
        String frmyr = fromDate.substring(6, 10);
        String fyr = frmyr.substring(2, 4);
        System.out.println(frmdt);
        System.out.println(frmyr);
        System.out.println(fyr);
        String todt = EITLERPGLOBAL.formatDateDB(toDate);
        String toyr = toDate.substring(6, 10);
        String tyr = toyr.substring(2, 4);
        System.out.println(todt);
        System.out.println(toyr);
        System.out.println(tyr);

        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)=17 AND SUBSTRING(VOUCHER_NO,3,2)='" + fyr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.formatDateDB(toDate);
                String linkNo = "8101";
                int lnk = Integer.parseInt(linkNo);
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='UNADJ' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "',CNH_LINK_NO=CONCAT('" + lnk + "',RIGHT(CNH_LINK_NO,10)) WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + fyr + "',1700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);
                        
                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                        lnk++;
                    }
                }
            }

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }

    public void GenerateYearEndCN(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);

        String frmdt = EITLERPGLOBAL.formatDateDB(fromDate);
        String frmyr = fromDate.substring(6, 10);
        String fyr = frmyr.substring(2, 4);
        System.out.println(frmdt);
        System.out.println(frmyr);
        System.out.println(fyr);
        String todt = EITLERPGLOBAL.formatDateDB(toDate);
        String toyr = toDate.substring(6, 10);
        String tyr = toyr.substring(2, 4);
        System.out.println(todt);
        System.out.println(toyr);
        System.out.println(tyr);

        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

//            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_INVOICE_NO,LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,LCN_RC_VOUCHER,LCN_INVOICE_DATE,LCN_AMOUNT,EXT2,LCN_MAIN_ACCOUNT_CODE) SELECT DISTINCT INVOICE_NO,Y.FORM2_YEAR_END_ID,PARTY_CODE,PARTY_NAME,PRODUCT_CODE,INVOICE_DATE,BAS_AMT,SEAM_CHG,210010 FROM PRODUCTION.FELT_SAL_INVOICE_HEADER I, PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 Y, PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 H WHERE Y.FORM2_YEAR_END_PARTY_CODE=I.PARTY_CODE AND Y.FORM2_YEAR_END_PRODUCT_CODE=I.PRODUCT_CODE AND Y.FORM2_YEAR_END_ID=H.FORM2_YEAR_END_ID AND H.APPROVED=1 AND H.CANCELED=0 AND I.APPROVED=1 AND I.CANCELLED=0 AND UPPER(Y.FORM2_YEAR_END_YES_NO)='YES' AND INVOICE_DATE>='" + frmdt + "' AND INVOICE_DATE<='" + todt + "' AND Y.FORM2_FROM_DATE>='" + frmdt + "' AND Y.FORM2_TO_DATE<='" + todt + "' AND INVOICE_DATE>=Y.FORM2_EFFECTIVE_FROM AND INVOICE_DATE<=Y.FORM2_EFFECTIVE_TO");
            
            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_INVOICE_NO,LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,LCN_RC_VOUCHER,LCN_INVOICE_DATE,LCN_AMOUNT,EXT2,LCN_MAIN_ACCOUNT_CODE) SELECT INV.* FROM ( SELECT DISTINCT INVOICE_NO,Y.FORM2_YEAR_END_ID,PARTY_CODE,PARTY_NAME,PRODUCT_CODE,INVOICE_DATE,BAS_AMT,SEAM_CHG,210010 FROM PRODUCTION.FELT_SAL_INVOICE_HEADER I, PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 Y, PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 H WHERE Y.FORM2_YEAR_END_PARTY_CODE=I.PARTY_CODE AND Y.FORM2_YEAR_END_PRODUCT_CODE=I.PRODUCT_CODE AND Y.FORM2_YEAR_END_ID=H.FORM2_YEAR_END_ID AND H.APPROVED=1 AND H.CANCELED=0 AND I.APPROVED=1 AND I.CANCELLED=0 AND UPPER(Y.FORM2_YEAR_END_YES_NO)='YES' AND INVOICE_DATE>='" + frmdt + "' AND INVOICE_DATE<='" + todt + "' AND Y.FORM2_FROM_DATE>='" + frmdt + "' AND Y.FORM2_TO_DATE<='" + todt + "' AND INVOICE_DATE>=Y.FORM2_EFFECTIVE_FROM AND INVOICE_DATE<=Y.FORM2_EFFECTIVE_TO ) AS INV LEFT JOIN ( SELECT D.* FROM PRODUCTION.FELT_CN_TEMP_HEADER H, PRODUCTION.FELT_CN_TEMP_DETAIL D WHERE H.CN_ID=D.CN_ID AND H.CN_TYPE IN (6,7) AND H.APPROVED=1 AND H.CANCELED=0 AND D.CN_INVOICE_DATE>='" + frmdt + "' AND D.CN_INVOICE_DATE<='" + todt + "' ORDER BY D.CN_INVOICE_NO ) AS GRCM ON CONCAT(INVOICE_NO,INVOICE_DATE)=CONCAT(CN_INVOICE_NO,SUBSTRING(CN_INVOICE_DATE,1,10)) WHERE COALESCE(CN_INVOICE_NO,'')='' " );

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST T,PRODUCTION.FELT_YEAR_END_DISCOUNT_DETAIL_FORM2 A, PRODUCTION.FELT_YEAR_END_DISCOUNT_HEADER_FORM2 H SET T.LCN_PER=A.FORM2_YEAR_PERCENT,T.EXT3=A.FORM2_YEAR_SEAM_PERCENT WHERE  A.FORM2_YEAR_END_ID=H.FORM2_YEAR_END_ID AND UPPER(A.FORM2_YEAR_END_YES_NO)='YES' AND A.FORM2_FROM_DATE>='" + frmdt + "' AND A.FORM2_TO_DATE<='" + todt + "'  AND H.APPROVED=1 AND H.CANCELED=0 AND T.LCN_SUB_ACCOUNT_CODE=A.FORM2_YEAR_END_PARTY_CODE AND T.LCN_RC_VOUCHER=A.FORM2_YEAR_END_PRODUCT_CODE AND T.LCN_INVOICE_DATE>=A.FORM2_EFFECTIVE_FROM AND T.LCN_INVOICE_DATE<=A.FORM2_EFFECTIVE_TO");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_CREDIT_AMOUNT = ROUND((LCN_AMOUNT*LCN_PER)/100,2)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET EXT4 = ROUND((EXT2*EXT3)/100,2)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('YEAREND','" + fyr + "','" + tyr + "') WHERE LCN_INVOICE_DATE>='" + frmdt + "' AND LCN_INVOICE_DATE<='" + todt + "'");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION = CONCAT('FROM ','" + fromDate + "',' TO ','" + toDate + "')");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL = CONCAT('" + frmyr + "','/','" + toyr + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT('/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = PARTY_NAME WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_EXT2,CND_EXT3,CND_EXT4) SELECT 1,LCN_NO,'YEAREND',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,LCN_CREDIT_AMOUNT,EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT2,EXT3,EXT4 FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_PERCENT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_EXT4,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,435132,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),CND_PERCENT,SUM(CND_CREDIT_AMOUNT+CND_EXT4),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-17',SUM(CND_EXT4),CND_LINK_NO,10,CONCAT('Year End Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('YEAREND','" + fyr + "','" + tyr + "') GROUP BY CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID,CNH_CREDIT_AMOUNT = ROUND(CNH_CREDIT_AMOUNT,0) WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void YearEndCNVoucherPosting(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);

        String frmdt = EITLERPGLOBAL.formatDateDB(fromDate);
        String frmyr = fromDate.substring(6, 10);
        String fyr = frmyr.substring(2, 4);
        System.out.println(frmdt);
        System.out.println(frmyr);
        System.out.println(fyr);
        String todt = EITLERPGLOBAL.formatDateDB(toDate);
        String toyr = toDate.substring(6, 10);
        String tyr = toyr.substring(2, 4);
        System.out.println(todt);
        System.out.println(toyr);
        System.out.println(tyr);

        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)=17 AND SUBSTRING(VOUCHER_NO,3,2)='" + fyr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
                String draftDate = EITLERPGLOBAL.formatDateDB(toDate);
                String linkNo = "8501";
                int lnk = Integer.parseInt(linkNo);
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_SUB_ACCOUNT_CODE,CNH_PERCENT,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('YEAREND','" + fyr + "','" + tyr + "') AND CNH_TYPE ='YEAREND' ORDER BY CNH_SUB_ACCOUNT_CODE");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "',CNH_LINK_NO=CONCAT('" + lnk + "',RIGHT(CNH_LINK_NO,10)) WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID =CONCAT('YEAREND','" + fyr + "','" + tyr + "') AND  CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + fyr + "',1700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID =CONCAT('YEAREND','" + fyr + "','" + tyr + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);
                        
                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));
                        
                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID =CONCAT('YEAREND','" + fyr + "','" + tyr + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' ";
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                        lnk++;
                    }
                }
            }

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,LEGACY_NO,LEGACY_DATE,REASON_CODE,EXCLUDE_IN_ADJ) SELECT 2,CNH_FIN_VOUCHER_NO,17,'DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE,1 FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE,'',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) SELECT 2,CNH_FIN_VOUCHER_NO,1,'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "') UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO,2,'D',CNH_MAIN_ACCOUNT_CODE, '',CNH_CREDIT_AMOUNT,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='YEAREND' AND CNH_YEAR_MON_ID = CONCAT('YEAREND','" + fyr + "','" + tyr + "') ");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646  AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);

        } catch (SQLException sqe) {
            sqe.printStackTrace();

        }
    }

    public static void PrintCNSummary(String title, String strSQL) {
        try {

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("TITLE", title);

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/Production/FeltCreditNote/CreditNoteSummary.jrxml", 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void PrintCNDetail(String title, String strSQL, String fileName) {
        try {

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("TITLE", title);

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName(fileName, 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void PrintCNDraft(String strSQL) {
        try {

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/Production/FeltCreditNote/CreditNoteDraft.jrxml", 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void PrintGRPCN(String voucherNo,String docNo,String strSQL) {
        try {

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();
            //parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("GROSS_AMOUNT",data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+voucherNo+"' AND EFFECT='D' " +
              "AND MAIN_ACCOUNT_CODE='305080'"));
            parameterMap.put("INS_AMOUNT",data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+voucherNo+"' AND EFFECT='D' " +
              "AND MAIN_ACCOUNT_CODE='427027'"));
            parameterMap.put("IGST_AMOUNT", data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + voucherNo + "' AND EFFECT='D' "
                    + "AND MAIN_ACCOUNT_CODE='127570'"));
            parameterMap.put("CGST_AMOUNT", data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + voucherNo + "' AND EFFECT='D' "
                    + "AND MAIN_ACCOUNT_CODE='127566'"));
            parameterMap.put("SGST_AMOUNT", data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + voucherNo + "' AND EFFECT='D' "
                    + "AND MAIN_ACCOUNT_CODE='127568'"));
            parameterMap.put("INV_NO_DATE", data.getStringValueFromDB("SELECT CONCAT(CN_INVOICE_NO,'   &   ',DATE_FORMAT(CN_INVOICE_DATE,'%d/%m/%Y')) FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='"+docNo+"'"));
            parameterMap.put("IGST_MAIN_CODE","127570");
            parameterMap.put("CGST_MAIN_CODE","127566");
            parameterMap.put("SGST_MAIN_CODE","127568");
            String partyRefNoDate=data.getStringValueFromDB("SELECT CONCAT(PARTY_REF_DOC_NO,'   &   ',DATE_FORMAT(PARTY_REF_DOC_DATE,'%d/%m/%Y')) FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='"+docNo+"'");
            if(partyRefNoDate.contains("00/00/0000")){
            //if(partyRefNoDate.equals("   &   00/00/0000")){
                parameterMap.put("PARTY_REF_NO_DATE","");
            }else{
                parameterMap.put("PARTY_REF_NO_DATE", partyRefNoDate);
            }
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/Production/FeltCreditNote/CreditNote_GR.jrxml", 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void PrintGRICN(String voucherNo,String docNo,String strSQL) {
        try {

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();
            //parameterMap.put("RUNDATE", EITLERPGLOBAL.getCurrentDate() + " " + EITLERPGLOBAL.getCurrentTime());
            parameterMap.put("GROSS_AMOUNT",data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+voucherNo+"' AND EFFECT='D' " +
              "AND MAIN_ACCOUNT_CODE='305080'"));
            parameterMap.put("INS_AMOUNT",data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='"+voucherNo+"' AND EFFECT='D' " +
              "AND MAIN_ACCOUNT_CODE='427027'"));
            parameterMap.put("IGST_AMOUNT", data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + voucherNo + "' AND EFFECT='D' "
                    + "AND MAIN_ACCOUNT_CODE='231777'")); //127570
            parameterMap.put("CGST_AMOUNT", data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + voucherNo + "' AND EFFECT='D' "
                    + "AND MAIN_ACCOUNT_CODE='231770'"));  //127566
            parameterMap.put("SGST_AMOUNT", data.getDoubleValueFromDB("SELECT AMOUNT FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + voucherNo + "' AND EFFECT='D' "
                    + "AND MAIN_ACCOUNT_CODE='231771'"));  //127568
            parameterMap.put("INV_NO_DATE", data.getStringValueFromDB("SELECT CONCAT(CN_INVOICE_NO,'   &   ',DATE_FORMAT(CN_INVOICE_DATE,'%d/%m/%Y')) FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='"+docNo+"'"));
            parameterMap.put("IGST_MAIN_CODE","231777");
            parameterMap.put("CGST_MAIN_CODE","231770");
            parameterMap.put("SGST_MAIN_CODE","231771");
            String partyRefNoDate=data.getStringValueFromDB("SELECT CONCAT(PARTY_REF_DOC_NO,'   &   ',DATE_FORMAT(PARTY_REF_DOC_DATE,'%d/%m/%Y')) FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='"+docNo+"'");
            if(partyRefNoDate.contains("00/00/0000")){
            //if(partyRefNoDate.equals("   &   00/00/0000")){
                parameterMap.put("PARTY_REF_NO_DATE","");
            }else{
                parameterMap.put("PARTY_REF_NO_DATE", partyRefNoDate);
            }
            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/Production/FeltCreditNote/CreditNote_GR.jrxml", 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void PrintGRCNDraft(String docNo,String strSQL) {
        try {

            Connection Conn = data.getConn();
            Statement st = Conn.createStatement();

            HashMap parameterMap = new HashMap();
            
            String partyRefNoDate=data.getStringValueFromDB("SELECT CONCAT(PARTY_REF_DOC_NO,'   &   ',DATE_FORMAT(PARTY_REF_DOC_DATE,'%d/%m/%Y')) FROM PRODUCTION.FELT_CN_TEMP_DETAIL WHERE CN_ID='"+docNo+"'");
            if(partyRefNoDate.equals("   &   00/00/0000")){
                parameterMap.put("PARTY_REF_NO_DATE","");
            }else{
                parameterMap.put("PARTY_REF_NO_DATE", partyRefNoDate);
            }

            ReportRegister rpt = new ReportRegister(parameterMap, Conn);

            System.out.println("SQL QUERY : " + strSQL);
            rpt.setReportName("/EITLERP/Production/FeltCreditNote/CreditNoteDraft_GR.jrxml", 1, strSQL);
            rpt.callReport();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GenerateUnadjGSTCN(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);
        
        String frmdt = EITLERPGLOBAL.formatDateDB(fromDate);
        String frmyr = fromDate.substring(6, 10);
        String fyr = frmyr.substring(2, 4);
        System.out.println(frmdt);
        System.out.println(frmyr);
        System.out.println(fyr);
        String todt = EITLERPGLOBAL.formatDateDB(toDate);
        String toyr = toDate.substring(6, 10);
        String tyr = toyr.substring(2, 4);
        System.out.println(todt);
        System.out.println(toyr);
        System.out.println(tyr);
        
        String docId = docNo.substring(0, 2);
        System.out.println(docId);

        try {
            Connection conn = data.getConn();
            Statement stmt = conn.createStatement();

            stmt.execute("TRUNCATE TABLE PRODUCTION.TMP_LC_CREDITNOTETEST");

            stmt.execute("INSERT INTO PRODUCTION.TMP_LC_CREDITNOTETEST (LCN_VOUCHER_NO,LCN_SUB_ACCOUNT_CODE,EXT1,LCN_RC_VOUCHER,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_PER,LCN_CREDIT_AMOUNT,EXT2,LCN_MAIN_ACCOUNT_CODE,LCN_IGST_PER,LCN_IGST_AMT,LCN_CGST_PER,LCN_CGST_AMT,LCN_SGST_PER,LCN_SGST_AMT,EXT3) SELECT H.UNADJ_ID,PARTY_CODE,PARTY_NAME,PRODUCT_CODE,INVOICE_NO,INVOICE_DATE,INV_BASIC_AMT,CASE WHEN WORK_DISC_PER>0 THEN WORK_DISC_PER ELSE SANC_SEAM_CHARGES END AS DISC_PER,TOTAL_DISC_AMT,PIECE_NO,210010,IGST_PER,IGST_AMT,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,DISC_AMT FROM PRODUCTION.FELT_UNADJUSTED_TRN_DETAIL D,PRODUCTION.FELT_UNADJUSTED_TRN_HEADER H WHERE D.UNADJ_ID = H.UNADJ_ID AND H.UNADJ_ID='" + docNo + "'  AND CANCELLED =0 AND APPROVED =1 AND INVOICE_NO NOT IN (SELECT CND_INVOICE_NO FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID LIKE '%" + docId + "%' AND CND_INVOICE_DATE BETWEEN '" + frmdt + "' AND '" + todt + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_YEAR_MON_ID=CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') WHERE LCN_INVOICE_DATE>='" + frmdt + "' AND LCN_INVOICE_DATE<='" + todt + "'");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_DAY = EXTRACT(DAY FROM LCN_INVOICE_DATE), LCN_MONTH =EXTRACT(MONTH FROM LCN_INVOICE_DATE), LCN_YEAR = EXTRACT(YEAR FROM LCN_INVOICE_DATE)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL_CAPTION =CONCAT('FROM ','" + fromDate + "',' TO ','" + toDate + "')");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_INTERVAL =CONCAT('" + frmyr + "','/','" + toyr + "') ");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_NO = CONCAT(LCN_SUB_ACCOUNT_CODE,LCN_YEAR_MON_ID,RIGHT(LCN_INVOICE_NO,4))");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST SET LCN_LINK_NO = CONCAT(RIGHT(LCN_INVOICE_NO,4),'/',LCN_INTERVAL)");

            stmt.execute("UPDATE PRODUCTION.TMP_LC_CREDITNOTETEST,DINESHMILLS.D_SAL_PARTY_MASTER SET LCN_LC_NAME = SUBSTRING(PARTY_NAME,1,18)  WHERE PARTY_CODE = LCN_SUB_ACCOUNT_CODE");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_DETAIL (CND_ID,CND_NO,CND_TYPE,CND_MAIN_ACCOUNT_CODE,CND_SUB_ACCOUNT_CODE,CND_EFFECT,CND_LINK_NO,CND_LINK_YEAR,CND_INVOICE_NO,CND_INVOICE_DATE,CND_INVOICE_AMOUNT,CND_CHARGE_CODE,CND_PERCENT,CND_BOOK_CODE,CND_RC_VOUCHER_NO,CND_RC_VOUCHER_DATE,CND_DAYS,CND_CREDIT_AMOUNT,CND_FIN_VOUCHER_NO,CND_FIN_VOUCHER_DATE,CND_FIN_ELIGIBLE,CND_LC_OPENER,CND_VALUE_DATE,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,CND_CATG,CND_EXT8,CND_EXT9,CND_EXT10,CND_EXT11,CND_EXT12,CND_EXT13,CND_EXT14) SELECT 1,LCN_NO,'UNADJ',LCN_MAIN_ACCOUNT_CODE,LCN_SUB_ACCOUNT_CODE,LCN_EFFECT,LCN_LINK_NO,LCN_INTERVAL,LCN_INVOICE_NO,LCN_INVOICE_DATE,LCN_AMOUNT,LCN_CHARGE_CODE,LCN_PER,LCN_BANK_TR_CODE,LCN_RC_VOUCHER,LCN_RC_VALUE_DATE,LCN_DAY_DIFF,ROUND(LCN_CREDIT_AMOUNT),EXT1,LCN_VOUCHER_DATE,'Y',LCN_LC_OPENER,LCN_RC_VALUE_DATE,LCN_LC_NAME,LCN_YEAR_MON_ID,LCN_INTERVAL_CAPTION,EXT2,LCN_CGST_PER,LCN_SGST_PER,LCN_IGST_PER,LCN_CGST_AMT,LCN_SGST_AMT,LCN_IGST_AMT,ROUND(EXT3) FROM PRODUCTION.TMP_LC_CREDITNOTETEST  WHERE LCN_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ORDER BY LCN_SUB_ACCOUNT_CODE,LCN_INVOICE_DATE,LCN_INVOICE_NO LIMIT 10000000");

            stmt.execute("DELETE FROM PRODUCTION.D_CREDIT_NOTE_HEADER  WHERE CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            stmt.execute("INSERT INTO PRODUCTION.D_CREDIT_NOTE_HEADER (CNH_ID,CNH_NO,CNH_TYPE,CNH_MAIN_ACCOUNT_CODE,CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,CNH_PARTY_NAME,CNH_EFFECT,CNH_INVOICE_AMOUNT,CNH_CREDIT_AMOUNT,CNH_YEAR_MON_ID,CNH_INTERVAL_CAPTION,CNH_BOOK_CODE,CNH_LINK_NO,CNH_REASON_CODE,CNH_REMARKS) SELECT CND_ID,CND_NO,CND_TYPE,435132,210010,CND_SUB_ACCOUNT_CODE,CND_MAIN_ACCOUNT_NAME,'D',SUM(CND_INVOICE_AMOUNT),ROUND(SUM(CND_CREDIT_AMOUNT)),CND_YEAR_MON_ID,CND_INTERVAL_CAPTION,'TR-07',CND_LINK_NO,'10',CONCAT('Unadjusted Discount - ',CND_INTERVAL_CAPTION) FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_YEAR_MON_ID  =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') GROUP BY CND_ID,CND_NO,CND_TYPE,CND_LC_OPENER,CND_MAIN_ACCOUNT_NAME,CND_YEAR_MON_ID,CND_INTERVAL_CAPTION LIMIT 1000000000000");

            stmt.execute("UPDATE DINESHMILLS.D_SAL_PARTY_MASTER,PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_PARTY_NAME = PARTY_NAME,CNH_CITY = CITY_ID WHERE CNH_SUB_ACCOUNT_CODE = PARTY_CODE AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UnadjGSTCNVoucherPosting(String docNo, String fromDt, String toDt) {
        String fromDate = EITLERPGLOBAL.formatDate(fromDt);
        String toDate = EITLERPGLOBAL.formatDate(toDt);
        
        String frmdt = EITLERPGLOBAL.formatDateDB(fromDate);
        String frmyr = fromDate.substring(6, 10);
        String fyr = frmyr.substring(2, 4);
        System.out.println(frmdt);
        System.out.println(frmyr);
        System.out.println(fyr);
        String todt = EITLERPGLOBAL.formatDateDB(toDate);
        String toyr = toDate.substring(6, 10);
        String tyr = toyr.substring(2, 4);
        System.out.println(todt);
        System.out.println(toyr);
        System.out.println(tyr);

        try {
            Conn = data.getConn();
            Stmt = Conn.createStatement();

            String frmYr = EITLERPGLOBAL.FinFromDateDB.substring(2, 4);
            System.out.println("FIN FROM YEAR : " + frmYr);

            if (data.IsRecordExist("SELECT VOUCHER_NO FROM D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN'", FinanceGlobal.FinURL)) {
                rsResultSet = Stmt.executeQuery("SELECT VOUCHER_NO,SUBSTRING(VOUCHER_NO,1,6) AS CNV,RIGHT(VOUCHER_NO,5) AS CNSRNO FROM (SELECT MAX(VOUCHER_NO) AS VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + "  AND LEFT(VOUCHER_NO,2) = 'CN' AND SUBSTRING(VOUCHER_NO,5,2)='07' AND SUBSTRING(VOUCHER_NO,3,2)='" + fyr + "' ) AS A");
                String LastDocNo = "";
                String LastcnvNo = "";
                int draftSrNo = 0;

                rsResultSet.first();
                while (!rsResultSet.isAfterLast() && rsResultSet.getRow() > 0) {
                    LastDocNo = rsResultSet.getString("VOUCHER_NO");
                    LastcnvNo = rsResultSet.getString("CNV");
                    draftSrNo = rsResultSet.getInt("CNSRNO");

                    rsResultSet.next();
                }

                String draftNo = "";
//                String draftDate = EITLERPGLOBAL.formatDateDB(toDate);
                String draftDate = EITLERPGLOBAL.getCurrentDateDB();
                String linkNo = "8101";
                int lnk = Integer.parseInt(linkNo);
                String linkSrNo = "";

                int counter = 1;
                ResultSet rsSchemeHeader = Stmt.executeQuery("SELECT CNH_NO,CNH_SUB_ACCOUNT_CODE,CNH_CREDIT_AMOUNT FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_TYPE ='UNADJ' ORDER BY CNH_SUB_ACCOUNT_CODE,CNH_NO");

                rsSchemeHeader.first();
                if (rsSchemeHeader.getRow() > 0) {
                    while (!rsSchemeHeader.isAfterLast()) {
//                        System.out.println("SUB_AC_CD : " + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE"));
                        System.out.println("CNH_NO : " + rsSchemeHeader.getString("CNH_NO"));

                        Statement st2 = Conn.createStatement();
                        String SQL1 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_DRAFT_CR_NOTE_NO='" + draftNo + (draftSrNo + counter) + "',CNH_DRAFT_CR_NOTE_DATE='" + draftDate + "' WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' AND CNH_NO='" + rsSchemeHeader.getString("CNH_NO") + "' ";
                        System.out.println("SQL 1 : " + SQL1);
                        st2.execute(SQL1);

                        String SQL2 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_FIN_VOUCHER_NO = CONCAT('CN','" + fyr + "','0',700000 +ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))),CNH_LEGACY_NO = ABS(REPLACE(RIGHT( CNH_DRAFT_CR_NOTE_NO,4),' ',''))  WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' AND CNH_NO='" + rsSchemeHeader.getString("CNH_NO") + "' ";
                        System.out.println("SQL 2 : " + SQL2);
                        st2.execute(SQL2);

                        String SQL4 = "UPDATE PRODUCTION.D_CREDIT_NOTE_DETAIL D,PRODUCTION.D_CREDIT_NOTE_HEADER H SET D.CND_FIN_VOUCHER_NO = H.CNH_FIN_VOUCHER_NO  WHERE D.CND_YEAR_MON_ID=H.CNH_YEAR_MON_ID AND CND_TYPE ='UNADJ' AND CND_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CND_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "'";//AND CNH_PERCENT='"+rsSchemeHeader.getString("CNH_PERCENT")+"'
                        System.out.println("SQL 4 : " + SQL4);
                        st2.execute(SQL4);

                        NumWord num = new NumWord();
                        String rsInWord = num.convertNumToWord(Math.round(rsSchemeHeader.getDouble("CNH_CREDIT_AMOUNT")));

                        String SQL3 = "UPDATE PRODUCTION.D_CREDIT_NOTE_HEADER SET CNH_EXT11 = '" + rsInWord + "' WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID =CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') AND CNH_SUB_ACCOUNT_CODE='" + rsSchemeHeader.getString("CNH_SUB_ACCOUNT_CODE") + "' AND CNH_NO='" + rsSchemeHeader.getString("CNH_NO") + "' ";
                        System.out.println("SQL 3 : " + SQL3);
                        st2.execute(SQL3);

                        rsSchemeHeader.next();
                        counter++;
                        lnk++;
                    }
                }
            }            
            
            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_HEADER");

            Stmt.execute("INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_HEADER (COMPANY_ID,VOUCHER_NO,BOOK_CODE,BANK_NAME,HIERARCHY_ID,REMARKS,VOUCHER_DATE,VOUCHER_TYPE,EXCLUDE_IN_ADJ,LEGACY_NO,LEGACY_DATE,REASON_CODE) SELECT 2,CNH_FIN_VOUCHER_NO,'07','DISCOUNT JOURNAL',1646,CNH_REMARKS,CNH_DRAFT_CR_NOTE_DATE,7,1,CNH_LEGACY_NO,CNH_DRAFT_CR_NOTE_DATE,CNH_REASON_CODE FROM PRODUCTION.D_CREDIT_NOTE_HEADER WHERE CNH_TYPE ='UNADJ' AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL");

            String detailInsertQuery = "";

            rsResultSet1 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");
            double cgstPer = 0, sgstPer = 0, igstPer = 0;
            double cgstAmt = 0, sgstAmt = 0, igstAmt = 0;
            int srno = 0;
            rsResultSet1.first();
            while (!rsResultSet1.isAfterLast() && rsResultSet1.getRow() > 0) {
                cgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT8"));
                sgstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT9"));
                igstPer = Double.parseDouble(rsResultSet1.getString("CND_EXT10"));
                cgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT11"));
                sgstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT12"));
                igstAmt = Double.parseDouble(rsResultSet1.getString("CND_EXT13"));

                srno = 1;
                detailInsertQuery = "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,"
                        + "SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";

                detailInsertQuery += "SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,"
                        + "CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                srno = srno + 1;
                detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',CNH_MAIN_ACCOUNT_CODE,'',"
                        + "CND_EXT14,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                if (igstPer == 12) {
                    srno = srno + 1;
                    detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',127570,'',"
                            + "CND_EXT13,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                } else {
                    srno = srno + 1;
                    detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',127566,'',"
                            + "CND_EXT11,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                    srno = srno + 1;
                    detailInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srno + ",'D',127568,'',"
                            + "CND_EXT12,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                }
                rsResultSet1.next();
            }
            System.out.println(detailInsertQuery);
            Stmt.execute(detailInsertQuery);

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX");

            String detailEXInsertQuery = "";

            rsResultSet2 = Stmt.executeQuery("SELECT * FROM PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CND_TYPE='UNADJ' AND CND_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "')");
//            double cgstPer = 0, sgstPer = 0, igstPer = 0;
//            double cgstAmt = 0, sgstAmt = 0, igstAmt = 0;
            int srnoEX = 0;
            rsResultSet2.first();
            while (!rsResultSet2.isAfterLast() && rsResultSet2.getRow() > 0) {
                cgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT8"));
                sgstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT9"));
                igstPer = Double.parseDouble(rsResultSet2.getString("CND_EXT10"));
                cgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT11"));
                sgstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT12"));
                igstAmt = Double.parseDouble(rsResultSet2.getString("CND_EXT13"));

                srnoEX = 1;
                detailEXInsertQuery = "INSERT INTO TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX (COMPANY_ID,VOUCHER_NO,SR_NO,EFFECT,MAIN_ACCOUNT_CODE,"
                        + "SUB_ACCOUNT_CODE,AMOUNT,MODULE_ID,REF_COMPANY_ID,IS_DEDUCTION,VALUE_DATE,LINK_NO) ";

                detailEXInsertQuery += "SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'C',CNH_MAIN_CODE,CNH_SUB_ACCOUNT_CODE,"
                        + "CNH_CREDIT_AMOUNT,70,2,0,CNH_DRAFT_CR_NOTE_DATE,CNH_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                srnoEX = srnoEX + 1;
                detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',CNH_MAIN_ACCOUNT_CODE,'',"
                        + "CND_EXT14,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                        + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                        + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                        + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                if (igstPer == 12) {
                    srnoEX = srnoEX + 1;
                    detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',127570,'',"
                            + "CND_EXT13,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                } else {
                    srnoEX = srnoEX + 1;
                    detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',127566,'',"
                            + "CND_EXT11,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";

                    srnoEX = srnoEX + 1;
                    detailEXInsertQuery += " UNION ALL SELECT 2,CNH_FIN_VOUCHER_NO," + srnoEX + ",'D',127568,'',"
                            + "CND_EXT12,0,0,0,CNH_DRAFT_CR_NOTE_DATE,CND_LINK_NO "
                            + "FROM PRODUCTION.D_CREDIT_NOTE_HEADER H,PRODUCTION.D_CREDIT_NOTE_DETAIL WHERE CNH_TYPE ='UNADJ' "
                            + "AND CNH_YEAR_MON_ID = CONCAT('UNADJ','" + fyr + "','" + tyr + "','" + docNo + "') "
                            + "AND CNH_TYPE = CND_TYPE AND CND_NO = CNH_NO AND CNH_YEAR_MON_ID = CND_YEAR_MON_ID  ";
                }
                rsResultSet2.next();
            }
            System.out.println(detailEXInsertQuery);
            Stmt.execute(detailEXInsertQuery);

            Stmt.execute("TRUNCATE TABLE TEMP_DATABASE.L_COM_DOC_DATA");

            Stmt.execute("INSERT INTO  TEMP_DATABASE.L_COM_DOC_DATA SELECT 2,70,VOUCHER_NO,NOW(),USER_ID,CASE WHEN CREATOR =1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'','0000-00-00','0000-00-00',0,'0000-00-00'  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,TEMP_DATABASE.L_FIN_VOUCHER_HEADER B WHERE B.HIERARCHY_ID =1646 AND A.HIERARCHY_ID = B.HIERARCHY_ID ");

            String sqlH = "INSERT INTO FINANCE.D_FIN_VOUCHER_HEADER SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_HEADER";
            Stmt.execute(sqlH);

            String sqlD = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL";
            Stmt.execute(sqlD);

            String sqlE = "INSERT INTO FINANCE.D_FIN_VOUCHER_DETAIL_EX SELECT * FROM TEMP_DATABASE.L_FIN_VOUCHER_DETAIL_EX";
            Stmt.execute(sqlE);

            String sqlDoc = "INSERT INTO DINESHMILLS.D_COM_DOC_DATA SELECT * FROM TEMP_DATABASE.L_COM_DOC_DATA";
            Stmt.execute(sqlDoc);

        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }


}
