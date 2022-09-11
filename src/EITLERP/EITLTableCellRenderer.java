/*
 * EITLTableCellRenderer.java
 *
 * Created on May 6, 2004, 12:22 PM
 */
 
package EITLERP;

/**
 *
 * @author  nrpithva
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.util.*;
import javax.swing.event.*;

 
public class EITLTableCellRenderer extends DefaultTableCellRenderer  {
    
    private HashMap colCellColor=new HashMap();
    private HashMap colCellForeColor=new HashMap();
    private HashMap colCellBackColor=new HashMap();
    
    private HashMap colComponent=new HashMap();
    private HashMap colObject=new HashMap();
    private HashMap colRowColor=new HashMap();
    private HashMap colToolTip=new HashMap();
        
    public static String CheckBox="CheckBox";
    
    JCheckBox checkBox = new JCheckBox();
    JComboBox comboBox=new JComboBox();
        
    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus,int row, int column) {
        //Return the component according to settings
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if(getCustomComponent(column).equals("Cell")) {
            /*if(getColor(row,column)!=Color.BLACK) {
                cell.setBackground(getColor(row,column));
            }*/
            
            cell.setForeground(getForeColor(row,column));
            cell.setBackground(getBackColor(row,column));
            
            
            return cell;
        }
        
        if(getCustomComponent(column).equals("CheckBox")) {
            if (value instanceof Boolean) {// Boolean
                checkBox.setBackground(Color.WHITE);
                checkBox.setSelected(((Boolean)value).booleanValue());
                return checkBox;
            }
        }

        if(getCustomComponent(column).equals("ComboBox")) 
        {
                return (JComboBox)getCustomObject(column);
        }
        
        String str = (value == null) ? "" : value.toString();
        return super.getTableCellRendererComponent(table,str,isSelected,hasFocus,row,column);
    }
    
    /** Creates a new instance of EITLTableCellRenderer */
    public EITLTableCellRenderer() {
    }
    
    public void setCustomComponent(int pCol,String pComponent) {
        colComponent.put(Integer.toString(pCol),pComponent);
    }
    
    public void setCustomComponent(int pCol,Object pComponent) {
        colObject.put(Integer.toString(pCol),pComponent);
    }
    
    
    public String getCustomComponent(int pCol) {
        if(colComponent.get(Integer.toString(pCol))==null) {
            return "Cell";
        }
        else {
            return (String)colComponent.get(Integer.toString(pCol));
        }
    }
    
    public Object getCustomObject(int pCol) {
        try
        {
        return colObject.get(Integer.toString(pCol));
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    public void setColor(int row,int col,Color pColor) {
        Dimension theCell=new Dimension(col,row);
        colCellColor.put(theCell.toString(),pColor);
    }
    
    public void setForeColor(int row,int col,Color pColor) {
        Dimension theCell=new Dimension(col,row);
        String theKey=Integer.toString(row).trim()+Integer.toString(col).trim();
        colCellForeColor.put(theKey,pColor);
    }

    
    public void setBackColor(int row,int col,Color pColor) {
        Dimension theCell=new Dimension(col,row);
        String theKey=Integer.toString(row).trim()+Integer.toString(col).trim();
        colCellBackColor.put(theKey,pColor);
    }
    
    public Color getColor(int row,int col) {
        Dimension theCell=new Dimension(col,row);
        Color sColor=(Color)colCellColor.get(theCell.toString());
        
        if(sColor==null) {
            return Color.BLACK;
        }
        else {
            return sColor;
        }
    }
    
    
    public Color getForeColor(int row,int col) {
        Dimension theCell=new Dimension(col,row);
        
        String theKey=Integer.toString(row).trim()+Integer.toString(col).trim();
        Color sColor=(Color)colCellForeColor.get(theKey);
        
        if(sColor==null) {
            return Color.BLACK;
        }
        else {
            return sColor;
        }
    }

    
    public Color getBackColor(int row,int col) {
        Dimension theCell=new Dimension(col,row);
        
        String theKey=Integer.toString(row).trim()+Integer.toString(col).trim();
        Color sColor=(Color)colCellBackColor.get(theKey);
        
        if(sColor==null) {
            return Color.WHITE;
        }
        else {
            return sColor;
        }
    }

    
   public void removeBackColors()
   {
     colCellBackColor.clear();  
   }
   
   public void removeForeColors()
   {
     colCellForeColor.clear();   
   }
   
   public void setCustomToolTipText(int col,String Str) {
        Dimension theCell=new Dimension(col,0);
        String theKey=Integer.toString(0).trim()+Integer.toString(col).trim();
        colToolTip.put(theKey,Str);
    }
    
    public String getCustomToolTipText(int col,String Str) {
        Dimension theCell=new Dimension(col,0);
        String theKey=Integer.toString(0).trim()+Integer.toString(col).trim();
        String ToolTip = colToolTip.get(theKey).toString();
        if(!ToolTip.equals("")) {
            return ToolTip;
        }
        return "";
    }

}
