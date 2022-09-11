/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.common;

import EITLERP.EITLERPGLOBAL;
import EITLERP.Finance.UtilFunctions;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
//import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 *
 */
public class clsOrderValueCalc {

//    static DecimalFormat f_single = new DecimalFormat("##.0");
//    static DecimalFormat f_double = new DecimalFormat("##.00");
    static DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    public String reason = "";

    public static FeltInvCalc calculate(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
        String gstinNo = "";

        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
//            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//            if (gstinNo.equals("")) {
//                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
//            }
            inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);

            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                System.out.println("RATE : " + sqlRt);
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

//            inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }                    
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            System.out.println("SQMTR_RATE : " + SQM_CHRG + " , WT RATE : " + WT_RATE);
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            float disc = 0;
            float seamvalue = 0;

//            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
//            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on product Discount: " + e.getMessage());
//                }
//            } else {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on piece Discount: " + e.getMessage());
//                }
//            }
            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
            
            String machineNo = data.getStringValueFromDB("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            String machinePosition = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            
            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
            System.out.println("diversionloss : SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        //System.out.println("* Query SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group(Group_Code);
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on Group Discount: " + e.getMessage());
                    }
                }

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND COALESCE(MACHINE_NO,0) = 0 AND COALESCE(MACHINE_POSITION,0) = 0 AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        //System.out.println("Find Dis 1 Qury : SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
                
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
            } else {
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                    //System.out.println("Find Discount Query : SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");    
                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                    rsData.first();
                    disc = rsData.getFloat(1);
                    seamvalue = rsData.getFloat(2);
                    inv_calc.setSanc_doc(rsData.getString(3));
                    inv_calc.setSanc_group("");
                    //System.out.println("Discount Percentage : "+disc);
                } catch (Exception e) {
                    disc = 0;
                    seamvalue = 0;
                    System.out.println("Error on piece Discount: " + e.getMessage());
                }
            }

            float seam_amt = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamPer(0);
                inv_calc.setFicSeamAmt(0);
            } else {
                inv_calc.setFicSeamPer(seamvalue);
                if (seamvalue == 100) {
                    seam_amt = inv_calc.getFicSeamChg();
                } else {
                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
                }
                inv_calc.setFicSeamAmt(seam_amt);
            }
//            System.out.println("before calc SEAM CHARGE : " + inv_calc.getFicSeamChg());
            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());
//            System.out.println("after calc SEAM CHARGE : " + inv_calc.getFicSeamChg());
            inv_calc.setFicDiscPer(disc);

            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
            inv_calc.setFicDiscAmt(disc_amt);

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);
            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }

            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0";
            //int insvar_ind = data.getIntValueFromDB(strInsVar);

            int insvar_ind = 0;

            if (data.IsRecordExist(strInsVar)) {
                insvar_ind = data.getIntValueFromDB(strInsVar);
//              
            }
            //if (insvar_ind == 1) {                
//                    inv_calc.setFicInsInd(0);                    
//                }else{
//                    inv_calc.setFicInsInd(1);
//                }
//                if (inv_calc.getFicInsInd() == 1) {
////                    if(insvar_ind == 0){
//                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//                }                        
//                    }
            if (insvar_ind == 1) {
                ins_amt = 0;
            }
//                    else{
//                        if(ins_amt==0){
//                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
//                        }
//                        else{
//                            ins_amt=ins_amt;
//                        }
//                    }      

            //}
//            if (inv_calc.getFicInsInd() == 1) {                           
////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//            }
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//        String state_gst_id = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");
//        if (gstinNo.equalsIgnoreCase("")) {
//            inv_calc.setFicCGSTPER(Float.valueOf(0));
//            inv_calc.setFicSGSTPER(Float.valueOf(0));
//            inv_calc.setFicIGSTPER(Float.valueOf(0));
//            CGST = Float.valueOf(0);
//            SGST = Float.valueOf(0);
//            IGST = Float.valueOf(0);
//        } else {
            if (gstinNo.equalsIgnoreCase("24")) {
                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                CGST = Float.valueOf(GST / 2);
                SGST = Float.valueOf(GST / 2);
            } else {
                inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
                IGST = GST;
            }
//        }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021            
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }

