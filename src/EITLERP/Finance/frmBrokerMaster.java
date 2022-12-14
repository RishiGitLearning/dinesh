/*
 * frmTemplate.java
 *
 * Created on April 7, 2004, 3:10 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  nitin
 */
/*<APPLET CODE=frmInward.class HEIGHT=500 WIDTH=665></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.text.*;
import EITLERP.Utils.*;
import java.sql.*;
import java.net.*;
import EITLERP.*;

public class frmBrokerMaster extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsBrokerMaster objBrokerMaster;
    
    private boolean Updating=false;
    private String theDocNo="";
    
        
    /** Creates new form frmTemplate */
    public frmBrokerMaster() {
        
        setSize(670,330);
        
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

                
        objBrokerMaster=new clsBrokerMaster();
        
        if(objBrokerMaster.LoadData(EITLERPGLOBAL.gCompanyID)) {
            objBrokerMaster.MoveFirst();
            DisplayData();
            SetMenuForRights();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while loading data. Error is "+objBrokerMaster.LastError);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jList1 = new javax.swing.JList();
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
        lblTitle = new javax.swing.JLabel();
        Tab = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        lblBrokerCode = new javax.swing.JLabel();
        txtBrokerCode = new javax.swing.JTextField();
        lblBrokerName = new javax.swing.JLabel();
        txtBrokerName = new javax.swing.JTextField();
        lblBrokerAddress = new javax.swing.JLabel();
        lblBrokerCity = new javax.swing.JLabel();
        lblBrokerPincode = new javax.swing.JLabel();
        txtBrokerAddress = new javax.swing.JTextField();
        txtBrokerCity = new javax.swing.JTextField();
        txtBrokerPincode = new javax.swing.JTextField();
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

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText(" BROKER MASTER");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 666, 25);

        Tab1.setLayout(null);

        Tab1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                Tab1FocusGained(evt);
            }
        });
        Tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Tab1MouseClicked(evt);
            }
        });

        lblBrokerCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBrokerCode.setText("Broker Code :");
        Tab1.add(lblBrokerCode);
        lblBrokerCode.setBounds(49, 13, 90, 15);

        txtBrokerCode.setEnabled(false);
        txtBrokerCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBrokerCodeActionPerformed(evt);
            }
        });
        txtBrokerCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBrokerCodeFocusGained(evt);
            }
        });

        Tab1.add(txtBrokerCode);
        txtBrokerCode.setBounds(147, 11, 124, 21);

        lblBrokerName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBrokerName.setText("Broker Name :");
        Tab1.add(lblBrokerName);
        lblBrokerName.setBounds(42, 47, 97, 15);

        txtBrokerName.setEnabled(false);
        txtBrokerName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBrokerNameFocusGained(evt);
            }
        });

        Tab1.add(txtBrokerName);
        txtBrokerName.setBounds(147, 48, 470, 21);

        lblBrokerAddress.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBrokerAddress.setText("Address :");
        Tab1.add(lblBrokerAddress);
        lblBrokerAddress.setBounds(58, 80, 80, 15);

        lblBrokerCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBrokerCity.setText("City :");
        Tab1.add(lblBrokerCity);
        lblBrokerCity.setBounds(65, 110, 72, 15);

        lblBrokerPincode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBrokerPincode.setText("Pincode :");
        Tab1.add(lblBrokerPincode);
        lblBrokerPincode.setBounds(75, 141, 63, 15);

        txtBrokerAddress.setEnabled(false);
        txtBrokerAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBrokerAddressFocusGained(evt);
            }
        });

        Tab1.add(txtBrokerAddress);
        txtBrokerAddress.setBounds(147, 80, 470, 21);

        txtBrokerCity.setEnabled(false);
        txtBrokerCity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBrokerCityFocusGained(evt);
            }
        });

        Tab1.add(txtBrokerCity);
        txtBrokerCity.setBounds(147, 110, 260, 21);

        txtBrokerPincode.setEnabled(false);
        txtBrokerPincode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBrokerPincodeFocusGained(evt);
            }
        });

        Tab1.add(txtBrokerPincode);
        txtBrokerPincode.setBounds(147, 140, 120, 21);

        Tab.addTab("Broker Details", Tab1);

        getContentPane().add(Tab);
        Tab.setBounds(2, 66, 654, 200);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(0, 270, 650, 22);

    }//GEN-END:initComponents

    private void txtBrokerPincodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokerPincodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Broker Pincode ........");
    }//GEN-LAST:event_txtBrokerPincodeFocusGained

    private void txtBrokerCityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokerCityFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Broker City ........");
    }//GEN-LAST:event_txtBrokerCityFocusGained

    private void txtBrokerAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokerAddressFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Broker Address ........");
    }//GEN-LAST:event_txtBrokerAddressFocusGained

    private void txtBrokerNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokerNameFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Broker Name ........");
    }//GEN-LAST:event_txtBrokerNameFocusGained

    private void txtBrokerCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBrokerCodeFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Broker Code ........");
    }//GEN-LAST:event_txtBrokerCodeFocusGained

    private void txtBrokerCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBrokerCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBrokerCodeActionPerformed
    
    private void Tab1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_Tab1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_Tab1FocusGained
    
    private void Tab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Tab1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Tab1MouseClicked
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        objBrokerMaster.Close();
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
    private javax.swing.JTabbedPane Tab;
    private javax.swing.JPanel Tab1;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
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
    private javax.swing.JList jList1;
    private javax.swing.JLabel lblBrokerAddress;
    private javax.swing.JLabel lblBrokerCity;
    private javax.swing.JLabel lblBrokerCode;
    private javax.swing.JLabel lblBrokerName;
    private javax.swing.JLabel lblBrokerPincode;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtBrokerAddress;
    private javax.swing.JTextField txtBrokerCity;
    private javax.swing.JTextField txtBrokerCode;
    private javax.swing.JTextField txtBrokerName;
    private javax.swing.JTextField txtBrokerPincode;
    // End of variables declaration//GEN-END:variables
    
    private void EnableToolbar() {
        //Puts toolbar in enable mode
        cmdTop.setEnabled(true);
        cmdBack.setEnabled(true);
        cmdNext.setEnabled(true);
        cmdLast.setEnabled(true);
        cmdNew.setEnabled(true);
        cmdEdit.setEnabled(true);
        cmdDelete.setEnabled(false);
        cmdSave.setEnabled(false);
        cmdCancel.setEnabled(false);
        cmdFilter.setEnabled(true);
        cmdPreview.setEnabled(false);
        cmdPrint.setEnabled(false);
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
    
    
    private void SetFields(boolean pStat) {
        txtBrokerCode.setEnabled(pStat);
        txtBrokerName.setEnabled(pStat);        
        txtBrokerAddress.setEnabled(pStat);
        txtBrokerCity.setEnabled(pStat);
        txtBrokerPincode.setEnabled(pStat);
    }
    
    private void ClearFields() {
        txtBrokerCode.setText("");
        txtBrokerName.setText("");
        txtBrokerAddress.setText("");
        txtBrokerCity.setText("");
        txtBrokerPincode.setText("");
    }
    
    //Didplay data on the Screen
    private void DisplayData() {
        txtBrokerCode.setText(objBrokerMaster.getAttribute("BROKER_CODE").getString());
        txtBrokerName.setText(objBrokerMaster.getAttribute("BROKER_NAME").getString());        
        txtBrokerAddress.setText(objBrokerMaster.getAttribute("BROKER_ADDRESS").getString());        
        txtBrokerCity.setText(objBrokerMaster.getAttribute("BROKER_CITY").getString());        
        txtBrokerPincode.setText(objBrokerMaster.getAttribute("BROKER_PINCODE").getString());        
    }
    
    //Sets data to the Class Object
    private void SetData() {
        objBrokerMaster.setAttribute("BROKER_CODE",txtBrokerCode.getText());
        objBrokerMaster.setAttribute("BROKER_NAME",txtBrokerName.getText());        
        objBrokerMaster.setAttribute("BROKER_ADDRESS",txtBrokerAddress.getText());        
        objBrokerMaster.setAttribute("BROKER_CITY",txtBrokerCity.getText());        
        objBrokerMaster.setAttribute("BROKER_PINCODE",txtBrokerPincode.getText());        
    }
    
    
    private void SetMenuForRights() {
        
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,11061)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,11062)) {
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
              
        
    }
    
    private void Add() {
        EditMode=EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        //long NewBrokerID=data.getMaxID("D_FD_BROKER_MASTER","BROKER_CODE",FinanceGlobal.FinURL);
        //txtBrokerCode.setText(Long.toString((NewBrokerID)));
        txtBrokerCode.requestFocus();
    }
    
    private void Edit() {
        
        EITLERPGLOBAL.ChangeCursorToWait(this);
        EditMode=EITLERPGLOBAL.EDIT;
        
        //---New Change ---//
        DisplayData();
        //----------------//
        
        SetFields(true);
        DisableToolbar();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        txtBrokerCode.requestFocus();
    }       
    
    private void Save() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        
        SetData();
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(objBrokerMaster.Insert()) {
                MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+objBrokerMaster.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(objBrokerMaster.Update()) {
                //Nothing to do
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+objBrokerMaster.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void Cancel() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.Finance.frmBrokerFind",true);
        frmBrokerFind ObjReturn= (frmBrokerFind) ObjLoader.getObj();
         
        if(ObjReturn.Cancelled==false) {
            if(!objBrokerMaster.Filter(ObjReturn.strQuery,EITLERPGLOBAL.gCompanyID)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveFirst();
        }
    }
    
    private void MoveFirst() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objBrokerMaster.MoveFirst();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MovePrevious() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objBrokerMaster.MovePrevious();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MoveNext() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objBrokerMaster.MoveNext();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MoveLast() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objBrokerMaster.MoveLast();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    public void FindEx(int pCompanyID,int pDocNo) {
        objBrokerMaster.Filter(" WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DEPOSIT_TYPE_ID='"+pDocNo+"'",EITLERPGLOBAL.gCompanyID);
        objBrokerMaster.MoveFirst();
        DisplayData();
    }
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }

    
}
