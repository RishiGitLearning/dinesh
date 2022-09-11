/*
 * frmTemplate.java
 *
 * Created on April 7, 2004, 3:10 PM
 */

/**
 *
 * @author  nhpatel
 */
package EITLERP;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import EITLERP.*;


public class FrmFirstFree extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsFirstFree ObjFirstFree;
    private EITLComboModel cmbModuleIdModel;
    
    /** Creates new form frmTemplate */
    public FrmFirstFree() {
        System.gc();
        setSize(500,350);
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
        
        ObjFirstFree=new clsFirstFree();
        
        if(ObjFirstFree.LoadData()) {
            SetFields(false);
            ObjFirstFree.MoveFirst();
            DisplayData();
            SetMenuForRights();
            ShowMessage("Ready ..........");
        }
        else {
            JOptionPane.showMessageDialog(null,"Error loading First Free. Error is "+ObjFirstFree.LastError);
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
        jLabel2 = new javax.swing.JLabel();
        cmbModuleID = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtPrefix = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtLastUsedNo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        ChkBlocked = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        txtBlockedDate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPaddingBy = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTotalLength = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtSuffix = new javax.swing.JTextField();

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

        jLabel2.setText("Module Name");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(44, 90, 90, 20);

        cmbModuleID.setNextFocusableComponent(txtPrefix);
        cmbModuleID.setEnabled(false);
        cmbModuleID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbModuleIDFocusGained(evt);
            }
        });

        getContentPane().add(cmbModuleID);
        cmbModuleID.setBounds(140, 90, 260, 24);

        jLabel3.setText("Prefix Character");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(30, 120, 110, 20);

        txtPrefix.setNextFocusableComponent(txtLastUsedNo);
        txtPrefix.setEnabled(false);
        txtPrefix.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPrefixFocusGained(evt);
            }
        });

        getContentPane().add(txtPrefix);
        txtPrefix.setBounds(140, 120, 91, 21);

        jLabel4.setText("Last Used No.");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(44, 150, 90, 20);

        txtLastUsedNo.setNextFocusableComponent(txtPaddingBy);
        txtLastUsedNo.setEnabled(false);
        txtLastUsedNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLastUsedNoFocusGained(evt);
            }
        });

        getContentPane().add(txtLastUsedNo);
        txtLastUsedNo.setBounds(140, 150, 90, 21);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText(" FIRST FREE NO");
        jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 40, 470, 25);

        ChkBlocked.setText("Blocked Series");
        ChkBlocked.setNextFocusableComponent(txtBlockedDate);
        ChkBlocked.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ChkBlockedFocusGained(evt);
            }
        });
        ChkBlocked.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChkBlockedItemStateChanged(evt);
            }
        });

        getContentPane().add(ChkBlocked);
        ChkBlocked.setBounds(140, 240, 236, 23);

        jLabel5.setText("Blocked Date");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(140, 270, 90, 20);

        txtBlockedDate.setNextFocusableComponent(cmbModuleID);
        txtBlockedDate.setEnabled(false);
        txtBlockedDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBlockedDateFocusGained(evt);
            }
        });

        getContentPane().add(txtBlockedDate);
        txtBlockedDate.setBounds(238, 270, 90, 21);

        jLabel6.setText("Padding by");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(58, 181, 77, 20);

        txtPaddingBy.setNextFocusableComponent(txtTotalLength);
        txtPaddingBy.setEnabled(false);
        txtPaddingBy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPaddingByFocusGained(evt);
            }
        });

        getContentPane().add(txtPaddingBy);
        txtPaddingBy.setBounds(140, 180, 91, 21);

        jLabel7.setText("Total No. Length");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(25, 210, 106, 20);

        txtTotalLength.setNextFocusableComponent(ChkBlocked);
        txtTotalLength.setEnabled(false);
        txtTotalLength.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTotalLengthFocusGained(evt);
            }
        });

        getContentPane().add(txtTotalLength);
        txtTotalLength.setBounds(140, 210, 91, 21);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 300, 470, 26);

        jLabel8.setText("Suffix");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(250, 120, 47, 20);

        txtSuffix.setNextFocusableComponent(txtLastUsedNo);
        txtSuffix.setEnabled(false);
        getContentPane().add(txtSuffix);
        txtSuffix.setBounds(296, 121, 91, 21);

    }//GEN-END:initComponents
    
    private void ChkBlockedFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ChkBlockedFocusGained
        // TODO add your handling code here:
        ShowMessage("Select if you want document transactions to be Blocked ..........");
    }//GEN-LAST:event_ChkBlockedFocusGained
    
    private void txtBlockedDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBlockedDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Blocked Date in [DD/MM/YYYY] format ..........");
    }//GEN-LAST:event_txtBlockedDateFocusGained
    
    private void txtTotalLengthFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotalLengthFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter total length of Doucment No. ..........");
    }//GEN-LAST:event_txtTotalLengthFocusGained
    
    private void txtPaddingByFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPaddingByFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Padding character (Eg. 0) ..........");
    }//GEN-LAST:event_txtPaddingByFocusGained
    
    private void txtLastUsedNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLastUsedNoFocusGained
        // TODO add your handling code here:
        ShowMessage("Specify Last used No. Documented as on date ..........");
    }//GEN-LAST:event_txtLastUsedNoFocusGained
    
    private void txtPrefixFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrefixFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Prefix Characters for individual Modules ..........");
    }//GEN-LAST:event_txtPrefixFocusGained
    
    private void cmbModuleIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbModuleIDFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Modules from given List ..........");
    }//GEN-LAST:event_cmbModuleIDFocusGained
    
    private void ChkBlockedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChkBlockedItemStateChanged
        // TODO add your handling code here:
        if(ChkBlocked.isSelected()) {
            txtBlockedDate.setEnabled(true);
            txtBlockedDate.setText((String) EITLERPGLOBAL.getCurrentDateDB());
        }
        else
            txtBlockedDate.setEnabled(false);
        txtBlockedDate.setText("");
    }//GEN-LAST:event_ChkBlockedItemStateChanged
    
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
        Find();
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
    private javax.swing.JCheckBox ChkBlocked;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JComboBox cmbModuleID;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtBlockedDate;
    private javax.swing.JTextField txtLastUsedNo;
    private javax.swing.JTextField txtPaddingBy;
    private javax.swing.JTextField txtPrefix;
    private javax.swing.JTextField txtSuffix;
    private javax.swing.JTextField txtTotalLength;
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
        try {
            ClearFields();
            EITLERPGLOBAL.setComboIndex(cmbModuleID,(int) ObjFirstFree.getAttribute("MODULE_ID").getVal());
            txtPrefix.setText((String) ObjFirstFree.getAttribute("PREFIX_CHARS").getObj()) ;
            txtSuffix.setText((String) ObjFirstFree.getAttribute("SUFFIX_CHARS").getObj()) ;
            txtLastUsedNo.setText((String)ObjFirstFree.getAttribute("LAST_USED_NO").getObj());
            String Block = (String) ObjFirstFree.getAttribute("BLOCKED").getObj();
            
            if (Block.equals("Y")) {
                ChkBlocked.setSelected(true);
                txtBlockedDate.setText(EITLERPGLOBAL.formatDate((String) ObjFirstFree.getAttribute("BLOCKED_DATE").getObj()));
            }else {
                ChkBlocked.setSelected(false);
            }
            
            txtPaddingBy.setText((String) ObjFirstFree.getAttribute("PADDING_BY").getObj());
            txtTotalLength.setText((String) Integer.toString((int)ObjFirstFree.getAttribute("NO_LENGTH").getVal()));
        }
        catch(Exception e) {
            
        }
    }
    
    //Sets data to the Class Object
    private void SetData() {
        
        ObjFirstFree.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjFirstFree.setAttribute("MODULE_ID",EITLERPGLOBAL.getComboCode(cmbModuleID));
        ObjFirstFree.setAttribute("PREFIX_CHARS",txtPrefix.getText());
        ObjFirstFree.setAttribute("SUFFIX_CHARS",txtSuffix.getText());
        ObjFirstFree.setAttribute("LAST_USED_NO",txtLastUsedNo.getText());
        ObjFirstFree.setAttribute("PADDING_BY",txtPaddingBy.getText());
        
        if(EITLERPGLOBAL.IsNumber(txtTotalLength.getText())) {
            ObjFirstFree.setAttribute("NO_LENGTH",(int)Double.parseDouble(txtTotalLength.getText()));
        }
        else {
            ObjFirstFree.setAttribute("NO_LENGTH",10);
        }
        
        if(ChkBlocked.isSelected()) {
            ObjFirstFree.setAttribute("BLOCKED","Y");
        }
        else {
            ObjFirstFree.setAttribute("BLOCKED","N");
        }
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            ObjFirstFree.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
            ObjFirstFree.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
            ObjFirstFree.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            ObjFirstFree.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        
    }
    
    private void ClearFields() {
        txtPrefix.setText("");
        txtSuffix.setText("");
        txtLastUsedNo.setText("");
        cmbModuleID.setSelectedIndex(-1);
        txtBlockedDate.setText("");
        ChkBlocked.setSelected(false);
        txtPaddingBy.setText("");
        txtTotalLength.setText("0");
    }
    
    private void SetFields(boolean pStat) {
        txtPrefix.setEnabled(pStat);
        txtSuffix.setEnabled(pStat);
        txtLastUsedNo.setEnabled(pStat);
        cmbModuleID.setEnabled(pStat);
        ChkBlocked.setEnabled(pStat);
        txtPaddingBy.setEnabled(pStat);
        txtTotalLength.setEnabled(pStat);
        
        if (ChkBlocked.isSelected()) {
            txtBlockedDate.setEnabled(true);
        }
        else
        {
            txtBlockedDate.setEnabled(false);
        }
    }
    
    private void Cancel() {
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        ShowMessage("Ready ..........");
    }
    
    
    private void MoveFirst() {
        ObjFirstFree.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjFirstFree.MovePrevious();
        DisplayData();
    }
    
    private void MoveNext() {
        ObjFirstFree.MoveNext();
        DisplayData();
    }
    
    private void MoveLast() {
        ObjFirstFree.MoveLast();
        DisplayData();
    }
    
    private void Add() {
        EditMode=EITLERPGLOBAL.ADD;
        DisableToolbar();
        SetFields(true);
        ClearFields();
        cmbModuleID.requestFocus();
    }
    
    private void Edit() {
        EditMode=EITLERPGLOBAL.EDIT;
        DisableToolbar();
        SetFields(true);
        cmbModuleID.setEnabled(false);
        txtLastUsedNo.requestFocus();
    }
    
    private void Delete() {
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","Confirmation",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if(ObjFirstFree.Delete()) {
                MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is"+ObjFirstFree.LastError);
            }
        }
    }
    
    private void Find() {
        
        Loader ObjLoader=new Loader(this,"EITLERP.frmFirstFreeFind",true);
        frmFirstFreeFind ObjReturn= (frmFirstFreeFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjFirstFree.Filter(ObjReturn.strQuery)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveLast();
        }
    }
    
    private void Save() {
        
        // --------- Form Level Validations ------------ //
        if(cmbModuleID.getSelectedIndex()== -1) {
            JOptionPane.showMessageDialog(null,"Please select Module Name");
            return;
        }
        
        if(txtLastUsedNo.getText().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Last used no for starting Module reference");
            return;
        }
        
        if(!EITLERPGLOBAL.IsNumber(txtLastUsedNo.getText()))
        {
           JOptionPane.showMessageDialog(null,"Please enter number in last used no.");
           return;
        }
        
        SetData();
        if(ChkBlocked.isSelected()) {
            ObjFirstFree.setAttribute("BLOCKED","Y");
        }
        else {
            ObjFirstFree.setAttribute("BLOCKED","N");
        }
        
        
        ObjFirstFree.setAttribute("MODULE_ID",EITLERPGLOBAL.getComboCode(cmbModuleID));
        ObjFirstFree.setAttribute("BLOCKED_DATE",EITLERPGLOBAL.formatDateDB(txtBlockedDate.getText()));
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjFirstFree.Insert()) {
                MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjFirstFree.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjFirstFree.Update()) {
                //Nothing to do
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjFirstFree.LastError);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        ShowMessage("Ready ..........");
    }
    
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,231)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,232)) {
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,233)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,234)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbType ------- //
        cmbModuleIdModel=new EITLComboModel();
        cmbModuleID.removeAllItems();
        cmbModuleID.setModel(cmbModuleIdModel);
        
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID);
        
        List=clsModules.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Text=(String) ObjModules.getAttribute("MODULE_DESC").getObj();
            aData.Code=(int) ObjModules.getAttribute("MODULE_ID").getVal();
            cmbModuleIdModel.addElement(aData);
        }
    }
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }
    
}