//    public static FeltInvCalc calculateWithoutGSTINNO(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate) {
//        FeltInvCalc inv_calc = new FeltInvCalc();
//        inv_calc.setReason("");
////        String gstinNo = "";
////
////        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
////        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
//////            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//////            if (gstinNo.equals("")) {
//////                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
//////            }
////	    inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
////        }
//
//        if (inv_calc.getReason().equals("")) {
//
//            inv_calc.setFicPieceNo(Piece_No);
//            inv_calc.setFicProductCode(Product_Code);
//            inv_calc.setFicPartyCode(Party_Code);
//
//            inv_calc.setFicLength(Length);
//            inv_calc.setFicWidth(Width);
//            inv_calc.setFicWeight(Weight);
//            inv_calc.setFicSqMtr(SQMT);
//            try {
//                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
//            } catch (ParseException ex) {
//                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
//            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
//            try {
//                Connection Conn;
//                Statement stmt;
//                ResultSet rsData;
//
//                Conn = data.getConn();
//                stmt = Conn.createStatement();
//                String prodCode = Product_Code.substring(0, 6);
//                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
//                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
//                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
//                rsData = stmt.executeQuery(sqlRt);
//                rsData.first();
//                CATEGORY = rsData.getString("CATEGORY");
//                SQM_CHRG = rsData.getFloat("SQM_CHRG");
//                WT_RATE = rsData.getFloat("WT_RATE");
//                SQM_IND = rsData.getString("SQM_IND");
//                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
//                PIN_IND = rsData.getString("PIN_IND");
//                SPR_IND = rsData.getString("SPR_IND");
//                SUR_IND = rsData.getString("SUR_IND");
//                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
//                PIN_CHRG = rsData.getFloat("PIN_CHRG");
//                SPR_CHRG = rsData.getFloat("SPR_CHRG");
//                SUR_CHRG = rsData.getFloat("SUR_CHRG");
//
//            } catch (Exception e) {
//                System.out.println("Error : " + e.getMessage());
//            }
//
//            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
//            inv_calc.setFicProductCatg(CATEGORY);
//
//            inv_calc.setFicRate(SQM_CHRG + WT_RATE);
//
//            float base_amt = 0;
//            float chem_trt = 0;
//            switch (SQM_IND) {
//                case "1":
////                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
////                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
//                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicRate();
//                    chem_trt = inv_calc.getFicSqMtr();
//                    break;
//                case "0":
//                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicRate()), 2) + "");
//                    chem_trt = inv_calc.getFicWeight();
//                    break;
//            }
//            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));
//
//            inv_calc.setFicChemTrtChg(0);
//            switch (CHEM_TRT_IND) {
//                case "1":
//                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
//                    break;
//                case "0":
//                    inv_calc.setFicChemTrtChg(0);
//                    break;
//            }
//
//            inv_calc.setFicPinChg(0);
//            switch (PIN_IND) {
//                case "1":
//                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
//                    break;
//                case "0":
//                    inv_calc.setFicPinChg(0);
//                    break;
//            }
//
//            inv_calc.setFicSpiralChg(0);
//            switch (SPR_IND) {
//                case "1":
//                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
//                    break;
//                case "0":
//                    inv_calc.setFicSpiralChg(0);
//                    break;
//            }
//
//            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());
//
//            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
//            //  FeltRateDiscMasterDetail rate_discount;
//            inv_calc.setFicDiscPer(0);
//            float disc = 0;
//            float seamvalue = 0;
//
////            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
////            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
////                try {
////                    Connection Conn;
////                    Statement stmt;
////                    ResultSet rsData;
////
////                    Conn = data.getConn();
////                    stmt = Conn.createStatement();
////                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
////                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
////                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
////                    rsData.first();
////                    disc = rsData.getFloat(1);
////                    seamvalue = rsData.getFloat(2);
////                    //System.out.println("Discount Percentage : "+disc);
////                } catch (Exception e) {
////                    disc = 0;
////                    seamvalue = 0;
////                    System.out.println("Error on product Discount: " + e.getMessage());
////                }
////            } else {
////                try {
////                    Connection Conn;
////                    Statement stmt;
////                    ResultSet rsData;
////
////                    Conn = data.getConn();
////                    stmt = Conn.createStatement();
////                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
////                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
////                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
////                    rsData.first();
////                    disc = rsData.getFloat(1);
////                    seamvalue = rsData.getFloat(2);
////                    //System.out.println("Discount Percentage : "+disc);
////                } catch (Exception e) {
////                    disc = 0;
////                    seamvalue = 0;
////                    System.out.println("Error on piece Discount: " + e.getMessage());
////                }
////            }
//            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
//            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
//            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                    try {
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;
//
//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                        rsData.first();
//                        disc = rsData.getFloat(1);
//                        seamvalue = rsData.getFloat(2);
//                        inv_calc.setSanc_doc(rsData.getString(3));
//                        inv_calc.setSanc_group(Group_Code);
//                        //System.out.println("Discount Percentage : "+disc);
//                    } catch (Exception e) {
//                        disc = 0;
//                        seamvalue = 0;
//                        System.out.println("Error on Group Discount: " + e.getMessage());
//                    }
//                }
//
//                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                    try {
//                        Connection Conn;
//                        Statement stmt;
//                        ResultSet rsData;
//
//                        Conn = data.getConn();
//                        stmt = Conn.createStatement();
//                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                        rsData.first();
//                        disc = rsData.getFloat(1);
//                        seamvalue = rsData.getFloat(2);
//                        inv_calc.setSanc_doc(rsData.getString(3));
//                        inv_calc.setSanc_group("");
//                        //System.out.println("Discount Percentage : "+disc);
//                    } catch (Exception e) {
//                        disc = 0;
//                        seamvalue = 0;
//                        System.out.println("Error on product Discount: " + e.getMessage());
//                    }
//                }
//            } else {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    inv_calc.setSanc_doc(rsData.getString(3));
//                    inv_calc.setSanc_group("");
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on piece Discount: " + e.getMessage());
//                }
//            }
//
//            float seam_amt = 0;
//
//            if (seamvalue == 0) {
//                inv_calc.setFicSeamPer(0);
//                inv_calc.setFicSeamAmt(0);
//            } else {
//                inv_calc.setFicSeamPer(seamvalue);
//                if (seamvalue == 100) {
//                    seam_amt = inv_calc.getFicSeamChg();
//                } else {
//                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
//                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
//                }
//                inv_calc.setFicSeamAmt(seam_amt);
//            }
//
//            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());
//
//            inv_calc.setFicDiscPer(disc);
//
//            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
//            inv_calc.setFicDiscAmt(disc_amt);
//
//            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
//            inv_calc.setFicDiscBasamt(disc_basamt);
//            float ins_amt = 0;
//            //inv_calc.setFicInsInd(0);
//            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
//            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
//            int ins_ind = data.getIntValueFromDB(strIns);
//
//            if (ins_ind == 1) {
//                //inv_calc.setFicInsInd(1);
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//            } else {
//                ins_amt = 0;
//            }
//
//            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
//            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0";
//            //int insvar_ind = data.getIntValueFromDB(strInsVar);
//
//            int insvar_ind = 0;
//
//            if (data.IsRecordExist(strInsVar)) {
//                insvar_ind = data.getIntValueFromDB(strInsVar);
////              
//            }
//            //if (insvar_ind == 1) {                
////                    inv_calc.setFicInsInd(0);                    
////                }else{
////                    inv_calc.setFicInsInd(1);
////                }
////                if (inv_calc.getFicInsInd() == 1) {
//////                    if(insvar_ind == 0){
////                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
////                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
////                ins_amt = (int) (ins_amt + 999) / 1000;
////                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
////                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
////                }                        
////                    }
//            if (insvar_ind == 1) {
//                ins_amt = 0;
//            }
////                    else{
////                        if(ins_amt==0){
////                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
////                ins_amt = (int) (ins_amt + 999) / 1000;
////                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
////                        }
////                        else{
////                            ins_amt=ins_amt;
////                        }
////                    }      
//
//            //}
////            if (inv_calc.getFicInsInd() == 1) {                           
//////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
////                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
////                ins_amt = (int) (ins_amt + 999) / 1000;
////                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
////                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
////            }
//            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));
//
//            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();
//
//            float IGST = 0;
//            float CGST = 0;
//            float SGST = 0;
//            inv_calc.setFicIGSTPER(0);
//            inv_calc.setFicCGSTPER(0);
//            inv_calc.setFicSGSTPER(0);
//
//            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");
//
//            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");
//
//
//            
////            if (gstinNo.equalsIgnoreCase("24")) {
////                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
////                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
////                CGST = Float.valueOf(GST / 2);
////                SGST = Float.valueOf(GST / 2);
////            } else {
//                inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
//                IGST = GST;
////           }
//
//            GST = Math.round(GST);
//            CGST = Math.round(CGST);
//            SGST = Math.round(SGST);
//            IGST = Math.round(IGST);
//
//            inv_calc.setFicGST(GST);
//            inv_calc.setFicIGST(IGST);
//            inv_calc.setFicCGST(CGST);
//            inv_calc.setFicSGST(SGST);
//
//            inv_calc.setSD(0);
//
//            inv_amt = inv_amt + IGST + CGST + SGST;
//
//            inv_calc.setFicInvAmt(inv_amt);
//
//        }
//
//        return inv_calc;
//
//    }
    public static FeltInvCalc calculateWithoutGSTINNO(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
//        String gstinNo = "";
//
//        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
////            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
////            if (gstinNo.equals("")) {
////                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
////            }
//	    inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
//        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);

            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }
//                    WT_RATE = OLD_RATE;
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            float disc = 0;
            float seamvalue = 0;

