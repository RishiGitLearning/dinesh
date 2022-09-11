/*
 * clsModuleInterface.java
 *
 * Created on December 10, 2004, 4:33 PM
 */
package EITLERP;

/**
 *
 * @author root
 */
import EITLERP.FeltSales.FeltEvaluation.*;
import EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.clsFeltGSTAdvancePaymentEntryForm;
import EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm;
import EITLERP.FeltSales.FeltPacking.clsFeltPacking;
import EITLERP.FeltSales.FeltPacking.frmFeltPacking;
import EITLERP.FeltSales.Order.FrmFeltOrder;
import EITLERP.FeltSales.Order.clsFeltOrder;
import EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion;
import EITLERP.FeltSales.OrderDiversion.clsFeltOrderDiversion;
import EITLERP.Stores.*;
import EITLERP.Purchase.*;
import EITLERP.Finance.*;
import EITLERP.Production.FeltDiscRateMaster.clsDiscRateMaster;
import EITLERP.Production.FeltDiscRateMaster.frmDiscRateMaster;
import EITLERP.Sales.*;
import EITLERP.FeltSales.FeltFinishing.clsFeltFinishing;
import EITLERP.FeltSales.FeltFinishing.frmFeltFinishing;
import EITLERP.Production.FeltMachineSurveyAmend.clsmachinesurveyAmend;
import EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend;
import EITLERP.FeltSales.GroupMasterAmend.clsFeltGroupMasterAmend;
import EITLERP.FeltSales.GroupMasterAmend.frmFelGroupMasterAmend;
import EITLERP.FeltSales.PartyMachineClosure.*;
import EITLERP.FeltSales.PartyMachineReOpen.*;
import EITLERP.FeltSales.PieceAmendmentApproval.clsPieceAmendApproval;
import EITLERP.FeltSales.PieceAmendmentApproval.frmPieceAmendApproval;
import EITLERP.FeltSales.PieceAmendmentApproval_STOCK.clsPieceAmendApproval_STOCK;
import EITLERP.FeltSales.PieceAmendmentApproval_STOCK.frmPieceAmendApproval_STOCK;
import EITLERP.FeltSales.PieceAmendmentWIP.clsPieceAmendWIP;
import EITLERP.FeltSales.PieceAmendmentWIP.frmPieceAmendWIP;
import EITLERP.FeltSales.ReopenBale12.clsFeltReopenBale;
import EITLERP.FeltSales.ReopenBale12.frmFeltReopenBale;
import EITLERP.Production.FeltUnadj.clsFeltUnadj;
import EITLERP.Production.FeltUnadj.frmFeltUnadj;
import EITLERP.Sales.DebitMemoReceiptMapping.clsDebitMemoReceiptMapping;
import EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping;
import EITLERP.Sales.DebitMemoReceiptMapping.*;
import EITLERP.FeltSales.FeltPieceAmend.*;
import EITLERP.FeltSales.Budget.*;
import EITLERP.FeltSales.DiversionList.*;
import EITLERP.FeltSales.DiversionLoss.FrmFeltDiversionLoss;
import EITLERP.FeltSales.DiversionLoss.clsFeltOrderDiversionLoss;
import EITLERP.FeltSales.FeltLRUpdation.*;
import EITLERP.FeltSales.FeltPDC.clsFeltPDC;
import EITLERP.FeltSales.FeltPDC.frmFeltPDC;
import EITLERP.FeltSales.FeltPDCAmend.clsFeltPDCAmend;
import EITLERP.FeltSales.FeltPDCAmend.frmFeltPDCAmend;
import EITLERP.FeltSales.FeltPartyContact.clsFeltPartyContact;
import EITLERP.FeltSales.FeltPartyContact.frmFeltPartyContact;
import EITLERP.FeltSales.FeltTransporterWeight.*;
import EITLERP.Production.FeltCreditNote.clsFeltCreditNote;
import EITLERP.FeltSales.GIDC_SDF.Despatch.clsDespatchGIDC_SDML;
import EITLERP.FeltSales.GIDC_SDF.Despatch.frmDespatchGIDC_SDML;
import EITLERP.FeltSales.GIDC_SDF.ProductionProcess.FrmProductionEntry;
import EITLERP.FeltSales.GIDC_SDF.ProductionProcess.clsProductionEntry;
import EITLERP.FeltSales.GIDC_SDF.Instruction.*;
//import EITLERP.FeltSales.SDF.Instruction.*;
import EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_GIDC;
import EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_SDML;
import EITLERP.FeltSales.GIDC_SDF.NRGP.clsNRGP_GIDC;
import EITLERP.FeltSales.GIDC_SDF.NRGP.clsNRGP_SDML;
import EITLERP.FeltSales.AutoPI.clsFeltAutoPISelection;
import EITLERP.FeltSales.AutoPI.frmFeltAutoPISelection;
import EITLERP.FeltSales.FeltInvCvrLtr.clsFeltInvCvrLtr;
import EITLERP.FeltSales.FeltInvCvrLtr.frmFeltInvCvrLtr;
import EITLERP.FeltSales.GroupCriticalLimitEnhancement.clsFeltGroupCriticalLimitEnhancement;
import EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement;
import EITLERP.FeltSales.PieceDivision.FrmPieceDivision;
import EITLERP.FeltSales.PieceDivision.clsPieceDivision;
import EITLERP.FeltSales.RateEligibility.clsFeltRateEligibility;
import EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility;
import EITLERP.FeltWH.clsPostInvoiceEntry;
import EITLERP.FeltWH.clsWHInvGatepassEntry;
import EITLERP.FeltWH.frmPostInvoiceEntry;
import EITLERP.FeltWH.frmWHInvGatepassEntry;
import EITLERP.Production.FeltHeatSetting.frmFeltHeatSetting;
import EITLERP.Production.FeltMachineSurvey.clsmachinesurvey;
import EITLERP.Production.FeltMachineSurvey.frmmachinesurvey;
import SDMLATTPAY.gatepass.FrmTimeCorrectionEntry;
import SDMLATTPAY.gatepass.clsSpecialDeductionEntry;

public class clsModuleInterface {

    public static String cancelMainCode = "";

    /**
     * Creates a new instance of clsModuleInterface
     */
    public clsModuleInterface() {
    }

