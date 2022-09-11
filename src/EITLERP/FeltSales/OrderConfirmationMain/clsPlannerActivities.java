/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EITLERP.FeltSales.OrderConfirmationMain;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;
import java.sql.ResultSet;

/**
 *
 * @author root
 */
public class clsPlannerActivities 
{
    
    String PLANNER_ID, PLANNER_DATE, PLANNER_TYPE, PIECE_NO, PARTY_CODE, UPN, REQ_MONTH, RESCHEDULE_REQ_MONTH, OC_MONTH, UPDATED_OC_MONTH, CURRENT_SCH_MONTH, UPDATED_CURRENT_SCH_MONTH, SPECIAL_REQ_MONTH_DATE, SPECIAL_REQ_MONTH, UPDATED_SPECIAL_REQ_MONTH, EXP_WIP_DELIVERY_DATE, UPDATED_EXP_WIP_DELIVERY_DATE, EXP_PI_DATE, UPDATED_EXP_PI_DATE, FROM_IP, UPDATE_STATUS, USER_ID, STATUS_UPDATE_DATE, EXP_PAY_CHQ_RCV_DATE;

    public String getPLANNER_ID() {
        return PLANNER_ID;
    }

    public void setPLANNER_ID(String PLANNER_ID) {
        this.PLANNER_ID = PLANNER_ID;
    }

    public String getPLANNER_DATE() {
        return PLANNER_DATE;
    }

    public void setPLANNER_DATE(String PLANNER_DATE) {
        this.PLANNER_DATE = PLANNER_DATE;
    }

    public String getPLANNER_TYPE() {
        return PLANNER_TYPE;
    }

    public void setPLANNER_TYPE(String PLANNER_TYPE) {
        this.PLANNER_TYPE = PLANNER_TYPE;
    }

    public String getPIECE_NO() {
        return PIECE_NO;
    }

    public void setPIECE_NO(String PIECE_NO) {
        this.PIECE_NO = PIECE_NO;
    }

    public String getPARTY_CODE() {
        return PARTY_CODE;
    }

    public void setPARTY_CODE(String PARTY_CODE) {
        this.PARTY_CODE = PARTY_CODE;
    }

    public String getUPN() {
        return UPN;
    }

    public void setUPN(String UPN) {
        this.UPN = UPN;
    }

    public String getREQ_MONTH() {
        return REQ_MONTH;
    }

    public void setREQ_MONTH(String REQ_MONTH) {
        this.REQ_MONTH = REQ_MONTH;
    }

    public String getRESCHEDULE_REQ_MONTH() {
        return RESCHEDULE_REQ_MONTH;
    }

    public void setRESCHEDULE_REQ_MONTH(String RESCHEDULE_REQ_MONTH) {
        this.RESCHEDULE_REQ_MONTH = RESCHEDULE_REQ_MONTH;
    }

    public String getOC_MONTH() {
        return OC_MONTH;
    }

    public void setOC_MONTH(String OC_MONTH) {
        this.OC_MONTH = OC_MONTH;
    }

    public String getUPDATED_OC_MONTH() {
        return UPDATED_OC_MONTH;
    }

    public void setUPDATED_OC_MONTH(String UPDATED_OC_MONTH) {
        this.UPDATED_OC_MONTH = UPDATED_OC_MONTH;
    }

    public String getCURRENT_SCH_MONTH() {
        return CURRENT_SCH_MONTH;
    }

    public void setCURRENT_SCH_MONTH(String CURRENT_SCH_MONTH) {
        this.CURRENT_SCH_MONTH = CURRENT_SCH_MONTH;
    }

    public String getUPDATED_CURRENT_SCH_MONTH() {
        return UPDATED_CURRENT_SCH_MONTH;
    }

    public void setUPDATED_CURRENT_SCH_MONTH(String UPDATED_CURRENT_SCH_MONTH) {
        this.UPDATED_CURRENT_SCH_MONTH = UPDATED_CURRENT_SCH_MONTH;
    }

