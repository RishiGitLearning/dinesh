/*
 * frmDeclarationConsumed.java
 *
 * Created on July 17, 2004, 5:40 PM
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
import org.nfunk.jep.*;
import org.nfunk.jep.type.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.text.*;

public class frmDeclarationConsumed extends javax.swing.JApplet {
    
    private int EditMode=0;
    private EITLTableModel DataModelL;
    private EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
    
   clsColumn ObjColumn=new clsColumn();
    
    //private JEP myParser=new JEP();
    private boolean Updating=false;
    private boolean Updating_H=false;
    private boolean DoNotEvaluate=false;
    
    private clsDecConsumed ObjDeclarationConsumed;
    
    private int SelHierarchyID=0; //Selected Hierarchy
    private int lnFromID=0;
    //private String SelPrefix=""; //Selected Prefix
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
       
    
    /** Creates new form frmTemplate */
    public frmDeclarationConsumed() {
        System.gc();
        setSize(758,474);
        
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
        
        GenerateCombos();
        ObjDeclarationConsumed=new clsDecConsumed();
        
        if(ObjDeclarationConsumed.LoadData(EITLERPGLOBAL.gCompanyID)) {
            ObjDeclarationConsumed.MoveFirst();
            DisplayData();
            SetMenuForRights();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while loading data. \n Error is "+ObjDeclarationConsumed.LastError);
        }
    }
    
    private void GenerateCombos() {
        //Generates Combo Boxes
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbHierarchy ------- //
        cmbHierarchyModel=new EITLComboModel();
        cmbHierarchy.removeAllItems();
        cmbHierarchy.setModel(cmbHierarchyModel);
        
        List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=39");
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
        
        ColList=clsSystemColumn.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=39 AND HIDDEN=0 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
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
            
            if(Variable.equals("CONSUMED_QTY")) {
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
        
        //----- Install Table Model Event Listener -------//
        TableL.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int col = e.getColumn();
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
                }
            }
        });            
        
        /*int RetCol = DataModelL.getColFromVariable("RETURNED");
        Renderer.setCustomComponent(RetCol,"CheckBox");
        JCheckBox aCheckBox=new JCheckBox();
        aCheckBox.setBackground(Color.WHITE);
        TableL.getColumnModel().getColumn(RetCol).setCellEditor(new DefaultCellEditor(aCheckBox));
        TableL.getColumnModel().getColumn(RetCol).setCellRenderer(Renderer);*/
        
}

private void SetMenuForRights() {
        // --- Add Rights --
   /* if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,131))
   {
      cmdNew.setEnabled(true);
   }
   else
   {
       cmdNew.setEnabled(false);
   }
     
   // --- Edit Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,132))
   {
      cmdEdit.setEnabled(true);
   }
   else
   {
       cmdEdit.setEnabled(false);
   }
     
   // --- Delete Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,133))
   {
      cmdDelete.setEnabled(true);
   }
   else
   {
      cmdDelete.setEnabled(false);
   }
     
   // --- Print Rights --
   if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID, 0,134))
   {
      cmdPreview.setEnabled(true);
      cmdPrint.setEnabled(true);
   }
   else
   {
      cmdPreview.setEnabled(false);
      cmdPrint.setEnabled(false);
   }*/
 }    
    