    public static void CancelDocument(int pCompanyID, int pModuleID, String pDocNo) {
        
        
        if (pModuleID == 878) {
            EITLERP.FeltSales.PPRSPlanning.clsPPRSEntry.CancelDoc(pDocNo);
        }

        if (pModuleID == 876) {
            EITLERP.FeltSales.ZoneMaster.clsZoneMaster.CancelDoc(pDocNo);
        }

        if (pModuleID == 877) {
            EITLERP.FeltSales.ZoneMaster.clsZoneMasterPartySelection.CancelDoc(pDocNo);
        }

        if (pModuleID == 662) {
            EITLERP.FeltSales.ObsoleteScrap.clsFeltObsoleteScrap.CancelDoc(pDocNo);
        }

        if (pModuleID == 873) {
            SDMLATTPAY.GatepassSelection.clsGatepassSelection.CancelDoc(pDocNo);
        }

        if (pModuleID == 860) {
            SDMLATTPAY.TravelAdvance.clsTravelVoucher.CancelDoc(pDocNo);
        }
        if (pModuleID == 859) {
            SDMLATTPAY.TravelAdvance.clsTravelEntryAmend.CancelDoc(pDocNo);            
        }
        if (pModuleID == 858) {
            SDMLATTPAY.TravelAdvance.clsTravelEntry.CancelDoc(pDocNo);            
        }
        if (pModuleID == 865) {
            SDMLATTPAY.ManpowerRequisition.clsManpowerRequisitionForm.CancelDoc(pDocNo);
        }

        if (pModuleID == 862) {
            SDMLATTPAY.RokdiSelection.clsRokdiSelection.CancelDoc(pDocNo);
        }

        if (pModuleID == 744) ///Felt Sales/Goods Return
        {
            EITLERP.FeltSales.SalesReturns.clsFeltSalesReturns.CancelDoc(pDocNo);
        }

        if (pModuleID == 635) {
            EITLERP.FeltSales.PostAuditDiscRateMaster.clsPostAuditDiscRateMaster.CancelDoc(pDocNo);
        }

        if (pModuleID == 769) {
            clsFeltGroupCriticalLimitEnhancement.CancelDoc(pDocNo);
        }

        if (pModuleID == 634) {
            clsFeltPartyContact.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 615) {
            clsFeltInvCvrLtr.CancelDoc(pDocNo);
        }

        if (pModuleID == 805) {
            SDMLATTPAY.gatepass.clsGatepass.CancelDoc(pDocNo);
        }

        if (pModuleID == 819) {
            SDMLATTPAY.gatepass.clsSpecialSanctionEntry.CancelDoc(pDocNo);
        }

        if (pModuleID == 810) {
            SDMLATTPAY.gatepass.clsTimeCorrectionEntry.CancelDoc(pDocNo);
        }

        if (pModuleID == 813) {
            SDMLATTPAY.leave.clsLeaveAmend.CancelDoc(pDocNo);
        }

        if (pModuleID == 811) {
            SDMLATTPAY.leave.clsLeaveApplication.CancelDoc(pDocNo);
        }

        if (pModuleID == 610) //Felt inv doc cancel request
        {
            EITLERP.FeltSales.FeltInvReport.clsDocCancel.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 632) {
            clsFeltPieceAmendmentPO.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 802) {
            clsPostInvoiceEntry.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 803) //NRGP
        {
            clsWHInvGatepassEntry.CancelNRGP(pCompanyID, pDocNo);
        }

        if (pModuleID == 630) {
            clsFeltAutoPISelection.CancelDoc(pDocNo);
        }

        if (pModuleID == 631) {
            clsFeltRateEligibility.CancelDoc(pDocNo);
        }

        if (pModuleID == 112) {
            //clsIssueRMG.CancelDoc(pCompanyID, pDocNo);
            clsIssueRMG.CancelIssue(pCompanyID, pDocNo);
        }

        if (pModuleID == 800) {
            //clsIssueStoresales.CancelDoc(pCompanyID, pDocNo);
            clsIssueStoresales.CancelIssue(pCompanyID, pDocNo);
        }

        if (pModuleID == 794) {
            clsNRGP_SDML.CancelNRGP(pCompanyID, pDocNo);
        }

//        if (pModuleID == 793) {
//            clsNRGP_GIDC.CancelNRGP(pCompanyID, pDocNo);
//        }
        if (pModuleID == clsGIDCInstruction.ModuleID) {
            clsGIDCInstruction.CancelDoc(pDocNo);
        }
        if (pModuleID == clsGIDCInstructionAmend.ModuleID) {
            clsGIDCInstructionAmend.CancelDoc(pDocNo);
        }
        if (pModuleID == 792) {
            clsDespatchGIDC_SDML.CancelDoc(pDocNo);
        }

//        if (pModuleID == 626) {
//            clsFeltPDCAmend.CancelDoc(pCompanyID, pDocNo);
//        }
        if (pModuleID == 625) {
            clsFeltPDC.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 742) {
            clsDiversionList.CancelDoc(pDocNo);
        }

        if (pModuleID == 735) {
            clsFeltCreditNote.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 621) {
            clsFeltPieceAmendmentWIP.CancelDoc(pCompanyID, pDocNo);
        }
        if (pModuleID == 622) {
            clsFeltPieceAmendmentSTOCK.CancelDoc(pCompanyID, pDocNo);
        }
        if (pModuleID == 623) {
            clsFeltPieceAmendmentCHR.CancelDoc(pCompanyID, pDocNo);
        }
        if (pModuleID == 624) {
            clsFeltPieceAmendmentEXPORT.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 612) {
            clsFeltEvaluationReOpen.CancelDoc(pDocNo);
        }

        if (pModuleID == 611) {
            clsFeltEvaluationClosure.CancelDoc(pDocNo);
        }

        if (pModuleID == 614) {
            clsFeltLRUpdation.CancelDoc(pDocNo);
        }

        if (pModuleID == clsPartyMachineReOpen.ModuleID) {
            clsPartyMachineReOpen.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == clsPartyMachineClosure.ModuleID) {
            clsPartyMachineClosure.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 100) // Debit Memo Cancellation
        {
            clsDMwithoutDNCancellation.CancelDoc(pCompanyID, pDocNo);
        }

        //Bhavesh
        if (pModuleID == 104) {
            clsDepositRefund.CancelDoc(pCompanyID, pDocNo);
        }

        //
        if (pModuleID == clsUpdationof09.ModuleID) {
            clsUpdationof09.CancelDoc(pCompanyID, pDocNo);
        }

        //
        if (pModuleID == clsOBCInvoice.ModuleID) {
            clsOBCInvoice.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == clsOBC.ModuleID) {
            clsOBC.CancelDoc(pCompanyID, pDocNo);
        }

//       if(pModuleID==clsSalesInvoice.ModuleID) {
//            clsSalesInvoice.CancelDoc(pCompanyID, pDocNo);
//        }
        if (pModuleID == 63) {
            clsExpenseTransaction.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 64) {
            clsIssueRequisitionRaw.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == clsCrAdjustment.ModuleID) {
            clsCrAdjustment.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == clsDrAdjustment.ModuleID) {
            clsDrAdjustment.CancelDoc(pCompanyID, pDocNo);
        }
        if (pModuleID == 66 || pModuleID == 90 || pModuleID == 65 || pModuleID == 59 || pModuleID == 67 || pModuleID == 83 || pModuleID == 68 || pModuleID == 69 || pModuleID == 70 || pModuleID == 89 || pModuleID == 94 || pModuleID == 205) //Voucher
        {
            clsVoucher.CancelDoc(pCompanyID, pDocNo, pModuleID);
        }

        if (pModuleID == 54) //Party Master
        {
            clsPartyMaster.CancelDoc(pCompanyID, UtilFunctions.CInt(pDocNo));
        }

        if (pModuleID == 57) //General Ledger
        {
            clsGL.CancelDoc(pCompanyID, UtilFunctions.CInt(pDocNo));
        }

        if (pModuleID == 52) //Issue Requisition
        {
            clsIssueRequisition.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 4) //Gatepass Requisition
        {
            clsGPR.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 44) //doc cancel request
        {
            clsDocCancel.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 5) //MIR Gen.
        {
            clsMIRGen.CancelMIR(pCompanyID, pDocNo);
        }
        if (pModuleID == 35) //MIR Gen.
        {
            clsMIRService.CancelMIR(pCompanyID, pDocNo);
        }

        if (pModuleID == 6) //MIR Raw
        {
            clsMIRRaw.CancelMIR(pCompanyID, pDocNo);
        }

        if (pModuleID == 7) //GRN Gen.
        {
            clsGRNGen.CancelGRN(pCompanyID, pDocNo);
        }

        if (pModuleID == 8) //GRN Raw
        {
            clsGRN.CancelGRN(pCompanyID, pDocNo);
        }

        if (pModuleID == 9) //RJN Gen.
        {
            clsRJNGen.CancelRJN(pCompanyID, pDocNo);
        }

        if (pModuleID == 10) //RJN Raw
        {
            clsRJN.CancelRJN(pCompanyID, pDocNo);
        }

        if (pModuleID == 48) //Jobwork entry
        {
            clsJobwork.CancelJOB(pCompanyID, pDocNo);
        }

        if (pModuleID == 14) //Issue Gen
        {
            clsIssue.CancelIssue(pCompanyID, pDocNo);
        }

        if (pModuleID == 15) //Issue Raw
        {
            clsIssueRaw.CancelIssue(pCompanyID, pDocNo);
        }

        if (pModuleID == 11) //NRGP
        {
            clsNRGP.CancelNRGP(pCompanyID, pDocNo);
        }

        if (pModuleID == 12) //RGP
        {
            clsRGP.CancelRGP(pCompanyID, pDocNo);
        }

        if (pModuleID == 20) //Summary
        {
            clsQuotApproval.CancelSummary(pCompanyID, pDocNo);
        }

        if (pModuleID == 19) //Quotation
        {
            clsQuotation.CancelQuotation(pCompanyID, pDocNo);
        }

        if (pModuleID == 38) //Supplier Master
        {
            clsRateApproval.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 37) //Supplier Master
        {
            int SuppID = Integer.parseInt(pDocNo);
            clsSupplier.CancelSupplier(pCompanyID, SuppID);
        }

        if ((pModuleID >= 21 && pModuleID <= 27) || (pModuleID == 46) || (pModuleID == 153)) {
            clsPOGen.CancelPO(pCompanyID, pDocNo);
        }

        if ((pModuleID >= 28 && pModuleID <= 36) || (pModuleID == 47) || (pModuleID == 168)) {
            clsPOAmendGen.CancelAmendment(pCompanyID, pDocNo);
        }

        if (pModuleID == 3) //Indent
        {
            clsIndent.CancelIndent(pCompanyID, pDocNo);
        }

        if (pModuleID == 18) { //Inquiry
            clsInquiry.CancelInquiry(pCompanyID, pDocNo);
        }

        if (pModuleID == 1) { //Item Master
            clsItem.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 2) { //Material Requisition
            clsMaterialRequisition.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 50) { //Supplier Master Updation
            clsSupplierAmend.CancelAmendment(pCompanyID, pDocNo);
        }

        if (pModuleID == 51) { //Item Master Updation
            clsItemAmend.CancelItemAmendment(pCompanyID, pDocNo);
        }

        if (pModuleID == 42) //Freight Comparison
        {
            clsFreightComparison.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 43) //Freight Calculation
        {
            clsFreightCalculation.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 17) //Stock Transfer Memo(Raw Material)
        {
            clsSTMRaw.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == 16) //Stock Transfer Memo(General)
        {
            clsSTMGen.CancelDoc(pCompanyID, pDocNo);
        }

        if (pModuleID == clsSalesPartyAmend.ModuleID) //Stock Transfer Memo(General)
        {
            clsSalesPartyAmend.CancelAmendment(pCompanyID, pDocNo);
        }

        if (pModuleID == 715) //Stock Transfer Memo(General)
        {
            clsFeltPacking.CancelDoc(pDocNo);
        }
        if (pModuleID == 604) //Stock Transfer Memo(General)
        {
            clsFeltOrderDiversion.CancelDoc(pDocNo);
        }
        if (pModuleID == 602) //Stock Transfer Memo(General)
        {
            clsFeltOrder.CancelDoc(pDocNo);
        }
        if (pModuleID == 730) //Stock Transfer Memo(General)
        {
            clsDiscRateMaster.CancelDoc(pDocNo);
        }
        if (pModuleID == 754) //Stock Transfer Memo(General)
        {
            clsFeltGSTAdvancePaymentEntryForm.CancelDoc(pDocNo);
        }
        if (pModuleID == 603) //Felt Sales Finishing Entry
        {
            clsFeltFinishing.CancelDoc(pDocNo);
        }
        if (pModuleID == 724) //Felt Machine Master Entry
        {
            clsmachinesurvey.CancelDoc(pDocNo);
        }
        if (pModuleID == 725) //Felt Machine Master Amend Entry
        {
            clsmachinesurveyAmend.CancelDoc(pDocNo);
        }
        if (pModuleID == 743) //Felt Sales Group Master Amend Entry
        {
            clsFeltGroupMasterAmend.CancelDoc(pDocNo);
        }

        if (pModuleID == 750) //Felt Sales Piece Amendment WIP
        {
            clsPieceAmendApproval.CancelDoc(pDocNo);
        }

        if (pModuleID == 762) //Felt Sales Piece Amendment STOCK
        {
            clsFeltTransporterWeigthEntryForm.CancelDoc(pDocNo);
        }

        if (pModuleID == 763) //Felt Sales Piece Amendment STOCK
        {
            clsPieceAmendApproval_STOCK.CancelDoc(pDocNo);
        }

        if (pModuleID == 740) //Felt Sales Piece Amendment STOCK
        {
            clsFeltReopenBale.CancelDoc(pDocNo);
        }

        if (pModuleID == 732) ///Felt UNADJUSTED
        {
            clsFeltUnadj.CancelDoc(pDocNo);
        }

        if (pModuleID == 99) ///Debit Memo
        {
            clsDebitMemoReceiptMapping.CancelDoc(pDocNo);
        }

        if (pModuleID == 774) ///WIP Amend
        {
            clsPieceAmendWIP.CancelDoc(pDocNo);
        }
        if (pModuleID == 768) //Manual Budget
        {
            clsBudgetManual.CancelDoc(pDocNo);
        }
        if (pModuleID == 779) //Manual Budget
        {
            clsFeltOrderDiversionLoss.CancelDoc(pDocNo);
        }
        if (pModuleID == 790) //GIDC Production Process Entry
        {
            clsProductionEntry.CancelDoc(pDocNo);
        }
        if (pModuleID == 760) //Felt Piece Division
        {
            clsPieceDivision.CancelDoc(pDocNo);
        }

    }