    public String getSPECIAL_REQ_MONTH_DATE() {
        return SPECIAL_REQ_MONTH_DATE;
    }

    public void setSPECIAL_REQ_MONTH_DATE(String SPECIAL_REQ_MONTH_DATE) {
        this.SPECIAL_REQ_MONTH_DATE = SPECIAL_REQ_MONTH_DATE;
    }

    public String getSPECIAL_REQ_MONTH() {
        return SPECIAL_REQ_MONTH;
    }

    public void setSPECIAL_REQ_MONTH(String SPECIAL_REQ_MONTH) {
        this.SPECIAL_REQ_MONTH = SPECIAL_REQ_MONTH;
    }

    public String getUPDATED_SPECIAL_REQ_MONTH() {
        return UPDATED_SPECIAL_REQ_MONTH;
    }

    public void setUPDATED_SPECIAL_REQ_MONTH(String UPDATED_SPECIAL_REQ_MONTH) {
        this.UPDATED_SPECIAL_REQ_MONTH = UPDATED_SPECIAL_REQ_MONTH;
    }

    public String getEXP_WIP_DELIVERY_DATE() {
        return EXP_WIP_DELIVERY_DATE;
    }

    public void setEXP_WIP_DELIVERY_DATE(String EXP_WIP_DELIVERY_DATE) {
        this.EXP_WIP_DELIVERY_DATE = EXP_WIP_DELIVERY_DATE;
    }

    public String getUPDATED_EXP_WIP_DELIVERY_DATE() {
        return UPDATED_EXP_WIP_DELIVERY_DATE;
    }

    public void setUPDATED_EXP_WIP_DELIVERY_DATE(String UPDATED_EXP_WIP_DELIVERY_DATE) {
        this.UPDATED_EXP_WIP_DELIVERY_DATE = UPDATED_EXP_WIP_DELIVERY_DATE;
    }

    public String getEXP_PI_DATE() {
        return EXP_PI_DATE;
    }

    public void setEXP_PI_DATE(String EXP_PI_DATE) {
        this.EXP_PI_DATE = EXP_PI_DATE;
    }

    public String getUPDATED_EXP_PI_DATE() {
        return UPDATED_EXP_PI_DATE;
    }

    public void setUPDATED_EXP_PI_DATE(String UPDATED_EXP_PI_DATE) {
        this.UPDATED_EXP_PI_DATE = UPDATED_EXP_PI_DATE;
    }

    public String getFROM_IP() {
        return FROM_IP;
    }

    public void setFROM_IP(String FROM_IP) {
        this.FROM_IP = FROM_IP;
    }

    public String getUPDATE_STATUS() {
        return UPDATE_STATUS;
    }

