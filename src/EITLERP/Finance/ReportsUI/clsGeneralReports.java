/**
 * clsVoucherReports.java
 *
 * Created on January 11, 2008, 1:25 PM
 */

package EITLERP.Finance.ReportsUI;

/**
 *
 * @author  root
 */
import EITLERP.Finance.*;
import EITLERP.*;
import TReportWriter.SimpleDataProvider.*;
import TReportWriter.*;
import java.util.*;
import java.sql.*;


public class clsGeneralReports {
    
    /** Creates a new instance of clsVoucherReports */
    public clsGeneralReports() {
    }
    
    public static TTable getExpenseSlipPrint(int CompanyID,String DocNo) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("COMPANY_ID");
        objData.AddColumn("COMPANY_NAME");
        objData.AddColumn("GROUP_DOC_NO");
        objData.AddColumn("DOC_NO");
        objData.AddColumn("DOC_DATE");
        objData.AddColumn("MAIN_ACCOUNT_CODE");
        objData.AddColumn("EXPENSE_ID");
        objData.AddColumn("EXPENSE_DESCRIPTION");
        objData.AddColumn("FROM_DATE");
        objData.AddColumn("TO_DATE");
        objData.AddColumn("INVOICE_AMOUNT");
        objData.AddColumn("FROM_READING");
        objData.AddColumn("TO_READING");
        objData.AddColumn("REMARKS");
        objData.AddColumn("AMOUNT1_CAPTION");
        objData.AddColumn("AMOUNT1");
        objData.AddColumn("AMOUNT2_CAPTION");
        objData.AddColumn("AMOUNT2");
        objData.AddColumn("AMOUNT3_CAPTION");
        objData.AddColumn("AMOUNT3");
        objData.AddColumn("AMOUNT4_CAPTION");
        objData.AddColumn("AMOUNT4");
        objData.AddColumn("AMOUNT5_CAPTION");
        objData.AddColumn("AMOUNT5");
        