//            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
//            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on product Discount: " + e.getMessage());
//                }
//            } else {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on piece Discount: " + e.getMessage());
//                }
//            }
            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
            
            String machineNo = data.getStringValueFromDB("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            String machinePosition = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            
            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO,YRED_DISC_PER,YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group(Group_Code);
                        inv_calc.setFicYearEndPer(rsData.getFloat(4));
                        inv_calc.setFicSeamValue(rsData.getFloat(2));
                        inv_calc.setFicYearEndSeam(rsData.getFloat(5));
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on Group Discount: " + e.getMessage());
                    }
                }

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND COALESCE(MACHINE_NO,0) = 0 AND COALESCE(MACHINE_POSITION,0) = 0 AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO,YRED_DISC_PER,YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        inv_calc.setFicYearEndPer(rsData.getFloat(4));
                        inv_calc.setFicSeamValue(rsData.getFloat(2));
                        inv_calc.setFicYearEndSeam(rsData.getFloat(5));
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
                
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
            } else {
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO,YRED_DISC_PER,YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                    rsData.first();
                    disc = rsData.getFloat(1);
                    seamvalue = rsData.getFloat(2);
                    inv_calc.setSanc_doc(rsData.getString(3));
                    inv_calc.setSanc_group("");
                    inv_calc.setFicYearEndPer(rsData.getFloat(4));
                    inv_calc.setFicSeamValue(rsData.getFloat(2));
                    inv_calc.setFicYearEndSeam(rsData.getFloat(5));
                    //System.out.println("Discount Percentage : "+disc);
                } catch (Exception e) {
                    disc = 0;
                    seamvalue = 0;
                    System.out.println("Error on piece Discount: " + e.getMessage());
                }
            }

            float seam_amt_dc = 0;
            float seam_amt_yr = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamValue(0);
                inv_calc.setFicYearEndSeam(0);
                inv_calc.setFicNetSeamUnit(0);
            } else {
                inv_calc.setFicSeamValue(seamvalue);
                if (seamvalue == 100) {
                    inv_calc.setFicNetSeamUnit(0);
                } else {
                    seam_amt_dc = Float.parseFloat(EITLERPGLOBAL.round(((4899 * inv_calc.getFicSeamValue()) / 100), 2) + "");
                    seam_amt_yr = Float.parseFloat(EITLERPGLOBAL.round(((4899 * inv_calc.getFicYearEndSeam()) / 100), 2) + "");
                }
                inv_calc.setFicNetSeamUnit(4899 - seam_amt_dc - seam_amt_yr);
            }

            float seam_amt = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamPer(0);
                inv_calc.setFicSeamAmt(0);
            } else {
                inv_calc.setFicSeamPer(seamvalue);
                if (seamvalue == 100) {
                    seam_amt = inv_calc.getFicSeamChg();
                } else {
                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
                }
                inv_calc.setFicSeamAmt(seam_amt);
            }

            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());
//            if(Product_Code.equals("729000"))
//            {
//                inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());
//            } 
            inv_calc.setFicDiscPer(disc);

            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
            inv_calc.setFicDiscAmt(disc_amt);

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);
            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }

            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0";
            //int insvar_ind = data.getIntValueFromDB(strInsVar);

            int insvar_ind = 0;

            if (data.IsRecordExist(strInsVar)) {
                insvar_ind = data.getIntValueFromDB(strInsVar);
//              
            }
            //if (insvar_ind == 1) {                
//                    inv_calc.setFicInsInd(0);                    
//                }else{
//                    inv_calc.setFicInsInd(1);
//                }
//                if (inv_calc.getFicInsInd() == 1) {
////                    if(insvar_ind == 0){
//                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//                }                        
//                    }
            if (insvar_ind == 1) {
                ins_amt = 0;
            }
//                    else{
//                        if(ins_amt==0){
//                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
//                        }
//                        else{
//                            ins_amt=ins_amt;
//                        }
//                    }      

            //}
//            if (inv_calc.getFicInsInd() == 1) {                           
////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//            }
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//            if (gstinNo.equalsIgnoreCase("24")) {
//                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
//                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
//                CGST = Float.valueOf(GST / 2);
//                SGST = Float.valueOf(GST / 2);
//            } else {
            inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
            IGST = GST;
//           }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021                        
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }

    //NEW FOR PRIOR APPROVAL
    public static FeltInvCalc calculate_ForPriorApproval_GetExist(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
//        String gstinNo = "";
//
//        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
////            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
////            if (gstinNo.equals("")) {
////                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
////            }
//	    inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
//        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);

            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }
//                    WT_RATE = OLD_RATE;
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            float disc = 0;
            float seamvalue = 0;

//            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
//            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on product Discount: " + e.getMessage());
//                }
//            } else {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on piece Discount: " + e.getMessage());
//                }
//            }
            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");            
            
            String machineNo = data.getStringValueFromDB("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            String machinePosition = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            
            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO,YRED_DISC_PER,YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group(Group_Code);
                        inv_calc.setFicYearEndPer(rsData.getFloat(4));
                        inv_calc.setFicSeamValue(rsData.getFloat(2));
                        inv_calc.setFicYearEndSeam(rsData.getFloat(5));
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on Group Discount: " + e.getMessage());
                    }
                }

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND COALESCE(MACHINE_NO,0) = 0 AND COALESCE(MACHINE_POSITION,0) = 0 AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO,YRED_DISC_PER,YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        inv_calc.setFicYearEndPer(rsData.getFloat(4));
                        inv_calc.setFicSeamValue(rsData.getFloat(2));
                        inv_calc.setFicYearEndSeam(rsData.getFloat(5));
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
                
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
            } else {
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO,YRED_DISC_PER,YRED_SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL)  ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                    rsData.first();
                    disc = rsData.getFloat(1);
                    seamvalue = rsData.getFloat(2);
                    inv_calc.setSanc_doc(rsData.getString(3));
                    inv_calc.setSanc_group("");
                    inv_calc.setFicYearEndPer(rsData.getFloat(4));
                    inv_calc.setFicSeamValue(rsData.getFloat(2));
                    inv_calc.setFicYearEndSeam(rsData.getFloat(5));
                    //System.out.println("Discount Percentage : "+disc);
                } catch (Exception e) {
                    disc = 0;
                    seamvalue = 0;
                    System.out.println("Error on piece Discount: " + e.getMessage());
                }
            }

            float seam_amt_dc = 0;
            float seam_amt_yr = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamValue(0);
                inv_calc.setFicYearEndSeam(0);
                inv_calc.setFicNetSeamUnit(0);
            } else {
                inv_calc.setFicSeamValue(seamvalue);
                if (seamvalue == 100) {
                    inv_calc.setFicNetSeamUnit(0);
                } else {
                    seam_amt_dc = Float.parseFloat(EITLERPGLOBAL.round(((4899 * inv_calc.getFicSeamValue()) / 100), 2) + "");
                    seam_amt_yr = Float.parseFloat(EITLERPGLOBAL.round(((4899 * inv_calc.getFicYearEndSeam()) / 100), 2) + "");
                }
                inv_calc.setFicNetSeamUnit(4899 - seam_amt_dc - seam_amt_yr);
            }

            float seam_amt = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamPer(0);
                inv_calc.setFicSeamAmt(0);
            } else {
                inv_calc.setFicSeamPer(seamvalue);
                if (seamvalue == 100) {
                    seam_amt = inv_calc.getFicSeamChg();
                } else {
                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
                }
                inv_calc.setFicSeamAmt(seam_amt);
            }

