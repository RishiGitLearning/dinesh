/*
 * clsPartyLedger.java
 *
 * Created on December 15, 2007, 10:31 AM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import java.sql.* ;
import EITLERP.* ;
import java.net.* ;
import java.io.* ;


public class clsPartyLedger {
    
    /** Creates a new instance of clsPartyLedger */
    public clsPartyLedger() {
    }
    
    public Object[][] getsubcodedetail(String MainAccountCode,String SubAccountCode,String FromDate,String ToDate,double Amounttxt,String selDept) {
        Object[][] ObjParty =new Object[0][0];
        try{
            Connection conn;
            Statement stmt ;
            ResultSet rsVoucherDetail,rsVoucherDetail2,rsInsertVoucher ;
            String VoucherNo,subaccode,Effect,PartyName,DeptName,DeptID;
            int BlockNo ;
            double Amount ;
            conn = data.getConn(EITLERPGLOBAL.DatabaseURL);
            stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            PartyName=clsAccount.getAccountName( MainAccountCode,SubAccountCode);
            String strQuery2 ="SELECT DISTINCT(A.VOUCHER_NO) AS VOUCHER_NO,A.EFFECT AS EFFECT FROM D_FIN_VOUCHER_DETAIL_EX A,D_FIN_VOUCHER_HEADER B WHERE A.VOUCHER_NO=B.VOUCHER_NO AND B.VOUCHER_DATE >='"+FromDate+"'AND B.VOUCHER_DATE <='"+ToDate+"'"+(new clsPartyInfo()).getStrQuery(MainAccountCode,SubAccountCode);
            rsVoucherDetail=data.getResult(strQuery2,FinanceGlobal.FinURL);
            rsVoucherDetail.last();
            ObjParty = new Object[rsVoucherDetail.getRow()+1][10];
            data.Execute("DELETE FROM TMP_GENERAL",EITLERPGLOBAL.DatabaseURL);
            rsVoucherDetail.first();
            //DeptName=data.getStringValueFromDB("SELECT B.DEPT_DESC FROM D_INV_GRN_DETAIL A,D_COM_DEPT_MASTER B WHERE A.DEPT_ID =B.DEPT_ID AND A.GRN_NO ='"+rsVoucherDetail.getString("GRN_NO")+"' GROUP BY A.GRN_NO",EITLERPGLOBAL.DatabaseURL);
            int i = 1 ;
            while(!rsVoucherDetail.isAfterLast()) {
                VoucherNo=rsVoucherDetail.getString("VOUCHER_NO");
                String strQuery ="SELECT SUM(A.AMOUNT) AS AMOUNT,A.VOUCHER_NO AS VOUCHERNO,VOUCHER_DATE,A.EFFECT AS EFFECT,CHEQUE_NO,PO_NO,GRN_NO,INVOICE_NO FROM D_FIN_VOUCHER_DETAIL_EX A,D_FIN_VOUCHER_HEADER B WHERE A.VOUCHER_NO ='"+rsVoucherDetail.getString("VOUCHER_NO")+"' AND A.VOUCHER_NO =B.VOUCHER_NO  AND A.EFFECT<>'"+rsVoucherDetail.getString("EFFECT")+"'GROUP BY A.VOUCHER_NO";
                rsVoucherDetail2=data.getResult(strQuery,FinanceGlobal.FinURL);
                Amount =rsVoucherDetail2.getDouble("AMOUNT");
                DeptID =data.getStringValueFromDB("SELECT B.DEPT_DESC FROM D_INV_GRN_DETAIL A,D_COM_DEPT_MASTER B WHERE A.DEPT_ID =B.DEPT_ID AND A.GRN_NO ='"+rsVoucherDetail2.getString("GRN_NO")+"' GROUP BY A.GRN_NO",EITLERPGLOBAL.DatabaseURL);
                if(Amount >= Amounttxt) {
                    if(selDept.equals("--")||selDept.equals("-")) {
                        ObjParty[i][0]=rsVoucherDetail2.getString("VOUCHERNO");
                        ObjParty[i][1]=EITLERPGLOBAL.formatDate(rsVoucherDetail2.getString("VOUCHER_DATE"));
                        ObjParty[i][2]=rsVoucherDetail2.getString("AMOUNT");
                        ObjParty[i][3]=rsVoucherDetail2.getString("EFFECT");
                        ObjParty[i][4]=rsVoucherDetail2.getString("CHEQUE_NO");
                        ObjParty[i][5]=rsVoucherDetail2.getString("PO_NO");
                        ObjParty[i][6]=rsVoucherDetail2.getString("GRN_NO");
                        ObjParty[i][7]=rsVoucherDetail2.getString("INVOICE_NO");
                        ObjParty[i][8]=data.getStringValueFromDB("SELECT B.DEPT_DESC FROM D_INV_GRN_DETAIL A,D_COM_DEPT_MASTER B WHERE A.DEPT_ID =B.DEPT_ID AND A.GRN_NO ='"+rsVoucherDetail2.getString("GRN_NO")+"' GROUP BY A.GRN_NO",EITLERPGLOBAL.DatabaseURL);
                        i++;
                    }
                    else if(selDept.equals(DeptID)) {
                        ObjParty[i][0]=rsVoucherDetail2.getString("VOUCHERNO");
                        ObjParty[i][1]=EITLERPGLOBAL.formatDate(rsVoucherDetail2.getString("VOUCHER_DATE"));
                        ObjParty[i][2]=rsVoucherDetail2.getString("AMOUNT");
                        ObjParty[i][3]=rsVoucherDetail2.getString("EFFECT");
                        ObjParty[i][4]=rsVoucherDetail2.getString("CHEQUE_NO");
                        ObjParty[i][5]=rsVoucherDetail2.getString("PO_NO");
                        ObjParty[i][6]=rsVoucherDetail2.getString("GRN_NO");
                        ObjParty[i][7]=rsVoucherDetail2.getString("INVOICE_NO");
                        ObjParty[i][8]=data.getStringValueFromDB("SELECT B.DEPT_DESC FROM D_INV_GRN_DETAIL A,D_COM_DEPT_MASTER B WHERE A.DEPT_ID =B.DEPT_ID AND A.GRN_NO ='"+rsVoucherDetail2.getString("GRN_NO")+"' GROUP BY A.GRN_NO",EITLERPGLOBAL.DatabaseURL);
                        i++;
                    }
                }
                rsVoucherDetail.next();
            }
            ObjParty[0][0]=Integer.toString(i-1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ObjParty;
    }
    
}
