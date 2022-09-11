/*
 * clsRMClosing.java
 *
 * Created on November 5, 2009, 3:26 PM
 */

package EITLERP.Stores;

import java.io.*;
import java.util.*;
import java.sql.*;
import EITLERP.Finance.*;
import EITLERP.*;

/**
 *
 * @author  root
 */
public class clsRMClosing {
    
    /** Creates a new instance of clsRMClosing */
    int CompanyID = 3;
    String dbURL = "";
    public clsRMClosing() {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //new clsRMClosing().ImporttoTable("/root/Desktop/rmank/RMANK.TXT");
        new clsRMClosing().ImportRMClosing();
        //new clsRMClosing().getItemLotNo();
    }
 
    private void ImporttoTable(String ItemFile) {
        String ItemID="" , GRNNo="", AutoReceiptNo="", ItemLotNo="";
        int Pointer = 0, GRNSrNo=0, Counter=0;
        double Qty = 0, Rate=0, Value;
        try {
            dbURL = clsFinYear.getDBURL(CompanyID, 2009);
            BufferedReader aFile=new BufferedReader(new FileReader(new File(ItemFile)));
            while(true) {
                Counter++;
                String FileRecord=aFile.readLine();
                Pointer = 0;
                ItemID = FileRecord.substring(Pointer,Pointer+10); Pointer+=10;
                GRNNo = FileRecord.substring(Pointer,Pointer+7); Pointer+=7;
                Qty = Double.parseDouble(FileRecord.substring(Pointer,Pointer+8)+"."+FileRecord.substring(Pointer+8,Pointer+10)); Pointer+=10;
                AutoReceiptNo = FileRecord.substring(Pointer,Pointer+6); Pointer+=6;
                
                GRNSrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND ITEM_ID='"+ItemID+"' ORDER BY SR_NO", dbURL);
                Rate = data.getDoubleValueFromDB("SELECT LANDED_RATE FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND ITEM_ID='"+ItemID+"' AND SR_NO="+GRNSrNo+" ", dbURL);
                Value = EITLERPGLOBAL.round(Rate * Qty, 3);
                ItemLotNo = data.getStringValueFromDB("SELECT ITEM_LOT_NO FROM D_INV_GRN_LOT WHERE GRN_NO='"+GRNNo+"' AND GRN_SR_NO="+GRNSrNo+" AND SR_NO=1", dbURL);
                data.Execute("INSERT INTO TMP_RAW_TABLE VALUES("+Counter+",'"+ItemID+"','"+GRNNo+"',"+GRNSrNo+",'"+ItemLotNo+"','"+AutoReceiptNo+"',"+Rate+","+Value+","+Qty+")", dbURL);
                
                if(data.IsRecordExist("SELECT * FROM D_INV_GRN_HEADER WHERE GRN_NO='"+GRNNo+"' AND GRN_DATE<='2009-09-30' " ,dbURL)) {
                    if(data.IsRecordExist("SELECT * FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND ITEM_ID='"+ItemID+"' " ,dbURL)) {
                        double ActualQty = EITLERPGLOBAL.round(data.getDoubleValueFromDB("SELECT QTY FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND ITEM_ID='"+ItemID+"' AND SR_NO="+GRNSrNo+"", dbURL),2);
                        if(Qty > ActualQty) {
                            System.out.println("GRNNo = "+ GRNNo +" ItemID = "+ ItemID +" Balance Qty "+ Qty +" greater then "+ ActualQty +" ");
                        }
                    } else {
                        System.out.println("Item ID "+ ItemID +" not found in GRN No. "+ GRNNo +" ");
                    }
                } else {
                    System.out.println("GRN No. "+ GRNNo +" not found..."+"Item ID "+ ItemID );
                }
            }
        } catch(Exception e) {
        }
    }
    
