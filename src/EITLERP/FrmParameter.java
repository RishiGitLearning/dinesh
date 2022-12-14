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
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import EITLERP.*;
 


public class FrmParameter extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsParameter ObjParameter;
    private EITLComboModel cmbParaIDModel;
    
    /** Creates new form frmTemplate */
    public FrmParameter() {
        System.gc();
        setSize(525,325);
        initComponents();
        GenerateCombos();        
        
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

        ObjParameter=new clsParameter();
        
        if(ObjParameter.LoadData())
        {
            SetFields(false); 
            ObjParameter.MoveFirst();
            DisplayData();
            SetMenuForRights();
            ShowMessage("Ready ..........");
        }
        else
        {
            JOptionPane.showMessageDialog(null,"Error loading location data. Error is "+ObjParameter.LastError);
        }
        
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbParaID = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtParaCode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtParaDesc = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtValue = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRemarks = new javax.swing.JTextArea();
        lblStatus = new javax.swing.JLabel();
        
        getContentPane().setLayout(null);
        
        ToolBar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        ToolBar.setRollover(true);
        cmdTop.setToolTipText("First Record");
        cmdTop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTopActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdTop);
        
        cmdBack.setToolTipText("Previous Record");
        cmdBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBackActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdBack);
        
        cmdNext.setToolTipText("Next Record");
        cmdNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNextActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdNext);
        
        cmdLast.setToolTipText("Last Record");
        cmdLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLastActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdLast);
        
        cmdNew.setToolTipText("New Record");
        cmdNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNewActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdNew);
        
        cmdEdit.setToolTipText("Edit");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdEdit);
        
        cmdDelete.setToolTipText("Delete");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdDelete);
        
        cmdSave.setToolTipText("Save");
        cmdSave.setEnabled(false);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdSave);
        
        cmdCancel.setToolTipText("Cancel");
        cmdCancel.setEnabled(false);
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdCancel);
        
        cmdFilter.setToolTipText("Find");
        cmdFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFilterActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdFilter);
        
        cmdPreview.setToolTipText("Preview");
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdPreview);
        
        cmdPrint.setToolTipText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdPrint);
        
        cmdExit.setToolTipText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        
        ToolBar.add(cmdExit);
        
        getContentPane().add(ToolBar);
        ToolBar.setBounds(0, 0, 800, 40);
        
        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("Parameter Master");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 40, 520, 25);
        
        jLabel2.setText("Parameter ID");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(30, 90, 90, 15);
        
        cmbParaID.setNextFocusableComponent(txtParaDesc);
        cmbParaID.setEnabled(false);
        cmbParaID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbParaIDFocusGained(evt);
            }
        });
        
        getContentPane().add(cmbParaID);
        cmbParaID.setBounds(140, 90, 210, 24);
        
        jLabel3.setText("Parameter Code");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(30, 120, 110, 15);
        
        txtParaCode.setNextFocusableComponent(txtParaDesc);
        txtParaCode.setEnabled(false);
        getContentPane().add(txtParaCode);
        txtParaCode.setBounds(140, 120, 40, 19);
        
        jLabel4.setText("Description");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(30, 150, 90, 20);
        
        txtParaDesc.setNextFocusableComponent(txtValue);
        txtParaDesc.setEnabled(false);
        txtParaDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtParaDescFocusGained(evt);
            }
        });
        
        getContentPane().add(txtParaDesc);
        txtParaDesc.setBounds(140, 150, 310, 19);
        
        jLabel5.setText("Value");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(30, 180, 100, 20);
        
        txtValue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValue.setNextFocusableComponent(txtRemarks);
        txtValue.setEnabled(false);
        txtValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValueFocusGained(evt);
            }
        });
        
        getContentPane().add(txtValue);
        txtValue.setBounds(140, 180, 100, 19);
        
        jLabel6.setText("Remarks");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(30, 210, 90, 20);
        
        txtRemarks.setNextFocusableComponent(cmbParaID);
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRemarksFocusGained(evt);
            }
        });
        
        jScrollPane1.setViewportView(txtRemarks);
        
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(140, 210, 310, 40);
        
        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 260, 520, 22);
        
    }//GEN-END:initComponents

    private void txtRemarksFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Remarks if applicable for particular parameter ..........");        
    }//GEN-LAST:event_txtRemarksFocusGained

    private void txtValueFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValueFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Value if Applicalbe for default transaction calculations ..........");
    }//GEN-LAST:event_txtValueFocusGained

    private void txtParaDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtParaDescFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Parameter Description upto 100 characters ..........");
    }//GEN-LAST:event_txtParaDescFocusGained

    private void cmbParaIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbParaIDFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Parameter ID ..........");
    }//GEN-LAST:event_cmbParaIDFocusGained

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdFilterActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed

    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed

    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed

    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed

    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JComboBox cmbParaID;
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtParaCode;
    private javax.swing.JTextField txtParaDesc;
    private javax.swing.JTextArea txtRemarks;
    private javax.swing.JTextField txtValue;
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
     EITLERPGLOBAL.setComboIndex(cmbParaID,(String)ObjParameter.getAttribute("PARA_ID").getObj());
     txtParaCode.setText(Double.toString(ObjParameter.getAttribute("PARA_CODE").getVal())) ;
     txtParaDesc.setText((String)ObjParameter.getAttribute("DESC").getObj());
     txtValue.setText(Double.toString(ObjParameter.getAttribute("VALUE").getVal()));
     txtRemarks.setText((String) ObjParameter.getAttribute("REMARKS").getObj());
    }
    
    //Sets data to the Class Object
    private void SetData()
 {
      ObjParameter.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
      ObjParameter.setAttribute("PARA_ID",cmbParaID.getSelectedItem());
      ObjParameter.setAttribute("PARA_CODE",txtParaCode.getText());     
      ObjParameter.setAttribute("DESC",txtParaDesc.getText());
      ObjParameter.setAttribute("VALUE",Double.parseDouble(txtValue.getText()));
      ObjParameter.setAttribute("REMARKS",txtRemarks.getText());
      
      if(EditMode==EITLERPGLOBAL.ADD)
      {
          ObjParameter.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
          ObjParameter.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
      }
      else
      {
          ObjParameter.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
          ObjParameter.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
      }
    }