        try {
            
            String strSQL="";
            ResultSet rsVoucher;
            String ExpenseID="";
            String DocDate="";
            
            //Retrieve data of main voucher
            strSQL="SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO='"+DocNo+"'";
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            if(rsVoucher.getRow()>0) {
                
                ExpenseID=UtilFunctions.getString(rsVoucher,"EXPENSE_ID","");
                DocDate=UtilFunctions.getString(rsVoucher,"DOC_DATE","0000-00-00");
                
                TRow objRow=new TRow();
                objRow.setValue("COMPANY_ID", Integer.toString(CompanyID));
                objRow.setValue("COMPANY_NAME",clsCompany.getCompanyName(CompanyID));
                objRow.setValue("GROUP_DOC_NO",DocNo);
                objRow.setValue("DOC_NO",DocNo);
                objRow.setValue("DOC_DATE",UtilFunctions.getString(rsVoucher,"DOC_DATE","0000-00-00"));
                objRow.setValue("MAIN_ACCOUNT_CODE",clsExpense.getMainCode(ExpenseID));
                objRow.setValue("EXPENSE_ID",EITLERPGLOBAL.padRight(17, ExpenseID, " ")+" "+clsExpense.getExpenseName(ExpenseID));
                objRow.setValue("EXPENSE_DESCRIPTION",clsExpense.getExpenseName(ExpenseID));
                objRow.setValue("FROM_DATE",UtilFunctions.getString(rsVoucher,"FROM_DATE","0000-00-00"));
                objRow.setValue("TO_DATE",UtilFunctions.getString(rsVoucher,"TO_DATE","0000-00-00"));
                objRow.setValue("INVOICE_AMOUNT",Double.toString(UtilFunctions.getDouble(rsVoucher,"INVOICE_AMOUNT",0)));
                objRow.setValue("FROM_READING",UtilFunctions.getString(rsVoucher,"FROM_READING",""));
                objRow.setValue("TO_READING",UtilFunctions.getString(rsVoucher,"TO_READING",""));
                objRow.setValue("REMARKS",UtilFunctions.getString(rsVoucher,"EXPENSE_DESCRIPTION",""));
                objRow.setValue("AMOUNT1_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT1_CAPTION",""));
                objRow.setValue("AMOUNT1",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT1",0)));
                objRow.setValue("AMOUNT2_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT2_CAPTION",""));
                objRow.setValue("AMOUNT2",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT2",0)));
                objRow.setValue("AMOUNT3_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT3_CAPTION",""));
                objRow.setValue("AMOUNT3",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT3",0)));
                objRow.setValue("AMOUNT4_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT4_CAPTION",""));
                objRow.setValue("AMOUNT4",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT4",0)));
                objRow.setValue("AMOUNT5_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT5_CAPTION",""));
                objRow.setValue("AMOUNT5",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT5",0)));
                
                objData.AddRow(objRow);
                
            }
            
            
            strSQL="SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO<>'"+DocNo+"' AND DOC_DATE<='"+DocDate+"' AND DOC_NO<>'"+DocNo+"' AND EXPENSE_ID='"+ExpenseID+"' AND CANCELLED = 0 ORDER BY FROM_DATE DESC LIMIT 6";
            
            //**************** Backward compatibility support *******************//
            boolean IsTelephoneExpenses=false;
            String ExpenseID1="";
            String ExpenseID2="";
            
            
            int ExpenseType=data.getIntValueFromDB("SELECT EXPENSE_TYPE FROM D_FIN_EXPENSE_MASTER WHERE EXPENSE_ID='"+ExpenseID+"'",FinanceGlobal.FinURL);
            
            if(ExpenseType==1) {
                
                if(ExpenseID.trim().endsWith("A")) {
                    ExpenseID1=ExpenseID;
                    ExpenseID2=ExpenseID.trim().substring(0,ExpenseID.trim().length()-1);
                }
                else {
                    ExpenseID1=ExpenseID;
                    ExpenseID2=ExpenseID+"A";
                }
                
                strSQL="SELECT * FROM D_FIN_EXPENSE_TRANSACTION WHERE COMPANY_ID="+CompanyID+" AND DOC_NO<>'"+DocNo+"' AND DOC_DATE<='"+DocDate+"' AND DOC_NO<>'"+DocNo+"' AND (EXPENSE_ID='"+ExpenseID1+"' OR EXPENSE_ID='"+ExpenseID2+"') ORDER BY FROM_DATE DESC LIMIT 12";
            }
            //******************************************************************//
            
            
            //Add Expense History for the current year
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            if(rsVoucher.getRow()>0) {
                
                while(!rsVoucher.isAfterLast()) {
                    ExpenseID=UtilFunctions.getString(rsVoucher,"EXPENSE_ID","");
                    DocDate=UtilFunctions.getString(rsVoucher,"DOC_DATE","0000-00-00");
                    
                    TRow objRow=new TRow();
                    objRow.setValue("COMPANY_ID", Integer.toString(CompanyID));
                    objRow.setValue("COMPANY_NAME",clsCompany.getCompanyName(CompanyID));
                    objRow.setValue("GROUP_DOC_NO",DocNo+"H");
                    objRow.setValue("DOC_NO",UtilFunctions.getString(rsVoucher,"DOC_NO",""));
                    objRow.setValue("DOC_DATE",UtilFunctions.getString(rsVoucher,"DOC_DATE","0000-00-00"));
                    objRow.setValue("MAIN_ACCOUNT_CODE",clsExpense.getMainCode(ExpenseID));
                    objRow.setValue("EXPENSE_ID",ExpenseID);
                    objRow.setValue("EXPENSE_DESCRIPTION",clsExpense.getExpenseName(ExpenseID));
                    objRow.setValue("FROM_DATE",UtilFunctions.getString(rsVoucher,"FROM_DATE","0000-00-00"));
                    objRow.setValue("TO_DATE",UtilFunctions.getString(rsVoucher,"TO_DATE","0000-00-00"));
                    objRow.setValue("INVOICE_AMOUNT",Double.toString(UtilFunctions.getDouble(rsVoucher,"INVOICE_AMOUNT",0)));
                    objRow.setValue("FROM_READING",UtilFunctions.getString(rsVoucher,"FROM_READING",""));
                    objRow.setValue("TO_READING",UtilFunctions.getString(rsVoucher,"TO_READING",""));
                    objRow.setValue("REMARKS",UtilFunctions.getString(rsVoucher,"EXPENSE_DESCRIPTION",""));
                    objRow.setValue("AMOUNT1_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT1_CAPTION",""));
                    objRow.setValue("AMOUNT1",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT1",0)));
                    objRow.setValue("AMOUNT2_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT2_CAPTION",""));
                    objRow.setValue("AMOUNT2",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT2",0)));
                    objRow.setValue("AMOUNT3_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT3_CAPTION",""));
                    objRow.setValue("AMOUNT3",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT3",0)));
                    objRow.setValue("AMOUNT4_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT4_CAPTION",""));
                    objRow.setValue("AMOUNT4",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT4",0)));
                    objRow.setValue("AMOUNT5_CAPTION",UtilFunctions.getString(rsVoucher,"AMOUNT5_CAPTION",""));
                    objRow.setValue("AMOUNT5",Double.toString(UtilFunctions.getDouble(rsVoucher,"AMOUNT5",0)));
                    
                    objData.AddRow(objRow);
                    
                    rsVoucher.next();
                }
                
            }
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
    
    public static TTable getAdvancePaymentReport(int CompanyID,String MainCode,String PartyCode,String FromDate,String ToDate) {
        
        TTable objData=new TTable();
        
        //Populate Columns
        objData.AddColumn("VOUCHER_NO");
        objData.AddColumn("VOUCHER_DATE");
        objData.AddColumn("AMOUNT");
        objData.AddColumn("PO_NO");
        objData.AddColumn("REMARKS");
        
        try {
            
            String strSQL="";
            ResultSet rsVoucher;
            String ExpenseID="";
            String DocDate="";
            
            //Retrieve data of main voucher
            strSQL="SELECT DISTINCT(A.VOUCHER_NO) AS VOUCHER_NO FROM D_FIN_VOUCHER_HEADER A,D_FIN_VOUCHER_DETAIL B WHERE A.VOUCHER_NO=B.VOUCHER_NO AND A.COMPANY_ID=B.COMPANY_ID AND B.MAIN_ACCOUNT_CODE='"+MainCode+"' AND B.SUB_ACCOUNT_CODE='"+PartyCode+"' AND B.EFFECT='D' AND A.CANCELLED=0 ";
            
            if(!FromDate.trim().equals(""))
            {
              strSQL+=" AND A.VOUCHER_DATE>='"+FromDate+"'";  
            }
            
            if(!ToDate.trim().equals(""))
            {
              strSQL+=" AND A.VOUCHER_DATE<='"+ToDate+"' ";  
            }
            
            rsVoucher=data.getResult(strSQL,FinanceGlobal.FinURL);
            rsVoucher.first();
            
            if(rsVoucher.getRow()>0) {
                String VoucherNo=rsVoucher.getString("VOUCHER_NO");
                String VoucherDate=data.getStringValueFromDB("SELECT VOUCHER_DATE FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
                double Amount=data.getDoubleValueFromDB("SELECT ROUND(SUM(AMOUNT),2) AS AMOUNT FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"'",FinanceGlobal.FinURL);
                String PONo=data.getStringValueFromDB("SELECT PO_NO FROM D_FIN_VOUCHER_DETAIL_EX WHERE VOUCHER_NO='"+VoucherNo+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND SUB_ACCOUNT_CODE='"+PartyCode+"'",FinanceGlobal.FinURL);
                String Remarks=data.getStringValueFromDB("SELECT REMARKS FROM D_FIN_VOUCHER_HEADER WHERE VOUCHER_NO='"+VoucherNo+"'",FinanceGlobal.FinURL);
                
                TRow objRow=new TRow();
                objRow.setValue("VOUCHER_NO", VoucherNo);
                objRow.setValue("VOUCHER_DATE",EITLERPGLOBAL.formatDate(VoucherDate));
                objRow.setValue("AMOUNT",Double.toString(Amount));
                objRow.setValue("REMARKS",Remarks);
                
                objData.AddRow(objRow);
            }
            
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return objData;
    }
    
}
