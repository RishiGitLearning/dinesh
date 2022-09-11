/*
 * frmSalesParty.java
 *
 * Created on June 14, 2004, 3:00 PM
 */

package EITLERP.Production.ReportUI;

/**
 *
 * @author jadave
 */
/*<APPLET CODE=frmSalesParty.class HEIGHT=574 WIDTH=758></APPLET>*/

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
import java.lang.*;
import javax.swing.text.*;
import EITLERP.Finance.*;
import java.sql.*;
import java.text.*;
import java.util.Date;


public class frmfeltOrder extends javax.swing.JApplet {
    
    private int EditMode=0;
    
    private EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint=new EITLTableCellRenderer();
    
    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;
       
    
    private HashMap colVariables=new HashMap();
    private HashMap colVariables_H=new HashMap();
    //clsColumn ObjColumn=new clsColumn();
    
    private boolean Updating=false;
    private boolean Updating_H=false;
    private boolean DoNotEvaluate=false;
    
    //private clsOrderParty ObjSalesParty;
    private clsOrderParty ObjSalesParty;
    
    private int SelHierarchyID=0; //Selected Hierarchy
    private int lnFromID=0;
    
    private EITLComboModel cmbHierarchyModel;
    private EITLComboModel cmbToModel;
    private EITLComboModel cmbPriorityModel;
     
       
    private EITLTableModel DataModelA;
    
    private boolean HistoryView=false;
    private String theDocNo="";
    private EITLTableModel DataModelHS;
    
    public frmPendingApprovals frmPA;
    private int charge09index=0;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    
    /** Creates new form frmSalesParty */
    public frmfeltOrder() {
        System.gc();        
        setSize(800,600);
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
        
        //ObjColumn.LoadData((int)EITLERPGLOBAL.gCompanyID);
       
        GenerateCombos();
        GenerateDeptCombo();
        FormatGrid();
        GenerateGrid();
    
      //  cmdSelectAll.setEnabled(false);
      //  cmdClearAll.setEnabled(false);
        
        ObjSalesParty = new clsOrderParty();
        lblRevNo.setVisible(false);
        
        //lblDocThrough.setVisible(false);
        //txtDocThrough.setVisible(false);
        
        if(ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID)) {
            DisplayData();
            SetMenuForRights();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while loading data. \n Error is "+ObjSalesParty.LastError);
        }
        
