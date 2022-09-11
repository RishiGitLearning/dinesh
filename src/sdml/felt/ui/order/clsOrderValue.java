/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.ui.order;

import EITLERP.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
//import sdml.felt.models.existing.DSalPartyMaster;
//import sdml.felt.models.production.FeltRateDiscMasterDetail;
//import sdml.felt.models.production.FeltRateMaster;
//import sdml.felt.services.impl.PartyMasterServiceImpl;
//import sdml.felt.services.production.impl.FeltRateDiscountServiceImpl;
//import sdml.felt.services.production.impl.FeltRateMasterServiceImpl;

/**
 *
 * @author root
 */
public class clsOrderValue {
    static DecimalFormat f_single=new DecimalFormat("##.0");
    static DecimalFormat f_double=new DecimalFormat("##.00");
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
                    try {
                        inv_calc.setFicMemoDate(df1.parse("0000-00-00"));
                    } catch (ParseException ex) {
//                        Logger.getLogger(FeltOrderEntry.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                   
                    String CATEGORY="",SQM_IND="",CHEM_TRT_IND="",PIN_IND="",SPR_IND="";
                    float SQM_RATE=0,WT_RATE=0,CHARGES=0;
                     try {
                            Connection Conn;
                            Statement  stmt;
                            ResultSet rsData;

                            Conn = data.getConn();
                            stmt = Conn.createStatement();
                            rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IND,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");
                            rsData.first();
                            CATEGORY = rsData.getString(1);
                            SQM_RATE = rsData.getFloat(2);
                            WT_RATE = rsData.getFloat(3);
                            SQM_IND = rsData.getString(4);
                            CHEM_TRT_IND = rsData.getString(5);
                            CHARGES = rsData.getFloat(6);
                            PIN_IND = rsData.getString(7);
                            SPR_IND = rsData.getString(8);
                            
                            
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
                            inv_calc.setFicChemTrtChg(inv_calc.getFicWeight() * CHARGES);
                            break;
                        case "0":
                            inv_calc.setFicChemTrtChg(0);
                            break;
                    }
                    
                    inv_calc.setFicPinChg(0);
                    switch (PIN_IND) {
                        case "1":
                            inv_calc.setFicPinChg(inv_calc.getFicWidth() * CHARGES);
                            break;
                        case "0":
                            inv_calc.setFicPinChg(0);
                            break;
                    }
                    
                    inv_calc.setFicSpiralChg(0);
                    switch (SPR_IND) {
                        case "1":
                            inv_calc.setFicSpiralChg(inv_calc.getFicWidth() * CHARGES);
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
                    
//                        rate_discount = service_discount.getDiscount(Party_Code, Product_Code, Order_Date);
//                        if(rate_discount.getDiscPer() == null)
//                        {
//                            disc = 0;
//                        }
//                        else
//                        {
//                            disc = Float.parseFloat(rate_discount.getDiscPer()+"");
//                        }    
                    
//                      try {
//                            Connection Conn;
//                            Statement  stmt;
//                            ResultSet rsData;
//
//                            Conn = data.getConn();
//                            stmt = Conn.createStatement();
//                            rsData = stmt.executeQuery("SELECT CATEGORY,SQM_RATE,WT_RATE,SQM_IND,CHEM_TRT_IND,CHARGES,PIN_IND,SPR_IND FROM PRODUCTION.FELT_RATE_MASTER where ITEM_CODE = '"+Product_Code+"'");
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
                      
                    inv_calc.setFicDiscPer(disc);
                    
                    float disc_amt = (inv_calc.getFicBasAmount() * inv_calc.getFicDiscPer())/100;
                    inv_calc.setFicDiscAmt(disc_amt);
                    
                    float disc_basamt = (inv_calc.getFicBasAmount() - inv_calc.getFicDiscAmt());
                    inv_calc.setFicDiscBasamt(disc_basamt);
                    
                    float calc = inv_calc.getFicDiscBasamt() + inv_calc.getFicSeamChg();
                    float excise = Float.parseFloat(f_double.format((calc*Float.parseFloat(12.50+"") / 100)));
                    inv_calc.setFicExcise(excise);
                    
                    
                    inv_calc.setFicInsInd(0);
                    //PartyMasterServiceImpl service_party =new PartyMasterServiceImpl();
                    //DSalPartyMaster party = service_party.getFeltPartyDetail(Party_Code);
//                    if(party != null)
//                    {
//                            if(party.getInsuranceCode() == null)
//                            {
//                                 inv_calc.setFicInsInd(0);
//
//                            }
//                            else
//                            {
//                               if(party.getInsuranceCode().equals(""))
//                                {
//                                    inv_calc.setFicInsInd(0);
//                                }
//                                else
//                                {
//                                    inv_calc.setFicInsInd(Integer.parseInt(party.getInsuranceCode()));
//                                }
//                            }
//                    }
                    float ins_amt = 0;
                    if(inv_calc.getFicInsInd() == 1)
                    {
                       ins_amt  = Float.parseFloat((Math.round((inv_calc.getFicDiscBasamt()+inv_calc.getFicExcise()+inv_calc.getFicSeamChg()))* 0.10 * 0.0039)+"");
                    }
                    inv_calc.setFicInsAmt(Float.parseFloat(f_double.format(ins_amt)));
                    
                    float inv_amt = Math.round(inv_calc.getFicDiscBasamt() + inv_calc.getFicExcise() + inv_calc.getFicSeamChg() + inv_calc.getFicInsAmt());
                    System.out.println("Disc Base Amt = "+inv_calc.getFicDiscBasamt());
                    System.out.println("Excise = "+inv_calc.getFicExcise());
                    
                    inv_calc.setFicInvAmt(inv_amt);
                    
                    return inv_calc;
    }
    
}
