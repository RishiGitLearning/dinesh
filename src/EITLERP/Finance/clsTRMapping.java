/*
 * clsTRMapping.java
 *
 * Created on December 7, 2007, 11:45 AM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import EITLERP.*;

public class clsTRMapping {
    
    /** Creates a new instance of clsTRMapping */
    public clsTRMapping() {
        
    }
    
    public static String getMainCode(int CompanyID,String TRCode)
    {
      String MainCode="";
      
      try
      {
         if(data.IsRecordExist("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_TR_MAPPING WHERE ITEM_CODE='"+TRCode+"'",FinanceGlobal.FinURL))
         {
            MainCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_TR_MAPPING WHERE ITEM_CODE='"+TRCode+"'",FinanceGlobal.FinURL);
         }
             
      }
      catch(Exception e)
      {
          
      }
      
      return MainCode;
    }

    public static String getMainCodeFromItemCode(int CompanyID,String ItemCode)
    {
      String MainCode="";
      
      try
      {
         if(data.IsRecordExist("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_ITEM_CODE_MAPPING WHERE COMPANY_ID="+CompanyID+" AND ITEM_CODE='"+ItemCode+"'",FinanceGlobal.FinURL))
         {
            MainCode=data.getStringValueFromDB("SELECT MAIN_ACCOUNT_CODE FROM D_FIN_ITEM_CODE_MAPPING WHERE COMPANY_ID="+CompanyID+" AND ITEM_CODE='"+ItemCode+"'",FinanceGlobal.FinURL);
         }
             
      }
      catch(Exception e)
      {
          
      }
      
      return MainCode;
    }
    
}
