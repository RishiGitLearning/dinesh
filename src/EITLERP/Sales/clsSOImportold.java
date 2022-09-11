/*
 * clsSOImport.java
 *
 * Created on July 21, 2008, 1:31 PM
 */

package EITLERP.Sales;
import EITLERP.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import EITLERP.Finance.UtilFunctions;

/**
 *
 * @author  Prathmesh Shah
 * DON'T ASK QUESTIONS IF YOU FIND ANY ERRORS. QUESTIONS ARE STRICTLY PROHIBITED.
 */
public class clsSOImportold {
    
    /** Creates a new instance of clsSOImport */
    public String NewOrdNO="";
    public int totalUnit=0;
    public int of41totalUnit=0;
    public clsSOImportold() {
        System.gc();
    }
    public static void main(String args[]) {
        try {
            
            /*if(args.length<=0) {
                System.out.println("Please specify the directory to put exported file");
                return;
            }*/
            
            clsSOImport objImport=new clsSOImport();
            
            //objImport.ImportSalesOrders(args[0]);
            objImport.ImportSalesOrders("/data/nisarg");
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean ImportSalesOrders(String InitialDir) {
        try {
            
            String dbURL="jdbc:mysql://localhost:3306/DINESHMILLS";
            data.OpenGlobalConnection(dbURL);
            Connection objConn=data.getConn();
            
            String FileName="im"+Integer.toString(EITLERPGLOBAL.getCurrentYear())+Integer.toString(EITLERPGLOBAL.getCurrentMonth())+Integer.toString(EITLERPGLOBAL.getCurrentDay());
            BufferedReader objBR=  new BufferedReader(new FileReader(InitialDir+"/"+FileName+".csv"));
            
            Statement stOrder=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOrder=stOrder.executeQuery("SELECT * FROM D_SAL_ORDER_HEADER");
            
            
            Statement stTmp=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            int delete = stTmp.executeUpdate("DELETE FROM D_SAL_OF41");
            if (delete==0) {
                System.out.println("All Rows are deleted");
            }
            
            ResultSet rsTmp=stTmp.executeQuery("SELECT * FROM D_SAL_OF41");
            int Counter =1;
            System.out.println("Importing Records...... ");
            try {
                while(true) {
                    String Line= objBR.readLine();
                    String[] LineCol=Line.split("~");
                    
                    rsTmp.moveToInsertRow();
                    rsTmp.updateString("UPDT_CODE", LineCol[0]);
                    rsTmp.updateString("TRANS_CODE", LineCol[1]);
                    rsTmp.updateString("PARTY_CODE", LineCol[2]);
                    rsTmp.updateString("ORDER_NO", LineCol[3]);
                    rsTmp.updateString("ORDER_DATE", LineCol[4]);
                    rsTmp.updateString("STATION", LineCol[5]);
                    rsTmp.updateString("QLTY_NO", LineCol[6]);
                    rsTmp.updateString("TOTL_UNITS", LineCol[7]);
                    rsTmp.updateString("UNIT_CODE", LineCol[8]);
                    rsTmp.updateString("DSCNT", LineCol[9]);
                    rsTmp.updateString("DLY_FRM", LineCol[10]);
                    rsTmp.updateString("DLY_TO", LineCol[11]);
                    
                    rsTmp.updateString("SH1", LineCol[12]);
                    rsTmp.updateString("UNITS1", LineCol[13]);
                    
                    rsTmp.updateString("SH2", LineCol[14]);
                    rsTmp.updateString("UNITS2", LineCol[15]);
                    
                    
                    rsTmp.updateString("SH3", LineCol[16]);
                    rsTmp.updateString("UNITS3", LineCol[17]);
                    
                    rsTmp.updateString("SH4", LineCol[18]);
                    rsTmp.updateString("UNITS4", LineCol[19]);
                    
                    
                    rsTmp.updateString("SH5", LineCol[20]);
                    rsTmp.updateString("UNITS5", LineCol[21]);
                    
                    rsTmp.updateString("SH6", LineCol[22]);
                    rsTmp.updateString("UNITS6", LineCol[23]);
                    
                    rsTmp.updateString("SH7", LineCol[24]);
                    rsTmp.updateString("UNITS7", LineCol[25]);
                    
                    rsTmp.updateString("SH8", LineCol[26]);
                    rsTmp.updateString("UNITS8", LineCol[27]);
                    
                    rsTmp.updateString("SH9", LineCol[28]);
                    rsTmp.updateString("UNITS9", LineCol[29]);
                    
                    rsTmp.updateString("SH10", LineCol[30]);
                    rsTmp.updateString("UNITS10", LineCol[31]);
                    
                    rsTmp.updateString("TRANS_MODE",LineCol[32]);
                    rsTmp.updateString("FILE_NO", LineCol[33]);
                    
                    if(LineCol.length>34) {
                        rsTmp.updateString("SEL_IND", LineCol[34]);
                    }
                    
                    if(LineCol.length>35) {
                        rsTmp.updateString("PST_NO", LineCol[35]);
                    }
                    rsTmp.updateInt("KEY", Counter);
                    
                    rsTmp.insertRow();
                    Counter++;
                }
            }
            catch(Exception ex) {
            }
            
            
            try {
                
                Statement stTmp1=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet rsTmp1=stTmp1.executeQuery("SELECT ORDER_NO, PARTY_CODE FROM D_SAL_OF41 GROUP BY ORDER_NO, PARTY_CODE");
                rsTmp1.first();
                
                {
                    while (!rsTmp1.isAfterLast()) {
                        String OrdNO = rsTmp1.getString("ORDER_NO");
                        String PartyCD = rsTmp1.getString("PARTY_CODE");
                        
                        String AreaID="";
                        int AreaUserID=0;
                        int UserFFID=0;
                        
                        stTmp=objConn.createStatement();
                        rsTmp=stTmp.executeQuery("SELECT AREA_ID FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+PartyCD+"'");
                        rsTmp.first();
                        if(rsTmp.getRow()>0) {
                            AreaID=rsTmp.getString("AREA_ID");
                        }
                        
                        
                        stTmp=objConn.createStatement();
                        rsTmp=stTmp.executeQuery("SELECT USER_ID FROM D_SAL_USER_AREA_MAPPING WHERE AREA_ID='"+AreaID+"'");
                        rsTmp.first();
                        if(rsTmp.getRow()>0) {
                            AreaUserID=rsTmp.getInt("USER_ID");
                        }
                        
                        if(AreaUserID>0) {
                            
                            stTmp=objConn.createStatement();
                            rsTmp=stTmp.executeQuery("SELECT FF_ID FROM D_SAL_DOC_PREFIX WHERE USER_ID="+AreaUserID);
                            rsTmp.first();
                            if(rsTmp.getRow()>0) {
                                UserFFID=rsTmp.getInt("FF_ID");
                            }
                            
                        }
                        
                        
                        if(UserFFID==0) {
                            UserFFID= clsFirstFree.getDefaultFirstFreeNo(EITLERPGLOBAL.gCompanyID, 82);
                            NewOrdNO= clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 82, UserFFID, true);
                            
                        }
                        else {
                            NewOrdNO= clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 82, UserFFID, true);
                        }
                        Statement stOrdHead=objConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
                        ResultSet rsOrdHead=stOrdHead.executeQuery("SELECT * FROM D_SAL_ORDER_HEADER WHERE OLD_ORDER_NO ='" +OrdNO +"' AND PARTY_CODE ='"+PartyCD+"' ");
                        // Add by prath
                        rsOrdHead.first();
                        
                        if (rsOrdHead.getRow()>0) {
                            System.out.println("ORDER NO: "+ OrdNO +" Already Exists. APPEND NEW ORDER");
                        }
                        else {
                            postHeaderOrder(OrdNO, PartyCD);
                            postDetailOrder(OrdNO, PartyCD);
                        }
                        System.out.println("\n\n");
                        rsTmp1.next();
                    }
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            
            System.out.println("THE END");
            
            
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    private void postHeaderOrder(String OrdNO, String PartyCD) {
        try {
            Connection objConn1=data.getConn();
            
            String strsql="SELECT  * FROM D_SAL_OF41 A LEFT JOIN D_SAL_PARTY_MASTER B ON (A.PARTY_CODE=B.PARTY_CODE) WHERE ORDER_NO='"+OrdNO+"' AND A.PARTY_CODE='"+PartyCD+"'";
            
            Statement stOrdHead=objConn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsOrdHead=stOrdHead.executeQuery(strsql);
            rsOrdHead.first();
            Statement stmt=objConn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsTmp=stmt.executeQuery("SELECT * FROM D_SAL_ORDER_HEADER LIMIT 1");
            
            rsTmp.moveToInsertRow();
            rsTmp.updateLong("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            
            rsTmp.updateString("ORDER_NO", NewOrdNO);
            rsTmp.updateString("OLD_ORDER_NO", OrdNO);
            rsTmp.updateString("ORDER_DATE", ConvertDateDB(UtilFunctions.getString(rsOrdHead,"ORDER_DATE","")));
            rsTmp.updateString("PARTY_CODE", PartyCD);
            rsTmp.updateString("TIN_NO",  UtilFunctions.getString(rsOrdHead, "TIN_NO",""));
            rsTmp.updateString("TIN_DATE", ConvertDateDB(UtilFunctions.getString(rsOrdHead,"TIN_DATE","")));
            rsTmp.updateInt("BANK_ID", Integer.parseInt(UtilFunctions.getString(rsOrdHead,"BANK_ID","0")));
            rsTmp.updateString("BANK_NAME", UtilFunctions.getString(rsOrdHead,"BANK_NAME",""));
            rsTmp.updateString("BANK_CITY", UtilFunctions.getString(rsOrdHead,"BANK_CITY",""));
            rsTmp.updateInt("TRANSPORTER_ID", Integer.parseInt( UtilFunctions.getString(rsOrdHead,"TRANSPORTER_ID","0")));
            rsTmp.updateString("TRANSPORTER_NAME", UtilFunctions.getString(rsOrdHead,"TRANSPORTER_NAME",""));
            rsTmp.updateString("CITY_ID", UtilFunctions.getString(rsOrdHead,"CITY_ID",""));
            rsTmp.updateString("PINCODE", UtilFunctions.getString(rsOrdHead,"PINCODE",""));
            rsTmp.updateString("CHARGE_CODE",UtilFunctions.getString(rsOrdHead,"CHARGE_CODE",""));
            rsTmp.updateString("DOCUMENT_THROUGH", UtilFunctions.getString(rsOrdHead,"DOCUMENT_THROUGH",""));
            rsTmp.updateInt("CREDIT_DAYS", Integer.parseInt(UtilFunctions.getString(rsOrdHead,"CREDIT_DAYS","0")));
            rsTmp.updateString("STATUS", "M");
            rsTmp.updateInt("APPROVED", 1);
            rsTmp.updateString("APPROVED_DATE", EITLERPGLOBAL.getCurrentDateDB());            
            
            rsTmp.insertRow();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void postDetailOrder(String OrdNO, String PartyCD) {
        try {
            Connection objConn2=data.getConn();
            Statement stmt=objConn2.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            String sqlqry = "SELECT A.ORDER_NO, A.PARTY_CODE, B.SUB_SR_NO, B.BRAND_ID, SUBSTRING(A.QLTY_NO,1,6) AS QLTY, B.EX_MILL_RATE, A.UNIT_CODE, A.PST_NO, A.DLY_FRM, A.DLY_TO,";
            sqlqry =sqlqry +"A.SH1, A.UNITS1, A.SH2, A.UNITS2, A.SH3, A.UNITS3, A.SH4, A.UNITS4, A.SH5, A.UNITS5, A.SH6, A.UNITS6, A.SH7, A.UNITS7, A.SH8, A.UNITS8, A.SH9, A.UNITS9, A.SH10, A.UNITS10,A.TOTL_UNITS, A.UNIT_CODE ";
            sqlqry =sqlqry + " FROM D_SAL_OF41 A";
            sqlqry =sqlqry + " LEFT JOIN  D_SAL_PRICE_LIST_DETAIL  B ON  SUBSTRING(A.QLTY_NO,1,6)=B.QUALITY_ID   ";
            sqlqry =sqlqry + " WHERE A.PARTY_CODE='" + PartyCD +"' AND A.ORDER_NO='"+OrdNO+"' ";
            
            System.out.println(sqlqry);
            
            ResultSet rsTmp2=stmt.executeQuery(sqlqry);
            Statement stDetail=objConn2.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetail=stDetail.executeQuery("SELECT * FROM D_SAL_ORDER_DETAIL");
            int Counter=1;
            rsTmp2.first();
            while (!rsTmp2.isAfterLast()) {
                String ShadeList="";
                for (int i=0;i<10;i++) {
                    if(rsTmp2.getInt("UNITS"+(i+1))>0) {
                        if (rsTmp2.getInt("UNITS"+(i+1)) >1) {
                            ShadeList = ShadeList + Integer.parseInt(rsTmp2.getString("SH"+(i+1)))+ "/" + Integer.parseInt(rsTmp2.getString("UNITS"+(i+1)))+",";
                        } else {
                            ShadeList = ShadeList + Integer.parseInt(rsTmp2.getString("SH"+(i+1)))+",";
                        }
                    }
                }
                
                of41totalUnit =  Integer.parseInt(rsTmp2.getString("TOTL_UNITS"));
                if(ShadeList.length()>1) {
                    ShadeList = ShadeList.substring(0,ShadeList.length()-1);
                } else {
                    ShadeList = ShadeList.substring(0,ShadeList.length());
                }
                System.out.println("PartyCode :" + PartyCD +  " Ordno : " + OrdNO + "  QLTY : " + rsTmp2.getString("QLTY") +" ShadeList :" + ShadeList);
                //    String ShadeListNew = FindUniqueOrder(OrdNO, PartyCD,rsTmp2.getString("QLTY"),ShadeList);
                String ShadeListNew = ShadeList;
                
                rsDetail.moveToInsertRow();
                rsDetail.updateLong("COMPANY_ID", 2);
                rsDetail.updateString("ORDER_NO", NewOrdNO);
                rsDetail.updateString("OLD_ORDER_NO", OrdNO);
                rsDetail.updateString("PARTY_CODE", PartyCD);
                rsDetail.updateInt("SR_NO", Counter);
                rsDetail.updateString("PRICE_LIST_SR_NO",UtilFunctions.getString(rsTmp2,"SUB_SR_NO",""));
                rsDetail.updateString("BRAND_ID", UtilFunctions.getString(rsTmp2,"BRAND_ID",""));
                rsDetail.updateString("QUALITY_ID", UtilFunctions.getString(rsTmp2,"QLTY",""));
                rsDetail.updateInt("EX_MILL_RATE",UtilFunctions.getInt(rsTmp2,"EX_MILL_RATE",0));
                rsDetail.updateString("SHADE_ID",ShadeListNew);
                rsDetail.updateString("UNIT_ID", UtilFunctions.getString(rsTmp2,"UNIT_CODE",""));
                
                
                if(rsTmp2.getString("PST_NO")==null ){
                    rsDetail.updateString("MSR_UNIT_ID_1", "");
                    rsDetail.updateString("MSR_UNIT_ID_2", "");
                }
                else {
                    String MSR[] = rsTmp2.getString("PST_NO").split(",");
                    if(MSR.length==1) {
                        rsDetail.updateString("MSR_UNIT_ID_1", MSR[0]);
                        rsDetail.updateString("MSR_UNIT_ID_2", "");
                    }else {
                        rsDetail.updateString("MSR_UNIT_ID_1", MSR[0]);
                        rsDetail.updateString("MSR_UNIT_ID_2", MSR[1]);
                    }
                }
                
                rsDetail.updateInt("TOTAL_UNITS", Integer.parseInt(rsTmp2.getString("TOTL_UNITS")));
                String delv_date="20"+rsTmp2.getString("DLY_FRM").substring(2,4) +"-" +rsTmp2.getString("DLY_FRM").substring(0,2)+"-01";
                String delv_from="20"+rsTmp2.getString("DLY_FRM").substring(2,4) +"-" +rsTmp2.getString("DLY_FRM").substring(0,2)+"-01";
                String delv_to="20"+rsTmp2.getString("DLY_TO").substring(2,4) +"-" +rsTmp2.getString("DLY_TO").substring(0,2)+"-01";
                
                rsDetail.updateString("DELIVERY_DATE",  delv_date);
                rsDetail.updateString("DELIVERY_FROM_DATE", delv_from);
                rsDetail.updateString("DELIVERY_TO_DATE", delv_to);
                
                rsDetail.insertRow();
                totalUnit=0;
                Counter++;
                rsTmp2.next();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public String ConvertDateDB(String Date) {
        if (Date.length()<6) {
            return "0000-00-00";
        }
        else {
            return "20"+Date.substring(4,6) + "-" + Date.substring(2,4) + "-" + Date.substring(0,2);
        }
    }
    
    public String FindUniqueOrder(String OldOrdNO, String PartyCD, String Qlty, String ShadeList){
        String finalShadeList="";
        try {
            Connection objConn3=data.getConn();
            Statement stDetail=objConn3.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rsDetail=stDetail.executeQuery("SELECT A.OLD_ORDER_NO, A.ORDER_NO, A.PARTY_CODE, B.QUALITY_ID, B.SHADE_ID FROM D_SAL_ORDER_HEADER A, D_SAL_ORDER_DETAIL B WHERE  A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND A.OLD_ORDER_NO = B.OLD_ORDER_NO  AND B.OLD_ORDER_NO ='"+OldOrdNO+"' AND A.PARTY_CODE = B.PARTY_CODE AND A.PARTY_CODE='"+PartyCD+"' AND B.QUALITY_ID ='"+Qlty+"'");
            
            String DetailShadeList;
            rsDetail.first();
            if (rsDetail.getRow()>0) {
                DetailShadeList = rsDetail.getString("SHADE_ID");
            }
            else {
                totalUnit=totalUnit + of41totalUnit;
                return ShadeList;
            }
            
            String DetailShUnit[] = DetailShadeList.split(",");
            String shades[]=ShadeList.split(",");
            
            int ShadeCounter=0;
            String sh="",unit="",sh1="",unit1="";
            for(int i=0;i<shades.length;i++) {
                if(shades[i].length()>2) {
                    String shunits[] = shades[i].split("/");
                    sh = shunits[0];
                    unit = shunits[1];
                } else {
                    String shunits[] = shades[i].split("/");
                    sh = shunits[0];
                    unit = "1";
                }
                
                for(int j=0;j<DetailShUnit.length;j++){
                    if(DetailShUnit[j].length()>2) {
                        String shunits1[] = DetailShUnit[j].split("/");
                        sh1 = shunits1[0];
                        unit1 = shunits1[1];
                    } else {
                        String shunits1 [] = DetailShUnit[j].split("/");
                        sh1 = shunits1[0];
                        unit1 = "1";
                    }
                    if(sh1.equals(sh) && unit1.equals(unit) ) {
                        continue;
                    } else{
                        totalUnit = totalUnit + Integer.parseInt(unit);
                        finalShadeList=finalShadeList + sh +"/"+unit+",";
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        if(finalShadeList.length()>1) {
            return finalShadeList.substring(0,finalShadeList.length()-1);
        } else {
            return finalShadeList.substring(0,finalShadeList.length());
        }
    }
}