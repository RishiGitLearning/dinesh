/*
 * frmCurrency.java
 *
 * Created on May 28, 2004, 9:10 AM
 */

package EITLERP;
 
/**
 *  
 * @author  jadave
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;
import java.text.*;

 

public class frmCurrency extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsCurrency ObjCurrency;
    
    /** Creates new form frmCurrency */
    public frmCurrency() {
        System.gc();
        setSize(410,258);
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
        
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        NumberFormatter ObjFormater=new NumberFormatter(decimalFormat);
        ObjFormater.setAllowsInvalid(false);
        txtRate.setFormatterFactory(new DefaultFormatterFactory(ObjFormater));
        
        //Load the Data
        ObjCurrency=new clsCurrency();
        if(ObjCurrency.LoadData((int)EITLERPGLOBAL.gCompanyID)) {
            ObjCurrency.MoveFirst();
            DisplayData();
            //------- Set the menu for User rights ----- //
            SetMenuForRights();
            ShowMessage("Ready ...........");
        }
        else {
            JOptionPane.showMessageDialog(null,"Failed to load data");
        }
        
         SetFields(false);
    }
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,171)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
 
        // --- Edit Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,172)) {
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
 
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,173)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
 
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,174)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
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
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCurrencyID = new javax.swing.JTextField();
        txtDesc = new javax.swing.JTextField();
        txtDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtRate = new javax.swing.JFormattedTextField();
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

        cmdEdit.setToolTipText("Edit Record");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });

        ToolBar.add(cmdEdit);

        cmdDelete.setToolTipText("Delete Record");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });

        ToolBar.add(cmdDelete);

        cmdSave.setToolTipText("Save Record");
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
        ToolBar.add(cmdFilter);

        cmdPreview.setToolTipText("Preview");
        ToolBar.add(cmdPreview);

        cmdPrint.setToolTipText("Print");
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
        jLabel1.setText("CURRENCY MASTER");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 40, 650, 25);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel3.setText("Description");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(12, 54, 70, 20);

        jLabel2.setText("Currency ID");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(12, 20, 82, 15);

        txtCurrencyID.setEditable(false);
        txtCurrencyID.setBorder(new javax.swing.border.EtchedBorder());
        txtCurrencyID.setNextFocusableComponent(txtDesc);
        txtCurrencyID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCurrencyIDActionPerformed(evt);
            }
        });

        jPanel1.add(txtCurrencyID);
        txtCurrencyID.setBounds(96, 18, 82, 19);

        txtDesc.setBorder(new javax.swing.border.EtchedBorder());
        txtDesc.setNextFocusableComponent(txtDate);
        txtDesc.setEnabled(false);
        txtDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDescFocusGained(evt);
            }
        });

        jPanel1.add(txtDesc);
        txtDesc.setBounds(96, 54, 270, 19);

        txtDate.setBorder(new javax.swing.border.EtchedBorder());
        txtDate.setNextFocusableComponent(txtRate);
        txtDate.setEnabled(false);
        txtDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDateActionPerformed(evt);
            }
        });
        txtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDateFocusGained(evt);
            }
        });

        jPanel1.add(txtDate);
        txtDate.setBounds(96, 79, 82, 19);

        jLabel4.setText("Date");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(12, 80, 50, 15);

        jLabel5.setText("Rate");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 104, 40, 15);

        txtRate.setBorder(new javax.swing.border.EtchedBorder());
        txtRate.setNextFocusableComponent(txtDesc);
        txtRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtRateFocusGained(evt);
            }
        });

        jPanel1.add(txtRate);
        txtRate.setBounds(96, 104, 82, 19);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 66, 390, 160);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 230, 410, 22);

    }//GEN-END:initComponents

    private void txtRateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter As on Date Currency Rate ..........");
    }//GEN-LAST:event_txtRateFocusGained

    private void txtDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter As on Date Currency Date ..........");
    }//GEN-LAST:event_txtDateFocusGained

    private void txtDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Currency Description .........");
    }//GEN-LAST:event_txtDescFocusGained
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjCurrency.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void Cancel() {
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        ShowMessage("Ready ..........");
    }
    private void SetFields(boolean pStat) {
        txtDesc.setEnabled(pStat);
        txtDate.setEnabled(pStat);
        txtRate.setEnabled(pStat);
    }
    
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void Save() {
        if(txtDesc.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter currency description");
            return;
        }
        
        if(txtDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter date");
            return;
        }
        
        if(txtRate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter rate");
            return;
        }
        
        SetData();
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjCurrency.Insert()) {
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. "+ObjCurrency.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjCurrency.Update()) {
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. "+ObjCurrency.LastError);
                return;
            }
        }
        
        EditMode=0;
        DisplayData();
        SetFields(false);
        EnableToolbar();
        ShowMessage("Ready ..........");
    }
    
    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed
    
    private void Delete() {
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if(ObjCurrency.Delete()) {
                ObjCurrency.MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error while deleting record. Error is"+ObjCurrency.LastError);
            }
        }
    }
    
    private void txtCurrencyIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCurrencyIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCurrencyIDActionPerformed
    
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
    
    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed
    
    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed
    
    private void MoveFirst() {
        ObjCurrency.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjCurrency.MovePrevious();
        DisplayData();
    }
    
    private void MoveNext() {
        ObjCurrency.MoveNext();
        DisplayData();
    }
    
    private void MoveLast() {
        ObjCurrency.MoveLast();
        DisplayData();
    }
    
    private void Add() {
        EditMode=EITLERPGLOBAL.ADD;
        ClearFields();
        SetFields(true);
        DisableToolbar();
        txtDesc.requestFocus();
    }
    private void ClearFields() {
        txtCurrencyID.setText("");
        txtDesc.setText("");
        txtDate.setText("");
        txtRate.setText("0.0");
    }
    
    private void Edit() {
        EditMode=EITLERPGLOBAL.EDIT;
        SetFields(true);
        DisableToolbar();
        txtDesc.requestFocus();
    }
    
    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed
    
    
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtCurrencyID;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JFormattedTextField txtRate;
    // End of variables declaration//GEN-END:variables
    
    private void EnableToolbar() {
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
    
    private void DisableToolbar() {
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
    private void DisplayData() {
       txtCurrencyID.setText(Integer.toString((int)ObjCurrency.getAttribute("CURRENCY_ID").getVal()));
       txtDesc.setText((String)ObjCurrency.getAttribute("CURRENCY_DESC").getObj());
       txtDate.setText(EITLERPGLOBAL.formatDate((String)ObjCurrency.getAttribute("CURRENCY_DATE").getObj()));       
       txtRate.setText(Double.toString(ObjCurrency.getAttribute("CURRENCY_RATE").getVal()));
    }
    
    //Sets data to the Class Object
    private void SetData() {
        ObjCurrency.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);        
        ObjCurrency.setAttribute("CURRENCY_DESC",txtDesc.getText());
        ObjCurrency.setAttribute("CURRENCY_DATE",EITLERPGLOBAL.formatDateDB(txtDate.getText()));
        ObjCurrency.setAttribute("CURRENCY_RATE", Float.parseFloat(txtRate.getText()));
         if(EditMode==EITLERPGLOBAL.ADD)
        {
          ObjCurrency.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
          ObjCurrency.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else
        {
          ObjCurrency.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
          ObjCurrency.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
    }

 private void ShowMessage(String pMessage)
 {
     lblStatus.setText(pMessage);
 }
    
}