//            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());
            if(Product_Code.equals("729000"))
            {
                inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());
            } 
            inv_calc.setFicDiscPer(disc);

            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
            inv_calc.setFicDiscAmt(disc_amt);

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);
            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }

            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0";
            //int insvar_ind = data.getIntValueFromDB(strInsVar);

            int insvar_ind = 0;

            if (data.IsRecordExist(strInsVar)) {
                insvar_ind = data.getIntValueFromDB(strInsVar);
//              
            }
            //if (insvar_ind == 1) {                
//                    inv_calc.setFicInsInd(0);                    
//                }else{
//                    inv_calc.setFicInsInd(1);
//                }
//                if (inv_calc.getFicInsInd() == 1) {
////                    if(insvar_ind == 0){
//                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//                }                        
//                    }
            if (insvar_ind == 1) {
                ins_amt = 0;
            }
//                    else{
//                        if(ins_amt==0){
//                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
//                        }
//                        else{
//                            ins_amt=ins_amt;
//                        }
//                    }      

            //}
//            if (inv_calc.getFicInsInd() == 1) {                           
////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//            }
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//            if (gstinNo.equalsIgnoreCase("24")) {
//                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
//                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
//                CGST = Float.valueOf(GST / 2);
//                SGST = Float.valueOf(GST / 2);
//            } else {
            inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
            IGST = GST;
//           }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021                        
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/        
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }

    //fro Invoice Process
    public static FeltInvCalc calculate(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate, String baleNo, String baleDate) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
        String gstinNo = "";

        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
//            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//            if (gstinNo.equals("")) {
//                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
//            }
            inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);
            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }
//                    WT_RATE = OLD_RATE;
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            float disc = 0;
            float seamvalue = 0;

            inv_calc.setSanc_doc("");
            inv_calc.setSanc_group("");

            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
            
            String machineNo = data.getStringValueFromDB("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            String machinePosition = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            
            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group(Group_Code);
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on Group Discount: " + e.getMessage());
                    }
                }

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND COALESCE(MACHINE_NO,0) = 0 AND COALESCE(MACHINE_POSITION,0) = 0 AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
                
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
            } else {
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                    rsData.first();
                    disc = rsData.getFloat(1);
                    seamvalue = rsData.getFloat(2);
                    inv_calc.setSanc_doc(rsData.getString(3));
                    inv_calc.setSanc_group("");
                    //System.out.println("Discount Percentage : "+disc);
                } catch (Exception e) {
                    disc = 0;
                    seamvalue = 0;
                    System.out.println("Error on piece Discount: " + e.getMessage());
                }
            }

            float seam_amt = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamPer(0);
                inv_calc.setFicSeamAmt(0);
            } else {
                inv_calc.setFicSeamPer(seamvalue);
                if (seamvalue == 100) {
                    seam_amt = inv_calc.getFicSeamChg();
                } else {
                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
                }
                inv_calc.setFicSeamAmt(seam_amt);
            }

            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());

            inv_calc.setFicDiscPer(disc);

            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
            inv_calc.setFicDiscAmt(disc_amt);

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);

            float aosdAmt = 0;
            inv_calc.setAosd_per(0);
            inv_calc.setAosd_amt(aosdAmt);

            //06 June 2019
//            if (data.IsRecordExist("SELECT D.* FROM PRODUCTION.FELT_SAL_SCHEME_HEADER H, PRODUCTION.FELT_SAL_SCHEME_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.DOC_DATE=D.DOC_DATE AND H.APPROVED=1 AND H.CANCELED=0 AND D.PIECE_NO='" + Piece_No + "' AND D.PARTY_CODE='" + Party_Code + "' AND D.PRODUCT_CODE='" + Product_Code + "'")) {
//                try {
//                    inv_calc.setAosd_per(1);
//                    aosdAmt = (inv_calc.getFicBasAmount() * inv_calc.getAosd_per()) / 100;
//                    inv_calc.setAosd_amt(aosdAmt);
//                } catch (Exception e) {
//                    inv_calc.setAosd_per(0);
//                    inv_calc.setAosd_amt(0);
//                    System.out.println("Error on Annual Order Special Discount: " + e.getMessage());
//                }
//            }
            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() - inv_calc.getAosd_amt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }

            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0 AND BALE_NO='" + baleNo + "' AND INSURANCE_CODE=1 ";
            //int insvar_ind = data.getIntValueFromDB(strInsVar);

            int insvar_ind = 0;

            if (data.IsRecordExist(strInsVar)) {
                insvar_ind = data.getIntValueFromDB(strInsVar);
//              
            }
            //if (insvar_ind == 1) {                
//                    inv_calc.setFicInsInd(0);                    
//                }else{
//                    inv_calc.setFicInsInd(1);
//                }
//                if (inv_calc.getFicInsInd() == 1) {
////                    if(insvar_ind == 0){
//                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//                }                        
//                    }
            if (insvar_ind == 1) {
                ins_amt = 0;
            }
//                    else{
//                        if(ins_amt==0){
//                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
//                        }
//                        else{
//                            ins_amt=ins_amt;
//                        }
//                    }

            //}
            String pkgTrnsMode = data.getStringValueFromDB("SELECT PKG_TRANSPORT_MODE FROM PRODUCTION.FELT_PKG_SLIP_HEADER WHERE PKG_BALE_NO = '" + baleNo + "' AND PKG_BALE_DATE = '" + baleDate + "' ");
            if (pkgTrnsMode.equals("BY HANDDELIVERY")) {
                ins_amt = 0;
            }

//            if (inv_calc.getFicInsInd() == 1) {                           
////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//            }
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

//            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();
            float inv_amt = inv_calc.getFicDiscBasamt() - inv_calc.getAosd_amt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//        String state_gst_id = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");
//        if (gstinNo.equalsIgnoreCase("")) {
//            inv_calc.setFicCGSTPER(Float.valueOf(0));
//            inv_calc.setFicSGSTPER(Float.valueOf(0));
//            inv_calc.setFicIGSTPER(Float.valueOf(0));
//            CGST = Float.valueOf(0);
//            SGST = Float.valueOf(0);
//            IGST = Float.valueOf(0);
//        } else {
            if (gstinNo.equalsIgnoreCase("24")) {
                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                CGST = Float.valueOf(GST / 2);
                SGST = Float.valueOf(GST / 2);
            } else {
                inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
                IGST = GST;
            }
//        }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021                        
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/        
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }

    //TEMP
    public static FeltInvCalc calculateWithOutGST(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
        String gstinNo = "";

        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
            if (gstinNo.equals("")) {
                //inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
                gstinNo = "33";
            }
        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);

            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }
