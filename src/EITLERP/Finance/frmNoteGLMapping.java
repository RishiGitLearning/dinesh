/*
 * frmItemCriteria.java
 *
 * Created on May 28, 2005, 3:04 PM
 */

package EITLERP.Finance;

/**
 *
 * @author  root
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import EITLERP.EITLComboModel;
import EITLERP.EITLTableModel;
import EITLERP.EITLERPGLOBAL;
import EITLERP.clsFinYear;
import EITLERP.ComboData;
import EITLERP.LOV;
import EITLERP.data;

public class frmNoteGLMapping extends javax.swing.JApplet {
    
    
    private EITLComboModel cmbFromModel=new EITLComboModel();
    private EITLComboModel cmbNoteModel=new EITLComboModel();
    private EITLComboModel cmbSubNoteModel=new EITLComboModel();
    
    private EITLTableModel DataModel=new EITLTableModel();
    
    private clsNoteGLMapping ObjNoteGLMapping=new clsNoteGLMapping();
    
    
    /** Initializes the applet frmItemCriteria */
    public void init() {
        initComponents();
        GenerateComboYear();
        
        setSize(675, 590);
        GenerateGrid();
    }
    
    
    
    
    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmdCopyMapping = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbFromYear = new javax.swing.JComboBox();
        txtToYear = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbNote = new javax.swing.JComboBox();
        lblStartsWith = new javax.swing.JLabel();
        txtMainCode = new javax.swing.JTextField();
        lblAccountName = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        OpgL = new javax.swing.JRadioButton();
        OpgS = new javax.swing.JRadioButton();
        OpgN = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        cmbSubNote = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jPanel1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(0, 102, 204));
        jPanel1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("  NOTE AND GL MAPPING");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(7, 10, 322, 14);

        cmdCopyMapping.setText("Copy Mapping");
        cmdCopyMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCopyMappingActionPerformed(evt);
            }
        });

        jPanel1.add(cmdCopyMapping);
        cmdCopyMapping.setBounds(513, 7, 130, 25);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(2, 2, 660, 35);

        jPanel2.setLayout(null);

        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Select For Year :");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 15, 100, 20);

        cmbFromYear.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFromYearItemStateChanged(evt);
            }
        });
        cmbFromYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbFromYearFocusGained(evt);
            }
        });

        jPanel2.add(cmbFromYear);
        cmbFromYear.setBounds(115, 15, 120, 24);

        txtToYear.setEnabled(false);
        jPanel2.add(txtToYear);
        txtToYear.setBounds(250, 15, 118, 21);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Select Note :");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(10, 45, 100, 20);

        cmbNote.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbNoteItemStateChanged(evt);
            }
        });
        cmbNote.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbNoteFocusGained(evt);
            }
        });

        jPanel2.add(cmbNote);
        cmbNote.setBounds(115, 45, 490, 24);

        lblStartsWith.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStartsWith.setText("Enter GL Code :");
        jPanel2.add(lblStartsWith);
        lblStartsWith.setBounds(10, 105, 100, 20);

        txtMainCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMainCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMainCodeFocusLost(evt);
            }
        });
        txtMainCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMainCodeKeyPressed(evt);
            }
        });

        jPanel2.add(txtMainCode);
        txtMainCode.setBounds(115, 105, 118, 21);

        lblAccountName.setText("...");
        jPanel2.add(lblAccountName);
        lblAccountName.setBounds(240, 105, 370, 20);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Indicator :");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 135, 100, 20);

        OpgL.setFont(new java.awt.Font("Tahoma", 1, 11));
        OpgL.setText(" L");
        buttonGroup1.add(OpgL);
        jPanel2.add(OpgL);
        OpgL.setBounds(115, 135, 50, 20);

        OpgS.setFont(new java.awt.Font("Tahoma", 1, 11));
        OpgS.setText(" S");
        buttonGroup1.add(OpgS);
        jPanel2.add(OpgS);
        OpgS.setBounds(180, 135, 50, 20);

        OpgN.setFont(new java.awt.Font("Tahoma", 1, 11));
        OpgN.setSelected(true);
        OpgN.setText(" N");
        buttonGroup1.add(OpgN);
        jPanel2.add(OpgN);
        OpgN.setBounds(245, 135, 50, 20);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Select Sub Note :");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 75, 100, 20);

        cmbSubNote.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubNoteItemStateChanged(evt);
            }
        });
        cmbSubNote.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbSubNoteFocusGained(evt);
            }
        });

        jPanel2.add(cmbSubNote);
        cmbSubNote.setBounds(115, 75, 490, 24);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(7, 41, 644, 180);

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
        jScrollPane1.setBounds(9, 260, 643, 261);

        cmdAdd.setText("Add Condition");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        getContentPane().add(cmdAdd);
        cmdAdd.setBounds(351, 230, 138, 25);

        cmdRemove.setText("Remove Condition");
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        getContentPane().add(cmdRemove);
        cmdRemove.setBounds(497, 230, 151, 25);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblStatus.setForeground(new java.awt.Color(51, 51, 255));
        lblStatus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(lblStatus);
        lblStatus.setBounds(4, 525, 650, 22);

    }//GEN-END:initComponents

    private void txtMainCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMainCodeFocusLost
        // TODO add your handling code here:
        if(!txtMainCode.getText().trim().equals("")) {
            if(clsAccount.IsValidAccount(txtMainCode.getText().trim(),"")) {
                lblAccountName.setText(clsAccount.getAccountName(txtMainCode.getText().trim(), ""));
            } else {
                lblAccountName.setText("...");
            }
        } else {
            lblAccountName.setText("...");
        }
    }//GEN-LAST:event_txtMainCodeFocusLost

    private void txtMainCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMainCodeFocusGained
        // TODO add your handling code here:
        showMessage("Press F1 or Mannualy Enter Main Code.");
    }//GEN-LAST:event_txtMainCodeFocusGained

    private void cmbSubNoteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbSubNoteFocusGained
        // TODO add your handling code here:
        showMessage("Select Sub Note.");
    }//GEN-LAST:event_cmbSubNoteFocusGained

    private void cmbNoteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbNoteFocusGained
        // TODO add your handling code here:
        showMessage("Select Note.");
    }//GEN-LAST:event_cmbNoteFocusGained

    private void cmbFromYearFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbFromYearFocusGained
        // TODO add your handling code here:
        showMessage("Select From Year.");
    }//GEN-LAST:event_cmbFromYearFocusGained
    
    private void cmdCopyMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCopyMappingActionPerformed
        // TODO add your handling code here:
        try {
            SelectFinancialYear ObjFyear=new SelectFinancialYear(); 
            if(ObjFyear.ShowList()) {
                int yearFromCopy = ObjFyear.SelYearFromGetCopy;
                //int yearToCopy = yearFromCopy+1;
                int yearFromPaste = ObjFyear.SelYearFromSetCopy;
                if(data.IsRecordExist("SELECT * FROM D_FIN_NOTE_GL_MAPPING WHERE YEAR_FROM="+yearFromPaste+"",FinanceGlobal.FinURL)) {
                    JOptionPane.showMessageDialog(this,yearFromPaste+" already added in mapping.","Copy Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //int yearToPaste = yearFromPaste+1;
                System.out.println("Copy : " + yearFromCopy);
                System.out.println("Paste : " + yearFromPaste);
                if(!ObjNoteGLMapping.InsertCopy(yearFromCopy,yearFromPaste)) {
                    JOptionPane.showMessageDialog(this,"Error occured during coping previous year data.","Copy Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    
    }//GEN-LAST:event_cmdCopyMappingActionPerformed
    
    private void cmbSubNoteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubNoteItemStateChanged
        // TODO add your handling code here:
        if(cmbSubNote.getSelectedIndex()!=0) {
            txtMainCode.setText("");
            lblAccountName.setText("");
            txtMainCode.setEnabled(true);
        } else {
            txtMainCode.setText("");
            lblAccountName.setText("...");
            txtMainCode.setEnabled(false);
        }
        
    }//GEN-LAST:event_cmbSubNoteItemStateChanged
    
    private void txtMainCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMainCodeKeyPressed
        // TODO add your handling code here:
        try {
            if(evt.getKeyCode()==112) {
                LOV aList=new LOV();
                
                aList.SQL="SELECT MAIN_ACCOUNT_CODE,ACCOUNT_NAME FROM D_FIN_GL WHERE  APPROVED=1 ORDER BY ACCOUNT_NAME";
                aList.ReturnCol=1;
                aList.ShowReturnCol=true;
                aList.DefaultSearchOn=1;
                aList.UseSpecifiedConn=true;
                aList.dbURL=FinanceGlobal.FinURL;
                
                if(aList.ShowLOV()) {
                    txtMainCode.setText(aList.ReturnVal);
                    lblAccountName.setText(clsAccount.getAccountName(aList.ReturnVal,""));
                }
            }
        } catch(Exception e) {
            
        }
    }//GEN-LAST:event_txtMainCodeKeyPressed
    
    private void cmbFromYearItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFromYearItemStateChanged
        // TODO add your handling code here:
        try {
            if(cmbFromYear.getSelectedIndex()!=0) {
                int ToYear=Integer.parseInt((String)cmbFromYear.getSelectedItem())+1;
                txtToYear.setText(Integer.toString(ToYear));
                
                int YearFrom=Integer.parseInt(cmbFromYear.getSelectedItem().toString());
                
                HashMap List=new HashMap();
                
                cmbNote.setModel(cmbNoteModel);
                cmbNote.removeAllItems();
                
                List=clsNote.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID + " AND YEAR_FROM="+YearFrom+" ORDER BY NOTE");
                
                ComboData cmbData=new ComboData();
                cmbData.Text="Select Note";
                cmbData.Code=0;
                cmbData.strCode ="0";
                cmbNoteModel.addElement(cmbData);
                for(int i=1;i<=List.size();i++) {
                    clsNote ObjNote =(clsNote) List.get(Integer.toString(i));
                    
                    cmbData=new ComboData();
                    cmbData.Text=ObjNote.getAttribute("NOTE_NAME").getString();
                    cmbData.Code=ObjNote.getAttribute("NOTE").getInt();
                    cmbNoteModel.addElement(cmbData);
                }
                cmbNote.setSelectedIndex(0);
                
                GenerateGrid();
                
            } else {
                txtToYear.setText("");
                
                HashMap List=new HashMap();
                cmbNote.setModel(cmbNoteModel);
                cmbNote.removeAllItems();
                ComboData cmbData=new ComboData();
                cmbData.Text="Select Note";
                cmbData.Code=0;
                cmbData.strCode ="0";
                cmbNoteModel.addElement(cmbData);
                cmbNote.setSelectedIndex(0);
                GenerateGrid();
            }
        } catch(Exception e) {
        }
    }//GEN-LAST:event_cmbFromYearItemStateChanged
    
    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        // TODO add your handling code here:
        if(Table.getSelectedRow()>=0 && Table.getRowCount()>0) {
            if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete selected mapping ?","Confirmation",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                int SrNo=Integer.parseInt((String)Table.getValueAt(Table.getSelectedRow(), 0));
                int YearFrom=Integer.parseInt((String)Table.getValueAt(Table.getSelectedRow(), 1));
                ObjNoteGLMapping.Remove(EITLERPGLOBAL.gCompanyID, SrNo,YearFrom);
                GenerateGrid();
            }
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed
    
    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        
        if(cmbFromYear.getSelectedIndex()<=0) {
            JOptionPane.showMessageDialog(this,"Please select From Year.");
            return;
        }
        
        if(cmbNote.getSelectedIndex()<=0) {
            JOptionPane.showMessageDialog(this,"Please select Note.");
            return;
        }
        
        if(cmbSubNote.getSelectedIndex()<=0) {
            JOptionPane.showMessageDialog(this,"Please select Sub Note.");
            return;
        }
        
        String MainCode = txtMainCode.getText().trim();
        if(MainCode.equals("")) {
            JOptionPane.showMessageDialog(this,"Please Enter GL Code.");
            return;
        }
        
        if(!clsAccount.IsValidAccount(MainCode,"")) {
            JOptionPane.showMessageDialog(this,"Please Enter valid GL Code.");
            return;
        }
        
        String Indicator="N";
        
        if(OpgL.isSelected()) {
            Indicator="L";
        }
        if(OpgS.isSelected()) {
            Indicator="S";
        }
        if(OpgN.isSelected()) {
            Indicator="N";
        }
        
        //Set the Data
        ObjNoteGLMapping.setAttribute("COMPANY_ID",EITLERPGLOBAL.gCompanyID);
        ObjNoteGLMapping.setAttribute("SR_NO",0);
        ObjNoteGLMapping.setAttribute("YEAR_FROM",EITLERPGLOBAL.getComboCode(cmbFromYear));
        ObjNoteGLMapping.setAttribute("YEAR_TO",Integer.parseInt(txtToYear.getText().trim()));
        ObjNoteGLMapping.setAttribute("NOTE",EITLERPGLOBAL.getComboCode(cmbNote));
        ObjNoteGLMapping.setAttribute("SUB_NOTE",EITLERPGLOBAL.getComboCode(cmbSubNote));
        ObjNoteGLMapping.setAttribute("MAIN_ACCOUNT_CODE",txtMainCode.getText().trim());
        ObjNoteGLMapping.setAttribute("INDICATOR",Indicator);
        ObjNoteGLMapping.setAttribute("CREATED_BY",EITLERPGLOBAL.gUserID);
        ObjNoteGLMapping.setAttribute("CREATED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        ObjNoteGLMapping.setAttribute("MODIFIED_BY",EITLERPGLOBAL.gUserID);
        ObjNoteGLMapping.setAttribute("MODIFIED_DATE",EITLERPGLOBAL.getCurrentDateDB());
        
        if(ObjNoteGLMapping.Insert()) {
            GenerateGrid();
            txtMainCode.setText("");
            lblAccountName.setText("...");
            OpgN.setSelected(true);
            txtMainCode.requestFocus();
        }
        else {
            JOptionPane.showMessageDialog(null,"Error occured while saving : " + ObjNoteGLMapping.LastError);
            txtMainCode.setText("");
            lblAccountName.setText("...");
            OpgN.setSelected(true);
            txtMainCode.requestFocus();
        }
    }//GEN-LAST:event_cmdAddActionPerformed
    
    private void cmbNoteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbNoteItemStateChanged
        // TODO add your handling code here:
        try {
            if(cmbNote.getSelectedIndex()!=0) {
                int YearFrom=Integer.parseInt(cmbFromYear.getSelectedItem().toString());
                long Note=cmbNoteModel.getCode(cmbNote.getSelectedIndex());
                
                HashMap List=new HashMap();
                
                cmbSubNote.setModel(cmbSubNoteModel);
                cmbSubNote.removeAllItems();
                
                List=clsNote.getSubList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID+" AND YEAR_FROM="+YearFrom+ " AND NOTE="+Note+" ORDER BY SR_NO");
                
                ComboData cmbData=new ComboData();
                cmbData.Text="Select Sub Note";
                cmbData.Code=0;
                cmbData.strCode ="0";
                cmbSubNoteModel.addElement(cmbData);
                for(int i=1;i<=List.size();i++) {
                    clsSubNote ObjSubNote =(clsSubNote) List.get(Integer.toString(i));
                    
                    cmbData=new ComboData();
                    cmbData.Text=ObjSubNote.getAttribute("SUB_NOTE_NAME").getString();
                    cmbData.Code=ObjSubNote.getAttribute("SR_NO").getInt();
                    cmbSubNoteModel.addElement(cmbData);
                }
                cmbSubNote.setSelectedIndex(0);
                
            } else {
                
                HashMap List=new HashMap();
                cmbSubNote.setModel(cmbSubNoteModel);
                cmbSubNote.removeAllItems();
                ComboData cmbData=new ComboData();
                cmbData.Text="Select Note";
                cmbData.Code=0;
                cmbData.strCode ="0";
                cmbSubNoteModel.addElement(cmbData);
                cmbSubNote.setSelectedIndex(0);
            }
        } catch(Exception e) {
        }
    }//GEN-LAST:event_cmbNoteItemStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton OpgL;
    private javax.swing.JRadioButton OpgN;
    private javax.swing.JRadioButton OpgS;
    private javax.swing.JTable Table;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbFromYear;
    private javax.swing.JComboBox cmbNote;
    private javax.swing.JComboBox cmbSubNote;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdCopyMapping;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAccountName;
    private javax.swing.JLabel lblStartsWith;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtMainCode;
    private javax.swing.JTextField txtToYear;
    // End of variables declaration//GEN-END:variables
    
    
    private void FormatGrid() {
        DataModel=new EITLTableModel();
        
        Table.removeAll();
        
        Table.setModel(DataModel);
        Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        DataModel.addColumn("Sr. No.");
        DataModel.addColumn("Year From");
        DataModel.addColumn("Year To");
        DataModel.addColumn("Note");
        DataModel.addColumn("Note Name");
        DataModel.addColumn("Sub Note");
        DataModel.addColumn("Sub Note Name");
        DataModel.addColumn("GL Code");
        DataModel.addColumn("Account Name");
        DataModel.addColumn("Indicator");
        
        DataModel.SetNumeric(0,true);
        DataModel.SetNumeric(1,true);
        DataModel.SetNumeric(2,true);
        DataModel.SetNumeric(3,true);
        DataModel.SetNumeric(5,true);
        Table.getColumnModel().getColumn(0).setPreferredWidth(40);
        Table.getColumnModel().getColumn(1).setPreferredWidth(60);
        Table.getColumnModel().getColumn(2).setPreferredWidth(60);
        Table.getColumnModel().getColumn(3).setPreferredWidth(50);
        Table.getColumnModel().getColumn(4).setPreferredWidth(90);
        Table.getColumnModel().getColumn(5).setPreferredWidth(60);
        Table.getColumnModel().getColumn(6).setPreferredWidth(90);
        Table.getColumnModel().getColumn(7).setPreferredWidth(70);
        Table.getColumnModel().getColumn(8).setPreferredWidth(90);
        Table.getColumnModel().getColumn(9).setPreferredWidth(60);
        DataModel.TableReadOnly(true);
        
    }
    
    
    private void GenerateGrid() {
        HashMap List=new HashMap();
        int YearFrom=EITLERPGLOBAL.getComboCode(cmbFromYear);
        List=ObjNoteGLMapping.getList(" WHERE YEAR_FROM="+YearFrom + " ORDER BY NOTE,SUB_NOTE,SR_NO");
        FormatGrid();
        
        for(int i=1;i<=List.size();i++) {
            Object[] rowData=new Object[10];
            clsNoteGLMapping ObjNoteGLMapping=(clsNoteGLMapping)List.get(Integer.toString(i));
            
            rowData[0]=Integer.toString(ObjNoteGLMapping.getAttribute("SR_NO").getInt());
            rowData[1]=Integer.toString(ObjNoteGLMapping.getAttribute("YEAR_FROM").getInt());
            rowData[2]=Integer.toString(ObjNoteGLMapping.getAttribute("YEAR_TO").getInt());
            rowData[3]=Integer.toString(ObjNoteGLMapping.getAttribute("NOTE").getInt());
            rowData[4]=clsNote.getNoteName(ObjNoteGLMapping.getAttribute("NOTE").getInt(),ObjNoteGLMapping.getAttribute("YEAR_FROM").getInt());
            rowData[5]=Integer.toString(ObjNoteGLMapping.getAttribute("SUB_NOTE").getInt());
            rowData[6]=clsNote.getSubNoteName(ObjNoteGLMapping.getAttribute("SUB_NOTE").getInt(),ObjNoteGLMapping.getAttribute("NOTE").getInt(),ObjNoteGLMapping.getAttribute("YEAR_FROM").getInt());
            rowData[7]=ObjNoteGLMapping.getAttribute("MAIN_ACCOUNT_CODE").getString();
            rowData[8]=clsAccount.getAccountName(ObjNoteGLMapping.getAttribute("MAIN_ACCOUNT_CODE").getString(),"");
            rowData[9]=ObjNoteGLMapping.getAttribute("INDICATOR").getString();
            DataModel.addRow(rowData);
        }
        
    }
    
    
    
    private void GenerateComboYear() {
        
        try {
            HashMap List=new HashMap();
            
            cmbFromYear.setModel(cmbFromModel);
            cmbFromYear.removeAllItems();
            
            List=clsFinYear.getList(" WHERE COMPANY_ID="+EITLERPGLOBAL.gCompanyID);
            
            ComboData cmbData=new ComboData();
            cmbData.Text="Select Year";
            cmbData.Code=0;
            cmbData.strCode ="0";
            cmbFromModel.addElement(cmbData);
            for(int i=1;i<=List.size();i++) {
                clsFinYear ObjYear =(clsFinYear) List.get(Integer.toString(i));
                
                cmbData=new ComboData();
                cmbData.Text=Integer.toString(ObjYear.getAttribute("YEAR_FROM").getInt());
                cmbData.Code=ObjYear.getAttribute("YEAR_FROM").getInt();
                cmbData.strCode =Integer.toString(ObjYear.getAttribute("YEAR_FROM").getInt());
                cmbFromModel.addElement(cmbData);
            }
            cmbFromYear.setSelectedIndex(0);
            
        } catch(Exception e) {
        }
    }
    
    private void showMessage(String pMessage) {
        lblStatus.setText(pMessage);
    }
}
