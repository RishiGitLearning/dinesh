package EITLERP.FeltSales.common;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author DAXESH PRAJAPATI
 *
 */
public class FeltInvCalc implements Serializable {

    private String ficPieceNo;

    private String ficProductCode;

    private String ficPartyCode;

    private float ficLength;

    private float ficWidth;

    private int ficGsm;

    private float ficSqMtr;

    private float ficWeight;

    private String ficProductCatg;

    private float ficRate;

    private float ficBasAmount;

    private float ficChemTrtChg;

    private float ficSpiralChg;

    private float ficPinChg;

    private float ficSeamChg;

    private int ficInsInd;

    private float ficInsAmt;

    private float ficExcise;

    private float ficGST;

    private float ficIGST;

    private float ficSGST;

    private float ficCGST;

    private float ficIGSTPER;

    private float ficCGSTPER;

    private float ficSGSTPER;

    private float ficDiscPer;

    private float ficDiscAmt;

    private float ficDiscBasamt;

    private Date ficMemoDate;

    private float ficInvAmt;

    private float cst;

    private float cst2;

    private float cst5;

    private float vat;

    private float vat1;

    private float vat4;

    private float SD;

    private String reason;

    private float ficSeamPer;

    private float ficSeamAmt;

    private String sanc_group;
    
    private String sanc_doc;
    
    private float aosd_per;
    
    private float aosd_amt;
    
    private float ficYearEndPer;
    
    private float ficSeamValue;
    
    private float ficYearEndSeam;
    
    private float ficNetSeamUnit;
    
    private float ficOldRate;
    
    private float ficSurcharge_per;
    
    private float ficSurcharge_rate;
    
    private float ficGrossRate;
    
    private float tcs_per;
    
    private float tcs_amt;

    public float getTCS_per() {
        return tcs_per;
    }

    public void setTCS_per(float tcs_per) {
        this.tcs_per = tcs_per;
    }

    public float getTCS_amt() {
        return tcs_amt;
    }

    public void setTCS_amt(float tcs_amt) {
        this.tcs_amt = tcs_amt;
    }
    
    
    public float getFicYearEndPer() {
        return ficYearEndPer;
    }

    public void setFicYearEndPer(float ficYearEndPer) {
        this.ficYearEndPer = ficYearEndPer;
    }

    public String getSanc_doc() {
        return sanc_doc;
    }

    public void setSanc_doc(String sanc_doc) {
        this.sanc_doc = sanc_doc;
    }

    public String getSanc_group() {
        return sanc_group;
    }

    public void setSanc_group(String sanc_group) {
        this.sanc_group = sanc_group;
    }

    public FeltInvCalc() {
    }

    public FeltInvCalc(float ficLength, float ficWidth, int ficGsm, float ficSqMtr, float ficWeight, String ficProductCatg, float ficRate, float ficBasAmount, float ficChemTrtChg, float ficSpiralChg, float ficPinChg, float ficSeamChg, int ficInsInd, float ficInsAmt, float ficExcise, float ficGST, float ficIGST, float ficSGST, float ficCGST, float ficIGSTPER, float ficCGSTPER, float ficSGSTPER, float ficDiscPer, float ficDiscAmt, float ficDiscBasamt, Date ficMemoDate, float ficInvAmt, float ficSeamPer, float ficSeamAmt, float ficYearEndPer, float ficSeamValue, float ficYearEndSeam, float ficNetSeamUnit, float ficOldRate, float ficSurcharge_per, float ficSurcharge_rate, float ficGrossRate, float tcs_per, float tcs_amt) {

        this.ficLength = ficLength;
        this.ficWidth = ficWidth;
        this.ficGsm = ficGsm;
        this.ficSqMtr = ficSqMtr;
        this.ficWeight = ficWeight;
        this.ficProductCatg = ficProductCatg;
        this.ficRate = ficRate;
        this.ficBasAmount = ficBasAmount;
        this.ficChemTrtChg = ficChemTrtChg;
        this.ficSpiralChg = ficSpiralChg;
        this.ficPinChg = ficPinChg;
        this.ficSeamChg = ficSeamChg;
        this.ficInsInd = ficInsInd;
        this.ficInsAmt = ficInsAmt;
        this.ficExcise = ficExcise;
        this.ficGST = ficGST;
        this.ficIGST = ficIGST;
        this.ficSGST = ficSGST;
        this.ficCGST = ficCGST;
        this.ficIGSTPER = ficIGSTPER;
        this.ficCGSTPER = ficCGSTPER;
        this.ficSGSTPER = ficSGSTPER;
        this.ficDiscPer = ficDiscPer;
        this.ficDiscAmt = ficDiscAmt;
        this.ficDiscBasamt = ficDiscBasamt;
        this.ficMemoDate = ficMemoDate;
        this.ficInvAmt = ficInvAmt;
        this.ficSeamPer = ficSeamPer;
        this.ficSeamAmt = ficSeamAmt;
        this.ficYearEndPer = ficYearEndPer;
        this.ficSeamValue = ficSeamValue;
        this.ficYearEndSeam = ficYearEndSeam;
        this.ficNetSeamUnit = ficNetSeamUnit;
        this.ficOldRate = ficOldRate;
        this.ficSurcharge_per = ficSurcharge_per;
        this.ficSurcharge_rate = ficSurcharge_rate;
        this.ficGrossRate = ficGrossRate;
        this.tcs_per = tcs_per;
        this.tcs_amt = tcs_amt;
        
    }