    public static boolean CanCancelDocument(int pCompanyID, int pModuleID, String pDocNo) {
        boolean CanCancel = false;

        if (pModuleID == 878) {
            CanCancel = EITLERP.FeltSales.PPRSPlanning.clsPPRSEntry.CanCancel(pDocNo);
        }
        
        if (pModuleID == 876) {
            CanCancel = EITLERP.FeltSales.ZoneMaster.clsZoneMaster.CanCancel(pDocNo);
        }

        if (pModuleID == 877) {
            CanCancel = EITLERP.FeltSales.ZoneMaster.clsZoneMasterPartySelection.CanCancel(pDocNo);
        }

        if (pModuleID == 662) {
            CanCancel = EITLERP.FeltSales.ObsoleteScrap.clsFeltObsoleteScrap.CanCancel(pDocNo);
        }

        if (pModuleID == 873) {
            CanCancel = SDMLATTPAY.GatepassSelection.clsGatepassSelection.CanCancel(pDocNo);
        }

        if (pModuleID == 860) {
            CanCancel = SDMLATTPAY.TravelAdvance.clsTravelVoucher.CanCancel(pDocNo);
        }
        if (pModuleID == 859) {
            CanCancel = SDMLATTPAY.TravelAdvance.clsTravelEntryAmend.CanCancel(pDocNo);            
        }
        if (pModuleID == 858) {
            CanCancel = SDMLATTPAY.TravelAdvance.clsTravelEntry.CanCancel(pDocNo);            
        }
        if (pModuleID == 865) {
            CanCancel = SDMLATTPAY.ManpowerRequisition.clsManpowerRequisitionForm.CanCancel(pDocNo);
        }

        if (pModuleID == 862) {
            CanCancel = SDMLATTPAY.RokdiSelection.clsRokdiSelection.CanCancel(pDocNo);
        }

        if (pModuleID == 744) {
            CanCancel = EITLERP.FeltSales.SalesReturns.clsFeltSalesReturns.CanCancel(pDocNo);
        }

        if (pModuleID == 811) {
            CanCancel = SDMLATTPAY.leave.clsLeaveApplication.CanCancel(pDocNo);
        }

        if (pModuleID == 635) {
            CanCancel = EITLERP.FeltSales.PostAuditDiscRateMaster.clsPostAuditDiscRateMaster.CanCancel(pDocNo);
        }

        if (pModuleID == 769) {
            CanCancel = clsFeltGroupCriticalLimitEnhancement.CanCancel(pDocNo);
        }

        if (pModuleID == 634) {
            CanCancel = clsFeltPartyContact.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 615) {
            CanCancel = clsFeltInvCvrLtr.CanCancel(pDocNo);
        }

        if (pModuleID == 610) //Felt Document Cancel Request itself
        {
            CanCancel = EITLERP.FeltSales.FeltInvReport.clsDocCancel.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 632) {
            CanCancel = clsFeltPieceAmendmentPO.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 802) {
            CanCancel = clsPostInvoiceEntry.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 803) //NRGP
        {
            CanCancel = clsWHInvGatepassEntry.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 630) {
            CanCancel = clsFeltAutoPISelection.CanCancel(pDocNo);
        }

        if (pModuleID == 631) {
            CanCancel = clsFeltRateEligibility.CanCancel(pDocNo);
        }

        if (pModuleID == 112) {
            CanCancel = clsIssueRMG.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 800) {
            CanCancel = clsIssueStoresales.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 794) {
            CanCancel = clsNRGP_SDML.CanCancel(pCompanyID, pDocNo);
        }

//        if (pModuleID == 793) {
//            CanCancel = clsNRGP_GIDC.CanCancel(pCompanyID, pDocNo);
//        }
        if (pModuleID == clsGIDCInstruction.ModuleID) {
            CanCancel = clsGIDCInstruction.CanCancel(pDocNo);
        }
        if (pModuleID == clsGIDCInstructionAmend.ModuleID) {
            CanCancel = clsGIDCInstructionAmend.CanCancel(pDocNo);
        }
        if (pModuleID == 792) {
            CanCancel = clsDespatchGIDC_SDML.CanCancel(pDocNo);
        }

//        if (pModuleID == 626) {
//            CanCancel = clsFeltPDCAmend.CanCancel(pCompanyID, pDocNo);
//        }
        if (pModuleID == 625) {
            CanCancel = clsFeltPDC.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 742) {
            CanCancel = clsDiversionList.CanCancel(pDocNo);
        }

        if (pModuleID == 735) {
            CanCancel = clsFeltCreditNote.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 621) {
            CanCancel = clsFeltPieceAmendmentWIP.CanCancel(pCompanyID, pDocNo);
        }
        if (pModuleID == 622) {
            CanCancel = clsFeltPieceAmendmentSTOCK.CanCancel(pCompanyID, pDocNo);
        }
        if (pModuleID == 623) {
            CanCancel = clsFeltPieceAmendmentCHR.CanCancel(pCompanyID, pDocNo);
        }
        if (pModuleID == 624) {
            CanCancel = clsFeltPieceAmendmentEXPORT.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 612) {
            CanCancel = clsFeltEvaluationReOpen.CanCancel(pDocNo);
        }

        if (pModuleID == 611) {
            CanCancel = clsFeltEvaluationClosure.CanCancel(pDocNo);
        }

        if (pModuleID == 614) {
            CanCancel = clsFeltLRUpdation.CanCancel(pDocNo);
        }

        if (pModuleID == clsPartyMachineReOpen.ModuleID) {
            CanCancel = clsPartyMachineReOpen.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == clsPartyMachineClosure.ModuleID) {
            CanCancel = clsPartyMachineClosure.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 100) // Debit Memo Cancellation
        {
            CanCancel = clsDMwithoutDNCancellation.CanCancel(pCompanyID, pDocNo);
        }

        //Bhavesh
        if (pModuleID == clsExpense.ModuleID) {
            CanCancel = clsExpense.CanCancel(pCompanyID, pDocNo);
        }
        if (pModuleID == 104) {
            CanCancel = clsDepositRefund.CanCancel(pCompanyID, pDocNo);
        }

        //BHAVESH
        if (pModuleID == clsUpdationof09.ModuleID) {
            CanCancel = clsUpdationof09.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == clsOBCInvoice.ModuleID) {
            CanCancel = clsOBCInvoice.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == clsOBC.ModuleID) {
            CanCancel = clsOBC.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 64) {
            CanCancel = clsIssueRequisitionRaw.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 63) {
            CanCancel = clsExpenseTransaction.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == clsCrAdjustment.ModuleID) {
            CanCancel = clsCrAdjustment.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == clsDrAdjustment.ModuleID) {
            CanCancel = clsDrAdjustment.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 66 || pModuleID == 90 || pModuleID == 65 || pModuleID == 59 || pModuleID == 67 || pModuleID == 83 || pModuleID == 68 || pModuleID == 69 || pModuleID == 70 || pModuleID == 89 || pModuleID == 94) //Voucher
        {
            CanCancel = clsVoucher.CanCancel(pCompanyID, pDocNo, pModuleID);
        }

        //ASHUTOSH 
        if (pModuleID == clsMemorandumJV.ModuleID) //MEMORANDOMJV
        {
            CanCancel = clsMemorandumJV.CanCancel(pCompanyID, pDocNo, pModuleID);
        }
        //ASHUTOSH

        if (pModuleID == 54) //Party Master
        {
            CanCancel = clsPartyMaster.CanCancel(pCompanyID, UtilFunctions.CInt(pDocNo));
        }

        if (pModuleID == 57) //General Ledger
        {
            CanCancel = clsGL.CanCancel(pCompanyID, UtilFunctions.CInt(pDocNo));
        }

        if (pModuleID == 52) //Issue Requisition
        {
            CanCancel = clsIssueRequisition.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 4) //Gatepass Requisition
        {
            CanCancel = clsGPR.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 44) //Document Cancel Request itself
        {
            CanCancel = clsDocCancel.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 5) //MIR Gen.
        {
            CanCancel = clsMIRGen.CanCancel(pCompanyID, pDocNo);
        }
        
        if (pModuleID == 35) //MIR Gen.
        {
            CanCancel = clsMIRService.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 6) //MIR Raw
        {
            CanCancel = clsMIRRaw.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 7) //GRN Gen.
        {
            CanCancel = clsGRNGen.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 8) //GRN Raw
        {
            CanCancel = clsGRN.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 9) //RJN Gen.
        {
            CanCancel = clsRJNGen.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 10) //RJN Raw
        {
            CanCancel = clsRJN.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 48) //Jobwork entry
        {
            CanCancel = clsJobwork.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 14) //Issue Gen
        {
            CanCancel = clsIssue.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 15) //Issue Raw
        {
            CanCancel = clsIssueRaw.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 11) //NRGP
        {
            CanCancel = clsNRGP.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 12) //RGP
        {
            CanCancel = clsRGP.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 20) //Summary
        {
            CanCancel = clsQuotApproval.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 19) //Quotation
        {
            CanCancel = clsQuotation.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 38) //RIA
        {
            CanCancel = clsRateApproval.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 37) //Supplier Master
        {
            int SuppID = Integer.parseInt(pDocNo);
            CanCancel = clsSupplier.CanCancelSupplier(pCompanyID, SuppID);
        }

        if ((pModuleID >= 21 && pModuleID <= 27) || (pModuleID == 46) || (pModuleID == 153)) {
            CanCancel = clsPOGen.CanCancel(pCompanyID, pDocNo);
        }

        if ((pModuleID >= 28 && pModuleID <= 36) || (pModuleID == 47) || (pModuleID == 168)) {
            CanCancel = clsPOAmendGen.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 3) //Indent
        {
            CanCancel = clsIndent.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 1) //Item Master
        {
            CanCancel = clsItem.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 2) //Purchase Requisition
        {
            CanCancel = clsMaterialRequisition.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 18) { //Inquiry
            CanCancel = clsInquiry.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 50) { //Supplier Master Updation
            CanCancel = clsSupplierAmend.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 51) { //Item Master Updation
            CanCancel = clsItemAmend.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 42) //Freight Comparison
        {
            CanCancel = clsFreightComparison.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 43) //Freight Calculation
        {
            CanCancel = clsFreightCalculation.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 17) //Stock Transfer Memo(Raw Material)
        {
            CanCancel = clsSTMRaw.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == 16) //Stock Transfer Memo(General)
        {
            CanCancel = clsSTMGen.CanCancel(pCompanyID, pDocNo);
        }
        if (pModuleID == clsSalesPartyAmend.ModuleID) {
            CanCancel = clsSalesPartyAmend.CanCancel(pCompanyID, pDocNo);
        }

        if (pModuleID == clsSales_Party.ModuleID) {
            if (cancelMainCode.equals("") || cancelMainCode.equals("0")) {
            } else {
                CanCancel = clsSales_Party.CanCancelParty(pCompanyID, pDocNo, cancelMainCode);
            }
        }

        if (pModuleID == 715) //Stock Transfer Memo(General)
        {
            CanCancel = clsFeltPacking.CanCancel(pDocNo);
        }
        if (pModuleID == 604) //
        {
            CanCancel = clsFeltOrderDiversion.CanCancel(pDocNo);
        }
        if (pModuleID == 602) //
        {
            CanCancel = clsFeltOrder.CanCancel(pDocNo);
        }

        if (pModuleID == 730) //
        {
            CanCancel = clsDiscRateMaster.CanCancel(pDocNo);
        }

        if (pModuleID == 754) //
        {
            CanCancel = clsFeltGSTAdvancePaymentEntryForm.CanCancel(pDocNo);
        }
        if (pModuleID == 603) //Felt Sales Finishing Entry
        {
            CanCancel = clsFeltFinishing.CanCancel(pDocNo);
        }
        if (pModuleID == 724) //Felt Machine Master Entry
        {
            CanCancel = clsmachinesurvey.CanCancel(pDocNo);
        }
        if (pModuleID == 725) //Felt Machine Master Amend Entry
        {
            CanCancel = clsmachinesurveyAmend.CanCancel(pDocNo);
        }
        if (pModuleID == 743) //Felt Sales Finishing Entry
        {
            CanCancel = clsFeltGroupMasterAmend.CanCancel(pDocNo);
        }

        if (pModuleID == 750) //Felt Sales Piece Amend WIP
        {
            CanCancel = clsPieceAmendApproval.CanCancel(pDocNo);
        }

        if (pModuleID == 762) ///Felt Transporter Weight
        {
            CanCancel = clsFeltTransporterWeigthEntryForm.CanCancel(pDocNo);
        }

        if (pModuleID == 763) ///Felt Sales Piece Amend STOCK
        {
            CanCancel = clsPieceAmendApproval_STOCK.CanCancel(pDocNo);
        }

        if (pModuleID == 740) ///Felt Sales Piece Amend STOCK
        {
            CanCancel = clsFeltReopenBale.CanCancel(pDocNo);
        }

        if (pModuleID == 732) ///Felt UNADJUSTED
        {
            CanCancel = clsFeltUnadj.CanCancel(pDocNo);
        }
        if (pModuleID == 99) ///Felt UNADJUSTED
        {
            CanCancel = clsDebitMemoReceiptMapping.CanCancel(pDocNo);
        }

        if (pModuleID == 774) ///WIP Amend
        {
            CanCancel = clsPieceAmendWIP.CanCancel(pDocNo);
        }
        if (pModuleID == 768) //Manual Budget
        {
            CanCancel = clsBudgetManual.CanCancel(pDocNo);
        }
        if (pModuleID == 779) //Manual Budget
        {
            CanCancel = clsFeltOrderDiversionLoss.CanCancel(pDocNo);
        }
        if (pModuleID == 790) //GIDC Production Process Entry
        {
            CanCancel = clsProductionEntry.CanCancel(pDocNo);
        }
        if (pModuleID == 760) //Felt Piece Division
        {
            CanCancel = clsPieceDivision.CanCancel(pDocNo);
        }

        return CanCancel;
    }

