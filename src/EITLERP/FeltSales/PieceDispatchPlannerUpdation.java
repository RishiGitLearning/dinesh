/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.FeltInvReport.clsFeltSalesInvoice;
import EITLERP.FeltSales.common.clsOrderValueCalc;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsVoucher;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author root
 */
public class PieceDispatchPlannerUpdation {

    public static EITLERP.FeltSales.common.FeltInvCalc inv_calculation;

    public static void main(String[] args) {
        UpdationProcess();
    }

    public static void UpdationProcess() {

        String AsOnDate = EITLERPGLOBAL.getCurrentDateDB();
        String MainCode = "210010";
        String upSQL = "";
        int cnt = 0;
        int Counter = 0;

        ResultSet rsPartyCode = null, rsBaleDetail = null, rsPieceDetail = null, rsUpdatePiece = null, rsProdDetail = null, rsDetail = null, rsUpdateDisc = null;

        data.Execute("TRUNCATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA");

        try {
            System.out.println("START OF PROCESS");
            System.out.println("----------------------------------------------------------");

            if (data.IsRecordExist("SELECT SELECTION_DATE FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE()")) {
                data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA (PIECE_NO,PARTY_CODE,PIECE_STAGE_ORIGINAL,DISCOUNT_CHECK,CHARGE_CODE_CHECK) SELECT PIECE_NO,PARTY_CODE,PIECE_STAGE,DISCOUNT_CHECK,CHARGE_CODE_CHECK FROM PRODUCTION.FELT_DISPATCH_SELECTION_DATA WHERE SELECTION_DATE=CURDATE() ");
                cnt++;
            }

            if (cnt > 0) {

//                        System.out.println("START : Piece Deail Updation");
                upSQL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA D, PRODUCTION.FELT_SALES_PIECE_REGISTER R "
                        + "SET MACHINE_NO=PR_MACHINE_NO, POSITION_NO=PR_POSITION_NO, PRODUCT_CODE=PR_BILL_PRODUCT_CODE, GROUP_NAME=PR_GROUP,"
                        + "STYLE=PR_BILL_STYLE, LENGTH=PR_LENGTH, WIDTH=PR_WIDTH, GSM=PR_BILL_GSM, THORITICAL_WEIGHT=PR_THORITICAL_WEIGHT,"
                        + "SQMTR=PR_BILL_SQMTR, SYN_PER=PR_SYN_PER, ACTUAL_WEIGHT=PR_BILL_WEIGHT, ACTUAL_LENGTH=PR_BILL_LENGTH, ACTUAL_WIDTH=PR_BILL_WIDTH,"
                        + "MATERIAL_CODE=PR_MATERIAL_CODE, PIECE_STAGE=PR_PIECE_STAGE, FINISHING_DATE=PR_FNSG_DATE, PIECE_UPN=PR_UPN, INCHARGE_AREA=PR_INCHARGE "
                        + "WHERE PIECE_NO=PR_PIECE_NO ";
                data.Execute(upSQL);

                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA , PRODUCTION.FELT_INCHARGE SET INCHARGE_AREA=INCHARGE_NAME WHERE INCHARGE_AREA=INCHARGE_CD");

                upSQL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA "
                        + "SET CHECK_POINT_REMARK='PIECE NOT FINISHED YET', PIECE_STATUS='UNCLEAR' "
                        + "WHERE COALESCE(FINISHING_DATE,'0000-00-00')='0000-00-00' ";
                data.Execute(upSQL);

                upSQL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA P, "
                        + "PRODUCTION.FELT_PKG_SLIP_DETAIL D,PRODUCTION.FELT_PKG_SLIP_HEADER H "
                        + "SET P.PACKING_NO=H.PKG_DP_NO, P.PACKING_DATE=H.PKG_DP_DATE, P.BALE_NO=COALESCE(H.PKG_BALE_NO,''), P.BALE_DATE=COALESCE(H.PKG_BALE_DATE,'0000-00-00') "
                        + "WHERE D.PKG_DP_NO=H.PKG_DP_NO AND D.PKG_DP_DATE=H.PKG_DP_DATE AND P.PIECE_NO=D.PKG_PIECE_NO AND COALESCE(H.BALE_REOPEN_FLG,0)=0 "
                        + "AND P.CHECK_POINT_REMARK='' ";
                data.Execute(upSQL);
//                        System.out.println("END : Piece Deail Updation");

                upSQL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA "
                        + "SET CHECK_POINT_REMARK='PACKING SLIP NOT CREATED', PIECE_STATUS='UNCLEAR' "
                        + "WHERE CHECK_POINT_REMARK='' AND COALESCE(PACKING_NO,'')='' ";
                data.Execute(upSQL);

                upSQL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA "
                        + "SET CHECK_POINT_REMARK='PENDING FINAL APPROVAL IN PACKING SLIP', PIECE_STATUS='UNCLEAR' "
                        + "WHERE CHECK_POINT_REMARK='' AND COALESCE(BALE_NO,'')='' ";
                data.Execute(upSQL);

//------------------------------------------------------------------------------
                //PARTY DATA UPDATION
                System.out.println("START : Party Data Updation");
                String upPartyData = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_NAME=D.PARTY_NAME,I.CHARGE_CODE=D.CHARGE_CODE,I.PARTY_CHARGE_CODE=D.CHARGE_CODE,I.ADDRESS1=D.ADDRESS1,I.ADDRESS2=D.ADDRESS2,I.PINCODE=D.PINCODE,I.CITY_ID=D.CITY_ID,I.DISPATCH_STATION=D.DISPATCH_STATION,I.CITY_NAME=D.CITY_NAME,I.DOCUMENT_THROUGH=D.DOCUMENT_THROUGH,I.CREDIT_DAYS=D.CREDIT_DAYS,I.GSTIN_NO=CASE WHEN D.GSTIN_NO='' THEN D.STATE_GST_CODE ELSE TRIM(D.GSTIN_NO) END,I.DELIVERY_MODE=CASE WHEN D.DELIVERY_MODE = 'Select Delivery Mode' THEN '' ELSE D.DELIVERY_MODE END ,I.MOBILE_NO=D.MOBILE_NO WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND D.APPROVED=1 AND D.CANCELLED=0 ";
                data.Execute(upPartyData);
                System.out.println("END : Party Data Updation");
//----------------------------------------------

                //PARTY GROUP DATA UPDATION
                System.out.println("START : Party Group Data Updation");
                String upPartyGrpData = "UPDATE PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D, PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA P SET P.PARTY_GROUP_CODE=H.GROUP_CODE,P.PARTY_GROUP_NAME=H.GROUP_DESC WHERE H.GROUP_CODE=D.GROUP_CODE AND D.PARTY_CODE=P.PARTY_CODE AND H.APPROVED=1 AND H.CANCELED=0";
                data.Execute(upPartyGrpData);
                System.out.println("END : Party Group Data Updation");
//----------------------------------------------

//                //INCHARGE AREA UPDATION
//                System.out.println("START : Incharge Area Updation");
//                String upPartyGrpData = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA P SET P.PARTY_GROUP_CODE=H.GROUP_CODE,P.PARTY_GROUP_NAME=H.GROUP_DESC WHERE H.GROUP_CODE=D.GROUP_CODE AND D.PARTY_CODE=P.PARTY_CODE AND H.APPROVED=1 AND H.CANCELED=0";
//                data.Execute(upPartyGrpData);
//                System.out.println("END : Incharge Area Updation");
//----------------------------------------------
                System.out.println("START : Packing Slip Pending");
                String PSDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='PENDING FINAL APPROVAL IN PACKING SLIP' ";
                ResultSet rsPSDetail = data.getResult(PSDetail);

                rsPSDetail.first();
                if (rsPSDetail.getRow() > 0) {
                    while (!rsPSDetail.isAfterLast()) {
                        String uID = data.getStringValueFromDB("SELECT USER_NAME FROM PRODUCTION.FELT_PROD_DOC_DATA D, DINESHMILLS.D_COM_USER_MASTER U WHERE DOC_NO='" + rsPSDetail.getString("PACKING_NO") + "' AND MODULE_ID=715 AND STATUS='W' AND D.USER_ID=U.USER_ID");
                        data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PACKING_PENDING_USER='" + uID + "' WHERE PACKING_NO='" + rsPSDetail.getString("PACKING_NO") + "'");
                        rsPSDetail.next();
                    }
                }
                System.out.println("END : Packing Slip Pending");

                upSQL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA D, PRODUCTION.FELT_SALES_PIECE_REGISTER R "
                        + "SET CHECK_POINT_REMARK='PIECE INVOICED', INVOICE_NO=PR_INVOICE_NO, INVOICE_DATE=PR_INVOICE_DATE, INVOICE_AMT=PR_INVOICE_AMOUNT, PIECE_STATUS='INVOICED'  "
                        + "WHERE PIECE_NO=PR_PIECE_NO AND PIECE_STAGE IN ('INVOICED','EXP-INVOICE') ";
                data.Execute(upSQL);

//-------------------------------------------------------------------                        
                System.out.println("START : Invoice Value Calculation");
                //---------------------------------------------------
//                String Detail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                String Detail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ";
                rsDetail = data.getResult(Detail);

                rsDetail.first();
                if (rsDetail.getRow() > 0) {
                    while (!rsDetail.isAfterLast()) {

                        Counter++;

                        //lblStatus.setText("Party : "+PartyCode);
                        String pieceNo = rsDetail.getString("PIECE_NO");
                        String prodCd = rsDetail.getString("PRODUCT_CODE");
                        String partyCd = rsDetail.getString("PARTY_CODE");
                        String baleNo = rsDetail.getString("BALE_NO");
                        String baleDate = rsDetail.getString("PACKING_DATE");
                        float length = rsDetail.getFloat("ACTUAL_LENGTH");
                        float width = rsDetail.getFloat("ACTUAL_WIDTH");
                        float gsm = rsDetail.getFloat("GSM");
                        float weight = rsDetail.getFloat("ACTUAL_WEIGHT");
                        float sqmtr = rsDetail.getFloat("SQMTR");

                        inv_calculation = EITLERP.FeltSales.common.clsOrderValueCalc.calculate(pieceNo, prodCd, partyCd, length, width, weight, sqmtr, AsOnDate, baleNo, baleDate);

                        if (inv_calculation.getReason().equals("")) {
                            float Rate = inv_calculation.getFicRate();
                            float BasAmount = inv_calculation.getFicBasAmount();
                            float ChemTrtChg = inv_calculation.getFicChemTrtChg();
                            float SpiralChg = inv_calculation.getFicSpiralChg();
                            float PinChg = inv_calculation.getFicPinChg();
                            float SeamChg = inv_calculation.getFicSeamChg();
                            int InsInd = inv_calculation.getFicInsInd();
                            float InsAmt = inv_calculation.getFicInsAmt();
                            float Excise = inv_calculation.getFicExcise();
                            float DiscPer = inv_calculation.getFicDiscPer();
                            float DiscAmt = inv_calculation.getFicDiscAmt();
                            float DiscBasamt = inv_calculation.getFicDiscBasamt();
                            float InvAmt = inv_calculation.getFicInvAmt();
                            float Gst = inv_calculation.getFicGST();
                            float IGst = inv_calculation.getFicIGST();
                            float SGst = inv_calculation.getFicSGST();
                            float CGst = inv_calculation.getFicCGST();
                            float IGstper = inv_calculation.getFicIGSTPER();
                            float CGstper = inv_calculation.getFicCGSTPER();
                            float SGstper = inv_calculation.getFicSGSTPER();
                            //float cst = inv_calculation.getCst();
                            float cst2 = inv_calculation.getCst2();
                            float cst5 = inv_calculation.getCst5();
                            //float vat = inv_calculation.getVat();
                            float vat1 = inv_calculation.getVat1();
                            float vat4 = inv_calculation.getVat4();
                            float SD = inv_calculation.getSD();

                            String SancDoc = inv_calculation.getSanc_doc();
                            String SancGrp = inv_calculation.getSanc_group();

                            float aosd_per = inv_calculation.getAosd_per();
                            float aosd_amt = inv_calculation.getAosd_amt();

                            float surcharge_per = inv_calculation.getFicSurcharge_per();
                            float surcharge_rate = inv_calculation.getFicSurcharge_rate();
                            float gross_rate = inv_calculation.getFicGrossRate();

                            float tcs_per = inv_calculation.getTCS_per();
                            float tcs_amt = inv_calculation.getTCS_amt();
//                                
                            upSQL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET RATE='" + Rate + "',BAS_AMT='" + BasAmount + "',DISC_PER='" + DiscPer + "',DISC_AMT='" + DiscAmt + "',DISC_BAS_AMT='" + DiscBasamt + "',EXCISE='" + Excise + "',SEAM_CHG='" + SeamChg + "',INSURANCE_AMT='" + InsAmt + "',CHEM_TRT_CHG='" + ChemTrtChg + "',PIN_CHG='" + PinChg + "',SPIRAL_CHG='" + SpiralChg + "',INS_IND='" + InsInd + "',CST2='" + cst2 + "',VAT1='" + vat1 + "',CST5='" + cst5 + "',VAT4='" + vat4 + "',SD_AMT='" + SD + "',IGST_AMT='" + IGst + "',CGST_AMT='" + CGst + "',SGST_AMT='" + SGst + "',IGST_PER='" + IGstper + "',CGST_PER='" + CGstper + "',SGST_PER='" + SGstper + "',INVOICE_AMT='" + InvAmt + "',SANC_GROUP='" + SancGrp + "',SANC_DOC='" + SancDoc + "',AOSD_PER='" + aosd_per + "',AOSD_AMT='" + aosd_amt + "',SURCHARGE_PER='" + surcharge_per + "',SURCHARGE_RATE='" + surcharge_rate + "',GROSS_RATE='" + gross_rate + "',TCS_PER='" + tcs_per + "',TCS_AMT='" + tcs_amt + "' WHERE PIECE_NO='" + pieceNo + "' ";
                            data.Execute(upSQL);
                        }

                        String chargeCd = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                        String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHARGE_CODE='" + chargeCd + "' WHERE PARTY_CODE = '" + partyCd + "' ";
                        data.Execute(upSQL1);

                        rsDetail.next();
                    }
                }
                System.out.println("END : Invoice Value Calculation");

//-------------------------------------------------------------------
                System.out.println("START : Checking of Party/Machine/Position Closure");
//                String closureDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY BALE_NO";
                String closureDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY BALE_NO";
                ResultSet rsclosureDetail = data.getResult(closureDetail);

                rsclosureDetail.first();
                if (rsclosureDetail.getRow() > 0) {
                    while (!rsclosureDetail.isAfterLast()) {
                        String baleNo = rsclosureDetail.getString("BALE_NO");
                        String partyCd = rsclosureDetail.getString("PARTY_CODE");
                        String machineNo = rsclosureDetail.getString("MACHINE_NO");
                        String positionNo = rsclosureDetail.getString("POSITION_NO");

                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + partyCd + "' ")) {
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PARTY CLOSED IN PARTY MASTER'),PARTY_CLOSED_REMARK='PARTY CLOSED IN PARTY MASTER',PIECE_STATUS='UNCLEAR' WHERE BALE_NO='" + baleNo + "' ");
                        } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCd + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PARTY MACHINE CLOSED IN MACHINE MASTER'),PARTY_CLOSED_REMARK='PARTY MACHINE CLOSED IN MACHINE MASTER',PIECE_STATUS='UNCLEAR' WHERE BALE_NO='" + baleNo + "' ");
                        } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCd + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PARTY MACHINE POSITION CLOSED IN MACHINE MASTER'),PARTY_CLOSED_REMARK='PARTY MACHINE POSITION CLOSED IN MACHINE MASTER',PIECE_STATUS='UNCLEAR' WHERE BALE_NO='" + baleNo + "' ");
                        }

                        rsclosureDetail.next();
                    }
                }
                System.out.println("END : Checking of Party/Machine/Position Closure");