private void ClearFields()
{
    txtParaCode.setText("");
    txtParaDesc.setText("");
    cmbParaID.setSelectedIndex(-1);
    txtValue.setText("0.00");
    txtRemarks.setText("");
}

  private void SetFields(boolean pStat)
  {
      txtParaCode.setEnabled(pStat); 
      txtParaDesc.setEnabled(pStat);
      cmbParaID.setEnabled(pStat);
      txtValue.setEnabled(pStat);
      txtRemarks.setEnabled(pStat);
  }    

  private void Cancel()
  {
      DisplayData();
      EditMode=0;
      SetFields(false);
      EnableToolbar();
      ShowMessage("Ready ..........");
  }

  private void MoveFirst()
  {
    ObjParameter.MoveFirst();
    DisplayData();
  }
  
  private void MovePrevious()
  {
    ObjParameter.MovePrevious();
    DisplayData();
  }
  
  private void MoveNext()
  {
    ObjParameter.MoveNext();
    DisplayData();
  }
  
  private void MoveLast()
  {
    ObjParameter.MoveLast();
    DisplayData();
  }
    
  private void Add()
  {
     EditMode=EITLERPGLOBAL.ADD;
     DisableToolbar();
     SetFields(true);
     txtParaCode.setEnabled(false);
     ClearFields();
     cmbParaID.requestFocus();
  }
  
  private void Edit()
  {
     EditMode=EITLERPGLOBAL.EDIT;
     DisableToolbar();
     SetFields(true);
     txtParaCode.setEnabled(false);
     cmbParaID.setEnabled(false);
     txtParaDesc.requestFocus();
  }

  
  private void Delete()
  {
       if(ObjParameter.Delete())
       {
        MoveLast();
        DisplayData();
       }
       else
       {
          JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is"+ObjParameter.LastError);
       }
  }

  private void Save()
  {
      // --------- Form Level Validations ------------ //
      
      if(cmbParaID.getSelectedIndex()== -1)
      {
         JOptionPane.showMessageDialog(null,"Please enter type of Para");
          return;
      }
      
      if(txtParaDesc.getText().equals(""))
      {
          JOptionPane.showMessageDialog(null,"Description is to be Neaded");
          return;
      }
      
      double Value = 0.00;
      Value = Double.parseDouble(txtValue.getText());
      
      if(Value < 0 )
      {
          JOptionPane.showMessageDialog(null,"Value of Parameter should not be less then Zero");
          return;
      }
      
      SetData();
      if(EditMode==EITLERPGLOBAL.ADD)
      {
        if(ObjParameter.Insert())
        {
            MoveLast();
            DisplayData();
        }
        else
        {
          JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjParameter.LastError);
          return;
        }
      }      

      if(EditMode==EITLERPGLOBAL.EDIT)
      {
       if(ObjParameter.Update())  
       {
        //Nothing to do
       }
       else
       {
        JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjParameter.LastError);
        return;
       }
      }
      
      EditMode=0;
      SetFields(false);
      EnableToolbar();
      ShowMessage("Ready ..........");
  }    

 private void SetMenuForRights()
  {
   // --- Add Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,181))
   {
      cmdNew.setEnabled(true);
   }
   else
   {
       cmdNew.setEnabled(false);
   }
      
   // --- Edit Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,182))
   {
      cmdEdit.setEnabled(true);
   }
   else
   {
       cmdEdit.setEnabled(false);
   }
   
   // --- Delete Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,183))
   {
      cmdDelete.setEnabled(true);
   }
   else
   {
      cmdDelete.setEnabled(false);
   }
   
   // --- Print Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,184))
   {
      cmdPreview.setEnabled(true);
      cmdPrint.setEnabled(true);
   }
   else
   {
      cmdPreview.setEnabled(false);
      cmdPrint.setEnabled(false);
   }
   }

  private void GenerateCombos()
  {
   //Generates Combo Boxes
   HashMap List=new HashMap();
   String strCondition="";
   
   //----- Generate cmbType ------- //
   cmbParaIDModel=new EITLComboModel();
   cmbParaID.removeAllItems();
   cmbParaID.setModel(cmbParaIDModel);
   
   strCondition=" WHERE COMPANY_ID="+Integer.toString(EITLERPGLOBAL.gCompanyID)+" GROUP BY PARA_ID";
   
   List=clsParameter.getList(strCondition);
   for(int i=1;i<=List.size();i++)
   {
       clsParameter ObjParameter=(clsParameter) List.get(Integer.toString(i));
       ComboData aData=new ComboData();
       aData.Text=(String) ObjParameter.getAttribute("PARA_ID").getObj();
       aData.strCode=(String) ObjParameter.getAttribute("PARA_ID").getObj();
       cmbParaIDModel.addElement(aData);
   }
  }   

 private void ShowMessage(String pMessage)
 {
     lblStatus.setText(pMessage);
 }
  
}
