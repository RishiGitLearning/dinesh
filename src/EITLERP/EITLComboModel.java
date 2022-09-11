/*
 * EITLComboModel.java
 *
 * Created on April 6, 2004, 1:22 PM
 */
 
package EITLERP;

import javax.swing.*;
import java.util.*;

public class EITLComboModel extends javax.swing.DefaultComboBoxModel
{ 
    
    private HashMap colComboData=new HashMap();
    
    //It will be in the form of ComboData Object
    public void addElement(Object anObject)
    {
        ComboData aData=(ComboData) anObject;
        super.addElement(aData.Text);
        
        int Counter=colComboData.size();
        colComboData.put(Integer.toString(Counter),anObject);
    }
    
    public void insertElementAt(Object anObject,int index)
    {
        ComboData aData=(ComboData) anObject;
        super.insertElementAt(aData.Text,index);
        
        colComboData.put(Integer.toString(index),anObject);
    }

    public void removeAllElements()
    {
        colComboData.clear();
        super.removeAllElements();
    }
    
    public void removeElementAt(int index)
    {
        super.removeElementAt(index);
        colComboData.remove(Integer.toString(index));
    }
    
    
    public long getCode(long Index)
    {
        ComboData aData = (ComboData) colComboData.get(Long.toString(Index));
        return aData.Code;
    }
    
    public String getText(long Index)
    {
        ComboData aData = (ComboData) colComboData.get(Long.toString(Index));
        return aData.Text;
    }

   public String getstrCode(long Index)
    {
        ComboData aData = (ComboData) colComboData.get(Long.toString(Index));
        return aData.strCode;
    }

    
    
    
    
    
}

    
    