//-------------------------------------------------------------------------
//-------------------------------------------------------------------------                        
                System.out.println("START : Party Master Updation Pending");
//                String PUDetail = "SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY PARTY_CODE";
                String PUDetail = "SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY PARTY_CODE";
                ResultSet rsPUDetail = data.getResult(PUDetail);

                rsPUDetail.first();
                if (rsPUDetail.getRow() > 0) {
                    while (!rsPUDetail.isAfterLast()) {
                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_AMEND_MASTER WHERE APPROVED=0 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + rsPUDetail.getString("PARTY_CODE") + "' ")) {
                            String uID = data.getStringValueFromDB("SELECT USER_NAME FROM DINESHMILLS.D_SAL_PARTY_AMEND_MASTER A, DINESHMILLS.D_COM_DOC_DATA D, DINESHMILLS.D_COM_USER_MASTER U WHERE APPROVED=0 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + rsPUDetail.getString("PARTY_CODE") + "' AND DOC_NO=AMEND_ID AND MODULE_ID=149 AND STATUS='W' AND D.USER_ID=U.USER_ID");
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PENDING FINAL APPROVAL IN PARTY UPDATION'),PARTY_UPDATION_PENDING_USER='" + uID + "',PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + rsPUDetail.getString("PARTY_CODE") + "'");
                        }
                        rsPUDetail.next();
                    }
                }
                System.out.println("END : Party Master Updation Pending");

                System.out.println("START : Discount Master Pending");
//                String DMDetail = "SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY PARTY_CODE";
                String DMDetail = "SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY PARTY_CODE";
                ResultSet rsDMDetail = data.getResult(DMDetail);

                rsDMDetail.first();
                if (rsDMDetail.getRow() > 0) {
                    while (!rsDMDetail.isAfterLast()) {
                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE APPROVED=0 AND CANCELED=0 AND PARTY_CODE='" + rsDMDetail.getString("PARTY_CODE") + "' ")) {
                            String uID = data.getStringValueFromDB("SELECT USER_NAME FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER A, PRODUCTION.FELT_PROD_DOC_DATA D, DINESHMILLS.D_COM_USER_MASTER U WHERE A.APPROVED=0 AND A.CANCELED=0 AND A.PARTY_CODE='" + rsDMDetail.getString("PARTY_CODE") + "' AND D.DOC_NO=A.MASTER_NO AND D.MODULE_ID=730 AND D.STATUS='W' AND D.USER_ID=U.USER_ID");
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PENDING FINAL APPROVAL IN DISC MASTER'),DISCOUNT_UPDATION_PENDING_USER='" + uID + "',PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + rsDMDetail.getString("PARTY_CODE") + "'");
                        }
                        rsDMDetail.next();
                    }
                }
                System.out.println("END : Discount Master Pending");

                System.out.println("START : Checking of Modification Form pending entry");
//                String invmodDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY BALE_NO";
                String invmodDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY BALE_NO";
                ResultSet rsinvmodDetail = data.getResult(invmodDetail);

                rsinvmodDetail.first();
                if (rsinvmodDetail.getRow() > 0) {
                    while (!rsinvmodDetail.isAfterLast()) {
                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND APPROVED=0 AND CANCELED=0 AND BALE_NO='" + rsinvmodDetail.getString("BALE_NO").trim() + "' AND PARTY_CODE='" + rsinvmodDetail.getString("PARTY_CODE").trim() + "' ")) {
                            String dNo = data.getStringValueFromDB("SELECT DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND APPROVED=0 AND CANCELED=0 AND BALE_NO='" + rsinvmodDetail.getString("BALE_NO").trim() + "' AND PARTY_CODE='" + rsinvmodDetail.getString("PARTY_CODE").trim() + "' ");
                            String dDt = data.getStringValueFromDB("SELECT DOC_DATE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND APPROVED=0 AND CANCELED=0 AND BALE_NO='" + rsinvmodDetail.getString("BALE_NO").trim() + "' AND PARTY_CODE='" + rsinvmodDetail.getString("PARTY_CODE").trim() + "' ");
                            String uID = data.getStringValueFromDB("SELECT USER_NAME FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST A, PRODUCTION.FELT_PROD_DOC_DATA D, DINESHMILLS.D_COM_USER_MASTER U WHERE A.APPROVED=0 AND A.CANCELED=0 AND A.PARTY_CODE='" + rsinvmodDetail.getString("PARTY_CODE") + "' AND D.DOC_NO=A.DOC_NO AND D.MODULE_ID=754 AND D.STATUS='W' AND D.USER_ID=U.USER_ID");
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PENDING FINAL APPROVAL IN PARAMETER MODIFICATION'),INVOICE_PARAMETER_MODIFICATION_NO='" + dNo + "',INVOICE_PARAMETER_MODIFICATION_DATE='" + dDt + "',INVOICE_PARAMETER_MODIFICATION_PENDING_USER='" + uID + "',PIECE_STATUS='UNCLEAR' WHERE BALE_NO='" + rsinvmodDetail.getString("BALE_NO").trim() + "' AND PARTY_CODE='" + rsinvmodDetail.getString("PARTY_CODE").trim() + "' ");
                        }
                        rsinvmodDetail.next();
                    }
                }
                System.out.println("END : Checking of Modification Form pending entry");

                System.out.println("START : Checking of Critical Limit Form pending entry");
//                String clmtDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY BALE_NO";
                String clmtDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY BALE_NO";
                ResultSet rsclmtDetail = data.getResult(clmtDetail);

                rsclmtDetail.first();
                if (rsclmtDetail.getRow() > 0) {
                    while (!rsclmtDetail.isAfterLast()) {
                        String baleNo = rsclmtDetail.getString("BALE_NO");
                        String baleDate = rsclmtDetail.getString("PACKING_DATE");
                        String partyCd = rsclmtDetail.getString("PARTY_CODE");

                        String sql = "SELECT H.DOC_NO,H.DOC_DATE,D.PARTY_CODE,H.PROCESSING_DATE,H.APPROVED,H.CANCELED, ";
                        sql += "D.BALE_NO,D.BALE_DATE,E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT ";
                        sql += "FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H, ";
                        sql += "PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL D, ";
                        sql += "PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL_ENHANCEMENT E ";
                        sql += "WHERE H.DOC_NO=D.DOC_NO AND H.DOC_DATE=D.DOC_DATE ";
                        sql += "AND D.PARTY_CODE='" + partyCd + "' AND H.PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' ";
                        sql += "AND D.BALE_NO='" + baleNo + "' AND D.BALE_DATE='" + baleDate + "' ";
                        sql += "AND H.APPROVED=0 AND H.CANCELED=0 ";
                        sql += "AND H.DOC_NO=E.DOC_NO AND H.DOC_DATE=E.DOC_DATE AND D.PARTY_CODE=E.GROUP_PARTY_CODE ";
                        sql += "AND E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT>0 ";
                        sql += "ORDER BY H.DOC_NO DESC ";

                        if (data.IsRecordExist(sql)) {
                            String cNo = data.getStringValueFromDB("SELECT DOC_NO FROM (" + sql + ") AS A");
                            String cDt = data.getStringValueFromDB("SELECT DOC_DATE FROM (" + sql + ") AS A");
                            String uID = data.getStringValueFromDB("SELECT USER_NAME FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER A, PRODUCTION.FELT_PROD_DOC_DATA D, DINESHMILLS.D_COM_USER_MASTER U WHERE A.APPROVED=0 AND A.CANCELED=0 AND D.DOC_NO='" + cNo + "' AND D.MODULE_ID=769 AND D.STATUS='W' AND D.USER_ID=U.USER_ID");
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PENDING FINAL APPROVAL IN GROUP CRITICAL LIMIT ENHANCEMENT'),GROUP_CRITICAL_LIMIT_ENHANCHMENT_NO='" + cNo + "',GROUP_CRITICAL_LIMIT_ENHANCHMENT_DATE='" + cDt + "',GROUP_CRITICAL_LIMIT_ENHANCHMENT_PENDING_USER='" + uID + "',PIECE_STATUS='UNCLEAR' WHERE BALE_NO='" + rsclmtDetail.getString("BALE_NO").trim() + "' AND PARTY_CODE='" + rsclmtDetail.getString("PARTY_CODE").trim() + "' ");
                        }
                        rsclmtDetail.next();
                    }
                }
                System.out.println("END : Checking of Critical Limit Form pending entry");

//-------------------------------------------------------------------  
                ////------------------------------------------------------------------------------------------------------
                //---------------------------------------------------
                //REMARK UPDATION
                ResultSet remark1 = null;
//                String rmkDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                String rmkDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ";
                remark1 = data.getResult(rmkDetail);
                remark1.first();
                if (remark1.getRow() > 0) {
                    while (!remark1.isAfterLast()) {
                        String partyCd = remark1.getString("PARTY_CODE");
                        float GST_PER = remark1.getFloat("CGST_PER") + remark1.getFloat("SGST_PER") + remark1.getFloat("IGST_PER");
                        if (!data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE = '" + partyCd + "'")) {
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PARTY CODE MISSING'),PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + partyCd + "'");
                        } else {
                            if (GST_PER <= 0) {
                                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', GSTIN NO/STATE GST CODE MISSING'),PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + partyCd + "'");
                            }
                        }
                        remark1.next();
                    }
                }
