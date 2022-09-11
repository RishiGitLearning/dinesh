/*
 * TColumn.java
 *
 * Created on December 7, 2007, 5:09 PM
 */

package TReportWriter.SimpleDataProvider;

/**
 *
 * @author  root
 */
public class TColumn {
    
    public String ColumnName="";
    public String Value="";
        
    /** Creates a new instance of TColumn */
    public TColumn() {
    }
    
    public TColumn(String pColumnName,String pValue)
    {
      ColumnName=pColumnName;
      Value=pValue;
    }
    
    public TColumn(String pColumnName)
    {
      ColumnName=pColumnName;
      Value="";
    }
    
}
