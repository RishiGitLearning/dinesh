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
/*<APPLET CODE=frmItemHierarchy.class HEIGHT=530 WIDTH=650*/

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import EITLERP.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.tree.*;
import java.util.regex.Pattern;


public class FrmHierarchyFeltSales extends javax.swing.JApplet {
    
    private int EditMode=0;
    private int SelModuleID=0;
    
    private HashMap colTree=new HashMap();
    private clsHierarchy ObjHierarchy;
    private EITLTableModel DataModel=new EITLTableModel();
    private EITLTableModel DataModelH=new EITLTableModel();
    private EITLTableModel DataModelL=new EITLTableModel();
    private EITLTableModel CopyDataModel= new EITLTableModel();
    private EITLTableModel DataModelTableA= new EITLTableModel();
    private EITLTableModel DataModelTableB= new EITLTableModel();
    private EITLComboModel cmbModelModel;
    private EITLComboModel cmbApprovalModel;
    private EITLComboModel cmbHFieldsModel;
    private EITLComboModel cmbLFieldsModel;
    private EITLComboModel cmbCopyModelModel;
        
    
    /** Creates new form frmTemplate */
    public FrmHierarchyFeltSales() {
        System.gc();
        setSize(620,545);
        initComponents();
        
        
        //Hide the Restore Checkbox
        chkRestore.setVisible(false);
        
        Table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int first=e.getFirstIndex();
                int last=e.getLastIndex();
                int i= Table.getSelectedRow();
                
                DisplayFieldAccess();
                
                txtUserID.setText((String) Table.getValueAt(i,1));
                txtUserName.setText((String) Table.getValueAt(i,2));
                
                if((String) Table.getValueAt(i,3) =="Y") {
                    chkCreator.setSelected(true);
                }
                else {
                    chkCreator.setSelected(false);
                }
                
                if((String)Table.getValueAt(i,4)=="Y") {
                    chkApprover.setSelected(true);
                }
                else {
                    chkApprover.setSelected(false);
                }
                
                if((String)Table.getValueAt(i,5)=="Y") {
                    chkFinalApprover.setSelected(true);
                }
                else {
                    chkFinalApprover.setSelected(false);
                }
                
                if((String)Table.getValueAt(i,6)=="Y") {
                    chkSequence.setSelected(true);
                }
                else {
                    chkSequence.setSelected(false);
                }
                
                if((String)Table.getValueAt(i,7)=="Y") {
                    chkGrantOther.setSelected(true);
                }
                else {
                    chkGrantOther.setSelected(false);
                }
                
                txtUserID1.setText((String)Table.getValueAt(i,8)); //GRANTED User ID
                txtUserName1.setText((String) Table.getValueAt(i,11));
                txtFromDate.setText((String)Table.getValueAt(i,10));
                txtToDate.setText((String)Table.getValueAt(i,11));
                if((String)Table.getValueAt(i,12)=="Y") {
                    chkRestore.setSelected(true);
                }
                else {
                    chkRestore.setSelected(false);
                }
            }
        });
        
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
        
        ObjHierarchy=new clsHierarchy();
        
        if(ObjHierarchy.LoadData()) {
            SetFields(false);
            ObjHierarchy.MoveFirst();
            DisplayData();
            SetMenuForRights();
            ShowMessage("Ready ..........");
        }
        else {
            JOptionPane.showMessageDialog(null,"Error loading Hierarchy data. Error is "+ObjHierarchy.LastError);
        }
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdSaving = new javax.swing.JButton();
        cmdCanceling = new javax.swing.JButton();
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
        JTabPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        cmbModule = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtDesc = new javax.swing.JTextField();
        chkDefault = new javax.swing.JCheckBox();
        cmdEdit1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        chkApprover = new javax.swing.JCheckBox();
        chkFinalApprover = new javax.swing.JCheckBox();
        chkCreator = new javax.swing.JCheckBox();
        chkSequence = new javax.swing.JCheckBox();
        chkGrantOther = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        txtUserID1 = new javax.swing.JTextField();
        cmdFind1 = new javax.swing.JButton();
        txtUserName1 = new javax.swing.JTextField();
        txtToDate = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        chkRestore = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        TableLPanel = new javax.swing.JScrollPane();
        TableL = new javax.swing.JTable();
        cmbLFields = new javax.swing.JComboBox();
        cmbHFields = new javax.swing.JComboBox();
        TableHPanel = new javax.swing.JScrollPane();
        TableH = new javax.swing.JTable();
        cmdHAdd = new javax.swing.JButton();
        cmdHRemove = new javax.swing.JButton();
        cmdLAdd = new javax.swing.JButton();
        cmdLRemove = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtUserID = new javax.swing.JTextField();
        cmdFind = new javax.swing.JButton();
        txtUserName = new javax.swing.JTextField();
        lblHierarchyID = new javax.swing.JLabel();
        cmdCopy = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CopyTable = new javax.swing.JTable();
        cmbCopyModule = new javax.swing.JComboBox();
        txtCopyDesc = new javax.swing.JTextField();
        lblCopyHierarchyID = new javax.swing.JLabel();
        chkCopyDefault = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        cmdFindUser = new javax.swing.JButton();
        txtUserNameA = new javax.swing.JTextField();
        txtUserIDA = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableB = new javax.swing.JTable();
        txtUserIDB = new javax.swing.JTextField();
        txtUserNameB = new javax.swing.JTextField();
        cmdFindUserB = new javax.swing.JButton();
        txtHname1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtHname2 = new javax.swing.JTextField();
        txtBeforeReplace = new javax.swing.JTextField();
        txtAfterReplace = new javax.swing.JTextField();
        cmdReplace = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();

        cmdSaving.setText("Save");
        cmdSaving.setEnabled(false);
        cmdSaving.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSavingActionPerformed(evt);
            }
        });

        cmdCanceling.setText("Cancel");
        cmdCanceling.setEnabled(false);
        cmdCanceling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelingActionPerformed(evt);
            }
        });

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

        JTabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(null);

        cmbModule.setEnabled(false);
        cmbModule.setNextFocusableComponent(txtDesc);
        cmbModule.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbModuleItemStateChanged(evt);
            }
        });
        cmbModule.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbModuleFocusGained(evt);
            }
        });
        jPanel1.add(cmbModule);
        cmbModule.setBounds(130, 12, 262, 26);

        jLabel2.setText("Module Name");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(30, 12, 90, 20);

        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(Table);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(8, 266, 480, 100);

        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.setNextFocusableComponent(cmdEdit1);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jPanel1.add(cmdAdd);
        cmdAdd.setBounds(496, 279, 80, 28);

        cmdRemove.setText("Remove");
        cmdRemove.setEnabled(false);
        cmdRemove.setNextFocusableComponent(cmbModule);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        jPanel1.add(cmdRemove);
        cmdRemove.setBounds(496, 339, 58, 28);

        jLabel5.setText("Hierarchy Name");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(30, 40, 100, 20);

        txtDesc.setEnabled(false);
        txtDesc.setNextFocusableComponent(chkDefault);
        txtDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDescFocusGained(evt);
            }
        });
        jPanel1.add(txtDesc);
        txtDesc.setBounds(130, 40, 260, 26);

        chkDefault.setText("Is Default");
        chkDefault.setEnabled(false);
        chkDefault.setNextFocusableComponent(cmdFind);
        chkDefault.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkDefaultFocusGained(evt);
            }
        });
        jPanel1.add(chkDefault);
        chkDefault.setBounds(498, 10, 80, 22);

        cmdEdit1.setText("Edit");
        cmdEdit1.setNextFocusableComponent(cmdRemove);
        cmdEdit1.setEnabled(false);
        cmdEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEdit1ActionPerformed(evt);
            }
        });
        jPanel1.add(cmdEdit1);
        cmdEdit1.setBounds(496, 309, 80, 28);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(null);

        chkApprover.setText("Can Approve");
        chkApprover.setEnabled(false);
        chkApprover.setNextFocusableComponent(chkFinalApprover);
        chkApprover.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkApproverFocusGained(evt);
            }
        });
        jPanel5.add(chkApprover);
        chkApprover.setBounds(7, 7, 106, 22);

        chkFinalApprover.setText("Can Final Approve");
        chkFinalApprover.setEnabled(false);
        chkFinalApprover.setNextFocusableComponent(chkCreator);
        chkFinalApprover.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkFinalApproverFocusGained(evt);
            }
        });
        jPanel5.add(chkFinalApprover);
        chkFinalApprover.setBounds(111, 7, 140, 22);

        chkCreator.setText("Can Create");
        chkCreator.setEnabled(false);
        chkCreator.setNextFocusableComponent(chkSequence);
        chkCreator.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkCreatorFocusGained(evt);
            }
        });
        jPanel5.add(chkCreator);
        chkCreator.setBounds(249, 7, 104, 22);

        chkSequence.setText("Can Skip Sequence");
        chkSequence.setNextFocusableComponent(chkGrantOther);
        chkSequence.setEnabled(false);
        chkSequence.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkSequenceFocusGained(evt);
            }
        });
        jPanel5.add(chkSequence);
        chkSequence.setBounds(353, 7, 144, 22);

        chkGrantOther.setText("Assign Approval Authority to other user");
        chkGrantOther.setEnabled(false);
        chkGrantOther.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGrantOtherActionPerformed(evt);
            }
        });
        chkGrantOther.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkGrantOtherFocusGained(evt);
            }
        });
        chkGrantOther.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkGrantOtherItemStateChanged(evt);
            }
        });
        jPanel5.add(chkGrantOther);
        chkGrantOther.setBounds(7, 43, 298, 22);

        jLabel11.setText("User ");
        jPanel5.add(jLabel11);
        jLabel11.setBounds(35, 71, 40, 20);

        txtUserID1.setEnabled(false);
        jPanel5.add(txtUserID1);
        txtUserID1.setBounds(77, 71, 80, 26);

        cmdFind1.setText("Select");
        cmdFind1.setNextFocusableComponent(txtFromDate);
        cmdFind1.setEnabled(false);
        cmdFind1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFind1ActionPerformed(evt);
            }
        });
        jPanel5.add(cmdFind1);
        cmdFind1.setBounds(161, 71, 70, 22);

        txtUserName1.setBorder(null);
        txtUserName1.setEnabled(false);
        jPanel5.add(txtUserName1);
        txtUserName1.setBounds(243, 71, 280, 20);

        txtToDate.setNextFocusableComponent(cmdAdd);
        txtToDate.setEnabled(false);
        txtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtToDateFocusGained(evt);
            }
        });
        jPanel5.add(txtToDate);
        txtToDate.setBounds(243, 96, 80, 26);

        jLabel14.setText("To Date");
        jPanel5.add(jLabel14);
        jLabel14.setBounds(188, 96, 50, 20);

        txtFromDate.setNextFocusableComponent(txtToDate);
        txtFromDate.setEnabled(false);
        txtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtFromDateFocusGained(evt);
            }
        });
        jPanel5.add(txtFromDate);
        txtFromDate.setBounds(78, 96, 80, 26);

        jLabel13.setText("From Date");
        jPanel5.add(jLabel13);
        jLabel13.setBounds(8, 96, 70, 20);

        chkRestore.setText("Restore");
        chkRestore.setEnabled(false);
        jPanel5.add(chkRestore);
        chkRestore.setBounds(447, 41, 80, 22);

        jTabbedPane1.addTab("User Information", jPanel5);

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(null);

        TableL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TableLPanel.setViewportView(TableL);

        jPanel6.add(TableLPanel);
        TableLPanel.setBounds(296, 66, 253, 79);

        cmbLFields.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmbLFields.setEnabled(false);
        jPanel6.add(cmbLFields);
        cmbLFields.setBounds(296, 18, 252, 20);

        cmbHFields.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmbHFields.setEnabled(false);
        jPanel6.add(cmbHFields);
        cmbHFields.setBounds(9, 18, 243, 20);

        TableH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TableHPanel.setViewportView(TableH);

        jPanel6.add(TableHPanel);
        TableHPanel.setBounds(9, 66, 247, 79);

        cmdHAdd.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmdHAdd.setText("Add");
        cmdHAdd.setEnabled(false);
        cmdHAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHAddActionPerformed(evt);
            }
        });
        jPanel6.add(cmdHAdd);
        cmdHAdd.setBounds(124, 42, 31, 20);

        cmdHRemove.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmdHRemove.setText("Remove");
        cmdHRemove.setEnabled(false);
        cmdHRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHRemoveActionPerformed(evt);
            }
        });
        jPanel6.add(cmdHRemove);
        cmdHRemove.setBounds(180, 42, 50, 20);

        cmdLAdd.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmdLAdd.setText("Add");
        cmdLAdd.setEnabled(false);
        cmdLAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLAddActionPerformed(evt);
            }
        });
        jPanel6.add(cmdLAdd);
        cmdLAdd.setBounds(417, 42, 31, 20);

        cmdLRemove.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cmdLRemove.setText("Remove");
        cmdLRemove.setEnabled(false);
        cmdLRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLRemoveActionPerformed(evt);
            }
        });
        jPanel6.add(cmdLRemove);
        cmdLRemove.setBounds(473, 42, 50, 20);

        jLabel3.setText("Header Fields");
        jPanel6.add(jLabel3);
        jLabel3.setBounds(8, 1, 96, 16);

        jLabel7.setText("Line Fields");
        jPanel6.add(jLabel7);
        jLabel7.setBounds(297, 1, 96, 16);

        jTabbedPane1.addTab("Field Access Permissions", jPanel6);
        jTabbedPane1.addTab("AccessRights", jPanel2);

        jPanel1.add(jTabbedPane1);
        jTabbedPane1.setBounds(7, 94, 570, 170);

        jLabel4.setText("User");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(97, 68, 36, 20);

        txtUserID.setEnabled(false);
        jPanel1.add(txtUserID);
        txtUserID.setBounds(131, 67, 80, 21);

        cmdFind.setText("Select");
        cmdFind.setEnabled(false);
        cmdFind.setNextFocusableComponent(chkApprover);
        cmdFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindActionPerformed(evt);
            }
        });
        cmdFind.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdFindFocusGained(evt);
            }
        });
        jPanel1.add(cmdFind);
        cmdFind.setBounds(215, 67, 72, 22);

        txtUserName.setBackground(new java.awt.Color(255, 255, 204));
        txtUserName.setBorder(null);
        txtUserName.setEnabled(false);
        jPanel1.add(txtUserName);
        txtUserName.setBounds(297, 67, 280, 20);

        lblHierarchyID.setText("_");
        jPanel1.add(lblHierarchyID);
        lblHierarchyID.setBounds(400, 14, 82, 16);

        cmdCopy.setText("Copy");
        cmdCopy.setMargin(new java.awt.Insets(2, 2, 2, 2));
        cmdCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCopyActionPerformed(evt);
            }
        });
        jPanel1.add(cmdCopy);
        cmdCopy.setBounds(462, 42, 50, 20);

        JTabPane.addTab("Hierarchy Detail", jPanel1);

        jPanel3.setLayout(null);

        CopyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        CopyTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CopyTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(CopyTable);

        jPanel3.add(jScrollPane2);
        jScrollPane2.setBounds(20, 90, 480, 100);

        cmbCopyModule.setEnabled(false);
        cmbCopyModule.setNextFocusableComponent(txtDesc);
        cmbCopyModule.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCopyModuleItemStateChanged(evt);
            }
        });
        cmbCopyModule.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbCopyModuleFocusGained(evt);
            }
        });
        jPanel3.add(cmbCopyModule);
        cmbCopyModule.setBounds(130, 12, 262, 26);

        txtCopyDesc.setEnabled(false);
        txtCopyDesc.setNextFocusableComponent(chkDefault);
        txtCopyDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCopyDescFocusGained(evt);
            }
        });
        jPanel3.add(txtCopyDesc);
        txtCopyDesc.setBounds(130, 40, 260, 26);

        lblCopyHierarchyID.setText("_");
        jPanel3.add(lblCopyHierarchyID);
        lblCopyHierarchyID.setBounds(400, 14, 70, 16);

        chkCopyDefault.setText("Is Default");
        chkCopyDefault.setEnabled(false);
        chkCopyDefault.setNextFocusableComponent(cmdFind);
        chkCopyDefault.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                chkCopyDefaultFocusGained(evt);
            }
        });
        jPanel3.add(chkCopyDefault);
        chkCopyDefault.setBounds(490, 10, 80, 22);

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);
        jButton1.setBounds(210, 210, 70, 28);

        JTabPane.addTab("Single Copy", jPanel3);

        jPanel4.setLayout(null);

        cmdFindUser.setText("Select");
        cmdFindUser.setNextFocusableComponent(chkApprover);
        cmdFindUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindUserActionPerformed(evt);
            }
        });
        cmdFindUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdFindUserFocusGained(evt);
            }
        });
        jPanel4.add(cmdFindUser);
        cmdFindUser.setBounds(50, 10, 72, 22);

        txtUserNameA.setBackground(new java.awt.Color(255, 255, 204));
        txtUserNameA.setBorder(null);
        jPanel4.add(txtUserNameA);
        txtUserNameA.setBounds(220, 10, 140, 20);

        txtUserIDA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserIDAKeyPressed(evt);
            }
        });
        jPanel4.add(txtUserIDA);
        txtUserIDA.setBounds(130, 10, 80, 21);

        TableA.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(TableA);

        jPanel4.add(jScrollPane3);
        jScrollPane3.setBounds(10, 40, 560, 110);

        TableB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(TableB);

        jPanel4.add(jScrollPane4);
        jScrollPane4.setBounds(0, 230, 570, 120);

        txtUserIDB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUserIDBKeyPressed(evt);
            }
        });
        jPanel4.add(txtUserIDB);
        txtUserIDB.setBounds(130, 170, 80, 21);

        txtUserNameB.setBackground(new java.awt.Color(255, 255, 204));
        txtUserNameB.setBorder(null);
        jPanel4.add(txtUserNameB);
        txtUserNameB.setBounds(220, 170, 180, 20);

        cmdFindUserB.setText("Select");
        cmdFindUserB.setNextFocusableComponent(chkApprover);
        cmdFindUserB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdFindUserBActionPerformed(evt);
            }
        });
        cmdFindUserB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmdFindUserBFocusGained(evt);
            }
        });
        jPanel4.add(cmdFindUserB);
        cmdFindUserB.setBounds(50, 170, 72, 22);
        jPanel4.add(txtHname1);
        txtHname1.setBounds(460, 170, 40, 26);

        jLabel6.setText("-");
        jPanel4.add(jLabel6);
        jLabel6.setBounds(500, 170, 10, 20);
        jPanel4.add(txtHname2);
        txtHname2.setBounds(510, 170, 40, 26);

        txtBeforeReplace.setEnabled(false);
        jPanel4.add(txtBeforeReplace);
        txtBeforeReplace.setBounds(400, 10, 120, 26);

        txtAfterReplace.setEnabled(false);
        jPanel4.add(txtAfterReplace);
        txtAfterReplace.setBounds(460, 200, 110, 26);

        cmdReplace.setText("jButton2");
        cmdReplace.setActionCommand("Replace");
        cmdReplace.setEnabled(false);
        cmdReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReplaceActionPerformed(evt);
            }
        });
        jPanel4.add(cmdReplace);
        cmdReplace.setBounds(410, 170, 30, 30);

        JTabPane.addTab("Copy Hierarchy", jPanel4);

        getContentPane().add(JTabPane);
        JTabPane.setBounds(2, 66, 590, 420);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setText(" APPROVAL HIERARCHY SETUP FELTSALES");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 40, 650, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(10, 500, 588, 22);
    }// </editor-fold>//GEN-END:initComponents
        
    private void cmdLRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLRemoveActionPerformed
        // TODO add your handling code here:
        if(TableL.getSelectedRow()>=0) {
            DataModelL.removeRow(TableL.getSelectedRow());
        }
    }//GEN-LAST:event_cmdLRemoveActionPerformed
    
    private void cmdHRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHRemoveActionPerformed
        // TODO add your handling code here:
        if(TableH.getSelectedRow()>=0) {
            DataModelH.removeRow(TableH.getSelectedRow());
        }
    }//GEN-LAST:event_cmdHRemoveActionPerformed
    
    private void cmdLAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLAddActionPerformed
        // TODO add your handling code here:
        String theField=EITLERPGLOBAL.getCombostrCode(cmbLFields);
        
        if(cmbLFields.getSelectedIndex()>=0) {
            for(int i=0;i<TableL.getRowCount();i++) {
                if(((String)TableL.getValueAt(i,0)).trim().equals(theField)) {
                    JOptionPane.showMessageDialog(null,"Field already exist");
                    return;
                }
            }
        }
        
        if(cmbLFields.getSelectedIndex()>=0) {
            Object[] rowData=new Object[1];
            rowData[0]=EITLERPGLOBAL.getCombostrCode(cmbLFields);
            DataModelL.addRow(rowData);
        }
        
    }//GEN-LAST:event_cmdLAddActionPerformed
    
    private void cmdHAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHAddActionPerformed
        // TODO add your handling code here:
        String theField=EITLERPGLOBAL.getCombostrCode(cmbHFields);
        
        if(cmbHFields.getSelectedIndex()>=0) {
            for(int i=0;i<TableH.getRowCount();i++) {
                if(((String)TableH.getValueAt(i,0)).trim().equals(theField)) {
                    JOptionPane.showMessageDialog(null,"Field already exist");
                    return;
                }
            }
            
            Object[] rowData=new Object[1];
            rowData[0]=EITLERPGLOBAL.getCombostrCode(cmbHFields);
            DataModelH.addRow(rowData);
        }
    }//GEN-LAST:event_cmdHAddActionPerformed
    
    private void cmbModuleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbModuleItemStateChanged
        // TODO add your handling code here:
        GenerateFieldCombo();
    }//GEN-LAST:event_cmbModuleItemStateChanged
    
    private void txtToDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter To Date upto which date approval rights granted ..........");
    }//GEN-LAST:event_txtToDateFocusGained
    
    private void txtFromDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromDateFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter From which date approval rights should be assigned ..........");
    }//GEN-LAST:event_txtFromDateFocusGained
    
    private void chkGrantOtherFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkGrantOtherFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Check box for Grantting Approval rights to other user ..........");
    }//GEN-LAST:event_chkGrantOtherFocusGained
    
    private void chkSequenceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkSequenceFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Check box wether user can Skip Sequence ?..........");
    }//GEN-LAST:event_chkSequenceFocusGained
    
    private void chkCreatorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkCreatorFocusGained
        // TODO add your handling code here:
        ShowMessage("Select check box wether user is only Creator ..........");
    }//GEN-LAST:event_chkCreatorFocusGained
    
    private void chkFinalApproverFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkFinalApproverFocusGained
        // TODO add your handling code here:
        ShowMessage("Select wether user is Final Approver..........");
    }//GEN-LAST:event_chkFinalApproverFocusGained
    
    private void chkApproverFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkApproverFocusGained
        // TODO add your handling code here:
        ShowMessage("Select wether user is only Approver ..........");
    }//GEN-LAST:event_chkApproverFocusGained
    
    private void cmdFindFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdFindFocusGained
        // TODO add your handling code here:
        ShowMessage("Select user while precssing Enter key from given list ..........");
    }//GEN-LAST:event_cmdFindFocusGained
    
    private void chkDefaultFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkDefaultFocusGained
        // TODO add your handling code here:
        ShowMessage("Select check mark if it is default hierarchy for specify module..........");
    }//GEN-LAST:event_chkDefaultFocusGained
    
    private void txtDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescFocusGained
        // TODO add your handling code here:
        ShowMessage("Enter Hierarchy Name for identification of users accesation ..........");
    }//GEN-LAST:event_txtDescFocusGained
    
    private void cmbModuleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbModuleFocusGained
        // TODO add your handling code here:
        ShowMessage("Select Module Name from given module list..........");
        //GenerateFieldCombo();
    }//GEN-LAST:event_cmbModuleFocusGained
    
    private void cmdCancelingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelingActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null,"Recently Feature not Available");
        return;
    }//GEN-LAST:event_cmdCancelingActionPerformed
    
    private void cmdSavingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSavingActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null,"Recently Feature not Available");
        return;
    }//GEN-LAST:event_cmdSavingActionPerformed
            
    private void cmdEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEdit1ActionPerformed
        // TODO add your handling code here:
        int row_selected = Table.getSelectedRow();
        for(int i=0;i<Table.getRowCount();i++) {
            if(row_selected != i) {
                if(chkCreator.isSelected()) {
                    if(Table.getValueAt(i,3).equals("Y")) {
                        JOptionPane.showMessageDialog(null,"Module can have only one creator");
                        return;
                    }
                }
                
                /*if(chkFinalApprover.isSelected()) {
                    if(Table.getValueAt(i,5).equals("Y")) {
                        JOptionPane.showMessageDialog(null,"Module can have only one Final Approver");
                        return;
                    }
                }*/
                
            }
        }
        
        if(chkGrantOther.isSelected()) {
            if(txtFromDate.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Granting rights to other person From Date");
                return;
            }
            if(txtToDate.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"Granting rights to other person Upto Date");
                return;
            }
        }
        
        //Building Field Access Collection
        HashMap colFA=new HashMap();
        
        for(int i=0;i<TableH.getRowCount();i++) {
            clsFieldAccess ObjFA=new clsFieldAccess();
            ObjFA.setAttribute("FIELD_TYPE","H");
            ObjFA.setAttribute("FIELD_NAME",(String)TableH.getValueAt(i,0));
            colFA.put(Integer.toString(colFA.size()+1),ObjFA);
        }
        
        for(int i=0;i<TableL.getRowCount();i++) {
            clsFieldAccess ObjFA=new clsFieldAccess();
            ObjFA.setAttribute("FIELD_TYPE","L");
            ObjFA.setAttribute("FIELD_NAME",(String)TableL.getValueAt(i,0));
            colFA.put(Integer.toString(colFA.size()+1),ObjFA);
        }
        
        
        DataModel.SetUserObject(row_selected, colFA);
        Table.setValueAt(txtUserID.getText(), row_selected,1);
        Table.setValueAt(txtUserName.getText(),row_selected,2);
        
        if(chkCreator.isSelected()) {
            Table.setValueAt("Y",row_selected,3);
        }
        else {
            Table.setValueAt(" ",row_selected,3);
        }
        
        if(chkApprover.isSelected()) {
            Table.setValueAt("Y",row_selected,4);
        }
        else {
            Table.setValueAt(" ",row_selected,4);
        }
        
        if(chkFinalApprover.isSelected()) {
            Table.setValueAt("Y",row_selected,5);
        }
        else {
            Table.setValueAt(" ",row_selected,5);
        }
        
        if(chkSequence.isSelected()) {
            Table.setValueAt("Y",row_selected,6);
        }
        else {
            Table.setValueAt(" ",row_selected,6);
        }
        
        if(chkGrantOther.isSelected()) {
            Table.setValueAt("Y",row_selected,7);
        }
        else {
            Table.setValueAt(" ",row_selected,7);
        }
        
        Table.setValueAt(txtUserID1.getText(),row_selected,8);
        Table.setValueAt(txtUserName1.getText(),row_selected,9);
        Table.setValueAt(txtFromDate.getText(),row_selected,10);
        Table.setValueAt(txtToDate.getText(),row_selected,11);
        
        if(chkRestore.isSelected()) {
            Table.setValueAt("Y",row_selected,12);
        }
        else {
            Table.setValueAt(" ",row_selected,12);
        }
        
        FormatFieldGrid();
        
    }//GEN-LAST:event_cmdEdit1ActionPerformed
    
    private void chkGrantOtherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGrantOtherActionPerformed
        // TODO add your handling code here:
        if (chkGrantOther.isSelected()) {
            txtUserID1.setEnabled(true);
            chkRestore.setEnabled(true);
            txtFromDate.setEnabled(true);
            txtToDate.setEnabled(true);
            cmdFind1.setEnabled(true);
        }
        else {
            txtUserID1.setEnabled(true);
            chkRestore.setEnabled(true);
            txtFromDate.setEnabled(false);
            txtToDate.setEnabled(false);
            cmdFind1.setEnabled(false);
        }
    }//GEN-LAST:event_chkGrantOtherActionPerformed
    
    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        int n = Table.getSelectedRow();
        DataModel.removeRow(n);
        
        //Re Arranging the Sr. Nos.
        for(int i=0;i<Table.getRowCount();i++) {
            Table.setValueAt(Integer.toString(i+1),i,1);
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed
                    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
                        // TODO add your handling code here:
                        long nUserID = 0;
                        long nUserID1 = 0;
                        
                        if(!txtUserID.getText().trim().equals("")) {
                            nUserID=Long.parseLong(txtUserID.getText());
                        }
                        else {
                            if(Table.getRowCount()<=0) {
                                JOptionPane.showMessageDialog(null,"Initiator of the document cannot be blank. Please specify the user");
                                return;
                            }
                        }
                        
                        //Check that User id is exist or not
                        for(int i=0;i<Table.getRowCount();i++) {
                            if(Integer.parseInt((String)Table.getValueAt(i,0))==nUserID) {
                                JOptionPane.showMessageDialog(null,"User already added to the list");
                                return;
                            }
                            
                            if(chkCreator.isSelected()) {
                                if(Table.getValueAt(i,3).equals("Y")) {
                                    JOptionPane.showMessageDialog(null,"Module can have only one creator");
                                    return;
                                }
                            }
                            
          /*if(chkFinalApprover.isSelected())
          {
              if(Table.getValueAt(i,5).equals("Y"))
              {
                  JOptionPane.showMessageDialog(null,"Module can have only one Final Approver");
                  return;
              }
          }*/
                            
                        }
                        
                        //Building Field Access Collection
                        HashMap colFA=new HashMap();
                        
                        for(int i=0;i<TableH.getRowCount();i++) {
                            clsFieldAccess ObjFA=new clsFieldAccess();
                            ObjFA.setAttribute("FIELD_TYPE","H");
                            ObjFA.setAttribute("FIELD_NAME",(String)TableH.getValueAt(i,0));
                            colFA.put(Integer.toString(colFA.size()+1),ObjFA);
                        }
                        
                        for(int i=0;i<TableL.getRowCount();i++) {
                            clsFieldAccess ObjFA=new clsFieldAccess();
                            ObjFA.setAttribute("FIELD_TYPE","L");
                            ObjFA.setAttribute("FIELD_NAME",(String)TableL.getValueAt(i,0));
                            colFA.put(Integer.toString(colFA.size()+1),ObjFA);
                        }
                        
                        
                        Object[] rowData=new Object[13];
                        rowData[0]=Long.toString(nUserID);
                        rowData[1]=Long.toString(Table.getRowCount()+1);
                        rowData[2]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,nUserID);
                        
                        if(chkCreator.isSelected()) {
                            rowData[3]="Y";
                        }
                        else {
                            rowData[3]=" ";
                        }
                        
                        if(chkApprover.isSelected()) {
                            rowData[4]="Y";
                        }
                        else {
                            rowData[4]=" ";
                        }
                        
                        if(chkFinalApprover.isSelected()) {
                            rowData[5]="Y";
                        }
                        else {
                            rowData[5]=" ";
                        }
                        
                        if(chkSequence.isSelected()) {
                            rowData[6]="Y";
                        }
                        else {
                            rowData[6]=" ";
                        }
                        
                        if(chkGrantOther.isSelected()) {
                            rowData[7]="Y";
                            nUserID1= Long.parseLong(txtUserID1.getText());
                        }
                        else {
                            rowData[7]=" ";
                        }
                        
                        rowData[8]=Long.toString(nUserID1);
                        rowData[9]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,nUserID1);
                        rowData[10]=txtFromDate.getText();
                        rowData[11]=txtToDate.getText();
                        
                        if(chkRestore.isSelected()) {
                            rowData[12]="Y";
                        }
                        else {
                            rowData[12]=" ";
                        }
                        
                        DataModel.addRow(rowData);
                        DataModel.SetUserObject(Table.getRowCount()-1,colFA);
                        
                        txtUserID.setText("");
                        txtUserName.setText("");
                        txtUserID1.setText("");
                        txtUserName1.setText("");
                        chkApprover.setSelected(false);
                        chkCreator.setSelected(false);
                        chkFinalApprover.setSelected(false);
                        chkSequence.setSelected(false);
                        chkGrantOther.setSelected(false);
                        chkRestore.setSelected(false);
                        txtFromDate.setText("");
                        txtToDate.setText("");
                        
                        FormatFieldGrid();
    }//GEN-LAST:event_cmdAddActionPerformed
                                                            private void cmdFind1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFind1ActionPerformed
                                                                // TODO add your handling code here:
                                                                LOV aList=new LOV();
                                                                
                                                                aList.SQL="SELECT USER_ID,USER_NAME FROM D_COM_USER_MASTER ORDER BY USER_ID";
                                                                aList.ReturnCol=1;
                                                                aList.ShowReturnCol=true;
                                                                aList.DefaultSearchOn=2;
                                                                
                                                                if(aList.ShowLOV()) {
                                                                    String UserID = (String) aList.ReturnVal;
                                                                    long User = (long) Long.parseLong(UserID);
                                                                    txtUserID1.setText(UserID);
                                                                    txtUserName1.setText((String) clsUser.getUserName(EITLERPGLOBAL.gCompanyID,User));
                                                                }
    }//GEN-LAST:event_cmdFind1ActionPerformed
                                                                                                                                            private void cmdFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindActionPerformed
                                                                                                                                                // TODO add your handling code here:
                                                                                                                                                LOV aList=new LOV();
                                                                                                                                                
                                                                                                                                                aList.SQL="SELECT USER_ID,USER_NAME FROM D_COM_USER_MASTER ORDER BY USER_NAME";
                                                                                                                                                aList.ReturnCol=1;
                                                                                                                                                aList.ShowReturnCol=true;
                                                                                                                                                aList.DefaultSearchOn=2;
                                                                                                                                                aList.UseCreatedConn=true;
                                                                                                                                                
                                                                                                                                                if(aList.ShowLOV()) {
                                                                                                                                                    String UserID = (String) aList.ReturnVal;
                                                                                                                                                    long User = (long) Long.parseLong(UserID);
                                                                                                                                                    txtUserID.setText(UserID);
                                                                                                                                                    txtUserName.setText((String) clsUser.getUserName(EITLERPGLOBAL.gCompanyID,User));
                                                                                                                                                }
    }//GEN-LAST:event_cmdFindActionPerformed
                                                                                                                                            
    private void chkGrantOtherItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkGrantOtherItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chkGrantOtherItemStateChanged
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPrintActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        PreviewReport();
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

    private void cmdCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCopyActionPerformed
        CopyGenerateCombos();        
        CopyDisplayData();
        txtCopyDesc.setEnabled(true);
        JTabPane.setSelectedIndex(1);
    }//GEN-LAST:event_cmdCopyActionPerformed

    private void cmbCopyModuleItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCopyModuleItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCopyModuleItemStateChanged

    private void cmbCopyModuleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbCopyModuleFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCopyModuleFocusGained

    private void txtCopyDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCopyDescFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCopyDescFocusGained

    private void chkCopyDefaultFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkCopyDefaultFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCopyDefaultFocusGained

    private void CopyTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CopyTableKeyPressed
    //if (EditMode != 0) {
        //=========== User List ===============
            if (CopyTable.getSelectedColumn() == CopyDataModel.getColFromVariable("USER_ID")) {
                if (evt.getKeyCode() == 112) //F1 Key pressed
                {
                    LOV aList = new LOV();
                    
                    //aList.SQL = "SELECT DEPT_ID,DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID=" + EITLERPGLOBAL.gCompanyID + " ORDER BY DEPT_DESC";
                    aList.SQL="SELECT USER_ID,USER_NAME FROM D_COM_USER_MASTER WHERE DEPT_ID=29 ORDER BY USER_NAME";
                    aList.ReturnCol = 1;
                    aList.ShowReturnCol = true;
                    aList.DefaultSearchOn = 2;
                    
                    
                    if (aList.ShowLOV()) {
                        if (CopyTable.getCellEditor() != null) {
                            CopyTable.getCellEditor().stopCellEditing();
                        }
                        CopyTable.setValueAt(aList.ReturnVal, CopyTable.getSelectedRow(), CopyDataModel.getColFromVariable("USER_ID"));
                        CopyTable.setValueAt(clsUser.getUserName(EITLERPGLOBAL.gCompanyID,Integer.parseInt(aList.ReturnVal)), CopyTable.getSelectedRow(), CopyDataModel.getColFromVariable("USER_ID")+2);
                        
                    }
                }
            }
            //=========================================
    //}      
    }//GEN-LAST:event_CopyTableKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CopySetData();
        if(ObjHierarchy.Insert()) {
                //MoveLast();
                CopyDisplayData();
            }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdFindUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindUserActionPerformed
    try{
        GenerateGridA();
    }catch(Exception e)        {
        e.printStackTrace();
    }
    }//GEN-LAST:event_cmdFindUserActionPerformed

    private void cmdFindUserFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdFindUserFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdFindUserFocusGained

    private void txtUserIDAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserIDAKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList = new LOV();

        aList.SQL = "SELECT USER_ID,USER_NAME FROM D_COM_USER_MASTER WHERE DEPT_ID=29 ORDER BY USER_ID";
        aList.ReturnCol = 1;
        aList.ShowReturnCol = true;
        aList.DefaultSearchOn = 2;

        if (aList.ShowLOV()) {
            String UserID = (String) aList.ReturnVal;
            long User = (long) Long.parseLong(UserID);
            txtUserIDA.setText(UserID);
            txtUserNameA.setText((String) clsUser.getUserName(EITLERPGLOBAL.gCompanyID, User));
        }
        }
    }//GEN-LAST:event_txtUserIDAKeyPressed

    private void txtUserIDBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserIDBKeyPressed
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList = new LOV();

        aList.SQL = "SELECT USER_ID,USER_NAME FROM D_COM_USER_MASTER WHERE DEPT_ID=29 ORDER BY USER_ID";
        aList.ReturnCol = 1;
        aList.ShowReturnCol = true;
        aList.DefaultSearchOn = 2;

        if (aList.ShowLOV()) {
            String UserID = (String) aList.ReturnVal;
            long User = (long) Long.parseLong(UserID);
            txtUserIDB.setText(UserID);
            txtUserNameB.setText((String) clsUser.getUserName(EITLERPGLOBAL.gCompanyID, User));
        }
        }
    }//GEN-LAST:event_txtUserIDBKeyPressed

    private void cmdFindUserBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFindUserBActionPerformed
       HashMap List=new HashMap();
        
        TreeSet set = new TreeSet();
        for (int i = 0; i < TableA.getRowCount(); i++) {
            Object obj = DataModelTableA.getValueAt(i, 0);
    if(set.add(obj)){
            List.put(Long.toString(List.size()+1), obj);
            }
            ///set.add(obj);
            //System.out.println(set);
        }  