////----------------------------------------------------------------------------------------------------------------------

                //---------------------------------------------------
                //CHARGE CODE UPDATION
                System.out.println("START : Charge Code & Critical Limit Uncheck Updation");
                ResultSet rsChrgCd = null;
                String chrgCdDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                rsChrgCd = data.getResult(chrgCdDetail);
                rsChrgCd.first();
                if (rsChrgCd.getRow() > 0) {
                    while (!rsChrgCd.isAfterLast()) {

                        String partyCd = rsChrgCd.getString("PARTY_CODE");

                        String chargeCd = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                        int transportCd = data.getIntValueFromDB("SELECT TRANSPORTER_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                        String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHARGE_CODE='" + chargeCd + "',TRANSPORTER_CODE='" + transportCd + "' WHERE PARTY_CODE = '" + partyCd + "' ";
//                        String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHARGE_CODE='" + chargeCd + "' WHERE PARTY_CODE = '" + partyCd + "' ";
                        data.Execute(upSQL1);

                        boolean uncheck = data.getBoolValueFromDB("SELECT CRITICAL_LIMIT_UNCHECK FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                        if (uncheck) {
                            double uncheckLimit = Double.valueOf(1000000000.00);
                            String unchkLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT='" + uncheckLimit + "' WHERE PARTY_CODE='" + partyCd + "' ";
                            data.Execute(unchkLimit);
                        }

                        rsChrgCd.next();
                    }
                }
                System.out.println("END : Charge Code & Critical Limit Uncheck Updation");

                //---------------------------------------------------
                //TRANSPORTER CODE UPDATION
                System.out.println("START : Transporter Code Updation");
                ResultSet rsTrnsCd = null;
//                String trnsCdDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY BALE_NO";
                String trnsCdDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY BALE_NO";
                rsTrnsCd = data.getResult(trnsCdDetail);
                rsTrnsCd.first();
                if (rsTrnsCd.getRow() > 0) {
                    while (!rsTrnsCd.isAfterLast()) {

                        String baleNo = rsTrnsCd.getString("BALE_NO");
                        String baleDate = rsTrnsCd.getString("PACKING_DATE");
                        String partyCd = rsTrnsCd.getString("PARTY_CODE");

                        String transMode = data.getStringValueFromDB("SELECT PKG_TRANSPORT_MODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO = '" + baleNo + "' AND PKG_BALE_DATE = '" + baleDate + "' ");
                        if (transMode.equals("BY TRANSPORT")) {
                            int transportCd = data.getIntValueFromDB("SELECT TRANSPORTER_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='02-TR' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (transMode.equals("BY ANGADIA")) {
                            int transportCd = Integer.parseInt("95");
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='01-ANG' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (transMode.equals("BY TRAIN")) {
                            int transportCd = Integer.parseInt("97");
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='03-PT' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (transMode.equals("BY IRPP")) {
                            int transportCd = Integer.parseInt("41");
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='04-IRP' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (transMode.equals("BY HANDDELIVERY")) {
                            int transportCd = Integer.parseInt("98");
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='05-HD' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (transMode.equals("BY AIR")) {
                            int transportCd = Integer.parseInt("99");
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='06-AIR' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }

                        rsTrnsCd.next();
                    }
                }
                System.out.println("END : Transporter Code Updation");

//------------------------------------------------------------------------------
//                        //PARTY PAN AND TCS ELIGIBLE AMOUNT UPDATION
//                        System.out.println("START : Party Pan and TCS Eligible Amount Updation");
//                        //String upPartyTCSData = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,PRODUCTION.TMP_TCS_PARTY_AMOUNT_ELIGIBILITY T SET I.GSTIN_PAN=T.GSTIN_PAN,I.TCS_ELIGIBLE_AMOUNT=CASE WHEN T.AMOUNT_PRE>T.AMOUNT_CUR THEN T.AMOUNT_PRE ELSE T.AMOUNT_CUR END WHERE I.PARTY_CODE=T.PARTY_CODE ";
//                        String upPartyTCSData = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,PRODUCTION.TMP_TCS_PARTY_AMOUNT_ELIGIBILITY T SET I.GSTIN_PAN=T.GSTIN_PAN,I.TCS_ELIGIBLE_AMOUNT=COALESCE(T.AMOUNT_CUR,0) WHERE I.PARTY_CODE=T.PARTY_CODE ";
//                        data.Execute(upPartyTCSData);
//                        System.out.println("END : Party Pan and TCS Eligible Amount Updation");
//----------------------------------------------
                System.out.println("START : Party Pin Code Checking");
//                String PINDetail = "SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY PARTY_CODE ";
                String PINDetail = "SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY PARTY_CODE ";
                ResultSet rsPINDetail = data.getResult(PINDetail);

                rsPINDetail.first();
                if (rsPINDetail.getRow() > 0) {
                    while (!rsPINDetail.isAfterLast()) {
                        if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND LENGTH(PINCODE)<6 AND PARTY_CODE='" + rsPINDetail.getString("PARTY_CODE") + "' ")) {
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CONCAT(CHECK_POINT_REMARK,', PIN CODE MISSING'),PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + rsPINDetail.getString("PARTY_CODE") + "'");
                        }
                        rsPINDetail.next();
                    }
                }
                System.out.println("END : Party Pin Code Checking");

//----------------------------------------------
//------------------------------------------------------------------------------
                //Quality DATA UPDATION
                System.out.println("START : Quality Data Updation");
                String upQltData = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,PRODUCTION.FELT_QLT_RATE_MASTER Q SET I.PRODUCT_DESC=Q.PRODUCT_DESC,I.RATE_UNIT=CASE WHEN Q.SQM_IND=1 THEN 'MTR' ELSE 'KG' END WHERE SUBSTRING(I.PRODUCT_CODE,1,6)=Q.PRODUCT_CODE AND Q.APPROVED=1 AND Q.CANCELED=0 AND Q.EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00') ";
                data.Execute(upQltData);
                System.out.println("END : Quality Data Updation");

//------------------------------------------------------------------------------
                //Machine DATA UPDATION
                System.out.println("START : Machine Data Updation");
                String upMMData = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,PRODUCTION.FELT_MACHINE_POSITION_MST M SET I.POSITION_DESC=M.POSITION_DESC WHERE I.POSITION_NO=M.POSITION_NO ";
                data.Execute(upMMData);
                System.out.println("END : Machine Data Updation");

                //---------------------------------------------------
                //ADVANCE AMOUNT PROCESS
//                System.out.println("START : Advance Amount Updation");
//                String AdvDetail = "SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHARGE_CODE=9 AND COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY PARTY_CODE ";
//                ResultSet rsAdvDetail = data.getResult(AdvDetail);
//
//                rsAdvDetail.first();
//                if (rsAdvDetail.getRow() > 0) {
//                    while (!rsAdvDetail.isAfterLast()) {
//                        String advAmt = "SELECT B.SUB_ACCOUNT_CODE, SUM(B.AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B  "
//                                + "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_DATE<=CURDATE() AND A.VOUCHER_TYPE IN (6,7,8,9) AND B.MAIN_ACCOUNT_CODE=210010 "
//                                + "AND B.SUB_ACCOUNT_CODE = '" + rsAdvDetail.getString("PARTY_CODE") + "' AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 "
//                                + "AND B.EFFECT='C' AND B.INVOICE_NO ='' AND B.MODULE_ID <>65 AND B.GRN_NO ='' AND (B.MATCHED=0 OR B.MATCHED IS NULL) "
//                                + "GROUP BY B.SUB_ACCOUNT_CODE "
//                                + "ORDER BY B.SUB_ACCOUNT_CODE ";
//                        data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET ADV_AMT=" + advAmt + ",09_BALANCE_AMT=" + advAmt + " WHERE PARTY_CODE='" + rsAdvDetail.getString("PARTY_CODE") + "'");
//                        rsAdvDetail.next();
//                    }
//                }
//                System.out.println("END : Advance Amount Updation");
                System.out.println("START : Advance Amount Updation");
                AdvAmt();
                //String upAdvAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS,TEMP_DATABASE.TEMP_ADV_AMT SET ADV_AMT=SUB_ADV_BAL WHERE PARTY_CODE = SUB_PARTY_CODE ";
                String upAdvAmt = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA,PRODUCTION.Z_ADV_AMT SET ADV_AMT=COALESCE(SUB_ADV_BAL,0),09_BALANCE_AMT=COALESCE(SUB_ADV_BAL,0) WHERE PARTY_CODE = SUB_PARTY_CODE AND CHECK_POINT_REMARK='' ";
                data.Execute(upAdvAmt);
                System.out.println("END : Advance Amount Updation");

//                        //---------------------------------------------------
//                        //OUTSTANDING AMOUNT PROCESS
//                        System.out.println("START : OutStanding Amount Updation");
//                        OutStandingAmt();
//                        //String upSubPartyAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS,TEMP_DATABASE.TEMP_OUTSTANDING_AMT SET OUT_STANDING_AMT=SUB_OUTSTANDING_BAL,GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE WHERE PARTY_CODE=SUB_PARTY_CODE";
//                        String upSubPartyAmt = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA,TEMP_DATABASE.TEMP_OUTSTANDING_AMT SET OUT_STANDING_AMT=SUB_OUTSTANDING_BAL,GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE WHERE PARTY_CODE=SUB_PARTY_CODE AND CHECK_POINT_REMARK='' ";
//                        data.Execute(upSubPartyAmt);
//                        //String upGrpPartyAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS,TEMP_DATABASE.TEMP_OUTSTANDING_REPORT SET GRP_OUT_STANDING_AMT=SUB_OUTSTANDING_BAL WHERE GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE";
//                        String upGrpPartyAmt = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA,TEMP_DATABASE.TEMP_OUTSTANDING_REPORT SET GRP_OUT_STANDING_AMT=SUB_OUTSTANDING_BAL WHERE GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE AND CHECK_POINT_REMARK='' ";
//                        data.Execute(upGrpPartyAmt);
//                        System.out.println("END : OutStanding Amount Updation");
                //---------------------------------------------------
                //OUTSTANDING AMOUNT PROCESS added on 11/04/2019
                System.out.println("START : OutStanding Amount Updation");
                OutStandingAmt2();

                String upSubPartyAmt = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA,PRODUCTION.Z_OUTSTANDING_AMT SET OUT_STANDING_AMT=SUB_OUTSTANDING_BAL,PARTY_OUTSTANDING_AMT=SUB_OUTSTANDING_BAL,GRP_CRITICAL_LIMIT=COALESCE(GROUP_CRITICAL_BAL,0),GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE WHERE PARTY_CODE=SUB_PARTY_CODE ";
                data.Execute(upSubPartyAmt);

                String upGrpPartyAmt = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA,PRODUCTION.Z_OUTSTANDING_REPORT SET GRP_OUT_STANDING_AMT=GROUP_OUTSTANDING_BAL,GROUP_OUTSTANDING_AMT=GROUP_OUTSTANDING_BAL WHERE GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE ";
                data.Execute(upGrpPartyAmt);
                System.out.println("END : OutStanding Amount Updation");

//---------------------------------------------------
                //PARTY LIMIT UPDATION
                System.out.println("START : Single Party Limit Updation");
                //String upPartyLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_CRITICAL_LIMIT=D.AMOUNT_LIMIT WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND I.PARTY_CRITICAL_LIMIT=0 AND I.GRP_CRITICAL_LIMIT=0";
                //String upPartyLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_CRITICAL_LIMIT=D.AMOUNT_LIMIT WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND I.PARTY_CRITICAL_LIMIT=0 AND I.GRP_CRITICAL_LIMIT=0";
                String upPartyLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_CRITICAL_LIMIT=D.AMOUNT_LIMIT WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND D.APPROVED=1 AND D.CANCELLED=0 AND I.CHECK_POINT_REMARK='' ";
                data.Execute(upPartyLimit);
                System.out.println("END : Single Party Limit Updation");

//-------------------------------------------------------------------------------                        
                //Invoice Modification Updation
                System.out.println("START : Invoice Modification Updation");
                ResultSet rsInvModCd = null;
                String InvModCdDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY BALE_NO";
                rsInvModCd = data.getResult(InvModCdDetail);
                rsInvModCd.first();
                if (rsInvModCd.getRow() > 0) {
                    while (!rsInvModCd.isAfterLast()) {

                        String baleNo = rsInvModCd.getString("BALE_NO");
                        String baleDate = rsInvModCd.getString("PACKING_DATE");
                        String partyCd = rsInvModCd.getString("PARTY_CODE");

                        String chargCd = data.getStringValueFromDB("SELECT CHARGE_CODE_NEW FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND F6=1");
                        if (chargCd.equals("01")) {
                            double NoLimit = Double.valueOf(1000000000.00);
                            String sql90 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHARGE_CODE='" + chargCd + "',CRITICAL_LIMIT_AMT='" + NoLimit + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql90);
                        }

                        String trnsCd = "";
                        trnsCd = data.getStringValueFromDB("SELECT TRANSPORTER_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND (TRANSPORTER_CODE IS NOT NULL OR TRANSPORTER_CODE!='') ORDER BY DOC_NO DESC");
                        if (!trnsCd.equalsIgnoreCase("")) {
                            String sql91 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TRANSPORTER_CODE='" + trnsCd + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql91);
                        }

                        String vehicalNo = "";
                        vehicalNo = data.getStringValueFromDB("SELECT VEHICLE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND (VEHICLE_NO IS NOT NULL OR VEHICLE_NO!='') ORDER BY DOC_NO DESC");
                        if (!vehicalNo.equalsIgnoreCase("")) {
                            String sql92 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET VEHICLE_NO='" + vehicalNo + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql92);
                        }

                        String advNo = "";
                        advNo = data.getStringValueFromDB("SELECT ADV_DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND (ADV_DOC_NO IS NOT NULL OR ADV_DOC_NO!='') ORDER BY DOC_NO DESC");
                        if (!advNo.equalsIgnoreCase("")) {
                            String sql93 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET ADV_DOC_NO='" + advNo + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql93);
                        }

                        double criticalLimit = 0, InvAmt = 0, IGSTAmt = 0, SGSTAmt = 0, CGSTAmt = 0, GSTCompCessAmt = 0;

                        criticalLimit = data.getDoubleValueFromDB("SELECT CRITICAL_LIMIT_NEW FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND CRITICAL_LIMIT_NEW>0 ORDER BY DOC_NO DESC");
                        if (criticalLimit > 0) {
                            String sql94 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PARTY_CRITICAL_LIMIT='" + criticalLimit + "',CRITICAL_LIMIT_AMT='" + criticalLimit + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql94);
                        }

                        InvAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_INV_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_INV_AMT>0 ORDER BY DOC_NO DESC");
                        if (InvAmt > 0) {
                            String sql95 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET ADV_AGN_INV_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql95);
                        }

                        IGSTAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_IGST_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_IGST_AMT>0 ORDER BY DOC_NO DESC");
                        if (InvAmt > 0) {
                            String sql96 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET ADV_AGN_IGST_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql96);
                        }

                        SGSTAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_SGST_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_SGST_AMT>0 ORDER BY DOC_NO DESC");
                        if (InvAmt > 0) {
                            String sql97 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET ADV_AGN_SGST_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql97);
                        }

                        CGSTAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_CGST_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_CGST_AMT>0 ORDER BY DOC_NO DESC");
                        if (InvAmt > 0) {
                            String sql98 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET ADV_AGN_CGST_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql98);
                        }

                        GSTCompCessAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_GST_COMP_CESS_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_GST_COMP_CESS_AMT>0 ORDER BY DOC_NO DESC");
                        if (GSTCompCessAmt > 0) {
                            String sql99 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET ADV_AGN_GST_COMP_CESS_AMT='" + GSTCompCessAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sql99);
                        }

                        if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND WITHOUT_CRITICAL_LIMIT=1")) {
                            double noLimit = Double.valueOf(1000000000.00);
                            String upLimitUp = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT='" + noLimit + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(upLimitUp);
                        }

                        rsInvModCd.next();
                    }
                }
                System.out.println("END : Invoice Modification Updation");
