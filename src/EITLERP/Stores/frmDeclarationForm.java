/*
 * frmDeclarationForm.java
 *
 * Created on June 8, 2004, 12:15 PM
 */

package EITLERP.Stores;

/**
 *
 * @author  jadave
 */
/*<APPLET CODE=frmDeclarationForm.class HEIGHT=474 WIDTH=758></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import EITLERP.Purchase.*;
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.text.*;
import java.util.*;
import java.net.*;
import EITLERP.Utils.*;


public class frmDeclarationForm extends javax.swing.JApplet {
    
    private int EditMode=0;
    
    private EITLTableModel DataModelH;
    private EITLTableModel DataModelL;
    private EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
    
    private HashMap colVariables=new HashMap();
    private HashMap colVariables_H=new HashMap();
    clsColumn ObjColumn=new clsColumn();
    
    //private JEP myParser=new JEP();
    private boolean Updating=false;
    private boolean Updating_H=false;
    private boolean DoNotEvaluate=false;
    
    private clsDeclarationForm ObjDeclarationForm;
    
    private int SelHierarchyID=0; //Selected Hierarchy
    private int lnFromID=0;
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    
    private EITLTableModel DataModelA;
    private EITLTableCellRenderer Paint=new EITLTableCellRenderer();
    
    private boolean HistoryView=false;
    private String theDocNo="";
    private EITLTableModel DataModelHS;
    
    String cellLastValueL="";
    String cellLastValueH="";
    public frmPendingApprovals frmPA;
    /** Creates new form frmTemplate */
    public void init() {
        System.gc();
        setSize(780,500);
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
        
        ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
        
        FormatGrid();
        FormatGrid_H();
        
        GenerateCombos();
        ObjDeclarationForm=new clsDeclarationForm();
        
        SetMenuForRights();
        
        if(getName().equals("Link")) {
            
        }
        else {
            if(ObjDeclarationForm.LoadData(EITLERPGLOBAL.gCompanyID)) {
                ObjDeclarationForm.MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while loading data. Error is "+ObjDeclarationForm.LastError);
            }
        }
        
        txtAuditRemarks.setVisible(false);
    }
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel=new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=13");
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=13");
        }
        for(int i=1;i<=List.size();i++) {
            clsHierarchy ObjHierarchy=(clsHierarchy) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text=(String)ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //
        
    }
    
    private void FormatGrid() {
        HashMap ColList=new HashMap();
        
        DataModelL=new EITLTableModel();
        
        TableL.removeAll();
        TableL.setModel(DataModelL);
        
        //Set the table Readonly
        DataModelL.TableReadOnly(false);
        
        
        
        ColList=clsSystemColumn.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=13 AND HIDDEN=0 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for(int i=1;i<=ColList.size();i++) {
            clsSystemColumn ObjColumn=(clsSystemColumn)ColList.get(Integer.toString(i));
            
            //Add Column First
            DataModelL.addColumn((String)ObjColumn.getAttribute("CAPTION").getObj()); //0
            
            
            if(ObjColumn.getAttribute("NUMERIC").getBool()) {
                DataModelL.SetNumeric(TableL.getColumnCount()-1, true);
            }
            else {
                DataModelL.SetNumeric(TableL.getColumnCount()-1, false);
            }
            
            
            String Variable=(String)ObjColumn.getAttribute("VARIABLE").getObj();
            
            if(Variable.equals("BAL_QTY")||Variable.equals("RECD_QTY")) {
                DataModelL.SetColID(TableL.getColumnCount()-1, -99);
            }
            else {
                DataModelL.SetColID(TableL.getColumnCount()-1, 0);
            }
            
            
            DataModelL.SetVariable(TableL.getColumnCount()-1,Variable.trim());
            DataModelL.SetOperation(TableL.getColumnCount()-1, "-");
            DataModelL.SetInclude(TableL.getColumnCount()-1,true);
            
            if(ObjColumn.getAttribute("READONLY").getBool()) {
                DataModelL.SetReadOnly(TableL.getColumnCount()-1);
            }
        }
        
        
        SetupColumns();
        
        //Now hide the column 1
        TableColumnModel ColModel=TableL.getColumnModel();
        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //------- Install Table List Selection Listener ------//
        TableL.getColumnModel().getSelectionModel().addListSelectionListener(
        new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int last=TableL.getSelectedColumn();
                String strVar=DataModelL.getVariable(last);
                
                
                //=============== Cell Editing Routine =======================//
                try {
                    cellLastValueL=(String)TableL.getValueAt(TableL.getSelectedRow(),TableL.getSelectedColumn());
                    
                    TableL.editCellAt(TableL.getSelectedRow(),TableL.getSelectedColumn());
                    if(TableL.getEditorComponent() instanceof JTextComponent) {
                        ((JTextComponent)TableL.getEditorComponent()).selectAll();
                    }
                }
                catch(Exception cell){}
                //============= Cell Editing Routine Ended =================//
                
                
            }
        }
        );
        //===================================================//
        
        
        //----- Install Table Model Event Listener -------//
        TableL.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    
                    //=========== Cell Update Prevention Check ===========//
                    String curValue=(String)TableL.getValueAt(TableL.getSelectedRow(), e.getColumn());
                    if(curValue.equals(cellLastValueL)) {
                        return;
                    }
                    //====================================================//
                    
                    
                    int col = e.getColumn();
                    
                    if(DoNotEvaluate) {
                        return;
                    }
                    /*if(!Updating) {
                        UpdateResults(col);
                    }*/
                    
                    //If Item ID has changed
                    if(col==DataModelL.getColFromVariable("ITEM_CODE")) {
                        try {
                            DoNotEvaluate=true; //Stops Formula Evaluation
                            String lItemID=(String)DataModelL.getValueAt(TableL.getSelectedRow(),DataModelL.getColFromVariable("ITEM_CODE"));
                            String lItemName=clsItem.getItemName((int)EITLERPGLOBAL.gCompanyID, lItemID);
                            
                            TableL.setValueAt(lItemName, TableL.getSelectedRow(),DataModelL.getColFromVariable("ITEM_NAME"));
                            
                            int lItemUnit=clsItem.getItemUnit(EITLERPGLOBAL.gCompanyID, lItemID);
                            TableL.setValueAt(Integer.toString(lItemUnit),TableL.getSelectedRow(),DataModelL.getColFromVariable("UNIT_ID"));
                            String lUnitName=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", lItemUnit);
                            TableL.setValueAt(lUnitName,TableL.getSelectedRow(),DataModelL.getColFromVariable("UNIT_NAME"));
                            DoNotEvaluate=false;
                        }
                        catch(Exception ex){DoNotEvaluate=false;}
                    }
                    
                    if(col==DataModelL.getColFromVariable("RECD_QTY")) {
                        DoNotEvaluate=true; //Stops Formula Evaluation
                        double ReceivedQty = Double.parseDouble(DataModelL.getValueAt(TableL.getSelectedRow(),DataModelL.getColFromVariable("RECD_QTY")).toString());
                        TableL.setValueAt(Double.toString(ReceivedQty), TableL.getSelectedRow(),DataModelL.getColFromVariable("BAL_QTY"));
                        DoNotEvaluate=false;
                    }
                }
            }
        });
        
        //This Change//
        //----------------------------------------------------------//
        // int RetCol = DataModelL.getColFromVariable("RETURNED");
        int RetCol = DataModelL.getColFromVariable("RETURNABLE");
        //----------------------------------------------------------//
        if(RetCol>0) {
            Renderer.setCustomComponent(RetCol,"CheckBox");
            JCheckBox aCheckBox=new JCheckBox();
            aCheckBox.setBackground(Color.WHITE);
            TableL.getColumnModel().getColumn(RetCol).setCellEditor(new DefaultCellEditor(aCheckBox));
            TableL.getColumnModel().getColumn(RetCol).setCellRenderer(Renderer);
        }
        
    }
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,591)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
       /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,132))
       {
          cmdEdit.setEnabled(true);
       }
       else
       {
           cmdEdit.setEnabled(false);
       }*/
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,593)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,594)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
    }
    
    private void SetupColumns() {
        HashMap List=new HashMap();
        HashMap ColList=new HashMap();
        
        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableL.setRowSelectionAllowed(true);
        TableL.setColumnSelectionAllowed(true);
        
        ColList=clsSystemColumn.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=13 AND HIDDEN=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for(int i=1;i<=ColList.size();i++) {
            clsSystemColumn ObjColumn=(clsSystemColumn)ColList.get(Integer.toString(i));
            
            //Add Column First
            DataModelL.addColumn(""); //
            DataModelL.SetColID(TableL.getColumnCount()-1, 0);
            DataModelL.SetVariable(TableL.getColumnCount()-1,(String)ObjColumn.getAttribute("VARIABLE").getObj());
            DataModelL.SetOperation(TableL.getColumnCount()-1, "-");
            DataModelL.SetInclude(TableL.getColumnCount()-1,true);
            DataModelL.SetNumeric(TableL.getColumnCount()-1,ObjColumn.getAttribute("NUMERIC").getBool());
            
            DataModelL.SetReadOnly(TableL.getColumnCount()-1);
            
            //Hide the Column
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setPreferredWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setMaxWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setMinWidth(0);
            TableL.getColumnModel().getColumn(TableL.getColumnCount()-1).setWidth(0);
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDecID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtContractor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPONO = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        cmdNext1 = new javax.swing.JButton();
        chkCancelled = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        txtAdd1 = new javax.swing.JTextField();
        txtAdd2 = new javax.swing.JTextField();
        txtAdd3 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPODate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPIN = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtDeptID = new javax.swing.JTextField();
        txtDept = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtPurpose = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtRecdBy = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TableL = new javax.swing.JTable();
        HeaderPane = new javax.swing.JScrollPane();
        TableH = new javax.swing.JTable();
        cmdNext2 = new javax.swing.JButton();
        cmdNext4 = new javax.swing.JButton();
        cmdInsert = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        cmbHierarchy = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        txtFrom = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtFromRemarks = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        OpgApprove = new javax.swing.JRadioButton();
        OpgFinal = new javax.swing.JRadioButton();
        OpgReject = new javax.swing.JRadioButton();
        OpgHold = new javax.swing.JRadioButton();
        jLabel33 = new javax.swing.JLabel();
        cmbSendTo = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        txtToRemarks = new javax.swing.JTextField();
        cmdNext3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        lblDocumentHistory = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdPreviewA = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();
        lblTitle = new javax.swing.JLabel();

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

        Tab1.setLayout(null);

        Tab1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.setEnabled(false);
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Declaration No :");
        Tab1.add(jLabel2);
        jLabel2.setBounds(5, 31, 125, 14);

        txtDecID.setEditable(false);
        Tab1.add(txtDecID);
        txtDecID.setBounds(135, 30, 98, 19);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Date :");
        Tab1.add(jLabel3);
        jLabel3.setBounds(275, 32, 65, 14);

        txtDate.setName("DECLARATION_DATE");
        txtDate.setNextFocusableComponent(txtPONO);
        txtDate.setEnabled(false);
        Tab1.add(txtDate);
        txtDate.setBounds(345, 30, 100, 19);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Contractor's Name :");
        Tab1.add(jLabel4);
        jLabel4.setBounds(5, 92, 125, 14);

        txtContractor.setName("CONTRACTOR_NAME");
        txtContractor.setNextFocusableComponent(txtAdd1);
        txtContractor.setEnabled(false);
        Tab1.add(txtContractor);
        txtContractor.setBounds(135, 90, 310, 19);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("PO No :");
        Tab1.add(jLabel6);
        jLabel6.setBounds(5, 62, 125, 14);

        txtPONO.setName("PO_NO");
        txtPONO.setEnabled(false);
        Tab1.add(txtPONO);
        txtPONO.setBounds(135, 60, 98, 19);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Remarks :");
        Tab1.add(jLabel21);
        jLabel21.setBounds(5, 325, 125, 14);

        txtRemarks.setName("REMARKS");
        txtRemarks.setNextFocusableComponent(chkCancelled);
        txtRemarks.setEnabled(false);
        Tab1.add(txtRemarks);
        txtRemarks.setBounds(135, 324, 310, 19);

        cmdNext1.setText("Next >>");
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });

        Tab1.add(cmdNext1);
        cmdNext1.setBounds(661, 332, 90, 25);

        chkCancelled.setText("Cancelled");
        chkCancelled.setNextFocusableComponent(cmdNext1);
        chkCancelled.setEnabled(false);
        Tab1.add(chkCancelled);
        chkCancelled.setBounds(667, 3, 85, 23);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Address :");
        Tab1.add(jLabel7);
        jLabel7.setBounds(5, 122, 125, 14);

        txtAdd1.setName("ADD1");
        txtAdd1.setNextFocusableComponent(txtAdd2);
        txtAdd1.setEnabled(false);
        Tab1.add(txtAdd1);
        txtAdd1.setBounds(135, 120, 310, 19);

        txtAdd2.setName("ADD2");
        txtAdd2.setNextFocusableComponent(txtAdd3);
        txtAdd2.setEnabled(false);
        Tab1.add(txtAdd2);
        txtAdd2.setBounds(135, 144, 310, 19);

        txtAdd3.setName("ADD3");
        txtAdd3.setNextFocusableComponent(txtCity);
        txtAdd3.setEnabled(false);
        Tab1.add(txtAdd3);
        txtAdd3.setBounds(135, 168, 310, 19);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Date :");
        Tab1.add(jLabel8);
        jLabel8.setBounds(275, 62, 65, 14);

        txtPODate.setBackground(new java.awt.Color(192, 192, 192));
        txtPODate.setName("PO_DATE");
        txtPODate.setEnabled(false);
        Tab1.add(txtPODate);
        txtPODate.setBounds(345, 60, 100, 19);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("City :");
        Tab1.add(jLabel9);
        jLabel9.setBounds(5, 198, 125, 14);

        txtCity.setName("CITY");
        txtCity.setNextFocusableComponent(txtPIN);
        txtCity.setEnabled(false);
        Tab1.add(txtCity);
        txtCity.setBounds(135, 196, 120, 19);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Pincode :");
        Tab1.add(jLabel10);
        jLabel10.setBounds(275, 198, 65, 14);

        txtPIN.setName("PINCODE");
        txtPIN.setNextFocusableComponent(txtDeptID);
        txtPIN.setEnabled(false);
        Tab1.add(txtPIN);
        txtPIN.setBounds(345, 196, 100, 19);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Department :");
        Tab1.add(jLabel11);
        jLabel11.setBounds(5, 232, 125, 14);

        txtDeptID.setName("FOR_DEPT_ID");
        txtDeptID.setNextFocusableComponent(txtPurpose);
        txtDeptID.setEnabled(false);
        txtDeptID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDeptIDFocusLost(evt);
            }
        });
        txtDeptID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDeptIDKeyPressed(evt);
            }
        });

        Tab1.add(txtDeptID);
        txtDeptID.setBounds(135, 230, 50, 19);

        txtDept.setBackground(new java.awt.Color(192, 192, 192));
        Tab1.add(txtDept);
        txtDept.setBounds(190, 230, 255, 19);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Purpose :");
        Tab1.add(jLabel12);
        jLabel12.setBounds(5, 264, 125, 14);

        txtPurpose.setName("PURPOSE");
        txtPurpose.setNextFocusableComponent(txtRecdBy);
        txtPurpose.setEnabled(false);
        Tab1.add(txtPurpose);
        txtPurpose.setBounds(135, 262, 310, 19);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Received By :");
        Tab1.add(jLabel22);
        jLabel22.setBounds(5, 294, 125, 14);

        txtRecdBy.setName("RECEIVED_BY");
        txtRecdBy.setNextFocusableComponent(txtRemarks);
        txtRecdBy.setEnabled(false);
        Tab1.add(txtRecdBy);
        txtRecdBy.setBounds(135, 292, 310, 19);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(240, 31, 25, 15);

        jTabbedPane1.addTab("Header", null, Tab1, "Quotation Header");

        jPanel2.setLayout(null);

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel20.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel20.setText("Declaration Items");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(10, 14, 100, 15);

        jPanel4.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.add(jPanel4);
        jPanel4.setBounds(115, 20, 274, 3);

        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.setEnabled(false);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        jPanel2.add(cmdAdd);
        cmdAdd.setBounds(554, 8, 88, 25);

        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove");
        cmdRemove.setEnabled(false);
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        jPanel2.add(cmdRemove);
        cmdRemove.setBounds(646, 8, 92, 25);

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
        TableL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TableLKeyPressed(evt);
            }
        });

        jScrollPane1.setViewportView(TableL);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(6, 38, 732, 176);

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
        HeaderPane.setViewportView(TableH);

        jPanel2.add(HeaderPane);
        HeaderPane.setBounds(6, 230, 254, 124);

        cmdNext2.setText("Next >>");
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });

        jPanel2.add(cmdNext2);
        cmdNext2.setBounds(659, 331, 90, 25);

        cmdNext4.setLabel("Previous <<");
        cmdNext4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext4ActionPerformed(evt);
            }
        });

        jPanel2.add(cmdNext4);
        cmdNext4.setBounds(539, 331, 120, 25);

        cmdInsert.setText("Insert from PO");
        cmdInsert.setName("PO_NO");
        cmdInsert.setNextFocusableComponent(txtContractor);
        cmdInsert.setEnabled(false);
        cmdInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdInsertActionPerformed(evt);
            }
        });

        jPanel2.add(cmdInsert);
        cmdInsert.setBounds(403, 9, 140, 25);

        jTabbedPane1.addTab("Item Information", null, jPanel2, "Quotation Item");

        jPanel3.setLayout(null);

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(5, 18, 90, 14);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });

        jPanel3.add(cmbHierarchy);
        cmbHierarchy.setBounds(110, 14, 184, 24);

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        jPanel3.add(jLabel32);
        jLabel32.setBounds(5, 52, 90, 14);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFrom);
        txtFrom.setBounds(110, 50, 182, 19);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        jPanel3.add(jLabel35);
        jLabel35.setBounds(5, 82, 90, 14);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFromRemarks);
        txtFromRemarks.setBounds(110, 78, 518, 19);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        jPanel3.add(jLabel36);
        jLabel36.setBounds(5, 124, 90, 14);

        jPanel6.setLayout(null);

        jPanel6.setBorder(new javax.swing.border.EtchedBorder());
        OpgApprove.setText("Approve & Forward");
        buttonGroup1.add(OpgApprove);
        OpgApprove.setEnabled(false);
        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 168, 23);

        OpgFinal.setText("Final Approve");
        buttonGroup1.add(OpgFinal);
        OpgFinal.setEnabled(false);
        OpgFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OpgFinalKeyPressed(evt);
            }
        });
        OpgFinal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgFinalMouseClicked(evt);
            }
        });

        jPanel6.add(OpgFinal);
        OpgFinal.setBounds(6, 32, 136, 20);

        OpgReject.setText("Reject");
        buttonGroup1.add(OpgReject);
        OpgReject.setEnabled(false);
        OpgReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgRejectMouseClicked(evt);
            }
        });

        jPanel6.add(OpgReject);
        OpgReject.setBounds(6, 54, 136, 20);

        OpgHold.setSelected(true);
        OpgHold.setText("Hold Document");
        buttonGroup1.add(OpgHold);
        OpgHold.setEnabled(false);
        jPanel6.add(OpgHold);
        OpgHold.setBounds(6, 76, 136, 20);

        jPanel3.add(jPanel6);
        jPanel6.setBounds(110, 116, 182, 100);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        jPanel3.add(jLabel33);
        jLabel33.setBounds(5, 228, 90, 14);

        cmbSendTo.setEnabled(false);
        jPanel3.add(cmbSendTo);
        cmbSendTo.setBounds(110, 226, 184, 24);

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        jPanel3.add(jLabel34);
        jLabel34.setBounds(5, 264, 90, 14);

        txtToRemarks.setEnabled(false);
        jPanel3.add(txtToRemarks);
        txtToRemarks.setBounds(110, 260, 516, 19);

        cmdNext3.setLabel("Previous <<");
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });

        jPanel3.add(cmdNext3);
        cmdNext3.setBounds(610, 320, 120, 25);

        jTabbedPane1.addTab("Approval", null, jPanel3, "Quotation Approval");

        jPanel5.setLayout(null);

        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel26.setText("Document Approval Status");
        jPanel5.add(jLabel26);
        jLabel26.setBounds(12, 10, 242, 14);

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
        jScrollPane2.setViewportView(TableA);

        jPanel5.add(jScrollPane2);
        jScrollPane2.setBounds(12, 40, 649, 144);

        lblDocumentHistory.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblDocumentHistory.setText("Document Update History");
        jPanel5.add(lblDocumentHistory);
        lblDocumentHistory.setBounds(13, 193, 182, 14);

        TableHS.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(TableHS);

        jPanel5.add(jScrollPane3);
        jScrollPane3.setBounds(13, 209, 508, 129);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });

        jPanel5.add(cmdViewHistory);
        cmdViewHistory.setBounds(530, 238, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });

        jPanel5.add(cmdNormalView);
        cmdNormalView.setBounds(530, 269, 132, 24);

        cmdPreviewA.setText("Preview Report");
        cmdPreviewA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewAActionPerformed(evt);
            }
        });

        jPanel5.add(cmdPreviewA);
        cmdPreviewA.setBounds(530, 208, 132, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });

        jPanel5.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(530, 299, 132, 24);

        txtAuditRemarks.setEnabled(false);
        jPanel5.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(533, 328, 129, 19);

        jTabbedPane1.addTab("Status", jPanel5);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(0, 70, 760, 390);

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("DECLARATION FORM");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 804, 25);

    }//GEN-END:initComponents

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        // TODO add your handling code here:
        OpgReject.setSelected(true);
        OpgFinal.setSelected(false);
        OpgApprove.setSelected(false);
        OpgHold.setSelected(false);
        
        GenerateRejectedUserCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked
    
