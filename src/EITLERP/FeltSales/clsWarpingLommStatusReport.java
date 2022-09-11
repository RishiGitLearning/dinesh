/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.io.File;

/**
 *
 * @author root
 */          //clsWarpingPreparationPlanningMail
public class clsWarpingLommStatusReport {

    public static EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            String mdate = data.getStringValueFromDB("SELECT DATE_FORMAT(CURDATE(),'%d_%m_%Y') FROM DUAL");

            String sql1, sql2, sql3, sql4, sql5, sql6;
            sql1 = "SELECT SR_NO,DATE_FORMAT(H.AS_ON_DATE,'%d/%m/%Y') AS AS_ON_DATE, PRODUCT_GROUP, LOOM_NO, NO_OF_PIECE, "
                    + "LOOM_PICKS, WARP_TO_BE_ALLOTED_MTS, WARP_TO_BE_ALLOTED_LOOM_PICKS, TOTAL_PICK_PENDING, "
                    + "DAY_PENDING_FOR_BEAM_FALL, "
                    + "CASE WHEN EXPECTED_DATE_OF_BEAM_FALL='0000-00-00' THEN '' ELSE DATE_FORMAT(EXPECTED_DATE_OF_BEAM_FALL,'%d/%m%Y') END  AS EXPECTED_DATE_OF_BEAM_FALL, "
                    + "DAYS_REQUIRED_FOR_WARPING, "
                    + "CASE WHEN EXPECTED_DATE_TO_START_WARPING='0000-00-00' THEN '' ELSE DATE_FORMAT(EXPECTED_DATE_TO_START_WARPING,'%d/%m%Y') END AS EXPECTED_DATE_TO_START_WARPING, "
                    + "APPOX_WARP_QUANTITY_REQUIRED, DAYS_REQUIRED_TO_MAKE_THIS_WARP_QUANTITY, "
                    + "CASE WHEN EXPECTED_DATE_TO_START_FOR_WARP_YARN='0000-00-00' THEN '' ELSE DATE_FORMAT(EXPECTED_DATE_TO_START_FOR_WARP_YARN,'%d/%m%Y') END AS EXPECTED_DATE_TO_START_FOR_WARP_YARN "
                    + "FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_HEADER H,"
                    + "PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_DETAIL D "
                    + "WHERE  D.LPP_DOC_NO=H.LPP_DOC_NO "
                    + "AND H.APPROVED_DATE=CURDATE() AND H.APPROVED=1 "
                    + "ORDER BY H.APPROVED_DATE DESC,SR_NO";
            sql2 = "SELECT DATE_FORMAT(H.WS_DOC_DATE,'%d/%m/%Y') AS DOC_DATE, PRODUCT_GROUP, LOOM_NO, WARPING_STATUS, "
                    + "CASE WHEN D.WARPING_COMPLETE_TARGET='0000-00-00' THEN '' ELSE DATE_FORMAT(D.WARPING_COMPLETE_TARGET,'%d/%m/%Y') END AS WARPING_COMPLETE_TARGET, "
                    + "CASE WHEN D.WARPING_STARTED_ACTUAL='0000-00-00' THEN '' ELSE DATE_FORMAT(WARPING_STARTED_ACTUAL,'%d/%m/%Y') END AS WARPING_STARTED_ACTUAL, "
                    + "CASE WHEN D.WARPING_DATE_AS_PLAN='0000-00-00' THEN '' ELSE DATE_FORMAT(D.WARPING_DATE_AS_PLAN,'%d/%m/%Y') END AS WARPING_DATE_AS_PLAN, "
                    + "CASE WHEN D.COMPLETION_DATE_AS_ON_TODAYS_STATUS='0000-00-00' THEN '' ELSE DATE_FORMAT(D.COMPLETION_DATE_AS_ON_TODAYS_STATUS,'%d/%m/%Y') END AS COMPLETION_DATE_AS_ON_TODAYS_STATUS, "
                    + "CASE WHEN D.PHASE_1_CREELING='0000-00-00' THEN '' ELSE DATE_FORMAT(D.PHASE_1_CREELING,'%d/%m/%Y') END AS PHASE_1_CREELING, PHASE_2_WARPING, PHASE_3_BEAMING, REMARKS "
                    + "FROM  PRODUCTION.WARPING_STATUS_WEAVING_LOOM_HEADER H,PRODUCTION.WARPING_STATUS_WEAVING_LOOM_DETAIL D "
                    + "WHERE H.WS_DOC_NO=D.WS_DOC_NO AND H.APPROVED_DATE=CURDATE() AND H.APPROVED=1 "
                    + "ORDER BY H.APPROVED_DATE DESC,SR_NO";
            sql3 = "SELECT H.WP_ACNE_NO, DATE_FORMAT(H.WP_ACNE_DATE,'%d/%m/%Y') AS WP_ACNE_DATE, PRODUCT_GROUP, LOOM_NO, "
                    + "CASE WHEN D.WARP_PREP_STARTED_ACTUAL='0000-00-00' THEN '' ELSE DATE_FORMAT(D.WARP_PREP_STARTED_ACTUAL,'%d/%m/%Y') END AS 'Warp Prep Started Actual', "
                    + "CASE WHEN D.WARP_PREP_DATE_PLAN='0000-00-00' THEN '' ELSE DATE_FORMAT(D.WARP_PREP_DATE_PLAN,'%d/%m/%Y') END AS 'Warp Prep date as per plan ', "
                    + "CASE WHEN D.TARGETTED_WARP_PREP_COMPLETE='0000-00-00' THEN '' ELSE DATE_FORMAT(D.TARGETTED_WARP_PREP_COMPLETE,'%d/%m/%Y') END AS 'TARGETTED WARP PREP COMPLETE ', "
                    + "WARP_PREP_STATUS, ACTUAL_WARP_PREP_COMPLETION, WINDING_CARDED_YARN_ON_CONE AS 'PHASE I : Winding Carded Yarn on Cone', "
                    + "PRIMARY_DOUBLING AS 'PHASE II : Primary Doubling', PRIMARY_TWISTING AS 'Phase III: Primary Twisting', "
                    + "WINDING_PRIMARY_ON_CONE AS 'Phase IV: Winding Primary on Cone', STREAMING_OF_CONES AS 'Phase V : Steaming of Cones', REMARKS "
                    + "FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER H,"
                    + "PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL D "
                    + "WHERE H.WP_ACNE_NO=D.WP_ACNE_NO AND H.APPROVED_DATE=CURDATE() AND H.APPROVED=1 "
                    + "ORDER BY H.APPROVED_DATE DESC,SR_NO";
            sql4 = "SELECT H.WP_MNE_NO, DATE_FORMAT(H.WP_MNE_DATE,'%d/%m/%Y') AS WP_MNE_DATE, PRODUCT_GROUP, LOOM_NO, "
                    + "CASE WHEN D.WARP_PREP_STARTED_ACTUAL='0000-00-00' THEN '' ELSE DATE_FORMAT(D.WARP_PREP_STARTED_ACTUAL,'%d/%m/%Y') END AS WARP_PREP_STARTED_ACTUAL, "
                    + "CASE WHEN D.WARP_PREP_DATE_AS_PLAN='0000-00-00' THEN '' ELSE DATE_FORMAT(D.WARP_PREP_DATE_AS_PLAN,'%d/%m/%Y') END AS WARP_PREP_DATE_AS_PLAN, "
                    + "CASE WHEN D.TARGETTED_WARP_PREP_COMPLETE='0000-00-00' THEN '' ELSE DATE_FORMAT(D.TARGETTED_WARP_PREP_COMPLETE,'%d/%m/%Y') END AS TARGETTED_WARP_PREP_COMPLETE, "
                    + "CASE WHEN D.ACTUAL_WARP_PREP_COMPLETION_DATE='0000-00-00' THEN '' ELSE DATE_FORMAT(D.ACTUAL_WARP_PREP_COMPLETION_DATE,'%d/%m/%Y') END AS ACTUAL_WARP_PREP_COMPLETION_DATE, "
                    + "WARP_PREP_STATUS, PHASE_1_PRIMARY_DOUBLING_TWISTING_JMW, PHASE_2_SECONDARY_DOUBLING_TWISTING, "
                    + "PHASE_3_PRIMARY_TWISTING, PHASE_4_WINDING_PRIMARY_ON_CONE, PHASE_5_STREAMING_OF_CONES, REMARKS "
                    + "FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_HEADER H,"
                    + "PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_DETAIL D "
                    + "WHERE H.WP_MNE_NO=D.WP_MNE_NO AND H.APPROVED_DATE=CURDATE() AND H.APPROVED=1 "
                    + "ORDER BY H.APPROVED_DATE DESC,SR_NO";
            sql5 = "SELECT WINDING_MACHINE_NAME, ACTUAL_TARGET, SPINDLES, SHIFT_A_COUNTER, SHIFT_A_DOFF, SHIFT_A_KGS,"
                    + "SHIFT_B_COUNTER, SHIFT_B_DOFF, SHIFT_B_KGS, SHIFT_C_COUNTER, SHIFT_C_DOFF, SHIFT_C_KGS,"
                    + "DAYS_PRODUCTION_COUNTER, DAYS_PRODUCTION_DOFF, DAYS_PRODUCTION_KGS "
                    + "FROM PRODUCTION.WINDING_MACHINES_DETAIL D,PRODUCTION.WINDING_MACHINES_HEADER H "
                    + "WHERE H.WM_DOC_NO=D.WM_DOC_NO AND H.APPROVED_DATE=CURDATE() AND H.APPROVED=1 "
                    + "ORDER BY H.APPROVED_DATE DESC,SR_NO ";
            sql6 = " SELECT DATE_FORMAT(D.BGS_DOC_DATE,'%d/%m/%Y') AS BEAM_GEITING_DATE, PRODUCT_GROUP, LOOM_NO, "
                    + " GAITING_STATUS, CASE WHEN BEAM_GETTING_STARTED_ACTUAL='0000-00-00' THEN '' ELSE DATE_FORMAT(BEAM_GETTING_STARTED_ACTUAL,'%d/%m/%Y') END AS BEAM_GETTING_STARTED_ACTUAL,"
                    + " CASE WHEN BEAM_GETTING_DATE_AS_PER_PLAN='0000-00-00' THEN '' ELSE DATE_FORMAT(BEAM_GETTING_DATE_AS_PER_PLAN,'%d/%m%Y') END AS BEAM_GETTING_DATE_AS_PER_PLAN,"
                    + " CASE WHEN TARGETTED_BEAM_GAITING_COMPLETE='0000-00-00' THEN '' ELSE DATE_FORMAT(TARGETTED_BEAM_GAITING_COMPLETE,'%d/%m%Y') END AS TARGETTED_BEAM_GAITING_COMPLETE,"
                    + " CASE WHEN ACTUAL_BEAM_GAITING_COMPLETION_DATE='0000-00-00' THEN '' ELSE DATE_FORMAT(ACTUAL_BEAM_GAITING_COMPLETION_DATE,'%d/%m%Y') END AS ACTUAL_BEAM_GAITING_COMPLETION_DATE,"
                    + " CASE WHEN PI_BEAM_LOADING='0000-00-00' THEN '' ELSE DATE_FORMAT(PI_BEAM_LOADING,'%d/%m%Y') END AS PI_BEAM_LOADING,"
                    + " CASE WHEN PII_KNOTTING_DRAWING='0000-00-00' THEN '' ELSE DATE_FORMAT(PII_KNOTTING_DRAWING,'%d/%m%Y') END AS PII_KNOTTING_DRAWING,"
                    + " CASE WHEN PIII_FRWD_KONTS='0000-00-00' THEN '' ELSE DATE_FORMAT(PIII_FRWD_KONTS,'%d/%m%Y') END AS PIII_FRWD_KONTS,"
                    + " CASE WHEN PIV_CLOTH_START='0000-00-00' THEN '' ELSE DATE_FORMAT(PIV_CLOTH_START,'%d/%m%Y') END AS PIV_CLOTH_START,"
                    + "      REMARKS "
                    + " FROM PRODUCTION.BEAM_GAITING_STATUS_DETAIL D,PRODUCTION.BEAM_GAITING_STATUS_HEADER H "
                    + " WHERE D.BGS_DOC_NO=H.BGS_DOC_NO AND H.APPROVED=1 AND H.APPROVED_DATE=CURDATE() "
                    + " ORDER BY H.APPROVED_DATE";
            System.out.println(sql1);
            System.out.println(sql2);
            System.out.println(sql3);
            System.out.println(sql4);
            System.out.println(sql5);
            System.out.println(sql6);
            exprt.fillData(sql5 + "#" + sql4 + "#" + sql3 + "#" + sql2 + "#" + sql6 + "#" + sql1, new File("/Email_Attachment/Weaving_Loom_Status_Report_" + mdate + ".xls"), "Winding#Warp_Preparation_MNE#Warp_Preparation_ACNE#Warping_Status#Beam_Gaiting_Status#BeamfallWarpPreparationPlanning");
            String pMessage = "<html>  ";

            pMessage = pMessage + "Dear Sir,<br><br> Felt Weaving Loom Status Report for ( " + mdate + " ) .<br><br>Please find attachment. ";

            String recievers = "brdfltweave1@dineshmills.com";
            //String recievers = "dharmendra@dineshmills.com";
            String pSubject = "Felt Weaving Loom Status Report for " + mdate;
            String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
            //String pcc = "sdmlerp@dineshmills.com";
            String Path = "/Email_Attachment/Weaving_Loom_Status_Report_" + mdate + ".xls";
            String PFiles = "Weaving_Loom_Status_Report_" + mdate + ".xls";

           //Send Mail Excel is closed on 19-01-2021, 
           //From 19-01-2021 clsWarpingPreparationPlanningMail is live. 
           // JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