//-------------------------------------------------------------------------------                

//-------------------------------------------------------------------------------                        
                //PDC Party Updation
                System.out.println("START : PDC Party Updation");
                ResultSet rsPDC = null;
                String PDCDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') AND CHARGE_CODE='08' ORDER BY BALE_NO";
                rsPDC = data.getResult(PDCDetail);
                rsPDC.first();
                if (rsPDC.getRow() > 0) {
                    while (!rsPDC.isAfterLast()) {

                        String pcNo = rsPDC.getString("PIECE_NO");
                        String partyCd = rsPDC.getString("PARTY_CODE");

                        if (data.IsRecordExist("SELECT PDC_PARTY_CODE,PDC_PIECE_NO,PDC_BILLING_DATE FROM PRODUCTION.FELT_PDC_HEADER H,PRODUCTION.FELT_PDC_BANK_DETAIL B,PRODUCTION.FELT_PDC_PIECE_DETAIL P WHERE H.PDC_DOC_NO=P.PDC_DOC_NO AND H.PDC_DOC_NO=B.PDC_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0 AND P.PDC_PIECE_STATUS IN ('INSERT','ADD') AND H.PDC_PARTY_CODE='" + partyCd + "' AND P.PDC_PIECE_NO='" + pcNo + "' AND B.PDC_BILLING_DATE<=CURDATE() ORDER BY PDC_PIECE_NO,PDC_BILLING_DATE DESC")) {
                            double noLimit = Double.valueOf(1000000000.00);
                            String upLimitUp = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT='" + noLimit + "',CHECK_POINT_REMARK='',PIECE_STATUS='' WHERE PARTY_CODE='" + partyCd + "' AND PIECE_NO='" + pcNo + "' ";
                            data.Execute(upLimitUp);
                        } else if (data.IsRecordExist("SELECT PDC_PARTY_CODE,PDC_PIECE_NO,PDC_BILLING_DATE FROM PRODUCTION.FELT_PDC_HEADER H,PRODUCTION.FELT_PDC_BANK_DETAIL B,PRODUCTION.FELT_PDC_PIECE_DETAIL P WHERE H.PDC_DOC_NO=P.PDC_DOC_NO AND H.PDC_DOC_NO=B.PDC_DOC_NO AND H.APPROVED=0 AND H.CANCELED=0 AND P.PDC_PIECE_STATUS IN ('INSERT','ADD') AND H.PDC_PARTY_CODE='" + partyCd + "' AND P.PDC_PIECE_NO='" + pcNo + "' AND B.PDC_BILLING_DATE<=CURDATE() ORDER BY PDC_PIECE_NO,PDC_BILLING_DATE DESC")) {
                            String pdcNo = data.getStringValueFromDB("SELECT H.PDC_DOC_NO FROM PRODUCTION.FELT_PDC_HEADER H,PRODUCTION.FELT_PDC_BANK_DETAIL B,PRODUCTION.FELT_PDC_PIECE_DETAIL P WHERE H.PDC_DOC_NO=P.PDC_DOC_NO AND H.PDC_DOC_NO=B.PDC_DOC_NO AND H.APPROVED=0 AND H.CANCELED=0 AND P.PDC_PIECE_STATUS IN ('INSERT','ADD') AND H.PDC_PARTY_CODE='" + partyCd + "' AND P.PDC_PIECE_NO='" + pcNo + "' AND B.PDC_BILLING_DATE<=CURDATE() ORDER BY PDC_PIECE_NO,PDC_BILLING_DATE DESC");
                            String pdcDt = data.getStringValueFromDB("SELECT H.PDC_DOC_DATE FROM PRODUCTION.FELT_PDC_HEADER H,PRODUCTION.FELT_PDC_BANK_DETAIL B,PRODUCTION.FELT_PDC_PIECE_DETAIL P WHERE H.PDC_DOC_NO=P.PDC_DOC_NO AND H.PDC_DOC_NO=B.PDC_DOC_NO AND H.APPROVED=0 AND H.CANCELED=0 AND P.PDC_PIECE_STATUS IN ('INSERT','ADD') AND H.PDC_PARTY_CODE='" + partyCd + "' AND P.PDC_PIECE_NO='" + pcNo + "' AND B.PDC_BILLING_DATE<=CURDATE() ORDER BY PDC_PIECE_NO,PDC_BILLING_DATE DESC");
                            String uID = data.getStringValueFromDB("SELECT USER_NAME FROM PRODUCTION.FELT_PDC_HEADER A, PRODUCTION.FELT_PROD_DOC_DATA D, DINESHMILLS.D_COM_USER_MASTER U WHERE A.APPROVED=0 AND A.CANCELED=0 AND D.DOC_NO='" + pdcNo + "' AND D.MODULE_ID=625 AND D.STATUS='W' AND D.USER_ID=U.USER_ID");
                            String upPending = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK='PENDING FINAL APPROVAL IN PDC',PDC_NO='" + pdcNo + "',PDC_DATE='" + pdcDt + "',PDC_PENDING_USER='" + uID + "',PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + partyCd + "' AND PIECE_NO='" + pcNo + "' ";
                            data.Execute(upPending);
                        } else {
                            String upLimitUp = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK='PDC Not Found',PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + partyCd + "' AND PIECE_NO='" + pcNo + "' ";
                            data.Execute(upLimitUp);
                        }

                        rsPDC.next();
                    }
                }
                System.out.println("END : PDC Party Updation");
//-------------------------------------------------------------------------------   

////------------------------------------------------------------------------------------------------------
                //---------------------------------------------------
                //REMARK UPDATION
//                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK = 'TRANSPORTER CODE MISSING',PIECE_STATUS='UNCLEAR' WHERE TRANSPORTER_CODE=0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ");
                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK = CONCAT(CHECK_POINT_REMARK,', TRANSPORTER CODE MISSING'),PIECE_STATUS='UNCLEAR' WHERE TRANSPORTER_CODE=0 AND COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ");

////----------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------
                //Transporter Name DATA UPDATION
                System.out.println("START : Transporter Name Data Updation");
                String upTRData = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,DINESHMILLS.D_SAL_TRANSPORTER_MASTER T SET I.TRANSPORTER_NAME=T.TRANSPORTER_NAME WHERE I.TRANSPORTER_CODE=T.TRANSPORTER_ID AND T.COMPANY_ID=2 AND I.CHECK_POINT_REMARK='' AND COALESCE(I.PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                data.Execute(upTRData);
                System.out.println("END : Transporter Name Data Updation");

//------------------------------------------------------------------------------
                //LC PARTY UPDATION
                System.out.println("START : LC Party DATA Updation");
                ResultSet rsLCmst = null;
                String LCmstDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHARGE_CODE='07' AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                rsLCmst = data.getResult(LCmstDetail);
                rsLCmst.first();
                if (rsLCmst.getRow() > 0) {
                    while (!rsLCmst.isAfterLast()) {

                        String baleNo = rsLCmst.getString("BALE_NO");
                        String baleDate = rsLCmst.getString("PACKING_DATE");
                        String partyCd = rsLCmst.getString("PARTY_CODE");
                        String chrgeCd = rsLCmst.getString("CHARGE_CODE");
                        String partyChrgeCd = rsLCmst.getString("PARTY_CHARGE_CODE");

                        if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' ")) {
                            String upSQLC1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK='LC MASTER NOT AVAILABLE',PIECE_STATUS='UNCLEAR' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQLC1);
                        } else {
                            if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC")) {
                                String upSQLC2 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK='LC EXPIRED',PIECE_STATUS='UNCLEAR' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                data.Execute(upSQLC2);
                            } else {
                                String lcNo = data.getStringValueFromDB("SELECT LC_NO FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                                String upSQLC3 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET LC_NO='" + lcNo + "' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                data.Execute(upSQLC3);
                            }
                        }

                        rsLCmst.next();
                    }
                }
                System.out.println("END : LC Party DATA Updation");
