/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.OrderDiversion;

import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
//import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
//import sdml.felt.models.existing.DSalPartyMaster;
//import sdml.felt.models.production.FeltRateDiscMasterDetail;
//import sdml.felt.models.production.FeltRateMaster;
//import sdml.felt.services.impl.PartyMasterServiceImpl;
//import sdml.felt.services.production.impl.FeltRateDiscountServiceImpl;
//import sdml.felt.services.production.impl.FeltRateMasterServiceImpl;

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
                    
                    inv_calc.setFicPieceNo(Piece_No);
                    inv_calc.setFicProductCode(Product_Code);
                    inv_calc.setFicPartyCode(Party_Code);
                    
                    inv_calc.setFicLength(Length);
                    inv_calc.setFicWidth(Width);
                    inv_calc.setFicGsm(GSM);
                    inv_calc.setFicWeight(Weight);   
                    
//                    String CATEGORY="",SQM_IND="",CHEM_TRT_IND="",PIN_IND="",SPR_IND="";
//                    float SQM_RATE=0,WT_RATE=0,CHARGES=0;
//                     try {
//                            Connection Conn;
//                            Statement  stmt;
//                            ResultSet rsData;
//
//                            Conn = data.getConn();
//                            stmt = Conn.createStatement();
//                            System.out.println("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");        
//                            rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IN,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");
//                            rsData.first();
//                            CATEGORY = rsData.getString(1);
//                            SQM_RATE = rsData.getFloat(2);
//                            WT_RATE = rsData.getFloat(3);
//                            SQM_IND = rsData.getString(4);
//                            CHEM_TRT_IND = rsData.getString(5);
//                            CHARGES = rsData.getFloat(6);
//                            PIN_IND = rsData.getString(7);
//                            SPR_IND = rsData.getString(8);
//                            
//                            
//                     }catch(Exception e)
//                     {
//                         System.out.println("Error : "+e.getMessage());
//                     }
                    
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
                            rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,SQM_CHRG,CHEM_TRT_IND,CHEM_TRT_CHRG,PIN_IND,PIN_CHRG,SPR_IND,SPR_CHRG,SUR_IND,SUR_CHRG,CHARGES FROM PRODUCTION.FELT_QLT_RATE_MASTER WHERE PRODUCT_CODE='"+Product_Code+"' AND EFFECTIVE_FROM <= '"+Order_Date+"' AND (EFFECTIVE_TO >= '"+Order_Date+"' or EFFECTIVE_TO >= \"0000-00-00\"  or EFFECTIVE_TO IS NULL)");
                           
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
                         System.out.println("Error : "+e.getMessage());
                     }
                    
                    inv_calc.setFicSqMtr(SQMT);
                    
                    inv_calc.setFicProductCatg(CATEGORY);
                    
                    inv_calc.setFicRate(SQM_RATE + WT_RATE);
                    
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
                    
                    float inv_amt = inv_calc.getFicBasAmount() + inv_calc.getFicSeamChg();
                    
//                    float VAT = 0;
//                    float CST = 0;
//           
//                    if(Party_Code.startsWith("831"))
//                    {
//                        VAT = (inv_amt)*4/100;
//                    }
//                    else
//                    {
//                        CST = (inv_amt)*2/100;
//                    }
//                    
//                    inv_calc.setCst(CST);
//                    inv_calc.setVat(VAT);
                    
//                    float SD=0;
//                    String strSQL_NEW = "SELECT * FROM FINANCE.D_FIN_PARTY_MASTER WHERE MAIN_ACCOUNT_CODE='132802' AND PARTY_CODE='" + inv_calc.getFicPartyCode() + "'";
//                    System.out.println(" OUTPUT : "+strSQL_NEW);
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
//                    
//                    inv_calc.setSD(SD);
                    
//                    inv_amt = inv_amt + SD +  CST + VAT;
                    
                    inv_calc.setFicInvAmt(inv_amt);
                    
                    return inv_calc;
                    
    }
    
}
