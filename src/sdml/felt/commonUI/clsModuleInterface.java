/*
 * clsModuleInterface.java
 *
 * Created on December 10, 2004, 4:33 PM
 */
package sdml.felt.commonUI;

/**
 *
 * @author 
 */
/*import RS.Stores.*;
 import RS.Purchase.*;
 import RS.Finance.*;
 import RS.Sales.*;
 */
public class clsModuleInterface {

    public static String cancelMainCode = "";

    /**
     * Creates a new instance of clsModuleInterface
     */
    public clsModuleInterface() {
    }

    public static void CancelDocument(int pCompanyID, int pModuleID, String pDocNo) {

        //Bhavesh
        /*if(pModuleID==104) {
         clsDepositRefund.CancelDoc(pCompanyID, pDocNo);
         }

         //
         if(pModuleID==clsUpdationof09.ModuleID) {
         clsUpdationof09.CancelDoc(pCompanyID, pDocNo);
         }
        
         //
        
         if(pModuleID==clsOBCInvoice.ModuleID) {
         clsOBCInvoice.CancelDoc(pCompanyID, pDocNo);
         }
        
         if(pModuleID==clsOBC.ModuleID) {
         clsOBC.CancelDoc(pCompanyID, pDocNo);
         }
        
         //       if(pModuleID==clsSalesInvoice.ModuleID) {
         //            clsSalesInvoice.CancelDoc(pCompanyID, pDocNo);
         //        }
        
         */
    }

    public static boolean CanCancelDocument(int pCompanyID, int pModuleID, String pDocNo) {
        boolean CanCancel = false;
        /* 
         //Bhavesh
         if(pModuleID==clsExpense.ModuleID) {
         CanCancel=clsExpense.CanCancel(pCompanyID, pDocNo);
         }
         if(pModuleID==104) {
         CanCancel=clsDepositRefund.CanCancel(pCompanyID, pDocNo);
         }

         //BHAVESH
        
         if(pModuleID==clsUpdationof09.ModuleID) {
         CanCancel=clsUpdationof09.CanCancel(pCompanyID, pDocNo);
         }
        
         if(pModuleID==clsOBCInvoice.ModuleID) {
         CanCancel=clsOBCInvoice.CanCancel(pCompanyID, pDocNo);
         }
        
         if(pModuleID==clsOBC.ModuleID) {
         CanCancel=clsOBC.CanCancel(pCompanyID, pDocNo);
         }
         */
        return CanCancel;
    }

    public static void OpenDocument(int pCompanyID, int pModuleID, String pDocNo, int MainCode) {
        String DocNo = pDocNo;
        int SelModule = pModuleID;
        String MCode = Integer.toString(MainCode);
        /*if(pModuleID==clsOBC.ModuleID) {
         AppletFrame aFrame=new AppletFrame("OBC");
         aFrame.startAppletEx("RS.Finance.frmOBC","OBC");
         frmOBC ObjItem=(frmOBC) aFrame.ObjApplet;
         ObjItem.FindEx((int)SDMLERPGLOBAL.gCompanyID,pDocNo);
         }*/

    }

}