//-------------------------------------------------------------------------------------------

                //DOCUMENT THROUGH UPDATION
                System.out.println("START : Document Through AND Payment Terms Updation");
                ResultSet rsDocThr = null;
                String DocThrDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                rsDocThr = data.getResult(DocThrDetail);
                rsDocThr.first();
                if (rsDocThr.getRow() > 0) {
                    while (!rsDocThr.isAfterLast()) {

                        String baleNo = rsDocThr.getString("BALE_NO");
                        String baleDate = rsDocThr.getString("PACKING_DATE");
                        String partyCd = rsDocThr.getString("PARTY_CODE");
                        String chrgeCd = rsDocThr.getString("CHARGE_CODE");
                        String partyChrgeCd = rsDocThr.getString("PARTY_CHARGE_CODE");

                        String CrDays = data.getStringValueFromDB("SELECT CREDIT_DAYS FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE='210010' AND PARTY_CODE='" + partyCd + "' ");

                        if (chrgeCd.equals("01")) {
                            if (partyChrgeCd.equals("09")) {
                                String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PARTY_BANK_NAME='F6',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('AGAINST ADVANCE PAYMENT') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                data.Execute(upSQL1);
                            } else {
                                double noLimit01 = Double.valueOf(1000000000.00);
//                                        String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,DINESHMILLS.D_SAL_PARTY_MASTER P SET I.CRITICAL_LIMIT_AMT = '" + noLimit01 + "',I.PARTY_BANK_NAME=P.BANK_NAME,I.PARTY_BANK_ADDRESS1=P.BANK_ADDRESS,I.PARTY_BANK_ADDRESS2=P.BANK_CITY WHERE I.PARTY_CODE=P.PARTY_CODE AND I.BALE_NO = '" + baleNo + "' AND I.PACKING_DATE = '" + baleDate + "' AND P.PARTY_CODE = '" + partyCd + "' AND P.MAIN_ACCOUNT_CODE=210010 AND P.APPROVED=1 AND P.CANCELLED=0 ";
                                String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT = '" + noLimit01 + "',PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('20 DAYS CREDIT FROM THE DATE OF INVOICE') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                data.Execute(upSQL1);
                            }
                        }
                        if (chrgeCd.equals("02")) {
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('" + CrDays + "',' DAYS CREDIT FROM THE DATE OF INVOICE') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (chrgeCd.equals("04")) {
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA I,DINESHMILLS.D_SAL_PARTY_MASTER P SET I.PARTY_BANK_NAME=P.BANK_NAME,I.PARTY_BANK_ADDRESS1=P.BANK_ADDRESS,I.PARTY_BANK_ADDRESS2=P.BANK_CITY,PAYMENT_TERMS=CONCAT('" + CrDays + "',' DAYS HUNDI THROUGH BANK') WHERE I.PARTY_CODE=P.PARTY_CODE AND I.BALE_NO = '" + baleNo + "' AND I.PACKING_DATE = '" + baleDate + "' AND P.PARTY_CODE = '" + partyCd + "' AND P.MAIN_ACCOUNT_CODE=210010 AND P.APPROVED=1 AND P.CANCELLED=0 ";
                            data.Execute(upSQL1);
                        }
                        if (chrgeCd.equals("07")) {
                            String bnkName = data.getStringValueFromDB("SELECT BANK_NAME FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                            String bnkAds = data.getStringValueFromDB("SELECT BANK_ADDRESS FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                            String bnkCity = data.getStringValueFromDB("SELECT BANK_CITY FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                            double lcAmt = data.getDoubleValueFromDB("SELECT AMT FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT='" + lcAmt + "',PARTY_BANK_NAME='" + bnkName + "',PARTY_BANK_ADDRESS1='" + bnkAds + "',PARTY_BANK_ADDRESS2='" + bnkCity + "',PAYMENT_TERMS='AGAINST LETTER OF CREDIT' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (chrgeCd.equals("08")) {
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('AGAINST ','" + CrDays + "',' DAYS PDC') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }
                        if (chrgeCd.equals("09")) {
                            String upSQL1 = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS='AGAINST ADVANCE PAYMENT' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                            data.Execute(upSQL1);
                        }

                        rsDocThr.next();
                    }
                }
                System.out.println("END : Document Through AND Payment Terms Updation");
//-------------------------------------------------------------------------------------------

                //---------------------------------------------------
                //ENHANCEMENT PARTY LIMIT UPDATION GROUP CODE WISE
                System.out.println("START : Enhancement Group Party Limit Updation");
                ResultSet rsEPCL = null;
                ResultSet remark2 = null;
                String EPCLDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY BALE_NO";
                rsEPCL = data.getResult(EPCLDetail);
                rsEPCL.first();
                if (rsEPCL.getRow() > 0) {
                    while (!rsEPCL.isAfterLast()) {

                        String baleNo = rsEPCL.getString("BALE_NO");
                        String baleDate = rsEPCL.getString("PACKING_DATE");
                        String partyCd = rsEPCL.getString("PARTY_CODE");
                        double criticalLimit = 0;

                        String sql = "SELECT H.DOC_NO,H.DOC_DATE,D.PARTY_CODE,H.PROCESSING_DATE,H.APPROVED,H.CANCELED, ";
                        sql += "D.BALE_NO,D.BALE_DATE,E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT ";
                        sql += "FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H, ";
                        sql += "PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL D, ";
                        sql += "PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL_ENHANCEMENT E ";
                        sql += "WHERE H.DOC_NO=D.DOC_NO AND H.DOC_DATE=D.DOC_DATE ";
                        sql += "AND D.PARTY_CODE='" + partyCd + "' AND H.PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' ";
                        sql += "AND D.BALE_NO='" + baleNo + "' AND D.BALE_DATE='" + baleDate + "' ";
                        sql += "AND H.APPROVED=1 AND H.CANCELED=0 ";
                        sql += "AND H.DOC_NO=E.DOC_NO AND H.DOC_DATE=E.DOC_DATE AND D.PARTY_CODE=E.GROUP_PARTY_CODE ";
                        sql += "AND E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT>0 ";
                        sql += "ORDER BY H.DOC_NO DESC ";

                        ResultSet docNoDate = data.getResult(sql);

                        if (data.IsRecordExist(sql)) {

                            String rmk2 = "SELECT H.DOC_NO,H.DOC_DATE,H.PROCESSING_DATE,H.APPROVED,H.CANCELED,E.GROUP_PARTY_CODE, ";
                            rmk2 += "COALESCE(E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT,0) AS GROUP_ENHANCE_PARTY_CRITICAL_LIMIT ";
                            rmk2 += "FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H, ";
                            rmk2 += "PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL_ENHANCEMENT E ";
                            rmk2 += "WHERE H.DOC_NO=E.DOC_NO AND H.DOC_DATE=E.DOC_DATE ";
                            rmk2 += "AND H.PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' ";
                            rmk2 += "AND H.DOC_NO='" + docNoDate.getString("DOC_NO") + "' AND H.DOC_DATE='" + docNoDate.getString("DOC_DATE") + "' ";
                            rmk2 += "AND H.APPROVED=1 AND H.CANCELED=0 ";
                            rmk2 += "ORDER BY E.GROUP_PARTY_CODE ";
                            remark2 = data.getResult(rmk2);
                            remark2.first();
                            if (remark2.getRow() > 0) {
                                while (!remark2.isAfterLast()) {

                                    String sqlEL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PARTY_CRITICAL_LIMIT='" + Double.parseDouble(remark2.getString("GROUP_ENHANCE_PARTY_CRITICAL_LIMIT")) + "',CRITICAL_LIMIT_AMT='" + Double.parseDouble(remark2.getString("GROUP_ENHANCE_PARTY_CRITICAL_LIMIT")) + "' WHERE PARTY_CODE='" + remark2.getString("GROUP_PARTY_CODE") + "' ";
                                    data.Execute(sqlEL);

                                    remark2.next();
                                }
                            }

                            String enLimit = data.getStringValueFromDB("SELECT H.ENHANCE_PARTY_CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H,PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL D,PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL_ENHANCEMENT E WHERE H.DOC_NO=D.DOC_NO AND H.DOC_DATE=D.DOC_DATE AND D.PARTY_CODE='" + partyCd + "' AND H.PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND D.BALE_NO='" + baleNo + "' AND D.BALE_DATE='" + baleDate + "' AND H.APPROVED=1 AND H.CANCELED=0 AND H.DOC_NO=E.DOC_NO AND H.DOC_DATE=E.DOC_DATE AND D.PARTY_CODE=E.GROUP_PARTY_CODE AND E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT>0 ORDER BY H.DOC_NO DESC ");
                            criticalLimit = Double.parseDouble(enLimit);

//                                    String sqlEL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET PARTY_CRITICAL_LIMIT='" + criticalLimit + "',CRITICAL_LIMIT_AMT='" + criticalLimit + "' WHERE BALE_NO='" + baleNo + "' AND PARTY_CODE='" + partyCd + "' ";
                            String sqlEL = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET GRP_ENHANCE_OUT_STANDING_AMT='" + criticalLimit + "' WHERE BALE_NO='" + baleNo + "' AND PARTY_CODE='" + partyCd + "' ";
                            data.Execute(sqlEL);
                        }

                        rsEPCL.next();
                    }
                }
                System.out.println("END : Enhancement Group Party Limit Updation");
//-------------------------------------------------------------------------------------------

                //ACTUAL LIMIT OF INVOICE PROCESS UPDATION
                System.out.println("START : Actual Limit For Invoicing Updation");
                //String upacLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET CRITICAL_LIMIT_AMT = CASE WHEN CHARGE_CODE='09' THEN CASE WHEN PARTY_CRITICAL_LIMIT>0 THEN PARTY_CRITICAL_LIMIT ELSE ADV_AMT END ELSE ROUND(PARTY_CRITICAL_LIMIT-OUT_STANDING_AMT,2) END WHERE CRITICAL_LIMIT_AMT=0";
//                        String upacLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT = CASE WHEN CHARGE_CODE='09' THEN CASE WHEN PARTY_CRITICAL_LIMIT>0 AND PARTY_CRITICAL_LIMIT!=1 THEN PARTY_CRITICAL_LIMIT ELSE ADV_AMT END ELSE ROUND(PARTY_CRITICAL_LIMIT-OUT_STANDING_AMT,2) END WHERE CRITICAL_LIMIT_AMT=0 AND CHECK_POINT_REMARK=''";
                String upacLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT = CASE WHEN CHARGE_CODE='09' THEN CASE WHEN PARTY_CRITICAL_LIMIT>0 AND PARTY_CRITICAL_LIMIT!=1 THEN PARTY_CRITICAL_LIMIT ELSE ADV_AMT END ELSE ROUND(PARTY_CRITICAL_LIMIT,2) END WHERE CRITICAL_LIMIT_AMT=0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                data.Execute(upacLimit);
                //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
                System.out.println("END : Actual Limit For Invoicing Updation");

////-------------------------------------------------------------------------------------------------------------------------------
//                        //CODE ADDES ON 06/10/2017 FOR MAKING INVOICEING WITHOUT CHECKING LIMITS
//                        System.out.println("START : MAKING INVOICEING WITHOUT CHECKING LIMITS");
//                        //String upacLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET CRITICAL_LIMIT_AMT = CASE WHEN CHARGE_CODE='09' THEN CASE WHEN PARTY_CRITICAL_LIMIT>0 THEN PARTY_CRITICAL_LIMIT ELSE ADV_AMT END ELSE ROUND(PARTY_CRITICAL_LIMIT-OUT_STANDING_AMT,2) END WHERE CRITICAL_LIMIT_AMT=0";
//                        double noLmt = Double.valueOf(999999999.99);
//                        String nLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT = '" + noLmt + "'";
//                        data.Execute(nLimit);
//                        //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
//                        data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
//                        System.out.println("END : MAKING INVOICEING WITHOUT CHECKING LIMITS");
////-------------------------------------------------------------------------------------------------------------------------------
                //---------------------------------------------------
                //CHECK CRITICAL LIMIT AMOUNT FOR INVOICING PROCESS
                System.out.println("START : Check Critical Amount");
                String chkCriticalLimitZero = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CASE WHEN CHARGE_CODE='09' THEN 'ADV BAL IS ZERO' ELSE CASE WHEN CHARGE_CODE='07' THEN 'LC BAL IS ZERO' ELSE 'CREDIT BAL IS ZERO' END END,PIECE_STATUS='UNCLEAR' WHERE CRITICAL_LIMIT_AMT=0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                data.Execute(chkCriticalLimitZero);

                String chkCriticalLimitLess = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK=CASE WHEN CHARGE_CODE='09' THEN 'ADV BAL IS ZERO' ELSE CASE WHEN CHARGE_CODE='07' THEN 'LC BAL IS ZERO' ELSE 'O/S EXCEEDED' END END,PIECE_STATUS='UNCLEAR' WHERE CRITICAL_LIMIT_AMT<0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                data.Execute(chkCriticalLimitLess);

                //String chkCriticalLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET FLAG = 1 WHERE CRITICAL_LIMIT_AMT>=0";
                String chkCriticalLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET FLAG = 1 WHERE CRITICAL_LIMIT_AMT>0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                data.Execute(chkCriticalLimit);

                String chkGrpOSLimit = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK='O/S EXCEEDED', FLAG=0, PIECE_STATUS='UNCLEAR' WHERE FLAG=1 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') AND CHARGE_CODE NOT IN ('01','09','07') AND GRP_ENHANCE_OUT_STANDING_AMT=0 AND GRP_OUT_STANDING_AMT>=GRP_CRITICAL_LIMIT";
                data.Execute(chkGrpOSLimit);

                System.out.println("END : Check Critical Amount");

                //CHECK GST AMOUNT FOR INVOICING PROCESS
                System.out.println("START : Check GST Amount");
                //String chkCriticalLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET FLAG = 1 WHERE CRITICAL_LIMIT_AMT>=0";
                String chkGSTAmt = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET FLAG = 0,CHECK_POINT_REMARK='GSTIN/STATE NO MISSING',PIECE_STATUS='UNCLEAR' WHERE IGST_AMT+CGST_AMT+SGST_AMT=0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                data.Execute(chkGSTAmt);
                System.out.println("END : Check GST Amount");

                //CALCULATION OF INVOICE AMOUNT UPTO IT'S LIMIT
                System.out.println("START : Invoice Amount Calculation till it's Limit");
                ResultSet rsCalc = null;
                String upCalc;
                //String calcDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS WHERE FLAG=1 ORDER BY PARTY_CODE,BALE_NO,PIECE_NO";
                String calcDetail = "SELECT * FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE FLAG=1 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ORDER BY PARTY_CODE,BALE_NO";
                rsCalc = data.getResult(calcDetail);
                rsCalc.first();
                if (rsCalc.getRow() > 0) {
                    while (!rsCalc.isAfterLast()) {

                        String baleNo = rsCalc.getString("BALE_NO");
                        String partyCd = rsCalc.getString("PARTY_CODE");
                        String chargeCd = rsCalc.getString("CHARGE_CODE");
                        float osAmt = Math.round(rsCalc.getFloat("OUT_STANDING_AMT"));
                        float invAmt = Math.round(rsCalc.getFloat("INVOICE_AMT"));
                        //double invCriticalAmt = data.getDoubleValueFromDB("SELECT INV_CRITICAL_LIMIT_AMT FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS WHERE PARTY_CODE='"+partyCd+"' AND PIECE_NO='"+pieceNo+"'");
                        double invCriticalAmt = data.getDoubleValueFromDB("SELECT INV_CRITICAL_LIMIT_AMT FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "' AND FLAG=1");
                                //rsCalc.getFloat("INV_CRITICAL_LIMIT_AMT");

                        //------- TCS Calculation --------
//                                String gstinPan = rsCalc.getString("GSTIN_PAN");
//                                float invAmtActual = rsCalc.getFloat("INVOICE_AMT");
//                                double tcsEligibleAmt = data.getDoubleValueFromDB("SELECT TCS_ELIGIBLE_AMOUNT FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "' ");
//                                double taxableAmt = data.getDoubleValueFromDB("SELECT INVOICE_AMT FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "' ");
//                                float tcsPer = rsCalc.getFloat("TCS_PER");
//                                double upTcsAmt = 0, upTcsPer = 0, upInvAmt = 0;
//                                if (tcsPer == 0 && (tcsEligibleAmt + taxableAmt) > 5000000) {
////                                    upTcsPer = 0.075;//till 31/03/2021
//                                    upTcsPer = 0.000;//onwards 01/04/2021
//                                    upTcsAmt = Math.round((taxableAmt * upTcsPer) / 100);
//                                    upInvAmt = invAmtActual + upTcsAmt;
//                                    data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TCS_PER='" + upTcsPer + "',TCS_AMT='" + upTcsAmt + "',INVOICE_AMT='" + (upInvAmt) + "' WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "' ");
//                                    data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TCS_ELIGIBLE_AMOUNT='" + (tcsEligibleAmt + taxableAmt) + "' WHERE GSTIN_PAN='" + gstinPan + "' AND PARTY_CODE='" + partyCd + "' AND BALE_NO>=" + baleNo + " ");
//                                    data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TCS_ELIGIBLE_AMOUNT='" + (tcsEligibleAmt + taxableAmt) + "' WHERE GSTIN_PAN='" + gstinPan + "' AND PARTY_CODE>" + partyCd + " ");
//                                    invAmt = data.getLongValueFromDB("SELECT INVOICE_AMT FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "' ");
//                                    invAmt = Math.round(invAmt);
//                                }
                        //--------------------------------
                        if ((invCriticalAmt - osAmt - invAmt) < 0) {
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET INV_CRITICAL_LIMIT_AMT='" + (invCriticalAmt - osAmt - invAmt) + "',INDICATOR=1,FLAG=0,CHECK_POINT_REMARK=CASE WHEN CHARGE_CODE='09' THEN 'ADV AMT IS LESS THEN INV AMT' ELSE CASE WHEN CHARGE_CODE='07' THEN 'LC AMT IS LESS THEN INV AMT' ELSE 'CREDIT AMT IS LESS THEN INV AMT' END END,PIECE_STATUS='UNCLEAR' WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "'");
//                                    if (tcsPer == 0 && (tcsEligibleAmt + taxableAmt) > 5000000) {
//                                        data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TCS_ELIGIBLE_AMOUNT='" + tcsEligibleAmt + "' WHERE GSTIN_PAN='" + gstinPan + "' AND PARTY_CODE='" + partyCd + "' AND BALE_NO>=" + baleNo + " ");
//                                        data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET TCS_ELIGIBLE_AMOUNT='" + tcsEligibleAmt + "' WHERE GSTIN_PAN='" + gstinPan + "' AND PARTY_CODE>" + partyCd + " ");
//                                    }
                        } else {

//                                    if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE=CURDATE() AND PARTY_CODE='" + partyCd + "'")) {
//                                        data.Execute("UPDATE PRODUCTION.FELT_INV_PROCESS_VAR_GST SET CRITICAL_LIMIT_NEW='" + (invCriticalAmt - invAmt) + "' WHERE PROCESSING_DATE=CURDATE() AND PARTY_CODE='" + partyCd + "'");
//                                    }
                            if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>=CURDATE() AND BALE_NO LIKE ('%" + baleNo + "%')")) {
                                data.Execute("UPDATE PRODUCTION.FELT_INV_PROCESS_VAR_GST SET CRITICAL_LIMIT_NEW='" + (invCriticalAmt - invAmt) + "' WHERE PROCESSING_DATE>=CURDATE() AND BALE_NO LIKE ('%" + baleNo + "%')");
                            }
                            //upCalc = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INV_CRITICAL_LIMIT_AMT='"+(invCriticalAmt-invAmt)+"' WHERE PARTY_CODE='"+partyCd+"' AND INDICATOR=0";
                            upCalc = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET INV_CRITICAL_LIMIT_AMT='" + (invCriticalAmt - invAmt) + "' WHERE PARTY_CODE='" + partyCd + "' AND INDICATOR=0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ";
                            data.Execute(upCalc);
                            //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INDICATOR=1 WHERE PARTY_CODE='"+partyCd+"' AND PIECE_NO='"+pieceNo+"'");
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET INDICATOR=1,PIECE_STATUS='CLEAR' WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "'");
                            data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CRITICAL_LIMIT_AMT='" + (invCriticalAmt - invAmt) + "', OUT_STANDING_AMT='" + (osAmt + invAmt) + "'  WHERE INDICATOR=0 AND FLAG=1 AND PARTY_CODE='" + partyCd + "' AND BALE_NO!='" + baleNo + "'");

                        }

                        rsCalc.next();
                    }
                }
                System.out.println("END : Invoice Amount Calculation till it's Limit");

                //---------------------------------------------------
                //CHECK INVOICE CRITICAL LIMIT AMOUNT of INVOICING PROCESS and UPDATE FLAG WHERE LIMIT BELOW 0
                System.out.println("START : Check Critical Limit of Invoicing");
                //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET FLAG=0 WHERE INV_CRITICAL_LIMIT_AMT<0");
                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET FLAG=0,CHECK_POINT_REMARK=CASE WHEN CHARGE_CODE='09' THEN 'ADV AMT IS LESS THEN INV AMT' ELSE CASE WHEN CHARGE_CODE='07' THEN 'LC AMT IS LESS THEN INV AMT' ELSE 'CREDIT AMT IS LESS THEN INV AMT' END END,PIECE_STATUS='UNCLEAR' WHERE INV_CRITICAL_LIMIT_AMT<0 AND CHECK_POINT_REMARK='' AND COALESCE(PIECE_STATUS,'') NOT IN ('UNCLEAR','INVOICED') ");
                System.out.println("END : Check Critical Limit of Invoicing");

                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA SET CHECK_POINT_REMARK = SUBSTRING(CHECK_POINT_REMARK,3) WHERE LEFT(CHECK_POINT_REMARK,2)=', ' AND COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ");

                data.Execute("UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA A, (SELECT DISTINCT PARTY_CODE AS B_PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHARGE_CODE=9 AND COALESCE(PIECE_STATUS,'') IN ('UNCLEAR')) B  SET A.CHECK_POINT_REMARK='-',A.PIECE_STATUS='UNCLEAR' WHERE A.CHARGE_CODE=9 AND COALESCE(A.PIECE_STATUS,'') IN ('CLEAR') AND A.PARTY_CODE=B.B_PARTY_CODE");

                data.Execute("TRUNCATE PRODUCTION.Z_CLEAR_TO_UNCLEAR");
                data.Execute("INSERT INTO PRODUCTION.Z_CLEAR_TO_UNCLEAR  SELECT D_PARTY,CASE WHEN BAL > 0 THEN 'CLEAR'  ELSE 'UNCLEAR' END AS ASD, CASE WHEN CNT =1 AND  BAL >= 0 THEN 'SUFFICIENCT BALANCE' WHEN CNT =1 AND  BAL < 0 THEN CONCAT('INSUFFICIENCT BALANCE PLEASE ADD AMOUNT Rs. ',abs(BAL)  ) WHEN CNT >1 AND  BAL >= 0 THEN 'SUFFICIENCT BALANCE' WHEN CNT >1 AND  BAL < 0 THEN CONCAT('INSUFFICIENCT BALANCE PLEASE ADD AMOUNT Rs. ',abs(BAL),' OR REMOVE PIECES FROM DISPATCH SELECTION LIST' ) END  AS B_REMARK FROM (SELECT PARTY_CODE AS D_PARTY,COUNT(PIECE_NO) AS CNT,SUM(ROUND(INVOICE_AMT,0)) AS INVOICE_AMT,ADV_AMT,(ADV_AMT - SUM(ROUND(INVOICE_AMT,0))) AS BAL ,GROUP_CONCAT(  CONCAT(PIECE_NO,'(',ROUND(INVOICE_AMT,0),')')) AS DET FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHARGE_CODE =9 GROUP BY PARTY_CODE )AS M ");
                
                String cSql = "UPDATE PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA , PRODUCTION.Z_CLEAR_TO_UNCLEAR  "
                        + "SET CLEAR_TO_UNCLEAR_REMARK=B_REMARK "
                        + "WHERE CHARGE_CODE=9 AND PARTY_CODE=D_PARTY ";
//                System.out.println("csql : "+cSql);
                data.Execute(cSql);
                
                data.Execute("DELETE FROM PRODUCTION.FELT_DISPATCH_PLANNER_DATA_HISTORY WHERE PROCESS_DATE=CURDATE() ");
                data.Execute("INSERT INTO PRODUCTION.FELT_DISPATCH_PLANNER_DATA_HISTORY (PROCESS_DATE, PIECE_STATUS, BALE_NO, NO_OF_PIECES, MACHINE_NO, POSITION_NO, PARTY_CODE, PRODUCT_CODE, GROUP_NAME, STYLE, LENGTH, WIDTH, GSM, THORITICAL_WEIGHT, SQMTR, SYN_PER, ACTUAL_WEIGHT, ACTUAL_LENGTH, ACTUAL_WIDTH, INVOICE_NO, INVOICE_DATE, INVOICE_PARTY, RATE, BAS_AMT, DISC_PER, DISC_AMT, DISC_BAS_AMT, EXCISE, SEAM_CHG, SEAM_CHG_PER, INSURANCE_AMT, CHEM_TRT_CHG, PIN_CHG, SPIRAL_CHG, INS_IND, CST, VAT, SD_AMT, INVOICE_AMT, SQM_IND, OUT_STANDING_AMT, ADV_AMT, GRP_CRITICAL_LIMIT, PARTY_CRITICAL_LIMIT, CHARGE_CODE, GRP_OUT_STANDING_AMT, GRP_MAIN_PARTY_CODE, INV_CRITICAL_LIMIT_AMT, CRITICAL_LIMIT_AMT, INDICATOR, FLAG, CST2, CST5, VAT1, VAT4, LOT_NO, TRANSPORTER_CODE, GATEPASS_NO, TAX_INV_NO, RETAIL_INV_NO, IGST_PER, IGST_AMT, CGST_PER, CGST_AMT, SGST_PER, SGST_AMT, GST_COMP_CESS_PER, GST_COMP_CESS_AMT, ADV_DOC_NO, ADV_RECEIVED_AMT, ADV_AGN_INV_AMT, ADV_AGN_IGST_AMT, ADV_AGN_SGST_AMT, ADV_AGN_CGST_AMT, ADV_AGN_GST_COMP_CESS_AMT, VEHICLE_NO, HSN_CODE, DOCUMENT_THROUGH, PRODUCT_DESC, PIECE_NO, POSITION_DESC, RATE_UNIT, TRANSPORTER_NAME, DESP_MODE, PARTY_NAME, GSTIN_NO, ADDRESS1, ADDRESS2, CITY_NAME, CITY_ID, DISPATCH_STATION, PLACE_OF_SUPPLY, PINCODE, PARTY_CHARGE_CODE, PARTY_BANK_NAME, PARTY_BANK_ADDRESS1, PARTY_BANK_ADDRESS2, CHECK_POINT_REMARK, PO_NO, PO_DATE, AOSD_PER, AOSD_AMT, CREDIT_DAYS, PAYMENT_TERMS, GRP_ENHANCE_OUT_STANDING_AMT, SURCHARGE_PER, SURCHARGE_RATE, GROSS_RATE, MOBILE_NO, DELIVERY_MODE, MATERIAL_CODE, TCS_PER, TCS_AMT, GSTIN_PAN, TCS_ELIGIBLE_AMOUNT, INCHARGE_AREA, PIECE_UPN, PIECE_STAGE, PIECE_STAGE_ORIGINAL, FINISHING_DATE, PACKING_NO, PACKING_DATE, PACKING_PENDING_USER, FELT_AMT, 09_BALANCE_AMT, PARTY_OUTSTANDING_AMT, GROUP_OUTSTANDING_AMT, GROUP_CRITICAL_LIMIT_ENHANCHMENT_NO, GROUP_CRITICAL_LIMIT_ENHANCHMENT_DATE, GROUP_CRITICAL_LIMIT_ENHANCHMENT_PENDING_USER, INVOICE_PARAMETER_MODIFICATION_NO, INVOICE_PARAMETER_MODIFICATION_DATE, INVOICE_PARAMETER_MODIFICATION_PENDING_USER, PDC_NO, PDC_DATE, PDC_PENDING_USER, LC_NO, LC_DATE, LC_PENDING_USER, PARTY_UPDATION_PENDING_USER, DISCOUNT_UPDATION_PENDING_USER, DISCOUNT_CHECK, CHARGE_CODE_CHECK, PARTY_CLOSED_REMARK, PARTY_GROUP_CODE, PARTY_GROUP_NAME, BALE_DATE, SANC_GROUP, SANC_DOC, CLEAR_TO_UNCLEAR_REMARK) "
                        + "SELECT CURDATE(), PIECE_STATUS, BALE_NO, NO_OF_PIECES, MACHINE_NO, POSITION_NO, PARTY_CODE, PRODUCT_CODE, GROUP_NAME, STYLE, LENGTH, WIDTH, GSM, THORITICAL_WEIGHT, SQMTR, SYN_PER, ACTUAL_WEIGHT, ACTUAL_LENGTH, ACTUAL_WIDTH, INVOICE_NO, INVOICE_DATE, INVOICE_PARTY, RATE, BAS_AMT, DISC_PER, DISC_AMT, DISC_BAS_AMT, EXCISE, SEAM_CHG, SEAM_CHG_PER, INSURANCE_AMT, CHEM_TRT_CHG, PIN_CHG, SPIRAL_CHG, INS_IND, CST, VAT, SD_AMT, INVOICE_AMT, SQM_IND, OUT_STANDING_AMT, ADV_AMT, GRP_CRITICAL_LIMIT, PARTY_CRITICAL_LIMIT, CHARGE_CODE, GRP_OUT_STANDING_AMT, GRP_MAIN_PARTY_CODE, INV_CRITICAL_LIMIT_AMT, CRITICAL_LIMIT_AMT, INDICATOR, FLAG, CST2, CST5, VAT1, VAT4, LOT_NO, TRANSPORTER_CODE, GATEPASS_NO, TAX_INV_NO, RETAIL_INV_NO, IGST_PER, IGST_AMT, CGST_PER, CGST_AMT, SGST_PER, SGST_AMT, GST_COMP_CESS_PER, GST_COMP_CESS_AMT, ADV_DOC_NO, ADV_RECEIVED_AMT, ADV_AGN_INV_AMT, ADV_AGN_IGST_AMT, ADV_AGN_SGST_AMT, ADV_AGN_CGST_AMT, ADV_AGN_GST_COMP_CESS_AMT, VEHICLE_NO, HSN_CODE, DOCUMENT_THROUGH, PRODUCT_DESC, PIECE_NO, POSITION_DESC, RATE_UNIT, TRANSPORTER_NAME, DESP_MODE, PARTY_NAME, GSTIN_NO, ADDRESS1, ADDRESS2, CITY_NAME, CITY_ID, DISPATCH_STATION, PLACE_OF_SUPPLY, PINCODE, PARTY_CHARGE_CODE, PARTY_BANK_NAME, PARTY_BANK_ADDRESS1, PARTY_BANK_ADDRESS2, CHECK_POINT_REMARK, PO_NO, PO_DATE, AOSD_PER, AOSD_AMT, CREDIT_DAYS, PAYMENT_TERMS, GRP_ENHANCE_OUT_STANDING_AMT, SURCHARGE_PER, SURCHARGE_RATE, GROSS_RATE, MOBILE_NO, DELIVERY_MODE, MATERIAL_CODE, TCS_PER, TCS_AMT, GSTIN_PAN, TCS_ELIGIBLE_AMOUNT, INCHARGE_AREA, PIECE_UPN, PIECE_STAGE, PIECE_STAGE_ORIGINAL, FINISHING_DATE, PACKING_NO, PACKING_DATE, PACKING_PENDING_USER, FELT_AMT, 09_BALANCE_AMT, PARTY_OUTSTANDING_AMT, GROUP_OUTSTANDING_AMT, GROUP_CRITICAL_LIMIT_ENHANCHMENT_NO, GROUP_CRITICAL_LIMIT_ENHANCHMENT_DATE, GROUP_CRITICAL_LIMIT_ENHANCHMENT_PENDING_USER, INVOICE_PARAMETER_MODIFICATION_NO, INVOICE_PARAMETER_MODIFICATION_DATE, INVOICE_PARAMETER_MODIFICATION_PENDING_USER, PDC_NO, PDC_DATE, PDC_PENDING_USER, LC_NO, LC_DATE, LC_PENDING_USER, PARTY_UPDATION_PENDING_USER, DISCOUNT_UPDATION_PENDING_USER, DISCOUNT_CHECK, CHARGE_CODE_CHECK, PARTY_CLOSED_REMARK, PARTY_GROUP_CODE, PARTY_GROUP_NAME, BALE_DATE, SANC_GROUP, SANC_DOC, CLEAR_TO_UNCLEAR_REMARK FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA ");

                System.out.println("----------------------------------------------------------");
                System.out.println("END OF PROCESS");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Bar.setVisible(false);
        //lblStatus.setVisible(false);
    }

    public static void OutStandingAmt2() {
        data.Execute("TRUNCATE PRODUCTION.Z_OUTSTANDING_AMT");
        data.Execute("TRUNCATE PRODUCTION.Z_OUTSTANDING_REPORT");

        data.Execute("INSERT INTO PRODUCTION.Z_OUTSTANDING_AMT (SUB_PARTY_CODE,MAIN_GROUP_CODE) SELECT DISTINCT P.PARTY_CODE,H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA P WHERE D.GROUP_CODE=H.GROUP_CODE AND D.PARTY_CODE = P.PARTY_CODE AND H.APPROVED=1 AND H.CANCELED=0 AND COALESCE(P.CHARGE_CODE,'')!='09' ORDER BY P.PARTY_CODE ");
        data.Execute("UPDATE PRODUCTION.Z_OUTSTANDING_AMT SET MAIN_PARTY_CODE=CASE WHEN LENGTH(MAIN_PARTY_CODE)>0 THEN MAIN_PARTY_CODE ELSE SUB_PARTY_CODE END");
        data.Execute("INSERT INTO PRODUCTION.Z_OUTSTANDING_REPORT (MAIN_PARTY_CODE,MAIN_GROUP_CODE) SELECT DISTINCT MAIN_PARTY_CODE,MAIN_GROUP_CODE FROM PRODUCTION.Z_OUTSTANDING_AMT ");

        createFile1();
        createFile2();
    }

    private static void createFile1() {
        try {
//            System.out.println("InvoiceType : " + InvoiceType);
            double MainBalance = 0, SubBalance = 0, grpCriticalBal = 0;
            String GroupMainParty = "", GroupSubParty = "", Record = "";
            String InvoiceNo = "", InvoiceDate = "", strMainBalance = "", strBalance = "", strDecimal = "", MainCode = "";
            ResultSet rsMainParty = null, rsSubParty = null;
            //String FileName = "/data/Balance_Transfer_Cobol/"+EITLERPGLOBAL.getCurrentDateDB().substring(8,10)+
            //EITLERPGLOBAL.getCurrentDateDB().substring(5,7)+EITLERPGLOBAL.getCurrentDateDB().substring(2,4);
//            String FileName = "/data/Balance_Transfer_Cobol/ost";
            HashMap List = new HashMap();

            int InvoiceType = 2;
            MainCode = "210010";

//            System.out.println("URL : " + FinanceGlobal.FinURL);
            data.Execute("TRUNCATE PRODUCTION.Z_BAL_TR");

            rsMainParty = data.getResult("SELECT MAIN_PARTY_CODE FROM PRODUCTION.Z_OUTSTANDING_REPORT ORDER BY MAIN_PARTY_CODE");
            rsMainParty.first();
            if (rsMainParty.getRow() > 0) {
                while (!rsMainParty.isAfterLast()) {
                    Record = "";
                    MainBalance = 0;
                    grpCriticalBal = 0;
                    SubBalance = 0;
                    GroupMainParty = rsMainParty.getString("MAIN_PARTY_CODE");
                    if (MainBalance == 0) {
                        MainBalance = EITLERPGLOBAL.round(BalanceTransfer1(MainCode, GroupMainParty), 2);
                        grpCriticalBal = data.getDoubleValueFromDB("SELECT COALESCE(H.GROUP_CRITICAL_LIMIT,0) FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE D.GROUP_CODE=H.GROUP_CODE AND D.PARTY_CODE ='" + GroupMainParty + "' AND H.APPROVED=1 AND H.CANCELED=0");

                        data.Execute("UPDATE PRODUCTION.Z_OUTSTANDING_AMT SET SUB_OUTSTANDING_BAL=" + MainBalance + ", GROUP_CRITICAL_BAL=" + grpCriticalBal + " WHERE MAIN_PARTY_CODE='" + GroupMainParty + "' ");
                    }

                    data.Execute("UPDATE PRODUCTION.Z_OUTSTANDING_REPORT SET SUB_OUTSTANDING_BAL=" + MainBalance + " WHERE MAIN_PARTY_CODE='" + GroupMainParty + "'");

                    rsMainParty.next();
                }
            }
//            System.out.println("File Created sucessfully...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createFile2() {
        try {
//            System.out.println("InvoiceType : " + InvoiceType);
            double MainBalance = 0, SubBalance = 0;
            String GroupMainParty = "", GroupSubParty = "", GroupMainCode = "", Record = "";
            String InvoiceNo = "", InvoiceDate = "", strMainBalance = "", strBalance = "", strDecimal = "", MainCode = "";
            ResultSet rsMainParty = null, rsSubParty = null;
            //String FileName = "/data/Balance_Transfer_Cobol/"+EITLERPGLOBAL.getCurrentDateDB().substring(8,10)+
            //EITLERPGLOBAL.getCurrentDateDB().substring(5,7)+EITLERPGLOBAL.getCurrentDateDB().substring(2,4);
//            String FileName = "/data/Balance_Transfer_Cobol/ost";
            HashMap List = new HashMap();

            int InvoiceType = 2;
            MainCode = "210010";

            data.Execute("TRUNCATE PRODUCTION.Z_BAL_TR");

            rsMainParty = data.getResult("SELECT DISTINCT MAIN_GROUP_CODE FROM PRODUCTION.Z_OUTSTANDING_REPORT WHERE COALESCE(MAIN_GROUP_CODE,'') != '' ORDER BY MAIN_GROUP_CODE");
            rsMainParty.first();
            if (rsMainParty.getRow() > 0) {
                while (!rsMainParty.isAfterLast()) {
                    Record = "";
                    MainBalance = 0;
                    SubBalance = 0;
//                    GroupMainParty = rsMainParty.getString("MAIN_PARTY_CODE");
                    GroupMainCode = rsMainParty.getString("MAIN_GROUP_CODE");
//                    MainBalance=BalanceTransfer1(MainCode, GroupMainParty);

                    rsSubParty = data.getResult("SELECT D.PARTY_CODE FROM PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H WHERE D.GROUP_CODE=H.GROUP_CODE AND H.GROUP_CODE ='" + GroupMainCode + "' AND H.APPROVED=1 AND H.CANCELED=0");
                    rsSubParty.first();
                    if (rsSubParty.getRow() > 0) {
                        while (!rsSubParty.isAfterLast()) {
                            GroupSubParty = rsSubParty.getString("PARTY_CODE");
                            SubBalance = EITLERPGLOBAL.round(SubBalance + BalanceTransfer1(MainCode, GroupSubParty), 2);
//                            SubBalance = EITLERPGLOBAL.round(BalanceTransfer1(MainCode, GroupSubParty), 2);
//                            System.out.println("GroupSubParty : " + GroupSubParty + " SubBalance : " + SubBalance);
//                            MainBalance = MainBalance + SubBalance;
//                            data.Execute("UPDATE TEMP_DATABASE.TEMP_OUTSTANDING_AMT SET SUB_OUTSTANDING_BAL=" + SubBalance + " WHERE MAIN_PARTY_CODE='" + GroupMainParty + "' AND SUB_PARTY_CODE='" + GroupSubParty + "'");
                            rsSubParty.next();
                        }
                    }
                    if (MainBalance == 0) {
                        MainBalance = EITLERPGLOBAL.round(BalanceTransfer1(MainCode, GroupMainParty), 2);
                        data.Execute("UPDATE PRODUCTION.Z_OUTSTANDING_AMT SET SUB_OUTSTANDING_BAL=" + MainBalance + " WHERE MAIN_PARTY_CODE='" + GroupMainParty + "' ");
                    }

                    data.Execute("UPDATE PRODUCTION.Z_OUTSTANDING_REPORT SET GROUP_OUTSTANDING_BAL=" + SubBalance + " WHERE MAIN_GROUP_CODE='" + GroupMainCode + "'");

                    rsMainParty.next();
                }
            }
//            System.out.println("Group O/S File Created sucessfully...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double BalanceTransfer1(String MainCode, String PartyCode) {
        String SQL = "", FromDate = "", ToDate = "", InvoiceNo = "", InvoiceDate = "", BookCode = "", ChargeCode = "";
        ResultSet rsInvoice = null;
        int InvoiceType = 0, EntryNo = 0;
        double TotalBalance = 0;
        try {
            if (MainCode.equals("210010")) {
                InvoiceType = 2;
                BookCode = " AND BOOK_CODE IN ('09') "; //,'18'
                //ChargeCode = " AND CHARGE_CODE IN ('02','08') ";
            }
            // SET LAST CLOSING DATE & TO DATE
            ToDate = EITLERPGLOBAL.getCurrentDateDB();
            FromDate = EITLERPGLOBAL.FinFromDateDB;
            SQL = "SELECT ENTRY_NO FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_DATE<='" + ToDate + "' ORDER BY ENTRY_DATE DESC";
            EntryNo = data.getIntValueFromDB(SQL, FinanceGlobal.FinURL);
            FromDate = data.getStringValueFromDB("SELECT ENTRY_DATE FROM D_FIN_DR_OPENING_OUTSTANDING_HEADER WHERE ENTRY_NO=" + EntryNo, FinanceGlobal.FinURL);
            // ------------------------------

            // GET PARTY'S INVOICE NO,INVOICE DATE USING UNION FROM OUTSTANDING DETAIL AND VOUCHER TABLES
            SQL = "(SELECT MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,BOOK_CODE,VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,INVOICE_NO,INVOICE_DATE,LINK_NO,AMOUNT,EFFECT FROM FINANCE.D_FIN_DR_OPENING_OUTSTANDING_DETAIL "
                    + "WHERE INVOICE_TYPE=" + InvoiceType + " AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND SUB_ACCOUNT_CODE='" + PartyCode + "' AND ENTRY_NO=" + EntryNo + " AND EFFECT='D' AND (MATCHED_DATE>'" + ToDate + "' OR MATCHED_DATE='0000-00-00') " + BookCode + " ) "
                    + "UNION ALL "
                    + "(SELECT B.MAIN_ACCOUNT_CODE,B.SUB_ACCOUNT_CODE,A.BOOK_CODE,A.VOUCHER_NO,A.VOUCHER_DATE,A.LEGACY_NO,B.INVOICE_NO,B.INVOICE_DATE,B.LINK_NO,B.AMOUNT,B.EFFECT FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B "
                    + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE='" + MainCode + "' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.EFFECT='D' AND A.APPROVED=1 AND A.CANCELLED=0 "
                    + "AND A.VOUCHER_DATE >'" + FromDate + "' AND A.VOUCHER_DATE <='" + ToDate + "' AND (B.MATCHED_DATE>'" + ToDate + "' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) " + BookCode + " ) "
                    + "ORDER BY VOUCHER_DATE ";

            data.Execute("INSERT INTO PRODUCTION.Z_BAL_TR (MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,BOOK_CODE,VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,INVOICE_NO,INVOICE_DATE,LINK_NO,AMOUNT,EFFECT) " + SQL);

            rsInvoice = data.getResult(SQL, FinanceGlobal.FinURL);
            rsInvoice.first();
            InvoiceNo = "";
            InvoiceDate = "";
            // ---------------------------------------------------------------------

            if (rsInvoice.getRow() > 0) {
                while (!rsInvoice.isAfterLast()) {

                    String VoucherNo = UtilFunctions.getString(rsInvoice, "VOUCHER_NO", "");
                    InvoiceNo = UtilFunctions.getString(rsInvoice, "INVOICE_NO", "");
                    InvoiceDate = UtilFunctions.getString(rsInvoice, "INVOICE_DATE", "");
                    double DebitAmount = 0;
                    double AdjustedAmount = 0;
                    if (clsVoucher.getVoucherType(VoucherNo) != FinanceGlobal.TYPE_SALES_JOURNAL) { //&& clsVoucher.getVoucherType(VoucherNo)!=FinanceGlobal.TYPE_DEBIT_NOTE
                        rsInvoice.next();
                        continue;
                    }
//                    if (InvoiceType == 2 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
//                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("02") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("08") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("04")) {
//                            rsInvoice.next();
//                            continue;
//                        }
//                    } else if (InvoiceType == 1 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
//                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("02") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("08") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("04")) {
//                            rsInvoice.next();
//                            continue;
//                        }
//                    }

                    if (InvoiceType == 2 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("1")) { //Closed on 29/08/2020 as requested by Mr. Motiani from Felt Sales Dept
//                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4") ) {
                            rsInvoice.next();
                            continue;
                        }
                    } else if (InvoiceType == 1 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("5") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4")) {
                            rsInvoice.next();
                            continue;
                        }
                    }

                    if (!data.IsRecordExist("SELECT * FROM D_FIN_DEBITNOTE_RECEIPT_MAPPING WHERE DEBITNOTE_VOUCHER_NO='" + VoucherNo + "'", FinanceGlobal.FinURL)
                            && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_DEBIT_NOTE) {
                        rsInvoice.next();
                        continue;
                    }

                    if (clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
                        SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + VoucherNo + "' AND EFFECT='D' "
                                + "AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND SUB_ACCOUNT_CODE='" + PartyCode + "' "
                                + "AND INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' "
                                + "AND (MATCHED_DATE>'" + ToDate + "' OR MATCHED_DATE='0000-00-00' OR MATCHED_DATE IS NULL ) ";
                        DebitAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);

                        SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_DETAIL B, D_FIN_VOUCHER_HEADER A "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 "
                                + "AND (B.MATCHED_DATE>'" + ToDate + "' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) "
                                + "AND B.MAIN_ACCOUNT_CODE='" + MainCode + "' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.EFFECT='C' "
                                + "AND B.INVOICE_DATE='" + InvoiceDate + "' AND B.INVOICE_NO='" + InvoiceNo + "' ";

                        AdjustedAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);
                    } else {
                        SQL = "SELECT SUM(AMOUNT) FROM D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + VoucherNo + "' AND EFFECT='D' "
                                + "AND MAIN_ACCOUNT_CODE='" + MainCode + "' AND SUB_ACCOUNT_CODE='" + PartyCode + "' "
                                + "AND (MATCHED_DATE>'" + ToDate + "' OR MATCHED_DATE='0000-00-00' OR MATCHED_DATE IS NULL ) ";
                        DebitAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);

                        SQL = "SELECT SUM(B.AMOUNT) FROM D_FIN_VOUCHER_DETAIL B, D_FIN_VOUCHER_HEADER A "
                                + "WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.APPROVED=1 AND A.CANCELLED=0 "
                                + "AND (B.MATCHED_DATE>'" + ToDate + "' OR B.MATCHED_DATE='0000-00-00' OR B.MATCHED_DATE IS NULL ) "
                                + "AND B.MAIN_ACCOUNT_CODE='" + MainCode + "' AND B.SUB_ACCOUNT_CODE='" + PartyCode + "' AND B.EFFECT='C' "
                                + "AND B.GRN_NO='" + VoucherNo + "' ";
                        AdjustedAmount = data.getDoubleValueFromDB(SQL, FinanceGlobal.FinURL);
                    }
                    if (DebitAmount == AdjustedAmount) {
                        rsInvoice.next();
                        continue;
                    }

                    TotalBalance = EITLERPGLOBAL.round(TotalBalance + EITLERPGLOBAL.round(DebitAmount - AdjustedAmount, 2), 2);
                    rsInvoice.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return TotalBalance;
        }
        return TotalBalance;
    }

    public static void AdvAmt() {
        data.Execute("TRUNCATE PRODUCTION.Z_ADV_AMT");

        data.Execute("INSERT INTO PRODUCTION.Z_ADV_AMT (SUB_PARTY_CODE) SELECT DISTINCT PARTY_CODE FROM PRODUCTION.FELT_DISPATCH_PIECE_DETAIL_DATA WHERE CHARGE_CODE=9 AND COALESCE(PIECE_STATUS,'') NOT IN ('INVOICED') ORDER BY PARTY_CODE ");

        data.Execute("UPDATE PRODUCTION.Z_ADV_AMT A , (SELECT B.SUB_ACCOUNT_CODE AS SUB_ACCOUNT_CODE, SUM(B.AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B "
                + "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_DATE<=CURDATE() AND A.VOUCHER_TYPE IN (6,7,8,9,12) AND B.MAIN_ACCOUNT_CODE=210010 AND B.SUB_ACCOUNT_CODE IN (SELECT SUB_PARTY_CODE FROM PRODUCTION.Z_ADV_AMT) "
                + "AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 AND B.EFFECT='C' AND B.INVOICE_NO ='' AND B.MODULE_ID <>65 "
                + "AND B.GRN_NO ='' AND (B.MATCHED=0 OR B.MATCHED IS NULL) "
                + "GROUP BY B.SUB_ACCOUNT_CODE "
                + "ORDER BY B.SUB_ACCOUNT_CODE) BAL "
                + "SET SUB_ADV_BAL= AMOUNT "
                + "WHERE SUB_PARTY_CODE=SUB_ACCOUNT_CODE");

        //UPDATE TABLE INV_PROCESS
    }

}
