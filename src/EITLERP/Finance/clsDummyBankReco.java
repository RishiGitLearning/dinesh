/*
 * clsNoDataObject.java
 *
 * Created on April 6, 2004, 9:32 AM
 */

package EITLERP.Finance;


import java.util.*;
import java.sql.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.*;

/**
 *
 * @author  nrpatel
 * @version
 */

public class clsDummyBankReco {
    
    private HashMap props;
    public HashMap ItemRecord;
    public boolean Ready = false;
    public String LastError = "";
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
    
    
    /** Creates new clsNoDataObject */
    public clsDummyBankReco() {
        props=new HashMap();
        props.put("SR_NO",new Variant(0));
        props.put("VOUCHER_DATE",new Variant("0000-00-00"));
        props.put("BOOK_CODE",new Variant(""));
        props.put("MAIN_ACCOUNT_CODE",new Variant(""));
        props.put("AMOUNT",new Variant(0));
        props.put("EFFECT",new Variant(""));
        props.put("ACCOUNT_DATE",new Variant("0000-00-00"));
        props.put("CHEQUE_NO",new Variant(""));
        props.put("CHEQUE_DATE",new Variant("0000-00-00"));
        props.put("REMARKS",new Variant(""));
        props.put("CREATED_BY",new Variant(""));
        props.put("CREATED_DATE",new Variant(""));
        props.put("MODIFIED_BY",new Variant(""));
        props.put("MODIFIED_DATE",new Variant(""));
        
        ItemRecord = new HashMap();
    }
    
    //Frees up the allocation either by issue or cancellation of Indent or PR
    // In order of First In First Out
    public boolean Insert(String BookCode,String Effect) {
        try {
            Connection tmpConn;
            Statement stTmp;
            ResultSet rsTmp;
            String Condition = "";
            if(!Effect.equals("")) {
                Condition = " AND EFFECT='"+Effect+"' ";
            }
            data.Execute("DELETE FROM D_FIN_DUMMY_VOUCHER WHERE BOOK_CODE='"+BookCode+"' "+ Condition, FinanceGlobal.FinURL);
            
            tmpConn=data.getConn(FinanceGlobal.FinURL);
            stTmp=tmpConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rsTmp=stTmp.executeQuery("SELECT * FROM D_FIN_DUMMY_VOUCHER WHERE BOOK_CODE='"+BookCode+"' " + Condition);
            rsTmp.first();
            
            for(int i=1; i<=ItemRecord.size();i++) {
                clsDummyBankReco objItem = (clsDummyBankReco)ItemRecord.get(Integer.toString(i));
                rsTmp.moveToInsertRow();
                if(Effect.equals("")) {
                    Condition = " AND EFFECT='"+objItem.getAttribute("EFFECT").getString()+"' ";
                }
                rsTmp.updateInt("SR_NO",data.getIntValueFromDB("SELECT MAX(CONVERT(SR_NO,SIGNED)) FROM D_FIN_DUMMY_VOUCHER WHERE BOOK_CODE='"+BookCode+"' " + Condition,FinanceGlobal.FinURL)+1);
                rsTmp.updateString("VOUCHER_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("VOUCHER_DATE").getString()));
                rsTmp.updateString("BOOK_CODE",objItem.getAttribute("BOOK_CODE").getString());
                rsTmp.updateString("MAIN_ACCOUNT_CODE",objItem.getAttribute("MAIN_ACCOUNT_CODE").getString());
                rsTmp.updateDouble("AMOUNT",objItem.getAttribute("AMOUNT").getDouble());
                rsTmp.updateString("EFFECT",objItem.getAttribute("EFFECT").getString());
                rsTmp.updateString("ACCOUNT_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("ACCOUNT_DATE").getString()));
                rsTmp.updateString("CHEQUE_NO",objItem.getAttribute("CHEQUE_NO").getString());
                rsTmp.updateString("CHEQUE_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("CHEQUE_DATE").getString()));
                rsTmp.updateString("REMARKS",objItem.getAttribute("REMARKS").getString());
                rsTmp.updateString("CREATED_BY",objItem.getAttribute("CREATED_BY").getString());
                rsTmp.updateString("CREATED_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("CREATED_DATE").getString()));
                rsTmp.updateString("MODIFIED_BY",objItem.getAttribute("MODIFIED_BY").getString());
                rsTmp.updateString("MODIFIED_DATE",EITLERPGLOBAL.formatDateDB(objItem.getAttribute("MODIFIED_DATE").getString()));
                
                rsTmp.insertRow();
            }
            rsTmp.close();
            stTmp.close();
            return true;
        }
        catch(Exception e) {
            LastError = e.getMessage();
            return false;
        }
    } 
    
    public static HashMap getBankRecoList(String BookCode,String Effect) {
        ResultSet rsTmp=null;
        HashMap List=new HashMap();
        String Condition = "";
        try {
            if(!Effect.equals("")) {
                Condition = "AND EFFECT='"+Effect+"' ";
            }
            rsTmp=data.getResult("SELECT * FROM D_FIN_DUMMY_VOUCHER WHERE BOOK_CODE='"+BookCode+"' " + Condition + " ORDER BY VOUCHER_DATE", FinanceGlobal.FinURL);
            rsTmp.first();
            
            if(rsTmp.getRow()>0) {
                while(!rsTmp.isAfterLast()) {
                    clsDummyBankReco ObjBankRecoItem = new clsDummyBankReco();
                    
                    ObjBankRecoItem.setAttribute("SR_NO",rsTmp.getInt("SR_NO"));
                    ObjBankRecoItem.setAttribute("VOUCHER_DATE",rsTmp.getString("VOUCHER_DATE"));
                    ObjBankRecoItem.setAttribute("BOOK_CODE",rsTmp.getString("BOOK_CODE"));
                    ObjBankRecoItem.setAttribute("MAIN_ACCOUNT_CODE",rsTmp.getString("MAIN_ACCOUNT_CODE"));
                    ObjBankRecoItem.setAttribute("AMOUNT",rsTmp.getDouble("AMOUNT"));
                    ObjBankRecoItem.setAttribute("EFFECT",rsTmp.getString("EFFECT"));
                    ObjBankRecoItem.setAttribute("ACCOUNT_DATE",rsTmp.getString("ACCOUNT_DATE"));
                    ObjBankRecoItem.setAttribute("CHEQUE_NO",rsTmp.getString("CHEQUE_NO"));
                    ObjBankRecoItem.setAttribute("CHEQUE_DATE",rsTmp.getString("CHEQUE_DATE"));
                    ObjBankRecoItem.setAttribute("REMARKS",rsTmp.getString("REMARKS"));
                    ObjBankRecoItem.setAttribute("CREATED_BY",rsTmp.getString("CREATED_BY"));
                    ObjBankRecoItem.setAttribute("CREATED_DATE",rsTmp.getString("CREATED_DATE"));
                    ObjBankRecoItem.setAttribute("MODIFIED_BY",rsTmp.getString("MODIFIED_BY"));
                    ObjBankRecoItem.setAttribute("MODIFIED_DATE",rsTmp.getString("MODIFIED_DATE"));
                    
                    List.put(Integer.toString(List.size()+1), ObjBankRecoItem);
                    
                    rsTmp.next();
                }
            }
        }
        catch(Exception e) {
           //e.printStackTrace();
        }
        
        return List;
    }
}
