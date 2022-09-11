/*
 * EITLComboModel.java
 *
 * Created on April 6, 2004, 1:22 PM
 */
 
package TReportWriter;

import javax.swing.DefaultComboBoxModel;
import java.util.HashMap;

public class TEITLComboModel extends javax.swing.DefaultComboBoxModel
{ 
    
    private HashMap colComboData=new HashMap();
    
    //It will be in the form of ComboData Object
    public void addElement(Object anObject)
    {
        TComboData aData=(TComboData) anObject;
        super.addElement(aData.Text);
        
        int Counter=colComboData.size();
        colComboData.put(Integer.toString(Counter),anObject);
    }
    
    public void insertElementAt(Object anObject,int index)
    {
        TComboData aData=(TComboData) anObject;
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
        TComboData aData = (TComboData) colComboData.get(Long.toString(Index));
        return aData.Code;
    }
    
    public String getText(long Index)
    {
        TComboData aData = (TComboData) colComboData.get(Long.toString(Index));
        return aData.Text;
    }

   public String getstrCode(long Index)
    {
        TComboData aData = (TComboData) colComboData.get(Long.toString(Index));
        return aData.strCode;
    }

    
    
    
    
    
}

    
    

