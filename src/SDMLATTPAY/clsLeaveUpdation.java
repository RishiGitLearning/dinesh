/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDMLATTPAY;

import EITLERP.EITLERPGLOBAL;
import EITLERP.data;

/**
 *
 * @author root
 */
public class clsLeaveUpdation {

    public static void main(String[] args) {
        if (data.getIntValueFromDB("SELECT day(CURDATE()) FROM DUAL") == 8) {
            
            int YEAR = data.getIntValueFromDB("SELECT YEAR(subdate(CURDATE(),INTERVAL DAY(curdate()) DAY)) FROM DUAL");

            int MONTH = data.getIntValueFromDB("SELECT MONTH(subdate(CURDATE(),INTERVAL DAY(curdate()) DAY)) FROM DUAL");
        
        
         
            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW "
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT) "
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,0, ' NO. WORKING DAYS',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ") AND DPTID = EMP_DEPARTMENT");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,1, 'PL',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ") AND DPTID = EMP_DEPARTMENT\n"
                    + "");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,2, 'CL',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ") AND DPTID = EMP_DEPARTMENT");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,3, 'SL',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ") AND DPTID = EMP_DEPARTMENT");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,4, 'ATTND',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ") AND DPTID = EMP_DEPARTMENT");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,5, 'LWOP',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ") AND DPTID = EMP_DEPARTMENT");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,6, 'L-OFF + EOF',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ") AND DPTID = EMP_DEPARTMENT");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,7, 'COMP ENJ',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ")  AND DPTID = EMP_DEPARTMENT");

            data.Execute("INSERT INTO SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW \n"
                    + "        (YYYY,MM,EMPID,EMP_NAME,LV_NO,LEAVE_CD,JOIN_DT,DEPT)\n"
                    + "        SELECT " + YEAR + "," + MONTH + ", PAY_EMP_NO,EMP_NAME,8, 'COMP ERN',EMP_JOIN_DATE,DPTCODE FROM  SDMLATTPAY.ATTPAY_EMPMST,SDMLATTPAY.ATT_DEPARTMENT_MASTER WHERE APPROVED =1 AND (YEAR(EMP_LEFT_DATE) =  0 OR YEAR(EMP_LEFT_DATE)  = " + YEAR + ")  AND DPTID = EMP_DEPARTMENT");


            /*  OPENING LEAVE */
            data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                    + "( SELECT LVBAL_PAYEMPCD,LVBAL_OPENING,LVBAL_LEAVE_CD, LVBAL_YEAR FROM SDMLATTPAY.ATT_LEAVE_BALANCE LB WHERE LB.LVBAL_YEAR = " + YEAR + " ) AS LB\n"
                    + "SET LS.OP_LEAVE= LVBAL_OPENING\n"
                    + "WHERE LS.EMPID=LB.LVBAL_PAYEMPCD AND LS.YYYY = LB.LVBAL_YEAR AND LS.LEAVE_CD=LB.LVBAL_LEAVE_CD AND  LS.YYYY=" + YEAR + " AND LS.MM=" + MONTH + "");


            /*  CREDIT /EARN  */
            data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW,\n"
                    + "(\n"
                    + "SELECT LVT_YEAR,LVT_PAY_EMPID,LVT_LEAVE_CODE,\n"
                    + "SUM(LVT_DAYS) AS EARN_DAYS \n"
                    + " FROM SDMLATTPAY.ATT_LEAVE_TRN WHERE LVT_LEAVE_TYPE IN (2,12) \n"
                    + "AND LVT_YEAR =" + YEAR + " AND " + MONTH + "  >= MONTH(LVT_FROMDATE) AND MONTH(LVT_FROMDATE) = MONTH(LVT_TODATE) \n"
                    + "GROUP BY LVT_YEAR,LVT_PAY_EMPID,LVT_LEAVE_CODE\n"
                    + ") AS OP_TRN\n"
                    + "SET TOTAL_EARN_LV = EARN_DAYS\n"
                    + "WHERE EMPID = LVT_PAY_EMPID AND LVT_YEAR = YYYY\n"
                    + "AND LVT_LEAVE_CODE IN ('PL','CL','SL') AND LVT_LEAVE_CODE = LEAVE_CD AND YYYY = " + YEAR + " AND MM=" + MONTH + "");

            data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW  SET TOT_OP_LV = (TOTAL_EARN_LV + OP_LEAVE) WHERE YYYY=" + YEAR + "  AND MM=" + MONTH + "");

            for (int i = 1; i <= MONTH; i++) {
                String MonthFieldName = "";

                if (i == 1) {
                    MonthFieldName = "JAN";
                } else if (i == 2) {
                    MonthFieldName = "FEB";
                } else if (i == 3) {
                    MonthFieldName = "MARCH";
                } else if (i == 4) {
                    MonthFieldName = "APRIL";
                } else if (i == 5) {
                    MonthFieldName = "MAY";
                } else if (i == 6) {
                    MonthFieldName = "JUNE";
                } else if (i == 7) {
                    MonthFieldName = "JULY";
                } else if (i == 8) {
                    MonthFieldName = "AUG";
                } else if (i == 9) {
                    MonthFieldName = "SEPT";
                } else if (i == 10) {
                    MonthFieldName = "OCT";
                } else if (i == 11) {
                    MonthFieldName = "NOVE";
                } else if (i == 12) {
                    MonthFieldName = "DECE";
                }

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID,PL, MAS_MM,MAS_YYYY FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + "=PL \n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY "
                        + "AND LS.LEAVE_CD='PL' AND LS.MM=" + MONTH + " ");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID,SL, MAS_MM,MAS_YYYY FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + "=SL\n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY\n"
                        + "AND LS.LEAVE_CD='SL'  AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID,CL, MAS_MM,MAS_YYYY FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + "=CL\n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY\n"
                        + "AND LS.LEAVE_CD='CL'  AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID, PRESENT_DAYS, MAS_MM,MAS_YYYY FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + "= PRESENT_DAYS\n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY\n"
                        + "AND LS.LEAVE_CD='ATTND'  AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID, LC_LWP_DAYS , MAS_MM,MAS_YYYY FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + "= LC_LWP_DAYS\n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY\n"
                        + "AND LS.LEAVE_CD='LWOP'  AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID, (MAS_WORKER_WOFF + MAS_STAFF_WOFF + MAS_PH) AS WOFPH , MAS_MM,MAS_YYYY \n"
                        + "FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + "= WOFPH\n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY\n"
                        + "AND LS.LV_NO=9  AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID, (LOFF+EOFF) AS DY, MAS_MM,MAS_YYYY FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + "= DY\n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY\n"
                        + "AND LS.LV_NO=6  AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(\n"
                        + "SELECT LVT_PAY_EMPID,SUM(LVT_DAYS) AS DD,MONTH(LT.LVT_FROMDATE) AS MM ,YEAR(LT.LVT_FROMDATE)  AS YY\n"
                        + "FROM SDMLATTPAY.ATT_LEAVE_TRN LT WHERE LT.LVT_LEAVE_CODE='CO' AND LVT_LEAVE_TYPE=3\n"
                        + "AND LVT_YEAR=" + YEAR + " \n"
                        + "AND  MONTH(LVT_FROMDATE)=1 \n"
                        + "AND MONTH(LVT_FROMDATE) = MONTH(LVT_TODATE) \n"
                        + "AND " + i + "  >= MONTH(LVT_FROMDATE) \n"
                        + "GROUP BY LVT_PAY_EMPID,MONTH(LT.LVT_FROMDATE) ,YEAR(LT.LVT_FROMDATE)  ) AS LT\n"
                        + "SET LS." + MonthFieldName + "=DD\n"
                        + "WHERE  LS.LV_NO=7 AND LS.LEAVE_CD='COMP ENJ'\n"
                        + "AND LT.LVT_PAY_EMPID=LS.EMPID\n"
                        + "AND LS.YYYY=" + YEAR + " AND LT.YY = LS.YYYY  AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(\n"
                        + "SELECT LVT_PAY_EMPID,SUM(LVT_DAYS) AS DD,MONTH(LT.LVT_FROMDATE) AS MM ,YEAR(LT.LVT_FROMDATE)  AS YY\n"
                        + "FROM SDMLATTPAY.ATT_LEAVE_TRN LT WHERE LT.LVT_LEAVE_CODE='CO' AND LVT_LEAVE_TYPE=2\n"
                        + "AND LVT_YEAR=" + YEAR + " \n"
                        + "AND  MONTH(LVT_FROMDATE)=1 \n"
                        + "AND MONTH(LVT_FROMDATE) = MONTH(LVT_TODATE) \n"
                        + "AND " + i + "  >= MONTH(LVT_FROMDATE) \n"
                        + "GROUP BY LVT_PAY_EMPID,MONTH(LT.LVT_FROMDATE) ,YEAR(LT.LVT_FROMDATE)  ) AS LT\n"
                        + "SET LS." + MonthFieldName + "=DD\n"
                        + "WHERE LS.LV_NO=8 AND LS.LEAVE_CD='COMP ERN' \n"
                        + "AND LT.LVT_PAY_EMPID=LS.EMPID\n"
                        + "AND LS.YYYY=" + YEAR + " AND LT.YY = LS.YYYY AND LS.MM=" + MONTH + "");

                data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS,\n"
                        + "(SELECT MAS_EMPID, (TOTAL_MONTH_DAYS -(MAS_WORKER_WOFF+MAS_STAFF_WOFF)) AS DY, MAS_MM,MAS_YYYY FROM SDMLATTPAY.ATT_MTH_AUDITED_DATA WHERE MAS_MM = " + i + " AND MAS_YYYY = " + YEAR + ") AS MS\n"
                        + "SET LS." + MonthFieldName + " =  DY\n"
                        + "WHERE LS.EMPID=MS.MAS_EMPID AND LS.YYYY = MS.MAS_YYYY\n"
                        + "AND LS.LV_NO=0  AND LS.MM=" + MONTH + "");
            }

            data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS\n"
                    + "SET TOTAL_EJ=(JAN+ FEB+ MARCH+ APRIL+ MAY+ JUNE+ JULY+ AUG+ SEPT+ OCT+ NOVE+ DECE)\n"
                    + "WHERE LS.YYYY=" + YEAR + " AND LS.MM=" + MONTH + "");

            data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS\n"
                    + "SET CL_BL=(TOT_OP_LV-TOTAL_EJ-LEAVE_DEDUCT-LV_LAPSE)\n"
                    + "WHERE LS.YYYY=" + YEAR + " AND LS.LEAVE_CD IN ('PL','CL','SL','COMP ENJ','COMP ERN') AND LS.MM=" + MONTH + "");

            data.Execute("UPDATE SDMLATTPAY.ATT_MTH_LEAVE_STATUS_NEW LS\n"
                    + "SET CL_BL=(TOTAL_EJ - LEAVE_DEDUCT)\n"
                    + "WHERE LS.YYYY=" + YEAR + " AND LS.LEAVE_CD IN ('LWOP') AND LS.MM=" + MONTH + "");

            
        }
    }
}
