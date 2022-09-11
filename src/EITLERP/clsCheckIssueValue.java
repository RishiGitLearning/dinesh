/*
 * clsCheckIssueValue.java
 *
 * Created on February 1, 2011, 3:22 PM
 */

package EITLERP;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Purchase.*;
import EITLERP.Finance.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.text.*;
import java.net.*;
import EITLERP.Utils.*;
import java.sql.*;

/**
 *
 * @author  root
 */
public class clsCheckIssueValue {
    
    /** Creates a new instance of clsCheckIssueValue */
    private HashMap props;
    private HashMap colprops;
    
    public Variant getAttribute(String PropName) {
        return (Variant) props.get(PropName);
    }
    
    public void setAttribute(String PropName,Object Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,int Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,long Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,double Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,float Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public void setAttribute(String PropName,boolean Value) {
        props.put(PropName,new Variant(Value));
    }
    
    public clsCheckIssueValue() {
        
        props = new HashMap();
        
        props.put("SR_NO",new Variant(""));
        props.put("ISSUE_NO",new Variant(""));
        props.put("ISSUE_DATE",new Variant(""));
        props.put("ITEM_ID",new Variant(""));
        props.put("ISSUE_SR_NO",new Variant(""));
        props.put("ISSUE_QTY",new Variant(""));
        colprops =  new HashMap();
    }
    
    public HashMap checkIssueValue(String FromDate,String ToDate) {
        try {
            String SQL = "SELECT ISSUE_NO,ISSUE_DATE FROM D_INV_ISSUE_HEADER WHERE ISSUE_NO LIKE 'R%' AND ISSUE_DATE>='" + FromDate + "' AND ISSUE_DATE<='" + ToDate + "' AND CANCELED=0 ORDER BY ISSUE_DATE"; // AND ISSUE_NO='R000150'
            String DataURL = EITLERPGLOBAL.DatabaseURL; //"jdbc:mysql://200.0.0.227:3306/DINESHMILLS";
            ResultSet rsData = data.getResult(SQL,DataURL);
            rsData.first();
            int Counter = 0;
            colprops.clear();
            while(!rsData.isAfterLast()) {
                String IssueNo = rsData.getString("ISSUE_NO");
                String IssueDate = rsData.getString("ISSUE_DATE");
                SQL = "SELECT SR_NO,ITEM_CODE,RATE,ISSUE_VALUE,QTY FROM D_INV_ISSUE_DETAIL WHERE ISSUE_NO='"+IssueNo+"' ORDER BY SR_NO"; //AND SR_NO=74
                ResultSet rsDetail = data.getResult(SQL,DataURL);
                rsDetail.first();
                while(!rsDetail.isAfterLast()) {
                    
                    int SrNo = rsDetail.getInt("SR_NO");
                    String ItemCode = rsDetail.getString("ITEM_CODE");
                    double Rate = rsDetail.getDouble("RATE");
                    double IssueValue = rsDetail.getDouble("ISSUE_VALUE");
                    String Qty = rsDetail.getString("QTY");
                    double TotalIssueValue = 0;
                    SQL = "SELECT SR_NO,ITEM_ID,ISSUED_LOT_QTY,AUTO_LOT_NO FROM D_INV_ISSUE_LOT WHERE ISSUE_NO='"+IssueNo+"' AND ISSUE_SR_NO="+SrNo+" ORDER BY SR_NO";
                    ResultSet rsLot = data.getResult(SQL,DataURL);
                    rsLot.first();
                    double LotIssueValue = 0;
                    int LotCounter = 0;
                    double TotalLandedRate = 0;
                    String AutoLotNo = "";
                    while(!rsLot.isAfterLast()) {
                        LotCounter++;
                        
                        AutoLotNo = rsLot.getString("AUTO_LOT_NO");
                        //int LotSrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_INV_GRN_LOT WHERE AUTO_LOT_NO='"+AutoLotNo+"' ", DataURL);
                        double IssuedLotQty = rsLot.getDouble("ISSUED_LOT_QTY");
                        double LandedRate = data.getDoubleValueFromDB("SELECT A.LANDED_RATE FROM D_INV_GRN_DETAIL A, D_INV_GRN_LOT B " +
                        "WHERE A.GRN_NO = B.GRN_NO AND A.SR_NO=B.GRN_SR_NO AND A.ITEM_ID = B.ITEM_ID " +
                        "AND A.ITEM_ID='" + ItemCode + "' AND B.AUTO_LOT_NO='" + AutoLotNo + "' ",DataURL); //AND B.SR_NO="+LotSrNo
                        TotalLandedRate = TotalLandedRate + LandedRate;
                        LotIssueValue = EITLERPGLOBAL.round(LandedRate * IssuedLotQty,3);
                        TotalIssueValue = TotalIssueValue + LotIssueValue;
                        rsLot.next();
                    }
                    if(Math.abs(EITLERPGLOBAL.round(TotalIssueValue,3)-EITLERPGLOBAL.round(IssueValue,3))>1 || IssueValue==0) {
                        Counter++;
                        //String Msg= "Issue No : " + IssueNo +" Sr No : " + SrNo + " Item Code : " + ItemCode + " IssueValue : " + EITLERPGLOBAL.round(IssueValue, 3) + " TotalIssueValue : " +
                        //"" + EITLERPGLOBAL.round(TotalIssueValue,3) + " Diff : " + EITLERPGLOBAL.round(EITLERPGLOBAL.round(IssueValue, 3)-EITLERPGLOBAL.round(TotalIssueValue,3),3);
                        //System.out.println(Msg);
                        clsCheckIssueValue ObjItem = new clsCheckIssueValue();
                        
                        ObjItem.setAttribute("SR_NO",Integer.toString(Counter));
                        ObjItem.setAttribute("ISSUE_NO",IssueNo);
                        ObjItem.setAttribute("ISSUE_DATE",IssueDate);
                        ObjItem.setAttribute("ISSUE_SR_NO",Integer.toString(SrNo));
                        ObjItem.setAttribute("ITEM_ID",ItemCode);
                        ObjItem.setAttribute("ISSUE_QTY",Qty);
                        colprops.put(Integer.toString(colprops.size()+1), ObjItem);
                    }
                    
                    
                    rsDetail.next();
                }
                rsData.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return colprops;
            
        }
        
        //System.out.println("*** Finished ***");
        colprops = checkSTMValue(colprops,FromDate,ToDate);
        return colprops;
        
    }
    
    private HashMap checkSTMValue(HashMap colprops,String FromDate,String ToDate) {
        try {
            String SQL = "SELECT STM_NO,STM_DATE FROM D_INV_STM_HEADER WHERE STM_NO LIKE 'RM%' AND STM_DATE>='" + FromDate + "' AND STM_DATE<='" + ToDate + "'  AND CANCELLED=0 ORDER BY STM_DATE"; // AND STM_NO='R000150'
            String DataURL = EITLERPGLOBAL.DatabaseURL;
            ResultSet rsData = data.getResult(SQL,DataURL);
            rsData.first();
            int Counter = 0;
            if(rsData.getRow()>0) {
                while(!rsData.isAfterLast()) {
                    String IssueNo = rsData.getString("STM_NO");
                    String IssueDate = rsData.getString("STM_DATE");
                    SQL = "SELECT SR_NO,ITEM_ID,RATE,NET_AMOUNT FROM D_INV_STM_DETAIL WHERE STM_NO='"+IssueNo+"' ORDER BY SR_NO"; //AND SR_NO=74
                    ResultSet rsDetail = data.getResult(SQL,DataURL);
                    rsDetail.first();
                    while(!rsDetail.isAfterLast()) {
                        double IssuedLotQty=0;
                        int SrNo = rsDetail.getInt("SR_NO");
                        String ItemCode = rsDetail.getString("ITEM_ID");
                        double Rate = rsDetail.getDouble("RATE");
                        double IssueValue = rsDetail.getDouble("NET_AMOUNT");
                        double TotalIssueValue = 0;
                        SQL = "SELECT SR_NO,ITEM_ID,ISSUED_LOT_QTY,AUTO_LOT_NO FROM D_INV_STM_LOT WHERE STM_NO='"+IssueNo+"' AND STM_SR_NO="+SrNo+" ORDER BY SR_NO";
                        ResultSet rsLot = data.getResult(SQL,DataURL);
                        rsLot.first();
                        double LotIssueValue = 0;
                        int LotCounter = 0;
                        double TotalLandedRate = 0;
                        while(!rsLot.isAfterLast()) {
                            LotCounter++;
                            
                            String AutoLotNo = rsLot.getString("AUTO_LOT_NO");
                            //int LotSrNo = data.getIntValueFromDB("SELECT SR_NO FROM D_INV_GRN_LOT WHERE AUTO_LOT_NO='"+AutoLotNo+"' ", DataURL);
                            IssuedLotQty = rsLot.getDouble("ISSUED_LOT_QTY");
                            double LandedRate = data.getDoubleValueFromDB("SELECT A.LANDED_RATE FROM D_INV_GRN_DETAIL A, D_INV_GRN_LOT B " +
                            "WHERE A.GRN_NO = B.GRN_NO AND A.SR_NO=B.GRN_SR_NO AND A.ITEM_ID = B.ITEM_ID " +
                            "AND A.ITEM_ID='" + ItemCode + "' AND B.AUTO_LOT_NO='" + AutoLotNo + "' ",DataURL); //AND B.SR_NO="+LotSrNo
                            TotalLandedRate = TotalLandedRate + LandedRate;
                            LotIssueValue = EITLERPGLOBAL.round(LandedRate * IssuedLotQty,3);
                            TotalIssueValue = TotalIssueValue + LotIssueValue;
                            rsLot.next();
                        }
                        if(Math.abs(EITLERPGLOBAL.round(TotalIssueValue,3)-EITLERPGLOBAL.round(IssueValue,3))>1) {
                            //if(EITLERPGLOBAL.round(TotalIssueValue,3)!=EITLERPGLOBAL.round(IssueValue,3)) {
                            /*TotalLandedRate = EITLERPGLOBAL.round((TotalLandedRate/LotCounter),3);
                            String UpdateQry = "UPDATE D_INV_STM_DETAIL " +
                            "SET RATE="+TotalLandedRate+", NET_AMOUNT="+EITLERPGLOBAL.round(TotalIssueValue,3)+" "+
                            "WHERE STM_NO='"+IssueNo+"' AND ITEM_ID='"+ItemCode+"' AND SR_NO="+SrNo;
                            data.Execute(UpdateQry,DataURL);
                             
                            UpdateQry = "UPDATE D_INV_STM_HEADER SET CHANGED=1 WHERE STM_NO='"+IssueNo+"' ";
                            data.Execute(UpdateQry,DataURL);
                             */
                            // String Msg= "Issue No : " + IssueNo +" Sr No : " + SrNo + " Item Code : " + ItemCode + " IssueValue : " + EITLERPGLOBAL.round(IssueValue, 3) + " TotalIssueValue : " +
                            //"" + EITLERPGLOBAL.round(TotalIssueValue,3) + " Diff : " + EITLERPGLOBAL.round(EITLERPGLOBAL.round(IssueValue, 3)-EITLERPGLOBAL.round(TotalIssueValue,3),3);
                            //System.out.println(Msg);
                            
                            clsCheckIssueValue ObjItem = new clsCheckIssueValue();
                            ObjItem.setAttribute("SR_NO",Integer.toString(colprops.size()+1));
                            ObjItem.setAttribute("ISSUE_NO",IssueNo);
                            ObjItem.setAttribute("ISSUE_DATE",IssueDate);
                            ObjItem.setAttribute("ISSUE_SR_NO",Integer.toString(SrNo));
                            ObjItem.setAttribute("ISSUE_QTY",IssuedLotQty);
                            ObjItem.setAttribute("ITEM_ID",ItemCode);
                            colprops.put(Integer.toString(colprops.size()+1), ObjItem);
                            IssuedLotQty=0;
                        }
                        rsDetail.next();
                    }
                    rsData.next();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            return colprops;
        }
        return colprops;
        //System.out.println("*** Finished ***");
    }
}
