/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.FeltInvReport;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.FeltInvReport.NumWord;
import EITLERP.FeltSales.FeltInvReport.clsFeltSalesInvoice;
import EITLERP.Finance.FinanceGlobal;
import EITLERP.Finance.UtilFunctions;
import EITLERP.Finance.clsAccount;
import EITLERP.Finance.clsVoucher;
import EITLERP.ReportRegister;
import EITLERP.clsFirstFree;
import EITLERP.clsPlaceOfSupply;
import EITLERP.clsSales_Party;
import EITLERP.clsUser;
import EITLERP.data;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class clsFeltJSPInvProcess {

    private EITLERP.FeltSales.common.FeltInvCalc inv_calculation;
    public static String gUserName = "";
    boolean PostSJ = false;

    public void ProcessJSPInvoice() {
        String AsOnDate = EITLERPGLOBAL.getCurrentDateDB();
        String MainCode = "210010";
        int cnt = 0;
        int Counter = 0;
        gUserName = clsUser.getUserName(2, EITLERPGLOBAL.gUserID);

        ResultSet rsPartyCode = null, rsBaleDetail = null, rsPieceDetail = null, rsUpdatePiece = null, rsProdDetail = null, rsDetail = null, rsUpdateDisc = null;

        try {
            if (1 == 1) {

                System.out.println("START : Piece Deail Updation");
                        UpdatePieceDetail();
                        System.out.println("END : Piece Deail Updation");

//-------------------------------------------------------------------                        
                        System.out.println("START : Invoice Value Calculation");
                        //---------------------------------------------------
                        String Detail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS";
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
//                                
                                    String upSQL = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET RATE='" + Rate + "',BAS_AMT='" + BasAmount + "',DISC_PER='" + DiscPer + "',DISC_AMT='" + DiscAmt + "',DISC_BAS_AMT='" + DiscBasamt + "',EXCISE='" + Excise + "',SEAM_CHG='" + SeamChg + "',INSURANCE_AMT='" + InsAmt + "',CHEM_TRT_CHG='" + ChemTrtChg + "',PIN_CHG='" + PinChg + "',SPIRAL_CHG='" + SpiralChg + "',INS_IND='" + InsInd + "',CST2='" + cst2 + "',VAT1='" + vat1 + "',CST5='" + cst5 + "',VAT4='" + vat4 + "',SD_AMT='" + SD + "',IGST_AMT='" + IGst + "',CGST_AMT='" + CGst + "',SGST_AMT='" + SGst + "',IGST_PER='" + IGstper + "',CGST_PER='" + CGstper + "',SGST_PER='" + SGstper + "',INVOICE_AMT='" + InvAmt + "',SANC_GROUP='" + SancGrp + "',SANC_DOC='" + SancDoc + "',AOSD_PER='" + aosd_per + "',AOSD_AMT='" + aosd_amt + "' WHERE PIECE_NO='" + pieceNo + "' ";
                                    data.Execute(upSQL);
                                } else {
                                    JOptionPane.showMessageDialog(null, inv_calculation.getReason());
                                }

//                        String chargeCd = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '"+partyCd+"' ");
//                        String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET CHARGE_CODE='" + chargeCd + "' WHERE PARTY_CODE = '"+partyCd+"' ";
//                        data.Execute(upSQL1);
                                rsDetail.next();
                            }
                        }
                        System.out.println("END : Invoice Value Calculation");

                        //---------------------------------------------------
                        //ENTER DETAIL INTO HEADER GROUP BY BALE NO
                        System.out.println("START : Header Creation Group by Bale No");
                        int lotNo = data.getIntValueFromDB("SELECT MAX(LOT_NO) FROM PRODUCTION.FELT_SALES_INV_PROCESS_REPORT_DATA WHERE SUBSTRING(PROCESSING_TIME,1,10)='" + EITLERPGLOBAL.getCurrentDateDB() + "'");
                        lotNo += 1;
                        String insertHeader = "INSERT INTO TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER (BALE_NO,PACKING_DATE,PARTY_CODE,NO_OF_PIECES,INVOICE_AMT,BAS_AMT,DISC_AMT,DISC_BAS_AMT,EXCISE,SEAM_CHG,INSURANCE_AMT,CHEM_TRT_CHG,PIN_CHG,SPIRAL_CHG,CST,VAT,SD_AMT,CST2,VAT1,CST5,VAT4,LOT_NO,IGST_PER,IGST_AMT,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,PRODUCT_CODE,PIECE_NO,MACHINE_NO,POSITION_NO,SYN_PER,LENGTH,WIDTH,GSM,STYLE,RATE,SQMTR,ACTUAL_WEIGHT,DISC_PER,CHECK_POINT_REMARK,PO_NO,PO_DATE,AOSD_PER,AOSD_AMT) SELECT BALE_NO,PACKING_DATE,PARTY_CODE,COUNT(PIECE_NO),SUM(INVOICE_AMT),SUM(BAS_AMT),SUM(DISC_AMT),SUM(DISC_BAS_AMT),SUM(EXCISE),SUM(SEAM_CHG),SUM(INSURANCE_AMT),SUM(CHEM_TRT_CHG),SUM(PIN_CHG),SUM(SPIRAL_CHG),SUM(CST),SUM(VAT),SUM(SD_AMT),SUM(CST2),SUM(VAT1),SUM(CST5),SUM(VAT4),'" + lotNo + "',IGST_PER,ROUND(SUM(IGST_AMT)),CGST_PER,ROUND(SUM(CGST_AMT)),SGST_PER,ROUND(SUM(SGST_AMT)),PRODUCT_CODE,GROUP_CONCAT(PIECE_NO),MACHINE_NO,POSITION_NO,SYN_PER,ACTUAL_LENGTH,ACTUAL_WIDTH,GSM,STYLE,RATE,SUM(SQMTR),SUM(ACTUAL_WEIGHT),DISC_PER,'',GROUP_CONCAT(PO_NO),GROUP_CONCAT(PO_DATE),AOSD_PER,SUM(AOSD_AMT) FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS GROUP BY BALE_NO";
                        data.Execute(insertHeader);
                        System.out.println("END : Header Creation Group by Bale No");

//-------------------------------------------------------------------
                        System.out.println("START : Checking of Party/Machine/Position Closure");
                        String closureDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' ORDER BY BALE_NO";
                        ResultSet rsclosureDetail = data.getResult(closureDetail);

                        rsclosureDetail.first();
                        if (rsclosureDetail.getRow() > 0) {
                            while (!rsclosureDetail.isAfterLast()) {
                                String baleNo = rsclosureDetail.getString("BALE_NO");
                                String partyCd = rsclosureDetail.getString("PARTY_CODE");
                                String machineNo = rsclosureDetail.getString("MACHINE_NO");
                                String positionNo = rsclosureDetail.getString("POSITION_NO");

                                if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CLOSE_IND=1 AND PARTY_CODE='" + partyCd + "' ")) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PARTY CLOSED IN PARTY MASTER' WHERE BALE_NO='" + baleNo + "' ");
                                } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_HEADER WHERE MACHINE_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCd + "' AND MM_MACHINE_NO='" + machineNo + "' ")) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PARTY MACHINE CLOSED IN MACHINE MASTER' WHERE BALE_NO='" + baleNo + "' ");
                                } else if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_MACHINE_MASTER_DETAIL WHERE POSITION_CLOSE_IND=1 AND MM_PARTY_CODE='" + partyCd + "' AND MM_MACHINE_NO='" + machineNo + "' AND MM_MACHINE_POSITION='" + positionNo + "' ")) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PARTY MACHINE POSITION CLOSED IN MACHINE MASTER' WHERE BALE_NO='" + baleNo + "' ");
                                }

                                rsclosureDetail.next();
                            }
                        }
                        System.out.println("END : Checking of Party/Machine/Position Closure");

//-------------------------------------------------------------------------
//                        System.out.println("START : Checking of Obsolete Pieces");
//                        String obsoleteDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' ORDER BY BALE_NO";
//                        ResultSet rsobsoleteDetail = data.getResult(obsoleteDetail);
//
//                        rsobsoleteDetail.first();
//                        if (rsobsoleteDetail.getRow() > 0) {
//                            while (!rsobsoleteDetail.isAfterLast()) {
//                                String baleNo = rsobsoleteDetail.getString("BALE_NO");
//                                String pieceNo = rsobsoleteDetail.getString("PIECE_NO");
//                                
//                                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + pieceNo + "' AND PR_DELINK IN ('DELINK','OBSOLETE') ")) {
//                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='OBSOLETE PIECE' WHERE BALE_NO='" + baleNo + "' ");
//                                }
//                                
//                                rsobsoleteDetail.next();
//                            }
//                        }
//                        System.out.println("END : Checking of Obsolete Pieces");
//-------------------------------------------------------------------------                        
                        System.out.println("START : Party Master Updation Pending");
                        String PUDetail = "SELECT DISTINCT PARTY_CODE FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER ORDER BY PARTY_CODE";
                        ResultSet rsPUDetail = data.getResult(PUDetail);

                        rsPUDetail.first();
                        if (rsPUDetail.getRow() > 0) {
                            while (!rsPUDetail.isAfterLast()) {
                                if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_AMEND_MASTER WHERE APPROVED=0 AND CANCELLED=0 AND MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + rsPUDetail.getString("PARTY_CODE") + "' ")) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PENDING FINAL APPROVAL IN PARTY UPDATION' WHERE PARTY_CODE='" + rsPUDetail.getString("PARTY_CODE") + "'");
                                }
                                rsPUDetail.next();
                            }
                        }
                        System.out.println("END : Party Master Updation Pending");

                        System.out.println("START : Discount Master Pending");
                        String DMDetail = "SELECT DISTINCT PARTY_CODE FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' ORDER BY PARTY_CODE";
                        ResultSet rsDMDetail = data.getResult(DMDetail);

                        rsDMDetail.first();
                        if (rsDMDetail.getRow() > 0) {
                            while (!rsDMDetail.isAfterLast()) {
                                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_HEADER WHERE APPROVED=0 AND CANCELED=0 AND PARTY_CODE='" + rsDMDetail.getString("PARTY_CODE") + "' ")) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PENDING FINAL APPROVAL IN DISC MASTER' WHERE PARTY_CODE='" + rsDMDetail.getString("PARTY_CODE") + "'");
                                }
                                rsDMDetail.next();
                            }
                        }
                        System.out.println("END : Discount Master Pending");

                        System.out.println("START : Checking of Modification Form pending entry");
                        String invmodDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' ORDER BY BALE_NO";
                        ResultSet rsinvmodDetail = data.getResult(invmodDetail);

                        rsinvmodDetail.first();
                        if (rsinvmodDetail.getRow() > 0) {
                            while (!rsinvmodDetail.isAfterLast()) {
                                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND APPROVED=0 AND CANCELED=0 AND BALE_NO='" + rsinvmodDetail.getString("BALE_NO").trim() + "' AND PARTY_CODE='" + rsinvmodDetail.getString("PARTY_CODE").trim() + "' ")) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PENDING FINAL APPROVAL IN PARAMETER MODIFICATION' WHERE BALE_NO='" + rsinvmodDetail.getString("BALE_NO").trim() + "' AND PARTY_CODE='" + rsinvmodDetail.getString("PARTY_CODE").trim() + "' ");
                                }
                                rsinvmodDetail.next();
                            }
                        }
                        System.out.println("END : Checking of Modification Form pending entry");

