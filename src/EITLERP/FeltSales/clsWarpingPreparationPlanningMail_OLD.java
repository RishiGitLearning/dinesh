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
public class clsWarpingPreparationPlanningMail_OLD {

    public static EITLERP.FeltSales.Reports.clsExcelExporter exprt = new EITLERP.FeltSales.Reports.clsExcelExporter();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SendMail();
        //SendMail_WarpingStatus();
        //SendMail_WarpingPreparationACNE();
        //SendMail_WarpingPreparationMNE();
    }
    public static void SendMail_WarpingPreparationACNE()
    {
        try {
            
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT WP_ACNE_NO FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Warping Preparation of Weaving Looms (ACNE) for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com,daxesh@dineshmills.com";

                    String SQL = "SELECT H.WP_ACNE_NO, DATE_FORMAT(H.WP_ACNE_DATE,'%d/%m/%Y') AS WP_ACNE_DATE, PRODUCT_GROUP, LOOM_NO, DATE_FORMAT(D.WARP_PREP_STARTED_ACTUAL,'%d/%m/%Y') AS 'Warp Prep Started Actual', DATE_FORMAT(D.WARP_PREP_DATE_PLAN,'%d/%m/%Y') AS 'Warp Prep date as per plan ', DATE_FORMAT(D.TARGETTED_WARP_PREP_COMPLETE,'%d/%m/%Y') AS 'TARGETTED WARP PREP COMPLETE ', WARP_PREP_STATUS, ACTUAL_WARP_PREP_COMPLETION, WINDING_CARDED_YARN_ON_CONE AS 'PHASE I : Winding Carded Yarn on Cone', PRIMARY_DOUBLING AS 'PHASE II : Primary Doubling', PRIMARY_TWISTING AS 'Phase III: Primary Twisting', WINDING_PRIMARY_ON_CONE AS 'Phase IV: Winding Primary on Cone', STREAMING_OF_CONES AS 'Phase V : Steaming of Cones', REMARKS\n" +
                    " FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_HEADER H,PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_ACNE_DETAIL D WHERE H.WP_ACNE_NO=D.WP_ACNE_NO AND H.WP_ACNE_NO='"+Doc_No+"'";
                   
                    
                    exprt.fillData(SQL, new File("/Email_Attachment/WARPING_PREPARATION_WEAVING_LOOM_ACNE.xls"), "Warping_ACNE");
                    
                    //System.out.println("Path : "+this.getClass().getResourceAsStream()+"/LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls");
                    String pSubject = "Felt Warping Preparation of Weaving Looms ACNE";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    //String pcc = "sdmlerp@dineshmills.com";
                    String Path = "/Email_Attachment/WARPING_PREPARATION_WEAVING_LOOM_ACNE.xls";
                    String PFiles = "WARPING_PREPARATION_WEAVING_LOOM_ACNE.xls";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void SendMail_WarpingPreparationMNE()
    {
        try {
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT WP_MNE_NO FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Warping Preparation of Weaving Looms (MNE) for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com,daxesh@dineshmills.com";

                    String SQL = "SELECT H.WP_MNE_NO, DATE_FORMAT(H.WP_MNE_DATE,'%d/%m/%Y') AS WP_MNE_DATE, PRODUCT_GROUP, LOOM_NO, DATE_FORMAT(D.WARP_PREP_STARTED_ACTUAL,'%d/%m/%Y') AS WARP_PREP_STARTED_ACTUAL, DATE_FORMAT(D.WARP_PREP_DATE_AS_PLAN,'%d/%m/%Y') AS WARP_PREP_DATE_AS_PLAN, DATE_FORMAT(D.TARGETTED_WARP_PREP_COMPLETE,'%d/%m/%Y') AS TARGETTED_WARP_PREP_COMPLETE, DATE_FORMAT(D.ACTUAL_WARP_PREP_COMPLETION_DATE,'%d/%m/%Y') AS ACTUAL_WARP_PREP_COMPLETION_DATE, WARP_PREP_STATUS, PHASE_1_PRIMARY_DOUBLING_TWISTING_JMW, PHASE_2_SECONDARY_DOUBLING_TWISTING, PHASE_3_PRIMARY_TWISTING, PHASE_4_WINDING_PRIMARY_ON_CONE, PHASE_5_STREAMING_OF_CONES, REMARKS " +
                    " FROM  PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_HEADER H,PRODUCTION.WARPING_PREPARATION_WEAVING_LOOM_MNE_DETAIL D WHERE H.WP_MNE_NO=D.WP_MNE_NO AND H.WP_MNE_NO='"+Doc_No+"';";
                    
                    
                    
                    String pSubject = "Felt Warping Preparation of Weaving Looms MNE";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    //String pcc = "sdmlerp@dineshmills.com";
                    String Path = "/Email_Attachment/WARPING_PREPARATION_WEAVING_LOOM_MNE.xls";
                    String PFiles = "WARPING_PREPARATION_WEAVING_LOOM_MNE.xls";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void SendMail_WarpingStatus()
    {
        try {
            
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT WS_DOC_NO FROM  PRODUCTION.WARPING_STATUS_WEAVING_LOOM_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Felt Warping Status of Weaving Loom for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com,daxesh@dineshmills.com";

                    String SQL = "SELECT DATE_FORMAT(H.WS_DOC_DATE,'%d/%m/%Y') AS DOC_DATE, PRODUCT_GROUP, LOOM_NO, WARPING_STATUS, DATE_FORMAT(D.WARPING_COMPLETE_TARGET,'%d/%m/%Y') AS WARPING_COMPLETE_TARGET, DATE_FORMAT(WARPING_STARTED_ACTUAL,'%d/%m/%Y') AS WARPING_STARTED_ACTUAL, DATE_FORMAT(D.WARPING_DATE_AS_PLAN,'%d/%m/%Y') AS WARPING_DATE_AS_PLAN, DATE_FORMAT(D.COMPLETION_DATE_AS_ON_TODAYS_STATUS,'%d/%m/%Y') AS COMPLETION_DATE_AS_ON_TODAYS_STATUS, DATE_FORMAT(D.PHASE_1_CREELING,'%d/%m/%Y') AS PHASE_1_CREELING, PHASE_2_WARPING, PHASE_3_BEAMING, REMARKS FROM  PRODUCTION.WARPING_STATUS_WEAVING_LOOM_HEADER H,PRODUCTION.WARPING_STATUS_WEAVING_LOOM_DETAIL D WHERE H.WS_DOC_NO=D.WS_DOC_NO AND H.WS_DOC_NO='"+Doc_No+"'";
                    //System.out.println("SQL "+SQL);
        //            InputStream stream = this.getClass().getResourceAsStream(reportName);
        //            System.out.println("Get Path + "+this.getCodeBase().getHost());
        //            System.out.println("Get Class + "+this.getClass());
                    //String sub_path = System.getProperty("user.home")+"/Desktop";    
                    exprt.fillData(SQL, new File("/Email_Attachment/WARPING_STATUS_WEAVING_LOOM.xls"), "Warping_Status");
                    
                    //System.out.println("Path : "+this.getClass().getResourceAsStream()+"/LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls");
                    String pSubject = "Felt Warping Status of Weaving Loom";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    //String pcc = "sdmlerp@dineshmills.com";
                    String Path = "/Email_Attachment/WARPING_STATUS_WEAVING_LOOM.xls";
                    String PFiles = "WARPING_STATUS_WEAVING_LOOM.xls";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void SendMail()
    {
        try {
            
            String Doc_No ="";
            Doc_No = data.getStringValueFromDB("SELECT LPP_DOC_NO FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_HEADER WHERE APPROVED_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");
            //System.out.println("SELECT LPP_DOC_NO FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_HEADER WHERE LPP_DOC_DATE='"+EITLERPGLOBAL.getCurrentDateDB()+"'");    
            if(!Doc_No.equals(""))
            {
                String pMessage = "<html>  ";

                    pMessage = pMessage + "Dear Sir,<br><br> Loom wise beam fall - warping preparation planning entry for (" + data.getStringValueFromDB("SELECT DAYNAME('"+EITLERPGLOBAL.getCurrentDateDB()+"')") + ", '"+EITLERPGLOBAL.getCurrentDateDB()+"').<br><br>Please find attachment. ";
        
                    String recievers = "brdfltweave1@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com";
                    //String recievers = "rishineekhra@dineshmills.com,daxesh@dineshmills.com";

                    String SQL = "SELECT SR_NO,DATE_FORMAT(H.AS_ON_DATE,'%d/%m/%Y') AS AS_ON_DATE, PRODUCT_GROUP, LOOM_NO, NO_OF_PIECE, LOOM_PICKS, WARP_TO_BE_ALLOTED_MTS, WARP_TO_BE_ALLOTED_LOOM_PICKS, TOTAL_PICK_PENDING, DAY_PENDING_FOR_BEAM_FALL, DATE_FORMAT(EXPECTED_DATE_OF_BEAM_FALL,'%d/%m/%Y') AS EXPECTED_DATE_OF_BEAM_FALL, DAYS_REQUIRED_FOR_WARPING, DATE_FORMAT(EXPECTED_DATE_TO_START_WARPING,'%d/%m/%Y') AS EXPECTED_DATE_TO_START_WARPING, APPOX_WARP_QUANTITY_REQUIRED, DAYS_REQUIRED_TO_MAKE_THIS_WARP_QUANTITY, DATE_FORMAT(EXPECTED_DATE_TO_START_FOR_WARP_YARN,'%d/%m/%Y') AS EXPECTED_DATE_TO_START_FOR_WARP_YARN FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_HEADER H,PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_DETAIL D " +
                                "where H.LPP_DOC_NO='"+Doc_No+"' AND D.LPP_DOC_NO=H.LPP_DOC_NO";
                    //System.out.println("SQL "+SQL);
        //            InputStream stream = this.getClass().getResourceAsStream(reportName);
        //            System.out.println("Get Path + "+this.getCodeBase().getHost());
        //            System.out.println("Get Class + "+this.getClass());
                    //String sub_path = System.getProperty("user.home")+"/Desktop";    
                    exprt.fillData(SQL, new File("/Email_Attachment/LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls"), "Warping");
                    
                    //System.out.println("Path : "+this.getClass().getResourceAsStream()+"/LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls");
                    String pSubject = "Loom wise beam fall - warping preparation planning";
                    String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com,sdmlerp@dineshmills.com";
                    //String pcc = "";
                    String Path = "/Email_Attachment/LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls";
                    String PFiles = "LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls";

                    JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
