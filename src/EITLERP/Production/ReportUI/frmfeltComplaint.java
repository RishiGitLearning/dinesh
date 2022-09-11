/*
 * frmfeltOrder.java
 *
 * Created on June 14, 2004, 3:00 PM
 */

package EITLERP.Production.ReportUI;

/**
 *
 * @author ashutosh
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


public class frmfeltComplaint extends javax.swing.JApplet {
    
    private int EditMode=0;
    int NewNo=0;
    
    private String SelPrefix=""; //Selected Prefix
    private String SelSuffix=""; //Selected Prefix
    private int FFNo=0;
    
    private EITLTableCellRenderer Renderer=new EITLTableCellRenderer();
    private EITLTableCellRenderer Paint=new EITLTableCellRenderer();
    
    private EITLTableModel DataModelD;
    private EITLTableModel DataModelO;
    private EITLTableModel DataModelSC;
    private EITLTableModel DataModelMainCode;
    private EITLComboModel cmbComplaintNatureModel;
   
       
    
    private HashMap colVariables=new HashMap();
    private HashMap colVariables_H=new HashMap();
    //clsColumn ObjColumn=new clsColumn();
    
    private boolean Updating=false;
    private boolean Updating_H=false;
    private boolean DoNotEvaluate=false;
    
    //private clsOrderParty ObjSalesParty;
    private clsComplain ObjComplain;
    
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
    public frmfeltComplaint() {
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
       
     //   GenerateCombos();
        GenerateCombo();
       // GenerateDeptCombo();
        FormatGrid();
        GenerateGrid();
    
      //  cmdSelectAll.setEnabled(false);
      //  cmdClearAll.setEnabled(false);
        
        ObjComplain = new clsComplain();
        lblRevNo.setVisible(false);
        
        //lblDocThrough.setVisible(false);
        //txtDocThrough.setVisible(false);
        
        if(ObjComplain.LoadData(EITLERPGLOBAL.gCompanyID)) {
            DisplayData();
            SetMenuForRights();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while loading data. \n Error is "+ObjComplain.LastError);
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
        
        List=clsHierarchy.getListEx(" WHERE D_COM_HIERARCHY.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsComplain.ModuleID);
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            List=clsHierarchy.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND MODULE_ID="+clsComplain.ModuleID);
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        grpSisterConcern = new javax.swing.ButtonGroup();
        lblTitle = new javax.swing.JLabel();
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
        jLabel5 = new javax.swing.JLabel();
        cmdNext1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblRevNo = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        lblStationName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
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
        jLabel53 = new javax.swing.JLabel();
        txtComplainNo = new javax.swing.JTextField();
        txtComplainDate = new javax.swing.JTextField();
        txtAttendedOn = new javax.swing.JTextField();
        txtCloseDate = new javax.swing.JTextField();
        txtPartyCode = new javax.swing.JTextField();
        txtInvoiceNo = new javax.swing.JTextField();
        txtInvoiceDate = new javax.swing.JTextField();
        txtInvoiceAmount = new javax.swing.JTextField();
        txtRemarks = new javax.swing.JTextField();
        txtPieceNo = new javax.swing.JTextField();
        txtMachineNo = new javax.swing.JTextField();
        txtPosition = new javax.swing.JTextField();
        txtWidth = new javax.swing.JTextField();
        txtGSM = new javax.swing.JTextField();
        txtLength = new javax.swing.JTextField();
        cmbComplaintNature = new javax.swing.JComboBox();
        txtPartyName = new javax.swing.JTextField();
        txtactiondate1 = new javax.swing.JTextField();
        txtActionRemarks1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtactiondate2 = new javax.swing.JTextField();
        txtActionRemarks2 = new javax.swing.JTextField();
        txtactiondate3 = new javax.swing.JTextField();
        txtActionRemarks3 = new javax.swing.JTextField();
        txtactiondate4 = new javax.swing.JTextField();
        txtActionRemarks4 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtactiondate7 = new javax.swing.JTextField();
        txtActionRemarks7 = new javax.swing.JTextField();
        txtactiondate5 = new javax.swing.JTextField();
        txtActionRemarks5 = new javax.swing.JTextField();
        txtactiondate6 = new javax.swing.JTextField();
        txtActionRemarks6 = new javax.swing.JTextField();
        txtactiondate8 = new javax.swing.JTextField();
        txtComplaintResolution = new javax.swing.JTextArea();
        txtActionRemarks8 = new javax.swing.JTextField();
        txtComplainNature = new javax.swing.JTextField();
        lblRevNo2 = new javax.swing.JLabel();
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

        lblTitle.setBackground(new java.awt.Color(0, 102, 153));
        lblTitle.setForeground(java.awt.Color.white);
        lblTitle.setText("Felt Complaint");
        lblTitle.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblTitle.setOpaque(true);
        getContentPane().add(lblTitle);
        lblTitle.setBounds(0, 40, 800, 25);

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

        jTabbedPane1.setToolTipText("");
        Tab1.setLayout(null);

        Tab1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        Tab1.setToolTipText("Complaint Header");
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Complaint No");
        Tab1.add(jLabel2);
        jLabel2.setBounds(40, 20, 100, 20);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Complaint Nature ");
        Tab1.add(jLabel5);
        jLabel5.setBounds(30, 50, 114, 20);

        cmdNext1.setText("Next >>");
        cmdNext1.setNextFocusableComponent(txtComplainNo);
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
        cmdNext1.setBounds(670, 470, 90, 25);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Attended On");
        Tab1.add(jLabel4);
        jLabel4.setBounds(40, 80, 100, 20);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Piece No");
        Tab1.add(jLabel7);
        jLabel7.setBounds(70, 180, 70, 20);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Machine No");
        Tab1.add(jLabel9);
        jLabel9.setBounds(260, 180, 80, 20);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Width");
        Tab1.add(jLabel10);
        jLabel10.setBounds(290, 200, 50, 20);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("GSM");
        Tab1.add(jLabel6);
        jLabel6.setBounds(500, 200, 40, 20);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Party Code");
        Tab1.add(jLabel14);
        jLabel14.setBounds(60, 120, 80, 20);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Position");
        Tab1.add(jLabel15);
        jLabel15.setBounds(480, 180, 60, 20);

        lblRevNo.setText("...");
        Tab1.add(lblRevNo);
        lblRevNo.setBounds(280, 20, 20, 20);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Invoice No ");
        Tab1.add(jLabel11);
        jLabel11.setBounds(260, 140, 80, 20);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Close Date ");
        Tab1.add(jLabel13);
        jLabel13.setBounds(390, 80, 100, 20);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Invoice Amount");
        Tab1.add(jLabel17);
        jLabel17.setBounds(460, 140, 110, 20);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Invoice Date");
        Tab1.add(jLabel30);
        jLabel30.setBounds(50, 140, 90, 20);

        lblStationName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStationName.setText("Length");
        Tab1.add(lblStationName);
        lblStationName.setBounds(80, 200, 60, 20);

        jLabel1.setText("Complaint Date");
        Tab1.add(jLabel1);
        jLabel1.setBounds(400, 20, 110, 20);

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
        cmdNext5.setNextFocusableComponent(txtComplainNo);
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
        txtLength1.setNextFocusableComponent(txtInvoiceNo);
        txtLength1.setEnabled(false);
        Tab2.add(txtLength1);
        txtLength1.setBounds(120, 220, 130, 20);

        txtPosition1.setName("ADD2");
        txtPosition1.setNextFocusableComponent(txtInvoiceDate);
        txtPosition1.setEnabled(false);
        Tab2.add(txtPosition1);
        txtPosition1.setBounds(120, 109, 130, 20);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Width  ");
        Tab2.add(jLabel29);
        jLabel29.setBounds(340, 220, 80, 20);

        txtWidth1.setName("CITY");
        txtWidth1.setNextFocusableComponent(txtInvoiceAmount);
        txtWidth1.setEnabled(false);
        Tab2.add(txtWidth1);
        txtWidth1.setBounds(430, 220, 130, 19);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("GSM");
        Tab2.add(jLabel38);
        jLabel38.setBounds(360, 250, 50, 20);

        txtGSQ1.setName("PINCODE");
        txtGSQ1.setNextFocusableComponent(txtRemarks);
        txtGSQ1.setEnabled(false);
        Tab2.add(txtGSQ1);
        txtGSQ1.setBounds(430, 250, 130, 19);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Delivery Date ");
        Tab2.add(jLabel41);
        jLabel41.setBounds(10, 280, 100, 15);

        txtDelivDate1.setName("STATE");
        txtDelivDate1.setEnabled(false);
        Tab2.add(txtDelivDate1);
        txtDelivDate1.setBounds(120, 280, 130, 19);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Group ");
        Tab2.add(jLabel42);
        jLabel42.setBounds(20, 310, 90, 20);

        txtGroup1.setName("PHONE_O");
        txtGroup1.setNextFocusableComponent(txtPartyCode);
        txtGroup1.setEnabled(false);
        Tab2.add(txtGroup1);
        txtGroup1.setBounds(120, 310, 130, 19);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Confirmation No");
        Tab2.add(jLabel43);
        jLabel43.setBounds(290, 80, 130, 20);

        txtConfNo1.setName("MOBILE_NO");
        txtConfNo1.setNextFocusableComponent(txtPosition);
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
        txtOrderCode1.setNextFocusableComponent(txtMachineNo);
        txtOrderCode1.setEnabled(false);
        Tab2.add(txtOrderCode1);
        txtOrderCode1.setBounds(120, 140, 130, 21);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Reference No");
        Tab2.add(jLabel49);
        jLabel49.setBounds(310, 110, 110, 20);

        txtRefNo1.setName("FROM_DATE_REG");
        txtRefNo1.setNextFocusableComponent(txtAttendedOn);
        txtRefNo1.setEnabled(false);
        Tab2.add(txtRefNo1);
        txtRefNo1.setBounds(430, 110, 130, 21);

        txtCommDate1.setName("STATE");
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

        jLabel53.setText("Remarks");
        Tab1.add(jLabel53);
        jLabel53.setBounds(80, 160, 54, 20);

        txtComplainNo.setEnabled(false);
        Tab1.add(txtComplainNo);
        txtComplainNo.setBounds(150, 20, 110, 19);

        txtComplainDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        txtComplainDate.setEnabled(false);
        Tab1.add(txtComplainDate);
        txtComplainDate.setBounds(510, 20, 110, 19);

        txtAttendedOn = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        txtAttendedOn.setEnabled(false);
        Tab1.add(txtAttendedOn);
        txtAttendedOn.setBounds(150, 80, 110, 19);

        txtCloseDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        txtCloseDate.setEnabled(false);
        Tab1.add(txtCloseDate);
        txtCloseDate.setBounds(510, 80, 110, 19);

        txtPartyCode.setEnabled(false);
        txtPartyCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartyCodeActionPerformed(evt);
            }
        });
        txtPartyCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyCodeKeyPressed(evt);
            }
        });

        Tab1.add(txtPartyCode);
        txtPartyCode.setBounds(150, 120, 80, 19);

        txtInvoiceNo.setEnabled(false);
        txtInvoiceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceNoKeyPressed(evt);
            }
        });

        Tab1.add(txtInvoiceNo);
        txtInvoiceNo.setBounds(360, 140, 90, 19);

        txtInvoiceDate = new JTextFieldHint(new JTextField(),"DD/MM/YYYY");
        txtInvoiceDate.setEnabled(false);
        txtInvoiceDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceDateKeyPressed(evt);
            }
        });

        Tab1.add(txtInvoiceDate);
        txtInvoiceDate.setBounds(150, 140, 110, 19);

        txtInvoiceAmount.setEnabled(false);
        Tab1.add(txtInvoiceAmount);
        txtInvoiceAmount.setBounds(580, 140, 110, 19);

        txtRemarks.setEnabled(false);
        Tab1.add(txtRemarks);
        txtRemarks.setBounds(150, 160, 390, 19);

        txtPieceNo.setEnabled(false);
        txtPieceNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPieceNoKeyPressed(evt);
            }
        });

        Tab1.add(txtPieceNo);
        txtPieceNo.setBounds(150, 180, 110, 19);

        txtMachineNo.setEnabled(false);
        Tab1.add(txtMachineNo);
        txtMachineNo.setBounds(360, 180, 110, 19);

        txtPosition.setEnabled(false);
        Tab1.add(txtPosition);
        txtPosition.setBounds(580, 180, 130, 19);

        txtWidth.setEnabled(false);
        Tab1.add(txtWidth);
        txtWidth.setBounds(360, 200, 110, 19);

        txtGSM.setEnabled(false);
        Tab1.add(txtGSM);
        txtGSM.setBounds(580, 200, 130, 19);

        txtLength.setEnabled(false);
        Tab1.add(txtLength);
        txtLength.setBounds(150, 200, 110, 19);

        cmbComplaintNature.setEnabled(false);
        cmbComplaintNature.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbComplaintNatureItemStateChanged(evt);
            }
        });
        cmbComplaintNature.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbComplaintNatureFocusLost(evt);
            }
        });

        Tab1.add(cmbComplaintNature);
        cmbComplaintNature.setBounds(390, 50, 210, 24);

        txtPartyName.setEnabled(false);
        Tab1.add(txtPartyName);
        txtPartyName.setBounds(230, 120, 260, 19);

        txtactiondate1.setEnabled(false);
        txtactiondate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtactiondate1ActionPerformed(evt);
            }
        });

        Tab1.add(txtactiondate1);
        txtactiondate1.setBounds(60, 300, 110, 19);

        txtActionRemarks1.setEnabled(false);
        txtActionRemarks1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtActionRemarks1ActionPerformed(evt);
            }
        });

        Tab1.add(txtActionRemarks1);
        txtActionRemarks1.setBounds(170, 300, 570, 19);

        jLabel12.setText("Action Date");
        Tab1.add(jLabel12);
        jLabel12.setBounds(80, 280, 90, 15);

        jLabel20.setText("Remarks");
        Tab1.add(jLabel20);
        jLabel20.setBounds(330, 280, 80, 15);

        txtactiondate2.setEnabled(false);
        txtactiondate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtactiondate2ActionPerformed(evt);
            }
        });

        Tab1.add(txtactiondate2);
        txtactiondate2.setBounds(60, 320, 110, 19);

        txtActionRemarks2.setEnabled(false);
        Tab1.add(txtActionRemarks2);
        txtActionRemarks2.setBounds(170, 340, 570, 19);

        txtactiondate3.setEnabled(false);
        txtactiondate3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtactiondate3ActionPerformed(evt);
            }
        });

        Tab1.add(txtactiondate3);
        txtactiondate3.setBounds(60, 340, 110, 19);

        txtActionRemarks3.setEnabled(false);
        txtActionRemarks3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtActionRemarks3ActionPerformed(evt);
            }
        });

        Tab1.add(txtActionRemarks3);
        txtActionRemarks3.setBounds(170, 320, 570, 19);

        txtactiondate4.setEnabled(false);
        Tab1.add(txtactiondate4);
        txtactiondate4.setBounds(60, 360, 110, 20);

        txtActionRemarks4.setEnabled(false);
        Tab1.add(txtActionRemarks4);
        txtActionRemarks4.setBounds(170, 360, 570, 19);

        jLabel37.setText("Complaint Resolution");
        Tab1.add(jLabel37);
        jLabel37.setBounds(60, 220, 240, 20);

        txtactiondate7.setEnabled(false);
        Tab1.add(txtactiondate7);
        txtactiondate7.setBounds(60, 420, 110, 20);

        txtActionRemarks7.setEnabled(false);
        Tab1.add(txtActionRemarks7);
        txtActionRemarks7.setBounds(170, 420, 570, 19);

        txtactiondate5.setEnabled(false);
        Tab1.add(txtactiondate5);
        txtactiondate5.setBounds(60, 380, 110, 20);

        txtActionRemarks5.setEnabled(false);
        Tab1.add(txtActionRemarks5);
        txtActionRemarks5.setBounds(170, 380, 570, 19);

        txtactiondate6.setEnabled(false);
        Tab1.add(txtactiondate6);
        txtactiondate6.setBounds(60, 400, 110, 20);

        txtActionRemarks6.setEnabled(false);
        Tab1.add(txtActionRemarks6);
        txtActionRemarks6.setBounds(170, 400, 570, 19);

        txtactiondate8.setEnabled(false);
        Tab1.add(txtactiondate8);
        txtactiondate8.setBounds(60, 440, 110, 20);

        txtComplaintResolution.setMinimumSize(new java.awt.Dimension(4, 19));
        txtComplaintResolution.setPreferredSize(new java.awt.Dimension(4, 19));
        txtComplaintResolution.setSelectionEnd(20);
        txtComplaintResolution.setEnabled(false);
        Tab1.add(txtComplaintResolution);
        txtComplaintResolution.setBounds(60, 240, 680, 40);

        txtActionRemarks8.setEnabled(false);
        Tab1.add(txtActionRemarks8);
        txtActionRemarks8.setBounds(170, 440, 570, 19);

        txtComplainNature.setEnabled(false);
        Tab1.add(txtComplainNature);
        txtComplainNature.setBounds(150, 50, 220, 19);

        lblRevNo2.setBackground(new java.awt.Color(255, 255, 204));
        lblRevNo2.setFont(new java.awt.Font("Dialog", 2, 10));
        lblRevNo2.setText("Enter Party Code then press F1 for Searching Party Wise Invoice ");
        Tab1.add(lblRevNo2);
        lblRevNo2.setBounds(70, 100, 560, 20);

        jTabbedPane1.addTab("Header", Tab1);

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
        jTabbedPane1.setBounds(5, 70, 840, 540);

    }//GEN-END:initComponents

    private void txtPartyCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyCodeKeyPressed
 String strpartyCode = txtPartyCode.getText();
       if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();
            
            aList.SQL="SELECT PARTY_CODE,INVOICE_DATE,INVOICE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PARTY_CODE='"+strpartyCode+"'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            //aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtPartyCode.setText(aList.ReturnVal);
                txtInvoiceNo.setText(clsComplain._getInvoiceNo(strpartyCode));
                txtInvoiceDate.setText(clsComplain._getInvoiceDate(strpartyCode));
                txtPartyName.setText(clsComplain._getPartyName(strpartyCode));
                txtInvoiceAmount.setText(clsComplain._getInvoiceAmount(strpartyCode));
                txtPieceNo.setText(clsComplain._getPieceNo(strpartyCode));
                txtLength.setText(clsComplain._getLength(strpartyCode)); 
                txtWidth.setText(clsComplain._getWidth(strpartyCode));
                txtPosition.setText(clsComplain._getPosition(strpartyCode));
               
        }
       }
        
    }//GEN-LAST:event_txtPartyCodeKeyPressed

    private void txtPartyCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartyCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartyCodeActionPerformed

    private void cmbComplaintNatureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbComplaintNatureItemStateChanged

      txtComplainNature.setText((String)cmbComplaintNature.getSelectedItem());
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbComplaintNatureItemStateChanged

    private void cmbComplaintNatureFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbComplaintNatureFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbComplaintNatureFocusLost

    private void txtactiondate3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtactiondate3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtactiondate3ActionPerformed

    private void txtactiondate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtactiondate2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtactiondate2ActionPerformed

    private void txtActionRemarks1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActionRemarks1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActionRemarks1ActionPerformed

    private void txtActionRemarks3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActionRemarks3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActionRemarks3ActionPerformed

    private void txtactiondate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtactiondate1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtactiondate1ActionPerformed

    private void txtPieceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPieceNoKeyPressed
         String strpieceNo = txtPieceNo.getText();
      //System.out.println(strinvoiceDate);
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();            
            aList.SQL="SELECT PIECE_NO,INVOICE_DATE,PARTY_CODE,INVOICE_NO FROM PRODUCTION.FELT_INVOICE_DATA WHERE PIECE_NO='"+strpieceNo+"'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            //aList.DefaultSearchOn=1;
         
            if(aList.ShowLOV()) {
                txtPieceNo.setText(aList.ReturnVal);  
                txtInvoiceDate.setText(clsComplain.getInvoiceDate(strpieceNo));
                txtInvoiceNo.setText(clsComplain.getInvoiceNo(strpieceNo));
                txtPartyCode.setText(clsComplain.getPartyCode(strpieceNo));
                txtPartyName.setText(clsComplain.getPartyName(strpieceNo));
                txtInvoiceAmount.setText(clsComplain.getInvoiceAmount(strpieceNo));
                txtLength.setText(clsComplain.getLength(strpieceNo)); 
                txtWidth.setText(clsComplain.getWidth(strpieceNo));
                //txtGSM.setText(clsComplain.getGSM(strpieceNo));
                //txtMachineNo.setText(clsComplain.getMachineNo(strpieceNo));
                txtPosition.setText(clsComplain.getPosition(strpieceNo));                
                //System.out.println(txtInvoiceAmount.getText());                
              //txtInvoiceNo.setText(clsComplain.getInvoiceNo(strPieceNo)); 
        }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPieceNoKeyPressed

    private void txtInvoiceDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceDateKeyPressed
        String strinvoiceNo = txtInvoiceNo.getText();
      //System.out.println(strinvoiceDate);
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();            
            aList.SQL="SELECT INVOICE_DATE,PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO='"+strinvoiceNo+"'";
            //aList.SQL="SELECT INVOICE_DATE,PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_NO=''";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            //aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtInvoiceDate.setText(aList.ReturnVal);
                txtInvoiceAmount.setText(clsComplain._getInvoiceAmount(aList.ReturnVal,strinvoiceNo));
                txtPartyCode.setText(clsComplain._getPartyCode(aList.ReturnVal,strinvoiceNo));
                txtPartyName.setText(clsComplain._getPartyName(aList.ReturnVal,strinvoiceNo));
                txtPieceNo.setText(clsComplain._getPieceNo(aList.ReturnVal,strinvoiceNo));
                txtLength.setText(clsComplain._getLength(aList.ReturnVal,strinvoiceNo));
                txtWidth.setText(clsComplain._getWidth(aList.ReturnVal,strinvoiceNo));
                txtGSM.setText(clsComplain._getGSM(aList.ReturnVal,strinvoiceNo));
                txtMachineNo.setText(clsComplain._getMachineNo(aList.ReturnVal,strinvoiceNo));
                txtPosition.setText(clsComplain._getPosition(aList.ReturnVal,strinvoiceNo));                
                //System.out.println(txtInvoiceAmount.getText());                
                
        }
        }
        
    }//GEN-LAST:event_txtInvoiceDateKeyPressed

    private void txtInvoiceNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceNoKeyPressed
       String strinvoiceDate = EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText());
      //System.out.println(strinvoiceDate);
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            LOV aList=new LOV();            
            aList.SQL="SELECT INVOICE_NO,PARTY_CODE,PARTY_NAME FROM PRODUCTION.FELT_INVOICE_DATA WHERE INVOICE_DATE='"+strinvoiceDate+"'";
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=2;
            //aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtInvoiceNo.setText(aList.ReturnVal);
                txtPartyCode.setText(clsComplain.getPartyCode(aList.ReturnVal,strinvoiceDate));
                txtPartyName.setText(clsComplain.getPartyName(aList.ReturnVal,strinvoiceDate));
                txtInvoiceAmount.setText(clsComplain.getInvoiceAmount(aList.ReturnVal,strinvoiceDate));
                txtPieceNo.setText(clsComplain.getPieceNo(aList.ReturnVal,strinvoiceDate)); 
                txtLength.setText(clsComplain.getLength(aList.ReturnVal,strinvoiceDate)); 
                txtWidth.setText(clsComplain.getWidth(aList.ReturnVal,strinvoiceDate));
                txtGSM.setText(clsComplain.getGSM(aList.ReturnVal,strinvoiceDate));
                txtMachineNo.setText(clsComplain.getMachineNo(aList.ReturnVal,strinvoiceDate));
                txtPosition.setText(clsComplain.getPosition(aList.ReturnVal,strinvoiceDate));                
                //System.out.println(txtInvoiceAmount.getText());                
                
        }
        }
        
    }//GEN-LAST:event_txtInvoiceNoKeyPressed

       
    private void cmdNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNext1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_cmdNext1ActionPerformed

    private void cmdNext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdNext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdNext1KeyPressed

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
            if(ApprovalFlow.IsOnceRejectedDoc(EITLERPGLOBAL.gCompanyID,clsComplain.ModuleID,PartyID)) {
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
        ObjComplain.ShowHistory(EITLERPGLOBAL.gCompanyID, DocNo);
        MoveFirst();
    }//GEN-LAST:event_cmdViewHistoryActionPerformed

    private void cmdNormalViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdNormalViewActionPerformed
        // TODO add your handling code here:
        ObjComplain.LoadData(EITLERPGLOBAL.gCompanyID);
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
        ObjComplain.Close();
        ((JFrame)getParent().getParent().getParent().getParent()).dispose();
    }//GEN-LAST:event_cmdExitActionPerformed
                        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgApprove;
    private javax.swing.JRadioButton OpgFinal;
    private javax.swing.JRadioButton OpgHold;
    private javax.swing.JRadioButton OpgReject;
    private javax.swing.JPanel Tab1;
    private javax.swing.JPanel Tab2;
    private javax.swing.JTable TableA;
    private javax.swing.JTable TableHS;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbComplaintNature;
    private javax.swing.JComboBox cmbHierarchy;
    private javax.swing.JComboBox cmbPriority1;
    private javax.swing.JComboBox cmbSendTo;
    private javax.swing.JButton cmdBack;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdFilter;
    private javax.swing.JButton cmdLast;
    private javax.swing.JButton cmdNew;
    private javax.swing.JButton cmdNext;
    private javax.swing.JButton cmdNext1;
    private javax.swing.JButton cmdNext3;
    private javax.swing.JButton cmdNext5;
    private javax.swing.JButton cmdNormalView;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdSave;
    private javax.swing.JButton cmdShowRemarks;
    private javax.swing.JButton cmdTop;
    private javax.swing.JButton cmdViewHistory;
    private javax.swing.ButtonGroup grpSisterConcern;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblRevNo;
    private javax.swing.JLabel lblRevNo1;
    private javax.swing.JLabel lblRevNo2;
    private javax.swing.JLabel lblStationName;
    private javax.swing.JLabel lblStationName1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtActionRemarks1;
    private javax.swing.JTextField txtActionRemarks2;
    private javax.swing.JTextField txtActionRemarks3;
    private javax.swing.JTextField txtActionRemarks4;
    private javax.swing.JTextField txtActionRemarks5;
    private javax.swing.JTextField txtActionRemarks6;
    private javax.swing.JTextField txtActionRemarks7;
    private javax.swing.JTextField txtActionRemarks8;
    private javax.swing.JTextField txtAttendedOn;
    private javax.swing.JTextField txtAuditRemarks;
    private javax.swing.JTextField txtCloseDate;
    private javax.swing.JTextField txtCommDate1;
    private javax.swing.JTextField txtComplainDate;
    private javax.swing.JTextField txtComplainNature;
    private javax.swing.JTextField txtComplainNo;
    private javax.swing.JTextArea txtComplaintResolution;
    private javax.swing.JTextField txtConfNo1;
    private javax.swing.JTextField txtDelivDate1;
    private javax.swing.JTextField txtFrom;
    private javax.swing.JTextField txtFromRemarks;
    private javax.swing.JTextField txtGSM;
    private javax.swing.JTextField txtGSQ1;
    private javax.swing.JTextField txtGroup1;
    private javax.swing.JTextField txtInvoiceAmount;
    private javax.swing.JTextField txtInvoiceDate;
    private javax.swing.JTextField txtInvoiceNo;
    private javax.swing.JTextField txtLength;
    private javax.swing.JTextField txtLength1;
    private javax.swing.JTextField txtMachineNo;
    private javax.swing.JTextField txtMachineNo1;
    private javax.swing.JTextField txtOrderCode1;
    private javax.swing.JTextField txtOrderDate1;
    private javax.swing.JTextField txtPartyCode;
    private javax.swing.JTextField txtPartyCode1;
    private javax.swing.JTextField txtPartyName;
    private javax.swing.JTextField txtPieceNo;
    private javax.swing.JTextField txtPieceNo1;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtPosition1;
    private javax.swing.JTextField txtProductCode1;
    private javax.swing.JTextField txtRefNo1;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtToRemarks;
    private javax.swing.JTextField txtWeight1;
    private javax.swing.JTextField txtWidth;
    private javax.swing.JTextField txtWidth1;
    private javax.swing.JTextField txtactiondate1;
    private javax.swing.JTextField txtactiondate2;
    private javax.swing.JTextField txtactiondate3;
    private javax.swing.JTextField txtactiondate4;
    private javax.swing.JTextField txtactiondate5;
    private javax.swing.JTextField txtactiondate6;
    private javax.swing.JTextField txtactiondate7;
    private javax.swing.JTextField txtactiondate8;
    private javax.swing.JTextField txtlastpriorityuser1;
    private javax.swing.JTextField txtprioritydate1;
    // End of variables declaration//GEN-END:variables
    
    protected void Add() {        
        //Now Generate new document no.
        SelectFirstFree aList=new SelectFirstFree();
        aList.ModuleID=717;
        
        if(aList.ShowList()) {
            EditMode=EITLERPGLOBAL.ADD;
            SetFields(true);
            DisableToolbar();
            ClearFields();
            SetupApproval();            
            SelPrefix=aList.Prefix; //Selected Prefix;
            SelSuffix=aList.Suffix;
            FFNo=aList.FirstFreeNo;            
            //Display newly generated document no.
            txtComplainNo.setText(clsFirstFree.getNextFreeNo(EITLERPGLOBAL.gCompanyID, 717, FFNo,  false));
            txtComplainDate.requestFocus();            
            lblTitle.setText("FELT COMPLAINT - "+txtComplainNo.getText());
            System.out.println(txtComplainNo.getText());
            lblTitle.setBackground(Color.BLUE);
        }
        else {
            JOptionPane.showMessageDialog(null,"You must select doucment number prefix. If no prefixes found in the list, Please do entry in First Free Nos.");
        }
        /*
        EITLERPGLOBAL.ChangeCursorToWait(this);
        EditMode=EITLERPGLOBAL.ADD;
        SetFields(true);
        DisableToolbar();
        ClearFields();
        SetupApproval();                   
        String CNo = data.getStringValueFromDB("SELECT MAX(CPNT_NO) AS CPNT_NO FROM FELT_COMPLAINTS ORDER BY CPNT_NO DESC");
        int ComplaintNo = Integer.parseInt(CNo);
        NewNo = ComplaintNo + 1;
        txtComplainNo.setText(Integer.toString(NewNo));        
        lblTitle.setText("FELT COMPLAINT  - "+txtComplainNo.getText());
        EITLERPGLOBAL.ChangeCursorToDefault(this);
         */
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
            
            int FromUserID=ApprovalFlow.getFromID((int)EITLERPGLOBAL.gCompanyID, clsComplain.ModuleID,(String)ObjComplain.getAttribute("PARTY_CODE").getObj());
            lnFromID=FromUserID;
            String strFromUser=clsUser.getUserName(EITLERPGLOBAL.gCompanyID, FromUserID);
            String strFromRemarks=ApprovalFlow.getFromRemarks( (int)EITLERPGLOBAL.gCompanyID,clsComplain.ModuleID,FromUserID,(String)ObjComplain.getAttribute("PARTY_CODE").getObj());
            
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
                
                String DocNo=(String)ObjComplain.getAttribute("PARTY_CODE").getObj();
                
                List=ApprovalFlow.getRemainingUsers((int)EITLERPGLOBAL.gCompanyID, clsComplain.ModuleID,DocNo);
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
        txtComplainDate.setEnabled(pStat);
        txtComplainNature.setEnabled(pStat);
        cmbComplaintNature.setEnabled(pStat);
        txtAttendedOn.setEnabled(pStat);
        txtCloseDate.setEnabled(pStat);
        txtPartyCode.setEnabled(pStat);
        txtPartyName.setEnabled(pStat);
        txtInvoiceNo.setEnabled(pStat);        
        txtInvoiceDate.setEnabled(pStat);
        txtInvoiceAmount.setEnabled(pStat);
        txtactiondate1.setEnabled(pStat);
        txtactiondate2.setEnabled(pStat);
        txtactiondate3.setEnabled(pStat);
        txtactiondate4.setEnabled(pStat);
        txtactiondate5.setEnabled(pStat);
        txtactiondate6.setEnabled(pStat);
        txtactiondate7.setEnabled(pStat);
        txtactiondate8.setEnabled(pStat);
        //txtactiondate9.setEnabled(pStat);
       // txtactiondate10.setEnabled(pStat);
        //txtInvoice2.seEnabled(pStat);
        //txtInvoice3.seEnabled(pStat);
        //txtInvoice4.seEnabled(pStat);
        txtRemarks.setEnabled(pStat);
        txtActionRemarks1.setEnabled(pStat);
        txtActionRemarks2.setEnabled(pStat);
        txtActionRemarks3.setEnabled(pStat);
        txtActionRemarks4.setEnabled(pStat);
        txtActionRemarks5.setEnabled(pStat);
        txtActionRemarks6.setEnabled(pStat);
        txtActionRemarks7.setEnabled(pStat);
        txtActionRemarks8.setEnabled(pStat);
       // txtActionRemarks9.setEnabled(pStat);
       // txtActionRemarks10.setEnabled(pStat);
        txtComplaintResolution.setEnabled(pStat);
        txtPieceNo.setEnabled(pStat);
        txtMachineNo.setEnabled(pStat);
        txtPosition.setEnabled(pStat);
        txtLength.setEnabled(pStat);
        txtWidth.setEnabled(pStat);        
        txtGSM.setEnabled(pStat); 
        
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
    
    private void GenerateCombo() {
        HashMap List=new HashMap();
        /*
        
        //===============================//
        */
        //----- Generate Department Combo ------- //
        
        cmbComplaintNatureModel=new EITLComboModel();
        cmbComplaintNature.removeAllItems();
        cmbComplaintNature.setModel(cmbComplaintNatureModel);
       
      
        try{
            ComboData combodata=new ComboData();
            combodata.Code=0;
            combodata.Text="Select Complaint Nature";
            
            cmbComplaintNatureModel.addElement(combodata);
            ResultSet rs=data.getResult("SELECT PARA_CODE,PARA_DESC FROM PRODUCTION.FELT_PARAMETER_MASTER WHERE PARA_ID ='COMPLAINT' ORDER BY PARA_CODE+0");
            while(!rs.isAfterLast()){
                combodata=new ComboData();
                combodata.Code=rs.getLong("PARA_CODE");
                //combodata.strCode=rs.getString("PRIORITY_DESC");
                combodata.Text=rs.getString("PARA_DESC");
                
               cmbComplaintNatureModel.addElement(combodata);
                rs.next();
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        
        /*List=clsPriority.getList("WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);        
        for(int i=1;i<=List.size();i++) {
            clsPriority ObjDept= (clsPriority) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjDept.getAttribute("PRIORITY_ID").getVal();
            aData.Text=(String)ObjDept.getAttribute("PRIORITY_DESC").getObj();
            cmbPriorityModel.addElement(aData);
        }*/
        
        //------------------------------ // 
        
    }
    

    
    private boolean Validate() {
        int ValidEntryCount=0;
        
        if (txtPartyCode.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtPartyCode.requestFocus(true);
            return false;
        }
        /*
        if(txtPartyCode.getText().trim().equals("")&&OpgFinal.isSelected()) {
            JOptionPane.showMessageDialog(null,"Please enter Party Code");
            txtPartyCode.requestFocus(true);
            return false;
        }
        */
        /*
        if(!txtPartyCode.getText().trim().equals("")) {
            if(EditMode==EITLERPGLOBAL.ADD) {
                
                if (data.IsRecordExist("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"'")) {
                    JOptionPane.showMessageDialog(null,"Party Code already exists!!");
                    txtPartyCode.requestFocus(true);
                    return false;
                }
                
            }
            if(EditMode==EITLERPGLOBAL.EDIT) {
                int MainCode = EITLERPGLOBAL.getComboCode(cmbMainCodeType);
                if (!data.IsRecordExist("SELECT * FROM D_SAL_PARTY_MASTER WHERE PARTY_CODE='"+txtPartyCode.getText().trim()+"' AND MAIN_ACCOUNT_CODE='"+MainCode+"' AND CANCELLED = 0 AND APPROVED = 0 ")) {
                    JOptionPane.showMessageDialog(null,"Party Code already exists!!");
                    txtPartyCode.requestFocus(true);
                    return false;
                }
            }
        }
        */                       
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
        txtComplainDate.setText("");
        txtComplainNature.setText("");
        txtAttendedOn.setText("");
        txtCloseDate.setText("");
        txtPartyCode.setText("");
        txtPartyName.setText("");
        txtInvoiceNo.setText("");
        txtactiondate1.setText("");
        txtactiondate2.setText("");
        txtactiondate3.setText("");
        txtactiondate4.setText("");
        txtactiondate5.setText("");
        txtactiondate6.setText("");
        txtactiondate7.setText("");
        txtactiondate8.setText("");
       // txtactiondate9.setText("");
       // txtactiondate10.setText("");
       // txtInvoice2.setText("");
       // txtInvoice3.setText("");
       // txtInvoice4.setText("");
        txtInvoiceDate.setText("");
        txtInvoiceAmount.setText("");
        txtRemarks.setText("");
        txtActionRemarks1.setText("");
        txtActionRemarks2.setText("");
        txtActionRemarks3.setText("");
        txtActionRemarks4.setText("");
        txtActionRemarks5.setText("");
        txtActionRemarks6.setText("");
        txtActionRemarks7.setText("");
        txtActionRemarks8.setText("");
       // txtActionRemarks9.setText("");
       // txtActionRemarks10.setText("");
        txtComplaintResolution.setText("");
        txtPieceNo.setText("");
        txtMachineNo.setText("");
        txtPosition.setText("");
        txtLength.setText("");
        txtWidth.setText("");        
        txtGSM.setText("");        
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
        String sPartyCode=ObjComplain.getAttribute("CPNT_NO").getString();
      //  if(ObjComplain.IsEditable(EITLERPGLOBAL.gCompanyID, sPartyCode, EITLERPGLOBAL.gNewUserID)) {
            EditMode=EITLERPGLOBAL.EDIT;
            
            //---New Change ---//
            //GenerateCombos();
            DisplayData();
            //----------------//
            
           // if(ApprovalFlow.IsCreator(clsComplain.ModuleID,sPartyCode)||clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 0,11512)) {
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
        String sPartyCode=ObjComplain.getAttribute("PARTY_CODE").getString();
        
        if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this record ?","SDML ERP",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if(ObjComplain.CanDelete(EITLERPGLOBAL.gCompanyID, sPartyCode)) {
                if(ObjComplain.Delete()) {
                    ObjComplain.LoadData(EITLERPGLOBAL.gCompanyID);
                    MoveLast();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Error occured while deleting. Error is "+ObjComplain.LastError);
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
       System.out.println("hi");
        if(EditMode==EITLERPGLOBAL.ADD) {
            if(ObjComplain.Insert()) {
                // MoveLast();
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjComplain.LastError);
                return;
            }
        }
        
        if(EditMode==EITLERPGLOBAL.EDIT) {
            
            if(ObjComplain.Update()) {
                DisplayData();
            }
            else {
                JOptionPane.showMessageDialog(null,"Error occured while saving. Error is "+ObjComplain.LastError);
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
        Loader ObjLoader=new Loader(this,"EITLERP.Production.ReportUI.frmComplaintFind",true);
        frmComplaintFind ObjReturn= (frmComplaintFind) ObjLoader.getObj();     
        
        if(ObjReturn.Cancelled==false) {
            if(!ObjComplain.Filter(ObjReturn.stringFindQuery)) {
                JOptionPane.showMessageDialog(null,"No records found.");
            }
            MoveFirst();
        }
    }
    
    private void MoveFirst() {
        ObjComplain.MoveFirst();
        DisplayData();
    }
    
    private void MovePrevious() {
        ObjComplain.MovePrevious();
        DisplayData();
    }
    
    
    private void MoveNext() {
        ObjComplain.MoveNext();
        DisplayData();
    }
    
    
    private void MoveLast() {
        ObjComplain.MoveLast();
        DisplayData();
    }
    
    //Didplay data on the Screen
    private void DisplayData() {
      //=========== Color Indication ===============//
      /*  try {
            if(EditMode==0) {
                if(ObjComplain.getAttribute("APPROVED").getInt()==1) {
                    
                    lblTitle.setBackground(Color.BLUE);
                }
                
                if(ObjComplain.getAttribute("APPROVED").getInt()!=1) {
                    lblTitle.setBackground(Color.GRAY);
                }
                
                if(ObjComplain.getAttribute("CANCELLED").getInt()==1) {
                    lblTitle.setBackground(Color.RED);
                }
            }
        }
        catch(Exception c) {
            
        }*/
        //============================================//
        
        
        //========= Authority Delegation Check =====================//
        /*if(EITLERPGLOBAL.gAuthorityUserID!=EITLERPGLOBAL.gUserID) {
            int ModuleID=clsComplain.ModuleID;
            
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
            lblTitle.setText("FELT COMPLAINT - " + (String)ObjComplain.getAttribute("CPNT_NO").getObj());                        
            txtComplainNo.setText((String)ObjComplain.getAttribute("CPNT_NO").getObj());
            txtComplainDate.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_DATE").getString()));            
            txtComplainNature.setText((String)ObjComplain.getAttribute("CPNT_NATURE").getObj());
            
            txtAttendedOn.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ATTENDED_ON").getString()));            
            txtCloseDate.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_CLOSE_DATE").getString()));
            txtPartyCode.setText((String)ObjComplain.getAttribute("CPNT_PARTYCODE").getObj());            
            txtPartyName.setText((String)ObjComplain.getAttribute("CPNT_PARTY_NAME").getObj());
            txtInvoiceNo.setText(ObjComplain.getAttribute("CPNT_INVOICE_NO").getString());            
            txtInvoiceDate.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_INVOICE_DATE").getString()));
            txtInvoiceAmount.setText(ObjComplain.getAttribute("CPNT_INVOICE_AMOUNT").getString());                        
            txtRemarks.setText((String)ObjComplain.getAttribute("CPNT_REMARKS").getObj());
            txtActionRemarks1.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK1").getObj());
            txtActionRemarks2.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK2").getObj());
            txtActionRemarks3.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK3").getObj());
            txtActionRemarks4.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK4").getObj());
            txtActionRemarks5.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK5").getObj());
            txtActionRemarks6.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK6").getObj());
            txtActionRemarks7.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK7").getObj());
            txtActionRemarks8.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK8").getObj());
           // txtActionRemarks9.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK9").getObj());
           // txtActionRemarks10.setText((String)ObjComplain.getAttribute("CPNT_ACTION_REMARK10").getObj());

            
            txtactiondate1.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE1").getString()));
            txtactiondate2.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE2").getString()));
            txtactiondate3.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE3").getString()));
            txtactiondate4.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE4").getString()));
            txtactiondate5.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE5").getString()));
            txtactiondate6.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE6").getString()));
            txtactiondate7.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE7").getString()));
            txtactiondate8.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE8").getString()));
         //   txtactiondate9.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE9").getString()));
         //   txtactiondate10.setText(EITLERPGLOBAL.formatDate(ObjComplain.getAttribute("CPNT_ACTION_DATE10").getString()));
            txtComplaintResolution.setText((String)ObjComplain.getAttribute("CPNT_RESOLUTION").getObj());
            txtPieceNo.setText((String)ObjComplain.getAttribute("CPNT_PC_NO").getObj());
            txtMachineNo.setText(ObjComplain.getAttribute("CPNT_MC_NO").getString());
            txtPosition.setText(ObjComplain.getAttribute("CPNT_POSITION").getString());
            txtLength.setText((String)ObjComplain.getAttribute("CPNT_SIZE_LENGTH").getObj());
            txtWidth.setText((String)ObjComplain.getAttribute("CPNT_SIZE_WIDTH").getObj());            
            txtGSM.setText((String)ObjComplain.getAttribute("CPNT_GSM").getObj());                       
            
           // lblRevNo.setText(Integer.toString(ObjComplain.getAttribute("REVISION_NO").getInt()));
            
           // EITLERPGLOBAL.setComboIndex(cmbHierarchy,ObjComplain.getAttribute("HIERARCHY_ID").getInt());            
            
            //===================Fill up Table===================//
            /*
            FormatGrid();
            GenerateGrid();
            String MainCode = ObjComplain.getAttribute("ACCOUNT_CODES").getString();
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
             */
            //==================================================//
            //======== Generating Grid for Document Approval Flow ========//
            /*
            FormatGridA();
            HashMap List=new HashMap();
            
            String DocNo=ObjComplain.getAttribute("PARTY_CODE").getString();
            List=ApprovalFlow.getDocumentFlow(EITLERPGLOBAL.gCompanyID, clsComplain.ModuleID, DocNo);
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
             */
            //============================================================//
            
            //Showing Audit Trial History
            /*
            FormatGridHS();
            String PartyID=(String)ObjComplain.getAttribute("PARTY_CODE").getObj();
            HashMap History=clsComplain.getHistoryList(EITLERPGLOBAL.gCompanyID, PartyID);
            for(int i=1;i<=History.size();i++) {
                clsComplain ObjHistory=(clsComplain)History.get(Integer.toString(i));
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
             */            
            //=========================================//
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Display Data Error: " + e.getMessage());
        }
    }
    
    
    //Sets data to the Class Object
    private void SetData() {
        //Header Fields
        ObjComplain.setAttribute("PREFIX",SelPrefix);
        ObjComplain.setAttribute("SUFFIX",SelSuffix);
        ObjComplain.setAttribute("FFNO",FFNo);
        ObjComplain.setAttribute("CPNT_NO",txtComplainNo.getText());      
        ObjComplain.setAttribute("CPNT_DATE",EITLERPGLOBAL.formatDateDB(txtComplainDate.getText()));
        ObjComplain.setAttribute("CPNT_NATURE",txtComplainNature.getText());
        ObjComplain.setAttribute("CPNT_ATTENDED_ON",EITLERPGLOBAL.formatDateDB(txtAttendedOn.getText()));
        ObjComplain.setAttribute("CPNT_CLOSE_DATE",EITLERPGLOBAL.formatDateDB(txtCloseDate.getText()));
        ObjComplain.setAttribute("CPNT_REMARKS",txtRemarks.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK1",txtActionRemarks1.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK2",txtActionRemarks2.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK3",txtActionRemarks3.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK4",txtActionRemarks4.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK5",txtActionRemarks5.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK6",txtActionRemarks6.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK7",txtActionRemarks7.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_REMARK8",txtActionRemarks8.getText().trim().toUpperCase());
     //   ObjComplain.setAttribute("CPNT_ACTION_REMARK9",txtActionRemarks9.getText().trim().toUpperCase());
     //   ObjComplain.setAttribute("CPNT_ACTION_REMARK10",txtActionRemarks10.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_ACTION_DATE1",EITLERPGLOBAL.formatDateDB(txtactiondate1.getText()));
        ObjComplain.setAttribute("CPNT_ACTION_DATE2",EITLERPGLOBAL.formatDateDB(txtactiondate2.getText()));
        ObjComplain.setAttribute("CPNT_ACTION_DATE3",EITLERPGLOBAL.formatDateDB(txtactiondate3.getText()));
        ObjComplain.setAttribute("CPNT_ACTION_DATE4",EITLERPGLOBAL.formatDateDB(txtactiondate4.getText()));
        ObjComplain.setAttribute("CPNT_ACTION_DATE5",EITLERPGLOBAL.formatDateDB(txtactiondate5.getText()));
        ObjComplain.setAttribute("CPNT_ACTION_DATE6",EITLERPGLOBAL.formatDateDB(txtactiondate6.getText()));
        ObjComplain.setAttribute("CPNT_ACTION_DATE7",EITLERPGLOBAL.formatDateDB(txtactiondate7.getText()));
        ObjComplain.setAttribute("CPNT_ACTION_DATE8",EITLERPGLOBAL.formatDateDB(txtactiondate8.getText()));
     //   ObjComplain.setAttribute("CPNT_ACTION_DATE9",EITLERPGLOBAL.formatDateDB(txtactiondate9.getText()));
     //   ObjComplain.setAttribute("CPNT_ACTION_DATE10",EITLERPGLOBAL.formatDateDB(txtactiondate10.getText()));
        ObjComplain.setAttribute("CPNT_RESOLUTION",txtComplaintResolution.getText().trim().toUpperCase());
        ObjComplain.setAttribute("CPNT_PARTYCODE",txtPartyCode.getText());              
        ObjComplain.setAttribute("CPNT_INVOICE_NO",txtInvoiceNo.getText());
        ObjComplain.setAttribute("CPNT_INVOICE_DATE",EITLERPGLOBAL.formatDateDB(txtInvoiceDate.getText()));
        ObjComplain.setAttribute("CPNT_INVOICE_AMOUNT",txtInvoiceAmount.getText());               
        ObjComplain.setAttribute("CPNT_PC_NO",txtPieceNo.getText());
        ObjComplain.setAttribute("CPNT_MC_NO",txtMachineNo.getText());
        ObjComplain.setAttribute("CPNT_POSITION",txtPosition.getText());
        ObjComplain.setAttribute("CPNT_SIZE_LENGTH",txtLength.getText());
        ObjComplain.setAttribute("CPNT_SIZE_WIDTH",txtWidth.getText());
        ObjComplain.setAttribute("CPNT_GSM",txtGSM.getText());                   
     
        //ObjComplain.setAttribute("REMARKS",txtSetPriority.getText().trim().toUpperCase()+":"+txtRemarks.getText().trim().toUpperCase()+" | "+txtPrevRemarks.getText().trim().toUpperCase());        
        
      
        //----- Update Approval Specific Fields -----------//
        /*ObjComplain.setAttribute("HIERARCHY_ID",EITLERPGLOBAL.getComboCode(cmbHierarchy));
        ObjComplain.setAttribute("FROM",EITLERPGLOBAL.gNewUserID);
        ObjComplain.setAttribute("TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        ObjComplain.setAttribute("REJECTED_REMARKS",txtToRemarks.getText());
        
        if(OpgApprove.isSelected()) {
            ObjComplain.setAttribute("APPROVAL_STATUS","A");
        }
        
        if(OpgFinal.isSelected()) {
            ObjComplain.setAttribute("APPROVAL_STATUS","F");
        }
        
        if(OpgReject.isSelected()) {
            ObjComplain.setAttribute("APPROVAL_STATUS","R");
            ObjComplain.setAttribute("SEND_DOC_TO",EITLERPGLOBAL.getComboCode(cmbSendTo));
        }
        
        if(OpgHold.isSelected()) {
            ObjComplain.setAttribute("APPROVAL_STATUS","H");
        }*/
        //-------------------------------------------------//
        
      if(EditMode==EITLERPGLOBAL.ADD) {
            ObjComplain.setAttribute("CREATED_BY",EITLERPGLOBAL.gNewUserID);
            ObjComplain.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
        else {
         //   ObjComplain.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gNewUserID);
            ObjComplain.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        }
     
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
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7018,70181)) {
            cmdNew.setEnabled(true);
        }
        else {
            cmdNew.setEnabled(false);
        }
        
        // --- Edit Rights --
        cmdEdit.setEnabled(true);
        
        // --- Delete Rights --
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7018,70183)) {
            cmdDelete.setEnabled(true);
        }
        else {
            cmdDelete.setEnabled(false);
        }
        
        // --- Print Rights --
        /*
        if(clsUser.IsFunctionGranted(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gNewUserID, 7018,70184)) {
            cmdPreview.setEnabled(true);
            cmdPrint.setEnabled(true);
        }
        else {
            cmdPreview.setEnabled(false);
            cmdPrint.setEnabled(false);
        }
         */
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
        ObjComplain.Filter(" WHERE PARTY_CODE IN (SELECT D_SAL_PARTY_MASTER.PARTY_CODE FROM D_SAL_PARTY_MASTER,D_COM_DOC_DATA WHERE D_SAL_PARTY_MASTER.PARTY_CODE=D_COM_DOC_DATA.DOC_NO AND D_SAL_PARTY_MASTER.COMPANY_ID=D_COM_DOC_DATA.COMPANY_ID AND D_SAL_PARTY_MASTER.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND D_COM_DOC_DATA.USER_ID="+EITLERPGLOBAL.gNewUserID+" AND D_COM_DOC_DATA.STATUS='W' AND MODULE_ID="+clsComplain.ModuleID+")");
        ObjComplain.MoveFirst();
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
                    IncludeUser=ApprovalFlow.IncludeUserInApproval(EITLERPGLOBAL.gCompanyID, clsComplain.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
                }
                
                if(OpgReject.isSelected()) {
                    IncludeUser=ApprovalFlow.IncludeUserInRejection(EITLERPGLOBAL.gCompanyID, clsComplain.ModuleID, PartyCode, (int) ObjUser.getAttribute("USER_ID").getVal(), EITLERPGLOBAL.gNewUserID);
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
            String DocNo=(String)ObjComplain.getAttribute("PARTY_CODE").getObj();
            int Creator=ApprovalFlow.getCreator(EITLERPGLOBAL.gCompanyID, clsComplain.ModuleID, DocNo);
            EITLERPGLOBAL.setComboIndex(cmbSendTo,Creator);
        }
        
    }
    
    public void FindEx(int pCompanyID,String pComplaintNo) {
        System.out.println(pComplaintNo);        
    //public void FindEx(int pCompanyID,String pPartyCode,String Maincode) {
        ObjComplain.Filter(" WHERE CPNT_NO='"+pComplaintNo+"'");
        ObjComplain.MoveFirst();
        DisplayData();
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
     /*  HashMap List=new HashMap();
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