private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
    // TODO add your handling code here:
    if(TableHS.getRowCount()>0&&TableHS.getSelectedRow()>=0) {
        txtAuditRemarks.setText((String)TableHS.getValueAt(TableHS.getSelectedRow(),4));
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtAuditRemarks;
        bigEdit.ShowEdit();
    }
    
}//GEN-LAST:event_cmdShowRemarksActionPerformed

private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
    // TODO add your handling code here:
    if(!OpgFinal.isEnabled()) {
        OpgHold.setSelected(true);
    }
    
}//GEN-LAST:event_OpgFinalMouseClicked

private void OpgFinalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OpgFinalKeyPressed
    // TODO add your handling code here:
}//GEN-LAST:event_OpgFinalKeyPressed

    private void cmdPreviewAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewAActionPerformed
        // TODO add your handling code here:
        PreviewAuditReport();
    }//GEN-LAST:event_cmdPreviewAActionPerformed
    
    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed
        // TODO add your handling code here:
        PreviewReport();
    }//GEN-LAST:event_cmdPreviewActionPerformed
    
    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjDeclarationForm.LoadData(EITLERPGLOBAL.gCompanyID);
        MoveLast();
    }//GEN-LAST:event_cmdNormalViewActionPerformed
    
    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo=txtDecID.getText();
        ObjDeclarationForm.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveLast();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed
    
    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        // TODO add your handling code here:
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        }
        else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged
    
    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if(TableL.getRowCount()>0) {
            DataModelL.removeRow(TableL.getSelectedRow());
            UpdateSrNo();
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        
        Object[] RowData = new Object[1];
        DataModelL.addRow(RowData);
        UpdateSrNo();
        TableL.changeSelection(TableL.getRowCount()-1,1,false,false);
        TableL.requestFocus();
        
    }//GEN-LAST:event_cmdAddActionPerformed
    
    private void TableLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyPressed
        // TODO add your handling code here:
        
        //======== Lot Entry ============
        /*if(TableL.getSelectedColumn()==DataModelL.getColFromVariable("RECD_QTY")) {
            if(evt.getKeyCode()==112) //F1 Key
            {
                EnterDFLotQty ObjLot=new EnterDFLotQty();
                if (DataModelL.getUserObject(TableL.getSelectedRow()) instanceof HashMap) {
                    ObjLot.colLot=(HashMap)DataModelL.getUserObject(TableL.getSelectedRow());
                }
                else {
                    ObjLot.colLot=new HashMap();
                }
                if(ObjLot.ShowList()) //Ok Pressed
                {
                    DataModelL.SetUserObject(TableL.getSelectedRow(),ObjLot.colLot);
                    //Show Updated Qty.
                    double TotalQty=0;
                    for(int i=1;i<=ObjLot.colLot.size();i++) {
                        clsDeclarationFormItemDetail ObjItemLot=(clsDeclarationFormItemDetail)ObjLot.colLot.get(Integer.toString(i));
                        TotalQty=TotalQty+ObjItemLot.getAttribute("LOT_QTY").getVal();
                    }
                    TableL.setValueAt(Double.toString(TotalQty), TableL.getSelectedRow(), DataModelL.getColFromVariable("RECD_QTY"));
                }
            }
        }*/
        //=========== Lot Entry Complete ============
        
        
        //=========== Item List ===============
        if(TableL.getSelectedColumn()==DataModelL.getColFromVariable("ITEM_CODE")) {
            if(evt.getKeyCode()==112) //F1 Key pressed
            {
                LOV aList=new LOV();
                
                aList.SQL="SELECT ITEM_ID,ITEM_DESCRIPTION FROM D_INV_ITEM_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND APPROVED=1 AND CANCELLED=0 ORDER BY ITEM_ID";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                
                if(aList.ShowLOV()) {
                    if(TableL.getCellEditor()!=null) {
                        TableL.getCellEditor().stopCellEditing();
                    }
                    TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(),DataModelL.getColFromVariable("ITEM_CODE"));
                }
            }
        }
        //=========================================
        
        if(TableL.getSelectedColumn()==DataModelL.getColFromVariable("UNIT_NAME")) {
            if(evt.getKeyCode()==112) //F1 Key pressed
            {
                LOV aList=new LOV();
                
                aList.SQL="SELECT PARA_CODE,D_COM_PARAMETER_MAST.DESC FROM D_COM_PARAMETER_MAST WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARA_ID='UNIT' ORDER BY D_COM_PARAMETER_MAST.DESC";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=2;
                
                if(aList.ShowLOV()) {
                    if(TableL.getCellEditor()!=null) {
                        TableL.getCellEditor().stopCellEditing();
                    }
                    
                    TableL.setValueAt(aList.ReturnVal, TableL.getSelectedRow(),DataModelL.getColFromVariable("UNIT_ID"));
                    clsParameter ObjParameter = new clsParameter();
                    String ParaDesc = ObjParameter.getParaDescription((int)EITLERPGLOBAL.gCompanyID,"UNIT",Integer.parseInt(aList.ReturnVal));
                    TableL.setValueAt(ParaDesc, TableL.getSelectedRow(),DataModelL.getColFromVariable("UNIT_NAME"));
                }
            }
        }
        //=========================================
        //            if(evt.getKeyCode()==155)//Insert Key Pressed
        //            {
        //                Object[] rowData=new Object[1];
        //                DataModelL.addRow(rowData);
        //                DataModelL.SetUserObject(TableL.getRowCount()-1,new HashMap());
        //                TableL.changeSelection(TableL.getRowCount()-1, 1, false,false);
        //                UpdateSrNo();
        //            }
        
        if(EditMode!=0) {
            if(evt.getKeyCode()==67&&evt.getModifiersEx()==128) //Ctrl+C Key Combonation
            {
                DoNotEvaluate=true;
                //Check that any row exist
                if(TableL.getRowCount()>0) {
                    //First Add new row
                    Object[] rowData=new Object[1];
                    DataModelL.addRow(rowData);
                    int NewRow=TableL.getRowCount()-1;
                    
                    //Copy New row with Previous one
                    for(int i=0;i<TableL.getColumnCount();i++) {
                        TableL.setValueAt(TableL.getValueAt(TableL.getSelectedRow(),i), NewRow, i);
                    }
                    UpdateSrNo();
                }
                DoNotEvaluate=false;
            }
        }
        
    }//GEN-LAST:event_TableLKeyPressed
    
    private void cmdInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInsertActionPerformed
        // TODO add your handling code here:
        SelectPO2 ObjPO=new SelectPO2();
        
        if(ObjPO.ShowList()) {
            DoNotEvaluate=true;
            // int pCompanyID,String pPONo,String pURL,int pType)
            if(ObjPO.colSelItems.size()>0) {
                clsPOItem ObjItem=(clsPOItem)ObjPO.colSelItems.get(Integer.toString(1));
                
                clsPOGen ObjPO1=new clsPOGen();
                
                int POType=(int)ObjItem.getAttribute("PO_TYPE").getVal();
                clsPOGen ObjPO2=(clsPOGen)ObjPO1.getObject(EITLERPGLOBAL.gCompanyID,(String)ObjItem.getAttribute("PO_NO").getObj(),EITLERPGLOBAL.DatabaseURL,POType);
                
                String PONo=(String)ObjItem.getAttribute("PO_NO").getObj();
                txtPONO.setText((String)PONo);
                String PODate=EITLERPGLOBAL.formatDate((String)ObjPO2.getAttribute("PO_DATE").getObj());
                
                txtPODate.setText(PODate);
            }
            
            //It will contain PO Item Objects
            for(int i=1;i<=ObjPO.colSelItems.size();i++) {
                clsPOItem ObjItem=(clsPOItem)ObjPO.colSelItems.get(Integer.toString(i));
                
                //Add Blank Row
                Object[] rowData=new Object[1];
                DataModelL.addRow(rowData);
                int NewRow=TableL.getRowCount()-1;
                
                DataModelL.setValueByVariable("SR_NO", Integer.toString((int)ObjItem.getAttribute("SR_NO").getVal()),NewRow);
                DataModelL.setValueByVariable("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_ID").getObj(),NewRow);
                String ItemName=clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String)ObjItem.getAttribute("ITEM_ID").getObj());
                DataModelL.setValueByVariable("ITEM_NAME",ItemName,NewRow);
                DataModelL.setValueByVariable("RECD_QTY",Double.toString(ObjItem.getAttribute("QTY").getVal()),NewRow);
                DataModelL.setValueByVariable("UNIT_ID",Integer.toString((int)ObjItem.getAttribute("UNIT").getVal()),NewRow);
                String UnitName=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("UNIT_NAME",UnitName,NewRow);
                //This Change
                //----------------------------------------------------------------------//
                DataModelL.setValueByVariable("RETURNABLE",Boolean.valueOf(false),NewRow);
                //DataModelL.setValueByVariable("RETURNED",Boolean.valueOf(false),NewRow);
                //---------------------------------------------------------------------//
                // UpdateResults(DataModelL.getColFromVariable("QTY"));
            }
            
            UpdateSrNo();
            DoNotEvaluate=false;
        }
    }//GEN-LAST:event_cmdInsertActionPerformed
    
    
    
    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext3ActionPerformed
    
    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext2ActionPerformed
    
    private void cmdNext4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext4ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_cmdNext4ActionPerformed
    
    private void txtDeptIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDeptIDFocusLost
        // TODO add your handling code here:
        if(!txtDeptID.getText().trim().equals("")) {
            txtDept.setText(clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, txtDeptID.getText()));
        }
    }//GEN-LAST:event_txtDeptIDFocusLost
    
    private void txtDeptIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDeptIDKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT DEPT_ID,DEPT_DESC FROM D_COM_DEPT_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" ORDER BY DEPT_DESC";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            
            if(aList.ShowLOV()) {
                txtDeptID.setText(aList.ReturnVal);
                txtDept.setText(clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID,aList.ReturnVal));
            }
        }
        //=========================================
        
    }//GEN-LAST:event_txtDeptIDKeyPressed
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        ObjColumn.Close();
        ObjDeclarationForm.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
    
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
    
    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed
    
    private void Add() {
        
        //---Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        
        //Now Generate new document no.
        SelectFirstFree aList=new SelectFirstFree();
        aList.ModuleID=13;
        
        if(aList.ShowList()) {
            EditMode=EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SelPrefix=aList.Prefix; //Selected Prefix;
            SelSuffix=aList.Suffix;
            FFNo=aList.FirstFreeNo;
            SetupApproval();
            //Display newly generated document no.
            txtDecID.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID,13, FFNo,  false));
            txtDate.setText(EITLERPGLOBAL.getCurrentDate());
            
            lblTitle.setText("DECLARATION FORM - "+txtDecID.getText());
            lblTitle.setBackground(Color.BLUE);
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. \n If no prefixes found in the list, Please do entry in First Free Nos.");
        }
        
    }
    
    private void SetupApproval() {
        // --- Hierarchy Change Rights Check --------
        /*if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,595)) {
            cmbHierarchy.setEnabled(true);
        }
        else {
            cmbHierarchy.setEnabled(false);
        }*/
        if(EditMode==EITLERPGLOBAL.ADD) {
            cmbHierarchy.setEnabled(true);
        }
        else {
            cmbHierarchy.setEnabled(false);
        }
        
        //Set Default Hierarchy ID for User
        int DefaultID=clsHierarchy.getDefaultHierarchy((int)EITLERPGLOBAL.gCompanyID);
        EITLERPGLOBAL.setComboIndex(cmbHierarchy,DefaultID);
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            lnFromID=(int)EITLERPGLOBAL.gNewUserID;
            txtFrom.setText(clsUser.getUserName(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID));
            txtFromRemarks.setText("Creator of Document");
        }
        else {
            
            int FromUserID=ApprovalFlow.getFromID((int)EITLERPGLOBAL.gCompanyID, 13,(String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=ApprovalFlow.getFromRemarks( (int)EITLERPGLOBAL.gCompanyID,13,FromUserID,(String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj());
            
            txtFrom.setText(strFromUser);
            txtFromRemarks.setText(strFromRemarks);
        }
        
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        GenerateFromCombo();
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gNewUserID)) {
            OpgFinal.setEnabled(true);
        }
        else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
        
        
        //In Edit Mode Hierarchy Should be disabled
        if(EditMode==EITLERPGLOBAL.EDIT) {
            cmbHierarchy.setEnabled(false);
        }
        
        if(EditMode==0) {
            //Disable all hierarchy controls if not in Add/Edit Mode
            cmbHierarchy.setEnabled(false);
            txtFrom.setEnabled(false);
            txtFromRemarks.setEnabled(false);
            OpgApprove.setEnabled(false);
            OpgFinal.setEnabled(false);
            OpgReject.setEnabled(false);
            cmbSendTo.setEnabled(false);
            txtToRemarks.setEnabled(false);
        }
    }
    
    private void GenerateFromCombo() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        
        try {
            if(EditMode==EITLERPGLOBAL.ADD) {
                //----- Generate cmbType ------- //
                cmbToModel=new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);
                
                List=clsHierarchy.getUserList((int)EITLERPGLOBAL.gCompanyID,SelHierarchyID,EITLERPGLOBAL.gNewUserID);
                for(int i=1;i<=List.size();i++) {
                    clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
                    ComboData aData=new ComboData();
                    aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
                    
                    if(ObjUser.getAttribute("USER_ID").getVal()==EITLERPGLOBAL.gNewUserID) {
                        //Exclude Current User
                    }
                    else {
                        cmbToModel.addElement(aData);
                    }
                }
                //------------------------------ //
            }
            else {
                //----- Generate cmbType ------- //
                cmbToModel=new EITLComboModel();
                cmbSendTo.removeAllItems();
                cmbSendTo.setModel(cmbToModel);
                
                List=ApprovalFlow.getRemainingUsers((int)EITLERPGLOBAL.gCompanyID, 13,(String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj());
                for(int i=1;i<=List.size();i++) {
                    clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
                    ComboData aData=new ComboData();
                    aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
                    aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
                    cmbToModel.addElement(aData);
                }
                //------------------------------ //
            }
        }
        catch(Exception e)
        {}
        
    }
    
    private void GenerateRejectedUserCombo() {
        HashMap List=new HashMap();
        HashMap DeptList=new HashMap();
        HashMap DeptUsers=new HashMap();
        
        //----- Generate cmbType ------- //
        cmbToModel=new EITLComboModel();
        cmbSendTo.removeAllItems();
        cmbSendTo.setModel(cmbToModel);
        
        
        //Now Add other hierarchy Users
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        
        List=clsHierarchy.getUserList((int)EITLERPGLOBAL.gCompanyID,SelHierarchyID,EITLERPGLOBAL.gNewUserID,true);
        for(int i=1;i<=List.size();i++) {
            clsUser ObjUser=(clsUser) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjUser.getAttribute("USER_ID").getVal();
            aData.Text=(String)ObjUser.getAttribute("USER_NAME").getObj();
            
            
            /// NEW CODE ///
            boolean IncludeUser=false;
            //Decide to include user or not
            if(EditMode==EITLERPGLOBAL.EDIT) {
                if(OpgApprove.isSelected()) {
                    IncludeUser=ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, clsDeclarationForm.ModuleID, txtDecID.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    IncludeUser=ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, clsDeclarationForm.ModuleID , txtDecID.getText(), (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(IncludeUser&&(((int) ObjUser.getAttribute("USER_ID").getVal())!=EITLERPGLOBAL.gNewUserID)) {
                    cmbToModel.addElement(aData);
                }
            }
            else {
                if(((int) ObjUser.getAttribute("USER_ID").getVal())!=EITLERPGLOBAL.gNewUserID) {
                    cmbToModel.addElement(aData);
                }
            }
            /// END NEW CODE ///
        }
        //------------------------------ //
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            int Creator=ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, clsDeclarationForm.ModuleID,txtDecID.getText());
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
        
    }
    
    private void SetFields(boolean pStat) {
        txtDate.setEnabled(pStat);
        txtContractor.setEnabled(pStat);
        txtPONO.setEnabled(pStat);
        txtAdd1.setEnabled(pStat);
        txtAdd2.setEnabled(pStat);
        txtAdd3.setEnabled(pStat);
        txtCity.setEnabled(pStat);
        txtPIN.setEnabled(pStat);
        txtDeptID.setEnabled(pStat);
        txtPurpose.setEnabled(pStat);
        txtRecdBy.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        chkCancelled.setEnabled(pStat);
        
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
        txtToRemarks.setEnabled(pStat);
        
        cmdInsert.setEnabled(pStat);
        cmdAdd.setEnabled(pStat);
        cmdRemove.setEnabled(pStat);
        
        SetupApproval();
        
        DataModelH.TableReadOnly(!pStat);
        DataModelL.TableReadOnly(!pStat);
    }
    
    private boolean Validate() {
        int ValidEntryCount=0;
        
        //Validates Item Entries
        if(TableL.getRowCount()<=0) {
            JOptionPane.showMessageDialog(null,"Please enter at least one item");
            return false;
        }
        
        if(txtContractor.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter contractor name");
            return false;
        }
        
        if(!EITLERPGLOBAL.IsNumber(txtDeptID.getText())) {
            txtDeptID.setText("0");
        }
        
        
        //Search in Table
        int ItemCol=DataModelL.getColFromVariable("ITEM_CODE");
        int QtyCol=DataModelL.getColFromVariable("RECD_QTY");
        
        for(int i=0;i<TableL.getRowCount();i++) {
            String ItemID="";
            double Qty=0;
            
            if(TableL.getValueAt(i, ItemCol)!=null && TableL.getValueAt(i, QtyCol)!=null) {
                ItemID=(String)TableL.getValueAt(i, ItemCol);
                Qty=Double.parseDouble((String)TableL.getValueAt(i,QtyCol));
                String theDesc=DataModelL.getValueByVariable("DECLARATION_DESC", i);
                
                if(Qty>0&&!theDesc.trim().equals("")) {
                    ValidEntryCount++;
                }
                else {
                    JOptionPane.showMessageDialog(null,"Item entry is not valid. Please be sure to enter following information. \nItem Description,Quantity");
                    TableL.changeSelection(i, 1, false,false);
                    return false;
                }
                
            }
        }
        if(ValidEntryCount==0) {
            JOptionPane.showMessageDialog(null,"Item entry is not valid. Please verify");
            return false;
        }
        
        //Now Header level validations
        if(txtDate.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Declaraton Date");
            return false;
        }
        
        if(cmbHierarchy.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(null,"Please select the hierarchy.");
            return false;
        }
        
        if((!OpgApprove.isSelected())&&(!OpgReject.isSelected())&&(!OpgFinal.isSelected())&&(!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null,"Please select the Approval Action");
            return false;
        }
        
        if(!txtDeptID.getText().trim().equals("")) {
            int DeptId = Integer.parseInt(txtDeptID.getText());
            if(clsDepartment.IsValidDeptCode(EITLERPGLOBAL.gCompanyID,DeptId)==false) {
                JOptionPane.showMessageDialog(null,"Please enter valid department code");
                return false;
            }
        }
        
        if(!EITLERPGLOBAL.isDate(txtDate.getText())) {
            JOptionPane.showMessageDialog(null,"Invalid Declaration Date");
            return false;
        }
        
        if(!txtPODate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtPODate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid PO Date");
                return false;
            }
        }
        
        if(OpgReject.isSelected()&&txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter the remarks for rejection");
            return false;
        }
        
        //        if( (OpgApprove.isSelected()||OpgReject.isSelected())&&cmbSendTo.getItemCount()<=0)
        //        {
        //          JOptionPane.showMessageDialog(null,"Please select the user, to whom rejected document to be send");
        //          return false;
        //        }
        
        
        return true;
    }
    
    private void ClearFields() {
        txtDecID.setText("");
        txtDate.setText("");
        txtContractor.setText("");
        txtPONO.setText("");
        txtPODate.setText("");
        txtAdd1.setText("");
        txtAdd2.setText("");
        txtAdd3.setText("");
        txtCity.setText("");
        txtPIN.setText("");
        txtDeptID.setText("");
        txtDept.setText("");
        txtPurpose.setText("");
        txtRemarks.setText("");
        txtToRemarks.setText("");
        txtRecdBy.setText("");
        txtToRemarks.setText("");
        chkCancelled.setSelected(false);
        
        FormatGrid();
        FormatGrid_H();
        FormatGridA();
        FormatGridHS();
    }
    
    private void FormatGrid_H() {
        
        DataModelH=new EITLTableModel();
        
        EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
        
        TableH.removeAll();
        TableH.setModel(DataModelH);
        
        Renderer.setColor(0, 0, Color.LIGHT_GRAY);
        
        //Set the table Readonly
        DataModelH.TableReadOnly(false);
        DataModelH.SetReadOnly(0);
        
        //Add Default Columns
        DataModelH.addColumn("Column");
        DataModelH.addColumn("Value");
        
        TableH.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        SetupColumns_H();
        
        TableColumnModel ColModel=TableH.getColumnModel();
        TableH.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        //------- Install Table List Selection Listener ------//
        TableH.getColumnModel().getSelectionModel().addListSelectionListener(
        new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int last=TableH.getSelectedColumn();
                String strVar=DataModelH.getVariable(last);
                
                
                //=============== Cell Editing Routine =======================//
                try {
                    cellLastValueH=(String)TableH.getValueAt(TableH.getSelectedRow(),TableH.getSelectedColumn());
                    
                    TableH.editCellAt(TableH.getSelectedRow(),TableH.getSelectedColumn());
                    if(TableH.getEditorComponent() instanceof JTextComponent) {
                        ((JTextComponent)TableH.getEditorComponent()).selectAll();
                    }
                }
                catch(Exception cell){}
                //============= Cell Editing Routine Ended =================//
                
                
            }
        }
        );
        //===================================================//
        
        
        //----- Install Table Model Event Listener -------//
        TableH.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    //=========== Cell Update Prevention Check ===========//
                    String curValue=(String)TableH.getValueAt(TableH.getSelectedRow(), e.getColumn());
                    if(curValue.equals(cellLastValueH)) {
                        return;
                    }
                    //====================================================//
                    
                    
                    int col = e.getColumn();
                    int row=e.getLastRow();
                    /*if(!Updating_H)
                    {UpdateResults_H(row);}*/
                }
            }
        });
    }
    
    private void SetupColumns_H() {
        HashMap List=new HashMap();
        Object[] rowData;
        
        List=clsColumn.getList(" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" AND MODULE_ID=13 AND HEADER_LINE='H' ORDER BY COL_ORDER");
        TableColumnModel ColModel=TableH.getColumnModel();
        
        TableH.removeAll();
        
        if(List.size()<=0) {
            HeaderPane.setVisible(false);
        }
    }
    
    private void Edit() {
        
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        String lDocNo=(String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj();
        if(ObjDeclarationForm.IsEditable(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            GenerateCombos();
            DisplayData();
            //----------------//
            
            if(ApprovalFlow.IsCreator(13,lDocNo)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,592)) {
                SetFields(true);
            }
            else {
                EnableApproval();
            }
            
            DisableToolbar();
        }
        else {
            JOptionPane.showMessageDialog(null,"You cannot edit this record. \n It is either approved/rejected or waiting approval for other user");
        }
    }
    
    private void Delete() {
        
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        
        String lDocNo=(String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj();
        
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if(ObjDeclarationForm.CanDelete(EITLERPGLOBAL.gCompanyID, lDocNo, EITLERPGLOBAL.gNewUserID)) {
                if(ObjDeclarationForm.Delete()) {
                    MoveLast();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is "+ObjDeclarationForm.LastError);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"You cannot delete this record. \n It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }
    
    private void Save() {
        //Form level validations
        if(Validate()==false) {
            return; //Validation failed
        }
        
        SetData();
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjDeclarationForm.Insert(SelPrefix)) {
                MoveLast();
                DisplayData();
            } else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjDeclarationForm.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            if(ObjDeclarationForm.Update()) {
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjDeclarationForm.LastError);
                return;
            }
        }
        
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
        
        try {
            frmPA.RefreshView();
        }catch(Exception e){}
        
    }
    
    private void Cancel() {
        DisplayData();
        EditMode=0;
        SetFields(false);
        EnableToolbar();
        SetMenuForRights();
    }
    
    private void Find() {
        Loader ObjLoader=new Loader(this,"EITLERP.Stores.frmDeclarationFind",true);
        frmDeclarationFind ObjReturn= (frmDeclarationFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjDeclarationForm.Filter(ObjReturn.strQuery,EITLERPGLOBAL.gCompanyID)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveLast();
        }
    }
    
    public void FindEx(int pCompanyID,String pDocNo) {
        ObjDeclarationForm.Filter(" WHERE COMPANY_ID="+Integer.toString(pCompanyID)+" AND DECLARATION_ID='"+pDocNo+"'",pCompanyID);
        ObjDeclarationForm.MoveLast();
        DisplayData();
    }
    
    
    public void FindWaiting() {
        ObjDeclarationForm.Filter(" WHERE DECLARATION_ID IN(SELECT D_INV_DECLARATION_HEADER.DECLARATION_ID FROM D_INV_DECLARATION_ID,D_COM_DOC_DATA WHERE D_INV_DECLARATION_HEADER.DECLARATION_ID=D_COM_DOC_DATA.DOC_NO AND D_INV_DECLARATION_HEADER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_INV_DECLARATION_HEADER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND D_COM_DOC_DATA.MODULE_ID=39)",EITLERPGLOBAL.gCompanyID);
        ObjDeclarationForm.MoveLast();
        DisplayData();
    }
    
    private void MoveFirst() {
        ObjDeclarationForm.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjDeclarationForm.MovePrevious();
        DisplayData();
    }
    
    
    private void MoveNext() {
        ObjDeclarationForm.MoveNext();
        DisplayData();
    }
    
    
    private void MoveLast() {
        ObjDeclarationForm.MoveLast();
        DisplayData();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane HeaderPane;
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel Tab1;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableH;
    private javax.swing.JTable TableHS;
    private javax.swing.JTable TableL;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkCancelled;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdInsert;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNext4;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreviewA;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblDocumentHistory;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAdd1;
    private javax.swing.JTextField txtAdd2;
    private javax.swing.JTextField txtAdd3;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtContractor;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDecID;
    private javax.swing.JTextField txtDept;
    private javax.swing.JTextField txtDeptID;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtPIN;
    private javax.swing.JTextField txtPODate;
    private javax.swing.JTextField txtPONO;
    private javax.swing.JTextField txtPurpose;
    private javax.swing.JTextField txtRecdBy;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtToRemarks;
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
        
        //=========== Color Indication ===============//
        try {
            if(EditMode==0) {
                if(ObjDeclarationForm.getAttribute("APPROVED").getInt()==1) {
                    lblTitle.setBackground(Color.BLUE);
                }
                
                if(ObjDeclarationForm.getAttribute("APPROVED").getInt()!=1) {
                    lblTitle.setBackground(Color.GRAY);
                }
                
                if(ObjDeclarationForm.getAttribute("CANCELLED").getInt()==1) {
                    lblTitle.setBackground(Color.RED);
                }
                
                
            }
        }
        catch(Exception c) {
            
        }
        //============================================//
        
        
        //========= Authority Delegation Check =====================//
        if(EITLERPGLOBAL.gAuthorityUserID!=EITLERPGLOBAL.gUserID) {
            int ModuleID=13;
            
            if(clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gUserID,EITLERPGLOBAL.gAuthorityUserID,ModuleID)) {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gAuthorityUserID;
            }
            else {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gUserID;
            }
        }
        //==========================================================//
        
        
        try {
            ClearFields();
            txtDecID.setText((String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj());
            lblTitle.setText("DECLARATION FORM - "+txtDecID.getText());
            lblRevNo.setText(Integer.toString((int)ObjDeclarationForm.getAttribute("REVISION_NO").getVal()));
            txtDate.setText(EITLERPGLOBAL.formatDate((String)ObjDeclarationForm.getAttribute("DECLARATION_DATE").getObj()));
            txtPONO.setText((String)ObjDeclarationForm.getAttribute("PO_NO").getObj());
            txtPODate.setText(EITLERPGLOBAL.formatDate((String)ObjDeclarationForm.getAttribute("PO_DATE").getObj()));
            txtContractor.setText((String)ObjDeclarationForm.getAttribute("CONTRACTOR_NAME").getObj());
            txtAdd1.setText((String)ObjDeclarationForm.getAttribute("ADD1").getObj());
            txtAdd2.setText((String)ObjDeclarationForm.getAttribute("ADD2").getObj());
            txtAdd3.setText((String)ObjDeclarationForm.getAttribute("ADD3").getObj());
            txtCity.setText((String)ObjDeclarationForm.getAttribute("CITY").getObj());
            txtPIN.setText((String)ObjDeclarationForm.getAttribute("PINCODE").getObj());
            txtDeptID.setText(Integer.toString((int)ObjDeclarationForm.getAttribute("FOR_DEPT_ID").getVal()));
            txtDept.setText(clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID,txtDeptID.getText()));
            txtPurpose.setText((String)ObjDeclarationForm.getAttribute("PURPOSE").getObj());
            txtRecdBy.setText((String)ObjDeclarationForm.getAttribute("RECEIVED_BY").getObj());
            txtRemarks.setText((String)ObjDeclarationForm.getAttribute("REMARKS").getObj());
            chkCancelled.setSelected( ObjDeclarationForm.getAttribute("CANCELLED").getBool() );
            
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,(int)ObjDeclarationForm.getAttribute("HIERARCHY_ID").getVal());
            
            //========= Display Line Items =============//
            FormatGrid();
            
            DoNotEvaluate=true;
            
            for(int i=1;i<=ObjDeclarationForm.colDeclarationFormItems.size();i++) {
                //Insert New Row
                Object[] rowData=new Object[1];
                DataModelL.addRow(rowData);
                int NewRow=TableL.getRowCount()-1;
                
                clsDeclarationFormItem ObjItem=(clsDeclarationFormItem)ObjDeclarationForm.colDeclarationFormItems.get(Integer.toString(i));
                
                //Set the User Object - Lot Nos.
                DataModelL.SetUserObject(NewRow, ObjItem.colItemLot);
                
                DataModelL.setValueByVariable("SR_NO",Integer.toString(i),NewRow);
                DataModelL.setValueByVariable("ITEM_CODE",(String)ObjItem.getAttribute("ITEM_CODE").getObj(),NewRow);
                String ItemName=clsItem.getItemName(EITLERPGLOBAL.gCompanyID, (String)ObjItem.getAttribute("ITEM_CODE").getObj());
                DataModelL.setValueByVariable("ITEM_NAME",ItemName,NewRow);
                DataModelL.setValueByVariable("DECLARATION_DESC",(String)ObjItem.getAttribute("DECLARATION_DESC").getObj(),NewRow);
                DataModelL.setValueByVariable("RECD_QTY",Double.toString(ObjItem.getAttribute("RECD_QTY").getVal()),NewRow);
                DataModelL.setValueByVariable("UNIT_ID",Integer.toString((int)ObjItem.getAttribute("UNIT").getVal()),NewRow);
                String UnitName=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID,"UNIT",(int)ObjItem.getAttribute("UNIT").getVal());
                DataModelL.setValueByVariable("UNIT_NAME",UnitName,NewRow);
                DataModelL.setValueByVariable("BAL_QTY",Double.toString(ObjItem.getAttribute("BAL_QTY").getVal()),NewRow);
                //This Change
                //--------------------------------------------------------------------------------------------------------------//
                DataModelL.setValueByVariable("RETURNABLE",Boolean.valueOf(ObjItem.getAttribute("RETURNED").getBool()),NewRow);
                // DataModelL.setValueByVariable("RETURNED",Boolean.valueOf(ObjItem.getAttribute("RETURNED").getBool()),NewRow);
                //-------------------------------------------------------------------------------------------------------------//
                
                DataModelL.setValueByVariable("EXP_RETURN_DATE",EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("EXP_RETURN_DATE").getObj()),NewRow);
                DataModelL.setValueByVariable("RETURNED_DATE",EITLERPGLOBAL.formatDate((String)ObjItem.getAttribute("RETURNED_DATE").getObj()),NewRow);
                DataModelL.setValueByVariable("REMARKS",(String)ObjItem.getAttribute("REMARKS").getObj(),NewRow);
                
            }
            
            DoNotEvaluate=false;
            
            UpdateSrNo();
            
            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List=new HashMap();
            String DocNo=(String)ObjDeclarationForm.getAttribute("DECLARATION_ID").getObj();
            List=ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, 13, DocNo);
            for(int i=1;i<=List.size();i++) {
                clsDocFlow ObjFlow=(clsDocFlow)List.get(Integer.toString(i));
                Object[] rowData=new Object[7];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(int)ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2]=(String)ObjFlow.getAttribute("STATUS").getObj();
                rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int)ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6]=(String)ObjFlow.getAttribute("REMARKS").getObj();
                
                DataModelA.addRow(rowData);
            }
            //============================================================//
            
            
            //Showing Audit Trial History
            FormatGridHS();
            HashMap History=clsDeclarationForm.getHistoryList(EITLERPGLOBAL.gCompanyID, DocNo);
            for(int i=1;i<=History.size();i++) {
                clsDeclarationForm ObjHistory=(clsDeclarationForm)History.get(Integer.toString(i));
                Object[] rowData=new Object[5];
                
                rowData[0]=Integer.toString((int)ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(long)ObjHistory.getAttribute("UPDATED_BY").getVal());
                rowData[2]=EITLERPGLOBAL.formatDate((String)ObjHistory.getAttribute("ENTRY_DATE").getObj());
                
                String ApprovalStatus="";
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("A")) {
                    ApprovalStatus="Approved";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("F")) {
                    ApprovalStatus="Final Approved";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("W")) {
                    ApprovalStatus="Waiting";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("R")) {
                    ApprovalStatus="Rejected";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("P")) {
                    ApprovalStatus="Pending";
                }
                
                if(((String)ObjHistory.getAttribute("APPROVAL_STATUS").getObj()).equals("C")) {
                    ApprovalStatus="Skiped";
                }
                
                
                rowData[3]=ApprovalStatus;
                rowData[4]=(String)ObjHistory.getAttribute("APPROVER_REMARKS").getObj();
                
                DataModelHS.addRow(rowData);
            }
            
            if(EditMode==0) {
                DataModelL.TableReadOnly(true);
                DataModelH.TableReadOnly(true);
            }
            //=========================================//
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Display Data Error: " + e.getMessage());
        }
    }
    
    private void UpdateSrNo() {
        int SrCol=DataModelL.getColFromVariable("SR_NO");
        
        for(int i=0;i<TableL.getRowCount();i++) {
            TableL.setValueAt(Integer.toString(i+1), i, SrCol);
        }
    }
    
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjDeclarationForm.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjDeclarationForm.setAttribute("PREFIX",SelPrefix);
        ObjDeclarationForm.setAttribute("SUFFIX",SelSuffix);
        ObjDeclarationForm.setAttribute("FFNO",FFNo);
        ObjDeclarationForm.setAttribute("DECLARATION_DATE",EITLERPGLOBAL.formatDateDB(txtDate.getText()));
        ObjDeclarationForm.setAttribute("CONTRACTOR_NAME",txtContractor.getText());
        ObjDeclarationForm.setAttribute("PO_NO",txtPONO.getText());
        ObjDeclarationForm.setAttribute("PO_DATE",EITLERPGLOBAL.formatDateDB(txtPODate.getText()));
        ObjDeclarationForm.setAttribute("ADD1",txtAdd1.getText());
        ObjDeclarationForm.setAttribute("ADD2",txtAdd2.getText());
        ObjDeclarationForm.setAttribute("ADD3",txtAdd3.getText());
        ObjDeclarationForm.setAttribute("CITY",txtCity.getText());
        ObjDeclarationForm.setAttribute("PINCODE",txtPIN.getText());
        ObjDeclarationForm.setAttribute("FOR_DEPT_ID",Integer.parseInt(txtDeptID.getText()));
        ObjDeclarationForm.setAttribute("PURPOSE",txtPurpose.getText());
        ObjDeclarationForm.setAttribute("RECEIVED_BY",txtRecdBy.getText());
        
        if(chkCancelled.isSelected()) {
            ObjDeclarationForm.setAttribute("CANCELLED",true);
        }
        else {
            ObjDeclarationForm.setAttribute("CANCELLED",false);
        }
        
        ObjDeclarationForm.setAttribute("REMARKS",txtRemarks.getText());
        
        //----- Update Approval Specific Fields -----------//
        ObjDeclarationForm.setAttribute("HIERARCHY_ID",EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjDeclarationForm.setAttribute("FROM",EITLERPGLOBAL.gNewUserID);
        ObjDeclarationForm.setAttribute("TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjDeclarationForm.setAttribute("FROM_REMARKS",txtToRemarks.getText());
        
        if(OpgApprove.isSelected()) {
            ObjDeclarationForm.setAttribute("APPROVAL_STATUS","A");
        }
        
        if(OpgFinal.isSelected()) {
            ObjDeclarationForm.setAttribute("APPROVAL_STATUS","F");
        }
        
        if(OpgReject.isSelected()) {
            ObjDeclarationForm.setAttribute("APPROVAL_STATUS","R");
        }
        
        if(OpgHold.isSelected()) {
            ObjDeclarationForm.setAttribute("APPROVAL_STATUS","H");
        }
        //-------------------------------------------------//
        
        if(EditMode==EITLERPGLOBAL.ADD) {
            ObjDeclarationForm.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            ObjDeclarationForm.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
            ObjDeclarationForm.setAttribute("MODIFIED_BY","");
            ObjDeclarationForm.setAttribute("MODIFIED_DATE","0000-00-00");
        } else {
            ObjDeclarationForm.setAttribute("CREATED_BY","");
            ObjDeclarationForm.setAttribute("CREATED_DATE","0000-00-00");
            ObjDeclarationForm.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            ObjDeclarationForm.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        
        //=================== Setting up Line Items ==================//
        
        ObjDeclarationForm.colDeclarationFormItems.clear();
        
        for(int i=0;i<TableL.getRowCount();i++) {
            clsDeclarationFormItem ObjItem=new clsDeclarationFormItem();
            
            ObjItem.setAttribute("SR_NO",DataModelL.getValueByVariable("SR_NO",i));
            ObjItem.setAttribute("ITEM_CODE",DataModelL.getValueByVariable("ITEM_CODE",i));
            if (DataModelL.getValueByVariable("DECLARATION_DESC",i)=="0") {
                ObjItem.setAttribute("DECLARATION_DESC","");
            }
            else {
                ObjItem.setAttribute("DECLARATION_DESC",DataModelL.getValueByVariable("DECLARATION_DESC",i));
            }
            ObjItem.setAttribute("UNIT",Integer.parseInt(DataModelL.getValueByVariable("UNIT_ID",i)));
            ObjItem.setAttribute("RECD_QTY",Double.parseDouble(DataModelL.getValueByVariable("RECD_QTY",i)));
            ObjItem.setAttribute("BAL_QTY",Double.parseDouble(DataModelL.getValueByVariable("BAL_QTY",i)));
            //This Change//
            //-------------------------------------------------------------------------------//
            ObjItem.setAttribute("CONSUMED_QTY","0");
            ObjItem.setAttribute("RETURNED_QTY","0");
            ObjItem.setAttribute("RETURNED",false);
            //--------------------------------------------------------------------------------//
            ObjItem.setAttribute("EXP_RETURN_DATE","0000-00-00");
            ObjItem.setAttribute("RETURNED_DATE","0000-00-00");
            ObjItem.setAttribute("REMARKS",DataModelL.getValueByVariable("REMARKS",i));
            
            HashMap lcolLot = new HashMap();
            //======= Insert Lot Nos. ======//
            if(DataModelL.getUserObject(i) instanceof HashMap) {
                lcolLot=(HashMap)DataModelL.getUserObject(i);
            }
            // else
            // {
            // HashMap lcolLot = new HashMap();
            //   }
            
            for(int l=1;l<=lcolLot.size();l++) {
                clsDeclarationFormItemDetail ObjLot=new clsDeclarationFormItemDetail();
                
                clsDeclarationFormItemDetail ObjList=(clsDeclarationFormItemDetail)lcolLot.get(Integer.toString(l));
                
                ObjLot.setAttribute("SRNO",l);
                ObjLot.setAttribute("ITEM_ID",(String)ObjItem.getAttribute("ITEM_CODE").getObj());
                ObjLot.setAttribute("LOT_NO",(String)ObjList.getAttribute("LOT_NO").getObj());
                ObjLot.setAttribute("LOT_QTY",ObjList.getAttribute("LOT_QTY").getVal());
                ObjLot.setAttribute("TOTAL_ISSUED_QTY",ObjList.getAttribute("TOTAL_ISSUED_QTY").getVal());
                ObjLot.setAttribute("BAL_QTY",ObjList.getAttribute("BAL_QTY").getVal());
                ObjItem.colItemLot.put(Integer.toString(ObjItem.colItemLot.size()+1),ObjLot);
            }
            
            //======== Lot Insertion Completed =========//
            
            ObjDeclarationForm.colDeclarationFormItems.put(Integer.toString(ObjDeclarationForm.colDeclarationFormItems.size()+1),ObjItem);
        }
        //======================Completed ===========================//
    }
    
    
    private void FormatGridA() {
        DataModelA=new EITLTableModel();
        
        TableA.removeAll();
        TableA.setModel(DataModelA);
        
        //Set the table Readonly
        DataModelA.TableReadOnly(true);
        
        //Add the columns
        DataModelA.addColumn("Sr.");
        DataModelA.addColumn("User");
        DataModelA.addColumn("Status");
        DataModelA.addColumn("Department");
        DataModelA.addColumn("Received Date");
        DataModelA.addColumn("Action Date");
        DataModelA.addColumn("Remarks");
        
        TableA.setAutoResizeMode(TableA.AUTO_RESIZE_OFF);
        TableA.getColumnModel().getColumn(0).setCellRenderer(Paint);
        Paint.setColor(1,1,Color.CYAN);
        
    }
    
    private void EnableApproval() {
        cmbSendTo.setEnabled(true);
        OpgApprove.setEnabled(true);
        OpgFinal.setEnabled(true);
        OpgReject.setEnabled(true);
        OpgHold.setEnabled(true);
        txtToRemarks.setEnabled(true);
        SetupApproval();
        
        //========== Setting Up Header Fields ================//
        String FieldName="";
        int SelHierarchy=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        
        for(int i=0;i<Tab1.getComponentCount()-1;i++) {
            if(Tab1.getComponent(i).getName()!=null) {
                
                FieldName=Tab1.getComponent(i).getName();
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    
                    Tab1.getComponent(i).setEnabled(true);
                }
                
            }
        }
        //=============== Header Fields Setup Complete =================//
        
        
        
        //=============== Setting Table Fields ==================//
        DataModelL.ClearAllReadOnly();
        for(int i=0;i<TableL.getColumnCount();i++) {
            FieldName=DataModelL.getVariable(i);
            
            if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "L", FieldName)) {
                //Do Nothing
            }
            else {
                DataModelL.SetReadOnly(i);
            }
        }
        //=======================================================//
        
    }
    
    private void FormatGridHS() {
        DataModelHS=new EITLTableModel();
        
        TableHS.removeAll();
        TableHS.setModel(DataModelHS);
        
        //Set the table Readonly
        DataModelHS.TableReadOnly(true);
        
        //Add the columns
        DataModelHS.addColumn("Rev No.");
        DataModelHS.addColumn("User");
        DataModelHS.addColumn("Date");
        DataModelHS.addColumn("Status");
        DataModelHS.addColumn("Remarks");
        
        TableHS.setAutoResizeMode(TableHS.AUTO_RESIZE_OFF);
    }
    
    private void PreviewReport() {
        HashMap Params=new HashMap();
        
        
        if(chkCancelled.isSelected()) {
            JOptionPane.showMessageDialog(null,"You cannot take printout of cancelled document");
            return;
        }
        
        Params.put("comp_id", new Integer(EITLERPGLOBAL.gCompanyID));
        Params.put("inquiry_no",txtDecID.getText());
        
        //DEC_ID
        //COMP_ID
        
        try {
            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptDF.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDecID.getText());
            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"File error "+e.getMessage());
        }
    }
    
    
    private void PreviewAuditReport() {
        try {
            URL ReportFile=new URL("http://"+EITLERPGLOBAL.HostIP+"/EITLERP/Reports/rptDeclarationA.jsp?dbURL="+EITLERPGLOBAL.DatabaseURL+"&CompanyID="+EITLERPGLOBAL.gCompanyID+"&DocNo="+txtDecID.getText());
            EITLERPGLOBAL.loginContext.showDocument(ReportFile,"_blank");
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Error Previwing "+e.getMessage());
        }
    }
    
}

