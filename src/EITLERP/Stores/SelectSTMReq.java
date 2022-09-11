package EITLERP.Stores;

/**
 *
 * @author  nhpatel
 */
/*<APPLET CODE=SelectMIR.Class HEIGHT=400 WIDTH=700></APPLET>*/

import javax.swing.*;
import java.awt.*;
import java.util.*;
import EITLERP.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;


public class SelectSTMReq extends javax.swing.JApplet {
    
      
    private EITLTableModel DataModel;
    private EITLTableCellRenderer Rend=new EITLTableCellRenderer();
    
    public boolean Cancelled=true;
    public HashMap colSelItems=new HashMap();
    private JDialog aDialog;
    public int ModuleID=0;
    private String SelSTMNo="";
    
    public String SQL;
    public int ReturnCol;
    public boolean ShowReturnCol;
    public int DefaultSearchOn;
    String ReturnVal="";
    
    public boolean CopyHeader=true;
    public clsSTMRequisitionRaw ObjSTMReq;
    
    private EITLComboModel cmbDeptModel;
    private int SelDeptID=0;
    public int IssueType=2;
    
    /** Creates new form LOV */
    public SelectSTMReq() {
        System.gc();
        
        initComponents();
        FormatGrid();
        GenerateCombo();
        int DeptID=clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID);
        EITLERPGLOBAL.setComboIndex(cmbDept,DeptID);
        
    }
    
    public void init() {
        System.gc();
        initComponents();
        FormatGrid();
        GenerateCombo();
        
        int DeptID=clsUser.getDeptID(EITLERPGLOBAL.gCompanyID, EITLERPGLOBAL.gUserID);
        EITLERPGLOBAL.setComboIndex(cmbDept,DeptID);
        
    }
    
    public SelectSTMReq(String pSQL, int pReturnCol, boolean pShowReturnCol, int pDefaultSearchOn) {
        initComponents();
        DataModel=new EITLTableModel();
        SQL=pSQL;
        ReturnCol=pReturnCol;
        ShowReturnCol=pShowReturnCol;
        DefaultSearchOn=pDefaultSearchOn;
        //  lblSearch.setDisplayedMnemonic('S');
        //  lblSearch.setLabelFor((Component) txtSearch);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSTMReqNo = new javax.swing.JTextField();
        cmdShow = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        cmdOK = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();
        cmdSelectAll = new javax.swing.JButton();
        cmdClearAll = new javax.swing.JButton();
        chkCopyHeader = new javax.swing.JCheckBox();
        cmbDept = new javax.swing.JComboBox();
        chkDept = new javax.swing.JCheckBox();

        getContentPane().setLayout(null);

        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSearchKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12));
        jLabel1.setText("Select STM Requisition Items from the List");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 10, 350, 15);

        jLabel2.setDisplayedMnemonic('M');
        jLabel2.setText("Req. No.");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(15, 79, 56, 15);

        txtSTMReqNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSTMReqNoActionPerformed(evt);
            }
        });
        txtSTMReqNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSTMReqNoKeyPressed(evt);
            }
        });

        getContentPane().add(txtSTMReqNo);
        txtSTMReqNo.setBounds(76, 74, 106, 21);

        cmdShow.setMnemonic('S');
        cmdShow.setText("Show Items");
        cmdShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdShowActionPerformed(evt);
            }
        });

        getContentPane().add(cmdShow);
        cmdShow.setBounds(190, 74, 114, 22);

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

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(12, 124, 660, 225);

        jPanel4.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jPanel4);
        jPanel4.setBounds(8, 114, 662, 6);

        cmdOK.setText("OK");
        cmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOKActionPerformed(evt);
            }
        });

        getContentPane().add(cmdOK);
        cmdOK.setBounds(500, 360, 78, 25);

        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        getContentPane().add(cmdCancel);
        cmdCancel.setBounds(590, 360, 79, 25);

        cmdSelectAll.setText("Select All");
        cmdSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectAllActionPerformed(evt);
            }
        });

        getContentPane().add(cmdSelectAll);
        cmdSelectAll.setBounds(20, 360, 106, 25);

        cmdClearAll.setText("Clear All");
        cmdClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearAllActionPerformed(evt);
            }
        });
        cmdClearAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdClearAllKeyPressed(evt);
            }
        });

        getContentPane().add(cmdClearAll);
        cmdClearAll.setBounds(130, 360, 102, 25);

        chkCopyHeader.setMnemonic('C');
        chkCopyHeader.setSelected(true);
        chkCopyHeader.setText("Copy Header Information");
        getContentPane().add(chkCopyHeader);
        chkCopyHeader.setBounds(474, 10, 187, 23);

        cmbDept.setEnabled(false);
        getContentPane().add(cmbDept);
        cmbDept.setBounds(76, 46, 224, 24);

        chkDept.setText("Dept");
        chkDept.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkDeptStateChanged(evt);
            }
        });
        chkDept.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDeptItemStateChanged(evt);
            }
        });

        getContentPane().add(chkDept);
        chkDept.setBounds(11, 46, 62, 23);

    }//GEN-END:initComponents
    
    private void chkDeptStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkDeptStateChanged
        // TODO add your handling code here:
        if(chkDept.isSelected()) {
            cmbDept.setEnabled(true);
        }
        else {
            cmbDept.setEnabled(false);
        }
        
    }//GEN-LAST:event_chkDeptStateChanged
    
    private void chkDeptItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDeptItemStateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_chkDeptItemStateChanged
    
    private void txtSTMReqNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSTMReqNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSTMReqNoActionPerformed
    
    private void cmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOKActionPerformed
        // TODO add your handling code here:
        if(Table.getRowCount()<=0) {
            Cancelled=true;
        }
        else {
            SetList();
            Cancelled=false;
        }
        aDialog.dispose();
    }//GEN-LAST:event_cmdOKActionPerformed
    
    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        // TODO add your handling code here:
        Cancelled=true;
        aDialog.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed
    
    private void cmdSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<Table.getRowCount();i++) {
            DataModel.setValueAt(Boolean.valueOf(true), i, 0);
        }
    }//GEN-LAST:event_cmdSelectAllActionPerformed
    
    private void cmdClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearAllActionPerformed
        // TODO add your handling code here:
        for(int i=0;i<Table.getRowCount();i++) {
            DataModel.setValueAt(Boolean.valueOf(false), i, 0);
        }
    }//GEN-LAST:event_cmdClearAllActionPerformed
    
    private void cmdClearAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdClearAllKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdClearAllKeyPressed
    
    private void cmdShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdShowActionPerformed
        // TODO add your handling code here:
        SelSTMNo=txtSTMReqNo.getText();
        
        String docStatus=clsSTMRequisitionRaw.getDocStatus(EITLERPGLOBAL.gCompanyID, SelSTMNo);
        
        if(!docStatus.trim().equals("")) {
            JOptionPane.showMessageDialog(null,docStatus);
            return;
        }
        
        FormatGrid();
        GenerateGrid();
        
        if(Table.getRowCount()==0&&docStatus.trim().equals("")) {
            JOptionPane.showMessageDialog(null,"No pending items found");
        }
    }//GEN-LAST:event_cmdShowActionPerformed
    
    private void txtSTMReqNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSTMReqNoKeyPressed
        // TODO add your handling code here:
        //=========== Supplier List ===============
        if(evt.getKeyCode()==112) //F1 Key pressed
        {
            String strSQL="";
            LOV aList=new LOV();
            SelDeptID=EITLERPGLOBAL.getComboCode(cmbDept);
            
            if(chkDept.isSelected()) {
                
                //aList.SQL="SELECT DISTINCT(A.STM_REQ_NO),DATE_FORMAT(C.STM_REQ_DATE,'%d/%m/%Y') AS STM_REQ_DATE FROM D_INV_STM_REQ_DETAIL A LEFT OUTER JOIN D_INV_STM_DETAIL B ON (A.STM_REQ_NO=B.STM_REQ_NO AND A.STM_REQ_SR_NO=B.STM_REQ_SR_NO) LEFT OUTER JOIN D_INV_ITEM_MASTER ITEM ON (A.ITEM_CODE=ITEM.ITEM_ID),D_INV_STM_REQ_HEADER C LEFT JOIN D_COM_DEPT_MASTER D ON (C.DEST_DEPT_ID=D.DEPT_ID) WHERE C.STORE='"+IssueType+"' AND A.STM_REQ_NO=C.STM_REQ_NO AND A.COMPANY_ID=C.COMPANY_ID AND IF(QTY IS NULL,0,QTY) <STM_REQ_QTY AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND C.DEST_DEPT_ID="+SelDeptID+" AND C.APPROVED=1 AND C.CANCELED=0 AND ITEM.ONETIME<>1 ORDER BY A.STM_REQ_NO ";
                
                strSQL="SELECT DISTINCT(A.STM_REQ_NO),DATE_FORMAT(A.STM_REQ_DATE,'%d/%m/%Y') AS STM_REQ_DATE,B.STM_REQ_QTY ";
                strSQL+="FROM ";
                strSQL+="D_INV_STM_REQ_HEADER A, ";
                strSQL+="D_INV_STM_REQ_DETAIL B ";
                strSQL+="LEFT JOIN D_INV_STM_DETAIL I ON (I.STM_REQ_NO=B.STM_REQ_NO AND I.STM_REQ_SR_NO=B.STM_REQ_SR_NO AND I.STM_NO " ;
                strSQL+="IN (SELECT STM_NO FROM D_INV_STM_HEADER WHERE STM_NO=I.STM_NO AND APPROVED=1 AND CANCELLED=0)), ";
                strSQL+="D_COM_DEPT_MASTER D, ";
                strSQL+="D_INV_ITEM_MASTER E ";
                strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
                strSQL+="B.ITEM_ID=E.ITEM_ID AND E.MAINTAIN_STOCK=1 AND ";
                strSQL+="A.STM_REQ_NO=B.STM_REQ_NO AND ";
                strSQL+="A.STM_REQ_TYPE=B.STM_REQ_TYPE AND ";
                strSQL+="A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ";
                strSQL+="A.APPROVED=1 AND A.CANCELLED=0 AND ";
                strSQL+="B.COMPANY_ID=D.COMPANY_ID AND A.FOR_DEPT_ID=D.DEPT_ID AND ";
                strSQL+="A.STM_REQ_TYPE="+IssueType+" AND ";
                strSQL+="A.FOR_DEPT_ID="+SelDeptID+" ";
                strSQL+="GROUP BY B.STM_REQ_NO,B.STM_REQ_TYPE,B.STM_REQ_SR_NO ";
                strSQL+="HAVING IF(SUM(I.QTY) IS NULL,0,SUM(I.QTY)) <B.STM_REQ_QTY ";
                
                aList.SQL=strSQL;
            }
            else {
                //aList.SQL="SELECT DISTINCT(A.STM_REQ_NO),DATE_FORMAT(C.STM_REQ_DATE,'%d/%m/%Y') AS STM_REQ_DATE FROM D_INV_STM_REQ_DETAIL A LEFT OUTER JOIN D_INV_STM_DETAIL B ON (A.STM_REQ_NO=B.STM_REQ_NO AND A.STM_REQ_SR_NO=B.STM_REQ_SR_NO) LEFT OUTER JOIN D_INV_ITEM_MASTER ITEM ON (A.ITEM_CODE=ITEM.ITEM_ID),D_INV_STM_REQ_HEADER C LEFT JOIN D_COM_DEPT_MASTER D ON (C.DEST_DEPT_ID=D.DEPT_ID) WHERE C.STORE='"+IssueType+"' AND A.STM_REQ_NO=C.STM_REQ_NO AND A.COMPANY_ID=C.COMPANY_ID AND IF(QTY IS NULL,0,QTY) <STM_REQ_QTY AND A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND C.APPROVED=1 AND C.CANCELED=0 AND ITEM.ONETIME<>1 ORDER BY A.STM_REQ_NO";
                
                strSQL="SELECT DISTINCT(A.STM_REQ_NO),DATE_FORMAT(A.STM_REQ_DATE,'%d/%m/%Y') AS STM_REQ_DATE,B.STM_REQ_QTY ";
                strSQL+="FROM ";
                strSQL+="D_INV_STM_REQ_HEADER A, ";
                strSQL+="D_INV_STM_REQ_DETAIL B ";
                strSQL+="LEFT JOIN D_INV_STM_DETAIL I ON (I.STM_REQ_NO=B.STM_REQ_NO AND I.STM_REQ_SR_NO=B.STM_REQ_SR_NO " ;
                strSQL+=" AND I.STM_NO IN (SELECT STM_NO FROM D_INV_STM_HEADER WHERE STM_NO=I.STM_NO AND APPROVED=1 AND CANCELLED=0)), ";
                strSQL+="D_COM_DEPT_MASTER D, ";
                strSQL+="D_INV_ITEM_MASTER E ";
                strSQL+="WHERE A.COMPANY_ID=B.COMPANY_ID AND ";
                strSQL+="B.ITEM_ID=E.ITEM_ID AND E.MAINTAIN_STOCK=1 AND ";
                strSQL+="A.STM_REQ_NO=B.STM_REQ_NO AND ";
                strSQL+="A.STM_REQ_TYPE=B.STM_REQ_TYPE AND ";
                strSQL+="A.COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND ";
                strSQL+="A.APPROVED=1 AND A.CANCELLED=0 AND ";
                strSQL+="B.COMPANY_ID=D.COMPANY_ID AND A.FOR_DEPT_ID=D.DEPT_ID AND ";
                strSQL+="A.STM_REQ_TYPE="+IssueType+" ";
                strSQL+="GROUP BY B.STM_REQ_NO,B.STM_REQ_TYPE,B.STM_REQ_SR_NO ";
                strSQL+="HAVING ROUND(IF(SUM(I.QTY) IS NULL,0,SUM(I.QTY)),3)<ROUND(B.STM_REQ_QTY,3) ";
                
                aList.SQL=strSQL;
            }
            
            aList.ReturnCol=1;
            aList.ShowReturnCol=true;
            aList.DefaultSearchOn=1;
            
            if(aList.ShowLOV()) {
                txtSTMReqNo.setText(aList.ReturnVal);
            }
        }
        //=========================================
    }//GEN-LAST:event_txtSTMReqNoKeyPressed
    
    private void txtSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyPressed
        // Add your handling code here:
        try {
            if(evt.getKeyCode()==10) //Enter key pressed
            {
                if(Table.getRowCount()<=0) {
                    Cancelled=true;
                }
                else {
                    Cancelled=false;
                    ReturnVal=(String) DataModel.getValueAt(Table.getSelectedRow(),ReturnCol-1);
                }
                aDialog.dispose();
                return;
            }
            
            if(evt.getKeyCode()==27) //Escape key pressed
            {
                Cancelled=true;
                ReturnVal="";
                aDialog.dispose();
                return;
            }
            
            if(evt.getKeyCode()==40) //Down Arrow key pressed
            {
                if(Table.getSelectedRow()<Table.getRowCount()) {
                    Table.changeSelection(Table.getSelectedRow()+1,DefaultSearchOn-1,false,false);
                }
                return;
            }
            
            
            if(evt.getKeyCode()==38) //Up Arrow key pressed
            {
                if(Table.getSelectedRow()>=0) {
                    Table.changeSelection(Table.getSelectedRow()-1,DefaultSearchOn-1,false,false);
                }
                
                return;
            }
            
        }
        catch(Exception e)
        {}
    }//GEN-LAST:event_txtSearchKeyPressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Table;
    private javax.swing.JCheckBox chkCopyHeader;
    private javax.swing.JCheckBox chkDept;
    private javax.swing.JComboBox cmbDept;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdClearAll;
    private javax.swing.JButton cmdOK;
    private javax.swing.JButton cmdSelectAll;
    private javax.swing.JButton cmdShow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtSTMReqNo;
    // End of variables declaration//GEN-END:variables
    
    
    private void FormatGrid() {
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(int i=1;i<=11;i++) {
            DataModel.SetReadOnly(i);
        }
        
        //Add Columns to it
        DataModel.addColumn(""); //0 Selection
        DataModel.addColumn("Sr.");//1
        DataModel.addColumn("Item Code");//2
        DataModel.addColumn("Item Name");//3
        DataModel.addColumn("Qty");//4
        DataModel.addColumn("Unit ID");//5
        DataModel.addColumn("Unit");//6
        DataModel.addColumn("Bal. Qty");//7
        DataModel.addColumn("STM Req No.");//8
        DataModel.addColumn("Issued Qty");//9
        
        Rend.setCustomComponent(0,"CheckBox");
        Table.getColumnModel().getColumn(0).setCellRenderer(Rend);
        Table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
    }
    
    private void GenerateGrid() {
        HashMap List=new HashMap();
        String STMNo=txtSTMReqNo.getText();
        
        if(!STMNo.equals("")) {
            List=clsSTMRequisitionRaw.getSTMReqItemList(EITLERPGLOBAL.gCompanyID, STMNo,false,IssueType);
            
            for(int i=1;i<=List.size();i++) {
                clsSTMReqItem ObjItem=(clsSTMReqItem)List.get(Integer.toString(i));
                Object[] rowData=new Object[11];
                
                rowData[0]=Boolean.valueOf(true); //By default not selected
                rowData[1]=Integer.toString((int)ObjItem.getAttribute("STM_REQ_SR_NO").getVal());
                rowData[2]=(String)ObjItem.getAttribute("ITEM_ID").getObj();
                rowData[3]=clsItem.getItemName(EITLERPGLOBAL.gCompanyID,(String)ObjItem.getAttribute("ITEM_ID").getObj());
                rowData[4]=Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("STM_REQ_QTY").getDouble(),3));
                rowData[5]=Double.toString(ObjItem.getAttribute("UNIT").getDouble());
                int lItemUnit= ObjItem.getAttribute("UNIT").getInt();
                rowData[6]=clsParameter.getParaDescription(EITLERPGLOBAL.gCompanyID, "UNIT", lItemUnit);
                rowData[7]=Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("BAL_QTY").getDouble(),3));
                rowData[8]=(String) ObjItem.getAttribute("STM_REQ_NO").getObj();
                rowData[9]=Double.toString(EITLERPGLOBAL.round(ObjItem.getAttribute("ISSUED_QTY").getDouble(),3));
                
                DataModel.addRow(rowData);
                
                //Set the Collection
                DataModel.SetUserObject(Table.getRowCount()-1, ObjItem);
            }
        }
        
    }
    
    private void SetList() {
        int SrNo=0;
        HashMap List=new HashMap();
        
        colSelItems.clear();
        String STMNo=txtSTMReqNo.getText();
        
        //========= Get the MR Object =============//
        clsSTMRequisitionRaw tmpObj=new clsSTMRequisitionRaw();
        ObjSTMReq=(clsSTMRequisitionRaw)tmpObj.getObject(EITLERPGLOBAL.gCompanyID, STMNo);
        
        CopyHeader=chkCopyHeader.isSelected();
        //===========================================//
        
        
        List=clsSTMRequisitionRaw.getSTMReqItemList(EITLERPGLOBAL.gCompanyID, STMNo, false,IssueType);
        //Scan the Table to get selected items
        for(int i=1;i<=List.size();i++) {
            clsSTMReqItem ObjItem=(clsSTMReqItem)List.get(Integer.toString(i));
            SrNo=(int)ObjItem.getAttribute("STM_REQ_SR_NO").getVal();
            
            //Search in the table for SrNo.
            for(int j=0;j<Table.getRowCount();j++) {
                if(Integer.parseInt(Table.getValueAt(j, 1).toString())==SrNo) {
                    if(Table.getValueAt(j,0).toString().equals("true")) {
                        //Selected Item
                        colSelItems.put(Integer.toString(colSelItems.size()+1),ObjItem);
                    }
                }
            }
        }
    }
    
    
    private Frame findParentFrame(JApplet pApplet) {
        Container c = (Container) pApplet;
        while(c != null) {
            if (c instanceof Frame)
                return (Frame)c;
            
            c = c.getParent();
        }
        return (Frame)null;
    }
    
    public boolean ShowList() {
        try {
            FormatGrid();
            
            setSize(700,450);
            
            Frame f=findParentFrame(this);
            
            aDialog=new JDialog(f,"Select Requisition Items",true);
            
            aDialog.getContentPane().add("Center",this);
            Dimension appletSize = this.getSize();
            aDialog.setSize(appletSize);
            aDialog.setResizable(false);
            
            //Place it to center of the screen
            Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
            aDialog.setLocation((int)(screenSize.width-appletSize.getWidth())/2,(int)(screenSize.height-appletSize.getHeight())/2);
            
            aDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            aDialog.show();
        }
        catch(Exception e) {
        }
        return !Cancelled;
    }
    
    private void GenerateCombo() {
        HashMap List=new HashMap();
        String strCondition="";
        
        //----- Generate cmbType ------- //
        cmbDeptModel=new EITLComboModel();
        cmbDept.removeAllItems();
        cmbDept.setModel(cmbDeptModel);
        
        strCondition=" WHERE COMPANY_ID="+Long.toString(EITLERPGLOBAL.gCompanyID)+" ";
        
        List=clsDepartment.getList(strCondition);
        for(int i=1;i<=List.size();i++) {
            clsDepartment ObjDept=(clsDepartment) List.get(Integer.toString(i));
            ComboData aData=new ComboData();
            aData.Code=(int) ObjDept.getAttribute("DEPT_ID").getVal();
            aData.Text=(String)ObjDept.getAttribute("DEPT_DESC").getObj();
            aData.strCode="";
            cmbDeptModel.addElement(aData);
        }
        //------------------------------ //
    }
    
}

