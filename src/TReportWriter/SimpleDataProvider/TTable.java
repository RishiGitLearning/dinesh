/*
 * TTable.java
 *
 * Created on December 7, 2007, 5:09 PM
 */

package TReportWriter.SimpleDataProvider;

/**
 *
 * @author  root
 */
import java.util.*;

public class TTable {
    
    
    private HashMap Columns=new HashMap();
    private HashMap Rows=new HashMap();
    
    /** Creates a new instance of TTable */
    public TTable() {
        
    }
    
    public TReportWriter.SimpleDataProvider.TRow Rows(int RowNo) {
        if(Rows.containsKey(Integer.toString(RowNo))) {
            return (TReportWriter.SimpleDataProvider.TRow)Rows.get(Integer.toString(RowNo));
        }
        else {
            return new TReportWriter.SimpleDataProvider.TRow();
        }
    }
    
    public int getRowCount() {
        return Rows.size();
    }
    
    public HashMap getColumns() {
        return Columns;
    }
    
    public void AddColumn(String ColumnName) {
        TReportWriter.SimpleDataProvider.TColumn objColumn=new TReportWriter.SimpleDataProvider.TColumn(ColumnName);
        Columns.put(ColumnName, objColumn);
    }
    
    public TReportWriter.SimpleDataProvider.TColumn getColumn(String ColumnName) {
        return (TReportWriter.SimpleDataProvider.TColumn)Columns.get(ColumnName);
    }
    
    public TReportWriter.SimpleDataProvider.TRow newRow() {
        TReportWriter.SimpleDataProvider.TRow objRow=new TReportWriter.SimpleDataProvider.TRow();
        
        Iterator keys= Columns.keySet().iterator();
        
        while(keys.hasNext()) {
            TReportWriter.SimpleDataProvider.TColumn objColumn=(TColumn)Columns.get(keys.next());
            
            TReportWriter.SimpleDataProvider.TColumn newColumn=new TColumn(objColumn.ColumnName);
            
            objRow.setValue(newColumn);
        }
        
        return objRow;
    }
    
    public void AddRow() {
        TReportWriter.SimpleDataProvider.TRow objRow=new TReportWriter.SimpleDataProvider.TRow();
        
        Iterator keys= Columns.keySet().iterator();
        
        while(keys.hasNext()) {
            TReportWriter.SimpleDataProvider.TColumn objColumn=(TReportWriter.SimpleDataProvider.TColumn)Columns.get(keys.next());
            
            TReportWriter.SimpleDataProvider.TColumn newColumn=new TReportWriter.SimpleDataProvider.TColumn(objColumn.ColumnName);
            
            objRow.setValue(newColumn);
        }
        
        Rows.put(Integer.toString(Rows.size()+1),objRow);
    }
    
    
    public void AddRow(TReportWriter.SimpleDataProvider.TRow objRow) {
        Rows.put(Integer.toString(Rows.size()+1),objRow);
    }
    
    public void AppendTable(TReportWriter.SimpleDataProvider.TTable objTable)
    {
       for(int i=1;i<=objTable.getRowCount();i++)
       {
          AddRow(objTable.Rows(i)); 
       }
    }
    
    
}