    public void setUPDATE_STATUS(String UPDATE_STATUS) {
        this.UPDATE_STATUS = UPDATE_STATUS;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getSTATUS_UPDATE_DATE() {
        return STATUS_UPDATE_DATE;
    }

    public void setSTATUS_UPDATE_DATE(String STATUS_UPDATE_DATE) {
        this.STATUS_UPDATE_DATE = STATUS_UPDATE_DATE;
    }

    public String getEXP_PAY_CHQ_RCV_DATE() {
        return EXP_PAY_CHQ_RCV_DATE;
    }

    public void setEXP_PAY_CHQ_RCV_DATE(String EXP_PAY_CHQ_RCV_DATE) {
        this.EXP_PAY_CHQ_RCV_DATE = EXP_PAY_CHQ_RCV_DATE;
    }
    
    public void saveTransaction()
    {
        try{
            
            ResultSet rsTmp=data.getResult("SELECT USER()");
            rsTmp.first();
            String str = rsTmp.getString(1);
            String str_split[] = str.split("@");
            
            System.out.println("INSERT INTO PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES" +
                                "(" +
                                "PLANNER_DATE,PLANNER_TYPE,PIECE_NO,PARTY_CODE,UPN,REQ_MONTH,RESCHEDULE_REQ_MONTH,OC_MONTH,UPDATED_OC_MONTH,CURRENT_SCH_MONTH,UPDATED_CURRENT_SCH_MONTH,SPECIAL_REQ_MONTH_DATE,SPECIAL_REQ_MONTH,UPDATED_SPECIAL_REQ_MONTH,EXP_WIP_DELIVERY_DATE,UPDATED_EXP_WIP_DELIVERY_DATE,EXP_PI_DATE,UPDATED_EXP_PI_DATE,FROM_IP,UPDATE_STATUS,USER_ID,STATUS_UPDATE_DATE,EXP_PAY_CHQ_RCV_DATE)" +
                                "VALUES" +
                                "('"+PLANNER_DATE+"','"+PLANNER_TYPE+"','"+PIECE_NO+"','"+PARTY_CODE+"','"+UPN+"','"+REQ_MONTH+"','"+RESCHEDULE_REQ_MONTH+"','"+OC_MONTH+"','"+UPDATED_OC_MONTH+"','"+CURRENT_SCH_MONTH+"','"+UPDATED_CURRENT_SCH_MONTH+"','"+SPECIAL_REQ_MONTH_DATE+"','"+SPECIAL_REQ_MONTH+"','"+UPDATED_SPECIAL_REQ_MONTH+"','"+EXP_WIP_DELIVERY_DATE+"','"+UPDATED_EXP_WIP_DELIVERY_DATE+"','"+EXP_PI_DATE+"','"+UPDATED_EXP_PI_DATE+"','"+str_split[1]+"','"+UPDATE_STATUS+"','"+USER_ID+"','"+STATUS_UPDATE_DATE+"','"+EXP_PAY_CHQ_RCV_DATE+"')");
            data.Execute("INSERT INTO PRODUCTION.FELT_SALES_PLANNER_ACTIVITIES" +
                                "(" +
                                "PLANNER_DATE,PLANNER_TYPE,PIECE_NO,PARTY_CODE,UPN,REQ_MONTH,RESCHEDULE_REQ_MONTH,OC_MONTH,UPDATED_OC_MONTH,CURRENT_SCH_MONTH,UPDATED_CURRENT_SCH_MONTH,SPECIAL_REQ_MONTH_DATE,SPECIAL_REQ_MONTH,UPDATED_SPECIAL_REQ_MONTH,EXP_WIP_DELIVERY_DATE,UPDATED_EXP_WIP_DELIVERY_DATE,EXP_PI_DATE,UPDATED_EXP_PI_DATE,FROM_IP,UPDATE_STATUS,USER_ID,STATUS_UPDATE_DATE,EXP_PAY_CHQ_RCV_DATE)" +
                                "VALUES" +
                                "('"+PLANNER_DATE+"','"+PLANNER_TYPE+"','"+PIECE_NO+"','"+PARTY_CODE+"','"+UPN+"','"+REQ_MONTH+"','"+RESCHEDULE_REQ_MONTH+"','"+OC_MONTH+"','"+UPDATED_OC_MONTH+"','"+CURRENT_SCH_MONTH+"','"+UPDATED_CURRENT_SCH_MONTH+"','"+SPECIAL_REQ_MONTH_DATE+"','"+SPECIAL_REQ_MONTH+"','"+UPDATED_SPECIAL_REQ_MONTH+"','"+EXP_WIP_DELIVERY_DATE+"','"+UPDATED_EXP_WIP_DELIVERY_DATE+"','"+EXP_PI_DATE+"','"+UPDATED_EXP_PI_DATE+"','"+str_split[1]+"','"+UPDATE_STATUS+"','"+USER_ID+"','"+STATUS_UPDATE_DATE+"','"+EXP_PAY_CHQ_RCV_DATE+"')");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