//        for(int j=1;j<=List.size();j++){
//            System.out.println(List.values());
//        }
        
        for(Object key:List.values()){
            //System.out.println("Value "+key);
            
            ObjHierarchy.colRights.clear();
            
            for (int r = 0; r < TableA.getRowCount(); r++) {
                if (TableA.getValueAt(r, 0).equals(key)) {
                    clsHierarchyUsers ObjUser=new clsHierarchyUsers();
        ObjHierarchy.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjHierarchy.setAttribute("MODULE_ID",Integer.parseInt((String) DataModelTableA.getValueAt(r, 2)));
        
        //ObjHierarchy.setAttribute("HIERARCHY_NAME",(String) DataModelTableA.getValueAt(r, 1));
        ObjHierarchy.setAttribute("HIERARCHY_NAME",ReplaceHierarchyName((String) DataModelTableA.getValueAt(r, 1)));
        if(Integer.parseInt((String)TableA.getValueAt(r,3))==0) {
            
            ObjHierarchy.setAttribute("IS_DEFAULT",false);
        }
        else
            ObjHierarchy.setAttribute("IS_DEFAULT",true);
        
        
            ObjHierarchy.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
            ObjHierarchy.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        
            
        
        //Now Set the detail records
        
        //int Rows= Table.getRowCount();
        
        //for(int i=0;i<Rows;i++) {
            
            
//            if(DataModel.getUserObject(i) instanceof HashMap) {
//                ObjUser.colFieldAccess=(HashMap)DataModel.getUserObject(i);
//            }
//            else {
//                ObjUser.colFieldAccess=new HashMap();
//                
//            }
            
            ObjUser.setAttribute("SR_NO",Integer.parseInt((String) TableA.getValueAt(r,5))); //Sr. No.
            //ObjUser.setAttribute("USER_ID",Integer.parseInt((String)TableA.getValueAt(r,4))); //User ID
            if(Integer.parseInt((String)TableA.getValueAt(r,4))==Integer.parseInt((String) txtUserIDA.getText())){
            ObjUser.setAttribute("USER_ID",Integer.parseInt((String) txtUserIDB.getText())); //User ID
            }else
                ObjUser.setAttribute("USER_ID",Integer.parseInt((String)TableA.getValueAt(r,4))); //User ID
            if((String) TableA.getValueAt(r,7) =="Y") {
                ObjUser.setAttribute("CREATOR",true);
            }
            else {
                ObjUser.setAttribute("CREATOR",false);
            }
            
            if((String)TableA.getValueAt(r,8)=="Y") {
                ObjUser.setAttribute("APPROVER",true);
            }
            else {
                ObjUser.setAttribute("APPROVER",false);
            }
            
            if((String)TableA.getValueAt(r,9)=="Y") {
                ObjUser.setAttribute("FINAL_APPROVER",true);
            }
            else {
                ObjUser.setAttribute("FINAL_APPROVER",false);
            }
            
            if((String)TableA.getValueAt(r,10)=="Y") {
                ObjUser.setAttribute("SKIP_SEQUENCE",true);
            }
            else {
                ObjUser.setAttribute("SKIP_SEQUENCE",false);
            }
            
            if((String)TableA.getValueAt(r,11)=="Y") {
                ObjUser.setAttribute("GRANT_OTHER",true);
            }
            else {
                ObjUser.setAttribute("GRANT_OTHER",false);
            }
            
            ObjUser.setAttribute("GRANT_USER_ID",Integer.parseInt((String)TableA.getValueAt(r,12))); //GRANTED User ID
            ObjUser.setAttribute("FROM_DATE",EITLERPGLOBAL.formatDateDB((String)TableA.getValueAt(r,14)));
            ObjUser.setAttribute("TO_DATE",EITLERPGLOBAL.formatDateDB((String)TableA.getValueAt(r,15)));
            if((String)TableA.getValueAt(r,16)=="Y") {
                ObjUser.setAttribute("RESTORE",true);
            }
            else {
                ObjUser.setAttribute("RESTORE",false);
            }
            //Add to collection
            ObjHierarchy.colRights.put(Integer.toString(ObjHierarchy.colRights.size()+1),ObjUser);
                       
                }
            }
            
            ObjHierarchy.Insert();
        }
        
        try{
        GenerateGridB();
    }catch(Exception e)        {
        e.printStackTrace();
    }
    }//GEN-LAST:event_cmdFindUserBActionPerformed

    private void cmdFindUserBFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdFindUserBFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdFindUserBFocusGained

    private void cmdReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReplaceActionPerformed
      String BeforeReplace=txtBeforeReplace.getText();
      String AfterReplace="";
      String Replace1=txtHname1.getText();
      String Replace2=txtHname2.getText();
      //AfterReplace=ReplaceBetween.replaceBetween(BeforeReplace,"","-",Replace1);
      
