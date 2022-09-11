package EITLERP.FeltSales.OrderDiversion;


import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author  DAXESH PRAJAPATI
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
   
    private float ficInvAmt;
    
    private float cst;
    
    private float vat;
    
    private float SD;

    public FeltInvCalc() {
    }

    public FeltInvCalc( float ficLength, float ficWidth, int ficGsm, float ficSqMtr, float ficWeight, String ficProductCatg, float ficRate, float ficBasAmount, float ficChemTrtChg, float ficSpiralChg, float ficPinChg, float ficSeamChg, int ficInsInd, float ficInsAmt, float ficExcise, float ficDiscPer, float ficDiscAmt, float ficDiscBasamt, Date ficMemoDate, float ficInvAmt) {
      
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
        this.ficInvAmt = ficInvAmt;
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

    public float getVat() {
        return vat;
    }

    public void setVat(float vat) {
        this.vat = vat;
    }

    public float getSD() {
        return SD;
    }

    public void setSD(float SD) {
        this.SD = SD;
    }

    
}