//                    WT_RATE = OLD_RATE;
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            float disc = 0;
            float seamvalue = 0;

//            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
//            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on product Discount: " + e.getMessage());
//                }
//            } else {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on piece Discount: " + e.getMessage());
//                }
//            }
            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
            
            String machineNo = data.getStringValueFromDB("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            String machinePosition = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            
            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
            System.out.println("DISC : v : SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group(Group_Code);
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on Group Discount: " + e.getMessage());
                    }
                }

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND COALESCE(MACHINE_NO,0) = 0 AND COALESCE(MACHINE_POSITION,0) = 0 AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
                
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
            } else {
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                    rsData.first();
                    disc = rsData.getFloat(1);
                    seamvalue = rsData.getFloat(2);
                    inv_calc.setSanc_doc(rsData.getString(3));
                    inv_calc.setSanc_group("");
                    //System.out.println("Discount Percentage : "+disc);
                } catch (Exception e) {
                    disc = 0;
                    seamvalue = 0;
                    System.out.println("Error on piece Discount: " + e.getMessage());
                }
            }

            float seam_amt = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamPer(0);
                inv_calc.setFicSeamAmt(0);
            } else {
                inv_calc.setFicSeamPer(seamvalue);
                if (seamvalue == 100) {
                    seam_amt = inv_calc.getFicSeamChg();
                } else {
                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
                }
                inv_calc.setFicSeamAmt(seam_amt);
            }

            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());

            inv_calc.setFicDiscPer(disc);

            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
            inv_calc.setFicDiscAmt(disc_amt);

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);
            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }

            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0";
            //int insvar_ind = data.getIntValueFromDB(strInsVar);

            int insvar_ind = 0;

            if (data.IsRecordExist(strInsVar)) {
                insvar_ind = data.getIntValueFromDB(strInsVar);
//              
            }
            //if (insvar_ind == 1) {                
//                    inv_calc.setFicInsInd(0);                    
//                }else{
//                    inv_calc.setFicInsInd(1);
//                }
//                if (inv_calc.getFicInsInd() == 1) {
////                    if(insvar_ind == 0){
//                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//                }                        
//                    }
            if (insvar_ind == 1) {
                ins_amt = 0;
            }
//                    else{
//                        if(ins_amt==0){
//                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
//                        }
//                        else{
//                            ins_amt=ins_amt;
//                        }
//                    }      

            //}
//            if (inv_calc.getFicInsInd() == 1) {                           
////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//            }
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//        String state_gst_id = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");
//        if (gstinNo.equalsIgnoreCase("")) {
//            inv_calc.setFicCGSTPER(Float.valueOf(0));
//            inv_calc.setFicSGSTPER(Float.valueOf(0));
//            inv_calc.setFicIGSTPER(Float.valueOf(0));
//            CGST = Float.valueOf(0);
//            SGST = Float.valueOf(0);
//            IGST = Float.valueOf(0);
//        } else {
            if (gstinNo.equalsIgnoreCase("24")) {
                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                CGST = Float.valueOf(GST / 2);
                SGST = Float.valueOf(GST / 2);
            } else {
                inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
                IGST = GST;
            }
//        }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021                        
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/        
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }

    public static FeltInvCalc calculate(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate, String PI) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
        String gstinNo = "";

        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
//            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//            if (gstinNo.equals("")) {
//                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
//            }
            inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);

            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }
//                    WT_RATE = OLD_RATE;
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            float disc = 0;
            float seamvalue = 0;

//            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
//            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on product Discount: " + e.getMessage());
//                }
//            } else {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on piece Discount: " + e.getMessage());
//                }
//            }
            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
            
            String machineNo = data.getStringValueFromDB("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            String machinePosition = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            
            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group(Group_Code);
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on Group Discount: " + e.getMessage());
                    }
                }

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND COALESCE(MACHINE_NO,0) = 0 AND COALESCE(MACHINE_POSITION,0) = 0 AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
                
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
            } else {
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                    rsData.first();
                    disc = rsData.getFloat(1);
                    seamvalue = rsData.getFloat(2);
                    inv_calc.setSanc_doc(rsData.getString(3));
                    inv_calc.setSanc_group("");
                    //System.out.println("Discount Percentage : "+disc);
                } catch (Exception e) {
                    disc = 0;
                    seamvalue = 0;
                    System.out.println("Error on piece Discount: " + e.getMessage());
                }
            }

            float seam_amt = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamPer(0);
                inv_calc.setFicSeamAmt(0);
            } else {
                inv_calc.setFicSeamPer(seamvalue);
                if (seamvalue == 100) {
                    seam_amt = inv_calc.getFicSeamChg();
                } else {
                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
                }
                inv_calc.setFicSeamAmt(seam_amt);
            }

            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());

            inv_calc.setFicDiscPer(disc);

            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
            inv_calc.setFicDiscAmt(disc_amt);

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);

            float aosdAmt = 0;
            inv_calc.setAosd_per(0);
            inv_calc.setAosd_amt(aosdAmt);

            //06 June 2019
//            if (data.IsRecordExist("SELECT D.* FROM PRODUCTION.FELT_SAL_SCHEME_HEADER H, PRODUCTION.FELT_SAL_SCHEME_DETAIL D WHERE H.DOC_NO=D.DOC_NO AND H.DOC_DATE=D.DOC_DATE AND H.APPROVED=1 AND H.CANCELED=0 AND D.PIECE_NO='" + Piece_No + "' AND D.PARTY_CODE='" + Party_Code + "' AND D.PRODUCT_CODE='" + Product_Code + "'")) {
//                try {
//                    inv_calc.setAosd_per(1);
//                    aosdAmt = (inv_calc.getFicBasAmount() * inv_calc.getAosd_per()) / 100;
//                    inv_calc.setAosd_amt(aosdAmt);
//                } catch (Exception e) {
//                    inv_calc.setAosd_per(0);
//                    inv_calc.setAosd_amt(0);
//                    System.out.println("Error on Annual Order Special Discount: " + e.getMessage());
//                }
//            }
            inv_calc.setFicDiscBasamt(disc_basamt - inv_calc.getAosd_amt());

            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }

            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0";
            //int insvar_ind = data.getIntValueFromDB(strInsVar);

            int insvar_ind = 0;

            if (data.IsRecordExist(strInsVar)) {
                insvar_ind = data.getIntValueFromDB(strInsVar);
//              
            }
            //if (insvar_ind == 1) {                
//                    inv_calc.setFicInsInd(0);                    
//                }else{
//                    inv_calc.setFicInsInd(1);
//                }
//                if (inv_calc.getFicInsInd() == 1) {
////                    if(insvar_ind == 0){
//                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//                }                        
//                    }
            if (insvar_ind == 1) {
                ins_amt = 0;
            }
//                    else{
//                        if(ins_amt==0){
//                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
//                        }
//                        else{
//                            ins_amt=ins_amt;
//                        }
//                    }      

            //}
