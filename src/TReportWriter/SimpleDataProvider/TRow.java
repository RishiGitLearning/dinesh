/*
 * TRow.java
 *
 * Created on December 7, 2007, 5:09 PM
 */

package TReportWriter.SimpleDataProvider;

/**
 *
 * @author  root
 */
import java.util.*;

public class TRow {
    
    HashMap Row=new HashMap();
       
    public String getValue(String ColumnName)
    {
      String Value="";
      
      if(Row.containsKey(ColumnName))
      {
        TReportWriter.SimpleDataProvider.TColumn objColumn=(TReportWriter.SimpleDataProvider.TColumn)Row.get(ColumnName);
        
        Value=objColumn.Value;
      }
      
      return Value;
    }
    
    public void setValue(String ColumnName,String Value)
    {
      TReportWriter.SimpleDataProvider.TColumn objColumn=new TReportWriter.SimpleDataProvider.TColumn(ColumnName);
      objColumn.Value=Value;
      
      Row.put(ColumnName,objColumn);
    }
    
    public void setValue(TReportWriter.SimpleDataProvider.TColumn objColumn)
    {
      Row.put(objColumn.ColumnName,objColumn);  
    }
    
}