        txtAuditRemarks.setVisible(false);
        
    }
    
   /* private void ChargeCodeCombos(String strCon) {
        //----------Charge Code---------//
        
        
        //------------------------------//
        
        //----------Second Charge Code---------//
        
        //------------------------------//           
    }*/
    
    private void GenerateDeptCombo()
    {

        cmbPriorityModel=new EITLComboModel();
        cmbPriority.removeAllItems();
        cmbPriority.setModel(cmbPriorityModel);
        
        HashMap List=clsPriority.getDeptList(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID);
        
        for(int i=1;i<=List.size();i++) {
            clsPriority ObjDept=(clsPriority) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjDept.getAttribute("PRIORITY_ID").getVal();
            aData.Text=(String)ObjDept.getAttribute("PRIORITY_DESC").getObj();
            cmbPriorityModel.addElement(aData);
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
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsOrderParty.ModuleID);
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsOrderParty.ModuleID);
        }
        
        for(int i=1;i<=List.size();i++) {
            clsHierarchy ObjHierarchy=(clsHierarchy) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjHierarchy.getAttribute("HIERARCHY_ID").getVal();
            aData.Text=(String)ObjHierarchy.getAttribute("HIERARCHY_NAME").getObj();
            cmbHierarchyModel.addElement(aData);
        }
        //------------------------------ //
        
        //----- Generate Department Combo ------- //
        
        cmbPriorityModel=new EITLComboModel();
        cmbPriority.removeAllItems();
        cmbPriority.setModel(cmbPriorityModel);
        
        List=clsPriority.getList("WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
        
        for(int i=1;i<=List.size();i++) {
            clsPriority ObjDept= (clsPriority) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjDept.getAttribute("PRIORITY_ID").getVal();
            aData.Text=(String)ObjDept.getAttribute("PRIORITY_DESC").getObj();
            cmbPriorityModel.addElement(aData);
        }
        
        //------------------------------ //       
        //--- Generate Type Combo ------//
        /*
        cmbPriorityModel=new EITLComboModel();
        cmbPriority.removeAllItems();
        cmbPriority.setModel(cmbPriorityModel);
        
        ComboData aData=new ComboData();
        aData.strCode="LOW";
        aData.Text="LOW";
        cmbPriorityModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="MEDIUM";
        aData.Text="MEDIUM";
        cmbPriorityModel.addElement(aData);
        
        aData=new ComboData();
        aData.strCode="HIGH";
        aData.Text="HIGH";
        cmbPriorityModel.addElement(aData);
         */
        //===============================//
        
        //-------Priority Type --------//
        /*
        cmbPriorityModel = new EITLComboModel();
        cmbPriority.removeAllItems();
        cmbPriority.setModel(cmbPriorityModel);
        
        ComboData aData=new ComboData();
        
        aData=new ComboData();
        aData.Code=1;
        aData.Text="LOW";//index 1
        cmbPriorityModel.addElement(aData);
        
        */
        /*aData=new ComboData();       
        aData.Code=2;
        aData.Text="MEDIUM";//index 2
        cmbPriorityModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="HIGH";//index 3
        cmbPriorityModel.addElement(aData);        
        */
        /*
        aData=new ComboData();       
        aData.Code=2;
        aData.Text="MEDIUM1";//index 2
        cmbPriorityModel.addElement(aData);
        
        aData=new ComboData();
        aData.Code=3;
        aData.Text="MEDIUM2";//index 3
        cmbPriorityModel.addElement(aData);        
        
        aData=new ComboData();
        aData.Code=4;
        aData.Text="HIGH";//index 3
        cmbPriorityModel.addElement(aData);        
        */
        //------------------------------//        
        //--------Category--------//       
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        Tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPartyCode = new javax.swing.JTextField();
        cmdNext1 = new javax.swing.JButton();
        txtPieceNo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtProductCode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtLength = new javax.swing.JTextField();
        txtPosition = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtWidth = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtGSQ = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDelivDate = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtGroup = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtConfNo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtWeight = new javax.swing.JTextField();
        lblRevNo = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtOrderCode = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtRefNo = new javax.swing.JTextField();
        txtCommDate = new javax.swing.JTextField();
        lblStationName = new javax.swing.JLabel();
        txtMachineNo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        cmbPriority = new javax.swing.JComboBox();
        txtOrderDate = new javax.swing.JTextField();
        txtprioritydate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtlastpriorityuser = new javax.swing.JTextField();
        Tab2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtPartyCode1 = new javax.swing.JTextField();
        cmdNext5 = new javax.swing.JButton();
        txtPieceNo1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtProductCode1 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtLength1 = new javax.swing.JTextField();
        txtPosition1 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtWidth1 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtGSQ1 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtDelivDate1 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        txtGroup1 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        txtConfNo1 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtWeight1 = new javax.swing.JTextField();
        lblRevNo1 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        txtOrderCode1 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtRefNo1 = new javax.swing.JTextField();
        txtCommDate1 = new javax.swing.JTextField();
        lblStationName1 = new javax.swing.JLabel();
        txtMachineNo1 = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        cmbPriority1 = new javax.swing.JComboBox();
        txtOrderDate1 = new javax.swing.JTextField();
        txtprioritydate1 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        txtlastpriorityuser1 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        txtRevisedRequestedDate = new javax.swing.JTextField();
        cmdRemarksBig = new javax.swing.JButton();
        cmdLastUsrBig = new javax.swing.JButton();
        txtSetPriority = new javax.swing.JTextField();
        txtPrevRemarks = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        txtAgreedDate = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtRevisedCommDate = new javax.swing.JTextField();
        lblStationName2 = new javax.swing.JLabel();
        txtLastPriority = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtRequestedRemarks = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtCommitedRemarks = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel59 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        Tab3 = new javax.swing.JPanel();
        cmdNext2 = new javax.swing.JButton();
        cmdNext4 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtProdInd = new javax.swing.JTextField();
        txtOrdDDMMA = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtProdIndA = new javax.swing.JTextField();
        txtOrdDDMMB = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtProdIndB = new javax.swing.JTextField();
        txtOrdDDMMC = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        txtSeamCharge = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblInvoiceNo = new javax.swing.JLabel();
        txtInvoiceNo = new javax.swing.JTextField();
        lblInvoiceDate = new javax.swing.JLabel();
        txtInvoiceAmt = new javax.swing.JTextField();
        lblDiscount = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JTextField();
        txtBaseAmt = new javax.swing.JTextField();
        lblInvoiceAmt = new javax.swing.JLabel();
        lblBaseAmt = new javax.swing.JLabel();
        txtOther = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtApproxAmt = new javax.swing.JTextField();
        lblOrderDDMMB = new javax.swing.JLabel();
        txtProdIndD = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtProdIndC = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        lblExcise = new javax.swing.JLabel();
        txtExcise = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblOrdDDMMA = new javax.swing.JLabel();
        txtInvoiceDate = new javax.swing.JTextField();
        txtWPSC = new javax.swing.JTextField();
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
        jLabel60 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableA = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TableHS = new javax.swing.JTable();
        cmdViewHistory = new javax.swing.JButton();
        cmdNormalView = new javax.swing.JButton();
        cmdShowRemarks = new javax.swing.JButton();
        txtAuditRemarks = new javax.swing.JTextField();

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

        cmdBack.setToolTipText(" Previous Record");
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

        cmdCancel.setToolTipText("Cancel Record");
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

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("Felt Order Master ");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 800, 25);

        jTabbedPane1.setToolTipText("Party Master Header");
        Tab1.setLayout(null);

        Tab1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.setToolTipText("Order Header");
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Piece No");
        Tab1.add(jLabel2);
        jLabel2.setBounds(40, 10, 80, 20);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Party code ");
        Tab1.add(jLabel5);
        jLabel5.setBounds(220, 10, 70, 20);

        txtPartyCode.setName("PARTY_NAME");
        txtPartyCode.setEnabled(false);
        Tab1.add(txtPartyCode);
        txtPartyCode.setBounds(290, 10, 80, 20);

        cmdNext1.setText("Next >>");
        cmdNext1.setNextFocusableComponent(txtPieceNo);
        cmdNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext1ActionPerformed(evt);
            }
        });
        cmdNext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdNext1KeyPressed(evt);
            }
        });

        Tab1.add(cmdNext1);
        cmdNext1.setBounds(660, 420, 90, 25);

        txtPieceNo.setName("PARTY_CODE");
        txtPieceNo.setEnabled(false);
        Tab1.add(txtPieceNo);
        txtPieceNo.setBounds(130, 10, 80, 20);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Product Code ");
        Tab1.add(jLabel4);
        jLabel4.setBounds(370, 10, 100, 20);

        txtProductCode.setName("ATTN");
        txtProductCode.setEnabled(false);
        Tab1.add(txtProductCode);
        txtProductCode.setBounds(470, 10, 80, 19);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Length ");
        Tab1.add(jLabel7);
        jLabel7.setBounds(30, 40, 90, 20);

        txtLength.setName("ADD1");
        txtLength.setNextFocusableComponent(txtPosition);
        txtLength.setEnabled(false);
        Tab1.add(txtLength);
        txtLength.setBounds(130, 40, 60, 20);

        txtPosition.setName("ADD2");
        txtPosition.setNextFocusableComponent(txtWidth);
        txtPosition.setEnabled(false);
        txtPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPositionActionPerformed(evt);
            }
        });

        Tab1.add(txtPosition);
        txtPosition.setBounds(240, 230, 130, 20);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Width  ");
        Tab1.add(jLabel9);
        jLabel9.setBounds(220, 40, 60, 20);

        txtWidth.setName("CITY");
        txtWidth.setNextFocusableComponent(txtGSQ);
        txtWidth.setEnabled(false);
        Tab1.add(txtWidth);
        txtWidth.setBounds(290, 40, 80, 19);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("GSM");
        Tab1.add(jLabel10);
        jLabel10.setBounds(410, 40, 50, 20);

        txtGSQ.setName("PINCODE");
        txtGSQ.setNextFocusableComponent(txtDelivDate);
        txtGSQ.setEnabled(false);
        Tab1.add(txtGSQ);
        txtGSQ.setBounds(470, 40, 50, 19);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Requested Date ");
        Tab1.add(jLabel6);
        jLabel6.setBounds(10, 70, 110, 20);

        txtDelivDate.setName("STATE");
        txtDelivDate.setNextFocusableComponent(txtGroup);
        txtDelivDate.setEnabled(false);
        txtDelivDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDelivDateActionPerformed(evt);
            }
        });
        txtDelivDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDelivDateFocusLost(evt);
            }
        });

        Tab1.add(txtDelivDate);
        txtDelivDate.setBounds(130, 70, 90, 20);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Group ");
        Tab1.add(jLabel12);
        jLabel12.setBounds(30, 360, 90, 20);

        txtGroup.setName("PHONE_O");
        txtGroup.setNextFocusableComponent(txtConfNo);
        txtGroup.setEnabled(false);
        Tab1.add(txtGroup);
        txtGroup.setBounds(130, 360, 130, 19);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Confirmation No");
        Tab1.add(jLabel14);
        jLabel14.setBounds(390, 260, 110, 20);

        txtConfNo.setName("MOBILE_NO");
        txtConfNo.setNextFocusableComponent(txtWeight);
        txtConfNo.setEnabled(false);
        txtConfNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConfNoActionPerformed(evt);
            }
        });

        Tab1.add(txtConfNo);
        txtConfNo.setBounds(520, 230, 100, 19);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("WeIght  ");
        Tab1.add(jLabel15);
        jLabel15.setBounds(570, 40, 60, 20);

        txtWeight.setName("EMAIL_ADD");
        txtWeight.setEnabled(false);
        Tab1.add(txtWeight);
        txtWeight.setBounds(640, 40, 60, 19);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(750, 10, 20, 20);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Position ");
        Tab1.add(jLabel11);
        jLabel11.setBounds(170, 230, 60, 20);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Priority ");
        Tab1.add(jLabel20);
        jLabel20.setBounds(50, 260, 60, 20);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Machine No ");
        Tab1.add(jLabel13);
        jLabel13.setBounds(20, 230, 90, 20);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Order No ");
        Tab1.add(jLabel17);
        jLabel17.setBounds(20, 420, 100, 20);

        txtOrderCode.setName("FROM_DATE_REG");
        txtOrderCode.setNextFocusableComponent(txtRefNo);
        txtOrderCode.setEnabled(false);
        Tab1.add(txtOrderCode);
        txtOrderCode.setBounds(130, 420, 80, 21);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Reference No");
        Tab1.add(jLabel30);
        jLabel30.setBounds(410, 230, 90, 20);

        txtRefNo.setName("FROM_DATE_REG");
        txtRefNo.setNextFocusableComponent(txtProductCode);
        txtRefNo.setEnabled(false);
        txtRefNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRefNoActionPerformed(evt);
            }
        });

        Tab1.add(txtRefNo);
        txtRefNo.setBounds(520, 260, 100, 20);

        txtCommDate.setName("STATE");
        txtCommDate.setNextFocusableComponent(txtGroup);
        txtCommDate.setEnabled(false);
        txtCommDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommDateActionPerformed(evt);
            }
        });

        Tab1.add(txtCommDate);
        txtCommDate.setBounds(350, 70, 90, 19);

        lblStationName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStationName.setText("Commited Date ");
        Tab1.add(lblStationName);
        lblStationName.setBounds(230, 70, 110, 20);

        txtMachineNo.setEnabled(false);
        txtMachineNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMachineNoActionPerformed(evt);
            }
        });

        Tab1.add(txtMachineNo);
        txtMachineNo.setBounds(120, 230, 30, 19);

        jLabel1.setText("Order Date");
        Tab1.add(jLabel1);
        jLabel1.setBounds(560, 10, 80, 20);

        jPanel7.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel7);
        jPanel7.setBounds(10, 350, 750, 10);

        cmbPriority.setEnabled(false);
        cmbPriority.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPriorityActionPerformed(evt);
            }
        });
        cmbPriority.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbPriorityFocusLost(evt);
            }
        });

        Tab1.add(cmbPriority);
        cmbPriority.setBounds(120, 260, 130, 24);

        txtOrderDate.setEnabled(false);
        Tab1.add(txtOrderDate);
        txtOrderDate.setBounds(640, 10, 90, 19);

        txtprioritydate.setEnabled(false);
        Tab1.add(txtprioritydate);
        txtprioritydate.setBounds(440, 360, 130, 20);

        jLabel8.setText("Last Change Date");
        Tab1.add(jLabel8);
        jLabel8.setBounds(320, 360, 110, 20);

        txtlastpriorityuser.setEnabled(false);
        Tab1.add(txtlastpriorityuser);
        txtlastpriorityuser.setBounds(130, 390, 440, 19);

        Tab2.setLayout(null);

        Tab2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Tab2.setToolTipText("Order Header");
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Piece No");
        Tab2.add(jLabel16);
        jLabel16.setBounds(30, 20, 80, 20);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Party code ");
        Tab2.add(jLabel18);
        jLabel18.setBounds(30, 50, 80, 20);

        txtPartyCode1.setName("PARTY_NAME");
        txtPartyCode1.setEnabled(false);
        Tab2.add(txtPartyCode1);
        txtPartyCode1.setBounds(120, 49, 130, 20);

        cmdNext5.setText("Next >>");
        cmdNext5.setNextFocusableComponent(txtPieceNo);
        Tab2.add(cmdNext5);
        cmdNext5.setBounds(660, 410, 90, 25);

        txtPieceNo1.setName("PARTY_CODE");
        txtPieceNo1.setEnabled(false);
        Tab2.add(txtPieceNo1);
        txtPieceNo1.setBounds(120, 20, 130, 20);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Product Code ");
        Tab2.add(jLabel22);
        jLabel22.setBounds(320, 50, 100, 20);

        txtProductCode1.setName("ATTN");
        txtProductCode1.setEnabled(false);
        Tab2.add(txtProductCode1);
        txtProductCode1.setBounds(430, 50, 130, 19);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Length ");
        Tab2.add(jLabel25);
        jLabel25.setBounds(20, 220, 90, 20);

        txtLength1.setName("ADD1");
        txtLength1.setNextFocusableComponent(txtPosition);
        txtLength1.setEnabled(false);
        Tab2.add(txtLength1);
        txtLength1.setBounds(120, 220, 130, 20);

        txtPosition1.setName("ADD2");
        txtPosition1.setNextFocusableComponent(txtWidth);
        txtPosition1.setEnabled(false);
        Tab2.add(txtPosition1);
        txtPosition1.setBounds(120, 109, 130, 20);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Width  ");
        Tab2.add(jLabel29);
        jLabel29.setBounds(340, 220, 80, 20);

        txtWidth1.setName("CITY");
        txtWidth1.setNextFocusableComponent(txtGSQ);
        txtWidth1.setEnabled(false);
        Tab2.add(txtWidth1);
        txtWidth1.setBounds(430, 220, 130, 19);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("GSM");
        Tab2.add(jLabel38);
        jLabel38.setBounds(360, 250, 50, 20);

        txtGSQ1.setName("PINCODE");
        txtGSQ1.setNextFocusableComponent(txtDelivDate);
        txtGSQ1.setEnabled(false);
        Tab2.add(txtGSQ1);
        txtGSQ1.setBounds(430, 250, 130, 19);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Delivery Date ");
        Tab2.add(jLabel41);
        jLabel41.setBounds(10, 280, 100, 15);

        txtDelivDate1.setName("STATE");
        txtDelivDate1.setNextFocusableComponent(txtGroup);
        txtDelivDate1.setEnabled(false);
        Tab2.add(txtDelivDate1);
        txtDelivDate1.setBounds(120, 280, 130, 19);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Group ");
        Tab2.add(jLabel42);
        jLabel42.setBounds(20, 310, 90, 20);

        txtGroup1.setName("PHONE_O");
        txtGroup1.setNextFocusableComponent(txtConfNo);
        txtGroup1.setEnabled(false);
        Tab2.add(txtGroup1);
        txtGroup1.setBounds(120, 310, 130, 19);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Confirmation No");
        Tab2.add(jLabel43);
        jLabel43.setBounds(290, 80, 130, 20);

        txtConfNo1.setName("MOBILE_NO");
        txtConfNo1.setNextFocusableComponent(txtWeight);
        txtConfNo1.setEnabled(false);
        Tab2.add(txtConfNo1);
        txtConfNo1.setBounds(430, 80, 130, 19);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("WeIght  ");
        Tab2.add(jLabel44);
        jLabel44.setBounds(50, 250, 60, 20);

        txtWeight1.setName("EMAIL_ADD");
        txtWeight1.setEnabled(false);
        Tab2.add(txtWeight1);
        txtWeight1.setBounds(120, 250, 130, 19);

        lblRevNo1.setText("...");
        Tab2.add(lblRevNo1);
        lblRevNo1.setBounds(260, 20, 20, 20);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Position ");
        Tab2.add(jLabel45);
        jLabel45.setBounds(30, 110, 80, 20);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Priority ");
        Tab2.add(jLabel46);
        jLabel46.setBounds(360, 140, 60, 20);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Machine No ");
        Tab2.add(jLabel47);
        jLabel47.setBounds(20, 80, 90, 20);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Order No ");
        Tab2.add(jLabel48);
        jLabel48.setBounds(0, 140, 110, 20);

        txtOrderCode1.setName("FROM_DATE_REG");
        txtOrderCode1.setNextFocusableComponent(txtRefNo);
        txtOrderCode1.setEnabled(false);
        Tab2.add(txtOrderCode1);
        txtOrderCode1.setBounds(120, 140, 130, 21);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Reference No");
        Tab2.add(jLabel49);
        jLabel49.setBounds(310, 110, 110, 20);

        txtRefNo1.setName("FROM_DATE_REG");
        txtRefNo1.setNextFocusableComponent(txtProductCode);
        txtRefNo1.setEnabled(false);
        Tab2.add(txtRefNo1);
        txtRefNo1.setBounds(430, 110, 130, 21);

        txtCommDate1.setName("STATE");
        txtCommDate1.setNextFocusableComponent(txtGroup);
        txtCommDate1.setEnabled(false);
        Tab2.add(txtCommDate1);
        txtCommDate1.setBounds(430, 280, 130, 19);

        lblStationName1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStationName1.setText("Commitment Date ");
        Tab2.add(lblStationName1);
        lblStationName1.setBounds(300, 280, 120, 20);

        txtMachineNo1.setEnabled(false);
        Tab2.add(txtMachineNo1);
        txtMachineNo1.setBounds(120, 80, 130, 19);

        jLabel50.setText("Order Date");
        Tab2.add(jLabel50);
        jLabel50.setBounds(340, 20, 70, 20);

        jPanel8.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab2.add(jPanel8);
        jPanel8.setBounds(20, 180, 750, 7);

        cmbPriority1.setEnabled(false);
        Tab2.add(cmbPriority1);
        cmbPriority1.setBounds(430, 140, 130, 24);

        txtOrderDate1.setEnabled(false);
        Tab2.add(txtOrderDate1);
        txtOrderDate1.setBounds(430, 20, 130, 19);

        txtprioritydate1.setEnabled(false);
        Tab2.add(txtprioritydate1);
        txtprioritydate1.setBounds(430, 310, 130, 20);

        jLabel51.setText("Last Change Date");
        Tab2.add(jLabel51);
        jLabel51.setBounds(310, 310, 110, 20);

        Tab2.add(txtlastpriorityuser1);
        txtlastpriorityuser1.setBounds(430, 340, 130, 19);

        Tab1.add(Tab2);
        Tab2.setBounds(0, 0, 0, 0);

        jLabel52.setText("Last Prio Set User");
        Tab1.add(jLabel52);
        jLabel52.setBounds(10, 390, 110, 20);

        jLabel53.setText("Remarks");
        Tab1.add(jLabel53);
        jLabel53.setBounds(50, 290, 60, 20);

        txtRemarks.setEnabled(false);
        Tab1.add(txtRemarks);
        txtRemarks.setBounds(120, 290, 440, 19);

        txtRevisedRequestedDate.setEnabled(false);
        txtRevisedRequestedDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRevisedRequestedDateActionPerformed(evt);
            }
        });
        txtRevisedRequestedDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRevisedRequestedDateFocusLost(evt);
            }
        });

        Tab1.add(txtRevisedRequestedDate);
        txtRevisedRequestedDate.setBounds(130, 120, 90, 20);

        cmdRemarksBig.setText("...");
        cmdRemarksBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemarksBigActionPerformed(evt);
            }
        });

        Tab1.add(cmdRemarksBig);
        cmdRemarksBig.setBounds(570, 290, 20, 20);

        cmdLastUsrBig.setText("...");
        cmdLastUsrBig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLastUsrBigActionPerformed(evt);
            }
        });

        Tab1.add(cmdLastUsrBig);
        cmdLastUsrBig.setBounds(580, 390, 20, 20);

        txtSetPriority.setEnabled(false);
        Tab1.add(txtSetPriority);
        txtSetPriority.setBounds(420, 420, 150, 19);

        txtPrevRemarks.setEnabled(false);
        Tab1.add(txtPrevRemarks);
        txtPrevRemarks.setBounds(120, 320, 520, 19);

        jLabel54.setText("Prev. Remarks");
        Tab1.add(jLabel54);
        jLabel54.setBounds(20, 320, 90, 20);

        jLabel55.setText("Agreed Date");
        Tab1.add(jLabel55);
        jLabel55.setBounds(450, 70, 90, 20);

        txtAgreedDate.setEnabled(false);
        txtAgreedDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgreedDateActionPerformed(evt);
            }
        });

        Tab1.add(txtAgreedDate);
        txtAgreedDate.setBounds(550, 70, 90, 19);

        jLabel56.setText("Requested Date");
        Tab1.add(jLabel56);
        jLabel56.setBounds(20, 120, 100, 20);

        txtRevisedCommDate.setName("STATE");
        txtRevisedCommDate.setNextFocusableComponent(txtGroup);
        txtRevisedCommDate.setEnabled(false);
        txtRevisedCommDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRevisedCommDateActionPerformed(evt);
            }
        });
        txtRevisedCommDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRevisedCommDateFocusLost(evt);
            }
        });

        Tab1.add(txtRevisedCommDate);
        txtRevisedCommDate.setBounds(130, 150, 90, 19);

        lblStationName2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStationName2.setText("Commited Date");
        Tab1.add(lblStationName2);
        lblStationName2.setBounds(20, 150, 100, 20);

        Tab1.add(txtLastPriority);
        txtLastPriority.setBounds(260, 260, 110, 20);

        jLabel57.setText("Reason");
        Tab1.add(jLabel57);
        jLabel57.setBounds(230, 120, 50, 20);

        txtRequestedRemarks.setEnabled(false);
        txtRequestedRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRequestedRemarksActionPerformed(evt);
            }
        });

        Tab1.add(txtRequestedRemarks);
        txtRequestedRemarks.setBounds(290, 120, 230, 20);

        jLabel58.setText("Reason");
        Tab1.add(jLabel58);
        jLabel58.setBounds(230, 150, 50, 20);

        txtCommitedRemarks.setEnabled(false);
        Tab1.add(txtCommitedRemarks);
        txtCommitedRemarks.setBounds(290, 150, 230, 20);

        Tab1.add(jSeparator1);
        jSeparator1.setBounds(0, 0, 0, 2);

        jLabel59.setBackground(java.awt.Color.blue);
        jLabel59.setText("Revised Dates");
        Tab1.add(jLabel59);
        jLabel59.setBounds(30, 100, 90, 15);

        jPanel9.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel9);
        jPanel9.setBounds(130, 100, 630, 10);

        jPanel10.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel10);
        jPanel10.setBounds(10, 180, 750, 10);

        Tab1.add(jSeparator2);
        jSeparator2.setBounds(0, 0, 0, 2);

        jPanel11.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab1.add(jPanel11);
        jPanel11.setBounds(10, 100, 10, 80);

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Tab1.add(jButton1);
        jButton1.setBounds(530, 120, 20, 20);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Tab1.add(jButton2);
        jButton2.setBounds(530, 150, 20, 20);

        jTabbedPane1.addTab("Header", null, Tab1, "Party Master Header");

        Tab3.setLayout(null);

        Tab3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Tab3.setToolTipText("");
        cmdNext2.setText("Next >>");
        cmdNext2.setNextFocusableComponent(cmdNext4);
        cmdNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext2ActionPerformed(evt);
            }
        });

        Tab3.add(cmdNext2);
        cmdNext2.setBounds(680, 415, 90, 25);

        cmdNext4.setText("<< Back");
        cmdNext4.setNextFocusableComponent(txtInvoiceNo);
        cmdNext4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext4ActionPerformed(evt);
            }
        });
        cmdNext4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdNext4KeyPressed(evt);
            }
        });

        Tab3.add(cmdNext4);
        cmdNext4.setBounds(590, 415, 90, 25);

        jLabel26.setText("WPSC");
        Tab3.add(jLabel26);
        jLabel26.setBounds(410, 70, 48, 20);

        jLabel23.setText("OTHER");
        Tab3.add(jLabel23);
        jLabel23.setBounds(100, 100, 50, 20);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Prod IND");
        Tab3.add(jLabel24);
        jLabel24.setBounds(60, 210, 80, 20);

        txtProdInd.setName("CST_NO");
        txtProdInd.setNextFocusableComponent(txtOrdDDMMA);
        txtProdInd.setEnabled(false);
        Tab3.add(txtProdInd);
        txtProdInd.setBounds(160, 210, 150, 21);

        txtOrdDDMMA.setName("CST_DATE");
        txtOrdDDMMA.setNextFocusableComponent(txtProdIndA);
        txtOrdDDMMA.setEnabled(false);
        Tab3.add(txtOrdDDMMA);
        txtOrdDDMMA.setBounds(470, 240, 140, 21);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Prod Ind A");
        Tab3.add(jLabel27);
        jLabel27.setBounds(70, 240, 80, 20);

        txtProdIndA.setName("GST_NO");
        txtProdIndA.setNextFocusableComponent(txtOrdDDMMB);
        txtProdIndA.setEnabled(false);
        Tab3.add(txtProdIndA);
        txtProdIndA.setBounds(160, 240, 150, 21);

        txtOrdDDMMB.setName("GST_DATE");
        txtOrdDDMMB.setNextFocusableComponent(txtProdIndB);
        txtOrdDDMMB.setEnabled(false);
        Tab3.add(txtOrdDDMMB);
        txtOrdDDMMB.setBounds(470, 270, 140, 21);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Prod Ind B");
        Tab3.add(jLabel28);
        jLabel28.setBounds(70, 270, 80, 20);

        txtProdIndB.setName("ECC_NO");
        txtProdIndB.setNextFocusableComponent(txtOrdDDMMC);
        txtProdIndB.setEnabled(false);
        Tab3.add(txtProdIndB);
        txtProdIndB.setBounds(160, 270, 150, 21);

        txtOrdDDMMC.setName("ECC_DATE");
        txtOrdDDMMC.setNextFocusableComponent(txtProdIndC);
        txtOrdDDMMC.setEnabled(false);
        Tab3.add(txtOrdDDMMC);
        txtOrdDDMMC.setBounds(470, 300, 140, 21);

        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel1);
        jPanel1.setBounds(10, 380, 760, 7);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Seam Charge");
        Tab3.add(jLabel39);
        jLabel39.setBounds(20, 130, 130, 20);

        txtSeamCharge.setName("FROM_DATE_REG");
        txtSeamCharge.setNextFocusableComponent(txtExcise);
        txtSeamCharge.setEnabled(false);
        Tab3.add(txtSeamCharge);
        txtSeamCharge.setBounds(160, 130, 150, 21);

        jPanel4.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        Tab3.add(jPanel4);
        jPanel4.setBounds(10, 190, 750, 7);

        lblInvoiceNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvoiceNo.setText("Invoice No");
        Tab3.add(lblInvoiceNo);
        lblInvoiceNo.setBounds(60, 10, 90, 20);

        txtInvoiceNo.setName("BANK_ID");
        txtInvoiceNo.setNextFocusableComponent(txtInvoiceAmt);
        txtInvoiceNo.setEnabled(false);
        Tab3.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(160, 10, 150, 19);

        lblInvoiceDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvoiceDate.setText("Invoice Date ");
        Tab3.add(lblInvoiceDate);
        lblInvoiceDate.setBounds(365, 10, 90, 20);

        txtInvoiceAmt.setName("ADD1");
        txtInvoiceAmt.setNextFocusableComponent(txtDiscount);
        txtInvoiceAmt.setEnabled(false);
        Tab3.add(txtInvoiceAmt);
        txtInvoiceAmt.setBounds(160, 40, 150, 19);

        lblDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDiscount.setText("Discount");
        Tab3.add(lblDiscount);
        lblDiscount.setBounds(350, 100, 100, 20);

        txtDiscount.setName("CITY");
        txtDiscount.setNextFocusableComponent(txtBaseAmt);
        txtDiscount.setEnabled(false);
        Tab3.add(txtDiscount);
        txtDiscount.setBounds(460, 100, 150, 19);

        txtBaseAmt.setName("BANK_ID");
        txtBaseAmt.setNextFocusableComponent(txtOther);
        txtBaseAmt.setEnabled(false);
        Tab3.add(txtBaseAmt);
        txtBaseAmt.setBounds(160, 70, 150, 19);

        lblInvoiceAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvoiceAmt.setText("Invoice Amount ");
        Tab3.add(lblInvoiceAmt);
        lblInvoiceAmt.setBounds(25, 40, 130, 20);

        lblBaseAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBaseAmt.setText("Base Amount ");
        Tab3.add(lblBaseAmt);
        lblBaseAmt.setBounds(40, 70, 115, 20);

        txtOther.setName("ADD1");
        txtOther.setNextFocusableComponent(txtApproxAmt);
        txtOther.setEnabled(false);
        Tab3.add(txtOther);
        txtOther.setBounds(160, 100, 150, 19);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Approx Amount");
        Tab3.add(jLabel21);
        jLabel21.setBounds(40, 160, 110, 20);

        txtApproxAmt.setName("CITY");
        txtApproxAmt.setNextFocusableComponent(txtProdInd);
        txtApproxAmt.setEnabled(false);
        Tab3.add(txtApproxAmt);
        txtApproxAmt.setBounds(160, 160, 150, 20);

        lblOrderDDMMB.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOrderDDMMB.setText("Ord DDMM B");
        Tab3.add(lblOrderDDMMB);
        lblOrderDDMMB.setBounds(350, 270, 100, 20);

        txtProdIndD.setName("BANK_ID");
        txtProdIndD.setEnabled(false);
        Tab3.add(txtProdIndD);
        txtProdIndD.setBounds(160, 330, 150, 19);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Prod Ind C");
        Tab3.add(jLabel37);
        jLabel37.setBounds(70, 300, 80, 20);

        txtProdIndC.setName("ECC_NO");
        txtProdIndC.setEnabled(false);
        Tab3.add(txtProdIndC);
        txtProdIndC.setBounds(160, 300, 150, 21);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Ord DDMM C");
        Tab3.add(jLabel40);
        jLabel40.setBounds(330, 300, 120, 20);

        lblExcise.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExcise.setText("Excise");
        Tab3.add(lblExcise);
        lblExcise.setBounds(390, 130, 60, 20);

        txtExcise.setName("FROM_DATE_REG");
        txtExcise.setNextFocusableComponent(txtProdIndD);
        txtExcise.setEnabled(false);
        Tab3.add(txtExcise);
        txtExcise.setBounds(460, 130, 150, 20);

        jLabel3.setText("Prod Ind D");
        Tab3.add(jLabel3);
        jLabel3.setBounds(85, 330, 70, 20);

        lblOrdDDMMA.setText("Ord DDMM A");
        Tab3.add(lblOrdDDMMA);
        lblOrdDDMMA.setBounds(370, 240, 90, 20);

        txtInvoiceDate.setEnabled(false);
        Tab3.add(txtInvoiceDate);
        txtInvoiceDate.setBounds(460, 10, 140, 19);

        txtWPSC.setEnabled(false);
        Tab3.add(txtWPSC);
        txtWPSC.setBounds(460, 70, 150, 19);

        jTabbedPane1.addTab("Other Information", null, Tab3, "");

        jPanel3.setLayout(null);

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setToolTipText("");
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Hierarchy :");
        jPanel3.add(jLabel31);
        jLabel31.setBounds(5, 13, 100, 15);

        cmbHierarchy.setEnabled(false);
        cmbHierarchy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHierarchyItemStateChanged(evt);
            }
        });

        jPanel3.add(cmbHierarchy);
        cmbHierarchy.setBounds(110, 13, 270, 24);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("From :");
        jPanel3.add(jLabel32);
        jLabel32.setBounds(5, 43, 100, 15);

        txtFrom.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFrom);
        txtFrom.setBounds(110, 43, 270, 22);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Remarks :");
        jPanel3.add(jLabel35);
        jLabel35.setBounds(5, 76, 100, 15);

        txtFromRemarks.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.add(txtFromRemarks);
        txtFromRemarks.setBounds(110, 73, 518, 22);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Your Action :");
        jPanel3.add(jLabel36);
        jLabel36.setBounds(5, 116, 100, 15);

        jPanel6.setLayout(null);

        jPanel6.setBorder(new javax.swing.border.EtchedBorder());
        OpgApprove.setText("Approve & Forward");
        buttonGroup1.add(OpgApprove);
        OpgApprove.setEnabled(false);
        OpgApprove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpgApproveMouseClicked(evt);
            }
        });

        jPanel6.add(OpgApprove);
        OpgApprove.setBounds(6, 6, 169, 23);

        OpgFinal.setText("Final Approve");
        buttonGroup1.add(OpgFinal);
        OpgFinal.setEnabled(false);
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
        jPanel6.setBounds(110, 113, 182, 100);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Send To :");
        jPanel3.add(jLabel33);
        jLabel33.setBounds(5, 226, 100, 15);

        cmbSendTo.setEnabled(false);
        jPanel3.add(cmbSendTo);
        cmbSendTo.setBounds(110, 223, 270, 24);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Remarks :");
        jPanel3.add(jLabel34);
        jLabel34.setBounds(5, 262, 100, 15);

        txtToRemarks.setEnabled(false);
        jPanel3.add(txtToRemarks);
        txtToRemarks.setBounds(110, 259, 516, 22);

        cmdNext3.setLabel("Previous <<");
        cmdNext3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNext3ActionPerformed(evt);
            }
        });

        jPanel3.add(cmdNext3);
        cmdNext3.setBounds(640, 340, 120, 25);

        jTabbedPane1.addTab("Approval", null, jPanel3, "");

        jPanel5.setLayout(null);

        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel60.setText("Document Approval Status");
        jPanel5.add(jLabel60);
        jLabel60.setBounds(12, 10, 242, 15);

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
        jScrollPane2.setBounds(12, 40, 694, 144);

        jLabel19.setText("Document Update History");
        jPanel5.add(jLabel19);
        jLabel19.setBounds(13, 191, 182, 15);

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
        jScrollPane6.setViewportView(TableHS);

        jPanel5.add(jScrollPane6);
        jScrollPane6.setBounds(13, 207, 540, 143);

        cmdViewHistory.setText("View Revisions");
        cmdViewHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewHistoryActionPerformed(evt);
            }
        });

        jPanel5.add(cmdViewHistory);
        cmdViewHistory.setBounds(571, 206, 132, 24);

        cmdNormalView.setText("Back to Normal");
        cmdNormalView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdNormalViewActionPerformed(evt);
            }
        });

        jPanel5.add(cmdNormalView);
        cmdNormalView.setBounds(571, 238, 132, 24);

        cmdShowRemarks.setText("Show Remarks");
        cmdShowRemarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowRemarksActionPerformed(evt);
            }
        });

        jPanel5.add(cmdShowRemarks);
        cmdShowRemarks.setBounds(571, 272, 132, 24);

        txtAuditRemarks.setEnabled(false);
        jPanel5.add(txtAuditRemarks);
        txtAuditRemarks.setBounds(575, 304, 129, 19);

        jTabbedPane1.addTab("Status", jPanel5);

        getContentPane().add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 70, 780, 480);

    }//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtCommitedRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtRequestedRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtRequestedRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRequestedRemarksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRequestedRemarksActionPerformed

    private void txtRevisedCommDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRevisedCommDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRevisedCommDateActionPerformed

    private void txtRevisedCommDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRevisedCommDateFocusLost
        // TODO add your handling code here:
        try{             
             int dateDiff= EITLERPGLOBAL.compareDate(txtRevisedRequestedDate.getText(),txtRevisedCommDate.getText());                       
             System.out.println("days : "+dateDiff);
              
             if(dateDiff==1)
             {
               txtAgreedDate.setText(txtRevisedRequestedDate.getText());
               //rdbCommDate.disable();
               //rdbReqDate.disable();
             }
             else if (dateDiff==-1)
              {
               //txtAgreedDate.setText(txtRevisedCommDate.getText());
                  txtAgreedDate.setText("");
                  JOptionPane.showMessageDialog(frmfeltOrder.this,"Requested date must be larger than or equal to Commited date Otherwise Agreed Date will be blank. ","Requested Date and Commited Date Comparison",JOptionPane.YES_OPTION);
               //txtRevisedRequestedDate.requestFocus();
               //rdbCommDate.setSelected(true);
               //rdbReqDate.enable();               
             }  
             else{
                txtAgreedDate.setText(txtRevisedRequestedDate.getText()); 
             }
          }
        catch(Exception e){
            
        }        
    }//GEN-LAST:event_txtRevisedCommDateFocusLost

    private void txtRevisedRequestedDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRevisedRequestedDateFocusLost
