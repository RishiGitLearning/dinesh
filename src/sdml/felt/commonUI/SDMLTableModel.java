/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdml.felt.commonUI;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.Frame;

/** 
 *
 * @author  nhpatel
 * @version
 */
public class SDMLTableModel extends DefaultTableModel {
    
        
    private HashMap colReadOnly=new HashMap();
    private HashMap colID=new HashMap();
    private HashMap colVariable=new HashMap(); //Stores Variables
    private HashMap colOperation=new HashMap(); // +/- Operation
    private HashMap colInclude=new HashMap(); //Include in calculation or not
    private HashMap colFormula=new HashMap();
    private HashMap colUserObject=new HashMap(); //Stores user object - for each row
    private HashMap colNumeric=new HashMap();
    private boolean ReadOnlyTable=false;
    

    public void ClearCollections()
    {
        colReadOnly.clear();
        colID.clear();
        colVariable.clear();
        colOperation.clear();
        colInclude.clear();
        colFormula.clear();
        colUserObject.clear();
        colNumeric.clear();
    }
    
    public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
    {
    
        
    }
    
    public boolean editCellAt(int row,int Col)
    {
      JOptionPane.showMessageDialog(null,"Started editing");  
      return true;
    }
    
    

    public Object getValueAt(int row,int column) {
        try {
            if(super.getValueAt(row,column)==null) {
                if(getNumeric(column)) {
                    return "0";
                }
                else {
                    return "";
                }
            }
            else {
                return super.getValueAt(row,column);
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public void SetNumeric(int pCol,boolean pVal) {
        if(pVal==true) {
            colNumeric.put(Integer.toString(pCol),"1");
        }
        else {
            colNumeric.put(Integer.toString(pCol),"0");
        }
    }
    
    public boolean getNumeric(int pCol) {
        try {
            if(colNumeric.get(Integer.toString(pCol))==null) {
                return false; //Default Behaviour - Not Numeric
            }
            else {
                if(colNumeric.get(Integer.toString(pCol)).toString().equals("1")) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public void TableReadOnly(boolean pVal) {
        ReadOnlyTable=pVal;
    }
    
    public void SetOperation(int pCol,String pValue) {
        colOperation.put(Integer.toString(pCol),pValue);
    }
    
    public String getOperation(int pCol) {
        try {
            return (String)colOperation.get(Integer.toString(pCol));
        }
        catch(Exception e){
            return "";
        }
    }
    
    public void SetUserObject(int pRow,Object pObject) {
        colUserObject.put(Integer.toString(pRow),pObject);
    }
    
    public boolean setObjectListAfterDelete(int row) {
        try {
            boolean found = false;
            for(int i=0;i<colUserObject.size()+1;i++) {
                if(i==colUserObject.size()) {
                    break;
                }
                if(row==i) {
                    found=true;
                }
                if(found) {
                    colUserObject.put(Integer.toString(i),colUserObject.get(Integer.toString(i+1)));
                }
            }
        }catch(Exception e) {
        }
        return true;
    }
    
    public Object getUserObject(int pRow) {
        try {
            return colUserObject.get(Integer.toString(pRow));
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public void SetInclude(int pCol,boolean pInclude) {
        try {
            if(pInclude) {
                colInclude.put(Integer.toString(pCol),"TRUE");
            }
            else {
                colInclude.put(Integer.toString(pCol),"FALSE");
            }
        }
        catch(Exception e) {
            
        }
    }
    
    public boolean getInclude(int pCol) {
        try {
            if(colInclude.get(Integer.toString(pCol)).equals("TRUE")) {
                return  true;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    public void SetReadOnly(int pCol) {
        colReadOnly.put(Integer.toString(colReadOnly.size()),Integer.toString(pCol));
    }
    
    public void ClearAllReadOnly()
    {
        try
        {
            colReadOnly.clear();
        }
        catch(Exception e){}
    }
    
    public void SetColID(int pCol,int pID) {
        colID.put(Integer.toString(pCol),Integer.toString(pID));
    }
    
    public int getColID(int pCol) {
        try {
            return Integer.parseInt((String)colID.get(Integer.toString(pCol)));
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public void SetVariable(int pCol,String pVariable) {
        colVariable.put(Integer.toString(pCol),pVariable);
    }
    
    public void SetFormula(int pCol,String pFormula) {
        colFormula.put(Integer.toString(pCol),pFormula);
    }
    
    public String getFormula(int pCol) {
        try {
            if(colFormula.get(Integer.toString(pCol))!=null) {
                return (String)colFormula.get(Integer.toString(pCol));
            }
            else {
                return "";
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    
    
    public int getColFromVariable(String pVarName) {
        try {
            String strVariable="";
            int lnCol=0;
            
            for(int i=0;i<colVariable.size();i++) {
                if(((String)colVariable.get(Integer.toString(i))).equals(pVarName))
                {
                   lnCol=i;
                   break;
                }
                /*strVariable=(String)colVariable.get(Integer.toString(i));
                if(strVariable!=null) {
                    if(strVariable.equals(pVarName)) {
                        lnCol=i;
                        break;
                    }
                }*/
            }
            return lnCol;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public int getColFromID(int pID) {
        try {
            int lnID=0,lnReturnID=0;
            
            for(int i=0;i<colID.size();i++) {
                lnID=Integer.parseInt((String)colID.get(Integer.toString(i)));
                if(lnID==pID) {
                    lnReturnID=i;
                }
            }
            return lnReturnID;
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public void setFormulaByID(int pID,String pFormula) {
        int pCol=getColFromID(pID);
        colFormula.put(Integer.toString(pCol),pFormula);
    }
    
    public String getFormulaByID(int pID) {
        try {
            String strFormula="";
            int pCol=0;
            
            pCol=getColFromID(pID);
            
            if(colFormula.get(Integer.toString(pCol))!=null) {
                strFormula=(String)colFormula.get(Integer.toString(pCol));
            }
            return strFormula;
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public void setValueByVariable(String pVarName,String pValue,int pRow) {
        try
        {
        //int col=getColFromVariable(pVarName);
        super.setValueAt(pValue,pRow,getColFromVariable(pVarName));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void setValueByVariable(String pVarName,Boolean pValue,int pRow) {
        try
        {
        //int col=getColFromVariable(pVarName);
        super.setValueAt(pValue,pRow,getColFromVariable(pVarName));
        }
        catch(Exception e){}
    }
    
    public void addRow(Object[] rowData) {
        super.addRow(rowData);
        
        //By Default put the string
        colUserObject.put(Integer.toString(super.getRowCount()-1),"");
    }
    
    public void addRow(Vector rowData) {
        super.addRow(rowData);
        
        //By Default put the string
        colUserObject.put(Integer.toString(super.getRowCount()-1),"");
    }
    
    public void removeRow(int pRow) {
        super.removeRow(pRow);
        
        try {
            //Remove it from User Objects
            colUserObject.remove(Integer.toString(pRow));
        }
        catch(Exception e)
        {}
    }
    
    
    public String getValueByVariable(String pVarName,int pRow) {
        try {
            int col=getColFromVariable(pVarName);
            
            if(super.getValueAt(pRow, col)==null) {
                if(getNumeric(col))
                {
                 return "0";   
                }
                else
                {
                return "";
                }
            }
            else {
                return (String)super.getValueAt(pRow, col);
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public boolean getBoolValueByVariable(String pVarName,int pRow) {
        try {
            int col=getColFromVariable(pVarName);
            if(super.getValueAt(pRow, col)==null) {
                return false;
            }
            else {
                if(super.getValueAt(pRow, col)==Boolean.TRUE) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch(Exception e) {
            return false;
        }
    }
    
    
    public void setValueByVariableEx(String pVarName,String pValue,int pCol) {
        try {
            //int col=getColFromVariable(pVarName);
            super.setValueAt(pValue,getColFromVariable(pVarName),pCol);
        }
        catch(Exception e){}
    }
    
    public String getValueByVariableEx(String pVarName,int pCol) {
        try {
            int col=getColFromVariable(pVarName);
            if(super.getValueAt(col,pCol)==null) {
                return "0";
            }
            else {
                return (String)super.getValueAt(col,pCol);
            }
        }
        catch(Exception e) {
            return "";
        }
    }
    public String getVariable(int pCol) {
        try {
            return (String)colVariable.get(Integer.toString(pCol));
        }
        catch(Exception e) {
            return "";
        }
    }
    
    public void ResetReadOnly(int pCol) {
        try {
            colReadOnly.remove(Integer.toString(pCol));
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    public void setValueAt(Object aValue,int row, int column) {
        try {
            if(aValue instanceof String) {
                if(getNumeric(column)) {
                    if(!SDMLERPGLOBAL.IsNumber(aValue.toString())) {
                        super.setValueAt("0",row,column);
                        return;
                    }
                }
            }
            
            super.setValueAt(aValue,row,column);
            fireTableCellUpdated(row,column);
        }
        catch(Exception e) {
            
        }
    }
    
    public boolean isCellEditable(int row, int column) {
        try {
            
            boolean Found=true;
            
            // No Column is editable. It's just read only table
            if(ReadOnlyTable==true) {
                return false;
            }
            else {
                
                //Search the collection
                for(int i=0;i<colReadOnly.size();i++) {
                    String Param=Integer.toString(column);
                    String Val=(String) colReadOnly.get(Integer.toString(i));
                    if(Param.equals(Val)) {
                        Found=false;
                    }
                }
                return Found;
            }
        }
        catch(Exception e) {
            return true;
        }
    }
}