//            if (inv_calc.getFicInsInd() == 1) {                           
////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//            }
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//        String state_gst_id = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");
//        if (gstinNo.equalsIgnoreCase("")) {
//            inv_calc.setFicCGSTPER(Float.valueOf(0));
//            inv_calc.setFicSGSTPER(Float.valueOf(0));
//            inv_calc.setFicIGSTPER(Float.valueOf(0));
//            CGST = Float.valueOf(0);
//            SGST = Float.valueOf(0);
//            IGST = Float.valueOf(0);
//        } else {
            if (gstinNo.equalsIgnoreCase("24")) {
                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                CGST = Float.valueOf(GST / 2);
                SGST = Float.valueOf(GST / 2);
            } else {
                inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
                IGST = GST;
            }
//        }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021                        
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/        
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }
    
    public static FeltInvCalc calculate_forDiversion(Float Th_Weight,String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
        String gstinNo = "";

        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
//            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//            if (gstinNo.equals("")) {
//                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
//            }
            inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);

            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                System.out.println("RATE : " + sqlRt);
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                
                String PieceNo = data.getStringValueFromDB("SELECT PR_PIECE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                if(PieceNo.equals(""))
                {
                    th_weight = Th_Weight;
                }
                
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }
//                    WT_RATE = OLD_RATE;
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Th_Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            System.out.println("SQMTR_RATE : " + SQM_CHRG + " , WT RATE : " + WT_RATE);
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            float disc = 0;
            float seamvalue = 0;

//            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
//            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC")) {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on product Discount: " + e.getMessage());
//                }
//            } else {
//                try {
//                    Connection Conn;
//                    Statement stmt;
//                    ResultSet rsData;
//
//                    Conn = data.getConn();
//                    stmt = Conn.createStatement();
//                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
//                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
//                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
//                    rsData.first();
//                    disc = rsData.getFloat(1);
//                    seamvalue = rsData.getFloat(2);
//                    //System.out.println("Discount Percentage : "+disc);
//                } catch (Exception e) {
//                    disc = 0;
//                    seamvalue = 0;
//                    System.out.println("Error on piece Discount: " + e.getMessage());
//                }
//            }
            String Group_Code = data.getStringValueFromDB("SELECT H.GROUP_CODE FROM PRODUCTION.FELT_GROUP_MASTER_HEADER H, PRODUCTION.FELT_GROUP_MASTER_DETAIL D WHERE H.GROUP_CODE=D.GROUP_CODE AND PARTY_CODE='" + Party_Code + "' AND H.APPROVED=1 AND H.CANCELED=0 ORDER BY H.GROUP_CODE");
            
            String machineNo = data.getStringValueFromDB("SELECT PR_MACHINE_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            String machinePosition = data.getStringValueFromDB("SELECT PR_POSITION_NO FROM PRODUCTION.FELT_SALES_PIECE_REGISTER WHERE PR_PIECE_NO='" + Piece_No + "' ");
            
            //if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC")) {
            System.out.println("diversionloss : SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
            if (!data.IsRecordExist("SELECT PIECE_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO='" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        //System.out.println("* Query SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND GROUP_CODE = " + Group_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group(Group_Code);
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on Group Discount: " + e.getMessage());
                    }
                }

                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND COALESCE(MACHINE_NO,0) = 0 AND COALESCE(MACHINE_POSITION,0) = 0 AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        //System.out.println("Find Dis 1 Qury : SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
                
                if (data.IsRecordExist("SELECT * FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC")) {
                    try {
                        Connection Conn;
                        Statement stmt;
                        ResultSet rsData;

                        Conn = data.getConn();
                        stmt = Conn.createStatement();
                        // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                        //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                        rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND SUBSTRING(PRODUCT_CODE,1,6) = " + Product_Code + " AND MACHINE_NO = " + machineNo + " AND MACHINE_POSITION = " + machinePosition + " AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                        rsData.first();
                        disc = rsData.getFloat(1);
                        seamvalue = rsData.getFloat(2);
                        inv_calc.setSanc_doc(rsData.getString(3));
                        inv_calc.setSanc_group("");
                        //System.out.println("Discount Percentage : "+disc);
                    } catch (Exception e) {
                        disc = 0;
                        seamvalue = 0;
                        System.out.println("Error on product Discount: " + e.getMessage());
                    }
                }
            } else {
                try {
                    Connection Conn;
                    Statement stmt;
                    ResultSet rsData;

                    Conn = data.getConn();
                    stmt = Conn.createStatement();
                    // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+CurDate+"\" AND (EFFECTIVE_TO >= \""+CurDate+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                    //rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) ORDER BY EFFECTIVE_FROM DESC");
                    //System.out.println("Find Discount Query : SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC");    
                    rsData = stmt.executeQuery("SELECT DISC_PER,SEAM_VALUE,MASTER_NO FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = 1 AND CANCELED= 0 AND PARTY_CODE = " + Party_Code + " AND PIECE_NO = '" + Piece_No + "' AND EFFECTIVE_FROM <= '" + CurDate + "' AND (EFFECTIVE_TO >= '" + CurDate + "' OR EFFECTIVE_TO = '0000-00-00'  OR EFFECTIVE_TO IS NULL) AND DIVERSION_FLAG=0 ORDER BY EFFECTIVE_FROM DESC,MASTER_NO DESC");
                    rsData.first();
                    disc = rsData.getFloat(1);
                    seamvalue = rsData.getFloat(2);
                    inv_calc.setSanc_doc(rsData.getString(3));
                    inv_calc.setSanc_group("");
                    //System.out.println("Discount Percentage : "+disc);
                } catch (Exception e) {
                    disc = 0;
                    seamvalue = 0;
                    System.out.println("Error on piece Discount: " + e.getMessage());
                }
            }

            float seam_amt = 0;

            if (seamvalue == 0) {
                inv_calc.setFicSeamPer(0);
                inv_calc.setFicSeamAmt(0);
            } else {
                inv_calc.setFicSeamPer(seamvalue);
                if (seamvalue == 100) {
                    seam_amt = inv_calc.getFicSeamChg();
                } else {
                    //seam_amt = Float.parseFloat(f_double.format((4899 - ((4899 * seamvalue)/100)) * inv_calc.getFicWidth()));
                    seam_amt = Float.parseFloat(EITLERPGLOBAL.round(((4899 * seamvalue) / 100) * inv_calc.getFicWidth(), 2) + "");
                }
                inv_calc.setFicSeamAmt(seam_amt);
            }
//            System.out.println("before calc SEAM CHARGE : " + inv_calc.getFicSeamChg());
            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());
//            System.out.println("after calc SEAM CHARGE : " + inv_calc.getFicSeamChg());
            inv_calc.setFicDiscPer(disc);

            float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer()) / 100;
            inv_calc.setFicDiscAmt(disc_amt);

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);
            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }

            //SELECT * FROM PRODUCTION.FELT_INV_PROCESS_VAR
            String strInsVar = "SELECT INSURANCE_CODE FROM PRODUCTION.FELT_INV_PROCESS_VAR_GST WHERE PROCESSING_DATE>='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELED=0";
            //int insvar_ind = data.getIntValueFromDB(strInsVar);

            int insvar_ind = 0;

            if (data.IsRecordExist(strInsVar)) {
                insvar_ind = data.getIntValueFromDB(strInsVar);
//              
            }
            //if (insvar_ind == 1) {                
