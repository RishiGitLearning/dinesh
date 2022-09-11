/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.Order;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
//import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author  DAXESH PRAJAPATI
 *
 */
public class clsOrderValue {
//    static DecimalFormat f_single=new DecimalFormat("##.0");
//    static DecimalFormat f_double=new DecimalFormat("##.00");
    static DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    static DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    public static FeltInvCalc calculate(String Piece_No,String Product_Code,String Party_Code,Float Length,Float Width,Integer GSM,Float Weight,Float SQMT,String Order_Date)
    {
        FeltInvCalc inv_calc = new FeltInvCalc();
                    
        
        //Weight = ((Length * Width * GSM) / 1000);

        //SQMT = (Length * Width);
        
                    inv_calc.setFicPieceNo(Piece_No);
                    inv_calc.setFicProductCode(Product_Code);
                    inv_calc.setFicPartyCode(Party_Code);
                    
                    inv_calc.setFicLength(Length);
                    inv_calc.setFicWidth(Width);
                    inv_calc.setFicGsm(GSM);
                    inv_calc.setFicWeight(Weight);   
                    try {
                        inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
                    } catch (ParseException ex) {
                        //Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                   
                    String CATEGORY="",SQM_IND="",CHEM_TRT_IND="",PIN_IND="",SPR_IND="",SUR_IND="";
                    float SQM_RATE=0,SQM_CHRG=0,WT_RATE=0,CHARGES=0,CHEM_TRT_CHRG=0,PIN_CHRG=0,SPR_CHRG=0,SUR_CHRG=0;
                     
                        
                     try {
                            Connection Conn;
                            Statement  stmt;
                            ResultSet rsData;

                            Conn = data.getConn();
                            stmt = Conn.createStatement();
                            // System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
                            // rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");
                            
                            Product_Code = Product_Code.substring(0, 6);
                            //rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+Product_Code+"' AND EFFECTIVE_FROM <= '"+Order_Date+"' AND (EFFECTIVE_TO >= '"+Order_Date+"' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
                            //System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+Product_Code+"' AND EFFECTIVE_FROM <= '"+Order_Date+"' AND (EFFECTIVE_TO >= '"+Order_Date+"' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
                            rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+Product_Code+"' AND EFFECTIVE_FROM <= '"+EITLERPGLOBAL.getCurrentDateDB()+"' AND (EFFECTIVE_TO >= '"+EITLERPGLOBAL.getCurrentDateDB()+"' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
                            //                            
                            rsData.first();
                          
                            CATEGORY = rsData.getString(1);
                            SQM_RATE = rsData.getFloat(2);
                            WT_RATE = rsData.getFloat(3);
                            SQM_IND = rsData.getString(4);
                            SQM_CHRG =  rsData.getFloat(5);
                            CHEM_TRT_IND = rsData.getString(6);
                            CHEM_TRT_CHRG = rsData.getFloat(7);
                            PIN_IND = rsData.getString(8);
                            PIN_CHRG= rsData.getFloat(9);
                            SPR_IND = rsData.getString(10);
                            SPR_CHRG = rsData.getFloat(11);
                            SUR_IND = rsData.getString(12);
                            SUR_CHRG = rsData.getFloat(13);
                            CHARGES = rsData.getFloat(14);
                            
                     }catch(Exception e)
                     {
                         System.out.println("Error123 : "+e.getMessage());
                     }
                    
                    inv_calc.setFicSqMtr(SQMT);
                    
                    inv_calc.setFicProductCatg(CATEGORY);
                    
                    inv_calc.setFicRate(SQM_CHRG + WT_RATE);
                    
                    float base_amt =0;
                    switch (SQM_IND) {
                        case "1":
                            base_amt = inv_calc.getFicLength() * inv_calc.getFicWidth() *  inv_calc.getFicRate();
                            break;
                        case "0":
                            base_amt = inv_calc.getFicWeight() * inv_calc.getFicRate();
                            break;
                    }
                    inv_calc.setFicBasAmount(base_amt);
                    
                    inv_calc.setFicChemTrtChg(0);
                    switch (CHEM_TRT_IND) {
                        case "1":
                            inv_calc.setFicChemTrtChg(inv_calc.getFicWeight() * CHEM_TRT_CHRG);
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
                    
                    inv_calc.setFicSeamChg(inv_calc.getFicPinChg()+inv_calc.getFicSpiralChg()+inv_calc.getFicChemTrtChg());
                    
                   // FeltRateDiscountServiceImpl service_discount = new FeltRateDiscountServiceImpl();
                  //  FeltRateDiscMasterDetail rate_discount;
                    
                    inv_calc.setFicDiscPer(0);
                    float disc = 0;
  
                    if(Product_Code.equals("7290000"))
                    {
                             try {
                                   Connection Conn;
                                   Statement  stmt;
                                   ResultSet rsData;

                                   Conn = data.getConn();
                                   stmt = Conn.createStatement();
                                  // System.out.println("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+Order_Date+"\" AND (EFFECTIVE_TO >= \""+Order_Date+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                                   rsData = stmt.executeQuery("SELECT DISC_PER FROM PRODUCTION.FELT_RATE_DISC_MASTER_DETAIL WHERE APPROVED = \"1\" AND CANCELED= \"0\" AND PARTY_CODE = "+Party_Code+" AND PRODUCT_CODE = "+Product_Code+" AND EFFECTIVE_FROM <= \""+Order_Date+"\" AND (EFFECTIVE_TO >= \""+Order_Date+"\" or EFFECTIVE_TO >= \"0001-01-01\"  or EFFECTIVE_TO IS NULL)");
                                   rsData.first();
                                   disc = rsData.getFloat(1);
                                   System.out.println("Discount Percentage : "+disc);
                               }catch(Exception e)
                               {
                                   System.out.println("Error on fid Discount: "+e.getMessage());
                                   e.printStackTrace();
                               }
                    }
                    else
                    {
                        //System.out.println("Discount Not Calculated.. because Discount is only applicable for PRODUCT - 7290000");
                    }
                            
                            
                    inv_calc.setFicDiscPer(disc);
                    
                    float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer())/100;
                    inv_calc.setFicDiscAmt(disc_amt);
                    
                    float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
                    inv_calc.setFicDiscBasamt(disc_basamt);
                    
//                    float calc = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg();
//                    float excise = Float.parseFloat(f_double.format((calc*Float.parseFloat(12.50+"") / 100)));
//                    inv_calc.setFicExcise(excise);
                    inv_calc.setFicExcise(0);
                    
                    inv_calc.setFicInsInd(0);
                    
                    String strIns = "SELECT RIGHT(INSURANCE_CODE,1) AS INSURANCE_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE=210010 AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "' AND APPROVED=1 AND CANCELLED=0";
                    int ins_ind = data.getIntValueFromDB(strIns);
                    if (ins_ind == 1) {
                        inv_calc.setFicInsInd(1);
                    }
                    float ins_amt = 0;
                    if(inv_calc.getFicInsInd() == 1)
                    {
                      // ins_amt  = Float.parseFloat((Math.round((inv_calc.getFicDiscBasamt()+inv_calc.getFicExcise()+inv_calc.getFicSeamChg()))* 0.10 * 0.0039)+"");
                        ins_amt = Float.parseFloat((((inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg()) * (1)) * 1.1 ) + "");//0.10 * 0.0039) + "");
                        ins_amt = (int) (ins_amt + 999) / 1000;
                        ins_amt = Float.parseFloat(((ins_amt * 3.9) + 0.05) + "");
                    }
                    
                    inv_calc.setFicInsAmt(Float.parseFloat(EITLERPGLOBAL.round(ins_amt,2)+""));
                    
                    float inv_amt = Math.round(inv_calc.getFicDiscBasamt()  + inv_calc.getFicSeamChg() + inv_calc.getFicInsAmt());

                   // float GST = (inv_amt)*12/100;
                   // inv_calc.setGST(GST);
                    
                    float VAT = 0;
                    float CST = 0;
//                    if(Party_Code.startsWith("831"))
//                    {
//                        VAT = (inv_amt)*4/100;
//                    }
//                    else
//                    {
//                        CST = (inv_amt)*2/100;
//                    }
                    inv_calc.setCst(CST);
                    inv_calc.setVat(VAT);
                    
                    float SD=0;
//                    String strSQL_NEW = "SELECT * FROM FINANCE.D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='132802' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "'";
//                    
//                    try {
//                        ResultSet rsTmp = data.getResult(strSQL_NEW);
//                        rsTmp.first();
//
//                        if(rsTmp.getFetchSize() != 0)
//                        {
//                               SD = (inv_amt) * 3 / 100;
//                        }
//                        
//                        rsTmp.next();
//                    } catch (Exception e) {
//
//                    }
                   
                    float IGST = 0;
                    float CGST = 0;
                    float SGST = 0;
                    float Composition = 0;
                    float RCM = 0;
                    float GST_COMPENSATION_CESS = 0;
                    
                    inv_calc.setCGST_PER(0);
                    inv_calc.setSGST_PER(0);
                    inv_calc.setIGST_PER(0);
                    inv_calc.setRCM_PER(0);
                    inv_calc.setGST_COMPENSATION_CESS_PER(0);


                    double GST_PER = data.getDoubleValueFromDB("SELECT TAX_PER FROM PRODUCTION.TAX_MASTER WHERE TAX_CODE='GST12'");

                    float GST = Float.parseFloat(EITLERPGLOBAL.round((inv_amt * Float.parseFloat(GST_PER + "") / 100),2)+"");

            //        String state_id = data.getStringValueFromDB("SELECT STATE_ID FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");
            //                
            //        if (Party_Code.startsWith("831")) {
            //            inv_calc.setFicCGSTPER(Float.parseFloat((GST_PER/2) + ""));
            //            inv_calc.setFicSGSTPER(Float.parseFloat((GST_PER/2) + ""));
            //            CGST = Float.valueOf(GST/2);
            //            SGST = Float.valueOf(GST/2);
            //        } else {
            //            inv_calc.setFicIGSTPER(Float.parseFloat((GST_PER) + ""));
            //            IGST = GST;
            //        }

                    String state_gst_id = data.getStringValueFromDB("SELECT STATE_GST_CODE FROM DINESHMILLS.D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+Party_Code+"' AND MAIN_ACCOUNT_CODE=210010 AND COMPANY_ID=2");

                    if (state_gst_id.equalsIgnoreCase("")) {
                        IGST = 0;
                        CGST = 0;
                        SGST = 0;
                        Composition = 0;
                        RCM = 0;
                        GST_COMPENSATION_CESS = 0;
                    
                    } else {
                        if (state_gst_id.equalsIgnoreCase("24")) {
                            
                            inv_calc.setCGST_PER(Float.parseFloat((GST_PER / 2) + ""));
                            inv_calc.setSGST_PER(Float.parseFloat((GST_PER / 2) + ""));
                            inv_calc.setIGST_PER(0);
                            inv_calc.setRCM_PER(0);
                            inv_calc.setGST_COMPENSATION_CESS_PER(0);
                            
                            CGST = GST / 2;
                            SGST = GST / 2;
                        } else {
                            
                            inv_calc.setCGST_PER(0);
                            inv_calc.setSGST_PER(0);
                            inv_calc.setIGST_PER(Float.parseFloat((GST_PER) + ""));
                            inv_calc.setRCM_PER(0);
                            inv_calc.setGST_COMPENSATION_CESS_PER(0);
                            
                            IGST = GST;
                        }
                    }

                    CGST = Math.round(CGST);
                    SGST = Math.round(SGST);
                    IGST = Math.round(IGST);
                    RCM = Math.round(RCM);
                    Composition = Math.round(Composition);
                    GST_COMPENSATION_CESS = Math.round(GST_COMPENSATION_CESS);

                    inv_calc.setCGST(CGST);
                    inv_calc.setSGST(SGST);
                    inv_calc.setIGST(IGST);
                    inv_calc.setRCM(RCM);
                    inv_calc.setCOMPOSITION(Composition);
                    inv_calc.setGST_COMPENSATION_CESS(GST_COMPENSATION_CESS);
                    
                    inv_calc.setSD(SD);
                    
                    inv_amt = inv_amt + SD + CGST+ SGST + IGST + RCM + Composition + GST_COMPENSATION_CESS;
                    
                    inv_calc.setFicInvAmt(inv_amt);
                    
                    return inv_calc;
                    
    }
    
}
