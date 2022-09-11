/*
 * TRow.java
 *
 * Created on December 7, 2007, 5:09 PM
 */

package EITLERP.Utils.SimpleDataProvider;

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
        TColumn objColumn=(TColumn)Row.get(ColumnName);
        
        Value=objColumn.Value;
      }
      
      return Value;
    }
    
    public void setValue(String ColumnName,String Value)
    {
      TColumn objColumn=new TColumn(ColumnName);
      objColumn.Value=Value;
      
      Row.put(ColumnName,objColumn);
    }
    
    public void setValue(TColumn objColumn)
    {
      Row.put(objColumn.ColumnName,objColumn);  
    }
    
}