//                    inv_calc.setFicInsInd(0);                    
//                }else{
//                    inv_calc.setFicInsInd(1);
//                }
//                if (inv_calc.getFicInsInd() == 1) {
////                    if(insvar_ind == 0){
//                        //            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//                }                        
//                    }
            if (insvar_ind == 1) {
                ins_amt = 0;
            }
//                    else{
//                        if(ins_amt==0){
//                  ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");                          
//                        }
//                        else{
//                            ins_amt=ins_amt;
//                        }
//                    }      

            //}
//            if (inv_calc.getFicInsInd() == 1) {                           
////            ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1 + (CST_PER + VAT_PER))) * 1.1 ) + "");//0.10 * 0.0039) + "");
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
//                ins_amt = (int) (ins_amt + 999) / 1000;
//                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
//                //ins_amt = Float.parseFloat((ins_amt * 3.9) + "");
//            }
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//        String state_gst_id = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");
//        if (gstinNo.equalsIgnoreCase("")) {
//            inv_calc.setFicCGSTPER(Float.valueOf(0));
//            inv_calc.setFicSGSTPER(Float.valueOf(0));
//            inv_calc.setFicIGSTPER(Float.valueOf(0));
//            CGST = Float.valueOf(0);
//            SGST = Float.valueOf(0);
//            IGST = Float.valueOf(0);
//        } else {
            if (gstinNo.equalsIgnoreCase("24")) {
                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                CGST = Float.valueOf(GST / 2);
                SGST = Float.valueOf(GST / 2);
            } else {
                inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
                IGST = GST;
            }
//        }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021                        
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/        
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }
    
    public static FeltInvCalc calculateWithoutDiscount(String Piece_No, String Product_Code, String Party_Code, Float Length, Float Width, Float Weight, Float SQMT, String CurDate) {
        FeltInvCalc inv_calc = new FeltInvCalc();
        inv_calc.setReason("");
        String gstinNo = "33";

//        gstinNo = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
//        if (gstinNo.equals("") && Party_Code.startsWith("8")) {
////            gstinNo = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2 AND APPROVED=1 AND CANCELLED=0");
////            if (gstinNo.equals("")) {
////                inv_calc.setReason("GSTIN NO/STATE GST CODE missing for " + Party_Code + " Party Code.");
////            }
//            inv_calc.setReason("GSTIN NO missing for " + Party_Code + " Party Code.");
//        }

        if (inv_calc.getReason().equals("")) {

            inv_calc.setFicPieceNo(Piece_No);
            inv_calc.setFicProductCode(Product_Code);
            inv_calc.setFicPartyCode(Party_Code);
            inv_calc.setFicLength(Length);
            inv_calc.setFicWidth(Width);
            inv_calc.setFicWeight(Weight);
            inv_calc.setFicSqMtr(SQMT);
            try {
                inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
            } catch (ParseException ex) {
                //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

            String CATEGORY = "", SQM_IND = "", CHEM_TRT_IND = "", PIN_IND = "", SPR_IND = "", SUR_IND = "";
            float WT_RATE = 0, SQM_CHRG = 0, CHEM_TRT_CHRG = 0, PIN_CHRG = 0, SPR_CHRG = 0, SUR_CHRG = 0;
            float OLD_RATE = 0, WEIGHT_RATE_CRITERIA = 0, SURCHARGE_1_PER = 0, SURCHARGE_2_PER = 0, SURCHARGE_LENGTH_CRITERIA = 0;
            try {
                Connection Conn;
                Statement stmt;
                ResultSet rsData;

                Conn = data.getConn();
                stmt = Conn.createStatement();
                String prodCode = Product_Code.substring(0, 6);
                // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '" + Product_Code + "'");
                String sqlRt = "SELECT * FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE = '" + prodCode + "'  AND APPROVED=1 AND CANCELED=0 AND EFFECTIVE_FROM<='" + EITLERPGLOBAL.getCurrentDateDB() + "' AND (EFFECTIVE_TO>='" + EITLERPGLOBAL.getCurrentDateDB() + "' OR EFFECTIVE_TO='0000-00-00')";
                rsData = stmt.executeQuery(sqlRt);
                rsData.first();
                CATEGORY = rsData.getString("CATEGORY");
                SQM_CHRG = rsData.getFloat("SQM_CHRG");
                WT_RATE = rsData.getFloat("WT_RATE");
                SQM_IND = rsData.getString("SQM_IND");
                CHEM_TRT_IND = rsData.getString("CHEM_TRT_IND");
                PIN_IND = rsData.getString("PIN_IND");
                SPR_IND = rsData.getString("SPR_IND");
                SUR_IND = rsData.getString("SUR_IND");
                CHEM_TRT_CHRG = rsData.getFloat("CHEM_TRT_CHRG");
                PIN_CHRG = rsData.getFloat("PIN_CHRG");
                SPR_CHRG = rsData.getFloat("SPR_CHRG");
                SUR_CHRG = rsData.getFloat("SUR_CHRG");

                OLD_RATE = rsData.getFloat("OLD_RATE");
                WEIGHT_RATE_CRITERIA = rsData.getFloat("WEIGHT_RATE_CRITERIA");
                SURCHARGE_LENGTH_CRITERIA = rsData.getFloat("SURCHARGE_LENGTH_CRITERIA");
                SURCHARGE_1_PER = rsData.getFloat("SURCHARGE_1_PER");
                SURCHARGE_2_PER = rsData.getFloat("SURCHARGE_2_PER");

                inv_calc.setFicOldRate(OLD_RATE);

            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }

            //inv_calc.setFicSqMtr(inv_calc.getFicLength() * inv_calc.getFicWidth());
            inv_calc.setFicProductCatg(CATEGORY);

//////////////Piece_No
            inv_calc.setFicSurcharge_per(0);
            inv_calc.setFicSurcharge_rate(0);
            if (!Piece_No.equals("")) {
                String indicator = data.getStringValueFromDB("SELECT PR_RATE_INDICATOR FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
//                double th_weight = data.getDoubleValueFromDB("SELECT PR_THORITICAL_WEIGHT FROM PRODUCTION.FELT_SALES_PIECE_REGISTER where PR_PIECE_NO='" + Piece_No + "'");
                double th_weight = data.getDoubleValueFromDB("SELECT CASE WHEN COALESCE(TAGGING_APPROVAL_IND,0)=3 THEN CASE WHEN COALESCE(PR_TENDER_WEIGHT,0)!=0 THEN PR_TENDER_WEIGHT ELSE PR_THORITICAL_WEIGHT END ELSE PR_THORITICAL_WEIGHT END FROM PRODUCTION.FELT_SALES_PIECE_REGISTER,DINESHMILLS.D_SAL_PARTY_MASTER WHERE PR_PARTY_CODE=PARTY_CODE AND PR_PIECE_NO='" + Piece_No + "'");
                if (indicator.equals("OLD")) {
                    if (SQM_IND.equals("1")) {
                        SQM_CHRG = OLD_RATE;
                    } else {
                        WT_RATE = OLD_RATE;
                    }
//                    WT_RATE = OLD_RATE;
                } 
//                else 
                if (th_weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            } else {
                //NEW
                if (Weight >= WEIGHT_RATE_CRITERIA) {
                    //NEW 
                    //WT_RATE = WT_RATE;
                } else {
                    if (Length >= SURCHARGE_LENGTH_CRITERIA) {
                        inv_calc.setFicSurcharge_per(SURCHARGE_1_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_1_PER) / 100, 0));

                    } else {
                        inv_calc.setFicSurcharge_per(SURCHARGE_2_PER);
                        inv_calc.setFicSurcharge_rate((float) EITLERPGLOBAL.round((WT_RATE * SURCHARGE_2_PER) / 100, 0));
                    }
                }
            }

            inv_calc.setFicGrossRate(SQM_CHRG + WT_RATE + inv_calc.getFicSurcharge_rate());

//////////////////////////////////
            inv_calc.setFicRate(SQM_CHRG + WT_RATE);

            float base_amt = 0;
            float chem_trt = 0;
            switch (SQM_IND) {
                case "1":
//                base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() * inv_calc.getFicRate();
//                chem_trt = inv_calc.getFicLength() * inv_calc.getFicWidth();
                    base_amt = inv_calc.getFicSqMtr() * inv_calc.getFicGrossRate();
                    chem_trt = inv_calc.getFicSqMtr();
                    break;
                case "0":
                    base_amt = Float.parseFloat(EITLERPGLOBAL.round((inv_calc.getFicWeight() * inv_calc.getFicGrossRate()), 2) + "");
                    chem_trt = inv_calc.getFicWeight();
                    break;
            }
            inv_calc.setFicBasAmount(Float.parseFloat(EITLERPGLOBAL.round(base_amt, 2) + ""));

            inv_calc.setFicChemTrtChg(0);
            switch (CHEM_TRT_IND) {
                case "1":
                    inv_calc.setFicChemTrtChg(chem_trt * CHEM_TRT_CHRG);
                    break;
                case "0":
                    inv_calc.setFicChemTrtChg(0);
                    break;
            }

            inv_calc.setFicPinChg(0);
            switch (PIN_IND) {
                case "1":
                    inv_calc.setFicPinChg(inv_calc.getFicWidth() * PIN_CHRG);
                    break;
                case "0":
                    inv_calc.setFicPinChg(0);
                    break;
            }

            inv_calc.setFicSpiralChg(0);
            switch (SPR_IND) {
                case "1":
                    inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * SPR_CHRG);
                    break;
                case "0":
                    inv_calc.setFicSpiralChg(0);
                    break;
            }

            inv_calc.setFicSeamChg(inv_calc.getFicPinChg() + inv_calc.getFicSpiralChg());

            // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
            //  FeltRateDiscMasterDetail rate_discount;
            inv_calc.setFicDiscPer(0);
            inv_calc.setFicDiscAmt(0);
            inv_calc.setFicSeamPer(0);
            inv_calc.setFicSeamAmt(0);
            
            inv_calc.setFicSeamChg(inv_calc.getFicSeamChg() - inv_calc.getFicSeamAmt());

            float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
            inv_calc.setFicDiscBasamt(disc_basamt);

            inv_calc.setAosd_per(0);
            inv_calc.setAosd_amt(0);

            float ins_amt = 0;
            //inv_calc.setFicInsInd(0);
            String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            //String strIns = "SELECT INSURANCE_CODE AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
            int ins_ind = data.getIntValueFromDB(strIns);

            if (ins_ind == 1) {
                //inv_calc.setFicInsInd(1);
//                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() - inv_calc.getAosd_amt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg()) * (1)) * 1.1) + "");//0.10 * 0.0039) + "");
                ins_amt = (int) (ins_amt + 999) / 1000;
                ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
            } else {
                ins_amt = 0;
            }
            
            inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt, 2) + ""));

