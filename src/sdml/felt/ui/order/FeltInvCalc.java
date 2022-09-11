package sdml.felt.ui.order;


import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author root
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
    
    private float ficDiscPer;
    
    private float ficDiscAmt;
    
    private float ficDiscBasamt;
    
    private Date ficMemoDate;
   
    private float ficInvAmt;

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
        this.ficInsInd = ficInsInd;
        this.ficInsAmt = ficInsAmt;
        this.ficExcise = ficExcise;
        this.ficDiscPer = ficDiscPer;
        this.ficDiscAmt = ficDiscAmt;
        this.ficDiscBasamt = ficDiscBasamt;
        this.ficMemoDate = ficMemoDate;
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

    
}