try{             
             int dateDiff= EITLERPGLOBAL.compareDate(txtRevisedRequestedDate.getText(),txtRevisedCommDate.getText());                       
             System.out.println("days : "+dateDiff);
              
             if(dateDiff==1)
             {
               txtAgreedDate.setText(txtRevisedRequestedDate.getText());
               //rdbCommDate.disable();
               //rdbReqDate.disable();
             }
             else if (dateDiff==-1)
              {
               //txtAgreedDate.setText(txtRevisedCommDate.getText());
               txtAgreedDate.setText("");
               JOptionPane.showMessageDialog(frmfeltOrder.this,"Requested date must be larger than or equal to Commited date Otherwise Agreed Date will be blank. ","Requested Date and Commited Date Comparison",JOptionPane.YES_OPTION);
               //txtRevisedRequestedDate.requestFocus();
               //rdbCommDate.setSelected(true);
               //rdbReqDate.enable();               
             } 
              else{
                txtAgreedDate.setText(txtRevisedRequestedDate.getText()); 
             }
          }
        catch(Exception e)
        {
          e.printStackTrace();
        }
    }//GEN-LAST:event_txtRevisedRequestedDateFocusLost

    private void txtRefNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRefNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRefNoActionPerformed

    private void txtConfNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConfNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtConfNoActionPerformed

    private void txtPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPositionActionPerformed

    private void txtMachineNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMachineNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMachineNoActionPerformed

    private void txtAgreedDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgreedDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgreedDateActionPerformed

    private void txtDelivDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDelivDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDelivDateActionPerformed

    private void txtRevisedRequestedDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRevisedRequestedDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRevisedRequestedDateActionPerformed

    private void txtCommDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommDateActionPerformed

    private void cmbPriorityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPriorityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPriorityActionPerformed

    private void txtDelivDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDelivDateFocusLost
               
       
    }//GEN-LAST:event_txtDelivDateFocusLost

  
    private void cmbPriorityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbPriorityFocusLost
        try{    
                String strSQL="";
                ResultSet rsTmp;
                strSQL= "";
                strSQL+="SELECT PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER WHERE PRIORITY_ID = "+EITLERPGLOBAL.getComboCode(cmbPriority)+"";
                //System.out.println(strSQL);
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                //txtSetPriority.setText(rsTmp.getString("PRIORITY_DESC"));
                txtSetPriority.setText(txtLastPriority.getText()+"-"+rsTmp.getString("PRIORITY_DESC"));
        }
        catch(Exception e){
        }
    }//GEN-LAST:event_cmbPriorityFocusLost

    private void cmdLastUsrBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastUsrBigActionPerformed
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtlastpriorityuser;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdLastUsrBigActionPerformed

    private void cmdRemarksBigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemarksBigActionPerformed
        BigEdit bigEdit=new BigEdit();
        bigEdit.theText=txtRemarks;
        bigEdit.ShowEdit();
    }//GEN-LAST:event_cmdRemarksBigActionPerformed

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
        //Set Default Send to User
    }//GEN-LAST:event_cmbHierarchyItemStateChanged

    private void OpgApproveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgApproveMouseClicked
        // TODO add your handling code here:
        String PartyID=txtPartyCode.getText().trim();
        
        SetupApproval();
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            GenerateRejectedUserCombo();
            if(ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID,clsOrderParty.ModuleID,PartyID)) {
                cmbSendTo.setEnabled(true);
            }
            else {
                cmbSendTo.setEnabled(false);
            }
        }
        
        if(cmbSendTo.getItemCount()<=0) {
            GenerateFromCombo();
        }
    }//GEN-LAST:event_OpgApproveMouseClicked

    private void OpgFinalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgFinalMouseClicked
        // TODO add your handling code here:
        if(!OpgFinal.isEnabled()) {
            OpgHold.setSelected(true);
        }
    }//GEN-LAST:event_OpgFinalMouseClicked

    private void OpgRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpgRejectMouseClicked
        // TODO add your handling code here:
        OpgApprove.setSelected(false);
        OpgFinal.setSelected(false);
        OpgReject.setSelected(true);
        OpgHold.setSelected(false);
        
        GenerateRejectedUserCombo();
        cmbSendTo.setEnabled(true);
    }//GEN-LAST:event_OpgRejectMouseClicked

    private void cmdNext3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext3ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext3ActionPerformed

    private void cmdViewHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewHistoryActionPerformed
        // TODO add your handling code here:
        String DocNo=txtPartyCode.getText();
        ObjSalesParty.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
        MoveFirst();
    }//GEN-LAST:event_cmdNormalViewActionPerformed

    private void cmdShowRemarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowRemarksActionPerformed
        // TODO add your handling code here:
        if(TableHS.getRowCount()>0&&TableHS.getSelectedRow()>=0) {
            txtAuditRemarks.setText((String)TableHS.getValueAt(TableHS.getSelectedRow(),4));
            BigEdit bigEdit=new BigEdit();
            bigEdit.theText=txtAuditRemarks;
            bigEdit.ShowEdit();
        }
    }//GEN-LAST:event_cmdShowRemarksActionPerformed
    private void DisplayAgentAplha() {
    /*if (!txtPartyCode.getText().trim().equals("")) {
            String Partycode = txtPartyCode.getText().trim().substring(0,2);
            String AgentAlpha = data.getStringValueFromDB("SELECT AREA_ID FROM D_SAL_PARTY_MASTER WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND PARTY_CODE='"+Partycode+"0000' ");
            txtAreaID.setText(AgentAlpha);
        }*/
    }                            
    private void cmdNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_cmdNext2ActionPerformed
    
    private void cmdNext4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext4ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_cmdNext4ActionPerformed
    
    private void cmdNext4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdNext4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext4KeyPressed
                                    
    private void cmdTopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTopActionPerformed
        // TODO add your handling code here:
        MoveFirst();
    }//GEN-LAST:event_cmdTopActionPerformed
    
    private void cmdBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBackActionPerformed
        // TODO add your handling code here:
        MovePrevious();
    }//GEN-LAST:event_cmdBackActionPerformed
    
    private void cmdNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNextActionPerformed
        // TODO add your handling code here:
        MoveNext();
    }//GEN-LAST:event_cmdNextActionPerformed
    
    private void cmdLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLastActionPerformed
        // TODO add your handling code here:
        MoveLast();
    }//GEN-LAST:event_cmdLastActionPerformed
    
    private void cmdNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNewActionPerformed
        // TODO add your handling code here:
        Add();
    }//GEN-LAST:event_cmdNewActionPerformed
    
    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        // TODO add your handling code here:
        Edit();
    }//GEN-LAST:event_cmdEditActionPerformed
    
    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        Delete();
    }//GEN-LAST:event_cmdDeleteActionPerformed
    
    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        // TODO add your handling code here:
        Save();
    }//GEN-LAST:event_cmdSaveActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancel();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdFilterActionPerformed
        // TODO add your handling code here:
        Find();
    }//GEN-LAST:event_cmdFilterActionPerformed
    
    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        // TODO add your handling code here:
        //ObjColumn.Close();
        ObjSalesParty.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
            
    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed
        
    private void cmdNext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdNext1KeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmdNext1KeyPressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JPanel Tab3;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbPriority;
    private javax.swing.JComboBox cmbPriority1;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdLastUsrBig;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext2;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNext4;
    private javax.swing.JButton cmdNext5;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemarksBig;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.ButtonGroup grpSisterConcern;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblBaseAmt;
    private javax.swing.JLabel lblDiscount;
    private javax.swing.JLabel lblExcise;
    private javax.swing.JLabel lblInvoiceAmt;
    private javax.swing.JLabel lblInvoiceDate;
    private javax.swing.JLabel lblInvoiceNo;
    private javax.swing.JLabel lblOrdDDMMA;
    private javax.swing.JLabel lblOrderDDMMB;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblRevNo1;
    private javax.swing.JLabel lblStationName;
    private javax.swing.JLabel lblStationName1;
    private javax.swing.JLabel lblStationName2;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAgreedDate;
    private javax.swing.JTextField txtApproxAmt;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtBaseAmt;
    private javax.swing.JTextField txtCommDate;
    private javax.swing.JTextField txtCommDate1;
    private javax.swing.JTextField txtCommitedRemarks;
    private javax.swing.JTextField txtConfNo;
    private javax.swing.JTextField txtConfNo1;
    private javax.swing.JTextField txtDelivDate;
    private javax.swing.JTextField txtDelivDate1;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtExcise;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGSQ;
    private javax.swing.JTextField txtGSQ1;
    private javax.swing.JTextField txtGroup;
    private javax.swing.JTextField txtGroup1;
    private javax.swing.JTextField txtInvoiceAmt;
    private javax.swing.JTextField txtInvoiceDate;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtLastPriority;
    private javax.swing.JTextField txtLength;
    private javax.swing.JTextField txtLength1;
    private javax.swing.JTextField txtMachineNo;
    private javax.swing.JTextField txtMachineNo1;
    private javax.swing.JTextField txtOrdDDMMA;
    private javax.swing.JTextField txtOrdDDMMB;
    private javax.swing.JTextField txtOrdDDMMC;
    private javax.swing.JTextField txtOrderCode;
    private javax.swing.JTextField txtOrderCode1;
    private javax.swing.JTextField txtOrderDate;
    private javax.swing.JTextField txtOrderDate1;
    private javax.swing.JTextField txtOther;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyCode1;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtPieceNo1;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtPosition1;
    private javax.swing.JTextField txtPrevRemarks;
    private javax.swing.JTextField txtProdInd;
    private javax.swing.JTextField txtProdIndA;
    private javax.swing.JTextField txtProdIndB;
    private javax.swing.JTextField txtProdIndC;
    private javax.swing.JTextField txtProdIndD;
    private javax.swing.JTextField txtProductCode;
    private javax.swing.JTextField txtProductCode1;
    private javax.swing.JTextField txtRefNo;
    private javax.swing.JTextField txtRefNo1;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtRequestedRemarks;
    private javax.swing.JTextField txtRevisedCommDate;
    private javax.swing.JTextField txtRevisedRequestedDate;
    private javax.swing.JTextField txtSeamCharge;
    private javax.swing.JTextField txtSetPriority;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtWPSC;
    private javax.swing.JTextField txtWeight;
    private javax.swing.JTextField txtWeight1;
    private javax.swing.JTextField txtWidth;
    private javax.swing.JTextField txtWidth1;
    private javax.swing.JTextField txtlastpriorityuser;
    private javax.swing.JTextField txtlastpriorityuser1;
    private javax.swing.JTextField txtprioritydate;
    private javax.swing.JTextField txtprioritydate1;
    // End of variables declaration//GEN-END:variables
    
    private void Add() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        EditMode=EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();
        //GenerateDeptCombo();
            
        //int Counter=data.getIntValueFromDB("SELECT MAX(CONVERT(SUBSTR(PARTY_CODE,5),SIGNED))+1 FROM D_SAL_PARTY_MASTER_H WHERE PARTY_CODE LIKE 'N%'");
        //txtPartyCode.setText("NEWD"+Counter);
        lblTitle.setBackground(Color.BLUE);
        
       //txtInsuranceCode.setText("01");
        //Object Obj = "09";
               Object Obj = "Other";
        
    }
    
    private void SetupApproval() {
        
        if(cmbHierarchy.getItemCount()>1) {
            cmbHierarchy.setEnabled(true);
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
            
            int FromUserID=ApprovalFlow.getFromID((int)EITLERPGLOBAL.gCompanyID, clsOrderParty.ModuleID,(String)ObjSalesParty.getAttribute("PARTY_CODE").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=ApprovalFlow.getFromRemarks( (int)EITLERPGLOBAL.gCompanyID,clsOrderParty.ModuleID,FromUserID,(String)ObjSalesParty.getAttribute("PARTY_CODE").getObj());
            
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
                
                String DocNo=(String)ObjSalesParty.getAttribute("PARTY_CODE").getObj();
                
                List=ApprovalFlow.getRemainingUsers((int)EITLERPGLOBAL.gCompanyID, clsOrderParty.ModuleID,DocNo);
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
    
    private void SetFields(boolean pStat) {
       /* txtPieceNo.setEnabled(pStat);
        txtOrderDate.setEnabled(pStat);
        txtPartyCode.setEnabled(pStat);
        txtProductCode.setEnabled(pStat);*/
        //Editing rights to Aditya Sir start
         //if(EITLERPGLOBAL.gUserID==243){
        //txtDelivDate.setEnabled(pStat);
       // txtCommDate.setEnabled(pStat);
        //txtAgreedDate.setEnabled(pStat);        
         //}         
        //if(EITLERPGLOBAL.gUserID==47){
        //txtDelivDate.setEnabled(pStat);
        //txtCommDate.setEnabled(pStat);
        //txtAgreedDate.setEnabled(pStat);        
         //}
         
          //if(EITLERPGLOBAL.gUserID==59){
       // txtDelivDate.setEnabled(pStat);
        //txtCommDate.setEnabled(pStat);
         //txtProductCode.setEnabled(pStat);
        //txtDelivDate.setEnabled(pStat);
        //txtAgreedDate.setEnabled(pStat);        
         //}         
         //txtAgreedDate.setEnabled(pStat);        
         //Editing rights to Aditya Sir End
         //Editing rights to Shanbaug Start
         //if(EITLERPGLOBAL.gUserID==28){
        txtPartyCode.setEnabled(pStat);
        txtProductCode.setEnabled(pStat);
        //txtDelivDate.setEnabled(pStat);
        txtRevisedRequestedDate.setEnabled(pStat);
        txtRevisedCommDate.setEnabled(pStat);
        txtRequestedRemarks.setEnabled(pStat);
        txtCommitedRemarks.setEnabled(pStat);
         //}
         
        //Editing rights to Shanbaug End
        txtMachineNo.setEnabled(pStat);
        txtPosition.setEnabled(pStat);
        cmbPriority.setEnabled(pStat);
        //txtprioritydate.setEnabled(pStat);
        txtConfNo.setEnabled(pStat);
        txtRefNo.setEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        //txtPrevRemarks.setEnabled(pStat);
       /* txtOrderCode.setEnabled(pStat);
        
        txtLength.setEnabled(pStat);
        txtWidth.setEnabled(pStat);
        txtWeight.setEnabled(pStat);*/
        txtGSQ.setEnabled(pStat);
        /*txtGroup.setEnabled(pStat);                    
        txtInvoiceNo.setEnabled(pStat);        
        txtInvoiceDate.setEnabled(pStat);
        txtInvoiceAmt.setEnabled(pStat);
        txtBaseAmt.setEnabled(pStat);
        txtWPSC.setEnabled(pStat);
        txtOther.setEnabled(pStat);
        txtDiscount.setEnabled(pStat);
        txtSeamCharge.setEnabled(pStat);
        txtExcise.setEnabled(pStat);
        txtApproxAmt.setEnabled(pStat);
        txtProdInd.setEnabled(pStat);
        txtProdIndA.setEnabled(pStat);
        txtOrdDDMMA.setEnabled(pStat);
        txtProdIndB.setEnabled(pStat);
        txtOrdDDMMB.setEnabled(pStat);
        txtProdIndC.setEnabled(pStat);
        txtOrdDDMMC.setEnabled(pStat);
        txtProdIndD.setEnabled(pStat);*/
                
        OpgApprove.setEnabled(pStat);
        OpgFinal.setEnabled(pStat);
        OpgReject.setEnabled(pStat);
        OpgHold.setEnabled(pStat);
                
        SetupApproval();             
    }
    
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
    
    private boolean Validate() {
        int ValidEntryCount=0;
        
        if(txtRequestedRemarks.getText().trim().equals("")){
            if(!txtDelivDate.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Please enter reason for Revised Requested Date");
            txtRequestedRemarks.requestFocus(true);
            return false;
            }
        }
        
        if(txtCommitedRemarks.getText().trim().equals("")){
            if(!txtDelivDate.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Please enter reason for Revised Commited Date");
            txtCommitedRemarks.requestFocus(true);
            return false;
        }
        }
        
        if (txtPartyCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtPartyCode.requestFocus(true);
            return false;
        }
        //Now Header level validations
        if(txtPartyCode.getText().trim().equals("")&&OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtPartyCode.requestFocus(true);
            return false;
        }
        
        
        if(!txtPartyCode.getText().trim().equals("")) {
            if(EditMode==EITLERPGLOBAL.ADD) {
                
                if (data.IsRecordExist("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"'")) {
                    JOptionPane.showMessageDialog(null,"Party Code already exists!!");
                    txtPartyCode.requestFocus(true);
                    return false;
                }
                
            }
            /*if(EditMode==EITLERPGLOBAL.EDIT) {
                int MainCode = EITLERPGLOBAL.getComboCode(cmbMainCodeType);
                if (!data.IsRecordExist("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND CANCELLED = 0 AND APPROVED = 0 ")) {
                    JOptionPane.showMessageDialog(null,"Party Code already exists!!");
                    txtPartyCode.requestFocus(true);
                    return false;
                }
            }*/
        }
        
        if(txtPieceNo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Piece Number");
            return false;
        }    
        
        //if(txtLastPriority.getText().equals("3")){
            if(txtRemarks.getText().equals(""))
            {
                if(!txtDelivDate.getText().trim().equals("")){
                //JOptionPane.showMessageDialog(null,"Please enter reason for High -> Low");
                JOptionPane.showMessageDialog(null,"Please enter reason for Changing Priority");
                return false;
                }
            }
        //}
        
        
        //        if(txtBankID.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(null,"Please enter Bank ID");
        //            return false;
        //        }
        //
        //        if(txtBankAdd.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(null,"Please enter Bank Address");
        //            return false;
        //        }
        //
        //        if(txtBankCity.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(null,"Please enter Bank City");
        //            return false;
        //        }
        
        //        if(txtBankName.getText().trim().equals("")) {
        //            JOptionPane.showMessageDialog(null,"Please Select Bank");
        //            return false;
        //        }
        
       /* if(txtAmountLimit.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Amount Limit");
            return false;
        }*/
        
        /*if(txtTranID.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Transporter ID");
            return false;
        }*/
        
        /*if(!txtCSTDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtCSTDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid CST Date");
                return false;
            }
        }*/
        
       /* if(!txtECCDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtECCDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid ECC Date");
                return false;
            }
        }*/
        
       /* if(!txtTinDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtTinDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid Tin Date");
                return false;
            }
        }*/
        
        /*if(txtPANNo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Insert Pan No.");
            return false;
        }*/
        
        /*if(!txtPANDate.getText().trim().equals("")) {
            if(!EITLERPGLOBAL.isDate(txtPANDate.getText())) {
                JOptionPane.showMessageDialog(null,"Invalid PAN Date");
                return false;
            }
        }*/
        /*
        if(cmbHierarchy.getSelectedIndex()==-1) {
            JOptionPane.showMessageDialog(null,"Please select the hierarchy.");
            return false;
        }
        
        if((!OpgApprove.isSelected())&&(!OpgReject.isSelected())&&(!OpgFinal.isSelected())&&(!OpgHold.isSelected())) {
            JOptionPane.showMessageDialog(null,"Please select the Approval Action");
            return false;
        }
        
        if(OpgReject.isSelected()&&txtToRemarks.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter the remarks for rejection");
            return false;
        }
        
        if( (OpgApprove.isSelected()||OpgReject.isSelected())&&cmbSendTo.getItemCount()<=0) {
            JOptionPane.showMessageDialog(null,"Please select the user, to whom rejected document to be send");
            return false;
        }
        
        if (OpgFinal.isSelected()) {
            if (txtPartyCode.getText().trim().substring(0,4).equals("NEWD")) {
                JOptionPane.showMessageDialog(null,"Invalid Party Code");
                txtPartyCode.requestFocus(true);
                return false;
            }
        }
        */
        return true;
    }
    
    private void ClearFields() {
        txtPieceNo.setText("");
        txtOrderDate.setText("");
        txtPartyCode.setText("");
        txtProductCode.setText("");
        txtMachineNo.setText("");
        txtPosition.setText("");
        cmbPriority.setSelectedIndex(0);
        txtLastPriority.setText("");
        //cmbPriority.setSelectedIndex(0);
        txtOrderCode.setText("");
        txtRefNo.setText("");
        txtLength.setText("");
        txtWidth.setText("");
        txtWeight.setText("");
        txtGSQ.setText("");
        txtDelivDate.setText("");
        txtCommDate.setText("");
        txtAgreedDate.setText("");
        txtGroup.setText("");
        txtConfNo.setText("");       
        txtInvoiceNo.setText("");
        txtInvoiceDate.setText("");
        txtInvoiceAmt.setText("");
        txtBaseAmt.setText("");
        txtWPSC.setText("");
        txtOther.setText("");
        txtDiscount.setText("");
        txtSeamCharge.setText("");
        txtExcise.setText("");
        txtApproxAmt.setText("");        
        txtProdInd.setText("");
        txtProdIndA.setText("");
        txtProdIndB.setText("");
        txtProdIndC.setText("");
        txtProdIndD.setText("");        
        txtOrdDDMMA.setText("");
        txtOrdDDMMB.setText("");
        txtOrdDDMMC.setText("");
        txtRemarks.setText("");
        txtPrevRemarks.setText("");
        txtprioritydate.setText("");
        txtlastpriorityuser.setText("");
        txtRevisedCommDate.setText("");
        txtRevisedRequestedDate.setText("");
        txtRequestedRemarks.setText("");
        txtCommitedRemarks.setText("");
        
        
        FormatGridHS();
        FormatGrid();
        GenerateGrid();
    }
    
    private void Edit() {
        //== Financial Year Validation-------------//
        /*if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }*/
        //----------------------------------//
        String sPartyCode=ObjSalesParty.getAttribute("PARTY_CD").getString();
      //if(ObjSalesParty.IsEditable(EITLERPGLOBAL.gCompanyID, sPartyCode, EITLERPGLOBAL.gNewUserID)) {
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            //GenerateCombos();
            DisplayData();
            //----------------//
            
           // if(ApprovalFlow.IsCreator(clsOrderParty.ModuleID,sPartyCode)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11512)) {
                SetFields(true);
           // }
           // else {
           //     EnableApproval();
           // }
            
            DisplayData();
            DisableToolbar();
       // }
       // else {
       //     JOptionPane.showMessageDialog(null,"You cannot edit this record. \n It is either approved/rejected or waiting approval for other user");
       // }
    }
    
    private void Delete() {
        //== Financial Year Validation-------------//
        if(!EITLERPGLOBAL.YearIsOpen) {
            JOptionPane.showMessageDialog(null,"The year is closed. You cannot enter/edit any transaction");
            return;
        }
        //----------------------------------//
        String sPartyCode=ObjSalesParty.getAttribute("PARTY_CODE").getString();
        
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if(ObjSalesParty.CanDelete(EITLERPGLOBAL.gCompanyID, sPartyCode)) {
                if(ObjSalesParty.Delete()) {
                    ObjSalesParty.LoadData(EITLERPGLOBAL.gCompanyID);
                    MoveLast();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is "+ObjSalesParty.LastError);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"You cannot delete this record. \n It is either approved/rejected record or waiting approval for other user or is referred in other documents");
            }
        }
    }
    
    private void Save() {
        //Form level validations        
        if(txtDelivDate.getText().trim().equals("")){            
            txtDelivDate.setText(txtRevisedRequestedDate.getText());            
        }
        if(txtCommDate.getText().trim().equals("")){
            txtCommDate.setText(txtRevisedCommDate.getText());
        }
        if(Validate()==false) {
            return; //Validation failed
        }
        
        SetData();  
      
        if(EditMode==EITLERPGLOBAL.ADD) {
            System.out.println("ADD");
            if(ObjSalesParty.Insert()) {              
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjSalesParty.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
             System.out.println("EDIT");
            if(ObjSalesParty.Update()) {
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjSalesParty.LastError);
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
        Loader ObjLoader=new Loader(this,"EITLERP.Production.ReportUI.frmOrderFind",true);
        frmOrderFind ObjReturn= (frmOrderFind) ObjLoader.getObj();
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjSalesParty.Filter(ObjReturn.strQuery)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveFirst();
        }
    }
    
    private void MoveFirst() {
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjSalesParty.MovePrevious();
        DisplayData();
    }
    
    
    private void MoveNext() {
        ObjSalesParty.MoveNext();
        DisplayData();
    }
    
    
    private void MoveLast() {
        ObjSalesParty.MoveLast();
        DisplayData();
    }
    
    //Didplay data on the Screen
    private void DisplayData() {
      //=========== Color Indication ===============//
      /*  try {
            if(EditMode==0) {
                if(ObjSalesParty.getAttribute("APPROVED").getInt()==1) {
                    
                    lblTitle.setBackground(Color.BLUE);
                }
                
                if(ObjSalesParty.getAttribute("APPROVED").getInt()!=1) {
                    lblTitle.setBackground(Color.GRAY);
                }
                
                if(ObjSalesParty.getAttribute("CANCELLED").getInt()==1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
        }
        catch(Exception c) {
            
        }*/
        //============================================//
        
        
        //========= Authority Delegation Check =====================//
        /*if(EITLERPGLOBAL.gAuthorityUserID!=EITLERPGLOBAL.gUserID) {
            int ModuleID=clsOrderParty.ModuleID;
            
            if(clsAuthority.IsAuthorityGiven(EITLERPGLOBAL.gCompanyID,EITLERPGLOBAL.gUserID,EITLERPGLOBAL.gAuthorityUserID,ModuleID)) {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gAuthorityUserID;
            }
            else {
                EITLERPGLOBAL.gNewUserID=EITLERPGLOBAL.gUserID;
            }
        }*/
        //==========================================================//
                 
        try {
            ClearFields();
            boolean bState = false;
            String st;
            lblTitle.setText("ORDER - " + (String)ObjSalesParty.getAttribute("PARTY_CD").getObj());
            txtPieceNo.setText((String)ObjSalesParty.getAttribute("PIECE_NO").getObj());                     
            txtOrderDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("ORDER_DATE").getString()));           
            txtPartyCode.setText((String)ObjSalesParty.getAttribute("PARTY_CD").getObj());
            txtProductCode.setText((String)ObjSalesParty.getAttribute("PRODUCT_CODE").getObj()); 
            txtRevisedCommDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("REVCOMMITED_DATE").getString())); 
            txtMachineNo.setText(ObjSalesParty.getAttribute("MACHINE_NO").getString());
            txtPosition.setText(ObjSalesParty.getAttribute("POSITION").getString());
            
            EITLERPGLOBAL.setComboIndex(cmbPriority,(int)ObjSalesParty.getAttribute("PRIORITY").getVal());
            //txtLastPriority.setText((String)ObjSalesParty.getAttribute("PRIORITY_DESC").getObj());           
            try{    
                String strSQL="";
                ResultSet rsTmp;
                strSQL= "";
                strSQL+="SELECT PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER WHERE PRIORITY_ID = "+EITLERPGLOBAL.getComboCode(cmbPriority)+"";
                //strSQL+="SELECT PRIORITY_DESC FROM PRODUCTION.FELT_PRIORITY_MASTER WHERE PRIORITY_ID = "+(int)ObjSalesParty.getAttribute("PRIORITY").getVal()+"";
                //System.out.println(strSQL);
                rsTmp=data.getResult(strSQL);
                rsTmp.first();
                txtLastPriority.setText(rsTmp.getString("PRIORITY_DESC"));
                
        }
        catch(Exception e){
        }
            //st = Integer.toString((int)ObjSalesParty.getAttribute("PRIORITY").getVal());
            //txtLastPriority.setText(st);
            //EITLERPGLOBAL.setComboIndex(cmbPriority,(String)ObjSalesParty.getAttribute("PRIORITY").getObj());
            //EITLERPGLOBAL.setComboIndex(cmbPriority,(String)ObjSalesParty.getAttribute("PRIORITY").getObj());
            //EITLERPGLOBAL.setComboIndex(cmbPriority,(String)ObjSalesParty.getAttribute("PRIORITY").getObj());
            //System.out.println(ObjSalesParty.getAttribute("PRIORITY").getInt());
           
            //System.out.println((int)ObjSalesParty.getAttribute("PRIORITY").getVal());
            System.out.println((int)ObjSalesParty.getAttribute("PRIORITY").getVal());
            //EITLERPGLOBAL.setComboIndex(cmbPriority,ObjSalesParty.getAttribute("PRIORITY").getString());
            txtOrderCode.setText(ObjSalesParty.getAttribute("ORDER_CODE").getString());            
            txtRefNo.setText(ObjSalesParty.getAttribute("REF_NO").getString());
            txtLength.setText((String)ObjSalesParty.getAttribute("LNGTH").getObj());
            txtWidth.setText((String)ObjSalesParty.getAttribute("WIDTH").getObj());
            txtWeight.setText((String)ObjSalesParty.getAttribute("WEIGHT").getObj());
            txtGSQ.setText((String)ObjSalesParty.getAttribute("GSQ").getObj());
            txtDelivDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("DELIV_DATE").getString()));
            txtCommDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("COMM_DATE").getString()));
            txtRevisedCommDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("REV_COMM_DATE").getString()));
            txtRevisedRequestedDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("REV_REQ_DATE").getString()));
            txtAgreedDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("AGREED_DATE").getString()));
            txtGroup.setText((String)ObjSalesParty.getAttribute("GRUP").getObj());
            txtConfNo.setText((String)ObjSalesParty.getAttribute("CONF_NO").getString());
            
           //lblRevNo.setText(Integer.toString(ObjSalesParty.getAttribute("REVISION_NO").getInt()));
            txtInvoiceNo.setText(ObjSalesParty.getAttribute("INV_NO").getString());            
            txtInvoiceDate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("INV_DATE").getString()));
            txtInvoiceAmt.setText(Double.toString(ObjSalesParty.getAttribute("INV_AMOUNT").getDouble()));            
            txtBaseAmt.setText(Double.toString(ObjSalesParty.getAttribute("BAS_AMT").getDouble()));
            txtWPSC.setText(Double.toString(ObjSalesParty.getAttribute("WPSC").getDouble()));
            txtOther.setText(Double.toString(ObjSalesParty.getAttribute("OTHER").getDouble()));
            txtDiscount.setText(Double.toString(ObjSalesParty.getAttribute("DISCOUNT").getDouble()));
            txtSeamCharge.setText(Double.toString(ObjSalesParty.getAttribute("SEAM_CHG").getDouble()));
            txtExcise.setText(Double.toString(ObjSalesParty.getAttribute("EXCISE").getDouble()));
            txtApproxAmt.setText(ObjSalesParty.getAttribute("APPROX_AMOUNT").getString());
            txtProdInd.setText(ObjSalesParty.getAttribute("PROD_IND").getString());
            txtProdIndA.setText(ObjSalesParty.getAttribute("PROD_IND_A").getString());
            txtProdIndB.setText(ObjSalesParty.getAttribute("PROD_IND_B").getString());
            txtProdIndC.setText(ObjSalesParty.getAttribute("PROD_IND_C").getString());
            txtProdIndD.setText(ObjSalesParty.getAttribute("PROD_IND_D").getString());
            txtOrdDDMMA.setText(ObjSalesParty.getAttribute("ORD_DDMM_A").getString());
            txtOrdDDMMB.setText(ObjSalesParty.getAttribute("ORD_DDMM_B").getString());
            txtOrdDDMMC.setText(ObjSalesParty.getAttribute("ORD_DDMM_C").getString());
            //txtRemarks.setText(ObjSalesParty.getAttribute("REMARKS").getString());
            txtPrevRemarks.setText(ObjSalesParty.getAttribute("REMARKS").getString());         
            txtprioritydate.setText(EITLERPGLOBAL.formatDate(ObjSalesParty.getAttribute("PRIORITY_DATE").getString()));           
            txtlastpriorityuser.setText((String)ObjSalesParty.getAttribute("LAST_PRIO_USR").getObj());
            //txtlastpriorityuser.setText((String)ObjSalesParty.getAttribute("LAST_PRIO_USR").getObj()+"-"+EITLERPGLOBAL.gLoginID);
            //EITLERPGLOBAL.setComboIndex(cmbChargeCode,"0"+ObjSalesParty.getAttribute("CHARGE_CODE").getString());
            //EITLERPGLOBAL.setComboIndex(cmbSecondChargeCode,ObjSalesParty.getAttribute("CHARGE_CODE_II").getString());
            
            //EITLERPGLOBAL.setComboIndex(cmbPriority,ObjSalesParty.getAttribute("PRIORITY").getString());        
            EITLERPGLOBAL.setComboIndex(cmbHierarchy,ObjSalesParty.getAttribute("HIERARCHY_ID").getInt());
            
            //EITLERPGLOBAL.setComboIndex(cmbPriority,(int)ObjSalesParty.getAttribute("PRIORITY").getVal());
            
            //===================Fill up Table===================//
            FormatGrid();
            GenerateGrid();
            
            String MainCode = ObjSalesParty.getAttribute("ACCOUNT_CODES").getString();
            if (!MainCode.trim().equals("")) {
                String sMainCode[] = MainCode.trim().split(",");                             
            }
            else {
                String str = "SELECT MAIN_ACCOUNT_CODE FROM D_FIN_PARTY_MASTER "+
                " WHERE PARTY_CODE = '"+txtPartyCode.getText().trim()+"' AND APPROVED=1 AND CANCELLED=0";
                ResultSet rsParty = data.getResult(str,FinanceGlobal.FinURL);
                rsParty.first();
                if (rsParty.getRow()>0) {
                    while (!rsParty.isAfterLast()) {
                        String Code = rsParty.getString("MAIN_ACCOUNT_CODE").trim();
                        rsParty.next();
                    }
                }
            }
            //==================================================//
            
            //======== Generating Grid for Document Approval Flow ========//
            FormatGridA();
            HashMap List=new HashMap();
            
            String DocNo=ObjSalesParty.getAttribute("PARTY_CODE").getString();
            List=ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, clsOrderParty.ModuleID, DocNo);
            for(int i=1;i<=List.size();i++) {
                clsDocFlow ObjFlow=(clsDocFlow)List.get(Integer.toString(i));
                Object[] rowData=new Object[7];
                
                rowData[0]=Integer.toString(i);
                rowData[1]=clsUser.getUserName(EITLERPGLOBAL.gCompanyID,(int)ObjFlow.getAttribute("USER_ID").getVal());
                rowData[2]=(String)ObjFlow.getAttribute("STATUS").getObj();
                //rowData[3]=clsDepartment.getDeptName(EITLERPGLOBAL.gCompanyID, (int)ObjFlow.getAttribute("DEPT_ID").getVal());
                rowData[4]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("RECEIVED_DATE").getObj());
                rowData[5]=EITLERPGLOBAL.formatDate((String)ObjFlow.getAttribute("ACTION_DATE").getObj());
                rowData[6]=(String)ObjFlow.getAttribute("REMARKS").getObj();
                
                DataModelA.addRow(rowData);
            }
            //============================================================//
            
            //Showing Audit Trial History
            FormatGridHS();
            String PartyID=(String)ObjSalesParty.getAttribute("PARTY_CODE").getObj();
            HashMap History=clsOrderParty.getHistoryList(EITLERPGLOBAL.gCompanyID, PartyID);
            for(int i=1;i<=History.size();i++) {
                clsOrderParty ObjHistory=(clsOrderParty)History.get(Integer.toString(i));
                Object[] rowData=new Object[5];
                
                rowData[0]=Integer.toString((int)ObjHistory.getAttribute("REVISION_NO").getVal());
                rowData[1]=ObjHistory.getAttribute("UPDATED_BY").getString();
                rowData[2]=ObjHistory.getAttribute("ENTRY_DATE").getString();
                
                String ApprovalStatus="";
                
                        if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("A")) {
                    ApprovalStatus="Approved";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("F")) {
                    ApprovalStatus="Final Approved";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("W")) {
                    ApprovalStatus="Waiting";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("R")) {
                    ApprovalStatus="Rejected";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("P")) {
                    ApprovalStatus="Pending";
                }
                
                if((ObjHistory.getAttribute("APPROVAL_STATUS").getString()).equals("C")) {
                    ApprovalStatus="Skiped";
                }
                
                
                rowData[3]=ApprovalStatus;
                rowData[4]=ObjHistory.getAttribute("APPROVER_REMARKS").getString();
                
                DataModelHS.addRow(rowData);
            }
            
            //=========================================//
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Display Data Error: " + e.getMessage());
        }
    }
    
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjSalesParty.setAttribute("PIECE_NO",txtPieceNo.getText());
      //ObjSalesParty.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjSalesParty.setAttribute("ORDER_DATE",txtOrderDate.getText());
        ObjSalesParty.setAttribute("PARTY_CD",txtPartyCode.getText());
        ObjSalesParty.setAttribute("PRODUCT_CODE",txtProductCode.getText());
        ObjSalesParty.setAttribute("MACHINE_NO",txtMachineNo.getText());
        ObjSalesParty.setAttribute("POSITION",txtPosition.getText());
        ObjSalesParty.setAttribute("PRIORITY",EITLERPGLOBAL.getComboCode(cmbPriority));
        
      //ObjSalesParty.setAttribute("PRIORITY",EITLERPGLOBAL.getComboCode(cmbPriority));
      //ObjSalesParty.setAttribute("PRIORITY",EITLERPGLOBAL.getCombostrCode(cmbPriority));
        ObjSalesParty.setAttribute("ORDER_CODE",txtOrderCode.getText());
        ObjSalesParty.setAttribute("REF_NO",txtRefNo.getText());
        ObjSalesParty.setAttribute("LNGHT",txtLength.getText());
               
        ObjSalesParty.setAttribute("WIDTH",txtWidth.getText());
        ObjSalesParty.setAttribute("WEIGHT",txtWeight.getText());
        ObjSalesParty.setAttribute("GSQ",txtGSQ.getText());
        ObjSalesParty.setAttribute("DELIV_DATE",EITLERPGLOBAL.formatDateDB(txtDelivDate.getText()));
        ObjSalesParty.setAttribute("COMM_DATE",EITLERPGLOBAL.formatDateDB(txtCommDate.getText()));
        ObjSalesParty.setAttribute("AGREED_DATE",EITLERPGLOBAL.formatDateDB(txtAgreedDate.getText()));
           
        ObjSalesParty.setAttribute("GRUP",txtGroup.getText());
        ObjSalesParty.setAttribute("CONF_NO",txtConfNo.getText());
        ObjSalesParty.setAttribute("INV_NO",txtInvoiceNo.getText());
        ObjSalesParty.setAttribute("INV_DATE",EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText()));
        ObjSalesParty.setAttribute("INV_AMOUNT",txtInvoiceAmt.getText());
        ObjSalesParty.setAttribute("BAS_AMT",txtBaseAmt.getText());
        ObjSalesParty.setAttribute("WPSC",txtWPSC.getText());
        ObjSalesParty.setAttribute("OTHER",txtOther.getText());
        ObjSalesParty.setAttribute("DISCOUNT",txtDiscount.getText());
        
        ObjSalesParty.setAttribute("SEAM_CHG",txtSeamCharge.getText());
        ObjSalesParty.setAttribute("EXCISE",txtExcise.getText());
        ObjSalesParty.setAttribute("APPROX_AMOUNT",txtApproxAmt.getText());
        ObjSalesParty.setAttribute("PROD_IND",txtProdInd.getText());
        ObjSalesParty.setAttribute("PROD_IND_A",txtProdIndA.getText());
        ObjSalesParty.setAttribute("PROD_IND_B",txtProdIndB.getText());
        ObjSalesParty.setAttribute("PROD_IND_C",txtProdIndC.getText());
        ObjSalesParty.setAttribute("PROD_IND_D",txtProdIndD.getText());
        ObjSalesParty.setAttribute("ORD_DDMM_A",txtOrdDDMMA.getText());
        ObjSalesParty.setAttribute("ORD_DDMM_B",txtOrdDDMMB.getText());
        ObjSalesParty.setAttribute("ORD_DDMM_C",txtOrdDDMMC.getText());
      //ObjSalesParty.setAttribute("ORD_DDMM_D",txtRemarks.getText());
        //ObjSalesParty.setAttribute("REMARKS",txtPrevRemarks.getText()+","+txtSetPriority.getText()+":"+txtRemarks.getText());
        //JOptionPane.showMessageDialog(null,txtSetPriority.getText()+":"+txtRemarks.getText()+" | "+txtPrevRemarks.getText());
        //ObjSalesParty.setAttribute("REMARKS",txtRemarks.getText());
     //   txtSetPriority.getText()+":"+txtRemarks.getText()+" | "+txtPrevRemarks.getText());
        ObjSalesParty.setAttribute("REMARKS",txtSetPriority.getText().trim().toUpperCase()+":"+txtRemarks.getText().trim().toUpperCase()+" | "+txtPrevRemarks.getText().trim().toUpperCase());
        //ObjSalesParty.setAttribute("REMARKS",txtSetPriority.getText()+":"+txtRemarks.getText());
        ObjSalesParty.setAttribute("PRIORITY_DATE",EITLERPGLOBAL.formatDateDB(txtprioritydate.getText()));        
        ObjSalesParty.setAttribute("LAST_PRIO_USR",txtlastpriorityuser.getText());
        ObjSalesParty.setAttribute("REV_REQ_REASON",txtRequestedRemarks.getText());
        ObjSalesParty.setAttribute("REV_COMM_REASON",txtCommitedRemarks.getText());
        ObjSalesParty.setAttribute("REV_REQ_DATE",EITLERPGLOBAL.formatDateDB(txtRevisedRequestedDate.getText()));        
        ObjSalesParty.setAttribute("REV_COMM_DATE",EITLERPGLOBAL.formatDateDB(txtRevisedCommDate.getText()));        
        
        
        //ObjSalesParty.setAttribute("PRIORITY_DATE",EITLERPGLOBAL.formatDateDB(txtprioritydate.getText()));
        //--------------Merge Main Code------------------------//
       /* int SrNo=0;
               
        HashMap List=new HashMap();
        String str_Condition = "";
        
        str_Condition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
        List=clsSalesPartyMainCode.getList(str_Condition);
        
        //Scan the Table to get selected items
        for(int i=1;i<=List.size();i++) {
            clsSalesPartyMainCode ObjType=(clsSalesPartyMainCode) List.get(Integer.toString(i));
            SrNo=i;
                                   
        }
        // ObjSalesParty.setAttribute("ACCOUNT_CODES",MainCode);
        //-----------------------------------------------------//
        */
        //----- Update Approval Specific Fields -----------//
        /*ObjSalesParty.setAttribute("HIERARCHY_ID",EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjSalesParty.setAttribute("FROM",EITLERPGLOBAL.gNewUserID);
        ObjSalesParty.setAttribute("TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjSalesParty.setAttribute("REJECTED_REMARKS",txtToRemarks.getText());
        
        if(OpgApprove.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","A");
        }
        
        if(OpgFinal.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","F");
        }
        
        if(OpgReject.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","R");
            ObjSalesParty.setAttribute("SEND_DOC_TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        }
        
        if(OpgHold.isSelected()) {
            ObjSalesParty.setAttribute("APPROVAL_STATUS","H");
        }*/
        //-------------------------------------------------//
        
     /*   if(EditMode==EITLERPGLOBAL.ADD) {
            ObjSalesParty.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
            ObjSalesParty.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            ObjSalesParty.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        */
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
    
    private void SetMenuForRights() {
        // --- Add Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1151,11511)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1151,11513)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 1151,11514)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
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
                
                if(FieldName.trim().equals("PARTY_CODE")) {
                    int a=0;
                }
                if(clsHierarchy.CanEditField(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, SelHierarchy, "H", FieldName)) {
                    
                    Tab1.getComponent(i).setEnabled(true);
                }
                
            }
        }
        //=============== Header Fields Setup Complete =================//
        
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
    
    public void FindWaiting() {
        ObjSalesParty.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsOrderParty.ModuleID+")");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    
    public void FindEx(int pCompanyID,String pPartyCode,String pPieceNo) {
        System.out.println(pPartyCode);
        System.out.println(pPieceNo);
    //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        ObjSalesParty.Filter(" WHERE PARTY_CD='"+pPartyCode+"' AND PIECE_NO='"+pPieceNo+"'");
        ObjSalesParty.MoveFirst();
        DisplayData();
    }
    
    
    private void GenerateRejectedUserCombo() {
        
        
        HashMap List=new HashMap();
        HashMap DeptList=new HashMap();
        HashMap DeptUsers=new HashMap();
        String PartyCode=txtPartyCode.getText();
        
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
                    IncludeUser=ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, clsOrderParty.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    IncludeUser=ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, clsOrderParty.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String DocNo=(String)ObjSalesParty.getAttribute("PARTY_CODE").getObj();
            int Creator=ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, clsOrderParty.ModuleID, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
        
    }
    
    private void FormatGrid() {
       /* DataModelMainCode=new EITLTableModel();
                     
        for(int i=1;i<=4;i++) {
            DataModelMainCode.SetReadOnly(i);
        }
        
        //Add Columns to it
        DataModelMainCode.addColumn("*"); //0 Selection
        DataModelMainCode.addColumn("Sr.");//1
        DataModelMainCode.addColumn("Main Account Code");//2
        DataModelMainCode.addColumn("Account Name");//3        
        Rend.setCustomComponent(0,"CheckBox");       
        */
    }
    
    private void GenerateGrid() {
     /*   HashMap List=new HashMap();
        String str_Condition = "";
        
        str_Condition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
        List=clsSalesPartyMainCode.getList(str_Condition);
        
        for(int i=1;i<=List.size();i++) {
            clsSalesPartyMainCode ObjType=(clsSalesPartyMainCode) List.get(Integer.toString(i));
            HashMap colLot=ObjType.colTypeLot;
            
            Object[] rowData=new Object[4];
            
            rowData[0]=Boolean.valueOf(false); //By default not selected
            rowData[1]=Integer.toString(i);
            rowData[2]=ObjType.getAttribute("MAIN_ACCOUNT_CODE").getString();
            rowData[3]=ObjType.getAttribute("ACCOUNT_NAME").getString();
            
            DataModelMainCode.addRow(rowData);
            
            //Set the Collection
           
        }*/
    }
}