    public String getFicPieceNo() {
        return ficPieceNo;
    }

    public void setFicPieceNo(String ficPieceNo) {
        this.ficPieceNo = ficPieceNo;
    }

    public String getFicProductCode() {
        return ficProductCode;
    }

    public void setFicProductCode(String ficProductCode) {
        this.ficProductCode = ficProductCode;
    }

    public String getFicPartyCode() {
        return ficPartyCode;
    }

    public void setFicPartyCode(String ficPartyCode) {
        this.ficPartyCode = ficPartyCode;
    }

    public float getFicLength() {
        return ficLength;
    }

    public void setFicLength(float ficLength) {
        this.ficLength = ficLength;
    }

    public float getFicWidth() {
        return ficWidth;
    }

    public void setFicWidth(float ficWidth) {
        this.ficWidth = ficWidth;
    }

    public int getFicGsm() {
        return ficGsm;
    }

    public void setFicGsm(int ficGsm) {
        this.ficGsm = ficGsm;
    }

    public float getFicSqMtr() {
        return ficSqMtr;
    }

    public void setFicSqMtr(float ficSqMtr) {
        this.ficSqMtr = ficSqMtr;
    }

    public float getFicWeight() {
        return ficWeight;
    }

    public void setFicWeight(float ficWeight) {
        this.ficWeight = ficWeight;
    }

    public String getFicProductCatg() {
        return ficProductCatg;
    }

    public void setFicProductCatg(String ficProductCatg) {
        this.ficProductCatg = ficProductCatg;
    }

    public float getFicRate() {
        return ficRate;
    }

    public void setFicRate(float ficRate) {
        this.ficRate = ficRate;
    }

    public float getFicBasAmount() {
        return ficBasAmount;
    }

    public void setFicBasAmount(float ficBasAmount) {
        this.ficBasAmount = ficBasAmount;
    }

    public float getFicChemTrtChg() {
        return ficChemTrtChg;
    }

    public void setFicChemTrtChg(float ficChemTrtChg) {
        this.ficChemTrtChg = ficChemTrtChg;
    }

    public float getFicSpiralChg() {
        return ficSpiralChg;
    }

    public void setFicSpiralChg(float ficSpiralChg) {
        this.ficSpiralChg = ficSpiralChg;
    }

    public float getFicPinChg() {
        return ficPinChg;
    }

    public void setFicPinChg(float ficPinChg) {
        this.ficPinChg = ficPinChg;
    }

    public float getFicSeamChg() {
        return ficSeamChg;
    }

    public void setFicSeamChg(float ficSeamChg) {
        this.ficSeamChg = ficSeamChg;
    }

    public int getFicInsInd() {
        return ficInsInd;
    }

    public void setFicInsInd(int ficInsInd) {
        this.ficInsInd = ficInsInd;
    }

    public float getFicInsAmt() {
        return ficInsAmt;
    }

    public void setFicInsAmt(float ficInsAmt) {
        this.ficInsAmt = ficInsAmt;
    }

    public float getFicExcise() {
        return ficExcise;
    }

    public void setFicExcise(float ficExcise) {
        this.ficExcise = ficExcise;
    }

    public float getFicGST() {
        return ficGST;
    }

    public void setFicGST(float ficGST) {
        this.ficGST = ficGST;
    }

    public float getFicIGST() {
        return ficIGST;
    }

    public void setFicIGST(float ficIGST) {
        this.ficIGST = ficIGST;
    }

    public float getFicSGST() {
        return ficSGST;
    }

    public void setFicSGST(float ficSGST) {
        this.ficSGST = ficSGST;
    }

    public float getFicCGST() {
        return ficCGST;
    }

