/*
 * GRNLIST.java
 *
 * Created on April 9, 2010, 12:59 PM
 */

package EITLERP;

import java.sql.*;
import java.util.*;
import java.io.*;
import EITLERP.*;
import EITLERP.Stores.*;
import TReportWriter.*;
import TReportWriter.SimpleDataProvider.*;
import EITLERP.Sales.*;
/**
 *
 * @author  root
 */
public class GRNLIST {
    
    /** Creates a new instance of GRNLIST */
    public GRNLIST() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            Connection objConn=data.getConn("jdbc:mysql://200.0.0.227:3306/DINESHMILLS");
            Statement stHeader=objConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            /*String qry = "SELECT A.GRN_NO,A.GRN_DATE,B.ITEM_ID,B.QTY,B.LANDED_RATE, B.QTY*B.LANDED_RATE AS VALUE " +
            "FROM D_INV_GRN_HEADER A,D_INV_GRN_DETAIL B WHERE A.GRN_TYPE = 2 AND A.APPROVED = 1 AND A.CANCELLED = 0 "+
            "AND A.SUPP_ID NOT IN ('888888','999999') "+
            "AND A.GRN_NO = B.GRN_NO "+
            "AND A.GRN_DATE >= '2009-10-01' AND A.GRN_DATE <= '2010-03-31' ";*/
            
            String qry = "SELECT A.GRN_NO,ROUND(SUM(B.QTY*B.LANDED_RATE),3) AS VALUE, " +
            "IF(ROUND(SUM(B.COLUMN_8_AMT),3) >0, ROUND(SUM(B.COLUMN_8_AMT),3) ,IF(ROUND(SUM(A.COLUMN_8_AMT),3)>0,ROUND(SUM(A.COLUMN_8_AMT),3),0)) AS CENVATE " +
            "FROM DINESHMILLS.D_INV_GRN_HEADER A,DINESHMILLS.D_INV_GRN_DETAIL B WHERE A.GRN_TYPE = 2 AND A.APPROVED = 1 AND A.CANCELLED = 0 " +
            "AND A.SUPP_ID NOT IN ('888888','999999') AND A.GRN_NO = B.GRN_NO AND A.GRN_DATE >= '2009-04-01' AND A.GRN_DATE <= '2009-09-30' " +
            "GROUP BY A.GRN_NO";
            
            ResultSet rs=stHeader.executeQuery(qry);
            ResultSet rsv = null;
            rs.first();
            int no=1;
            while(!rs.isAfterLast()) {
                
                //String ItemName = data.getStringValueFromDB("SELECT ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE ITEM_ID = '" + rs.getString("ITEM_ID") + "'  ","jdbc:mysql://200.0.0.227:3306/DINESHMILLSA?user=root");
                String Voucher ="";
                qry = "SELECT DISTINCT A.VOUCHER_NO,ROUND(SUM(B.AMOUNT),2) AS VOUCHER_VALUE FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B " +
                "WHERE A.COMPANY_ID = 2 AND A.VOUCHER_TYPE = 1 AND A.VOUCHER_NO = B.VOUCHER_NO AND B.MAIN_ACCOUNT_CODE NOT IN (403036,403050) " +
                "AND B.GRN_NO = '"+rs.getString("GRN_NO")+"' " +
                "GROUP BY A.VOUCHER_NO";
                
                
                rsv = data.getResult(qry,"jdbc:mysql://200.0.0.227:3306/FINANCE");
                rsv.first();
                int Counter=0;
                
                if(rsv.getRow()>0) {
                    do {
                        System.out.println(no+"~"+rs.getString("GRN_NO")+"~"+rs.getDouble("VALUE")+"~"+rs.getDouble("CENVATE")+"~"+rsv.getString("VOUCHER_NO")+"~"+rsv.getDouble("VOUCHER_VALUE"));
                    }while(rsv.next());
                }
                
                //System.out.println(no+"~"+rs.getString("GRN_NO")+"~"+EITLERPGLOBAL.formatDate(rs.getString("GRN_DATE"))+"~"+rs.getString("ITEM_ID")+"~"+ItemName+"~"+rs.getString("QTY")+"~"+rs.getString("LANDED_RATE")+"~"+rs.getString("VALUE")+"~"+voucher);
                
                //System.out.println(no+"~"+rs.getString("GRN_NO")+"~"+rs.getString("VALUE")+"~"+voucher);
                
                no++;
                rs.next();
            }
            
            
            System.out.println("finish");
        }
        catch(Exception e) {
            
            e.printStackTrace();
        }
    }
    
}