//-------------------------------------------------------------------  
                        ////------------------------------------------------------------------------------------------------------
                        //---------------------------------------------------
                        //REMARK UPDATION
                        ResultSet remark1 = null;
                        String rmkDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK=''";
                        remark1 = data.getResult(rmkDetail);
                        remark1.first();
                        if (remark1.getRow() > 0) {
                            while (!remark1.isAfterLast()) {
                                String partyCd = remark1.getString("PARTY_CODE");
                                float GST_PER = remark1.getFloat("CGST_PER") + remark1.getFloat("SGST_PER") + remark1.getFloat("IGST_PER");
                                if (!data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND APPROVED=1 AND CANCELLED=0 AND PARTY_CODE = '" + partyCd + "'")) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PARTY CODE MISSING' WHERE PARTY_CODE='" + partyCd + "'");
                                } else {
                                    if (GST_PER <= 0) {
                                        data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='GSTIN NO/STATE GST CODE MISSING' WHERE PARTY_CODE='" + partyCd + "'");
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
                        String chrgCdDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER ";
                        rsChrgCd = data.getResult(chrgCdDetail);
                        rsChrgCd.first();
                        if (rsChrgCd.getRow() > 0) {
                            while (!rsChrgCd.isAfterLast()) {

                                Counter++;

                                String partyCd = rsChrgCd.getString("PARTY_CODE");

                                String chargeCd = data.getStringValueFromDB("SELECT CHARGE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                                //int transportCd = data.getIntValueFromDB("SELECT TRANSPORTER_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                                //String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHARGE_CODE='" + chargeCd + "',TRANSPORTER_CODE='" + transportCd + "' WHERE PARTY_CODE = '" + partyCd + "' ";
                                String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHARGE_CODE='" + chargeCd + "' WHERE PARTY_CODE = '" + partyCd + "' ";
                                data.Execute(upSQL1);

                                boolean uncheck = data.getBoolValueFromDB("SELECT CRITICAL_LIMIT_UNCHECK FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE = 210010 AND PARTY_CODE = '" + partyCd + "' ");
                                if (uncheck) {
                                    double uncheckLimit = Double.valueOf(1000000000.00);
                                    String unchkLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT='" + uncheckLimit + "' WHERE PARTY_CODE='" + partyCd + "' ";
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
                        String trnsCdDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' ORDER BY BALE_NO";
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
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='02-TR' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (transMode.equals("BY ANGADIA")) {
                                    int transportCd = Integer.parseInt("95");
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='01-ANG' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (transMode.equals("BY TRAIN")) {
                                    int transportCd = Integer.parseInt("97");
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='03-PT' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (transMode.equals("BY IRPP")) {
                                    int transportCd = Integer.parseInt("41");
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='04-IRP' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (transMode.equals("BY HANDDELIVERY")) {
                                    int transportCd = Integer.parseInt("98");
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='05-HD' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (transMode.equals("BY AIR")) {
                                    int transportCd = Integer.parseInt("99");
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET TRANSPORTER_CODE='" + transportCd + "',DESP_MODE='06-AIR' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }

                                rsTrnsCd.next();
                            }
                        }
                        System.out.println("END : Transporter Code Updation");

//------------------------------------------------------------------------------
                        //PARTY DATA UPDATION
                        System.out.println("START : Party Data Updation");
                        //String upPartyData = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_NAME=D.PARTY_NAME,I.PARTY_CHARGE_CODE=D.CHARGE_CODE,I.ADDRESS1=D.ADDRESS1,I.ADDRESS2=D.ADDRESS2,I.PINCODE=D.PINCODE,I.CITY_ID=D.CITY_ID,I.DISPATCH_STATION=D.DISPATCH_STATION,I.CITY_NAME=D.CITY_NAME,I.DOCUMENT_THROUGH=D.DOCUMENT_THROUGH,I.GSTIN_NO=D.GSTIN_NO WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND D.APPROVED=1 AND D.CANCELLED=0";
//                        String upPartyData = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_NAME=D.PARTY_NAME,I.PARTY_CHARGE_CODE=D.CHARGE_CODE,I.ADDRESS1=D.ADDRESS1,I.ADDRESS2=D.ADDRESS2,I.PINCODE=D.PINCODE,I.CITY_ID=D.CITY_ID,I.DISPATCH_STATION=D.DISPATCH_STATION,I.CITY_NAME=D.CITY_NAME,I.DOCUMENT_THROUGH=D.DOCUMENT_THROUGH,I.GSTIN_NO=CASE WHEN D.GSTIN_NO='' THEN D.STATE_GST_CODE ELSE TRIM(D.GSTIN_NO) END WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND D.APPROVED=1 AND D.CANCELLED=0 ";
                        String upPartyData = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_NAME=D.PARTY_NAME,I.PARTY_CHARGE_CODE=D.CHARGE_CODE,I.ADDRESS1=D.ADDRESS1,I.ADDRESS2=D.ADDRESS2,I.PINCODE=D.PINCODE,I.CITY_ID=D.CITY_ID,I.DISPATCH_STATION=D.DISPATCH_STATION,I.CITY_NAME=D.CITY_NAME,I.DOCUMENT_THROUGH=D.DOCUMENT_THROUGH,I.CREDIT_DAYS=D.CREDIT_DAYS,I.GSTIN_NO=CASE WHEN D.GSTIN_NO='' THEN D.STATE_GST_CODE ELSE TRIM(D.GSTIN_NO) END WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND D.APPROVED=1 AND D.CANCELLED=0 ";
                        data.Execute(upPartyData);
                        System.out.println("END : Party Data Updation");

//------------------------------------------------------------------------------
                        //Quality DATA UPDATION
                        System.out.println("START : Quality Data Updation");
                        String upQltData = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,PRODUCTION.FELT_QLT_RATE_MASTER Q SET I.PRODUCT_DESC=Q.PRODUCT_DESC,I.RATE_UNIT=CASE WHEN Q.SQM_IND=1 THEN 'MTR' ELSE 'KG' END WHERE SUBSTRING(I.PRODUCT_CODE,1,6)=Q.PRODUCT_CODE AND Q.APPROVED=1 AND Q.CANCELED=0 AND Q.EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00') ";
                        data.Execute(upQltData);
                        System.out.println("END : Quality Data Updation");

//------------------------------------------------------------------------------
                        //Machine DATA UPDATION
                        System.out.println("START : Machine Data Updation");
                        String upMMData = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,PRODUCTION.FELT_MACHINE_POSITION_MST M SET I.POSITION_DESC=M.POSITION_DESC WHERE I.POSITION_NO=M.POSITION_NO ";
                        data.Execute(upMMData);
                        System.out.println("END : Machine Data Updation");

                        //---------------------------------------------------
                        //ADVANCE AMOUNT PROCESS
                        System.out.println("START : Advance Amount Updation");
                        AdvAmt();
                        //String upAdvAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS,TEMP_DATABASE.TEMP_ADV_AMT SET ADV_AMT=SUB_ADV_BAL WHERE PARTY_CODE = SUB_PARTY_CODE ";
                        String upAdvAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER,TEMP_DATABASE.TEMP_ADV_AMT SET ADV_AMT=SUB_ADV_BAL WHERE PARTY_CODE = SUB_PARTY_CODE AND CHECK_POINT_REMARK='' ";
                        data.Execute(upAdvAmt);
                        System.out.println("END : Advance Amount Updation");

                        //---------------------------------------------------
                        //OUTSTANDING AMOUNT PROCESS
                        System.out.println("START : OutStanding Amount Updation");
                        OutStandingAmt();
                        //String upSubPartyAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS,TEMP_DATABASE.TEMP_OUTSTANDING_AMT SET OUT_STANDING_AMT=SUB_OUTSTANDING_BAL,GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE WHERE PARTY_CODE=SUB_PARTY_CODE";
                        String upSubPartyAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER,TEMP_DATABASE.TEMP_OUTSTANDING_AMT SET OUT_STANDING_AMT=SUB_OUTSTANDING_BAL,GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE WHERE PARTY_CODE=SUB_PARTY_CODE AND CHECK_POINT_REMARK='' ";
                        data.Execute(upSubPartyAmt);
                        //String upGrpPartyAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS,TEMP_DATABASE.TEMP_OUTSTANDING_REPORT SET GRP_OUT_STANDING_AMT=SUB_OUTSTANDING_BAL WHERE GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE";
                        String upGrpPartyAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER,TEMP_DATABASE.TEMP_OUTSTANDING_REPORT SET GRP_OUT_STANDING_AMT=SUB_OUTSTANDING_BAL WHERE GRP_MAIN_PARTY_CODE=MAIN_PARTY_CODE AND CHECK_POINT_REMARK='' ";
                        data.Execute(upGrpPartyAmt);
                        System.out.println("END : OutStanding Amount Updation");

                        //---------------------------------------------------
//                        //GROUP PARTY LIMIT UPDATION
//                        System.out.println("START : Group Party Limit Updation");
//                        //String upGrpPartyLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS I,PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H SET I.PARTY_CRITICAL_LIMIT=D.CRITICAL_LIMIT,I.GRP_CRITICAL_LIMIT=H.GROUP_CRITICAL_LIMIT WHERE D.GROUP_CODE=H.GROUP_CODE AND I.PARTY_CODE=D.PARTY_CODE AND H.APPROVED=1 AND H.CANCELED=0";
//                        String upGrpPartyLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,PRODUCTION.FELT_GROUP_MASTER_DETAIL D,PRODUCTION.FELT_GROUP_MASTER_HEADER H SET I.PARTY_CRITICAL_LIMIT=D.CRITICAL_LIMIT,I.GRP_CRITICAL_LIMIT=H.GROUP_CRITICAL_LIMIT WHERE D.GROUP_CODE=H.GROUP_CODE AND I.PARTY_CODE=D.PARTY_CODE AND H.APPROVED=1 AND H.CANCELED=0";
//                        data.Execute(upGrpPartyLimit);
//                        System.out.println("END : Group Party Limit Updation");
                        //---------------------------------------------------
                        //PARTY LIMIT UPDATION
                        System.out.println("START : Single Party Limit Updation");
                        //String upPartyLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_CRITICAL_LIMIT=D.AMOUNT_LIMIT WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND I.PARTY_CRITICAL_LIMIT=0 AND I.GRP_CRITICAL_LIMIT=0";
                        //String upPartyLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_CRITICAL_LIMIT=D.AMOUNT_LIMIT WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND I.PARTY_CRITICAL_LIMIT=0 AND I.GRP_CRITICAL_LIMIT=0";
                        String upPartyLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_CRITICAL_LIMIT=D.AMOUNT_LIMIT WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND D.APPROVED=1 AND D.CANCELLED=0 AND I.CHECK_POINT_REMARK='' ";
                        data.Execute(upPartyLimit);
                        System.out.println("END : Single Party Limit Updation");

//                //---------------------------------------------------
//                        //INVOICE PARAMETER MODIFICATION LIMIT UPDATION
//                        System.out.println("START : Invoice Modification Parameter Limit Updation");
//                        ResultSet rsLimit = null;
//                        String upLimit;
//                        //String limitDetail = "SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND APPROVED=1 AND CANCELED=0";
//                        String limitDetail = "SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND APPROVED=1 AND CANCELED=0";
//                        rsLimit = data.getResult(limitDetail);
//                        rsLimit.first();
//                        if (rsLimit.getRow() > 0) {
//                            while (!rsLimit.isAfterLast()) {
//                                
//                                Counter++;
//                                
//                                Bar.setValue(Counter);
//                                Bar.repaint();
//                                
//                                String bale = rsLimit.getString("BALE_NO");
//                                String partyCd = rsLimit.getString("PARTY_CODE");
//                                String crgCd = rsLimit.getString("CHARGE_CODE_NEW");
//                                String trnCd = rsLimit.getString("TRANSPORTER_CODE");
//                                String vehicalNo = rsLimit.getString("VEHICLE_NO");
//                                String advDn = rsLimit.getString("ADV_DOC_NO");
//                                String cnd = "";
////                                if (rsLimit.getString("CHARGE_CODE_NEW").equalsIgnoreCase("") || rsLimit.getString("CHARGE_CODE_NEW") == null) {
////                                    cnd = "";
////                                } else {
////                                    cnd = ",CHARGE_CODE='" + crgCd + "' ";
////                                }
//                                if (rsLimit.getString("CHARGE_CODE_NEW").equals("01")) {
//                                    cnd = ",CHARGE_CODE='" + crgCd + "' ";
//                                }
//                                if (rsLimit.getString("TRANSPORTER_CODE").equalsIgnoreCase("") || rsLimit.getString("TRANSPORTER_CODE") == null) {
//                                    cnd += "";
//                                } else {
//                                    cnd += ",TRANSPORTER_CODE='" + trnCd + "' ";
//                                }
//                                if (rsLimit.getString("VEHICLE_NO").equalsIgnoreCase("") || rsLimit.getString("VEHICLE_NO") == null) {
//                                    cnd += "";
//                                } else {
//                                    cnd += ",VEHICLE_NO='" + vehicalNo + "' ";
//                                }
//                                if (rsLimit.getString("ADV_DOC_NO").equalsIgnoreCase("") || rsLimit.getString("ADV_DOC_NO") == null) {
//                                    cnd += "";
//                                } else {
//                                    cnd += ",ADV_DOC_NO='" + advDn + "' ";
//                                }
//                                System.out.println("condition : " + cnd);
//                                float criticalLimit = rsLimit.getFloat("CRITICAL_LIMIT_NEW");
//                                float InvAmt = rsLimit.getFloat("ADV_AGN_INV_AMT");
//                                float IGSTAmt = rsLimit.getFloat("ADV_AGN_IGST_AMT");
//                                float SGSTAmt = rsLimit.getFloat("ADV_AGN_SGST_AMT");
//                                float CGSTAmt = rsLimit.getFloat("ADV_AGN_CGST_AMT");
//                                float GSTCompCessAmt = rsLimit.getFloat("ADV_AGN_GST_COMP_CESS_AMT");
//                                
//                                
//                                String[] bNo = bale.split(",");
//                                System.out.println("BALE NO is : " + bale);
//                                if (!bale.equals("")) {
//                                    for (int i = 0; i < bNo.length; i++) {
//                                        bNo[i] = bNo[i].replaceAll("[^\\w]", ",");
//                                        //upLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET PARTY_CRITICAL_LIMIT='"+criticalLimit+"',CHARGE_CODE='"+crgCd+"',CRITICAL_LIMIT_AMT='"+criticalLimit+"' WHERE BALE_NO='"+bNo[i]+"'";
//                                        upLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_CRITICAL_LIMIT='" + criticalLimit + "',CRITICAL_LIMIT_AMT='" + criticalLimit + "',ADV_AGN_INV_AMT='" + InvAmt + "',ADV_AGN_IGST_AMT='" + IGSTAmt + "',ADV_AGN_SGST_AMT='" + SGSTAmt + "',ADV_AGN_CGST_AMT='" + CGSTAmt + "',ADV_AGN_GST_COMP_CESS_AMT='" + GSTCompCessAmt + "'" + cnd + " WHERE BALE_NO='" + bNo[i] + "'";
//                                        data.Execute(upLimit);
//                                    }
//                                } 
////                                else {
////                                    //upLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET PARTY_CRITICAL_LIMIT='"+criticalLimit+"',CHARGE_CODE='"+crgCd+"',CRITICAL_LIMIT_AMT='"+criticalLimit+"' WHERE PARTY_CODE='"+partyCd+"'";
////                                    upLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_CRITICAL_LIMIT='" + criticalLimit + "',CRITICAL_LIMIT_AMT='" + criticalLimit + "'" + cnd + " WHERE PARTY_CODE='" + partyCd + "'";
////                                    data.Execute(upLimit);
////                                }
//                                
//                                if (rsLimit.getBoolean("WITHOUT_CRITICAL_LIMIT")) {
//                                    double noLimit = Double.valueOf(999999999.99);
//                                    upLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT='" + noLimit + "' WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='"+bale+"'";
//                                    data.Execute(upLimit);
//                                }
//                                
//                                rsLimit.next();
//                            }
//                        }
//                        System.out.println("END : Invoice Modification Parameter Limit Updation");
//                //String upLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS I,DINESHMILLS.D_SAL_PARTY_MASTER D SET I.PARTY_CRITICAL_LIMIT=D.AMOUNT_LIMIT WHERE I.PARTY_CODE=D.PARTY_CODE AND D.MAIN_ACCOUNT_CODE=210010 AND I.PARTY_CRITICAL_LIMIT=0 AND I.GRP_CRITICAL_LIMIT=0";
//                        //data.Execute(upLimit);
//-------------------------------------------------------------------------------                        
                        //Invoice Modification Updation
                        System.out.println("START : Invoice Modification Updation");
                        ResultSet rsInvModCd = null;
                        String InvModCdDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' ORDER BY BALE_NO";
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
                                    String sql90 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHARGE_CODE='" + chargCd + "',CRITICAL_LIMIT_AMT='" + NoLimit + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql90);
                                }

                                String trnsCd = "";
                                trnsCd = data.getStringValueFromDB("SELECT TRANSPORTER_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND (TRANSPORTER_CODE IS NOT NULL OR TRANSPORTER_CODE!='') ORDER BY DOC_NO DESC");
                                if (!trnsCd.equalsIgnoreCase("")) {
                                    String sql91 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET TRANSPORTER_CODE='" + trnsCd + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql91);
                                }

                                String vehicalNo = "";
                                vehicalNo = data.getStringValueFromDB("SELECT VEHICLE_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND (VEHICLE_NO IS NOT NULL OR VEHICLE_NO!='') ORDER BY DOC_NO DESC");
                                if (!vehicalNo.equalsIgnoreCase("")) {
                                    String sql92 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET VEHICLE_NO='" + vehicalNo + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql92);
                                }

                                String advNo = "";
                                advNo = data.getStringValueFromDB("SELECT ADV_DOC_NO FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND (ADV_DOC_NO IS NOT NULL OR ADV_DOC_NO!='') ORDER BY DOC_NO DESC");
                                if (!advNo.equalsIgnoreCase("")) {
                                    String sql93 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET ADV_DOC_NO='" + advNo + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql93);
                                }

                                double criticalLimit = 0, InvAmt = 0, IGSTAmt = 0, SGSTAmt = 0, CGSTAmt = 0, GSTCompCessAmt = 0;

                                criticalLimit = data.getDoubleValueFromDB("SELECT CRITICAL_LIMIT_NEW FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND CRITICAL_LIMIT_NEW>0 ORDER BY DOC_NO DESC");
                                if (criticalLimit > 0) {
                                    String sql94 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_CRITICAL_LIMIT='" + criticalLimit + "',CRITICAL_LIMIT_AMT='" + criticalLimit + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql94);
                                }

                                InvAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_INV_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_INV_AMT>0 ORDER BY DOC_NO DESC");
                                if (InvAmt > 0) {
                                    String sql95 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET ADV_AGN_INV_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql95);
                                }

                                IGSTAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_IGST_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_IGST_AMT>0 ORDER BY DOC_NO DESC");
                                if (InvAmt > 0) {
                                    String sql96 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET ADV_AGN_IGST_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql96);
                                }

                                SGSTAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_SGST_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_SGST_AMT>0 ORDER BY DOC_NO DESC");
                                if (InvAmt > 0) {
                                    String sql97 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET ADV_AGN_SGST_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql97);
                                }

                                CGSTAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_CGST_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_CGST_AMT>0 ORDER BY DOC_NO DESC");
                                if (InvAmt > 0) {
                                    String sql98 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET ADV_AGN_CGST_AMT='" + InvAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql98);
                                }

                                GSTCompCessAmt = data.getDoubleValueFromDB("SELECT ADV_AGN_GST_COMP_CESS_AMT FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND ADV_AGN_GST_COMP_CESS_AMT>0 ORDER BY DOC_NO DESC");
                                if (GSTCompCessAmt > 0) {
                                    String sql99 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET ADV_AGN_GST_COMP_CESS_AMT='" + GSTCompCessAmt + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sql99);
                                }

                                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND BALE_NO='" + baleNo + "' AND BALE_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' AND APPROVED=1 AND CANCELED=0 AND WITHOUT_CRITICAL_LIMIT=1")) {
                                    double noLimit = Double.valueOf(1000000000.00);
                                    String upLimitUp = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT='" + noLimit + "' WHERE BALE_NO='" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE='" + partyCd + "' ";
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
                        String PDCDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' AND CHARGE_CODE='08' ORDER BY BALE_NO";
                        rsPDC = data.getResult(PDCDetail);
                        rsPDC.first();
                        if (rsPDC.getRow() > 0) {
                            while (!rsPDC.isAfterLast()) {

                                String pcNo = rsPDC.getString("PIECE_NO");
                                String partyCd = rsPDC.getString("PARTY_CODE");

                                if (data.IsRecordExist("SELECT PDC_PARTY_CODE,PDC_PIECE_NO,PDC_BILLING_DATE FROM PRODUCTION.FELT_PDC_HEADER H,PRODUCTION.FELT_PDC_BANK_DETAIL B,PRODUCTION.FELT_PDC_PIECE_DETAIL P WHERE H.PDC_DOC_NO=P.PDC_DOC_NO AND H.PDC_DOC_NO=B.PDC_DOC_NO AND H.APPROVED=1 AND H.CANCELED=0 AND P.PDC_PIECE_STATUS IN ('INSERT','ADD') AND H.PDC_PARTY_CODE='" + partyCd + "' AND P.PDC_PIECE_NO='" + pcNo + "' AND B.PDC_BILLING_DATE<=CURDATE() ORDER BY PDC_PIECE_NO,PDC_BILLING_DATE DESC")) {
                                    double noLimit = Double.valueOf(1000000000.00);
                                    String upLimitUp = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT='" + noLimit + "',CHECK_POINT_REMARK='' WHERE PARTY_CODE='" + partyCd + "' AND PIECE_NO='" + pcNo + "' ";
                                    data.Execute(upLimitUp);
                                } else {
                                    String upLimitUp = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='PDC Not Found' WHERE PARTY_CODE='" + partyCd + "' AND PIECE_NO='" + pcNo + "' ";
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
                        data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK = 'TRANSPORTER CODE MISSING' WHERE TRANSPORTER_CODE=0 AND CHECK_POINT_REMARK=''");

////----------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------
                        //Transporter Name DATA UPDATION
                        System.out.println("START : Transporter Name Data Updation");
                        String upTRData = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_TRANSPORTER_MASTER T SET I.TRANSPORTER_NAME=T.TRANSPORTER_NAME WHERE I.TRANSPORTER_CODE=T.TRANSPORTER_ID AND T.COMPANY_ID=2 AND I.CHECK_POINT_REMARK=''";
                        data.Execute(upTRData);
                        System.out.println("END : Transporter Name Data Updation");

//------------------------------------------------------------------------------
                        //LC PARTY UPDATION
                        System.out.println("START : LC Party DATA Updation");
                        ResultSet rsLCmst = null;
                        String LCmstDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHARGE_CODE='07' AND CHECK_POINT_REMARK=''";
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
                                    String upSQLC1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='LC MASTER NOT AVAILABLE' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQLC1);
                                } else {
                                    if (!data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC")) {
                                        String upSQLC2 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK='LC EXPIRED' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                        data.Execute(upSQLC2);
                                    } else {
                                        String lcNo = data.getStringValueFromDB("SELECT LC_NO FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                                        String upSQLC3 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET LC_NO='" + lcNo + "' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                        data.Execute(upSQLC3);
                                    }
                                }

                                rsLCmst.next();
                            }
                        }
                        System.out.println("END : LC Party DATA Updation");
//-------------------------------------------------------------------------------------------

//                        //DOCUMENT THROUGH UPDATION
//                        System.out.println("START : Document Through Updation");
//                        ResultSet rsDocThr = null;
//                        String DocThrDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK=''";
//                        rsDocThr = data.getResult(DocThrDetail);
//                        rsDocThr.first();
//                        if (rsDocThr.getRow() > 0) {
//                            while (!rsDocThr.isAfterLast()) {
//
//                                String baleNo = rsDocThr.getString("BALE_NO");
//                                String baleDate = rsDocThr.getString("PACKING_DATE");
//                                String partyCd = rsDocThr.getString("PARTY_CODE");
//                                String chrgeCd = rsDocThr.getString("CHARGE_CODE");
//                                String partyChrgeCd = rsDocThr.getString("PARTY_CHARGE_CODE");
//
//                                if (chrgeCd.equals("01")) {
//                                    if (partyChrgeCd.equals("09")) {
//                                        String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='F6',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
//                                        data.Execute(upSQL1);
//                                    } else {
//                                        double noLimit01 = Double.valueOf(1000000000.00);
//                                        String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER P SET I.CRITICAL_LIMIT_AMT = '" + noLimit01 + "',I.PARTY_BANK_NAME=P.BANK_NAME,I.PARTY_BANK_ADDRESS1=P.BANK_ADDRESS,I.PARTY_BANK_ADDRESS2=P.BANK_CITY WHERE I.PARTY_CODE=P.PARTY_CODE AND I.BALE_NO = '" + baleNo + "' AND I.PACKING_DATE = '" + baleDate + "' AND P.PARTY_CODE = '" + partyCd + "' AND P.MAIN_ACCOUNT_CODE=210010 AND P.APPROVED=1 AND P.CANCELLED=0 ";
//                                        data.Execute(upSQL1);
//                                    }
//                                }
//                                if (chrgeCd.equals("02")) {
//                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
//                                    data.Execute(upSQL1);
//                                }
//                                if (chrgeCd.equals("04")) {
//                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER P SET I.PARTY_BANK_NAME=P.BANK_NAME,I.PARTY_BANK_ADDRESS1=P.BANK_ADDRESS,I.PARTY_BANK_ADDRESS2=P.BANK_CITY WHERE I.PARTY_CODE=P.PARTY_CODE AND I.BALE_NO = '" + baleNo + "' AND I.PACKING_DATE = '" + baleDate + "' AND P.PARTY_CODE = '" + partyCd + "' AND P.MAIN_ACCOUNT_CODE=210010 AND P.APPROVED=1 AND P.CANCELLED=0 ";
//                                    data.Execute(upSQL1);
//                                }
//                                if (chrgeCd.equals("07")) {
//                                    String bnkName = data.getStringValueFromDB("SELECT BANK_NAME FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
//                                    String bnkAds = data.getStringValueFromDB("SELECT BANK_ADDRESS FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
//                                    String bnkCity = data.getStringValueFromDB("SELECT BANK_CITY FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
//                                    double lcAmt = data.getDoubleValueFromDB("SELECT AMT FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
//                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT='" + lcAmt + "',PARTY_BANK_NAME='" + bnkName + "',PARTY_BANK_ADDRESS1='" + bnkAds + "',PARTY_BANK_ADDRESS2='" + bnkCity + "' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
//                                    data.Execute(upSQL1);
//                                }
//                                if (chrgeCd.equals("08")) {
//                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='DIRECT PDC',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
//                                    data.Execute(upSQL1);
//                                }
//                                if (chrgeCd.equals("09")) {
//                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='AGAINST ADVANCE PAYMENT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
//                                    data.Execute(upSQL1);
//                                }
//
//                                rsDocThr.next();
//                            }
//                        }
//                        System.out.println("END : Document Through Updation");
////-------------------------------------------------------------------------------------------
                        //DOCUMENT THROUGH UPDATION
                        System.out.println("START : Document Through AND Payment Terms Updation");
                        ResultSet rsDocThr = null;
                        String DocThrDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK=''";
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
                                        String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='F6',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('AGAINST ADVANCE PAYMENT') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                        data.Execute(upSQL1);
                                    } else {
                                        double noLimit01 = Double.valueOf(1000000000.00);
//                                        String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER P SET I.CRITICAL_LIMIT_AMT = '" + noLimit01 + "',I.PARTY_BANK_NAME=P.BANK_NAME,I.PARTY_BANK_ADDRESS1=P.BANK_ADDRESS,I.PARTY_BANK_ADDRESS2=P.BANK_CITY WHERE I.PARTY_CODE=P.PARTY_CODE AND I.BALE_NO = '" + baleNo + "' AND I.PACKING_DATE = '" + baleDate + "' AND P.PARTY_CODE = '" + partyCd + "' AND P.MAIN_ACCOUNT_CODE=210010 AND P.APPROVED=1 AND P.CANCELLED=0 ";
                                        String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT = '" + noLimit01 + "',PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('20 DAYS CREDIT FROM THE DATE OF INVOICE') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                        data.Execute(upSQL1);
                                    }
                                }
                                if (chrgeCd.equals("02")) {
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('" + CrDays + "',' DAYS CREDIT FROM THE DATE OF INVOICE') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (chrgeCd.equals("04")) {
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER I,DINESHMILLS.D_SAL_PARTY_MASTER P SET I.PARTY_BANK_NAME=P.BANK_NAME,I.PARTY_BANK_ADDRESS1=P.BANK_ADDRESS,I.PARTY_BANK_ADDRESS2=P.BANK_CITY,PAYMENT_TERMS=CONCAT('" + CrDays + "',' DAYS HUNDI THROUGH BANK') WHERE I.PARTY_CODE=P.PARTY_CODE AND I.BALE_NO = '" + baleNo + "' AND I.PACKING_DATE = '" + baleDate + "' AND P.PARTY_CODE = '" + partyCd + "' AND P.MAIN_ACCOUNT_CODE=210010 AND P.APPROVED=1 AND P.CANCELLED=0 ";
                                    data.Execute(upSQL1);
                                }
                                if (chrgeCd.equals("07")) {
                                    String bnkName = data.getStringValueFromDB("SELECT BANK_NAME FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                                    String bnkAds = data.getStringValueFromDB("SELECT BANK_ADDRESS FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                                    String bnkCity = data.getStringValueFromDB("SELECT BANK_CITY FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                                    double lcAmt = data.getDoubleValueFromDB("SELECT AMT FROM PRODUCTION.FELT_SALES_LC_PARTY_MASTER WHERE APPROVED=1 AND CANCELLED=0 AND PARTY_CODE='" + partyCd + "' AND EXP_DATE>=CURDATE() ORDER BY EXP_DATE DESC");
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT='" + lcAmt + "',PARTY_BANK_NAME='" + bnkName + "',PARTY_BANK_ADDRESS1='" + bnkAds + "',PARTY_BANK_ADDRESS2='" + bnkCity + "',PAYMENT_TERMS='AGAINST LETTER OF CREDIT' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (chrgeCd.equals("08")) {
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS=CONCAT('AGAINST ','" + CrDays + "',' DAYS PDC') WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }
                                if (chrgeCd.equals("09")) {
                                    String upSQL1 = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_BANK_NAME='DIRECT',PARTY_BANK_ADDRESS1='',PARTY_BANK_ADDRESS2='',PAYMENT_TERMS='AGAINST ADVANCE PAYMENT' WHERE BALE_NO = '" + baleNo + "' AND PACKING_DATE = '" + baleDate + "' AND PARTY_CODE = '" + partyCd + "' ";
                                    data.Execute(upSQL1);
                                }

                                rsDocThr.next();
                            }
                        }
                        System.out.println("END : Document Through AND Payment Terms Updation");
//-------------------------------------------------------------------------------------------

                        //---------------------------------------------------
                        //ENHANCEMENT PARTY LIMIT UPDATION
                        System.out.println("START : Enhancement Party Limit Updation");
                        ResultSet rsEPCL = null;
                        String EPCLDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE CHECK_POINT_REMARK='' ORDER BY BALE_NO";
                        rsEPCL = data.getResult(EPCLDetail);
                        rsEPCL.first();
                        if (rsEPCL.getRow() > 0) {
                            while (!rsEPCL.isAfterLast()) {

                                String baleNo = rsEPCL.getString("BALE_NO");
                                String baleDate = rsEPCL.getString("PACKING_DATE");
                                String partyCd = rsEPCL.getString("PARTY_CODE");
                                double criticalLimit = 0;

                                String sql = "SELECT H.DOC_NO,H.DOC_DATE,H.PARTY_CODE,H.PROCESSING_DATE,H.APPROVED,H.CANCELED, ";
                                sql += "D.BALE_NO,D.BALE_DATE,E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT ";
                                sql += "FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H, ";
                                sql += "PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL D, ";
                                sql += "PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL_ENHANCEMENT E ";
                                sql += "WHERE H.DOC_NO=D.DOC_NO AND H.DOC_DATE=D.DOC_DATE AND H.PARTY_CODE=D.PARTY_CODE ";
                                sql += "AND H.PARTY_CODE='" + partyCd + "' AND H.PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' ";
                                sql += "AND D.BALE_NO='" + baleNo + "' AND D.BALE_DATE='" + baleDate + "' ";
                                sql += "AND H.APPROVED=1 AND H.CANCELED=0 ";
                                sql += "AND H.DOC_NO=E.DOC_NO AND H.DOC_DATE=E.DOC_DATE AND H.PARTY_CODE=E.GROUP_PARTY_CODE ";
                                sql += "AND E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT>0 ";
                                sql += "ORDER BY H.DOC_NO DESC ";

                                if (data.IsRecordExist(sql)) {
                                    String enLimit = data.getStringValueFromDB("SELECT E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT FROM PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_HEADER H,PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL D,PRODUCTION.FELT_GROUP_CRITICAL_LIMIT_DETAIL_ENHANCEMENT E WHERE H.DOC_NO=D.DOC_NO AND H.DOC_DATE=D.DOC_DATE AND H.PARTY_CODE=D.PARTY_CODE AND H.PARTY_CODE='" + partyCd + "' AND H.PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND D.BALE_NO='" + baleNo + "' AND D.BALE_DATE='" + baleDate + "' AND H.APPROVED=1 AND H.CANCELED=0 AND H.DOC_NO=E.DOC_NO AND H.DOC_DATE=E.DOC_DATE AND H.PARTY_CODE=E.GROUP_PARTY_CODE AND E.GROUP_ENHANCE_PARTY_CRITICAL_LIMIT>0 ORDER BY H.DOC_NO DESC ");
                                    criticalLimit = Double.parseDouble(enLimit);

                                    String sqlEL = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET PARTY_CRITICAL_LIMIT='" + criticalLimit + "',CRITICAL_LIMIT_AMT='" + criticalLimit + "' WHERE BALE_NO='" + baleNo + "' AND PARTY_CODE='" + partyCd + "' ";
                                    data.Execute(sqlEL);
                                }

                                rsEPCL.next();
                            }
                        }
                        System.out.println("END : Enhancement Party Limit Updation");
//-------------------------------------------------------------------------------------------

                        //ACTUAL LIMIT OF INVOICE PROCESS UPDATION
                        System.out.println("START : Actual Limit For Invoicing Updation");
                        //String upacLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET CRITICAL_LIMIT_AMT = CASE WHEN CHARGE_CODE='09' THEN CASE WHEN PARTY_CRITICAL_LIMIT>0 THEN PARTY_CRITICAL_LIMIT ELSE ADV_AMT END ELSE ROUND(PARTY_CRITICAL_LIMIT-OUT_STANDING_AMT,2) END WHERE CRITICAL_LIMIT_AMT=0";
                        String upacLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT = CASE WHEN CHARGE_CODE='09' THEN CASE WHEN PARTY_CRITICAL_LIMIT>0 AND PARTY_CRITICAL_LIMIT!=1 THEN PARTY_CRITICAL_LIMIT ELSE ADV_AMT END ELSE ROUND(PARTY_CRITICAL_LIMIT-OUT_STANDING_AMT,2) END WHERE CRITICAL_LIMIT_AMT=0 AND CHECK_POINT_REMARK=''";
                        data.Execute(upacLimit);
                        //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
                        data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
                        System.out.println("END : Actual Limit For Invoicing Updation");

////-------------------------------------------------------------------------------------------------------------------------------
//                        //CODE ADDES ON 06/10/2017 FOR MAKING INVOICEING WITHOUT CHECKING LIMITS
//                        System.out.println("START : MAKING INVOICEING WITHOUT CHECKING LIMITS");
//                        //String upacLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET CRITICAL_LIMIT_AMT = CASE WHEN CHARGE_CODE='09' THEN CASE WHEN PARTY_CRITICAL_LIMIT>0 THEN PARTY_CRITICAL_LIMIT ELSE ADV_AMT END ELSE ROUND(PARTY_CRITICAL_LIMIT-OUT_STANDING_AMT,2) END WHERE CRITICAL_LIMIT_AMT=0";
//                        double noLmt = Double.valueOf(999999999.99);
//                        String nLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT = '" + noLmt + "'";
//                        data.Execute(nLimit);
//                        //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
//                        data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET INV_CRITICAL_LIMIT_AMT = CRITICAL_LIMIT_AMT");
//                        System.out.println("END : MAKING INVOICEING WITHOUT CHECKING LIMITS");
////-------------------------------------------------------------------------------------------------------------------------------
                        //---------------------------------------------------
                        //CHECK CRITICAL LIMIT AMOUNT FOR INVOICING PROCESS
                        System.out.println("START : Check Critical Amount");
                        String chkCriticalLimitZero = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CHECK_POINT_REMARK=CASE WHEN CHARGE_CODE='09' THEN 'ADV BAL IS ZERO' ELSE CASE WHEN CHARGE_CODE='07' THEN 'LC BAL IS ZERO' ELSE 'CREDIT BAL IS ZERO' END END WHERE CRITICAL_LIMIT_AMT=0 AND CHECK_POINT_REMARK='' ";
                        data.Execute(chkCriticalLimitZero);
                        //String chkCriticalLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET FLAG = 1 WHERE CRITICAL_LIMIT_AMT>=0";
                        String chkCriticalLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET FLAG = 1 WHERE CRITICAL_LIMIT_AMT>0 AND CHECK_POINT_REMARK='' ";
                        data.Execute(chkCriticalLimit);
                        System.out.println("END : Check Critical Amount");

                        //CHECK GST AMOUNT FOR INVOICING PROCESS
                        System.out.println("START : Check GST Amount");
                        //String chkCriticalLimit = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET FLAG = 1 WHERE CRITICAL_LIMIT_AMT>=0";
                        String chkGSTAmt = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET FLAG = 0,CHECK_POINT_REMARK='GSTIN/STATE NO MISSING' WHERE IGST_AMT+CGST_AMT+SGST_AMT=0 AND CHECK_POINT_REMARK=''";
                        data.Execute(chkGSTAmt);
                        System.out.println("END : Check GST Amount");

                        //CALCULATION OF INVOICE AMOUNT UPTO IT'S LIMIT
                        System.out.println("START : Invoice Amount Calculation till it's Limit");
                        ResultSet rsCalc = null;
                        String upCalc;
                        //String calcDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS WHERE FLAG=1 ORDER BY PARTY_CODE,BALE_NO,PIECE_NO";
                        String calcDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE FLAG=1 AND CHECK_POINT_REMARK='' ORDER BY PARTY_CODE,BALE_NO";
                        rsCalc = data.getResult(calcDetail);
                        rsCalc.first();
                        if (rsCalc.getRow() > 0) {
                            while (!rsCalc.isAfterLast()) {

                                Counter++;

                                String baleNo = rsCalc.getString("BALE_NO");
                                String partyCd = rsCalc.getString("PARTY_CODE");
                                //String pieceNo = rsCalc.getString("PIECE_NO");
//                                float invAmt = rsCalc.getFloat("INVOICE_AMT");
                                float invAmt = Math.round(rsCalc.getFloat("INVOICE_AMT"));
                                //double invCriticalAmt = data.getDoubleValueFromDB("SELECT INV_CRITICAL_LIMIT_AMT FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS WHERE PARTY_CODE='"+partyCd+"' AND PIECE_NO='"+pieceNo+"'");
                                double invCriticalAmt = data.getDoubleValueFromDB("SELECT INV_CRITICAL_LIMIT_AMT FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "' AND FLAG=1");
                                //rsCalc.getFloat("INV_CRITICAL_LIMIT_AMT");

                                if ((invCriticalAmt - invAmt) < 0) {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET INV_CRITICAL_LIMIT_AMT='" + (invCriticalAmt - invAmt) + "',INDICATOR=1,FLAG=0,CHECK_POINT_REMARK=CASE WHEN CHARGE_CODE='09' THEN 'ADV AMT IS LESS THEN INV AMT' ELSE CASE WHEN CHARGE_CODE='07' THEN 'LC AMT IS LESS THEN INV AMT' ELSE 'CREDIT AMT IS LESS THEN INV AMT' END END WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "'");
                                } else {

//                                    if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE=CURDATE() AND PARTY_CODE='" + partyCd + "'")) {
//                                        data.Execute("UPDATE PRODUCTION.FELT_INV_PROCESS_VAR_GST SET CRITICAL_LIMIT_NEW='" + (invCriticalAmt - invAmt) + "' WHERE PROCESSING_DATE=CURDATE() AND PARTY_CODE='" + partyCd + "'");
//                                    }
                                    if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>=CURDATE() AND BALE_NO LIKE ('%" + baleNo + "%')")) {
                                        data.Execute("UPDATE PRODUCTION.FELT_INV_PROCESS_VAR_GST SET CRITICAL_LIMIT_NEW='" + (invCriticalAmt - invAmt) + "' WHERE PROCESSING_DATE>=CURDATE() AND BALE_NO LIKE ('%" + baleNo + "%')");
                                    }
                                    //upCalc = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INV_CRITICAL_LIMIT_AMT='"+(invCriticalAmt-invAmt)+"' WHERE PARTY_CODE='"+partyCd+"' AND INDICATOR=0";
                                    upCalc = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET INV_CRITICAL_LIMIT_AMT='" + (invCriticalAmt - invAmt) + "' WHERE PARTY_CODE='" + partyCd + "' AND INDICATOR=0 AND CHECK_POINT_REMARK='' ";
                                    data.Execute(upCalc);
                                    //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INDICATOR=1 WHERE PARTY_CODE='"+partyCd+"' AND PIECE_NO='"+pieceNo+"'");
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET INDICATOR=1 WHERE PARTY_CODE='" + partyCd + "' AND BALE_NO='" + baleNo + "'");
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET CRITICAL_LIMIT_AMT='" + (invCriticalAmt - invAmt) + "' WHERE INDICATOR=0 AND FLAG=1 AND PARTY_CODE='" + partyCd + "' AND BALE_NO!='" + baleNo + "'");

                                }

                                rsCalc.next();
                            }
                        }
                        System.out.println("END : Invoice Amount Calculation till it's Limit");

                        //---------------------------------------------------
                        //CHECK INVOICE CRITICAL LIMIT AMOUNT of INVOICING PROCESS and UPDATE FLAG WHERE LIMIT BELOW 0
                        System.out.println("START : Check Critical Limit of Invoicing");
                        //data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET FLAG=0 WHERE INV_CRITICAL_LIMIT_AMT<0");
                        data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET FLAG=0,CHECK_POINT_REMARK=CASE WHEN CHARGE_CODE='09' THEN 'ADV AMT IS LESS THEN INV AMT' ELSE CASE WHEN CHARGE_CODE='07' THEN 'LC AMT IS LESS THEN INV AMT' ELSE 'CREDIT AMT IS LESS THEN INV AMT' END END WHERE INV_CRITICAL_LIMIT_AMT<0 AND CHECK_POINT_REMARK=''");
                        System.out.println("END : Check Critical Limit of Invoicing");

                        //---------------------------------------------------
                        //INVOICE NO UPDATION in Header & Detail
                        System.out.println("START : Invoice No Updation");
                        String invNo = "";
//                        String invNo = null;
//                        String gpNo = null;
//                        String tinvNo = null;
//                        String rinvNo = null;
//                        
                        ResultSet rsInvNo = null;
                        String upInvNo;
                        String invDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE FLAG=1 AND INDICATOR=1 AND CHECK_POINT_REMARK='' ORDER BY PARTY_CODE,BALE_NO";
                        rsInvNo = data.getResult(invDetail);
                        rsInvNo.first();
                        if (rsInvNo.getRow() > 0) {
                            while (!rsInvNo.isAfterLast()) {

                                Counter++;

                                String baleNo = rsInvNo.getString("BALE_NO");
                                String baleDate = rsInvNo.getString("PACKING_DATE");
                                String partyCd = rsInvNo.getString("PARTY_CODE");

                                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '" + EITLERPGLOBAL.gUserID + "', '" + gUserName + "', '" + baleNo + "', '" + baleDate + "', '" + partyCd + "', '', '', '0', 'START : INVOICE PROCESS', '' ) ");

                                if (data.IsRecordExist("SELECT * FROM D_FIN_PARTY_MASTER WHERE PARTY_CODE='" + partyCd + "' AND MAIN_ACCOUNT_CODE='210010' AND APPROVED=1 AND CANCELLED=0 ", FinanceGlobal.FinURL)) {

                                    String sql = "SELECT LS_NO - LAST_USED_NO  FROM\n"
                                            + "\n"
                                            + "(SELECT * FROM\n"
                                            + "\n"
                                            + "(SELECT MAX(RIGHT(INVOICE_NO,6)) AS LS_NO\n"
                                            + "\n"
                                            + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H\n"
                                            + "\n"
                                            + "WHERE INVOICE_DATE >='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE <='" + EITLERPGLOBAL.FinToDateDB + "'\n"
                                            + "\n"
                                            + "AND INVOICE_DATE >= DATE_SUB(NOW(), INTERVAL 30 DAY)) AS H,\n"
                                            + "\n"
                                            + "(SELECT LAST_USED_NO FROM DINESHMILLS.D_COM_FIRSTFREE WHERE MODULE_ID = 0 AND FIRSTFREE_NO = 208) AS M) AS M";

                                    int intSQL = data.getIntValueFromDB(sql);
//                                    System.out.println("*************LS_NO - LAST_USED_NO****************** : " + intSQL);

                                    if (intSQL != 0) {
//                                        System.out.println("From DATE : " + EITLERPGLOBAL.FinFromDateDB);
                                        String lastInvNo = data.getStringValueFromDB("SELECT MAX(RIGHT(INVOICE_NO,6)) AS LS_NO\n"
                                                + "\n"
                                                + "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER H\n"
                                                + "\n"
                                                + "WHERE INVOICE_DATE >='" + EITLERPGLOBAL.FinFromDateDB + "' AND INVOICE_DATE <='" + EITLERPGLOBAL.FinToDateDB + "'\n"
                                                + "\n"
                                                + "AND INVOICE_DATE >= DATE_SUB(NOW(), INTERVAL 30 DAY)");
//                                        String lsNo = String.format("%06d",lastInvNo);
//                                        System.out.println("LAST NO : " + lastInvNo);
                                        data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '" + EITLERPGLOBAL.gUserID + "', '" + gUserName + "', '" + baleNo + "', '" + baleDate + "', '" + partyCd + "', '" + invNo + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '0', 'FIRSTFREE UPDATED WITH " + lastInvNo + " ', '' ) ");
                                        data.Execute("UPDATE DINESHMILLS.D_COM_FIRSTFREE SET LAST_USED_NO='" + lastInvNo + "' WHERE MODULE_ID = 0 AND FIRSTFREE_NO = 208 ");
                                    }

                                    int intSQLCheck = data.getIntValueFromDB(sql);

                                    if (intSQLCheck == 0) {

                                        invNo = getNextFreeNo(2, 0, 208, false);

                                        upInvNo = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET INVOICE_NO='" + invNo + "',INVOICE_DATE='" + data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL") + "',INVOICE_PARTY='" + partyCd + "' WHERE BALE_NO = '" + baleNo + "' AND PARTY_CODE='" + partyCd + "' ";
                                        data.Execute(upInvNo);

                                        data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INVOICE_NO='" + invNo + "',INVOICE_DATE='" + data.getStringValueFromDB("SELECT CURRENT_TIMESTAMP AS DATE FROM DUAL") + "',INVOICE_PARTY='" + partyCd + "' WHERE BALE_NO = '" + baleNo + "' AND PARTY_CODE='" + partyCd + "' ");

                                        try {
                                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '" + EITLERPGLOBAL.gUserID + "', '" + gUserName + "', '" + baleNo + "', '" + baleDate + "', '" + partyCd + "', '" + invNo + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '1', 'START : INSERTION INVOICE FOR " + invNo + " ', '' ) ");

                                            ImportInvoicesFelt(true, partyCd, baleNo);
                                            invNo = getNextFreeNo(2, 0, 208, true);

                                            String upPkgFlg = "UPDATE PRODUCTION.FELT_PKG_SLIP_HEADER SET INVOICE_FLG=1 WHERE PKG_BALE_NO = '" + baleNo + "' AND PKG_BALE_DATE = '" + baleDate + "' AND PKG_PARTY_CODE='" + partyCd + "' ";
                                            data.Execute(upPkgFlg);

                                        } catch (Exception e) {
                                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '" + EITLERPGLOBAL.gUserID + "', '" + gUserName + "', '" + baleNo + "', '" + baleDate + "', '" + partyCd + "', '" + invNo + "', '" + EITLERPGLOBAL.getCurrentDateDB() + "', '1', 'ERROR : INSERTION INVOICE FOR " + invNo + " ', '' ) ");
                                            data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET INVOICE_NO='',INVOICE_DATE='0000-00-00 00:00:00' WHERE BALE_NO = '" + baleNo + "' AND PARTY_CODE='" + partyCd + "' ");
                                            data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET INVOICE_NO='',INVOICE_DATE='0000-00-00 00:00:00',CHECK_POINT_REMARK='PROCESS INTERRUPTED' WHERE BALE_NO = '" + baleNo + "' AND PARTY_CODE='" + partyCd + "' ");
                                            JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                                            e.printStackTrace();
                                            return;
                                        }

                                    }
//                                    String InvReportData = "INSERT INTO PRODUCTION.FELT_SALES_INV_PROCESS_REPORT_DATA SELECT *,CURRENT_TIMESTAMP() AS PROCESSING_TIME FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE BALE_NO = '" + baleNo + "' AND PARTY_CODE='" + partyCd + "'";
//                                    data.Execute(InvReportData);

                                } else {
                                    data.Execute("UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER SET FLAG=0,CHECK_POINT_REMARK='PARTY MISSING IN FINANCE MASTER' WHERE BALE_NO = '" + baleNo + "' AND PARTY_CODE='" + partyCd + "'");
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '" + EITLERPGLOBAL.gUserID + "', '" + gUserName + "', '" + baleNo + "', '" + baleDate + "', '" + partyCd + "', '', '', '1', 'PARTY MISSING IN FIANACE PARTY MASTER', '' ) ");
                                }
                                rsInvNo.next();
                            }
                        }
                        System.out.println("END : Invoice No Updation");

//                        //---------------------------------------------------
                        //INSERT PROCESSED INVOICE LIST INTO REPORT DATA
                        System.out.println("START : Insert Report Data");
                        String InvReportData = "INSERT INTO PRODUCTION.FELT_SALES_INV_PROCESS_REPORT_DATA SELECT *,CURRENT_TIMESTAMP() AS PROCESSING_TIME FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER";
                        data.Execute(InvReportData);
                        System.out.println("END : Insert Report Data");

                        //---------------------------------------------------
                        //INSERT COMPLETE PROCESSED INVOICE LIST INTO COMPLETED TABLES (HEADER & DETAIL)
                        System.out.println("START : Insert Completed Invoices");
                        String compInvHeader = "INSERT INTO PRODUCTION.FELT_SAL_COMPLETED_INV_HEADER_LIST SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE FLAG=1 AND CHECK_POINT_REMARK=''";
                        data.Execute(compInvHeader);
                        String compInvDetail = "INSERT INTO PRODUCTION.FELT_SAL_COMPLETED_INV_DETAIL_LIST SELECT D.* FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS D,TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER H WHERE H.BALE_NO=D.BALE_NO AND H.FLAG=1 AND H.CHECK_POINT_REMARK=''";
                        data.Execute(compInvDetail);
                        System.out.println("END : Insert Completed Invoices");

                        //---------------------------------------------------
                        //INSERT REJECTED INVOICE LIST INTO REJECTED TABLES (HEADER & DETAIL)
                        System.out.println("START : Insert Rejected Invoices");
                        String rejInvHeader = "INSERT INTO PRODUCTION.FELT_SAL_REJECTED_INV_HEADER_LIST SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE FLAG=0 AND CHECK_POINT_REMARK!=''";
                        data.Execute(rejInvHeader);
                        String rejInvDetail = "INSERT INTO PRODUCTION.FELT_SAL_REJECTED_INV_DETAIL_LIST SELECT D.* FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS D,TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER H WHERE H.BALE_NO=D.BALE_NO AND H.FLAG=0 AND H.CHECK_POINT_REMARK!=''";
                        data.Execute(rejInvDetail);
                        System.out.println("END : Insert Rejected Invoices");

                        //---------------------------------------------------
                        //InvValReport();
                        System.out.println("----------------------------------------------------------");
                        System.out.println("END OF PROCESS");

                        data.Execute("TRUNCATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS");
                        data.Execute("TRUNCATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER");                

            }           
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdatePieceDetail() {

        //PIECE DETAIL UPDATION FOR PIECE FROM PIECE REGISTER 
        ResultSet rsPieceDetail = null, rsUpdatePiece = null;

        try {
            String PieceDetail = "SELECT PIECE_NO FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS";
            rsPieceDetail = data.getResult(PieceDetail);
            rsPieceDetail.first();
            if (rsPieceDetail.getRow() > 0) {
                while (!rsPieceDetail.isAfterLast()) {

                    String pieceDetail = "SELECT * FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + rsPieceDetail.getString("PIECE_NO") + "'";
                    rsUpdatePiece = data.getResult(pieceDetail);

                    String pieceNo = rsPieceDetail.getString("PIECE_NO");
                    String mcnNo = rsUpdatePiece.getString("PR_MACHINE_NO");
                    String pstnNo = rsUpdatePiece.getString("PR_POSITION_NO");
                    //String prodCd = rsUpdatePiece.getString("PR_PRODUCT_CODE");
                    String prodCd = rsUpdatePiece.getString("PR_BILL_PRODUCT_CODE");
                    String grp = rsUpdatePiece.getString("PR_GROUP");
                    String style = rsUpdatePiece.getString("PR_BILL_STYLE");
                    float length = rsUpdatePiece.getFloat("PR_LENGTH");
                    float width = rsUpdatePiece.getFloat("PR_WIDTH");
                    //float gsm = rsUpdatePiece.getFloat("PR_GSM");
                    float gsm = rsUpdatePiece.getFloat("PR_BILL_GSM");
                    float t_weight = rsUpdatePiece.getFloat("PR_THORITICAL_WEIGHT");
                    //float sqmtr = rsUpdatePiece.getFloat("PR_SQMTR");
                    float sqmtr = rsUpdatePiece.getFloat("PR_BILL_SQMTR");

                    String synper = rsUpdatePiece.getString("PR_SYN_PER");
//                    float a_weight = rsUpdatePiece.getFloat("PR_ACTUAL_WEIGHT");
//                    float a_length = rsUpdatePiece.getFloat("PR_ACTUAL_LENGTH");
//                    float a_width = rsUpdatePiece.getFloat("PR_ACTUAL_WIDTH");
                    float a_weight = rsUpdatePiece.getFloat("PR_BILL_WEIGHT");
                    float a_length = rsUpdatePiece.getFloat("PR_BILL_LENGTH");
                    float a_width = rsUpdatePiece.getFloat("PR_BILL_WIDTH");

                    String upSQL = "UPDATE TEMP_DATABASE.TEMP_INV_VAL_PROCESS SET MACHINE_NO='" + mcnNo + "',POSITION_NO='" + pstnNo + "',PRODUCT_CODE='" + prodCd + "',GROUP_NAME='" + grp + "',STYLE='" + style + "',LENGTH=" + a_length + ",WIDTH=" + a_width + ",GSM=" + gsm + ",THORITICAL_WEIGHT=" + t_weight + ",SQMTR=" + sqmtr + ",SYN_PER='" + synper + "',ACTUAL_WEIGHT=" + a_weight + ",ACTUAL_LENGTH=" + a_length + ",ACTUAL_WIDTH=" + a_width + " WHERE PIECE_NO='" + pieceNo + "' ";
                    data.Execute(upSQL);

                    rsPieceDetail.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void AdvAmt() {
        String AsOnDate = EITLERPGLOBAL.getCurrentDateDB();
        String MainCode = "210010";
        int cnt = 0;

        data.Execute("TRUNCATE TEMP_DATABASE.TEMP_ADV_AMT");

        ResultSet rsPieceDetail = null;

        try {
            String PieceDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS";
            rsPieceDetail = data.getResult(PieceDetail);
            rsPieceDetail.first();
            if (rsPieceDetail.getRow() > 0) {
                while (!rsPieceDetail.isAfterLast()) {

                    data.Execute("INSERT INTO TEMP_DATABASE.TEMP_ADV_AMT (SUB_PARTY_CODE,SUB_ADV_BAL) SELECT B.SUB_ACCOUNT_CODE, SUM(B.AMOUNT) AS AMOUNT FROM FINANCE.D_FIN_VOUCHER_HEADER A, FINANCE.D_FIN_VOUCHER_DETAIL B "
                            + "WHERE A.VOUCHER_NO = B.VOUCHER_NO AND A.VOUCHER_DATE<='" + AsOnDate + "' AND A.VOUCHER_TYPE IN (6,7,8,9) AND B.MAIN_ACCOUNT_CODE='" + MainCode + "' AND B.SUB_ACCOUNT_CODE = '" + rsPieceDetail.getString("PARTY_CODE") + "' "
                            + "AND A.APPROVED=1 AND A.CANCELLED=0 AND A.EXCLUDE_IN_ADJ =0 AND B.EFFECT='C' AND B.INVOICE_NO ='' AND B.MODULE_ID <>65 "
                            + "AND B.GRN_NO ='' AND (B.MATCHED=0 OR B.MATCHED IS NULL) "
                            + "GROUP BY B.SUB_ACCOUNT_CODE "
                            + "ORDER BY B.SUB_ACCOUNT_CODE");

                    rsPieceDetail.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //UPDATE TABLE INV_PROCESS
    }

    public void OutStandingAmt() {
        String AsOnDate = EITLERPGLOBAL.getCurrentDateDB();
        String MainCode = "210010";
        int cnt = 0;

        data.Execute("TRUNCATE TEMP_DATABASE.TEMP_OUTSTANDING_AMT");
        data.Execute("TRUNCATE TEMP_DATABASE.TEMP_OUTSTANDING_REPORT");

        ResultSet rsPieceDetail = null;

        try {
            String PieceDetail = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS";
            rsPieceDetail = data.getResult(PieceDetail);
            rsPieceDetail.first();
            if (rsPieceDetail.getRow() > 0) {
                while (!rsPieceDetail.isAfterLast()) {

                    if (!clsSales_Party.getChargeCode(EITLERPGLOBAL.gCompanyID, rsPieceDetail.getString("PARTY_CODE"), "210010").equals("09")) {
                        data.Execute("INSERT INTO TEMP_DATABASE.TEMP_OUTSTANDING_AMT (SUB_PARTY_CODE) VALUES ('" + rsPieceDetail.getString("PARTY_CODE") + "')");
                    }
                    rsPieceDetail.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        data.Execute("UPDATE TEMP_DATABASE.TEMP_OUTSTANDING_AMT SET MAIN_PARTY_CODE=CASE WHEN LENGTH(MAIN_PARTY_CODE)>0 THEN MAIN_PARTY_CODE ELSE SUB_PARTY_CODE END");
        data.Execute("INSERT INTO TEMP_DATABASE.TEMP_OUTSTANDING_REPORT (MAIN_PARTY_CODE) SELECT DISTINCT MAIN_PARTY_CODE FROM TEMP_DATABASE.TEMP_OUTSTANDING_AMT");

        createFile1();

    }
    
    private void createFile1() {
        try {
//            System.out.println("InvoiceType : " + InvoiceType);
            double MainBalance = 0, SubBalance = 0;
            String GroupMainParty = "", GroupSubParty = "", Record = "";
            String InvoiceNo = "", InvoiceDate = "", strMainBalance = "", strBalance = "", strDecimal = "", MainCode = "";
            ResultSet rsMainParty = null, rsSubParty = null;
            //String FileName = "/data/Balance_Transfer_Cobol/"+EITLERPGLOBAL.getCurrentDateDB().substring(8,10)+
            //EITLERPGLOBAL.getCurrentDateDB().substring(5,7)+EITLERPGLOBAL.getCurrentDateDB().substring(2,4);
//            String FileName = "/data/Balance_Transfer_Cobol/ost";
            HashMap List = new HashMap();

            int InvoiceType = 2;
            MainCode = "210010";

            System.out.println("URL : " + FinanceGlobal.FinURL);

            data.Execute("TRUNCATE TEMP_DATABASE.TEMP_BAL_TR");

            rsMainParty = data.getResult("SELECT MAIN_PARTY_CODE FROM TEMP_DATABASE.TEMP_OUTSTANDING_REPORT ORDER BY MAIN_PARTY_CODE");
            rsMainParty.first();
            if (rsMainParty.getRow() > 0) {
                while (!rsMainParty.isAfterLast()) {
                    Record = "";
                    MainBalance = 0;
                    SubBalance = 0;
                    GroupMainParty = rsMainParty.getString("MAIN_PARTY_CODE");
                    
                    if (MainBalance == 0) {
                        MainBalance = EITLERPGLOBAL.round(BalanceTransfer1(MainCode, GroupMainParty), 2);
                        data.Execute("UPDATE TEMP_DATABASE.TEMP_OUTSTANDING_AMT SET SUB_OUTSTANDING_BAL=" + MainBalance + " WHERE MAIN_PARTY_CODE='" + GroupMainParty + "' ");
                    }

                    data.Execute("UPDATE TEMP_DATABASE.TEMP_OUTSTANDING_REPORT SET SUB_OUTSTANDING_BAL=" + MainBalance + " WHERE MAIN_PARTY_CODE='" + GroupMainParty + "'");

                    rsMainParty.next();
                }
            }
            System.out.println("File Created sucessfully...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double BalanceTransfer1(String MainCode, String PartyCode) {
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

            data.Execute("INSERT INTO TEMP_DATABASE.TEMP_BAL_TR (MAIN_ACCOUNT_CODE,SUB_ACCOUNT_CODE,BOOK_CODE,VOUCHER_NO,VOUCHER_DATE,LEGACY_NO,INVOICE_NO,INVOICE_DATE,LINK_NO,AMOUNT,EFFECT) " + SQL);

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

                    if (InvoiceType == 2 && clsVoucher.getVoucherType(VoucherNo) == FinanceGlobal.TYPE_SALES_JOURNAL) {
                        if (!clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("2") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("8") && !clsFeltSalesInvoice.getInvoiceChargeCode(InvoiceNo, InvoiceDate).startsWith("4")) {
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
   
    public static String getNextFreeNo(int pCompanyID, int pModuleID, int pFirstFreeNo, boolean UpdateLastNo) {
        Connection tmpConn;
        Statement tmpStmt;
        ResultSet rsTmp;
        String strSQL = "";
        String strNewNo = "";
        int lnNewNo = 0;
        String Prefix = "";
        String Suffix = "";

        try {
            tmpConn = data.getConn();
            tmpStmt = tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //strSQL="SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH FROM D_COM_FIRSTFREE WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'";
            strSQL = "SELECT LAST_USED_NO,PADDING_BY,NO_LENGTH,PREFIX_CHARS,SUFFIX_CHARS FROM DINESHMILLS.D_COM_FIRSTFREE WHERE COMPANY_ID=" + pCompanyID + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFreeNo;
            rsTmp = tmpStmt.executeQuery(strSQL);

            rsTmp.first();
            if (rsTmp.getRow() > 0) {
                lnNewNo = rsTmp.getInt("LAST_USED_NO") + 1;
                strNewNo = EITLERPGLOBAL.Padding(Integer.toString(lnNewNo), rsTmp.getInt("NO_LENGTH"), rsTmp.getString("PADDING_BY"));
                Prefix = rsTmp.getString("PREFIX_CHARS");
                Suffix = rsTmp.getString("SUFFIX_CHARS");

                if (UpdateLastNo) {
                    //Update last no. in database
                    //data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='"+strNewNo.trim()+"',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID="+pCompanyID+" AND MODULE_ID="+pModuleID+" AND PREFIX_CHARS='"+pPrefix+"' AND SUFFIX_CHARS='"+pSuffix+"'");
                    data.Execute("UPDATE D_COM_FIRSTFREE SET LAST_USED_NO='" + strNewNo.trim() + "',CHANGED=1,CHANGED_DATE=CURDATE() WHERE COMPANY_ID=" + pCompanyID + " AND MODULE_ID=" + pModuleID + " AND FIRSTFREE_NO=" + pFirstFreeNo);
                }

                strNewNo = Prefix + strNewNo + Suffix;

                //tmpConn.close();
                tmpStmt.close();
                rsTmp.close();

                return strNewNo;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }
    
    public static void ImportInvoicesFelt(boolean pPostSJ, String pPartyCode, String pBaleNo) {

        boolean Done = false;
        boolean canPostSJ = false;
        boolean AutoAdj = false;
        long Counter = 0;

        try {

            clsFeltSalesInvoice objInvoice = new clsFeltSalesInvoice();

            String dbURL = EITLERPGLOBAL.DatabaseURL;

            Connection objConn = data.getConn(dbURL);
            Statement stInvoice = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoice = stInvoice.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER LIMIT 1");

            // ---- History Connection ------ //
            Statement stInvoiceHistory = objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rsInvoiceHistory = stInvoiceHistory.executeQuery("SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER_H LIMIT 1"); // '1' for restricting all data retrieval

            ResultSet rsInvHeader = null;
            //String invHeader = "SELECT * FROM PRODUCTION.FELT_SAL_COMPLETED_INV_HEADER_LIST WHERE INVOICE_DATE = '" + CurrentDate + "'";
            String invHeader = "SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS_HEADER WHERE FLAG=1 AND CHECK_POINT_REMARK='' AND BALE_NO = '" + pBaleNo + "' AND PARTY_CODE = '" + pPartyCode + "' ";//INVOICE_DATE = '" + EITLERPGLOBAL.getCurrentDateDB() + "' AND 
            rsInvHeader = data.getResult(invHeader);
            rsInvHeader.first();
            if (rsInvHeader.getRow() > 0) {
                while (!rsInvHeader.isAfterLast()) {

                    try {

                        String gstrPos = "";

                        String InvoiceNo = "FE/" + rsInvHeader.getString("INVOICE_NO");

                        // Added by mrugesh for auto adjustment start
                        String InvNo = InvoiceNo;
                        String InvDate = rsInvHeader.getString("INVOICE_DATE").substring(0, 10);
                        String ChargeCode = rsInvHeader.getString("CHARGE_CODE");
                        String Party_code = rsInvHeader.getString("PARTY_CODE");;
                        String Filler = "";
                        double InvoiceAmount = Math.round(rsInvHeader.getFloat("INVOICE_AMT"));
                        canPostSJ = false;
                        AutoAdj = false;
                        String pBaleDate = rsInvHeader.getString("PACKING_DATE");;
                        // Added by mrugesh for auto adjustment end

                        String InvoiceDate = rsInvHeader.getString("INVOICE_DATE").substring(0, 10);
                        System.out.println("invdt : " + InvoiceDate);
                        
                        // COMMENTED ON 17/05/2011 BY MRUGESH AS CANCELLATION WILL BE TAKEN CARE BY USER 
                        String str = "SELECT * FROM PRODUCTION.FELT_SAL_INVOICE_HEADER "
                                + " WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "' ";

                        if (!data.IsRecordExist(str, dbURL)) {

                            //Party_code= FileRecord.substring(Pointer,Pointer+6);
                            System.out.println("Importing Invoice " + InvoiceNo);
                            rsInvoice.moveToInsertRow();
                            rsInvoice.updateInt("COMPANY_ID", 2);
                            rsInvoice.updateInt("INVOICE_TYPE", 2);
                            rsInvoice.updateString("INVOICE_NO", InvoiceNo);
                            rsInvoice.updateString("INVOICE_DATE", InvoiceDate);
                            rsInvoice.updateString("BALE_NO", rsInvHeader.getString("BALE_NO"));
                            rsInvoice.updateString("PARTY_CODE", Party_code);
                            rsInvoice.updateString("NO_OF_PIECES", rsInvHeader.getString("NO_OF_PIECES"));
                            rsInvoice.updateString("CHARGE_CODE", rsInvHeader.getString("CHARGE_CODE"));
                            rsInvoice.updateFloat("BAS_AMT", rsInvHeader.getFloat("BAS_AMT"));
                            rsInvoice.updateFloat("DISC_AMT", rsInvHeader.getFloat("DISC_AMT"));
                            rsInvoice.updateFloat("DISC_BAS_AMT", rsInvHeader.getFloat("DISC_BAS_AMT"));
                            rsInvoice.updateFloat("CHEM_TRT_CHG", rsInvHeader.getFloat("CHEM_TRT_CHG"));
                            rsInvoice.updateFloat("PIN_CHG", rsInvHeader.getFloat("PIN_CHG"));
                            rsInvoice.updateFloat("SPIRAL_CHG", rsInvHeader.getFloat("SPIRAL_CHG"));
                            rsInvoice.updateFloat("SEAM_CHG", rsInvHeader.getFloat("SEAM_CHG"));
                            //rsInvoice.updateFloat("GROSS_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("PIN_CHG") + rsInvHeader.getFloat("SPIRAL_CHG")));
                            //rsInvoice.updateFloat("GROSS_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG")));
                            rsInvoice.updateFloat("GROSS_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") - rsInvHeader.getFloat("AOSD_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG")));
                            rsInvoice.updateFloat("EXCISE", rsInvHeader.getFloat("EXCISE"));
                            rsInvoice.updateFloat("INSURANCE_AMT", rsInvHeader.getFloat("INSURANCE_AMT"));
                            rsInvoice.updateFloat("BANK_CHARGES", Float.valueOf(0));
                            rsInvoice.updateFloat("CESS", Float.valueOf(0));
                            rsInvoice.updateFloat("GST_AMT", Float.valueOf(0));
                            rsInvoice.updateFloat("CST2", rsInvHeader.getFloat("CST2"));
                            rsInvoice.updateFloat("CST5", rsInvHeader.getFloat("CST5"));
                            rsInvoice.updateFloat("VAT1", rsInvHeader.getFloat("VAT1"));
                            rsInvoice.updateFloat("VAT4", rsInvHeader.getFloat("VAT4"));
                            rsInvoice.updateFloat("SD_AMT", rsInvHeader.getFloat("SD_AMT"));
                            rsInvoice.updateFloat("NET_AMT", rsInvHeader.getFloat("INVOICE_AMT"));
                            rsInvoice.updateFloat("INVOICE_AMT", Math.round(rsInvHeader.getFloat("INVOICE_AMT")));

                            rsInvoice.updateFloat("AOSD_PER", rsInvHeader.getFloat("AOSD_PER"));
                            rsInvoice.updateFloat("AOSD_AMT", rsInvHeader.getFloat("AOSD_AMT"));

                            NumWord num = new NumWord();
                            String rsInWord = num.convertNumToWord(Math.round(rsInvHeader.getFloat("INVOICE_AMT")));
                            rsInvoice.updateString("INVOICE_AMT_IN_WORD", rsInWord);

                            rsInvoice.updateInt("LOT_NO", rsInvHeader.getInt("LOT_NO"));
                            rsInvoice.updateInt("TRANSPORTER_CODE", rsInvHeader.getInt("TRANSPORTER_CODE"));
                            rsInvoice.updateString("PACKING_DATE", rsInvHeader.getString("PACKING_DATE"));
                            rsInvoice.updateString("GATEPASS_NO", rsInvHeader.getString("GATEPASS_NO"));
                            rsInvoice.updateString("TAX_INV_NO", rsInvHeader.getString("TAX_INV_NO"));
                            rsInvoice.updateString("RETAIL_INV_NO", rsInvHeader.getString("RETAIL_INV_NO"));
                            rsInvoice.updateString("FINYR", EITLERPGLOBAL.FinYearFrom + "-" + EITLERPGLOBAL.FinYearTo);

                            rsInvoice.updateDouble("INV_CRITICAL_LIMIT_AMT", rsInvHeader.getDouble("INV_CRITICAL_LIMIT_AMT"));
                            rsInvoice.updateDouble("CRITICAL_LIMIT_AMT", rsInvHeader.getDouble("CRITICAL_LIMIT_AMT"));

                            //rsInvoice.updateBoolean("APPROVED",true);
                            rsInvoice.updateString("HSN_CODE", "5911");
                            rsInvoice.updateFloat("IGST_PER", rsInvHeader.getFloat("IGST_PER"));
                            rsInvoice.updateFloat("IGST_AMT", rsInvHeader.getFloat("IGST_AMT"));
                            rsInvoice.updateFloat("CGST_PER", rsInvHeader.getFloat("CGST_PER"));
                            rsInvoice.updateFloat("CGST_AMT", rsInvHeader.getFloat("CGST_AMT"));
                            rsInvoice.updateFloat("SGST_PER", rsInvHeader.getFloat("SGST_PER"));
                            rsInvoice.updateFloat("SGST_AMT", rsInvHeader.getFloat("SGST_AMT"));
                            rsInvoice.updateFloat("GST_COMP_CESS_PER", rsInvHeader.getFloat("GST_COMP_CESS_PER"));
                            rsInvoice.updateFloat("GST_COMP_CESS_AMT", rsInvHeader.getFloat("GST_COMP_CESS_AMT"));
                            rsInvoice.updateString("ADV_DOC_NO", rsInvHeader.getString("ADV_DOC_NO"));
                            rsInvoice.updateString("VEHICLE_NO", rsInvHeader.getString("VEHICLE_NO"));
                            rsInvoice.updateFloat("ADV_RECEIVED_AMT", rsInvHeader.getFloat("ADV_RECEIVED_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_INV_AMT", rsInvHeader.getFloat("ADV_AGN_INV_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_IGST_AMT", rsInvHeader.getFloat("ADV_AGN_IGST_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_SGST_AMT", rsInvHeader.getFloat("ADV_AGN_SGST_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_CGST_AMT", rsInvHeader.getFloat("ADV_AGN_CGST_AMT"));
                            rsInvoice.updateFloat("ADV_AGN_GST_COMP_CESS_AMT", rsInvHeader.getFloat("ADV_AGN_GST_COMP_CESS_AMT"));

//                                int id = 1746;
                            int id = 1677;
                            rsInvoice.updateInt("HIERARCHY_ID", id);

                            rsInvoice.updateBoolean("APPROVED", false);
//                                rsInvoice.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());
                            rsInvoice.updateString("APPROVED_DATE", "0000-00-00");
                            rsInvoice.updateBoolean("CANCELLED", false);

                            rsInvoice.updateString("CREATED_BY", String.valueOf(EITLERPGLOBAL.gUserID));
                            rsInvoice.updateString("CREATED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                            rsInvoice.updateBoolean("CHANGED", true);
                            rsInvoice.updateString("CHANGED_DATE", EITLERPGLOBAL.getCurrentDateDB());

                            String DueDate = "0000-00-00";
                            if (ChargeCode.startsWith("02")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsFeltSalesInvoice.getCreditDays(Party_code, "210010") + 15, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("08")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsFeltSalesInvoice.getCreditDays(Party_code, "210010") + 6, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("01")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 30, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("04")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, clsFeltSalesInvoice.getCreditDays(Party_code, "210010"), "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("07")) {
                                DueDate = EITLERPGLOBAL.addDaysToDate(InvDate, 45, "yyyy-MM-dd");
                            } else if (ChargeCode.startsWith("09")) {
                                DueDate = InvDate;
                            }

                            rsInvoice.updateString("DUE_DATE", DueDate);

                            rsInvoice.updateString("DOCUMENT_THROUGH", rsInvHeader.getString("DOCUMENT_THROUGH"));
                            rsInvoice.updateString("PRODUCT_CODE", rsInvHeader.getString("PRODUCT_CODE"));
                            rsInvoice.updateString("PRODUCT_DESC", rsInvHeader.getString("PRODUCT_DESC"));
                            rsInvoice.updateString("PIECE_NO", rsInvHeader.getString("PIECE_NO"));
                            rsInvoice.updateString("MACHINE_NO", rsInvHeader.getString("MACHINE_NO"));
                            rsInvoice.updateString("POSITION_NO", rsInvHeader.getString("POSITION_NO"));
                            rsInvoice.updateString("POSITION_DESC", rsInvHeader.getString("POSITION_DESC"));
                            rsInvoice.updateString("STYLE", rsInvHeader.getString("STYLE"));
                            rsInvoice.updateFloat("LENGTH", rsInvHeader.getFloat("LENGTH"));
                            rsInvoice.updateFloat("WIDTH", rsInvHeader.getFloat("WIDTH"));
                            rsInvoice.updateFloat("GSM", rsInvHeader.getFloat("GSM"));
                            rsInvoice.updateFloat("ACTUAL_WEIGHT", rsInvHeader.getFloat("ACTUAL_WEIGHT"));
                            rsInvoice.updateFloat("SQMTR", rsInvHeader.getFloat("SQMTR"));
                            rsInvoice.updateString("SYN_PER", rsInvHeader.getString("SYN_PER"));
                            rsInvoice.updateDouble("RATE", rsInvHeader.getDouble("RATE"));
                            rsInvoice.updateString("RATE_UNIT", rsInvHeader.getString("RATE_UNIT"));
                            rsInvoice.updateString("TRANSPORTER_NAME", rsInvHeader.getString("TRANSPORTER_NAME"));
                            rsInvoice.updateString("DESP_MODE", rsInvHeader.getString("DESP_MODE"));
                            rsInvoice.updateString("PARTY_NAME", rsInvHeader.getString("PARTY_NAME"));
                            rsInvoice.updateString("GSTIN_NO", rsInvHeader.getString("GSTIN_NO"));
                            rsInvoice.updateString("ADDRESS1", rsInvHeader.getString("ADDRESS1"));
                            rsInvoice.updateString("ADDRESS2", rsInvHeader.getString("ADDRESS2"));
                            rsInvoice.updateString("CITY_NAME", rsInvHeader.getString("CITY_NAME"));
                            rsInvoice.updateString("CITY_ID", rsInvHeader.getString("CITY_ID"));
                            rsInvoice.updateString("DISPATCH_STATION", rsInvHeader.getString("DISPATCH_STATION"));
                            String statecode = rsInvHeader.getString("GSTIN_NO").substring(0, 2);
                            String pos = data.getStringValueFromDB("SELECT STATE_NAME FROM DINESHMILLS.D_SAL_STATE_MASTER WHERE STATE_GST_CODE='" + statecode + "'");
                            rsInvoice.updateString("PLACE_OF_SUPPLY", pos);
                            //rsInvoice.updateString("PLACE_OF_SUPPLY", rsInvHeader.getString("PLACE_OF_SUPPLY"));
                            rsInvoice.updateDouble("DISC_PER", rsInvHeader.getDouble("DISC_PER"));
                            rsInvoice.updateString("PINCODE", rsInvHeader.getString("PINCODE"));
                            rsInvoice.updateString("PARTY_CHARGE_CODE", rsInvHeader.getString("PARTY_CHARGE_CODE"));
                            rsInvoice.updateString("PARTY_BANK_NAME", rsInvHeader.getString("PARTY_BANK_NAME"));
                            rsInvoice.updateString("PARTY_BANK_ADDRESS1", rsInvHeader.getString("PARTY_BANK_ADDRESS1"));
                            rsInvoice.updateString("PARTY_BANK_ADDRESS2", rsInvHeader.getString("PARTY_BANK_ADDRESS2"));

                            rsInvoice.updateString("PO_NO", rsInvHeader.getString("PO_NO"));
                            rsInvoice.updateString("PO_DATE", rsInvHeader.getString("PO_DATE"));
                            rsInvoice.updateString("LC_NO", rsInvHeader.getString("LC_NO"));

                            rsInvoice.updateString("CREDIT_DAYS", rsInvHeader.getString("CREDIT_DAYS"));
                            rsInvoice.updateString("PAYMENT_TERMS", rsInvHeader.getString("PAYMENT_TERMS"));

                            String hNo = "";
                            int hundiNo = 0;
                            if (ChargeCode.startsWith("04")) {
                                hNo = clsFirstFree.getNextFreeNo(2, 0, 226, true);
                                hundiNo = Integer.parseInt(hNo);
                                rsInvoice.updateInt("HUNDI_NO", hundiNo);
                            }

                            int trLetNo = 0;
                            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "' AND TR_LET_DATE=CURDATE() AND LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "'")) {
                                trLetNo = data.getIntValueFromDB("SELECT TR_LET_NO FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "' AND TR_LET_DATE=CURDATE() AND LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "'");
                                rsInvoice.updateInt("TR_LET_NO", trLetNo);
                            } else {
                                trLetNo = data.getIntValueFromDB("SELECT TR_LET_NO FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "'");
                                rsInvoice.updateInt("TR_LET_NO", trLetNo + 1);
                            }

                            //rsInvoice.updateFloat("INVOICE_TAXABLE_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG") + rsInvHeader.getFloat("INSURANCE_AMT")));
                            rsInvoice.updateFloat("INVOICE_TAXABLE_AMT", Float.valueOf(rsInvHeader.getFloat("DISC_BAS_AMT") - rsInvHeader.getFloat("AOSD_AMT") + rsInvHeader.getFloat("CHEM_TRT_CHG") + rsInvHeader.getFloat("SEAM_CHG") + rsInvHeader.getFloat("INSURANCE_AMT")));

                            gstrPos = clsPlaceOfSupply.PlaceOfSupply(statecode);

                            rsInvoice.insertRow();
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '2', 'INSERT : INVOICE HEADER TABLE FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
                            //DETAILS UPDATION
                            String invDetailUp = "INSERT INTO PRODUCTION.FELT_SAL_INVOICE_DETAIL SELECT * FROM TEMP_DATABASE.TEMP_INV_VAL_PROCESS WHERE BALE_NO='" + rsInvHeader.getString("BALE_NO") + "' AND INVOICE_NO='" + rsInvHeader.getString("INVOICE_NO") + "'";
                            data.Execute(invDetailUp);
                            data.Execute("UPDATE PRODUCTION.FELT_SAL_INVOICE_DETAIL SET INVOICE_NO=CONCAT('FE/',INVOICE_NO) WHERE BALE_NO='" + rsInvHeader.getString("BALE_NO") + "' AND INVOICE_NO='" + rsInvHeader.getString("INVOICE_NO") + "'");
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '3', 'INSERT : INVOICE DETAIL TABLE FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                            String cSQL = "SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = '" + Party_code + "' AND H.INVOICE_NO = '" + InvNo + "' AND H.INVOICE_DATE = '" + InvDate + "' ";

                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '4', 'START : CHECK CHARGE CODE OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
                            if (data.IsRecordExist(cSQL)) {
                                
                                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5', 'CHECK : CHARGE CODE OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
//                                ExternalImportSJ(true, ChargeCode, InvoiceAmount, Party_code, InvNo, InvDate);
                                if (ChargeCode.startsWith("09")) {
                                    
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.1', 'CHECKED : CHARGE CODE O9 OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                                    double availableAmount = clsAccount.get09AmountByParty("210010", Party_code, InvDate);

                                    System.out.println("09 AMOUNT : " + availableAmount);

                                    if (InvoiceAmount > availableAmount) {
                                        
                                        data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.2', 'CHECKED : O9 BALANCE LESS THAN INVOICE AMOUNT OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                        canPostSJ = false;
                                        AutoAdj = false;
                                        data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
                                        data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
//                                        Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " of party " + Party_code + " with Invoice Amount is (" + InvoiceAmount + ") and advance amount is (" + availableAmount + ") \n"
//                                                + "before invoice date, so invoice not imported.";
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println("Invoice not Imported ");
                                    } else {
                                        data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.2', 'CHECKED : O9 BALANCE SUFFICIENT OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                        canPostSJ = true;
                                        AutoAdj = true;
//                                        Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " is imported.";
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println("Invoice Imported ");
                                    }
                                    // Remove the comment for posting 09 prathmesh 03-10-2010 end
                                } else {
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '5.1', 'CHECKED : CHARGE CODE NOT 09 OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                    canPostSJ = true;
                                    AutoAdj = false;
//                                    Msg = "Invoice No (" + InvNo + ") on date " + InvDate + " is imported.";
//                                    log.logToFile(LogFileName, Msg, 2);
                                    System.out.println("Invoice Imported ");
                                }

                                if (pPostSJ && canPostSJ) {
                                    System.out.println("Posting SJ");
                                    
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '6', 'START : SJ POSTING OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                                    objInvoice = (clsFeltSalesInvoice) objInvoice.getObject(2, InvNo, InvDate, dbURL);

                                    if (objInvoice.PostSJTypeFelt(2, InvNo, InvDate, AutoAdj, pBaleNo, pBaleDate)) {                                        
//                                        Msg = "SJ has been posted for Invoice No (" + InvNo + ") on date " + InvDate;
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println("Invoice " + InvNo + " Posted.");
                                    } else {
                                        data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '6', 'ERROR : SJ POSTING FAILED OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
//                                        Msg = "Invoice and SJ posting has been stoped due to ::" + objInvoice.LastError;
//                                        log.logToFile(LogFileName, Msg, 2);
                                        System.out.println(objInvoice.LastError);
                                        JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                                        return;
                                    }
                                }

                                String cSQL1 = "";
                                if (ChargeCode.equals("09")) {
                                    cSQL1 = "SELECT * FROM ( SELECT INV.INVOICE_NO,INV.INVOICE_DATE,INV.INVOICE_AMT,COALESCE(AMT,0) AS AMT,INVOICE_AMT-COALESCE(AMT,0) AS FAMT FROM (SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = '" + Party_code + "' AND H.INVOICE_NO = '" + InvNo + "' AND H.INVOICE_DATE = '" + InvDate + "' AND H.CHARGE_CODE =09) AS INV LEFT JOIN (SELECT D.INVOICE_NO,D.INVOICE_DATE,SUM(COALESCE(AMOUNT,0)) AS AMT FROM FINANCE.D_FIN_VOUCHER_DETAIL D ,FINANCE.D_FIN_VOUCHER_HEADER H WHERE H.VOUCHER_NO = D.VOUCHER_NO AND INVOICE_NO = '" + InvNo + "' AND INVOICE_DATE = '" + InvDate + "' AND EFFECT ='C' AND SUB_ACCOUNT_CODE = '" + Party_code + "' AND SUBSTRING(H.VOUCHER_NO,1,2) !='SJ' GROUP BY D.INVOICE_NO,D.INVOICE_DATE ) AS RC ON RC.INVOICE_NO= INV.INVOICE_NO AND RC.INVOICE_DATE = INV.INVOICE_DATE ) SUB WHERE FAMT=0 ";
                                } else {
                                    cSQL1 = "SELECT INV.INVOICE_NO,INV.INVOICE_DATE,INV.INVOICE_AMT,COALESCE(AMT,0) AS AMT,INVOICE_AMT-COALESCE(AMT,0) AS FAMT FROM (SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = '" + Party_code + "' AND H.INVOICE_NO = '" + InvNo + "' AND H.INVOICE_DATE = '" + InvDate + "' ) AS INV LEFT JOIN (SELECT D.INVOICE_NO,D.INVOICE_DATE,SUM(COALESCE(AMOUNT,0)) AS AMT FROM FINANCE.D_FIN_VOUCHER_DETAIL D ,FINANCE.D_FIN_VOUCHER_HEADER H WHERE H.VOUCHER_NO = D.VOUCHER_NO AND INVOICE_NO = '" + InvNo + "' AND INVOICE_DATE = '" + InvDate + "' AND EFFECT ='D' AND SUB_ACCOUNT_CODE = '" + Party_code + "' AND SUBSTRING(H.VOUCHER_NO,1,2) ='SJ' GROUP BY D.INVOICE_NO,D.INVOICE_DATE ) AS RC ON RC.INVOICE_NO= INV.INVOICE_NO AND RC.INVOICE_DATE = INV.INVOICE_DATE ";
                                }
                                System.out.println("check SQL 1 for SJ: " + cSQL1);
                                
                                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '7', 'CHECK : SJ AGAINEST INVOICE POSTING OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                
                                if (!data.IsRecordExist(cSQL1)) {
                                    
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '7.1', 'ERROR : SJ AGAINEST INVOICE POSTING MISMATCHED OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                    
                                    data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_HEADER WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");
                                    data.Execute("DELETE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL WHERE INVOICE_NO='" + InvNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvDate + "' ");

                                    String SJNo = data.getStringValueFromDB("SELECT DISTINCT VOUCHER_NO FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE SUB_ACCOUNT_CODE='" + Party_code + "' AND INVOICE_NO='" + InvNo + "' AND INVOICE_DATE='" + InvDate + "' AND SUBSTRING(VOUCHER_NO,1,2)='SJ'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='" + SJNo + "'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_DETAIL WHERE VOUCHER_NO='" + SJNo + "'");
                                    data.Execute("DELETE FROM FINANCE.D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='" + SJNo + "'");

                                    data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL SET INVOICE_NO='', INVOICE_DATE='0000-00-00', INVOICE_AMOUNT=0, GRN_NO='', GRN_DATE='0000-00-00', MODULE_ID=0, REF_COMPANY_ID=0 WHERE INVOICE_NO='" + InvNo + "' AND INVOICE_DATE='" + InvDate + "' AND SUBSTRING(VOUCHER_NO,1,2)!='SJ'");
                                    data.Execute("UPDATE FINANCE.D_FIN_VOUCHER_DETAIL_EX SET INVOICE_NO='', INVOICE_DATE='0000-00-00', INVOICE_AMOUNT=0, GRN_NO='', GRN_DATE='0000-00-00', MODULE_ID=0, REF_COMPANY_ID=0 WHERE INVOICE_NO='" + InvNo + "' AND INVOICE_DATE='" + InvDate + "' AND SUBSTRING(VOUCHER_NO,1,2)!='SJ'");
                                    
//                                    Msg = "Invoice data not matched with Finance data for Invoice No (" + InvNo + ") on date " + InvDate;
//                                    log.logToFile(LogFileName, Msg, 2);

                                    JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                                    return;
                                } else {
                                    data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '7.1', 'CHECKED : SJ AGAINEST INVOICE POSTING MATCHED OF PARTY "+pPartyCode+" FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                                }
                                
                            } else {
                                data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '4', 'ERROR : SJ POSTING FAILED FOR "+InvNo+" DATED "+InvDate+" DUE TO HEADER AND DETAIL DATA NOT FOUND ', 'SELECT H.INVOICE_NO,H.INVOICE_DATE,H.INVOICE_AMT,H.CHARGE_CODE FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL D ,PRODUCTION.FELT_SAL_INVOICE_HEADER H WHERE H.INVOICE_NO = D.INVOICE_NO AND H.INVOICE_DATE = SUBSTRING(D.INVOICE_DATE,1,10) AND H.PARTY_CODE = " + Party_code + " AND H.INVOICE_NO = " + InvNo + " AND H.INVOICE_DATE = " + InvDate + " ' ) ");
//                                Msg = "Felt Sales Header not matched with Detail for Invoice No (" + InvNo + ") on date " + InvDate;
//                                log.logToFile(LogFileName, Msg, 2);
                                JOptionPane.showMessageDialog(null, "Process interrupted. Please try again.");
                                return;
                            }

                            //INSERT HIERARCHY INTO DOC DATA
                            String DocUpDt = "INSERT INTO PRODUCTION.FELT_PROD_DOC_DATA SELECT 80,INVOICE_NO,CURDATE(),USER_ID,CASE WHEN CREATOR=1 THEN 'W' ELSE 'P'  END, CASE WHEN CREATOR =0 THEN 'A' ELSE 'C'  END,'',SR_NO,0,'',NOW(),'0000-00-00 00:00:00',0,NOW()  FROM DINESHMILLS.D_COM_HIERARCHY_RIGHTS A,PRODUCTION.FELT_SAL_INVOICE_HEADER B WHERE B.INVOICE_NO='" + InvoiceNo + "' AND B.INVOICE_DATE='"+InvoiceDate+"' AND B.HIERARCHY_ID =1677 AND A.HIERARCHY_ID = B.HIERARCHY_ID ";
                            data.Execute(DocUpDt);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '8', 'DONE : DOC DATA POSTED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");
                            
                            //UPDATION OF PIECE REGISTER
                            String pieceUpdate = "UPDATE PRODUCTION.FELT_SALES_PIECE_REGISTER P,PRODUCTION.FELT_SAL_INVOICE_DETAIL I SET PR_INVOICE_NO='" + InvoiceNo + "',PR_INVOICE_DATE='" + InvoiceDate + "',PR_INVOICE_PARTY='" + Party_code + "' WHERE P.PR_PIECE_NO=I.PIECE_NO AND I.INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(I.INVOICE_DATE,1,10)='" + InvoiceDate + "' ";
                            data.Execute(pieceUpdate);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '9', 'DONE : PIECE REGISTER UPDATED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                            String strSQL = "INSERT INTO DINESHMILLS.D_SAL_INVOICE_HEADER ";
                            strSQL += "(COMPANY_ID,INVOICE_TYPE,INVOICE_NO,INVOICE_DATE,PARTY_CODE,PIECE_NO,DUE_DATE,PAYMENT_TERM_CODE,BALE_NO,TOTAL_GROSS_AMOUNT,TOTAL_NET_AMOUNT, ";
                            strSQL += "NET_AMOUNT,GROSS_WEIGHT,TRANSPORTER_CODE,PARTY_NAME,LENGTH,WIDTH,NO_OF_PIECES,COLUMN_1_AMT,COLUMN_1_CAPTION,COLUMN_3_AMT,COLUMN_3_CAPTION, ";
                            strSQL += "COLUMN_6_AMT,COLUMN_8_PER,COLUMN_8_AMT,COLUMN_8_CAPTION,COLUMN_9_AMT,COLUMN_9_CAPTION,COLUMN_10_CAPTION,COLUMN_11_AMT,COLUMN_12_AMT,COLUMN_13_AMT, ";
                            strSQL += "COLUMN_14_CAPTION,COLUMN_24_AMT,COLUMN_25_AMT,APPROVED,CANCELLED,CHANGED,VAT1,SD_AMT,TOT_INV_SD_AMT,CHANGED_DATE,CGST_PER,CGST_AMT,SGST_PER, ";
                            strSQL += "SGST_AMT,IGST_PER,IGST_AMT) ";
                            //strSQL += "VALUES ";
                            strSQL += "(SELECT COMPANY_ID,INVOICE_TYPE,INVOICE_NO,INVOICE_DATE,PARTY_CODE,PIECE_NO,DUE_DATE,CONCAT(SUBSTRING(CHARGE_CODE,2,1),SUBSTRING(DESP_MODE,2,1)),BALE_NO,BAS_AMT,NET_AMT,INVOICE_AMT,ACTUAL_WEIGHT, ";
                            strSQL += "TRANSPORTER_CODE,PARTY_NAME,LENGTH,WIDTH,NO_OF_PIECES,SGST_AMT,'SGST_AMT',CGST_AMT+IGST_AMT,'IGST_CGST_AMT',0,DISC_PER,0,'DISC_PER',INSURANCE_AMT, ";
                            strSQL += "'INS CHRG','BANK CHRG',0,0,0,'GST AMOUNT',CHEM_TRT_CHG,SEAM_CHG,1,CANCELLED,CHANGED,0,0,0,CHANGED_DATE,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT,IGST_PER,IGST_AMT ";
                            strSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
                            strSQL += "WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "') ";

                            System.out.println("Dineshmills SQL : " + strSQL);
                            data.Execute(strSQL);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '10', 'DONE : DINESHMILLS INVOICE HEADER UPDATED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                            String strSQLdet = "INSERT INTO DINESHMILLS.D_SAL_INVOICE_DETAIL (COMPANY_ID, INVOICE_TYPE, INVOICE_NO, INVOICE_DATE, QUALITY_NO, PIECE_NO, PARTY_CODE, RATE, GROSS_SQ_MTR, GROSS_KG, GROSS_AMOUNT, TRD_DISCOUNT, ADDITIONAL_DUTY, NET_AMOUNT, HSN_CODE) ";
                            //strSQLdet += "VALUES ";
                            strSQLdet += "(SELECT 2,2,INVOICE_NO,SUBSTRING(INVOICE_DATE, 1, 10),PRODUCT_CODE,PIECE_NO,PARTY_CODE,RATE,SQMTR,ACTUAL_WEIGHT,BAS_AMT,DISC_AMT,AOSD_AMT,INVOICE_AMT,5911 ";
                            strSQLdet += "FROM PRODUCTION.FELT_SAL_INVOICE_DETAIL ";
                            strSQLdet += "WHERE INVOICE_NO='" + InvoiceNo + "' AND SUBSTRING(INVOICE_DATE,1,10)='" + InvoiceDate + "') ";

                            System.out.println("Dineshmills Detail SQL : " + strSQLdet);
                            data.Execute(strSQLdet);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '11', 'DONE : DINESHMILLS INVOICE DETAIL UPDATED FOR "+InvNo+" DATED "+InvDate+" ', '' ) ");

                            if (!data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "' AND TR_LET_DATE=CURDATE() AND LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "'")) {
                                int trNo = data.getIntValueFromDB("SELECT TR_LET_NO FROM DINESHMILLS.D_SAL_TRANSPORTER_MASTER WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "'");
                                trNo = trNo + 1;
                                data.Execute("UPDATE DINESHMILLS.D_SAL_TRANSPORTER_MASTER SET TR_LET_NO='" + trNo + "',TR_LET_DATE=CURDATE(),LOT_NO='" + rsInvHeader.getInt("LOT_NO") + "' WHERE TRANSPORTER_ID ='" + rsInvHeader.getInt("TRANSPORTER_CODE") + "'");
                            }

                            String strGstrSQL = "INSERT INTO FINANCE.D_SAL_GSTR_INVOICE_ERP ";
                            strGstrSQL += "(COMPANY_ID,INPUT_TYPE,WH_CODE,INVOICE_TYPE,PARTY_CODE,GSTIN_NO,INVOICE_NO,INVOICE_DATE, ";
                            strGstrSQL += "INVOICE_VALUE,ITEM_DESC,HSN_CODE,TAXABLE_VALUE,IGST_PER,IGST_AMT,CGST_PER,CGST_AMT, ";
                            strGstrSQL += "SGST_PER,SGST_AMT,STATE_CODE,STATE_NAME,PLACE_OF_SUPPLY,RATE,REV_CHRG,E_COMM_GSTIN_NO, ";
                            strGstrSQL += "STATE_PIN_CODE,APPROVED,CANCELLED) ";

                            strGstrSQL += "(SELECT COMPANY_ID,INVOICE_TYPE,'FLT','Regular',PARTY_CODE,GSTIN_NO, ";
                            strGstrSQL += "INVOICE_NO,INVOICE_DATE,INVOICE_AMT,PRODUCT_DESC,HSN_CODE,INVOICE_TAXABLE_AMT, ";
                            strGstrSQL += "IGST_PER,IGST_AMT,CGST_PER,CGST_AMT,SGST_PER,SGST_AMT, ";
                            strGstrSQL += "SUBSTRING(GSTIN_NO,1,2),PLACE_OF_SUPPLY,'" + gstrPos + "', ";
                            strGstrSQL += "'12','N','',PINCODE,APPROVED,CANCELLED ";
                            strGstrSQL += "FROM PRODUCTION.FELT_SAL_INVOICE_HEADER ";
                            strGstrSQL += "WHERE INVOICE_NO='" + InvoiceNo + "' AND INVOICE_DATE='" + InvoiceDate + "') ";

                            System.out.println("GSTR SQL : " + strGstrSQL);
                            data.Execute(strGstrSQL);
                            
                            data.Execute("INSERT INTO PRODUCTION.FELT_INVOICE_PROCESS_LOG ( LOG_DATETIME, LOG_IP_NO, LOG_USER_ID, LOG_USER_NAME, LOG_BALE_NO, LOG_BALE_DATE, LOG_PARTY_CODE, LOG_INVOICE_NO, LOG_INVOICE_DATE, LOG_MESSAGE_SR_NO, LOG_MESSAGE, LOG_QUERY ) VALUES ( CURRENT_TIMESTAMP(), USER(), '"+EITLERPGLOBAL.gUserID+"', '"+gUserName+"', '" + pBaleNo + "', '" + pBaleDate + "', '" + pPartyCode + "', '"+InvNo+"', '"+InvDate+"', '12', 'DONE : GSTR INVOICE DATA UPDATED FOR "+InvNo+" DATED "+InvDate+"', '' ) ");

                            //EITLERP.FeltSales.common.JavaMail.SendMail("gaurang@dineshmills.com", "Invoice Detail :/p Charge Code : -" + ChargeCode + "-  Party Code : -" + Party_code + "- Inv No : -" + InvNo + "- Inv Date : -" + InvDate + "- Invoice Amount : -" + InvoiceAmount + "- Invoice Date : -" + InvoiceDate + "- Invoice No : -" + InvoiceNo + "-", "Invoice Detail", "");
//                                ExternalImportSJ(true, ChargeCode, InvoiceAmount, Party_code, InvNo, InvDate);
                        }
                    } catch (Exception c) {
                        c.printStackTrace();
//                        Msg = c.getMessage();
//                    log.logToFile(LogFileName, Msg, 2);
                        Done = true;
                    }

                    rsInvHeader.next();
                }
            }

            System.out.println("Finished");
        } catch (Exception e) {
        }
    }
    
    public String LotNo() {
        try {
            return data.getStringValueFromDB("SELECT MAX(LOT_NO) FROM PRODUCTION.FELT_SALES_INV_PROCESS_REPORT_DATA WHERE SUBSTRING(PROCESSING_TIME,1,10)='" + EITLERPGLOBAL.getCurrentDateDB() + "' ");
        } catch (Exception e) {
            return "0";
        }
    }

}
