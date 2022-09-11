/*
 * STM_ACTION.java
 *
 * Created on May 13, 2010, 5:46 PM
 */

package EITLERP.Stores;


import java.util.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import EITLERP.Finance.*;

/**
 *
 * @author  root
 */
public class STM_ACTION {
    
    
    /** Creates a new instance of STM_ACTION */
    public STM_ACTION() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String StmNo="", StmSrNo="", StmItemID="", dbURL="jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
        String StmAutoLotNo="";
        double IssueLotQty=0.0;
        double GrnLandedRate=0.0;
        double StmItemValue=0.0;
       
        try {
            ResultSet rsDetail;
            
            
            Connection Conn;
            Statement Stmt;
            Conn=data.getConn("jdbc:mysql://200.0.0.227:3306/DINESHMILLSA");
            
            Stmt=Conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            /*String Qry = "SELECT STM_NO,STM_SR_NO,SR_NO,ITEM_ID,AUTO_LOT_NO,ISSUED_LOT_QTY FROM D_INV_STM_LOT WHERE AUTO_LOT_NO<>'' "+
            "GROUP BY STM_NO,STM_SR_NO "+
            "HAVING COUNT(STM_SR_NO)>1  "+
            "ORDER BY STM_NO,STM_SR_NO,ITEM_ID";*/
            String Qry = "SELECT A.STM_NO,A.STM_DATE,B.SR_NO,B.ITEM_ID FROM D_INV_STM_HEADER A,D_INV_STM_DETAIL B " +
            "WHERE A.STM_NO=B.STM_NO AND A.STM_TYPE=B.STM_TYPE AND A.STM_DATE>='2009-10-01' AND A.STM_TYPE=2 " +
            "ORDER BY A.STM_DATE,A.STM_NO,B.SR_NO";
            
            ResultSet rsResultSet=Stmt.executeQuery(Qry);
            rsResultSet.first();
            int Counter =0;
            while (!rsResultSet.isAfterLast()){
                StmNo=UtilFunctions.getString(rsResultSet, "STM_NO", "");
                //StmSrNo=UtilFunctions.getString(rsResultSet, "STM_SR_NO", "");
                StmSrNo=UtilFunctions.getString(rsResultSet, "SR_NO", "");
                StmItemID=UtilFunctions.getString(rsResultSet, "ITEM_ID", "");
                
                
                //String strSQL = "SELECT * FROM D_INV_GRN_DETAIL WHERE MIR_NO='"+StmNo+"' AND MIR_SR_NO="+StmSrNo+" AND ITEM_ID='"+StmItemID+"' AND APPROVED=1 AND CANCELLED=0";
                String strSQL = "SELECT * FROM D_INV_GRN_HEADER A, D_INV_GRN_DETAIL B WHERE A.GRN_NO=B.GRN_NO AND A.GRN_TYPE=B.GRN_TYPE " +
                "AND B.MIR_NO='"+StmNo+"' AND B.MIR_SR_NO="+StmSrNo+" AND B.ITEM_ID='"+StmItemID+"' " +
                "AND A.APPROVED=1 AND A.CANCELLED=0 ";
                if(!data.IsRecordExist(strSQL, dbURL)) {
                    Counter++;
                    System.out.println("Counter " + Counter +"STM NO : " + StmNo + " STM SR NO : " + StmSrNo + " ITEM ID : " + StmItemID);
                }
                
                /*StmItemValue=0.0;
                Qry = "SELECT * FROM D_INV_STM_LOT WHERE STM_NO = '"+StmNo+"' AND ITEM_ID = '"+StmItemID+"' AND STM_SR_NO = '"+StmSrNo+"' ";
                rsDetail = data.getResult(Qry, dbURL);
                rsDetail.first();
                
                
                while (!rsDetail.isAfterLast()){
                    StmAutoLotNo= UtilFunctions.getString(rsDetail, "AUTO_LOT_NO", "");
                    IssueLotQty=UtilFunctions.getDouble(rsDetail, "ISSUED_LOT_QTY", 0);
                    
                    Qry = "SELECT A.LANDED_RATE " +
                    "FROM D_INV_GRN_DETAIL A,D_INV_GRN_LOT B " +
                    "WHERE A.GRN_NO = B.GRN_NO AND A.SR_NO = B.GRN_SR_NO AND B.ITEM_ID = A.ITEM_ID " +
                    "AND B.AUTO_LOT_NO IN("+StmAutoLotNo+")";
                    
                    GrnLandedRate = data.getDoubleValueFromDB(Qry, dbURL);
                    StmItemValue=StmItemValue + EITLERPGLOBAL.round((GrnLandedRate*IssueLotQty),3) ;
                    System.out.println("STM No. :'"+StmNo+"' Srno : '"+StmSrNo +"' Item Code : '"+StmItemID +"' STM Qty :"+IssueLotQty +" GRN Landed Rate"+  GrnLandedRate  +" Issued Value :" + EITLERPGLOBAL.round((GrnLandedRate*IssueLotQty),2)) ;
                    rsDetail.next();
                }

                double OldValue = data.getDoubleValueFromDB("SELECT  TOTAL_AMOUNT FROM D_INV_STM_DETAIL WHERE STM_NO='"+StmNo+"' AND STM_SR_NO = "+StmSrNo+" ",dbURL);
                StmItemValue = EITLERPGLOBAL.round(StmItemValue,3);
                System.out.println("STM No. :'"+StmNo+"' Srno : '"+StmSrNo +"' " + "Old Value : "+ OldValue +  "  NEW ISSUE VALUE :" +StmItemValue + " \n\n") ;
                */
                
                
                rsResultSet.next();
            }
            
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
        System.out.println("End of Result...");
    }
    
}