    public static void OpenDocument(int pCompanyID, int pModuleID, String pDocNo, int MainCode) {
        String DocNo = pDocNo;
        int SelModule = pModuleID;
        String MCode = Integer.toString(MainCode);

        if (SelModule == 878) {
            AppletFrame aFrame = new AppletFrame("Felt PPRS Planning");
            aFrame.startAppletEx("EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry", "Felt PPRS Planning");
            EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry ObjDoc = (EITLERP.FeltSales.PPRSPlanning.FrmPPRSEntry) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }
        
        if (SelModule == 876) {
            AppletFrame aFrame = new AppletFrame("Zone Master");
            aFrame.startAppletEx("EITLERP.FeltSales.ZoneMaster.FrmZoneMaster", "Zone Master");
            EITLERP.FeltSales.ZoneMaster.FrmZoneMaster ObjItem = (EITLERP.FeltSales.ZoneMaster.FrmZoneMaster) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 877) {
            AppletFrame aFrame = new AppletFrame("Zone Party Selection");
            aFrame.startAppletEx("EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection", "Zone Party Selection");
            EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection ObjItem = (EITLERP.FeltSales.ZoneMaster.FrmZoneMasterPartySelection) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 704) {
            AppletFrame aFrame = new AppletFrame("Felt Heat Setting Entry");
            aFrame.startAppletEx("EITLERP.Production.FeltHeatSetting.frmFeltHeatSetting", "Felt Heat Setting Entry");
            frmFeltHeatSetting ObjItem = (frmFeltHeatSetting) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }        
        
        
        if (SelModule == 662) {
            AppletFrame aFrame = new AppletFrame("Obsolete Piece Scrap");
            aFrame.startAppletEx("EITLERP.FeltSales.ObsoleteScrap.frmFeltObsoleteScrap", "Obsolete Piece Scrap");
            EITLERP.FeltSales.ObsoleteScrap.frmFeltObsoleteScrap ObjItem = (EITLERP.FeltSales.ObsoleteScrap.frmFeltObsoleteScrap) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 873) {
            AppletFrame aFrame = new AppletFrame("Gatepass Selection");
            aFrame.startAppletEx("SDMLATTPAY.GatepassSelection.frmGatepassSelection", "Gatepass Selection");
            SDMLATTPAY.GatepassSelection.frmGatepassSelection ObjItem = (SDMLATTPAY.GatepassSelection.frmGatepassSelection) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 860) {

            AppletFrame aFrame = new AppletFrame("Travelling Voucher");
            aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelVoucher", "Travelling Voucher");
            SDMLATTPAY.TravelAdvance.FrmTravelVoucher ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelVoucher) aFrame.ObjApplet;
            ObjDoc.Find(DocNo);
        }
        if (SelModule == 859) {
            AppletFrame aFrame = new AppletFrame("Travelling Sanction Amendment");
            aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend", "Travelling Sanction Amendment");
            SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelEntryAmend) aFrame.ObjApplet;
            ObjDoc.Find(DocNo);
        }
        if (SelModule == 858) {
            AppletFrame aFrame = new AppletFrame("Travelling Sanction");
            aFrame.startAppletEx("SDMLATTPAY.TravelAdvance.FrmTravelEntry", "Travelling Sanction");
            SDMLATTPAY.TravelAdvance.FrmTravelEntry ObjDoc = (SDMLATTPAY.TravelAdvance.FrmTravelEntry) aFrame.ObjApplet;
            ObjDoc.Find(DocNo);
        }

        if (SelModule == 865) {
            AppletFrame aFrame = new AppletFrame("Excess Manpower Requisition");
            aFrame.startAppletEx("SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm", "Excess Manpower Requisition");
            SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm ObjItem = (SDMLATTPAY.ManpowerRequisition.frmManpowerRequisitionForm) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 862) {
            AppletFrame aFrame = new AppletFrame("Rokdi Selection");
            aFrame.startAppletEx("SDMLATTPAY.RokdiSelection.frmRokdiSelection", "Rokdi Selection");
            SDMLATTPAY.RokdiSelection.frmRokdiSelection ObjItem = (SDMLATTPAY.RokdiSelection.frmRokdiSelection) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 744) {
            AppletFrame aFrame = new AppletFrame("Felt Sales/Goods Return");
            aFrame.startAppletEx("EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns", "Felt Sales/Goods Return");
            EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns ObjItem = (EITLERP.FeltSales.SalesReturns.frmFeltSalesReturns) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 635) {
            AppletFrame aFrame = new AppletFrame("Post Audit Disc Rate Master");
            aFrame.startAppletEx("EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster", "Post Audit Disc Rate Master");
            EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster ObjItem = (EITLERP.FeltSales.PostAuditDiscRateMaster.frmPostAuditDiscRateMaster) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (pModuleID == 769) {
            AppletFrame aFrame = new AppletFrame("Felt Group Critical Limit Enhancement");
            aFrame.startAppletEx("EITLERP.FeltSales.GroupCriticalLimitEnhancement.frmFelGroupCriticalLimitEnhancement", "Felt Critical Limit Enhancement");
            frmFelGroupCriticalLimitEnhancement ObjItem = (frmFelGroupCriticalLimitEnhancement) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 634) {
            AppletFrame aFrame = new AppletFrame("Felt Party Contact Detail Upadtion");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPartyContact.frmFeltPartyContact", "Felt Party Contact Detail Upadtion");
            frmFeltPartyContact ObjItem = (frmFeltPartyContact) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 615) {
            AppletFrame aFrame = new AppletFrame("Felt Invoice Covering Letter");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltInvCvrLtr.frmFeltInvCvrLtr", "Felt Invoice Covering Letter");
            frmFeltInvCvrLtr ObjItem = (frmFeltInvCvrLtr) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (pModuleID == 805) {
            AppletFrame aFrame = new AppletFrame("Gatepass Entry");
            aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmGatepass", "Gatepass Entry");
            SDMLATTPAY.gatepass.FrmGatepass ObjItem = (SDMLATTPAY.gatepass.FrmGatepass) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 819) {
            AppletFrame aFrame = new AppletFrame("Special Sanction Entry");
            aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmSpecialSanctionEntry", "Special Sanction Entry");
            SDMLATTPAY.gatepass.FrmSpecialSanctionEntry ObjDoc = (SDMLATTPAY.gatepass.FrmSpecialSanctionEntry) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 810) {
            AppletFrame aFrame = new AppletFrame("Time Correction Entry");
            aFrame.startAppletEx("SDMLATTPAY.gatepass.FrmTimeCorrectionEntry", "Time Correction Entry");
            FrmTimeCorrectionEntry ObjDoc = (FrmTimeCorrectionEntry) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 813) {
            AppletFrame aFrame = new AppletFrame("Leave Updation Entry");
            aFrame.startAppletEx("SDMLATTPAY.leave.FrmLeaveUpdation", "Leave Updation Entry");
            SDMLATTPAY.leave.FrmLeaveUpdation ObjItem = (SDMLATTPAY.leave.FrmLeaveUpdation) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 811) {
            AppletFrame aFrame = new AppletFrame("Leave Application Entry");
            aFrame.startAppletEx("SDMLATTPAY.leave.FrmLeaveApplication", "Leave Application Entry");
            SDMLATTPAY.leave.FrmLeaveApplication ObjItem = (SDMLATTPAY.leave.FrmLeaveApplication) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 632) {
            AppletFrame aFrame = new AppletFrame("PO Details Updation");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO", "PO Details Updation");
            EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentPO) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 802) {
            AppletFrame aFrame = new AppletFrame("Felt WH Post Invoice Data");
            aFrame.startAppletEx("EITLERP.FeltWH.frmPostInvoiceEntry", "Felt WH Post Invoice Data");
            frmPostInvoiceEntry ObjDoc = (frmPostInvoiceEntry) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 803) {
            AppletFrame aFrame = new AppletFrame("Felt WH Gatepass");
            aFrame.startAppletEx("EITLERP.FeltWH.frmWHInvGatepassEntry", "Felt WH Gatepass");
            frmWHInvGatepassEntry ObjDoc = (frmWHInvGatepassEntry) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 630) {
            AppletFrame aFrame = new AppletFrame("Felt Auto PI Selection");
            aFrame.startAppletEx("EITLERP.FeltSales.AutoPI.frmFeltAutoPISelection", "Felt Auto PI Selection");
            frmFeltAutoPISelection ObjItem = (frmFeltAutoPISelection) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 631) {
            AppletFrame aFrame = new AppletFrame("Special Rate Sanction Eligibility");
            aFrame.startAppletEx("EITLERP.FeltSales.RateEligibility.frmFeltRateEligibility", "Special Rate Sanction Eligibility");
            frmFeltRateEligibility ObjItem = (frmFeltRateEligibility) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (pModuleID == 112) {
            AppletFrame aFrame = new AppletFrame("ISSUE (RMG)");
            aFrame.startAppletEx("EITLERP.Stores.FrmIssue_RMG", "ISSUE (RMG)");
            FrmIssue_RMG ObjItem = (FrmIssue_RMG) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == 800) {
            AppletFrame aFrame = new AppletFrame("ISSUE (Stores Sales)");
            aFrame.startAppletEx("EITLERP.Stores.FrmIssue_Storesales", "ISSUE (Stores Sales)");
            FrmIssue_Storesales ObjItem = (FrmIssue_Storesales) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == 794) {
            AppletFrame aFrame = new AppletFrame("NON RETURNABLE GATEPASS SDML To GIDC");
            aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_SDML", "NON RETURNABLE GATEPASS SDML To GIDC");
            FrmNRGP_SDML ObjItem = (FrmNRGP_SDML) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

//        if (pModuleID == 793) {
//            AppletFrame aFrame = new AppletFrame("NON RETURNABLE GATEPASS GIDC To SDML");
//            aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.NRGP.FrmNRGP_GIDC", "NON RETURNABLE GATEPASS GIDC To SDML");
//            FrmNRGP_GIDC ObjItem = (FrmNRGP_GIDC) aFrame.ObjApplet;
//            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
//        }
        if (pModuleID == clsGIDCInstruction.ModuleID) {
            AppletFrame aFrame = new AppletFrame("GIDC Instruction Entry [SDF]");
            aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Instruction.frmGIDCInstruction", "GIDC Instruction Entry [SDF]");
            frmGIDCInstruction ObjItem = (frmGIDCInstruction) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == clsGIDCInstructionAmend.ModuleID) {
            AppletFrame aFrame = new AppletFrame("GIDC Instruction Amend Entry [SDF]");
            aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Instruction.frmGIDCInstructionAmend", "GIDC Instruction Amend Entry [SDF]");
            frmGIDCInstructionAmend ObjItem = (frmGIDCInstructionAmend) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == 792) {
            AppletFrame aFrame = new AppletFrame("GIDC Despatch of SDF to SDML");
            aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.Despatch.frmDespatchGIDC_SDML", "GIDC Despatch of SDF to SDML");
            frmDespatchGIDC_SDML ObjItem = (frmDespatchGIDC_SDML) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

//        if (pModuleID == 626) {
//            AppletFrame aFrame = new AppletFrame("Felt PDC Amendment");
//            aFrame.startAppletEx("EITLERP.FeltSales.FeltPDCAmend.frmFeltPDCAmend", "Felt PDC Amendment");
//            frmFeltPDCAmend ObjItem = (frmFeltPDCAmend) aFrame.ObjApplet;
//            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
//        }
        if (pModuleID == 625) {
            AppletFrame aFrame = new AppletFrame("Felt PDC Entry Form");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPDC.frmFeltPDC", "Felt PDC Entry Form");
            frmFeltPDC ObjItem = (frmFeltPDC) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 760) {
            AppletFrame aFrame = new AppletFrame("Felt Piece Division");
            aFrame.startAppletEx("EITLERP.FeltSales.PieceDivision.FrmPieceDivision", "Felt Piece Division");
            FrmPieceDivision ObjItem = (FrmPieceDivision) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 742) {
            AppletFrame aFrame = new AppletFrame("Manual Diversion List");
            aFrame.startAppletEx("EITLERP.FeltSales.DiversionList.FrmDiversionList", "Manual Diversion List");
            FrmDiversionList ObjItem = (FrmDiversionList) aFrame.ObjApplet;
            ObjItem.FindEx(DocNo);
        }

        if (pModuleID == 735) {
            AppletFrame aFrame = new AppletFrame("Felt Credit Note");
            aFrame.startAppletEx("EITLERP.Production.FeltCreditNote.frmFeltCreditNote", "Felt Credit Note");
            EITLERP.Production.FeltCreditNote.frmFeltCreditNote ObjItem = (EITLERP.Production.FeltCreditNote.frmFeltCreditNote) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 621) {
            AppletFrame aFrame = new AppletFrame("WIP Piece Amendment");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP", "WIP Piece Amendment");
            EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentWIP) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 622) {
            AppletFrame aFrame = new AppletFrame("STOCK Piece Tagging");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK", "STOCK Piece Tagging");
            EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentSTOCK) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 623) {
            AppletFrame aFrame = new AppletFrame("Piece Cancellation");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR", "Piece Cancellation");
            EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentCHR) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 624) {
            AppletFrame aFrame = new AppletFrame("Piece Transfer to Export");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT", "Piece Transfer to Export");
            EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT ObjItem = (EITLERP.FeltSales.FeltPieceAmend.frmFeltPieceAmendmentEXPORT) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (pModuleID == 612) {
            AppletFrame aFrame = new AppletFrame("Felt Evaluation ReOpen");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltEvaluation.frmFeltEvaluationReOpen", "Felt Evaluation ReOpen");
            frmFeltEvaluationReOpen ObjItem = (frmFeltEvaluationReOpen) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == 611) {
            AppletFrame aFrame = new AppletFrame("Felt Evaluation Closure");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltEvaluation.frmFeltEvaluationClosure", "Felt Evaluation Closure");
            frmFeltEvaluationClosure ObjItem = (frmFeltEvaluationClosure) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == 614) {
            AppletFrame aFrame = new AppletFrame("Felt Sales LR Updation");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltLRUpdation.frmFeltLRUpdation", "Felt Sales LR Updation");
            frmFeltLRUpdation ObjItem = (frmFeltLRUpdation) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == clsPartyMachineReOpen.ModuleID) {
            AppletFrame aFrame = new AppletFrame("ReOpen Party Machine Position");
            aFrame.startAppletEx("EITLERP.FeltSales.PartyMachineReOpen.frmPartyMachineReOpen", "ReOpen Party Machine Position");
            frmPartyMachineReOpen ObjItem = (frmPartyMachineReOpen) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == clsPartyMachineClosure.ModuleID) {
            AppletFrame aFrame = new AppletFrame("Party Machine Position Closure");
            aFrame.startAppletEx("EITLERP.FeltSales.PartyMachineClosure.frmPartyMachineClosure", "Party Machine Position Closure");
            frmPartyMachineClosure ObjItem = (frmPartyMachineClosure) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (SelModule == 100) { // Debit Memo Cancellation
            AppletFrame aFrame = new AppletFrame("Debit Memo Cancellation");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDMwithoutDNCancellation", "Debit Memo Cancellation");
            frmDMwithoutDNCancellation ObjItem = (frmDMwithoutDNCancellation) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (pModuleID == clsOBC.ModuleID) {
            AppletFrame aFrame = new AppletFrame("OBC");
            aFrame.startAppletEx("EITLERP.Finance.frmOBC", "OBC");
            frmOBC ObjItem = (frmOBC) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

        if (pModuleID == clsOBCInvoice.ModuleID) {
            AppletFrame aFrame = new AppletFrame("OBC Invoice");
            aFrame.startAppletEx("EITLERP.Finance.frmOBCInvoice", "OBC Invoice");
            frmOBCInvoice ObjItem = (frmOBCInvoice) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, pDocNo);
        }

//        if(pModuleID==clsSalesInvoice.ModuleID)
//        {
//            AppletFrame aFrame=new AppletFrame("Sales Invoice");
//            aFrame.startAppletEx("EITLERP.Sales.frmSalesInvoice","Sales Invoice");
//            frmSalesInvoice ObjItem=(frmSalesInvoice) aFrame.ObjApplet;
//            ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,pDocNo);
//            
//        }
        //BHAVESH UPDATION/09EITLERP.Finance.frmUpdationof09
        if (pModuleID == 177) {
            String DocID = pDocNo;
            AppletFrame aFrame = new AppletFrame("Misc. Expense Entry");
            aFrame.startAppletEx("EITLERP.Finance.frmUpdationof09", "UPDATION 09");
            frmUpdationof09 ObjItem = (frmUpdationof09) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocID);
        }

        if (pModuleID == 63) {
            String ExpenseID = pDocNo;
            AppletFrame aFrame = new AppletFrame("Misc. Expense Entry");
            aFrame.startAppletEx("EITLERP.Finance.frmExpenseTransaction", "Expense Transaction");
            frmExpenseTransaction ObjItem = (frmExpenseTransaction) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, ExpenseID);
        }

        if (pModuleID == 62) {
            String ExpenseID = pDocNo;
            AppletFrame aFrame = new AppletFrame("Misc. Expense Entry");
            aFrame.startAppletEx("EITLERP.Finance.frmExpense", "Misc. Expense Entry");
            frmExpense ObjItem = (frmExpense) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, ExpenseID);
        }

        if (pModuleID == 57) {
            String AccountID = pDocNo;
            AppletFrame aFrame = new AppletFrame("General Ledger");
            aFrame.startAppletEx("EITLERP.Finance.frmGL", "General Ledger");
            frmGL ObjItem = (frmGL) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, Integer.parseInt(AccountID));
        }

