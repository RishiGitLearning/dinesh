/*
 * clsSystemLog.java
 *
 * Created on September 17, 2007, 10:53 AM
 */

package EITLERP.Logging;

/**
 *
 * @author  root
 */
import java.sql.*;
import java.util.*;
import EITLERP.*;

public class clsSystemLog {
    
    public int CompanyID=0;
    public long SrNo=0;
    public String ModuleName="";
    public String Method="";
    public String Trace="";
    public String Remarks="";
    
    /** Creates a new instance of clsSystemLog */
    public clsSystemLog() {
    }
    
    public void LogEvent()
    {
      try
      {
        String strSQL="INSERT INTO SYSTEM_LOG (COMPANY_ID,MODULE_NAME,METHOD,TRACE,REMARKS) VALUES("+CompanyID+",'"+ModuleName+"','"+Method+"','"+Trace+"','"+Remarks+"')";
        data.Execute(strSQL,EITLERPGLOBAL.SystemLogURL);
        
      }
      catch(Exception e)
      {
          
      }
    }
    
}