String[] parts = BeforeReplace.split("\\-"); // String array, each element is text between dots

String beforeFirstHyphen = parts[0];
String betweenFirstAndSecondHyphen=parts[1];
//String beforeFirstHyphen = BefpreReplace.split("\\.")[0];
        System.out.println(beforeFirstHyphen);
        System.out.println(betweenFirstAndSecondHyphen);
      //txtAfterReplace.setText(AfterReplace);      
        AfterReplace=BeforeReplace.replaceAll(beforeFirstHyphen,Replace1);
        txtAfterReplace.setText(AfterReplace);
        if(Replace1.equals("")){
            if(!Replace2.equals("")){
                AfterReplace=BeforeReplace.replaceAll(betweenFirstAndSecondHyphen,Replace2);
                txtAfterReplace.setText(AfterReplace);
            }
        }else{
            AfterReplace=BeforeReplace.replaceAll(beforeFirstHyphen,Replace1);
                txtAfterReplace.setText(AfterReplace);
        }
      
      
      
    }//GEN-LAST:event_cmdReplaceActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CopyTable;
    private javax.swing.JTabbedPane JTabPane;
    private javax.swing.JTable Table;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableB;
    private javax.swing.JTable TableH;
    private javax.swing.JScrollPane TableHPanel;
    private javax.swing.JTable TableL;
    private javax.swing.JScrollPane TableLPanel;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JCheckBox chkApprover;
    private javax.swing.JCheckBox chkCopyDefault;
    private javax.swing.JCheckBox chkCreator;
    private javax.swing.JCheckBox chkDefault;
    private javax.swing.JCheckBox chkFinalApprover;
    private javax.swing.JCheckBox chkGrantOther;
    private javax.swing.JCheckBox chkRestore;
    private javax.swing.JCheckBox chkSequence;
    private javax.swing.JComboBox cmbCopyModule;
    private javax.swing.JComboBox cmbHFields;
    private javax.swing.JComboBox cmbLFields;
    private javax.swing.JComboBox cmbModule;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdCanceling;
    private javax.swing.JButton cmdCopy;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdEdit1;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdFind;
    private javax.swing.JButton cmdFind1;
    private javax.swing.JButton cmdFindUser;
    private javax.swing.JButton cmdFindUserB;
    private javax.swing.JButton cmdHAdd;
    private javax.swing.JButton cmdHRemove;
    private javax.swing.JButton cmdLAdd;
    private javax.swing.JButton cmdLRemove;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdReplace;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdSaving;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblCopyHierarchyID;
    private javax.swing.JLabel lblHierarchyID;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtAfterReplace;
    private javax.swing.JTextField txtBeforeReplace;
    private javax.swing.JTextField txtCopyDesc;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtFromDate;
    private javax.swing.JTextField txtHname1;
    private javax.swing.JTextField txtHname2;
    private javax.swing.JTextField txtToDate;
    private javax.swing.JTextField txtUserID;
    private javax.swing.JTextField txtUserID1;
    private javax.swing.JTextField txtUserIDA;
    private javax.swing.JTextField txtUserIDB;
    private javax.swing.JTextField txtUserName;
    private javax.swing.JTextField txtUserName1;
    private javax.swing.JTextField txtUserNameA;
    private javax.swing.JTextField txtUserNameB;
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
        
        lblHierarchyID.setText(Integer.toString((int)ObjHierarchy.getAttribute("HIERARCHY_ID").getVal()));
        txtDesc.setText((String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj());
        EITLERPGLOBAL.setComboIndex(cmbModule,(int) ObjHierarchy.getAttribute("MODULE_ID").getVal());
        if((boolean) ObjHierarchy.getAttribute("IS_DEFAULT").getBool()) {
            chkDefault.setSelected(true);
        }
        else
            chkDefault.setSelected(false);
        
        //Now Generate the Table
        FormatGrid();
        
        for(int i=1;i<=ObjHierarchy.colRights.size();i++) {
            clsHierarchyUsers ObjUser=(clsHierarchyUsers) ObjHierarchy.colRights.get(Integer.toString(i));
            Object[] rowData=new Object[13];
            
            rowData[0]=Integer.toString((int)ObjUser.getAttribute("USER_ID").getVal());
            rowData[1]=Integer.toString((int)ObjUser.getAttribute("SR_NO").getVal());
            rowData[2]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("USER_ID").getVal());
            
            if((boolean) ObjUser.getAttribute("CREATOR").getBool()) {
                rowData[3]="Y";
            }
            else {
                rowData[3]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("APPROVER").getBool()) {
                rowData[4]="Y";
            }
            else {
                rowData[4]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("FINAL_APPROVER").getBool()) {
                rowData[5]="Y";
            }
            else {
                rowData[5]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("SKIP_SEQUENCE").getBool()) {
                rowData[6]="Y";
            }
            else {
                rowData[6]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("GRANT_OTHER").getBool()) {
                rowData[7]="Y";
            }
            else {
                rowData[7]=" ";
            }
            
            rowData[8]=Integer.toString((int)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[9]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[10]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("FROM_DATE").getObj());
            rowData[11]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("TO_DATE").getObj());
            
            if((boolean)ObjUser.getAttribute("RESTORE").getBool()) {
                rowData[12]="Y";
            }
            else {
                rowData[12]=" ";
            }
            
            DataModel.addRow(rowData);
            DataModel.SetUserObject(Table.getRowCount()-1, ObjUser.colFieldAccess);
        }//End of For loop
    }
    
    private void CopyDisplayData() {
        
        lblCopyHierarchyID.setText(Integer.toString((int)ObjHierarchy.getAttribute("HIERARCHY_ID").getVal()));
        txtCopyDesc.setText((String) ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj());
        EITLERPGLOBAL.setComboIndex(cmbCopyModule,(int) ObjHierarchy.getAttribute("MODULE_ID").getVal());
        if((boolean) ObjHierarchy.getAttribute("IS_DEFAULT").getBool()) {
            chkCopyDefault.setSelected(true);
        }
        else
            chkCopyDefault.setSelected(false);
        
        //Now Generate the Table
        CopyFormatGrid();       
        
        
        for(int i=1;i<=ObjHierarchy.colRights.size();i++) {
            clsHierarchyUsers ObjUser=(clsHierarchyUsers) ObjHierarchy.colRights.get(Integer.toString(i));
            Object[] rowData=new Object[13];
            
            rowData[0]=Integer.toString((int)ObjUser.getAttribute("USER_ID").getVal());
            rowData[1]=Integer.toString((int)ObjUser.getAttribute("SR_NO").getVal());
            rowData[2]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("USER_ID").getVal());
            
            if((boolean) ObjUser.getAttribute("CREATOR").getBool()) {
                rowData[3]="Y";
            }
            else {
                rowData[3]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("APPROVER").getBool()) {
                rowData[4]="Y";
            }
            else {
                rowData[4]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("FINAL_APPROVER").getBool()) {
                rowData[5]="Y";
            }
            else {
                rowData[5]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("SKIP_SEQUENCE").getBool()) {
                rowData[6]="Y";
            }
            else {
                rowData[6]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("GRANT_OTHER").getBool()) {
                rowData[7]="Y";
            }
            else {
                rowData[7]=" ";
            }
            
            rowData[8]=Integer.toString((int)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[9]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[10]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("FROM_DATE").getObj());
            rowData[11]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("TO_DATE").getObj());
            
            if((boolean)ObjUser.getAttribute("RESTORE").getBool()) {
                rowData[12]="Y";
            }
            else {
                rowData[12]=" ";
            }
            
            CopyDataModel.addRow(rowData);
            CopyDataModel.SetUserObject(CopyTable.getRowCount()-1, ObjUser.colFieldAccess);
        }//End of For loop
    }
    //Sets data to the Class Object
    private void SetData() {
        ObjHierarchy.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjHierarchy.setAttribute("MODULE_ID",EITLERPGLOBAL.getComboCode(cmbModule));
        ObjHierarchy.setAttribute("HIERARCHY_NAME",txtDesc.getText());
        if(chkDefault.isSelected()) {
            ObjHierarchy.setAttribute("IS_DEFAULT",true);
        }
        else
            ObjHierarchy.setAttribute("IS_DEFAULT",false);
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            ObjHierarchy.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
            ObjHierarchy.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
            ObjHierarchy.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
            ObjHierarchy.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        
        //Now Set the detail records
        ObjHierarchy.colRights.clear();
        int Rows= Table.getRowCount();
        
        for(int i=0;i<Rows;i++) {
            clsHierarchyUsers ObjUser=new clsHierarchyUsers();
            
            if(DataModel.getUserObject(i) instanceof HashMap) {
                ObjUser.colFieldAccess=(HashMap)DataModel.getUserObject(i);
            }
            else {
                ObjUser.colFieldAccess=new HashMap();
                
            }
            
            ObjUser.setAttribute("SR_NO",Integer.parseInt((String) Table.getValueAt(i,1))); //Sr. No.
            ObjUser.setAttribute("USER_ID",Integer.parseInt((String)Table.getValueAt(i,0))); //User ID
            if((String) Table.getValueAt(i,3) =="Y") {
                ObjUser.setAttribute("CREATOR",true);
            }
            else {
                ObjUser.setAttribute("CREATOR",false);
            }
            
            if((String)Table.getValueAt(i,4)=="Y") {
                ObjUser.setAttribute("APPROVER",true);
            }
            else {
                ObjUser.setAttribute("APPROVER",false);
            }
            
            if((String)Table.getValueAt(i,5)=="Y") {
                ObjUser.setAttribute("FINAL_APPROVER",true);
            }
            else {
                ObjUser.setAttribute("FINAL_APPROVER",false);
            }
            
            if((String)Table.getValueAt(i,6)=="Y") {
                ObjUser.setAttribute("SKIP_SEQUENCE",true);
            }
            else {
                ObjUser.setAttribute("SKIP_SEQUENCE",false);
            }
            
            if((String)Table.getValueAt(i,7)=="Y") {
                ObjUser.setAttribute("GRANT_OTHER",true);
            }
            else {
                ObjUser.setAttribute("GRANT_OTHER",false);
            }
            
            ObjUser.setAttribute("GRANT_USER_ID",Integer.parseInt((String)Table.getValueAt(i,8))); //GRANTED User ID
            ObjUser.setAttribute("FROM_DATE",EITLERPGLOBAL.formatDateDB((String)Table.getValueAt(i,10)));
            ObjUser.setAttribute("TO_DATE",EITLERPGLOBAL.formatDateDB((String)Table.getValueAt(i,11)));
            if((String)Table.getValueAt(i,12)=="Y") {
                ObjUser.setAttribute("RESTORE",true);
            }
            else {
                ObjUser.setAttribute("RESTORE",false);
            }
            //Add to collection
            ObjHierarchy.colRights.put(Integer.toString(ObjHierarchy.colRights.size()+1),ObjUser);
        }
    }
    
    private void CopySetData() {
        ObjHierarchy.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjHierarchy.setAttribute("MODULE_ID",EITLERPGLOBAL.getComboCode(cmbCopyModule));
        ObjHierarchy.setAttribute("HIERARCHY_NAME",txtCopyDesc.getText());
        if(chkCopyDefault.isSelected()) {
            ObjHierarchy.setAttribute("IS_DEFAULT",true);
        }
        else
            ObjHierarchy.setAttribute("IS_DEFAULT",false);
        
        //if(EditMode==EITLERPGLOBAL.ADD) {
            ObjHierarchy.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
            ObjHierarchy.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //}
        //else {
        //    ObjHierarchy.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
        //    ObjHierarchy.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        //}
        
        //Now Set the detail records
        ObjHierarchy.colRights.clear();
        int Rows= CopyTable.getRowCount();
        
        for(int i=0;i<Rows;i++) {
            clsHierarchyUsers ObjUser=new clsHierarchyUsers();
            
            if(CopyDataModel.getUserObject(i) instanceof HashMap) {
                ObjUser.colFieldAccess=(HashMap)CopyDataModel.getUserObject(i);
            }
            else {
                ObjUser.colFieldAccess=new HashMap();
                
            }
            
            ObjUser.setAttribute("SR_NO",Integer.parseInt((String) CopyTable.getValueAt(i,1))); //Sr. No.
            ObjUser.setAttribute("USER_ID",Integer.parseInt((String)CopyTable.getValueAt(i,0))); //User ID
            if((String) CopyTable.getValueAt(i,3) =="Y") {
                ObjUser.setAttribute("CREATOR",true);
            }
            else {
                ObjUser.setAttribute("CREATOR",false);
            }
            
            if((String)CopyTable.getValueAt(i,4)=="Y") {
                ObjUser.setAttribute("APPROVER",true);
            }
            else {
                ObjUser.setAttribute("APPROVER",false);
            }
            
            if((String)CopyTable.getValueAt(i,5)=="Y") {
                ObjUser.setAttribute("FINAL_APPROVER",true);
            }
            else {
                ObjUser.setAttribute("FINAL_APPROVER",false);
            }
            
            if((String)CopyTable.getValueAt(i,6)=="Y") {
                ObjUser.setAttribute("SKIP_SEQUENCE",true);
            }
            else {
                ObjUser.setAttribute("SKIP_SEQUENCE",false);
            }
            
            if((String)CopyTable.getValueAt(i,7)=="Y") {
                ObjUser.setAttribute("GRANT_OTHER",true);
            }
            else {
                ObjUser.setAttribute("GRANT_OTHER",false);
            }
            
            ObjUser.setAttribute("GRANT_USER_ID",Integer.parseInt((String)CopyTable.getValueAt(i,8))); //GRANTED User ID
            ObjUser.setAttribute("FROM_DATE",EITLERPGLOBAL.formatDateDB((String)CopyTable.getValueAt(i,10)));
            ObjUser.setAttribute("TO_DATE",EITLERPGLOBAL.formatDateDB((String)CopyTable.getValueAt(i,11)));
            if((String)CopyTable.getValueAt(i,12)=="Y") {
                ObjUser.setAttribute("RESTORE",true);
            }
            else {
                ObjUser.setAttribute("RESTORE",false);
            }
            //Add to collection
            ObjHierarchy.colRights.put(Integer.toString(ObjHierarchy.colRights.size()+1),ObjUser);
        }
    }
    
    private void ClearFields() {
        txtDesc.setText("");
        txtUserID.setText("");
        txtUserName.setText("");
        txtUserID1.setText("");
        txtUserName1.setText("");
        cmbModule.setSelectedIndex(-1);
        txtFromDate.setText("");
        txtToDate.setText("");
        
        chkDefault.setSelected(false);
        chkApprover.setSelected(false);
        chkFinalApprover.setSelected(false);
        chkCreator.setSelected(false);
        chkGrantOther.setSelected(false);
        chkRestore.setSelected(false);
        chkSequence.setSelected(false);
        FormatGrid();
    }
    
    private void SetFields(boolean pStat) {
        txtDesc.setEnabled(pStat);
        txtUserID.setEnabled(pStat);
        txtUserName.setEnabled(false);
        chkDefault.setEnabled(pStat);
        
        chkApprover.setEnabled(pStat);
        chkCreator.setEnabled(pStat);
        chkFinalApprover.setEnabled(pStat);
        chkGrantOther.setEnabled(pStat);
        chkSequence.setEnabled(pStat);
        
        cmbModule.setEnabled(pStat);
        cmdFind.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);
        cmdEdit1.setEnabled(pStat);
        
        cmdSaving.setEnabled(! pStat);
        cmdCanceling.setEnabled(! pStat);
        
        cmbHFields.setEnabled(pStat);
        cmbLFields.setEnabled(pStat);
        cmdHAdd.setEnabled(pStat);
        cmdHRemove.setEnabled(pStat);
        cmdLAdd.setEnabled(pStat);
        cmdLRemove.setEnabled(pStat);
        
        DataModelH.TableReadOnly(true);
        DataModelL.TableReadOnly(true);
    }
    
    private void Cancel() {
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
    }
    
    
    private void MoveFirst() {
        ObjHierarchy.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjHierarchy.MovePrevious();
        DisplayData();
    }
    
    private void MoveNext() {
        ObjHierarchy.MoveNext();
        DisplayData();
    }
    
    private void MoveLast() {
        ObjHierarchy.MoveLast();
        DisplayData();
    }
    
    private void Add() {
        EditMode=EITLERPGLOBAL.ADD;
        DisableToolbar();
        SetFields(true);
        ClearFields();
        cmbModule.requestFocus();
    }
    
    private void Edit() {
        EditMode=EITLERPGLOBAL.EDIT;
        DisableToolbar();
        SetFields(true);
        
        if (chkGrantOther.isSelected()) {
            txtUserID1.setEnabled(true);
            txtUserName1.setEnabled(false);
            chkRestore.setEnabled(true);
            cmdFind1.setEnabled(true);
        }
        else {
            txtUserID1.setEnabled(false);
            txtUserName1.setEnabled(false);
            chkRestore.setEnabled(false);
            cmdFind1.setEnabled(false);
        }
        txtDesc.requestFocus();
    }
    
    private void Delete() {
        if(ObjHierarchy.Delete()) {
            MoveLast();
            DisplayData();
            //GenerateTree();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is"+ObjHierarchy.LastError);
        }
    }
    
    private void Save() {
        // --------- Form Level Validations ------------ //
        
        if(txtDesc.getText().equals("")) {
            JOptionPane.showMessageDialog(null,"Please Specify Hierarchy Name");
            return;
        }
        
        if(cmbModule.getSelectedIndex()== -1) {
            JOptionPane.showMessageDialog(null,"Please select Module Name");
            return;
        }
        
        if(Table.getRowCount()<=0) {
            JOptionPane.showMessageDialog(null,"Please specify at least one user for hierarchy");
            return;
        }
        
        if(chkDefault.isSelected()) {
            ObjHierarchy.Find(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.getComboCode(cmbModule),true);
            if(ObjHierarchy.Ready) {
                JOptionPane.showMessageDialog(null,"Same Module can not be Default Module");
                return;
            }
        }
        
        SetData();
        ObjHierarchy.setAttribute("MODULE_ID",EITLERPGLOBAL.getComboCode(cmbModule));
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjHierarchy.Insert()) {
                MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjHierarchy.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjHierarchy.Update()) {
                //Nothing to do
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving item. Error is"+ObjHierarchy.LastError);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        
        
    }
    
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,211)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,212)) {
            cmdEdit.setEnabled(true);
        }
        else {
            cmdEdit.setEnabled(false);
        }
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,213)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,214)) {
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
        cmbModelModel=new EITLComboModel();
        cmbModule.removeAllItems();
        cmbModule.setModel(cmbModelModel);        
        //strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID IN (SELECT MODULE_ID FROM D_COM_MENU_MASTER WHERE MODULE_ID!=0 AND (PARENT_ID LIKE '60%' OR PARENT_ID LIKE '70%') ORDER BY MODULE_ID)";
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID IN (SELECT MODULE_ID FROM D_COM_MENU_MASTER WHERE MODULE_ID!=0 AND (PARENT_ID LIKE '60%' OR PARENT_ID LIKE '70%') OR MODULE_ID=72 OR MODULE_ID=149 ORDER BY MODULE_ID)";
        
        List=clsModules.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Text=(String) ObjModules.getAttribute("MODULE_DESC").getObj();
            aData.Code=(int) ObjModules.getAttribute("MODULE_ID").getVal();
            cmbModelModel.addElement(aData);
        }
    }
    
    private void CopyGenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbType ------- //
        cmbCopyModelModel=new EITLComboModel();
        cmbCopyModule.removeAllItems();
        cmbCopyModule.setModel(cmbCopyModelModel);        
       //strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID IN (SELECT MODULE_ID FROM D_COM_MENU_MASTER WHERE MODULE_ID!=0 AND (PARENT_ID LIKE '60%' OR PARENT_ID LIKE '70%') ORDER BY MODULE_ID)";
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID IN (SELECT MODULE_ID FROM D_COM_MENU_MASTER WHERE MODULE_ID!=0 AND (PARENT_ID LIKE '60%' OR PARENT_ID LIKE '70%') OR MODULE_ID=72 OR MODULE_ID=149 ORDER BY MODULE_ID)";
        
        List=clsModules.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsModules ObjModules=(clsModules) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Text=(String) ObjModules.getAttribute("MODULE_DESC").getObj();
            aData.Code=(int) ObjModules.getAttribute("MODULE_ID").getVal();
            cmbCopyModelModel.addElement(aData);
        }
    }
    
    private void FormatGrid() {
        
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        Table.setModel(DataModel);
        
        //Set the table Readonly
        DataModel.TableReadOnly(true);
        
        DefaultTableModel a;
        
        //Add Columns
        DataModel.addColumn("UserID");
        DataModel.addColumn("Sr.");
        DataModel.addColumn("User");
        DataModel.addColumn("Creator");
        DataModel.addColumn("Approver");
        DataModel.addColumn("Final Approver");
        DataModel.addColumn("Skip Sequence");
        DataModel.addColumn("Grant Other");
        DataModel.addColumn("Grant UserID");
        DataModel.addColumn("User Name");
        DataModel.addColumn("From Date");
        DataModel.addColumn("UpTo Date");
        DataModel.addColumn("Restore");
        
        //Now hide the column 1
        TableColumnModel ColModel=Table.getColumnModel();
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ColModel.getColumn(0).setMinWidth(0);
        ColModel.getColumn(0).setPreferredWidth(0);
        
        FormatFieldGrid();
    }
    
    private void FormatGridTableA() {
        
        DataModelTableA=new EITLTableModel();
        
        TableA.removeAll();
        TableA.setModel(DataModelTableA);
        
        //Set the table Readonly
        DataModelTableA.TableReadOnly(true);
        
        DefaultTableModel a;
        
        //Add Columns
        DataModelTableA.addColumn("HierarchyID");
        DataModelTableA.addColumn("HierarchyName");
        DataModelTableA.addColumn("ModuleID");
        DataModelTableA.addColumn("IsDefault");
        
        DataModelTableA.addColumn("UserID");
        DataModelTableA.addColumn("Sr.");
        DataModelTableA.addColumn("User");
        DataModelTableA.addColumn("Creator");
        DataModelTableA.addColumn("Approver");
        DataModelTableA.addColumn("Final Approver");
        DataModelTableA.addColumn("Skip Sequence");
        DataModelTableA.addColumn("Grant Other");
        DataModelTableA.addColumn("Grant UserID");
        DataModelTableA.addColumn("User Name");
        DataModelTableA.addColumn("From Date");
        DataModelTableA.addColumn("UpTo Date");
        DataModelTableA.addColumn("Restore");
        
        
        
        DataModelTableA.SetVariable(0,"HIERARCHY_ID"); //1
        DataModelTableA.SetVariable(1,"HIERARCHY_NAME");
        DataModelTableA.SetVariable(2,"MODULE_ID"); //1
        DataModelTableA.SetVariable(3,"IS_DEFAULT");
        
        DataModelTableA.SetVariable(4,"USER_ID"); //1
        DataModelTableA.SetVariable(5, "SR_NO");
        //DataModelTableA.SetVariable(6,"USER_NAME"); //1
        DataModelTableA.SetVariable(7, "CREATOR");
        DataModelTableA.SetVariable(8,"APPROVER"); //1
        DataModelTableA.SetVariable(9, "FINAL_APPROVER");
        DataModelTableA.SetVariable(10,"SKIP_SEQUENCE"); //1
        DataModelTableA.SetVariable(11, "GRANT_OTHER");
        DataModelTableA.SetVariable(12,"GRANT_USER_ID"); //1
        //DataModelTableA.SetVariable(13, "USER_NAME");
        DataModelTableA.SetVariable(14,"FROM_DATE");
        DataModelTableA.SetVariable(15,"TO_DATE");
        DataModelTableA.SetVariable(16,"RESTORE");
        
        //Now hide the column 1
        //TableColumnModel ColModel=TableA.getColumnModel();
        TableA.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        ColModel.getColumn(0).setMinWidth(0);
//        ColModel.getColumn(0).setPreferredWidth(0);
//        
        //FormatFieldGrid();
    }
    
    private void FormatGridTableB() {
        
        DataModelTableB=new EITLTableModel();
        
        TableB.removeAll();
        TableB.setModel(DataModelTableB);
        
        //Set the table Readonly
        DataModelTableB.TableReadOnly(true);
        
        DefaultTableModel a;
        
        //Add Columns
        DataModelTableB.addColumn("HierarchyID");
        DataModelTableB.addColumn("HierarchyName");
        DataModelTableB.addColumn("ModuleID");
        DataModelTableB.addColumn("IsDefault");
        
        DataModelTableB.addColumn("UserID");
        DataModelTableB.addColumn("Sr.");
        DataModelTableB.addColumn("User");
        DataModelTableB.addColumn("Creator");
        DataModelTableB.addColumn("Approver");
        DataModelTableB.addColumn("Final Approver");
        DataModelTableB.addColumn("Skip Sequence");
        DataModelTableB.addColumn("Grant Other");
        DataModelTableB.addColumn("Grant UserID");
        DataModelTableB.addColumn("User Name");
        DataModelTableB.addColumn("From Date");
        DataModelTableB.addColumn("UpTo Date");
        DataModelTableB.addColumn("Restore");
        
        
        
        DataModelTableB.SetVariable(0,"HIERARCHY_ID"); //1
        DataModelTableB.SetVariable(1,"HIERARCHY_NAME");
        DataModelTableB.SetVariable(2,"MODULE_ID"); //1
        DataModelTableB.SetVariable(3,"IS_DEFAULT");
        
        DataModelTableB.SetVariable(4,"USER_ID"); //1
        DataModelTableB.SetVariable(5, "SR_NO");
        //DataModelTableB.SetVariable(6,"USER_NAME"); //1
        DataModelTableB.SetVariable(7, "CREATOR");
        DataModelTableB.SetVariable(8,"APPROVER"); //1
        DataModelTableB.SetVariable(9, "FINAL_APPROVER");
        DataModelTableB.SetVariable(10,"SKIP_SEQUENCE"); //1
        DataModelTableB.SetVariable(11, "GRANT_OTHER");
        DataModelTableB.SetVariable(12,"GRANT_USER_ID"); //1
        //DataModelTableB.SetVariable(13, "USER_NAME");
        DataModelTableB.SetVariable(14,"FROM_DATE");
        DataModelTableB.SetVariable(15,"TO_DATE");
        DataModelTableB.SetVariable(16,"RESTORE");
        
        //Now hide the column 1
        //TableColumnModel ColModel=TableA.getColumnModel();
        TableB.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        ColModel.getColumn(0).setMinWidth(0);
//        ColModel.getColumn(0).setPreferredWidth(0);
//        
        //FormatFieldGrid();
    }
    
    private void CopyFormatGrid() {
        
        CopyDataModel=new EITLTableModel();
        
        CopyTable.removeAll();
        CopyTable.setModel(CopyDataModel);
        
        //Set the table Readonly
        CopyDataModel.TableReadOnly(false);
        CopyDataModel.SetReadOnly(0);
        
        DefaultTableModel a;
        
        //Add Columns
        CopyDataModel.addColumn("UserID");
        CopyDataModel.addColumn("Sr.");
        CopyDataModel.addColumn("User");
        CopyDataModel.addColumn("Creator");
        CopyDataModel.addColumn("Approver");
        CopyDataModel.addColumn("Final Approver");
        CopyDataModel.addColumn("Skip Sequence");
        CopyDataModel.addColumn("Grant Other");
        CopyDataModel.addColumn("Grant UserID");
        CopyDataModel.addColumn("User Name");
        CopyDataModel.addColumn("From Date");
        CopyDataModel.addColumn("UpTo Date");
        CopyDataModel.addColumn("Restore");
        
        
            //CopyDataModel.SetVariable(0,"SR_NO"); //0 - Read Only
            CopyDataModel.SetVariable(0,"USER_ID"); //1
            CopyDataModel.SetVariable(2, "USER_NAME");
            
            
            
            
            
        //Now hide the column 1
//        TableColumnModel ColModel=CopyTable.getColumnModel();
        CopyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        ColModel.getColumn(0).setMinWidth(0);
//        ColModel.getColumn(0).setPreferredWidth(0);
//        
        //FormatFieldGrid();
    }
    
    
    private void FormatFieldGrid() {
        DataModelH=new EITLTableModel();
        
        TableH.removeAll();
        TableH.setModel(DataModelH);
        
        //Set the table Readonly
        DataModelH.TableReadOnly(true);
        
        //Add Columns
        DataModelH.addColumn("Field Name");
        
        
        DataModelL=new EITLTableModel();
        
        TableL.removeAll();
        TableL.setModel(DataModelL);
        
        //Set the table Readonly
        DataModelL.TableReadOnly(true);
        
        //Add Columns
        DataModelL.addColumn("Field Name");
        
    }
    
    
    private void ShowMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }
    
    private void GenerateFieldCombo() {
        
        Connection tmpConn;
        Statement stTmp;
        ResultSet rsTmp;
        ResultSetMetaData rsInfo;
        
        
        String HeaderTable="";
        String DetailTable="";
        
        SelModuleID=EITLERPGLOBAL.getComboCode(cmbModule);
        
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        
        HeaderTable=clsModules.getHeaderTableName(EITLERPGLOBAL.gCompanyID, SelModuleID);
        DetailTable=clsModules.getDetailTableName(EITLERPGLOBAL.gCompanyID, SelModuleID);
        
        try {
            tmpConn=data.getConn();
            
            stTmp=tmpConn.createStatement();
            rsTmp=stTmp.executeQuery("SELECT * FROM "+HeaderTable+" LIMIT 1");
            rsInfo=rsTmp.getMetaData();
            
            cmbLFields.removeAllItems();
            
            //----- Generate cmbType ------- //
            cmbHFieldsModel=new EITLComboModel();
            cmbHFields.removeAllItems();
            cmbHFields.setModel(cmbHFieldsModel);
            
            for(int i=1;i<=rsInfo.getColumnCount();i++) {
                
                ComboData aData=new ComboData();
                aData.Text=rsInfo.getColumnName(i);
                aData.strCode=rsInfo.getColumnName(i);
                cmbHFieldsModel.addElement(aData);
            }
            
            rsTmp=null;
            
            rsTmp=stTmp.executeQuery("SELECT * FROM  "+DetailTable+" LIMIT 1");
            rsInfo=rsTmp.getMetaData();
            
            
            //----- Generate cmbType ------- //
            cmbLFieldsModel=new EITLComboModel();
            cmbLFields.removeAllItems();
            cmbLFields.setModel(cmbLFieldsModel);
            
            for(int i=1;i<=rsInfo.getColumnCount();i++) {
                ComboData aData=new ComboData();
                aData.Text=rsInfo.getColumnName(i);
                aData.strCode=rsInfo.getColumnName(i);
                cmbLFieldsModel.addElement(aData);
            }
            
        }
        catch(Exception e) {
            
        }
    }
    
    
    private void DisplayFieldAccess() {
        int SelRow=0;
        
        try {
            if(Table.getSelectedRow()>=0) {
                SelRow=Table.getSelectedRow();
                
                if(DataModel.getUserObject(SelRow) instanceof HashMap) {
                    FormatFieldGrid();
                    
                    HashMap colFA=(HashMap) DataModel.getUserObject(SelRow);
                    
                    for(int i=1;i<=colFA.size();i++) {
                        clsFieldAccess ObjFA=(clsFieldAccess)colFA.get(Integer.toString(i));
                        
                        String FieldType=(String)ObjFA.getAttribute("FIELD_TYPE").getObj();
                        
                        if(FieldType.equals("H")) {
                            Object[] rowData=new Object[1];
                            rowData[0]=(String)ObjFA.getAttribute("FIELD_NAME").getObj();
                            DataModelH.addRow(rowData);
                        }
                        
                        if(FieldType.equals("L")) {
                            Object[] rowData=new Object[1];
                            rowData[0]=(String)ObjFA.getAttribute("FIELD_NAME").getObj();
                            DataModelL.addRow(rowData);
                        }
                    }
                }
            }
            
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error occured while dipslaying Field Access Data. \n"+e.getMessage());
        }
    }
    
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.frmHierarchyFeltSalesFind",true);
        frmHierarchyFeltSalesFind ObjReturn= (frmHierarchyFeltSalesFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjHierarchy.Filter(ObjReturn.strQuery)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveFirst();
        }
    }
    
    private void PreviewReport() {
        try {
            AppletFrame aFrame=new AppletFrame("Hierarchy Report");
            aFrame.startAppletEx("EITLERP.frmRptHierarchy","Hierarchy Report");
        }
        catch(Exception e) {
            
        }
    }
    
    private void GenerateGridA(){
        try{
            HashMap ListA;
            FormatGridTableA();
            ListA=clsHierarchy.getHirarchy(txtUserIDA.getText());
            
            //for(int i=1;i<=ObjHierarchy.colRights.size();i++) {
            //for(int i=1;i<=(ListA.size()-1);i++){                            
                for(int i=1;i<=ListA.size();i++){                            
                System.out.println(i);
            clsHierarchyUsersList ObjUser=(clsHierarchyUsersList) ListA.get(Integer.toString(i));
            Object[] rowData=new Object[17];
            rowData[0]=Integer.toString((int)ObjUser.getAttribute("HIERARCHY_ID").getVal());
            rowData[1]=ObjUser.getAttribute("HIERARCHY_NAME").getString();
            rowData[2]=Integer.toString((int)ObjUser.getAttribute("MODULE_ID").getVal());
            rowData[3]=Integer.toString((int)ObjUser.getAttribute("IS_DEFAULT").getVal());
            
            rowData[4]=Integer.toString((int)ObjUser.getAttribute("USER_ID").getVal());
            rowData[5]=Integer.toString((int)ObjUser.getAttribute("SR_NO").getVal());
            rowData[6]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("USER_ID").getVal());
            
            if((boolean) ObjUser.getAttribute("CREATOR").getBool()) {
                rowData[7]="Y";
            }
            else {
                rowData[7]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("APPROVER").getBool()) {
                rowData[8]="Y";
            }
            else {
                rowData[8]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("FINAL_APPROVER").getBool()) {
                rowData[9]="Y";
            }
            else {
                rowData[9]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("SKIP_SEQUENCE").getBool()) {
                rowData[10]="Y";
            }
            else {
                rowData[10]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("GRANT_OTHER").getBool()) {
                rowData[11]="Y";
            }
            else {
                rowData[11]=" ";
            }
            
            rowData[12]=Integer.toString((int)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[13]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[14]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("FROM_DATE").getObj());
            rowData[15]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("TO_DATE").getObj());
            
            if((boolean)ObjUser.getAttribute("RESTORE").getBool()) {
                rowData[16]="Y";
            }
            else {
                rowData[16]=" ";
            }
            
            DataModelTableA.addRow(rowData);
            //DataModelTableA.SetUserObject(TableA.getRowCount()-1, ObjUser.colFieldAccess);
        }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void GenerateGridB(){
        try{
            HashMap ListB;
            FormatGridTableB();
            ListB=clsHierarchy.getHirarchy(txtUserIDB.getText());
            
            //for(int i=1;i<=ObjHierarchy.colRights.size();i++) {
            //for(int i=1;i<=(ListA.size()-1);i++){                            
                for(int i=1;i<=ListB.size();i++){                            
                System.out.println(i);
            clsHierarchyUsersList ObjUser=(clsHierarchyUsersList) ListB.get(Integer.toString(i));
            Object[] rowData=new Object[17];
            rowData[0]=Integer.toString((int)ObjUser.getAttribute("HIERARCHY_ID").getVal());
            rowData[1]=ObjUser.getAttribute("HIERARCHY_NAME").getString();
            rowData[2]=Integer.toString((int)ObjUser.getAttribute("MODULE_ID").getVal());
            rowData[3]=Integer.toString((int)ObjUser.getAttribute("IS_DEFAULT").getVal());
            
            rowData[4]=Integer.toString((int)ObjUser.getAttribute("USER_ID").getVal());
            rowData[5]=Integer.toString((int)ObjUser.getAttribute("SR_NO").getVal());
            rowData[6]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("USER_ID").getVal());
            
            if((boolean) ObjUser.getAttribute("CREATOR").getBool()) {
                rowData[7]="Y";
            }
            else {
                rowData[7]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("APPROVER").getBool()) {
                rowData[8]="Y";
            }
            else {
                rowData[8]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("FINAL_APPROVER").getBool()) {
                rowData[9]="Y";
            }
            else {
                rowData[9]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("SKIP_SEQUENCE").getBool()) {
                rowData[10]="Y";
            }
            else {
                rowData[10]=" ";
            }
            
            if((boolean)ObjUser.getAttribute("GRANT_OTHER").getBool()) {
                rowData[11]="Y";
            }
            else {
                rowData[11]=" ";
            }
            
            rowData[12]=Integer.toString((int)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[13]= clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjUser.getAttribute("GRANT_USER_ID").getVal());
            rowData[14]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("FROM_DATE").getObj());
            rowData[15]= EITLERPGLOBAL.formatDate((String) ObjUser.getAttribute("TO_DATE").getObj());
            
            if((boolean)ObjUser.getAttribute("RESTORE").getBool()) {
                rowData[16]="Y";
            }
            else {
                rowData[16]=" ";
            }
            
            DataModelTableB.addRow(rowData);
            //DataModelTableA.SetUserObject(TableA.getRowCount()-1, ObjUser.colFieldAccess);
        }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private String ReplaceHierarchyName(String HierarchyName){
        String BeforeReplace = HierarchyName;
        String AfterReplace = "";
        String Replace1 = txtHname1.getText();
        String Replace2 = txtHname2.getText();
      //AfterReplace=ReplaceBetween.replaceBetween(BeforeReplace,"","-",Replace1);
        int count=BeforeReplace.indexOf("-");

        String[] parts = BeforeReplace.split("\\-"); // String array, each element is text between dots                
//String beforeFirstHyphen = BefpreReplace.split("\\.")[0];       
        
        //txtAfterReplace.setText(AfterReplace);      
        //AfterReplace=BeforeReplace.replaceAll(beforeFirstHyphen,Replace1);
        //txtAfterReplace.setText(AfterReplace);
        //if(count!=-1){}
        if (Replace1.equals("")) {
            if (!Replace2.equals("")) {
               if(count!=-1){
                String betweenFirstAndSecondHyphen = parts[1];
                System.out.println(betweenFirstAndSecondHyphen);
                AfterReplace = BeforeReplace.replaceAll(betweenFirstAndSecondHyphen, Replace2);
              //  return AfterReplace;
            }
               if(count==-1){
                   String beforeFirstHyphen = parts[0];
            System.out.println(beforeFirstHyphen);
            AfterReplace = BeforeReplace.replaceAll(beforeFirstHyphen, Replace2);
               }
            }
        } else {
            String beforeFirstHyphen = parts[0];
            System.out.println(beforeFirstHyphen);
            AfterReplace = BeforeReplace.replaceAll(beforeFirstHyphen, Replace1);
            //return AfterReplace;
        }
        System.out.println(AfterReplace);
        return AfterReplace;
    }
    
    
    
}