//        if(pModuleID==54) {
//            String PartyID=pDocNo;
//            AppletFrame aFrame=new AppletFrame("Party Master");
//            aFrame.startAppletEx("EITLERP.Finance.frmPartyMaster","Party Master");
//            frmPartyMaster ObjItem=(frmPartyMaster) aFrame.ObjApplet;
//            ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,Integer.parseInt(PartyID));
//        }
        if (pModuleID == 66 || pModuleID == 90 || pModuleID == 59 || pModuleID == 67 || pModuleID == 83 || pModuleID == 68 || pModuleID == 69 || pModuleID == 70 || pModuleID == 89 || pModuleID == 94) //Voucher
        {

            int VoucherType = clsVoucher.getVoucherType(pDocNo);
            clsVoucher.OpenVoucher(pDocNo, new frmPendingApprovals());
            /*String VoucherNo=pDocNo;
             AppletFrame aFrame=new AppletFrame("Voucher");
             aFrame.startAppletEx("EITLERP.Finance.frmVoucher","Voucher");
             frmVoucher ObjItem=(frmVoucher) aFrame.ObjApplet;
             ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,VoucherNo);*/
        }

        if (pModuleID == 65) {
            String VoucherNo = pDocNo;
            AppletFrame aFrame = new AppletFrame("DebitNote Voucher");
            aFrame.startAppletEx("EITLERP.Finance.frmDebitNoteVoucher", "DebitNote Voucher");
            frmDebitNoteVoucher ObjItem = (frmDebitNoteVoucher) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, VoucherNo);
        }

        if (SelModule == clsCrAdjustment.ModuleID) {
            String nDocNo = pDocNo;
            AppletFrame aFrame = new AppletFrame("Advance Payment Adjustment");
            aFrame.startAppletEx("EITLERP.Finance.frmCrAdjustment", "Advance Payment Adjustment");
            frmCrAdjustment ObjItem = (frmCrAdjustment) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, nDocNo);
        }

        if (SelModule == clsDrAdjustment.ModuleID) {
            String nDocNo = pDocNo;
            AppletFrame aFrame = new AppletFrame("Advance Receipt Adjustment");
            aFrame.startAppletEx("EITLERP.Finance.frmDrAdjustment", "Advance Receipt Adjustment");
            frmDrAdjustment ObjItem = (frmDrAdjustment) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, nDocNo);
        }

        if (SelModule == 52) {
            String lItemID = DocNo;
            AppletFrame aFrame = new AppletFrame("Issue Requisition");
            aFrame.startAppletEx("EITLERP.Stores.frmIssueReq", "Issue Requisition");
            frmIssueReq ObjItem = (frmIssueReq) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
        }

        if (SelModule == 64) {
            String lItemID = DocNo;
            AppletFrame aFrame = new AppletFrame("Issue Requisition RM");
            aFrame.startAppletEx("EITLERP.Stores.frmIssueReqRaw", "Issue Requisition");
            frmIssueReqRaw ObjItem = (frmIssueReqRaw) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
        }

        //-----*
        if (SelModule == 19) {
            String lItemID = DocNo;
            AppletFrame aFrame = new AppletFrame("Quotation");
            aFrame.startAppletEx("EITLERP.Purchase.frmQuotation", "Quotation");
            frmQuotation ObjItem = (frmQuotation) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
        }
        
        //======*
        if (SelModule == 35) {
            AppletFrame aFrame = new AppletFrame("MIR Service");
            aFrame.startAppletEx("EITLERP.Stores.frmMIRService", "MIR Service");
            frmMIRService ObjDoc = (frmMIRService) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //-----*
        if (SelModule == 1) {
            String lItemID = DocNo;
            AppletFrame aFrame = new AppletFrame("Item");
            aFrame.startAppletEx("EITLERP.frmItem", "Item Master");
            frmItem ObjItem = (frmItem) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
        }

        //-----*
        if (SelModule == 37) {
            String lItemID = DocNo;
            AppletFrame aFrame = new AppletFrame("Supplier");
            aFrame.startAppletEx("EITLERP.frmSupplier", "Supplier");
            frmSupplier ObjItem = (frmSupplier) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, lItemID);
        }

        //----*
        if (SelModule == 2) {
            AppletFrame aFrame = new AppletFrame("Material Requisition");
            aFrame.startAppletEx("EITLERP.Stores.frmMR", "Material Requisition");
            frmMR ObjDoc = (frmMR) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //----*
        if (SelModule == 42) {
            AppletFrame aFrame = new AppletFrame("Freight Comparison");
            aFrame.startAppletEx("EITLERP.Purchase.frmFreightComparison", "Freight Comparison");
            frmFreightComparison ObjDoc = (frmFreightComparison) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //----*
        if (SelModule == 43) {
            AppletFrame aFrame = new AppletFrame("Freight Calculation");
            aFrame.startAppletEx("EITLERP.Purchase.frmFreightCalculation", "Freight Calculation");
            frmFreightCalculation ObjDoc = (frmFreightCalculation) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //-----*
        if (SelModule == 20) {
            AppletFrame aFrame = new AppletFrame("Summary");
            aFrame.startAppletEx("EITLERP.Purchase.frmQuotApproval", "Quot Approval");
            frmQuotApproval ObjDoc = (frmQuotApproval) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //------*
        if (SelModule == 11) {
            AppletFrame aFrame = new AppletFrame("NRGP");
            aFrame.startAppletEx("EITLERP.Stores.FrmNRGP_General", "NRGP");
            FrmNRGP_General ObjDoc = (FrmNRGP_General) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //-----*
        if (SelModule == 12) {
            AppletFrame aFrame = new AppletFrame("RGP");
            aFrame.startAppletEx("EITLERP.Stores.FrmRGP_General", "RGP");
            FrmRGP_General ObjDoc = (FrmRGP_General) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //======*
        if (SelModule == 14) {
            AppletFrame aFrame = new AppletFrame("Issue");
            aFrame.startAppletEx("EITLERP.Stores.FrmIssue_General", "Issue");
            FrmIssue_General ObjDoc = (FrmIssue_General) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=====*
        if (SelModule == 15) {
            AppletFrame aFrame = new AppletFrame("Issue");
            aFrame.startAppletEx("EITLERP.Stores.FrmIssue_Raw", "Issue");
            FrmIssue_Raw ObjDoc = (FrmIssue_Raw) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //==*
        if (SelModule == 3) {
            AppletFrame aFrame = new AppletFrame("Indent");
            aFrame.startAppletEx("EITLERP.Stores.FrmIndent", "Indent");
            FrmIndent ObjDoc = (FrmIndent) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //========*
        if (SelModule == 40) {
            AppletFrame aFrame = new AppletFrame("RGP Return");
            aFrame.startAppletEx("EITLERP.Stores.frmRGPReturn", "RGP Return");
            frmRGPReturn ObjDoc = (frmRGPReturn) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=====*
        if (SelModule == 18) {
            AppletFrame aFrame = new AppletFrame("Inquiry");
            aFrame.startAppletEx("EITLERP.Purchase.FrmInquiry", "Inquiry");
            FrmInquiry ObjDoc = (FrmInquiry) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //------ *
        if (SelModule == 4) {
            AppletFrame aFrame = new AppletFrame("Gatepass Requisition");
            aFrame.startAppletEx("EITLERP.Stores.frmGPR", "Gatepass Requisition");
            frmGPR ObjDoc = (frmGPR) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //======*
        if (SelModule == 5) {
            AppletFrame aFrame = new AppletFrame("MIR General");
            aFrame.startAppletEx("EITLERP.Stores.frmMIRGen", "MIR General");
            frmMIRGen ObjDoc = (frmMIRGen) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //======*
        if (SelModule == 6) {
            AppletFrame aFrame = new AppletFrame("MIR Raw Material");
            aFrame.startAppletEx("EITLERP.Stores.frmMIR", "MIR Raw Material");
            frmMIR ObjDoc = (frmMIR) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=====*
        if (SelModule == 7) {
            AppletFrame aFrame = new AppletFrame("GRN General");
            aFrame.startAppletEx("EITLERP.Stores.frmGRNGen", "GRN General");
            frmGRNGen ObjDoc = (frmGRNGen) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //==========*
        if (SelModule == 8) {
            AppletFrame aFrame = new AppletFrame("GRN Raw Material");
            aFrame.startAppletEx("EITLERP.Stores.frmGRN", "GRN Raw Material");
            frmGRN ObjDoc = (frmGRN) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=====*
        if (SelModule == 9) {
            AppletFrame aFrame = new AppletFrame("RJN General");
            aFrame.startAppletEx("EITLERP.Stores.frmRJNGen", "RJN General");
            frmRJNGen ObjDoc = (frmRJNGen) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //======*
        if (SelModule == 10) {
            AppletFrame aFrame = new AppletFrame("RJN Raw Material");
            aFrame.startAppletEx("EITLERP.Stores.frmRJN", "RJN Raw Material");
            frmRJN ObjDoc = (frmRJN) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //======*
        if (SelModule == 21) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 1;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 46) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmServiceContract", "Service Contract");
            frmServiceContract ObjDoc = (frmServiceContract) aFrame.ObjApplet;
            ObjDoc.POType = 8;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //===== *
        if (SelModule == 22) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 2;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //====*
        if (SelModule == 23) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 3;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //====*
        if (SelModule == 24) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 4;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=====*
        if (SelModule == 25) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 5;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //======*
        if (SelModule == 26) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 6;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=====*
        if (SelModule == 27) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 7;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 153) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOGen", "Purchase Order");
            frmPOGen ObjDoc = (frmPOGen) aFrame.ObjApplet;
            ObjDoc.POType = 9;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //Amendments
        //======*
        if (SelModule == 28) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 1;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 47) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmServiceContractAmend", "Service Contract Amendment");
            frmServiceContractAmend ObjDoc = (frmServiceContractAmend) aFrame.ObjApplet;
            ObjDoc.POType = 8;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=======*
        if (SelModule == 29) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 2;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //========*
        if (SelModule == 30) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 3;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=======*
        if (SelModule == 31) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 4;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //======*
        if (SelModule == 32) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 5;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=======*
        if (SelModule == 33) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 6;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //=======*
        if (SelModule == 34) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 7;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 168) {
            AppletFrame aFrame = new AppletFrame("Purchase Order");
            aFrame.startAppletEx("EITLERP.Purchase.frmPOAmendGen", "Purchase Order Amendment");
            frmPOAmendGen ObjDoc = (frmPOAmendGen) aFrame.ObjApplet;
            ObjDoc.POType = 9;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }
        //======*
        if (SelModule == 38) {
            AppletFrame aFrame = new AppletFrame("Rate Approval");
            aFrame.startAppletEx("EITLERP.Purchase.frmRateApproval", "Rate Approval");
            frmRateApproval ObjDoc = (frmRateApproval) aFrame.ObjApplet;
            ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 48) {
            AppletFrame aFrame = new AppletFrame("Job work entry");
            aFrame.startAppletEx("EITLERP.Stores.frmJobwork", "Job work entry");
            frmJobwork ObjItem = (frmJobwork) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 50) {
            AppletFrame aFrame = new AppletFrame("Supplier Master Updation");
            aFrame.startAppletEx("EITLERP.frmSupplierAmend", "Supplier Master Updation");
            frmSupplierAmend ObjItem = (frmSupplierAmend) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 51) {
            AppletFrame aFrame = new AppletFrame("Item Master Updation");
            aFrame.startAppletEx("EITLERP.frmItemAmend", "Item Master Updation");
            frmItemAmend ObjItem = (frmItemAmend) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, Integer.parseInt(DocNo));
        }
        //*** KOMAL
        if (SelModule == 96) {
            if (DocNo.trim().startsWith("R")) {
                DocNo = DocNo.trim().substring(1);
                AppletFrame aFrame = new AppletFrame("RGP");
                aFrame.startAppletEx("EITLERP.Stores.FrmRGP_General", "RGP");
                FrmRGP_General ObjDoc = (FrmRGP_General) aFrame.ObjApplet;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            } else if (DocNo.trim().startsWith("N")) {
                DocNo = DocNo.trim().substring(1);
                AppletFrame aFrame = new AppletFrame("NRGP");
                aFrame.startAppletEx("EITLERP.Stores.FrmNRGP_General", "NRGP");
                FrmNRGP_General ObjDoc = (FrmNRGP_General) aFrame.ObjApplet;
                ObjDoc.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
            }
        }

        //Bhavesh 
        if (SelModule == 104) {
            AppletFrame aFrame = new AppletFrame("Deposit Refund");
            aFrame.startAppletEx("EITLERP.Finance.frmDepositRefund", "Deposit Refund");
            frmDepositRefund ObjItem = (frmDepositRefund) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        // *** KOMAL
        if (SelModule == 105) {
            AppletFrame aFrame = new AppletFrame("Policy Master");
            aFrame.startAppletEx("EITLERP.Finance.frmPolicyMaster", "Policy Master");
            frmPolicyMaster ObjItem = (frmPolicyMaster) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == clsPolicyMaster.ModuleID) {
            AppletFrame aFrame = new AppletFrame("Policy Master");
            aFrame.startAppletEx("EITLERP.Finance.frmPolicyMaster", "Policy Master");
            frmPolicyMaster ObjItem = (frmPolicyMaster) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

//        if (SelModule == clsVoucher.CreditNoteVoucherModuleID) {
//            AppletFrame aFrame=new AppletFrame("Credit Note Voucher");
//            aFrame.startAppletEx("EITLERP.Finance.frmCreditNoteVoucher","Credit Note Voucher");
//            frmCreditNoteVoucher ObjItem=(frmCreditNoteVoucher) aFrame.ObjApplet;
//            ObjItem.FindEx((int)EITLERPGLOBAL.gCompanyID,DocNo);
//        }
        if (SelModule == 17) { //Stock transfer Raw Material
            AppletFrame aFrame = new AppletFrame(" STOCK TRANSFER MEMO (Raw Material)");
            aFrame.startAppletEx("EITLERP.Stores.frmSTM", " STOCK TRANSFER MEMO (Raw Material)");
            frmSTM ObjItem = (frmSTM) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == 16) { //Stock transfer General
            AppletFrame aFrame = new AppletFrame(" STOCK TRANSFER MEMO (General)");
            aFrame.startAppletEx("EITLERP.Stores.frmSTMGen", " STOCK TRANSFER MEMO (General)");
            frmSTMGen ObjItem = (frmSTMGen) aFrame.ObjApplet;
            ObjItem.FindEx((int) EITLERPGLOBAL.gCompanyID, DocNo);
        }

        if (SelModule == clsSales_Party.ModuleID) { //Stock transfer General
            AppletFrame aFrame = new AppletFrame(" Sales Party Master ");
            aFrame.startAppletEx("EITLERP.frmSalesParty", " Sales Party Master ");
            frmSalesParty ObjItem = (frmSalesParty) aFrame.ObjApplet;
            ObjItem.FindEx(EITLERPGLOBAL.gCompanyID, DocNo, MCode);
        }

        if (SelModule == clsSalesPartyAmend.ModuleID) { //Stock transfer General
            AppletFrame aFrame = new AppletFrame("Sales Party Master Amend");
            aFrame.startAppletEx("EITLERP.frmSalesPartyAmend", " Sales Party Master Updation");
            frmSalesPartyAmend ObjItem = (frmSalesPartyAmend) aFrame.ObjApplet;
            ObjItem.FindEx(EITLERPGLOBAL.gCompanyID, Integer.parseInt(DocNo));
        }

        if (SelModule == clsFASCardwithoutGRN.ModuleID) { //Fixed Asset Without GRN
            AppletFrame aFrame = new AppletFrame("Fixed Asset Card Entry Form");
            aFrame.startAppletEx("EITLERP.Finance.frmFASCardwithoutGRN", "Fixed Asset Card Entry Form");
            frmFASCardwithoutGRN ObjItem = (frmFASCardwithoutGRN) aFrame.ObjApplet;
            ObjItem.FindEx(EITLERPGLOBAL.gCompanyID, DocNo);
        }

        //Ashutosh
        if (pModuleID == clsMemorandumJV.ModuleID) {
            AppletFrame aFrame = new AppletFrame("Memorandum JV");
            aFrame.startAppletEx("EITLERP.Finance.frmMemorandumJV", "Memorandum JV");
            frmMemorandumJV Obj = (frmMemorandumJV) aFrame.ObjApplet;
            Obj.FindEx(EITLERPGLOBAL.gCompanyID, DocNo);

        }
        if (SelModule == 715) { //Stock transfer Raw Material
            AppletFrame aFrame = new AppletFrame(" Felt Packing Slip ");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltPacking.frmFeltPacking", " Felt Packing Slip");
            frmFeltPacking ObjItem = (frmFeltPacking) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        if (SelModule == 604) { //Stock transfer Raw Material
            AppletFrame aFrame = new AppletFrame(" Felt Sales Order Diversion");
            aFrame.startAppletEx("EITLERP.FeltSales.OrderDiversion.FrmFeltOrderDiversion", " Felt Sales Order Diversion");
            FrmFeltOrderDiversion ObjItem = (FrmFeltOrderDiversion) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        if (SelModule == 602) { //Felt Sales Order Entry
            AppletFrame aFrame = new AppletFrame(" Felt Sales Order Entry");
            aFrame.startAppletEx("EITLERP.FeltSales.Order.FrmFeltOrder", " Felt Sales Order Entry");
            FrmFeltOrder ObjItem = (FrmFeltOrder) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 730) { //Stock transfer Raw Material
            AppletFrame aFrame = new AppletFrame(" Felt Discount Master");
            aFrame.startAppletEx("EITLERP.Production.FeltDiscRateMaster.frmDiscRateMaster", " Felt Sales Order Diversion");
            frmDiscRateMaster ObjItem = (frmDiscRateMaster) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 754) { //Stock transfer Raw Material
            AppletFrame aFrame = new AppletFrame(" Invoice Parameter Modification");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltGSTAdvancePaymentEntryForm.frmFeltGSTAdvancePaymentEntryForm", " Invoice Parameter Modification");
            frmFeltGSTAdvancePaymentEntryForm ObjItem = (frmFeltGSTAdvancePaymentEntryForm) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 603) { //Felt Sales Finishing Entry
            AppletFrame aFrame = new AppletFrame("Felt Finishing Entry");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltFinishing.frmFeltFinishing", "Felt Finishing Entry");
            frmFeltFinishing ObjItem = (frmFeltFinishing) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 724) { //Felt Machine Master Entry
            AppletFrame aFrame = new AppletFrame("Felt Machine Master Entry");
            aFrame.startAppletEx("EITLERP.Production.FeltMachineSurvey.frmmachinesurvey", "Felt Machine Master Entry");
            frmmachinesurvey ObjItem = (frmmachinesurvey) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 725) { //Felt Machine Master Amend Entry
            AppletFrame aFrame = new AppletFrame("Felt Machine Master Amend Entry");
            aFrame.startAppletEx("EITLERP.Production.FeltMachineSurveyAmend.frmmachinesurveyAmend", "Felt Machine Master Amend Entry");
            frmmachinesurveyAmend ObjItem = (frmmachinesurveyAmend) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 743) { //Felt Sales Finishing Entry
            AppletFrame aFrame = new AppletFrame("Felt Gruop Master Amend Entry");
            aFrame.startAppletEx("EITLERP.FeltSales.GroupMasterAmend.frmFelGroupMasterAmend", "Felt Gruop Master Amend Entry");
            frmFelGroupMasterAmend ObjItem = (frmFelGroupMasterAmend) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 750) { //Felt Sales Piece Amendmend WIP Pieces
            AppletFrame aFrame = new AppletFrame(" Felt Sales Piece Amendmend WIP Pieces");
            aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentApproval.frmPieceAmendApproval", " Felt Sales Piece Amendmend WIP Pieces");
            frmPieceAmendApproval ObjItem = (frmPieceAmendApproval) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 763) { //Felt Sales Piece Amendmend STOCK Pieces
            AppletFrame aFrame = new AppletFrame(" Felt Sales Piece Amendmend STOCK Pieces");
            aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentApproval_STOCK.frmPieceAmendApproval_STOCK", " Felt Sales Piece Amendmend STOCK Pieces");
            frmPieceAmendApproval_STOCK ObjItem = (frmPieceAmendApproval_STOCK) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 762) { //Felt Transporter Weight
            AppletFrame aFrame = new AppletFrame(" Felt Transporter Weight");
            aFrame.startAppletEx("EITLERP.FeltSales.FeltTransporterWeight.frmFeltTransporterWeigthEntryForm", "Felt Transporter Weight");
            frmFeltTransporterWeigthEntryForm ObjItem = (frmFeltTransporterWeigthEntryForm) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        //

        if (SelModule == 740) { //Felt Sales Piece Amendmend STOCK Pieces
            AppletFrame aFrame = new AppletFrame(" Felt Sales Bale Reopen");
            aFrame.startAppletEx("EITLERP.FeltSales.ReopenBale12.frmFeltReopenBale", " Felt Sales Bale Reopen");
            frmFeltReopenBale ObjItem = (frmFeltReopenBale) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }

        if (SelModule == 732) { //Felt UNADJUSTED
            AppletFrame aFrame = new AppletFrame("Felt UnAdjusted TRN");
            aFrame.startAppletEx("EITLERP.Production.FeltUnadj.frmFeltUnadj", "Felt UnAdjusted TRN");
            frmFeltUnadj ObjItem = (frmFeltUnadj) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        if (SelModule == 99) { //Debit Memo
            AppletFrame aFrame = new AppletFrame("Debit Memo");
            aFrame.startAppletEx("EITLERP.Sales.DebitMemoReceiptMapping.frmDebitMemoReceiptMapping", "Debit Memo");
            frmDebitMemoReceiptMapping ObjItem = (frmDebitMemoReceiptMapping) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        if (SelModule == 774) { //WIP Piece Amend
            AppletFrame aFrame = new AppletFrame("WIP Piece Amend");
            aFrame.startAppletEx("EITLERP.FeltSales.PieceAmendmentWIP.frmPieceAmendWIP", "WIP Piece Amend");
            frmPieceAmendWIP ObjItem = (frmPieceAmendWIP) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        if (SelModule == 768) { //Budget Manual
            AppletFrame aFrame = new AppletFrame(" Budget Manual Entry");
            aFrame.startAppletEx("EITLERP.FeltSales.Budget.FrmBudgetManual", " Busdget Manual Entry");
            FrmBudgetManual ObjItem = (FrmBudgetManual) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        if (SelModule == 779) { //Budget Manual
            AppletFrame aFrame = new AppletFrame("Prior Approval for Diversion");
            aFrame.startAppletEx("EITLERP.FeltSales.DiversionLoss.FrmFeltDiversionLoss", "Prior Approval for Diversion");
            FrmFeltDiversionLoss ObjItem = (FrmFeltDiversionLoss) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
        if (SelModule == 790) { //Budget Manual
            AppletFrame aFrame = new AppletFrame("GIDC Production Process Entry");
            aFrame.startAppletEx("EITLERP.FeltSales.GIDC_SDF.ProductionProcess.FrmProductionEntry", " Busdget Manual Entry");
            FrmProductionEntry ObjItem = (FrmProductionEntry) aFrame.ObjApplet;
            ObjItem.Find(DocNo);
        }
    }

}
