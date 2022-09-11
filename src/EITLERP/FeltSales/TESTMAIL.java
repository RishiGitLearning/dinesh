/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales;

import EITLERP.EITLERPGLOBAL;
import EITLERP.FeltSales.Reports.clsExcelExporter;
import EITLERP.FeltSales.common.JavaMail;
import EITLERP.data;
import java.io.File;

/**
 *
 * @author root
 */
public class TESTMAIL {

    /**
     * @param args the command line arguments
     */
    private static clsExcelExporter exprt = new clsExcelExporter();
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            String pMessage = "<html>  ";

            pMessage = pMessage + "Dear Sir,<br><br> Loom wise beam fall - warping praparation planning entry final approved for (" + data.getStringValueFromDB("SELECT DAYNAME('" + EITLERPGLOBAL.getCurrentDateDB() + "')") + ", " + EITLERPGLOBAL.getCurrentDateDB() + ").<br><br>Please find attachment. ";

            System.out.println("MESAGE : " + pMessage);
            //String recievers = "brdfltweave1@dineshmills.com";
            String recievers = "dharmendra@dineshmills.com";

            String SQL = "SELECT SR_NO, PRODUCT_GROUP, LOOM_NO, NO_OF_PIECE, LOOM_PICKS, WARP_TO_BE_ALLOTED_MTS, WARP_TO_BE_ALLOTED_LOOM_PICKS, TOTAL_PICK_PENDING, DAY_PENDING_FOR_BEAM_FALL, EXPECTED_DATE_OF_BEAM_FALL, DAYS_REQUIRED_FOR_WARPING, EXPECTED_DATE_TO_START_WARPING, APPOX_WARP_QUANTITY_REQUIRED, DAYS_REQUIRED_TO_MAKE_THIS_WARP_QUANTITY, EXPECTED_DATE_TO_START_FOR_WARP_YARN FROM PRODUCTION.LOOMWISE_BEAM_FALL_WARP_PREPARATION_PLANNING_DETAIL where LPP_DOC_NO='LPP202100003'";
            exprt.fillData(SQL, new File("/Email_Attachment/LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls"), "LOOMWISE_BEAM_FALL_WARPING_PLANNING");

            String pSubject = "Loom wise beam fall - warping praparation planning ";
            //String pcc = "aditya@dineshmills.com,abtewary@dineshmills.com,yrpatel@dineshmills.com,amitkanti@dineshmills.com,raghavendra@dineshmills.com";
            String pcc = "";
            String Path = "/Email_Attachment/LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls";
            String PFiles = "LOOMWISE_BEAM_FALL_WARPING_PLANNING.xls";

            JavaMail.SendMailwithAttachment(recievers, pMessage, pSubject, pcc, Path, PFiles);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