    public void setFicCGST(float ficCGST) {
        this.ficCGST = ficCGST;
    }

    public float getFicIGSTPER() {
        return ficIGSTPER;
    }

    public void setFicIGSTPER(float ficIGSTPER) {
        this.ficIGSTPER = ficIGSTPER;
    }

    public float getFicCGSTPER() {
        return ficCGSTPER;
    }

    public void setFicCGSTPER(float ficCGSTPER) {
        this.ficCGSTPER = ficCGSTPER;
    }

    public float getFicSGSTPER() {
        return ficSGSTPER;
    }

    public void setFicSGSTPER(float ficSGSTPER) {
        this.ficSGSTPER = ficSGSTPER;
    }

    public float getFicDiscPer() {
        return ficDiscPer;
    }

    public void setFicDiscPer(float ficDiscPer) {
        this.ficDiscPer = ficDiscPer;
    }

    public float getFicDiscAmt() {
        return ficDiscAmt;
    }

    public void setFicDiscAmt(float ficDiscAmt) {
        this.ficDiscAmt = ficDiscAmt;
    }

    public float getFicDiscBasamt() {
        return ficDiscBasamt;
    }

    public void setFicDiscBasamt(float ficDiscBasamt) {
        this.ficDiscBasamt = ficDiscBasamt;
    }

    public Date getFicMemoDate() {
        return ficMemoDate;
    }

    public void setFicMemoDate(Date ficMemoDate) {
        this.ficMemoDate = ficMemoDate;
    }

    public float getFicInvAmt() {
        return ficInvAmt;
    }

    public void setFicInvAmt(float ficInvAmt) {
        this.ficInvAmt = ficInvAmt;
    }

    public float getCst() {
        return cst;
    }

    public void setCst(float cst) {
        this.cst = cst;
    }

    public float getCst2() {
        return cst2;
    }

    public void setCst2(float cst2) {
        this.cst2 = cst2;
    }

    public float getCst5() {
        return cst5;
    }

    public void setCst5(float cst5) {
        this.cst5 = cst5;
    }

    public float getVat() {
        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
    }

    public float getVat1() {
        return vat1;
    }

    public void setVat1(float vat1) {
        this.vat1 = vat1;
    }

    public float getVat4() {
        return vat4;
    }

    public void setVat4(float vat4) {
        this.vat4 = vat4;
    }

    public float getSD() {
        return SD;
    }

    public void setSD(float SD) {
        this.SD = SD;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public float getFicSeamPer() {
        return ficSeamPer;
    }

    public void setFicSeamPer(float ficSeamPer) {
        this.ficSeamPer = ficSeamPer;
    }

    public float getFicSeamAmt() {
        return ficSeamAmt;
    }

    public void setFicSeamAmt(float ficSeamAmt) {
        this.ficSeamAmt = ficSeamAmt;
    }
    
    public float getAosd_per() {
        return aosd_per;
    }

    public void setAosd_per(float aosd_per) {
        this.aosd_per = aosd_per;
    }
    
    public float getAosd_amt() {
        return aosd_amt;
    }

    public void setAosd_amt(float aosd_amt) {
        this.aosd_amt = aosd_amt;
    }

    public float getFicSeamValue() {
        return ficSeamValue;
    }

    public void setFicSeamValue(float ficSeamValue) {
        this.ficSeamValue = ficSeamValue;
    }

    public float getFicYearEndSeam() {
        return ficYearEndSeam;
    }

    public void setFicYearEndSeam(float ficYearEndSeam) {
        this.ficYearEndSeam = ficYearEndSeam;
    }

    public float getFicNetSeamUnit() {
        return ficNetSeamUnit;
    }

    public void setFicNetSeamUnit(float ficNetSeamUnit) {
        this.ficNetSeamUnit = ficNetSeamUnit;
    }

    public float getFicOldRate() {
        return ficOldRate;
    }

    public void setFicOldRate(float ficOldRate) {
        this.ficOldRate = ficOldRate;
    }

    public float getFicSurcharge_per() {
        return ficSurcharge_per;
    }

    public void setFicSurcharge_per(float ficSurcharge_per) {
        this.ficSurcharge_per = ficSurcharge_per;
    }

    public float getFicSurcharge_rate() {
        return ficSurcharge_rate;
    }

    public void setFicSurcharge_rate(float ficSurcharge_rate) {
        this.ficSurcharge_rate = ficSurcharge_rate;
    }

    public float getFicGrossRate() {
        return ficGrossRate;
    }

    public void setFicGrossRate(float ficGrossRate) {
        this.ficGrossRate = ficGrossRate;
    }   

}