    private void ImportRMClosing() {
        
        String ItemID="", GRNNo = "", GRNDate="", AutoReceiptNo="" , SQL="", NewItemID="", ItemLotNo="";
        int GRNSrNo = 0;
        double Qty = 0, Value = 0, Rate=0;
        int SrNo = 0;
        Connection Conn=null;
        Statement stClosingDetail=null;
        ResultSet rsClosingDetail=null, tmpResult=null, tmpData=null;
        int FoundCounter = 0;
        int NotFoundCounter = 0;
        try {
            dbURL = clsFinYear.getDBURL(CompanyID, 2009);
            Conn = data.getConn(dbURL);
            
            stClosingDetail = Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsClosingDetail=stClosingDetail.executeQuery("SELECT * FROM D_COM_OPENING_STOCK_DETAIL LIMIT 1");
            
            tmpResult = data.getResult("SELECT * FROM D_INV_ITEM_MASTER WHERE ITEM_ID LIKE 'RM%' AND APPROVED=1 AND CANCELLED=0 ORDER BY ITEM_ID", dbURL);
            tmpResult.first();
            while(!tmpResult.isAfterLast()) {
                NewItemID = tmpResult.getString("ITEM_ID");
                if(data.IsRecordExist("SELECT * FROM TMP_RAW_TABLE WHERE ITEM_ID='"+NewItemID+"' ",dbURL)) {
                    tmpData = data.getResult("SELECT * FROM TMP_RAW_TABLE WHERE ITEM_ID='"+NewItemID+"' ",dbURL);
                    while(!tmpData.isAfterLast()) {
                        ItemID = tmpData.getString("ITEM_ID");
                        GRNNo = tmpData.getString("GRN_NO");
                        GRNSrNo = tmpData.getInt("GRN_SR_NO");
                        ItemLotNo=tmpData.getString("ITEM_LOT_NO");
                        AutoReceiptNo = tmpData.getString("AUTO_LOT_NO");
                        Rate = tmpData.getDouble("RATE");
                        Qty = tmpData.getDouble("QTY");
                        Value = tmpData.getDouble("VALUE");
                        double ActualQty = EITLERPGLOBAL.round(data.getDoubleValueFromDB("SELECT QTY FROM D_INV_GRN_DETAIL WHERE GRN_NO='"+GRNNo+"' AND ITEM_ID='"+ItemID+"' AND SR_NO="+GRNSrNo+"", dbURL),2);
                        if(data.IsRecordExist("SELECT * FROM D_INV_GRN_LOT WHERE GRN_NO='"+GRNNo+"' AND GRN_SR_NO='"+GRNSrNo+"'",dbURL)) {
                            FoundCounter++;
                            SQL = "UPDATE D_INV_GRN_LOT SET ITEM_ID='"+ItemID+"', AUTO_LOT_NO='"+AutoReceiptNo+"', APPROVED=1, LOT_ACCEPTED_QTY="+Qty+" " +
                            "WHERE GRN_NO='"+GRNNo+"' AND GRN_SR_NO='"+GRNSrNo+"' AND SR_NO=1";
                            data.Execute(SQL,dbURL);
                        } else {
                            NotFoundCounter++;
                            data.Execute("INSERT INTO D_INV_GRN_LOT VALUES("+CompanyID+",'"+GRNNo+"','"+GRNSrNo+"',"+1+",'"+ItemID+"','"+ItemLotNo+"','"+AutoReceiptNo+"',"+ActualQty+","+0+","+Qty+",0,2,0,1,'0000-00-00',0,1,CURDATE())", dbURL);
                        }
                        
                        SQL = "UPDATE D_INV_GRN_HEADER SET CHANGED=1, CHANGED_DATE=CURDATE() WHERE GRN_NO='"+GRNNo+"'";
                        data.Execute(SQL,dbURL);
                        
                        String WareHouseID=clsItem.getItemWareHouseID(CompanyID, ItemID);
                        String LocationID=clsItem.getItemLocationID(CompanyID, ItemID);
                        String BOENo="X";
                        
                        SrNo++;
                        
                        rsClosingDetail.moveToInsertRow();
                        rsClosingDetail.updateInt("COMPANY_ID",CompanyID);
                        rsClosingDetail.updateLong("ENTRY_NO",5); //FOR BARODA -- 8 // FOR ANK --5
                        rsClosingDetail.updateString("ENTRY_DATE","2009-10-01");
                        rsClosingDetail.updateLong("SR_NO",SrNo);
                        rsClosingDetail.updateString("ITEM_ID",ItemID);
                        rsClosingDetail.updateString("WAREHOUSE_ID",WareHouseID);
                        rsClosingDetail.updateString("LOCATION_ID",LocationID);
                        rsClosingDetail.updateString("BOE_NO",BOENo);
                        rsClosingDetail.updateString("ITEM_LOT_NO",ItemLotNo);
                        rsClosingDetail.updateString("LOT_NO",AutoReceiptNo);
                        rsClosingDetail.updateDouble("OPENING_QTY",Qty);
                        rsClosingDetail.updateDouble("OPENING_RATE",Rate);
                        rsClosingDetail.updateDouble("OPENING_VALUE",Value);
                        rsClosingDetail.updateDouble("ZERO_VAL_OPENING_QTY",0);
                        rsClosingDetail.updateDouble("CREATED_BY",EITLERPGLOBAL.gUserID);
                        rsClosingDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsClosingDetail.updateDouble("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                        rsClosingDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                        rsClosingDetail.insertRow();
                        
                        tmpData.next();
                    }
                } else {
                    String WareHouseID=clsItem.getItemWareHouseID(CompanyID, NewItemID);
                    String LocationID=clsItem.getItemLocationID(CompanyID, NewItemID);
                    String BOENo="X";
                    
                    SrNo++;
                    
                    rsClosingDetail.moveToInsertRow();
                    rsClosingDetail.updateInt("COMPANY_ID",CompanyID);
                    rsClosingDetail.updateLong("ENTRY_NO",5); //FOR BARODA -- 8 // FOR ANK --5
                    rsClosingDetail.updateString("ENTRY_DATE","2009-10-01");
                    rsClosingDetail.updateLong("SR_NO",SrNo);
                    rsClosingDetail.updateString("ITEM_ID",NewItemID);
                    rsClosingDetail.updateString("WAREHOUSE_ID",WareHouseID);
                    rsClosingDetail.updateString("LOCATION_ID",LocationID);
                    rsClosingDetail.updateString("BOE_NO",BOENo);
                    rsClosingDetail.updateString("ITEM_LOT_NO","");
                    rsClosingDetail.updateString("LOT_NO","");
                    rsClosingDetail.updateDouble("OPENING_QTY",0);
                    rsClosingDetail.updateDouble("OPENING_RATE",0);
                    rsClosingDetail.updateDouble("OPENING_VALUE",0);
                    rsClosingDetail.updateDouble("ZERO_VAL_OPENING_QTY",0);
                    rsClosingDetail.updateDouble("CREATED_BY",EITLERPGLOBAL.gUserID);
                    rsClosingDetail.updateString("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsClosingDetail.updateDouble("MODIFIED_BY",EITLERPGLOBAL.gUserID);
                    rsClosingDetail.updateString("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
                    rsClosingDetail.insertRow();
                }
                tmpResult.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Found Counter = " + FoundCounter + " Not Found Counter = " + NotFoundCounter);
    }
    
    private void getItemLotNo() {
        ResultSet tmpData=null;
        String ItemID="", GRNNo = "", GRNDate="", AutoReceiptNo="" , SQL="";
        int GRNSrNo = 0;
        String ItemLotNo = "";
        try {
            dbURL = clsFinYear.getDBURL(CompanyID, 2009);
            tmpData = data.getResult("SELECT * FROM TMP_RAW_TABLE ",dbURL);
            while(!tmpData.isAfterLast()) {
                ItemID = tmpData.getString("ITEM_ID");
                GRNNo = tmpData.getString("GRN_NO");
                GRNSrNo = tmpData.getInt("GRN_SR_NO");
                AutoReceiptNo = tmpData.getString("AUTO_LOT_NO");
                
                
                
                SQL = "UPDATE TMP_RAW_TABLE SET ITEM_LOT_NO='"+ItemLotNo+"' WHERE AUTO_LOT_NO='"+AutoReceiptNo+"'";
                data.Execute(SQL, dbURL);
                //SQL = "UPDATE D_COM_OPENING_STOCK_DETAIL SET ITEM_LOT_NO='"+ItemLotNo+"' WHERE LOT_NO='"+AutoReceiptNo+"'";
                //data.Execute(SQL, dbURL);
                tmpData.next();
            }
        } catch(Exception e) {
        }
    }    
    
    private String getNextLOTNo(int CompanyID) {
        String newLotNo="";
        String Prefix ="";
        String URL = "";
        try {
            URL = clsFinYear.getDBURL(CompanyID,EITLERPGLOBAL.FinYearFrom);
            Prefix = "00";
            String strSQL="SELECT MAX(SUBSTRING(AUTO_LOT_NO,LENGTH('"+Prefix+"')+1)) AS MAX_NO FROM D_INV_MIR_LOT WHERE AUTO_LOT_NO LIKE '"+Prefix+"%' AND APPROVED=1 AND CANCELLED=0 ";
            int MaxNo=UtilFunctions.CInt(data.getStringValueFromDB(strSQL, URL))+1;
            String strMaxNo=Integer.toString(MaxNo);
            strMaxNo=EITLERPGLOBAL.Padding(strMaxNo,4,"0");
            newLotNo=Prefix+strMaxNo;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return newLotNo;
    }
}