//            float inv_amt = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();
            float inv_amt = inv_calc.getFicDiscBasamt() - inv_calc.getAosd_amt() + inv_calc.getFicSeamChg() + inv_calc.getFicChemTrtChg() + inv_calc.getFicInsAmt();

            float IGST = 0;
            float CGST = 0;
            float SGST = 0;
            inv_calc.setFicIGSTPER(0);
            inv_calc.setFicCGSTPER(0);
            inv_calc.setFicSGSTPER(0);

            double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

            float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100), 2) + "");

//        String state_gst_id = data.getStringValueFromDB("SELECT SUBSTRING(GSTIN_NO,1,2) AS GSTIN_NO FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");
//        if (gstinNo.equalsIgnoreCase("")) {
//            inv_calc.setFicCGSTPER(Float.valueOf(0));
//            inv_calc.setFicSGSTPER(Float.valueOf(0));
//            inv_calc.setFicIGSTPER(Float.valueOf(0));
//            CGST = Float.valueOf(0);
//            SGST = Float.valueOf(0);
//            IGST = Float.valueOf(0);
//        } else {
            if (gstinNo.equalsIgnoreCase("24")) {
                inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER / 2) + ""));
                CGST = Float.valueOf(GST / 2);
                SGST = Float.valueOf(GST / 2);
            } else {
                inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
                IGST = GST;
            }
//        }

            GST = Math.round(GST);
            CGST = Math.round(CGST);
            SGST = Math.round(SGST);
            IGST = Math.round(IGST);

            inv_calc.setFicGST(GST);
            inv_calc.setFicIGST(IGST);
            inv_calc.setFicCGST(CGST);
            inv_calc.setFicSGST(SGST);

            inv_calc.setSD(0);
            
            float tcsAmt = 0;
            inv_calc.setTCS_per(0);
            inv_calc.setTCS_amt(tcsAmt);

/* Closed as per instruction on 01/07/2021                        
            //Added on 15 Sep 2020
            if (data.IsRecordExist("SELECT * FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='" + Party_Code + "' AND COALESCE(TCS_ELIGIBILITY,0)=1")) {
                try {
//                    inv_calc.setTCS_per(Float.parseFloat(0.075+""));
                    inv_calc.setTCS_per(Float.parseFloat(0.100+""));
                    tcsAmt = Math.round(((inv_amt + IGST + CGST + SGST) * inv_calc.getTCS_per()) / 100);
                    inv_calc.setTCS_amt(tcsAmt);
                } catch (Exception e) {
                    inv_calc.setTCS_per(0);
                    inv_calc.setTCS_amt(0);
                    System.out.println("Error on TCS Discount: " + e.getMessage());
                }
            }
*/        
            
            inv_amt = inv_amt + IGST + CGST + SGST + inv_calc.getTCS_amt();
            
//            inv_amt = inv_amt + IGST + CGST + SGST;

            inv_calc.setFicInvAmt(inv_amt);

        }

        return inv_calc;

    }

}