private void SetupColumns() {
        HashMap List=new HashMap();
        HashMap ColList=new HashMap();
        
        TableL.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableL.setRowSelectionAllowed(true);
        TableL.setColumnSelectionAllowed(true);
        
        ColList=clsSystemColumn.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID=39 AND HIDDEN=1 ORDER BY D_COM_SYSTEM_COLUMNS.ORDER");
        for(int i=1;i<=ColList.size();i++) {
            clsSystemColumn ObjColumn=(clsSystemColumn)ColList.get(Integer.toString(i));
            
            //Add Column First
            DataModelL.addColumn(""); //
            DataModelL.SetColID(TableL.getColumnCount()-1, 0);
            DataModelL.SetVariable(TableL.getColumnCount()-1,(String)ObjColumn.getAttribute("VARIABLE").getObj());
            DataModelL.SetOperation(TableL.getColumnCount()-1, "-");
            DataModelL.SetInclude(TableL.getColumnCount()-1,true);
            
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
    jPanel1 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    txtDocID = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    txtDate = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    txtContractor = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    txtDecID = new javax.swing.JTextField();
    jLabel21 = new javax.swing.JLabel();
    txtRemarks = new javax.swing.JTextField();
    cmdInsert = new javax.swing.JButton();
    cmdNext1 = new javax.swing.JButton();
    chkCancelled = new javax.swing.JCheckBox();
    jLabel7 = new javax.swing.JLabel();
    txtAdd1 = new javax.swing.JTextField();
    txtAdd2 = new javax.swing.JTextField();
    txtAdd3 = new javax.swing.JTextField();
    jLabel8 = new javax.swing.JLabel();
    txtDecDate = new javax.swing.JTextField();
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
    jPanel2 = new javax.swing.JPanel();
    jLabel20 = new javax.swing.JLabel();
    jPanel4 = new javax.swing.JPanel();
    cmdRemove = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    TableL = new javax.swing.JTable();
    cmdNext2 = new javax.swing.JButton();
    cmdNext4 = new javax.swing.JButton();
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
    jLabel1 = new javax.swing.JLabel();
    
    getContentPane().setLayout(null);
    
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
    
    cmdFilter.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdFilterActionPerformed(evt);
        }
    });
    
    ToolBar.add(cmdFilter);
    
    ToolBar.add(cmdPreview);
    
    ToolBar.add(cmdPrint);
    
    ToolBar.add(cmdExit);
    
    getContentPane().add(ToolBar);
    ToolBar.setBounds(0, 0, 800, 40);
    
    jPanel1.setLayout(null);
    
    jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
    jPanel1.setEnabled(false);
    jLabel2.setText("Declaration ID");
    jPanel1.add(jLabel2);
    jLabel2.setBounds(30, 60, 80, 15);
    
    txtDocID.setEditable(false);
    jPanel1.add(txtDocID);
    txtDocID.setBounds(120, 30, 114, 19);
    
    jLabel3.setText("Date");
    jPanel1.add(jLabel3);
    jLabel3.setBounds(260, 30, 30, 15);
    
    txtDate.setName("CONSUMEDDOC_DATE");
    txtDate.setEnabled(false);
    jPanel1.add(txtDate);
    txtDate.setBounds(300, 30, 100, 19);
    
    jLabel4.setText("Contractor");
    jPanel1.add(jLabel4);
    jLabel4.setBounds(50, 90, 60, 15);
    
    txtContractor.setEnabled(false);
    jPanel1.add(txtContractor);
    txtContractor.setBounds(120, 90, 280, 19);
    
    jLabel6.setText("Document ID");
    jPanel1.add(jLabel6);
    jLabel6.setBounds(30, 30, 90, 15);
    
    txtDecID.setName("DECLARATION_ID");
    txtDecID.setEnabled(false);
    txtDecID.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtDecIDActionPerformed(evt);
        }
    });
    
    jPanel1.add(txtDecID);
    txtDecID.setBounds(120, 60, 114, 19);
    
    jLabel21.setText("Remarks");
    jPanel1.add(jLabel21);
    jLabel21.setBounds(62, 326, 60, 15);
    
    txtRemarks.setName("REMARKS");
    txtRemarks.setEnabled(false);
    jPanel1.add(txtRemarks);
    txtRemarks.setBounds(122, 324, 280, 19);
    
    cmdInsert.setText("Insert from Declaration Form");
    cmdInsert.setName("DECLARATION_ID");
    cmdInsert.setEnabled(false);
    cmdInsert.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmdInsertActionPerformed(evt);
        }
    });
    
    jPanel1.add(cmdInsert);
    cmdInsert.setBounds(410, 60, 217, 25);
    
    cmdNext1.setText("Next >>");
    jPanel1.add(cmdNext1);
    cmdNext1.setBounds(640, 320, 90, 25);
    
    chkCancelled.setText("Cancelled");
    chkCancelled.setEnabled(false);
    jPanel1.add(chkCancelled);
    chkCancelled.setBounds(630, 30, 85, 23);
    
    jLabel7.setText("Address");
    jPanel1.add(jLabel7);
    jLabel7.setBounds(60, 120, 50, 15);
    
    txtAdd1.setEnabled(false);
    jPanel1.add(txtAdd1);
    txtAdd1.setBounds(120, 120, 280, 19);
    
    txtAdd2.setEnabled(false);
    jPanel1.add(txtAdd2);
    txtAdd2.setBounds(120, 144, 280, 19);
    
    txtAdd3.setEnabled(false);
    jPanel1.add(txtAdd3);
    txtAdd3.setBounds(120, 168, 280, 19);
    
    jLabel8.setText("Date");
    jPanel1.add(jLabel8);
    jLabel8.setBounds(260, 60, 30, 15);
    
    txtDecDate.setEnabled(false);
    jPanel1.add(txtDecDate);
    txtDecDate.setBounds(300, 60, 100, 19);
    
    jLabel9.setText("City");
    jPanel1.add(jLabel9);
    jLabel9.setBounds(80, 196, 30, 15);
    
    txtCity.setEnabled(false);
    jPanel1.add(txtCity);
    txtCity.setBounds(120, 196, 120, 19);
    
    jLabel10.setText("Pincode");
    jPanel1.add(jLabel10);
    jLabel10.setBounds(250, 196, 48, 15);
    
    txtPIN.setEnabled(false);
    jPanel1.add(txtPIN);
    txtPIN.setBounds(300, 196, 100, 19);
    
    jLabel11.setText("For Department");
    jPanel1.add(jLabel11);
    jLabel11.setBounds(22, 230, 90, 15);
    
    txtDeptID.setEnabled(false);
    jPanel1.add(txtDeptID);
    txtDeptID.setBounds(122, 230, 70, 19);
    
    txtDept.setBackground(new java.awt.Color(192, 192, 192));
    jPanel1.add(txtDept);
    txtDept.setBounds(192, 230, 210, 19);
    
    jLabel12.setText("Purpose");
    jPanel1.add(jLabel12);
    jLabel12.setBounds(62, 262, 50, 15);
    
    txtPurpose.setEnabled(false);
    jPanel1.add(txtPurpose);
    txtPurpose.setBounds(122, 262, 280, 19);
    
    jLabel22.setText("Received By");
    jPanel1.add(jLabel22);
    jLabel22.setBounds(40, 296, 80, 15);
    
    txtRecdBy.setEnabled(false);
    jPanel1.add(txtRecdBy);
    txtRecdBy.setBounds(122, 292, 180, 19);
    
    jTabbedPane1.addTab("Header", null, jPanel1, "Quotation Header");
    
    jPanel2.setLayout(null);
    
    jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
    jLabel20.setFont(new java.awt.Font("Arial", 1, 12));
    jLabel20.setText("Declaration Items");
    jPanel2.add(jLabel20);
    jLabel20.setBounds(10, 14, 100, 15);
    
    jPanel4.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
    jPanel2.add(jPanel4);
    jPanel4.setBounds(115, 20, 510, 4);
    
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
    jScrollPane1.setBounds(6, 38, 732, 230);
    
    cmdNext2.setText("Next >>");
    jPanel2.add(cmdNext2);
    cmdNext2.setBounds(640, 320, 90, 25);
    
    cmdNext4.setLabel("Previous <<");
    jPanel2.add(cmdNext4);
    cmdNext4.setBounds(520, 320, 120, 25);
    
    jTabbedPane1.addTab("Item Information", null, jPanel2, "Quotation Item");
    
    jPanel3.setLayout(null);
    
    jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
    jLabel31.setText("Hierarchy ");
    jPanel3.add(jLabel31);
    jLabel31.setBounds(16, 18, 66, 15);
    
    cmbHierarchy.setEnabled(false);
    cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbHierarchyItemStateChanged(evt);
        }
    });
    
    jPanel3.add(cmbHierarchy);
    cmbHierarchy.setBounds(86, 14, 184, 24);
    
    jLabel32.setText("From");
    jPanel3.add(jLabel32);
    jLabel32.setBounds(20, 52, 56, 15);
    
    txtFrom.setBackground(new java.awt.Color(204, 204, 204));
    jPanel3.add(txtFrom);
    txtFrom.setBounds(86, 50, 182, 19);
    
    jLabel35.setText("Remarks");
    jPanel3.add(jLabel35);
    jLabel35.setBounds(20, 82, 62, 15);
    
    txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
    jPanel3.add(txtFromRemarks);
    txtFromRemarks.setBounds(86, 78, 518, 19);
    
    jLabel36.setText("Your Action  ");
    jPanel3.add(jLabel36);
    jLabel36.setBounds(8, 124, 76, 15);
    
    jPanel6.setLayout(null);
    
    jPanel6.setBorder(new javax.swing.border.EtchedBorder());
    OpgApprove.setText("Approve & Forward");
    OpgApprove.setEnabled(false);
    jPanel6.add(OpgApprove);
    OpgApprove.setBounds(6, 6, 136, 23);
    
    OpgFinal.setText("Final Approve");
    OpgFinal.setEnabled(false);
    jPanel6.add(OpgFinal);
    OpgFinal.setBounds(6, 32, 136, 20);
    
    OpgReject.setText("Reject");
    OpgReject.setEnabled(false);
    jPanel6.add(OpgReject);
    OpgReject.setBounds(6, 54, 136, 20);
    
    OpgHold.setSelected(true);
    OpgHold.setText("Hold Document");
    OpgHold.setEnabled(false);
    jPanel6.add(OpgHold);
    OpgHold.setBounds(6, 76, 136, 20);
    
    jPanel3.add(jPanel6);
    jPanel6.setBounds(88, 116, 182, 100);
    
    jLabel33.setText("Send To");
    jPanel3.add(jLabel33);
    jLabel33.setBounds(18, 228, 60, 15);
    
    cmbSendTo.setEnabled(false);
    jPanel3.add(cmbSendTo);
    cmbSendTo.setBounds(86, 226, 184, 24);
    
    jLabel34.setText("Remarks");
    jPanel3.add(jLabel34);
    jLabel34.setBounds(16, 264, 60, 15);
    
    txtToRemarks.setEnabled(false);
    jPanel3.add(txtToRemarks);
    txtToRemarks.setBounds(86, 260, 516, 19);
    
    cmdNext3.setLabel("Previous <<");
    jPanel3.add(cmdNext3);
    cmdNext3.setBounds(610, 320, 120, 25);
    
    jTabbedPane1.addTab("Approval", null, jPanel3, "Quotation Approval");
    
    getContentPane().add(jTabbedPane1);
    jTabbedPane1.setBounds(0, 70, 760, 390);
    
    jLabel1.setBackground(new java.awt.Color(0, 102, 153));
    jLabel1.setForeground(java.awt.Color.white);
    jLabel1.setText("DECLARATION OF CONSUMED ITEM FORM");
    jLabel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
    jLabel1.setOpaque(true);
    getContentPane().add(jLabel1);
    jLabel1.setBounds(0, 40, 804, 25);
    
}//GEN-END:initComponents

    private void cmdInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdInsertActionPerformed
        // TODO add your handling code here: Nitin
        /* SelectDF ObjDF=new SelectDF();  
        
        if(ObjDF.ShowList()) {
            DoNotEvaluate=true;
            // int pCompanyID,String pPONo,String pURL,int pType) 
            if(ObjDF.colSelItems.size()>0)
            {
            clsDeclarationConsumedItem ObjDCItem=(clsDeclarationConsumedItem)ObjDF.colSelItems.get(Integer.toString(1));     
            
            clsDeclarationConsumed ObjDF1=new clsDeclarationConsumed();
           
            clsDeclarationForm ObjDF2=(clsPOGen)ObjPO1.getObject(EITLERPGLOBAL.gCompanyID,(String)ObjItem.getAttribute("PO_NO").getObj(),EITLERPGLOBAL.DatabaseURL,POType);
            
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
                DataModelL.setValueByVariable("RETURNED",Boolean.valueOf(false),NewRow);                
              // UpdateResults(DataModelL.getColFromVariable("QTY"));
            }
            
            UpdateSrNo();
            DoNotEvaluate=false;
        }*/
    }//GEN-LAST:event_cmdInsertActionPerformed

    private void TableLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TableLKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==127) //Delete key pressed
            {
                if(TableL.getRowCount()>0) {
                    DataModelL.removeRow(TableL.getSelectedRow());
                    //UpdateSrNo();
                }
            }
    }//GEN-LAST:event_TableLKeyPressed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if(TableL.getRowCount()>0) {
            DataModelL.removeRow(TableL.getSelectedRow());
            //UpdateSrNo();
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmbHierarchyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHierarchyItemStateChanged
        // TODO add your handling code here:
        SelHierarchyID=EITLERPGLOBAL.getComboCode(cmbHierarchy);
        //GenerateFromCombo(); //Nitin
        
        if(clsHierarchy.CanSkip((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gUserID)) {
            cmbSendTo.setEnabled(true);
        }
        else {
            cmbSendTo.setEnabled(false);
        }
        
        if(clsHierarchy.CanFinalApprove((int)EITLERPGLOBAL.gCompanyID, SelHierarchyID, (int)EITLERPGLOBAL.gUserID)) {
            OpgFinal.setEnabled(true);
        }
        else {
            OpgFinal.setEnabled(false);
            OpgFinal.setSelected(false);
        }
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void txtDecIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDecIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDecIDActionPerformed
    
    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdFilterActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JTable TableL;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JCheckBox chkCancelled;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbSendTo;
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
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdTop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtAdd1;
    private javax.swing.JTextField txtAdd2;
    private javax.swing.JTextField txtAdd3;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtContractor;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtDecDate;
    private javax.swing.JTextField txtDecID;
    private javax.swing.JTextField txtDept;
    private javax.swing.JTextField txtDeptID;
    private javax.swing.JTextField txtDocID;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtPIN;
    private javax.swing.JTextField txtPurpose;
    private javax.swing.JTextField txtRecdBy;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtToRemarks;
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
