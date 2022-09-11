/*
 * frmTemplate.java
 *
 * Created on April 7, 2004, 3:10 PM
 */

package EITLERP;
 
/**
 * 
 * @author  nhpatel
 */
import javax.swing.*;
import java.awt.*;

 
public class frmTemplate extends javax.swing.JApplet {

    private int EditMode=0;
    
    /** Creates new form frmTemplate */
    public frmTemplate() {
        System.gc();
        initComponents();
        
        //Now show the Images
        cmdTop.setIcon(EITLERPGLOBAL.getImage("TOP"));
        cmdBack.setIcon(EITLERPGLOBAL.getImage("BACK"));
        cmdNext.setIcon(EITLERPGLOBAL.getImage("NEXT"));
        cmdLast.setIcon(EITLERPGLOBAL.getImage("LAST"));
        cmdNew.setIcon(EITLERPGLOBAL.getImage("NEW"));
        cmdEdit.setIcon(EITLERPGLOBAL.getImage("EDIT"));
        cmdDelete.setIcon(EITLERPGLOBAL.getImage("DELETE"));
        cmdSave.setIcon(EITLERPGLOBAL.getImage("SAVE"));
        cmdCancel.setIcon(EITLERPGLOBAL.getImage("UNDO"));
        cmdFilter.setIcon(EITLERPGLOBAL.getImage("FIND"));
        cmdPreview.setIcon(EITLERPGLOBAL.getImage("PREVIEW"));
        cmdPrint.setIcon(EITLERPGLOBAL.getImage("PRINT"));
        cmdExit.setIcon(EITLERPGLOBAL.getImage("EXIT"));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        ToolBar = new javax.swing.JToolBar();
        cmdTop = new javax.swing.JButton();
        cmdBack = new javax.swing.JButton();
        cmdNext = new javax.swing.JButton();
        cmdLast = new javax.swing.JButton();
        cmdNew = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        cmdSave = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdFilter = new javax.swing.JButton();
        cmdPreview = new javax.swing.JButton();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        
        getContentPane().setLayout(null);
        
        ToolBar.setBackground(java.awt.Color.lightGray);
        ToolBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        ToolBar.setRollover(true);
        ToolBar.add(cmdTop);
        
        ToolBar.add(cmdBack);
        
        ToolBar.add(cmdNext);
        
        ToolBar.add(cmdLast);
        
        ToolBar.add(cmdNew);
        
        ToolBar.add(cmdEdit);
        
        ToolBar.add(cmdDelete);
        
        cmdSave.setEnabled(false);
        ToolBar.add(cmdSave);
        
        cmdCancel.setEnabled(false);
        ToolBar.add(cmdCancel);
        
        ToolBar.add(cmdFilter);
        
        ToolBar.add(cmdPreview);
        
        ToolBar.add(cmdPrint);
        
        ToolBar.add(cmdExit);
        
        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);
        
    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdTop;
    // End of variables declaration//GEN-END:variables

  private void EnableToolbar()
  {
      //Puts toolbar in enable mode
      cmdTop.setEnabled(true);
      cmdBack.setEnabled(true);
      cmdNext.setEnabled(true);
      cmdLast.setEnabled(true);
      cmdNew.setEnabled(true);
      cmdEdit.setEnabled(true);
      cmdDelete.setEnabled(true);
      cmdSave.setEnabled(false);
      cmdCancel.setEnabled(false);
      cmdFilter.setEnabled(true);
      cmdPreview.setEnabled(true);
      cmdPrint.setEnabled(true);
      cmdExit.setEnabled(true);
  }
  
  private void DisableToolbar()
  {
      //Puts toolbar in enable mode
      cmdTop.setEnabled(false);
      cmdBack.setEnabled(false);
      cmdNext.setEnabled(false);
      cmdLast.setEnabled(false);
      cmdNew.setEnabled(false);
      cmdEdit.setEnabled(false);
      cmdDelete.setEnabled(false);
      cmdSave.setEnabled(true);
      cmdCancel.setEnabled(true);
      cmdFilter.setEnabled(false);
      cmdPreview.setEnabled(false);
      cmdPrint.setEnabled(false);
      cmdExit.setEnabled(false);
  }
  
  //Didplay data on the Screen
  private void DisplayData()
  {
  }

  //Sets data to the Class Object
  private void SetData()
  {
  }
  
}
