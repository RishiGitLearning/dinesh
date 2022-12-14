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

public class frmInterestCalcDate extends javax.swing.JApplet {
    
    private int EditMode=0;
    private clsInterestCalcDate objInterestCalcDate;
    
    //private EITLTableModel DataModel;
    private EITLTableModel DataModel;
    
    /** Creates new form frmTemplate */
    public frmInterestCalcDate() {
        
        setSize(680,350);
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
        
        objInterestCalcDate=new clsInterestCalcDate();
        
        SetMenuForRights();
        
        if(getName().equals("Link")) {
            
        }
        else {
            
            if(objInterestCalcDate.LoadData(EITLERPGLOBAL.gCompanyID)) {
                objInterestCalcDate.MoveFirst();
                DisplayData();
                SetMenuForRights();
                SetFields(false);
                ShowMessage("Ready ........");
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while loading data. Error is "+objInterestCalcDate.LastError);
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        txtAuditRemarks = new javax.swing.JTextField();
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
        Header = new javax.swing.JTabbedPane();
        Panel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        txtAuditRemarks.setEnabled(false);

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
        ToolBar.setBounds(0, 0, 660, 40);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("  FD/LD INTEREST CALCULATION DATE");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 660, 25);

        Panel1.setLayout(null);

        Panel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        Table.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                TableFocusGained(evt);
            }
        });

        jScrollPane1.setViewportView(Table);

        Panel1.add(jScrollPane1);
        jScrollPane1.setBounds(7, 10, 630, 130);

        cmdAdd.setFont(new java.awt.Font("Dialog", 0, 10));
        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setNextFocusableComponent(cmdRemove);
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        cmdAdd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdAddFocusGained(evt);
            }
        });

        Panel1.add(cmdAdd);
        cmdAdd.setBounds(460, 150, 80, 20);

        cmdRemove.setFont(new java.awt.Font("Dialog", 0, 10));
        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setEnabled(false);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        cmdRemove.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdRemoveFocusGained(evt);
            }
        });

        Panel1.add(cmdRemove);
        cmdRemove.setBounds(550, 150, 86, 20);

        Header.addTab("Date Detail", Panel1);

        getContentPane().add(Header);
        Header.setBounds(5, 70, 655, 210);
        Header.getAccessibleContext().setAccessibleName("Scheme Details");
        Header.getAccessibleContext().setAccessibleDescription("Scheme Details");

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(5, 285, 655, 22);

    }//GEN-END:initComponents

    private void TableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TableFocusGained
        // TODO add your handling code here:
        ShowMessage("Please Enter Month in accending order.");
    }//GEN-LAST:event_TableFocusGained
                                                            
    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        try {
            if(Table.getRowCount()>0) {
                int CurrentRow=Table.getSelectedRow();
                
                if(CurrentRow>0) {
                    CurrentRow--;
                }
                
                DataModel.removeRow(Table.getSelectedRow());
                
                if(Table.getRowCount()-1>CurrentRow) {
                    Table.changeSelection(CurrentRow, DataModel.getColFromVariable("INTEREST_MONTH"),false,false);
                    Table.changeSelection(CurrentRow, DataModel.getColFromVariable("INTEREST_DAYS"),false,false);
                }
                UpdateSrNo();
            }
        }
        catch(Exception e) {
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed
    
    private void cmdRemoveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdRemoveFocusGained
        // TODO add your handling code here:
        ShowMessage("Click on this button to remove selected row from the table");
    }//GEN-LAST:event_cmdRemoveFocusGained
    
    private void cmdAddFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdAddFocusGained
        // TODO add your handling code here:
        ShowMessage("Click on this button to add a new row to table");
    }//GEN-LAST:event_cmdAddFocusGained
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        try {
            Object[] rowData=new Object[3];
            rowData[0]=Integer.toString(Table.getRowCount()+1);
            rowData[1]="";
            rowData[2]="";
            
            DataModel.addRow(rowData);
            Table.changeSelection(Table.getRowCount()-1, 1, false,false);
            Table.requestFocus();
        }
        catch(Exception e) {
        }
    }//GEN-LAST:event_cmdAddActionPerformed
                                            
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        objInterestCalcDate.Close();
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
        //Find();
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
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            //Delete();
        }
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
    private javax.swing.JTabbedPane Header;
    private javax.swing.JPanel Panel1;
    private javax.swing.JTable Table;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cmdAdd;
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
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdTop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAuditRemarks;
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
        cmdFilter.setEnabled(false);
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
        cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);
        Table.setEnabled(pStat);
    }
    
    /*private void ClearFields() {        
        FormatGridDetail();
    }*/
    
    //Didplay data on the Screen
    private void DisplayData() {
                
        FormatGridDetail();
        //=========Now Generate Table====================//
        for(int i=1;i<=objInterestCalcDate.colInterestCalcDate.size();i++) {
            clsInterestCalcDate ObjItem=(clsInterestCalcDate)objInterestCalcDate.colInterestCalcDate.get(Integer.toString(i));
            Object[] rowData=new Object[1];
            DataModel.addRow(rowData);
            
            int NewRow=Table.getRowCount()-1;
            
            DataModel.setValueByVariable("SR_NO",Integer.toString(i), NewRow);
            DataModel.setValueByVariable("INTEREST_MONTH",ObjItem.getAttribute("INTEREST_MONTH").getString(),NewRow);
            DataModel.setValueByVariable("INTEREST_DAYS",ObjItem.getAttribute("INTEREST_DAYS").getString(),NewRow);
        }
        //===============================================//
    }
    
    //Sets data to the Class Object
    private void SetData() {
        
        objInterestCalcDate.colInterestCalcDate.clear();
        for(int i=0;i<Table.getRowCount();i++) {
            clsInterestCalcDate objItem=new clsInterestCalcDate();
            objItem.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
            objItem.setAttribute("SR_NO",i+1);
            
            objItem.setAttribute("INTEREST_MONTH",DataModel.getValueByVariable("INTEREST_MONTH",i));
            objItem.setAttribute("INTEREST_DAYS",DataModel.getValueByVariable("INTEREST_DAYS",i));
            
            objInterestCalcDate.colInterestCalcDate.put(Integer.toString(objInterestCalcDate.colInterestCalcDate.size()+1), objItem);
        }
    }
    
    private void SetMenuForRights() {
        
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11051)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11052)) {
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11053)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11054)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }
    
    private void Add() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        EditMode=EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
        //ClearFields();
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
    }
    
    private void Delete() {
        if(objInterestCalcDate.Delete(EITLERPGLOBAL.gNewUserID)) {
            MoveLast();
        }
    }
    
    private boolean Validate() {
        if(Table.getRowCount() < 0 ) {
            JOptionPane.showMessageDialog(null,"Please Add Interest Month and Interest Days.");
            return false;
        }
        return true;
    }
    
    private void Save() {
        
        EITLERPGLOBAL.ChangeCursorToWait(this);
        if(!Validate()) {
            EITLERPGLOBAL.ChangeCursorToDefault(this);
            return;
        }
        SetData();
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(objInterestCalcDate.Insert()) {
                MoveFirst();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+objInterestCalcDate.LastError);
                EITLERPGLOBAL.ChangeCursorToDefault(this);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(objInterestCalcDate.Update()) {
                MoveFirst();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+objInterestCalcDate.LastError);
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
        lblTitle.setText(" DEPOSIT SCHEME MASTER");
        lblTitle.setBackground(Color.GRAY);
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MoveFirst() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objInterestCalcDate.MoveFirst();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MovePrevious() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objInterestCalcDate.MovePrevious();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MoveNext() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objInterestCalcDate.MoveNext();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void MoveLast() {
        EITLERPGLOBAL.ChangeCursorToWait(this);
        objInterestCalcDate.MoveLast();
        DisplayData();
        EITLERPGLOBAL.ChangeCursorToDefault(this);
    }
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }
    
    private void FormatGridDetail() {
        try {
            
            DataModel=new EITLTableModel();
            
            EITLTableCellRenderer cellRender=new EITLTableCellRenderer();
            cellRender.setHorizontalAlignment(JLabel.RIGHT);
            
            Table.removeAll();
            Table.setModel(DataModel);
            
            TableColumnModel ColModel=Table.getColumnModel();
            Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            //Add the columns
            DataModel.addColumn("Sr. No."); //0 Read Only
            DataModel.addColumn("Interest Month"); //1
            DataModel.addColumn("Interest Days");
            
            DataModel.SetVariable(0,"SR_NO"); //0 - Read Only
            DataModel.SetVariable(1,"INTEREST_MONTH"); //1
            DataModel.SetVariable(2,"INTEREST_DAYS");
            
            DataModel.TableReadOnly(false);
            DataModel.SetReadOnly(0);
            
            DataModel.SetNumeric(0,true);
            //DataModel.SetNumeric(1,true);
            //DataModel.SetNumeric(2,true);
            
            Table.getColumnModel().getColumn(1).setCellRenderer(cellRender);
            //Table formatting completed
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void UpdateSrNo() {
        int SrCol=DataModel.getColFromVariable("SR_NO");
        
        for(int i=0;i<Table.getRowCount();i++) {
            Table.setValueAt(Integer.toString(i+1), i, SrCol);
        }
    }
}