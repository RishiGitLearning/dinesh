/*
 * TTable.java
 *
 * Created on December 7, 2007, 5:09 PM
 */

package EITLERP.Utils.SimpleDataProvider;

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
    
    public TRow Rows(int RowNo) {
        if(Rows.containsKey(Integer.toString(RowNo))) {
            return (TRow)Rows.get(Integer.toString(RowNo));
        }
        else {
            return new TRow();
        }
    }
    
    public int getRowCount() {
        return Rows.size();
    }
    
    public HashMap getColumns() {
        return Columns;
    }
    
    
    public void AddColumn(String ColumnName) {
        TColumn objColumn=new TColumn(ColumnName);
        Columns.put(ColumnName, objColumn);
    }
    
    public TColumn getColumn(String ColumnName) {
        return (TColumn)Columns.get(ColumnName);
    }
    
    public TRow newRow() {
        TRow objRow=new TRow();
        
        Iterator keys= Columns.keySet().iterator();
        
        while(keys.hasNext()) {
            TColumn objColumn=(TColumn)Columns.get(keys.next());
            
            TColumn newColumn=new TColumn(objColumn.ColumnName);
            
            objRow.setValue(newColumn);
        }
        
        return objRow;
    }
    
    public void AddRow() {
        TRow objRow=new TRow();
        
        Iterator keys= Columns.keySet().iterator();
        
        while(keys.hasNext()) {
            TColumn objColumn=(TColumn)Columns.get(keys.next());
            
            TColumn newColumn=new TColumn(objColumn.ColumnName);
            
            objRow.setValue(newColumn);
        }
        
        Rows.put(Integer.toString(Rows.size()+1),objRow);
    }
    
    
    public void AddRow(TRow objRow) {
        Rows.put(Integer.toString(Rows.size()+1),objRow);
    }
    
}
